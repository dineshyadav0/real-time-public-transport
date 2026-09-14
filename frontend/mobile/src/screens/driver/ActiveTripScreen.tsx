import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';
import * as Location from 'expo-location';

const calculateDistance = (
  lat1: number,
  lon1: number,
  lat2: number,
  lon2: number,
) => {
  const R = 6371;

  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLon = ((lon2 - lon1) * Math.PI) / 180;

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) *
      Math.cos((lat2 * Math.PI) / 180) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);

  const c =
    2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return R * c;
};

export default function ActiveTripScreen({ navigation }: any) {
  const [location, setLocation] =
    useState<Location.LocationObject | null>(null);

  const [distance, setDistance] = useState(0);
  const [gpsStatus, setGpsStatus] = useState('Starting GPS...');

  useEffect(() => {
    let subscription: Location.LocationSubscription | null = null;

    const startTracking = async () => {
      try {
        const { status } =
          await Location.requestForegroundPermissionsAsync();

        if (status !== 'granted') {
          setGpsStatus('Location permission denied');
          return;
        }

        setGpsStatus('GPS tracking active');

        const initialLocation =
          await Location.getCurrentPositionAsync({
            accuracy: Location.Accuracy.High,
          });

        setLocation(initialLocation);

        subscription = await Location.watchPositionAsync(
          {
            accuracy: Location.Accuracy.High,
            timeInterval: 3000,
            distanceInterval: 5,
          },
          (newLocation) => {
            setLocation((previousLocation) => {
              if (previousLocation) {
                const addedDistance = calculateDistance(
                  previousLocation.coords.latitude,
                  previousLocation.coords.longitude,
                  newLocation.coords.latitude,
                  newLocation.coords.longitude,
                );

                setDistance((previousDistance) =>
                  previousDistance + addedDistance,
                );
              }

              return newLocation;
            });
          },
        );
      } catch (error) {
        console.error('GPS error:', error);
        setGpsStatus('Unable to access GPS');
      }
    };

    startTracking();

    return () => {
      if (subscription) {
        subscription.remove();
      }
    };
  }, []);

  const speed = location?.coords.speed
    ? Math.max(0, location.coords.speed * 3.6)
    : 0;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>

        <Text style={styles.status}>●  TRIP ACTIVE</Text>

        <Text style={styles.title}>Bus 102</Text>

        <Text style={styles.route}>
          City Center → Railway Station
        </Text>

        <View style={styles.gpsCard}>
          <Text style={styles.gpsIcon}>📡</Text>

          <Text style={styles.gpsTitle}>
            GPS tracking active
          </Text>

          <Text style={styles.gpsText}>
  {gpsStatus}
</Text>

          <View style={styles.locationBox}>
            <Text style={styles.locationLabel}>
              CURRENT LOCATION
            </Text>

            <Text style={styles.locationValue}>
  {location
    ? `${location.coords.latitude.toFixed(6)}, ${location.coords.longitude.toFixed(6)}`
    : gpsStatus}
</Text>
          </View>
        </View>

        <View style={styles.statsRow}>
          <View style={styles.stat}>
            <Text style={styles.statValue}>
  {distance.toFixed(2)} km
</Text>
            <Text style={styles.statLabel}>Distance</Text>
          </View>

          <View style={styles.stat}>
            <Text style={styles.statValue}>0</Text>
            <Text style={styles.statLabel}>Stops passed</Text>
          </View>

          <View style={styles.stat}>
    <Text style={styles.statValue}>
  {speed.toFixed(1)} km/h
</Text>
            <Text style={styles.statLabel}>Speed</Text>
          </View>
        </View>

        <View style={styles.spacer} />

        <TouchableOpacity
          style={styles.endButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.endButtonText}>
            End Trip
          </Text>
        </TouchableOpacity>

        <Text style={styles.footer}>
          GPS tracking stops when the trip ends.
        </Text>

      </View>
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
    padding: 20,
  },

  status: {
    fontSize: 12,
    fontWeight: '800',
    color: '#059669',
    letterSpacing: 0.5,
    marginTop: 15,
  },

  title: {
    fontSize: 36,
    fontWeight: '800',
    color: '#111827',
    marginTop: 12,
  },

  route: {
    fontSize: 14,
    color: '#6B7280',
    marginTop: 5,
  },

  gpsCard: {
    marginTop: 25,
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 20,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  gpsIcon: {
    fontSize: 32,
  },

  gpsTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: '#111827',
    marginTop: 12,
  },

  gpsText: {
    fontSize: 12,
    color: '#6B7280',
    lineHeight: 18,
    marginTop: 5,
  },

  locationBox: {
    marginTop: 18,
    backgroundColor: '#F3F4F6',
    borderRadius: 14,
    padding: 14,
  },

  locationLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.7,
  },

  locationValue: {
    fontSize: 14,
    fontWeight: '700',
    color: '#374151',
    marginTop: 5,
  },

  statsRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 12,
  },

  stat: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  statValue: {
    fontSize: 15,
    fontWeight: '800',
    color: '#111827',
  },

  statLabel: {
    fontSize: 9,
    color: '#9CA3AF',
    marginTop: 5,
  },

  spacer: {
    flex: 1,
  },

  endButton: {
    height: 56,
    borderRadius: 18,
    backgroundColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  endButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '800',
  },

  footer: {
    textAlign: 'center',
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 10,
    marginBottom: 5,
  },
});