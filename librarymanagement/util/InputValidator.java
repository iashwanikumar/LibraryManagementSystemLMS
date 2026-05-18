package librarymanagement.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class InputValidator {
    
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern ISBN_PATTERN = 
            Pattern.compile("^(?:\\d{10}|\\d{13})$");
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate ISBN format (10 or 13 digits)
     */
    public static boolean isValidISBN(String isbn) {
        return isbn != null && ISBN_PATTERN.matcher(isbn).matches();
    }
    
    /**
     * Validate string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(int num) {
        return num > 0;
    }
    
    /**
     * Validate password strength (minimum 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Sanitize input string
     */
    public static String sanitize(String input) {
        return input != null ? input.trim() : "";
    }
}