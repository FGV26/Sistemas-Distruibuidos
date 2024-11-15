package dao;

import entidades.Pedido;
import conexion.conexion;
import entidades.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private static final String SQL_SELECT = "SELECT * FROM pedidos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM pedidos WHERE Id_Pedido = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (Id_Cliente, Id_Empleado, Fecha_Pedido, SubTotal, Total, Estado, Fecha_Modificacion, Id_Despachador, Cod_Pedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE pedidos SET Estado = ? WHERE Id_Pedido = ?";
    private static final String SQL_OBTENER_ULTIMO_ID = "SELECT MAX(Id_Pedido) AS ultimoId FROM pedidos";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE Id_Pedido = ?";

    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM pedidos WHERE idEmpleado = ? AND estado = ?";
    private static final String SQL_DELETE_PEDIDO = "DELETE FROM pedidos WHERE idPedido = ? AND estado = ?";
    private static final String SQL_BUSCAR_PEDIDOS
            = "SELECT p.*, CONCAT(c.nombre, ' ', c.apellido) AS nombreCompleto "
            + "FROM pedidos p "
            + "JOIN clientes c ON p.idCliente = c.idCliente "
            + "WHERE p.idEmpleado = ? AND CONCAT(c.nombre, ' ', c.apellido) LIKE ?";

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

    // Método insertarPedido actualizado
    public int insertarPedido(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int generatedId = -1;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, pedido.getIdCliente());
            stmt.setInt(2, pedido.getIdEmpleado());
            stmt.setDate(3, new java.sql.Date(pedido.getFechaPedido().getTime())); // Usamos java.sql.Date
            stmt.setBigDecimal(4, pedido.getSubTotal());
            stmt.setBigDecimal(5, pedido.getTotal());
            stmt.setString(6, pedido.getEstado());
            stmt.setDate(7, new java.sql.Date(pedido.getFechaModificacion().getTime())); // Usamos java.sql.Date
            stmt.setObject(8, pedido.getIdDespachador(), Types.INTEGER); // Puede ser nulo
            stmt.setString(9, pedido.getCodPedido());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
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
        return generatedId;
    }

    // Mapeo de ResultSet a entidad Pedido
    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        pedido.setIdPedido(rs.getInt("idPedido"));
        pedido.setIdCliente(rs.getInt("idCliente"));
        pedido.setIdEmpleado(rs.getInt("idEmpleado"));

        // Conversión de fechaPedido y fecha_modificacion a java.util.Date
        pedido.setFechaPedido(new Date(rs.getTimestamp("fechaPedido").getTime()));
        pedido.setFechaModificacion(new Date(rs.getTimestamp("fecha_modificacion").getTime()));

        // Conversiones de BigDecimal
        pedido.setSubTotal(rs.getBigDecimal("subTotal"));
        pedido.setTotal(rs.getBigDecimal("total"));

        pedido.setEstado(rs.getString("estado"));

        // Manejo de idDespachador que puede ser nulo
        int idDespachador = rs.getInt("idDespachador");
        if (rs.wasNull()) {
            pedido.setIdDespachador(null); // Si es nulo, seteamos como null
        } else {
            pedido.setIdDespachador(idDespachador);
        }

        pedido.setCodPedido(rs.getString("codPedido"));

        return pedido;
    }

    // Método para listar pedidos de un empleado por estado
    public List<Pedido> listarPedidosPorEmpleado(int idEmpleado, String estado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO);
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, estado);
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

    // Método para eliminar un pedido (solo si está en estado "Proceso")
    public boolean eliminarPedido(int idPedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean eliminado = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_PEDIDO);
            stmt.setInt(1, idPedido);
            stmt.setString(2, "Proceso");

            eliminado = stmt.executeUpdate() > 0;
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
        return eliminado;
    }

    public List<Pedido> buscarPedidosPorNombreCliente(int idEmpleado, String nombreCliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_BUSCAR_PEDIDOS);
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, "%" + nombreCliente + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = mapResultSetToPedido(rs);
                pedido.setNombreCliente(rs.getString("nombreCompleto"));
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

}
