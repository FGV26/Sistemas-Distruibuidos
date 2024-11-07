// Variable de control para evitar la advertencia tras interactuar con el modal
let clienteSeleccionado = false;

// Función para abrir el modal de registro de cliente y cerrar el modal principal
function abrirModalRegistro() {
    clienteSeleccionado = true;
    var clienteModal = bootstrap.Modal.getInstance(document.getElementById('clienteModal'));
    clienteModal.hide();
    var modal = new bootstrap.Modal(document.getElementById('registroClienteModal'));
    modal.show();
}

// Función para abrir el modal para buscar por DNI y cerrar el modal principal
function abrirModalBuscarDni() {
    clienteSeleccionado = true;
    var clienteModal = bootstrap.Modal.getInstance(document.getElementById('clienteModal'));
    clienteModal.hide();
    var modal = new bootstrap.Modal(document.getElementById('buscarDniModal'));
    modal.show();
}

// Función para abrir el modal inicial de cliente
function abrirModalCliente() {
    clienteSeleccionado = false; // Resetear la variable de control
    var advertenciaModal = bootstrap.Modal.getInstance(document.getElementById('advertenciaModal'));
    advertenciaModal.hide();
    var clienteModal = new bootstrap.Modal(document.getElementById('clienteModal'));
    clienteModal.show();
}

// Función para redirigir al Dashboard
function redirigirDashboard() {
    window.location.href = "DashboardActividades?accion=Listar";
}

// Placeholder para registrar cliente
function submitRegistro() {
    alert("Formulario de registro enviado (simulado)");
    var modal = bootstrap.Modal.getInstance(document.getElementById('registroClienteModal'));
    modal.hide();
}

// Placeholder para buscar cliente
function buscarCliente() {
    alert("Buscar cliente por DNI (simulado)");
    var modal = bootstrap.Modal.getInstance(document.getElementById('buscarDniModal'));
    modal.hide();
}

// Mostrar el modal inicial al cargar la página
document.addEventListener("DOMContentLoaded", function () {
    clienteSeleccionado = false;
    var clienteModal = new bootstrap.Modal(document.getElementById('clienteModal'));
    clienteModal.show();
});

// Solo mostrar advertencia si no se seleccionó ningún cliente
document.getElementById('clienteModal').addEventListener('hidden.bs.modal', function () {
    if (!clienteSeleccionado) {
        var advertenciaModal = new bootstrap.Modal(document.getElementById('advertenciaModal'));
        advertenciaModal.show();
    }
});

// Redirigir al dashboard si el modal de advertencia se cierra sin acción
document.getElementById('advertenciaModal').addEventListener('hidden.bs.modal', function () {
    if (!clienteSeleccionado) {
        redirigirDashboard();
    }
});
