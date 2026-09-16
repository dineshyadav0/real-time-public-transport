package com.haryana.roadways.service;

import com.haryana.roadways.entity.GPSLocation;
import com.haryana.roadways.repository.GPSLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GPSLocationService {

    private final GPSLocationRepository gpsLocationRepository;

    public GPSLocationService(GPSLocationRepository gpsLocationRepository) {
        this.gpsLocationRepository = gpsLocationRepository;
    }

    public GPSLocation createLocation(GPSLocation location) {
        return gpsLocationRepository.save(location);
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
            throw new RuntimeException("GPS Location not found with ID: " + id);
        }

        gpsLocationRepository.deleteById(id);
    }
}