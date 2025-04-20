package com.SriLanka.BarberShop.services;

import com.SriLanka.BarberShop.dto.AgendamentoRequest;
import com.SriLanka.BarberShop.model.Agenda;
import com.SriLanka.BarberShop.model.Estabelecimento;
import com.SriLanka.BarberShop.model.Servico;
import com.SriLanka.BarberShop.model.Usuario;
import com.SriLanka.BarberShop.repository.AgendaRepository;
import com.SriLanka.BarberShop.repository.EstabelecimentoRepository;
import com.SriLanka.BarberShop.repository.ServicoRepository;
import com.SriLanka.BarberShop.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    public Agenda agendar(AgendamentoRequest request){

        Usuario cliente = usuarioRepository.findById(request.getClienteId()).orElseThrow();
        Servico servico = servicoRepository.findById(request.getServicoId()).orElseThrow();
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(request.getEstabelecimentoId()).orElseThrow();

        Agenda agenda = new Agenda();
        agenda.setCliente(cliente);
        agenda.setServico(servico);
        agenda.setEstabelecimento(estabelecimento);
        agenda.setDataHora(request.getDataHora());
        agenda.setObservacoes(request.getObservacoes());

        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarTodos(){
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarPorId(Long id) {
        return agendaRepository.findById(id);
    }

}
