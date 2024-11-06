package Controler;

import dao.UsuarioDAO;
import entidades.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ValidarLogin", urlPatterns = {"/ValidarLogin"})
public class ValidarLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String user = request.getParameter("txtUsuario");
        String pass = request.getParameter("txtClave");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorUsername(user);

        if (usuario != null && usuario.getPassword().equals(pass)) {
            // Si es correcto, creamos la sesión
            HttpSession session = request.getSession();
            session.setAttribute("user", usuario);

            // Redirigir según el rol del usuario
            switch (usuario.getRol()) {
                case "Empleado":
                    response.sendRedirect("DashboardActividades?accion=Listar");  
                    break;
                case "Despachador":
                    response.sendRedirect("DashboardActividades?accion=Listar");
                    break;
                case "Administrador":
                    response.sendRedirect("DashboardActividades?accion=Listar");
                    break;
                default:
                    request.setAttribute("errorMessage", "Rol de usuario no reconocido.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    break;
            }

        } else {
            // Si las credenciales son incorrectas, mostrar mensaje de error
            request.setAttribute("errorMessage", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
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

    @Override
    public String getServletInfo() {
        return "Servlet para validar el login con redirección por rol de usuario";
    }
}
