package com.ggstore.api.service;

import com.ggstore.api.dto.BibliotecaResponse;
import com.ggstore.api.repository.BibliotecaRepository;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;

    @Transactional(readOnly = true)
    public List<BibliotecaResponse> obtenerBiblioteca(UserPrincipal principal) {
        return bibliotecaRepository.findByUsuarioId(principal.getId())
                .stream()
                .map(BibliotecaResponse::from)
                .toList();
    }
}
