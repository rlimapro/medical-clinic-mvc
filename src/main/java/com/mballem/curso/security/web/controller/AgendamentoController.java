package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping({"/historico/paciente", "/historico/consultas"})
    public String historico() {
        return "agendamento/historico-paciente";
    }

    @GetMapping("/datatables/server/historico")
    public ResponseEntity<?> historicoAgendamentoPorPaciente(HttpServletRequest request, @AuthenticationPrincipal User user) {
        if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {
            return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
        }
        else if(user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {
            return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/editar/consulta/{id}")
    public String preEditarConsultaPaciente(@PathVariable("id") Long id,
                                            Model model,
                                            @AuthenticationPrincipal User user) {

        Agendamento agendamento = service.buscarPorId(id);
        model.addAttribute("agendamento", agendamento);
        return "agendamento/cadastro";
    }

    @PostMapping("/editar")
    public String editarConsulta(Agendamento agendamento,
                                 RedirectAttributes attributes,
                                 @AuthenticationPrincipal User user) {

        String especialidadeTitulo = agendamento.getEspecialidade().getTitulo();
        Especialidade especialidade = especialidadeService.buscarEspecialidadePorTitulo(especialidadeTitulo);
        agendamento.setEspecialidade(especialidade);

        service.editar(agendamento, user.getUsername());
        attributes.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso");
        return "redirect:/agendamentos/agendar";
    }
}
