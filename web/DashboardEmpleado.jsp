
<%@page import="entidades.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Dashboard Empleado</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="resources/css/Dashboard.css?timestamp=<%= System.currentTimeMillis()%>">
        <!-- Bootstrap Icons CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
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

        <!-- Contenedor Principal que agrupa la barra lateral y el contenido -->
        <div class="dashboard-wrapper">
            <!-- Barra Lateral -->
            <nav class="sidebar">
                <div class="sidebar-top-wrapper">
                    <div class="sidebar-top">
                        <a href="#" class="logo__wrapper">
                            <i class="bi bi-person-circle"></i>
                            <span class="hide company-name">Dashboard Empleado</span>
                        </a>
                    </div>
                    <button class="expand-btn" type="button">
                        <i class="bi bi-arrow-bar-left"></i>
                    </button>
                </div>

                <div class="sidebar-links-wrapper">
                    <div class="sidebar-links">
                        <ul>
                            <!-- Gestionar un Pedido -->
                            <li>
                                <a href="GestionDePedido.jsp" title="Gestionar Pedido">
                                    <i class="bi bi-cart"></i>
                                    <span class="link hide">Gestionar Pedido</span>
                                </a>
                            </li>

                            <!-- Gestionar Mis Clientes -->
                            <li>
                                <a href="ControlerCliente?accion=Listar" title="Gestionar Mis Clientes">
                                    <i class="bi bi-people"></i>
                                    <span class="link hide">Gestionar Mis Clientes</span>
                                </a>
                            </li>

                            <!-- Mirar Productos -->
                            <li>
                                <a href="MirarProductos?accion=Listar" title="Mirar Productos">
                                    <i class="bi bi-box-seam"></i>
                                    <span class="link hide">Mirar Productos</span>
                                </a>
                            </li>


                            <!-- Ver Información -->
                            <li>
                                <a href="VerInformacion" title="Ver Información">
                                    <i class="bi bi-person-circle"></i>
                                    <span class="link hide">Ver Información</span>
                                </a>
                            </li>

                            <!-- Cerrar Sesión -->
                            <li>
                                <a href="CerrarSesion" title="Cerrar Sesión">
                                    <i class="bi bi-box-arrow-right"></i>
                                    <span class="link hide">Cerrar Sesión</span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <!-- Contenido Principal -->
            <div class="main-content">
                <div class="dashboard-content">
                    <!-- Sección de Bienvenida -->
                    <div class="welcome-section">
                        <img src="resources/img/user.png" alt="User Image" class="welcome-image">
                        <h2>Bienvenido, <%= user.getNombre()%> (Empleado)</h2>
                    </div>

                    <div class="reminder-section">
                        <h3>Actividades Recientes</h3>
                        <table class="reminder-table">
                            <thead>
                                <tr>
                                    <th>Tipo</th>
                                    <th>Descripción</th>
                                    <th>Fecha</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Mostrar un mensaje si no hay registros disponibles -->
                                <c:choose>
                                    <c:when test="${empty registrosActividad}">
                                        <tr><td colspan="3">No hay registros de actividad disponibles.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Mostrar registros -->
                                        <c:forEach var="registro" items="${registrosActividad}">
                                            <tr>
                                                <td>${registro.tipo}</td>
                                                <td>${registro.descripcion}</td>
                                                <td>${registro.fecha}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <script src="resources/js/Dashboard.js"></script>
    </body>
</html>
