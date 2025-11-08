package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.model.Customer;
import co.edu.umanizales.bookverse.repository.ICustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    private final ICustomerRepository customerRepository;

    @Autowired
    public CustomerService(ICustomerRepository customerRepository) {
        log.info("Initializing CustomerService with repository: {}", customerRepository.getClass().getName());
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean deleteCustomer(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.info("Customer deleted successfully: {}", id);
            return true;
        }
        log.warn("Customer not found for deletion: {}", id);
        return false;
    }

    public boolean existsById(String id) {
        return customerRepository.existsById(id);
    }

    public List<Customer> getActiveCustomers() {
        return customerRepository.findActiveCustomers();
    }

    public List<Customer> getCustomersByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public boolean deactivateCustomer(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setActive(false);
            customerRepository.save(customer);
            log.info("Customer deactivated: {}", id);
            return true;
        }
        log.warn("Customer not found for deactivation: {}", id);
        return false;
    }

    public boolean activateCustomer(String id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setActive(true);
            customerRepository.save(customer);
            log.info("Customer activated: {}", id);
            return true;
        }
        log.warn("Customer not found for activation: {}", id);
        return false;
    }
}
