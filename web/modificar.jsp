
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modificar Cliente</title>
    </head>
    <body>
        <h1>Modificar Cliente</h1>
        <form action="ControlerCliente?accion=Actualizar" method="post">
            <table border="1">
                <tr>
                    <td>Id Cliente</td>
                    <td>${cliente.idCliente}</td>
                <input type="hidden" name="Id" value="${cliente.idCliente}">
                </tr>
                <tr>
                    <td>Apellidos</td>
                    <td><input type="text" name="apellido" value="${cliente.apellido}"></td>
                </tr>
                <tr>
                    <td>Nombres</td>
                    <td><input type="text" name="nombre" value="${cliente.nombre}"></td>
                </tr>     
                <tr>
                    <td>DNI</td>
                    <td><input type="text" name="DNI" value="${cliente.dni}"></td>
                </tr>        
                <tr>
                    <td>Dirección</td>
                    <td><input type="text" name="direccion" value="${cliente.direccion}"></td>
                </tr>  
                <tr>
                    <td>Teléfono</td>
                    <td><input type="text" name="telefono" value="${cliente.telefono}"></td>
                </tr>                 
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" value="${cliente.email}"></td>
                </tr>                 
            </table>
            <input type="submit" value="Guardar Cambios">
        </form>
    </body>
</html>
