package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.model.Employee;
import co.edu.umanizales.bookverse.repository.IEmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(IEmployeeRepository employeeRepository) {
        log.info("Initializing EmployeeService with repository: {}", employeeRepository.getClass().getName());
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public boolean deleteEmployee(String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            log.info("Employee deleted successfully: {}", id);
            return true;
        }
        log.warn("Employee not found for deletion: {}", id);
        return false;
    }

    public boolean existsById(String id) {
        return employeeRepository.existsById(id);
    }

    public List<Employee> getEmployeesByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }

    public List<Employee> getEmployeesByType(String type) {
        return employeeRepository.findByType(type);
    }
}
