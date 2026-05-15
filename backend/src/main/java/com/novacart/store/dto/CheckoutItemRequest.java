package com.novacart.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CheckoutItemRequest(
        @NotNull(message = "Product is required.")
        @Positive(message = "Product ID must be positive.")
        Long productId,

        @Min(value = 1, message = "Quantity must be at least 1.")
        int quantity
) {
}
