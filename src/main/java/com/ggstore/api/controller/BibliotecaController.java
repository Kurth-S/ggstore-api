package com.ggstore.api.controller;

import com.ggstore.api.dto.BibliotecaResponse;
import com.ggstore.api.security.UserPrincipal;
import com.ggstore.api.service.BibliotecaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
@RequiredArgsConstructor
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    @GetMapping
    public ResponseEntity<List<BibliotecaResponse>> obtener(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(bibliotecaService.obtenerBiblioteca(principal));
    }
}