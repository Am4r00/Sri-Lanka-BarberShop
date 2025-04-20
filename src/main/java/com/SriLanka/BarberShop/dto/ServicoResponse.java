package com.SriLanka.BarberShop.dto;


import com.SriLanka.BarberShop.model.Servico;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServicoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;

    public ServicoResponse(Servico servico) {
        this.id = servico.getId();
        this.nome = servico.getNome();
        this.descricao = servico.getDescricao();
        this.preco = servico.getPreco();
    }
}
