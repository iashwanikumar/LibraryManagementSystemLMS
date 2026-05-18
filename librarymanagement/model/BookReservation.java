package librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;



/**
 * BookReservation for hold/queue system
 */
public class BookReservation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String reservationId;
    private String bookId;
    private String userId;
    private LocalDateTime reservationDate;
    private LocalDate expiryDate;
    private ReservationStatus status;
    private int queuePosition;
    
    private static final int HOLD_DAYS = 3; // Days to hold book after available
    
    public BookReservation() {}
    
    public BookReservation(String reservationId, String bookId, String userId, int queuePosition) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.userId = userId;
        this.reservationDate = LocalDateTime.now();
        this.expiryDate = LocalDate.now().plusDays(HOLD_DAYS);
        this.status = ReservationStatus.PENDING;
        this.queuePosition = queuePosition;
    }
    
    public void markAsReady() {
        this.status = ReservationStatus.READY;
        this.expiryDate = LocalDate.now().plusDays(HOLD_DAYS);
    }
    
    public void markAsFulfilled() {
        this.status = ReservationStatus.FULFILLED;
    }
    
    public void markAsExpired() {
        this.status = ReservationStatus.EXPIRED;
    }
    
    public void markAsCancelled() {
        this.status = ReservationStatus.CANCELLED;
    }
    
    public boolean isExpired() {
        return status == ReservationStatus.READY && 
               LocalDate.now().isAfter(expiryDate);
    }
    
    // Getters and Setters
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { 
        this.reservationDate = reservationDate; 
    }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    
    public int getQueuePosition() { return queuePosition; }
    public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }
    
    @Override
    public String toString() {
        return String.format("Reservation: %s | Book: %s | Status: %s | Position: %d",
                reservationId, bookId, status, queuePosition);
    }
}
