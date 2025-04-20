package com.SriLanka.BarberShop.controller;

import com.SriLanka.BarberShop.dto.AgendamentoRequest;
import com.SriLanka.BarberShop.model.Agenda;
import com.SriLanka.BarberShop.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoRequest request){
        Agenda novoAgendamento = agendamentoService.agendar(request);
        return ResponseEntity.ok("Agendamento confirmado!");
    }

    @GetMapping
    public ResponseEntity<List<Agenda>> listarTodos() {
        List<Agenda> agendamentos = agendamentoService.listarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Agenda> agendamento = agendamentoService.buscarPorId(id);

        if (agendamento.isPresent()) {
            return ResponseEntity.ok(agendamento.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
        }
    }

}
