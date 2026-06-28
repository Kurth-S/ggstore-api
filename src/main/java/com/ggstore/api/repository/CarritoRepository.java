package com.ggstore.api.repository;

import com.ggstore.api.models.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarritoRepository extends JpaRepository<Carrito, UUID> {
    Optional<Carrito> findByUsuarioId(UUID usuarioId);
}