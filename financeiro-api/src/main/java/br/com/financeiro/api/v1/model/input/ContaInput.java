package br.com.financeiro.api.v1.model.input;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ContaInput {
	
	@Size(max = 100)
	@NotBlank
	private String descricao;
	
	@PositiveOrZero
	@NotNull
	private BigDecimal valor;
	
	@NotNull
	private LocalDate dataVencimento;
	
	@NotNull
	private LocalDate dataPagamento;
	
	
	@Size(max = 20)
	@NotBlank
	private String situacao;

}
