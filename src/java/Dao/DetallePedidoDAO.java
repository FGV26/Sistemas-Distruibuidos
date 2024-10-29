
package dao;

import entidades.DetallePedido;
import conexion.conexion;  // Ajuste del paquete de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO {

    private static final String SQL_SELECT = "SELECT * FROM detalle_pedido";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM detalle_pedido WHERE Id_Detalle = ?";
    private static final String SQL_INSERT = "INSERT INTO detalle_pedido (Id_Pedido, Id_Producto, Cantidad, Precio, Total) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE detalle_pedido SET Id_Pedido = ?, Id_Producto = ?, Cantidad = ?, Precio = ?, Total = ? WHERE Id_Detalle = ?";
    private static final String SQL_DELETE = "DELETE FROM detalle_pedido WHERE Id_Detalle = ?";

    // Listar todos los detalles de pedidos
    public List<DetallePedido> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DetallePedido> detalles = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setIdDetalle(rs.getInt("Id_Detalle"));
                detalle.setIdPedido(rs.getInt("Id_Pedido"));
                detalle.setIdProducto(rs.getInt("Id_Producto"));
                detalle.setCantidad(rs.getInt("Cantidad"));
                detalle.setPrecio(rs.getDouble("Precio"));
                detalle.setTotal(rs.getDouble("Total"));
                detalles.add(detalle);
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
        return detalles;
    }

    // Buscar detalle por ID
    public DetallePedido buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DetallePedido detalle = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                detalle = new DetallePedido();
                detalle.setIdDetalle(rs.getInt("Id_Detalle"));
                detalle.setIdPedido(rs.getInt("Id_Pedido"));
                detalle.setIdProducto(rs.getInt("Id_Producto"));
                detalle.setCantidad(rs.getInt("Cantidad"));
                detalle.setPrecio(rs.getDouble("Precio"));
                detalle.setTotal(rs.getDouble("Total"));
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
        return detalle;
    }

    // Insertar un nuevo detalle de pedido
    public int insertar(DetallePedido detalle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, detalle.getIdPedido());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio());
            stmt.setDouble(5, detalle.getTotal());
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

    // Actualizar un detalle de pedido
    public int actualizar(DetallePedido detalle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setInt(1, detalle.getIdPedido());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio());
            stmt.setDouble(5, detalle.getTotal());
            stmt.setInt(6, detalle.getIdDetalle());
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

    // Eliminar un detalle de pedido
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
