package utils;

import model.Alumno;

public class Regex {
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String REGEX_DNI = "^\\d{7,8}$";
    public static final String REGEX_TELEFONO = "^\\d{8,15}$";
    public static boolean esEmailValido(String email) {
        return email != null && email.matches(REGEX_EMAIL);
    }

    public static boolean esDniValido(String dni) {
        return dni != null && dni.matches(REGEX_DNI);
    }

    public static boolean esTelefonoValido(String telefono) {
        return telefono != null && telefono.matches(REGEX_TELEFONO);
    }
    public static void validarAlumno(Alumno alumno) throws Exception {
        if (!esEmailValido(alumno.getEmail())) {
            throw new Exception("Email inválido");
        }
        if (!esDniValido(alumno.getDni())) {
            throw new Exception("DNI inválido");
        }
        if (!esTelefonoValido(alumno.getTelefono())) {
            throw new Exception("Teléfono inválido");
        }
    }
}
