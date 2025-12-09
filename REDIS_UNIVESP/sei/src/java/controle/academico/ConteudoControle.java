package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.component.UITree;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.event.TreeSelectionChangeEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import appletImpressaoMatricial.ArquivoHelper;
import controle.arquitetura.DataModelo;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SuperControle;
import controle.arquitetura.TreeNodeCustomizado;
import controle.ead.AvaliacaoOnlineAlunoSuperControle;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoCategoriaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoPizzaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoSerieVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumInteracaoVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.IconeVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.RecursoEducacionalVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumEnum;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumInteracaoEnum;
import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoConteudistaEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.AnotacaoDisciplinaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.NivelImportanciaEnum;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.PeriodoDisponibilizacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDefinicaoPeriodoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoProvaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ConteudoControle")
@Scope("viewScope")
@Lazy
public class ConteudoControle extends AvaliacaoOnlineAlunoSuperControle {

	/**
     * 
     */
	private static final long serialVersionUID = 6277259802775630961L;
	private ConteudoVO conteudo;
	private UnidadeConteudoVO unidadeConteudoVO;
	protected List<DisciplinaVO> listaConsultaDisciplina;
	protected List<ConteudoVO> listaConsultaConteudo;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private Boolean incluirUnidade;
	private Boolean editarUnidade;
	private ConteudoUnidadePaginaVO conteudoUnidadePagina;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional;
	private Integer irPagina;
	private Boolean recursoAdicionadoComSucesso = false;
	private List<RecursoEducacionalVO> recursoEducacionalVOs;
	private List<IconeVO> iconeVOs;
	private List<ForumVO> forumVOs;
	private List<ListaExercicioVO> listaExercicioVOs;
	private TreeNodeCustomizado treeNodeCustomizado;
	private TipoRecursoEducacionalEnum tipoRecursoEducacional;
	private String filtroNomeDisciplina;
	private Boolean utilizarRecursoEducacionalGatilho;
	private Boolean utilizarRecursoEducacionalConteudo;
	private String categoriaGraficoConteudoUnidadePagina;
	private String serieGraficoConteudoUnidadePagina;
	private Double valorGraficoConteudoUnidadePagina;
	private String categoriaGraficoConteudoUnidadePaginaRecursoEducacional;
	private String serieGraficoConteudoUnidadePaginaRecursoEducacional;
	private Double valorGraficoConteudoUnidadePaginaRecursoEducacional;
	private Boolean visualizarGraficoConteudoUnidadePagina;
	private Boolean visualizarGraficoConteudoUnidadePaginaRecursoEducacional;
	private Boolean apresentarListaRecursoEducacionalConteudo;
	private Boolean apresentarListaRecursoEducacionalGatilho;
	private Boolean uploadIconeVoltar;
	private Boolean naoExisteConteudoCadastrado;
	private Boolean apresentarResumoFinalConteudo;
	private Integer codigoDisciplinaAluno;
	private String matriculaAluno;
	private Date dataUltimoAcesso;
	private Integer totalAcessoConteudo;
	private Integer totalAcessoPagina;
	private Double totalPontoAtingido;
	private String porcentagemEvolucaoConteudo;
	protected List<SelectItem> listaSelectItemDisciplina;

	private Integer qtdeExercicioFacil;
	private Integer qtdeExercicioMedio;
	private Integer qtdeExercicioDificil;
	private QuestaoVO questaoVO;
	private DataModelo controleConsultaQuestao;
	private Boolean publicarImagem;
	private String nomeImagem;
	private Boolean emularAcessoAluno;
	private Boolean emularAcessoProfessor;
	private Boolean emulandoAcessoPagina;
	private Integer codigoUnidadeConteudoEmular;
	private Integer paginaEmular;
	private Boolean apresentarGatilho;
	private Boolean replicarBackgroundTodoConteudo;
	private AnotacaoDisciplinaVO anotacaoDisciplinaVO;
	private Boolean imagemComAnotacaoSemAnotacao;
	private ConfiguracaoEADVO configuracaoEADVO;
	private String caminhoArquivoConteudoUnidadePaginaRecursoEducacional;
	private List listaSelectItemDisciplinasProfessor;
	private DisciplinaVO disciplinaVO;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;
	private List<SelectItem> listaSelectItemTemaAssuntoVOs;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private String modalPanelAddRecursoEducacional;
	private List<SelectItem> comboBoxSituacaoConteudoConsulta;
	private String campoSituacaoConteudo;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	private List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacaoAluno;
	private GestaoEventoConteudoTurmaInteracaoAtaVO gestaoEventoConteudoTurmaInteracaoAta;
	private GestaoEventoConteudoTurmaResponsavelAtaVO gestaoEventoConteudoTurmaResponsavelAta;
	private MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum;
	private boolean apresentarConteudoUnidadePaginaRecursoEducacionalProfessor;
	private List<SelectItem> listaSelectPessoa;
	private boolean usuarioRedatorAta;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalAntesAlteracao;
	private String modalPanelDisciplinaConteudoClone;
	private DisciplinaVO disciplinaModalConteudoClone;
	private boolean agendarLiberacaoRecursoEducacional = false;
	
	private List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	private String fecharModalPanelQuestoesFixasRandomicas;
	private Integer nivelFacil;
	private Integer nivelMedio;
	private Integer nivelDificil;
	private Integer qualquerNivel;
	private boolean usoOnline = false; 
	private boolean usoPresencial = false;  
	private boolean usoExercicio = false;

	private String widthFormRecursoEducacional;
	private String heightFormRecursoEducacional;
	private String widthPanelGroupRecursoEducacional;
	private String heightPanelGroupRecursoEducacional;
	
