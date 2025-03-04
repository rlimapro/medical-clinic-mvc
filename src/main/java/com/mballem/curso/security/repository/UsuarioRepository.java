package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.websocket.server.PathParam;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.email LIKE :email")
    Usuario findByEmail(@PathParam("email") String email);

    @Query("SELECT DISTINCT u FROM Usuario u " +
            "JOIN u.perfis p ON p.id = u.id " +
            "WHERE u.email LIKE :search% OR p.desc LIKE :search%")
    Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);
}
