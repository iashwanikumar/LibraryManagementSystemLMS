package librarymanagement.ui;

import librarymanagement.model.*;
import librarymanagement.service.*;
import librarymanagement.util.LibraryException;
import java.util.List;
import java.util.Scanner;

/**
 * Admin user interface
 * Demonstrates Polymorphism through method overriding
 */
public class AdminUI {
    
    private final Scanner scanner;
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final User currentUser;
    
    public AdminUI(Scanner scanner, User user, BookService bookService, 
                   UserService userService, TransactionService transactionService) {
        this.scanner = scanner;
        this.currentUser = user;
        this.bookService = bookService;
        this.userService = userService;
        this.transactionService = transactionService;
    }
    
    public void showMenu() {
        while (true) {
            currentUser.displayDashboard();
            System.out.println("\n1. Add New Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Books");
            System.out.println("6. View All Issued Books");
            System.out.println("7. View Overdue Books");
            System.out.println("8. View All Members");
            System.out.println("9. View All Transactions");
            System.out.println("0. Logout");
            System.out.print("\nEnter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1" -> addBook();
                case "2" -> updateBook();
                case "3" -> deleteBook();
                case "4" -> viewAllBooks();
                case "5" -> searchBooks();
                case "6" -> viewIssuedBooks();
                case "7" -> viewOverdueBooks();
                case "8" -> viewAllMembers();
                case "9" -> viewAllTransactions();
                case "0" -> {
                    System.out.println("Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addBook() {
        try {
            System.out.println("\n=== ADD NEW BOOK ===");
            System.out.print("Title: ");
            String title = scanner.nextLine();
            
            System.out.print("Author: ");
            String author = scanner.nextLine();
            
            System.out.print("Publisher: ");
            String publisher = scanner.nextLine();
            
            System.out.print("Category: ");
            String category = scanner.nextLine();
            
            System.out.print("ISBN (10 or 13 digits): ");
            String isbn = scanner.nextLine();
            
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            
            String bookId = bookService.addBook(title, author, publisher, 
                    category, isbn, quantity);
            System.out.println("\n✓ Book added successfully! Book ID: " + bookId);
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid quantity format");
        }
    }
    
    private void updateBook() {
        try {
            System.out.println("\n=== UPDATE BOOK ===");
            System.out.print("Enter Book ID: ");
            String bookId = scanner.nextLine();
            
            Book book = bookService.getBookById(bookId);
            System.out.println("\nCurrent Details:");
            System.out.println(book.getDetailedInfo());
            
            System.out.println("Enter new values (press Enter to skip):");
            
            System.out.print("Title [" + book.getTitle() + "]: ");
            String title = scanner.nextLine();
            
            System.out.print("Author [" + book.getAuthor() + "]: ");
            String author = scanner.nextLine();
            
            System.out.print("Publisher [" + book.getPublisher() + "]: ");
            String publisher = scanner.nextLine();
            
            System.out.print("Category [" + book.getCategory() + "]: ");
            String category = scanner.nextLine();
            
            System.out.print("Quantity [" + book.getTotalQuantity() + "]: ");
            String qtyStr = scanner.nextLine();
            int quantity = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
            
            bookService.updateBook(bookId, title, author, publisher, category, quantity);
            System.out.println("\n✓ Book updated successfully!");
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid input format");
        }
    }
    
    private void deleteBook() {
        try {
            System.out.println("\n=== DELETE BOOK ===");
            System.out.print("Enter Book ID: ");
            String bookId = scanner.nextLine();
            
            Book book = bookService.getBookById(bookId);
            System.out.println("\n" + book.getDetailedInfo());
            
            System.out.print("Are you sure you want to delete this book? (yes/no): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                bookService.deleteBook(bookId);
                System.out.println("\n✓ Book deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private void viewAllBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        List<Book> books = bookService.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("\nTotal books: " + books.size());
    }
    
    private void searchBooks() {
        System.out.println("\n=== SEARCH BOOKS ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by Category");
        System.out.print("Choose search type: ");
        
        String choice = scanner.nextLine();
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        List<Book> results = switch (choice) {
            case "1" -> bookService.searchByTitle(searchTerm);
            case "2" -> bookService.searchByAuthor(searchTerm);
            case "3" -> bookService.searchByCategory(searchTerm);
            default -> {
                System.out.println("Invalid choice");
                yield List.of();
            }
        };
        
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\nSearch Results:");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }
    
    private void viewIssuedBooks() {
        System.out.println("\n=== ISSUED BOOKS ===");
        List<BookTransaction> transactions = transactionService.getAllActiveTransactions();
        
        if (transactions.isEmpty()) {
            System.out.println("No books currently issued.");
            return;
        }
        
        for (BookTransaction txn : transactions) {
            System.out.println(txn);
        }
    }
    
    private void viewOverdueBooks() {
        System.out.println("\n=== OVERDUE BOOKS ===");
        List<BookTransaction> overdueTransactions = transactionService.getOverdueTransactions();
        
        if (overdueTransactions.isEmpty()) {
            System.out.println("No overdue books.");
            return;
        }
        
        for (BookTransaction txn : overdueTransactions) {
            System.out.println(txn.getDetailedInfo());
        }
    }
    
    private void viewAllMembers() {
        System.out.println("\n=== ALL MEMBERS ===");
        List<User> members = userService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        
        for (User member : members) {
            System.out.println(member);
        }
    }
    
    private void viewAllTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        List<BookTransaction> transactions = transactionService.getAllTransactions();
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        for (BookTransaction txn : transactions) {
            System.out.println(txn);
        }
    }
}