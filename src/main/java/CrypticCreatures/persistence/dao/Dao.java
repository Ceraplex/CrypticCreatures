package CrypticCreatures.persistence.dao;
import java.util.Collection;
import java.util.Optional;


// DAO overview see: https://www.baeldung.com/java-dao-pattern
public interface Dao<T> {

    // READ
    Optional<T> get(int id);

    Collection<T> getAll();

    // CREATE
    boolean save(T t);

    // UPDATE
    boolean update(T t);

    // DELETE
    boolean delete(T t);
}
