package com.callamechanic.admin.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.callamechanic.admin.dto.AssignMechanicRequestDTO;
import com.callamechanic.admin.dto.CreateMechanicRequestDTO;
import com.callamechanic.admin.dto.CreateMechanicResponseDTO;
import com.callamechanic.admin.dto.DeleteUserResponseDTO;
import com.callamechanic.admin.dto.UserListResponseDTO;
import com.callamechanic.admin.service.AdminService;
import com.callamechanic.appointment.dto.AppointmentResponseDTO;
import com.callamechanic.appointment.service.AppointmentService;
import com.callamechanic.exception.ApiException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final AppointmentService appointmentService;

    public AdminController(AdminService adminService, AppointmentService appointmentService) {
        this.adminService = adminService;
        this.appointmentService = appointmentService;
    }

    /**
     * Get all clients and mechanics
     * GET /api/v1/admin/users
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Long userId = extractUserIdFromToken();

        UserListResponseDTO responseData = adminService.getAllUsers(userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Create mechanic account
     * POST /api/v1/admin/mechanics
     */
    @PostMapping("/mechanics")
    public ResponseEntity<Map<String, Object>> createMechanic(
            @Valid @RequestBody CreateMechanicRequestDTO request) {

        Long userId = extractUserIdFromToken();

        CreateMechanicResponseDTO responseData = adminService.createMechanic(
                userId,
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(),
                request.getMechanicId()
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * Delete user account
     * DELETE /api/v1/admin/users/{id}
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Long userId = extractUserIdFromToken();

        DeleteUserResponseDTO responseData = adminService.deleteUser(userId, id);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Assign mechanic to appointment
     * PATCH /api/v1/admin/appointments/{id}/assign-mechanic
     */
    @PatchMapping("/appointments/{id}/assign-mechanic")
    public ResponseEntity<Map<String, Object>> assignMechanic(
            @PathVariable Long id,
            @Valid @RequestBody AssignMechanicRequestDTO request) {

        Long userId = extractUserIdFromToken();

        AppointmentResponseDTO responseData = appointmentService.assignMechanicByAdmin(
                userId, id, request.getMechanicId()
        );

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
