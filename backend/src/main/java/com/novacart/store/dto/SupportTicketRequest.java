package com.novacart.store.dto;

import com.novacart.store.entity.SupportIssueType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SupportTicketRequest(
        @NotNull(message = "Issue type is required.")
        SupportIssueType issueType,

        @Size(max = 32, message = "Order number must be 32 characters or fewer.")
        String orderNumber,

        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        @Size(max = 180, message = "Email address must be 180 characters or fewer.")
        String email,

        @NotBlank(message = "Customer name is required.")
        @Size(max = 140, message = "Customer name must be 140 characters or fewer.")
        String customerName,

        @NotBlank(message = "Message is required.")
        @Size(max = 2_000, message = "Support message must be 2,000 characters or fewer.")
        String message
) {
}
