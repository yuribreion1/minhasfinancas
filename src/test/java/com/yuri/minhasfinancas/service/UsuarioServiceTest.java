package com.yuri.minhasfinancas.service;

import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UsuarioServiceTest {

    @Autowired
    UsuarioService service;

    @Autowired
    UsuarioRepository repository;

    @Test
    @DisplayName("Validar e-mail")
    void validarEmail() {
        //scenario
        repository.deleteAll();

        //action
        service.validarEmail("email@email.com");
    }

    @Test
    @DisplayName("Acionar exceção")
    void validarRegraDeNegocioException() {
        assertThrows(RegraNegocioException.class, () -> {
            //scenario
            Usuario usuario = Usuario.builder()
                .email("email@email.com")
                .nome("Usuario")
                .build();

            repository.save(usuario);

            service.validarEmail("email@email.com");
        });
    }
}
