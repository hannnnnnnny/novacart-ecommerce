package com.novacart.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefundRequestCreateRequest(
        @NotBlank(message = "Order number is required.")
        @Size(max = 32, message = "Order number must be 32 characters or fewer.")
        String orderNumber,

        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        @Size(max = 180, message = "Email address must be 180 characters or fewer.")
        String email,

        @NotBlank(message = "Refund reason is required.")
        @Size(max = 1_200, message = "Refund reason must be 1,200 characters or fewer.")
        String reason
) {
}
