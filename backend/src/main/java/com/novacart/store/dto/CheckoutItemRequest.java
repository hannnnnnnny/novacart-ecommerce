package com.novacart.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CheckoutItemRequest(
        @NotNull(message = "Product is required.")
        @Positive(message = "Product ID must be positive.")
        Long productId,

        @Size(max = 40, message = "Selected size must be 40 characters or fewer.")
        String selectedSize,

        @Size(max = 60, message = "Selected color must be 60 characters or fewer.")
        String selectedColor,

        @Min(value = 1, message = "Quantity must be at least 1.")
        int quantity
) {
        public CheckoutItemRequest(Long productId, int quantity) {
                this(productId, null, null, quantity);
        }
}
