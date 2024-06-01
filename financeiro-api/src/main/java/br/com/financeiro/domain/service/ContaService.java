package br.com.financeiro.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.financeiro.domain.model.Conta;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
public interface ContaService {
	
	Conta salvar(Conta conta);
	void salvar(List<Conta> contas);
	Conta recuperar(Long contaId);
	BigDecimal recuperarValorTotalPagoPorPeriodo(LocalDate periodoInicial, LocalDate periodoFinal);

}
