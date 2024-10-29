
package Controler;

import dao.UsuarioDAO; // Asegúrate de tener importado el DAO
import entidades.Usuario; // Asegúrate de importar la entidad correcta
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
        
        // 1. Obtener los parámetros del formulario
        String user = request.getParameter("txtUsuario");
        String pass = request.getParameter("txtClave");
        
        // 2. Instanciar el DAO y buscar el usuario por nombre de usuario
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorUsername(user);  // Aquí usamos el método que busca por el username

        // 3. Validar si el usuario existe y si la contraseña es correcta
        if (usuario != null && usuario.getPassword().equals(pass)) {
            // Si es correcto, creamos la sesión
            HttpSession session = request.getSession();
            session.setAttribute("user", usuario);  // Guardamos el objeto Usuario en la sesión
            request.getRequestDispatcher("index.jsp").forward(request, response);  // Redirigir a la página principal
        } else {
            // Si falla, redirigir al error de login
            request.getRequestDispatcher("ErrorLogin.jsp").forward(request, response);
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
        return "Servlet para validar el login";
    }
}
