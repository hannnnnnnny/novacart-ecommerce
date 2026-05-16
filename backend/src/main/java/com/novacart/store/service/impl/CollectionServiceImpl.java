package com.novacart.store.service.impl;

import com.novacart.store.dto.CollectionRequest;
import com.novacart.store.dto.CollectionResponse;
import com.novacart.store.entity.CollectionStatus;
import com.novacart.store.entity.FashionCollection;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.DuplicateResourceException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.FashionCollectionRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.service.CollectionService;
import com.novacart.store.service.SlugService;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final FashionCollectionRepository collectionRepository;
    private final ProductRepository productRepository;
    private final SlugService slugService;

    public CollectionServiceImpl(
            FashionCollectionRepository collectionRepository,
            ProductRepository productRepository,
            SlugService slugService
    ) {
        this.collectionRepository = collectionRepository;
        this.productRepository = productRepository;
        this.slugService = slugService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> findPublicCollections() {
        return collectionRepository.findAllByStatusOrderBySortOrderAscNameAsc(CollectionStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> findFeaturedCollections() {
        return collectionRepository.findAllByFeaturedTrueAndStatusOrderBySortOrderAscNameAsc(CollectionStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> findAdminCollections() {
        return collectionRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(FashionCollection::getSortOrder)
                        .thenComparing(FashionCollection::getName, String.CASE_INSENSITIVE_ORDER))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CollectionResponse createCollection(CollectionRequest request) {
        validateDateRange(request);
        String slug = slugService.createSlug(request.slug(), request.name());
        if (collectionRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("A collection with this slug already exists.");
        }
        FashionCollection collection = new FashionCollection(
                request.name().trim(),
                slug,
                clean(request.description()),
                clean(request.heroImageUrl()),
                clean(request.displayImageUrl()),
                request.status() == null ? CollectionStatus.ACTIVE : request.status(),
                Boolean.TRUE.equals(request.featured()),
                request.startDate(),
                request.endDate(),
                request.sortOrder() == null ? 0 : request.sortOrder()
        );
        return toResponse(collectionRepository.save(collection));
    }

    @Override
    public CollectionResponse updateCollection(Long id, CollectionRequest request) {
        validateDateRange(request);
        FashionCollection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection was not found."));
        String slug = slugService.createSlug(request.slug(), request.name());
        if (collectionRepository.existsBySlugAndIdNot(slug, id)) {
            throw new DuplicateResourceException("A collection with this slug already exists.");
        }
        collection.setName(request.name().trim());
        collection.setSlug(slug);
        collection.setDescription(clean(request.description()));
        collection.setHeroImageUrl(clean(request.heroImageUrl()));
        collection.setDisplayImageUrl(clean(request.displayImageUrl()));
        collection.setStatus(request.status() == null ? collection.getStatus() : request.status());
        collection.setFeatured(Boolean.TRUE.equals(request.featured()));
        collection.setStartDate(request.startDate());
        collection.setEndDate(request.endDate());
        collection.setSortOrder(request.sortOrder() == null ? collection.getSortOrder() : request.sortOrder());
        return toResponse(collection);
    }

    @Override
    public void deleteCollection(Long id) {
        if (productRepository.existsByCollectionId(id)) {
            throw new BusinessRuleException("Collection cannot be deleted while products are assigned.");
        }
        collectionRepository.deleteById(id);
    }

    private void validateDateRange(CollectionRequest request) {
        if (request.startDate() != null && request.endDate() != null && request.endDate().isBefore(request.startDate())) {
            throw new BusinessRuleException("Collection end date cannot be before the start date.");
        }
    }

    private CollectionResponse toResponse(FashionCollection collection) {
        return new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getSlug(),
                collection.getDescription(),
                collection.getHeroImageUrl(),
                collection.getDisplayImageUrl(),
                collection.getStatus(),
                collection.isFeatured(),
                collection.getStartDate(),
                collection.getEndDate(),
                collection.getSortOrder(),
                productRepository.countByCollectionId(collection.getId()),
                collection.getCreatedAt(),
                collection.getUpdatedAt()
        );
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
