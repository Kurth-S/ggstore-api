package com.ggstore.api.dto;

import java.util.UUID;

public record JuegoMasVendidoResponse(
        UUID juegoId,
        String titulo,
        long cantidadVendida
) {}
