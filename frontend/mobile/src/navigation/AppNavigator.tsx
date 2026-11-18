import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import WelcomeScreen from '../screens/WelcomeScreen';
import PassengerHomeScreen from '../screens/passenger/PassengerHomeScreen';
import DriverHomeScreen from '../screens/driver/DriverHomeScreen';
import BusDetailsScreen from '../screens/passenger/BusDetailsScreen';
import ActiveTripScreen from '../screens/driver/ActiveTripScreen';

export type BusData = {
  id?: number;
  tripId?: number;
  number: string;
  registrationNumber?: string;
  route: string;
  routeNumber?: string;
  eta: string;
  distance: string;
  occupancy: number;
  delay: number;
  walking: string;
  catchChance: number;
  origin: string;
  destination: string;
  depot?: string;
  serviceType?: string;
  status?: 'LIVE' | 'SCHEDULED' | 'OFFLINE';
  stops: {
    name: string;
    latitude: number;
    longitude: number;
    time?: string;
  }[];
  dataType?: string;
  currentLatitude?: number;
  currentLongitude?: number;
  verified?: boolean;
};

export type ActiveTripParams = {
  tripId: number;
  tripNumber: string;
  busNumber: string;
  registrationNumber: string;
  source: string;
  destination: string;
  distanceKm?: number;
};

export type RootStackParamList = {
  Welcome: undefined;
  PassengerHome: undefined;
  DriverHome: undefined;
  BusDetails: {
    bus: BusData;
  };
  ActiveTrip: ActiveTripParams | undefined;
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export default function AppNavigator() {
  return (
    <Stack.Navigator
      initialRouteName="Welcome"
      screenOptions={{
        headerShown: false,
      }}
    >
      <Stack.Screen name="Welcome" component={WelcomeScreen} />
      <Stack.Screen name="PassengerHome" component={PassengerHomeScreen} />
      <Stack.Screen name="DriverHome" component={DriverHomeScreen} />
      <Stack.Screen name="BusDetails" component={BusDetailsScreen} />
      <Stack.Screen name="ActiveTrip" component={ActiveTripScreen} />
    </Stack.Navigator>
  );
}
