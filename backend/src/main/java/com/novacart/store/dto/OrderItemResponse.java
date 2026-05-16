package com.novacart.store.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        String selectedSize,
        String selectedColor,
        BigDecimal unitPrice,
        BigDecimal originalUnitPrice,
        BigDecimal discountAmount,
        int quantity,
        BigDecimal lineTotal
) {
}
