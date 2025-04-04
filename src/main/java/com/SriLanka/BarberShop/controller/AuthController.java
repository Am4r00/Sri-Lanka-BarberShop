package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.AuthResponse;
import com.SriLanka.BarberShop.dto.AuthRequest;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import com.SriLanka.BarberShop.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.gerarToken(usuario.getEmail());

        return new AuthResponse(token);
    }

    @PostMapping("/registrar")
    public String registrar(@RequestBody AuthRequest request) {
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuarioRepository.save(usuario);
        return "Usuário registrado com sucesso!";
    }
}