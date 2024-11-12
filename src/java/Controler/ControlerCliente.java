
package Controler;

import dao.ClienteDAO;
import dao.RegistroActividadDAO; // Importar el DAO de RegistroActividad
import entidades.Cliente;
import entidades.RegistroActividad; // Importar la entidad RegistroActividad
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ControlerCliente", urlPatterns = {"/ControlerCliente"})
public class ControlerCliente extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO(); // Instanciar RegistroActividadDAO

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");

        // Validación de sesión y rol
        if (user == null || !"Empleado".equals(user.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        switch (accion) {
            case "Listar":
                listarClientesPorEmpleado(request, response, user.getIdUsuario());
                break;
            case "Buscar":
                buscarClientes(request, response, user);
                break;
            case "Crear":
                crearCliente(request, response, user);
                break;
            case "Actualizar":
                actualizarCliente(request, response, user);
                break;
            case "Eliminar":
                eliminarCliente(request, response, user);
                break;
            default:
                response.sendRedirect("GestionMisClientes.jsp");
                break;
        }
    }

    // Método para registrar la actividad
    private void registrarActividad(String tipo, String descripcion, Usuario user) {
        RegistroActividad registro = new RegistroActividad();
        registro.setTipo(tipo);
        registro.setDescripcion(descripcion);
        registro.setFecha(LocalDateTime.now());
        registro.setIdUsuario(user.getIdUsuario());
        registroActividadDAO.insertar(registro);
    }

    private void listarClientesPorEmpleado(HttpServletRequest request, HttpServletResponse response, int idEmpleado)
            throws ServletException, IOException {
        List<Cliente> listaClientes = clienteDAO.listarClientesPorEmpleado(idEmpleado);
        request.setAttribute("listaClientes", listaClientes);
        request.getRequestDispatcher("GestionMisClientes.jsp").forward(request, response);
    }

    private void buscarClientes(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String dni = request.getParameter("dniBusqueda");
        List<Cliente> clientes = clienteDAO.buscarClientesPorDniYEmpleado(dni, user.getIdUsuario());
        request.setAttribute("listaClientes", clientes);
        request.getRequestDispatcher("GestionMisClientes.jsp").forward(request, response);

        // Registrar actividad de búsqueda
        registrarActividad("Búsqueda", "El empleado buscó un cliente por DNI: " + dni, user);
    }

    private void crearCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String direccion = request.getParameter("direccion");
        String dni = request.getParameter("dni");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        if (nombre == null || nombre.isEmpty()
                || apellido == null || apellido.isEmpty()
                || direccion == null || direccion.isEmpty()
                || dni == null || dni.isEmpty()
                || telefono == null || telefono.isEmpty()
                || email == null || email.isEmpty()) {

            request.setAttribute("error", "Todos los campos son obligatorios.");
            listarClientesPorEmpleado(request, response, user.getIdUsuario());
            return;
        }

        Cliente nuevoCliente = new Cliente(nombre, apellido, direccion, dni, telefono, email, user.getIdUsuario());
        int result = clienteDAO.insertar(nuevoCliente);

        if (result > 0) {
            response.sendRedirect("ControlerCliente?accion=Listar");

            // Registrar actividad de creación
            registrarActividad("Creación", "El empleado creó un nuevo cliente: " + nombre + " " + apellido, user);
        } else {
            request.setAttribute("error", "Hubo un problema al crear el cliente.");
            listarClientesPorEmpleado(request, response, user.getIdUsuario());
        }
    }

    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("Id"));
        String codCliente = request.getParameter("codCliente");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String direccion = request.getParameter("direccion");
        String dni = request.getParameter("dni");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        Cliente clienteExistente = clienteDAO.obtenerClientePorId(id);
        Timestamp fechaCreacion = clienteExistente.getFechaCreacion();

        Cliente cliente = new Cliente(id, nombre, apellido, direccion, dni, telefono, email, fechaCreacion, user.getIdUsuario(), codCliente);
        clienteDAO.actualizarCliente(cliente);

        response.sendRedirect("ControlerCliente?accion=Listar");

        // Registrar actividad de actualización
        registrarActividad("Modificación", "El empleado modificó al cliente con ID: " + id, user);
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("Id"));
        clienteDAO.eliminar(idEliminar);
        response.sendRedirect("ControlerCliente?accion=Listar");

        // Registrar actividad de eliminación
        registrarActividad("Eliminación", "El empleado eliminó al cliente con ID: " + idEliminar, user);
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
        return "Controlador para gestionar clientes de empleados específicos";
    }
}

