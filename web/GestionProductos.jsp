
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
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="resources/css/Alerta.css">
        <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <h1 class="mt-4">Gestión de Productos</h1>

            <!-- Formulario de búsqueda -->
            <form class="form-inline my-3" action="GestionProductos" method="GET">
                <input type="hidden" name="accion" value="Buscar">
                <input type="text" name="nombre" class="form-control mr-sm-2" placeholder="Buscar por nombre" required>
                <button type="submit" class="btn btn-outline-success my-2 my-sm-0">Buscar</button>
                <a href="GestionProductos?accion=Listar" class="btn btn-outline-primary my-2 my-sm-0 ml-2">Mostrar Todo</a>
            </form>

            <!-- Botones de filtros de stock -->
            <div class="d-flex justify-content-start mb-3">
                <a href="GestionProductos?accion=ListarPocoStock" class="btn btn-warning mr-2">Productos con Poco Stock</a>
                <a href="GestionProductos?accion=ListarSinStock" class="btn btn-danger mr-2">Productos Sin Stock</a>
                <!-- Botón para regresar al menú del administrador -->
                <a href="DashboardActividades?accion=Listar" class="btn btn-secondary">Volver al Menú Administrador</a>
            </div>


            <!-- Botón para agregar producto -->
            <button type="button" class="btn btn-info mb-3" data-toggle="modal" data-target="#agregarProductoModal">Agregar Producto</button>

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

            <!-- Tabla de productos -->
            <table class="table table-hover">
                <thead class="thead-light">
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
                                    <button class="btn btn-warning editBtn" data-toggle="modal" data-target="#editModal"
                                            data-id="${producto.idProducto}" data-idcategoria="${producto.idCategoria}"
                                            data-nombre="${producto.nombre}" data-precio="${producto.precio}"
                                            data-stock="${producto.stock}" data-stockminimo="${producto.stockMinimo}"
                                            data-descripcion="${producto.descripcion}" data-estado="${producto.estado}">
                                        Editar
                                    </button>
                                    <!-- Botón Eliminar -->
                                    <a href="GestionProductos?accion=Eliminar&idProducto=${producto.idProducto}"
                                       class="btn btn-danger ml-2"
                                       onclick="return confirm('¿Estás seguro de que deseas eliminar este producto?');">
                                        <i class="fas fa-trash-alt"></i> Eliminar
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Modal para agregar producto -->
            <div class="modal fade" id="agregarProductoModal" tabindex="-1" role="dialog" aria-labelledby="agregarProductoLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Agregar Producto</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="GestionProductos" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
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
                                <div class="form-group">
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
                                <button type="submit" class="btn btn-info">Agregar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Modal para editar producto -->
            <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Producto</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
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
                                <div class="form-group">
                                    <label for="editEstadoProducto">Estado</label>
                                    <select name="estado" id="editEstadoProducto" class="form-control" required>
                                        <option value="Disponible">Disponible</option>
                                        <option value="No Disponible">No Disponible</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="editImagenProducto">Imagen</label>
                                    <input type="file" name="imagen" id="editImagenProducto" class="form-control-file">
                                </div>
                                <button type="submit" class="btn btn-success">Guardar cambios</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <<c:forEach var="mensaje" items="${mensajesAdvertencia}" varStatus="status">
                <div class="modal fade 
                     <c:choose>
                         <c:when test="${tiposMensaje[status.index] == 'warning'}">modal-warning</c:when>
                         <c:when test="${tiposMensaje[status.index] == 'danger'}">modal-danger</c:when>
                     </c:choose>" 
                     id="modalAdvertencia${status.index}" tabindex="-1" role="dialog" aria-labelledby="modalAdvertenciaLabel${status.index}" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="modalAdvertenciaLabel${status.index}">
                                    <c:choose>
                                        <c:when test="${tiposMensaje[status.index] == 'warning'}">Aviso de Stock Bajo</c:when>
                                        <c:when test="${tiposMensaje[status.index] == 'danger'}">Aviso de Sin Stock</c:when>
                                    </c:choose>
                                </h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                ${mensaje}
                            </div>
                            <div class="modal-footer">
                                <!-- Botón dinámico según el tipo de mensaje -->
                                <c:choose>
                                    <c:when test="${tiposMensaje[status.index] == 'warning'}">
                                        <a href="GestionProductos?accion=ListarPocoStock" class="btn btn-warning">Mostrar con Poco Stock</a>
                                    </c:when>
                                    <c:when test="${tiposMensaje[status.index] == 'danger'}">
                                        <a href="GestionProductos?accion=ListarSinStock" class="btn btn-danger">Mostrar sin Stock</a>
                                    </c:when>
                                </c:choose>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>


            <script>
                $(document).ready(function () {
                    // Mostrar cada modal de advertencia si hay mensajes
                <c:forEach var="mensaje" items="${mensajesAdvertencia}" varStatus="status">
                    $('#modalAdvertencia${status.index}').modal('show');
                </c:forEach>
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
        </div>
    </body>
</html>
