package com.haryana.roadways.service;

import com.haryana.roadways.entity.GPSLocation;
import com.haryana.roadways.entity.Trip;
import com.haryana.roadways.entity.TripStatus;
import com.haryana.roadways.repository.GPSLocationRepository;
import com.haryana.roadways.repository.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GPSLocationService {

    private final GPSLocationRepository gpsLocationRepository;
    private final TripRepository tripRepository;

    public GPSLocationService(
            GPSLocationRepository gpsLocationRepository,
            TripRepository tripRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
        this.tripRepository = tripRepository;
    }

    @Transactional
    public GPSLocation createLocation(GPSLocation incoming) {
        if (incoming.getTrip() == null || incoming.getTrip().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A valid trip ID is required");
        }

        Trip trip = tripRepository.findById(incoming.getTrip().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trip not found"));

        if (trip.getStatus() != TripStatus.RUNNING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "GPS updates are accepted only for running trips");
        }

        // Re-attach the managed trip; never persist client-supplied trip fields.
        incoming.setTrip(trip);
        if (incoming.getRecordedAt() == null) {
            incoming.setRecordedAt(LocalDateTime.now());
        }
        return gpsLocationRepository.save(incoming);
    }

    public List<GPSLocation> getAllLocations() {
        return gpsLocationRepository.findAll();
    }

    public List<GPSLocation> getLocationsByTripId(Long tripId) {
        return gpsLocationRepository.findByTripId(tripId);
    }

    public Optional<GPSLocation> getLatestLocationByTripId(Long tripId) {
        return gpsLocationRepository.findTopByTripIdOrderByRecordedAtDesc(tripId);
    }

    public Optional<GPSLocation> getLocationById(Long id) {
        return gpsLocationRepository.findById(id);
    }

    public void deleteLocation(Long id) {
        if (!gpsLocationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "GPS location not found");
        }
        gpsLocationRepository.deleteById(id);
    }
}
