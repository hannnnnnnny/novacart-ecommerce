package com.novacart.store.repository;

import com.novacart.store.entity.MerchantAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantAccountRepository extends JpaRepository<MerchantAccount, Long> {

    Optional<MerchantAccount> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
