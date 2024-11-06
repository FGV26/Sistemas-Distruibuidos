package dao;

import entidades.Usuario;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {

    // Consultas Generales // 
    private static final String SQL_SELECT = "SELECT * FROM usuarios";
    private static final String SQL_INSERT = "INSERT INTO usuarios (Username, Password, Email, Rol, Nombre, Apellido, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuarios SET Username = ?, Password = ?, Email = ?, Rol = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_DELETE = "DELETE FROM usuarios WHERE Id_Usuario = ?";

    /* Seleccionar Usuario*/
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM usuarios WHERE Id_Usuario = ?";

    /* Consultas para el Empleado*/
    private static final String SQL_SELECT_EMPLEADOS = "SELECT * FROM usuarios WHERE Rol = 'Empleado'";
    private static final String SQL_INSERT_EMPLEADO = "INSERT INTO usuarios (Username, Password, Email, Rol, Nombre, Apellido, Estado) VALUES (?, ?, ?, 'Empleado', ?, ?, ?)";
    private static final String SQL_UPDATE_EMPLEADO_CON_PASSWORD = "UPDATE usuarios SET Username = ?, Password = ?, Email = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_UPDATE_EMPLEADO_SIN_PASSWORD = "UPDATE usuarios SET Username = ?, Email = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_DELETE_EMPLEADO = "DELETE FROM usuarios WHERE Id_Usuario = ?";
    private static final String SQL_SEARCH_EMPLEADO = "SELECT * FROM usuarios WHERE Nombre LIKE ? OR Username LIKE ?";

    // Consultas para el Despachador
    private static final String SQL_SELECT_DESPACHADORES = "SELECT * FROM usuarios WHERE Rol = 'Despachador'";
    private static final String SQL_INSERT_DESPACHADOR = "INSERT INTO usuarios (Username, Password, Email, Rol, Nombre, Apellido, Estado) VALUES (?, ?, ?, 'Despachador', ?, ?, ?)";
    private static final String SQL_UPDATE_DESPACHADOR_CON_PASSWORD = "UPDATE usuarios SET Username = ?, Password = ?, Email = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_UPDATE_DESPACHADOR_SIN_PASSWORD = "UPDATE usuarios SET Username = ?, Email = ?, Nombre = ?, Apellido = ?, Estado = ? WHERE Id_Usuario = ?";
    private static final String SQL_DELETE_DESPACHADOR = "DELETE FROM usuarios WHERE Id_Usuario = ?";
    private static final String SQL_SEARCH_DESPACHADOR = "SELECT * FROM usuarios WHERE Rol = 'Despachador' AND (Nombre LIKE ? OR Username LIKE ?)";

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

    // Metodos para Empleados //
    // Listar empleados
    public List<Usuario> listarEmpleados() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> empleados = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_EMPLEADOS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario empleado = new Usuario();
                empleado.setIdUsuario(rs.getInt("Id_Usuario"));
                empleado.setUsername(rs.getString("Username"));
                empleado.setPassword(rs.getString("Password")); // Guardamos el hash
                empleado.setEmail(rs.getString("Email"));
                empleado.setRol(rs.getString("Rol"));
                empleado.setNombre(rs.getString("Nombre"));
                empleado.setApellido(rs.getString("Apellido"));
                empleado.setEstado(rs.getString("Estado"));
                empleados.add(empleado);
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
        return empleados;
    }

    // Agregar un nuevo empleado con hash de contraseña
    public boolean agregarEmpleado(Usuario empleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            // Hashear la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(empleado.getPassword(), BCrypt.gensalt(12));

            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_EMPLEADO);
            stmt.setString(1, empleado.getUsername());
            stmt.setString(2, hashedPassword);  // Guardamos la contraseña hasheada
            stmt.setString(3, empleado.getEmail());
            stmt.setString(4, empleado.getNombre());
            stmt.setString(5, empleado.getApellido());
            stmt.setString(6, empleado.getEstado());
            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

