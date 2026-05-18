package librarymanagement.util;



/**
 * Exception for book not available
 */
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String bookId) {
        super("Book is not available for issue: " + bookId);
    }
}