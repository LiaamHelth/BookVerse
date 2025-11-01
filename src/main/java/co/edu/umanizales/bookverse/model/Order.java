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
@AllArgsConstructor
public class Order implements Exportable {
    
    private String id;
    private String customerId;
    private String salespersonId;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private PaymentMethod paymentMethod;
    private String status;
    private String shippingAddress;
    
    public Order(String id, String customerId, String salespersonId, LocalDateTime orderDate,
                 PaymentMethod paymentMethod, String status, String shippingAddress) {
        this.id = id;
        this.customerId = customerId;
        this.salespersonId = salespersonId;
        this.orderDate = orderDate;
        this.items = new ArrayList<>();
        this.subtotal = 0.0;
        this.taxes = 0.0;
        this.total = 0.0;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.shippingAddress = shippingAddress;
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
            id, customerId, salespersonId, orderDate, itemsStr, 
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
    public Integer getTotalItemCount() {
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
        return new PurchaseHistory(id, customerId, orderDate, total, paymentMethod, status);
    }
    
    /**
     * Inner class representing an order item
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String bookId;
        private String bookTitle;
        private Integer quantity;
        private Double unitPrice;
        
        public Double calculateSubtotal() {
            return quantity * unitPrice;
        }
    }
}
