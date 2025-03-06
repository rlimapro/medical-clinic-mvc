package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query("SELECT m FROM Medico m WHERE m.usuario.id = :id")
    Optional<Medico> findByUsuarioId(Long id);
}
