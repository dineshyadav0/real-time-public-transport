import { getApiBaseUrl } from '../config/api';
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
};

// ==========================================
// FALLBACK DATASETS ACROSS HARYANA ROADWAYS
// ==========================================

export const FALLBACK_BUSES: Bus[] = [
  {
    id: 1,
    busNumber: 'HR-12-8834',
    registrationNumber: 'HR-12-G-8834',
    busType: 'ORDINARY',
    capacity: 52,
    status: 'ACTIVE',
  },
  {
    id: 2,
    busNumber: 'HR-19-1042',
    registrationNumber: 'HR-19-B-1042',
    busType: 'SEMI_DELUXE',
    capacity: 48,
    status: 'ACTIVE',
  },
  {
    id: 3,
    busNumber: 'HR-16-2055',
    registrationNumber: 'HR-16-E-2055',
    busType: 'ORDINARY',
    capacity: 52,
    status: 'ACTIVE',
  },
  {
    id: 4,
    busNumber: 'HR-26-4012',
    registrationNumber: 'HR-26-H-4012',
    busType: 'VOLVO_AC',
    capacity: 45,
    status: 'ACTIVE',
  },
  {
    id: 5,
    busNumber: 'HR-20-3301',
    registrationNumber: 'HR-20-F-3301',
    busType: 'ORDINARY',
    capacity: 52,
    status: 'ACTIVE',
  },
];

export const FALLBACK_ROUTES: Route[] = [
  {
    id: 1,
    routeNumber: 'RTK-DEL-01',
    source: 'Rohtak',
    destination: 'Delhi ISBT',
    totalDistance: 72.5,
  },
  {
    id: 2,
    routeNumber: 'CD-BHW-02',
    source: 'Charkhi Dadri',
    destination: 'Bhiwani',
    totalDistance: 31.0,
  },
  {
    id: 3,
    routeNumber: 'CD-HSR-03',
    source: 'Charkhi Dadri',
    destination: 'Hisar',
    totalDistance: 94.2,
  },
  {
    id: 4,
    routeNumber: 'GGN-CDG-04',
    source: 'Gurugram',
    destination: 'Chandigarh ISBT',
    totalDistance: 278.0,
  },
  {
    id: 5,
    routeNumber: 'RTK-HSR-05',
    source: 'Rohtak',
    destination: 'Hisar',
    totalDistance: 98.4,
  },
];

export const FALLBACK_STOPS: BusStop[] = [
  {
    id: 1,
    name: 'Rohtak New Bus Stand',
    district: 'Rohtak',
    latitude: 28.8955,
    longitude: 76.6066,
  },
  {
    id: 2,
    name: 'Bahadurgarh Bus Stand',
    district: 'Jhajjar',
    latitude: 28.6924,
    longitude: 76.9242,
  },
  {
    id: 3,
    name: 'Delhi ISBT Kashmiri Gate',
    district: 'Delhi',
    latitude: 28.6675,
    longitude: 77.2285,
  },
  {
    id: 4,
    name: 'Charkhi Dadri Bus Stand',
    district: 'Charkhi Dadri',
    latitude: 28.5921,
    longitude: 76.2711,
  },
  {
    id: 5,
    name: 'Kitlana Bypass',
    district: 'Bhiwani',
    latitude: 28.712,
    longitude: 76.241,
  },
  {
    id: 6,
    name: 'Bhiwani New Bus Stand',
    district: 'Bhiwani',
    latitude: 28.7972,
    longitude: 76.1322,
  },
  {
    id: 7,
    name: 'Hisar General Bus Stand',
    district: 'Hisar',
    latitude: 29.1539,
    longitude: 75.7229,
  },
  {
    id: 8,
    name: 'Gurugram Bus Stand',
    district: 'Gurugram',
    latitude: 28.4595,
    longitude: 77.0266,
  },
  {
    id: 9,
    name: 'Chandigarh Sector 17 ISBT',
    district: 'Chandigarh',
    latitude: 30.7415,
    longitude: 76.7794,
  },
];

