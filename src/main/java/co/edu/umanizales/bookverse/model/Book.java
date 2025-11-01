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
public class Book implements Exportable {
    
    private String id;
    private String isbn;
    private String title;
    private Author author;
    private String publisher;
    private LocalDate publicationDate;
    private String genre;
    private int pageCount;
    private double price;
    private int stock;
    private String description;
    private String language;
    
    /**
     * Main constructor with Author object
     */
    public Book(String id, String isbn, String title, Author author, String publisher, 
                LocalDate publicationDate, String genre, int pageCount, double price, 
                int stock, String description, String language) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.pageCount = pageCount;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.language = language;
    }
    
    /**
     * Backward-compatible constructor with String authorId
     * @deprecated Use constructor with Author object instead
     */
    @Deprecated
    public Book(String id, String isbn, String title, String authorId, String publisher, 
                LocalDate publicationDate, String genre, int pageCount, double price, 
                int stock, String description, String language) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = new Author();
        this.author.setId(authorId);
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.pageCount = pageCount;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.language = language;
    }
    
    /**
     * Gets the author ID for backward compatibility
     */
    public String getAuthorId() {
        return author != null ? author.getId() : null;
    }
    
    /**
     * Gets the author name for display
     */
    public String getAuthorName() {
        return author != null ? author.getFullName() : "Unknown";
    }
    
    @Override
    public String toCsv() {
        String escapedDescription = description != null ? 
            "\"" + description.replace("\"", "\"\"") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%d,%.2f,%d,%s,%s",
            id, isbn, title, getAuthorId(), publisher, publicationDate, 
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
        return stock > 0;
    }
    
    /**
     * Decreases the stock by the specified quantity
     */
    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    /**
     * Increases the stock by the specified quantity
     */
    public void increaseStock(int quantity) {
        stock += quantity;
    }
    
    /**
     * Calculates the age of the book in years
     */
    public int calculateAge() {
        if (publicationDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - publicationDate.getYear();
    }
}
