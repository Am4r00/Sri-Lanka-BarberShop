package com.SriLanka.BarberShop.repository;

import com.SriLanka.BarberShop.model.Estabelecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long> {
    List<Estabelecimento> findByBarbeiroId(Long barbeiroId);
    List<Estabelecimento> findByCep(String cep);
}
