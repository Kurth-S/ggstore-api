package com.ggstore.api.service;

import com.ggstore.api.dto.DashboardResponse;
import com.ggstore.api.enums.RolUsuario;
import com.ggstore.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;

    public DashboardResponse obtenerEstadisticas() {
        long totalUsuarios = usuarioRepository.countByRol(RolUsuario.USUARIO);
        long totalPedidos = pedidoRepository.count();

        BigDecimal ingresos = pedidoRepository.sumIngresosTotales();
        if (ingresos == null) ingresos = BigDecimal.ZERO;

        return new DashboardResponse(
                totalUsuarios,
                totalPedidos,
                ingresos,
                pedidoRepository.ventasPorMes(),
                pedidoDetalleRepository.topJuegosVendidos(PageRequest.of(0, 10))
        );
    }
}