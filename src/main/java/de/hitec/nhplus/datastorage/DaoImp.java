package de.hitec.nhplus.datastorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>DaoImp</code> implements the Dao Interface and its defined functions.
 */
public abstract class DaoImp<T> implements Dao<T> {
    protected Connection connection;

    public DaoImp(Connection connection) {
        this.connection = connection;
    }

    /**
     * Implements the create function.
     */
    @Override
    public void create(T t) throws SQLException {
        getCreateStatement(t).executeUpdate();
    }

    /**
     * Implements the read function.
     */
    @Override
    public T read(long key) throws SQLException {
        T object = null;
        ResultSet result = getReadByIDStatement(key).executeQuery();
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * Implements the readAll function.
     */
    @Override
    public List<T> readAll() throws SQLException {
        return getListFromResultSet(getReadAllStatement().executeQuery());
    }

    /**
     * Implements the update function.
     */
    @Override
    public void update(T t) throws SQLException {
        getUpdateStatement(t).executeUpdate();
    }

    /**
     * Implements the deleteById function.
     */
    @Override
    public void deleteById(long key) throws SQLException {
        getDeleteStatement(key).executeUpdate();
    }

    /**
     * Gets an instance from a resultset.
     * @param set The given resultset.
     */
    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    /**
     * Gets a list from a resultset.
     * @param set The given resultset.
     */
    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    /**
     * Gets a create statement.
     * @param t The type of Dao.
     */
    protected abstract PreparedStatement getCreateStatement(T t);

    /**
     * Gets a create stamement.
     * @param key identifier
     */
    protected abstract PreparedStatement getReadByIDStatement(long key);

    /**
     * Gets an instance from a resultset.
     */
    protected abstract PreparedStatement getReadAllStatement();

    /**
     * Gets an update statement.
     * @param t The type of Dao.
     */
    protected abstract PreparedStatement getUpdateStatement(T t);

    /**
     * Gets a delete statement.
     * @param key identifier
     */
    protected abstract PreparedStatement getDeleteStatement(long key);
}
