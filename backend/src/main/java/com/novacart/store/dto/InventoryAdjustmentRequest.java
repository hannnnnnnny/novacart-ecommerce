package com.novacart.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record InventoryAdjustmentRequest(
        @NotNull(message = "Product is required.")
        @Positive(message = "Product ID must be positive.")
        Long productId,

        @NotNull(message = "Quantity change is required.")
        @Min(value = -10000, message = "Quantity change cannot be lower than -10,000.")
        @Max(value = 10000, message = "Quantity change cannot be greater than 10,000.")
        Integer quantityChange,

        @NotBlank(message = "Adjustment reason is required.")
        @Size(max = 500, message = "Adjustment reason must be 500 characters or fewer.")
        String reason
) {
}
