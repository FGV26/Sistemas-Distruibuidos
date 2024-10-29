
package dao;

import entidades.Producto;
import conexion.conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String SQL_SELECT = "SELECT * FROM productos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM productos WHERE Id_Producto = ?";
    private static final String SQL_INSERT = "INSERT INTO productos (Descripcion, Costo, Precio, Stock, Stock_Minimo, Estado) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE productos SET Descripcion = ?, Costo = ?, Precio = ?, Stock = ?, Stock_Minimo = ?, Estado = ? WHERE Id_Producto = ?";
    private static final String SQL_DELETE = "DELETE FROM productos WHERE Id_Producto = ?";

    // Listar todos los productos
    public List<Producto> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("Id_Producto"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setCosto(rs.getDouble("Costo"));
                producto.setPrecio(rs.getDouble("Precio"));
                producto.setStock(rs.getInt("Stock"));
                producto.setStockMinimo(rs.getInt("Stock_Minimo"));
                producto.setEstado(rs.getString("Estado"));
                productos.add(producto);
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
        return productos;
    }

    // Buscar producto por ID
    public Producto buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Producto producto = null;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("Id_Producto"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setCosto(rs.getDouble("Costo"));
                producto.setPrecio(rs.getDouble("Precio"));
                producto.setStock(rs.getInt("Stock"));
                producto.setStockMinimo(rs.getInt("Stock_Minimo"));
                producto.setEstado(rs.getString("Estado"));
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
        return producto;
    }

    // Insertar un nuevo producto
    public int insertar(Producto producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, producto.getDescripcion());
            stmt.setDouble(2, producto.getCosto());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getStockMinimo());
            stmt.setString(6, producto.getEstado());
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

    // Actualizar un producto
    public int actualizar(Producto producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, producto.getDescripcion());
            stmt.setDouble(2, producto.getCosto());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getStockMinimo());
            stmt.setString(6, producto.getEstado());
            stmt.setInt(7, producto.getIdProducto());
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

    // Eliminar un producto
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
