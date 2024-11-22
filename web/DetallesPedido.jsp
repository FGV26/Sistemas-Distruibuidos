
<%@page import="entidades.Usuario"%>
<%@page import="entidades.Pedido"%>
<%@page import="entidades.DetallePedido"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Detalle del Pedido</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
        <style>
            .progress-bar {
                background-color: #4caf50;
                transition: width 0.4s ease;
            }
            .progress-color {
                width: 2.5rem;
                height: 2.5rem;
            }
            .step-label {
                font-size: 0.9rem;
                text-align: center;
            }
        </style>
    </head>
    <body>

        <%
            // Verificar sesión y rol del usuario
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

            Usuario user = (Usuario) session.getAttribute("user");

            if (user == null || !"Despachador".equals(user.getRol())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>

        <div class="container mt-5">
            <!-- Título -->
            <div class="d-flex justify-content-between align-items-center">
                <h1 class="text-center mb-0">Detalles del Pedido</h1>
                <a href="ControlerPedido?accion=Listar" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Volver
                </a>
            </div>
            <hr>
            <!-- Datos del Pedido -->
            <div class="row mt-4">
                <div class="col-md-4">
                    <h5><strong>Código del Pedido:</strong></h5>
                    <p>${pedido.codPedido}</p>
                </div>
                <div class="col-md-4">
                    <h5><strong>Nombre del Cliente:</strong></h5>
                    <p>${pedido.nombreCliente}</p>
                </div>
                <div class="col-md-4">
                    <h5><strong>Nombre del Empleado:</strong></h5>
                    <p>${pedido.nombreEmpleado}</p>
                </div>
            </div>

            <!-- Contenedor de la notificación de cancelación -->
            <div aria-live="polite" aria-atomic="true" class="position-relative">
                <div class="toast-container position-fixed top-0 end-0 p-3">
                    <div id="cancel-toast" class="toast bg-danger text-white fade" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div id="toast-body" class="toast-body">
                                <!-- El mensaje se inserta dinámicamente aquí -->
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Barra de Progreso -->
            <div class="progress-container container mt-4 mb-4" 
                 data-estado="${pedido.estado}" 
                 data-idpedido="${pedido.idPedido}">
                <div class="progress" style="height: 5px;">
                    <div class="progress-bar" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
                <div class="d-flex justify-content-between mt-3">
                    <span class="bg-success text-white rounded-circle d-flex align-items-center justify-content-center progress-color">
                        <i class="fas fa-tasks"></i>
                    </span>
                    <span class="bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center progress-color">
                        <i class="fas fa-eye"></i>
                    </span>
                    <span class="bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center progress-color">
                        <i class="fas fa-box-open"></i>
                    </span>
                    <span class="bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center progress-color">
                        <i class="fas fa-shipping-fast"></i>
                    </span>
                </div>
                <div class="d-flex justify-content-between mt-2">
                    <span class="step-label">Proceso</span>
                    <span class="step-label">Leido</span>
                    <span class="step-label">Empaquetado</span>
                    <span class="step-label">Enviado</span>
                </div>
            </div>

            <!-- Botones de Acción -->
            <div class="d-flex justify-content-between mt-4">
                <button id="cancel-btn" class="btn btn-danger">Cancelar Pedido</button>
                <button id="next-btn-progress" class="btn btn-primary">Avanzar Estado</button>
            </div>
        </div>

        <!-- Tabla de Productos -->
        <div class="container mt-5">
            <h2 class="text-center mb-4">Productos del Pedido</h2>
            <div class="row">
                <div class="col-md-9">
                    <!-- Tabla con scroll -->
                    <div class="table-responsive" style="max-height: 300px; overflow-y: auto;">
                        <table class="table table-bordered">
                            <thead class="table-light text-center">
                                <tr>
                                    <th>Producto</th>
                                    <th>Nombre</th>
                                    <th>Cantidad</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="detalle" items="${pedido.detalles}">
                                    <tr>
                                        <td class="text-center">
                                            <img src="resources/img/productos/${detalle.imagenProducto}" alt="${detalle.nombreProducto}" style="width: 50px; height: 50px;">
                                        </td>
                                        <td>${detalle.nombreProducto}</td>
                                        <td class="text-center">${detalle.cantidad}</td>
                                        <td class="text-end">S/ ${detalle.total}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="col-md-3">
                    <!-- Contenedor del Total General -->
                    <div class="border p-3">
                        <h5>Total General</h5>
                        <p class="fs-4 text-end"><strong>S/ ${pedido.total}</strong></p>
                        <!-- Botón para generar la boleta -->
                        <a href="GenerarBoleta?idPedido=${pedido.idPedido}" class="btn btn-success w-100 mt-3" target="_blank">
                            <i class="fas fa-file-pdf"></i> Generar Boleta
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap Scripts -->
        <script src="resources/js/DetallePedido.js?timestamp=<%= System.currentTimeMillis()%>"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
