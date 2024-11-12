
package entidades;

import java.sql.Timestamp;

public class Cliente {

    private int idCliente;
    private String nombre;
    private String apellido;
    private String direccion;
    private String dni;
    private String telefono;
    private String email;
    private Timestamp fechaCreacion;
    private int idEmpleado;
    private String codCliente; // Código del cliente

    // Constructor vacío
    public Cliente() {
    }

    // Constructor completo
    public Cliente(int idCliente, String nombre, String apellido, String direccion, String dni, String telefono, 
                   String email, Timestamp fechaCreacion, int idEmpleado, String codCliente) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.fechaCreacion = fechaCreacion;
        this.idEmpleado = idEmpleado;
        this.codCliente = codCliente;
    }

    // Constructor para crear un nuevo cliente sin ID de cliente y sin codCliente
    public Cliente(String nombre, String apellido, String direccion, String dni, String telefono, String email, int idEmpleado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.idEmpleado = idEmpleado;
    }

    // Getters y Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }
}
