/**
 * Domain types matching Haryana Roadways Spring Boot Backend
 */

export interface Bus {
  id: number;
  busNumber: string;
  registrationNumber: string;
  busType: 'ORDINARY' | 'SEMI_DELUXE' | 'DELUXE' | 'VOLVO_AC' | 'HVAC' | string;
  capacity: number;
  status: 'ACTIVE' | 'MAINTENANCE' | 'OFF_DUTY' | string;
}

export interface Route {
  id: number;
  routeNumber: string;
  source: string;
  destination: string;
  totalDistance?: number | null;
}

export interface BusStop {
  id: number;
  name: string;
  district: string;
  latitude: number | null;
  longitude: number | null;
}

export interface JourneySearchResponse {
  scheduleId: number;
  routeNumber: string;
  routeHeading: string;
  depotName: string;
  source: string;
  destination: string;
  via?: string | null;
  serviceType: string;
  operatingDays: string;
  travelDate: string;
  boardingTime: string;
  arrivalTime: string | null;
  sourceDocument?: string | null;
  sourcePage?: number | null;
  verified: boolean;
}

export type TripStatus = 'SCHEDULED' | 'RUNNING' | 'COMPLETED' | 'CANCELLED';

export interface Trip {
  id: number;
  tripNumber: string;
  bus: Bus;
  route: Route;
  departureTime: string;
  arrivalTime: string;
  status: TripStatus;
}

export interface GPSLocationPayload {
  trip: {
    id: number;
  };
  latitude: number;
  longitude: number;
  speed: number;
}

export interface GPSLocationResponse {
  id: number;
  tripId: number;
  latitude: number;
  longitude: number;
  speed: number;
  recordedAt: string;
}

export interface LiveLocationUpdate {
  tripId: number;
  latitude: number;
  longitude: number;
  speed: number;
  recordedAt: string;
}

// Mobile UI Extended Bus representation combining backend entities
export interface UIBus {
  id: number;
  number: string;
  registrationNumber: string;
  routeNumber: string;
  routeHeading: string;
  origin: string;
  destination: string;
  depot: string;
  serviceType: string;
  status: 'LIVE' | 'SCHEDULED' | 'OFFLINE';
  tripId?: number;
  currentLatitude?: number;
  currentLongitude?: number;
  speed?: number;
  lastUpdated?: string;
  etaMinutes?: number;
  distanceKm?: number;
  occupancyPercent?: number;
  delayMinutes?: number;
  catchChancePercent?: number;
  walkingMinutes?: number;
  stops: {
    name: string;
    latitude: number;
    longitude: number;
    time?: string;
  }[];
  verified: boolean;
}
