package co.edu.umanizales.bookverse.model;

import java.time.LocalDateTime;

/**
 * Record representing a purchase history entry
 * Records are immutable and ideal for data transfer objects
 */
public record HistorialCompra(
    String idPedido,
    String idCliente,
    LocalDateTime fechaCompra,
    Double montoTotal,
    MetodoPago metodoPago,
    String estado
) {
    
    /**
     * Compact constructor with validation
     */
    public HistorialCompra {
        if (idPedido == null || idPedido.isBlank()) {
            throw new IllegalArgumentException("El ID del pedido no puede estar vacío");
        }
        if (idCliente == null || idCliente.isBlank()) {
            throw new IllegalArgumentException("El ID del cliente no puede estar vacío");
        }
        if (montoTotal == null || montoTotal < 0) {
            throw new IllegalArgumentException("El monto total debe ser mayor o igual a cero");
        }
        if (metodoPago == null) {
            throw new IllegalArgumentException("El método de pago no puede ser nulo");
        }
    }
    
    /**
     * Formats the purchase history as a readable string
     */
    public String formatear() {
        return String.format("Pedido: %s | Cliente: %s | Fecha: %s | Total: $%.2f | Pago: %s | Estado: %s",
            idPedido, idCliente, fechaCompra, montoTotal, metodoPago.getNombre(), estado);
    }
}
