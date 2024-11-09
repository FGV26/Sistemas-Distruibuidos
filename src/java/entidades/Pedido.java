
package entidades;

import java.util.Date;

public class Pedido {
    private int idPedido;
    private String codPedido; // Nuevo campo para el código del pedido
    private int idCliente;
    private int idEmpleado;
    private Date fechaPedido;
    private double subTotal;
    private double total;
    private String estado;

    // Constructor vacío
    public Pedido() {}

    // Constructor completo con todos los parámetros
    public Pedido(int idPedido, String codPedido, int idCliente, int idEmpleado, Date fechaPedido, double subTotal, double total, String estado) {
        this.idPedido = idPedido;
        this.codPedido = codPedido;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaPedido = fechaPedido;
        this.subTotal = subTotal;
        this.total = total;
        this.estado = estado;
    }

    // Constructor simplificado (para `crearPedido` en el controlador)
    public Pedido(int idCliente, double subTotal, double total) {
        this.idCliente = idCliente;
        this.subTotal = subTotal;
        this.total = total;
        this.fechaPedido = new Date();  // Fecha actual por defecto
        this.estado = "Proceso";        // Estado predeterminado
    }

    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
