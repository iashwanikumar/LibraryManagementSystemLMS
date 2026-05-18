package librarymanagement;

import librarymanagement.gui.LoginFrame;
import librarymanagement.service.*;
import librarymanagement.util.LibraryException;
import javax.swing.*;

/**
 * Main GUI Application Entry Point
 * Library Management System with Swing GUI
 */
public class LibraryManagementSystemGUI {
    
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;
    
    public LibraryManagementSystemGUI() {
        this.bookService = new BookService();
        this.userService = new UserService();
        this.transactionService = new TransactionService(bookService, userService);
    }
    
    public void start() {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize default data
        initializeDefaultData();
        
        // Show login frame
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(userService, bookService, transactionService);
            loginFrame.setVisible(true);
        });
    }
    
    /**
     * Initialize with default admin and sample data
     */
    private void initializeDefaultData() {
        try {
            // Check if admin already exists
            try {
                userService.login("admin@library.com", "admin123");
            } catch (LibraryException e) {
                // Admin doesn't exist, create one
                userService.addAdmin("System Admin", "admin@library.com", "admin123");
                System.out.println("[System] Default admin created");
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
                bookService.addBook("The Pragmatic Programmer", "Andrew Hunt", 
                        "Addison-Wesley", "Technology", "9780135957059", 4);
                bookService.addBook("Design Patterns", "Erich Gamma", 
                        "Addison-Wesley", "Technology", "9780201633610", 2);
                bookService.addBook("Pride and Prejudice", "Jane Austen", 
                        "T. Egerton", "Classic", "9780141439518", 5);
                
                System.out.println("[System] Sample books added to library");
            }
            
        } catch (LibraryException e) {
            System.err.println("Error initializing data: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        LibraryManagementSystemGUI system = new LibraryManagementSystemGUI();
        system.start();
    }
}