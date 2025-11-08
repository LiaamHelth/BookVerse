package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.Customer;
import co.edu.umanizales.bookverse.repository.ICustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CustomerCsvRepository implements ICustomerRepository {
    
    private final String filePath;
    
    public CustomerCsvRepository(@Value("${bookverse.data.path:./data}") String dataPath) {
        this.filePath = dataPath + "/clientes.csv";
        ensureDirectoryExists();
    }
    
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            log.error("Error initializing customers data file", e);
            throw new RuntimeException("Error initializing data file", e);
        }
    }
    
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        customers.add(parseLine(line));
                    } catch (Exception e) {
                        log.error("Error parsing customer line: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading customers file", e);
            throw new RuntimeException("Error reading customers file", e);
        }
        return customers;
    }
    
    @Override
    public Optional<Customer> findById(String id) {
        return findAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Customer save(Customer customer) {
        List<Customer> customers = findAll();
        
        if (customer.getId() == null || customer.getId().isEmpty()) {
            customer.setId(UUID.randomUUID().toString());
            customers.add(customer);
            log.info("Creating new customer with ID: {}", customer.getId());
        } else {
            boolean found = false;
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getId().equals(customer.getId())) {
                    customers.set(i, customer);
                    found = true;
                    log.info("Updating customer with ID: {}", customer.getId());
                    break;
                }
            }
            if (!found) {
                customers.add(customer);
                log.info("Adding customer with existing ID: {}", customer.getId());
            }
        }
        
        saveAll(customers);
        return customer;
    }
    
    @Override
    public void deleteById(String id) {
        List<Customer> customers = findAll();
        boolean removed = customers.removeIf(c -> c.getId().equals(id));
        if (removed) {
            log.info("Deleted customer with ID: {}", id);
            saveAll(customers);
        } else {
            log.warn("Customer with ID {} not found for deletion", id);
        }
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(c -> c.getId().equals(id));
    }
    
    @Override
    public List<Customer> findActiveCustomers() {
        return findAll().stream()
                .filter(Customer::isActive)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Customer> findByEmail(String email) {
        return findAll().stream()
                .filter(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Customer customer : customers) {
                writer.println(toCSV(customer));
            }
        } catch (IOException e) {
            log.error("Error saving customers", e);
            throw new RuntimeException("Error saving customers", e);
        }
    }
    
    private Customer parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        Customer customer = new Customer();
        customer.setId(parts[0]);
        customer.setName(parts.length > 1 ? parts[1] : "");
        customer.setLastName(parts.length > 2 ? parts[2] : "");
        customer.setEmail(parts.length > 3 ? parts[3] : "");
        customer.setPhone(parts.length > 4 ? parts[4] : "");
        customer.setAddress(parts.length > 5 ? parts[5] : "");
        
        if (parts.length > 6 && !parts[6].isEmpty()) {
            try {
                customer.setRegistrationDate(LocalDate.parse(parts[6]));
            } catch (Exception e) {
                log.warn("Error parsing registration date: {}", parts[6]);
            }
        }
        
        if (parts.length > 7 && !parts[7].isEmpty()) {
            String historyStr = parts[7].replaceAll("^\"|\"$", "");
            if (!historyStr.isEmpty()) {
                List<String> orderHistory = Arrays.asList(historyStr.split(";"));
                customer.setOrderHistory(new ArrayList<>(orderHistory));
            }
        }
        
        if (parts.length > 8) {
            customer.setActive(Boolean.parseBoolean(parts[8]));
        }
        
        return customer;
    }
    
    private String toCSV(Customer customer) {
        String historyStr = customer.getOrderHistory() != null && !customer.getOrderHistory().isEmpty() ? 
            "\"" + String.join(";", customer.getOrderHistory()) + "\"" : "";
            
        return String.join(",",
            escapeCsv(customer.getId()),
            escapeCsv(customer.getName()),
            escapeCsv(customer.getLastName()),
            escapeCsv(customer.getEmail()),
            escapeCsv(customer.getPhone()),
            escapeCsv(customer.getAddress()),
            customer.getRegistrationDate() != null ? customer.getRegistrationDate().toString() : "",
            historyStr,
            String.valueOf(customer.isActive())
        );
    }
    
    private String escapeCsv(String input) {
        if (input == null) {
            return "";
        }
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
    }
}
