package com.ggstore.api.dto;

public record AuthResponse(
        String token,
        String nombre,
        String email,
        String rol
) {}
