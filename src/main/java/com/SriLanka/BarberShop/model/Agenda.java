package com.SriLanka.BarberShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @ManyToOne
    private Usuario cliente;

    @ManyToOne
    private Servico servico;

    @ManyToOne
    private Estabelecimento estabelecimento;

    private String observacoes;

}
