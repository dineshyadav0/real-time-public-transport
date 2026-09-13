import React from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TextInput,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';

const buses = [
  {
    number: '102',
    route: 'City Center → Railway Station',
    eta: '8 min',
    distance: '2.4 km',
    occupancy: 72,
    delay: 18,
    walking: '3 min',
    catchChance: 87,
  },
  {
    number: '205',
    route: 'ISBT → City Center',
    eta: '14 min',
    distance: '4.1 km',
    occupancy: 46,
    delay: 9,
    walking: '5 min',
    catchChance: 74,
  },
];

export default function PassengerHomeScreen() {
  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.scrollContent}
      >
        {/* Header */}
        <View style={styles.header}>
          <View>
            <Text style={styles.greeting}>Good morning 👋</Text>
            <Text style={styles.title}>Where are you going?</Text>
          </View>

          <View style={styles.profileCircle}>
            <Text style={styles.profileText}>J</Text>
          </View>
        </View>

        {/* Search */}
        <View style={styles.searchBox}>
          <Text style={styles.searchIcon}>⌕</Text>

          <TextInput
            placeholder="Search destination or bus"
            placeholderTextColor="#9CA3AF"
            style={styles.searchInput}
          />
        </View>

        {/* Quick actions */}
        <View style={styles.quickActions}>
          <TouchableOpacity style={styles.quickAction}>
            <Text style={styles.quickIcon}>📍</Text>
            <Text style={styles.quickText}>Nearby</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.quickAction}>
            <Text style={styles.quickIcon}>⭐</Text>
            <Text style={styles.quickText}>Favorites</Text>
          </TouchableOpacity>

          <TouchableOpacity style={styles.quickAction}>
            <Text style={styles.quickIcon}>🕘</Text>
            <Text style={styles.quickText}>Recent</Text>
          </TouchableOpacity>
        </View>

        {/* Nearby buses */}
        <View style={styles.sectionHeader}>
          <Text style={styles.sectionTitle}>Nearby buses</Text>
          <TouchableOpacity>
            <Text style={styles.viewAll}>View map</Text>
          </TouchableOpacity>
        </View>

        {buses.map((bus) => (
          <TouchableOpacity
            key={bus.number}
            style={styles.busCard}
            activeOpacity={0.8}
          >
            {/* Bus heading */}
            <View style={styles.busHeader}>
              <View style={styles.busNumberContainer}>
                <Text style={styles.busEmoji}>🚌</Text>

                <View>
                  <Text style={styles.busNumber}>BUS {bus.number}</Text>
                  <Text style={styles.route}>{bus.route}</Text>
                </View>
              </View>

              <View style={styles.liveBadge}>
                <View style={styles.liveDot} />
                <Text style={styles.liveText}>LIVE</Text>
              </View>
            </View>

            {/* ETA */}
            <View style={styles.etaRow}>
              <View>
                <Text style={styles.etaLabel}>ARRIVES IN</Text>
                <Text style={styles.eta}>{bus.eta}</Text>
              </View>

              <View style={styles.divider} />

              <View>
                <Text style={styles.etaLabel}>DISTANCE</Text>
                <Text style={styles.distance}>{bus.distance}</Text>
              </View>
            </View>

            {/* Predictions */}
            <View style={styles.predictionRow}>
              <View style={styles.predictionBox}>
                <Text style={styles.predictionIcon}>👥</Text>

                <View>
                  <Text style={styles.predictionLabel}>Crowding</Text>
                  <Text style={styles.predictionValue}>
                    {bus.occupancy}% occupied
                  </Text>
                </View>
              </View>

              <View style={styles.predictionBox}>
                <Text style={styles.predictionIcon}>⚡</Text>

                <View>
                  <Text style={styles.predictionLabel}>Delay chance</Text>
                  <Text style={styles.predictionValue}>
                    {bus.delay}%
                  </Text>
                </View>
              </View>
            </View>

            {/* Catchability */}
            <View style={styles.catchCard}>
              <View>
                <Text style={styles.catchTitle}>
                  Chance you'll catch it
                </Text>

                <Text style={styles.catchSubtitle}>
                  🚶 {bus.walking} walk to stop
                </Text>
              </View>

              <View style={styles.catchCircle}>
                <Text style={styles.catchNumber}>
                  {bus.catchChance}%
                </Text>
              </View>
            </View>
          </TouchableOpacity>
        ))}

        {/* Intelligence banner */}
        <View style={styles.intelligenceCard}>
          <Text style={styles.intelligenceIcon}>✦</Text>

          <View style={styles.intelligenceContent}>
            <Text style={styles.intelligenceTitle}>
              Smart prediction
            </Text>

            <Text style={styles.intelligenceText}>
              ETAs and crowd levels are predicted using live and
              historical travel data.
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

  scrollContent: {
    paddingHorizontal: 20,
    paddingBottom: 30,
  },

  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingTop: 18,
    paddingBottom: 20,
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

  searchBox: {
    height: 54,
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  searchIcon: {
    fontSize: 28,
    color: '#6B7280',
    marginRight: 8,
  },

  searchInput: {
    flex: 1,
    fontSize: 15,
    color: '#111827',
  },

  quickActions: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 14,
    marginBottom: 28,
  },

  quickAction: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    borderRadius: 14,
    paddingVertical: 13,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  quickIcon: {
    fontSize: 18,
    marginBottom: 4,
  },

  quickText: {
    fontSize: 12,
    fontWeight: '600',
    color: '#374151',
  },

  sectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
  },

  sectionTitle: {
    fontSize: 20,
    fontWeight: '800',
    color: '#111827',
  },

  viewAll: {
    fontSize: 13,
    fontWeight: '600',
    color: '#4B5563',
  },

  busCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 17,
    marginBottom: 14,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  busHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },

  busNumberContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },

  busEmoji: {
    fontSize: 31,
    marginRight: 11,
  },

  busNumber: {
    fontSize: 16,
    fontWeight: '800',
    color: '#111827',
  },

  route: {
    fontSize: 12,
    color: '#6B7280',
    marginTop: 3,
  },

  liveBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#ECFDF5',
    borderRadius: 20,
    paddingHorizontal: 9,
    paddingVertical: 5,
  },

  liveDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: '#10B981',
    marginRight: 5,
  },

  liveText: {
    fontSize: 9,
    fontWeight: '800',
    color: '#059669',
  },

  etaRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 18,
    paddingVertical: 13,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    borderColor: '#F0F1F3',
  },

  etaLabel: {
    fontSize: 9,
    fontWeight: '700',
    color: '#9CA3AF',
    letterSpacing: 0.5,
  },

  eta: {
    fontSize: 25,
    fontWeight: '800',
    color: '#111827',
    marginTop: 2,
  },

  distance: {
    fontSize: 17,
    fontWeight: '700',
    color: '#374151',
    marginTop: 6,
  },

  divider: {
    width: 1,
    height: 35,
    backgroundColor: '#E5E7EB',
    marginHorizontal: 35,
  },

  predictionRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 12,
  },

  predictionBox: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#F7F9FC',
    borderRadius: 12,
    padding: 10,
  },

  predictionIcon: {
    fontSize: 18,
    marginRight: 8,
  },

  predictionLabel: {
    fontSize: 10,
    color: '#6B7280',
  },

  predictionValue: {
    fontSize: 12,
    fontWeight: '700',
    color: '#111827',
    marginTop: 2,
  },

  catchCard: {
    marginTop: 12,
    backgroundColor: '#F3F4F6',
    borderRadius: 14,
    padding: 12,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },

  catchTitle: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  catchSubtitle: {
    fontSize: 11,
    color: '#6B7280',
    marginTop: 4,
  },

  catchCircle: {
    width: 52,
    height: 52,
    borderRadius: 26,
    borderWidth: 4,
    borderColor: '#111827',
    justifyContent: 'center',
    alignItems: 'center',
  },

  catchNumber: {
    fontSize: 13,
    fontWeight: '800',
    color: '#111827',
  },

  intelligenceCard: {
    backgroundColor: '#111827',
    borderRadius: 18,
    padding: 17,
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 5,
  },

  intelligenceIcon: {
    fontSize: 28,
    color: '#FFFFFF',
    marginRight: 14,
  },

  intelligenceContent: {
    flex: 1,
  },

  intelligenceTitle: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '800',
    marginBottom: 4,
  },

  intelligenceText: {
    color: '#D1D5DB',
    fontSize: 11,
    lineHeight: 17,
  },
});