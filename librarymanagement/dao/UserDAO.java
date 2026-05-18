package librarymanagement.dao;

import librarymanagement.model.*;
import java.util.List;



/**
 * User-specific DAO interface
 */
public interface UserDAO extends GenericDAO<User> {
    User findByEmail(String email);
    User authenticate(String email, String password);
}