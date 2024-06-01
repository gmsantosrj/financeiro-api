package br.com.financeiro.infrastructure.service.arquivo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import br.com.financeiro.api.v1.model.input.ContaInput;
import br.com.financeiro.domain.exception.ValidationException;
import br.com.financeiro.domain.exception.bean.ValidationError;
import br.com.financeiro.domain.service.ArquivoContaProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: GILMAR
 * @since: 25 de mai. de 2024
 */
@Slf4j
@Component
public class ArquivoExcelContaProcessor implements ArquivoContaProcessor {
	
	private static final String MSG_ERRO_NUMERICO_INVALIDO = "%s na linha %d referente a coluna %s possui um valor inválido. Corrija e informe um valor que contenha apenas números.";
	private static final String MSG_ERRO_TIPO_INVALIDO = "%s na linha %d referente a coluna %s possui um valor inválido. Corrija e informe um valor compatível com o tipo %s";
	private static final String MSG_ERRO_VALOR_INVALIDO = "%s na linha %d referente a coluna %s possui um valor inválido. Corrija e informe um valor válido.";
	
	private static final int NUMERO_INDICE = 0;
	private static final int QUANTIDADE_LINHAS = 2;
	private static final int SHEET_CONTAS = 0;
	
	private List<ContaInput> registrosDaConta;
	
	private List<ValidationError> erros;
	
	@Override
	public List<ContaInput> processar(InputStream inputStream) {

		try {
			log.info("PORCESSAMENTO DO ARQUIVO EXCEL DE CONTAS A PAGAR INICIADO");
			Workbook workbook = WorkbookFactory.create(inputStream);
			
			this.erros = new ArrayList<>();
			this.registrosDaConta = new ArrayList<>();

			Sheet sheet = workbook.getSheetAt(SHEET_CONTAS);
			removerLinhas(sheet, NUMERO_INDICE, QUANTIDADE_LINHAS);

			List<Row> rows = getRows(sheet);
			processarLinhas(rows);

			log.info("PORCESSAMENTO DO ARQUIVO EXCEL DE CONTAS A PAGAR FINALIZADO");

			return registrosDaConta;

		} catch (IOException e) {
			throw new ArquivoContaException("Não foi possível processar o arquivo.", e);

		}
	}
		
	@SuppressWarnings("unchecked")
	private List<Row> getRows(Sheet sheet){
		return (List<Row>) toList(sheet.iterator());
	}
	
	public List<?> toList(Iterator<?> iterator) {
		return IteratorUtils.toList(iterator);
	}

	
	private void processarLinhas(List<Row> rows) {
		validarQuantidadeDeColunas(rows);
		rows.forEach(row -> {
			List<Cell> celulas = getCell(row);
			validarCelula(celulas);
		});

		if (this.erros.isEmpty()) {
			rows.forEach(this::criarArquivoMovimentacaoDTO);
			
			if(this.registrosDaConta.isEmpty()) {
				throw new ArquivoContaException("O arquivo de movimentação não possui dados para serem processados.");
			}
			
		} else {
			throw new ValidationException(erros);
		}
	}
	
