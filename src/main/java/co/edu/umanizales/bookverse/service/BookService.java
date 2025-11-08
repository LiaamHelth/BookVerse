package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.exception.ResourceNotFoundException;
import co.edu.umanizales.bookverse.model.Book;
import co.edu.umanizales.bookverse.repository.IBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    private final IBookRepository bookRepository;

    @Autowired
    public BookService(IBookRepository bookRepository) {
        log.info("Initializing BookService with repository: {}", bookRepository.getClass().getName());
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public boolean deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            log.info("Book deleted successfully: {}", id);
            return true;
        }
        log.warn("Book not found for deletion: {}", id);
        return false;
    }

    public boolean existsById(String id) {
        return bookRepository.existsById(id);
    }

    public List<Book> getBooksByAuthor(String authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    public boolean updateStock(String id, int quantity) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setStock(quantity);
            bookRepository.save(book);
            log.info("Stock updated for book {}: new stock = {}", id, quantity);
            return true;
        }
        log.warn("Book not found for stock update: {}", id);
        return false;
    }

    public boolean reduceStock(String id, int quantity) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            if (book.reduceStock(quantity)) {
                bookRepository.save(book);
                log.info("Stock reduced for book {}: quantity = {}", id, quantity);
                return true;
            }
            log.warn("Insufficient stock for book {}: requested = {}, available = {}", 
                id, quantity, book.getStock());
            return false;
        }
        throw new ResourceNotFoundException("Book not found with id: " + id);
    }

    public void increaseStock(String id, int quantity) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.increaseStock(quantity);
            bookRepository.save(book);
            log.info("Stock increased for book {}: quantity = {}", id, quantity);
        } else {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
    }
}
