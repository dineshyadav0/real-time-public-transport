import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import WelcomeScreen from '../screens/WelcomeScreen';
import PassengerHomeScreen from '../screens/passenger/PassengerHomeScreen';
import DriverHomeScreen from '../screens/driver/DriverHomeScreen';
import BusDetailsScreen from '../screens/passenger/BusDetailsScreen';
export type BusData = {
  number: string;
  route: string;
  eta: string;
  distance: string;
  occupancy: number;
  delay: number;
  walking: string;
  catchChance: number;
};

export type RootStackParamList = {
  Welcome: undefined;
  PassengerHome: undefined;
  DriverHome: undefined;
  BusDetails: {
    bus: BusData;
  };
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
    </Stack.Navigator>
  );
}