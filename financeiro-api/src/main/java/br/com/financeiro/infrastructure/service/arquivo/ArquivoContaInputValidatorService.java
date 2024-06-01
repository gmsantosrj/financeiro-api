package br.com.financeiro.infrastructure.service.arquivo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.domain.exception.bean.ValidationError;
import br.com.financeiro.domain.service.ArquivoContaInputValidator;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Service
public class ArquivoContaInputValidatorService implements ArquivoContaInputValidator {

	@Autowired
	private Validator validator;

	
	private List<ValidationError> validacoes;
	
	@Override
	public void validarContasInput(List<ContaInput> contasInput) {
		
		contasInput.forEach(input -> {
			limparValidacoes();
			validarContaInput(input);
		});
		
		processarValidacoes();
	}

	 void validarContaInput(ContaInput input) {
		validarDescricao(input);
		validarValor(input) ;
		validarSituacao(input);
		validarDataPagamento(input);
		validarDataVencimento(input); 
	}

	private void validarDescricao(ContaInput conta) {
		try {
			validarCampos(conta, "descricao");
		} catch (ValidationException e) {
			addValidationError("descricao", e.getMessage());
		}
	}

	private void validarSituacao(ContaInput conta) {
		try {
			validarCampos(conta, "situacao");
		} catch (ValidationException e) {
			addValidationError("situacao", e.getMessage());
		}
	}

	private void validarDataVencimento(ContaInput conta) {
		try {
			validarCampos(conta, "dataVencimento");
		} catch (ValidationException e) {
			addValidationError("dataVencimento", e.getMessage());
		}
	}

	private void validarDataPagamento(ContaInput conta) {
		try {
			validarCampos(conta, "dataPagamento");
		} catch (ValidationException e) {
			addValidationError("dataPagamento", e.getMessage());
		}
	}

	private void validarValor(ContaInput conta) {
		try {
			validarCampos(conta, "valor");

		} catch (ValidationException e) {
			addValidationError("Valor", e.getMessage());
		}
	}

	private void validarCampos(ContaInput conta, String campo) {
		var violations = this.validator.validateProperty(conta, campo);

		if (!violations.isEmpty()) {
			throw new ValidationException(violations.iterator().next().getMessage());
		}
	}

	private void addValidationError(String campo, String mensagemErro) {
		this.validacoes.add(new ValidationError(campo, mensagemErro));
	}
	
	private void limparValidacoes() {
		this.validacoes = new ArrayList<>();
	}
	
	private void processarValidacoes() {
		if (!this.validacoes.isEmpty()) {
			throw new br.com.financeiro.domain.exception.ValidationException(validacoes);
		}
	}
	

}
