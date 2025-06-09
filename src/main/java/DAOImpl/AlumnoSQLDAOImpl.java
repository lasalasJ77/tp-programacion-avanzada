package DAOImpl;

import DAO.Alumno.AlumnoDAO;
import model.Alumno;
import utils.ConexionSQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoSQLDAOImpl implements AlumnoDAO {
    public void guardar(Alumno a) throws Exception {
        if (consultar(a.getDni()) != null){
            throw new Exception("Alumno ya existe");
        }
        PreparedStatement ps = ConexionSQL.getConexion().prepareStatement("INSERT INTO alumnos VALUES (?, ?, ?, ?, ?, ?, false)");
        ps.setString(1, a.getDni());
        ps.setString(2, a.getApellido());
        ps.setString(3, a.getNombre());
        ps.setString(4, a.getEmail());
        ps.setString(5, a.getTelefono());
        ps.setString(6, a.getDireccion());
        ps.executeUpdate();
    }

    public void modificar(Alumno a) throws Exception {
        PreparedStatement ps = ConexionSQL.getConexion().prepareStatement("UPDATE alumnos SET apellido=?, nombre=?, telefono=?, email=?, direccion=? WHERE dni=?");
        ps.setString(1, a.getApellido());
        ps.setString(2, a.getNombre());
        ps.setString(3, a.getTelefono());
        ps.setString(4, a.getEmail());
        ps.setString(5, a.getDireccion());
        ps.setString(6, a.getDni());
        ps.executeUpdate();
    }

    public void eliminar(String dni) throws Exception {
        PreparedStatement ps = ConexionSQL.getConexion().prepareStatement("UPDATE alumnos SET eliminado=true WHERE dni=?");
        ps.setString(1, dni);
        ps.executeUpdate();
    }

    public Alumno consultar(String dni) throws Exception {
        PreparedStatement ps = ConexionSQL.getConexion().prepareStatement("SELECT * FROM alumnos WHERE dni=?");
        ps.setString(1, dni);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Alumno(rs.getString("dni"), rs.getString("apellido"), rs.getString("nombre"), rs.getString("email"), rs.getString("telefono"), rs.getString("direccion"), rs.getBoolean("eliminado"));
        }
        return null;
    }

    public List<Alumno> obtenerTodos(boolean incluirEliminados) throws Exception {
        String sql = "SELECT * FROM alumnos";
        if (!incluirEliminados) sql += " WHERE eliminado=false";
        Statement st = ConexionSQL.getConexion().createStatement();
        ResultSet rs = st.executeQuery(sql);

        List<Alumno> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(new Alumno(rs.getString("dni"), rs.getString("apellido"), rs.getString("nombre"),rs.getString("email"), rs.getString("telefono"), rs.getString("direccion"), rs.getBoolean("eliminado")));
        }
        return lista;
    }
}
