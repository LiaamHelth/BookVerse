package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Regular class representing an order
 */
@Data
@NoArgsConstructor
public class Order implements Exportable {
    
    private String id;
    private Customer customer;
    private Salesperson salesperson;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double subtotal;
    private double taxes;
    private double total;
    private PaymentMethod paymentMethod;
    private String status;
    private String shippingAddress;
    
    /**
     * Main constructor with object references
     */
    public Order(String id, Customer customer, Salesperson salesperson, LocalDateTime orderDate,
                 PaymentMethod paymentMethod, String status, String shippingAddress) {
        this.id = id;
        this.customer = customer;
        this.salesperson = salesperson;
        this.orderDate = orderDate;
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxes = 0.0;
        this.total = 0.0;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
    
    /**
     * Backward-compatible constructor with String IDs
     * @deprecated Use constructor with Customer and Salesperson objects instead
     */
    @Deprecated
    public Order(String id, String customerId, String salespersonId, LocalDateTime orderDate,
                 PaymentMethod paymentMethod, String status, String shippingAddress) {
        this.id = id;
        this.customer = new Customer();
        this.customer.setId(customerId);
        this.salesperson = new Salesperson();
        this.salesperson.setId(salespersonId);
        this.orderDate = orderDate;
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxes = 0.0;
        this.total = 0.0;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
    
    /**
     * Gets the customer ID for backward compatibility
     */
    public String getCustomerId() {
        return customer != null ? customer.getId() : null;
    }
    
    /**
     * Gets the salesperson ID for backward compatibility
     */
    public String getSalespersonId() {
        return salesperson != null ? salesperson.getId() : null;
    }
    
    @Override
    public String toCsv() {
        String itemsStr = items != null ? 
            "\"" + items.stream()
                .map(item -> String.format("%s:%d:%.2f", 
                    item.getBookId(), item.getQuantity(), item.getUnitPrice()))
                .reduce((a, b) -> a + ";" + b)
                .orElse("") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%s,%s,%s",
            id, getCustomerId(), getSalespersonId(), orderDate, itemsStr, 
            subtotal, taxes, total, paymentMethod, status, shippingAddress);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,customerId,salespersonId,orderDate,items,subtotal,taxes,total,paymentMethod,status,shippingAddress";
    }
    
    /**
     * Adds an item to the order
     */
    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        calculateTotals();
    }
    
    /**
     * Removes an item from the order
     */
    public boolean removeItem(String bookId) {
        if (this.items == null) {
            return false;
        }
        boolean removed = this.items.removeIf(item -> item.getBookId().equals(bookId));
        if (removed) {
            calculateTotals();
        }
        return removed;
    }
    
    /**
     * Calculates subtotal, taxes, and total
     */
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            this.subtotal = 0.0;
            this.taxes = 0.0;
            this.total = 0.0;
            return;
        }
        
        this.subtotal = items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
            .sum();
        
        this.taxes = subtotal * 0.19; // 19% tax
        this.total = subtotal + taxes;
    }
    
    /**
     * Gets the total number of items in the order
     */
    public int getTotalItemCount() {
        return items != null ? items.stream()
            .mapToInt(OrderItem::getQuantity)
            .sum() : 0;
    }
    
    /**
     * Checks if the order is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status) || 
               "DELIVERED".equalsIgnoreCase(status);
    }
    
    /**
     * Converts the order to a PurchaseHistory record
     */
    public PurchaseHistory toPurchaseHistory() {
        return new PurchaseHistory(id, getCustomerId(), orderDate, total, paymentMethod, status);
    }
    
    /**
     * Gets the customer name for display
     */
    public String getCustomerName() {
        return customer != null ? customer.getFullName() : "Unknown";
    }
    
    /**
     * Gets the salesperson name for display
     */
    public String getSalespersonName() {
        return salesperson != null ? salesperson.getFullName() : "Unknown";
    }
    
    /**
     * Inner class representing an order item with proper Book reference
     */
    @Data
    @NoArgsConstructor
    public static class OrderItem {
        private Book book;
        private int quantity;
        private double unitPrice;
        
        /**
         * Main constructor with Book object
         */
        public OrderItem(Book book, int quantity, double unitPrice) {
            this.book = book;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
        
        /**
         * Backward-compatible constructor with String bookId
         * @deprecated Use constructor with Book object instead
         */
        @Deprecated
        public OrderItem(String bookId, String bookTitle, int quantity, double unitPrice) {
            this.book = new Book();
            this.book.setId(bookId);
            this.book.setTitle(bookTitle);
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
        
        /**
         * Gets the book ID for backward compatibility
         */
        public String getBookId() {
            return book != null ? book.getId() : null;
        }
        
        /**
         * Gets the book title for backward compatibility
         */
        public String getBookTitle() {
            return book != null ? book.getTitle() : null;
        }
        
        public double calculateSubtotal() {
            return quantity * unitPrice;
        }
    }
}
