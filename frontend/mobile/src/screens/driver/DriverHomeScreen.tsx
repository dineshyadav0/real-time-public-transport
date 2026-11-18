import React, { useEffect, useState, useCallback } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  ActivityIndicator,
  Alert,
  RefreshControl,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../navigation/AppNavigator';
import { ApiService } from '../../services/apiService';
import { Trip, TripStatus } from '../../types';

type Props = NativeStackScreenProps<RootStackParamList, 'DriverHome'>;

export default function DriverHomeScreen({ navigation }: Props) {
  const [trips, setTrips] = useState<Trip[]>([]);
  const [selectedTrip, setSelectedTrip] = useState<Trip | null>(null);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [isStartingTrip, setIsStartingTrip] = useState(false);
  const [isBackendConnected, setIsBackendConnected] = useState(false);

  const loadDriverTrips = useCallback(async () => {
    try {
      const allTrips = await ApiService.getTrips();
      setTrips(allTrips);
      setIsBackendConnected(ApiService.isBackendAvailable());

      // Priority 1: Check if there is an already RUNNING trip
      const runningTrip = allTrips.find((t) => t.status === 'RUNNING');
      if (runningTrip) {
        setSelectedTrip(runningTrip);
      } else {
        // Priority 2: Select first SCHEDULED trip
        const scheduledTrip = allTrips.find((t) => t.status === 'SCHEDULED');
        setSelectedTrip(scheduledTrip || allTrips[0] || null);
      }
    } catch (err) {
      console.warn('Error loading driver trips:', err);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  }, []);

  useEffect(() => {
    loadDriverTrips();
  }, [loadDriverTrips]);

  const onRefresh = () => {
    setRefreshing(true);
    loadDriverTrips();
  };

  /**
   * Driver starts the trip:
   * 1. Calls PATCH /api/trips/{tripId}/start on backend
   * 2. Confirms backend 200 OK
   * 3. Navigates to ActiveTripScreen with confirmed trip ID
   */
  const handleStartTrip = async () => {
    if (!selectedTrip) {
      Alert.alert('No Trip Selected', 'Please select a scheduled trip to start.');
      return;
    }

    // If trip is already running, resume directly
    if (selectedTrip.status === 'RUNNING') {
      navigation.navigate('ActiveTrip', {
        tripId: selectedTrip.id,
        tripNumber: selectedTrip.tripNumber,
        busNumber: selectedTrip.bus.busNumber,
        registrationNumber: selectedTrip.bus.registrationNumber,
        source: selectedTrip.route.source,
        destination: selectedTrip.route.destination,
        distanceKm: selectedTrip.route.totalDistance ?? 32,
      });
      return;
    }

    setIsStartingTrip(true);
    try {
      // Call backend lifecycle transition: PATCH /api/trips/{tripId}/start
      const updatedTrip = await ApiService.startTrip(selectedTrip.id);

      setSelectedTrip(updatedTrip);

      // Start GPS tracking only after backend confirms 200 OK
      navigation.navigate('ActiveTrip', {
        tripId: updatedTrip.id,
        tripNumber: updatedTrip.tripNumber,
        busNumber: updatedTrip.bus.busNumber,
        registrationNumber: updatedTrip.bus.registrationNumber,
        source: updatedTrip.route.source,
        destination: updatedTrip.route.destination,
        distanceKm: updatedTrip.route.totalDistance ?? 32,
      });
    } catch (err: any) {
      Alert.alert(
        'Unable to Start Trip',
        err?.message ||
          'Backend rejected trip start. Trip may already be running or completed.'
      );
    } finally {
      setIsStartingTrip(false);
    }
  };

  const isTripRunning = selectedTrip?.status === 'RUNNING';

  return (
    <SafeAreaView style={styles.container}>
      {/* Top Bar */}
      <View style={styles.topBar}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.navigate('Welcome')}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>

        <View style={styles.topBarCenter}>
          <Text style={styles.topBarTitle}>Driver Cockpit</Text>
          <Text style={styles.topBarSubtitle}>
            {isBackendConnected ? 'Backend Connected' : 'Offline / Standalone'}
          </Text>
        </View>

        <View style={styles.profileCircle}>
          <Text style={styles.profileText}>D</Text>
        </View>
      </View>

      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.content}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }
      >
        {/* Header greeting */}
        <View style={styles.header}>
          <Text style={styles.greeting}>Welcome, Officer</Text>
          <Text style={styles.title}>Ready for your route?</Text>
        </View>

        {/* Status Card */}
        <View style={styles.statusCard}>
          <View style={styles.statusIcon}>
            <Text style={styles.busIcon}>🚌</Text>
          </View>

          <View style={styles.statusContent}>
            <Text style={styles.statusLabel}>DRIVER DUTY STATUS</Text>
            <Text style={styles.statusTitle}>
              {isTripRunning ? 'On Active Duty' : 'Standby / Scheduled'}
            </Text>
            <Text style={styles.statusSubtitle}>
              {isTripRunning
                ? 'Trip currently in progress. Tap below to resume telemetry.'
                : 'Starting a trip activates real-time GPS telemetry to passengers.'}
            </Text>
          </View>

          <View
            style={[
              styles.dutyBadge,
              { backgroundColor: isTripRunning ? '#ECFDF5' : '#F3F4F6' },
            ]}
          >
            <View
              style={[
                styles.dutyDot,
                { backgroundColor: isTripRunning ? '#10B981' : '#9CA3AF' },
              ]}
            />
            <Text
              style={[
                styles.dutyText,
                { color: isTripRunning ? '#059669' : '#6B7280' },
              ]}
            >
              {isTripRunning ? 'RUNNING' : 'STANDBY'}
            </Text>
          </View>
        </View>

        {/* Scheduled Trips Selection */}
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Assigned Trips</Text>
          <Text style={styles.tripCount}>{trips.length} rostered</Text>
        </View>

        {loading && (
          <View style={styles.loadingBox}>
            <ActivityIndicator size="small" color="#111827" />
            <Text style={styles.loadingText}>Fetching roster from backend...</Text>
          </View>
        )}

        {!loading && trips.length === 0 && (
          <View style={styles.emptyBox}>
            <Text style={styles.emptyTitle}>No Trips Assigned</Text>
            <Text style={styles.emptySubtitle}>
              Ensure trips exist in backend database or run timetable import.
            </Text>
          </View>
        )}

        {!loading &&
          trips.map((trip) => {
            const isSelected = selectedTrip?.id === trip.id;
            const isRunning = trip.status === 'RUNNING';

            return (
              <TouchableOpacity
                key={`driver-trip-${trip.id}`}
                style={[
                  styles.tripSelectCard,
                  isSelected && styles.tripSelectCardActive,
                ]}
                activeOpacity={0.85}
                onPress={() => setSelectedTrip(trip)}
              >
                <View style={styles.tripSelectHeader}>
                  <View>
                    <Text style={styles.tripNumberText}>
                      {trip.tripNumber} • BUS {trip.bus.busNumber}
                    </Text>
                    <Text style={styles.tripRouteText}>
                      {trip.route.source} → {trip.route.destination}
                    </Text>
                  </View>

                  <View
                    style={[
                      styles.tripStatusTag,
                      isRunning && styles.tripStatusTagRunning,
                    ]}
                  >
                    <Text
                      style={[
                        styles.tripStatusTagText,
                        isRunning && styles.tripStatusTagTextRunning,
                      ]}
                    >
                      {trip.status}
                    </Text>
                  </View>
                </View>

                <View style={styles.tripMetaRow}>
                  <Text style={styles.tripMetaItem}>
                    Departure: {new Date(trip.departureTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  </Text>
                  <Text style={styles.tripMetaItem}>
                    Vehicle: {trip.bus.registrationNumber}
                  </Text>
                  <Text style={styles.tripMetaItem}>
                    Type: {trip.bus.busType}
                  </Text>
                </View>
              </TouchableOpacity>
            );
          })}

        {/* Selected Trip Details */}
        {selectedTrip && (
          <View style={styles.routeCard}>
            <View style={styles.routeTop}>
              <View>
                <Text style={styles.busNumber}>
                  BUS {selectedTrip.bus.busNumber}
                </Text>
                <Text style={styles.routeName}>
                  {selectedTrip.route.source} → {selectedTrip.route.destination}
                </Text>
              </View>

              <View style={styles.routeBadge}>
                <Text style={styles.routeBadgeText}>
                  {selectedTrip.route.routeNumber}
                </Text>
              </View>
            </View>

            <View style={styles.routeLineContainer}>
              <View style={styles.routeDotActive} />
              <View style={styles.routeLine} />
              <View style={styles.routeDot} />
            </View>

            <View style={styles.routeStops}>
              <View>
                <Text style={styles.stopTitle}>
                  {selectedTrip.route.source}
                </Text>
                <Text style={styles.stopSubtitle}>Terminal Origin</Text>
              </View>

              <View style={styles.destination}>
                <Text style={styles.stopTitle}>
                  {selectedTrip.route.destination}
                </Text>
                <Text style={styles.stopSubtitle}>Final Destination</Text>
              </View>
            </View>

            {/* Trip Stats */}
            <View style={styles.infoRow}>
              <View style={styles.infoCard}>
                <Text style={styles.infoIcon}>📏</Text>
                <Text style={styles.infoLabel}>Distance</Text>
                <Text style={styles.infoValue}>
                  {selectedTrip.route.totalDistance
                    ? `${selectedTrip.route.totalDistance} km`
                    : '32 km'}
                </Text>
              </View>

              <View style={styles.infoCard}>
                <Text style={styles.infoIcon}>👥</Text>
                <Text style={styles.infoLabel}>Capacity</Text>
                <Text style={styles.infoValue}>
                  {selectedTrip.bus.capacity} seats
                </Text>
              </View>

              <View style={styles.infoCard}>
                <Text style={styles.infoIcon}>⏱</Text>
                <Text style={styles.infoLabel}>Trip ID</Text>
                <Text style={styles.infoValue}>#{selectedTrip.id}</Text>
              </View>
            </View>
          </View>
        )}

        {/* GPS tracking instructions */}
        <View style={styles.gpsCard}>
          <View style={styles.gpsHeader}>
            <View style={styles.gpsIconContainer}>
              <Text style={styles.gpsIcon}>📡</Text>
            </View>

            <View style={styles.gpsContent}>
              <Text style={styles.gpsTitle}>Backend GPS Telemetry</Text>
              <Text style={styles.gpsSubtitle}>
                GPS points are submitted to POST /api/gps and broadcast to
                /topic/trips/{selectedTrip?.id || 1} via WebSocket STOMP.
              </Text>
            </View>
          </View>

          <View style={styles.gpsStatus}>
            <View
              style={[
                styles.gpsStatusDot,
                { backgroundColor: isTripRunning ? '#10B981' : '#6B7280' },
              ]}
            />
            <Text
              style={[
                styles.gpsStatusText,
                { color: isTripRunning ? '#059669' : '#6B7280' },
              ]}
            >
              {isTripRunning
                ? 'Trip is currently RUNNING • GPS actively accepted by backend'
                : 'Ready to start • Backend accepts GPS only while status is RUNNING'}
            </Text>
          </View>
        </View>

        {/* Action Button: Start Trip / Resume Trip */}
        <TouchableOpacity
          style={[
            styles.startButton,
            isTripRunning && styles.resumeButton,
            isStartingTrip && styles.buttonDisabled,
          ]}
          activeOpacity={0.85}
          onPress={handleStartTrip}
          disabled={isStartingTrip || !selectedTrip}
        >
          {isStartingTrip ? (
            <ActivityIndicator color="#FFFFFF" size="small" />
          ) : (
            <>
              <Text style={styles.startButtonIcon}>
                {isTripRunning ? '⚡' : '▶'}
              </Text>
              <Text style={styles.startButtonText}>
                {isTripRunning ? 'Resume Active Trip' : 'Start Trip & GPS'}
              </Text>
            </>
          )}
        </TouchableOpacity>

        <Text style={styles.warning}>
          GPS coordinates are transmitted only while the trip lifecycle is RUNNING.
        </Text>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F7F9FC',
  },

  topBar: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 12,
    backgroundColor: '#FFFFFF',
    borderBottomWidth: 1,
    borderBottomColor: '#E5E7EB',
  },

  backButton: {
    width: 38,
    height: 38,
    borderRadius: 12,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
  },

  backButtonText: {
    fontSize: 20,
    fontWeight: '700',
    color: '#111827',
  },

  topBarCenter: {
    alignItems: 'center',
  },

  topBarTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: '#111827',
  },

  topBarSubtitle: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 1,
  },

  profileCircle: {
    width: 38,
    height: 38,
    borderRadius: 19,
    backgroundColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  profileText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '700',
  },

  content: {
    padding: 20,
    paddingBottom: 40,
  },

  header: {
    marginBottom: 16,
  },

  greeting: {
    fontSize: 14,
    color: '#6B7280',
    fontWeight: '600',
  },

  title: {
    fontSize: 26,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  statusCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
    marginBottom: 20,
  },

  statusIcon: {
    width: 48,
    height: 48,
    borderRadius: 14,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },

  busIcon: {
    fontSize: 24,
  },

  statusContent: {
    flex: 1,
  },

  statusLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.6,
  },

  statusTitle: {
    fontSize: 16,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  statusSubtitle: {
    fontSize: 10,
    color: '#6B7280',
    marginTop: 2,
    lineHeight: 14,
  },

  dutyBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 16,
    paddingHorizontal: 8,
    paddingVertical: 5,
  },

  dutyDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: 5,
  },

  dutyText: {
    fontSize: 9,
    fontWeight: '800',
  },

  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },

  sectionTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: '#111827',
  },

  tripCount: {
    fontSize: 12,
    color: '#6B7280',
    fontWeight: '600',
  },

  loadingBox: {
    padding: 20,
    alignItems: 'center',
  },

  loadingText: {
    fontSize: 12,
    color: '#6B7280',
    marginTop: 6,
  },

  emptyBox: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 20,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
    marginBottom: 16,
  },

  emptyTitle: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
  },

  emptySubtitle: {
    fontSize: 11,
    color: '#6B7280',
    textAlign: 'center',
    marginTop: 4,
  },

  tripSelectCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 14,
    borderWidth: 1.5,
    borderColor: '#E5E7EB',
    marginBottom: 10,
  },

  tripSelectCardActive: {
    borderColor: '#111827',
    backgroundColor: '#FAFAFA',
  },

  tripSelectHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  tripNumberText: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
  },

  tripRouteText: {
    fontSize: 12,
    color: '#4B5563',
    marginTop: 2,
    fontWeight: '500',
  },

  tripStatusTag: {
    backgroundColor: '#F3F4F6',
    borderRadius: 8,
    paddingHorizontal: 8,
    paddingVertical: 3,
  },

  tripStatusTagRunning: {
    backgroundColor: '#ECFDF5',
  },

  tripStatusTagText: {
    fontSize: 9,
    fontWeight: '800',
    color: '#6B7280',
  },

  tripStatusTagTextRunning: {
    color: '#059669',
  },

  tripMetaRow: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 8,
    paddingTop: 8,
    borderTopWidth: 1,
    borderTopColor: '#F3F4F6',
  },

  tripMetaItem: {
    fontSize: 10,
    color: '#6B7280',
    fontWeight: '500',
  },

  routeCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E5E7EB',
    marginTop: 8,
    marginBottom: 16,
  },

  routeTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },

  busNumber: {
    fontSize: 18,
    fontWeight: '800',
    color: '#111827',
  },

  routeName: {
    fontSize: 12,
    color: '#6B7280',
    marginTop: 3,
  },

  routeBadge: {
    backgroundColor: '#F3F4F6',
    borderRadius: 8,
    paddingHorizontal: 8,
    paddingVertical: 4,
    height: 25,
  },

  routeBadgeText: {
    fontSize: 9,
    fontWeight: '800',
    color: '#6B7280',
  },

  routeLineContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 20,
    paddingHorizontal: 4,
  },

  routeDotActive: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#111827',
  },

  routeLine: {
    flex: 1,
    height: 3,
    backgroundColor: '#111827',
    marginHorizontal: 4,
  },

  routeDot: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#FFFFFF',
    borderWidth: 3,
    borderColor: '#111827',
  },

  routeStops: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 8,
  },

  stopTitle: {
    fontSize: 12,
    fontWeight: '700',
    color: '#111827',
  },

  stopSubtitle: {
    fontSize: 9,
    color: '#9CA3AF',
    marginTop: 2,
  },

  destination: {
    alignItems: 'flex-end',
  },

  infoRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 14,
    paddingTop: 14,
    borderTopWidth: 1,
    borderTopColor: '#F3F4F6',
  },

  infoCard: {
    flex: 1,
    backgroundColor: '#F9FAFB',
    borderRadius: 12,
    padding: 10,
    alignItems: 'center',
  },

  infoIcon: {
    fontSize: 16,
  },

  infoLabel: {
    fontSize: 9,
    color: '#9CA3AF',
    marginTop: 4,
  },

  infoValue: {
    fontSize: 12,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  gpsCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E5E7EB',
    marginBottom: 16,
  },

  gpsHeader: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  gpsIconContainer: {
    width: 40,
    height: 40,
    borderRadius: 12,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },

  gpsIcon: {
    fontSize: 20,
  },

  gpsContent: {
    flex: 1,
  },

  gpsTitle: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  gpsSubtitle: {
    fontSize: 10,
    lineHeight: 14,
    color: '#6B7280',
    marginTop: 2,
  },

  gpsStatus: {
    marginTop: 12,
    paddingTop: 10,
    borderTopWidth: 1,
    borderColor: '#F0F1F3',
    flexDirection: 'row',
    alignItems: 'center',
  },

  gpsStatusDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: 6,
  },

  gpsStatusText: {
    fontSize: 10,
    fontWeight: '600',
  },

  startButton: {
    height: 54,
    borderRadius: 16,
    backgroundColor: '#111827',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },

  resumeButton: {
    backgroundColor: '#059669',
  },

  buttonDisabled: {
    opacity: 0.6,
  },

  startButtonIcon: {
    color: '#FFFFFF',
    fontSize: 14,
    marginRight: 8,
  },

  startButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '800',
  },

  warning: {
    textAlign: 'center',
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 10,
  },
});
