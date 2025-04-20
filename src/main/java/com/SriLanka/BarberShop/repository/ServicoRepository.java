package com.SriLanka.BarberShop.repository;

import com.SriLanka.BarberShop.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByBarbeiroId(Long barbeiroId);
}
