package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sun.faces.renderkit.html_basic.DoctypeRenderer;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.RegistroAulaConteudoVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.UnificadorPDF;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.DocumentoAssinado;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DiarioRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;


@SuppressWarnings("unchecked")
@Controller("DiarioRelControle")
@Scope("viewScope")
@Lazy
public class DiarioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<SelectItem> listaSelectItemProfessor;
	protected List<TurmaVO> listaConsultaTurma;
	protected Boolean existeUnidadeEnsino;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemTurma;
	protected TurmaVO turmaVO;
	protected Integer disciplina;
	protected String semestre;
	protected String ano;
	protected Integer professor;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	private String tipoLayout;
	private String tipoDiario;
	private String filtroTipoCursoAluno;
	private RegistroAulaConteudoVO registroAulaConteudoVO;
	private String tipoAluno;
	private String mes;
	private String anoMes;
	private Boolean possuiDiversidadeConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private Integer configuracaoAcademico;
	private Boolean apresentarAulasNaoRegistradas;
	private Boolean apresentarSituacaoMatricula;
	private Boolean trazerAlunoTransferencia;	
	private List<SelectItem> listaSelectItemProfessorDisciplinaTurmaVOs;
	private Boolean apresentarComboBoxProfessorTurmaDisciplina;
	private ProfessorTitularDisciplinaTurmaVO professorDisciplinaTurmaVO;
	private Map<Integer, ProfessorTitularDisciplinaTurmaVO> mapProfessorDisciplinaTurmaVOs = new HashMap<Integer, ProfessorTitularDisciplinaTurmaVO>(0);
	private String tipoFiltroPeriodo;
    private Date dataInicio;
    private Date dataFim;
	private Boolean apresentarVersoDiarioInformacoesPraticaSupervisionada;
	private Boolean apresentarVersoAgrupado;
	private Boolean apresentarCargaHorariaFormatoInteiro;
	private DataModelo listaDocumentosAssinados;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private boolean permitirGerarDiarioAssinado =false;
	private boolean apresentarModalExisteDiarioAssinado =true;
	private String validarModalDiarioAssinado;
	private Map<String, String> filtroNotaBimestre;
	private List<String> filtroNotaBimestreSelecionados;
	private Boolean marcarTodos;
	private Boolean permitirAlterarApresentarSituacaoMatricula;
	private Boolean permitirTrazerAlunosTransferenciaMatriz;
	private Boolean validarAulasRegistradas;
	private List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs;
	private String tituloRelatorio;
	private String observacoesRelatorio;

	
	public DiarioRelControle() throws Exception {
		setModuloLayoutEtiquetaEnum(ModuloLayoutEtiquetaEnum.CRONOGRAMA_AULA);
		verificarPermissaoGerarRelatorioDiarioAssinado();
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			verificarPermissaoTrazerAlunosTransferenciaMatriz();
		}
		inicializarListasSelectItemTodosComboBox();		
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void inicializarUnidadeEnsino() {
		try {
			getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				setExisteUnidadeEnsino(Boolean.TRUE);
			} else {
				setExisteUnidadeEnsino(Boolean.FALSE);
			}
		} catch (Exception e) {
			setExisteUnidadeEnsino(Boolean.FALSE);
		}
	}
	
	public void imprimirObjetoDiarioAssinadoExistente() {
		try {
			setApresentarModalExisteDiarioAssinado(false);
			imprimirObjetoDiarioPDF();
		} catch (Exception e) {
			setExisteUnidadeEnsino(Boolean.FALSE);
		}
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
		Integer codigoUnidadeEnsino = 0;
		if(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo())) {
			codigoUnidadeEnsino = getUnidadeEnsinoVO().getCodigo();
		}else if(Uteis.isAtributoPreenchido(getTurmaVO().getUnidadeEnsino().getCodigo())) {
			codigoUnidadeEnsino = getTurmaVO().getUnidadeEnsino().getCodigo();
		}
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) || !isAssinarDigitalmente()){
			setProvedorDeAssinaturaEnum(null);
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.DIARIO);
	}

	public void imprimirObjetoDiarioPDF() throws Exception {
		setValidarModalDiarioAssinado("");
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (getTurmaVO().getIdentificadorTurma().equals("")) {
				throw new Exception("O campo TURMA deve ser informado!");
			}
			if (getDisciplina().intValue() == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado!");
			}
			if (possuiDiversidadeConfiguracaoAcademico) {
				if (!Uteis.isAtributoPreenchido(configuracaoAcademico)) {
					throw new Exception(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_configuracaoAcademico"));
				}
			}
			if(isAssinarDigitalmente() && isApresentarModalExisteDiarioAssinado()){
				DisciplinaVO dis = new DisciplinaVO();
				dis.setCodigo(getDisciplina());
				if(getFacadeFactory().getDocumentoAssinadoFacade().consultarSeExisteDocumentoAssinado(getTurmaVO().getUnidadeEnsino(), getTurmaVO().getCurso(), getTurmaVO(), dis, getAno(), getSemestre(), null, TipoOrigemDocumentoAssinadoEnum.DIARIO, null, null, null, getUsuarioLogado().getPessoa(), getUsuarioLogado())){
					setValidarModalDiarioAssinado("RichFaces.$('panelMsgDiarioAssinado').show();");
					setFazerDownload(false);
					limparMensagem();
					return ;
				};	
			}
			p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), true, getUsuarioLogado());
			listaProfessores.add(p);

			if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				setFiltroTipoCursoAluno("posGraduacao");
				setTipoLayout("DiarioModRetratoRel");
			} else {
				setFiltroTipoCursoAluno("todos");
				// setTipoLayout("DiarioRel");
			}
			definirAnoFiltrar();
			List<DiarioRegistroAulaVO> listaRegistro = getFacadeFactory().getDiarioRelFacade().consultarRegistroAula(listaProfessores, getDisciplina(), getTurmaVO(), getSemestre(), 
					getAno(), getUsuarioLogado(), true, getTrazerAlunoPendenteFinanceiramente(), true, getFiltroTipoCursoAluno(), getTipoLayout(), getTipoDiario(), getTipoAluno(), 
					getMes(), getAnoMes(), getConfiguracaoAcademico(), getApresentarAulasNaoRegistradas(), getApresentarSituacaoMatricula(), getTrazerAlunoTransferencia(), 
					getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), getTipoFiltroPeriodo(), getDataInicio(), getDataFim(),
					getFiltroNotaBimestreSelecionados());							
			if (!listaRegistro.isEmpty()) {
				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(DiarioRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Diário da Turma");
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().adicionarParametro("listaFiltrosNotas", getFiltroNotaBimestreSelecionados().stream().map(b -> BimestreEnum.getEnum(b)).map(b -> b.getValorApresentar()).collect(Collectors.joining(" / ")));
				getSuperParametroRelVO().setSemestre(getSemestre());
				getSuperParametroRelVO().adicionarParametro("tituloRelatorioApresentar", getTituloRelatorio());
				getSuperParametroRelVO().adicionarParametro("observacoesRelatorioApresentar", getObservacoesRelatorio());
				if(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao())) {
					getSuperParametroRelVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getNomeCertificacao());
				}else {
					getSuperParametroRelVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getDescricao());
				}
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getDescricaoAbonoFalta())) {
					getSuperParametroRelVO().adicionarParametro("descricaoAbonoFalta", getConfiguracaoGeralPadraoSistema().getDescricaoAbonoFalta());						
				}
				if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getSiglaAbonoFalta())) {
					getSuperParametroRelVO().adicionarParametro("siglaAbonoFalta", getConfiguracaoGeralPadraoSistema().getSiglaAbonoFalta());						
				}
				if (getTurmaVO().getTurmaAgrupada() && !Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getNome()) && Uteis.isAtributoPreenchido(getTurmaVO().getAbreviaturaCurso())) {
					getTurmaVO().getCurso().setNome(getTurmaVO().getAbreviaturaCurso());
				}
				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				File fileAssinar = realizarGeracaoVersoDiarioUnificarComFrenteDiario(listaRegistro.get(0).getTurma().getCodigo().toString(), listaRegistro.get(0).getDisciplina().getCodigo().toString(), getAno(), getSemestre());
				
				if(isAssinarDigitalmente() && assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada() && getListaProfessorDisciplinaVOs().size() > 1) {
					setCaminhoRelatorio(getFacadeFactory().getDiarioRelFacade().executarAssinaturaDiarios(listaRegistro.get(0).getTurma(), listaRegistro.get(0).getDisciplina(), getAno(), getSemestre(), montarListaProfessores(getListaProfessorDisciplinaVOs()) , fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(listaRegistro.get(0).getTurma().getUnidadeEnsino().getCodigo()),getProvedorDeAssinaturaEnum(), getValidarAulasRegistradas(), getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), getUsuarioLogado()));
				}else if (isAssinarDigitalmente()) {
					List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>();
					listaProfessor.add(listaRegistro.get(0).getProfessor());
					setCaminhoRelatorio(getFacadeFactory().getDiarioRelFacade().executarAssinaturaDiarios(listaRegistro.get(0).getTurma(), listaRegistro.get(0).getDisciplina(), getAno(), getSemestre(), listaProfessor, fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(listaRegistro.get(0).getTurma().getUnidadeEnsino().getCodigo()), getProvedorDeAssinaturaEnum(),getValidarAulasRegistradas(), getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), getUsuarioLogado()));		
				} else {
					setCaminhoRelatorio(fileAssinar.getAbsolutePath().replaceAll("[\\\\]", "/"));
				}
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
				limparMensagem();
			} else {
				setMensagemDetalhada("msg_erro", "Não foram encontrados nenhum registro de aula com base nos filtros aplicados.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setCaminhoRelatorio("");
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());		
		}
	}
	
	 private boolean assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada() throws Exception {
	  	ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
	   	if(Uteis.isAtributoPreenchido(configGEDVO)) {
	   		return configGEDVO.getConfiguracaoGedDiarioVO().getAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada();
	   	}
	   	return false;
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		executarValidacaoSimulacaoVisaoCoordenador();
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(valor, "diario", "designDiario"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), "diario", "ano"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), "diario", "semestre"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarAulasNaoRegistradas().toString(), "diario", "apresentarAulasNaoRegistrada"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarSituacaoMatricula().toString(), "diario", "apresentarSituacaoMatricula"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerAlunoTransferencia().toString(), "diario", "trazerAlunoTransferencia"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoAluno(), "diario", "tipoAluno"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarVersoDiarioInformacoesPraticaSupervisionada().toString(), "diario", "apresentarVersoDiarioInformacoesPraticaSupervisionada"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarCargaHorariaFormatoInteiro().toString(), "diario", "apresentarCargaHorariaFormatoInteiro"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getApresentarVersoAgrupado().toString(), "diario", "apresentarVersoAgrupado"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTituloRelatorio(), "diario", "tituloRelatorio", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getObservacoesRelatorio(), "diario", "observacoesRelatorio", getUsuarioLogado());
		
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "diario"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());	
	}

	private void verificarLayoutPadrao() {
		try{
			Map<String, String> dadosPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"designDiario"+getTurmaVO().getCurso().getNivelEducacional(),"ano"+getTurmaVO().getCurso().getNivelEducacional(), "semestre"+getTurmaVO().getCurso().getNivelEducacional(), "apresentarAulasNaoRegistrada"+getTurmaVO().getCurso().getNivelEducacional(), "apresentarSituacaoMatricula"+getTurmaVO().getCurso().getNivelEducacional(),"tipoAluno"+getTurmaVO().getCurso().getNivelEducacional(), "trazerAlunoTransferencia"+getTurmaVO().getCurso().getNivelEducacional() , "apresentarVersoDiarioInformacoesPraticaSupervisionada"+getTurmaVO().getCurso().getNivelEducacional(),"apresentarCargaHorariaFormatoInteiro"+getTurmaVO().getCurso().getNivelEducacional(),"apresentarVersoAgrupado"+getTurmaVO().getCurso().getNivelEducacional(), "tituloRelatorio", "observacoesRelatorio"}, "diario");
			for(String key: dadosPadroes.keySet()){
			if(key.equals("designDiario"+getTurmaVO().getCurso().getNivelEducacional())){
				setTipoLayout(dadosPadroes.get(key));
			}else if(key.equals("ano"+getTurmaVO().getCurso().getNivelEducacional()) && getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				setAno(dadosPadroes.get(key));
			}else if(key.equals("semestre"+getTurmaVO().getCurso().getNivelEducacional()) && getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				setSemestre(dadosPadroes.get(key));
			}else if(key.equals("apresentarAulasNaoRegistrada"+getTurmaVO().getCurso().getNivelEducacional())){
				setApresentarAulasNaoRegistradas(Boolean.valueOf(dadosPadroes.get(key)));
			}else if(key.equals("apresentarSituacaoMatricula"+getTurmaVO().getCurso().getNivelEducacional())){
				setApresentarSituacaoMatricula(Boolean.valueOf(dadosPadroes.get(key)));				
			}else if(key.equals("trazerAlunoTransferencia"+getTurmaVO().getCurso().getNivelEducacional())){
				setTrazerAlunoTransferencia(Boolean.valueOf(dadosPadroes.get(key)));				
			}else if(key.equals("tipoAluno"+getTurmaVO().getCurso().getNivelEducacional())){
				setTipoAluno(dadosPadroes.get(key));				
			}else if(key.equals("apresentarVersoDiarioInformacoesPraticaSupervisionada"+getTurmaVO().getCurso().getNivelEducacional())){
				setApresentarVersoDiarioInformacoesPraticaSupervisionada(Boolean.valueOf(dadosPadroes.get(key)));				
			}else if(key.equals("apresentarCargaHorariaFormatoInteiro"+getTurmaVO().getCurso().getNivelEducacional())){
				setApresentarCargaHorariaFormatoInteiro(Boolean.valueOf(dadosPadroes.get(key)));				
			}else if(key.equals("apresentarVersoAgrupado"+getTurmaVO().getCurso().getNivelEducacional())){
				setApresentarVersoAgrupado(Boolean.valueOf(dadosPadroes.get(key)));				
			}else if(key.equals("tituloRelatorio")){
				setTituloRelatorio(dadosPadroes.get(key));				
			}else if(key.equals("observacoesRelatorio")){
				setObservacoesRelatorio(dadosPadroes.get(key));				
			}
			
			
		}		
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "diario"+getTurmaVO().getCurso().getNivelEducacional(), getUsuarioLogado());	
		dadosPadroes= null;
		}catch(Exception e){
			
		}
	}

	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			if (getTipoDiario().equals("normal") || getTipoDiario().equals("")) {
				itens.add(new SelectItem("DiarioModRetratoRel", "Layout 1 - Pós-Graduação"));
//				setTipoLayout("DiarioModRetratoRel");
			} else {
				itens.add(new SelectItem("DiarioReposicaoRel", "Layout 2 - Pós-Graduação"));
//				setTipoLayout("DiarioReposicaoRel");
			}
			itens.add(new SelectItem("DiarioRelFrequenciaNota", "Diário Frequência e Notas"));
			itens.add(new SelectItem("DiarioRelFrequencia", "Diário Frequência"));			
		} else {
			if(getTurmaVO().getCurso().getNivelEducacional().equals("MT")){
				itens.add(new SelectItem("DiarioModRetratoRel", "Layout 1 - Diário Frequência e Média Final"));
			}
			itens.add(new SelectItem("DiarioRelFrequenciaNota", "Diário Frequência e Notas"));
			itens.add(new SelectItem("DiarioRelFrequencia", "Diário Frequência"));
			itens.add(new SelectItem("DiarioRelNota", "Diário Notas"));
			itens.add(new SelectItem("DiarioMesMesRel", "Diário Frequência Agrupado Mês a Mês"));
			itens.add(new SelectItem("DiarioRelFrequenciaNota2", "Diário Frequência e Notas Modelo 2"));
			itens.add(new SelectItem("DiarioRelFrequenciaNota3", "Diário Frequência e Notas Modelo 3"));
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				itens.add(new SelectItem("LayoutImpressaoEtiqueta", "Impressão Etiqueta"));
			}
		}
		Collections.sort(itens, new SelectItemOrdemValor());
		return itens;
	}

	public List<SelectItem> getListaTipoRelPos() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		return itens;
	}

	private void definirAnoFiltrar() throws Exception {
		if (getMes().equals("")) {
			anoMes = "";
		}
		if (getTurmaVO().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN")) {
			setAnoMes(getAno());
		}
		if (getTurmaVO().getPeriodicidade().equals("IN") && !getMes().trim().isEmpty() && getAnoMes().trim().isEmpty()) {
			throw new Exception("O campo ANO deve ser informado.");
		}
	}

	public void imprimirObjetoDiarioPDFVisaoFuncionario() throws Exception {
		String titulo = "";
		if (getTipoLayout().equals("DiarioRelFrequenciaNota2")) {
			titulo = "Diário de Classe";			
		}else if(getTipoLayout().equals("DiarioRelFrequenciaNota3")){
			titulo = "DIÁRIO DE CLASSE";
		}
		else {
			titulo = "Diário da Turma";			
		}
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio PDF - Visao Funcionario", "Emitindo Relatorio");
			getFacadeFactory().getDiarioRelFacade().validarDadosRelatorio(getTurmaVO(), getSemestre(), getAno(), getDisciplina(), getUnidadeEnsinoVO(), getPossuiDiversidadeConfiguracaoAcademico(), getConfiguracaoAcademico());
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}						
			definirAnoFiltrar();
			final List<Integer> listaDisciplina = new ArrayList<Integer>();
			int contador = 1;
			while (contador < getListaSelectItemDisciplina().size()) {
				if (getDisciplina() == 0) {
					listaDisciplina.add(Integer.parseInt((((SelectItem)getListaSelectItemDisciplina().get(contador)).getValue()).toString()));
				} else {
					listaDisciplina.add(getDisciplina());
					break;
				}
				contador++;				
			}
			final UsuarioVO usuario = getUsuarioLogado();
			final Boolean trazerAlunoPendenteFinanceiramente = getTrazerAlunoPendenteFinanceiramente();
			final Boolean permitirProfessorRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			List<DiarioRegistroAulaVO> listaRegistro = new ArrayList<DiarioRegistroAulaVO>();
			final Map<Integer, List<DiarioRegistroAulaVO>> mapListaRegistro = new HashMap<Integer, List<DiarioRegistroAulaVO>>(0);
			final ConsistirException consistirException = new ConsistirException("");
			ProcessarParalelismo.executar(0, listaDisciplina.size(), consistirException, new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
					Integer disciplinaParam = 0;
					List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
					ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
					try {
						disciplinaParam = listaDisciplina.get(i);
						if (!getTurmaVO().getIdentificadorTurma().equals("")) {
							if (!getProfessorDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
								p = getMapProfessorDisciplinaTurmaVOs().get(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo());
							} else {
								p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), disciplinaParam, getAno(), getSemestre(), false, usuario);
							}
							if (p != null) {
								listaProfessores.add(p);
							}
						}
						if (!listaProfessores.isEmpty()) {
							List<DiarioRegistroAulaVO> listaDiarioRegistroAulaVOs = getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaVisaoAdministrativa(listaProfessores, 
									disciplinaParam, getTurmaVO(), getSemestre(), getAno(), usuario, false, trazerAlunoPendenteFinanceiramente, false, getFiltroTipoCursoAluno(), 
									getTipoLayout(), getTipoDiario(), getTipoAluno(), getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getConfiguracaoAcademico(), 
									getApresentarAulasNaoRegistradas(), getApresentarSituacaoMatricula(), getTrazerAlunoTransferencia(), 
									permitirProfessorRealizarLancamentoAlunosPreMatriculados, getTipoFiltroPeriodo(), getDataInicio(), getDataFim(), 
									getFiltroNotaBimestreSelecionados());
							if (!listaDiarioRegistroAulaVOs.isEmpty() && !mapListaRegistro.containsKey(disciplinaParam)) {
								mapListaRegistro.put(disciplinaParam, listaDiarioRegistroAulaVOs);
							}
						} else {
							throw new Exception("Não foi Encontrado um Professor Titular pra esta Turma.");
						}
					} catch (Exception e) {
						consistirException.adicionarListaMensagemErro(e.getMessage());
					}
				}
			});
			if(!consistirException.getListaMensagemErro().isEmpty()){
				setListaMensagemErro(consistirException.getListaMensagemErro());
				throw consistirException ;
			}
			if (!mapListaRegistro.isEmpty()) {
				Integer disciplinaAnt = getDisciplina();
				List<File> files = new ArrayList<File>(0);
				for (Integer disc : mapListaRegistro.keySet()) {				
					List<DiarioRegistroAulaVO> diarioRegistroAulaVOs = mapListaRegistro.get(disc);
					getSuperParametroRelVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorio(getTipoLayout()));
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
					getSuperParametroRelVO().setSubReport_Dir(DiarioRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
					getSuperParametroRelVO().setTituloRelatorio(titulo);
					getSuperParametroRelVO().setListaObjetos(diarioRegistroAulaVOs);
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
					getSuperParametroRelVO().setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorio());
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setQuantidade(diarioRegistroAulaVOs.size());
					getSuperParametroRelVO().setAno(getAno());
					getSuperParametroRelVO().setSemestre(getSemestre());
					getSuperParametroRelVO().adicionarParametro("tituloRelatorioApresentar", getTituloRelatorio());
					getSuperParametroRelVO().adicionarParametro("observacoesRelatorioApresentar", getObservacoesRelatorio());
					if(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao())) {
						getSuperParametroRelVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getNomeCertificacao());
					}else {
						getSuperParametroRelVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getDescricao());
					}				
					if (Uteis.isAtributoPreenchido(getTurmaVO().getUnidadeEnsino().getNomeExpedicaoDiploma())) {
						getSuperParametroRelVO().adicionarParametro("unidadeEnsinoExpedicaoDiploma",getTurmaVO().getUnidadeEnsino().getNomeExpedicaoDiploma());
					}
					getSuperParametroRelVO().setFiltros("");
					getSuperParametroRelVO().adicionarParametro("listaFiltrosNotas", getFiltroNotaBimestreSelecionados().stream().map(b -> BimestreEnum.getEnum(b)).map(b -> b.getValorApresentar()).collect(Collectors.joining(" / ")));
					if (getTipoLayout().equals("DiarioRelFrequenciaNota2")) {
						if (getTipoFiltroPeriodo().equals("porPeriodoData")) {
							getSuperParametroRelVO().adicionarParametro("periodoFiltro", UteisData.getDataFormatada(getDataInicio()) + " à " + UteisData.getDataFormatada(getDataFim()));
						} else if (getTipoFiltroPeriodo().equals("porMes")) {
							getSuperParametroRelVO().adicionarParametro("periodoFiltro", MesAnoEnum.getEnum(getMes()).getMes());						
						} else {
							getSuperParametroRelVO().adicionarParametro("periodoFiltro", "");
						}
					}
					if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
		                	setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		                	if (!getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio().equals("") && !getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio().equals("")) {
		                		getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
		                	} else {
		                		getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
		                	}
		                }
					if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getDescricaoAbonoFalta())) {
						getSuperParametroRelVO().adicionarParametro("descricaoAbonoFalta", getConfiguracaoGeralPadraoSistema().getDescricaoAbonoFalta());						
					}
					if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getSiglaAbonoFalta())) {
						getSuperParametroRelVO().adicionarParametro("siglaAbonoFalta", getConfiguracaoGeralPadraoSistema().getSiglaAbonoFalta());						
					}
					if (getTurmaVO().getTurmaAgrupada() && !Uteis.isAtributoPreenchido(getTurmaVO().getCurso().getNome()) && Uteis.isAtributoPreenchido(getTurmaVO().getAbreviaturaCurso())) {
						getTurmaVO().getCurso().setNome(getTurmaVO().getAbreviaturaCurso());
					}
					realizarImpressaoRelatorio();
					setDisciplina(disc);
					File fileAssinar = realizarGeracaoVersoDiarioUnificarComFrenteDiario(diarioRegistroAulaVOs.get(0).getTurma().getCodigo().toString(), diarioRegistroAulaVOs.get(0).getDisciplina().getCodigo().toString(), getAno(), getSemestre());
					if(isAssinarDigitalmente() && assinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada() && getListaProfessorDisciplinaVOs().size() > 1) {
						files.add(new File(getFacadeFactory().getDiarioRelFacade().executarAssinaturaDiarios(diarioRegistroAulaVOs.get(0).getTurma(), diarioRegistroAulaVOs.get(0).getDisciplina(), getAno(), getSemestre(), montarListaProfessores(getListaProfessorDisciplinaVOs()) , fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(diarioRegistroAulaVOs.get(0).getTurma().getUnidadeEnsino().getCodigo()), getProvedorDeAssinaturaEnum(),getValidarAulasRegistradas(), getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), getUsuarioLogado())));
					}else if(isAssinarDigitalmente()){
						List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>();
						listaProfessor.add(diarioRegistroAulaVOs.get(0).getProfessor());
						files.add(new File(getFacadeFactory().getDiarioRelFacade().executarAssinaturaDiarios(diarioRegistroAulaVOs.get(0).getTurma(), diarioRegistroAulaVOs.get(0).getDisciplina(), getAno(), getSemestre(),listaProfessor, fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(diarioRegistroAulaVOs.get(0).getTurma().getUnidadeEnsino().getCodigo()), getProvedorDeAssinaturaEnum(),getValidarAulasRegistradas(), getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), getUsuarioLogado())));	
					}else{
						files.add(fileAssinar);
					}					
				}
				if(files.size() > 1) {
					String nomeNovoArquivo = getTurmaVO().getCodigo()+"_"+getAno()+getSemestre()+"_"+getUsuarioLogado().getCodigo()+""+new Date().getTime();
					if(isAssinarDigitalmente()) {
						nomeNovoArquivo += ".zip";
						File[] filesArray = new File[files.size()];
						getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File( getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nomeNovoArquivo));
					}else {
						nomeNovoArquivo += ".pdf";
					  UnificadorPDF.realizarUnificacaoListaPdf(files, getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + nomeNovoArquivo);
					  for(File file : files) {
						  file.delete();
					  }
					  Uteis.liberarListaMemoria(files);
					}
					setCaminhoRelatorio(nomeNovoArquivo);					
				}
				persistirLayoutPadrao(getTipoLayout());
				setMensagemDetalhada("", "");
				verificarLayoutPadrao();
				limparMensagem();
				setDisciplina(disciplinaAnt);
			} else {
				setMensagemDetalhada("msg_erro", "Não foram encontrados nenhum registro de aula com base nos filtros aplicados.");
			}
