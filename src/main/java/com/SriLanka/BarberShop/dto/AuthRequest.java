package com.SriLanka.BarberShop.dto;

import com.SriLanka.BarberShop.model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private Long id;
    private String email;
    private String senha;
    private String nome;
    private String telefone;
    private TipoUsuario tipo;

    public AuthRequest(Long id, String nome, String email, String telefone, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
    }
}
