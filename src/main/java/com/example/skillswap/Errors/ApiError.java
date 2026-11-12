package com.example.skillswap.Errors;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        int status,
        String message,
        Map<String, String> errors,
        Instant timestamp
) {
    public ApiError(int status, String message) {
        this(status, message, Map.of(), Instant.now());
    }

    public ApiError(int status, String message, Map<String, String> errors) {
        this(status, message, errors, Instant.now());
    }
}
