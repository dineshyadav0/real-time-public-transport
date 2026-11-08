package com.haryana.roadways.service;

import com.haryana.roadways.entity.GPSLocation;
import com.haryana.roadways.entity.Trip;
import com.haryana.roadways.entity.TripStatus;
import com.haryana.roadways.repository.GPSLocationRepository;
import com.haryana.roadways.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GPSLocationServiceTest {

    @Mock
    private GPSLocationRepository gpsLocationRepository;

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private GPSLocationService gpsLocationService;

    @Test
    void savesLocationForRunningTripAndSetsTimestamp() {
        Trip trip = new Trip();
        trip.setId(12L);
        trip.setStatus(TripStatus.RUNNING);

        GPSLocation incoming = new GPSLocation();
        Trip reference = new Trip();
        reference.setId(12L);
        incoming.setTrip(reference);
        incoming.setLatitude(28.8955);
        incoming.setLongitude(76.6066);
        incoming.setSpeed(35.0);

        when(tripRepository.findById(12L)).thenReturn(Optional.of(trip));
        when(gpsLocationRepository.save(any(GPSLocation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GPSLocation saved = gpsLocationService.createLocation(incoming);

        assertSame(trip, saved.getTrip());
        assertNotNull(saved.getRecordedAt());
        assertEquals(28.8955, saved.getLatitude());
        verify(gpsLocationRepository).save(incoming);
    }

    @Test
    void rejectsGpsForScheduledTrip() {
        Trip trip = new Trip();
        trip.setId(12L);
        trip.setStatus(TripStatus.SCHEDULED);

        GPSLocation incoming = new GPSLocation();
        Trip reference = new Trip();
        reference.setId(12L);
        incoming.setTrip(reference);
        incoming.setLatitude(28.9);
        incoming.setLongitude(76.6);

        when(tripRepository.findById(12L)).thenReturn(Optional.of(trip));

        assertThrows(ResponseStatusException.class,
                () -> gpsLocationService.createLocation(incoming));
        verify(gpsLocationRepository, never()).save(any());
    }

    @Test
    void preservesDeviceTimestampWhenProvided() {
        Trip trip = new Trip();
        trip.setId(12L);
        trip.setStatus(TripStatus.RUNNING);
        LocalDateTime timestamp = LocalDateTime.of(2026, 11, 7, 10, 15);

        GPSLocation incoming = new GPSLocation();
        Trip reference = new Trip();
        reference.setId(12L);
        incoming.setTrip(reference);
        incoming.setLatitude(28.9);
        incoming.setLongitude(76.6);
        incoming.setRecordedAt(timestamp);

        when(tripRepository.findById(12L)).thenReturn(Optional.of(trip));
        when(gpsLocationRepository.save(any(GPSLocation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GPSLocation saved = gpsLocationService.createLocation(incoming);

        assertEquals(timestamp, saved.getRecordedAt());
    }
}
