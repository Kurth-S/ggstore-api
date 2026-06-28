package com.ggstore.api.repository;

import com.ggstore.api.dto.JuegoMasVendidoResponse;
import com.ggstore.api.models.PedidoDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, UUID> {

    List<PedidoDetalle> findByPedidoId(UUID pedidoId);

    @Query("""
        SELECT new com.ggstore.api.dto.JuegoMasVendidoResponse(
            pd.juego.id, pd.juego.titulo, SUM(pd.cantidad)
        )
        FROM PedidoDetalle pd
        GROUP BY pd.juego.id, pd.juego.titulo
        ORDER BY SUM(pd.cantidad) DESC
        """)
    List<JuegoMasVendidoResponse> topJuegosVendidos(Pageable pageable);
}