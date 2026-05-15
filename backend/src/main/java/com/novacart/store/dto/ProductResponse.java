package com.novacart.store.dto;

import com.novacart.store.entity.ProductStatus;
import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String slug,
        String sku,
        String brand,
        String description,
        BigDecimal price,
        BigDecimal compareAtPrice,
        int stockQuantity,
        int lowStockThreshold,
        String imageUrl,
        List<String> imageGallery,
        List<String> tags,
        boolean featured,
        ProductStatus status,
        boolean active,
        CategoryResponse category
) {
}
