package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MapaAlunoAptoFormarVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MapaAlunoAptoFormarInterfaceFacade;


@Repository
@Scope("singleton")
public class MapaAlunoAptoFormar extends ControleAcesso implements MapaAlunoAptoFormarInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9203700311511730555L;
	
	@Override
	public File realizarGeracaoExcel(List<MapaAlunoAptoFormarVO> listaMapaAlunoAptoForma,  String urlLogoPadraoRelatorio,UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(listaMapaAlunoAptoForma), "Não foi encontrado dados para geração do Relatório.");
		File arquivo = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Mapa Aluno Apto Formar");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, sheet, urlLogoPadraoRelatorio, null , 9, "");
		montarCabecalhoRelatorioExcel(uteisExcel, workbook, sheet);
		montarCorpoRelatorioExcel(listaMapaAlunoAptoForma, uteisExcel, workbook, sheet, usuario);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + String.valueOf(new Date().getTime())+".xls");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	public void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet) {
		int cellnum = 0;	
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000, "Matrícula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Aluno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Situação Atual Matrícula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Curso");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Turma");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Data 1ª Aula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Data Última Aula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Qtd Disc. Turma");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 5000,"Qtde Disc. Cursada");		
	}
	
	public void montarCorpoRelatorioExcel(List<MapaAlunoAptoFormarVO> listaMapaAlunoAptoForma, UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet,  UsuarioVO usuario) throws Exception {		
		Row row = null;
		for (MapaAlunoAptoFormarVO mapaAlunoApto : listaMapaAlunoAptoForma) {			
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			int cellnum = 0;
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getMatriculaPeriodo().getMatriculaVO().getMatricula());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getMatriculaPeriodo().getMatriculaVO().getAluno().getNome());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getSituacao_Apresentar());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getMatriculaPeriodo().getMatriculaVO().getCurso().getNome());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getMatriculaPeriodo().getTurma().getIdentificadorTurma());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getDataAulaIni());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getDataAulaFim());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getQtdeDisc());
			uteisExcel.preencherCelula( row, cellnum++,mapaAlunoApto.getQtdeDiscCursada());			
		}
	}

}
