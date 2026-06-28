package com.ggstore.api.repository;

import com.ggstore.api.models.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, UUID> {
    List<Biblioteca> findByUsuarioId(UUID usuarioId);
    boolean existsByUsuarioIdAndJuegoId(UUID usuarioId, UUID juegoId);
}