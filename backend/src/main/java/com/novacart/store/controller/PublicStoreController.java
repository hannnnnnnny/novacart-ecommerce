package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.StoreResponse;
import com.novacart.store.entity.MerchantStore;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.MerchantStoreRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/stores")
public class PublicStoreController {

    private final MerchantStoreRepository merchantStoreRepository;

    public PublicStoreController(MerchantStoreRepository merchantStoreRepository) {
        this.merchantStoreRepository = merchantStoreRepository;
    }

    @GetMapping
    public ApiResponse<List<StoreResponse>> findStores() {
        List<StoreResponse> stores = merchantStoreRepository.findAllByOrderByNameAsc().stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success("Merchant stores loaded successfully.", stores);
    }

    @GetMapping("/{slug}")
    public ApiResponse<StoreResponse> findStore(@PathVariable String slug) {
        MerchantStore store = merchantStoreRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant store was not found."));
        return ApiResponse.success("Merchant store loaded successfully.", toResponse(store));
    }

    private StoreResponse toResponse(MerchantStore store) {
        return new StoreResponse(
                store.getId(),
                store.getMerchantAccount().getName(),
                store.getName(),
                store.getSlug(),
                store.getCategory(),
                store.getDescription(),
                store.getTemplateKey(),
                store.getBrandColor(),
                store.getLogoText(),
                store.getCurrency(),
                store.getShippingMessage(),
                store.getAnnouncement(),
                store.isPublished()
        );
    }
}
