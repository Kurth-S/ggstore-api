package com.ggstore.api.dto;

import jakarta.validation.constraints.*;

public record ResenaRequest(
        @NotNull(message = "El juego es obligatorio")
        java.util.UUID juegoId,

        @Min(1) @Max(5)
        int puntuacion,

        String comentario
) {}