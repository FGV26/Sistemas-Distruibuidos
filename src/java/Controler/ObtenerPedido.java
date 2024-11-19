package Controler;

import dao.PedidoDAO;
import dao.DetallePedidoDAO;
import dao.RegistroActividadDAO;
import entidades.Pedido;
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

@WebServlet(name = "ObtenerPedido", urlPatterns = {"/ObtenerPedido"})
public class ObtenerPedido extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar si el empleado está autenticado
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
            case "AsinarPedido":
                asinarPedido(request, response, user);
                break;
            default:
                response.sendRedirect("ObtenerPedido.jsp");
                break;
        }
    }

    // Listar pedidos creados por el empleado
    private void listarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        // Obtener pedidos disponibles
        List<Pedido> listaPedidos = pedidoDAO.listarPedidosDisponibles();

        if (listaPedidos == null || listaPedidos.isEmpty()) {
            request.setAttribute("mensaje", "No hay pedidos disponibles para asignar.");
        } else {
            request.setAttribute("ListaPedidos", listaPedidos);
        }

        // Redirigir al JSP con los pedidos disponibles
        request.getRequestDispatcher("ObtenerPedido.jsp").forward(request, response);
    }

    // Buscar pedidos por el nombre del cliente
    private void buscarPedidos(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombreCliente = request.getParameter("nombreCliente");

        // Validación de nombre ingresado
        if (nombreCliente == null || nombreCliente.isEmpty()) {
            response.sendRedirect("ObtenerPedido?accion=Listar&error=Debe+ingresar+el+nombre+del+cliente");
            return;
        }

        // Buscar pedidos por nombre del cliente
        List<Pedido> pedidos = pedidoDAO.buscarPedidosPorNombreClienteDisponibles(nombreCliente);

        if (pedidos != null && !pedidos.isEmpty()) {
            request.setAttribute("ListaPedidos", pedidos);
        } else {
            request.setAttribute("error", "No se encontraron pedidos para el cliente especificado.");
        }

        // Redirigir al JSP con los resultados de la búsqueda
        request.getRequestDispatcher("ObtenerPedido.jsp").forward(request, response);
    }

    private void asinarPedido(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");

        // Validación del ID de pedido
        if (idPedidoParam == null || idPedidoParam.isEmpty()) {
            response.sendRedirect("ObtenerPedido?accion=Listar&error=ID+de+pedido+inválido");
            return;
        }

        try {
            int idPedido = Integer.parseInt(idPedidoParam);

            // Intentar asignar el pedido al despachador
            boolean success = pedidoDAO.asignarPedido(idPedido, user.getIdUsuario());

            if (success) {
                registrarActividad("Asignación", "Se Asigno el Pedido " + idPedido + " asignado al Despachador " + user.getNombre(), user);
                response.sendRedirect("ObtenerPedido?accion=Listar&mensaje=Pedido+asignado+correctamente");
            } else {
                response.sendRedirect("ObtenerPedido?accion=Listar&error=No+se+pudo+asignar+el+pedido");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("ObtenerPedido?accion=Listar&error=ID+de+pedido+no+válido");
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
        return "Servlet para obtener pedidos de los despachadores";
    }
}
