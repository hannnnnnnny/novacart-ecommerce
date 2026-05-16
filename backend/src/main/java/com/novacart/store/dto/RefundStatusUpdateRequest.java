package com.novacart.store.dto;

import com.novacart.store.entity.RefundStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefundStatusUpdateRequest(
        @NotNull(message = "Refund status is required.")
        RefundStatus status,

        @Size(max = 1_000, message = "Internal notes must be 1,000 characters or fewer.")
        String internalNotes
) {
}
