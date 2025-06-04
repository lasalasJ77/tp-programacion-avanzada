package gui;

import DAOImpl.AlumnoSQLDAOImpl;
import model.Alumno;
import service.alumno.AlumnoService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AlumnoForm extends JFrame {

    private final JTextField txtDni = new JTextField(10);
    private final JTextField txtApellido = new JTextField(10);
    private final JTextField txtNombre = new JTextField(10);
    private final JCheckBox chkMostrarEliminados = new JCheckBox("Ver eliminados");

    private final AlumnoTable tabla = new AlumnoTable();
    private final AlumnoService service;

    public AlumnoForm() {
        setTitle("Gestión de Alumnos");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cambia entre TXT o SQL según quieras:
        // this.service = new AlumnoService(new AlumnoTXTDAOImpl());
        this.service = new AlumnoService(new AlumnoSQLDAOImpl());

        initUI();
        cargarTabla();
    }

    private void initUI() {
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 5, 5));
        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellido);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(chkMostrarEliminados);

        chkMostrarEliminados.addActionListener(e -> cargarTabla());

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    txtDni.setText(tabla.getValueAt(fila, 0).toString());
                    txtApellido.setText(tabla.getValueAt(fila, 1).toString());
                    txtNombre.setText(tabla.getValueAt(fila, 2).toString());
                }
            }
        });
    }

    private void cargarTabla() {
        try {
            List<Alumno> lista = service.listar(chkMostrarEliminados.isSelected());
            tabla.setData(lista);
        } catch (Exception e) {
            mostrarError("Error al cargar alumnos: " + e.getMessage());
        }
    }

    private void guardar() {
        try {
            Alumno a = new Alumno(txtDni.getText(), txtApellido.getText(), txtNombre.getText(), false);
            service.registrarAlumno(a);
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificar() {
        try {
            Alumno a = new Alumno(txtDni.getText(), txtApellido.getText(), txtNombre.getText(), false);
            service.modificarAlumno(a);
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un alumno para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al alumno?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String dni = tabla.getValueAt(fila, 0).toString();
                service.eliminarAlumno(dni);
                limpiar();
                cargarTabla();
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    private void limpiar() {
        txtDni.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
