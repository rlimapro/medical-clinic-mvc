package com.mballem.curso.security.service;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepository repository;
    @Autowired
    private Datatables datatable;

    @Transactional(readOnly = false)
    public void salvar(Especialidade especialidade) {
        repository.save(especialidade);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
        datatable.setRequest(request);
        datatable.setColunas(DatatablesColunas.ESPECIALIDADES);
        Page<?> page = datatable.getSearch().isEmpty()
                ? repository.findAll(datatable.getPageable())
                : repository.findAllByTitulo(datatable.getSearch(), datatable.getPageable());
        return datatable.getResponse(page);
    }

    public Especialidade buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    public void remover(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<String> buscarEspecialidadesPorTermo(String termo) {
        return repository.findEspecialidadeByTermo(termo);
    }

    @Transactional(readOnly = true)
    public Set<Especialidade> buscarPorTitulo(String[] titulos) {
        return repository.findByTitle(titulos);
    }

    @Transactional(readOnly = true)
    public Object buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
        datatable.setRequest(request);
        datatable.setColunas(DatatablesColunas.ESPECIALIDADES);
        Page<Especialidade> page = repository.findSpecialtiesByDoctorId(id, datatable.getPageable());
        return datatable.getResponse(page);
    }

    public Especialidade buscarEspecialidadePorTitulo(String titulo) {
        return repository.findByTitulo(titulo);
    }
}
