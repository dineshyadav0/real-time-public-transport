package com.haryana.roadways.service;

import com.haryana.roadways.entity.Trip;
import com.haryana.roadways.entity.TripStatus;
import com.haryana.roadways.repository.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip createTrip(Trip trip) {
        if (trip.getStatus() == null) {
            trip.setStatus(TripStatus.SCHEDULED);
        }
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    @Transactional
    public Trip updateTrip(Long id, Trip updatedTrip) {
        Trip existingTrip = findTrip(id);
        if (existingTrip.getStatus() != TripStatus.SCHEDULED) {
            throw conflict("Only scheduled trips can have their details edited");
        }

        existingTrip.setTripNumber(updatedTrip.getTripNumber());
        existingTrip.setBus(updatedTrip.getBus());
        existingTrip.setRoute(updatedTrip.getRoute());
        existingTrip.setDepartureTime(updatedTrip.getDepartureTime());
        existingTrip.setArrivalTime(updatedTrip.getArrivalTime());
        // Status is deliberately controlled only by lifecycle endpoints.
        return tripRepository.save(existingTrip);
    }

    @Transactional
    public Trip startTrip(Long id) {
        Trip trip = findTrip(id);
        requireStatus(trip, TripStatus.SCHEDULED, "Only scheduled trips can be started");
        trip.setStatus(TripStatus.RUNNING);
        return tripRepository.save(trip);
    }

    @Transactional
    public Trip completeTrip(Long id) {
        Trip trip = findTrip(id);
        requireStatus(trip, TripStatus.RUNNING, "Only running trips can be completed");
        trip.setStatus(TripStatus.COMPLETED);
        return tripRepository.save(trip);
    }

    @Transactional
    public Trip cancelTrip(Long id) {
        Trip trip = findTrip(id);
        if (trip.getStatus() != TripStatus.SCHEDULED
                && trip.getStatus() != TripStatus.RUNNING) {
            throw conflict("Only scheduled or running trips can be cancelled");
        }
        trip.setStatus(TripStatus.CANCELLED);
        return tripRepository.save(trip);
    }

    public void deleteTrip(Long id) {
        Trip trip = findTrip(id);
        if (trip.getStatus() == TripStatus.RUNNING) {
            throw conflict("A running trip cannot be deleted");
        }
        tripRepository.delete(trip);
    }

    private Trip findTrip(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trip not found"));
    }

    private void requireStatus(Trip trip, TripStatus expected, String message) {
        if (trip.getStatus() != expected) {
            throw conflict(message);
        }
    }

    private ResponseStatusException conflict(String message) {
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}
