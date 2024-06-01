package br.com.financeiro.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Setter
@Getter
public class ContaSituacaoInput {
	
	@NotBlank
	private String situacao; 

}
