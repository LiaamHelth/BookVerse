package co.edu.umanizales.bookverse.model;

import java.time.LocalDateTime;

/**
 * Record representing a purchase history entry
 * Records are immutable and ideal for data transfer objects
 */
public record PurchaseHistory(
    String orderId,
    String customerId,
    LocalDateTime purchaseDate,
    double totalAmount,
    PaymentMethod paymentMethod,
    String status
) {
    
    /**
     * Compact constructor with validation
     */
    public PurchaseHistory {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be empty");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount must be greater than or equal to zero");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
    }
    
    /**
     * Formats the purchase history as a readable string
     */
    public String format() {
        return String.format("Order: %s | Customer: %s | Date: %s | Total: $%.2f | Payment: %s | Status: %s",
            orderId, customerId, purchaseDate, totalAmount, paymentMethod.getName(), status);
    }
}
