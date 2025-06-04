package utils;

import java.io.File;

public class ConexionTXT {

    private static File archivo;

    public static File getArchivo() {
        if (archivo == null) {
            archivo = new File("alumnos.txt");
        }
        return archivo;
    }
}
