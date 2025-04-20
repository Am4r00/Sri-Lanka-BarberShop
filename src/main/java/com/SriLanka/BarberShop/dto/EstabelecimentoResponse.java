package com.SriLanka.BarberShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EstabelecimentoResponse {
    private Long id;
    private String nome;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
}
