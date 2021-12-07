package com.yuri.minhasfinancas.controller;

import com.yuri.minhasfinancas.dto.AtualizarStatusDTO;
import com.yuri.minhasfinancas.dto.LancamentoDTO;
import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Lancamento;
import com.yuri.minhasfinancas.model.entity.Usuario;
import com.yuri.minhasfinancas.model.enums.StatusLancamento;
import com.yuri.minhasfinancas.model.enums.TipoLancamento;
import com.yuri.minhasfinancas.service.LancamentoService;
import com.yuri.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService service;

    private final UsuarioService usuarioService;


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

    @PutMapping("{id}")
    public ResponseEntity atualizar(@RequestBody LancamentoDTO lancamentoDTO, @PathVariable("id") Long id) {
        return service.pegarLancamentoPorId(id).map(entity -> {
            try {
                Lancamento lancamento = converterParaDTO(lancamentoDTO);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusDTO atualizarStatusDTO) {
        return service.pegarLancamentoPorId(id).map(entity -> {
          StatusLancamento statusEscolhido = StatusLancamento.valueOf(atualizarStatusDTO.getStatus());
          if (statusEscolhido == null) return ResponseEntity.badRequest().body("Informe um status valido");
          try {
              entity.setStatus(statusEscolhido);
              service.atualizar(entity);
              return ResponseEntity.ok(entity);
          } catch (RegraNegocioException e) {
              return ResponseEntity.badRequest().body(e.getMessage());
          }
        }).orElseGet(() -> new ResponseEntity("Lancamento não encontrado", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id) {
        return service.pegarLancamentoPorId(id).map( entidade -> {
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario) {

        Optional<Usuario> usuario = usuarioService.pegarUsuarioPorId(idUsuario);

        Lancamento lancamento = Lancamento.builder()
                .descricao(descricao)
                .mes(mes)
                .ano(ano)
                .build();

        if (!usuario.isPresent()) return ResponseEntity.badRequest().body("Usuário não encontrado para o ID informado");
        else lancamento.setUsuario(usuario.get());

        List<Lancamento> lancamentos = service.buscar(lancamento);
        return ResponseEntity.ok(lancamentos);
    }

    private Lancamento converterParaDTO(LancamentoDTO lancamentoDTO) {
        Usuario usuario = usuarioService.pegarUsuarioPorId(lancamentoDTO.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));
        Lancamento lancamento = new Lancamento();

        if (lancamentoDTO.getTipo() != null) lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
        if (lancamentoDTO.getStatus() != null) lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));

        lancamento.setId(lancamentoDTO.getId());
        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setMes(lancamentoDTO.getMes());
        lancamento.setAno(lancamentoDTO.getAno());
        lancamento.setUsuario(usuario);
        lancamento.setValor(lancamentoDTO.getValor());

        return lancamento;
    }
}
