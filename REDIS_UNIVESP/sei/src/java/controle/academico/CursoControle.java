package controle.academico;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas cursoForm.jsp cursoCons.jsp) com
 * as funcionalidades da classe <code>Curso</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Curso
 * @see CursoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentacaoCursoVO;
import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularTipoAtividadeComplementarVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.LogGradeCurricularVO;
import negocio.comuns.academico.LogImpactoMatrizCurricularVO;
import negocio.comuns.academico.MaterialCursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.TipoAlteracaoMatrizCurricularEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoControleGrupoOptativaEnum;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.comuns.utilitarias.dominios.TituloCursoMedio;
import negocio.comuns.utilitarias.dominios.TituloCursoPos;
import negocio.comuns.utilitarias.dominios.TituloCursoSuperior;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.arquitetura.AtributoComparacao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas cursoForm.jsp cursoCons.jsp) com
 * as funcionalidades da classe <code>Curso</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Curso
 * @see CursoVO
 */

@Controller("CursoControle")
@Scope("viewScope")
@Lazy
public class CursoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 841178705014304678L;

	private CursoVO cursoVO;
	private String responsavel_Erro;
	protected List<SelectItem> listaSelectItemAreaConhecimento;
	protected List<SelectItem> listaSelectItemConfiguracaoAcademico;
	protected List<SelectItem> listaSelectItemConfiguracaoTCC;
	protected List<SelectItem> listaSelectItemDisciplina;
	protected List<SelectItem> listaSelectItemDisciplinaGrade;
	private DocumentacaoCursoVO documentacaoCursoVO;
	private CursoTurnoVO cursoTurnoVO;
	protected List<SelectItem> listaSelectItemTurno;
	protected PeriodoLetivoVO periodoLetivoVO;
	protected GradeDisciplinaVO gradeDisciplinaVO;
	protected DisciplinaVO disciplinaVO;
	protected List listaConsultaDisciplina;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	protected List<DisciplinaVO> listaDisciplinasAnteriores;
	private List<DisciplinaVO> listaTodasDisciplinas;
	private List<DisciplinaVO> listaTodasDisciplinasGrupoOptativa;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	protected List listaConsultaTurma;
	private String identificacaoConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	protected List listaConsultaFuncionario;
	protected List<CursoCoordenadorVO> listaCursoCoordenadorAux;
	protected CursoCoordenadorVO cursoCoordenadorVO;
	protected DisciplinaPreRequisitoVO disciplinaPreRequisitoVO;
	private GradeCurricularVO gradeCurricularVO;
	private GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO;
	protected String controleAba;
	protected Integer itemGradeDisciplina;
