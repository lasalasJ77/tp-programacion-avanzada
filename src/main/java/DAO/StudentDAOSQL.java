/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Exceptions.DAOException;
import Exceptions.StudentExistsException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;

public class StudentDAOSQL extends GenericDAO<Student, Integer> {
    
    private Connection connection;
    private PreparedStatement insertPS;
    private PreparedStatement updatePS;
    private PreparedStatement readPS;
    private PreparedStatement findAllPS;
    private PreparedStatement deletePS;
    
    public StudentDAOSQL(String host, String port, String user, String password) throws DAOException {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/alumnos";
            // jdbc:mysql://localhost:3306/universidad?user=root
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al conectarse a la BD ("+ex.getMessage()+")");
        }
        
        String insertSQL = "INSERT INTO alumnos\n" +
                            "(dni, nombre, apellido, email, direccion, telefono)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?);";
        try {
            insertPS = connection.prepareStatement(insertSQL);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al crear SQL INSERT ("+ex.getMessage()+")");
        }
        
        String updateSQL = "UPDATE alumnos\n" +
                            "SET nombre = ?, apellido = ?, email = ?, direccion = ?, telefono = ?\n" +
                            "WHERE dni = ?;";
        try {
            updatePS = connection.prepareStatement(updateSQL);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al actualizar SQL INSERT ("+ex.getMessage()+")");
        }
        
        String deleteSQL = "UPDATE alumnos\n" +
                            "SET eliminado = ?\n" +
                            "WHERE dni = ?;";
        try {
            deletePS = connection.prepareStatement(deleteSQL);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al eliminar SQL INSERT ("+ex.getMessage()+")");
        }

        String readSQL = "SELECT * FROM alumnos where dni = ?";
        String findAllSQL = "SELECT * FROM alumnos";
        try {
            readPS = connection.prepareStatement(readSQL);
            findAllPS = connection.prepareStatement(findAllSQL);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al crear SQL SELECT ("+ex.getMessage()+")");
        }
    }
    
    @Override
    public void delete(Integer dni) throws DAOException {
        try {
            int index = 1;
            deletePS.setInt(index++, 1);
            deletePS.setInt(index++, dni);
            deletePS.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al eliminar ("+ex.getMessage()+")");
        }
    }
    
    @Override
    public void create(Student student) throws DAOException, StudentExistsException {
        try {
            if (exist(student.getDni())) {
                throw new StudentExistsException("El alumno con el DNI " + student.getDni().toString() + " ya se encuentra cargado");
            }
            int index = 1;
            insertPS.setInt(index++, student.getDni());
            insertPS.setString(index++, student.getName());
            insertPS.setString(index++, student.getLastName());
            insertPS.setString(index++, student.getEmail());
            insertPS.setString(index++, student.getAddress());
            insertPS.setString(index++, student.getPhone());
            insertPS.executeUpdate();
        } catch (SQLException | StudentExistsException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            if (ex instanceof SQLException) {
                throw new DAOException("Error al insertar ("+ex.getMessage()+")");
            } else {
                throw new StudentExistsException(ex.getMessage());
            }
        }
    }
    
    @Override
    public void update(Student student) throws DAOException {
        try {
            int index = 1;
            updatePS.setString(index++, student.getName());
            updatePS.setString(index++, student.getLastName());
            updatePS.setString(index++, student.getEmail());
            updatePS.setString(index++, student.getAddress());
            updatePS.setString(index++, student.getPhone());
            updatePS.setInt(index++, student.getDni());
            updatePS.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al actualizar ("+ex.getMessage()+")");
        }
    }
    
    @Override
    public Student read(Integer dni) throws DAOException {
        try {
            readPS.setInt(1, dni);
            ResultSet resultSet = readPS.executeQuery();
            if (resultSet.next()) {
                return getStudentFromDB(resultSet);
            }
        // TODO: Agregar excepciones
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al leer ("+ex.getMessage()+")");
        }
        
        return null;
    }
    
    // TODO: Agregar excepciones
    // , NombreVacioException, NombreNullException, DniPersonaException
    private Student getStudentFromDB(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setDni(resultSet.getInt("dni"));
        student.setName(resultSet.getString("nombre"));
        student.setLastName(resultSet.getString("apellido"));
        student.setAddress(resultSet.getString("direccion"));
        student.setEmail(resultSet.getString("email"));
        student.setPhone(resultSet.getString("telefono"));
        student.setDeleted(resultSet.getBoolean("eliminado"));
        return student;
    }
    
    @Override
    public List<Student> findAll(boolean includeDeleted) throws DAOException {
        List<Student> alumnos = new ArrayList<>();
        
        try {
            ResultSet resultSet = findAllPS.executeQuery();
            while (resultSet.next()) {
                Student studentFromDB = getStudentFromDB(resultSet);
                if (includeDeleted || !studentFromDB.isDeleted()) {
                    alumnos.add(studentFromDB);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al leer ("+ex.getMessage()+")");
        }
        
        return alumnos;
    }
    
    @Override
    public boolean exist(Integer id) throws DAOException {
        try {
            Student student = read(id);
            return student != null;
        } catch (DAOException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al validar si existe el DNI " + id.toString() + " ("+ex.getMessage()+")");
        }
    }
    
    @Override
    public void closeConnection() throws DAOException {
        try {
            if (connection!=null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}