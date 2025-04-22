package com.SriLanka.BarberShop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequest {

    private Long clienteId;
    private Long servicoId;
    private Long estabelecimentoId;
    private LocalDateTime dataHora;
    private String observacoes;
}
