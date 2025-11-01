package co.edu.umanizales.bookverse.model;

/**
 * Interface for entities that can receive notifications
 */
public interface Notificable {
    
    /**
     * Sends a notification to the entity
     * @param message The notification message
     */
    void sendNotification(String message);
    
    /**
     * Gets the notification contact information
     * @return Contact information for notifications
     */
    String getNotificationContact();
}
