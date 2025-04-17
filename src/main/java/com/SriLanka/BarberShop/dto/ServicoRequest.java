package com.SriLanka.BarberShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class ServicoRequest {
    private String nome;
    private String descricao;
    private BigDecimal preco;
}