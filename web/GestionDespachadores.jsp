<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="entidades.Usuario"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Usuario> listaDespachadores = (List<Usuario>) request.getAttribute("ListaDespachadores");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Despachadores</title>
    <!-- Bootstrap 5.3.3 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
<body>
    <div class="container">
        <header class="mt-4 mb-4 border rounded app-header">
            <div class="d-flex align-items-center" >
                <div class="d-flex justify-content-start align-items-center">
                    <h4 class="m-4">Gestión de Despachadores<h4/>
                </div>
            </div>
        </header>
        
        
        <div class="border rounded" style="min-height: 80vh;">

        <!-- Busqueda y botones arriba de la tabla -->
            <div class="mt-4 mb-4 p-4">
                <form action="GestionDespachadores" method="GET">
                    <div class="input-group mb-3">
                        <input type="hidden" name="accion" value="Buscar">
                        <input type="text" name="nombre" class="form-control mr-sm-2" placeholder="Buscar por nombre o usuario" required>
                        <button type="submit" name="accion" class="btn btn-outline-success">Buscar</button>
                    </div>
                </form>
                <div class="input-group mb-3 justify-content-end">
                    <a class="btn btn-outline-primary"" role="button" href="GestionDespachadores?accion=Listar">Mostrar Todo</a>
                    <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#agregarDespachadorModal">Agregar Despachador</button>
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

        <!-- Tabla de despachadores -->
        <div class="container mt-4 mb-4">
            <table class="table">
                <thead>
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
                                    <button class="btn btn-primary editBtn" data-bs-toggle="modal" data-bs-target="#editModal"
                                            data-id="${despachador.idUsuario}" data-username="${despachador.username}" 
                                            data-email="${despachador.email}" data-nombre="${despachador.nombre}" 
                                            data-apellido="${despachador.apellido}" data-estado="${despachador.estado}">
                                        <i class="fas fa-edit"></i> 
                                    </button>
                                    <!-- Eliminar -->
                                    <a href="GestionDespachadores?accion=Eliminar&Id=${despachador.idUsuario}" 
                                       class="btn btn-danger ml-2" 
                                       onclick="return confirm('¿Estás seguro de que deseas eliminar este despachador?');">
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

        <!-- Modal para agregar despachador -->
        <div class="modal fade" id="agregarDespachadorModal" tabindex="-1" aria-labelledby="agregarDespachadorModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="agregarDespachadorModalLabel">Agregar Despachador</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    
                        <form action="GestionDespachadores" method="POST">
                            <div class="modal-body">
                            <input type="hidden" name="accion" value="Crear">
                            <div class="mb-3">
                                <label for="txtUsuario" class="form-label">Usuario</label>
                                <input type="text" name="username" id="txtUsuario" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="txtClave" class="form-label">Clave</label>
                                <input type="password" name="password" id="txtClave" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="txtNombre" class="form-label">Nombre</label>
                                <input type="text" name="nombre" id="txtNombre" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="txtApellido" class="form-label">Apellido</label>
                                <input type="text" name="apellido" id="txtApellido" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="txtEmail" class="form-label">Email</label>
                                <input type="email" name="email" id="txtEmail" class="form-control" required>
                            </div>
                            <div>
                                <label for="txtEstado" class="form-label">Estado</label>
                                <select name="estado" id="txtEstado" class="form-select" required>
                                    <option value="Activo">Activo</option>
                                    <option value="Inactivo">Inactivo</option>
                                </select>
                            </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                <button type="submit" class="btn btn-primary">Agregar despachador</button>
                            </div>
                        </form>
                </div>
            </div>
        </div>

        <!-- Modal para editar despachador -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Despachador</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="GestionDespachadores" method="POST">
                            <input type="hidden" name="accion" value="Modificar">
                            <input type="hidden" name="idUsuario" id="editIdUsuario">
                            <div class="mb-3">
                                <label for="editUsername" class="form-label">Usuario</label>
                                <input type="text" name="username" id="editUsername" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editPassword" class="form-label">Clave</label>
                                <input type="password" name="password" id="editPassword" class="form-control">
                            </div>
                            <div class="mb-3">
                                <label for="editNombre" class="form-label">Nombre</label>
                                <input type="text" name="nombre" id="editNombre" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editApellido" class="form-label">Apellido</label>
                                <input type="text" name="apellido" id="editApellido" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editEmail" class="form-label">Email</label>
                                <input type="email" name="email" id="editEmail" class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editEstado" class="form-label">Estado</label>
                                <select name="estado" id="editEstado" class="form-select" required>
                                    <option value="Activo">Activo</option>
                                    <option value="Inactivo">Inactivo</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-warning w-100">Guardar Cambios</button>
                        </form>
                    </div>
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

    <!-- Bootstrap JS Bundle (Incluye Popper) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script>
        // Script para llenar el modal de editar
        var editModal = document.getElementById('editModal');
        editModal.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget;
            document.getElementById('editIdUsuario').value = button.getAttribute('data-id');
            document.getElementById('editUsername').value = button.getAttribute('data-username');
            document.getElementById('editEmail').value = button.getAttribute('data-email');
            document.getElementById('editNombre').value = button.getAttribute('data-nombre');
            document.getElementById('editApellido').value = button.getAttribute('data-apellido');
            document.getElementById('editEstado').value = button.getAttribute('data-estado');
        });
    </script>
</body>
</html>

