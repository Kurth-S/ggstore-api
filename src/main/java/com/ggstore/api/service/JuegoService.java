package com.ggstore.api.service;

import com.ggstore.api.dto.JuegoRequest;
import com.ggstore.api.dto.JuegoResponse;
import com.ggstore.api.models.Categoria;
import com.ggstore.api.models.Juego;
import com.ggstore.api.repository.CategoriaRepository;
import com.ggstore.api.repository.JuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private final JuegoRepository juegoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public Page<JuegoResponse> listar(int page, int size, String titulo, UUID categoriaId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo").ascending());

        if (titulo != null && !titulo.isBlank()) {
            return juegoRepository.findByTituloContainingIgnoreCase(titulo, pageable)
                    .map(JuegoResponse::from);
        }
        if (categoriaId != null) {
            return juegoRepository.findByCategoriaId(categoriaId, pageable)
                    .map(JuegoResponse::from);
        }
        return juegoRepository.findAll(pageable).map(JuegoResponse::from);
    }

    @Transactional(readOnly = true)
    public JuegoResponse obtenerPorId(UUID id) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        return JuegoResponse.from(juego);
    }

    @Transactional
    public JuegoResponse crear(JuegoRequest request) {
        Juego juego = buildJuego(new Juego(), request);
        return JuegoResponse.from(juegoRepository.save(juego));
    }

    @Transactional
    public JuegoResponse actualizar(UUID id, JuegoRequest request) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        return JuegoResponse.from(juegoRepository.save(buildJuego(juego, request)));
    }

    @Transactional
    public void eliminar(UUID id) {
        if (!juegoRepository.existsById(id)) {
            throw new RuntimeException("Juego no encontrado");
        }
        juegoRepository.deleteById(id);
    }

    private Juego buildJuego(Juego juego, JuegoRequest request) {
        Categoria categoria = null;
        if (request.categoriaId() != null) {
            categoria = categoriaRepository.findById(request.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        }
        juego.setTitulo(request.titulo());
        juego.setDescripcion(request.descripcion());
        juego.setPrecio(request.precio());
        juego.setStock(request.stock());
        juego.setImagenUrl(request.imagenUrl());
        juego.setPlataforma(request.plataforma());
        juego.setCategoria(categoria);
        juego.setDescuentoPorcentaje(request.descuentoPorcentaje());
        return juego;
    }
}