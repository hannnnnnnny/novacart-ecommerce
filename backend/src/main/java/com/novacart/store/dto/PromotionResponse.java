package com.novacart.store.dto;

import com.novacart.store.entity.PromotionDiscountType;
import com.novacart.store.entity.PromotionTargetType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record PromotionResponse(
        Long id,
        String name,
        String description,
        PromotionDiscountType discountType,
        BigDecimal discountValue,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        PromotionTargetType targetType,
        List<String> targetValues,
        Instant createdAt,
        Instant updatedAt
) {
}
