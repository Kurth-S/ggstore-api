package com.ggstore.api.service;

import com.ggstore.api.dto.CategoriaRequest;
import com.ggstore.api.dto.CategoriaResponse;
import com.ggstore.api.models.Categoria;
import com.ggstore.api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(CategoriaResponse::from)
                .toList();
    }

    public CategoriaResponse obtenerPorId(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return CategoriaResponse.from(categoria);
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.findByNombre(request.nombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        Categoria categoria = Categoria.builder()
                .nombre(request.nombre())
                .build();
        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    public CategoriaResponse actualizar(UUID id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setNombre(request.nombre());
        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    public void eliminar(UUID id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}