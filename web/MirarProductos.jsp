
<%@page import="entidades.Usuario"%>
<%@page import="entidades.Producto"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<Producto> listaProductos = (List<Producto>) request.getAttribute("ListaProductos");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mirar Productos</title>
        <!-- Bootstrap 5.3.3 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
    </head>
    <body>

        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

            // Obtener el objeto "user" de la sesión y verificar su rol
            Usuario user = (Usuario) session.getAttribute("user");

            // Verificar si el usuario no está en sesión o no tiene el rol de Empleado
            if (user == null || !"Empleado".equals(user.getRol())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>

        <div class="container">
            <header class="mt-4 mb-4 border rounded app-header">
                <div class="d-flex align-items-center" >
                    <div class="d-flex justify-content-start align-items-center">
                        <h4 class="m-4">Productos en Inventario<h4/>
                    </div>
                </div>
            </header>
                <div class="border rounded" style="min-height: 80vh;">
                    
                    <!-- Busqueda y botones arriba de la tabla -->
                    <div class="mt-4 mb-4 p-4">
                        <form action="GestionEmpleados" method="GET">
                            <div class="input-group mb-3">
                                <input type="text" name="nombre" class="form-control" placeholder="Buscar por nombre de producto" required>
                                <button type="submit" name="accion" class="btn btn-outline-success">Buscar</button>
                            </div>
                        </form>
                        <div class="input-group mb-3 justify-content-end">
                            <a class="btn btn-outline-primary"" role="button" href="MirarProductos?accion=Listar">Mostrar Todo</a>
                            <a href="DashboardActividades?accion=Listar" role="button" class="btn btn-outline-dark">Menú Administrador</a>
                        </div>
                    </div>

            <!-- Tabla de productos -->
            <div class="container mt-4 mb-4">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Categoría</th>
                            <th>Precio</th>
                            <th>Stock</th>
                            <th>Descripción</th>
                            <th>Imagen</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="producto" items="${ListaProductos}">
                            <tr>
                                <td>${producto.idProducto}</td>
                                <td>${producto.nombre}</td>
                                <td>
                                    <!-- Mostrar el nombre de la categoría del producto -->
                                    <c:forEach var="categoria" items="${ListaCategorias}">
                                        <c:if test="${categoria.idCategoria == producto.idCategoria}">
                                            ${categoria.nombre}
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>${producto.precio}</td>
                                <td>${producto.stock}</td>
                                <td>${producto.descripcion}</td>
                                <td>
                                    <!-- Mostrar imagen del producto -->
                                    <img src="resources/img/productos/${producto.imagen}?timestamp=<%= System.currentTimeMillis()%>" alt="${producto.nombre}" width="50" height="50">
                                </td>
                                <td>${producto.estado}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
