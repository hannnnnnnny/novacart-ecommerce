package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.ProductRequest;
import com.novacart.store.dto.ProductResponse;
import com.novacart.store.service.ProductService;
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
@RequestMapping("/api/admin/products")
@Validated
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> findProducts() {
        return ApiResponse.success("Products loaded successfully.", productService.findAdminProducts());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> findProduct(
            @Positive(message = "Product ID must be positive.")
            @PathVariable Long id
    ) {
        return ApiResponse.success("Product loaded successfully.", productService.findAdminProduct(id));
    }

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.success("Product created successfully.", productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(
            @Positive(message = "Product ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ApiResponse.success("Product updated successfully.", productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(
            @Positive(message = "Product ID must be positive.")
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ApiResponse.success("Product deleted successfully.");
    }
}
