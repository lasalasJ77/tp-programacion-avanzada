package gui;

import DAOImpl.AlumnoSQLDAOImpl;
import DAOImpl.AlumnoTXTDAOImpl;
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
    private final JTextField txtEmail = new JTextField(10);
    private final JTextField txtTelefono = new JTextField(10);
    private final JTextField txtDireccion = new JTextField(10);
    private final JCheckBox chkMostrarEliminados = new JCheckBox("Ver eliminados");

    private String dniActualAlumno;

    private final AlumnoTable tabla = new AlumnoTable();
    private AlumnoService service;
    private AlumnoSQLDAOImpl mysqlDAO;
    private AlumnoTXTDAOImpl txtDAO;

    private final JComboBox<String> comboAlmacenarDatos = new JComboBox<>(new String[] {"MySQL", "TXT"});

    public AlumnoForm() {
        setTitle("Gestión de Alumnos");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.mysqlDAO = new AlumnoSQLDAOImpl();
        this.txtDAO = new AlumnoTXTDAOImpl();
        this.service = new AlumnoService(mysqlDAO);

        initUI();
        cargarTabla();
    }

    private void initUI() {
        JPanel panelForm = new JPanel(new GridLayout(4, 4, 5, 5));
        panelForm.add(new JLabel("DNI:"));
        panelForm.add(txtDni);
        panelForm.add(new JLabel("Apellido:"));
        panelForm.add(txtApellido);
        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Email:"));
        panelForm.add(txtEmail);
        panelForm.add(new JLabel("Telefono:"));
        panelForm.add(txtTelefono);
        panelForm.add(new JLabel("Direccion:"));
        panelForm.add(txtDireccion);
        panelForm.add(new JLabel("Almacenar datos:"));
        panelForm.add(comboAlmacenarDatos);

        configurarComboAlmacenarDatos();


        JButton btnGuardar = new JButton("Guardar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnConsultar = new JButton("Consultar");

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnConsultar.addActionListener(e -> consultar());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnConsultar);
        panelBotones.add(chkMostrarEliminados);

        chkMostrarEliminados.addActionListener(e -> cargarTabla());

        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    dniActualAlumno = tabla.getValueAt(fila,0).toString();
                    txtDni.setText(dniActualAlumno);
                    txtApellido.setText(tabla.getValueAt(fila, 1).toString());
                    txtNombre.setText(tabla.getValueAt(fila, 2).toString());
                    txtEmail.setText(tabla.getValueAt(fila, 3).toString());
                    txtTelefono.setText(tabla.getValueAt(fila, 4).toString());
                    txtDireccion.setText(tabla.getValueAt(fila, 5).toString());
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
            Alumno a = new Alumno(txtDni.getText(), txtApellido.getText(), txtNombre.getText(), txtEmail.getText(), txtTelefono.getText(), txtDireccion.getText(), false);
            service.registrarAlumno(a);
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void modificar() {
        try {
            String dniNuevoAlumno = txtDni.getText();
            if(!dniActualAlumno.equals(dniNuevoAlumno)) {
                mostrarError("No se puede modificar el DNI");
                limpiar();
                return;
            }
            Alumno a = new Alumno(txtDni.getText(), txtApellido.getText(), txtNombre.getText(), txtEmail.getText(), txtTelefono.getText(), txtDireccion.getText(), false);
            service.modificarAlumno(a);
            JOptionPane.showMessageDialog(this, "Alumno modificado exitosamente.", "Acción exitosa", JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Alumno eliminado exitosamente.", "Acción exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
                cargarTabla();
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
        }
    }

    private void consultar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un alumno para consultar.");
            return;
        }
        Alumno alumnoFila = tabla.getAlumnoFila(fila);

        tabla.setMostrarColumnas(true);

        String info = String.format("""
        DNI: %s
        Apellido: %s
        Nombre: %s
        Dirección: %s
        Teléfono: %s
        Email: %s
        Estado: %s
        """,
                alumnoFila.getDni(),
                alumnoFila.getApellido(),
                alumnoFila.getNombre(),
                alumnoFila.getDireccion(),
                alumnoFila.getTelefono(),
                alumnoFila.getEmail(),
                alumnoFila.isEliminado() ? "Eliminado" : "Activo"
        );

        JOptionPane.showMessageDialog(this, info, "Ficha Completa del Alumno", JOptionPane.INFORMATION_MESSAGE);

        tabla.setMostrarColumnas(false);
        limpiar();
    }

    private void limpiar() {
        txtDni.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void configurarComboAlmacenarDatos() {
        comboAlmacenarDatos.addActionListener(e -> {
            String fuente = (String) comboAlmacenarDatos.getSelectedItem();

            if ("MySQL".equals(fuente)) {
                this.service.setDao(mysqlDAO);
            } else if ("TXT".equals(fuente)) {
                this.service.setDao(txtDAO);
            }
            cargarTabla();
        });
    }

}
