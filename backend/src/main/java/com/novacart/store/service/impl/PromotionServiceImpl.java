package com.novacart.store.service.impl;

import com.novacart.store.dto.PromotionRequest;
import com.novacart.store.dto.PromotionResponse;
import com.novacart.store.entity.Product;
import com.novacart.store.entity.Promotion;
import com.novacart.store.entity.PromotionDiscountType;
import com.novacart.store.entity.PromotionTargetType;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.repository.PromotionRepository;
import com.novacart.store.service.PromotionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> findPromotions() {
        return promotionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PromotionResponse createPromotion(PromotionRequest request) {
        validate(request);
        Promotion promotion = new Promotion(
                request.name().trim(),
                clean(request.description()),
                request.discountType(),
                request.discountValue(),
                request.startDate(),
                request.endDate(),
                request.active() == null || request.active(),
                request.targetType(),
                normalizeTargets(request.targetValues())
        );
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    public PromotionResponse updatePromotion(Long id, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion was not found."));
        validate(request);
        promotion.setName(request.name().trim());
        promotion.setDescription(clean(request.description()));
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setActive(request.active() == null || request.active());
        promotion.setTargetType(request.targetType());
        promotion.setTargetValues(normalizeTargets(request.targetValues()));
        return toResponse(promotion);
    }

    @Override
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion was not found.");
        }
        promotionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscountQuote quote(Product product) {
        BigDecimal bestPrice = product.getPrice();
        for (Promotion promotion : promotionRepository.findActiveOn(LocalDate.now())) {
            if (!matches(promotion, product)) {
                continue;
            }
            BigDecimal candidatePrice = applyDiscount(product.getPrice(), promotion);
            if (candidatePrice.compareTo(bestPrice) < 0) {
                bestPrice = candidatePrice;
            }
        }

        BigDecimal discountAmount = product.getPrice().subtract(bestPrice).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        Integer discountPercent = null;
        if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            discountPercent = discountAmount.multiply(ONE_HUNDRED)
                    .divide(product.getPrice(), 0, RoundingMode.HALF_UP)
                    .intValue();
        } else if (product.getCompareAtPrice() != null && product.getCompareAtPrice().compareTo(product.getPrice()) > 0) {
            discountPercent = product.getCompareAtPrice().subtract(product.getPrice())
                    .multiply(ONE_HUNDRED)
                    .divide(product.getCompareAtPrice(), 0, RoundingMode.HALF_UP)
                    .intValue();
        }
        return new DiscountQuote(bestPrice.setScale(2, RoundingMode.HALF_UP), discountAmount, discountPercent);
    }

    private void validate(PromotionRequest request) {
        if (request.endDate() != null && request.startDate() != null && request.endDate().isBefore(request.startDate())) {
            throw new BusinessRuleException("Promotion end date cannot be before the start date.");
        }
        if (request.discountType() == PromotionDiscountType.PERCENTAGE && request.discountValue().compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessRuleException("Percentage discount cannot be greater than 100.");
        }
        if (request.discountType() == PromotionDiscountType.FIXED_AMOUNT) {
            List<Product> targets = productRepository.findAll()
                    .stream()
                    .filter(product -> matches(request.targetType(), normalizeTargets(request.targetValues()), product))
                    .toList();
            if (targets.stream().anyMatch(product -> request.discountValue().compareTo(product.getPrice()) >= 0)) {
                throw new BusinessRuleException("Fixed discounts must be lower than every targeted product price.");
            }
        }
    }

    private BigDecimal applyDiscount(BigDecimal price, Promotion promotion) {
        BigDecimal discount = promotion.getDiscountType() == PromotionDiscountType.PERCENTAGE
                ? price.multiply(promotion.getDiscountValue()).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP)
                : promotion.getDiscountValue();
        BigDecimal effectivePrice = price.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        if (effectivePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Promotion discount cannot create a negative product price.");
        }
        return effectivePrice;
    }

    private boolean matches(Promotion promotion, Product product) {
        return matches(promotion.getTargetType(), promotion.getTargetValues(), product);
    }

    private boolean matches(PromotionTargetType targetType, List<String> targetValues, Product product) {
        List<String> targets = targetValues.stream().map(this::normalize).toList();
        return switch (targetType) {
            case SELECTED_PRODUCTS -> targets.contains(String.valueOf(product.getId()));
            case CATEGORY -> targets.contains(String.valueOf(product.getCategory().getId()))
                    || targets.contains(normalize(product.getCategory().getSlug()))
                    || targets.contains(normalize(product.getCategory().getName()));
            case COLLECTION -> product.getCollection() != null && (
                    targets.contains(String.valueOf(product.getCollection().getId()))
                            || targets.contains(normalize(product.getCollection().getSlug()))
                            || targets.contains(normalize(product.getCollection().getName()))
            );
            case SEASON -> targets.contains(normalize(product.getSeason()));
            case TAGS -> product.getTags().stream().map(this::normalize).anyMatch(targets::contains);
        };
    }

    private List<String> normalizeTargets(List<String> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private PromotionResponse toResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getDiscountType(),
                promotion.getDiscountValue(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.isActive(),
                promotion.getTargetType(),
                List.copyOf(promotion.getTargetValues()),
                promotion.getCreatedAt(),
                promotion.getUpdatedAt()
        );
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
