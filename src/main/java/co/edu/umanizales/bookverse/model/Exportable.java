package co.edu.umanizales.bookverse.model;

/**
 * Interface for entities that can be exported to CSV format
 */
public interface Exportable {
    
    /**
     * Converts the entity to a CSV row format
     * @return String representation in CSV format
     */
    String toCsv();
    
    /**
     * Gets the CSV header for this entity type
     * @return String with column names separated by commas
     */
    String getCsvHeader();
}
