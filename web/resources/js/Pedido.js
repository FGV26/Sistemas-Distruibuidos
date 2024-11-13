
// Control de navegación entre módulos
function goToStep(stepNumber) {
    $(".step-content").addClass("d-none");
    $("#step" + stepNumber).removeClass("d-none");

    if (stepNumber === 2) {
        $("#nextToStep3").prop("disabled", $("#carrito").is(':empty'));
    }
}

// Modulo 1
// Control de navegación entre módulos
let clienteSeleccionado = false;  // Indicador de selección

// Bloquear/desbloquear acciones entre búsqueda y registro
function bloquearBusqueda() {
    console.log("Bloquear búsqueda");
    $("#formBuscarCliente :input").prop("disabled", true);
    $("#formBuscarCliente button").prop("disabled", true);
}

function bloquearRegistro() {
    console.log("Bloquear registro");
    $("#formRegistroCliente :input").prop("disabled", true);
    $("#formRegistroCliente button").prop("disabled", true);
}

// Buscar cliente en base de datos interna por DNI
async function buscarCliente() {
    const dniInput = $("#dniCliente").val().trim();
    console.log("Iniciando búsqueda de cliente con DNI:", dniInput);

    if (!dniInput) {
        alert("Por favor, ingrese un DNI para buscar.");
        return;
    }

    const data = {
        accion: "BuscarCliente",
        dni: dniInput
    };

    $.post("/Sistemas-Distruibuidos/GestionDePedidos", data, function(response) {
        console.log("Respuesta del servidor al buscar cliente:", response);

        if (!response || response.error) {
            alert(response ? response.error : "Cliente no encontrado.");
            return;
        }

        // Cargar datos de cliente encontrado en campos
        $("#nombreCliente").val(response.nombre);
        $("#apellidoCliente").val(response.apellido);
        $("#direccionCliente").val(response.direccion);
        $("#telefonoCliente").val(response.telefono);
        $("#emailCliente").val(response.email);

        clienteSeleccionado = true;
        $("#nextToStep2").prop("disabled", false);  // Habilitar botón "Siguiente"
        bloquearRegistro();  // Bloquear el formulario de registro
    }).fail(function() {
        console.log("Error al hacer la solicitud al servidor para buscar cliente.");
        alert("Error al buscar cliente.");
    });
}

// Registrar un nuevo cliente en la base de datos
function registrarCliente() {
    const data = {
        accion: "CrearCliente",
        dni: $("#dniRegistro").val().trim(),
        nombre: $("#nombre").val().trim(),
        apellido: $("#apellido").val().trim(),
        direccion: $("#direccion").val().trim(),
        telefono: $("#telefono").val().trim(),
        email: $("#email").val().trim()
    };
    console.log("Intentando registrar cliente con datos:", data);

    // Validación de campos de registro
    if (!data.dni || !data.nombre || !data.apellido || !data.direccion || !data.telefono || !data.email) {
        alert("Todos los campos de registro son obligatorios.");
        return;
    }

    $.post("/Sistemas-Distruibuidos/GestionDePedidos", data, function(response) {
        console.log("Respuesta del servidor al registrar cliente:", response);

        if (response.error) {
            alert(response.error);
            return;
        }

        alert("Cliente registrado correctamente.");
        clienteSeleccionado = true;
        $("#nextToStep2").prop("disabled", false);  // Habilitar botón "Siguiente"
        bloquearBusqueda();  // Bloquear el formulario de búsqueda
    }).fail(function() {
        console.log("Error al hacer la solicitud al servidor para registrar cliente.");
        alert("Error al registrar el cliente.");
    });
}

