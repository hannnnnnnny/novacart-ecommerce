package com.novacart.store.dto;

import com.novacart.store.entity.ProductStatus;

public record InventoryWarningResponse(
        Long productId,
        String productName,
        String categoryName,
        int stockQuantity,
        int lowStockThreshold,
        ProductStatus status,
        boolean active
) {
}
