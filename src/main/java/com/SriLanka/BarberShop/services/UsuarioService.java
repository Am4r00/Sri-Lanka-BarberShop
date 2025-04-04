package com.SriLanka.BarberShop.services;

import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
