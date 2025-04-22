package com.SriLanka.BarberShop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SriLanka.BarberShop.model.Agendamento;
import com.SriLanka.BarberShop.model.Estabelecimento;
import com.SriLanka.BarberShop.repository.AgendamentoRepository;

@Service
public class AgentamentoService { // Formatação para o envio do email.
   
   @Autowired
   private AgendamentoRepository repo;

   @Autowired
   private EmailService emailService;

   public Agendamento marcarCorte(Agendamento agendamento){
    Agendamento salvo = repo.save(agendamento);

    String endereco = formatarEndereco(salvo.getBarbearia());

    String emailCliente = String.format(
        "Olá %s, seu corte foi marcado!\n\nLocal: %s\nBarbeiro: %s\nData: %s\nHora: %s",
        salvo.getCliente().getNome(),
        endereco,
        salvo.getBarbeiro().getNome(),
        salvo.getData(),
        salvo.getHora());
        
    String emailBarbeiro = String.format(
        "Novo corte agendado!\n\nCliente: %s\nData: %s\nHora: %s",
        salvo.getCliente().getNome(),
        salvo.getData(),
        salvo.getHora());

    
    emailService.enviarEmail(salvo.getCliente().getEmail(), "Confirmação de corte", emailCliente);
    emailService.enviarEmail(salvo.getBarbeiro().getEmail(), "Novo corte agendado", emailBarbeiro);

        return salvo;
   }
   
   
    public String formatarEndereco(Estabelecimento barbearia){
        return String.format("%s, %s - %s, %s - %s, %s - %s",
        barbearia.getRua(),barbearia.getNumero(),
        barbearia.getComplemento() != null ? barbearia.getComplemento() : " ",
        barbearia.getBairro(), barbearia.getCidade(), barbearia.getEstado(), barbearia.getCep());
    }
}
