package librarymanagement.dao;

import librarymanagement.model.*;
import java.util.List;



/**
 * Book-specific DAO interface
 */
public interface BookDAO extends GenericDAO<Book> {
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> searchByCategory(String category);
}