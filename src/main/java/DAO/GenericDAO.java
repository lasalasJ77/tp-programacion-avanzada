package DAO;

import java.util.List;

public interface GenericDAO<T> {
    void guardar(T obj) throws Exception;
    void modificar(T obj) throws Exception;
    void eliminar(String id) throws Exception;
    T consultar(String id) throws Exception;
    List<T> obtenerTodos(boolean incluirEliminados) throws Exception;
}