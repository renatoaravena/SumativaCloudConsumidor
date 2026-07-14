package com.duoc.SumativaCloudConsumidor.consumer;

import com.duoc.SumativaCloudConsumidor.constants.RabbitConstants;
import com.duoc.SumativaCloudConsumidor.dto.GuiaMessage;
import com.duoc.SumativaCloudConsumidor.model.GuiaProcesada;
import com.duoc.SumativaCloudConsumidor.repository.GuiaProcesadaRepository;

// Importamos tus servicios de S3 y generador de PDF
import com.duoc.SumativaCloudConsumidor.service.AwsS3Service;
import com.duoc.SumativaCloudConsumidor.service.PdfGeneratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuiaPersistListener {

    private final GuiaProcesadaRepository repository;
    
 
    private final AwsS3Service awsS3Service;
    private final PdfGeneratorService pdfGeneratorService;

    @RabbitListener(queues = RabbitConstants.GUIA_QUEUE)
    public void recibirYGuardar(GuiaMessage guia) {
        log.info("Mensaje recibido desde cola 1: {}", guia);

        try {

            String transportista = guia.getTransportista() != null ? guia.getTransportista() : "Sin_Transportista";
            String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            String idCorto = UUID.randomUUID().toString().substring(0, 8);
            String nombreArchivo = "guia_" + idCorto + ".pdf";
            
            String s3Key = fechaActual + "/" + transportista + "/" + nombreArchivo;
            log.info("Ruta S3 generada: {}", s3Key);

            byte[] pdfBytes = pdfGeneratorService.generarPdf(guia);

    
            awsS3Service.uploadBytes(s3Key, pdfBytes);
            log.info("PDF subido exitosamente a S3 en la ruta: {}", s3Key);

          
            GuiaProcesada entidad = new GuiaProcesada();
            entidad.setOperacion(guia.getOperacion());
            
  
            entidad.setS3KeyOriginal(s3Key); 
            
            entidad.setTransportista(guia.getTransportista());
            entidad.setNumeroPedido(guia.getNumeroPedido());
            entidad.setDestinatario(guia.getDestinatario());
            entidad.setDireccionDestino(guia.getDireccionDestino());
            entidad.setDescripcionCarga(guia.getDescripcionCarga());
            entidad.setPesoKg(guia.getPesoKg());
            entidad.setFechaProcesado(LocalDateTime.now());

            repository.save(entidad);
            log.info("Guía persistida en H2 con id: {}", entidad.getId());

        } catch (Exception e) {
            log.error("Error procesando la guía: {}. Será enviada a la DLQ.", e.getMessage());
            throw new RuntimeException("Fallo al procesar y subir a S3", e);
        }
    }
}