export const FALLBACK_TRIPS: Trip[] = [
  {
    id: 1,
    tripNumber: 'TRIP-RTK-01',
    bus: FALLBACK_BUSES[0],
    route: FALLBACK_ROUTES[0],
    departureTime: '2026-11-19T08:00:00',
    arrivalTime: '2026-11-19T10:15:00',
    status: 'RUNNING',
  },
  {
    id: 2,
    tripNumber: 'TRIP-CD-02',
    bus: FALLBACK_BUSES[1],
    route: FALLBACK_ROUTES[1],
    departureTime: '2026-11-19T09:30:00',
    arrivalTime: '2026-11-19T10:45:00',
    status: 'SCHEDULED',
  },
  {
    id: 3,
    tripNumber: 'TRIP-CD-03',
    bus: FALLBACK_BUSES[2],
    route: FALLBACK_ROUTES[2],
    departureTime: '2026-11-19T11:00:00',
    arrivalTime: '2026-11-19T13:30:00',
    status: 'SCHEDULED',
  },
  {
    id: 4,
    tripNumber: 'TRIP-GGN-04',
    bus: FALLBACK_BUSES[3],
    route: FALLBACK_ROUTES[3],
    departureTime: '2026-11-19T06:30:00',
    arrivalTime: '2026-11-19T12:00:00',
    status: 'RUNNING',
  },
];

export const FALLBACK_JOURNEYS: JourneySearchResponse[] = [
  {
    scheduleId: 101,
    routeNumber: 'RTK-DEL-01',
    routeHeading: 'Rohtak → Delhi ISBT',
    depotName: 'Rohtak Depot',
    source: 'Rohtak',
    destination: 'Delhi',
    via: 'Sampla, Bahadurgarh, Tikri',
    serviceType: 'ORDINARY',
    operatingDays: 'DAILY',
    travelDate: getTodayDateString(),
    boardingTime: '08:00:00',
    arrivalTime: '10:15:00',
    sourceDocument: 'Rohtak Timetable CSV',
    sourcePage: 1,
    verified: true,
  },
  {
    scheduleId: 102,
    routeNumber: 'CD-BHW-02',
    routeHeading: 'Charkhi Dadri → Bhiwani',
    depotName: 'Charkhi Dadri Sub-Depot',
    source: 'Charkhi Dadri',
    destination: 'Bhiwani',
    via: 'Kitlana',
    serviceType: 'ORDINARY',
    operatingDays: 'DAILY',
    travelDate: getTodayDateString(),
    boardingTime: '09:30:00',
    arrivalTime: '10:45:00',
    sourceDocument: 'Dadri Depot Roster',
    sourcePage: 2,
    verified: true,
  },
  {
    scheduleId: 103,
    routeNumber: 'CD-HSR-03',
    routeHeading: 'Charkhi Dadri → Hisar',
    depotName: 'Charkhi Dadri Sub-Depot',
    source: 'Charkhi Dadri',
    destination: 'Hisar',
    via: 'Bhiwani, Hansi',
    serviceType: 'SEMI_DELUXE',
    operatingDays: 'DAILY',
    travelDate: getTodayDateString(),
    boardingTime: '11:00:00',
    arrivalTime: '13:30:00',
    sourceDocument: 'Bhiwani Sector Time Table',
    sourcePage: 4,
    verified: true,
  },
  {
    scheduleId: 104,
    routeNumber: 'GGN-CDG-04',
    routeHeading: 'Gurugram → Chandigarh ISBT',
    depotName: 'Gurugram Depot',
    source: 'Gurugram',
    destination: 'Chandigarh',
    via: 'Delhi Bypass, Panipat, Karnal, Ambala',
    serviceType: 'VOLVO_AC',
    operatingDays: 'DAILY',
    travelDate: getTodayDateString(),
    boardingTime: '06:30:00',
    arrivalTime: '12:00:00',
    sourceDocument: 'Haryana Antyodaya Parivahan',
    sourcePage: 5,
    verified: true,
  },
];
