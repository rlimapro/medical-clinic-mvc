package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query(
           "SELECT h FROM Horario h " +
           "WHERE NOT EXISTS (" +
                "SELECT a.horario.id " +
                "FROM Agendamento a " +
                "WHERE a.medico.id = :id " +
                "AND a.dataConsulta = :data " +
                "AND a.horario.id = h.id" +
           ") " +
           "ORDER BY h.horaMinuto ASC"
    )
    List<Horario> findMedicoAndDataByHorarioDisponivel(Long id, LocalDate data);
}
