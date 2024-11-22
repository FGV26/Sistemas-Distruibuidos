

<%@page import="entidades.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Gestión de Pedidos</title>
        <link rel="stylesheet" href="https://unicons.iconscout.com/release/v4.0.0/css/line.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/js/all.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <style>
            .progress-container {
                width: 50%;
            }
            @media (max-width: 700px) {
                .progress-container {
                    width: 90%;
                }
            }
        </style>
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

        
        <div class="container pt-5">        
            <div class="border rounded" style="min-height: 80vh; box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;">
                <div class="pt-2 pb-2 d-flex justify-content-end" id="boton-dashboard" style="margin:20px">
                    <button type="button" class="btn d-flex justify-content-center align-items-center" 
                            style="background-color: #198754; color: white;" 
                            onmouseover="this.style.backgroundColor='#E49156'" 
                            onmouseout="this.style.backgroundColor='#198754'"
                            onclick="window.location.href='DashboardActividades?accion=Listar'">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house-heart me-2" viewBox="0 0 16 16">
                            <path d="M8 6.982C9.664 5.309 13.825 8.236 8 12 2.175 8.236 6.336 5.309 8 6.982"/>
                            <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.707L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.646a.5.5 0 0 0 .708-.707L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5z"/>
                        </svg>
                        Dashboard
                    </button>
                </div>
                <!-- Progress Bar -->
                <div class="progress-container container pt-5 pb-5 align-items-center" style="min-height: 100px;">
                    <div class="mx-auto">
                        <div class="progress-container" style="position:  relative; margin-top: 20px; width: 100%;">
                            <div class="progress" role="progressbar" aria-label="Progress" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="height: 1px;  width: 100%; z-index: 1; position: absolute; top: 15px;">
                                <div class="progress-bar" style="width: 0%; background-color: #4caf50;"></div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center">

                            <span class="bg-success text-white rounded-pill d-flex align-items-center justify-content-center progress-color" style="width: 2rem; height:2rem; z-index: 2;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                                </svg>
                            </span>

                            <span class="bg-secondary text-white rounded-pill d-flex align-items-center justify-content-center progress-color" style="width: 2rem; height:2rem; z-index: 2;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-box-seam-fill" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M15.528 2.973a.75.75 0 0 1 .472.696v8.662a.75.75 0 0 1-.472.696l-7.25 2.9a.75.75 0 0 1-.557 0l-7.25-2.9A.75.75 0 0 1 0 12.331V3.669a.75.75 0 0 1 .471-.696L7.443.184l.01-.003.268-.108a.75.75 0 0 1 .558 0l.269.108.01.003zM10.404 2 4.25 4.461 1.846 3.5 1 3.839v.4l6.5 2.6v7.922l.5.2.5-.2V6.84l6.5-2.6v-.4l-.846-.339L8 5.961 5.596 5l6.154-2.461z"/>
                                </svg>
                            </span>

                            <span class="bg-secondary text-white rounded-pill d-flex align-items-center justify-content-center progress-color" style="width: 2rem; height:2rem; z-index: 2;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bag-check" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M10.854 8.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 0 1 .708-.708L7.5 10.793l2.646-2.647a.5.5 0 0 1 .708 0"/>
                                    <path d="M8 1a2.5 2.5 0 0 1 2.5 2.5V4h-5v-.5A2.5 2.5 0 0 1 8 1m3.5 3v-.5a3.5 3.5 0 1 0-7 0V4H1v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V4zM2 5h12v9a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1z"/>
                                </svg>
                            </span>

                            <span class="bg-secondary text-white rounded-pill d-flex align-items-center justify-content-center progress-color" style="width: 2rem; height:2rem; z-index: 2;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-credit-card-fill" viewBox="0 0 16 16">
                                    <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v1H0zm0 3v5a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7zm3 2h1a1 1 0 0 1 1 1v1a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1v-1a1 1 0 0 1 1-1"/>
                                </svg>
                            </span>

                        </div>

                        <div class="d-flex justify-content-between align-items-center pt-3">

                            <span class="step-label d-flex align-items-center justify-content-center" style="width: 2rem; height:2rem;">
                                Cliente
                            </span>

                            <span class="step-label d-flex align-items-center justify-content-center" style="width: 2rem; height:2rem;">
                                Producto
                            </span>

                            <span class="step-label d-flex align-items-center justify-content-center" style="width: 2rem; height:2rem;">
                                Confirmacion
                            </span>

                            <span class="step-label d-flex align-items-center justify-content-center" style="width: 2rem; height:2rem;">
                                Pago
                            </span>

                        </div>

                    </div>
                </div>


                <!-- Módulo 1: Content Client -->

                <div class="container cliente pt-2 pb-2" id="cliente" >

                    <h3 class="text-center pb-5">Seleccionar o Registrar Cliente</h3>

                    <div class="row d-flex justify-content-around">

                        <div class="col-md-5 p-3 mb-5 bg-body-tertiary rounded" style="box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;">

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

                        <div class="col-md-5 p-3 mb-5 bg-body-tertiary rounded" style="box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;">

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

                    <div id="confirmacionSeleccion" class="mt-3"></div> 

                </div>

                <!-- Módulo 2: Content Product -->
                <div class="container producto pt-2 pb-5 d-none" id="producto">
                    <h3 style="color: #28a745; font-weight: 800; margin-bottom: 20px">
                        Seleccionar productos a comprar : 
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-down-square-fill ms-2" viewBox="0 0 16 16">
                            <path d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2zm6.5 4.5v5.793l2.146-2.147a.5.5 0 0 1 .708.708l-3 3a.5.5 0 0 1-.708 0l-3-3a.5.5 0 1 1 .708-.708L7.5 10.293V4.5a.5.5 0 0 1 1 0"/>
                        </svg>
                    </h3>

                    <!-- Barra de búsqueda de productos -->
                    <div class="d-flex mb-3">
                        <div class="input-group me-2 flex-grow-1">
                            <span class="input-group-text">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clipboard-check" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M10.854 7.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 9.793l2.646-2.647a.5.5 0 0 1 .708 0"/>
                                    <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1z"/>
                                    <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0z"/>
                                </svg>
                            </span>
                            <input type="text" class="form-control" id="buscarProducto" placeholder="Buscar producto por nombre" onkeyup="buscarProducto()">
                        </div>
                        <button class="btn btn-secondary flex-shrink-0" style="background-color: #A1512E; color: white; border: none;" onmouseover="this.style.backgroundColor='#D53C2D'" onmouseout="this.style.backgroundColor='#E49156'" 
                                onclick="listarProductos()">Mostrar Todo
                        </button>
                        <select class="form-select ms-2 flex-grow-1" id="categoriaProducto" onchange="listarPorCategoria(this.value)">
                            <option value="">Categoría</option>
                            <!-- Las opciones se añadirán dinámicamente a través de JavaScript -->
                        </select>
                    </div>


                    <!-- Contenedor principal de selección de productos y carrito -->
                    <div class="d-flex">
                        <!-- Tabla de productos con scroll para mostrar solo 5 elementos -->
                        <div style="width: 60%; max-height: 300px; overflow-y: auto; border: 1px solid #ddd; padding: 10px;">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th scope="col">Imagen</th>
                                        <th scope="col">Producto</th>
                                        <th scope="col">Cantidad</th>
                                        <th scope="col">Acción</th>
                                    </tr>
                                </thead>
                                <tbody id="listaProductos">
                                    <!-- Los productos se cargarán aquí dinámicamente desde JavaScript -->
                                </tbody>
                            </table>
                        </div>

                        <!-- Carrito de productos seleccionados -->
                        <div style="width: 50%; margin-left: 20px; border: 1px solid #ddd; padding: 10px;">
                            <h5>Productos Seleccionados</h5>
                            <div id="carrito" style="max-height: 250px; overflow-y: auto;">
                                <!-- Lista de productos seleccionados en el carrito -->
                                <!-- Los productos seleccionados se cargarán aquí dinámicamente desde JavaScript -->
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Módulo 3: Content Confirmation -->
                <div class="container confirmacion pt-2 pb-5 d-none" id="confirmacion">

                    <h3 class="text-center pb-5">Confirmación de Pedido</h3>

                    <!-- Información del Cliente -->
                    <div class="client-info mb-4">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="codigoCliente">Cod. Cliente:</label>
                                <input type="text" id="codigoClienteConf" class="form-control" readonly>
                            </div>
                            <div class="col-md-6">
                                <label for="nombreClienteConf">Nombres:</label>
                                <input type="text" id="nombreClienteConf" class="form-control" readonly>
                            </div>
                            <div class="col-md-6">
                                <label for="direccionClienteConf">Dirección:</label>
                                <input type="text" id="direccionClienteConf" class="form-control" readonly>
                            </div>
                            <div class="col-md-6">
                                <label for="fechaPedidoConf">Fecha:</label>
                                <input type="text" id="fechaPedidoConf" class="form-control" readonly>
                            </div>
                        </div>
                    </div>

                    <!-- Información del Pedido -->
                    <div class="order-info mb-4">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="numeroPedido">Nro. Pedido:</label>
                                <input type="text" id="numeroPedido" class="form-control" readonly>
                            </div>
                        </div>
                    </div>

                    <!-- Resumen de Productos -->
                    <div class="products-summary mb-4">
                        <h5>Resumen de Productos</h5>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Item</th>
                                    <th>Descripción</th>
                                    <th>Precio</th>
                                    <th>Cantidad</th>
                                    <th>IGV</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody id="listaResumenProductos">
                                <!-- Los productos seleccionados se cargarán aquí dinámicamente desde JavaScript -->
                            </tbody>
                        </table>
                    </div>

                    <!-- Totales del Pedido -->
                    <div class="order-totals mb-4">
                        <div class="row">
                            <div class="col-md-4 offset-md-8">
                                <div class="d-flex justify-content-between">
                                    <span>Subtotal:</span>
                                    <span id="subtotalPedido">0.00</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span>IGV:</span>
                                    <span id="igvPedido">0.00</span>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span>Total:</span>
                                    <span id="totalPedido">0.00</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Módulo 4: Content Pay -->
                <div class="container pago pt-2 pb-2 d-none" id="pago" style="border: 1px solid #ddd; border-radius: 5px; margin-left: auto; margin-right: auto; padding: 20px; max-width: 600px;">
                    <h3 class="text-center pb-5" style="padding-bottom: 20px; color: #EC6959; ">Métodos de Pago</h3>
                    <form id="formPago" class="pb-5">
                        <label for="tarjeta" style="font-weight: 700; ">Tipo de Tarjeta</label>  
                        <select id="tarjeta" class="form-control mt-2" required>
                            <option value="credito">Tarjeta de Crédito</option>
                            <option value="debito">Tarjeta de Débito</option>
                            <option value="mastercard">Mastercard</option>
                        </select>
                        <label for="card-number" style="font-weight: 700; ">Número de la Tarjeta</label>
                        <input type="text" id="card-number" class="form-control mt-2" placeholder="Ejemplo: (0000-0000-0000-0000)" pattern="\d*" maxlength="16" required>
                        <label for="cvv" style="font-weight: 700; ">Ingresar tu CVV (tu número de 3 dígitos)</label>
                        <input type="text" id="cvv" class="form-control mt-2" placeholder="Ejemplo de CVV: *123*" pattern="\d*" maxlength="3" required>
                        <label for="expired" style="font-weight: 700; ">Ingresar Fecha de Expiración</label>
                        <input id="expired" class="form-control mt-2" type="text" autocomplete="cc-exp" placeholder="MM/YY" maxlength="5" pattern="[0-9/]+" required>
                        <div class="text-center mt-4">
                            <button type="submit" class="btn btn-success" style="background-color: green;">Realizar Pedido</button>
                        </div>
                    </form>
                </div>

                <div class="container d-flex justify-content-center pb-5" style="margin-top: 20px;">
                    <div class="d-flex justify-content-between align-items-center" style="width: 80%">
                        <button id="prev-btn-progress" onclick="prevStep()" class="btn btn-primary d-flex align-items-center justify-content-around rounded-pill" style="width: 90px; height: 40px">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8"/>
                            </svg>
                            <span>Antes<span>
                        </button>
                        <button id="next-btn-progress" onclick="nextStep()" class="btn btn-success d-flex align-items-center justify-content-around rounded-pill" style="width: 120px; height: 40px">
                            <span>Siguiente<span>
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-right" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="resources/js/progress-bar.js"></script>
        <script src="resources/js/Pedido.js?timestamp=<%= System.currentTimeMillis()%>"></script>
    </body>
</html>
