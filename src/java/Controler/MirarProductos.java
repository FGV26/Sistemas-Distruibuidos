
package Controler;

import dao.ProductoDAO;
import entidades.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entidades.Usuario;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MirarProductos", urlPatterns = {"/MirarProductos"})
public class MirarProductos extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !"Empleado".equals(user.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        List<Producto> productos;

        switch (accion) {
            case "Buscar":
                String nombre = request.getParameter("nombre");
                productos = productoDAO.buscarProductosConStock(nombre); // Buscar solo productos con stock
                break;
            default:
                productos = productoDAO.listarConStock(); // Listar solo productos con stock
                break;
        }

        request.setAttribute("ListaProductos", productos);
        request.getRequestDispatcher("MirarProductos.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
