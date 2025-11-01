package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Concrete class representing a salesperson
 * Demonstrates inheritance and polymorphism from Employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Salesperson extends Employee {
    
    private Double commissionPerSale;
    private Integer salesCompleted;
    private String assignedZone;
    
    public Salesperson(String id, String name, String lastName, String email, String phone,
                   LocalDate hireDate, Double baseSalary, String position,
                   Double commissionPerSale, Integer salesCompleted, String assignedZone) {
        super(id, name, lastName, email, phone, hireDate, baseSalary, position);
        this.commissionPerSale = commissionPerSale;
        this.salesCompleted = salesCompleted;
        this.assignedZone = assignedZone;
    }
    
    @Override
    public Double calculateSalary() {
        // Base salary + commission for each sale completed
        return getBaseSalary() + (commissionPerSale * salesCompleted);
    }
    
    @Override
    public String getRole() {
        return "Salesperson - Zone: " + assignedZone;
    }
    
    @Override
    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s,%s,%.2f,%s,Salesperson,%.2f,%d,%s",
            getId(), getName(), getLastName(), getEmail(), getPhone(),
            getHireDate(), getBaseSalary(), getPosition(),
            commissionPerSale, salesCompleted, assignedZone);
    }
    
    /**
     * Registers a new sale for the salesperson
     */
    public void registerSale() {
        this.salesCompleted++;
    }
    
    /**
     * Gets the total commission earned
     */
    public Double getTotalCommission() {
        return commissionPerSale * salesCompleted;
    }
}
