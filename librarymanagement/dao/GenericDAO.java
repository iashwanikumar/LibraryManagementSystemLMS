
package librarymanagement.dao;

import librarymanagement.model.*;
import java.util.List;



/**
 * Generic DAO interface demonstrating Abstraction
 */
public interface GenericDAO<T> {
    void save(T entity);
    T findById(String id);
    List<T> findAll();
    void update(T entity);
    void delete(String id);
}