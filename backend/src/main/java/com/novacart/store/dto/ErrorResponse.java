package com.novacart.store.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        boolean success,
        String message,
        int status,
        String path,
        List<FieldValidationError> errors,
        Instant timestamp
) {

    public static ErrorResponse of(String message, int status, String path) {
        return new ErrorResponse(false, message, status, path, List.of(), Instant.now());
    }

    public static ErrorResponse withErrors(
            String message,
            int status,
            String path,
            List<FieldValidationError> errors
    ) {
        return new ErrorResponse(false, message, status, path, errors, Instant.now());
    }
}
