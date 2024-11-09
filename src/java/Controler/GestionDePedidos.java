package Controler;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.PedidoDAO;
import dao.RegistroActividadDAO;
import entidades.Cliente;
import entidades.Producto;
import entidades.Pedido;
import entidades.DetallePedido;
import entidades.RegistroActividad;
import entidades.Usuario;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "GestionDePedidos", urlPatterns = {"/GestionDePedidos"})
public class GestionDePedidos extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();
    private final Gson gson = new Gson();

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
            case "Ingresar":
                System.out.println("Ingresé al servlet con acción: Ingresar");
                response.sendRedirect("GestionDePedido.jsp");  // Redirige a la página JSP de gestión de pedidos
                break;
            case "BuscarCliente":
                buscarCliente(request, response, user);
                break;
            case "CrearCliente":
                crearCliente(request, response, user);
                break;
            case "ListarProductos":
                listarProductos(request, response);
                break;
            case "Filtros":
                listarProductosPorCategoria(request, response);
                break;
            case "BusquedaDeProducto":
                buscarProducto(request, response);
                break;
            case "GenerarPagoTotal":
                generarPagoTotal(request, response);
                break;
            case "GenerarPedido":
                generarPedido(request, response, user);
                break;
            default:
                response.sendRedirect("GestionDePedido.jsp");
                break;
        }
    }

    private void buscarCliente(HttpServletRequest request, HttpServletResponse response, Usuario user) throws IOException {
        try{
            if (user == null) {
                response.sendRedirect("GestionDePedido.jsp?error=Usuario+no+autenticado");
                return;
            }
            
            String dni = request.getParameter("dni");
            
            System.out.println("dni:" + dni);
            
            if(dni == null){
                return;
            }
            
            Cliente cliente = clienteDAO.obtenerClientePorDni(dni);
            
            if(cliente == null) {
                return;
            };
            
            String clienteJson = gson.toJson(cliente);
            response.setContentType("application/json");
            response.getWriter().write(clienteJson);
            
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al procesar solicitud\"}");
        }
    }


    private void crearCliente(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws IOException {
        try {
            if (user == null) {
                response.sendRedirect("GestionDePedido.jsp?error=Usuario+no+autenticado");
                return;
            }

            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String direccion = request.getParameter("direccion");
            String dni = request.getParameter("dni");
            String telefono = request.getParameter("telefono");
            String email = request.getParameter("email");

            if (nombre == null || apellido == null || direccion == null || dni == null || telefono == null || email == null
                    || nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || dni.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
                response.sendRedirect("GestionDePedido.jsp?error=Datos+incompletos");
                return;
            }

            int nuevoId = clienteDAO.obtenerUltimoIdCliente();
            if (nuevoId < 0) {
                response.getWriter().write("{\"error\": \"Error al obtener último ID del cliente\"}");
                return;
            }

            nuevoId++;
            String codCliente = "CLIEN" + String.format("%03d", nuevoId);

            Cliente cliente = new Cliente();
            cliente.setCodCliente(codCliente);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setDireccion(direccion);
            cliente.setDni(dni);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setIdEmpleado(user.getIdUsuario());

            int resultado = clienteDAO.insertar(cliente);
            if (resultado > 0) {
                registrarActividad("Creación", "El empleado creó un nuevo cliente con DNI: " + dni, user.getIdUsuario());
                response.getWriter().write("{\"mensaje\": \"Cliente registrado correctamente\", \"codCliente\": \"" + cliente.getCodCliente() + "\"}");
            } else {
                response.getWriter().write("{\"error\": \"Error al crear cliente\"}");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al procesar solicitud\"}");
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Producto> productos = productoDAO.listar();
        response.getWriter().write(gson.toJson(productos));
    }

    private void listarProductosPorCategoria(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String categoria = request.getParameter("categoria");
        List<Producto> productos = productoDAO.listarPorCategoria(categoria);
        response.getWriter().write(gson.toJson(productos));
    }

    private void buscarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String nombre = request.getParameter("nombre");
        List<Producto> productos = productoDAO.buscarPorNombre(nombre);
        response.getWriter().write(gson.toJson(productos));
    }

    private void generarPagoTotal(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        List<DetallePedido> carrito = (List<DetallePedido>) session.getAttribute("carrito");

        double subTotal = carrito.stream().mapToDouble(DetallePedido::getTotal).sum();
        double igv = subTotal * 0.18;
        double total = subTotal + igv;

        String resumen = "{ \"subTotal\": " + subTotal + ", \"igv\": " + igv + ", \"total\": " + total + " }";
        response.getWriter().write(resumen);
    }

    private void generarPedido(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws IOException {
        HttpSession session = request.getSession();
        List<DetallePedido> carrito = (List<DetallePedido>) session.getAttribute("carrito");
        int idCliente = (int) session.getAttribute("idCliente");

        double subTotal = carrito.stream().mapToDouble(DetallePedido::getTotal).sum();
        double igv = subTotal * 0.18;
        double total = subTotal + igv;

        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);
        pedido.setIdEmpleado(user.getIdUsuario());
        pedido.setSubTotal(subTotal);
        pedido.setTotal(total);
        pedido.setEstado("Proceso");

        int idPedido = pedidoDAO.insertarPedido(pedido);
        if (idPedido > 0) {
            for (DetallePedido detalle : carrito) {
                detalle.setIdPedido(idPedido);
            }
            pedidoDAO.insertarDetallePedido(carrito);
            session.removeAttribute("carrito");
            registrarActividad("Generación de Pedido", "El empleado generó un pedido para el cliente con ID: " + idCliente, user.getIdUsuario());
            response.sendRedirect("GestionDePedido.jsp?mensaje=Pedido+generado+exitosamente");
        } else {
            response.sendRedirect("GestionDePedido.jsp?error=Error+al+generar+el+pedido");
        }
    }

    private void registrarActividad(String tipo, String descripcion, int idUsuario) {
        RegistroActividad registro = new RegistroActividad();
        registro.setTipo(tipo);
        registro.setDescripcion(descripcion);
        registro.setFecha(LocalDateTime.now());
        registro.setIdUsuario(idUsuario);
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
        return "Servlet para gestionar pedidos y clientes";
    }
}
