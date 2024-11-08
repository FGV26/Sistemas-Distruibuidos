
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

// Función para enviar el formulario de registro de cliente
function submitRegistro() {
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const direccion = document.getElementById("direccion").value;
    const dni = document.getElementById("dni").value;
    const telefono = document.getElementById("telefono").value;
    const email = document.getElementById("email").value;

    // Enviar datos del cliente al servlet
    fetch('ValidarClientes?accion=Crear', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `nombre=${nombre}&apellido=${apellido}&direccion=${direccion}&dni=${dni}&telefono=${telefono}&email=${email}`
    }).then(response => {
        if (response.ok) {
            alert("Cliente registrado exitosamente");
            bootstrap.Modal.getInstance(document.getElementById('registroClienteModal')).hide();
            abrirModalCliente();
        } else {
            alert("Error al registrar el cliente");
        }
    });
}

// Función para buscar cliente por DNI
function buscarCliente() {
    const dni = document.getElementById("dniBuscar").value.trim();
    if (dni) {
        // Llamada al servlet para buscar cliente
        fetch(`ValidarClientes?accion=Buscar&dni=${dni}`)
                .then(response => response.json())
                .then(data => {
                    if (data) {
                        alert(`Cliente encontrado: ${data.nombre} ${data.apellido}`);
                        bootstrap.Modal.getInstance(document.getElementById('buscarDniModal')).hide();
                        abrirModalCliente();
                    } else {
                        alert("Cliente no encontrado");
                    }
                })
                .catch(() => alert("Error en la búsqueda del cliente"));
    } else {
        alert("Por favor, ingrese un DNI");
    }
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

// Autocompletar en el campo de búsqueda de DNI
document.addEventListener("DOMContentLoaded", function () {
    const inputDni = document.getElementById("dniBuscar");
    const resultadosDiv = document.getElementById("resultadosAutocompletar");

    inputDni.addEventListener("input", function () {
        const dni = inputDni.value.trim();
        if (dni.length >= 3) { // Empieza a buscar después de 3 caracteres
            fetch(`ValidarClientes?accion=AutoCompletar&dni=${dni}`)
                    .then(response => response.json())
                    .then(data => {
                        mostrarResultados(data);
                    });
        } else {
            resultadosDiv.innerHTML = "";
        }
    });

    function mostrarResultados(data) {
        resultadosDiv.innerHTML = "";
        data.forEach(cliente => {
            const div = document.createElement("div");
            div.classList.add("autocomplete-item");

            // Añade un ícono de usuario y el nombre del cliente
            div.innerHTML = `<i class="fas fa-user"></i> ${cliente.nombre} ${cliente.apellido}`;

            div.addEventListener("click", () => {
                inputDni.value = cliente.dni; // Asigna el DNI al input
                resultadosDiv.innerHTML = ""; // Borra los resultados
            });
            resultadosDiv.appendChild(div);
        });
    }
});

