package librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * BookTransaction represents a book issue/return transaction
 */
public class BookTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String bookId;
    private String userId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;
    private double fine;
    
    private static final int LOAN_PERIOD_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;
    
    public BookTransaction() {}
    
    public BookTransaction(String transactionId, String bookId, String userId) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.userId = userId;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS);
        this.status = TransactionStatus.ISSUED;
        this.fine = 0.0;
    }
    
    /**
     * Calculate fine for late return
     */
    public void calculateFine() {
        if (returnDate != null && returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            fine = daysLate * FINE_PER_DAY;
        }
    }
    
    /**
     * Mark book as returned
     */
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = TransactionStatus.RETURNED;
        calculateFine();
    }
    
    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    
    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
    
    public boolean isOverdue() {
        return status == TransactionStatus.ISSUED && LocalDate.now().isAfter(dueDate);
    }
    
    @Override
    public String toString() {
        return String.format("Transaction ID: %s | Book: %s | User: %s | Status: %s | Fine: ₹%.2f",
                transactionId, bookId, userId, status, fine);
    }
    
    public String getDetailedInfo() {
        return String.format("""
                Transaction ID: %s
                Book ID: %s
                User ID: %s
                Issue Date: %s
                Due Date: %s
                Return Date: %s
                Status: %s
                Fine: ₹%.2f
                """, transactionId, bookId, userId, issueDate, dueDate, 
                returnDate != null ? returnDate : "Not Returned", status, fine);
    }
}


