package com.novacart.store.dto;

import com.novacart.store.entity.SupportIssueType;
import com.novacart.store.entity.SupportTicketStatus;
import java.time.Instant;

public record SupportTicketResponse(
        Long id,
        SupportIssueType issueType,
        String orderNumber,
        String email,
        String customerName,
        String message,
        SupportTicketStatus status,
        String internalNotes,
        Instant createdAt,
        Instant updatedAt
) {
}
