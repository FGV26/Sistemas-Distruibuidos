
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="entidades.Usuario"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Usuario> listaDespachadores = (List<Usuario>) request.getAttribute("ListaDespachadores");
%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" 
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
        <meta charset="UTF-8">
        <title>Gestión de Despachadores</title>
    </head>
    <body>
        <div class="container">
            <h1 class="mt-4">Gestión de Despachadores</h1>

            <!-- Formulario de búsqueda -->
            <form class="form-inline my-3" action="GestionDespachadores" method="GET">
                <input type="hidden" name="accion" value="Buscar">
                <input type="text" name="nombre" class="form-control mr-sm-2" placeholder="Buscar por nombre o usuario" required>
                <button type="submit" class="btn btn-outline-success my-2 my-sm-0">Buscar</button>
                <a href="GestionDespachadores?accion=Listar" class="btn btn-outline-primary my-2 my-sm-0 ml-2">Mostrar Todo</a>
            </form>

            <!-- Botones para agregar despachador y volver al menú administrador -->
            <div class="d-flex justify-content-start mb-3">
                <button type="button" class="btn btn-info mr-2" data-toggle="modal" data-target="#agregarDespachadorModal">Agregar Despachador</button>
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

            <!-- Tabla de despachadores -->
            <div class="d-flex">
                <div class="col-sm-12">
                    <table class="table table-hover">
                        <thead class="thead-light">
                            <tr>
                                <th>ID</th>
                                <th>Usuario</th>
                                <th>Rol</th>
                                <th>Email</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="despachador" items="${ListaDespachadores}">
                                <tr>
                                    <td>${despachador.idUsuario}</td>
                                    <td>${despachador.username}</td>
                                    <td>${despachador.rol}</td>
                                    <td>${despachador.email}</td>
                                    <td>${despachador.nombre}</td>
                                    <td>${despachador.apellido}</td>
                                    <td>${despachador.estado}</td>
                                    <td>
                                        <div class="btn-group">
                                            <!-- Editar -->
                                            <button class="btn btn-warning editBtn" data-toggle="modal" data-target="#editModal"
                                                    data-id="${despachador.idUsuario}" data-username="${despachador.username}"
                                                    data-email="${despachador.email}" data-nombre="${despachador.nombre}" 
                                                    data-apellido="${despachador.apellido}" data-estado="${despachador.estado}">
                                                <i class="fas fa-edit"></i> Editar
                                            </button>
                                            <!-- Eliminar -->
                                            <a href="GestionDespachadores?accion=Eliminar&Id=${despachador.idUsuario}"
                                               class="btn btn-danger ml-2"
                                               onclick="return confirm('¿Estás seguro de que deseas eliminar este despachador?');">
                                                <i class="fas fa-trash-alt"></i> Eliminar
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Modal para agregar despachador -->
            <div class="modal fade" id="agregarDespachadorModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Despachador</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <!-- Formulario con validación -->
                            <form action="GestionDespachadores" method="POST" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Crear">
                                <div class="form-group">
                                    <label for="txtUsuario">Usuario</label>
                                    <input type="text" name="username" id="txtUsuario" class="form-control" required>
                                    <div class="invalid-feedback">El campo usuario es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="txtClave">Clave</label>
                                    <div class="input-group">
                                        <input type="password" name="password" id="txtClave" class="form-control" required>
                                        <div class="input-group-append">
                                            <button type="button" class="btn btn-outline-secondary toggle-password">
                                                <i class="fas fa-eye"></i>
                                            </button>
                                        </div>
                                        <div class="invalid-feedback">El campo clave es obligatorio.</div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="txtNombre">Nombre</label>
                                    <input type="text" name="nombre" id="txtNombre" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="txtApellido">Apellido</label>
                                    <input type="text" name="apellido" id="txtApellido" class="form-control" required>
                                    <div class="invalid-feedback">El campo apellido es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="txtEmail">Email</label>
                                    <input type="email" name="email" id="txtEmail" class="form-control" required>
                                    <div class="invalid-feedback">Debe ingresar un email válido.</div>
                                </div>
                                <div class="form-group">
                                    <label for="txtEstado">Estado</label>
                                    <select name="estado" id="txtEstado" class="form-control" required>
                                        <option value="Activo">Activo</option>
                                        <option value="Inactivo">Inactivo</option>
                                    </select>
                                    <div class="invalid-feedback">Debe seleccionar un estado.</div>
                                </div>
                                <div style="display: flex; justify-content: center;">
                                    <input type="submit" class="btn btn-info" value="Agregar">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para editar despachador -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Despachador</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionDespachadores" method="POST" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Modificar">
                                <input type="hidden" name="idUsuario" id="editIdUsuario">

                                <!-- Usuario -->
                                <div class="form-group">
                                    <label for="editUsername">Usuario</label>
                                    <input type="text" name="username" id="editUsername" class="form-control" required>
                                    <div class="invalid-feedback">El campo usuario es obligatorio.</div>
                                </div>

                                <!-- Clave -->
                                <div class="form-group">
                                    <label for="editPassword">Clave</label>
                                    <div class="input-group">
                                        <input type="password" name="password" id="editPassword" class="form-control" 
                                               placeholder="Dejar en blanco para mantener la contraseña actual">
                                        <div class="input-group-append">
                                            <span class="input-group-text">
                                                <i class="fas fa-eye-slash" id="togglePassword"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <!-- Nombre, Apellido, Email, Estado -->
                                <div class="form-group">
                                    <label for="editNombre">Nombre</label>
                                    <input type="text" name="nombre" id="editNombre" class="form-control" required>
                                    <div class="invalid-feedback">El campo nombre es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="editApellido">Apellido</label>
                                    <input type="text" name="apellido" id="editApellido" class="form-control" required>
                                    <div class="invalid-feedback">El campo apellido es obligatorio.</div>
                                </div>
                                <div class="form-group">
                                    <label for="editEmail">Email</label>
                                    <input type="email" name="email" id="editEmail" class="form-control" required>
                                    <div class="invalid-feedback">Debe ingresar un email válido.</div>
                                </div>
                                <div class="form-group">
                                    <label for="editEstado">Estado</label>
                                    <select name="estado" id="editEstado" class="form-control" required>
                                        <option value="Activo">Activo</option>
                                        <option value="Inactivo">Inactivo</option>
                                    </select>
                                    <div class="invalid-feedback">Debe seleccionar un estado.</div>
                                </div>

                                <div style="display: flex; justify-content: center;">
                                    <input type="submit" class="btn btn-success" value="Guardar cambios">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- JavaScript para mostrar/ocultar la contraseña -->
            <script>
                $(document).ready(function () {
                    $('.editBtn').on('click', function () {
                        var id = $(this).data('id');
                        var username = $(this).data('username');
                        var nombre = $(this).data('nombre');
                        var apellido = $(this).data('apellido');
                        var email = $(this).data('email');
                        var estado = $(this).data('estado');

                        $('#editIdUsuario').val(id);
                        $('#editUsername').val(username);
                        $('#editPassword').val('');
                        $('#editNombre').val(nombre);
                        $('#editApellido').val(apellido);
                        $('#editEmail').val(email);
                        $('#editEstado').val(estado);
                    });

                    $('.toggle-password').click(function () {
                        let input = $(this).closest('.input-group').find('input');
                        let icon = $(this).find('i');
                        if (input.attr('type') === 'password') {
                            input.attr('type', 'text');
                            icon.removeClass('fa-eye').addClass('fa-eye-slash');
                        } else {
                            input.attr('type', 'password');
                            icon.removeClass('fa-eye-slash').addClass('fa-eye');
                        }
                    });
                });

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
            </script>

        </div>
    </body>
</html>
