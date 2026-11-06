package com.haryana.roadways.service;

import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.entity.BusStop;
import com.haryana.roadways.entity.ScheduleStop;
import com.haryana.roadways.repository.BusScheduleRepository;
import com.haryana.roadways.repository.BusStopRepository;
import com.haryana.roadways.repository.ScheduleStopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleStopService {

    private final ScheduleStopRepository scheduleStopRepository;
    private final BusScheduleRepository busScheduleRepository;
    private final BusStopRepository busStopRepository;

    public ScheduleStopService(
            ScheduleStopRepository scheduleStopRepository,
            BusScheduleRepository busScheduleRepository,
            BusStopRepository busStopRepository) {

        this.scheduleStopRepository = scheduleStopRepository;
        this.busScheduleRepository = busScheduleRepository;
        this.busStopRepository = busStopRepository;
    }

    // Add a stop to a bus schedule
    public ScheduleStop addStopToSchedule(
            Long scheduleId,
            Long stopId,
            Integer stopOrder,
            java.time.LocalTime arrivalTime,
            java.time.LocalTime departureTime) {

        if (stopOrder == null || stopOrder < 1) {
            throw new IllegalArgumentException(
                    "Stop order must be a positive number");
        }

        BusSchedule schedule = busScheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found: " + scheduleId));

        BusStop stop = busStopRepository.findById(stopId)
                .orElseThrow(() ->
                        new RuntimeException("Bus stop not found: " + stopId));

        ScheduleStop scheduleStop = new ScheduleStop();
        scheduleStop.setSchedule(schedule);
        scheduleStop.setStop(stop);
        scheduleStop.setStopOrder(stopOrder);
        scheduleStop.setArrivalTime(arrivalTime);
        scheduleStop.setDepartureTime(departureTime);

        return scheduleStopRepository.save(scheduleStop);
    }

    // Get all stops for a schedule in travel order
    public List<ScheduleStop> getStopsBySchedule(Long scheduleId) {

        if (!busScheduleRepository.existsById(scheduleId)) {
            throw new RuntimeException("Schedule not found: " + scheduleId);
        }

        return scheduleStopRepository
                .findByScheduleIdOrderByStopOrderAsc(scheduleId);
    }

    // Find schedules passing through a particular stop
    public List<ScheduleStop> getSchedulesByStop(Long stopId) {

        if (!busStopRepository.existsById(stopId)) {
            throw new RuntimeException("Bus stop not found: " + stopId);
        }

        return scheduleStopRepository.findByStopId(stopId);
    }

    // Remove a stop from a schedule
    public void removeStopFromSchedule(Long id) {

        if (!scheduleStopRepository.existsById(id)) {
            throw new RuntimeException("Schedule stop not found: " + id);
        }

        scheduleStopRepository.deleteById(id);
    }
}