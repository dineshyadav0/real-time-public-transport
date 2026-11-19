
package com.haryana.roadways.service;

import com.haryana.roadways.dto.TimetableImportResponse;
import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.entity.BusStop;
import com.haryana.roadways.entity.Route;
import com.haryana.roadways.entity.ScheduleStop;
import com.haryana.roadways.repository.BusScheduleRepository;
import com.haryana.roadways.repository.BusStopRepository;
import com.haryana.roadways.repository.RouteRepository;
import com.haryana.roadways.repository.ScheduleStopRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimetableImportService {

    private final BusScheduleRepository busScheduleRepository;
    private final BusStopRepository busStopRepository;
    private final RouteRepository routeRepository;
    private final ScheduleStopRepository scheduleStopRepository;

    public TimetableImportService(
            BusScheduleRepository busScheduleRepository,
            BusStopRepository busStopRepository,
            RouteRepository routeRepository,
            ScheduleStopRepository scheduleStopRepository) {

        this.busScheduleRepository = busScheduleRepository;
        this.busStopRepository = busStopRepository;
        this.routeRepository = routeRepository;
        this.scheduleStopRepository = scheduleStopRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public TimetableImportResponse importTimetable(
            MultipartFile schedulesFile,
            MultipartFile stopsFile) {

        if (schedulesFile == null || schedulesFile.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schedule CSV file is required");
        }

        if (stopsFile == null || stopsFile.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schedule stops CSV file is required");
        }

        TimetableImportResponse response =
                new TimetableImportResponse();

        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        importSchedules(schedulesFile, response, messages, errors);
        importStops(stopsFile, response, messages, errors);

        response.setMessages(messages);
        response.setErrors(errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Timetable import cancelled. No records were saved. "
                            + String.join("; ", errors));
        }

        return response;
    }

    private CSVParser createParser(MultipartFile file) throws Exception {

        Reader reader = new BufferedReader(
                new InputStreamReader(
                        file.getInputStream(),
                        StandardCharsets.UTF_8));

        reader.mark(1);

        int firstCharacter = reader.read();

        if (firstCharacter != 0xFEFF) {
            reader.reset();
        }

        return CSVParser.parse(
                reader,
                CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreEmptyLines(true)
                        .get());
    }

    private void importSchedules(
            MultipartFile file,
            TimetableImportResponse response,
            List<String> messages,
            List<String> errors) {

        try (CSVParser parser = createParser(file)) {

            validateHeaders(
                    parser,
                    List.of(
                            "routeNumber",
                            "depotName",
                            "source",
                            "destination",
                            "departureTime",
                            "arrivalTime",
                            "operatingDays",
                            "effectiveFrom",
                            "effectiveUntil",
                            "sourceUrl",
                            "verified"
                    ));

            List<Route> routes = routeRepository.findAll();

            for (CSVRecord record : parser) {

                response.setTotalRecords(
                        response.getTotalRecords() + 1);

                try {
                    String routeNumber =
                            required(record, "routeNumber");

                    String depotName =
                            required(record, "depotName");

                    String source =
                            required(record, "source");

                    String destination =
                            required(record, "destination");

                    LocalTime departureTime =
                            LocalTime.parse(
                                    required(record, "departureTime"));

                    LocalTime arrivalTime =
                            optionalTime(record, "arrivalTime");

                    LocalDate effectiveFrom =
                            optionalDate(record, "effectiveFrom");

                    LocalDate effectiveUntil =
                            optionalDate(record, "effectiveUntil");

                    if (effectiveFrom != null
                            && effectiveUntil != null
                            && effectiveUntil.isBefore(effectiveFrom)) {

                        throw new IllegalArgumentException(
                                "Effective end date is before start date");
                    }

                    Optional<Route> routeOptional = routes.stream()
                            .filter(route ->
                                    route.getRouteNumber()
                                            .equalsIgnoreCase(routeNumber))
                            .findFirst();

                    if (routeOptional.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Route not found: " + routeNumber);
                    }

                    Route route = routeOptional.get();

                    if (!route.getSource().equalsIgnoreCase(source)
                            || !route.getDestination()
                            .equalsIgnoreCase(destination)) {

                        throw new IllegalArgumentException(
                                "CSV source/destination do not match route "
                                        + routeNumber);
                    }

                    boolean duplicate =
                            busScheduleRepository.findAll()
                                    .stream()
                                    .anyMatch(existing ->
                                            existing.getRoute().getId()
                                                    .equals(route.getId())
                                                    && existing.getDepotName()
                                                    .equalsIgnoreCase(depotName)
                                                    && existing.getDepartureTime()
                                                    .equals(departureTime)
                                                    && sameDate(
                                                            existing.getEffectiveFrom(),
                                                            effectiveFrom)
                                                    && sameDate(
                                                            existing.getEffectiveUntil(),
                                                            effectiveUntil));

                    if (duplicate) {

                        response.setSkippedRecords(
                                response.getSkippedRecords() + 1);

                        messages.add(
                                "Duplicate schedule skipped: "
                                        + routeNumber + " at "
                                        + departureTime);

                        continue;
                    }

                    BusSchedule schedule = new BusSchedule();

                    schedule.setRoute(route);
                    schedule.setDepotName(depotName);
                    schedule.setDepartureTime(departureTime);
                    schedule.setArrivalTime(arrivalTime);

                    schedule.setOperatingDays(
                            optionalValue(record, "operatingDays"));

                    schedule.setEffectiveFrom(effectiveFrom);
                    schedule.setEffectiveUntil(effectiveUntil);

                    schedule.setSourceUrl(
                            optionalValue(record, "sourceUrl"));

                    schedule.setVerified(false);

                    busScheduleRepository.save(schedule);

                    response.setImportedRecords(
                            response.getImportedRecords() + 1);

                    messages.add(
                            "Schedule imported: " + routeNumber);

                } catch (Exception exception) {

                    response.setFailedRecords(
                            response.getFailedRecords() + 1);

                    errors.add(
                            "Schedule CSV row "
                                    + record.getRecordNumber()
                                    + ": " + exception.getMessage());
                }
            }

        } catch (Exception exception) {

            throw new IllegalArgumentException(
                    "Unable to read schedule CSV: "
                            + exception.getMessage(),
                    exception);
        }
    }

    private void importStops(
            MultipartFile file,
            TimetableImportResponse response,
            List<String> messages,
            List<String> errors) {

        try (CSVParser parser = createParser(file)) {

            validateHeaders(
                    parser,
                    List.of(
                            "routeNumber",
                            "departureTime",
                            "stopName",
                            "district",
                            "stopOrder",
                            "arrivalTime",
                            "stopDepartureTime"
                    ));

            for (CSVRecord record : parser) {

                response.setTotalRecords(
                        response.getTotalRecords() + 1);

                try {
                    String routeNumber =
                            required(record, "routeNumber");

                    LocalTime scheduleDeparture =
                            LocalTime.parse(
                                    required(record, "departureTime"));

                    String stopName =
                            required(record, "stopName");

                    String district =
                            required(record, "district");

                    int stopOrder = Integer.parseInt(
                            required(record, "stopOrder"));

                    if (stopOrder <= 0) {
                        throw new IllegalArgumentException(
                                "Stop order must be positive");
                    }

                    LocalTime arrivalTime =
                            optionalTime(record, "arrivalTime");

                    LocalTime departureTime =
                            optionalTime(record, "stopDepartureTime");

                    List<BusSchedule> matchingSchedules =
                            busScheduleRepository.findAll()
                                    .stream()
                                    .filter(schedule ->
                                            schedule.getRoute()
                                                    .getRouteNumber()
                                                    .equalsIgnoreCase(routeNumber)
                                                    && schedule.getDepartureTime()
                                                    .equals(scheduleDeparture))
                                    .toList();

                    if (matchingSchedules.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Matching schedule not found for "
                                        + routeNumber + " at "
                                        + scheduleDeparture);
                    }

                    if (matchingSchedules.size() > 1) {
                        throw new IllegalArgumentException(
                                "Multiple schedules match "
                                        + routeNumber + " at "
                                        + scheduleDeparture
                                        + ". Stop CSV needs depot identification.");
                    }

                    BusSchedule schedule = matchingSchedules.get(0);

                    List<ScheduleStop> existingStops =
                            scheduleStopRepository
                                    .findByScheduleIdOrderByStopOrderAsc(
                                            schedule.getId());

                    boolean duplicateOrder =
                            existingStops.stream()
                                    .anyMatch(stop ->
                                            stop.getStopOrder() == stopOrder);

                    if (duplicateOrder) {

                        response.setSkippedRecords(
                                response.getSkippedRecords() + 1);

                        messages.add(
                                "Duplicate stop order skipped for schedule "
                                        + schedule.getId()
                                        + ": " + stopOrder);

                        continue;
                    }

                    BusStop stop =
                            busStopRepository
                                    .findByNameIgnoreCaseAndDistrictIgnoreCase(
                                            stopName, district)
                                    .orElseGet(() -> {

                                        BusStop newStop = new BusStop();

                                        newStop.setName(stopName);
                                        newStop.setDistrict(district);

                                        return busStopRepository.save(newStop);
                                    });

                    ScheduleStop scheduleStop =
                            new ScheduleStop();

                    scheduleStop.setSchedule(schedule);
                    scheduleStop.setStop(stop);
                    scheduleStop.setStopOrder(stopOrder);
                    scheduleStop.setArrivalTime(arrivalTime);
                    scheduleStop.setDepartureTime(departureTime);

                    scheduleStopRepository.save(scheduleStop);

                    response.setImportedRecords(
                            response.getImportedRecords() + 1);

                    messages.add(
                            "Stop imported: " + stopName);

                } catch (Exception exception) {

                    response.setFailedRecords(
                            response.getFailedRecords() + 1);

                    errors.add(
                            "Stops CSV row "
                                    + record.getRecordNumber()
                                    + ": " + exception.getMessage());
                }
            }

        } catch (Exception exception) {

            throw new IllegalArgumentException(
                    "Unable to read stops CSV: "
                            + exception.getMessage(),
                    exception);
        }
    }

    private void validateHeaders(
            CSVParser parser,
            List<String> requiredHeaders) {

        List<String> actualHeaders = new ArrayList<>();

        for (String header : parser.getHeaderMap().keySet()) {
            actualHeaders.add(
                    header.replace("\uFEFF", "").trim());
        }

        for (String requiredHeader : requiredHeaders) {

            if (!actualHeaders.contains(requiredHeader)) {
                throw new IllegalArgumentException(
                        "Missing CSV header: " + requiredHeader);
            }
        }
    }

    private boolean sameDate(
            LocalDate first,
            LocalDate second) {

        return first == null
                ? second == null
                : first.equals(second);
    }

    private String required(
            CSVRecord record,
            String column) {

        String value = optionalValue(record, column);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Missing required column value: " + column);
        }

        return value;
    }

    private String optionalValue(
            CSVRecord record,
            String column) {

        if (!record.isMapped(column)) {
            return null;
        }

        String value = record.get(column);

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private LocalTime optionalTime(
            CSVRecord record,
            String column) {

        String value = optionalValue(record, column);

        if (value == null) {
            return null;
        }

        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Invalid time in " + column + ": " + value);
        }
    }

    private LocalDate optionalDate(
            CSVRecord record,
            String column) {

        String value = optionalValue(record, column);

        if (value == null) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "Invalid date in " + column + ": " + value);
        }
    }
}
