package com.novacart.store.dto;

import com.novacart.store.entity.PaymentStatus;
import com.novacart.store.entity.OrderStatus;
import com.novacart.store.entity.ShippingMethod;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String customerName,
        String customerEmail,
        String shippingAddress,
        String city,
        String postalCode,
        String country,
        ShippingMethod shippingMethod,
        String paymentMethod,
        PaymentStatus paymentStatus,
        OrderStatus status,
        BigDecimal subtotalAmount,
        BigDecimal shippingAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {
}
