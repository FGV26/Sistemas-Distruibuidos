
package Controler;

import dao.PedidoDAO;
import dao.DetallePedidoDAO;
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

@WebServlet(name = "MisPedidos", urlPatterns = {"/MisPedidos"})
public class MisPedidos extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar si el empleado está autenticado
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !"Empleado".equals(user.getRol())) {
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
            case "Eliminar":
                eliminarPedido(request, response, user);
                break;
            default:
                response.sendRedirect("MisPedidos.jsp");
                break;
        }
    }

    // Listar pedidos creados por el empleado
    private void listarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        List<Pedido> listaPedidos = pedidoDAO.listarPedidosPorEmpleado(user.getIdUsuario(), null);
        request.setAttribute("ListaPedidos", listaPedidos);
        request.getRequestDispatcher("MisPedidos.jsp").forward(request, response);
    }

    // Buscar pedidos por el nombre del cliente
    private void buscarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombreCliente = request.getParameter("nombre");

        if (nombreCliente == null || nombreCliente.isEmpty()) {
            response.sendRedirect("MisPedidos?accion=Listar&error=Debe+ingresar+el+nombre+del+cliente");
            return;
        }

        List<Pedido> pedidos = pedidoDAO.buscarPedidosPorNombreCliente(user.getIdUsuario(), nombreCliente);

        if (pedidos != null && !pedidos.isEmpty()) {
            request.setAttribute("ListaPedidos", pedidos);
            request.getRequestDispatcher("MisPedidos.jsp").forward(request, response);
        } else {
            response.sendRedirect("MisPedidos?accion=Listar&error=No+se+encontraron+pedidos+para+el+cliente+especificado");
        }
    }

    // Eliminar un pedido
    private void eliminarPedido(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int idPedido = Integer.parseInt(request.getParameter("idPedido"));

        boolean detallesEliminados = detallePedidoDAO.eliminarDetallesPorPedido(idPedido);
        boolean pedidoEliminado = pedidoDAO.eliminarPedido(idPedido);

        if (detallesEliminados && pedidoEliminado) {
            response.sendRedirect("MisPedidos?accion=Listar&mensaje=Pedido+eliminado+correctamente");
        } else {
            response.sendRedirect("MisPedidos?accion=Listar&error=No+se+pudo+eliminar+el+pedido");
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
        return "Servlet para gestionar los pedidos de los empleados";
    }
}
