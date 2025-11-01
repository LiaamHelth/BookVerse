package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Regular class representing a customer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Exportable, Notificable {
    
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate registrationDate;
    private List<String> orderHistory;
    private boolean active;
    
    public Customer(String id, String name, String lastName, String email, String phone, 
                  String address, LocalDate registrationDate, boolean active) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
        this.orderHistory = new ArrayList<>();
        this.active = active;
    }
    
    @Override
    public String toCsv() {
        String historyStr = orderHistory != null ? 
            "\"" + String.join(";", orderHistory) + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
            id, name, lastName, email, phone, address, 
            registrationDate, historyStr, active);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,name,lastName,email,phone,address,registrationDate,orderHistory,active";
    }
    
    @Override
    public void sendNotification(String message) {
        System.out.println(String.format("Notification sent to %s %s (%s): %s",
            name, lastName, email, message));
    }
    
    @Override
    public String getNotificationContact() {
        return email;
    }
    
    /**
     * Gets the full name of the customer
     */
    public String getFullName() {
        return name + " " + lastName;
    }
    
    /**
     * Adds a new order to the purchase history using order ID
     */
    public void addOrder(String orderId) {
        if (this.orderHistory == null) {
            this.orderHistory = new ArrayList<>();
        }
        this.orderHistory.add(orderId);
    }
    
    /**
     * Adds a new order to the purchase history using Order object
     */
    public void addOrder(Order order) {
        if (order != null && order.getId() != null) {
            addOrder(order.getId());
        }
    }
    
    /**
     * Removes an order from the purchase history
     */
    public boolean removeOrder(String orderId) {
        if (this.orderHistory == null) {
            return false;
        }
        return this.orderHistory.remove(orderId);
    }
    
    /**
     * Gets the total number of orders made by the customer
     */
    public int getTotalOrders() {
        return orderHistory != null ? orderHistory.size() : 0;
    }
    
    /**
     * Checks if the customer is a frequent buyer (more than 5 orders)
     */
    public boolean isFrequentCustomer() {
        return getTotalOrders() >= 5;
    }
}
