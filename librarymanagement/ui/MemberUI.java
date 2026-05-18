package librarymanagement.ui;

import librarymanagement.model.*;
import librarymanagement.service.*;
import librarymanagement.util.LibraryException;
import java.util.List;
import java.util.Scanner;

/**
 * Member user interface
 */
public class MemberUI {
    
    private final Scanner scanner;
    private final BookService bookService;
    private final TransactionService transactionService;
    private final User currentUser;
    
    public MemberUI(Scanner scanner, User user, BookService bookService, 
                    TransactionService transactionService) {
        this.scanner = scanner;
        this.currentUser = user;
        this.bookService = bookService;
        this.transactionService = transactionService;
    }
    
    public void showMenu() {
        while (true) {
            currentUser.displayDashboard();
            System.out.println("\n1. View Available Books");
            System.out.println("2. Search Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. View My Issued Books");
            System.out.println("6. View Transaction History");
            System.out.println("0. Logout");
            System.out.print("\nEnter your choice: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1" -> viewAvailableBooks();
                case "2" -> searchBooks();
                case "3" -> issueBook();
                case "4" -> returnBook();
                case "5" -> viewMyIssuedBooks();
                case "6" -> viewTransactionHistory();
                case "0" -> {
                    System.out.println("Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void viewAvailableBooks() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        List<Book> books = bookService.getAvailableBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books available at the moment.");
            return;
        }
        
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("\nTotal available books: " + books.size());
    }
    
    private void searchBooks() {
        System.out.println("\n=== SEARCH BOOKS ===");
        System.out.println("1. Search by Book ID");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.print("Choose search type: ");
        
        String choice = scanner.nextLine();
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        try {
            switch (choice) {
                case "1" -> {
                    Book book = bookService.getBookById(searchTerm);
                    System.out.println("\n" + book.getDetailedInfo());
                }
                case "2" -> {
                    List<Book> results = bookService.searchByTitle(searchTerm);
                    displaySearchResults(results);
                }
                case "3" -> {
                    List<Book> results = bookService.searchByAuthor(searchTerm);
                    displaySearchResults(results);
                }
                default -> System.out.println("Invalid choice");
            }
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private void displaySearchResults(List<Book> results) {
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\nSearch Results:");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }
    
    private void issueBook() {
        try {
            System.out.println("\n=== ISSUE BOOK ===");
            
            // Check if member can issue more books
            Member member = (Member) currentUser;
            if (!member.canIssueBook()) {
                System.out.println("✗ You have reached the maximum book limit.");
                System.out.println("Please return a book before issuing a new one.");
                return;
            }
            
            System.out.print("Enter Book ID: ");
            String bookId = scanner.nextLine();
            
            // Display book details
            Book book = bookService.getBookById(bookId);
            System.out.println("\n" + book.getDetailedInfo());
            
            if (!book.isAvailable()) {
                System.out.println("✗ This book is not available for issue.");
                return;
            }
            
            System.out.print("\nConfirm issue? (yes/no): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                String transactionId = transactionService.issueBook(bookId, 
                        currentUser.getUserId());
                
                BookTransaction txn = transactionService.getTransactionById(transactionId);
                
                System.out.println("\n✓ Book issued successfully!");
                System.out.println("Transaction ID: " + transactionId);
                System.out.println("Due Date: " + txn.getDueDate());
                System.out.println("Please return the book by the due date to avoid fines.");
            } else {
                System.out.println("Issue cancelled.");
            }
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private void returnBook() {
        try {
            System.out.println("\n=== RETURN BOOK ===");
            
            // Show currently issued books
            List<BookTransaction> issuedBooks = transactionService
                    .getActiveTransactionsByUser(currentUser.getUserId());
            
            if (issuedBooks.isEmpty()) {
                System.out.println("You don't have any issued books.");
                return;
            }
            
            System.out.println("Your Issued Books:");
            for (BookTransaction txn : issuedBooks) {
                Book book = bookService.getBookById(txn.getBookId());
                System.out.printf("%s - %s (Due: %s)%n", 
                        txn.getTransactionId(), book.getTitle(), txn.getDueDate());
            }
            
            System.out.print("\nEnter Transaction ID: ");
            String transactionId = scanner.nextLine();
            
            BookTransaction txn = transactionService.getTransactionById(transactionId);
            Book book = bookService.getBookById(txn.getBookId());
            
            System.out.println("\nReturning: " + book.getTitle());
            
            if (txn.isOverdue()) {
                System.out.println("⚠ WARNING: This book is overdue!");
            }
            
            System.out.print("Confirm return? (yes/no): ");
            String confirm = scanner.nextLine();
            
            if (confirm.equalsIgnoreCase("yes")) {
                double fine = transactionService.returnBook(transactionId);
                
                System.out.println("\n✓ Book returned successfully!");
                if (fine > 0) {
                    System.out.printf("Fine Amount: ₹%.2f (Late by %d days)%n", 
                            fine, (int)(fine / 5));
                    System.out.println("Please pay the fine at the counter.");
                } else {
                    System.out.println("No fine. Thank you for returning on time!");
                }
            } else {
                System.out.println("Return cancelled.");
            }
            
        } catch (LibraryException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
    
    private void viewMyIssuedBooks() {
        System.out.println("\n=== MY ISSUED BOOKS ===");
        List<BookTransaction> transactions = transactionService
                .getActiveTransactionsByUser(currentUser.getUserId());
        
        if (transactions.isEmpty()) {
            System.out.println("You don't have any issued books.");
            return;
        }
        
        for (BookTransaction txn : transactions) {
            try {
                Book book = bookService.getBookById(txn.getBookId());
                System.out.printf("%nBook: %s%n", book.getTitle());
                System.out.printf("Transaction ID: %s%n", txn.getTransactionId());
                System.out.printf("Issue Date: %s%n", txn.getIssueDate());
                System.out.printf("Due Date: %s%n", txn.getDueDate());
                
                if (txn.isOverdue()) {
                    System.out.println("Status: ⚠ OVERDUE");
                } else {
                    System.out.println("Status: Active");
                }
                System.out.println("---");
            } catch (LibraryException e) {
                System.out.println("Error loading book details");
            }
        }
    }
    
    private void viewTransactionHistory() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        List<BookTransaction> transactions = transactionService
                .getTransactionHistoryByUser(currentUser.getUserId());
        
        if (transactions.isEmpty()) {
            System.out.println("No transaction history.");
            return;
        }
        
        for (BookTransaction txn : transactions) {
            try {
                Book book = bookService.getBookById(txn.getBookId());
                System.out.printf("%n%s - %s%n", txn.getTransactionId(), book.getTitle());
                System.out.printf("Issue Date: %s | Due Date: %s%n", 
                        txn.getIssueDate(), txn.getDueDate());
                System.out.printf("Status: %s", txn.getStatus());
                
                if (txn.getReturnDate() != null) {
                    System.out.printf(" | Returned: %s", txn.getReturnDate());
                }
                if (txn.getFine() > 0) {
                    System.out.printf(" | Fine: ₹%.2f", txn.getFine());
                }
                System.out.println("\n---");
            } catch (LibraryException e) {
                System.out.println("Error loading transaction details");
            }
        }
    }
}