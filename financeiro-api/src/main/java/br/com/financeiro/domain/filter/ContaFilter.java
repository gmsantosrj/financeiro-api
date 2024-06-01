package br.com.financeiro.domain.filter;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Setter
@Getter
public class ContaFilter {
	
	private LocalDate dataVencimento;
	private String descricao;

}
