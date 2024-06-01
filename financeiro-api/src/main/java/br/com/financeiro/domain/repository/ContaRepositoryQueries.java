package br.com.financeiro.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.financeiro.domain.filter.ContaFilter;
import br.com.financeiro.domain.model.Conta;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public interface ContaRepositoryQueries {
	
	public Page<Conta> filtrar (ContaFilter filter, Pageable pageable);

}
