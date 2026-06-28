package com.ggstore.api.repository;

import com.ggstore.api.dto.JuegoMasVendidoResponse;
import com.ggstore.api.dto.VentasMensualesResponse;
import com.ggstore.api.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByUsuarioIdOrderByFechaDesc(UUID usuarioId);

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado = 'PAGADO'")
    BigDecimal sumIngresosTotales();

    @Query("""
        SELECT new com.ggstore.api.dto.VentasMensualesResponse(
            YEAR(p.fecha), MONTH(p.fecha), COUNT(p), SUM(p.total)
        )
        FROM Pedido p
        WHERE p.estado = 'PAGADO'
        GROUP BY YEAR(p.fecha), MONTH(p.fecha)
        ORDER BY YEAR(p.fecha) DESC, MONTH(p.fecha) DESC
        """)
    List<VentasMensualesResponse> ventasPorMes();
}