package DAOImpl;

import model.Alumno;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoTXTDAOImpl {

    private final String archivo = "alumnos.txt";

    public void guardar(Alumno a) throws Exception {
        if (consultar(a.getDni()) != null)
            throw new Exception("Alumno ya existe");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(a.getDni() + ";" + a.getApellido() + ";" + a.getNombre() + ";false\n");
        }
    }

    public void modificar(Alumno a) throws Exception {
        List<Alumno> lista = obtenerTodos(true);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Alumno alumno : lista) {
                if (alumno.getDni().equals(a.getDni())) {
                    alumno.setApellido(a.getApellido());
                    alumno.setNombre(a.getNombre());
                }
                bw.write(alumno.getDni() + ";" + alumno.getApellido() + ";" + alumno.getNombre() + ";" + alumno.isEliminado() + "\n");
            }
        }
    }

    public void eliminar(String dni) throws Exception {
        List<Alumno> lista = obtenerTodos(true);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Alumno alumno : lista) {
                if (alumno.getDni().equals(dni)) {
                    alumno.setEliminado(true);
                }
                bw.write(alumno.getDni() + ";" + alumno.getApellido() + ";" + alumno.getNombre() + ";" + alumno.isEliminado() + "\n");
            }
        }
    }

    public Alumno consultar(String dni) throws Exception {
        List<Alumno> lista = obtenerTodos(true);
        for (Alumno a : lista) {
            if (a.getDni().equals(dni)) {
                return a;
            }
        }
        return null;
    }

    public List<Alumno> obtenerTodos(boolean incluirEliminados) throws Exception {
        List<Alumno> lista = new ArrayList<>();
        File f = new File(archivo);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    boolean eliminado = Boolean.parseBoolean(partes[3]);
                    if (!eliminado || incluirEliminados) {
                        lista.add(new Alumno(partes[0], partes[1], partes[2], eliminado));
                    }
                }
            }
        }
        return lista;
    }
}
