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
public class Book implements Exportable {
    
    private String id;
    private String isbn;
    private String title;
    private String authorId;
    private String publisher;
    private LocalDate publicationDate;
    private String genre;
    private Integer pageCount;
    private Double price;
    private Integer stock;
    private String description;
    private String language;
    
    @Override
    public String toCsv() {
        String escapedDescription = description != null ? 
            "\"" + description.replace("\"", "\"\"") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%d,%.2f,%d,%s,%s",
            id, isbn, title, authorId, publisher, publicationDate, 
            genre, pageCount, price, stock, escapedDescription, language);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,isbn,title,authorId,publisher,publicationDate,genre,pageCount,price,stock,description,language";
    }
    
    /**
     * Checks if the book is available in stock
     */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }
    
    /**
     * Decreases the stock by the specified quantity
     */
    public boolean reduceStock(Integer quantity) {
        if (stock != null && stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Increases the stock by the specified quantity
     */
    public void increaseStock(Integer quantity) {
        if (stock == null) {
            stock = 0;
        }
        stock += quantity;
    }
    
    /**
     * Calculates the age of the book in years
     */
    public Integer calculateAge() {
        if (publicationDate == null) {
            return null;
        }
        return LocalDate.now().getYear() - publicationDate.getYear();
    }
}
