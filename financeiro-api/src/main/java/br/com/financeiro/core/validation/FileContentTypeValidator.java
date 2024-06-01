package br.com.financeiro.core.validation;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private List<String> contentTypesPermitidos;

	@Override
	public void initialize(FileContentType constraint) {
		 this.contentTypesPermitidos = Arrays.asList(constraint.allowed());
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

		if (value == null || value.isEmpty()) {
			return true;
		}

		String originalFilename = value.getOriginalFilename();
		
		if (!originalFilename.isEmpty()) {
			String lowerCaseFilename = originalFilename.toLowerCase();
			return contentTypesPermitidos.stream().anyMatch(lowerCaseFilename::endsWith);
		}

		return false;
	}

}
