package com.novacart.store.service.impl;

import com.novacart.store.dto.CategoryRequest;
import com.novacart.store.dto.CategoryResponse;
import com.novacart.store.entity.Category;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.DuplicateResourceException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.service.CategoryService;
import com.novacart.store.service.SlugService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SlugService slugService;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            SlugService slugService
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.slugService = slugService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findPublicCategories() {
        return categoryRepository.findAllByActiveTrueOrderBySortOrderAscNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAdminCategories() {
        return categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Category::getSortOrder).thenComparing(Category::getName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        String slug = slugService.createSlug(request.slug(), request.name());
        if (categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("A category with this slug already exists.");
        }

        Category category = new Category(
                request.name().trim(),
                slug,
                clean(request.description()),
                clean(request.imageUrl()),
                request.sortOrder() == null ? 0 : request.sortOrder(),
                request.active() == null || request.active()
        );
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        String slug = slugService.createSlug(request.slug(), request.name());
        if (categoryRepository.existsBySlugAndIdNot(slug, id)) {
            throw new DuplicateResourceException("A category with this slug already exists.");
        }

        category.setName(request.name().trim());
        category.setSlug(slug);
        category.setDescription(clean(request.description()));
        category.setImageUrl(clean(request.imageUrl()));
        category.setSortOrder(request.sortOrder() == null ? category.getSortOrder() : request.sortOrder());
        category.setActive(request.active() == null || request.active());
        return toResponse(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = findCategory(id);
        if (productRepository.existsByCategoryId(id)) {
            throw new BusinessRuleException("Category cannot be deleted while products are assigned.");
        }
        categoryRepository.delete(category);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category was not found."));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getImageUrl(),
                category.getSortOrder(),
                category.isActive()
        );
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
