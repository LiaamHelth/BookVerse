package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Customer;
import java.util.List;
import java.util.Optional;

public interface ICustomerRepository {
    List<Customer> findAll();
    Optional<Customer> findById(String id);
    Customer save(Customer customer);
    void deleteById(String id);
    boolean existsById(String id);
    List<Customer> findActiveCustomers();
    List<Customer> findByEmail(String email);
}
