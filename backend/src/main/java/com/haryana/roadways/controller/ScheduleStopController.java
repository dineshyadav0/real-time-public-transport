package com.haryana.roadways.controller;

import com.haryana.roadways.entity.ScheduleStop;
import com.haryana.roadways.service.ScheduleStopService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedule-stops")
public class ScheduleStopController {

    private final ScheduleStopService scheduleStopService;

    public ScheduleStopController(
            ScheduleStopService scheduleStopService) {
        this.scheduleStopService = scheduleStopService;
    }

    // Add a stop to a schedule
    @PostMapping("/{scheduleId}/stops/{stopId}")
    public ResponseEntity<ScheduleStop> addStopToSchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long stopId,
            @RequestParam Integer stopOrder,
            @RequestParam(required = false) String arrivalTime,
            @RequestParam(required = false) String departureTime) {

        LocalTime arrival = arrivalTime == null
                ? null : LocalTime.parse(arrivalTime);

        LocalTime departure = departureTime == null
                ? null : LocalTime.parse(departureTime);

        return ResponseEntity.ok(
                scheduleStopService.addStopToSchedule(
                        scheduleId,
                        stopId,
                        stopOrder,
                        arrival,
                        departure
                )
        );
    }

    // Get all stops of a schedule in order
    @GetMapping("/{scheduleId}")
    public ResponseEntity<List<ScheduleStop>> getStopsBySchedule(
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(
                scheduleStopService.getStopsBySchedule(scheduleId));
    }

    // Find schedules passing through a stop
    @GetMapping("/stop/{stopId}")
    public ResponseEntity<List<ScheduleStop>> getSchedulesByStop(
            @PathVariable Long stopId) {

        return ResponseEntity.ok(
                scheduleStopService.getSchedulesByStop(stopId));
    }

    // Remove a stop from a schedule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStopFromSchedule(
            @PathVariable Long id) {

        scheduleStopService.removeStopFromSchedule(id);

        return ResponseEntity.noContent().build();
    }
}