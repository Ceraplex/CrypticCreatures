package CrypticCreatures.persistence.dao;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of the Data-Access-Object Pattern
 *
 * @param <T>
 */
// DAO overview see: https://www.baeldung.com/java-dao-pattern
public interface Dao<T> {

    // READ
    Optional<T> get(int id);

    Collection<T> getAll();

    // CREATE
    boolean save(T t);

    // UPDATE
    boolean update(T t, String[] params);

    // DELETE
    boolean delete(T t);
}
