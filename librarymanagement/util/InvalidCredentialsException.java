package librarymanagement.util;




/**
 * Exception for invalid credentials
 */
public class InvalidCredentialsException extends LibraryException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}