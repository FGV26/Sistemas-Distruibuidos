package Controler;

import com.google.gson.Gson;
import dao.PedidoDAO;
import entidades.DetallePedido;
import entidades.Pedido;
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ControlerPedido", urlPatterns = {"/ControlerPedido"})
public class ControlerPedido extends HttpServlet {

    private PedidoDAO pedidoDAO = new PedidoDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !"Despachador".equals(user.getRol())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Listar":
                listarPedidos(request, response, user);
                break;
            case "Buscar":
                buscarPedidos(request, response, user);
                break;
            case "ObtenerDetalles":
                obtenerDetallesPedido(request, response);
                break;
            default:
                response.sendRedirect("GestionPedido.jsp");
                break;
        }
    }

    private void listarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {

        List<Pedido> listaPedidos = pedidoDAO.listarPedidosPorDespachador(user.getIdUsuario());

        if (listaPedidos == null || listaPedidos.isEmpty()) {
            request.setAttribute("mensaje", "No hay pedidos disponibles para asignar.");
        } else {
            request.setAttribute("ListaPedidos", listaPedidos);
        }
        request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
    }

    private void buscarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {

        String nombreCliente = request.getParameter("nombreCliente");

        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            request.setAttribute("error", "Debe ingresar un nombre para buscar.");
            listarPedidos(request, response, user);
            return;
        }

        List<Pedido> listaPedidos = pedidoDAO.buscarPedidosPorNombreCliente(user.getIdUsuario(), nombreCliente.trim());

        if (listaPedidos == null || listaPedidos.isEmpty()) {
            request.setAttribute("error", "No se encontraron pedidos para el cliente especificado. Mostrando todos los pedidos.");
            listarPedidos(request, response, user);
        } else {
            request.setAttribute("ListaPedidos", listaPedidos);
            request.setAttribute("mensaje", "Resultados de la búsqueda para: " + nombreCliente);
            request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
        }
    }

    protected void obtenerDetallesPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoStr = request.getParameter("idPedido");

        try {
            int idPedido = Integer.parseInt(idPedidoStr);

            // Obtener el pedido con sus detalles desde la capa DAO
            Pedido pedido = pedidoDAO.obtenerPedidoConDetalles(idPedido);

            if (pedido != null) {
                // Pasar el pedido como atributo de la solicitud
                request.setAttribute("pedido", pedido);
                
                // Redirigir al JSP de DetallesPedido.jsp
                request.getRequestDispatcher("DetallesPedido.jsp").forward(request, response);
            } else {
                // Manejar caso de error si no se encuentra el pedido
                request.setAttribute("error", "El pedido no fue encontrado.");
                request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // Manejar caso de error si el ID no es válido
            
            request.setAttribute("error", "ID de pedido inválido.");
            request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
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
        return "Controlador para gestionar pedidos";
    }
}
