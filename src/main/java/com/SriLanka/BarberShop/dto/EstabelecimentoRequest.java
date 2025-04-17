package com.SriLanka.BarberShop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstabelecimentoRequest {
    private String nome;
    private String cep;
    private String numero;
    private String complemento;
}