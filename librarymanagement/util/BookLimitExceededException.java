package librarymanagement.util;





/**
 * Exception for book limit exceeded
 */
public class BookLimitExceededException extends LibraryException {
    public BookLimitExceededException() {
        super("Maximum book issue limit reached. Please return a book first.");
    }
}