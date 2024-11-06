
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
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" crossorigin="anonymous"></script>
        <meta charset="UTF-8">
        <title>Gestión de Categorías</title>
    </head>
    <body>
        <div class="container">
            <h1 class="mt-4">Gestión de Categorías</h1>

            <!-- Formulario de búsqueda -->
            <form class="form-inline my-3" action="GestionCategoria" method="GET">
                <input type="hidden" name="accion" value="Buscar">
                <input type="text" name="nombre" class="form-control mr-sm-2" placeholder="Buscar por nombre" required>
                <button type="submit" class="btn btn-outline-success my-2 my-sm-0">Buscar</button>
                <a href="GestionCategoria?accion=Listar" class="btn btn-outline-primary my-2 my-sm-0 ml-2">Mostrar Todo</a>
            </form>

            <!-- Botones para agregar categoría y volver al menú administrador -->
            <div class="d-flex justify-content-start mb-3">
                <button type="button" class="btn btn-info mr-2" data-toggle="modal" data-target="#agregarCategoriaModal">Agregar Categoría</button>
                <a href="DashboardActividades?accion=Listar" class="btn btn-outline-secondary">Volver a Menú Administrador</a>
            </div>

            <!-- Mensajes de éxito o error -->
            <% String mensaje = request.getParameter("mensaje");
                String error = request.getParameter("error"); %>

            <% if (mensaje != null) {%>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= mensaje%>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <% } else if (error != null) {%>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error%>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <% }%>

            <!-- Tabla de categorías -->
            <table class="table table-hover">
                <thead class="thead-light">
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
                                    <button class="btn btn-warning editBtn" data-toggle="modal" data-target="#editModal"
                                            data-id="${categoria.idCategoria}" data-nombre="${categoria.nombre}"
                                            data-descripcion="${categoria.descripcion}">
                                        <i class="fas fa-edit"></i> Editar
                                    </button>
                                    <!-- Eliminar -->
                                    <a href="GestionCategoria?accion=Eliminar&idCategoria=${categoria.idCategoria}"
                                       class="btn btn-danger ml-2"
                                       onclick="return confirm('¿Estás seguro de que deseas eliminar esta categoría?');">
                                        <i class="fas fa-trash-alt"></i> Eliminar
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Modal para agregar categoría -->
            <div class="modal fade" id="agregarCategoriaModal" tabindex="-1" role="dialog" aria-labelledby="agregarCategoriaLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Categoría</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionCategoria" method="POST" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Crear">
                                <div class="form-group">
                                    <label for="nombreCategoria">Nombre</label>
                                    <input type="text" name="nombre" id="nombreCategoria" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="descripcionCategoria">Descripción</label>
                                    <textarea name="descripcion" id="descripcionCategoria" class="form-control" required></textarea>
                                    <div class="invalid-feedback">El campo descripción es obligatorio.</div>
                                </div>
                                <button type="submit" class="btn btn-info">Agregar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para editar categoría -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Categoría</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionCategoria" method="POST" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Modificar">
                                <input type="hidden" name="idCategoria" id="editIdCategoria">
                                <div class="form-group">
                                    <label for="editNombre">Nombre</label>
                                    <input type="text" name="nombre" id="editNombre" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="editDescripcion">Descripción</label>
                                    <textarea name="descripcion" id="editDescripcion" class="form-control" required></textarea>
                                    <div class="invalid-feedback">El campo descripción es obligatorio.</div>
                                </div>
                                <button type="submit" class="btn btn-success">Guardar cambios</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

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
