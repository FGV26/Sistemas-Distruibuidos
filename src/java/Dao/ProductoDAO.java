package dao;

import entidades.Producto;
import conexion.conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String SQL_SELECT = "SELECT * FROM productos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM productos WHERE idProducto = ?";
    private static final String SQL_SELECT_BY_NAME = "SELECT * FROM productos WHERE nombre LIKE ?";
    private static final String SQL_SELECT_LOW_STOCK = "SELECT * FROM productos WHERE stock < stock_minimo";
    private static final String SQL_SELECT_NO_STOCK = "SELECT * FROM productos WHERE stock = 0";
    private static final String SQL_INSERT = "INSERT INTO productos (idCategoria, nombre, precio, stock, stock_minimo, descripcion, imagen, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE productos SET idCategoria = ?, nombre = ?, precio = ?, stock = ?, stock_minimo = ?, descripcion = ?, imagen = ?, estado = ? WHERE idProducto = ?";
    private static final String SQL_DELETE = "DELETE FROM productos WHERE idProducto = ?";

    private static final String SQL_OBTENER_ULTIMO_NUMERO
            = "SELECT MAX(CAST(SUBSTRING(imagen, 4, LENGTH(imagen) - 7) AS UNSIGNED)) AS ultimoNumero FROM productos";
    private static final String IMAGE_PREFIX = "PRO";

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
                Producto producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getInt("idCategoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        rs.getString("descripcion"),
                        rs.getString("imagen"),
                        rs.getString("estado"),
                        rs.getString("fecha_creacion")
                );
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

    // Obtener producto por ID
    public Producto obtenerProductoPorId(int id) {
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
                producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getInt("idCategoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        rs.getString("descripcion"),
                        rs.getString("imagen"),
                        rs.getString("estado"),
                        rs.getString("fecha_creacion")
                );
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

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_NAME);
            stmt.setString(1, "%" + nombre + "%"); // Usar comodín para búsqueda parcial
            rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getInt("idCategoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        rs.getString("descripcion"),
                        rs.getString("imagen"),
                        rs.getString("estado"),
                        rs.getString("fecha_creacion")
                );
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

    // Listar productos con poco stock
    public List<Producto> listarPocoStock() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_LOW_STOCK);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getInt("idCategoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        rs.getString("descripcion"),
                        rs.getString("imagen"),
                        rs.getString("estado"),
                        rs.getString("fecha_creacion")
                );
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

    // Listar productos sin stock
    public List<Producto> listarSinStock() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Producto> productos = new ArrayList<>();

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_NO_STOCK);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("idProducto"),
                        rs.getInt("idCategoria"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getInt("stock_minimo"),
                        rs.getString("descripcion"),
                        rs.getString("imagen"),
                        rs.getString("estado"),
                        rs.getString("fecha_creacion")
                );
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

    // Método para insertar un producto (devuelve boolean)
    public boolean insertar(Producto producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, producto.getIdCategoria());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getStockMinimo());
            stmt.setString(6, producto.getDescripcion());
            stmt.setString(7, producto.getImagen());
            stmt.setString(8, producto.getEstado());

            int rows = stmt.executeUpdate();
            success = (rows > 0);
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
        return success;
    }

// Método para actualizar un producto (devuelve boolean)
    public boolean actualizar(Producto producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setInt(1, producto.getIdCategoria());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getStockMinimo());
            stmt.setString(6, producto.getDescripcion());
            stmt.setString(7, producto.getImagen());
            stmt.setString(8, producto.getEstado());
            stmt.setInt(9, producto.getIdProducto());

            int rows = stmt.executeUpdate();
            success = (rows > 0);
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
        return success;
    }

// Método para eliminar un producto (devuelve boolean)
    public boolean eliminar(int idProducto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, idProducto);

            int rows = stmt.executeUpdate();
            success = (rows > 0);
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
        return success;
    }

    // Método para insertar un producto y obtener el ID generado
    public int insertarImagen(Producto producto) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int generatedId = -1;

        try {
            conn = conexion.getConnection();
            // Usando el nombre correcto de la tabla y columnas
            stmt = conn.prepareStatement(
                    "INSERT INTO productos (idCategoria, nombre, precio, stock, stock_minimo, descripcion, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            stmt.setInt(1, producto.getIdCategoria());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getStockMinimo());  // Cambiado a `stock_minimo`
            stmt.setString(6, producto.getDescripcion());
            stmt.setString(7, producto.getEstado());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    producto.setIdProducto(generatedId); // Guardar el ID en el objeto Producto
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

    // Método para actualizar el nombre de la imagen en la tabla 'productos'
    public boolean actualizarImagen(int idProducto, String imagen) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;

        try {
            conn = conexion.getConnection();
            stmt = conn.prepareStatement("UPDATE productos SET imagen = ? WHERE idProducto = ?");
            stmt.setString(1, imagen);
            stmt.setInt(2, idProducto);

            int rows = stmt.executeUpdate();
            success = (rows > 0);
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
        return success;
    }

}
