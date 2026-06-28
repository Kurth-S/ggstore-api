package com.ggstore.api.repository;

import com.ggstore.api.models.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, UUID> {
    List<CarritoDetalle> findByCarritoId(UUID carritoId);
    Optional<CarritoDetalle> findByCarritoIdAndJuegoId(UUID carritoId, UUID juegoId);
}
