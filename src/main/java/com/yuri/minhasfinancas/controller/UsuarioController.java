package com.yuri.minhasfinancas.controller;

import com.yuri.minhasfinancas.dto.UsuarioDTO;
import com.yuri.minhasfinancas.exception.AutenticacaoException;
import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.service.LancamentoService;
import com.yuri.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Slf4j
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final LancamentoService lancamentoService;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (AutenticacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/salvar")
    public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}/saldo")
    public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.pegarUsuarioPorId(id);

        if (!usuario.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }

}
