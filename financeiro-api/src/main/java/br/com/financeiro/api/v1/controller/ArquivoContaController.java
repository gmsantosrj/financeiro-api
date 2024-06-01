package br.com.financeiro.api.v1.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.financeiro.api.v1.model.input.ArquivoContaInput;
import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.api.v1.modelmapper.ContaModelMapper;
import br.com.financeiro.domain.model.Conta;
import br.com.financeiro.domain.service.ArquivoContaInputValidator;
import br.com.financeiro.domain.service.ArquivoContaProcessor;
import br.com.financeiro.domain.service.ContaService;
import jakarta.validation.Valid;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@RestController
@RequestMapping(path = "/v1/arquivo-contas")
public class ArquivoContaController {
	
	@Autowired
	private ArquivoContaProcessor arquivoContaProcessor;
	
	@Autowired
	private ArquivoContaInputValidator arquivoContaInputValidator;
	
	@Autowired
	private ContaModelMapper contaModelMapper;
	
	@Autowired
	private ContaService contaService;
	
	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void upload(@Valid ArquivoContaInput arquivoContaInput) throws IOException {
		MultipartFile arquivo = arquivoContaInput.getArquivo();
		List<ContaInput> contasInput = this.arquivoContaProcessor.processar(arquivo.getInputStream());

		this.arquivoContaInputValidator.validarContasInput(contasInput);
		List<Conta> contas = this.contaModelMapper.toCollectiomDomainObject(contasInput);
		
		this.contaService.salvar(contas); 
	}
	
	@GetMapping(path = "/download-modelo")
	public ResponseEntity<byte[]> downloadModeloArquivo() throws IOException {
		ClassPathResource resource = new ClassPathResource("arquivo/modelo-contas.xlsx");
		byte[] fileContent;

		try (InputStream inputStream = resource.getInputStream()) {
			fileContent = inputStream.readAllBytes();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDispositionFormData("attachment", "modelo-contas.xlsx");
		return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
	}

}
