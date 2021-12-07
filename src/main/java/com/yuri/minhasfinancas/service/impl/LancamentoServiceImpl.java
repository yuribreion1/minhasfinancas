package com.yuri.minhasfinancas.service.impl;

import com.yuri.minhasfinancas.exception.RegraNegocioException;
import com.yuri.minhasfinancas.model.entity.Lancamento;
import com.yuri.minhasfinancas.model.enums.StatusLancamento;
import com.yuri.minhasfinancas.repository.LancamentoRepository;
import com.yuri.minhasfinancas.service.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository repository;

    public LancamentoServiceImpl(LancamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Lancamento salvar(Lancamento lancamento) {
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId()); // para garantir que só atualize um registro que existe
        validar(lancamento);
        return repository.save(lancamento);
    }

    @Override
    @Transactional
    public void deletar(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public void validar(Lancamento lancamento) {
        if (lancamento.getDescricao().isEmpty() || lancamento.getDescricao() == null) throw new RegraNegocioException("Informe uma regra valida");
        if (lancamento.getMes() < 1 || lancamento.getMes() > 12 || lancamento.getMes() == null) throw new RegraNegocioException("Informe um mês válido");
        if (lancamento.getAno() == null || lancamento.getAno().toString().length() > 4) throw new RegraNegocioException("Informe um ano válido");
        if (lancamento.getUsuario() == null || lancamento.getId() == null) throw new RegraNegocioException("Um usuário é necessário");
        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) throw new RegraNegocioException("Um valor válido precisa ser especificado");
        if (lancamento.getTipo() == null) throw new RegraNegocioException("Informe um tipo válido para lancamento");
    }
}
