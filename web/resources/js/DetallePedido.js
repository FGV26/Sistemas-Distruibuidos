
console.log("DetallePedido.js cargado correctamente.");

// Mapeo de estados a pasos de progreso
const stateToStepMap = {
    "Proceso": 0,
    "Leido": 1,
    "Empaquetado": 2,
    "Enviado": 3
};
const stepToStateMap = ["Proceso", "Leido", "Empaquetado", "Enviado"]; // Inverso para obtener el estado desde el paso

const steps = ['0%', '33%', '66%', '100%']; // Porcentajes de progreso
const progressColors = document.getElementsByClassName('progress-color');
const progressBar = document.querySelector('.progress-bar');
const nextBtn = document.getElementById('next-btn-progress');

// Variable para controlar el paso actual
let currentStep = 0;
let pedidoId = null; // Se llenará al inicializar

// Función para actualizar la vista de la barra de progreso
function updateView() {
    console.log("Actualizando vista: Paso actual", currentStep);

    // Actualizar el ancho de la barra
    progressBar.style.width = steps[currentStep];
    progressBar.setAttribute('aria-valuenow', steps[currentStep]);

    // Actualizar colores de los íconos en función del paso actual
    for (let i = 0; i < progressColors.length; i++) {
        if (i <= currentStep) {
            progressColors[i].classList.remove('bg-secondary');
            progressColors[i].classList.add('bg-success');
        } else {
            progressColors[i].classList.remove('bg-success');
            progressColors[i].classList.add('bg-secondary');
        }
    }

    // Deshabilitar el botón "Avanzar Estado" si está en el último paso
    if (nextBtn) {
        nextBtn.disabled = currentStep === steps.length - 1;
    }
}

// Función para avanzar al siguiente estado
function advanceState() {
    if (currentStep >= steps.length - 1) {
        console.warn("Ya estás en el último estado. No se puede avanzar más.");
        return;
    }

    // Incrementar el paso actual
    currentStep++;
    const nuevoEstado = stepToStateMap[currentStep]; // Obtener el nuevo estado
    console.log("Avanzando al siguiente estado:", nuevoEstado);

    // Hacer la llamada AJAX para actualizar el estado en el backend
    fetch("ControlerPedido?accion=ActualizarEstado", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `idPedido=${pedidoId}&estado=${nuevoEstado}`
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    console.log("Estado actualizado correctamente en el backend.");
                    updateView(); // Actualizar la barra de progreso en la interfaz
                    showToast(`Estado actualizado a: ${nuevoEstado}`);
                } else {
                    console.error("Error al actualizar el estado:", data.message);
                    showToast("No se pudo actualizar el estado.");
                    currentStep--; // Revertir el paso si falla la actualización
                }
            })
            .catch(error => {
                console.error("Error en la llamada AJAX:", error);
                showToast("Error de conexión con el servidor.");
                currentStep--; // Revertir el paso si falla la conexión
            });
}

// Función para cancelar el pedido
function cancelOrder() {
    console.log("Intentando cancelar el pedido con ID:", pedidoId);

    // Confirmar con el usuario antes de proceder
    if (!confirm("¿Estás seguro de que deseas cancelar el pedido?")) {
        console.log("Cancelación abortada por el usuario.");
        return;
    }

    // Hacer la llamada AJAX para actualizar el estado a "Cancelado"
    fetch("ControlerPedido?accion=ActualizarEstado", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `idPedido=${pedidoId}&estado=Cancelado`
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    console.log("Pedido cancelado correctamente en el backend.");
                    handleCancellation(); // Aplicar lógica de cancelación en la interfaz
                    showToast("El pedido ha sido cancelado.");
                } else {
                    console.error("Error al cancelar el pedido:", data.message);
                    showToast("No se pudo cancelar el pedido.");
                }
            })
            .catch(error => {
                console.error("Error en la llamada AJAX para cancelar el pedido:", error);
                showToast("Error de conexión con el servidor.");
            });
}

// Función para manejar cancelación desde el estado inicial
function handleCancellation() {
    console.log("El pedido está en estado 'Cancelado'. Aplicando lógica de cancelación.");
    progressBar.style.width = "100%";
    progressBar.classList.add("bg-danger");
    progressBar.setAttribute('aria-valuenow', '100');

    // Cambiar todos los íconos a una "X"
    for (let i = 0; i < progressColors.length; i++) {
        progressColors[i].classList.remove('bg-success', 'bg-secondary');
        progressColors[i].classList.add('bg-danger');
        progressColors[i].innerHTML = `<i class="fas fa-times"></i>`;
    }

    // Deshabilitar los botones
    disableButtons();

    // Mostrar un toast rojo para Cancelado
    showToast("El pedido ha sido cancelado.", "error");
}

// Función para inicializar el estado según el atributo recibido
function initializeState(estadoPedido) {
    const estadoLimpio = estadoPedido.trim().normalize("NFC");

    if (estadoLimpio === "Cancelado") {
        handleCancellation(); // Si el estado es Cancelado, aplica la lógica de cancelación
    } else if (stateToStepMap.hasOwnProperty(estadoLimpio)) {
        currentStep = stateToStepMap[estadoLimpio];
        updateView();

        // Si el estado es "Enviado", deshabilitar los botones
        if (estadoLimpio === "Enviado") {
            console.log("El pedido ya está en estado 'Enviado'. Deshabilitando botones.");
            disableButtons();
        }
    } else {
        currentStep = 0; // Valor predeterminado si el estado no es válido
        updateView();
    }
}

// Función para deshabilitar botones
function disableButtons() {
    if (nextBtn) {
        nextBtn.disabled = true;
    }
    const cancelBtn = document.getElementById('cancel-btn');
    if (cancelBtn) {
        cancelBtn.disabled = true;
    }
    showToast("El pedido ha llegado al estado final 'Enviado'.", "success");
}

// Función para mostrar el toast de notificación
function showToast(message, type = "success") {
    const toastElement = document.getElementById('cancel-toast');
    const toastBody = document.getElementById('toast-body');
    
    // Cambiar el color dinámicamente según el tipo
    if (type === "success") {
        toastElement.classList.remove('bg-danger');
        toastElement.classList.add('bg-success');
    } else if (type === "error") {
        toastElement.classList.remove('bg-success');
        toastElement.classList.add('bg-danger');
    }

    // Cambiar el mensaje dinámicamente
    toastBody.innerText = message;

    // Mostrar el toast usando Bootstrap
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}

// Inicializar la vista al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    console.log("Inicializando vista...");

    const progressContainer = document.querySelector('.progress-container');
    const estadoPedido = progressContainer.getAttribute('data-estado');
    const cancelBtn = document.getElementById('cancel-btn');
    pedidoId = progressContainer.getAttribute('data-idpedido'); 

    console.log("Estado inicial:", estadoPedido);
    console.log("ID del Pedido:", pedidoId);

    initializeState(estadoPedido);

    // Asociar el botón de avanzar estado
    if (nextBtn) {
        nextBtn.addEventListener("click", advanceState);
    }
    if (cancelBtn) {
        cancelBtn.addEventListener("click", cancelOrder);
    }
});
