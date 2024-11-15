package entidades;

import java.math.BigDecimal;
import java.util.Date;

public class Pedido {

    private int idPedido;
    private int idCliente;
    private int idEmpleado;
    private Date fechaPedido;
    private BigDecimal subTotal;
    private BigDecimal total;
    private String estado;
    private Date fechaModificacion; 
    private Integer idDespachador;
    private String codPedido;
    private String nombreCliente; 

    public Pedido() {
    }

    public Pedido(int idPedido, int idCliente, int idEmpleado, Date fechaPedido, BigDecimal subTotal, BigDecimal total, String estado, Date fechaModificacion, Integer idDespachador, String codPedido) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaPedido = fechaPedido;
        this.subTotal = subTotal;
        this.total = total;
        this.estado = estado;
        this.fechaModificacion = fechaModificacion;
        this.idDespachador = idDespachador;
        this.codPedido = codPedido;
    }

    public Pedido(int idCliente, int idEmpleado, Date fechaPedido, BigDecimal subTotal, BigDecimal total, String estado, Date fechaModificacion, Integer idDespachador, String codPedido) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fechaPedido = fechaPedido;
        this.subTotal = subTotal;
        this.total = total;
        this.estado = estado;
        this.fechaModificacion = fechaModificacion;
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getIdDespachador() {
        return idDespachador;
    }

    public void setIdDespachador(Integer idDespachador) {
        this.idDespachador = idDespachador;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