	private void validarQuantidadeDeColunas(List<Row> rows) {
		int quantidadeDeColunas = rows.get(0).getLastCellNum();
		
		if (quantidadeDeColunas != ColunaPlanilha.values().length) {
			var valores = Arrays.stream(ColunaPlanilha.values())
					.map(ColunaPlanilha::getDescricao)
					.collect(Collectors.joining(", "));
			
			throw new ArquivoContaException("O arquivo de contas a pagar está vazio ou contém um número inválido de colunas. Certifique-se de que as colunas estejam na seguinte ordem: " + valores);
		}
	}
	
	
	private void criarArquivoMovimentacaoDTO(Row row) {
		if (possuiCelulasPreenchidas(row)) {
			List<Cell> celulas = getCell(row);

			ContaInput contaInput = ContaInput.builder()
					.descricao(getStringValue(celulas.get(ColunaPlanilha.DESCRICAO.getColuna())))
					.valor(getBigDecimalValue(celulas.get(ColunaPlanilha.VALOR.getColuna())))
					.dataPagamento(getLocalDateValue(celulas.get(ColunaPlanilha.DATA_PAGAMENTO.getColuna())))
					.dataVencimento(getLocalDateValue(celulas.get(ColunaPlanilha.DATA_VENCIMENTO.getColuna())))
					.situacao(getStringValue(celulas.get(ColunaPlanilha.SITUACAO.getColuna()))).build();

			this.registrosDaConta.add(contaInput);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Cell> getCell(Row row) {
		return (List<Cell>) toList(row.cellIterator());		
	}

	
    private void removerLinhas(Sheet sheet, int startRowIndex, int numRows) {
        for (int i = 0; i < numRows; i++) {
            Row row = sheet.getRow(startRowIndex);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
        
       sheet.shiftRows(startRowIndex + numRows, sheet.getLastRowNum(), -numRows);
    }
    
	public void validarCelula(List<Cell> cells) {
		for (int idxCel = 0; idxCel < cells.size(); idxCel++) {

			Cell celula = cells.get(idxCel);
			int linha = celula.getRowIndex() + 3;
			int indexDacoluna = celula.getColumnIndex();
			String coluna = converterIndiceParaLetra(indexDacoluna);
			
			ColunaPlanilha tipoColuna = ColunaPlanilha.getColuna(idxCel);
	        validarColuna(tipoColuna, celula, linha, coluna);
		}
	}
    
    private void validarColuna(ColunaPlanilha tipoColuna, Cell celula, int linha, String coluna) {

		switch (tipoColuna) {
		
			case DESCRICAO:
				validarColunaDescricao(celula, linha, coluna);
				break;
	
			case VALOR:
				validarColunaValor(celula, linha, coluna);
				break;
	
			case DATA_VENCIMENTO:
				validarColunaDataVencimento(celula, linha, coluna);
				break;
	
			case DATA_PAGAMENTO:
				validarColunaDataPagamento(celula, linha, coluna);
				break;
	
			case SITUACAO:
				validarColunaSituacao(celula, linha, coluna);
				break;
		}
	}
    
    private void validarColunaDescricao(Cell celula, int linha, String coluna) {
		if (celula.getCellType() != CellType.BLANK && celula.getCellType() != CellType.STRING) {
			Object[] parametros = {ColunaPlanilha.DESCRICAO.getDescricao(), linha, coluna, ColunaPlanilha.DESCRICAO.getTipo()};
			adicionarErro(ColunaPlanilha.DESCRICAO, MSG_ERRO_TIPO_INVALIDO, parametros);
		} 
	}
    
    private void validarColunaSituacao(Cell celula, int linha, String coluna) {
  		if (celula.getCellType() != CellType.BLANK && celula.getCellType() != CellType.STRING) {
  			Object[] parametros = {ColunaPlanilha.SITUACAO.getDescricao(), linha, coluna, ColunaPlanilha.DESCRICAO.getTipo()};
  			adicionarErro(ColunaPlanilha.SITUACAO, MSG_ERRO_TIPO_INVALIDO, parametros);
  		} 
  	}
    
    private void validarColunaValor(Cell celula, int linha, String coluna) {
		if(celula.getCellType() != CellType.BLANK && celula.getCellType() != CellType.NUMERIC) {
			Object[] parametros = {ColunaPlanilha.VALOR.getDescricao(), linha, coluna};
			adicionarErro(ColunaPlanilha.VALOR, MSG_ERRO_NUMERICO_INVALIDO, parametros);
		}
	}
    
    private void validarColunaDataVencimento(Cell celula, int linha, String letraColuna) {
		if (!isDataValida(celula)) {
			Object[] parametros = { ColunaPlanilha.DATA_VENCIMENTO.getDescricao(), linha, letraColuna };
			adicionarErro(ColunaPlanilha.DATA_VENCIMENTO, MSG_ERRO_VALOR_INVALIDO, parametros);
		}
	}
    
    private void validarColunaDataPagamento(Cell celula, int linha, String letraColuna) {
  		if (!isDataValida(celula)) {
  			Object[] parametros = { ColunaPlanilha.DATA_PAGAMENTO.getDescricao(), linha, letraColuna };
  			adicionarErro(ColunaPlanilha.DATA_PAGAMENTO, MSG_ERRO_VALOR_INVALIDO, parametros);
  		}
  	}
    
    private void adicionarErro(ColunaPlanilha coluna, String mensagemErro, Object... parametros) {
	    this.erros.add(new ValidationError(coluna.getDescricao(), String.format(mensagemErro, parametros)));
	}
    
    private boolean isDataValida(Cell celula) {
    	
    	if(celula.getCellType() == CellType.BLANK) {
    		return true;
    	}
    	
		return celula.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(celula);
	}
    
    private String converterIndiceParaLetra(int indice) {
		return Character.toString((char) ('A' + indice));
	}
    
	private String getStringValue(Cell celula) {
		return celula == null || celula.getCellType() == CellType.BLANK ? null : celula.getStringCellValue();
	}
	
	private BigDecimal getBigDecimalValue(Cell celula) {
		return celula == null || celula.getCellType() == CellType.BLANK ? null : BigDecimal.valueOf(celula.getNumericCellValue());
	}
	
	private boolean possuiCelulasPreenchidas(Row row) {
		return row.cellIterator().hasNext();
	}
	
	private LocalDate getLocalDateValue(Cell celula) {
		if (celula.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(celula)) {
			Date date = celula.getDateCellValue();
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		}
		return null;
	}
	
   
}


