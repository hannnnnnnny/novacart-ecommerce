package com.novacart.store.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CustomerProfileResponse(
        Long id,
        String name,
        String email,
        String phone,
        String addressSummary,
        String country,
        String region,
        String city,
        long totalOrders,
        BigDecimal totalSpent,
        BigDecimal averageOrderValue,
        Instant createdAt,
        Instant lastOrderAt,
        Instant updatedAt
) {
}
