package com.duoc.SumativaCloudConsumidor.service;

import com.duoc.SumativaCloudConsumidor.dto.GuiaMessage;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfGeneratorService {

    public byte[] generarPdf(GuiaMessage guia) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(baos);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            document.add(new Paragraph("Guía de Despacho"));
            document.add(new Paragraph("Transportista: " + guia.getTransportista()));
            document.add(new Paragraph("N° Pedido: " + guia.getNumeroPedido()));
            document.add(new Paragraph("Destinatario: " + guia.getDestinatario()));
            document.add(new Paragraph("Dirección destino: " + guia.getDireccionDestino()));
            document.add(new Paragraph("Descripción carga: " + guia.getDescripcionCarga()));
            document.add(new Paragraph("Peso (kg): " + guia.getPesoKg()));

        } catch (IOException e) {
            throw new RuntimeException("Error generando el PDF de la guía: " + guia.getNumeroPedido(), e);
        }

        return baos.toByteArray();
    }
}