package br.com.financeiro.api.v1.model.input;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @author: GILMAR
 * @since: 30 de mai. de 2024
 */
public class ContaInputTests {

	private static Validator validator;
	
	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();				
	}
	
	@Test
	public void Nao_Deve_Aceitar_dataVencimento_Com_Valor_Null() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDataVencimento(null);
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "dataVencimento".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Deve_Aceitar_dataVencimento_Com_Valor_Valido() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDataVencimento(LocalDate.of(2024, 05, 15));
		var violations = validator.validate(contaInput);
		assertFalse(violations.stream()
				.anyMatch(violation -> "dataVencimento".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_dataPagamento_Com_Valor_Null() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDataPagamento(null);
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "dataPagamento".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Deve_Aceitar_datadataPagamento_Com_Valor_Valido() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDataPagamento(LocalDate.of(2024, 05, 15));
		var violations = validator.validate(contaInput);
		assertFalse(violations.stream()
				.anyMatch(violation -> "dataPagamento".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_valor_Null() {
		ContaInput contaInput = new ContaInput();
		contaInput.setValor(null);
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "valor".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_valor_Negativo() {
		ContaInput contaInput = new ContaInput();
		contaInput.setValor(new BigDecimal("-100"));
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "valor".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Deve_Aceitar_valor_Valido() {
		ContaInput contaInput = new ContaInput();
		contaInput.setValor(new BigDecimal("150"));
		var violations = validator.validate(contaInput);
		assertFalse(violations.stream()
				.anyMatch(violation -> "valor".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_descricao_Com_Valor_Null() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDescricao(null);
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "descricao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_descricao_Com_Valor_Vazio() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDescricao("");
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "descricao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	void Nao_Deve_Aceitar_descricao_Acima_De_100_Caracteres() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDescricao("PAGAMENTO DE CONTA PAGAMENTO DE CONTA PAGAMENTO DE CONTA PAGAMENTO DE CONTA PAGAMENTO DE CONTA PAGAME");
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "descricao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Deve_Aceitar_descricao_Com_Valor_Valido() {
		ContaInput contaInput = new ContaInput();
		contaInput.setDescricao("PAGAMENTO DE CONTA");
		var violations = validator.validate(contaInput);
		assertFalse(violations.stream()
				.anyMatch(violation -> "descricao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_situacao_Com_Valor_Null() {
		ContaInput contaInput = new ContaInput();
		contaInput.setSituacao(null);
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "situacao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	public void Nao_Deve_Aceitar_situacao_Com_Valor_Vazio() {
		ContaInput contaInput = new ContaInput();
		contaInput.setSituacao("");
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "situacao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	void Nao_Deve_Aceitar_situacao_Acima_De_20_Caracteres() {
		ContaInput contaInput = new ContaInput();
		contaInput.setSituacao("PAGO PAGO PAGO PAGOWQ");
		var violations = validator.validate(contaInput);
		assertTrue(violations.stream()
				.anyMatch(violation -> "situacao".equals(violation.getPropertyPath().toString())));
	}
	
	@Test
	void Deve_Aceitar_situacao_Com_Valor_Valido() {
		ContaInput contaInput = new ContaInput();
		contaInput.setSituacao("PAGO");
		var violations = validator.validate(contaInput);
		assertFalse(violations.stream()
				.anyMatch(violation -> "situacao".equals(violation.getPropertyPath().toString())));
	}

}
