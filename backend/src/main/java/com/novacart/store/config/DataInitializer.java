package com.novacart.store.config;

import com.novacart.store.entity.AdminUser;
import com.novacart.store.repository.AdminUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    public static final String DEFAULT_ADMIN_EMAIL = "admin@novacart.local";
    public static final String DEFAULT_ADMIN_PASSWORD = "NovaCartAdmin123!";

    @Bean
    CommandLineRunner seedAdminUser(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!adminUserRepository.existsByEmailIgnoreCase(DEFAULT_ADMIN_EMAIL)) {
                AdminUser adminUser = new AdminUser(
                        DEFAULT_ADMIN_EMAIL,
                        passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD),
                        "ADMIN",
                        true
                );
                adminUserRepository.save(adminUser);
            }
        };
    }
}
