
package com.haryana.roadways.controller;

import com.haryana.roadways.entity.BusSchedule;
import com.haryana.roadways.service.ScheduleVerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/schedules")
public class ScheduleAdminController {

    private final ScheduleVerificationService service;
    private final String adminApiKey;

    public ScheduleAdminController(
            ScheduleVerificationService service,
            @Value("${app.admin.api-key:}") String adminApiKey) {

        this.service = service;
        this.adminApiKey = adminApiKey;
    }

    private boolean isAuthorized(String suppliedKey) {

        if (adminApiKey == null || adminApiKey.isBlank()
                || suppliedKey == null) {
            return false;
        }

        return MessageDigest.isEqual(
                adminApiKey.getBytes(StandardCharsets.UTF_8),
                suppliedKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    private ResponseEntity<Map<String, String>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error",
                        "Missing or invalid administrator API key"
                ));
    }

    @GetMapping
    public ResponseEntity<?> getSchedules(
            @RequestHeader(
                    value = "X-Admin-Key",
                    required = false
            ) String key,
            @RequestParam boolean verified) {

        if (!isAuthorized(key)) {
            return unauthorized();
        }

        List<BusSchedule> schedules =
                service.getSchedulesByVerification(verified);

        return ResponseEntity.ok(schedules);
    }

    @PatchMapping("/{id}/verification")
    public ResponseEntity<?> updateVerification(
            @RequestHeader(
                    value = "X-Admin-Key",
                    required = false
            ) String key,
            @PathVariable Long id,
            @RequestParam boolean verified) {

        if (!isAuthorized(key)) {
            return unauthorized();
        }

        try {
            return ResponseEntity.ok(
                    service.updateVerification(id, verified)
            );
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", exception.getMessage()));
        }
    }
}

