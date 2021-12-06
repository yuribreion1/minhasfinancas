package com.yuri.minhasfinancas.repository;

import com.yuri.minhasfinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Just return if the e-mail exists
    Boolean existsByEmail(String email);
}
