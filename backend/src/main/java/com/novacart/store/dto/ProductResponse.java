package com.novacart.store.dto;

import com.novacart.store.entity.ProductStatus;
import com.novacart.store.entity.GenderTarget;
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
        List<String> sizes,
        List<String> colors,
        String material,
        String careInstructions,
        String season,
        GenderTarget genderTarget,
        BigDecimal effectivePrice,
        BigDecimal discountAmount,
        Integer discountPercent,
        boolean featured,
        ProductStatus status,
        boolean active,
        CategoryResponse category,
        CollectionResponse collection
) {
}
