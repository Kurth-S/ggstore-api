package com.ggstore.api.dto;

import com.ggstore.api.enums.PlataformaJuego;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record JuegoRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150)
        String titulo,

        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
        BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo")
        Integer stock,

        String imagenUrl,

        @NotNull(message = "La plataforma es obligatoria")
        PlataformaJuego plataforma,

        UUID categoriaId,

        @DecimalMin(value = "0.0")
        @DecimalMax(value = "100.0")
        BigDecimal descuentoPorcentaje
) {}
