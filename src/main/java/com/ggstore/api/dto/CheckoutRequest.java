package com.ggstore.api.dto;

public record CheckoutRequest(
        String codigoCupon  // opcional, puede ser null
) {}