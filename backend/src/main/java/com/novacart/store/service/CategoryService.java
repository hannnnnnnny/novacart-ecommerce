package com.novacart.store.service;

import com.novacart.store.dto.CategoryRequest;
import com.novacart.store.dto.CategoryResponse;
import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findPublicCategories();

    List<CategoryResponse> findAdminCategories();

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
