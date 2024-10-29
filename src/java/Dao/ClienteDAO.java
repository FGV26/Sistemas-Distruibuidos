
package dao;

import entidades.Cliente;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String SQL_SELECT = "SELECT * FROM clientes";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM clientes WHERE Id_Cliente = ?";
    private static final String SQL_INSERT = "INSERT INTO clientes (Nombre, Apellido, Direccion, DNI, Telefono, Email) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE clientes SET Nombre = ?, Apellido = ?, Direccion = ?, DNI = ?, Telefono = ?, Email = ? WHERE Id_Cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM clientes WHERE Id_Cliente = ?";

    // Listar todos los clientes
    public List<Cliente> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                clientes.add(cliente);
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
        return clientes;
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
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
        return cliente;
    }

    // Insertar un nuevo cliente
    public int insertar(Cliente cliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getDni());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
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

    // Actualizar un cliente
    public int actualizar(Cliente cliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getDni());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
            stmt.setInt(7, cliente.getIdCliente());
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

    // Eliminar un cliente
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
