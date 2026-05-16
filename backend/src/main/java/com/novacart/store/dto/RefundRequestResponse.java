package com.novacart.store.dto;

import com.novacart.store.entity.RefundStatus;
import java.time.Instant;

public record RefundRequestResponse(
        Long id,
        Long orderId,
        String orderNumber,
        String customerName,
        String email,
        String reason,
        RefundStatus status,
        String internalNotes,
        Instant createdAt,
        Instant updatedAt
) {
}
