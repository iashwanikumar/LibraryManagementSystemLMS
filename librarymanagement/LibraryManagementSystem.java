package librarymanagement;

import librarymanagement.model.*;
import librarymanagement.service.*;
import librarymanagement.ui.*;
import librarymanagement.util.*;
import java.util.Scanner;

/**
 * Main application class - Entry point
 * Library Management System
 */
public class LibraryManagementSystem {
    
    private final Scanner scanner;
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;
    
    public LibraryManagementSystem() {
        this.scanner = new Scanner(System.in);
        this.bookService = new BookService();
        this.userService = new UserService();
        this.transactionService = new TransactionService(bookService, userService);
    }
    
    public void start() {
        displayWelcomeMessage();
        initializeDefaultData();
        
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    System.out.println("\nThank you for using Library Management System!");
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayWelcomeMessage() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║   LIBRARY MANAGEMENT SYSTEM                   ║");
        System.out.println("║   Developed with Java OOP Principles          ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Login");
        System.out.println("2. Register (New Member)");
        System.out.println("3. Exit");
        System.out.print("\nEnter your choice: ");
    }
    
    private void login() {
        try {
            System.out.println("\n=== LOGIN ===");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            User user = userService.login(email, password);
            
            System.out.println("\n✓ Login successful!");
            
            // Route to appropriate UI based on role
            if (user.getRole() == UserRole.ADMIN) {
                AdminUI adminUI = new AdminUI(scanner, user, bookService, 
                        userService, transactionService);
                adminUI.showMenu();
            } else {
                MemberUI memberUI = new MemberUI(scanner, user, bookService, 
                        transactionService);
                memberUI.showMenu();
            }
            
        } catch (LibraryException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
    
    private void register() {
        try {
            System.out.println("\n=== MEMBER REGISTRATION ===");
            
            System.out.print("Full Name: ");
            String name = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Password (min 6 characters): ");
            String password = scanner.nextLine();
            
            System.out.print("Confirm Password: ");
            String confirmPassword = scanner.nextLine();
            
            if (!password.equals(confirmPassword)) {
                System.out.println("✗ Passwords do not match!");
                return;
            }
            
            String userId = userService.registerMember(name, email, password);
            
            System.out.println("\n✓ Registration successful!");
            System.out.println("Your Member ID: " + userId);
            System.out.println("You can now login with your email and password.");
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    /**
     * Initialize with default admin and sample data
     */
    private void initializeDefaultData() {
        try {
            // Check if admin already exists
            User existingAdmin = null;
            try {
                existingAdmin = userService.login("admin@library.com", "admin123");
            } catch (LibraryException e) {
                // Admin doesn't exist, create one
            }
            
            if (existingAdmin == null) {
                userService.addAdmin("System Admin", "admin@library.com", "admin123");
                System.out.println("\n[System] Default admin created");
                System.out.println("Admin Login - Email: admin@library.com, Password: admin123");
                
                // Add sample books
                bookService.addBook("The Great Gatsby", "F. Scott Fitzgerald", 
                        "Scribner", "Fiction", "9780743273565", 5);
                bookService.addBook("To Kill a Mockingbird", "Harper Lee", 
                        "J.B. Lippincott & Co.", "Fiction", "9780061120084", 3);
                bookService.addBook("1984", "George Orwell", 
                        "Secker & Warburg", "Dystopian", "9780451524935", 4);
                bookService.addBook("Clean Code", "Robert C. Martin", 
                        "Prentice Hall", "Technology", "9780132350884", 2);
                bookService.addBook("Introduction to Algorithms", "Thomas H. Cormen", 
                        "MIT Press", "Computer Science", "9780262033848", 3);
                
                System.out.println("[System] Sample books added to library");
            }
            
        } catch (LibraryException e) {
            System.err.println("Error initializing data: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.start();
    }
}