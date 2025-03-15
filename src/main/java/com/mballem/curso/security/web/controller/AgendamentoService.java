package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Transactional(readOnly = true)
    public List<Horario> buscarHorariosDisponiveisPorMedicoAndData(Long id, LocalDate data) {
        return repository.findMedicoAndDataByHorarioDisponivel(id, data);
    }
}
