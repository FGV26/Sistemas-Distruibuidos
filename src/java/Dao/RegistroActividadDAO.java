
package dao;

import entidades.RegistroActividad;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RegistroActividadDAO {

    private static final String SQL_SELECT_LAST_8 = "SELECT * FROM registro_actividades ORDER BY fecha DESC LIMIT 8";
    private static final String SQL_INSERT = "INSERT INTO registro_actividades (tipo, descripcion, fecha, id_usuario) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_LAST_8_BY_USER = "SELECT * FROM registro_actividades WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 8";

    // Método para insertar un nuevo registro de actividad
    public int insertar(RegistroActividad registro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, registro.getTipo());
            stmt.setString(2, registro.getDescripcion());
            stmt.setTimestamp(3, Timestamp.valueOf(registro.getFecha())); // Convertimos LocalDateTime a Timestamp
            stmt.setInt(4, registro.getIdUsuario()); // Asociamos la actividad con el usuario
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

    // Método para obtener los últimos 8 registros de un usuario específico
    public List<RegistroActividad> obtenerUltimosRegistrosPorUsuario(int idUsuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RegistroActividad> registros = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_LAST_8_BY_USER);
            stmt.setInt(1, idUsuario); // Filtramos por el ID del usuario
            rs = stmt.executeQuery();
            while (rs.next()) {
                RegistroActividad registro = new RegistroActividad();
                registro.setId(rs.getInt("id"));
                registro.setTipo(rs.getString("tipo"));
                registro.setDescripcion(rs.getString("descripcion"));
                registro.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                registro.setIdUsuario(rs.getInt("id_usuario"));
                registros.add(registro);
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
        return registros;
    }
    
    
}
