package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
    private static Connection conexion;

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion =
         DriverManager.getConnection("jdbc:mysql://localhost:3306/alumnos", "root", "");

        }
        return conexion;
    }
}
