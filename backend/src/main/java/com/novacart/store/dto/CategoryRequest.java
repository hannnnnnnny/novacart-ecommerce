package com.novacart.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name is required.")
        @Size(max = 120, message = "Category name must be 120 characters or fewer.")
        String name,

        @Size(max = 140, message = "Category slug must be 140 characters or fewer.")
        String slug,

        @Size(max = 500, message = "Category description must be 500 characters or fewer.")
        String description,

        Boolean active
) {
}
