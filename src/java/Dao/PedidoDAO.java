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
    private static final String SQL_SELECT_BY_EMPLEADO
            = "SELECT p.*, CONCAT(c.nombre, ' ', c.apellido) AS nombreCompleto  "
            + "FROM pedidos p "
            + "JOIN clientes c ON p.Id_Cliente = c.Id_Cliente "
            + "WHERE p.Id_Empleado = ?";

    private static final String SQL_DELETE_PEDIDO = "DELETE FROM pedidos WHERE Id_Pedido = ? AND estado = 'Proceso'";
    private static final String SQL_BUSCAR_PEDIDOS
            = "SELECT p.*, CONCAT(c.nombre, ' ', c.apellido) AS nombreCompleto "
            + "FROM pedidos p "
            + "JOIN clientes c ON p.Id_Cliente = c.Id_Cliente "
            + "WHERE p.Id_Empleado = ? AND CONCAT(c.nombre, ' ', c.apellido) LIKE ?";

    //ObterPedido 
    // Constantes SQL específicas
    private static final String SQL_LISTAR_PEDIDOS_DISPONIBLES
            = "SELECT p.*, CONCAT(c.Nombre, ' ', c.Apellido) AS nombreCompleto "
            + "FROM pedidos p "
            + "JOIN clientes c ON p.Id_Cliente = c.Id_Cliente "
            + "WHERE p.Estado = 'Proceso' AND p.Id_Despachador IS NULL";

    private static final String SQL_ASIGNAR_PEDIDO
            = "UPDATE pedidos SET Id_Despachador = ?, Estado = 'Leído' WHERE Id_Pedido = ? AND Id_Despachador IS NULL AND Estado = 'Proceso'";

    private static final String SQL_BUSCAR_POR_CLIENTE
            = "SELECT p.*, CONCAT(c.Nombre, ' ', c.Apellido) AS nombreCompleto "
            + "FROM pedidos p "
            + "JOIN clientes c ON p.Id_Cliente = c.Id_Cliente "
            + "WHERE p.Estado = 'Proceso' AND p.Id_Despachador IS NULL AND "
            + "      CONCAT(c.Nombre, ' ', c.Apellido) LIKE ?";

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

    public List<Pedido> listarPedidosPorEmpleado(int idEmpleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();

            // Usar la consulta SQL corregida
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO);
            stmt.setInt(1, idEmpleado);

            rs = stmt.executeQuery();

            // Procesar los resultados
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

    public List<Pedido> buscarPedidosPorNombreCliente(int idEmpleado, String nombreCliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();

            // Usar la consulta SQL corregida
            stmt = conn.prepareStatement(SQL_BUSCAR_PEDIDOS);
            stmt.setInt(1, idEmpleado); // ID del empleado
            stmt.setString(2, "%" + nombreCliente + "%"); // Buscar con LIKE

            rs = stmt.executeQuery();

            // Procesar los resultados
            while (rs.next()) {
                Pedido pedido = mapResultSetToPedido(rs);
                pedidos.add(pedido);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out); // Manejar excepciones
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

    public List<Pedido> buscarPedidosPorNombreClienteDisponibles(String nombreCliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_BUSCAR_POR_CLIENTE);
            stmt.setString(1, "%" + nombreCliente + "%");
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

    public boolean eliminarPedido(int idPedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean rowsAffected = false;

        try {
            conn = conexion.getConnection();

            // Usar la consulta SQL corregida
            stmt = conn.prepareStatement(SQL_DELETE_PEDIDO);
            stmt.setInt(1, idPedido);

            int rows = stmt.executeUpdate();
            rowsAffected = rows > 0;
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
        return rowsAffected;
    }

    public List<Pedido> listarPedidosDisponibles() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Pedido> pedidos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_LISTAR_PEDIDOS_DISPONIBLES);
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

    public boolean asignarPedido(int idPedido, int idDespachador) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean actualizado = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_ASIGNAR_PEDIDO);

            stmt.setInt(1, idDespachador); // Id del despachador
            stmt.setInt(2, idPedido);     // Id del pedido

            int rowsAffected = stmt.executeUpdate();
            actualizado = rowsAffected > 0; // Si se afectaron filas, la asignación fue exitosa
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
        return actualizado;
    }

    // Mapeo de ResultSet a entidad Pedido
    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        // Asegúrate de que estos nombres coincidan con las columnas de tu consulta SQL
        pedido.setIdPedido(rs.getInt("Id_Pedido"));
        pedido.setIdCliente(rs.getInt("Id_Cliente"));
        pedido.setIdEmpleado(rs.getInt("Id_Empleado"));
        pedido.setFechaPedido(rs.getTimestamp("Fecha_Pedido")); // Asume que "Fecha_Pedido" es tipo timestamp
        pedido.setFechaModificacion(rs.getTimestamp("Fecha_Modificacion")); // Asume que "Fecha_Modificacion" es tipo timestamp
        pedido.setSubTotal(rs.getBigDecimal("SubTotal"));
        pedido.setTotal(rs.getBigDecimal("Total"));
        pedido.setEstado(rs.getString("estado"));

        // Manejo de idDespachador que puede ser nulo
        int idDespachador = rs.getInt("Id_Despachador");
        if (rs.wasNull()) {
            pedido.setIdDespachador(null);
        } else {
            pedido.setIdDespachador(idDespachador);
        }

        pedido.setCodPedido(rs.getString("Cod_Pedido"));

        pedido.setNombreCliente(rs.getString("nombreCompleto"));

        return pedido;
    }

}
