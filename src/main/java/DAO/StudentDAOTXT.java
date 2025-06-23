/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Exceptions.DAOException;
import Exceptions.StudentExistsException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Student;

public class StudentDAOTXT extends GenericDAO<Student, Integer> {
    private final RandomAccessFile raf;
            
    public StudentDAOTXT(String fullpath) throws DAOException {
        try {
            raf = new RandomAccessFile(fullpath, "rws");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error de E/S ("+ex.getMessage()+")");
        }
    }
    
    
    @Override
    public void create(Student student) throws DAOException, StudentExistsException {
        try {
            if (exist(student.getDni())) {
                throw new StudentExistsException("El alumno con el DNI " + student.getDni() + " ya existe");
            }
            // Me posiciono al final del TXT
            raf.seek(raf.length());
            
            raf.writeBytes(student.toString()+System.lineSeparator());
        } catch (StudentExistsException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new StudentExistsException(ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error de escritura ("+ex.getMessage()+")");
        }
    }

    @Override
    public Student read(Integer dni) throws DAOException {
        try {
            raf.seek(0); // Me posocionó al incio
            String linea;
            String[] fieldsStudent;
            while ((linea = raf.readLine())!=null) {
                fieldsStudent = linea.split(String.valueOf(Student.DELIM));
                if (Integer.valueOf(fieldsStudent[0]).equals(dni)) {
                    return Student.str2Student(fieldsStudent);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error de E/S ("+ex.getMessage()+")");
        }
        
        return null;
    }

    @Override
    public void update(Student student) throws DAOException {
        try {
            File tempFile = File.createTempFile("student_temp", ".txt");
            
            boolean updated;
            try (BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFile))) {
                raf.seek(0);
                String line;
                updated = false;
                while ((line = raf.readLine()) != null) {
                    String[] fields = line.split(String.valueOf(Student.DELIM));
                    int currentDni = Integer.parseInt(fields[0]);
                    
                    if (currentDni == student.getDni()) {
                        tempWriter.write(student.toString());
                        updated = true;
                    } else {
                        tempWriter.write(line);
                    }
                    tempWriter.newLine();
                }
            }
            
            if (!updated) {
                tempFile.delete();
                throw new DAOException("No se encontró el alumno a actualizar.");
            }
            
            raf.setLength(0);
            try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile))) {
                String newLine;
                
                while((newLine = tempReader.readLine()) != null) {
                    raf.writeBytes(newLine + System.lineSeparator());
                }
            }
            tempFile.delete();
        } catch (IOException ex) {
            throw new DAOException("Error al actualizar alumno " + ex.getMessage());
        }
    }

    @Override
    public void delete(Integer dni) throws DAOException {
        Student student2Read = read(dni);
        if (student2Read != null) {
            student2Read.setDeleted(true);
            update(student2Read);
        }
        else {
            throw new DAOException("El alumno a eliminar no existe");
        }
    }

    @Override
    public List<Student> findAll(boolean includeDeleted) throws DAOException {
            List<Student> students = new ArrayList<>();
        try {
            raf.seek(0); // Me posocionó al incio
            String linea;
            String[] fieldsStudent;
            while ((linea = raf.readLine())!=null) {
                fieldsStudent = linea.split(String.valueOf(Student.DELIM));
                Student student = Student.str2Student(fieldsStudent);
                if (includeDeleted || !student.isDeleted())
                students.add(student);
            }
        } catch (IOException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error de E/S ("+ex.getMessage()+")");
        }

        return students;
    }

    @Override
    public boolean exist(Integer dni) throws DAOException {
        try {
            raf.seek(0); // Me posocionó al incio
            String line;
            String[] fieldsStudent;
            while ((line = raf.readLine())!=null) {
                fieldsStudent = line.split(String.valueOf(Student.DELIM));
                if (Integer.valueOf(fieldsStudent[0]).equals(dni)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error de E/S ("+ex.getMessage()+")");
        }
        
        return false;
    }

    @Override
    public void closeConnection() throws DAOException {
        if (raf!=null) {
            try {
                raf.close();
            } catch (IOException ex) {
                Logger.getLogger(StudentDAOTXT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
