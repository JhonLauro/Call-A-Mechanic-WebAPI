package com.callamechanic.vehicle.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.callamechanic.exception.ApiException;
import com.callamechanic.vehicle.dto.VehicleRequestDTO;
import com.callamechanic.vehicle.dto.VehicleResponseDTO;
import com.callamechanic.vehicle.service.VehicleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vehicles")
@PreAuthorize("hasAnyRole('CLIENT', 'MECHANIC')")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Get all vehicles for authenticated user
     * GET /api/v1/vehicles
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVehicles() {
        Long userId = extractUserIdFromToken();

        List<VehicleResponseDTO> responseData = vehicleService.getAllVehiclesByUser(userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Get specific vehicle by ID
     * GET /api/v1/vehicles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVehicleById(@PathVariable Long id) {
        Long userId = extractUserIdFromToken();

        VehicleResponseDTO responseData = vehicleService.getVehicleById(id, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Create new vehicle
     * POST /api/v1/vehicles
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createVehicle(
            @Valid @RequestBody VehicleRequestDTO request) {

        Long userId = extractUserIdFromToken();

        VehicleResponseDTO responseData = vehicleService.createVehicle(request, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * Update vehicle
     * PUT /api/v1/vehicles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequestDTO request) {

        Long userId = extractUserIdFromToken();

        VehicleResponseDTO responseData = vehicleService.updateVehicle(id, request, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Delete vehicle
     * DELETE /api/v1/vehicles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVehicle(@PathVariable Long id) {
        Long userId = extractUserIdFromToken();

        vehicleService.deleteVehicle(id, userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", Map.of("message", "Vehicle deleted successfully"));
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
