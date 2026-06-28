package com.ggstore.api.dto;

import com.ggstore.api.models.Categoria;
import java.util.UUID;

public record CategoriaResponse(
        UUID id,
        String nombre
) {
    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre());
    }
}
