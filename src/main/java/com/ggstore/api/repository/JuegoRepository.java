package com.ggstore.api.repository;

import com.ggstore.api.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JuegoRepository extends JpaRepository<Juego, UUID> {
    Page<Juego> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<Juego> findByCategoriaId(UUID categoriaId, Pageable pageable);
}