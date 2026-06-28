package com.ggstore.api.service;

import com.ggstore.api.dto.CheckoutRequest;
import com.ggstore.api.dto.PedidoResponse;
import com.ggstore.api.enums.EstadoPedido;
import com.ggstore.api.models.*;
import com.ggstore.api.repository.*;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final JuegoRepository juegoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final CuponRepository cuponRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public PedidoResponse checkout(UserPrincipal principal, CheckoutRequest request) {

        // 1. Obtener carrito del usuario
        Carrito carrito = carritoRepository.findByUsuarioId(principal.getId())
                .orElseThrow(() -> new RuntimeException("El carrito está vacío"));

        List<CarritoDetalle> items = carritoDetalleRepository.findByCarritoId(carrito.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // 2. Verificar stock y calcular total
        BigDecimal total = BigDecimal.ZERO;

        for (CarritoDetalle item : items) {
            Juego juego = item.getJuego();
            if (juego.getStock() < item.getCantidad()) {
                throw new IllegalStateException(
                    "Stock insuficiente para: " + juego.getTitulo() +
                    ". Disponible: " + juego.getStock()
                );
            }
            BigDecimal descuento = juego.getDescuentoPorcentaje() != null
                    ? juego.getDescuentoPorcentaje() : BigDecimal.ZERO;
            BigDecimal precioFinal = juego.getPrecio()
                    .multiply(BigDecimal.ONE.subtract(descuento.divide(BigDecimal.valueOf(100))));
            total = total.add(precioFinal.multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        // 3. Aplicar cupón si viene en el request
        Cupon cupon = null;
        if (request.codigoCupon() != null && !request.codigoCupon().isBlank()) {
            cupon = cuponRepository.findByCodigoAndActivoTrue(request.codigoCupon())
                    .orElseThrow(() -> new RuntimeException("Cupón inválido o expirado"));

            if (cupon.getFechaExpiracion() != null &&
                cupon.getFechaExpiracion().isBefore(OffsetDateTime.now())) {
                throw new RuntimeException("El cupón ha expirado");
            }

            BigDecimal descuentoCupon = cupon.getPorcentajeDescuento()
                    .divide(BigDecimal.valueOf(100));
            total = total.multiply(BigDecimal.ONE.subtract(descuentoCupon));
        }

        // 4. Crear el pedido
        Usuario usuario = usuarioRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fecha(OffsetDateTime.now())
                .total(total)
                .estado(EstadoPedido.PAGADO) // en un proyecto real aquí iría el proceso de pago
                .cupon(cupon)
                .build();
        pedidoRepository.save(pedido);

        // 5. Crear detalles, descontar stock y generar claves digitales
        List<PedidoDetalle> detalles = new ArrayList<>();

        for (CarritoDetalle item : items) {
            Juego juego = item.getJuego();

            // Descontar stock
            juego.setStock(juego.getStock() - item.getCantidad());
            juegoRepository.save(juego);

            // Calcular precio al momento de la compra
            BigDecimal descuento = juego.getDescuentoPorcentaje() != null
                    ? juego.getDescuentoPorcentaje() : BigDecimal.ZERO;
            BigDecimal precio = juego.getPrecio()
                    .multiply(BigDecimal.ONE.subtract(descuento.divide(BigDecimal.valueOf(100))));

            // Generar clave digital ficticia
            String claveDigital = "GG-" + juego.getId().toString().substring(0, 8).toUpperCase()
                    + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            PedidoDetalle detalle = PedidoDetalle.builder()
                    .pedido(pedido)
                    .juego(juego)
                    .precio(precio)
                    .cantidad(item.getCantidad())
                    .claveDigital(claveDigital)
                    .build();
            pedidoDetalleRepository.save(detalle);
            detalles.add(detalle);

            // 6. Agregar a biblioteca si no lo tiene ya
            if (!bibliotecaRepository.existsByUsuarioIdAndJuegoId(principal.getId(), juego.getId())) {
                Biblioteca entrada = Biblioteca.builder()
                        .usuario(usuario)
                        .juego(juego)
                        .pedidoDetalle(detalle)
                        .build();
                bibliotecaRepository.save(entrada);
            }
        }

        // 7. Vaciar el carrito
        carritoDetalleRepository.deleteAll(items);

        return PedidoResponse.from(pedido, detalles);
    }

    public List<PedidoResponse> historial(UserPrincipal principal) {
        return pedidoRepository.findByUsuarioIdOrderByFechaDesc(principal.getId())
                .stream()
                .map(pedido -> {
                    List<PedidoDetalle> detalles = pedidoDetalleRepository.findByPedidoId(pedido.getId());
                    return PedidoResponse.from(pedido, detalles);
                })
                .toList();
    }
}