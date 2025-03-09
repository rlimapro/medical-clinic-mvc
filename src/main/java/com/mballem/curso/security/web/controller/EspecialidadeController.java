package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService service;

    @GetMapping({"", "/"})
    public String abrir(Especialidade especialidade) {
        return "especialidade/especialidade";
    }

    @PostMapping("/salvar")
    public String salvar(Especialidade especialidade, RedirectAttributes attributes) {
        service.salvar(especialidade);
        attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
        return "redirect:/especialidades";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
        return ResponseEntity.ok(service.buscarEspecialidades(request));
    }

    @GetMapping("/editar/{id}")
    public String preEditar(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("especialidade", service.buscarPorId(id));
        return "especialidade/especialidade";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes attributes) {
        service.remover(id);
        attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
        return "redirect:/especialidades";
    }

    @GetMapping("/titulo")
    public ResponseEntity<?> getEspecialidadesPorTermo(@RequestParam("termo") String termo) {
        List<String> especialidades = service.buscarEspecialidadesPorTermo(termo);
        return ResponseEntity.ok(especialidades);
    }
}
