package com.novacart.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email address is required.")
        @Email(message = "Enter a valid email address.")
        @Size(max = 180, message = "Email address must be 180 characters or fewer.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(max = 120, message = "Password must be 120 characters or fewer.")
        String password
) {
}