// Búsqueda en API externa de DNI para completar automáticamente los datos
async function searchClient() {
    const dni = $("#dniRegistro").val().trim();
    console.log("Iniciando búsqueda en API externa para DNI:", dni);

    if (!dni) {
        alert("Por favor, ingrese un DNI para buscar.");
        return;
    }

    const token_api = "bb41db102773b0701695ead93cb71d54829e5cf38b90f9b6af5cc238db7f1170";

    try {
        const response = await fetch("https://apiperu.dev/api/dni", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token_api}`
            },
            body: JSON.stringify({ dni: dni })
        });

        console.log("Respuesta de la API externa:", response);

        if (!response.ok) {
            console.error('Error en la solicitud a la API externa: ' + response.status);
            return;
        }

        const data = await response.json();
        console.log("Datos obtenidos de la API externa:", data);

        if (data && data.data) {
            $("#nombre").val(data.data.nombres);
            $("#apellido").val(`${data.data.apellido_paterno} ${data.data.apellido_materno}`);
        } else {
            alert("No se encontró información del DNI en la API.");
        }
    } catch (error) {
        console.error("Error en la solicitud a la API externa:", error);
    }
}

// Modulo 2: Selección de Productos

// Variables para almacenar los productos y el carrito
let productos = [];         // Lista de productos disponibles
let carrito = [];            // Carrito de productos seleccionados

// Función para listar todos los productos con stock desde el servidor
function listarProductos() {
    $.get("/Sistemas-Distruibuidos/GestionDePedidos", {accion: "ListarProductos"}, function (response) {
        if (response.error) {
            alert(response.error);
            return;
        }
        productos = response;
        mostrarProductos(productos);
    }).fail(function () {
        alert("Error al listar productos");
    });
}

// Función para buscar productos por nombre mientras el usuario escribe
function buscarProducto() {
    const query = $("#buscarProducto").val().toLowerCase();
    if (query) {
        $.get("/Sistemas-Distruibuidos/GestionDePedidos", {accion: "BuscarProductoPorNombre", nombre: query}, function (response) {
            if (response.error) {
                alert(response.error);
                return;
            }
            mostrarProductos(response);
        }).fail(function () {
            alert("Error al buscar producto");
        });
    } else {
        mostrarProductos(productos); // Si no hay búsqueda, mostrar todos
    }
}

// Función para listar productos por categoría
function listarPorCategoria(categoria) {
    if (categoria) {
        $.get("/Sistemas-Distruibuidos/GestionDePedidos", {accion: "ListarPorCategoria", categoria}, function (response) {
            if (response.error) {
                alert(response.error);
                return;
            }
            // Muestra solo los productos de la categoría seleccionada
            mostrarProductos(response);
        }).fail(function () {
            alert("Error al listar productos por categoría");
        });
    } else {
        // Si no hay categoría seleccionada, listar todos los productos
        listarProductos();
    }
}

// Función para cargar las categorías en el select
function cargarCategorias() {
    $.ajax({
        url: '/Sistemas-Distruibuidos/GestionDePedidos', // Ajusta la URL según tu configuración
        type: 'GET',
        data: {accion: 'ListarCategorias'},
        success: function (response) {
            // Limpiar el select antes de agregar opciones
            $('#categoriaProducto').empty().append('<option value="">Categoría</option>');

            // Verificar si hubo un error
            if (response.error) {
                console.error(response.error);
                return;
            }

            // Agregar cada categoría al select
            response.forEach(function (categoria) {
                $('#categoriaProducto').append(
                    `<option value="${categoria.nombre}">${categoria.nombre}</option>`
                );
            });
        },
        error: function () {
            console.error('Error al cargar categorías.');
        }
    });
}

// Llamar a la función para cargar categorías cuando se cargue el documento
$(document).ready(function () {
    cargarCategorias();
});

// Función para mostrar los productos en la tabla con scroll y botón de agregar
function mostrarProductos(productosLista) {
    let html = "";

    productosLista.forEach(producto => {
        html += `
            <tr>
                <!-- Columna de Imagen -->
                <td>
                    <img src="resources/img/productos/${producto.imagen}" alt="${producto.nombre}" 
                         class="product-image" style="width: 50px; height: 50px;">
                </td>
                
                <!-- Columna de Nombre y Precio -->
                <td>
                    <strong>${producto.nombre}</strong> - $${producto.precio}
                </td>
                
                <!-- Columna de Cantidad -->
                <td>
                    <div class="d-flex align-items-center">
                        <button onclick="cambiarCantidad(${producto.idProducto}, 1)" class="btn btn-sm btn-outline-primary">+</button>
                        <span id="cantidad-${producto.idProducto}" class="mx-2">1</span>
                        <button onclick="cambiarCantidad(${producto.idProducto}, -1)" class="btn btn-sm btn-outline-secondary">-</button>
                    </div>
                </td>
                
                <!-- Columna de Acción -->
                <td>
                    <button onclick="agregarAlCarrito(${producto.idProducto})" class="btn btn-sm btn-success">Agregar</button>
                </td>
            </tr>
        `;
    });

    // Inserta las filas generadas en el tbody con id "listaProductos"
    $("#listaProductos").html(html);
}

// Función para cambiar la cantidad de un producto antes de agregar al carrito, verificando el stock
function cambiarCantidad(idProducto, incremento) {
    const producto = productos.find(p => p.idProducto === idProducto);
    const cantidadElement = $(`#cantidad-${idProducto}`);
    let cantidadActual = parseInt(cantidadElement.text()) || 1;

    // Asegurar que la cantidad no exceda el stock disponible
    const nuevaCantidad = cantidadActual + incremento;
    if (nuevaCantidad > producto.stock) {
        alert(`Stock máximo alcanzado. Disponibles: ${producto.stock}`);
        return;
    }
    cantidadActual = Math.max(1, nuevaCantidad);  // Evitar cantidades menores a 1
    cantidadElement.text(cantidadActual);
}

// Función para agregar productos al carrito con validación de stock
function agregarAlCarrito(idProducto) {
    const producto = productos.find(p => p.idProducto === idProducto);
    const cantidad = parseInt($(`#cantidad-${idProducto}`).text()) || 1;

    // Verificar si la cantidad total en el carrito más la cantidad solicitada excede el stock
    const productoEnCarrito = carrito.find(item => item.idProducto === idProducto);
    const cantidadEnCarrito = productoEnCarrito ? productoEnCarrito.cantidad : 0;

    if (cantidad + cantidadEnCarrito > producto.stock) {
        alert(`No se puede agregar más de ${producto.stock} unidades de este producto.`);
        return;
    }

    if (productoEnCarrito) {
        productoEnCarrito.cantidad += cantidad;
    } else {
        carrito.push({...producto, cantidad});
    }

    actualizarCarrito();
    $("#nextToStep3").prop("disabled", false);  // Habilitar el botón "Siguiente" si hay productos en el carrito
}

// Función para mostrar el contenido del carrito en la sección correspondiente con scroll
function actualizarCarrito() {
    let html = `
        <div class="carrito-list" style="max-height: 200px; overflow-y: auto;">
    `;

    html += carrito.map(item => `
        <div class="carrito-item d-flex align-items-center mb-2">
            <img src="resources/img/productos/${item.imagen}" alt="${item.nombre}" 
                 class="product-image" style="width: 50px; height: 50px; margin-right: 10px;">
            <div class="product-info">
                <strong>${item.nombre}</strong> - Cantidad: ${item.cantidad}
                <button onclick="eliminarDelCarrito(${item.idProducto})" class="btn btn-sm btn-danger ml-2">Eliminar</button>
            </div>
        </div>
    `).join("");

    html += `</div>`;
    $("#carrito").html(html);
}

// Función para eliminar un producto del carrito
function eliminarDelCarrito(idProducto) {
    carrito = carrito.filter(item => item.idProducto !== idProducto);
    actualizarCarrito();

    // Si el carrito está vacío, deshabilitar el botón "Siguiente"
    if (carrito.length === 0) {
        $("#nextToStep3").prop("disabled", true);
    }
}

// Inicializar la lista de productos al cargar el módulo 2
$(document).ready(function () {
    listarProductos();  // Listar productos con stock al iniciar el módulo 2
});

// Modulo 3: Confirmación de Pedido

// Variables para el cálculo de totales
const IGV_RATE = 0.18; // Tasa de IGV del 18%

// Función principal para cargar y mostrar la información del pedido
function cargarInformacionPedido() {
    cargarDatosCliente();
    generarNumeroPedido();
    mostrarResumenProductos();
    calcularTotalesPedido();
}

// Cargar los datos del cliente desde los datos almacenados
function cargarDatosCliente() {
    // Supongamos que tenemos los datos del cliente almacenados en variables globales o en el sessionStorage
    $("#codigoCliente").val(sessionStorage.getItem("codigoCliente"));
    $("#nombreCliente").val(sessionStorage.getItem("nombreCliente"));
    $("#direccionCliente").val(sessionStorage.getItem("direccionCliente"));
    
    // Fecha actual
    const fecha = new Date().toLocaleDateString();
    $("#fechaPedido").val(fecha);
}

// Generar el número de pedido automáticamente
function generarNumeroPedido() {
    $.get("/Sistemas-Distruibuidos/GestionDePedidos", {accion: "ObtenerUltimoIdPedido"}, function(response) {
        if (response.error) {
            alert(response.error);
            return;
        }
        
        const nuevoIdPedido = response.ultimoId + 1;
        const numeroPedido = `PEDI${String(nuevoIdPedido).padStart(3, '0')}`;
        $("#numeroPedido").val(numeroPedido);
    }).fail(function() {
        alert("Error al generar el número de pedido.");
    });
}

// Mostrar los productos seleccionados en el resumen del pedido
function mostrarResumenProductos() {
    let html = "";
    
    carrito.forEach((producto, index) => {
        const precio = producto.precio;
        const cantidad = producto.cantidad;
        const subtotal = precio * cantidad;
        const igv = subtotal * IGV_RATE;

        html += `
            <tr>
                <td><img src="resources/img/productos/${producto.imagen}" alt="${producto.nombre}" style="width: 50px; height: 50px;"></td>
                <td>${producto.nombre}</td>
                <td>$${precio.toFixed(2)}</td>
                <td>${cantidad}</td>
                <td>$${igv.toFixed(2)}</td>
                <td>$${subtotal.toFixed(2)}</td>
            </tr>
        `;
    });
    
    $("#listaResumenProductos").html(html);
}

// Calcular y mostrar los totales del pedido (Subtotal, IGV, Total)
function calcularTotalesPedido() {
    let subtotalPedido = 0;
    let totalIGV = 0;

    carrito.forEach(producto => {
        const precio = producto.precio;
        const cantidad = producto.cantidad;
        const subtotal = precio * cantidad;
        const igv = subtotal * IGV_RATE;

        subtotalPedido += subtotal;
        totalIGV += igv;
    });

    const totalPedido = subtotalPedido + totalIGV;

    // Mostrar los valores en el HTML
    $("#subtotalPedido").text(`$${subtotalPedido.toFixed(2)}`);
    $("#igvPedido").text(`$${totalIGV.toFixed(2)}`);
    $("#totalPedido").text(`$${totalPedido.toFixed(2)}`);
}

// Inicializar módulo 3 cuando se accede
$(document).ready(function () {
    if ($("#step3").is(":visible")) {
        cargarInformacionPedido(); // Cargar la información del pedido si estamos en el módulo 3
    }
});
