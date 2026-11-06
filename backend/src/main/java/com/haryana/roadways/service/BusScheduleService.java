
package com.haryana.roadways.service;

import com.haryana.roadways.dto.JourneySearchResponse;
import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.entity.Route;
import com.haryana.roadways.entity.ScheduleStop;
import com.haryana.roadways.repository.BusScheduleRepository;
import com.haryana.roadways.repository.RouteRepository;
import com.haryana.roadways.repository.ScheduleStopRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

@Service
public class BusScheduleService {

    private final BusScheduleRepository busScheduleRepository;
    private final RouteRepository routeRepository;
    private final ScheduleStopRepository scheduleStopRepository;

    public BusScheduleService(
            BusScheduleRepository busScheduleRepository,
            RouteRepository routeRepository,
            ScheduleStopRepository scheduleStopRepository) {

        this.busScheduleRepository = busScheduleRepository;
        this.routeRepository = routeRepository;
        this.scheduleStopRepository = scheduleStopRepository;
    }

    public BusSchedule createSchedule(BusSchedule schedule) {

        if (schedule.getRoute() == null ||
                schedule.getRoute().getId() == null) {
            throw new IllegalArgumentException("Route ID is required");
        }

        Long routeId = schedule.getRoute().getId();

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() ->
                        new RuntimeException("Route not found: " + routeId));

        schedule.setRoute(route);

