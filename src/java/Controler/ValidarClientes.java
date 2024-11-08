
package Controler;

import com.google.gson.Gson;
import dao.ClienteDAO;
import dao.RegistroActividadDAO;
import entidades.Cliente;
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

@WebServlet(name = "ValidarClientes", urlPatterns = {"/ValidarClientes"})
public class ValidarClientes extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !user.getRol().equals("Empleado")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Buscar":
                buscarCliente(request, response, user);
                break;
            case "Crear":
                crearCliente(request, response, user);
                break;
            case "AutoCompletar":
                autocompletarCliente(request, response);
                break;
            case "Ingresar":
                ingresarCliente(request, response);
                break;
            default:
                response.sendRedirect("CrearClientes.jsp");
                break;
        }
    }

    private void ingresarCliente(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Ya ingresé al servlet ValidarClientes con acción Ingresar.");
    }

    private void buscarCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String dni = request.getParameter("dni");

        if (dni == null || dni.isEmpty()) {
            response.sendRedirect("CrearClientes.jsp?error=Debe+ingresar+un+DNI+para+buscar");
            return;
        }

        Cliente cliente = clienteDAO.obtenerClientePorDni(dni);

        if (cliente != null) {
            request.setAttribute("Cliente", cliente);
            request.getRequestDispatcher("CrearClientes.jsp").forward(request, response);

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Búsqueda");
            registro.setDescripcion("El empleado buscó un cliente con DNI: " + dni);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("CrearClientes.jsp?error=Cliente+no+encontrado");
        }
    }

    private void crearCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String direccion = request.getParameter("direccion");
        String dni = request.getParameter("dni");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        int idEmpleado = user.getIdUsuario();

        if (nombre == null || nombre.isEmpty()
                || apellido == null || apellido.isEmpty()
                || direccion == null || direccion.isEmpty()
                || dni == null || dni.isEmpty()
                || telefono == null || telefono.isEmpty()
                || email == null || email.isEmpty()) {

            response.sendRedirect("CrearClientes.jsp?error=Datos+incompletos");
            return;
        }

        Cliente nuevoCliente = new Cliente(nombre, apellido, direccion, dni, telefono, email, idEmpleado);
        int resultado = clienteDAO.insertar(nuevoCliente);

        if (resultado > 0) {
            response.sendRedirect("CrearClientes.jsp?mensaje=Cliente+creado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Creación");
            registro.setDescripcion("El empleado creó un nuevo cliente con DNI: " + dni);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("CrearClientes.jsp?error=Error+al+crear+cliente");
        }
    }

    private void autocompletarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String dniParcial = request.getParameter("dni");

        if (dniParcial != null && !dniParcial.trim().isEmpty()) {
            // Llama al DAO para obtener coincidencias de clientes por DNI parcial
            List<Cliente> coincidencias = clienteDAO.buscarClientesPorDniParcial(dniParcial);

            // Convierte la lista a JSON usando Gson
            Gson gson = new Gson();
            String json = gson.toJson(coincidencias);

            // Configura la respuesta
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else {
            response.getWriter().write("[]"); // Devuelve una lista vacía si no hay DNI
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
        return "Servlet para validar y gestionar clientes";
    }
}
