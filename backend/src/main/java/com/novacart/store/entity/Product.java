package com.novacart.store.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(unique = true, length = 80)
    private String sku;

    @Column(length = 120)
    private String brand;

    @Column(nullable = false, length = 2_000)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 12, scale = 2)
    private BigDecimal compareAtPrice;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int lowStockThreshold = 5;

    @Column(nullable = false, length = 600)
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", length = 600)
    private List<String> imageGallery = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag", length = 80)
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size_value", length = 40)
    private List<String> sizes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "color_value", length = 60)
    private List<String> colors = new ArrayList<>();

    @Column(length = 120)
    private String material;

    @Column(length = 800)
    private String careInstructions;

    @Column(length = 80)
    private String season;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private GenderTarget genderTarget = GenderTarget.UNISEX;

    @Column(nullable = false)
    private boolean featured;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private FashionCollection collection;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    protected Product() {
    }

    public Product(
            String name,
            String slug,
            String description,
            BigDecimal price,
            int stockQuantity,
            String imageUrl,
            boolean active,
            Category category
    ) {
        this(
                name,
                slug,
                createFallbackSku(slug),
                "Nova Atelier",
                description,
                price,
                null,
                stockQuantity,
                5,
                imageUrl,
                List.of(imageUrl),
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                GenderTarget.UNISEX,
                false,
                active ? ProductStatus.ACTIVE : ProductStatus.DRAFT,
                active,
                category,
                null
        );
    }

    public Product(
            String name,
            String slug,
            String sku,
            String brand,
            String description,
            BigDecimal price,
            BigDecimal compareAtPrice,
            int stockQuantity,
            int lowStockThreshold,
            String imageUrl,
            List<String> imageGallery,
            List<String> tags,
            List<String> sizes,
            List<String> colors,
            String material,
            String careInstructions,
            String season,
            GenderTarget genderTarget,
            boolean featured,
            ProductStatus status,
            boolean active,
            Category category,
            FashionCollection collection
    ) {
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.compareAtPrice = compareAtPrice;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.imageUrl = imageUrl;
        setImageGallery(imageGallery);
        setTags(tags);
        setSizes(sizes);
        setColors(colors);
        this.material = material;
        this.careInstructions = careInstructions;
        this.season = season;
        this.genderTarget = genderTarget == null ? GenderTarget.UNISEX : genderTarget;
        this.featured = featured;
        this.status = status == null ? ProductStatus.ACTIVE : status;
        this.active = active;
        this.category = category;
        this.collection = collection;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(BigDecimal compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageGallery() {
        return imageGallery;
    }

    public void setImageGallery(List<String> imageGallery) {
        this.imageGallery = imageGallery == null ? new ArrayList<>() : new ArrayList<>(imageGallery);
        if (this.imageGallery.isEmpty() && imageUrl != null) {
            this.imageGallery.add(imageUrl);
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes == null ? new ArrayList<>() : new ArrayList<>(sizes);
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors == null ? new ArrayList<>() : new ArrayList<>(colors);
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public GenderTarget getGenderTarget() {
        return genderTarget;
    }

    public void setGenderTarget(GenderTarget genderTarget) {
        this.genderTarget = genderTarget;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FashionCollection getCollection() {
        return collection;
    }

    public void setCollection(FashionCollection collection) {
        this.collection = collection;
    }

    public boolean isStorefrontVisible() {
        return active && status == ProductStatus.ACTIVE;
    }

    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private static String createFallbackSku(String slug) {
        if (slug == null || slug.isBlank()) {
            return null;
        }
        return "NC-" + slug.toUpperCase();
    }
}
