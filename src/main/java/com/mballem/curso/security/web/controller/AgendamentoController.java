package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @GetMapping("/agendar")
    public String agendarConsulta(Agendamento agendamento) {
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
                                         @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {

        return ResponseEntity.ok(service.buscarHorariosDisponiveisPorMedicoAndData(id, data));
    }
}
