package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.AuthRequest;
import com.SriLanka.BarberShop.model.TipoUsuario;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import com.SriLanka.BarberShop.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<AuthRequest>> listarUsuarios(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário autenticado não encontrado"));

        List<Usuario> usuarios;

        if (usuarioLogado.getTipo() == TipoUsuario.CLIENTE) {
            usuarios = List.of(usuarioLogado);
        }else if (usuarioLogado.getTipo() == TipoUsuario.BARBEIRO_ADMIN) {
            usuarios = usuarioRepository.findByCriadoPor(usuarioLogado);
            usuarios.add(usuarioLogado);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AuthRequest> resposta = usuarios.stream()
                .map(u -> new AuthRequest(u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getTipo()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Usuario usuarioAlvo = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário a ser deletado não encontrado"));


        if (usuarioLogado.getId().equals(usuarioAlvo.getId())) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Sua conta foi deletada com sucesso.");
        }


        if (usuarioLogado.getTipo() == TipoUsuario.BARBEIRO_ADMIN &&
                usuarioAlvo.getCriadoPor() != null &&
                usuarioAlvo.getCriadoPor().getId().equals(usuarioLogado.getId())) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok("Usuário deletado com sucesso pelo admin.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar este usuário.");
    }



}
