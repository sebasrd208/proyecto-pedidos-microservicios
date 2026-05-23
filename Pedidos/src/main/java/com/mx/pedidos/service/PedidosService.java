package com.mx.pedidos.service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import org.modelmapper.*;
import com.lowagie.text.*;
import com.google.zxing.*;
import java.util.stream.*;
import com.mx.pedidos.dto.*;
import com.lowagie.text.pdf.*;
import com.mx.pedidos.models.*;
import com.mx.pedidos.entity.*;
import com.google.zxing.qrcode.*;
import com.google.zxing.common.*;
import com.mx.pedidos.repository.*;
import com.mx.pedidos.excepciones.*;
import com.google.zxing.client.j2se.*;
import com.mx.pedidos.serviceClient.*;
import org.springframework.stereotype.*;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.*;

@Service
public class PedidosService {

    @Autowired
    iPedidosRepository repository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    ProductosServiceClient productoServiceClient;

    @Autowired
    ProveedorServiceClient proveedorServiceClient;

    public List<PedidosResponseDTO> listar(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "idPedido")).stream().
        map(pedidos -> mapper.map(pedidos, PedidosResponseDTO.class)).collect(Collectors.toList());
    }

    public PedidosResponseDTO buscar(int idPedido){
        Pedidos pedidos = repository.findById(idPedido)
                .orElseThrow(()-> new PedidosNotFoundException("Pedido no encontrado con el id " + idPedido));

        return mapper.map(pedidos, PedidosResponseDTO.class);
    }

    public void eliminar(int id){
        Pedidos pedidos = repository.findById(id)
                .orElseThrow(() -> new PedidosNotFoundException("Pedido no encontrado con el id " + id));

        repository.delete(pedidos);
    }

    public PedidosResponseDTO guardarPedido(PedidosRequestDTO dto){
        Pedidos pedidos = mapper.map(dto, Pedidos.class);

        return mapper.map(repository.save(pedidos), PedidosResponseDTO.class);
    }

    public ProveedorResponseDTO buscarProveedor(int idProveedor){
        ProveedorResponseDTO proveedor = proveedorServiceClient.buscarProveedorSeguro(idProveedor);

        return mapper.map(proveedor, ProveedorResponseDTO.class);
    }

    public TicketProveedorResponseDTO generar(RespuestaRequestDTO request) {

        ProductoRequestDTO productoRequest = new ProductoRequestDTO();
        productoRequest.setPedidos(request.getPedidos());

        List<ProductosDTO> productosGenerados =
                productoServiceClient.generarTicket(productoRequest);

        double totalGeneral = 0;

        if (productosGenerados != null && !productosGenerados.isEmpty()) {
            totalGeneral = productosGenerados.get(0).getTotal();
        }

        PedidosRequestDTO pedidoRequest = new PedidosRequestDTO();
        pedidoRequest.setProveedorId(request.getProveedor());
        pedidoRequest.setTotal(totalGeneral);
        pedidoRequest.setEstado(Estado.PROCESADO);
        PedidosResponseDTO pedido = guardarPedido(pedidoRequest);

        ProveedorResponseDTO proveedor =
                proveedorServiceClient.buscarProveedorSeguro(request.getProveedor());

        TicketProveedorResponseDTO response = new TicketProveedorResponseDTO();
        response.setPedido(pedido);
        response.setProveedor(proveedor);
        response.setTicket(productosGenerados);

        return response;
    }

    public byte[] generarPDF(RespuestaRequestDTO request) {

        try {
            ProductoRequestDTO productoRequest = new ProductoRequestDTO();
            productoRequest.setPedidos(request.getPedidos());

            List<ProductosDTO> productosGenerados =
                    productoServiceClient.generarTicket(productoRequest);

            double totalGeneral = 0;
            if (productosGenerados != null && !productosGenerados.isEmpty()) {
                totalGeneral = productosGenerados.get(0).getTotal();
            }

            PedidosRequestDTO pedidoRequest = new PedidosRequestDTO();
            pedidoRequest.setProveedorId(request.getProveedor());
            pedidoRequest.setTotal(totalGeneral);
            pedidoRequest.setEstado(Estado.PROCESADO);
            pedidoRequest.setDocumento(null);

            PedidosResponseDTO pedidoGuardado = guardarPedido(pedidoRequest);
            ProveedorResponseDTO proveedor =
                    proveedorServiceClient.buscarProveedorSeguro(request.getProveedor());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Rectangle pageSize = new Rectangle(226, 842);
            Document document = new Document(pageSize, 12, 12, 10, 10);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font fontBase = new Font(Font.COURIER, 8, Font.NORMAL);
            Font fontBold = new Font(Font.COURIER, 8, Font.BOLD);
            Font fontTitle = new Font(Font.COURIER, 10, Font.BOLD);
            Font fontMoney = new Font(Font.NORMAL, 10, Font.BOLD);
            Font fontSmall = new Font(Font.COURIER, 6, Font.NORMAL);

            Paragraph header = new Paragraph();
            header.setAlignment(Element.ALIGN_CENTER);
            header.add(new Chunk("Abarrotes \"El Atorón\"\n", fontTitle));
            header.add(new Chunk("Fecha: " + pedidoGuardado.getFechaPedido() + "\n", fontBase));
            header.add(new Chunk("Factura No. " + pedidoGuardado.getIdPedido() + "\n", fontBase));
            header.add(new Chunk("--------------------------------\n", fontBase));
            header.add(new Chunk(proveedor.getNombre().toUpperCase() + "\n", fontBold));
            header.add(new Chunk("Tel: " + proveedor.getTelefono() + "\n", fontBase));
            header.add(new Chunk("ENTREGA DE PRODUCTOS\n\n", fontBase));
            document.add(header);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 4.3f, 2.0f, 2.5f});

            String[] headers = {"CANT", "DESCRIPCION", "P.U.", "IMPORTE"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontBold));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(
                        h.equals("P.U.") || h.equals("IMPORTE") ?
                                Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            StringBuilder detalleQR = new StringBuilder();
            detalleQR.append("Folio: ").append(pedidoGuardado.getFolio()).append("\n");

            if (productosGenerados != null) {
                for (ProductosDTO ticket : productosGenerados) {
                    if (ticket.getCompras() != null) {
                        for (TicketDTO c : ticket.getCompras()) {

                            detalleQR.append("- ").append(c.getCantidad())
                                    .append(" ").append(c.getProducto())
                                    .append(" $").append(c.getPrecioUnitario()).append("\n");

                            PdfPCell c1 = new PdfPCell(new Phrase(c.getCantidad(), fontBase));
                            c1.setBorder(Rectangle.NO_BORDER);
                            table.addCell(c1);

                            PdfPCell c2 = new PdfPCell(new Phrase(c.getProducto(), fontBase));
                            c2.setBorder(Rectangle.NO_BORDER);
                            table.addCell(c2);

                            PdfPCell c3 = new PdfPCell(new Phrase("$" + c.getPrecioUnitario(), fontBase));
                            c3.setBorder(Rectangle.NO_BORDER);
                            c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(c3);

                            PdfPCell c4 = new PdfPCell(new Phrase("$" + c.getTotal(), fontBase));
                            c4.setBorder(Rectangle.NO_BORDER);
                            c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(c4);
                        }
                    }
                }
            }

            document.add(table);

            document.add(new Paragraph("--------------------------------", fontBase));

            PdfPTable tableTotal = new PdfPTable(2);
            tableTotal.setWidthPercentage(100);

            PdfPCell labelTotal = new PdfPCell(new Phrase("Total Neto $", fontMoney));
            labelTotal.setBorder(Rectangle.NO_BORDER);
            labelTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell valueTotal = new PdfPCell(new Phrase(String.format("%.2f", totalGeneral), fontTitle));
            valueTotal.setBorder(Rectangle.NO_BORDER);
            valueTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

            tableTotal.addCell(labelTotal);
            tableTotal.addCell(valueTotal);

            document.add(tableTotal);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(detalleQR.toString(),
                    BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream qrBaos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrBaos);

            Image qrImage = Image.getInstance(qrBaos.toByteArray());
            qrImage.scaleAbsolute(80, 80);
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);

            Paragraph footer = new Paragraph();
            footer.add(new Chunk("Folio: " + pedidoGuardado.getFolio() + "\n", fontSmall));
            footer.add(new Chunk("Estado: " + pedidoGuardado.getEstado() + "\n", fontSmall));
            document.add(footer);

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

            pedidoRequest.setIdPedido(pedidoGuardado.getIdPedido());
            pedidoRequest.setDocumento(pdfBase64);
            guardarPedido(pedidoRequest);

            return pdfBytes;

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el ticket", e);
        }
    }

    public byte[] obtenerPdfPorId(int idPedido) {

        Pedidos pedido = repository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String base64 = pedido.getDocumento();

        return Base64.getDecoder().decode(base64);
    }
}
