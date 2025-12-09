package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.financeiro.CategoriaDespesaTurmaVO;
import relatorio.negocio.comuns.financeiro.DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO;
import relatorio.negocio.comuns.financeiro.DemonstrativoResultadoFinanceiroTurmaRelVO;
import relatorio.negocio.interfaces.financeiro.DemonstrativoResultadoFinanceiroRelInterfaceFacade;

@Service
@Lazy
public class DemonstrativoResultadoFinanceiroRel extends ControleAcesso implements DemonstrativoResultadoFinanceiroRelInterfaceFacade {

	private static final long serialVersionUID = 7756441591668088623L;

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws ConsistirException {
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				return;
			}
		}
		throw new ConsistirException(UteisJSF.internacionalizar("msg_DRE_unidadeEnsino"));
	}

	@Override
	public String realizarGeracaoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, Integer mesInicio, Integer anoInicio, Integer mesTermino, Integer anoTermino, String nivelEducacional, UsuarioVO usuarioVO, boolean filtrarDataFatoGerador) throws Exception {
		validarDados(unidadeEnsinoVOs);
		String nomeArquivo = "DRE_" + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + ".xls";
		FileOutputStream fileOut = null;
		HSSFWorkbook workbook = null;
		HSSFSheet worksheet = null;
		HSSFSheet resumoFinal = null;
		Map<String, CategoriaDespesaVO> categoriaDespesasVOs = consultarCategoriaDespesaPeriodo();
		try {
			fileOut = new FileOutputStream(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			workbook = new HSSFWorkbook();
			// realizarGeracaoDadosRelatorio(unidadeEnsinoVOs, cursoVO, turmaVO,
			// calInicio.getTime(), calTermino.getTime(), nivelEducacional,
			// worksheet, categoriaDespesasVOs, filtrarDataFatoGerador,
			// resumoFinal, linhaResumoFinal);
			int linhaResumoFinal = 0;

			int mes1, mes2, contador = 0;
			for (int ano = anoInicio; ano <= anoTermino; ano++) {
				contador++;
				if (ano < anoTermino.intValue()) {
					if (contador <= 1) {
						mes1 = mesInicio;
						mes2 = 12;
					} else {
						mes1 = 1;
						mes2 = 12;
					}
				} else {
					if (contador <= 1) {
						mes1 = mesInicio;
						mes2 = mesTermino;
					} else {
						mes1 = 1;
						mes2 = mesTermino;
					}
				}
				for (int mes = mes1; mes <= mes2; mes++) {
					linhaResumoFinal++;
					worksheet = workbook.createSheet(MesAnoEnum.getEnum(UteisData.getNumeroDoisDigitos(mes)).getMes() + " " + ano);
					Calendar calInicio = Calendar.getInstance();
					calInicio.set(Calendar.DAY_OF_MONTH, 1);
					calInicio.set(Calendar.MONTH, mes - 1);
					calInicio.set(Calendar.YEAR, ano);
					Calendar calTermino = Calendar.getInstance();
					calTermino.set(Calendar.MONTH, mes - 1);
					calTermino.set(Calendar.YEAR, ano);
					calTermino.set(Calendar.DAY_OF_MONTH, calTermino.getActualMaximum(Calendar.DAY_OF_MONTH));
					realizarGeracaoCabecalhoRelatorio(worksheet, categoriaDespesasVOs);
					realizarGeracaoDadosRelatorio(unidadeEnsinoVOs, cursoVO, turmaVO, calInicio.getTime(), calTermino.getTime(), nivelEducacional, worksheet, categoriaDespesasVOs, filtrarDataFatoGerador, resumoFinal, linhaResumoFinal);
				}
			}
			worksheet = workbook.createSheet("TOTAL");

			Calendar calInicioTotal = Calendar.getInstance();
			calInicioTotal.set(Calendar.DAY_OF_MONTH, 1);
			calInicioTotal.set(Calendar.MONTH, mesInicio - 1);
			calInicioTotal.set(Calendar.YEAR, anoInicio);
			Calendar calTerminoTotal = Calendar.getInstance();
			calTerminoTotal.set(Calendar.DAY_OF_MONTH, calTerminoTotal.getActualMaximum(Calendar.DAY_OF_MONTH));
			calTerminoTotal.set(Calendar.MONTH, mesTermino - 1);
			calTerminoTotal.set(Calendar.YEAR, anoTermino);
			realizarGeracaoCabecalhoRelatorio(worksheet, categoriaDespesasVOs);
			realizarGeracaoDadosRelatorio(unidadeEnsinoVOs, cursoVO, turmaVO, calInicioTotal.getTime(), calTerminoTotal.getTime(), nivelEducacional, worksheet, categoriaDespesasVOs, filtrarDataFatoGerador, resumoFinal, linhaResumoFinal);
			workbook.write(fileOut);
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			fileOut = null;
			workbook = null;
			worksheet = null;
			if (categoriaDespesasVOs != null) {
				categoriaDespesasVOs.clear();
			}
			categoriaDespesasVOs = null;
		}
		return nomeArquivo;
	}

	private String getLetraCorrespondente(Integer coluna) {
		return CellReference.convertNumToColString(coluna - 1);
	}

	public void realizarGeracaoDadosRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, Date dataInicio, Date dataTermino, String nivelEducacional, HSSFSheet worksheet, Map<String, CategoriaDespesaVO> categoriaDespesasVOs, boolean filtrarDataFatoGerador, HSSFSheet resultadoFinal, Integer linhaResumoFinal) throws Exception {

		List<DemonstrativoResultadoFinanceiroTurmaRelVO> demonstrativoResultadoFinanceiroTurmaRelVOs = null;
		Integer linha = 1;
		Integer coluna = 0;
		// Integer colunaFinal = 0;
		HSSFRow cellLinha = null;
		HSSFCell cellColuna = null;
		HSSFRow cellLinhaResumoFinal = null;
		HSSFCell cellColunaResumoFinal = null;

		HSSFCellStyle styleOfInteger = worksheet.getWorkbook().createCellStyle();
		styleOfInteger.setFillForegroundColor(HSSFColor.WHITE.index);
		styleOfInteger.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleOfInteger.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfInteger.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfInteger.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfInteger.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfInteger.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfInteger.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle styleOfDouble = worksheet.getWorkbook().createCellStyle();
		styleOfDouble.setDataFormat(worksheet.getWorkbook().createDataFormat().getFormat("#,##0.00"));
		styleOfDouble.setFillForegroundColor(HSSFColor.WHITE.index);
		styleOfDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleOfDouble.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfDouble.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfDouble.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfDouble.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfDouble.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfDouble.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle styleOfPercent = worksheet.getWorkbook().createCellStyle();
		styleOfPercent.setDataFormat(worksheet.getWorkbook().createDataFormat().getFormat("#0.0%"));
		styleOfPercent.setFillForegroundColor(HSSFColor.WHITE.index);
		styleOfPercent.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleOfPercent.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfPercent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfPercent.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfPercent.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfPercent.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfPercent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle styleOfString = worksheet.getWorkbook().createCellStyle();
		styleOfString.setFillForegroundColor(HSSFColor.WHITE.index);
		styleOfString.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		styleOfString.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfString.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfString.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfString.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfString.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfString.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		Double somaReceita = 0.0;
		try {
			demonstrativoResultadoFinanceiroTurmaRelVOs = consultarDadosGeracaoRelatorio(unidadeEnsinoVOs, cursoVO, turmaVO, dataInicio, dataTermino, nivelEducacional, categoriaDespesasVOs, filtrarDataFatoGerador);
			if (!demonstrativoResultadoFinanceiroTurmaRelVOs.isEmpty()) {
				for (DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO : demonstrativoResultadoFinanceiroTurmaRelVOs) {
					// Boolean existeDespesa = false;
					// for
					// (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO
					// demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO
					// :
					// demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs())
					// {
					// if
					// (demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.getValor()
					// > 0) {
					// existeDespesa = true;
					// break;
					// }
					// }
					// if
					// (demonstrativoResultadoFinanceiroTurmaRelVO.getReceita()
					// > 0 ||
					// demonstrativoResultadoFinanceiroTurmaRelVO.getInadimplencia()
					// > 0) { // || existeDespesa
					cellLinha = worksheet.createRow(linha++);
					coluna = 0;

					// Coluna da Unidade de Ensino
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_STRING);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getUnidadeEnsino());
					cellColuna.setCellStyle(styleOfString);

					// Coluna do Curso ou Departamento
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_STRING);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getCurso());
					cellColuna.setCellStyle(styleOfString);

					// Coluna da Turma
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (demonstrativoResultadoFinanceiroTurmaRelVO.getTurma().equals("")) {
						cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getDepartamento());
					} else {
						cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getTurma());
					}
					cellColuna.setCellStyle(styleOfString);

					// Coluna Total de Bolsas
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getTotalBolsas());
					cellColuna.setCellStyle(styleOfInteger);

					// Coluna Qtde de Pagantes
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getQtdeAlunos());
					cellColuna.setCellStyle(styleOfInteger);

					// Coluna de Inadimpl?ncia
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getInadimplencia());
					cellColuna.setCellStyle(styleOfDouble);

					// Coluna de Receitas
					cellColuna = cellLinha.createCell(coluna++);
					cellColuna.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaRelVO.getReceita());
					somaReceita += demonstrativoResultadoFinanceiroTurmaRelVO.getReceita();
					cellColuna.setCellStyle(styleOfDouble);
					List listaCat = new ArrayList(categoriaDespesasVOs.keySet());
					Collections.sort(listaCat);
					Iterator i = listaCat.iterator();
					// Do what you need with sorted
					while (i.hasNext()) {
						String key = (String) i.next();
						for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
							if (categoriaDespesasVOs.get(key).getCodigo().equals(demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.getCategoriaDespesa())) {
								cellColuna = cellLinha.createCell(coluna++);
								cellColuna.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cellColuna.setCellValue(demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.getValor());
								cellColuna.setCellStyle(styleOfDouble);
							}
						}
					}
					// }
				}

				// cellLinhaResumoFinal =
				// resultadoFinal.createRow(linhaResumoFinal);
				// cellColunaResumoFinal = cellLinhaResumoFinal.createCell(0);
				// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_STRING);
				// cellColunaResumoFinal.setCellValue(unidadeEnsinoVO.getNome());

				if (linha > 1) {
					// HSSFCellStyle styleOfFooter =
					// worksheet.getWorkbook().createCellStyle();
					// styleOfFooter.setFillForegroundColor(HSSFColor.YELLOW.index);
					// styleOfFooter.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					// styleOfFooter.setFillPattern(HSSFCellStyle.BORDER_THIN);
					// styleOfFooter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					// styleOfFooter.setBorderRight(HSSFCellStyle.BORDER_THIN);
					// styleOfFooter.setBottomBorderColor(HSSFColor.BLACK.index);
					// styleOfFooter.setLeftBorderColor(HSSFColor.BLACK.index);
					// styleOfFooter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					// styleOfFooter.setDataFormat(worksheet.getWorkbook().createDataFormat().getFormat("#,##0.00"));
					//
					// HSSFCellStyle styleOfFooterInt =
					// worksheet.getWorkbook().createCellStyle();
					// styleOfFooterInt.setFillForegroundColor(HSSFColor.YELLOW.index);
					// styleOfFooterInt.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					// styleOfFooterInt.setFillPattern(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterInt.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterInt.setBorderRight(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterInt.setBottomBorderColor(HSSFColor.BLACK.index);
					// styleOfFooterInt.setLeftBorderColor(HSSFColor.BLACK.index);
					// styleOfFooterInt.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					// styleOfFooterInt.setDataFormat(worksheet.getWorkbook().createDataFormat().getFormat("##0"));
					//
					// HSSFCellStyle styleOfFooterPercent =
					// worksheet.getWorkbook().createCellStyle();
					// styleOfFooterPercent.setFillForegroundColor(HSSFColor.YELLOW.index);
					// styleOfFooterPercent.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					// styleOfFooterPercent.setFillPattern(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterPercent.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterPercent.setBorderRight(HSSFCellStyle.BORDER_THIN);
					// styleOfFooterPercent.setBottomBorderColor(HSSFColor.BLACK.index);
					// styleOfFooterPercent.setLeftBorderColor(HSSFColor.BLACK.index);
					// styleOfFooterPercent.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					// styleOfFooterPercent.setDataFormat(worksheet.getWorkbook().createDataFormat().getFormat("#0.0%"));
					//
					// coluna = 2;
					// // colunaFinal = 2;
					// // Coluna com o total de alunos
					// styleOfFooter.setFillForegroundColor(HSSFColor.YELLOW.index);
					// cellLinha = worksheet.createRow(linha);
					// cellColuna = cellLinha.createCell(coluna++);
					// cellColuna.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// cellColuna.setCellFormula("SUM(" +
					// getLetraCorrespondente(coluna) + "2:" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// cellColuna.setCellStyle(styleOfFooterInt);
					//
					// // cellLinhaResumoFinal =
					// resultadoFinal.createRow(linhaResumoFinal);
					// // cellColunaResumoFinal =
					// cellLinhaResumoFinal.createCell(colunaFinal++);
					// //
					// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_STRING);
					// //
					// cellColunaResumoFinal.setCellValue(unidadeEnsinoVO.getNome());
					//
					//
					// // cellColunaResumoFinal =
					// cellLinhaResumoFinal.createCell(colunaFinal++);
					// //
					// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// //
					// cellColunaResumoFinal.setCellFormula("SUM('DRE - "+unidadeEnsinoVO.getNome()+"'!"+
					// getLetraCorrespondente(coluna) + "2:"
					// +"'DRE - "+unidadeEnsinoVO.getNome()+"'!"
					// +getLetraCorrespondente(coluna) + linha.toString() +
					// ")");
					// // cellColunaResumoFinal.setCellStyle(styleOfFooterInt);
					//
					// // Coluna com o total de receitas
					// cellColuna = cellLinha.createCell(coluna++);
					// cellColuna.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// cellColuna.setCellFormula("SUM(" +
					// getLetraCorrespondente(coluna) + "2:" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// cellColuna.setCellStyle(styleOfFooter);
					//
					// // cellColunaResumoFinal =
					// cellLinhaResumoFinal.createCell(colunaFinal++);
					// //
					// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// //
					// cellColunaResumoFinal.setCellFormula("SUM('DRE - "+unidadeEnsinoVO.getNome()+"'!"
					// + getLetraCorrespondente(coluna) + "2:"
					// +"'DRE - "+unidadeEnsinoVO.getNome()+"'!"+
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// // cellColunaResumoFinal.setCellStyle(styleOfFooter);
					//
					// // Coluna com o total de imposto
					// cellColuna = cellLinha.createCell(coluna++);
					// cellColuna.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// cellColuna.setCellFormula("SUM(" +
					// getLetraCorrespondente(coluna) + "2:" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// cellColuna.setCellStyle(styleOfFooter);
					//
					// // cellColunaResumoFinal =
					// cellLinhaResumoFinal.createCell(colunaFinal++);
					// //
					// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// //
					// cellColunaResumoFinal.setCellFormula("SUM('DRE - "+unidadeEnsinoVO.getNome()+"'!"
					// + getLetraCorrespondente(coluna) + "2:"
					// +"'DRE - "+unidadeEnsinoVO.getNome()+"'!"+
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// // cellColunaResumoFinal.setCellStyle(styleOfFooter);
					//
					// // Coluna com o total de inadimplencia
					// cellColuna = cellLinha.createCell(coluna++);
					// cellColuna.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// cellColuna.setCellFormula("SUM(" +
					// getLetraCorrespondente(coluna) + "2:" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// cellColuna.setCellStyle(styleOfFooter);
					//
					// // cellColunaResumoFinal =
					// cellLinhaResumoFinal.createCell(colunaFinal++);
					// //
					// cellColunaResumoFinal.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// //
					// cellColunaResumoFinal.setCellFormula("SUM('DRE - "+unidadeEnsinoVO.getNome()+"'!"
					// + getLetraCorrespondente(coluna) + "2:"
					// +"'DRE - "+unidadeEnsinoVO.getNome()+"'!" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// // cellColunaResumoFinal.setCellStyle(styleOfFooter);
					//
					// List listaCat = new
					// ArrayList(categoriaDespesasVOs.keySet());
					// Collections.sort(listaCat);
					// Iterator i = listaCat.iterator();
					// // Do what you need with sorted
					// while (i.hasNext()) {
					// String key = (String)i.next();
					// cellColuna = cellLinha.createCell(coluna++);
					// cellColuna.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					// cellColuna.setCellFormula("SUM(" +
					// getLetraCorrespondente(coluna) + "2:" +
					// getLetraCorrespondente(coluna) + linha.toString() + ")");
					// cellColuna.setCellStyle(styleOfFooter);
					// }
				} else {
					// throw new
					// Exception("N?o foi encontrado nenhuma informa??o para a gera??o deste relat?rio.");
				}
			} else {
				// throw new
				// Exception("N?o foi encontrado nenhuma informa??o para a gera??o deste relat?rio.");
			}
		} catch (Exception e) {
			demonstrativoResultadoFinanceiroTurmaRelVOs.clear();
			demonstrativoResultadoFinanceiroTurmaRelVOs = null;
			throw e;
		}
	}

	private void realizarGeracaoCabecalhoRelatorioResumoFinal(HSSFSheet worksheet) throws Exception {
		HSSFRow cabecalho = null;
		HSSFCell cellCabecalho = null;
		Integer coluna = 0;
		HSSFCellStyle styleOfHeader = worksheet.getWorkbook().createCellStyle();
		styleOfHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleOfHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleOfHeader.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfHeader.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		try {
			cabecalho = worksheet.createRow(0);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Total de Bolsas");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Total de Pagantes");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Inadimpl?ncia");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Receita");
			cellCabecalho.setCellStyle(styleOfHeader);

			worksheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + getLetraCorrespondente(coluna) + "1"));
			worksheet.createFreezePane(1, 1);
			worksheet.setAutobreaks(true);
		} catch (Exception e) {
			throw e;
		} finally {
			cabecalho = null;
			cellCabecalho = null;
			coluna = 0;
		}
	}

	private void realizarGeracaoCabecalhoRelatorio(HSSFSheet worksheet, Map<String, CategoriaDespesaVO> categoriaDespesasVOs) throws Exception {
		HSSFRow cabecalho = null;
		HSSFCell cellCabecalho = null;
		Integer coluna = 0;

		HSSFCellStyle styleOfHeader = worksheet.getWorkbook().createCellStyle();
		styleOfHeader.setFillForegroundColor(HSSFColor.WHITE.index);
		styleOfHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleOfHeader.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfHeader.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfHeader.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		HSSFCellStyle styleOfHeaderPrincipal = worksheet.getWorkbook().createCellStyle();
		styleOfHeaderPrincipal.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleOfHeaderPrincipal.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleOfHeaderPrincipal.setFillPattern(HSSFCellStyle.BORDER_THIN);
		styleOfHeaderPrincipal.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		styleOfHeaderPrincipal.setBorderRight(HSSFCellStyle.BORDER_THIN);
		styleOfHeaderPrincipal.setBottomBorderColor(HSSFColor.BLACK.index);
		styleOfHeaderPrincipal.setLeftBorderColor(HSSFColor.BLACK.index);
		styleOfHeaderPrincipal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		try {
			cabecalho = worksheet.createRow(0);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Unidade");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Curso");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Turma");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Total de Bolsas");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("total de Pagantes");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Inadimpl?ncia");
			cellCabecalho.setCellStyle(styleOfHeader);

			cellCabecalho = cabecalho.createCell(coluna++);
			cellCabecalho.setCellValue("Receita");
			cellCabecalho.setCellStyle(styleOfHeader);

			List listaCat = new ArrayList(categoriaDespesasVOs.keySet());
			Collections.sort(listaCat);
			Iterator i = listaCat.iterator();
			// Do what you need with sorted
			while (i.hasNext()) {
				String codigoCategoriaDespesa = (String) i.next();
				cellCabecalho = cabecalho.createCell(coluna++);
				cellCabecalho.setCellValue(categoriaDespesasVOs.get(codigoCategoriaDespesa).getDescricao());
				if ((categoriaDespesasVOs.get(codigoCategoriaDespesa).getCategoriaDespesaPrincipal() == null) || (categoriaDespesasVOs.get(codigoCategoriaDespesa).getCategoriaDespesaPrincipal().equals(0))) {
					cellCabecalho.setCellStyle(styleOfHeaderPrincipal);
				} else {
					cellCabecalho.setCellStyle(styleOfHeader);
				}
			}
			worksheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + getLetraCorrespondente(coluna) + "1"));
			worksheet.createFreezePane(1, 1);
			worksheet.setAutobreaks(true);
		} catch (Exception e) {
			throw e;
		} finally {
			cabecalho = null;
			cellCabecalho = null;
			coluna = 0;
		}
	}

	private Map<String, CategoriaDespesaVO> consultarCategoriaDespesaPeriodo() {
		Map<String, CategoriaDespesaVO> categoriaDespesasVOs = new HashMap<String, CategoriaDespesaVO>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select codigo, descricao, categoriaDespesaPrincipal, identificadorCategoriaDespesa from categoriaDespesa order by identificadorcategoriadespesa");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		CategoriaDespesaVO categoriaDespesaVO = null;
		while (rs.next()) {
			categoriaDespesaVO = new CategoriaDespesaVO();
			categoriaDespesaVO.setCodigo(rs.getInt("codigo"));
			categoriaDespesaVO.setDescricao(rs.getString("descricao"));
			categoriaDespesaVO.setIdentificadorCategoriaDespesa(rs.getString("identificadorCategoriaDespesa"));
			categoriaDespesaVO.setCategoriaDespesaPrincipal(rs.getInt("categoriaDespesaPrincipal"));
			categoriaDespesasVOs.put(rs.getString("identificadorCategoriaDespesa"), categoriaDespesaVO);
		}
		return categoriaDespesasVOs;
	}

	private List<DemonstrativoResultadoFinanceiroTurmaRelVO> consultarDadosGeracaoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, Date dataInicio, Date dataTermino, String nivelEducacional, Map<String, CategoriaDespesaVO> categoriaDespesasVOs, boolean filtrarDataFatoGerador) throws Exception {
		Map<String, DemonstrativoResultadoFinanceiroTurmaRelVO> mapDemonstrativoResultadoFinanceiroTurmaRelVOs = consultarReceitasGeracaoRelatorio(unidadeEnsinoVOs, cursoVO, turmaVO, dataInicio, dataTermino, nivelEducacional, categoriaDespesasVOs, filtrarDataFatoGerador);
		List<DemonstrativoResultadoFinanceiroTurmaRelVO> lista = consultarDadosDespesa(mapDemonstrativoResultadoFinanceiroTurmaRelVOs, dataInicio, dataTermino, categoriaDespesasVOs, filtrarDataFatoGerador, unidadeEnsinoVOs, cursoVO, turmaVO, nivelEducacional);
		// SOMAR VALORES NAS CATEGORIAS DESPESAS PRINCIPAIS
		return lista;
	}

	private List<DemonstrativoResultadoFinanceiroTurmaRelVO> consultarDadosDespesa(Map<String, DemonstrativoResultadoFinanceiroTurmaRelVO> mapDemonstrativoResultadoFinanceiroTurmaRelVOs, Date dataInicio, Date dataTermino, Map<String, CategoriaDespesaVO> categoriaDespesasVOs, boolean filtrarDataFatoGerador, List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, String nivelEducacional) {
		StringBuilder sql = new StringBuilder("");
		if (filtrarDataFatoGerador) {
			sql.append(" select Curso.nome as curso, Turma.identificadorTurma as turma, Turma.codigo as codigoTurma,  Departamento.nome as departamento,  Departamento.codigo as codigoDepartamento,  categoriaDespesa.codigo as categoriaDespesa, " + "case when turma.codigo is not null then 'TU' else case when departamento.codigo is not null then 'DE' else 'UE' end end as nivel,  "
			// + " 'UE' as nivel,"
					+ "sum(case when contapagar.situacao =  'AP' then valor else valorPago end) as valor, unidadeEnsino.nome as unidadeEnsino, categoriaDespesa.codigo as categoraDespesa, categoriaDespesa.categoriaDespesaPrincipal from contapagar ");
			sql.append(" inner join categoriaDespesa on categoriaDespesa.codigo = centrodespesa ");
			sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = contaPagar.unidadeEnsino ");
			sql.append(" left join turma on turma.codigo = contapagar.turma ");
			sql.append(" left join curso on curso.codigo = turma.curso ");
			sql.append(" left join departamento on departamento.codigo = contapagar.departamento ");
			sql.append(" where contapagar.situacao in ('AP', 'PA') AND ");
			sql.append("contapagar.unidadeEnsino in (");
			String aux = "";
			for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOs) {
				if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
					sql.append(aux).append(unidadeEnsino.getCodigo());
					aux = ",";
				}
			}
			sql.append(") AND ");
			if (filtrarDataFatoGerador) {
				sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "contapagar.dataFatoGerador", true));
			}
			if (turmaVO != null && turmaVO.getCodigo() > 0) {
				sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
			}
			if (cursoVO != null && cursoVO.getCodigo() > 0) {
				sql.append(" and turma.curso = ").append(cursoVO.getCodigo());
			}
			if (nivelEducacional != null && !nivelEducacional.trim().isEmpty() && turmaVO.getCodigo() == 0 && cursoVO.getCodigo() == 0) {
				if (nivelEducacional.equals("POS_GRADUACAO") || nivelEducacional.equals("EXTENSAO")) {
					sql.append(" and curso.niveleducacional = 'PO'");
				}
				if (nivelEducacional.equals("GRADUACAO")) {
					sql.append(" and curso.niveleducacional = 'SU'");
				}
			}
			sql.append(" group by unidadeEnsino.nome, Curso.nome, Turma.identificadorTurma, Departamento.nome, categoriaDespesa.codigo, categoriaDespesa.descricao, turma.codigo,  departamento.codigo ");
			sql.append(" having sum(case when contapagar.situacao =  'AP' then valor else valorPago end) > 0 ");
			sql.append(" order by unidadeEnsino.nome, Curso.nome, Turma.identificadorTurma, Departamento.nome, case when turma.codigo is not null then 1 else case when departamento.codigo is not null then 2 else 3 end end, categoriaDespesa.identificadorCategoriaDespesa  ");
		} else {
			// select do data fato gerador desmarcado
			sql.append(" select Curso.nome as curso, Turma.identificadorTurma as turma, Turma.codigo as codigoTurma,  Departamento.nome as departamento,  Departamento.codigo as codigoDepartamento,  categoriaDespesa.codigo as categoriaDespesa, "
					+ "case when turma.codigo is not null then 'TU' else case when departamento.codigo is not null then 'DE' else 'UE' end end as nivel,  "
					//+ " 'UE' as nivel,"
					+ "sum(valorPago) as valor, unidadeEnsino.nome as unidadeEnsino, categoriaDespesa.codigo as categoraDespesa, categoriaDespesa.categoriaDespesaPrincipal from contapagar ");
			sql.append(" inner join categoriaDespesa on categoriaDespesa.codigo = centrodespesa ");
			sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = contaPagar.unidadeEnsino ");
			sql.append(" LEFT JOIN contapagarnegociacaopagamento as cpnp ON (cpnp.contapagar = contapagar.codigo) ");
			sql.append(" LEFT JOIN negociacaopagamento as pagamento ON (pagamento.codigo = cpnp.negociacaocontapagar) LEFT JOIN fornecedor ON (pagamento.fornecedor = fornecedor.codigo) ");
			sql.append(" left join turma on turma.codigo = contapagar.turma ");
			sql.append(" left join curso on curso.codigo = turma.curso ");
			sql.append(" left join departamento on departamento.codigo = contapagar.departamento ");
			sql.append(" where contapagar.situacao in ('PA') AND ");
			sql.append("contapagar.unidadeEnsino in (");
			String aux = "";
			for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOs) {
				if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
					sql.append(aux).append(unidadeEnsino.getCodigo());
					aux = ",";
				}
			}
			sql.append(") AND ");
			sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "pagamento.data", true));
			if (turmaVO != null && turmaVO.getCodigo() > 0) {
				sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
			}
			if (cursoVO != null && cursoVO.getCodigo() > 0) {
				sql.append(" and turma.curso = ").append(cursoVO.getCodigo());
			}
			if (nivelEducacional != null && !nivelEducacional.trim().isEmpty() && turmaVO.getCodigo() == 0 && cursoVO.getCodigo() == 0) {
				if (nivelEducacional.equals("POS_GRADUACAO") || nivelEducacional.equals("EXTENSAO")) {
					sql.append(" and curso.niveleducacional = 'PO'");
				}
				if (nivelEducacional.equals("GRADUACAO")) {
					sql.append(" and curso.niveleducacional = 'SU'");
				}
			}
			sql.append(" group by unidadeEnsino.nome, Curso.nome, Turma.identificadorTurma, Departamento.nome, categoriaDespesa.codigo, categoriaDespesa.descricao, turma.codigo,  departamento.codigo ");
			sql.append(" having sum(valorPago) > 0 ");
			sql.append(" order by unidadeEnsino.nome, Curso.nome, Turma.identificadorTurma, Departamento.nome, case when turma.codigo is not null then 1 else case when departamento.codigo is not null then 2 else 3 end end, categoriaDespesa.identificadorCategoriaDespesa  ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		while (rs.next()) {
			if (!(rs.getString("nivel").equals("TU") && mapDemonstrativoResultadoFinanceiroTurmaRelVOs.containsKey("A" + rs.getString("nivel") + rs.getString("unidadeEnsino") + rs.getString("turma"))) && !(rs.getString("nivel").equals("DE") && mapDemonstrativoResultadoFinanceiroTurmaRelVOs.containsKey("B" + rs.getString("nivel") + rs.getString("unidadeEnsino") + rs.getString("departamento"))) && !(rs.getString("nivel").equals("UE") && mapDemonstrativoResultadoFinanceiroTurmaRelVOs.containsKey("C" + rs.getString("nivel") + rs.getString("unidadeEnsino")))) {
				DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO = new DemonstrativoResultadoFinanceiroTurmaRelVO();
				demonstrativoResultadoFinanceiroTurmaRelVO.setUnidadeEnsino(rs.getString("unidadeEnsino"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setCategoriaDespesa(rs.getInt("categoriaDespesa"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setCurso(rs.getString("curso"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setTurma(rs.getString("turma"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setCodigoTurma(rs.getInt("codigoTurma"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setDepartamento(rs.getString("departamento"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setCodigoDepartamento(rs.getInt("codigoDepartamento"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setNivelCategoriaDespesa(rs.getString("nivel"));
				demonstrativoResultadoFinanceiroTurmaRelVO.setTotalBolsas(0);
				demonstrativoResultadoFinanceiroTurmaRelVO.setQtdeAlunos(0);
				demonstrativoResultadoFinanceiroTurmaRelVO.setReceita(0.0);
				demonstrativoResultadoFinanceiroTurmaRelVO.setInadimplencia(0.0);
				List listaCat = new ArrayList(categoriaDespesasVOs.keySet());
				Collections.sort(listaCat);
				// Do what you need with sorted
				Iterator i = listaCat.iterator();
				while (i.hasNext()) {
					String key = (String) i.next();
					DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
					demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setCategoriaDespesa(categoriaDespesasVOs.get(key).getCodigo());
					demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setCategoriaDespesaPrincipal(rs.getInt("categoriaDespesaPrincipal"));
					// demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setNivelCategoriaDespesa(categoriaDespesasVOs.get(key).getNivelCategoriaDespesa());
					demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setValor(0.0);
					if (demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.getCategoriaDespesa().equals(rs.getInt("categoriaDespesa"))) {
						demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setValor(rs.getDouble("valor"));
						realizarSomaValorEmCategoriaSuperior(demonstrativoResultadoFinanceiroTurmaRelVO, demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.getCategoriaDespesaPrincipal(), rs.getDouble("valor"));
					}
					demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs().add(demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO);
				}
				if (rs.getString("nivel").equals("TU")) {
					mapDemonstrativoResultadoFinanceiroTurmaRelVOs.put("A" + rs.getString("nivel") + rs.getString("unidadeensino") + rs.getString("turma"), demonstrativoResultadoFinanceiroTurmaRelVO);
				} else if (rs.getString("nivel").equals("DE")) {
					mapDemonstrativoResultadoFinanceiroTurmaRelVOs.put("B" + rs.getString("nivel") + rs.getString("unidadeensino") + rs.getString("departamento"), demonstrativoResultadoFinanceiroTurmaRelVO);
				} else {
					mapDemonstrativoResultadoFinanceiroTurmaRelVOs.put("C" + rs.getString("nivel") + rs.getString("unidadeensino"), demonstrativoResultadoFinanceiroTurmaRelVO);
				}
			} else {
				DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO = null;
				if (rs.getString("nivel").equals("TU")) {
					demonstrativoResultadoFinanceiroTurmaRelVO = mapDemonstrativoResultadoFinanceiroTurmaRelVOs.get("A" + rs.getString("nivel") + rs.getString("unidadeensino") + rs.getString("turma"));
				} else if (rs.getString("nivel").equals("DE")) {
					demonstrativoResultadoFinanceiroTurmaRelVO = mapDemonstrativoResultadoFinanceiroTurmaRelVOs.get("B" + rs.getString("nivel") + rs.getString("unidadeensino") + rs.getString("departamento"));
				} else {
					demonstrativoResultadoFinanceiroTurmaRelVO = mapDemonstrativoResultadoFinanceiroTurmaRelVOs.get("C" + rs.getString("nivel") + rs.getString("unidadeensino"));
				}
				//
				for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
					if (cat.getCategoriaDespesa().equals(rs.getInt("categoriaDespesa"))) {
						cat.setValor(cat.getValor() + rs.getDouble("valor"));
						realizarSomaValorEmCategoriaSuperior(demonstrativoResultadoFinanceiroTurmaRelVO, cat.getCategoriaDespesaPrincipal(), rs.getDouble("valor"));
						break;
					}
				}
			}
		}
		List<DemonstrativoResultadoFinanceiroTurmaRelVO> demonstrativoResultadoFinanceiroTurmaRelVOs = new ArrayList<DemonstrativoResultadoFinanceiroTurmaRelVO>(0);

		List listaFim = new ArrayList(mapDemonstrativoResultadoFinanceiroTurmaRelVOs.keySet());
		Collections.sort(listaFim);
		// Do what you need with sorted
		Iterator i = listaFim.iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			demonstrativoResultadoFinanceiroTurmaRelVOs.add((DemonstrativoResultadoFinanceiroTurmaRelVO) mapDemonstrativoResultadoFinanceiroTurmaRelVOs.get(key));
		}
		// Ordenacao.ordenarLista(demonstrativoResultadoFinanceiroTurmaRelVOs,
		// "ordenacao");
		return demonstrativoResultadoFinanceiroTurmaRelVOs;
	}

	private void realizarSomaValorEmCategoriaSuperior(DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO, Integer categoriaPrincipal, Double valor) {
		for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
			if (cat.getCategoriaDespesa().intValue() == categoriaPrincipal.intValue()) {
				System.out.println("categoria:" + cat.getCategoriaDespesa());
				cat.setValor(cat.getValor() + valor);
				Integer categoriaPai = null;
				if (cat.getCategoriaDespesaPrincipal().intValue() != 0) {
					// System.out.println("categoriaPai:" +
					// cat.getCategoriaDespesaPrincipal().intValue());
					// realizarSomaValorEmCategoriaSuperior(demonstrativoResultadoFinanceiroTurmaRelVO,
					// cat.getCategoriaDespesaPrincipal(), valor);
					for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat2 : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
						if (cat2.getCategoriaDespesa().intValue() == cat.getCategoriaDespesaPrincipal().intValue()) {
							// System.out.println("categoria:" +
							// cat.getCategoriaDespesa());
							cat2.setValor(cat2.getValor() + valor);
							if (cat2.getCategoriaDespesaPrincipal().intValue() != 0) {

								for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat3 : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
									if (cat3.getCategoriaDespesa().intValue() == cat2.getCategoriaDespesaPrincipal().intValue()) {
										// System.out.println("categoria:" +
										// cat.getCategoriaDespesa());
										cat3.setValor(cat3.getValor() + valor);
										if (cat3.getCategoriaDespesaPrincipal().intValue() != 0) {

											for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat4 : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
												if (cat4.getCategoriaDespesa().intValue() == cat3.getCategoriaDespesaPrincipal().intValue()) {
													// System.out.println("categoria:"
													// +
													// cat.getCategoriaDespesa());
													cat4.setValor(cat4.getValor() + valor);
													if (cat4.getCategoriaDespesaPrincipal().intValue() != 0) {

														for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO cat5 : demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs()) {
															if (cat5.getCategoriaDespesa().intValue() == cat4.getCategoriaDespesaPrincipal().intValue()) {
																// System.out.println("categoria:"
																// +
																// cat.getCategoriaDespesa());
																cat5.setValor(cat5.getValor() + valor);
															}
														}
													}
												}
											}

										}

									}
								}

							}
						}
					}
				}
				break;
			}
		}
	}

	private Map<String, DemonstrativoResultadoFinanceiroTurmaRelVO> consultarReceitasGeracaoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, Date dataInicio, Date dataTermino, String nivelEducacional, Map<String, CategoriaDespesaVO> categoriaDespesasVOs, boolean filtrarDataFatoGerador) throws Exception {
		Map<String, DemonstrativoResultadoFinanceiroTurmaRelVO> demonstrativoResultadoFinanceiroTurmaRelVOs = new HashMap<String, DemonstrativoResultadoFinanceiroTurmaRelVO>(0);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select unidadeEnsino.nome as unidadeEnsino, curso.nome as curso, Turma.codigo as codigoTurma, Turma.identificadorTurma as turma, ");
		sql.append(" (select count(distinct contareceber.matriculaaluno) from contareceber ");
		sql.append(" inner join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" where contareceber.codigo in (");
		if (!filtrarDataFatoGerador) {
			sql.append(" select contareceber from contarecebernegociacaorecebimento  ");
			sql.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo  ");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaorecebimento.data", true)).append(" ");
			sql.append(" union ");
		}
		sql.append(" select contareceber.codigo from contareceber where 1=1 ");
		if (!filtrarDataFatoGerador) {
			sql.append(" and contareceber.situacao =  'AR'  ");
		}
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", true)).append(") ");
		sql.append(" and matriculaperiodo.turma = turma.codigo and valor > 0");
		sql.append(" ) as qtdeAlunos, ");
		sql.append(" (select count(distinct contareceber.matriculaaluno) from contareceber ");
		sql.append(" inner join matricula on matricula.matricula = contareceber.matriculaaluno ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" where contareceber.codigo in (");
		if (!filtrarDataFatoGerador) {
			sql.append(" select contareceber from contarecebernegociacaorecebimento  ");
			sql.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo  ");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaorecebimento.data", true)).append(" ");
			sql.append(" union ");
		}
		sql.append(" select contareceber.codigo from contareceber where 1=1 ");
		if (!filtrarDataFatoGerador) {
			sql.append(" and contareceber.situacao =  'AR'  ");
		}
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", true)).append(") ");
		sql.append(" and matriculaperiodo.turma = turma.codigo and valor > 0 and matriculaperiodo.bolsista = true ");
		sql.append(" ) as qtdeBolsas, ");
		sql.append(" sum(cr1.valorrecebido) as receita, ");
		if (!filtrarDataFatoGerador) {
			sql.append(" sum(cr2.valor) as inadimplencia ");
		} else {
			sql.append(" sum(cr2.valordescontocalculadoprimeirafaixadescontos) as inadimplencia ");
		}
		sql.append(" from Turma ");
		sql.append(" inner join curso on curso.codigo =  Turma.curso ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = Turma.unidadeEnsino ");
		// if (!filtrarDataFatoGerador) {
		sql.append(" left join contareceber cr1 on cr1.matriculaaluno =  matriculaperiodo.matricula ");
		// } else {
		// sql.append(" left join contareceber cr1 on cr1.matriculaperiodo =  matriculaperiodo.codigo ");
		// }
		if (!filtrarDataFatoGerador) {
			sql.append(" and cr1.valorrecebido > 0 ");
		}
		sql.append(" and cr1.codigo in (");

		if (!filtrarDataFatoGerador) {
			sql.append(" select contareceber from contarecebernegociacaorecebimento ");
			sql.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaorecebimento.data", true)).append("  ");
		} else {
			sql.append(" select codigo from contareceber ");
			sql.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datavencimento", true)).append(" ");
		}
		if (dataTermino.compareTo(new Date()) > 0) {
			// if (!filtrarDataFatoGerador) {
			sql.append(" union ");
			// }
			sql.append(" select contareceber.codigo from contareceber where 1=1 ");
			if (!filtrarDataFatoGerador) {
				sql.append(" and contareceber.situacao =  'AR' ");
			}
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", true));
		}
		sql.append(" ) ");
		// if (!filtrarDataFatoGerador) {
		sql.append(" left join contareceber cr2 on cr2.matriculaaluno =  matriculaperiodo.matricula ");
		// } else {
		// sql.append(" left join contareceber cr2 on cr2.matriculaperiodo =  matriculaperiodo.codigo ");
		// }
		sql.append(" and cr2.situacao =  'AR' and cr2.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and cr2.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append("' and cr2.dataVencimento <current_date ");
		sql.append(" where Turma.unidadeEnsino in (");
		String aux = "";
		for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOs) {
			if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
				sql.append(aux).append(unidadeEnsino.getCodigo());
				aux = ",";
			}
		}
		sql.append(") ");
		if (turmaVO != null && turmaVO.getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sql.append(" and turma.curso = ").append(cursoVO.getCodigo());
		}
		if (nivelEducacional != null && !nivelEducacional.trim().isEmpty() && turmaVO.getCodigo() == 0 && cursoVO.getCodigo() == 0) {
			if (nivelEducacional.equals("POS_GRADUACAO")) {
				sql.append(" and curso.niveleducacional = 'PO'");
			}
			if (nivelEducacional.equals("GRADUACAO")) {
				sql.append(" and curso.niveleducacional = 'SU'");
			}
			if (nivelEducacional.equals("EXTENSAO")) {
				sql.append(" and curso.niveleducacional = 'PO' and matricula.tipoMatricula =  'EX'");
			}
		}
		sql.append(" group by unidadeEnsino.nome, curso.nome, Turma.identificadorTurma, Turma.codigo ");
		sql.append(" order by unidadeEnsino.nome, curso.nome, Turma.identificadorTurma ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO = null;
		DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO = null;
		while (rs.next()) {
			demonstrativoResultadoFinanceiroTurmaRelVO = new DemonstrativoResultadoFinanceiroTurmaRelVO();
			demonstrativoResultadoFinanceiroTurmaRelVO.setUnidadeEnsino(rs.getString("unidadeEnsino"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setCurso(rs.getString("curso"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setTurma(rs.getString("turma"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setCodigoTurma(rs.getInt("codigoTurma"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setQtdeAlunos(rs.getInt("qtdeAlunos"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setTotalBolsas(rs.getInt("qtdeBolsas"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setReceita(rs.getDouble("receita"));
			demonstrativoResultadoFinanceiroTurmaRelVO.setInadimplencia(rs.getDouble("inadimplencia"));
			List listaCat = new ArrayList(categoriaDespesasVOs.keySet());
			Collections.sort(listaCat);
			Iterator i = listaCat.iterator();
			// Do what you need with sorted
			while (i.hasNext()) {
				String key = (String) i.next();
				demonstrativoResultadoFinanceiroTurmaRelVO.setCategoriaDespesa(categoriaDespesasVOs.get(key).getCodigo());
				demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
				demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setCategoriaDespesaPrincipal(categoriaDespesasVOs.get(key).getCategoriaDespesaPrincipal());
				demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setCategoriaDespesa(categoriaDespesasVOs.get(key).getCodigo());
				// demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setNivelCategoriaDespesa(categoriaDespesasVOs.get(key).getNivelCategoriaDespesa());
				demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO.setValor(0.0);
				demonstrativoResultadoFinanceiroTurmaRelVO.getDemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs().add(demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO);
			}
			demonstrativoResultadoFinanceiroTurmaRelVOs.put("A" + "TU" + rs.getString("unidadeEnsino") + rs.getString("turma"), demonstrativoResultadoFinanceiroTurmaRelVO);
		}
		return demonstrativoResultadoFinanceiroTurmaRelVOs;

	}
	
	public void criarDemonstrativoResultado(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaParamVO, String nivelEducacional, Date dataInicio, Date dataFinal, Boolean filtrarDataFatoGerador, DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO) throws Exception {
        List<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO> demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs = new ArrayList<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO>(0);
        List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>(0);
        try {            
        	demonstrativoResultadoFinanceiroTurmaRelVO.getListaDemonstrativoResultadoFinanceiroTurmaRelVOs().clear();
        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa1VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa1VO.setIdentificadorCategoriaDespesa("Matrícula");
        	objCategoriaDespesa1VO.setMatricula(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa1VO);
        	
        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa2VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa2VO.setIdentificadorCategoriaDespesa("Mensalidade");
        	objCategoriaDespesa2VO.setMensalidade(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa2VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa3VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa3VO.setIdentificadorCategoriaDespesa("(-) Deduções");
        	objCategoriaDespesa3VO.setDeducoes(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa3VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa4VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa4VO.setIdentificadorCategoriaDespesa("Tributos");
        	objCategoriaDespesa4VO.setTributos(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa4VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa5VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa5VO.setIdentificadorCategoriaDespesa("Descontos");
        	objCategoriaDespesa5VO.setDescontos(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa5VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa6VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa6VO.setIdentificadorCategoriaDespesa("Cancelamentos");
        	objCategoriaDespesa6VO.setCancelamentos(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa6VO);
        	
        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa7VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa7VO.setIdentificadorCategoriaDespesa("Receita Líquida");
        	objCategoriaDespesa7VO.setReceitaLiquida(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa7VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa8VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa8VO.setIdentificadorCategoriaDespesa("(-) Custos e Despesas Variáveis");
        	objCategoriaDespesa8VO.setCustosDespesaVariavel(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa8VO);

        	List<CategoriaDespesaVO> categorias = getFacadeFactory().getCategoriaDespesaFacade().consultaRapidaCategoriaDespDRE();
        	Iterator<CategoriaDespesaVO> c = categorias.iterator();
        	while (c.hasNext()) {
        		CategoriaDespesaVO categoria = c.next();
	        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objDespesas = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
	        	objDespesas.setIdentificadorCategoriaDespesa(categoria.getDescricao());
	        	objDespesas.setCategoriasDespesas(true);
	        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objDespesas);
        	}

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa9VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa9VO.setIdentificadorCategoriaDespesa("= Margem de Contribuição");
        	objCategoriaDespesa9VO.setMargemContribuicao(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa9VO);

        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa10VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa10VO.setIdentificadorCategoriaDespesa("(-) Despesas Fixas");
        	objCategoriaDespesa10VO.setDespesasFixas(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa10VO);
        	
        	DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO objCategoriaDespesa11VO = new DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO();
        	objCategoriaDespesa11VO.setIdentificadorCategoriaDespesa("= Resultado");
        	objCategoriaDespesa11VO.setResultado(true);
        	demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs.add(objCategoriaDespesa11VO);
        	
        	List<TurmaVO> turmas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasDRE(unidadeEnsinoVOs, cursoVO, turmaParamVO, filtrarDataFatoGerador, dataInicio, dataFinal);
        	Iterator<TurmaVO> i = turmas.iterator();
        	while (i.hasNext()) {
        		TurmaVO turma = i.next();
            	turmaVOs.add(turma);
        	}
        	TurmaVO turma = new TurmaVO();
        	turma.setIdentificadorTurma(" CONSOLIDADO ");
        	turmaVOs.add(turma);
        	
            if (turmaVOs != null && !turmaVOs.isEmpty()) {
                boolean primeiro = true;
                for (TurmaVO turmaVO : turmaVOs) {
                	demonstrativoResultadoFinanceiroTurmaRelVO.getListaDemonstrativoResultadoFinanceiroTurmaRelVOs().add(criarGradeTamanhoItemPedido(demonstrativoResultadoFinanceiroTurmaRelVO, demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs, turmaVO, primeiro, filtrarDataFatoGerador, dataInicio, dataFinal));
                    primeiro = false;
                }
            }
        } finally {
        }
    }

    public DemonstrativoResultadoFinanceiroTurmaRelVO criarGradeTamanhoItemPedido(DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO, List<DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO> demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs, TurmaVO turmaVO, Boolean primeiro, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
    	DemonstrativoResultadoFinanceiroTurmaRelVO obj = new DemonstrativoResultadoFinanceiroTurmaRelVO();
    	Double valorMatricula = 0.0;
    	Double valorMensalidade = 0.0;
    	Double valorTributos = 0.0;
    	Double valorCancelamentos = 0.0;
    	Double valorDescontos = 0.0;
    	Double valorTotalDespesasVar = 0.0;
    	Double valorTotalDespesasFix = 0.0;
        try {
            for (DemonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVO resultadoCategoria : demonstrativoResultadoFinanceiroTurmaCategoriaDespesaRelVOs) {
                CategoriaDespesaTurmaVO categoriaDespesaTurmaVO = new CategoriaDespesaTurmaVO();
                categoriaDespesaTurmaVO.setCategoriaDespesa(resultadoCategoria.getIdentificadorCategoriaDespesa());
                categoriaDespesaTurmaVO.setTurma(turmaVO.getIdentificadorTurma());
                obj.setPrimeiro(primeiro);
                if (resultadoCategoria.getMatricula()) {
                	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaMatriculaDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal));
                	valorMatricula = categoriaDespesaTurmaVO.getValor();
                } else if (resultadoCategoria.getMensalidade()) {
                	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaMensalidadeDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal));
                	valorMensalidade = categoriaDespesaTurmaVO.getValor();
                } else if (resultadoCategoria.getDeducoes()) {
                	valorTributos = getFacadeFactory().getTurmaFacade().consultaRapidaTributosDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal); 
                	valorDescontos = getFacadeFactory().getTurmaFacade().consultaRapidaDescontosDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal);
                	valorCancelamentos = getFacadeFactory().getTurmaFacade().consultaRapidaCancelamentosDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal);
                	categoriaDespesaTurmaVO.setValor(valorCancelamentos + valorTributos + valorDescontos);
                } else if (resultadoCategoria.getTributos()) {
                	categoriaDespesaTurmaVO.setValor(valorTributos);
                } else if (resultadoCategoria.getDescontos()) {
                	categoriaDespesaTurmaVO.setValor(valorDescontos);
                } else if (resultadoCategoria.getCancelamentos()) {
                	categoriaDespesaTurmaVO.setValor(valorCancelamentos);
                } else if (resultadoCategoria.getReceitaLiquida()) {
                	categoriaDespesaTurmaVO.setValor((valorMatricula + valorMensalidade) - (valorCancelamentos + valorTributos + valorDescontos));
                } else if (resultadoCategoria.getCustosDespesaVariavel()) {
                	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaCustosDespesaVariavelDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal));
                	valorTotalDespesasVar += categoriaDespesaTurmaVO.getValor(); 
//	            } else if (resultadoCategoria.getMargemContribuicao()) {
//	            	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaMargemContribuicaoDRE(turmaVO.getCodigo(), getDataInicio(), getDataFinal()));
	            } else if (resultadoCategoria.getDespesasFixas()) {
	            	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaDespesasFixasDRE(turmaVO.getCodigo(), filtrarDataFatoGerador, dataInicio, dataFinal));
	            	valorTotalDespesasFix += categoriaDespesaTurmaVO.getValor(); 
	            } else if (resultadoCategoria.getMargemContribuicao()) {
	            	categoriaDespesaTurmaVO.setValor(((valorMatricula + valorMensalidade) - (valorCancelamentos + valorTributos + valorDescontos)) - valorTotalDespesasVar);
	            } else if (resultadoCategoria.getResultado()) {
	            	categoriaDespesaTurmaVO.setValor(valorTotalDespesasVar - valorTotalDespesasFix);
	            } else {
	            	categoriaDespesaTurmaVO.setValor(getFacadeFactory().getTurmaFacade().consultaRapidaCategoriaDespesaDRE(turmaVO.getCodigo(), resultadoCategoria.getIdentificadorCategoriaDespesa(), filtrarDataFatoGerador, dataInicio, dataFinal));
	            }
                
                obj.getListaCategoriaDespesaTurmaVOs().add(categoriaDespesaTurmaVO);
            }
            return obj;
        } finally {
        }
    }
}