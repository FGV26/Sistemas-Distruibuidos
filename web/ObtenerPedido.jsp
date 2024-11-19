
<%@ page import="entidades.Usuario"%>
<%@ page import="entidades.Pedido"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Verificar sesión y rol del usuario
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    Usuario user = (Usuario) session.getAttribute("user");

    if (user == null || !"Despachador".equals(user.getRol())) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Obtenemos la lista de pedidos desde el atributo de la solicitud
    List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("ListaPedidos");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Obtener Pedido</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <main class="container">
            <!-- Header -->
            <header class="mt-4 mb-4 border rounded app-header">
                <div class="d-flex align-items-center">
                    <h4 class="m-4">Obtener un Pedido - Escoge un Pedido</h4>
                </div>
            </header>

            <!-- Mensajes de Éxito/Error usando Toasts -->
            <c:if test="${not empty mensaje}">
                <div aria-live="polite" aria-atomic="true" class="position-relative">
                    <div class="toast-container position-fixed top-0 end-0 p-3">
                        <div class="toast bg-success text-white fade show" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    ${mensaje}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div aria-live="polite" aria-atomic="true" class="position-relative">
                    <div class="toast-container position-fixed top-0 end-0 p-3">
                        <div class="toast bg-danger text-white fade show" role="alert" aria-live="assertive" aria-atomic="true">
                            <div class="d-flex">
                                <div class="toast-body">
                                    ${error}
                                </div>
                                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <!-- Contenido -->
            <div class="border rounded" style="min-height: 80vh;">
                <!-- Barra de Búsqueda -->
                <div class="mt-4 mb-4 p-4">
                    <form action="ObtenerPedido" method="GET">
                        <div class="input-group mb-3">
                            <input type="text" name="nombreCliente" class="form-control" placeholder="Buscar por nombre del cliente" required>
                            <button type="submit" name="accion" class="btn btn-outline-success" value="Buscar">Buscar</button>
                        </div>
                    </form>
                    <div class="input-group mb-3 justify-content-end">
                        <a class="btn btn-outline-primary" href="ObtenerPedido?accion=Listar">Mostrar Todo</a>
                        <a class="btn btn-outline-dark" href="DashboardActividades?accion=Listar">Volver al Dashboard</a>
                    </div>
                </div>

                <!-- Tabla de Pedidos -->
                <div class="container mt-4 mb-4">
                    <c:choose>
                        <c:when test="${not empty ListaPedidos}">
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
                                                <c:choose>
                                                    <c:when test="${pedido.idDespachador == null}">
                                                        <a href="ObtenerPedido?accion=AsinarPedido&idPedido=${pedido.idPedido}" 
                                                           class="btn btn-success btn-sm"
                                                           onclick="return confirm('¿Estás seguro de que deseas obtener este pedido?');">
                                                            <i class="fas fa-check"></i> Obtener Pedido
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">Asignado</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning" role="alert">
                                No hay pedidos disponibles para asignar.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Script para mostrar automáticamente las notificaciones -->
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const toastElList = document.querySelectorAll('.toast');
                const toastList = [...toastElList].map(toastEl => {
                    const toast = new bootstrap.Toast(toastEl);
                    toast.show();
                    return toast;
                });
            });
        </script>
    </body>
</html>
