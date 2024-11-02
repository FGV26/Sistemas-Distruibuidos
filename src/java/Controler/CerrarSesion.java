
package Controler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CerrarSesion", urlPatterns = {"/CerrarSesion"})
public class CerrarSesion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalida la sesión para cerrar completamente la sesión del usuario
        HttpSession session = request.getSession(false); // false para no crear una nueva sesión si no existe
        if (session != null) {
            session.invalidate(); // Invalida toda la sesión
        }

        // Redirige al usuario a la página de inicio de sesión
        response.sendRedirect("login.jsp"); // Cambia "login.jsp" a la URL de tu página de inicio de sesión
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
        return "Servlet para cerrar sesión del usuario";
    }
}
