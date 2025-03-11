package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;
    @Autowired
    private UsuarioService usuarioService;

    // abrir pagina de dados pessoais de medicos pelo MEDICO
    @GetMapping("/dados")
    public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {
        if(medico.hasNotId()) {
            medico = medicoService.buscarPorEmail(user.getUsername());
            model.addAttribute("medico", medico);
        }
        return "medico/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Medico medico, RedirectAttributes attributes, @AuthenticationPrincipal User user) {
        // Se o metodo for chamado por um MEDICO
        if(medico.hasNotId() && medico.getUsuario().hasNotId()) {
            Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
            medico.setUsuario(usuario);
        }
        medicoService.salvar(medico);
        attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
        attributes.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    @PostMapping("/editar")
    public String editar(Medico medico, RedirectAttributes attributes) {
        medicoService.editar(medico);
        attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
        attributes.addFlashAttribute("medico", medico);
        return "redirect:/medicos/dados";
    }

    @GetMapping("/id/{idMed}/excluir/especializacao/{idEsp}")
    public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed,
                          @PathVariable("idEsp") Long idEsp,
                          RedirectAttributes attributes) {
        medicoService.excluirEspecialidadePorMedico(idMed, idEsp);
        attributes.addFlashAttribute("sucesso", "Especialidade removida com sucesso!");
        return "redirect:/medicos/dados";
    }
}
