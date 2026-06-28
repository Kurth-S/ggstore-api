package com.ggstore.api.dto;

import com.ggstore.api.models.Resena;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ResenaResponse(
        UUID id,
        UUID juegoId,
        String tituloJuego,
        String nombreUsuario,
        int puntuacion,
        String comentario,
        OffsetDateTime createdAt
) {
    public static ResenaResponse from(Resena r) {
        return new ResenaResponse(
                r.getId(),
                r.getJuego().getId(),
                r.getJuego().getTitulo(),
                r.getUsuario().getNombre(),
                r.getPuntuacion(),
                r.getComentario(),
                r.getCreatedAt()
        );
    }
}