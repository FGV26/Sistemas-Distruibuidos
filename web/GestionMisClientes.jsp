
<%@page import="entidades.Cliente"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Mis Clientes</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    </head>
    <body>

        <div class="container mt-4">
            <h1 class="mb-4">Gestión de Mis Clientes</h1>

            <!-- Barra de búsqueda y botones -->
            <form action="ControlerCliente" method="GET" class="d-flex mb-3">
                <input type="hidden" name="accion" value="Buscar">
                <input type="text" name="dniBusqueda" class="form-control me-2" placeholder="Buscar por DNI">
                <button type="submit" class="btn btn-outline-success me-2">Buscar</button>
                <a href="ControlerCliente?accion=Listar" class="btn btn-outline-primary me-2">Mostrar Todo</a>
                <button type="button" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#agregarClienteModal">Agregar Cliente</button>
                <a href="DashboardActividades?accion=Listar" class="btn btn-secondary">Volver al Dashboard</a>
            </form>

            <!-- Tabla de clientes -->
            <table class="table table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Código Cliente</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Dirección</th> <!-- Nueva columna de Dirección -->
                        <th>DNI</th>
                        <th>Teléfono</th>
                        <th>Email</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cliente" items="${listaClientes}">
                        <tr>
                            <td>${cliente.idCliente}</td>
                            <td>${cliente.codCliente}</td>
                            <td>${cliente.nombre}</td>
                            <td>${cliente.apellido}</td>
                            <td>${cliente.direccion}</td> <!-- Mostrar dirección en la tabla -->
                            <td>${cliente.dni}</td>
                            <td>${cliente.telefono}</td>
                            <td>${cliente.email}</td>
                            <td>
                                <button class="btn btn-warning btn-sm editBtn" data-bs-toggle="modal" data-bs-target="#editarClienteModal"
                                        data-id="${cliente.idCliente}" data-codcliente="${cliente.codCliente}"
                                        data-nombre="${cliente.nombre}" data-apellido="${cliente.apellido}"
                                        data-direccion="${cliente.direccion}" data-dni="${cliente.dni}" 
                                        data-telefono="${cliente.telefono}" data-email="${cliente.email}">
                                    Editar
                                </button>
                                <a href="ControlerCliente?accion=Eliminar&Id=${cliente.idCliente}" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('¿Estás seguro de eliminar este cliente?');">
                                    Eliminar
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>


        </div>

        <div class="modal fade" id="agregarClienteModal" tabindex="-1" aria-labelledby="agregarClienteLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="agregarClienteLabel">Agregar Cliente</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="ControlerCliente" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="accion" value="Crear">
                        <div class="modal-body">
                            <!-- Campos del formulario -->
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre</label>
                                <input type="text" name="nombre" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="apellido" class="form-label">Apellido</label>
                                <input type="text" name="apellido" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label">Dirección</label> <!-- Agregamos el campo dirección -->
                                <input type="text" name="direccion" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="dni" class="form-label">DNI</label>
                                <input type="text" name="dni" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="text" name="telefono" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" name="email" class="form-control" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Agregar Cliente</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal para editar cliente -->
        <div class="modal fade" id="editarClienteModal" tabindex="-1" aria-labelledby="editarClienteLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editarClienteLabel">Editar Cliente</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="ControlerCliente" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="accion" value="Actualizar">
                        <input type="hidden" name="Id" id="editIdCliente">
                        <div class="modal-body">
                            <!-- Campos del formulario -->
                            <div class="mb-3">
                                <label for="codCliente" class="form-label">Código Cliente</label>
                                <input type="text" name="codCliente" id="editCodCliente" class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre</label>
                                <input type="text" name="nombre" id="editNombre" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="apellido" class="form-label">Apellido</label>
                                <input type="text" name="apellido" id="editApellido" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="direccion" class="form-label">Dirección</label>
                                <input type="text" name="direccion" id="editDireccion" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="dni" class="form-label">DNI</label>
                                <input type="text" name="dni" id="editDni" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input type="text" name="telefono" id="editTelefono" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" name="email" id="editEmail" class="form-control" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="submit" class="btn btn-primary">Guardar Cambios</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script>
                                       document.addEventListener("DOMContentLoaded", function () {
                                           document.querySelectorAll('.editBtn').forEach(button => {
                                               button.addEventListener('click', function () {
                                                   // Cargar todos los datos en los campos del modal
                                                   document.getElementById('editIdCliente').value = this.getAttribute('data-id');
                                                   document.getElementById('editCodCliente').value = this.getAttribute('data-codcliente');
                                                   document.getElementById('editNombre').value = this.getAttribute('data-nombre');
                                                   document.getElementById('editApellido').value = this.getAttribute('data-apellido');
                                                   document.getElementById('editDireccion').value = this.getAttribute('data-direccion');
                                                   document.getElementById('editDni').value = this.getAttribute('data-dni');
                                                   document.getElementById('editTelefono').value = this.getAttribute('data-telefono');
                                                   document.getElementById('editEmail').value = this.getAttribute('data-email');
                                               });
                                           });

                                           // Validación de formularios de Bootstrap
                                           (function () {
                                               'use strict';
                                               const forms = document.querySelectorAll('.needs-validation');
                                               Array.from(forms).forEach(form => {
                                                   form.addEventListener('submit', event => {
                                                       if (!form.checkValidity()) {
                                                           event.preventDefault();
                                                           event.stopPropagation();
                                                       }
                                                       form.classList.add('was-validated');
                                                   }, false);
                                               });
                                           })();
                                       });
        </script>

    </body>
</html>
