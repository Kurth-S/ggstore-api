package com.ggstore.api.dto;

import java.math.BigDecimal;

public record VentasMensualesResponse(
        int anio,
        int mes,
        long totalPedidos,
        BigDecimal ingresos
) {}