//	protected Boolean alterar;
	protected Integer codigoCursoGradeDisciplina;
	protected Boolean existePreRequisito;
	protected List listaSelectItemTipoDeDocumento;
	protected List listaTituloCurso;
	protected Boolean existeTitulo;
	private List listaNivelEducacional;
	private Boolean adicionarDisciplinas;
	private AutorizacaoCursoVO autorizacaoCurso;
	private MaterialCursoVO materialCurso;
	private FuncionarioVO funcionarioVO;
	private Boolean editandoCursoCoordenador;
	private List listaSelectItemUnidadeEnsino;
	private String erroUpload;
	private String msgErroUpload;
	private GradeDisciplinaVO gradeDisciplinaCriacaoTCC;
	private Integer totalAlunosCriacaoTCC;
	private GradeDisciplinaVO gradeDisciplinasPeriodo;
	private List<SelectItem> listaSelectItemTipoAtividadeComplementar;
	private GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO;
	private GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaEdicaoVO;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	private List<UnidadeEnsinoVO> listaConsultaUnidadeEnsino;
	private Boolean marcarTodasUnidadeEnsino;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO;
	private Boolean disciplinaCompostaGrupoOptativa;
	private Boolean apresentarAtivarGrade;
	private Boolean apresentarInativarGrade;
	private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;
	private String onCompleteConfigurarGrupoOptativa;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaAuxiliar;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private Integer gradeCurricularClonarAtividadeComplementar;
	private List<SelectItem> listaSelectItemCurso;
	private Integer cursoClonarAtividadeComplementar;

	private Boolean apresentarModalidadeCurso;
	private List<SelectItem> listaSelectItemModalidadeCurso;
	public Boolean apresentarMensagemControleInclusaoDisciplinasMininoMaximo;
	
	private List<MatriculaVO> listaMatriculaVinculadaAutorizacaoCurso;
	private AutorizacaoCursoVO autorizacaoCursoExcluir;
	private String usernameExcluirAutorizacaoCurso;
	private String senhaExcluirAutorizacaoCurso;
	
	private List<SelectItem> listaSelectItemGrupoPessoa;
	
	private List<UnidadeEnsinoCursoVO> listaUnidadeEnsinoCursos;
	Map<Integer, String> mapaUnidadeEnsinoCurso = new HashMap<>();
	private List<SelectItem> listaSelectItemQuestionario;
	private Boolean possuiPermissaoAlterarMatrizCurricularAtivaInativa;
	private Boolean possuiPermissaoAlterarMatrizCurricularConstrucao;
	
	private StringBuilder msgAvisoAlteracaoGrade;
	private LogGradeCurricularVO logGradeCurricularVO;
	private TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricular;
	private Boolean mensagemImpactoAlteracaoMatrizcurricularApresentada;
	private Boolean possuiImpactoHistoricoExclusaoGradeDisciplina;
	private GradeDisciplinaVO gradeDisciplinaExcluirVO;
	
	private GradeCurricularVO gradeCurricularOriginalBaseDadosVO;
	private List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs;
	private Boolean erroExclusaoGradeDisciplinaMatrizAtivaInativa;
	
	private DisciplinaVO disciplinaIncluirVO;
	private Boolean adicionandoNovaDisciplinaComposta;
	private List<SelectItem> listaSelectItemConfiguracaoLdap;
	
	private GradeCurricularEstagioVO gradeCurricularEstagioVO;
	private List<SelectItem> listaSelectItemQuestionarioEstagio;
	private List<SelectItem> listaSelectItemQuestionarioEstagioEdicao;
	private List<SelectItem> listaSelectItemTextoPadraoDeclaracao;
	private List<SelectItem> listaSelectConteudoMasterBlackboard;
	private List<SelectItem> listaSelectItemEixoCurso;
	private List listaSelectItemTextoPadraoContratoMatriculaCalouro;
	private List<SelectItem> listaSelectItemTipoAutorizacaoCurso;
	private DataModelo controleConsultaOtimizadoPessoa;
	private List<SelectItem> tipoConsultaComboDocenteResposavelEstagio;
	private GradeCurricularEstagioVO gradeCurricularEstagioEditar;
	private Boolean editarGradeCurricularEstagio;
	
	
	public AutorizacaoCursoVO getAutorizacaoCursoExcluir() {
		return autorizacaoCursoExcluir;
	}

	public void setAutorizacaoCursoExcluir(AutorizacaoCursoVO autorizacaoCursoExcluir) {
		this.autorizacaoCursoExcluir = autorizacaoCursoExcluir;
	}

	public CursoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setListaDisciplinasAnteriores(new ArrayList<DisciplinaVO>(0));
		setListaSelectItemDisciplinaGrade(new ArrayList(0));
		setGradeCurricularVO(new GradeCurricularVO());
		setMensagemID("msg_entre_prmconsulta");
		setControleAba("cursoTab");
		setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
		setPossuiPermissaoAlterarMatrizCurricularAtivaInativa(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularAtivaInativa());
		setExistePreRequisito(Boolean.FALSE);
		montarListaTituloCurso();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemPeriodicidade();
	}

	public void verificarPermissaoAtivarGrade() {
		try {
			getFacadeFactory().getGradeCurricularFacade().ativarGrade(getGradeCurricularVO(), getUsuarioLogado());
			setApresentarAtivarGrade(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarAtivarGrade(Boolean.FALSE);
		}
	}

	public void verificarPermissaoModalidadeCurso() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PermitirInformarMolidadeCurso", getUsuarioLogado());
			setApresentarModalidadeCurso(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarModalidadeCurso(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoAlterarMatrizCurricularAtivaInativa() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PermitirAlterarMatrizCurricularAtivaInativa", getUsuarioLogado());
			setPossuiPermissaoAlterarMatrizCurricularAtivaInativa(Boolean.TRUE);
		} catch (Exception e) {
			setPossuiPermissaoAlterarMatrizCurricularAtivaInativa(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoInativarGrade() {
		try {
			getFacadeFactory().getGradeCurricularFacade().desativarGrade(getGradeCurricularVO(), getUsuarioLogado());
			setApresentarInativarGrade(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarInativarGrade(Boolean.FALSE);
		}
	}
		
	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Curso</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setCursoVO(new CursoVO());
		setPeriodoLetivoVO(new PeriodoLetivoVO());
		inicializarListasSelectItemTodosComboBox();
		setCursoTurnoVO(new CursoTurnoVO());
		setDocumentacaoCursoVO(new DocumentacaoCursoVO());
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setGradeCurricularVO(new GradeCurricularVO());
		setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
		setListaSelectItemDisciplinaGrade(new ArrayList(0));
		setMensagemID("msg_entre_dados");
		setControleAba("cursoTab");
		setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
		setGradeCurricularVO(new GradeCurricularVO());
		setExistePreRequisito(Boolean.FALSE);
		setListaTituloCurso(new ArrayList(0));
		setCursoCoordenadorVO(new CursoCoordenadorVO());
		setListaCursoCoordenadorAux(new ArrayList<CursoCoordenadorVO>(0));
		verificarPermissaoAtivarGrade();
		verificarPermissaoInativarGrade();
		verificarPermissaoModalidadeCurso();
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Curso</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		try {
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			setCursoVO(obj);
			montarListaTituloCurso();
			setCursoTurnoVO(new CursoTurnoVO());
			setDocumentacaoCursoVO(new DocumentacaoCursoVO());
			setGradeCurricularVO(new GradeCurricularVO());
			setPeriodoLetivoVO(new PeriodoLetivoVO());
			setGradeDisciplinaVO(new GradeDisciplinaVO());
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setCursoCoordenadorVO(new CursoCoordenadorVO());
			setListaCursoCoordenadorAux(new ArrayList<CursoCoordenadorVO>(0));
			setListaSelectItemDisciplinaGrade(new ArrayList(0));
			inicializarListasSelectItemTodosComboBox();
			setExistePreRequisito(Boolean.FALSE);
			setGradeCurricularVO(new GradeCurricularVO());
			getCursoVO().atualizarTotalCargaHoraria();
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			verificarPermissaoAtivarGrade();
			verificarPermissaoInativarGrade();
			verificarPermissaoModalidadeCurso();
			setControleAba("cursoTab");
			obj.setMensagemConfirmacaoNovaMatricula(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA, obj.getCodigo(), false, getUsuarioLogado()));
			obj.setMensagemRenovacaoMatricula(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA, obj.getCodigo(), false, getUsuarioLogado()));
			obj.setMensagemAtivacaoPreMatriculaEntregaDocumento(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE, obj.getCodigo(), false, getUsuarioLogado()));
			obj.setHabilitarMensagemNotificacaoNovaMatricula(Uteis.isAtributoPreenchido(obj.getMensagemConfirmacaoNovaMatricula()) && Uteis.isAtributoPreenchido(obj.getMensagemConfirmacaoNovaMatricula().getCursoVO()));
			obj.setHabilitarMensagemNotificacaoRenovacaoMatricula(Uteis.isAtributoPreenchido(obj.getMensagemRenovacaoMatricula()) && Uteis.isAtributoPreenchido(obj.getMensagemRenovacaoMatricula().getCursoVO()));
			obj.setHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento(Uteis.isAtributoPreenchido(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento()) && Uteis.isAtributoPreenchido(obj.getMensagemAtivacaoPreMatriculaEntregaDocumento().getCursoVO()));
			Ordenacao.ordenarLista(obj.getGradeCurricularVOs(), "situacao");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
	}

	public String visualizarMaterial() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setCursoVO(obj);
		// montarListaTituloCurso();
		setCursoTurnoVO(new CursoTurnoVO());
		setDocumentacaoCursoVO(new DocumentacaoCursoVO());
		setGradeCurricularVO(new GradeCurricularVO());
		setPeriodoLetivoVO(new PeriodoLetivoVO());
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
		setCursoCoordenadorVO(new CursoCoordenadorVO());
		setListaCursoCoordenadorAux(new ArrayList<CursoCoordenadorVO>(0));
		setListaSelectItemDisciplinaGrade(new ArrayList(0));
		inicializarListasSelectItemTodosComboBox();
		// setExistePreRequisito(Boolean.FALSE);
		// setGradeCurricularPeriodoLetivo(new GradeCurricularVO());
		// getCursoVO().atualizarTotalCargaHoraria();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
	}
	
	public Boolean getNovoObj(){
 		return getCursoVO().isNovoObj();
	}
	
	public void consultarUnidadeEnsinoAdicionarCurso(){
		try {
			setMarcarTodasUnidadeEnsino(Boolean.FALSE);
			getListaConsultaUnidadeEnsino().clear();
			getListaConsultaUnidadeEnsino().addAll(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(null, false, getUsuarioLogado()));
			setMensagemID("msg_curso_selecionarUnidadeEnsino");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void gravarUnidadeEnsinoCurso(){
		try {
			getFacadeFactory().getUnidadeEnsinoCursoFacade().criarUnidadeEnsinoCursoCadastroCurso(getListaConsultaUnidadeEnsino(), getCursoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodasUnidadesEnsinoAction(){
    	for(UnidadeEnsinoVO unidade :getListaConsultaUnidadeEnsino()){
    		if(marcarTodasUnidadeEnsino){
    			unidade.setSelecionarAdicionarCursoInstituicao(Boolean.TRUE);
    		}else {
    			unidade.setSelecionarAdicionarCursoInstituicao(Boolean.FALSE);
			}
    	}
    }

	public String getCaminhoServidorDownload() {
		try {
			MaterialCursoVO obj = (MaterialCursoVO) context().getExternalContext().getRequestMap().get("materialCursoVOItens");
			if(Uteis.isAtributoPreenchido(obj.getArquivoVO().getNome())){
				context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public CursoVO montarDadosUnidadeEnsinoVOCompleto(CursoVO obj) {
		try {
			getFacadeFactory().getCursoFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
			return obj;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new CursoVO();
	}

	public Boolean getNivelEducacional() {
		if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
			return true;
		}
		return false;
	}
	
	public void carregarUnidadeEnsinoCurso() {
		if (Uteis.isAtributoPreenchido(getCursoVO().getCodigo())) {
			listaUnidadeEnsinoCursos = getFacadeFactory().getUnidadeEnsinoCursoFacade()
					.consultarPorCursoAgrupandoPorUnidadeEnsino(getCursoVO().getCodigo());
		}

		for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : listaUnidadeEnsinoCursos) {
			if (!Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCodigoInep())) {
				unidadeEnsinoCursoVO.setCodigoInep(getCursoVO().getIdCursoInep());
			}
		}
	}

	public void atualizarUnidadeEnsinoCurso() {
		try {
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				getFacadeFactory().getUnidadeEnsinoCursoFacade().atualizarUnidadeEnsinoCurso(listaUnidadeEnsinoCursos, getCursoVO());
				setMensagemID("msg_dados_gravados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Curso</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (Uteis.isAtributoPreenchido(cursoVO.getConfiguracaoAcademico().getCodigo())) {
				cursoVO.setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(cursoVO.getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			}
			if (cursoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getCursoFacade().incluir(cursoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getCursoFacade().alterar(cursoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			excluirCursoCoordenadores();
			finalizarPeriodoLetivo();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm.xhtml");
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {

			if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
					setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
					setMsgErroUpload("Prezado professor/coordenador, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
				} else {
					setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
					getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialCurso().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CURSO_TMP, getUsuarioLogado());
				}
			} else {
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialCurso().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CURSO_TMP, getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	// public void copiarArquivo(File origem, File destino) throws Exception {
	// // Cria channel no origem
	// FileChannel oriChannel = new
	// FileInputStream(origem.getAbsolutePath()).getChannel();
	// // Cria channel no destino
	// FileChannel destChannel = new
	// FileOutputStream(destino.getAbsolutePath()).getChannel();
	// // Copia conteúdo da origem no destino
	// destChannel.transferFrom(oriChannel, 0, oriChannel.size());
	// // Fecha channels
	// oriChannel.close();
	// destChannel.close();
	// }
	//
	private File obterNomeArquivoFisicoComAcentos(String arquivoSendoBuscado, Map<String, File> mapaArquivosExistentesComNomesSemAcentos) throws Exception {
		for (String nomeArquivoExistente : mapaArquivosExistentesComNomesSemAcentos.keySet()) {
			if (arquivoSendoBuscado.equals(nomeArquivoExistente)) {
				return mapaArquivosExistentesComNomesSemAcentos.get(nomeArquivoExistente);
			}
		}
		return null;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CursoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrRegistroInterno")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoCons");
		}
	}

	public List getListaSelectItemTipoDisciplinaDisciplina() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoDisciplinaDisciplinas = (Hashtable) Dominios.getTipoDisciplinaDisciplina();
		Enumeration keys = tipoDisciplinaDisciplinas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDisciplinaDisciplinas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void executarAlterarDisciplinaPeriodo() {
		// int index = 0;
		// for (GradeDisciplinaVO gradeDisciplina :
		// getPeriodoLetivoVO().getGradeDisciplinaVOs()) {
		// if (gradeDisciplina.getCodigo() ==
		// getGradeDisciplinasPeriodo().getCodigo()) {
		// getPeriodoLetivoVO().getGradeDisciplinaVOs().set(index,
		// getGradeDisciplinasPeriodo());
		// }
		// index++;
		// }
	}

	public void executarCalcularCargaHorariaTeorica() {
		try {
			GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			if (gradeDisciplina != null) {
				setGradeDisciplinasPeriodo(gradeDisciplina);
				executarCalcularCargaHorariaTeorica(gradeDisciplina);
			}
			executarCalcularCargaHorariaTotalETotalCreditos(gradeDisciplina, "cargaHoraria", false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarCalcularCargaHorariaTeoricaDisciplinaComposta() {
		try {
			Integer cargaHorariaDisciplina = getDisciplinaCompostaGrupoOptativa() ? getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() : getGradeDisciplinasPeriodo().getCargaHoraria();
			getFacadeFactory().getGradeDisciplinaCompostaFacade().executarCalcularCargaHorariaTeoricaDisciplinaComposta(cargaHorariaDisciplina, getGradeDisciplinaCompostaVOs(), getGradeDisciplinaCompostaVO());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarCalcularCargaHorariaTeorica(GradeDisciplinaVO gradeDisciplina) throws Exception {
		if (gradeDisciplina.getCargaHorariaPratica() > gradeDisciplina.getCargaHoraria()) {
			setMensagemDetalhada(UteisJSF.internacionalizar("msg_erro_Disciplina_cargaHorariaPraticaMaiorQueCargaHorariaTotal"));
		} else {
			gradeDisciplina.setCargaHorariaTeorica(gradeDisciplina.getCargaHoraria() - gradeDisciplina.getCargaHorariaPratica());
			setMensagemDetalhada("");
		}
	}
	
	public void executarCalcularCargaHorariaTotalETotalCreditos() {
		executarCalcularCargaHorariaTotalETotalCreditos(null, null, false);
	}
	
	public void executarCalcularCargaHorariaTotalETotalCreditosVerificandoTotais() {
		executarCalcularCargaHorariaTotalETotalCreditos(getGradeDisciplinasPeriodo(), "creditos", false);
	}

	public void executarCalcularCargaHorariaTotalETotalCreditos(GradeDisciplinaVO gd, String origem, boolean limparMensagem) {
		int subTotalCargaHorariaGradeDisciplina = 0;
		int subTotalCreditosGradeDisciplina = 0;
		int totalCargaHoraria = 0;
		int totalCreditos = 0;
		int index = 0;
		for (GradeDisciplinaVO gradeDisciplina : getPeriodoLetivoVO().getGradeDisciplinaVOs()) {
			if (gradeDisciplina.getCargaHoraria() > 0) {
				subTotalCargaHorariaGradeDisciplina += gradeDisciplina.getCargaHoraria();
			}
			if (gradeDisciplina.getNrCreditos() > 0) {
				subTotalCreditosGradeDisciplina += gradeDisciplina.getNrCreditos();
			}
		}
		if (getPeriodoLetivoVO().getControleOptativaGrupo()) {
			subTotalCargaHorariaGradeDisciplina += getPeriodoLetivoVO().getNumeroCargaHorariaOptativa();
			subTotalCreditosGradeDisciplina += getPeriodoLetivoVO().getNumeroCreditoOptativa();
		}
		getPeriodoLetivoVO().setTotalCargaHoraria(subTotalCargaHorariaGradeDisciplina);
		getPeriodoLetivoVO().setTotalCreditos(subTotalCreditosGradeDisciplina);

		for (PeriodoLetivoVO periodoLetivo : getGradeCurricularVO().getPeriodoLetivosVOs()) {
			totalCargaHoraria += periodoLetivo.getTotalCargaHoraria();
			totalCreditos += periodoLetivo.getTotalCreditos();

			if (periodoLetivo.getPeriodoLetivo().equals(getPeriodoLetivoVO().getPeriodoLetivo())) {
				getGradeCurricularVO().getPeriodoLetivosVOs().set(index, getPeriodoLetivoVO());
			}
			index++;
		}
		getGradeCurricularVO().setTotalCargaHorariaDisciplinasObrigatorias(null);
		getGradeCurricularVO().setTotalCargaHoraria(totalCargaHoraria);
		getGradeCurricularVO().setTotalCreditos(totalCreditos);
		
		executarValidacaoTotalCargaHorariaCreditos(gd, origem, limparMensagem);
	}
	
	public void executarValidacaoTotalCargaHorariaCreditos() {
		executarValidacaoTotalCargaHorariaCreditos(getGradeDisciplinasPeriodo(), "ambos", true);
	}
	
	public void executarValidacaoTotalCargaHorariaCreditos(GradeDisciplinaVO gd, String origem, boolean limparMensagem) {
		if (gd != null && gd.getDisciplinaComposta() && getGradeDisciplinasPeriodo().getTipoControleComposicao().equals(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS)) {
			int totalCargaHorariaComposta = 0;
			int totalCreditosComposta = 0;
			for (GradeDisciplinaVO comp : gd.getGradeDisciplinaCompostaVOs()) {
				totalCargaHorariaComposta += comp.getCargaHoraria();
				totalCreditosComposta += comp.getNrCreditos();
			}
			if (origem != null && ("cargaHoraria".equals(origem) || "ambos".equals(origem)) && totalCargaHorariaComposta > gd.getCargaHoraria()) {
				setMensagemDetalhada(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_cargaHorariaMaiorCargaHorariaDisciplinaPrincipal"));
			} else if (origem != null && ("creditos".equals(origem) || "ambos".equals(origem)) && totalCreditosComposta > gd.getNrCreditos()) {
				setMensagemDetalhada(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_creditosMaiorCreditosDisciplinaPrincipal"));
			} else {
				setMensagemDetalhada("");
			}
		} else {
			if (limparMensagem) {
				setMensagemDetalhada("");
			}
		}
	}

	public void consultarDisciplina() {
		try {

			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				throw new Exception("Digite um valor para consulta");
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if(!Uteis.getIsValorNumerico(getValorConsultaDisciplina())) {
					throw new Exception("Informe apenas valores numéricos.");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaNivelEducacional(new Integer(valorInt), getCursoVO().getNivelEducacional(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(disciplina)) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeNivelEducional(getValorConsultaDisciplina(), getCursoVO().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparListaConsultaDisciplina() {
		setValorConsultaDisciplina("");
		setListaConsultaDisciplina(new ArrayList(0));
		setMensagemID("msg_dados_consultados");
	}

	public void selecionarDisciplinaEixoTematico() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) getRequestMap().get("disciplinaItens");
			if (disciplina != null) {
				getGradeDisciplinasPeriodo().setDisciplinaEixoTematico(disciplina);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarConfiguracaoAcademico() {
		try {
			if (Uteis.isAtributoPreenchido(getGradeDisciplinasPeriodo().getConfiguracaoAcademico())) {
				getGradeDisciplinasPeriodo().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getGradeDisciplinasPeriodo().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			}else{
				getGradeDisciplinasPeriodo().setConfiguracaoAcademico(new ConfiguracaoAcademicoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void inicializarMenuCursoVisaoCoordenador() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoCoordenador()){
			setListaConsulta(getFacadeFactory().getCursoFacade().consultarListaCursoPorCodigoPessoaCoordenador(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemDetalhada("", "");
			}
			
		} catch (Exception e) {
			
		}
	}

	@PostConstruct
	public void inicializarMenuCursoVisaoProfessor() {
		if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
		try {
			setListaConsulta(getFacadeFactory().getCursoFacade().consultarCursoPorProfessorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), "AT", null, 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false, null, getUsuarioLogado()));
			setMensagemDetalhada("", "");			
		} catch (Exception e) {
			
		}
		}
	}

	public void inicializarConsultaDisciplinaEixoTematico() {
		// setGradeDisciplinaVO(new GradeDisciplinaVO());
		// setGradeDisciplinaVO((GradeDisciplinaVO)
		// getRequestMap().get("gradeDisciplina"));
	}

	public void fecharConsultaDisciplinaEixoTematico() {
		// setGradeDisciplinaVO(new GradeDisciplinaVO());
	}

	public void limparDisciplinaEixoTematico() {
		getGradeDisciplinasPeriodo().setDisciplinaEixoTematico(null);
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CursoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public void excluir() {
		try {
			setCursoVO(cursoVO);
			int index = 0;
			Iterator i = getCursoVO().getGradeCurricularVOs().iterator();
			while (i.hasNext()) {
				GradeCurricularVO objExistente = (GradeCurricularVO) i.next();
				if (!objExistente.getSituacao().equals("CO")) {
					setMensagemDetalhada("O CURSO não pode ser excluído por que a GRADE CURRICULAR está sendo usada");
				}
				index++;
			}

			getFacadeFactory().getCursoFacade().excluir(cursoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setCursoVO(new CursoVO());
			setCursoTurnoVO(new CursoTurnoVO());
			setGradeCurricularVO(new GradeCurricularVO());
			setPeriodoLetivoVO(new PeriodoLetivoVO());
			setGradeDisciplinaVO(new GradeDisciplinaVO());
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setDocumentacaoCursoVO(new DocumentacaoCursoVO());
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>GradeCurricular</code> para o objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public String adicionarGradeCurricular() throws Exception {
		try {
			if (getCursoVO().getCodigo().equals(0)) {
				throw new Exception("Este é um curso novo, favor clique no botão GRAVAR antes de adicionar uma matriz curricular.");
			}
			this.setGradeCurricularVO(new GradeCurricularVO());
			this.setPeriodoLetivoVO(new PeriodoLetivoVO());
			setGradeCurricularEstagioVO(new GradeCurricularEstagioVO());
			getGradeCurricularVO().setCurso(getCursoVO().getCodigo());
			getGradeCurricularVO().setPercentualPermitirIniciarEstagio(50.0);
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			montarListaSelectItemPeriodicidade();
			montarListaSelectItemQuestionarioEstagio();
			montarListaSelectItemTextoPadraoDeclaracao();
			return Uteis.getCaminhoRedirecionamentoNavegacao("gradeCurricularForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
		}
	}

	public void finalizarPeriodoLetivo() {
		setControleAba("cursoTab");
		setGradeCurricularVO(new GradeCurricularVO());
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>GradeCurricular</code> para edição pelo usuário.
	 */
	public String editarGradeCurricular() {
		try {
			setMensagemID("");
			GradeCurricularVO obj = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
			getFacadeFactory().getGradeCurricularFacade().realizarMongagemDadosEmGradeCurricularExistente(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setGradeCurricularVO(obj);
			setListaSelectItemGradeCurricularGrupoOptativa(null);
			
			CursoVO.validarDadosIncluirPeriodoLetivo(cursoVO);
			setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
			if (getGradeCurricularVO().getSituacao().equals("CO")) {
				setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			} else {
				setPossuiPermissaoAlterarMatrizCurricularConstrucao(Boolean.FALSE);
				setPossuiPermissaoAlterarMatrizCurricularAtivaInativa(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularAtivaInativa());
				setGradeCurricularOriginalBaseDadosVO((GradeCurricularVO) Uteis.clonar(getGradeCurricularVO()));
				getGradeCurricularVO().setGradeCurricularOriginalVO(getGradeCurricularOriginalBaseDadosVO());
			}
			getCursoVO().setNrPeriodoLetivo(getGradeCurricularVO().getPeriodoLetivosVOs().size());
			verificarApresentarMensagemControleInclusaoDisciplinasMininoMaximo(true);
			montarListaSelectItemPeriodicidade();
			setGradeCurricularEstagioVO(new GradeCurricularEstagioVO());
			montarListaSelectItemQuestionarioEstagio();
			montarListaSelectItemTextoPadraoDeclaracao();
			return Uteis.getCaminhoRedirecionamentoNavegacao("gradeCurricularForm");
		} catch (Exception e) {
			setControleAba("gradeCurricular");
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
		}
	}

	public void removerSomentePeriodosLetivosGradeCurricular() throws Exception {
		GradeCurricularVO obj = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
		setMensagemDetalhada("");
		getFacadeFactory().getCursoFacade().excluirPeriodosLetivosGradeCurricular(obj, getCursoVO(), getUsuarioLogado());
		setGradeCurricularVO(new GradeCurricularVO());
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>GradeCurricular</code> do objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public void removerGradeCurricular() throws Exception {
		GradeCurricularVO obj = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
		getFacadeFactory().getGradeCurricularFacade().realizarMongagemDadosEmGradeCurricularExistente(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		setGradeCurricularVO(obj);
		setMensagemDetalhada("");
		if (getGradeCurricularVO().getSituacao().equals("CO")) {
			getFacadeFactory().getCursoFacade().excluirObjGradeCurricularVOs(getGradeCurricularVO(), getCursoVO(), getUsuarioLogado());
			setGradeCurricularVO(new GradeCurricularVO());
			setMensagemID("msg_dados_excluidos");
		} else if (getGradeCurricularVO().getSituacao().equals("IN")) {
			// consultar se existe vincula, caso nao pode remover.
			if (!getFacadeFactory().getMatriculaPeriodoFacade().consultarSeExisteMatriculaPeriodoVinculadaAGradeCurricular(getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado())) {
				if (!getFacadeFactory().getTurmaDisciplinaFacade().consultarSeExisteTurmaDisciplinaVinculadaAGradeCurricular(getGradeCurricularVO().getCodigo(), false, getUsuarioLogado())) {
					getFacadeFactory().getCursoFacade().excluirObjGradeCurricularVOs(getGradeCurricularVO(), getCursoVO(), getUsuarioLogado());
					setGradeCurricularVO(new GradeCurricularVO());
					setMensagemID("msg_dados_excluidos");
				} else {
					setMensagemDetalhada(" A Grade Curricular não pode ser REMOVIDA pois existem turmas vinculadas a mesma.");
				}
			} else {
				setMensagemDetalhada(" A Grade Curricular não pode ser REMOVIDA pois existem matrículas vinculadas a mesma.");
			}
		} else if (getGradeCurricularVO().getSituacao().equals("AT")) {
			if (this.getCursoVO().getNivelEducacional().equals("PO")) {				
				if (!getFacadeFactory().getMatriculaPeriodoFacade().consultarSeExisteMatriculaPeriodoVinculadaAGradeCurricular(getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado())) {
					if (!getFacadeFactory().getTurmaDisciplinaFacade().consultarSeExisteTurmaDisciplinaVinculadaAGradeCurricular(getGradeCurricularVO().getCodigo(), false, getUsuarioLogado())) {
						getFacadeFactory().getCursoFacade().excluirObjGradeCurricularVOs(getGradeCurricularVO(), getCursoVO(), getUsuarioLogado());
						setGradeCurricularVO(new GradeCurricularVO());
						setMensagemID("msg_dados_excluidos");
					} else {
						setMensagemDetalhada(" A Grade Curricular não pode ser REMOVIDA pois existem turmas vinculadas a mesma.");
					}
				} else {
					setMensagemDetalhada(" A Grade Curricular não pode ser REMOVIDA pois existem matrículas vinculadas a mesma.");
				}
			} else {
				setMensagemDetalhada(" A Grade Curricular não pode ser Removida pois ela está Ativa ou Defasada.");
			}
		} else {
			setMensagemDetalhada(" A Grade Curricular não pode ser Removida pois ela está Ativa ou Defasada.");
		}
	}

	public String clonarGrade() {
		try {
			GradeCurricularVO obj = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
			getFacadeFactory().getGradeCurricularFacade().realizarMongagemDadosEmGradeCurricularExistente(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			GradeCurricularVO objC = (GradeCurricularVO) obj.clone();
			objC.setCodigo(0);
			objC.setNome(obj.getNome() + " - Clonada");
			int qtde = 0;
			for (GradeCurricularVO gcVO : getCursoVO().getGradeCurricularVOs()) {
				if (gcVO.getNome().contains(objC.getNome())) {
					qtde++;
				}
			}
			if (qtde != 0) {
				objC.setNome(objC.getNome() + " " + qtde);
			}
			objC.setNovoObj(true);
			objC.setDataCadastro(new Date());
			objC.setDataDesativacao(null);
			objC.setDataFinalVigencia(null);
			objC.setResponsavelAtivacao(new UsuarioVO());
			objC.setResponsavelDesativacao(new UsuarioVO());
			objC.setSituacao("CO");
			objC.setPeriodoLetivosVOs(new ArrayList<PeriodoLetivoVO>(0));
			objC.setListaGradeCurricularTipoAtividadeComplementarVOs(new ArrayList<GradeCurricularTipoAtividadeComplementarVO>(0));
			objC.setGradeCurricularGrupoOptativaVOs(new ArrayList<GradeCurricularGrupoOptativaVO>(0));
			for (GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO : obj.getListaGradeCurricularTipoAtividadeComplementarVOs()) {
				GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarClone = (GradeCurricularTipoAtividadeComplementarVO) gradeCurricularTipoAtividadeComplementarVO.clone();
				gradeCurricularTipoAtividadeComplementarClone.setCodigo(0);
				gradeCurricularTipoAtividadeComplementarClone.setNovoObj(true);
				gradeCurricularTipoAtividadeComplementarClone.setGradeCurricularVO(objC);
				objC.getListaGradeCurricularTipoAtividadeComplementarVOs().add(gradeCurricularTipoAtividadeComplementarClone);
			}

			for (GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO : obj.getGradeCurricularGrupoOptativaVOs()) {
				GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaCloneVO = (GradeCurricularGrupoOptativaVO) gradeCurricularGrupoOptativaVO.clone();
				gradeCurricularGrupoOptativaCloneVO.setCodigo(0);
				gradeCurricularGrupoOptativaCloneVO.setGradeCurricular(objC);
				gradeCurricularGrupoOptativaCloneVO.setNovoObj(true);
				gradeCurricularGrupoOptativaCloneVO.setGradeCurricularGrupoOptativaDisciplinaVOs(new ArrayList<GradeCurricularGrupoOptativaDisciplinaVO>(0));
				for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
					GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaCloneVO = (GradeCurricularGrupoOptativaDisciplinaVO) gradeCurricularGrupoOptativaDisciplinaVO.clone();
					gradeCurricularGrupoOptativaDisciplinaCloneVO.setNovoObj(true);
					gradeCurricularGrupoOptativaDisciplinaCloneVO.setCodigo(0);
					gradeCurricularGrupoOptativaDisciplinaCloneVO.setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaCloneVO);
					gradeCurricularGrupoOptativaDisciplinaCloneVO.setGradeDisciplinaCompostaVOs(new ArrayList<GradeDisciplinaCompostaVO>(0));
					for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()) {
						GradeDisciplinaCompostaVO gradeDisciplinaCompostaClone = (GradeDisciplinaCompostaVO) gradeDisciplinaCompostaVO.clone();
						gradeDisciplinaCompostaClone.setCodigo(0);
						gradeDisciplinaCompostaClone.setNovoObj(true);
						gradeDisciplinaCompostaClone.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaCloneVO);
						gradeCurricularGrupoOptativaDisciplinaCloneVO.getGradeDisciplinaCompostaVOs().add(gradeDisciplinaCompostaClone);
					}
					gradeCurricularGrupoOptativaDisciplinaCloneVO.setDisciplinaRequisitoVOs(new ArrayList<DisciplinaPreRequisitoVO>(0));
					for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : gradeCurricularGrupoOptativaDisciplinaVO.getDisciplinaRequisitoVOs()) {
						DisciplinaPreRequisitoVO disciplinaPreRequisitoClone = (DisciplinaPreRequisitoVO) disciplinaPreRequisitoVO.clone();
						disciplinaPreRequisitoClone.setCodigo(0);
						disciplinaPreRequisitoClone.setNovoObj(true);
						disciplinaPreRequisitoClone.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaCloneVO.getCodigo());
						gradeCurricularGrupoOptativaDisciplinaCloneVO.getDisciplinaRequisitoVOs().add(disciplinaPreRequisitoClone);
					}
					gradeCurricularGrupoOptativaCloneVO.getGradeCurricularGrupoOptativaDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaCloneVO);
				}
				objC.getGradeCurricularGrupoOptativaVOs().add(gradeCurricularGrupoOptativaCloneVO);
			}
			for (PeriodoLetivoVO p : obj.getPeriodoLetivosVOs()) {
				PeriodoLetivoVO perC = (PeriodoLetivoVO) p.clone();
				perC.setCodigo(0);
				perC.setNovoObj(true);
				perC.setGradeCurricular(0);
				perC.setControleOptativaGrupo(false);
				perC.setGradeDisciplinaVOs(new ArrayList<GradeDisciplinaVO>(0));
				for (GradeDisciplinaVO grade : p.getGradeDisciplinaVOs()) {
					GradeDisciplinaVO gradeC = (GradeDisciplinaVO) grade.clone();
					gradeC.setCodigo(0);
					gradeC.setPeriodoLetivo(0);
					gradeC.setNovoObj(true);
					gradeC.setDisciplinaRequisitoVOs(new ArrayList<DisciplinaPreRequisitoVO>(0));
					for (DisciplinaPreRequisitoVO disciPre : grade.getDisciplinaRequisitoVOs()) {
						DisciplinaPreRequisitoVO disciPreC = (DisciplinaPreRequisitoVO) disciPre.clone();
						disciPreC.setCodigo(0);
						disciPreC.setNovoObj(true);
						disciPreC.setGradeDisciplina(0);
						gradeC.adicionarObjDisciplinaPreRequisitoVOs(disciPreC);
					}
					gradeC.setGradeDisciplinaCompostaVOs(new ArrayList<GradeDisciplinaCompostaVO>(0));
					for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : grade.getGradeDisciplinaCompostaVOs()) {
						GradeDisciplinaCompostaVO gradeDisciplinaCompostaClone = (GradeDisciplinaCompostaVO) gradeDisciplinaCompostaVO.clone();
						gradeDisciplinaCompostaClone.setCodigo(0);
						gradeDisciplinaCompostaClone.setNovoObj(true);
						gradeDisciplinaCompostaClone.setGradeDisciplina(gradeC);
						gradeC.getGradeDisciplinaCompostaVOs().add(gradeDisciplinaCompostaClone);
					}
					perC.getGradeDisciplinaVOs().add(gradeC);
				}
				objC.adicionarObjPeriodoLetivoVOs(perC);
			}
//			this.getCursoVO().adicionarObjGradeCurricularVOs(objC);
			setGradeCurricularVO(objC);
			setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			getCursoVO().setNrPeriodoLetivo(getGradeCurricularVO().getPeriodoLetivosVOs().size());
			setMensagemID("msg_dados_clonados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("gradeCurricularForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
		}
	}

	public void selecionarGrade() {
		try {
			setGradeCurricularVO(new GradeCurricularVO());
			GradeCurricularVO obj = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
			getFacadeFactory().getGradeCurricularFacade().realizarMongagemDadosEmGradeCurricularExistente(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setGradeCurricularVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarGradeDisciplinasPeriodo() {
		setGradeDisciplinasPeriodo(new GradeDisciplinaVO());
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		setGradeDisciplinasPeriodo(obj);
		montarListaSelectItemConfiguracaoAcademico();
		realizarInicializacaoDadosGradeDisciplinaCompostaPorGradeDisciplina();
	}

	public void configurarPeriodoLetivo() throws Exception {
		setPeriodoLetivoVO(new PeriodoLetivoVO());
		PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setPeriodoLetivoVO(obj);
		if (getGradeCurricularVO().getSituacao().equals("CO")) {
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
		} else {
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(Boolean.FALSE);
			if (!getCursoVO().getNivelEducacionalPosGraduacao() && !getPossuiPermissaoAlterarMatrizCurricularAtivaInativa()) {
				setMensagemDetalhada("Disciplinas não podem ser alteradas pois a Grade Curricular está Ativa ou Defazada.");
			}
		}
	}
	
	public void validarDadosImpactoInclusaoDisciplina() {
		try {
			if (getValidarAlteracaoMatrizCurricularAtivaInativa()) {
				DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina2");
				setDisciplinaIncluirVO(disciplina);
				validarDadosImpactoMatriculaFormadaInclusaoGradeDisciplina(getGradeDisciplinaVO());
				if (getListaLogImpactoGradeDisciplinaVOs().isEmpty()) {
					adicionarGradeDisciplina();
				}
			} else {
				DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina2");
				setDisciplinaIncluirVO(disciplina);
				adicionarGradeDisciplina();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarGradeDisciplina() throws Exception {
		try {
			setMensagemDetalhada("");
//			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina2");
			getGradeDisciplinaVO().setDisciplina(getDisciplinaIncluirVO());
			getGradeDisciplinaVO().setModalidadeDisciplina(getDisciplinaIncluirVO().getModalidadeDisciplina());
			getGradeDisciplinaVO().setDisciplinaTCC(getDisciplinaIncluirVO().getClassificacaoDisciplina().equals(ClassificacaoDisciplinaEnum.TCC));
			getGradeDisciplinaVO().setDisciplinaEstagio(getDisciplinaIncluirVO().getClassificacaoDisciplina().equals(ClassificacaoDisciplinaEnum.ESTAGIO));
			getGradeDisciplinaVO().setListaLogImpactoGradeDisciplinaVOs(getListaLogImpactoGradeDisciplinaVOs());
			if (getPeriodoLetivoVO().getCodigo().intValue() != 0) {
				getGradeDisciplinaVO().setPeriodoLetivo(getPeriodoLetivoVO().getCodigo());
			}
			getFacadeFactory().getGradeCurricularFacade().realizarVerificacaoDisciplinaJaAdicionada(getGradeCurricularVO(), getDisciplinaIncluirVO());	
			if (getMensagemDetalhada().equals("")) {
				getFacadeFactory().getGradeDisciplinaFacade().validarDadosFormulaCalculoComposicao(getGradeDisciplinaVO());
				getGradeDisciplinaVO().setOrdem(obterMaiorOrdemListagem(getPeriodoLetivoVO().getGradeDisciplinaVOs()) + 1);
				getPeriodoLetivoVO().adicionarObjGradeDisciplinaVOs(getGradeDisciplinaVO(), getCursoVO().getRegime());
				setMensagemID("msg_dados_adicionados");
				//Ordenacao.ordenarLista(getPeriodoLetivoVO().getGradeDisciplinaVOs(), "ordenacao");

			}
			getGradeCurricularVO().setTotalCargaHorariaDisciplinasObrigatorias(null);
			setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
			// setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getCursoVO().getCargaHoraria(),
			// getCursoVO().getCreditos(), getCursoVO().getRegime()));
			this.setGradeDisciplinaVO(new GradeDisciplinaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer obterMaiorOrdemListagem(List<GradeDisciplinaVO> lista) {
		Integer ordem = 0;
		if (!lista.isEmpty()) {			
			Iterator i = lista.iterator();
			while (i.hasNext()) {
				GradeDisciplinaVO grade = (GradeDisciplinaVO)i.next();
				if (grade.getOrdem().intValue() > ordem) {
					ordem = grade.getOrdem();
				}
			}
		}
		return ordem;
	}

	public void aumentarOrdemObjetoOrdemOuInferior(Integer ordemParam) {
		if (!getPeriodoLetivoVO().getGradeDisciplinaVOs().isEmpty()) {			
			Iterator i = getPeriodoLetivoVO().getGradeDisciplinaVOs().iterator();
			while (i.hasNext()) {
				GradeDisciplinaVO grade = (GradeDisciplinaVO)i.next();
				if (grade.getOrdem().intValue() == ordemParam) {
					grade.setOrdem(grade.getOrdem() + 1);
					break;
				}
			}
		}
	}

	public void diminuirOrdemObjetoOrdemOuInferior(Integer ordemParam) {
		if (!getPeriodoLetivoVO().getGradeDisciplinaVOs().isEmpty()) {			
			Iterator i = getPeriodoLetivoVO().getGradeDisciplinaVOs().iterator();
			while (i.hasNext()) {
				GradeDisciplinaVO grade = (GradeDisciplinaVO)i.next();
				if (grade.getOrdem().intValue() == ordemParam) {
					grade.setOrdem(grade.getOrdem() - 1);
					break;
				}
			}
		}
	}

	public void reorganizarOrdemListagem(Integer ordemParam) {
		if (!getPeriodoLetivoVO().getGradeDisciplinaVOs().isEmpty()) {			
			Iterator i = getPeriodoLetivoVO().getGradeDisciplinaVOs().iterator();
			while (i.hasNext()) {
				GradeDisciplinaVO grade = (GradeDisciplinaVO)i.next();
				if (grade.getOrdem().intValue() >= ordemParam) {
					grade.setOrdem(grade.getOrdem() - 1);
				}
			}
		}
	}

	public void moverDisciplinaPraCima() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		aumentarOrdemObjetoOrdemOuInferior(obj.getOrdem() - 1);
		obj.setOrdem(obj.getOrdem() - 1);
		Ordenacao.ordenarLista(getPeriodoLetivoVO().getGradeDisciplinaVOs(), "ordem");
	}

	public void moverDisciplinaPraBaixo() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		diminuirOrdemObjetoOrdemOuInferior(obj.getOrdem() + 1);
		obj.setOrdem(obj.getOrdem() + 1);
		Ordenacao.ordenarLista(getPeriodoLetivoVO().getGradeDisciplinaVOs(), "ordem");
	}

	public void configurarPreRequisito() throws Exception {
		try {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
			setPeriodoLetivoVO(obj);
			setExistePreRequisito(Boolean.FALSE);
			setCodigoCursoGradeDisciplina(0);
			setGradeDisciplinaVO(new GradeDisciplinaVO());
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			montarListaDisciplinasAnteriores();
			montarListaDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("Erro ao configurar o Pré Requisito");
		}
	}

	public void configurarGrupoOptativa() {
		try {
			setPeriodoLetivoVO(new PeriodoLetivoVO());
			PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
			setPeriodoLetivoVO(obj);
			if (getGradeCurricularVO().isNovoObj() && !getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs().isEmpty()) {
				throw new Exception("Os dados de Grupo Optativa devem ser gravados.");
			}
			setListaSelectItemGradeCurricularGrupoOptativa(null);
			setOnCompleteConfigurarGrupoOptativa("RichFaces.$('panelGrupoOptativaPeriodo').show();");
		} catch (Exception e) {
			setOnCompleteConfigurarGrupoOptativa("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaDisciplina() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		Iterator j = getPeriodoLetivoVO().getGradeDisciplinaVOs().iterator();
		while (j.hasNext()) {
			GradeDisciplinaVO item = (GradeDisciplinaVO) j.next();
			objs.add(new SelectItem(item.getDisciplina().getCodigo(), item.getDisciplina().getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinaGrade(objs);
	}

	public void montarListaDisciplinasAnteriores() {
		List<Integer> codidgoDisciplinaRequisitos = new ArrayList<Integer>(0);
		for (DisciplinaPreRequisitoVO pre : getGradeDisciplinaVO().getDisciplinaRequisitoVOs()) {
			codidgoDisciplinaRequisitos.add(pre.getDisciplina().getCodigo().intValue());
		}
		List<DisciplinaVO> listaDisciplina = new ArrayList<DisciplinaVO>(0);
		Iterator i = getGradeCurricularVO().getPeriodoLetivosVOs().iterator();
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			if (obj.getPeriodoLetivo().intValue() < getPeriodoLetivoVO().getPeriodoLetivo().intValue()) {
				Iterator j = obj.getGradeDisciplinaVOs().iterator();
				while (j.hasNext()) {
					GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) j.next();
					if (!codidgoDisciplinaRequisitos.contains(gradeDisciplina.getDisciplina().getCodigo().intValue())) {
						listaDisciplina.add(gradeDisciplina.getDisciplina());
					}
					if(gradeDisciplina.getDisciplinaComposta()){
						for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplina.getGradeDisciplinaCompostaVOs()){
							if (!codidgoDisciplinaRequisitos.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue())) {
								listaDisciplina.add(gradeDisciplinaCompostaVO.getDisciplina());
							}
						}
					}
				}
			}
		}
		Ordenacao.ordenarLista(listaDisciplina, "nome");
		setListaDisciplinasAnteriores(listaDisciplina);
		codidgoDisciplinaRequisitos = null;
	}
	
	public void montarListaCompostaDisciplinasAnteriores() {
		List<Integer> codidgoDisciplinaRequisitos = new ArrayList<Integer>(0);
		for (DisciplinaPreRequisitoVO pre : getGradeDisciplinaCompostaAuxiliar().getDisciplinaRequisitoVOs()) {
			codidgoDisciplinaRequisitos.add(pre.getDisciplina().getCodigo().intValue());
		}
		List<DisciplinaVO> listaDisciplina = new ArrayList<DisciplinaVO>(0);
		Iterator i = getGradeCurricularVO().getPeriodoLetivosVOs().iterator();
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			if (obj.getPeriodoLetivo().intValue() < getPeriodoLetivoVO().getPeriodoLetivo().intValue()) {
				Iterator j = obj.getGradeDisciplinaVOs().iterator();
				while (j.hasNext()) {
					GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) j.next();
					if (!codidgoDisciplinaRequisitos.contains(gradeDisciplina.getDisciplina().getCodigo().intValue())) {
						listaDisciplina.add(gradeDisciplina.getDisciplina());
					}
					if(gradeDisciplina.getDisciplinaComposta()){
						for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO:gradeDisciplina.getGradeDisciplinaCompostaVOs()){
							if (!codidgoDisciplinaRequisitos.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue())) {
								listaDisciplina.add(gradeDisciplinaCompostaVO.getDisciplina());
							}
						}
					}
				}
			}
		}
		Ordenacao.ordenarLista(listaDisciplina, "nome");
		setListaDisciplinasAnteriores(listaDisciplina);
		codidgoDisciplinaRequisitos = null;
	}
	
	public void verificarExistenciaPreRequisito() {
		Iterator i = getListaDisciplinasAnteriores().iterator();
		int index = 0;
		while (i.hasNext()) {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
			Iterator j = getGradeDisciplinaVO().getDisciplinaRequisitoVOs().iterator();
			while (j.hasNext()) {
				DisciplinaPreRequisitoVO preRequisito = (DisciplinaPreRequisitoVO) j.next();
				if (obj.getDisciplina().getCodigo().intValue() == preRequisito.getDisciplina().getCodigo()) {
					obj.getDisciplina().setPreRequisito(Boolean.TRUE);
					getListaDisciplinasAnteriores().set(index, obj.getDisciplina());
				}else if(obj.getDisciplinaComposta()){
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: obj.getGradeDisciplinaCompostaVOs()){
						if (gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue() == preRequisito.getDisciplina().getCodigo()) {
							gradeDisciplinaCompostaVO.getDisciplina().setPreRequisito(Boolean.TRUE);
							getListaDisciplinasAnteriores().set(index, gradeDisciplinaCompostaVO.getDisciplina());
						}
					}
				}
			}
			index++;
		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>GradeDisciplina</code> do objeto <code>gradeCurricularVO</code> da
	 * classe <code>GradeCurricular</code>
	 */
	public void removerGradeDisciplina() throws Exception {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		try {
			if (getValidarAlteracaoMatrizCurricularAtivaInativa()) {
				if (Uteis.isAtributoPreenchido(obj)) {
					if (obj.getDisciplinaComposta()) {
						setMensagemDetalhada("msg_erro", "Não é possível realizar EXCLUSÃO para uma Disciplina Composta.", Uteis.ERRO);
						setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide();");
						return;
					}
					setMensagemImpactoAlteracaoMatrizcurricularApresentada(false);
					obj.setDeveValidarImpactoExclusao(true);
				}
				validarDadosImpactoExclusaoGradeDisciplina(obj != null ? obj : getGradeDisciplinaExcluirVO());
				if (getPossuiImpactoHistoricoExclusaoGradeDisciplina() && !getMensagemImpactoAlteracaoMatrizcurricularApresentada()) {
					setMensagemID("msg_dados_editar");
					return;
				}
			}
			reorganizarOrdemListagem(obj != null ? obj.getOrdem() : getGradeDisciplinaExcluirVO().getOrdem());
			getFacadeFactory().getPeriodoLetivoFacade().excluirObjGradeDisciplinaVOs(obj != null ? obj : getGradeDisciplinaExcluirVO(), getPeriodoLetivoVO(), getValidarAlteracaoMatrizCurricularAtivaInativa(), getUsuarioLogado());			
			getGradeCurricularVO().setTotalCargaHorariaDisciplinasObrigatorias(null);
			setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
			setErroExclusaoGradeDisciplinaMatrizAtivaInativa(false);
			// setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getCursoVO().getCargaHoraria(),
			// getCursoVO().getCreditos(), getCursoVO().getRegime()));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			if (getValidarAlteracaoMatrizCurricularAtivaInativa() && !getMensagemImpactoAlteracaoMatrizcurricularApresentada()) {
				getGradeDisciplinaExcluirVO().setDeveValidarImpactoExclusao(true);
			}
			setErroExclusaoGradeDisciplinaMatrizAtivaInativa(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplinaPreRequisito() throws Exception {
		try {
			DisciplinaVO disciplinaVO = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaAnteriorItens");
			adicionarDisciplinaPreRequisito(disciplinaVO);
			montarListaDisciplinasAnteriores();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplinaPreRequisito(DisciplinaVO disciplinaAnterior) throws Exception {
		try {
			setMensagemDetalhada("");
			if (getGradeDisciplinaVO().getDisciplina().getCodigo().intValue() != 0) {
				if (!getGradeDisciplinaVO().getCodigo().equals(0)) {
					disciplinaPreRequisitoVO.setGradeDisciplina(getGradeDisciplinaVO().getCodigo());
				}
				getDisciplinaPreRequisitoVO().getDisciplina().setCodigo(disciplinaAnterior.getCodigo());
				if (getDisciplinaPreRequisitoVO().getDisciplina().getCodigo().intValue() != 0) {
					Integer campoConsulta = getDisciplinaPreRequisitoVO().getDisciplina().getCodigo();
					DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					getDisciplinaPreRequisitoVO().setDisciplina(disciplina);
				}
				getGradeDisciplinaVO().adicionarObjDisciplinaPreRequisitoVOs(getDisciplinaPreRequisitoVO());
				this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
				Ordenacao.ordenarLista(getGradeDisciplinaVO().getDisciplinaRequisitoVOs(), "ordenacao");
				setMensagemID("msg_dados_adicionados");
			} else {
				setMensagemDetalhada("O campo DISCIPLINA deve ser informado.");
			}
			setExistePreRequisito(Boolean.TRUE);
		} catch (Exception e) {
			this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DisciplinaPreRequisito</code> do objeto <code>disciplinaVO</code>
	 * da classe <code>Disciplina</code>
	 */
	public void removerDisciplinaPreRequisito() throws Exception {
		DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) context().getExternalContext().getRequestMap().get("disciplinaPreRequisitoItens");
		getFacadeFactory().getGradeDisciplinaFacade().excluirObjDisciplinaPreRequisitoVOs(obj.getDisciplina().getCodigo(), getGradeDisciplinaVO(), getUsuarioLogado());
		montarListaDisciplinasAnteriores();
		setMensagemID("msg_dados_excluidos");
	}

	public void adicionarPeriodoLetivo() {
		try {
			if (cursoVO.getPeriodicidade() == null || cursoVO.getPeriodicidade().equals("")) {
				setMensagemDetalhada("O campo PERIODICIDADE (Curso) deve ser informado");
				return;
			}
			if (this.cursoVO.getNrPeriodoLetivo().equals(0)) {
				setMensagemDetalhada("O campo NÚMERO PERÍODO LETIVO (Curso) deve ser informado");
				return;
			}
			if ((getGradeCurricularVO().getSituacao().equals("AT") && !getPossuiPermissaoAlterarMatrizCurricularAtivaInativa()) || getGradeCurricularVO().getSituacao().equals("DE")) {
				setMensagemDetalhada("A GRADE CURRICULAR selecionada não pode ser alterada");
				return;
			}
			int contador = 1;
			boolean periodoExiste;
			while (contador <= getCursoVO().getNrPeriodoLetivo().intValue()) {
				periodoExiste = false;
				if (getGradeCurricularVO().getPeriodoLetivosVOs() != null && !getGradeCurricularVO().getPeriodoLetivosVOs().isEmpty()) {
					for (PeriodoLetivoVO periodoLetivoVO : getGradeCurricularVO().getPeriodoLetivosVOs()) {
						if (periodoLetivoVO.getPeriodoLetivo().equals(contador)) {
							periodoExiste = true;
							break;
						}
					}
				}
				if (!periodoExiste) {
					PeriodoLetivoVO novoPeriodoLetivo = new PeriodoLetivoVO();
					if (getGradeCurricularVO().getCodigo().intValue() != 0) {
						novoPeriodoLetivo.setGradeCurricular(getGradeCurricularVO().getCodigo());
					}
					novoPeriodoLetivo.setPeriodoLetivo(new Integer(contador));
					String periodo = getCursoVO().getPeriodicidade_Apresentar();
					if (periodo.equals("Integral")) {
						novoPeriodoLetivo.setDescricao(periodo);
					} else if (periodo.equals("Anual")) {
						novoPeriodoLetivo.setDescricao(String.valueOf(contador) + "º Ano");
					} else if (periodo.equals("Semestral")) {
						novoPeriodoLetivo.setDescricao(String.valueOf(contador) + "º Semestre");
					}
					getGradeCurricularVO().adicionarObjPeriodoLetivoVOs(novoPeriodoLetivo);
//					setPeriodoLetivoVO(new PeriodoLetivoVO());
				}
				contador++;
			}
			Ordenacao.ordenarLista(getGradeCurricularVO().getPeriodoLetivosVOs(), "periodoLetivo");
			setMensagemID("msg_dados_adicionados");

		} catch (Exception e) {
			setPeriodoLetivoVO(new PeriodoLetivoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerPeriodoLetivo() throws Exception {
		PeriodoLetivoVO periodoLetivo = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
		getGradeCurricularVO().getPeriodoLetivosVOs().remove(periodoLetivo);
		setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
		// setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getCursoVO().getCargaHoraria(),
		// getCursoVO().getCreditos(), getCursoVO().getRegime()));
		recontarNumeroPeriodoLetivo();
	}

	/**
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoGradeCurricular() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoGradeCurriculars = (Hashtable) Dominios.getSituacaoGradeCurricular();
		Enumeration keys = situacaoGradeCurriculars.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoGradeCurriculars.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>CursoTurno</code> para o objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public void adicionarCursoTurno() throws Exception {
		try {
			if (!getCursoVO().getCodigo().equals(0)) {
				cursoTurnoVO.setCurso(getCursoVO().getCodigo());
			}
			if (getCursoTurnoVO().getTurno().getCodigo().intValue() != 0) {
				Integer campoConsulta = getCursoTurnoVO().getTurno().getCodigo();
				TurnoVO turno = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getCursoTurnoVO().setTurno(turno);
			}
			getCursoVO().adicionarObjCursoTurnoVOs(getCursoTurnoVO());
			this.setCursoTurnoVO(new CursoTurnoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>CursoTurno</code> para edição pelo usuário.
	 */
	public String editarCursoTurno() throws Exception {
		CursoTurnoVO obj = (CursoTurnoVO) context().getExternalContext().getRequestMap().get("cursoTurnoItem");
		setCursoTurnoVO(obj);
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>CursoTurno</code> do objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public void removerCursoTurno() throws Exception {
		CursoTurnoVO obj = (CursoTurnoVO) context().getExternalContext().getRequestMap().get("cursoTurnoItem");
		getCursoVO().excluirObjCursoTurnoVOs(obj.getTurno().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	// Alberto 13/12/10 Acrescentado coordenador curso
	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>CursoCoordenador</code> para o objeto <code>cursoVO</code> da
	 * classe <code>Curso</code>
	 */

	public void adicionarCursoCoordenador() throws Exception {
		try {
			getFacadeFactory().getCursoCoordenadorFacade().validarDados(getCursoCoordenadorVO(), getUsuarioLogado());
			if (!getCursoVO().getCodigo().equals(0)) {
				getCursoCoordenadorVO().getCurso().setCodigo(getCursoVO().getCodigo());
			}
			if (getEditandoCursoCoordenador() && (!getCursoCoordenadorVO().getFuncionario().getCodigo().equals(getFuncionarioVO().getCodigo()))) {
				CursoCoordenadorVO obj = new CursoCoordenadorVO();
				obj.setFuncionario(getFuncionarioVO());
				getListaCursoCoordenadorAux().add(obj);
			}
			getCursoVO().adicionarObjCursoCoordenadorVOs(getCursoCoordenadorVO());
			setEditandoCursoCoordenador(false);
			this.setCursoCoordenadorVO(new CursoCoordenadorVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>CursoCoordenador</code> para edição pelo usuário.
	 */
	public void editarCursoCoordenador() {
		try {
			CursoCoordenadorVO obj = (CursoCoordenadorVO) context().getExternalContext().getRequestMap().get("cursoCoordenadorItem");
			setCursoCoordenadorVO((CursoCoordenadorVO) obj.clone());
			setFuncionarioVO(obj.getFuncionario());
			setEditandoCursoCoordenador(true);			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getCursoCoordenadorVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getCursoCoordenadorVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getCursoCoordenadorVO().setUnidadeEnsino(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getCursoCoordenadorVO().getUnidadeEnsino().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void preencherUnidadeEnsino() {
		try {
			getCursoCoordenadorVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getCursoCoordenadorVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			setMensagemID("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>CursoCoordenador</code> do objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public void removerCursoCoordenador() throws Exception {
		CursoCoordenadorVO obj = (CursoCoordenadorVO) context().getExternalContext().getRequestMap().get("cursoCoordenadorItem");
		if (obj.getCodigo() != 0) {
			getListaCursoCoordenadorAux().add(obj);
		}
		getCursoVO().excluirObjCursoCoordenadorVOs(obj);
		setMensagemID("msg_dados_excluidos");
	}

	public void excluirCursoCoordenadores() throws Exception {
		if (!getListaCursoCoordenadorAux().isEmpty()) {
			for (CursoCoordenadorVO obj : getListaCursoCoordenadorAux()) {
				getFacadeFactory().getCursoCoordenadorFacade().excluir(obj.getCodigo(), getUsuarioLogado());
				if (!getFacadeFactory().getCursoCoordenadorFacade().validarExisteCoordenadorMaisDeUmCurso(obj.getFuncionario().getPessoa().getCodigo(), getUsuarioLogado())) {
					getFacadeFactory().getPessoaFacade().alterarCoordenador(obj.getFuncionario().getPessoa().getCodigo(), false);
				}
			}
		}
	}

	// Alberto 13/12/10 Acrescentado coordenador curso
	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>DocumentacaoCurso</code> para o objeto <code>cursoVO</code> da
	 * classe <code>Curso</code>
	 */

	public void adicionarDocumentacaoCurso() throws Exception {
		try {
			if (!getCursoVO().getCodigo().equals(0)) {
				documentacaoCursoVO.setCurso(getCursoVO().getCodigo());
			}
			getDocumentacaoCursoVO().setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(getDocumentacaoCursoVO().getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getCursoVO().adicionarObjDocumentacaoCursoVOs(getDocumentacaoCursoVO());
			this.setDocumentacaoCursoVO(new DocumentacaoCursoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DocumentacaoCurso</code> para edição pelo usuário.
	 */
	public void editarDocumentacaoCurso() throws Exception {
		DocumentacaoCursoVO obj = (DocumentacaoCursoVO) context().getExternalContext().getRequestMap().get("documentacaoCursoItem");
		setDocumentacaoCursoVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DocumentacaoCurso</code> do objeto <code>cursoVO</code> da classe
	 * <code>Curso</code>
	 */
	public void removerDocumentacaoCurso() throws Exception {
		DocumentacaoCursoVO obj = (DocumentacaoCursoVO) context().getExternalContext().getRequestMap().get("documentacaoCursoItem");
		getCursoVO().excluirObjDocumentacaoCursoVOs(obj.getTipoDeDocumentoVO().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public Boolean getValidaPeriodicidade() {
		if (getCursoVO().getPeriodicidade_Apresentar().equals("Integral")) {
			getCursoVO().setNrPeriodoLetivo(new Integer(1));
			return true;
		}
		return false;
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Turno</code>.
	 */
	public void montarListaSelectItemTurno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTurnoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurnoVO obj = (TurnoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turno</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemTipoDeDocumentoDocumentacaoCurso() throws Exception {
		List resultadoConsulta = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
		setListaSelectItemTipoDeDocumento(lista);
	}

	public void montarListaSelectItemTipoAtividadeComplementar() throws Exception {
		List resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade().consultar(false, getUsuarioLogado());
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
		setListaSelectItemTipoAtividadeComplementar(lista);
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>regime</code>
	 */
	public List getListaSelectItemRegimeCurso() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable regimeCursos = (Hashtable) Dominios.getRegimeCurso();
		Enumeration keys = regimeCursos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) regimeCursos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>regimeAprovacao</code>
	 */
	public List getListaSelectItemRegimeAprovacaoCurso() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable regimeAprovacaoCursos = (Hashtable) Dominios.getRegimeAprovacaoCurso();
		Enumeration keys = regimeAprovacaoCursos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) regimeAprovacaoCursos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>periodicidade</code>
	 */
	public List getListaSelectItemPeriodicidadeCurso() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable periodicidadeCursos = (Hashtable) Dominios.getPeriodicidadeCurso();
		Enumeration keys = periodicidadeCursos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) periodicidadeCursos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>nivelEducacional</code>
	 */
	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			getListaNivelEducacional().add(item);
		}
		// setListaNivelEducacional(opcoes);
		// List objs = new ArrayList(0);
		// objs.add(new SelectItem("", ""));
		// Hashtable nivelEducacionalCursos = (Hashtable)
		// Dominios.getNivelEducacionalCurso();
		// Enumeration keys = nivelEducacionalCursos.keys();
		// while (keys.hasMoreElements()) {
		// String value = (String) keys.nextElement();
		// String label = (String) nivelEducacionalCursos.get(value);
		// objs.add(new SelectItem(value, label));
		// }
		// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		// return objs;
	}

	public List getListaSelectItemNivelEducacionalCurso() throws Exception {
		listaNivelEducacional = new ArrayList<>();
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			getListaNivelEducacional().add(item);
		}
		return listaNivelEducacional;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>Título</code>
	 */

	public void montarListaTituloCurso() throws Exception {
		if (!(getCursoVO().getNivelEducacional().equals("")) && (!getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())) && !getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.SEQUENCIAL.getValor()) && !getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.EXTENSAO.getValor())) {
			List<SelectItem> opcoes = new ArrayList<SelectItem>();
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.PROFISSIONALIZANTE.getValor())) {
				opcoes.add(new SelectItem(TituloCursoSuperior.TECNICO.getValor(), TituloCursoSuperior.TECNICO.getDescricao()));
			}
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())) {
				opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TituloCursoMedio.class, false);
			}
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())) {
				opcoes = montaListaExcluindoValor(TituloCursoSuperior.TECNICO);
			}
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
				opcoes.add(new SelectItem(TituloCursoPos.LATO_SENSU.getValor(), TituloCursoPos.LATO_SENSU.getDescricao()));
				opcoes.add(new SelectItem(TituloCursoPos.RESIDENCIA_MEDICA.getValor(), TituloCursoPos.RESIDENCIA_MEDICA.getDescricao()));
				opcoes.add(new SelectItem(TituloCursoPos.STRICTO_SENSU.getValor(), TituloCursoPos.STRICTO_SENSU.getDescricao()));
			}
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.MESTRADO.getValor())) {
				opcoes.add(new SelectItem(TituloCursoPos.STRICTO_SENSU.getValor(), TituloCursoPos.STRICTO_SENSU.getDescricao()));				
			}
			if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
				opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TituloCursoSuperior.class, false);
			}
			setListaTituloCurso(opcoes);
			setExisteTitulo(true);
		} else {
			setExisteTitulo(false);
			getCursoVO().setTitulo("");
			setListaTituloCurso(new ArrayList(0));
		}
		definirPeriodicidadeCursoDeAcordoNivelEducacional();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>ConfiguracaoAcademico</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>ConfiguracaoAcademico</code>. Esta
	 * rotina não recebe parâmetros para filtragem de dados, isto é importante
	 * para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarTodasConfiguracaoAcademica(Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			getListaSelectItemConfiguracaoAcademico().clear();
			getListaSelectItemConfiguracaoAcademico().add(new SelectItem(0, ""));
			for (ConfiguracaoAcademicoVO configuracaoAcademicoVO : resultadoConsulta) {
				getListaSelectItemConfiguracaoAcademico().add(new SelectItem(configuracaoAcademicoVO.getCodigo(), configuracaoAcademicoVO.getCodigo() + " - " + configuracaoAcademicoVO.getNome()));	
			}
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());

		}
	}
	
	public void montarListaSelectItemConfiguracaoTCC() {
		try {
			getListaSelectItemConfiguracaoTCC().clear();
			List<ConfiguracaoTCCVO> configuracaoTCCVOs = getFacadeFactory().getConfiguracaoTCCFacade().consultarPorDescricao("", Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			getListaSelectItemConfiguracaoTCC().add(new SelectItem(0, ""));
			for (ConfiguracaoTCCVO configuracaoTCCVO : configuracaoTCCVOs) {
				getListaSelectItemConfiguracaoTCC().add(new SelectItem(configuracaoTCCVO.getCodigo(), configuracaoTCCVO.getDescricao()));
			}
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());

		}
	}

	public Boolean getIsApresentarGradeDisciplinaParteDiversificada() {
		return getCursoVO().getUtilizaDisciplinaParteDiversificada();
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>AreaConhecimento</code>.
	 */
	public void montarListaSelectItemAreaConhecimento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAreaConhecimentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AreaConhecimentoVO obj = (AreaConhecimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemAreaConhecimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>AreaConhecimento</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>AreaConhecimento</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemAreaConhecimento() {
		try {
			montarListaSelectItemAreaConhecimento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemAreaConhecimento();
		montarListaSelectItemConfiguracaoAcademico();
		montarListaSelectItemTurno();
		montarListaSelectItemTipoDeDocumentoDocumentacaoCurso();
		montarListaSelectItemNivelEducacionalCurso();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTipoAtividadeComplementar();
		montarListaSelectItemQuestionario();
		montarListaSelectItemGrupoPessoa();
		montarListaSelectItemConfiguracaoLdap();
		montarListaSelectItemEixoCurso();
		montarListaSelectConteudoMasterBlackboard();
		montarListaSelectItemTextoPadraoContratoMatriculaCalouro();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nrRegistroInterno", "Número (Registro Interno)"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
		itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoCons");
	}

	public void ativarGradeCurricular() {
		try {
			setMensagemDetalhada(getGradeCurricularVO().validarTotalCargaHorariaCredito(getGradeCurricularVO().getCargaHoraria(), getGradeCurricularVO().getCreditos(), getCursoVO().getRegime()));
			if (!getGradeCurricularVO().getSituacao().equals("AT")) {
				GradeCurricularVO.validarDadosAtivarGrade(getGradeCurricularVO());
				getFacadeFactory().getGradeCurricularFacade().ativarGrade(getGradeCurricularVO(), getUsuarioLogado());
				getGradeCurricularVO().setSituacao("AT");
				getGradeCurricularVO().setDataAtivacao(new Date());
				getGradeCurricularVO().setResponsavelAtivacao(getUsuarioLogadoClone());
//				persistirGradeCurricular();
				getFacadeFactory().getGradeCurricularFacade().persistirAtivacaoGradeCurricular(getGradeCurricularVO(), getUsuarioLogado());
				Ordenacao.ordenarLista(getCursoVO().getGradeCurricularVOs(), "situacao");
				setGradeCurricularVO(new GradeCurricularVO());
				setMensagemID("msg_GradeCurricular_ativacaoGrade");
			} else {
				setMensagemDetalhada("Esta GRADE CURRICULAR não pode voltar a ser ATIVA pois ela esta " + getGradeCurricularVO().getSituacao_Apresentar().toUpperCase());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void desativarGradeCurricular() {
		try {
			GradeCurricularVO grade = (GradeCurricularVO) context().getExternalContext().getRequestMap().get("gradeCurricularItem");
			getFacadeFactory().getGradeCurricularFacade().realizarMongagemDadosEmGradeCurricularExistente(grade, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setGradeCurricularVO(grade);
			if (getGradeCurricularVO().getSituacao().equals("AT")) {
				getFacadeFactory().getGradeCurricularFacade().desativarGrade(getGradeCurricularVO(), getUsuarioLogado());
				getGradeCurricularVO().setSituacao("IN");
				getGradeCurricularVO().setDataDesativacao(new Date());
				getGradeCurricularVO().setResponsavelDesativacao(getUsuarioLogadoClone());
				getGradeCurricularVO().setDataFinalVigencia(new Date());
//				persistirGradeCurricular();
				getFacadeFactory().getGradeCurricularFacade().persistirDesativacaoGradeCurricular(getGradeCurricularVO(), getUsuarioLogado());
				Ordenacao.ordenarLista(getCursoVO().getGradeCurricularVOs(), "situacao");
				setGradeCurricularVO(new GradeCurricularVO());
				setMensagemID("msg_GradeCurricular_desativadaGrade");
			} else {
				setMensagemDetalhada("Para INATIVAR uma Grade Curricular ela deve estar ATIVA");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarConsultarFuncionarioCoordenador() {
		try {
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
			setIdentificacaoConsultaFuncionario("FUNCIONARIO_COORDENADOR");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarConsultarFuncionarioResponsavelAssinaturaTermoEstagioVO() {
		try {
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
			getListaConsultaFuncionario().clear();
			setIdentificacaoConsultaFuncionario("FUNCIONARIO_RESPONSAVEL_ASSINATURA_TERMO_ESTAGIO");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setListaConsulta(new ArrayList(0));
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioRespPreInscricao() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		switch (getIdentificacaoConsultaFuncionario()) {
		case "FUNCIONARIO_COORDENADOR":
			getCursoCoordenadorVO().setFuncionario(obj);
			break;
		case "FUNCIONARIO_RESPONSAVEL_ASSINATURA_TERMO_ESTAGIO":
			getCursoVO().setFuncionarioResponsavelAssinaturaTermoEstagioVO(obj);
			break;
		default:
			break;
		}
	}
	
	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
	    }

	public void selecionarFuncionarioRespPreInscricao() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario2");
		getCursoVO().setFuncionarioRespPreInscricao(obj);
	}

	public void limparDadosFuncionario() {
		getCursoCoordenadorVO().setFuncionario(new FuncionarioVO());
	}
	
	public void limparDadosFuncionarioResponsavelAssinaturaTermoEstagioVO() {
		getCursoVO().setFuncionarioResponsavelAssinaturaTermoEstagioVO(new FuncionarioVO());
	}

	public void limparDadosFuncionarioRespPreInscricao() {
		getCursoVO().setFuncionarioRespPreInscricao(new FuncionarioVO());
	}

	public void limparConsultaFuncionario() {
		getListaConsultaFuncionario().clear();
	}

	public void limparConsultaFuncionarioRespPreInscricao() {
		getListaConsultaFuncionario().clear();
	}

	public void limparConsultaTurma() {
		getListaConsultaTurma().clear();
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
		getCursoCoordenadorVO().setTurma(obj);
	}

	public void limparDadosTurma() {
		getCursoCoordenadorVO().setTurma(new TurmaVO());
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		return itens;
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public List getListaConsultaDisciplina() {
		if(listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>();
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public List getListaSelectItemTurno() {
		return (listaSelectItemTurno);
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public CursoTurnoVO getCursoTurnoVO() {
		if(this.cursoTurnoVO == null){
			this.cursoTurnoVO = new CursoTurnoVO();
		}
		return cursoTurnoVO;
	}

	public void setCursoTurnoVO(CursoTurnoVO cursoTurnoVO) {
		this.cursoTurnoVO = cursoTurnoVO;
	}

	public DocumentacaoCursoVO getDocumentacaoCursoVO() {
		if(this.documentacaoCursoVO == null){
			this.documentacaoCursoVO = new DocumentacaoCursoVO();
		}
		return documentacaoCursoVO;
	}

	public void setDocumentacaoCursoVO(DocumentacaoCursoVO documentacaoCursoVO) {
		this.documentacaoCursoVO = documentacaoCursoVO;
	}

	public List getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemConfiguracaoAcademico);
	}

	public void setListaSelectItemConfiguracaoAcademico(List listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public List getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemAreaConhecimento);
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			setCursoVO(new CursoVO());
		}
		return cursoVO;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if(this.gradeCurricularVO == null){
			this.gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String getCampoConsultaDisciplina() {
		if(this.campoConsultaDisciplina == null){
			this.campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(this.disciplinaVO == null){
			this.disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getValorConsultaDisciplina() {
		if(this.valorConsultaDisciplina == null){
			this.valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if(this.periodoLetivoVO == null){
			this.periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if(this.gradeDisciplinaVO == null){
			this.gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public List getListaSelectItemDisciplina() {
		if(this.listaSelectItemDisciplina == null){
			this.listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<DisciplinaVO> getListaDisciplinasAnteriores() {
		if(this.listaDisciplinasAnteriores == null){
			this.listaDisciplinasAnteriores = new ArrayList<DisciplinaVO>(0);
		}
		return listaDisciplinasAnteriores;
	}

	public void setListaDisciplinasAnteriores(List<DisciplinaVO> listaDisciplinasAnteriores) {
		this.listaDisciplinasAnteriores = listaDisciplinasAnteriores;
	}

	public Integer getItemGradeDisciplina() {
		if(this.itemGradeDisciplina == null){
			this.itemGradeDisciplina = 0;
		}
		return itemGradeDisciplina;
	}

	public void setItemGradeDisciplina(Integer itemGradeDisciplina) {
		this.itemGradeDisciplina = itemGradeDisciplina;
	}

	public DisciplinaPreRequisitoVO getDisciplinaPreRequisitoVO() {
		if(this.disciplinaPreRequisitoVO == null){
			this.disciplinaPreRequisitoVO = new DisciplinaPreRequisitoVO();
		}
		return disciplinaPreRequisitoVO;
	}

	public void setDisciplinaPreRequisitoVO(DisciplinaPreRequisitoVO disciplinaPreRequisitoVO) {
		this.disciplinaPreRequisitoVO = disciplinaPreRequisitoVO;
	}

	public String getControleAba() {
		if (controleAba == null) {
			controleAba = "cursoTab";
		}
		return controleAba;
	}

	public void setControleAba(String controleAba) {
		this.controleAba = controleAba;
	}

	public String getResponsavel_Erro() {
		if (responsavel_Erro == null) {
			responsavel_Erro = "";
		}
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public Integer getCodigoCursoGradeDisciplina() {
		if (codigoCursoGradeDisciplina == null) {
			codigoCursoGradeDisciplina = 0;
		}
		return codigoCursoGradeDisciplina;
	}

	public void setCodigoCursoGradeDisciplina(Integer codigoCursoGradeDisciplina) throws Exception {
		this.codigoCursoGradeDisciplina = codigoCursoGradeDisciplina;
		if (this.codigoCursoGradeDisciplina.intValue() != 0) {
			setGradeDisciplinaVO(getPeriodoLetivoVO().consultarObjGradeDisciplinaVO(codigoCursoGradeDisciplina));
		} else {
			setGradeDisciplinaVO(new GradeDisciplinaVO());
		}
		if (getGradeDisciplinaVO().getDisciplinaRequisitoVOs().isEmpty()) {
			setExistePreRequisito(Boolean.FALSE);
			if (codigoCursoGradeDisciplina.intValue() != 0) {
				setMensagemDetalhada("Não foi encontrado nenhum Pré Requisito para a Disciplina " + getGradeDisciplinaVO().getDisciplina().getNome().toUpperCase());
			}
		} else {
			setExistePreRequisito(Boolean.TRUE);
			setMensagemDetalhada("");
		}
	}

	public List getListaSelectItemDisciplinaGrade() {
		if (listaSelectItemDisciplinaGrade == null) {
			listaSelectItemDisciplinaGrade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaGrade;
	}

	public void setListaSelectItemDisciplinaGrade(List listaSelectItemDisciplinaGrade) {
		this.listaSelectItemDisciplinaGrade = listaSelectItemDisciplinaGrade;
	}

	public Boolean getExistePreRequisito() {
		if (existePreRequisito == null) {
			existePreRequisito = Boolean.FALSE;
		}
		return existePreRequisito;
	}

	public void setExistePreRequisito(Boolean existePreRequisito) {
		this.existePreRequisito = existePreRequisito;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		cursoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemAreaConhecimento);
		Uteis.liberarListaMemoria(listaSelectItemConfiguracaoAcademico);
		Uteis.liberarListaMemoria(listaSelectItemDisciplina);
		documentacaoCursoVO = null;
		cursoTurnoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemTurno);
		gradeCurricularVO = null;
		gradeDisciplinaVO = null;
		disciplinaVO = null;
		Uteis.liberarListaMemoria(listaConsultaDisciplina);
		campoConsultaDisciplina = null;
		valorConsultaDisciplina = null;
		Uteis.liberarListaMemoria(listaDisciplinasAnteriores);
		disciplinaPreRequisitoVO = null;
	}

	/**
	 * @author rodrigo.arantes Metodo reponsavel por editar cada
	 *         AutorizacaoCursoVO, onde caso o vo escolhido para editar seja um
	 *         VO novo, o mesmo é retirado da lista da tela e incluido novamente
	 */
	public void editarAutorizacaoCurso() {
		try {
			AutorizacaoCursoVO obj = (AutorizacaoCursoVO) context().getExternalContext().getRequestMap().get("autorizacaoVOItens");
			// realizarExcluirAutorizacaoCurso(obj, 1);
			setAutorizacaoCurso(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Remove autorizacaoCurso da lista CursoVO.autorizacaoCursoVOs
	 */
	public void removerAutorizacaoCurso() {
		try {
			//AutorizacaoCursoVO obj = (AutorizacaoCursoVO) context().getExternalContext().getRequestMap().get("autorizacaoVOItens");
			
			realizarExcluirAutorizacaoCurso(getAutorizacaoCursoExcluir(), 0);

			if (getAutorizacaoCurso().getCodigo().equals(getAutorizacaoCurso().getCodigo())) {
				setAutorizacaoCursoExcluir(new AutorizacaoCursoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarMatriculaVinculadaAutorizacaoCurso() {
		try {
			setOncompleteModal("");
			AutorizacaoCursoVO obj = (AutorizacaoCursoVO) context().getExternalContext().getRequestMap().get("autorizacaoVOItens");
			setAutorizacaoCursoExcluir(obj); // AutorizacaoCurso a remover
			
			if (obj.getAutorizacaoUsadaPorMatricula()) {
				setListaMatriculaVinculadaAutorizacaoCurso(getFacadeFactory().getMatriculaFacade().consultaRapidaAutorizacaoCursoMatricula(obj.getCodigo(), null));
				setOncompleteModal("RichFaces.$('panelRemoverAutorizacaoCurso').show();");
			} else {
				setListaMatriculaVinculadaAutorizacaoCurso(null);
				removerAutorizacaoCurso();
				setOncompleteModal("RichFaces.$('panelRemoverAutorizacaoCurso').hide();");
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public List<MatriculaVO> getListaMatriculaVinculadaAutorizacaoCurso() {
		return listaMatriculaVinculadaAutorizacaoCurso;
	}

	public void setListaMatriculaVinculadaAutorizacaoCurso(List<MatriculaVO> listaMatriculaVinculadaAutorizacaoCurso) {
		if (listaMatriculaVinculadaAutorizacaoCurso == null) {
			this.listaMatriculaVinculadaAutorizacaoCurso = new ArrayList<MatriculaVO> (0);
		} 
			this.listaMatriculaVinculadaAutorizacaoCurso = listaMatriculaVinculadaAutorizacaoCurso;	
	}

	public void realizarExcluirAutorizacaoCurso(AutorizacaoCursoVO obj, Integer aux) throws Exception {
		Iterator i = getCursoVO().getAutorizacaoCursoVOs().iterator();
		while (i.hasNext()) {
			AutorizacaoCursoVO vo = (AutorizacaoCursoVO) i.next();
			if (vo.equals(obj)) {
				// caso o vo escolhido para remover tenha codigo, entao deve ser
				// tbm deletado do banco
				if (!vo.isNovoObj() && aux.intValue() == 0) {
					getFacadeFactory().getAutorizacaoCursoFacade().excluir(vo, getUsuarioLogado());
				}
				i.remove();
				break;
			}
		}
	}
	

	public void adicionarAutorizacaoCurso() throws Exception {
		try {
			getFacadeFactory().getCursoFacade().validarReconhecimentoCurso(getAutorizacaoCurso(), getCursoVO());
			realizarExcluirAutorizacaoCurso(getAutorizacaoCurso(), 1);
			getCursoVO().getAutorizacaoCursoVOs().add(getAutorizacaoCurso());
			setAutorizacaoCurso(new AutorizacaoCursoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerMaterialCurso() {
		try {
			MaterialCursoVO obj = (MaterialCursoVO) context().getExternalContext().getRequestMap().get("materialCursoVOItens");
			realizarExcluirMaterialCurso(obj, 0);
			if (getMaterialCurso().getCodigo().equals(obj.getCodigo())) {
				setMaterialCurso(new MaterialCursoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarExcluirMaterialCurso(MaterialCursoVO obj, Integer aux) throws Exception {
		Iterator i = getCursoVO().getMaterialCursoVOs().iterator();
		while (i.hasNext()) {
			MaterialCursoVO vo = (MaterialCursoVO) i.next();
			if (vo.equals(obj)) {
				// caso o vo escolhido para remover tenha codigo, entao deve ser
				// tbm deletado do banco
				if (!vo.isNovoObj() && aux.intValue() == 0) {
					getFacadeFactory().getMaterialCursoFacade().excluir(vo);
				}
				i.remove();
				break;
			}
		}
	}

	public void adicionarMaterialCurso() throws Exception {
		try {
			// getFacadeFactory().getCursoFacade().validarReconhecimentoCurso(getAutorizacaoCurso(),
			// getCursoVO());
			if (getMaterialCurso().getDescricao() == "") {
				throw new Exception("O campo DESCRIÇÃO (Material Curso) deve ser preenchido.");
			}
			realizarExcluirMaterialCurso(getMaterialCurso(), 1);
			getCursoVO().getMaterialCursoVOs().add(getMaterialCurso());
			setMaterialCurso(new MaterialCursoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarGradeCurricularTipoAtividadeComplementarVO() throws Exception {
		try {
			getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().adicionarGradeCurricularTipoAtividadeComplementarVOs(this.getGradeCurricularTipoAtividadeComplementarVO(), this.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs(),this.getGradeCurricularVO().getTotalCargaHorariaAtividadeComplementar(), true, getUsuarioLogado());
			this.setGradeCurricularTipoAtividadeComplementarVO(new GradeCurricularTipoAtividadeComplementarVO());
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerGradeCurricularTipoAtividadeComplementarVO() throws Exception {
		try {
			GradeCurricularTipoAtividadeComplementarVO obj = (GradeCurricularTipoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("gradeCurricularTipoAtividadeComplementarItens");
			if (!getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarSeExisteRegistroVinculadoATipoAtividadeComplementar(getGradeCurricularVO().getCodigo(), obj.getTipoAtividadeComplementarVO().getCodigo(), false, getUsuarioLogado())) {
				getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().removerGradeCurricularTipoAtividadeComplementarVOs(obj, this.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs());
			} else {
				setMensagemDetalhada(" A Atividade Complementar não pode ser REMOVIDA pois existem matrículas vinculadas a mesma.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaSelectItemTipoDeDocumento
	 */
	public List getListaSelectItemTipoDeDocumento() {
		if (listaSelectItemTipoDeDocumento == null) {
			listaSelectItemTipoDeDocumento = new ArrayList(0);
		}
		return listaSelectItemTipoDeDocumento;
	}

	/**
	 * @param listaSelectItemTipoDeDocumento
	 *            the listaSelectItemTipoDeDocumento to set
	 */
	public void setListaSelectItemTipoDeDocumento(List listaSelectItemTipoDeDocumento) {
		this.listaSelectItemTipoDeDocumento = listaSelectItemTipoDeDocumento;
	}

	/**
	 * @return the listaTituloCurso
	 */
	public List getListaTituloCurso() {
		if (listaTituloCurso == null) {
			setListaTituloCurso(new ArrayList(0));
		}
		return listaTituloCurso;
	}

	/**
	 * @param listaTituloCurso
	 *            the listaTituloCurso to set
	 */
	public void setListaTituloCurso(List listaTituloCurso) {
		this.listaTituloCurso = listaTituloCurso;
	}

	/**
	 * @return the existeTitulo
	 */
	public Boolean getExisteTitulo() {
		if (existeTitulo == null) {
			existeTitulo = Boolean.FALSE;
		}
		return existeTitulo;
	}

	/**
	 * @param existeTitulo
	 *            the existeTitulo to set
	 */
	public void setExisteTitulo(Boolean existeTitulo) {
		this.existeTitulo = existeTitulo;
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getIdentificacaoConsultaFuncionario() {
		if (identificacaoConsultaFuncionario == null) {
			identificacaoConsultaFuncionario = "";
		}
		return identificacaoConsultaFuncionario;
	}

	public void setIdentificacaoConsultaFuncionario(String identificacaoConsultaFuncionario) {
		this.identificacaoConsultaFuncionario = identificacaoConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public CursoCoordenadorVO getCursoCoordenadorVO() {
		if (cursoCoordenadorVO == null) {
			cursoCoordenadorVO = new CursoCoordenadorVO();
		}
		return cursoCoordenadorVO;
	}

	public void setCursoCoordenadorVO(CursoCoordenadorVO cursoCoordenadorVO) {
		this.cursoCoordenadorVO = cursoCoordenadorVO;
	}

	public List<CursoCoordenadorVO> getListaCursoCoordenadorAux() {
		if (listaCursoCoordenadorAux == null) {
			listaCursoCoordenadorAux = new ArrayList<CursoCoordenadorVO>(0);
		}
		return listaCursoCoordenadorAux;
	}

	public void setListaCursoCoordenadorAux(List<CursoCoordenadorVO> listaCursoCoordenadorAux) {
		this.listaCursoCoordenadorAux = listaCursoCoordenadorAux;
	}

	public void setAdicionarDisciplinas(Boolean adicionarDisciplinas) {
		this.adicionarDisciplinas = adicionarDisciplinas;
	}

	public Boolean getAdicionarDisciplinas() {
		if ((getGradeCurricularVO().getSituacao().equals("CO")) 
				|| (getGradeCurricularVO().getSituacao().equals("AT") && getCursoVO().getNivelEducacionalPosGraduacao())
				|| getValidarAlteracaoMatrizCurricularAtivaInativa()) {
			return true;
		} else {
			return false;
		}
	}

	public AutorizacaoCursoVO getAutorizacaoCurso() {
		if (autorizacaoCurso == null) {
			autorizacaoCurso = new AutorizacaoCursoVO();
		}
		return autorizacaoCurso;
	}

	public void setAutorizacaoCurso(AutorizacaoCursoVO autorizacaoCurso) {
		this.autorizacaoCurso = autorizacaoCurso;
	}

	public MaterialCursoVO getMaterialCurso() {
		if (materialCurso == null) {
			materialCurso = new MaterialCursoVO();
		}
		return materialCurso;
	}

	public void setMaterialCurso(MaterialCursoVO materialCurso) {
		this.materialCurso = materialCurso;
	}


	public Boolean getIsLimitarQtdeDiasDownload() {
		if (getCursoVO().getLimitarQtdeDiasMaxDownload()) {
			return true;
		}
		return false;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public Boolean getEditandoCursoCoordenador() {
		if (editandoCursoCoordenador == null) {
			editandoCursoCoordenador = false;
		}
		return editandoCursoCoordenador;
	}

	public void setEditandoCursoCoordenador(Boolean editandoCursoCoordenador) {
		this.editandoCursoCoordenador = editandoCursoCoordenador;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public boolean getApresentarLiberarRegistroAulaEntrePeriodo() {
		return getCursoVO().getNivelEducacional().equals("PR") || getCursoVO().getNivelEducacional().equals("GT") || getCursoVO().getNivelEducacional().equals("SU");
	}

	public List<SelectItem> getListaSelectItemModalidadeDisciplina() {
		return ModalidadeDisciplinaEnum.getListaSelectItemModalidadeDisciplina();
	}

	public List<SelectItem> getListaSelectItemModalidadeCurso() {
		return ModalidadeDisciplinaEnum.getListaSelectItemModalidadeDisciplinaEscolhaMatricula();
	}

	public String getErroUpload() {
		if (erroUpload == null) {
			erroUpload = "";
		}
		return erroUpload;
	}

	public void setErroUpload(String erroUpload) {
		this.erroUpload = erroUpload;
	}

	public String getMsgErroUpload() {
		if (msgErroUpload == null) {
			msgErroUpload = "";
		}
		return msgErroUpload;
	}

	public void setMsgErroUpload(String msgErroUpload) {
		this.msgErroUpload = msgErroUpload;
	}

	public String getVerificarUltrapassouTamanhoMaximoUpload() {
		try {
			return "Arquivo não Enviado. Tamanho Máximo Permitido " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "";
		}

	}

	public String getTamanhoMaximoUpload() {
		try {
			return "Tamanho Máximo Permitido: " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "Tamanho Máximo Não Configurado";
		}
	}

	public List<SelectItem> getListaSelectItemConfiguracaoTCC() {
		if (listaSelectItemConfiguracaoTCC == null) {
			listaSelectItemConfiguracaoTCC = new ArrayList<SelectItem>();
			montarListaSelectItemConfiguracaoTCC();
		}
		return listaSelectItemConfiguracaoTCC;
	}

	public void setListaSelectItemConfiguracaoTCC(List<SelectItem> listaSelectItemConfiguracaoTCC) {
		this.listaSelectItemConfiguracaoTCC = listaSelectItemConfiguracaoTCC;
	}

	public void realizarApresentacaoAlunosParaCriacaoTCC() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) getGradeDisciplinasPeriodo();
//		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinasPeriodo");
		setGradeDisciplinaCriacaoTCC(obj);
		setTotalAlunosCriacaoTCC(getFacadeFactory().getTrabalhoConclusaoCursoFacade().consultarTotalAlunosTCCGradeDisciplina(getGradeDisciplinaCriacaoTCC(), getUsuarioLogado()));
	}

	public void realizarCriacaoTCC() {
		try {
			getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarCriacaoTrabalhoConclusaoCursoGradeDisciplina(getGradeDisciplinaCriacaoTCC(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public GradeDisciplinaVO getGradeDisciplinaCriacaoTCC() {
		if (gradeDisciplinaCriacaoTCC == null) {
			gradeDisciplinaCriacaoTCC = new GradeDisciplinaVO();
		}
		return gradeDisciplinaCriacaoTCC;
	}

	public void setGradeDisciplinaCriacaoTCC(GradeDisciplinaVO gradeDisciplinaCriacaoTCC) {
		this.gradeDisciplinaCriacaoTCC = gradeDisciplinaCriacaoTCC;
	}

	public Integer getTotalAlunosCriacaoTCC() {
		if (totalAlunosCriacaoTCC == null) {
			totalAlunosCriacaoTCC = 0;
		}
		return totalAlunosCriacaoTCC;
	}

	public void setTotalAlunosCriacaoTCC(Integer totalAlunosCriacaoTCC) {
		this.totalAlunosCriacaoTCC = totalAlunosCriacaoTCC;
	}

	public String getMensagemCriacaoTCC() {

		if (getTotalAlunosCriacaoTCC() > 0) {
			return "A Disciplina " + getGradeDisciplinaCriacaoTCC().getDisciplina().getNome() + " possui " + getTotalAlunosCriacaoTCC() + " aluno(s) matriculado(s)." + " Deseja criar Trabalho de Conclusão de Curso para este(s) aluno(s)?";
		} else {
			return "A Disciplina " + getGradeDisciplinaCriacaoTCC().getDisciplina().getNome() + " não possui nenhum aluno matriculado!";
		}
	}

	public GradeDisciplinaVO getGradeDisciplinasPeriodo() {
		if (gradeDisciplinasPeriodo == null) {
			gradeDisciplinasPeriodo = new GradeDisciplinaVO();
		}
		return gradeDisciplinasPeriodo;
	}

	public void setGradeDisciplinasPeriodo(GradeDisciplinaVO gradeDisciplinasPeriodo) {
		this.gradeDisciplinasPeriodo = gradeDisciplinasPeriodo;
	}

	public GradeCurricularTipoAtividadeComplementarVO getGradeCurricularTipoAtividadeComplementarVO() {
		if (gradeCurricularTipoAtividadeComplementarVO == null) {
			gradeCurricularTipoAtividadeComplementarVO = new GradeCurricularTipoAtividadeComplementarVO();
		}
		return gradeCurricularTipoAtividadeComplementarVO;
	}

	public void setGradeCurricularTipoAtividadeComplementarVO(GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO) {
		this.gradeCurricularTipoAtividadeComplementarVO = gradeCurricularTipoAtividadeComplementarVO;
	}

	public List getListaSelectItemTipoAtividadeComplementar() {
		if (listaSelectItemTipoAtividadeComplementar == null) {
			listaSelectItemTipoAtividadeComplementar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoAtividadeComplementar;
	}

	public void setListaSelectItemTipoAtividadeComplementar(List listaSelectItemTipoAtividadeComplementar) {
		this.listaSelectItemTipoAtividadeComplementar = listaSelectItemTipoAtividadeComplementar;
	}

	public GradeCurricularGrupoOptativaVO getGradeCurricularGrupoOptativaVO() {
		if (gradeCurricularGrupoOptativaVO == null) {
			gradeCurricularGrupoOptativaVO = new GradeCurricularGrupoOptativaVO();
		}
		return gradeCurricularGrupoOptativaVO;
	}

	public void setGradeCurricularGrupoOptativaVO(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO) {
		this.gradeCurricularGrupoOptativaVO = gradeCurricularGrupoOptativaVO;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void abrirModalGrupoOptativa() {
		try {
			getFacadeFactory().getGradeCurricularFacade().validarDados(getGradeCurricularVO(), getCursoVO(), getUsuario());
			setFecharModalGrupoOptativa("");
			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			setGradeCurricularGrupoOptativaVO(new GradeCurricularGrupoOptativaVO());
			setListaSelectItemGradeCurricularGrupoOptativa(null);
			setFecharModalGrupoOptativa("RichFaces.$('panelGrupoOptativa').show()");
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private String fecharModalGrupoOptativa;

	public void gravarGradeCurricularGrupoOptativa() {
		try {
			setFecharModalGrupoOptativa("");
			if (getGradeCurricularVO().isNovoObj()) {
				getFacadeFactory().getGradeCurricularFacade().incluir(getGradeCurricularVO(), getCursoVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getGradeCurricularFacade().alterar(getGradeCurricularVO(), getCursoVO(), getUsuarioLogado());
			}
			setFecharModalGrupoOptativa("RichFaces.$('panelGrupoOptativa').hide()");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void persistirGradeCurricular() {
		try {
			getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().validarTotalCargaHorariaAtividadeComplementar(this.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs(),this.getGradeCurricularVO().getTotalCargaHorariaAtividadeComplementar());
			getFacadeFactory().getGradeCurricularFacade().persistir(getGradeCurricularVO(), getCursoVO(), getValidarAlteracaoMatrizCurricularAtivaInativa(), getUsuarioLogado());
			getFacadeFactory().getCursoFacade().alterarPeriodicidadeCurso(cursoVO, getUsuarioLogado());
			getAplicacaoControle().removerGradeCurricularVO(getGradeCurricularVO().getCodigo());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void cancelarCriacaoGradeCurricularGrupoOptativa() {
		try {
			setFecharModalGrupoOptativa("");
			if (!getGradeCurricularVO().isNovoObj()) {
				getGradeCurricularVO().setGradeCurricularGrupoOptativaVOs(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorGradeCurricular(getGradeCurricularVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			}
			setFecharModalGrupoOptativa("RichFaces.$('panelGrupoOptativa').hide();");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getFecharModalGrupoOptativa() {
		if (fecharModalGrupoOptativa == null) {
			fecharModalGrupoOptativa = "";
		}
		return fecharModalGrupoOptativa;
	}

	public void setFecharModalGrupoOptativa(String fecharModalGrupoOptativa) {
		this.fecharModalGrupoOptativa = fecharModalGrupoOptativa;
	}

	public void selecionarGrupoOptativa() {
		try {
			setGradeCurricularGrupoOptativaEdicaoVO(new GradeCurricularGrupoOptativaVO());
			setGradeCurricularGrupoOptativaEdicaoVO((GradeCurricularGrupoOptativaVO) getRequestMap().get("grupoOptativaItens"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarGradeCurricularGrupoOptativaVO() {
		try {
			getFacadeFactory().getGradeCurricularFacade().adicionarGradeCurricularGrupoOptativaVO(getGradeCurricularVO(), getGradeCurricularGrupoOptativaVO());
			setGradeCurricularGrupoOptativaVO(new GradeCurricularGrupoOptativaVO());
			setListaSelectItemGradeCurricularGrupoOptativa(null);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerGradeCurricularGrupoOptativaVO() {
		try {
			getFacadeFactory().getGradeCurricularFacade().removerGradeCurricularGrupoOptativaVO(getGradeCurricularVO(), (GradeCurricularGrupoOptativaVO) getRequestMap().get("grupoOptativaItens"));
			setListaSelectItemGradeCurricularGrupoOptativa(null);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private List<SelectItem> listaSelectItemGradeCurricularGrupoOptativa;

	public List<SelectItem> getListaSelectItemGradeCurricularGrupoOptativa() {
		if (listaSelectItemGradeCurricularGrupoOptativa == null) {
			listaSelectItemGradeCurricularGrupoOptativa = new ArrayList<SelectItem>(0);
			listaSelectItemGradeCurricularGrupoOptativa.add(new SelectItem(0, ""));
			for (GradeCurricularGrupoOptativaVO obj : getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs()) {
				listaSelectItemGradeCurricularGrupoOptativa.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
		}
		return listaSelectItemGradeCurricularGrupoOptativa;
	}

	public void setListaSelectItemGradeCurricularGrupoOptativa(List listaSelectItemGradeCurricularGrupoOptativa) {
		this.listaSelectItemGradeCurricularGrupoOptativa = listaSelectItemGradeCurricularGrupoOptativa;
	}

	public void adicionarGradeCurricularGrupoOptativaDisciplina() {
		try {
			getFacadeFactory().getGradeCurricularFacade().realizarVerificacaoDisciplinaJaAdicionada(getGradeCurricularVO(), getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina());
			getFacadeFactory().getGradeCurricularGrupoOptativaFacade().adicionarGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaEdicaoVO(), getGradeCurricularGrupoOptativaDisciplinaVO());
			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			Ordenacao.ordenarLista(getGradeCurricularGrupoOptativaEdicaoVO().getGradeCurricularGrupoOptativaDisciplinaVOs(), "ordenacao");
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerGradeCurricularGrupoOptativaDisciplina() {
		try {
			getFacadeFactory().getGradeCurricularGrupoOptativaFacade().removerGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaEdicaoVO(), (GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("grupoOptativaDisciplinaItens"));
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarGradeCurricularGrupoOptativaDisciplina() {
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("grupoOptativaDisciplinaItens"));
			realizarInicializacaoDadosGradeDisciplinaCompostaPorGrupoOptativa();
			getFacadeFactory().getGradeCurricularGrupoOptativaFacade().removerGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaEdicaoVO(), (GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("grupoOptativaDisciplinaItens"));
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDisciplinaOptativa() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) getRequestMap().get("disciplina3");
			if (disciplina != null) {
				getGradeCurricularGrupoOptativaDisciplinaVO().setDisciplina(disciplina);
			}
			getListaConsultaDisciplina().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarInicializacaoDadosGradeDisciplinaCompostaPorGrupoOptativa() {
		setGradeDisciplinaCompostaVOs(new ArrayList<GradeDisciplinaCompostaVO>(0));
		setGradeDisciplinaCompostaVOs(getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs());
		setDisciplinaCompostaGrupoOptativa(true);
		setAdicionandoNovaDisciplinaComposta(false);
		novaGradeDisciplinaComposta();
	}

	public void realizarInicializacaoDadosGradeDisciplinaCompostaPorGradeDisciplina() {
		setGradeDisciplinaCompostaVO(new GradeDisciplinaCompostaVO());
		setGradeDisciplinaCompostaVOs(new ArrayList<GradeDisciplinaCompostaVO>(0));
		setGradeDisciplinaCompostaVOs(getGradeDisciplinasPeriodo().getGradeDisciplinaCompostaVOs());
		setDisciplinaCompostaGrupoOptativa(false);
		setAdicionandoNovaDisciplinaComposta(getGradeCurricularVO().getSituacao().equals("CO") ? getPossuiPermissaoAlterarMatrizCurricularConstrucao() : false);
		novaGradeDisciplinaComposta();
	}

	private void novaGradeDisciplinaComposta() {
		setGradeDisciplinaCompostaVO(new GradeDisciplinaCompostaVO());
		if (getDisciplinaCompostaGrupoOptativa()) {
			getGradeDisciplinaCompostaVO().getConfiguracaoAcademico().setCodigo(getGradeCurricularGrupoOptativaDisciplinaVO().getConfiguracaoAcademico().getCodigo());
			getGradeDisciplinaCompostaVO().setDiversificada(getGradeCurricularGrupoOptativaDisciplinaVO().getDiversificada());
			getGradeDisciplinaCompostaVO().setGrupoOptativa(true);
			getGradeDisciplinaCompostaVO().setModalidadeDisciplina(getGradeCurricularGrupoOptativaDisciplinaVO().getModalidadeDisciplina());
		} else {
			getGradeDisciplinaCompostaVO().getConfiguracaoAcademico().setCodigo(getGradeDisciplinasPeriodo().getConfiguracaoAcademico().getCodigo());
			getGradeDisciplinaCompostaVO().setDiversificada(getGradeDisciplinasPeriodo().getDiversificada());
			getGradeDisciplinaCompostaVO().setGrupoOptativa(false);
			getGradeDisciplinaCompostaVO().setModalidadeDisciplina(getGradeDisciplinasPeriodo().getModalidadeDisciplina());
		}
	}

	public void selecionarDisciplinaComposta() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) getRequestMap().get("disciplina4");
			if (disciplina != null) {
				getGradeDisciplinaCompostaVO().setDisciplina(disciplina);
			}
			getListaConsultaDisciplina().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void subirGradeDisciplinaComposta() {
		try {
			getFacadeFactory().getGradeDisciplinaCompostaFacade().alterarOrdemGradeDisciplinaComposta(getGradeDisciplinaCompostaVOs(), (GradeDisciplinaCompostaVO) getRequestMap().get("gradeDisciplinaCompostaItens"), true);
			if(getDisciplinaCompostaGrupoOptativa()){
				getGradeCurricularGrupoOptativaDisciplinaVO().setGradeDisciplinaCompostaVOs(getGradeDisciplinaCompostaVOs());
			}else{
				getGradeDisciplinasPeriodo().setGradeDisciplinaCompostaVOs(getGradeDisciplinaCompostaVOs());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Integer getQtdeDisciplinasCompostas(){
		return getGradeDisciplinaCompostaVOs().size();
	}
	
	public void descerGradeDisciplinaComposta() {
		try {
			getFacadeFactory().getGradeDisciplinaCompostaFacade().alterarOrdemGradeDisciplinaComposta(getGradeDisciplinaCompostaVOs(), (GradeDisciplinaCompostaVO) getRequestMap().get("gradeDisciplinaCompostaItens"), false);
			if(getDisciplinaCompostaGrupoOptativa()){
				getGradeCurricularGrupoOptativaDisciplinaVO().setGradeDisciplinaCompostaVOs(getGradeDisciplinaCompostaVOs());
			}else{
				getGradeDisciplinasPeriodo().setGradeDisciplinaCompostaVOs(getGradeDisciplinaCompostaVOs());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarGradeDisciplinaComposta() {
		try {
			getFacadeFactory().getGradeCurricularFacade().realizarVerificacaoDisciplinaJaAdicionada(getGradeCurricularVO(), getGradeDisciplinaCompostaVO().getDisciplina());
			if (getDisciplinaCompostaGrupoOptativa()) {
				getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().adicionarGradeDisciplinaCompostaVOs(getGradeCurricularGrupoOptativaDisciplinaVO(), getGradeDisciplinaCompostaVO(), getGradeCurricularVO().getSituacao());
				setGradeDisciplinaCompostaVOs(getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs());
			} else {
				getFacadeFactory().getGradeDisciplinaFacade().adicionarGradeDisciplinaCompostaVOs(getGradeDisciplinasPeriodo(), getGradeDisciplinaCompostaVO(), getGradeCurricularVO().getSituacao(), getGradeDisciplinasPeriodo());
				setGradeDisciplinaCompostaVOs(getGradeDisciplinasPeriodo().getGradeDisciplinaCompostaVOs());
			}
			novaGradeDisciplinaComposta();
			int x = 0;
			for (GradeDisciplinaCompostaVO objExistente : getGradeDisciplinasPeriodo().getGradeDisciplinaCompostaVOs()) {
				objExistente.setOrdem(x);
				x++;
			}
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerGradeDisciplinaComposta() {
		try {
			if (getDisciplinaCompostaGrupoOptativa()) {
				getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().removerGradeDisciplinaCompostaVOs(getGradeCurricularGrupoOptativaDisciplinaVO(), (GradeDisciplinaCompostaVO)getRequestMap().get("gradeDisciplinaCompostaItens"));
				setGradeDisciplinaCompostaVOs(getGradeCurricularGrupoOptativaDisciplinaVO().getGradeDisciplinaCompostaVOs());
			} else {
				getFacadeFactory().getGradeDisciplinaFacade().removerGradeDisciplinaCompostaVOs(getGradeDisciplinasPeriodo(), (GradeDisciplinaCompostaVO)getRequestMap().get("gradeDisciplinaCompostaItens"));
				setGradeDisciplinaCompostaVOs(getGradeDisciplinasPeriodo().getGradeDisciplinaCompostaVOs());
			}
			novaGradeDisciplinaComposta();
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarGradeDisciplinaComposta() {
		setGradeDisciplinaCompostaVO(new GradeDisciplinaCompostaVO());
		setGradeDisciplinaCompostaVO((GradeDisciplinaCompostaVO) getRequestMap().get("gradeDisciplinaCompostaItens"));
		getGradeDisciplinaCompostaVOs().remove(getGradeDisciplinaCompostaVO());
		int x = 0;
		for (GradeDisciplinaCompostaVO objExistente : getGradeDisciplinasPeriodo().getGradeDisciplinaCompostaVOs()) {
			objExistente.setOrdem(x);
			x++;
		}
	}

	public void limparDisciplinaOptativa() {
		getGradeCurricularGrupoOptativaDisciplinaVO().setDisciplina(null);
	}
	
	
	
	public void inicializarConsultaDisciplinaPadraoTcc() {
		try {
			getListaConsultaDisciplina().clear();
			setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarDisciplinaPadraoTcc() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) getRequestMap().get("disciplina3");
			getGradeCurricularVO().setDisciplinaPadraoTcc(disciplina);
			getListaConsultaDisciplina().clear();
			setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDisciplinaPadraoTcc() {
		getGradeCurricularVO().setDisciplinaPadraoTcc(new DisciplinaVO());
		inicializarMensagemVazia();
	}

	private List<SelectItem> listaSelectItemTipoControleGrupoOptativa;

	public List<SelectItem> getListaSelectItemTipoControleGrupoOptativa() {
		if (listaSelectItemTipoControleGrupoOptativa == null) {
			listaSelectItemTipoControleGrupoOptativa = new ArrayList<SelectItem>(0);
			listaSelectItemTipoControleGrupoOptativa.add(new SelectItem(TipoControleGrupoOptativaEnum.CREDITO, TipoControleGrupoOptativaEnum.CREDITO.getValorApresentar()));
			listaSelectItemTipoControleGrupoOptativa.add(new SelectItem(TipoControleGrupoOptativaEnum.CARGA_HORARIA, TipoControleGrupoOptativaEnum.CARGA_HORARIA.getValorApresentar()));
		}
		return listaSelectItemTipoControleGrupoOptativa;
	}

	public void setListaSelectItemTipoControleGrupoOptativa(List<SelectItem> listaSelectItemTipoControleGrupoOptativa) {
		this.listaSelectItemTipoControleGrupoOptativa = listaSelectItemTipoControleGrupoOptativa;
	}

	public GradeCurricularGrupoOptativaVO getGradeCurricularGrupoOptativaEdicaoVO() {
		if (gradeCurricularGrupoOptativaEdicaoVO == null) {
			gradeCurricularGrupoOptativaEdicaoVO = new GradeCurricularGrupoOptativaVO();
		}
		return gradeCurricularGrupoOptativaEdicaoVO;
	}

	public void setGradeCurricularGrupoOptativaEdicaoVO(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaEdicaoVO) {
		this.gradeCurricularGrupoOptativaEdicaoVO = gradeCurricularGrupoOptativaEdicaoVO;
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
		if (gradeDisciplinaCompostaVO == null) {
			gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaVO;
	}

	public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
		this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
	}

	public Boolean getDisciplinaCompostaGrupoOptativa() {
		if (disciplinaCompostaGrupoOptativa == null) {
			disciplinaCompostaGrupoOptativa = false;
		}
		return disciplinaCompostaGrupoOptativa;
	}

	public void setDisciplinaCompostaGrupoOptativa(Boolean disciplinaCompostaGrupoOptativa) {
		this.disciplinaCompostaGrupoOptativa = disciplinaCompostaGrupoOptativa;
	}

	public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
		if (gradeDisciplinaCompostaVOs == null) {
			gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
		}
		return gradeDisciplinaCompostaVOs;
	}

	public void setGradeDisciplinaCompostaVOs(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
		this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
	}
	
	public boolean getIsPermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular() {
		return getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarDataAtivacaoDataFinalVigenciaMatrizCurricular();
	}

	public List<UnidadeEnsinoVO> getListaConsultaUnidadeEnsino() {
		if(listaConsultaUnidadeEnsino == null){
			listaConsultaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaConsultaUnidadeEnsino;
	}

	public void setListaConsultaUnidadeEnsino(List<UnidadeEnsinoVO> listaConsultaUnidadeEnsino) {
		this.listaConsultaUnidadeEnsino = listaConsultaUnidadeEnsino;
	}

	public Boolean getMarcarTodasUnidadeEnsino() {
		if(marcarTodasUnidadeEnsino == null){
			marcarTodasUnidadeEnsino = Boolean.FALSE;
		}
		return marcarTodasUnidadeEnsino;
	}

	public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
		this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
	}
	

	public Boolean getApresentarAtivarGrade() {
		if (apresentarAtivarGrade == null) {
			apresentarAtivarGrade = Boolean.FALSE;
		}
		return apresentarAtivarGrade;
	}

	public void setApresentarAtivarGrade(Boolean apresentarAtivarGrade) {
		this.apresentarAtivarGrade = apresentarAtivarGrade;
	}
	
	/**
	 * Método responsável por realizar a recontagem do número de período letivos
	 * no ato da remoção do mesmo.
	 * 
	 * @throws Exception
	 */
	public void recontarNumeroPeriodoLetivo() throws Exception {
		String periodicidade = getCursoVO().getPeriodicidade();
		int contador = 1;
		for (Iterator<PeriodoLetivoVO> iterator = getGradeCurricularVO().getPeriodoLetivosVOs().iterator(); iterator.hasNext();) {
			PeriodoLetivoVO periodoLetivoVO = (PeriodoLetivoVO) iterator.next();
			periodoLetivoVO.setPeriodoLetivo(contador);
			if (periodicidade.equals("IN")) {
				periodoLetivoVO.setDescricao(periodicidade);
			} else if (periodicidade.equals("AN")) {
				periodoLetivoVO.setDescricao(String.valueOf(contador) + "º Ano");
			} else if (periodicidade.equals("SE")) {
				periodoLetivoVO.setDescricao(String.valueOf(contador) + "º Semestre");
			}
			
			contador++;
		}
		Ordenacao.ordenarLista(getGradeCurricularVO().getPeriodoLetivosVOs(), "periodoLetivo");
	}
	
	public void definirPeriodicidadeCursoDeAcordoNivelEducacional() throws Exception {
		String nivelEducacional = getCursoVO().getNivelEducacional();
		if (getCursoVO().isNovoObj()) {
			if (nivelEducacional.equals(TipoNivelEducacional.INFANTIL.getValor()) || nivelEducacional.equals(TipoNivelEducacional.MEDIO.getValor()) || nivelEducacional.equals(TipoNivelEducacional.BASICO.getValor())) {
				getCursoVO().setPeriodicidade("AN");
			} else if (nivelEducacional.equals(TipoNivelEducacional.SUPERIOR.getValor()) || nivelEducacional.equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor()) || nivelEducacional.equals(TipoNivelEducacional.SEQUENCIAL.getValor())) {
				getCursoVO().setPeriodicidade("SE");
			} else if (nivelEducacional.equals(TipoNivelEducacional.POS_GRADUACAO.getValor()) || nivelEducacional.equals(TipoNivelEducacional.PROFISSIONALIZANTE.getValor()) || nivelEducacional.equals(TipoNivelEducacional.EXTENSAO.getValor())) {
				getCursoVO().setPeriodicidade("IN");
			} else if (nivelEducacional.equals(TipoNivelEducacional.MESTRADO.getValor())) {
				getCursoVO().setPeriodicidade("SE");
			}
		}
	}
	
	private List<SelectItem> listaSelectFormulaCalculoNota;

	/**
	 * @return the listaSelectFormulaCalculoNota
	 */
	public List<SelectItem> getListaSelectFormulaCalculoNota() {
		if (listaSelectFormulaCalculoNota == null) {
			listaSelectFormulaCalculoNota = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FormulaCalculoNotaEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectFormulaCalculoNota;
	}
	
	public boolean getApresentarVariavelNotaDisciplinaComposta(){
		if (getDisciplinaCompostaGrupoOptativa()) {
			return getGradeCurricularGrupoOptativaDisciplinaVO().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) || (getGradeCurricularGrupoOptativaDisciplinaVO().getApresentarOpcaoRecuperacao() && getGradeCurricularGrupoOptativaDisciplinaVO().getControlarRecuperacaoPelaDisciplinaPrincipal());
		}
		return getGradeDisciplinasPeriodo().getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO) || (getGradeDisciplinasPeriodo().getApresentarOpcaoRecuperacao() && getGradeDisciplinasPeriodo().getControlarRecuperacaoPelaDisciplinaPrincipal());
	}

	public String getOnCompleteConfigurarGrupoOptativa() {
		if (onCompleteConfigurarGrupoOptativa == null) {
			onCompleteConfigurarGrupoOptativa = "";
		}
		return onCompleteConfigurarGrupoOptativa;
	}

	public void setOnCompleteConfigurarGrupoOptativa(String onCompleteConfigurarGrupoOptativa) {
		this.onCompleteConfigurarGrupoOptativa = onCompleteConfigurarGrupoOptativa;
	}
	
	public boolean getApresentarCampoNumeroMaximoDisciplinaComposicaoEstudar() {
		return TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(getGradeDisciplinasPeriodo().getTipoControleComposicao());
	}
	
	public void configurarPreRequisitoGradeDisciplinaComposta() {
		try {
			setGradeDisciplinaCompostaAuxiliar(new GradeDisciplinaCompostaVO());
			setGradeDisciplinaCompostaAuxiliar((GradeDisciplinaCompostaVO) getRequestMap().get("gradeDisciplinaCompostaItens"));
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			montarListaCompostaDisciplinasAnteriores();
		} catch (Exception e) {
			setMensagemDetalhada("Erro ao configurar o Pré Requisito");
		}
	}
	
	public void adicionarDisciplinaCompostaPreRequisito() throws Exception {
		try {
			DisciplinaVO gradeDisc = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaAnterior2");
			adicionarDisciplinaCompostaPreRequisito(gradeDisc);
			montarListaCompostaDisciplinasAnteriores();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDisciplinaCompostaPreRequisito(DisciplinaVO disciplinaAnterior) {
		try {
			if (!Uteis.isAtributoPreenchido(getGradeDisciplinaCompostaAuxiliar().getDisciplina())) {
				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_disciplina"));
			}
			getDisciplinaPreRequisitoVO().setGradeDisciplinaComposta(getGradeDisciplinaCompostaAuxiliar().getCodigo());
			getDisciplinaPreRequisitoVO().setDisciplina(disciplinaAnterior);
			getFacadeFactory().getGradeDisciplinaCompostaFacade().adicionarDisciplinaPreRequisitoVOs(getGradeDisciplinaCompostaAuxiliar().getDisciplinaRequisitoVOs(), getDisciplinaPreRequisitoVO());
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			Ordenacao.ordenarLista(getGradeDisciplinaCompostaAuxiliar().getDisciplinaRequisitoVOs(), "ordenacao");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerDisciplinaCompostaPreRequisito() {
		try {
			DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) getRequestMap().get("disciplinaPreRequisito2");
			getFacadeFactory().getGradeDisciplinaCompostaFacade().removerDisciplinaPreRequisitoVOs(getGradeDisciplinaCompostaAuxiliar().getDisciplinaRequisitoVOs(), obj, getUsuarioLogado());
			montarListaCompostaDisciplinasAnteriores();
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> listaSelectNomeTurnoCenso;

	public List<SelectItem> getListaSelectNomeTurnoCenso() {
		if (listaSelectNomeTurnoCenso == null) {
			listaSelectNomeTurnoCenso = new ArrayList<SelectItem>(0);
			listaSelectNomeTurnoCenso.add(new SelectItem("", ""));
			listaSelectNomeTurnoCenso.add(new SelectItem(NomeTurnoCensoEnum.MATUTINO, "Matutino"));
			listaSelectNomeTurnoCenso.add(new SelectItem(NomeTurnoCensoEnum.VESPERTINO, "Vespertino"));
			listaSelectNomeTurnoCenso.add(new SelectItem(NomeTurnoCensoEnum.NOTURNO, "Noturno"));
			listaSelectNomeTurnoCenso.add(new SelectItem(NomeTurnoCensoEnum.INTEGRAL, "Integral"));

		}
		return listaSelectNomeTurnoCenso;
	}
	
	public List<DisciplinaVO> getListaTodasDisciplinas() {
		if (listaTodasDisciplinas == null) {
			listaTodasDisciplinas = new ArrayList<DisciplinaVO>(0);
		}
		return listaTodasDisciplinas;
	}

	public void setListaTodasDisciplinas(List<DisciplinaVO> listaTodasDisciplinas) {
		this.listaTodasDisciplinas = listaTodasDisciplinas;
	}
	
	public void configurarPreRequisitoGrupoOptativa() throws Exception {
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("grupoOptativaDisciplinaItens"));
			realizarInicializacaoDadosGradeDisciplinaCompostaPorGrupoOptativa();
			getFacadeFactory().getGradeCurricularGrupoOptativaFacade().removerGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaEdicaoVO(), (GradeCurricularGrupoOptativaDisciplinaVO) getRequestMap().get("grupoOptativaDisciplinaItens"));
			setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			montarListaTodasDisciplinas();
			for (GradeCurricularVO gc : getCursoVO().getGradeCurricularVOs()) {
				if (gc.getCodigo().intValue() == getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getGradeCurricular().getCodigo().intValue()) {
					getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().getGradeCurricular().setSituacao(gc.getSituacao());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("Erro ao configurar o Pré Requisito");
		}
	}
	
	public void montarListaTodasDisciplinas() {
		List<Integer> codidgoDisciplinaRequisitos = new ArrayList<Integer>(0);
		for (DisciplinaPreRequisitoVO pre : getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaRequisitoVOs()) {
			codidgoDisciplinaRequisitos.add(pre.getDisciplina().getCodigo().intValue());
		}
		// lista de disciplinas da grade curricular
		List<DisciplinaVO> listaDisciplina = new ArrayList<DisciplinaVO>(0);
		Iterator i = getGradeCurricularVO().getPeriodoLetivosVOs().iterator();
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			Iterator j = obj.getGradeDisciplinaVOs().iterator();
			while (j.hasNext()) {
				GradeDisciplinaVO gradeDisciplina = (GradeDisciplinaVO) j.next();
				if (!codidgoDisciplinaRequisitos.contains(gradeDisciplina.getDisciplina().getCodigo().intValue())) {
					listaDisciplina.add(gradeDisciplina.getDisciplina());
				}
				if(gradeDisciplina.getDisciplinaComposta()){
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplina.getGradeDisciplinaCompostaVOs()){
						if (!codidgoDisciplinaRequisitos.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue())) {
							listaDisciplina.add(gradeDisciplinaCompostaVO.getDisciplina());
						}
					}
				}
			}
		}
		Ordenacao.ordenarLista(listaDisciplina, "nome");
		setListaTodasDisciplinas(listaDisciplina);
		// lista de disciplinas de grupo optativa
		List<DisciplinaVO> listaDisciplina2 = new ArrayList<DisciplinaVO>(0);
		Iterator i2 = getGradeCurricularVO().getGradeCurricularGrupoOptativaVOs().iterator();
		while (i2.hasNext()) {
			GradeCurricularGrupoOptativaVO item = (GradeCurricularGrupoOptativaVO) i2.next();
			Iterator j2 = item.getGradeCurricularGrupoOptativaDisciplinaVOs().iterator();
			while (j2.hasNext()) {
				GradeCurricularGrupoOptativaDisciplinaVO d = (GradeCurricularGrupoOptativaDisciplinaVO) j2.next();
				if (!codidgoDisciplinaRequisitos.contains(d.getDisciplina().getCodigo().intValue())) {
					if (d.getDisciplina().getCodigo().intValue() != getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().intValue()) {
						listaDisciplina2.add(d.getDisciplina());
					}
				}
				if(d.getDisciplinaComposta()){
					for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: d.getGradeDisciplinaCompostaVOs()){
						if (!codidgoDisciplinaRequisitos.contains(gradeDisciplinaCompostaVO.getDisciplina().getCodigo().intValue())) {
							listaDisciplina.add(gradeDisciplinaCompostaVO.getDisciplina());
						}
					}
				}
			}
		}
		Ordenacao.ordenarLista(listaDisciplina2, "nome");
		setListaTodasDisciplinasGrupoOptativa(listaDisciplina2);
		
		codidgoDisciplinaRequisitos = null;
	}
	
	public void desmarcarTodosRequisitosGrupoOptativa() {
		getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaRequisitoVOs().clear();
		montarListaTodasDisciplinas();
	}
	
	public void desmarcarTodosRequisitosComposta() {
		getGradeDisciplinaCompostaAuxiliar().getDisciplinaRequisitoVOs().clear();
		montarListaCompostaDisciplinasAnteriores();
	}
	
	public void desmarcarTodosRequisitos() {
		getGradeDisciplinaVO().getDisciplinaRequisitoVOs().clear();
		montarListaDisciplinasAnteriores();
	}
	
	public void marcarTodosRequisitosGrupoOptativa() {
		try {
			for (DisciplinaVO gd : getListaTodasDisciplinas()) {
				adicionarDisciplinaPreRequisitoGrupoOptativa(gd);
			}
			getListaTodasDisciplinas().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosGrupoOptativaRequisitosGrupoOptativa() {
		try {
			for (DisciplinaVO d : getListaTodasDisciplinasGrupoOptativa()) {
				adicionarDisciplinaGrupoOptativaPreRequisitoGrupoOptativa(d);
			}
			getListaTodasDisciplinasGrupoOptativa().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosRequisitosComposta() {
		try {
			for (DisciplinaVO gd : getListaDisciplinasAnteriores()) {
				adicionarDisciplinaCompostaPreRequisito(gd);
			}
			getListaDisciplinasAnteriores().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void marcarTodosRequisitos() {
		try {
			for (DisciplinaVO gd : getListaDisciplinasAnteriores()) {
				adicionarDisciplinaPreRequisito(gd);
			}
			getListaDisciplinasAnteriores().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDisciplinaPreRequisitoGrupoOptativa() throws Exception {
		try {
			DisciplinaVO disciplinaVO = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disc");
			adicionarDisciplinaPreRequisitoGrupoOptativa(disciplinaVO);
			montarListaTodasDisciplinas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDisciplinaPreRequisitoGrupoOptativa(DisciplinaVO disciplinaVO) throws Exception {
		try {
			if (getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().intValue() != 0) {
				if (!getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
					disciplinaPreRequisitoVO.setGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
				}
				getDisciplinaPreRequisitoVO().getDisciplina().setCodigo(disciplinaVO.getCodigo());
				if (getDisciplinaPreRequisitoVO().getDisciplina().getCodigo().intValue() != 0) {
					Integer campoConsulta = getDisciplinaPreRequisitoVO().getDisciplina().getCodigo();
					DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					getDisciplinaPreRequisitoVO().setDisciplina(disciplina);
				}
				getGradeCurricularGrupoOptativaDisciplinaVO().adicionarObjDisciplinaPreRequisitoVOs(getDisciplinaPreRequisitoVO());
				this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
				Ordenacao.ordenarLista(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaRequisitoVOs(), "ordenacao");
				setMensagemID("msg_dados_adicionados");
			} else {
				setMensagemDetalhada("O campo DISCIPLINA deve ser informado.");
			}
		} catch (Exception e) {
			this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDisciplinaGrupoOptativaPreRequisitoGrupoOptativa() throws Exception {
		try {
			DisciplinaVO disc = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disc2");
			adicionarDisciplinaGrupoOptativaPreRequisitoGrupoOptativa(disc);
			montarListaTodasDisciplinas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDisciplinaGrupoOptativaPreRequisitoGrupoOptativa(DisciplinaVO d) throws Exception {
		try {
			if (getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo().intValue() != 0) {
				if (!getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(0)) {
					disciplinaPreRequisitoVO.setGradeCurricularGrupoOptativaDisciplina(getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
				}
				getDisciplinaPreRequisitoVO().getDisciplina().setCodigo(d.getCodigo());
				if (getDisciplinaPreRequisitoVO().getDisciplina().getCodigo().intValue() != 0) {
					Integer campoConsulta = getDisciplinaPreRequisitoVO().getDisciplina().getCodigo();
					DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					getDisciplinaPreRequisitoVO().setDisciplina(disciplina);
				}
				getGradeCurricularGrupoOptativaDisciplinaVO().adicionarObjDisciplinaPreRequisitoVOs(getDisciplinaPreRequisitoVO());
				this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
				Ordenacao.ordenarLista(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinaRequisitoVOs(), "ordenacao");
				setMensagemID("msg_dados_adicionados");
			} else {
				setMensagemDetalhada("O campo DISCIPLINA deve ser informado.");
			}
		} catch (Exception e) {
			this.setDisciplinaPreRequisitoVO(new DisciplinaPreRequisitoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerDisciplinaPreRequisitoGrupoOptativa() throws Exception {
		DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) context().getExternalContext().getRequestMap().get("requisitoGrupoOptativa");
		getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().excluirObjDisciplinaPreRequisitoVOs(obj.getDisciplina().getCodigo(), getGradeCurricularGrupoOptativaDisciplinaVO(), getUsuarioLogado());
		montarListaTodasDisciplinas();
		setMensagemID("msg_dados_excluidos");
	}
	
	
	
	public void adicionarGradeCurricularEstagio() {
		try {
			getFacadeFactory().getGradeCurricularFacade().adicionarGradeCurricularEstagio(getGradeCurricularVO(), getGradeCurricularEstagioVO(), getUsuarioLogadoClone());
			setGradeCurricularEstagioVO(new GradeCurricularEstagioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerGradeCurricularEstagio() {
		try {
			GradeCurricularEstagioVO obj = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get("gradeCurricularEstagioItens");
			getFacadeFactory().getGradeCurricularFacade().removerGradeCurricularEstagio(getGradeCurricularVO(), obj, getUsuarioLogadoClone());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCamposGradeCurricularEstagioQuestionarioEnum() {
		try {
			getGradeCurricularEstagioVO().setQuestionarioAproveitamentoPorDocenteRegular(new QuestionarioVO());
			getGradeCurricularEstagioVO().setQuestionarioAproveitamentoPorLicenciatura(new QuestionarioVO());
			getGradeCurricularEstagioVO().setQuestionarioEquivalencia(new QuestionarioVO());
			getGradeCurricularEstagioVO().setHoraMaximaAproveitamentoOuEquivalencia(0);
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String inicializarVoltar() {
		try {
			getCursoVO().setGradeCurricularVOs(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, true, getUsuarioLogado()).getGradeCurricularVOs());
			Ordenacao.ordenarLista(getCursoVO().getGradeCurricularVOs(), "situacao");
			setGradeCurricularVO(new GradeCurricularVO());
			setPeriodoLetivoVO(new PeriodoLetivoVO());
			setGradeCurricularTipoAtividadeComplementarVO(new GradeCurricularTipoAtividadeComplementarVO());
			setPossuiPermissaoAlterarMatrizCurricularConstrucao(getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao());
			verificarPermissaoAtivarGrade();
			verificarPermissaoInativarGrade();
			setControleAba("gradeCurricular");
			return Uteis.getCaminhoRedirecionamentoNavegacao("cursoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("gradeCurricularForm");
		}
	}
	
	

	public GradeCurricularEstagioVO getGradeCurricularEstagioVO() {
		if (gradeCurricularEstagioVO == null) {
			gradeCurricularEstagioVO =new GradeCurricularEstagioVO();
		}
		return gradeCurricularEstagioVO;
	}

	public void setGradeCurricularEstagioVO(GradeCurricularEstagioVO gradeCurricularEstagioVO) {
		this.gradeCurricularEstagioVO = gradeCurricularEstagioVO;
	}

	public List<SelectItem> getListaSelectItemQuestionarioEstagio() {
		if (listaSelectItemQuestionarioEstagio == null) {
			listaSelectItemQuestionarioEstagio = new ArrayList<>();
		}
		return listaSelectItemQuestionarioEstagio;
	}

	public void setListaSelectItemQuestionarioEstagio(List<SelectItem> listaSelectItemQuestionarioEstagio) {
		this.listaSelectItemQuestionarioEstagio = listaSelectItemQuestionarioEstagio;
	}
	
	
	
	

	public List<SelectItem> getListaSelectItemQuestionarioEstagioEdicao() {
		if (listaSelectItemQuestionarioEstagioEdicao == null) {
			listaSelectItemQuestionarioEstagioEdicao = new ArrayList<>();
		}
		return listaSelectItemQuestionarioEstagioEdicao;
	}

	public void setListaSelectItemQuestionarioEstagioEdicao(List<SelectItem> listaSelectItemQuestionarioEstagioEdicao) {
		this.listaSelectItemQuestionarioEstagioEdicao = listaSelectItemQuestionarioEstagioEdicao;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoDeclaracao() {
		if (listaSelectItemTextoPadraoDeclaracao == null) {
			listaSelectItemTextoPadraoDeclaracao = new ArrayList<>();
		}
		return listaSelectItemTextoPadraoDeclaracao;
	}

	public void setListaSelectItemTextoPadraoDeclaracao(List<SelectItem> listaSelectItemTextoPadraoDeclaracao) {
		this.listaSelectItemTextoPadraoDeclaracao = listaSelectItemTextoPadraoDeclaracao;
	}

	public List<DisciplinaVO> getListaTodasDisciplinasGrupoOptativa() {
		if (listaTodasDisciplinasGrupoOptativa == null) {
			listaTodasDisciplinasGrupoOptativa = new ArrayList<DisciplinaVO>(0);
		}
		return listaTodasDisciplinasGrupoOptativa;
	}

	public void setListaTodasDisciplinasGrupoOptativa(List<DisciplinaVO> listaTodasDisciplinasGrupoOptativa) {
		this.listaTodasDisciplinasGrupoOptativa = listaTodasDisciplinasGrupoOptativa;
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaAuxiliar() {
		if (gradeDisciplinaCompostaAuxiliar == null) {
			gradeDisciplinaCompostaAuxiliar = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaAuxiliar;
	}

	public void setGradeDisciplinaCompostaAuxiliar(
			GradeDisciplinaCompostaVO gradeDisciplinaCompostaAuxiliar) {
		this.gradeDisciplinaCompostaAuxiliar = gradeDisciplinaCompostaAuxiliar;
	}
	
	public Boolean getApresentarInativarGrade() {
		if (apresentarInativarGrade == null) {
			apresentarInativarGrade = Boolean.FALSE;
		}
		return apresentarInativarGrade;
	}

	public void setApresentarInativarGrade(Boolean apresentarInativarGrade) {
		this.apresentarInativarGrade = apresentarInativarGrade;
	}
	
	public boolean getIsPermiteAlterarMatrizCurricular() {
		return getLoginControle().getPermissaoAcessoMenuVO().getPermitirAlterarMatrizCurricularConstrucao();
	}
	
	public Boolean getAdicionandoNovaDisciplinaOptativaComposta(){
		return getDisciplinaCompostaGrupoOptativa() && !Uteis.isAtributoPreenchido(getGradeCurricularGrupoOptativaDisciplinaVO()); 
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}
	
	public void montarListaSelectItemPeriodicidade() {
		getListaSelectItemPeriodicidade().clear();
//		if(!getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor()) && getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
			getListaSelectItemPeriodicidade().add(new SelectItem("IN", "Integral"));
//		}
		getListaSelectItemPeriodicidade().add(new SelectItem("SE", "Semestral"));
		if (getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.MESTRADO.getValor()) || getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor()) || getCursoVO().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
			getListaSelectItemPeriodicidade().add(new SelectItem("AN", "Anual"));
		}
	}

	public Boolean getApresentarModalidadeCurso() {
		if (apresentarModalidadeCurso == null) {
			apresentarModalidadeCurso = Boolean.FALSE;
		}
		return apresentarModalidadeCurso;
	}

	public void setApresentarModalidadeCurso(Boolean apresentarModalidadeCurso) {
		this.apresentarModalidadeCurso = apresentarModalidadeCurso;
	}

	public void setListaSelectItemModalidadeCurso(List<SelectItem> listaSelectItemModalidadeCurso) {
		this.listaSelectItemModalidadeCurso = listaSelectItemModalidadeCurso;
	}	
	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public Integer getCursoClonarAtividadeComplementar() {
		if (cursoClonarAtividadeComplementar == null) {
			cursoClonarAtividadeComplementar = getCursoVO().getCodigo();
		}
		return cursoClonarAtividadeComplementar;
	}

	public void setCursoClonarAtividadeComplementar(Integer cursoClonarAtividadeComplementar) {
		this.cursoClonarAtividadeComplementar = cursoClonarAtividadeComplementar;
	}

	public void montarListaSelectItemGradeCurricular() {
		limparMensagem();
		setCursoClonarAtividadeComplementar(getCursoVO().getCodigo());
		consultarGradeCursoClonarAtividadeComplementar();		
	}

	public void montarListaSelectItemCloneTipoAtividadeComplementar() {
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemCurso();
	}
	
	public void montarListaSelectItemCurso() {
		if(!Uteis.isAtributoPreenchido(getListaSelectItemCurso())) {
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursosPossuemGradeCurricularComTipoAtividadeComplementar();
			for(CursoVO cursoVO: cursoVOs) {
				getListaSelectItemCurso().add(new SelectItem(cursoVO.getCodigo(), cursoVO.getNome()));
			}
		}
	}
	
	public void consultarGradeCursoClonarAtividadeComplementar() {
		try {
			limparMensagem();
			getListaSelectItemGradeCurricular().clear();
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCursoPossuemAtividadeComplementar(getCursoClonarAtividadeComplementar(), getUsuarioLogado());
			for (GradeCurricularVO gradeCurricularVO : gradeCurricularVOs) {
				if (!gradeCurricularVO.getCodigo().equals(getGradeCurricularVO().getCodigo())) {
					getListaSelectItemGradeCurricular().add(new SelectItem(gradeCurricularVO.getCodigo(), gradeCurricularVO.getNome()));
				}
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);			
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}
	
	
	public Integer getGradeCurricularClonarAtividadeComplementar() {
		if (gradeCurricularClonarAtividadeComplementar == null) {
			gradeCurricularClonarAtividadeComplementar = 0;
		}
		return gradeCurricularClonarAtividadeComplementar;
	}

	public void setGradeCurricularClonarAtividadeComplementar(Integer gradeCurricularClonarAtividadeComplementar) {
		this.gradeCurricularClonarAtividadeComplementar = gradeCurricularClonarAtividadeComplementar;
	}

	public void realizarCopiaTipoAtividadeComplementarOutraGrade() {
		try{
			setOncompleteModal("");
			getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().realizarCopiaTipoAtividadeComplementarOutraGrade(getGradeCurricularClonarAtividadeComplementar(), getGradeCurricularVO(), getUsuarioLogado());
			setOncompleteModal("Rich.$('panelClonarAtividade').hide()");
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private List<SelectItem> montaListaExcluindoValor(TituloCursoSuperior tituloCursoSuperiorExcluir) {
		List<SelectItem> listaSelectItem = new ArrayList<>(0);
		for (TituloCursoSuperior tituloCursoSuperior : TituloCursoSuperior.values()) {
			if (!tituloCursoSuperior.getValor().equals(tituloCursoSuperiorExcluir.getValor())) {
				listaSelectItem.add(new SelectItem(tituloCursoSuperior.getValor(), tituloCursoSuperior.getDescricao()));
			}
		}
		return listaSelectItem;
	}
	
	private List<SelectItem> listaSelectItemTipoCoordenadorCurso;
	
	public List<SelectItem> getListaSelectItemTipoCoordenadorCurso(){
		if(listaSelectItemTipoCoordenadorCurso == null) {
			listaSelectItemTipoCoordenadorCurso = UtilSelectItem.getListaSelectItemEnum(TipoCoordenadorCursoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoCoordenadorCurso;
	}
	
	public Boolean getApresentarMensagemControleInclusaoDisciplinasMininoMaximo() {
		if (apresentarMensagemControleInclusaoDisciplinasMininoMaximo == null) {
			return Boolean.FALSE;
		}
		return apresentarMensagemControleInclusaoDisciplinasMininoMaximo;
	}

	public void setApresentarMensagemControleInclusaoDisciplinasMininoMaximo(
			Boolean apresentarMensagemControleInclusaoDisciplinasMininoMaximo) {
		this.apresentarMensagemControleInclusaoDisciplinasMininoMaximo = apresentarMensagemControleInclusaoDisciplinasMininoMaximo;
	}
	
	public void verificarApresentarMensagemControleInclusaoDisciplinasMininoMaximo() throws Exception {
		verificarApresentarMensagemControleInclusaoDisciplinasMininoMaximo(false);
	}

	public void verificarApresentarMensagemControleInclusaoDisciplinasMininoMaximo(boolean lancarExcecao) throws Exception {
		try {
			setApresentarMensagemControleInclusaoDisciplinasMininoMaximo(getGradeCurricularVO().getPeriodoLetivosVOs().stream().mapToInt(PeriodoLetivoVO::getNumeroMinimoCreditoAlunoPodeCursar).anyMatch(p -> p > 0) ? true :
				getGradeCurricularVO().getPeriodoLetivosVOs().stream().mapToInt(PeriodoLetivoVO::getNumeroMaximoCreditoAlunoPodeCursar).anyMatch(p -> p > 0) ? true :
					getGradeCurricularVO().getPeriodoLetivosVOs().stream().mapToInt(PeriodoLetivoVO::getNumeroMinimoCargaHorariaAlunoPodeCursar).anyMatch(p -> p > 0) ? true :
						getGradeCurricularVO().getPeriodoLetivosVOs().stream().mapToInt(PeriodoLetivoVO::getNumeroMaximoCargaHorariaAlunoPodeCursar).anyMatch(p -> p > 0) ? true : false);
		} catch (Exception e) {
			if (lancarExcecao) {
				throw e;
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getAsterisco() {
		return getApresentarMensagemControleInclusaoDisciplinasMininoMaximo() ?  "*" : "";
	}

	public String getUsernameExcluirAutorizacaoCurso() {
		if (usernameExcluirAutorizacaoCurso == null) {
			usernameExcluirAutorizacaoCurso = "";
		}
		return usernameExcluirAutorizacaoCurso;
	}

	public void setUsernameExcluirAutorizacaoCurso(String usernameExcluirAutorizacaoCurso) {
		this.usernameExcluirAutorizacaoCurso = usernameExcluirAutorizacaoCurso;
	}

	public String getSenhaExcluirAutorizacaoCurso() {
		return senhaExcluirAutorizacaoCurso;
	}

	public void setSenhaExcluirAutorizacaoCurso(String senhaExcluirAutorizacaoCurso) {
		this.senhaExcluirAutorizacaoCurso = senhaExcluirAutorizacaoCurso;
	}

	public List<UnidadeEnsinoCursoVO> getListaUnidadeEnsinoCursos() {
		if (listaUnidadeEnsinoCursos == null) {
			listaUnidadeEnsinoCursos = new ArrayList<>();
		}
		return listaUnidadeEnsinoCursos;
	}

	public void setListaUnidadeEnsinoCursos(List<UnidadeEnsinoCursoVO> listaUnidadeEnsinoCursos) {
		this.listaUnidadeEnsinoCursos = listaUnidadeEnsinoCursos;
	}
	
	public void montarListaSelectItemGrupoPessoa() {		
		try {
			getListaSelectItemGrupoPessoa().clear();
			getListaSelectItemGrupoPessoa().add(new SelectItem(0, ""));
			List<GrupoPessoaVO> lista = getFacadeFactory().getGrupoPessoaFacade().consultaGrupoPessoaCombobox(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone());
			lista.stream().map(q -> new SelectItem(q.getCodigo(), q.getNome())).forEach(getListaSelectItemGrupoPessoa()::add);			
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.GERAL, e);
		}
	}
	

	public List<SelectItem> getListaSelectItemGrupoPessoa() {
		if (listaSelectItemGrupoPessoa == null) {
			listaSelectItemGrupoPessoa = new ArrayList<>();
		}
		return listaSelectItemGrupoPessoa;
	}

	public void setListaSelectItemGrupoPessoa(List<SelectItem> listaSelectItemGrupoPessoa) {
		this.listaSelectItemGrupoPessoa = listaSelectItemGrupoPessoa;
	}

	public List<SelectItem> getListaSelectItemQuestionario() {
		if(listaSelectItemQuestionario == null) {
			listaSelectItemQuestionario = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}	
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Questionario</code>.
	 */
	public void montarListaSelectItemQuestionario(String prm) throws Exception {
		getListaSelectItemQuestionario().clear();
		try {
			getListaSelectItemQuestionario().add(new SelectItem(0, ""));
			consultarQuestionarioPorNome(prm).stream().map(q -> new SelectItem(q.getCodigo(), q.getDescricao())).forEach(getListaSelectItemQuestionario()::add);
			Collections.sort(getListaSelectItemQuestionario(), new SelectItemOrdemValor());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Questionario</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Questionario</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemQuestionario() {
		try {
			montarListaSelectItemQuestionario("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<QuestionarioVO> consultarQuestionarioPorNome(String nomePrm) throws Exception {
		List<QuestionarioVO> lista = getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum.PLANO_ENSINO, "AT", false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		if(Uteis.isAtributoPreenchido(getCursoVO().getQuestionarioVO())) {
			QuestionarioVO questionarioVO = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getCursoVO().getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(questionarioVO) && lista.stream().map(QuestionarioVO::getCodigo).noneMatch(getCursoVO().getQuestionarioVO().getCodigo()::equals)) {
				lista.add(questionarioVO);			
			}
		}
		return lista;
	}
	
	public void montarListaSelectItemQuestionarioEstagio() throws Exception {
		getListaSelectItemQuestionarioEstagio().clear();
		getListaSelectItemQuestionarioEstagioEdicao().clear();
		try {
			QuestionarioVO questionarioTemp = null;
			List<QuestionarioVO> lista = getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum.ESTAGIO, "AT", false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (GradeCurricularEstagioVO gce : getGradeCurricularVO().getListaGradeCurricularEstagioVO()) {
				if(Uteis.isAtributoPreenchido(gce.getQuestionarioRelatorioFinal())) {
					questionarioTemp = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gce.getQuestionarioRelatorioFinal().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(questionarioTemp) && lista.stream().map(QuestionarioVO::getCodigo).noneMatch(gce.getQuestionarioRelatorioFinal().getCodigo()::equals)) {
						lista.add(questionarioTemp);			
					}
				}	
				if(Uteis.isAtributoPreenchido(gce.getQuestionarioAproveitamentoPorDocenteRegular())) {
					questionarioTemp = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gce.getQuestionarioAproveitamentoPorDocenteRegular().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(questionarioTemp) && lista.stream().map(QuestionarioVO::getCodigo).noneMatch(gce.getQuestionarioAproveitamentoPorDocenteRegular().getCodigo()::equals)) {
						lista.add(questionarioTemp);			
					}
				}	
				if(Uteis.isAtributoPreenchido(gce.getQuestionarioAproveitamentoPorLicenciatura())) {
					questionarioTemp = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gce.getQuestionarioAproveitamentoPorLicenciatura().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(questionarioTemp) && lista.stream().map(QuestionarioVO::getCodigo).noneMatch(gce.getQuestionarioAproveitamentoPorLicenciatura().getCodigo()::equals)) {
						lista.add(questionarioTemp);			
					}
				}	
				if(Uteis.isAtributoPreenchido(gce.getQuestionarioEquivalencia())) {
					questionarioTemp = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(gce.getQuestionarioEquivalencia().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(questionarioTemp) && lista.stream().map(QuestionarioVO::getCodigo).noneMatch(gce.getQuestionarioEquivalencia().getCodigo()::equals)) {
						lista.add(questionarioTemp);			
					}
				}	
			}
			getListaSelectItemQuestionarioEstagio().add(new SelectItem(0, ""));
			getListaSelectItemQuestionarioEstagioEdicao().add(new SelectItem(0, ""));
			lista.stream().map(q -> new SelectItem(q.getCodigo(), q.getDescricao())).forEach(getListaSelectItemQuestionarioEstagio()::add);
			lista.stream().map(q -> new SelectItem(q.getCodigo(), q.getDescricao())).forEach(getListaSelectItemQuestionarioEstagioEdicao()::add);
			Collections.sort(getListaSelectItemQuestionarioEstagio(), new SelectItemOrdemValor());
			Collections.sort(getListaSelectItemQuestionarioEstagioEdicao(), new SelectItemOrdemValor());
		} catch (Exception e) {
			throw e;
		}
	}
	public void montarListaSelectItemTextoPadraoDeclaracao() throws Exception {
		getListaSelectItemTextoPadraoDeclaracao().clear();
		try {
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoTemp = null;
			List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("ES", 0, "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for (GradeCurricularEstagioVO gce : getGradeCurricularVO().getListaGradeCurricularEstagioVO()) {
				if(Uteis.isAtributoPreenchido(gce.getTextoPadraoDeclaracaoVO())) {
					textoPadraoDeclaracaoTemp = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(gce.getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(textoPadraoDeclaracaoTemp) && lista.stream().map(TextoPadraoDeclaracaoVO::getCodigo).noneMatch(gce.getTextoPadraoDeclaracaoVO().getCodigo()::equals)) {
						lista.add(textoPadraoDeclaracaoTemp);			
					}
				}
			}
			getListaSelectItemTextoPadraoDeclaracao().add(new SelectItem(0, ""));
			lista.stream().map(q -> new SelectItem(q.getCodigo(), q.getDescricao())).forEach(getListaSelectItemTextoPadraoDeclaracao()::add);
			Collections.sort(getListaSelectItemTextoPadraoDeclaracao(), new SelectItemOrdemValor());
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Boolean getPossuiPermissaoAlterarMatrizCurricularAtivaInativa() {
		if (possuiPermissaoAlterarMatrizCurricularAtivaInativa == null) {
			possuiPermissaoAlterarMatrizCurricularAtivaInativa = false;
		}
		return possuiPermissaoAlterarMatrizCurricularAtivaInativa;
	}

	public void setPossuiPermissaoAlterarMatrizCurricularAtivaInativa(Boolean possuiPermissaoAlterarMatrizCurricularAtivaInativa) {
		this.possuiPermissaoAlterarMatrizCurricularAtivaInativa = possuiPermissaoAlterarMatrizCurricularAtivaInativa;
	}

	public Boolean getPossuiPermissaoAlterarMatrizCurricularConstrucao() {
		if (possuiPermissaoAlterarMatrizCurricularConstrucao == null) {
			possuiPermissaoAlterarMatrizCurricularConstrucao = false;
		}
		return possuiPermissaoAlterarMatrizCurricularConstrucao;
	}

	public void setPossuiPermissaoAlterarMatrizCurricularConstrucao(Boolean possuiPermissaoAlterarMatrizCurricularConstrucao) {
		this.possuiPermissaoAlterarMatrizCurricularConstrucao = possuiPermissaoAlterarMatrizCurricularConstrucao;
	}
	
	public Boolean getPossuiPermissaoAlterarMatrizCurricular() {
		if (getGradeCurricularVO().isNovoObj()) {
			return true;
		}
		return (getGradeCurricularVO().getSituacao().equals("CO") && getPossuiPermissaoAlterarMatrizCurricularConstrucao()) 
				|| (!getGradeCurricularVO().getSituacao().equals("CO") && getPossuiPermissaoAlterarMatrizCurricularAtivaInativa());
	}
	
	public void inicializarDadosAlteracaoMatrizCurricularAtivaInativa(TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricularEnum) {
		setMsgAvisoAlteracaoGrade( new StringBuilder(""));
		setOncompleteModal("");
		setLogGradeCurricularVO(new LogGradeCurricularVO());
		setTipoAlteracaoMatrizCurricular(tipoAlteracaoMatrizCurricularEnum);
		setListaLogImpactoGradeDisciplinaVOs(new ArrayList<LogImpactoMatrizCurricularVO>(0));
	}
	
	public void validarDadosImpactoMatriculaFormadaInclusaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaIncluirVO) throws Exception {
		if (getValidarAlteracaoMatrizCurricularAtivaInativa() && gradeDisciplinaIncluirVO.isNovoObj()) {
			inicializarDadosAlteracaoMatrizCurricularAtivaInativa(TipoAlteracaoMatrizCurricularEnum.ADICIONAR_GRADE_DISCIPLINA);
			setListaLogImpactoGradeDisciplinaVOs(getFacadeFactory().getGradeDisciplinaFacade().validarDadosImpactoMatriculaFormadaInclusaoGradeDisciplinaMatrizCurricular(getGradeCurricularVO(), gradeDisciplinaIncluirVO, getMsgAvisoAlteracaoGrade(), getUsuarioLogado()));
			abrirModalAvisoAlteracaoMatrizCurricularAtivaInativa();
		}
	}
	
	public void validarDadosImpactoExclusaoGradeDisciplina(GradeDisciplinaVO gradeDisciplinaExcluirVO) throws Exception {
		if (gradeDisciplinaExcluirVO.getDeveValidarImpactoExclusao()) {
			inicializarDadosAlteracaoMatrizCurricularAtivaInativa(TipoAlteracaoMatrizCurricularEnum.EXCLUIR_GRADE_DISCIPLINA);
			getFacadeFactory().getGradeDisciplinaFacade().validarDadosImpactoExclusaoGradeDisciplina(getGradeCurricularVO(), gradeDisciplinaExcluirVO, getUsuarioLogado());
			getMsgAvisoAlteracaoGrade().append(gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().isEmpty() ? "" : "Possui Alteração.");
			if (!gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs().isEmpty()) {
				abrirModalAvisoAlteracaoMatrizCurricularAtivaInativa();
				setPossuiImpactoHistoricoExclusaoGradeDisciplina(true);
				setListaLogImpactoGradeDisciplinaVOs(gradeDisciplinaExcluirVO.getListaLogImpactoGradeDisciplinaVOs());
			} else {
				setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
			}
			gradeDisciplinaExcluirVO.setDeveValidarImpactoExclusao(false);
			setGradeDisciplinaExcluirVO(gradeDisciplinaExcluirVO);
		} else {
			getGradeDisciplinaExcluirVO().setListaLogImpactoGradeDisciplinaVOs(getListaLogImpactoGradeDisciplinaVOs());
		}
	}
	
	public void cancelarImpactoExclusaoGradeDisciplina() {
		if (Uteis.isAtributoPreenchido(getGradeDisciplinaExcluirVO().getCodigo())) {
			getGradeDisciplinaExcluirVO().setSofreuAlteracaoMatrizAtivaInativa(false);
			setPossuiImpactoHistoricoExclusaoGradeDisciplina(false);
			getGradeDisciplinaExcluirVO().setDeveValidarImpactoExclusao(true);
			inicializarDadosAlteracaoMatrizCurricularAtivaInativa(TipoAlteracaoMatrizCurricularEnum.NENHUM);
		}
	}
	
	public void cancelarAlteracaoGradeDisciplinaMatrizAtivaInativa() throws Exception {
		if (Uteis.isAtributoPreenchido(getGradeDisciplinasPeriodo().getCodigo())) {
			setGradeDisciplinasPeriodo(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplinasPeriodo().getCodigo(), getUsuarioLogado()));
			getGradeCurricularVO().substituirGradeDisciplina(getGradeDisciplinasPeriodo());
		}
	}
	
	public void validarDadosImpactoAlteracaoGradeDisciplina() throws Exception {
		try {

			if (getValidarAlteracaoMatrizCurricularAtivaInativa()) {
				inicializarDadosAlteracaoMatrizCurricularAtivaInativa(TipoAlteracaoMatrizCurricularEnum.EDITAR_GRADE_DISCIPLINA);
				executarCalcularCargaHorariaTeorica();
				getFacadeFactory().getGradeDisciplinaFacade().validarDadosImpactoAlteracaoGradeDisciplina(getGradeCurricularVO(), getGradeDisciplinasPeriodo(), getMsgAvisoAlteracaoGrade(), getUsuarioLogado());

				if (getGradeDisciplinasPeriodo().getDisciplinaComposta()) {
					if (!getMsgAvisoAlteracaoGrade().toString().equals("")) {
						cancelarAlteracaoGradeDisciplinaMatrizAtivaInativa();
						setMensagemDetalhada("msg_erro", "Não é possível realizar ALTERAÇÃO para uma Disciplina Composta.", Uteis.ERRO);
					}
					setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide();");
					return;
				}

				abrirModalAvisoAlteracaoMatrizCurricularAtivaInativa();
				setListaLogImpactoGradeDisciplinaVOs(getGradeDisciplinasPeriodo().getListaLogImpactoGradeDisciplinaVOs());
				if (getMsgAvisoAlteracaoGrade().length() == 0) {
					setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide();");
				}
			} else {
				executarCalcularCargaHorariaTeorica();
				setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide();");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void abrirModalAvisoAlteracaoMatrizCurricularAtivaInativa() {
		if (!getMsgAvisoAlteracaoGrade().toString().equals("")) {
			setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').show();");
		}
	}
	
	public void realizarVerificacaoImpactosAlteracaoMatrizCurricularPorTipoAlteracao() {
		try {
			switch (getTipoAlteracaoMatrizCurricular()) {
				case ADICIONAR_GRADE_DISCIPLINA:
					validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina();
					adicionarGradeDisciplina();
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case EDITAR_GRADE_DISCIPLINA:
					validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina();
					getGradeDisciplinasPeriodo().setListaLogImpactoGradeDisciplinaVOs(getListaLogImpactoGradeDisciplinaVOs());
					setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide(); RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					setMensagemID("msg_dados_editar");
					return;
				case EXCLUIR_GRADE_DISCIPLINA:
					validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina();
					setMensagemImpactoAlteracaoMatrizcurricularApresentada(true);
					removerGradeDisciplina();
					if (!getErroExclusaoGradeDisciplinaMatrizAtivaInativa()) {
						setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					}
					return;
				case EDITAR_GRUPO_OPTATIVA_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case EXCLUIR_GRUPO_OPTATIVA_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case EXCLUIR_GRUPO_OPTATIVA_PERIODO_LETIVO :
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case ADICIONAR_GRUPO_OPTATIVA_PERIODO_LETIVO :
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case EDITAR_GRUPO_OPTATIVA_PERIODO_LETIVO : 
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case ADICIONAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA: 
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					return;
				case EXCLUIR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					return;
				case EXCLUIR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					return;		
				case ADICIONAR_PRE_REQUISITO_GRUPO_OPTATIVA_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case ADICIONAR_PRE_REQUISITO_GRADE_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				
				case ADICIONAR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide(); RichFaces.$('panelDisciplinaPeriodo').hide();");
					return;
				case EDITAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					return;
				case EDITAR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
				case ADICIONAR_PERIODO_LETIVO:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					return;
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void cancelarImpactosAlteracaoMatrizCurricularPorTipoAlteracao() {
		try {
			switch (getTipoAlteracaoMatrizCurricular()) {
				
				case ADICIONAR_PERIODO_LETIVO:
					for(int i =0; i< getGradeCurricularVO().getPeriodoLetivosVOs().size();) {
						if(getGradeCurricularVO().getPeriodoLetivosVOs().get(i).isNovoObj()) {
							getGradeCurricularVO().getPeriodoLetivosVOs().remove(i);
						} else {
							i++;
						}
					}
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					break;
				
				case EXCLUIR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					break;
				case EXCLUIR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					break;
				case EDITAR_GRADE_DISCIPLINA_COMPOSTA_OPTATIVA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					break;
				case EDITAR_GRUPO_OPTATIVA_PERIODO_LETIVO:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					break;
				case EXCLUIR_GRUPO_OPTATIVA_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					break;
				case EXCLUIR_GRUPO_OPTATIVA_PERIODO_LETIVO:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					break;
				case ADICIONAR_GRUPO_OPTATIVA_PERIODO_LETIVO : 
					setOncompleteModal("RichFaces.$('panelGrupoOptativaPeriodo').hide();");
					break;
				case DEFINIR_PRE_REQUISITO_GRUPO_DISCIPLINA_OPTATIVA:
					setOncompleteModal("RichFaces.$('panelGrupoOptativaPreRequisito').hide();");
					break;
				case ADICIONAR_PRE_REQUISITO_GRADE_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplina').hide();");
					break;
				case EDITAR_GRADE_DISCIPLINA:
					cancelarAlteracaoGradeDisciplinaMatrizAtivaInativa();
					setOncompleteModal("RichFaces.$('panelDisciplinaPeriodo').hide();");
					break;
				case EXCLUIR_GRADE_DISCIPLINA:
					cancelarImpactoExclusaoGradeDisciplina();
					break;
				case EDITAR_GRUPO_OPTATIVA_DISCIPLINA: 
					setOncompleteModal("RichFaces.$('panelGrupoOptativaDisciplina').hide();");
					break;
				case ADICIONAR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
					break;
				case ADICIONAR_GRADE_DISCIPLINA:
					setOncompleteModal("RichFaces.$('panelAlteracaoMatrizCurricular').hide();");
					break;
				case EDITAR_GRADE_DISCIPLINA_COMPOSTA:
					setOncompleteModal("RichFaces.$('panelGradeDisciplinaComposta').hide();");
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getValidarAlteracaoMatrizCurricularAtivaInativa() {
		return !getGradeCurricularVO().getSituacao().equals("CO") && getPossuiPermissaoAlterarMatrizCurricularAtivaInativa();
	}

	public StringBuilder getMsgAvisoAlteracaoGrade() {
		if (msgAvisoAlteracaoGrade == null) {
			msgAvisoAlteracaoGrade = new StringBuilder();
		}
		return msgAvisoAlteracaoGrade;
	}
	
	public String getMsgAvisoAlteracaoGrade_Apresentar() {
		return getMsgAvisoAlteracaoGrade().toString();
	}

	public void setMsgAvisoAlteracaoGrade(StringBuilder msgAvisoAlteracaoGrade) {
		this.msgAvisoAlteracaoGrade = msgAvisoAlteracaoGrade;
	}

	public LogGradeCurricularVO getLogGradeCurricularVO() {
		if (logGradeCurricularVO == null) {
			logGradeCurricularVO = new LogGradeCurricularVO();
		}
		return logGradeCurricularVO;
	}

	public void setLogGradeCurricularVO(LogGradeCurricularVO logGradeCurricularVO) {
		this.logGradeCurricularVO = logGradeCurricularVO;
	}

	public TipoAlteracaoMatrizCurricularEnum getTipoAlteracaoMatrizCurricular() {
		if (tipoAlteracaoMatrizCurricular == null) {
			tipoAlteracaoMatrizCurricular = TipoAlteracaoMatrizCurricularEnum.NENHUM;
		}
		return tipoAlteracaoMatrizCurricular;
	}

	public void setTipoAlteracaoMatrizCurricular(TipoAlteracaoMatrizCurricularEnum tipoAlteracaoMatrizCurricular) {
		this.tipoAlteracaoMatrizCurricular = tipoAlteracaoMatrizCurricular;
	}

	public Boolean getMensagemImpactoAlteracaoMatrizcurricularApresentada() {
		if (mensagemImpactoAlteracaoMatrizcurricularApresentada == null) {
			mensagemImpactoAlteracaoMatrizcurricularApresentada = false;
		}
		return mensagemImpactoAlteracaoMatrizcurricularApresentada;
	}

	public void setMensagemImpactoAlteracaoMatrizcurricularApresentada(Boolean mensagemImpactoAlteracaoMatrizcurricularApresentada) {
		this.mensagemImpactoAlteracaoMatrizcurricularApresentada = mensagemImpactoAlteracaoMatrizcurricularApresentada;
	}

	public Boolean getPossuiImpactoHistoricoExclusaoGradeDisciplina() {
		if (possuiImpactoHistoricoExclusaoGradeDisciplina == null) {
			possuiImpactoHistoricoExclusaoGradeDisciplina = false;
		}
		return possuiImpactoHistoricoExclusaoGradeDisciplina;
	}

	public void setPossuiImpactoHistoricoExclusaoGradeDisciplina(Boolean possuiImpactoHistoricoExclusaoGradeDisciplina) {
		this.possuiImpactoHistoricoExclusaoGradeDisciplina = possuiImpactoHistoricoExclusaoGradeDisciplina;
	}

	public GradeDisciplinaVO getGradeDisciplinaExcluirVO() {
		if (gradeDisciplinaExcluirVO == null) {
			gradeDisciplinaExcluirVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaExcluirVO;
	}

	public void setGradeDisciplinaExcluirVO(GradeDisciplinaVO gradeDisciplinaExcluirVO) {
		this.gradeDisciplinaExcluirVO = gradeDisciplinaExcluirVO;
	}
	
	public GradeCurricularVO getGradeCurricularOriginalBaseDadosVO() {
		if (gradeCurricularOriginalBaseDadosVO == null) {
			gradeCurricularOriginalBaseDadosVO = new GradeCurricularVO();
		}
		return gradeCurricularOriginalBaseDadosVO;
	}

	public void setGradeCurricularOriginalBaseDadosVO(GradeCurricularVO gradeCurricularOriginalBaseDadosVO) {
		this.gradeCurricularOriginalBaseDadosVO = gradeCurricularOriginalBaseDadosVO;
	}

	public List<LogImpactoMatrizCurricularVO> getListaLogImpactoGradeDisciplinaVOs() {
		if (listaLogImpactoGradeDisciplinaVOs == null) {
			listaLogImpactoGradeDisciplinaVOs = new ArrayList<LogImpactoMatrizCurricularVO>(0);
		}
		return listaLogImpactoGradeDisciplinaVOs;
	}

	public void setListaLogImpactoGradeDisciplinaVOs(List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs) {
		this.listaLogImpactoGradeDisciplinaVOs = listaLogImpactoGradeDisciplinaVOs;
	}

	public List getTipoConsultaComboLogOperacaoExclusaoGradeDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("FORA_GRADE", "Deixar Histórico Fora da Grade"));
        itens.add(new SelectItem("EXCLUIR_HISTORICO", "Excluir Histórico"));
        return itens;
    }

	public Boolean getErroExclusaoGradeDisciplinaMatrizAtivaInativa() {
		if (erroExclusaoGradeDisciplinaMatrizAtivaInativa == null) {
			erroExclusaoGradeDisciplinaMatrizAtivaInativa = false;
		}
		return erroExclusaoGradeDisciplinaMatrizAtivaInativa;
	}

	public void setErroExclusaoGradeDisciplinaMatrizAtivaInativa(Boolean erroExclusaoGradeDisciplinaMatrizAtivaInativa) {
		this.erroExclusaoGradeDisciplinaMatrizAtivaInativa = erroExclusaoGradeDisciplinaMatrizAtivaInativa;
	}
	
	public void validarDadosLiEConcordoComOsTermosAlteracaoGradeDsiciplina() throws Exception {
		for (LogImpactoMatrizCurricularVO logImpactoGradeDisciplinaVO : getListaLogImpactoGradeDisciplinaVOs()) {
			if (!logImpactoGradeDisciplinaVO.getLiEConcordoComOsTermos()) {
				throw new Exception("Para prosseguir com a operação é necessário marcar a opção que concorda com as alterações "+logImpactoGradeDisciplinaVO.getTituloImpactoMatrizCurricularEnum().getTitulo().toUpperCase()+"");
			}
		}
	}

	public DisciplinaVO getDisciplinaIncluirVO() {
		if (disciplinaIncluirVO == null) {
			disciplinaIncluirVO = new DisciplinaVO();
		}
		return disciplinaIncluirVO;
	}

	public void setDisciplinaIncluirVO(DisciplinaVO disciplinaIncluirVO) {
		this.disciplinaIncluirVO = disciplinaIncluirVO;
	}
	
	public void marcarDesmarcarOpcaoLiConcordoTermos() {
		LogImpactoMatrizCurricularVO obj = (LogImpactoMatrizCurricularVO) context().getExternalContext().getRequestMap().get("logImpactoGradeDisciplinaItem");
		if (obj.getLiEConcordoComOsTermos()) {
			obj.setLiEConcordoComOsTermos(false);
		} else {
			obj.setLiEConcordoComOsTermos(true);
		}
	}

	public Boolean getAdicionandoNovaDisciplinaComposta() {
		if (adicionandoNovaDisciplinaComposta == null) {
			adicionandoNovaDisciplinaComposta = false;
		}
		return adicionandoNovaDisciplinaComposta;
	}

	public void setAdicionandoNovaDisciplinaComposta(Boolean adicionandoNovaDisciplinaComposta) {
		this.adicionandoNovaDisciplinaComposta = adicionandoNovaDisciplinaComposta;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoLdap() {
		if (listaSelectItemConfiguracaoLdap == null) {
			listaSelectItemConfiguracaoLdap = new ArrayList<>();
		}
		return listaSelectItemConfiguracaoLdap;
	}

	public void setListaSelectItemConfiguracaoLdap(List<SelectItem> listaSelectItemConfiguracaoLdap) {
		this.listaSelectItemConfiguracaoLdap = listaSelectItemConfiguracaoLdap;
	}

	public void montarListaSelectItemConfiguracaoLdap() throws Exception {
		getListaSelectItemConfiguracaoLdap().clear();
		try {
			getListaSelectItemConfiguracaoLdap().add(new SelectItem(0, ""));
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdaps()
					.stream()
					.map(q -> new SelectItem(q.getCodigo(), q.getDominio()))
					.forEach(getListaSelectItemConfiguracaoLdap()::add);
			Collections.sort(getListaSelectItemConfiguracaoLdap(), new SelectItemOrdemValor());
		} catch (Exception e) {
			throw e;
		}
	}
	
	private List<SelectItem> listaSelectItemBimestre;


	public List<SelectItem> getListaSelectItemBimestre() {
		if(listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(0, ""));
			listaSelectItemBimestre.add(new SelectItem(1, "1"));
			listaSelectItemBimestre.add(new SelectItem(2, "2"));
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}

	public List<SelectItem> getListaSelectItemEixoCurso() {
			if(listaSelectItemEixoCurso == null) {
				listaSelectItemEixoCurso = new ArrayList<>(0);
			}
		return listaSelectItemEixoCurso;
	}

	public void setListaSelectItemEixoCurso(List<SelectItem> listaSelectItemEixoCurso) {
		this.listaSelectItemEixoCurso = listaSelectItemEixoCurso;
	}
	
	public void montarListaSelectItemEixoCurso() {
		try {
			setListaSelectItemEixoCurso(new ArrayList<>(0));
			getListaSelectItemEixoCurso().add(new SelectItem(0, ""));
			for (EixoCursoVO eixoCursoVO : getFacadeFactory().getEixoCursoFacade().consultarTodos(Uteis.NIVELMONTARDADOS_COMBOBOX)) {
				getListaSelectItemEixoCurso().add(new SelectItem(eixoCursoVO.getCodigo(), eixoCursoVO.getNome()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectConteudoMasterBlackboard() {
		if (listaSelectConteudoMasterBlackboard == null) {
			listaSelectConteudoMasterBlackboard = new ArrayList<SelectItem>(0);
		}
		return listaSelectConteudoMasterBlackboard;
	}

	public void setListaSelectConteudoMasterBlackboard(List<SelectItem> listaSelectConteudoMasterBlackboard) {
		this.listaSelectConteudoMasterBlackboard = listaSelectConteudoMasterBlackboard;
	}
	
	public void montarListaSelectConteudoMasterBlackboard() {
		try {
			List<SalaAulaBlackboardVO> resultadoConsulta = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaSalaAulaBlackboardConteudoMaster(getUsuarioLogado());
			setListaSelectConteudoMasterBlackboard(UtilSelectItem.getListaSelectItem(resultadoConsulta, "idSalaAulaBlackboard", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	
    public void habilidadeConfirmacaoMatricula() {
    	try {
    		if (getCursoVO().getHabilitarMensagemNotificacaoNovaMatricula()) {
    			if(getCursoVO().getMensagemConfirmacaoNovaMatricula() != null){
        			PersonalizacaoMensagemAutomaticaVO mensagemAuto = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA, getCursoVO().getCodigo(), false, getUsuarioLogado());
        			if (mensagemAuto == null) {
        				mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
        				mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA);
        				mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.getTags_Apresentar());
        				mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.toString()));
        				mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.name()));
        				mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
        				mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.name() + "_SMS"));
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
        				mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
        			}else if(!mensagemAuto.getCursoVO().getCodigo().equals(getCursoVO().getCodigo())) {        				
        				mensagemAuto.setCodigo(0);
        				mensagemAuto.setCursoVO(getCursoVO());        				
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(false);
        			}
        			getCursoVO().setMensagemConfirmacaoNovaMatricula(mensagemAuto);
        		}
    		}
    	} catch (Exception e) {
    		PersonalizacaoMensagemAutomaticaVO mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
			mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA);
			mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.getTags_Apresentar());
			mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.toString()));
			mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.name()));
			mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
			mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA.name() + "_SMS"));
			mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
			mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
			getCursoVO().setMensagemConfirmacaoNovaMatricula(mensagemAuto);
		}
    }

    public void habilidadeRenovacaoMatricula() {
    	try {
    		if (getCursoVO().getHabilitarMensagemNotificacaoRenovacaoMatricula()) {
    			if(getCursoVO().getMensagemRenovacaoMatricula() != null){
        			PersonalizacaoMensagemAutomaticaVO mensagemAuto = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA, getCursoVO().getCodigo(), false, getUsuarioLogado());
        			if (mensagemAuto == null) {
        				mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
        				mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA);
        				mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.getTags_Apresentar());
        				mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.toString()));
        				mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.name()));
        				mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
        				mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.name() + "_SMS"));
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
        				mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
        			} else if(!mensagemAuto.getCursoVO().getCodigo().equals(getCursoVO().getCodigo())) {        				
        				mensagemAuto.setCodigo(0);
        				mensagemAuto.setCursoVO(getCursoVO());        				
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(false);
        			}
        			getCursoVO().setMensagemRenovacaoMatricula(mensagemAuto);
        		}
    		}
    	} catch (Exception e) {
    		PersonalizacaoMensagemAutomaticaVO mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
    		
			mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA);
			mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.getTags_Apresentar());
			mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.toString()));
			mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.name()));
			mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
			mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA.name() + "_SMS"));
			mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
			mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
			getCursoVO().setMensagemRenovacaoMatricula(mensagemAuto);
    	}
    }
    
    public void consultarFuncionarioPelaMatricula() {
		try {
			getListaConsultaFuncionario().clear();
			getListaConsulta().clear();
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(getCursoCoordenadorVO().getFuncionario().getMatricula(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Funcionario de matrícula " + getFuncionarioVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			selecionarFuncionarioMatricula(obj);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void selecionarFuncionarioMatricula(FuncionarioVO obj) {
		getCursoCoordenadorVO().setFuncionario(obj);
	}
    
    public void habilidadeMensagemAtivacaoPreMatriculaEntregaDocumento() {
    	try {
    		if (getCursoVO().getHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento()) {
    			if(getCursoVO().getMensagemAtivacaoPreMatriculaEntregaDocumento() != null){
        			PersonalizacaoMensagemAutomaticaVO mensagemAuto = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate_Curso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE, getCursoVO().getCodigo(), false, getUsuarioLogado());
        			if (mensagemAuto == null) {
        				mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
        				mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE);
        				mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.getTags_Apresentar());
        				mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.toString()));
        				mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.name()));
        				mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
        				mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.name() + "_SMS"));
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
        				mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
        			} else if(!mensagemAuto.getCursoVO().getCodigo().equals(getCursoVO().getCodigo())) {        				
        				mensagemAuto.setCodigo(0);
        				mensagemAuto.setCursoVO(getCursoVO());        				
        				mensagemAuto.setDesabilitarEnvioMensagemAutomatica(false);
        			}
        			getCursoVO().setMensagemAtivacaoPreMatriculaEntregaDocumento(mensagemAuto);
        		}
    		}
    	} catch (Exception e) {
    		PersonalizacaoMensagemAutomaticaVO mensagemAuto = new PersonalizacaoMensagemAutomaticaVO();
			mensagemAuto.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE);
			mensagemAuto.setTags(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.getTags_Apresentar());
			mensagemAuto.setAssunto(UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.toString()));
			mensagemAuto.setMensagem(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.name()));
			mensagemAuto.setMensagem(mensagemAuto.getMensagemComLayout(mensagemAuto.getMensagem()));
			mensagemAuto.setMensagemSMS(UteisJSF.internacionalizar("msg_TemplateMensagemAutomaticaEnum_" + TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE.name() + "_SMS"));
			mensagemAuto.setDesabilitarEnvioMensagemAutomatica(true);
			mensagemAuto.setDesabilitarEnvioMensagemSMSAutomatica(true);
			getCursoVO().setMensagemAtivacaoPreMatriculaEntregaDocumento(mensagemAuto);
		}
    }
    
          
       
	public void montarListaSelectItemTextoPadraoContratoMatriculaCalouro() {
		try {
			List<TextoPadraoVO> textoPadraoVOMatricula = getFacadeFactory().getTextoPadraoFacade().
					consultarPorTipoNivelComboBox(TipoTextoPadrao.CONTRATO.getValor(), null, "AT", false,getUsuarioLogado());
			getCursoVO().setListaSelectItemTextoPadraoContratoMatriculaCalouro(UtilSelectItem.getListaSelectItem(textoPadraoVOMatricula, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
       
	public List getListaSelectItemTipoAutorizacaoCurso() {
		if (listaSelectItemTipoAutorizacaoCurso == null) {
			listaSelectItemTipoAutorizacaoCurso = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAutorizacaoCurso.add(new SelectItem("", ""));
			for (TipoAutorizacaoCursoEnum enumerador : TipoAutorizacaoCursoEnum.values()) {
				listaSelectItemTipoAutorizacaoCurso.add(new SelectItem(enumerador, enumerador.getValorApresentar()));
			}
		}
		return listaSelectItemTipoAutorizacaoCurso;
	}
	
	public DataModelo getControleConsultaOtimizadoPessoa() {
		if (controleConsultaOtimizadoPessoa == null) {
			controleConsultaOtimizadoPessoa = new DataModelo();
		}
		return controleConsultaOtimizadoPessoa;
	}
	
	public void setControleConsultaOtimizadoPessoa(DataModelo controleConsultaOtimizadoPessoa) {
		this.controleConsultaOtimizadoPessoa = controleConsultaOtimizadoPessoa;
	}
	
	public List<SelectItem> getTipoConsultaComboDocenteResposavelEstagio() {
		if (tipoConsultaComboDocenteResposavelEstagio == null) {
			tipoConsultaComboDocenteResposavelEstagio = new ArrayList<>(0);
			tipoConsultaComboDocenteResposavelEstagio.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDocenteResposavelEstagio.add(new SelectItem("cpf", "CPF"));
		}
		return tipoConsultaComboDocenteResposavelEstagio;
	}
	
	public GradeCurricularEstagioVO getGradeCurricularEstagioEditar() {
		if (gradeCurricularEstagioEditar == null) {
			gradeCurricularEstagioEditar = new GradeCurricularEstagioVO();
		}
		return gradeCurricularEstagioEditar;
	}
	
	public void setGradeCurricularEstagioEditar(GradeCurricularEstagioVO gradeCurricularEstagioEditar) {
		this.gradeCurricularEstagioEditar = gradeCurricularEstagioEditar;
	}
	
	public Boolean getEditarGradeCurricularEstagio() {
		if (editarGradeCurricularEstagio == null) {
			editarGradeCurricularEstagio = Boolean.FALSE;
		}
		return editarGradeCurricularEstagio;
	}
	
	public void setEditarGradeCurricularEstagio(Boolean editarGradeCurricularEstagio) {
		this.editarGradeCurricularEstagio = editarGradeCurricularEstagio;
	}
	
	public void inicializacaoConsultaDocente() {
		setGradeCurricularEstagioEditar(new GradeCurricularEstagioVO());
		setEditarGradeCurricularEstagio(Boolean.FALSE);
		limparDadosConsultaDocente();
	}
	
	public void consultarDocenteResponsavelEstagio() {
		try {
			getFacadeFactory().getPessoaFacade().consultarDocentesEstagioOtimizado(getControleConsultaOtimizadoPessoa());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarDocenteResposavelEstagio() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("docenteItens");
		try {
			if (Uteis.isAtributoPreenchido(obj)) {
				if (getEditarGradeCurricularEstagio()) {
					getGradeCurricularEstagioEditar().setDocenteResponsavelEstagio(obj);
					UtilReflexao.adicionarObjetoLista(getGradeCurricularVO().getListaGradeCurricularEstagioVO(), getGradeCurricularEstagioEditar(), new AtributoComparacao().add("editar", Boolean.TRUE));
					getGradeCurricularVO().getListaGradeCurricularEstagioVO().forEach(e -> e.setEditar(Boolean.FALSE));
					setGradeCurricularEstagioEditar(new GradeCurricularEstagioVO());
				} else {
					getGradeCurricularEstagioVO().setDocenteResponsavelEstagio(obj);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void scrollerListenerPessoa(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizadoPessoa().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizadoPessoa().setPage(DataScrollEvent.getPage());
		consultarDocenteResponsavelEstagio();
	}
	
	public void selecionarComponenteEstagio() {
		getGradeCurricularVO().getListaGradeCurricularEstagioVO().forEach(e -> e.setEditar(Boolean.FALSE));
		setGradeCurricularEstagioEditar(new GradeCurricularEstagioVO());
		setEditarGradeCurricularEstagio(Boolean.TRUE);
		limparDadosConsultaDocente();
		GradeCurricularEstagioVO obj = (GradeCurricularEstagioVO) context().getExternalContext().getRequestMap().get("gradeCurricularEstagioItens");
		try {
			obj.setEditar(Boolean.TRUE);
			setGradeCurricularEstagioEditar((GradeCurricularEstagioVO) obj.clone());
		} catch (CloneNotSupportedException e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void fecharModalDocenteResposavelEstagio() {
		if (getEditarGradeCurricularEstagio()) {
			getGradeCurricularVO().getListaGradeCurricularEstagioVO().forEach(e -> e.setEdicaoManual(Boolean.FALSE));
			setGradeCurricularEstagioEditar(new GradeCurricularEstagioVO());
		}
	}
	
	public void limparDadosConsultaDocente() {
		setControleConsultaOtimizadoPessoa(new DataModelo());
	}

	public void limparDadosDocenteResposanvelEstagio(GradeCurricularEstagioVO gradeCurricularEstagioVO) {
		gradeCurricularEstagioVO.setDocenteResponsavelEstagio(new PessoaVO());
	}

}
