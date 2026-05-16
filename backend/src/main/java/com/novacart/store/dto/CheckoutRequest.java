package com.novacart.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CheckoutRequest(
        @NotBlank(message = "Customer name is required.")
        @Size(max = 140, message = "Customer name must be 140 characters or fewer.")
        String customerName,

        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        @Size(max = 180, message = "Email address must be 180 characters or fewer.")
        String customerEmail,

        @Size(max = 40, message = "Phone number must be 40 characters or fewer.")
        String customerPhone,

        @NotBlank(message = "Shipping address is required.")
        @Size(max = 220, message = "Shipping address must be 220 characters or fewer.")
        String shippingAddress,

        @NotBlank(message = "City is required.")
        @Size(max = 120, message = "City must be 120 characters or fewer.")
        String city,

        @Size(max = 120, message = "Region must be 120 characters or fewer.")
        String region,

        @NotBlank(message = "Postal code is required.")
        @Size(max = 40, message = "Postal code must be 40 characters or fewer.")
        String postalCode,

        @NotBlank(message = "Country is required.")
        @Size(max = 80, message = "Country must be 80 characters or fewer.")
        String country,

        @Pattern(
                regexp = "^(STANDARD|EXPRESS|PICKUP)$",
                message = "Select a valid shipping method."
        )
        String shippingMethod,

        @Size(max = 80, message = "Payment method must be 80 characters or fewer.")
        String paymentMethod,

        @Size(max = 120, message = "Idempotency key must be 120 characters or fewer.")
        String idempotencyKey,

        Boolean refundPolicyAcknowledged,

        Boolean simulatePaymentFailure,

        @Valid
        @NotEmpty(message = "Your cart must include at least one item.")
        @Size(max = 100, message = "Checkout can include no more than 100 line items.")
        List<CheckoutItemRequest> items
) {
    public CheckoutRequest(
            String customerName,
            String customerEmail,
            String shippingAddress,
            String city,
            String postalCode,
            String country,
            List<CheckoutItemRequest> items
    ) {
        this(
                customerName,
                customerEmail,
                null,
                shippingAddress,
                city,
                null,
                postalCode,
                country,
                "STANDARD",
                "Demo Card Approved",
                null,
                true,
                false,
                items
        );
    }

    public CheckoutRequest(
            String customerName,
            String customerEmail,
            String shippingAddress,
            String city,
            String postalCode,
            String country,
            String shippingMethod,
            String paymentMethod,
            String idempotencyKey,
            Boolean simulatePaymentFailure,
            List<CheckoutItemRequest> items
    ) {
        this(
                customerName,
                customerEmail,
                null,
                shippingAddress,
                city,
                null,
                postalCode,
                country,
                shippingMethod,
                paymentMethod,
                idempotencyKey,
                true,
                simulatePaymentFailure,
                items
        );
    }
}
