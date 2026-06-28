package com.ggstore.api.service;

import com.ggstore.api.dto.WishlistResponse;
import com.ggstore.api.models.Juego;
import com.ggstore.api.models.Usuario;
import com.ggstore.api.models.Wishlist;
import com.ggstore.api.repository.*;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<WishlistResponse> obtener(UserPrincipal principal) {
        return wishlistRepository.findByUsuarioId(principal.getId())
                .stream()
                .map(WishlistResponse::from)
                .toList();
    }

    @Transactional
    public WishlistResponse agregar(UserPrincipal principal, UUID juegoId) {
        if (wishlistRepository.findByUsuarioIdAndJuegoId(principal.getId(), juegoId).isPresent()) {
            throw new IllegalArgumentException("El juego ya está en tu lista de deseos");
        }
        Usuario usuario = usuarioRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Juego juego = juegoRepository.findById(juegoId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        Wishlist wishlist = Wishlist.builder()
                .usuario(usuario)
                .juego(juego)
                .build();
        return WishlistResponse.from(wishlistRepository.save(wishlist));
    }

    @Transactional
    public void eliminar(UserPrincipal principal, UUID wishlistId) {
        Wishlist w = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        if (!w.getUsuario().getId().equals(principal.getId())) {
            throw new RuntimeException("No autorizado");
        }
        wishlistRepository.delete(w);
    }
}
