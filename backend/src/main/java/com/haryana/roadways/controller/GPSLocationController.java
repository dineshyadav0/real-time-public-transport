
package com.haryana.roadways.controller;

import com.haryana.roadways.dto.GPSLocationResponse;
import com.haryana.roadways.dto.LocationUpdate;
import com.haryana.roadways.entity.GPSLocation;
import com.haryana.roadways.service.GPSLocationService;
import com.haryana.roadways.service.LocationBroadcastService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gps")
public class GPSLocationController {

    private final GPSLocationService gpsLocationService;
    private final LocationBroadcastService locationBroadcastService;

    public GPSLocationController(
            GPSLocationService gpsLocationService,
            LocationBroadcastService locationBroadcastService) {
        this.gpsLocationService = gpsLocationService;
        this.locationBroadcastService = locationBroadcastService;
    }

    @PostMapping
    public ResponseEntity<GPSLocationResponse> createLocation(
            @Valid @RequestBody GPSLocation location) {

        GPSLocation savedLocation =
                gpsLocationService.createLocation(location);

        LocationUpdate update = new LocationUpdate(
                savedLocation.getTrip().getId(),
                savedLocation.getLatitude(),
                savedLocation.getLongitude(),
                savedLocation.getSpeed(),
                savedLocation.getRecordedAt().toString()
        );

        // Broadcast only after the database save succeeds.
        locationBroadcastService.broadcastLocation(update);

        return ResponseEntity.ok(
                new GPSLocationResponse(savedLocation));
    }

    @GetMapping
    public ResponseEntity<List<GPSLocationResponse>> getAllLocations() {
        List<GPSLocationResponse> responses =
                gpsLocationService.getAllLocations()
                        .stream()
                        .map(GPSLocationResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<GPSLocationResponse>> getLocationsByTripId(
            @PathVariable Long tripId) {

        List<GPSLocationResponse> responses =
                gpsLocationService.getLocationsByTripId(tripId)
                        .stream()
                        .map(GPSLocationResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/trip/{tripId}/latest")
    public ResponseEntity<GPSLocationResponse> getLatestLocationByTripId(
            @PathVariable Long tripId) {

        return gpsLocationService.getLatestLocationByTripId(tripId)
                .map(GPSLocationResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GPSLocationResponse> getLocationById(
            @PathVariable Long id) {

        return gpsLocationService.getLocationById(id)
                .map(GPSLocationResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        gpsLocationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
