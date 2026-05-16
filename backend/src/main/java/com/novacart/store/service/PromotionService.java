package com.novacart.store.service;

import com.novacart.store.dto.PromotionRequest;
import com.novacart.store.dto.PromotionResponse;
import com.novacart.store.entity.Product;
import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {

    List<PromotionResponse> findPromotions();

    PromotionResponse createPromotion(PromotionRequest request);

    PromotionResponse updatePromotion(Long id, PromotionRequest request);

    void deletePromotion(Long id);

    DiscountQuote quote(Product product);

    record DiscountQuote(BigDecimal effectivePrice, BigDecimal discountAmount, Integer discountPercent) {
    }
}
