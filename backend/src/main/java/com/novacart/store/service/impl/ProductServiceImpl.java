package com.novacart.store.service.impl;

import com.novacart.store.dto.CategoryResponse;
import com.novacart.store.dto.ProductRequest;
import com.novacart.store.dto.ProductResponse;
import com.novacart.store.entity.Category;
import com.novacart.store.entity.Product;
import com.novacart.store.entity.ProductStatus;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.DuplicateResourceException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.service.ProductService;
import com.novacart.store.service.SlugService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
                ? productRepository.findAllByActiveTrueAndStatusOrderByNameAsc(ProductStatus.ACTIVE)
                : productRepository.findAllByActiveTrueAndStatusAndCategoryIdOrderByNameAsc(ProductStatus.ACTIVE, categoryId);

        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findPublicProduct(Long id) {
        Product product = productRepository.findByIdAndActiveTrueAndStatus(id, ProductStatus.ACTIVE)
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

        String sku = normalizeSku(request.sku(), slug);
        if (productRepository.existsBySku(sku)) {
            throw new DuplicateResourceException("A product with this SKU already exists.");
        }
        validateCompareAtPrice(request.price(), request.compareAtPrice());

        Category category = findCategory(request.categoryId());
        ProductStatus status = request.status() == null ? ProductStatus.ACTIVE : request.status();
        boolean active = request.active() == null ? status == ProductStatus.ACTIVE : request.active();
        Product product = new Product(
                request.name().trim(),
                slug,
                sku,
                normalizeText(request.brand(), "NovaCart Supply"),
                request.description().trim(),
                request.price(),
                request.compareAtPrice(),
                request.stockQuantity(),
                request.lowStockThreshold() == null ? 5 : request.lowStockThreshold(),
                request.imageUrl().trim(),
                normalizeImageGallery(request.imageUrl(), request.imageGallery()),
                normalizeList(request.tags()),
                Boolean.TRUE.equals(request.featured()),
                status,
                active,
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

        String sku = request.sku() == null || request.sku().isBlank()
                ? (product.getSku() == null ? normalizeSku(null, slug) : product.getSku())
                : normalizeSku(request.sku(), slug);
        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new DuplicateResourceException("A product with this SKU already exists.");
        }
        validateCompareAtPrice(request.price(), request.compareAtPrice());
        ProductStatus status = request.status() == null ? product.getStatus() : request.status();

        product.setName(request.name().trim());
        product.setSlug(slug);
        product.setSku(sku);
        product.setBrand(normalizeText(request.brand(), "NovaCart Supply"));
        product.setDescription(request.description().trim());
        product.setPrice(request.price());
        product.setCompareAtPrice(request.compareAtPrice());
        product.setStockQuantity(request.stockQuantity());
        product.setLowStockThreshold(request.lowStockThreshold() == null ? product.getLowStockThreshold() : request.lowStockThreshold());
        product.setImageUrl(request.imageUrl().trim());
        product.setImageGallery(normalizeImageGallery(request.imageUrl(), request.imageGallery()));
        product.setTags(normalizeList(request.tags()));
        product.setFeatured(Boolean.TRUE.equals(request.featured()));
        product.setStatus(status);
        product.setActive(request.active() == null ? status == ProductStatus.ACTIVE : request.active());
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

    private String normalizeSku(String sku, String fallbackSource) {
        if (sku != null && !sku.isBlank()) {
            return sku.trim().toUpperCase(Locale.ROOT);
        }
        return "NC-" + fallbackSource.trim().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private String normalizeText(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    private List<String> normalizeImageGallery(String primaryImageUrl, List<String> imageGallery) {
        List<String> images = normalizeList(imageGallery);
        String primaryImage = primaryImageUrl.trim();
        if (images.isEmpty()) {
            return List.of(primaryImage);
        }
        if (!images.contains(primaryImage)) {
            return java.util.stream.Stream.concat(java.util.stream.Stream.of(primaryImage), images.stream()).toList();
        }
        return images;
    }

    private List<String> normalizeList(List<String> values) {
        if (values == null) {
            return List.of();
        }
        return values.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
    }

    private void validateCompareAtPrice(BigDecimal price, BigDecimal compareAtPrice) {
        if (compareAtPrice != null && compareAtPrice.compareTo(price) <= 0) {
            throw new BusinessRuleException("Compare-at price must be greater than the product price.");
        }
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
                product.getSku(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice(),
                product.getCompareAtPrice(),
                product.getStockQuantity(),
                product.getLowStockThreshold(),
                product.getImageUrl(),
                List.copyOf(product.getImageGallery()),
                List.copyOf(product.getTags()),
                product.isFeatured(),
                product.getStatus(),
                product.isActive(),
                categoryResponse
        );
    }
}
