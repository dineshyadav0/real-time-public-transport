import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import WelcomeScreen from '../screens/WelcomeScreen';
import PassengerHomeScreen from '../screens/passenger/PassengerHomeScreen';
import DriverHomeScreen from '../screens/driver/DriverHomeScreen';

export type RootStackParamList = {
  Welcome: undefined;
  PassengerHome: undefined;
  DriverHome: undefined;
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
    </Stack.Navigator>
  );
}