package com.callamechanic.register.controller;

import com.callamechanic.register.dto.RegisterRequestDTO;
import com.callamechanic.register.dto.RegisterResponseDTO;
import com.callamechanic.register.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        RegisterResponseDTO responseData = registerService.register(
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword()
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
}