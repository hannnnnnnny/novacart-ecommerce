package com.novacart.store.repository;

import com.novacart.store.entity.RefundRequest;
import com.novacart.store.entity.RefundStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {

    List<RefundRequest> findAllByOrderByCreatedAtDesc();

    List<RefundRequest> findAllByStatusOrderByCreatedAtDesc(RefundStatus status);

    boolean existsByOrderIdAndStatusIn(Long orderId, List<RefundStatus> statuses);
}
