package com.novacart.store.dto;

public record FieldValidationError(
        String field,
        String message
) {
}
