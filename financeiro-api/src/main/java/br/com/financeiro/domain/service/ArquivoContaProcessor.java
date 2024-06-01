package br.com.financeiro.domain.service;

import java.io.InputStream;
import java.util.List;

import br.com.financeiro.api.v1.model.input.ContaInput;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public interface ArquivoContaProcessor {
	
	List<ContaInput> processar(InputStream inputStream);

}
