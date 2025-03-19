package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.repository.projection.HistorioPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT a.id AS id, " +
            "a.paciente AS paciente, " +
            "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) AS dataConsulta, " +
            "a.medico AS medico, " +
            "a.especialidade AS especialidade " +
            "FROM Agendamento a " +
            "WHERE a.paciente.usuario.email LIKE :email")
    Page<HistorioPaciente> findByHistoricoPacienteEmail(String email, Pageable pageable);

    @Query("SELECT a.id AS id, " +
            "a.paciente AS paciente, " +
            "CONCAT(a.dataConsulta, ' ', a.horario.horaMinuto) AS dataConsulta, " +
            "a.medico AS medico, " +
            "a.especialidade AS especialidade " +
            "FROM Agendamento a " +
            "WHERE a.medico.usuario.email LIKE :email")
    Page<HistorioPaciente> findByHistoricoMedicoEmail(String email, Pageable pageable);
}
