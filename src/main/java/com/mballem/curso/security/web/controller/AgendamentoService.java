package com.mballem.curso.security.web.controller;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.AgendamentoRepository;
import com.mballem.curso.security.repository.projection.HistorioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;
    @Autowired
    private Datatables datatable;

    @Transactional(readOnly = true)
    public List<Horario> buscarHorariosDisponiveisPorMedicoAndData(Long id, LocalDate data) {
        return repository.findMedicoAndDataByHorarioDisponivel(id, data);
    }

    @Transactional(readOnly = false)
    public void salvar(Agendamento agendamento) {
        repository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, HttpServletRequest request) {
        datatable.setRequest(request);
        datatable.setColunas(DatatablesColunas.AGENDAMENTOS);
        Page<HistorioPaciente> page = repository.findByHistoricoPacienteEmail(email, datatable.getPageable());
        return datatable.getResponse(page);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarHistoricoPorMedicoEmail(String email, HttpServletRequest request) {
        datatable.setRequest(request);
        datatable.setColunas(DatatablesColunas.AGENDAMENTOS);
        Page<HistorioPaciente> page = repository.findByHistoricoMedicoEmail(email, datatable.getPageable());
        return datatable.getResponse(page);
    }
}
