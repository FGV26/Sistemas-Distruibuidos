package Controler;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import dao.PedidoDAO;
import entidades.Pedido;
import entidades.DetallePedido;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet(name = "GenerarBoleta", urlPatterns = {"/GenerarBoleta"})
public class GenerarBoleta extends HttpServlet {

    private PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idPedidoStr = request.getParameter("idPedido");
        try {
            int idPedido = Integer.parseInt(idPedidoStr);

            // Obtener los datos del pedido
            Pedido pedido = pedidoDAO.obtenerPedidoConDetalles(idPedido);

            if (pedido == null) {
                response.setContentType("text/html");
                response.getWriter().println("El pedido no fue encontrado.");
                return;
            }

            // Configuración de respuesta para PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Boleta_" + pedido.getCodPedido() + ".pdf");

            // Crear el PDF
            try (OutputStream out = response.getOutputStream()) {
                Document document = new Document();
                PdfWriter.getInstance(document, out);
                document.open();

                // **Encabezado**
                document.add(new Paragraph("Supermercados Siestitas S.A.", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph("R.U.C.: 20100045783", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("BOLETA DE VENTA ELECTRÓNICA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(Chunk.NEWLINE);

                // **Información del Pedido**
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(10);
                infoTable.setSpacingAfter(10);

                infoTable.addCell(new Phrase("Código del Pedido:"));
                infoTable.addCell(pedido.getCodPedido());

                infoTable.addCell(new Phrase("Cliente:"));
                infoTable.addCell(pedido.getNombreCliente());

                infoTable.addCell(new Phrase("Empleado:"));
                infoTable.addCell(pedido.getNombreEmpleado());

                infoTable.addCell(new Phrase("Despachador:"));
                infoTable.addCell(pedido.getNombreDespachador());

                infoTable.addCell(new Phrase("Fecha del Pedido:"));
                infoTable.addCell(pedido.getFechaPedido().toString());

                document.add(infoTable);

                // **Tabla de Detalles del Pedido**
                PdfPTable detalleTable = new PdfPTable(5); // 5 columnas: Producto, Cantidad, Precio, Total, TotalDetalle
                detalleTable.setWidthPercentage(100);
                detalleTable.setWidths(new int[]{3, 5, 2, 2, 2}); // Anchos de las columnas

                // **Encabezado de la Tabla**
                detalleTable.addCell("Código Producto");
                detalleTable.addCell("Nombre");
                detalleTable.addCell("Cantidad");
                detalleTable.addCell("Precio Unitario");
                detalleTable.addCell("Total Detalle");

                // **Detalles de los Productos**
                List<DetallePedido> detalles = pedido.getDetalles();
                for (DetallePedido detalle : detalles) {
                    String CodigoImg = detalle.getImagenProducto();
                    CodigoImg = CodigoImg.replace(".jpg", "");
                    detalleTable.addCell(CodigoImg + "");
                    detalleTable.addCell(detalle.getNombreProducto());
                    detalleTable.addCell(detalle.getCantidad() + "");
                    detalleTable.addCell("S/ " + detalle.getPrecio());
                    detalleTable.addCell("S/ " + detalle.getTotal());
                }

                document.add(detalleTable);

                // **Total General**
                Paragraph totalGeneral = new Paragraph("Total General: S/ " + pedido.getTotal(),
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
                totalGeneral.setAlignment(Element.ALIGN_RIGHT);
                document.add(Chunk.NEWLINE);
                document.add(totalGeneral);

                // **Términos y Condiciones**
                Paragraph terminos = new Paragraph("Términos y Condiciones:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
                terminos.add(new Paragraph(
                        "1. Esta boleta es válida solo como comprobante de pago.\n"
                        + "2. Los productos están sujetos a disponibilidad.\n"
                        + "3. En caso de devoluciones, conserve este documento para tramitar su solicitud.\n"
                        + "4. Si tiene alguna duda, comuníquese con nuestro servicio de atención al cliente.",
                        FontFactory.getFont(FontFactory.HELVETICA, 10)
                ));
                terminos.setSpacingBefore(20);
                document.add(terminos);

                // Cerrar el documento
                document.close();
            }

        } catch (DocumentException | IOException | NumberFormatException e) {
            e.printStackTrace(System.out);
            response.setContentType("text/html");
            response.getWriter().println("Ocurrió un error al generar la boleta.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
