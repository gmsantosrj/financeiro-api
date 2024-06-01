package br.com.financeiro.domain.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.financeiro.domain.exception.ContaNaoEncontradaException;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.repository.ContaRepository;
import br.com.financeiro.domain.service.ContaService;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@Service
public class ContaServiceImpl implements ContaService {
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Transactional
	@Override
	public Conta salvar(Conta conta) {
		return contaRepository.save(conta);
	}
	
	@Transactional
	public void salvar(List<Conta> contas) {
		contas.forEach(this::salvar);
	}
	
	@Override
	public Conta recuperar(Long contaId) {
		return this.contaRepository.findById(contaId)
				.orElseThrow(()-> new ContaNaoEncontradaException(contaId));
	}
	
	@Override
	public BigDecimal recuperarValorTotalPagoPorPeriodo(LocalDate periodoInicial, LocalDate periodoFinal) {
		BigDecimal valorTotal = this.contaRepository.findTotalValorPagoByPeriodo(periodoInicial, periodoFinal);
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }
	
	


}
