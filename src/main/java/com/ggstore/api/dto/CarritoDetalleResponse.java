package com.ggstore.api.dto;

import com.ggstore.api.models.CarritoDetalle;
import java.math.BigDecimal;
import java.util.UUID;

public record CarritoDetalleResponse(
        UUID id,
        UUID juegoId,
        String tituloJuego,
        String imagenUrl,
        BigDecimal precioUnitario,
        BigDecimal precioFinal,
        int cantidad,
        BigDecimal subtotal
) {
    public static CarritoDetalleResponse from(CarritoDetalle detalle) {
        BigDecimal descuento = detalle.getJuego().getDescuentoPorcentaje() != null
                ? detalle.getJuego().getDescuentoPorcentaje()
                : BigDecimal.ZERO;

        BigDecimal precioFinal = detalle.getJuego().getPrecio()
                .multiply(BigDecimal.ONE.subtract(descuento.divide(BigDecimal.valueOf(100))));

        BigDecimal subtotal = precioFinal.multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return new CarritoDetalleResponse(
                detalle.getId(),
                detalle.getJuego().getId(),
                detalle.getJuego().getTitulo(),
                detalle.getJuego().getImagenUrl(),
                detalle.getJuego().getPrecio(),
                precioFinal,
                detalle.getCantidad(),
                subtotal
        );
    }
}
