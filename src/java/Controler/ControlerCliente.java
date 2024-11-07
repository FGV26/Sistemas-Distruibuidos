package Controler;

import dao.ClienteDAO;
import entidades.Cliente;
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ControlerCliente", urlPatterns = {"/ControlerCliente"})
public class ControlerCliente extends HttpServlet {

    private ClienteDAO clienteDAO = new ClienteDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Listar":
                listarClientes(request, response);
                break;
            case "Consultar":
                consultarCliente(request, response);
                break;
            case "Modificar":
                modificarCliente(request, response);
                break;
//            case "Actualizar":
//                actualizarCliente(request, response);
//                break;
            case "Nuevo":
                nuevoCliente(request, response);
                break;
//            case "Crear":
//                crearCliente(request, response);
//                break;
            default:
                response.sendRedirect("Clientes.jsp");
                break;
        }
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cliente> listaClientes = clienteDAO.listar();
        request.setAttribute("Lista", listaClientes);
        request.getRequestDispatcher("listar.jsp").forward(request, response);
    }

    private void consultarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idConsultar = Integer.parseInt(request.getParameter("Id"));
        Cliente cliente = clienteDAO.obtenerClientePorId(idConsultar);
        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("consultar.jsp").forward(request, response);
    }

    private void modificarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idModificar = Integer.parseInt(request.getParameter("Id"));
        Cliente cliente = clienteDAO.obtenerClientePorId(idModificar);
        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("modificar.jsp").forward(request, response);
    }

//    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("Id"));
//        String nombre = request.getParameter("nombre");
//        String apellido = request.getParameter("apellido");
//        String dni = request.getParameter("DNI");
//        String direccion = request.getParameter("direccion");
//        String telefono = request.getParameter("telefono");
//        String email = request.getParameter("email");
//
//        Cliente cliente = new Cliente(id, nombre, apellido, dni, direccion, telefono, email);
//        clienteDAO.actualizarCliente(cliente);
//        response.sendRedirect("ControlerCliente?accion=Listar");
//    }

    private void nuevoCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("nuevo.jsp").forward(request, response);
    }

//    private void crearCliente(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String nombre = request.getParameter("nombre");
//        String apellido = request.getParameter("apellido");
//        String dni = request.getParameter("DNI");
//        String direccion = request.getParameter("direccion");
//        String telefono = request.getParameter("telefono");
//        String email = request.getParameter("email");
//
//        // Crear el cliente con el constructor correcto
//        Cliente nuevoCliente = new Cliente(nombre, apellido, direccion, dni, telefono, email);  // Revise el orden aquí
//        clienteDAO.insertar(nuevoCliente);
//        response.sendRedirect("ControlerCliente?accion=Listar");
//    }

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
