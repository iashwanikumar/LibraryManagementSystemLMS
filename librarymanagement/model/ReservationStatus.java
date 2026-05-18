package librarymanagement.model;



/**
 * Reservation status enum
 */
enum ReservationStatus {
    PENDING,    // Waiting in queue
    READY,      // Book available, user notified
    FULFILLED,  // Book issued to user
    EXPIRED,    // Didn't collect within hold period
    CANCELLED   // User cancelled
}