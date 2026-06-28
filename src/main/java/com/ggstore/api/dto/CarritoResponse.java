package com.ggstore.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CarritoResponse(
        UUID carritoId,
        List<CarritoDetalleResponse> items,
        BigDecimal total
) {}