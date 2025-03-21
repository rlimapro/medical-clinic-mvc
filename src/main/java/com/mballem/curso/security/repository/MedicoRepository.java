package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query("SELECT m FROM Medico m WHERE m.usuario.id = :id")
    Optional<Medico> findByUsuarioId(Long id);

    @Query("SELECT m FROM Medico m WHERE m.usuario.email LIKE :email")
    Optional<Medico> findByEmail(String email);

    @Query("SELECT m FROM Medico m " +
            "JOIN m.especialidades e " +
            "WHERE e.titulo LIKE :titulo " +
            "AND m.usuario.ativo = true")
    List<Medico> findMedicosByEspecialidades(String titulo);

    @Query("SELECT m.id FROM Medico m " +
            "JOIN m.especialidades e " +
            "JOIN m.agendamentos as a " +
            "WHERE a.medico.id = :idMed " +
            "AND a.especialidade.id = :idEsp")
    Optional<Long> hasEspecialidadeAgendadada(Long idEsp, Long idMed);
}
