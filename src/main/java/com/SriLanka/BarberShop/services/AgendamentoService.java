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

    @Autowired
    private  EmailService emailService;

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

        enviarEmailBarbeiro(agenda);
        enviarEmailCliente(agenda);
    

        return agendaRepository.save(agenda);
    }

    public String enviarEmailBarbeiro(Agenda agenda){
        String emailBarbeiro = String.format(
            "Novo corte agendado!\n\nCliente: %s\nData: %s",
            agenda.getCliente().getNome(),
            agenda.getDataHora());

            emailService.enviarEmail(agenda.getServico().getBarbeiro().getEmail(), "Novo corte agendado", emailBarbeiro);

            return emailBarbeiro;
    }

    public String enviarEmailCliente(Agenda agenda){
        String endereco = formatarEndereco(agenda.getEstabelecimento());

        String emailCliente = String.format(
            "Olá %s, seu corte foi marcado!\n\nLocal: %s\nBarbeiro: %s\nData e Hora : %s",
            agenda.getCliente().getNome(),
            endereco,
            agenda.getServico().getBarbeiro().getNome(),
            agenda.getDataHora());
    
        emailService.enviarEmail(agenda.getCliente().getEmail(), "Confirmação de corte", emailCliente);

        return emailCliente;
    }

    public List<Agenda> listarTodos(){
        return agendaRepository.findAll();
    }

    public Optional<Agenda> buscarPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public String formatarEndereco(Estabelecimento barbearia){
        return String.format("%s, %s - %s, %s - %s, %s - %s",
        barbearia.getRua(),barbearia.getNumero(),
        barbearia.getComplemento() != null ? barbearia.getComplemento() : " ",
        barbearia.getBairro(), barbearia.getCidade(), barbearia.getEstado(), barbearia.getCep());
    }

}
