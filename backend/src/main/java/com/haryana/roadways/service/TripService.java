package com.haryana.roadways.service;

import com.haryana.roadways.entity.Trip;
import com.haryana.roadways.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public Trip updateTrip(Long id, Trip updatedTrip) {
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with ID: " + id));

        existingTrip.setTripNumber(updatedTrip.getTripNumber());
        existingTrip.setBus(updatedTrip.getBus());
        existingTrip.setRoute(updatedTrip.getRoute());
        existingTrip.setDepartureTime(updatedTrip.getDepartureTime());
        existingTrip.setArrivalTime(updatedTrip.getArrivalTime());
        existingTrip.setStatus(updatedTrip.getStatus());

        return tripRepository.save(existingTrip);
    }

    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found with ID: " + id);
        }

        tripRepository.deleteById(id);
    }
}