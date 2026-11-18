import React, { useEffect, useState, useRef } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  ActivityIndicator,
  Share,
} from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../navigation/AppNavigator';
import { WebSocketService, WebSocketStatus } from '../../services/webSocketService';
import { ApiService } from '../../services/apiService';
import { LiveLocationUpdate } from '../../types';

type Props = NativeStackScreenProps<RootStackParamList, 'BusDetails'>;

export default function BusDetailsScreen({ route, navigation }: Props) {
  const { bus } = route.params;

  // Real-time GPS state
  const [liveLocation, setLiveLocation] = useState<LiveLocationUpdate | null>(null);
  const [wsStatus, setWsStatus] = useState<WebSocketStatus>('DISCONNECTED');
  const [lastUpdatedText, setLastUpdatedText] = useState<string>('No GPS telemetry yet');
  const [isLoadingInitialGps, setIsLoadingInitialGps] = useState(false);

  // Map view reference
  const mapRef = useRef<MapView>(null);

  // Initial map coordinates from bus stops or live location
  const initialStop = bus.stops && bus.stops.length > 0 ? bus.stops[0] : null;
  const initialLat = bus.currentLatitude ?? initialStop?.latitude ?? 28.8955;
  const initialLng = bus.currentLongitude ?? initialStop?.longitude ?? 76.6066;

  // Active coordinates: prioritizes live GPS message from backend WebSocket
  const currentLatitude = liveLocation ? liveLocation.latitude : initialLat;
  const currentLongitude = liveLocation ? liveLocation.longitude : initialLng;
  const currentSpeed = liveLocation ? liveLocation.speed : 0;

  // Subscribe to live WebSocket updates if bus has a tripId
  useEffect(() => {
    let timer: any = null;

    if (bus.tripId) {
      setIsLoadingInitialGps(true);

      // 1. Fetch latest stored GPS from backend REST API: GET /api/gps/trip/{tripId}/latest
      ApiService.getLatestGps(bus.tripId)
        .then((latest) => {
          if (latest) {
            setLiveLocation({
              tripId: latest.tripId,
              latitude: latest.latitude,
              longitude: latest.longitude,
              speed: latest.speed,
              recordedAt: latest.recordedAt,
            });
            setLastUpdatedText(`Last recorded at ${new Date(latest.recordedAt).toLocaleTimeString()}`);
          }
        })
        .finally(() => setIsLoadingInitialGps(false));

      // 2. Subscribe to real-time STOMP topic: /topic/trips/{tripId}
      WebSocketService.subscribeToTrip(
        bus.tripId,
        (update: LiveLocationUpdate) => {
          setLiveLocation(update);
          setLastUpdatedText(`Updated just now (${new Date().toLocaleTimeString()})`);

          // Animate map camera smoothly to new bus coordinates
          if (mapRef.current) {
            mapRef.current.animateToRegion(
              {
                latitude: update.latitude,
                longitude: update.longitude,
                latitudeDelta: 0.04,
                longitudeDelta: 0.04,
              },
              1000
            );
          }
        },
        (status: WebSocketStatus) => {
          setWsStatus(status);
        }
      );
    } else {
      setLastUpdatedText('Scheduled timetable service (Trip not yet active)');
    }

    return () => {
      if (timer) clearInterval(timer);
      WebSocketService.disconnect();
    };
  }, [bus.tripId]);

  const routeCoordinates = (bus.stops || []).map((stop) => ({
    latitude: stop.latitude,
    longitude: stop.longitude,
  }));

  const handleShare = async () => {
    try {
      await Share.share({
        message: `Tracking Haryana Roadways Bus ${bus.number} on route ${bus.route}. Destination: ${bus.destination}.`,
      });
    } catch (_) {}
  };

  const isLiveActive = liveLocation != null || bus.status === 'LIVE';

  return (
    <SafeAreaView style={styles.container}>
      {/* Top Bar */}
      <View style={styles.topBar}>
        <TouchableOpacity
          style={styles.backButton}
          onPress={() => navigation.goBack()}
        >
          <Text style={styles.backButtonText}>←</Text>
        </TouchableOpacity>

        <View style={styles.topBarCenter}>
          <Text style={styles.topBarTitle}>BUS {bus.number}</Text>
          <Text style={styles.topBarSubtitle}>{bus.route}</Text>
        </View>

        <TouchableOpacity style={styles.shareButton} onPress={handleShare}>
          <Text style={styles.shareButtonText}>↗</Text>
        </TouchableOpacity>
      </View>

      <ScrollView showsVerticalScrollIndicator={false}>
        {/* Interactive Map View */}
        <View style={styles.mapContainer}>
          <MapView
            ref={mapRef}
            style={styles.map}
            initialRegion={{
              latitude: currentLatitude,
              longitude: currentLongitude,
              latitudeDelta: 0.07,
              longitudeDelta: 0.07,
            }}
          >
            {/* Route polyline connecting scheduled stops */}
            {routeCoordinates.length > 1 && (
              <Polyline
                coordinates={routeCoordinates}
                strokeWidth={4}
                strokeColor="#111827"
              />
            )}

            {/* Scheduled stop markers */}
            {bus.stops &&
              bus.stops.map((stop, index) => (
                <Marker
                  key={`stop-${stop.name}-${index}`}
                  coordinate={{
                    latitude: stop.latitude,
                    longitude: stop.longitude,
                  }}
                  title={stop.name}
                  description={
                    index === 0
                      ? 'Origin'
                      : index === bus.stops.length - 1
                      ? 'Destination'
                      : 'Scheduled stop'
                  }
                  pinColor="#6B7280"
                />
              ))}

            {/* Live Bus Marker (Updates via STOMP) */}
            <Marker
              coordinate={{
                latitude: currentLatitude,
                longitude: currentLongitude,
              }}
              title={`Bus ${bus.number} (${isLiveActive ? 'Live GPS' : 'Initial Position'})`}
              description={`Speed: ${currentSpeed.toFixed(1)} km/h • ${lastUpdatedText}`}
              pinColor={isLiveActive ? '#10B981' : '#2563EB'}
            />
          </MapView>

          {/* Real-time Status Overlay Pill */}
          <View style={styles.mapOverlayPill}>
            <View
              style={[
                styles.liveStatusDot,
                {
                  backgroundColor:
                    wsStatus === 'CONNECTED' && isLiveActive
                      ? '#10B981'
                      : wsStatus === 'CONNECTING'
                      ? '#F59E0B'
                      : '#9CA3AF',
                },
              ]}
            />
            <Text style={styles.mapOverlayText}>
              {wsStatus === 'CONNECTED' && isLiveActive
                ? `LIVE GPS STREAM • ${currentSpeed.toFixed(0)} km/h`
                : wsStatus === 'CONNECTING'
                ? 'CONNECTING TO STOMP /ws...'
                : bus.status === 'LIVE'
                ? 'WAITING FOR DRIVER GPS'
                : 'SCHEDULED ROUTE'}
            </Text>
          </View>
        </View>

        {/* Content Details */}
        <View style={styles.content}>
          {/* Main Bus Header Card */}
          <View style={styles.mainCard}>
            <View style={styles.cardHeader}>
              <View>
                <View style={styles.titleRow}>
                  <Text style={styles.busTitle}>BUS {bus.number}</Text>
                  {bus.registrationNumber ? (
                    <Text style={styles.busReg}>• {bus.registrationNumber}</Text>
                  ) : null}
                </View>
                <Text style={styles.busRoute}>{bus.route}</Text>
              </View>

              {isLiveActive ? (
                <View style={styles.liveBadge}>
                  <View style={styles.liveDot} />
                  <Text style={styles.liveText}>LIVE</Text>
                </View>
              ) : (
                <View style={styles.offlineBadge}>
                  <Text style={styles.offlineText}>SCHEDULED</Text>
                </View>
              )}
            </View>

            {/* Connection & Telemetry info banner */}
            <View style={styles.telemetryBanner}>
              <Text style={styles.telemetryIcon}>📡</Text>
              <View style={{ flex: 1 }}>
                <Text style={styles.telemetryTitle}>
                  {isLiveActive ? 'Live GPS Updates Active' : 'Timetable Schedule'}
                </Text>
                <Text style={styles.telemetrySubtitle}>
                  {lastUpdatedText}
                </Text>
              </View>
            </View>

            {/* Metrics Row */}
            <View style={styles.metricsRow}>
              <View style={styles.metricBox}>
                <Text style={styles.metricLabel}>LIVE SPEED</Text>
                <Text style={styles.metricValue}>
                  {currentSpeed.toFixed(1)} km/h
                </Text>
              </View>

              <View style={styles.metricDivider} />

              <View style={styles.metricBox}>
                <Text style={styles.metricLabel}>SERVICE TYPE</Text>
                <Text style={styles.metricValue}>
                  {bus.serviceType || 'ORDINARY'}
                </Text>
              </View>

              <View style={styles.metricDivider} />

              <View style={styles.metricBox}>
                <Text style={styles.metricLabel}>DEPOT</Text>
                <Text style={styles.metricValue}>
                  {bus.depot || 'Haryana'}
                </Text>
              </View>
            </View>
          </View>

          {/* AI Intelligence & Catchability Cards */}
          <View style={styles.intelligenceRow}>
            <View style={styles.intelCard}>
              <Text style={styles.intelIcon}>👥</Text>
              <Text style={styles.intelLabel}>Expected Crowding</Text>
              <Text style={styles.intelValue}>{bus.occupancy || 65}% capacity</Text>
              <Text style={styles.intelSub}>Based on route history</Text>
            </View>

            <View style={styles.intelCard}>
              <Text style={styles.intelIcon}>⚡</Text>
              <Text style={styles.intelLabel}>Delay Probability</Text>
              <Text style={styles.intelValue}>{bus.delay || 10}% low</Text>
              <Text style={styles.intelSub}>Traffic conditions normal</Text>
            </View>
          </View>

          {/* Catch Probability Banner */}
          <View style={styles.catchBanner}>
            <View style={{ flex: 1 }}>
              <Text style={styles.catchBannerTitle}>Chance you will catch it</Text>
              <Text style={styles.catchBannerSubtitle}>
                🚶 ~{bus.walking || '4 min'} walking time to nearest boarding point
              </Text>
            </View>
            <View style={styles.catchRing}>
              <Text style={styles.catchRingText}>
                {bus.catchChance || 85}%
              </Text>
            </View>
          </View>

          {/* Route Stops Sequence */}
          <Text style={styles.sectionHeading}>Route stops & Timings</Text>

          <View style={styles.stopsCard}>
            {(bus.stops || []).map((stop, index) => {
              const isFirst = index === 0;
              const isLast = index === (bus.stops?.length ?? 1) - 1;

              return (
                <View key={`seq-${stop.name}-${index}`} style={styles.stopRow}>
                  <View style={styles.timelineColumn}>
                    <View
                      style={[
                        styles.timelineDot,
                        isFirst && styles.timelineDotOrigin,
                        isLast && styles.timelineDotDest,
                      ]}
                    />
                    {!isLast && <View style={styles.timelineLine} />}
                  </View>

                  <View style={styles.stopDetails}>
                    <Text
                      style={[
                        styles.stopName,
                        (isFirst || isLast) && styles.stopNameHighlight,
                      ]}
                    >
                      {stop.name}
                    </Text>
                    <Text style={styles.stopRole}>
                      {isFirst
                        ? 'Departure Terminal'
                        : isLast
                        ? 'Final Destination'
                        : `Stop ${index + 1} • Intermediate`}
                    </Text>
                  </View>
                </View>
              );
            })}
          </View>

          {/* Live Subscription Status Footer */}
          <View style={styles.footerCard}>
            <Text style={styles.footerCardTitle}>
              WebSocket STOMP Channel: /topic/trips/{bus.tripId || 'none'}
            </Text>
            <Text style={styles.footerCardBody}>
              {wsStatus === 'CONNECTED'
                ? 'Client actively listening for driver GPS broadcasts from Spring Boot.'
                : 'Connecting to real-time telemetry stream...'}
            </Text>
          </View>
        </View>
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

  shareButton: {
    width: 38,
    height: 38,
    borderRadius: 12,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
  },

  shareButtonText: {
    fontSize: 16,
    fontWeight: '700',
    color: '#111827',
  },

  mapContainer: {
    height: 320,
    width: '100%',
    position: 'relative',
  },

  map: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },

  mapOverlayPill: {
    position: 'absolute',
    top: 14,
    left: 14,
    right: 14,
    backgroundColor: 'rgba(17, 24, 39, 0.92)',
    borderRadius: 20,
    paddingHorizontal: 14,
    paddingVertical: 8,
    flexDirection: 'row',
    alignItems: 'center',
  },

  liveStatusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 8,
  },

  mapOverlayText: {
    color: '#FFFFFF',
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 0.5,
  },

  content: {
    padding: 20,
    paddingBottom: 40,
  },

  mainCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 18,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },

  titleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },

  busTitle: {
    fontSize: 22,
    fontWeight: '800',
    color: '#111827',
  },

  busReg: {
    fontSize: 13,
    color: '#6B7280',
    fontWeight: '600',
  },

  busRoute: {
    fontSize: 13,
    color: '#4B5563',
    marginTop: 3,
    fontWeight: '500',
  },

  liveBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#ECFDF5',
    borderRadius: 16,
    paddingHorizontal: 10,
    paddingVertical: 5,
  },

  liveDot: {
    width: 7,
    height: 7,
    borderRadius: 4,
    backgroundColor: '#10B981',
    marginRight: 6,
  },

  liveText: {
    fontSize: 10,
    fontWeight: '800',
    color: '#059669',
  },

  offlineBadge: {
    backgroundColor: '#F3F4F6',
    borderRadius: 16,
    paddingHorizontal: 10,
    paddingVertical: 5,
  },

  offlineText: {
    fontSize: 10,
    fontWeight: '800',
    color: '#6B7280',
  },

  telemetryBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#F9FAFB',
    borderRadius: 14,
    padding: 12,
    marginTop: 14,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  telemetryIcon: {
    fontSize: 22,
    marginRight: 10,
  },

  telemetryTitle: {
    fontSize: 12,
    fontWeight: '700',
    color: '#111827',
  },

  telemetrySubtitle: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 2,
  },

  metricsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 14,
    paddingTop: 14,
    borderTopWidth: 1,
    borderTopColor: '#F3F4F6',
  },

  metricBox: {
    flex: 1,
    alignItems: 'center',
  },

  metricLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.5,
  },

  metricValue: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
    marginTop: 3,
  },

  metricDivider: {
    width: 1,
    height: 26,
    backgroundColor: '#E5E7EB',
  },

  intelligenceRow: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 14,
  },

  intelCard: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  intelIcon: {
    fontSize: 20,
    marginBottom: 6,
  },

  intelLabel: {
    fontSize: 10,
    color: '#6B7280',
    fontWeight: '600',
  },

  intelValue: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  intelSub: {
    fontSize: 9,
    color: '#9CA3AF',
    marginTop: 4,
  },

  catchBanner: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 16,
    marginTop: 14,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  catchBannerTitle: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  catchBannerSubtitle: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 3,
  },

  catchRing: {
    width: 48,
    height: 48,
    borderRadius: 24,
    borderWidth: 4,
    borderColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  catchRingText: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  sectionHeading: {
    fontSize: 17,
    fontWeight: '800',
    color: '#111827',
    marginTop: 24,
    marginBottom: 12,
  },

  stopsCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  stopRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    marginBottom: 16,
  },

  timelineColumn: {
    alignItems: 'center',
    width: 24,
    marginRight: 12,
  },

  timelineDot: {
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: '#D1D5DB',
  },

  timelineDotOrigin: {
    backgroundColor: '#111827',
  },

  timelineDotDest: {
    backgroundColor: '#2563EB',
  },

  timelineLine: {
    width: 2,
    height: 36,
    backgroundColor: '#E5E7EB',
    marginTop: 4,
  },

  stopDetails: {
    flex: 1,
  },

  stopName: {
    fontSize: 13,
    fontWeight: '600',
    color: '#374151',
  },

  stopNameHighlight: {
    fontWeight: '800',
    color: '#111827',
  },

  stopRole: {
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 2,
  },

  footerCard: {
    marginTop: 16,
    backgroundColor: '#F3F4F6',
    borderRadius: 14,
    padding: 14,
  },

  footerCardTitle: {
    fontSize: 10,
    fontWeight: '800',
    color: '#4B5563',
    letterSpacing: 0.5,
  },

  footerCardBody: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 4,
    lineHeight: 16,
  },
});
