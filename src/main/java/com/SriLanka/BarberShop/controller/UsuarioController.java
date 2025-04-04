package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import com.SriLanka.BarberShop.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!usuario.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você só pode deletar sua própria conta.");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário deletado com sucesso.");
    }
}
