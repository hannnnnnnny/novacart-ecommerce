package com.novacart.store.security;

public record JwtTokenValidation(
        JwtTokenStatus status,
        String subject
) {

    public static JwtTokenValidation valid(String subject) {
        return new JwtTokenValidation(JwtTokenStatus.VALID, subject);
    }

    public static JwtTokenValidation expired() {
        return new JwtTokenValidation(JwtTokenStatus.EXPIRED, null);
    }

    public static JwtTokenValidation invalid() {
        return new JwtTokenValidation(JwtTokenStatus.INVALID, null);
    }
}
