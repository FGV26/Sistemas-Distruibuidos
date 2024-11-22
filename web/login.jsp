
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión</title>
    <link rel="stylesheet" href="resources/css/login.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body style="background: #F8F8F8;">
    <form class="my-form border" action="ValidarLogin" method="post">
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
            
            <div aria-live="polite" aria-atomic="true" class="position-relative">
                <div class="toast-container position-fixed top-0 end-0 p-3 show">
                    <div class="toast bg-danger fade show" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body text-white">
                                ${errorMessage}
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                </div>
            </div>

        </c:if>

        <!-- Botón de inicio de sesión -->
        <button type="submit" class="my-form__button">Ingresar</button>

        <!-- Enlace para crear una cuenta -->
        <div class="my-form_actions">
            <div class="my-form_row">
            </div>
        </div>
    </form>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>
