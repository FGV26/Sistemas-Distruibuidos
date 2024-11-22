
console.log("DetallePedido.js cargado correctamente.");

// Variables de control para la barra de progreso
let currentStep = 0; // Posición inicial
const steps = ['0%', '25%', '50%', '75%', '100%']; // Progreso en porcentaje
const progressColors = document.getElementsByClassName('progress-color');
const progressBar = document.querySelector('.progress-bar');
const nextBtn = document.getElementById('next-btn-progress');

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

    // Verificar y deshabilitar el botón de avance si estamos en el último paso
    if (nextBtn) {
        nextBtn.disabled = currentStep === steps.length - 1;
    } else {
        console.error("El botón 'next-btn-progress' no existe en el DOM.");
    }
}

// Función para avanzar al siguiente paso
function nextStep() {
    console.log("Avanzando al siguiente paso.");
    if (currentStep < steps.length - 1) {
        currentStep++;
        updateView();
    }
}

// Función para cancelar el pedido con animación
function cancelOrder() {
    console.log("CancelOrder fue llamado.");
    if (confirm("¿Estás seguro de que deseas cancelar el pedido?")) {
        // Animación de la barra y cambio de color a rojo
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
        const nextBtn = document.getElementById('next-btn-progress');
        const cancelBtn = document.getElementById('cancel-btn');

        if (nextBtn) {
            nextBtn.disabled = true;
        }
        if (cancelBtn) {
            cancelBtn.disabled = true;
        }

        // Mostrar el toast de notificación
        showToast("Se canceló el pedido.");
    }
}

// Función para mostrar el toast de notificación
function showToast(message) {
    const toastElement = document.getElementById('cancel-toast');
    const toastBody = document.getElementById('toast-body');

    // Cambiar el mensaje dinámicamente
    toastBody.innerText = message;

    // Mostrar el toast usando Bootstrap
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}

// Inicializar la vista al cargar la página
document.addEventListener('DOMContentLoaded', function () {
    console.log("Inicializando vista...");
    updateView();
});
