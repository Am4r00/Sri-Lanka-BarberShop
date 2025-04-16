package com.SriLanka.BarberShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipo;

    @ManyToOne
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    @Column(name = "codigo_recuperacao", length = 10)
    private String codigoRecuperacao;

    @Column(name = "codigo_expiracao")
    private LocalDateTime codigoExpiracao;
}
