package com.ggstore.api.controller;

import com.ggstore.api.dto.ResenaRequest;
import com.ggstore.api.dto.ResenaResponse;
import com.ggstore.api.security.UserPrincipal;
import com.ggstore.api.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping("/juego/{juegoId}")
    public ResponseEntity<List<ResenaResponse>> porJuego(@PathVariable UUID juegoId) {
        return ResponseEntity.ok(resenaService.obtenerPorJuego(juegoId));
    }

    @GetMapping("/juego/{juegoId}/promedio")
    public ResponseEntity<Double> promedio(@PathVariable UUID juegoId) {
        return ResponseEntity.ok(resenaService.promedioPorJuego(juegoId));
    }

    @PostMapping
    public ResponseEntity<ResenaResponse> crear(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ResenaRequest request) {
        return ResponseEntity.ok(resenaService.crear(principal, request));
    }

    @DeleteMapping("/{resenaId}")
    public ResponseEntity<Void> eliminar(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID resenaId) {
        resenaService.eliminar(principal, resenaId);
        return ResponseEntity.noContent().build();
    }
}
