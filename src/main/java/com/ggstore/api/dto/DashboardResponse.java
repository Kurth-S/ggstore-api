package com.ggstore.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        long totalUsuarios,
        long totalPedidos,
        BigDecimal ingresosTotales,
        List<VentasMensualesResponse> ventasPorMes,
        List<JuegoMasVendidoResponse> juegosMasVendidos
) {}
