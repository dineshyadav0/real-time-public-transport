import React from 'react';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../navigation/AppNavigator';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
} from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';

type Props = NativeStackScreenProps<
  RootStackParamList,
  'BusDetails'
>;

export default function BusDetailsScreen({
  navigation,
  route,
}: Props) {
  const { bus } = route.params;
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView showsVerticalScrollIndicator={false}>
        
        {/* Header */}
        <View style={styles.header}>
          <TouchableOpacity
            onPress={() => navigation.goBack()}
            style={styles.backButton}
          >
            <Text style={styles.backText}>‹</Text>
          </TouchableOpacity>

          <Text style={styles.headerTitle}>Bus {bus.number}</Text>

          <TouchableOpacity style={styles.moreButton}>
            <Text style={styles.moreText}>•••</Text>
          </TouchableOpacity>
        </View>

        {/* Live Map */}
<View style={styles.mapCard}>
  <Text style={styles.mapTitle}>LIVE ROUTE</Text>

  <MapView
    style={styles.map}
    initialRegion={{
      latitude: 30.3165,
      longitude: 78.0322,
      latitudeDelta: 0.035,
      longitudeDelta: 0.035,
    }}
  >
    {/* Route */}
    <Polyline
      coordinates={[
        {
          latitude: 30.3165,
          longitude: 78.0322,
        },
        {
          latitude: 30.3188,
          longitude: 78.0460,
        },
        {
          latitude: 30.3250,
          longitude: 78.0500,
        },
        {
          latitude: 30.3350,
          longitude: 78.0550,
        },
      ]}
      strokeWidth={5}
      strokeColor="#111827"
    />

    {/* Bus */}
    <Marker
      coordinate={{
        latitude: 30.3220,
        longitude: 78.0480,
      }}
      title="Bus 102"
      description="Live location"
    />

    {/* Stops */}
    <Marker
      coordinate={{
        latitude: 30.3165,
        longitude: 78.0322,
      }}
      title="City Center"
    />

    <Marker
      coordinate={{
        latitude: 30.3188,
        longitude: 78.0460,
      }}
      title="Clock Tower"
    />

    <Marker
      coordinate={{
        latitude: 30.3250,
        longitude: 78.0500,
      }}
      title="ISBT"
    />

    <Marker
      coordinate={{
        latitude: 30.3350,
        longitude: 78.0550,
      }}
      title="Railway Station"
    />
  </MapView>

  <View style={styles.liveRow}>
    <View style={styles.liveDot} />
    <Text style={styles.liveText}>
      Bus location updated just now
    </Text>
  </View>
</View>
        {/* Bus information */}
        <View style={styles.content}>
          <View style={styles.routeHeader}>
            <View>
              <Text style={styles.busNumber}>BUS {bus.number}</Text>
              <Text style={styles.routeName}>
  {bus.route}
</Text>
            </View>

            <View style={styles.liveBadge}>
              <View style={styles.badgeDot} />
              <Text style={styles.badgeText}>LIVE</Text>
            </View>
          </View>

          {/* ETA */}
          <View style={styles.etaCard}>
            <View>
              <Text style={styles.smallLabel}>ARRIVING AT YOUR STOP</Text>
              <Text style={styles.eta}>{bus.eta}</Text>
              <Text style={styles.etaSubtext}>
  Predicted arrival
</Text>
            </View>

            <View style={styles.clockCircle}>
              <Text style={styles.clock}>⏱</Text>
            </View>
          </View>

          {/* Prediction cards */}
          <View style={styles.statsRow}>
            <View style={styles.statCard}>
              <Text style={styles.statIcon}>👥</Text>
              <Text style={styles.statLabel}>Crowding</Text>
              <Text style={styles.statValue}>{bus.occupancy}%</Text>
              <Text style={styles.statSubtext}>Occupied</Text>
            </View>

            <View style={styles.statCard}>
              <Text style={styles.statIcon}>⚡</Text>
              <Text style={styles.statLabel}>Delay chance</Text>
              <Text style={styles.statValue}>{bus.delay}%</Text>
              <Text style={styles.statSubtext}>Low risk</Text>
            </View>
          </View>

          {/* Catchability */}
          <View style={styles.catchCard}>
            <View style={styles.catchHeader}>
              <View>
                <Text style={styles.catchTitle}>
                  Your chance of catching this bus
                </Text>
                <Text style={styles.catchDescription}>
                  Based on walking time + predicted arrival
                </Text>
              </View>

              <View style={styles.catchCircle}>
                <Text style={styles.catchPercentage}>
  {bus.catchChance}%
</Text>
              </View>
            </View>

            <View style={styles.progressBackground}>
              <View
  style={[
    styles.progressFill,
    { width: `${bus.catchChance}%` },
  ]}
/>
            </View>

            <Text style={styles.catchFooter}>
  🚶 {bus.walking} walk • 🚌 {bus.eta} arrival
</Text>
          </View>

          {/* Stops */}
          <Text style={styles.sectionTitle}>Upcoming stops</Text>

          <View style={styles.stopsCard}>
            <View style={styles.stopRow}>
              <View style={styles.timeline}>
                <View style={styles.timelineDotActive} />
                <View style={styles.timelineLine} />
              </View>

              <View style={styles.stopInfo}>
                <Text style={styles.stopNameActive}>
                  Clock Tower
                </Text>
                <Text style={styles.stopTimeActive}>
                  Your stop • ~5 min
                </Text>
              </View>
            </View>

            <View style={styles.stopRow}>
              <View style={styles.timeline}>
                <View style={styles.timelineDot} />
                <View style={styles.timelineLine} />
              </View>

              <View style={styles.stopInfo}>
                <Text style={styles.stopName}>
                  ISBT
                </Text>
                <Text style={styles.stopTime}>
                  ~10 min
                </Text>
              </View>
            </View>

            <View style={styles.stopRow}>
              <View style={styles.timeline}>
                <View style={styles.timelineDot} />
              </View>

              <View style={styles.stopInfo}>
                <Text style={styles.stopName}>
                  Railway Station
                </Text>
                <Text style={styles.stopTime}>
                  ~16 min
                </Text>
              </View>
            </View>
          </View>

          {/* Track button */}
          <TouchableOpacity style={styles.trackButton}>
            <Text style={styles.trackButtonText}>
              Track this bus
            </Text>
          </TouchableOpacity>

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

  header: {
    height: 62,
    paddingHorizontal: 20,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },

  backButton: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: '#FFFFFF',
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  backText: {
    fontSize: 32,
    color: '#111827',
    marginTop: -4,
  },

  headerTitle: {
    fontSize: 18,
    fontWeight: '800',
    color: '#111827',
  },

  moreButton: {
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },

  moreText: {
    fontSize: 18,
    fontWeight: '800',
    color: '#6B7280',
  },

  mapCard: {
    marginHorizontal: 20,
    backgroundColor: '#E9EEF4',
    borderRadius: 22,
    overflow: 'hidden',
  },

  mapTitle: {
    position: 'absolute',
    top: 14,
    left: 16,
    zIndex: 5,
    fontSize: 10,
    fontWeight: '800',
    color: '#6B7280',
    letterSpacing: 1,
  },

map: {
  height: 245,
  width: '100%',
},

  liveRow: {
    height: 42,
    paddingHorizontal: 16,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
  },

  liveDot: {
    width: 7,
    height: 7,
    borderRadius: 4,
    backgroundColor: '#10B981',
    marginRight: 7,
  },

  liveText: {
    fontSize: 11,
    color: '#6B7280',
  },

  content: {
    paddingHorizontal: 20,
    paddingTop: 20,
    paddingBottom: 35,
  },

  routeHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  busNumber: {
    fontSize: 21,
    fontWeight: '800',
    color: '#111827',
  },

  routeName: {
    fontSize: 13,
    color: '#6B7280',
    marginTop: 4,
  },

  liveBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 20,
    backgroundColor: '#ECFDF5',
  },

  badgeDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: '#10B981',
    marginRight: 5,
  },

  badgeText: {
    fontSize: 9,
    fontWeight: '800',
    color: '#059669',
  },

  etaCard: {
    marginTop: 16,
    padding: 17,
    borderRadius: 18,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E5E7EB',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  smallLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.6,
  },

  eta: {
    fontSize: 34,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  etaSubtext: {
    fontSize: 11,
    color: '#6B7280',
  },

  clockCircle: {
    width: 52,
    height: 52,
    borderRadius: 26,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
  },

  clock: {
    fontSize: 23,
  },

  statsRow: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 12,
  },

  statCard: {
    flex: 1,
    padding: 14,
    borderRadius: 17,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  statIcon: {
    fontSize: 21,
  },

  statLabel: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 8,
  },

  statValue: {
    fontSize: 20,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  statSubtext: {
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 2,
  },

  catchCard: {
    marginTop: 12,
    padding: 17,
    borderRadius: 18,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  catchHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  catchTitle: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
    maxWidth: 240,
  },

  catchDescription: {
    fontSize: 10,
    color: '#6B7280',
    marginTop: 4,
  },

  catchCircle: {
    width: 55,
    height: 55,
    borderRadius: 28,
    borderWidth: 4,
    borderColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  catchPercentage: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  progressBackground: {
    height: 7,
    backgroundColor: '#E5E7EB',
    borderRadius: 5,
    marginTop: 15,
    overflow: 'hidden',
  },

  progressFill: {
  height: '100%',
  backgroundColor: '#111827',
  borderRadius: 5,
},

  catchFooter: {
    fontSize: 10,
    color: '#6B7280',
    marginTop: 8,
  },

  sectionTitle: {
    fontSize: 19,
    fontWeight: '800',
    color: '#111827',
    marginTop: 25,
    marginBottom: 10,
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
    minHeight: 58,
  },

  timeline: {
    width: 25,
    alignItems: 'center',
  },

  timelineDotActive: {
    width: 13,
    height: 13,
    borderRadius: 7,
    backgroundColor: '#111827',
    marginTop: 3,
  },

  timelineDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: '#D1D5DB',
    marginTop: 4,
  },

  timelineLine: {
    width: 2,
    flex: 1,
    backgroundColor: '#E5E7EB',
    marginVertical: 3,
  },

  stopInfo: {
    flex: 1,
    paddingLeft: 10,
  },

  stopNameActive: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
  },

  stopTimeActive: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 3,
  },

  stopName: {
    fontSize: 14,
    fontWeight: '600',
    color: '#374151',
  },

  stopTime: {
    fontSize: 11,
    color: '#9CA3AF',
    marginTop: 3,
  },

  trackButton: {
    marginTop: 18,
    height: 54,
    borderRadius: 17,
    backgroundColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  trackButtonText: {
    color: '#FFFFFF',
    fontSize: 16,
    fontWeight: '800',
  },
});