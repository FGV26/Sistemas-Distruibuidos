package entidades;

import java.util.Date;

public class Pedido {

    private int idPedido;
    private int idCliente;
    private int idEmpleado;
    private Date fechaPedido;
    private double subTotal;
    private double total;
    private String estado;
    private String fecha_modificacion;
    private int idDespachador;
    private String codPedido;

    public Pedido() {
    }

    public Pedido(int idPedido, int idCliente, int idEmpleado, Date fechaPedido, double subTotal, double total, String estado, String fecha_modificacion, int idDespachador, String codPedido) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaPedido = fechaPedido;
        this.subTotal = subTotal;
        this.total = total;
        this.estado = estado;
        this.fecha_modificacion = fecha_modificacion;
        this.idDespachador = idDespachador;
        this.codPedido = codPedido;
    }

    public Pedido(int idCliente, int idEmpleado, Date fechaPedido, double subTotal, double total, String estado, String fecha_modificacion, int idDespachador, String codPedido) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaPedido = fechaPedido;
        this.subTotal = subTotal;
        this.total = total;
        this.estado = estado;
        this.fecha_modificacion = fecha_modificacion;
        this.idDespachador = idDespachador;
        this.codPedido = codPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    public String getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(String fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }

    public int getIdDespachador() {
        return idDespachador;
    }

    public void setIdDespachador(int idDespachador) {
        this.idDespachador = idDespachador;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }
    
    
    
}
