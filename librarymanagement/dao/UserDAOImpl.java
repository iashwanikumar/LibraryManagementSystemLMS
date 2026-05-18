package librarymanagement.dao;

import librarymanagement.model.User;
import java.io.*;
import java.util.*;

/**
 * Implementation of UserDAO using file-based storage
 */
public class UserDAOImpl implements UserDAO {
    
    private static final String USER_FILE = "users.dat";
    private Map<String, User> userMap;
    
    public UserDAOImpl() {
        userMap = new HashMap<>();
        loadFromFile();
    }
    
    @Override
    public void save(User user) {
        userMap.put(user.getUserId(), user);
        saveToFile();
    }
    
    @Override
    public User findById(String id) {
        return userMap.get(id);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }
    
    @Override
    public void update(User user) {
        if (userMap.containsKey(user.getUserId())) {
            userMap.put(user.getUserId(), user);
            saveToFile();
        }
    }
    
    @Override
    public void delete(String id) {
        userMap.remove(id);
        saveToFile();
    }
    
    @Override
    public User findByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public User authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * Save users to file using serialization
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(USER_FILE))) {
            oos.writeObject(userMap);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Load users from file
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(USER_FILE))) {
            userMap = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            userMap = new HashMap<>();
        }
    }
}