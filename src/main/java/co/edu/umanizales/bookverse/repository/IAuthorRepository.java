package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Author;
import java.util.List;
import java.util.Optional;

public interface IAuthorRepository {
    List<Author> findAll();
    Optional<Author> findById(String id);
    Author save(Author author);
    void deleteById(String id);
    boolean existsById(String id);
}
