package librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Activity logging for audit trail
 */
public class ActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String logId;
    private String userId;
    private String action;
    private String entityType; // BOOK, USER, TRANSACTION, etc.
    private String entityId;
    private String details;
    private LocalDateTime timestamp;
    private String ipAddress;
    
    public ActivityLog() {}
    
    public ActivityLog(String logId, String userId, String action, 
                      String entityType, String entityId, String details) {
        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s performed %s on %s(%s)",
                timestamp, userId, action, entityType, entityId);
    }
}