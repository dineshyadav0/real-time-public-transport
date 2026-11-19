package com.haryana.roadways.service;

import com.haryana.roadways.dto.TimetableImportResponse;
import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.entity.Route;
import com.haryana.roadways.repository.BusScheduleRepository;
import com.haryana.roadways.repository.RouteRepository;

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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RohtakTimetableImportService {

    private final BusScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;

    public RohtakTimetableImportService(
            BusScheduleRepository scheduleRepository,
            RouteRepository routeRepository) {

        this.scheduleRepository = scheduleRepository;
        this.routeRepository = routeRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public TimetableImportResponse importCsv(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Rohtak timetable CSV file is required");
        }

        TimetableImportResponse response =
                new TimetableImportResponse();

        List<String> messages = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (
                Reader reader = createReader(file);
                CSVParser parser = CSVParser.parse(
                        reader,
                        CSVFormat.DEFAULT.builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .setIgnoreEmptyLines(true)
                                .get())
        ) {

            validateHeaders(parser);

            for (CSVRecord record : parser) {

                response.setTotalRecords(
                        response.getTotalRecords() + 1);

                try {

                    String operator = required(record, "operator");

                    // Only Haryana Roadways services are imported.
                    if (!operator.equalsIgnoreCase("HR")) {

                        response.setSkippedRecords(
                                response.getSkippedRecords() + 1);

                        continue;
                    }

                    String routeHeading =
                            required(record, "routeHeading");

                    String depot =
                            required(record, "depot");

                    String source =
                            required(record, "source");

                    String destination =
                            required(record, "destination");

                    String via =
                            optional(record, "via");

                    LocalTime departureTime =
                            LocalTime.parse(
                                    required(record, "departureTime"));

                    String serviceType =
                            optional(record, "serviceType");

                    String serviceDays =
                            optional(record, "serviceDays");

                    Integer sourcePage =
                            Integer.parseInt(
                                    required(record, "sourcePage"));

                    String sourceDocument =
                            required(record, "sourceDocument");

                    if (sourcePage <= 0) {
                        throw new IllegalArgumentException(
                                "Source page must be positive");
                    }

                    if (sourceDocument.length() > 255) {
                        throw new IllegalArgumentException(
                                "Source document name is too long");
                    }

                    /*
                     * Generate a stable route number from
                     * the original PDF route heading.
                     */
                    String routeNumber =
                            createRouteNumber(routeHeading);

                    /*
                     * Find an existing route or create a new one.
                     */
                    Route route = routeRepository
                            .findByRouteNumberIgnoreCase(routeNumber)
                            .orElse(null);

                    if (route == null) {

                        route = new Route();

                        route.setRouteNumber(routeNumber);
                        route.setSource(source);
                        route.setDestination(routeHeading);
                        route.setTotalDistance(null);

                        route = routeRepository.save(route);

                    } else {

                        /*
                         * Never reuse a route number if its
                         * source or destination is inconsistent.
                         */
                        if (route.getSource() == null
                                || route.getDestination() == null
                                || !route.getSource()
                                        .equalsIgnoreCase(source)
                                || !route.getDestination()
                                        .equalsIgnoreCase(routeHeading)) {

                            throw new IllegalArgumentException(
                                    "Route details conflict for route number: "
                                            + routeNumber);
                        }
                    }

                    /*
                     * Check whether this exact timetable
                     * record already exists.
                     */
                    boolean duplicate =
                            scheduleRepository
                                    .existsByRouteIdAndDepotNameIgnoreCaseAndDepartureTimeAndRouteHeadingAndActualDestinationAndViaAndServiceTypeAndSourcePage(
                                            route.getId(),
                                            depot,
                                            departureTime,
                                            routeHeading,
                                            destination,
                                            via,
                                            serviceType,
                                            sourcePage);

                    if (duplicate) {

                        response.setSkippedRecords(
                                response.getSkippedRecords() + 1);

                        messages.add(
                                "Duplicate skipped: "
                                        + routeHeading
                                        + " at "
                                        + departureTime);

                        continue;
                    }

                    /*
                     * Create timetable schedule.
                     */
                    BusSchedule schedule = new BusSchedule();

                    schedule.setRoute(route);
                    schedule.setDepotName(depot);
                    schedule.setDepartureTime(departureTime);

                    // The source PDF does not provide arrival times.
                    schedule.setArrivalTime(null);

                    schedule.setOperatingDays(serviceDays);

                    // No effective dates are supplied by the extraction.
                    schedule.setEffectiveFrom(null);
                    schedule.setEffectiveUntil(null);

                    schedule.setSourceUrl(null);

                    // Preserve original timetable information.
                    schedule.setRouteHeading(routeHeading);
                    schedule.setActualDestination(destination);
                    schedule.setVia(via);
                    schedule.setServiceType(serviceType);
                    schedule.setSourcePage(sourcePage);
                    schedule.setSourceDocument(sourceDocument);

                    // Imported records require manual verification.
                    schedule.setVerified(false);

                    scheduleRepository.save(schedule);

                    response.setImportedRecords(
                            response.getImportedRecords() + 1);

                } catch (Exception exception) {

                    response.setFailedRecords(
                            response.getFailedRecords() + 1);

                    errors.add(
                            "CSV row "
                                    + record.getRecordNumber()
                                    + ": "
                                    + exception.getMessage());
                }
            }

        } catch (Exception exception) {

            throw new IllegalArgumentException(
                    "Unable to import Rohtak timetable: "
                            + exception.getMessage(),
                    exception);
        }

        response.setMessages(messages);
        response.setErrors(errors);

        /*
         * Reject the complete import if any record is invalid.
         * The transaction rolls back all changes.
         */
        if (!errors.isEmpty()) {

            throw new IllegalArgumentException(
                    "Import cancelled. No records were saved. "
                            + String.join("; ", errors));
        }

        return response;
    }

    private Reader createReader(MultipartFile file)
            throws Exception {

        Reader reader = new BufferedReader(
                new InputStreamReader(
                        file.getInputStream(),
                        StandardCharsets.UTF_8));

        reader.mark(1);

        int firstCharacter = reader.read();

        if (firstCharacter != 0xFEFF) {
            reader.reset();
        }

        return reader;
    }

    private void validateHeaders(CSVParser parser) {

        List<String> requiredHeaders = List.of(
                "routeHeading",
                "depot",
                "source",
                "destination",
                "via",
                "departureTime",
                "serviceType",
                "operator",
                "serviceDays",
                "sourcePage",
                "sourceDocument"
        );

        List<String> actualHeaders =
                new ArrayList<>(parser.getHeaderMap().keySet());

        for (String header : requiredHeaders) {

            if (!actualHeaders.contains(header)) {

                throw new IllegalArgumentException(
                        "Missing CSV header: " + header);
            }
        }
    }

    private String required(
            CSVRecord record,
            String column) {

        String value = optional(record, column);

        if (value == null) {

            throw new IllegalArgumentException(
                    "Missing value: " + column);
        }

        return value;
    }

    private String optional(
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

    private String createRouteNumber(String routeHeading) {

        String code = routeHeading
                .toUpperCase()
                .replaceAll("[^A-Z0-9]+", "-")
                .replaceAll("^-|-$", "");

        return "HR-ROHTAK-" + code;
    }
}