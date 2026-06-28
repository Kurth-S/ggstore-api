package com.ggstore.api.repository;

import com.ggstore.api.models.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CuponRepository extends JpaRepository<Cupon, UUID> {
    Optional<Cupon> findByCodigoAndActivoTrue(String codigo);
}