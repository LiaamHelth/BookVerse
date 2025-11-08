package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Book;
import java.util.List;
import java.util.Optional;

public interface IBookRepository {
    List<Book> findAll();
    Optional<Book> findById(String id);
    Book save(Book book);
    void deleteById(String id);
    boolean existsById(String id);
    List<Book> findByAuthorId(String authorId);
    List<Book> findByGenre(String genre);
    List<Book> findAvailableBooks();
}
