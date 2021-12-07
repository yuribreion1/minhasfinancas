package com.yuri.minhasfinancas.service;

import com.yuri.minhasfinancas.model.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    // Business rule 1 - Authenticate user
    Usuario autenticar(String email, String senha);

    // Business rule 2 - Add new user
    Usuario salvarUsuario(Usuario usuario);

    // Business rule 3 - Validate e-mail
    void validarEmail(String email);

    Optional<Usuario> pegarUsuarioPorId(Long id);
}
