package librarymanagement.dao;

import librarymanagement.model.BookTransaction;
import librarymanagement.model.TransactionStatus;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;



/**
 * Implementation of TransactionDAO using file-based storage
 */
public class TransactionDAOImpl implements TransactionDAO {
    
    private static final String TRANSACTION_FILE = "transactions.dat";
    private Map<String, BookTransaction> transactionMap;
    
    public TransactionDAOImpl() {
        transactionMap = new HashMap<>();
        loadFromFile();
    }
    
    @Override
    public void save(BookTransaction transaction) {
        transactionMap.put(transaction.getTransactionId(), transaction);
        saveToFile();
    }
    
    @Override
    public BookTransaction findById(String id) {
        return transactionMap.get(id);
    }
    
    @Override
    public List<BookTransaction> findAll() {
        return new ArrayList<>(transactionMap.values());
    }
    
    @Override
    public void update(BookTransaction transaction) {
        if (transactionMap.containsKey(transaction.getTransactionId())) {
            transactionMap.put(transaction.getTransactionId(), transaction);
            saveToFile();
        }
    }
    
    @Override
    public void delete(String id) {
        transactionMap.remove(id);
        saveToFile();
    }
    
    @Override
    public List<BookTransaction> findByUserId(String userId) {
        return transactionMap.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookTransaction> findByBookId(String bookId) {
        return transactionMap.values().stream()
                .filter(t -> t.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookTransaction> findActiveTransactions() {
        return transactionMap.values().stream()
                .filter(t -> t.getStatus() == TransactionStatus.ISSUED)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookTransaction> findOverdueTransactions() {
        return transactionMap.values().stream()
                .filter(BookTransaction::isOverdue)
                .collect(Collectors.toList());
    }
    
    /**
     * Save transactions to file using serialization
     */
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TRANSACTION_FILE))) {
            oos.writeObject(transactionMap);
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
    
    /**
     * Load transactions from file
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TRANSACTION_FILE))) {
            transactionMap = (Map<String, BookTransaction>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            transactionMap = new HashMap<>();
        }
    }
}

