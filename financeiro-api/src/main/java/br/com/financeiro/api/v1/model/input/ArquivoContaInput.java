package br.com.financeiro.api.v1.model.input;

import org.springframework.web.multipart.MultipartFile;

import br.com.financeiro.core.validation.FileContentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Setter
@Getter
public class ArquivoContaInput {
	
	@NotNull
	@FileContentType(allowed = {"xls", "xlsx"})
	private MultipartFile arquivo;
	
}
