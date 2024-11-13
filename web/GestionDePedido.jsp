
<%@page import="entidades.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Gestión de Pedidos</title>
        <link rel="stylesheet" href="https://unicons.iconscout.com/release/v4.0.0/css/line.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"></script>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
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


        <!-- Gestion de pedidos -->
        
        <div class="container mt-4">

            <h2 class="text-center mb-4" style="font-weight: bold">Gestión de Pedidos</h2>

            <!-- Barra de Progreso -->
            <ul class="nav nav-pills justify-content-center">
                <li class="nav-item">
                    <a class="nav-link active" id="step1-tab" data-toggle="pill">Cliente</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step2-tab" data-toggle="pill">Productos</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step3-tab" data-toggle="pill">Confirmación</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="step4-tab" data-toggle="pill">Pago</a>
                </li>
            </ul>

            <!-- Módulo 1: Selección o Registro de Cliente -->
            <div id="step1" class="step-content">
                
                <h3 class="text-center pt-4 pb-4">Seleccionar o Registrar Cliente</h3>
                
                <div class="row d-flex justify-content-around">
                    
                    <div class="col-md-5 shadow-sm p-3 mb-5 bg-body-tertiary rounded">
                        
                        <h4 class="text-center pt-2 pb-2 text-uppercase" style="font-weight: bold">Registro de Cliente</h4>

                        <div class="container">
                            <form id="formRegistroCliente">
                                <!-- Campo de DNI para el formulario de registro -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                    <input type="text" class="form-control" id="dniRegistro" placeholder="Ingrese DNI" value="">
                                    <button class="input-group-text btn btn-outline-primary" type="button" onclick="searchClient()">Buscar</button>
                                </div>

                                <!-- Campo de Nombre -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="nombre" placeholder="Ingrese Nombre">
                                </div>

                                <!-- Campo de Apellido -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="apellido" placeholder="Ingrese Apellido">
                                </div>

                                <!-- Campo de Dirección -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-home"></i></span>
                                    <input type="text" class="form-control" id="direccion" placeholder="Ingrese Dirección">
                                </div>

                                <!-- Campo de Teléfono -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                    <input type="text" class="form-control" id="telefono" placeholder="Ingrese Teléfono">
                                </div>

                                <!-- Campo de Email -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    <input type="email" class="form-control" id="email" placeholder="Ingrese Email">
                                </div>
                                
                                <div class="pt-3 m-1 row d-flex justify-content-end">
                                    <button type="button" class="btn btn-success mt-2" onclick="registrarCliente()" id="btnRegistrar">Registrar</button>
                                </div>
                                

                            </form>  
                        </div>
                        
                    </div>
                    
                    <div class="col-md-5 shadow-sm p-3 mb-5 bg-body-tertiary rounded">
                        
                        <h5 class="text-center pt-2 pb-2 text-uppercase" style="font-weight: bold"> Búsqueda de Cliente</h5>
                        
                        <div class="container">
                            <form id="formBuscarCliente">

                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                                    <input type="text" class="form-control" id="dniCliente" placeholder="Ingrese DNI" value="">
                                    <button class="input-group-text btn btn-outline-primary" type="button" onclick="buscarCliente()" >Buscar</button>
                                </div>

                                <!-- Campo de Nombre -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="nombreCliente" placeholder="Ingrese Nombre" readonly> 
                                </div>

                                <!-- Campo de Apellido -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="apellidoCliente" placeholder="Ingrese Apellido" readonly>
                                </div>

                                <!-- Campo de Dirección -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-home"></i></span>
                                    <input type="text" class="form-control" id="direccionCliente" placeholder="Ingrese Dirección" readonly>
                                </div>

                                <!-- Campo de Teléfono -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                    <input type="text" class="form-control" id="telefonoCliente" placeholder="Ingrese Teléfono" readonly>
                                </div>

                                <!-- Campo de Email -->
                                <div class="input-group mt-2">
                                    <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                    <input type="email" class="form-control" id="emailCliente" placeholder="Ingrese Email" readonly>
                                </div>

                            </form>
                        </div>
                        
                        <div id="autocompleteResults" class="list-group mt-2"></div> <!-- Lista de sugerencias -->
                        
                    </div>
                </div>

                <div id="confirmacionSeleccion" class="mt-3"></div> <!-- Confirmación del cliente seleccionado -->
                
                <div class="row">
                    <div class="col d-flex justify-content-end align-items-center">
                        <button class="btn btn-primary mt-3" id="nextToStep2" onclick="goToStep(2)" disabled>Siguiente</button>
                    </div>
                </div>
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
