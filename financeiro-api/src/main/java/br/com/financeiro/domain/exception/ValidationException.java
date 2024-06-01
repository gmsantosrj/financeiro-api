package br.com.financeiro.domain.exception;

import java.util.List;

import br.com.financeiro.domain.exception.bean.ValidationError;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private List<ValidationError> erros;
	
	public ValidationException(List<ValidationError> erros) {
		this.erros = erros;
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public List<ValidationError> getErros(){
		return this.erros;
	}

}
