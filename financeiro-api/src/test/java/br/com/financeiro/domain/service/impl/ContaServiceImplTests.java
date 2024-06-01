package br.com.financeiro.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.financeiro.domain.exception.ContaNaoEncontradaException;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.repository.ContaRepository;

/**
 * @author: GILMAR
 * @since: 30 de mai. de 2024
 */
@ExtendWith(MockitoExtension.class)
public class ContaServiceImplTests {

	@Mock
	private ContaRepository contaRepository;

	@InjectMocks
	private ContaServiceImpl contaService;

	private Conta conta;

	@BeforeEach
	public void setUp() {
		conta = new Conta();
		conta.setId(1L);
	}

	@Test
	void Dado_uma_conta_Quando_executar_o_metodo_salvar_Entao_deve_salvar_uma_conta_com_sucesso() {
		when(this.contaRepository.save(any(Conta.class))).thenReturn(this.conta);
		Conta result = this.contaService.salvar(this.conta);

		assertNotNull(result);
		assertEquals(this.conta.getId(), result.getId());
		verify(this.contaRepository, Mockito.times(1)).save(this.conta);
	}
	
	@Test
	void Dado_uma_lista_de_contas_Quando_executar_o_metodo_salvar_Entao_deve_salvar_uma_lista_de_conta_com_sucesso() {
		List<Conta> contas = Arrays.asList(this.conta, new Conta());
		this.contaService.salvar(contas);
		verify(this.contaRepository, Mockito.times(contas.size())).save(any(Conta.class));
	}
	
	@Test
	void Dado_uma_contaId_valida_Quando_recuperar_Entao_deve_retornar_uma_conta() {
		when(this.contaRepository.findById(anyLong())).thenReturn(Optional.of(this.conta));
		Conta result = contaService.recuperar(1L);

		assertNotNull(result);
		assertEquals(this.conta.getId(), result.getId());
		verify(this.contaRepository, Mockito.times(1)).findById(1L);
	}
	
	@Test
	void Dado_uma_contaId_invalida_Quando_recuperar_Entao_deve_retornar_ContaNaoEncontradaException() {
		when(this.contaRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ContaNaoEncontradaException.class, ()-> contaService.recuperar(anyLong()));
	}
	
    @Test
    void Dado_periodoInicial_e_periodoInicial_Quando_recuperarValorTotalPagoPorPeriodo_Entao_deve_retornar_o_total_pago_no_periodo_informado() {
        LocalDate periodoInicial = LocalDate.of(2023, 1, 1);
        LocalDate periodoFinal = LocalDate.of(2023, 12, 31);
        BigDecimal valorTotalEsperado = BigDecimal.valueOf(1000);

        when(this.contaRepository.findTotalValorPagoByPeriodo(periodoInicial, periodoFinal)).thenReturn(valorTotalEsperado);
        BigDecimal result = contaService.recuperarValorTotalPagoPorPeriodo(periodoInicial, periodoFinal);

        assertNotNull(result);
        assertEquals(valorTotalEsperado, result);
        verify(contaRepository, Mockito.times(1)).findTotalValorPagoByPeriodo(periodoInicial, periodoFinal);
    }
    
    @Test
    void Dado_periodoInicial_e_periodoInicial_que_nao_existe_Quando_recuperarValorTotalPagoPorPeriodo_Entao_deve_retornar_ZERO() {
        LocalDate periodoInicial = LocalDate.of(2023, 1, 1);
        LocalDate periodoFinal = LocalDate.of(2023, 12, 31);

        when(this.contaRepository.findTotalValorPagoByPeriodo(periodoInicial, periodoFinal)).thenReturn(null);
        BigDecimal result = contaService.recuperarValorTotalPagoPorPeriodo(periodoInicial, periodoFinal);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result);
        verify(contaRepository, Mockito.times(1)).findTotalValorPagoByPeriodo(periodoInicial, periodoFinal);
    }

}
