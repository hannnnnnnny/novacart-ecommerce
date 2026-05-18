package com.novacart.store.repository;

import com.novacart.store.entity.MerchantStore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantStoreRepository extends JpaRepository<MerchantStore, Long> {

    @EntityGraph(attributePaths = "merchantAccount")
    List<MerchantStore> findAllByOrderByNameAsc();

    @EntityGraph(attributePaths = "merchantAccount")
    Optional<MerchantStore> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
