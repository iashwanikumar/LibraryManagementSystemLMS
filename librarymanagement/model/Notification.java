package librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;



/**
 * In-app notification system
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean isRead;
    private String actionLink; // Optional: bookId or transactionId
    
    public Notification() {}
    
    public Notification(String notificationId, String userId, String title, 
                       String message, NotificationType type) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }
    
    public void markAsRead() {
        this.isRead = true;
    }
    
    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { 
        this.notificationId = notificationId; 
    }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getActionLink() { return actionLink; }
    public void setActionLink(String actionLink) { this.actionLink = actionLink; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s", 
                type, title, isRead ? "Read" : "Unread");
    }
}