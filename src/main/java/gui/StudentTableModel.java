package gui;


import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Student;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alepr
 */
public class StudentTableModel extends AbstractTableModel {
    private final String[] headers = {"DNI", "Nombre", "Apellido", "Estado"};
    private List<Student> students;
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
    @Override
    public int getRowCount() {
        if (students == null) {
            return 0;
        }
        return students.size();
    }
    
    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Object getValueAt(int fila, int col) {
        Student alumno = students.get(fila);
        switch (col) {
            case 0 -> {
                return alumno.getDni();
            }
            case 1 -> {
                return alumno.getName();
            }
            case 2 -> {
                return alumno.getLastName();
            }
            case 3 -> {
                if (alumno.isDeleted()) {
                    return "Bochado";
                }
                return "Activo";
            }
        }
        return null;
    }
}
