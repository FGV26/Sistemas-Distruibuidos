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
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "ValidarLogin", urlPatterns = {"/ValidarLogin"})
public class ValidarLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String user = request.getParameter("txtUsuario");
        String pass = request.getParameter("txtClave");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorUsername(user);

        if (usuario == null) {
            // Caso 1: Usuario no encontrado (correo incorrecto)
            request.setAttribute("errorMessage", "Correo no encontrado.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if (!BCrypt.checkpw(pass, usuario.getPassword())) {
            // Caso 2: Contraseña incorrecta
            request.setAttribute("errorMessage", "Contraseña incorrecta.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if (!"Activo".equals(usuario.getEstado())) {
            // Caso 3: Usuario inactivo
            request.setAttribute("errorMessage", "Su usuario está inactivo.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // Caso exitoso: Credenciales correctas y usuario activo
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
                    // Si el rol no es reconocido
                    request.setAttribute("errorMessage", "Rol de usuario no reconocido.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    break;
            }
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
