let currentStep = 0;
const steps = ['0%', '33%', '66%', '100%'];
const content = ['cliente', 'producto', 'confirmacion', 'pago'];
const nextBtn = document.getElementById('next-btn-progress');
const prevBtn = document.getElementById('prev-btn-progress');
const progressColors = document.getElementsByClassName('progress-color');

function updateView() {
    for (let i = 0; i < content.length; i++) {
        const contentStep = document.getElementById(content[i]);
        if (currentStep === i) {
            contentStep.classList.remove('d-none');
        } else {
            contentStep.classList.add('d-none');
        }
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
        currentStep--;
        progressBar.style.width = steps[currentStep];

        if (progressColors[currentStep + 1].classList.contains('bg-success')) {
            progressColors[currentStep + 1].classList.remove('bg-success');
            progressColors[currentStep + 1].classList.add('bg-secondary');
        }

        updateView();
    }

    if (currentStep < steps.length - 1) {
        nextBtn.disabled = false;
    }

    if (currentStep === 0) {
        prevBtn.disabled = true;
    } else {
        prevBtn.disabled = false;
    }
}

// Inicializa la vista
updateView();
prevBtn.disabled = true;

