package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Regular class representing a customer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements Exportable, Notificable {
    
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
    private List<String> historialPedidos;
    private Boolean activo;
    
    public Cliente(String id, String nombre, String apellido, String email, String telefono, 
                  String direccion, LocalDate fechaRegistro, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.historialPedidos = new ArrayList<>();
        this.activo = activo;
    }
    
    @Override
    public String toCsv() {
        String historialStr = historialPedidos != null ? 
            "\"" + String.join(";", historialPedidos) + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
            id, nombre, apellido, email, telefono, direccion, 
            fechaRegistro, historialStr, activo);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,nombre,apellido,email,telefono,direccion,fechaRegistro,historialPedidos,activo";
    }
    
    @Override
    public void enviarNotificacion(String mensaje) {
        System.out.println(String.format("Notificación enviada a %s %s (%s): %s",
            nombre, apellido, email, mensaje));
    }
    
    @Override
    public String getContactoNotificacion() {
        return email;
    }
    
    /**
     * Gets the full name of the customer
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Adds a new order to the purchase history
     */
    public void agregarPedido(String idPedido) {
        if (this.historialPedidos == null) {
            this.historialPedidos = new ArrayList<>();
        }
        this.historialPedidos.add(idPedido);
    }
    
    /**
     * Gets the total number of orders made by the customer
     */
    public Integer obtenerTotalPedidos() {
        return historialPedidos != null ? historialPedidos.size() : 0;
    }
    
    /**
     * Checks if the customer is a frequent buyer (more than 5 orders)
     */
    public boolean esClienteFrecuente() {
        return obtenerTotalPedidos() >= 5;
    }
}
