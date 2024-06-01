package br.com.financeiro.domain.exception;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
public class EntidadeNaoEncontradaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}


}
