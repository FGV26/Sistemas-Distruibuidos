
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión</title>
    <link rel="stylesheet" href="resources/css/login.css">
</head>
<body>
    <form class="my-form" action="ValidarLogin" method="post">
        <div class="login-welcome-row">
            <a href="#" title="Logo">
                <img src="resources/img/password.png" alt="Logo" class="logo">
            </a>
            <h1>Iniciar Sesión</h1>
        </div>

        <!-- Campo de entrada para el usuario -->
        <div class="text-field">
            <label for="email">Usuario</label>
            <input type="text" id="email" name="txtUsuario" autocomplete="off" placeholder="Ingrese su usuario" required>
        </div>

        <!-- Campo de entrada para la contraseña -->
        <div class="text-field">
            <label for="password">Contraseña</label>
            <input type="password" id="password" name="txtClave" required>
        </div>

        <!-- Mostrar mensaje de error general si existe -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <!-- Botón de inicio de sesión -->
        <button type="submit" class="my-form__button">Ingresar</button>

        <!-- Enlace para crear una cuenta -->
        <div class="my-form_actions">
            <div class="my-form_row">
                <span>¿No tienes una cuenta?</span>
                <a href="registro.jsp" title="Crear Cuenta">Regístrate</a>
            </div>
        </div>
    </form>
</body>
</html>
