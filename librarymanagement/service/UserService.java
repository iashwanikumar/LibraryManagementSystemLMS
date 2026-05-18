package librarymanagement.service;

import librarymanagement.dao.UserDAO;
import librarymanagement.dao.UserDAOImpl;
import librarymanagement.model.*;
import librarymanagement.util.*;
import java.util.List;
import java.util.UUID;

/**
 * Service class for User-related business logic
 */
public class UserService {
    
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }
    
    /**
     * Register a new member
     */
    public String registerMember(String name, String email, String password) 
            throws LibraryException {
        
        // Validate inputs
        if (!InputValidator.isNotEmpty(name)) {
            throw new LibraryException("Name cannot be empty");
        }
        if (!InputValidator.isValidEmail(email)) {
            throw new LibraryException("Invalid email format");
        }
        if (!InputValidator.isValidPassword(password)) {
            throw new LibraryException("Password must be at least 6 characters");
        }
        
        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            throw new LibraryException("Email already registered");
        }
        
        // Generate unique user ID
        String userId = "M" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Member member = new Member(userId, name, email, password);
        userDAO.save(member);
        
        return userId;
    }
    
    /**
     * Add a new admin (restricted operation)
     */
    public String addAdmin(String name, String email, String password) 
            throws LibraryException {
        
        if (!InputValidator.isValidEmail(email)) {
            throw new LibraryException("Invalid email format");
        }
        if (!InputValidator.isValidPassword(password)) {
            throw new LibraryException("Password must be at least 6 characters");
        }
        
        if (userDAO.findByEmail(email) != null) {
            throw new LibraryException("Email already registered");
        }
        
        String userId = "A" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Admin admin = new Admin(userId, name, email, password);
        userDAO.save(admin);
        
        return userId;
    }
    
    /**
     * Authenticate user login
     */
    public User login(String email, String password) throws LibraryException {
        User user = userDAO.authenticate(email, password);
        if (user == null) {
            throw new InvalidCredentialsException();
        }
        return user;
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(String userId) throws LibraryException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    /**
     * Get all members (excluding admins)
     */
    public List<User> getAllMembers() {
        return userDAO.findAll().stream()
                .filter(user -> user.getRole() == UserRole.MEMBER)
                .toList();
    }
    
    /**
     * Update user information
     */
    public void updateUser(User user) {
        userDAO.update(user);
    }
    
    /**
     * Check if user can issue more books
     */
    public boolean canIssueBook(String userId) throws LibraryException {
        User user = getUserById(userId);
        if (user instanceof Member member) {
            return member.canIssueBook();
        }
        return false;
    }
}