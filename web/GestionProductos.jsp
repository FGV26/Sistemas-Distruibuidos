
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="entidades.Producto" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%
    List<Producto> listaProductos = (List<Producto>) request.getAttribute("ListaProductos");
    List<String> mensajesAdvertencia = (List<String>) request.getAttribute("mensajesAdvertencia");
    List<String> tiposMensaje = (List<String>) request.getAttribute("tiposMensaje");

%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Productos</title>
        <link rel="stylesheet" href="resources/css/Alerta.css">
        
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
                    <h4 class="m-4">Gestión de Productos<h4/>
                </div>
            </div>
        </header>
        
        <div class="border rounded" style="min-height: 80vh;">

            <!-- Busqueda y botones arriba de la tabla -->
            <div class="container mt-4 mb-4 p-4">
                <form action="GestionProductos" method="GET">
                    <div class="input-group mb-3">
                        <input type="hidden" name="accion" value="Buscar">
                        <input type="text" name="nombre" class="form-control" placeholder="Buscar por nombre de producto" required>
                        <button type="submit" name="accion" class="btn btn-outline-success">Buscar</button>
                    </div>
                </form>
                <div class="input-group mb-3 justify-content-end">
                    <a class="btn btn-outline-primary"" role="button" href="GestionProductos?accion=Listar">Mostrar Todo</a>
                    <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#agregarProductoModal">Agregar producto</button>
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

            <!-- Tabla de productos -->
            <div class="container mt-4 mb-4">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Stock Mínimo</th>
                        <th>Descripción</th>
                        <th>Imagen</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="producto" items="${ListaProductos}">
                        <tr>
                            <td>${producto.idProducto}</td>
                            <td>${producto.nombre}</td>
                            <td>
                                <!-- Buscar y mostrar el nombre de la categoría -->
                                <c:forEach var="categoria" items="${ListaCategorias}">
                                    <c:if test="${categoria.idCategoria == producto.idCategoria}">
                                        ${categoria.nombre}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>${producto.precio}</td>
                            <td>${producto.stock}</td>
                            <td>${producto.stockMinimo}</td>
                            <td>${producto.descripcion}</td>
                            <td>
                                <!-- Mostrar imagen -->
                                <img src="resources/img/productos/${producto.imagen}?timestamp=<%= System.currentTimeMillis()%>" alt="${producto.nombre}" width="50" height="50">
                            </td>
                            <td>${producto.estado}</td>
                            <td>
                                <div class="btn-group">
                                    <!-- Botón Editar -->
                                    <button class="btn btn-primary editBtn" data-bs-toggle="modal" data-bs-target="#editModal"
                                            data-id="${producto.idProducto}" data-idcategoria="${producto.idCategoria}"
                                            data-nombre="${producto.nombre}" data-precio="${producto.precio}"
                                            data-stock="${producto.stock}" data-stockminimo="${producto.stockMinimo}"
                                            data-descripcion="${producto.descripcion}" data-estado="${producto.estado}">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <!-- Botón Eliminar -->
                                    <a href="GestionProductos?accion=Eliminar&idProducto=${producto.idProducto}"
                                       class="btn btn-danger ml-2"
                                       onclick="return confirm('¿Estás seguro de que deseas eliminar este producto?');">
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

            <!-- Modal para agregar producto -->
            <div class="modal fade" id="agregarProductoModal" tabindex="-1" role="dialog" aria-labelledby="agregarProductoLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Producto</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        
                            <form action="GestionProductos" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                                <div class="modal-body">
                                <input type="hidden" name="accion" value="Agregar">
                                <div class="form-group">
                                    <label for="nombreProducto">Nombre</label>
                                    <input type="text" name="nombre" id="nombreProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="idCategoria">Categoría</label>
                                    <select name="idCategoria" id="idCategoria" class="form-control" required>
                                        <c:forEach var="categoria" items="${ListaCategorias}">
                                            <option value="${categoria.idCategoria}">${categoria.nombre}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="precioProducto">Precio</label>
                                    <input type="number" step="0.01" name="precio" id="precioProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="stockProducto">Stock</label>
                                    <input type="number" name="stock" id="stockProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="stockMinimoProducto">Stock Mínimo</label>
                                    <input type="number" name="stockMinimo" id="stockMinimoProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="descripcionProducto">Descripción</label>
                                    <textarea name="descripcion" id="descripcionProducto" class="form-control" required></textarea>
                                </div>
                                <div class="form-group mb-2">
                                    <label for="estadoProducto">Estado</label>
                                    <select name="estado" id="estadoProducto" class="form-control" required>
                                        <option value="Disponible">Disponible</option>
                                        <option value="No Disponible">No Disponible</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="imagenProducto">Imagen</label>
                                    <input type="file" name="imagen" id="imagenProducto" class="form-control-file" required>
                                </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                                    <button type="submit" class="btn btn-primary">Agregar producto</button>
                                </div>
                            </form>
                        
                    </div>
                </div>
            </div>

            <!-- Modal para editar producto -->
            <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Producto</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionProductos" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                                <input type="hidden" name="accion" value="Editar">
                                <input type="hidden" name="idProducto" id="editIdProducto">
                                <div class="form-group">
                                    <label for="editNombreProducto">Nombre</label>
                                    <input type="text" name="nombre" id="editNombreProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editIdCategoria">Categoría</label>
                                    <select name="idCategoria" id="editIdCategoria" class="form-control" required>
                                        <c:forEach var="categoria" items="${ListaCategorias}">
                                            <option value="${categoria.idCategoria}"
                                                    <c:if test="${categoria.idCategoria == producto.idCategoria}">selected</c:if>>
                                                ${categoria.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>

                                </div>
                                <div class="form-group">
                                    <label for="editPrecioProducto">Precio</label>
                                    <input type="number" step="0.01" name="precio" id="editPrecioProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editStockProducto">Stock</label>
                                    <input type="number" name="stock" id="editStockProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editStockMinimoProducto">Stock Mínimo</label>
                                    <input type="number" name="stockMinimo" id="editStockMinimoProducto" class="form-control" required>
                                </div>
                                <div class="form-group">
                                    <label for="editDescripcionProducto">Descripción</label>
                                    <textarea name="descripcion" id="editDescripcionProducto" class="form-control" required></textarea>
                                </div>
                                <div class="form-group mb-3">
                                    <label for="editEstadoProducto">Estado</label>
                                    <select name="estado" id="editEstadoProducto" class="form-control" required>
                                        <option value="Disponible">Disponible</option>
                                        <option value="No Disponible">No Disponible</option>
                                    </select>
                                </div>
                                <div class="form-group mb-3">
                                    <label for="editImagenProducto">Imagen</label>
                                    <input type="file" name="imagen" id="editImagenProducto" class="form-control-file">
                                </div>
                                <button type="submit" class="btn btn-warning w-100">Guardar Cambios</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Toast deyvids - Informacion deyvids -->
            
            <div aria-live="polite" aria-atomic="true" class="position-relative">
                <div class="toast-container position-fixed top-0 end-0 p-3">
                    <c:forEach var="mensaje" items="${mensajesAdvertencia}" varStatus="status">
                        <!-- Cambia el color de fondo según el tipo de mensaje -->
                        <div class="toast <c:choose>
                            <c:when test="${tiposMensaje[status.index] == 'warning'}">bg-warning</c:when>
                            <c:when test="${tiposMensaje[status.index] == 'danger'}">bg-danger</c:when>
                        </c:choose>" role="alert" aria-live="assertive" aria-atomic="true">

                            <div class="toast-header">
                                <strong class="me-auto">
                                    <c:choose>
                                        <c:when test="${tiposMensaje[status.index] == 'warning'}">Aviso de Stock Bajo</c:when>
                                        <c:when test="${tiposMensaje[status.index] == 'danger'}">Aviso de Sin Stock</c:when>
                                    </c:choose>
                                </strong>
                                <small class="text-body-secondary">just now</small>
                                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                            </div>

                            <div class="toast-body text-white">
                                ${mensaje}
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            
            <script>
                document.addEventListener("DOMContentLoaded", function () {
                    const toastElList = document.querySelectorAll('.toast');
                    const toastList = [...toastElList].map(toastEl => {
                        const toast = new bootstrap.Toast(toastEl);
                        toast.show();
                        return toast;
                    });
                });
            </script>
            
            <script>
                // JavaScript para el modal de edición
                $(document).ready(function () {
                    $('.editBtn').on('click', function () {
                        $('#editIdProducto').val($(this).data('id'));
                        $('#editIdCategoria').val($(this).data('idcategoria'));
                        $('#editNombreProducto').val($(this).data('nombre'));
                        $('#editPrecioProducto').val($(this).data('precio'));
                        $('#editStockProducto').val($(this).data('stock'));
                        $('#editStockMinimoProducto').val($(this).data('stockminimo'));
                        $('#editDescripcionProducto').val($(this).data('descripcion'));
                        $('#editEstadoProducto').val($(this).data('estado'));
                    });

                    // Validación de formulario de bootstrap
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
             
            <!-- Bootstrap JS Bundle (Incluye Popper) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
            
        </div>
    </body>
</html>
