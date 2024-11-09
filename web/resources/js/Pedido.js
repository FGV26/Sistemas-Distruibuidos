
// Control de navegación entre módulos

// Bloquear/desbloquear acciones entre búsqueda y registro
let clienteSeleccionado = false;

// Buscar cliente con autocompletado
$("#dniCliente").on("input", function () {
    const dni = $(this).val();
    if (dni.length >= 3) { // Autocompletar desde el tercer carácter
        $.get("GestionDePedido", {accion: "BuscarClienteAuto", dni: dni}, function (data) {
            const resultados = JSON.parse(data);
            let lista = "";
            resultados.forEach(cliente => {
                lista += `<a href="#" class="list-group-item list-group-item-action" onclick="seleccionarCliente('${cliente.dni}', '${cliente.nombre}', '${cliente.apellido}', '${cliente.email}')">${cliente.nombre} ${cliente.apellido}</a>`;
            });
            $("#autocompleteResults").html(lista);
        });
    }
});

// Seleccionar cliente de la lista de autocompletar
function seleccionarCliente(dni, nombre, apellido, email) {
    clienteSeleccionado = true;
    $("#autocompleteResults").empty();
    $("#dniCliente").val(dni);
    $("#confirmacionSeleccion").html(`Cliente seleccionado: ${nombre} ${apellido} (${email})`);
    $("#nextToStep2").prop("disabled", false); // Habilita el botón siguiente
    bloquearRegistro();
}

// Bloquear el registro cuando se selecciona un cliente
function bloquearRegistro() {
    $("#formRegistroCliente :input").prop("disabled", true);
    $("#btnRegistrar").prop("disabled", true);
    $("#btnBuscar").prop("disabled", false); // Deshabilita la búsqueda en caso de registro
}


function buscarCliente() {
    const  dni = $("#dniCliente").val();
    $.get("GestionDePedido", {accion: "BuscarCliente", dni: dni}, function (response) {
        const cliente = JSON.parse(response);
        if (cliente) {
            $("#resultadoBusqueda").html(`Cliente: ${cliente.nombre} ${cliente.apellido}`);
            $("#nextToStep2").prop("disabled", false); // Habilitar botón para avanzar
        } else {
            alert("Cliente no encontrado");
        }
    }).fail(function () {
        alert("Error en la búsqueda del cliente");
    });
}


function registrarCliente() {
    const data = {
        accion: "CrearCliente",
        dni: $("#dniRegistro").val(), 
        nombre: $("#nombre").val(),
        apellido: $("#apellido").val(),
        direccion: $("#direccion").val(),
        telefono: $("#telefono").val(),
        email: $("#email").val()
    };

    $.post("/Sistemas-Distruibuidos/GestionDePedidos", data, function (response) {
        console.log("Respuesta del servidor:", response);
        alert("Cliente registrado correctamente");
        $("#nextToStep2").prop("disabled", false);
        bloquearBusqueda();
    }).fail(function () {
        alert("Error al registrar el cliente");
    });
}

// Bloquear búsqueda cuando se crea un cliente nuevo
function bloquearBusqueda() {
    clienteSeleccionado = true;
    $("#dniCliente").prop("disabled", true);
    $("#btnBuscar").prop("disabled", true);
}

function listarProductos() {
    $.get("GestionDePedido", {accion: "ListarProductos"}, function (response) {
        const productos = JSON.parse(response);
        let html = "<table class='table'><tr><th>Nombre</th><th>Precio</th><th>Acción</th></tr>";
        productos.forEach(producto => {
            html += `<tr><td>${producto.nombre}</td><td>${producto.precio}</td>
<td><button onclick="agregarAlCarrito(${producto.idProducto})" class="btn btn-primary">Agregar</button></td></tr>`;
        });
        html += "</table>";
        $("#listaProductos").html(html);
    }).fail(function () {
        alert("Error al listar productos");
    });
}

function agregarAlCarrito(idProducto) {
// Ejemplo de implementación: llamar a un método del servlet para agregar al carrito
    $.post("GestionDePedido", {accion: "AgregarAlCarrito", idProducto: idProducto}, function (response) {
// Actualiza la vista del carrito aquí
    }).fail(function () {
        alert("Error al agregar producto al carrito");
    });
}

function generarPagoTotal() {
    $.get("GestionDePedido", {accion: "GenerarPagoTotal"}, function (response) {
        const resumen = JSON.parse(response);
        $("#resumenPedido").html(`Subtotal: ${resumen.subTotal} - IGV: ${resumen.igv} - Total: ${resumen.total}`);
    }).fail(function () {
        alert("Error al generar el pago total");
    });
}


function realizarPedido() {
    $.post("GestionDePedido", {accion: "GenerarPedido"}, function (response) {
        alert("Pedido realizado con éxito");
        window.location.href = "DashboardEmpleado.jsp"; // Redirige a otra página al finalizar
    }).fail(function () {
        alert("Error al realizar el pedido");
    });
}
