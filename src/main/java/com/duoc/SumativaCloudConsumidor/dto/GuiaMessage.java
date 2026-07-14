package com.duoc.SumativaCloudConsumidor.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuiaMessage {
    private String operacion;
    private String s3KeyOriginal;
    private String transportista;
    private String numeroPedido;
    private String destinatario;
    private String direccionDestino;
    private String descripcionCarga;
    private double pesoKg;
}