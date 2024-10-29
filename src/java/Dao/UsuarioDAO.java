package dao;

import entidades.Usuario;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String SQL_SELECT = "SELECT * FROM usuarios";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM usuarios WHERE Id_Usuario = ?";
    private static final String SQL_INSERT = "INSERT INTO usuarios (Username, Password, Email, Rol, Nombre, Apellido, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuarios SET Username = ?, Password = ?, Email = ?, Rol = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_DELETE = "DELETE FROM usuarios WHERE Id_Usuario = ?";

    // Listar todos los usuarios
    public List<Usuario> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("Id_Usuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password"));
                usuario.setEmail(rs.getString("Email"));
                usuario.setRol(rs.getString("Rol"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setApellido(rs.getString("Apellido"));
                usuario.setEstado(rs.getString("Estado"));
                usuarios.add(usuario);
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
        return usuarios;
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("Id_Usuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password"));
                usuario.setEmail(rs.getString("Email"));
                usuario.setRol(rs.getString("Rol"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setApellido(rs.getString("Apellido"));
                usuario.setEstado(rs.getString("Estado"));
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
        return usuario;
    }

    // Insertar un nuevo usuario
    public int insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getRol());
            stmt.setString(5, usuario.getNombre());
            stmt.setString(6, usuario.getApellido());
            stmt.setString(7, usuario.getEstado());
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

    // Actualizar un usuario
    public int actualizar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getRol());
            stmt.setString(5, usuario.getNombre());
            stmt.setString(6, usuario.getApellido());
            stmt.setString(7, usuario.getEstado());
            stmt.setInt(8, usuario.getIdUsuario());
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

    // Eliminar un usuario
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

    public Usuario buscarPorUsername(String username) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = conexion.getConnection();
            String sql = "SELECT * FROM usuarios WHERE Username = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("Id_Usuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setPassword(rs.getString("Password"));
                usuario.setEmail(rs.getString("Email"));
                usuario.setRol(rs.getString("Rol"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setApellido(rs.getString("Apellido"));
                usuario.setEstado(rs.getString("Estado"));
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
        return usuario;
    }

}
