package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/u")
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private UsuarioService usuarioService;

    // abrir cadastro de usuarios (medico/admin/paciente)
    @GetMapping("/novo/cadastro/usuario")
    public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
        return "usuario/cadastro";
    }

    // abrir lista de usuarios
    @GetMapping("/lista")
    public String listarUsuarios() {
        return "usuario/lista";
    }

    // listar usuarios na datatables
    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listarUsuariosDatatable(HttpServletRequest request) {
        return ResponseEntity.ok(service.buscarTodos(request));
    }

    // salvar cadastro de usuarios por admin
    @PostMapping("/cadastro/salvar")
    public String salvarUsuario(Usuario usuario, RedirectAttributes attributes) {
        List<Perfil> perfis = usuario.getPerfis();
        if(
                perfis.size() > 2
                || perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L)))
                || perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))
        ) {
            attributes.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
            attributes.addFlashAttribute("usuario", usuario);
        } else {
            try {
                service.salvarUsuario(usuario);
                attributes.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
            } catch (DataIntegrityViolationException exception) {
                attributes.addFlashAttribute("falha", "Cadastro não realizado, email já existente!");
            }
        }
        return "redirect:/u/novo/cadastro/usuario";
    }

    // pre edicao de credenciais de usuarios
    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
        return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
    }

    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
                                                       @PathVariable("perfis") Long[] perfisId) {

        Usuario usuario = service.buscarPorIdAndPerfis(usuarioId, perfisId);

        // Se for apenas ADMIN
        if(usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) && !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
            return new ModelAndView("usuario/cadastro", "usuario", usuario);
        }
        // Se for MEDICO
        else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
            Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
            return medico.hasNotId()
                    ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
                    : new ModelAndView("medico/cadastro", "medico", medico);
        }
        // Se for PACIENTE
        else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
            ModelAndView model = new ModelAndView("error");
            model.addObject("status", 403);
            model.addObject("error", "Área restrita!");
            model.addObject("message", "Os dados de pacientes são restritos à pacientes.");
            return model;
        }

        return new ModelAndView("redirect:/u/lista");
    }

    @GetMapping("/editar/senha")
    public String abrirEditarSenha() {
        return "usuario/editar-senha";
    }

    @PostMapping("/confirmar/senha")
    public String editarSenha(@RequestParam("senha1") String s1,
                              @RequestParam("senha2") String s2,
                              @RequestParam("senha3") String s3,
                              @AuthenticationPrincipal User user, RedirectAttributes attributes) {

        if(!s1.equals(s2)) {
            attributes.addFlashAttribute("falha", "Senhas são diferentes, tente novamente!");
            return "redirect:/u/editar/senha";
        }

        Usuario u = service.buscarPorEmail(user.getUsername());
        if(!usuarioService.isSenhaCorreta(s3, u.getSenha())) {
            attributes.addFlashAttribute("falha", "Senha atual incorreta, tente novamente!");
            return "redirect:/u/editar/senha";
        }

        usuarioService.alterarSenha(u, s1);
        attributes.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
        return "redirect:/u/editar/senha";
    }
}
