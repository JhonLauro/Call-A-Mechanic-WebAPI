package com.callamechanic.login.controller;

import com.callamechanic.login.dto.LoginRequestDTO;
import com.callamechanic.login.dto.LoginResponseDTO;
import com.callamechanic.login.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO responseData = loginService.login(
                request.getIdentifier(),
                request.getPassword()
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("data", responseData);
        body.put("error", null);
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }
}