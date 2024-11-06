
package Controler;

import dao.RegistroActividadDAO;
import entidades.RegistroActividad;
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardActividades", urlPatterns = {"/DashboardActividades"})
public class DashboardActividades extends HttpServlet {

    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int idUsuario = user.getIdUsuario();  // Obtener el ID del usuario actual
        String rol = user.getRol();
        String accion = request.getParameter("accion");

        // Si la acción no está definida, establecer "Listar" por defecto.
        if (accion == null) {
            accion = "Listar";
        }

        // Verificamos la acción solicitada
        switch (accion) {
            case "Listar":
                listarActividadesPorUsuario(request, response, idUsuario, rol);
                break;
            default:
                response.sendRedirect("login.jsp");
                break;
        }
    }

    // Método para listar actividades específicas del usuario en sesión
    private void listarActividadesPorUsuario(HttpServletRequest request, HttpServletResponse response, int idUsuario, String rol)
            throws ServletException, IOException {
        
        // Obtener los últimos 8 registros de actividad para el usuario específico
        List<RegistroActividad> registros = registroActividadDAO.obtenerUltimosRegistrosPorUsuario(idUsuario);

        // Pasar los registros a la vista (JSP) correspondiente según el rol
        request.setAttribute("registrosActividad", registros);

        switch (rol) {
            case "Administrador":
                request.getRequestDispatcher("DashboardAdministrador.jsp").forward(request, response);
                break;
            case "Empleado":
                request.getRequestDispatcher("DashboardEmpleado.jsp").forward(request, response);
                break;
            case "Despachador":
                request.getRequestDispatcher("DashboardDespachador.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("login.jsp"); // Si el rol no es reconocido
                break;
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
        return "Servlet para mostrar los últimos registros de actividad en el dashboard según el rol del usuario";
    }
}
