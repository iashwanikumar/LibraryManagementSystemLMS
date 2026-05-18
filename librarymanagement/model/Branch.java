package librarymanagement.model;

import java.io.Serializable;

/**
 * Branch entity for multi-library support
 */
public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String branchId;
    private String branchName;
    private String location;
    private String contactNumber;
    private String email;
    private boolean isActive;
    
    public Branch() {}
    
    public Branch(String branchId, String branchName, String location, 
                 String contactNumber, String email) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.email = email;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", branchId, branchName, location);
    }
}