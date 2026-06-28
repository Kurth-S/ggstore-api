package com.ggstore.api.dto;

import com.ggstore.api.enums.PlataformaJuego;
import com.ggstore.api.models.Juego;
import java.math.BigDecimal;
import java.util.UUID;

public record JuegoResponse(
        UUID id,
        String titulo,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagenUrl,
        PlataformaJuego plataforma,
        UUID categoriaId,
        String categoriaNombre,
        BigDecimal descuentoPorcentaje,
        BigDecimal precioFinal
) {
    public static JuegoResponse from(Juego juego) {
        BigDecimal descuento = juego.getDescuentoPorcentaje() != null
                ? juego.getDescuentoPorcentaje()
                : BigDecimal.ZERO;

        BigDecimal precioFinal = juego.getPrecio()
                .multiply(BigDecimal.ONE.subtract(descuento.divide(BigDecimal.valueOf(100))));

        return new JuegoResponse(
                juego.getId(),
                juego.getTitulo(),
                juego.getDescripcion(),
                juego.getPrecio(),
                juego.getStock(),
                juego.getImagenUrl(),
                juego.getPlataforma(),
                juego.getCategoria() != null ? juego.getCategoria().getId() : null,
                juego.getCategoria() != null ? juego.getCategoria().getNombre() : null,
                descuento,
                precioFinal
        );
    }
}