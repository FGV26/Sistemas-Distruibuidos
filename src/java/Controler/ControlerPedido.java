package Controler;

import dao.PedidoDAO;
import entidades.DetallePedido;
import entidades.Pedido;
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
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Listar":
                listarPedidos(request, response);
                break;
            case "Consultar":
                consultarPedido(request, response);
                break;
            case "Eliminar":
                eliminarPedido(request, response);
                break;
            case "Nuevo":
                nuevoPedido(request, response);
                break;
            case "Crear":
                crearPedido(request, response);
                break;
            default:
                response.sendRedirect("Pedidos.jsp");
                break;
        }
    }

    private void listarPedidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Pedido> listaPedidos = pedidoDAO.listar();
        request.setAttribute("Lista", listaPedidos);
        request.getRequestDispatcher("listarPedido.jsp").forward(request, response);
    }

    private void consultarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        int idPedido = Integer.parseInt(request.getParameter("Id"));
//        List<DetallePedido> detalles = pedidoDAO.obtenerDetallesPorIdPedido(idPedido);
//        request.setAttribute("Lista", detalles);
//        request.getRequestDispatcher("consultarPedido.jsp").forward(request, response);
    }

    private void eliminarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        int idPedido = Integer.parseInt(request.getParameter("Id"));
//        pedidoDAO.eliminar(idPedido);
//        response.sendRedirect("ControlerPedido?accion=Listar");
    }

    private void nuevoPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("nuevoPedido.jsp").forward(request, response);
    }

    private void crearPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
//        double subTotal = Double.parseDouble(request.getParameter("subTotal"));
//        double totalVenta = Double.parseDouble(request.getParameter("totalVenta"));
//
//        Pedido nuevoPedido = new Pedido(idCliente, subTotal, totalVenta);
//        pedidoDAO.insertar(nuevoPedido);
//        response.sendRedirect("ControlerPedido?accion=Listar");
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
