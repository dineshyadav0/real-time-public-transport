import { getWebSocketUrl, getSockJsUrl } from '../config/api';
import { LiveLocationUpdate } from '../types';

export type WebSocketStatus =
  | 'DISCONNECTED'
  | 'CONNECTING'
  | 'CONNECTED'
  | 'RECONNECTING'
  | 'ERROR';

export type LocationCallback = (update: LiveLocationUpdate) => void;
export type StatusCallback = (status: WebSocketStatus, error?: string) => void;

/**
 * Robust STOMP-over-WebSocket Client for React Native
 * Native implementation compatible with Spring Boot /ws STOMP endpoints
 */
class STOMPWebSocketClient {
  private socket: WebSocket | null = null;
  private currentTripId: number | null = null;
  private onLocationUpdate: LocationCallback | null = null;
  private onStatusChange: StatusCallback | null = null;
  private status: WebSocketStatus = 'DISCONNECTED';
  private reconnectTimer: any = null;
  private heartbeatTimer: any = null;
  private shouldStayConnected = false;
  private subscriptionId = 'sub-0';

  public getStatus(): WebSocketStatus {
    return this.status;
  }

  private setStatus(newStatus: WebSocketStatus, errorMsg?: string) {
    this.status = newStatus;
    if (this.onStatusChange) {
      this.onStatusChange(newStatus, errorMsg);
    }
  }

  /**
   * Subscribe to live updates for a specific trip
   */
  public subscribeToTrip(
    tripId: number,
    onLocation: LocationCallback,
    onStatus?: StatusCallback
  ): void {
    // If already subscribed to this trip and connected, just update callbacks
    if (this.currentTripId === tripId && this.status === 'CONNECTED') {
      this.onLocationUpdate = onLocation;
      if (onStatus) this.onStatusChange = onStatus;
      this.setStatus('CONNECTED');
      return;
    }

    this.disconnect();

    this.currentTripId = tripId;
    this.onLocationUpdate = onLocation;
    this.onStatusChange = onStatus || null;
    this.shouldStayConnected = true;

    this.connect();
  }

  /**
   * Unsubscribe and cleanly disconnect
   */
  public disconnect(): void {
    this.shouldStayConnected = false;
    this.clearTimers();

    if (this.socket) {
      try {
        if (this.socket.readyState === WebSocket.OPEN) {
          // Send STOMP UNSUBSCRIBE & DISCONNECT
          this.socket.send(`UNSUBSCRIBE\nid:${this.subscriptionId}\n\n\0`);
          this.socket.send(`DISCONNECT\n\n\0`);
        }
        this.socket.close();
      } catch (_) {}
      this.socket = null;
    }

    this.currentTripId = null;
    this.setStatus('DISCONNECTED');
  }

  private clearTimers() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
  }

  private connect(): void {
    if (!this.shouldStayConnected || !this.currentTripId) return;

    this.clearTimers();
    this.setStatus('CONNECTING');

    const wsUrl = getWebSocketUrl();

    try {
      this.socket = new WebSocket(wsUrl);

      this.socket.onopen = () => {
        // Send STOMP CONNECT frame
        const connectFrame =
          'CONNECT\n' +
          'accept-version:1.1,1.2\n' +
          'host:localhost\n' +
          'heart-beat:10000,10000\n\n\0';
        this.socket?.send(connectFrame);
      };

      this.socket.onmessage = (event) => {
        this.handleSocketMessage(event.data);
      };

      this.socket.onerror = (e) => {
        this.setStatus('ERROR', 'WebSocket connection failed');
      };

      this.socket.onclose = () => {
        if (this.shouldStayConnected) {
          this.setStatus('RECONNECTING');
          this.reconnectTimer = setTimeout(() => {
            this.connect();
          }, 3000);
        } else {
          this.setStatus('DISCONNECTED');
        }
      };
    } catch (err: any) {
      this.setStatus('ERROR', err?.message || 'Failed to initialize WebSocket');
      if (this.shouldStayConnected) {
        this.reconnectTimer = setTimeout(() => {
          this.connect();
        }, 3500);
      }
    }
  }

  private handleSocketMessage(rawData: any): void {
    if (typeof rawData !== 'string') return;

    // Handle heartbeats (newline ping)
    if (rawData === '\n' || rawData === '\r\n') {
      return;
    }

    // Parse STOMP frames (separated by null character)
    const frames = rawData.split('\0');
    for (const frame of frames) {
      const trimmed = frame.trim();
      if (!trimmed) continue;

      const lines = trimmed.split('\n');
      const command = lines[0].trim();

      if (command === 'CONNECTED') {
        this.setStatus('CONNECTED');

        // Start heartbeat ping
        this.heartbeatTimer = setInterval(() => {
          if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send('\n');
          }
        }, 10000);

        // Send SUBSCRIBE frame for /topic/trips/{tripId}
        if (this.currentTripId) {
          this.subscriptionId = `sub-${this.currentTripId}-${Date.now()}`;
          const subFrame =
            `SUBSCRIBE\n` +
            `id:${this.subscriptionId}\n` +
            `destination:/topic/trips/${this.currentTripId}\n\n\0`;
          this.socket?.send(subFrame);
        }
      } else if (command === 'MESSAGE') {
        // Extract body after double newline
        const bodyIndex = frame.indexOf('\n\n');
        if (bodyIndex !== -1) {
          const body = frame.slice(bodyIndex + 2).replace(/\0$/, '').trim();
          try {
            const parsed = JSON.parse(body);
            if (
              parsed &&
              typeof parsed.latitude === 'number' &&
              typeof parsed.longitude === 'number'
            ) {
              const locationUpdate: LiveLocationUpdate = {
                tripId: parsed.tripId ?? this.currentTripId ?? 0,
                latitude: parsed.latitude,
                longitude: parsed.longitude,
                speed: parsed.speed ?? 0,
                recordedAt: parsed.recordedAt ?? new Date().toISOString(),
              };

              if (this.onLocationUpdate) {
                this.onLocationUpdate(locationUpdate);
              }
            }
          } catch (err) {
            console.warn('Failed to parse STOMP message body:', err);
          }
        }
      } else if (command === 'ERROR') {
        console.warn('STOMP Error frame received:', frame);
        this.setStatus('ERROR', 'STOMP error from backend');
      }
    }
  }
}

export const WebSocketService = new STOMPWebSocketClient();
