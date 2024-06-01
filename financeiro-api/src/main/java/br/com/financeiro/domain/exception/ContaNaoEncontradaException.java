package br.com.financeiro.domain.exception;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
public class ContaNaoEncontradaException extends EntidadeNaoEncontradaException {
	

	private static final long serialVersionUID = 1L;

	public ContaNaoEncontradaException(String message) {
		super(message);
	}
	
	public ContaNaoEncontradaException(Long contaId) {
		this(String.format("Não existe um cadastro de conta com código %d", contaId));
	}

}
