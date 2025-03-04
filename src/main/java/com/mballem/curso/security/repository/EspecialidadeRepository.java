package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
    @Query("SELECT e from Especialidade e WHERE e.titulo LIKE :search%")
    Page<Especialidade> findAllByTitulo(String search, Pageable pageable);
}
