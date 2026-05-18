package librarymanagement.model;


/**
 * Notification types
 */
enum NotificationType {
    DUE_REMINDER,        // Book due soon
    OVERDUE_ALERT,       // Book overdue
    RESERVATION_READY,   // Reserved book available
    FINE_NOTICE,         // Fine pending
    SYSTEM_ALERT,        // System messages
    BOOK_RETURNED        // Confirmation
}