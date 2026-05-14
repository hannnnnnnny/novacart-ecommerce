package com.novacart.store.service.impl;

import com.novacart.store.dto.CategoryResponse;
import com.novacart.store.dto.ProductRequest;
import com.novacart.store.dto.ProductResponse;
import com.novacart.store.entity.Category;
import com.novacart.store.entity.Product;
import com.novacart.store.exception.DuplicateResourceException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.service.ProductService;
import com.novacart.store.service.SlugService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SlugService slugService;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SlugService slugService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.slugService = slugService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findPublicProducts(Long categoryId) {
        List<Product> products = categoryId == null
                ? productRepository.findAllByActiveTrueOrderByNameAsc()
                : productRepository.findAllByActiveTrueAndCategoryIdOrderByNameAsc(categoryId);

        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findPublicProduct(Long id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));
        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAdminProducts() {
        return productRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findAdminProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));
        return toResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        String slug = slugService.createSlug(request.slug(), request.name());
        if (productRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("A product with this slug already exists.");
        }

        Category category = findCategory(request.categoryId());
        Product product = new Product(
                request.name().trim(),
                slug,
                request.description().trim(),
                request.price(),
                request.stockQuantity(),
                request.imageUrl().trim(),
                request.active() == null || request.active(),
                category
        );
        return toResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));

        String slug = slugService.createSlug(request.slug(), request.name());
        if (productRepository.existsBySlugAndIdNot(slug, id)) {
            throw new DuplicateResourceException("A product with this slug already exists.");
        }

        product.setName(request.name().trim());
        product.setSlug(slug);
        product.setDescription(request.description().trim());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl().trim());
        product.setActive(request.active() == null || request.active());
        product.setCategory(findCategory(request.categoryId()));
        return toResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));
        productRepository.delete(product);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category was not found."));
    }

    private ProductResponse toResponse(Product product) {
        Category category = product.getCategory();
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.isActive()
        );

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.isActive(),
                categoryResponse
        );
    }
}
