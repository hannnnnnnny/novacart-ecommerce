package com.novacart.store.dto;

import com.novacart.store.entity.CollectionStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CollectionRequest(
        @Size(max = 140, message = "Collection name must be 140 characters or fewer.")
        String name,

        @Size(max = 160, message = "Collection slug must be 160 characters or fewer.")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Collection slug must use lowercase letters, numbers, and hyphens."
        )
        String slug,

        @Size(max = 800, message = "Collection description must be 800 characters or fewer.")
        String description,

        @Size(max = 600, message = "Collection hero image URL must be 600 characters or fewer.")
        @Pattern(
                regexp = "^(https?://|/).+",
                message = "Collection hero image URL must be an absolute HTTP URL or a local public asset path."
        )
        String heroImageUrl,

        @Size(max = 600, message = "Collection display image URL must be 600 characters or fewer.")
        @Pattern(
                regexp = "^(https?://|/).+",
                message = "Collection display image URL must be an absolute HTTP URL or a local public asset path."
        )
        String displayImageUrl,

        CollectionStatus status,
        Boolean featured,
        LocalDate startDate,
        LocalDate endDate,
        Integer sortOrder
) {
}
