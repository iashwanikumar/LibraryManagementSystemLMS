package librarymanagement.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Admin class - represents an administrator
 * Demonstrates Inheritance
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;
    
    public Admin() {
        super();
    }
    
    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.ADMIN);
    }
    
    @Override
    public void displayDashboard() {
        System.out.println("\n=== ADMIN DASHBOARD ===");
        System.out.println("Welcome, " + name + "!");
        System.out.println("You have full access to the system.");
    }
}
