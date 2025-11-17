package co.edu.umanizales.bookverse.controller;

import co.edu.umanizales.bookverse.model.Book;
import co.edu.umanizales.bookverse.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable String id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(
            @RequestBody Book book) {
        try {
            Book savedBook = bookService.saveBook(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
        if (!bookService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        book.setId(id);
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable String id) {
        try {
            if (bookService.deleteBook(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        Integer stock = request.get("stock");
        if (stock == null || stock < 0) {
            return ResponseEntity.badRequest().body("Invalid stock value");
        }
        
        if (bookService.updateStock(id, stock)) {
            return ResponseEntity.ok().body(Map.of("message", "Stock updated successfully"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<?> reduceStock(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().body("Invalid quantity value");
        }
        
        try {
            if (bookService.reduceStock(id, quantity)) {
                return ResponseEntity.ok().body(Map.of("message", "Stock reduced successfully"));
            } else {
                return ResponseEntity.badRequest().body("Insufficient stock");
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/increase-stock")
    public ResponseEntity<?> increaseStock(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().body("Invalid quantity value");
        }
        
        try {
            bookService.increaseStock(id, quantity);
            return ResponseEntity.ok().body(Map.of("message", "Stock increased successfully"));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
