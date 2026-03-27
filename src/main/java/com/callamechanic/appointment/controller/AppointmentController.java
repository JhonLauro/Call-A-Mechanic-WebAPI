package com.callamechanic.appointment.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.callamechanic.appointment.dto.AppointmentResponseDTO;
import com.callamechanic.appointment.dto.CreateAppointmentRequestDTO;
import com.callamechanic.appointment.dto.UpdateStatusRequestDTO;
import com.callamechanic.appointment.service.AppointmentService;
import com.callamechanic.exception.ApiException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Get appointments (filtered by role)
     * GET /api/v1/appointments
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAppointments() {
        Long userId = extractUserIdFromToken();

        List<AppointmentResponseDTO> responseData = appointmentService.getAppointments(userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Create new appointment (client only)
     * POST /api/v1/appointments
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAppointment(
            @Valid @RequestBody CreateAppointmentRequestDTO request) {

        Long userId = extractUserIdFromToken();

        AppointmentResponseDTO responseData = appointmentService.createAppointment(userId, request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * Update appointment status (mechanic or admin only)
     * PATCH /api/v1/appointments/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDTO request) {

        Long userId = extractUserIdFromToken();

        AppointmentResponseDTO responseData = appointmentService.updateStatus(
                userId, id, request.getStatus()
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Mechanic claims appointment
     * PATCH /api/v1/appointments/{id}/claim
     */
    @PatchMapping("/{id}/claim")
    public ResponseEntity<Map<String, Object>> claimAppointment(@PathVariable Long id) {
        Long userId = extractUserIdFromToken();

        AppointmentResponseDTO responseData = appointmentService.claimAppointment(userId, id);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Extract user ID from JWT token in SecurityContext
     */
    private Long extractUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException("AUTH-002",
                    "Authentication required",
                    "Please provide a valid JWT token in the Authorization header.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof com.callamechanic.login.service.CustomUserDetails) {
            return ((com.callamechanic.login.service.CustomUserDetails) principal).getUserId();
        }

        throw new ApiException("AUTH-002",
                "Invalid token",
                "Unable to extract user information from the provided token.");
    }
}
