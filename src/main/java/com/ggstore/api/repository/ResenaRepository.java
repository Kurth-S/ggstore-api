package com.ggstore.api.repository;

import com.ggstore.api.models.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResenaRepository extends JpaRepository<Resena, UUID> {
    List<Resena> findByJuegoId(UUID juegoId);

    @Query("SELECT AVG(r.puntuacion) FROM Resena r WHERE r.juego.id = :juegoId")
    Double promedioByJuegoId(@Param("juegoId") UUID juegoId);
}
