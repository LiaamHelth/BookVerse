package co.edu.umanizales.bookverse.repository;

import co.edu.umanizales.bookverse.model.Employee;
import java.util.List;
import java.util.Optional;

public interface IEmployeeRepository {
    List<Employee> findAll();
    Optional<Employee> findById(String id);
    Employee save(Employee employee);
    void deleteById(String id);
    boolean existsById(String id);
    List<Employee> findByPosition(String position);
    List<Employee> findByType(String type);
}
