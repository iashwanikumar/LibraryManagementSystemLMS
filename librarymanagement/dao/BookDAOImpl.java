package librarymanagement.dao;

import librarymanagement.model.Book;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of BookDAO using file-based storage
 * Demonstrates Encapsulation and File Handling
 */
public class BookDAOImpl implements BookDAO {
    
    private static final String BOOK_FILE = "books.dat";
    private Map<String, Book> bookMap;
    
    public BookDAOImpl() {
        bookMap = new HashMap<>();
        loadFromFile();
    }
    
    @Override
    public void save(Book book) {
        bookMap.put(book.getBookId(), book);
        saveToFile();
    }
    
    @Override
    public Book findById(String id) {
        return bookMap.get(id);
    }
    
    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookMap.values());
    }
    
    @Override
    public void update(Book book) {
        if (bookMap.containsKey(book.getBookId())) {
            bookMap.put(book.getBookId(), book);
            saveToFile();
        }
    }
    
    @Override
    public void delete(String id) {
        bookMap.remove(id);
        saveToFile();
    }
    
    @Override
    public List<Book> searchByTitle(String title) {
        return bookMap.values().stream()
                .filter(book -> book.getTitle().toLowerCase()
                        .contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> searchByAuthor(String author) {
        return bookMap.values().stream()
                .filter(book -> book.getAuthor().toLowerCase()
                        .contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> searchByCategory(String category) {
        return bookMap.values().stream()
                .filter(book -> book.getCategory().toLowerCase()
                        .contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Save books to file using serialization
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(BOOK_FILE))) {
            oos.writeObject(bookMap);
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
    
    /**
     * Load books from file
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(BOOK_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(BOOK_FILE))) {
            bookMap = (Map<String, Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading books: " + e.getMessage());
            bookMap = new HashMap<>();
        }
    }
}