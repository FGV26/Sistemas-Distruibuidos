
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

    if (!dniInput) {
        alert("Por favor, ingrese un DNI para buscar.");
        return;
    }

    const data = {
        accion: "BuscarCliente",
        dni: dniInput
    };

    $.post("/Sistemas-Distruibuidos/GestionDePedidos", data, function (response) {
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

        // Guardar en sessionStorage
        sessionStorage.setItem("cliente", JSON.stringify(response));
        console.log(sessionStorage.getItem("cliente"));

        clienteSeleccionado = true;
        $("#nextToStep2").prop("disabled", false);  // Habilitar botón "Siguiente"
        bloquearRegistro();  // Bloquear el formulario de registro
    }).fail(function () {
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

    if (!data.dni || !data.nombre || !data.apellido || !data.direccion || !data.telefono || !data.email) {
        alert("Todos los campos de registro son obligatorios.");
        return;
    }

    $.post("/Sistemas-Distruibuidos/GestionDePedidos", data, function (response) {
        if (response.error) {
            alert(response.error);
            return;
        }

        alert("Cliente registrado correctamente.");

        // Guardar en sessionStorage
        sessionStorage.setItem("cliente", JSON.stringify(data));

        clienteSeleccionado = true;
        $("#nextToStep2").prop("disabled", false);
        bloquearBusqueda();  // Bloquear el formulario de búsqueda
    }).fail(function () {
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
            body: JSON.stringify({dni: dni})
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

    // Guardar el carrito en sessionStorage
    sessionStorage.setItem("carrito", JSON.stringify(carrito));
    console.log(sessionStorage.getItem("carrito"));

   
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

// Cargar datos del cliente y productos del carrito en el módulo 3
function cargarDatosClienteEnModulo3() {
    console.log("Cargando datos del cliente en el módulo 3");

    // 1. Obtener código de pedido desde el backend
    generarCodigoPedido();

    // 2. Cargar información del cliente
    const cliente = JSON.parse(sessionStorage.getItem("cliente"));
    
    
    if (cliente) {
        $("#codigoCliente").val(cliente.codCliente);  
        $("#nombreCliente").val(`${cliente.nombre}  ${cliente.apellido}`);
        $("#direccionCliente").val(cliente.direccion);
        $("#fechaPedido").val(new Date().toLocaleDateString());
    } else {
        console.warn("No se encontraron datos del cliente en sessionStorage.");
    }
    
    // 3. Cargar y mostrar productos del carrito con desglose de precios
    const carrito = JSON.parse(sessionStorage.getItem("carrito"));
    if (carrito && carrito.length > 0) {
        let html = "";
        let subtotal = 0;
        const IGV_RATE = 0.18;  // Tasa de IGV, por ejemplo 18%

        carrito.forEach((item, index) => {
            const itemSubtotal = item.precio * item.cantidad;
            subtotal += itemSubtotal;
            html += `
                <tr>
                    <td>${index + 1}</td> <!-- Número de ítem -->
                    <td><img src="resources/img/productos/${item.imagen}" alt="${item.nombre}" style="width: 50px; height: 50px;"></td>
                    <td>${item.nombre}</td>
                    <td>${item.cantidad}</td>
                    <td>$${item.precio.toFixed(2)}</td>
                    <td>$${itemSubtotal.toFixed(2)}</td>
                </tr>
            `;
        });

        console.log("Datos del cliente:", cliente);
        console.log("Productos en el carrito:", carrito);

        // Calcular IGV y total
        const igv = subtotal * IGV_RATE;
        const total = subtotal + igv;

        // Insertar HTML de los productos y mostrar desglose de precios
        $("#listaResumenProductos").html(html);
        $("#subtotalPedido").text(`$${subtotal.toFixed(2)}`);
        $("#igvPedido").text(`$${igv.toFixed(2)}`);
        $("#totalPedido").text(`$${total.toFixed(2)}`);
    } else {
        console.warn("No se encontraron datos del carrito en sessionStorage.");
    }
}

// AJAX para generar y mostrar el código de pedido
function generarCodigoPedido() {
    $.ajax({
        url: "/Sistemas-Distruibuidos/GestionDePedidos",
        type: "GET",
        data: { accion: "GenerarCodigoPedido" },
        success: function(response) {
            $("#numeroPedido").val(response.codPedido); // Asignar código de pedido al campo
        },
        error: function() {
            console.error("Error al obtener el código de pedido.");
        }
    });
}

$(document).ready(function () {
    console.log("holi");
    if ($("#confirmacion").is(":visible")) {
        cargarDatosClienteEnModulo3();
    }
});
;
