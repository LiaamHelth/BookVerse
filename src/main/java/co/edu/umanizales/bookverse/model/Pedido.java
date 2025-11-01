package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Regular class representing an order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido implements Exportable {
    
    private String id;
    private String idCliente;
    private String idVendedor;
    private LocalDateTime fechaPedido;
    private List<ItemPedido> items;
    private Double subtotal;
    private Double impuestos;
    private Double total;
    private MetodoPago metodoPago;
    private String estado;
    private String direccionEnvio;
    
    public Pedido(String id, String idCliente, String idVendedor, LocalDateTime fechaPedido,
                 MetodoPago metodoPago, String estado, String direccionEnvio) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.fechaPedido = fechaPedido;
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.impuestos = 0.0;
        this.total = 0.0;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
    }
    
    @Override
    public String toCsv() {
        String itemsStr = items != null ? 
            "\"" + items.stream()
                .map(item -> String.format("%s:%d:%.2f", 
                    item.getIdLibro(), item.getCantidad(), item.getPrecioUnitario()))
                .reduce((a, b) -> a + ";" + b)
                .orElse("") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%s,%s,%s",
            id, idCliente, idVendedor, fechaPedido, itemsStr, 
            subtotal, impuestos, total, metodoPago, estado, direccionEnvio);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,idCliente,idVendedor,fechaPedido,items,subtotal,impuestos,total,metodoPago,estado,direccionEnvio";
    }
    
    /**
     * Adds an item to the order
     */
    public void agregarItem(ItemPedido item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        calcularTotales();
    }
    
    /**
     * Removes an item from the order
     */
    public boolean removerItem(String idLibro) {
        if (this.items == null) {
            return false;
        }
        boolean removed = this.items.removeIf(item -> item.getIdLibro().equals(idLibro));
        if (removed) {
            calcularTotales();
        }
        return removed;
    }
    
    /**
     * Calculates subtotal, taxes, and total
     */
    public void calcularTotales() {
        if (items == null || items.isEmpty()) {
            this.subtotal = 0.0;
            this.impuestos = 0.0;
            this.total = 0.0;
            return;
        }
        
        this.subtotal = items.stream()
            .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
            .sum();
        
        this.impuestos = subtotal * 0.19; // 19% tax
        this.total = subtotal + impuestos;
    }
    
    /**
     * Gets the total number of items in the order
     */
    public Integer obtenerCantidadItems() {
        return items != null ? items.stream()
            .mapToInt(ItemPedido::getCantidad)
            .sum() : 0;
    }
    
    /**
     * Checks if the order is completed
     */
    public boolean estaCompletado() {
        return "COMPLETADO".equalsIgnoreCase(estado) || 
               "ENTREGADO".equalsIgnoreCase(estado);
    }
    
    /**
     * Converts the order to a HistorialCompra record
     */
    public HistorialCompra toHistorialCompra() {
        return new HistorialCompra(id, idCliente, fechaPedido, total, metodoPago, estado);
    }
    
    /**
     * Inner class representing an order item
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedido {
        private String idLibro;
        private String tituloLibro;
        private Integer cantidad;
        private Double precioUnitario;
        
        public Double calcularSubtotal() {
            return cantidad * precioUnitario;
        }
    }
}
