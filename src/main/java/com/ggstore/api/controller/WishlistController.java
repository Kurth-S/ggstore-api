package com.ggstore.api.controller;

import com.ggstore.api.dto.WishlistResponse;
import com.ggstore.api.security.UserPrincipal;
import com.ggstore.api.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> obtener(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(wishlistService.obtener(principal));
    }

    @PostMapping("/{juegoId}")
    public ResponseEntity<WishlistResponse> agregar(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID juegoId) {
        return ResponseEntity.ok(wishlistService.agregar(principal, juegoId));
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> eliminar(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable UUID wishlistId) {
        wishlistService.eliminar(principal, wishlistId);
        return ResponseEntity.noContent().build();
    }
}