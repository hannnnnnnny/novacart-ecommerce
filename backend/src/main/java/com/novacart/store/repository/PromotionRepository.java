package com.novacart.store.repository;

import com.novacart.store.entity.Promotion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
            select p from Promotion p
            where p.active = true
              and (p.startDate is null or p.startDate <= :today)
              and (p.endDate is null or p.endDate >= :today)
            order by p.discountValue desc
            """)
    List<Promotion> findActiveOn(@Param("today") LocalDate today);
}
