import React, { useEffect, useState, useRef } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
  ActivityIndicator,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import * as Location from 'expo-location';
import { RootStackParamList } from '../../navigation/AppNavigator';
import { ApiService, calculateDistanceKm } from '../../services/apiService';

type Props = NativeStackScreenProps<RootStackParamList, 'ActiveTrip'>;

export default function ActiveTripScreen({ route, navigation }: Props) {
  const params = route.params;
  const tripId = params?.tripId ?? 1;
  const tripNumber = params?.tripNumber ?? 'TRIP-01';
  const busNumber = params?.busNumber ?? 'HR-12-8834';
  const source = params?.source ?? 'Rohtak';
  const destination = params?.destination ?? 'Delhi';

  // Live Location & telemetry states
  const [currentLocation, setCurrentLocation] =
    useState<Location.LocationObject | null>(null);
  const [distanceKm, setDistanceKm] = useState(0);
  const [gpsStatus, setGpsStatus] = useState('Acquiring satellite GPS lock...');
  const [packetCount, setPacketCount] = useState(0);
  const [lastSubmittedTime, setLastSubmittedTime] = useState<string>('--');
  const [isEndingTrip, setIsEndingTrip] = useState(false);
  const [elapsedSeconds, setElapsedSeconds] = useState(0);

  // References to prevent duplicate subscriptions and cleanly tear down
  const locationSubscriptionRef = useRef<Location.LocationSubscription | null>(null);
  const isTripCompletedRef = useRef(false);
  const previousCoordsRef = useRef<{ latitude: number; longitude: number } | null>(null);

  // Elapsed timer
  useEffect(() => {
    const timer = setInterval(() => {
      setElapsedSeconds((sec) => sec + 1);
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  // Format seconds to HH:MM:SS
  const formatTimer = (totalSeconds: number): string => {
    const hrs = Math.floor(totalSeconds / 3600);
    const mins = Math.floor((totalSeconds % 3600) / 60);
    const secs = totalSeconds % 60;
    return `${hrs > 0 ? `${hrs}:` : ''}${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  };

  /**
   * Start high-precision GPS tracking and stream to POST /api/gps
   */
  useEffect(() => {
    let isMounted = true;

    const initTracking = async () => {
      try {
        const { status } = await Location.requestForegroundPermissionsAsync();
        if (status !== 'granted') {
          if (isMounted) {
            setGpsStatus('Location permission denied by driver');
          }
          Alert.alert(
            'GPS Permission Denied',
            'Location access is required to stream real-time coordinates to Haryana Roadways dispatch and passengers.'
          );
          return;
        }

        if (isMounted) {
          setGpsStatus('GPS locked • Streaming coordinates');
        }

        // Get initial location fix
        const initialLoc = await Location.getCurrentPositionAsync({
          accuracy: Location.Accuracy.High,
        });

        if (isMounted && initialLoc) {
          setCurrentLocation(initialLoc);
          previousCoordsRef.current = {
            latitude: initialLoc.coords.latitude,
            longitude: initialLoc.coords.longitude,
          };

          // Send first GPS reading to backend
          sendReadingToBackend(
            initialLoc.coords.latitude,
            initialLoc.coords.longitude,
            Math.max(0, (initialLoc.coords.speed || 0) * 3.6)
          );
        }

        // Clean up previous subscription if any
        if (locationSubscriptionRef.current) {
          locationSubscriptionRef.current.remove();
          locationSubscriptionRef.current = null;
        }

        // Watch position continuously while trip is running
        const sub = await Location.watchPositionAsync(
          {
            accuracy: Location.Accuracy.High,
            timeInterval: 3000, // Every 3 seconds
            distanceInterval: 5, // Or 5 meters
          },
          (newLoc) => {
            if (!isMounted || isTripCompletedRef.current) return;

            setCurrentLocation(newLoc);

            // Compute distance delta
            if (previousCoordsRef.current) {
              const delta = calculateDistanceKm(
                previousCoordsRef.current.latitude,
                previousCoordsRef.current.longitude,
                newLoc.coords.latitude,
                newLoc.coords.longitude
              );
              if (delta > 0.002) {
                setDistanceKm((prev) => prev + delta);
              }
            }
            previousCoordsRef.current = {
              latitude: newLoc.coords.latitude,
              longitude: newLoc.coords.longitude,
            };

            const speedKmh = Math.max(0, (newLoc.coords.speed || 0) * 3.6);

            // Transmit to backend
            sendReadingToBackend(
              newLoc.coords.latitude,
              newLoc.coords.longitude,
              speedKmh
            );
          }
        );

        locationSubscriptionRef.current = sub;
      } catch (err: any) {
        if (isMounted) {
          setGpsStatus(`GPS error: ${err?.message || 'Signal lost'}`);
        }
      }
    };

    initTracking();

    return () => {
      isMounted = false;
      if (locationSubscriptionRef.current) {
        locationSubscriptionRef.current.remove();
        locationSubscriptionRef.current = null;
      }
    };
  }, [tripId]);

  // Transmit location update to POST /api/gps
  const sendReadingToBackend = async (
    lat: number,
    lng: number,
    speedKmh: number
  ) => {
    if (isTripCompletedRef.current) return;

    try {
      await ApiService.sendGpsReading(tripId, lat, lng, speedKmh);
      setPacketCount((c) => c + 1);
      setLastSubmittedTime(new Date().toLocaleTimeString());
      setGpsStatus('Active • Coordinates broadcasted');
    } catch (err: any) {
      console.warn('GPS transmission error:', err?.message);
      setGpsStatus(`Transmission alert: ${err?.message || 'Network delay'}`);
    }
  };

  /**
   * Driver ends the trip:
   * 1. Calls PATCH /api/trips/{tripId}/complete on backend
   * 2. Stops GPS tracking after successful completion
   * 3. Prevents further GPS submissions
   * 4. Returns to Driver Home
   */
  const handleEndTrip = () => {
    Alert.alert(
      'Complete Trip',
      `Are you sure you want to complete ${tripNumber} (${source} → ${destination})? GPS tracking will stop immediately.`,
      [
        {
          text: 'Cancel',
          style: 'cancel',
        },
        {
          text: 'End Trip Now',
          style: 'destructive',
          onPress: async () => {
            setIsEndingTrip(true);
            try {
              // 1. Mark completed in state & tear down watcher first
              isTripCompletedRef.current = true;
              if (locationSubscriptionRef.current) {
                locationSubscriptionRef.current.remove();
                locationSubscriptionRef.current = null;
              }

              // 2. Call backend lifecycle endpoint: PATCH /api/trips/{tripId}/complete
              await ApiService.completeTrip(tripId);

              Alert.alert(
                'Trip Completed',
                `Trip ${tripNumber} has ended successfully. Total distance: ${distanceKm.toFixed(2)} km.`,
                [
                  {
                    text: 'OK',
                    onPress: () => navigation.goBack(),
                  },
                ]
              );
            } catch (err: any) {
              Alert.alert(
                'End Trip Error',
                err?.message ||
                  'Backend rejected completion. Check server connection.'
              );
              setIsEndingTrip(false);
            }
          },
        },
      ]
    );
  };

  const speedKmh = currentLocation?.coords.speed
    ? Math.max(0, currentLocation.coords.speed * 3.6)
    : 0;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        {/* Active badge */}
        <View style={styles.statusHeader}>
          <View style={styles.statusDot} />
          <Text style={styles.statusText}>TRIP ACTIVE • BROADCASTING GPS</Text>
        </View>

        {/* Bus & Route Titles */}
        <Text style={styles.busTitle}>BUS {busNumber}</Text>
        <Text style={styles.routeSubtitle}>
          {source} → {destination}
        </Text>

        {/* GPS Card */}
        <View style={styles.gpsCard}>
          <View style={styles.gpsCardTop}>
            <Text style={styles.gpsIcon}>📡</Text>
            <View style={{ flex: 1, marginLeft: 12 }}>
              <Text style={styles.gpsCardTitle}>Live GPS Telemetry</Text>
              <Text style={styles.gpsCardStatus}>{gpsStatus}</Text>
            </View>
          </View>

          {/* Current Coordinates Box */}
          <View style={styles.locationBox}>
            <Text style={styles.locationLabel}>COORDINATES (LAT, LNG)</Text>
            <Text style={styles.locationValue}>
              {currentLocation
                ? `${currentLocation.coords.latitude.toFixed(6)}, ${currentLocation.coords.longitude.toFixed(6)}`
                : 'Acquiring satellite coordinates...'}
            </Text>
          </View>

          {/* Telemetry packet details */}
          <View style={styles.telemetryRow}>
            <Text style={styles.telemetryText}>
              Trip ID: #{tripId} • Packets sent: {packetCount}
            </Text>
            <Text style={styles.telemetryText}>
              Last broadcast: {lastSubmittedTime}
            </Text>
          </View>
        </View>

        {/* Real-time stats row: Distance, Speed, Elapsed Time */}
        <View style={styles.statsRow}>
          <View style={styles.statBox}>
            <Text style={styles.statValue}>{distanceKm.toFixed(2)} km</Text>
            <Text style={styles.statLabel}>Distance</Text>
          </View>

          <View style={styles.statBox}>
            <Text style={styles.statValue}>{speedKmh.toFixed(1)} km/h</Text>
            <Text style={styles.statLabel}>Current Speed</Text>
          </View>

          <View style={styles.statBox}>
            <Text style={styles.statValue}>{formatTimer(elapsedSeconds)}</Text>
            <Text style={styles.statLabel}>Trip Duration</Text>
          </View>
        </View>

        {/* Spacer */}
        <View style={styles.spacer} />

        {/* End Trip Button */}
        <TouchableOpacity
          style={[styles.endButton, isEndingTrip && styles.buttonDisabled]}
          activeOpacity={0.85}
          onPress={handleEndTrip}
          disabled={isEndingTrip}
        >
          {isEndingTrip ? (
            <ActivityIndicator color="#FFFFFF" size="small" />
          ) : (
            <Text style={styles.endButtonText}>End Trip & Stop GPS</Text>
          )}
        </TouchableOpacity>

        <Text style={styles.footerNote}>
          Calls PATCH /api/trips/{tripId}/complete and halts location sharing.
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

  statusHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 10,
    backgroundColor: '#ECFDF5',
    alignSelf: 'flex-start',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: '#A7F3D0',
  },

  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    backgroundColor: '#10B981',
    marginRight: 8,
  },

  statusText: {
    fontSize: 10,
    fontWeight: '800',
    color: '#059669',
    letterSpacing: 0.6,
  },

  busTitle: {
    fontSize: 32,
    fontWeight: '800',
    color: '#111827',
    marginTop: 14,
  },

  routeSubtitle: {
    fontSize: 15,
    color: '#4B5563',
    fontWeight: '600',
    marginTop: 4,
  },

  gpsCard: {
    marginTop: 20,
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 18,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  gpsCardTop: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  gpsIcon: {
    fontSize: 28,
  },

  gpsCardTitle: {
    fontSize: 15,
    fontWeight: '800',
    color: '#111827',
  },

  gpsCardStatus: {
    fontSize: 11,
    color: '#059669',
    fontWeight: '600',
    marginTop: 2,
  },

  locationBox: {
    marginTop: 14,
    backgroundColor: '#F3F4F6',
    borderRadius: 12,
    padding: 12,
  },

  locationLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.6,
  },

  locationValue: {
    fontSize: 13,
    fontWeight: '700',
    color: '#1F2937',
    marginTop: 4,
  },

  telemetryRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 12,
    paddingTop: 10,
    borderTopWidth: 1,
    borderTopColor: '#F3F4F6',
  },

  telemetryText: {
    fontSize: 10,
    color: '#6B7280',
    fontWeight: '500',
  },

  statsRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 14,
  },

  statBox: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 14,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  statValue: {
    fontSize: 16,
    fontWeight: '800',
    color: '#111827',
  },

  statLabel: {
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 4,
    fontWeight: '600',
  },

  spacer: {
    flex: 1,
  },

  endButton: {
    height: 56,
    borderRadius: 18,
    backgroundColor: '#DC2626',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#DC2626',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 3,
  },

  buttonDisabled: {
    opacity: 0.6,
  },

  endButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '800',
  },

  footerNote: {
    textAlign: 'center',
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 10,
    marginBottom: 6,
  },
});
