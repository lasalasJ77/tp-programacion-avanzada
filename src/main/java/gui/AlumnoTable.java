package gui;

import model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class AlumnoTable extends JTable {

    private final DefaultTableModel modelo;
    private List<Alumno> alumnos = new ArrayList<>();

    public AlumnoTable() {
        modelo = new DefaultTableModel(new String[]{"DNI", "Apellido", "Nombre", "Email", "Telefono", "Direccion", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(modelo);
        setMostrarColumnas(false);
    }

    public void setData(List<Alumno> lista) {
        this.alumnos = lista;
        modelo.setRowCount(0);
        for (Alumno a : lista) {
            modelo.addRow(new Object[]{
                    a.getDni(),
                    a.getApellido(),
                    a.getNombre(),
                    a.getEmail(),
                    a.getTelefono(),
                    a.getDireccion(),
                    a.isEliminado(),
            });
        }
    }

    public Alumno getAlumnoFila(int fila) {
        if(fila >= 0 && fila < alumnos.size()) {
            return alumnos.get(fila);
        }
        return null;
    }

    public void setMostrarColumnas(boolean mostrarColumnas) {
        int[] columnas = {3, 4, 5};

        for (int col : columnas) {
            if (mostrarColumnas) {
                getColumnModel().getColumn(col).setMinWidth(100);
                getColumnModel().getColumn(col).setMaxWidth(100);
                getColumnModel().getColumn(col).setWidth(100);
            } else {
                getColumnModel().getColumn(col).setMinWidth(0);
                getColumnModel().getColumn(col).setMaxWidth(0);
                getColumnModel().getColumn(col).setWidth(0);
            }
        }
    }
}
