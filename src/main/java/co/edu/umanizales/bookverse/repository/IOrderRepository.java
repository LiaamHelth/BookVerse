package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Order;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    List<Order> findAll();
    Optional<Order> findById(String id);
    Order save(Order order);
    void deleteById(String id);
    boolean existsById(String id);
    List<Order> findByCustomerId(String customerId);
    List<Order> findBySalespersonId(String salespersonId);
    List<Order> findByStatus(String status);
}
