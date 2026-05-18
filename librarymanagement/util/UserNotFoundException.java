package librarymanagement.util;



/**
 * Exception for user not found
 */
public class UserNotFoundException extends LibraryException {
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }
}