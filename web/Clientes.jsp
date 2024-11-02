
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <%  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
        }
    %>
    <body>
        <h1>Menú de Clientes</h1>
        <p><a href="ControlerCliente?accion=Listar">Listar Clientes</a></p>
        <p><a href="ControlerCliente?accion=Nuevo">Nuevo Cliente</a></p>
    </body>
</html>
