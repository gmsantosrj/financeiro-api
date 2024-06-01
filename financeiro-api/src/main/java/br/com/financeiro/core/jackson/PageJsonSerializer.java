
package br.com.financeiro.core.jackson;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {
	
	@Override
	public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		
		gen.writeObjectField("content", page.getContent());
		gen.writeNumberField("qtdDeRegistrosPorPagina", page.getSize()); 
		gen.writeNumberField("totalDeRegistros", page.getTotalElements());
		gen.writeNumberField("totalPaginas", page.getTotalPages()); 
		gen.writeNumberField("paginaAtual", page.getNumber()); 
		gen.writeBooleanField("empty", page.isEmpty()); 
		gen.writeBooleanField("primeiraPagina", page.isFirst()); 
		gen.writeBooleanField("ultimaPagina", page.isLast()); 	
		
		gen.writeEndObject();
	}
	



}
