import React from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
} from 'react-native';

export default function DriverHomeScreen({ navigation }: any) {
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.content}
      >
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>Driver dashboard</Text>
            <Text style={styles.title}>Ready for your trip?</Text>
          </View>

          <View style={styles.profileCircle}>
            <Text style={styles.profileText}>D</Text>
          </View>
        </View>

        {/* Status */}
        <View style={styles.statusCard}>
          <View style={styles.statusIcon}>
            <Text style={styles.busIcon}>🚌</Text>
          </View>

          <View style={styles.statusContent}>
            <Text style={styles.statusLabel}>DRIVER STATUS</Text>
            <Text style={styles.statusTitle}>Off duty</Text>
            <Text style={styles.statusSubtitle}>
              Start a trip to begin GPS tracking
            </Text>
          </View>

          <View style={styles.offlineBadge}>
            <View style={styles.offlineDot} />
            <Text style={styles.offlineText}>OFFLINE</Text>
          </View>
        </View>

        {/* Trip selection */}
        <Text style={styles.sectionTitle}>Today's route</Text>

        <View style={styles.routeCard}>
          <View style={styles.routeTop}>
            <View>
              <Text style={styles.busNumber}>BUS 102</Text>
              <Text style={styles.routeName}>
                City Center → Railway Station
              </Text>
            </View>

            <View style={styles.routeBadge}>
              <Text style={styles.routeBadgeText}>ROUTE 102</Text>
            </View>
          </View>

          <View style={styles.routeLineContainer}>
            <View style={styles.routeDotActive} />
            <View style={styles.routeLine} />
            <View style={styles.routeDot} />
          </View>

          <View style={styles.routeStops}>
            <View>
              <Text style={styles.stopTitle}>City Center</Text>
              <Text style={styles.stopSubtitle}>Starting point</Text>
            </View>

            <View style={styles.destination}>
              <Text style={styles.stopTitle}>Railway Station</Text>
              <Text style={styles.stopSubtitle}>Destination</Text>
            </View>
          </View>
        </View>

        {/* Trip information */}
        <View style={styles.infoRow}>
          <View style={styles.infoCard}>
            <Text style={styles.infoIcon}>📍</Text>
            <Text style={styles.infoLabel}>Stops</Text>
            <Text style={styles.infoValue}>12</Text>
          </View>

          <View style={styles.infoCard}>
            <Text style={styles.infoIcon}>📏</Text>
            <Text style={styles.infoLabel}>Distance</Text>
            <Text style={styles.infoValue}>8.6 km</Text>
          </View>

          <View style={styles.infoCard}>
            <Text style={styles.infoIcon}>⏱</Text>
            <Text style={styles.infoLabel}>Duration</Text>
            <Text style={styles.infoValue}>32 min</Text>
          </View>
        </View>

        {/* GPS information */}
        <View style={styles.gpsCard}>
          <View style={styles.gpsHeader}>
            <View style={styles.gpsIconContainer}>
              <Text style={styles.gpsIcon}>📡</Text>
            </View>

            <View style={styles.gpsContent}>
              <Text style={styles.gpsTitle}>GPS tracking</Text>
              <Text style={styles.gpsSubtitle}>
                Location sharing starts when your trip begins
              </Text>
            </View>
          </View>

          <View style={styles.gpsStatus}>
            <View style={styles.gpsStatusDot} />
            <Text style={styles.gpsStatusText}>
              Ready to track your location
            </Text>
          </View>
        </View>

        {/* Start trip */}
        <TouchableOpacity
          style={styles.startButton}
          activeOpacity={0.85}
          onPress={() => navigation.navigate('ActiveTrip')}
        >
          <Text style={styles.startButtonIcon}>▶</Text>
          <Text style={styles.startButtonText}>Start Trip</Text>
        </TouchableOpacity>

        <Text style={styles.warning}>
          GPS tracking will only be active during the trip.
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

  content: {
    paddingHorizontal: 20,
    paddingBottom: 35,
  },

  header: {
    paddingTop: 18,
    paddingBottom: 22,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  greeting: {
    fontSize: 14,
    color: '#6B7280',
    marginBottom: 5,
  },

  title: {
    fontSize: 25,
    fontWeight: '800',
    color: '#111827',
  },

  profileCircle: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  profileText: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: '700',
  },

  statusCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 16,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  statusIcon: {
    width: 50,
    height: 50,
    borderRadius: 16,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },

  busIcon: {
    fontSize: 25,
  },

  statusContent: {
    flex: 1,
  },

  statusLabel: {
    fontSize: 9,
    fontWeight: '800',
    color: '#9CA3AF',
    letterSpacing: 0.7,
  },

  statusTitle: {
    fontSize: 17,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  statusSubtitle: {
    fontSize: 10,
    color: '#6B7280',
    marginTop: 3,
  },

  offlineBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#F3F4F6',
    borderRadius: 20,
    paddingHorizontal: 8,
    paddingVertical: 6,
  },

  offlineDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: '#9CA3AF',
    marginRight: 5,
  },

  offlineText: {
    fontSize: 8,
    fontWeight: '800',
    color: '#6B7280',
  },

  sectionTitle: {
    fontSize: 20,
    fontWeight: '800',
    color: '#111827',
    marginTop: 28,
    marginBottom: 12,
  },

  routeCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 17,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  routeTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },

  busNumber: {
    fontSize: 19,
    fontWeight: '800',
    color: '#111827',
  },

  routeName: {
    fontSize: 12,
    color: '#6B7280',
    marginTop: 4,
  },

  routeBadge: {
    backgroundColor: '#F3F4F6',
    borderRadius: 10,
    paddingHorizontal: 8,
    paddingVertical: 6,
    height: 27,
  },

  routeBadgeText: {
    fontSize: 8,
    fontWeight: '800',
    color: '#6B7280',
  },

  routeLineContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 24,
    paddingHorizontal: 4,
  },

  routeDotActive: {
    width: 14,
    height: 14,
    borderRadius: 7,
    backgroundColor: '#111827',
  },

  routeLine: {
    flex: 1,
    height: 3,
    backgroundColor: '#111827',
    marginHorizontal: 5,
  },

  routeDot: {
    width: 14,
    height: 14,
    borderRadius: 7,
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
    marginTop: 3,
  },

  destination: {
    alignItems: 'flex-end',
  },

  infoRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 12,
  },

  infoCard: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    padding: 12,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  infoIcon: {
    fontSize: 17,
  },

  infoLabel: {
    fontSize: 9,
    color: '#9CA3AF',
    marginTop: 7,
  },

  infoValue: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  gpsCard: {
    marginTop: 12,
    backgroundColor: '#FFFFFF',
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  gpsHeader: {
    flexDirection: 'row',
    alignItems: 'center',
  },

  gpsIconContainer: {
    width: 44,
    height: 44,
    borderRadius: 14,
    backgroundColor: '#F3F4F6',
    justifyContent: 'center',
    alignItems: 'center',
    marginRight: 12,
  },

  gpsIcon: {
    fontSize: 21,
  },

  gpsContent: {
    flex: 1,
  },

  gpsTitle: {
    fontSize: 14,
    fontWeight: '800',
    color: '#111827',
  },

  gpsSubtitle: {
    fontSize: 10,
    lineHeight: 15,
    color: '#6B7280',
    marginTop: 3,
  },

  gpsStatus: {
    marginTop: 14,
    paddingTop: 12,
    borderTopWidth: 1,
    borderColor: '#F0F1F3',
    flexDirection: 'row',
    alignItems: 'center',
  },

  gpsStatusDot: {
    width: 7,
    height: 7,
    borderRadius: 4,
    backgroundColor: '#10B981',
    marginRight: 7,
  },

  gpsStatusText: {
    fontSize: 10,
    color: '#059669',
    fontWeight: '600',
  },

  startButton: {
    marginTop: 18,
    height: 56,
    borderRadius: 18,
    backgroundColor: '#111827',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },

  startButtonIcon: {
    color: '#FFFFFF',
    fontSize: 14,
    marginRight: 9,
  },

  startButtonText: {
    color: '#FFFFFF',
    fontSize: 17,
    fontWeight: '800',
  },

  warning: {
    textAlign: 'center',
    fontSize: 10,
    color: '#9CA3AF',
    marginTop: 10,
  },
});