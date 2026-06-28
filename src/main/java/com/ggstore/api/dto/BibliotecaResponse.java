package com.ggstore.api.dto;

import com.ggstore.api.models.Biblioteca;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BibliotecaResponse(
        UUID id,
        UUID juegoId,
        String tituloJuego,
        String imagenUrl,
        String claveDigital,
        OffsetDateTime fechaAdquisicion
) {
    public static BibliotecaResponse from(Biblioteca b) {
        String clave = b.getPedidoDetalle() != null
                ? b.getPedidoDetalle().getClaveDigital()
                : null;
        return new BibliotecaResponse(
                b.getId(),
                b.getJuego().getId(),
                b.getJuego().getTitulo(),
                b.getJuego().getImagenUrl(),
                clave,
                b.getFechaAdquisicion()
        );
    }
}