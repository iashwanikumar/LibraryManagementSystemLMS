package librarymanagement.service;

import librarymanagement.dao.TransactionDAO;
import librarymanagement.dao.TransactionDAOImpl;
import librarymanagement.model.*;
import librarymanagement.util.*;
import java.util.List;
import java.util.UUID;

/**
 * Service class for Transaction-related business logic
 * Demonstrates coordination between multiple services
 */
public class TransactionService {
    
    private final TransactionDAO transactionDAO;
    private final BookService bookService;
    private final UserService userService;
    
    public TransactionService(BookService bookService, UserService userService) {
        this.transactionDAO = new TransactionDAOImpl();
        this.bookService = bookService;
        this.userService = userService;
    }
    
    /**
     * Issue a book to a member
     */
    public String issueBook(String bookId, String userId) throws LibraryException {
        
        // Validate book exists and is available
        Book book = bookService.getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }
        
        // Validate user exists and can issue books
        User user = userService.getUserById(userId);
        if (!(user instanceof Member member)) {
            throw new LibraryException("Only members can issue books");
        }
        
        if (!member.canIssueBook()) {
            throw new BookLimitExceededException();
        }
        
        // Check if user already has this book
        List<BookTransaction> activeTransactions = getActiveTransactionsByUser(userId);
        boolean alreadyHasBook = activeTransactions.stream()
                .anyMatch(t -> t.getBookId().equals(bookId));
        
        if (alreadyHasBook) {
            throw new LibraryException("You have already issued this book");
        }
        
        // Create transaction
        String transactionId = "TXN" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase();
        BookTransaction transaction = new BookTransaction(transactionId, bookId, userId);
        
        // Update book availability
        bookService.decreaseAvailability(bookId);
        
        // Update member's issued books count
        member.incrementBooksIssued();
        userService.updateUser(member);
        
        // Save transaction
        transactionDAO.save(transaction);
        
        return transactionId;
    }
    
    /**
     * Return a book
     */
    public double returnBook(String transactionId) throws LibraryException {
        
        BookTransaction transaction = transactionDAO.findById(transactionId);
        if (transaction == null) {
            throw new LibraryException("Transaction not found: " + transactionId);
        }
        
        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new LibraryException("Book already returned");
        }
        
        // Mark as returned and calculate fine
        transaction.returnBook();
        
        // Update book availability
        bookService.increaseAvailability(transaction.getBookId());
        
        // Update member's issued books count
        User user = userService.getUserById(transaction.getUserId());
        if (user instanceof Member member) {
            member.decrementBooksIssued();
            userService.updateUser(member);
        }
        
        // Update transaction
        transactionDAO.update(transaction);
        
        return transaction.getFine();
    }
    
    /**
     * Get transaction by ID
     */
    public BookTransaction getTransactionById(String transactionId) 
            throws LibraryException {
        BookTransaction transaction = transactionDAO.findById(transactionId);
        if (transaction == null) {
            throw new LibraryException("Transaction not found: " + transactionId);
        }
        return transaction;
    }
    
    /**
     * Get all transactions
     */
    public List<BookTransaction> getAllTransactions() {
        return transactionDAO.findAll();
    }
    
    /**
     * Get active transactions for a user
     */
    public List<BookTransaction> getActiveTransactionsByUser(String userId) {
        return transactionDAO.findByUserId(userId).stream()
                .filter(t -> t.getStatus() == TransactionStatus.ISSUED)
                .toList();
    }
    
    /**
     * Get transaction history for a user
     */
    public List<BookTransaction> getTransactionHistoryByUser(String userId) {
        return transactionDAO.findByUserId(userId);
    }
    
    /**
     * Get all active transactions
     */
    public List<BookTransaction> getAllActiveTransactions() {
        return transactionDAO.findActiveTransactions();
    }
    
    /**
     * Get overdue transactions
     */
    public List<BookTransaction> getOverdueTransactions() {
        return transactionDAO.findOverdueTransactions();
    }
    
    /**
     * Get transactions for a specific book
     */
    public List<BookTransaction> getTransactionsByBook(String bookId) {
        return transactionDAO.findByBookId(bookId);
    }
}