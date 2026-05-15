package com.novacart.store.dto;

import com.novacart.store.entity.StockMovementType;
import java.time.Instant;

public record StockMovementResponse(
        Long id,
        Long productId,
        String productName,
        Long orderId,
        StockMovementType type,
        int quantityChange,
        int stockAfter,
        String reason,
        Instant createdAt
) {
}
