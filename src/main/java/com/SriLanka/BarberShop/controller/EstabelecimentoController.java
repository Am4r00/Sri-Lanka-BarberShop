package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.EstabelecimentoRequest;
import com.SriLanka.BarberShop.dto.EstabelecimentoResponse;
import com.SriLanka.BarberShop.model.Estabelecimento;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.EstabelecimentoRepository;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import com.SriLanka.BarberShop.services.ViaCepService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estabelecimentos")
@RequiredArgsConstructor
public class EstabelecimentoController {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ViaCepService viaCepService;

    @PostMapping
    @PreAuthorize("hasAnyRole('BARBEIRO_ADMIN')")
    public ResponseEntity<?> criar(@RequestBody EstabelecimentoRequest request, Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Map<String, String> endereco = viaCepService.buscarEnderecoPorCep(request.getCep());

        Estabelecimento est = new Estabelecimento();
        est.setNome(request.getNome());
        est.setCep(request.getCep());
        est.setRua(endereco.get("logradouro"));
        est.setBairro(endereco.get("bairro"));
        est.setCidade(endereco.get("localidade"));
        est.setEstado(endereco.get("uf"));
        est.setNumero(request.getNumero());
        est.setComplemento(request.getComplemento());
        est.setBarbeiro(usuario);

        estabelecimentoRepository.save(est);

        return ResponseEntity.ok(new EstabelecimentoResponse(
                est.getId(), est.getNome(), est.getRua(), est.getNumero(),
                est.getBairro(), est.getCidade(), est.getEstado()
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('BARBEIRO_ADMIN')")
    public ResponseEntity<List<EstabelecimentoResponse>> listar(Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<EstabelecimentoResponse> lista = estabelecimentoRepository.findByBarbeiroId(usuario.getId())
                .stream()
                .map(est -> new EstabelecimentoResponse(
                        est.getId(), est.getNome(), est.getRua(), est.getNumero(),
                        est.getBairro(), est.getCidade(), est.getEstado()
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('BARBEIRO_ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody EstabelecimentoRequest request, Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Estabelecimento est = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        if (!est.getBarbeiro().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body("Você não tem permissão para editar esse estabelecimento");
        }

        Map<String, String> endereco = viaCepService.buscarEnderecoPorCep(request.getCep());

        est.setNome(request.getNome());
        est.setCep(request.getCep());
        est.setRua(endereco.get("logradouro"));
        est.setBairro(endereco.get("bairro"));
        est.setCidade(endereco.get("localidade"));
        est.setEstado(endereco.get("uf"));
        est.setNumero(request.getNumero());
        est.setComplemento(request.getComplemento());

        estabelecimentoRepository.save(est);

        return ResponseEntity.ok("Estabelecimento atualizado com sucesso!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('BARBEIRO_ADMIN')")

    public ResponseEntity<?> deletar(@PathVariable Long id, Authentication auth) {
        Usuario usuario = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Estabelecimento est = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        if (!est.getBarbeiro().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body("Você não tem permissão para deletar esse estabelecimento");
        }

        estabelecimentoRepository.delete(est);
        return ResponseEntity.ok("Estabelecimento deletado com sucesso!");
    }
    @GetMapping("/buscar-por-cep/{cep}")
    @PermitAll
    public ResponseEntity<List<EstabelecimentoResponse>> buscarPorCep(@PathVariable String cep) {
        List<EstabelecimentoResponse> lista = estabelecimentoRepository.findByCep(cep)
                .stream()
                .map(est -> new EstabelecimentoResponse(
                        est.getId(), est.getNome(), est.getRua(), est.getNumero(),
                        est.getBairro(), est.getCidade(), est.getEstado()
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }

}

