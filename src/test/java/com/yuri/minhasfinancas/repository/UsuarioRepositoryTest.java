package com.yuri.minhasfinancas.repository;

import com.yuri.minhasfinancas.model.entity.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static Usuario criarUsuario() {
        return Usuario.builder().nome("Usuario").email("usuario@email.com").build();
    }

    @Test
    @DisplayName("Verifica se o e-mail existe")
    void verificaSeEmailExiste() {
        entityManager.persist(criarUsuario());
        //action - execution
        Boolean resultado = repository.existsByEmail("usuario@email.com");

        //verification - Checking if the user exists
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Verifica se o e-mail não existe")
    void verificaSeEmailNaoExiste() {

        //action - execution
        Boolean resultado = repository.existsByEmail("usuario@email.com");

        //verification
        assertThat(resultado).isFalse();
    }

    @Test
    void adicionarUsuario() {
        Usuario usuarioSalvo = repository.save(criarUsuario());
        assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    void procurarUsuarioPorEmail() {
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        Optional<Usuario> resultado = repository.findByEmail("usuario@email.com");
        assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    void procurarUsuarioNaoExistente() {

        Optional<Usuario> resultado = repository.findByEmail("usuario@email.com");
        assertThat(resultado.isPresent()).isFalse();
    }
}
