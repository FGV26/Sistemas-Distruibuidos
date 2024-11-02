<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nuevo Cliente</title>
    </head>
    <body>
        <h1>Crear Nuevo Cliente</h1>
        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>
        <form action="ControlerCliente?accion=Crear" method="post">
            <table border="1">
                <tr>
                    <td>Apellidos</td>
                    <td><input type="text" name="apellido"></td>
                </tr>
                <tr>
                    <td>Nombres</td>
                    <td><input type="text" name="nombre"></td>
                </tr>     
                <tr>
                    <td>DNI</td>
                    <td><input type="text" name="DNI" maxlength="8" required></td>
                </tr>


                <tr>
                    <td>Dirección</td>
                    <td><input type="text" name="direccion"></td>
                </tr>  
                <tr>
                    <td>Teléfono</td>
                    <td><input type="text" name="telefono"></td>
                </tr>                 
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email"></td>
                </tr>                 
            </table>
            <input type="submit" value="Guardar Nuevo Cliente">
        </form>
    </body>
</html>
