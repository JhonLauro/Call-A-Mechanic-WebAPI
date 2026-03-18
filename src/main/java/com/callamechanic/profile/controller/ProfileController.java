package com.callamechanic.profile.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.callamechanic.exception.ApiException;
import com.callamechanic.profile.dto.EditPasswordRequestDTO;
import com.callamechanic.profile.dto.EditProfileRequestDTO;
import com.callamechanic.profile.dto.ProfileResponseDTO;
import com.callamechanic.profile.dto.UploadPhotoResponseDTO;
import com.callamechanic.profile.service.ProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Get user profile
     * GET /api/v1/profile
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProfile() {
        Long userId = extractUserIdFromToken();

        ProfileResponseDTO responseData = profileService.getProfile(userId);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Edit user profile
     * PUT /api/v1/profile
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> editProfile(
            @Valid @RequestBody EditProfileRequestDTO request) {

        Long userId = extractUserIdFromToken();

        ProfileResponseDTO responseData = profileService.editProfile(userId, request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Change user password
     * PUT /api/v1/profile/password
     */
    @PutMapping("/password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @Valid @RequestBody EditPasswordRequestDTO request) {

        Long userId = extractUserIdFromToken();

        profileService.changePassword(userId, request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", null);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }

    /**
     * Upload user photo
     * POST /api/v1/profile/photo
     */
    @PostMapping("/photo")
    public ResponseEntity<Map<String, Object>> uploadPhoto(
            @RequestParam("file") MultipartFile file) throws IOException {

        try {
            Long userId = extractUserIdFromToken();

            // Validate file is not empty
            if (file.isEmpty()) {
                throw new ApiException("FILE-002",
                        "Empty file",
                        "The selected file is empty. Please choose a valid image file.");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/"))) {
                throw new ApiException("FILE-001",
                        "Invalid file type",
                        "Only image files (jpg, png, etc.) are allowed.");
            }

            byte[] photoData = file.getBytes();
            UploadPhotoResponseDTO responseData = profileService.uploadPhoto(userId, photoData);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("success", true);
            body.put("data", responseData);
            body.put("error", null);
            body.put("timestamp", Instant.now().toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("Error uploading photo: " + ex.getMessage());
            ex.printStackTrace();
            throw new ApiException("FILE-003",
                    "Upload failed",
                    "An error occurred while uploading the photo: " + ex.getMessage());
        }
    }

    /**
     * Extract user ID from JWT token in SecurityContext
     * This assumes the principal contains the user ID
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
