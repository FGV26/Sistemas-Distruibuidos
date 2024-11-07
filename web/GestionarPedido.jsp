
<%@page import="entidades.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestionar Pedido</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
        
        <!-- Contenido Principal -->
        <div class="container mt-5">
            <h2 class="text-center">Gestión de Pedido</h2>
            <p class="text-center">Aquí podrás gestionar el pedido seleccionando un cliente y añadiendo productos.</p>
        </div>

        <!-- Modal Inicial para la selección de cliente -->
        <div class="modal fade" id="clienteModal" tabindex="-1" aria-labelledby="clienteModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="clienteModalLabel">¿El cliente está registrado?</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>Seleccione una opción para continuar con el proceso del pedido:</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" onclick="abrirModalRegistro()">Registrar Cliente</button>
                        <button type="button" class="btn btn-secondary" onclick="abrirModalBuscarDni()">Cliente Registrado</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal de Registro de Cliente -->
        <div class="modal fade" id="registroClienteModal" tabindex="-1" aria-labelledby="registroClienteLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="registroClienteLabel">Registrar Un nuevo Cliente</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <h6>Completar el Siguiente Formulario</h6>
                        <form id="registroClienteForm">
                            <div class="mb-3">
                                <label for="nombre" class="form-label"><i class="fas fa-user"></i> Ingrese Nombre</label>
                                <input type="text" class="form-control" id="nombre" placeholder="Ingrese Nombre">
                            </div>
                            <div class="mb-3">
                                <label for="apellido" class="form-label"><i class="fas fa-user"></i> Ingrese Apellido</label>
                                <input type="text" class="form-control" id="apellido" placeholder="Ingrese Apellido">
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label"><i class="fas fa-home"></i> Ingrese Dirección</label>
                                <input type="text" class="form-control" id="direccion" placeholder="Ingrese Dirección">
                            </div>
                            <div class="mb-3">
                                <label for="dni" class="form-label"><i class="fas fa-id-card"></i> Ingrese DNI</label>
                                <input type="text" class="form-control" id="dni" placeholder="Ingrese DNI">
                            </div>
                            <div class="mb-3">
                                <label for="telefono" class="form-label"><i class="fas fa-phone"></i> Ingrese Teléfono</label>
                                <input type="text" class="form-control" id="telefono" placeholder="Ingrese Teléfono">
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label"><i class="fas fa-envelope"></i> Ingrese Email</label>
                                <input type="email" class="form-control" id="email" placeholder="Ingrese Email">
                            </div>
                            <button type="button" class="btn btn-primary" onclick="submitRegistro()">Registrar Cliente</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal para Buscar Cliente por DNI -->
        <div class="modal fade" id="buscarDniModal" tabindex="-1" aria-labelledby="buscarDniLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="buscarDniLabel">Ingrese su DNI por favor</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <div class="input-group mb-3">
                            <span class="input-group-text"><i class="fas fa-id-card"></i></span>
                            <input type="text" class="form-control" id="dniBuscar" placeholder="Ingrese DNI">
                            <button class="btn btn-primary" onclick="buscarCliente()">Buscar</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal de Advertencia -->
        <div class="modal fade" id="advertenciaModal" tabindex="-1" aria-labelledby="advertenciaLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="advertenciaLabel">Advertencia</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p>No se puede realizar un pedido sin un cliente seleccionado.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" onclick="abrirModalCliente()">Ingresar Cliente</button>
                        <button type="button" class="btn btn-secondary" onclick="redirigirDashboard()">Volver a Dashboard</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS y Popper -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.1.3/js/bootstrap.bundle.min.js"></script>

        <script>
                            // Variable de control para evitar la advertencia tras interactuar con el modal
                            let clienteSeleccionado = false;

                            // Función para abrir el modal de registro de cliente y cerrar el modal principal
                            function abrirModalRegistro() {
                                clienteSeleccionado = true;
                                var clienteModal = bootstrap.Modal.getInstance(document.getElementById('clienteModal'));
                                clienteModal.hide();
                                var modal = new bootstrap.Modal(document.getElementById('registroClienteModal'));
                                modal.show();
                            }

                            // Función para abrir el modal para buscar por DNI y cerrar el modal principal
                            function abrirModalBuscarDni() {
                                clienteSeleccionado = true;
                                var clienteModal = bootstrap.Modal.getInstance(document.getElementById('clienteModal'));
                                clienteModal.hide();
                                var modal = new bootstrap.Modal(document.getElementById('buscarDniModal'));
                                modal.show();
                            }

                            // Función para abrir el modal inicial de cliente
                            function abrirModalCliente() {
                                clienteSeleccionado = false; // Resetear la variable de control
                                var advertenciaModal = bootstrap.Modal.getInstance(document.getElementById('advertenciaModal'));
                                advertenciaModal.hide();
                                var clienteModal = new bootstrap.Modal(document.getElementById('clienteModal'));
                                clienteModal.show();
                            }

                            // Función para redirigir al Dashboard
                            function redirigirDashboard() {
                                window.location.href = "DashboardActividades?accion=Listar";
                            }

                            // Placeholder para registrar cliente
                            function submitRegistro() {
                                alert("Formulario de registro enviado (simulado)");
                                var modal = bootstrap.Modal.getInstance(document.getElementById('registroClienteModal'));
                                modal.hide();
                            }

                            // Placeholder para buscar cliente
                            function buscarCliente() {
                                alert("Buscar cliente por DNI (simulado)");
                                var modal = bootstrap.Modal.getInstance(document.getElementById('buscarDniModal'));
                                modal.hide();
                            }

                            // Mostrar el modal inicial al cargar la página
                            document.addEventListener("DOMContentLoaded", function () {
                                clienteSeleccionado = false;
                                var clienteModal = new bootstrap.Modal(document.getElementById('clienteModal'));
                                clienteModal.show();
                            });

                            // Solo mostrar advertencia si no se seleccionó ningún cliente
                            document.getElementById('clienteModal').addEventListener('hidden.bs.modal', function () {
                                if (!clienteSeleccionado) {
                                    var advertenciaModal = new bootstrap.Modal(document.getElementById('advertenciaModal'));
                                    advertenciaModal.show();
                                }
                            });

                            // Redirigir al dashboard si el modal de advertencia se cierra sin acción
                            document.getElementById('advertenciaModal').addEventListener('hidden.bs.modal', function () {
                                if (!clienteSeleccionado) {
                                    redirigirDashboard();
                                }
                            });
        </script>

    </body>
</html>
