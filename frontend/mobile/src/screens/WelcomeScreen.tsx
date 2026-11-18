import React, { useEffect, useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  TextInput,
  Modal,
  Alert,
} from 'react-native';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from '../navigation/AppNavigator';
import {
  resolveServerHost,
  setCustomServerHost,
  BACKEND_PORT,
} from '../config/api';
import { ApiService } from '../services/apiService';

type Props = NativeStackScreenProps<RootStackParamList, 'Welcome'>;

export default function WelcomeScreen({ navigation }: Props) {
  const [serverHost, setServerHost] = useState(resolveServerHost());
  const [isBackendHealthy, setIsBackendHealthy] = useState(false);
  const [showConfigModal, setShowConfigModal] = useState(false);
  const [inputHost, setInputHost] = useState(serverHost);

  const checkConnection = async () => {
    const ok = await ApiService.checkHealth();
    setIsBackendHealthy(ok);
  };

  useEffect(() => {
    checkConnection();
    const interval = setInterval(checkConnection, 8000);
    return () => clearInterval(interval);
  }, [serverHost]);

  const handleSaveHost = () => {
    const clean = inputHost.trim();
    if (!clean) {
      Alert.alert('Invalid Host', 'Please enter a valid IP address or hostname');
      return;
    }
    setCustomServerHost(clean);
    setServerHost(clean);
    setShowConfigModal(false);
    setTimeout(checkConnection, 500);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        {/* Backend Connectivity Status Pill */}
        <TouchableOpacity
          style={styles.connectionPill}
          activeOpacity={0.7}
          onPress={() => setShowConfigModal(true)}
        >
          <View
            style={[
              styles.connectionDot,
              { backgroundColor: isBackendHealthy ? '#10B981' : '#F59E0B' },
            ]}
          />
          <Text style={styles.connectionText}>
            {isBackendHealthy
              ? `Backend Live: ${serverHost}:${BACKEND_PORT}`
              : `Server: ${serverHost}:${BACKEND_PORT} (Tap to change)`}
          </Text>
        </TouchableOpacity>

        <Text style={styles.emoji}>🚌</Text>

        <Text style={styles.title}>TransitIQ</Text>

        <Text style={styles.subtitle}>
          Real-time public transport intelligence for Haryana Roadways
        </Text>

        <Text style={styles.description}>
          Track buses across all Haryana districts, plan schedules, receive live GPS
          telemetry, and make smarter travel decisions.
        </Text>

        <View style={styles.spacer} />

        <Text style={styles.question}>Select your role</Text>

        <TouchableOpacity
          style={styles.primaryButton}
          activeOpacity={0.85}
          onPress={() => navigation.navigate('PassengerHome')}
        >
          <Text style={styles.buttonTitle}>I'm a Passenger</Text>
          <Text style={styles.buttonSubtitle}>
            Find buses, search timetables & track live locations
          </Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.secondaryButton}
          activeOpacity={0.85}
          onPress={() => navigation.navigate('DriverHome')}
        >
          <Text style={styles.secondaryButtonTitle}>I'm a Driver</Text>
          <Text style={styles.secondaryButtonSubtitle}>
            Start trip lifecycle & stream GPS coordinates
          </Text>
        </TouchableOpacity>
      </View>

      <Text style={styles.footer}>
        Department of State Transport, Haryana • Final Year Project
      </Text>

      {/* Backend Server Configuration Modal */}
      <Modal
        visible={showConfigModal}
        transparent
        animationType="fade"
        onRequestClose={() => setShowConfigModal(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Backend Server Host</Text>
            <Text style={styles.modalSubtitle}>
              Enter your computer's LAN IP address so your phone can reach the
              Spring Boot backend (Port {BACKEND_PORT}).
            </Text>

            <TextInput
              style={styles.hostInput}
              value={inputHost}
              onChangeText={setInputHost}
              placeholder="e.g. 192.168.1.24"
              autoCapitalize="none"
              autoCorrect={false}
            />

            <View style={styles.modalButtons}>
              <TouchableOpacity
                style={styles.modalCancel}
                onPress={() => setShowConfigModal(false)}
              >
                <Text style={styles.modalCancelText}>Cancel</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.modalSave}
                onPress={handleSaveHost}
              >
                <Text style={styles.modalSaveText}>Connect</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
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
    paddingTop: 30,
  },

  connectionPill: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'flex-start',
    backgroundColor: '#FFFFFF',
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 20,
    marginBottom: 20,
    borderWidth: 1,
    borderColor: '#E5E7EB',
  },

  connectionDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 8,
  },

  connectionText: {
    fontSize: 11,
    fontWeight: '600',
    color: '#4B5563',
  },

  emoji: {
    fontSize: 54,
    marginBottom: 14,
  },

  title: {
    fontSize: 42,
    fontWeight: '800',
    color: '#111827',
    letterSpacing: -1,
  },

  subtitle: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '600',
    color: '#374151',
    lineHeight: 26,
  },

  description: {
    marginTop: 14,
    fontSize: 14,
    lineHeight: 22,
    color: '#6B7280',
    maxWidth: 360,
  },

  spacer: {
    flex: 1,
    minHeight: 30,
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
    fontSize: 11,
    marginBottom: 16,
  },

  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },

  modalCard: {
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    padding: 24,
    width: '100%',
    maxWidth: 380,
  },

  modalTitle: {
    fontSize: 19,
    fontWeight: '800',
    color: '#111827',
  },

  modalSubtitle: {
    fontSize: 13,
    color: '#6B7280',
    marginTop: 8,
    lineHeight: 18,
  },

  hostInput: {
    marginTop: 16,
    height: 48,
    borderWidth: 1,
    borderColor: '#D1D5DB',
    borderRadius: 12,
    paddingHorizontal: 14,
    fontSize: 15,
    color: '#111827',
    backgroundColor: '#F9FAFB',
  },

  modalButtons: {
    flexDirection: 'row',
    justifyContent: 'flex-end',
    gap: 12,
    marginTop: 20,
  },

  modalCancel: {
    paddingVertical: 10,
    paddingHorizontal: 16,
    borderRadius: 10,
  },

  modalCancelText: {
    color: '#6B7280',
    fontWeight: '600',
    fontSize: 14,
  },

  modalSave: {
    backgroundColor: '#111827',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 10,
  },

  modalSaveText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 14,
  },
});
