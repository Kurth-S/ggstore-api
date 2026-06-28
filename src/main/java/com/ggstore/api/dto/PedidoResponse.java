package com.ggstore.api.dto;

import com.ggstore.api.models.Pedido;
import com.ggstore.api.models.PedidoDetalle;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponse(
        UUID id,
        OffsetDateTime fecha,
        BigDecimal total,
        String estado,
        List<PedidoDetalleResponse> items
) {
    public static PedidoResponse from(Pedido pedido, List<PedidoDetalle> detalles) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getTotal(),
                pedido.getEstado().name(),
                detalles.stream().map(PedidoDetalleResponse::from).toList()
        );
    }
}
