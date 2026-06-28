package com.ggstore.api.controller;

import com.ggstore.api.dto.CheckoutRequest;
import com.ggstore.api.dto.PedidoResponse;
import com.ggstore.api.security.UserPrincipal;
import com.ggstore.api.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody(required = false) CheckoutRequest request) {
        // Si no mandan body, usamos un request vacío (sin cupón)
        if (request == null) request = new CheckoutRequest(null);
        return ResponseEntity.ok(checkoutService.checkout(principal, request));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<PedidoResponse>> historial(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(checkoutService.historial(principal));
    }
}