package br.com.financeiro.api.v1.modelmapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.financeiro.api.v1.model.ContaModel;
import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.domain.model.Conta;

/**
 * @author: GILMAR
 * @since: 24 de mai. de 2024
 */
@Component
public class ContaModelMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Conta toDomainObject (ContaInput contaInput) {
		return this.modelMapper.map(contaInput, Conta.class);
	}
	
	public ContaModel toModel(Conta conta) {
		return this.modelMapper.map(conta, ContaModel.class);
	}
	
	public void copyToDomainObject(ContaInput contaInput, Conta conta) {
		this.modelMapper.map(contaInput, conta);
	}
	
	public List<ContaModel> toCollectiomModel (List<Conta> contas){
		return contas.stream()
				.map(conta -> toModel(conta))
				.collect(Collectors.toList());
	}
	
	public List<Conta> toCollectiomDomainObject (List<ContaInput> contasInput) {
		return contasInput.stream()
				.map(conta -> toDomainObject(conta))
				.collect(Collectors.toList());
	}

}
