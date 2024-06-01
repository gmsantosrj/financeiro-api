package br.com.financeiro.domain.service;

import java.util.List;

import br.com.financeiro.api.v1.model.input.ContaInput;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public interface ArquivoContaInputValidator {
	
	void validarContasInput(List<ContaInput> contasInput);

}
