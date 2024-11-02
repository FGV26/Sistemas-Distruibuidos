
package Controler;

import dao.UsuarioDAO;
import entidades.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GestionEmpleados", urlPatterns = {"/GestionEmpleados"})
public class ControladorEmpleado extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            listarEmpleados(request, response);
            return;
        }
        
        switch (accion) {
            case "listar":
                listarEmpleados(request, response);
                break;
            case "eliminar":
                eliminarEmpleado(request, response);
                break;
            case "editar":
                mostrarEditarEmpleado(request, response);
                break;
            default:
                listarEmpleados(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        
        if (accion == null) {
            response.sendRedirect("GestionEmpleados?accion=listar");
            return;
        }
        
        switch (accion) {
            case "agregar":
                agregarEmpleado(request, response);
                break;
            case "editar":
                editarEmpleado(request, response);
                break;
            default:
                response.sendRedirect("GestionEmpleados?accion=listar");
                break;
        }
    }

    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Usuario> listaEmpleados = usuarioDAO.listarEmpleados(); // Suponiendo que este método existe en UsuarioDAO
        request.setAttribute("ListaEmpleados", listaEmpleados);
        request.getRequestDispatcher("GestionEmpleados.jsp").forward(request, response);
    }

    private void agregarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Usuario nuevoEmpleado = new Usuario();
            nuevoEmpleado.setUsername(request.getParameter("usuario"));
            nuevoEmpleado.setPassword(request.getParameter("clave"));
            nuevoEmpleado.setEmail(request.getParameter("email"));
            nuevoEmpleado.setRol("Empleado");
            nuevoEmpleado.setNombre(request.getParameter("nombre"));
            nuevoEmpleado.setApellido(request.getParameter("apellido"));
            
            boolean exito = usuarioDAO.agregar(nuevoEmpleado);
            if (exito) {
                response.sendRedirect("GestionEmpleados?accion=listar&mensaje=Empleado+agregado+correctamente");
            } else {
                response.sendRedirect("GestionEmpleados?accion=listar&error=Error+al+agregar+empleado");
            }
        } catch (Exception e) {
            response.sendRedirect("GestionEmpleados?accion=listar&error=Excepción+al+agregar+empleado");
        }
    }

    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
            boolean exito = usuarioDAO.eliminar(idEmpleado);
            if (exito) {
                response.sendRedirect("GestionEmpleados?accion=listar&mensaje=Empleado+eliminado+correctamente");
            } else {
                response.sendRedirect("GestionEmpleados?accion=listar&error=Error+al+eliminar+empleado");
            }
        } catch (Exception e) {
            response.sendRedirect("GestionEmpleados?accion=listar&error=Excepción+al+eliminar+empleado");
        }
    }

    private void mostrarEditarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
        Usuario empleado = usuarioDAO.obtenerPorId(idEmpleado);
        request.setAttribute("empleado", empleado);
        request.getRequestDispatcher("editarEmpleado.jsp").forward(request, response);
    }

    private void editarEmpleado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idEmpleado = Integer.parseInt(request.getParameter("idEmpleado"));
            Usuario empleado = usuarioDAO.obtenerPorId(idEmpleado);
            
            if (empleado != null) {
                empleado.setUsername(request.getParameter("usuario"));
                empleado.setPassword(request.getParameter("clave"));
                empleado.setEmail(request.getParameter("email"));
                empleado.setNombre(request.getParameter("nombre"));
                empleado.setApellido(request.getParameter("apellido"));
                
                boolean exito = usuarioDAO.actualizar(empleado);
                if (exito) {
                    response.sendRedirect("GestionEmpleados?accion=listar&mensaje=Empleado+actualizado+correctamente");
                } else {
                    response.sendRedirect("GestionEmpleados?accion=listar&error=Error+al+actualizar+empleado");
                }
            } else {
                response.sendRedirect("GestionEmpleados?accion=listar&error=Empleado+no+encontrado");
            }
        } catch (Exception e) {
            response.sendRedirect("GestionEmpleados?accion=listar&error=Excepción+al+actualizar+empleado");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para la gestión de empleados";
    }
}
