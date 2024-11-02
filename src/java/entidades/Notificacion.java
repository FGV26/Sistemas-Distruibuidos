
package entidades;

import java.util.Date;

public class Notificacion {
    private int idNotificacion;
    private int idPedido;
    private String tipo;
    private String estadoPedido;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private String estado;
    private Date fechaEnvio;

    // Constructor vacío
    public Notificacion() {
    }

    // Constructor con parámetros
    public Notificacion(int idNotificacion, int idPedido, String tipo, String estadoPedido, String destinatario, String asunto, String mensaje, String estado, Date fechaEnvio) {
        this.idNotificacion = idNotificacion;
        this.idPedido = idPedido;
        this.tipo = tipo;
        this.estadoPedido = estadoPedido;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y Setters
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
