package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AlunoCensoVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CensoUnidadeEnsinoVO;
import negocio.comuns.academico.CensoVO;
import negocio.comuns.academico.CursoCensoProfessorVO;
import negocio.comuns.academico.CursoCensoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.ProfessorCensoVO;
import negocio.comuns.academico.TurmaCensoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.AguaConsumidaEnum;
import negocio.comuns.administrativo.enumeradores.CategoriaEscolaPrivadaEnum;
import negocio.comuns.administrativo.enumeradores.ConveniadaPoderPublicoEnum;
import negocio.comuns.administrativo.enumeradores.DependenciaAdministativaEnum;
import negocio.comuns.administrativo.enumeradores.FormaOcupacaoPredioEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoDiferenciadaEscolaEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoZonaEscolaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.ApoioSocial;
import negocio.comuns.utilitarias.dominios.AtividadeFormacaoComplementar;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.JustificativaCensoEnum;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.ReservasVagas;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoMobilidadeAcademicaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CensoInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CensoVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CensoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CensoVO
 * @see ControleAcesso
 */
@SuppressWarnings({ "unchecked" })
@Repository
@Scope("singleton")
@Lazy
public class Censo extends ControleAcesso implements CensoInterfaceFacade {

	private static final String ETAPA_ENSINO_3 = "3";
	private static final String ETAPA_ENSINO_22 = "22";
	private static final String ETAPA_ENSINO_23 = "23";
	private static final String ETAPA_ENSINO_72 = "72";
	private static final String ETAPA_ENSINO_56 = "56";
	private static final String ETAPA_ENSINO_64 = "64";
	private static final long serialVersionUID = -5612398850711763105L;
	protected static String idEntidade;
	private ArquivoHelper arquivoHelper = new ArquivoHelper();
	
	private Date dataPadrao = new GregorianCalendar(2009, 11, 30).getTime();
	public static final String GRADUACAO = "GRADUACAO";
	public static final String TECNICO = "TECNICO";
	public static final String MEDIO = "MEDIO";
	public static final String GRADUACAO_TECNOLOGICA = "GRADUACAO_TECNOLOGICA";
	public static final String EDUCACAO_BASICA_TECNICO = "EDUCACAO_BASICA_TECNICO";

	public Censo() throws Exception {
		super();
		setIdEntidade("Censo");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator
				+ "CensoAlunoRel.jrxml");
	}
	
	private void inicializarArquivosLoteCenso(CensoVO censo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int lote, UsuarioVO usuario) throws Exception{
		String nomeArquivoAluno = "aluno_" + Uteis.getData(new Date(), "ddMMyyyyHHmmss") + "_"+ censo.getUnidadeEnsino().getCodigo() + "_" + censo.getAno() + "_" + censo.getCodigo()+"_lote"+lote;
		File fileTxt =  new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.CENSO.getValue()+File.separator+nomeArquivoAluno+".txt");
		censo.getArquivosTxt().add(fileTxt);
		executarCriarArquivoTexto(censo, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.CENSO.getValue(), nomeArquivoAluno+".txt", null);
		executarCriarCabecalho(censo);
		
		File fileExcel =  new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.CENSO.getValue()+File.separator+nomeArquivoAluno+".xlsx");
		censo.getArquivosExcel().add(fileExcel);
		
		censo.setWorkbook(new XSSFWorkbook());
		censo.setWorksheet(censo.getWorkbook().createSheet("Censo"));
		censo.getWorksheet().setAutobreaks(true);
		censo.getWorksheet().setVerticallyCenter(true);
		realizarCriacaoCabecalhoAlunoArquivoExcel(censo.getWorksheet());
		censo.getWorksheet().createFreezePane(0, 1);
		censo.setFileOutputStream(new FileOutputStream(fileExcel));
		
	}
	
	private void realizarFinalizacaoLoteArquivoCenso(CensoVO censo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception{
		if(censo.getPwAluno() != null) {
			EditorOC editorOC = new EditorOC();
			censo.getPwAluno().print(editorOC.getText());
			censo.getPwAluno().close();
			censo.setPwAluno(null);
		}
		if (censo.getFileOutputStream() != null) {
			censo.getWorkbook().write(censo.getFileOutputStream());
			censo.getFileOutputStream().flush();
			censo.getFileOutputStream().close();
			censo.setFileOutputStream(null);
			censo.setWorkbook(null);
		}		
	}
	
	private void realizarFinalizacaoArquivosCenso(CensoVO censo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception{
		
		
		
		String nomeArquivoAluno = "aluno_" + Uteis.getData(new Date(), "ddMMyyyyHHmmss") + "_"+ censo.getUnidadeEnsino().getCodigo() + "_" + censo.getAno() + "_" + censo.getCodigo();
		
		
		File fileZipTxt =  new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.CENSO.getValue()+File.separator+nomeArquivoAluno+"_txt.zip");
		censo.getArquivoAluno().setNome(nomeArquivoAluno+"_txt.zip");
		censo.getArquivoAluno().setDescricao(nomeArquivoAluno+"_txt.zip");
		censo.getArquivoAluno().setExtensao(".zip");
		censo.getArquivoAluno().setOrigem(OrigemArquivo.CENSO_ALUNO.getValor());
		censo.getArquivoAluno().setCodOrigem(censo.getCodigo());
		censo.getArquivoAluno().setPastaBaseArquivo(PastaBaseArquivoEnum.CENSO.getValue());
		censo.getArquivoAluno().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
		getFacadeFactory().getArquivoHelper().zip(censo.getArquivosTxt().toArray(new File[censo.getArquivosTxt().size()]), fileZipTxt);
		fileZipTxt =  null;
		
		getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAluno(), usuario, configuracaoGeralSistemaVO);
		
		File fileZipExcel =  new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.CENSO.getValue()+File.separator+nomeArquivoAluno+"_excel.zip");
		censo.getArquivoAlunoExcel().setNome(nomeArquivoAluno+"_excel.zip");
		censo.getArquivoAlunoExcel().setDescricao(nomeArquivoAluno+"_excel.zip");
		censo.getArquivoAlunoExcel().setExtensao(".zip");
		censo.getArquivoAlunoExcel().setOrigem(OrigemArquivo.CENSO_ALUNO.getValor());
		censo.getArquivoAlunoExcel().setCodOrigem(censo.getCodigo());
		censo.getArquivoAlunoExcel().setPastaBaseArquivo(PastaBaseArquivoEnum.CENSO.getValue());
		censo.getArquivoAlunoExcel().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
		getFacadeFactory().getArquivoHelper().zip(censo.getArquivosExcel().toArray(new File[censo.getArquivosExcel().size()]), fileZipExcel);
		fileZipExcel =  null;
		
		getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAlunoExcel(), usuario, configuracaoGeralSistemaVO);
		
	}

	

	@Deprecated
	public void executarGerarArquivoLayoutTecnico(CensoVO censo, String caminhoPasta,
			UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		String nomeArquivoAluno = "aluno_" + Uteis.getData(new Date(), "ddMMyyyyHHmmss" + "_"
				+ censo.getUnidadeEnsino().getCodigo() + "_" + censo.getAno() + "_" + censo.getSemestre()) + ".txt";
		try {
			FuncionarioVO diretorGeral = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaDiretorGeralPorCodigoUnidadeEnsino(censo.getUnidadeEnsino().getCodigo(), usuario);
			if (!Uteis.isAtributoPreenchido(diretorGeral)) {
				throw new Exception("Não existe um Diretor Geral cadastrado na unidade de ensino "
						+ censo.getUnidadeEnsino().getNome());
			}
			executarCriarArquivoTextoLayoutTecnico(censo,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue(), nomeArquivoAluno);

			censo.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(censo.getUnidadeEnsino().getCodigo(), usuario));
			// Registro 00
			executarCriarCabecalhoLayoutTecnico(censo, usuario, diretorGeral);
			// Registro 10
			executarCriarTipoRegistroLayoutTecnico(censo, usuario);
			// Registro 20
			executarCriarRegistroTurmasLayoutTecnico(censo, usuario);
			// Registro 30
			executarCriarRegistroProfessoresLayoutTecnico(censo, usuario);
			// Registro 60
			executarCriarRegistroAlunosLayoutTecnico(censo, usuario);
			// Registro 99
			censo.getPwLayoutTecnico().println("99|");

			EditorOC editorOC = new EditorOC();

			censo.getPwLayoutTecnico().print(editorOC.getText());
			censo.getPwLayoutTecnico().close();

			if (!censo.getListaAlunoCenso().isEmpty()) {
				SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
				superParametroRelVO.setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				superParametroRelVO.setNomeDesignIreport(getDesignIReportRelatorio());
				superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				superParametroRelVO.setSubReport_Dir(getCaminhoBaseRelatorio());
				superParametroRelVO.setNomeEmpresa(censo.getUnidadeEnsino().getNome());
				superParametroRelVO.setTituloRelatorio("Censo Aluno - " + censo.getSemestre() + "/" + censo.getAno());
				superParametroRelVO.setListaObjetos(censo.getListaAlunoCenso());
				GeradorRelatorio.realizarExportacaoEXCEL(superParametroRelVO,
						caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue(),
						nomeArquivoAluno.replaceAll(".txt", ".xlsx"));
				censo.getArquivoAlunoExcel().setNome(nomeArquivoAluno.replaceAll(".txt", ".xlsx"));
				censo.getArquivoAlunoExcel().setOrigem(OrigemArquivo.CENSO_ALUNO.getValor());
				censo.getArquivoAlunoExcel().setCodOrigem(censo.getCodigo());
				censo.getArquivoAlunoExcel()
						.setPastaBaseArquivo(caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
				censo.getArquivoAlunoExcel().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
				getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAlunoExcel(), usuario,
						configuracaoGeralSistema);
			}

			censo.getArquivoAluno().setNome(nomeArquivoAluno);
			censo.getArquivoAluno().setOrigem(OrigemArquivo.CENSO_ALUNO.getValor());
			censo.getArquivoAluno().setCodOrigem(censo.getCodigo());
			censo.getArquivoAluno()
					.setPastaBaseArquivo(caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
			censo.getArquivoAluno().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
			getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAluno(), usuario, configuracaoGeralSistema);
		} catch (Exception e) {
			ArquivoVO arqExcluir = new ArquivoVO();
			arqExcluir.setNome(nomeArquivoAluno);
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
			arqExcluir.setNome(nomeArquivoAluno.replace(".txt", ".xlsx"));
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
			throw e;
		}
	}

	/***
	 * Para criar um arquivo
	 * 
	 * @param caminhoPasta
	 * @param nomeArquivo
	 * @param tipo
	 * @return
	 * @throws Exception
	 */
	private void executarCriarArquivoTexto(CensoVO censo, String caminhoPasta, String nomeArquivoAluno, String nomeArquivoProfessor)
			throws Exception {
		arquivoHelper.criarCaminhoPastaDeInclusaoArquivo(caminhoPasta);
		censo.setPwAluno(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivoAluno, true));
//		setPwProfessor(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivoProfessor, true));
	}

	private void executarCriarArquivoTextoLayoutTecnico(CensoVO censoVO, String caminhoPasta, String nomeArquivoLayoutTecnico)
			throws Exception {
		arquivoHelper.criarCaminhoPastaDeInclusaoArquivo(caminhoPasta);
		censoVO.setPwLayoutTecnico(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivoLayoutTecnico, true));
	}

	private void executarCriarArquivoTextoLayoutEducacaoBasica(CensoVO censoVO, String caminhoPasta, String nomeArquivoLayoutEnsinoMedio)
			throws Exception {
		arquivoHelper.criarCaminhoPastaDeInclusaoArquivo(caminhoPasta);
		censoVO.setPwLayoutEducacaoBasica(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivoLayoutEnsinoMedio, true));
	}

	/***
	 * Método responsável por criar registros dos professores
	 * 
	 * @param censo
	 * @throws Exception
	 */
	private void executarCriarRegistroProfessores(CensoVO censo, UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		FuncionarioVO professorVO;
		for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
			professorVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(
					professorCenso.getProfessor().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			censo.getPwProfessor().println(
					executarGerarRegistroProfessor(linha, professorCenso, professorVO, professorVO.getPessoa()));
			linha.setLength(0);
			for (CursoCensoProfessorVO cursoCensoProfessor : professorCenso.getListaCursoCenso()) {
				censo.getPwProfessor().println(gerarRegistroVinculoProfessorCurso(linha, cursoCensoProfessor));
				linha.setLength(0);
			}
		}
	}

	@Deprecated
	private void executarCriarRegistroProfessoresLayoutTecnico(CensoVO censo, UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		FuncionarioVO professorVO;
		for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
			professorVO = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCodigoPessoaCenso(professorCenso.getProfessor().getCodigo(), usuario);
			censo.getPwLayoutTecnico().println(executarGerarRegistroProfessorLayoutTecnicoIdentificacao(linha, professorCenso,
					professorVO, professorVO.getPessoa(), censo, usuario));
			linha = new StringBuilder();
			censo.getPwLayoutTecnico().println(executarGerarRegistroProfessorLayoutTecnicoDocumento(linha, professorCenso,
					professorVO, professorVO.getPessoa(), censo));
			linha = new StringBuilder();
			censo.getPwLayoutTecnico().println(executarGerarRegistroProfessorLayoutTecnicoDadosVariaveis(linha,
					professorCenso, professorVO, professorVO.getPessoa(), censo));
			for (TurmaCensoVO turmaCensoVO : professorCenso.getListaTurmaCensoVOs()) {
				linha = new StringBuilder();
				censo.getPwLayoutTecnico().println(executarGerarRegistroProfessorLayoutTecnicoDadosDocencia(linha,
						professorCenso, professorVO, professorVO.getPessoa(), turmaCensoVO, censo));
			}
			linha.setLength(0);
			for (CursoCensoProfessorVO cursoCensoProfessor : professorCenso.getListaCursoCenso()) {
				censo.getPwLayoutTecnico().println(gerarRegistroVinculoProfessorCurso(linha, cursoCensoProfessor));
				linha.setLength(0);
			}
		}
	}

	// Educação Básica
		StringBuilder linha = new StringBuilder();
	private FuncionarioVO executarCriarRegistroGestorEducacaoBasicaIdentificacao(FuncionarioVO gestorVO, UsuarioVO usuario) throws Exception {
		gestorVO = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(gestorVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		
		gestorVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoaCenso(gestorVO.getPessoa().getCodigo(), usuario);
		
		return gestorVO;
		
	}
	
	private void executarCriarRegistroGestorEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		
		// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - IDENTIFICAÇÃO - REGISTRO
		// 30
		FuncionarioVO gestorVO = censo.getUnidadeEnsino().getDiretorGeral();
		StringBuilder linha = new StringBuilder();
		
		gestorVO = executarCriarRegistroGestorEducacaoBasicaIdentificacao(gestorVO, usuario);	

		censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroGestorProfessorLayoutEducacaoBasicaIdentificacao(linha, gestorVO, gestorVO.getPessoa(), censo, true));

		
	}
	
	private void executarGerarRegistroGestorEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		
		// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - Gestor
		// REGISTRO 40
		FuncionarioVO gestorVO = censo.getUnidadeEnsino().getDiretorGeral();
		StringBuilder linha = new StringBuilder();		
		
		gestorVO = executarCriarRegistroGestorEducacaoBasicaIdentificacao(gestorVO, usuario);	

		censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroGestorLayoutEducacaoBasica(linha, gestorVO, gestorVO.getPessoa(), censo));
		
	}
	
	
	private void executarCriarRegistroProfessorEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		
		// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - IDENTIFICAÇÃO - REGISTRO
		// 30
		FuncionarioVO professorVO;
		
		for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
			professorVO = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCodigoPessoaCenso(professorCenso.getProfessor().getCodigo(), usuario);

			censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroGestorProfessorLayoutEducacaoBasicaIdentificacao(linha, professorVO, professorVO.getPessoa(), censo, false));	

		}
		
	}
	
	private void executarCriarRegistroAlunoEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		
			// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - IDENTIFICAÇÃO - REGISTRO
			// 30
		PessoaVO alunoVO;
		
		for(AlunoCensoVO alunoCenso: censo.getListaAlunoCenso()) {
			StringBuilder linha = new StringBuilder();
			alunoVO = getFacadeFactory().getPessoaFacade()
					.consultaRapidaPorCodigoPessoaCenso(alunoCenso.getCodigo(), usuario);
	
			censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroAlunoLayoutEducacaoBasicaIdentificacao(linha,
					alunoCenso, alunoVO, censo));	
			
		}
		
	}	
	
	private void executarCriarVinculoProfessorTurmaEducacaoBasica (CensoVO censo, UsuarioVO usuario) throws Exception{
		
		FuncionarioVO professorVO;
		
		for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
			StringBuilder linha = new StringBuilder();
			professorVO = getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCodigoPessoaCenso(professorCenso.getProfessor().getCodigo(), usuario);			
			// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - DADOS VARIAVEIS - REGISTRO
			// 50
			/*getPwLayoutEnsinoMedio().println(executarGerarRegistroProfessorLayoutEnsinoMedioDadosVariaveis(linha,
					professorCenso, professorVO, professorVO.getPessoa(), censo));*/
			for (TurmaCensoVO turmaCensoVO : professorCenso.getListaTurmaCensoVOs()) {
				linha = new StringBuilder();
				// CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA - DADOS DE DOCÊNCIA -
				// REGISTRO 50
				censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroProfessorTurmaLayoutEducacaoBasica(linha, professorVO, turmaCensoVO, censo));
			}
			linha.setLength(0);
//			for (CursoCensoProfessorVO cursoCensoProfessor : professorCenso.getListaCursoCenso()) {
//				getPwLayoutTecnico().println(gerarRegistroVinculoProfessorCurso(linha, cursoCensoProfessor));
//				linha.setLength(0);
//			}
		}	
	}
		
	private void executarCriarRegistroPessoaFisicaLayoutEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		
		executarCriarRegistroGestorEducacaoBasica(censo, usuario);
		
		executarCriarRegistroProfessorEducacaoBasica(censo, usuario);
		
		executarCriarRegistroAlunoEducacaoBasica(censo, usuario);
		
	}

	/***
	 * Monta os registros do curso em relação ao professor
	 * 
	 * @param linha
	 * @param cursoProfessorCenso
	 * @return
	 * @throws Exception
	 */
	private String gerarRegistroVinculoProfessorCurso(StringBuilder linha, CursoCensoProfessorVO cursoProfessorCenso)
			throws Exception {
		// List<CursoVO> cursos
		// =getFacadeFactory().getCursoFacade().consultarPorCodigoProfessor(funcionarioVO.getPessoa().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_TODOS);
		linha.append(cursoProfessorCenso.getTipoRegistro()).append(CensoVO.SEPARADOR);
		linha.append(cursoProfessorCenso.getIdCurso());
		return linha.toString();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de professor
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	private String executarGerarRegistroProfessor(StringBuilder linha, ProfessorCensoVO professorCenso,
			FuncionarioVO professorVO, PessoaVO pessoaVO) {
		// TIPO REGISTRO - 01 - OBRIGATORIO
		linha.append(professorCenso.getTipoRegistro()).append(CensoVO.SEPARADOR);
		// ID DOCENTE NA IES - 02 - OPCIONAL
		linha.append(professorVO.getMatricula()).append(CensoVO.SEPARADOR);
		// NOME - 03 - OBRIGATORIO
		linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(pessoaVO.getNome().toUpperCase())))
				.append(CensoVO.SEPARADOR);
		// CPF - 04 - OBRIGATORIO
		linha.append(Uteis.retirarMascaraCPF(pessoaVO.getCPF().replace(".", "").replace("-", "")))
				.append(CensoVO.SEPARADOR);
		// DOCUMENTO ESTRANGEIRO - 05 - OPCIONAL
		linha.append(professorCenso.getDocumentoEstrangeiro()).append(CensoVO.SEPARADOR);
		// DATA NASCIMENTO - 06 - OBRIGATORIO
		linha.append(Uteis.getData(pessoaVO.getDataNasc(), "ddMMyyyy", new GregorianCalendar(1996, 3, 5).getTime(),
				new GregorianCalendar(1911, 1, 1).getTime())).append(CensoVO.SEPARADOR);
		// COR/RAÇA - 07 - OBRIGATORIO
		linha.append(pessoaVO.getCorRacaCenso()).append(CensoVO.SEPARADOR);
		// NACIONALIDADE - 8 - OBRIGATORIO
		linha.append(pessoaVO.getNacionalidadeCenso()).append(CensoVO.SEPARADOR);
		// PAIS ORIGEM - 9 - OBRIGATORIO
		linha.append(professorCenso.getPaisOrigem()).append(CensoVO.SEPARADOR);
		// UF DE NASCIMENTO - 10 - OPCIONAL
		if (pessoaVO.getNacionalidadeCenso().equals(1)) {
			linha.append(professorCenso.getUfDeNascimento()).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.SEPARADOR);
		}
		// MUNICIPIO DE NASCIMENTO - 11 - CONDICIONAL
		if (pessoaVO.getNacionalidadeCenso().equals(1)
				&& Uteis.isAtributoPreenchido(professorCenso.getUfDeNascimento())) {
			linha.append(professorCenso.getMunicipioNascimento()).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.SEPARADOR);
		}
		// DOCENTE COM DEFICIENCIA - CAMPUS 12 A 21 - OBRIGATORIO
		linha.append(pessoaVO.getDeficienciasCenso()).append(CensoVO.SEPARADOR);
		// ESCOLARIDADE - 22 - ORBIGATORIO
		linha.append(professorCenso.getEscolaridade()).append(CensoVO.SEPARADOR);
		// SITUACAÇÃO DO DOCENTE NA IES - 23 - ORBIGATORIO
		linha.append(professorCenso.getSituacaoDocente()).append(CensoVO.SEPARADOR);
		if (professorCenso.getSituacaoDocente().equals("1")) {
			// DOCENTE EM EXERCICIO EM 31/12/2014 ? - 24 - CONDICIONAL
			linha.append(professorCenso.getDocenteEmExercicioAnoAnterior(professorVO)).append(CensoVO.SEPARADOR);
			// REGIME DE TRABALHO - 25 - CONDICIONAL
			linha.append(professorVO.getRegimeTrabalhoDoceneteCenso()).append(CensoVO.SEPARADOR);
			// DOCENTE SUBSTITUTO - 26 - CONDICIONAL
			linha.append(professorCenso.getDocenteSubstituto()).append(CensoVO.SEPARADOR);
			// DOCENTE VISITANTE - 27 - CONDICIONAL
			linha.append(professorCenso.getDocenteVisitante()).append(CensoVO.SEPARADOR);
			// TIPO DE VINCULO DE DOCENTE VISITANTE Á IES - 28 - CONDICIONAL
			linha.append(professorCenso.getTipoVinculoDocenteIES()).append(CensoVO.SEPARADOR);
		} else {
			linha.append("||||").append(CensoVO.SEPARADOR);
		}
		// ATUAÇÃO DOCENTE - ENSINO EM CURSO SEQUENCIAL DE FORMAÇÃO ESPECIFICA - CAMPOS
		// 29 A 37 - CONDICIONAL
		if (professorCenso.getSituacaoDocente().equals("1")) {
			linha.append(professorCenso.getAtuacaoDoDocente(professorVO));
		} else {
			linha.append("||||||||");
		}
//		if (professorCenso.getSituacaoDocente().equals("1")) {
//			linha.append(professorCenso.getAtuacaoDoDocentePesquisa(professorVO));
//		} else {
//			linha.append("|||");
//		}
		return linha.toString();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de identificação professor no
	 * layout técnico reg.30
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	@Deprecated
	private String executarGerarRegistroProfessorLayoutTecnicoIdentificacao(StringBuilder linha,
			ProfessorCensoVO professorCenso, FuncionarioVO professorVO, PessoaVO pessoaVO, CensoVO censo,
			UsuarioVO usuario) throws Exception {
		professorVO = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(professorVO.getCodigo(),
				professorVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_IDENTIFICACAO).append(CensoVO.SEPARADOR); // 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR); // 2
		linha.append("").append(CensoVO.SEPARADOR); // 3
		linha.append(professorVO.getMatricula()).append(CensoVO.SEPARADOR); // 4
		linha.append(Uteis.retirarAcentuacao(Uteis.removerAcentuacao(professorVO.getPessoa().getNome())))
				.append(CensoVO.SEPARADOR); // 5
		linha.append("").append(CensoVO.SEPARADOR); // 6
		linha.append("").append(CensoVO.SEPARADOR); // 7
		linha.append(Uteis.getData(professorVO.getPessoa().getDataNasc(), "dd/MM/yyyy")).append(CensoVO.SEPARADOR); // 8
		linha.append(pessoaVO.getSexoCensoTecnico()).append(CensoVO.SEPARADOR); // 9
		linha.append(pessoaVO.getCorRacaCenso() + CensoVO.SEPARADOR); // 10
		if (!pessoaVO.getNomeMae().equals("") || !pessoaVO.getNomePai().equals("")) {
			linha.append("1").append(CensoVO.SEPARADOR); // 11
		} else {
			linha.append("0").append(CensoVO.SEPARADOR); // 11
		}
		if (!pessoaVO.getNomeMae().equals("") || !pessoaVO.getNomePai().equals("")) {
			if (!pessoaVO.getNomeMae().equals("")) {
				linha.append(Uteis.removerAcentuacao(pessoaVO.getNomeMae())).append(CensoVO.SEPARADOR); // 12
			} else {
				linha.append(Uteis.removerAcentuacao(pessoaVO.getNomePai())).append(CensoVO.SEPARADOR); // 12
			}
		} else {
			linha.append("").append(CensoVO.SEPARADOR); // 12
		}
		if (!pessoaVO.getNomeMae().equals("") || !pessoaVO.getNomePai().equals("")) {
			if (!pessoaVO.getNomeMae().equals("")) {
				linha.append(Uteis.removerAcentuacao(pessoaVO.getNomePai())).append(CensoVO.SEPARADOR); // 13
			} else {
				linha.append("").append(CensoVO.SEPARADOR); // 13
			}
		} else {
			linha.append("").append(CensoVO.SEPARADOR); // 13
		}
		linha.append("1").append(CensoVO.SEPARADOR); // 14
		linha.append(CensoVO.PAIS_ORIGEM).append(CensoVO.SEPARADOR); // 15
		linha.append("53").append(CensoVO.SEPARADOR); // 16
		linha.append("5300108").append(CensoVO.SEPARADOR); // 17
		linha.append(pessoaVO.getDeficienciasCenso()); // 18
		return linha.toString().toUpperCase();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de identificação gestor e professor no
	 * layout EDUCACAOBASICA reg.30
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	private String executarGerarRegistroGestorProfessorLayoutEducacaoBasicaIdentificacao(StringBuilder linha, FuncionarioVO professorVO, PessoaVO pessoaVO, CensoVO censo, boolean gestor) {
		// Seq 1
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_IDENTIFICACAO).append(CensoVO.SEPARADOR);// TIPO REGISTRO
		// Seq 2
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO
																													// DA
																													// ESCOLA
																													// -
																													// INEP
		// SEQ 3
		//linha.append(CensoVO.SEPARADOR);// IDENTIFICAÇÃO UNIDA DO PROFISSIONAL ESOLCAR EM SALA DE AULA
		// Seq 3
		linha.append(pessoaVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO DO PROFISSIONAL ESCOLAR EM SALA DE
																			// AULA NA ENTIDADE/ESCOLA
		//seq 4 Identificação única (Inep)
		linha.append("").append(CensoVO.SEPARADOR);
		
		//seq 5 Número do CPF
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 6 
		linha.append(Uteis.retirarAcentuacao(Uteis.removerAcentuacao(professorVO.getPessoa().getNome()))).append(CensoVO.SEPARADOR);// Nome completo
		// Seq 7
		linha.append(Uteis.getData(pessoaVO.getDataNasc(), "dd/MM/yyyy")).append(CensoVO.SEPARADOR);// DATA NASCIMENTO
		
		if(!Uteis.isAtributoPreenchido(professorVO.getPessoa().getFiliacaoVOs())) {
			//seq 8 Filiação
			linha.append("0").append(CensoVO.SEPARADOR);
			//seq 9 Filiação 1 (preferencialmente o nome da mãe)
			linha.append("").append(CensoVO.SEPARADOR);
			//seq 10 Filiação 2 (preferencialmente o nome do pai)
			linha.append("").append(CensoVO.SEPARADOR);
		} else {
			//seq 8 Filiação
			linha.append("1").append(CensoVO.SEPARADOR);
			
			Optional<FiliacaoVO> mae =  professorVO.getPessoa().getFiliacaoVOs().stream().filter(f -> f.getTipo().equals("MA")).findFirst();
			Optional<FiliacaoVO> pai =  professorVO.getPessoa().getFiliacaoVOs().stream().filter(f -> f.getTipo().equals("PA")).findFirst();
			
			if(mae.isPresent()) {
				//seq 9 Filiação 1 (preferencialmente o nome da mãe)
				linha.append(Uteis.removerAcentuacao(mae.get().getPais().getNome().trim())).append(CensoVO.SEPARADOR);
			}else {
				//seq 9 Filiação 1 (preferencialmente o nome da mãe)
				linha.append("").append(CensoVO.SEPARADOR);
			}
			if(pai.isPresent()) {
				//seq 10 Filiação 2 (preferencialmente o nome do pai)
				linha.append(Uteis.removerAcentuacao(pai.get().getPais().getNome().trim())).append(CensoVO.SEPARADOR);
			}else {
				//seq 10 Filiação 2 (preferencialmente o nome do pai)
				linha.append("").append(CensoVO.SEPARADOR);
			}
		}	
				
		// Seq 11
		linha.append(pessoaVO.getSexoCensoTecnico()).append(CensoVO.SEPARADOR);// SEXO
		// Seq 12
		linha.append(pessoaVO.getCorRacaCenso() + CensoVO.SEPARADOR).append(CensoVO.SEPARADOR);// COR/RAÇA,NOME COMPLETO
																								// DA MÃE
		// Seq 13
		linha.append("1").append(CensoVO.SEPARADOR);// NACIONALIDADE DO PROFISSIONAL ESCOLAR EM SALA DE AULA
		// Seq 14
		linha.append(CensoVO.PAIS_ORIGEM).append(CensoVO.SEPARADOR);// PAIS ORIGEM
		// Seq 15
		//linha.append("53").append(CensoVO.SEPARADOR);// UF DE NASCIMENTO
		// Seq 15
		linha.append("5300108").append(CensoVO.SEPARADOR);// MUNICIPIO NASCIMENTO
		
		// Seq 16 A 24
		//linha.append(pessoaVO.getDeficienciasCenso()).append(CensoVO.SEPARADOR);// DEFICIÊNCIA
		
		//Deficiência, transtorno do espectro autista ou altas habilidades/ superdotação
		linha.append("0").append(CensoVO.SEPARADOR);// Pessoa física com deficiência, transtorno do espectro autista ou altas habilidades/ superdotação - 16
		linha.append("").append(CensoVO.SEPARADOR);// Cegueira - 17
		linha.append("").append(CensoVO.SEPARADOR);// Baixa visão - 18
		linha.append("").append(CensoVO.SEPARADOR);// Surdez - 19
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência auditiva - 20
		linha.append("").append(CensoVO.SEPARADOR);// Surdocegueira - 21
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência física - 22
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência intelectual - 23
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência múltipla - 24
		linha.append("").append(CensoVO.SEPARADOR);// Transtorno do espectro autista - 25
		linha.append("").append(CensoVO.SEPARADOR);// Altas habilidades/ superdotação - 26
		
		//Recursos necessários para uso do(a) aluno(a) e para a participação em avaliações do Inep (Saeb)
		linha.append("").append(CensoVO.SEPARADOR);// Auxílio ledor - 27 
		linha.append("").append(CensoVO.SEPARADOR);// Auxílio transcrição - 28
		linha.append("").append(CensoVO.SEPARADOR);// Guia-Intérprete - 29
		linha.append("").append(CensoVO.SEPARADOR);// Tradutor-Intérprete de Libras - 30
		linha.append("").append(CensoVO.SEPARADOR);// Leitura Labial - 31
		linha.append("").append(CensoVO.SEPARADOR);// Prova Ampliada (Fonte 18) - 32
		linha.append("").append(CensoVO.SEPARADOR);// Prova superampliada (Fonte 24) - 33
		linha.append("").append(CensoVO.SEPARADOR);// CD com áudio para deficiente visual  - 34
		linha.append("").append(CensoVO.SEPARADOR);// Prova de Língua Portuguesacomo Segunda Língua para surdos e deficientes auditivos - 35
		linha.append("").append(CensoVO.SEPARADOR);// Prova em Vídeo em Libras - 36
		linha.append("").append(CensoVO.SEPARADOR);// Material didático e prova em Braille - 37
		linha.append("").append(CensoVO.SEPARADOR);// Nenhum - 38
		
		linha.append("").append(CensoVO.SEPARADOR);// Número de Identificação Social (NIS) - 39
		linha.append("").append(CensoVO.SEPARADOR);// Número da matrícula da certidão de nascimento (certidão nova) - 40
		//linha.append("").append(CensoVO.SEPARADOR);// Justificativa da falta de documentação - 41
		
		if (gestor) {
			linha.append("").append(CensoVO.SEPARADOR);// País da residência - 41
		}
		else {
			linha.append("76").append(CensoVO.SEPARADOR);// País da residência - 41
		}
		linha.append("").append(CensoVO.SEPARADOR);// CEP - 42
		linha.append("").append(CensoVO.SEPARADOR);// Município de residência - 43
		linha.append("1").append(CensoVO.SEPARADOR);// Localização/ Zona de residência - 44
		linha.append("").append(CensoVO.SEPARADOR);// Localização diferenciada - 45
		
		if(pessoaVO.getProfessor()) {			
					
			if(pessoaVO.getSiglaMaiorTitulacaoNivelEscolaridade().equals("EF")) {
				linha.append("2").append(CensoVO.SEPARADOR);// Maior nível de escolaridade concluída - 46
			}else if(pessoaVO.getSiglaMaiorTitulacaoNivelEscolaridade().equals("EM")) {
				linha.append("7").append(CensoVO.SEPARADOR);// Maior nível de escolaridade concluída - 46
			}else {
				linha.append("6").append(CensoVO.SEPARADOR);// Maior nível de escolaridade concluída - 46
			}
			
		}else {
			linha.append("").append(CensoVO.SEPARADOR);// Maior nível de escolaridade concluída - 46
		}
		
		linha.append("1").append(CensoVO.SEPARADOR);// Tipo de ensino médio cursado - 47
		
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 1 - 48
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  1 - 49
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 1 - 50
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 2 - 51
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  2 - 52
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 2 - 53
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 3 - 54
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  3 - 55
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 3 - 56
		
		//Formação/Complementação pedagógica
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 1 - 57
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 2 - 58
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 3 - 59
		
		//Pós-Graduações concluídas
		if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("GR"))) {
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("EP"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Especialização - 60
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Especialização - 60
			}
			
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("MS"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Mestrado - 61
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Mestrado - 61
			}
			
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("DR"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Doutorado - 62
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Doutorado - 62
			}
			
			if(pessoaVO.getSiglaMaiorTitulacaoNivelEscolaridade().equals("GR")) {
				linha.append("1").append(CensoVO.SEPARADOR);// Não tem pós-graduação concluída - 63
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Não tem pós-graduação concluída - 63
			}
			
		}else {
			linha.append("").append(CensoVO.SEPARADOR);// Especialização - 60
			linha.append("").append(CensoVO.SEPARADOR);// Mestrado - 61
			linha.append("").append(CensoVO.SEPARADOR);// Doutorado - 62
			linha.append("").append(CensoVO.SEPARADOR);//Não tem pós-graduação concluída - 63
			
		}
		
		//Outros cursos específicos (Formação continuada com mínimo de 80 horas)
		linha.append("0").append(CensoVO.SEPARADOR);// Creche (0 a 3 anos) - 64
		linha.append("0").append(CensoVO.SEPARADOR);// Pré-escola (4 e 5 anos) - 65
		linha.append("0").append(CensoVO.SEPARADOR);// Anos iniciais do ensino fundamental - 66
		linha.append("0").append(CensoVO.SEPARADOR);// Anos finais do ensino fundamental - 67
		linha.append("0").append(CensoVO.SEPARADOR);// Ensino médio - 68
		linha.append("0").append(CensoVO.SEPARADOR);// Educação de jovens e adultos - 69
		linha.append("0").append(CensoVO.SEPARADOR);// Educação especial - 70
		linha.append("0").append(CensoVO.SEPARADOR);// Educação Indígena - 71
		linha.append("0").append(CensoVO.SEPARADOR);// Educação do campo - 72
		linha.append("0").append(CensoVO.SEPARADOR);// Educação ambiental - 73
		linha.append("0").append(CensoVO.SEPARADOR);// Educação em direitos humanos - 74
		linha.append("0").append(CensoVO.SEPARADOR);// Gênero e diversidade sexual - 75
		linha.append("0").append(CensoVO.SEPARADOR);// Direitos de criança e adolescente - 76
		linha.append("0").append(CensoVO.SEPARADOR);// Educação para as relações étnico-raciais e História e cultura Afro-Brasileira e Africana - 77
		linha.append("0").append(CensoVO.SEPARADOR);// Gestão Escolar - 78
		linha.append("0").append(CensoVO.SEPARADOR);// Outros - 79
		linha.append("1").append(CensoVO.SEPARADOR);// Nenhum - 80
		
		if(gestor) {
			linha.append(pessoaVO.getEmail());// Endereço Eletrônico (e-mail) - 81
		}
		else {
			linha.append("");// Endereço Eletrônico (e-mail) - 81
		}
		
		return linha.toString().toUpperCase();
	}
		
	private String executarGerarRegistroAlunoLayoutEducacaoBasicaIdentificacao(StringBuilder linha,
			AlunoCensoVO alunoCenso, PessoaVO pessoaVO, CensoVO censo) {
		// Seq 1
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_IDENTIFICACAO).append(CensoVO.SEPARADOR);// TIPO REGISTRO
		// Seq 2
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO
																													// DA
																													// ESCOLA
																													// -
																													// INEP
		// SEQ 3
		//linha.append(CensoVO.SEPARADOR);// IDENTIFICAÇÃO UNIDA DO PROFISSIONAL ESOLCAR EM SALA DE AULA
		// Seq 3
		linha.append(pessoaVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO DO PROFISSIONAL ESCOLAR EM SALA DE
																			// AULA NA ENTIDADE/ESCOLA
		if(Uteis.isAtributoPreenchido(alunoCenso.getIdAlunoINEP())) {
			//seq 4 Identificação única (Inep)
			linha.append(alunoCenso.getIdAlunoINEP()).append(CensoVO.SEPARADOR);
		}else {
			//seq 4 Identificação única (Inep)
			linha.append("").append(CensoVO.SEPARADOR);
		}

		
		//seq 5 Número do CPF
		//linha.append(pessoaVO.getCPF().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 6 
		linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(pessoaVO.getNome().toUpperCase()))).append(CensoVO.SEPARADOR);// Nome completo
		// Seq 7
		linha.append(Uteis.getData(pessoaVO.getDataNasc(), "dd/MM/yyyy")).append(CensoVO.SEPARADOR);// DATA NASCIMENTO
		
		if(!Uteis.isAtributoPreenchido(alunoCenso.getNomeMae()) && !Uteis.isAtributoPreenchido(alunoCenso.getNomePai())) {
			//seq 8 Filiação
			linha.append("0").append(CensoVO.SEPARADOR);
			//seq 9 Filiação 1 (preferencialmente o nome da mãe)
			linha.append("").append(CensoVO.SEPARADOR);
			//seq 10 Filiação 2 (preferencialmente o nome do pai)
			linha.append("").append(CensoVO.SEPARADOR);
		}else {
			//seq 8 Filiação
			linha.append("1").append(CensoVO.SEPARADOR);
			if(Uteis.isAtributoPreenchido(alunoCenso.getNomeMae())){
				//seq 9 Filiação 1 (preferencialmente o nome da mãe)
				linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(alunoCenso.getNomeMae().trim()))).append(CensoVO.SEPARADOR);
			}else {
				//seq 9 Filiação 1 (preferencialmente o nome da mãe)
				linha.append("").append(CensoVO.SEPARADOR);
			}
			if(Uteis.isAtributoPreenchido(alunoCenso.getNomePai())){
				//seq 10 Filiação 2 (preferencialmente o nome do pai)
				linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(alunoCenso.getNomePai().trim()))).append(CensoVO.SEPARADOR);
			}else {
				//seq 10 Filiação 2 (preferencialmente o nome do pai)
				linha.append("").append(CensoVO.SEPARADOR);
			}
		}			
		
		// Seq 11
		linha.append(pessoaVO.getSexoCensoTecnico()).append(CensoVO.SEPARADOR);// SEXO
		// Seq 12
		linha.append(pessoaVO.getCorRacaCenso()).append(CensoVO.SEPARADOR);// COR/RAÇA,NOME COMPLETO
																								// DA MÃE
		// Seq 13
		linha.append("1").append(CensoVO.SEPARADOR);// NACIONALIDADE DO PROFISSIONAL ESCOLAR EM SALA DE AULA
		// Seq 14
		linha.append(CensoVO.PAIS_ORIGEM).append(CensoVO.SEPARADOR);// PAIS ORIGEM
		// Seq 15
		//linha.append("53").append(CensoVO.SEPARADOR);// UF DE NASCIMENTO
		// Seq 15
		//linha.append("5300108").append(CensoVO.SEPARADOR);// MUNICIPIO NASCIMENTO
		linha.append(pessoaVO.getNaturalidade().getCodigoIBGE()).append(CensoVO.SEPARADOR); // 15
		
		// Seq 16 A 24
		//linha.append(pessoaVO.getDeficienciasCenso()).append(CensoVO.SEPARADOR);// DEFICIÊNCIA
		
		//Deficiência, transtorno do espectro autista ou altas habilidades/ superdotação
		linha.append("0").append(CensoVO.SEPARADOR);// Pessoa física com deficiência, transtorno do espectro autista ou altas habilidades/ superdotação - 16
		linha.append("").append(CensoVO.SEPARADOR);// Cegueira - 17
		linha.append("").append(CensoVO.SEPARADOR);// Baixa visão - 18
		linha.append("").append(CensoVO.SEPARADOR);// Surdez - 19
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência auditiva - 20
		linha.append("").append(CensoVO.SEPARADOR);// Surdocegueira - 21
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência física - 22
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência intelectual - 23
		linha.append("").append(CensoVO.SEPARADOR);// Deficiência múltipla - 24
		linha.append("").append(CensoVO.SEPARADOR);// Transtorno do espectro autista - 25
		linha.append("").append(CensoVO.SEPARADOR);// Altas habilidades/ superdotação - 26
		
		//Recursos necessários para uso do(a) aluno(a) e para a participação em avaliações do Inep (Saeb)
		linha.append("").append(CensoVO.SEPARADOR);// Auxílio ledor - 27 
		linha.append("").append(CensoVO.SEPARADOR);// Auxílio transcrição - 28
		linha.append("").append(CensoVO.SEPARADOR);// Guia-Intérprete - 29
		linha.append("").append(CensoVO.SEPARADOR);// Tradutor-Intérprete de Libras - 30
		linha.append("").append(CensoVO.SEPARADOR);// Leitura Labial - 31
		linha.append("").append(CensoVO.SEPARADOR);// Prova Ampliada (Fonte 18) - 32
		linha.append("").append(CensoVO.SEPARADOR);// Prova superampliada (Fonte 24) - 33
		linha.append("").append(CensoVO.SEPARADOR);// CD com áudio para deficiente visual  - 34
		linha.append("").append(CensoVO.SEPARADOR);// Prova de Língua Portuguesacomo Segunda Língua para surdos e deficientes auditivos - 35
		linha.append("").append(CensoVO.SEPARADOR);// Prova em Vídeo em Libras - 36
		linha.append("").append(CensoVO.SEPARADOR);// Material didático e prova em Braille - 37
		linha.append("").append(CensoVO.SEPARADOR);// Nenhum - 38
		
		linha.append("").append(CensoVO.SEPARADOR);// Número de Identificação Social (NIS) - 39
		linha.append("").append(CensoVO.SEPARADOR);// Número da matrícula da certidão de nascimento (certidão nova) - 40
		//linha.append("").append(CensoVO.SEPARADOR);// Justificativa da falta de documentação - 41
		
		linha.append("76").append(CensoVO.SEPARADOR);// País da residência - 41
		linha.append("").append(CensoVO.SEPARADOR);// CEP - 42
		linha.append("").append(CensoVO.SEPARADOR);// Município de residência - 43
		linha.append("1").append(CensoVO.SEPARADOR);// Localização/ Zona de residência - 44
		linha.append("").append(CensoVO.SEPARADOR);// Localização diferenciada - 45
					

		linha.append("").append(CensoVO.SEPARADOR);// Maior nível de escolaridade concluída - 46
		
		if(!pessoaVO.getSiglaMaiorTitulacaoNivelEscolaridade().equals("EF")) {
			linha.append("1").append(CensoVO.SEPARADOR);// Tipo de ensino médio cursado - 47	
		}else {
			linha.append("").append(CensoVO.SEPARADOR);// Tipo de ensino médio cursado - 47	
		}
		
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 1 - 48
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  1 - 49
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 1 - 50
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 2 - 51
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  2 - 52
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 2 - 53
		linha.append("").append(CensoVO.SEPARADOR);// Código do Curso 3 - 54
		linha.append("").append(CensoVO.SEPARADOR);// Ano de Conclusão  3 - 55
		linha.append("").append(CensoVO.SEPARADOR);// Instituição de educação superior 3 - 56
		
		//Formação/Complementação pedagógica
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 1 - 57
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 2 - 58
		linha.append("").append(CensoVO.SEPARADOR);// Área do conhecimento/ componentes curriculares 3 - 59
		
		//Pós-Graduações concluídas
		if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("GR"))) {
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("EP"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Especialização - 60
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Especialização - 60
			}
			
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("MS"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Mestrado - 61
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Mestrado - 61
			}
			
			if(pessoaVO.getFormacaoAcademicaVOs().stream().anyMatch(fa -> fa.getEscolaridade().equals("DR"))) {
				linha.append("1").append(CensoVO.SEPARADOR);// Doutorado - 62
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Doutorado - 62
			}
			
			if(pessoaVO.getSiglaMaiorTitulacaoNivelEscolaridade().equals("GR")) {
				linha.append("1").append(CensoVO.SEPARADOR);// Não tem pós-graduação concluída - 63
			}else {
				linha.append("0").append(CensoVO.SEPARADOR);// Não tem pós-graduação concluída - 63
			}
			
		}else {
			linha.append("").append(CensoVO.SEPARADOR);// Especialização - 60
			linha.append("").append(CensoVO.SEPARADOR);// Mestrado - 61
			linha.append("").append(CensoVO.SEPARADOR);// Doutorado - 62
			linha.append("").append(CensoVO.SEPARADOR);//Não tem pós-graduação concluída - 63
			
		}
		
		//Outros cursos específicos (Formação continuada com mínimo de 80 horas)
		linha.append("").append(CensoVO.SEPARADOR);// Creche (0 a 3 anos) - 64
		linha.append("").append(CensoVO.SEPARADOR);// Pré-escola (4 e 5 anos) - 65
		linha.append("").append(CensoVO.SEPARADOR);// Anos iniciais do ensino fundamental - 66
		linha.append("").append(CensoVO.SEPARADOR);// Anos finais do ensino fundamental - 67
		linha.append("").append(CensoVO.SEPARADOR);// Ensino médio - 68
		linha.append("").append(CensoVO.SEPARADOR);// Educação de jovens e adultos - 69
		linha.append("").append(CensoVO.SEPARADOR);// Educação especial - 70
		linha.append("").append(CensoVO.SEPARADOR);// Educação Indígena - 71
		linha.append("").append(CensoVO.SEPARADOR);// Educação do campo - 72
		linha.append("").append(CensoVO.SEPARADOR);// Educação ambiental - 73
		linha.append("").append(CensoVO.SEPARADOR);// Educação em direitos humanos - 74
		linha.append("").append(CensoVO.SEPARADOR);// Gênero e diversidade sexual - 75
		linha.append("").append(CensoVO.SEPARADOR);// Direitos de criança e adolescente - 76
		linha.append("").append(CensoVO.SEPARADOR);// Educação para as relações étnico-raciais e História e cultura Afro-Brasileira e Africana - 77
		linha.append("").append(CensoVO.SEPARADOR);// Gestão Escolar - 78
		linha.append("").append(CensoVO.SEPARADOR);// Outros - 79
		linha.append("").append(CensoVO.SEPARADOR);// Nenhum - 80
		linha.append("");// email - 81
		
		return linha.toString().toUpperCase();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de documentação professor no
	 * layout tecnico reg.
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	@Deprecated
	private String executarGerarRegistroProfessorLayoutTecnicoDocumento(StringBuilder linha,
			ProfessorCensoVO professorCenso, FuncionarioVO professorVO, PessoaVO pessoaVO, CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_DOCUMENTO).append(CensoVO.SEPARADOR);
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);
		linha.append(professorVO.getMatricula()).append(CensoVO.SEPARADOR);
		linha.append(professorVO.getPessoa().getCPF().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);
		linha.append("|||||||");
		return linha.toString().toUpperCase();
	}

	
	private String executarGerarRegistroGestorLayoutEducacaoBasica(StringBuilder linha, FuncionarioVO gestorVO, PessoaVO pessoaVO, CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_DOCUMENTO).append(CensoVO.SEPARADOR);// TIPO REGISTRO - 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO
																													// INEP
																													// -
																													// 2
		//linha.append("|");// Identificação única do Profissional escolar em sala de Aula (INEP) - 3
		linha.append(pessoaVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO PROFISSIONAL ESCOLAR EM SALA DE
																			// AULA NA ENTIDADE/ESCOLA - 3
		//linha.append(professorVO.getPessoa().getCPF().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);// NUMERO
																													// CPF
																													// -
		linha.append("").append(CensoVO.SEPARADOR);//	Identificação única (Inep)	// 4
		linha.append("2").append(CensoVO.SEPARADOR);//	Cargo	// 5
		linha.append("").append(CensoVO.SEPARADOR);//	Critério de acesso ao cargo/função	// 6
		//linha.append("").append(CensoVO.SEPARADOR);//	Especificação do critério de acesso	// 7
		linha.append("").append(CensoVO.SEPARADOR);//	Situação Funcional/ Regime de contratação/Tipo de vínculo	// 7
		
		
		//linha.append("|");// Localização/Zona de residência - 6
		//linha.append("|");// CEP -7
		//linha.append("|");// Endereço - 8
		//linha.append("|");// Número do Endereço - 9
		//linha.append("|");// Complemento - 10
		//linha.append("|");// Bairro - 11
		//linha.append("|");// UF - 12
		//linha.append("|");// Municipio - 13
		return linha.toString().toUpperCase();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de dados variáveis professor no
	 * layout técnico
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	@Deprecated
	private String executarGerarRegistroProfessorLayoutTecnicoDadosVariaveis(StringBuilder linha,
			ProfessorCensoVO professorCenso, FuncionarioVO professorVO, PessoaVO pessoaVO, CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_DADOS_VARIAVEIS).append(CensoVO.SEPARADOR); // 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR); // 2
		linha.append("").append(CensoVO.SEPARADOR); // 3
		linha.append(professorVO.getMatricula()).append(CensoVO.SEPARADOR); // 4
		linha.append(professorCenso.getEscolaridade()).append(CensoVO.SEPARADOR);// 5
		linha.append("||||||||||||||||||||||");
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 28
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 29
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 30
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 31
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 32
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 33
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 34
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 35
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 36
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 37
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 38
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 39
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 40
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 41
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR);// 42
		linha.append("1");// 43
		return linha.toString().toUpperCase();
	}

	/***
	 * 
	 * Método responsável por trazer os registros de dados de docência professor no
	 * layout técnico
	 * 
	 * @param linha
	 * @param professorCenso
	 * @param professorVO
	 * @param pessoaVO
	 * @return
	 */
	@Deprecated
	private String executarGerarRegistroProfessorLayoutTecnicoDadosDocencia(StringBuilder linha,
			ProfessorCensoVO professorCenso, FuncionarioVO professorVO, PessoaVO pessoaVO, TurmaCensoVO turmaCensoVO,
			CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_PROFESSOR_DADO_DOCENCIA).append(CensoVO.SEPARADOR); // 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR); // 2
		linha.append("").append(CensoVO.SEPARADOR);// 3
		linha.append(professorVO.getMatricula()).append(CensoVO.SEPARADOR); // 4
		linha.append("").append(CensoVO.SEPARADOR);// 5
		linha.append(turmaCensoVO.getCodigo()).append(CensoVO.SEPARADOR); // 6
		linha.append(CensoVO.FUNCAO_EXERCE_ESCOLA_TURMA_CADASTRO_PROFESSOR).append(CensoVO.SEPARADOR); // 7
		linha.append("").append(CensoVO.SEPARADOR); // 8
		int aux = 0;
		for (TurmaCensoVO turmaCensoVO2 : professorCenso.getListaTurmaCensoVOs()) {
			if (turmaCensoVO.getCodigo().equals(turmaCensoVO2.getCodigo())) {
				aux = 1;
				linha.append("17").append(CensoVO.SEPARADOR); // 9
				break;
			}
		}
		if (aux == 0) {
			linha.append("").append(CensoVO.SEPARADOR); // 9
		}
		linha.append("").append(CensoVO.SEPARADOR); // 10
		linha.append("").append(CensoVO.SEPARADOR); // 11
		linha.append("").append(CensoVO.SEPARADOR); // 12
		linha.append("").append(CensoVO.SEPARADOR); // 13
		linha.append("").append(CensoVO.SEPARADOR); // 14
		linha.append("").append(CensoVO.SEPARADOR); // 15
		linha.append("").append(CensoVO.SEPARADOR); // 16
		linha.append("").append(CensoVO.SEPARADOR); // 17
		linha.append("").append(CensoVO.SEPARADOR); // 18
		linha.append("").append(CensoVO.SEPARADOR); // 19
		linha.append("").append(CensoVO.SEPARADOR); // 20
		linha.append(""); // 21
		return linha.toString().toUpperCase();
	}


	private String executarGerarRegistroProfessorTurmaLayoutEducacaoBasica(StringBuilder linha, FuncionarioVO professorVO, TurmaCensoVO turmaCensoVO, CensoVO censo) {
		
		linha.append("50").append(CensoVO.SEPARADOR);// TIPO REGISTRO
																										// -1
		linha.append(censo.getUnidadeEnsino().getCodigoIES() + CensoVO.SEPARADOR).append(CensoVO.SEPARADOR);// CODIGO
																											// ESCOLA -
																											// INEP -2
		
		linha.append(professorVO.getPessoa().getCodigo()).append(CensoVO.SEPARADOR);// CODIGO PROFISSIONAL
																								// ESCOLAR EM SALA DE
																								// AULA NA
																								// ENTIDADE/ESCOLA - 3
		linha.append("").append(CensoVO.SEPARADOR);// IDENTIFICAÇÃO UNICA(INEP) - 4
		linha.append(turmaCensoVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO DA TURMA NA ENTIDADE/ESCOLA - 5
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TURMA NO INEP - 6
	
		linha.append(CensoVO.FUNCAO_EXERCE_ESCOLA_TURMA_CADASTRO_PROFESSOR)
				.append(CensoVO.SEPARADOR);// FUNÇÃO QUE EXERCE NA ESCOLA/TURMA - 7
		// CAMPOS 08
		linha.append(CensoVO.SITUACAO_REGIME_REGIME_CONTRATACAO_TIPO_VINCULO).append(CensoVO.SEPARADOR);// SITUACAO
																										// FUNCIONAL/
																										// REGIME
																										// CONTRATACAO
																										// TIPO VINCULO
		if(turmaCensoVO.getEtapaEnsino().equals("1") || turmaCensoVO.getEtapaEnsino().equals("2") || turmaCensoVO.getEtapaEnsino().equals("3")) {
		linha.append("").append(CensoVO.SEPARADOR);// Código 1 - 9
		}
		else {
			if(turmaCensoVO.getNivelEducacional().equals("PR")) {
				linha.append("17").append(CensoVO.SEPARADOR);// Código 1 - 9
			}
			else {
			linha.append("99").append(CensoVO.SEPARADOR);// Código 1 - 9
		}
		
		}
		
		linha.append("").append(CensoVO.SEPARADOR);// Código 2 - 10
		linha.append("").append(CensoVO.SEPARADOR);// Código 3 - 11
		linha.append("").append(CensoVO.SEPARADOR);// Código 4 - 12
		linha.append("").append(CensoVO.SEPARADOR);// Código 5 - 13
		linha.append("").append(CensoVO.SEPARADOR);// Código 6 - 14
		linha.append("").append(CensoVO.SEPARADOR);// Código 7 - 15
		linha.append("").append(CensoVO.SEPARADOR);// Código 8 - 16
		linha.append("").append(CensoVO.SEPARADOR);// Código 9 - 17
		linha.append("").append(CensoVO.SEPARADOR);// Código 10 - 18
		linha.append("").append(CensoVO.SEPARADOR);// Código 11 - 19
		linha.append("").append(CensoVO.SEPARADOR);// Código 12 - 20
		linha.append("").append(CensoVO.SEPARADOR);// Código 13 - 21
		linha.append("").append(CensoVO.SEPARADOR);// Código 14 - 22
		linha.append("").append(CensoVO.SEPARADOR);// Código 15 - 23
		return linha.toString().toUpperCase();
	}

	/***
	 * Método responsável por criar linha a linha do arquivo do aluno
	 * 
	 * @param censo
	 * @throws Exception
	 */

	private void executarCriarRegistroAlunos(CensoVO censo, List<AlunoCensoVO> listaAlunoCenso,  XSSFSheet worksheet, ProgressBarVO progressBarVO) throws Exception {
		StringBuilder linha = new StringBuilder();
		
		for (AlunoCensoVO censoAlunoItemVO : listaAlunoCenso) {
			progressBarVO.setStatus("Registrando aluno no arquivo de exportação...");
			Integer anoIngresso = null;
			if (!censoAlunoItemVO.getListaCursoCenso().isEmpty()) {
				Ordenacao.ordenarLista(censoAlunoItemVO.getListaCursoCenso(), "anoIngresso");
				anoIngresso = censoAlunoItemVO.getListaCursoCenso().get(0).getAnoIngresso();
			}
			censo.getPwAluno().println(executarGerarRegistroAluno(linha, anoIngresso, censoAlunoItemVO, worksheet));
			linha.setLength(0);
			for (CursoCensoVO cursoDoAluno : censoAlunoItemVO.getListaCursoCenso()) {
				censo.getPwAluno().println(executarGerarRegistroVinculoAlunoComCurso(linha, censoAlunoItemVO, cursoDoAluno, censo, worksheet));
				linha.setLength(0);
			}
			progressBarVO.incrementarSemStatus();
		}
	}
	
	private void realizarCriacaoCabecalhoAlunoArquivoExcel(XSSFSheet worksheet) {
		int coluna  = 0;
		XSSFRow row = worksheet.createRow(0);
		XSSFCell cell = row.createCell(coluna);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Tipo Registro - 41");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("ID Aluno Inep");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Nome");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("CPF");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Doc Estrangeiro");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Data Nasc.");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Cor/Raça");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Nacionalidade");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("UF Nascimento");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Cidade Nascimento");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Pais Nascimento");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Aluno C/ Deficiência");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Cegueira");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Baixa Visão");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Surdez");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Auditiva");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Deficiência Física");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Surdo Cegueira");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Intelectual");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Autismo");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Superdotado");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Tipo Escola Ensino Médio");
		
		
		
	}
	
	private void realizarCriacaoCabecalhoCursoArquivoExcel(XSSFSheet worksheet) {
		int coluna  = 0;
		XSSFRow row = worksheet.createRow(1);
		XSSFCell cell = row.createCell(coluna);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Tipo Registro - 42");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("ID Aluno Inep");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Semestre");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("ID Polo EAD");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Turno");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Sit. Matrícula");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Curso Origem");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Semestre Conclusão");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Aluno PARFOR");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("2ª Licenciatura");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Tipo 2ª Licenciatura");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Data Ingresso");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Vestibular");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Enem");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Avaliação Seriada");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Seleção Simplificada");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Egresso BI/LI");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("PEC-G");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Transf. Ex Officio");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Decisão Judicial");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Vagas Remanescentes");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Programas Especiais");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Mobilidade Acadêmica");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Tipo Mobilidade Acadêmica");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Mobilidade Acadêmica - Nacional");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Mobilidade Acadêmica - Internacional");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Programa de Vagas");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Financiamento Estudantil");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Fies");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Governo Estadual");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Governo Municipal");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("IES");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Entidades Externas");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Prouni Integral");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Prouni Parcial Não Reembolsável");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Entidades Externas Não Reembolsável");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Governo Estado Não Reembolsável");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("IES Não Reembolsável");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Governo Municipal Não Reembolsável");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Apoio Social");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Apoio Social");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Alimentação");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Moradia");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Transporte");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Material Didático");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Bolsa Trabalho");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Bolsa Permanência");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Atividade Complementar");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Pesquisa");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Pesquisa Remunerada");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Extensão");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Extensão Remunerada");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Monitoria");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Monitoria Remunerada");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Estágio Não Obrigatório");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Estágio Não Obrigatório Remunerado");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("CH Total Curso");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("CH Total Integralizado");
		cell = row.createCell(coluna++);
		cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Justificativa");
	}

	//reg 60 EDUCACAOBASICA
	private void executarCriarRegistroAlunoLayoutEducacaoBasica(CensoVO censo,
			UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		for (AlunoCensoVO censoAlunoItemVO : censo.getListaAlunoCenso()) {
			// CADASTRO DE ALUNO - IDENTIFICAÇÃO - REGISTRO 60

			linha = new StringBuilder();
			// CADASTRO DE ALUNO - VINCULO (MATRICULA) - REGISTRO 60
			censo.getPwLayoutEducacaoBasica().println(
					executarGerarRegistroAlunoLayoutEducacaoBasicaVinculoMatricula(linha, censoAlunoItemVO, censo));
			linha.setLength(0);
		}
	}

	private void executarCriarRegistroFinalArquivoLayoutEducacaoBasica(CensoVO censo) throws Exception {
		StringBuilder linha = new StringBuilder();
		linha.append("99").append(CensoVO.SEPARADOR);// TIPO DE REGISTRO - 1
		censo.getPwLayoutEducacaoBasica().println(linha.toString().toUpperCase());
	}

	@Deprecated
	private void executarCriarRegistroAlunosLayoutTecnico(CensoVO censo,
			UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		for (AlunoCensoVO censoAlunoItemVO : censo.getListaAlunoCenso()) {
			censo.getPwLayoutTecnico().println(
					executarGerarRegistroAlunoLayoutTecnicoIdentificacao(linha, censoAlunoItemVO, censo, usuario));
			linha = new StringBuilder();
			censo.getPwLayoutTecnico()
					.println(executarGerarRegistroAlunoLayoutTecnicoDocumentoEndereco(linha, censoAlunoItemVO, censo));
			linha = new StringBuilder();
			censo.getPwLayoutTecnico()
					.println(executarGerarRegistroAlunoLayoutTecnicoVinculoMatricula(linha, censoAlunoItemVO, censo));
			linha.setLength(0);
			if (censo.getLayout().equals(Censo.GRADUACAO)) {
				for (CursoCensoVO cursoDoAluno : censoAlunoItemVO.getListaCursoCenso()) {
					censo.getPwLayoutTecnico()
							.println(executarGerarRegistroVinculoAlunoComCurso(linha, censoAlunoItemVO, cursoDoAluno, censo, censo.getWorksheet()));
					linha.setLength(0);
				}
			}
		}
	}

	/***
	 * Método responsável por gerar uma linha do curso de um aluno
	 * 
	 * @param linha
	 * @param alunoCenso
	 * @param cursoDoAluno
	 * @param matriculaVO
	 * @param alunoVO
	 * @return
	 * @throws ParseException
	 */
	private String executarGerarRegistroVinculoAlunoComCurso(StringBuilder linha, AlunoCensoVO alunoCenso,
			CursoCensoVO cursoDoAluno, CensoVO censo, XSSFSheet worksheet) throws ParseException {
		try {
			XSSFRow row = worksheet.createRow(worksheet.getLastRowNum()+1);
			int coluna = 0;
			// 1 - LINHA QUE REFERE AO CAMPO TIPO DE REGISTRO DE TAMANHO 2 - OBRIGATORIO
			XSSFCell cell = row.createCell(coluna);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getTipoRegistro());
			linha.append(cursoDoAluno.getTipoRegistro()).append(CensoVO.SEPARADOR);
			
			// 2 - LINHA QUE REFERE AO CAMPO ID na IES - Identificação única do aluno na IES - opcional
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCenso.getCodigoAluno());
			linha.append(alunoCenso.getCodigoAluno()).append(CensoVO.SEPARADOR);
			
			// 3 - LINHA QUE REFERE AO CAMPO SEMESTRE DE REFERÊNCIA - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(censo.getUnidadeEnsino().getDependenciaAdministrativa().equals("FE")) {
				cell.setCellValue(Uteis.isAtributoPreenchido(censo.getDataBase()) ? Uteis.getSemestreDataCenso(censo.getDataBase()) : "");
				linha.append("").append(CensoVO.SEPARADOR);
			} else {
				linha.append(CensoVO.SEPARADOR);
			}
			
			// 4 - LINHA QUE REFERE AO CAMPO DO CODIGO DO CURSO - OBRIGATORIO
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getIdCursoInep());
			linha.append(cursoDoAluno.getIdCursoInep()).append(CensoVO.SEPARADOR);
			
			// 5 - LINHA QUE REFERE AO CAMPO DE CODIGO DO POLO CURSO A DISTANCIA - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getCodigoPoloEADINEP());
			linha.append(cursoDoAluno.getCodigoPoloEADINEP()).append(CensoVO.SEPARADOR);
			

			// 6 - LINHA QUE REFERE AO TURNO DO ALUNO - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getTurnoAluno());
			linha.append(cursoDoAluno.getTurnoAluno()).append(CensoVO.SEPARADOR);
			
			// 7 - LINHA QUE REFERE A SITUACAO MATRICULA DO ALUNO - OBRIGATORIO
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			SituacaoVinculoMatricula situacaoVinculoMatricula = SituacaoVinculoMatricula.getEnum(cursoDoAluno.getSituacaoVinculo());
			if (situacaoVinculoMatricula != null) {
				if(censo.getTratarAbandonoCurso().equals("CA") && situacaoVinculoMatricula.equals(SituacaoVinculoMatricula.ABANDONO_CURSO)) {
					cell.setCellValue("4");
					linha.append("4").append(CensoVO.SEPARADOR);
				}else {
					cell.setCellValue(situacaoVinculoMatricula.getCodigoCenso());
					linha.append(situacaoVinculoMatricula.getCodigoCenso()).append(CensoVO.SEPARADOR);
				}
				cursoDoAluno.setSituacaoVinculo(situacaoVinculoMatricula.getDescricao());
			} else {
				cell.setCellValue("2");
				linha.append("2").append(CensoVO.SEPARADOR);
			}
			
			// 8 - LINHA QUE REFERE AO CURSO ORIGEM QUANDO TRASNFERENCIAINTERNA - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (cursoDoAluno.getCursoOrigem() != null && !cursoDoAluno.getCursoOrigem().equals(0) && situacaoVinculoMatricula.getCodigoCenso() != 5) {
				cell.setCellValue(cursoDoAluno.getCursoOrigem());
				linha.append(cursoDoAluno.getCursoOrigem()).append(CensoVO.SEPARADOR);
			} else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}

			// 9 - LINHA REFERENTE AO SEMESTRE DE CONCLUSAO - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (!censo.getUnidadeEnsino().getDependenciaAdministrativa().equals("FE") && situacaoVinculoMatricula != null && (situacaoVinculoMatricula.equals(SituacaoVinculoMatricula.FORMADO) || situacaoVinculoMatricula.equals(SituacaoVinculoMatricula.FINALIZADA))) {
				cell.setCellValue(cursoDoAluno.getSemestreconclusao());
				linha.append(cursoDoAluno.getSemestreconclusao()).append(CensoVO.SEPARADOR);
			} else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			// 10 - LINHA REFERENTE AO ALUNO PARFOR - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (cursoDoAluno.getTitulo().equals("LI")) {
				cell.setCellValue("");
				linha.append("0").append(CensoVO.SEPARADOR);
			} else {
				cell.setCellValue("");
				linha.append(CensoVO.SEPARADOR);
			}
			// 11 - LINHA REFERENTE Segunda Licenciatura / Formação pedagógica - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("");
			linha.append(CensoVO.SEPARADOR);
			
			// 12 - LINHA REFERENTE Tipo - Segunda Licenciatura / Formação pedagógica - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("");
			linha.append(CensoVO.SEPARADOR);
			
			
			// 13 - LINHA REFERENTE A DATA(semestre) DE INGRESSO NO CURSO - OBRIGATORIO
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (cursoDoAluno.getDataIngresso() != null && !cursoDoAluno.getDataIngresso().isEmpty()) {
				cell.setCellValue(cursoDoAluno.getDataIngresso());
				linha.append(cursoDoAluno.getDataIngresso()).append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("");
				linha.append(CensoVO.SEPARADOR);
			}
			
			
			// Forma de ingresso/seleção - Vestibular - 14
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.VESTIBULAR.getValor()) 
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor())
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.ENTREVISTA.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Enem - 15
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.ENEM.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Avaliação Seriada - 16
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.AVALIACAO_SERIADA.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Seleção Simplificada - 17
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.SELECAO_SIMPLIFICADA.getValor())) {
				cell.setCellValue("0");
				linha.append("1").append(CensoVO.SEPARADOR);
			}
			else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção -  Egresso BI/LI- 18
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("0");
			linha.append("0").append(CensoVO.SEPARADOR);
			
			// Forma de ingresso/seleção - PEC-G - 19
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);			
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.PECG.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Transferência Ex Officio - 20
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA_OFICIO.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}
			else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Decisão judicial - 21
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.DECISAO_JUDICIAL.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}
			else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Seleção para Vagas Remanescentes - 22
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.VAGAS_REMANESCENTES.getValor()) 
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.TRANSFERENCIA_INTERNA.getValor())
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor())
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.REINGRESSO.getValor())
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.PORTADOR_DE_DIPLOMA.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}
			else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			// Forma de ingresso/seleção - Seleção para Vagas de Programas Especiais - 23
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS.getValor()) 
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.VAGAS_PROGRAMAS_ESPECIAIS_FIES.getValor())
					|| cursoDoAluno.getOutrasFormasDeIngresso().equals(FormaIngresso.PROUNI.getValor())) {
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
			}
			else {
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
			}
			
			
			// 24 - MOBILIDADE ACADEMICA - CONDICIONAL (MOBILIDADE ACADEMICA EQUIVALE A
			// INTERCAMBIO)
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getMobilidadeAcademica());
			linha.append(cursoDoAluno.getMobilidadeAcademica()).append(CensoVO.SEPARADOR);
			
			// 25 - LINHA REFERENTE A TIPO MODALIDADE ACADEMICA - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getTipoMobilidadeAcademica());
			linha.append(cursoDoAluno.getTipoMobilidadeAcademica()).append(CensoVO.SEPARADOR);
			
			// 26 - LINHA REFERENTE A IES DESTINO - CONDICIONAL
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (cursoDoAluno.getTipoMobilidadeAcademica().equals(TipoMobilidadeAcademicaEnum.NACIONAL.getValor())) {
				cell.setCellValue(cursoDoAluno.getMobilidadeAcademicaComplemento());
				linha.append(cursoDoAluno.getMobilidadeAcademicaComplemento()).append(CensoVO.SEPARADOR);
			} else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			
			// 27 - LINHA REFERENTE A Mobilidade acadêmica Internacional - País destino
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if (cursoDoAluno.getTipoMobilidadeAcademica().equals(TipoMobilidadeAcademicaEnum.INTERNACIONAL.getValor())) {
				cell.setCellValue(cursoDoAluno.getMobilidadeAcademicaComplemento());
				linha.append(cursoDoAluno.getMobilidadeAcademicaComplemento()).append(CensoVO.SEPARADOR);
			} else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			
			if(Uteis.isAtributoPreenchido(cursoDoAluno.getFinanciamentoEstudantil())) {
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
				
				if(Uteis.isAtributoPreenchido(cursoDoAluno.getListaFinanciamentoEstudantilVOs())) {
					
					// 35 - Financiamento Estudantil Reembolsável- FIES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.FIES.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 36 - Financiamento Estudantil Reembolsável- Governo Estadual
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.GOVERNO_ESTADUAL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 37 - Financiamento Estudantil Reembolsável- Governo Municipal
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.GOVERNO_MUNICIPAL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 38 - Financiamento Estudantil Reembolsável- IES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.IES.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 39 - Financiamento Estudantil Reembolsável- Entidades externas
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.ENTIDADES_EXTERNAS.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 40 - Tipo de financiamento não reembolsável - ProUni integral
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.PROUNI_INTEGRAL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 41 - Tipo de financiamento não reembolsável - ProUni parcial
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.PROUNI_PARCIAL_NAO_REEMBOLSAVEL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 42 - Tipo de financiamento não reembolsável - Entidades externas
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.ENTIDADES_EXTERNAS_NAO_REEMBOLSAVEL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 43 - Tipo de financiamento não reembolsável - Governo estadual
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.GOVERNO_ESTADUAL_NAO_REEMBOLSAVEL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 44 - Tipo de financiamento não reembolsável - IES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.IES_NAO_REEMBOLSAVEL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}
					
					// 45 - Tipo de financiamento não reembolsável - Governo municipal
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					if(cursoDoAluno.getListaFinanciamentoEstudantilVOs().contains(FinanciamentoEstudantil.GOVERNO_MUNICIPAL_NAO_REEMBOLSAVEL.getValor())) {
						cell.setCellValue("1");
						linha.append("1").append(CensoVO.SEPARADOR);
					}
					else {
						cell.setCellValue("0");
						linha.append("0").append(CensoVO.SEPARADOR);
					}

				}else {
					// 35 - Financiamento Estudantil Reembolsável- FIES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 36 - Financiamento Estudantil Reembolsável- Governo Estadual
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 37 - Financiamento Estudantil Reembolsável- Governo Municipal
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 38 - Financiamento Estudantil Reembolsável- IES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 39 - Financiamento Estudantil Reembolsável- Entidades externas
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 40 - Tipo de financiamento não reembolsável - ProUni integral
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 41 - Tipo de financiamento não reembolsável - ProUni parcial
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 42 - Tipo de financiamento não reembolsável - Entidades externas
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 43 - Tipo de financiamento não reembolsável - Governo estadual
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 44 - Tipo de financiamento não reembolsável - IES
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);
					// 45 - Tipo de financiamento não reembolsável - Governo municipal
					cell = row.createCell(coluna++);
					cell.setCellType(XSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("");
					linha.append("").append(CensoVO.SEPARADOR);

				}
			}
			else {
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
				// 35 - Financiamento Estudantil Reembolsável- FIES
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 36 - Financiamento Estudantil Reembolsável- Governo Estadual
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 37 - Financiamento Estudantil Reembolsável- Governo Municipal
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 38 - Financiamento Estudantil Reembolsável- IES
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 39 - Financiamento Estudantil Reembolsável- Entidades externas
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 40 - Tipo de financiamento não reembolsável - ProUni integral
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 41 - Tipo de financiamento não reembolsável - ProUni parcial
				linha.append("").append(CensoVO.SEPARADOR);
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				// 42 - Tipo de financiamento não reembolsável - Entidades externas
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 43 - Tipo de financiamento não reembolsável - Governo estadual
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 44 - Tipo de financiamento não reembolsável - IES
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 45 - Tipo de financiamento não reembolsável - Governo municipal
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}										
			
			/*linha.append(alunoCenso.getFinanciamentoEstudantil(cursoDoAluno.getListaFinanciamentoEstudantilVOs()))
					.append(CensoVO.SEPARADOR);
			FinanciamentoEstudantil financiamentoEstudantil = FinanciamentoEstudantil
					.getEnum(cursoDoAluno.getFinanciamentoEstudantil());
			if (financiamentoEstudantil != null) {
				cursoDoAluno.setFinanciamentoEstudantil(financiamentoEstudantil.getDescricao());
			}*/
			// 46 - LINHA REFERENTE AO APOIO SOCIAL - OBRIGATORIO a 52
			
			// 46 - LINHA REFERENTE AO APOIO SOCIAL
			if(Uteis.isAtributoPreenchido(cursoDoAluno.getApoioSocial())){
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
				
				// 47 - Tipo de apoio social - Alimentação
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.ALIMENTACAO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 48 - Tipo de apoio social - Moradia
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.MORADIA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 49 - Tipo de apoio social - Transporte
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.TRANSPORTE)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 50 - Tipo de apoio social - Material didático
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.MATERIAL_DIDATICO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 51 - Tipo de apoio social - Bolsa trabalho
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.BOLSA_TRABALHO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 52 - Tipo de apoio social - Bolsa permanência
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getApoioSocial().equals(ApoioSocial.BOLSA_PERMANENCIA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
			}
			else {
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
				// 47 - Tipo de apoio social - Alimentação
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 48 - Tipo de apoio social - Moradia
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 49 - Tipo de apoio social - Transporte
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 50 - Tipo de apoio social - Material didático
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 51 - Tipo de apoio social - Bolsa trabalho
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 52 - Tipo de apoio social - Bolsa permanência
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			// 53 - Atividade extracurricular
			if(Uteis.isAtributoPreenchido(cursoDoAluno.getAtividadeFormacaoComplementar())) {
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("1");
				linha.append("1").append(CensoVO.SEPARADOR);
				
				// 54 - Atividade extracurricular - Pesquisa
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.PESQUISA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 55 - Bolsa/remuneração referente à atividade extracurricular - Pesquisa
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.PESQUISA_REMUNERADA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 56 - Atividade extracurricular - Extensão
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.EXTENSAO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 57 - Bolsa/remuneração referente à atividade extracurricular - Extensão
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.EXTENSAO_REMUNERADA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 58 - Atividade extracurricular - Monitoria
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.MONITORIA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 59 - Bolsa/remuneração referente à atividade extracurricular - Monitoria
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.MONITORIA_REMUNERADA)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
				
				// 60 - Atividade extracurricular - Estágio não obrigatório
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.ESTAGIO_NAO_OBRIGATORIO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}				
				// 61 - Bolsa/remuneração referente à atividade extracurricular - Estágio não obrigatório
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(cursoDoAluno.getAtividadeFormacaoComplementar().equals(AtividadeFormacaoComplementar.ESTAGIO_NAO_OBRIGATORIO_REMUNERADO)) {
					cell.setCellValue("1");
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					cell.setCellValue("0");
					linha.append("0").append(CensoVO.SEPARADOR);
				}
			}
			else {
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("0");
				linha.append("0").append(CensoVO.SEPARADOR);
				// 54 - Atividade extracurricular - Pesquisa
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 55 - Bolsa/remuneração referente à atividade extracurricular - Pesquisa
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 56 - Atividade extracurricular - Extensão
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 57 - Bolsa/remuneração referente à atividade extracurricular - Extensão
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 58 - Atividade extracurricular - Monitoria
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 59 - Bolsa/remuneração referente à atividade extracurricular - Monitoria
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 60 - Atividade extracurricular - Estágio não obrigatório
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
				// 61 - Bolsa/remuneração referente à atividade extracurricular - Estágio não obrigatório
				cell = row.createCell(coluna++);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			
			// 62 - Carga horária total do curso por aluno
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getCargaHorariaTotalCurso());
			linha.append(cursoDoAluno.getCargaHorariaTotalCurso()).append(CensoVO.SEPARADOR);
			// 63 - Carga horária integralizada pelo aluno
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getCargaHorariaIntegralizadaAluno());
			linha.append(cursoDoAluno.getCargaHorariaIntegralizadaAluno()).append(CensoVO.SEPARADOR);
			
//			// 28- LINHA REFERENTE AO PROGRAMA DE RESERVA DE VAGAS - OBRIGATORIO ATÈ O				// CAMPO 33 DO CENSO
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(getProgramaReservaVagas(cursoDoAluno.getProgramaReservaVagasOutros()));
			linha.append(getProgramaReservaVagas(cursoDoAluno.getProgramaReservaVagasOutros())).append(CensoVO.SEPARADOR);
			// 64 - Justificativa
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(cursoDoAluno.getJustificativaCenso());
			linha.append(cursoDoAluno.getJustificativaCenso());
			
			/*linha.append(alunoCenso.getApoioSocial(cursoDoAluno.getApoioSocial())).append(CensoVO.SEPARADOR);
			ApoioSocial apoioSocial = ApoioSocial.getEnum(cursoDoAluno.getApoioSocial());
			if (apoioSocial != null) {
				cursoDoAluno.setApoioSocial(apoioSocial.getDescricao());
			}*/
			// LINHA REFERENTE A ATIVIDADE EXTRACURRICULAR - OBRIGATORIO 53  a 61
			/*linha.append(alunoCenso.getAtividadeFormacaoComplementar(cursoDoAluno.getAtividadeFormacaoComplementar()))
					.append(CensoVO.SEPARADOR);
			AtividadeFormacaoComplementar atividadeFormacaoComplementar = AtividadeFormacaoComplementar
					.getEnum(cursoDoAluno.getAtividadeFormacaoComplementar());
			if (atividadeFormacaoComplementar != null) {
				cursoDoAluno.setAtividadeFormacaoComplementar(atividadeFormacaoComplementar.getDescricao());
			}*/
			
			
			return linha.toString();
		} catch (Exception e) {
			return linha.toString();
		}
	}

	/***
	 * Retorna linha de cada aluno do arquivo
	 * 
	 * @param linha
	 * @param alunoCensoVO
	 * @param matriculaVO
	 * @param alunoVO
	 * @return
	 * @throws ParseException
	 */
	private String executarGerarRegistroAluno(StringBuilder linha, Integer anoIngressos, AlunoCensoVO alunoCensoVO, XSSFSheet worksheet) throws ParseException {
		try {
			int coluna = 0;
			// TIPO REGISTRO - OBRIGATORIO
			XSSFRow row = worksheet.createRow(worksheet.getLastRowNum() + 1);
			XSSFCell cell = row.createCell(coluna);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getTipoRegistro());
			linha.append(alunoCensoVO.getTipoRegistro()).append(CensoVO.SEPARADOR); //1
 
			// ID DO ALUNO NO INEP - OPCIONAL -2
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue("");
			linha.append("").append(CensoVO.SEPARADOR);

			// NOME - OBRIGATORIO - 3
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(alunoCensoVO.getNome().toUpperCase())));
			linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(alunoCensoVO.getNome().toUpperCase()))).append(CensoVO.SEPARADOR);
			
			// CPF -OBRIGATORIO - 4
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getCpf().replace(".", "").replace("-", ""));
			linha.append(alunoCensoVO.getCpf().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);

			// Documento de estrangeiro ou passaporte - Opcional 5
			linha.append("").append(CensoVO.SEPARADOR);
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getPassaporte());
			
			// DATA NASCIMENTO - OBRIGATORIO - 6
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getDataNasc().isEmpty() ? "" : alunoCensoVO.getDataNasc().replaceAll("/", ""));			
			linha.append(alunoCensoVO.getDataNasc().isEmpty() ? "" : alunoCensoVO.getDataNasc().replaceAll("/", "")).append(CensoVO.SEPARADOR);

			// COR/RACA - OBRIGATORIO - 7
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getCorRaca());
			linha.append(alunoCensoVO.getCorRaca()).append(CensoVO.SEPARADOR);
			
			// NACIONALIDADE - OBRIGATORIO - 8
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getNacionalidade());
			linha.append(alunoCensoVO.getNacionalidade()).append(CensoVO.SEPARADOR);
			// UF NASCIMENTO - CONDICIONAL - 9
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(alunoCensoVO.getNacionalidade().equals("1")) {
				cell.setCellValue(alunoCensoVO.getCodigoIbgeEstado());
				linha.append(alunoCensoVO.getCodigoIbgeEstado()).append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR); 
			}
			// MUNICIPIO DE NASCIMENTO - OPCIONAL - 10
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(alunoCensoVO.getNacionalidade().equals("1")) {
				linha.append(alunoCensoVO.getMunicipioDeNascimento()).append(CensoVO.SEPARADOR);
			}else {
				cell.setCellValue("");
				linha.append("").append(CensoVO.SEPARADOR);
			}
			// PAIS ORIGEM - OBRIGATORIO - 11
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getPaisOrigem());
			linha.append(alunoCensoVO.getPaisOrigem()).append(CensoVO.SEPARADOR);
			
			// Aluno com deficiência, transtorno global do desenvolvimento ou altas habilidades/superdotação - OBRIGATORIO  - 12
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getAlunoComDeficiencia());
			linha.append(alunoCensoVO.getAlunoComDeficiencia()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA CEGUEIRA -CONDICIONAL - 13
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getAlunoComDeficiencia());
			linha.append(alunoCensoVO.getCegueira()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA BAIXA VISAO- CONDICIONAL - 14
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getBaixaVisao());
			linha.append(alunoCensoVO.getBaixaVisao()).append(CensoVO.SEPARADOR);
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getBaixaVisao());
			linha.append(alunoCensoVO.getBaixaVisaoOuVisaoMonocular()).append(CensoVO.SEPARADOR);
			// TIPO DE DEFICIENCIA SURDEZ - CONDICIONAL - 15
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getSurdez());
			linha.append(alunoCensoVO.getSurdez()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA DEFICIENCIA AUDITIVA - CONDICIONAL - 16
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getAuditiva());
			linha.append(alunoCensoVO.getAuditiva()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA DEFICIENCIA FISICA - CONDICIONAL - 17
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getFisica());
			linha.append(alunoCensoVO.getFisica()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA SURDOCEGUEIRA  - CONDICIONAL - 18
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getSurdocegueira());
			linha.append(alunoCensoVO.getSurdocegueira()).append(CensoVO.SEPARADOR);

			// MENTAL/INTELECTUAL - CONDICIONAL - 19
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getMental());
			linha.append(alunoCensoVO.getMental()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA AUTISMO - CONDICIONAL - 20
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getAutismoInfantil());
			linha.append(alunoCensoVO.getAutismoInfantil()).append(CensoVO.SEPARADOR);
			
			// TIPO DE DEFICIENCIA SUPERDOTADO CONDICIONAL - 21
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(alunoCensoVO.getAltasHabilidadesSuperdotacao());
			linha.append(alunoCensoVO.getAltasHabilidadesSuperdotacao()).append(CensoVO.SEPARADOR);
			
			// Tipo de escola que concluiu o Ensino Médio - obrigatório - 22
			cell = row.createCell(coluna++);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			if(!Uteis.isAtributoPreenchido(alunoCensoVO.getTipoEscolaConcluiuEnsinoMedio())){
				cell.setCellValue(2);
				linha.append(2);
			}else {
				if(alunoCensoVO.getTipoEscolaConcluiuEnsinoMedio().equals("PU")) {
					cell.setCellValue(1);
					linha.append(1);// Tipo de escola que concluiu o Ensino Médio - obrigatório - 22
				}else {
					cell.setCellValue(0);
					linha.append(0);// Tipo de escola que concluiu o Ensino Médio - obrigatório - 22
				}
			}

			return linha.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linha.toString();

	}

	// cadastro aluno, identificacao - reg.60
	@Deprecated
	private String executarGerarRegistroAlunoLayoutTecnicoIdentificacao(StringBuilder linha, AlunoCensoVO alunoCensoVO,
			CensoVO censo, UsuarioVO usuario) throws Exception {
		PessoaVO aluno = getFacadeFactory().getPessoaFacade().consultarAlunoPorMatricula(alunoCensoVO.getMatricula(),
				false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_ALUNO_IDENTIFICACAO).append(CensoVO.SEPARADOR); // 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR); // 2
		linha.append("").append(CensoVO.SEPARADOR); // 3
		linha.append(alunoCensoVO.getMatricula()).append(CensoVO.SEPARADOR); // 4
		linha.append(
				Uteis.removerCaracteresEspeciais3(Uteis.removerAcentos(alunoCensoVO.getNome().replaceAll("\\s+$", ""))))
				.append(CensoVO.SEPARADOR); // 5
		linha.append(Uteis.getData(aluno.getDataNasc(), "dd/MM/yyyy")).append(CensoVO.SEPARADOR); // 6
		linha.append(alunoCensoVO.getSexoCensoTecnico()).append(CensoVO.SEPARADOR); // 7
		linha.append("0").append(CensoVO.SEPARADOR); // 8
		linha.append("0").append(CensoVO.SEPARADOR); // 9
		linha.append("").append(CensoVO.SEPARADOR); // 10
		linha.append("").append(CensoVO.SEPARADOR); // 11
		linha.append("1").append(CensoVO.SEPARADOR); // 12
		linha.append("76").append(CensoVO.SEPARADOR); // 13
		linha.append(aluno.getNaturalidade().getEstado().getCodigoIBGE()).append(CensoVO.SEPARADOR); // 14
		linha.append(aluno.getNaturalidade().getCodigoIBGE()).append(CensoVO.SEPARADOR); // 15
		linha.append(alunoCensoVO.getAlunoComDeficiencia()).append(CensoVO.SEPARADOR); // 16

		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getCegueira().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 17
		} else {
			linha.append(alunoCensoVO.getCegueira()).append(CensoVO.SEPARADOR); // 17
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getBaixaVisao().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 18
		} else {
			linha.append(alunoCensoVO.getBaixaVisao()).append(CensoVO.SEPARADOR); // 18
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getSurdez().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 19
		} else {
			linha.append(alunoCensoVO.getSurdez()).append(CensoVO.SEPARADOR); // 19
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getAuditiva().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 20
		} else {
			linha.append(alunoCensoVO.getAuditiva()).append(CensoVO.SEPARADOR); // 20
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getSurdocegueira().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 21
		} else {
			linha.append(alunoCensoVO.getSurdocegueira()).append(CensoVO.SEPARADOR); // 21
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getFisica().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 22
		} else {
			linha.append(alunoCensoVO.getFisica()).append(CensoVO.SEPARADOR); // 22
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getMental().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 23
		} else {
			linha.append(alunoCensoVO.getMental()).append(CensoVO.SEPARADOR); // 23
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getMultipla().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 24
		} else {
			linha.append(alunoCensoVO.getMultipla()).append(CensoVO.SEPARADOR); // 24
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getAutismoInfantil().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 25
		} else {
			linha.append(alunoCensoVO.getAutismoInfantil()).append(CensoVO.SEPARADOR); // 25
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getSindromeAsperger().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 26
		} else {
			linha.append(alunoCensoVO.getSindromeAsperger()).append(CensoVO.SEPARADOR); // 26
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1") && alunoCensoVO.getSindromeRett().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 27
		} else {
			linha.append(alunoCensoVO.getSindromeRett()).append(CensoVO.SEPARADOR); // 27
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1")
				&& alunoCensoVO.getTranstornoDesintegrativoInfancia().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 28
		} else {
			linha.append(alunoCensoVO.getTranstornoDesintegrativoInfancia()).append(CensoVO.SEPARADOR); // 28
		}
		if (alunoCensoVO.getAlunoComDeficiencia().equals("1")
				&& alunoCensoVO.getAltasHabilidadesSuperdotacao().equals("")) {
			linha.append("0").append(CensoVO.SEPARADOR); // 29
		} else {
			linha.append(alunoCensoVO.getAltasHabilidadesSuperdotacao()).append(CensoVO.SEPARADOR); // 29
		}
		linha.append("").append(CensoVO.SEPARADOR); // 30
		linha.append("").append(CensoVO.SEPARADOR); // 31
		linha.append("").append(CensoVO.SEPARADOR); // 32
		linha.append("").append(CensoVO.SEPARADOR); // 33
		linha.append("").append(CensoVO.SEPARADOR); // 34
		linha.append("").append(CensoVO.SEPARADOR); // 35
		linha.append("").append(CensoVO.SEPARADOR); // 36
		linha.append("").append(CensoVO.SEPARADOR); // 37
		linha.append("").append(CensoVO.SEPARADOR); // 38
		linha.append(""); // 39
		return linha.toString().toUpperCase();
	}

	// Cadastro de aluno, documentos e endereco - reg.70
	@Deprecated
	private String executarGerarRegistroAlunoLayoutTecnicoDocumentoEndereco(StringBuilder linha,
			AlunoCensoVO alunoCensoVO, CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_ALUNO_DOCUMENTO).append(CensoVO.SEPARADOR); // 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// 2
		linha.append("").append(CensoVO.SEPARADOR);// 3
		linha.append(alunoCensoVO.getMatricula()).append(CensoVO.SEPARADOR);// 4
		linha.append("").append(CensoVO.SEPARADOR); // 5
		linha.append("").append(CensoVO.SEPARADOR); // 6
		linha.append("").append(CensoVO.SEPARADOR); // 7
		linha.append("").append(CensoVO.SEPARADOR); // 8
		linha.append("").append(CensoVO.SEPARADOR); // 9
		linha.append("").append(CensoVO.SEPARADOR); // 10
		linha.append("").append(CensoVO.SEPARADOR); // 11
		linha.append("").append(CensoVO.SEPARADOR); // 12
		linha.append("").append(CensoVO.SEPARADOR); // 13
		linha.append("").append(CensoVO.SEPARADOR); // 14
		linha.append("").append(CensoVO.SEPARADOR); // 15
		linha.append("").append(CensoVO.SEPARADOR); // 16
		linha.append("").append(CensoVO.SEPARADOR); // 17
		linha.append("").append(CensoVO.SEPARADOR); // 18
		linha.append(alunoCensoVO.getCpf().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);// 19 - CPF -
																										// OBRIGATORIO
		linha.append("").append(CensoVO.SEPARADOR); // 20
		linha.append("").append(CensoVO.SEPARADOR); // 21
		linha.append(CensoVO.LOCALIZACAO_ZONA_RESIDENCIA).append(CensoVO.SEPARADOR);// 22
		linha.append("").append(CensoVO.SEPARADOR); // 23
		linha.append("").append(CensoVO.SEPARADOR); // 24
		linha.append("").append(CensoVO.SEPARADOR); // 25
		linha.append("").append(CensoVO.SEPARADOR); // 26
		linha.append("").append(CensoVO.SEPARADOR); // 27
		linha.append("").append(CensoVO.SEPARADOR); // 28
		linha.append(""); // 29
		return linha.toString().toUpperCase();
	}

	// Cadastro de aluno, vinculo matricula - reg.80
	@Deprecated
	private String executarGerarRegistroAlunoLayoutTecnicoVinculoMatricula(StringBuilder linha,
			AlunoCensoVO alunoCensoVO, CensoVO censo) {
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_ALUNO_VINCULO_MATRICULA).append(CensoVO.SEPARADOR);
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(alunoCensoVO.getMatricula()).append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(alunoCensoVO.getMatriculaPeriodo().getTurma().getCodigo()).append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.RECEBE_ESCOLARIZACAO_OUTRO_ESPACO).append(CensoVO.SEPARADOR);
		linha.append(CensoVO.UTILIZA_TRANSPORTE_ESCOLAR_PUBLICO).append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		linha.append(CensoVO.SEPARADOR);
		return linha.toString().toUpperCase();
	}

	// CADASTRO DE ALUNO - VINCULO (MATRICULA) - REGISTRO 60
	private String executarGerarRegistroAlunoLayoutEducacaoBasicaVinculoMatricula(StringBuilder linha,
			AlunoCensoVO alunoCensoVO, CensoVO censo) {
		linha.append("60").append(CensoVO.SEPARADOR);// TIPO DE
																										// REGISTRO - 1
		linha.append(censo.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO ESCOLA INEP - 2		
		//linha.append(alunoCensoVO.getMatricula()).append(CensoVO.SEPARADOR);// CODIGO ALUNO ENTIDADE/ESCOLA - 3
		linha.append(alunoCensoVO.getCodigo()).append(CensoVO.SEPARADOR);
		if(Uteis.isAtributoPreenchido(alunoCensoVO.getIdAlunoINEP())){
			linha.append(alunoCensoVO.getIdAlunoINEP()).append(CensoVO.SEPARADOR);// IDENTIFICACAO ALUNO INEP - 4
		}
		else {
			linha.append(CensoVO.SEPARADOR);// IDENTIFICACAO ALUNO INEP - 4
		}
		
	
		linha.append(alunoCensoVO.getMatriculaPeriodo().getTurma().getCodigo()).append(CensoVO.SEPARADOR);// CODIGO TUMA
																											// ENTIDADE/ESCOLA
																											// - 5
		linha.append(CensoVO.SEPARADOR);// CODIGO TURMA INEP - 6
		linha.append(CensoVO.SEPARADOR);// Código da Matrícula do(a) aluno(a) CAMPO - 7 
//		
		Optional<String> opt = Arrays.asList(ETAPA_ENSINO_3, ETAPA_ENSINO_22, ETAPA_ENSINO_23, ETAPA_ENSINO_56, ETAPA_ENSINO_64, ETAPA_ENSINO_72).stream()
		.filter(item -> item.equals(alunoCensoVO.getEtapaEnsino()))
		.findFirst();
		
		if (opt.isPresent()) {
			switch (opt.get()) {
			case ETAPA_ENSINO_3:
				linha.append("1");// Turma multi CAMPO - 8
				break;
			case ETAPA_ENSINO_22:
				linha.append("14");// Turma multi CAMPO - 8
				break;
			case ETAPA_ENSINO_23:
				linha.append("14");// Turma multi CAMPO - 8
				break;			
			case ETAPA_ENSINO_56:
				linha.append("1");// Turma multi CAMPO - 8
				break;
			case ETAPA_ENSINO_64:
				linha.append("39");// Turma multi CAMPO - 8
				break;
			case ETAPA_ENSINO_72:
				linha.append("69");// Turma multi CAMPO - 8
				break;
			}
			
		} else {
			linha.append("");// Turma multi CAMPO - 8
		}
		linha.append(CensoVO.SEPARADOR);
		
		//linha.append("14").append(CensoVO.SEPARADOR);// Turma multi CAMPO - 8
		
		//Tipo de atendimento educacionalespecializado
		linha.append(CensoVO.SEPARADOR); // Desenvolvimento de funções cognitivas CAMPO - 9
		
		
		linha.append(CensoVO.SEPARADOR);// Desenvolvimento de vida autônoma CAMPO - 10
		linha.append(CensoVO.SEPARADOR);// Enriquecimento curricular CAMPO - 11
		linha.append(CensoVO.SEPARADOR);// Ensino da informática acessível CAMPO - 12
		linha.append(CensoVO.SEPARADOR);// Ensino da Língua Brasileira de Sinais (Libras) CAMPO - 13
		linha.append(CensoVO.SEPARADOR);// Ensino da Língua Portuguesa como Segunda Língua CAMPO - 14
		linha.append(CensoVO.SEPARADOR);// Ensino das técnicas do cálculo no Soroban CAMPO - 15
		linha.append(CensoVO.SEPARADOR);// Ensino de Sistema Braille CAMPO - 16
		linha.append(CensoVO.SEPARADOR);// Ensino de técnicas para orientação e mobilidade CAMPO - 17
		linha.append(CensoVO.SEPARADOR);// Ensino de uso da Comunicação Alternativa e Aumentativa (CAA) CAMPO - 18
		linha.append(CensoVO.SEPARADOR);// Ensino de uso de recursos ópticos e não ópticos CAMPO - 19
		linha.append("1").append(CensoVO.SEPARADOR);// Recebeescolarização em outro espaço (diferente da escola) - CAMPO - 20
		linha.append(CensoVO.UTILIZA_TRANSPORTE_ESCOLAR_PUBLICO).append(CensoVO.SEPARADOR);// Transporte escolar público  CAMPO - 21
		linha.append(CensoVO.SEPARADOR);// Poder Público responsável pelo transporte escolar CAMPO - 22
		//Tipo de veículo utilizado no transporte escolar público
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Bicicleta CAMPO - 23
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Microônibus CAMPO - 24
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Ônibus CAMPO - 25
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Tração Animal CAMPO - 26
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Vans/Kombis CAMPO - 27
		linha.append(CensoVO.SEPARADOR);// Rodoviário - Outro  CAMPO - 28
		linha.append(CensoVO.SEPARADOR);// Aquaviário - Capacidade de até 5 aluno(a)s CAMPO - 29
		linha.append(CensoVO.SEPARADOR);// Aquaviário - Capacidade entre 5 a 15 aluno(a)s CAMPO - 30
		linha.append(CensoVO.SEPARADOR);// Aquaviário - Capacidade entre 15 a 35 aluno(a)s CAMPO - 31
		linha.append(CensoVO.SEPARADOR);// Aquaviário - Capacidade acima de 35 aluno(a)s CAMPO - 32
		return linha.toString().toUpperCase();
	}

	@Deprecated
	private void executarCriarRegistroTurmasLayoutTecnico(CensoVO censo, UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		for (TurmaCensoVO turmaCensoItemVO : censo.getListaTurmaCenso()) {
			censo.getPwLayoutTecnico()
					.println(executarGerarRegistroTurmaLayoutTecnico(linha, censo, turmaCensoItemVO, usuario));
			linha.setLength(0);
		}
	}

	// reg.10 EDUCACAO BASICA
		private void executarCriarTipoRegistroLayoutEducacaoBasica(CensoVO censoVO, UsuarioVO usuario) throws Exception {
		/*
		 * FuncionarioVO diretorGeral = getFacadeFactory().getFuncionarioFacade().
		 * consultaRapidaDiretorGeralPorCodigoUnidadeEnsino(censoVO.getUnidadeEnsino().
		 * getCodigo(), usuario); if (diretorGeral == null ||
		 * diretorGeral.getCodigo().equals(0)) { throw new
		 * Exception("Não existe um Diretor Geral cadastrado na unidade de ensino " +
		 * censoVO.getUnidadeEnsino().getNome()); }
		 */
		StringBuilder linha = new StringBuilder();
		linha.append(CensoVO.TIPO_REGISTRO_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO - 1
		linha.append(censoVO.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO DA UNIDADE DE ENSINO
																							// - 2
		// linha.append(diretorGeral.getPessoa().getCPF()).append(CensoVO.SEPARADOR);//
		// NUMERO CPF DO GESTOR ESCOLAR - 3
		// linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(diretorGeral.getPessoa().getNome()))).append(CensoVO.SEPARADOR);//
		// NOME DO GESTOR ESCOLAR - 4
		// linha.append(CensoVO.CARGO_GESTOR_ESCOLAR).append(CensoVO.SEPARADOR);// CARGO
		// DO GESTOR ESCOLAR - 5
		// linha.append(diretorGeral.getPessoa().getEmail()).append(CensoVO.SEPARADOR);//
		// ENDEREÇO ELETRONICO DO GESTOR ESCOLAR - 6

			montarDadosLocalFuncionamentoEscolaEducacaoBasica(censoVO, linha);// LOCAL DE FUNCIONAMENTO DA ESCOLA CAMPOS 03 A 8

		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE")) {
			linha.append(FormaOcupacaoPredioEnum.getCodigo(censoVO.getUnidadeEnsino().getFormaOcupacaoPredio()))
					.append(CensoVO.SEPARADOR);// FORMA DE OCUPAÇÃO DO PRÉDIO - 9
		} else {
			linha.append("").append(CensoVO.SEPARADOR);// FORMA DE OCUPAÇÃO DO PRÉDIO - 9
		}

		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE")) {
			if (censoVO.getUnidadeEnsino().getPredioCompartilhado()) {
				linha.append("1").append(CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- SIM - 10
				// NO MÁXIMO PODE-SE COMPARTILHAR O PRÉDIO COM 6 ESCOLAS
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada1()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 1
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 11
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada2()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 2
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 12
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada3()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 3
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 13
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada4()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 4
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 14
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada5()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 5
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 15
				linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada6()).append(CensoVO.SEPARADOR);// CÓDIGO
																													// DA
																													// 6
																													// ESCOLA
																													// QUE
																													// COMPARTILHA
																													// O
																													// PRÉDIO
																													// -
																													// 16
			} else {
				linha.append("0").append(CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- NÃO - 10
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 1 ESCOLA QUE COMPARTILHA O PRÉDIO - 11
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 2 ESCOLA QUE COMPARTILHA O PRÉDIO - 12
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 3 ESCOLA QUE COMPARTILHA O PRÉDIO - 13
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 4 ESCOLA QUE COMPARTILHA O PRÉDIO - 14
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 5 ESCOLA QUE COMPARTILHA O PRÉDIO - 15
				linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 6 ESCOLA QUE COMPARTILHA O PRÉDIO - 16
			}
		} else {
			linha.append("").append(CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- NÃO - 10
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 1 ESCOLA QUE COMPARTILHA O PRÉDIO - 11
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 2 ESCOLA QUE COMPARTILHA O PRÉDIO - 12
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 3 ESCOLA QUE COMPARTILHA O PRÉDIO - 13
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 4 ESCOLA QUE COMPARTILHA O PRÉDIO - 14
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 5 ESCOLA QUE COMPARTILHA O PRÉDIO - 15
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA 6 ESCOLA QUE COMPARTILHA O PRÉDIO - 16
		}

		if (censoVO.getUnidadeEnsino().getForneceAguaPotavelConsumoHumano()) {
			linha.append("1").append(CensoVO.SEPARADOR);// FORNECE ÁGUA POTÁVEL PARA O CONSUMO HUMANO - 17

		} else {
			linha.append("0").append(CensoVO.SEPARADOR);// FORNECE ÁGUA POTÁVEL PARA O CONSUMO HUMANO - 17
		}
		// linha.append(AguaConsumidaEnum.getCodigo(censoVO.getUnidadeEnsino().getAguaConsumida())).append(CensoVO.SEPARADOR);//
		// ÁGUA CONSUMIDA PELOS ALUNOS

		montarAbastecimentoAgua(censoVO, linha);// ABASTECIMENTO DE AGUA DA ESCOLA - 18 a 22

			montarAbastecimentoEnergiaEducacaoBasica(censoVO, linha);// ABASTECIMENTO DE ENERGIA DA ESCOLA - 23 a 26

			montarDadosEsgotoSanitarioEducacaoBasica(censoVO, linha);// ESGOTO SANITÁRIO - 27-30

			montarDadosDestinacaoLixoEducacaoBasica(censoVO, linha);// DESTINAÇÃO LIXO - 31 a 35

		montarDadosTratamentoLixo(censoVO, linha);// TRATAMENTO LIXO 36 a 39

			montarDadosDependenciasEducacaoBasica(censoVO, linha);// MONTAR DADOS DEPÊNDENCIAS ATÉ CAMPO - 40 a 74
		
		montarDadosRecursosAcessibilidade(censoVO, linha); // MONTAR DADOS RECURSOS ACESSIBILIDADE - 75 a 83
		
		if(Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar())) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar()); // NÚMERO DE SALA DE AULA UTILIZADAS NA ESCOLA DENTRO DO PRÉDIO ESCOLAR
		}else {
			linha.append("").append(CensoVO.SEPARADOR);// NÚMERO DE SALA DE AULA UTILIZADAS NA ESCOLA DENTRO DO PRÉDIO ESCOLAR - 84
		}
		
		linha.append("").append(CensoVO.SEPARADOR);// NÚMERO DE SALAS DE AULA UTILIZADAS NA ESCOLA FORA DO PRÉDIO ESCOLAR - 85
		linha.append("").append(CensoVO.SEPARADOR);// NÚMERO DE SALAS DE AULA CLIMATIZADAS (AR CONDICIONADO, AQUECEDOR OU CLIMATIZADOR) - 86
		linha.append("").append(CensoVO.SEPARADOR);// NÚMERO DE SALA DE AULA COM ACESSIBILIDADE PARA PESSOAS COM DEFICIÊNCIA OU MOBILIDADE REDUZIDA - 87

		//montarDadosNumeroSalasExistentes(censoVO, linha);// NÚMERO DE SALAS EXISTENTES NA ESCOLA - 73

		//montarDadosNumeroSalasExistentesDentroFora(censoVO, linha);// NÚMERO DE SALAS UTILIZADAS COMO SALA DE AULA
																	// DENTRO E FORA PREDIO - 74

		
		montarDadosEquipamentosExistentes(censoVO, linha); // EQUIPAMENTOS EXISTENTES NA ESCOLA 88 - 93
		
		// QUANTIDADE DE EQUIPAMENTOS PARA O PROCESSO ENSINO APRENDIZAGEM
		linha.append("").append(CensoVO.SEPARADOR);// APARELHO DE DVD/BLURAY - 94
		linha.append("").append(CensoVO.SEPARADOR);// APARELHO DE SOM - 95
		linha.append("").append(CensoVO.SEPARADOR);// APARELHO DE TELEVISÃO 96 
		linha.append("").append(CensoVO.SEPARADOR);// LOUSA DIGITAL - 97 
		linha.append("").append(CensoVO.SEPARADOR);// PROJETOR MULTIMÍDIA (DATASHOW) - 98
		
		// QUANTIDADE DE COMPUTADORES EM USO PELOS ALUNOS
				linha.append("").append(CensoVO.SEPARADOR);// COMPUTADORES DE MESA (DESKTOP) - 99
		linha.append("").append(CensoVO.SEPARADOR);// COMPUTADORES PORTÁTEIS - 100
		linha.append("").append(CensoVO.SEPARADOR);// TABLETS - 101
		
		montarDadosAcessoInternet(censoVO, linha); // ACESSO À INTERNET 102 A 109
		
		montarDadosRedeLocal(censoVO, linha);// REDE LOCAL DE INTERLIGAÇÃO DE COMPUTADORES - 110 A 112
		
		// TOTAL DE PROFISSIONAIS QUE ATUAM NAS SEGUINTES FUNÇÕES NA ESCOLA
		linha.append("").append(CensoVO.SEPARADOR);// AUXILIARES DE SECRETARIA OU AUXILIARES ADMINISTRATIVOS, ATENDENTES - 113
		linha.append("").append(CensoVO.SEPARADOR);// AUXILIAR DE SERVIÇOS GERAIS, PORTEIRO(A), ZELADOR(A), FAXINEIRO(A), HORTICULTOR(A), JARDINEIRO(A) - 114
		linha.append("").append(CensoVO.SEPARADOR);// BIBLIOTECÁRIO(A), AUXILIAR DE BIBLIOTECA OU MONITOR(A) DA SALA DE LEITURA - 115
		linha.append("").append(CensoVO.SEPARADOR);// BOMBEIRO(A) BRIGADISTA, PROFISSIONAIS DE ASSISÊNCIA A SAÚDE (URGÊNCIA E EMERGÊNCIA), ENFERMEIRO(A), TÉCNICO(A) DE ENFERMAGEM E SOCORRISTA - 116
		linha.append("").append(CensoVO.SEPARADOR);// COORDENADOR(A) DE TURNO/DISCIPLINAR - 117
		linha.append("").append(CensoVO.SEPARADOR);// FONOAUDIÓLOGO(A) - 118
		linha.append("").append(CensoVO.SEPARADOR);// NUTRICIONISTA - 119
		linha.append("").append(CensoVO.SEPARADOR);// PSICÓLOGO(A) ESCOLAR - 120
		linha.append("").append(CensoVO.SEPARADOR);// PROFISSIONAIS DE PREPARAÇÃO E SEGURANÇA ALIMENTAR, COZINHEIRO(A), MERENDEIRA E AUXILIAR DE COZINHA - 121
		linha.append("").append(CensoVO.SEPARADOR);// PROFISSIONAIS DE APOIO E SUPERVISÃO PEDAGÓGICA: (PEDAGOGO(A), COORDENADOR(A), PEDAGÓGICO(A), ORIENTADOR(A) EDUCACIONAL, SUPERVISOR(A) ESCOLAR E COORDENADOR(A) DE ÁREA DE ENSINO - 122
		linha.append("").append(CensoVO.SEPARADOR);// SECRETÁRIO(A) ESCOLAR - 123
		linha.append("").append(CensoVO.SEPARADOR);// SEGURANÇA, GUARDA OU SEGURANÇA PATRIMONIAL - 124
		linha.append("").append(CensoVO.SEPARADOR);// TÉCNICOS(AS), MONITORES(AS) OU AUXILIARES DE LABORATÓRIO(S) - 125
		linha.append("").append(CensoVO.SEPARADOR);//Vice-diretor(a) ou diretor(a) adjunto(a), profissionais responsáveis pela gestão administrativa e/ou financeira
		linha.append("").append(CensoVO.SEPARADOR);//Orientador(a) comunitário(a) ou assistente social
		
		
		if(censoVO.getUnidadeEnsino().getAlimentacaoEscolarAlunos()) {
			linha.append("1").append(CensoVO.SEPARADOR);// AlIMENTAÇÃO ESCOLAR PARA OS ALUNO(A)S
			//FORMA(S) DE ORGANIZAÇÃO DO ENSINO - 128
		}else {
			linha.append("0").append(CensoVO.SEPARADOR);// AlIMENTAÇÃO ESCOLAR PARA OS ALUNO(A)S
			//FORMA(S) DE ORGANIZAÇÃO DO ENSINO - 128
		}
		
			linha.append("1").append(CensoVO.SEPARADOR);// SÉRIE/ANO(SÉRIES ANUAIS) - 129
			linha.append("").append(CensoVO.SEPARADOR);// PERÍODOS SEMESTRAIS -  130
			linha.append("").append(CensoVO.SEPARADOR);// CICLO(S) DO ENSINO FUNDAMENTAL - 131
			linha.append("").append(CensoVO.SEPARADOR);// GRUPOS NÃO SERIADOS COM BASE NA IDADE OU COMPENTÊNCIA (ART. 23 LDB) - 132
			linha.append("").append(CensoVO.SEPARADOR);// MÓDULOS - 133
			linha.append("").append(CensoVO.SEPARADOR);// ALTERNÂNCIA REGULAR DE PERÍODOS DE ESTUDOS (PROPOSTA PEDAGÓGICA DE FORMAÇÃO POR ALTERNÂNCIA: TEMPO-ESCOLA E TEMPO-COMUNIDADE) - 134
		
		// INSTRUMENTOS, MATERIAIS SOCIOCULTURAIS E/OU PEDAGÓGICOS EM USO NA ESCOLA PARA O DESENVOLVIMENTO DE ATIVIDADES DE ENSINO APRENDIZAGEM
		linha.append("0").append(CensoVO.SEPARADOR);// ACERVO MULTIMÍDIA  - 135
		linha.append("0").append(CensoVO.SEPARADOR);// BRINQUEDOS PARA EDUCAÇÃO INFANTIL - 136
		linha.append("0").append(CensoVO.SEPARADOR);// CONJUNTO DE MATERIAIS CIENTÍFICOS - 137
		linha.append("0").append(CensoVO.SEPARADOR);// EQUIPAMENTO PARA AMPLIFICAÇÃO E DIFUSÃO DE SOM/ÁUDIO -138
		linha.append("0").append(CensoVO.SEPARADOR);// INSTRUMENTOS MUSICAIS PARA CONJUNTO, BANDA/FANFARRA E/OU AULAS DE MÚSICAS - 139
		linha.append("0").append(CensoVO.SEPARADOR);// JOGOS EDUCATIVOS - 140
		linha.append("0").append(CensoVO.SEPARADOR);// MATERIAIS PARA ATIVIDADES CULTURAIS E ARTÍSTICAS - 141
		linha.append("0").append(CensoVO.SEPARADOR);// MATERIAIS PARA PRÁTICA DESPORTIVA E RECREAÇÃO - 142
		linha.append("0").append(CensoVO.SEPARADOR);// MATERIAIS PEDAGÓGICOS PARA EDUCAÇÃO ESCOLAR INDÍGENA -143
		linha.append("0").append(CensoVO.SEPARADOR);// MATERIAIS PEDAGÓGICOS PARA A EDUCAÇÃO DAS RELAÇÕES ÉTNICOS RACIAIS - 144
		linha.append("0").append(CensoVO.SEPARADOR);// MATERIAIS PEDAGÓGICOS PARA A EDUCAÇÃO DO CAMPO - 145
		if(censoVO.getUnidadeEnsino().getEducacaoEscolarIndigena()) {
			linha.append("1").append(CensoVO.SEPARADOR);// EDUCAÇÃO ESCOLAR INDÍGENA - 146
		}else {
			linha.append("0").append(CensoVO.SEPARADOR);// EDUCAÇÃO ESCOLAR INDÍGENA - 146
		}
				
				
		//LÍNGUA EM QUE O ENSINO É MINISTRADO
				if(censoVO.getUnidadeEnsino().getLinguaIndigena()) {				
			linha.append("1").append(CensoVO.SEPARADOR);// LÍNGUA INDÍGENA - 147
				}else {
			linha.append("0").append(CensoVO.SEPARADOR);// LÍNGUA INDÍGENA - 147
				}
				
		if(censoVO.getUnidadeEnsino().getLinguaPortuguesa()) {
			linha.append("1").append(CensoVO.SEPARADOR);// LÍNGUA PORTUGUESA - 148
		}else {
			linha.append("0").append(CensoVO.SEPARADOR);// LÍNGUA PORTUGUESA - 148
		}
					
		linha.append(censoVO.getUnidadeEnsino().getCodigoLinguaIndigena1()).append(CensoVO.SEPARADOR);// CÓDIGO DA LÍNGUA INDÍGENA 1 - 149
		linha.append(censoVO.getUnidadeEnsino().getCodigoLinguaIndigena2()).append(CensoVO.SEPARADOR);// CÓDIGO DA LÍNGUA INDÍGENA 2 - 150
		linha.append(censoVO.getUnidadeEnsino().getCodigoLinguaIndigena3()).append(CensoVO.SEPARADOR);// CÓDIGO DA LÍNGUA INDÍGENA 3 - 151
		linha.append("").append(CensoVO.SEPARADOR);// A escola faz exame de seleção para ingresso de seus aluno(a)s (avaliação por prova e /ou analise curricular) - 150
		
		//Reserva de vagas por sistema de cotas para grupos específicos de aluno(a)s
		linha.append("0").append(CensoVO.SEPARADOR);// Autodeclarado preto, pardo ou indígena (PPI) - 152
		linha.append("0").append(CensoVO.SEPARADOR);// Condição de renda - 153 
		linha.append("0").append(CensoVO.SEPARADOR);// Oriundo de escola pública - 155
		linha.append("0").append(CensoVO.SEPARADOR);// Pessoa com deficiência (PCD) - 156
		linha.append("0").append(CensoVO.SEPARADOR);// Outros grupos que não os listados - 157
		linha.append("0").append(CensoVO.SEPARADOR);// Sem reservas de vagas para sistema de cotas (ampla concorrência) - 158
		linha.append("").append(CensoVO.SEPARADOR);// A escola possui site ou blog ou página em redes sociais para comunicação institucional - 159
		linha.append("").append(CensoVO.SEPARADOR);// A escola compartilha espaços para atividades de integração escola-comunidade - 160
		linha.append("").append(CensoVO.SEPARADOR);// A escola usa espaços e equipamentos do entorno escolar para atividades regulares com os aluno(a)s - 161
		
		//Órgãos colegiados em funcionamento na escola
		linha.append("0").append(CensoVO.SEPARADOR);// Associação de Pais - 162
		linha.append("0").append(CensoVO.SEPARADOR);// Associação de pais e mestres - 163
		linha.append("0").append(CensoVO.SEPARADOR);// Conselho escolar - 164
		linha.append("0").append(CensoVO.SEPARADOR);// Grêmio estudantil - 165
		linha.append("0").append(CensoVO.SEPARADOR);// Outros - 166
		linha.append("0").append(CensoVO.SEPARADOR);//Não há órgãos colegiados em funcionamento - 167
		linha.append("").append(CensoVO.SEPARADOR);//Projeto político pedagógico ou a proposta pedagógica da escola (conforme art. 12 da LDB) atualizado nos últimos 12 meses até a data de referência - 168
					
		
			censoVO.getPwLayoutEducacaoBasica().println(linha.toString().toUpperCase());
	}

		// reg 00 EDUCACAO BASICA
		private void executarCriarCabecalhoLayoutEducaoBasica(CensoVO censoVO) throws Exception {
		StringBuilder linha = new StringBuilder();
		consultarDataInicioPeriodoLetivoDataFimPeriodoLetivo(censoVO, censoVO.getAno());
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO - 1
		linha.append(censoVO.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO ESCOLA - INEP - 2
		linha.append(CensoVO.SITUACAO_FUNCIONAMENTO_LAYOUT_TECNICO_EM_ATIVIDADE).append(CensoVO.SEPARADOR);// SITUACAO
																											// FUNCIONAMENTO
																											// - 3
		linha.append(Uteis.getData(censoVO.getDataInicioPeriodoLetivoUnidadeEnsino(), "dd/MM/yyyy"))
				.append(CensoVO.SEPARADOR);// DATA INICIO ANO LETIVO - 4
		linha.append(Uteis.getData(censoVO.getDataFimPeriodoLetivoUnidadeEnsino(), "dd/MM/yyyy"))
				.append(CensoVO.SEPARADOR);// DATA TERMINO ANO LETIVO - 5
		linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(censoVO.getUnidadeEnsino().getNome())))
				.append(CensoVO.SEPARADOR);// NOME ESCOLA - 6
		// linha.append(CensoVO.SEPARADOR);// LATITUDE - 7
		// linha.append(CensoVO.SEPARADOR);// LONGITUDE - 8
		linha.append(censoVO.getUnidadeEnsino().getCEP().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);// CEP
																														// -
																														// 7
		linha.append(censoVO.getUnidadeEnsino().getCidade().getCodigoIBGE()).append(CensoVO.SEPARADOR);// MUNICIPIO - 8
			linha.append(censoVO.getUnidadeEnsino().getCodigoDistritoCenso()).append(CensoVO.SEPARADOR);// DISTRITO -
																												// 9
			linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(censoVO.getUnidadeEnsino().getEndereco().toUpperCase()))).append(CensoVO.SEPARADOR);// ENDERECO - 10
			linha.append(censoVO.getUnidadeEnsino().getNumero()).append(CensoVO.SEPARADOR);// NUMERO - 11
			linha.append(CensoVO.SEPARADOR);// COMPLEMENTO - 12
			linha.append(censoVO.getUnidadeEnsino().getSetor()).append(CensoVO.SEPARADOR);// BAIRRO - 13
			linha.append(CensoVO.SEPARADOR);// DDD - 14
			// linha.append(censoVO.getUnidadeEnsino().getCidade().getEstado().getCodigoInep()).append(CensoVO.SEPARADOR);//
			// UF - 14
			linha.append(CensoVO.SEPARADOR);// TELEFONE - 15
			// linha.append(CensoVO.SEPARADOR);// TELEFONE PUBLICO - 16
			linha.append(CensoVO.SEPARADOR);// OUTRO TELEFONE CONTATO - 16
			// linha.append(CensoVO.SEPARADOR);// FAX - 21
			linha.append(CensoVO.SEPARADOR);// ENDERECO ELETRONICO (E-MAIL) - 17
			linha.append(censoVO.getUnidadeEnsino().getCodigoOrgaoRegionalEnsino()).append(CensoVO.SEPARADOR);// CODIGO DO ÓRGÃO REGIONAL DE ENSINO - 18
		linha.append(LocalizacaoZonaEscolaEnum.getCodigo(censoVO.getUnidadeEnsino().getLocalizacaoZonaEscola()))
				.append(CensoVO.SEPARADOR);// LOCALIZAÇÃO ZONA DA ESCOLA - 19
		linha.append(LocalizacaoDiferenciadaEscolaEnum
				.getCodigo(censoVO.getUnidadeEnsino().getLocalizacaoDiferenciadaEscola())).append(CensoVO.SEPARADOR);// LOCALIZAÇÃO
																														// DIFERENCIADA
																														// DA
																														// ESCOLA
																														// -
																														// 20
		linha.append(DependenciaAdministativaEnum.getCodigo(censoVO.getUnidadeEnsino().getDependenciaAdministrativa()))
				.append(CensoVO.SEPARADOR);// DEPENDÊNCIA ADMINISTRATIVA - 21
		if (censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("PR")) {
			linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_NAO).append(CensoVO.SEPARADOR);// SECRETARIA DE
																								// EDUCAÇÃO/MINISTÉRIO
																								// DA EDUCAÇÃO - 22
			linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_NAO).append(CensoVO.SEPARADOR);// SECRETARIA DE
																								// SEGURANÇA
																								// PÚBLICA/FORÇAS
																								// ARMADAS/MILITAR - 23
			linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_NAO).append(CensoVO.SEPARADOR);// SECRETARIA DA
																								// SAÚDE/MINISTÉRIO DA
																								// SAÚDE - 24
			linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_NAO).append(CensoVO.SEPARADOR);// OUTRO ÓRGÃO DA
																								// ADMINISTRAÇÃO PÚBLICA
																								// - 25
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA
																							// PRIVADA EMPRESA GRUPOS
																							// EMPRESARIAS DO SETOR
																							// PRIVADO OU PESSOA FISICA
																							// - 26
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA SINDICATOS DE
																							// TRABALHADORES OU
																							// PATRONAIS, ASSOCIAÇÕES,
																							// COOPERATIVAS - 27
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA ORGANIZACAO
																							// NÃO GOVERNAMENTAL - ONG
																							// INTERNACIONAL OU NACIONAL
																							// - 28
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA INSTITUIÇÃO
																							// SEM FINS LUCRATIVOS - 29
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA SISTEMA S
																							// (SESI, SENAI, SESC,
																							// OUTROS) - 30
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA ORGANIZAÇÃO DA
																							// SOCIEDADE CIVIL DE
																							// INTERESSE PÚBLICO (OSCIP)
																							// - 31
			linha.append(CategoriaEscolaPrivadaEnum.getCodigo(censoVO.getUnidadeEnsino().getCategoriaEscolaPrivada()))
					.append(CensoVO.SEPARADOR);// CATEGORIA ESCOLA PRIVADA - 32
				if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getConveniadaPoderPublico())) {
					linha.append("")
					.append(CensoVO.SEPARADOR);// CONVENIADA COM PODER PUBLICO - 33
				}else {
			linha.append(ConveniadaPoderPublicoEnum.getCodigo(censoVO.getUnidadeEnsino().getConveniadaPoderPublico()))
					.append(CensoVO.SEPARADOR);// CONVENIADA COM PODER PUBLICO - 33
				}

				linha.append(censoVO.getUnidadeEnsino().getCnpjMantenedora().replace(".", "")
						.replace("/", "").replace("-", "")).append(CensoVO.SEPARADOR);// CNPJ MANTENEDORA
																										// - 34
				linha.append(Uteis.removeCaractersEspeciais(censoVO.getUnidadeEnsino().getCNPJ()).replaceAll(" ", "")
						.replaceAll("/", "")).append(CensoVO.SEPARADOR);// CNPJ ESCOLA PRIVADA - 35
			} else {
				linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_SIM).append(CensoVO.SEPARADOR);// SECRETARIA DE
																									// EDUCAÇÃO/MINISTÉRIO
																									// DA EDUCAÇÃO - 22
				linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_SIM).append(CensoVO.SEPARADOR);// SECRETARIA DE
																									// SEGURANÇA
																									// PÚBLICA/FORÇAS
																									// ARMADAS/MILITAR - 23
				linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_SIM).append(CensoVO.SEPARADOR);// SECRETARIA DA
																									// SAÚDE/MINISTÉRIO DA
																									// SAÚDE - 24
				linha.append(CensoVO.ORGAO_ESCOLA_PUBLICA_VINCULADA_SIM).append(CensoVO.SEPARADOR);// OUTRO ÓRGÃO DA
																									// ADMINISTRAÇÃO PÚBLICA
																									// - 25
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA PRIVADA EMPRESA GRUPOS EMPRESARIAS DO
															// SETOR PRIVADO OU PESSOA FISICA - 26
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA SINDICATOS DE TRABALHADORES OU PATRONAIS,
															// ASSOCIAÇÕES, COOPERATIVAS - 27
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA ORGANIZACAO NÃO GOVERNAMENTAL - ONG INTERNACIONAL
															// OU NACIONAL - 28
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA INSTITUIÇÃO SEM FINS LUCRATIVOS - 29
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA SISTEMA S (SESI, SENAI, SESC, OUTROS) - 30
				linha.append("").append(CensoVO.SEPARADOR);// MANTENEDORA ORGANIZAÇÃO DA SOCIEDADE CIVIL DE INTERESSE
															// PÚBLICO (OSCIP) - 31
				linha.append("").append(CensoVO.SEPARADOR);// // CATEGORIA ESCOLA PRIVADA - 32
				linha.append("").append(CensoVO.SEPARADOR);// CONVENIADA COM PODER PUBLICO - 33
				linha.append("").append(CensoVO.SEPARADOR);// CNPJ MANTENEDORA - 34
				linha.append("").append(CensoVO.SEPARADOR);// CNPJ ESCOLA PRIVADA - 35
			}
			linha.append(CensoVO.REGULAMENTACAO_AUTORIZACAO).append(CensoVO.SEPARADOR);// REGULAMENTACAO AUTORIZACAO NO
																						// CONSELHO OU ORGÃO MUNICIPAL
																						// ESTADUAL OU FEDERAL DE EDUCAÇÃO -
																						// 36
			if (censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("FE")
					|| censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("PR")) {
				linha.append(CensoVO.ESFERA_ADMINISTRATIVA_CONSELHO_RESPONSAVEL_AUTORIZACAO).append(CensoVO.SEPARADOR); // FEDERAL
																														// -
																														// 37
			} else {
				linha.append("").append(CensoVO.SEPARADOR); // FEDERAL - 37
			}
			linha.append(CensoVO.ESFERA_ADMINISTRATIVA_CONSELHO_RESPONSAVEL_AUTORIZACAO).append(CensoVO.SEPARADOR); // ESTADUAL
			if (censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("FE")
					|| censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("PR") 
					|| censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("ES")
					|| censoVO.getUnidadeEnsino().getCidade().getCodigoIBGE().equals("5300108")) {
				linha.append("0").append(CensoVO.SEPARADOR); // MUNICIPAL - 39
			} else {
				linha.append(CensoVO.ESFERA_ADMINISTRATIVA_CONSELHO_RESPONSAVEL_AUTORIZACAO).append(CensoVO.SEPARADOR); // MUNICIPAL
																														// -
																														// 39
			}
		//linha.append(censoVO.getUnidadeEnsino().getUnidadeVinculadaEscolaEducacaoBasica()).append(CensoVO.SEPARADOR);// UNIDADE
																														// VINCULADA
																														// À
																														// ESCOLA
																														// DE
																														// EDUCAÇÃO
																														// BÁSICA
																														// OU
																														// UNIDADE
																														// OFERTANTE
		
		
																														// DE
																														// EDUCAÇÃO
																														// SUPERIOR
																														// 40
		if (censoVO.getUnidadeEnsino().getUnidadeVinculadaEscolaEducacaoBasica().equals("UV")) {
			
			linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaSede()).append(CensoVO.SEPARADOR);// CÓDIGO DA ESCOLA 41
																										// SEDE
		} else {
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA ESCOLA SEDE1
		}
		if (censoVO.getUnidadeEnsino().getUnidadeVinculadaEscolaEducacaoBasica().equals("UO")) {
			linha.append(censoVO.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CÓDIGO DA IES 42
		} else {
			linha.append("").append(CensoVO.SEPARADOR);// CÓDIGO DA IES 42
		}

			censoVO.getPwLayoutEducacaoBasica().println(linha.toString().toUpperCase());
	}

		//reg.20 EDUCACAO BASICA
		private void executarCriarRegistroTurmasLayoutEducacaoBasica(CensoVO censo, UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
			List<TurmaCensoVO> listaTurmaCenso = new ArrayList<TurmaCensoVO>(0);			

			listaTurmaCenso.addAll(consultarTurmasPorUnidadeEnsinoEducacaoBasica(usuario, censo.getUnidadeEnsino().getCodigo(), censo.getAno(), censo.getSemestre(), censo.getDataBase(), censo.getLayout(), censo.getListaTurmaCenso()));	
				
			for (TurmaCensoVO turmaCensoItemVO : listaTurmaCenso) {
				turmaCensoItemVO.setListaTurnoHorarioVOs(getFacadeFactory().getTurnoHorarioFacade().consultarDiasSemanaHorarioTurnoPorTurmaCenso(turmaCensoItemVO.getCodigo(), false, usuario));
				censo.getPwLayoutEducacaoBasica().println(executarGerarRegistroTurmaLayoutEducacaoBasica(linha, turmaCensoItemVO));
			linha.setLength(0);
		}
	}

		// EDUCACAO BASICA
		public String executarGerarRegistroTurmaLayoutEducacaoBasica(StringBuilder linha, TurmaCensoVO turmaCensoVO) {
			// Seq 1
			linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_TURMA_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO
			// Seq 2
			linha.append(turmaCensoVO.getCodigoINEP()).append(CensoVO.SEPARADOR);// CODIGO DA
																											// ESCOLA
			// Seq 3
			linha.append(turmaCensoVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO DA TURMA NA ENTIDADE/ESCOLA
			
			// Seq 4
			linha.append("").append(CensoVO.SEPARADOR);// CODIGO DA TURMA NO INEP
			// Seq 5
			linha.append(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(turmaCensoVO.getNomeTurma())).replace("/", "")
					.replace("  ", " ")).append(CensoVO.SEPARADOR);// NOME DA TURMA
			
			// Seq 6
			linha.append("1").append(CensoVO.SEPARADOR);// Tipo de mediação didático pedagógica 6
			// Seq 7
			linha.append(turmaCensoVO.getHorarioInicialHoraTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																								// INICIAL HORA
			// Seq 8
			linha.append(turmaCensoVO.getHorarioInicialMinutoTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA -
																								// HORÁRIO INICIAL - MINUTO
			// Seq 9
			linha.append(turmaCensoVO.getHorarioFinalHoraTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																							// FINAL - HORA
			// Seq 10
			linha.append(turmaCensoVO.getHorarioFinalMinutoTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																								// FINAL - MINUTO
			// Seq 11 A 17 Dias da Semana
			
			
			if(Uteis.isAtributoPreenchido(turmaCensoVO.getListaTurnoHorarioVOs())) {
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("01")) {
					// Seq 11 
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 11 
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("02")) {
					// Seq 12
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 12
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("03")) {
					// Seq 13
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 13
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("04")) {
					// Seq 14
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 14
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("05")) {
					// Seq 15
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 15
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("06")) {
					// Seq 16
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 16
					linha.append(0).append(CensoVO.SEPARADOR);
				}
				if(turmaCensoVO.getListaTurnoHorarioVOs().contains("07")) {
					// Seq 17
					linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR);
				}else {
					// Seq 17
					linha.append(0).append(CensoVO.SEPARADOR);
				}
			}			
			
			// TIPO ATENDIMENTO
			
			// Seq 18
			linha.append("1").append(CensoVO.SEPARADOR);// escolarização
																											
		linha.append("0").append(CensoVO.SEPARADOR);// Atividade complementar 19 
		linha.append("0").append(CensoVO.SEPARADOR);// Atendimento educacional especializado - AEE 20
		
		

		//linha.append(CensoVO.SEPARADOR);// TURMA PARTICIPANTE DO PROGRAMA MAIS EDUCACAO/ENSINO MEDIO INOVADOR
		// Seq 21 A 26 CÓDIGO DO TIPO DE ATIVIDADE COMPLEMENTAR
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 1
		// Seq 22
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 2
		// Seq 23
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 3
		// Seq 24
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 4
		// Seq 25
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 5
		// Seq 26
		linha.append("").append(CensoVO.SEPARADOR);// CODIGO TIPO ATIVIDADE 6
		
		//Seq 27
		linha.append("0").append(CensoVO.SEPARADOR); //Local de funcionamento diferenciado
		
		
		// Seq 2 a 35 ATIVIDADE DO ATENDIMENTO EDUCACIONAL ESPECIALIZADO - AEE
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DO
																												// SISTEMA
																												// BRAILLE
		// Seq 26
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DO
																												// USO
																												// DE
																												// RECURSOS
																												// OPTICOS
																												// E NAO
																												// OPTICOS
		// Seq 27
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ESTRATEGIAS
																												// PARA
																												// O
																												// DESENVOLVIMENTO
																												// DE
																												// PROCESSOS
																												// MENTAIS
		// Seq 28
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// TECNICAS
																												// DE
																												// ORIENTAÇÃO
																												// E
																												// MOBILIDADE
		// Seq 29
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DA
																												// LINGUA
																												// BRASILEIRA
																												// DE
																												// SINAIS
																												// -
																												// LIBRAS
		// Seq 30
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DE
																												// USO
																												// DA
																												// COMUNICAÇÃO
																												// ALTERNATIVA
																												// E
																												// AUMENTATIVA
																												// - CAA
		// Seq 31
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ESTRATEGIAS
																												// PARA
																												// ENRIQUECIMENTO
																												// CURRICULAR
		// Seq 32
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DO
																												// USO
																												// DO
																												// SOROBAN
		// Seq 33
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DA
																												// USABILIDADE
																												// E DAS
																												// FUNCIONALIDADES
																												// DA
																												// INFORMATICA
																												// ACESSIVEL
		// Seq 34
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ENSINO
																												// DA
																												// LINGUA
																												// PORTUGUESA
																												// NA
																												// MODALIDADE
																												// ESCRITA
		// Seq 35
		//linha.append(CensoVO.ATIVIDADE_DO_ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO_AEE).append(CensoVO.SEPARADOR);// ESTRATEGIAS
																												// PARA
																												// AUTONOMIA
																												// NO
																												// AMBIENTE
																												// ESCOLAR
		// Seq 28
			if(turmaCensoVO.getNivelEducacional().equals("PR")) {
				linha.append("4").append(CensoVO.SEPARADOR);//Modalidade
			}
			else {
				linha.append(CensoVO.MODALIDADE).append(CensoVO.SEPARADOR);//Modalidade
			}

		// Seq 29
			linha.append(turmaCensoVO.getEtapaEnsino()).append(CensoVO.SEPARADOR);//Etapa
		// Seq 30
			if(turmaCensoVO.getNivelEducacional().equals("PR")) {
				linha.append(turmaCensoVO.getCodigoCurso()).append(CensoVO.SEPARADOR);//Código Curso
			}
			else {
				linha.append("").append(CensoVO.SEPARADOR);//Código Curso
			}
		
			
			if(turmaCensoVO.getNivelEducacional().equals("IN")) {
		//Áreas do conhecimento/componentes curriculares
		// Seq 31 Química
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 32 Física
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 33 Matemática
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 34 Biologia
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 35 Ciências
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 36  Língua/Literatura Portuguesa
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 37 Língua/Literatura Estrangeira - Inglês
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 38 Língua/Literatura Estrangeira - Espanhol
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 39  Língua/Literatura Estrangeira - outra
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 40  Arte (Educação Artística, Teatro, Dança, Música, Artes Plásticas e outras)
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 41 Educação Física
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 42 História
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 43 Geografia
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 44 Filosofia
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 45 Informática/ Computação
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 46 Disciplinas dos Cursos Técnicos Profissionais
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 47 Libras
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 48 Disciplinas Pedagógicas
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 49 Ensino Religioso
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 50 Língua Indígena
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 51 Estudos Sociais
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 52 Sociologia
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 53  Língua/Literatura Estrangeira - Francês
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 54  Língua Portuguesa como Segunda Língua
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 55  Estágio Curricular Supervisionado
		linha.append("").append(CensoVO.SEPARADOR);
		// Seq 56 Outras disciplinas
		linha.append("").append(CensoVO.SEPARADOR);
			}else {
				//Áreas do conhecimento/componentes curriculares
				// Seq 31 Química
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 32 Física
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 33 Matemática
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 34 Biologia
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 35 Ciências
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 36  Língua/Literatura Portuguesa
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 37 Língua/Literatura Estrangeira - Inglês
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 38 Língua/Literatura Estrangeira - Espanhol
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 39  Língua/Literatura Estrangeira - outra
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 40  Arte (Educação Artística, Teatro, Dança, Música, Artes Plásticas e outras)
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 41 Educação Física
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 42 História
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 43 Geografia
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 44 Filosofia
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 45 Informática/ Computação
				linha.append("0").append(CensoVO.SEPARADOR);
				if(turmaCensoVO.getNivelEducacional().equals("PR")) {
				// Seq 46 Disciplinas dos Cursos Técnicos Profissionais
					linha.append("1").append(CensoVO.SEPARADOR);
				}
				else {
					// Seq 46 Disciplinas dos Cursos Técnicos Profissionais
				linha.append("0").append(CensoVO.SEPARADOR);
				}

				// Seq 47 Libras
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 48 Disciplinas Pedagógicas
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 49 Ensino Religioso
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 50 Língua Indígena
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 51 Estudos Sociais
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 52 Sociologia
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 53  Língua/Literatura Estrangeira - Francês
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 54  Língua Portuguesa como Segunda Língua
				linha.append("0").append(CensoVO.SEPARADOR);
				// Seq 55  Estágio Curricular Supervisionado
				linha.append("0").append(CensoVO.SEPARADOR);
				if(turmaCensoVO.getNivelEducacional().equals("PR")) {
				// Seq 56 Outras disciplinas
					linha.append("0");
				}
				else {
					// Seq 56 Outras disciplinas
				linha.append("1");
			}

			}
			
		return linha.toString().toUpperCase();
	}

	// reg.20
		@Deprecated
	public String executarGerarRegistroTurmaLayoutTecnico(StringBuilder linha, CensoVO censo, TurmaCensoVO turmaCensoVO,
			UsuarioVO usuario) throws Exception {
		TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaCensoVO.getCodigo(),
				Uteis.NIVELMONTARDADOS_TODOS, usuario);
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_TURMA_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO
																									// 1
		linha.append(turmaCensoVO.getCodigoINEP()).append(CensoVO.SEPARADOR);// CODIGO DA ESCOLA 2
		linha.append("").append(CensoVO.SEPARADOR); // 3
		linha.append(turmaCensoVO.getCodigo()).append(CensoVO.SEPARADOR);// CODIGO DA TURMA NO INEP 4
		linha.append(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(turmaCensoVO.getNomeTurma())).replace("/", "")
				.replace("  ", "").replace(" ", "")).append(CensoVO.SEPARADOR);// NOME DA TURMA 5
		linha.append("1").append(CensoVO.SEPARADOR);// Tipo de mediação didático pedagógica 6
		linha.append(turmaCensoVO.getHorarioInicialHoraTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																							// INICIAL HORA 7
		linha.append(turmaCensoVO.getHorarioInicialMinutoTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA -
																							// HORÁRIO INICIAL - MINUTO
																							// 8
		linha.append(turmaCensoVO.getHorarioFinalHoraTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																						// FINAL - HORA 9
		linha.append(turmaCensoVO.getHorarioFinalMinutoTurma()).append(CensoVO.SEPARADOR);// HORÁRIO DA TURMA - HORÁRIO
																							// FINAL - MINUTO 10
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 11
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 12
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 13
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 14
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 15
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 16
		linha.append(CensoVO.DIA_SEMANA_CADASTRO_TURMA_AULA).append(CensoVO.SEPARADOR); // 17
		linha.append("0").append(CensoVO.SEPARADOR); // TIPO ATENDIMENTO //18
		linha.append("").append(CensoVO.SEPARADOR); // 19
		linha.append("").append(CensoVO.SEPARADOR); // 20
		linha.append("").append(CensoVO.SEPARADOR); // 21
		linha.append("").append(CensoVO.SEPARADOR); // 22
		linha.append("").append(CensoVO.SEPARADOR); // 23
		linha.append("").append(CensoVO.SEPARADOR); // 24
		linha.append("").append(CensoVO.SEPARADOR); // 25
		linha.append("").append(CensoVO.SEPARADOR); // 26
		linha.append("").append(CensoVO.SEPARADOR); // 27
		linha.append("").append(CensoVO.SEPARADOR); // 28
		linha.append("").append(CensoVO.SEPARADOR); // 29
		linha.append("").append(CensoVO.SEPARADOR); // 30
		linha.append("").append(CensoVO.SEPARADOR); // 31
		linha.append("").append(CensoVO.SEPARADOR); // 32
		linha.append("").append(CensoVO.SEPARADOR); // 33
		linha.append("").append(CensoVO.SEPARADOR); // 34
		linha.append("").append(CensoVO.SEPARADOR); // 35
		linha.append("").append(CensoVO.SEPARADOR); // 36
		linha.append("4").append(CensoVO.SEPARADOR); // 37
		linha.append("39").append(CensoVO.SEPARADOR); // 38
		linha.append(turmaVO.getCurso().getIdCursoInep()).append(CensoVO.SEPARADOR); // 39
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 40
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 41
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 42
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 43
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 44
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 45
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 46
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 47
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 48
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 49
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 50
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 51
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 52
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 53
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 54
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 55
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 56
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 57
		if (!censo.getListaProfessorCenso().isEmpty()) {
			int aux = 0;
			for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
				for (TurmaCensoVO turmaCensoVO2 : professorCenso.getListaTurmaCensoVOs()) {
					if (turmaCensoVO.getCodigo().equals(turmaCensoVO2.getCodigo())) {
						aux++;
						linha.append("1").append(CensoVO.SEPARADOR); // 58
						break;
					}
				}
				if (aux == 1) {
					break;
				}
			}
			if (aux == 0) {
				linha.append("2").append(CensoVO.SEPARADOR); // 58
			}
		} else {
			linha.append("2").append(CensoVO.SEPARADOR); // 58
		}
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 59
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 60
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 61
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 62
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 63
		linha.append(CensoVO.DISCIPLINA).append(CensoVO.SEPARADOR); // 64
		linha.append(CensoVO.DISCIPLINA); // 65
		// Não possue campo 66 no layout 2016
//		if(!censo.getListaProfessorCenso().isEmpty()) {
//			int aux = 0;
//			for (ProfessorCensoVO professorCenso : censo.getListaProfessorCenso()) {
//				for (TurmaCensoVO turmaCensoVO2 : professorCenso.getListaTurmaCensoVOs()) {
//					if(turmaCensoVO.getCodigo().equals(turmaCensoVO2.getCodigo())) {
//						aux++;
//						linha.append("0");//66
//						break;
//					}
//				}
//				if(aux == 1) {
//					break;
//				}
//			}	
//			if(aux == 0) {
//				linha.append("1"); //66				
//			}
//		} else {
//			linha.append("1"); //66
//		}
		return linha.toString().toUpperCase();
	}

	private void executarCriarCabecalho(CensoVO censoVO) throws Exception {
		censoVO.getPwAluno().println(CensoVO.TIPO_REGISTRO_CABECALHO_ALUNO + CensoVO.SEPARADOR + censoVO.getUnidadeEnsino().getCodigoIES());
//		getPwProfessor().println(CensoVO.TIPO_REGISTRO_CABECALHO_PROFESSOR + CensoVO.SEPARADOR
//				+ censoVO.getUnidadeEnsino().getCodigoIES());
	}

	// reg.00
	@Deprecated
	private void executarCriarCabecalhoLayoutTecnico(CensoVO censoVO, UsuarioVO usuario, FuncionarioVO diretorGeral)
			throws Exception {
		StringBuilder linha = new StringBuilder();
		consultarDataInicioPeriodoLetivoDataFimPeriodoLetivo(censoVO, censoVO.getAno());
		linha.append(CensoVO.TIPO_REGISTRO_CADASTRO_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO
		linha.append(censoVO.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO ESCOLA - INEP
		linha.append(diretorGeral.getPessoa().getCPF().replace("-", "").replace(".", "")).append(CensoVO.SEPARADOR);// Número
																													// do
																													// CPF
																													// do
																													// Gestor
																													// Escolar
		linha.append(Uteis.removeCaractersEspeciais(diretorGeral.getPessoa().getNome().toUpperCase()))
				.append(CensoVO.SEPARADOR);// Nome do Gestor Escolar
		linha.append(CensoVO.CARGO_GESTOR_ESCOLAR).append(CensoVO.SEPARADOR);// CARGO DO GESTOR ESCOLAR
		linha.append(diretorGeral.getPessoa().getEmail()).append(CensoVO.SEPARADOR);// ENDEREÇO ELETRONICO DO GESTOR
																					// ESCOLAR
		linha.append(CensoVO.SITUACAO_FUNCIONAMENTO_LAYOUT_TECNICO_EM_ATIVIDADE).append(CensoVO.SEPARADOR);// SITUACAO
																											// FUNCIONAMENTO
		linha.append(Uteis.getData(censoVO.getDataInicioPeriodoLetivoUnidadeEnsino(), "dd/MM/yyyy"))
				.append(CensoVO.SEPARADOR);// DATA INICIO ANO LETIVO
		linha.append(Uteis.getData(censoVO.getDataFimPeriodoLetivoUnidadeEnsino(), "dd/MM/yyyy"))
				.append(CensoVO.SEPARADOR);// DATA TERMINO ANO LETIVO
		linha.append(Uteis.removeCaractersEspeciais(Uteis.removerAcentos(censoVO.getUnidadeEnsino().getNome())))
				.append(CensoVO.SEPARADOR);// NOME ESCOLA
		linha.append(CensoVO.SEPARADOR);// LATITUDE
		linha.append(CensoVO.SEPARADOR);// LONGITUDE
		linha.append(censoVO.getUnidadeEnsino().getCEP().replace(".", "").replace("-", "")).append(CensoVO.SEPARADOR);// CEP
		linha.append(censoVO.getUnidadeEnsino().getEndereco()).append(CensoVO.SEPARADOR);// ENDERECO
		linha.append(censoVO.getUnidadeEnsino().getNumero()).append(CensoVO.SEPARADOR);// NUMERO
		linha.append(CensoVO.SEPARADOR);// COMPLEMENTO
		linha.append(censoVO.getUnidadeEnsino().getSetor()).append(CensoVO.SEPARADOR);// BAIRRO
		linha.append(censoVO.getUnidadeEnsino().getCidade().getEstado().getCodigoInep()).append(CensoVO.SEPARADOR);// UF
		linha.append(censoVO.getUnidadeEnsino().getCidade().getCodigoIBGE()).append(CensoVO.SEPARADOR);// MUNICIPIO
		linha.append(censoVO.getUnidadeEnsino().getCodigoDistritoCenso()).append(CensoVO.SEPARADOR);// DISTRITO
		linha.append(CensoVO.SEPARADOR);// DDD
		linha.append(CensoVO.SEPARADOR);// TELEFONE
		linha.append(CensoVO.SEPARADOR);// TELEFONE PUBLICO 1
		linha.append(CensoVO.SEPARADOR);// OUTRO TELEFONE CONTATO
		linha.append(CensoVO.SEPARADOR);// FAX
		linha.append(CensoVO.SEPARADOR);// ENDERECO ELETRONICO (E-MAIL)
		linha.append(censoVO.getUnidadeEnsino().getCodigoOrgaoRegionalEnsino()).append(CensoVO.SEPARADOR);// CODIGO DO
																											// ÓRGÃO
																											// REGIONAL
																											// DE ENSINO
		linha.append(DependenciaAdministativaEnum.getCodigo(censoVO.getUnidadeEnsino().getDependenciaAdministrativa()))
				.append(CensoVO.SEPARADOR);// DEPENDÊNCIA ADMINISTRATIVA
		linha.append(LocalizacaoZonaEscolaEnum.getCodigo(censoVO.getUnidadeEnsino().getLocalizacaoZonaEscola()))
				.append(CensoVO.SEPARADOR);// LOCALIZAÇÃO ZONA DA ESCOLA
		if (censoVO.getUnidadeEnsino().getDependenciaAdministrativa().equals("PR")) {
			linha.append(CategoriaEscolaPrivadaEnum.getCodigo(censoVO.getUnidadeEnsino().getCategoriaEscolaPrivada())
					+ CensoVO.SEPARADOR);// CATEGORIA ESCOLA PRIVADA
			linha.append("").append(CensoVO.SEPARADOR);// CONVENIADA COM PODER PUBLICO
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_SIM).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA
																							// PRIVADA EMPRESA GRUPOS
																							// EMPRESARIAS DO SETOR
																							// PRIVADO OU PESSOA FISICA
		} else {
			linha.append(CensoVO.SEPARADOR);// CATEGORIA ESCOLA PRIVADA
			linha.append(CensoVO.SEPARADOR);// CONVENIADA COM PODER PUBLICO
			linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_NAO).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA
																							// PRIVADA EMPRESA GRUPOS
																							// EMPRESARIAS DO SETOR
																							// PRIVADO OU PESSOA FISICA
		}
		linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_NAO).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA PRIVADA
																						// SINDICATOS DE TRABALHADORES
																						// OU PATRONAIS
		linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_NAO).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA PRIVADA
																						// ORGANIZACAO GOVERNAMENTAL
		linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_NAO).append(CensoVO.SEPARADOR);// MANTENEDORA DA ESCOLA PRIVADA
																						// INSTITUIÇÕES SEM FINS
																						// LUCRATIVOS
		linha.append(CensoVO.MANTENEDORA_ESCOLA_PRIVADA_NAO).append(CensoVO.SEPARADOR);// SISTEMA S ( SESI, SENAI,
																						// SEC,OUTROS)
		linha.append("" + CensoVO.SEPARADOR);// CNPJ MANTENEDORA
		linha.append(Uteis.removeCaractersEspeciais(censoVO.getUnidadeEnsino().getCNPJ()).replaceAll(" ", "")
				.replaceAll("/", "") + CensoVO.SEPARADOR);// CNPJ ESCOLA PRIVADA
		linha.append(CensoVO.REGULAMENTACAO_AUTORIZACAO).append(CensoVO.SEPARADOR);// REGULAMENTACAO AUTORIZACAO NO
																					// CONSELHO OU ORGÃO MUNICIPAL
																					// ESTADUAL OU FEDERAL DE EDUCAÇÃO
		linha.append("0").append(CensoVO.SEPARADOR);// Unidade Vinculada a Escola de Educação Básica ou Unidade
													// ofertante de Ensino Superior
		linha.append("").append(CensoVO.SEPARADOR);// Código da Escola Sede
		linha.append("");// Código da IES
		censoVO.getPwLayoutTecnico().println(linha.toString().toUpperCase());
	}

	// reg.10
	@Deprecated
	private void executarCriarTipoRegistroLayoutTecnico(CensoVO censoVO, UsuarioVO usuario) throws Exception {
		StringBuilder linha = new StringBuilder();
		linha.append(CensoVO.TIPO_REGISTRO_LAYOUT_TECNICO).append(CensoVO.SEPARADOR);// TIPO DE REGISTRO 1
		linha.append(censoVO.getUnidadeEnsino().getCodigoIES()).append(CensoVO.SEPARADOR);// CODIGO DA UNIDADE DE ENSINO
																							// 2
		montarDadosLocalFuncionamentoEscola(censoVO, linha);// LOCAL DE FUNCIONAMENTO DA ESCOLA CAMPOS 03 A 11
		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("OU")) {
			linha.append("").append(CensoVO.SEPARADOR);// FORMA DE OCUPAÇÃO DO PRÉDIO 12
		} else {
			linha.append(FormaOcupacaoPredioEnum.getCodigo(censoVO.getUnidadeEnsino().getFormaOcupacaoPredio()))
					.append(CensoVO.SEPARADOR);// FORMA DE OCUPAÇÃO DO PRÉDIO 12
		}
		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE")) {
			if (censoVO.getUnidadeEnsino().getPredioCompartilhado()) {
				linha.append("1" + CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- SIM 13
			} else {
				linha.append("0" + CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- NÃO 13
			}
		} else {
			linha.append("" + CensoVO.SEPARADOR);// PRÉDIO COMPARTILHADO COM OUTRA ESCOLA -- nulo 13
		}
		// NO MÁXIMO PODE-SE COMPARTILHAR O PRÉDIO COM 6 ESCOLAS
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada1() + CensoVO.SEPARADOR);// CÓDIGO DA 1
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 14
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada2() + CensoVO.SEPARADOR);// CÓDIGO DA 2
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 15
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada3() + CensoVO.SEPARADOR);// CÓDIGO DA 3
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 16
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada4() + CensoVO.SEPARADOR);// CÓDIGO DA 4
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 17
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada5() + CensoVO.SEPARADOR);// CÓDIGO DA 5
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 18
		linha.append(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada6() + CensoVO.SEPARADOR);// CÓDIGO DA 6
																										// ESCOLA QUE
																										// COMPARTILHA O
																										// PRÉDIO 19
		linha.append(AguaConsumidaEnum.getCodigo(censoVO.getUnidadeEnsino().getAguaConsumida()))
				.append(CensoVO.SEPARADOR);// ÁGUA CONSUMIDA PELOS ALUNOS 20

		montarAbastecimentoAgua(censoVO, linha);// ABASTECIMENTO DE AGUA DA ESCOLA 21 a 25

		montarAbastecimentoEnergia(censoVO, linha);// ABASTECIMENTO DE ENERGIA DA ESCOLA 26 a 29

		montarDadosEsgotoSanitario(censoVO, linha);// ESGOTO SANITÁRIO 30 a 32

		montarDadosDestinacaoLixo(censoVO, linha);// DESTINAÇÃO LIXO 33 a 38

		montarDadosDependencias(censoVO, linha);// MONTAR DADOS DEPÊNDENCIAS 39 a 68

		montarDadosNumeroSalasExistentes(censoVO, linha);// NÚMERO DE SALAS EXISTENTES NA ESCOLA 69

		montarDadosNumeroSalasExistentesDentroFora(censoVO, linha);// NÚMERO DE SALAS UTILIZADAS COMO SALA DE AULA
																	// DENTRO E FORA PREDIO 70

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeTelevisao(), linha);// QUANTIDADE DE TELEVISÕES
																							// 71

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeVideoCassete(), linha);// QUANTIDADE DE
																								// VIDEOCASSETE 72

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeDVD(), linha);// QUANTIDADE DE APARELHOS DE DVD
																						// 73

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeAntenaParabolica(), linha);// QUANTIDADE DE
																									// ANTENAS
																									// PARABÓLICAS 74

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeCopiadora(), linha);// QUANTIDADE DE COPIADORAS
																							// 75

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeRetroprojetor(), linha);// QUANTIDADE DE
																								// RETROPROJETORES 76

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeImpressora(), linha);// QUANTIDADE DE
																								// IMPRESSORAS 77

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeAparelhoSom(), linha);// QUANTIDADE DE APARELHOS
																								// DE SOM 78

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeProjetorMultimidia(), linha);// QUANTIDADE DE
																										// APARELHOS DE
																										// MULTIMÍDIA 79

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeFax(), linha);// QUANTIDADE DE APARELHOS DE FAX
																						// 80

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeMaquinaFotograficaFilmadora(), linha);// QUANTIDADE
																												// DE
																												// MÁQUINAS
																												// FOTOGRÁFICAS
																												// E
																												// FILMADORAS
																												// 81

		montarDadosEquipamentos(censoVO.getUnidadeEnsino().getQuantidadeComputadores(), linha);// QUANTIDADE
																								// COMPUTADORES 82

		if (censoVO.getUnidadeEnsino().getQuantidadeComputadores() != null
				|| censoVO.getUnidadeEnsino().getQuantidadeComputadores() == 0) {
			linha.append(CensoVO.SEPARADOR);// QUANTIDADE IMPRESSORAS MULTIFUNCIONAIS 83
			linha.append(censoVO.getUnidadeEnsino().getQuantidadeComputadoresAdministrativos() + CensoVO.SEPARADOR);// QUANTIDADE
																													// COMPUTADORES
																													// ADMINISTRATIVOS
																													// 84
			linha.append(censoVO.getUnidadeEnsino().getQuantidadeComputadoresAlunos() + CensoVO.SEPARADOR);// QUANTIDADE
																											// COMPUTADORES
																											// ALUNOS 85
			if (censoVO.getUnidadeEnsino().getComputadoresAcessoInternet()) { // ACESSO A INTERNET 86
				linha.append("1" + CensoVO.SEPARADOR);
			} else {
				linha.append("0" + CensoVO.SEPARADOR);
			}
			if (censoVO.getUnidadeEnsino().getInternetBandaLarga()) {// INTERNET BANDA LARGA 87
				linha.append("1" + CensoVO.SEPARADOR);
			} else {
				linha.append("0" + CensoVO.SEPARADOR);
			}
		} else {
			linha.append(CensoVO.SEPARADOR);// QUANTIDADE IMPRESSORAS MULTIFUNCIONAIS 83
			linha.append(CensoVO.SEPARADOR);// QUANTIDADE COMPUTADORES ADMINISTRATIVOS 84
			linha.append(CensoVO.SEPARADOR);// QUANTIDADE COMPUTADORES ALUNOS 85
			linha.append(CensoVO.SEPARADOR);// ACESSO A INTERNET 86
			linha.append(CensoVO.SEPARADOR);// INTERNET BANDA LARGA 87
		}
		linha.append(censoVO.getListaProfessorCenso().size()).append(CensoVO.SEPARADOR);// TOTAL DE FUNCIONÁROS DA
																						// ESCOLA 88
		linha.append(CensoVO.ALIMENTACAO_ESCOLAR_ALUNO).append(CensoVO.SEPARADOR); // 89
		linha.append(CensoVO.ATENDIMENTO_EDUCACIONAL_ESPECIALIZADO).append(CensoVO.SEPARADOR); // 90
		linha.append(CensoVO.ATIVIDADE_COMPLEMENTAR).append(CensoVO.SEPARADOR);// 91
		linha.append(CensoVO.MODALIDADE_NAO).append(CensoVO.SEPARADOR); // 92
		linha.append(CensoVO.MODALIDADE_NAO).append(CensoVO.SEPARADOR); // 93
		linha.append(CensoVO.MODALIDADE_NAO).append(CensoVO.SEPARADOR); // 94
		linha.append(CensoVO.MODALIDADE_SIM).append(CensoVO.SEPARADOR); // 95
		linha.append("").append(CensoVO.SEPARADOR); // 96
		linha.append(CensoVO.LOCALIZACAO_DIFERENCIADA_ESCOLA).append(CensoVO.SEPARADOR); // 97
		linha.append(CensoVO.MATERIAL_DIDATICO_ESPECIFICO_ATENDIMENTO_DIVERSIDADE_SOCIOCULTURAL_SIM)
				.append(CensoVO.SEPARADOR); // 98
		linha.append(CensoVO.MATERIAL_DIDATICO_ESPECIFICO_ATENDIMENTO_DIVERSIDADE_SOCIOCULTURAL_NAO)
				.append(CensoVO.SEPARADOR); // 99
		linha.append(CensoVO.MATERIAL_DIDATICO_ESPECIFICO_ATENDIMENTO_DIVERSIDADE_SOCIOCULTURAL_NAO)
				.append(CensoVO.SEPARADOR); // 100
		linha.append("0").append(CensoVO.SEPARADOR); // 101
		linha.append("").append(CensoVO.SEPARADOR); // 102
		linha.append("").append(CensoVO.SEPARADOR); // 103
		linha.append("").append(CensoVO.SEPARADOR); // 104
		linha.append(CensoVO.ESCOLA_CEDE_ESPACO_TURMA_BRASIL_ALFABETIZADO).append(CensoVO.SEPARADOR); // 105
		linha.append(CensoVO.ESCOLA_ABRE_FINAL_SEMANA_COMUNIDADE).append(CensoVO.SEPARADOR); // 106
		linha.append(CensoVO.ESCOLA_PROPOSTA_PEDAGOGICA_FORMACAO_ALTERNANCIA); // 107
		censoVO.getPwLayoutTecnico().println(linha.toString().toUpperCase());
	}

	public void montarDadosNumeroSalasExistentesDentroFora(CensoVO censoVO, StringBuilder linha) {
		Integer tamanhoCampoSalasExistentesDentroFora = censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio()
				.length();
		if (tamanhoCampoSalasExistentesDentroFora == 1) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentesDentroFora == 2) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentesDentroFora == 3) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentesDentroFora == 4) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio() + CensoVO.SEPARADOR);
		}
	}

	public void montarDadosEquipamentos(Integer equipamento, StringBuilder linha) {
		if (equipamento == null || equipamento == 0) {
			linha.append(CensoVO.SEPARADOR);
		} else {
			linha.append(equipamento + CensoVO.SEPARADOR);
		}
	}

	public void montarDadosNumeroSalasExistentes(CensoVO censoVO, StringBuilder linha) {
		Integer tamanhoCampoSalasExistentes = censoVO.getUnidadeEnsino().getNumeroSalasAulaExistente().length();
		if (tamanhoCampoSalasExistentes == 1) {
			linha.append("000" + censoVO.getUnidadeEnsino().getNumeroSalasAulaExistente() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentes == 2) {
			linha.append("00" + censoVO.getUnidadeEnsino().getNumeroSalasAulaExistente() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentes == 3) {
			linha.append("0" + censoVO.getUnidadeEnsino().getNumeroSalasAulaExistente() + CensoVO.SEPARADOR);
		} else if (tamanhoCampoSalasExistentes == 4) {
			linha.append(censoVO.getUnidadeEnsino().getNumeroSalasAulaExistente() + CensoVO.SEPARADOR);
		}
	}

	public void montarDadosDependencias(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getSalaDiretoria()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaProfessores()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaSecretaria()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getLaboratorioInformatica()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getLaboratorioCiencias()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getRecursosMultifuncionais()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getQuadraEsportesCoberta()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getQuadraEsportesDescoberta()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getCozinha()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBiblioteca()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaLeitura()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getParqueInfantil()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBercario()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroForaPredio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroDentroPredio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroEducacaoInfantil()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroDeficiencia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getViasDeficiencia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroChuveiro()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getRefeitorio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getDespensa()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAlmoxarifado()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAuditorio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getPatioCoberto()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getPatioDescoberto()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAlojamentoAluno()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAlojamentoProfessor()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAreaVerde()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getLavanderia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getNenhumaDependencia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosDependenciasEducacaoBasica(CensoVO censoVO, StringBuilder linha) {

		if (censoVO.getUnidadeEnsino().getAlmoxarifado()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAreaVerde()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAuditorio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroDentroPredio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroDeficiencia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroEducacaoInfantil()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroExclusivoFuncionarios()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBanheiroChuveiro()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getBiblioteca()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getCozinha()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getDespensa()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAlojamentoAluno()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getAlojamentoProfessor()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getLaboratorioCiencias()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getLaboratorioInformatica()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getParqueInfantil()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getPatioCoberto()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getPatioDescoberto()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getPiscina()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getQuadraEsportesCoberta()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getQuadraEsportesDescoberta()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getRefeitorio()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaRepousoAluno()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaArtes()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaMusica()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaDanca()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaMultiuso()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getTerreirao()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getViveiroAnimais()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaDiretoria()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if(censoVO.getUnidadeEnsino().getSalaLeitura()){
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		}else{
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}

		if (censoVO.getUnidadeEnsino().getSalaProfessores()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getRecursosMultifuncionais()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}		

		if (censoVO.getUnidadeEnsino().getSalaSecretaria()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}		

		if (censoVO.getUnidadeEnsino().getNenhumaDependencia()) {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.DEPENDENCIA_EXISTENTE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
	}
	
	public void montarDadosRecursosAcessibilidade(CensoVO censoVO, StringBuilder linha) {

		if (censoVO.getUnidadeEnsino().getCorrimaoGuardaCorpos()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getElevador()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getPisosTateis()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getPortasVaoLivreMinimoOitentaCentimetros()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getRampas()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getSinalizacaoSonora()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getSinalizacaoTatil()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getSinalizacaoVisual()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		if (censoVO.getUnidadeEnsino().getNenhumRecursoAcessibilidade()) {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.RECURSOS_ACESSIBILIDADE_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}		
	}
	
	public void montarDadosEquipamentosExistentes(CensoVO censoVO, StringBuilder linha) {

		if (censoVO.getUnidadeEnsino().getAntenaParabolica()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getComputadores()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getCopiadora()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getImpressora()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getImpressoraMultifuncional()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getScanner()) {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.EQUIPAMENTOS_EXISTENTES_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		}
		
	}

	public void montarDadosAcessoInternet(CensoVO censoVO, StringBuilder linha) {

		if (censoVO.getUnidadeEnsino().getAcessoInternetUsoAdiministrativo()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getAcessoInternetUsoProcessoEnsinoAprendizagem()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getAcessoInternetUsoAlunos()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getAcessoInternetComunidade()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}		
		
		if (censoVO.getUnidadeEnsino().getNaoPossuiAcessoInternet()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getAcessoInternetComputadoresEscola()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}	
		
		if (censoVO.getUnidadeEnsino().getAcessoInternetDispositivosPessoais()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}	
		
		if (censoVO.getUnidadeEnsino().getInternetBandaLarga()) {
			linha.append(CensoVO.ACESSO_INTERNET_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.ACESSO_INTERNET_NAO).append(CensoVO.SEPARADOR);
		}	
		
	}
	
	public void montarDadosRedeLocal(CensoVO censoVO, StringBuilder linha) {

		if (censoVO.getUnidadeEnsino().getRedeLocalCabo()) {
			linha.append(CensoVO.REDE_LOCAL_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.REDE_LOCAL_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getRedeLocalWireless()) {
			linha.append(CensoVO.REDE_LOCAL_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.REDE_LOCAL_NAO).append(CensoVO.SEPARADOR);
		}
		
		if (censoVO.getUnidadeEnsino().getNaoExisteRedeLocal()) {
			linha.append(CensoVO.REDE_LOCAL_SIM).append(CensoVO.SEPARADOR);
		} else {
			linha.append(CensoVO.REDE_LOCAL_NAO).append(CensoVO.SEPARADOR);
		}
		
		
		
	}
	
	public void montarDadosDestinacaoLixo(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("CP")) {
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("QU")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("JA")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("RC")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("EN")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("OU")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosEsgotoSanitario(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("PU")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("FO")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("IN")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarAbastecimentoEnergia(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("PU")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("GE")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("PG")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("OU")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("IN")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarAbastecimentoAgua(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getAbastecimentoAgua().equals("PU")) {
			linha.append(CensoVO.ABASTECIMENTO_AGUA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoAgua().equals("PO")) {
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoAgua().equals("CC")) {
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoAgua().equals("FR")) {
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoAgua().equals("IN")) {
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_AGUA_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosLocalFuncionamentoEscola(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("TI")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("SE")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("CP")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("SO")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("GR")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("US")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("UP")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("OU")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosLocalFuncionamentoEscolaEducacaoBasica(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("SO")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("GR")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("US")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("UP")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("OU")) {
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.LOCAL_FUNCIONAMENTO_ESCOLA_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarAbastecimentoEnergiaEducacaoBasica(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("PU")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("GE")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("OU")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getAbastecimentoEnergia().equals("IN")) {
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ABASTECIMENTO_ENERGIA_ELETRICA_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosEsgotoSanitarioEducacaoBasica(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("PU")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("FO")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("FR")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
		} else if (censoVO.getUnidadeEnsino().getEsgotoSanitario().equals("IN")) {
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.ESGOTO_SANITARIO_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosDestinacaoLixoEducacaoBasica(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("CP")) {
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("QU")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("EN")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("OU")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("JA")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public void montarDadosTratamentoLixo(CensoVO censoVO, StringBuilder linha) {
		if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("SL")) {
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("RE")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("RC")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);

		} else if (censoVO.getUnidadeEnsino().getDestinoLixo().equals("NT")) {
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_NAO).append(CensoVO.SEPARADOR);
			linha.append(CensoVO.DESTINACAO_LIXO_SIM).append(CensoVO.SEPARADOR);
		}
	}

	public CensoVO novo() throws Exception {
		Censo.incluir(getIdEntidade());
		CensoVO obj = new CensoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CensoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param censo Objeto da classe <code>CensoVO</code> que será gravado no banco
	 *              de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou
	 *                      validação de dados.
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CensoVO censo, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ProgressBarVO progressBarVO) throws Exception {
		try {
			CensoVO.validarDados(censo);
//			if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA) || censo.getLayout().equals("TECNICO")) {
//				montarDadosTurmasCenso(censo, usuario);
//			}
			censo.setArquivoAluno(new ArquivoVO());
			censo.setArquivoAlunoExcel(new ArquivoVO());
			incluir(censo, usuario);
			censo.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(censo.getUnidadeEnsino().getCodigo(), usuario));
			montarDadosAlunosCenso(censo, configuracaoGeralSistema, usuario, progressBarVO);
			if (!censo.getLayout().equals(Censo.GRADUACAO) && !censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
				validarDadosGeracaoCenso(censo);
			}
//			montarDadosProfessoresCenso(censo, usuario);
			progressBarVO.setStatus("Finalizando arquivo de exportação...");
			if (censo.getLayout().equals(Censo.EDUCACAO_BASICA_TECNICO) || censo.getLayout().equals(Censo.TECNICO) || censo.getLayout().equals(Censo.MEDIO)) {
				executarGerarArquivoLayoutEducacaoBasica(censo, configuracaoGeralSistema.getLocalUploadArquivoTemp(),usuario, configuracaoGeralSistema);
			}
			if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
				realizarFinalizacaoArquivosCenso(censo, configuracaoGeralSistema, usuario);
			}			
			vincularArquivosGeradosCenso(censo);
		} catch (Exception e) {
			if(censo.getFileOutputStream() != null) {
				censo.getFileOutputStream().flush();
				censo.getFileOutputStream().close();
				censo.setFileOutputStream(null);
			}
			if(censo.getPwAluno() != null) {				
				censo.getPwAluno().close();
			}
			File arquivoAluno = new File(configuracaoGeralSistema.getLocalUploadArquivoFixo()+File.separator+censo.getArquivoAluno().getPastaBaseArquivo()+File.separator+censo.getArquivoAluno().getNome());
			if(arquivoAluno.exists()) {
				arquivoAluno.delete();
			}
			arquivoAluno = new File(configuracaoGeralSistema.getLocalUploadArquivoFixo()+File.separator+censo.getArquivoAlunoExcel().getPastaBaseArquivo()+File.separator+censo.getArquivoAlunoExcel().getNome());
			if(arquivoAluno.exists()) {
				arquivoAluno.delete();
			}
			censo.setNovoObj(Boolean.TRUE);
			throw e;
		}finally {
			censo.getArquivosExcel().forEach(f -> f.delete());
			censo.getArquivosTxt().forEach(f -> f.delete());
			if(censo.getFileOutputStream() != null) {
				censo.getFileOutputStream().flush();
				censo.getFileOutputStream().close();
			}
			censo.getArquivosExcel().clear();
			censo.getArquivosTxt().clear();
			censo.setFileOutputStream(null);
			censo.setWorkbook(null);
			censo.setWorksheet(null);
			censo.setPwAluno(null);
			censo.setPwProfessor(null);
			censo.setPwLayoutEducacaoBasica(null);
			censo.setPwLayoutTecnico(null);
		}
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final CensoVO censo, UsuarioVO usuario) throws Exception {
		Censo.incluir(getIdEntidade(), true, usuario);
		
		censo.realizarUpperCaseDados();
		final String sql = "INSERT INTO Censo( ano, dataGeracao,  responsavel, semestre, unidadeensino, arquivoaluno, arquivoprofessor , observacao, arquivoalunoexcel, layout, arquivoLayoutTecnico,dataBase ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ) returning codigo"
				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		censo.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, censo.getAno());
				sqlInserir.setDate(2, Uteis.getDataJDBC(censo.getDataGeracao()));
				if (censo.getResponsavel().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, censo.getResponsavel().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}
				sqlInserir.setString(4, censo.getSemestre());
				if (censo.getUnidadeEnsino() != null && censo.getUnidadeEnsino().getCodigo().intValue() != 0) {
					sqlInserir.setInt(5, censo.getUnidadeEnsino().getCodigo());
				} else {
					sqlInserir.setNull(5, 0);
				}
				if (Uteis.isAtributoPreenchido(censo.getArquivoAluno())) {
					sqlInserir.setInt(6, censo.getArquivoAluno().getCodigo());
				} else {
					sqlInserir.setNull(6, 0);
				}
				if (Uteis.isAtributoPreenchido(censo.getArquivoProfessor())) {
					sqlInserir.setInt(7, censo.getArquivoProfessor().getCodigo());
				} else {
					sqlInserir.setNull(7, 0);
				}
				sqlInserir.setString(8, censo.getObservacao());
				if (Uteis.isAtributoPreenchido(censo.getArquivoAlunoExcel())) {
					sqlInserir.setInt(9, censo.getArquivoAlunoExcel().getCodigo());
				} else {
					sqlInserir.setNull(9, 0);
				}
				sqlInserir.setString(10, censo.getLayout());
				if (Uteis.isAtributoPreenchido(censo.getArquivoLayoutTecnico())) {
					sqlInserir.setInt(11, censo.getArquivoLayoutTecnico().getCodigo());
				} else {
					sqlInserir.setNull(11, 0);
				}
				sqlInserir.setDate(12, Uteis.getDataJDBC(censo.getDataBase()));

				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					censo.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getCensoUnidadeEnsinoFacade().persistir(censo, usuario);
	}

	private void validarDadosGeracaoCenso(CensoVO censo) throws Exception {
		if (!Uteis.isAtributoPreenchido(censo.getListaAlunoCenso())) {
			throw new ConsistirException("Não foi encontrado nenhum aluno para a geração do CENSO.");
		}
	}

	private void montarDadosTurmasCenso(final CensoVO censo, UsuarioVO usuario) throws Exception {
		for(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO: censo.getCensoUnidadeEnsinoVOs()) {
			if(censoUnidadeEnsinoVO.getIsSelecionado()) {
				censo.getListaTurmaCenso().addAll(consultarTurmasPorUnidadeEnsino(usuario, censoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo(),
				censo.getAno(), censo.getSemestre(), censo.getDataBase(), censo.getLayout()));
			}
		}
	}

	private void montarDadosProfessoresCenso(final CensoVO censo, UsuarioVO usuario) throws Exception {
		List<ProfessorCensoVO> listaProfessorCenso = new ArrayList<ProfessorCensoVO>(0);
		if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
			
					listaProfessorCenso = (consultarProfessorAnoSemestre(0, censo.getAno(),
							censo.getSemestre(), censo.getLayout(), usuario));
			
		}else if (censo.getLayout().equals(Censo.EDUCACAO_BASICA_TECNICO)) {
			
			listaProfessorCenso.addAll(consultarProfessorAnoUnidadeEnsinoEducacoaBasica(censo.getAno(),
					censo.getUnidadeEnsino().getCodigo(), censo.getLayout(), usuario, censo));
			
		}/*else {
			listaProfessorCenso = consultarProfessorAnoSemestreTecnico(censo.getUnidadeEnsino().getCodigo(),
					usuario, censo);
		}*/
		for (ProfessorCensoVO professorCensoVO : listaProfessorCenso) {
			List<CursoCensoProfessorVO> cursosDoProfessorCorrente = new ArrayList<CursoCensoProfessorVO>(0);
			
			if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA) /*|| censo.getLayout().equals("TECNICO")*/) {
				
				cursosDoProfessorCorrente = consultarCursosPorProfessorAnoSemestreCenso(professorCensoVO.getProfessor().getCodigo().intValue(), censo.getUnidadeEnsino().getCodigo(), censo.getAno(), censo.getSemestre(), usuario);
				
			}
			
			professorCensoVO.setListaCursoCenso(cursosDoProfessorCorrente);
			censo.getListaProfessorCenso().add(professorCensoVO);
		}
	}
	
	private void montarDadosAlunosCenso(final CensoVO censo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario, ProgressBarVO progressBarVO) throws Exception {
		List<AlunoCensoVO> listaAlunosCenso = new ArrayList<AlunoCensoVO>(0);
		progressBarVO.setStatus("Consultando informações dos alunos (Lote 1) ");
		if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
					DataModelo dataModelo =  new DataModelo();
					dataModelo.setLimitePorPagina(10000);
					int pagina = 1;
					int lote = 1;
					int sequenciaLote = 0;
					boolean possuiAluno = false;
					progressBarVO.setStatus("Iniciando arquivo "+lote+".");
					inicializarArquivosLoteCenso(censo, configuracaoGeralSistemaVO, lote, usuario);					
					do {
						dataModelo.setPaginaAtual(pagina);
						dataModelo.setPage(pagina);
						dataModelo.getOffset();
						dataModelo.setListaConsulta(new ArrayList());
						progressBarVO.setProgresso1(Long.valueOf(pagina));						
						consultarAlunoPorAnoSemestreUnidadeEnsinoCensoGraduacao(censo.getAno(), censo.getCensoUnidadeEnsinoVOs(), usuario, censo.getLayout(), censo.getTratarAbandonoCurso().equals("CA"), progressBarVO, dataModelo);						
						if(!dataModelo.getListaConsulta().isEmpty()) {							
							possuiAluno = true;
							carregarDadosCursoCenso(censo, (List<AlunoCensoVO>)dataModelo.getListaConsulta(), usuario, progressBarVO);
							executarCriarRegistroAlunos(censo, (List<AlunoCensoVO>)dataModelo.getListaConsulta(), censo.getWorksheet(), progressBarVO);
							//listaAlunosCenso.addAll((List<AlunoCensoVO>)dataModelo.getListaConsulta());
							Uteis.liberarListaMemoria(dataModelo.getListaConsulta());
							sequenciaLote++;
						}else {
							possuiAluno = false;
							if(pagina == 1) {
								throw new ConsistirException("Não foi encontrado nenhum aluno para a geração do CENSO.");
							}
						}
						if(sequenciaLote == 4) {
							progressBarVO.setStatus("Finalizando arquivo "+lote+".");
							sequenciaLote = 0;
							lote++;
							realizarFinalizacaoLoteArquivoCenso(censo, configuracaoGeralSistemaVO, usuario);
							progressBarVO.setStatus("Iniciando arquivo "+lote+".");
							inicializarArquivosLoteCenso(censo, configuracaoGeralSistemaVO, lote, usuario);
						}
						pagina++;
					}while(dataModelo.getTotalRegistrosEncontrados() > dataModelo.getOffset() && possuiAluno);
					
					if(sequenciaLote > 1 && sequenciaLote < 4) {
						realizarFinalizacaoLoteArquivoCenso(censo, configuracaoGeralSistemaVO, usuario);
					}
					
		} else if (censo.getLayout().equals(Censo.EDUCACAO_BASICA_TECNICO) || censo.getLayout().equals(Censo.TECNICO) || censo.getLayout().equals(Censo.MEDIO)) {
			censo.getListaTurmaCenso().clear();
			for(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO: censo.getCensoUnidadeEnsinoSelecionadoVOs()) {				
					Map<String, Object> mapAlunosTurmasCenso = consultarAlunoPorAnoUnidadeEnsinoCensoEducacaoBasica(censo.getDataBase(), censo.getAno(),
					censoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo(), usuario, censo.getLayout());			  
					listaAlunosCenso.addAll((List<AlunoCensoVO>) mapAlunosTurmasCenso.get("listaAlunosCenso"));
					censo.getListaTurmaCenso().addAll(((Set<TurmaCensoVO>) mapAlunosTurmasCenso.get("listaTurmasCenso")).stream().collect(Collectors.toList()));				
			}
			carregarDadosCursoCenso(censo, listaAlunosCenso, usuario, progressBarVO);
		}
		
		
		censo.setListaAlunoCenso(listaAlunosCenso);
	}
	
	private void carregarDadosCursoCenso(CensoVO censo, List<AlunoCensoVO> listaAlunosCenso, UsuarioVO usuario, ProgressBarVO progressBarVO) throws Exception {
		List<CursoCensoVO> cursosDoAlunoCorrente = new ArrayList<CursoCensoVO>(0);
		if (!listaAlunosCenso.isEmpty()) {
			progressBarVO.setStatus("Consultando informações dos cursos dos alunos (Lote "+progressBarVO.getProgresso1()+"). ");
			if (censo.getLayout().equals(Censo.GRADUACAO) || censo.getLayout().equals(Censo.GRADUACAO_TECNOLOGICA)) {
				cursosDoAlunoCorrente = consultarCursosPorAluno(listaAlunosCenso, censo.getAno(),  usuario, censo.getLayout(), progressBarVO);
			}
			progressBarVO.setStatus("Vinculando cursos aos alunos (Lote "+progressBarVO.getProgresso1()+"). ");
			for (CursoCensoVO cursoCensoVO : cursosDoAlunoCorrente) {
				for (AlunoCensoVO alunoCensoVO : listaAlunosCenso) {
					if (alunoCensoVO.getCodigoAluno().equals(cursoCensoVO.getCodigoAluno())) {
						if(alunoCensoVO.getListaCursoCenso().isEmpty()) {							
							progressBarVO.incrementarSemStatus();
						}
						alunoCensoVO.getListaCursoCenso().add(cursoCensoVO);
						break;
					}
				}
			}
			realizarValidacaoTransferenciaInternaMesmoCurso(listaAlunosCenso);
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CensoVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param censo Objeto da classe <code>CensoVO</code> que será removido no banco
	 *              de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CensoVO censo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario)
			throws Exception {
		try {
			Censo.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM Censo WHERE ((codigo = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { censo.getCodigo() });
			// APAGANDO PASTA DOS DIRETÓRIOS
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(censo.getArquivoAluno(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ PastaBaseArquivoEnum.CENSO.getValue());
			getFacadeFactory().getArquivoFacade().excluir(censo.getArquivoAluno(), usuario, configuracaoGeralSistemaVO);
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(censo.getArquivoAlunoExcel(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ PastaBaseArquivoEnum.CENSO.getValue());
			getFacadeFactory().getArquivoFacade().excluir(censo.getArquivoAlunoExcel(), usuario,
					configuracaoGeralSistemaVO);
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(censo.getArquivoProfessor(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ PastaBaseArquivoEnum.CENSO.getValue());
			getFacadeFactory().getArquivoFacade().excluir(censo.getArquivoProfessor(), usuario,
					configuracaoGeralSistemaVO);
			// Apagar Layout Tecnico
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(censo.getArquivoLayoutTecnico(),
					configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator
							+ PastaBaseArquivoEnum.CENSO.getValue());
			getFacadeFactory().getArquivoFacade().excluir(censo.getArquivoLayoutTecnico(), usuario,
					configuracaoGeralSistemaVO);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Censo</code> através do valor
	 * do atributo <code>String ano</code>. Retorna os objetos, com início do valor
	 * do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
	 *                        possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CensoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorAno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Censo WHERE upper( ano ) like('" + valorConsulta.toUpperCase()
				+ "%') ORDER BY ano";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Censo</code> através do valor
	 * do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
	 *                        possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CensoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Censo WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>CensoVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		List vetResultado = new ArrayList(0);
		CensoVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>CensoVO</code>.
	 * 
	 * @return O objeto da classe <code>CensoVO</code> com os dados devidamente
	 *         montados.
	 */
	public static CensoVO montarDados(SqlRowSet dadosSQL, CensoVO censo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		censo = new CensoVO();
		censo.setCodigo(dadosSQL.getInt("codigo"));
		censo.setAno(dadosSQL.getString("ano"));
		censo.setDataGeracao(dadosSQL.getDate("dataGeracao"));
		censo.getArquivoAluno().setCodigo(new Integer(dadosSQL.getInt("arquivoaluno")));
		censo.getArquivoAlunoExcel().setCodigo(new Integer(dadosSQL.getInt("arquivoalunoexcel")));
		censo.getArquivoProfessor().setCodigo(new Integer(dadosSQL.getInt("arquivoprofessor")));
		censo.setObservacao(dadosSQL.getString("observacao"));
		censo.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
		censo.setSemestre(dadosSQL.getString("semestre"));
		censo.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
		censo.setLayout(dadosSQL.getString("layout"));
		censo.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return censo;
		}
		montarDadosUnidadeEnsino(censo, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosResponsavel(censo, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosArquivoAluno(censo, nivelMontarDados, usuario);
		montarDadosArquivoAlunoExcel(censo, nivelMontarDados, usuario);
		montarDadosArquivoProfessor(censo, nivelMontarDados, usuario);
		censo.setCensoUnidadeEnsinoVOs(getFacadeFactory().getCensoUnidadeEnsinoFacade().consultarPorCenso(censo.getCodigo()));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return censo;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return censo;
		}
		return censo;
	}

	public static void montarDadosArquivoAluno(CensoVO censo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (censo.getArquivoAluno().getCodigo().intValue() == 0) {
			censo.setArquivoAluno(new ArquivoVO());
			return;
		}
		censo.setArquivoAluno(getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(
				censo.getArquivoAluno().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosArquivoAlunoExcel(CensoVO censo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (censo.getArquivoAlunoExcel().getCodigo().intValue() == 0) {
			censo.setArquivoAlunoExcel(new ArquivoVO());
			return;
		}
		censo.setArquivoAlunoExcel(getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(
				censo.getArquivoAlunoExcel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosArquivoProfessor(CensoVO censo, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (censo.getArquivoProfessor().getCodigo().intValue() == 0) {
			censo.setArquivoProfessor(new ArquivoVO());
			return;
		}
		censo.setArquivoProfessor(getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(
				censo.getArquivoProfessor().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(CensoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade()
				.consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>CensoVO</code>. Faz uso da
	 * chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(CensoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade()
				.consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>CensoVO</code>
	 * através de sua chave primária.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto
	 *                      procurado.
	 */
	public CensoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Censo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Censo ).");
		}
		CensoVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	/***
	 * Método responsável por trazer os alunos para o CENSO
	 * 
	 * @param ano
	 * @param semestre
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	public void consultarAlunoPorAnoSemestreUnidadeEnsinoCensoGraduacao(String ano, List<CensoUnidadeEnsinoVO> censoUnidadeEnsinoVOs, UsuarioVO usuario, String tipoLayout, boolean trazerAlunoAbandonoCursoComoCancelado, ProgressBarVO progressBarVO, DataModelo dataModelo) throws Exception {
		List<AlunoCensoVO> listaAlunosCenso = new ArrayList<AlunoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT count(*) over() as totalRegistros, ");
		sqlStr.append(" 41 as tiporegistro, ");
		sqlStr.append(" pessoa.idalunoinep, ");
		sqlStr.append(" matricula.aluno as idalunoies, cidade.codigoibge as municipiodenascimento, estado.codigoibge as estadocodigoibge, ");
		sqlStr.append(" case when pessoa.nomeCenso is not null and trim(pessoa.nomeCenso) != '' then pessoa.nomeCenso else ");
		sqlStr.append(" case when pessoa.nomebatismo is not null and trim(pessoa.nomebatismo) != '' then pessoa.nomebatismo ");
		sqlStr.append(" else pessoa.nome end end AS nomeCenso,");
		sqlStr.append(" replace(replace(pessoa.cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(" CASE WHEN (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN '' WHEN (paiz.nome not ilike('%brasil%') AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" pessoa.datanasc as dataNasc, ");
		sqlStr.append(" CASE WHEN (pessoa.sexo = 'M') THEN '0' ELSE '1' END as sexo, ");
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4'  ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6'  ");
		sqlStr.append(" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' OR pessoa.corraca = '') THEN '0' END as corraca,");
		sqlStr.append(" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) then '1' else '3' end as nacionalidade, ");
		sqlStr.append(" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN 'BRA' ");
		sqlStr.append("  WHEN paiz.nome not like ('%brasil%') THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(" case when ( case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) then 'BRA' when paiz.nome not like ('%brasil%') then paiz.siglainep end ) != 'BRA' then '' else estado.sigla end ufdenascimento, ");
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN 1 ELSE 0 END alunocomdeficiencia, ");
		
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.CEGUEIRA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END cegueira, ");
//		
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.BAIXA_VISAO.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END baixavisao, ");
//		
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SURDEZ.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END surdez, ");
//		
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.AUDITIVA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END auditiva, ");
//		
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.FISICA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END fisica, ");
//
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SURDOCEGUEIRA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END surdocegueira, ");
//
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.MENTAL.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END mental, ");
//
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.TGDTEA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END autismo, ");
//
//		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SUPERDOTACAO.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
//		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END superdotacao, ");
		realizarGeracaoSQLConsultarDeficiencia(sqlStr);

		if (tipoLayout.equals("TECNICO")) {
			sqlStr.append(
					"  matricula.matricula as matricula , pessoa.codigo as codigoaluno, pais.nome as nomeMae,turma.codigo as turma");
		} else {
			sqlStr.append("  pessoa.codigo::VARCHAR as matricula , pessoa.codigo as codigoaluno, pais.nome as nomeMae");
		}
		sqlStr.append(" FROM  ");
		sqlStr.append("  matricula ");
		sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(
				" and matriculaperiodo.codigo in(select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PR', 'PC') and extract(year from matriculaperiodo.data)<= '")
				.append(ano).append("' ");
		sqlStr.append(" and mp.ano <= '").append(ano)
				.append("' order by mp.ano||'/'|| mp.semestre desc, mp.codigo desc limit 1 ) ");
		if (tipoLayout.equals("TECNICO")) {
			sqlStr.append(" INNER JOIN turma on matriculaperiodo.turma = turma.codigo ");
		}
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno  ");
		sqlStr.append(" left join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'MA'");
		sqlStr.append(" left join pessoa pais on pais.codigo = filiacao.pais");
		sqlStr.append(" LEFT JOIN paiz ON paiz.codigo = pessoa.nacionalidade ");
		sqlStr.append(" LEFT JOIN cidade on cidade.codigo = pessoa.naturalidade ");
		sqlStr.append(" LEFT JOIN estado on estado.codigo = cidade.estado ");
		if (tipoLayout.equals("TECNICO")) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'PR' ");
			sqlStr.append(" and extract(year from matriculaperiodo.data)<= " + ano
					+ " and matricula.matricula =  (select matricula from matriculaperiodo where codigo = (select max(matriculaperiodo.codigo) from matriculaperiodo inner join matricula m on m.matricula = matriculaperiodo.matricula where m.aluno = matricula.aluno and curso.codigo = m.curso group by curso, aluno ) ) ");
		} else if (tipoLayout.equals(Censo.GRADUACAO)) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'SU' ");
			sqlStr.append(" and ((matriculaperiodo.ano = '").append(ano).append("') ");
			sqlStr.append(" or (matriculaperiodo.ano = '").append(Integer.parseInt(ano) - 1).append(
					"' and matriculaperiodo.situacaomatriculaperiodo not in ('FO', 'DE', 'IN', 'TS', 'TI', 'FI', 'JU', 'CA'").append(trazerAlunoAbandonoCursoComoCancelado ? ", 'AC'" : "").append(")) ");
			sqlStr.append(" or (matriculaperiodo.ano < '").append(Integer.parseInt(ano) - 1);
			if(trazerAlunoAbandonoCursoComoCancelado) {
				sqlStr.append("' and matriculaperiodo.situacaomatriculaperiodo = 'TR')) ");
			}else {
				sqlStr.append("' and matriculaperiodo.situacaomatriculaperiodo in ('TR', 'AC'))) ");
			}
			sqlStr.append(" and extract(year from matriculaperiodo.data)<=  " + ano + " ");
			// sqlStr.append(" and extract(year from matricula.data)<= " + ano + " and
			// (matricula.matricula = (select matricula from matriculaperiodo where codigo =
			// (select max(matriculaperiodo.codigo) from matriculaperiodo inner join
			// matricula m on m.matricula = matriculaperiodo.matricula where ano::INT >=
			// ").append(Integer.parseInt(ano) - 1).append(" AND ano::INT <=
			// ").append(Integer.parseInt(ano)).append(" AND m.aluno = matricula.aluno and
			// curso.codigo = m.curso group by curso, aluno ) ) or (matricula.situacao =
			// 'TR' and matriculaperiodo.ano < '" + (Integer.valueOf(ano) - 1) + "')) ");
		} else if (tipoLayout.equals(Censo.GRADUACAO_TECNOLOGICA)) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'GT' ");
			sqlStr.append(" and ((matriculaperiodo.ano = '").append(ano).append("') ");
			sqlStr.append(" or (matriculaperiodo.ano = '").append(Integer.parseInt(ano) - 1).append(
					"' and matriculaperiodo.situacaomatriculaperiodo not in ('FO', 'DE', 'IN', 'TS', 'TI', 'FI', 'JU', 'CA' ").append(trazerAlunoAbandonoCursoComoCancelado ? ", 'AC'" : "").append(")) ");
			sqlStr.append(" or (matriculaperiodo.ano < '").append(Integer.parseInt(ano) - 1);
			if(trazerAlunoAbandonoCursoComoCancelado) {
				sqlStr.append("' and matriculaperiodo.situacaomatriculaperiodo = 'TR')) ");
			}else {
				sqlStr.append("' and matriculaperiodo.situacaomatriculaperiodo in ('TR', 'AC'))) ");
			}
			sqlStr.append(" and extract(year from matriculaperiodo.data)<=  " + ano + " ");
		}
		// sqlStr.append(" AND matriculaperiodo.semestre =
		// '").append(semestre).append("' ");
		sqlStr.append(" AND matricula.unidadeensino in (0");		
		for(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO: censoUnidadeEnsinoVOs) {		
			if(censoUnidadeEnsinoVO.getIsSelecionado()) {
				sqlStr.append(", ").append(censoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
			}
		}
		sqlStr.append(" ) ");	
		
//		sqlStr.append(" AND pessoa.datanasc is not null ");
		// if (tipoLayout.equals("TECNICO")) {
		// sqlStr.append(" and turma <> 2728 ");
		// }
		sqlStr.append(" and curso.idcursoinep is not null and curso.idcursoinep > 0 ");
		sqlStr.append(" and (matricula.naoapresentarcenso is null or matricula.naoapresentarcenso = 'false') ");
		sqlStr.append(" order by nomeCenso,  cpf ");
		sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		int x = dataModelo.getOffset();
		while (tabelaResultado.next()) {
			x++;
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("totalRegistros"));
			if(dataModelo.getTotalRegistrosEncontrados() > 0 && dataModelo.getPaginaAtual() == 1) {
				progressBarVO.setMaxValue((dataModelo.getTotalRegistrosEncontrados()*3) + 1);
			}
			progressBarVO.setStatus("Montando informações do aluno (Lote "+dataModelo.getPaginaAtual()+") "+x+"/"+tabelaResultado.getInt("totalRegistros"));
			AlunoCensoVO alunoCenso = new AlunoCensoVO();
			montarDadosCensoAluno(alunoCenso, tabelaResultado, usuario, tipoLayout);
			listaAlunosCenso.add(alunoCenso);			
			progressBarVO.incrementarSemStatus();
		}
		dataModelo.setListaConsulta(listaAlunosCenso);
	}

	// CONSULTAR ALUNO ANO, UNIDADE ENSINO PARA EDUCACAO BASICA
	public  Map<String, Object> consultarAlunoPorAnoUnidadeEnsinoCensoEducacaoBasica(Date dataBase, String ano, Integer unidadeEnsino,
			UsuarioVO usuario, String tipoLayout) throws Exception {
		List<AlunoCensoVO> listaAlunosCenso = new ArrayList<AlunoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT  ");
		sqlStr.append(" 60 as tiporegistro, ");
		sqlStr.append(" pessoa.idalunoinep, ");
		sqlStr.append(" pessoa.codigo as codigoaluno,");
		sqlStr.append(" matricula.aluno as idalunoies, ");
		sqlStr.append(" curso.niveleducacional as niveleducacionalaluno, ");
		sqlStr.append(" case ");
		sqlStr.append(" when (curso.niveleducacional = 'ME') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '25' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '26' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '27' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '4') then '28' ");
			sqlStr.append(" else '29' ");
			sqlStr.append(" end ");
		sqlStr.append(" when (curso.niveleducacional = 'BA') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '14' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '15' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '16' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '4') then '17' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '5') then '18' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '6') then '19' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '7') then '20' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '8') then '21' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '9') then '22'");
			sqlStr.append(" else '23' ");
			sqlStr.append(" end ");
		sqlStr.append(" when (curso.niveleducacional = 'IN') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '1' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '2' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '3' ");
			sqlStr.append(" else '3' ");
			sqlStr.append(" end ");
			sqlStr.append(" when (curso.niveleducacional = 'PR') then ");
				sqlStr.append(" case ");
				sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '40' ");
				sqlStr.append(" else '40' ");
			sqlStr.append(" end ");
		sqlStr.append(" end  as etapa, ");		
		sqlStr.append(" case when pessoa.nomeCenso is not null and trim(pessoa.nomeCenso) != '' then pessoa.nomeCenso else ");
		sqlStr.append(" case when pessoa.nomebatismo is not null and trim(pessoa.nomebatismo) != '' then pessoa.nomebatismo ");
		sqlStr.append(" else pessoa.nome end end AS nomeCenso,");
		sqlStr.append(" replace(replace(pessoa.cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(
				" CASE WHEN (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN '' WHEN (paiz.nome not ilike('%brasil%') AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" pessoa.datanasc as dataNasc, ");
		sqlStr.append(" CASE WHEN (pessoa.sexo = 'M') THEN '0' ELSE '1' END as sexo, ");
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4'  ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6'  ");
		sqlStr.append(
				" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' OR pessoa.corraca = '') THEN '0' END as corraca,");
		sqlStr.append(
				" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) then '1' else '3' end as nacionalidade, ");
		sqlStr.append(" '' as ufdenascimento, ");
		sqlStr.append(" '' as municipiodenascimento, ");
		sqlStr.append(" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN 'BRA' ");
		sqlStr.append("  WHEN paiz.nome not like ('%brasil%') THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'NE' OR pessoa.deficiencia = '' OR pessoa.deficiencia is null OR pessoa.deficiencia = '0' ) THEN 0 ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN 1 ELSE 0 END as alunocomdeficiencia , ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'CE' OR pessoa.deficiencia = '2' ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'CE' ) AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0'  ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as cegueira, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'BV' OR pessoa.deficiencia = '3'  )  THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'BV') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as baixavisao, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'SU' OR pessoa.deficiencia = '4'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'SU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as surdez, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'AU' OR pessoa.deficiencia = '5'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'AU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as auditiva, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'FI' OR pessoa.deficiencia = '6'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'FI') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as fisica, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'SC' OR pessoa.deficiencia = '7'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'SC') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as surdocegueira, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'MU' OR pessoa.deficiencia = '8'  ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'MU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as multipla, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'ME' OR pessoa.deficiencia = '9'  ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'ME') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"   WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as mental, ");
		sqlStr.append(
				"  matricula.matricula as matricula , pessoa.codigo as codigoaluno, turma.codigo as turma, ");
		sqlStr.append(" (select pai.nome from filiacao inner join pessoa pai on filiacao.pais = pai.codigo and filiacao.tipo = 'MA' and filiacao.aluno = pessoa.codigo limit 1) as nomeMae, ");
		sqlStr.append(" (select pai.nome from filiacao inner join pessoa pai on filiacao.pais = pai.codigo and filiacao.tipo = 'PA' and filiacao.aluno = pessoa.codigo limit 1) as nomePai ");
		sqlStr.append(" FROM  matricula ");
		sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(
				" and matriculaperiodo.codigo in(select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and extract(year from matricula.data)<= '")
				.append(ano).append("' order by mp.codigo desc limit 1)");
		sqlStr.append(" INNER JOIN turma on matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN periodoletivo ON turma.periodoletivo = periodoletivo.codigo ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno  ");
		sqlStr.append(" INNER JOIN paiz ON paiz.codigo = pessoa.nacionalidade ");
		sqlStr.append(" WHERE curso.nivelEducacional IN ('ME', 'BA', 'IN', 'PR') ");
		sqlStr.append(" and extract(year from matricula.data)<= " + ano
				+ " and matricula.matricula =  (select matricula from matriculaperiodo where codigo = (select max(matriculaperiodo.codigo) from matriculaperiodo inner join matricula m on m.matricula = matriculaperiodo.matricula where m.aluno = matricula.aluno and curso.codigo = m.curso group by curso, aluno ) ) ");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		sqlStr.append(" AND pessoa.datanasc is not null ");
		sqlStr.append(" AND COALESCE(curso.idcursoinep, 0) != 0 ");
		sqlStr.append(" and matricula.naoapresentarcenso = 'false'");
		sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("'");

		sqlStr.append(" and ( matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sqlStr.append(" or ( matriculaperiodo.origemfechamentomatriculaperiodo in ('FORMATURA', 'TRANCAMENTO', 'CANCELAMENTO', 'TRANSFERENCIA_INTERNA', 'TRANSFERENCIA_SAIDA', 'ABANDONO', 'JUBILAMENTO') ");
		sqlStr.append(" and matriculaperiodo.datafechamentomatriculaperiodo > '").append(Uteis.getDataJDBC(dataBase)).append("'))");
		sqlStr.append(" and matriculaperiodo.data <= '").append(Uteis.getDataJDBC(dataBase)).append("'");
	
		sqlStr.append(" ORDER BY nomeCenso ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Set<TurmaCensoVO> listaTurmasCenso = new HashSet<>();
			while (tabelaResultado.next()) {
				AlunoCensoVO alunoCenso = new AlunoCensoVO();
			TurmaCensoVO turmaCensoVO = new TurmaCensoVO();
			montarDadosCensoAlunoEnsinoBasico(alunoCenso, tabelaResultado, usuario, tipoLayout);
				listaAlunosCenso.add(alunoCenso);
			turmaCensoVO.setCodigo(tabelaResultado.getInt("turma"));
			listaTurmasCenso.add(turmaCensoVO);
			}
		 Map<String, Object> mapAlunosTurmasCenso = new HashMap<>();
		 mapAlunosTurmasCenso.put("listaAlunosCenso", listaAlunosCenso);
		 mapAlunosTurmasCenso.put("listaTurmasCenso", listaTurmasCenso);
		return mapAlunosTurmasCenso;
		}

	@Deprecated
	public List<AlunoCensoVO> consultarAlunoPorAnoSemestreUnidadeEnsinoCensoTecnico(Date dataBase,
			Integer unidadeEnsino, UsuarioVO usuario, String tipoLayout) throws Exception {
		List<AlunoCensoVO> listaAlunosCenso = new ArrayList<AlunoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT  ");
		sqlStr.append(" 41 as tiporegistro, ");
		sqlStr.append(" pessoa.idalunoinep, ");
		sqlStr.append(" matricula.aluno as idalunoies, ");
		sqlStr.append(" case when pessoa.nomeCenso is not null and trim(pessoa.nomeCenso) != '' then pessoa.nomeCenso else ");
		sqlStr.append(" case when pessoa.nomebatismo is not null and trim(pessoa.nomebatismo) != '' then pessoa.nomebatismo ");
		sqlStr.append(" else pessoa.nome end end AS nomeCenso,");
		sqlStr.append(" replace(replace(pessoa.cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(
				" CASE WHEN (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN '' WHEN (paiz.nome not ilike('%brasil%') AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" pessoa.datanasc as dataNasc, ");
		sqlStr.append(" CASE WHEN (pessoa.sexo = 'M') THEN '0' ELSE '1' END as sexo, ");
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4'  ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6'  ");
		sqlStr.append(
				" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' OR pessoa.corraca = '') THEN '0' END as corraca,");
		sqlStr.append(
				" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) then '1' else '3' end as nacionalidade, ");
		sqlStr.append("  '' as ufdenascimento, ");
		sqlStr.append(" '' as municipiodenascimento, ");
		sqlStr.append(" case when (paiz.nome ilike('%brasil%') or pessoa.nacionalidade isnull) THEN 'BRA' ");
		sqlStr.append("  WHEN paiz.nome not like ('%brasil%') THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'NE' OR pessoa.deficiencia = '' OR pessoa.deficiencia is null OR pessoa.deficiencia = '0' ) THEN 0 ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN 1 ELSE 0 END as alunocomdeficiencia , ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'CE' OR pessoa.deficiencia = '2' ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'CE' ) AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0'  ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as cegueira, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'BV' OR pessoa.deficiencia = '3'  )  THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'BV') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as baixavisao, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'SU' OR pessoa.deficiencia = '4'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'SU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as surdez, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'AU' OR pessoa.deficiencia = '5'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'AU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as auditiva, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'FI' OR pessoa.deficiencia = '6'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'FI') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as fisica, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'SC' OR pessoa.deficiencia = '7'  ) THEN '1' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia <> 'SC') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as surdocegueira, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'MU' OR pessoa.deficiencia = '8'  ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'MU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as multipla, ");
		sqlStr.append(" CASE WHEN (pessoa.deficiencia = 'ME' OR pessoa.deficiencia = '9'  ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'ME') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"   WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as mental, ");
		sqlStr.append(
				"  matricula.matricula as matricula , pessoa.codigo as codigoaluno, pais.nome as nomeMae,turma.codigo as turma");
		sqlStr.append(" FROM  ");
		sqlStr.append("  matricula ");
		sqlStr.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(
				" and matriculaperiodo.codigo in(select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.data <='");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("' order by mp.codigo asc limit 1)");
		sqlStr.append(" INNER JOIN turma on matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno  ");
		sqlStr.append(" left join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'MA'");
		sqlStr.append(" left join pessoa pais on pais.codigo = filiacao.pais");
		sqlStr.append(" LEFT JOIN paiz ON paiz.codigo = pessoa.nacionalidade ");
		sqlStr.append(" WHERE curso.niveleducacional = 'PR' ");
		sqlStr.append(
				"and ((matriculaperiodo.situacaomatriculaperiodo IN  ('TR', 'CA', 'AB', 'TS', 'TI', 'FI') and datafechamentomatriculaperiodo is not null and datafechamentomatriculaperiodo >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("')");
		sqlStr.append(" or (matriculaperiodo.situacaomatriculaperiodo IN  ('AT'))");
		sqlStr.append(
				"or (matriculaperiodo.situacaomatriculaperiodo IN  ('FO') AND matricula.dataatualizacaomatriculaformada >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("')");
		sqlStr.append(")");
		sqlStr.append(" and turma.codigo in (");
		sqlStr.append(" SELECT distinct professortitulardisciplinaturma.turma FROM professortitulardisciplinaturma ");
		sqlStr.append(" inner join turma t on t.codigo = professortitulardisciplinaturma.turma ");
		sqlStr.append(" where t.unidadeensino =  ").append(unidadeEnsino);
		sqlStr.append(") ");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		// sqlStr.append(" AND pessoa.datanasc is not null ");
		sqlStr.append(" AND COALESCE(curso.idcursoinep, 0) != 0 ");
		sqlStr.append(" and matricula.naoapresentarcenso = 'false' ORDER BY nomeCenso ");
		// System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		while (tabelaResultado.next()) {
			AlunoCensoVO alunoCenso = new AlunoCensoVO();
			montarDadosCensoAluno(alunoCenso, tabelaResultado, usuario, tipoLayout);
			listaAlunosCenso.add(alunoCenso);
		}
		return listaAlunosCenso;
	}

	/***
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>AlunoCensoVO</code>.
	 * 
	 * @param alunoCenso
	 * @param dadosSQL
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static AlunoCensoVO montarDadosCensoAluno(AlunoCensoVO alunoCenso, SqlRowSet dadosSQL, UsuarioVO usuario,
			String tipoLayout) throws Exception {
		alunoCenso.setTipoRegistro(dadosSQL.getString("tiporegistro"));
		alunoCenso.setIdAlunoINEP(dadosSQL.getString("idalunoinep"));
		alunoCenso.setIdAlunoIES(dadosSQL.getString("idalunoies"));
		alunoCenso.setNome(dadosSQL.getString("nomeCenso"));
		alunoCenso.setNomeMae(dadosSQL.getString("nomeMae"));
		alunoCenso.setCpf(dadosSQL.getString("cpf"));
		alunoCenso.setPassaporte(dadosSQL.getString("documentoestrangeiro"));
		alunoCenso.setDataNasc(Uteis.getData(dadosSQL.getDate("dataNasc"), "dd/MM/yyyy"));
		alunoCenso.setSexo(dadosSQL.getString("sexo"));
		alunoCenso.setCorRaca(dadosSQL.getString("corraca"));
		// alunoCenso.setNomeFiliador(dadosSQL.getString("nomefiliador"));//Nome
		// da Mãe
		alunoCenso.setNacionalidade(dadosSQL.getString("nacionalidade"));
		alunoCenso.setUfDeNascimento(dadosSQL.getString("ufdenascimento"));
		alunoCenso.setMunicipioDeNascimento(dadosSQL.getString("municipiodenascimento"));
		alunoCenso.setPaisOrigem(dadosSQL.getString("paisorigem"));
		alunoCenso.setAlunoComDeficiencia(dadosSQL.getString("alunocomdeficiencia"));
		alunoCenso.setCegueira(dadosSQL.getString("cegueira"));
		alunoCenso.setBaixaVisao(dadosSQL.getString("baixavisao"));
		alunoCenso.setSurdez(dadosSQL.getString("surdez"));
		alunoCenso.setAuditiva(dadosSQL.getString("auditiva"));
		alunoCenso.setFisica(dadosSQL.getString("fisica"));
		alunoCenso.setSurdocegueira(dadosSQL.getString("surdocegueira"));
		alunoCenso.setMental(dadosSQL.getString("mental"));
		alunoCenso.setAutismoInfantil(dadosSQL.getString("autismo"));
		alunoCenso.setBaixaVisaoOuVisaoMonocular(dadosSQL.getString("visaomonocular"));
		alunoCenso.setAltasHabilidadesSuperdotacao(dadosSQL.getString("superdotacao"));
		alunoCenso.setMatricula(dadosSQL.getString("matricula"));
		alunoCenso.setCodigoAluno(dadosSQL.getString("codigoaluno"));
		alunoCenso.setCodigoIbgeEstado(dadosSQL.getInt("estadocodigoibge"));
		if (tipoLayout.equals(Censo.TECNICO)) {
			alunoCenso.getMatriculaPeriodo().getTurma().setCodigo(dadosSQL.getInt("turma"));
		}

		if(tipoLayout.equals(Censo.GRADUACAO) || tipoLayout.equals(Censo.GRADUACAO_TECNOLOGICA)){
			FormacaoAcademicaVO formacaoAcademica = new FormacaoAcademicaVO();
			formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridade(Integer.parseInt(alunoCenso.getCodigoAluno()), NivelFormacaoAcademica.MEDIO, false, usuario);
			alunoCenso.setTipoEscolaConcluiuEnsinoMedio(formacaoAcademica.getTipoInst());
		}
		// Integer(dadosSQL.getInt("turma")));
		return alunoCenso;
	}

	/***
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>AlunoCensoVO</code>.
	 * 
	 * @param alunoCenso
	 * @param dadosSQL
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static AlunoCensoVO montarDadosCensoAlunoEnsinoBasico(AlunoCensoVO alunoCenso, SqlRowSet dadosSQL, UsuarioVO usuario,
			String tipoLayout) throws Exception {
		alunoCenso.setTipoRegistro(dadosSQL.getString("tiporegistro"));
		alunoCenso.setIdAlunoINEP(dadosSQL.getString("idalunoinep"));
		alunoCenso.setIdAlunoIES(dadosSQL.getString("idalunoies"));
		alunoCenso.setCodigo(dadosSQL.getInt("codigoAluno"));
		alunoCenso.setNome(dadosSQL.getString("nomeCenso"));
		alunoCenso.setNomeMae(dadosSQL.getString("nomeMae"));
		alunoCenso.setNomePai(dadosSQL.getString("nomePai"));
		alunoCenso.setCpf(dadosSQL.getString("cpf"));
		alunoCenso.setPassaporte(dadosSQL.getString("documentoestrangeiro"));
		alunoCenso.setDataNasc(Uteis.getData(dadosSQL.getDate("dataNasc"), "dd/MM/yyyy"));
		alunoCenso.setSexo(dadosSQL.getString("sexo"));
		alunoCenso.setCorRaca(dadosSQL.getString("corraca"));
		// alunoCenso.setNomeFiliador(dadosSQL.getString("nomefiliador"));//Nome
		// da Mãe
		alunoCenso.setNacionalidade(dadosSQL.getString("nacionalidade"));
		alunoCenso.setUfDeNascimento(dadosSQL.getString("ufdenascimento"));
		alunoCenso.setMunicipioDeNascimento(dadosSQL.getString("municipiodenascimento"));
		alunoCenso.setPaisOrigem(dadosSQL.getString("paisorigem"));
		alunoCenso.setAlunoComDeficiencia(dadosSQL.getString("alunocomdeficiencia"));
		alunoCenso.setCegueira(dadosSQL.getString("cegueira"));
		alunoCenso.setBaixaVisao(dadosSQL.getString("baixavisao"));
		alunoCenso.setSurdez(dadosSQL.getString("surdez"));
		alunoCenso.setAuditiva(dadosSQL.getString("auditiva"));
		alunoCenso.setFisica(dadosSQL.getString("fisica"));
		alunoCenso.setSurdocegueira(dadosSQL.getString("surdocegueira"));
		alunoCenso.setMultipla(dadosSQL.getString("multipla"));
		alunoCenso.setMental(dadosSQL.getString("mental"));
		alunoCenso.setMatricula(dadosSQL.getString("matricula"));
		alunoCenso.setCodigoAluno(dadosSQL.getString("codigoaluno"));
		alunoCenso.setEtapaEnsino(dadosSQL.getString("etapa"));
		alunoCenso.setNivelEducacional(dadosSQL.getString("niveleducacionalaluno"));
		if (tipoLayout.equals(Censo.EDUCACAO_BASICA_TECNICO)) {
			alunoCenso.getMatriculaPeriodo().getTurma().setCodigo(dadosSQL.getInt("turma"));
		}

		// Integer(dadosSQL.getInt("turma")));
		return alunoCenso;
	}

	public String getSqlConsultaCursoPorAluno(String matriculas,  String ano, 
			String tipoLayout) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT distinct count(*) over() as totalRegistros, 42 as tiporegistro, ");
		// Codigo do Inep carregar da unidadeensinocurso caso não exista trazer do
		// curso.

		sqlStr.append(" case when ( ");
		sqlStr.append(" (select codigoinep from unidadeensinocurso where unidadeensinocurso.unidadeensino = unidadeensino.codigo ")
		.append(" and unidadeensinocurso.curso = curso.codigo limit 1) is null ");
		sqlStr.append(" or ( ");
		sqlStr.append(" select codigoinep from unidadeensinocurso where unidadeensinocurso.unidadeensino = unidadeensino.codigo ")
				.append(" and unidadeensinocurso.curso = curso.codigo limit 1) = 0 )");
		sqlStr.append(" then ( case when curso.idcursoinep = 0 then null else curso.idcursoinep end)");
		sqlStr.append(" else ( select codigoinep from unidadeensinocurso where unidadeensinocurso.unidadeensino = unidadeensino.codigo ")
				.append(" and unidadeensinocurso.curso = curso.codigo limit 1)");
		sqlStr.append(" end as idcursoinep,");
		sqlStr.append(" case when curso.modalidadecurso = 'ON_LINE' and unidadeensino.codigoies > 0 then unidadeensino.codigoies::text else '' end as poloeadinep, ");
		sqlStr.append(" case when coalesce(unidadeensino.matriz, false) is true then null else unidadeensino.codigoIES end  as poloeadinep, ");
		sqlStr.append(" CASE WHEN turma.codigoTurnoApresentarCenso > 0 THEN turma.codigoTurnoApresentarCenso ELSE ");
		sqlStr.append(" CASE ");
		sqlStr.append(
				" WHEN COALESCE(cursoTurno.nomeTurnoCenso,'') <> '' and cursoTurno.nomeTurnoCenso = 'MATUTINO' then 1 ");
		sqlStr.append(
				" WHEN COALESCE(cursoTurno.nomeTurnoCenso,'') <> '' and cursoTurno.nomeTurnoCenso = 'VESPERTINO' then 2 ");
		sqlStr.append(
				" WHEN COALESCE(cursoTurno.nomeTurnoCenso,'') <> '' and cursoTurno.nomeTurnoCenso = 'NOTURNO' then 3 ");
		sqlStr.append(
				" WHEN COALESCE(cursoTurno.nomeTurnoCenso,'') <> '' and cursoTurno.nomeTurnoCenso = 'INTEGRAL' then 4 ");
		sqlStr.append(" ELSE CASE ");
		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Matutino%') THEN 1 ");
		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Vespertino%') THEN 2 ");
		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Noturno%') THEN 3 ");
		sqlStr.append(
				" WHEN (UPPER(turno.nome) like UPPER('%Integral%') OR UPPER(turno.nome) like UPPER('%Diurno%')) THEN 4 ");
		sqlStr.append(" END END END as turno, ");

//		sqlStr.append(" CASE WHEN UPPER(turno.nome) like UPPER('%Matutino%') THEN 1 ");
//		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Vespertino%') THEN 2 ");
//		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Noturno%') THEN 3 ");
//		sqlStr.append(" WHEN (UPPER(turno.nome) like UPPER('%Integral%') OR UPPER(turno.nome) like UPPER('%Diurno%')) THEN 4 END as turno, ");
		sqlStr.append(" ultimamatriculaperiodo.situacaomatriculaperiodo AS situacaovinculo,  ");

		sqlStr.append("(select cursoOrigem.idcursoinep from transferenciaentrada ");
		sqlStr.append(" inner join matricula matriculaOrigem on matriculaOrigem.matricula = transferenciaentrada.matricula ");
		sqlStr.append(" inner join curso cursoOrigem on cursoOrigem.codigo =  matriculaOrigem.curso ");
		sqlStr.append(" inner join matriculaperiodo as ultimamatriculaperiodo_ts on ultimamatriculaperiodo_ts.matricula = matriculaOrigem.matricula and ultimamatriculaperiodo_ts.codigo = ( ");
		sqlStr.append(" select mp.codigo from matriculaperiodo mp where mp.matricula = matriculaOrigem.matricula order by mp.ano || '/' || mp.semestre desc limit 1)  ");
		sqlStr.append(" where matriculaOrigem.aluno = aluno.codigo ");
		sqlStr.append(" and cursoOrigem.niveleducacional = curso.niveleducacional ");
		sqlStr.append(" and ultimamatriculaperiodo_ts.ano = '").append(ano).append("' ");
		sqlStr.append(
				" and transferenciaentrada.curso = matricula.curso and transferenciaentrada.tipotransferenciaentrada = 'IN' and matriculaOrigem.naoapresentarcenso = false and transferenciaentrada.situacao != 'IN' order by transferenciaentrada.codigo desc limit 1 ");
		sqlStr.append(" ) as cursoorigem, ");

//		sqlStr.append(" primeiramatriculaperiodo.ano as anoingresso, ");
		sqlStr.append(
				" case when matricula.semestreanoingressocenso is not null and matricula.semestreanoingressocenso != '' then ");
		sqlStr.append(" substring(matricula.semestreanoingressocenso, 3) else primeiramatriculaperiodo.ano ");
		sqlStr.append(" end as anoingresso, ");
		sqlStr.append(" case when matricula.semestreanoingressocenso is not null and matricula.semestreanoingressocenso != '' then ");
		sqlStr.append(" matricula.semestreanoingressocenso when coalesce(matricula.semestreingresso, '') <> '' and coalesce(matricula.anoingresso, '') <> '' ");
		sqlStr.append(" then '0'||matricula.semestreingresso||matricula.anoingresso ");
		sqlStr.append(" else '0'||(case when curso.periodicidade = 'AN' then (case when extract(month from primeiramatriculaperiodo.data) > 6 then '2' else '1' end ) else primeiramatriculaperiodo.semestre end)||primeiramatriculaperiodo.ano ");
		sqlStr.append(" end as dataingresso, ");

		sqlStr.append(
				" case (select tipoinst  from formacaoacademica  where    formacaoacademica.pessoa  = aluno.codigo  ");
		sqlStr.append(" and formacaoacademica.escolaridade =  'EM' limit 1) ");
		sqlStr.append(" when 'PU' then 1 when 'PR' then 0 else 2 end as alunoprocedenteescolapublica ,  ");
		sqlStr.append(
				" matricula.formaingresso, matricula.programareservavaga, matricula.financiamentoestudantil, matricula.apoiosocial,  matricula.atividadecomplementar ");
		sqlStr.append(
				" , curso.nome,curso.titulo as titulo, matricula.data as dataMatricula, aluno.dataNasc, aluno.codigo as aluno, ");
		sqlStr.append(" case when matricula.situacao = 'FO' then (case when curso.periodicidade = 'AN' then (case when extract(month from ultimamatriculaperiodo.data) > 6 then '2' else '1' end ) else ultimamatriculaperiodo.semestre end) else '' end as semestreconclusao, ");

		// Consulta a carga Horaria Total do Curso
		sqlStr.append(
				"(select cargahoraria from gradecurricular where gradecurricular.codigo = matricula.gradecurricularAtual) AS cargaHorariaTotalCurso, ");
		// Consulta a carga horária integralizada pelo aluno
		sqlStr.append(
				"(SELECT SUM(case when historico.cargahorariadisciplina is not null and historico.cargahorariadisciplina > 0");
		sqlStr.append(" then historico.cargahorariadisciplina else case when gradedisciplina.codigo is not null");
		sqlStr.append(
				" then gradedisciplina.cargahoraria else gradecurriculargrupooptativadisciplina.cargahoraria end end");
		sqlStr.append(") AS CARGAHORARIA FROM historico");
		sqlStr.append(" LEFT JOIN gradedisciplina on gradedisciplina.codigo = historico.gradeDisciplina ");
		sqlStr.append(
				" LEFT JOIN gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" WHERE historico.MATRICULA = matricula.matricula ");
		List<SituacaoHistorico> listaSituacaoAprovacao = SituacaoHistorico.getSituacoesDeAprovacao();
		if (Uteis.isAtributoPreenchido(listaSituacaoAprovacao)) {
			sqlStr.append(listaSituacaoAprovacao.stream().map(SituacaoHistorico::getValor)
					.collect(Collectors.joining("', '", "AND historico.situacao in ('", "')")));
		}
		sqlStr.append(" AND historico.matrizcurricular = matricula.gradecurricularatual ");
		sqlStr.append(
				" and (historico.gradecurriculargrupooptativadisciplina is not null or historico.gradedisciplina is not null or historicodisciplinaforagrade) ");
		sqlStr.append(" and (historicoequivalente = false or historicoequivalente is null) ");
		sqlStr.append(
				" and (historicodisciplinafazpartecomposicao = false or historicodisciplinafazpartecomposicao is null) ");
		sqlStr.append(") AS cargaHorariaIntegralizadaAluno, ");

		sqlStr.append(
				"(select case when sum(racm.cargaHorariaConsiderada) > gradecurricularatual.totalcargahorariaatividadecomplementar then  ");
		sqlStr.append(
				" gradecurricularatual.totalcargahorariaatividadecomplementar else sum(racm.cargaHorariaConsiderada) ");
		sqlStr.append(" end as cargaHorariaConsiderada ");
		sqlStr.append(" from registroatividadecomplementarmatricula as racm ");
		sqlStr.append(" inner join registroatividadecomplementar rac on	racm.registroatividadecomplementar = rac.codigo ");
		sqlStr.append(" where racm.matricula = matricula.matricula AND racm.situacaoatividadecomplementarmatricula = 'DEFERIDO' AND (racm.datadeferimentoindeferimento is null OR extract(year from rac.data) <= ").append(ano);
		sqlStr.append(" ) ) AS cargahorariaRealizadaAtividadeComplementar, ");
		sqlStr.append(" (SELECT sum(estagio.cargaHoraria) from estagio where estagio.matricula = matricula.matricula AND estagio.ano <= '").append(ano).append("') as cargaHorariaEstagio, ");
		sqlStr.append(" (SELECT array_to_string(array_agg(formacaoacademica.titulo), ';') AS titulos FROM formacaoacademica WHERE formacaoacademica.pessoa = aluno.codigo ");
		sqlStr.append(" AND formacaoacademica.situacao = 'CO' AND formacaoacademica.escolaridade IN ('GR', 'SU') GROUP BY formacaoacademica.pessoa ) AS titulos, ");
		
		sqlStr.append(" matricula.matricula, matricula.tipomobilidadeacademica, ");
		sqlStr.append(" CASE WHEN matricula.informacoescensorelativoano = '").append(ano).append("' THEN matricula.justificativaCenso ELSE 'NENHUMA' END justificativaCenso, ");
		sqlStr.append(" CASE WHEN matricula.tipomobilidadeacademica = '").append(TipoMobilidadeAcademicaEnum.NACIONAL).append("' THEN matricula.mobilidadeacademicanacionaliesdestino ");
		sqlStr.append(" WHEN matricula.tipomobilidadeacademica = '").append(TipoMobilidadeAcademicaEnum.INTERNACIONAL).append("' THEN matricula.mobilidadeAcademicaInternacionalPaisDestino ");
		sqlStr.append(" ELSE '' END complementomobilidadeacademica ");

		sqlStr.append(" FROM matricula ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(
				" LEFT JOIN cursoTurno ON cursoTurno.curso = curso.codigo and cursoTurno.turno = matricula.turno ");
		// sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula =
		// matricula.matricula and matriculaperiodo.ano <= '").append(ano).append("' ");
		sqlStr.append(
				" INNER JOIN matriculaperiodo as ultimamatriculaperiodo ON ultimamatriculaperiodo.matricula = matricula.matricula  and ultimamatriculaperiodo.codigo = (select codigo from matriculaperiodo  as mp where  mp.ano <= '")
				.append(ano)
				.append("' and mp.situacaomatriculaperiodo not in ('PR', 'PC') and mp.matricula = matricula.matricula order by mp.ano||'/'||mp.semestre desc limit 1 )");
		sqlStr.append(
				" INNER JOIN matriculaperiodo as primeiramatriculaperiodo ON primeiramatriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(" and primeiramatriculaperiodo.codigo = ");
		sqlStr.append(
				" ( select codigo from matriculaperiodo  as mp where mp.situacaomatriculaperiodo not in ('PR', 'PC') and mp.matricula = matricula.matricula order by mp.ano||'/'||mp.semestre asc limit 1 )");

		sqlStr.append(" INNER JOIN pessoa as aluno ON aluno.codigo = matricula.aluno ");
		sqlStr.append(" INNER JOIN turno ON turno.codigo = matricula.turno ");
		sqlStr.append(" INNER JOIN gradecurricular gradecurricularatual on gradecurricularatual.codigo = matricula.gradecurricularatual ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = ultimamatriculaperiodo.turma ");
//		sqlStr.append(" LEFT JOIN transferenciaentrada on transferenciaentrada.matricula = matricula.matricula and  transferenciaentrada.tipotransferenciaentrada = 'IN' and matricula.situacao = 'TI'  and transferenciaentrada.situacao = 'EF' ");
//		sqlStr.append(" LEFT JOIN curso as cursotransferido on cursotransferido.codigo = transferenciaentrada.curso ");
//		sqlStr.append(" and cursotransferido.codigo in (select curso from matricula m where m.aluno = matricula.aluno) ");
		if (tipoLayout.equals("TECNICO")) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'PR' ");
		} else if (tipoLayout.equals(Censo.GRADUACAO)) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'SU' ");
		} else if (tipoLayout.equals(Censo.GRADUACAO_TECNOLOGICA)) {
			sqlStr.append(" WHERE curso.nivelEducacional = 'GT' ");
		}
		sqlStr.append(" and ((ultimamatriculaperiodo.ano = '").append(ano).append("') ");
		sqlStr.append(" or (ultimamatriculaperiodo.ano <= '").append(Integer.parseInt(ano) - 1).append(
				"' and ultimamatriculaperiodo.situacaomatriculaperiodo not in ('FO', 'DE', 'IN', 'TS', 'TI', 'FI', 'JU', 'CA')) ");
		sqlStr.append(" or (ultimamatriculaperiodo.ano < '").append(Integer.parseInt(ano) - 1)
				.append("' and ultimamatriculaperiodo.situacaomatriculaperiodo in ('TR', 'AC'))) ");
		sqlStr.append(" and extract(year from ultimamatriculaperiodo.data)<= " + ano + " ");

//		sqlStr.append(" and (matricula.matricula =  ");
//		sqlStr.append(" (select matricula from matriculaperiodo where codigo = ");
//		sqlStr.append(" (select max(matriculaperiodo.codigo) from matriculaperiodo inner join matricula m ");
//		sqlStr.append(" on m.matricula = matriculaperiodo.matricula where ano::INT >= ").append(Integer.parseInt(ano) - 1);
//		sqlStr.append(" AND ano::INT <= ").append(Integer.parseInt(ano));
//		sqlStr.append(" AND m.matricula = matricula.matricula and curso.codigo = m.curso group by curso, aluno ) ) ");
//		sqlStr.append(" or (matricula.situacao =  'TR' and matriculaperiodo.situacaomatriculaperiodo = 'TR' and matriculaperiodo.ano < '");
//		sqlStr.append((Integer.parseInt(ano) - 1) + "')) ");
		sqlStr.append(" AND matricula.aluno in ( ").append(matriculas).append(") ");
		sqlStr.append(" and matricula.situacao not in ('PR', 'ER') ");
		sqlStr.append(" and COALESCE(matricula.naoapresentarcenso, false) = false ");
		sqlStr.append(" and curso.idcursoinep is not null and curso.idcursoinep > 0 ");
		return sqlStr.toString();
	}

	public String getSqlConsultaCursoPorAlunoTecnico(Date dataBase, int codigoUnidadeEnsino, String ano) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT distinct 42 as tiporegistro, ");
		sqlStr.append(" CASE WHEN curso.idcursoinep = 0 THEN null ELSE curso.idcursoinep END as idcursoinep, ");
		sqlStr.append("  '' as poloeadinep, ");
		sqlStr.append(" CASE WHEN turma.codigoTurnoApresentarCenso > 0 THEN turma.codigoTurnoApresentarCenso ELSE ");
		sqlStr.append(" CASE WHEN UPPER(turno.nome) like UPPER('%Matutino%') THEN 1 ");
		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Vespertino%') THEN 2 ");
		sqlStr.append(" WHEN UPPER(turno.nome) like UPPER('%Noturno%') THEN 3 ");
		sqlStr.append(
				" WHEN (UPPER(turno.nome) like UPPER('%Integral%') OR UPPER(turno.nome) like UPPER('%Diurno%')) THEN 4 END END as turno, ");
		sqlStr.append(" matriculaperiodo.situacaomatriculaperiodo AS situacaovinculo,  ");
		sqlStr.append(" (select cursoOrigem.idcursoinep from transferenciaentrada ");
		sqlStr.append(
				" inner join matricula matriculaOrigem on matriculaOrigem.matricula = transferenciaentrada.matricula ");
		sqlStr.append(" inner join curso cursoOrigem on cursoOrigem.codigo =  matriculaOrigem.curso ");
		sqlStr.append(" where matriculaOrigem.aluno = aluno.codigo ");
		sqlStr.append(" and cursoOrigem.niveleducacional = curso.niveleducacional ");
		sqlStr.append(" and extract(year from transferenciaentrada.data) = '").append(ano).append("' ");
		sqlStr.append(
				" and transferenciaentrada.curso = matricula.curso and transferenciaentrada.tipotransferenciaentrada = 'IN' and matriculaOrigem.naoapresentarcenso = false and transferenciaentrada.situacao != 'IN' order by transferenciaentrada.codigo desc limit 1 ");
		sqlStr.append(" ) as cursoorigem, ");
		sqlStr.append(" (select datainicioperiodoletivo as dataingresso from matriculaperiodo ");
		sqlStr.append(" inner join unidadeensinocurso on   unidadeensinocurso.unidadeensino = matricula.unidadeensino");
		sqlStr.append(" and unidadeensinocurso.curso = matricula.curso and unidadeensinocurso.turno = matricula.turno");
		sqlStr.append(
				" inner join periodoletivoativounidadeensinocurso on unidadeensinocurso.codigo = periodoletivoativounidadeensinocurso.unidadeensinocurso");
		sqlStr.append(
				" and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = matriculaperiodo.ano and semestrereferenciaperiodoletivo = semestre");
		sqlStr.append(" where matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" order by ano, semestre limit 1) as dataingresso,");
		sqlStr.append(
				" case (select tipoinst  from formacaoacademica  where    formacaoacademica.pessoa  = aluno.codigo  ");
		sqlStr.append(" and formacaoacademica.escolaridade =  'EM' limit 1) ");
		sqlStr.append(" when 'PU' then 1 when 'PR' then 0 else 2 end as alunoprocedenteescolapublica ,  ");
		sqlStr.append(
				" matricula.matricula as matricula, matricula.formaingresso, matricula.programareservavaga, matricula.financiamentoestudantil, matricula.apoiosocial,  matricula.atividadecomplementar ");
		sqlStr.append(" , curso.nome, matricula.data as dataMatricula, aluno.dataNasc, aluno.codigo as aluno, matricula.tipomobilidadeacademica, ");
		sqlStr.append(" CASE WHEN matricula.informacoescensorelativoano = '").append(ano).append("' THEN matricula.justificativaCenso ELSE 'NENHUMA' END justificativaCenso, ");
		sqlStr.append(" CASE WHEN matricula.tipomobilidadeacademica = '").append(TipoMobilidadeAcademicaEnum.NACIONAL).append("' THEN matricula.mobilidadeacademicanacionaliesdestino ");
		sqlStr.append(" WHEN matricula.tipomobilidadeacademica = '").append(TipoMobilidadeAcademicaEnum.INTERNACIONAL).append("' THEN matricula.mobilidadeAcademicaInternacionalPaisDestino ");
		sqlStr.append(" ELSE '' END complementomobilidadeacademica ");
		sqlStr.append(
				" case when matricula.situacao = 'FO' then matriculaperiodo.semestre else '' end as semestreconclusao, curso.titulo, ");

		// Consulta a carga Horaria Total do Curso
		sqlStr.append("(select sum(cargahoraria) from ( ");
		sqlStr.append(" select gradecurricular.codigo, gradecurricular.cargahoraria from gradecurricular  ");
		sqlStr.append(" where gradecurricular.codigo = matricula.gradecurricularAtual ");
		sqlStr.append(" ) as t) AS cargaHorariaTotalCurso, ");
		// Consulta a carga horária integralizada pelo aluno
		sqlStr.append(
				"(SELECT SUM(case when historico.cargahorariadisciplina is not null and historico.cargahorariadisciplina > 0");
		sqlStr.append(" then historico.cargahorariadisciplina else case when gradedisciplina.codigo is not null");
		sqlStr.append(
				" then gradedisciplina.cargahoraria else gradecurriculargrupooptativadisciplina.cargahoraria end end");
		sqlStr.append(") AS CARGAHORARIA FROM historico");
		sqlStr.append(" LEFT JOIN gradedisciplina on gradedisciplina.codigo = historico.gradeDisciplina ");
		sqlStr.append(
				" LEFT JOIN gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append(" WHERE historico.MATRICULA = matricula.matricula ");
		List<SituacaoHistorico> listaSituacaoAprovacao = SituacaoHistorico.getSituacoesDeAprovacao();
		if (Uteis.isAtributoPreenchido(listaSituacaoAprovacao)) {
			sqlStr.append(listaSituacaoAprovacao.stream().map(SituacaoHistorico::getValor)
					.collect(Collectors.joining("', '", "AND historico.situacao in ('", "')")));
		}
		sqlStr.append(" AND historico.matrizcurricular = matricula.gradecurricularatual ");
		sqlStr.append(
				" and (historico.gradecurriculargrupooptativadisciplina is not null or historico.gradedisciplina is not null or historicodisciplinaforagrade) ");
		sqlStr.append(" and (historicoequivalente = false or historicoequivalente is null) ");
		sqlStr.append(
				" and (historicodisciplinafazpartecomposicao = false or historicodisciplinafazpartecomposicao is null) ");
		sqlStr.append(") AS cargaHorariaIntegralizadaAluno, ");

		sqlStr.append("(select sum(racm.cargaHorariaConsiderada) as cargaHorariaConsiderada ");
		sqlStr.append(" from registroatividadecomplementarmatricula as racm ");
		sqlStr.append(" where racm.matricula = matricula.matricula ");
		sqlStr.append(" ) AS cargahorariaRealizadaAtividadeComplementar, ");
		sqlStr.append(
				"(SELECT sum(estagio.cargaHoraria) from estagio where estagio.matricula = matricula.matricula) as cargaHorariaEstagio ");

		sqlStr.append(" FROM matricula ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(
				" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula and   matriculaperiodo.data <= '")
				.append(Uteis.getDataJDBC(dataBase)).append("' ");
		sqlStr.append(
				" INNER JOIN matriculaperiodo as ultimamatriculaperiodo ON ultimamatriculaperiodo.matricula = matricula.matricula  and ultimamatriculaperiodo.codigo = (select codigo from matriculaperiodo  as mp where  mp.ano <= '")
				.append(Uteis.getDataJDBC(dataBase))
				.append("' and mp.situacaomatriculaperiodo not in ('PR', 'PC') and mp.matricula = matricula.matricula order by mp.ano||'/'||mp.semestre desc limit 1 )");
		sqlStr.append(" INNER JOIN pessoa as aluno ON aluno.codigo = matricula.aluno ");
		sqlStr.append(" INNER JOIN turno ON turno.codigo = matricula.turno ");
		sqlStr.append(" LEFT JOIN transferenciaentrada on transferenciaentrada.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN turma on turma.codigo = ultimamatriculaperiodo.turma ");
		sqlStr.append(" WHERE curso.nivelEducacional = 'PR' ");
		sqlStr.append(" and matricula.matricula in (");
		sqlStr.append(" select matricula.matricula  from matricula ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(
				" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.data <= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("' order by data asc limit 1 )");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" where curso.niveleducacional = 'PR' ");
		sqlStr.append(
				"and ((matriculaperiodo.situacaomatriculaperiodo IN  ('TR', 'CA', 'AB', 'TS', 'TI', 'FI') and datafechamentomatriculaperiodo is not null and datafechamentomatriculaperiodo >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("')");
		sqlStr.append(" or (matriculaperiodo.situacaomatriculaperiodo IN  ('AT'))");
		sqlStr.append(
				"or (matriculaperiodo.situacaomatriculaperiodo IN  ('FO') AND matricula.dataatualizacaomatriculaformada >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("'))");
		sqlStr.append(")");

		sqlStr.append(" and matricula.unidadeensino = ").append(codigoUnidadeEnsino);
		sqlStr.append(" and matricula.situacao not in ('PR', 'ER') ");
		sqlStr.append(" and matricula.naoapresentarcenso = 'false' ");
		sqlStr.append(" AND COALESCE(curso.idcursoinep, 0) != 0 ");
		return sqlStr.toString();
	}

	/***
	 * Método responsável por trazer os cursos por aluno para arquivo do censo
	 * 
	 * @param codigoAluno
	 * @param codigoUnidadeEnsino
	 * @param ano
	 * @param semestre
	 * @return
	 */
	public List<CursoCensoVO> consultarCursosPorAluno(List<AlunoCensoVO> alunoCensoVOs, 
			String ano, UsuarioVO usuario, String tipoLayout, ProgressBarVO progressBarVO) throws Exception {

		List<CursoCensoVO> listaCursoCenso = new ArrayList<>(0);
		StringBuilder sqlStr = new StringBuilder();
		StringBuilder matriculas = new StringBuilder();
		for (AlunoCensoVO aluno : alunoCensoVOs) {
			if (!matriculas.toString().isEmpty()) {
				matriculas.append(", ");
			}
			matriculas.append(aluno.getCodigoAluno());
		}
		sqlStr.append(getSqlConsultaCursoPorAluno(matriculas.toString(), ano, tipoLayout));
		sqlStr.append(" ORDER BY aluno,  dataingresso, datamatricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		int x = 1;
		while (tabelaResultado.next()) {
			progressBarVO.setStatus("Montando informações dos cursos (Lote "+progressBarVO.getProgresso1()+") "+x+"/"+tabelaResultado.getInt("totalRegistros"));			
			CursoCensoVO cursoCenso = new CursoCensoVO();
			montarDadosCensoCursoAluno(cursoCenso, ano, tabelaResultado, usuario);
			listaCursoCenso.add(cursoCenso);
			x++;
		}
		return listaCursoCenso;
	}

	@Deprecated
	public List<CursoCensoVO> consultarCursosPorAlunoTecnico(List<AlunoCensoVO> alunoCensoVOs, String ano,
			int codigoUnidadeEnsino, UsuarioVO usuario, Date dataBase) throws Exception {

		List<CursoCensoVO> listaCursoCenso = new ArrayList<>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(getSqlConsultaCursoPorAlunoTecnico(dataBase, codigoUnidadeEnsino, ano));
		sqlStr.append(" ORDER BY aluno, nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			CursoCensoVO cursoCenso = new CursoCensoVO();
			montarDadosCensoCursoAluno(cursoCenso, ano, tabelaResultado, usuario);
			listaCursoCenso.add(cursoCenso);
		}
		return listaCursoCenso;
	}

	/***
	 * Método responsável por montar os dados dos cursos pelo aluno
	 * 
	 * @param cursoCenso
	 * @param dadosSQL
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static CursoCensoVO montarDadosCensoCursoAluno(CursoCensoVO cursoCenso, String ano, SqlRowSet dadosSQL,
			UsuarioVO usuario) throws Exception {
		cursoCenso.setMatricula(dadosSQL.getString("matricula"));
		cursoCenso.setTipoRegistro(dadosSQL.getString("tiporegistro"));
		cursoCenso.setCodigoAluno(String.valueOf(dadosSQL.getInt("aluno")));
		cursoCenso.setIdCursoInep(dadosSQL.getString("idcursoinep"));
		cursoCenso.setCodigoPoloEADINEP(dadosSQL.getString("poloeadinep"));
		cursoCenso.setTurnoAluno(dadosSQL.getString("turno"));
		cursoCenso.setNomeCurso(dadosSQL.getString("nome"));
		cursoCenso.setSituacaoVinculo(dadosSQL.getString("situacaovinculo"));
		cursoCenso.setDataIngresso(dadosSQL.getString("dataingresso"));
		cursoCenso.setAlunoProcedenteEscolaPublica(dadosSQL.getString("alunoprocedenteescolapublica"));
		cursoCenso.setOutrasFormasDeIngresso(dadosSQL.getString("formaingresso"));
		cursoCenso.setProgramaReservaVagasOutros(dadosSQL.getString("programareservavaga"));
		cursoCenso.setFinanciamentoEstudantil(dadosSQL.getString("financiamentoestudantil"));
		cursoCenso.setApoioSocial(dadosSQL.getString("apoiosocial"));
		cursoCenso.setAtividadeFormacaoComplementar(dadosSQL.getString("atividadecomplementar"));
		cursoCenso.setDataMatricula(dadosSQL.getDate("dataMatricula"));
		cursoCenso.setDataNasc(dadosSQL.getDate("dataNasc"));
		cursoCenso.setCursoOrigem(dadosSQL.getInt("cursoorigem"));
		cursoCenso.setSemestreconclusao(dadosSQL.getString("semestreconclusao"));
		cursoCenso.setTitulo(dadosSQL.getString("titulo"));
		cursoCenso.setCargaHorariaTotalCurso(dadosSQL.getInt("CargaHorariaTotalCurso"));
		cursoCenso.setCargaHorariaIntegralizadaAluno(dadosSQL.getInt("cargaHorariaIntegralizadaAluno")
				+ dadosSQL.getInt("cargahorariaRealizadaAtividadeComplementar")
				+ dadosSQL.getInt("cargaHorariaEstagio"));
		if (dadosSQL.getString("justificativaCenso") != null && JustificativaCensoEnum.valueOf(dadosSQL.getString("justificativaCenso")) != null) {
			cursoCenso.setJustificativaCenso(JustificativaCensoEnum.valueOf(dadosSQL.getString("justificativaCenso")).getValor());
		}
		if (dadosSQL.getString("tipomobilidadeacademica") != null) {
			TipoMobilidadeAcademicaEnum tipoMobilidadeAcademicaEnum = TipoMobilidadeAcademicaEnum.valueOf(dadosSQL.getString("tipomobilidadeacademica"));
			if (tipoMobilidadeAcademicaEnum != null && !tipoMobilidadeAcademicaEnum.isNenhuma()) {
				if (tipoMobilidadeAcademicaEnum.isVazio()) {
					cursoCenso.setMobilidadeAcademica("");
				} else {
					cursoCenso.setMobilidadeAcademica("1");
					cursoCenso.setTipoMobilidadeAcademica(tipoMobilidadeAcademicaEnum.getValor());
					cursoCenso.setMobilidadeAcademicaComplemento(dadosSQL.getString("complementomobilidadeacademica"));
				}
			}
		}
		cursoCenso.setListaFinanciamentoEstudantilVOs(consultarFinanciamentoEstudantilPorAluno(cursoCenso.getMatricula(), ano));
		if (!Uteis.isAtributoPreenchido(cursoCenso.getFinanciamentoEstudantil()) && Uteis.isAtributoPreenchido(cursoCenso.getListaFinanciamentoEstudantilVOs())) {
			cursoCenso.setFinanciamentoEstudantil("1");
		}
		return cursoCenso;
	}
	
	public static List<String> consultarFinanciamentoEstudantilPorAluno(String matricula, String ano) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct convenio.tipofinanciamentoestudantil from planofinanceiroaluno ");
		sb.append(
				" inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.planofinanceiroaluno = planofinanceiroaluno.codigo ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = planofinanceiroaluno.matriculaperiodo ");
		sb.append(" inner join convenio on convenio.codigo = itemplanofinanceiroaluno.convenio ");
		sb.append(" WHERE planofinanceiroaluno.matricula = ? ");
		if (!ano.equals("")) {
			sb.append(" AND ano = '").append(ano).append("' ");
		}
		sb.append(" and tipofinanciamentoestudantil is not null ");
		sb.append(" UNION SELECT DISTINCT regexp_split_to_table(financiamentoEstudantilCenso, ';') tipofinanciamentoestudantil FROM matricula ");
		sb.append(" WHERE matricula.matricula = ? and matricula.financiamentoEstudantilCenso <> '' AND matricula.informacoescensorelativoano = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), matricula, matricula, ano);
		List<String> listaFinanciamentoEstudantilVOs = new ArrayList<String>(0);
		while (tabelaResultado.next()) {
			listaFinanciamentoEstudantilVOs.add(tabelaResultado.getString("tipofinanciamentoestudantil"));
		}
		return listaFinanciamentoEstudantilVOs;
	}

	/***
	 * Método responsável por trazer os professores de acordo com ano semestre e
	 * unidade ensino
	 * 
	 * @param ano
	 * @param semestre
	 * @return
	 */
	public List<ProfessorCensoVO> consultarProfessorAnoSemestre(int unidadeEnsino, String ano, String semestre,
			String tipoLayout, UsuarioVO usuario) throws Exception {

		List listaCursoCenso = new ArrayList<CursoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT  31 AS tiporegistro, ");
		sqlStr.append(" pessoa.codigo AS idprofessories,");
		sqlStr.append(" pessoa.nome, pessoa.cidade, ");
		sqlStr.append("cidade.codigoinep as  municipiodenascimento,estado.codigoinep as ufdenascimento,  ");
		sqlStr.append(" replace(replace(pessoa.cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(
				" CASE WHEN pessoa.nacionalidade = '1' THEN '' WHEN (pessoa.nacionalidade <> '1' AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" to_char(pessoa.datanasc,'DDMMYYYY') as datanascimento, ");
		if (tipoLayout.equals("TECNICO")) {
			sqlStr.append(" CASE WHEN pessoa.sexo = 'M' THEN '1' ");
			sqlStr.append(" WHEN pessoa.sexo = 'F' THEN '2' END as sexo, ");
		} else {
			sqlStr.append(" CASE WHEN pessoa.sexo = 'M' THEN '0' ");
			sqlStr.append(" WHEN pessoa.sexo = 'F' THEN '1' END as sexo, ");
		}
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' ) THEN '0' END as corraca, ");
		sqlStr.append(" mae.nome as nomecompletomae, ");
		sqlStr.append(
				"  CASE WHEN pessoa.nacionalidade = 1 THEN 1  WHEN pessoa.nacionalidade <> 1 THEN 3 END as nacionalidade, ");
		// sqlStr.append(" '' as ufdenascimento, ");
		// sqlStr.append(" '' as municipiodenascimento, ");
		sqlStr.append(" CASE WHEN pessoa.nacionalidade = 1 THEN 'BRA' ");
		sqlStr.append(" WHEN pessoa.nacionalidade <> 1 THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'NE' OR pessoa.deficiencia = '' OR pessoa.deficiencia is null OR pessoa.deficiencia = '0' ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '1' ELSE '0' END as docentecomdeficiencia , ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'CE' OR pessoa.deficiencia = '2' ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'CE' ) AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0'  ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as cegueira, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'BV' OR pessoa.deficiencia = '3'  )  THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'BV') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as baixavisao, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SU' OR pessoa.deficiencia = '4'  ) THEN '1' WHEN (pessoa.deficiencia <> 'SU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as surdez, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'AU' OR pessoa.deficiencia = '5'  ) THEN '1' ");
		sqlStr.append(
				"   WHEN (pessoa.deficiencia <> 'AU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as auditiva, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'FI' OR pessoa.deficiencia = '6'  ) THEN '1' WHEN (pessoa.deficiencia <> 'FI') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as fisica, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SC' OR pessoa.deficiencia = '7'  ) THEN '1'  WHEN (pessoa.deficiencia <> 'SC') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as surdocegueira, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'MU' OR pessoa.deficiencia = '8'  ) THEN '1' WHEN (pessoa.deficiencia <> 'MU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as multipla,  ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'ME' OR pessoa.deficiencia = '9'  ) THEN '1' WHEN (pessoa.deficiencia <> 'ME') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as mental , ");

		sqlStr.append(
				" ( select coalesce(max(case when fa.escolaridade = any('{PD,DR}') then 6 when fa.escolaridade = any('{MS}') then 5 when fa.escolaridade = any('{PO,EP}') then 4 when fa.escolaridade = any('{GR}') then 3 else 1 end), 1) from formacaoacademica fa where fa.pessoa = pessoa.codigo) escolaridade, ");
		sqlStr.append(
				" (select case formacaoacademica.escolaridade when 'DO' then '3' when 'PD' then '3' when 'ME' then '2' else '1' end as  posgraduacao from formacaoacademica ");
		sqlStr.append(" where formacaoacademica.pessoa = pessoa.codigo  ");
		sqlStr.append(
				" and formacaoacademica.escolaridade in ('DO', 'PD', 'PL', 'ME') order by posgraduacao desc limit 1)  as posgraduacao, ");

		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '4' END as situacaodocente, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '' END as docenteemexercicio31122010, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN  '' ELSE '' END as regimetrabalho,   ");
		sqlStr.append(" '0' AS docentesubstituto, ");
		sqlStr.append(" '0' as docentevisitante, ");
		sqlStr.append(" '' AS tipovinculodocentevisitante, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1'  ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN ''  ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN ''  END as atuacaododocentesequencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocentegraduacao, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentegraduacaodistancia, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '").append(ano).append("' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '0' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocenteposgraduacaopresencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true THEN '0' WHEN pessoa.ativo = false THEN '' END as posgraduacaodistancia,  ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentepesquisa, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocenteextensao, ");
		sqlStr.append("  CASE WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = true THEN '1' ");
		sqlStr.append("  WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = false THEN '0' ");
		sqlStr.append("  WHEN pessoa.ativo = false THEN '' END as atuacaodocentegestao, ");
		sqlStr.append("  '' AS bolsadepesquisa,  ");
		// if (tipoLayout.equals("TECNICO")) {
		// sqlStr.append(" (select distinct professortitulardisciplinaturma.turma from
		// professortitulardisciplinaturma ");
		// sqlStr.append(" where professortitulardisciplinaturma.professor =
		// pttd.professor ");
		// sqlStr.append(" order by turma limit 1) AS turma, ");
		// }
		sqlStr.append("  pessoa.codigo as codigoprofessor  ");
		sqlStr.append(" FROM  Funcionario  ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo  ");
		sqlStr.append(" left join filiacao on filiacao.aluno = pessoa.codigo and filiacao.tipo = 'MA' ");
		sqlStr.append(" left join pessoa as mae on filiacao.pais = mae.codigo ");
		sqlStr.append(" left join cidade on cidade.codigo = pessoa.cidade  ");
		sqlStr.append(" left join estado on estado.codigo = cidade.estado  ");

		sqlStr.append(" left join paiz on pessoa.nacionalidade = paiz.codigo ");
		sqlStr.append(" left join formacaoacademica on formacaoacademica.pessoa = pessoa.codigo ");
		// if (tipoLayout.equals("TECNICO")) {
		// sqlStr.append(" inner join professortitulardisciplinaturma pttd on
		// pttd.professor = pessoa.codigo ");
		// }
		sqlStr.append(" WHERE pessoa.professor ");
		sqlStr.append(" and pessoa.nacionalidade = 1");
		sqlStr.append(" and Funcionario.dataadmissao < '").append(Integer.parseInt(ano) + 1).append("/01/01' ");

		if (tipoLayout.equals(Censo.GRADUACAO) || tipoLayout.equals(Censo.GRADUACAO_TECNOLOGICA)) {
			sqlStr.append(" and EXISTS ( ");
			sqlStr.append(" select pessoaemailinstitucional.pessoa from salaaulablackboard "); 
			sqlStr.append(" inner join salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
			sqlStr.append(" inner join pessoaemailinstitucional on salaaulablackboardpessoa.pessoaemailinstitucional = pessoaemailinstitucional.codigo ");
			sqlStr.append(" where  salaaulablackboard.ano = '").append(ano).append("' and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = 'PROFESSOR' ");
			sqlStr.append(" AND pessoaemailinstitucional.pessoa = funcionario.pessoa LIMIT 1 ) ");
		} else {
			sqlStr.append(" and funcionario.pessoa in (SELECT distinct professor FROM professortitulardisciplinaturma ");
			sqlStr.append(" inner join turma on turma.codigo = professortitulardisciplinaturma.turma ");
			sqlStr.append("' where (professortitulardisciplinaturma.semestre = '").append(semestre)
					.append("' or professortitulardisciplinaturma.semestre = '') ");
			if (unidadeEnsino > 0) {
				sqlStr.append(" and  turma.unidadeensino =  ").append(unidadeEnsino);
			}
			sqlStr.append(" ");
			sqlStr.append(" )");
		}
		sqlStr.append(" ORDER BY nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		while (tabelaResultado.next()) {
			ProfessorCensoVO professorCenso = new ProfessorCensoVO();
			montarDadosCensoProfessor(professorCenso, tabelaResultado, usuario, ano, unidadeEnsino, tipoLayout, null);
			listaCursoCenso.add(professorCenso);
		}

		return listaCursoCenso;
	}

	@Deprecated
	public List<ProfessorCensoVO> consultarProfessorAnoSemestreTecnico(int unidadeEnsino,
			UsuarioVO usuario, CensoVO censo) throws Exception {

		List listaCursoCenso = new ArrayList<CursoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT  31 AS tiporegistro, ");
		sqlStr.append(" pessoa.codigo AS idprofessories,");
		sqlStr.append(" pessoa.nome, ");
		sqlStr.append(" replace(replace(cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(
				" CASE WHEN pessoa.nacionalidade = '1' THEN '' WHEN (pessoa.nacionalidade <> '1' AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" to_char(pessoa.datanasc,'DDMMYYYY') as datanascimento, ");
		sqlStr.append(" CASE WHEN sexo = 'M' THEN '1' ");
		sqlStr.append(" WHEN sexo = 'F' THEN '2' END as sexo, ");
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(
				" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' ) THEN '0' END as corraca, ");
		sqlStr.append(" pessoa.nomefiador as nomecompletomae, ");
		sqlStr.append(
				"  CASE WHEN pessoa.nacionalidade = 1 THEN 1  WHEN pessoa.nacionalidade <> 1 THEN 3 END as nacionalidade, ");
		sqlStr.append("  '' as ufdenascimento, ");
		sqlStr.append("  '' as municipiodenascimento, ");
		sqlStr.append(" CASE WHEN pessoa.nacionalidade = 1 THEN 'BRA' ");
		sqlStr.append(" WHEN pessoa.nacionalidade <> 1 THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'NE' OR pessoa.deficiencia = '' OR pessoa.deficiencia is null OR pessoa.deficiencia = '0' ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '1' ELSE '0' END as docentecomdeficiencia , ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'CE' OR pessoa.deficiencia = '2' ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'CE' ) AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0'  ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as cegueira, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'BV' OR pessoa.deficiencia = '3'  )  THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'BV') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as baixavisao, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SU' OR pessoa.deficiencia = '4'  ) THEN '1' WHEN (pessoa.deficiencia <> 'SU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as surdez, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'AU' OR pessoa.deficiencia = '5'  ) THEN '1' ");
		sqlStr.append(
				"   WHEN (pessoa.deficiencia <> 'AU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as auditiva, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'FI' OR pessoa.deficiencia = '6'  ) THEN '1' WHEN (pessoa.deficiencia <> 'FI') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as fisica, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SC' OR pessoa.deficiencia = '7'  ) THEN '1'  WHEN (pessoa.deficiencia <> 'SC') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as surdocegueira, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'MU' OR pessoa.deficiencia = '8'  ) THEN '1' WHEN (pessoa.deficiencia <> 'MU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as multipla,  ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'ME' OR pessoa.deficiencia = '9'  ) THEN '1' WHEN (pessoa.deficiencia <> 'ME') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as mental , ");
		sqlStr.append("'2' AS escolaridade, ");
		sqlStr.append("'0' AS posgraduacao, ");

		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '4' END as situacaodocente, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '' END as docenteemexercicio31122010, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN  '' ELSE '' END as regimetrabalho,   ");
		sqlStr.append(" '0' AS docentesubstituto, ");
		sqlStr.append(" '0' as docentevisitante, ");
		sqlStr.append(" '' AS tipovinculodocentevisitante, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1'  ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN ''  ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN ''  END as atuacaododocentesequencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocentegraduacao, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentegraduacaodistancia, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '0' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocenteposgraduacaopresencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true THEN '0' WHEN pessoa.ativo = false THEN '' END as posgraduacaodistancia,  ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentepesquisa, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocenteextensao, ");
		sqlStr.append("  CASE WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = true THEN '1' ");
		sqlStr.append("  WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = false THEN '0' ");
		sqlStr.append("  WHEN pessoa.ativo = false THEN '' END as atuacaodocentegestao, ");
		sqlStr.append("  '' AS bolsadepesquisa,  ");
		// if (tipoLayout.equals("TECNICO")) {
		// sqlStr.append(" (select distinct professortitulardisciplinaturma.turma from
		// professortitulardisciplinaturma ");
		// sqlStr.append(" where professortitulardisciplinaturma.professor =
		// pttd.professor ");
		// sqlStr.append(" order by turma limit 1) AS turma, ");
		// }
		sqlStr.append("  pessoa.codigo as codigoprofessor  ");
		sqlStr.append(" FROM  Funcionario  ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo  ");
		sqlStr.append(" left join paiz on pessoa.nacionalidade = paiz.codigo ");
		sqlStr.append(" left join formacaoacademica on formacaoacademica.pessoa = pessoa.codigo ");
		// if (tipoLayout.equals("TECNICO")) {
		// sqlStr.append(" inner join professortitulardisciplinaturma pttd on
		// pttd.professor = pessoa.codigo ");
		// }
		sqlStr.append(" WHERE pessoa.professor ");
		sqlStr.append(" and Funcionario.dataadmissao < '").append(Uteis.getAnoData(censo.getDataBase()))
				.append("/01/01' ");
		sqlStr.append(" and funcionario.pessoa in (SELECT distinct professor FROM professortitulardisciplinaturma ");
		sqlStr.append(" inner join turma on turma.codigo = professortitulardisciplinaturma.turma ");
		if (!censo.getListaTurmaCenso().isEmpty()) {
			sqlStr.append(" WHERE professortitulardisciplinaturma.turma in (");
			StringBuilder turmas = new StringBuilder();
			for (TurmaCensoVO turma : censo.getListaTurmaCenso()) {
				if (!turmas.toString().isEmpty()) {
					turmas.append(", ");
				}
				turmas.append(turma.getCodigo());
			}
			sqlStr.append(turmas).append(")");
		}
		if (unidadeEnsino > 0) {
			sqlStr.append(" and  turma.unidadeensino =  ").append(unidadeEnsino);
		}
		sqlStr.append(" and pessoa.nacionalidade = 1");
		sqlStr.append(" ");
		sqlStr.append(" )");
		sqlStr.append(" ORDER BY nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		while (tabelaResultado.next()) {
			ProfessorCensoVO professorCenso = new ProfessorCensoVO();
			montarDadosCensoProfessor(professorCenso, tabelaResultado, usuario, Uteis.getAno(censo.getDataBase()),
					unidadeEnsino, "TECNICO", censo);
			listaCursoCenso.add(professorCenso);
		}

		return listaCursoCenso;
	}

	private List<ProfessorCensoVO> consultarProfessorAnoUnidadeEnsinoEducacoaBasica(String ano,
			Integer unidadeEnsino, String tipoLayout, UsuarioVO usuario, CensoVO censo) {
		List listaCursoCenso = new ArrayList<CursoCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT  31 AS tiporegistro, ");
		sqlStr.append(" pessoa.codigo AS idprofessories,");
		sqlStr.append(" pessoa.nome, ");
		//sqlStr.append(" formacaoacademica.codigo as codigoFormacaoAcademica, ");
		sqlStr.append(" replace(replace(cpf, '.',''),'-','') as cpf, ");
		sqlStr.append(
				" CASE WHEN pessoa.nacionalidade = '1' THEN '' WHEN (pessoa.nacionalidade <> '1' AND pessoa.passaporte is not null) THEN cast(pessoa.passaporte as VARCHAR) ELSE '' END as documentoestrangeiro, ");
		sqlStr.append(" to_char(pessoa.datanasc,'DDMMYYYY') as datanascimento, ");
		sqlStr.append(" CASE WHEN sexo = 'M' THEN '1' ");
		sqlStr.append(" WHEN sexo = 'F' THEN '2' END as sexo, ");
		sqlStr.append(" CASE WHEN (pessoa.corraca = 'BR') THEN '1' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PR') THEN '2' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'PA') THEN '3' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'AM') THEN '4' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'IN') THEN '5' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(" WHEN (pessoa.corraca = 'NI') THEN '6' ");
		sqlStr.append(
				" WHEN (pessoa.corraca = '' OR pessoa.corraca is NULL OR pessoa.corraca = ' ' ) THEN '0' END as corraca, ");
		sqlStr.append(" pessoa.nomefiador as nomecompletomae, ");
		sqlStr.append(
				"  CASE WHEN pessoa.nacionalidade = 1 THEN 1  WHEN pessoa.nacionalidade <> 1 THEN 3 END as nacionalidade, ");
		sqlStr.append("  '' as ufdenascimento, ");
		sqlStr.append("  '' as municipiodenascimento, ");
		sqlStr.append(" CASE WHEN pessoa.nacionalidade = 1 THEN 'BRA' ");
		sqlStr.append(" WHEN pessoa.nacionalidade <> 1 THEN paiz.siglainep END as paisorigem, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'NE' OR pessoa.deficiencia = '' OR pessoa.deficiencia is null OR pessoa.deficiencia = '0' ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '1' ELSE '0' END as docentecomdeficiencia , ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'CE' OR pessoa.deficiencia = '2' ) THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'CE' ) AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0'  ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as cegueira, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'BV' OR pessoa.deficiencia = '3'  )  THEN '1' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia <> 'BV') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as baixavisao, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SU' OR pessoa.deficiencia = '4'  ) THEN '1' WHEN (pessoa.deficiencia <> 'SU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				"  WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as surdez, ");
		sqlStr.append("  CASE WHEN (pessoa.deficiencia = 'AU' OR pessoa.deficiencia = '5'  ) THEN '1' ");
		sqlStr.append(
				"   WHEN (pessoa.deficiencia <> 'AU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as auditiva, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'FI' OR pessoa.deficiencia = '6'  ) THEN '1' WHEN (pessoa.deficiencia <> 'FI') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END as fisica, ");
		sqlStr.append(
				"  CASE WHEN (pessoa.deficiencia = 'SC' OR pessoa.deficiencia = '7'  ) THEN '1'  WHEN (pessoa.deficiencia <> 'SC') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as surdocegueira, ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'MU' OR pessoa.deficiencia = '8'  ) THEN '1' WHEN (pessoa.deficiencia <> 'MU') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as multipla,  ");
		sqlStr.append(
				" CASE WHEN (pessoa.deficiencia = 'ME' OR pessoa.deficiencia = '9'  ) THEN '1' WHEN (pessoa.deficiencia <> 'ME') AND (pessoa.deficiencia <> 'NE' AND pessoa.deficiencia <> '' AND pessoa.deficiencia <> '0'  ) THEN '0' ");
		sqlStr.append(
				" WHEN (pessoa.deficiencia = 'NE' AND pessoa.deficiencia = '' AND pessoa.deficiencia = '0'  ) THEN '' END  as mental , ");
		sqlStr.append(" CASE WHEN formacaoacademica.escolaridade = 'PR' THEN '1' ");
		sqlStr.append(" WHEN formacaoacademica.escolaridade = 'BA' THEN '3' ");
		sqlStr.append("  WHEN formacaoacademica.escolaridade = 'SU'  THEN '6' END AS escolaridade, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '4' END as situacaodocente, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '1' ELSE '' END as docenteemexercicio31122010, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN  '' ELSE '' END as regimetrabalho,   ");
		sqlStr.append(" '0' AS docentesubstituto, ");
		sqlStr.append(" '0' as docentevisitante, ");
		sqlStr.append(" '' AS tipovinculodocentevisitante, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1'  ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SE' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN ''  ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN ''  END as atuacaododocentesequencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'SU' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocentegraduacao, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentegraduacaodistancia, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true  and  (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '1' ");
		sqlStr.append(
				" WHEN pessoa.ativo = true  and  false = (SELECT (count(curso.codigo) > 0) FROM public.professortitulardisciplinaturma inner join turma ON turma.codigo = professortitulardisciplinaturma.turma inner join curso on turma.curso = curso.codigo where curso.niveleducacional = 'PO' AND ano = '2010' and semestre = '2' and professortitulardisciplinaturma.professor = funcionario.pessoa) THEN '0' ");
		sqlStr.append(" WHEN pessoa.ativo = false THEN '' END as atuacaododocenteposgraduacaopresencial, ");
		sqlStr.append(
				" CASE WHEN pessoa.ativo = true THEN '0' WHEN pessoa.ativo = false THEN '' END as posgraduacaodistancia,  ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocentepesquisa, ");
		sqlStr.append(" CASE WHEN pessoa.ativo = true THEN '0' ELSE '' END as atuacaododocenteextensao, ");
		sqlStr.append("  CASE WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = true THEN '1' ");
		sqlStr.append("  WHEN pessoa.ativo = true and funcionario.exercecargoadministrativo = false THEN '0' ");
		sqlStr.append("  WHEN pessoa.ativo = false THEN '' END as atuacaodocentegestao, ");
		sqlStr.append("  '' AS bolsadepesquisa,  ");
		sqlStr.append("  pessoa.codigo as codigoprofessor  ");
		sqlStr.append(" FROM  Funcionario  ");
		sqlStr.append(" inner join pessoa on funcionario.pessoa = pessoa.codigo  ");
		sqlStr.append(" left join paiz on pessoa.nacionalidade = paiz.codigo ");
		sqlStr.append(" left join formacaoacademica on formacaoacademica.pessoa = pessoa.codigo ");
		sqlStr.append(" WHERE pessoa.professor ");
		sqlStr.append(" and Funcionario.dataadmissao < '").append(Integer.parseInt(ano) + 1).append("/01/01' ");
		sqlStr.append(" and funcionario.pessoa in (SELECT distinct professor FROM professortitulardisciplinaturma ");
		sqlStr.append(" inner join turma on turma.codigo = professortitulardisciplinaturma.turma ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" WHERE matriculaperiodo.ano <= '").append(ano).append("'");
		sqlStr.append(" and  turma.unidadeensino =  ").append(unidadeEnsino);
		sqlStr.append(" and pessoa.nacionalidade = 1");
		sqlStr.append(" and curso.niveleducacional  IN ('ME', 'BA', 'IN', 'PR') ");

		if (!censo.getListaTurmaCenso().isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" and turma.codigo in (");
			for (TurmaCensoVO turmaCensoVO : censo.getListaTurmaCenso()) {
				sqlStr.append(virgula ? ", " : "");
				sqlStr.append(turmaCensoVO.getCodigo());
				virgula = true;
			}
		sqlStr.append(" )");
			}

		//sqlStr.append(" ");
		sqlStr.append(" )");
		sqlStr.append(" ORDER BY nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		while (tabelaResultado.next()) {
			ProfessorCensoVO professorCenso = new ProfessorCensoVO();
			try {
				montarDadosCensoProfessor(professorCenso, tabelaResultado, usuario, ano, unidadeEnsino, tipoLayout,
						null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			listaCursoCenso.add(professorCenso);
		}

		return listaCursoCenso;
	}

	/***
	 * Método monta os dados do professor
	 * 
	 * @param alunoCenso
	 * @param dadosSQL
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public ProfessorCensoVO montarDadosCensoProfessor(ProfessorCensoVO professorCenso, SqlRowSet dadosSQL,
			UsuarioVO usuario, String ano, int unidadeEnsino, String tipoLayout, CensoVO censo) throws Exception {
		professorCenso.setTipoRegistro(dadosSQL.getString("tiporegistro"));
		professorCenso.setIdDocenteIES(dadosSQL.getString("idprofessories"));
		professorCenso.setNome(dadosSQL.getString("nome"));
		professorCenso.setCPF(dadosSQL.getString("cpf"));
		professorCenso.setDocumentoEstrangeiro(dadosSQL.getString("documentoestrangeiro"));
		professorCenso.setDataNascimento(dadosSQL.getString("datanascimento"));
		professorCenso.setSexo(dadosSQL.getString("sexo"));
		professorCenso.setCorRaca(dadosSQL.getString("corraca"));
		professorCenso.setNomeCompletoMae(dadosSQL.getString("nomecompletomae"));
		professorCenso.setNacionalidade(dadosSQL.getString("nacionalidade"));
		professorCenso.setPaisOrigem(dadosSQL.getString("paisorigem"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("ufdenascimento"))) {
			professorCenso.setUfDeNascimento(dadosSQL.getString("ufdenascimento"));
		} else {
			professorCenso.setUfDeNascimento("");
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("municipiodenascimento"))) {
			professorCenso.setMunicipioNascimento(dadosSQL.getString("municipiodenascimento"));
		} else {
			professorCenso.setMunicipioNascimento("");
		}
		professorCenso.setDocenteDeficiente(dadosSQL.getString("docentecomdeficiencia"));
		professorCenso.setCegueira(dadosSQL.getString("cegueira"));
		professorCenso.setBaixaVisao(dadosSQL.getString("baixavisao"));
		professorCenso.setSurdez(dadosSQL.getString("surdez"));
		professorCenso.setSurdocegueira(dadosSQL.getString("surdocegueira"));
		professorCenso.setMultipla(dadosSQL.getString("multipla"));
		professorCenso.setMental(dadosSQL.getString("mental"));
		professorCenso.setEscolaridade(dadosSQL.getString("escolaridade"));
		professorCenso.setSituacaoDocente(dadosSQL.getString("situacaodocente"));
		professorCenso.setDocenteEmExercicio31122010IES(dadosSQL.getString("docenteemexercicio31122010"));
		professorCenso.setRegimeTrabalho(dadosSQL.getString("regimetrabalho"));
		professorCenso.setDocenteSubstituto(dadosSQL.getString("docentesubstituto"));
		professorCenso.setDocenteVisitante(dadosSQL.getString("docentevisitante"));
		professorCenso.setTipoVinculoDocenteIES(dadosSQL.getString("tipovinculodocentevisitante"));
		professorCenso.setAtuacaoDoDocenteEnsinoSequencialFormacaoEspecifica(
				dadosSQL.getString("atuacaododocentesequencial"));
		professorCenso.setAtuacaoDoDocenteGraduacaoPresencial(dadosSQL.getString("atuacaododocentegraduacao"));
		professorCenso
				.setAtuacaoDoDocenteEnsinoGraduacaoADistancia(dadosSQL.getString("atuacaododocentegraduacaodistancia"));
		professorCenso.setAtuacaoDoDocenteEnsinoPosGraduacaoPresencial(
				dadosSQL.getString("atuacaododocenteposgraduacaopresencial"));
		professorCenso.setAtuacaoDoDocenteEnsinoPosGraduacaoADistancia(dadosSQL.getString("posgraduacaodistancia"));
		professorCenso.setAtuacaoDoDocentePesquisa(dadosSQL.getString("atuacaododocentepesquisa"));
		professorCenso.setAtuacaoDoDocenteExtensao(dadosSQL.getString("atuacaododocenteextensao"));
		professorCenso.setAtuacaoDoDocenteGestaoPlanejamentoEAvaliacao(dadosSQL.getString("atuacaodocentegestao"));
		professorCenso.setBolsaPesquisa(dadosSQL.getString("bolsadepesquisa"));
		professorCenso.setAtuacaoDoDocenteGestaoPlanejamentoEAvaliacao(dadosSQL.getString("atuacaodocentegestao"));
		professorCenso.getProfessor().setCodigo(dadosSQL.getInt("codigoprofessor"));
//		if (tipoLayout.equals("MEDIO")) {
//			professorCenso.setCodigoFormacaoAcademica(dadosSQL.getInt("codigoFormacaoAcademica"));
//		}
		if (tipoLayout.equals(Censo.GRADUACAO) || tipoLayout.equals(Censo.GRADUACAO_TECNOLOGICA)) {
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("posgraduacao"))) {
				professorCenso.setPosGraduacao(dadosSQL.getString("posgraduacao"));
			} else if (dadosSQL.getString("escolaridade").equals("2")) {
				professorCenso.setPosGraduacao("0");
			}
		}
		/*if (tipoLayout.equals("TECNICO")) {
			professorCenso.setListaTurmaCensoVOs(consultarTurmaCensoProfessorLeciona(
					professorCenso.getProfessor().getCodigo(), censo.getListaTurmaCenso(), unidadeEnsino, usuario));
		}*/
		if (tipoLayout.equals(Censo.EDUCACAO_BASICA_TECNICO)) {
			professorCenso.setListaTurmaCensoVOs(consultarTurmaCensoProfessorLecionaEducacaoBasica(
					professorCenso.getProfessor().getCodigo(), censo.getListaTurmaCenso(), unidadeEnsino, usuario));
		}
		return professorCenso;

	}

	@Deprecated
	public List<TurmaCensoVO> consultarTurmaCensoProfessorLeciona(Integer professor, List<TurmaCensoVO> turmaCenso,
			int unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct professortitulardisciplinaturma.turma from professortitulardisciplinaturma ");
		sb.append(" inner join turma on turma.codigo = professortitulardisciplinaturma.turma ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" where professor = ").append(professor);
		if (!turmaCenso.isEmpty()) {
			sb.append(" and turma.codigo in (");
			StringBuilder turmas = new StringBuilder();
			for (TurmaCensoVO turma : turmaCenso) {
				if (!turmas.toString().isEmpty()) {
					turmas.append(", ");
				}
				turmas.append(turma.getCodigo());
			}
			sb.append(turmas).append(")");
		}
		sb.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaCensoVO> listaTurmaCensoVOs = new ArrayList<TurmaCensoVO>(0);
		while (tabelaResultado.next()) {
			TurmaCensoVO obj = new TurmaCensoVO();
			obj.setCodigo(tabelaResultado.getInt("turma"));
			listaTurmaCensoVOs.add(obj);
		}
		return listaTurmaCensoVOs;
	}

	public List<TurmaCensoVO> consultarTurmaCensoProfessorLecionaEducacaoBasica(Integer professor, List<TurmaCensoVO> turmaCenso,
			int unidadeEnsino, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct professortitulardisciplinaturma.turma, ");
		sb.append(" curso.niveleducacional as niveleducacionalaluno, ");
		sb.append(" case ");
		sb.append(" when (curso.niveleducacional = 'ME') then ");
			sb.append(" case ");
			sb.append(" when (periodoletivo.periodoletivo = '1') then '25' ");
			sb.append(" when (periodoletivo.periodoletivo = '2') then '26' ");
			sb.append(" when (periodoletivo.periodoletivo = '3') then '27' ");
			sb.append(" when (periodoletivo.periodoletivo = '4') then '28' ");
			sb.append(" else '29' ");
			sb.append(" end ");
		sb.append(" when (curso.niveleducacional = 'BA') then ");
			sb.append(" case ");
			sb.append(" when (periodoletivo.periodoletivo = '1') then '14' ");
			sb.append(" when (periodoletivo.periodoletivo = '2') then '15' ");
			sb.append(" when (periodoletivo.periodoletivo = '3') then '16' ");
			sb.append(" when (periodoletivo.periodoletivo = '4') then '17' ");
			sb.append(" when (periodoletivo.periodoletivo = '5') then '18' ");
			sb.append(" when (periodoletivo.periodoletivo = '6') then '19' ");
			sb.append(" when (periodoletivo.periodoletivo = '7') then '20' ");
			sb.append(" when (periodoletivo.periodoletivo = '8') then '21' ");
			sb.append(" when (periodoletivo.periodoletivo = '9') then '22'");
			sb.append(" else '23' ");
			sb.append(" end ");
		sb.append(" when (curso.niveleducacional = 'IN') then ");
			sb.append(" case ");
			sb.append(" when (periodoletivo.periodoletivo = '1') then '1' ");
			sb.append(" when (periodoletivo.periodoletivo = '2') then '2' ");
			sb.append(" when (periodoletivo.periodoletivo = '3') then '3' ");
			sb.append(" else '3' ");
			sb.append(" end ");
			sb.append(" when (curso.niveleducacional = 'PR') then ");
				sb.append(" case ");
				sb.append(" when (periodoletivo.periodoletivo = '1') then '40' ");
				sb.append(" else '40' ");
			sb.append(" end ");
		sb.append(" end  as etapa ");	
		sb.append(" from professortitulardisciplinaturma ");
		sb.append(" inner join turma on turma.codigo = professortitulardisciplinaturma.turma ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" INNER JOIN curso ON curso.codigo = turma.curso ");
		sb.append(" INNER JOIN periodoletivo ON turma.periodoletivo = periodoletivo.codigo ");
		sb.append(" where professor = ").append(professor);
		if (!turmaCenso.isEmpty()) {
			sb.append(" and turma.codigo in (");
			StringBuilder turmas = new StringBuilder();
			for (TurmaCensoVO turma : turmaCenso) {
				if (!turmas.toString().isEmpty()) {
					turmas.append(", ");
				}
				turmas.append(turma.getCodigo());
			}
			sb.append(turmas).append(")");
		}
		sb.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaCensoVO> listaTurmaCensoVOs = new ArrayList<TurmaCensoVO>(0);
		while (tabelaResultado.next()) {
			TurmaCensoVO obj = new TurmaCensoVO();
			obj.setCodigo(tabelaResultado.getInt("turma"));
			obj.setEtapaEnsino(tabelaResultado.getString("etapa"));
			obj.setNivelEducacional(tabelaResultado.getString("niveleducacionalaluno"));
			listaTurmaCensoVOs.add(obj);
		}
		return listaTurmaCensoVOs;
	}
	
	/***
	 * Método responsável por buscar os cursos baseados no professor por ano e
	 * semestre
	 * 
	 * @param codigoProfessor
	 * @param ano
	 * @param semestre
	 * @return
	 * @throws Exception
	 */
	public List<CursoCensoProfessorVO> consultarCursosPorProfessorAnoSemestreCenso(int codigoProfessor,
			int codigoUnidadeEnsino, String ano, String semestre, UsuarioVO usuario) throws Exception {
		List<CursoCensoProfessorVO> listaCursoCensoProfessor = new ArrayList<CursoCensoProfessorVO>(0);
		StringBuilder sqlStr = new StringBuilder(0);
		sqlStr.append(" SELECT DISTINCT 32 as tiporegistro, curso.idcursoinep ");
		sqlStr.append(" FROM  professortitulardisciplinaturma  INNER JOIN turma ON turma.codigo = professortitulardisciplinaturma.turma LEFT JOIN curso ON turma.curso = curso.codigo ");
		sqlStr.append(" WHERE ");
		sqlStr.append(" case when ");
		sqlStr.append(" turma.semestral then (curso.niveleducacional in('SE', 'SU', 'GT') and ano = '").append(ano).append("' ) ");
		sqlStr.append(" else case when ");
		sqlStr.append(" turma.anual then (curso.niveleducacional in('IN', 'BA', 'ME') and ano = '").append(ano).append("' ) ");
		sqlStr.append(" else (curso.niveleducacional in('PO', 'MT'))");
		sqlStr.append(" end end  ");
		sqlStr.append(" AND professortitulardisciplinaturma.professor = ").append(codigoProfessor);
		sqlStr.append(" AND turma.unidadeensino = ").append(codigoUnidadeEnsino);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		while (tabelaResultado.next()) {
			CursoCensoProfessorVO cursoCensoProfessor = new CursoCensoProfessorVO();
			montarDadosCursoProfessorCenso(cursoCensoProfessor, tabelaResultado, usuario);
			listaCursoCensoProfessor.add(cursoCensoProfessor);
		}

		return listaCursoCensoProfessor;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static CursoCensoProfessorVO montarDadosCursoProfessorCenso(CursoCensoProfessorVO cursoCensoProfessor,
			SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {

		cursoCensoProfessor.setTipoRegistro(dadosSQL.getString("tiporegistro"));
		cursoCensoProfessor.setIdCurso(dadosSQL.getString("idcursoinep"));

		return cursoCensoProfessor;

	}

	public List<TurmaCensoVO> consultarTurmasPorUnidadeEnsino(UsuarioVO usuario, Integer codigoUnidadeEnsino,
			String ano, String semestre, Date dataBase, String layout) throws Exception {
		List<TurmaCensoVO> listaTurmaCenso = new ArrayList<TurmaCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder(0);
		sqlStr.append(
				" select codigoies,situacao, codigo, identificadorturma, horarioinicialhora, horarioinicialminuto, horariofinalhora,   ");
		sqlStr.append(
				" case when cast(horariofinalminuto as int) > 55 then '55' else horariofinalminuto end as horariofinalminuto   ");
		sqlStr.append(" from (   ");
		sqlStr.append(
				" select distinct unidadeensino.codigoies, turma.codigo, turma.identificadorturma, turma.situacao,  ");
		sqlStr.append(" substring(turnohorario.horarioinicioaula, 0, 3) as horarioinicialhora, ");
		sqlStr.append(" substring(turnohorario.horarioinicioaula, 4) as horarioinicialminuto,   ");
		sqlStr.append(" substring(th2.horariofinalaula, 0, 3)  as horariofinalhora,  ");
		sqlStr.append(" substring(th2.horariofinalaula, 4)  as horariofinalminuto, ");
		sqlStr.append(" turnohorario.horarioinicioaula as horarioinicio, ");
		sqlStr.append(" th2.horariofinalaula as  horariotermino ");
		sqlStr.append(" from turma  ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino   ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno   ");
		sqlStr.append(" inner join turnohorario on  turno.codigo = turnohorario.turno ");
		sqlStr.append(
				" and turnohorario.codigo = (select th.codigo from turnohorario th where turno.codigo = th.turno order by horarioinicioaula limit 1  ) ");
		sqlStr.append(" inner join turnohorario th2 on  turno.codigo = th2.turno ");
		sqlStr.append(
				" and th2.codigo = (select th.codigo from turnohorario th where turno.codigo = th.turno and turnohorario.diasemana = th.diasemana  order by horariofinalaula asc limit 1  ) ");
		sqlStr.append(" where unidadeensino.codigo = ").append(codigoUnidadeEnsino);
		sqlStr.append(" and turma.codigo in ( ");
//		sqlStr.append(" select distinct turma from matriculaperiodo  ");
//		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
//		sqlStr.append(" inner join curso on matricula.curso = curso.codigo ");
//		sqlStr.append(" where  ((ano ||'/'||semestre = '").append(ano).append("/").append(semestre).append("') or (ano ||'/'||semestre < '");
//		sqlStr.append(ano).append("/").append(semestre).append("' and matriculaperiodo.situacaomatriculaperiodo = 'TR'))");
//		sqlStr.append("  and curso.niveleducacional= 'PR' ");
		sqlStr.append(" select distinct  matriculaperiodo.turma  from matricula ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append(
				" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.data <= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("' order by data asc limit 1 )");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		if (layout.equals(Censo.GRADUACAO) || layout.equals(Censo.GRADUACAO_TECNOLOGICA)) {
			sqlStr.append(" where curso.niveleducacional in('SE', 'SU', 'GT')");
		}		
		else if (layout.equals("MEDIO")) {
			sqlStr.append(" where curso.niveleducacional = 'ME' ");
		} else if (layout.equals("BASICO")) {
			sqlStr.append(" where curso.niveleducacional = 'BA' ");
		} else if (layout.equals("INFANTIL")) {
			sqlStr.append(" where curso.niveleducacional = 'IN' ");
		}else {
			sqlStr.append(" where curso.niveleducacional = 'PR' ");
		}
		sqlStr.append(
				"and ((matriculaperiodo.situacaomatriculaperiodo IN  ('TR', 'CA', 'AB', 'TS', 'TI', 'FI') and datafechamentomatriculaperiodo is not null and datafechamentomatriculaperiodo >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("')");
		sqlStr.append(" or (matriculaperiodo.situacaomatriculaperiodo IN  ('AT'))");
		sqlStr.append(
				"or (matriculaperiodo.situacaomatriculaperiodo IN  ('FO') AND matricula.dataatualizacaomatriculaformada >= '");
		sqlStr.append(Uteis.getDataJDBC(dataBase));
		sqlStr.append("'))");
		sqlStr.append(")");
		sqlStr.append(" and turma.codigo in (");
		sqlStr.append(" SELECT distinct professortitulardisciplinaturma.turma FROM professortitulardisciplinaturma ");
		sqlStr.append(" inner join turma t on t.codigo = professortitulardisciplinaturma.turma ");
		sqlStr.append(" where t.unidadeensino =  ").append(codigoUnidadeEnsino);
		sqlStr.append(") ");
		sqlStr.append(" ) as t  ");
		sqlStr.append(" where  ");
		sqlStr.append(" horarioinicio < horariotermino   ");
		sqlStr.append(" order by codigo desc ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			TurmaCensoVO turmaCenso = new TurmaCensoVO();
			montarDadosCensoTurma(turmaCenso, tabelaResultado, usuario);
			listaTurmaCenso.add(turmaCenso);
		}
		return listaTurmaCenso;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static TurmaCensoVO montarDadosCensoTurma(TurmaCensoVO turmaCenso, SqlRowSet dadosSQL, UsuarioVO usuario)
			throws Exception {
		turmaCenso.setCodigoINEP(dadosSQL.getString("codigoies"));
		turmaCenso.setCodigo(dadosSQL.getInt("codigo"));
		turmaCenso.setNomeTurma(dadosSQL.getString("identificadorturma"));
		turmaCenso.setHorarioInicialHoraTurma(dadosSQL.getString("horarioinicialhora"));
		turmaCenso.setHorarioInicialMinutoTurma(dadosSQL.getString("horarioinicialminuto"));
		turmaCenso.setHorarioFinalHoraTurma(dadosSQL.getString("horariofinalhora"));
		turmaCenso.setHorarioFinalMinutoTurma(dadosSQL.getString("horariofinalminuto"));
		return turmaCenso;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static TurmaCensoVO montarDadosCensoTurmaEducacaoBasica(TurmaCensoVO turmaCenso, SqlRowSet dadosSQL, UsuarioVO usuario)
			throws Exception {
		turmaCenso.setCodigoINEP(dadosSQL.getString("codigoies"));
		turmaCenso.setCodigo(dadosSQL.getInt("codigo"));
		turmaCenso.setNomeTurma(dadosSQL.getString("identificadorturma"));
		turmaCenso.setHorarioInicialHoraTurma(dadosSQL.getString("horarioinicialhora"));
		turmaCenso.setHorarioInicialMinutoTurma(dadosSQL.getString("horarioinicialminuto"));
		turmaCenso.setHorarioFinalHoraTurma(dadosSQL.getString("horariofinalhora"));
		turmaCenso.setHorarioFinalMinutoTurma(dadosSQL.getString("horariofinalminuto"));
		turmaCenso.setEtapaEnsino(dadosSQL.getString("etapa"));
		turmaCenso.setNivelEducacional(dadosSQL.getString("niveleducacionalaluno"));
		turmaCenso.setCodigoCurso(dadosSQL.getInt("codigocursoinep"));
		return turmaCenso;
	}
		
	
	public List<TurmaCensoVO> consultarTurmasPorUnidadeEnsinoEducacaoBasica(UsuarioVO usuario, Integer codigoUnidadeEnsino,
			String ano, String semestre, Date dataBase, String layout, List<TurmaCensoVO> turmasCensoCodigo)throws Exception {
				
		List<TurmaCensoVO> listaTurmaCenso = new ArrayList<TurmaCensoVO>(0);
		StringBuilder sqlStr = new StringBuilder(0);
		
		sqlStr.append(" select ");
		sqlStr.append(" unidadeensino.codigoies, ");
		sqlStr.append(" turma.codigo, ");
		sqlStr.append(" turma.identificadorturma, ");
		sqlStr.append(" turma.situacao, ");
		sqlStr.append(" substring(turnohorario.horarioinicioaula, 0, 3) as horarioinicialhora, ");
		sqlStr.append(" substring(turnohorario.horarioinicioaula, 4) as horarioinicialminuto, ");
		sqlStr.append(" substring(th2.horariofinalaula, 0, 3) as horariofinalhora, ");
		sqlStr.append(" substring(th2.horariofinalaula, 4) as horariofinalminuto, ");
		sqlStr.append(" turnohorario.horarioinicioaula as horarioinicio, ");
		sqlStr.append(" th2.horariofinalaula as horariotermino, ");
		sqlStr.append(" curso.niveleducacional as niveleducacionalaluno, ");
		sqlStr.append(" curso.idcursoinep as codigocursoinep, ");
		sqlStr.append(" case ");
		sqlStr.append(" when (curso.niveleducacional = 'ME') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '25' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '26' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '27' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '4') then '28' ");
			sqlStr.append(" else '29' ");
			sqlStr.append(" end ");
		sqlStr.append(" when (curso.niveleducacional = 'BA') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '14' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '15' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '16' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '4') then '17' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '5') then '18' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '6') then '19' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '7') then '20' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '8') then '21' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '9') then '22'");
			sqlStr.append(" else '23' ");
			sqlStr.append(" end ");
		sqlStr.append(" when (curso.niveleducacional = 'IN') then ");
			sqlStr.append(" case ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '1' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '2') then '2' ");
			sqlStr.append(" when (periodoletivo.periodoletivo = '3') then '3' ");
			sqlStr.append(" else '3' ");
			sqlStr.append(" end ");
			sqlStr.append(" when (curso.niveleducacional = 'PR') then ");
				sqlStr.append(" case ");
				sqlStr.append(" when (periodoletivo.periodoletivo = '1') then '40' ");
				sqlStr.append(" else '40' ");
			sqlStr.append(" end ");
		sqlStr.append(" end  as etapa ");
		sqlStr.append(" from ");
		sqlStr.append(" turma ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sqlStr.append(" inner join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sqlStr.append(" inner join curso on turma.curso = curso.codigo ");
		sqlStr.append(" inner join turno on turno.codigo = turma.turno");
		sqlStr.append(" inner join turnohorario on turno.codigo = turnohorario.turno and turnohorario.codigo = ( ");
		sqlStr.append(" select th.codigo from turnohorario th where turno.codigo = th.turno order by horarioinicioaula limit 1 ) ");
		sqlStr.append(" inner join turnohorario th2 on turno.codigo = th2.turno and th2.codigo = ( ");
		sqlStr.append(" select th.codigo from turnohorario th where turno.codigo = th.turno and turnohorario.diasemana = th.diasemana order by horariofinalaula asc limit 1 ) ");
		sqlStr.append(" where unidadeensino.codigo = ").append(codigoUnidadeEnsino);
		sqlStr.append(" and curso.niveleducacional IN ('ME', 'BA', 'IN', 'PR') ");
		
		if (!turmasCensoCodigo.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" and turma.codigo in (");
			for (TurmaCensoVO turmaCensoVO : turmasCensoCodigo) {
				sqlStr.append(virgula ? ", " : "");
				sqlStr.append(turmaCensoVO.getCodigo());
				virgula = true;
			}
			sqlStr.append(") ");
		}				
		sqlStr.append(" order by ");
		sqlStr.append(" codigo desc");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			TurmaCensoVO turmaCenso = new TurmaCensoVO();
			montarDadosCensoTurmaEducacaoBasica(turmaCenso, tabelaResultado, usuario);
			listaTurmaCenso.add(turmaCenso);
		}
		return listaTurmaCenso;
		
	}
		
	public void consultarDataInicioPeriodoLetivoDataFimPeriodoLetivo(CensoVO censoVO, String ano) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" select min(periodoletivoativounidadeensinocurso.datainicioperiodoletivo) as datainicioperiodoletivo , max(periodoletivoativounidadeensinocurso.datafimperiodoletivo) as datafimperiodoletivo ");
		sb.append(" from processomatricula ");
		sb.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo ");
		sb.append(" inner join periodoletivoativounidadeensinocurso on processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo ");
		sb.append(" where processomatricula.nivelprocessomatricula in ('IN', 'BA', 'ME', 'PR') ");
		sb.append(" and unidadeensino = ").append(censoVO.getUnidadeEnsino().getCodigo());
		sb.append(" and ((processomatricula.nivelprocessomatricula in ('IN', 'BA', 'ME') and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo = '").append(ano).append("') ");
		sb.append(" or (processomatricula.nivelprocessomatricula = 'PR' and periodoletivoativounidadeensinocurso.datainicioperiodoletivo <= '").append(censoVO.getDataBase()).append("' ");
		sb.append(" and periodoletivoativounidadeensinocurso.datafimperiodoletivo >= '").append(censoVO.getDataBase()).append("'))");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			censoVO.setDataInicioPeriodoLetivoUnidadeEnsino(tabelaResultado.getDate("datainicioperiodoletivo"));
			censoVO.setDataFimPeriodoLetivoUnidadeEnsino(tabelaResultado.getDate("datafimperiodoletivo"));
		} else {
			throw new Exception(
					"Não foi encontrado a Data Início e a Data Fim do Período Letivo para a Unidade de Ensino informada.");
		}
	}

	public void executarGerarArquivoLayoutEducacaoBasica(CensoVO censo, String caminhoPasta,
			UsuarioVO usuario,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {

		validarDadosCensoEducacaoBasica(censo);

		String nomeArquivoLayoutEducacaoBasica = "layoutEducacaoBasicaTecnico_" + Uteis.getData(new Date(), "ddMMyyyyHHmmss" + "_"
				+ censo.getUnidadeEnsino().getCodigo() + "_" + censo.getAno() + "_" + censo.getSemestre()) + ".txt";
		try {
			executarCriarArquivoTextoLayoutEducacaoBasica(censo,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue(),
					nomeArquivoLayoutEducacaoBasica);
			censo.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(censo.getUnidadeEnsino().getCodigo(), usuario));			
			// Registro 00 - CADASTRO DE ESCOLA IDENTIFICAÇÃO REGISTRO 00
			executarCriarCabecalhoLayoutEducaoBasica(censo);
			// Registro 10 - CADASTRO DE ESCOLA CARACTERIZAÇÃO E INFRAESTRUTURA REGISTRO 10
			executarCriarTipoRegistroLayoutEducacaoBasica(censo, usuario);
			// Registro 20 - CADASTRO DE TURMA REGISTRO 20
			executarCriarRegistroTurmasLayoutEducacaoBasica(censo, usuario);
			// Registro 30 - CADASTRO DE PROFISSIONAL ESCOLAR EM SALA DE AULA -
			// IDENTIFICAÇÃO - REGISTRO 30
			executarCriarRegistroPessoaFisicaLayoutEducacaoBasica(censo, usuario);	
			// Registro 40
			executarGerarRegistroGestorEducacaoBasica(censo, usuario);
			// Registro 50 - VINCULO PROFESSOR TURMA - REGISTRO 50
			executarCriarVinculoProfessorTurmaEducacaoBasica(censo, usuario);
			// Registro 60 - IDENTIFICAÇÃO - REGISTRO 60
			executarCriarRegistroAlunoLayoutEducacaoBasica(censo, usuario);
			// Registro 99 - sinalizando que o mesmo foi encerrado - REGISTRO 99
			executarCriarRegistroFinalArquivoLayoutEducacaoBasica(censo);

			EditorOC editorOC = new EditorOC();
			censo.getPwLayoutEducacaoBasica().print(editorOC.getText());
			censo.getPwLayoutEducacaoBasica().close();

			if (!censo.getListaAlunoCenso().isEmpty()) {
				SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
				superParametroRelVO.setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				superParametroRelVO.setNomeDesignIreport(getDesignIReportRelatorio());
				superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				superParametroRelVO.setSubReport_Dir(getCaminhoBaseRelatorio());
				superParametroRelVO.setNomeEmpresa(censo.getUnidadeEnsino().getNome());
				superParametroRelVO.setTituloRelatorio("Censo Aluno - " + censo.getSemestre() + "/" + censo.getAno());
				superParametroRelVO.setListaObjetos(censo.getListaAlunoCenso());
				GeradorRelatorio.realizarExportacaoEXCEL(superParametroRelVO,
						caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue(),
						nomeArquivoLayoutEducacaoBasica.replaceAll(".txt", ".xlsx"));
				censo.getArquivoAlunoExcel().setNome(nomeArquivoLayoutEducacaoBasica.replaceAll(".txt", ".xlsx"));
				censo.getArquivoAlunoExcel().setOrigem(OrigemArquivo.CENSO_ALUNO.getValor());
				censo.getArquivoAlunoExcel().setCodOrigem(censo.getCodigo());
				censo.getArquivoAlunoExcel()
						.setPastaBaseArquivo(caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
				censo.getArquivoAlunoExcel().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
				getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAlunoExcel(), usuario,
						configuracaoGeralSistema);
			}

			censo.getArquivoAluno().setNome(nomeArquivoLayoutEducacaoBasica);
			censo.getArquivoAluno().setOrigem(OrigemArquivo.CENSO_LAYOUT_EDUCACAO_BASICA_TECNICO.getValor());
			censo.getArquivoAluno().setCodOrigem(censo.getCodigo());
			if (caminhoPasta.endsWith("'\'")) {
				censo.getArquivoAluno().setPastaBaseArquivo(caminhoPasta + PastaBaseArquivoEnum.CENSO.getValue());
				censo.getArquivoAluno().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
			} else {
				censo.getArquivoAluno()
						.setPastaBaseArquivo(caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
				censo.getArquivoAluno().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CENSO);
			}

			getFacadeFactory().getArquivoFacade().incluir(censo.getArquivoAluno(), usuario, configuracaoGeralSistema);
		} catch (Exception e) {
			ArquivoVO arqExcluir = new ArquivoVO();
			arqExcluir.setNome(nomeArquivoLayoutEducacaoBasica);
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
			arqExcluir.setNome(nomeArquivoLayoutEducacaoBasica.replace(".txt", ".xlsx"));
			getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arqExcluir,
					caminhoPasta + File.separator + PastaBaseArquivoEnum.CENSO.getValue());
			throw e;
		}
	}
	
	public static String getIdEntidade() {
		return Censo.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Censo.idEntidade = idEntidade;
	}

	public ArquivoHelper getArquivoHelper() {
		return arquivoHelper;
	}

	public void setArquivoHelper(ArquivoHelper arquivoHelper) {
		this.arquivoHelper = arquivoHelper;
	}


	private void vincularArquivosGeradosCenso(CensoVO censoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(censoVO)) {
			List<String> arquivos = new ArrayList<>();
			if (Uteis.isAtributoPreenchido(censoVO.getArquivoAluno())) {
				arquivos.add("arquivoaluno = " + censoVO.getArquivoAluno().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(censoVO.getArquivoAlunoExcel())) {
				arquivos.add("arquivoalunoexcel = " + censoVO.getArquivoAlunoExcel().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(censoVO.getArquivoLayoutTecnico())) {
				arquivos.add("arquivolayouttecnico = " + censoVO.getArquivoLayoutTecnico().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(censoVO.getArquivoProfessor())) {
				arquivos.add("arquivoprofessor = " + censoVO.getArquivoProfessor().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(arquivos)) {
				String sql = arquivos.stream()
						.collect(Collectors.joining(", ", "UPDATE censo SET ", " WHERE codigo = ?"));
				getConexao().getJdbcTemplate().update(sql, censoVO.getCodigo());
			}
		}
	}
	
	public void validarDadosCensoEducacaoBasica(CensoVO censoVO) throws Exception {
		
		if(!Uteis.isAtributoPreenchido(censoVO.getAno())) {
			throw new Exception("O campo ANO deve ser informado.");
		}
		
		if(!Uteis.isAtributoPreenchido(censoVO.getDataBase())) {
			throw new Exception("O campo DATA BASE deve ser informado.");
		}
		
		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino())) {
			throw new Exception("O campo Unidade de Ensino deve ser informado.");
		}
		
		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getDiretorGeral())) {
			throw new Exception("O campo Diretor Geral em Unidade de Ensino(Dados Básico) deve ser informado.");
		}
		
		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getCnpjMantenedora())) {
			throw new Exception("O campo CNPJ da IES Mantenedora em Unidade de Ensino(Dados Básico) deve ser informado.");
		}
		
		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getCodigoOrgaoRegionalEnsino())) {
			throw new Exception("O campo Código Orgão Regional Ensino em Unidade de Ensino(Dados Censo - Dados Básico) deve ser informado.");
		}

		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getCodigoDistritoCenso())) {
			throw new Exception("O campo Código Distrito Censo em Unidade de Ensino(Dados Censo - Dados Básico) deve ser informado.");
		}

		if(censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE") && !Uteis.isAtributoPreenchido( censoVO.getUnidadeEnsino().getFormaOcupacaoPredio())) {
			throw new Exception("O campo Forma de Ocupação do Prédio em Unidade de Ensino(Dados Censo - Dados Básico) deve ser informado.");
		}
		else if(censoVO.getUnidadeEnsino().getPredioCompartilhado()) {
			validarCodigoEscolaCompartilhada(censoVO);			
		}
		else if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getAbastecimentoAgua())) {
			throw new Exception("O campo Abastecimento de Água em Unidade de Ensino(Dados Censo - Dados Abastecimento) deve ser informado.");
		}
		else if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getAbastecimentoEnergia())) {
			throw new Exception("O campo Abastecimento de Energia em Unidade de Ensino(Dados Censo - Dados Abastecimento) deve ser informado.");
		}
		else if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getEsgotoSanitario())) {
			throw new Exception("O campo Esgoto Sanitário em Unidade de Ensino(Dados Censo - Dados Abastecimento) deve ser informado.");
		}
		else if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getDestinoLixo())) {
			throw new Exception("O campo Destinação do Lixo em Unidade de Ensino(Dados Censo - Dados Abastecimento) deve ser informado.");
		}
		else if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getTratamentoLixo())) {
			throw new Exception("O campo Tratamento do Lixo/Resíduos que a Escola Realiza em Unidade de Ensino(Dados Censo - Dados Abastecimento) deve ser informado.");
		}
		else if(checkSelecionadosDadosDependencia(censoVO)){
			throw new Exception("Ao menos uma opção em Unidade de Ensino(Dados Censo - Dados Dependências) deve ser selecionado.");
		}
		else if(checkSelecionadosDadosRecursosAcessibilidade(censoVO)) {
			throw new Exception("Ao menos uma opção em Unidade de Ensino(Dados Censo - Dados Recursos de Acessibilidade) deve ser selecionado.");
		}
		else if(censoVO.getUnidadeEnsino().getLocalFuncionamentoDaEscola().equals("PE") && !Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getNumeroSalasAulaUtilizadasEscolaDentroPredioEscolar()) && !Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getNumeroSalasDentroForaPredio())) {
			throw new Exception("O Número Salas de Aula Utilizadas na Escola dentro do Prédio Escolar ou o Número de salas utilizadas como Sala de Aula Dentro e Fora do Prédio  em Unidade de Ensino(Dados Censo - Dados Dependência) deve ser informado.");
		}
		else if(censoVO.getUnidadeEnsino().getEducacaoEscolarIndigena() && !censoVO.getUnidadeEnsino().getLinguaIndigena() && !censoVO.getUnidadeEnsino().getLinguaPortuguesa()) {
			throw new Exception("O campo Língua Indígena ou o campo Língua Portuguesa(Dados Censo - Dados Básicos) deve ser selecionado.");
		}
		else if(censoVO.getUnidadeEnsino().getLinguaIndigena() && !Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getCodigoLinguaIndigena1())) {
			throw new Exception("O campo Código da Língua Indígena 1(Dados Censo - Dados Básicos) deve ser preenchido.");
		}
		else if(checkSelecionadosDadosAcessoInternet(censoVO)) {
			throw new Exception("Ao menos uma opção em Unidade de Ensino(Dados Censo - Dados Acesso à Internet) deve ser selecionado.");
	}
		else if(checkSelecionadosDadosRedeLocalInterigacaoComputadores(censoVO)) {
			throw new Exception("Ao menos uma opção (Rede Local a Cabo, Rede Local Wireless, Não há Rede Local Interligando Computadores) em Unidade de Ensino(Dados Censo - Dados Acesso à Internet) deve ser selecionado.");
		}

		else if(!censoVO.getUnidadeEnsino().getBanheiroDentroPredio() && (censoVO.getUnidadeEnsino().getBanheiroDeficiencia() || censoVO.getUnidadeEnsino().getBanheiroEducacaoInfantil() || censoVO.getUnidadeEnsino().getBanheiroExclusivoFuncionarios() || censoVO.getUnidadeEnsino().getBanheiroChuveiro())) {
			throw new Exception("O campo Banheiro Dentro do Prédio em Unidade de Ensino(Dados Censo - Dados Dependências) deve ser selecionado.");
		}
	}

	private boolean checkSelecionadosDadosRecursosAcessibilidade(CensoVO censoVO) {
		return !censoVO.getUnidadeEnsino().getCorrimaoGuardaCorpos() && !censoVO.getUnidadeEnsino().getElevador() 
				&& !censoVO.getUnidadeEnsino().getPisosTateis() && !censoVO.getUnidadeEnsino().getPortasVaoLivreMinimoOitentaCentimetros()
				&& !censoVO.getUnidadeEnsino().getRampas() && !censoVO.getUnidadeEnsino().getSinalizacaoSonora() 
				&& !censoVO.getUnidadeEnsino().getSinalizacaoTatil() && !censoVO.getUnidadeEnsino().getSinalizacaoVisual() 
				&& !censoVO.getUnidadeEnsino().getNenhumRecursoAcessibilidade();
	}

	private boolean checkSelecionadosDadosAcessoInternet(CensoVO censoVO) {
		return !censoVO.getUnidadeEnsino().getNaoPossuiAcessoInternet() && !censoVO.getUnidadeEnsino().getAcessoInternetUsoAdiministrativo()
				&& !censoVO.getUnidadeEnsino().getAcessoInternetUsoProcessoEnsinoAprendizagem() && !censoVO.getUnidadeEnsino().getAcessoInternetUsoAlunos()
				&& !censoVO.getUnidadeEnsino().getAcessoInternetComunidade() && !censoVO.getUnidadeEnsino().getAcessoInternetComputadoresEscola()
				&& !censoVO.getUnidadeEnsino().getAcessoInternetDispositivosPessoais();
	}
	
	private boolean checkSelecionadosDadosRedeLocalInterigacaoComputadores(CensoVO censoVO) {
		return !censoVO.getUnidadeEnsino().getRedeLocalCabo() && !censoVO.getUnidadeEnsino().getRedeLocalWireless() && !censoVO.getUnidadeEnsino().getNaoExisteRedeLocal();
	}

	private void validarCodigoEscolaCompartilhada(CensoVO censoVO) throws Exception {
		String codigoEscolaCompartilhada = "";
		if(!Uteis.isAtributoPreenchido(censoVO.getUnidadeEnsino().getCodigoEscolaCompartilhada1())) {
			codigoEscolaCompartilhada = "1";
		}		
		if (Uteis.isAtributoPreenchido(codigoEscolaCompartilhada)) {
			throw new Exception("O campo Código Escola Compartilhada "+ codigoEscolaCompartilhada +" em Unidade de Ensino(Dados Censo - Dados Básico) deve ser informado.");
		}
	}

	private boolean checkSelecionadosDadosDependencia(CensoVO censoVO) {
		return !censoVO.getUnidadeEnsino().getAlmoxarifado() && !censoVO.getUnidadeEnsino().getAreaVerde() 
				&& !censoVO.getUnidadeEnsino().getAuditorio() && !censoVO.getUnidadeEnsino().getBanheiroDentroPredio() 
				&& !censoVO.getUnidadeEnsino().getBanheiroDeficiencia() && !censoVO.getUnidadeEnsino().getBanheiroEducacaoInfantil()
				&& !censoVO.getUnidadeEnsino().getBanheiroExclusivoFuncionarios() && !censoVO.getUnidadeEnsino().getBanheiroChuveiro() 
				&& !censoVO.getUnidadeEnsino().getBiblioteca() && !censoVO.getUnidadeEnsino().getCozinha() 
				&& !censoVO.getUnidadeEnsino().getDespensa() && !censoVO.getUnidadeEnsino().getAlojamentoAluno() 
				&& !censoVO.getUnidadeEnsino().getAlojamentoProfessor() && !censoVO.getUnidadeEnsino().getLaboratorioCiencias() 
				&& !censoVO.getUnidadeEnsino().getLaboratorioInformatica() && !censoVO.getUnidadeEnsino().getParqueInfantil() 
				&& !censoVO.getUnidadeEnsino().getPatioCoberto() && !censoVO.getUnidadeEnsino().getPatioDescoberto() 
				&& !censoVO.getUnidadeEnsino().getPiscina() && !censoVO.getUnidadeEnsino().getQuadraEsportesCoberta() 
				&& !censoVO.getUnidadeEnsino().getQuadraEsportesDescoberta() && !censoVO.getUnidadeEnsino().getRefeitorio() 
				&& !censoVO.getUnidadeEnsino().getSalaRepousoAluno() && !censoVO.getUnidadeEnsino().getSalaArtes() 
				&& !censoVO.getUnidadeEnsino().getSalaMusica() && !censoVO.getUnidadeEnsino().getSalaDanca()
				&& !censoVO.getUnidadeEnsino().getSalaMultiuso() && !censoVO.getUnidadeEnsino().getTerreirao() 
				&& !censoVO.getUnidadeEnsino().getViveiroAnimais() && !censoVO.getUnidadeEnsino().getSalaDiretoria() 
				&& !censoVO.getUnidadeEnsino().getSalaLeitura() && !censoVO.getUnidadeEnsino().getSalaProfessores() 
				&& !censoVO.getUnidadeEnsino().getRecursosMultifuncionais() && !censoVO.getUnidadeEnsino().getSalaSecretaria() 
				&& !censoVO.getUnidadeEnsino().getNenhumaDependencia();
	}

	
	public String getProgramaReservaVagas(String programaReservaVagaBase) {
	    String programaReservaVagas = "0||||||||||||||"; // Garante 15 colunas vazias
	    ReservasVagas reservasVagas = ReservasVagas.getEnum(programaReservaVagaBase);

	    if (reservasVagas == null) {
	        return programaReservaVagas;
	    }

	    switch (reservasVagas) {
	        case ETNICO:
	            programaReservaVagas = "1|2||1|0|0|0|0|0|0|0|0|0|0|0"; 
	            break;
	        case SOCIAL_RENDA_FAMILIAR:
	            programaReservaVagas = "1|2||0|1|0|0|0|0|0|0|0|0|0|0";
	            break;
	        case DEFICIENCIA:
	            programaReservaVagas = "1|2||0|0|1|0|0|0|0|0|0|0|0|0"; 
	            break;
	        case PROCEDENTE_ENSINO_PUBLICO:
	            programaReservaVagas = "1|2||0|0|0|1|0|0|0|0|0|0|0|0";
	            break;
	        case QUILOMBOLA:
	            programaReservaVagas = "1|2||0|0|0|0|1|0|0|0|0|0|0|0";
	            break;
	        case TRANSGENERO_TRAVESTI:
	            programaReservaVagas = "1|2||0|0|0|0|0|1|0|0|0|0|0|0";
	            break;
	        case ESTRANGEIRO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|1|0|0|0|0|0";
	            break;
	        case REFUGIADO_APATRIADO_VISTO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|1|0|0|0|0";
	            break;
	        case IDOSO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|1|0|0|0";
	            break;
	        case COMUNIDADE_TRADICIONAL:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|1|0|0";
	            break;
	        case COMPETICOES_CONHECIMENTO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|0|1|0";
	            break;
	        case OUTROS:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|0|0|1";
	            break;

	        default:
	            return programaReservaVagas;
	    }

	    return programaReservaVagas;
	}
	
	private void realizarValidacaoTransferenciaInternaMesmoCurso(List<AlunoCensoVO> alunoCensoVOs) {
		for (AlunoCensoVO alunoCensoVO : alunoCensoVOs) {
			CursoCensoVO cursoCensoVO = null;
			if (Uteis.isAtributoPreenchido(alunoCensoVO.getListaCursoCenso()) 
					&& (cursoCensoVO = alunoCensoVO.getListaCursoCenso().get(0)) != null
					&& cursoCensoVO.getSituacaoVinculo().equals(SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor())) {
				CursoCensoVO ultimoCursoCensoVO2 = alunoCensoVO.getListaCursoCenso().get(alunoCensoVO.getListaCursoCenso().size()-1);
				if (!ultimoCursoCensoVO2.getSituacaoVinculo().equals(SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor()) 
						&& cursoCensoVO.getIdCursoInep().equals(ultimoCursoCensoVO2.getIdCursoInep())
						&& alunoCensoVO.getListaCursoCenso().stream().collect(Collectors.groupingBy(CursoCensoVO::getIdCursoInep)).values().size() == 1) {
					ultimoCursoCensoVO2.setDataIngresso(cursoCensoVO.getDataIngresso());
					ultimoCursoCensoVO2.setOutrasFormasDeIngresso(cursoCensoVO.getOutrasFormasDeIngresso());
					ultimoCursoCensoVO2.setDataMatricula(cursoCensoVO.getDataMatricula());
					alunoCensoVO.setListaCursoCenso(new ArrayList<>());
					alunoCensoVO.getListaCursoCenso().add(ultimoCursoCensoVO2);
				}
			}
		}
	}
	
	private void realizarGeracaoSQLConsultarDeficiencia(StringBuilder sqlStr) {
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN 1 ELSE 0 END pessoacomdeficiencia, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.CEGUEIRA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END cegueira, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.BAIXA_VISAO.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END baixavisao, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SURDEZ.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END surdez, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.AUDITIVA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END auditiva, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.FISICA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END fisica, ");

		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SURDOCEGUEIRA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END surdocegueira, ");

		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.MENTAL.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END mental, ");

		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.TGDTEA.getSQLMatrizChaveConsulta()).append(" or  (pessoa.deficiencia = ANY ").append(TipoDeficiencia.TRANSTORNO_ESPECTRO_AUTISTA.getSQLMatrizChaveConsulta()).append(")   THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END autismo, ");

		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.SUPERDOTACAO.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END superdotacao, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.INTELECTUAL.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END Intelectual, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.TRANSTORNO_ESPECTRO_AUTISTA.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END transtornoEspectroAutista, ");
		
		sqlStr.append(" CASE WHEN pessoa.deficiencia = ANY ").append(TipoDeficiencia.VISAO_MONOCULAR.getSQLMatrizChaveConsulta()).append(" THEN '1' WHEN pessoa.deficiencia = ANY ");
		sqlStr.append(TipoDeficiencia.getSQLMatrizGeralChaveConsulta()).append(" THEN '0' ELSE '' END visaomonocular, ");
		
	}	
}