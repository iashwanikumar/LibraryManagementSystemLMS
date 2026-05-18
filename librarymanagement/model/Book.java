package librarymanagement.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Enhanced Book entity with additional features
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookId;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private String isbn;
    private int totalQuantity;
    private int availableQuantity;
    
    // New fields for enhanced features
    private String description;
    private String coverImagePath;
    private LocalDate addedDate;
    private String branchId;
    private boolean isActive;
    private int timesIssued; // For popularity tracking
    
    public Book() {}
    
    public Book(String bookId, String title, String author, String publisher, 
                String category, String isbn, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.isbn = isbn;
        this.totalQuantity = quantity;
        this.availableQuantity = quantity;
        this.addedDate = LocalDate.now();
        this.isActive = true;
        this.timesIssued = 0;
        this.description = "";
        this.coverImagePath = "default_cover.png";
    }
    
    public void incrementTimesIssued() {
        this.timesIssued++;
    }
    
    // Getters and Setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { 
        this.availableQuantity = availableQuantity; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCoverImagePath() { return coverImagePath; }
    public void setCoverImagePath(String coverImagePath) { 
        this.coverImagePath = coverImagePath; 
    }
    
    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }
    
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public int getTimesIssued() { return timesIssued; }
    public void setTimesIssued(int timesIssued) { this.timesIssued = timesIssued; }
    
    public boolean isAvailable() {
        return availableQuantity > 0 && isActive;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Title: %s | Author: %s | Available: %d/%d%s",
                bookId, title, author, availableQuantity, totalQuantity,
                isActive ? "" : " [INACTIVE]");
    }
    
    public String getDetailedInfo() {
        return String.format("""
                Book ID: %s
                Title: %s
                Author: %s
                Publisher: %s
                Category: %s
                ISBN: %s
                Description: %s
                Total Quantity: %d
                Available: %d
                Times Issued: %d
                Added Date: %s
                Status: %s
                """, bookId, title, author, publisher, category, isbn, 
                description, totalQuantity, availableQuantity, timesIssued,
                addedDate, isActive ? "Active" : "Inactive");
    }
}