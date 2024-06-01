package br.com.financeiro.api.v1.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: GILMAR
 * @since: 30 de mai. de 2024
 */
@AllArgsConstructor
@Setter
@Getter
public class ContaValorTotalPagoModel {
	
	private BigDecimal valorTotalPago;

}
