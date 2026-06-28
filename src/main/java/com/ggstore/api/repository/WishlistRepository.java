package com.ggstore.api.repository;

import com.ggstore.api.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    List<Wishlist> findByUsuarioId(UUID usuarioId);
    Optional<Wishlist> findByUsuarioIdAndJuegoId(UUID usuarioId, UUID juegoId);
}