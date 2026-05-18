package librarymanagement.service;

import librarymanagement.dao.BookDAO;
import librarymanagement.dao.BookDAOImpl;
import librarymanagement.model.Book;
import librarymanagement.util.*;
import java.util.List;
import java.util.UUID;

/**
 * Service class for Book-related business logic
 * Demonstrates Service Layer pattern and Exception Handling
 */
public class BookService {
    
    private final BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAOImpl();
    }
    
    /**
     * Add a new book to the library
     */
    public String addBook(String title, String author, String publisher, 
                         String category, String isbn, int quantity) 
            throws LibraryException {
        
        // Validate inputs
        if (!InputValidator.isNotEmpty(title)) {
            throw new LibraryException("Title cannot be empty");
        }
        if (!InputValidator.isNotEmpty(author)) {
            throw new LibraryException("Author cannot be empty");
        }
        if (!InputValidator.isValidISBN(isbn)) {
            throw new LibraryException("Invalid ISBN format. Must be 10 or 13 digits.");
        }
        if (!InputValidator.isPositiveInteger(quantity)) {
            throw new LibraryException("Quantity must be positive");
        }
        
        // Generate unique book ID
        String bookId = "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Book book = new Book(bookId, title, author, publisher, category, isbn, quantity);
        bookDAO.save(book);
        
        return bookId;
    }
    
    /**
     * Update existing book details
     */
    public void updateBook(String bookId, String title, String author, 
                          String publisher, String category, int quantity) 
            throws LibraryException {
        
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        
        if (InputValidator.isNotEmpty(title)) book.setTitle(title);
        if (InputValidator.isNotEmpty(author)) book.setAuthor(author);
        if (InputValidator.isNotEmpty(publisher)) book.setPublisher(publisher);
        if (InputValidator.isNotEmpty(category)) book.setCategory(category);
        if (quantity > 0) {
            int diff = quantity - book.getTotalQuantity();
            book.setTotalQuantity(quantity);
            book.setAvailableQuantity(book.getAvailableQuantity() + diff);
        }
        
        bookDAO.update(book);
    }
    
    /**
     * Delete a book
     */
    public void deleteBook(String bookId) throws LibraryException {
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        
        // Check if book is currently issued
        if (book.getAvailableQuantity() < book.getTotalQuantity()) {
            throw new LibraryException("Cannot delete book. Some copies are currently issued.");
        }
        
        bookDAO.delete(bookId);
    }
    
    /**
     * Get book by ID
     */
    public Book getBookById(String bookId) throws LibraryException {
        Book book = bookDAO.findById(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        return book;
    }
    
    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }
    
    /**
     * Search books by title
     */
    public List<Book> searchByTitle(String title) {
        return bookDAO.searchByTitle(title);
    }
    
    /**
     * Search books by author
     */
    public List<Book> searchByAuthor(String author) {
        return bookDAO.searchByAuthor(author);
    }
    
    /**
     * Search books by category
     */
    public List<Book> searchByCategory(String category) {
        return bookDAO.searchByCategory(category);
    }
    
    /**
     * Get available books only
     */
    public List<Book> getAvailableBooks() {
        return bookDAO.findAll().stream()
                .filter(Book::isAvailable)
                .toList();
    }
    
    /**
     * Update book availability when issued
     */
    public void decreaseAvailability(String bookId) throws LibraryException {
        Book book = getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(bookId);
        }
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookDAO.update(book);
    }
    
    /**
     * Update book availability when returned
     */
    public void increaseAvailability(String bookId) throws LibraryException {
        Book book = getBookById(bookId);
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookDAO.update(book);
    }
}