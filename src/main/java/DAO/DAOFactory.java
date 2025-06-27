package DAO;


import Exceptions.DAOFactoryException;
import Exceptions.DAOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class DAOFactory {
    private static DAOFactory instance;
    public static final String TYPE_DAO = "TYPE_DAO";
    public static final String TYPE_DAO_TXT = "TYPE_DAO_TXT";
    public static final String TYPE_DAO_SQL = "TYPE_DAO_SQL";
    
    public static final String FILENAME = "FILENAME";
    public static final String HOST = "HOST";
    public static final String PORT = "PORT";
    public static final String USER = "USER";
    public static final String PWD = "PWD";
    
    private DAOFactory() {}
    
    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    public static GenericDAO createDao(Map<String, Object> config) throws DAOFactoryException {
        try {
            String type = (String) config.get(TYPE_DAO);
            
            switch (type) {
                case TYPE_DAO_TXT -> {
                    String filename = (String) config.get(FILENAME);
                    return new StudentDAOTXT(filename);
                }
                case TYPE_DAO_SQL -> {
                    String host = (String) config.get(HOST);
                    String port = (String) config.get(PORT);
                    String user = (String) config.get(USER);
                    String pwd = (String) config.get(PWD);
                    return new StudentDAOSQL(host, port, user, pwd);
                }
                default -> throw new DAOFactoryException("El repositorio deseado no fue implementado");
            }
        } catch (DAOException ex) {
            Logger.getLogger(DAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOFactoryException(ex.getMessage());
        }
    }
}
