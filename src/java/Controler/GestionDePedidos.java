package Controler;

import dao.ClienteDAO;
import dao.ProductoDAO;
import dao.PedidoDAO;
import dao.RegistroActividadDAO;
import entidades.Cliente;
import entidades.RegistroActividad;
import entidades.Usuario;
import com.google.gson.Gson;
import dao.CategoriaDAO;
import entidades.Categoria;
import entidades.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "GestionDePedidos", urlPatterns = {"/GestionDePedidos"})
public class GestionDePedidos extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
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
//            case "ObtenerUltimoIdPedido":
//                obtenerUltimoIdPedido(request, response);
//                break;
//            case "CrearPedido":
//                crearPedido(request, response, user);
//                break;
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

    private void registrarActividad(String tipo, String descripcion, int idUsuario) {
        RegistroActividad registro = new RegistroActividad();
        registro.setTipo(tipo);
        registro.setDescripcion(descripcion);
        registro.setFecha(LocalDateTime.now());
        registro.setIdUsuario(idUsuario);
        registroActividadDAO.insertar(registro);
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

//    // Acción para obtener el último ID de pedido
//    private void obtenerUltimoIdPedido(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        try {
//            int ultimoId = pedidoDAO.obtenerUltimoIdPedido(); // Suponiendo que tienes este método en PedidoDAO
//            String jsonResponse = gson.toJson(new HashMap<String, Integer>() {
//                {
//                    put("ultimoId", ultimoId);
//                }
//            });
//            response.setContentType("application/json");
//            response.getWriter().write(jsonResponse);
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//            response.getWriter().write("{\"error\": \"Error al obtener último ID del pedido\"}");
//        }
//    }
//
//// Acción para crear un nuevo pedido
//    private void crearPedido(HttpServletRequest request, HttpServletResponse response, Usuario user) throws IOException {
//        try {
//            String carritoJson = request.getParameter("carrito");
//            List<Producto> carrito = gson.fromJson(carritoJson, new TypeToken<List<Producto>>() {
//            }.getType());
//
//            String codCliente = request.getParameter("codigoCliente");
//            double subtotal = Double.parseDouble(request.getParameter("subtotal"));
//            double igv = Double.parseDouble(request.getParameter("igv"));
//            double total = Double.parseDouble(request.getParameter("total"));
//
//            // Crear el objeto pedido
//            Pedido pedido = new Pedido();
//            pedido.setCodCliente(codCliente);
//            pedido.setFecha(LocalDateTime.now());
//            pedido.setSubtotal(subtotal);
//            pedido.setIgv(igv);
//            pedido.setTotal(total);
//            pedido.setIdEmpleado(user.getIdUsuario());
//
//            int nuevoIdPedido = pedidoDAO.crearPedido(pedido, carrito); // Crear el pedido y devolver el ID generado
//
//            if (nuevoIdPedido > 0) {
//                response.getWriter().write("{\"mensaje\": \"Pedido creado correctamente\", \"idPedido\": " + nuevoIdPedido + "}");
//            } else {
//                response.getWriter().write("{\"error\": \"Error al crear pedido\"}");
//            }
//        } catch (IOException | NumberFormatException e) {
//            e.printStackTrace(System.out);
//            response.getWriter().write("{\"error\": \"Error al procesar solicitud\"}");
//        }
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
        return "Servlet para gestionar pedidos y clientes";
    }
}
