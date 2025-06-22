package DAO;

import java.util.List;

/**
 * @param <T>
 * @param <K>
 */
public abstract class GenericDAO<T, K> {
    public abstract void create(T obj) throws DAOException;
    public abstract void update(T obj) throws DAOException;
    public abstract void delete(Integer dni) throws DAOException;
    public abstract T read(Integer dni) throws DAOException;
    public abstract List<T> findAll(boolean includeDeleted) throws DAOException;
    public abstract boolean exist(K id) throws DAOException;
    
    public abstract void closeConnection() throws DAOException;
}