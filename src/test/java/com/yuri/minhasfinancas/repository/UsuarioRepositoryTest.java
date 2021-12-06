package com.yuri.minhasfinancas.repository;

import com.yuri.minhasfinancas.model.entity.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    @DisplayName("Verifica se o e-mail existe")
    void verificaSeEmailExiste() {
        // scenario
        Usuario usuario = Usuario.builder()
                .nome("Nome do usuario")
                .email("usuario@email.com")
                .build();
        repository.save(usuario);
        //action - execution
        Boolean resultado = repository.existsByEmail("usuario@email.com");

        //verification - Checking if the user exists
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Verifica se o e-mail não existe")
    void verificaSeEmailNaoExiste() {
        //scenario - cleaning the database
        repository.deleteAll();
        //action - execution
        Boolean resultado = repository.existsByEmail("usuario@email.com");

        //verification
        assertThat(resultado).isFalse();
    }
}
