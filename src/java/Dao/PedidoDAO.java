package dao;

import entidades.Pedido;
import conexion.conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String SQL_SELECT = "SELECT * FROM pedidos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM pedidos WHERE idPedido = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (idCliente, idEmpleado, fechaPedido, subTotal, total, estado, fecha_modificacion, idDespachador, codPedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE pedidos SET estado = ? WHERE idPedido = ?";
    private static final String SQL_OBTENER_ULTIMO_ID = "SELECT MAX(idPedido) AS ultimoId FROM pedidos";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE idPedido = ?";

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
                Pedido pedido = mapResultSetToPedido(rs);
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return pedidos;
    }

    // Obtener pedido por ID
    public Pedido obtenerPedidoPorId(int id) {
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
                pedido = mapResultSetToPedido(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
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
            stmt.setDate(3, new Date(pedido.getFechaPedido().getTime()));
            stmt.setDouble(4, pedido.getSubTotal());
            stmt.setDouble(5, pedido.getTotal());
            stmt.setString(6, pedido.getEstado());
            stmt.setString(7, pedido.getFecha_modificacion());
            stmt.setInt(8, pedido.getIdDespachador());
            stmt.setString(9, pedido.getCodPedido());

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Actualizar el estado de un pedido
    public int actualizarEstado(int idPedido, String nuevoEstado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idPedido);
            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows;
    }

    // Obtener el último ID de pedido
    public int obtenerUltimoId() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int ultimoId = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_OBTENER_ULTIMO_ID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ultimoId = rs.getInt("ultimoId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return ultimoId;
    }

    // Eliminar un pedido por ID
    public int eliminar(int idPedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, idPedido); // Establece el ID del pedido a eliminar
            rows = stmt.executeUpdate(); // Ejecuta la eliminación y guarda el número de filas afectadas
        } catch (SQLException ex) {
            ex.printStackTrace(System.out); // Imprime el error en la consola
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return rows; // Retorna el número de filas eliminadas
    }

    // Mapeo de ResultSet a entidad Pedido
    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("idPedido"));
        pedido.setIdCliente(rs.getInt("idCliente"));
        pedido.setIdEmpleado(rs.getInt("idEmpleado"));
        pedido.setFechaPedido(rs.getDate("fechaPedido"));
        pedido.setSubTotal(rs.getDouble("subTotal"));
        pedido.setTotal(rs.getDouble("total"));
        pedido.setEstado(rs.getString("estado"));
        pedido.setFecha_modificacion(rs.getString("fecha_modificacion"));
        pedido.setIdDespachador(rs.getInt("idDespachador"));
        pedido.setCodPedido(rs.getString("codPedido"));
        return pedido;
    }
}
