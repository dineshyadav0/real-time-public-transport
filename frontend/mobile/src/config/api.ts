import Constants from 'expo-constants';

/**
 * Centralized API & WebSocket Configuration
 * Haryana Roadways Real-Time Public Transport Tracking System
 */

// Default port configured on Spring Boot backend
export const BACKEND_PORT = 8081;

// Default port for Python/Flask ML ETA microservice
export const ML_PORT = 5000;

// Default fallback IP of the development workstation on local Wi-Fi LAN
export const DEFAULT_LAN_IP = '192.168.1.24';

let customServerHost: string | null = null;

/**
 * Resolves the backend host dynamically:
 * 1. Custom host override if set by user in developer settings
 * 2. Host IP extracted from Expo debugger / Metro server (automatically matches Wi-Fi LAN IP)
 * 3. Workstation LAN IP fallback (192.168.1.24)
 * 4. Localhost fallback
 */
export function resolveServerHost(): string {
  if (customServerHost && customServerHost.trim().length > 0) {
    return customServerHost.trim();
  }

  try {
    const hostUri =
      Constants.expoConfig?.hostUri ||
      (Constants as any).manifest?.debuggerHost ||
      (Constants as any).manifest2?.extra?.expoClient?.hostUri;

    if (hostUri) {
      const ip = hostUri.split(':')[0];
      if (ip && ip !== 'localhost' && ip !== '127.0.0.1') {
        return ip;
      }
    }
  } catch (_) {
    // Ignore and fallback
  }

  return DEFAULT_LAN_IP;
}

export function setCustomServerHost(host: string | null): void {
  customServerHost = host;
}

export function getCustomServerHost(): string | null {
  return customServerHost;
}

export function getApiBaseUrl(): string {
  const host = resolveServerHost();
  return `http://${host}:${BACKEND_PORT}/api`;
}

export function getWebSocketUrl(): string {
  const host = resolveServerHost();
  return `ws://${host}:${BACKEND_PORT}/ws/websocket`;
}

export function getSockJsUrl(): string {
  const host = resolveServerHost();
  return `http://${host}:${BACKEND_PORT}/ws`;
}

export function getMlBaseUrl(): string {
  const host = resolveServerHost();
  return `http://${host}:${ML_PORT}`;
}

export const API_CONFIG = {
  get baseUrl() {
    return getApiBaseUrl();
  },
  get wsUrl() {
    return getWebSocketUrl();
  },
  get sockJsUrl() {
    return getSockJsUrl();
  },
  get mlUrl() {
    return getMlBaseUrl();
  },
  port: BACKEND_PORT,
  mlPort: ML_PORT,
  defaultLanIp: DEFAULT_LAN_IP,
};
