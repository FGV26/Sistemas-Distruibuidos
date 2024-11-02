
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="Modelo.*"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
              integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="resources/css/styleAdministrador.css">
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Gestión de Usuarios</title>
    </head>
    <body>
        <div class="container">
            <h1 class="mt-4">Gestión de Usuarios</h1>

            <!-- Formulario de búsqueda -->
            <form class="form-inline my-3" action="ControladorUsuario" method="GET">
                <input type="hidden" name="Op" value="Buscar">
                <input type="text" name="nombre" class="form-control mr-sm-2" placeholder="Buscar por nombre" required>
                <button type="submit" class="btn btn-outline-success my-2 my-sm-0">Buscar</button>
                <a href="ControladorUsuario?Op=Listar" class="btn btn-outline-primary my-2 my-sm-0 ml-2">Mostrar Todo</a>
            </form>

            <!-- Botones para agregar usuario y volver al menú administrador -->
            <div class="d-flex justify-content-start mb-3">
                <button type="button" class="btn btn-info mr-2" id="btnAgregar" data-toggle="modal" data-target="#agregarUsuarioModal">Agregar Usuario</button>
                <a href="MenuAdministrador.jsp" class="btn btn-outline-secondary">Volver a Menú Administrador</a>
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

            <!-- Tabla de usuarios -->
            <div class="d-flex">
                <div class="col-sm-12">
                    <table class="table table-hover" border="1" style="border-radius:20px; margin-top:15px;">
                        <thead>
                            <tr style="background-color:#fff; font-weight:bold;">
                                <td><center>ID</center></td>
                                <td><center>Usuario</center></td>
                                <td><center>Rol</center></td>
                                <td><center>Email</center></td>
                                <td><center>Nombre</center></td>
                                <td><center>Apellido</center></td>
                                <td><center>Estado</center></td>
                                <td><center>Acciones</center></td>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="usuario" items="${listaUsuarios}">
                                <tr>
                                    <td><center>${usuario.getIdUsuario()}</center></td>
                                    <td><center>${usuario.getUsername()}</center></td>
                                    <td><center>${usuario.getRol()}</center></td>
                                    <td><center>${usuario.getEmail()}</center></td>
                                    <td><center>${usuario.getNombre()}</center></td>
                                    <td><center>${usuario.getApellido()}</center></td>
                                    <td><center>${usuario.getEstado()}</center></td>
                                    <td>
                                        <div style="display: flex;">
                                            <a class="btn btn-warning editBtn" data-toggle="modal" data-target="#editModal" 
                                               data-id="${usuario.getIdUsuario()}" data-username="${usuario.getUsername()}"
                                               data-rol="${usuario.getRol()}" data-email="${usuario.getEmail()}"
                                               data-nombre="${usuario.getNombre()}" data-apellido="${usuario.getApellido()}">
                                                <i class="fas fa-edit"></i> Editar
                                            </a>
                                            <a href="ControladorUsuario?Op=Eliminar&idUsuario=${usuario.getIdUsuario()}" 
                                               class="btn btn-danger" style="margin-left: 5px;" 
                                               onclick="return confirm('¿Estás seguro de que deseas eliminar este usuario?');">
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

            <!-- Modal para agregar usuario -->
            <div class="modal fade" id="agregarUsuarioModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Agregar Usuario</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="ControladorUsuario" method="POST">
                                <div class="form-group">
                                    <label for="txtUsuario">Usuario</label>
                                    <input type="text" name="username" id="txtUsuario" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="txtClave">Clave</label>
                                    <input type="password" name="password" id="txtClave" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="txtRol">Rol</label>
                                    <select name="rol" id="txtRol" class="form-control" required>
                                        <option value="Empleado">Empleado</option>
                                        <option value="Despachador">Despachador</option>
                                        <option value="Administrador">Administrador</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="txtNombre">Nombre</label>
                                    <input type="text" name="nombre" id="txtNombre" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="txtApellido">Apellido</label>
                                    <input type="text" name="apellido" id="txtApellido" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="txtEmail">Email</label>
                                    <input type="email" name="email" id="txtEmail" class="form-control" required>
                                </div>
                                <div style="display: flex; justify-content: center;">
                                    <input type="submit" name="accion" value="Agregar" class="btn btn-info">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para editar usuario -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editModalLabel">Editar Usuario</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form id="editForm" action="ControladorUsuario" method="POST">
                                <input type="hidden" name="idUsuario" id="editIdUsuario">
                                <div class="form-group">
                                    <label for="editUsername">Usuario</label>
                                    <input type="text" name="username" id="editUsername" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editRol">Rol</label>
                                    <input type="text" name="rol" id="editRol" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editNombre">Nombre</label>
                                    <input type="text" name="nombre" id="editNombre" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editApellido">Apellido</label>
                                    <input type="text" name="apellido" id="editApellido" class="form-control">
                                </div>
                                <div class="form-group">
                                    <label for="editEmail">Email</label>
                                    <input type="email" name="email" id="editEmail" class="form-control" required>
                                </div>
                                <div style="display: flex; justify-content: center;">
                                    <input type="submit" name="accion" value="Editar" class="btn btn-success">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                $(document).ready(function () {
                    $('.editBtn').on('click', function () {
                        var id = $(this).data('id');
                        var username = $(this).data('username');
                        var rol = $(this).data('rol');
                        var nombre = $(this).data('nombre');
                        var apellido = $(this).data('apellido');
                        var email = $(this).data('email');

                        $('#editIdUsuario').val(id);
                        $('#editUsername').val(username);
                        $('#editRol').val(rol);
                        $('#editNombre').val(nombre);
                        $('#editApellido').val(apellido);
                        $('#editEmail').val(email);
                    });
                });
            </script>
        </div>
    </body>
</html>

