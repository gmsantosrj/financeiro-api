package br.com.financeiro.api.v1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.financeiro.api.v1.model.ContaModel;
import br.com.financeiro.api.v1.model.ContaValorTotalPagoModel;
import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.api.v1.model.input.ContaSituacaoInput;
import br.com.financeiro.api.v1.modelmapper.ContaModelMapper;
import br.com.financeiro.domain.exception.ContaNaoEncontradaException;
import br.com.financeiro.domain.filter.ContaFilter;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.repository.ContaRepository;
import br.com.financeiro.domain.service.ContaService;

/**
 * @author: GILMAR
 * @since: 30 de mai. de 2024
 */
@ExtendWith(MockitoExtension.class)
public class ContaControllerTests {
	
	@InjectMocks
	private ContaController contaController;
	
	@Mock
	private ContaModelMapper contaModelMapper;
	
	@Mock
	private ContaRepository contaRepository;
	
	@Mock
	private ContaService contaService;
	
    private ContaInput contaInput;
    private Conta conta;
    private ContaModel contaModel;
    
    @BeforeEach
    void setUp() {
 
		this.contaInput = ContaInput.builder()
				.dataVencimento(LocalDate.of(2024, 5, 24))
				.dataPagamento(LocalDate.of(2024, 5, 25))
				.valor(new BigDecimal("100.00"))
				.descricao("CONTA DE TELEFONE")
				.situacao("Paga").build();

		conta = new Conta();
		conta.setId(1L);
		conta.setDataVencimento(contaInput.getDataVencimento());
		conta.setDataPagamento(contaInput.getDataPagamento());
		conta.setValor(contaInput.getValor());
		conta.setDescricao(contaInput.getDescricao());
		conta.setSituacao(contaInput.getSituacao());

		contaModel = new ContaModel();
		contaModel.setId(conta.getId());
		contaModel.setDataVencimento(conta.getDataVencimento());
		contaModel.setDataPagamento(conta.getDataPagamento());
		contaModel.setValor(conta.getValor());
		contaModel.setDescricao(conta.getDescricao());
		contaModel.setSituacao(conta.getSituacao());

		
	}
	
	@Test
	void Dado_uma_contaInput_Quando_adicionar_Entao_deve_adicionar_uma_conta() {
        when(this.contaModelMapper.toDomainObject(any(ContaInput.class))).thenReturn(this.conta);
        when(this.contaService.salvar(any(Conta.class))).thenReturn(this.conta);
        when(this.contaModelMapper.toModel(any(Conta.class))).thenReturn(this.contaModel);

        ContaModel result = contaController.adicionar(contaInput);

        assertEquals(this.contaModel.getId(), result.getId());
        assertEquals(this.contaModel.getDataVencimento(), result.getDataVencimento());
        assertEquals(this.contaModel.getDataPagamento(), result.getDataPagamento());
        assertEquals(this.contaModel.getValor(), result.getValor());
        assertEquals(this.contaModel.getDescricao(), result.getDescricao());
        assertEquals(this.contaModel.getSituacao(), result.getSituacao());

        verify(this.contaModelMapper).toDomainObject(this.contaInput);
        verify(this.contaService).salvar(this.conta);
        verify(this.contaModelMapper).toModel(this.conta);
	}
	
	@Test
    void Dado_uma_contaId_valido_Quando_buscar_Entao_deve_retornar_uma_ContaModel() {

        when(this.contaService.recuperar(anyLong())).thenReturn(this.conta);
        when(this.contaModelMapper.toModel(conta)).thenReturn(this.contaModel);

        ContaModel result = contaController.buscar(1L);
        
        assertEquals(this.contaModel.getId(), result.getId());
        assertEquals(this.contaModel.getDataVencimento(), result.getDataVencimento());
        assertEquals(this.contaModel.getDataPagamento(), result.getDataPagamento());
        assertEquals(this.contaModel.getValor(), result.getValor());
        assertEquals(this.contaModel.getDescricao(), result.getDescricao());
        assertEquals(this.contaModel.getSituacao(), result.getSituacao());

        verify(this.contaService).recuperar(1L);
        verify(this.contaModelMapper).toModel(conta);
    }
	
	@Test
    void Dado_uma_contaId_invalida_Quando_buscar_Entao_deve_retornar_ContaNaoEncontradaException() {
        when(this.contaService.recuperar(anyLong())).thenThrow(ContaNaoEncontradaException.class);
        assertThrows(ContaNaoEncontradaException.class, ()-> this.contaController.buscar(1589L));
    }
	
	
    @Test
    void Dado_contaFilter_Quando_lista_Entao_deve_retornar_um_Page_ContaModel() {
        ContaFilter contaFilter = new ContaFilter();
        Pageable  pageable = PageRequest.of(0, 10);
        Page<Conta> contasPage = new PageImpl<>(Arrays.asList(conta), pageable, 1);
        List<ContaModel>  contaModelList = Arrays.asList(contaModel);
    	
        when(this.contaRepository.filtrar(any(ContaFilter.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(contasPage);
        when(this.contaModelMapper.toCollectiomModel(any(List.class))).thenReturn(contaModelList);

        Page<ContaModel> result = contaController.listar(contaFilter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(this.contaModel.getId(), result.getContent().get(0).getId());
        assertEquals(this.contaModel.getDataVencimento(), result.getContent().get(0).getDataVencimento());
        assertEquals(this.contaModel.getDataPagamento(), result.getContent().get(0).getDataPagamento());
        assertEquals(this.contaModel.getValor(), result.getContent().get(0).getValor());
        assertEquals(this.contaModel.getDescricao(), result.getContent().get(0).getDescricao());
        assertEquals(this.contaModel.getSituacao(), result.getContent().get(0).getSituacao());
    }
    
    @Test
    void Dado_uma_contaId_valida_Quando_atualizarSituacao_Entao_deve_atualizar_a_situacao_da_conta() {
    	ContaSituacaoInput contaSituacaoInput = new ContaSituacaoInput();
        contaSituacaoInput.setSituacao("Cancelada");
        
        when(this.contaService.recuperar(anyLong())).thenReturn(this.conta);
        this.contaController.atualizarSituacao(1L, contaSituacaoInput);
        
        verify(this.contaService).recuperar(1L);
        verify(this.contaService).salvar(this.conta);
        assertEquals("Cancelada", this.conta.getSituacao());
    }
    
    @Test
    void Dado_uma_contaId_invalido_Quando_atualizarSituacao_Entao_deve_retornar_ContaNaoEncontradaException() {
    	ContaSituacaoInput contaSituacaoInput = new ContaSituacaoInput();
        contaSituacaoInput.setSituacao("Cancelada");
        
        when(this.contaService.recuperar(anyLong())).thenThrow(ContaNaoEncontradaException.class);
        assertThrows(ContaNaoEncontradaException.class, ()-> this.contaController.atualizarSituacao(1L, contaSituacaoInput));
    }
    
    @Test
    void Dado_periodoInicial_e_periodoInicial_Quando_totalValorPago_Entao_deve_retornar_o_total_pago_no_periodo_informado() {
    	BigDecimal totalPago = new BigDecimal("1500.00");
    	 
        when(this.contaService.recuperarValorTotalPagoPorPeriodo(any(LocalDate.class), any(LocalDate.class))).thenReturn(totalPago);
        ContaValorTotalPagoModel result = this.contaController.totalValorPago(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        assertEquals(totalPago, result.getValorTotalPago());
        verify(this.contaService).recuperarValorTotalPagoPorPeriodo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    }
	
	
}
