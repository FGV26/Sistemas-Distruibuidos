// Código actual de PedidoDAO ya satisface los requisitos
// No se necesitan cambios en el código
package dao;

import entidades.Pedido;
import entidades.DetallePedido;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String SQL_SELECT = "SELECT Id_Pedido, Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado FROM pedidos";
    private static final String SQL_SELECT_DETALLE = "SELECT Id_Detalle, Id_Pedido, Id_Producto, Cantidad, Precio, Total FROM detalle_pedido WHERE Id_Pedido = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE Id_Pedido = ?";
    private static final String SQL_INSERT_DETAIL = "INSERT INTO detalle_pedido (Id_Pedido, Id_Producto, Cantidad, Precio, Total) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_PEDIDO = "INSERT INTO pedidos (idCliente, idEmpleado, fechaPedido, subtotal, total, estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_DETALLE = "INSERT INTO detalle_pedido (idPedido, idProducto, cantidad, precio, total) VALUES (?, ?, ?, ?, ?)";

    // Listar todos los pedidos
    public List<Pedido> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(rs);
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return pedidos;
    }

    // Obtener detalles del pedido por ID
    public List<DetallePedido> obtenerDetallesPorIdPedido(int idPedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DetallePedido> detalles = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_DETALLE);
            stmt.setInt(1, idPedido);
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
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(rs);
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return detalles;
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
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Eliminar pedido por ID
    public int eliminar(int idPedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, idPedido);
            rows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException e) {
                e.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Insertar detalle de pedido
    public boolean insertarDetalle(DetallePedido detalle) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_DETAIL);
            stmt.setInt(1, detalle.getIdPedido());
            stmt.setInt(2, detalle.getIdProducto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecio());
            stmt.setDouble(5, detalle.getTotal());

            int rows = stmt.executeUpdate();
            success = (rows > 0);
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
        return success;
    }

    // Método para insertar un nuevo pedido y devolver el ID generado
    public int insertarPedido(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int idPedido = -1;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdEmpleado());
            stmt.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime()));
            stmt.setDouble(4, pedido.getSubTotal());
            stmt.setDouble(5, pedido.getTotal());
            stmt.setString(6, pedido.getEstado());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPedido = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return idPedido;
    }

    // Método para insertar un detalle de pedido
    public boolean insertarDetallePedido(List<DetallePedido> detalles) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_DETAIL);

            for (DetallePedido detalle : detalles) {
                stmt.setInt(1, detalle.getIdPedido());
                stmt.setInt(2, detalle.getIdProducto());
                stmt.setInt(3, detalle.getCantidad());
                stmt.setDouble(4, detalle.getPrecio());
                stmt.setDouble(5, detalle.getTotal());
                stmt.addBatch();
            }

            int[] rowsAffected = stmt.executeBatch();
            success = rowsAffected.length == detalles.size();
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
        return success;
    }

}
