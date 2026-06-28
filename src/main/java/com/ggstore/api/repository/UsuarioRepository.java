package com.ggstore.api.repository;

import com.ggstore.api.enums.RolUsuario;
import com.ggstore.api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRol(RolUsuario rol);
}