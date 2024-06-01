package br.com.financeiro.api.v1.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@Setter
@Getter
public class ContaModel {
	
	private Long id;
	private LocalDate dataVencimento;
	private LocalDate dataPagamento;
	private BigDecimal valor;
	private String descricao;
	private String situacao;

}
