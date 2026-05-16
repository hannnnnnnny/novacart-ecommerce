package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.PromotionRequest;
import com.novacart.store.dto.PromotionResponse;
import com.novacart.store.service.PromotionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/promotions")
@Validated
public class AdminPromotionController {

    private final PromotionService promotionService;

    public AdminPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> findPromotions() {
        return ApiResponse.success("Promotions loaded successfully.", promotionService.findPromotions());
    }

    @PostMapping
    public ApiResponse<PromotionResponse> createPromotion(@Valid @RequestBody PromotionRequest request) {
        return ApiResponse.success("Promotion created successfully.", promotionService.createPromotion(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<PromotionResponse> updatePromotion(
            @Positive(message = "Promotion ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequest request
    ) {
        return ApiResponse.success("Promotion updated successfully.", promotionService.updatePromotion(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePromotion(
            @Positive(message = "Promotion ID must be positive.")
            @PathVariable Long id
    ) {
        promotionService.deletePromotion(id);
        return ApiResponse.success("Promotion deleted successfully.");
    }
}