// Actualizar empleado con opción de cambiar contraseña
    public boolean actualizarEmpleado(Usuario empleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = conexion.getConnection();

            // Si la contraseña no está vacía, hashearla y actualizarla; si está vacía, mantener la actual
            if (empleado.getPassword() != null && !empleado.getPassword().isEmpty()) {
                // Hashear la nueva contraseña
                String hashedPassword = BCrypt.hashpw(empleado.getPassword(), BCrypt.gensalt(12));
                stmt = conn.prepareStatement(SQL_UPDATE_EMPLEADO_CON_PASSWORD);
                stmt.setString(1, empleado.getUsername());
                stmt.setString(2, hashedPassword); // Usar la nueva contraseña hasheada
                stmt.setString(3, empleado.getEmail());
                stmt.setString(4, empleado.getNombre());
                stmt.setString(5, empleado.getApellido());
                stmt.setString(6, empleado.getEstado());
                stmt.setInt(7, empleado.getIdUsuario());
            } else {
                // Mantener la contraseña actual
                stmt = conn.prepareStatement(SQL_UPDATE_EMPLEADO_SIN_PASSWORD);
                stmt.setString(1, empleado.getUsername());
                stmt.setString(2, empleado.getEmail());
                stmt.setString(3, empleado.getNombre());
                stmt.setString(4, empleado.getApellido());
                stmt.setString(5, empleado.getEstado());
                stmt.setInt(6, empleado.getIdUsuario());
            }

            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

    // Eliminar empleado
    public boolean eliminarEmpleado(int idEmpleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_EMPLEADO);
            stmt.setInt(1, idEmpleado);
            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

    // Obtener empleado por ID
    public Usuario obtenerEmpleadoPorId(int idEmpleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario empleado = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, idEmpleado);
            rs = stmt.executeQuery();
            if (rs.next()) {
                empleado = new Usuario();
                empleado.setIdUsuario(rs.getInt("Id_Usuario"));
                empleado.setUsername(rs.getString("Username"));
                empleado.setPassword(rs.getString("Password")); // Guardamos el hash
                empleado.setEmail(rs.getString("Email"));
                empleado.setRol(rs.getString("Rol"));
                empleado.setNombre(rs.getString("Nombre"));
                empleado.setApellido(rs.getString("Apellido"));
                empleado.setEstado(rs.getString("Estado"));
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
        return empleado;
    }

    // Método para buscar empleados por nombre o username
    public List<Usuario> buscarEmpleadosPorNombreOUsername(String parametroBusqueda) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> empleados = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SEARCH_EMPLEADO);
            stmt.setString(1, "%" + parametroBusqueda + "%"); // Búsqueda parcial con LIKE
            stmt.setString(2, "%" + parametroBusqueda + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario empleado = new Usuario();
                empleado.setIdUsuario(rs.getInt("Id_Usuario"));
                empleado.setUsername(rs.getString("Username"));
                empleado.setPassword(rs.getString("Password"));
                empleado.setEmail(rs.getString("Email"));
                empleado.setRol(rs.getString("Rol"));
                empleado.setNombre(rs.getString("Nombre"));
                empleado.setApellido(rs.getString("Apellido"));
                empleado.setEstado(rs.getString("Estado"));
                empleados.add(empleado);
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
        return empleados;
    }

    // Metodos de Despachador // 
    public List<Usuario> listarDespachadores() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> despachadores = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_DESPACHADORES);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario despachador = new Usuario();
                despachador.setIdUsuario(rs.getInt("Id_Usuario"));
                despachador.setUsername(rs.getString("Username"));
                despachador.setPassword(rs.getString("Password"));
                despachador.setEmail(rs.getString("Email"));
                despachador.setRol(rs.getString("Rol"));
                despachador.setNombre(rs.getString("Nombre"));
                despachador.setApellido(rs.getString("Apellido"));
                despachador.setEstado(rs.getString("Estado"));
                despachadores.add(despachador);
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
        return despachadores;
    }

    public boolean agregarDespachador(Usuario despachador) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            // Hashear la contraseña antes de guardarla con 12 rondas
            String hashedPassword = BCrypt.hashpw(despachador.getPassword(), BCrypt.gensalt(12));

            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_DESPACHADOR);
            stmt.setString(1, despachador.getUsername());
            stmt.setString(2, hashedPassword);  // Guardamos la contraseña hasheada
            stmt.setString(3, despachador.getEmail());
            stmt.setString(4, despachador.getNombre());
            stmt.setString(5, despachador.getApellido());
            stmt.setString(6, despachador.getEstado());

            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

    public boolean actualizarDespachador(Usuario despachador) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = conexion.getConnection();

            if (despachador.getPassword() != null && !despachador.getPassword().isEmpty()) {
                // Hashear la nueva contraseña con 12 rondas
                String hashedPassword = BCrypt.hashpw(despachador.getPassword(), BCrypt.gensalt(12));
                stmt = conn.prepareStatement(SQL_UPDATE_DESPACHADOR_CON_PASSWORD);
                stmt.setString(1, despachador.getUsername());
                stmt.setString(2, hashedPassword); // Usar la nueva contraseña hasheada
                stmt.setString(3, despachador.getEmail());
                stmt.setString(4, despachador.getNombre());
                stmt.setString(5, despachador.getApellido());
                stmt.setString(6, despachador.getEstado());
                stmt.setInt(7, despachador.getIdUsuario());
            } else {
                stmt = conn.prepareStatement(SQL_UPDATE_DESPACHADOR_SIN_PASSWORD);
                stmt.setString(1, despachador.getUsername());
                stmt.setString(2, despachador.getEmail());
                stmt.setString(3, despachador.getNombre());
                stmt.setString(4, despachador.getApellido());
                stmt.setString(5, despachador.getEstado());
                stmt.setInt(6, despachador.getIdUsuario());
            }

            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

    public boolean eliminarDespachador(int idDespachador) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_DESPACHADOR);
            stmt.setInt(1, idDespachador);
            exito = stmt.executeUpdate() > 0;
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
        return exito;
    }

    public List<Usuario> buscarDespachadoresPorNombreOUsername(String parametroBusqueda) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Usuario> despachadores = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SEARCH_DESPACHADOR);
            stmt.setString(1, "%" + parametroBusqueda + "%");
            stmt.setString(2, "%" + parametroBusqueda + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario despachador = new Usuario();
                despachador.setIdUsuario(rs.getInt("Id_Usuario"));
                despachador.setUsername(rs.getString("Username"));
                despachador.setPassword(rs.getString("Password"));
                despachador.setEmail(rs.getString("Email"));
                despachador.setRol(rs.getString("Rol"));
                despachador.setNombre(rs.getString("Nombre"));
                despachador.setApellido(rs.getString("Apellido"));
                despachador.setEstado(rs.getString("Estado"));
                despachadores.add(despachador);
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
        return despachadores;
    }

}
