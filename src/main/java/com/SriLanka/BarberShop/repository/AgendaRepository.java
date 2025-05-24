package com.SriLanka.BarberShop.repository;

import com.SriLanka.BarberShop.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByEstabelecimentoIdAndDataHoraBetween(Long estabelecimentoId, LocalDateTime inicio, LocalDateTime fim);

    List<Agenda> findByClienteId(Long id);

    @Query("SELECT a FROM Agenda a WHERE DATE(a.dataHora) = :data")
    List<Agenda> findByData(@Param("data") LocalDate data);
}
