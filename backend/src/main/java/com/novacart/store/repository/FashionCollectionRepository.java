package com.novacart.store.repository;

import com.novacart.store.entity.CollectionStatus;
import com.novacart.store.entity.FashionCollection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FashionCollectionRepository extends JpaRepository<FashionCollection, Long> {

    List<FashionCollection> findAllByStatusOrderBySortOrderAscNameAsc(CollectionStatus status);

    List<FashionCollection> findAllByFeaturedTrueAndStatusOrderBySortOrderAscNameAsc(CollectionStatus status);

    Optional<FashionCollection> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
