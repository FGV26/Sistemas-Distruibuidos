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

@WebServlet(name = "GestionEmpleados", urlPatterns = {"/GestionEmpleados"})
public class ControlerEmpleados extends HttpServlet {

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
                listarEmpleados(request, response);
                break;
            case "Buscar":
                buscarEmpleado(request, response, user);
                break;
            case "Crear":
                crearEmpleado(request, response, user);
                break;
            case "Modificar":
                modificarEmpleado(request, response, user);
                break;
            case "Eliminar":
                eliminarEmpleado(request, response, user);
                break;
            default:
                response.sendRedirect("GestionEmpleados.jsp");
                break;
        }
    }

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Usuario> listaEmpleados = usuarioDAO.listarEmpleados();
        request.setAttribute("ListaEmpleados", listaEmpleados);
        request.getRequestDispatcher("GestionEmpleados.jsp").forward(request, response);
    }

    private void buscarEmpleado(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String parametroBusqueda = request.getParameter("nombre");

        if (parametroBusqueda == null || parametroBusqueda.isEmpty()) {
            response.sendRedirect("GestionEmpleados?accion=Listar&error=Debe+ingresar+un+nombre+o+usuario+para+buscar");
            return;
        }

        List<Usuario> empleados = usuarioDAO.buscarEmpleadosPorNombreOUsername(parametroBusqueda);

        if (empleados != null && !empleados.isEmpty()) {
            request.setAttribute("ListaEmpleados", empleados);
            request.getRequestDispatcher("GestionEmpleados.jsp").forward(request, response);

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Búsqueda");
            registro.setDescripcion("El administrador buscó un empleado con nombre o usuario: " + parametroBusqueda);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionEmpleados?accion=Listar&error=Empleado+no+encontrado");
        }
    }

    private void crearEmpleado(HttpServletRequest request, HttpServletResponse response, Usuario user)
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

            response.sendRedirect("GestionEmpleados?accion=Listar&error=Datos+incompletos");
            return;
        }

        // Crear el empleado sin encriptación en la contraseña
        Usuario nuevoEmpleado = new Usuario(username, password, email, "Empleado", nombre, apellido, "Activo");
        boolean exito = usuarioDAO.agregarEmpleado(nuevoEmpleado);

        if (exito) {
            response.sendRedirect("GestionEmpleados?accion=Listar&mensaje=Empleado+agregado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Creación");
            registro.setDescripcion("El administrador creó un nuevo empleado: " + username);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionEmpleados?accion=Listar&error=Error+al+agregar+empleado");
        }
    }

    private void modificarEmpleado(HttpServletRequest request, HttpServletResponse response, Usuario user)
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

            response.sendRedirect("GestionEmpleados?accion=Listar&error=Datos+incompletos");
            return;
        }

        // En este caso, la contraseña no se encripta
        Usuario empleado = new Usuario(id, username, password, email, "Empleado", nombre, apellido, estado);
        boolean exito = usuarioDAO.actualizarEmpleado(empleado);

        if (exito) {
            response.sendRedirect("GestionEmpleados?accion=Listar&mensaje=Empleado+modificado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Modificación");
            registro.setDescripcion("El administrador modificó el empleado: " + username);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionEmpleados?accion=Listar&error=Error+al+modificar+empleado");
        }
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("Id"));
        boolean exito = usuarioDAO.eliminarEmpleado(id);

        if (exito) {
            response.sendRedirect("GestionEmpleados?accion=Listar&mensaje=Empleado+eliminado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Eliminación");
            registro.setDescripcion("El administrador eliminó un empleado con ID: " + id);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionEmpleados?accion=Listar&error=Error+al+eliminar+empleado");
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
        return "Controlador para gestionar empleados";
    }
}
