package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete class representing an administrator
 * Demonstrates inheritance and polymorphism from Employee
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends Employee {
    
    private String accessLevel;
    private List<String> permissions;
    private String department;
    private double annualBonus;
    
    public Administrator(String id, String name, String lastName, String email, String phone,
                        LocalDate hireDate, double baseSalary, String position,
                        String accessLevel, List<String> permissions, String department, double annualBonus) {
        super(id, name, lastName, email, phone, hireDate, baseSalary, position);
        this.accessLevel = accessLevel;
        this.permissions = permissions != null ? permissions : new ArrayList<>();
        this.department = department;
        this.annualBonus = annualBonus;
    }
    
    @Override
    public double calculateSalary() {
        // Base salary + annual bonus prorated monthly
        return getBaseSalary() + (annualBonus / 12);
    }
    
    @Override
    public String getRole() {
        return String.format("Administrator - %s (Level: %s)", department, accessLevel);
    }
    
    @Override
    public String toCsv() {
        String permissionsStr = permissions != null ? String.join(";", permissions) : "";
        return String.format(java.util.Locale.US, "%s,%s,%s,%s,%s,%s,%.2f,%s,Administrator,%s,\"%s\",%s,%.2f",
            getId(), getName(), getLastName(), getEmail(), getPhone(),
            getHireDate(), getBaseSalary(), getPosition(),
            accessLevel, permissionsStr, department, annualBonus);
    }
    
    /**
     * Adds a permission to the administrator
     */
    public void addPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<>();
        }
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }
    
    /**
     * Removes a permission from the administrator
     */
    public void removePermission(String permission) {
        if (this.permissions != null) {
            this.permissions.remove(permission);
        }
    }
    
    /**
     * Checks if the administrator has a specific permission
     */
    public boolean hasPermission(String permission) {
        return this.permissions != null && this.permissions.contains(permission);
    }
}
