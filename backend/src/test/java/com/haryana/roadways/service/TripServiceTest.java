package com.haryana.roadways.service;

import com.haryana.roadways.entity.Trip;
import com.haryana.roadways.entity.TripStatus;
import com.haryana.roadways.repository.TripRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void startsScheduledTrip() {
        Trip trip = tripWithStatus(TripStatus.SCHEDULED);
        when(tripRepository.findById(7L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        Trip result = tripService.startTrip(7L);

        assertEquals(TripStatus.RUNNING, result.getStatus());
        verify(tripRepository).save(trip);
    }

    @Test
    void rejectsStartingAlreadyRunningTrip() {
        Trip trip = tripWithStatus(TripStatus.RUNNING);
        when(tripRepository.findById(7L)).thenReturn(Optional.of(trip));

        assertThrows(ResponseStatusException.class, () -> tripService.startTrip(7L));
        verify(tripRepository, never()).save(any());
    }

    @Test
    void completesRunningTrip() {
        Trip trip = tripWithStatus(TripStatus.RUNNING);
        when(tripRepository.findById(7L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        Trip result = tripService.completeTrip(7L);

        assertEquals(TripStatus.COMPLETED, result.getStatus());
    }

    private Trip tripWithStatus(TripStatus status) {
        Trip trip = new Trip();
        trip.setStatus(status);
        return trip;
    }
}
