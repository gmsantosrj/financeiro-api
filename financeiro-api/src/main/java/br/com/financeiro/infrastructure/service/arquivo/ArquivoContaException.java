package br.com.financeiro.infrastructure.service.arquivo;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public class ArquivoContaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ArquivoContaException(String mensagem) {
		super(mensagem);
	}
	
	public ArquivoContaException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
