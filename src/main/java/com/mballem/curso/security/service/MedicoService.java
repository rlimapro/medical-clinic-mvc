package com.mballem.curso.security.service;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional(readOnly = true)
    public Medico buscarPorUsuarioId(Long id) {
        return medicoRepository.findByUsuarioId(id).orElse(new Medico());
    }

    @Transactional(readOnly = false)
    public void salvar(Medico medico) {
        medicoRepository.save(medico);
    }

    @Transactional(readOnly = false)
    public void editar(Medico medico) {
        // objeto persistente
        Medico editedMedico = medicoRepository.findById(medico.getId()).get();
        editedMedico.setCrm(medico.getCrm());
        editedMedico.setDtInscricao(medico.getDtInscricao());
        editedMedico.setNome(medico.getNome());
        if(!medico.getEspecialidades().isEmpty()) {
            editedMedico.getEspecialidades().addAll(medico.getEspecialidades());
        }
    }

    @Transactional(readOnly = true)
    public Medico buscarPorEmail(String email) {
        return medicoRepository.findByEmail(email).orElse(new Medico());
    }

    @Transactional(readOnly = false)
    public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
        Medico medico = medicoRepository.findById(idMed).get();
        medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));
    }

    @Transactional(readOnly = true)
    public List<Medico> buscarMedicosPorEspecialidades(String titulo) {
        return medicoRepository.findMedicosByEspecialidades(titulo);
    }

    @Transactional(readOnly = true)
    public boolean existeEspecialidadeAgendada(Long idEsp, Long idMed) {
        return medicoRepository.hasEspecialidadeAgendadada(idEsp, idMed).isPresent();
    }
}
