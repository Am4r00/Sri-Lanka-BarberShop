package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.ServicoRequest;
import com.SriLanka.BarberShop.dto.ServicoResponse;
import com.SriLanka.BarberShop.model.Servico;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.ServicoRepository;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('BARBEIRO', 'BARBEIRO_ADMIN')")
    public ResponseEntity<?> criarServico(@RequestBody ServicoRequest request, Authentication authentication) {
        String email = authentication.getName();
        Usuario barbeiro = usuarioRepository.findByEmail(email).orElseThrow();

        Servico servico = new Servico();
        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setPreco(request.getPreco());
        servico.setBarbeiro(barbeiro);

        servicoRepository.save(servico);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ServicoResponse(servico));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BARBEIRO', 'BARBEIRO_ADMIN')")
    public List<ServicoResponse> listarServicosDoBarbeiro(Authentication authentication) {
        String email = authentication.getName();
        Usuario barbeiro = usuarioRepository.findByEmail(email).orElseThrow();

        return servicoRepository.findByBarbeiroId(barbeiro.getId())
                .stream().map(ServicoResponse::new).toList();
    }
}
