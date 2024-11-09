
<%@page import="entidades.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Gestión de Pedidos</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"></script> <!-- Para los íconos -->
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

        <div class="container mt-5">
            <h2 class="text-center">Gestión de Pedidos</h2>

            <!-- Barra de Progreso -->
            <ul class="nav nav-pills mb-3 justify-content-center">
                <li class="nav-item">
                    <a class="nav-link active" id="step1-tab" data-toggle="pill">Paso 1: Cliente</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step2-tab" data-toggle="pill">Paso 2: Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step3-tab" data-toggle="pill">Paso 3: Confirmación</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step4-tab" data-toggle="pill">Paso 4: Pago</a>
                </li>
            </ul>

            <!-- Módulo 1: Selección o Registro de Cliente -->
            <div id="step1" class="step-content">
                <h4>Seleccionar o Registrar Cliente</h4>
                <div class="row">

                    <div class="col-md-6">
                        <h5>Registro de Cliente</h5>
                        <form id="formRegistroCliente">
                            <!-- Campo de DNI para el formulario de registro -->
                            <div class="input-group mt-2">

                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                </div>

                                <input type="text" class="form-control" id="dniRegistro" placeholder="Ingrese DNI" value="">

                                <div class="input-group-prepend">
                                    <button class="input-group btn btn-outline-primary" type="button" onclick="searchClient()">Buscar</button>
                                </div>
                            </div>

                            <!-- Campo de Nombre -->
                            <div class="input-group mt-2">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                </div>
                                <input type="text" class="form-control" id="nombre" placeholder="Ingrese Nombre">
                            </div>

                            <!-- Campo de Apellido -->
                            <div class="input-group mt-2">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                </div>
                                <input type="text" class="form-control" id="apellido" placeholder="Ingrese Apellido">
                            </div>

                            <!-- Campo de Dirección -->
                            <div class="input-group mt-2">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-home"></i></span>
                                </div>
                                <input type="text" class="form-control" id="direccion" placeholder="Ingrese Dirección">
                            </div>

                            <!-- Campo de Teléfono -->
                            <div class="input-group mt-2">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                </div>
                                <input type="text" class="form-control" id="telefono" placeholder="Ingrese Teléfono">
                            </div>

                            <!-- Campo de Email -->
                            <div class="input-group mt-2">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                </div>
                                <input type="email" class="form-control" id="email" placeholder="Ingrese Email">
                            </div>
                            <button type="button" class="btn btn-success mt-2" onclick="registrarCliente()" id="btnRegistrar">Registrar</button>

                        </form>
                    </div>
                    
                    <div class="col-md-6">
                        <h5>Búsqueda de Cliente</h5>
                        <input type="text" id="dniCliente" class="form-control" placeholder="Ingrese DNI del cliente" autocomplete="off">
                        <div id="autocompleteResults" class="list-group mt-2"></div> <!-- Lista de sugerencias -->
                        <button class="btn btn-primary mt-2" onclick="buscarCliente()" id="btnBuscar">Buscar</button>
                    </div>
                </div>

                <div id="confirmacionSeleccion" class="mt-3"></div> <!-- Confirmación del cliente seleccionado -->
                <button class="btn btn-primary mt-3" id="nextToStep2" onclick="goToStep(2)" disabled>Siguiente</button>
            </div>


            <!-- Módulo 2: Selección de Productos -->
            <div id="step2" class="step-content d-none">
                <h4>Seleccionar Productos</h4>
                <input type="text" class="form-control mb-3" id="buscarProducto" placeholder="Buscar producto por nombre" onkeyup="buscarProducto()">
                <button class="btn btn-secondary mb-3" onclick="listarProductos()">Ver Todos</button>
                <div id="listaProductos"></div>
                <h5>Carrito</h5>
                <div id="carrito"></div>
                <button class="btn btn-primary mt-3" id="nextToStep3" onclick="goToStep(3)" disabled>Confirmar</button>
                <button class="btn btn-secondary mt-3" onclick="goToStep(1)">Retroceder</button>
            </div>

            <!-- Módulo 3: Confirmación de Pedido -->
            <div id="step3" class="step-content d-none">
                <h4>Confirmación de Pedido</h4>
                <div id="resumenPedido"></div>
                <button class="btn btn-primary mt-3" id="nextToStep4" onclick="goToStep(4)">Confirmar</button>
                <button class="btn btn-secondary mt-3" onclick="goToStep(2)">Retroceder</button>
            </div>

            <!-- Módulo 4: Pago -->
            <div id="step4" class="step-content d-none">
                <h4>Pago</h4>
                <form id="formPago">
                    <input type="text" class="form-control mt-2" placeholder="Número de Tarjeta">
                    <input type="text" class="form-control mt-2" placeholder="CVV">
                    <input type="text" class="form-control mt-2" placeholder="Nombre en la Tarjeta">
                    <button type="button" class="btn btn-success mt-3" onclick="realizarPedido()">Realizar Pedido</button>
                    <button type="button" class="btn btn-secondary mt-3" onclick="goToStep(3)">Retroceder</button>
                </form>
            </div>
        </div>

        <script src="resources/js/Pedido.js?timestamp=<%= System.currentTimeMillis()%>"></script>
    </body>
</html>
