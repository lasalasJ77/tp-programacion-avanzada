package gui;

import model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AlumnoTable extends JTable {

    private final DefaultTableModel modelo;

    public AlumnoTable() {
        modelo = new DefaultTableModel(new String[]{"DNI", "Apellido", "Nombre", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(modelo);
    }

    public void setData(List<Alumno> lista) {
        modelo.setRowCount(0);
        for (Alumno a : lista) {
            modelo.addRow(new Object[]{
                    a.getDni(),
                    a.getApellido(),
                    a.getNombre(),
                    a.isEliminado(),
            });
        }
    }
}
