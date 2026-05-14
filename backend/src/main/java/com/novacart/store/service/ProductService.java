package com.novacart.store.service;

import com.novacart.store.dto.ProductRequest;
import com.novacart.store.dto.ProductResponse;
import java.util.List;

public interface ProductService {

    List<ProductResponse> findPublicProducts(Long categoryId);

    ProductResponse findPublicProduct(Long id);

    List<ProductResponse> findAdminProducts();

    ProductResponse findAdminProduct(Long id);

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
