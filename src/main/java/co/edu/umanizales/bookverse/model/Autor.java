package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Regular class representing an author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor implements Exportable {
    
    private String id;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String biografia;
    private String email;
    
    @Override
    public String toCsv() {
        String biografiaEscapada = biografia != null ? 
            "\"" + biografia.replace("\"", "\"\"") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s",
            id, nombre, apellido, nacionalidad, fechaNacimiento, 
            biografiaEscapada, email);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,nombre,apellido,nacionalidad,fechaNacimiento,biografia,email";
    }
    
    /**
     * Gets the full name of the author
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Calculates the age of the author
     */
    public Integer calcularEdad() {
        if (fechaNacimiento == null) {
            return null;
        }
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
}
