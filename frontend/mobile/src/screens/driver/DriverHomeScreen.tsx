import React from 'react';
import { SafeAreaView, Text, StyleSheet } from 'react-native';

export default function DriverHomeScreen() {
  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Driver Home</Text>
      <Text style={styles.subtitle}>
        Your trip and GPS dashboard will be built here.
      </Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 24,
  },
  title: {
    fontSize: 28,
    fontWeight: '800',
  },
  subtitle: {
    marginTop: 10,
    textAlign: 'center',
    color: '#6B7280',
  },
});
