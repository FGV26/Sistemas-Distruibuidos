<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta de Cliente</title>
    </head>
    <body>
        <h1>Consulta de Cliente</h1>
        <table border="1">
            <tr>
                <td>Id Cliente</td>
                <td>${cliente.idCliente}</td>
            </tr>
            <tr>
                <td>Apellidos</td>
                <td>${cliente.apellido}</td>
            </tr>
            <tr>
                <td>Nombres</td>
                <td>${cliente.nombre}</td>
            </tr>     
            <tr>
                <td>DNI</td>
                <td>${cliente.dni}</td>
            </tr>        
            <tr>
                <td>Dirección</td>
                <td>${cliente.direccion}</td>
            </tr>  
            <tr>
                <td>Teléfono</td>
                <td>${cliente.telefono}</td>
            </tr>                 
            <tr>
                <td>Email</td>
                <td>${cliente.email}</td>
            </tr>                 
        </table>
    </body>
</html>
