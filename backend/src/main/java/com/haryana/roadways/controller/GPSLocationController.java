package com.haryana.roadways.controller;

import com.haryana.roadways.entity.GPSLocation;
import com.haryana.roadways.service.GPSLocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gps")
public class GPSLocationController {

    private final GPSLocationService gpsLocationService;

    public GPSLocationController(GPSLocationService gpsLocationService) {
        this.gpsLocationService = gpsLocationService;
    }

    @PostMapping
    public ResponseEntity<GPSLocation> createLocation(
            @RequestBody GPSLocation location) {
        return ResponseEntity.ok(
                gpsLocationService.createLocation(location)
        );
    }

    @GetMapping
    public ResponseEntity<List<GPSLocation>> getAllLocations() {
        return ResponseEntity.ok(
                gpsLocationService.getAllLocations()
        );
    }

    @GetMapping("/trip/{tripId}")
public ResponseEntity<List<GPSLocation>> getLocationsByTripId(
        @PathVariable Long tripId) {

    return ResponseEntity.ok(
            gpsLocationService.getLocationsByTripId(tripId)
    );
}

@GetMapping("/trip/{tripId}/latest")
public ResponseEntity<GPSLocation> getLatestLocationByTripId(
        @PathVariable Long tripId) {

    return gpsLocationService.getLatestLocationByTripId(tripId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}

    @GetMapping("/{id}")
    public ResponseEntity<GPSLocation> getLocationById(
            @PathVariable Long id) {

        return gpsLocationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long id) {

        gpsLocationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}