package com.ggstore.api.controller;

import com.ggstore.api.dto.JuegoRequest;
import com.ggstore.api.dto.JuegoResponse;
import com.ggstore.api.service.JuegoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoService juegoService;

    @GetMapping
    public ResponseEntity<Page<JuegoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) UUID categoriaId) {
        return ResponseEntity.ok(juegoService.listar(page, size, titulo, categoriaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoResponse> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(juegoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegoResponse> crear(@Valid @RequestBody JuegoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JuegoResponse> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody JuegoRequest request) {
        return ResponseEntity.ok(juegoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        juegoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}