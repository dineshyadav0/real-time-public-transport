import React from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';

import { RootStackParamList } from '../navigation/AppNavigator';

type Props = NativeStackScreenProps<RootStackParamList, 'Welcome'>;

export default function WelcomeScreen({ navigation }: Props) {
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.emoji}>🚌</Text>

        <Text style={styles.title}>TransitIQ</Text>

        <Text style={styles.subtitle}>
          Real-time public transport intelligence
        </Text>

        <Text style={styles.description}>
          Track buses, predict arrival times, understand crowd levels,
          and make smarter travel decisions.
        </Text>

        <View style={styles.spacer} />

        <Text style={styles.question}>How are you using TransitIQ?</Text>

        <TouchableOpacity
          style={styles.primaryButton}
          onPress={() => navigation.navigate('PassengerHome')}
        >
          <Text style={styles.buttonTitle}>I'm a Passenger</Text>
          <Text style={styles.buttonSubtitle}>
            Find buses & plan my journey
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.secondaryButton}
          onPress={() => navigation.navigate('DriverHome')}
        >
          <Text style={styles.secondaryButtonTitle}>I'm a Driver</Text>
          <Text style={styles.secondaryButtonSubtitle}>
            Start a trip & share my location
          </Text>
        </TouchableOpacity>
      </View>

      <Text style={styles.footer}>Predict the journey. Not just the bus.</Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F7F9FC',
  },

  content: {
    flex: 1,
    paddingHorizontal: 24,
    paddingTop: 50,
  },

  emoji: {
    fontSize: 58,
    marginBottom: 18,
  },

  title: {
    fontSize: 42,
    fontWeight: '800',
    color: '#111827',
    letterSpacing: -1,
  },

  subtitle: {
    marginTop: 8,
    fontSize: 19,
    fontWeight: '600',
    color: '#374151',
    lineHeight: 27,
  },

  description: {
    marginTop: 18,
    fontSize: 15,
    lineHeight: 23,
    color: '#6B7280',
    maxWidth: 350,
  },

  spacer: {
    flex: 1,
    minHeight: 40,
  },

  question: {
    fontSize: 17,
    fontWeight: '700',
    color: '#111827',
    marginBottom: 14,
  },

  primaryButton: {
    backgroundColor: '#111827',
    borderRadius: 18,
    paddingVertical: 17,
    paddingHorizontal: 20,
    marginBottom: 12,
  },

  buttonTitle: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '700',
  },

  buttonSubtitle: {
    color: '#D1D5DB',
    fontSize: 13,
    marginTop: 4,
  },

  secondaryButton: {
    backgroundColor: '#FFFFFF',
    borderRadius: 18,
    paddingVertical: 17,
    paddingHorizontal: 20,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  secondaryButtonTitle: {
    color: '#111827',
    fontSize: 17,
    fontWeight: '700',
  },

  secondaryButtonSubtitle: {
    color: '#6B7280',
    fontSize: 13,
    marginTop: 4,
  },

  footer: {
    textAlign: 'center',
    color: '#9CA3AF',
    fontSize: 12,
    marginBottom: 18,
  },
});