        return busScheduleRepository.save(schedule);
    }

    public List<BusSchedule> getAllSchedules() {
        return busScheduleRepository.findAll();
    }

    public BusSchedule getScheduleById(Long id) {
        return busScheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Schedule not found: " + id));
    }

    public List<BusSchedule> getSchedulesByRoute(Long routeId) {
        return busScheduleRepository
                .findByRouteIdOrderByDepartureTimeAsc(routeId);
    }

    public List<BusSchedule> getSchedulesByDepot(String depotName) {
        return busScheduleRepository
                .findByDepotNameIgnoreCase(depotName);
    }

    public List<BusSchedule> getSchedulesByTimeRange(
            LocalTime startTime,
            LocalTime endTime) {

        if (startTime == null || endTime == null ||
                startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(
                    "Invalid departure time range");
        }

        return busScheduleRepository
                .findByDepartureTimeBetweenOrderByDepartureTimeAsc(
                        startTime, endTime);
    }

    public List<BusSchedule> searchSchedules(
            String source,
            String destination,
            LocalDate travelDate) {

        validateSearch(source, destination, travelDate);

        List<BusSchedule> schedules =
                busScheduleRepository.searchDirectSchedules(
                        source.trim(),
                        destination.trim(),
                        travelDate);

        return filterByOperatingDay(schedules, travelDate);
    }

    // Passenger journey search for imported and manually entered schedules.
    public List<JourneySearchResponse> searchJourneys(
            String source,
            String destination,
            LocalDate travelDate) {

        validateSearch(source, destination, travelDate);

        String requestedSource = source.trim();
        String requestedDestination = destination.trim();

        List<BusSchedule> allSchedules =
                busScheduleRepository.findAll();

        List<JourneySearchResponse> results = new ArrayList<>();

        for (BusSchedule schedule : allSchedules) {

            Route route = schedule.getRoute();

            if (route == null) {
                continue;
            }

            if (schedule.getEffectiveFrom() != null &&
                    travelDate.isBefore(schedule.getEffectiveFrom())) {
                continue;
            }

            if (schedule.getEffectiveUntil() != null &&
                    travelDate.isAfter(schedule.getEffectiveUntil())) {
                continue;
            }

            if (!operatesOnDate(schedule, travelDate)) {
                continue;
            }

            /*
             * Imported PDF timetable matching.
             */
            boolean importedSourceMatches =
                    equalsIgnoreCase(route.getSource(), requestedSource);

            boolean importedDestinationMatches =
                    equalsIgnoreCase(
                            schedule.getActualDestination(),
                            requestedDestination);

            boolean importedViaMatches =
                    containsLocation(
                            schedule.getVia(),
                            requestedDestination);

            boolean importedSchedule =
                    schedule.getSourceDocument() != null ||
                    schedule.getActualDestination() != null;

            if (importedSchedule) {

                if (importedSourceMatches &&
                        (importedDestinationMatches || importedViaMatches)) {

                    results.add(createJourneyResponse(
                            schedule,
                            route,
                            requestedSource,
                            requestedDestination,
                            travelDate,
                            schedule.getDepartureTime(),
                            schedule.getArrivalTime()
                    ));
                }

                continue;
            }

            /*
             * Existing manually entered schedule matching.
             */
            boolean startsAtRouteOrigin =
                    equalsIgnoreCase(route.getSource(), requestedSource);

            boolean endsAtRouteDestination =
                    equalsIgnoreCase(route.getDestination(), requestedDestination);

            List<ScheduleStop> stops =
                    scheduleStopRepository
                            .findByScheduleIdOrderByStopOrderAsc(
                                    schedule.getId());

            ScheduleStop sourceStop = null;
            ScheduleStop destinationStop = null;

            for (ScheduleStop stop : stops) {

                String stopName = stop.getStop().getName();

                if (equalsIgnoreCase(stopName, requestedSource)) {
                    sourceStop = stop;
                }

                if (equalsIgnoreCase(stopName, requestedDestination)) {
                    destinationStop = stop;
                }
            }

            boolean sourceExists =
                    startsAtRouteOrigin || sourceStop != null;

            boolean destinationExists =
                    endsAtRouteDestination || destinationStop != null;

            if (!sourceExists || !destinationExists) {
                continue;
            }

            boolean correctDirection;

            if (startsAtRouteOrigin && endsAtRouteDestination) {
                correctDirection = true;
            } else if (startsAtRouteOrigin && destinationStop != null) {
                correctDirection = true;
            } else if (sourceStop != null && endsAtRouteDestination) {
                correctDirection = true;
            } else if (sourceStop != null && destinationStop != null) {
                correctDirection =
                        sourceStop.getStopOrder() <
                        destinationStop.getStopOrder();
            } else {
                correctDirection = false;
            }

            if (!correctDirection) {
                continue;
            }

            LocalTime boardingTime = startsAtRouteOrigin
                    ? schedule.getDepartureTime()
                    : sourceStop.getDepartureTime();

            LocalTime passengerArrivalTime = endsAtRouteDestination
                    ? schedule.getArrivalTime()
                    : destinationStop.getArrivalTime();

            if (boardingTime == null) {
                continue;
            }

            results.add(createJourneyResponse(
                    schedule,
                    route,
                    requestedSource,
                    requestedDestination,
                    travelDate,
                    boardingTime,
                    passengerArrivalTime
            ));
        }

        results.sort((first, second) ->
                first.getBoardingTime().compareTo(second.getBoardingTime()));

        return results;
    }

    private JourneySearchResponse createJourneyResponse(
            BusSchedule schedule,
            Route route,
            String source,
            String destination,
            LocalDate travelDate,
            LocalTime boardingTime,
            LocalTime arrivalTime) {

        return new JourneySearchResponse(
                schedule.getId(),
                route.getRouteNumber(),
                schedule.getRouteHeading(),
                schedule.getDepotName(),
                source,
                destination,
                schedule.getVia(),
                schedule.getServiceType(),
                schedule.getOperatingDays(),
                travelDate,
                boardingTime,
                arrivalTime,
                schedule.getSourceDocument(),
                schedule.getSourcePage(),
                Boolean.TRUE.equals(schedule.getVerified())
        );
    }

    private boolean equalsIgnoreCase(String first, String second) {

        return first != null &&
                second != null &&
                first.trim().equalsIgnoreCase(second.trim());
    }

    private boolean containsLocation(String via, String location) {

        if (via == null || location == null || via.isBlank()) {
            return false;
        }

        String[] locations = via.split("[,;]");

        for (String item : locations) {
            if (item.trim().equalsIgnoreCase(location.trim())) {
                return true;
            }
        }

        return false;
    }

    private void validateSearch(
            String source,
            String destination,
            LocalDate travelDate) {

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source is required");
        }

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination is required");
        }

        if (travelDate == null) {
            throw new IllegalArgumentException("Travel date is required");
        }

        if (source.trim().equalsIgnoreCase(destination.trim())) {
            throw new IllegalArgumentException(
                    "Source and destination cannot be the same");
        }
    }

    private List<BusSchedule> filterByOperatingDay(
            List<BusSchedule> schedules,
            LocalDate travelDate) {

        String requestedDay = getDayCode(travelDate.getDayOfWeek());

        return schedules.stream()
                .filter(schedule -> operatesOnDay(schedule, requestedDay))
                .toList();
    }

    private boolean operatesOnDate(
            BusSchedule schedule,
            LocalDate travelDate) {

        return operatesOnDay(
                schedule,
                getDayCode(travelDate.getDayOfWeek()));
    }

    private boolean operatesOnDay(
            BusSchedule schedule,
            String requestedDay) {

        String days = schedule.getOperatingDays();

        if (days == null || days.isBlank()) {
            return false;
        }

        String normalizedDays =
                days.toUpperCase(Locale.ROOT).trim();

        if (normalizedDays.equals("ALL DAYS") ||
                normalizedDays.equals("DAILY") ||
                normalizedDays.equals("EVERYDAY") ||
                normalizedDays.equals("EVERY DAY")) {
            return true;
        }

        String[] dayEntries = normalizedDays.split("[,;\\s]+");

        for (String day : dayEntries) {

            if (day.equals(requestedDay)) {
                return true;
            }

            if (day.equals("MONDAY") && requestedDay.equals("MON")) return true;
            if (day.equals("TUESDAY") && requestedDay.equals("TUE")) return true;
            if (day.equals("WEDNESDAY") && requestedDay.equals("WED")) return true;
            if (day.equals("THURSDAY") && requestedDay.equals("THU")) return true;
            if (day.equals("FRIDAY") && requestedDay.equals("FRI")) return true;
            if (day.equals("SATURDAY") && requestedDay.equals("SAT")) return true;
            if (day.equals("SUNDAY") && requestedDay.equals("SUN")) return true;
        }

        return false;
    }

    private String getDayCode(DayOfWeek day) {

        return switch (day) {
            case MONDAY -> "MON";
            case TUESDAY -> "TUE";
            case WEDNESDAY -> "WED";
            case THURSDAY -> "THU";
            case FRIDAY -> "FRI";
            case SATURDAY -> "SAT";
            case SUNDAY -> "SUN";
        };
    }

    public BusSchedule updateSchedule(
            Long id,
            BusSchedule updatedSchedule) {

        BusSchedule existing = getScheduleById(id);

        if (updatedSchedule.getRoute() != null &&
                updatedSchedule.getRoute().getId() != null) {

            Long routeId = updatedSchedule.getRoute().getId();

            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() ->
                            new RuntimeException("Route not found: " + routeId));

            existing.setRoute(route);
        }

        existing.setDepotName(updatedSchedule.getDepotName());
        existing.setDepartureTime(updatedSchedule.getDepartureTime());
        existing.setArrivalTime(updatedSchedule.getArrivalTime());
        existing.setOperatingDays(updatedSchedule.getOperatingDays());
        existing.setEffectiveFrom(updatedSchedule.getEffectiveFrom());
        existing.setEffectiveUntil(updatedSchedule.getEffectiveUntil());
        existing.setSourceUrl(updatedSchedule.getSourceUrl());
        existing.setVerified(updatedSchedule.getVerified());

        return busScheduleRepository.save(existing);
    }

    // Delete associated stops and schedule atomically.
    @Transactional
    public void deleteSchedule(Long id) {

        if (!busScheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found: " + id);
        }

        scheduleStopRepository.deleteByScheduleId(id);
        busScheduleRepository.deleteById(id);
    }
}
