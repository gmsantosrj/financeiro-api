package br.com.financeiro.infrastructure.repository.paginacao;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Component
public class PaginacaoUtil {
	
	public void preparar(TypedQuery<?> typedQuery, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistroPorPagina;
		
		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalRegistroPorPagina);
	}
}
