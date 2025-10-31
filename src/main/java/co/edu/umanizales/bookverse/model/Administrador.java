package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class representing an administrator
 * Demonstrates inheritance and polymorphism from Empleado
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrador extends Empleado {
    
    private String nivelAcceso;
    private List<String> permisos;
    private String departamento;
    private Double bonoAnual;
    
    public Administrador(String id, String nombre, String apellido, String email, String telefono,
                        LocalDate fechaContratacion, Double salarioBase, String cargo,
                        String nivelAcceso, List<String> permisos, String departamento, Double bonoAnual) {
        super(id, nombre, apellido, email, telefono, fechaContratacion, salarioBase, cargo);
        this.nivelAcceso = nivelAcceso;
        this.permisos = permisos != null ? permisos : new ArrayList<>();
        this.departamento = departamento;
        this.bonoAnual = bonoAnual;
    }
    
    @Override
    public Double calcularSalario() {
        // Salario base + bono anual prorrateado mensualmente
        return getSalarioBase() + (bonoAnual / 12);
    }
    
    @Override
    public String obtenerRol() {
        return String.format("Administrador - %s (Nivel: %s)", departamento, nivelAcceso);
    }
    
    @Override
    public String toCsv() {
        String permisosStr = permisos != null ? String.join(";", permisos) : "";
        return String.format("%s,%s,%s,%s,%s,%s,%.2f,%s,Administrador,%s,\"%s\",%s,%.2f",
            getId(), getNombre(), getApellido(), getEmail(), getTelefono(),
            getFechaContratacion(), getSalarioBase(), getCargo(),
            nivelAcceso, permisosStr, departamento, bonoAnual);
    }
    
    /**
     * Adds a permission to the administrator
     */
    public void agregarPermiso(String permiso) {
        if (this.permisos == null) {
            this.permisos = new ArrayList<>();
        }
        if (!this.permisos.contains(permiso)) {
            this.permisos.add(permiso);
        }
    }
    
    /**
     * Removes a permission from the administrator
     */
    public void removerPermiso(String permiso) {
        if (this.permisos != null) {
            this.permisos.remove(permiso);
        }
    }
    
    /**
     * Checks if the administrator has a specific permission
     */
    public boolean tienePermiso(String permiso) {
        return this.permisos != null && this.permisos.contains(permiso);
    }
}
