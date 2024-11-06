package Controler;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import dao.RegistroActividadDAO;
import entidades.Categoria;
import entidades.Producto;
import entidades.RegistroActividad;
import entidades.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GestionProductos", urlPatterns = {"/GestionProductos"})
@MultipartConfig
public class ControlerProductos extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();
    private static final String ABSOLUTE_IMAGE_PATH = "D:/GitHub/Sistemas-Distribuidos/build/web/resources/img/productos/";
    private static final String IMAGES_PATH = "resources/img/productos/";
    private static final String IMAGE_PREFIX = "PRO";
    private int currentImageIndex = 144;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        if (user == null || !user.getRol().equals("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");  // Cambiado de "Op" a "accion"

        switch (accion) {
            case "Listar":
                listarProductos(request, response);
                break;
            case "Buscar":
                buscarProducto(request, response, user);
                break;
            case "ListarPocoStock":
                listarPocoStock(request, response);
                break;
            case "ListarSinStock":
                listarSinStock(request, response);
                break;
            case "Agregar":
                agregarProducto(request, response, user);
                break;
            case "Editar":
                editarProducto(request, response, user);
                break;
            case "Eliminar":
                eliminarProducto(request, response, user);
                break;
            default:
                response.sendRedirect("GestionProductos.jsp");
                break;
        }
    }

    // Listar todos los productos con advertencias de stock
    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Producto> listaProductos = productoDAO.listar();
        List<Categoria> listaCategorias = categoriaDAO.listar(); // Obtener las categorías

        // Listas para almacenar mensajes de advertencia
        List<String> mensajesAdvertencia = new ArrayList<>();
        List<String> tiposMensaje = new ArrayList<>();

        // Generar mensajes de advertencia si el stock es bajo o nulo
        for (Producto producto : listaProductos) {
            if (producto.getStock() <= 0) {
                mensajesAdvertencia.add("El producto " + producto.getNombre() + " está agotado.");
                tiposMensaje.add("danger");
            } else if (producto.getStock() < producto.getStockMinimo()) {
                mensajesAdvertencia.add("El producto " + producto.getNombre() + " tiene stock bajo.");
                tiposMensaje.add("warning");
            }
        }

        // Pasar listas a la JSP
        request.setAttribute("ListaProductos", listaProductos);
        request.setAttribute("ListaCategorias", listaCategorias); // Pasar categorías al JSP
        request.setAttribute("mensajesAdvertencia", mensajesAdvertencia); // Pasar mensajes de advertencia
        request.setAttribute("tiposMensaje", tiposMensaje); // Pasar tipos de mensaje (danger o warning)

        // Redirigir a la página de gestión de productos
        request.getRequestDispatcher("GestionProductos.jsp").forward(request, response);
    }

    // Buscar productos por nombre
    private void buscarProducto(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        if (nombre == null || nombre.isEmpty()) {
            response.sendRedirect("GestionProductos?accion=Listar&error=Ingrese+un+nombre+para+buscar");
            return;
        }

        List<Producto> productos = productoDAO.buscarPorNombre(nombre);
        List<Categoria> listaCategorias = categoriaDAO.listar();
        request.setAttribute("ListaCategorias", listaCategorias);

        if (!productos.isEmpty()) {
            request.setAttribute("ListaProductos", productos);
            request.getRequestDispatcher("GestionProductos.jsp").forward(request, response);

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Búsqueda");
            registro.setDescripcion("El administrador buscó un producto: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionProductos?accion=Listar&error=Producto+no+encontrado");
        }
    }

    // Filtrar productos con poco stock
    private void listarPocoStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los productos con poco stock
        List<Producto> productos = productoDAO.listarPocoStock();

        // Obtener la lista de categorías y añadirla al request
        List<Categoria> listaCategorias = categoriaDAO.listar();
        request.setAttribute("ListaCategorias", listaCategorias);

        // Añadir la lista de productos al request y redirigir al JSP
        request.setAttribute("ListaProductos", productos);
        request.getRequestDispatcher("GestionProductos.jsp").forward(request, response);
    }

