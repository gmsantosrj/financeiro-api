package br.com.financeiro.domain.exception.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */

@AllArgsConstructor
@Setter
@Getter
public class ValidationError {
	
	private String field;
    private String message;

}
