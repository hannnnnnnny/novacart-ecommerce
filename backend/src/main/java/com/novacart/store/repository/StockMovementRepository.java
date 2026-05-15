package com.novacart.store.repository;

import com.novacart.store.entity.StockMovement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findTop20ByOrderByCreatedAtDesc();
}
