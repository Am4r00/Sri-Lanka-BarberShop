package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.AuthRequest;
import com.SriLanka.BarberShop.dto.AuthResponse;
import com.SriLanka.BarberShop.dto.RecuperarSenhaRequest;
import com.SriLanka.BarberShop.dto.RedefinirSenhaRequest;
import com.SriLanka.BarberShop.model.TipoUsuario;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import com.SriLanka.BarberShop.services.EmailService;
import com.SriLanka.BarberShop.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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

    @Autowired
    private EmailService emailService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest  authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getSenha())
            );

            System.out.println("Login autenticado com sucesso: " + authRequest.getEmail());

            Usuario usuario = usuarioRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String token = jwtService.gerarToken(usuario.getEmail());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao fazer login: " + e.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody AuthRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setNome(request.getNome());
        usuario.setTelefone(request.getTelefone());


        usuario.setTipo(request.getTipo());

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
    @PostMapping("/criar-usuario")
    @PreAuthorize("hasAuthority('BARBEIRO_ADMIN')")
    public ResponseEntity<String> criarUsuario(@RequestBody AuthRequest request, Authentication authentication) {
        Usuario criador = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));


        if (request.getTipo() != TipoUsuario.BARBEIRO) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você só pode criar usuários do tipo BARBEIRO.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(request.getSenha()));
        novoUsuario.setNome(request.getNome());
        novoUsuario.setTelefone(request.getTelefone());
        novoUsuario.setTipo(request.getTipo());
        novoUsuario.setCriadoPor(criador);

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário BARBEIRO criado com sucesso!");
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> solicitarRecuperacao(@RequestBody @Valid RecuperarSenhaRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("E-mail não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        String codigo = String.valueOf(new Random().nextInt(900000) + 100000); // Gera código de 6 dígitos

        usuario.setCodigoRecuperacao(codigo);
        usuario.setCodigoExpiracao(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        emailService.enviarEmail(usuario.getEmail(), "Recuperação de Senha",
                "Seu código de recuperação é: " + codigo);

        return ResponseEntity.ok("Código enviado para seu e-mail.");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody @Valid RedefinirSenhaRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("E-mail não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!request.getCodigo().equals(usuario.getCodigoRecuperacao()) ||
                usuario.getCodigoExpiracao().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido ou expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuario.setCodigoRecuperacao(null);
        usuario.setCodigoExpiracao(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

}
