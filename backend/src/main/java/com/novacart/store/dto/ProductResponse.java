package com.novacart.store.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        int stockQuantity,
        String imageUrl,
        boolean active,
        CategoryResponse category
) {
}
