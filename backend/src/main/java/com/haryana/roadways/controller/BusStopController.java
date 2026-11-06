package com.haryana.roadways.controller;

import com.haryana.roadways.entity.BusStop;
import com.haryana.roadways.service.BusStopService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class BusStopController {

    private final BusStopService busStopService;

    public BusStopController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    // Create a bus stop
    @PostMapping
    public ResponseEntity<BusStop> createBusStop(
            @RequestBody BusStop busStop) {

        return ResponseEntity.ok(
                busStopService.createBusStop(busStop));
    }

    // Get all bus stops
    @GetMapping
    public ResponseEntity<List<BusStop>> getAllBusStops() {

        return ResponseEntity.ok(
                busStopService.getAllBusStops());
    }

    // Get bus stop by ID
    @GetMapping("/{id}")
    public ResponseEntity<BusStop> getBusStopById(
            @PathVariable Long id) {

        return busStopService.getBusStopById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get bus stops by district
    @GetMapping("/district/{district}")
    public ResponseEntity<List<BusStop>> getBusStopsByDistrict(
            @PathVariable String district) {

        return ResponseEntity.ok(
                busStopService.getBusStopsByDistrict(district));
    }

    // Update bus stop
    @PutMapping("/{id}")
    public ResponseEntity<BusStop> updateBusStop(
            @PathVariable Long id,
            @RequestBody BusStop busStop) {

        return ResponseEntity.ok(
                busStopService.updateBusStop(id, busStop));
    }

    // Delete bus stop
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBusStop(
            @PathVariable Long id) {

        busStopService.deleteBusStop(id);

        return ResponseEntity.noContent().build();
    }
}