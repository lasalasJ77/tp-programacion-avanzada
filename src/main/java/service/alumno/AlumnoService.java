package service.alumno;

import DAO.Alumno.AlumnoDAO;
import model.Alumno;
import utils.Regex;

import java.util.List;

public class AlumnoService {
    private AlumnoDAO dao;

    public AlumnoService(AlumnoDAO dao) {
        this.dao = dao;
    }

    public void setDao(AlumnoDAO dao) {
        this.dao = dao;
    }

    public void registrarAlumno(Alumno a) throws Exception {
        if (a.getDni().isEmpty() || a.getNombre().isEmpty() || a.getApellido().isEmpty()) {
            throw new Exception("Todos los campos son obligatorios");
        }
        Regex.validarAlumno(a);
        dao.guardar(a);
    }

    public void modificarAlumno(Alumno a) throws Exception {
        if (a.getDni() == null) throw new Exception("DNI obligatorio");
        Regex.validarAlumno(a);
        dao.modificar(a);
    }

    public void eliminarAlumno(String dni) throws Exception {
        dao.eliminar(dni);
    }

    public Alumno buscarAlumno(String dni) throws Exception {
        return dao.consultar(dni);
    }

    public List<Alumno> listar(boolean incluirEliminados) throws Exception {
        return dao.obtenerTodos(incluirEliminados);
    }
}