// Filtrar productos sin stock
    private void listarSinStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los productos sin stock
        List<Producto> productos = productoDAO.listarSinStock();

        // Obtener la lista de categorías y añadirla al request
        List<Categoria> listaCategorias = categoriaDAO.listar();
        request.setAttribute("ListaCategorias", listaCategorias);

        // Añadir la lista de productos al request y redirigir al JSP
        request.setAttribute("ListaProductos", productos);
        request.getRequestDispatcher("GestionProductos.jsp").forward(request, response);
    }

    // Método para guardar la imagen en la carpeta especificada
    private String guardarImagen(Part filePart, String nombreImagen) throws IOException {
        String directoryPath = getServletContext().getRealPath("/resources/img/productos/");
        System.out.println("Ruta de almacenamiento de la imagen: " + directoryPath);

        // Crear el directorio si no existe
        Files.createDirectories(Paths.get(directoryPath));

        // Ruta completa del archivo a guardar
        Path filePath = Paths.get(directoryPath, nombreImagen);
        System.out.println("Ruta completa del archivo a guardar: " + filePath.toString());

        // Guardar la imagen en el sistema de archivos
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Imagen guardada exitosamente en: " + filePath.toString());
        } catch (IOException e) {
            e.printStackTrace(System.out);
            System.out.println("Error al guardar la imagen: " + e.getMessage());
        }

        // Retornar la ruta relativa para almacenar en la base de datos
        return "resources/img/productos/" + nombreImagen;
    }

    // Método para agregar un producto
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        double precio = Double.parseDouble(request.getParameter("precio"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int stockMinimo = Integer.parseInt(request.getParameter("stockMinimo"));
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");

        Producto producto = new Producto(0, idCategoria, nombre, precio, stock, stockMinimo, descripcion, null, estado, null);
        int idProducto = productoDAO.insertarImagen(producto);

        if (idProducto > 0) {
            Part filePart = request.getPart("imagen");
            if (filePart != null && filePart.getSize() > 0) {
                String extension = obtenerExtensionImagen(filePart);
                String nombreImagen = IMAGE_PREFIX + String.format("%04d", idProducto) + extension;
                guardarImagen(filePart, nombreImagen);

                productoDAO.actualizarImagen(idProducto, nombreImagen);
            }
            response.sendRedirect("GestionProductos?accion=Listar&mensaje=Producto+agregado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Creación");
            registro.setDescripcion("El administrador creó un nuevo producto: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionProductos?accion=Listar&error=Error+al+agregar+producto");
        }
    }

    // Método para editar un producto
    private void editarProducto(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        String nombre = request.getParameter("nombre");
        int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
        double precio = Double.parseDouble(request.getParameter("precio"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int stockMinimo = Integer.parseInt(request.getParameter("stockMinimo"));
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");

        String imagenActual = request.getParameter("imagenActual");
        Part filePart = request.getPart("imagen");
        String nombreImagen = imagenActual;
        if (filePart != null && filePart.getSize() > 0) {
            String extension = obtenerExtensionImagen(filePart);
            nombreImagen = IMAGE_PREFIX + String.format("%04d", idProducto) + extension;
            guardarImagen(filePart, nombreImagen);
            eliminarImagen(imagenActual);
        }

        Producto producto = new Producto(idProducto, idCategoria, nombre, precio, stock, stockMinimo, descripcion, nombreImagen, estado, null);
        boolean success = productoDAO.actualizar(producto);

        if (success) {
            response.sendRedirect("GestionProductos?accion=Listar&mensaje=Producto+modificado+correctamente");

            // Registrar actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Modificación");
            registro.setDescripcion("El administrador modificó el producto: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionProductos?accion=Listar&error=Error+al+modificar+producto");
        }
    }

    // Método para eliminar un producto
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        Producto producto = productoDAO.obtenerProductoPorId(idProducto);

        if (producto != null) {
            if (producto.getImagen() != null) {
                eliminarImagen(producto.getImagen());
            }

            boolean success = productoDAO.eliminar(idProducto);
            if (success) {
                response.sendRedirect("GestionProductos?accion=Listar&mensaje=Producto+eliminado+correctamente");

                // Registrar actividad
                RegistroActividad registro = new RegistroActividad();
                registro.setTipo("Eliminación");
                registro.setDescripcion("El administrador eliminó un producto con nombre: " + producto.getNombre());
                registro.setFecha(LocalDateTime.now());
                registro.setIdUsuario(user.getIdUsuario());
                registroActividadDAO.insertar(registro);
            } else {
                response.sendRedirect("GestionProductos?accion=Listar&error=Error+al+eliminar+producto");
            }
        } else {
            response.sendRedirect("GestionProductos?accion=Listar&error=Producto+no+encontrado");
        }
    }

    // Método para eliminar la imagen del sistema de archivos
    private void eliminarImagen(String nombreImagen) {
        // Asegúrate de que directoryPath termine con una barra diagonal
        String directoryPath = getServletContext().getRealPath("/resources/img/productos/");
        if (!directoryPath.endsWith(File.separator)) {
            directoryPath += File.separator;  // Agrega la barra diagonal si falta
        }

        String filePath = directoryPath + nombreImagen;

        System.out.println("Intentando eliminar imagen en: " + filePath); // Depuración

        File imageFile = new File(filePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                System.out.println("Imagen eliminada correctamente: " + filePath);
            } else {
                System.out.println("No se pudo eliminar la imagen: " + filePath);
            }
        } else {
            System.out.println("La imagen no se encontró para eliminar: " + filePath);
        }
    }

    // Método para obtener la extensión del archivo de imagen
    private String obtenerExtensionImagen(Part filePart) {
        String fileName = filePart.getSubmittedFileName();
        return fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
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
        return "Controlador para gestionar productos";
    }
}
