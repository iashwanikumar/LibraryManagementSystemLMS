
package librarymanagement.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced Member class with favorites and preferences
 * Demonstrates Inheritance
 */
public class Member extends User {
    private static final long serialVersionUID = 1L;
    
    private int booksIssued;
    private static final int MAX_BOOKS = 3;
    private List<String> favoriteBooks;
    private int renewalsUsed;
    private static final int MAX_RENEWALS = 2;
    private String preferredBranchId;
    
    public Member() {
        super();
        this.favoriteBooks = new ArrayList<>();
        this.renewalsUsed = 0;
    }
    
    public Member(String userId, String name, String email, String password) {
        super(userId, name, email, password, UserRole.MEMBER);
        this.booksIssued = 0;
        this.favoriteBooks = new ArrayList<>();
        this.renewalsUsed = 0;
    }
    
    public int getBooksIssued() { return booksIssued; }
    public void setBooksIssued(int booksIssued) { this.booksIssued = booksIssued; }
    
    public List<String> getFavoriteBooks() { return favoriteBooks; }
    public void setFavoriteBooks(List<String> favoriteBooks) { 
        this.favoriteBooks = favoriteBooks; 
    }
    
    public void addFavorite(String bookId) {
        if (!favoriteBooks.contains(bookId)) {
            favoriteBooks.add(bookId);
        }
    }
    
    public void removeFavorite(String bookId) {
        favoriteBooks.remove(bookId);
    }
    
    public boolean isFavorite(String bookId) {
        return favoriteBooks.contains(bookId);
    }
    
    public int getRenewalsUsed() { return renewalsUsed; }
    public void setRenewalsUsed(int renewalsUsed) { this.renewalsUsed = renewalsUsed; }
    
    public boolean canRenew() {
        return renewalsUsed < MAX_RENEWALS;
    }
    
    public void incrementRenewal() {
        if (canRenew()) {
            renewalsUsed++;
        }
    }
    
    public void resetRenewals() {
        renewalsUsed = 0;
    }
    
    public String getPreferredBranchId() { return preferredBranchId; }
    public void setPreferredBranchId(String preferredBranchId) { 
        this.preferredBranchId = preferredBranchId; 
    }
    
    public boolean canIssueBook() {
        return booksIssued < MAX_BOOKS && isActive;
    }
    
    public void incrementBooksIssued() {
        if (canIssueBook()) {
            booksIssued++;
        }
    }
    
    public void decrementBooksIssued() {
        if (booksIssued > 0) {
            booksIssued--;
        }
    }
    
    @Override
    public void displayDashboard() {
        System.out.println("\n=== MEMBER DASHBOARD ===");
        System.out.println("Welcome, " + name + "!");
        System.out.println("Books Issued: " + booksIssued + "/" + MAX_BOOKS);
        System.out.println("Renewals Used: " + renewalsUsed + "/" + MAX_RENEWALS);
        System.out.println("Favorite Books: " + favoriteBooks.size());
    }
}