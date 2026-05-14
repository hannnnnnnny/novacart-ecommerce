package com.novacart.store.security;

import com.novacart.store.entity.AdminUser;
import com.novacart.store.repository.AdminUserRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDetailsService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AdminUser adminUser = adminUserRepository.findByEmailIgnoreCase(username)
                .filter(AdminUser::isActive)
                .orElseThrow(() -> new UsernameNotFoundException("Admin account was not found."));

        return new User(
                adminUser.getEmail(),
                adminUser.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + adminUser.getRole()))
        );
    }
}
