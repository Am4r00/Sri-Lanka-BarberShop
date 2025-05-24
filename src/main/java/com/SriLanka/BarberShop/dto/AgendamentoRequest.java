package com.SriLanka.BarberShop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
public class AgendamentoRequest {

    private Long clienteId;
    private Long servicoId;
    private Long estabelecimentoId;
    private LocalDateTime dataHora;
    private String observacoes;
}
