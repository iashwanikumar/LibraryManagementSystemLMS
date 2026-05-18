package librarymanagement.dao;

import librarymanagement.model.*;
import java.util.List;



/**
 * Transaction-specific DAO interface
 */
public interface TransactionDAO extends GenericDAO<BookTransaction> {
    List<BookTransaction> findByUserId(String userId);
    List<BookTransaction> findByBookId(String bookId);
    List<BookTransaction> findActiveTransactions();
    List<BookTransaction> findOverdueTransactions();
}