<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Listado de Clientes</title>
    </head>
    <body>
        <h1>Listado de Clientes</h1>
        <table border="1">
            <tr>
                <th>Id Cliente</th>
                <th>Apellidos</th>
                <th>Nombres</th>
                <th>DNI</th>
                <th>Acciones</th>
            </tr>

            <c:forEach var="campo" items="${Lista}">
                <tr>
                    <td>${campo.idCliente}</td>
                    <td>${campo.apellido}</td>
                    <td>${campo.nombre}</td>
                    <td>${campo.dni}</td>
                    <td>
                        <a href="ControlerCliente?accion=Consultar&Id=${campo.idCliente}">Consultar</a> |
                        <a href="ControlerCliente?accion=Modificar&Id=${campo.idCliente}">Modificar</a> |
                        <a href="ControlerCliente?accion=Eliminar&Id=${campo.idCliente}">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
