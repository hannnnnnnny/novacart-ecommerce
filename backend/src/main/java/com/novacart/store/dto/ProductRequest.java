package com.novacart.store.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.hibernate.validator.constraints.URL;

public record ProductRequest(
        @NotBlank(message = "Product name is required.")
        @Size(max = 180, message = "Product name must be 180 characters or fewer.")
        String name,

        @Size(max = 200, message = "Product slug must be 200 characters or fewer.")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Product slug must use lowercase letters, numbers, and hyphens."
        )
        String slug,

        @NotBlank(message = "Product description is required.")
        @Size(max = 2_000, message = "Product description must be 2,000 characters or fewer.")
        String description,

        @NotNull(message = "Product price is required.")
        @DecimalMin(value = "0.01", message = "Product price must be greater than zero.")
        @Digits(integer = 10, fraction = 2, message = "Product price must use no more than 10 digits and 2 decimals.")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required.")
        @Min(value = 0, message = "Stock quantity cannot be negative.")
        Integer stockQuantity,

        @NotBlank(message = "Product image URL is required.")
        @Size(max = 600, message = "Product image URL must be 600 characters or fewer.")
        @URL(message = "Product image URL must be a valid URL.")
        String imageUrl,

        Boolean active,

        @NotNull(message = "Product category is required.")
        Long categoryId
) {
}