	private boolean maximizarTela = false;
	private boolean maximizarTelaGeralConteudo = true;
	private String heightMaximizarTelaGeralConteudo;
	private String imagemBackgroundPagina;
	private String imagemBackgroundConteudo;
	private String imagemBackgroundUnidadeConteudo;
	private List<SelectItem> listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	private Boolean permitirAlteracaoValorNotaAvaliacaoOnline;
	private String valorConsulta;
	private String campoConsulta;
	private List<SelectItem> listaSelectItemVariavelNotaCfaVOs;
	private Boolean pendenciaAvaliacaoOnlineRea;
	private String OncompletePanelAddRecursoEducacional;
	private List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs; 

	
	public ConteudoControle() {
		super();
		limparMensagem();
		setConteudo(new ConteudoVO());
		setUnidadeConteudoVO(new UnidadeConteudoVO());
		setAnotacaoDisciplinaVO(new AnotacaoDisciplinaVO());
		setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
		setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	@PostConstruct
	public void init() {
		restauraValorPadraoPanelRecursoEducacional();
		if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("pagina") != null) {
			try {
				editarIndiceConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorChavePrimaria(Integer.valueOf(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("pagina")), NivelMontarDados.BASICO, false, getUsuarioLogado()));							
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}else if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			inicializarDadosVisaoAlunoProfessor();
			verificarAnotacao();
			if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("avaliacaoOnlineRea"))) {
				setAvaliacaoOnlineMatriculaVO((AvaliacaoOnlineMatriculaVO) context().getExternalContext().getSessionMap().get("avaliacaoOnlineMatriculaVO"));
				setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getSessionMap().get("conteudoUnidadePaginaRecursoEducacionalVO"));
				setApresentarOpcaoFecharGatilho((boolean) context().getExternalContext().getSessionMap().get("apresentarOpcaoFecharGatilho"));
				try {
					carregarAvaliacaoOnlineRea();
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				}
				setApresentarModalMensagemAvisoAluno(false);
				setModalMensagemAvisoAluno("");
				setApresentarGatilho(true);				
				context().getExternalContext().getSessionMap().remove("avaliacaoOnlineRea");
				context().getExternalContext().getSessionMap().remove("avaliacaoOnlineMatriculaVO");
				context().getExternalContext().getSessionMap().remove("conteudoUnidadePaginaRecursoEducacionalVO");
				context().getExternalContext().getSessionMap().remove("apresentarOpcaoFecharGatilho");
				
				
			}
		}else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("booleanoEditarConteudoVisaoProfessor")) && (Boolean) context().getExternalContext().getSessionMap().get("booleanoEditarConteudoVisaoProfessor")) {
				montarDadosPopUpVisaoProfessor();
			} else if (Uteis.isAtributoPreenchido(context().getExternalContext().getSessionMap().get("booleanoNovoObjetoVisaoProfessor")) && (Boolean) context().getExternalContext().getSessionMap().get("booleanoNovoObjetoVisaoProfessor")) {
				setIdControlador("ConteudoControle_0");
				getConteudo().setUsoExclusivoProfessor(true);
				getConteudo().setProfessor(getUsuarioLogado().getPessoa());
				context().getExternalContext().getSessionMap().remove("booleanoNovoObjetoVisaoProfessor");
				
				setMensagemID("msg_entre_dados", Uteis.ALERTA);
			} else if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("emulandoGestao"))) {
				emularAcessoConteudoPorGestaoEventoConteudo();
			}
			verificarPermissaoParaManipulacaoDeDadosEspecificosParaProfessor();
			getConteudo().setMaximixado(false);
			visualizarIndiceConteudo();		
		}
	}
	

	public void verificarPermissaoParaManipulacaoDeDadosEspecificosParaProfessor() {
		try {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarConteudoQualquerDisciplina()) {
				return;
			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarConteudoApenasAulasProgramadas()) {
				montarListaSelectItemDisciplinasProfessor();
				return;
			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarApenasConteudosExclusivos()) {
				montarListaSelectItemDisciplinasConteudoExclusivoProfessor();
				return;
			} else {
				montarListaSelectItemDisciplinasProfessor();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarDadosPopUpVisaoProfessor() {
		try {
			setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria((Integer) context().getExternalContext().getSessionMap().get("codigoConteudo"), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			context().getExternalContext().getSessionMap().remove("booleanoEditarConteudoVisaoProfessor");
			context().getExternalContext().getSessionMap().remove("codigoConteudo");
			setIdControlador("ConteudoControle_"+getConteudo().getCodigo());
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				getConteudo().setProfessor(getUsuarioLogado().getPessoa());
			}
			getFacadeFactory().getConteudoFacade().persistir(getConteudo(), true, getUsuarioLogado(), false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		try {
			verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor();
			setUnidadeConteudoVO(null);
			setConteudoUnidadePagina(null);
			setConteudoUnidadePaginaRecursoEducacional(null);
			setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(((ConteudoVO) context().getExternalContext().getRequestMap().get("conteudoItem")).getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			montarComboBoxTemaAssuntoDisciplina();
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
	}

	public String editarVisaoAluno() {
		try {
			setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(((ConteudoVO) context().getExternalContext().getRequestMap().get("conteudo")).getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			getConteudo().setMaximixado(false);
			visualizarIndiceConteudo();		
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return getCaminhoRedirecionamentoNavegacao("conteudoCons.xhtml");
	}

	public void ativar() {
		try {
			getFacadeFactory().getConteudoFacade().ativarConteudo(getConteudo(), true, getUsuarioLogado(), false);
			setMensagemID("msg_dados_ativados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativar() {
		try {
			getFacadeFactory().getConteudoFacade().inativarConteudo(getConteudo(), true, getUsuarioLogado(), false);
			setMensagemID("msg_dados_inativados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void emConstrucao() {
		try {
			getFacadeFactory().getConteudoFacade().emConstrucaoConteudo(getConteudo(), true, getUsuarioLogado(), false);
			setMensagemID("msg_dados_emConstrucao", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void emularAcessoConteudoPorGestaoEventoConteudo() {

		setEmularAcessoProfessor(true);
		setEmularAcessoAluno(false);
		setEmulandoAcessoPagina(false);
		setConteudo((ConteudoVO) context().getExternalContext().getSessionMap().get("conteudoGestao"));
		setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
		getMatriculaPeriodoTurmaDisciplinaVO().setConteudo((ConteudoVO) context().getExternalContext().getSessionMap().get("conteudoGestao"));
		getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo((Integer) context().getExternalContext().getSessionMap().get("turmaGestao"));
		getMatriculaPeriodoTurmaDisciplinaVO().setAno((String) context().getExternalContext().getSessionMap().get("anoGestao"));
		getMatriculaPeriodoTurmaDisciplinaVO().setSemestre((String) context().getExternalContext().getSessionMap().get("semestreGestao"));
		getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setCodigo((Integer) context().getExternalContext().getSessionMap().get("disciplinaGestao"));
		setCodigoUnidadeConteudoEmular(0);
		setPaginaEmular(0);
		setApresentarOpcaoFecharGatilho(false);
		setApresentarResumoFinalConteudo(false);
		setUnidadeConteudoVO(new UnidadeConteudoVO());
		inicializarAcesso();
		if (!getConteudo().getUnidadeConteudoVOs().isEmpty()) {
			inicializarUltimoAcessoAlunoConteudo();
		}
		context().getExternalContext().getSessionMap().remove("emulandoGestao");
		context().getExternalContext().getSessionMap().remove("conteudoGestao");
		context().getExternalContext().getSessionMap().remove("turmaGestao");
		context().getExternalContext().getSessionMap().remove("anoGestao");
		context().getExternalContext().getSessionMap().remove("semestreGestao");
		context().getExternalContext().getSessionMap().remove("disciplinaGestao");
		getConteudo().setMaximixado(false);
		visualizarIndiceConteudo();		
	}

	public String emularAcessoConteudo() {
		try {
			setEmularAcessoAluno(true);
			setEmulandoAcessoPagina(false);
			setCodigoUnidadeConteudoEmular(0);
			setPaginaEmular(0);
			setApresentarOpcaoFecharGatilho(false);
			setApresentarResumoFinalConteudo(false);
			setUnidadeConteudoVO(new UnidadeConteudoVO());
			inicializarAcesso();
			
			getConteudo().setMaximixado(false);
			visualizarIndiceConteudo();		
			if (!getConteudo().getUnidadeConteudoVOs().isEmpty()) {
				inicializarUltimoAcessoAlunoConteudo();
			}
			return getCaminhoRedirecionamentoNavegacao("/visaoAluno/conteudoAlunoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		}
	}

	public String emularAcessoPagina() {
		try {
			setEmularAcessoAluno(true);
			setEmulandoAcessoPagina(true);
			setCodigoUnidadeConteudoEmular(getUnidadeConteudoVO().getCodigo());
			getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
			setPaginaEmular(getConteudoUnidadePagina().getPagina());
			setApresentarOpcaoFecharGatilho(false);
			setApresentarResumoFinalConteudo(false);
			inicializarApresentacaoGatilhoAnterior();
			visualizarGraficoConteudoUnidadePagina();
			getConteudo().setMaximixado(false);
			visualizarIndiceConteudo();		
			return getCaminhoRedirecionamentoNavegacao("/visaoAluno/conteudoAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		}
	}

	public String voltarEmularConteudo() {
		try {
			setVisualizarGraficoConteudoUnidadePagina(false);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);

			if (getEmulandoAcessoPagina()) {
				for (UnidadeConteudoVO obj : getConteudo().getUnidadeConteudoVOs()) {
					if (obj.getCodigo().intValue() == getCodigoUnidadeConteudoEmular().intValue()) {
						setUnidadeConteudoVO(new UnidadeConteudoVO());
						setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
						setUnidadeConteudoVO(obj);

						for (ConteudoUnidadePaginaVO pagina : obj.getConteudoUnidadePaginaVOs()) {
							if (pagina.getPagina().intValue() == getPaginaEmular().intValue()) {
								setConteudoUnidadePagina(pagina);
								break;
							}
						}
						break;
					}
				}
				if (getConteudoUnidadePagina().getCodigo().intValue() == 0) {
					getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(getUnidadeConteudoVO().getCodigo());
					getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getPaginas() + 1);
					getConteudoUnidadePagina().setPagina(getUnidadeConteudoVO().getPaginas());
					setIrPagina(getUnidadeConteudoVO().getPaginas());
					setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
					inicializarRecursoEducacional();
				} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
				}
				setEmularAcessoAluno(false);
				limparMensagem();
				return getCaminhoRedirecionamentoNavegacao("/visaoAdministrativo/ead/unidadeConteudoForm");
			}
			setUnidadeConteudoVO(new UnidadeConteudoVO());
			setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
			setEmularAcessoAluno(false);
			limparMensagem();
			if (getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				return getCaminhoRedirecionamentoNavegacao("estudoOnlineForm.xhtml");
			}
			if (getEmularAcessoProfessor()) {
				context().getExternalContext().getSessionMap().put("emulandoGestao", true);
				context().getExternalContext().getSessionMap().put("conteudoGestao", getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
				context().getExternalContext().getSessionMap().put("turmaGestao", getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo());
				context().getExternalContext().getSessionMap().put("anoGestao", getMatriculaPeriodoTurmaDisciplinaVO().getAno());
				context().getExternalContext().getSessionMap().put("semestreGestao", getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				context().getExternalContext().getSessionMap().put("disciplinaGestao", getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				return getCaminhoRedirecionamentoNavegacao("/visaoProfessor/gestaoEventoConteudoCons.xhtml");
			}
			return getCaminhoRedirecionamentoNavegacao("/visaoAdministrativo/ead/conteudoForm");
		} catch (Exception e) {

		}
		return "";
	}

	public String novo() {
		setConteudo(new ConteudoVO());
		getConteudo().setResponsavelCadastro(getUsuarioLogadoClone());
		setUnidadeConteudoVO(new UnidadeConteudoVO());
		setAnotacaoDisciplinaVO(new AnotacaoDisciplinaVO());
		setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
		setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			getConteudo().setUsoExclusivoProfessor(Boolean.TRUE);
			getConteudo().setProfessor(getUsuarioLogado().getPessoa());
		}
		limparMensagem();
		verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
	}

	public String editarUnidadeConteudo() {
		try {
			setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
			setEditarUnidade(true);
			if (getUnidadeConteudoVO().getPaginas() > 0) {
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getUnidadeConteudoVO().getPaginas(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				setIrPagina(getUnidadeConteudoVO().getPaginas());
				setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
				setFiltroNomeDisciplina("");
				inicializarRecursoEducacional();
				setApresentarListaRecursoEducacionalConteudo(false);
				setVisualizarGraficoConteudoUnidadePagina(false);
				if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
					visualizarGraficoConteudoUnidadePagina();
				}
			} else {
				editarEAdicionarConteudoUnidadePagina();
			}
			return getCaminhoRedirecionamentoNavegacao("unidadeConteudoForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getCaminhoRedirecionamentoNavegacao("unidadeConteudoForm.xhtml");
	}

	public String editarEAdicionarConteudoUnidadePagina() {
		setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
		setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
		getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(getUnidadeConteudoVO().getCodigo());
		getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getPaginas() + 1);
		setIrPagina(getUnidadeConteudoVO().getPaginas());
		setVisualizarGraficoConteudoUnidadePagina(false);
		setApresentarListaRecursoEducacionalConteudo(false);
		setEditarUnidade(true);
		return getCaminhoRedirecionamentoNavegacao("unidadeConteudoForm.xhtml");
	}

	public String editarPagina() {
		try {
			setConteudoUnidadePagina((ConteudoUnidadePaginaVO) getRequestMap().get("pagina"));
			UnidadeConteudoVO obj = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (obj == null) {
				for (UnidadeConteudoVO unidadeConteudoVO : getConteudo().getUnidadeConteudoVOs()) {
					if (unidadeConteudoVO.getCodigo().intValue() == getConteudoUnidadePagina().getUnidadeConteudo().getCodigo().intValue()) {
						obj = unidadeConteudoVO;
						break;
					}
				}
			}
			setUnidadeConteudoVO(obj);
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
			setVisualizarGraficoConteudoUnidadePagina(false);
			if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
				visualizarGraficoConteudoUnidadePagina();
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			inicializarRecursoEducacional();
			setApresentarListaRecursoEducacionalConteudo(false);
			return getCaminhoRedirecionamentoNavegacao("unidadeConteudoForm.xhtml");
		} catch (ConsistirException e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		} catch (Exception e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		}

	}

	public void visualizarPaginaAnteriorLiveEditor() {
		try {
			Integer pagina = getIrPagina() - 1;
			if (pagina > 0) {
				setIrPagina(pagina);
				getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
				setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getIrPagina(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
				inicializarRecursoEducacional();
				setVisualizarGraficoConteudoUnidadePagina(false);
				if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
					visualizarGraficoConteudoUnidadePagina();
				}
				setApresentarListaRecursoEducacionalConteudo(false);
				limparMensagem();
				setMensagemID("msg_entre_dados", Uteis.ALERTA);
			}
		} catch (ConsistirException e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarPaginaPosteriorLiveEditor() {
		try {
			Integer pagina = getIrPagina() + 1;
			if (pagina <= getUnidadeConteudoVO().getPaginas()) {
				setIrPagina(pagina);
				getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
				setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getIrPagina(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
				inicializarRecursoEducacional();
				setVisualizarGraficoConteudoUnidadePagina(false);
				if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
					visualizarGraficoConteudoUnidadePagina();
				}
				setApresentarListaRecursoEducacionalConteudo(false);
				limparMensagem();
				setMensagemID("msg_entre_dados", Uteis.ALERTA);
			}
		} catch (ConsistirException e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setIrPagina(getConteudoUnidadePagina().getPagina());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void iniciarNovaPagina() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);

			setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
			getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(getUnidadeConteudoVO().getCodigo());
			getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getPaginas() + 1);
			getConteudoUnidadePagina().setPagina(getUnidadeConteudoVO().getPaginas());

			if (!getUnidadeConteudoVO().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND)) {
				getConteudoUnidadePagina().setCorBackground(getUnidadeConteudoVO().getCorBackground());
				getConteudoUnidadePagina().setCaminhoBaseBackground(getUnidadeConteudoVO().getCaminhoBaseBackground());
				getConteudoUnidadePagina().setNomeImagemBackground(getUnidadeConteudoVO().getNomeImagemBackground());
				getConteudoUnidadePagina().setOrigemBackgroundConteudo(getUnidadeConteudoVO().getOrigemBackgroundConteudo());
				getConteudoUnidadePagina().setTamanhoImagemBackgroundConteudo(getUnidadeConteudoVO().getTamanhoImagemBackgroundConteudo());
			}

			setIrPagina(getUnidadeConteudoVO().getPaginas());
			setVisualizarGraficoConteudoUnidadePagina(false);
			setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String voltarConteudo() {
		try {
			setUnidadeConteudoVO(getFacadeFactory().getUnidadeConteudoFacade().consultarPorChavePrimaria(getUnidadeConteudoVO().getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			int index = 0;
			for (UnidadeConteudoVO unidadeConteudoVO : getConteudo().getUnidadeConteudoVOs()) {
				if (unidadeConteudoVO.getCodigo().intValue() == getUnidadeConteudoVO().getCodigo().intValue()) {
					getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getConteudoUnidadePaginaVOs().size());
					getConteudo().getUnidadeConteudoVOs().set(index, getUnidadeConteudoVO());
					break;
				}
				index++;
			}
			setUnidadeConteudoVO(new UnidadeConteudoVO());
			setEditarUnidade(false);
			limparMensagem();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		} catch (Exception e) {

			return getCaminhoRedirecionamentoNavegacao("conteudoForm.xhtml");
		}
	}

	/**
	 * Inclui um novo gatilho anterior
	 */
	public void incluirNovoConteudoUnidadePaginaRecursoEducacionalAnterior() {
		try {
			setPermitirAlteracaoValorNotaAvaliacaoOnline(true);
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			getConteudoUnidadePaginaRecursoEducacional().setTipoRecursoEducacional((TipoRecursoEducacionalEnum) context().getExternalContext().getRequestMap().get("recurso"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaIncluirNovoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.ANTES);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	/**
	 * Inclui um novo gatilho porterior
	 */
	public void incluirNovoConteudoUnidadePaginaRecursoEducacionalPosterior() {
		try {
			setPermitirAlteracaoValorNotaAvaliacaoOnline(true);
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			getConteudoUnidadePaginaRecursoEducacional().setTipoRecursoEducacional((TipoRecursoEducacionalEnum) context().getExternalContext().getRequestMap().get("recurso"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaIncluirNovoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.DEPOIS);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	/**
	 * Inclui um novo gatilho apoio ao professor
	 */
	public void incluirNovoConteudoUnidadePaginaRecursoEducacionalApoioProfessor() {
		try {
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			getConteudoUnidadePaginaRecursoEducacional().setTipoRecursoEducacional((TipoRecursoEducacionalEnum) context().getExternalContext().getRequestMap().get("recurso"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaIncluirNovoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR);
			//setRecursoEducacionalAnterior(false);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void carregarDadosComunParaIncluirNovoConteudoUnidadePaginaRecursoEducacional(){
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			getConteudoUnidadePaginaRecursoEducacional().setListaExercicio(getFacadeFactory().getListaExercicioFacade().novo());
			getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setConteudoVO(getConteudo());
			verificarPermissaoRecursoRandomizarQuestaoProfessor();
		}
		if (!getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
			getConteudoUnidadePaginaRecursoEducacional().setTexto("");
		}
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoRecursoAtaPbl()) {
			getConteudoUnidadePaginaRecursoEducacional().setRequerLiberacaoProfessor(true);
		}
		if(getTipoRecursoEducacional().isTipoAvaliacaoOnline()){
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setTipoUso(TipoUsoEnum.REA);
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setTipoGeracaoProvaOnline(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE);
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setConteudoVO(getConteudo());
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setDisciplinaVO(getConteudo().getDisciplina());
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setReposnsavelCriacao(getUsuarioLogadoClone());
			montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
			verificarPermissaoRecursoRandomizarQuestaoProfessor();
		}
		if (!getConteudoUnidadePagina().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND)) {
			getConteudoUnidadePaginaRecursoEducacional().setCorBackground(getConteudoUnidadePagina().getCorBackground());
			getConteudoUnidadePaginaRecursoEducacional().setCaminhoBaseBackground(getConteudoUnidadePagina().getCaminhoBaseBackground());
			getConteudoUnidadePaginaRecursoEducacional().setNomeImagemBackground(getConteudoUnidadePagina().getNomeImagemBackground());
			getConteudoUnidadePaginaRecursoEducacional().setOrigemBackgroundConteudo(getConteudoUnidadePagina().getOrigemBackgroundConteudo());
			getConteudoUnidadePaginaRecursoEducacional().setTamanhoImagemBackgroundConteudo(getConteudoUnidadePagina().getTamanhoImagemBackgroundConteudo());
		}
	}

	public void editarConteudoUnidadePaginaRecursoEducacionalAnterior() {
		try {
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoAnterior"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaEdicaoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.ANTES);
			setPermitirAlteracaoValorNotaAvaliacaoOnline(true);
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_ONLINE)) {
				verificarAvaliacaoOnlineMatriculaExistente(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo());
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void editarConteudoUnidadePaginaRecursoEducacionalPosterior() {
		try {
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoPosterior"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaEdicaoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.DEPOIS);
			//setRecursoEducacionalAnterior(false);
			setPermitirAlteracaoValorNotaAvaliacaoOnline(true);
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_ONLINE)) {
				verificarAvaliacaoOnlineMatriculaExistente(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo());
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void editarConteudoUnidadePaginaRecursoEducacionalApoioProfessor() {
		try {
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoApoioProfessor"));
			setTipoRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional());
			setFiltroNomeDisciplina("");
			inicializarRecursoEducacional();
			carregarDadosComunParaEdicaoConteudoUnidadePaginaRecursoEducacional();
			setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR);
			//setRecursoEducacionalAnterior(false);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void carregarDadosComunParaEdicaoConteudoUnidadePaginaRecursoEducacional() throws Exception{
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePaginaRecursoEducacional());
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional();
		}
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
			getConteudoUnidadePaginaRecursoEducacional().setForum(getFacadeFactory().getForumFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getForum().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS));
		}
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && !getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().isNovoObj()) {
			getConteudoUnidadePaginaRecursoEducacional().setListaExercicio(getFacadeFactory().getListaExercicioFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getCodigo()));
			getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setConteudoAlterado(true);
			for(QuestaoListaExercicioVO questaoListaExercicioVO:getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs()) {
				if(questaoListaExercicioVO.getQuestao().getOpcaoRespostaQuestaoVOs().isEmpty()) {					
					questaoListaExercicioVO.getQuestao().setOpcaoRespostaQuestaoVOs(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorQuestao(questaoListaExercicioVO.getQuestao().getCodigo()));					
				}
			}
			inicializarTotalNivelComplexidadeExercicio();
		}
		if(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoAvaliacaoOnline()){
			getConteudoUnidadePaginaRecursoEducacional().setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline();
			for(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO:getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs()) {
				if(avaliacaoOnlineQuestaoVO.getQuestaoVO().getOpcaoRespostaQuestaoVOs().isEmpty()) {					
					avaliacaoOnlineQuestaoVO.getQuestaoVO().setOpcaoRespostaQuestaoVOs(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorQuestao(avaliacaoOnlineQuestaoVO.getQuestaoVO().getCodigo()));					
				}
			}
		}
	}

	public void adicionarConteudoUnidadePaginaRecursoEducacional() {
		try {
			setRecursoAdicionadoComSucesso(false);
			getConteudoUnidadePaginaRecursoEducacional().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setCodigo(getConteudo().getCodigo());
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setDisciplina(getConteudo().getDisciplina());
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getConteudoVO().setCodigo(getConteudo().getCodigo());
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setConteudoAlterado(true);
			}
			getFacadeFactory().getConteudoUnidadePaginaFacade().adicionarConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), getConteudoUnidadePaginaRecursoEducacional(), getMomentoApresentacaoRecursoEducacionalEnum(), getConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), false);
			setRecursoAdicionadoComSucesso(true);
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			setCaminhoArquivoConteudoUnidadePaginaRecursoEducacional("");
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			inicializarRecursoEducacional();
		} catch (ConsistirException e) {
			setRecursoAdicionadoComSucesso(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarRecursoEducacional() throws Exception {

		setApresentarListaRecursoEducacionalGatilho(false);
		
		if (getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
			if (getForumVOs().isEmpty()) {
				setForumVOs(getFacadeFactory().getForumFacade().consultarForumPorConteudoDisciplina(getConteudo().getDisciplina(), "", OpcaoOrdenacaoForumEnum.TEMA, getUsuarioLogado().getCodigo(), 0, 0, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS));
			}
			return;
		}
		if (getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			if (getListaExercicioVOs().isEmpty()) {
				setListaExercicioVOs(getFacadeFactory().getListaExercicioFacade().consultar(getConteudo().getDisciplina().getCodigo(), null, "", SituacaoListaExercicioEnum.ATIVA, null, PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO, false, "ListaExercicio", getUsuarioLogado(), 0, 0));
				inicializarTotalNivelComplexidadeExercicio();
			}

			return;
		}		
		if (getRecursoEducacionalVOs() == null || getRecursoEducacionalVOs().isEmpty() || !getRecursoEducacionalVOs().get(0).getTipoRecursoEducacional().equals(getTipoRecursoEducacional())) {
			setRecursoEducacionalVOs(getFacadeFactory().getRecursoEducacionalFacade().consultarRecursoEducacional("", getTipoRecursoEducacional(), SituacaoRecursoEducacionalEnum.ATIVO, 0, "", NivelMontarDados.TODOS, false, getUsuarioLogado(), 0, 0));
		}
		int disciplina = 0;
		setTreeNodeCustomizado(new TreeNodeCustomizado());
		TreeNodeCustomizado disciplinaTree = null;
		TreeNodeCustomizado  recursoEducacionalTree = null;
		int x = 0;
		for (RecursoEducacionalVO recursoEducacionalVO : getRecursoEducacionalVOs()) {
			if (getFiltroNomeDisciplina().trim().isEmpty() || Uteis.removerAcentuacao(recursoEducacionalVO.getDisciplina().getNome().trim().toUpperCase()).contains(Uteis.removerAcentuacao(getFiltroNomeDisciplina().trim().toUpperCase()))) {
				if (disciplina == 0 || disciplina != recursoEducacionalVO.getDisciplina().getCodigo().intValue()) {
					x = 0;
					disciplina = recursoEducacionalVO.getDisciplina().getCodigo();
					disciplinaTree = new TreeNodeCustomizado();
					RecursoEducacionalVO recursoEducacionalVO2 = (RecursoEducacionalVO)recursoEducacionalVO.clone();
					recursoEducacionalVO2.setTreeFilho(false);
					disciplinaTree.setData(recursoEducacionalVO2);
					getTreeNodeCustomizado().getFilhos().add(disciplinaTree);
					getTreeNodeCustomizado().addChild(disciplina, disciplinaTree);
					getTreeNodeCustomizado().getChildrenKeysIterator().hasNext();
				}
				recursoEducacionalTree = new TreeNodeCustomizado();
				recursoEducacionalTree.setData(recursoEducacionalVO);
				disciplinaTree.insertChild(x++, recursoEducacionalVO.getCodigo(), recursoEducacionalTree);
				disciplinaTree.getFilhos().add(recursoEducacionalTree);
				//disciplinaTree.addChild(recursoEducacionalVO.getCodigo(), recursoEducacionalTree);
				disciplinaTree.getChildrenKeysIterator();
			}
		}
	}

//	public void selecionarRecursoEducacionalGatilho(NodeSelectedEvent event) {
//		HtmlTree tree = (HtmlTree) event.getComponent();
//		if (tree.getRowData() instanceof RecursoEducacionalVO) {
//			getConteudoUnidadePaginaRecursoEducacional().setRecursoEducacional((RecursoEducacionalVO) tree.getRowData());
//		}
//	}
	
	public void selecionarRecursoEducacionalGatilho(TreeSelectionChangeEvent event){
		UITree tree = (UITree) event.getComponent();
		if(tree.getRowData() instanceof RecursoEducacionalVO){
			getConteudoUnidadePaginaRecursoEducacional().setRecursoEducacional((RecursoEducacionalVO) tree.getRowData());
		}
	}

//	public void selecionarRecursoEducacionalConteudoUnidadePagina(NodeSelectedEvent event) {
//		
////		HtmlTree tree = (HtmlTree) event.getComponent();
//		UITree tree = (UITree) event.getComponent();
//		
//		if (tree.getRowData() instanceof RecursoEducacionalVO) {
//			getConteudoUnidadePagina().setRecursoEducacional((RecursoEducacionalVO) tree.getRowData());
//		}
//	}
//	
	public void selecionarRecursoEducacionalConteudoUnidadePagina(TreeSelectionChangeEvent event) {
		UITree tree = (UITree) event.getComponent();
		
		if (tree.getRowData() instanceof RecursoEducacionalVO) {
			getConteudoUnidadePagina().setRecursoEducacional((RecursoEducacionalVO) tree.getRowData());
		}
	}

	public void dropIcone(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof IconeVO) {
				IconeVO icone = (IconeVO) dropEvent.getDragValue();
				if (getUploadIconeVoltar()) {
					getConteudoUnidadePagina().getIconeVoltar().setCodigo(icone.getCodigo());
					getConteudoUnidadePagina().getIconeVoltar().setNovoObj(false);
					getConteudoUnidadePagina().setCaminhoIconeVoltar(icone.getCaminhoBase());
					getConteudoUnidadePagina().setNomeIconeVoltar(icone.getNomeReal());
				} else if (!getUploadIconeVoltar()) {
					getConteudoUnidadePagina().getIconeAvancar().setCodigo(icone.getCodigo());
					getConteudoUnidadePagina().getIconeAvancar().setNovoObj(false);
					getConteudoUnidadePagina().setCaminhoIconeAvancar(icone.getCaminhoBase());
					getConteudoUnidadePagina().setNomeIconeAvancar(icone.getNomeReal());
				}
			}
		} catch (Exception e) {

		}
	}

	public void selecionarForumRecursoEducacional() {
		ForumVO forum = (ForumVO) context().getExternalContext().getRequestMap().get("forumItens");
		getConteudoUnidadePaginaRecursoEducacional().setForum(forum);
		getConteudoUnidadePaginaRecursoEducacional().getForum().setNovoObj(false);
		setApresentarListaRecursoEducacionalGatilho(false);
	}

	public void selecionarForumConteudo() {
		ForumVO forum = (ForumVO) context().getExternalContext().getRequestMap().get("forumItens");
		getConteudoUnidadePagina().setForum(forum);
		getConteudoUnidadePagina().getForum().setNovoObj(false);
		setApresentarListaRecursoEducacionalConteudo(false);
	}

	public void selecionarListaExercicio() {
		try {
			ListaExercicioVO listaExercicioVO = (ListaExercicioVO) context().getExternalContext().getRequestMap().get("listaExercicioItens");
			listaExercicioVO = getFacadeFactory().getListaExercicioFacade().consultarPorChavePrimaria(listaExercicioVO.getCodigo());
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getConteudoUnidadePaginaRecursoEducacional().setListaExercicio(listaExercicioVO);
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setNovoObj(false);
				setApresentarListaRecursoEducacionalGatilho(false);
			} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getConteudoUnidadePagina().setListaExercicio(listaExercicioVO);
				getConteudoUnidadePagina().getListaExercicio().setNovoObj(false);
				setApresentarListaRecursoEducacionalConteudo(false);
			}

			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void dropRecursoEducacionalGatilho(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof ForumVO) {
				getConteudoUnidadePaginaRecursoEducacional().setRecursoEducacional(new RecursoEducacionalVO());
				getConteudoUnidadePaginaRecursoEducacional().setForum((ForumVO) dropEvent.getDragValue());
				getConteudoUnidadePaginaRecursoEducacional().getForum().setNovoObj(false);
			} else if (dropEvent.getDragValue() instanceof RecursoEducacionalVO) {
				RecursoEducacionalVO re = (RecursoEducacionalVO) dropEvent.getDragValue();
				if (!re.getTipoRecursoEducacional().equals(getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional()) && getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
					StringBuilder incorporar = new StringBuilder("</br>");
					if (re.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM)) {
						incorporar.append("<img style=\"vertical-align:middle;max-height:200px; text-align:center;\" src=\"" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo());
						incorporar.append("/" + re.getCaminhoBaseRepositorio().replaceAll("\\\\", "/"));
						incorporar.append("/" + re.getNomeFisicoArquivo() + "?UID=" + Calendar.getInstance().getTime() + "\"/>");
						getConteudoUnidadePaginaRecursoEducacional().setTexto(getConteudoUnidadePaginaRecursoEducacional().getTexto().replaceAll("</body>", "</br>" + incorporar.toString() + "</body>"));
					}

				} else {
					getConteudoUnidadePaginaRecursoEducacional().setRecursoEducacional(new RecursoEducacionalVO());
					getConteudoUnidadePaginaRecursoEducacional().setRecursoEducacional(re);
					getConteudoUnidadePaginaRecursoEducacional().setAltura(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getAltura());
					getConteudoUnidadePaginaRecursoEducacional().setLargura(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getLargura());
					getConteudoUnidadePaginaRecursoEducacional().setApresentarLegenda(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getApresentarLegenda());
					getConteudoUnidadePaginaRecursoEducacional().setCaminhoBaseRepositorio(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getCaminhoBaseRepositorio());
					getConteudoUnidadePaginaRecursoEducacional().setNomeFisicoArquivo(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getNomeFisicoArquivo());
					getConteudoUnidadePaginaRecursoEducacional().setNomeRealArquivo(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getNomeRealArquivo());
					if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
						getConteudoUnidadePaginaRecursoEducacional().setTexto(getConteudoUnidadePaginaRecursoEducacional().getTexto().replaceAll("</body>", getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTexto()));
					} else {
						getConteudoUnidadePaginaRecursoEducacional().setTexto(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTexto());
					}
					getConteudoUnidadePaginaRecursoEducacional().setTituloEixoX(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTituloEixoX());
					getConteudoUnidadePaginaRecursoEducacional().setTituloEixoY(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTituloEixoY());
					getConteudoUnidadePaginaRecursoEducacional().setTituloGrafico(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTituloGrafico());
					getConteudoUnidadePaginaRecursoEducacional().setValorGrafico(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getValorGrafico());
					getConteudoUnidadePaginaRecursoEducacional().setCategoriaGrafico(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getCategoriaGrafico());
					getConteudoUnidadePaginaRecursoEducacional().setTipoGrafico(getConteudoUnidadePaginaRecursoEducacional().getRecursoEducacional().getTipoGrafico());
					if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
						getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePaginaRecursoEducacional());
					}
				}
				setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			}
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
		}
	}

	public void alterarOrdemApresentacaoUnidadeConteudo(DropEvent dropEvent) {
		try {

			if (dropEvent.getDropValue() instanceof UnidadeConteudoVO && dropEvent.getDragValue() instanceof UnidadeConteudoVO) {
				UnidadeConteudoVO unidadeConteudo1 = (UnidadeConteudoVO) dropEvent.getDragValue();
				UnidadeConteudoVO unidadeConteudo2 = (UnidadeConteudoVO) dropEvent.getDropValue();
				getFacadeFactory().getConteudoFacade().alterarOrdemUnidadeConteudo(getConteudo(), unidadeConteudo1, unidadeConteudo2, getUsuarioLogado(), false);
			}
			if (dropEvent.getDropValue() instanceof UnidadeConteudoVO && dropEvent.getDragValue() instanceof ConteudoUnidadePaginaVO) {

				ConteudoUnidadePaginaVO conteudoUnidadePaginaVO = (ConteudoUnidadePaginaVO) dropEvent.getDragValue();
				UnidadeConteudoVO unidadeConteudo1 = (UnidadeConteudoVO) dropEvent.getDropValue();
				if (conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo().intValue() != unidadeConteudo1.getCodigo().intValue()) {
					getFacadeFactory().getConteudoFacade().alterarPaginaUnidadeConteudoParaOutraUnidadeConteudo(getConteudo(), unidadeConteudo1, conteudoUnidadePaginaVO, getUsuarioLogado());
				}
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerUnidadeConteudo() {
		try {
			UnidadeConteudoVO unidadeConteudo1 = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (unidadeConteudo1.getOrdem().intValue() == getConteudo().getUnidadeConteudoVOs().size()) {
				return;
			}
			UnidadeConteudoVO unidadeConteudo2 = getConteudo().getUnidadeConteudoVOs().get(unidadeConteudo1.getOrdem());
			getFacadeFactory().getConteudoFacade().alterarOrdemUnidadeConteudo(getConteudo(), unidadeConteudo1, unidadeConteudo2, getUsuarioLogado(), false);
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirUnidadeConteudo() {
		try {
			UnidadeConteudoVO unidadeConteudo1 = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (unidadeConteudo1.getOrdem().intValue() == 1) {
				return;
			}
			UnidadeConteudoVO unidadeConteudo2 = getConteudo().getUnidadeConteudoVOs().get(unidadeConteudo1.getOrdem() - 2);
			getFacadeFactory().getConteudoFacade().alterarOrdemUnidadeConteudo(getConteudo(), unidadeConteudo1, unidadeConteudo2, getUsuarioLogado(), false);
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerConteudoUnidadePagina() {
		try {
			ConteudoUnidadePaginaVO conteudoUnidadePaginaVO = (ConteudoUnidadePaginaVO) getRequestMap().get("pagina");
			UnidadeConteudoVO obj = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (obj == null) {
				for (UnidadeConteudoVO unidadeConteudoVO : getConteudo().getUnidadeConteudoVOs()) {
					if (unidadeConteudoVO.getCodigo().intValue() == conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo().intValue()) {
						obj = unidadeConteudoVO;
						break;
					}
				}
			}

			if (obj != null) {
				if (conteudoUnidadePaginaVO.getPagina().intValue() == obj.getConteudoUnidadePaginaVOs().size()) {
					return;
				}
				ConteudoUnidadePaginaVO conteudoUnidadePaginaVO2 = obj.getConteudoUnidadePaginaVOs().get(conteudoUnidadePaginaVO.getPagina());
				getFacadeFactory().getUnidadeConteudoFacade().alterarOrdemConteudoUnidadePagina(obj, obj, conteudoUnidadePaginaVO, conteudoUnidadePaginaVO2, getUsuarioLogado());
				limparMensagem();
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirConteudoUnidadePagina() {
		try {
			ConteudoUnidadePaginaVO conteudoUnidadePaginaVO = (ConteudoUnidadePaginaVO) getRequestMap().get("pagina");
			UnidadeConteudoVO obj = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (obj == null) {
				for (UnidadeConteudoVO unidadeConteudoVO : getConteudo().getUnidadeConteudoVOs()) {
					if (unidadeConteudoVO.getCodigo().intValue() == conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo().intValue()) {
						obj = unidadeConteudoVO;
						break;
					}
				}
			}
			if (conteudoUnidadePaginaVO.getPagina().intValue() <= 1) {
				limparMensagem();
				return;
			}
			ConteudoUnidadePaginaVO conteudoUnidadePaginaVO2 = obj.getConteudoUnidadePaginaVOs().get(conteudoUnidadePaginaVO.getPagina() - 2);
			getFacadeFactory().getUnidadeConteudoFacade().alterarOrdemConteudoUnidadePagina(obj, obj, conteudoUnidadePaginaVO, conteudoUnidadePaginaVO2, getUsuarioLogado());
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemApresentacaoConteudoUnidadePaginaRecursoEducacional(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof ConteudoUnidadePaginaRecursoEducacionalVO && (dropEvent.getDropValue() instanceof ConteudoUnidadePaginaRecursoEducacionalVO || dropEvent.getDropValue() == null)) {
				ConteudoUnidadePaginaRecursoEducacionalVO unidadeConteudoUnidadePaginaRecursoEducacional1 = (ConteudoUnidadePaginaRecursoEducacionalVO) dropEvent.getDragValue();
				ConteudoUnidadePaginaRecursoEducacionalVO unidadeConteudoUnidadePaginaRecursoEducacional2 = null;
				if (dropEvent.getDropValue() != null) {
					unidadeConteudoUnidadePaginaRecursoEducacional2 = (ConteudoUnidadePaginaRecursoEducacionalVO) dropEvent.getDropValue();
				}
				getFacadeFactory().getConteudoUnidadePaginaFacade().alterarOrdemConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), unidadeConteudoUnidadePaginaRecursoEducacional1, unidadeConteudoUnidadePaginaRecursoEducacional2, getUsuarioLogado());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void alterarOrdemApresentacaoConteudoUnidadePaginaRecursoEducacionalProfessor(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof ConteudoUnidadePaginaRecursoEducacionalVO && (dropEvent.getDropValue() instanceof ConteudoUnidadePaginaRecursoEducacionalVO || dropEvent.getDropValue() == null)) {
				ConteudoUnidadePaginaRecursoEducacionalVO unidadeConteudoUnidadePaginaRecursoEducacional1 = (ConteudoUnidadePaginaRecursoEducacionalVO) dropEvent.getDragValue();
				ConteudoUnidadePaginaRecursoEducacionalVO unidadeConteudoUnidadePaginaRecursoEducacional2 = null;
				if (dropEvent.getDropValue() != null) {
					unidadeConteudoUnidadePaginaRecursoEducacional2 = (ConteudoUnidadePaginaRecursoEducacionalVO) dropEvent.getDropValue();
				}
				getFacadeFactory().getConteudoUnidadePaginaFacade().alterarOrdemConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), unidadeConteudoUnidadePaginaRecursoEducacional1, unidadeConteudoUnidadePaginaRecursoEducacional2, getUsuarioLogado());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemApresentacaoConteudoUnidadePagina(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof ConteudoUnidadePaginaVO && (dropEvent.getDropValue() instanceof ConteudoUnidadePaginaVO || dropEvent.getDropValue() instanceof UnidadeConteudoVO)) {

				ConteudoUnidadePaginaVO unidadeConteudoUnidadePagina1 = (ConteudoUnidadePaginaVO) dropEvent.getDragValue();
				ConteudoUnidadePaginaVO unidadeConteudoUnidadePagina2 = dropEvent.getDropValue() instanceof ConteudoUnidadePaginaVO ? (ConteudoUnidadePaginaVO) dropEvent.getDropValue() : null;

				UnidadeConteudoVO unidadeConteudoVO1 = null;
				for (UnidadeConteudoVO obj1 : getConteudo().getUnidadeConteudoVOs()) {
					if (obj1.getCodigo().intValue() == unidadeConteudoUnidadePagina1.getUnidadeConteudo().getCodigo().intValue()) {
						unidadeConteudoVO1 = obj1;
						break;
					}
				}
				UnidadeConteudoVO unidadeConteudoVO2 = dropEvent.getDropValue() instanceof UnidadeConteudoVO ? (UnidadeConteudoVO) dropEvent.getDropValue() : null;;
				if(unidadeConteudoUnidadePagina2 != null) {
					for (UnidadeConteudoVO obj2 : getConteudo().getUnidadeConteudoVOs()) {
						if (obj2.getCodigo().intValue() == unidadeConteudoUnidadePagina2.getUnidadeConteudo().getCodigo().intValue()) {
							unidadeConteudoVO2 = obj2;
							break;
						}
					}
				}

				getFacadeFactory().getUnidadeConteudoFacade().alterarOrdemConteudoUnidadePagina(unidadeConteudoVO1, unidadeConteudoVO2, unidadeConteudoUnidadePagina1, unidadeConteudoUnidadePagina2, getUsuarioLogado());
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void dropRecursoEducacionalConteudoUnidadePagina(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof RecursoEducacionalVO) {
				RecursoEducacionalVO re = (RecursoEducacionalVO) dropEvent.getDragValue();

				if (!re.getTipoRecursoEducacional().equals(getConteudoUnidadePagina().getTipoRecursoEducacional()) && getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
					StringBuilder incorporar = new StringBuilder("</br>");

					if (re.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM)) {

						incorporar.append("<img style=\"vertical-align:middle;max-height:200px; text-align:center;\" src=\"" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo());
						incorporar.append("/" + re.getCaminhoBaseRepositorio().replaceAll("\\\\", "/"));
						incorporar.append("/" + re.getNomeFisicoArquivo() + "?UID=" + Calendar.getInstance().getTime() + "\"/>");
						getConteudoUnidadePagina().setTexto(getConteudoUnidadePagina().getTexto().replaceAll("</body>", "</br>" + incorporar.toString() + "</body>"));
					}

				} else {
					getConteudoUnidadePagina().setRecursoEducacional(new RecursoEducacionalVO());
					getConteudoUnidadePagina().setRecursoEducacional((RecursoEducacionalVO) dropEvent.getDragValue());

					getConteudoUnidadePagina().setAltura(getConteudoUnidadePagina().getRecursoEducacional().getAltura());
					getConteudoUnidadePagina().setLargura(getConteudoUnidadePagina().getRecursoEducacional().getLargura());
					getConteudoUnidadePagina().setApresentarLegenda(getConteudoUnidadePagina().getRecursoEducacional().getApresentarLegenda());
					getConteudoUnidadePagina().setCaminhoBaseRepositorio(getConteudoUnidadePagina().getRecursoEducacional().getCaminhoBaseRepositorio());
					getConteudoUnidadePagina().setNomeFisicoArquivo(getConteudoUnidadePagina().getRecursoEducacional().getNomeFisicoArquivo());
					getConteudoUnidadePagina().setNomeRealArquivo(getConteudoUnidadePagina().getRecursoEducacional().getNomeRealArquivo());

					if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
						getConteudoUnidadePagina().setTexto(getConteudoUnidadePagina().getTexto().replaceAll("</body>", getConteudoUnidadePagina().getRecursoEducacional().getTexto()));
					} else {
						getConteudoUnidadePagina().setTexto(getConteudoUnidadePagina().getRecursoEducacional().getTexto());
					}

					getConteudoUnidadePagina().setTexto(getConteudoUnidadePagina().getRecursoEducacional().getTexto());
					getConteudoUnidadePagina().setTituloEixoX(getConteudoUnidadePagina().getRecursoEducacional().getTituloEixoX());
					getConteudoUnidadePagina().setTituloEixoY(getConteudoUnidadePagina().getRecursoEducacional().getTituloEixoY());
					getConteudoUnidadePagina().setTituloGrafico(getConteudoUnidadePagina().getRecursoEducacional().getTituloGrafico());
					getConteudoUnidadePagina().setValorGrafico(getConteudoUnidadePagina().getRecursoEducacional().getValorGrafico());
					getConteudoUnidadePagina().setCategoriaGrafico(getConteudoUnidadePagina().getRecursoEducacional().getCategoriaGrafico());
					getConteudoUnidadePagina().setTipoGrafico(getConteudoUnidadePagina().getRecursoEducacional().getTipoGrafico());
					if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
						getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoConteudoUnidadePaginaGraficoVO(getConteudoUnidadePagina());
					}
				}
				setVisualizarGraficoConteudoUnidadePagina(false);
			}
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
		}
	}

	public String getFecharModalAdicionarConteudoUnidadePaginaRecursoEducacional() {
		if (getRecursoAdicionadoComSucesso()) {
			return "RichFaces.$('panelAddRecursoEducacional').hide()";
		}
		return "";
	}

	public void alterarOrdemApresentacaoQuestaoListaExercicio(DropEvent dropEvent) {
		try {
			if (dropEvent.getDragValue() instanceof QuestaoListaExercicioVO && dropEvent.getDropValue() instanceof QuestaoListaExercicioVO) {

				if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), (QuestaoListaExercicioVO) dropEvent.getDragValue(), (QuestaoListaExercicioVO) dropEvent.getDropValue());
				} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), (QuestaoListaExercicioVO) dropEvent.getDragValue(), (QuestaoListaExercicioVO) dropEvent.getDropValue());
				}
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirQuestaoListaExercicio() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (questaoListaExercicioVO.getOrdemApresentacao() > 1) {
				if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					QuestaoListaExercicioVO questaoListaExercicio2 = getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao() - 2);
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), questaoListaExercicioVO, questaoListaExercicio2);
				} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					QuestaoListaExercicioVO questaoListaExercicio2 = getConteudoUnidadePagina().getListaExercicio().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao() - 2);
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), questaoListaExercicioVO, questaoListaExercicio2);
				}
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerQuestaoListaExercicio() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs().size() >= questaoListaExercicioVO.getOrdemApresentacao()) {
				if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					QuestaoListaExercicioVO questaoListaExercicio2 = getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao());
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), questaoListaExercicioVO, questaoListaExercicio2);
				} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					QuestaoListaExercicioVO questaoListaExercicio2 = getConteudoUnidadePagina().getListaExercicio().getQuestaoListaExercicioVOs().get(questaoListaExercicioVO.getOrdemApresentacao());
					getFacadeFactory().getListaExercicioFacade().alterarOrdemApresentacaoQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), questaoListaExercicioVO, questaoListaExercicio2);
				}
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarQuestao() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItens");
			if(questaoVO.getOpcaoRespostaQuestaoVOs().isEmpty()) {
				questaoVO.setOpcaoRespostaQuestaoVOs(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorQuestao(questaoVO.getCodigo()));
			}
			
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoRecursoExercicio()) {
				questaoListaExercicioVO.setQuestao(questaoVO);
				getFacadeFactory().getListaExercicioFacade().adicionarQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), questaoListaExercicioVO);
			} else if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
				AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = new AvaliacaoOnlineQuestaoVO();
				avaliacaoOnlineQuestaoVO.setQuestaoVO(questaoVO);
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().adicionarQuestaoAvaliacaoOnline(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO);
				questaoVO.setSelecionado(true);
				somarNotaAvaliacao();
			} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				questaoListaExercicioVO.setQuestao(questaoVO);
				getFacadeFactory().getListaExercicioFacade().adicionarQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), questaoListaExercicioVO);
			}
			questaoVO.setSelecionado(true);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestao() {
		try {
			QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
			QuestaoVO questaoVO = (QuestaoVO) context().getExternalContext().getRequestMap().get("questaoItens");
			questaoListaExercicioVO.setQuestao(questaoVO);
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), questaoListaExercicioVO);
			} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), questaoListaExercicioVO);
			}
			questaoVO.setSelecionado(false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerQuestaoListaExercicio() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens"));
			} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				getFacadeFactory().getListaExercicioFacade().removerQuestaoListaExercicio(getConteudoUnidadePagina().getListaExercicio(), (QuestaoListaExercicioVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens"));
			}
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarConsultaQuestaoExercicios() {
		setControleConsultaQuestao(null);
		limparMensagem();
		setUsoExercicio(true);
		setUsoOnline(false);
		setUsoPresencial(false);
	}
	
	public void inicializarConsultaQuestaoAvaliacaoOnline() {
		setControleConsultaQuestao(null);
		limparMensagem();
		setUsoExercicio(false);
		setUsoOnline(true);
		setUsoPresencial(false);
	}

	public void consultarQuestao() {
		try {
			getControleConsultaQuestao().setLimitePorPagina(5);
			List objs = new ArrayList<>();
			if (getCampoConsulta().equals("codigo")) {
				QuestaoVO questaoVO = getFacadeFactory().getQuestaoFacade().consultarPorChavePrimaria(Integer.parseInt(getValorConsulta()));
				objs.add(questaoVO);
				getControleConsultaQuestao().setListaConsulta(objs);
				getControleConsultaQuestao().setTotalRegistrosEncontrados(1);
			} else {
				getControleConsultaQuestao().setListaConsulta(getFacadeFactory().getQuestaoFacade().consultar(getQuestaoVO().getEnunciado(), new TemaAssuntoVO(), getConteudo().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, isUsoOnline(), isUsoPresencial(), isUsoExercicio(), false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), false, "", getUsuarioLogado(), getControleConsultaQuestao().getLimitePorPagina(), getControleConsultaQuestao().getOffset(), getConteudo().getCodigo(), getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum(), null, null, null, false));
				getControleConsultaQuestao().setTotalRegistrosEncontrados(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro(getQuestaoVO().getEnunciado(), getUnidadeConteudoVO().getTemaAssuntoVO(), getConteudo().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, isUsoOnline(), isUsoPresencial(), isUsoExercicio(), false, getQuestaoVO().getTipoQuestaoEnum(), getQuestaoVO().getNivelComplexidadeQuestao(), getConteudo().getCodigo(), getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum(), null, null, null, null, false));
			}
			marcarQuestaoJaSelecionada();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void marcarQuestaoJaSelecionada() {
		q: for (QuestaoVO questaoVO : (List<QuestaoVO>) getControleConsultaQuestao().getListaConsulta()) {
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				for (QuestaoListaExercicioVO questaoListaExercicioVO : getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs()) {
					if (questaoVO.getCodigo().intValue() == questaoListaExercicioVO.getQuestao().getCodigo().intValue()) {
						questaoVO.setSelecionado(true);
						continue q;
					}
				}
			} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
				for (QuestaoListaExercicioVO questaoListaExercicioVO : getConteudoUnidadePagina().getListaExercicio().getQuestaoListaExercicioVOs()) {
					if (questaoVO.getCodigo().intValue() == questaoListaExercicioVO.getQuestao().getCodigo().intValue()) {
						questaoVO.setSelecionado(true);
						continue q;
					}
				}
			}
		}
	}

	protected List<SelectItem> listaSelectItemTipoGeracaoListaExercicio;

	public List<SelectItem> getListaSelectItemTipoGeracaoListaExercicio() {
		if (listaSelectItemTipoGeracaoListaExercicio == null) {
			listaSelectItemTipoGeracaoListaExercicio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoGeracaoListaExercicio.add(new SelectItem(TipoGeracaoListaExercicioEnum.RANDOMICO, TipoGeracaoListaExercicioEnum.RANDOMICO.getValorApresentar()));
			listaSelectItemTipoGeracaoListaExercicio.add(new SelectItem(TipoGeracaoListaExercicioEnum.FIXO, TipoGeracaoListaExercicioEnum.FIXO.getValorApresentar()));
		}
		return listaSelectItemTipoGeracaoListaExercicio;
	}

	public void paginarQuestao(DataScrollEvent DataScrollEvent) {
		getControleConsultaQuestao().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaQuestao().setPage(DataScrollEvent.getPage());
		consultarQuestao();
	}

	public void inicializarTotalNivelComplexidadeExercicio() {
		try {
			setQtdeExercicioDificil(0);
			setQtdeExercicioFacil(0);
			setQtdeExercicioMedio(0);
			if (getConteudo().getDisciplina().getCodigo() > 0) {
				setQtdeExercicioDificil(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("",getUnidadeConteudoVO().getTemaAssuntoVO(), getConteudo().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.DIFICIL, getConteudo().getCodigo(), getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum(), null, null, null, null, false));
				setQtdeExercicioMedio(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", getUnidadeConteudoVO().getTemaAssuntoVO(), getConteudo().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.MEDIO, getConteudo().getCodigo(), getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum(), null, null, null, null, false));
				setQtdeExercicioFacil(getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", getUnidadeConteudoVO().getTemaAssuntoVO(), getConteudo().getDisciplina().getCodigo(), SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.FACIL, getConteudo().getCodigo(), getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum(), null, null, null, null, false));
			}
		} catch (Exception e) {

		}
	}

	public Integer getQtdeExercicioFacil() {
		if (qtdeExercicioFacil == null) {
			qtdeExercicioFacil = 0;
		}
		return qtdeExercicioFacil;
	}

	public void setQtdeExercicioFacil(Integer qtdeExercicioFacil) {
		this.qtdeExercicioFacil = qtdeExercicioFacil;
	}

	public Integer getQtdeExercicioMedio() {
		if (qtdeExercicioMedio == null) {
			qtdeExercicioMedio = 0;
		}
		return qtdeExercicioMedio;
	}

	public void setQtdeExercicioMedio(Integer qtdeExercicioMedio) {
		this.qtdeExercicioMedio = qtdeExercicioMedio;
	}

	public Integer getQtdeExercicioDificil() {
		if (qtdeExercicioDificil == null) {
			qtdeExercicioDificil = 0;
		}
		return qtdeExercicioDificil;
	}

	public void setQtdeExercicioDificil(Integer qtdeExercicioDificil) {
		this.qtdeExercicioDificil = qtdeExercicioDificil;
	}

	public QuestaoVO getQuestaoVO() {
		if (questaoVO == null) {
			questaoVO = new QuestaoVO();
		}
		return questaoVO;
	}

	public void setQuestaoVO(QuestaoVO questaoVO) {
		this.questaoVO = questaoVO;
	}

	public DataModelo getControleConsultaQuestao() {
		if (controleConsultaQuestao == null) {
			controleConsultaQuestao = new DataModelo();
			controleConsultaQuestao.setLimitePorPagina(10);
		}
		return controleConsultaQuestao;
	}

	public void setControleConsultaQuestao(DataModelo controleConsultaQuestao) {
		this.controleConsultaQuestao = controleConsultaQuestao;
	}

	private List<SelectItem> listaSelectItemTipoQuestaoConsulta;
	private List<SelectItem> listaSelectItemComplexidadeQuestaoConsulta;

	public List<SelectItem> getListaSelectItemTipoQuestaoConsulta() {
		if (listaSelectItemTipoQuestaoConsulta == null) {
			listaSelectItemTipoQuestaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.UNICA_ESCOLHA, TipoQuestaoEnum.UNICA_ESCOLHA.getValorApresentar()));
			listaSelectItemTipoQuestaoConsulta.add(new SelectItem(TipoQuestaoEnum.MULTIPLA_ESCOLHA, TipoQuestaoEnum.MULTIPLA_ESCOLHA.getValorApresentar()));
		}
		return listaSelectItemTipoQuestaoConsulta;
	}

	public List<SelectItem> getListaSelectItemComplexidadeQuestaoConsulta() {
		if (listaSelectItemComplexidadeQuestaoConsulta == null) {
			listaSelectItemComplexidadeQuestaoConsulta = new ArrayList<SelectItem>(0);			
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.FACIL, NivelComplexidadeQuestaoEnum.FACIL.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.MEDIO, NivelComplexidadeQuestaoEnum.MEDIO.getValorApresentar()));
			listaSelectItemComplexidadeQuestaoConsulta.add(new SelectItem(NivelComplexidadeQuestaoEnum.DIFICIL, NivelComplexidadeQuestaoEnum.DIFICIL.getValorApresentar()));
		}
		return listaSelectItemComplexidadeQuestaoConsulta;
	}

	private LoginControle loginControle = (LoginControle) getControlador("LoginControle");

	public Boolean getPermiteAlterarListaExercicio() {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			return getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().isNovoObj() || (getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO) && getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessor());
		} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			return getConteudoUnidadePagina().getListaExercicio().isNovoObj() || (getConteudoUnidadePagina().getListaExercicio().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO) && getConteudoUnidadePagina().getListaExercicio().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessor());
		}
		return false;
	}

	public Boolean getPermiteAlterarListaExercicioProfessor() {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			return getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().isNovoObj() || (getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO) && getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessorProfessor());
		} else if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			return getConteudoUnidadePagina().getListaExercicio().isNovoObj() || (getConteudoUnidadePagina().getListaExercicio().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO) && getConteudoUnidadePagina().getListaExercicio().getResponsavelCriacao().getCodigo().equals(getUsuarioLogado().getCodigo()) || loginControle.getPermissaoAcessoMenuVO().getAlterarListaExercicioOutroProfessorProfessor());
		}

		return false;
	}

	public void removerConteudoUnidadePaginaRecursoEducacionalAnterior() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().removerConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), (ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoAnterior"), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setRecursoAdicionadoComSucesso(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerConteudoUnidadePaginaRecursoEducacionalPosterior() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().removerConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), (ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoPosterior"), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setRecursoAdicionadoComSucesso(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerConteudoUnidadePaginaRecursoEducacionalApoioProfessor() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().removerConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina(), (ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoApoioProfessor"), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setRecursoAdicionadoComSucesso(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setRecursoAdicionadoComSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarTipoRecursoEducacionalConteudoUnidadePagina() {
		try {
			TipoRecursoEducacionalEnum tp = (TipoRecursoEducacionalEnum) context().getExternalContext().getRequestMap().get("recurso");
			if (!tp.equals(getConteudoUnidadePagina().getTipoRecursoEducacional())) {
				getConteudoUnidadePagina().setTipoRecursoEducacional(tp);
				getConteudoUnidadePagina().setRecursoEducacional(new RecursoEducacionalVO());
				if (tp.equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
					getConteudoUnidadePagina().setTexto(null);
				} else {
					getConteudoUnidadePagina().setTexto("");
				}
				getConteudoUnidadePagina().setCaminhoBaseRepositorio("");
				getConteudoUnidadePagina().setNomeFisicoArquivo("");
				getConteudoUnidadePagina().setNomeRealArquivo("");
				setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
				setFiltroNomeDisciplina("");
				inicializarRecursoEducacional();
				setApresentarListaRecursoEducacionalConteudo(false);

			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void fecharEditarEIniciarNovaPagina() {
		setUnidadeConteudoVO(new UnidadeConteudoVO());
		setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
	}

	public void editarEIniciarNovaPagina() {
		try {
			setPaginaGravadaComSucesso(false);
			setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
			setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
			getConteudoUnidadePagina().getTexto();
			getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(getUnidadeConteudoVO().getCodigo());
			if (!getUnidadeConteudoVO().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND)) {
				getConteudoUnidadePagina().setCorBackground(getUnidadeConteudoVO().getCorBackground());
				getConteudoUnidadePagina().setCaminhoBaseBackground(getUnidadeConteudoVO().getCaminhoBaseBackground());
				getConteudoUnidadePagina().setNomeImagemBackground(getUnidadeConteudoVO().getNomeImagemBackground());
				getConteudoUnidadePagina().setOrigemBackgroundConteudo(getUnidadeConteudoVO().getOrigemBackgroundConteudo());
				getConteudoUnidadePagina().setTamanhoImagemBackgroundConteudo(getUnidadeConteudoVO().getTamanhoImagemBackgroundConteudo());
			}
			// getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getPaginas()
			// + 1);
			getConteudoUnidadePagina().setPagina(getUnidadeConteudoVO().getConteudoUnidadePaginaVOs().size() + 1);
			getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getConteudoUnidadePaginaVOs().size() + 1);
			// setIrPagina(getUnidadeConteudoVO().getPaginas());
			// setEditarUnidade(true);
			// setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
			// setFiltroNomeDisciplina("");
			// inicializarRecursoEducacional();

			// } catch (ConsistirException e) {
			// setConsistirExceptionMensagemDetalhada("msg_erro", e,
			// Uteis.ERRO);
			setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
			inicializarRecursoEducacional();
			setApresentarListaRecursoEducacionalConteudo(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public Boolean getIsApresentarCancelarPagina() {
		return getConteudoUnidadePagina().getCodigo() == 0;
	}

	public Boolean paginaGravadaComSucesso;

	public Boolean getPaginaGravadaComSucesso() {
		if (paginaGravadaComSucesso == null) {
			paginaGravadaComSucesso = false;
		}
		return paginaGravadaComSucesso;
	}

	public void setPaginaGravadaComSucesso(Boolean paginaGravadaComSucesso) {
		this.paginaGravadaComSucesso = paginaGravadaComSucesso;
	}

	public String getAcaoModalNovaPagina() {
		if (getPaginaGravadaComSucesso()) {
			return "RichFaces.$('panelNovaPagina').hide()";
		}
		return "";
	}

	public void gravarConteudoUnidadePagina() {
		try {
			
			if(getConteudoUnidadePagina().getTexto().contains("background-image")) {
				getConteudoUnidadePagina().setTexto(formatarTextoSemBackGround(getConteudoUnidadePagina().getTexto()));
			}
			
			getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
			getFacadeFactory().getUnidadeConteudoFacade().adicionarPagina(getUnidadeConteudoVO(), getConteudoUnidadePagina());

			if (getConteudoUnidadePagina().getPublicarIconeVoltar()) {
				getConteudoUnidadePagina().setPublicarIconeVoltar(false);
				getIconeVOs().add(getConteudoUnidadePagina().getIconeVoltar());
			}
			if (getConteudoUnidadePagina().getPublicarIconeAvancar()) {
				getConteudoUnidadePagina().setPublicarIconeAvancar(false);
				getIconeVOs().add(getConteudoUnidadePagina().getIconeAvancar());
			}
			setPaginaGravadaComSucesso(true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarConteudoUnidadePaginaForm() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
			getFacadeFactory().getUnidadeConteudoFacade().adicionarPagina(getUnidadeConteudoVO(), getConteudoUnidadePagina());

			if (getConteudoUnidadePagina().getPublicarIconeVoltar()) {
				getConteudoUnidadePagina().setPublicarIconeVoltar(false);
				getIconeVOs().add(getConteudoUnidadePagina().getIconeVoltar());
			}
			if (getConteudoUnidadePagina().getPublicarIconeAvancar()) {
				getConteudoUnidadePagina().setPublicarIconeAvancar(false);
				getIconeVOs().add(getConteudoUnidadePagina().getIconeAvancar());
			}
			setUnidadeConteudoVO(new UnidadeConteudoVO());
			setPaginaGravadaComSucesso(true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirConteudoUnidadePaginaIndice() {
		try {
			ConteudoUnidadePaginaVO obj = (ConteudoUnidadePaginaVO) getRequestMap().get("pagina");
			if (!obj.isNovoObj()) {
				getFacadeFactory().getConteudoUnidadePaginaFacade().excluir(getConteudo(), obj, true, getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirConteudoUnidadePagina() {
		try {
			if (!getConteudoUnidadePagina().isNovoObj()) {
				getFacadeFactory().getConteudoUnidadePaginaFacade().excluir(getConteudo(), getConteudoUnidadePagina(), true, getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
			}
			getUnidadeConteudoVO().setPaginas(getUnidadeConteudoVO().getPaginas() - 1);
			setIrPagina(getIrPagina() - 1);
			if (getUnidadeConteudoVO().getPaginas() > 0) {
				if (getIrPagina() + 1 > getUnidadeConteudoVO().getPaginas()) {
					setIrPagina(getUnidadeConteudoVO().getPaginas());
				} else {
					setIrPagina(getIrPagina() + 1);
				}
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getIrPagina(), NivelMontarDados.TODOS, false, getUsuarioLogado()));

			} else {
				setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
				getConteudoUnidadePagina().setPagina(1);
				getUnidadeConteudoVO().setPaginas(1);
				setIrPagina(1);
				getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(getUnidadeConteudoVO().getCodigo());

			}
			setFiltroNomeDisciplina("");
			setTipoRecursoEducacional(getConteudoUnidadePagina().getTipoRecursoEducacional());
			inicializarRecursoEducacional();

			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarUnidadeConteudo() {
		// UnidadeConteudoVO obj = (UnidadeConteudoVO)
		// context().getExternalContext().getRequestMap().get("unidade");
		String titulo = "";
		try {
			if (getUnidadeConteudoVO().getTitulo().trim().isEmpty()) {
				UnidadeConteudoVO obj2 = getFacadeFactory().getUnidadeConteudoFacade().consultarPorChavePrimaria(getUnidadeConteudoVO().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
				titulo = (obj2.getTitulo());
			}
			getFacadeFactory().getUnidadeConteudoFacade().persistir(getUnidadeConteudoVO(), getConteudo().getDisciplina(), false, getUsuarioLogado(), false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			if (getUnidadeConteudoVO().getTitulo().trim().isEmpty()) {
				getUnidadeConteudoVO().setTitulo(titulo);
			}
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirUnidadeConteudo() {
		try {
			getFacadeFactory().getConteudoFacade().excluirUnidadeConteudo(getConteudo(), (UnidadeConteudoVO) getRequestMap().get("unidade"), true, getUsuarioLogado(), false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarUnidadeConteudo() {
		try {
			getFacadeFactory().getConteudoFacade().adicionarUnidadeConteudo(getConteudo(), getUnidadeConteudoVO(), true, getUsuarioLogado(), false);
			setUnidadeConteudoVO(new UnidadeConteudoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadArquivoConteudoUnidadePaginaRecursoEducacional(FileUploadEvent event) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().upLoadArquivoConteudoUnidadePaginaRecursoEducacional(event, getConteudo().getDisciplina().getCodigo(), getConteudoUnidadePaginaRecursoEducacional(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadArquivoConteudoUnidadePagina(FileUploadEvent event) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().upLoadArquivoConteudoUnidadePagina(event, getConteudo().getDisciplina().getCodigo(), getConteudoUnidadePagina(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void removerImagemSlideConteudoUnidadePagina(String imagem) {
		
			try {			
				getFacadeFactory().getConteudoUnidadePaginaFacade().removerImagemSlide(getConteudoUnidadePagina(), imagem, getUsuarioLogado());						
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			} catch (Exception e) {
				setMensagemDetalhada("smg_erro", e.getMessage(), Uteis.ERRO);
			}
		
	}
	
	public void removerImagemSlideConteudoUnidadePaginaREA(String imagem) {
			try {
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().removerImagemSlide(getConteudoUnidadePaginaRecursoEducacional(), imagem, getUsuarioLogado());
				setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			} catch (Exception e) {
				setMensagemDetalhada("smg_erro", e.getMessage(), Uteis.ERRO);
			}
	}

	public void iniciarAbrirModalUpload() {
		setNomeImagem("");
		setPublicarImagem(true);
		if (getTipoRecursoEducacional() == null) {
			setTipoRecursoEducacional(TipoRecursoEducacionalEnum.TEXTO_HTML);
		}
	}

	public void upLoadArquivoConteudoUnidadePaginaHtml(FileUploadEvent event) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().upLoadArquivoConteudoUnidadePaginaHtml(event, getConteudo().getDisciplina().getCodigo(), getConteudoUnidadePagina(), getPublicarImagem(), getNomeImagem(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			inicializarRecursoEducacional();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public String getExecutarScriptRecursoEducacionalConteudo() {
		if (getConteudoUnidadePagina().getTipoRecursoEducacional() != null) {
			if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {			
				return "definirGraficoApresentar('form:graficoGerado', 'true');";
			}
			if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
				return "iniciarBanner();";
			}
			if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM)) {
				return "iniciarCrop();";
			}			
		}
		return "";
	}

	public String getExecutarScriptRecursoEducacionalGatilho() {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional() != null) {
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO) ) {
				return "definirGraficoApresentar('formAddRecursoEducacional:graficoGeradoRE', 'false');";
			}
			
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
				return "iniciarBannerREA();";
			}
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.IMAGEM)) {
				return "iniciarCropREA();";
			}	
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
				return "inicializarForum();";
			}	
			
		}
		return "";
	}
	

	public void upLoadArquivoConteudoUnidadePaginaRecursoEducacionalHtml(FileUploadEvent event) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().upLoadArquivoConteudoUnidadePaginaHtml(event, getConteudo().getDisciplina().getCodigo(), getConteudoUnidadePagina(), getPublicarImagem(), getNomeImagem(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			inicializarRecursoEducacional();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void uploadArquivoIcone(FileUploadEvent event) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().upLoadIconeConteudoUnidadePagina(event, getConteudoUnidadePagina(), getUploadIconeVoltar(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void downloadArquivoConteudoUnidadePaginaRecursoEducacional() {
		try {

			context().getExternalContext().getSessionMap().put("nomeArquivo", getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo());
			context().getExternalContext().getSessionMap().put("nomeReal", getConteudoUnidadePaginaRecursoEducacional().getNomeRealArquivo());
			if (getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio().contains("_TMP")) {
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio());
			} else {
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio());
			}
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			// context().getExternalContext().dispatch("/DownloadSV");
			// FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	private String erroUpload;

	public String getErroUpload() {
		if (erroUpload == null) {
			erroUpload = "";
		}
		return erroUpload;
	}

	public void setErroUpload(String erroUpload) {
		this.erroUpload = erroUpload;
	}

	public String getVerificarUltrapassouTamanhoMaximoUpload() {
		String tamanhoMaximo = ((HttpServletRequest) context().getExternalContext().getRequest()).getSession().getServletContext().getInitParameter("tamanhoMaximoUpload");
		if (tamanhoMaximo == null) {
			// setMensagemDetalhada("msg_erro",
			// "Arquivo não Enviado. Tamanho Máximo Permitido 15MB.",
			// Uteis.ERRO);
			return "Arquivo não Enviado. Tamanho Máximo Permitido 15MB.";
		} else {
			// setMensagemDetalhada("msg_erro",
			// "Arquivo não Enviado. Tamanho Máximo Permitido 15MB.",
			// Uteis.ERRO);
			return "Arquivo não Enviado. Tamanho Máximo Permitido: " + tamanhoMaximo;
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDisciplina() {
		getConteudo().setDisciplina(null);
		setDisciplinaVO(null);
		getListaConsultaConteudo().clear();
		getListaConsulta().clear();
		setMensagemID("msg_informe_disciplina", Uteis.ALERTA);
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			if(!getFacadeFactory().getConteudoFacade().validarDadosAlteracaoDisciplina(getConteudo(), disciplina)){
				setModalPanelDisciplinaConteudoClone("RichFaces.$('panelDisciplinaConteudoClone').show();");
				setDisciplinaModalConteudoClone(disciplina);
			}else{
				setModalPanelDisciplinaConteudoClone("RichFaces.$('panelDisciplina').hide();");	
				getConteudo().setDisciplina(disciplina);
				montarComboBoxTemaAssuntoDisciplina();
			}
			
		} catch (Exception e) {
			setModalPanelDisciplinaConteudoClone("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void clonarReasOutraDisciplina(){
		try {
			if(getConteudo().isClonarReasOutraDisciplina()){
				getConteudo().setDescartarReasOutraDisciplina(false);
			}else{
				getConteudo().setClonarQuestaoExercicio(false);
		    	getConteudo().setClonarQuestaoOnline(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}
	
	public void descartarReasOutraDisciplina(){
		try {
			if(getConteudo().isDescartarReasOutraDisciplina()){
				getConteudo().setClonarReasOutraDisciplina(false);
				getConteudo().setClonarQuestaoExercicio(false);
				getConteudo().setClonarQuestaoOnline(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}
	
	public void realizarClonagemReaPorSelecaoDisciplina(){
		try {
			getConteudo().setDisciplina(getDisciplinaModalConteudoClone());
			getFacadeFactory().getConteudoFacade().realizarClonagemReaPorSelecaoDisciplina(getConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			montarComboBoxTemaAssuntoDisciplina();
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}	
	}

	public List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	public void consultarConteudista() {
		try {

			getControleConsultaOtimizado().setLimitePorPagina(10);
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);

			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), 0, "", 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorNome(getControleConsulta().getValorConsulta(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), 0, 0, null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorMatricula(getControleConsulta().getValorConsulta(), 0, null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getControleConsulta().getValorConsulta(), 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCidade(getControleConsulta().getValorConsulta(), 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getControleConsulta().getValorConsulta(), 0, "", 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCPF(getControleConsulta().getValorConsulta(), "", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getControleConsulta().getValorConsulta(), 0, 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorCargo(getControleConsulta().getValorConsulta(), 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getControleConsulta().getValorConsulta(), "FU", 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorNomeDepartamento(getControleConsulta().getValorConsulta(), "FU", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getControleConsulta().getValorConsulta(), "FU", 0, null, null, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioFacade().consultaTotalDeRegistroRapidaPorUnidadeEnsino(getControleConsulta().getValorConsulta(), "FU", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("CPF")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}

	public void scrollerListenerConteudista(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarConteudista();
	}

	public void selecionarConteudista() {
		getConteudo().setConteudista(((FuncionarioVO) getRequestMap().get("funcionarioItens")).getPessoa());
	}

	public List<SelectItem> tipoConsultaComboConteudista;

	public List<SelectItem> getTipoConsultaComboConteudista() {
		if (tipoConsultaComboConteudista == null) {
			tipoConsultaComboConteudista = new ArrayList<SelectItem>(0);
			tipoConsultaComboConteudista.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboConteudista.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboConteudista.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboConteudista.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboConteudista.add(new SelectItem("departamento", "Departamento"));
			tipoConsultaComboConteudista.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		}
		return tipoConsultaComboConteudista;
	}

	public void limparConteudista() {
		getConteudo().setConteudista(null);
	}

	public List<SelectItem> getListaSelectItemTipoConteudista() {
		return TipoConteudistaEnum.getCombobox();
	}

	public List<SelectItem> getListaSelectItemTipoGrafico() {
		return TipoGraficoEnum.getCombobox();
	}

	public String inicializarConsultar() {
		try {
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		limparMensagem();

		return getCaminhoRedirecionamentoNavegacao("conteudoCons");
	}

	public ConteudoVO getConteudo() {
		if (conteudo == null) {
			conteudo = new ConteudoVO();
		}
		return conteudo;
	}

	public void setConteudo(ConteudoVO conteudo) {
		this.conteudo = conteudo;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<ConteudoVO> getListaConsultaConteudo() {
		if (listaConsultaConteudo == null) {
			listaConsultaConteudo = new ArrayList<ConteudoVO>(0);
		}
		return listaConsultaConteudo;
	}

	public void setListaConsultaConteudo(List<ConteudoVO> listaConsultaConteudo) {
		this.listaConsultaConteudo = listaConsultaConteudo;
	}

	public UnidadeConteudoVO getUnidadeConteudoVO() {
		if (unidadeConteudoVO == null) {
			unidadeConteudoVO = new UnidadeConteudoVO();
		}
		return unidadeConteudoVO;
	}

	public void setUnidadeConteudoVO(UnidadeConteudoVO unidadeConteudoVO) {
		this.unidadeConteudoVO = unidadeConteudoVO;
	}

	public Boolean getIncluirUnidade() {
		if (incluirUnidade == null) {
			incluirUnidade = false;
		}
		return incluirUnidade;
	}

	public void setIncluirUnidade(Boolean incluirUnidade) {
		this.incluirUnidade = incluirUnidade;
	}

	public Boolean getEditarUnidade() {
		if (editarUnidade == null) {
			editarUnidade = false;
		}
		return editarUnidade;
	}

	public void setEditarUnidade(Boolean editarUnidade) {
		this.editarUnidade = editarUnidade;
	}

	public ConteudoUnidadePaginaVO getConteudoUnidadePagina() {
		if (conteudoUnidadePagina == null) {
			conteudoUnidadePagina = new ConteudoUnidadePaginaVO();
		}
		return conteudoUnidadePagina;
	}

	public void setConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina) {
		this.conteudoUnidadePagina = conteudoUnidadePagina;
	}

	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacional() {
		if (conteudoUnidadePaginaRecursoEducacional == null) {
			conteudoUnidadePaginaRecursoEducacional = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacional;
	}

	public void setConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional) {
		this.conteudoUnidadePaginaRecursoEducacional = conteudoUnidadePaginaRecursoEducacional;
	}

	public Integer getIrPagina() {
		if (irPagina == null) {
			irPagina = 1;
		}
		return irPagina;
	}

	public void setIrPagina(Integer irPagina) {
		this.irPagina = irPagina;
	}

	public List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalConteudo() {
		return TipoRecursoEducacionalEnum.getTipoRecursoEducacionalConteudo();
	}

	public List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalGatilho() {
		return TipoRecursoEducacionalEnum.getTipoRecursoEducacionalGatilho();
	}

	public List<TipoRecursoEducacionalEnum> getTipoRecursoEducacionalGatilhoApoioProfessor() {
		return TipoRecursoEducacionalEnum.getTipoRecursoEducacionalGatilhoApoioProfessor();
	}

	/*public Boolean getRecursoEducacionalAnterior() {
		return recursoEducacionalAnterior;
	}

	public void setRecursoEducacionalAnterior(Boolean recursoEducacionalAnterior) {
		this.recursoEducacionalAnterior = recursoEducacionalAnterior;
	}*/

	public Boolean getRecursoAdicionadoComSucesso() {
		return recursoAdicionadoComSucesso;
	}

	public void setRecursoAdicionadoComSucesso(Boolean recursoAdicionadoComSucesso) {
		this.recursoAdicionadoComSucesso = recursoAdicionadoComSucesso;
	}

	public List<RecursoEducacionalVO> getRecursoEducacionalVOs() {
		if (recursoEducacionalVOs == null) {
			recursoEducacionalVOs = new ArrayList<RecursoEducacionalVO>(0);
		}
		return recursoEducacionalVOs;
	}

	public void setRecursoEducacionalVOs(List<RecursoEducacionalVO> recursoEducacionalVOs) {
		this.recursoEducacionalVOs = recursoEducacionalVOs;
	}

	public TreeNodeCustomizado getTreeNodeCustomizado() {
		if(treeNodeCustomizado == null){
			treeNodeCustomizado = new TreeNodeCustomizado();
		}
		return treeNodeCustomizado;
	}

	public void setTreeNodeCustomizado(TreeNodeCustomizado TreeNodeCustomizado) {
		this.treeNodeCustomizado = TreeNodeCustomizado;
	}

	public TipoRecursoEducacionalEnum getTipoRecursoEducacional() {
		return tipoRecursoEducacional;
	}

	public void setTipoRecursoEducacional(TipoRecursoEducacionalEnum tipoRecursoEducacional) {
		this.tipoRecursoEducacional = tipoRecursoEducacional;
	}

	public String getFiltroNomeDisciplina() {
		if (filtroNomeDisciplina == null) {
			filtroNomeDisciplina = "";
		}
		return filtroNomeDisciplina;
	}

	public void setFiltroNomeDisciplina(String filtroNomeDisciplina) {
		this.filtroNomeDisciplina = filtroNomeDisciplina;
	}

	public Boolean getUtilizarRecursoEducacionalGatilho() {
		if (utilizarRecursoEducacionalConteudo == null) {
			utilizarRecursoEducacionalConteudo = false;
		}
		return utilizarRecursoEducacionalGatilho;
	}

	public void setUtilizarRecursoEducacionalGatilho(Boolean utilizarRecursoEducacionalGatilho) {
		this.utilizarRecursoEducacionalGatilho = utilizarRecursoEducacionalGatilho;
	}

	public Boolean getUtilizarRecursoEducacionalConteudo() {
		if (utilizarRecursoEducacionalConteudo == null) {
			utilizarRecursoEducacionalConteudo = false;
		}
		return utilizarRecursoEducacionalConteudo;
	}

	public void setUtilizarRecursoEducacionalConteudo(Boolean utilizarRecursoEducacionalConteudo) {
		this.utilizarRecursoEducacionalConteudo = utilizarRecursoEducacionalConteudo;
	}

	public String getCaminhoArquivoConteudoUnidadePagina() {		
		if(getNomeTelaAtual().contains("conteudoAlunoForm.xhtml")) {
			return getConteudoUnidadePagina().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), "calc(100vh - 230px)");
		}else {
			return getConteudoUnidadePagina().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), "360px");
		}
	}

	public String getCaminhoArquivoConteudoUnidadePaginaRecursoEducacional() {
		if(getNomeTelaAtual().contains("conteudoAlunoForm.xhtml")) {
			if(isMaximizarTela()) {
				return getConteudoUnidadePaginaRecursoEducacional().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL) ? 88 : 64);
			}else {
				return getConteudoUnidadePaginaRecursoEducacional().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), 240);
			}
		}
		if(isMaximizarTela()) {
			return getConteudoUnidadePaginaRecursoEducacional().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), 150);
		}else {
			return getConteudoUnidadePaginaRecursoEducacional().getConteudoPaginaApresentar(getConfiguracaoGeralPadraoSistema(), 350);
		}
	}

	public void adicionarSerieGraficoConteudoUnidadePagina() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().adicionarSerieGrafico(getConteudoUnidadePagina(), getSerieGraficoConteudoUnidadePagina(), getValorGraficoConteudoUnidadePagina());
			setSerieGraficoConteudoUnidadePagina("");
			setValorGraficoConteudoUnidadePagina(0.0);
			visualizarGraficoConteudoUnidadePagina();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerSerieGraficoConteudoUnidadePagina() {
		try {
			Object objSerie = context().getExternalContext().getRequestMap().get("serieItens");
			String serie = "";
			if (objSerie instanceof ConteudoUnidadePaginaGraficoSerieVO) {
				serie = ((ConteudoUnidadePaginaGraficoSerieVO) objSerie).getSerie();
			}
			if (objSerie instanceof ConteudoUnidadePaginaGraficoPizzaVO) {
				serie = ((ConteudoUnidadePaginaGraficoPizzaVO) objSerie).getSerie();
			}

			getFacadeFactory().getConteudoUnidadePaginaFacade().removerSerieGrafico(getConteudoUnidadePagina(), serie);
			visualizarGraficoConteudoUnidadePagina();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarCategoriaGraficoConteudoUnidadePagina() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().adicionarCategoriaGrafico(getConteudoUnidadePagina(), getCategoriaGraficoConteudoUnidadePagina());
			setCategoriaGraficoConteudoUnidadePagina("");
			visualizarGraficoConteudoUnidadePagina();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerCategoriaGraficoConteudoUnidadePagina() {
		try {
			ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO = (ConteudoUnidadePaginaGraficoCategoriaVO) context().getExternalContext().getRequestMap().get("categoria");
			getFacadeFactory().getConteudoUnidadePaginaFacade().removerCategoriaGrafico(getConteudoUnidadePagina(), conteudoUnidadePaginaGraficoCategoriaVO);
			visualizarGraficoConteudoUnidadePagina();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarSerieGraficoConteudoUnidadePaginaRecursoEducacional() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().adicionarSerieGrafico(getConteudoUnidadePaginaRecursoEducacional(), getSerieGraficoConteudoUnidadePaginaRecursoEducacional(), getValorGraficoConteudoUnidadePaginaRecursoEducacional());
			setSerieGraficoConteudoUnidadePaginaRecursoEducacional("");
			setValorGraficoConteudoUnidadePaginaRecursoEducacional(0.0);
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerSerieGraficoConteudoUnidadePaginaRecursoEducacional() {
		try {
			Object objSerie = context().getExternalContext().getRequestMap().get("serieRE");
			String serie = "";
			if (objSerie instanceof ConteudoUnidadePaginaGraficoSerieVO) {
				serie = ((ConteudoUnidadePaginaGraficoSerieVO) objSerie).getSerie();
			}
			if (objSerie instanceof ConteudoUnidadePaginaGraficoPizzaVO) {
				serie = ((ConteudoUnidadePaginaGraficoPizzaVO) objSerie).getSerie();
			}
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().removerSerieGrafico(getConteudoUnidadePaginaRecursoEducacional(), serie);
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarCategoriaGraficoConteudoUnidadePaginaRecursoEducacional() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().adicionarCategoriaGrafico(getConteudoUnidadePaginaRecursoEducacional(), getCategoriaGraficoConteudoUnidadePaginaRecursoEducacional());
			setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional("");
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerCategoriaGraficoConteudoUnidadePaginaRecursoEducacional() {
		try {
			ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO = (ConteudoUnidadePaginaGraficoCategoriaVO) context().getExternalContext().getRequestMap().get("categoriaRE");
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().removerCategoriaGrafico(getConteudoUnidadePaginaRecursoEducacional(), conteudoUnidadePaginaGraficoCategoriaVO);
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarGraficoConteudoUnidadePaginaRecursoEducacional() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				if (!getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getIsApresentarVisaoPais() && !getEmularAcessoAluno()) {
					getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarGeracaoGrafico(getConteudoUnidadePaginaRecursoEducacional());
				}
				carregarGraficoConteudoUnidadePaginaRecursoEducacional();
			}
		} catch (Exception e) {
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void carregarGraficoConteudoUnidadePaginaRecursoEducacional() throws Exception {

		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			context().getExternalContext().getSessionMap().put("tipoGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getTipoGrafico().getTipoGraficoApresentar());
			context().getExternalContext().getSessionMap().put("tituloGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getTituloGrafico());
			context().getExternalContext().getSessionMap().put("categoriaGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getCategoriaGrafico());
			context().getExternalContext().getSessionMap().put("tituloXGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getTituloEixoX());
			context().getExternalContext().getSessionMap().put("tituloYGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getTituloEixoY());
			context().getExternalContext().getSessionMap().put("valorGraficoRE", getConteudoUnidadePaginaRecursoEducacional().getValorGrafico());
			context().getExternalContext().getSessionMap().put("apresentarLegendaRE", getConteudoUnidadePaginaRecursoEducacional().getApresentarLegenda());
			if(getConteudoUnidadePaginaRecursoEducacional().getTipoGrafico().getPermiteTituloXY() && !getConteudoUnidadePaginaRecursoEducacional().getConteudoUnidadePaginaGraficoCategoriaVOs().isEmpty()
					&& !getConteudoUnidadePaginaRecursoEducacional().getConteudoUnidadePaginaGraficoSerieVOs().isEmpty()) {
				setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(true);
			}else if(!getConteudoUnidadePaginaRecursoEducacional().getTipoGrafico().getPermiteTituloXY() && !getConteudoUnidadePaginaRecursoEducacional().getConteudoUnidadePaginaGraficoVOs().isEmpty()) {
				setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(true);
			}
		}
	}
	
	public void editarGraficoConteudoUnidadePaginaRecursoEducacional() {
		setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
	}

	public void editarGraficoConteudoUnidadePagina() {
		setVisualizarGraficoConteudoUnidadePagina(false);
	}

	public Integer getQtdeElementosSerieGraficoConteudoUnidadePaginaApresentar() {

		return getConteudoUnidadePagina().getConteudoUnidadePaginaGraficoSerieVOs().size();

	}

	public Integer getQtdeElementosSerieGraficoConteudoUnidadePaginaApresentarRecursoEducacional() {
		return getConteudoUnidadePaginaRecursoEducacional().getConteudoUnidadePaginaGraficoSerieVOs().size();
	}

	public String getCategoriaGraficoConteudoUnidadePagina() {
		if (categoriaGraficoConteudoUnidadePagina == null) {
			categoriaGraficoConteudoUnidadePagina = "";
		}
		return categoriaGraficoConteudoUnidadePagina;
	}

	public void setCategoriaGraficoConteudoUnidadePagina(String categoriaGraficoConteudoUnidadePagina) {
		this.categoriaGraficoConteudoUnidadePagina = categoriaGraficoConteudoUnidadePagina;
	}

	public String getSerieGraficoConteudoUnidadePagina() {
		if (serieGraficoConteudoUnidadePagina == null) {
			serieGraficoConteudoUnidadePagina = "";
		}
		return serieGraficoConteudoUnidadePagina;
	}

	public void setSerieGraficoConteudoUnidadePagina(String serieGraficoConteudoUnidadePagina) {
		this.serieGraficoConteudoUnidadePagina = serieGraficoConteudoUnidadePagina;
	}

	public Double getValorGraficoConteudoUnidadePagina() {
		if (valorGraficoConteudoUnidadePagina == null) {
			valorGraficoConteudoUnidadePagina = 0.0;
		}
		return valorGraficoConteudoUnidadePagina;
	}

	public void setValorGraficoConteudoUnidadePagina(Double valorGraficoConteudoUnidadePagina) {
		this.valorGraficoConteudoUnidadePagina = valorGraficoConteudoUnidadePagina;
	}

	public String getCategoriaGraficoConteudoUnidadePaginaRecursoEducacional() {
		if (categoriaGraficoConteudoUnidadePaginaRecursoEducacional == null) {
			categoriaGraficoConteudoUnidadePaginaRecursoEducacional = "";
		}
		return categoriaGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public void setCategoriaGraficoConteudoUnidadePaginaRecursoEducacional(String categoriaGraficoConteudoUnidadePaginaRecursoEducacional) {
		this.categoriaGraficoConteudoUnidadePaginaRecursoEducacional = categoriaGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public String getSerieGraficoConteudoUnidadePaginaRecursoEducacional() {
		if (serieGraficoConteudoUnidadePaginaRecursoEducacional == null) {
			serieGraficoConteudoUnidadePaginaRecursoEducacional = "";
		}
		return serieGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public void setSerieGraficoConteudoUnidadePaginaRecursoEducacional(String serieGraficoConteudoUnidadePaginaRecursoEducacional) {
		this.serieGraficoConteudoUnidadePaginaRecursoEducacional = serieGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public Double getValorGraficoConteudoUnidadePaginaRecursoEducacional() {
		if (valorGraficoConteudoUnidadePaginaRecursoEducacional == null) {
			valorGraficoConteudoUnidadePaginaRecursoEducacional = 0.;
		}
		return valorGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public void setValorGraficoConteudoUnidadePaginaRecursoEducacional(Double valorGraficoConteudoUnidadePaginaRecursoEducacional) {
		this.valorGraficoConteudoUnidadePaginaRecursoEducacional = valorGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public Boolean getVisualizarGraficoConteudoUnidadePagina() {
		if (visualizarGraficoConteudoUnidadePagina == null) {
			visualizarGraficoConteudoUnidadePagina = false;
		}
		return visualizarGraficoConteudoUnidadePagina;
	}

	public void setVisualizarGraficoConteudoUnidadePagina(Boolean visualizarGraficoConteudoUnidadePagina) {
		this.visualizarGraficoConteudoUnidadePagina = visualizarGraficoConteudoUnidadePagina;
	}

	public Boolean getVisualizarGraficoConteudoUnidadePaginaRecursoEducacional() {
		if (visualizarGraficoConteudoUnidadePaginaRecursoEducacional == null) {
			visualizarGraficoConteudoUnidadePaginaRecursoEducacional = false;
		}
		return visualizarGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public void setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(Boolean visualizarGraficoConteudoUnidadePaginaRecursoEducacional) {
		this.visualizarGraficoConteudoUnidadePaginaRecursoEducacional = visualizarGraficoConteudoUnidadePaginaRecursoEducacional;
	}

	public void minimizarListaRecursoEducacionalConteudo() {
		setApresentarListaRecursoEducacionalConteudo(false);
	}

	public void maximizarListaRecursoEducacionalConteudo() {
		setApresentarListaRecursoEducacionalConteudo(true);
	}

	public void minimizarListaRecursoEducacionalGatilho() {
		setApresentarListaRecursoEducacionalGatilho(false);
	}

	public void maximizarListaRecursoEducacionalGatilho() {
		setApresentarListaRecursoEducacionalGatilho(true);
	}

	public Boolean getApresentarListaRecursoEducacionalConteudo() {
		if (apresentarListaRecursoEducacionalConteudo == null) {
			apresentarListaRecursoEducacionalConteudo = true;
		}
		return apresentarListaRecursoEducacionalConteudo;
	}

	public void setApresentarListaRecursoEducacionalConteudo(Boolean apresentarListaRecursoEducacionalConteudo) {
		this.apresentarListaRecursoEducacionalConteudo = apresentarListaRecursoEducacionalConteudo;
	}

	public Boolean getApresentarListaRecursoEducacionalGatilho() {
		if (apresentarListaRecursoEducacionalGatilho == null) {
			apresentarListaRecursoEducacionalGatilho = true;
		}
		return apresentarListaRecursoEducacionalGatilho;
	}

	public void setApresentarListaRecursoEducacionalGatilho(Boolean apresentarListaRecursoEducacionalGatilho) {
		this.apresentarListaRecursoEducacionalGatilho = apresentarListaRecursoEducacionalGatilho;
	}

	public List<IconeVO> getIconeVOs() {
		if (iconeVOs == null) {
			try {
				iconeVOs = getFacadeFactory().getIconeFacade().consultarIcones(0, 0, getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return iconeVOs;
	}

	public String getIconeVoltarApresentar() {
		return "/resources/imagens/botaoSetaEsquerda.jpg";
		// try {
		// if
		// (getConteudoUnidadePagina().getCaminhoIconeVoltar().contains("/imagens"))
		// {
		// return getConteudoUnidadePagina().getCaminhoIconeVoltar() + "/" +
		// getConteudoUnidadePagina().getNomeIconeVoltar();
		// }
		// return
		// getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() +
		// "/" +
		// getConteudoUnidadePagina().getCaminhoIconeVoltar().replaceAll("\\\\",
		// "/").trim() + "/"
		// + getConteudoUnidadePagina().getNomeIconeVoltar();
		// } catch (Exception e) {
		// return "./imagens/botaoAnteriorIcone.png";
		// }
	}

	public String getIconeAvancarApresentar() {
		return "/resources/imagens/botaoSetaDireita.jpg";
		// try {
		// if
		// (getConteudoUnidadePagina().getCaminhoIconeAvancar().contains("/imagens"))
		// {
		// return getConteudoUnidadePagina().getCaminhoIconeAvancar() + "/" +
		// getConteudoUnidadePagina().getNomeIconeAvancar();
		// }
		// return
		// getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() +
		// "/" +
		// getConteudoUnidadePagina().getCaminhoIconeAvancar().replaceAll("\\\\",
		// "/").trim() +
		// "/" + getConteudoUnidadePagina().getNomeIconeAvancar();
		// } catch (Exception e) {
		// return "./imagens/botaoProximoIcone.png";
		// }
	}

	public void setIconeVOs(List<IconeVO> iconeVOs) {
		this.iconeVOs = iconeVOs;
	}

	public Boolean getUploadIconeVoltar() {
		if (uploadIconeVoltar == null) {
			uploadIconeVoltar = true;
		}
		return uploadIconeVoltar;
	}

	public void setUploadIconeVoltar(Boolean uploadIconeVoltar) {
		this.uploadIconeVoltar = uploadIconeVoltar;
	}

	public void adicionarIconeVoltar() {
		setUploadIconeVoltar(true);
		limparMensagem();
	}

	public void adicionarIconeAvancar() {
		setUploadIconeVoltar(false);
		limparMensagem();
	}

	public Integer getQtdeColunaIconeApresentar() {
		if (getIconeVOs().size() > 28) {
			return 28;
		}
		return getIconeVOs().size();
	}

	public Integer getQtdeIconeApresentar() {
		return getIconeVOs().size();
	}

	public List<ForumVO> getForumVOs() {
		if (forumVOs == null) {
			forumVOs = new ArrayList<ForumVO>(0);
		}
		return forumVOs;
	}

	public void setForumVOs(List<ForumVO> forumVOs) {
		this.forumVOs = forumVOs;
	}

	public Integer getCodigoDisciplinaAluno() {
		if (codigoDisciplinaAluno == null) {
			codigoDisciplinaAluno = 0;
		}
		return codigoDisciplinaAluno;
	}

	public void setCodigoDisciplinaAluno(Integer codigoDisciplinaAluno) {
		this.codigoDisciplinaAluno = codigoDisciplinaAluno;
	}

	public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
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

	public Boolean getNaoExisteConteudoCadastrado() {
		if (naoExisteConteudoCadastrado == null) {
			naoExisteConteudoCadastrado = false;
		}
		return naoExisteConteudoCadastrado;
	}

	public void setNaoExisteConteudoCadastrado(Boolean naoExisteConteudoCadastrado) {
		this.naoExisteConteudoCadastrado = naoExisteConteudoCadastrado;
	}

	public Boolean getApresentarGatilho() {
		if (apresentarGatilho == null) {
			apresentarGatilho = false;
		}
		return apresentarGatilho;
	}

	public void setApresentarGatilho(Boolean apresentarGatilho) {
		this.apresentarGatilho = apresentarGatilho;
	}

	/**
	 * Utilizado na visão do aluno
	 */
	private ForumVO forum;
	private ForumInteracaoVO forumInteracao;
	private OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao;
	private DataModelo controleConsultaInteracao;
	private ForumInteracaoVO forumInteracaoFilho;
	private Boolean mostarGabarito;

	public ForumVO getForum() {
		if (forum == null) {
			forum = new ForumVO();
		}
		return forum;
	}

	public void setForum(ForumVO forum) {
		this.forum = forum;
	}
	
	public ForumInteracaoVO getForumInteracaoFilho() {
		if (forumInteracaoFilho == null) {
			forumInteracaoFilho = new ForumInteracaoVO();
        }
		return forumInteracaoFilho;
	}

	public void setForumInteracaoFilho(ForumInteracaoVO forumInteracaoFilho) {
		this.forumInteracaoFilho = forumInteracaoFilho;
	}

	public DataModelo getControleConsultaInteracao() {
		if (controleConsultaInteracao == null) {
			controleConsultaInteracao = new DataModelo();
		}
		return controleConsultaInteracao;
	}

	public void setControleConsultaInteracao(DataModelo controleConsultaInteracao) {
		this.controleConsultaInteracao = controleConsultaInteracao;
	}

	public OpcaoOrdenacaoForumInteracaoEnum getOpcaoOrdenacaoForumInteracao() {
		if (opcaoOrdenacaoForumInteracao == null) {
			opcaoOrdenacaoForumInteracao = OpcaoOrdenacaoForumInteracaoEnum.DATA_INTERACAO;
		}
		return opcaoOrdenacaoForumInteracao;
	}

	public void setOpcaoOrdenacaoForumInteracao(OpcaoOrdenacaoForumInteracaoEnum opcaoOrdenacaoForumInteracao) {
		this.opcaoOrdenacaoForumInteracao = opcaoOrdenacaoForumInteracao;
	}

	public void consultarForumInteracao() throws Exception {
		getControleConsultaInteracao().setLimitePorPagina(10);
		getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getForumInteracaoFacade().consultarPorForum(getForum().getCodigo(), getOpcaoOrdenacaoForumInteracao(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), false, getUsuarioLogado()));
		getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getForumInteracaoFacade().consultarTotalRegistroPorForum(getForum().getCodigo()));
	}

	public void scrollListenerInteracao(DataScrollEvent DataScrollEvent) {
		getControleConsultaInteracao().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaInteracao().setPage(DataScrollEvent.getPage());
		try {
			consultarForumInteracao();
			limparMensagem();
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void gostarForumInteracao() {
		try {
			if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				ForumInteracaoVO forumInteracaoVO = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
	            getFacadeFactory().getForumInteracaoGostadoFacade().incluir(forumInteracaoVO, getUsuarioLogado());
	            forumInteracaoVO.setQtdeGostado(getFacadeFactory().getForumInteracaoFacade().consultarTotalGostado(forumInteracaoVO.getCodigo()));
			} else {
				ForumInteracaoVO forumInteracaoVO =  ((ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens"));
				forumInteracaoVO.setJaGostado(true);
				forumInteracaoVO.setQtdeGostado(forumInteracaoVO.getQtdeGostado() + 1);
			}
			setMensagemID("msg_forum_gostado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirGostarForumInteracao() {
		try {
			if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				ForumInteracaoVO forumInteracaoVO = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
				getFacadeFactory().getForumInteracaoGostadoFacade().excluir(forumInteracaoVO, getUsuarioLogado());
				forumInteracaoVO.setQtdeGostado(getFacadeFactory().getForumInteracaoFacade().consultarTotalGostado(forumInteracaoVO.getCodigo()));
			} else {
				ForumInteracaoVO forumInteracaoVO =  ((ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens"));
				forumInteracaoVO.setJaGostado(false);
				forumInteracaoVO.setQtdeGostado(forumInteracaoVO.getQtdeGostado() - 1);
			}
			setMensagemID("msg_forum_gostado", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirForumInteracao() {
		try {
			if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				getFacadeFactory().getForumInteracaoFacade().excluir((ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens"), false, getUsuarioLogado());
				setMensagemID("msg_interacao_excluida", Uteis.SUCESSO);
			} else {
				setMensagemDetalhada("msg_erro", "Ação não permitida no recurso de emulação", Uteis.ERRO);
			}

		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarForumInteracao() {
		try {
			if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				getForumInteracao().setForum(getForum().getCodigo());
				getForumInteracao().setForumInteracaoPai(new ForumInteracaoVO());
				getForumInteracao().setUsuarioInteracao(getUsuarioLogadoClone());
				getFacadeFactory().getForumInteracaoFacade().persistir(getForumInteracao(), false, getUsuarioLogado());				
				getControleConsultaInteracao().setLimitePorPagina(10);
				getControleConsultaInteracao().setTotalRegistrosEncontrados(getFacadeFactory().getForumInteracaoFacade().consultarTotalRegistroPorForum(getForum().getCodigo()));
				if(getControleConsultaInteracao().getTotalRegistrosEncontrados()/10.0 > 0) {
					getControleConsultaInteracao().setPage(Uteis.arredondarParaMais(getControleConsultaInteracao().getTotalRegistrosEncontrados()/10.0));
					getControleConsultaInteracao().setPaginaAtual(Uteis.arredondarParaMais(getControleConsultaInteracao().getTotalRegistrosEncontrados()/10.0));
				}
				getControleConsultaInteracao().setListaConsulta(getFacadeFactory().getForumInteracaoFacade().consultarPorForum(getForum().getCodigo(), getOpcaoOrdenacaoForumInteracao(), getControleConsultaInteracao().getLimitePorPagina(), getControleConsultaInteracao().getOffset(), false, getUsuarioLogado()));
				
				consultarForumInteracao();
				setForumInteracao(new ForumInteracaoVO());
				setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
			} else {				
				setMensagemDetalhada("msg_erro", "Ação não permitida no recurso de emulação", Uteis.ERRO);
			}
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("scrollTOREA('div.textoInteracaoForum"+getControleConsultaInteracao().getTotalRegistrosEncontrados()+"');");
		} catch (ConsistirException e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void responderComentarioForumInteracao() {
        try {
        	ForumInteracaoVO obj = (ForumInteracaoVO) context().getExternalContext().getRequestMap().get("forumInteracaoItens");
        	if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
	            setForumInteracaoFilho(new ForumInteracaoVO());
	            getForumInteracaoFilho().setForum(obj.getForum());
	            getForumInteracaoFilho().setForumInteracaoPai(obj);
	            getForumInteracaoFilho().setUsuarioInteracao(getUsuarioLogadoClone());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
        	} else {				
				setMensagemDetalhada("msg_erro", "Ação não permitida no recurso de emulação", Uteis.ERRO);
			}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void persistirRespostaForumInteracao() {
        try {
        	if (!getEmularAcessoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
	        	executarValidacaoSimulacaoVisaoAluno();
	            getFacadeFactory().getForumInteracaoFacade().persistir(getForumInteracaoFilho(), false, getUsuarioLogado());
	            consultarForumInteracao();
	            setForumInteracaoFilho(new ForumInteracaoVO());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
        	} else {				
				setMensagemDetalhada("msg_erro", "Ação não permitida no recurso de emulação", Uteis.ERRO);
			}
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

	private List<SelectItem> listaSelectItemOpcaoOrdenacaoForumInteracao;

	public List<SelectItem> getListaSelectItemOpcaoOrdenacaoForumInteracao() {
		if (listaSelectItemOpcaoOrdenacaoForumInteracao == null) {
			listaSelectItemOpcaoOrdenacaoForumInteracao = new ArrayList<SelectItem>(0);
			for (OpcaoOrdenacaoForumInteracaoEnum opcao : OpcaoOrdenacaoForumInteracaoEnum.values()) {
				listaSelectItemOpcaoOrdenacaoForumInteracao.add(new SelectItem(opcao, opcao.getValorApresentar()));
			}
		}
		return listaSelectItemOpcaoOrdenacaoForumInteracao;
	}

	public String getCaminhoBaseFoto() {
		try {

			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();
		} catch (Exception e) {
			return "/resources/imagens/visao/foto_usuario.png";
		}
	}

	public void editarForum() {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
			try {

				setForum(getFacadeFactory().getForumFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getForum().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS));
				setForumInteracao(null);
				getControleConsultaInteracao().setPage(0);
				getControleConsultaInteracao().setPaginaAtual(1);
				setOpcaoOrdenacaoForumInteracao(OpcaoOrdenacaoForumInteracaoEnum.DATA_INTERACAO);
				consultarForumInteracao();
				if (!getEmularAcessoAluno() && !getEmularAcessoProfessor()) {
					getFacadeFactory().getForumAcessoFacade().incluir(getForum(), getUsuarioLogado());

					if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("aluno") || getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("pais"))) {
						VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
						visaoAlunoControle.setQtdeAtualizacaoForum(null);
						setSucesso(false);
						setMensagemID("msg_ForumInteracao_envieUmaNovaInteracao", Uteis.ALERTA);
						return;
					}
					if ((getUsuarioLogado().getVisaoLogar().equalsIgnoreCase("professor"))) {
						try {
							VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
							visaoProfessorControle.setQtdeAtualizacaoForum(null);
							limparMensagem();
						} catch (Exception e) {
							setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
							e.printStackTrace();
						}
					}
				}

			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void editarListaExercicio() {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
			try {
				setMostarGabarito(false);
				getConteudoUnidadePaginaRecursoEducacional().setListaExercicio(getFacadeFactory().getListaExercicioFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getCodigo()));
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getQuestaoListaExercicioVOs().clear();
				if(getEmularAcessoAluno()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setConteudo(getConteudo());
					getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicioParaRespostaAluno(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), getMatriculaPeriodoTurmaDisciplinaVO(), getUnidadeConteudoVO().getCodigo(), getUnidadeConteudoVO().getTemaAssuntoVO().getCodigo(), getUsuarioLogado());	
				} else {
					getFacadeFactory().getQuestaoListaExercicioFacade().consultarPorListaExercicioParaRespostaAluno(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(), getUnidadeConteudoVO().getCodigo(), getUnidadeConteudoVO().getTemaAssuntoVO().getCodigo(), getUsuarioLogado());					
				}
				setSucesso(false);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}
	
	public void editarAvaliacaoOnline() throws Exception {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoAvaliacaoOnline() && !getEmularAcessoAluno()) {
			carregarAvaliacaoOnlineRea();
		}else if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoAvaliacaoOnline() && getEmularAcessoAluno()) {
			if(getVisaoAlunoControle() != null) {
				inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno();
				realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(false);				
			}else {
				CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
				calendarioAtividadeMatriculaVO.setSituacaoAtividade(SituacaoAtividadeEnum.NAO_CONCLUIDA);
				calendarioAtividadeMatriculaVO.setDataInicio(new Date());
				calendarioAtividadeMatriculaVO.setDataFim(Uteis.getDataAdicionadaEmHoras(new Date(), getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTempoLimiteRealizacaoAvaliacaoOnline()));
				calendarioAtividadeMatriculaVO.setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
				calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setDataInicioLiberacao(new Date());
				calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO);
				calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setDataFimLiberacao(Uteis.getDataAdicionadaEmHoras(new Date(), getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTempoLimiteRealizacaoAvaliacaoOnline()));
				getCalendarioAtividadeMatriculaVOs().clear();
				getCalendarioAtividadeMatriculaVOs().add(calendarioAtividadeMatriculaVO);
				
			}
			
		}
		
	}
	
	public void carregarAvaliacaoOnlineRea() throws Exception {
		setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarUltimaAvalicaoOnlinePorAvaliacaoOnlinePorMatriculaPeriodoTurmaDisciplina(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO())){
			getConteudoUnidadePaginaRecursoEducacional().setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getAvaliacaoOnlineMatriculaVO().setAvaliacaoOnlineVO(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO());			
			getAvaliacaoOnlineMatriculaVO().setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo()));
			if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO())){
				throw new Exception("Não foi encontrado a configuração ead para o cadastro da turma por favor verificar.");
			}
			getAvaliacaoOnlineMatriculaVO().getMatriculaVO().setMatricula(getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
			getAvaliacaoOnlineMatriculaVO().setMatriculaPeriodoTurmaDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVO());
			getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO);
			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlineRea(getMatriculaPeriodoTurmaDisciplinaVO(), getConfiguracaoEADVO(), getConteudoUnidadePaginaRecursoEducacional(), getUsuarioLogado());
			for(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO : calendarioAtividadeMatriculaVOs) {
				
				if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO) || calendarioAtividadeMatriculaVO.getDataInicio() == null || calendarioAtividadeMatriculaVO.getDataFim() == null) {
					getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_DATA_LIBERACAO);
				}else {
					getAvaliacaoOnlineMatriculaVO().setDataInicioLiberacao(calendarioAtividadeMatriculaVO.getDataInicio());
					getAvaliacaoOnlineMatriculaVO().setDataFimLiberacao(calendarioAtividadeMatriculaVO.getDataFim());
				}
				if (getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO) && UteisData.getDateHoraFinalDia(UteisData.getDataJDBC(getAvaliacaoOnlineMatriculaVO().getDataFimLiberacao())).compareTo(new Date()) < 0) {
					getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EXPIRADO);	
				}
				calendarioAtividadeMatriculaVO.setAvaliacaoOnlineMatriculaVO(getAvaliacaoOnlineMatriculaVO());
				
				
			}
			setCalendarioAtividadeMatriculaVOs(calendarioAtividadeMatriculaVOs);
			
		}else { 		
			
			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlineRea(getMatriculaPeriodoTurmaDisciplinaVO(), getConfiguracaoEADVO(), getConteudoUnidadePaginaRecursoEducacional(), getUsuarioLogado());
			for(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO : calendarioAtividadeMatriculaVOs) {				  
				if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO())) {
					
					getConteudoUnidadePaginaRecursoEducacional().setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setAvaliacaoOnlineVO(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO());			
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo()));
					if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO())){
						throw new Exception("Não foi encontrado a configuração ead para o cadastro da turma por favor verificar.");
					}
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getMatriculaVO().setMatricula(getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setMatriculaPeriodoTurmaDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVO());
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO);
					
					setAvaliacaoOnlineMatriculaVO(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO());  
				}
				if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO) || calendarioAtividadeMatriculaVO.getDataInicio() == null || calendarioAtividadeMatriculaVO.getDataFim() == null) {
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_DATA_LIBERACAO);
				}else {
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setDataInicioLiberacao(calendarioAtividadeMatriculaVO.getDataInicio());
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setDataFimLiberacao(calendarioAtividadeMatriculaVO.getDataFim());
				}
				if (calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO) && UteisData.getDateHoraFinalDia(UteisData.getDataJDBC(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getDataFimLiberacao())).compareTo(new Date()) < 0) {
					calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EXPIRADO);	
				}
				
				
			}			
			setCalendarioAtividadeMatriculaVOs(calendarioAtividadeMatriculaVOs);
		
		}
	}
	
	public void editarAvaliacaoPBL() throws Exception {

		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL) && !getEmularAcessoAluno()) {
			if (getEmularAcessoProfessor()) {
				inicializarAvaliacaoPBLProfessor();
			} else {
				inicializarAvaliacaoPBLAluno();
			}
			verificarRegrasApresentacaoConteudoUnidadePaginaRecursoEducacional();
			montarListaNotaConceitoAvaliacaoPbl();
		}
		if(getVisaoAlunoControle() == null && getEmulandoAcessoPagina()) {
			int x = 1;
			getListaAvaliados().clear();
			while(x<=10) {
				GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().setNome("Colega Simulado "+x);
				getListaAvaliados().add(gestaoEventoConteudoTurmaAvaliacaoPBLVO);
				x++;
			}
		}
		if(getVisaoProfessorControle() == null && getEmulandoAcessoPagina()) {
			int x = 1;
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().clear();
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getListaAvaliacaoDaAvaliacaoPBLVOs().clear();
			while(x<=10) {
				GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
				GestaoEventoConteudoTurmaAvaliacaoPBLVO prof = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().setNome("Colega Simulado "+x);
				prof.getAvaliador().setNome("Professor Simulado");
				prof.getAvaliado().setNome("Aluno Simulado "+x);
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.setGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(prof);
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliador().setNome("Aluno Simulado "+x);
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliador().setCodigo(x);
				gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().setCodigo(x);
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(gestaoEventoConteudoTurmaAvaliacaoPBLVO);
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getListaAvaliacaoDaAvaliacaoPBLVOs().add(gestaoEventoConteudoTurmaAvaliacaoPBLVO.clone());
				x++;
			}
		}
	}

	public void editarAtaPBL() throws Exception {
		if (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().isTipoRecursoAtaPbl() && !getEmularAcessoAluno()) {
			getConteudoUnidadePaginaRecursoEducacional().setGestaoEventoConteudoTurmaVO(getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarRapidaGestaoEventoConteudo(getConteudoUnidadePaginaRecursoEducacional().getCodigo(), getConteudo().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO())) {
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setTurmaVO(getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setAno(getMatriculaPeriodoTurmaDisciplinaVO().getAno());
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina());
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setConteudoVO(getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().inicializarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), getUsuarioLogado());
			}
			setUsuarioRedatorAta(getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().consultarSeUsuarioResponsavelFuncaoAta(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getCodigo(), FuncaoResponsavelAtaEnum.REDATOR, getUsuarioLogado()));
			verificarRegrasApresentacaoConteudoUnidadePaginaRecursoEducacional();
			montarListaSelectItemPessoa();

		}
	}

	public ForumInteracaoVO getForumInteracao() {
		if (forumInteracao == null) {
			forumInteracao = new ForumInteracaoVO();
		}
		return forumInteracao;
	}

	public void setForumInteracao(ForumInteracaoVO forumInteracao) {
		this.forumInteracao = forumInteracao;
	}
	
	public void carregarEdicaoRecursoEducacionalDeAcordoComTipo() throws Exception {
		if(getVisaoAlunoControle() == null && getVisaoProfessorControle() == null && getEmularAcessoAluno()) {
			setEmulandoAcessoPagina(true);
		}
		editarForum();
		editarListaExercicio();
		editarAtaPBL();
		editarAvaliacaoPBL();
		editarAvaliacaoOnline();
		carregarGraficoConteudoUnidadePaginaRecursoEducacional();
		setModalMensagemAvisoAluno("");
		setApresentarModalMensagemAvisoAluno(false);
	}

	public Boolean getExisteProximaPagina() {
		return getUnidadeConteudoVO().getPaginas() != 0 && getConteudoUnidadePagina().getPagina() <= getUnidadeConteudoVO().getPaginas() || getUnidadeConteudoVO().getOrdem() < getConteudo().getUnidadeConteudoVOs().size();
	}

	public Boolean getExistePaginaAnterior() {
		return getConteudoUnidadePagina().getPagina() > 1 || getUnidadeConteudoVO().getOrdem() > 1;
	}

	public Boolean getIsUltimaPagina() {
		return getConteudoUnidadePagina().getPagina().equals(getUnidadeConteudoVO().getPaginas()) || getUnidadeConteudoVO().getPaginas().equals(0);
	}

	public Boolean getExisteProximoGatilho() {
		if (getConteudoUnidadePaginaRecursoEducacional().isNovoObj()) {
			return false;
		}
		if (getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional() != null && getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.ANTES)) {
			return getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() < getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().size();
		}
		return getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() < getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().size();
	}

	public Boolean getExisteGatilhoAnterior() {
		if (getConteudoUnidadePaginaRecursoEducacional().isNovoObj()) {
			return false;
		}
		if (getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.ANTES)) {
			return getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() > 1;
		}
		return getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() > 1;
	}

	public Boolean apresentarOpcaoFecharGatilho;

	public Boolean getApresentarOpcaoFecharGatilho() {
		if (apresentarOpcaoFecharGatilho == null) {
			apresentarOpcaoFecharGatilho = false;
		}
		return apresentarOpcaoFecharGatilho;
	}

	public void setApresentarOpcaoFecharGatilho(Boolean apresentarOpcaoFecharGatilho) {
		this.apresentarOpcaoFecharGatilho = apresentarOpcaoFecharGatilho;
	}

	public void validarSeConteudoUnidadePaginaPodeAcancarProximoGatilho() throws Exception {
		if (!getEmularAcessoProfessor() && !getEmularAcessoAluno()) {
			try {
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().validarRegrasParaGatilhoConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional(), getAvaliacaoOnlineMatriculaVO(), getAutoAvaliacaoPbl(), getListaAvaliados(),  getUsuarioLogado());
			}catch (Exception e) {	
				setApresentarOpcaoFecharGatilho(true);
				throw e;
			}
		}
	}

	public void fecharModalRecursoEducacional() {
		if(getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR)
				&& Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao())) {
			setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao());
			setConteudoUnidadePaginaRecursoEducacionalAntesAlteracao(new ConteudoUnidadePaginaRecursoEducacionalVO());
			
			return;
		}
		setApresentarModalMensagemAvisoAluno(false);
		setApresentarGatilho(false);
		setApresentarOpcaoFecharGatilho(false);
		setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());		
	}

	public void visualizarIndiceConteudo() {
		try {
			if(!getConteudo().getMaximixado()) {
				setApresentarModalMensagemAvisoAluno(false);
				setApresentarResumoFinalConteudo(false);
				getFacadeFactory().getConteudoFacade().realizarGeracaoIndiceConteudo(getConteudo(), 0, getMatriculaAluno(), getUsuarioLogado());
				limparMensagem();
				setIconeMensagem("");
				setMensagemID("", "");
				setMensagemDetalhada("", "", "");
				setSucesso(false);
				getConteudo().setMaximixado(true);
			}else {
				getConteudo().setMaximixado(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}

	public void editarIndiceUnidadeConteudo() {
		try {			
			verificarAlunoPodeAvancarConteudoREAPendente();
			setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidadeItens"));
			if (!getUnidadeConteudoVO().getConteudoUnidadePaginaVOs().isEmpty()) {
				setConteudoUnidadePagina(getUnidadeConteudoVO().getConteudoUnidadePaginaVOs().get(0));
				getConteudoUnidadePagina().setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(getConteudoUnidadePagina().getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.ANTES, NivelMontarDados.TODOS, false, getUsuarioLogado()));
				getConteudoUnidadePagina().setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(getConteudoUnidadePagina().getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.DEPOIS, NivelMontarDados.TODOS, false, getUsuarioLogado()));
			} else {
				setConteudoUnidadePagina(new ConteudoUnidadePaginaVO());
				throw new Exception("Não foi encontrada nenhuma página a ser estudada nesta unidade, aguarde a conclusão da construção do conteúdo ou entre em contato com o Departamento Acadêmico/EAD.");
			}
			inicializarAcesso();
			incluirConteudoRegistroAcesso();
			visualizarGraficoConteudoUnidadePagina();
			if (getTotalAcessoPagina() == 0) {
				inicializarApresentacaoGatilhoAnterior();
			} else {
				setApresentarGatilho(false);
			}
			if(getAppMovel()) {
				getConteudo().setMaximixado(false);
				setMaximizarTela(true);
			}
			
			limparMensagem();
			setIconeMensagem("");
			setMensagemID("");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
		if ((context().getExternalContext().getSessionMap().get("booleanoReverConteudo") != null) && ((Boolean) context().getExternalContext().getSessionMap().get("booleanoReverConteudo"))) {
			context().getExternalContext().getSessionMap().remove("booleanoReverConteudo");
		
		}
	}

	public void editarIndiceConteudoUnidadePagina() {		
		 editarIndiceConteudoUnidadePagina((ConteudoUnidadePaginaVO) context().getExternalContext().getRequestMap().get("pagina"));		
	}
	
	public void editarIndiceConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) {
		try {
			
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
				setMatriculaAluno(visaoAlunoControle.getMatricula().getMatricula());
				setMatriculaPeriodoTurmaDisciplinaVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO());
			}
			setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			setUnidadeConteudoVO(getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo())).findFirst().get());
//			setUnidadeConteudoVO(getFacadeFactory().getUnidadeConteudoFacade().consultarPorChavePrimaria(conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado()));
//			if(!getConteudo().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
//				setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getUnidadeConteudoVO().getConteudo().getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
//				getConteudo().setNivelMontarDados(NivelMontarDados.TODOS);
//			}
			if(!conteudoUnidadePaginaVO.getPaginaJaVisualizada()) {
				getConteudoUnidadePagina().setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(getConteudoUnidadePagina().getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.ANTES, NivelMontarDados.TODOS, false, getUsuarioLogado()));
				getConteudoUnidadePagina().setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(getConteudoUnidadePagina().getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.DEPOIS, NivelMontarDados.TODOS, false, getUsuarioLogado()));
			}
			verificarAlunoPodeAvancarConteudoREAPendente();
			inicializarAcesso();
			incluirConteudoRegistroAcesso();
			visualizarGraficoConteudoUnidadePagina();
			consultarPercentualEstudado();
			if (getTotalAcessoPagina() == 0) {
				inicializarApresentacaoGatilhoAnterior();
			} else {
				setApresentarGatilho(false);
			}
			if(getAppMovel()) {
				getConteudo().setMaximixado(false);
				setMaximizarTela(true);
			}
			limparMensagem();
			setIconeMensagem("");
			setMensagemID("");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
		if ((context().getExternalContext().getSessionMap().get("booleanoReverConteudo") != null) && ((Boolean) context().getExternalContext().getSessionMap().get("booleanoReverConteudo"))) {
			context().getExternalContext().getSessionMap().remove("booleanoReverConteudo");
			
		} 
	}

	public void inicializarUltimoAcessoAlunoConteudo() {
		try {
			ConteudoRegistroAcessoVO conteudoRegistroAcessoVO = null;
			if (!getEmularAcessoAluno() && !getEmularAcessoProfessor()) {
				conteudoRegistroAcessoVO = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarConteudoUltimoRegistroAcessoPorMatriculaConteudo(getMatriculaAluno(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getConteudo().getCodigo());
			} else {
				conteudoRegistroAcessoVO = new ConteudoRegistroAcessoVO();
			}

			if (conteudoRegistroAcessoVO.isNovoObj()) {
				setUnidadeConteudoVO(getFacadeFactory().getUnidadeConteudoFacade().consultarUnidadeConteudoPorConteudoEOrdem(getConteudo().getCodigo(), 1, NivelMontarDados.BASICO, false, getUsuarioLogado()));
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), 1, NivelMontarDados.TODOS, false, getUsuarioLogado()));
			} else {
				setDataUltimoAcesso(conteudoRegistroAcessoVO.getDataAcesso());
				setUnidadeConteudoVO(getFacadeFactory().getUnidadeConteudoFacade().consultarPorChavePrimaria(conteudoRegistroAcessoVO.getUnidadeConteudo(), NivelMontarDados.BASICO, false, getUsuarioLogado()));
				setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorChavePrimaria(conteudoRegistroAcessoVO.getConteudoUnidadePagina(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				setApresentarOpcaoFecharGatilho(true);
			}
			inicializarAcesso();
			incluirConteudoRegistroAcesso();
			visualizarGraficoConteudoUnidadePagina();
			consultarPercentualEstudado();
			if (getTotalAcessoPagina() == 0) {
				inicializarApresentacaoGatilhoAnterior();
			} else {
				setApresentarGatilho(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void incluirConteudoRegistroAcesso() throws Exception {
		if (!getEmularAcessoAluno() && !getEmularAcessoProfessor() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
			getFacadeFactory().getConteudoRegistroAcessoFacade().incluir(getMatriculaAluno(), getConteudo().getCodigo(), getUnidadeConteudoVO().getCodigo(), getConteudoUnidadePagina().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());			
		}
		
		if(Uteis.isAtributoPreenchido(getUnidadeConteudoVO()) && Uteis.isAtributoPreenchido(getConteudoUnidadePagina()) && !getConteudoUnidadePagina().getPaginaJaVisualizada()) {
			getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())).findFirst().get().getConteudoUnidadePaginaVOs().stream().filter(t -> t.getCodigo().equals(getConteudoUnidadePagina().getCodigo())).findFirst().get().setPaginaJaVisualizada(true);
			getConteudoUnidadePagina().setPaginaJaVisualizada(true);
		}
		getConteudo().getUnidadeConteudoVOs().stream().forEach(t -> {
			if(!t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())) {
				t.setMaximixado(false);
			}else if(t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())) {
				t.setMaximixado(true);
			}
		});
		

	}

	public void visualizarGraficoConteudoUnidadePagina() {
		try {
			if (getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				setVisualizarGraficoConteudoUnidadePagina(false);
				if (!getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getIsApresentarVisaoPais() && !getEmularAcessoAluno() && !getEmularAcessoProfessor()) {
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoGrafico(getConteudoUnidadePagina());
				}
				context().getExternalContext().getSessionMap().put("tipoGrafico", getConteudoUnidadePagina().getTipoGrafico().getTipoGraficoApresentar());
				context().getExternalContext().getSessionMap().put("tituloGrafico", getConteudoUnidadePagina().getTituloGrafico());
				context().getExternalContext().getSessionMap().put("categoriaGrafico", getConteudoUnidadePagina().getCategoriaGrafico());
				context().getExternalContext().getSessionMap().put("tituloXGrafico", getConteudoUnidadePagina().getTituloEixoX());
				context().getExternalContext().getSessionMap().put("tituloYGrafico", getConteudoUnidadePagina().getTituloEixoY());
				context().getExternalContext().getSessionMap().put("valorGrafico", getConteudoUnidadePagina().getValorGrafico());
				context().getExternalContext().getSessionMap().put("apresentarLegenda", getConteudoUnidadePagina().getApresentarLegenda());
				if(getConteudoUnidadePagina().getTipoGrafico().getPermiteTituloXY() && !getConteudoUnidadePagina().getConteudoUnidadePaginaGraficoCategoriaVOs().isEmpty()
						&& !getConteudoUnidadePagina().getConteudoUnidadePaginaGraficoSerieVOs().isEmpty()) {
					setVisualizarGraficoConteudoUnidadePagina(true);
				}else if(!getConteudoUnidadePagina().getTipoGrafico().getPermiteTituloXY() && !getConteudoUnidadePagina().getConteudoUnidadePaginaGraficoVOs().isEmpty()) {
					setVisualizarGraficoConteudoUnidadePagina(true);
				}
				setVisualizarGraficoConteudoUnidadePagina(true);

			}
		} catch (Exception e) {
			setVisualizarGraficoConteudoUnidadePaginaRecursoEducacional(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarAcesso() {
		try {
			if (!getEmularAcessoAluno() && !getEmularAcessoProfessor()) {
				setTotalAcessoConteudo(getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalAcessoAlunoRealizouConteudo(getMatriculaAluno(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getConteudo().getCodigo()));
				setTotalAcessoPagina(getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalAcessoAlunoRealizouPagina(getMatriculaAluno(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getConteudo().getCodigo(), getConteudoUnidadePagina().getCodigo()));
				Map<String, Object> auxiliar = new HashMap<String, Object>();
				getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina(), getUsuarioLogado());
				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(), null, Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado")),  getConfiguracaoEADVO(), getUsuarioLogado());
				inicializarPontuacao();
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}

	public void inicializarPontuacao() {
		try {
			Double percentualPontoAtingido = null;
			setTotalPontoAtingido(getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalPontosAlunoAtingiuConteudo(getMatriculaAluno(), getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getConteudo().getCodigo()));
			if (getConteudo().getPontoTotal() > 0) {				
				setPorcentagemEvolucaoConteudo(Uteis.getDoubleFormatado(percentualPontoAtingido)+ "%");
				percentualPontoAtingido = Uteis.arrendondarForcando2CadasDecimais((getTotalPontoAtingido() * 100) / getConteudo().getPontoTotal());
				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(),percentualPontoAtingido, null,  getConfiguracaoEADVO(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void visualizarTelaApoioProfessor() {
		try {
			if(isApresentarConteudoUnidadePaginaRecursoEducacionalProfessor()){
				setApresentarConteudoUnidadePaginaRecursoEducacionalProfessor(false);	
			}else{
				setApresentarConteudoUnidadePaginaRecursoEducacionalProfessor(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void fecharModalRecursoEducacionalApoioProfessor() {
		if(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao()) && getApresentarGatilho()){
			setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao());
			setModalMensagemAvisoAluno("RichFaces.$('panelAddRecursoEducacionalApoioProfessor').hide(); RichFaces.$('panelAddRecursoEducacional').show();");
		}else{
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			setModalMensagemAvisoAluno("RichFaces.$('panelAddRecursoEducacionalApoioProfessor').hide(); RichFaces.$('panelAddRecursoEducacional').hide();");
		}
		setConteudoUnidadePaginaRecursoEducacionalAntesAlteracao(new ConteudoUnidadePaginaRecursoEducacionalVO());
	}
	
	public void carregarConteudoUnidadePaginaRecursoEducacionalProfessor() {
		try {
			if(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional()) && !Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao())){
				setConteudoUnidadePaginaRecursoEducacionalAntesAlteracao(getConteudoUnidadePaginaRecursoEducacional());
			}
			setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
			setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("recursoApoioProfessor"));
			setBackgroundRecursoEducacional(montarBackgroundGatilho());
			carregarEdicaoRecursoEducacionalDeAcordoComTipo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarApresentacaoGatilhoAnterior() throws Exception {
		setApresentarOpcaoFecharGatilho(false);
		setApresentarModalMensagemAvisoAluno(false);
		setModalMensagemAvisoAluno("");
		setApresentarGatilho(!getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().isEmpty());
		if (getApresentarGatilho()) {
			setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().get(0));
			getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
			carregarEdicaoRecursoEducacionalDeAcordoComTipo();
		}
	}

	public void inicializarApresentacaoGatilhoPosterior() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			setApresentarGatilho(!getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().isEmpty() && getTotalAcessoPagina() <= 1);
			if (getApresentarGatilho()) {
				setApresentarOpcaoFecharGatilho(false);
				setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().get(0));
				carregarEdicaoRecursoEducacionalDeAcordoComTipo();
			} else {
				verificarAlunoPodeAvancarConteudoREAPendente();
				setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
				ConteudoRegistroAcessoVO conteudoRegistroAcessoVO = new ConteudoRegistroAcessoVO();
				conteudoRegistroAcessoVO = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarPorMatriculaConteudoUnidadePagina(getMatriculaAluno(), getConteudoUnidadePagina().getCodigo());
				if (Uteis.isAtributoPreenchido(conteudoRegistroAcessoVO.getDataAcesso()) && UteisData.diferencaEmMinutosEntreDatas(conteudoRegistroAcessoVO.getDataAcesso(), new Date()) < Double.valueOf(getConteudoUnidadePagina().getTempo())) {
					throw new Exception("O tempo mínimo de acesso a essa página é de " + getConteudoUnidadePagina().getTempo() + " minuto(s). Tempo atual: " + Uteis.arredondar(UteisData.diferencaEmMinutosEntreDatas(conteudoRegistroAcessoVO.getDataAcesso(), new Date()), 1, 0));
				}
				visualizarProximoPagina();
			}
		} catch (ConsistirException e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarProximoPagina() {
		try {
			if (getExisteProximaPagina()) {
				if (getIsUltimaPagina()) {
					if (getUnidadeConteudoVO().getOrdem() == getConteudo().getUnidadeConteudoVOs().size()) {
						setApresentarResumoFinalConteudo(true);
						limparMensagem();
						setIconeMensagem("");
						setMensagemID("");
						return;
					} else {
						setUnidadeConteudoVO(getConteudo().getUnidadeConteudoVOs().get(getUnidadeConteudoVO().getOrdem()));
						setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), 1, NivelMontarDados.TODOS, false, getUsuarioLogado()));
					}
				} else {
					setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getConteudoUnidadePagina().getPagina() + 1, NivelMontarDados.TODOS, false, getUsuarioLogado()));
				}
				inicializarAcesso();
				incluirConteudoRegistroAcesso();
				visualizarGraficoConteudoUnidadePagina();
				consultarPercentualEstudado();
				if (getTotalAcessoPagina() == 0) {
					inicializarApresentacaoGatilhoAnterior();
				} else {
					setApresentarGatilho(false);
				}

			} else {
				setApresentarResumoFinalConteudo(true);
			}
			verificarAnotacao();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarPaginaAnterior() {
		try {
			setApresentarResumoFinalConteudo(false);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			if (getExistePaginaAnterior()) {
				if (getConteudoUnidadePagina().getPagina() > 1) {
					setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getConteudoUnidadePagina().getPagina() - 1, NivelMontarDados.TODOS, false, getUsuarioLogado()));
				} else {
					setUnidadeConteudoVO(getConteudo().getUnidadeConteudoVOs().get(getUnidadeConteudoVO().getOrdem() - 2));
					setConteudoUnidadePagina(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPagina(getUnidadeConteudoVO().getCodigo(), getUnidadeConteudoVO().getPaginas(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				}
				visualizarGraficoConteudoUnidadePagina();
				inicializarAcesso();
				incluirConteudoRegistroAcesso();
				setApresentarGatilho(false);
			}
			verificarAnotacao();
			limparMensagem();
			setIconeMensagem("");
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarGatilho() {
		try {
			setConteudoUnidadePaginaRecursoEducacional((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getRequestMap().get("gatilhoItens"));
			setModalPanelAddRecursoEducacional("");
			setApresentarGatilho(false);
			setBackgroundRecursoEducacional(montarBackgroundGatilho());
			carregarEdicaoRecursoEducacionalDeAcordoComTipo();
			setApresentarGatilho(true);
			setApresentarOpcaoFecharGatilho(true);
			setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').show();");
			inicializarMensagemVazia();
		} catch (ConsistirException e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').hide();");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarProximoGatilho() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			validarSeConteudoUnidadePaginaPodeAcancarProximoGatilho();
			if (getExisteProximoGatilho()) {
				if (getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.ANTES)) {
					setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().get(getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao()));
				} else {
					setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().get(getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao()));
				}
				carregarEdicaoRecursoEducacionalDeAcordoComTipo();				
				setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').show();");

			} else {
				setApresentarOpcaoFecharGatilho(false);
				setApresentarGatilho(false);
				if (getConteudoUnidadePaginaRecursoEducacional().isNovoObj() || getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.DEPOIS)) {
					setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
					visualizarProximoPagina();
				} else {
					setConteudoUnidadePaginaRecursoEducacional(new ConteudoUnidadePaginaRecursoEducacionalVO());
				}
			}
			inicializarMensagemVazia();
		} catch (ConsistirException e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarGatilhoAnterior() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			if (getConteudoUnidadePaginaRecursoEducacional().getMomentoApresentacaoRecursoEducacional().equals(MomentoApresentacaoRecursoEducacionalEnum.ANTES)) {
				setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().get(getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() - 2));
			} else {
				setConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePagina().getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().get(getConteudoUnidadePaginaRecursoEducacional().getOrdemApresentacao() - 2));
			}
			carregarEdicaoRecursoEducacionalDeAcordoComTipo();
			inicializarMensagemVazia();
		} catch (ConsistirException e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').hide()");
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setApresentarGatilho(false);
			setModalPanelAddRecursoEducacional("RichFaces.$('panelAddRecursoEducacional').hide()");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosVisaoAlunoProfessor() {
		try {
			setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo()));
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
				setMatriculaAluno(visaoAlunoControle.getMatricula().getMatricula());
				if (visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo() > 0) {
					setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
				} else {
					setConteudo(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().registrarVinculoMatriculaPeriodoTurmaDisciplinaComConteudo(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getUsuarioLogado()));
					visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO().setConteudo(getConteudo());
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirConteudoMatriculaPeriodoTurmaDisciplina(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO());
				}
				setMatriculaPeriodoTurmaDisciplinaVO(visaoAlunoControle.getMatriculaPeriodoTurmaDisciplinaVO());
				inicializarUltimoAcessoAlunoConteudo();
				setNaoExisteConteudoCadastrado(getConteudo().isNovoObj());
				getConteudo().getUnidadeConteudoVOs().stream().forEach(t -> {
					if(!t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())) {
						t.setMaximixado(false);
					}
				});
				
				
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				getListaSelectItemDisciplina().clear();
				List<DisciplinaVO> listaConsultas = null;
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado());
				for (DisciplinaVO obj : listaConsultas) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				if (!listaConsultas.isEmpty()) {
					getConteudo().getDisciplina().setCodigo(listaConsultas.get(0).getCodigo());
				}
			}
			getConteudo().setMaximixado(false);
			visualizarIndiceConteudo();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getApresentarResumoFinalConteudo() {
		if (apresentarResumoFinalConteudo == null) {
			apresentarResumoFinalConteudo = false;
		}
		return apresentarResumoFinalConteudo;
	}

	public void setApresentarResumoFinalConteudo(Boolean apresentarResumoFinalConteudo) {
		this.apresentarResumoFinalConteudo = apresentarResumoFinalConteudo;
	}
	

	public Date getDataUltimoAcesso() {
		if (dataUltimoAcesso == null) {
			dataUltimoAcesso = new Date();
		}
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public Integer getTotalAcessoConteudo() {
		if (totalAcessoConteudo == null) {
			totalAcessoConteudo = 0;
		}
		return totalAcessoConteudo;
	}

	public void setTotalAcessoConteudo(Integer totalAcessoConteudo) {
		this.totalAcessoConteudo = totalAcessoConteudo;
	}

	public Integer getTotalAcessoPagina() {
		if (totalAcessoPagina == null) {
			totalAcessoPagina = 0;
		}
		return totalAcessoPagina;
	}

	public void setTotalAcessoPagina(Integer totalAcessoPagina) {
		this.totalAcessoPagina = totalAcessoPagina;
	}

	public Double getTotalPontoAtingido() {
		if (totalPontoAtingido == null) {
			totalPontoAtingido = 0.0;
		}
		return totalPontoAtingido;
	}

	public void setTotalPontoAtingido(Double totalPontoAtingido) {
		this.totalPontoAtingido = totalPontoAtingido;
	}

	public String getAcaoModalRecursoEducacional() {
		if (getApresentarGatilho()) {
			return "RichFaces.$('panelAddRecursoEducacional').show();";
		} else if (getApresentarResumoFinalConteudo()) {
			return "RichFaces.$('panelAddRecursoEducacional').hide(); RichFaces.$('panelTerminoConteudo').show();";
		}
		return "RichFaces.$('panelAddRecursoEducacional').hide(); RichFaces.$('panelTerminoConteudo').hide();";
	}

	public List<ListaExercicioVO> getListaExercicioVOs() {
		if (listaExercicioVOs == null) {
			listaExercicioVOs = new ArrayList<ListaExercicioVO>(0);
		}
		return listaExercicioVOs;
	}

	public void setListaExercicioVOs(List<ListaExercicioVO> listaExercicioVOs) {
		this.listaExercicioVOs = listaExercicioVOs;
	}

	public void realizarCorrecaoExercicio() {
		try {
			getFacadeFactory().getListaExercicioFacade().realizarGeracaoGabarito(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio());
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setMostarGabarito(true);
		} catch (ConsistirException e) {
			setMostarGabarito(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMostarGabarito(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getMostarGabarito() {
		if (mostarGabarito == null) {
			mostarGabarito = false;
		}
		return mostarGabarito;
	}

	public void setMostarGabarito(Boolean mostarGabarito) {
		this.mostarGabarito = mostarGabarito;
	}

	public void realizarVerificacaoQuestaoUnicaEscolha() {
		OpcaoRespostaQuestaoVO orq = (OpcaoRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
		if (orq.getMarcada()) {
			orq.setMarcada(false);
		} else {
			orq.setMarcada(true);
			getFacadeFactory().getListaExercicioFacade().realizarVerificacaoQuestaoUnicaEscolha(getConteudoUnidadePaginaRecursoEducacional().getListaExercicio(), orq);
		}
	}

	public String getPorcentagemEvolucaoConteudo() {
		if (porcentagemEvolucaoConteudo == null) {
			porcentagemEvolucaoConteudo = "0,00%";
		}
		return porcentagemEvolucaoConteudo;
	}

	public void setPorcentagemEvolucaoConteudo(String porcentagemEvolucaoConteudo) {
		this.porcentagemEvolucaoConteudo = porcentagemEvolucaoConteudo;
	}

	public Boolean getExisteRecursoEducacionalConteudo() {
		return (getConteudoUnidadePagina().getTipoRecursoEducacional() != null && getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && !getForumVOs().isEmpty()) || (getConteudoUnidadePagina().getTipoRecursoEducacional() != null && getConteudoUnidadePagina().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && !getListaExercicioVOs().isEmpty()) || (getTreeNodeCustomizado() != null && getTreeNodeCustomizado().getData() != null && getTreeNodeCustomizado().getChildrenKeysIterator().hasNext());
	}

	public Boolean getExisteRecursoEducacionalGatilho() {
		return (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional() != null && getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && !getForumVOs().isEmpty()) 
				|| (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional() != null && getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && !getListaExercicioVOs().isEmpty()) 
				|| (getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional() != null && !getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && !getConteudoUnidadePaginaRecursoEducacional().getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && getTreeNodeCustomizado() != null && getTreeNodeCustomizado().getData() != null && getTreeNodeCustomizado().getChildrenKeysIterator().hasNext());
	}

	public Boolean getPublicarImagem() {
		if (publicarImagem == null) {
			publicarImagem = true;
		}
		return publicarImagem;
	}

	public void setPublicarImagem(Boolean publicarImagem) {
		this.publicarImagem = publicarImagem;
	}

	public String getNomeImagem() {
		if (nomeImagem == null) {
			nomeImagem = "";
		}
		return nomeImagem;
	}

	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}

	public Boolean getEmularAcessoAluno() {
		if (emularAcessoAluno == null) {
			emularAcessoAluno = false;
		}
		return emularAcessoAluno;
	}

	public void setEmularAcessoAluno(Boolean emularAcessoAluno) {
		this.emularAcessoAluno = emularAcessoAluno;
	}

	public Boolean getEmularAcessoProfessor() {
		if (emularAcessoProfessor == null) {
			emularAcessoProfessor = false;
		}
		return emularAcessoProfessor;
	}

	public void setEmularAcessoProfessor(Boolean emularAcessoProfessor) {
		this.emularAcessoProfessor = emularAcessoProfessor;
	}

	public Boolean getEmulandoAcessoPagina() {
		if (emulandoAcessoPagina == null) {
			emulandoAcessoPagina = false;
		}
		return emulandoAcessoPagina;
	}

	public void setEmulandoAcessoPagina(Boolean emulandoAcessoPagina) {
		this.emulandoAcessoPagina = emulandoAcessoPagina;
	}

	public Integer getCodigoUnidadeConteudoEmular() {
		if (codigoUnidadeConteudoEmular == null) {
			codigoUnidadeConteudoEmular = 0;
		}
		return codigoUnidadeConteudoEmular;
	}

	public void setCodigoUnidadeConteudoEmular(Integer codigoUnidadeConteudoEmular) {
		this.codigoUnidadeConteudoEmular = codigoUnidadeConteudoEmular;
	}

	public Integer getPaginaEmular() {
		if (paginaEmular == null) {
			paginaEmular = 1;
		}
		return paginaEmular;
	}

	public void setPaginaEmular(Integer paginaEmular) {
		this.paginaEmular = paginaEmular;
	}

	public void recortarImagemPagina() {
		try {
			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getConteudoUnidadePagina().setNomeFisicoArquivo(getFacadeFactory().getArquivoHelper().recortarImagem(getConteudoUnidadePagina().getCaminhoBaseRepositorio(), getConteudoUnidadePagina().getNomeFisicoArquivo(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado()));
			getFacadeFactory().getConteudoUnidadePaginaFacade().persistir(getConteudoUnidadePagina(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
			getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void recortarImagemPaginaRE() {
		try {
			if (getLarguraVerso() == 0f && getAlturaVerso() == 0f && getXcropVerso() == 0f && getYcropVerso() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getConteudoUnidadePaginaRecursoEducacional().setNomeFisicoArquivo(getFacadeFactory().getArquivoHelper().recortarImagem(getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio(), getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo(), getConfiguracaoGeralPadraoSistema(), getLarguraVerso(), getAlturaVerso(), getXcropVerso(), getYcropVerso(), getUsuarioLogado()));
			if (getConteudoUnidadePaginaRecursoEducacional().getCodigo() > 0) {
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().persistir(getConteudoUnidadePaginaRecursoEducacional(), getConteudo(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado(), false);
			}
			getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void executarZoomInPagina() {
		String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePagina().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePagina().getNomeFisicoArquivo();
		String extensao = getConteudoUnidadePagina().getNomeFisicoArquivo().substring(getConteudoUnidadePagina().getNomeFisicoArquivo().lastIndexOf(".") + 1);
		getFacadeFactory().getArquivoHelper().executarZoomImagem("in", arquivo, extensao);
		getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
	}

	public void executarZoomOutPagina() {
		String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePagina().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePagina().getNomeFisicoArquivo();
		String extensao = getConteudoUnidadePagina().getNomeFisicoArquivo().substring(getConteudoUnidadePagina().getNomeFisicoArquivo().lastIndexOf(".") + 1);
		getFacadeFactory().getArquivoHelper().executarZoomImagem("out", arquivo, extensao);
		getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
	}

	public void executarZoomInPaginaRE() {
		String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo();
		String extensao = getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().substring(getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().lastIndexOf(".") + 1);
		getFacadeFactory().getArquivoHelper().executarZoomImagem("in", arquivo, extensao);
		getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
	}

	public void executarZoomOutPaginaRE() {
		String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo();
		String extensao = getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().substring(getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().lastIndexOf(".") + 1);
		getFacadeFactory().getArquivoHelper().executarZoomImagem("out", arquivo, extensao);
		getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
	}

	public void rotacionarPagina90GrausParaEsquerda() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePagina().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePagina().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePagina().getNomeFisicoArquivo().substring(getConteudoUnidadePagina().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(arquivo, extensao);
			getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionarPagina90GrausParaDireita() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePagina().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePagina().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePagina().getNomeFisicoArquivo().substring(getConteudoUnidadePagina().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(arquivo, extensao);
			getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionarPagina180Graus() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePagina().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePagina().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePagina().getNomeFisicoArquivo().substring(getConteudoUnidadePagina().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(arquivo, extensao);
			getConteudoUnidadePagina().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionarPaginaRE90GrausParaEsquerda() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().substring(getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(arquivo, extensao);
			getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionarPaginaRE90GrausParaDireita() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().substring(getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(arquivo, extensao);
			getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionarPaginaRE180Graus() {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseRepositorio() + File.separator + getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo();
			String extensao = getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().substring(getConteudoUnidadePaginaRecursoEducacional().getNomeFisicoArquivo().lastIndexOf(".") + 1);
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(arquivo, extensao);
			getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void carregarCorFundoBackgroundConteudo() {
		try {
			getConteudo().setNomeImagemBackground("");
			getConteudo().setCaminhoBaseBackground("");
			getConteudo().setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.UNIDADE);
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadImagemBackgroundConteudo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getConteudoFacade().uploadImagemBackgroundConteudo(getConteudo(), uploadEvent, getReplicarBackgroundTodoConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), false);
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarImagemBackgroundConteudo() {
		try {
			getFacadeFactory().getConteudoFacade().alterarBackground(getConteudo(), getReplicarBackgroundTodoConteudo(), getUsuarioLogado(), false);
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagemBackgroundConteudo() {
		try {
			getFacadeFactory().getConteudoFacade().removerImagemBackgroundConteudo(getConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Método responsável por Upar uma imagem para Background do Conteudo
	 * Unidade Página.
	 * 
	 * @param uploadEvent
	 */
	public void uploadImagemBackgroundConteudoUnidadePagina(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().uploadImagemBackgroundConteudoUnidadePagina(getConteudoUnidadePagina(), getConteudo().getDisciplina().getCodigo(), uploadEvent, getReplicarBackgroundTodoConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarImagemBackgroundConteudoUnidadePagina() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().alterarBackgroundEdicao(getConteudoUnidadePagina(), getReplicarBackgroundTodoConteudo());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagemBackgroundConteudoUnidadePagina() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaFacade().removerImagemBackgroundConteudoUnidadePagina(getConteudoUnidadePagina(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadImagemBackgroundUnidadeConteudo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getUnidadeConteudoFacade().uploadImagemBackgroundUnidadeConteudo(getUnidadeConteudoVO(), getConteudo().getDisciplina(), uploadEvent, getReplicarBackgroundTodoConteudo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), false);
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarImagemBackgroundUnidadeConteudo() {
		try {
			getFacadeFactory().getUnidadeConteudoFacade().alterarBackground(getUnidadeConteudoVO(), getConteudo().getDisciplina(), getReplicarBackgroundTodoConteudo(), getUsuarioLogado(), false);
			getBackgroundUnidadeConteudo();
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagemBackgroundUnidadeConteudo() {
		try {
			getFacadeFactory().getUnidadeConteudoFacade().removerImagemBackgroundUnidadeConteudo(getUnidadeConteudoVO(), getConteudo().getDisciplina(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), false);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void uploadImagemBackgroundConteudoUnidadePaginaRecursoEducacional(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().uploadImagemBackgroundConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional(), getConteudo().getDisciplina().getCodigo(), uploadEvent, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarImagemBackgroundConteudoUnidadePaginaRecursoEducacional() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacional().getCodigo() > 0) {
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().alterarBackground(getConteudoUnidadePaginaRecursoEducacional());
			}
			setMensagemID("msg_sucesso_upload", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerImagemBackgroundConteudoUnidadePaginaRecursoEducacional() {
		try {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().removerImagemBackgroundConteudoUnidadePaginaRecursoEducacional(getConteudoUnidadePaginaRecursoEducacional(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getReplicarBackgroundTodoConteudo() {
		if (replicarBackgroundTodoConteudo == null) {
			replicarBackgroundTodoConteudo = true;
		}
		return replicarBackgroundTodoConteudo;
	}

	public void setReplicarBackgroundTodoConteudo(Boolean replicarBackgroundTodoConteudo) {
		this.replicarBackgroundTodoConteudo = replicarBackgroundTodoConteudo;
	}

	public String getBackgroundPagina() {
		setImagemBackgroundPagina("");
		String width = getNomeTelaAtual().contains("conteudoAlunoForm.xhtml") && getConteudo().getMaximixado() && !getAppMovel() ? "width: 70.5% !important" : "width: 100% !important" ;
		if (getConteudoUnidadePagina().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && getConteudoUnidadePagina().getCorBackground().trim().isEmpty()) {
			return "background-color:#FFFFFF !important;"+width;
		} else if (!getConteudoUnidadePagina().getCaminhoBaseBackground().trim().isEmpty() && !getConteudoUnidadePagina().getNomeImagemBackground().trim().isEmpty()) {
			setImagemBackgroundPagina(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getConteudoUnidadePagina().getCaminhoBaseBackground().replaceAll("\\\\", "/") + "/" + getConteudoUnidadePagina().getNomeImagemBackground());
			StringBuilder back = new StringBuilder("background-image: url('" + getImagemBackgroundPagina() + "') !important");
			if (!getConteudoUnidadePagina().getCorBackground().trim().isEmpty()) {
				back.append("; background-color: ").append(getConteudoUnidadePagina().getCorBackground().contains("#")? "" : "#").append(getConteudoUnidadePagina().getCorBackground()).append(" !important");
			}
			if (getConteudoUnidadePagina().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO)) {
				back.append("; background-size:100% !important");
			}
			if (getConteudoUnidadePagina().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL)) {
				back.append("; background-position:center center !important; margin: 0 auto !important");
			}
			back.append(";  background-repeat:no-repeat !important;");
			return back.toString()+width;
		}
		if (!getConteudoUnidadePagina().getCorBackground().trim().isEmpty()) {
			return "background-image: none !important;  background-color:" + (getConteudoUnidadePagina().getCorBackground().contains("#") ? "" : "#") + getConteudoUnidadePagina().getCorBackground() + " !important;"+width;
		}
		return "background-color:#FFFFFF !important;"+width;
	}

	public String getBackgroundUnidadeConteudo() {
		setImagemBackgroundUnidadeConteudo("");
		if (getUnidadeConteudoVO().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && getUnidadeConteudoVO().getCorBackground().trim().isEmpty()) {
			return "background-color:#FFFFFF  !important;";
		} else if (!getUnidadeConteudoVO().getCaminhoBaseBackground().trim().isEmpty() && !getUnidadeConteudoVO().getNomeImagemBackground().trim().isEmpty()) {
			setImagemBackgroundUnidadeConteudo(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeConteudoVO().getCaminhoBaseBackground().replaceAll("\\\\", "/") + "/" + getUnidadeConteudoVO().getNomeImagemBackground());
			StringBuilder back = new StringBuilder("background-image: url('" + getImagemBackgroundUnidadeConteudo() + "')  !important");
			if (!getUnidadeConteudoVO().getCorBackground().trim().isEmpty()) {
				back.append("; background-color: ").append(getUnidadeConteudoVO().getCorBackground().contains("#")? "" : "#").append(getUnidadeConteudoVO().getCorBackground()).append("  !important");
			}
			if (getUnidadeConteudoVO().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO)) {
				back.append("; background-size:100%  !important");
			}
			if (getUnidadeConteudoVO().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL)) {
				back.append("; background-position:center center  !important; margin: 0 auto  !important");
			} 
			back.append(";  background-repeat:no-repeat  !important;");
			return back.toString();
		}
		if (!getUnidadeConteudoVO().getCorBackground().trim().isEmpty()) {
			return "background-image: none !important; background-color:" + (getUnidadeConteudoVO().getCorBackground().contains("#") ? "" : "#") + getUnidadeConteudoVO().getCorBackground() + "  !important;";
		}
		return "background-color:#FFFFFF  !important;";
	}

	public String getBackgroundConteudo() {
		setImagemBackgroundConteudo("");
		if (getConteudo().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && getConteudo().getCorBackground().trim().isEmpty()) {
			return "background-color:#FFFFFF  !important;";
		} else if (!getConteudo().getCaminhoBaseBackground().trim().isEmpty() && !getConteudo().getNomeImagemBackground().trim().isEmpty()) {
			setImagemBackgroundConteudo(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getConteudo().getCaminhoBaseBackground().replaceAll("\\\\", "/") + "/" + getConteudo().getNomeImagemBackground());
			StringBuilder back = new StringBuilder("background-image: url('" + getImagemBackgroundConteudo() + "')  !important");
			if (!getConteudo().getCorBackground().trim().isEmpty()) {
				back.append("; background-color: ").append(getConteudo().getCorBackground().contains("#")? "" : "#").append(getConteudo().getCorBackground()).append("  !important");
			}
			if (getConteudo().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO)) {
				back.append("; background-size:100%  !important");
			}
			if (getConteudo().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL)) {
				back.append("; background-position:center center  !important; margin: 0 auto  !important");
			}
			back.append(";  background-repeat:no-repeat  !important;");
			return back.toString();
		}
		if (!getConteudo().getCorBackground().trim().isEmpty()) {
			return "background-image: none !important; background-color:"  + (getConteudo().getCorBackground().contains("#") ? "" : "#") + getConteudo().getCorBackground() + "  !important;";
		}
		return "background-color:#FFFFFF  !important;";
	}

	public String getBackgroundRecursoEducacional() {
		backgroundRecursoEducacional = null;
		backgroundRecursoEducacional = montarBackgroundGatilho();
		return backgroundRecursoEducacional;
	}

	public List<SelectItem> getListaSelectItemTamanhoImagemBackground() {
		return TamanhoImagemBackgroundConteudoEnum.getListaSelectItemTamanhoImagemBackgroundConteudo();
	}

	public AnotacaoDisciplinaVO getAnotacaoDisciplinaVO() {
		if (anotacaoDisciplinaVO == null) {
			anotacaoDisciplinaVO = new AnotacaoDisciplinaVO();
		}
		return anotacaoDisciplinaVO;
	}

	public void setAnotacaoDisciplinaVO(AnotacaoDisciplinaVO anotacaoDisciplinaVO) {
		this.anotacaoDisciplinaVO = anotacaoDisciplinaVO;
	}

	public Boolean getImagemComAnotacaoSemAnotacao() {
		return imagemComAnotacaoSemAnotacao;
	}

	public void setImagemComAnotacaoSemAnotacao(Boolean imagemComAnotacaoSemAnotacao) {
		this.imagemComAnotacaoSemAnotacao = imagemComAnotacaoSemAnotacao;
	}

	public void verificarAnotacao() {
		try {
			setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarAnotacaoDisciplinaPorDisciplinaMatriculaConteudoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudoUnidadePagina().getUnidadeConteudo().getConteudo(), getConteudoUnidadePagina().getUnidadeConteudo(), getConteudoUnidadePagina(), getUsuarioLogado()));
			setImagemComAnotacaoSemAnotacao(true);
			if (getAnotacaoDisciplinaVO().getCodigo().equals(0)) {
				setImagemComAnotacaoSemAnotacao(false);
				setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().realizarCriarAnotacaoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudo(), getUnidadeConteudoVO(), getConteudoUnidadePagina(), getUsuarioLogado()));
			}
			setMensagemID("msg_segmentacao_vazio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
	}

	public void verificarAnotacaoIndiceConteudoUnidadePagina() {
		try {
			setConteudoUnidadePagina((ConteudoUnidadePaginaVO) context().getExternalContext().getRequestMap().get("pagina"));
			setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarAnotacaoDisciplinaPorDisciplinaMatriculaConteudoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudoUnidadePagina().getUnidadeConteudo().getConteudo(), getConteudoUnidadePagina().getUnidadeConteudo(), getConteudoUnidadePagina(), getUsuarioLogado()));
			if (getAnotacaoDisciplinaVO().getCodigo().equals(0)) {
				setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().realizarCriarAnotacaoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudo(), getUnidadeConteudoVO(), getConteudoUnidadePagina(), getUsuarioLogado()));
			}
			setMensagemID("msg_segmentacao_vazio", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
	}

	public void verificarAnotacaoIndiceUnidadeConteudo() {
		try {
			setUnidadeConteudoVO((UnidadeConteudoVO) context().getExternalContext().getRequestMap().get("unidade"));
			setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarAnotacaoDisciplinaPorDisciplinaMatriculaUnidadeConteudo(getMatriculaAluno(), getConteudo().getDisciplina(), getUnidadeConteudoVO().getConteudo(), getUnidadeConteudoVO(), getUsuarioLogado()));
			if (getAnotacaoDisciplinaVO().getCodigo().equals(0)) {
				setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().realizarCriarAnotacaoUnidadeConteudo(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudo(), getUnidadeConteudoVO(), getUsuarioLogado()));
			}
			setMensagemID("msg_segmentacao_vazio", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
	}

	public void persistirAnotacao() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().persistir(getAnotacaoDisciplinaVO(), false, getUsuarioLogado());			
			if(Uteis.isAtributoPreenchido(getUnidadeConteudoVO()) && Uteis.isAtributoPreenchido(getConteudoUnidadePagina()) && !getConteudoUnidadePagina().getExisteAnotacaoDisciplina()) {
				getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())).findFirst().get().getConteudoUnidadePaginaVOs().stream().filter(t -> t.getCodigo().equals(getConteudoUnidadePagina().getCodigo())).findFirst().get().setExisteAnotacaoDisciplina(true);
				getConteudoUnidadePagina().setExisteAnotacaoDisciplina(true);
			}else	if(Uteis.isAtributoPreenchido(getUnidadeConteudoVO()) && !Uteis.isAtributoPreenchido(getConteudoUnidadePagina()) && !getConteudoUnidadePagina().getPaginaJaVisualizada()) {
				getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())).findFirst().get().setExisteAnotacaoDisciplina(true);
				getUnidadeConteudoVO().setExisteAnotacaoDisciplina(true);
			}
			setImagemComAnotacaoSemAnotacao(true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemNivelImportancia() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(NivelImportanciaEnum.ALTA, NivelImportanciaEnum.ALTA.getValorApresentar()));
		objs.add(new SelectItem(NivelImportanciaEnum.NORMAL, NivelImportanciaEnum.NORMAL.getValorApresentar()));
		objs.add(new SelectItem(NivelImportanciaEnum.BAIXA, NivelImportanciaEnum.BAIXA.getValorApresentar()));
		return objs;
	}

	public String voltarPaginaInicial() {
		try {
			setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarAnotacaoDisciplinaPorDisciplinaMatriculaConteudoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudoUnidadePagina().getUnidadeConteudo().getConteudo(), getConteudoUnidadePagina().getUnidadeConteudo(), getConteudoUnidadePagina(), getUsuarioLogado()));
			setImagemComAnotacaoSemAnotacao(true);
			if (getAnotacaoDisciplinaVO().getCodigo().equals(0)) {
				setImagemComAnotacaoSemAnotacao(false);
				setAnotacaoDisciplinaVO(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().realizarCriarAnotacaoConteudoUnidadePagina(getMatriculaAluno(), getConteudo().getDisciplina(), getConteudo(), getUnidadeConteudoVO(), getConteudoUnidadePagina(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
		return getCaminhoRedirecionamentoNavegacao("conteudoAlunoForm.xhtml");
	}

	public void excluirAnotacao() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().excluir(getAnotacaoDisciplinaVO(), false, getUsuarioLogado());
			setAnotacaoDisciplinaVO(new AnotacaoDisciplinaVO());
			setImagemComAnotacaoSemAnotacao(false);
			if(Uteis.isAtributoPreenchido(getUnidadeConteudoVO()) && Uteis.isAtributoPreenchido(getConteudoUnidadePagina()) && getConteudoUnidadePagina().getExisteAnotacaoDisciplina()) {
				getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())).findFirst().get().getConteudoUnidadePaginaVOs().stream().filter(t -> t.getCodigo().equals(getConteudoUnidadePagina().getCodigo())).findFirst().get().setExisteAnotacaoDisciplina(false);
				getConteudoUnidadePagina().setExisteAnotacaoDisciplina(false);
			}else	if(Uteis.isAtributoPreenchido(getUnidadeConteudoVO()) && !Uteis.isAtributoPreenchido(getConteudoUnidadePagina()) && getConteudoUnidadePagina().getPaginaJaVisualizada()) {
				getConteudo().getUnidadeConteudoVOs().stream().filter(t -> t.getCodigo().equals(getUnidadeConteudoVO().getCodigo())).findFirst().get().setExisteAnotacaoDisciplina(false);
				getUnidadeConteudoVO().setExisteAnotacaoDisciplina(false);
			}
			setMensagemID("msg_dados_excluido", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
	}

	public void persistirAnotacaoIndice() {
		try {
			getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().persistir(getAnotacaoDisciplinaVO(), false, getUsuarioLogado());
			visualizarIndiceConteudo();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}
	}

	public void excluirAnotacaoIndice() {
		try {
			getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().excluir(getAnotacaoDisciplinaVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_excluido", Uteis.SUCESSO);
			visualizarIndiceConteudo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro" + e.getMessage());
		}

	}
	
	public String navegarParaAvaliacaoOnlineRea() {
		try {
			carregarAvaliacaoOnlineRea();
			context().getExternalContext().getSessionMap().put("avaliacaoOnlineRea", true);
			context().getExternalContext().getSessionMap().put("avaliacaoOnlineMatriculaVO", getAvaliacaoOnlineMatriculaVO());
			context().getExternalContext().getSessionMap().put("conteudoUnidadePaginaRecursoEducacionalVO", getConteudoUnidadePaginaRecursoEducacional());
			context().getExternalContext().getSessionMap().put("apresentarOpcaoFecharGatilho", getApresentarOpcaoFecharGatilho());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaForm.xhtml");
	}
	
	
	
	
	public void limparCamposQuestoesQuandoTipoGeracaoAlteradoAlterado() {
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().clear();
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoDificil(0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoMedio(0);	
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setQuantidadeNivelQuestaoFacil(0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setQuantidadeQualquerNivelQuestao(0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaPorQuestaoNivelDificil(0.0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaPorQuestaoNivelFacil(0.0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaPorQuestaoNivelMedio(0.0);
		getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaMaximaAvaliacao(0.0);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}
	
	public void removerQuestaoAvaliacaoOnline() {
		try {
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().removerQuestaoAvaliacaoOnline(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens"), getUsuarioLogado());
			somarNotaAvaliacao();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void subirQuestaoAvaliacaoOnline() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (avaliacaoOnlineQuestaoVO.getOrdemApresentacao() > 1) {
				AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 = getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().get(avaliacaoOnlineQuestaoVO.getOrdemApresentacao() - 2);
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarOrdemApresentacaoQuestaoAvaliacaoOnline(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO, avaliacaoOnlineQuestaoVO2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerQuestaoAvaliacaoOnline() {
		try {
			AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO = (AvaliacaoOnlineQuestaoVO) context().getExternalContext().getRequestMap().get("questaoListaExercicioItens");
			if (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().size() >= avaliacaoOnlineQuestaoVO.getOrdemApresentacao()) {
				AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO2 = getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().get(avaliacaoOnlineQuestaoVO.getOrdemApresentacao());
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().alterarOrdemApresentacaoQuestaoAvaliacaoOnline(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), avaliacaoOnlineQuestaoVO, avaliacaoOnlineQuestaoVO2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void somarNotaAvaliacao() {
		if (isRenderizarCamposRandomizacaoQuestaoTipoUsoDisciplinaOuGeral()) {
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaPorQuestaoQualquerNivel(
					getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() 
					&& getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() ?  getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil()
					: getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() 
					&& getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() ? getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio()
							: getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() 
									&& getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() >= getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() ? getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil()
											: getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil());
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaMaximaAvaliacao((getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil()) + (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio()) + (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil()) + (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoQualquerNivel() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeQualquerNivelQuestao()));
		} else {
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNotaMaximaAvaliacao((getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelFacil() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoFacil()) + (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelMedio() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoMedio()) + (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getNotaPorQuestaoNivelDificil() * getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getQuantidadeNivelQuestaoDificil()));
		}
	}
	
	public void gerarQuestoesRandomicamente() {
		try {
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO);
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setAvaliacaoOnlineQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineQuestaoInterfaceFacade().gerarQuestoesRandomicamente(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), getNivelFacil(), getNivelMedio(), getNivelDificil(), getQualquerNivel(), null, getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs(), getUsuarioLogado()));
			if(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_AvaliacaoOnline_nenhumaQuestaoEncontrada"));
			}
			setFecharModalPanelQuestoesFixasRandomicas("RichFaces.$('panelQuestoesFixasRandomicamente').hide()");
			somarNotaAvaliacao();
		} catch (Exception e) {
			setFecharModalPanelQuestoesFixasRandomicas("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public boolean getIsNaoPermitirAlterarQuandoAtivado() {
		return getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.ATIVO) || getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getSituacao().equals(SituacaoEnum.INATIVO);
	}
	
	public List<SelectItem> getListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		if(listaSelectItemParametrosMonitoramentoAvaliacaoOnline == null) {
			listaSelectItemParametrosMonitoramentoAvaliacaoOnline = new ArrayList<SelectItem>();
		}
		return listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}

	public void setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(List<SelectItem> listaSelectItemParametrosMonitoramentoAvaliacaoOnline) {
		this.listaSelectItemParametrosMonitoramentoAvaliacaoOnline = listaSelectItemParametrosMonitoramentoAvaliacaoOnline;
	}
	
	public void montarListaSelectItemParametrosMonitoramentoAvaliacaoOnline() {
		try {
			setListaSelectItemParametrosMonitoramentoAvaliacaoOnline(UtilSelectItem.getListaSelectItem(getFacadeFactory().getParametrosMonitoramentoAvaliacaoOnlineFacade().consultarTodosParametros(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo", "descricao", true));
			montarListaDeNotasDaConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoGeracaoProvaOnline() {
		List<SelectItem> comboBoxTipoGeracaoProvaOnlineEnum = new ArrayList<SelectItem>();
		comboBoxTipoGeracaoProvaOnlineEnum = new ArrayList<SelectItem>();
		comboBoxTipoGeracaoProvaOnlineEnum.add(new SelectItem(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE, TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE.getValorApresentar()));
		comboBoxTipoGeracaoProvaOnlineEnum.add(new SelectItem(TipoGeracaoProvaOnlineEnum.FIXO, TipoGeracaoProvaOnlineEnum.FIXO.getValorApresentar()));
		
		return comboBoxTipoGeracaoProvaOnlineEnum;

	}
	
	public List<SelectItem> getComboBoxRegraPoliticaSelecaoQuestao() {
		List<SelectItem> comboBoxRegraPoliticaSelecaoQuestao = new ArrayList<SelectItem>();
		comboBoxRegraPoliticaSelecaoQuestao = new ArrayList<SelectItem>();
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO, PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO.getValorApresentar()));
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE, PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE.getValorApresentar()));
		comboBoxRegraPoliticaSelecaoQuestao.add(new SelectItem(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO, PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO.getValorApresentar()));
		return comboBoxRegraPoliticaSelecaoQuestao;
	}
	
	public boolean isApresentarBotaoIniciarAvaliacaoOnlineRea(){
		if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO()) || getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO)){
			return true;
		}
		return false;
	}
	
	/**
	 * No mesmo caso do getIsRenderizarComboBoxPoliticaSelecaoDeQuestao(), porém
	 * ele será renderizado também se a Política de Seleção de Questão seja
	 * QUESTOES_TODOS_ASSUNTOS_CONTEUDO.
	 * 
	 * @return
	 */
	public boolean getIsApresentarComboBoxRegraDistribuicaoQuestao() {
		return getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE) && (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO) || getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE));
	}
	
	public boolean isRenderizarCamposRandomizacaoQuestaoTipoUsoDisciplinaOuGeral() {
		return getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE);
	}
	
	public boolean isRenderizarCamposQuestoesTipoUsoDisciplina() {
		return getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO);
	}

	public String getFecharModalPanelQuestoesFixasRandomicas() {
		if (fecharModalPanelQuestoesFixasRandomicas == null) {
			fecharModalPanelQuestoesFixasRandomicas = "";
		}
		return fecharModalPanelQuestoesFixasRandomicas;
	}

	public void setFecharModalPanelQuestoesFixasRandomicas(String fecharModalPanelQuestoesFixasRandomicas) {
		this.fecharModalPanelQuestoesFixasRandomicas = fecharModalPanelQuestoesFixasRandomicas;
	}

	public Integer getNivelFacil() {
		if (nivelFacil == null) {
			nivelFacil = 0;
		}
		return nivelFacil;
	}

	public void setNivelFacil(Integer nivelFacil) {
		this.nivelFacil = nivelFacil;
	}

	public Integer getNivelMedio() {
		if (nivelMedio == null) {
			nivelMedio = 0;
		}
		return nivelMedio;
	}

	public void setNivelMedio(Integer nivelMedio) {
		this.nivelMedio = nivelMedio;
	}

	public Integer getNivelDificil() {
		if (nivelDificil == null) {
			nivelDificil = 0;
		}
		return nivelDificil;
	}

	public void setNivelDificil(Integer nivelDificil) {
		this.nivelDificil = nivelDificil;
	}

	public Integer getQualquerNivel() {
		if (qualquerNivel == null) {
			qualquerNivel = 0;
		}
		return qualquerNivel;
	}

	public void setQualquerNivel(Integer qualquerNivel) {
		this.qualquerNivel = qualquerNivel;
	}

	public boolean isUsoOnline() {
		return usoOnline;
	}

	public void setUsoOnline(boolean usoOnline) {
		this.usoOnline = usoOnline;
	}

	public boolean isUsoPresencial() {
		return usoPresencial;
	}

	public void setUsoPresencial(boolean usoPresencial) {
		this.usoPresencial = usoPresencial;
	}

	public boolean isUsoExercicio() {
		return usoExercicio;
	}

	public void setUsoExercicio(boolean usoExercicio) {
		this.usoExercicio = usoExercicio;
	}

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if(avaliacaoOnlineMatriculaVO == null){
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}	

	public ConfiguracaoEADVO getConfiguracaoEADVO() {
		if (configuracaoEADVO == null) {
			configuracaoEADVO = new ConfiguracaoEADVO();
		}
		return configuracaoEADVO;
	}

	public void setConfiguracaoEADVO(ConfiguracaoEADVO configuracaoEADVO) {
		this.configuracaoEADVO = configuracaoEADVO;
	}

	public List<SelectItem> getCamposConsulta() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("descricao", "Descrição Conteúdo"));
		objs.add(new SelectItem("codigoDisciplina", "Código Disciplina"));
		objs.add(new SelectItem("disciplina", "Nome Disciplina"));
		objs.add(new SelectItem("codigoConteudo", "Código Conteúdo"));
		return objs;
	}

	public String consultar() {
		try {
			getListaConsulta().clear();
			setListaConsulta(getFacadeFactory().getConteudoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getCampoSituacaoConteudo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			if (getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void setCaminhoArquivoConteudoUnidadePaginaRecursoEducacional(String caminhoArquivoConteudoUnidadePaginaRecursoEducacional) {
		this.caminhoArquivoConteudoUnidadePaginaRecursoEducacional = caminhoArquivoConteudoUnidadePaginaRecursoEducacional;
	}

	public Boolean getIsNaoPermitirStringCampoNumerico() {
		return getControleConsulta().getCampoConsulta().equals("codigoDisciplina");
	}

	/**
	 * @author Victor Hugo 21/01/2015
	 * 
	 *         Este método além de clonar, já persisti o objeto.
	 */
	public void clonar() {
		try {
			setConteudo(getFacadeFactory().getConteudoFacade().consultarPorChavePrimaria(getConteudo().getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			setConteudo(getFacadeFactory().getConteudoFacade().clonarConteudoVO(getConteudo(), getUsuarioLogado()));
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List getListaSelectItemDisciplinasProfessor() {
		if (listaSelectItemDisciplinasProfessor == null) {
			listaSelectItemDisciplinasProfessor = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasProfessor;
	}

	public void setListaSelectItemDisciplinasProfessor(List listaSelectItemDisciplinasProfessor) {
		this.listaSelectItemDisciplinasProfessor = listaSelectItemDisciplinasProfessor;
	}

	public void montarListaSelectItemDisciplinasProfessor() {
		try {
			setListaSelectItemDisciplinasProfessor(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemDisciplinasConteudoExclusivoProfessor() {
		try {
			setListaSelectItemDisciplinasProfessor(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasConteudoUsoExclusivoProfessor(false, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void consultarVisaoProfessor() {
		try {
			getListaConsulta().clear();
			setListaConsulta(getFacadeFactory().getConteudoFacade().consultarPorCodigoProfessorNivelFuncionalidades(getUsuarioLogado().getPessoa().getCodigo(), getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarConteudoQualquerDisciplina(), getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarConteudoApenasAulasProgramadas(), getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarApenasConteudosExclusivos(), getDisciplinaVO().getCodigo(), getCampoSituacaoConteudo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			if (getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarVisaoProfessor() {
		context().getExternalContext().getSessionMap().put("booleanoEditarConteudoVisaoProfessor", true);
		setConteudo((ConteudoVO) context().getExternalContext().getRequestMap().get("conteudoItens"));
		context().getExternalContext().getSessionMap().put("codigoConteudo", getConteudo().getCodigo());
		removerControleMemoriaFlashTela("ConteudoControle_"+getConteudo().getCodigo());
	}

	public void selecionarDisciplinaVisaoProfessor() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(disciplina);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList();
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public void selecionarProfessor() {
		try {
			getConteudo().setProfessor((PessoaVO) context().getExternalContext().getRequestMap().get("professor"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				if (getValorConsultaProfessor() == "") {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				if (getValorConsultaProfessor() == "") {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void limparDadosProfessor() {
		removerObjetoMemoria(getConteudo().getProfessor());
	}

	public void novoVisaoProfessor() {
		removerControleMemoriaFlashTela("ConteudoControle_0");
		context().getExternalContext().getSessionMap().put("booleanoNovoObjetoVisaoProfessor", true);
		setIdControlador("ConteudoControle_0");
	}

	public void montarComboBoxTemaAssuntoDisciplina() {
		try {
			setListaSelectItemTemaAssuntoVOs(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTemaAssuntoFacade().consultarTemaAssuntoPorCodigoDisciplina(getConteudo().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public Boolean getIsApresentarRegraDistribuicaoQuestao() {
		return getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO) && (getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO) || getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS));
	}

	public List<SelectItem> getListaSelectItemTemaAssuntoVOs() {
		if (listaSelectItemTemaAssuntoVOs == null) {
			listaSelectItemTemaAssuntoVOs = new ArrayList<SelectItem>();
		}
		return listaSelectItemTemaAssuntoVOs;
	}

	public void setListaSelectItemTemaAssuntoVOs(List<SelectItem> listaSelectItemTemaAssuntoVOs) {
		this.listaSelectItemTemaAssuntoVOs = listaSelectItemTemaAssuntoVOs;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	public Boolean getIsApresentarBotaoNovoVisaoAdministrativo() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}

	public Boolean getIsApresentarBotaoNovoVisaoProfessor() {
		return getUsuarioLogado().getIsApresentarVisaoProfessor() && !getLoginControle().getPermissaoAcessoMenuVO().getPermitirProfessorCadastrarApenasConteudosExclusivos();
	}

	private String backgroundRecursoEducacional;

	public String montarBackgroundGatilho() {
		
		if (getConteudoUnidadePaginaRecursoEducacional().getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && getConteudoUnidadePaginaRecursoEducacional().getCorBackground().trim().isEmpty()) {
			return "background-color:#FFFFFF;  !important";
		} else if (!getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseBackground().trim().isEmpty() && !getConteudoUnidadePaginaRecursoEducacional().getNomeImagemBackground().trim().isEmpty()) {
			String imagemBackgroundREA = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getConteudoUnidadePaginaRecursoEducacional().getCaminhoBaseBackground().replaceAll("\\\\", "/") + "/" + getConteudoUnidadePaginaRecursoEducacional().getNomeImagemBackground();
			StringBuilder back = new StringBuilder("background-image: url('" + imagemBackgroundREA + "')  !important");
			if (!getConteudoUnidadePaginaRecursoEducacional().getCorBackground().trim().isEmpty()) {
				back.append("; background-color: ").append(getConteudoUnidadePaginaRecursoEducacional().getCorBackground().contains("#")? "" : "#").append(getConteudoUnidadePaginaRecursoEducacional().getCorBackground()).append("  !important");
			}
			if (getConteudoUnidadePaginaRecursoEducacional().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO)) {
				back.append("; background-size:100%  !important");
			}
			if (getConteudoUnidadePaginaRecursoEducacional().getTamanhoImagemBackgroundConteudo().equals(TamanhoImagemBackgroundConteudoEnum.TAMANHO_ORIGINAL)) {
				back.append("; background-position:center center  !important; margin: 0 auto  !important");
			}
			back.append(";  background-repeat:no-repeat  !important;");
			return back.toString();
		}
		if (!getConteudoUnidadePaginaRecursoEducacional().getCorBackground().trim().isEmpty()) {
			return "background-color:"  + (getConteudoUnidadePaginaRecursoEducacional().getCorBackground().contains("#") ? "" : "#") + getConteudoUnidadePaginaRecursoEducacional().getCorBackground() + "  !important;";
		}
		return "background-color:#FFFFFF  !important;";				
	}

	public void setBackgroundRecursoEducacional(String backgroundRecursoEducacional) {
		this.backgroundRecursoEducacional = backgroundRecursoEducacional;
	}

	/**
	 * 
	 * @author Victor Hugo de Paula Costa - 1 de jul de 2016
	 */
	private NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO;
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacaoPbl;
	private List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliados;

	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> getListaAvaliados() {
		if (listaAvaliados == null) {
			listaAvaliados = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		}
		return listaAvaliados;
	}

	public void setListaAvaliados(List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliados) {
		this.listaAvaliados = listaAvaliados;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getAutoAvaliacaoPbl() {
		if (autoAvaliacaoPbl == null) {
			autoAvaliacaoPbl = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return autoAvaliacaoPbl;
	}

	public void setAutoAvaliacaoPbl(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) {
		this.autoAvaliacaoPbl = gestaoEventoConteudoTurmaAvaliacaoPBLVO;
	}

	public NotaConceitoAvaliacaoPBLVO getNotaConceitoAvaliacaoPBLVO() {
		if (notaConceitoAvaliacaoPBLVO == null) {
			notaConceitoAvaliacaoPBLVO = new NotaConceitoAvaliacaoPBLVO();
		}
		return notaConceitoAvaliacaoPBLVO;
	}

	public void setNotaConceitoAvaliacaoPBLVO(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) {
		this.notaConceitoAvaliacaoPBLVO = notaConceitoAvaliacaoPBLVO;
	}

	public void incluirFormaCalculoAutoAvaliacao() {
		if (getConteudoUnidadePaginaRecursoEducacional().getAutoAvaliacao()) {
			if (!getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("AUTO_AVAL")) {
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal() + ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL);
			}
		} else {
			if (getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("AUTO_AVAL")) {
				String aux = getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, "");
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(aux);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMinimaNotaAutoAvaliacao(0.0);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMaximaNotaAutoAvaliacao(0.0);
			}
		}
	}

	public void incluirFormaCalculoAlunoAvaliaAluno() {
		if (getConteudoUnidadePaginaRecursoEducacional().getAlunoAvaliaAluno()) {
			if (!getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("ALUNO_AVAL")) {
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal() + ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL);
			}
		} else {
			if (getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("ALUNO_AVAL")) {
				String aux = getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, "");
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(aux);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMinimaNotaAlunoAvaliaAluno(0.0);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMaximaNotaAlunoAvaliaAluno(0.0);
			}
		}
	}

	public void incluirFormaCalculProfessorAvaliaAluno() {
		if (getConteudoUnidadePaginaRecursoEducacional().getProfessorAvaliaAluno()) {
			if (!getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("PROF_AVAL")) {
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal() + ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL);
			}
		} else {
			if (getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().contains("PROF_AVAL")) {
				String aux = getConteudoUnidadePaginaRecursoEducacional().getFormulaCalculoNotaFinal().replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, "");
				getConteudoUnidadePaginaRecursoEducacional().setFormulaCalculoNotaFinal(aux);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMinimaNotaProfessorAvaliaAluno(0.0);
				getConteudoUnidadePaginaRecursoEducacional().setFaixaMaximaNotaProfessorAvaliaAluno(0.0);
			}
		}
	}

	/**
	 * @author Victor Hugo 05/07/2016 11:22
	 */
	private String tipoNotaConceito;

	public String getTipoNotaConceito() {
		if (tipoNotaConceito == null) {
			tipoNotaConceito = "";
		}
		return tipoNotaConceito;
	}

	public void setTipoNotaConceito(String tipoNotaConceito) {
		this.tipoNotaConceito = tipoNotaConceito;
	}

	public void limparDadosNotaConceito() {
		setNotaConceitoAvaliacaoPBLVO(new NotaConceitoAvaliacaoPBLVO());
		limparMensagem();
	}

	public void adicionarNotaConceito() {
		try {
			String tipo = getTipoNotaConceito();
			getNotaConceitoAvaliacaoPBLVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(tipo));
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().adicionarNotaConceito(getConteudoUnidadePaginaRecursoEducacional(), getNotaConceitoAvaliacaoPBLVO());
			setNotaConceitoAvaliacaoPBLVO(new NotaConceitoAvaliacaoPBLVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		}
	}

	public void removerNotaConceito() {
		try {
			NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO = (NotaConceitoAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("notaConceito");
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().removerNotaConceito(getConteudoUnidadePaginaRecursoEducacional(), notaConceitoAvaliacaoPBLVO, getUsuarioLogado());
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		}
	}

	public void inicializarAvaliacaoPBLAluno() throws Exception {
		getConteudoUnidadePaginaRecursoEducacional().setGestaoEventoConteudoTurmaVO(getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacional().getCodigo(), getConteudo().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setProfessor(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getProfessor());
		if (!Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO()) && !getConteudoUnidadePaginaRecursoEducacional().getRequerLiberacaoProfessor() ) {
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setTurmaVO(getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setAno(getMatriculaPeriodoTurmaDisciplinaVO().getAno());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setConteudoVO(getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().inicializarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), getUsuarioLogado());
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), false, getUsuarioLogado());
		}else if(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO()) && 
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente() && 
				(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getDateLiberacao().compareTo(new Date()) <= 0)){
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.LIBERADO);
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().atualizarSituacaoGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), SituacaoPBLEnum.LIBERADO, true, getUsuarioLogado());
		}
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacional(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
		setAutoAvaliacaoPbl(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAutoAvaliacao(getConteudoUnidadePaginaRecursoEducacional().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getConteudo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setListaAvaliados(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAvaliador(getConteudoUnidadePaginaRecursoEducacional().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getConteudo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	}

	public void inicializarAvaliacaoPBLProfessor() throws Exception {
		getConteudoUnidadePaginaRecursoEducacional().setGestaoEventoConteudoTurmaVO(getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoComAvaliacaoPbl(getConteudoUnidadePaginaRecursoEducacional().getCodigo(), getConteudo().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setProfessor(getUsuarioLogado().getPessoa());
		if (!Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO())) {
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setTurmaVO(getMatriculaPeriodoTurmaDisciplinaVO().getTurma());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setAno(getMatriculaPeriodoTurmaDisciplinaVO().getAno());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina());
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setConteudoVO(getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().inicializarGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), getUsuarioLogado());
			if (!getConteudoUnidadePaginaRecursoEducacional().getRequerLiberacaoProfessor()) {
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), false, getUsuarioLogado());
			}
		}else if(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO()) && 
				getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente() && 
				(Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getDateLiberacao().compareTo(new Date()) <= 0)){
			getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.LIBERADO);
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().atualizarSituacaoGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), SituacaoPBLEnum.LIBERADO, true, getUsuarioLogado());
		}
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().verificarSePossuiNovasMatriculas(getConteudoUnidadePaginaRecursoEducacional(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
	}

	private String modalMensagemAvisoAluno;
	private Boolean apresentarModalMensagemAvisoAluno;

	public String getModalMensagemAvisoAluno() {
		if (modalMensagemAvisoAluno == null) {
			modalMensagemAvisoAluno = "";
		}
		return modalMensagemAvisoAluno;
	}

	public void setModalMensagemAvisoAluno(String modalMensagemAvisoAluno) {
		this.modalMensagemAvisoAluno = modalMensagemAvisoAluno;
	}

	public void validarNotaMinimaMaximaConceitoAlunoAvaliaAluno() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO = (GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("notaColega");
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAlunoAvaliaAluno(gestaoEventoConteudoTurmaAvaliacaoPBLVO, getConteudoUnidadePaginaRecursoEducacional());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
		}
	}

	public void validarNotaMinimaMaximaConceitoAutoAvaliacao() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAutoAvaliacao(getAutoAvaliacaoPbl(), getConteudoUnidadePaginaRecursoEducacional());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
		}
	}

	public void validarNotaMinimaMaximaConceitoProfAvaliaAluno() {
		try {
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoProfAvaliaAluno(getAutoAvaliacaoPbl(), getConteudoUnidadePaginaRecursoEducacional());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
		}
	}

	public Boolean getApresentarModalMensagemAvisoAluno() {
		if (apresentarModalMensagemAvisoAluno == null) {
			apresentarModalMensagemAvisoAluno = false;
		}
		return apresentarModalMensagemAvisoAluno;
	}

	public void setApresentarModalMensagemAvisoAluno(Boolean apresentarModalMensagemAvisoAluno) {
		this.apresentarModalMensagemAvisoAluno = apresentarModalMensagemAvisoAluno;
	}

	public void verificarRegrasApresentacaoConteudoUnidadePaginaRecursoEducacional() throws Exception {
		if (getConteudoUnidadePaginaRecursoEducacional().getRequerLiberacaoProfessor() && getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente() && !getEmularAcessoProfessor()) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setModalPanelAddRecursoEducacional("");
			setApresentarGatilho(false);
			throw new Exception(UteisJSF.internacionalizar("msg_RequerLiberacaoProfessorENaoPermiteAlunoAvancar"));
		}
	}
	

	/*public void gravarGestaoEventoConteudoTurmaAvaliacaoPBL() {
		try {
			setModalMensagemAvisoAluno("");
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistirGestaoEventoConteudoTurmaAvaliacaoVisaoAluno(getConteudoUnidadePaginaRecursoEducacional(), getListaAvaliados(), getAutoAvaliacaoPbl(), false, getUsuarioLogado());
			setMensagemID("msg_Conteudo_notasSalvasComSucesso", Uteis.SUCESSO);
			setModalMensagemAvisoAluno("Richfaces.showModalPanel('panelMensagem');");
			setApresentarModalMensagemAvisoAluno(true);
		} catch (Exception e) {
			ConsistirException ce = new ConsistirException();
			ce.adicionarListaMensagemErro(e.getMessage());
			setConsistirExceptionMensagemDetalhada("msg_erro", ce, Uteis.ERRO);
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("Richfaces.showModalPanel('panelMensagem');");
		}
	}*/
	
	public void calcularNotaFinalAcaoTabelaAutoAvaliacaoVisaoAluno() {
		try {			
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacional(), getAutoAvaliacaoPbl(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado());
			setMensagemID("", "");
			setMensagem(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_notasLancadasComSucessoAutoAvaliacao"));
			setSucesso(true);
			setIconeMensagem("/resources/imagens/sucesso.gif");
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setIconeMensagem("/resources/imagens/erro.gif");
			setSucesso(true);
		}
	}

	public void calcularNotaFinalAcaoTabelaAlunoVisaoAluno() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("notaColega"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacional(), obj, TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("", "");
			setMensagem(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_notasLancadasComSucessoAlunoAvaliaAluno").replace("{0}", obj.getAvaliado().getNome()));
			setSucesso(true);
			setIconeMensagem("/resources/imagens/sucesso.gif");
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setIconeMensagem("/resources/imagens/erro.gif");
			setSucesso(true);
		}
	}

	public void liberarGestaoEventoConteudoTurma() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().preencherDadosGestaoEventoConteudoTurmaLiberadaParaLiberacao(getConteudoUnidadePaginaRecursoEducacional());
			gravarGestaoEventoConteudoTurma();
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarGestaoEventoConteudoTurma() {
		try {
			if (!Uteis.isAtributoPreenchido(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO().getSituacao().isPendente()) {
				throw new Exception(internacionalizar("msg_GestaoEventoConteudo_dataLiberacao"));
			}
			getFacadeFactory().getGestaoEventoConteudoTurmaFacade().persistirGestaoEventoConteudoTurma(getConteudoUnidadePaginaRecursoEducacional(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void calcularNotaFinalAcaoTabelaAutoAvaliacao() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoItem"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacional(), obj, TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void calcularNotaFinalAcaoTabelaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO obj) {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacional(), obj, TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucessoAlunoAvaliaAluno".replace("{0}", getAvaliacaoAluno().getAvaliado().getNome()), Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void calcularNotaFinalAcaoTabelaProfessor() {
		try {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = ((GestaoEventoConteudoTurmaAvaliacaoPBLVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoItem"));
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(getConteudoUnidadePaginaRecursoEducacional(), obj.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(), TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO, getUsuarioLogado());
			setMensagemID("msg_GestaoEventoConteudoTurma_notasLancadasComSucesso", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaNotaConceitoAvaliacaoPbl() {
		try {
			if (getConteudoUnidadePaginaRecursoEducacional().getUtilizarNotaConceito()) {
				if (getConteudoUnidadePaginaRecursoEducacional().getNotaConceitoAvaliacaoPBLVOs().isEmpty()) {
					Integer codigo = getConteudoUnidadePaginaRecursoEducacional().getCodigo();
					getConteudoUnidadePaginaRecursoEducacional().setNotaConceitoAvaliacaoPBLVOs(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(codigo, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacional().getAutoAvaliacao()) {
					setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacional().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacional().getAlunoAvaliaAluno()) {
					setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacional().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, getUsuarioLogado()));
				}
				if (getConteudoUnidadePaginaRecursoEducacional().getProfessorAvaliaAluno()) {
					setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(getConteudoUnidadePaginaRecursoEducacional().getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO, getUsuarioLogado()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			if(getEmulandoAcessoPagina() || getEmularAcessoAluno() || getEmularAcessoProfessor()) {
				getGestaoEventoConteudoTurmaInteracaoAta().getPessoaVO().setNome(getUsuarioLogado().getNome());
				getGestaoEventoConteudoTurmaInteracaoAta().getPessoaVO().setCodigo(getUsuarioLogado().getCodigo());
				getFacadeFactory().getGestaoEventoConteudoTurmaFacade().adicionarGestaoEventoConteudoTurmaInteracaoAtaVO(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), getGestaoEventoConteudoTurmaInteracaoAta(), getUsuarioLogado());				
			}else {
				getFacadeFactory().getGestaoEventoConteudoTurmaInteracaoAtaFacade().persistirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), getGestaoEventoConteudoTurmaInteracaoAta(), getEmularAcessoProfessor(), false, getUsuarioLogado());
			}
			setGestaoEventoConteudoTurmaInteracaoAta(new GestaoEventoConteudoTurmaInteracaoAtaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editaGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			GestaoEventoConteudoTurmaInteracaoAtaVO obj = (GestaoEventoConteudoTurmaInteracaoAtaVO) context().getExternalContext().getRequestMap().get("gestaoInteracao");
			setGestaoEventoConteudoTurmaInteracaoAta(obj);
			setMensagemID("msg_dados_editar", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerGestaoEventoConteudoTurmaInteracaoAta() {
		try {
			GestaoEventoConteudoTurmaInteracaoAtaVO obj = (GestaoEventoConteudoTurmaInteracaoAtaVO) context().getExternalContext().getRequestMap().get("gestaoInteracao");
			getFacadeFactory().getGestaoEventoConteudoTurmaInteracaoAtaFacade().excluirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), obj, getEmularAcessoProfessor(), getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarGestaoEventoConteudoTurmaResponsavelAta() {
		try {
			getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().persistirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), getGestaoEventoConteudoTurmaResponsavelAta(), false, getUsuarioLogado());
			setGestaoEventoConteudoTurmaResponsavelAta(new GestaoEventoConteudoTurmaResponsavelAtaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerGestaoEventoConteudoTurmaResponsavelAta() {
		try {
			GestaoEventoConteudoTurmaResponsavelAtaVO obj = (GestaoEventoConteudoTurmaResponsavelAtaVO) context().getExternalContext().getRequestMap().get("gestaoEventoConteudoTurmaResponsavelAtaItens");
			getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().excluirGestaoEventoConteudoTurmaResponsavelAtaVisaoAluno(getConteudoUnidadePaginaRecursoEducacional().getGestaoEventoConteudoTurmaVO(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			setApresentarModalMensagemAvisoAluno(false);
			setModalMensagemAvisoAluno("");
		} catch (Exception e) {
			setApresentarModalMensagemAvisoAluno(true);
			setModalMensagemAvisoAluno("RichFaces.$('panelMensagem').show();");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemPessoa() {
		try {
			montarListaSelectItemPessoa("");
		} catch (Exception e) {

		}
	}

	public void montarListaSelectItemPessoa(String prm) throws Exception {
		List resultadoConsulta = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorDisciplinaTurmaAnoSemestreUnidadeEnsino(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getUsuarioLogado());
		Iterator i = resultadoConsulta.iterator();
		getListaSelectPessoa().clear();
		getListaSelectPessoa().add(new SelectItem(0, ""));
		while (i.hasNext()) {
			PessoaVO obj = (PessoaVO) i.next();
			getListaSelectPessoa().add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
	}

	public GestaoEventoConteudoTurmaInteracaoAtaVO getGestaoEventoConteudoTurmaInteracaoAta() {
		if (gestaoEventoConteudoTurmaInteracaoAta == null) {
			gestaoEventoConteudoTurmaInteracaoAta = new GestaoEventoConteudoTurmaInteracaoAtaVO();
		}
		return gestaoEventoConteudoTurmaInteracaoAta;
	}

	public void setGestaoEventoConteudoTurmaInteracaoAta(GestaoEventoConteudoTurmaInteracaoAtaVO gestaoEventoConteudoTurmaInteracaoAta) {
		this.gestaoEventoConteudoTurmaInteracaoAta = gestaoEventoConteudoTurmaInteracaoAta;
	}

	public GestaoEventoConteudoTurmaResponsavelAtaVO getGestaoEventoConteudoTurmaResponsavelAta() {
		if (gestaoEventoConteudoTurmaResponsavelAta == null) {
			gestaoEventoConteudoTurmaResponsavelAta = new GestaoEventoConteudoTurmaResponsavelAtaVO();
		}
		return gestaoEventoConteudoTurmaResponsavelAta;
	}

	public void setGestaoEventoConteudoTurmaResponsavelAta(GestaoEventoConteudoTurmaResponsavelAtaVO gestaoEventoConteudoTurmaResponsavelAta) {
		this.gestaoEventoConteudoTurmaResponsavelAta = gestaoEventoConteudoTurmaResponsavelAta;
	}

	public List<SelectItem> getListaSelectPessoa() {
		if (listaSelectPessoa == null) {
			listaSelectPessoa = new ArrayList<SelectItem>();
		}
		return listaSelectPessoa;
	}

	public void setListaSelectPessoa(List<SelectItem> listaSelectPessoa) {
		this.listaSelectPessoa = listaSelectPessoa;
	}

	public boolean isUsuarioRedatorAta() {
		return usuarioRedatorAta;
	}

	public void setUsuarioRedatorAta(boolean usuarioRedatorAta) {
		this.usuarioRedatorAta = usuarioRedatorAta;
	}

	public String getModalPanelAddRecursoEducacional() {
		if (modalPanelAddRecursoEducacional == null) {
			modalPanelAddRecursoEducacional = "";
		}
		return modalPanelAddRecursoEducacional;
	}

	public void setModalPanelAddRecursoEducacional(String modalPanelAddRecursoEducacional) {
		this.modalPanelAddRecursoEducacional = modalPanelAddRecursoEducacional;
	}

	public void verificarAlunoPodeAvancarConteudoREAPendente() throws Exception {
		if (!getEmularAcessoProfessor() && !getEmularAcessoAluno()) {
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().verificarAlunoPodeAvancarConteudoREAPendente(getConteudo(), getConteudoUnidadePagina(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado());
		}
	}

	public List<SelectItem> getComboBoxSituacaoConteudoConsulta() {
		if (comboBoxSituacaoConteudoConsulta == null) {
			comboBoxSituacaoConteudoConsulta = new ArrayList<SelectItem>();
			comboBoxSituacaoConteudoConsulta.add(new SelectItem("TODOS", "Todos"));
			comboBoxSituacaoConteudoConsulta.addAll(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoConteudoEnum.class, "nome", "valorApresentar", false));
		}
		return comboBoxSituacaoConteudoConsulta;
	}

	public void setComboBoxSituacaoConteudoConsulta(List<SelectItem> comboBoxSituacaoConteudoConsulta) {
		this.comboBoxSituacaoConteudoConsulta = comboBoxSituacaoConteudoConsulta;
	}

	public String getCampoSituacaoConteudo() {
		if (campoSituacaoConteudo == null) {
			campoSituacaoConteudo = "";
		}
		return campoSituacaoConteudo;
	}

	public void setCampoSituacaoConteudo(String campoSituacaoConteudo) {
		this.campoSituacaoConteudo = campoSituacaoConteudo;
	}
	
	public DisciplinaVO getDisciplinaModalConteudoClone() {
		return disciplinaModalConteudoClone;
	}

	public void setDisciplinaModalConteudoClone(DisciplinaVO disciplinaModalConteudoClone) {
		this.disciplinaModalConteudoClone = disciplinaModalConteudoClone;
	}
	
	public String getModalPanelDisciplinaConteudoClone() {
		if(modalPanelDisciplinaConteudoClone == null){
			modalPanelDisciplinaConteudoClone = "";
		}
		return modalPanelDisciplinaConteudoClone;
	}

	public void setModalPanelDisciplinaConteudoClone(String modalPanelDisciplinaConteudoClone) {
		this.modalPanelDisciplinaConteudoClone = modalPanelDisciplinaConteudoClone;
	}
	

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao = listaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao;
	}

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno;
	}

	public List<SelectItem> getListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno() {
		if (listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno == null) {
			listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}

	public void setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(List<SelectItem> listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno) {
		this.listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno = listaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBLVO getAvaliacaoAluno() {
		if (avaliacaoAluno == null) {
			avaliacaoAluno = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		}
		return avaliacaoAluno;
	}

	public void setAvaliacaoAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacaoAluno) {
		this.avaliacaoAluno = avaliacaoAluno;
	}
	
	public MomentoApresentacaoRecursoEducacionalEnum getMomentoApresentacaoRecursoEducacionalEnum() {
		return momentoApresentacaoRecursoEducacionalEnum;
	}

	public void setMomentoApresentacaoRecursoEducacionalEnum(MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum) {
		this.momentoApresentacaoRecursoEducacionalEnum = momentoApresentacaoRecursoEducacionalEnum;
	}

	public boolean isApresentarConteudoUnidadePaginaRecursoEducacionalProfessor() {
		return apresentarConteudoUnidadePaginaRecursoEducacionalProfessor;
	}

	public void setApresentarConteudoUnidadePaginaRecursoEducacionalProfessor(boolean apresentarConteudoUnidadePaginaRecursoEducacionalProfessor) {
		this.apresentarConteudoUnidadePaginaRecursoEducacionalProfessor = apresentarConteudoUnidadePaginaRecursoEducacionalProfessor;
	}

	
	
	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalAntesAlteracao() {
		if(conteudoUnidadePaginaRecursoEducacionalAntesAlteracao == null){
			conteudoUnidadePaginaRecursoEducacionalAntesAlteracao = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalAntesAlteracao;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalAntesAlteracao(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalAntesAlteracao) {
		this.conteudoUnidadePaginaRecursoEducacionalAntesAlteracao = conteudoUnidadePaginaRecursoEducacionalAntesAlteracao;
	}

	public String getWidthFormRecursoEducacional() {
		return widthFormRecursoEducacional;
	}

	public void setWidthFormRecursoEducacional(String widthFormRecursoEducacional) {
		this.widthFormRecursoEducacional = widthFormRecursoEducacional;
	}

	public String getHeightFormRecursoEducacional() {
		return heightFormRecursoEducacional;
	}

	public void setHeightFormRecursoEducacional(String heightFormRecursoEducacional) {
		this.heightFormRecursoEducacional = heightFormRecursoEducacional;
	}

	public String getWidthPanelGroupRecursoEducacional() {
		return widthPanelGroupRecursoEducacional;
	}

	public void setWidthPanelGroupRecursoEducacional(String widthPanelGroupRecursoEducacional) {
		this.widthPanelGroupRecursoEducacional = widthPanelGroupRecursoEducacional;
	}

	public String getHeightPanelGroupRecursoEducacional() {
		return heightPanelGroupRecursoEducacional;
	}

	public void setHeightPanelGroupRecursoEducacional(String heightPanelGroupRecursoEducacional) {
		this.heightPanelGroupRecursoEducacional = heightPanelGroupRecursoEducacional;
	}

	public void maximizarPanelRecursoEducacional() {
		getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
		setMaximizarTela(true);

	}

	public void restauraValorPadraoPanelRecursoEducacional() {
		getConteudoUnidadePaginaRecursoEducacional().setConteudoPaginaApresentar(null);
		setWidthFormRecursoEducacional("800");
		setHeightFormRecursoEducacional("537");
		setWidthPanelGroupRecursoEducacional("765");
		setHeightPanelGroupRecursoEducacional("428");
		setMaximizarTela(false);		
	}

	public String getStyleCssPanelGroupRecursoEducacional() {
		return "width:" + getWidthPanelGroupRecursoEducacional() + "px; height:" + (Integer.parseInt(getHeightPanelGroupRecursoEducacional())- 80) + "px;";
	}

	public String getStyleCssPanelGroupItemRecursoEducacional() {
		return "width:" + (Integer.parseInt(getWidthPanelGroupRecursoEducacional()) - 20) + "px; height:" + (Integer.parseInt(getHeightPanelGroupRecursoEducacional()) - 80) + "px;";
	}
	
	public boolean isMaximizarTelaGeralConteudo() {
		return maximizarTelaGeralConteudo;
	}

	public void setMaximizarTelaGeralConteudo(boolean maximizarTelaGeralConteudo) {
		this.maximizarTelaGeralConteudo = maximizarTelaGeralConteudo;
	}
	
	public String getCssMaximizarTelaGeralConteudo() {
		if (isMaximizarTelaGeralConteudo()){
			return "left:82px;float:right;width:90%;margin-right:10px;height:100%;";
		}
		return "float:right;width:97%;margin-right:10px;top: 20px;position: relative;height:100%;";
	}
	
	
	
	public String getHeightMaximizarTelaGeralConteudo() {
		if (isMaximizarTelaGeralConteudo() || heightMaximizarTelaGeralConteudo== null ){
			return "min-height:338px;";
		}
		return "min-height:"+heightMaximizarTelaGeralConteudo+"px;";
	}

	public void setHeightMaximizarTelaGeralConteudo(String heightMaximizarTelaGeralConteudo) {
		this.heightMaximizarTelaGeralConteudo = heightMaximizarTelaGeralConteudo;
	}	
	
	public void realizarMaximizacaoTelaGeralConteudo() {
		if(isMaximizarTelaGeralConteudo()){
			setMaximizarTelaGeralConteudo(false);	
		}else{
			setMaximizarTelaGeralConteudo(true);
		}
	}
	
	public boolean isAgendarLiberacaoRecursoEducacional() {
		return agendarLiberacaoRecursoEducacional;
	}

	public void setAgendarLiberacaoRecursoEducacional(boolean agendarLiberacaoRecursoEducacional) {
		this.agendarLiberacaoRecursoEducacional = agendarLiberacaoRecursoEducacional;
	}
	
	public boolean isMaximizarTela() {
		return maximizarTela;
	}

	public void setMaximizarTela(boolean maximizarTela) {
		this.maximizarTela = maximizarTela;
	}
	
	public String getLarguraEditor() {
		
		if (getApresentarListaRecursoEducacionalConteudo()) {
			return "width:560px; height:250px; float:left";
		}
		return "width:810px; height:100%; float:left";
	}
	
	public void maximizarTreeREA() {
		TreeNodeCustomizado node = (TreeNodeCustomizado) context().getExternalContext().getRequestMap().get("node");
		if (node.getMaximizarTree()) {
			node.setMaximizarTree(false);
		} else {
			node.setMaximizarTree(true);
		}
	}
	

	
	public String formatarTextoSemBackGround(String texto) {
		texto = texto.replace(texto.substring(texto.indexOf("style"), texto.indexOf(";\"") + 2), "") ;
		return texto;
		
	}

	public String getImagemBackgroundPagina() {
		if (imagemBackgroundPagina == null) {
			imagemBackgroundPagina = "";
		}
		return imagemBackgroundPagina;
	}

	public void setImagemBackgroundPagina(String imagemBackgroundPagina) {
		this.imagemBackgroundPagina = imagemBackgroundPagina;
	}

	public String getImagemBackgroundConteudo() {
		if (imagemBackgroundConteudo == null) {
			imagemBackgroundConteudo = "";
		}
		return imagemBackgroundConteudo;
	}

	public void setImagemBackgroundConteudo(String imagemBackgroundConteudo) {
		this.imagemBackgroundConteudo = imagemBackgroundConteudo;
	}

	public String getImagemBackgroundUnidadeConteudo() {
		if (imagemBackgroundUnidadeConteudo == null) {
			imagemBackgroundUnidadeConteudo = "";
		}
		return imagemBackgroundUnidadeConteudo;
	}

	public void setImagemBackgroundUnidadeConteudo(String imagemBackgroundUnidadeConteudo) {
		this.imagemBackgroundUnidadeConteudo = imagemBackgroundUnidadeConteudo;
	}
	
	public List<SelectItem> getListaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline() {
		if (listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline == null) {
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline = new ArrayList<SelectItem>(0);
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA.getValorApresentar()));
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.NUMERO_DIA_ESPECIFICO, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.NUMERO_DIA_ESPECIFICO.getValorApresentar()));
			listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline.add(new SelectItem(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA, RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA.getValorApresentar()));
		}
		return listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	}

	public void setListaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline(List<SelectItem> listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline) {
		this.listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline = listaSelectItemRegraDefinicaoPeriodoAvaliacaoOnline;
	}	
	
	public Boolean getPermitirAlteracaoValorNotaAvaliacaoOnline() {
		if (permitirAlteracaoValorNotaAvaliacaoOnline == null) {
			permitirAlteracaoValorNotaAvaliacaoOnline = true;
		}
		return permitirAlteracaoValorNotaAvaliacaoOnline;
	}

	public void setPermitirAlteracaoValorNotaAvaliacaoOnline(Boolean permitirAlteracaoValorNotaAvaliacaoOnline) {
		this.permitirAlteracaoValorNotaAvaliacaoOnline = permitirAlteracaoValorNotaAvaliacaoOnline;
	}
	
	public void verificarAvaliacaoOnlineMatriculaExistente(Integer codigoAvaliacaoOnline){
		try {
			setPermitirAlteracaoValorNotaAvaliacaoOnline(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().verificarAvaliacaoOnlineMatriculaExistente(codigoAvaliacaoOnline));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("questao", "Enunciado"));
		return itens;
	}
	
	public void limparCamposBusca() {
		getControleConsultaQuestao().setListaConsulta(new ArrayList<>());
		setValorConsulta("");
	}

	public String getValorConsulta() {
		if (valorConsulta == null) {
			valorConsulta = "questao";
		}
		return valorConsulta;
	}

	public void setValorConsulta(String valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public String getCampoConsulta() {
		if (campoConsulta == null) {
			campoConsulta = "";
		}
		return campoConsulta;
	}

	public void setCampoConsulta(String campoConsulta) {
		this.campoConsulta = campoConsulta;
	}
	
	public void validarSelecaoPoliticaSelecaoQuestaoEnum() {
		if (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)) {
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO);
		}
	}
	
	public boolean getIsNaoPermitirAlterarPoliticaSelecaoQuestaoEnum() {
		return getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE);
	}
	
	public void montarListaDeNotasDaConfiguracaoAcademico() {
		try {
			getListaSelectItemVariavelNotaCfaVOs().clear();
			getListaSelectItemVariavelNotaCfaVOs().addAll(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarVariavelTituloConfiguracaoAcademicoEAvaliacaoOnline(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemVariavelNotaCfaVOs() {
		if (listaSelectItemVariavelNotaCfaVOs == null) {
			listaSelectItemVariavelNotaCfaVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemVariavelNotaCfaVOs;
	}

	public void setListaSelectItemVariavelNotaCfaVOs(List<SelectItem> listaSelectItemVariavelNotaCfaVOs) {
		this.listaSelectItemVariavelNotaCfaVOs = listaSelectItemVariavelNotaCfaVOs;
	}
	
	public String getValidarExcluirConteudoUnidadePaginaIndice() {
		try {
			ConteudoUnidadePaginaVO obj = (ConteudoUnidadePaginaVO) getRequestMap().get("pagina");
			if (Uteis.isAtributoPreenchido(obj)) {
				Integer quantidadeRegistrosPorConteudoUnidadePagina = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarQuantidadeRegistrosPorConteudoUnidadePaginaUnidadeConteudo(obj.getCodigo(), 0);
				if (Uteis.isAtributoPreenchido(quantidadeRegistrosPorConteudoUnidadePagina)) {
					return "Existem " + quantidadeRegistrosPorConteudoUnidadePagina + " registros de acesso dos alunos a esta Página. Estes registros também serão excluídos. Confirmar exclusão?";
				}
				return UteisJSF.internacionalizar("msg_confirmacao_excluxao");
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public String getValidarExcluirUnidadeConteudo() {
		try {
			UnidadeConteudoVO obj = (UnidadeConteudoVO) getRequestMap().get("unidade");
			if (Uteis.isAtributoPreenchido(obj)) {
				Integer quantidadeRegistrosPorUnidadeConteudo = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarQuantidadeRegistrosPorConteudoUnidadePaginaUnidadeConteudo(0, obj.getCodigo());
				if (Uteis.isAtributoPreenchido(quantidadeRegistrosPorUnidadeConteudo)) {
					return "Existem " + quantidadeRegistrosPorUnidadeConteudo + " registros de acesso dos alunos a esta Unidade. Estes registros também serão excluídos. Confirmar exclusão?";
				}
				return UteisJSF.internacionalizar("msg_confirmacao_excluxao");
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public String getValidarExcluirConteudoUnidadePagina() {
		try {
			if (!getConteudoUnidadePagina().isNovoObj()) {
				Integer quantidadeRegistrosPorUnidadeConteudo = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarQuantidadeRegistrosPorConteudoUnidadePaginaUnidadeConteudo(getConteudoUnidadePagina().getCodigo(), 0);
				if (Uteis.isAtributoPreenchido(quantidadeRegistrosPorUnidadeConteudo)) {
					return "Existem " + quantidadeRegistrosPorUnidadeConteudo + " registros de acesso dos alunos a esta Página. Estes registros também serão excluídos. Confirmar exclusão?";
				}
				return UteisJSF.internacionalizar("msg_confirmacao_excluxao");
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public String getCaminhoRedirecionamentoNavegacao(String pagina) {
		if (Uteis.isAtributoPreenchido(getIdControlador().replace("ConteudoControle_", ""))) {
			return Uteis.getCaminhoRedirecionamentoNavegacao(pagina, getIdControlador());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(pagina);
	}
	
	public boolean getVisualizacaoConteudoProfessor() {
		return !getUsuarioLogado().getIsApresentarVisaoAluno();
	}
	
	
	public void inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno() {
		try {
			setOncompleteModal("");
			limparMensagem();
			setSimulandoAvaliacao(true);
			setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getDisciplinaVO().setCodigo(getConteudo().getDisciplina().getCodigo());
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getConteudoVO().setCodigo(getConteudo().getCodigo());		
			getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setNome(getConteudoUnidadePaginaRecursoEducacional().getTitulo());
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().inicializarDadosSimulacaoVisualizacaoAvaliacaoOnlineAluno(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado());
			if(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
				realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(true);
			}else {			
				if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno(Uteis.getAnoDataAtual());
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(Uteis.getSemestreAtual());
					montarListaSelectItemTurma();
				}
				setOncompleteModal("RichFaces.$('panelAddRecursoEducacional').hide();RichFaces.$('panelSimularAvaliacao').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Boolean simulandoAvaliacao;
	
	public Boolean getSimulandoAvaliacao() {
		if (simulandoAvaliacao == null) {
			simulandoAvaliacao = false;
		}
		return simulandoAvaliacao;
	}

	public void setSimulandoAvaliacao(Boolean simulandoAvaliacao) {
		this.simulandoAvaliacao = simulandoAvaliacao;
	}


	private List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoSimulacaoVOs;

	public List<AvaliacaoOnlineTemaAssuntoVO> getAvaliacaoOnlineTemaAssuntoSimulacaoVOs() {
		if (avaliacaoOnlineTemaAssuntoSimulacaoVOs == null) {
			avaliacaoOnlineTemaAssuntoSimulacaoVOs = new ArrayList<AvaliacaoOnlineTemaAssuntoVO>(0);
		}
		return avaliacaoOnlineTemaAssuntoSimulacaoVOs;
	}

	public void setAvaliacaoOnlineTemaAssuntoSimulacaoVOs(List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoSimulacaoVOs) {
		this.avaliacaoOnlineTemaAssuntoSimulacaoVOs = avaliacaoOnlineTemaAssuntoSimulacaoVOs;
	}

	public void realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(boolean fecharPanelAddRecursoEducacional) {
		try {
			setMostrarGabarito(false);
			limparMensagem();
			setOncompleteModal("");
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO(), getMatriculaPeriodoTurmaDisciplinaVO(), getAvaliacaoOnlineTemaAssuntoSimulacaoVOs(), getUsuarioLogado(), false));
			setApresentarResultadoAvaliacaoOnline(false);
			if (fecharPanelAddRecursoEducacional) {
				setOncompleteModal("RichFaces.$('panelAvaliacaoOnlineMatricula').show();RichFaces.$('panelSimularAvaliacao').hide();RichFaces.$('panelAddRecursoEducacional').hide()");
			} else {
				setOncompleteModal("RichFaces.$('panelAvaliacaoOnlineMatricula').show();RichFaces.$('panelSimularAvaliacao').hide()");
			}
		} catch (Exception e) {
			setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno() {
		realizarSimulacaoVisualizacaoAvaliacaoOnlineAluno(false);
	}
	
	private List<SelectItem> listaSelectItemProfessorSimularAvaliacao;
	
	public List<SelectItem> getListaSelectItemProfessorSimularAvaliacao() {
		if(listaSelectItemProfessorSimularAvaliacao == null) {
			listaSelectItemProfessorSimularAvaliacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessorSimularAvaliacao;
	}

	public void setListaSelectItemProfessorSimularAvaliacao(List<SelectItem> listaSelectItemProfessorSimularAvaliacao) {
		this.listaSelectItemProfessorSimularAvaliacao = listaSelectItemProfessorSimularAvaliacao;
	}

	public void consultarDefinicoesTutoriaOnlineTurmaDisciplina() {
		getListaSelectItemProfessorSimularAvaliacao().clear();
		if(getIsApresentarOpcaoSelecaoProfessorSimulacaoAvaliacao()){
			try {
				getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setCodigo(0);
				getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setNome("");
				DefinicoesTutoriaOnlineEnum definicoes = getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo());
				if (definicoes == null || !definicoes.isProgramacaoAula()) {
					ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarProgramacaoTutoriaOnlinePorTurmaDisciplinaAnoSemestre(getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getUsuarioLogado());
					if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO)) {
						List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs =  getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						for(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO: programacaoTutoriaOnlineProfessorVOs) {
							getListaSelectItemProfessorSimularAvaliacao().add(new SelectItem(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), programacaoTutoriaOnlineProfessorVO.getProfessor().getNome()));
							if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getProfessor())) {
								getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setCodigo(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
								getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setNome(programacaoTutoriaOnlineProfessorVO.getProfessor().getNome());
							}
						}
					}
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	public Boolean getIsApresentarOpcaoSelecaoProfessorSimulacaoAvaliacao() {
			
			return (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma()) 
			&& Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina()) 
			&& ((getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getAnual() && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getAno()))
				||(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getSemestral() && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getAno()) && Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getSemestre()))
				|| (getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIntegral()))
			&& !getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getUsoExclusivoProfessor() && getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getRandomizarApenasQuestoesCadastradasPeloProfessor()
			&& !getUsuarioLogado().getIsApresentarVisaoProfessor());
		
	} 
	
	public void realizarVerificacaoQuestaoUnicaEscolhaAvalicaoOnline() {
		try {
			AvaliacaoOnlineMatriculaRespostaQuestaoVO orq = (AvaliacaoOnlineMatriculaRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (orq.getMarcada()) {
				orq.setMarcada(false);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarCalculoQuantidadePerguntasRespondidas(getAvaliacaoOnlineMatriculaVO());
			} else {
				orq.setMarcada(true);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVerificacaoQuestaoUnicaEscolha(getAvaliacaoOnlineMatriculaVO(), orq, getUsuarioLogado());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarCorrecaoAvaliacaoOnline() {
		realizarFinalizacaoAvaliacaoOnline(true);
	}

	public void realizarFinalizacaoAvaliacaoOnline(boolean validarPerguntasRespondidas) {
		try {
			getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().executarCorrecaoAvaliacaoOnline(getAvaliacaoOnlineMatriculaVO(), null, validarPerguntasRespondidas, getUsuarioLogado(), true);
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setApresentarResultadoAvaliacaoOnline(true);
			setMostrarGabarito(true);
			
		} catch (ConsistirException e) {
			setApresentarResultadoAvaliacaoOnline(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarResultadoAvaliacaoOnline(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public Boolean mostrarGabarito;
	
	public Boolean getMostrarGabarito() {
		if (mostrarGabarito == null) {
			mostrarGabarito = false;
		}
		return mostrarGabarito;
	}

	public void setMostrarGabarito(Boolean mostrarGabarito) {
		this.mostrarGabarito = mostrarGabarito;
	}
	
	private Boolean apresentarResultadoAvaliacaoOnline;
	public Boolean getApresentarResultadoAvaliacaoOnline() {
		if (apresentarResultadoAvaliacaoOnline == null) {
			apresentarResultadoAvaliacaoOnline = false;
		}
		return apresentarResultadoAvaliacaoOnline;
	}

	public void setApresentarResultadoAvaliacaoOnline(Boolean apresentarResultadoAvaliacaoOnline) {
		this.apresentarResultadoAvaliacaoOnline = apresentarResultadoAvaliacaoOnline;
	}
	
	
	public Integer getTotalQuestoes() {
		return getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size();
	}


	public String getAbrirModalFinalizacaoAvaliacaoOnline() {
		if (getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino().compareTo(new Date()) <= 0 && getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
			return "RichFaces.$('panelFinalizacao').show()";
		}
		return "";
	}

	public String getTempoApresentar() {
		return "Faltam " + Uteis.pegarTempoEntreDuasDatas(getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino(), new Date());
	}

	public boolean isHabilitarTempo() {
		return getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO) && getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual() > 0;
	}

	public Long tempoRestanteAvaliacaoOnline;
	public Long getTempoRestanteAvaliacaoOnline() {
		if (tempoRestanteAvaliacaoOnline == null) {
			tempoRestanteAvaliacaoOnline = getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual();
		}
		return tempoRestanteAvaliacaoOnline;
	}

	public void setTempoRestanteAvaliacaoOnline(Long tempoRestanteAvaliacaoOnline) {
		this.tempoRestanteAvaliacaoOnline = tempoRestanteAvaliacaoOnline;
	}
	
	public long getDataAtual() {
		return new Date().getTime();
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = null;
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
				getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
			} else {
				if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma())) {
					getFacadeFactory().getTurmaFacade().carregarDados(getMatriculaPeriodoTurmaDisciplinaVO().getTurma(), NivelMontarDados.TODOS, getUsuarioLogado());
				} else {
					getMatriculaPeriodoTurmaDisciplinaVO().setTurma(new TurmaVO());
				}
				obj = getMatriculaPeriodoTurmaDisciplinaVO().getTurma();
			}

			getMatriculaPeriodoTurmaDisciplinaVO().setTurma(obj);
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO().getTurma())) {
				if (!obj.getIntegral() && getMatriculaPeriodoTurmaDisciplinaVO().getAno().isEmpty()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno(Uteis.getAnoDataAtual());
				}
				if (obj.getSemestral() && getMatriculaPeriodoTurmaDisciplinaVO().getSemestre().isEmpty()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(Uteis.getSemestreAtual());
				}
				if (obj.getIntegral()) {
					getMatriculaPeriodoTurmaDisciplinaVO().setAno("");
					getMatriculaPeriodoTurmaDisciplinaVO().setSemestre("");
				}
				consultarDefinicoesTutoriaOnlineTurmaDisciplina();
			}

			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoDisciplina(getValorConsultaTurma(), 0, getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public String campoConsultaTurma;
	public String getCampoConsultaTurma() {
		campoConsultaTurma = Optional.ofNullable(campoConsultaTurma).orElse("");
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String valorConsultaTurma;
	public String getValorConsultaTurma() {
		valorConsultaTurma = Optional.ofNullable(valorConsultaTurma).orElse("");
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> listaConsultaTurma;
	public List<TurmaVO> getListaConsultaTurma() {
		listaConsultaTurma = Optional.ofNullable(listaConsultaTurma).orElse(new ArrayList<>());
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}
	
	public List<SelectItem> listaSelectItemTurma;
	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}
	
	public void montarListaSelectItemTurma() {
		setSimulandoAvaliacao(false);
		List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
		try {					
			List<TurmaVO> listaResultado = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			listaResultado.stream().forEach(p -> {
				if (!mapAuxiliarSelectItem.contains(p.getCodigo())) {
					getListaSelectItemTurma().add(new SelectItem(p.getCodigo(), p.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(p.getCodigo());
				}
			});
			
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), true, "AT", getUnidadeEnsinoLogado().getCodigo(), 0, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, false, true, null, false, getConteudo().getDisciplina().getCodigo());
	}
	
	private void verificarPermissaoRecursoRandomizarQuestaoProfessor() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("IniciarAtivoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
			if(getTipoRecursoEducacional().isTipoRecursoExercicio()){
				getConteudoUnidadePaginaRecursoEducacional().getListaExercicio().setRandomizarApenasQuestoesCadastradasPeloProfessor(true);
			}else {
				getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().setRandomizarApenasQuestoesCadastradasPeloProfessor(true);
			}
			}
		} catch (Exception e) {
			
		}
	}
	
	private void verificarPermissaoDesabilitarRecursoRandomizarQuestaoProfessor() {
		try {
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(false);
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DesabilitarAlteracaoRecursoRandomizarQuestoesCadastradasProfessor", getUsuarioLogadoClone());
				setDesabilitarRecursoRandomizacaoQuestaoProfessor(true);
			}
		} catch (Exception e) {
			setDesabilitarRecursoRandomizacaoQuestaoProfessor(false);
		}
	}

	public Boolean desabilitarRecursoRandomizacaoQuestaoProfessor;
	public Boolean getDesabilitarRecursoRandomizacaoQuestaoProfessor() {
		if (desabilitarRecursoRandomizacaoQuestaoProfessor == null) {
			desabilitarRecursoRandomizacaoQuestaoProfessor = false;
		}
		return desabilitarRecursoRandomizacaoQuestaoProfessor;
	}

	public void setDesabilitarRecursoRandomizacaoQuestaoProfessor(Boolean desabilitarRecursoRandomizacaoQuestaoProfessor) {
		this.desabilitarRecursoRandomizacaoQuestaoProfessor = desabilitarRecursoRandomizacaoQuestaoProfessor;
	}
		
	
	public Boolean getPendenciaAvaliacaoOnlineRea() {
		if (pendenciaAvaliacaoOnlineRea == null) {
			pendenciaAvaliacaoOnlineRea = false;
		}
		return pendenciaAvaliacaoOnlineRea;
	}

	public void setPendenciaAvaliacaoOnlineRea(Boolean pendenciaAvaliacaoOnlineRea) {
		this.pendenciaAvaliacaoOnlineRea = pendenciaAvaliacaoOnlineRea;
	}
	

	
	public String getOncompletePanelAddRecursoEducacional() {
		if (OncompletePanelAddRecursoEducacional == null) {
			OncompletePanelAddRecursoEducacional = "";
		}
		return OncompletePanelAddRecursoEducacional;
	}

	public void setOncompletePanelAddRecursoEducacional(String oncompletePanelAddRecursoEducacional) {
		OncompletePanelAddRecursoEducacional = oncompletePanelAddRecursoEducacional;
	}
	

	
	public void limparTurma() {
		getMatriculaPeriodoTurmaDisciplinaVO().setTurma(new TurmaVO());
	}

	public String getOncompletePanelAvaliacaoOnlineMatricula() {
		if (getConteudoUnidadePaginaRecursoEducacional().getAvaliacaoOnlineVO().getTipoGeracaoProvaOnline().equals(TipoGeracaoProvaOnlineEnum.FIXO)) {
			return "RichFaces.$('panelAvaliacaoOnlineMatricula').hide(); RichFaces.$('panelAddRecursoEducacional').show()";
		}
		return "RichFaces.$('panelAvaliacaoOnlineMatricula').hide(); RichFaces.$('panelSimularAvaliacao').show()";
	}
	
	

	
	public boolean getIsDesativarCamposSeAtivo() {
		return getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.ATIVO) || getAvaliacaoOnlineMatriculaVO().getConfiguracaoEADVO().getSituacao().equals(SituacaoEnum.INATIVO);
	}
	
	public List<CalendarioAtividadeMatriculaVO> getCalendarioAtividadeMatriculaVOs() {
		if (calendarioAtividadeMatriculaVOs == null) {
			calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return calendarioAtividadeMatriculaVOs;
	}

	public void setCalendarioAtividadeMatriculaVOs(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs) {
		this.calendarioAtividadeMatriculaVOs = calendarioAtividadeMatriculaVOs;
	}

	public String editarVisualizarPagina() {
		editarPagina();
		return emularAcessoPagina();
	}
	
	public List<String> getListaImagensSlidePagina(){
		return getConteudoUnidadePagina().getListaImagensSlide(getConfiguracaoGeralPadraoSistema());
	}
	
	public List<String> getListaImagensSlideREA(){
		return getConteudoUnidadePaginaRecursoEducacional().getListaImagensSlide(getConfiguracaoGeralPadraoSistema());
	}
	
	
	public String getScrollMenu() {
		return "scrollTOMenu('div.pagina"+getUnidadeConteudoVO().getCodigo()+""+getConteudoUnidadePagina().getCodigo()+"');";
	}
	
	public void consultarPercentualEstudado() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && getVisaoAlunoControle() != null) {
		Map<String, Object> auxiliar = new HashMap<String, Object>();
		getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, getConteudo().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina(), null);
		if (Uteis.isAtributoPreenchido(auxiliar.get("percentARealizar"))) {
			getConteudo().setPercentARealizar(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentARealizar")));
		}
		if (Uteis.isAtributoPreenchido(auxiliar.get("percentEstudado"))) {
			getConteudo().setPercentEstudado(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado")));
		}
		if (Uteis.isAtributoPreenchido(auxiliar.get("percentAtrasado"))) {
			getConteudo().setPercentAtrasado(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentAtrasado")));
		}
			}
		}catch(Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
}
