package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Abstract class representing an employee in the system
 * Demonstrates inheritance and polymorphism
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Empleado implements Exportable, Notificable {
    
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private LocalDate fechaContratacion;
    private Double salarioBase;
    private String cargo;
    
    /**
     * Abstract method to calculate salary
     * Each subclass must implement its own calculation logic
     */
    public abstract Double calcularSalario();
    
    /**
     * Abstract method to get employee role description
     */
    public abstract String obtenerRol();
    
    @Override
    public void enviarNotificacion(String mensaje) {
        System.out.println(String.format("Notificación para %s %s (%s): %s", 
            nombre, apellido, email, mensaje));
    }
    
    @Override
    public String getContactoNotificacion() {
        return email;
    }
    
    @Override
    public String getCsvHeader() {
        return "id,nombre,apellido,email,telefono,fechaContratacion,salarioBase,cargo,tipo";
    }
    
    /**
     * Gets the full name of the employee
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
