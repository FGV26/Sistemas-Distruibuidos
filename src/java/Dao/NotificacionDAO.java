
package dao;

import entidades.Notificacion;
import conexion.conexion;  // Ajuste del paquete de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    private static final String SQL_SELECT = "SELECT * FROM notificaciones";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM notificaciones WHERE Id_Notificacion = ?";
    private static final String SQL_INSERT = "INSERT INTO notificaciones (Id_Pedido, Tipo, Estado_Pedido, Destinatario, Asunto, Mensaje, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE notificaciones SET Id_Pedido = ?, Tipo = ?, Estado_Pedido = ?, Destinatario = ?, Asunto = ?, Mensaje = ?, Estado = ? WHERE Id_Notificacion = ?";
    private static final String SQL_DELETE = "DELETE FROM notificaciones WHERE Id_Notificacion = ?";

    // Listar todas las notificaciones
    public List<Notificacion> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Notificacion> notificaciones = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Notificacion notificacion = new Notificacion();
                notificacion.setIdNotificacion(rs.getInt("Id_Notificacion"));
                notificacion.setIdPedido(rs.getInt("Id_Pedido"));
                notificacion.setTipo(rs.getString("Tipo"));
                notificacion.setEstadoPedido(rs.getString("Estado_Pedido"));
                notificacion.setDestinatario(rs.getString("Destinatario"));
                notificacion.setAsunto(rs.getString("Asunto"));
                notificacion.setMensaje(rs.getString("Mensaje"));
                notificacion.setEstado(rs.getString("Estado"));
                notificaciones.add(notificacion);
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
        return notificaciones;
    }

    // Buscar notificación por ID
    public Notificacion buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Notificacion notificacion = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                notificacion = new Notificacion();
                notificacion.setIdNotificacion(rs.getInt("Id_Notificacion"));
                notificacion.setIdPedido(rs.getInt("Id_Pedido"));
                notificacion.setTipo(rs.getString("Tipo"));
                notificacion.setEstadoPedido(rs.getString("Estado_Pedido"));
                notificacion.setDestinatario(rs.getString("Destinatario"));
                notificacion.setAsunto(rs.getString("Asunto"));
                notificacion.setMensaje(rs.getString("Mensaje"));
                notificacion.setEstado(rs.getString("Estado"));
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
        return notificacion;
    }

    // Insertar una nueva notificación
    public int insertar(Notificacion notificacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, notificacion.getIdPedido());
            stmt.setString(2, notificacion.getTipo());
            stmt.setString(3, notificacion.getEstadoPedido());
            stmt.setString(4, notificacion.getDestinatario());
            stmt.setString(5, notificacion.getAsunto());
            stmt.setString(6, notificacion.getMensaje());
            stmt.setString(7, notificacion.getEstado());
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

    // Actualizar una notificación
    public int actualizar(Notificacion notificacion) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setInt(1, notificacion.getIdPedido());
            stmt.setString(2, notificacion.getTipo());
            stmt.setString(3, notificacion.getEstadoPedido());
            stmt.setString(4, notificacion.getDestinatario());
            stmt.setString(5, notificacion.getAsunto());
            stmt.setString(6, notificacion.getMensaje());
            stmt.setString(7, notificacion.getEstado());
            stmt.setInt(8, notificacion.getIdNotificacion());
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

    // Eliminar una notificación
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
