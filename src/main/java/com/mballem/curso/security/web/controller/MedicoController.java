package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    // abrir pagina de dados pessoais de medicos pelo MEDICO
    @GetMapping("/dados")
    public String abrirPorMedico(Medico medico, ModelMap model) {
        return "medico/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(Medico medico, RedirectAttributes attributes) {
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
}
