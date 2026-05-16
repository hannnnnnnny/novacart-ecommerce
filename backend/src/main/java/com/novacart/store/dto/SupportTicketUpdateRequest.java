package com.novacart.store.dto;

import com.novacart.store.entity.SupportTicketStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SupportTicketUpdateRequest(
        @NotNull(message = "Support ticket status is required.")
        SupportTicketStatus status,

        @Size(max = 1_000, message = "Internal notes must be 1,000 characters or fewer.")
        String internalNotes
) {
}
