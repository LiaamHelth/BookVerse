package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Concrete class representing a salesperson
 * Demonstrates inheritance and polymorphism from Empleado
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor extends Empleado {
    
    private Double comisionPorVenta;
    private Integer ventasRealizadas;
    private String zonaAsignada;
    
    public Vendedor(String id, String nombre, String apellido, String email, String telefono,
                   LocalDate fechaContratacion, Double salarioBase, String cargo,
                   Double comisionPorVenta, Integer ventasRealizadas, String zonaAsignada) {
        super(id, nombre, apellido, email, telefono, fechaContratacion, salarioBase, cargo);
        this.comisionPorVenta = comisionPorVenta;
        this.ventasRealizadas = ventasRealizadas;
        this.zonaAsignada = zonaAsignada;
    }
    
    @Override
    public Double calcularSalario() {
        // Salario base + comisión por cada venta realizada
        return getSalarioBase() + (comisionPorVenta * ventasRealizadas);
    }
    
    @Override
    public String obtenerRol() {
        return "Vendedor - Zona: " + zonaAsignada;
    }
    
    @Override
    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s,%s,%.2f,%s,Vendedor,%.2f,%d,%s",
            getId(), getNombre(), getApellido(), getEmail(), getTelefono(),
            getFechaContratacion(), getSalarioBase(), getCargo(),
            comisionPorVenta, ventasRealizadas, zonaAsignada);
    }
    
    /**
     * Registers a new sale for the salesperson
     */
    public void registrarVenta() {
        this.ventasRealizadas++;
    }
    
    /**
     * Gets the total commission earned
     */
    public Double obtenerComisionTotal() {
        return comisionPorVenta * ventasRealizadas;
    }
}
