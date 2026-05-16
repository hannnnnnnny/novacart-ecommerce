package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.CollectionRequest;
import com.novacart.store.dto.CollectionResponse;
import com.novacart.store.service.CollectionService;
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
@RequestMapping("/api/admin/collections")
@Validated
public class AdminCollectionController {

    private final CollectionService collectionService;

    public AdminCollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public ApiResponse<List<CollectionResponse>> findCollections() {
        return ApiResponse.success("Collections loaded successfully.", collectionService.findAdminCollections());
    }

    @PostMapping
    public ApiResponse<CollectionResponse> createCollection(@Valid @RequestBody CollectionRequest request) {
        return ApiResponse.success("Collection created successfully.", collectionService.createCollection(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<CollectionResponse> updateCollection(
            @Positive(message = "Collection ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody CollectionRequest request
    ) {
        return ApiResponse.success("Collection updated successfully.", collectionService.updateCollection(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCollection(
            @Positive(message = "Collection ID must be positive.")
            @PathVariable Long id
    ) {
        collectionService.deleteCollection(id);
        return ApiResponse.success("Collection deleted successfully.");
    }
}
