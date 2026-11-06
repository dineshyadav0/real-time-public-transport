package com.haryana.roadways.controller;

import com.haryana.roadways.dto.TimetableImportResponse;
import com.haryana.roadways.service.TimetableImportService;
import com.haryana.roadways.service.RohtakTimetableImportService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/timetable-import")
public class TimetableImportController {

    private final TimetableImportService timetableImportService;
    private final RohtakTimetableImportService rohtakTimetableImportService;

    public TimetableImportController(
            TimetableImportService timetableImportService,
            RohtakTimetableImportService rohtakTimetableImportService) {

        this.timetableImportService = timetableImportService;
        this.rohtakTimetableImportService = rohtakTimetableImportService;
    }

    // Existing two-file importer
    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data")
    public ResponseEntity<?> uploadTimetable(
            @RequestParam("schedulesFile") MultipartFile schedulesFile,
            @RequestParam("stopsFile") MultipartFile stopsFile) {

        try {
            TimetableImportResponse response =
                    timetableImportService.importTimetable(
                            schedulesFile, stopsFile);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Timetable import rejected",
                            "message", exception.getMessage()
                    ));
        }
    }

    // New Rohtak PDF timetable importer
    @PostMapping(
            value = "/rohtak",
            consumes = "multipart/form-data")
    public ResponseEntity<?> uploadRohtakTimetable(
            @RequestParam("file") MultipartFile file) {

        try {
            TimetableImportResponse response =
                    rohtakTimetableImportService.importCsv(file);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Rohtak timetable import rejected",
                            "message", exception.getMessage()
                    ));
        }
    }
}