package Controler;

import dao.ClienteDAO;  // Asegúrate de tener el DAO implementado
import entidades.Cliente;
import entidades.Usuario;
import conexion.conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControlerCliente", urlPatterns = {"/ControlerCliente"})
public class ControlerCliente extends HttpServlet {

    // Instancias necesarias (puedes ajustarlas según tu lógica)
    ClienteDAO clienteDAO = new ClienteDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificar si el usuario está autenticado
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Obtener la acción solicitada
        String accion = request.getParameter("accion");

        // Definir el switch para las operaciones
        switch (accion) {
            case "Listar":
                listarClientes(request, response);
                break;
            case "Nuevo":
                nuevoCliente(request, response);
                break;
            case "Crear":
                crearCliente(request, response);
                break;
            default:
                // Por defecto, redirigir a la página principal de clientes
                response.sendRedirect("Clientes.jsp");
                break;
        }
    }

    /**
     * Método para listar clientes.
     */
    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cliente> listaClientes = new ArrayList<>();
        ClienteDAO clienteDAO = new ClienteDAO();  // Instancia de ClienteDAO
        listaClientes = clienteDAO.listar();  // Obtener lista de clientes de la base de datos
        request.setAttribute("Lista", listaClientes);
        request.getRequestDispatcher("listar.jsp").forward(request, response);  // Enviar lista a la JSP
    }

    private void nuevoCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a la página de nuevo cliente
        request.getRequestDispatcher("nuevo.jsp").forward(request, response);
    }

    private void crearCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger los datos del formulario de nuevo cliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("DNI");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Crear instancia del cliente con los datos obtenidos
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setDni(dni);
        nuevoCliente.setDireccion(direccion);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setEmail(email);

        ClienteDAO clienteDAO = new ClienteDAO();  // Instancia de ClienteDAO
        clienteDAO.insertar(nuevoCliente);  // Insertar el nuevo cliente
        response.sendRedirect("ControlerCliente?accion=Listar");  // Redirigir al listado de clientes
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
        return "Controlador para gestionar clientes";
    }
}
