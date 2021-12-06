package com.yuri.minhasfinancas.service;

import com.yuri.minhasfinancas.exception.AutenticacaoException;
import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.repository.UsuarioRepository;
import com.yuri.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UsuarioServiceTest {

    @MockBean
    UsuarioRepository repository;

    @SpyBean
    UsuarioServiceImpl service;

    @Test
    void naoSalvarUsuarioJaCadastrado() {
        // scenario
        String email = "usuario@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        // verification
        assertThrows(RegraNegocioException.class, () -> {
            service.salvarUsuario(usuario);
        });

    }

    @Test
    @DisplayName("Adicionando usuário no banco")
    void salvarUsuario() {
        // scenario
        doNothing().when(service).validarEmail(anyString());
        Usuario usuario = Usuario.builder().nome("Usuario").senha("senha").email("usuario@email.com").id(1L).build();
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // action
        Usuario resultado = service.salvarUsuario(new Usuario());

        // verificacao
        assertNotNull(resultado);

    }

    @Test
    @DisplayName("Testando autenticação com sucesso")
    void autenticacaoComSucesso() {
        // scenario
        String email = "email@email.com";
        String senha = "senha";
        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // action
        Usuario resultado = service.autenticar(email, senha);

        //verification
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Testando erro de autenticação sem usuário")
    void erroDeAutenticacaoSemUsuario() {
        // scenario
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        // action and verification
        assertThrows(AutenticacaoException.class, () -> {
            service.autenticar("email@email.com", "senha");
        });
    }

    @Test
    @DisplayName("Testando erro de autenticação por senha errada")
    void erroDeUsuarioComSenhaErrada() {
        // scenario
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // action
        // verification
        Exception exception = assertThrows(AutenticacaoException.class, () -> {
            service.autenticar("email@email.com", "1234");
        });
        assertEquals("Senha invalida para o usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Validar e-mail")
    void validarEmail() {
        // scenario
        when(repository.existsByEmail(anyString())).thenReturn(false);

        //action
        service.validarEmail("email@email.com");
    }

    @Test
    @DisplayName("Acionar exceção")
    void validarRegraDeNegocioException() {
        when(repository.existsByEmail(anyString())).thenReturn(true);
        //scenario
        Usuario usuario = Usuario.builder()
                .email("email@email.com")
                .nome("Usuario")
                .build();

        repository.save(usuario);

        assertThrows(RegraNegocioException.class, () -> {
            service.validarEmail("email@email.com");
        });
    }
}
