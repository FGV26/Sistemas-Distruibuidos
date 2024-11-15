package Controler;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.PedidoDAO;
import dao.RegistroActividadDAO;
import entidades.Cliente;
import entidades.RegistroActividad;
import entidades.Usuario;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dao.CategoriaDAO;
import dao.DetallePedidoDAO;
import entidades.Categoria;
import entidades.DetallePedido;
import entidades.Pedido;
import entidades.Producto;
import java.io.BufferedReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "GestionDePedidos", urlPatterns = {"/GestionDePedidos"})
public class GestionDePedidos extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
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
                response.sendRedirect("GestionDePedido.jsp");
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
            case "BuscarProductoPorNombre":
                buscarProductoPorNombre(request, response);
                break;
            case "ListarPorCategoria":
                listarPorCategoria(request, response);
                break;
            case "ListarCategorias":
                listarCategorias(request, response);
                break;
            case "GenerarCodigoPedido":
                generarCodigoPedido(response);
                break;
            case "CrearPedido":
                crearPedido(request, response, user); // Llamada al método crearPedido
                break;
            default:
                response.sendRedirect("GestionDePedido.jsp");
                break;
        }
    }

    private void buscarCliente(HttpServletRequest request, HttpServletResponse response, Usuario user) throws IOException {
        try {
            if (user == null) {
                response.sendRedirect("GestionDePedido.jsp?error=Usuario+no+autenticado");
                return;
            }

            String dni = request.getParameter("dni");

            System.out.println("dni:" + dni);

            if (dni == null) {
                return;
            }

            Cliente cliente = clienteDAO.obtenerClientePorDni(dni);

            if (cliente == null) {
                return;
            };

            String clienteJson = gson.toJson(cliente);
            response.setContentType("application/json");
            response.getWriter().write(clienteJson);
            
            registrarActividad("Búsqueda", "El empleado buscó un cliente por DNI: " + dni, user);

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
                registrarActividad("Creación", "El empleado creó un nuevo cliente con DNI: " + dni, user);
                response.getWriter().write("{\"mensaje\": \"Cliente registrado correctamente\", \"codCliente\": \"" + cliente.getCodCliente() + "\"}");
            } else {
                response.getWriter().write("{\"error\": \"Error al crear cliente\"}");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al procesar solicitud\"}");
        }
    }

    // Acción para listar todos los productos con stock
    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Producto> productos = productoDAO.listarConStock();
            String productosJson = gson.toJson(productos);
            response.setContentType("application/json");
            response.getWriter().write(productosJson);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al listar productos\"}");
        }
    }

    // Acción para buscar productos por nombre
    private void buscarProductoPorNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        try {
            if (nombre != null && !nombre.isEmpty()) {
                List<Producto> productos = productoDAO.buscarProductosConStock(nombre);
                String productosJson = gson.toJson(productos);
                response.setContentType("application/json");
                response.getWriter().write(productosJson);
            } else {
                response.getWriter().write("{\"error\": \"Nombre de producto vacío\"}");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al buscar productos por nombre\"}");
        }
    }

    // Acción para listar productos por categoría
    private void listarPorCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String categoria = request.getParameter("categoria");
        try {
            if (categoria != null && !categoria.isEmpty()) {
                List<Producto> productos = productoDAO.listarPorCategoria(categoria);
                String productosJson = gson.toJson(productos);
                response.setContentType("application/json");
                response.getWriter().write(productosJson);
            } else {
                response.getWriter().write("{\"error\": \"Categoría no seleccionada\"}");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al listar productos por categoría\"}");
        }
    }

    // Acción para listar categorías
    private void listarCategorias(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Categoria> categorias = categoriaDAO.listar(); // Suponiendo que tienes un método listar en CategoriaDAO
            String categoriasJson = gson.toJson(categorias);
            response.setContentType("application/json");
            response.getWriter().write(categoriasJson);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al listar categorías\"}");
        }
    }

    private void generarCodigoPedido(HttpServletResponse response) throws IOException {
        try {
            // Obtener el último ID de pedido
            int ultimoId = pedidoDAO.obtenerUltimoId();
            int nuevoId = ultimoId + 1; // Incrementamos en 1 para el nuevo pedido

            // Generar el código de pedido en formato PEDI### (con tres dígitos)
            String codPedido = "PEDI" + String.format("%03d", nuevoId);

            // Devolver el código en formato JSON al frontend
            response.setContentType("application/json");
            response.getWriter().write("{\"codPedido\": \"" + codPedido + "\"}");

        } catch (IOException e) {
            e.printStackTrace(System.out);
            response.getWriter().write("{\"error\": \"Error al generar el código de pedido\"}");
        }
    }

    private void crearPedido(HttpServletRequest request, HttpServletResponse response, Usuario user) throws IOException {
        try {
            // Leer los parámetros enviados
            String pedidoJson = request.getParameter("pedido");
            String detallesJson = request.getParameter("detalles");

            // Verificar si los parámetros no son nulos
            if (pedidoJson == null || detallesJson == null) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Datos incompletos enviados al servidor.\"}");
                return;
            }

            // Parsear JSON a objetos
            Pedido pedido = gson.fromJson(pedidoJson, Pedido.class);
            DetallePedido[] detallesArray = gson.fromJson(detallesJson, DetallePedido[].class);
            List<DetallePedido> detalles = Arrays.asList(detallesArray);

            // Asignar el idEmpleado desde el usuario logueado
            pedido.setIdEmpleado(user.getIdUsuario());

            // Generar y asignar el código de pedido
            String codPedido = generarCodigoPedido(); 
            pedido.setCodPedido(codPedido);

            // Asegurar que cada detalle tenga su total calculado (precio * cantidad)
            for (DetallePedido detalle : detalles) {
                BigDecimal cantidad = new BigDecimal(detalle.getCantidad());
                BigDecimal total = detalle.getPrecio().multiply(cantidad);
                detalle.setTotal(total);
            }

            // Insertar el pedido y procesar detalles (descomentar para insertar en la base de datos)
            int idPedido = pedidoDAO.insertarPedido(pedido);
            if (idPedido > 0) {
                for (DetallePedido detalle : detalles) {
                    detalle.setIdPedido(idPedido);
                }

                boolean detallesExito = detallePedidoDAO.insertarDetalle(idPedido, detalles);
                if (detallesExito) {
                    boolean stockExito = true;

                    for (DetallePedido detalle : detalles) {
                        int affectedRows = productoDAO.actualizarStockProducto(detalle.getIdProducto(), detalle.getCantidad());
                        if (affectedRows <= 0) {
                            stockExito = false;
                            break;
                        }
                    }

                    if (stockExito) {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": true, \"message\": \"Pedido creado exitosamente.\"}");
                    } else {
                        response.setContentType("application/json");
                        response.getWriter().write("{\"success\": false, \"message\": \"Error al actualizar el stock.\"}");
                
                    }
                registrarActividad("Creacion", "El empleado Creo un Pedido con el codigo de: " + pedido.getCodPedido(), user);
                
                } else {
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\": false, \"message\": \"Error al insertar detalles.\"}");
                }
            } else {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Error al insertar el pedido.\"}");
            }
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace(System.out);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Error al procesar la solicitud.\"}");
        }
        
    }
    
    // Método privado para generar código de pedido
    private String generarCodigoPedido() {
        try {
            int ultimoId = pedidoDAO.obtenerUltimoId();
            int nuevoId = ultimoId + 1;
            return "PEDI" + String.format("%03d", nuevoId);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return null; // O devuelve un valor predeterminado
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
        return "Servlet para gestionar pedidos y clientes";
    }
}
