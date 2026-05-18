package librarymanagement.model;

import java.io.Serializable;

/**
 * Abstract base class for all users in the system
 * Demonstrates Abstraction and Inheritance
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected UserRole role;
    
    public User() {}
    
    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract void displayDashboard();
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Email: %s | Role: %s",
                userId, name, email, role);
    }
}

