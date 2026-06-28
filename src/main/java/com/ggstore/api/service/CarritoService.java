package com.ggstore.api.service;

import com.ggstore.api.dto.AgregarAlCarritoRequest;
import com.ggstore.api.dto.CarritoDetalleResponse;
import com.ggstore.api.dto.CarritoResponse;
import com.ggstore.api.models.*;
import com.ggstore.api.repository.*;
import com.ggstore.api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final JuegoRepository juegoRepository;
    private final UsuarioRepository usuarioRepository;

    private Carrito obtenerOCrearCarrito(UUID usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    Carrito nuevo = Carrito.builder().usuario(usuario).build();
                    return carritoRepository.save(nuevo);
                });
    }

    @Transactional(readOnly = true)
    public CarritoResponse obtenerCarrito(UserPrincipal principal) {
        Carrito carrito = obtenerOCrearCarrito(principal.getId());
        return buildCarritoResponse(carrito);
    }

    @Transactional
    public CarritoResponse agregar(UserPrincipal principal, AgregarAlCarritoRequest request) {
        Carrito carrito = obtenerOCrearCarrito(principal.getId());

        Juego juego = juegoRepository.findById(request.juegoId())
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        if (juego.getStock() < request.cantidad()) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + juego.getStock());
        }

        CarritoDetalle detalle = carritoDetalleRepository
                .findByCarritoIdAndJuegoId(carrito.getId(), juego.getId())
                .orElse(CarritoDetalle.builder().carrito(carrito).juego(juego).cantidad(0).build());

        int nuevaCantidad = detalle.getCantidad() + request.cantidad();

        if (juego.getStock() < nuevaCantidad) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + juego.getStock());
        }

        detalle.setCantidad(nuevaCantidad);
        carritoDetalleRepository.save(detalle);

        return buildCarritoResponse(carrito);
    }

    @Transactional
    public CarritoResponse actualizar(UserPrincipal principal, UUID detalleId, int cantidad) {
        CarritoDetalle detalle = carritoDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        if (!detalle.getCarrito().getUsuario().getId().equals(principal.getId())) {
            throw new RuntimeException("No autorizado");
        }

        if (cantidad <= 0) {
            carritoDetalleRepository.delete(detalle);
        } else {
            if (detalle.getJuego().getStock() < cantidad) {
                throw new IllegalStateException("Stock insuficiente. Disponible: " + detalle.getJuego().getStock());
            }
            detalle.setCantidad(cantidad);
            carritoDetalleRepository.save(detalle);
        }

        Carrito carrito = carritoRepository.findByUsuarioId(principal.getId())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return buildCarritoResponse(carrito);
    }

    @Transactional
    public void eliminarItem(UserPrincipal principal, UUID detalleId) {
        CarritoDetalle detalle = carritoDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        if (!detalle.getCarrito().getUsuario().getId().equals(principal.getId())) {
            throw new RuntimeException("No autorizado");
        }

        carritoDetalleRepository.delete(detalle);
    }

    @Transactional
    public void vaciarCarrito(UUID usuarioId) {
        carritoRepository.findByUsuarioId(usuarioId).ifPresent(carrito -> {
            List<CarritoDetalle> items = carritoDetalleRepository.findByCarritoId(carrito.getId());
            carritoDetalleRepository.deleteAll(items);
        });
    }

    private CarritoResponse buildCarritoResponse(Carrito carrito) {
        List<CarritoDetalle> items = carritoDetalleRepository.findByCarritoId(carrito.getId());

        List<CarritoDetalleResponse> detalles = items.stream()
                .map(CarritoDetalleResponse::from)
                .toList();

        BigDecimal total = detalles.stream()
                .map(CarritoDetalleResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoResponse(carrito.getId(), detalles, total);
    }
}