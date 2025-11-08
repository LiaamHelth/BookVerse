package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.Administrator;
import co.edu.umanizales.bookverse.model.Employee;
import co.edu.umanizales.bookverse.model.Salesperson;
import co.edu.umanizales.bookverse.repository.IEmployeeRepository;
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
public class EmployeeCsvRepository implements IEmployeeRepository {
    
    private final String filePath;
    
    public EmployeeCsvRepository(@Value("${bookverse.data.path:./data}") String dataPath) {
        this.filePath = dataPath + "/empleados.csv";
        ensureDirectoryExists();
    }
    
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            log.error("Error initializing employees data file", e);
            throw new RuntimeException("Error initializing data file", e);
        }
    }
    
    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        employees.add(parseLine(line));
                    } catch (Exception e) {
                        log.error("Error parsing employee line: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading employees file", e);
            throw new RuntimeException("Error reading employees file", e);
        }
        return employees;
    }
    
    @Override
    public Optional<Employee> findById(String id) {
        return findAll().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Employee save(Employee employee) {
        List<Employee> employees = findAll();
        
        if (employee.getId() == null || employee.getId().isEmpty()) {
            employee.setId(UUID.randomUUID().toString());
            employees.add(employee);
            log.info("Creating new employee with ID: {}", employee.getId());
        } else {
            boolean found = false;
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getId().equals(employee.getId())) {
                    employees.set(i, employee);
                    found = true;
                    log.info("Updating employee with ID: {}", employee.getId());
                    break;
                }
            }
            if (!found) {
                employees.add(employee);
                log.info("Adding employee with existing ID: {}", employee.getId());
            }
        }
        
        saveAll(employees);
        return employee;
    }
    
    @Override
    public void deleteById(String id) {
        List<Employee> employees = findAll();
        boolean removed = employees.removeIf(e -> e.getId().equals(id));
        if (removed) {
            log.info("Deleted employee with ID: {}", id);
            saveAll(employees);
        } else {
            log.warn("Employee with ID {} not found for deletion", id);
        }
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(e -> e.getId().equals(id));
    }
    
    @Override
    public List<Employee> findByPosition(String position) {
        return findAll().stream()
                .filter(e -> e.getPosition() != null && e.getPosition().equalsIgnoreCase(position))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Employee> findByType(String type) {
        return findAll().stream()
                .filter(e -> {
                    if ("Administrator".equalsIgnoreCase(type)) {
                        return e instanceof Administrator;
                    } else if ("Salesperson".equalsIgnoreCase(type)) {
                        return e instanceof Salesperson;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Employee employee : employees) {
                writer.println(employee.toCsv());
            }
        } catch (IOException e) {
            log.error("Error saving employees", e);
            throw new RuntimeException("Error saving employees", e);
        }
    }
    
    private Employee parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        String type = parts.length > 8 ? parts[8] : "";
        
        if ("Administrator".equalsIgnoreCase(type)) {
            return parseAdministrator(parts);
        } else if ("Salesperson".equalsIgnoreCase(type)) {
            return parseSalesperson(parts);
        } else {
            log.warn("Unknown employee type: {}", type);
            return null;
        }
    }
    
    private Administrator parseAdministrator(String[] parts) {
        Administrator admin = new Administrator();
        admin.setId(parts[0]);
        admin.setName(parts.length > 1 ? parts[1] : "");
        admin.setLastName(parts.length > 2 ? parts[2] : "");
        admin.setEmail(parts.length > 3 ? parts[3] : "");
        admin.setPhone(parts.length > 4 ? parts[4] : "");
        
        if (parts.length > 5 && !parts[5].isEmpty()) {
            try {
                admin.setHireDate(LocalDate.parse(parts[5]));
            } catch (Exception e) {
                log.warn("Error parsing hire date: {}", parts[5]);
            }
        }
        
        if (parts.length > 6 && !parts[6].isEmpty()) {
            try {
                admin.setBaseSalary(Double.parseDouble(parts[6]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing base salary: {}", parts[6]);
            }
        }
        
        admin.setPosition(parts.length > 7 ? parts[7] : "");
        admin.setAccessLevel(parts.length > 9 ? parts[9] : "");
        
        if (parts.length > 10 && !parts[10].isEmpty()) {
            String permissionsStr = parts[10].replaceAll("^\"|\"$", "");
            if (!permissionsStr.isEmpty()) {
                admin.setPermissions(new ArrayList<>(Arrays.asList(permissionsStr.split(";"))));
            }
        }
        
        admin.setDepartment(parts.length > 11 ? parts[11] : "");
        
        if (parts.length > 12 && !parts[12].isEmpty()) {
            try {
                admin.setAnnualBonus(Double.parseDouble(parts[12]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing annual bonus: {}", parts[12]);
            }
        }
        
        return admin;
    }
    
    private Salesperson parseSalesperson(String[] parts) {
        Salesperson salesperson = new Salesperson();
        salesperson.setId(parts[0]);
        salesperson.setName(parts.length > 1 ? parts[1] : "");
        salesperson.setLastName(parts.length > 2 ? parts[2] : "");
        salesperson.setEmail(parts.length > 3 ? parts[3] : "");
        salesperson.setPhone(parts.length > 4 ? parts[4] : "");
        
        if (parts.length > 5 && !parts[5].isEmpty()) {
            try {
                salesperson.setHireDate(LocalDate.parse(parts[5]));
            } catch (Exception e) {
                log.warn("Error parsing hire date: {}", parts[5]);
            }
        }
        
        if (parts.length > 6 && !parts[6].isEmpty()) {
            try {
                salesperson.setBaseSalary(Double.parseDouble(parts[6]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing base salary: {}", parts[6]);
            }
        }
        
        salesperson.setPosition(parts.length > 7 ? parts[7] : "");
        
        if (parts.length > 9 && !parts[9].isEmpty()) {
            try {
                salesperson.setCommissionPerSale(Double.parseDouble(parts[9]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing commission per sale: {}", parts[9]);
            }
        }
        
        if (parts.length > 10 && !parts[10].isEmpty()) {
            try {
                salesperson.setSalesCompleted(Integer.parseInt(parts[10]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing sales completed: {}", parts[10]);
            }
        }
        
        salesperson.setAssignedZone(parts.length > 11 ? parts[11] : "");
        
        return salesperson;
    }
}
