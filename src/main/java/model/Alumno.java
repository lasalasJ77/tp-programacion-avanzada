package model;

public class Alumno {

    private String dni;
    private String apellido;
    private String nombre;
    private boolean eliminado;

    public Alumno(String dni, String apellido, String nombre, Boolean eliminado) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.eliminado = eliminado;
    }

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public boolean isEliminado() {
        return eliminado;
    }
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

}
