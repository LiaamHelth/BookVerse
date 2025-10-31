package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Autor;
import java.util.List;
import java.util.Optional;

public interface IAutorRepository {
    List<Autor> findAll();
    Optional<Autor> findById(String id);
    Autor save(Autor autor);
    void deleteById(String id);
    boolean existsById(String id);
}
