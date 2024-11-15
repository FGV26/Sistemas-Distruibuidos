
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entidades.Categoria" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%
    List<Categoria> listaCategorias = (List<Categoria>) request.getAttribute("ListaCategorias");
%>

<!DOCTYPE html>
<html>
    <head>
        
        <meta charset="UTF-8">
        <title>Gestión de Categorías</title>
        <!-- Bootstrap 5.3.3 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
    </head>
    <body>
        <div class="container">
            
            <header class="mt-4 mb-4 border rounded app-header">
                <div class="d-flex align-items-center" >
                    <div class="d-flex justify-content-start align-items-center">
                        <h4 class="m-4">Gestión de Categorías<h4/>
                    </div>
                </div>
            </header>
            <div class="border rounded" style="min-height: 80vh;">
                <!-- Busqueda y botones arriba de la tabla -->
                    <div class="mt-4 mb-4 p-4">
                        <form action="GestionEmpleados" method="GET">
                            <div class="input-group mb-3">
                                <input type="text" name="nombre" class="form-control" placeholder="Buscar por categoría" required>
                                <button type="submit" name="accion" class="btn btn-outline-success">Buscar</button>
                            </div>
                        </form>
                        <div class="input-group mb-3 justify-content-end">
                            <a class="btn btn-outline-primary"" role="button" href="GestionCategoria?accion=Listar">Mostrar Todo</a>
                            <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#agregarCategoriaModal">Agregar categoría</button>
                            <a href="DashboardActividades?accion=Listar" role="button" class="btn btn-outline-dark">Menú Administrador</a>
                        </div>
                    </div>

            <!-- Mensajes de éxito o error -->
            <% String mensaje = request.getParameter("mensaje");
            String error = request.getParameter("error"); %>

            <% if (mensaje != null) {%>
            
            <div aria-live="polite" aria-atomic="true" class="position-relative">
                <div class="toast-container position-fixed top-0 end-0 p-3 show">
                    <div class="toast bg-success fade show" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body text-white">
                                <%= mensaje%>
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                </div>
            </div>
                            
            <% } else if (error != null) {%>
            
            <div aria-live="polite" aria-atomic="true" class="position-relative">
                <div class="toast-container position-fixed top-0 end-0 p-3 show">
                    <div class="toast bg-danger fade show" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body text-white">
                                <%= error%>
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                </div>
            </div>
                            
            <% }%>

            <!-- Tabla de categorías -->
            <div class="container mt-4 mb-4">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Descripción</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="categoria" items="${ListaCategorias}">
                            <tr>
                                <td>${categoria.idCategoria}</td>
                                <td>${categoria.nombre}</td>
                                <td>${categoria.descripcion}</td>
                                <td>
                                    <div class="btn-group">
                                        <!-- Editar -->
                                        <button class="btn btn-primary editBtn" data-bs-toggle="modal" data-bs-target="#editModal"
                                                data-id="${categoria.idCategoria}" data-nombre="${categoria.nombre}"
                                                data-descripcion="${categoria.descripcion}">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <!-- Eliminar -->
                                        <a href="GestionCategoria?accion=Eliminar&idCategoria=${categoria.idCategoria}"
                                           class="btn btn-danger ml-2"
                                           onclick="return confirm('¿Estás seguro de que deseas eliminar esta categoría?');">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

            <!-- Modal para agregar categoría -->
            <div class="modal fade" id="agregarCategoriaModal" tabindex="-1" role="dialog" aria-labelledby="agregarCategoriaLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Categoría</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                            <form action="GestionCategoria" method="POST" class="needs-validation" novalidate>
                                <div class="modal-body">
                                <input type="hidden" name="accion" value="Crear">
                                <div class="form-group">
                                    <label for="nombreCategoria">Nombre</label>
                                    <input type="text" name="nombre" id="nombreCategoria" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group mb-3">
                                    <label for="descripcionCategoria">Descripción</label>
                                    <textarea name="descripcion" id="descripcionCategoria" class="form-control" required></textarea>
                                    <div class="invalid-feedback">El campo descripción es obligatorio.</div>
                                </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                    <button type="submit" class="btn btn-primary">Agregar categoría</button>
                                </div>
                            </form>
                    </div>
                </div>
            </div>

            <!-- Modal para editar categoría -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Categoría</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionCategoria" method="POST" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Modificar">
                                <input type="hidden" name="idCategoria" id="editIdCategoria">
                                <div class="form-group mb-3">
                                    <label for="editNombre">Nombre</label>
                                    <input type="text" name="nombre" id="editNombre" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group mb-3">
                                    <label for="editDescripcion">Descripción</label>
                                    <textarea name="descripcion" id="editDescripcion" class="form-control" required></textarea>
                                    <div class="invalid-feedback">El campo descripción es obligatorio.</div>
                                </div>
                                <button type="submit" class="btn btn-warning w-100">Guardar Cambios</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

            <script>
                // Lógica para el modal de editar
                $(document).ready(function () {
                    $('.editBtn').on('click', function () {
                        var id = $(this).data('id');
                        var nombre = $(this).data('nombre');
                        var descripcion = $(this).data('descripcion');

                        $('#editIdCategoria').val(id);
                        $('#editNombre').val(nombre);
                        $('#editDescripcion').val(descripcion);
                    });

                    // Validación de formulario
                    (function () {
                        'use strict';
                        window.addEventListener('load', function () {
                            var forms = document.getElementsByClassName('needs-validation');
                            Array.prototype.filter.call(forms, function (form) {
                                form.addEventListener('submit', function (event) {
                                    if (form.checkValidity() === false) {
                                        event.preventDefault();
                                        event.stopPropagation();
                                    }
                                    form.classList.add('was-validated');
                                }, false);
                            });
                        }, false);
                    })();
                });
            </script>
        </div>
    </body>
</html>
