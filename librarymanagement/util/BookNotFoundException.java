package librarymanagement.util;



/**
 * Exception for book not found
 */
public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String bookId) {
        super("Book not found with ID: " + bookId);
    }
}