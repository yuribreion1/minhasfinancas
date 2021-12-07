package com.yuri.minhasfinancas.controller;

import com.yuri.minhasfinancas.dto.LancamentoDTO;
import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Lancamento;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.model.enums.StatusLancamento;
import com.yuri.minhasfinancas.model.enums.TipoLancamento;
import com.yuri.minhasfinancas.service.LancamentoService;
import com.yuri.minhasfinancas.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private LancamentoService service;

    private UsuarioService usuarioService;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO lancamentoDTO) {
        Lancamento lancamento = converterParaDTO(lancamentoDTO);
        try {
            Lancamento lancamentoSalvo = service.salvar(lancamento);
            return ResponseEntity.ok(lancamentoSalvo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Lancamento converterParaDTO(LancamentoDTO dto) {
        Usuario usuario = usuarioService.pegarUsuarioPorId(dto.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));
        return Lancamento.builder()
                .descricao(dto.getDescricao())
                .id(dto.getId())
                .ano(dto.getAno())
                .mes(dto.getMes())
                .status(StatusLancamento.valueOf(dto.getStatus()))
                .tipo(TipoLancamento.valueOf(dto.getTipo()))
                .usuario(usuario)
                .valor(dto.getValor())
                .build();
    }
}
