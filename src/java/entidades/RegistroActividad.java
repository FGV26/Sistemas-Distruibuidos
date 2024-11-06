package entidades;

import java.time.LocalDateTime;

public class RegistroActividad {
    private int id;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private int idUsuario; // Agregar este campo para la clave foránea

    // Constructor vacío
    public RegistroActividad() {
    }

    // Constructor completo
    public RegistroActividad(int id, String tipo, String descripcion, LocalDateTime fecha, int idUsuario) {
        this.id = id;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
    }

    // Constructor sin ID (para insertar nuevos registros)
    public RegistroActividad(String tipo, String descripcion, int idUsuario) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
