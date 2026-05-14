package com.novacart.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        String email,

        @NotBlank(message = "Password is required.")
        String password
) {
}
