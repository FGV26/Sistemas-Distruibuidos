
package dao;

import entidades.Pedido;
import conexion.conexion;  // Ajuste del paquete de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String SQL_SELECT = "SELECT * FROM pedidos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM pedidos WHERE Id_Pedido = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE pedidos SET Id_Cliente = ?, Id_Empleado = ?, Fecha_Pedido = ?, SubTotal = ?, Total = ?, Estado = ? WHERE Id_Pedido = ?";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE Id_Pedido = ?";

    // Listar todos los pedidos
    public List<Pedido> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();  // Ajuste en la llamada a conexión
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
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
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);  // Ajuste del printStackTrace
        } finally {
            try {
                conexion.close(rs);
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return pedidos;
    }

    // Buscar pedido por ID
    public Pedido buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Pedido pedido = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("Id_Pedido"));
                pedido.setIdCliente(rs.getInt("Id_Cliente"));
                pedido.setIdEmpleado(rs.getInt("Id_Empleado"));
                pedido.setFechaPedido(rs.getDate("Fecha_Pedido"));
                pedido.setSubTotal(rs.getDouble("SubTotal"));
                pedido.setTotal(rs.getDouble("Total"));
                pedido.setEstado(rs.getString("Estado"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(rs);
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return pedido;
    }

    // Insertar un nuevo pedido
    public int insertar(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdEmpleado());
            stmt.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime()));
            stmt.setDouble(4, pedido.getSubTotal());
            stmt.setDouble(5, pedido.getTotal());
            stmt.setString(6, pedido.getEstado());
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Actualizar un pedido
    public int actualizar(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdEmpleado());
            stmt.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime()));
            stmt.setDouble(4, pedido.getSubTotal());
            stmt.setDouble(5, pedido.getTotal());
            stmt.setString(6, pedido.getEstado());
            stmt.setInt(7, pedido.getIdPedido());
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Eliminar un pedido
    public int eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows;
    }
}
