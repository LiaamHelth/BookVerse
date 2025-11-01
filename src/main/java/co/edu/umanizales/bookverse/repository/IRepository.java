package co.edu.umanizales.bookverse.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for basic CRUD operations
 * @param <T> Entity type
 * @param <ID> Entity ID type
 */
public interface IRepository<T, ID> {
    /**
     * Gets all elements
     */
    List<T> findAll();
    
    /**
     * Finds an element by its ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Saves an element (creates or updates)
     */
    T save(T entity);
    
    /**
     * Deletes an element by its ID
     */
    void deleteById(ID id);
    
    /**
     * Checks if an element exists with the given ID
     */
    boolean existsById(ID id);
    
    /**
     * Counts the total number of elements
     */
    long count();
}
