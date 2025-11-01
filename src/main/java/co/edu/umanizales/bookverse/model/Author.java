package co.edu.umanizales.bookverse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Regular class representing an author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Exportable {
    
    private String id;
    private String name;
    private String lastName;
    private String nationality;
    private LocalDate birthDate;
    private String biography;
    private String email;
    
    @Override
    public String toCsv() {
        String escapedBiography = biography != null ? 
            "\"" + biography.replace("\"", "\"\"") + "\"" : "";
        return String.format("%s,%s,%s,%s,%s,%s,%s",
            id, name, lastName, nationality, birthDate, 
            escapedBiography, email);
    }
    
    @Override
    public String getCsvHeader() {
        return "id,name,lastName,nationality,birthDate,biography,email";
    }
    
    /**
     * Gets the full name of the author
     */
    public String getFullName() {
        return name + " " + lastName;
    }
    
    /**
     * Calculates the age of the author
     */
    public int calculateAge() {
        if (birthDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
