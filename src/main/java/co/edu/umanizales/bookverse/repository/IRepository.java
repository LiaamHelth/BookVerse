package co.edu.umanizales.bookverse.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD básicas
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del ID de la entidad
 */
public interface IRepository<T, ID> {
    /**
     * Obtiene todos los elementos
     */
    List<T> findAll();
    
    /**
     * Busca un elemento por su ID
     */
    Optional<T> findById(ID id);
    
    /**
     * Guarda un elemento (crea o actualiza)
     */
    T save(T entity);
    
    /**
     * Elimina un elemento por su ID
     */
    void deleteById(ID id);
    
    /**
     * Verifica si existe un elemento con el ID dado
     */
    boolean existsById(ID id);
    
    /**
     * Cuenta el número total de elementos
     */
    long count();
}
