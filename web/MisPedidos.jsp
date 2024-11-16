
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="entidades.Pedido"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("ListaPedidos");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Mis Pedidos</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <main class="container">
            <!-- Header -->
            <header class="mt-4 mb-4 border rounded app-header">
                <div class="d-flex align-items-center">
                    <h4 class="m-4">Mis Pedidos</h4>
                </div>
            </header>

            <div class="border rounded" style="min-height: 80vh;">
                <!-- Busqueda y botones -->
                <div class="mt-4 mb-4 p-4">
                    <form action="MisPedidos" method="GET">
                        <div class="input-group mb-3">
                            <input type="text" name="nombreCliente" class="form-control" placeholder="Buscar por nombre del cliente" required>
                            <button type="submit" name="accion" class="btn btn-outline-success" value="Buscar">Buscar</button>
                        </div>
                    </form>
                    <div class="input-group mb-3 justify-content-end">
                        <a class="btn btn-outline-primary" role="button" href="MisPedidos?accion=Listar">Mostrar Todo</a>
                        <a href="DashboardActividades?accion=Listar" role="button" class="btn btn-outline-dark">Volver al Dashboard</a>
                    </div>
                </div>

                <!-- Tabla -->
                <div class="container mt-4 mb-4">
                    <table class="table">
                        <thead>
                            <tr>
                                <th scope="col">ID Pedido</th>
                                <th scope="col">Cliente</th>
                                <th scope="col">Fecha Pedido</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Total</th>
                                <th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="pedido" items="${ListaPedidos}">
                                <tr>
                                    <td>${pedido.idPedido}</td>
                                    <td>${pedido.nombreCliente}</td>
                                    <td>${pedido.fechaPedido}</td>
                                    <td>${pedido.estado}</td>
                                    <td>${pedido.total}</td>
                                    <td>
                                        <div class="btn-group">
                                            <!-- Eliminar solo si el estado es "Proceso" -->
                                            <c:if test="${pedido.estado == 'Proceso'}">
                                                <a href="MisPedidos?accion=Eliminar&idPedido=${pedido.idPedido}" class="btn btn-danger"
                                                   onclick="return confirm('¿Estás seguro de que deseas eliminar este pedido?');">
                                                    <i class="fas fa-trash-alt"></i> Eliminar
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>

        <!-- Mensajes de éxito o error -->
        <% String mensaje = request.getParameter("mensaje");
            String error = request.getParameter("error"); %>

        <% if (mensaje != null) {%>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensaje%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% } else if (error != null) {%>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error%>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% }%>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>
