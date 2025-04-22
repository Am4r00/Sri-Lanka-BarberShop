package com.SriLanka.BarberShop.repository;
import com.SriLanka.BarberShop.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    
    List<Agenda> findByEstabelecimentoIdAndDataHoraBetween(Long estabelecimentoId, LocalDateTime inicio, LocalDateTime fim);

     //busca todos os cortes do cliente
    List<Agenda> findByClienteId(Long id);
    
    // Buscar Por data uso pro barbeiro
    List<Agenda> findByData(LocalDate data);

}
