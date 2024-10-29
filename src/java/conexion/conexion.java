
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class conexion {

    // Datos de conexión a la base de datos
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sistemas_distribuidos?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";  // Cambia 'root' si usas otro usuario
    private static final String JDBC_PASSWORD = "";  // Añade tu contraseña si tienes una

    // Método para obtener una conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // Cerrar ResultSet
    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }

    // Cerrar Statement
    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    // Cerrar PreparedStatement
    public static void close(PreparedStatement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    // Cerrar conexión
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
