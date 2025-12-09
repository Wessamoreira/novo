
package negocio.facade.jdbc.processosel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.EstadoCivilEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModalidadeBolsaEnum;
import negocio.comuns.basico.enumeradores.SituacaoMilitarEnum;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ImportarCandidatoInscricaoProcessoSeletivoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ImportarCandidatoInscricaoProcessoSeletivo extends ControleAcesso  implements ImportarCandidatoInscricaoProcessoSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	private File arquivo;

    public ImportarCandidatoInscricaoProcessoSeletivo() throws Exception {
        super();
        setIdEntidade("ImportarCandidatoInscricaoProcessoSeletivo");
    }
    
    @Override
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo(FileUploadEvent uploadEvent,  ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoVO, PessoaVO candidatoFiltroVO, String numeroInscricao, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoErroVOs, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoObservacaoVOs, List<ProcSeletivoVO> listaProcSeletivoNaoEncontradoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoNaoEncontradoVOs, List<CursoVO> listaCursoNaoEncontradoVOs, List<TurnoVO> listaTurnoNaoEncontradoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO,  String tipoLayout, UsuarioVO usuario) throws Exception {
		return realizarProcessamentoExcelImportarCandidadoInscricaoProcessoSeletivo(uploadEvent, importarCandidatoInscricaoVO, candidatoFiltroVO, numeroInscricao, listaCandidatoInscricaoProcessoSeletivoErroVOs, listaCandidatoInscricaoProcessoSeletivoObservacaoVOs, listaProcSeletivoNaoEncontradoVOs, listaUnidadeEnsinoNaoEncontradoVOs, listaCursoNaoEncontradoVOs, listaTurnoNaoEncontradoVOs, configuracaoGeralSistemaVO, inicializarDadosArquivo, progressBarVO, tipoLayout ,usuario);
	}

    public List<ImportarCandidatoInscricaoProcessoSeletivoVO> realizarProcessamentoExcelImportarCandidadoInscricaoProcessoSeletivo(FileUploadEvent uploadEvent, ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoVO, PessoaVO pessoaFiltroVO, String numeroInscricao, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoErroVOs, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoObservacaoVOs,  List<ProcSeletivoVO> listaProcSeletivoNaoEncontradoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoErroNaoEncontradoVOs, List<CursoVO> listaCursoNaoEncontradoVOs, List<TurnoVO> listaTurnoNaoEncontradoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean inicializarDadosArquivo, ProgressBarVO progressBarVO,  String tipoLayout, UsuarioVO usuario) throws Exception {
		List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0); 
		listaCandidatoInscricaoProcessoSeletivoErroVOs.clear();
		listaProcSeletivoNaoEncontradoVOs.clear();
		listaUnidadeEnsinoErroNaoEncontradoVOs.clear();
		listaCursoNaoEncontradoVOs.clear();
		listaCandidatoInscricaoProcessoSeletivoObservacaoVOs.clear();
		if (inicializarDadosArquivo) {
			inicializarDadosArquivoImportarCandidatoInscricaoProcessoSeletivo(importarCandidatoInscricaoVO, uploadEvent, configuracaoGeralSistemaVO, usuario);
		}
		
		File arquivo = arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue()
				+ File.separator + importarCandidatoInscricaoVO.getArquivoVO().getNome());
		
		InputStream stream = new FileInputStream(arquivo);
		
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
		int rowMax = 0;
		XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
		if (extensao.equals("xlsx")) {
//			PARA XLSX UTILIZA XSSFWorkbook
			XSSFWorkbook workbook = new XSSFWorkbook(stream);
			mySheetXlsx = workbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();

		} else {
//			PARA XLS UTILIZA HSSFWorkbook
			HSSFWorkbook workbook = new HSSFWorkbook(stream);
			mySheetXls = workbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
		}
		//progressBarVO.iniciar(1l, rowMax, "Iniciando.....", false, null, "");
		progressBarVO.setMaxValue(rowMax+1);
		int qtdeLinhaEmBranco = 0;
		int linha = 0;
		
		Map<String, ProcSeletivoVO> mapaProcessoSeletivo = new HashMap<String, ProcSeletivoVO>(0);
		Map<String, ImportarCandidatoInscricaoProcessoSeletivoVO> mapaCandidato = new HashMap<String, ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsino = new HashMap<Integer, UnidadeEnsinoVO>(0);
		Map<String, CursoVO> mapCurso = new HashMap<String, CursoVO>(0);
		Map<String, TurnoVO> mapTurno = new HashMap<String, TurnoVO>(0);
		Map<String, PaizVO> mapNacionalidade = new HashMap<String, PaizVO>(0);		
		Map<String, Map<Integer, Integer>> mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis = new HashMap<String, Map<Integer, Integer>>(0);
		Map<String, Integer> mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino = new HashMap<String, Integer>(0);
		
		Row row = null;
		while (linha <= rowMax) {
			System.out.println("Linha : " +linha);
			progressBarVO.setStatus("Carregando informações da linha "+linha+" de "+rowMax);
			if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
			if (linha == 0) {
				linha++;
				continue;
			}
			if (linha == 1) {
				validarDadosCabecalhoExcelTabela(row, tipoLayout);
				linha++;
				continue;
			}
			if (qtdeLinhaEmBranco == 2 ) {
				break;
			}
			if (getValorCelula(7, row, true) == null || getValorCelula(7, row, true).toString().equals("")) {
				qtdeLinhaEmBranco++;
				continue;
			}
//			System.out.println("Processando linha "+linha);
//			if (offset <= 0 ? numeroPessoa >= offset && numeroPessoa <= limit : numeroPessoa > offset && numeroPessoa <= offset + limit) {
				ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO = new ImportarCandidatoInscricaoProcessoSeletivoVO();
				
				if (progressBarVO.getForcarEncerramento()) {
					break;
				}
				int coluna = 0;
				try {
//				DADOS CANDIDATO
				candidatoInscricaoVO.setCorRaca(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
				coluna++;
				candidatoInscricaoVO.setTituloEleitoral(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA A
				coluna++;
				candidatoInscricaoVO.setOrgaoEmissor(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA B
				coluna++;
				candidatoInscricaoVO.setEstadoEmissaoRG(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA C
				coluna++;
				if(getValorCelula(coluna, row, false) != null && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataEmissaoRG(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA D
				}else {
					candidatoInscricaoVO.setDataEmissaoRG(getValorCelula(coluna, row, true) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, true)), "DD/MM/YYYY") : null); // COLUNA D
				}
				coluna++;
				candidatoInscricaoVO.setCertificadoMilitar(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA E
				coluna++;
				candidatoInscricaoVO.setRG(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA F
				coluna++;
				candidatoInscricaoVO.setCPF(getValorCelula(coluna, row, true) != null ? Uteis.removerEspacosString(String.valueOf(getValorCelula(coluna, row, true))) : "");  // COLUNA G
				coluna++;
				candidatoInscricaoVO.setNacionalidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");  // COLUNA H
				coluna++;
				candidatoInscricaoVO.setNomeNaturalidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA I
				coluna++;
				candidatoInscricaoVO.setEstadoNaturalidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");  // COLUNA J
				coluna++;
				if(getValorCelula(coluna, row, false) != null && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataNasc(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataNasc(getValorCelula(coluna, row, true) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, true)), "DD/MM/YYYY") : null); // COLUNA D
				}
				coluna++;
				candidatoInscricaoVO.setEmail(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)).trim() : ""); // COLUNA L
				coluna++;
				candidatoInscricaoVO.setCelular(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)).trim() : ""); // COLUNA M
				coluna++;
				candidatoInscricaoVO.setTelefoneRecado(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA N
				coluna++;
				candidatoInscricaoVO.setTelefoneRes(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA O
				coluna++;
				candidatoInscricaoVO.setEstadoCivil(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA P
				coluna++;
				candidatoInscricaoVO.setSexo(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA Q
				coluna++;
				candidatoInscricaoVO.setDeficiencia(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA R
				coluna++;
				candidatoInscricaoVO.setNomeCidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA S
				coluna++;
				candidatoInscricaoVO.setEstadoCidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA T
				coluna++;
				candidatoInscricaoVO.setComplemento(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA U
				coluna++;
				candidatoInscricaoVO.setCep(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA V
				coluna++;
				candidatoInscricaoVO.setNumero(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA W
				coluna++;
				candidatoInscricaoVO.setSetor(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA X
				coluna++;
				candidatoInscricaoVO.setEndereco(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA Y
				coluna++;
				candidatoInscricaoVO.setNome(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA Z
				coluna++;
				candidatoInscricaoVO.setNomeSocial(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // TODO Ordenar
				coluna++;
				candidatoInscricaoVO.setCodigoCandidato(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AA
				coluna++;
				candidatoInscricaoVO.setZonaEleitoral(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AB
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataExpedicaoCertificadoMilitar(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataExpedicaoCertificadoMilitar(getValorCelula(coluna, row, true) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, true)), "DD/MM/YYYY") : null); // COLUNA D
				}				
				coluna++;
				candidatoInscricaoVO.setOrgaoExpedidorCertificadoMilitar(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AD
				coluna++;
				candidatoInscricaoVO.setSituacaoMilitar(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AE
				coluna++;
				candidatoInscricaoVO.setBanco(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AF
				coluna++;
				candidatoInscricaoVO.setAgencia(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AG
				coluna++;
				candidatoInscricaoVO.setContaCorrente(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AH
				coluna++;
				candidatoInscricaoVO.setUniversidadeParceira(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AI
				coluna++;
				candidatoInscricaoVO.setModalidadeBolsa(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)).toUpperCase() : ""); // COLUNA AJ
				coluna++;
				candidatoInscricaoVO.setValorBolsa(getValorCelula(coluna, row, true) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? Double.parseDouble(getValorCelula(coluna, row, false).toString()) : null); // COLUNA AK				
//				DADOS INSCRIÇÃO
				coluna++;
				candidatoInscricaoVO.setNumeroInscricao(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AL
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataInscricao(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataInscricao(getValorCelula(coluna, row, true) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, true)), "DD/MM/YYYY") : null); // COLUNA D
				}					
				coluna++;
				candidatoInscricaoVO.setNomeCurso(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // COLUNA AN
				coluna++;
				candidatoInscricaoVO.setIdPolo(getValorCelula(coluna, row, false) != null ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString()) : null); // COLUNA AO				
				if(tipoLayout.equals("layout2")) {
					coluna++;
					candidatoInscricaoVO.setIdPolo2(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString().trim()) : null); // COLUNA AP
					coluna++;
					candidatoInscricaoVO.setIdPolo3(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString().trim()) : null); // COLUNA AQ
					coluna++;
					candidatoInscricaoVO.setIdPolo4(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString().trim()) : null); // COLUNA AR
					coluna++;
					candidatoInscricaoVO.setIdPolo5(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString().trim()) : null); // COLUNA AS			
				}
				coluna++;
				candidatoInscricaoVO.setNomeTurno(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA AT :  COLUNA AP
				coluna++;
				candidatoInscricaoVO.setDescricaoProcessoSeletivo(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA AU :  COLUNA AQ
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataProva(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataProva(getValorCelula(coluna, row, false) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, false)), "DD/MM/YYYY") : null); // COLUNA D
				}					
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setHora(getValorCelula(coluna, row, false) != null ? Uteis.gethoraHHMM(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setHora(getValorCelula(coluna, row, false) != null ? String.valueOf(getValorCelula(coluna, row, false)) : null); // COLUNA D
				}					
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataInicioInscricao(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataInicioInscricao(getValorCelula(coluna, row, false) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, false)), "DD/MM/YYYY") : null); // COLUNA D
				}					
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataTerminoInscricao(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataTerminoInscricao(getValorCelula(coluna, row, false) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, false)), "DD/MM/YYYY") : null); // COLUNA D
				}				
				coluna++;
				candidatoInscricaoVO.setSituacaoInscricao(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA AZ :  COLUNA AV
				coluna++;
				candidatoInscricaoVO.setFormaIngresso(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BA :  COLUNA AW
				coluna++;
				candidatoInscricaoVO.setAno(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BB :  COLUNA AX
				coluna++;
				candidatoInscricaoVO.setSemestre(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BC :  COLUNA AY
				coluna++;
				candidatoInscricaoVO.setSobreBolsasEAuxilios(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2  ? COLUNA BD :  COLUNA AZ
				coluna++;
				candidatoInscricaoVO.setAutodeclaracaoPretoPardoOuIndigena(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BE :  COLUNA BA
				coluna++;
				candidatoInscricaoVO.setEscolaPublica(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BF :  COLUNA BB
				coluna++;
				candidatoInscricaoVO.setClassificacao(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString()) : null); // layout2 ? COLUNA BG :  COLUNA BC
				coluna++;
				candidatoInscricaoVO.setNumeroChamada(getValorCelula(coluna, row, false) != null && !getValorCelula(coluna, row, false).toString().trim().isEmpty() ? (int) Double.parseDouble(getValorCelula(coluna, row, false).toString()) : null); // layout2 ? COLUNA BH :  COLUNA BD
				
//				DADOS RESULTADO PROCSELETIVO
				coluna++;
				if(getValorCelula(coluna, row, false) != null  && row.getCell(coluna).getCellType() == 0) {
					candidatoInscricaoVO.setDataRegistro(getValorCelula(coluna, row, false) != null ? Uteis.getDataJDBC(getValorCelula(coluna, row, false).getDateCellValue()) : null); // COLUNA K
				}else {
					candidatoInscricaoVO.setDataRegistro(getValorCelula(coluna, row, false) != null ? Uteis.getData(String.valueOf(getValorCelula(coluna, row, false)), "DD/MM/YYYY") : null); // COLUNA D
				}				
				coluna++;
				candidatoInscricaoVO.setNotaProcessoSeletivo(getValorCelula(coluna, row, false) != null ? row.getCell(coluna).getCellType() == 1 ? Double.valueOf(String.valueOf(getValorCelula(coluna, row, true)).replace(",", ".")) :  getValorCelula(coluna, row, false).getNumericCellValue() : null); // layout2 ? COLUNA BJ :  COLUNA BG
				coluna++;
				candidatoInscricaoVO.setResultadoProcessoSeletivo(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : ""); // layout2 ? COLUNA BK :  COLUNA BG
				
//				DADOS FORMAÇÃO ACADÊMICA
				if(tipoLayout.equals("layout1")) {
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaCurso(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaTipoIes(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaIes(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaEscolaridade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaAno(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaSemestre(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaCidade(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");
					coluna++;
					candidatoInscricaoVO.setFormacaoAcademicaEstado(getValorCelula(coluna, row, true) != null ? String.valueOf(getValorCelula(coluna, row, true)) : "");					
				}
				}catch (Exception e) {
					throw new Exception("Erro na linha "+(linha+1)+" e coluna "+ CellReference.convertNumToColString(coluna) +" o valor '"+row.getCell(coluna).getStringCellValue()+"' não condiz com o valor esperado.");
				}
				
				validarDadosCandidato(candidatoInscricaoVO, mapaCandidato, false, usuario);
				if(tipoLayout.equals("layout1")) {
					validarDadosFormacaoAcademica(candidatoInscricaoVO, false);					
				}
				
				validarDadosCandidatoNacionalidade(candidatoInscricaoVO, mapNacionalidade, usuario);
				validarDadosCandidatoCidadeENaturalidade(candidatoInscricaoVO, usuario);
				validarDadosExistenciaProcessoSeletivo(candidatoInscricaoVO, mapaProcessoSeletivo, listaProcSeletivoNaoEncontradoVOs, usuario);
				if(tipoLayout.equals("layout1")) {
					validarDadosExistenciaUnidadeEnsino(candidatoInscricaoVO, mapUnidadeEnsino, listaUnidadeEnsinoErroNaoEncontradoVOs, false, usuario);
				}
					
				validarDadosExistenciaCurso(candidatoInscricaoVO, mapCurso, listaCursoNaoEncontradoVOs, false, usuario);
				validarDadosExistenciaTurno(candidatoInscricaoVO, mapTurno, listaTurnoNaoEncontradoVOs, false, usuario);
				if(Uteis.isAtributoPreenchido(candidatoInscricaoVO.getCursoVO()) && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getTurnoVO())) {
					validarDadosInscricao(candidatoInscricaoVO, false, !tipoLayout.equals("layout2") ,usuario);
					validarDadosResultadoProcessoSeletivo(candidatoInscricaoVO, false);
				}
			
				if(tipoLayout.equals("layout2") && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getCursoVO()) && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getTurnoVO())) {
					validarDadosExistenciaUnidadeEnsinoVagasEixoCurso(candidatoInscricaoVO, mapUnidadeEnsino, listaUnidadeEnsinoErroNaoEncontradoVOs,mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis,mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino, false, usuario);
					if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getUnidadeEnsinoVO())){
						String msg =  "Não foi definido nenhuma UNIDADE DE ENSINO ("+candidatoInscricaoVO.getIdPolo()+","+candidatoInscricaoVO.getIdPolo2()+","+candidatoInscricaoVO.getIdPolo3()+","+candidatoInscricaoVO.getIdPolo4()+","+candidatoInscricaoVO.getIdPolo5()+") apta para cadastrar a inscrição, verifique o código das unidade de ensino e o número de vagas.";
						candidatoInscricaoVO.setPossuiErro(true);
						candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - "+msg+" \n" );
						candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
					}
				}
				
				
				
				
				if (candidatoInscricaoVO.getPossuiErro()) {
		    		listaCandidatoInscricaoProcessoSeletivoErroVOs.add(candidatoInscricaoVO);
		    	}
				
				if (candidatoInscricaoVO.getPossuiObservacao()) {
		    		listaCandidatoInscricaoProcessoSeletivoObservacaoVOs.add(candidatoInscricaoVO);
		    	}
				
				if (!pessoaFiltroVO.getNome().equals("")) {
					if (Uteis.removerAcentuacao(candidatoInscricaoVO.getNome()).toUpperCase().contains(Uteis.removerAcentuacao(pessoaFiltroVO.getNome()).toUpperCase())) {
						listaCandidatoInscricaoVOs.add(candidatoInscricaoVO);
						linha++;
					} else {
						linha++;
					}
					
				} else {
					if(!candidatoInscricaoVO.getPossuiErro()) {
						listaCandidatoInscricaoVOs.add(candidatoInscricaoVO);
					}
					linha++;
				}
				progressBarVO.incrementar();		
				
		}
		
//		progressBarVO.setForcarEncerramento(true);
//		progressBarVO.encerrar();
		
		return listaCandidatoInscricaoVOs;
	}
    
    public void validarDadosCandidato(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, ImportarCandidatoInscricaoProcessoSeletivoVO> mapCandidato, Boolean lancarExcessao, UsuarioVO usuario) throws Exception {
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getCPF())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo CPF do Candidato NOME("+candidatoInscricaoVO.getNome()+") - CPF("+candidatoInscricaoVO.getCPF()+") está Nulo.");
    		} else {
    			candidatoInscricaoVO.setErro("CPF Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("CPF Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataNasc())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA DE NASCIMENTO do Candidato NOME("+candidatoInscricaoVO.getNome()+") - CPF("+candidatoInscricaoVO.getCPF()+") está Nulo.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  DATA NASCIMENTO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA NASCIMENTO Nulo. ");
    		}
    		
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getEstadoCivil())) {
    		Hashtable<String, String> estadoCivils = (Hashtable<String, String>) Dominios.getEstadoCivil();
    		Enumeration<String> keys = estadoCivils.keys();    		
    		while (keys.hasMoreElements()) {
    			String value = (String) keys.nextElement();
    			String label = (String) estadoCivils.get(value);    			
    			if(Uteis.removerAcentos(label.replace("(a)", "")).toLowerCase().equals(Uteis.removerAcentos(candidatoInscricaoVO.getEstadoCivil()).trim().toLowerCase())){
    				candidatoInscricaoVO.setEstadoCivil(value);
    				break;
    			}
    		}    		
    		if (!candidatoInscricaoVO.getEstadoCivil().equals("S") && !candidatoInscricaoVO.getEstadoCivil().equals("C") && !candidatoInscricaoVO.getEstadoCivil().equals("V")
    				&& !candidatoInscricaoVO.getEstadoCivil().equals("D") && !candidatoInscricaoVO.getEstadoCivil().equals("A")
    				&& !candidatoInscricaoVO.getEstadoCivil().equals("U") && !candidatoInscricaoVO.getEstadoCivil().equals("E")
    				&& !candidatoInscricaoVO.getEstadoCivil().equals("Q")
    				) {
    			
    			candidatoInscricaoVO.setObservacao(" - Informação contida na coluna ESTADO CIVIL não satisfatória ("+candidatoInscricaoVO.getEstadoCivil()+"). Verificar no modelo padrão as opções.");
    			candidatoInscricaoVO.setPossuiObservacao(true);
    		}
    	}
    	
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getSituacaoMilitar())) {
    		if (!SituacaoMilitarEnum.getExisteValor(candidatoInscricaoVO.getSituacaoMilitar())) {
    			candidatoInscricaoVO.setObservacao(" - Informação contida na coluna SITUAÇÃO MILITAR não satisfatória ("+candidatoInscricaoVO.getSituacaoMilitar()+"). Verificar no modelo padrão as opções.");
    			candidatoInscricaoVO.setPossuiObservacao(true);
    		}
    	}
    	
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getSexo())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo SEXO do Candidato NOME("+candidatoInscricaoVO.getNome()+") - CPF("+candidatoInscricaoVO.getCPF()+") está Nulo.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - SEXO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("SEXO Nulo. ");
    		}
    	} else {
    		if (!candidatoInscricaoVO.getSexo().equals("M") && !candidatoInscricaoVO.getSexo().equals("F")) {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Informação contida na coluna SEXO não satisfatória ("+candidatoInscricaoVO.getSexo()+"). Deve conter M (Masculino) ou F (Feminino)");
    			candidatoInscricaoVO.setPossuiErro(true);
    			candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Informação contida na coluna SEXO não satisfatória ("+candidatoInscricaoVO.getSexo()+"). Deve conter M (Masculino) ou F (Feminino). ");
    		}
    	}
    	
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDeficiencia())) {
    	
			if (!TipoDeficiencia.getExisteValor(candidatoInscricaoVO.getDeficiencia())) {
				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Informação contida na coluna DEFICIENCIA não satisfatória ("+candidatoInscricaoVO.getDeficiencia()+").");
				candidatoInscricaoVO.setPossuiErro(true);
				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Informação contida na coluna DEFICIENCIA não satisfatória ("+candidatoInscricaoVO.getDeficiencia()+"). ");

			}
    	}
    	
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNome())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOME do Candidato de CPF "+candidatoInscricaoVO.getCPF()+" está Nulo.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - NOME Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("NOME Nulo. ");
    		}
    	}
    	
    	if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeSocial())) {
    		candidatoInscricaoVO.setNomeSocial(candidatoInscricaoVO.getNome());
    	}
    	
        String cpfCnpj = Uteis.removerMascara(candidatoInscricaoVO.getCPF());        
        if(cpfCnpj.length() < 11 ) {
        	String cpfFormatado = Uteis.preencherComZerosPosicoesVagas(cpfCnpj, 11);
        	candidatoInscricaoVO.setCPF(cpfFormatado);
        }
    	
    	if (mapCandidato.containsKey(candidatoInscricaoVO.getCPF())) {
    		candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " - CANDIDATO em Duplicidade \n" );
        	candidatoInscricaoVO.setPossuiObservacao(true);
    	} else {
    		mapCandidato.put(candidatoInscricaoVO.getCPF(), candidatoInscricaoVO);
    	}
    	
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataEmissaoRG())  && candidatoInscricaoVO.getDataEmissaoRG().after(new Date())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA EMISSÃO RG do candidato ("+candidatoInscricaoVO.getNome()+") está com data Futura.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - DATA EMISSÃO RG com data Futura \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA EMISSÃO RG com data Futura. ");
    		}
    	}
    	
    	if(Uteis.isAtributoPreenchido(candidatoInscricaoVO.getModalidadeBolsa())) {
    		if(!candidatoInscricaoVO.getModalidadeBolsa().equals("MESTRANDO") && !candidatoInscricaoVO.getModalidadeBolsa().equals("DOUTORANDO") && !Uteis.removerAcentuacao(candidatoInscricaoVO.getModalidadeBolsa()).equalsIgnoreCase("Estagio SEDUC") && !candidatoInscricaoVO.getModalidadeBolsa().equals("NENHUM")) {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Informação contida na coluna Modalidade da Bolsa não satisfatória ("+candidatoInscricaoVO.getModalidadeBolsa()+"). Deve conter MESTRANDO, DOUTORANDO, Estágio SEDUC ou deixar a célula em branco");
    			candidatoInscricaoVO.setPossuiErro(true);
    			candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Informação contida na coluna Modalidade da Bolsa não satisfatória ("+candidatoInscricaoVO.getModalidadeBolsa()+"). Deve conter MESTRANDO, DOUTORANDO, Estágio SEDUC ou deixar a célula em branco. ");
    		}
    	}
    }
    
    public void validarDadosCandidatoNacionalidade(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, PaizVO> mapNacionalidade, UsuarioVO usuario) throws Exception {
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNacionalidade())) {
    		if (!mapNacionalidade.containsKey(candidatoInscricaoVO.getNacionalidade())) {
    			PaizVO paizVO = getFacadeFactory().getPaizFacade().consultarPorNacionalidade(candidatoInscricaoVO.getNacionalidade(), false, usuario);
    			if (Uteis.isAtributoPreenchido(paizVO.getCodigo())) {
    				candidatoInscricaoVO.setNacionalidadeOriginal(paizVO);
    			}else {
    				candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " -  NACIONALIDADE '"+candidatoInscricaoVO.getNacionalidade()+"'  não encontrada \n" );
    				candidatoInscricaoVO.setPossuiObservacao(true);
    			}
    			mapNacionalidade.put(candidatoInscricaoVO.getNacionalidade(), paizVO);
    		} else {
    			PaizVO paizVO = mapNacionalidade.get(candidatoInscricaoVO.getNacionalidade());    			
        		if (Uteis.isAtributoPreenchido(paizVO.getCodigo())) {
        			candidatoInscricaoVO.setNacionalidadeOriginal(paizVO);        			
        		} else {
        			candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " -  NACIONALIDADE '"+candidatoInscricaoVO.getNacionalidade()+"' não encontrada \n" );        			
        			candidatoInscricaoVO.setPossuiObservacao(true);
        		}
    		}
    	}
    }
    
    public void validarDadosCandidatoCidadeENaturalidade(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, UsuarioVO usuario) throws Exception {
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeNaturalidade()) && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getEstadoNaturalidade())) {
    		CidadeVO naturalidadeVO = getFacadeFactory().getCidadeFacade().consultarPorNomeCidadeSiglaEstado(candidatoInscricaoVO.getNomeNaturalidade(), candidatoInscricaoVO.getEstadoNaturalidade(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
    		if (!Uteis.isAtributoPreenchido(naturalidadeVO.getCodigo())) {
    			candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " -  NATURALIDADE '"+candidatoInscricaoVO.getNomeNaturalidade()+"-"+candidatoInscricaoVO.getEstadoNaturalidade()+"' não encontrada \n" );
    			candidatoInscricaoVO.setPossuiObservacao(true);
    		} else {
    			candidatoInscricaoVO.setNaturalidadeOriginal(naturalidadeVO);
    		}
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeCidade()) && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getEstadoCidade())) {
    		CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorNomeCidadeSiglaEstado(candidatoInscricaoVO.getNomeCidade(), candidatoInscricaoVO.getEstadoCidade(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
    		if (!Uteis.isAtributoPreenchido(cidadeVO.getCodigo())) {
    			candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " -  CIDADE '"+candidatoInscricaoVO.getNomeCidade()+" - "+candidatoInscricaoVO.getEstadoCidade()+"' não encontrada \n" );
    			candidatoInscricaoVO.setPossuiObservacao(true);
    		} else {
    			candidatoInscricaoVO.setCidadeOriginal(cidadeVO);
    		}
    	}
    }
    
    public void validarDadosInscricao(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Boolean lancarExcessao,  Boolean validarSomenteUmPolo  ,UsuarioVO usuarioVO) throws Exception {
    	if (!lancarExcessao) {
    		
    		if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getProcSeletivoVO().getCodigo())) {
    			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorProcSeletivoECPF(candidatoInscricaoVO.getProcSeletivoVO().getCodigo(), candidatoInscricaoVO.getCPF(), candidatoInscricaoVO.getUnidadeEnsinoVO().getCodigo(), candidatoInscricaoVO.getCursoVO().getCodigo(), candidatoInscricaoVO.getTurnoVO().getCodigo(),  Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
    			if (Uteis.isAtributoPreenchido(inscricaoVO.getCodigo())) {
    				candidatoInscricaoVO.setInscricaoExistente(true);
    				candidatoInscricaoVO.setSelecionado(false);
    				return;
    			}
    		}
    	}
    	
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataInscricao())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA INSCRIÇÃO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - DATA INSCRIÇÃO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA INSCRIÇÃO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeCurso())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOME CURSO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - NOME CURSO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("NOME CURSO Nulo. ");
    		}
    		
    	}
    	
		if (validarSomenteUmPolo  && !Uteis.isAtributoPreenchido(candidatoInscricaoVO.getIdPolo())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo ID POLO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - ID PÓLO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("NOME PÓLO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeTurno())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOME TURNO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - NOME TURNO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("NOME TURNO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDescricaoProcessoSeletivo())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DESCRIÇÃO PROCESSO SELETIVO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  DESCRIÇÃO PROCESSO SELETIVO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DESCRIÇÃO PROCESSO SELETIVO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataProva())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA PROVA (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - DATA PROVA Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA PROVA Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getHora())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo HORA (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  HORA Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("HORA Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataInicioInscricao())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA INÍCIO INSCRIÇÃO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  DATA INÍCIO INSCRIÇÃO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA INÍCIO INSCRIÇÃO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataTerminoInscricao())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo DATA TÉRMINO INSCRIÇÃO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  DATA TÉRMINO INSCRIÇÃO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA TÉRMINO INSCRIÇÃO Nulo. ");
    		}
    		
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataInscricao()) && Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataInicioInscricao())
    			&& Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDataTerminoInscricao())) {
    		
    		if ((candidatoInscricaoVO.getDataInscricao().compareTo(candidatoInscricaoVO.getDataInicioInscricao()) < 0 ||
        			candidatoInscricaoVO.getDataInscricao().compareTo(candidatoInscricaoVO.getDataTerminoInscricao()) > 0)
    				&& !Uteis.getData(candidatoInscricaoVO.getDataInscricao(), "dd/MM/yyyy").equals(Uteis.getData(candidatoInscricaoVO.getDataInicioInscricao(), "dd/MM/yyyy"))
    				&& !Uteis.getData(candidatoInscricaoVO.getDataInscricao(), "dd/MM/yyyy").equals(Uteis.getData(candidatoInscricaoVO.getDataTerminoInscricao(), "dd/MM/yyyy"))) {
        		if (lancarExcessao) {
        			throw new Exception("A Data de Inscrição do candidato "+candidatoInscricaoVO.getNome().toUpperCase()+" não está entre os períodos de inscrição do Processo Seletivo. Data de Inscrição( "+Uteis.getDataAno4Digitos(candidatoInscricaoVO.getDataInscricao())+"), Data Início Inscrição "+Uteis.getDataAno4Digitos(candidatoInscricaoVO.getDataInicioInscricao())+", Data Fim Inscrição "+Uteis.getDataAno4Digitos(candidatoInscricaoVO.getDataTerminoInscricao())+"");
        		} else {
        			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  DATA INSCRIÇÃO não está entre os períodos de inscrição do processo seletivo \n" );
            		candidatoInscricaoVO.setPossuiErro(true);
            		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("DATA INSCRIÇÃO não está entre os períodos de inscrição do processo seletivo. ");
        		}
    		}
    	}
    	
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getSituacaoInscricao())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo SITUAÇÃO INSCRIÇÃO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  SITUAÇÃO INSCRIÇÃO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("SITUAÇÃO INSCRIÇÃO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getFormaIngresso())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo FORMA INGRESSO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  FORMA INGRESSO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("FORMA INGRESSO Nulo. ");
    		}
    		
    	} else {
    		if (!candidatoInscricaoVO.getFormaIngresso().equals("EN") && !candidatoInscricaoVO.getFormaIngresso().equals("PD")
    				&& !candidatoInscricaoVO.getFormaIngresso().equals("TR") && !candidatoInscricaoVO.getFormaIngresso().equals("PS")) {
    			candidatoInscricaoVO.setObservacao(" - Informação contida na coluna FORMA INGRESSO não satisfatória ("+candidatoInscricaoVO.getFormaIngresso()+"). Verificar no modelo padrão as opções.");
    			candidatoInscricaoVO.setPossuiObservacao(true);
    		}
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getAno())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo ANO (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  ANO Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("ANO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getSemestre())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo SEMESTRE (INSCRIÇÃO) está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - SEMESTRE Nulo \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("SEMESTRE Nulo. ");
    		}
    	}
    	if (candidatoInscricaoVO.getResultadoProcessoSeletivo().equals("RE") && candidatoInscricaoVO.getNumeroChamada() > 0) {
    		if (lancarExcessao) {
    			throw new Exception("A inscrição do candidato ("+candidatoInscricaoVO.getNome().toUpperCase()+") está Reprovado e não é possível fazer a chamada do mesmo.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Candidato Reprovado com Chamada \n" );
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Candidato Reprovado com Chamada. ");
    		}
    	}
    	
    }
    
    public void validarDadosResultadoProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Boolean lancarExcessao) throws Exception {
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNotaProcessoSeletivo())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOTA PROCESSO SELETIVO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " NOTA PROCESSO SELETIVO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("NOTA PROCESSO SELETIVO Nulo. ");
    		}
    		
    	}
    	if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getResultadoProcessoSeletivo())) {
    		if (lancarExcessao) {
    			throw new Exception("O campo NOTA PROCESSO SELETIVO está Nulo");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " RESULTADO PROCESSO SELETIVO Nulo \n");
        		candidatoInscricaoVO.setPossuiErro(true);
        		candidatoInscricaoVO.getListaMotivoErrosImportacao().add("RESULTADO PROCESSO SELETIVO Nulo. ");
    		}
    		
    	}
    }
    
    public void validarDadosFormacaoAcademica(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Boolean lancarExcessao) throws Exception {
    	if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getFormacaoAcademicaCurso())) {
    		if(lancarExcessao) {
    			throw new Exception("O campo FORMAÇÃO ACADÊMICA CURSO não informado.");
    		} else {
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " FORMAÇÃO ACADÊMICA CURSO não informado. \n");
    			candidatoInscricaoVO.setPossuiErro(true);
    			candidatoInscricaoVO.getListaMotivoErrosImportacao().add("FORMAÇÃO ACADÊMICA CURSO não informado. ");
    		}
    	} else {
    		if (!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getFormacaoAcademicaEscolaridade())) {
    			if(lancarExcessao) {
    				throw new Exception("O campo FORMAÇÃO ACADÊMICA ESCOLARIDADE não informado.");
    			} else {
    				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " FORMAÇÃO ACADÊMICA ESCOLARIDADE não informada. \n");
        			candidatoInscricaoVO.setPossuiErro(true);
        			candidatoInscricaoVO.getListaMotivoErrosImportacao().add("FORMAÇÃO ACADÊMICA ESCOLARIDADE não informada. ");
    			}
    		} else {
    			if (!(candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.INFANTIL.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.FUNDAMENTAL.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.MEDIO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.TECNICO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.EXTENSAO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.GRADUACAO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.ESPECIALIZACAO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.POS_GRADUACAO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.MESTRADO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.DOUTORADO.getValor())
    				|| candidatoInscricaoVO.getFormacaoAcademicaEscolaridade().equals(NivelFormacaoAcademica.POS_DOUTORADO.getValor()))
    				) {
    				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Informação contida na coluna FORMAÇÃO ACADÊMICA ESCOLARIDADE não satisfatória ("+candidatoInscricaoVO.getFormacaoAcademicaEscolaridade()+").");
    				candidatoInscricaoVO.setPossuiErro(true);
    				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Informação contida na coluna FORMAÇÃO ACADÊMICA ESCOLARIDADE não satisfatória ("+candidatoInscricaoVO.getFormacaoAcademicaEscolaridade()+").");
    			} 
    		}
    	}
    	
    	if(Uteis.isAtributoPreenchido(candidatoInscricaoVO.getFormacaoAcademicaAno())) {
    		if(!candidatoInscricaoVO.getFormacaoAcademicaAno().matches("[0-9]+")){
    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - Informação contida na coluna FORMAÇÃO ACADÊMICA ANO não satisfatória ("+candidatoInscricaoVO.getFormacaoAcademicaAno()+").");
				candidatoInscricaoVO.setPossuiErro(true);
				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("Informação contida na coluna FORMAÇÃO ACADÊMICA ANO não satisfatória ("+candidatoInscricaoVO.getFormacaoAcademicaAno()+").");
    		}
    	}
    	
    }
    
    public void validarDadosExistenciaUnidadeEnsino(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsino, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, Boolean lancarExcessao   ,UsuarioVO usuario) throws Exception {
    	if (mapUnidadeEnsino == null) {
    		return;
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getIdPolo())) {
    		if (!mapUnidadeEnsino.containsKey(candidatoInscricaoVO.getIdPolo())) {
    			
    			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(candidatoInscricaoVO.getIdPolo(), usuario);
        			
        			if (!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
        				if (lancarExcessao) {
        					throw new Exception("Não foi encontrado UNIDADE DE ENSINO com ID do Polo + "+candidatoInscricaoVO.getIdPolo()+"");
        				} else {
        					unidadeEnsinoVO.setCodigo(candidatoInscricaoVO.getIdPolo());
            				listaUnidadeEnsinoVOs.add(unidadeEnsinoVO);
            				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  UNIDADE DE ENSINO ("+candidatoInscricaoVO.getIdPolo()+") não encontrada \n" );
            				candidatoInscricaoVO.setPossuiErro(true);
            				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("UNIDADE DE ENSINO ("+candidatoInscricaoVO.getIdPolo()+") não encontrada. ");

        				}
        			} else {
        				candidatoInscricaoVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
        			}
        		mapUnidadeEnsino.put(candidatoInscricaoVO.getIdPolo(), unidadeEnsinoVO);
    		} else {
    			UnidadeEnsinoVO unidadeEnsinoVO = mapUnidadeEnsino.get(candidatoInscricaoVO.getIdPolo());
    			if (!Uteis.isAtributoPreenchido(unidadeEnsinoVO.getNome())) {
    				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  UNIDADE DE ENSINO ("+candidatoInscricaoVO.getIdPolo()+") não encontrada \n" );
    				candidatoInscricaoVO.setPossuiErro(true);
    				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("UNIDADE DE ENSINO ("+candidatoInscricaoVO.getIdPolo()+") não encontrada. ");
    			}
    			candidatoInscricaoVO.setUnidadeEnsinoVO(mapUnidadeEnsino.get(candidatoInscricaoVO.getIdPolo()));
    		}
    	}
    }
    
    public void validarDadosExistenciaCurso(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, CursoVO> mapCurso, List<CursoVO> listaCursoVOs, Boolean lancarExcessao, UsuarioVO usuario) throws Exception {
    	if (mapCurso == null) {
    		return;
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeCurso())) {
    		String msg = null;
    		if (!mapCurso.containsKey(candidatoInscricaoVO.getNomeCurso())) {
    			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorNome(candidatoInscricaoVO.getNomeCurso(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        		if (!Uteis.isAtributoPreenchido(cursoVO.getCodigo())) {
        			if (lancarExcessao) {
        				throw new Exception("Não foi encontrado CURSO com o nome "+candidatoInscricaoVO.getNomeCurso()+"");
        			} else {
        				cursoVO.setNome(candidatoInscricaoVO.getNomeCurso());
            			listaCursoVOs.add(cursoVO);  
            			msg =  "CURSO "+candidatoInscricaoVO.getNomeCurso().toUpperCase()+" não cadastrado no sistema.";
            			candidatoInscricaoVO.setPossuiErro(true);
            			if(!candidatoInscricaoVO.getErro().contains(msg)){
            			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+" \n" );
            			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
            			}
        			}
        		} else {
        			candidatoInscricaoVO.setCursoVO(cursoVO);
        		}
        		mapCurso.put(candidatoInscricaoVO.getNomeCurso(), cursoVO);
        		
    		} else {
    			CursoVO cursoVO = mapCurso.get(candidatoInscricaoVO.getNomeCurso());
    			if (!Uteis.isAtributoPreenchido(cursoVO.getCodigo())) {
    				msg =  "CURSO "+candidatoInscricaoVO.getNomeCurso().toUpperCase()+" não cadastrado no sistema.";
        			candidatoInscricaoVO.setPossuiErro(true);
        			if(!candidatoInscricaoVO.getErro().contains(msg)){
        				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+" \n" );
        				candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
        			}
    			}
    			candidatoInscricaoVO.setCursoVO(mapCurso.get(candidatoInscricaoVO.getNomeCurso()));
    		}
    	}
    }
    
    public void validarDadosExistenciaTurno(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, TurnoVO> mapTurno, List<TurnoVO> listaTurnoVOs, Boolean lancarExcessao, UsuarioVO usuario) throws Exception {
    	if (mapTurno == null) {
    		return;
    	}
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getNomeTurno())) {
    		
    		String msg = null;
    		if (!mapTurno.containsKey(candidatoInscricaoVO.getNomeTurno())) {
    			TurnoVO turnoVO = getFacadeFactory().getTurnoFacade().consultarTurnoPorNomeUnico(candidatoInscricaoVO.getNomeTurno(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        		if (!Uteis.isAtributoPreenchido(turnoVO.getCodigo())) {
        			if (lancarExcessao) {
        				throw new Exception("Não foi encontrado TURNO com o nome "+candidatoInscricaoVO.getNomeTurno()+"");
        			} else {
        				turnoVO.setNome(candidatoInscricaoVO.getNomeTurno());
            			listaTurnoVOs.add(turnoVO);            			
            			msg =  "TURNO "+candidatoInscricaoVO.getNomeTurno().toUpperCase()+" não cadastrado no sistema.";
            			candidatoInscricaoVO.setPossuiErro(true);
            			if(!candidatoInscricaoVO.getErro().contains(msg)){
            				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+" \n" );
            				candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
            			}
        			}
        		} else {
        			candidatoInscricaoVO.setTurnoVO(turnoVO);
        		}
        		mapTurno.put(candidatoInscricaoVO.getNomeTurno(), turnoVO);
    		} else {
    			TurnoVO turnoVO = mapTurno.get(candidatoInscricaoVO.getNomeTurno());
    			if (!Uteis.isAtributoPreenchido(turnoVO.getCodigo())) {
    				msg =  "TURNO "+candidatoInscricaoVO.getNomeTurno().toUpperCase()+" não cadastrado no sistema.";
        			candidatoInscricaoVO.setPossuiErro(true);
        			if(!candidatoInscricaoVO.getErro().contains(msg)){
        				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+" \n" );
        				candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
        			}    				
    			}
    			candidatoInscricaoVO.setTurnoVO(mapTurno.get(candidatoInscricaoVO.getNomeTurno()));
    		}
    	}
    }
    
    public void validarDadosExistenciaProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, ProcSeletivoVO> mapProcessoSeletivo, List<ProcSeletivoVO> listaProcSeletivoVOs, UsuarioVO usuario) throws Exception {
    	if (Uteis.isAtributoPreenchido(candidatoInscricaoVO.getDescricaoProcessoSeletivo())) {
    		if (!mapProcessoSeletivo.containsKey(candidatoInscricaoVO.getDescricaoProcessoSeletivo())) {
        		
        		ProcSeletivoVO procSeletivoVO = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoExata(candidatoInscricaoVO.getDescricaoProcessoSeletivo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            	if (!Uteis.isAtributoPreenchido(procSeletivoVO)) {
            		procSeletivoVO.setDescricao(candidatoInscricaoVO.getDescricaoProcessoSeletivo());
            		listaProcSeletivoVOs.add(procSeletivoVO);
            		candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " - PROCESSO SELETIVO "+candidatoInscricaoVO.getDescricaoProcessoSeletivo()+" não encontrado \n\r");
            		candidatoInscricaoVO.setPossuiObservacao(true);
            	} else {
            		procSeletivoVO.setProcSeletivoUnidadeEnsinoVOs(ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(procSeletivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
            		candidatoInscricaoVO.setProcSeletivoVO(procSeletivoVO);
            	}
            	mapProcessoSeletivo.put(candidatoInscricaoVO.getDescricaoProcessoSeletivo(), procSeletivoVO);
            	
        	} else {
        		ProcSeletivoVO procSeletivoVO = mapProcessoSeletivo.get(candidatoInscricaoVO.getDescricaoProcessoSeletivo());
        		if (!Uteis.isAtributoPreenchido(procSeletivoVO.getCodigo())) {
        			candidatoInscricaoVO.setObservacao(candidatoInscricaoVO.getObservacao() + " - PROCESSO SELETIVO "+candidatoInscricaoVO.getDescricaoProcessoSeletivo()+" não encontrado \n\r");
            		candidatoInscricaoVO.setPossuiObservacao(true);
        		}
        		candidatoInscricaoVO.setProcSeletivoVO(mapProcessoSeletivo.get(candidatoInscricaoVO.getDescricaoProcessoSeletivo()));
        	}
    		
    	}
    	String msg = null;
		if (candidatoInscricaoVO.getDataInicioInscricao() != null && candidatoInscricaoVO.getDataProva() != null && candidatoInscricaoVO.getDataInicioInscricao().after(candidatoInscricaoVO.getDataProva())) {
			msg = ("O campo DATA INÍCIO INSCRIÇÃO (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataInicioInscricao()) + ") deve ser menor que a DATA DA PROVA (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataProva()) + ").");
			candidatoInscricaoVO.setPossuiErro(true);
			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro()+" - "+msg+" \n");
			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
		}
		if (candidatoInscricaoVO.getDataInicioInscricao() != null && candidatoInscricaoVO.getDataTerminoInscricao() != null && candidatoInscricaoVO.getDataInicioInscricao().after(candidatoInscricaoVO.getDataTerminoInscricao())) {
			msg = ("O campo DATA INÍCIO INSCRIÇÃO (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataInicioInscricao()) + ") deve ser menor que a DATA DE TÉRMINO (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataTerminoInscricao()) + ").");
			candidatoInscricaoVO.setPossuiErro(true);
			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro()+" - "+msg+" \n");
			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
		}
		if (candidatoInscricaoVO.getDataTerminoInscricao() != null &&  candidatoInscricaoVO.getDataProva() != null && candidatoInscricaoVO.getDataTerminoInscricao().after(candidatoInscricaoVO.getDataProva())) {
			
			msg = ("O campo DATA TÉRMINO INSCRIÇÃO (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataTerminoInscricao()) + ") deve ser deve menor que a DATA DA PROVA (" + Uteis.getDataComHora(candidatoInscricaoVO.getDataProva()) + ") .");
			candidatoInscricaoVO.setPossuiErro(true);
			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro()+" - "+msg+" \n");
			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
		}
    }
	
	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}
	
	public void validarDadosCabecalhoExcelTabela(Row row,String tipoLayout) throws Exception {
		int index = 0;
		if (row.getCell(index) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("corraca")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo COR/RAÇA, favor informe um título com a descrição \"corRaca\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("tituloeleitoral")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo TITULO ELEITORAL, favor informe um título com a descrição \"tituloEleitoral\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("orgaoemissor")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ORGÃO EMISSOR, favor informe um título com a descrição \"orgaoEmissor\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("estadoemissaorg")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ESTADO EMISSOR RG, favor informe um título com a descrição \"estadoEmissaoRG\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("dataemissaorg")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo DATA EMISSÃO RG, favor informe um título com a descrição \"dataEmissaoRG\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("certificadomilitar")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ORGÃO EMISSOR, favor informe um título com a descrição \"certificadoMilitar\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("rg")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo RG, favor informe um título com a descrição \"RG\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("cpf")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo CPF, favor informe um título com a descrição \"CPF\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nacionalidade")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NACIONALIDADE, favor informe um título com a descrição \"nacionalidade\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nomenaturalidade")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"'v deve ser referente ao campo NOME NATURALIDADE, favor informe um título com a descrição \"nomeNaturalidade\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("estadonaturalidade")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ESTADO NATURALIDADE, favor informe um título com a descrição \"estadoNaturalidade\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("datanasc")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"'v deve ser referente ao campo DATA NASCIMENTO, favor informe um título com a descrição \"dataNasc\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("email")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo E-MAIL, favor informe um título com a descrição \"email\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("celular")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo CELULAR, favor informe um título com a descrição \"celular\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("telefonerecado")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo TELEFONE RECADO, favor informe um título com a descrição \"telefoneRecado\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("telefoneres")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo TELEFONE RESIDENCIAL, favor informe um título com a descrição \"telefoneRes\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("estadocivil")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ESTADO CIVIL, favor informe um título com a descrição \"estadoCivil\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("sexo")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ESTADO CIVIL, favor informe um título com a descrição \"sexo\".");
		}
		if (row.getCell(index++) == null || (!row.getCell(index).getStringCellValue().toLowerCase().equals("deficiência") && !row.getCell(index).getStringCellValue().toLowerCase().equals("deficiencia"))) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo DEFICIÊNCIA, favor informe um título com a descrição \"deficiência\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nomecidade")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NOME CIDADE, favor informe um título com a descrição \"nomeCidade\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("estadocidade")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ESTADO CIDADE, favor informe um título com a descrição \"estadoCidade\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("complemento")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo COMPLEMENTO, favor informe um título com a descrição \"complemento\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("cep")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo CEP, favor informe um título com a descrição \"cep\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("numero")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NÚMERO, favor informe um título com a descrição \"numero\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("setor")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo SETOR, favor informe um título com a descrição \"setor\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("endereco")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ENDEREÇO, favor informe um título com a descrição \"endereco\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nome")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NOME, favor informe um título com a descrição \"nome\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nomesocial")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NOME SOCIAL, favor informe um título com a descrição \"nomeSocial\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("codigocandidato")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo CÓDIGO CANDIDATO, favor informe um título com a descrição \"codigoCandidato\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("zonaeleitoral")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ZONA ELEITORAL, favor informe um título com a descrição \"zonaEleitoral\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("dataexpedicaocertificadomilitar")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo DATA EXPEDIÇÃO CERTIFICADO MILITAR, favor informe um título com a descrição \"dataExpedicaoCertificadoMilitar\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("orgaoexpedidorcertificadomilitar")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ORGÃO EXPEDIDOR CERTIFICADO MILITAR, favor informe um título com a descrição \"orgaoExpedidorCertificadoMilitar\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("situacaomilitar")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo SITUAÇÃO MILITAR, favor informe um título com a descrição \"situacaoMilitar\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("banco")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo BANCO, favor informe um título com a descrição \"banco\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("agencia")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Agência, favor informe um título com a descrição \"agencia\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("contacorrente")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Conta Corrente, favor informe um título com a descrição \"contacorrente\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("universidadeparceira")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Universidade Parceira, favor informe um título com a descrição \"universidadeParceira\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("modalidadedabolsa")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Modalidade da Bolsa, favor informe um título com a descrição \"modalidadeBolsa\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("valordabolsa")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo Valor da Bolsa, favor informe um título com a descrição \"valorBolsa\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("numeroinscricao")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NÚMERO INSCRIÇÃO, favor informe um título com a descrição \"numeroInscricao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("datainscricao")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo DATA INSCRIÇÃO, favor informe um título com a descrição \"dataInscricao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nomecurso")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo NOME CURSO, favor informe um título com a descrição \"nomeCurso\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo")) {
			throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ID POLO, favor informe um título com a descrição \"idPolo\".");
		}
		if(tipoLayout.equals("layout2")){
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo2")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ID POLO 2, favor informe um título com a descrição \"idPolo2\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo3")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ID POLO 3, favor informe um título com a descrição \"idPolo3\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo4")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ID POLO 4, favor informe um título com a descrição \"idPolo4\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo5")) {
				throw new Exception("A coluna '"+CellReference.convertNumToColString(index)+"' da linha 2 deve ser referente ao campo ID POLO 5, favor informe um título com a descrição \"idPolo5\".");
			}
			
		}
		
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("nometurno")) {
			if(row.getCell(index).getStringCellValue().toLowerCase().equals("idpolo2")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index) +" da linha 2 deve ser referente ao campo NOME TURNO, favor informe um título com a descrição \"nomeTurno\", ou verifique o layout escolhido.");
			}
			throw new Exception("A coluna " +CellReference.convertNumToColString(index) +" da linha 2 deve ser referente ao campo NOME TURNO, favor informe um título com a descrição \"nomeTurno\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("descricaoprocessoseletivo")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index) +" da linha 2  deve ser referente ao campo DESCRIÇÃO PROCESSO SELETIVO, favor informe um título com a descrição \"descricaoProcessoSeletivo\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("dataprova")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo DATA PROVA, favor informe um título com a descrição \"dataProva\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("hora")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo HORA, favor informe um título com a descrição \"hora\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("datainicioinscricao")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo DATA INÍCIO INSCRIÇÃO, favor informe um título com a descrição \"dataInicioInscricao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("dataterminoinscricao")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo DATA TERMINO INSCRIÇÃO, favor informe um título com a descrição \"dataTerminoInscricao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("situacaoinscricao")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo SITUAÇÃO INSCRIÇÃO, favor informe um título com a descrição \"situacaoInscricao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formaingresso")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo FORMA INGRESSO, favor informe um título com a descrição \"formaIngresso\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("ano")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo ANO, favor informe um título com a descrição \"ano\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("semestre")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo SEMESTRE, favor informe um título com a descrição \"semestre\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("sobrebolsaseauxilios")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo SOBRE BOLSAS E AUXÍLIOIS, favor informe um título com a descrição \"sobreBolsasEAuxilios\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("autodeclaracaopretopardoouindigena")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo AUTO DECLARAÇÃO PRETO, PARDO OU INDÍGENA, favor informe um título com a descrição \"autodeclaracaoPretoPardoOuIndigena\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("escolapublica")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Escola Pública, favor informe um título com a descrição \"escolaPublica\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("classificacao")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo CLASSIFICAÇÃO, favor informe um título com a descrição \"classificacao\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("numerochamada")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo NÚMERO CHAMADA, favor informe um título com a descrição \"numeroChamada\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("dataregistro")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo DATA REGISTRO, favor informe um título com a descrição \"dataRegistro\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("notaprocessoseletivo")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo NOTA PROCESSO SELETIVO, favor informe um título com a descrição \"notaProcessoSeletivo\".");
		}
		if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("resultadoprocessoseletivo")) {
			throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo RESULTADO PROCESSO SELETIVO, favor informe um título com a descrição \"resultadoProcessoSeletivo\".");
		}
		if (tipoLayout.equals("layout1")) {
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicacurso")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Curso, favor informe um título com a descrição \"formacaoacademicacurso\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicatipoies")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Tipo IES, favor informe um título com a descrição \"formacaoacademicatipoies\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicaies")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica IES, favor informe um título com a descrição \"formacaoacademicaies\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicaescolaridade")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Escolaridade, favor informe um título com a descrição \"formacaoacademicaescolaridade\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicaano")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Ano, favor informe um título com a descrição \"formacaoacademicaano\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicasemestre")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Semestre, favor informe um título com a descrição \"formacaoacademicasemestre\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicacidade")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Cidade, favor informe um título com a descrição \"formacaoacademicacidade\".");
			}
			if (row.getCell(index++) == null || !row.getCell(index).getStringCellValue().toLowerCase().equals("formacaoacademicaestado")) {
				throw new Exception("A coluna " +CellReference.convertNumToColString(index)+" da linha 2 deve ser referente ao campo Formação Acadêmica Estado, favor informe um título com a descrição \"formacaoacademica\".");
			}
		}
	}

    public static String getIdEntidade() {
        return ImportarCandidatoInscricaoProcessoSeletivo.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
    	ImportarCandidatoInscricaoProcessoSeletivo.idEntidade = idEntidade;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(ImportarCandidatoInscricaoProcessoSeletivoVO obj, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaImportarCandidatoInscricaoProcessoSeletivoVOs, boolean validarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ProgressBarVO progressBarVO, String tipoLayout , UsuarioVO usuarioVO) throws Exception {
    	String nomeCandidato = "";
    	String cpfCandidato = "";
    	if (listaImportarCandidatoInscricaoProcessoSeletivoVOs.isEmpty()) {
    		throw new Exception("Não foi Encontrado nenhum candidato na lista. Favor Selecionar o Arquivo para importação dos dados.");
    	}
    	Map<String, ProcSeletivoVO> mapProcSeletivoVOs = new HashMap<String, ProcSeletivoVO>(0);
    	List<InscricaoVO> listaInscricaoVOs = new ArrayList<InscricaoVO>(0);
    	Map<String, ResultadoProcessoSeletivoVO> mapResultadoProcessoSeletivoVOs = new HashMap<String, ResultadoProcessoSeletivoVO>();
    	Map<String, ImportarCandidatoInscricaoProcessoSeletivoVO> mapCandidatoVOs = new HashMap<String, ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
    	Map<String, ItemProcSeletivoDataProvaVO> mapItemProcSeltivoDataProvaVOs = new HashMap<String, ItemProcSeletivoDataProvaVO>(0);
    	Map<String, ProcSeletivoUnidadeEnsinoVO> mapProcSeletivoUnidadeEnsinoVOs = new HashMap<String, ProcSeletivoUnidadeEnsinoVO>(0);

    	try {
    	for (ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO : listaImportarCandidatoInscricaoProcessoSeletivoVOs) {
    		
    			
    			if (progressBarVO.getForcarEncerramento()) {
					throw new Exception("Encerramento forçado pelo usuário.");
				}
    			progressBarVO.setStatus("Gerando inscrição do candidato CPF("+ importarCandidatoVO.getCPF()+") - Nome("+importarCandidatoVO.getNome().toUpperCase()+") ");
    			validarDados(importarCandidatoVO, mapCandidatoVOs, !tipoLayout.equals("layout2"),  usuarioVO);
    			
    			nomeCandidato = importarCandidatoVO.getNome();
    			cpfCandidato = importarCandidatoVO.getCPF();
    			
    			if (!Uteis.isAtributoPreenchido(importarCandidatoVO.getProcSeletivoVO().getCodigo())) {
    				if (!mapProcSeletivoVOs.containsKey(importarCandidatoVO.getDescricaoProcessoSeletivo())) {
    					ProcSeletivoVO procSeletivoVO = getFacadeFactory().getProcSeletivoFacade().inicializarDadosProcSeletivoImportacaoCandidatoInscricao(importarCandidatoVO, usuarioVO);
        				importarCandidatoVO.setProcSeletivoVO(procSeletivoVO);
        				mapProcSeletivoVOs.put(importarCandidatoVO.getDescricaoProcessoSeletivo(), importarCandidatoVO.getProcSeletivoVO());
    				} else {
    					ProcSeletivoVO procSeletivoVO = mapProcSeletivoVOs.get(importarCandidatoVO.getDescricaoProcessoSeletivo());
    					if (procSeletivoVO.getDataInicio().after(importarCandidatoVO.getDataInicioInscricao())) {
    						procSeletivoVO.setDataInicio(importarCandidatoVO.getDataInicioInscricao());
    					}
    					importarCandidatoVO.setProcSeletivoVO(procSeletivoVO);
    				}
    			} else {
    				if (!mapProcSeletivoVOs.containsKey(importarCandidatoVO.getDescricaoProcessoSeletivo())) {
    					mapProcSeletivoVOs.put(importarCandidatoVO.getDescricaoProcessoSeletivo(), importarCandidatoVO.getProcSeletivoVO());
    				}
    			}
    			
    			PessoaVO candidatoVO = getFacadeFactory().getPessoaFacade().inicializarDadosCandidatoImportacaoCandidatoInscricaoProcessoSeletivo(importarCandidatoVO, configuracaoGeralSistemaVO, usuarioVO);
    			ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO = getFacadeFactory().getItemProcSeletivoDataProvaFacade().inicializarDadosItemImportacaoCandidatoInscricao(importarCandidatoVO, importarCandidatoVO.getProcSeletivoVO(), mapItemProcSeltivoDataProvaVOs, usuarioVO);
    			InscricaoVO inscricaoVO = null;
    			if (itemProcSeletivoDataProvaVO != null) {
    				inscricaoVO = getFacadeFactory().getInscricaoFacade().inicializarDadosInscricaoImportacaoCandidato(importarCandidatoVO, importarCandidatoVO.getProcSeletivoVO(), itemProcSeletivoDataProvaVO, candidatoVO, usuarioVO);
    			} else {
    				ItemProcSeletivoDataProvaVO itemDataProva = mapItemProcSeltivoDataProvaVOs.get(importarCandidatoVO.getProcSeletivoVO().getDescricao().toString() +"."+UteisData.getDataAno4Digitos(importarCandidatoVO.getDataProva()) +"."+ importarCandidatoVO.getHora());
    				inscricaoVO = getFacadeFactory().getInscricaoFacade().inicializarDadosInscricaoImportacaoCandidato(importarCandidatoVO, importarCandidatoVO.getProcSeletivoVO(), itemDataProva, candidatoVO, usuarioVO);
    			}
    			ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO = getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().inicializarDadosImportarCandidatoInscricaoProcSeletivo(importarCandidatoVO, importarCandidatoVO.getProcSeletivoVO(), mapProcSeletivoUnidadeEnsinoVOs, usuarioVO);
    			ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = getFacadeFactory().getResultadoProcessoSeletivoFacade().inicializarDadosResultadoImportarCandidatoInscricao(importarCandidatoVO, inscricaoVO, usuarioVO);

    			ProcSeletivoVO procSeletivoVO = mapProcSeletivoVOs.get(importarCandidatoVO.getDescricaoProcessoSeletivo());
    			
    			if (itemProcSeletivoDataProvaVO != null) {
    				procSeletivoVO.getItemProcSeletivoDataProvaVOs().add(itemProcSeletivoDataProvaVO);
    			}
    			if (procSeletivoUnidadeEnsinoVO != null) {
    				procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs().add(procSeletivoUnidadeEnsinoVO);
    			}    			
    							
    			listaInscricaoVOs.add(inscricaoVO);
    			mapResultadoProcessoSeletivoVOs.put(candidatoVO.getCPF(), resultadoProcessoSeletivoVO);
    			progressBarVO.incrementar();
    			
    		}
    	progressBarVO.setStatus("Gravando informações do processo seletivo");
    	persistirProcessoSeletivo(mapProcSeletivoVOs, usuarioVO);
    	progressBarVO.incrementar();
    	progressBarVO.setStatus("Gravando inscrições");
    	persistirInscricao(listaInscricaoVOs, mapResultadoProcessoSeletivoVOs, usuarioVO, progressBarVO);
    	progressBarVO.setStatus("Fechando processamento do arquivo");
    	incluir(obj, validarAcesso, configuracaoGeralSistemaVO, usuarioVO);
    	progressBarVO.incrementar();
    	
    	}catch (Exception e) {
    		listaImportarCandidatoInscricaoProcessoSeletivoVOs.forEach(i -> i.getProcSeletivoVO().setCodigo(0));
//    		System.out.println("Nome Candidato Erro ("+nomeCandidato+"), CPF candidato ("+cpfCandidato+")");
    		if(e instanceof SQLException || e instanceof DataIntegrityViolationException) {
    			throw new Exception("Erro Nome Candidato Erro ("+nomeCandidato+"), CPF candidato ("+cpfCandidato+")."+e.getMessage());
    		}
    		throw e;
		}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirProcessoSeletivo(Map<String, ProcSeletivoVO> mapProcSeletivoVOs, UsuarioVO usuarioVO) throws Exception {
    	for (ProcSeletivoVO procSeletivoVO : mapProcSeletivoVOs.values()) {
    		
    		if (Uteis.isAtributoPreenchido(procSeletivoVO.getCodigo())) {
//    			INCLUIR PRIMEIRAMENTE AS DATAS DAS PROVAS COM HORA
    			for (ItemProcSeletivoDataProvaVO item : procSeletivoVO.getItemProcSeletivoDataProvaVOs()) {
					if (!Uteis.isAtributoPreenchido(item.getCodigo())) {
						getFacadeFactory().getItemProcSeletivoDataProvaFacade().incluir(item, usuarioVO);
					}
				}
    			for(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO: procSeletivoVO.getProcSeletivoUnidadeEnsinoVOs()) {
    				if(!Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoVO.getCodigo())) {
    					procSeletivoUnidadeEnsinoVO.setProcSeletivo(procSeletivoVO);
    					getFacadeFactory().getProcSeletivoUnidadeEnsinoFacade().incluir(procSeletivoUnidadeEnsinoVO, usuarioVO);
    				}else {
    					for(ProcSeletivoCursoVO procSeletivoCursoVO: procSeletivoUnidadeEnsinoVO.getProcSeletivoCursoVOs()) {
    						if(!Uteis.isAtributoPreenchido(procSeletivoCursoVO)) {
    							getFacadeFactory().getProcSeletivoCursoFacade().incluir(procSeletivoCursoVO, usuarioVO);
    						}
    					}
    				}
    			}
    		} else {
    			getFacadeFactory().getProcSeletivoFacade().incluir(procSeletivoVO, usuarioVO);
    		}
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistirInscricao(List<InscricaoVO> listaInscricaoVOs, Map<String, ResultadoProcessoSeletivoVO> mapResultadoProcessoSeletivoVOs, UsuarioVO usuarioVO, ProgressBarVO progressBarVO) throws Exception {
    	for (InscricaoVO inscricaoVO : listaInscricaoVOs) {
    		progressBarVO.setStatus("Gravando inscrição do candidato "+inscricaoVO.getCandidato().getNome()+".");
    		if (Uteis.isAtributoPreenchido(inscricaoVO.getCodigo())) {
    			ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = mapResultadoProcessoSeletivoVOs.get(inscricaoVO.getCandidato().getCPF());
    			if (!Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getCodigo())) {
    				getFacadeFactory().getResultadoProcessoSeletivoFacade().incluir(resultadoProcessoSeletivoVO, usuarioVO);
    			}
    			if(inscricaoVO.getAlterarNumeroChamada()) {
    				getFacadeFactory().getInscricaoFacade().alterarNumeroChamadaInscricao(inscricaoVO, usuarioVO);
    			}
    			continue;
    		}
    		if (!Uteis.isAtributoPreenchido(inscricaoVO.getProcSeletivo().getCodigo())) {
				inscricaoVO.setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoExata(inscricaoVO.getProcSeletivo().getDescricao(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			} 
    		getFacadeFactory().getInscricaoFacade().incluir(inscricaoVO, null, null, usuarioVO);
    		ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = mapResultadoProcessoSeletivoVOs.get(inscricaoVO.getCandidato().getCPF());
    		resultadoProcessoSeletivoVO.setInscricao(inscricaoVO);
    		progressBarVO.setStatus("Gravando resultado do processo seletivo do candidato "+inscricaoVO.getCandidato().getNome()+".");
    		getFacadeFactory().getResultadoProcessoSeletivoFacade().incluir(resultadoProcessoSeletivoVO, usuarioVO);
    		progressBarVO.incrementar();
		}
    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void validarDados(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, Map<String, ImportarCandidatoInscricaoProcessoSeletivoVO> mapCandidato, Boolean validarSomenteUmPolo , UsuarioVO usuario) throws Exception {
    	validarDadosCandidato(candidatoInscricaoVO, mapCandidato, true, usuario);
		validarDadosInscricao(candidatoInscricaoVO, true, validarSomenteUmPolo,  usuario);
		validarDadosResultadoProcessoSeletivo(candidatoInscricaoVO, true);
		validarDadosExistenciaUnidadeEnsino(candidatoInscricaoVO, null, null, true, usuario);
		validarDadosExistenciaCurso(candidatoInscricaoVO, null, null, true, usuario);
		validarDadosExistenciaTurno(candidatoInscricaoVO, null, null, true, usuario);
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(ImportarCandidatoInscricaoProcessoSeletivoVO obj, boolean validarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		ImportarCandidatoInscricaoProcessoSeletivo.incluir(getIdEntidade(), validarAcesso, usuario);
		getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
		incluir(obj, "ImportarCandidatoInscricaoProcessoSeletivo", new AtributoPersistencia() .add("arquivo", obj.getArquivoVO()), usuario);
		obj.setNovoObj(Boolean.TRUE);
	}
    
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosArquivoImportarCandidatoInscricaoProcessoSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoVO, FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		importarCandidatoInscricaoVO.getArquivoVO().setNome(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName())));
		importarCandidatoInscricaoVO.getArquivoVO().setExtensao(ArquivoHelper.getExtensaoArquivo(fileUploadEvent.getUploadedFile().getName()));
		importarCandidatoInscricaoVO.getArquivoVO().setDescricao(fileUploadEvent.getUploadedFile().getName());
		importarCandidatoInscricaoVO.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
		importarCandidatoInscricaoVO.getArquivoVO().setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_TMP.getValue());
		importarCandidatoInscricaoVO.getArquivoVO().getResponsavelUpload().setCodigo(usuarioVO.getCodigo());
		importarCandidatoInscricaoVO.getArquivoVO().getResponsavelUpload().setNome(usuarioVO.getNome());
		importarCandidatoInscricaoVO.getArquivoVO().setDataUpload(new Date());
		importarCandidatoInscricaoVO.getArquivoVO().setManterDisponibilizacao(true);
		importarCandidatoInscricaoVO.getArquivoVO().setDataDisponibilizacao(new Date());
		importarCandidatoInscricaoVO.getArquivoVO().setDataIndisponibilizacao(null);
		importarCandidatoInscricaoVO.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
		importarCandidatoInscricaoVO.getArquivoVO().setOrigem(OrigemArquivo.PROCESSO_SELETIVO_IMPORTACAO.getValor());
		ArquivoHelper.salvarArquivoNaPastaTemp(fileUploadEvent, importarCandidatoInscricaoVO.getArquivoVO().getNome(), importarCandidatoInscricaoVO.getArquivoVO().getPastaBaseArquivo(), configuracaoGeralSistemaVO, usuarioVO);
		
	}
	
	@Override
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultar(String valorConsulta, String campoConsulta, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {
		if (campoConsulta.equals("responsavel")) {
			return consultarPorResponsavel(valorConsulta, usuarioVO);
		}
		if (campoConsulta.equals("data")) {
			return consultarPorData(dataInicio, dataFim, usuarioVO);
		}
		return new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
	}
	
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultarPorResponsavel(String valorConsulta, UsuarioVO usuarioVO) {
		StringBuilder sb = getConsultaBasica();
		sb.append(" WHERE upper(sem_acentos(usuario.nome)) like (upper(sem_acentos(?))) ");
		sb.append(" order by usuario.nome,  imp.created");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}
	
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultarPorData(Date dataInicio, Date dataFim, UsuarioVO usuarioVO) {
		StringBuilder sb = getConsultaBasica();
		sb.append(" where cast(imp.created as date) >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sb.append(" and cast(imp.created as date) <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		sb.append(" order by imp.created");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, usuarioVO);
	}
	
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> montarDadosConsulta(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaImportarCandidatoInscricaoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		while (dadosSQL.next()) {
			ImportarCandidatoInscricaoProcessoSeletivoVO obj = new ImportarCandidatoInscricaoProcessoSeletivoVO();
			montarDados(dadosSQL, obj, usuarioVO);
			listaImportarCandidatoInscricaoVOs.add(obj);
		}
		return listaImportarCandidatoInscricaoVOs;
	}
	
	public void montarDados(SqlRowSet dadosSQL, ImportarCandidatoInscricaoProcessoSeletivoVO obj, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNomeCreated(dadosSQL.getString("nomeCreated"));
		obj.setCreated(dadosSQL.getTimestamp("created"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("codigoArquivo"));
		obj.getArquivoVO().setNome(dadosSQL.getString("nome"));
		obj.getArquivoVO().setDescricao(dadosSQL.getString("descricao"));
		obj.getArquivoVO().setDataUpload(dadosSQL.getDate("dataUpload"));
	}
	
	public StringBuilder getConsultaBasica() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct imp.codigo, imp.nomeCreated, imp.created, arquivo.codigo as codigoArquivo, arquivo.nome, arquivo.descricao, arquivo.dataupload, ");
		sb.append(" usuario.nome as nomeResponsavel ");
		sb.append(" from importarcandidatoinscricaoprocessoseletivo imp ");
		sb.append(" inner join arquivo on arquivo.codigo = imp.arquivo ");
		sb.append(" inner join usuario on usuario.codigo = imp.codigocreated ");
		return sb;
	}

	@Override
	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> consultarPorNomeCandidatos(String nomeCandidato, List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoVOs, UsuarioVO usuarioVO) {
		List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		for (ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO : listaCandidatoInscricaoProcessoSeletivoVOs) {
			if (Uteis.removerAcentuacao(importarCandidatoVO.getNome()).toUpperCase().trim().contains(Uteis.removerAcentuacao(nomeCandidato).toUpperCase().trim())) {
				listaCandidatoVOs.add(importarCandidatoVO);
			}
		}
		return listaCandidatoVOs;
	}
	
	
	
	
	 
	public void validarDadosExistenciaUnidadeEnsinoVagasEixoCurso(ImportarCandidatoInscricaoProcessoSeletivoVO candidatoInscricaoVO, 
			Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsino, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs,
			Map<String, Map<Integer, Integer>> mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis, Map<String, Integer> mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino , Boolean lancarExcessao, UsuarioVO usuario) throws Exception {
		    
	    	String msg = null; 
		    if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo())) {
	    		candidatoInscricaoVO.setPossuiErro(true);
	    		msg = "No cadastro do CURSO "+candidatoInscricaoVO.getCursoVO().getNome().toUpperCase()+ " não foi definido o EIXO DO CURSO.";
	    		if(!candidatoInscricaoVO.getErro().contains(msg)) {
	    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - "+msg+"  \n" );
	    			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
	    		}
	    		return;
	    	}		    
	    	if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getProcSeletivoVO().getCodigo())) {
	    		candidatoInscricaoVO.setPossuiErro(true);
	    		msg = "PROCESSO SELETIVO ("+candidatoInscricaoVO.getDescricaoProcessoSeletivo()+") não cadastrado";
	    		if(!candidatoInscricaoVO.getErro().contains(msg)) {
	    			candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - " +msg +" \n" );
	    			candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
	    		}
	    		return;
	    	}	    	
	    	if(!Uteis.isAtributoPreenchido(candidatoInscricaoVO.getProcSeletivoVO().getProcSeletivoUnidadeEnsinoVOs().isEmpty())) {
	        	 candidatoInscricaoVO.setPossuiErro(true);
	        	 msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" deve ser informado as UNIDADES DE ENSINO";
	        	 if(!candidatoInscricaoVO.getErro().contains(msg)) {
	        		 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " - "+msg+"  \n" );
	        		 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
	        	 }
	      		 return;
		    } 
	    	
	    	
		     List<Integer> listaUnidadeEnsino = new ArrayList<Integer>(Arrays.asList(new Integer[] { candidatoInscricaoVO.getIdPolo(),	candidatoInscricaoVO.getIdPolo2(), candidatoInscricaoVO.getIdPolo3(),candidatoInscricaoVO.getIdPolo4(), candidatoInscricaoVO.getIdPolo5() }).stream().filter(p -> Uteis.isAtributoPreenchido(p)).collect(Collectors.toList()));
		     for(Integer poloUnidadeEnsino : listaUnidadeEnsino) {
			    	if (!mapUnidadeEnsino.containsKey(poloUnidadeEnsino)) { 		    		 
			    		 mapUnidadeEnsino.put(poloUnidadeEnsino, getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(poloUnidadeEnsino, usuario));
			    	}
			    	UnidadeEnsinoVO unidadeEnsinoVO = mapUnidadeEnsino.get(poloUnidadeEnsino);   		 
			    		 
				    if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {      			
				    	
				    	 ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoExistenteVO = candidatoInscricaoVO.getProcSeletivoVO().consultarObjProcSeletivoUnidadeEnsinoVO(unidadeEnsinoVO.getCodigo());

						    if(procSeletivoUnidadeEnsinoExistenteVO == null || !Uteis.isAtributoPreenchido(procSeletivoUnidadeEnsinoExistenteVO.getCodigo())) {		        					
								// aqui  devemos adicionar uma mensagem  de erro pois nao existe essa unidade de ensino vinculada ao processo seletivo 
					    		// e seguir para proxima unidade de ensino 
						    	msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" deve ser informado a UNIDADE DE ENSINO de "+unidadeEnsinoVO.getNome().toUpperCase()+" de CÓDIGO "+unidadeEnsinoVO.getCodigo();
								candidatoInscricaoVO.setPossuiErro(true);
								 if(!candidatoInscricaoVO.getErro().contains(msg)) {
									 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() +" - "+ msg+  "  \n");	        	    				
									 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
								 }
								continue ;        		    		
					    	}
						    
							procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivoCursoVOs().stream().forEach( obj ->  obj.getProcSeletivoUnidadeEnsino().setProcSeletivo(procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivo()));

							if(!procSeletivoUnidadeEnsinoExistenteVO.getContemProcSeletivoCurso(candidatoInscricaoVO.getProcSeletivoVO().getCodigo(), candidatoInscricaoVO.getCursoVO().getCodigo(),  unidadeEnsinoVO.getCodigo(), candidatoInscricaoVO.getTurnoVO().getCodigo())) {
								// aqui  devemos adicionar uma mensagem  de erro pois nao existe este curso  na unidade de ensino vinculada ao processo seletivo 
					    		// e seguir para proxima unidade de ensino 
								UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeTurno(candidatoInscricaoVO.getCursoVO().getCodigo(), candidatoInscricaoVO.getUnidadeEnsinoVO().getCodigo(), candidatoInscricaoVO.getTurnoVO().getCodigo(), usuario);
					    		if (!Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCodigo())) {
					    			msg = "No cadastro da UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" deve ser vinculado o curso "+candidatoInscricaoVO.getCursoVO().getNome().toUpperCase()+" e turno "+candidatoInscricaoVO.getTurnoVO().getNome().toUpperCase();
									candidatoInscricaoVO.setPossuiErro(true);
									 if(!candidatoInscricaoVO.getErro().contains(msg)) {
										 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() +" - "+ msg+  "  \n");	        	    				
										 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
									 }
					    		}else { 
					    			procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivoCursoVOs().add(getFacadeFactory().getProcSeletivoCursoFacade().inicializarDadosImportarCandidatoInscricaoProcSeletivo(candidatoInscricaoVO, procSeletivoUnidadeEnsinoExistenteVO, unidadeEnsinoCursoVO, usuario));
					    		}
								
