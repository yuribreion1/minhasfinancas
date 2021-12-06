package com.yuri.minhasfinancas.service;

import com.yuri.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

    // Business rule 1 - Authenticate user
    Usuario autenticar(String email, String senha);

    // Business rule 2 - Add new user
    Usuario salvarUsuario(Usuario AUsuario);

    // Business rule 3 - Validate e-mail
    void validarEmail(String email);
}
