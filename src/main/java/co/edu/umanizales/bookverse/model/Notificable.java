package co.edu.umanizales.bookverse.model;

/**
 * Interface for entities that can receive notifications
 */
public interface Notificable {
    
    /**
     * Sends a notification to the entity
     * @param mensaje The notification message
     */
    void enviarNotificacion(String mensaje);
    
    /**
     * Gets the notification contact information
     * @return Contact information for notifications
     */
    String getContactoNotificacion();
}
