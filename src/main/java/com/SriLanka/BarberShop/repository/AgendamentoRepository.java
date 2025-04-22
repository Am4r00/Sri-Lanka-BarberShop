package com.SriLanka.BarberShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SriLanka.BarberShop.model.Agendamento;
import java.util.List;
import java.time.LocalDate;


public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    //busca todos os cortes do cliente
    List<Agendamento> findByClienteId(Long id);
    
    // Buscar Por data uso pro barbeiro
    List<Agendamento> findByData(LocalDate data);
}
