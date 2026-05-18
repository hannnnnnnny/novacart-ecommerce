package com.novacart.store.dto;

public record StoreResponse(
        Long id,
        String merchantName,
        String name,
        String slug,
        String category,
        String description,
        String templateKey,
        String brandColor,
        String logoText,
        String currency,
        String shippingMessage,
        String announcement,
        boolean published
) {
}
