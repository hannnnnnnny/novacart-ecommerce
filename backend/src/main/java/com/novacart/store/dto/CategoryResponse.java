package com.novacart.store.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        boolean active
) {
}
