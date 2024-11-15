let currentStep = 0;
const steps = ['0%', '33%', '66%', '100%'];
const content = ['cliente', 'producto', 'confirmacion', 'pago'];
const nextBtn = document.getElementById('next-btn-progress');
const prevBtn = document.getElementById('prev-btn-progress');
const progressColors = document.getElementsByClassName('progress-color');

function updateView() {
    // Ocultar todos los módulos
    for (let i = 0; i < content.length; i++) {
        const contentStep = document.getElementById(content[i]);
        contentStep.classList.add('d-none');
    }

    // Mostrar el módulo correspondiente al paso actual
    const currentContent = document.getElementById(content[currentStep]);
    currentContent.classList.remove('d-none');

    // Si el paso actual es el de confirmación (por ejemplo, paso 2), llama a cargarDatosClienteEnModulo3()
    if (content[currentStep] === 'confirmacion') {
        console.log("Ingrese");
        cargarDatosClienteEnModulo3();
    }
}

function nextStep() {
    const progressBar = document.getElementsByClassName('progress-bar')[0];

    if (progressBar && currentStep < steps.length - 1) {
        currentStep++;
        progressBar.style.width = steps[currentStep];

        if (progressColors[currentStep].classList.contains('bg-secondary')) {
            progressColors[currentStep].classList.remove('bg-secondary');
            progressColors[currentStep].classList.add('bg-success');
        }

        updateView();
    }

    if (currentStep > 0) {
        prevBtn.disabled = false;
    } else {
        prevBtn.disabled = true;
    }

    if (currentStep === steps.length - 1) {
        nextBtn.disabled = true;
    } else {
        nextBtn.disabled = false;
    }
}

function prevStep() {
    const progressBar = document.getElementsByClassName('progress-bar')[0];

    if (progressBar && currentStep > 0) {
        // Revertir el progreso de la barra y los colores
        if (progressColors[currentStep].classList.contains('bg-success')) {
            progressColors[currentStep].classList.remove('bg-success');
            progressColors[currentStep].classList.add('bg-secondary');
        }

        currentStep--;
        progressBar.style.width = steps[currentStep];

        updateView();
    }

    if (currentStep === 0) {
        prevBtn.disabled = true;
    } else {
        prevBtn.disabled = false;
    }

    if (currentStep < steps.length - 1) {
        nextBtn.disabled = false;
    }
}

// Inicializa la vista
updateView();
prevBtn.disabled = true;
