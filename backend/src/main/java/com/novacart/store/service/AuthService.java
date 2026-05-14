package com.novacart.store.service;

import com.novacart.store.dto.LoginRequest;
import com.novacart.store.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