//			    				candidatoInscricaoVO.setPossuiErro(true);
//			    				candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  CURSO ("+candidatoInscricaoVO.getCursoVO().getNome()+") não informado no processo seletivo  \n" );		        		    		
//			    				candidatoInscricaoVO.getListaMotivoErrosImportacao().add("CURSO ("+candidatoInscricaoVO.getCursoVO().getNome()+") não informado no processo seletivo. ");
//			    				continue ; 
							}
							
							if(procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().isEmpty()) {
			    				// aqui  devemos adicionar uma mensagem  de erro pois nao existe eixo curso cadastrado essa unidade de ensino vinculada ao processo seletivo 
					    		// e seguir para proxima unidade de ensino 
								msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" deve ser informado na UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" vagas para o eixo dos cursos";
								candidatoInscricaoVO.setPossuiErro(true);
								 if(!candidatoInscricaoVO.getErro().contains(msg)) {
									 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() +" - "+ msg+  "  \n");	        	    				
									 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
								 }											    			
			    				continue ;   
			    			}
							
							

							String keyProcSeletivoUnidadeEnsino = candidatoInscricaoVO.getProcSeletivoVO().getDescricao().toString() +"."+ unidadeEnsinoVO.getCodigo().toString() ;

							if(!mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis.containsKey(keyProcSeletivoUnidadeEnsino)) {
				    			
			                   	// aqui iremos pegar todas os eixo cursos cadastrados para unidade de ensino e jogar no mapa  com 
				    			// a chave sendo o proprio  codigo do eixo curso que tbm esta vinculada ao objeto ProcSeletivoUnidadeEnsinoEixoCursoVO
				    			// o valor sendo o numero de vagas disponiveis para aquele eixo curso 
				    			// sendo assim podemos  recuperar do mapa o valor de vagas disponiveis para cada eixo curso vinculado a unidade de ensino  
				    			Map<Integer, Integer> mapEixoCursoNumeroVagasDisponiveis = new HashMap<Integer, Integer>(0);			    			
				    			procSeletivoUnidadeEnsinoExistenteVO.getProcSeletivoUnidadeEnsinoEixoCursoVOs().stream().forEach( obj -> mapEixoCursoNumeroVagasDisponiveis.put(obj.getEixoCurso().getCodigo(), obj.getNrVagasEixoCurso()) );
				    						    					    			
				    			// aqui iremos jogar o mapa de EixoCursoNumeroVagasDisponiveis dentro de outro mapa 
				    			// mapa  ProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis que armazena os mapas de eixo curso nr vagas para a unidade de ensino do processo seletivo 
			                    mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis.put((keyProcSeletivoUnidadeEnsino), mapEixoCursoNumeroVagasDisponiveis);
								
							}     				
							
							// aqui iremos recuperar o mapa com todos os eixo curso ,nr vagas disponiveis  vinculados  ao processo seletivo unidade de ensino 
							Map<Integer, Integer> mapEixoCursoNumeroVagasDisponiveis = 	mapProcSeletivoUnidadeEnsinoMapsEixoCursoNumeroVagasDisponiveis.get(keyProcSeletivoUnidadeEnsino) ;
							String keyProcSeletivoUnidadeEnsinoEixoCurso = keyProcSeletivoUnidadeEnsino+"EIXO_MATRICULADOS"+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo();
							String keyProcSeletivoUnidadeEnsinoEixoCursoNovasInscricoes = keyProcSeletivoUnidadeEnsino+"EIXO_NOVAS_VAGAS"+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo();
							// aqui iremos verificar se existe eixoCurso/nrVagas  no mapa igual ao eixo curso informado no cadastro do curso  escolhido  
							if(mapEixoCursoNumeroVagasDisponiveis.containsKey(candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo())){
								Integer nrVagasEixoCurso = mapEixoCursoNumeroVagasDisponiveis.get(candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo());
								Integer totalNovosInscritos = 0;
								if(mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.containsKey(keyProcSeletivoUnidadeEnsinoEixoCursoNovasInscricoes)) {
									totalNovosInscritos = mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.get(keyProcSeletivoUnidadeEnsinoEixoCursoNovasInscricoes);
								}
								if(Uteis.isAtributoPreenchido(nrVagasEixoCurso) && nrVagasEixoCurso > 0) {
									
									if(!mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.containsKey(keyProcSeletivoUnidadeEnsinoEixoCurso)){
			    						Integer numeroAlunosMatriculados = getFacadeFactory().getProcSeletivoFacade().verificarQuantidadeAlunosMatriculadosPorProcessoSeletivoUnidadeEnsino(candidatoInscricaoVO.getProcSeletivoVO().getCodigo(), unidadeEnsinoVO.getCodigo(), candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo(), usuario);
			    						mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.put(keyProcSeletivoUnidadeEnsinoEixoCurso, numeroAlunosMatriculados);
									}
									
									
									Integer qdteAlunosMatriculados = mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.get(keyProcSeletivoUnidadeEnsinoEixoCurso);
			    					 if((qdteAlunosMatriculados >= nrVagasEixoCurso)) {
			    						 if(Uteis.isAtributoPreenchido(totalNovosInscritos)) {
			    							 msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" não foi encontrado na UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" vagas para o eixo do curso "+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getNome().toUpperCase()+" já existem "+qdteAlunosMatriculados+" alunos matriculados e "+totalNovosInscritos+" novos inscritos nesta chamada, ocupando o total de "+(qdteAlunosMatriculados+totalNovosInscritos)+" vagas";
			    						 }else {
			    							 msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" não foi encontrado na UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" vagas para o eixo do curso "+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getNome().toUpperCase()+" já existem "+qdteAlunosMatriculados+" alunos matriculados no total de "+qdteAlunosMatriculados+" vagas liberadas." ;
			    						 }
										candidatoInscricaoVO.setPossuiErro(true);
										if(!candidatoInscricaoVO.getErro().contains(msg)) {												 
											candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+" \n" );
											candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
										}			    				    
			    				    	continue;
			    					 }
			    					 
		    				    		
			    					mapEixoCursoNumeroVagasDisponiveis.put(candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getCodigo(), nrVagasEixoCurso - 1);
			    					mapNumeroVagasOcupadasPorMatriculasAtivasPorProcessoSeletivoUnidadeEnsino.put(keyProcSeletivoUnidadeEnsinoEixoCursoNovasInscricoes, totalNovosInscritos+1);
									
		    				    	candidatoInscricaoVO.setUnidadeEnsinoVO(unidadeEnsinoVO);
		    				    	candidatoInscricaoVO.setPossuiErro(false);	
		    				    	candidatoInscricaoVO.setErro("");
		    				    	candidatoInscricaoVO.getListaMotivoErrosImportacao().clear();
		    				    	return ;
			    				   
								}else {	  
									msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" não foi encontrado na UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" vagas para o eixo do curso "+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getNome().toLowerCase();
									candidatoInscricaoVO.setPossuiErro(true);
									if(!candidatoInscricaoVO.getErro().contains(msg)) {											
										candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() + " -  "+msg+"  \n" );
										candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
									}
									continue;
								}  
								    
							}else {	 
								// aqui  devemos adicionar uma mensagem  de erro pois nao existe Eixo Curso cadastrado  na UNIDADE ENSINO vinculada ao processo seletivo que tenha o curso escolhido  
					    		// e seguir para proxima unidade de ensino
								msg = "No cadastro do PROCESSO SELETIVO "+candidatoInscricaoVO.getProcSeletivoVO().getDescricao()+" não foi encontrado na UNIDADE DE ENSINO "+unidadeEnsinoVO.getNome().toUpperCase()+" vagas para o eixo do curso "+candidatoInscricaoVO.getCursoVO().getEixoCursoVO().getNome().toLowerCase();
								candidatoInscricaoVO.setPossuiErro(true);
								 if(!candidatoInscricaoVO.getErro().contains(msg)) {
									 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() +" - "+ msg+  "  \n");	        	    				
									 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
								 }
																
								continue;
							}	
		 
				    	
				    }else {	        				
						unidadeEnsinoVO.setCodigo(poloUnidadeEnsino);
						listaUnidadeEnsinoVOs.add(unidadeEnsinoVO);
						// aqui  devemos adicionar uma mensagem  de erro pois nao existe essa unidade de ensino  com este codigo 
			    		// e seguir para proxima unidade de ensino
						candidatoInscricaoVO.setPossuiErro(true);
						msg = "Não foi encontrado a UNIDADE DE ENSINO "+poloUnidadeEnsino;
						if(!candidatoInscricaoVO.getErro().contains(msg)) {
							 candidatoInscricaoVO.setErro(candidatoInscricaoVO.getErro() +" - "+ msg+  "  \n");	        	    				
							 candidatoInscricaoVO.getListaMotivoErrosImportacao().add(msg);
						 }
						continue ;	        				 
					} 
				    
		     }   
				    
	    	
    }


	
}
