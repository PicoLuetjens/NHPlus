package de.hitec.nhplus.datastorage;

import java.sql.SQLException;
import java.util.List;

/**
 * The <code>Dao</code> is implemented by the DaoImo class.
 * @param <T> DaoType
 */
public interface Dao<T> {

    /**
     * defines a creation function.
     * @param t DaoType
     */
    void create(T t) throws SQLException;

    /**
     * defines a read function.
     * @param key indentifier
     */
    T read(long key) throws SQLException;

    /**
     * defines a readAll function.
     */
    List<T> readAll() throws SQLException;

    /**
     * defines an update function.
     * @param t DaoType
     */
    void update(T t) throws SQLException;

    /**
     * defines a deleteById function.
     * @param key identifier
     */
    void deleteById(long key) throws SQLException;
}
