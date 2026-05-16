package com.novacart.store.repository;

import com.novacart.store.entity.CustomerProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    Optional<CustomerProfile> findByEmailIgnoreCase(String email);

    List<CustomerProfile> findAllByOrderByLastOrderAtDescCreatedAtDesc();

    long countByLastOrderAtNotNull();
}
