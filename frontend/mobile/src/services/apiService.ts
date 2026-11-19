import { getApiBaseUrl, getMlBaseUrl } from '../config/api';
import {
  Bus,
  Route,
  BusStop,
  JourneySearchResponse,
  Trip,
  GPSLocationPayload,
  GPSLocationResponse,
  UIBus,
} from '../types';

/**
 * Calculates Great-Circle Distance between two coordinates in kilometers (Haversine formula)
 */
export function calculateDistanceKm(
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number
): number {
  const R = 6371; // Earth's radius in km
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c;
}

export function getTodayDateString(): string {
  const d = new Date();
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// Stateful track of whether last network call reached the backend
let backendAvailable = false;

export const ApiService = {
  isBackendAvailable(): boolean {
    return backendAvailable;
  },

  async checkHealth(): Promise<boolean> {
    const url = `${getApiBaseUrl()}/health`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      backendAvailable = res.ok;
      return res.ok;
    } catch (_) {
      backendAvailable = false;
      return false;
    }
  },

  /**
   * GET /api/buses
   */
  async getBuses(): Promise<Bus[]> {
    const url = `${getApiBaseUrl()}/buses`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    backendAvailable = false;
    return FALLBACK_BUSES;
  },

  /**
   * GET /api/routes
   */
  async getRoutes(): Promise<Route[]> {
    const url = `${getApiBaseUrl()}/routes`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    backendAvailable = false;
    return FALLBACK_ROUTES;
  },

  /**
   * GET /api/stops
   */
  async getStops(): Promise<BusStop[]> {
    const url = `${getApiBaseUrl()}/stops`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    backendAvailable = false;
    return FALLBACK_STOPS;
  },

  /**
   * GET /api/schedules/journey?source={source}&destination={destination}&travelDate={YYYY-MM-DD}
   */
  async searchJourneys(
    source: string,
    destination: string,
    travelDate: string = getTodayDateString()
  ): Promise<JourneySearchResponse[]> {
    const cleanSource = encodeURIComponent(source.trim());
    const cleanDestination = encodeURIComponent(destination.trim());
    const url = `${getApiBaseUrl()}/schedules/journey?source=${cleanSource}&destination=${cleanDestination}&travelDate=${travelDate}`;

    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 4000);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}

    // Fallback search across local schedule records
    return FALLBACK_JOURNEYS.filter(
      (j) =>
        (j.source.toLowerCase().includes(source.toLowerCase()) ||
          source.toLowerCase().includes(j.source.toLowerCase())) &&
        (j.destination.toLowerCase().includes(destination.toLowerCase()) ||
          destination.toLowerCase().includes(j.destination.toLowerCase()))
    );
  },

  /**
   * GET /api/trips
   */
  async getTrips(): Promise<Trip[]> {
    const url = `${getApiBaseUrl()}/trips`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    backendAvailable = false;
    return FALLBACK_TRIPS;
  },

  /**
   * GET /api/trips/{id}
   */
  async getTripById(id: number): Promise<Trip | null> {
    const url = `${getApiBaseUrl()}/trips/${id}`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 3500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    return FALLBACK_TRIPS.find((t) => t.id === id) || null;
  },

  /**
   * PATCH /api/trips/{tripId}/start
   */
  async startTrip(tripId: number): Promise<Trip> {
    const url = `${getApiBaseUrl()}/trips/${tripId}/start`;
    const res = await fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!res.ok) {
      const errorText = await res.text().catch(() => '');
      throw new Error(
        `Failed to start trip (HTTP ${res.status}): ${errorText || 'Server rejected transition'}`
      );
    }
    backendAvailable = true;
    return await res.json();
  },

  /**
   * PATCH /api/trips/{tripId}/complete
   */
  async completeTrip(tripId: number): Promise<Trip> {
    const url = `${getApiBaseUrl()}/trips/${tripId}/complete`;
    const res = await fetch(url, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!res.ok) {
      const errorText = await res.text().catch(() => '');
      throw new Error(
        `Failed to complete trip (HTTP ${res.status}): ${errorText || 'Server rejected completion'}`
      );
    }
    backendAvailable = true;
    return await res.json();
  },

  /**
   * POST /api/gps
   */
  async sendGpsReading(
    tripId: number,
    latitude: number,
    longitude: number,
    speed: number
  ): Promise<GPSLocationResponse> {
    const payload: GPSLocationPayload = {
      trip: {
        id: tripId,
      },
      latitude: Number(latitude.toFixed(6)),
      longitude: Number(longitude.toFixed(6)),
      speed: Number(Math.max(0, speed).toFixed(1)),
    };

    const url = `${getApiBaseUrl()}/gps`;
    const res = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      const errorText = await res.text().catch(() => '');
      throw new Error(
        `GPS submission rejected (HTTP ${res.status}): ${errorText || 'Trip must be in RUNNING state'}`
      );
    }
    backendAvailable = true;
    return await res.json();
  },

  /**
   * GET /api/gps/trip/{tripId}/latest
   */
  async getLatestGps(tripId: number): Promise<GPSLocationResponse | null> {
    const url = `${getApiBaseUrl()}/gps/trip/${tripId}/latest`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2500);
      const res = await fetch(url, { signal: controller.signal });
      clearTimeout(timeoutId);
      if (res.ok) {
        backendAvailable = true;
        return await res.json();
      }
    } catch (_) {}
    return null;
  },

  /**
   * Calls AI/ML ETA Prediction microservice on POST http://{host}:5000/predict-eta
   */
  async getPredictedEta(
    distanceRemaining: number,
    avgSpeed: number = 30,
    stopsRemaining: number = 2
  ): Promise<number | null> {
    const url = `${getMlBaseUrl()}/predict-eta`;
    try {
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 2000);
      const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          distance_remaining: distanceRemaining,
          average_speed: avgSpeed,
          stops_remaining: stopsRemaining,
          traffic_level: 1,
          hour: new Date().getHours(),
          day_of_week: new Date().getDay(),
        }),
        signal: controller.signal,
      });
      clearTimeout(timeoutId);
      if (res.ok) {
        const data = await res.json();
        if (data.success && typeof data.estimated_eta_minutes === 'number') {
          return data.estimated_eta_minutes;
        }
      }
    } catch (_) {}
    return null;
  },
};

