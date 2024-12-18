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
    private static final String SQL_INSERT = "INSERT INTO clientes (Nombre, Apellido, Direccion, DNI, Telefono, Email, Fecha_Creacion, Id_Empleado, Cod_Cliente) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE clientes SET Nombre = ?, Apellido = ?, Direccion = ?, DNI = ?, Telefono = ?, Email = ?, Id_Empleado = ?, Cod_Cliente = ? WHERE Id_Cliente = ?";
    private static final String SQL_DELETE = "DELETE FROM clientes WHERE Id_Cliente = ?";
    private static final String SQL_SELECT_BY_DNI = "SELECT * FROM clientes WHERE DNI = ?";
    private static final String SQL_OBTENER_ULTIMO_ID = "SELECT MAX(Id_Cliente) AS ultimoId FROM clientes";
    private static final String SQL_BUSCAR_DNI_PARCIAL = "SELECT Id_Cliente, Cod_Cliente, Nombre, Apellido FROM clientes WHERE DNI LIKE ?";
    private static final String SQL_SELECT_BY_EMPLEADO = "SELECT * FROM clientes WHERE Id_Empleado = ?";
    private static final String SQL_BUSCAR_CLIENTES_POR_DNI_Y_EMPLEADO = "SELECT * FROM clientes WHERE DNI LIKE ? AND Id_Empleado = ?";

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
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setFechaCreacion(rs.getTimestamp("Fecha_Creacion"));
                cliente.setIdEmpleado(rs.getInt("Id_Empleado"));
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

    // Obtener cliente por ID
    public Cliente obtenerClientePorId(int id) {
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
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setFechaCreacion(rs.getTimestamp("Fecha_Creacion"));
                cliente.setIdEmpleado(rs.getInt("Id_Empleado"));
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

    // Obtener el último ID de cliente para generar el Cod_Cliente
    public int obtenerUltimoIdCliente() {
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
                conexion.close(rs);
                conexion.close(stmt);
                conexion.close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return ultimoId;
    }

    public int insertar(Cliente cliente) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();

            int nuevoId = obtenerUltimoIdCliente() + 1;
            String nuevoCodCliente = "CLIEN" + String.format("%03d", nuevoId);
            cliente.setCodCliente(nuevoCodCliente);

            stmt = conn.prepareStatement(SQL_INSERT);

            // Asignación de parámetros
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getDireccion());
            stmt.setString(4, cliente.getDni());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
            stmt.setInt(7, cliente.getIdEmpleado());
            stmt.setString(8, cliente.getCodCliente());
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

    // Actualizar un cliente existente
    public int actualizarCliente(Cliente cliente) {
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
            stmt.setInt(7, cliente.getIdEmpleado());
            stmt.setString(8, cliente.getCodCliente());
            stmt.setInt(9, cliente.getIdCliente());

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

    // Obtener cliente por DNI
    public Cliente obtenerClientePorDni(String dni) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cliente cliente = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_DNI);
            stmt.setString(1, dni);
            rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setIdEmpleado(rs.getInt("Id_Empleado"));
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

    // Búsqueda parcial de clientes por DNI
    public List<Cliente> buscarClientesPorDniParcial(String dniParcial) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_BUSCAR_DNI_PARCIAL);
            stmt.setString(1, dniParcial + "%"); // Usa '%' para coincidencias que comiencen con 'dniParcial'
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
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

    public List<Cliente> listarClientesPorEmpleado(int idEmpleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMPLEADO);
            stmt.setInt(1, idEmpleado);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setIdEmpleado(rs.getInt("Id_Empleado"));
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

    public List<Cliente> buscarClientesPorDniYEmpleado(String dniParcial, int idEmpleado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_BUSCAR_CLIENTES_POR_DNI_Y_EMPLEADO);
            stmt.setString(1, dniParcial + "%");  // Búsqueda parcial de DNI
            stmt.setInt(2, idEmpleado);  // Filtrar por Id_Empleado
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("Id_Cliente"));
                cliente.setCodCliente(rs.getString("Cod_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setDireccion(rs.getString("Direccion"));
                cliente.setDni(rs.getString("DNI"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setEmail(rs.getString("Email"));
                cliente.setIdEmpleado(rs.getInt("Id_Empleado"));
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

}
