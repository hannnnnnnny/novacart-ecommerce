package com.novacart.store.service.impl;

import com.novacart.store.dto.LoginRequest;
import com.novacart.store.dto.LoginResponse;
import com.novacart.store.entity.AdminUser;
import com.novacart.store.exception.AuthenticationFailedException;
import com.novacart.store.repository.AdminUserRepository;
import com.novacart.store.security.JwtService;
import com.novacart.store.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        AdminUser adminUser = adminUserRepository.findByEmailIgnoreCase(request.email().trim())
                .filter(AdminUser::isActive)
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email address or password."));

        if (!passwordEncoder.matches(request.password(), adminUser.getPasswordHash())) {
            throw new AuthenticationFailedException("Invalid email address or password.");
        }

        String token = jwtService.generateToken(adminUser.getEmail(), adminUser.getRole());
        return new LoginResponse(token, "Bearer", jwtService.getExpirationMinutes(), adminUser.getEmail(), adminUser.getRole());
    }
}
