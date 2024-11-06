
package Controler;

import dao.RegistroActividadDAO;
import dao.UsuarioDAO;
import entidades.RegistroActividad;
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "GestionDespachadores", urlPatterns = {"/GestionDespachadores"})
public class ControlerDespachadores extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !user.getRol().equals("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Listar":
                listarDespachadores(request, response);
                break;
            case "Buscar":
                buscarDespachador(request, response, user);
                break;
            case "Crear":
                crearDespachador(request, response, user);
                break;
            case "Modificar":
                modificarDespachador(request, response, user);
                break;
            case "Eliminar":
                eliminarDespachador(request, response, user);
                break;
            default:
                response.sendRedirect("GestionDespachadores.jsp");
                break;
        }
    }

    private void listarDespachadores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Usuario> listaDespachadores = usuarioDAO.listarDespachadores();
        request.setAttribute("ListaDespachadores", listaDespachadores);
        request.getRequestDispatcher("GestionDespachadores.jsp").forward(request, response);
    }

    private void buscarDespachador(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String parametroBusqueda = request.getParameter("nombre");

        if (parametroBusqueda == null || parametroBusqueda.isEmpty()) {
            response.sendRedirect("GestionDespachadores?accion=Listar&error=Debe+ingresar+un+nombre+o+usuario+para+buscar");
            return;
        }

        List<Usuario> despachadores = usuarioDAO.buscarDespachadoresPorNombreOUsername(parametroBusqueda);

        if (despachadores != null && !despachadores.isEmpty()) {
            request.setAttribute("ListaDespachadores", despachadores);
            request.getRequestDispatcher("GestionDespachadores.jsp").forward(request, response);

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Búsqueda");
            registro.setDescripcion("El administrador buscó un despachador con nombre o usuario: " + parametroBusqueda);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario()); // Asociamos la actividad al usuario que la realizó
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionDespachadores?accion=Listar&error=Despachador+no+encontrado");
        }
    }

    private void crearDespachador(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");

        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || email == null || email.isEmpty()
                || nombre == null || nombre.isEmpty()
                || apellido == null || apellido.isEmpty()) {

            response.sendRedirect("GestionDespachadores?accion=Listar&error=Datos+incompletos");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario nuevoDespachador = new Usuario(username, hashedPassword, email, "Despachador", nombre, apellido, "Activo");
        boolean exito = usuarioDAO.agregarDespachador(nuevoDespachador);

        if (exito) {
            response.sendRedirect("GestionDespachadores?accion=Listar&mensaje=Despachador+agregado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Creación");
            registro.setDescripcion("El administrador creó un nuevo despachador: " + username);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionDespachadores?accion=Listar&error=Error+al+agregar+despachador");
        }
    }

    private void modificarDespachador(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String idString = request.getParameter("idUsuario");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String estado = request.getParameter("estado");

        int id = Integer.parseInt(idString);

        if (username == null || username.isEmpty()
                || email == null || email.isEmpty()
                || nombre == null || nombre.isEmpty()
                || apellido == null || apellido.isEmpty()
                || estado == null || estado.isEmpty()) {

            response.sendRedirect("GestionDespachadores?accion=Listar&error=Datos+incompletos");
            return;
        }

        Usuario despachadorActual = usuarioDAO.obtenerEmpleadoPorId(id);
        String hashedPassword = (password != null && !password.isEmpty() && !BCrypt.checkpw(password, despachadorActual.getPassword()))
                ? BCrypt.hashpw(password, BCrypt.gensalt()) : despachadorActual.getPassword();

        Usuario despachador = new Usuario(id, username, hashedPassword, email, "Despachador", nombre, apellido, estado);
        boolean exito = usuarioDAO.actualizarDespachador(despachador);

        if (exito) {
            response.sendRedirect("GestionDespachadores?accion=Listar&mensaje=Despachador+modificado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Modificación");
            registro.setDescripcion("El administrador modificó el despachador: " + username);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionDespachadores?accion=Listar&error=Error+al+modificar+despachador");
        }
    }

    private void eliminarDespachador(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("Id"));
        boolean exito = usuarioDAO.eliminarDespachador(id);

        if (exito) {
            response.sendRedirect("GestionDespachadores?accion=Listar&mensaje=Despachador+eliminado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Eliminación");
            registro.setDescripcion("El administrador eliminó un despachador con ID: " + id);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionDespachadores?accion=Listar&error=Error+al+eliminar+despachador");
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
        return "Controlador para gestionar despachadores";
    }
}
