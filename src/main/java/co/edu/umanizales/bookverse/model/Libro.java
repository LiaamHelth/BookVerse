package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Regular class representing a book in the catalog
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro implements Exportable {
    
    private String id;
    private String isbn;
    private String titulo;
    private String idAutor;
    private String editorial;
    private LocalDate fechaPublicacion;
    private String genero;
    private Integer numeroPaginas;
    private Double precio;
    private Integer stock;
    private String descripcion;
    private String idioma;
    
    @Override
    public String toCsv() {
        String descripcionEscapada = descripcion != null ? 
            "\"" + descripcion.replace("\"", "\"\"") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%d,%.2f,%d,%s,%s",
            id, isbn, titulo, idAutor, editorial, fechaPublicacion, 
            genero, numeroPaginas, precio, stock, descripcionEscapada, idioma);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,isbn,titulo,idAutor,editorial,fechaPublicacion,genero,numeroPaginas,precio,stock,descripcion,idioma";
    }
    
    /**
     * Checks if the book is available in stock
     */
    public boolean estaDisponible() {
        return stock != null && stock > 0;
    }
    
    /**
     * Decreases the stock by the specified quantity
     */
    public boolean reducirStock(Integer cantidad) {
        if (stock != null && stock >= cantidad) {
            stock -= cantidad;
            return true;
        }
        return false;
    }
    
    /**
     * Increases the stock by the specified quantity
     */
    public void aumentarStock(Integer cantidad) {
        if (stock == null) {
            stock = 0;
        }
        stock += cantidad;
    }
    
    /**
     * Calculates the age of the book in years
     */
    public Integer calcularAntiguedad() {
        if (fechaPublicacion == null) {
            return null;
        }
        return LocalDate.now().getYear() - fechaPublicacion.getYear();
    }
}
