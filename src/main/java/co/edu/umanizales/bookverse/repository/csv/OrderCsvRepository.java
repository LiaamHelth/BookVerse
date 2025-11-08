package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.*;
import co.edu.umanizales.bookverse.repository.IBookRepository;
import co.edu.umanizales.bookverse.repository.ICustomerRepository;
import co.edu.umanizales.bookverse.repository.IEmployeeRepository;
import co.edu.umanizales.bookverse.repository.IOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class OrderCsvRepository implements IOrderRepository {
    
    private final String filePath;
    private final ICustomerRepository customerRepository;
    private final IEmployeeRepository employeeRepository;
    private final IBookRepository bookRepository;
    
    @Autowired
    public OrderCsvRepository(
            @Value("${bookverse.data.path:./data}") String dataPath,
            ICustomerRepository customerRepository,
            IEmployeeRepository employeeRepository,
            IBookRepository bookRepository) {
        this.filePath = dataPath + "/ordenes.csv";
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.bookRepository = bookRepository;
        ensureDirectoryExists();
    }
    
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            log.error("Error initializing orders data file", e);
            throw new RuntimeException("Error initializing data file", e);
        }
    }
    
    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        orders.add(parseLine(line));
                    } catch (Exception e) {
                        log.error("Error parsing order line: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading orders file", e);
            throw new RuntimeException("Error reading orders file", e);
        }
        return orders;
    }
    
    @Override
    public Optional<Order> findById(String id) {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Order save(Order order) {
        List<Order> orders = findAll();
        
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
            orders.add(order);
            log.info("Creating new order with ID: {}", order.getId());
        } else {
            boolean found = false;
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getId().equals(order.getId())) {
                    orders.set(i, order);
                    found = true;
                    log.info("Updating order with ID: {}", order.getId());
                    break;
                }
            }
            if (!found) {
                orders.add(order);
                log.info("Adding order with existing ID: {}", order.getId());
            }
        }
        
        saveAll(orders);
        return order;
    }
    
    @Override
    public void deleteById(String id) {
        List<Order> orders = findAll();
        boolean removed = orders.removeIf(o -> o.getId().equals(id));
        if (removed) {
            log.info("Deleted order with ID: {}", id);
            saveAll(orders);
        } else {
            log.warn("Order with ID {} not found for deletion", id);
        }
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(o -> o.getId().equals(id));
    }
    
    @Override
    public List<Order> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(o -> o.getCustomerId() != null && o.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findBySalespersonId(String salespersonId) {
        return findAll().stream()
                .filter(o -> o.getSalespersonId() != null && o.getSalespersonId().equals(salespersonId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Order> findByStatus(String status) {
        return findAll().stream()
                .filter(o -> o.getStatus() != null && o.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Order> orders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Order order : orders) {
                writer.println(order.toCsv());
            }
        } catch (IOException e) {
            log.error("Error saving orders", e);
            throw new RuntimeException("Error saving orders", e);
        }
    }
    
    private Order parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        Order order = new Order();
        order.setId(parts[0]);
        
        if (parts.length > 1 && !parts[1].isEmpty()) {
            Optional<Customer> customer = customerRepository.findById(parts[1]);
            order.setCustomer(customer.orElseGet(() -> {
                Customer c = new Customer();
                c.setId(parts[1]);
                return c;
            }));
        }
        
        if (parts.length > 2 && !parts[2].isEmpty()) {
            Optional<Employee> employee = employeeRepository.findById(parts[2]);
            if (employee.isPresent() && employee.get() instanceof Salesperson) {
                order.setSalesperson((Salesperson) employee.get());
            } else {
                Salesperson s = new Salesperson();
                s.setId(parts[2]);
                order.setSalesperson(s);
            }
        }
        
        if (parts.length > 3 && !parts[3].isEmpty()) {
            try {
                order.setOrderDate(LocalDateTime.parse(parts[3]));
            } catch (Exception e) {
                log.warn("Error parsing order date: {}", parts[3]);
            }
        }
        
        if (parts.length > 4 && !parts[4].isEmpty()) {
            String itemsStr = parts[4].replaceAll("^\"|\"$", "");
            if (!itemsStr.isEmpty()) {
                List<Order.OrderItem> items = new ArrayList<>();
                String[] itemParts = itemsStr.split(";");
                for (String itemPart : itemParts) {
                    String[] itemData = itemPart.split(":");
                    if (itemData.length >= 3) {
                        try {
                            String bookId = itemData[0];
                            int quantity = Integer.parseInt(itemData[1]);
                            double unitPrice = Double.parseDouble(itemData[2]);
                            
                            Optional<Book> book = bookRepository.findById(bookId);
                            Order.OrderItem item = new Order.OrderItem(
                                book.orElseGet(() -> {
                                    Book b = new Book();
                                    b.setId(bookId);
                                    return b;
                                }),
                                quantity,
                                unitPrice
                            );
                            items.add(item);
                        } catch (Exception e) {
                            log.warn("Error parsing order item: {}", itemPart, e);
                        }
                    }
                }
                order.setItems(items);
            }
        }
        
        if (parts.length > 5 && !parts[5].isEmpty()) {
            try {
                order.setSubtotal(Double.parseDouble(parts[5]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing subtotal: {}", parts[5]);
            }
        }
        
        if (parts.length > 6 && !parts[6].isEmpty()) {
            try {
                order.setTaxes(Double.parseDouble(parts[6]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing taxes: {}", parts[6]);
            }
        }
        
        if (parts.length > 7 && !parts[7].isEmpty()) {
            try {
                order.setTotal(Double.parseDouble(parts[7]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing total: {}", parts[7]);
            }
        }
        
        if (parts.length > 8 && !parts[8].isEmpty()) {
            try {
                order.setPaymentMethod(PaymentMethod.valueOf(parts[8]));
            } catch (IllegalArgumentException e) {
                log.warn("Error parsing payment method: {}", parts[8]);
            }
        }
        
        order.setStatus(parts.length > 9 ? parts[9] : "");
        order.setShippingAddress(parts.length > 10 ? parts[10] : "");
        
        return order;
    }
}
