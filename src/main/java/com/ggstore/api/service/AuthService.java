package com.ggstore.api.service;

import com.ggstore.api.dto.AuthResponse;
import com.ggstore.api.dto.LoginRequest;
import com.ggstore.api.dto.RegisterRequest;
import com.ggstore.api.enums.RolUsuario;
import com.ggstore.api.models.Usuario;
import com.ggstore.api.repository.UsuarioRepository;
import com.ggstore.api.security.JwtService;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // nunca texto plano
                .rol(RolUsuario.USUARIO) // todo registro nuevo es USUARIO, nunca ADMIN
                .build();

        usuarioRepository.save(usuario);

        UserPrincipal principal = new UserPrincipal(usuario);
        String token = jwtService.generateToken(principal);

        return new AuthResponse(token, usuario.getNombre(), usuario.getEmail(), usuario.getRol().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        UserPrincipal principal = new UserPrincipal(usuario);
        String token = jwtService.generateToken(principal);

        return new AuthResponse(token, usuario.getNombre(), usuario.getEmail(), usuario.getRol().name());
    }
}