package br.com.financeiro.api.v1.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.financeiro.api.v1.model.ContaModel;
import br.com.financeiro.api.v1.model.ContaValorTotalPagoModel;
import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.api.v1.model.input.ContaSituacaoInput;
import br.com.financeiro.api.v1.modelmapper.ContaModelMapper;
import br.com.financeiro.domain.filter.ContaFilter;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.repository.ContaRepository;
import br.com.financeiro.domain.service.ContaService;
import jakarta.validation.Valid;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@RestController
@RequestMapping(value = "/v1/contas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContaController {
	
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private ContaModelMapper contaModelMapper;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ContaModel adicionar(@RequestBody @Valid ContaInput contaInput) {
		Conta conta = this.contaModelMapper.toDomainObject(contaInput);
		conta = this.contaService.salvar(conta);
		return this.contaModelMapper.toModel(conta);
	}
	
	@GetMapping("/{contaId}")
	public ContaModel buscar(@PathVariable Long contaId) {
		Conta conta = this.contaService.recuperar(contaId);
		return contaModelMapper.toModel(conta);
	}
	
	@GetMapping
	public Page<ContaModel> listar(ContaFilter contaFilter, Pageable pageable) {
		Page<Conta> contasPage = this.contaRepository.filtrar(contaFilter, pageable);
		List<ContaModel> contasModel = contaModelMapper.toCollectiomModel(contasPage.getContent());
		Page<ContaModel> contasModelPage = new PageImpl<>(contasModel, pageable, contasPage.getTotalElements());
		return contasModelPage;
	}
	
	@PutMapping("/{contaId}")
	private ContaModel atualizar(@PathVariable Long contaId, @RequestBody @Valid ContaInput contaInput ) {
		Conta contaAtual = this.contaService.recuperar(contaId);
		this.contaModelMapper.copyToDomainObject(contaInput, contaAtual);
		contaAtual = this.contaService.salvar(contaAtual);
		return this.contaModelMapper.toModel(contaAtual);
	}
	
	@PutMapping("/{contaId}/situacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarSituacao(@PathVariable Long contaId, @RequestBody @Valid ContaSituacaoInput contaSituacaoInput) {
		Conta contaAtual = this.contaService.recuperar(contaId);
		contaAtual.setSituacao(contaSituacaoInput.getSituacao());
		this.contaService.salvar(contaAtual);
	}
	
	@GetMapping("/valor-total-pago")
	public ContaValorTotalPagoModel totalValorPago(@RequestParam LocalDate periodoInicial, @RequestParam LocalDate periodoFinal) {
		BigDecimal totalPago = this.contaService.recuperarValorTotalPagoPorPeriodo(periodoInicial, periodoFinal);
		return new ContaValorTotalPagoModel(totalPago) ;
	}

}
