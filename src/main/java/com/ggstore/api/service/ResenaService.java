package com.ggstore.api.service;

import com.ggstore.api.dto.ResenaRequest;
import com.ggstore.api.dto.ResenaResponse;
import com.ggstore.api.models.Juego;
import com.ggstore.api.models.Resena;
import com.ggstore.api.models.Usuario;
import com.ggstore.api.repository.*;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ResenaResponse> obtenerPorJuego(UUID juegoId) {
        return resenaRepository.findByJuegoId(juegoId)
                .stream()
                .map(ResenaResponse::from)
                .toList();
    }

    public Double promedioPorJuego(UUID juegoId) {
        return resenaRepository.promedioByJuegoId(juegoId);
    }

    public ResenaResponse crear(UserPrincipal principal, ResenaRequest request) {
        if (resenaRepository.findByJuegoId(request.juegoId())
                .stream()
                .anyMatch(r -> r.getUsuario().getId().equals(principal.getId()))) {
            throw new IllegalArgumentException("Ya reseñaste este juego");
        }

        Usuario usuario = usuarioRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Juego juego = juegoRepository.findById(request.juegoId())
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        Resena resena = Resena.builder()
                .usuario(usuario)
                .juego(juego)
                .puntuacion(request.puntuacion())
                .comentario(request.comentario())
                .build();

        return ResenaResponse.from(resenaRepository.save(resena));
    }

    public void eliminar(UserPrincipal principal, UUID resenaId) {
        Resena resena = resenaRepository.findById(resenaId)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        if (!resena.getUsuario().getId().equals(principal.getId())) {
            throw new RuntimeException("No autorizado");
        }
        resenaRepository.delete(resena);
    }
}