package dao;

import entidades.Pedido;
import entidades.DetallePedido;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String SQL_SELECT = "SELECT Id_Pedido, Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado FROM pedidos";
    private static final String SQL_SELECT_DETALLE = "SELECT Id_Pedido, Id_Producto, Cantidad, Precio, Total FROM detalle_pedido WHERE Id_Pedido = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE Id_Pedido = ?";

    // Método para listar todos los pedidos
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("Id_Pedido"));
                pedido.setIdCliente(rs.getInt("Id_Cliente"));
                pedido.setIdEmpleado(rs.getInt("Id_Empleado"));
                pedido.setFechaPedido(rs.getDate("Fecha_Pedido"));
                pedido.setSubTotal(rs.getDouble("SubTotal"));
                pedido.setTotal(rs.getDouble("Total"));
                pedido.setEstado(rs.getString("Estado"));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return pedidos;
    }

    // Método para obtener detalles del pedido por ID
    public List<DetallePedido> obtenerDetallesPorIdPedido(int idPedido) {
        List<DetallePedido> detalles = new ArrayList<>();
        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_DETALLE)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdDetalle(rs.getInt("Id_Detalle"));  // Asegúrate de que Id_Detalle esté en SQL_SELECT_DETALLE si se necesita
                    detalle.setIdPedido(rs.getInt("Id_Pedido"));
                    detalle.setIdProducto(rs.getInt("Id_Producto"));
                    detalle.setCantidad(rs.getInt("Cantidad"));
                    detalle.setPrecio(rs.getDouble("Precio"));
                    detalle.setTotal(rs.getDouble("Total"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return detalles;
    }

    // Método para insertar un nuevo pedido
    public int insertar(Pedido pedido) {
        int rows = 0;
        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, pedido.getIdCliente());
            ps.setInt(2, pedido.getIdEmpleado());
            ps.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime()));
            ps.setDouble(4, pedido.getSubTotal());
            ps.setDouble(5, pedido.getTotal());
            ps.setString(6, pedido.getEstado());
            rows = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return rows;
    }

    // Método para eliminar un pedido por ID
    public int eliminar(int idPedido) {
        int rows = 0;
        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, idPedido);
            rows = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
        return rows;
    }
}
