package com.yuri.minhasfinancas.service.impl;

import com.yuri.minhasfinancas.exception.AutenticacaoException;
import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.repository.UsuarioRepository;
import com.yuri.minhasfinancas.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if (usuario.isEmpty()) throw new AutenticacaoException("Usuário não encontrado");

        if (!usuario.get().getSenha().equals(senha)) throw new AutenticacaoException("Senha invalida para o usuário");

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        Boolean existe = repository.existsByEmail(email);
        if (Boolean.TRUE.equals(existe)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail");
        }
    }

    @Override
    public Optional<Usuario> pegarUsuarioPorId(Long id) {
        return repository.findById(id);
    }
}
