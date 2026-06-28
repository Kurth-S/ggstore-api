package com.ggstore.api.controller;

import com.ggstore.api.dto.AgregarAlCarritoRequest;
import com.ggstore.api.dto.CarritoResponse;
import com.ggstore.api.security.UserPrincipal;
import com.ggstore.api.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public ResponseEntity<CarritoResponse> obtener(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(principal));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregar(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AgregarAlCarritoRequest request) {
        return ResponseEntity.ok(carritoService.agregar(principal, request));
    }

    @PutMapping("/items/{detalleId}")
    public ResponseEntity<CarritoResponse> actualizar(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID detalleId,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.actualizar(principal, detalleId, cantidad));
    }

    @DeleteMapping("/items/{detalleId}")
    public ResponseEntity<Void> eliminarItem(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID detalleId) {
        carritoService.eliminarItem(principal, detalleId);
        return ResponseEntity.noContent().build();
    }
}