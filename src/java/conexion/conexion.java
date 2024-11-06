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
    private static final String JDBC_USER = "root";  
    private static final String JDBC_PASSWORD = "";  

    // Método para obtener una conexión
    public static Connection getConnection() throws SQLException {  
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se pudo cargar el driver de MySQL", e);
        }
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
