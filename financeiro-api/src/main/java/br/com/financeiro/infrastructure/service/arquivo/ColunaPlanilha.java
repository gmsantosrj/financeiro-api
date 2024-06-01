package br.com.financeiro.infrastructure.service.arquivo;

import java.util.Arrays;

import lombok.Getter;


/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Getter
public enum ColunaPlanilha {
	
	DESCRICAO(0, "TIPO DESCRICAO", "ALFABÉTICO"),
	VALOR(1, "VALOR", "NUMÉRICO"),
	DATA_PAGAMENTO(2, "DATA DE PAGAMENTO", "DATA"),
	DATA_VENCIMENTO(3, "DATA DE VENCIMENTO", "DATA"),
	SITUACAO(4, "SITUACAO", "ALFABÉTICO");
	
	private int coluna;
	private String descricao;
	private String tipo;

	private ColunaPlanilha(int coluna, String descricao, String tipo) {
		this.coluna = coluna;
		this.descricao = descricao;
		this.tipo = tipo;

	}
	
	public static ColunaPlanilha getColuna(int indice) {
		return Arrays.stream(values())
				.filter(coluna -> coluna.getColuna() == indice)
				.findFirst()
				.orElseThrow(() -> new ArquivoContaException(
						String.format("O arquivo de contas a pagar excedeu o número máximo de colunas permitido. A quantidade máxima é de %d colunas. ",  (values().length))));
	}

}