// ==========================================
// FALLBACK DATASETS ACROSS ALL 22 DISTRICTS OF HARYANA
// ==========================================

import transportData from '../data/haryana-transport-data.json';

export const FALLBACK_BUSES: Bus[] = transportData.buses.map((b, idx) => ({
  id: idx + 1,
  busNumber: b.busNumber,
  registrationNumber: b.busNumber,
  busType: b.busType as any,
  capacity: b.totalCapacity,
  status: 'ACTIVE',
}));

export const FALLBACK_ROUTES: Route[] = transportData.routes.map((r, idx) => ({
  id: idx + 1,
  routeNumber: r.routeCode,
  source: r.origin.replace(' Bus Stand', '').replace(' General Bus Stand', '').replace(' Main Bus Stand', '').replace(' New Bus Stand', ''),
  destination: r.destination.replace(' Bus Stand', '').replace(' General Bus Stand', '').replace(' Main Bus Stand', '').replace(' New Bus Stand', ''),
  totalDistance: r.distanceKm,
}));

export const FALLBACK_STOPS: BusStop[] = transportData.stops.map((s, idx) => ({
  id: idx + 1,
  name: s.name,
  district: s.city,
  latitude: s.latitude,
  longitude: s.longitude,
}));

export const FALLBACK_TRIPS: Trip[] = transportData.trips.map((t, idx) => {
  const bus = FALLBACK_BUSES[idx % FALLBACK_BUSES.length];
  const route = FALLBACK_ROUTES[idx % FALLBACK_ROUTES.length];
  const now = new Date();
  const depTime = new Date(now.getTime() - 25 * 60000).toISOString();
  const arrTime = new Date(now.getTime() + 65 * 60000).toISOString();

  return {
    id: idx + 1,
    tripNumber: t.id.toUpperCase(),
    bus,
    route,
    departureTime: depTime,
    arrivalTime: arrTime,
    status: t.status === 'ACTIVE' ? 'RUNNING' : 'SCHEDULED',
  };
});

export const FALLBACK_JOURNEYS: JourneySearchResponse[] = transportData.routes.map((r, idx) => ({
  scheduleId: idx + 1,
  routeNumber: r.routeCode,
  routeHeading: `${r.origin} - ${r.destination}`,
  depotName: `${r.majorCities[0]} Depot`,
  source: r.origin,
  destination: r.destination,
  via: r.majorCities.slice(1, -1).join(', '),
  serviceType: 'ORDINARY',
  operatingDays: 'ALL_DAYS',
  travelDate: getTodayDateString(),
  boardingTime: '07:30:00',
  arrivalTime: '10:45:00',
  verified: true,
}));
