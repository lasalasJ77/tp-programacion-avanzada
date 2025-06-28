/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Exceptions.DAOException;
import Exceptions.StudentExistsException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
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
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al conectarse a la BD ("+ex.getMessage()+")");
        }
        
        String insertSQL = "INSERT INTO alumnos\n" +
                            "(dni, nombre, apellido, email, direccion, telefono, fechaIngreso, promedio)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            insertPS = connection.prepareStatement(insertSQL);
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al crear SQL INSERT ("+ex.getMessage()+")");
        }
        
        String updateSQL = "UPDATE alumnos\n" +
                            "SET nombre = ?, apellido = ?, email = ?, direccion = ?, telefono = ?, fechaIngreso = ?, promedio = ?\n" +
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
    public void changeState(Integer dni, Boolean deleted) throws DAOException {
        try {
            int index = 1;
            deletePS.setInt(index++, deleted ? 1 : 0);
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
            
            LocalDate date = student.getDateAdmission();
            insertPS.setInt(index++, student.getDni());
            insertPS.setString(index++, student.getName());
            insertPS.setString(index++, student.getLastName());
            insertPS.setString(index++, student.getEmail());
            insertPS.setString(index++, student.getAddress());
            insertPS.setString(index++, student.getPhone());
            insertPS.setDate(index++, new Date(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
            insertPS.setDouble(index++, student.getAverage());
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
            LocalDate date = student.getDateAdmission();

            updatePS.setString(index++, student.getName());
            updatePS.setString(index++, student.getLastName());
            updatePS.setString(index++, student.getEmail());
            updatePS.setString(index++, student.getAddress());
            updatePS.setString(index++, student.getPhone());
            
            updatePS.setDate(index++, new Date(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
            updatePS.setDouble(index++, student.getAverage());
            
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
        } catch (SQLException ex) {
            Logger.getLogger(StudentDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error al leer ("+ex.getMessage()+")");
        }
        
        return null;
    }

    private Student getStudentFromDB(ResultSet resultSet) throws SQLException {
        Date date = resultSet.getDate("fechaIngreso");
        
        Student student = new Student();
        student.setDni(resultSet.getInt("dni"));
        student.setName(resultSet.getString("nombre"));
        student.setLastName(resultSet.getString("apellido"));
        student.setAddress(resultSet.getString("direccion"));
        student.setEmail(resultSet.getString("email"));
        student.setPhone(resultSet.getString("telefono"));
        student.setDeleted(resultSet.getBoolean("eliminado"));
        student.setDateAdmission(LocalDate.of(date.getYear(), date.getMonth(), date.getDate()));
        student.setAverage(resultSet.getDouble("promedio"));
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