package com.ggstore.api.dto;

import com.ggstore.api.models.PedidoDetalle;
import java.math.BigDecimal;
import java.util.UUID;

public record PedidoDetalleResponse(
        UUID id,
        UUID juegoId,
        String tituloJuego,
        String imagenUrl,
        BigDecimal precio,
        int cantidad,
        String claveDigital
) {
    public static PedidoDetalleResponse from(PedidoDetalle d) {
        return new PedidoDetalleResponse(
                d.getId(),
                d.getJuego().getId(),
                d.getJuego().getTitulo(),
                d.getJuego().getImagenUrl(),
                d.getPrecio(),
                d.getCantidad(),
                d.getClaveDigital()
        );
    }
}