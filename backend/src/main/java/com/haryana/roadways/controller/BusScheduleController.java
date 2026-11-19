
package com.haryana.roadways.controller;

import com.haryana.roadways.dto.JourneySearchResponse;
import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.service.BusScheduleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class BusScheduleController {

    private final BusScheduleService busScheduleService;

    public BusScheduleController(
            BusScheduleService busScheduleService) {
        this.busScheduleService = busScheduleService;
    }

    @PostMapping
    public ResponseEntity<BusSchedule> createSchedule(
            @RequestBody BusSchedule schedule) {
        return ResponseEntity.ok(
                busScheduleService.createSchedule(schedule));
    }

    @GetMapping
    public ResponseEntity<List<BusSchedule>> getAllSchedules() {
        return ResponseEntity.ok(
                busScheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusSchedule> getScheduleById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                busScheduleService.getScheduleById(id));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<BusSchedule>> getSchedulesByRoute(
            @PathVariable Long routeId) {
        return ResponseEntity.ok(
                busScheduleService.getSchedulesByRoute(routeId));
    }

    @GetMapping("/depot/{depotName}")
    public ResponseEntity<List<BusSchedule>> getSchedulesByDepot(
            @PathVariable String depotName) {
        return ResponseEntity.ok(
                busScheduleService.getSchedulesByDepot(depotName));
    }

    @GetMapping("/time")
    public ResponseEntity<List<BusSchedule>> getSchedulesByTimeRange(
            @RequestParam LocalTime start,
            @RequestParam LocalTime end) {
        return ResponseEntity.ok(
                busScheduleService.getSchedulesByTimeRange(start, end));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BusSchedule>> searchSchedules(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam LocalDate travelDate) {
        return ResponseEntity.ok(
                busScheduleService.searchSchedules(
                        source, destination, travelDate));
    }

    @GetMapping("/journey")
    public ResponseEntity<List<JourneySearchResponse>> searchJourneys(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam LocalDate travelDate) {
        return ResponseEntity.ok(
                busScheduleService.searchJourneys(
                        source, destination, travelDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusSchedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody BusSchedule schedule) {
        return ResponseEntity.ok(
                busScheduleService.updateSchedule(id, schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id) {
        busScheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
