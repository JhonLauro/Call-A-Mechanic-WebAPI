package com.callamechanic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles custom API exceptions (AUTH-001, DB-002, etc.)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        HttpStatus status = mapCodeToStatus(ex.getCode());
        return ResponseEntity.status(status).body(
                buildError(ex.getCode(), ex.getMessage(), ex.getDetails())
        );
    }

    // Handles @Valid validation failures → VALID-001
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            details.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                buildError("VALID-001", "Validation failed", details)
        );
    }

    // Catch-all for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildError("SYSTEM-001", "Internal server error", null)
        );
    }

    private Map<String, Object> buildError(String code, String message, Object details) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code",    code);
        error.put("message", message);
        error.put("details", details);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success",   false);
        body.put("data",      null);
        body.put("error",     error);
        body.put("timestamp", Instant.now().toString());
        return body;
    }

    private HttpStatus mapCodeToStatus(String code) {
        return switch (code) {
            case "AUTH-001"   -> HttpStatus.UNAUTHORIZED;
            case "AUTH-002"   -> HttpStatus.UNAUTHORIZED;
            case "AUTH-003"   -> HttpStatus.FORBIDDEN;
            case "VALID-001"  -> HttpStatus.BAD_REQUEST;
            case "DB-001"     -> HttpStatus.NOT_FOUND;
            case "DB-002"     -> HttpStatus.CONFLICT;
            case "SYSTEM-001" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default           -> HttpStatus.BAD_REQUEST;
        };
    }
}