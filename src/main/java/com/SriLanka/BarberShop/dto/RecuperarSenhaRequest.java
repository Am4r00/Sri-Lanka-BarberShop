package com.SriLanka.BarberShop.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class RecuperarSenhaRequest {
    @NotBlank
    @Email
    private String email;
}