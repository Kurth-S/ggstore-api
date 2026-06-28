package com.ggstore.api.dto;

import com.ggstore.api.models.Wishlist;
import java.math.BigDecimal;
import java.util.UUID;

public record WishlistResponse(
        UUID id,
        UUID juegoId,
        String tituloJuego,
        String imagenUrl,
        BigDecimal precio,
        BigDecimal descuentoPorcentaje,
        BigDecimal precioFinal
) {
    public static WishlistResponse from(Wishlist w) {
        BigDecimal descuento = w.getJuego().getDescuentoPorcentaje() != null
                ? w.getJuego().getDescuentoPorcentaje()
                : BigDecimal.ZERO;
        BigDecimal precioFinal = w.getJuego().getPrecio()
                .multiply(BigDecimal.ONE.subtract(descuento.divide(BigDecimal.valueOf(100))));
        return new WishlistResponse(
                w.getId(),
                w.getJuego().getId(),
                w.getJuego().getTitulo(),
                w.getJuego().getImagenUrl(),
                w.getJuego().getPrecio(),
                descuento,
                precioFinal
        );
    }
}
