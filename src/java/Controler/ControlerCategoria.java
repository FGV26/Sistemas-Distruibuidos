
package Controler;

import dao.CategoriaDAO;
import dao.RegistroActividadDAO;
import entidades.Categoria;
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

@WebServlet(name = "GestionCategoria", urlPatterns = {"/GestionCategoria"})
public class ControlerCategoria extends HttpServlet {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final RegistroActividadDAO registroActividadDAO = new RegistroActividadDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        
        // Validación de acceso para administrador
        if (user == null || !user.getRol().equals("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");

        switch (accion) {
            case "Listar":
                listarCategorias(request, response);
                break;
            case "Buscar":
                buscarCategoria(request, response, user);
                break;
            case "Crear":
                crearCategoria(request, response, user);
                break;
            case "Modificar":
                modificarCategoria(request, response, user);
                break;
            case "Eliminar":
                eliminarCategoria(request, response, user);
                break;
            default:
                response.sendRedirect("GestionCategoria.jsp");
                break;
        }
    }

    private void listarCategorias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Categoria> listaCategorias = categoriaDAO.listar();
        request.setAttribute("ListaCategorias", listaCategorias);
        request.getRequestDispatcher("GestionCategoria.jsp").forward(request, response);
    }

    private void buscarCategoria(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");

        if (nombre == null || nombre.isEmpty()) {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Debe+ingresar+un+nombre+para+buscar");
            return;
        }

        List<Categoria> categorias = categoriaDAO.buscarPorNombre(nombre);

        if (categorias != null && !categorias.isEmpty()) {
            request.setAttribute("ListaCategorias", categorias);
            request.getRequestDispatcher("GestionCategoria.jsp").forward(request, response);

            // Registro de actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Búsqueda");
            registro.setDescripcion("El administrador buscó una categoría: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());  // Asociar el ID del usuario
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Categoría+no+encontrada");
        }
    }

    private void crearCategoria(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        if (nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty()) {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Datos+incompletos");
            return;
        }

        Categoria nuevaCategoria = new Categoria(nombre, descripcion);
        boolean exito = categoriaDAO.insertar(nuevaCategoria) > 0;

        if (exito) {
            response.sendRedirect("GestionCategoria?accion=Listar&mensaje=Categoría+agregada+correctamente");

            // Registro de actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Creación");
            registro.setDescripcion("El administrador creó una nueva categoría: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());  // Asociar el ID del usuario
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Error+al+agregar+categoría");
        }
    }

    private void modificarCategoria(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String idString = request.getParameter("idCategoria");
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        if (idString == null || nombre == null || nombre.isEmpty() || descripcion == null || descripcion.isEmpty()) {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Datos+incompletos");
            return;
        }

        int id = Integer.parseInt(idString);
        Categoria categoria = new Categoria(id, nombre, descripcion);
        boolean exito = categoriaDAO.actualizar(categoria) > 0;

        if (exito) {
            response.sendRedirect("GestionCategoria?accion=Listar&mensaje=Categoría+modificada+correctamente");

            // Registro de actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Modificación");
            registro.setDescripcion("El administrador modificó la categoría: " + nombre);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());  // Asociar el ID del usuario
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Error+al+modificar+categoría");
        }
    }

    private void eliminarCategoria(HttpServletRequest request, HttpServletResponse response, Usuario user)
            throws ServletException, IOException {
        String idString = request.getParameter("idCategoria");

        if (idString == null || idString.isEmpty()) {
            response.sendRedirect("GestionCategoria?accion=Listar&error=ID+de+categoría+no+proporcionado");
            return;
        }

        int id = Integer.parseInt(idString);
        boolean exito = categoriaDAO.eliminar(id) > 0;

        if (exito) {
            response.sendRedirect("GestionCategoria?accion=Listar&mensaje=Categoría+eliminada+correctamente");

            // Registro de actividad
            RegistroActividad registro = new RegistroActividad();
            registro.setTipo("Eliminación");
            registro.setDescripcion("El administrador eliminó una categoría con ID: " + id);
            registro.setFecha(LocalDateTime.now());
            registro.setIdUsuario(user.getIdUsuario());  // Asociar el ID del usuario
            registroActividadDAO.insertar(registro);
        } else {
            response.sendRedirect("GestionCategoria?accion=Listar&error=Error+al+eliminar+categoría");
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
        return "Controlador para gestionar categorías";
    }
}
