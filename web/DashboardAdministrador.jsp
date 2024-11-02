<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Dashboard Administrador</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Inter&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="resources/css/Dashboard.css">
        <!-- Bootstrap Icons CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    </head>

    <body>
        <nav class="sidebar">
            <div class="sidebar-top-wrapper">
                <div class="sidebar-top">
                    <a href="#" class="logo__wrapper">
                        <img src="assets/logo.svg" alt="Crestline Bank Logo" class="logo">
                        <span class="hide company-name">Crestline Bank</span>
                    </a>
                </div>
                <button class="expand-btn" type="button">
                    <i class="bi bi-arrow-bar-left"></i> <!-- Icono de expansión de Bootstrap -->
                </button>
            </div>

            <div class="sidebar-links-wrapper">
                <div class="sidebar-links">
                    <ul>
                        <li>
                            <a href="#dashboard" title="Dashboard">
                                <i class="bi bi-grid-fill"></i>
                                <span class="link hide">Dashboard</span>
                            </a>
                        </li>
                        <li>
                            <a href="#manage-employees" title="Gestión de Empleados">
                                <i class="bi bi-people"></i>
                                <span class="link hide">Gestión de Empleados</span>
                            </a>
                        </li>
                        <li>
                            <a href="#manage-despachadores" title="Gestión de Despachadores">
                                <i class="bi bi-truck"></i>
                                <span class="link hide">Gestión de Despachadores</span>
                            </a>
                        </li>
                        <li>
                            <a href="#manage-products" title="Gestión de Productos">
                                <i class="bi bi-box-seam"></i>
                                <span class="link hide">Gestión de Productos</span>
                            </a>
                        </li>
                        <li>
                            <a href="#generate-reports" title="Generar Reportes">
                                <i class="bi bi-bar-chart-line"></i>
                                <span class="link hide">Generar Reportes</span>
                            </a>
                        </li>
                        <!-- Nuevos enlaces para Ver Información y Cerrar Sesión -->
                        <li>
                            <a href="#profile" title="Ver Información">
                                <i class="bi bi-person-circle"></i>
                                <span class="link hide">Ver Información</span>
                            </a>
                        </li>
                        <li>
                            <a href="CerrarSesion" title="Cerrar Sesión">
                                <i class="bi bi-box-arrow-right"></i> <!-- Icono de logout -->
                                <span class="link hide">Cerrar Sesión</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <script src="resources/js/Dashboard.js"></script>
    </body>
</html>
