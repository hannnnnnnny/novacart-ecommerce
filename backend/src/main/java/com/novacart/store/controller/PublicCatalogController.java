package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.CategoryResponse;
import com.novacart.store.dto.ProductResponse;
import com.novacart.store.service.CategoryService;
import com.novacart.store.service.ProductService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicCatalogController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public PublicCatalogController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> findCategories() {
        return ApiResponse.success("Categories loaded successfully.", categoryService.findPublicCategories());
    }

    @GetMapping("/products")
    public ApiResponse<List<ProductResponse>> findProducts(
            @Positive(message = "Category ID must be positive.")
            @RequestParam(required = false) Long categoryId
    ) {
        return ApiResponse.success("Products loaded successfully.", productService.findPublicProducts(categoryId));
    }

    @GetMapping("/products/{id}")
    public ApiResponse<ProductResponse> findProduct(
            @Positive(message = "Product ID must be positive.")
            @PathVariable Long id
    ) {
        return ApiResponse.success("Product loaded successfully.", productService.findPublicProduct(id));
    }
}
