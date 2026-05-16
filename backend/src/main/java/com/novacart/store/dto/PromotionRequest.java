package com.novacart.store.dto;

import com.novacart.store.entity.PromotionDiscountType;
import com.novacart.store.entity.PromotionTargetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PromotionRequest(
        @NotBlank(message = "Promotion name is required.")
        @Size(max = 140, message = "Promotion name must be 140 characters or fewer.")
        String name,

        @Size(max = 800, message = "Promotion description must be 800 characters or fewer.")
        String description,

        @NotNull(message = "Discount type is required.")
        PromotionDiscountType discountType,

        @NotNull(message = "Discount value is required.")
        @DecimalMin(value = "0.01", message = "Discount value must be greater than zero.")
        @Digits(integer = 10, fraction = 2, message = "Discount value must use no more than 10 digits and 2 decimals.")
        BigDecimal discountValue,

        LocalDate startDate,
        LocalDate endDate,
        Boolean active,

        @NotNull(message = "Promotion target type is required.")
        PromotionTargetType targetType,

        @Size(max = 200, message = "Promotion can include up to 200 target values.")
        List<@Size(max = 160, message = "Promotion target value must be 160 characters or fewer.") String> targetValues
) {
}