//			persistirLayoutPadrao(getTipoLayout());
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio PDF - Visao Funcionario", "Emitindo Relatorio");
		} catch (ConsistirException e) {
			setCaminhoRelatorio("");
			setFazerDownload(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setCaminhoRelatorio("");
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// Uteis.liberarListaMemoria(listaRegistro);
			// Uteis.liberarListaMemoria(listaProfessores);
			// titulo = null;
			// removerObjetoMemoria(p);
		}
	}
	
	private List<PessoaVO> montarListaProfessores(List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs ){
		List<PessoaVO> listaProfessores = new ArrayList<PessoaVO>();
		for(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO : listaProfessorDisciplinaVOs) {
			listaProfessores.add(professorTitularDisciplinaTurmaVO.getProfessor().getPessoa());
		}
		return listaProfessores;
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
	}
	
	public void imprimirObjetoDiarioVersoPDF() throws Exception {
		imprimirObjetoDiarioVersoPDF(false);
	}

	public void imprimirObjetoDiarioVersoPDF(boolean imprimirJuntoFrente) throws Exception {
		List<RegistroAulaVO> listaRegistro = null;
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio PDF - Verso", "Emitindo Relatorio");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				if (!getProfessorDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
					p = getMapProfessorDisciplinaTurmaVOs().get(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo());
				} else {
					p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), false, getUsuarioLogado());
				}
				listaProfessores.add(p);
			} else {
				throw new Exception("O campo Identicador Turma deve ser informado!");
			}
			if (getDisciplina().intValue() == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado!");
			}
			if (possuiDiversidadeConfiguracaoAcademico) {
				if (!Uteis.isAtributoPreenchido(configuracaoAcademico)) {
					throw new Exception(UteisJSF.internacionalizar("msg_CalendarioLancamentoNota_configuracaoAcademico"));
				}
			}
			if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				setFiltroTipoCursoAluno("posGraduacao");
			} else {
				setFiltroTipoCursoAluno("todos");
			}
			definirAnoFiltrar();
			listaRegistro = getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaVerso(listaProfessores, getDisciplina(), getTurmaVO(), getSemestre(), getAno(), true, getTrazerAlunoPendenteFinanceiramente(), true, getFiltroTipoCursoAluno(), getTipoAluno(), getUsuarioLogado(), null, getMes(), getAnoMes(), getConfiguracaoAcademico(), getApresentarAulasNaoRegistradas(), getApresentarSituacaoMatricula(), getTipoLayout(), getTrazerAlunoTransferencia(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), getTipoFiltroPeriodo(), getDataInicio(), getDataFim() , getApresentarVersoAgrupado());

			if (!listaRegistro.isEmpty()) {

				setMensagemDetalhada("", "");
				Boolean dividirLista = Boolean.TRUE;
				if (getTipoLayout().equals("DiarioModRetratoRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoPos());
					dividirLista = Boolean.FALSE;
				} else if (getTipoLayout().equals("DiarioRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVerso());
				} else if (getTipoLayout().equals("DiarioReposicaoRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoPos());
					dividirLista = Boolean.FALSE;
				} else {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVerso());
				}

				preencherConteudoDiarioRelVerso(listaRegistro, getApresentarVersoDiarioInformacoesPraticaSupervisionada(), dividirLista , getApresentarCargaHorariaFormatoInteiro(), getTipoLayout());
				getRegistroAulaConteudoVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getRegistroAulaConteudoVO().setSubReport_Dir(DiarioRel.getCaminhoBaseRelatorioVerso());
				getRegistroAulaConteudoVO().setNomeUsuario(getUsuarioLogado().getNome());
				getRegistroAulaConteudoVO().setTituloRelatorio("Verso Diário da Turma");
				getRegistroAulaConteudoVO().setListaObjetos(listaRegistro);
				getRegistroAulaConteudoVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getRegistroAulaConteudoVO().setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorioVerso());
				getRegistroAulaConteudoVO().setVersaoSoftware(getVersaoSistema());
				getRegistroAulaConteudoVO().setQuantidade(listaRegistro.size());
				getRegistroAulaConteudoVO().adicionarParametro("apresentarVersoDiarioInformacoesPraticaSupervisionada", getApresentarVersoDiarioInformacoesPraticaSupervisionada());
				getRegistroAulaConteudoVO().adicionarParametro("apresentarVersoAgrupado", getApresentarVersoAgrupado());
				getRegistroAulaConteudoVO().adicionarParametro("apresentarCargaHorariaFormatoInteiro", getApresentarCargaHorariaFormatoInteiro());
				getRegistroAulaConteudoVO().adicionarParametro("tituloRelatorioApresentar", getTituloRelatorio());
				getRegistroAulaConteudoVO().adicionarParametro("observacoesRelatorioApresentar", getObservacoesRelatorio());
				if(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao())) {
					getRegistroAulaConteudoVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getNomeCertificacao());
				}else {
					getRegistroAulaConteudoVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getDescricao());
				}
				realizarImpressaoRelatorio(getRegistroAulaConteudoVO());
				if(!imprimirJuntoFrente) {
					removerObjetoMemoria(this);
					inicializarListasSelectItemTodosComboBox();
					verificarLayoutPadrao();
					limparMensagem();
				}
			} else {
				if(!imprimirJuntoFrente) {
				setMensagemDetalhada("msg_erro", "Não foram encontrados nenhum registro de aula com base nos filtros aplicados.");
			}
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio PDF - Verso", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRegistro);
			Uteis.liberarListaMemoria(listaProfessores);
			if(!imprimirJuntoFrente) {
				removerObjetoMemoria(p);
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				montarListaSelectItemTurmaVisaoCoordenador();
			} else {
				montarListaSelectItemTurmaVisaoProfessor();
			}
		}
	}
	}

	public void imprimirObjetoDiarioVersoPDFVisaoFuncionario() throws Exception {
		imprimirObjetoDiarioVersoPDFVisaoFuncionario(false);
	}
	
	public void imprimirObjetoDiarioVersoPDFVisaoFuncionario(boolean imprimirJuntoFrente) throws Exception {
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		ProfessorTitularDisciplinaTurmaVO p = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio PDF - Verso - Visao Funcionario", "Emitindo Relatorio");
			getFacadeFactory().getDiarioRelFacade().validarDadosRelatorio(getTurmaVO(), getSemestre(), getAno(), getDisciplina(), getUnidadeEnsinoVO(), getPossuiDiversidadeConfiguracaoAcademico(), getConfiguracaoAcademico());
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			
			definirAnoFiltrar();
			final List<Integer> listaDisciplina = new ArrayList<Integer>();
			int contador = 1;
			while (contador < getListaSelectItemDisciplina().size()) {
				if (getDisciplina() == 0) {
					listaDisciplina.add(Integer.parseInt((((SelectItem)getListaSelectItemDisciplina().get(contador)).getValue()).toString()));
				} else {
					listaDisciplina.add(getDisciplina());
					break;
				}
				contador++;
			}
			List<ProfessorTitularDisciplinaTurmaVO> listaProfessoresTitularVOs = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
			final UsuarioVO usuario = getUsuarioLogado();
			final Boolean trazerAlunoPendenteFinanceiramente = getTrazerAlunoPendenteFinanceiramente();
			final Boolean permitirProfessorRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			List<RegistroAulaVO> listaRegistro = new ArrayList<RegistroAulaVO>();			
			final Map<Integer, List<RegistroAulaVO>> mapListaRegistro = new HashMap<Integer, List<RegistroAulaVO>>(0);			
			final  ConsistirException consistirException = new ConsistirException("");
			ProcessarParalelismo.executar(0, listaDisciplina.size(), consistirException, new ProcessarParalelismo.Processo() {				
				@Override
				public void run(int i) {					
					Integer disciplinaParam = 0;	
					List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
					ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
					try {
						disciplinaParam = listaDisciplina.get(i);
						if (!getTurmaVO().getIdentificadorTurma().equals("")) {
							if (!getProfessorDisciplinaTurmaVO().getProfessor().getCodigo().equals(0)) {
								p = getMapProfessorDisciplinaTurmaVOs().get(getProfessorDisciplinaTurmaVO().getProfessor().getCodigo());
							} else {
								p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurmaVO(), disciplinaParam, getAno(), getSemestre(), false, usuario);
							}
							if( p != null){
								listaProfessores.add(p);
							}
						}			
						if (!listaProfessores.isEmpty()) {
							listaProfessoresTitularVOs.add(getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().executarObterDadosProfessorTitularDisciplinaTurma(listaProfessores));
							mapListaRegistro.put(disciplinaParam, getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaVerso(listaProfessores, disciplinaParam, getTurmaVO(), getSemestre(), getAno(), true, trazerAlunoPendenteFinanceiramente, false, getFiltroTipoCursoAluno(), getTipoAluno(), usuario, getFiltroRelatorioAcademicoVO(), getMes(), getAnoMes(), getConfiguracaoAcademico(), getApresentarAulasNaoRegistradas(), getApresentarSituacaoMatricula(), getTipoLayout(), getTrazerAlunoTransferencia(), permitirProfessorRealizarLancamentoAlunosPreMatriculados, getTipoFiltroPeriodo(), getDataInicio(), getDataFim() , getApresentarVersoAgrupado()));
						}

					} catch (Exception e) {
						consistirException.adicionarListaMensagemErro(e.getMessage());
					}
				}
			});
			Set<Integer> set = mapListaRegistro.keySet();
			for (Integer chave : set) {
				List<RegistroAulaVO> lista = (List)mapListaRegistro.get(chave);				
				listaRegistro.addAll(lista);
			}
			if(!consistirException.getListaMensagemErro().isEmpty()){
				throw new Exception(consistirException.getToStringMensagemErro());
			}
			if (!listaRegistro.isEmpty()) {
				Boolean dividirLista = Boolean.TRUE;
				setMensagemDetalhada("", "");
				if (getTipoLayout().equals("DiarioModRetratoRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoPos());
					dividirLista = Boolean.FALSE;
				} else if (getTipoLayout().equals("DiarioRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVerso());
				} else if (getTipoLayout().equals("DiarioReposicaoRel")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoPos());
					dividirLista = Boolean.FALSE;
				} else if (getTipoLayout().equals("DiarioRelFrequenciaNota2")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoModeloDois());
				} else if (getTipoLayout().equals("DiarioRelFrequenciaNota3")) {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVersoModeloTres());
				} else {
					getRegistroAulaConteudoVO().setNomeDesignIreport(DiarioRel.getDesignIReportRelatorioVerso());
				}
				preencherConteudoDiarioRelVerso(listaRegistro, getApresentarVersoDiarioInformacoesPraticaSupervisionada(), dividirLista , getApresentarCargaHorariaFormatoInteiro(), getTipoLayout());
				getRegistroAulaConteudoVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getRegistroAulaConteudoVO().setSubReport_Dir(DiarioRel.getCaminhoBaseRelatorioVerso());
				getRegistroAulaConteudoVO().setNomeUsuario(getUsuarioLogado().getNome());
				if (getTipoLayout().equals("DiarioRelFrequenciaNota2")) {
					getRegistroAulaConteudoVO().setTituloRelatorio("Conteúdos");
				} else {
					getRegistroAulaConteudoVO().setTituloRelatorio("Verso Diário da Turma");
				}
				getRegistroAulaConteudoVO().setListaObjetos(listaRegistro);
				getRegistroAulaConteudoVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getRegistroAulaConteudoVO().setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorioVerso());
				getRegistroAulaConteudoVO().setVersaoSoftware(getVersaoSistema());
				getRegistroAulaConteudoVO().setQuantidade(listaRegistro.size());
				getRegistroAulaConteudoVO().adicionarParametro("apresentarVersoDiarioInformacoesPraticaSupervisionada", getApresentarVersoDiarioInformacoesPraticaSupervisionada());
				getRegistroAulaConteudoVO().adicionarParametro("tituloRelatorioApresentar", getTituloRelatorio());
				getRegistroAulaConteudoVO().adicionarParametro("observacoesRelatorioApresentar", getObservacoesRelatorio());
				if(Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getNomeCertificacao())) {
					getRegistroAulaConteudoVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getNomeCertificacao());
				}else {
					getRegistroAulaConteudoVO().adicionarParametro("nomeCertificacao",getTurmaVO().getPeridoLetivo().getDescricao());
				}
				ConfiguracaoGEDVO configGEDVO = null;
				ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO = listaProfessoresTitularVOs.get(0);
				if (isAssinarDigitalmente()) {
					configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo())) {
						professorTitularDisciplinaTurmaVO.getProfessor().setArquivoAssinaturaVO(getFacadeFactory().getArquivoFacade().consultarAssinaturaDigitalFuncionarioPorCodigoFuncionario(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuario));
					} 
					if (Uteis.isAtributoPreenchido(professorTitularDisciplinaTurmaVO.getProfessor().getArquivoAssinaturaVO().getCodigo())) {
						getRegistroAulaConteudoVO().adicionarParametro("assinaturaDigitalFuncionario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + professorTitularDisciplinaTurmaVO.getProfessor().getArquivoAssinaturaVO().getPastaBaseArquivoEnum().getValue() + File.separator + professorTitularDisciplinaTurmaVO.getProfessor().getArquivoAssinaturaVO().getNome());
						getRegistroAulaConteudoVO().adicionarParametro("apresentarAssinaturaDigitalFuncionario", configGEDVO.getConfiguracaoGedDiarioVO().getApresentarAssinaturaDigitalizadoFuncionario());
					} else {
						getRegistroAulaConteudoVO().adicionarParametro("apresentarAssinaturaDigitalFuncionario", false);
					}
				} 
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		            if (!getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio().equals("") && !getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio().equals("")) {
		               	getRegistroAulaConteudoVO().adicionarParametro("logoPadraoRelatorio", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
		            } else {
		            	getRegistroAulaConteudoVO().adicionarParametro("logoPadraoRelatorio", getLogoPadraoRelatorio());
		            }
		        }
				realizarImpressaoRelatorio(getRegistroAulaConteudoVO());
				if(!imprimirJuntoFrente) {
				removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();
				limparMensagem();
				}
			} else {
				setMensagemDetalhada("msg_erro", "Não foram encontrados nenhum registro de aula com base nos filtros aplicados.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			//Uteis.liberarListaMemoria(listaRegistro);
			Uteis.liberarListaMemoria(listaProfessores);
			if(!imprimirJuntoFrente) {
			removerObjetoMemoria(p);
		}
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio PDF - Verso - Visao Funcionario", "Emitindo Relatorio");
	}
	

	public void preencherConteudoDiarioRelVerso(List<RegistroAulaVO> lista, Boolean apresentarVersoDiarioInformacoesPraticaSupervisionada, Boolean dividirLista ,Boolean apresentarCargaHorariaFormatoInteiro, String tipoLayout) {
		int index = 0;
		RegistroAulaVO regFinal = new RegistroAulaVO();
		if (apresentarVersoDiarioInformacoesPraticaSupervisionada || !dividirLista) {
			regFinal = lista.stream().findFirst().get();
			List<RegistroAulaVO> listaRegistros = lista.stream().filter(ravo-> {
				return Uteis.isAtributoPreenchido(ravo);
			}).collect(Collectors.toList());
			regFinal.getListaVerso1().addAll(listaRegistros);
			
			regFinal.getListaVerso1().forEach(ravo -> {
				validarCargaHorariaRegistroAula(apresentarCargaHorariaFormatoInteiro, ravo, ravo);
			});
		} else {
			for (RegistroAulaVO registroAulaVO : lista) {
				if (index == 0) {
					regFinal = registroAulaVO;
				}
				RegistroAulaVO obj = new RegistroAulaVO();
				if (registroAulaVO.getCodigo() != null && registroAulaVO.getCodigo() > 0) {
					validarCargaHorariaRegistroAula(apresentarCargaHorariaFormatoInteiro, registroAulaVO, obj);
				if (tipoLayout.equals("DiarioRelFrequenciaNota2")) {
						obj.setConteudo(registroAulaVO.getConteudo().trim());
					} else {
						obj.setConteudo(registroAulaVO.getConteudo());
					}
					obj.setData(registroAulaVO.getData());
				}
				if (registroAulaVO.getCodigo() != null && registroAulaVO.getCodigo() > 0) {
					if (index % 2 == 0) {
						regFinal.getListaVerso1().add(obj);
					} else {
						regFinal.getListaVerso2().add(obj);
					}
					index++;
				}
			}
		}
		lista.clear();
		lista.add(regFinal);
	}

	private void validarCargaHorariaRegistroAula(Boolean apresentarCargaHorariaFormatoInteiro, RegistroAulaVO registroAulaVO,
			RegistroAulaVO obj) {
		boolean valorVericacao = registroAulaVO.getCargaHorariaStr().contains(":") && registroAulaVO.getCargaHorariaStr().endsWith("00");
		
		if(registroAulaVO.getTurma().getTurno().getConsiderarHoraAulaSessentaMinutosGeracaoDiario()) {
			obj.setCargaHoraria(registroAulaVO.getCargaHoraria()/60);
		}else {
			obj.setCargaHoraria(registroAulaVO.getCargaHoraria());
		}
		
		if (apresentarCargaHorariaFormatoInteiro && valorVericacao) {						
			obj.setCargaHorariaStr(registroAulaVO.getCargaHorariaStr().substring(0 , registroAulaVO.getCargaHorariaStr().indexOf(":")));
		}else {
			obj.setCargaHorariaStr(registroAulaVO.getCargaHorariaStr());
			obj.setConteudo(registroAulaVO.getConteudo());
			obj.setData(registroAulaVO.getData());
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioPDF() throws Exception {
		// String nomeRelatorio = DiarioRel.getIdEntidade();
		List listaRegistro = null;
		ProfessorTitularDisciplinaTurmaVO p = null;
		String titulo = "Espelho do Diário da Turma";
		String design = DiarioRel.getDesignIReportRelatorioEspelhoDiario();
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio PDF - Espelho Diario", "Emitindo Relatorio");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			if (!getTurmaVO().getIdentificadorTurma().equals("") && getDisciplina() != null && !getDisciplina().equals(0)) {
				p = consultarProfessorTitularTurma();
				setProfessor(p.getProfessor().getCodigo());
			} else {
				throw new Exception("Os campos Identicador Turma e Disciplina devem ser informados!");
			}
			listaRegistro = getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaEspelho(getTurmaVO(), getSemestre(), getAno(), getProfessor(), getDisciplina(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (!listaRegistro.isEmpty()) {
				setMensagemDetalhada("", "");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(DiarioRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setListaObjetos(listaRegistro);
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(listaRegistro.size());
				getSuperParametroRelVO().adicionarParametro("tituloRelatorioApresentar", getTituloRelatorio());
				getSuperParametroRelVO().adicionarParametro("observacoesRelatorioApresentar", getObservacoesRelatorio());
//				persistirLayoutPadrao(getTipoLayout());
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				verificarLayoutPadrao();

			} else {
				setMensagemDetalhada("msg_erro", "Não foram encontrados nenhum registro de aula com base nos filtros aplicados.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio PDF - Espelho Diario", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRegistro);
			removerObjetoMemoria(p);
			titulo = null;
			design = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void montarListaSelectItemTurmaVisaoCoordenador() {
		List listaResultado = null;
		Iterator i = null;
		Map<Integer, String> hashTurmasAdicionadas = new HashMap<Integer, String>(0);
		try {
			listaResultado = consultarTurmaPorCoordenador();
			
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if (turma.getTurmaAgrupada()) {
					List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(turma.getCodigo(), false, getUsuarioLogado());
					for (TurmaVO turmaParticipaAgrupamento : listaTurmasAgrupadas) {
						if (!hashTurmasAdicionadas.containsKey(turmaParticipaAgrupamento.getCodigo())) {
							value = turmaParticipaAgrupamento.getIdentificadorTurma() + " - Curso " + turmaParticipaAgrupamento.getCurso().getNome() + " - Turno " + turmaParticipaAgrupamento.getTurno().getNome();
							getListaSelectItemTurma().add(new SelectItem(turmaParticipaAgrupamento.getCodigo(), value));
							hashTurmasAdicionadas.put(turmaParticipaAgrupamento.getCodigo(), turmaParticipaAgrupamento.getIdentificadorTurma());
						}
					}
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				} else {
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			hashTurmasAdicionadas = null;
			i = null;
		}

	}

	public List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	@SuppressWarnings("rawtypes")
	public void montarListaSelectItemTurmaVisaoProfessor() {
		List listaResultado = null;
		Iterator i = null;
		Map<Integer, String> hashTurmasAdicionadas = new HashMap<Integer, String>(0);
		try {
			listaResultado = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if (!getUsuarioLogado().getVisaoLogar().equals("professor")) {
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						if (turma.getTurmaAgrupada()) {
							value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
						} else {
							value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
						}
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				} else {
					if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
						List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(turma.getCodigo(), false, getUsuarioLogado());
						for (TurmaVO turmaParticipaAgrupamento : listaTurmasAgrupadas) {
							if (!hashTurmasAdicionadas.containsKey(turmaParticipaAgrupamento.getCodigo())) {
								value = turmaParticipaAgrupamento.getIdentificadorTurma() + " - Curso " + turmaParticipaAgrupamento.getCurso().getNome() + " - Turno " + turmaParticipaAgrupamento.getTurno().getNome();
								getListaSelectItemTurma().add(new SelectItem(turmaParticipaAgrupamento.getCodigo(), value));
								hashTurmasAdicionadas.put(turmaParticipaAgrupamento.getCodigo(), turmaParticipaAgrupamento.getIdentificadorTurma());
							}
						}
						if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
							value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
							getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
							hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
						}
					} else {
						if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
							if (turma.getTurmaAgrupada()) {
								value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
							} else {
								value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
							}							
							getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
							hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
						}
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			hashTurmasAdicionadas = null;
			i = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		// return
		// getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(),
		// getSemestre(), getAno(), "AT", 0,
		// getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
		// if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
		}
		// } else {
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		// }
	}

	public void consultarTurmaProfessor() throws Exception {
		try {
			getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
			montarListaSelectItemDisciplinaTurma();
			setProfessor(getUsuarioLogado().getPessoa().getCodigo());
			limparMensagem();
			montarFiltroPorNota();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setProfessor(0);
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinaTurma() {
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		try {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador() && Uteis.isAtributoPreenchido(getTurmaVO())) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				List<DisciplinaVO> resultado = getFacadeFactory().getDisciplinaFacade().consultaRapidaPorDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo(), false, getUsuarioLogado());
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
			} else {
				List<HorarioTurmaDisciplinaProgramadaVO> resultado = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigoDisciplina", "nomeDisciplina"));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}

	@SuppressWarnings("rawtypes")
	public void imprimirObjetoEspelhoDiarioHTML() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Iniciando Impressao Relatorio HTML", "Emitindo Relatorio");
			List listaRegistro = getFacadeFactory().getDiarioRelFacade().consultarRegistroAulaEspelho(getTurmaVO(), getSemestre(), getAno(), getProfessor(), getDisciplina(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			String nomeRelatorio = DiarioRel.getIdEntidade();
			String titulo = "Espelho Diário da Turma";
			String design = DiarioRel.getDesignIReportRelatorio(getTipoLayout());
			setMensagemDetalhada("", "");
			if (!listaRegistro.isEmpty()) {
				apresentarRelatorioObjetos(nomeRelatorio, titulo, "", "", "HTML", "/" + DiarioRel.getIdEntidade() + "/registros", design, getUsuarioLogado().getNome(), "", listaRegistro, DiarioRel.getCaminhoBaseRelatorio());
			} else {
				setMensagemDetalhada("Não existe nenhum aluno matriculado nesta TURMA neste período.");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "DiarioRelControle", "Finalizando Impressao Relatorio HTML", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparIdentificador() {
		setTurmaVO(null);
		getListaSelectItemDisciplina().clear();
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			if (getTurmaVO().getSubturma()) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if(getTurmaVO().getTurmaAgrupada()) {
				getTurmaVO().getCurso().setNivelEducacional(getFacadeFactory().getCursoFacade().consultarNivelEducacionalPorTurma(getTurmaVO().getCodigo()));				
			}
//			else if (getTurmaVO().getTurmaAgrupada()) {
//				throw new Exception("Não é possível emitir diário de uma turma agrupada! Selecione uma turma específica.");
//			}
			setProfessor(getUsuarioLogado().getPessoa().getCodigo());
			montarListaSelectItemDisciplinaTurma();
			verificarLayoutPadrao();
			montarFiltroPorNota();
			obj = null;
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		setTurmaVO(null);
	}

	public List<SelectItem> getListaSelectItemTipoAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("normal", "Alunos (Turma Origem)"));
		itens.add(new SelectItem("reposicao", "Alunos (Reposição/Inclusão)"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public Boolean getSolicitarSemestreAnoParaEmissaoDiario() {
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
			return false;
		} else {
			return true;
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				} else {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				}
				if (getTurmaVO().getSubturma()) {
					getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				} 
//				else if (getTurmaVO().getTurmaAgrupada()) {
//					throw new Exception("Não é possível emitir diário de uma turma agrupada! Selecione uma turma específica.");
//				}
			} else {
				throw new Exception("Informe a Turma.");
			}
			getListaSelectItemDisciplina().clear();
			setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
			montarListaSelectItemDisciplinaTurma();
			verificarLayoutPadrao();
			montarFiltroPorNota();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			getListaSelectItemDisciplina().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public ProfessorMinistrouAulaTurmaVO
	// consultarProfessorTitularTurma(Integer disciplina) throws Exception {
	// String semestrePrm = "";
	// String anoPrm = "";
	// if (this.getDiarioRel().getTurmaVO().getAnual()) {
	// anoPrm = getDiarioRel().getAno();
	// semestrePrm = "";
	// }
	// if (this.getDiarioRel().getTurmaVO().getSemestral()) {
	// semestrePrm = getDiarioRel().getSemestre();
	// anoPrm = getDiarioRel().getAno();
	// }
	// ProfessorMinistrouAulaTurmaVO p = new ProfessorMinistrouAulaTurmaVO();
	// p.setAno(anoPrm);
	// p.setSemestre(semestrePrm);
	// p.setTurma(getDiarioRel().getTurmaVO());
	// p.getDisciplina().setCodigo(disciplina);
	// p =
	// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// return p;
	// }
	public ProfessorTitularDisciplinaTurmaVO consultarProfessorTitularTurma() throws Exception {
		String semestrePrm = "";
		String anoPrm = "";
		if (getTurmaVO().getAnual()) {
			anoPrm = getAno();
			semestrePrm = "";
		}
		if (getTurmaVO().getSemestral()) {
			semestrePrm = getSemestre();
			anoPrm = getAno();
		}
		ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
		p.setAno(anoPrm);
		p.setSemestre(semestrePrm);
		p.setTurma(getTurmaVO());
		p.getDisciplina().setCodigo(getDisciplina());
		semestrePrm = null;
		anoPrm = null;
		// p =
		// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		p = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresTitularDisciplinaTurmaTitular(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return p;
	}

	// public ProfessorMinistrouAulaTurmaVO consultarProfessorTitularTurma()
	// throws Exception {
	// String semestrePrm = "";
	// String anoPrm = "";
	// if (this.getDiarioRel().getTurmaVO().getAnual()) {
	// anoPrm = getDiarioRel().getAno();
	// semestrePrm = "";
	// }
	// if (this.getDiarioRel().getTurmaVO().getSemestral()) {
	// semestrePrm = getDiarioRel().getSemestre();
	// anoPrm = getDiarioRel().getAno();
	// }
	// ProfessorMinistrouAulaTurmaVO p = new ProfessorMinistrouAulaTurmaVO();
	// p.setAno(anoPrm);
	// p.setSemestre(semestrePrm);
	// p.setTurma(getDiarioRel().getTurmaVO());
	// p.getDisciplina().setCodigo(getDiarioRel().getDisciplina());
	// p =
	// getFacadeFactory().getProfessorMinistrouAulaTurmaFacade().montarProfessoresMinistrouAulaTurmaTitular(p,
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// return p;
	// }
	//

	// public void montarListaSelectItemProfessor() {
	// try {
	// setMensagemDetalhada("", "");
	// setListaSelectItemProfessor(new ArrayList(0));
	// Iterator i = getListaProfessor().iterator();
	// while (i.hasNext()) {
	// PessoaVO professor = (PessoaVO) i.next();
	// getListaSelectItemProfessor().add(new SelectItem(professor.getCodigo(),
	// professor.getNome()));
	// }
	// setMensagemID("msg_dados_consultados");
	// } catch (Exception e) {
	// // System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarFiltroPorNota();
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			boolean campoEmBranco = false;
			if (!Uteis.isAtributoPreenchido(super.getUnidadeEnsinoLogado())) {
				campoEmBranco = true;
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", campoEmBranco));
			verificarLayoutPadrao();
		} catch (Exception e) {
			throw e;
		}
	}

	public void limparDados() {
		setTurmaVO(null);
		setSemestre(null);
		setAno(null);
		setDisciplina(null);
		setProfessor(null);
	}

	public void limparDadosMemoria() {
		removerObjetoMemoria(this);
		verificarPermissaoGerarRelatorioDiarioAssinado();
		inicializarListasSelectItemTodosComboBox();
		executarInicializacaoFiltroRelatorioAcademicoDefault();
	}

	public void limparDadosMemoriaVisaoProfessor() {
		removerObjetoMemoria(this);
		verificarPermissaoGerarRelatorioDiarioAssinado();
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemTurmaVisaoProfessor();
	}

	public void limparDadosMemoriaVisaoCoordenador() {
		removerObjetoMemoria(this);
		verificarPermissaoGerarRelatorioDiarioAssinado();
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemTurmaVisaoCoordenador();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	public void realizarRegistroAssinaturaDocumento() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo()+File.separator+getDocumentoAssinadoVO().getArquivo().getNome());
			if(arquivo.exists()) {
				getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarDocumento(getDocumentoAssinadoVO(), arquivo, getConfiguracaoGeralPadraoSistema(), getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), true, getUsuarioLogado());
				realizarConsultaDocumentosAssinados();
			}else {
				throw new Exception("O arquivo "+getDocumentoAssinadoVO().getArquivo().getNome()+" não existe mais no diretório ("+(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+getDocumentoAssinadoVO().getArquivo().getPastaBaseArquivo())+").");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarDocumentoAssinado() {		
		try {
			setDocumentoAssinadoVO((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void visualizarDocumentosAssinados() {
		try {
			getListaDocumentosAssinados().setOffset(0);
			getListaDocumentosAssinados().setPage(0);
			getListaDocumentosAssinados().setPaginaAtual(0);
			getListaDocumentosAssinados().setLimitePorPagina(10);
			realizarConsultaDocumentosAssinados();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarConsultaDocumentosAssinados() throws Exception {
		DisciplinaVO disc = new DisciplinaVO();
		disc.setCodigo(getDisciplina());
		getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosAssinados(), getTurmaVO().getUnidadeEnsino(), getTurmaVO().getCurso(), getTurmaVO(), disc, getAno(), getSemestre(), null, TipoOrigemDocumentoAssinadoEnum.DIARIO, null, null, null, getUsuarioLogado().getPessoa(), getUsuarioLogado(), getListaDocumentosAssinados().getLimitePorPagina(), getListaDocumentosAssinados().getOffset());
	}
	
	public void paginarDocumentosAssinados(DataScrollEvent DataScrollEvent) {
		try {
			getListaDocumentosAssinados().setPaginaAtual(DataScrollEvent.getPage());
			getListaDocumentosAssinados().setPage(DataScrollEvent.getPage());
			realizarConsultaDocumentosAssinados();	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public String getUrlDonloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public void verificarPermissaoGerarRelatorioDiarioAssinado() {
		try {
			DiarioRel.verificarPermissaoUsuarioFuncionalidade("PermitirGerarRelatorioDiarioAssinado", getUsuarioLogado());
			setPermitirGerarDiarioAssinado(true);
		} catch (Exception ex) {
			setPermitirGerarDiarioAssinado(false);
		}
	}
	
	
	
	public DataModelo getListaDocumentosAssinados() {
		if(listaDocumentosAssinados == null) {
			listaDocumentosAssinados =  new DataModelo();
		}
		return listaDocumentosAssinados;
	}

	public void setListaDocumentosAssinados(DataModelo listaDocumentosAssinados) {
		this.listaDocumentosAssinados = listaDocumentosAssinados;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if(documentoAssinadoVO == null) {
			documentoAssinadoVO =  new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}

	public boolean isPermitirGerarDiarioAssinado() {
		return permitirGerarDiarioAssinado;
	}

	public void setPermitirGerarDiarioAssinado(boolean permitirGerarDiarioAssinado) {
		this.permitirGerarDiarioAssinado = permitirGerarDiarioAssinado;
	}

	public boolean isApresentarModalExisteDiarioAssinado() {
		return apresentarModalExisteDiarioAssinado;
	}

	public void setApresentarModalExisteDiarioAssinado(boolean apresentarModalExisteDiarioAssinado) {
		this.apresentarModalExisteDiarioAssinado = apresentarModalExisteDiarioAssinado;
	}
	
	

	public String getValidarModalDiarioAssinado() {
		if (validarModalDiarioAssinado == null) {
			validarModalDiarioAssinado = "";
		}
		return validarModalDiarioAssinado;
	}

	public void setValidarModalDiarioAssinado(String validarModalDiarioAssinado) {
		this.validarModalDiarioAssinado = validarModalDiarioAssinado;
	}

	public List<SelectItem> getTipoFiltroComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("posGraduacao", "Pós-Graduação"));
		itens.add(new SelectItem("extensao", "Extensão"));
		itens.add(new SelectItem("modular", "Especial"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public Boolean getExisteUnidadeEnsino() {
		if (existeUnidadeEnsino == null) {
			existeUnidadeEnsino = false;
		}
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioDiarioRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if ((getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getPeriodicidade().equals("AN")) || (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getPeriodicidade().equals("SE"))) {
						setAno(getAno());
						return true;
					} else {
						setAno("");
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if ((getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getPeriodicidade().equals("AN")) || (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getPeriodicidade().equals("SE"))) {
						setAno(getAno());
					} else {
						setAno("");
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirGerarRelatorioDiarioRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getPeriodicidade().equals("SE")) {
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (!getTurmaVO().getPeriodicidade().equals("SE")) {
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public Integer getProfessor() {
		if (professor == null) {
			professor = 0;
		}
		return professor;
	}

	public void setProfessor(Integer professor) {
		this.professor = professor;
	}

	public String getSemestre() {
		if (semestre == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		
		return turmaVO;
		
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public String getFiltroTipoCursoAluno() {
		if (filtroTipoCursoAluno == null) {
			filtroTipoCursoAluno = "";
		}
		return filtroTipoCursoAluno;
	}

	public void setFiltroTipoCursoAluno(String filtroTipoCursoAluno) {
		this.filtroTipoCursoAluno = filtroTipoCursoAluno;
	}

	public void setarTipoLayout() {
		if (getTipoDiario().equals("reposicao")) {
			setFiltroTipoCursoAluno("todos");
		}
	}

	public boolean getIsApresentarFiltro() {
		if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao() && getTipoDiario().equals("normal")) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarDadosAposSelecionarTurma() {
		return getTurmaVO().getCodigo() != 0;
	}

	public RegistroAulaConteudoVO getRegistroAulaConteudoVO() {
		if (registroAulaConteudoVO == null) {
			registroAulaConteudoVO = new RegistroAulaConteudoVO();
		}
		return registroAulaConteudoVO;
	}

	public void setRegistroAulaConteudoVO(RegistroAulaConteudoVO registroAulaConteudoVO) {
		this.registroAulaConteudoVO = registroAulaConteudoVO;
	}

	/**
	 * @return the tipoDiario
	 */
	public String getTipoDiario() {
		if (tipoDiario == null) {
			tipoDiario = "";
		}
		return tipoDiario;
	}

	/**
	 * @param tipoDiario
	 *            the tipoDiario to set
	 */
	public void setTipoDiario(String tipoDiario) {
		this.tipoDiario = tipoDiario;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public List<SelectItem> getListaSelectItemMes() throws Exception {
		List<SelectItem> listaSelectItemMes = new ArrayList<SelectItem>(0);
		listaSelectItemMes.add(new SelectItem("", ""));
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurmaPrincipalSubturma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		}
				
		if ((getTurmaVO().getCurso().getPeriodicidade().equals("AN") || getTurmaVO().getPeriodicidade().equals("AN")) || (getTurmaVO().getCurso().getPeriodicidade().equals("IN") || getTurmaVO().getPeriodicidade().equals("IN"))) {
			for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
				listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
			}
		}
		if (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getPeriodicidade().equals("SE")) {
			for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
				if (Integer.valueOf(mesAnoEnum.getKey()) >= 1 && Integer.valueOf(mesAnoEnum.getKey()) <= 7 && getSemestre().equals("1")) {
					listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
				}
				if (Integer.valueOf(mesAnoEnum.getKey()) >= 7 && Integer.valueOf(mesAnoEnum.getKey()) <= 12 && getSemestre().equals("2")) {
					listaSelectItemMes.add(new SelectItem(mesAnoEnum.getKey(), mesAnoEnum.getMes()));
				}
			}
		}
		return listaSelectItemMes;
	}

	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnoMes() {
		if (anoMes == null) {
			anoMes = "";
		}
		return anoMes;
	}

	public void setAnoMes(String anoMes) {
		this.anoMes = anoMes;
	}

	@Override
	public String getDownload() {
		String url = super.getDownload();
//		if (!url.isEmpty()) {
//			url += ";RichFaces.$('panelOkDiario').show()";
//		}
		return url;
	}

	public boolean getIsApresentarCampoAno() {
		return getTurmaVO().getAnual() || getTurmaVO().getSemestral();
	}

	public boolean getIsApresentarCampoSemestre() {
		return getTurmaVO().getSemestral();
	}

	/**
	 * @return the possuiDiversidadeConfiguracaoAcademico
	 */
	public Boolean getPossuiDiversidadeConfiguracaoAcademico() {
		if (possuiDiversidadeConfiguracaoAcademico == null) {
			possuiDiversidadeConfiguracaoAcademico = false;
		}
		return possuiDiversidadeConfiguracaoAcademico;
	}

	/**
	 * @param possuiDiversidadeConfiguracaoAcademico
	 *            the possuiDiversidadeConfiguracaoAcademico to set
	 */
	public void setPossuiDiversidadeConfiguracaoAcademico(Boolean possuiDiversidadeConfiguracaoAcademico) {
		this.possuiDiversidadeConfiguracaoAcademico = possuiDiversidadeConfiguracaoAcademico;
	}

	public void executarVerificacaoPossuiDiversidadeConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarExistenciaMaisDeUmaConfiguracaoAcademicoHistoricoDiario(getTurmaVO(), getSemestre(), getAno(), getDisciplina(), getFiltroTipoCursoAluno(), getTipoAluno(), getTipoLayout(), true, getTrazerAlunoPendenteFinanceiramente(), true, getTrazerAlunoTransferencia(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), false, getUsuarioLogado());
			if (configuracaoAcademicoVOs.size() > 1) {
				setPossuiDiversidadeConfiguracaoAcademico(true);
				setListaSelectItemConfiguracaoAcademico(UtilSelectItem.getListaSelectItem(configuracaoAcademicoVOs, "codigo", "nome"));
			} else {
				setPossuiDiversidadeConfiguracaoAcademico(false);
			}
			consultarProfessorDisciplinaTurma();
		} catch (Exception e) {
			setPossuiDiversidadeConfiguracaoAcademico(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaSelectItemConfiguracaoAcademico
	 */
	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	/**
	 * @param listaSelectItemConfiguracaoAcademico
	 *            the listaSelectItemConfiguracaoAcademico to set
	 */
	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	/**
	 * @return the configuracaoAcademico
	 */
	public Integer getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = 0;
		}
		return configuracaoAcademico;
	}

	/**
	 * @param configuracaoAcademico
	 *            the configuracaoAcademico to set
	 */
	public void setConfiguracaoAcademico(Integer configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}
	
	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}
	
	@PostConstruct
	public void executarInicializacaoFiltroRelatorioAcademicoDefault() {
		setFiltroRelatorioAcademicoVO(new FiltroRelatorioAcademicoVO());
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		verificarLayoutPadrao();
		if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurmaVisaoProfessor();
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaSelectItemTurmaVisaoCoordenador();
		}
	}

	public Boolean getApresentarAulasNaoRegistradas() {
		if (apresentarAulasNaoRegistradas == null) {
			apresentarAulasNaoRegistradas = false;
		}
		return apresentarAulasNaoRegistradas;
	}

	public void setApresentarAulasNaoRegistradas(Boolean apresentarAulasNaoRegistradas) {
		this.apresentarAulasNaoRegistradas = apresentarAulasNaoRegistradas;
	}

	public boolean getApresentarLayoutEtiqueta(){
    	return getTipoLayout().equals("LayoutImpressaoEtiqueta");
    }
	
	public void realizarImpressaoEtiqueta(){		
		try{
			setFazerDownload(false);
			setCaminhoRelatorio(getFacadeFactory().getDiarioRelFacade().realizarImpressaoEtiqueta(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), 
					getLayoutEtiquetaVO(), getTipoLayout(), getNumeroCopias(), getLinha(), getColuna(), getRemoverEspacoTAGVazia(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);
			removerObjetoMemoria(this);
			inicializarListasSelectItemTodosComboBox();
			verificarLayoutPadrao();
			limparMensagem();
			setMensagemID("msg_relatorio_ok");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	public Boolean getApresentarSituacaoMatricula() {
		if (apresentarSituacaoMatricula == null) {
			apresentarSituacaoMatricula = Boolean.FALSE;
		}
		return apresentarSituacaoMatricula;
	}

	public void setApresentarSituacaoMatricula(Boolean apresentarSituacaoMatricula) {
		this.apresentarSituacaoMatricula = apresentarSituacaoMatricula;
	}

	public Boolean getTrazerAlunoTransferencia() {
		if (trazerAlunoTransferencia == null) {
			trazerAlunoTransferencia = Boolean.FALSE;
		}
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(Boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}
	
	private File realizarGeracaoVersoDiarioUnificarComFrenteDiario(String turma, String disciplina, String ano, String semestre) throws Exception {
		String caminhoFrente = getCaminhoRelatorio();
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			imprimirObjetoDiarioVersoPDFVisaoFuncionario(true);
		}else {
			imprimirObjetoDiarioVersoPDF(true);
		}		
		String caminhoVerso = getCaminhoRelatorio();
		disciplina =  Uteis.removerCaracteresEspeciais3(Uteis.removerAcentuacao(disciplina)).replaceAll(" ", "_").replaceAll("/", "_").replace("\\", "_");
		turma =  Uteis.removerCaracteresEspeciais3(Uteis.removerAcentuacao(turma)).replaceAll(" ", "_").replaceAll("/", "_").replace("\\", "_");
		String nomeNovoArquivo = turma+"_"+disciplina+"_"+""+ano+semestre+"_"+getUsuarioLogado().getCodigo()+""+ (new Date().getTime()) + ".pdf";
		String caminhoBasePdf = getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator; 
		String caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		UnificadorPDF.realizarUnificacaoPdf(caminhoBasePdf + caminhoFrente, caminhoBasePdf + caminhoVerso, caminhoPdf);
		File frente = new File(caminhoBasePdf + caminhoFrente);
		frente.delete();
		frente =  null;
		File verso = new File(caminhoBasePdf + caminhoVerso);
		verso.delete();
		verso =  null; 
		setCaminhoRelatorio(nomeNovoArquivo);			
		return new File(caminhoPdf);
	}

	public List<SelectItem> getListaSelectItemProfessorDisciplinaTurmaVOs() {
		if (listaSelectItemProfessorDisciplinaTurmaVOs == null) {
			listaSelectItemProfessorDisciplinaTurmaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessorDisciplinaTurmaVOs;
	}

	public void setListaSelectItemProfessorDisciplinaTurmaVOs(List<SelectItem> listaSelectItemProfessorDisciplinaTurmaVOs) {
		this.listaSelectItemProfessorDisciplinaTurmaVOs = listaSelectItemProfessorDisciplinaTurmaVOs;
	}
	
	public List<ProfessorTitularDisciplinaTurmaVO> consultarProfessorDisciplinaTurma() {
		List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs = new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0);
		if (!getDisciplina().equals(0)) {
			try {
				listaProfessorDisciplinaVOs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarProfessoresDisciplinaTurma(getTurmaVO(), getDisciplina(), getAno(), getSemestre(), false, getTurmaVO().getCurso().getLiberarRegistroAulaEntrePeriodo(), "", Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				montarListaSelectItemProfessorTitularDisciplinaTurma(listaProfessorDisciplinaVOs);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			setApresentarComboBoxProfessorTurmaDisciplina(false);
		}
		return listaProfessorDisciplinaVOs;
	}
	
	public void montarListaSelectItemProfessorTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs) {
		getMapProfessorDisciplinaTurmaVOs().clear();
		if (listaProfessorDisciplinaVOs.size() > 1) {
			List<SelectItem> itens = new ArrayList<SelectItem>(0); 
			setApresentarComboBoxProfessorTurmaDisciplina(true);
			for (ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO : listaProfessorDisciplinaVOs) {
				if (!getMapProfessorDisciplinaTurmaVOs().containsKey(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo())) {
					professorTitularDisciplinaTurmaVO.setIgnorarTitularidadeProfessor(true);
					getMapProfessorDisciplinaTurmaVOs().put(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo(), professorTitularDisciplinaTurmaVO);
				}
				itens.add(new SelectItem(professorTitularDisciplinaTurmaVO.getProfessor().getCodigo(), professorTitularDisciplinaTurmaVO.getProfessor().getPessoa().getNome()));
			}
			setListaSelectItemProfessorDisciplinaTurmaVOs(itens);
		} else {
			setApresentarComboBoxProfessorTurmaDisciplina(false);
		}
	}
	
	public ProfessorTitularDisciplinaTurmaVO getProfessorDisciplinaTurmaVO() {
		if (professorDisciplinaTurmaVO == null) {
			professorDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
		}
		return professorDisciplinaTurmaVO;
	}

	public void setProfessorDisciplinaTurmaVO(ProfessorTitularDisciplinaTurmaVO professorDisciplinaTurmaVO) {
		this.professorDisciplinaTurmaVO = professorDisciplinaTurmaVO;
	}

	public Map<Integer, ProfessorTitularDisciplinaTurmaVO> getMapProfessorDisciplinaTurmaVOs() {
		if (mapProfessorDisciplinaTurmaVOs == null) {
			mapProfessorDisciplinaTurmaVOs = new HashMap<Integer, ProfessorTitularDisciplinaTurmaVO>(0);
		}
		return mapProfessorDisciplinaTurmaVOs;
	}

	public void setMapProfessorDisciplinaTurmaVOs(Map<Integer, ProfessorTitularDisciplinaTurmaVO> mapProfessorDisciplinaTurmaVOs) {
		this.mapProfessorDisciplinaTurmaVOs = mapProfessorDisciplinaTurmaVOs;
	}

	public Boolean getApresentarComboBoxProfessorTurmaDisciplina() {
		if (apresentarComboBoxProfessorTurmaDisciplina == null) {
			apresentarComboBoxProfessorTurmaDisciplina = false;
		}
		return apresentarComboBoxProfessorTurmaDisciplina;
	}

	public void setApresentarComboBoxProfessorTurmaDisciplina(Boolean apresentarComboBoxProfessorTurmaDisciplina) {
		this.apresentarComboBoxProfessorTurmaDisciplina = apresentarComboBoxProfessorTurmaDisciplina;
	}

	public String getTipoFiltroPeriodo() {
		if (tipoFiltroPeriodo == null) {
			tipoFiltroPeriodo = "";
		}
		return tipoFiltroPeriodo;
	}

	public void setTipoFiltroPeriodo(String tipoFiltroPeriodo) {
		this.tipoFiltroPeriodo = tipoFiltroPeriodo;
	}
	
	
	public List<SelectItem> getListaTipoFiltroPeriodo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", "Todas as Aulas"));
		itens.add(new SelectItem("porPeriodoData", "Por Período de Data"));
		itens.add(new SelectItem("porMes", "Por Mês"));
		return itens;
	}

    public Date getDataFim() {
        if (dataFim == null) {
        	dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
        	dataInicio = new Date();
        }
        return dataInicio;
    }

    public String getDataInicio_Apresentar() {
		if (dataInicio == null) {
			return "";
		}
		return (Uteis.getData(dataInicio));
    }
    
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

	public Boolean getApresentarVersoDiarioInformacoesPraticaSupervisionada() {
		if (apresentarVersoDiarioInformacoesPraticaSupervisionada == null) {
			apresentarVersoDiarioInformacoesPraticaSupervisionada = Boolean.FALSE;
		}
		return apresentarVersoDiarioInformacoesPraticaSupervisionada;
	}

	public void setApresentarVersoDiarioInformacoesPraticaSupervisionada(
			Boolean apresentarVersoDiarioInformacoesPraticaSupervisionada) {
		this.apresentarVersoDiarioInformacoesPraticaSupervisionada = apresentarVersoDiarioInformacoesPraticaSupervisionada;
	}

    public Map<String, String> getFiltroNotaBimestre() {
    	if (filtroNotaBimestre == null) {
    		filtroNotaBimestre = new LinkedHashMap<String, String>(0);
    	}
		return filtroNotaBimestre;
	}

	public void setFiltroNotaBimestre(Map<String, String> filtroNotaBimestre) {
		this.filtroNotaBimestre = filtroNotaBimestre;
	}
	
	public List<String> getFiltroNotaBimestreSelecionados() {
		if (filtroNotaBimestreSelecionados == null) {
			filtroNotaBimestreSelecionados = new ArrayList<String>(0);
		}
		return filtroNotaBimestreSelecionados;
	}
	
	public void setFiltroNotaBimestreSelecionados(List<String> filtroNotaBimestreSelecionados) {
		this.filtroNotaBimestreSelecionados = filtroNotaBimestreSelecionados;
	}
	
	public Boolean getMarcarTodos() {
		if (marcarTodos == null) {
			marcarTodos = Boolean.FALSE;
		}
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}

	private void montarListaFiltroPorNota() {
		List<BimestreEnum> listaBimestreSemestre = Arrays.asList(BimestreEnum.values());
		Stream<BimestreEnum> listaBimestreSemestreFiltrados = listaBimestreSemestre.stream()
				.filter(b -> !b.equals(BimestreEnum.NAO_CONTROLA)
						&& !b.equals(BimestreEnum.RECUPERACAO_01)
						&& !b.equals(BimestreEnum.RECUPERACAO_02));
		listaBimestreSemestreFiltrados.forEach(b -> {
			getFiltroNotaBimestre().put(b.getValorApresentar(), b.getValor());
		});
	}
	
	public void montarFiltroPorNota() {
		definirObrigatoriedadeApresentarSituacaoMatricula();
		if (getUtilizarFiltroPorNota()) {
			montarListaFiltroPorNota();
		} else {
			setMarcarTodos(false);
			setFiltroNotaBimestreSelecionados(new ArrayList<String>(0));
		}
		verificarSelecaoBimestre();
	}

	public boolean getUtilizarFiltroPorNota() {
		return ((getTipoLayout().equals("DiarioRelFrequenciaNota") || getTipoLayout().equals("DiarioRelFrequenciaNota3")) && (getTurmaVO().getCurso().getNivelEducacional().equals("ME") || getTurmaVO().getCurso().getNivelEducacional().equals("BA"))) || (getTipoLayout().equals("DiarioRelFrequenciaNota") && (getTurmaVO().getCurso().getNivelEducacional().equals("ME") || getTurmaVO().getCurso().getNivelEducacional().equals("BA"))) || (getTipoLayout().equals("DiarioRelNota") && (getTurmaVO().getCurso().getNivelEducacional().equals("ME") || getTurmaVO().getCurso().getNivelEducacional().equals("BA")))  || (getTipoLayout().equals("DiarioRelFrequenciaNota2") && (getTurmaVO().getCurso().getNivelEducacional().equals("ME") || getTurmaVO().getCurso().getNivelEducacional().equals("BA")));
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosBimestres() {
		if (getMarcarTodos()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void marcarDesmarcarTodosBimestres() {
		setFiltroNotaBimestreSelecionados(getMarcarTodos() ? new ArrayList<String>(getFiltroNotaBimestre().values()) : new ArrayList<String>(0));
	}
	
	public void verificarSelecaoBimestre() {
		setMarcarTodos(getFiltroNotaBimestreSelecionados().size() == getFiltroNotaBimestre().size());
	}

	public Boolean getApresentarVersoAgrupado() {
		if (apresentarVersoAgrupado == null) {
			apresentarVersoAgrupado = true;
		}
		return apresentarVersoAgrupado;
	}

	public void setApresentarVersoAgrupado(Boolean apresentarVersoAgrupado) {
		this.apresentarVersoAgrupado = apresentarVersoAgrupado;
	}

	public Boolean getApresentarCargaHorariaFormatoInteiro() {
		if (apresentarCargaHorariaFormatoInteiro == null) {
			apresentarCargaHorariaFormatoInteiro = false;
		}
		return apresentarCargaHorariaFormatoInteiro;
	}

	public void setApresentarCargaHorariaFormatoInteiro(Boolean apresentarCargaHorariaFormatoInteiro) {
		this.apresentarCargaHorariaFormatoInteiro = apresentarCargaHorariaFormatoInteiro;
	}
	
	public Boolean getPermitirAlterarApresentarSituacaoMatricula() {
		if (permitirAlterarApresentarSituacaoMatricula == null) {
			permitirAlterarApresentarSituacaoMatricula = true;
		}
		return permitirAlterarApresentarSituacaoMatricula;
	}

	public void setPermitirAlterarApresentarSituacaoMatricula(Boolean permitirAlterarApresentarSituacaoMatricula) {
		this.permitirAlterarApresentarSituacaoMatricula = permitirAlterarApresentarSituacaoMatricula;
	}

	public void definirObrigatoriedadeApresentarSituacaoMatricula() {
		if (getTipoLayout().equals("DiarioRelFrequenciaNota2")) {
			setApresentarSituacaoMatricula(true);
			setPermitirAlterarApresentarSituacaoMatricula(false);
		} else {
			setApresentarSituacaoMatricula(getApresentarSituacaoMatricula());
			setPermitirAlterarApresentarSituacaoMatricula(true);
		}
	}
	
	public void verificarPermissaoTrazerAlunosTransferenciaMatriz() {
		try {
			DiarioRel.verificarPermissaoUsuarioFuncionalidade("PermitirTrazerAlunosTransferenciaMatriz", getUsuarioLogado());
			setPermitirTrazerAlunosTransferenciaMatriz(true);
		} catch (Exception ex) {
			setPermitirTrazerAlunosTransferenciaMatriz(false);
		}
	}

	public Boolean getPermitirTrazerAlunosTransferenciaMatriz() {
		if (permitirTrazerAlunosTransferenciaMatriz == null) {
			permitirTrazerAlunosTransferenciaMatriz = false;
		}
		return permitirTrazerAlunosTransferenciaMatriz;
	}

	public void setPermitirTrazerAlunosTransferenciaMatriz(Boolean permitirTrazerAlunosTransferenciaMatriz) {
		this.permitirTrazerAlunosTransferenciaMatriz = permitirTrazerAlunosTransferenciaMatriz;
	}

	public Boolean getValidarAulasRegistradas() {
		if(validarAulasRegistradas == null){
			try {
				DiarioRel.verificarPermissaoUsuarioFuncionalidade("PermitirGerarRelatorioDiarioComTodasAulasRegistradas", getUsuarioLogado());
				validarAulasRegistradas = true;
			} catch (Exception e) {
				validarAulasRegistradas = false;
			}
			
		}
		return validarAulasRegistradas;
	}

	public void setValidarAulasRegistradas(Boolean validarAulasRegistradas) {
		this.validarAulasRegistradas = validarAulasRegistradas;
	}
	
	public List<ProfessorTitularDisciplinaTurmaVO> getListaProfessorDisciplinaVOs() {
		if(listaProfessorDisciplinaVOs == null) {
			listaProfessorDisciplinaVOs = new ArrayList<ProfessorTitularDisciplinaTurmaVO>();
		}
		return listaProfessorDisciplinaVOs;
	}

	public void setListaProfessorDisciplinaVOs(List<ProfessorTitularDisciplinaTurmaVO> listaProfessorDisciplinaVOs) {
		this.listaProfessorDisciplinaVOs = listaProfessorDisciplinaVOs;
	}
	


	private Boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	
	public Boolean getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso() {
		if(permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso == null){
			try {
				DocumentoAssinado.verificarPermissaoUsuarioFuncionalidade("PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso", getUsuarioLogado());
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = true;
			} catch (Exception e) {
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = false;
			}
			
		}
		return permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	}

	public void setPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(Boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso) {
		this.permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	}

	public String getTituloRelatorio() {
		if(tituloRelatorio == null) {
			tituloRelatorio = "";
		}
		return tituloRelatorio;
	}

	public void setTituloRelatorio(String tituloRelatorio) {
		this.tituloRelatorio = tituloRelatorio;
	}

	public String getObservacoesRelatorio() {
		if(tituloRelatorio == null) {
			tituloRelatorio = "";
		}
		return observacoesRelatorio;
	}

	public void setObservacoesRelatorio(String observacoesRelatorio) {
		this.observacoesRelatorio = observacoesRelatorio;
	}	
	
}
