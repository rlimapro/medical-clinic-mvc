package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private EspecialidadeService especialidadeService;

    @GetMapping("/agendar")
    public String agendarConsulta(Agendamento agendamento) {
        return "agendamento/cadastro";
    }

    @GetMapping("/horario/medico/{id}/data/{data}")
    public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
                                         @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {

        return ResponseEntity.ok(service.buscarHorariosDisponiveisPorMedicoAndData(id, data));
    }

    @PostMapping("/salvar")
    public String salvar(Agendamento agendamento,
                         RedirectAttributes attributes,
                         @AuthenticationPrincipal User user) {

        Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
        String especialidadeTitulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService.buscarEspecialidadePorTitulo(especialidadeTitulo);

        agendamento.setPaciente(paciente);
        agendamento.setEspecialidade(especialidade);

        service.salvar(agendamento);

        attributes.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso!");

        return "redirect:/agendamentos/agendar";
    }
}
