package Controler;

import com.google.gson.Gson;
import dao.PedidoDAO;
import dao.RegistroActividadDAO;
import entidades.DetallePedido;
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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ControlerPedido", urlPatterns = {"/ControlerPedido"})
public class ControlerPedido extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

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
            case "ActualizarEstado":
                actualizarEstadoPedido(request, response,user);
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
            registrarActividad("Búsqueda", "El Despachador buscó un cliente por el nombre de : " + nombreCliente, user);
        }
    }

    protected void obtenerDetallesPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoStr = request.getParameter("idPedido");

        try {
            int idPedido = Integer.parseInt(idPedidoStr);

            Pedido pedido = pedidoDAO.obtenerPedidoConDetalles(idPedido);

            if (pedido != null) {

                request.setAttribute("pedido", pedido);
                request.getRequestDispatcher("DetallesPedido.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "El pedido no fue encontrado.");
                request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {

            request.setAttribute("error", "ID de pedido inválido.");
            request.getRequestDispatcher("GestionPedido.jsp").forward(request, response);
        }
    }

    protected void actualizarEstadoPedido(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        try {
            // Obtener parámetros
            int idPedido = Integer.parseInt(request.getParameter("idPedido"));
            String estado = request.getParameter("estado");
            
            System.out.println("ID: " + idPedido);
            System.out.println("Estado: " + estado);
            // Validar que el estado sea válido
            List<String> estadosValidos = Arrays.asList("Proceso", "Leido", "Empaquetado", "Enviado", "Cancelado");
            if (!estadosValidos.contains(estado)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Estado inválido\"}");
                return;
            }

            // Actualizar estado en la base de datos
            int filasActualizadas = pedidoDAO.actualizarEstado(idPedido, estado); // Llamada al método DAO
            if (filasActualizadas > 0) {
                // Éxito: se actualizó el estado
                response.getWriter().write("{\"success\": true}");
                registrarActividad("Modificacion", "El Despachador cambio el estado del pedido" + idPedido+ "al estado "+estado, user);
            } else {
                // Error: no se actualizó el estado
                response.getWriter().write("{\"success\": false, \"message\": \"No se pudo actualizar el estado\"}");
            }
        } catch (NumberFormatException e) {
            // Error de formato en el ID del pedido
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"ID de pedido inválido\"}");
            e.printStackTrace(System.out);
        } catch (IOException e) {
            // Error general
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Error interno del servidor\"}");
            e.printStackTrace(System.out);
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
        return "Controlador para gestionar pedidos";
    }
}
