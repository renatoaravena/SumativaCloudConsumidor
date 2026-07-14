package com.duoc.SumativaCloudConsumidor.consumer;

import com.duoc.SumativaCloudConsumidor.constants.RabbitConstants;
import com.duoc.SumativaCloudConsumidor.dto.GuiaMessage;
import com.duoc.SumativaCloudConsumidor.model.GuiaProcesada;
import com.duoc.SumativaCloudConsumidor.repository.GuiaProcesadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuiaPersistListener {

    private final GuiaProcesadaRepository repository;

    @RabbitListener(queues = RabbitConstants.GUIA_QUEUE)
    public void recibirYGuardar(GuiaMessage guia) {
        log.info("Mensaje recibido desde cola 1: {}", guia);

        // Si algo falla aquí (ej: dato inválido), la excepción hace que
        // RabbitMQ rechace el mensaje y, por la config de DLX del producer,
        // termine en la cola 2 (guia.dlq) automáticamente.
        GuiaProcesada entidad = new GuiaProcesada();
        entidad.setOperacion(guia.getOperacion());
        entidad.setS3KeyOriginal(guia.getS3KeyOriginal());
        entidad.setTransportista(guia.getTransportista());
        entidad.setNumeroPedido(guia.getNumeroPedido());
        entidad.setDestinatario(guia.getDestinatario());
        entidad.setDireccionDestino(guia.getDireccionDestino());
        entidad.setDescripcionCarga(guia.getDescripcionCarga());
        entidad.setPesoKg(guia.getPesoKg());
        entidad.setFechaProcesado(LocalDateTime.now());

        repository.save(entidad);
        log.info("Guía persistida en H2 con id: {}", entidad.getId());
    }
}