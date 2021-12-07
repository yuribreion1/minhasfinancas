package com.yuri.minhasfinancas.service;

import com.yuri.minhasfinancas.model.entity.Lancamento;
import com.yuri.minhasfinancas.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamento);

    Lancamento atualizar(Lancamento lancamento);

    void deletar(Lancamento lancamento);

    void atualizarStatus(Lancamento lancamento, StatusLancamento status);

    List<Lancamento> buscar(Lancamento lancamentoFiltro);

    void validar(Lancamento lancamento);

    Optional<Lancamento> pegarLancamentoPorId(Long id);
}
