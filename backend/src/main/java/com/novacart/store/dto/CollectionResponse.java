package com.novacart.store.dto;

import com.novacart.store.entity.CollectionStatus;
import java.time.Instant;
import java.time.LocalDate;

public record CollectionResponse(
        Long id,
        String name,
        String slug,
        String description,
        String heroImageUrl,
        String displayImageUrl,
        CollectionStatus status,
        boolean featured,
        LocalDate startDate,
        LocalDate endDate,
        int sortOrder,
        long productCount,
        Instant createdAt,
        Instant updatedAt
) {
}
