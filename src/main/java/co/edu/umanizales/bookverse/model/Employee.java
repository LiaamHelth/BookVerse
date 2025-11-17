package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

/**
 * Abstract class representing an employee in the system
 * Demonstrates inheritance and polymorphism
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Administrator.class, name = "Administrator"),
    @JsonSubTypes.Type(value = Salesperson.class, name = "Salesperson")
})
public abstract class Employee implements Exportable, Notificable {
    
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private double baseSalary;
    private String position;
    
    /**
     * Abstract method to calculate salary
     * Each subclass must implement its own calculation logic
     */
    public abstract double calculateSalary();
    
    /**
     * Abstract method to get employee role description
     */
    public abstract String getRole();
    
    @Override
    public void sendNotification(String message) {
        System.out.println(String.format("Notification for %s %s (%s): %s", 
            name, lastName, email, message));
    }
    
    @Override
    public String getNotificationContact() {
        return email;
    }
    
    @Override
    public String getCsvHeader() {
        return "id,name,lastName,email,phone,hireDate,baseSalary,position,type";
    }
    
    /**
     * Gets the full name of the employee
     */
    public String getFullName() {
        return name + " " + lastName;
    }
}
