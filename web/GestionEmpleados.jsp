<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="entidades.Usuario"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    List<Usuario> listaEmpleados = (List<Usuario>) request.getAttribute("ListaEmpleados");
%>

<!DOCTYPE html>
<html>
    <head>
        
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js" crossorigin="anonymous"></script>
        <script src="https://kit.fontawesome.com/26a3cc7edf.js" crossorigin="anonymous"></script>
        <meta charset="UTF-8">
        <title>Gestión de Empleados</title>
    </head>
    <body>
        
        <main class="container">
            
            <header class="mt-4 mb-4 border rounded app-header">
                <div class="d-flex align-items-center" >
                    <div class="d-flex justify-content-start align-items-center">
                        <h4 class="m-4">Gestión de Empleados<h4/>
                    </div>
                </div>
            </header>
                <div class="border rounded" style="min-height: 80vh;">
                    
                    <!-- Busqueda y botones arriba de la tabla -->
                    <div class="mt-4 mb-4 p-4">
                        <form action="GestionEmpleados" method="GET">
                            <div class="input-group mb-3">
                                <input type="text" name="nombre" class="form-control" placeholder="Buscar por nombre o usuario" required>
                                <button type="submit" name="accion" class="btn btn-outline-success">Buscar</button>
                            </div>
                        </form>
                        <div class="input-group mb-3 justify-content-end">
                            <a class="btn btn-outline-primary"" role="button" href="GestionEmpleados?accion=Listar">Mostrar Todo</a>
                            <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#agregarEmpleadoModal">Agregar empleado</button>
                            <a href="DashboardActividades?accion=Listar" role="button" class="btn btn-outline-dark">Menú Administrador</a>
                        </div>
                    </div>
                    
                    <!-- Tabla -->
                    
                    <div class="container mt-4 mb-4">
                        <table class="table">
                            <thead>
                              <tr>
                                <th scope="col">ID</th>
                                <th scope="col">Usuario</th>
                                <th scope="col">Rol</th>
                                <th scope="col">Email</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Apellido</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Acciones</th>
                              </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="empleado" items="${ListaEmpleados}">
                                  <tr>
                                      <td>${empleado.idUsuario}</td>
                                      <td>${empleado.username}</td>
                                      <td>${empleado.rol}</td>
                                      <td>${empleado.email}</td>
                                      <td>${empleado.nombre}</td>
                                      <td>${empleado.apellido}</td>
                                      <td>${empleado.estado}</td>
                                      <td>
                                          <div class="btn-group">
                                              <!-- Editar -->
                                              <button class="btn btn-primary editBtn" data-bs-toggle="modal" data-bs-target="#editModal"
                                                      data-id="${empleado.idUsuario}" data-username="${empleado.username}"
                                                      data-email="${empleado.email}" data-nombre="${empleado.nombre}" 
                                                      data-apellido="${empleado.apellido}" data-estado="${empleado.estado}">
                                                  <i class="fas fa-edit"></i>
                                              </button>
                                              <!-- Eliminar -->
                                              <a href="GestionEmpleados?accion=Eliminar&Id=${empleado.idUsuario}"
                                                 class="btn btn-danger ml-2"
                                                 onclick="return confirm('¿Estás seguro de que deseas eliminar este empleado?');">
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
            
            
        <main>
        
        
        <div class="container">

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

            <!-- Modal para agregar empleado -->
            <div class="modal fade" id="agregarEmpleadoModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <!-- Header -->
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">Agregar Empleado</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <!-- body -->
                        <div class="modal-body">
                            <!-- Formulario con validación -->
                            <form action="GestionEmpleados" method="POST" class="needs-validation" novalidate>
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
                                
                                <div class="d-grid gap-2 col-6 mx-auto mt-2">
                                    <button class="btn btn-primary" type="submit">Agregar</button>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para editar empleado -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        
                        <!-- Header -->
                        
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">Editar Empleado</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        
                        <!-- Body -->
                        
                        <div class="modal-body">
                            <form action="GestionEmpleados" method="POST" class="needs-validation" novalidate>
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
                                        <span class="input-group-text">
                                            <i class="fas fa-eye-slash" id="togglePassword"></i>
                                        </span>
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
                                
                                <div class="d-grid gap-2 col-6 mx-auto mt-4">
                                    <button class="btn btn-primary" type="submit">Guardar cambios</button>
                                </div>
 
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- JavaScript para mostrar/ocultar la contraseña -->
            <script>
                $(document).ready(function () {
                    // Cargar datos en el formulario de edición sin mostrar la contraseña hasheada
                    $('.editBtn').on('click', function () {
                        var id = $(this).data('id');
                        var username = $(this).data('username');
                        var nombre = $(this).data('nombre');
                        var apellido = $(this).data('apellido');
                        var email = $(this).data('email');
                        var estado = $(this).data('estado');

                        $('#editIdUsuario').val(id);
                        $('#editUsername').val(username);
                        $('#editPassword').val(''); // Mantener el campo de contraseña vacío
                        $('#editNombre').val(nombre);
                        $('#editApellido').val(apellido);
                        $('#editEmail').val(email);
                        $('#editEstado').val(estado);
                    });

                    // Mostrar/ocultar la contraseña en los campos de contraseña
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

                // Validación de formulario de Bootstrap
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
            
            <!-- Boostrap -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        </div>
    </body>
</html>
