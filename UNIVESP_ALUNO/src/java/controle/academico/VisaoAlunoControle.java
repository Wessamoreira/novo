package controle.academico;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpSession;

import negocio.comuns.academico.*;
import negocio.facade.jdbc.academico.Matricula;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import controle.administrativo.ComunicacaoInternaControle;
import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.estagio.EstagioObrigatorioControle;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;

import negocio.comuns.basico.AreaProfissionalInteresseContratacaoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.SituacaoMilitarEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.MonitorConhecimentoVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.estagio.DashboardEstagioVO;
//import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
//import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.job.RegistroExecucaoJobVO;

import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.CalendarDataModelImpl;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoTranstornosNeurodivergentes;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.DocumentoIntegralizacaoCurricularRelControle;
import relatorio.controle.academico.GradeCurricularAlunoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import webservice.servicos.AplicativoSEISV;
import webservice.servicos.QRCodeLoginRSVO;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;
import org.springframework.web.context.annotation.SessionScope;


import static java.lang.Math.max;
import static negocio.comuns.utilitarias.Uteis.calcularSomaSemestres;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Otimize-TI
 */
@Component("VisaoAlunoControle") 
@Scope("view")
@Lazy
public class VisaoAlunoControle extends SuperControleRelatorio implements Serializable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean menuRecado;
	private Boolean menuMeusAmigos;
	private Boolean menuMeusProfessores;
	private Boolean menuConfiguracao;
	private Boolean menuDadosPessoais;
	private Boolean menuMinhasNotas;
	private Boolean menuSenha;
	private Boolean configuracao;
	private Boolean disciplinaEditar;
	private Boolean disciplinaConsultar;
	private Boolean menuDisciplina;
	private Boolean menuMeusHorarios;
	private Boolean menuRequerimento;
	private Boolean menuMinhasContasAPagar;
	private Boolean consultaRequerimento;
	private Boolean menuRenovacaoMatricula;
	private Boolean menuNovaMatricula;
	private Boolean novoRequerimento;
	private Boolean novoComunicado;
	private Boolean lerComunicado;
	private Boolean downloadArquivos;
	private Boolean planoDeEstudoAluno;
	private Boolean ambienteVisaoAluno;
	private boolean apresentarDashboardEstagio = false;
	protected Boolean submeterPagina = true;
	private Boolean apresentarBotaoReposicao = false;
	private Boolean apresentarImprimir = true;
	private Boolean apresentarContratos;

	protected List<DocumetacaoMatriculaVO> documetacaoMatriculaVOS;

	private FormacaoAcademicaVO formacaoAcademicaVO;
	private PessoaVO pessoaVO;
	private List<HistoricoVO> historicoVOs;

	private MatriculaPeriodoVO matriculaPeriodoVO;
	private List<SelectItem> listaSelectItemMatriculaCursoTurnoTopoAluno;

	private MatriculaVO matricula;
	private CursoVO cursoVO;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private String semestre;
	private String ano;
	private String mes;

	protected List<CidadeVO> listaConsultaCidade;
	private String campoConsultaCidade;
	private String valorConsultaCidade;

	private List<List<HistoricoVO>> listaPrincipalHistoricos;
	private DadosComerciaisVO dadosComerciaisVO;
	private FormacaoExtraCurricularVO formacaoExtraCurricularVO;
//	private AreaProfissionalVO areaProfissionalVO;
	protected List<SelectItem> listaSelectItemAreaProfissional;
	private Boolean mostrarBotoesGravarGeral;
	private QuestionarioVO questionarioVO;
	private QuestionarioAlunoVO questionarioAlunoVO;
	private Boolean permitirAlterarUsername;
	private Boolean existeProcessoMatriculaAberto;
	private List<SelectItem> listaDeAlunosPorResponsavelLegal;
	private UsuarioVO alunoPorResponsavelLegal;
	private int controlePanel = 0;
	private String abaApresentar;
	private Integer qtdeAtualizacaoForum;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaOnlineVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs;
	private List<SelectItem> listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	private Integer qtdeAtualizacaoDuvidaProfessor;
	private List<List<HistoricoVO>> listaHistoricoDisciplinasReprovadas;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private String mensagemAviso;
	private Boolean apresentarMenuTCC;
	private DocumetacaoMatriculaVO documetacaoMatriculaVO;
	private String nomeArquivo;
	private Integer qtdeNovidadesTCC;
	private Boolean isPrimeiroAcessoAlunoTCC;

	private Boolean meusDadosAbaDadosBasicos;
	private Boolean meusDadosAbaFormacaoAcademica;
	private Boolean meusDadosAbaExperienciasProfissionais;
	private Boolean meusDadosAbaFormacaoExtraCurricular;

	private List<MatriculaPeriodoVO> matriculaPeriodoContratoVOs;
	private List<AdvertenciaVO> advertenciaVOs;
	private AdvertenciaVO advertenciaVO;
	private CalendarDataModelImpl calendarioAtividadeMatriculaEstudoOnline;
	private String modalPanelErroAcessoConteudoEstudo;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO;
	private Integer qtdeAvaliacoesOnlineDisponiveis;
	private Integer qtdeInteracoesAtividadeDiscursiva;
	private Integer qtdeInteracoesAtividadeDiscursivaOnline;
	private MonitorConhecimentoVO monitorConhecimentoVO;

	private List<SelectItem> listaSelectItemAnoSemestre;
	private String filterAnoSemestre;
	private Boolean permitiAlterarDados;
	private Boolean apresentarModalResetarSenha;
	private boolean apresentarMinhasNotasPbl;
	private Boolean bloquearMenuVisaoAluno;
	protected List<SelectItem> listaSelectItemNacionalidade;
	private String campoConsultaNaturalidade;
	private String valorConsultaNaturalidade;
	private List<CidadeVO> listaConsultaNaturalidade;
	private FiliacaoVO filiacaoVO;
	private Boolean mostrarSelectCursos;
	private List<SelectItem> listaPessoasEditaveis;
	private Boolean meusDadosAbaQualificaoesInformacoesComplementares;
	private Boolean meusDadosAbaObjetivo;
	private Boolean meusDadosAbaQuestionario;
	private Boolean apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula;
	private String mensagemAlertaAlunoPendenciaDocumentacaoMatricula;
	private CalendarioHorarioAulaVO<DataEventosRSVO> calendarioDataEventoRsVO;
	private Boolean matriculaCalouro;
	private Integer quantidadeAtividadeComplementar;
	private Integer totalInteracaoNaoLida;
	private Boolean apresentarCampoProfessor;
	private String abrirModalInclusaoArquivoVerso;
	private FiliacaoVO filiacaoEdicaoVO;
	private DashboardVO dashboardMinhasDisciplinas;
	private DashboardVO dashboardCalendario;
	private DashboardVO dashboardEvolucaoAcademica;
	private DashboardVO dashboardEstagio;
	private DashboardVO dashboardAtividadeComplementar;
	private DashboardVO dashboardVagaEmprego;
	private DashboardVO dashboardMapaEstagio;
	private DashboardVO dashboardTcc;
	private DashboardVO dashboardProjetoIntegrador;
	private DashboardVO dashboardIntegralizacaoCurricular;

	private DashboardVO dashboardMonitorConhecimentoEAD;
	private DashboardVO dashboardEvolucaoEstudoOnline;
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoTcc;
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoProjetoIntegrador;
	private Boolean avaliacaoOnlineEmRealizacao;
	private Boolean realizarNavegacaoParaAvaliacaoOnline;
	private CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnlineEmRealizacao;
	private Integer frozenColumnsMinhasNotasAluno;
	private Integer totalCargaHorariaPeriodoLetivoExigida;
	private Integer totalCargaHorariaPeriodoLetivoCumprida;
	private Boolean apresentarModalPendenciaContratoAssinado;
	private DocumentoAssinadoVO documentoAssinado;
	private String caminhoPreviewContrato;
	private DocumentoAssinadoPessoaVO documentoAssinadoPessoa;
	private Boolean apresentarModalDocumentoAssinado;
	private Boolean apresentarModalContratoCertsign;
	private String actionSistemasProvasMestreGR;					
    private String loginSistemasProvasMestreGR;
    private String keySistemasProvasMestreGR;
    private String tokenSistemasProvasMestreGR;
    private String headerBarIntegracaoSistemasProvaMestreGR;
    private Boolean isSistemaProvasHabilitado;
    private Boolean aptoAcessarSistemaProvasHabilitado;
    private Boolean alunoNaoAssinouAtaColacaoGrau;
    private Boolean apresentarModalPendenciaAtaColacaoGrauAssinado;
    private String caminhoPreviewDocumentoAta;
    private DocumentoAssinadoVO documentoAssinadoAtaColacaoGrau;
    private DocumentoAssinadoPessoaVO documentoAssinadoPessoaAtaColacaoGrau;
    private Boolean permitirUploadAnexo;
    private MatriculaIntegralizacaoCurricularVO matriculaIntegralizacaoCurricularVO;
    
    private DocumentoAssinadoVO documentoAssinadoTermoEstagioNaoObrigatorio;
    private DocumentoAssinadoVO documentoAssinadoTermoEstagioObrigatorio;
    private DocumentoAssinadoPessoaVO documentoAssinadoPessoaTermoEstagioNaoObrigatorio;
    private DocumentoAssinadoPessoaVO documentoAssinadoPessoaTermoEstagioObrigatorio;
    private Boolean apresentarModalPendenciaTermoEstagioNaoObrigatorio;
    private Boolean apresentarModalPendenciaTermoEstagioObrigatorio;
    private String caminhoPreviewTermoEstagioNaoObrigatorio;
	private String corDoTextoJubilamento;
	private PeriodoLetivoVO periodoLetivoVO;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private boolean apresentarDashboardIntegralizacao = false;

	public static final String TEXTO_VERDE = "texto-green";
	public static final String TEXTO_AMARELO = "texto-yellow";
	public static final String TEXTO_VERMELHO = "texto-red";

	public VisaoAlunoControle() {
		super();
	}
	
	@PostConstruct
    public void init() { 
        try {
            setMensagemID("msg_entre_prmrelatorio");
            getFacadeFactory().getPessoaFacade().setIdEntidade("Aluno");
            setApresentarImprimir(false);
            setSubmeterPagina(Boolean.TRUE);
            setMatricula(new MatriculaVO());
            setHistoricoVOs(new ArrayList<HistoricoVO>(0));
            if(getUsuarioLogado().getIsApresentarVisaoAluno()) {
                inicializarListasSelectItemTodosComboBox();
            }
            setDocumetacaoMatriculaVOS(new ArrayList<DocumetacaoMatriculaVO>(0));
            inicializarDadosFotoUsuario();
            inicializarMenuRecado();
            if(getUsuarioLogado().getIsApresentarVisaoPais()) {
                realizarVerificacaoSeUsuarioLogadoPossuiVisaoPais();
            }
            verificarPrimeiroAcessoAluno();
            verificarAlertarAlunoPendenciaDocumentacaoMatriculaLogar();
            setAbaApresentar("dbAba1");
            setAmbienteVisaoAluno(true);
            realizarVerificacaoUsuarioFacilitador();
            realizarCriacaoAcessoSistemasProvasMestreGR(Boolean.FALSE);
            verificarQuantidadeDownloadsNaoRealizadosDownloadVisaoAluno();
            consultarConfiguracaoDashboard();
        } catch (Exception e) {
            
            e.printStackTrace(); 
        }
    }
	

	private void verificarRequerimentoNaoLido() {
		try {
			setTotalInteracaoNaoLida(getFacadeFactory().getRequerimentoFacade().consultarTotalInteracaoNaoLida(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String inicializarTelaInicialVisaoEstudoOnline() {
		try {
			gerarDadosGraficoSituacaoEstudos();
			limparCalendarioAtividadeMatricula();
			listarCompromissosDiaSelecionado();
			consultarAlertaAvaliacaoOnlineDisponivel();
			consultarAlertaInteracoesAtividadeDiscursivaEstudoOnline();
			montarGraficoAproveitamentoAvaliacaoOnlineVisaoEstudoOnline();
			setApresentarMinhasNotasPbl(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarSeExisteAvaliacaoPblNoConteudoAtivoPorDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getUsuarioLogado()));
			setAmbienteVisaoAluno(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("estudoOnlineForm.xhtml");
	}

	public String inicializarListaExercicioVisaoAlunoSelecionandoDisciplina() {
		context().getExternalContext().getSessionMap().put("disciplinaSelecionada", (MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem"));
		return inicializarListaExercicioVisaoAluno();
	}

	public String inicializarListaExercicioVisaoAluno() {
		try {
			removerControleMemoriaFlash("ListaExercicioControle");
			removerControleMemoriaTela("ListaExercicioControle");
			setAmbienteVisaoAluno(true);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioAlunoCons.xhtml");
	}

	public String inicializarListaExercicioEstudoOnline() {
		try {
			removerControleMemoriaFlash("ListaExercicioControle");
			removerControleMemoriaTela("ListaExercicioControle");
			setAmbienteVisaoAluno(false);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioAlunoCons.xhtml");
	}

	public void incializarDadosPessoa(UsuarioVO aluno) {
		try {
			setPessoaVO(aluno.getPessoa());
			setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getPessoaVO().getCodigo(), false, true, false, getUsuarioLogado()));
//			getPessoaVO().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(getPessoaVO().getCodigo()));
		} catch (Exception e) {
			setPessoaVO(new PessoaVO());
		}
	}

	public void alterarSenha() {
		try {
			executarValidacaoSimulacaoVisaoAluno();			
			getFacadeFactory().getUsuarioFacade().alterarSenha( getUsuarioLogado(), getLoginAnterior(), getSenhaAnterior(), getLogin(), getSenha(), isAlterarSenhaContaGsuite());
			getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(this.obterConfiguracaoLdapPorMatricula(), getUsuarioLogado(), getSenha());
			setMensagemID("msg_dados_gravados");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private ConfiguracaoLdapVO obterConfiguracaoLdapPorMatricula() throws Exception {
		CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuario());
		return getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorCodigo(curso.getConfiguracaoLdapVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
	}

	public String editarDadosPessoa() throws Exception {
		try {
			setMensagemAviso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Editar Dados Pessoa", "Editando");
			if(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().contains(getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha()) && getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get(getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha()).getPessoa().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
				setPessoaVO(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get(getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha()).getPessoa());
			}else {
				setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, true, false, getUsuarioLogado()));
			}
//			getPessoaVO().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(getPessoaVO().getCodigo()));
			getPessoaVO().setNovoObj(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			inicializarMenuDadosPessoais();
			if (getPessoaVO().getQtdFilhos() > 0) {
				getPessoaVO().setPossuiFilho(true);
			}
			verificarPermissaoVisaoAlunoMeusDados();
			if (getUsuarioLogado().getIsApresentarVisaoPais() && !getUsuarioLogado().getPessoa().getCodigo().equals(getPessoaVO().getCodigo())) {
				getPessoaVO().limparDadosSegundoRegraAtualizacaoCadastral(getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO());
			} else if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				getPessoaVO().limparDadosSegundoRegraAtualizacaoCadastral(getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO());
			}

			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Finalizando Editar Dados Pessoa", "Editando");
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		}
	}

	public void editarDadosPessoaVagas() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Editar Dados Pessoa", "Editando");
			setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getPessoaVO().getCodigo(), false, true, false, getUsuarioLogado()));
//			getPessoaVO().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(getPessoaVO().getCodigo()));
			getPessoaVO().setNovoObj(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			inicializarMenuDadosPessoais();
			if (getPessoaVO().getQtdFilhos() > 0) {
				getPessoaVO().setPossuiFilho(true);
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Finalizando Editar Dados Pessoa", "Editando");
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String gravarDadosPessoa() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			setMostrarSelectCursos(Boolean.TRUE);
			getFacadeFactory().getPessoaFacade().setIdEntidade("Aluno");
			setMensagemAviso(getPessoaVO().validarPreenchimentoCurriculo(getPessoaVO()));
			getFacadeFactory().getPessoaFacade().validarDadosPessoaisVisaoAluno(getPessoaVO(), getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO());
			pessoaVO.setAluno(Boolean.TRUE);
			if (!getPessoaVO().getCurriculoAtualizado()) {
				if (getPessoaVO().getParticipaBancoCurriculum()) {
					ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
					PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
					if (responsavel.getCodigo() != 0) {
						enviarEmail(config, responsavel);
					}
					getPessoaVO().setCurriculoAtualizado(Boolean.TRUE);
				} else {
					getPessoaVO().setCurriculoAtualizado(Boolean.FALSE);
				}
			} else if (!getPessoaVO().getParticipaBancoCurriculum()) {
				getPessoaVO().setCurriculoAtualizado(Boolean.FALSE);
			}
			if (pessoaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPessoaFacade().incluir(pessoaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
			} else {
				pessoaVO.setDadosPessoaisAtualizado(Boolean.TRUE);
				pessoaVO.setDataUltimaAtualizacaoCadatral(new Date());
				pessoaVO.setRegistrarLogAtualizacaoCadastral(Boolean.TRUE);
				pessoaVO.inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "ALUNO");
				getFacadeFactory().getPessoaFacade().alterarSemFiliacao(pessoaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
				this.setBloquearMenuVisaoAluno(Boolean.FALSE);
			}
			setMensagemID("msg_dados_gravados");
			getUsuarioLogado().getPessoa().setDataUltimaAtualizacaoCadatral(pessoaVO.getDataUltimaAtualizacaoCadatral());			
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		}
	}

	private void enviarEmail(ConfiguracaoGeralSistemaVO config, PessoaVO responsavel) throws Exception {
		List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
		listaComunicadoInternoDestinatarioVO.clear();
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("SI");
		if (!getPessoaVO().getEmail().equals("")) {
			comunicadoInternoDestinatarioVO.setEmail(getPessoaVO().getEmail());
			comunicadoInternoDestinatarioVO.setNome(getPessoaVO().getNome());
			comunicadoInternoDestinatarioVO.getDestinatario().setNome(getPessoaVO().getNome());
			comunicadoInternoDestinatarioVO.getDestinatario().setEmail(getPessoaVO().getEmail());
			comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
		} else {
			comunicacaoInternaVO.setEnviarEmail(Boolean.FALSE);
		}
		comunicadoInternoDestinatarioVO.setDestinatario(getPessoaVO());
		listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
		comunicacaoInternaVO.setAssunto("Boas Vindas");
		comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
		comunicacaoInternaVO.setData(new Date());
		comunicacaoInternaVO.setTipoDestinatario("AL");
		comunicacaoInternaVO.setTipoRemetente("SI");
		comunicacaoInternaVO.setResponsavel(responsavel);
		comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
//		String corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("boasVindasAluno", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
//		if (!corpoMensagem.trim().isEmpty()) {
//			corpoMensagem = substituirTags(corpoMensagem, getPessoaVO());
//			comunicacaoInternaVO.setMensagem(corpoMensagem);
//			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), null);
//		}
	}

	private String substituirTags(String corpoMensagem, PessoaVO aluno) throws Exception {
		if (corpoMensagem.contains("#Titulo_BancoCurriculum")) {
			corpoMensagem = corpoMensagem.replaceAll("#Titulo_BancoCurriculum", getConfiguracaoGeralPadraoSistema().getTituloTelaBancoCurriculum());
		}
		if (corpoMensagem.contains("#Nome_Aluno")) {
			corpoMensagem = corpoMensagem.replaceAll("#Nome_Aluno", aluno.getNome());
		}
		if (corpoMensagem.contains("<!DOCTYPE")) {
			corpoMensagem = corpoMensagem.replace(corpoMensagem.substring(corpoMensagem.indexOf("<!DOCTYPE"), corpoMensagem.indexOf("<html>")), "");
		}
		return corpoMensagem;
	}

	public void validarDadosPeriodicidadeFormacaoAcademica(Date dataInicio, Date dataFim) throws Exception {
		// if (dataInicio.after(dataFim)) {
		// throw new
		// Exception("Data de Inï¿½cio superior a data de conclusï¿½o do
		// curso.");
		// }
	}

	public void adicionarFormacaoAcademica() throws Exception {
		try {
			validarDadosPeriodicidadeFormacaoAcademica(getFormacaoAcademicaVO().getDataInicio(), getFormacaoAcademicaVO().getDataFim());
			if (!getPessoaVO().getCodigo().equals(0)) {
				formacaoAcademicaVO.setPessoa(getPessoaVO().getCodigo());
			}
			Integer quantidadeFormacoesAcademicas = getPessoaVO().getFormacaoAcademicaVOs().size();
			getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
			if (getPessoaVO().getFormacaoAcademicaVOs().size() > quantidadeFormacoesAcademicas) {
				getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade("Pessoa");
				getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicas(getPessoaVO().getCodigo(), getPessoaVO().getFormacaoAcademicaVOs(), true, getUsuarioLogado());
			}
			this.setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF() + File.separator + getPessoaVO().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
			getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getUrlWebCam() {
		try {
			String url = request().getRequestURL().toString().substring(0, request().getRequestURL().toString().indexOf(request().getContextPath())) + request().getContextPath();
			return "webcam.freeze();webcam.upload('" + url + "/UploadServlet')";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
//			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());

		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaEsquerdaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireitaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180GrausVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomInVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void editarFormacaoAcademica() throws Exception {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
		setFormacaoAcademicaVO(obj);
	}

	public void removerFormacaoAcademica() throws Exception {
		try {
			FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademicaItens");
			getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
			getFacadeFactory().getFormacaoAcademicaFacade().setIdEntidade("Pessoa");
			getFacadeFactory().getFormacaoAcademicaFacade().alterarFormacaoAcademicas(getPessoaVO().getCodigo(), getPessoaVO().getFormacaoAcademicaVOs(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> listaSelectItemSituacaoFormacaoAcademica;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SelectItem> getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
		if (listaSelectItemSituacaoFormacaoAcademica == null) {
			listaSelectItemSituacaoFormacaoAcademica = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoFormacaoAcademica.add(new SelectItem("", ""));
			Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
			Enumeration keys = situacaoFormacaoAcademicas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) situacaoFormacaoAcademicas.get(value);
				listaSelectItemSituacaoFormacaoAcademica.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) listaSelectItemSituacaoFormacaoAcademica, ordenador);
		}
		return listaSelectItemSituacaoFormacaoAcademica;
	}

	public List<SelectItem> listaSelectItemTipoInstFormacaoAcademica;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SelectItem> getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
		if (listaSelectItemTipoInstFormacaoAcademica == null) {
			listaSelectItemTipoInstFormacaoAcademica = new ArrayList<SelectItem>(0);
			listaSelectItemTipoInstFormacaoAcademica.add(new SelectItem("", ""));
			Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
			Enumeration keys = tipoInstFormacaoAcademicas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoInstFormacaoAcademicas.get(value);
				listaSelectItemTipoInstFormacaoAcademica.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort(listaSelectItemTipoInstFormacaoAcademica, ordenador);
		}
		return listaSelectItemTipoInstFormacaoAcademica;
	}

	public String solicitarRequerimento() {
		executarMetodoControle("RequerimentoControle", "novoVisaoAlunoReposicao");
		return "";
	}

	public String selecionarCursoTopo() {
		removerControleMemoriaFlashTela("EstagioObrigatorioControle");
		return selecionarCursoTopo(true, true);
	}

	public String selecionarCursoTopo(Boolean validarAvaliacaoInstitucinal, Boolean verificacaoAtualizacaoCadastral) {
		existeProcessoMatriculaAberto = null;
		try {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(getMatricula().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			if(getAplicacaoControle().getMatriculaAlunoCache().containsKey(getMatricula().getMatricula()) && getAplicacaoControle().getMatriculaAlunoCache().get(getMatricula().getMatricula()) != null && Uteis.isAtributoPreenchido(getAplicacaoControle().getMatriculaAlunoCache().get(getMatricula().getMatricula()).getMatricula())) {
				setMatricula(getAplicacaoControle().getMatriculaAlunoCache().get(getMatricula().getMatricula()));
				setCorDoTextoJubilamento(getMatricula().getGradeCurricularAtual().getCorDoTextoJubilamento());
			}else {
				setMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatricula().getMatricula(), 0, false, getUsuarioLogado()));
				getMatricula().setMatriculaComHistoricoAlunoVO(getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(getMatricula(), getMatricula().getGradeCurricularAtual().getCodigo(), false, getMatricula().getCurso().getConfiguracaoAcademico(), getUsuarioLogado()));
				if (configuracaoGeralSistemaVO.getHabilitarRecursosAcademicosVisaoAluno()) {
					getMatricula().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(getMatricula().getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, getUsuarioLogadoClone()));
					getMatricula().setSalaAulaBlackboardTcc(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(getMatricula().getMatricula(), TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, getUsuarioLogadoClone()));
				}
				gerarPrazoJubilamentoMatricula(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
			}
			if(getAplicacaoControle().getMatriculaPeriodoAlunoCache().containsKey(getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(getAplicacaoControle().getMatriculaPeriodoAlunoCache().get(getMatricula().getMatricula()))) {
				setMatriculaPeriodoVO(getAplicacaoControle().getMatriculaPeriodoAlunoCache().get(getMatricula().getMatricula()));
			}else {
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getMatricula().getMatriculaComHistoricoAlunoVO().gerarDadosGraficoEvolucaoAcademicaAluno();
			carregarPercentuaisIntegralizacaoCurricularMatricula();
			getMatricula().getMatriculaComHistoricoAlunoVO().gerarDadosGraficoTcc(getMatriculaIntegralizacaoCurricularVO());
			getMatricula().getMatriculaComHistoricoAlunoVO().gerarDadosGraficoIntegralizacaoCurricular(getMatriculaIntegralizacaoCurricularVO());				
			getMatricula().setMatriculaPeriodoVO(getMatriculaPeriodoVO());
			getMatricula().setAlunoNaoAssinouContratoMatricula(false);
			setApresentarDashboardEstagio(getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getExisteGradeCurricularEstagio());
			getUsuarioLogado().setUnidadeEnsinoLogado(getMatricula().getUnidadeEnsino());
			setCursoVO(getMatricula().getCurso());
			setUnidadeEnsinoVO(getMatricula().getUnidadeEnsino());
			montarListaAnoSemestre();
			if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula()) && getMatricula().getAlunoNaoAssinouContratoMatricula()) {
					getUsuarioLogado().setPerfilAcesso(configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula());
				} else {
					getUsuarioLogado().setPerfilAcesso(executarVerificacaoPerfilAcessoSelecionarVisaoPais(configuracaoGeralSistemaVO));
				}
			} else {
				getUsuarioLogado().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(configuracaoGeralSistemaVO, getMatricula(), getMatricula().getAlunoNaoAssinouContratoMatricula(), getUsuarioLogado()));
			}
			getLoginControle().unidadeEnsinoVO = getMatricula().getUnidadeEnsino();
			getLoginControle().montarPermissoesMenu(getUsuarioLogado().getPerfilAcesso());
			getLoginControle().inicializarLogoApartirUsuario(getUsuarioLogadoClone());
			getLoginControle().setIsMinhaBibliotecaHabilitado(null);
			if (configuracaoGeralSistemaVO.getApresentarMensagemAlertaAlunoNaoAssinouContrato()) {
				getMatricula().setMensagemAlertaAlunoNaoAssinouContratoMatricula(configuracaoGeralSistemaVO.getMensagemAlertaAlunoNaoAssinouContratoMatricula());
			}
			setApresentarBotaoNovoRequerimento(null);
			consultarMatriculaPeriodoTurmaDisciplinaOnline();
			if (configuracaoGeralSistemaVO.getHabilitarRecursosAcademicosVisaoAluno()) {
				verificarApresentacaoMenuTCC();
				verificarDocumentoContratoAssinadoPendente();
				executarInicializacaoCalendarioAgrupamentoProjetoIntegrador();
			}
			
			resetarMemoriaFlash();
//			removerMemoriaAplicacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoAluno.xhtml");
	}

	private void executarInicializacaoCalendarioAgrupamentoProjetoIntegrador() {
		try {
			setDashboardProjetoIntegrador(null);
			setCalendarioAgrupamentoProjetoIntegrador(new CalendarioAgrupamentoTccVO());
			getCalendarioAgrupamentoProjetoIntegrador().setPeriodoLiberado(false);
			if(getAplicacaoControle().getCalendarioProjetoIntegradorCache().containsKey(getMatricula().getMatricula())) {
				setCalendarioAgrupamentoProjetoIntegrador(getAplicacaoControle().getCalendarioProjetoIntegradorCache().get(getMatricula().getMatricula()));
			}else {
				setCalendarioAgrupamentoProjetoIntegrador(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestrePorMatriculaPeriodoTurmaDisciplina(getListaMatriculaPeriodoTurmaDisciplinaVOs(), ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, getMatriculaPeriodoVO().getAno(), getMatriculaPeriodoVO().getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			}
			if(getMatriculaPeriodoVO().isSituacaoAtiva()
					&& Uteis.isAtributoPreenchido(getCalendarioAgrupamentoProjetoIntegrador()) 
					&& getListaMatriculaPeriodoTurmaDisciplinaVOs().stream().anyMatch(p-> p.getDisciplina().getClassificacaoDisciplina().isProjetoIntegrador() && p.getAno().equals(getMatriculaPeriodoVO().getAno()) && p.getSemestre().equals(getMatriculaPeriodoVO().getSemestre()))				
					&& UteisData.getCompareData(new Date(), getCalendarioAgrupamentoProjetoIntegrador().getDataInicial()) >= 0 
					&& UteisData.getCompareData(getCalendarioAgrupamentoProjetoIntegrador().getDataFinal(), new Date()) >= 0 ) {
				getCalendarioAgrupamentoProjetoIntegrador().setPeriodoLiberado(true);
			}
			getDashboardProjetoIntegrador();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void executarInicializacaoDadosEstagio() { }

	public String navegarTela() {
		if (getLoginControle().getOpcao().contains("avaliacaoInstitucionalQuestionario")) {
			return Uteis.getCaminhoRedirecionamentoNavegacao(getLoginControle().getOpcao());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getLoginControle().getOpcao());
	}

	public boolean getApresentarAvancar() {
		try {
			if (getAbaApresentar().equals("dbAba1")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba2")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba3")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba4") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba5") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba6") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba7") && getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba7") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba8") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba9") && getPessoaVO().getParticipaBancoCurriculum()) {
				return false;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void avancar() {
		try {
			if (getAbaApresentar().equals("dbAba1")) {
				setAbaApresentar("dbAba2");
			} else if (getAbaApresentar().equals("dbAba2")) {
				setAbaApresentar("dbAba3");
			} else if (getAbaApresentar().equals("dbAba3")) {
				setAbaApresentar("dbAba4");
			} else if (getAbaApresentar().equals("dbAba4") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba5");
			} else if (getAbaApresentar().equals("dbAba5") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba6");
			} else if (getAbaApresentar().equals("dbAba6") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba7");
			} else if (getAbaApresentar().equals("dbAba7") && getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba8");
			} else if (getAbaApresentar().equals("dbAba7") && !getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba9");
			} else if (getAbaApresentar().equals("dbAba8") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba9");
			} else if (getAbaApresentar().equals("dbAba9") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba9");
			}
		} catch (Exception e) {
			setAbaApresentar("dbAba1");
		}
	}

	public boolean getApresentarVoltar() {
		try {
			if (getAbaApresentar().equals("dbAba1")) {
				return false;
			} else if (getAbaApresentar().equals("dbAba2")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba3")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba4")) {
				return true;
			} else if (getAbaApresentar().equals("dbAba5") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba6") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba7") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba8") && getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			} else if (getAbaApresentar().equals("dbAba9") && getPessoaVO().getParticipaBancoCurriculum()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void voltar() {
		try {
			if (getAbaApresentar().equals("dbAba1")) {
				setAbaApresentar("dbAba1");
			} else if (getAbaApresentar().equals("dbAba2")) {
				setAbaApresentar("dbAba1");
			} else if (getAbaApresentar().equals("dbAba3")) {
				setAbaApresentar("dbAba2");
			} else if (getAbaApresentar().equals("dbAba4")) {
				setAbaApresentar("dbAba3");
			} else if (getAbaApresentar().equals("dbAba5") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba4");
			} else if (getAbaApresentar().equals("dbAba6") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba5");
			} else if (getAbaApresentar().equals("dbAba7") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba6");
			} else if (getAbaApresentar().equals("dbAba8") && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba7");
			} else if (getAbaApresentar().equals("dbAba9") && getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba8");
			} else if (getAbaApresentar().equals("dbAba9") && !getVerificarQuestionarioAlunoBancoCurriculum() && getPessoaVO().getParticipaBancoCurriculum()) {
				setAbaApresentar("dbAba7");
			}
		} catch (Exception e) {
			setAbaApresentar("dbAba1");
		}
	}

	public String verArquivoDisciplinaTurmaSegunda() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.inicializarDadosVisaoAluno();
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaSegunda());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaSegunda());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaTerca() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.inicializarDadosVisaoAluno();
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaTerca());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaTerca());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaQuarta() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.inicializarDadosVisaoAluno();
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaQuarta());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaQuarta());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaQuinta() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.inicializarDadosVisaoAluno();
			arquivoControle.setArquivoVO(new ArquivoVO());
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaQuinta());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaQuinta());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaSexta() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.inicializarDadosVisaoAluno();
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaSexta());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaSexta());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaSabado() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.inicializarDadosVisaoAluno();
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaSabado());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaSabado());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaDomingo() {
		try {
//			DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO = (DisponibilidadeHorarioAlunoVO) context().getExternalContext().getRequestMap().get("horarioProgramacao");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.inicializarDadosVisaoAluno();
			arquivoControle.setArquivoVO(new ArquivoVO());
//			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplinaDomingo());
//			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurmaDomingo());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String verArquivoDisciplinaTurmaDia() {
		try {
			HorarioAlunoDiaItemVO disponibilidadeHorarioAlunoVO = (HorarioAlunoDiaItemVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItem");
			ArquivoControle arquivoControle = (ArquivoControle) getControlador("ArquivoControle");
			arquivoControle.inicializarDadosVisaoAluno();
			arquivoControle.setArquivoVO(new ArquivoVO());
			arquivoControle.setDisciplinaVO(disponibilidadeHorarioAlunoVO.getDisciplina());
			arquivoControle.setTurmaVO(disponibilidadeHorarioAlunoVO.getTurma());
			arquivoControle.montarListaDisciplinaTurmaVisaoAluno();
			arquivoControle.consultarArquivosPorDisciplina();
			return "downloadAluno";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public List<SelectItem> listaSelectItemEscolaridadeFormacaoAcademica;

	public List<SelectItem> getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		if (listaSelectItemEscolaridadeFormacaoAcademica == null) {
			listaSelectItemEscolaridadeFormacaoAcademica = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, true);
		}
		return listaSelectItemEscolaridadeFormacaoAcademica;
	}

	public List<SelectItem> listaSelectItemEstadoCivilPessoa;

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemEstadoCivilPessoa() throws Exception {
		if (listaSelectItemEstadoCivilPessoa == null) {
			listaSelectItemEstadoCivilPessoa = new ArrayList<SelectItem>(0);
			listaSelectItemEstadoCivilPessoa.add(new SelectItem("", ""));
			Hashtable<String, String> estadoCivils = (Hashtable<String, String>) Dominios.getEstadoCivil();
			Enumeration<String> keys = estadoCivils.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) estadoCivils.get(value);
				listaSelectItemEstadoCivilPessoa.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort(listaSelectItemEstadoCivilPessoa, ordenador);
		}
		return listaSelectItemEstadoCivilPessoa;
	}

	public void carregarEnderecoPessoaFiliacao() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getFiliacaoVO().getPais(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarQuestionarioAlunoBancoCurriculum() throws Exception {
//		try {			
//			montarListaDeQuestoesBancoCurriculum(
//					getFacadeFactory().getQuestionarioFacade().consultarUltimoQuestionarioPorEscopoSituacao("BC", "AT",
//							false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
		}

	public Boolean getVerificarQuestionarioAlunoBancoCurriculum() throws Exception {
//		try {
//			if (getQuestionarioVO() != null && getQuestionarioVO().getCodigo() != null
//					&& getQuestionarioVO().getCodigo().intValue() != 0) {
//				return true;
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
		return false;
	}

	public void montarListaDeQuestoesBancoCurriculum(QuestionarioVO obj) throws Exception {
		if (obj != null) {
			setQuestionarioVO(obj);
			setQuestionarioAlunoVO(getFacadeFactory().getQuestionarioAlunoFacade().consultarPorQuestionarioAluno(getQuestionarioVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (getQuestionarioAlunoVO().getCodigo().intValue() != 0) {
				realizarAlteracaoRespostasRespondidas();
			}
		}
	}

	public void gravarQuestionarioAlunoBancoCurriculum() throws Exception {
		try {
			getFacadeFactory().getQuestionarioAlunoFacade().montarRespostasQuestionarioAluno(getQuestionarioVO(), getQuestionarioAlunoVO(), getUsuarioLogado().getPessoa());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarAlteracaoRespostasRespondidas() throws Exception {
		getFacadeFactory().getQuestionarioFacade().montarQuestionarioRespostasDoQuestionarioAluno(getQuestionarioVO(), getQuestionarioAlunoVO());
	}

//	@SuppressWarnings("unchecked")
//	public List<AreaConhecimentoVO> consultarAreaConhecimentoPorNome(String nomePrm) throws Exception {
//		List<AreaConhecimentoVO> lista = getFacadeFactory().getAreaConhecimentoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
//		return lista;
//	}

	public void imprimirContrato() {

		try {
			setOncompleteModal("");
			setCaminhoRelatorio("");
			setCaminhoPreviewContrato("");
			MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoContratoVOItens");
//			ConfiguracaoFinanceiroVO config = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
			DocumentoAssinadoVO documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(matriculaPeriodoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
				setDocumentoAssinado(documentoAssinadoVO);				
				if(documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(documentoAssinadoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
					getDocumentoAssinado().getListaDocumentoAssinadoPessoa().stream().forEach(
							documentoAssinadoPessoa -> {
								if (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO)) {
									setDocumentoAssinadoPessoa(documentoAssinadoPessoa);									
								}
							}
					);
					if(abrirModalContratoCertisign().isEmpty()) {
						setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+documentoAssinadoVO.getArquivo().getPastaBaseArquivo().replace("\\", "/")+"/" + documentoAssinadoVO.getArquivo().getNome());
						setApresentarModalDocumentoAssinado(Boolean.TRUE);
					}
				}
				if(documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(documentoAssinadoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
					getDocumentoAssinado().getListaDocumentoAssinadoPessoa().stream().forEach(
							documentoAssinadoPessoa -> {
								if (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO)) {
								}
								setDocumentoAssinadoPessoa(documentoAssinadoPessoa);
							}
					);
					if(abrirModalContratoTechCert().isEmpty()) {
						setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+documentoAssinadoVO.getArquivo().getPastaBaseArquivo().replace("\\", "/")+"/" + documentoAssinadoVO.getArquivo().getNome());
						setApresentarModalDocumentoAssinado(Boolean.TRUE);
					}
				}
				else {
					setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + documentoAssinadoVO.getArquivo().getNome());
					setApresentarModalDocumentoAssinado(Boolean.TRUE);
				}
				
			}else {
//			  String caminho = getFacadeFactory().getImpressaoContratoFacade().imprimirContratoVisaoAluno(getMatricula(),matriculaPeriodoVO, config, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			  documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodoContratoAssinado(matriculaPeriodoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			  if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
				  if(documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
						getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(documentoAssinadoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
						setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+documentoAssinadoVO.getArquivo().getPastaBaseArquivo().replace("\\", "/")+"/" + documentoAssinadoVO.getArquivo().getNome());
						setApresentarModalDocumentoAssinado(Boolean.TRUE);
					}
				  if(documentoAssinadoVO.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
					  getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(documentoAssinadoVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
					  setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"+documentoAssinadoVO.getArquivo().getPastaBaseArquivo().replace("\\", "/")+"/" + documentoAssinadoVO.getArquivo().getNome());
					  setApresentarModalDocumentoAssinado(Boolean.TRUE);
				  }
				  else {
						setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + documentoAssinadoVO.getArquivo().getNome());
						setApresentarModalDocumentoAssinado(Boolean.TRUE);
					}
					setDocumentoAssinado(documentoAssinadoVO);				
			  }else {
//				  setCaminhoRelatorio(caminho);
			  }
			  
			  setDocumentoAssinado(null);
			  setApresentarModalDocumentoAssinado(Boolean.FALSE);
			}
			if (!Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
				setFazerDownload(false);
			}
			if (Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && getCaminhoRelatorio().contains(".pdf")) {
				setFazerDownload(true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getDownloadContrato() {
		String retorno = "";
		if (getApresentarModalDocumentoAssinado()) {
			retorno = "PF('panelDocumentoContratoAssinado').show()";			
		}else if (getFazerDownload()) {
			return getDownload();
		}else {
			retorno = abrirModalContratoCertisign();
		}
//		else {
//			retorno = "abrirPopup('../VisualizarContrato', 'RelatorioContrato', 730, 545);";
//		}
		
		return retorno;
		
	}	
	

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCursoTopoAluno(getUsuarioLogado());
	}

	public void montarConfiguracaoAcademico() throws Exception {
		if (!Uteis.isAtributoPreenchido(getMatricula().getCurso().getConfiguracaoAcademico().getCodigo())) {
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
		}
		setConfiguracaoAcademico(getAplicacaoControle().carregarDadosConfiguracaoAcademica(getMatricula().getCurso().getConfiguracaoAcademico().getCodigo()).clone());
	}

	public void montarListaHistoricoAluno(CursoVO curso) {
		try {

			setHistoricoVOs(new ArrayList<HistoricoVO>(0));
			/// montarListaSemestreAno();
			consultarNotasAluno();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setHistoricoVOs(new ArrayList<HistoricoVO>(0));
			setListaPrincipalHistoricos(new ArrayList<List<HistoricoVO>>(0));
		}
	}

	private void montarListaSemestreAno() throws Exception {
		getListaSelectItemAnoSemestre().clear();
		getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
		setFilterAnoSemestre("");
		if (getIsApresentarCampoAno()) {
			setFilterAnoSemestre(getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(getMatricula().getMatricula(), getListaSelectItemAnoSemestre()));
		}
	}

	public void consultarNotasAluno() {
		try {
//			Stopwatch stopwatch = new Stopwatch();
//			stopwatch.start();
			setHistoricoVOs(getFacadeFactory().getHistoricoFacade().executarMontagemListaHistoricoAluno(getMatricula(), 0, getConfiguracaoAcademico(), getMatricula().getCurso(),  getFilterAnoSemestre(), true, true, false, getUsuarioLogado()));
//			CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
//			getHistoricoVOs().stream().filter(HistoricoVO::getHistoricoDisciplinaAproveitada).forEach(h -> h.setCalendarioLancamentoNotaVO(calendarioLancamentoNotaVO));
			realizarMontagemListaHistoricoPrincipal();
			realizarMontagemTotalizadoresCargasHorarias();
//			stopwatch.stop();
//			System.out.println(stopwatch.getElapsed());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setHistoricoVOs(new ArrayList<HistoricoVO>(0));
			setListaPrincipalHistoricos(new ArrayList<List<HistoricoVO>>(0));
		}
	}

	public void montarListaHistoricoAluno() {
		montarListaHistoricoAluno(null);
	}

	public void visualizarFaltas() {
//		try {
//			setListaDetalhesMinhasFaltasVOs(new ArrayList<RegistroAulaVO>(0));
//			RegistroAulaVO reg = (RegistroAulaVO) context().getExternalContext().getRequestMap().get("faltasItens");
//			// MatriculaPeriodoVO matriculaPeriodo =
//			// getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaAnoSemestre(getMatricula().getMatricula(),
//			// getSemestre(), getAno(), false, getUsuarioLogado());
//			setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(reg.getTurma().getCodigo(), getMatricula().getMatricula(), reg.getDisciplina().getCodigo(), reg.getSemestre(), reg.getAno(), false, getUsuarioLogado()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void consultarMinhasFaltasAluno() {
		try {
//			montarListaAnoSemestre();
			consultarFaltasAluno();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFaltasAluno() {
//		try {
//			setListaFaltas(null);
//			setSemestre(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(getFilterAnoSemestre().indexOf("/") + 1, getFilterAnoSemestre().length()) : "");
//			setAno(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(0, getFilterAnoSemestre().indexOf("/")) : "");
//			List<RegistroAulaVO> registroAulaVOs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoQuantidade(0, getMatricula().getMatricula(), getSemestre(), getAno(), false, getUsuarioLogado());
//			Map<String, List<RegistroAulaVO>> map = new HashMap<String, List<RegistroAulaVO>>(0);
//			List<RegistroAulaVO> registroAulaSecundarioVOs = null;
//			for (RegistroAulaVO obj : registroAulaVOs) {
//				if (!map.containsKey(obj.getAnoSemestreApresentar())) {
//					registroAulaSecundarioVOs = new ArrayList<RegistroAulaVO>(0);
//					map.put(obj.getAnoSemestreApresentar(), registroAulaSecundarioVOs);
//				}
//				registroAulaSecundarioVOs = map.get(obj.getAnoSemestreApresentar());
//				registroAulaSecundarioVOs.add(obj);
//			}
//			SortedSet<String> keys = new TreeSet<String>(map.keySet());
//			for (String anoSemestre : keys) {
//				getListaFaltas().add(map.get(anoSemestre));
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	private void realizarMontagemListaHistoricoPrincipal() throws Exception {
		setListaPrincipalHistoricos(getFacadeFactory().getHistoricoFacade().realizarSeparacaoHistoricoPorPeriodicidade(getHistoricoVOs(), getUsuarioLogado()));

		for (HistoricoVO historicoVO : getHistoricoVOs()) {
			if (historicoVO.getConfiguracaoAcademico().getOcultarMediaFinalDisciplinaCasoReprovado()) {
				if (historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor())) {
					historicoVO.setOcultarMediaFinalDisciplinaMaeCasoReprovadoDisciplinaFilha(true);
				}
			}
		}

		if (getMatricula().getCurso().getIntegral()) {
			for (List<HistoricoVO> listas : getListaPrincipalHistoricos()) {
				Ordenacao.ordenarLista(listas, "data");
			}
			if (getApresentarCampoProfessor()) {
				setFrozenColumnsMinhasNotasAluno(4);
			} else {
				setFrozenColumnsMinhasNotasAluno(3);
			}
		} else {
			setFrozenColumnsMinhasNotasAluno(2);
		}
	}

	public void realizarMontagemListaSelectItemPeriodoLetivo() {
		try {
			getListaPrincipalHistoricos().clear();
//			CursoVO curso = getFacadeFactory().getCursoFacade().consultarCursoPorMatriculaParaInicializarNotaRapida(
//					getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			if (getMatricula().getCurso().getNivelEducacionalPosGraduacao() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO())) {
//				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//				try {
//					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("MarcarReposicaoAula", getUsuarioLogado());
//					setApresentarBotaoReposicao(Boolean.TRUE);
//				} catch (Exception e) {
//					setApresentarBotaoReposicao(Boolean.FALSE);
//				}
//			} else {
//				setApresentarBotaoReposicao(Boolean.FALSE);
//			}
			montarConfiguracaoAcademico();
			montarListaHistoricoAluno(getMatricula().getCurso());
//			curso = null;

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> listaSelectSemestre;

	public List<SelectItem> getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList<SelectItem>(0);
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}

	@SuppressWarnings("unchecked")
	public List<MatriculaVO> consultarMatriculaPorCodigoPessoa(UsuarioVO aluno) throws Exception {
		return getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaNaoCancelada(aluno.getPessoa().getCodigo(), aluno.getIsApresentarVisaoPais(), false, true, !getUsuarioLogado().getIsApresentarVisaoPais(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
	}

	public void inicializarDadosFotoUsuario() {
		try {
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
//			getPessoaVO().getArquivoImagem().setCpfRequerimento(getUsuarioLogadoClone().getPessoa().getCPF());
			setPessoaVO(getUsuarioLogadoClone().getPessoa());
			setCaminhoFotoUsuario("");
			if(Uteis.isAtributoPreenchido(getUsuarioLogadoClone().getPessoa().getArquivoImagem())) {
				getPessoaVO().setArquivoImagem(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(getUsuarioLogadoClone().getPessoa().getArquivoImagem().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado()));
				setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getUsuarioLogadoClone().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb() + File.separator + "resources", "foto_usuario.png", false));				
			}
			getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
			}
		} catch (Exception e) {
			inicializarDadosPadroes();
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.inicializarDadosFotoUsuario: "+e.getMessage());
		}
	}

	public void verificarPrimeiroAcessoAluno() {
		try {
			if (Uteis.isAtributoPreenchido(getUsuarioLogado()) && !getUsuarioLogado().getResetouSenhaPrimeiroAcesso() && getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPrimeiroAcessoAlunoResetarSenha().booleanValue()) {
				setApresentarModalResetarSenha(Boolean.TRUE);
				setLogin(getUsuarioLogado().getUsername());
				setSenha("");
				setSenhaAnterior("");
			}
		} catch (Exception e) {
			setApresentarModalResetarSenha(Boolean.FALSE);
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.verificarPrimeiroAcessoAluno: "+e.getMessage());
		}
	}

	public void alterarSenhaPrimeiroAcesso() {
		setOncompleteModal("");
		try {
			setOncompleteModal("");
			executarValidacaoSimulacaoVisaoAluno();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo());
			if (!getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				UsuarioVO usuario = getUsuarioLogado();
				usuario.setSenha(getSenha());
				getFacadeFactory().getUsuarioFacade().alterarSenhaPrimeiroAcesso(true, usuario, config);
				getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(this.obterConfiguracaoLdapPorMatricula(), usuario, getSenha());
			}
			setOncompleteModal("PF('panelPrimeiroAcesso').hide()");
			setApresentarModalResetarSenha(Boolean.FALSE);
			setOncompleteModal("PF('panelPrimeiroAcesso').hide()");
			setMensagem("Senha redefinida com Sucesso! Utilize o username acima e a nova senha para acessar!");
		} catch (ConsistirException e) {
			if (!e.getListaMensagemErro().isEmpty()) {
				setMensagemDetalhada("msg_erro", e.getListaMensagemErro().get(0));
			} else {
				if(Uteis.isAtributoPreenchido(e.getMessage())) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemCursoTopoAluno(UsuarioVO aluno) {
		List<MatriculaVO> listaMatricula = null;
		Iterator<MatriculaVO> i = null;
		try {
			if (aluno == null || !Uteis.isAtributoPreenchido(aluno) || !aluno.getIsApresentarVisaoAlunoOuPais()) {
				return;
			}
			List<SelectItem> lista = new ArrayList<SelectItem>(0);
			List<SelectItem> listaAlunos = new ArrayList<SelectItem>(0);
			boolean primeiro = Boolean.FALSE;
			if(getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getMatriculaVOs().isEmpty()) {
				listaMatricula = getUsuarioLogado().getMatriculaVOs();
			}else {
				listaMatricula = consultarMatriculaPorCodigoPessoa(getUsuarioLogadoClone().getIsApresentarVisaoPais() ? getUsuarioLogadoClone() : aluno);
			}
			i = listaMatricula.iterator();
			while (i.hasNext()) {
				MatriculaVO matriculas = (MatriculaVO) i.next();
				if (!primeiro && !Uteis.isAtributoPreenchido(getMatricula().getMatricula())) {
					setMatricula(matriculas);
//					getMatricula().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(getMatricula().getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, getUsuarioLogadoClone()));
					getUsuarioLogado().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
					getUsuarioLogado().setUnidadeEnsinoLogado((UnidadeEnsinoVO) getMatricula().getUnidadeEnsino().clone());
					getLoginControle().inicializarLogoApartirUsuario(getUsuarioLogadoClone());
					primeiro = Boolean.TRUE;
				}
				if (getUsuarioLogadoClone().getIsApresentarVisaoPais()) {
					if (!listaAlunos.stream().map(alu -> alu.getValue()).anyMatch(m -> m.equals(matriculas.getAluno().getCodigo()))) {
						listaAlunos.add(new SelectItem(matriculas.getAluno().getCodigo(),matriculas.getAluno().getNome()));
					}
					if (matriculas.getAluno().getCodigo().equals(getMatricula().getAluno().getCodigo())) {
						lista.add(new SelectItem(matriculas.getMatricula(), matriculas.getMatricula() + " - " + matriculas.getCurso().getNome() + " - " + 
						((matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals("CO") || matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals("FI") && !matricula.getSituacao().equals("FI")) ? "Ativo" : matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo_Apresentar())));
					}
				} else {
					lista.add(new SelectItem(matriculas.getMatricula(), matriculas.getMatricula() + " - " + matriculas.getCurso().getNome() + " - " + ((matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals("CO") || matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals("FI") && !matriculas.getSituacao().equals("FI")) ? "Ativo" : matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo_Apresentar())));
				}
			}
			setListaSelectItemMatriculaCursoTurnoTopoAluno(lista);
			setListaSelectItemAluno(listaAlunos);
			selecionarCursoTopo(false, false);
		} catch (Exception e) {
			setListaSelectItemMatriculaCursoTurnoTopoAluno(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.montarListaSelectItemCursoTopoAluno: "+e.getMessage());
		} finally {
			//Uteis.liberarListaMemoria(listaMatricula);
			i = null;
		}
	}

	public String realizarAcessoEstudoOnline(String telaAvancar) {
		try {
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem");
			executarMontagemDadosEstudoOnline(matriculaPeriodoTurmaDisciplinaVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setModalPanelErroAcessoConteudoEstudo("PF('panelErroAcessoConteudoEstudo').show()");
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(Uteis.isAtributoPreenchido(telaAvancar) ? telaAvancar : validarExistenciaAvaliacaoOnlineEmRealizacao());
	}

	private void executarMontagemDadosEstudoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getMatriculaPeriodoTurmaDisciplinaVO().setClassroomGoogleVO(matriculaPeriodoTurmaDisciplinaVO.getClassroomGoogleVO());
		getMatriculaPeriodoTurmaDisciplinaVO().setSalaAulaBlackboardVO(matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardVO());
		getMatriculaPeriodoTurmaDisciplinaVO().setSalaAulaBlackboardGrupoVO(matriculaPeriodoTurmaDisciplinaVO.getSalaAulaBlackboardGrupoVO());
		removerObjetosModuloEadMemoria();
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarAcessoEstudoOnline(getMatriculaPeriodoTurmaDisciplinaVO(), false, getUsuarioLogado());
		setQtdeAtualizacaoDuvidaProfessor(null);
//		if (getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
//			setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosDoDia(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo(), new Date()));
//			setCalendarioAtividadeMatriculaEstudoOnline(new CalendarDataModelImpl());
//		}
		gerarDadosGraficoSituacaoEstudos();
		setModalPanelErroAcessoConteudoEstudo("");
		setMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO(new MatriculaPeriodoTurmaDisciplinaVO());
		getMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO().setCodigo(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
		montarGraficoAproveitamentoAvaliacaoOnlineVisaoEstudoOnline();
		setAmbienteVisaoAluno(false);
		setApresentarMinhasNotasPbl(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarSeExisteAvaliacaoPblNoConteudoAtivoPorDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), getUsuarioLogado()));
		atualizarQuantidadesMenuEstudoOnline();
		realizarInicializacaoCalendarioAluno();
	}

	public String alterarAcessoEstudoOnline() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO mptdEditar = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(getMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarAcessoEstudoOnline(mptdEditar, false, getUsuarioLogado());
			setMatriculaPeriodoTurmaDisciplinaVO(mptdEditar);
			removerObjetosModuloEadMemoria();
//			if (getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
//				setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosDoDia(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo(), new Date()));
//				setCalendarioAtividadeMatriculaEstudoOnline(new CalendarDataModelImpl());
//			}

			getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().setDefinicoesTutoriaOnline(getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo()));
//			if (getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDefinicoesTutoriaOnline().equals(DefinicoesTutoriaOnlineEnum.DINAMICA)) {
//				getMatriculaPeriodoTurmaDisciplinaVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarSeExisteClassroom(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo()));
//				//getMatriculaPeriodoTurmaDisciplinaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum.DISCIPLINA, getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), 0, getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo(), getUsuarioLogadoClone()));
//			} else {
//				getMatriculaPeriodoTurmaDisciplinaVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarSeExisteClassroom(getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), 0));
//				//getMatriculaPeriodoTurmaDisciplinaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteSalaAulaBlackboard(TipoSalaAulaBlackboardEnum.DISCIPLINA, getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVO().getAno(), getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), 0, 0, getUsuarioLogadoClone()));
//			}
			getMatriculaPeriodoTurmaDisciplinaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPeriodoTurmaDisciplina(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado()));
			setQtdeAtualizacaoDuvidaProfessor(null);
			gerarDadosGraficoSituacaoEstudos();
			montarGraficoAproveitamentoAvaliacaoOnlineVisaoEstudoOnline();
			consultarAlertaAvaliacaoOnlineDisponivel();
			consultarAlertaInteracoesAtividadeDiscursivaEstudoOnline();
			atualizarQuantidadesMenuEstudoOnline();
			realizarInicializacaoCalendarioAluno();
			setModalPanelErroAcessoConteudoEstudo("");
			context().getExternalContext().getSessionMap().remove("avaliacaoOnlineRea");
			context().getExternalContext().getSessionMap().remove("avaliacaoOnlineMatriculaVO");
			context().getExternalContext().getSessionMap().remove("conteudoUnidadePaginaRecursoEducacionalVO");
			context().getExternalContext().getSessionMap().remove("apresentarOpcaoFecharGatilho");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO().setCodigo(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
			setModalPanelErroAcessoConteudoEstudo("PF('panelErroAcessoConteudoEstudo').show()");
			return "";
		}
		return validarExistenciaAvaliacaoOnlineEmRealizacao();
	}

	public String sairEstudoOnline() {
		try {
			setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			consultarMatriculaPeriodoTurmaDisciplinaOnline();
			realizarInicializacaoCalendarioAluno();
			setAmbienteVisaoAluno(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoAluno.xhtml");
	}

	public void consultarMatriculaPeriodoTurmaDisciplinaOnline() {
		try {
			if(getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().containsKey(getMatricula().getMatricula())) {
				setListaMatriculaPeriodoTurmaDisciplinaVOs(getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().get(getMatricula().getMatricula()));
			}else {				
				setListaMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaDoAlunoPorMatricula(getMatricula().getMatricula(), getFiltroAnoSemestreTelaInicial(), getLoginControle().getPermissaoAcessoMenuVO(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMatriculaPeriodoTurmaDisciplinaOnlineVOs(getListaMatriculaPeriodoTurmaDisciplinaVOs().stream().filter(d -> d.getPermiteAcessoEAD()).collect(Collectors.toList()));
			getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs().clear();
			for (MatriculaPeriodoTurmaDisciplinaVO obj : getMatriculaPeriodoTurmaDisciplinaOnlineVOs()) {
				obj.setOcultarDownload(true);
				getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs().add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
			}
//			if (Uteis.isAtributoPreenchido(getFiltroAnoSemestreTelaInicial()) && getListaSelectItemAnoSemestre().size() > 1 && !getFiltroAnoSemestreTelaInicial().equals(getListaSelectItemAnoSemestre().get(1).getValue())) {
//				try {
//					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoVizualizarMateriaisPeriodoConcluido", getUsuarioLogado());
//				} catch (Exception e) {
//					getListaMatriculaPeriodoTurmaDisciplinaVOs().stream().forEach(t -> t.setOcultarDownload(true));
//				}
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void verificarApresentacaoMenuTCC() throws Exception {
//		TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO = getFacadeFactory().getTrabalhoConclusaoCursoFacade().consultarTrabalhoConclusaoCursoAtualAluno(getMatricula().getMatricula());
//		Boolean apresentar = getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarVerificacaoTrabalhoConclusaoCursoAptoApresentar(trabalhoConclusaoCursoVO, getUsuarioLogado());
//		if (apresentar) {
//			setQtdeNovidadesTCC(getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarVerificacaoQtdeNovidadesTCC(trabalhoConclusaoCursoVO, getUsuarioLogado()));
//			setIsPrimeiroAcessoAlunoTCC(getFacadeFactory().getTrabalhoConclusaoCursoFacade().realizarVerificacaoPrimeiroAcessoAluno(trabalhoConclusaoCursoVO, getUsuarioLogado()));
//		}
		setApresentarMenuTCC(false);
		// setApresentarMenuTCC(true);
	}

	public void consultarEntregaDocumentos() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().validarConsultarDocumentoEntregueAluno(getUsuarioLogadoClone());
			setDocumetacaoMatriculaVOS(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculas(getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getSubmeter() {
		if (getSubmeterPagina() == null) {
			setSubmeterPagina(Boolean.TRUE);
		}
		if (getSubmeterPagina().equals(Boolean.TRUE)) {
			setSubmeterPagina(Boolean.FALSE);
			return "submit();";
		}
		return "";
	}

	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarFoto(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
			getPessoaVO().getArquivoImagem().setCpfRequerimento(getPessoaVO().getCPF());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			getPessoaVO().getArquivoImagem().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IMAGEM);
			this.confirmarFoto();
			setOncompleteModal("PF('panelImagem').hide();");
		} catch (Exception ex) {
			setOncompleteModal("PF('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void recortarImagem() {
		try {

			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void recortarImagemVerso() {
		try {
			if (getLarguraVerso() == 0f && getAlturaVerso() == 0f && getXcropVerso() == 0f && getYcropVerso() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVOVerso(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLarguraVerso(), getAlturaVerso(), getXcropVerso(), getYcropVerso(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void cancelar() {
		try {
			setRemoverFoto((Boolean) false);
			setExibirUpload(true);
			setExibirBotao(false);
			getFacadeFactory().getArquivoHelper().realizarGravacaoFotoSemRecorte(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			confirmarFoto();
		} catch (Exception ex) {
			setOncompleteModal("PF('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public String getShowFotoCrop() {
		try {
			if (getPessoaVO().getArquivoImagem().getNome() == null) {
				return "resources/imagens/usuarioPadrao.jpg";
			}
			return getCaminhoFotoUsuario() + "?UID=" + new Date().getTime();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}

	// public void getShowFoto(OutputStream out, Object data) throws IOException
	// {
	// try {
	// // BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(new
	// ByteArrayInputStream(getPessoaVO().getFoto())));
	// // ImageIO.write(bufferedImage, "jpg", out);
	// } catch (Exception e) {
	// //System.out.println("Erro:" + e.getMessage());
	// }
	// }
	public void paint(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		try {
			arquivoHelper.renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
		}
	}

	public void paintTemp(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		try {
			arquivoHelper.renderizarImagemNaTela(out, getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
		}
	}

	public void confirmarFoto() throws Exception {

		getPessoaVO().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "ALUNO");
		getFacadeFactory().getPessoaFacade().alterarFoto(getPessoaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
		getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
		removerImagensUploadArquivoTemp();
		setRemoverFoto((Boolean) false);
		setExibirUpload(true);
		setExibirBotao(false);

	}

	public void removerImagensUploadArquivoTemp() throws Exception {
		try {
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getPessoaVO().getCPF();
			File arquivo = new File(arquivoExterno);
			ArquivoHelper.deleteRecursivo(arquivo);
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
		}
	}

	public void removerFoto() {
		try {
			getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(true, getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema());
			getPessoaVO().setArquivoImagem(null);
			getFacadeFactory().getPessoaFacade().alterar(getPessoaVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
			getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public boolean getControlaAprovacaoDocumento() {
		try {
			return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue().booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean permitirPostagemPortalAluno;

	public Boolean getPermitirPostagemPortalAluno() {
		if (permitirPostagemPortalAluno == null) {
			try {
				permitirPostagemPortalAluno = false;
				Iterator<DocumetacaoMatriculaVO> k = getDocumetacaoMatriculaVOS().iterator();
				while (k.hasNext()) {
					DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) k.next();
					if (obj.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno().booleanValue()) {
						permitirPostagemPortalAluno = true;
						break;
					}
				}
			} catch (Exception e) {

			}
		}
		return permitirPostagemPortalAluno;
	}

	public void adicionarArquivoDocumentacaoMatricula() {
		try {
			setAbrirModalInclusaoArquivoVerso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVO().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVO().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}
			if (!getDocumetacaoMatriculaVO().getEntregue()) {
				getDocumetacaoMatriculaVO().setDataNegarDocDep(null);
				getDocumetacaoMatriculaVO().setRespAprovacaoDocDep(null);
				getDocumetacaoMatriculaVO().setJustificativaNegacao(null);
			}
			// getDocumetacaoMatriculaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			diminuirResolucaoImagem(getDocumetacaoMatriculaVO().getArquivoVO());
			if (!getDocumetacaoMatriculaVO().getEntregue() && getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getDocumentoFrenteVerso()) {
				setAbrirModalInclusaoArquivoVerso("PF('panelIncluirArquivoVerso').show()");
			} else {
				setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			}
		    getFacadeFactory().getDocumetacaoMatriculaFacade().removerVinculoMotivoIndeferimentoDocumentoAluno(getDocumetacaoMatriculaVO(), getUsuarioLogadoClone());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoDocumentacaoMatriculaVerso() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setResponsavelUpload(getUsuarioLogadoClone());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}
			if (!getDocumetacaoMatriculaVO().getEntregue()) {
				getDocumetacaoMatriculaVO().setDataNegarDocDep(null);
				getDocumetacaoMatriculaVO().setRespAprovacaoDocDep(null);
				getDocumetacaoMatriculaVO().setJustificativaNegacao(null);
			}
			// getDocumetacaoMatriculaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			diminuirResolucaoImagem(getDocumetacaoMatriculaVO().getArquivoVOVerso());
			int index = 0;
			Iterator<DocumetacaoMatriculaVO> i = getDocumetacaoMatriculaVOS().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
				if (documetacaoMatriculaVO.getCodigo().equals(getDocumetacaoMatriculaVO().getCodigo())) {
					getDocumetacaoMatriculaVOS().set(index,getDocumetacaoMatriculaVO());
				}
				index++;
			}
		    getFacadeFactory().getDocumetacaoMatriculaFacade().removerVinculoMotivoIndeferimentoDocumentoAluno(getDocumetacaoMatriculaVO(), getUsuarioLogadoClone());
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void diminuirResolucaoImagem(ArquivoVO obj) throws IOException {
		if (obj.getIsImagem()) {
			File fileTemp = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome());
			if (Uteis.isAtributoPreenchido(fileTemp.getName()) && fileTemp.exists()) {
				getFacadeFactory().getArquivoHelper().diminuirResolucaoImagem(fileTemp, 0.5f);
			}
		}
	}

	public void gravarDocumentacaoMatricula() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getDocumetacaoMatriculaFacade().gravarDocumentacaoMatriculaVisaoAluno(getDocumetacaoMatriculaVOS(), getMatricula(), getUsuarioLogadoClone(), getConfiguracaoGeralPadraoSistema(), Boolean.TRUE);
			notificarArqPostados();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void notificarArqPostados() {
//		try {
//			List<DocumetacaoMatriculaVO> listaNotificar = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentoAprovadoPendenteNotificarAlunoDocPostado(getMatricula().getMatricula());
//			try {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoDocumentoPostado(getMatricula(), listaNotificar, getUsuarioLogado());
//			} catch (Exception e) {
//			}
//			setMensagemID("msg_dados_deferido");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public String getCaminhoServidorDownload() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(), obj.getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadVerso() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVOVerso(), obj.getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacao() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVO(), getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacaoVerso() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatricula().getAluno(), true, false, null, getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());			
			setMensagemID("msg_sucesso_upload");
			setOncompleteModal("PF('panelUpload').hide(); PF('panelImagemDocumentosEntregues').show();");
		} catch (Exception e) {
			setOncompleteModal("PF('panelUpload').hide(); PF('panelImagemDocumentosEntregues').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void upLoadArquivoVerso(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatricula().getAluno(), false, false, null, getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());
			setMensagemID("msg_sucesso_upload");
			setOncompleteModal("PF('panelUploadVerso').hide(); PF('panelImagemDocumentosEntreguesVerso').show();");
		} catch (Exception e) {
			setMensagemID("msg_sucesso_upload");
			setOncompleteModal("PF('panelUploadVerso').hide(); PF('panelImagemDocumentosEntreguesVerso').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public String getCaminhoPDF() {
		return getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoWeb()+"?embedded=true";
	}

	public String getCaminhoPDFVerso() {
		return getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoWeb()+"?embedded=true";
	}

	public void selecionarObjetoDocumentacaoMatriculaVerso() {
		setOncompleteModal("");
		try {
			setTam(100);
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			setDocumetacaoMatriculaVO((DocumetacaoMatriculaVO) obj.clone());
			validarBloqueioEntregaDocumentoForaPrazoConformePeridoChamadaProcessoSeletivoParaMatriculasVinculadasAInscricao(getDocumetacaoMatriculaVO());
			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			if (obj.getArquivoVOVerso().getNome().equals("")) {
				getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao(obj.getTipoDeDocumentoVO().getNome() + "_VERSO");
			}
			setOncompleteModal("PF('panelUploadVerso').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	

	public void selecionarObjetoDocumentacaoMatricula() {
		setOncompleteModal("");
		try {
			setTam(100);
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			setDocumetacaoMatriculaVO(obj);
			validarBloqueioEntregaDocumentoForaPrazoConformePeridoChamadaProcessoSeletivoParaMatriculasVinculadasAInscricao(getDocumetacaoMatriculaVO());
			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			if (obj.getArquivoVO().getNome().equals("")) {
				getDocumetacaoMatriculaVO().setArquivoVO(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVO().setDescricao(obj.getTipoDeDocumentoVO().getNome());
			}
			setOncompleteModal("PF('panelUpload').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarBloqueioEntregaDocumentoForaPrazoConformePeridoChamadaProcessoSeletivoParaMatriculasVinculadasAInscricao(DocumetacaoMatriculaVO doc) throws Exception {
		String mensagem ="Prezado " + getUsuarioLogado().getNome().toUpperCase()+" não é possível realizar Upload Documento Indeferido/Não Entregue fora do prazo do período de chamada ou fora período de upload de documentação Documento Indeferido do processo seletivo.";
        Uteis.checkState(getFacadeFactory().getDocumetacaoMatriculaFacade().verificarBloqueioAprovacaoDocumentoMatriculaCalouroDeAcordoComPeriodoChamadaDoProcessoSeletivo(getMatricula().getMatricula(), doc.getIndeferido() ,true ,getUsuarioLogado()), mensagem);
	}

	public void criarNomeArquivo() {
		setTam(100);
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
	}

	public void selecionarArquivoDocumentoEntregueExclusao() {
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		setDocumetacaoMatriculaVO(obj);
	}

	public void removerArquivoDocumentacao() throws Exception {
		try {
			getDocumetacaoMatriculaVO().setExcluirArquivo(true);
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
			getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(getDocumetacaoMatriculaVO(), getConfiguracaoGeralPadraoSistema(),true , getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void cancelarExclusaoArquivoDocumentoEntregue() {
		if (getDocumetacaoMatriculaVO() != null && !getDocumetacaoMatriculaVO().getArquivoVO().getNome().equals("") && !getDocumetacaoMatriculaVO().getEntregue()) {
			getDocumetacaoMatriculaVO().setEntregue(true);
		}
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
	}

	public Boolean getExisteImagem() {
		return true;
	}

	public void inicializarDadosPadroes() {
		inicializarMenuRecado();
	}

	public void inicializarMenuRecado() {
		setMenuRecado(Boolean.TRUE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setNovoComunicado(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.FALSE);
		setLerComunicado(Boolean.FALSE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public void realizarVerificacaoSeUsuarioLogadoPossuiVisaoPais()  {
		try {
		if (Uteis.isAtributoPreenchido(getUsuarioLogado()) && getUsuarioLogado().getIsApresentarVisaoPais()) {
			List<UsuarioVO> listaAlunos = (getFacadeFactory().getUsuarioFacade().consultaRapidaPorResponsavelLegal(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado()));
			for (UsuarioVO usuario : listaAlunos) {
				getListaDeAlunosPorResponsavelLegal().add(new SelectItem(usuario.getCodigo(), usuario.getPessoa().getNome()));
				getListaPessoasEditaveis().add(new SelectItem(usuario.getPessoa().getCodigo(), usuario.getPessoa().getNome()));
			}
			getListaPessoasEditaveis().add(new SelectItem(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getPessoa().getNome()));
			if (!listaAlunos.isEmpty()) {
				getAlunoPorResponsavelLegal().setCodigo(listaAlunos.get(0).getCodigo());
				getAlunoPorResponsavelLegal().setNome(listaAlunos.get(0).getNome());
				getAlunoPorResponsavelLegal().getPessoa().setCodigo(listaAlunos.get(0).getPessoa().getCodigo());
				getAlunoPorResponsavelLegal().getPessoa().setNome(listaAlunos.get(0).getPessoa().getNome());
				getAlunoPorResponsavelLegal().setUsername(listaAlunos.get(0).getUsername());
				getAlunoPorResponsavelLegal().setTipoUsuario(listaAlunos.get(0).getTipoUsuario());
				realizarPreenchimentoDadosParaUsuarioPorResponsavelLegal();
			}
		}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.realizarVerificacaoSeUsuarioLogadoPossuiVisaoPais: "+e.getMessage());
		}
	}

	public String realizarSelecaoPessoa() {
//		try {
//			setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getPessoaVO().getCodigo(), false, true, false, getUsuarioLogado()));
//			getPessoaVO().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(getPessoaVO().getCodigo()));
//			setMensagemID("msg_dados_selecionados");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
		return "aluno";
	}

	public String realizarSelecaoUsuarioPorResponsavelLegal() {
		try {
			setAlunoPorResponsavelLegal(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getAlunoPorResponsavelLegal().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			realizarPreenchimentoDadosParaUsuarioPorResponsavelLegal();
			existeProcessoMatriculaAberto = null;
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "aluno";
		// return Uteis.getCaminhoRedirecionamentoNavegacao("")
	}

	public void realizarPreenchimentoDadosParaUsuarioPorResponsavelLegal() throws Exception {
		setQtdeAtualizacaoForum(null);
		getAlunoPorResponsavelLegal().setVisaoLogar("aluno");
		montarListaSelectItemCursoTopoAluno(getAlunoPorResponsavelLegal());
		incializarDadosPessoa(getAlunoPorResponsavelLegal());
		ComunicacaoInternaControle comunicacaoInternaControle = (ComunicacaoInternaControle) context().getExternalContext().getSessionMap().get("ComunicacaoInternaControle");
		if (comunicacaoInternaControle != null) {
			comunicacaoInternaControle.verificarQtdeMensagemCaixaEntrada();
//			comunicacaoInternaControle.setQtdMensagemCaixaEntradaAluno(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasVisaoAluno(getAlunoPorResponsavelLegal().getPessoa().getCodigo()));
			// comunicacaoInternaControle.consultarTodasEntradaMarketingLeituraObrigatoriaAluno(getAlunoPorResponsavelLegal());
		}
	}

	public String inicializarMenuRenovacaoMatricula() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Renovação Matrícula", "Inicializando");
		setMenuRenovacaoMatricula(Boolean.TRUE);
		setMenuNovaMatricula(Boolean.FALSE);
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setNovoComunicado(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.FALSE);
		setLerComunicado(Boolean.FALSE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
		removerControleMemoriaFlashTela("RenovarMatriculaControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("renovacaoMatriculaAluno.xhtml");
	}

	public String inicializarMenuNovaMatricula() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Nova Matrícula", "Inicializando");
		setMenuRenovacaoMatricula(Boolean.FALSE);
		setMenuNovaMatricula(Boolean.TRUE);
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setNovoComunicado(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.FALSE);
		setLerComunicado(Boolean.FALSE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("matriculaOnlineVisaoAlunoForm.xhtml");
	}

	public void inicializarMenuPlanoDeEstudoAluno() {
		setPlanoDeEstudoAluno(true);
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuNovoRecado() {
		setNovoComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
		// getMatricula().setMatricula("");
	}

	public void inicializarMenuLerRecado() {
		setNovoComunicado(Boolean.FALSE);
		setLerComunicado(Boolean.TRUE);
		// getMatricula().setMatricula("");

	}

	private String urlMoodle;

	public void realizaLoginMoodle() throws Exception {
		try {
			String token = getFacadeFactory().getMatriculaFacade().consultarTokenPessoaPorMatricula(getMatricula().getMatricula());
			String url = getConfiguracaoGeralPadraoSistema().getLinkAcessoVisoesMoodle() + token;
			if (!url.equals("")) {
				setUrlMoodle(url);
			} else {
				throw new Exception("Não foi possível logar no ambiente de estudo, entre em contato com o departamento responsável!");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarMenuConfiguracao() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Configuração", "Inicializando");
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.TRUE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		setMensagemDetalhada("", "");
		setLogin("");
		setSenha("");
		setLoginAnterior("");
		setSenhaAnterior("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAluno.xhtml");
	}

	public String inicializarMenuMeusAmigos() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Meus Amigos", "Inicializando");
		setMenuRecado(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.TRUE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("msg_entre_prmconsulta", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("meusAmigosAluno.xhtml");
	}

	public void inicializarMenuDadosPessoais() {
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.TRUE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuMeusProfessores() {
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.TRUE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemID("msg_entre_prmconsulta");
		setMensagemDetalhada("", "");

	}

	public String inicializarMenuMinhasNotas() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Minhas Notas", "Inicializando");
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.TRUE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		setApresentarCampoProfessor(verificarPermissaoApresentarProdessor());

		realizarMontagemListaSelectItemPeriodoLetivo();
		realizarMontagemListaDisplinasReprovadas();
		setMensagemDetalhada("", "");
		setHistoricoVOs(new ArrayList<HistoricoVO>(0));

		return Uteis.getCaminhoRedirecionamentoNavegacao("minhasNotasAlunos");
	}

	private boolean verificarPermissaoApresentarProdessor() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_APRESENTAR_PROFESSOR, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String inicializarMenuMinhasFaltas() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Minhas Faltas", "Inicializando");
		if (getMatricula().getCurso().getIsNivelMontarDadosNaoInicializado()) {
			getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(getMatricula().getMatricula(), false, getUsuarioLogado()));
			getMatricula().getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		}
		consultarMinhasFaltasAluno();
		return Uteis.getCaminhoRedirecionamentoNavegacao("minhasFaltasAluno.xhtml");
	}

	public String inicializarMenuAtividadeComplementar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Atividade Complementar", "Inicializando");
		removerControleMemoriaFlashTela("AcompanhamentoAtividadeComplementarControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("atividadeComplementarAluno.xhtml");
	}

	public String inicializarMenuEstagio() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Estágio", "Inicializando");			
			context().getExternalContext().getSessionMap().put("matricula", getMatricula());
			removerControleMemoriaFlash("EstagioObrigatorioControle");
			removerControleMemoriaTela("EstagioObrigatorioControle");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("estagioAluno.xhtml");
	}
	
	public String inicializarMenuTcc() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Tcc", "Inicializando");			
			context().getExternalContext().getSessionMap().put("catcc", true);
			context().getExternalContext().getSessionMap().put("matriculatcc", getMatricula());
			context().getExternalContext().getSessionMap().put("matriculaperiodotcc", getMatriculaPeriodoVO());
			removerControleMemoriaFlash("CalendarioAgrupamentoTccControle");
			removerControleMemoriaTela("CalendarioAgrupamentoTccControle");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("tccAluno.xhtml");
	}
	
	public String inicializarMenuProjetoIntegrador() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Projeto Integrador", "Inicializando");
			context().getExternalContext().getSessionMap().put("capi", getCalendarioAgrupamentoProjetoIntegrador());
			context().getExternalContext().getSessionMap().put("matriculapi", getMatricula());
			context().getExternalContext().getSessionMap().put("listaMptd", getListaMatriculaPeriodoTurmaDisciplinaVOs()
					.stream()
					.filter(p-> p.getDisciplina().getClassificacaoDisciplina().isProjetoIntegrador())
					.collect(Collectors.toList()));
			removerControleMemoriaFlash("CalendarioAgrupamentoTccControle");
			removerControleMemoriaTela("CalendarioAgrupamentoTccControle");
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("projetoIntegradorAluno.xhtml");
	}

	public String inicializarMenuMinhasNotasDestinoContrato() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Minhas Notas Destino Contrato", "Inicializando");
		setMatriculaPeriodoContratoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodosComContrato(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  getUsuarioLogado(), true));
		return Uteis.getCaminhoRedirecionamentoNavegacao("meusContratosAluno.xhtml");
	}

	public void inicializarMenuMeusContratos() {
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.TRUE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuDisciplina() {
		setMenuRecado(Boolean.FALSE);
		setMenuDisciplina(Boolean.TRUE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		inicializarDisciplinaConsultar();
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public String inicializarMenuMeusHorarios() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Meus Horários", "Inicializando");
		setMenuRecado(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.TRUE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);

		String mesAtual = String.valueOf(Uteis.getMesDataAtual());
		if (mesAtual.length() == 1) {
			setMes("0" + mesAtual);
		} else {
			setMes(mesAtual);
		}
		setAno(Uteis.getAnoDataAtual());
		inicializarDisciplinaConsultar();
		setMensagemDetalhada("msg_entre_dados", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("meusHorariosAluno.xhtml");
	}

	public void inicializarMenuMinhasContasAPagar() {
		setMenuRecado(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuRequerimento(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.TRUE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public void inicializarMenuRequerimento() {
		setMenuRecado(Boolean.FALSE);
		setMenuMeusHorarios(Boolean.FALSE);
		setMenuDisciplina(Boolean.FALSE);
		setMenuConfiguracao(Boolean.FALSE);
		setMenuDadosPessoais(Boolean.FALSE);
		setMenuMeusAmigos(Boolean.FALSE);
		setMenuMeusProfessores(Boolean.FALSE);
		setMenuMinhasNotas(Boolean.FALSE);
		setMenuSenha(Boolean.FALSE);
		setConfiguracao(Boolean.TRUE);
		setMenuRequerimento(Boolean.TRUE);
		setMenuMinhasContasAPagar(Boolean.FALSE);
		setMenuRenovacaoMatricula(Boolean.FALSE);
		// getMatricula().setMatricula("");
		inicializarConsultaRequerimento();
		setMensagemDetalhada("", "");
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(pessoaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDisciplinaEditar() {
		setDisciplinaEditar(Boolean.TRUE);
		setDisciplinaConsultar(Boolean.FALSE);
	}

	public void inicializarDisciplinaConsultar() {
		setDisciplinaEditar(Boolean.FALSE);
		setDisciplinaConsultar(Boolean.TRUE);
	}

	public void inicializarMenuSenha() {
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		limparMensagem();
		setLoginAnterior(getUsuarioLogado().getUsername());
		setLogin(getUsuarioLogado().getUsername());
		try {
			setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
		} catch (Exception e) {
		}
	}

	public String inicializarMenuEntregaDocumentos() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Entrega Documentos", "Inicializando");
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setApresentarModalAlertarAlunoPendenciaDocumentacaoMatricula(Boolean.FALSE);
		try {
			consultarEntregaDocumentos();
		} catch (Exception e) {
		}
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoAluno.xhtml");
	}

	public String inicializarMenuEmprestimosEmAberto() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Empréstimos Em Aberto", "Inicializando");
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("emprestimosAluno.xhtml");
	}

	public String inicializarMenuBiblioteca() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Biblioteca", "Inicializando");
		setConfiguracao(Boolean.FALSE);
		setMenuSenha(Boolean.TRUE);
		setMensagemDetalhada("", "");
		return Uteis.getCaminhoRedirecionamentoNavegacao("buscaBibliotecaAluno.xhtml");
	}

	public void inicializarConfiguracao() {
		setConfiguracao(Boolean.TRUE);
		setMenuSenha(Boolean.FALSE);
		setMensagemDetalhada("", "");
	}

	public void inicializarConsultaRequerimento() {
		setConsultaRequerimento(Boolean.TRUE);
		setNovoRequerimento(Boolean.FALSE);

		setMensagemDetalhada("", "");
	}

	public void inicializarNovoRequerimento() {
		setConsultaRequerimento(Boolean.FALSE);
		setNovoRequerimento(Boolean.TRUE);
		// getMatricula().setMatricula("");
		setMensagemDetalhada("", "");
	}

	public void inicializarDownloadArquivos() {
		try {
			setDownloadArquivos(Boolean.TRUE);
			getFacadeFactory().getArquivoFacade().validarDownloadArquivoAluno(getUsuarioLogadoClone());
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarCidade() {
		try {
			List<CidadeVO> objs = new ArrayList<CidadeVO>(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public List<SelectItem> tipoConsultaCidade;

	public List<SelectItem> getTipoConsultaCidade() {
		if (tipoConsultaCidade == null) {
			tipoConsultaCidade = new ArrayList<SelectItem>(0);
			tipoConsultaCidade.add(new SelectItem("nome", "Nome"));
			tipoConsultaCidade.add(new SelectItem("estado", "Estado"));
			tipoConsultaCidade.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaCidade;
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getPessoaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	

	/*
	 * Mï¿½todo responsï¿½vel por disponibilizar dados de um objeto da classe <code>FormacaoAcademica</code> para ediï¿½ï¿½o pelo usuï¿½rio.
	 */
	public void editarDadosComerciais() throws Exception {
		DadosComerciaisVO obj = (DadosComerciaisVO) context().getExternalContext().getRequestMap().get("dadosComerciaisItens");
		setDadosComerciaisVO(obj);
	}

	/**
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
	 **/
	public void removerDadosComerciais() throws Exception {
//		DadosComerciaisVO obj = (DadosComerciaisVO) context().getExternalContext().getRequestMap().get("dadosComerciaisItens");
//		getFacadeFactory().getDadosComerciaisFacade().excluirObjDadosComerciaisVOs(obj, getPessoaVO());
//		getFacadeFactory().getDadosComerciaisFacade().setIdEntidade("Pessoa");
//		getFacadeFactory().getDadosComerciaisFacade().alterarDadosComerciais(getPessoaVO(), getPessoaVO().getDadosComerciaisVOs(), true);
//		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Mï¿½todo responsï¿½vel por adicionar um novo objeto da classe <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
	 */
	public void adicionarAreaProfissional() {
		try {
//			AreaProfissionalVO areaProfissional = getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(getAreaProfissionalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			Integer numeroAreasProfissionais = getPessoaVO().getAreaProfissionalInteresseContratacaoVOs().size();
//			getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().adicionarObjAreaProfissionalVOs(areaProfissional, getPessoaVO());
//			if (getPessoaVO().getAreaProfissionalInteresseContratacaoVOs().size() > numeroAreasProfissionais) {
//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().setIdEntidade("Pessoa");
//				getFacadeFactory().getAreaProfissionalInteresseContratacaoFacade().incluirAreaProfissionalInteresseContratacao(getPessoaVO().getCodigo(), getPessoaVO().getAreaProfissionalInteresseContratacaoVOs(), getUsuarioLogado());
//			}
//			this.setAreaProfissionalVO(new AreaProfissionalVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
	 **/
	

	public void adicionarFormacaoExtraCurricular() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getFormacaoExtraCurricularVO().setPessoa(getPessoaVO());
			}
			Integer numeroFormacoesExtraCurriculares = getPessoaVO().getFormacaoExtraCurricularVOs().size();
			getFacadeFactory().getFormacaoExtraCurricularFacade().adicionarObjFormacaoExtraCurricularVOs(getFormacaoExtraCurricularVO(), getPessoaVO());
			if (getPessoaVO().getFormacaoExtraCurricularVOs().size() > numeroFormacoesExtraCurriculares) {
				getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade("Pessoa");
				getFacadeFactory().getFormacaoExtraCurricularFacade().incluirFormacaoExtraCurricular(getPessoaVO(), getPessoaVO().getFormacaoExtraCurricularVOs(), true);
			}
			this.setFormacaoExtraCurricularVO(new FormacaoExtraCurricularVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por disponibilizar dados de um objeto da classe <code>FormacaoAcademica</code> para ediï¿½ï¿½o pelo usuï¿½rio.
	 */
	public void editarFormacaoExtraCurricular() throws Exception {
		FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) context().getExternalContext().getRequestMap().get("formacaoExtraCurricularItens");
		setFormacaoExtraCurricularVO(obj);
	}

	/**
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe <code>Pessoa</code>
	 **/
	public void removerFormacaoExtraCurricular() throws Exception {
		FormacaoExtraCurricularVO obj = (FormacaoExtraCurricularVO) context().getExternalContext().getRequestMap().get("formacaoExtraCurricularItens");
		getFacadeFactory().getFormacaoExtraCurricularFacade().excluirObjFormacaoExtraCurricularVOs(obj, getPessoaVO());
		getFacadeFactory().getFormacaoExtraCurricularFacade().setIdEntidade("Pessoa");
		getFacadeFactory().getFormacaoExtraCurricularFacade().alterarFormacaoExtraCurricular(getPessoaVO(), getPessoaVO().getFormacaoExtraCurricularVOs(), true);
		setMensagemID("msg_dados_excluidos");
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Cidade</code>.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemAreaProfissional(String prm) throws Exception {
//		SelectItemOrdemValor ordenador = null;
//		List<AreaProfissionalVO> resultadoConsulta = null;
//		Iterator<AreaProfissionalVO> i = null;
//		try {
//			resultadoConsulta = consultarAreaProfissionalPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List<SelectItem> objs = new ArrayList<SelectItem>(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				AreaProfissionalVO obj = (AreaProfissionalVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoAreaProfissional()));
//			}
//			ordenador = new SelectItemOrdemValor();
//			Collections.sort((List) objs, ordenador);
//			setListaSelectItemAreaProfissional(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			ordenador = null;
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemAreaProfissional() {
		try {
			montarListaSelectItemAreaProfissional("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
//	@SuppressWarnings("unchecked")
//	public List<AreaProfissionalVO> consultarAreaProfissionalPorNome(String nomePrm) throws Exception {
//		List<AreaProfissionalVO> lista = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//		return lista;
//	}

	public List<SelectItem> tipoConsultaComboSalario;

	public List<SelectItem> getTipoConsultaComboSalario() {
		if (tipoConsultaComboSalario == null) {
			tipoConsultaComboSalario = new ArrayList<SelectItem>(0);
			tipoConsultaComboSalario.add(new SelectItem("Até R$999", "Até R$999,00"));
			tipoConsultaComboSalario.add(new SelectItem("R$1000 à R$1999", "De R$ 1.000,00 até R$ 1.999,00"));
			tipoConsultaComboSalario.add(new SelectItem("R$2000 à R$2999", "De R$ 2.000,00 até R$ 2.999,00"));
			tipoConsultaComboSalario.add(new SelectItem("R$3000 à R$3999", "De R$ 3.000,00 até R$ 3.999,00"));
			tipoConsultaComboSalario.add(new SelectItem("R$4000 à R$4999", "De R$ 4.000,00 até R$ 4.999,00"));
			tipoConsultaComboSalario.add(new SelectItem("R$5000 à R$5999", "De R$ 5.000,00 até R$ 5.999,00"));
			tipoConsultaComboSalario.add(new SelectItem("acima de R$6000", "Acima de R$ 6.000,00"));
		}
		return tipoConsultaComboSalario;
	}

	public List<SelectItem> tipoComboNivelIngles;

	public List<SelectItem> getTipoComboNivelIngles() {
		if (tipoComboNivelIngles == null) {
			tipoComboNivelIngles = new ArrayList<SelectItem>(0);
			tipoComboNivelIngles.add(new SelectItem("", ""));
			tipoComboNivelIngles.add(new SelectItem("inicial", "Inicial"));
			tipoComboNivelIngles.add(new SelectItem("intermediario", "Intermediário"));
			tipoComboNivelIngles.add(new SelectItem("avancado", "Avançado"));
		}
		return tipoComboNivelIngles;
	}

	public List<SelectItem> tipoComboNivelFrances;

	public List<SelectItem> getTipoComboNivelFrances() {
		if (tipoComboNivelFrances == null) {
			tipoComboNivelFrances = new ArrayList<SelectItem>(0);
			tipoComboNivelFrances.add(new SelectItem("", ""));
			tipoComboNivelFrances.add(new SelectItem("inicial", "Inicial"));
			tipoComboNivelFrances.add(new SelectItem("intermediario", "Intermediário"));
			tipoComboNivelFrances.add(new SelectItem("avancado", "Avançado"));
		}
		return tipoComboNivelFrances;
	}

	public List<SelectItem> tipoComboNivelEspanhol;

	public List<SelectItem> getTipoComboNivelEspanhol() {
		if (tipoComboNivelEspanhol == null) {
			tipoComboNivelEspanhol = new ArrayList<SelectItem>(0);
			tipoComboNivelEspanhol.add(new SelectItem("", ""));
			tipoComboNivelEspanhol.add(new SelectItem("inicial", "Inicial"));
			tipoComboNivelEspanhol.add(new SelectItem("intermediario", "Intermediário"));
			tipoComboNivelEspanhol.add(new SelectItem("avancado", "Avançado"));
		}
		return tipoComboNivelEspanhol;
	}

	public List<SelectItem> tipoComboNivelOutrosIdiomas;

	public List<SelectItem> getTipoComboNivelOutrosIdiomas() {
		if (tipoComboNivelOutrosIdiomas == null) {
			tipoComboNivelOutrosIdiomas = new ArrayList<SelectItem>(0);
			tipoComboNivelOutrosIdiomas.add(new SelectItem("", ""));
			tipoComboNivelOutrosIdiomas.add(new SelectItem("inicial", "Inicial"));
			tipoComboNivelOutrosIdiomas.add(new SelectItem("intermediario", "Intermediário"));
			tipoComboNivelOutrosIdiomas.add(new SelectItem("avancado", "Avançado"));
		}
		return tipoComboNivelOutrosIdiomas;
	}

	public String realizarAbrirRichCurriculumAluno() {
		context().getExternalContext().getSessionMap().put("pessoaVO", getPessoaVO());
		return "panelCurriculum";
	}

	public void carregarEnderecoEmpresaPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoEmpresa(getDadosComerciaisVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Boolean getMenuConfiguracao() {
		return menuConfiguracao;
	}

	public void setMenuConfiguracao(Boolean menuConfiguracao) {
		this.menuConfiguracao = menuConfiguracao;
	}

	public Boolean getMenuDadosPessoais() {
		return menuDadosPessoais;
	}

	public void setMenuDadosPessoais(Boolean menuDadosPessoais) {
		this.menuDadosPessoais = menuDadosPessoais;
	}

	public Boolean getMenuMeusAmigos() {
		return menuMeusAmigos;
	}

	public void setMenuMeusAmigos(Boolean menuMeusAmigos) {
		this.menuMeusAmigos = menuMeusAmigos;
	}

	public Boolean getMenuMeusProfessores() {
		return menuMeusProfessores;
	}

	public void setMenuMeusProfessores(Boolean menuMeusProfessores) {
		this.menuMeusProfessores = menuMeusProfessores;
	}

	public Boolean getMenuRecado() {
		return menuRecado;
	}

	public void setMenuRecado(Boolean menuRecado) {
		this.menuRecado = menuRecado;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}

	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public Boolean getSubmeterPagina() {
		return submeterPagina;
	}

	public void setSubmeterPagina(Boolean submeterPagina) {
		this.submeterPagina = submeterPagina;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	public MatriculaVO getMatricula() {
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public List<HistoricoVO> getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList<HistoricoVO>();
		}
		return historicoVOs;
	}

	public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}

	public Boolean getMenuMinhasNotas() {
		return menuMinhasNotas;
	}

	public void setMenuMinhasNotas(Boolean menuMinhasNotas) {
		this.menuMinhasNotas = menuMinhasNotas;
	}

	public Boolean getMenuSenha() {
		return menuSenha;
	}

	public void setMenuSenha(Boolean menuSenha) {
		this.menuSenha = menuSenha;
	}

	public Boolean getDisciplinaConsultar() {
		return disciplinaConsultar;
	}

	public void setDisciplinaConsultar(Boolean disciplinaConsultar) {
		this.disciplinaConsultar = disciplinaConsultar;
	}

	public Boolean getDisciplinaEditar() {
		return disciplinaEditar;
	}

	public void setDisciplinaEditar(Boolean disciplinaEditar) {
		this.disciplinaEditar = disciplinaEditar;
	}

	public Boolean getMenuDisciplina() {
		return menuDisciplina;
	}

	public void setMenuDisciplina(Boolean menuDisciplina) {
		this.menuDisciplina = menuDisciplina;
	}

	public Boolean getMenuMeusHorarios() {
		return menuMeusHorarios;
	}

	public void setMenuMeusHorarios(Boolean menuMeusHorarios) {
		this.menuMeusHorarios = menuMeusHorarios;
	}

	public Boolean getConsultaRequerimento() {
		return consultaRequerimento;
	}

	public void setConsultaRequerimento(Boolean consultaRequerimento) {
		this.consultaRequerimento = consultaRequerimento;
	}

	public Boolean getMenuRequerimento() {
		return menuRequerimento;
	}

	public void setMenuRequerimento(Boolean menuRequerimento) {
		this.menuRequerimento = menuRequerimento;
	}

	public Boolean getNovoRequerimento() {
		return novoRequerimento;
	}

	public void setNovoRequerimento(Boolean novoRequerimento) {
		this.novoRequerimento = novoRequerimento;
	}

	public Boolean getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Boolean configuracao) {
		this.configuracao = configuracao;
	}

	public Boolean getNovoComunicado() {
		return novoComunicado;
	}

	public void setNovoComunicado(Boolean novoComunicado) {
		this.novoComunicado = novoComunicado;
	}

	public Boolean getLerComunicado() {
		return lerComunicado;
	}

	public void setLerComunicado(Boolean lerComunicado) {
		this.lerComunicado = lerComunicado;
	}

	public Boolean getMenuMinhasContasAPagar() {
		return menuMinhasContasAPagar;
	}

	public void setMenuMinhasContasAPagar(Boolean menuMinhasContasAPagar) {
		this.menuMinhasContasAPagar = menuMinhasContasAPagar;
	}

	public Boolean getMenuRenovacaoMatricula() {
		return menuRenovacaoMatricula;
	}

	public void setMenuRenovacaoMatricula(Boolean menuRenovacaoMatricula) {
		this.menuRenovacaoMatricula = menuRenovacaoMatricula;
	}

	public Boolean getApresentarImprimir() {
		return apresentarImprimir;
	}

	public void setApresentarImprimir(Boolean apresentarImprimir) {
		this.apresentarImprimir = apresentarImprimir;
	}

	public List<DocumetacaoMatriculaVO> getDocumetacaoMatriculaVOS() {
		return documetacaoMatriculaVOS;
	}

	public void setDocumetacaoMatriculaVOS(List<DocumetacaoMatriculaVO> documetacaoMatriculaVOS) {
		this.documetacaoMatriculaVOS = documetacaoMatriculaVOS;
	}

	public Boolean getDownloadArquivos() {
		return downloadArquivos;
	}

	public void setDownloadArquivos(Boolean downloadArquivos) {
		this.downloadArquivos = downloadArquivos;
	}

	public Boolean getPlanoDeEstudoAluno() {
		return planoDeEstudoAluno;
	}

	public void setPlanoDeEstudoAluno(Boolean planoDeEstudoAluno) {
		this.planoDeEstudoAluno = planoDeEstudoAluno;
	}

	public Boolean getApresentarContratos() {
		if (apresentarContratos == null) {
			apresentarContratos = false;
		}
		return apresentarContratos;
	}

	public void setApresentarContratos(Boolean apresentarContratos) {
		this.apresentarContratos = apresentarContratos;
	}

	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public List<CidadeVO> getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>(0);
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	/**
	 * @return the listaPrincipalHistoricos
	 */
	public List<List<HistoricoVO>> getListaPrincipalHistoricos() {
		if (listaPrincipalHistoricos == null) {
			listaPrincipalHistoricos = new ArrayList<List<HistoricoVO>>();
		}
		return listaPrincipalHistoricos;
	}

	/**
	 * @param listaPrincipalHistoricos the listaPrincipalHistoricos to set
	 */
	public void setListaPrincipalHistoricos(List<List<HistoricoVO>> listaPrincipalHistoricos) {
		this.listaPrincipalHistoricos = listaPrincipalHistoricos;
	}

	/**
	 * @return the listaSelectItemMatriculaCursoTurnoTopoAluno
	 */
	public List<SelectItem> getListaSelectItemMatriculaCursoTurnoTopoAluno() {
		return listaSelectItemMatriculaCursoTurnoTopoAluno;
	}

	/**
	 * @param listaSelectItemMatriculaCursoTurnoTopoAluno the listaSelectItemMatriculaCursoTurnoTopoAluno to set
	 */
	public void setListaSelectItemMatriculaCursoTurnoTopoAluno(List<SelectItem> listaSelectItemMatriculaCursoTurnoTopoAluno) {
		this.listaSelectItemMatriculaCursoTurnoTopoAluno = listaSelectItemMatriculaCursoTurnoTopoAluno;
	}

	public List<SelectItem> listaSelectItemMes;

	public List<SelectItem> getListaSelectItemMes() {
		if (listaSelectItemMes == null) {
			listaSelectItemMes = new ArrayList<SelectItem>();
			listaSelectItemMes.add(new SelectItem("01", "Janeiro"));
			listaSelectItemMes.add(new SelectItem("02", "Fevereiro"));
			listaSelectItemMes.add(new SelectItem("03", UteisJSF.internacionalizar("prt_Calendario_marco")));
			listaSelectItemMes.add(new SelectItem("04", "Abril"));
			listaSelectItemMes.add(new SelectItem("05", "Maio"));
			listaSelectItemMes.add(new SelectItem("06", "Junho"));
			listaSelectItemMes.add(new SelectItem("07", "Julho"));
			listaSelectItemMes.add(new SelectItem("08", "Agosto"));
			listaSelectItemMes.add(new SelectItem("09", "Setembro"));
			listaSelectItemMes.add(new SelectItem("10", "Outubro"));
			listaSelectItemMes.add(new SelectItem("11", "Novembro"));
			listaSelectItemMes.add(new SelectItem("12", "Dezembro"));
		}
		return listaSelectItemMes;
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}

	/**
	 * @param mes the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

//	public AreaProfissionalVO getAreaProfissionalVO() {
//		if (areaProfissionalVO == null) {
//			areaProfissionalVO = new AreaProfissionalVO();
//		}
//		return areaProfissionalVO;
//	}
//
//	public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
//		this.areaProfissionalVO = areaProfissionalVO;
//	}

	public DadosComerciaisVO getDadosComerciaisVO() {
		if (dadosComerciaisVO == null) {
			dadosComerciaisVO = new DadosComerciaisVO();
		}
		return dadosComerciaisVO;
	}

	public void setDadosComerciaisVO(DadosComerciaisVO dadosComerciaisVO) {
		this.dadosComerciaisVO = dadosComerciaisVO;
	}

	public FormacaoExtraCurricularVO getFormacaoExtraCurricularVO() {
		if (formacaoExtraCurricularVO == null) {
			formacaoExtraCurricularVO = new FormacaoExtraCurricularVO();
		}
		return formacaoExtraCurricularVO;
	}

	public void setFormacaoExtraCurricularVO(FormacaoExtraCurricularVO formacaoExtraCurricularVO) {
		this.formacaoExtraCurricularVO = formacaoExtraCurricularVO;
	}

	public List<SelectItem> getListaSelectItemAreaProfissional() {
		if (listaSelectItemAreaProfissional == null) {
			listaSelectItemAreaProfissional = new ArrayList<SelectItem>(0);
			montarListaSelectItemAreaProfissional();
		}
		return listaSelectItemAreaProfissional;
	}

	public void setListaSelectItemAreaProfissional(List<SelectItem> listaSelectItemAreaProfissional) {
		this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
	}

	public Boolean getMostrarBotoesGravarGeral() {
		if (mostrarBotoesGravarGeral == null) {
			return true;
		}
		return mostrarBotoesGravarGeral;
	}

	public void setMostrarBotoesGravarGeral(Boolean mostrarBotoesGravarGeral) {
		this.mostrarBotoesGravarGeral = mostrarBotoesGravarGeral;
	}

	public void processAction(ActionEvent e) {
		if (e.getComponent().getId().equals("abaQuestionario")) {
			setMostrarBotoesGravarGeral(false);
		} else {
			setMostrarBotoesGravarGeral(true);
		}
	}

	public void alterarSimplesEscolha() {

		PerguntaQuestionarioVO perguntaQuestionario = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("questoesDeBancoCurriculum");
		RespostaPerguntaVO resposta = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("opcaoRespostaItens");

		if (perguntaQuestionario.getPergunta().getTipoResposta().equals("SE")) {
			for (int i = 0; i < perguntaQuestionario.getPergunta().getRespostaPerguntaVOs().size(); i++) {
				if (perguntaQuestionario.getPergunta().getRespostaPerguntaVOs().get(i).getCodigo().intValue() == resposta.getCodigo().intValue()) {
					perguntaQuestionario.getPergunta().getRespostaPerguntaVOs().get(i).setSelecionado(true);
				} else {
					perguntaQuestionario.getPergunta().getRespostaPerguntaVOs().get(i).setSelecionado(false);
				}
			}
		}

	}

	public QuestionarioVO getQuestionarioVO() {
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public QuestionarioAlunoVO getQuestionarioAlunoVO() {
		if (questionarioAlunoVO == null) {
			questionarioAlunoVO = new QuestionarioAlunoVO();
		}
		return questionarioAlunoVO;
	}

	public void setQuestionarioAlunoVO(QuestionarioAlunoVO questionarioAlunoVO) {
		this.questionarioAlunoVO = questionarioAlunoVO;
	}

	public Boolean getMostrarModalAtualizacaoCurriculum() {
//		if (!Uteis.isAtributoPreenchido(getPessoaVO())) {
//			try {
//				if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
//					editarDadosPessoa();
//				} else {
//					editarDadosPessoaPorResponsavel();
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if (!getPessoaVO().getCurriculoAtualizado() && controlePanel <= 1) {
//			controlePanel += 1;
//			if (controlePanel == 2) {
//				return true;
//			}
//		}
		return false;
	}

//	/**
//	 * @return the listaFaltas
//	 */
//	public List<List<RegistroAulaVO>> getListaFaltas() {
//		if (listaFaltas == null) {
//			listaFaltas = new ArrayList<List<RegistroAulaVO>>(0);
//		}
//		return listaFaltas;
//	}
//
//	/**
//	 * @param listaFaltas the listaFaltas to set
//	 */
//	public void setListaFaltas(List<List<RegistroAulaVO>> listaFaltas) {
//		this.listaFaltas = listaFaltas;
//	}
//
//	/**
//	 * @return the listaDetalhesMinhasFaltasVOs
//	 */
//	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
//		if (listaDetalhesMinhasFaltasVOs == null) {
//			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
//		}
//		return listaDetalhesMinhasFaltasVOs;
//	}
//
//	/**
//	 * @param listaDetalhesMinhasFaltasVOs the listaDetalhesMinhasFaltasVOs to set
//	 */
//	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
//		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
//	}

	/**
	 * @return the permitirAlterarUsername
	 */
	public Boolean getPermitirAlterarUsername() {
		return permitirAlterarUsername;
	}

	/**
	 * @param permitirAlterarUsername the permitirAlterarUsername to set
	 */
	public void setPermitirAlterarUsername(Boolean permitirAlterarUsername) {
		this.permitirAlterarUsername = permitirAlterarUsername;
	}

	public Boolean getExisteProcessoMatriculaAberto() {
		if (existeProcessoMatriculaAberto == null && getMatricula() != null
				&& !getMatricula().getMatricula().isEmpty()) {
			try {
				if (getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno() && !getFacadeFactory().getProcessoMatriculaFacade()
						.consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(
								getMatricula().getTurno().getCodigo(), getMatricula().getCurso().getCodigo(),
								getMatricula().getUnidadeEnsino().getCodigo(), "AT", "PR_AT", true, false,
								 Uteis.NIVELMONTARDADOS_COMBOBOX, getMatricula().getMatricula(), getUsuarioLogado(), TipoAlunoCalendarioMatriculaEnum.VETERANO)
						.isEmpty()) {
					existeProcessoMatriculaAberto = true;
				} else {
					existeProcessoMatriculaAberto = false;
				}
			} catch (Exception e) {
				existeProcessoMatriculaAberto = false;
			}

		}
		return existeProcessoMatriculaAberto;
	}

	public void setExisteProcessoMatriculaAberto(Boolean existeProcessoMatriculaAberto) {
		this.existeProcessoMatriculaAberto = existeProcessoMatriculaAberto;
	}

	public List<SelectItem> getListaDeAlunosPorResponsavelLegal() {
		if (listaDeAlunosPorResponsavelLegal == null) {
			listaDeAlunosPorResponsavelLegal = new ArrayList<SelectItem>();
		}
		return listaDeAlunosPorResponsavelLegal;
	}

	public void setListaDeAlunosPorResponsavelLegal(List<SelectItem> listaDeAlunosPorResponsavelLegal) {
		this.listaDeAlunosPorResponsavelLegal = listaDeAlunosPorResponsavelLegal;
	}

	public UsuarioVO getAlunoPorResponsavelLegal() {
		if (alunoPorResponsavelLegal == null) {
			alunoPorResponsavelLegal = new UsuarioVO();
		}
		return alunoPorResponsavelLegal;
	}

	public void setAlunoPorResponsavelLegal(UsuarioVO alunoPorResponsavelLegal) {
		this.alunoPorResponsavelLegal = alunoPorResponsavelLegal;
	}

	public void selecionarCidadeEmpresa() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeEmpresaItens");
		getDadosComerciaisVO().setCidadeEmpresa(obj);
		getListaConsultaCidadeEmpresa().clear();
		this.setValorConsultaCidadeEmpresa("");
		this.setCampoConsultaCidadeEmpresa("");
	}

	/**
	 * Mï¿½todo responsï¿½vel por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
	 */
	public List<SelectItem> tipoConsultaCidadeEmpresa;

	public List<SelectItem> getTipoConsultaCidadeEmpresa() {
		if (tipoConsultaCidadeEmpresa == null) {
			tipoConsultaCidadeEmpresa = new ArrayList<SelectItem>(0);
			tipoConsultaCidadeEmpresa.add(new SelectItem("nome", "Nome"));
			tipoConsultaCidadeEmpresa.add(new SelectItem("codigo", "Código"));
			tipoConsultaCidadeEmpresa.add(new SelectItem("estado", "Estado"));
		}
		return tipoConsultaCidadeEmpresa;
	}

	private String campoConsultaCidadeEmpresa;
	private String valorConsultaCidadeEmpresa;
	private List<CidadeVO> listaConsultaCidadeEmpresa;

	/**
	 * @return the campoConsultaCidadeEmpresa
	 */
	public String getCampoConsultaCidadeEmpresa() {
		if (campoConsultaCidadeEmpresa == null) {
			campoConsultaCidadeEmpresa = "";
		}
		return campoConsultaCidadeEmpresa;
	}

	/**
	 * @param campoConsultaCidadeEmpresa the campoConsultaCidadeEmpresa to set
	 */
	public void setCampoConsultaCidadeEmpresa(String campoConsultaCidadeEmpresa) {
		this.campoConsultaCidadeEmpresa = campoConsultaCidadeEmpresa;
	}

	/**
	 * @return the valorConsultaCidadeEmpresa
	 */
	public String getValorConsultaCidadeEmpresa() {
		if (valorConsultaCidadeEmpresa == null) {
			valorConsultaCidadeEmpresa = "";
		}
		return valorConsultaCidadeEmpresa;
	}

	/**
	 * @param valorConsultaCidadeEmpresa the valorConsultaCidadeEmpresa to set
	 */
	public void setValorConsultaCidadeEmpresa(String valorConsultaCidadeEmpresa) {
		this.valorConsultaCidadeEmpresa = valorConsultaCidadeEmpresa;
	}

	/**
	 * @return the listaConsultaCidadeEmpresa
	 */
	public List<CidadeVO> getListaConsultaCidadeEmpresa() {
		if (listaConsultaCidadeEmpresa == null) {
			listaConsultaCidadeEmpresa = new ArrayList<CidadeVO>(0);
		}
		return listaConsultaCidadeEmpresa;
	}

	/**
	 * @param listaConsultaCidadeEmpresa the listaConsultaCidadeEmpresa to set
	 */
	public void setListaConsultaCidadeEmpresa(List<CidadeVO> listaConsultaCidadeEmpresa) {
		this.listaConsultaCidadeEmpresa = listaConsultaCidadeEmpresa;
	}

	@SuppressWarnings("unchecked")
	public void consultarCidadeEmpresa() {
		try {
			List<CidadeVO> objs = new ArrayList<CidadeVO>(0);
			if (getCampoConsultaCidadeEmpresa().equals("codigo")) {
				if (getValorConsultaCidadeEmpresa().equals("")) {
					setValorConsultaCidadeEmpresa("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidadeEmpresa());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidadeEmpresa().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidadeEmpresa(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidadeEmpresa().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidadeEmpresa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCidadeEmpresa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * @return the abaApresentar
	 */
	public String getAbaApresentar() {
		if (abaApresentar == null) {
			abaApresentar = "";
		}
		return abaApresentar;
	}

	/**
	 * @param abaApresentar the abaApresentar to set
	 */
	public void setAbaApresentar(String abaApresentar) {
		this.abaApresentar = abaApresentar;
	}

	public Integer getQtdeAtualizacaoForum() {
//		if (qtdeAtualizacaoForum == null) {
//			qtdeAtualizacaoForum = getFacadeFactory().getForumFacade().consultarQtdeAtualizacaoForumPorUsuarioAluno(getMatricula().getMatricula(), 0, getUsuarioLogado().getCodigo());
//			if (qtdeAtualizacaoForum >= 100) {
//				qtdeAtualizacaoForum = 99;
//			}
//		}
//		return qtdeAtualizacaoForum;
		return 0;
	}

	public void setQtdeAtualizacaoForum(Integer qtdeAtualizacaoForum) {
		this.qtdeAtualizacaoForum = qtdeAtualizacaoForum;
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

	/**
	 * @return the apresentarBotaoReposicao
	 */
	public Boolean getApresentarBotaoReposicao() {
		return apresentarBotaoReposicao;
	}

	/**
	 * @param apresentarBotaoReposicao the apresentarBotaoReposicao to set
	 */
	public void setApresentarBotaoReposicao(Boolean apresentarBotaoReposicao) {
		this.apresentarBotaoReposicao = apresentarBotaoReposicao;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaOnlineVOs() {
		if (matriculaPeriodoTurmaDisciplinaOnlineVOs == null) {
			matriculaPeriodoTurmaDisciplinaOnlineVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaOnlineVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaOnlineVOs) {
		this.matriculaPeriodoTurmaDisciplinaOnlineVOs = matriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplinaVOs() {
		if (listaMatriculaPeriodoTurmaDisciplinaVOs == null) {
			listaMatriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaMatriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setListaMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVOs) {
		this.listaMatriculaPeriodoTurmaDisciplinaVOs = listaMatriculaPeriodoTurmaDisciplinaVOs;
	}

	public List<SelectItem> getListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs() {
		if (listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs == null) {
			listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public void setListaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs(List<SelectItem> listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs) {
		this.listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs = listaSelectItemMatriculaPeriodoTurmaDisciplinaOnlineVOs;
	}

	public Integer getQtdeAtualizacaoDuvidaProfessor() {
//		if (qtdeAtualizacaoDuvidaProfessor == null) {
//			qtdeAtualizacaoDuvidaProfessor = getFacadeFactory().getDuvidaProfessorFacade().consultarQtdeAtualizacaoDuvidaPorUsuarioAluno(getMatricula().getMatricula(), getMatriculaPeriodoTurmaDisciplinaVO());
//			if (qtdeAtualizacaoDuvidaProfessor >= 100) {
//				qtdeAtualizacaoDuvidaProfessor = 99;
//			}
//		}
//		return qtdeAtualizacaoDuvidaProfessor;
		return 0;
	}

	public void setQtdeAtualizacaoDuvidaProfessor(Integer qtdeAtualizacaoDuvidaProfessor) {
		this.qtdeAtualizacaoDuvidaProfessor = qtdeAtualizacaoDuvidaProfessor;
	}

	public void uploadArquivoCurriculum(FileUploadEvent upload) {
//		try {
//			getFacadeFactory().getPessoaFacade().uploadArquivoCurriculum(upload, getPessoaVO(),  getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
//			setMensagemID("msg_arquivo_adicionado", Uteis.SUCESSO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void adicionarArquivoCurriculum() {
//		try {
//			getFacadeFactory().getPessoaFacade().adicionarArquivoCurriculum(getPessoaVO(), getCurriculumPessoaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
//			setCurriculumPessoaVO(new CurriculumPessoaVO());
//			setMensagemID("msg_arquivo_adicionado", Uteis.SUCESSO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void excluirArquivoCurriculum() {
//		try {
//			getFacadeFactory().getPessoaFacade().deletarArquivoCurriculum(getPessoaVO(), (CurriculumPessoaVO) getRequestMap().get("curriculumPessoaItens"), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
//			setMensagemID("msg_arquivo_excluido", Uteis.SUCESSO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

//	public CurriculumPessoaVO getCurriculumPessoaVO() {
//		if (curriculumPessoaVO == null) {
//			curriculumPessoaVO = new CurriculumPessoaVO();
//		}
//		return curriculumPessoaVO;
//	}

//	public void setCurriculumPessoaVO(CurriculumPessoaVO curriculumPessoaVO) {
//		this.curriculumPessoaVO = curriculumPessoaVO;
//	}

	public String getCaminhoServidorDownloadCurriculum() {
//		try {
//			CurriculumPessoaVO obj = (CurriculumPessoaVO) getRequestMap().get("curriculumPessoaItens");
//			if (obj.isNovoObj()) {
//				return "abrirPopup('" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.CURRICULUM_TMP.getValue() + "/" + obj.getNomeRealArquivo() + "', '" + obj.getNomeRealArquivo() + "', 800, 600)";
//			}
//			return "abrirPopup('" + getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.CURRICULUM.getValue() + "/" + obj.getNomeRealArquivo() + "', '" + obj.getNomeRealArquivo() + "', 800, 600)";
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		}
		return "";
	}

	public List<List<HistoricoVO>> getListaHistoricoDisciplinasReprovadas() {
		if (listaHistoricoDisciplinasReprovadas == null) {
			listaHistoricoDisciplinasReprovadas = new ArrayList<List<HistoricoVO>>(0);
		}
		return listaHistoricoDisciplinasReprovadas;
	}

	public void setListaHistoricoDisciplinasReprovadas(List<List<HistoricoVO>> listaHistoricoDisciplinasReprovadas) {
		this.listaHistoricoDisciplinasReprovadas = listaHistoricoDisciplinasReprovadas;
	}

	public Boolean getApresentarListaDisciplinaReprovadas() throws Exception {

		return (!getListaHistoricoDisciplinasReprovadas().isEmpty()) && getApresentarBotaoReposicao() && getApresentarBotaoNovoRequerimento();

	}

	private Boolean apresentarBotaoNovoRequerimento;

	public Boolean getApresentarBotaoNovoRequerimento() {
		if (apresentarBotaoNovoRequerimento == null) {
			apresentarBotaoNovoRequerimento = true;
			try {
				if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPermitiAlunoPreMatriculaSolicitarRequerimento()) {
					MatriculaPeriodoVO matPer = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado());
					if (matPer != null && !matPer.getCodigo().equals(0) && !matPer.getSituacao().equals("") && matPer.isPreMatricula()) {
						apresentarBotaoNovoRequerimento = false;
					}
				}
			} catch (Exception e) {
				apresentarBotaoNovoRequerimento = true;
			}
		}
		return apresentarBotaoNovoRequerimento;
	}

	public void setApresentarBotaoNovoRequerimento(Boolean apresentarBotaoNovoRequerimento) {
		this.apresentarBotaoNovoRequerimento = apresentarBotaoNovoRequerimento;
	}

	public void realizarMontagemListaDisplinasReprovadas() throws Exception {
//		List<HistoricoVO> listaHistorico = new ArrayList<HistoricoVO>(0);
//		getListaHistoricoDisciplinasReprovadas().clear();
//		for (Iterator<List<HistoricoVO>> iterator = getListaPrincipalHistoricos().iterator(); iterator.hasNext();) {
//			List<HistoricoVO> obj1 = (List<HistoricoVO>) iterator.next();
//			for (Iterator<HistoricoVO> iterator2 = obj1.iterator(); iterator2.hasNext();) {
//				HistoricoVO obj2 = (HistoricoVO) iterator2.next();
//				if (obj2.getSituacao().equals("RE") || obj2.getSituacao().equals("RP") || obj2.getSituacao().equals("RF")) {
//					Boolean possuiRequerimento = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaReposicaoInclusaoAluno(obj2.getMatricula().getMatricula(), obj2.getDisciplina().getCodigo(), obj2.getMatriculaPeriodoTurmaDisciplina().getCodigo());
//					if (possuiRequerimento) {
//						obj2.setPermiteMarcarReposicao(false);
//					} else {
//						listaHistorico.add(obj2);
//						obj2.setPermiteMarcarReposicao(true);
//					}

//				}
//			}
//		}
//		if (!listaHistorico.isEmpty()) {
//			getListaHistoricoDisciplinasReprovadas().add(listaHistorico);
//		}
//		List<TipoRequerimentoVO> resultadoConsulta = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoAlunoMinhasNotas(Boolean.TRUE, "AT", getUnidadeEnsinoLogado().getCodigo(), getMatricula().getCurso().getCodigo(), getMatricula().getMatricula(), null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
//		;
//		if (getUsuarioLogado().getIsApresentarVisaoPais()) {
//			resultadoConsulta.addAll(getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoPais(Boolean.TRUE, getUnidadeEnsinoLogado().getCodigo(), getMatricula().getMatricula(), getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
//		}

		setApresentarBotaoReposicao(false);
	}

	public void enviarComunicado() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralPadraoSistema();
			getComunicacaoInternaVO().setTipoOrigemComunicacaoInternaEnum(null);
			getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
			getComunicacaoInternaVO().setData(new Date());
			getComunicacaoInternaVO().setEnviarEmail(true);
			getComunicacaoInternaVO().setCodigo(0);
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getComunicacaoInternaVO().setAssunto("Solicitação de Alteração Cadastrais (Parceiro)");
			// getComunicacaoInternaVO().setMensagem(obterMensagemFormatadaMensagemProfessorPostagemMaterial(notificacaoRegistroAulaNaoLancadaVO,
			// mensagemTemplate1.getMensagem()));
			getComunicacaoInternaVO().setTipoDestinatario("FU");
			getFacadeFactory().getFuncionarioFacade().carregarDados(config.getFuncionarioRespAlteracaoDados(), getUsuarioLogado());
			getComunicacaoInternaVO().setFuncionario(config.getFuncionarioRespAlteracaoDados());
			getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(config.getFuncionarioRespAlteracaoDados().getPessoa()));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioLogado(), config, null);
			setMensagemID("msg_email_enviado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the comunicacaoInternaVO
	 */
	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = inicializarDadosPadrao(new ComunicacaoInternaVO());
		}
		return comunicacaoInternaVO;
	}

	/**
	 * @param comunicacaoInternaVO the comunicacaoInternaVO to set
	 */
	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	/**
	 * @return the mensagemAviso
	 */
	public Boolean getApresentarMensagemAviso() {
		if (getMensagemAviso().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public String getMensagemAviso() {
		if (mensagemAviso == null) {
			mensagemAviso = "";
		}
		return mensagemAviso;
	}

	/**
	 * @param mensagemAviso the mensagemAviso to set
	 */
	public void setMensagemAviso(String mensagemAviso) {
		this.mensagemAviso = mensagemAviso;
	}

	public Boolean getApresentarMenuTCC() {
		if (apresentarMenuTCC == null) {
			apresentarMenuTCC = false;
		}
		return apresentarMenuTCC;
	}

	public void setApresentarMenuTCC(Boolean apresentarMenuTCC) {
		this.apresentarMenuTCC = apresentarMenuTCC;
	}

	public DocumetacaoMatriculaVO getDocumetacaoMatriculaVO() {
		if (documetacaoMatriculaVO == null) {
			documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
		}
		return documetacaoMatriculaVO;
	}

	public void setDocumetacaoMatriculaVO(DocumetacaoMatriculaVO documetacaoMatriculaVO) {
		this.documetacaoMatriculaVO = documetacaoMatriculaVO;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Integer getQtdeNovidadesTCC() {
		if (qtdeNovidadesTCC == null) {
			qtdeNovidadesTCC = 0;
		}
		return qtdeNovidadesTCC;
	}

	public void setQtdeNovidadesTCC(Integer qtdeNovidadesTCC) {
		this.qtdeNovidadesTCC = qtdeNovidadesTCC;
	}

	public String realizarRegistroAcessoAluno() {
//		try {
//			TrabalhoConclusaoCursoVO tcc = getFacadeFactory().getTrabalhoConclusaoCursoFacade().consultarTrabalhoConclusaoCursoAtualAluno(getMatricula().getMatricula());
//			tcc.setDataUltimoAcessoAluno(new Date());
//			getFacadeFactory().getTrabalhoConclusaoCursoFacade().alterarDataUltimoAcessoAluno(tcc);
//			return "tccAluno";
//		} catch (Exception e) {
//			return "tccAluno";
//		}
		return "";
	}

	public Boolean getIsApresentarMenusPrimeiroAcesso() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPrimeiroAcessoAlunoCairMeusDados().booleanValue()) {
				if (Uteis.getDateSemHora(getUsuarioLogado().getDataPrimeiroAcesso()).compareTo(Uteis.getDateSemHora(new Date())) == 0 && !getPessoaVO().getDadosPessoaisAtualizado()) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean getIsPrimeiroAcessoAlunoTCC() {
		if (isPrimeiroAcessoAlunoTCC == null) {
			isPrimeiroAcessoAlunoTCC = false;
		}
		return isPrimeiroAcessoAlunoTCC;
	}

	public void setIsPrimeiroAcessoAlunoTCC(Boolean isPrimeiroAcessoAlunoTCC) {
		this.isPrimeiroAcessoAlunoTCC = isPrimeiroAcessoAlunoTCC;
	}	


	public Boolean getMeusDadosAbaDadosBasicos() {
		if (meusDadosAbaDadosBasicos == null) {
			meusDadosAbaDadosBasicos = Boolean.FALSE;
		}
		return meusDadosAbaDadosBasicos;
	}

	public void setMeusDadosAbaDadosBasicos(Boolean meusDadosAbaDadosBasicos) {
		this.meusDadosAbaDadosBasicos = meusDadosAbaDadosBasicos;
	}

	public Boolean getMeusDadosAbaFormacaoAcademica() {
		if (meusDadosAbaFormacaoAcademica == null) {
			meusDadosAbaFormacaoAcademica = Boolean.FALSE;
		}
		return meusDadosAbaFormacaoAcademica;
	}

	public void setMeusDadosAbaFormacaoAcademica(Boolean meusDadosAbaFormacaoAcademica) {
		this.meusDadosAbaFormacaoAcademica = meusDadosAbaFormacaoAcademica;
	}

	public Boolean getMeusDadosAbaExperienciasProfissionais() {
		if (meusDadosAbaExperienciasProfissionais == null) {
			meusDadosAbaExperienciasProfissionais = Boolean.FALSE;
		}
		return meusDadosAbaExperienciasProfissionais;
	}

	public void setMeusDadosAbaExperienciasProfissionais(Boolean meusDadosAbaExperienciasProfissionais) {
		this.meusDadosAbaExperienciasProfissionais = meusDadosAbaExperienciasProfissionais;
	}

	public Boolean getMeusDadosAbaFormacaoExtraCurricular() {
		if (meusDadosAbaFormacaoExtraCurricular == null) {
			meusDadosAbaFormacaoExtraCurricular = Boolean.FALSE;
		}
		return meusDadosAbaFormacaoExtraCurricular;
	}

	public void setMeusDadosAbaFormacaoExtraCurricular(Boolean meusDadosAbaFormacaoExtraCurricular) {
		this.meusDadosAbaFormacaoExtraCurricular = meusDadosAbaFormacaoExtraCurricular;
	}

	public void verificarPermissaoVisaoAlunoMeusDados() throws Exception {
		verificarApresentarGravarDadosPessoais();
		verificarApresentarAbaDadosPessoais();
		verificarApresentarAbaDadosFormacaoAcademica();
		verificarApresentarAbaExperienciasProfissionais();
		verificarApresentarAbaFormacaoExtraCurricular();
		verificarApresentarAbaQualificaoesInformacoesComplementares();
		verificarApresentarAbaOjetivo();
		verificarApresentarAbaQuestionario();
	}

	public void verificarApresentarGravarDadosPessoais() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().incluir(Pessoa.getIdEntidade(), true, getUsuarioLogado());
			setPermitiAlterarDados(Boolean.TRUE);
		} catch (Exception e) {
			setPermitiAlterarDados(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaDadosPessoais() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaDadosBasicos", getUsuarioLogado());
			setMeusDadosAbaDadosBasicos(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaDadosBasicos(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaDadosFormacaoAcademica() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaFormacaoAcademica", getUsuarioLogado());
			setMeusDadosAbaFormacaoAcademica(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaFormacaoAcademica(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaExperienciasProfissionais() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaExperienciasProfissionais", getUsuarioLogado());
			setMeusDadosAbaExperienciasProfissionais(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaExperienciasProfissionais(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaFormacaoExtraCurricular() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaFormacaoExtraCurricular", getUsuarioLogado());
			setMeusDadosAbaFormacaoExtraCurricular(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaFormacaoExtraCurricular(Boolean.FALSE);
		}
	}

	public void imprimirDocumentoIntegracaoCurricular() {
		try {

			GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			DocumentoIntegralizacaoCurricularRelControle documentoIntegralizacaoCurricularRelControle = new DocumentoIntegralizacaoCurricularRelControle();
			documentoIntegralizacaoCurricularRelControle.imprimirDocumentoIntegracaoCurricular(getMatricula(), gradeCurricularVO);
			setFazerDownload(documentoIntegralizacaoCurricularRelControle.getFazerDownload());
			setCaminhoRelatorio(documentoIntegralizacaoCurricularRelControle.getCaminhoRelatorio());
			// executarMetodoControle(DocumentoIntegralizacaoCurricularRelControle.class.getSimpleName(),
			// "imprimirDocumentoIntegracaoCurricular", getMatricula(), gradeCurricularVO);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<MatriculaPeriodoVO> getMatriculaPeriodoContratoVOs() {
		if (matriculaPeriodoContratoVOs == null) {
			matriculaPeriodoContratoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoContratoVOs;
	}

	public void setMatriculaPeriodoContratoVOs(List<MatriculaPeriodoVO> matriculaPeriodoContratoVOs) {
		this.matriculaPeriodoContratoVOs = matriculaPeriodoContratoVOs;
	}

	public String inicializarMenuMinhasAdvertencias() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Minhas Advertencias", "Inicializando");
		consultarMinhaAdvertenciaAluno();
		return Uteis.getCaminhoRedirecionamentoNavegacao("minhasAdvertenciasAluno.xhtml");
	}

	public void consultarMinhaAdvertenciaAluno() throws Exception {
		try {
			setAdvertenciaVOs(getFacadeFactory().getAdvertenciaFacade().consultaAdvertenciaVisaoAluno(getMatricula().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void visualizarAdvertencias() {
		try {
			setAdvertenciaVO((AdvertenciaVO) context().getExternalContext().getRequestMap().get("advertenciaItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<AdvertenciaVO> getAdvertenciaVOs() {
		if (advertenciaVOs == null) {
			advertenciaVOs = new ArrayList<AdvertenciaVO>(0);
		}
		return advertenciaVOs;
	}

	public void setAdvertenciaVOs(List<AdvertenciaVO> advertenciaVOs) {
		this.advertenciaVOs = advertenciaVOs;
	}

	public boolean getIsExibirBimestre() {
		return getMatricula().getCurso().getPeriodicidade().equals("AN");
	}

	public AdvertenciaVO getAdvertenciaVO() {
		if (advertenciaVO == null) {
			advertenciaVO = new AdvertenciaVO();
		}
		return advertenciaVO;
	}

	public void setAdvertenciaVO(AdvertenciaVO advertenciaVO) {
		this.advertenciaVO = advertenciaVO;
	}

	/**
	 * @author Victor Hugo 28/11/2014
	 * 
	 */
	private List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs;

	public List<CalendarioAtividadeMatriculaVO> getCalendarioAtividadeMatriculaVOs() {
		if (calendarioAtividadeMatriculaVOs == null) {
			calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return calendarioAtividadeMatriculaVOs;
	}

	public void setCalendarioAtividadeMatriculaVOs(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs) {
		this.calendarioAtividadeMatriculaVOs = calendarioAtividadeMatriculaVOs;
	}

	public CalendarDataModelImpl getCalendarioAtividadeMatriculaEstudoOnline() {
		if (calendarioAtividadeMatriculaEstudoOnline == null) {
			calendarioAtividadeMatriculaEstudoOnline = new CalendarDataModelImpl();
		}
		return calendarioAtividadeMatriculaEstudoOnline;
	}

	public void setCalendarioAtividadeMatriculaEstudoOnline(CalendarDataModelImpl calendarioAtividadeMatriculaEstudoOnline) {
		this.calendarioAtividadeMatriculaEstudoOnline = calendarioAtividadeMatriculaEstudoOnline;
	}

	public void listarCompromissosDiaSelecionado() {
//		try {
//			setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosDoDia(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo(), getCalendarioAtividadeMatriculaEstudoOnline().getCurrentDate()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void limparCalendarioAtividadeMatricula() {
		try {
			setCalendarioAtividadeMatriculaEstudoOnline(new CalendarDataModelImpl());
			getCalendarioAtividadeMatriculaVOs().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gerarDadosGraficoSituacaoEstudos() throws Exception {

		StringBuilder graficoSituacaoEstudosAluno = new StringBuilder();
		String tituloGraficoEstudosAluno = "";
		String subTituloGraficoEstulosAluno = "";

		Map<String, Object> auxiliar = new HashMap<String, Object>();
		getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina(), getUsuarioLogado());
		// ESTUDADO...
		graficoSituacaoEstudosAluno.append("{name: 'Estudado (").append(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado"))).append("%)', data: [" + Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado")) + "], color: '#4572A7',").append("index: 3, zIndex: 300, order:3 } ");
		// PENDENTE...
		graficoSituacaoEstudosAluno.append(", {name: 'Pendente (").append(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentARealizar"))).append("%)',data: [" + (Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentARealizar"))) + "],  color: '#FF6600',").append("index: 2, zIndex: 200, order:2 } ");
		if (getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
			// ATRASADO...
			graficoSituacaoEstudosAluno.append(", {name: 'Atrasado (").append(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentAtrasado"))).append("%)', data: [").append(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentAtrasado"))).append("], color: '").append("#AA4643").append("', index: 1, zIndex: 100, order:1 }");
		}

		tituloGraficoEstudosAluno = "Evolução Estudos On-line (" + Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado")) + "%)";

		subTituloGraficoEstulosAluno = "";
		context().getExternalContext().getSessionMap().put("graficoSituacaoEstudosAluno", graficoSituacaoEstudosAluno.toString());
		context().getExternalContext().getSessionMap().put("tituloGraficoEstudosAluno", tituloGraficoEstudosAluno);
		context().getExternalContext().getSessionMap().put("subTituloGraficoEstulosAluno", subTituloGraficoEstulosAluno);
	}

	public String getModalPanelErroAcessoConteudoEstudo() {
		if (modalPanelErroAcessoConteudoEstudo == null) {
			modalPanelErroAcessoConteudoEstudo = "";
		}
		return modalPanelErroAcessoConteudoEstudo;
	}

	public void setModalPanelErroAcessoConteudoEstudo(String modalPanelErroAcessoConteudoEstudo) {
		this.modalPanelErroAcessoConteudoEstudo = modalPanelErroAcessoConteudoEstudo;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO() {
		if (matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO == null) {
			matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO) {
		this.matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO = matriculaPeriodoTurmaDisciplinaComboBoxEstudoOnlineVO;
	}

	public String getResetSelectedDate() {
		return "#{rich:component('organizer')}.resetSelectedDate()";
	}

	public void consultarAlertaAvaliacaoOnlineDisponivel() {
		try {
			//setQtdeAvaliacoesOnlineDisponiveis(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineNaoConcluidosMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));
			setQtdeAvaliacoesOnlineDisponiveis(0);
		} catch (Exception e) {
			e.printStackTrace();
			setQtdeAvaliacoesOnlineDisponiveis(0);
		}
	}

	public void consultarAlertaInteracoesAtividadeDiscursivaEstudoOnline() {
		try {
			setQtdeInteracoesAtividadeDiscursivaOnline(0);
			//setQtdeInteracoesAtividadeDiscursivaOnline(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarQtdeInteracoesProfessorPorMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), false, getUsuarioLogado()));

		} catch (Exception e) {
			e.printStackTrace();
			setQtdeInteracoesAtividadeDiscursivaOnline(0);
		}
	}

	public void consultarAlertaInteracoesAtividadeDiscursivaVisaoGeral() {
		try {
			//setQtdeInteracoesAtividadeDiscursiva(getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarQtdeInteracoesPorMatricula(getMatricula().getMatricula(), getUsuarioLogado()));
			setQtdeInteracoesAtividadeDiscursiva(0);
		} catch (Exception e) {
			e.printStackTrace();
			setQtdeInteracoesAtividadeDiscursiva(0);
		}
	}

	public Integer getQtdeAvaliacoesOnlineDisponiveis() {
		if (qtdeAvaliacoesOnlineDisponiveis == null) {
			qtdeAvaliacoesOnlineDisponiveis = 0;
		}
		return qtdeAvaliacoesOnlineDisponiveis;
	}

	public void setQtdeAvaliacoesOnlineDisponiveis(Integer qtdeAvaliacoesOnlineDisponiveis) {
		this.qtdeAvaliacoesOnlineDisponiveis = qtdeAvaliacoesOnlineDisponiveis;
	}

	public Integer getQtdeInteracoesAtividadeDiscursiva() {
		if (qtdeInteracoesAtividadeDiscursiva == null) {
			consultarAlertaInteracoesAtividadeDiscursivaVisaoGeral();
		}
		return qtdeInteracoesAtividadeDiscursiva;
	}

	public void setQtdeInteracoesAtividadeDiscursiva(Integer qtdeInteracoesAtividadeDiscursiva) {
		this.qtdeInteracoesAtividadeDiscursiva = qtdeInteracoesAtividadeDiscursiva;
	}

	public List<SelectItem> listaSelectItemSituacaoMilitar;

	public List<SelectItem> getListaSelectItemSituacaoMilitar() {
		if (listaSelectItemSituacaoMilitar == null) {
			listaSelectItemSituacaoMilitar = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoMilitar.add(new SelectItem("", ""));
			for (SituacaoMilitarEnum obj : SituacaoMilitarEnum.values()) {
				listaSelectItemSituacaoMilitar.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemSituacaoMilitar;
	}

	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public void montarGraficoAproveitamentoAvaliacaoOnlineVisaoEstudoOnline() {
		try {
			Integer codigoAvaliacaoMatricula = getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarUltimaAvalicaoOnlineRealizadaPorMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
			if (!codigoAvaliacaoMatricula.equals(0)) {
				List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs = new ArrayList<GraficoAproveitamentoAvaliacaoVO>();
				graficoAproveitamentoAvaliacaoVOs = getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().consultarAproveitamentoAvaliacaoOnlineAluno(codigoAvaliacaoMatricula);
				getAvaliacaoOnlineMatriculaVO().setCodigo(codigoAvaliacaoMatricula);
				getAvaliacaoOnlineMatriculaVO().setGraficoAproveitamentoAvaliacaoVOs(getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().realizarParametrosGraficoAvaliacaoOnlineMatricula(graficoAproveitamentoAvaliacaoVOs, getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), 200));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MonitorConhecimentoVO getMonitorConhecimentoVO() {
		if (monitorConhecimentoVO == null) {
			monitorConhecimentoVO = new MonitorConhecimentoVO();
		}
		return monitorConhecimentoVO;
	}

	public void setMonitorConhecimentoVO(MonitorConhecimentoVO monitorConhecimentoVO) {
		this.monitorConhecimentoVO = monitorConhecimentoVO;
	}

	public Boolean getMenuNovaMatricula() {
		if (menuNovaMatricula == null) {
			menuNovaMatricula = false;
		}
		return menuNovaMatricula;
	}

	public void setMenuNovaMatricula(Boolean menuNovaMatricula) {
		this.menuNovaMatricula = menuNovaMatricula;
	}

	public void inicializarBanner() {
		try {
//			getLoginControle().inicializarBanner();
//			StringBuilder html = new StringBuilder();
//			String url = "";
//			List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs;
//			politicaDivulgacaoMatriculaOnlineVOs = getFacadeFactory()
//					.getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarBanners(getUsuarioLogadoClone());
//			for (PoliticaDivulgacaoMatriculaOnlineVO politica : politicaDivulgacaoMatriculaOnlineVOs) {
//				url = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
////				url = "http://192.168.0.56/SEI/"
//						+ politica.getCaminhoBaseLogo().replaceAll("\\\\", "/").trim() + "/"
//						+ politica.getNomeArquivoLogo().replaceAll("\\\\", "/").trim();
//				html.append("<li class=\"col-md-12 text-center\">");
//				html.append("<a href=\"" + UteisJSF.getUrlAplicacaoExterna()
//						+ "/visaoAluno/matriculaOnlineVisaoAlunoForm.xhtml?banner=" + politica.getCodigo() + "\"  >");
//				html.append(" 	<img u=\"image\" id=\"bannerMat").append(politica.getCodigo()).append("\" src=\"").append(url).append("\" height=\"290px\"  title=\""+politica.getNome()+"\" />");
//				html.append("</a>");
//				html.append("</li>");
//			}
//			setCaminhoArquivoBanner(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getIsApresentarCampoAno() {
		return getMatricula().getCurso().getPeriodicidade().equals("AN") || getMatricula().getCurso().getPeriodicidade().equals("SE");
	}

	public boolean getIsApresentarCampoSemestre() {
		return getMatricula().getCurso().getPeriodicidade().equals("SE");
	}

	/**
	 * Responsável por definir qual perfil acesso logar de acordo com o nível educacional do curso na visão dos pais.
	 * 
	 * @author Wellington - 26 de ago de 2015
	 * @return PerfilAcessoVO
	 * @throws Exception
	 */
	private PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoPais(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		PerfilAcessoVO perfilAcessoLogar = null;

		switch (TipoNivelEducacional.getEnum(getMatricula().getCurso().getNivelEducacional())) {
		case INFANTIL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoInfantil();
			break;
		case BASICO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoFundamental();
			break;
		case MEDIO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoMedio();
			break;
		case EXTENSAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoExtensao();
			break;
		case SEQUENCIAL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoSequencial();
			break;
		case GRADUACAO_TECNOLOGICA:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoGraduacaoTecnologica();
			break;
		case SUPERIOR:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoGraduacao();
			break;
		case POS_GRADUACAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoPosGraduacao();
			break;
		case MESTRADO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoPosGraduacao();
			break;
		case PROFISSIONALIZANTE:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoTecnicoProfissionalizante();
			break;
		}
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		return configuracaoGeralSistemaVO.getPerfilPadraoPais();
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

	/**
	 * @return the listaSelectItemAnoSemestre
	 */
	public List<SelectItem> getListaSelectItemAnoSemestre() {
		if (listaSelectItemAnoSemestre == null) {
			listaSelectItemAnoSemestre = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoSemestre;
	}

	/**
	 * @param listaSelectItemAnoSemestre the listaSelectItemAnoSemestre to set
	 */
	public void setListaSelectItemAnoSemestre(List<SelectItem> listaSelectItemAnoSemestre) {
		this.listaSelectItemAnoSemestre = listaSelectItemAnoSemestre;
	}

	/**
	 * @return the filterAnoSemestre
	 */
	public String getFilterAnoSemestre() {
		if (filterAnoSemestre == null) {
			filterAnoSemestre = "";
		}
		return filterAnoSemestre;
	}

	/**
	 * @param filterAnoSemestre the filterAnoSemestre to set
	 */
	public void setFilterAnoSemestre(String filterAnoSemestre) {
		this.filterAnoSemestre = filterAnoSemestre;
	}

	public void imprimirMatrizCurricularAluno() {
		try {
			GradeCurricularAlunoRelControle gradeCurricularAlunoRelControle = (GradeCurricularAlunoRelControle) context().getExternalContext().getSessionMap().get(GradeCurricularAlunoRelControle.class.getSimpleName());
			if (gradeCurricularAlunoRelControle == null) {
				gradeCurricularAlunoRelControle = new GradeCurricularAlunoRelControle();
				context().getExternalContext().getSessionMap().put(GradeCurricularAlunoRelControle.class.getSimpleName(), gradeCurricularAlunoRelControle);
			}
			gradeCurricularAlunoRelControle.setMatriculaVO(getMatricula());
			gradeCurricularAlunoRelControle.setLayout("layout2");
			gradeCurricularAlunoRelControle.setVisaoAluno(true);
			gradeCurricularAlunoRelControle.imprimirPDF();
			setFazerDownload(gradeCurricularAlunoRelControle.getFazerDownload());
			setCaminhoRelatorio(gradeCurricularAlunoRelControle.getCaminhoRelatorio());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private String linkBannerMatricula;

	public String novaMatricula() {
		return linkBannerMatricula;
	}

	public String getLinkBannerMatricula() {
		if (linkBannerMatricula == null) {
			linkBannerMatricula = "";
		}
		return linkBannerMatricula;
	}

	public void setLinkBannerMatricula(String linkBannerMatricula) {
		this.linkBannerMatricula = linkBannerMatricula;
	}

	public Boolean getPermitiAlterarDados() {
		if (permitiAlterarDados == null) {
			permitiAlterarDados = Boolean.FALSE;
		}
		return permitiAlterarDados;
	}

	public void setPermitiAlterarDados(Boolean permitiAlterarDados) {
		this.permitiAlterarDados = permitiAlterarDados;
	}

	public boolean isApresentarMinhasNotasPbl() {
		return apresentarMinhasNotasPbl;
	}

	public void setApresentarMinhasNotasPbl(boolean apresentarMinhasNotasPbl) {
		this.apresentarMinhasNotasPbl = apresentarMinhasNotasPbl;
	}

	private String qrCode;

	public void montarQRCodeLoginAplicativo() throws Exception {
		AplicativoSEISV webServiceAplicativo = new AplicativoSEISV();
		QRCodeLoginRSVO qrCodeLoginRSVO = new QRCodeLoginRSVO();
		// qrCodeLoginRSVO.setCodigoUnidadeEnsino(getMatricula().getUnidadeEnsino().getCodigo());
		// qrCodeLoginRSVO.setNomeUnidadeEnsino(getMatricula().getUnidadeEnsino().getNome());
		// qrCodeLoginRSVO.setMatricula(getMatricula().getMatricula());
		// qrCodeLoginRSVO.setNomeAluno(getUsuarioLogado().getPessoa().getNome());
		// qrCodeLoginRSVO.setCodigoCurso(getMatricula().getCurso().getCodigo());
		// qrCodeLoginRSVO.setNomeCurso(getMatricula().getCurso().getNome());
		// qrCodeLoginRSVO.setTipoRecursoEducacional(getMatricula().getCurso().getNivelEducacional());
		// qrCodeLoginRSVO.setUrlFotoPerfilAluno(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getUsuarioLogado().getPessoa().getArquivoImagem(),
		// PastaBaseArquivoEnum.IMAGEM.getValue(),
		// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
		// "foto_usuario.jpg", false));
		qrCodeLoginRSVO.setUrlBaseWS(webServiceAplicativo.getCaminhoWeb(getUsuarioLogadoClone()) + "webservice/aplicativoSEISV");
		qrCodeLoginRSVO.setLogin(getUsuarioLogado().getUsername());
		qrCodeLoginRSVO.setSenha(getUsuarioLogado().getSenha());
		Gson convert = new Gson();
		setQrCode(convert.toJson(qrCodeLoginRSVO));
	}

	public String getQrCode() {
		if (qrCode == null) {
			qrCode = "";
		}
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Boolean getApresentarModalResetarSenha() {
		if (apresentarModalResetarSenha == null) {
			apresentarModalResetarSenha = Boolean.FALSE;
		}
		return apresentarModalResetarSenha;
	}

	public void setApresentarModalResetarSenha(Boolean apresentarModalResetarSenha) {
		this.apresentarModalResetarSenha = apresentarModalResetarSenha;
	}

	public Boolean getBloquearMenuVisaoAluno() {
		if (bloquearMenuVisaoAluno == null) {
			bloquearMenuVisaoAluno = Boolean.FALSE;
		}
		return bloquearMenuVisaoAluno;
	}

	public void setBloquearMenuVisaoAluno(Boolean bloquearMenuVisaoAluno) {
		this.bloquearMenuVisaoAluno = bloquearMenuVisaoAluno;
	}

	public List<SelectItem> listaSelectItemEstadoEmissaoRGPessoa;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SelectItem> getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
		if (listaSelectItemEstadoEmissaoRGPessoa == null) {
			listaSelectItemEstadoEmissaoRGPessoa = new ArrayList<SelectItem>(0);
			listaSelectItemEstadoEmissaoRGPessoa.add(new SelectItem("", ""));
			Hashtable estados = (Hashtable) Dominios.getEstado();
			Enumeration keys = estados.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) estados.get(value);
				listaSelectItemEstadoEmissaoRGPessoa.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemEstadoEmissaoRGPessoa, ordenador);
		}
		return listaSelectItemEstadoEmissaoRGPessoa;
	}

	public List<SelectItem> listaSelectItemCorRaca;

	public List<SelectItem> getListaSelectItemCorRaca() throws Exception {
		if (listaSelectItemCorRaca == null) {
			listaSelectItemCorRaca = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class, false);
		}
		return listaSelectItemCorRaca;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemNacionalidade(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<PaizVO> resultadoConsulta = null;
		Iterator<PaizVO> i = null;
		try {
			resultadoConsulta = consultarPaizPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PaizVO obj = (PaizVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemNacionalidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<PaizVO> consultarPaizPorNome(String nomePrm) throws Exception {
		List<PaizVO> lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemNacionalidade() {
		try {
			montarListaSelectItemNacionalidade("");
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getListaSelectItemNacionalidade() {
		if (listaSelectItemNacionalidade == null) {
			listaSelectItemNacionalidade = new ArrayList<SelectItem>();
			montarListaSelectItemNacionalidade();
		}
		return (listaSelectItemNacionalidade);
	}

	public void setListaSelectItemNacionalidade(List<SelectItem> listaSelectItemNacionalidade) {
		this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
	}

	public List<SelectItem> tipoConsultaNaturalidade;

	public List<SelectItem> getTipoConsultaNaturalidade() {
		if (tipoConsultaNaturalidade == null) {
			tipoConsultaNaturalidade = new ArrayList<SelectItem>(0);
			tipoConsultaNaturalidade.add(new SelectItem("nome", "Nome"));
			tipoConsultaNaturalidade.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaNaturalidade;
	}

	public void consultarNaturalidade() {
		try {
			List<CidadeVO> objs = new ArrayList<CidadeVO>(0);
			if (getCampoConsultaNaturalidade().equals("codigo")) {
				if (getValorConsultaNaturalidade().equals("")) {
					setValorConsultaNaturalidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaNaturalidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaNaturalidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaNaturalidade(), false, getUsuarioLogado());
			}

			setListaConsultaNaturalidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaNaturalidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO em Naturalidade <code>Cidade/code>.
	 */
	public void selecionarNaturalidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidade");
		getPessoaVO().setNaturalidade(obj);
		getListaConsultaNaturalidade().clear();
		this.setValorConsultaNaturalidade("");
		this.setCampoConsultaNaturalidade("");
	}

	/**
	 * @return the campoConsultaNaturalidade
	 */
	public String getCampoConsultaNaturalidade() {
		return campoConsultaNaturalidade;
	}

	/**
	 * @param campoConsultaNaturalidade the campoConsultaNaturalidade to set
	 */
	public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
		this.campoConsultaNaturalidade = campoConsultaNaturalidade;
	}

	/**
	 * @return the valorConsultaNaturalidade
	 */
	public String getValorConsultaNaturalidade() {
		return valorConsultaNaturalidade;
	}

	/**
	 * @param valorConsultaNaturalidade the valorConsultaNaturalidade to set
	 */
	public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
		this.valorConsultaNaturalidade = valorConsultaNaturalidade;
	}

	/**
	 * @return the listaConsultaNaturalidade
	 */
	public List<CidadeVO> getListaConsultaNaturalidade() {
		return listaConsultaNaturalidade;
	}

	/**
	 * @param listaConsultaNaturalidade the listaConsultaNaturalidade to set
	 */
	public void setListaConsultaNaturalidade(List<CidadeVO> listaConsultaNaturalidade) {
		this.listaConsultaNaturalidade = listaConsultaNaturalidade;
	}

	public FiliacaoVO getFiliacaoVO() {
		if (filiacaoVO == null) {
			filiacaoVO = new FiliacaoVO();
		}
		return filiacaoVO;
	}

	public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
		this.filiacaoVO = filiacaoVO;
	}

	public Boolean getAmbienteVisaoAluno() {
		if (ambienteVisaoAluno == null) {
			ambienteVisaoAluno = false;
		}
		return ambienteVisaoAluno;
	}

	public void setAmbienteVisaoAluno(Boolean ambienteVisaoAluno) {
		this.ambienteVisaoAluno = ambienteVisaoAluno;
	}
	
	public boolean isApresentarDashboardEstagio() {		
		return apresentarDashboardEstagio;
	}

	public void setApresentarDashboardEstagio(boolean apresentarDashboardEstagio) {
		this.apresentarDashboardEstagio = apresentarDashboardEstagio;
	}

	public List<SelectItem> listaSelectItemTipoFiliacao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<SelectItem> getListaSelectItemTipoFiliacao() throws Exception {
		if (listaSelectItemTipoFiliacao == null) {
			listaSelectItemTipoFiliacao = new ArrayList<SelectItem>(0);
			listaSelectItemTipoFiliacao.add(new SelectItem("", ""));
			Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
			Enumeration keys = tipoFiliacaos.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoFiliacaos.get(value);
				listaSelectItemTipoFiliacao.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) listaSelectItemTipoFiliacao, ordenador);
		}
		return listaSelectItemTipoFiliacao;
	}

	public Integer getColumn() throws Exception {
		if (getPessoaVO().getFiliacaoVOs().size() > 4) {
			return 4;
		}
		return getPessoaVO().getFiliacaoVOs().size();
	}

	public Integer getElement() throws Exception {
		return getPessoaVO().getFiliacaoVOs().size();
	}

	public void carregarFiliacaoPessoa() {
		try {
			FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
			if (obj != null) {
				obj = getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(obj, getPessoaVO(), true, getUsuarioLogado());
				if (obj.getPais().getCodigo().intValue() == 0 || obj.getPais().getCEP().isEmpty()) {
					obj.getPais().setCEP(getPessoaVO().getCEP());
					obj.getPais().setEndereco(getPessoaVO().getEndereco());
					obj.getPais().setSetor(getPessoaVO().getSetor());
					obj.getPais().setNumero(getPessoaVO().getNumero());
					obj.getPais().setComplemento(getPessoaVO().getComplemento());
					obj.getPais().setCidade(getPessoaVO().getCidade());
				}
			} else {
				filiacaoEdicaoVO = getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(getFiliacaoEdicaoVO(), getPessoaVO(), true, getUsuarioLogado());
				if (getFiliacaoEdicaoVO().getPais().getCodigo().intValue() == 0 || getFiliacaoEdicaoVO().getPais().getCEP().isEmpty()) {
					getFiliacaoEdicaoVO().getPais().setCEP(getPessoaVO().getCEP());
					getFiliacaoEdicaoVO().getPais().setEndereco(getPessoaVO().getEndereco());
					getFiliacaoEdicaoVO().getPais().setSetor(getPessoaVO().getSetor());
					getFiliacaoEdicaoVO().getPais().setNumero(getPessoaVO().getNumero());
					getFiliacaoEdicaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
					getFiliacaoEdicaoVO().getPais().setCidade(getPessoaVO().getCidade());
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getQtdeInteracoesAtividadeDiscursivaOnline() {
		if (qtdeInteracoesAtividadeDiscursivaOnline == null) {
			qtdeInteracoesAtividadeDiscursivaOnline = 0;
		}
		return qtdeInteracoesAtividadeDiscursivaOnline;
	}

	public void setQtdeInteracoesAtividadeDiscursivaOnline(Integer qtdeInteracoesAtividadeDiscursivaOnline) {
		this.qtdeInteracoesAtividadeDiscursivaOnline = qtdeInteracoesAtividadeDiscursivaOnline;
	}

	public String editarDadosPessoaPorResponsavel() throws Exception {
		try {
			setMensagemAviso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Editar Dados Pessoa", "Editando");
//			getPessoaVO().setCurriculumPessoaVOs(getFacadeFactory().getCurriculumPessoaFacade().consultarPorPessoa(getPessoaVO().getCodigo()));
			getPessoaVO().setNovoObj(Boolean.FALSE);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			inicializarMenuDadosPessoais();
//			montarListaSelectItemCidade();
//			montarListaSelectItemAreaConhecimento();
//			montarListaSelectItemCursoAluno();
			if (getPessoaVO().getQtdFilhos() > 0) {
				getPessoaVO().setPossuiFilho(true);
			}
			verificarPermissaoVisaoAlunoMeusDados();
			getPessoaVO().limparDadosSegundoRegraAtualizacaoCadastral(getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Finalizando Editar Dados Pessoa", "Editando");
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("dadosPessoaisAluno.xhtml");
		}
	}

	public Boolean getMostrarSelectCursos() {
		if (mostrarSelectCursos == null) {
			mostrarSelectCursos = Boolean.TRUE;
		}
		return mostrarSelectCursos;
	}

	public void setMostrarSelectCursos(Boolean mostrarSelectCursos) {
		this.mostrarSelectCursos = mostrarSelectCursos;
	}

	public List<SelectItem> getListaPessoasEditaveis() {
		if (listaPessoasEditaveis == null) {
			listaPessoasEditaveis = new ArrayList<SelectItem>(0);
		}
		return listaPessoasEditaveis;
	}

	public void setListaPessoasEditaveis(List<SelectItem> pessoasEditaveis) {
		this.listaPessoasEditaveis = pessoasEditaveis;
	}

	public void removerObjetosModuloEadMemoria() {
		removerControleMemoriaFlashTela("ConteudoControle");
		removerControleMemoriaFlashTela("GestaoEventoConteudoTurmaControle");
		removerControleMemoriaFlashTela("DuvidaProfessorControle");
		removerControleMemoriaFlashTela("AvaliacaoOnlineMatriculaControle");
		removerControleMemoriaFlashTela("AtividadeDiscursivaControle");
		removerControleMemoriaFlashTela("ListaExercicioControle");
		removerControleMemoriaFlashTela("ForumControle");
	}

	public String getUrlMoodle() {
		if (urlMoodle == null) {
			urlMoodle = "";
		}
		return urlMoodle;
	}

	public void setUrlMoodle(String urlMoodle) {
		this.urlMoodle = urlMoodle;
	}

	public String inicializarMenuDeclaracaoImpostoRenda() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "VisaoAlunoControle", "Inicializando Menu Declaração Imposto de Renda", "Inicializando");
		return Uteis.getCaminhoRedirecionamentoNavegacao("declaracaoImpostoRenda.xhtml");
	}

	public String selecionarDisciplinaParaReposicao(Integer disciplina) {
		removerControleMemoriaFlashTela("RequerimentoControle");
		context().getExternalContext().getSessionMap().put("disciplinaReposicao", disciplina);
		return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAluno.xhtml");
	}

	public Boolean getMeusDadosAbaQualificaoesInformacoesComplementares() {
		if (meusDadosAbaQualificaoesInformacoesComplementares == null) {
			meusDadosAbaQualificaoesInformacoesComplementares = Boolean.FALSE;
		}
		return meusDadosAbaQualificaoesInformacoesComplementares;
	}

	public void setMeusDadosAbaQualificaoesInformacoesComplementares(Boolean meusDadosAbaQualificaoesInformacoesComplementares) {
		this.meusDadosAbaQualificaoesInformacoesComplementares = meusDadosAbaQualificaoesInformacoesComplementares;
	}

	public Boolean getMeusDadosAbaObjetivo() {
		if (meusDadosAbaObjetivo == null) {
			meusDadosAbaObjetivo = Boolean.FALSE;
		}
		return meusDadosAbaObjetivo;
	}

	public void setMeusDadosAbaObjetivo(Boolean meusDadosAbaObjetivo) {
		this.meusDadosAbaObjetivo = meusDadosAbaObjetivo;
	}

	public Boolean getMeusDadosAbaQuestionario() {
		if (meusDadosAbaQuestionario == null) {
			meusDadosAbaQuestionario = Boolean.FALSE;
		}
		return meusDadosAbaQuestionario;
	}

	public void setMeusDadosAbaQuestionario(Boolean meusDadosAbaQuestionario) {
		this.meusDadosAbaQuestionario = meusDadosAbaQuestionario;
	}

	public void verificarApresentarAbaQualificaoesInformacoesComplementares() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaQualificaoesInformacoesComplementares", getUsuarioLogado());
			setMeusDadosAbaQualificaoesInformacoesComplementares(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaQualificaoesInformacoesComplementares(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaOjetivo() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaObjetivo", getUsuarioLogado());
			setMeusDadosAbaObjetivo(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaObjetivo(Boolean.FALSE);
		}
	}

	public void verificarApresentarAbaQuestionario() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MeusDadosAbaQuestionario", getUsuarioLogado());
			setMeusDadosAbaQuestionario(Boolean.TRUE);
		} catch (Exception e) {
			setMeusDadosAbaQuestionario(Boolean.FALSE);
		}
	}

//	private CalendarioHorarioAulaVO<DataEventosRSVO> calendarioDataEventoRsVO;
//	private DataEventosRSVO dataEventosRSVO;
//	private AgendaAlunoRSVO agendaAlunoRSVO;
//	private String modalCalendario;
//
//	public CalendarioHorarioAulaVO<DataEventosRSVO> getCalendarioDataEventoRsVO() {
//		if (calendarioDataEventoRsVO == null) {
//			calendarioDataEventoRsVO = new CalendarioHorarioAulaVO<DataEventosRSVO>();
//		}
//		return calendarioDataEventoRsVO;
//	}

//	public void setCalendarioDataEventoRsVO(CalendarioHorarioAulaVO<DataEventosRSVO> calendarioDataEventoRsVO) {
//		this.calendarioDataEventoRsVO = calendarioDataEventoRsVO;
//	}

//	public DataEventosRSVO getDataEventosRSVO() {
//		if (dataEventosRSVO == null) {
//			dataEventosRSVO = new DataEventosRSVO();
//		}
//		return dataEventosRSVO;
//	}
//
//	public void setDataEventosRSVO(DataEventosRSVO dataEventosRSVO) {
//		this.dataEventosRSVO = dataEventosRSVO;
//	}
//
//	public AgendaAlunoRSVO getAgendaAlunoRSVO() {
//		if (agendaAlunoRSVO == null) {
//			agendaAlunoRSVO = new AgendaAlunoRSVO();
//		}
//		return agendaAlunoRSVO;
//	}
//
//	public void setAgendaAlunoRSVO(AgendaAlunoRSVO agendaAlunoRSVO) {
//		this.agendaAlunoRSVO = agendaAlunoRSVO;
//	}
//
//	public String getModalCalendario() {
//		if (modalCalendario == null) {
//			modalCalendario = "";
//		}
//		return modalCalendario;
//	}
//
//	public void setModalCalendario(String modalCalendario) {
//		this.modalCalendario = modalCalendario;
//	}

	public void realizarInicializacaoCalendarioAluno() {
//		try {
//			Date dataBase = new Date();
//			consultarCalendarioAluno(MesAnoEnum.getMesData(dataBase), Uteis.getAnoData(dataBase));
//			if (getCalendarioDataEventoRsVO().getObjetoSelecionado() != null && getCalendarioDataEventoRsVO().getObjetoSelecionado() instanceof DataEventosRSVO) {
//				setDataEventosRSVO((DataEventosRSVO) getCalendarioDataEventoRsVO().getObjetoSelecionado());
//				if (getDataEventosRSVO().isCssHorarioRegistroLancado() || getDataEventosRSVO().isCssHorarioFeriado()) {
//					List<MatriculaPeriodoTurmaDisciplinaVO> listaTemp = new ArrayList<>();
//					if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO())) {
//						listaTemp.add(getMatriculaPeriodoTurmaDisciplinaVO());
//					} else {
//						listaTemp.addAll(getListaMatriculaPeriodoTurmaDisciplinaVOs().stream().filter(t -> !t.getCodigo().equals(0)).collect(Collectors.toList()));
//					}
//					getDataEventosRSVO().setAgendaAlunoRSVOs(getFacadeFactory().getHorarioAlunoFacade().consultarAgendaAlunoDia(listaTemp, getDataEventosRSVO().getData(), getMatricula().getUnidadeEnsino(), getUsuarioLogadoClone()));
//				}
//				getDataEventosRSVO().setStyleClass("colunaHorarioSelecionada");
//			}
//			isPermiteConfiguracaoSeiGsuite();
//			setMensagemID("", Uteis.ALERTA);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void consultarCalendarioAluno(MesAnoEnum mesAnoEnum, Integer ano) throws Exception {
//		List<MatriculaPeriodoTurmaDisciplinaVO> listaTemp = new ArrayList<>();
//		if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO())) {
//			listaTemp.add(getMatriculaPeriodoTurmaDisciplinaVO());
//		} else {
//			listaTemp.addAll(getListaMatriculaPeriodoTurmaDisciplinaVOs().stream().filter(t -> !t.getCodigo().equals(0)).collect(Collectors.toList()));
//		}
////		setCalendarioDataEventoRsVO(getFacadeFactory().getHorarioAlunoFacade().realizarGeracaoCalendarioAluno(listaTemp, getMatricula().getMatricula(), getMatricula().getUnidadeEnsino(), mesAnoEnum, ano, getUsuarioLogadoClone()));
//		PessoaGsuiteVO pessoaGsuite = getFacadeFactory().getPessoaGsuiteFacade().consultarPorPessoaPorUnidadeEnsino(getMatricula().getAluno().getCodigo(), getMatricula().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogadoClone());
//		if (Uteis.isAtributoPreenchido(pessoaGsuite)) {
//			getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO().add(pessoaGsuite);
//		}
//		PessoaEmailInstitucionalVO pei = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogadoClone());
//		if (Uteis.isAtributoPreenchido(pei)) {
//			getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().add(pei);
//		}
//		setDataEventosRSVO(new DataEventosRSVO());
//		setAgendaAlunoRSVO(new AgendaAlunoRSVO());
	}

	public void visualizarCalendarioProximoMes() {
//		try {
//			if (getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior().equals(MesAnoEnum.JANEIRO)) {
//				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior(), Integer.parseInt(getCalendarioDataEventoRsVO().getAno()) + 1);
//			} else {
//				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoPosterior(), Integer.parseInt(getCalendarioDataEventoRsVO().getAno()));
//			}
//			setMensagemID("", Uteis.ALERTA);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void visualizarCalendarioAnteriorMes() {
//		try {
//			if (getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior().equals(MesAnoEnum.DEZEMBRO)) {
//				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior(), Integer.parseInt(getCalendarioDataEventoRsVO().getAno()) - 1);
//			} else {
//				consultarCalendarioAluno(getCalendarioDataEventoRsVO().getMesAno().getMesAnoAnterior(), Integer.parseInt(getCalendarioDataEventoRsVO().getAno()));
//			}
//			setMensagemID("", Uteis.ALERTA);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void selecionarDiaCalendario() {
//		try {
//			DataEventosRSVO dataEventoRSVO = (DataEventosRSVO) context().getExternalContext().getRequestMap().get("diaAgendaItens");
//
//			if (getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty()) {
//				getDataEventosRSVO().setStyleClass("colunaHorarioLivre");
//			} else if (!getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty() && getDataEventosRSVO().getAgendaAlunoRSVOs().stream().allMatch(p -> p.getOrigemAgendaAluno().isFeriado())) {
//				getDataEventosRSVO().setStyleClass("horarioFeriado");
//			} else if (!getDataEventosRSVO().getAgendaAlunoRSVOs().isEmpty()) {
//				getDataEventosRSVO().setStyleClass("horarioRegistroLancado");
//			}
//			setDataEventosRSVO(dataEventoRSVO);
//			if (getDataEventosRSVO() != null && (getDataEventosRSVO().isCssHorarioRegistroLancado() || getDataEventosRSVO().isCssHorarioFeriado())) {
//				List<MatriculaPeriodoTurmaDisciplinaVO> listaTemp = new ArrayList<>();
//				if (Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVO())) {
//					listaTemp.add(getMatriculaPeriodoTurmaDisciplinaVO());
//				} else {
//					listaTemp.addAll(getListaMatriculaPeriodoTurmaDisciplinaVOs());
//				}
//				getDataEventosRSVO().setAgendaAlunoRSVOs(getFacadeFactory().getHorarioAlunoFacade().consultarAgendaAlunoDia(listaTemp, getDataEventosRSVO().getData(), getMatricula().getUnidadeEnsino(), getUsuarioLogadoClone()));
//			}
//			getDataEventosRSVO().setStyleClass("colunaHorarioSelecionada");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public String realizarNavegacaoParaEventoCalendario() {
//		String retorno = "";
//		try {
//			setModalCalendario("");
//			AgendaAlunoRSVO agendaAlunoRSVO = (AgendaAlunoRSVO) context().getExternalContext().getRequestMap().get("agendaAlunoRSVOItem");
//			if (agendaAlunoRSVO.isEventoAvaliacaoOnline()) {
//				executarMontagemDadosEstudoOnline(agendaAlunoRSVO.getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO());
//				setRealizarNavegacaoParaAvaliacaoOnline(Boolean.TRUE);
//				retorno = validarExistenciaAvaliacaoOnlineEmRealizacao();
//			} else if (agendaAlunoRSVO.isEventoAtividadeDiscursiva()) {
//				retorno = "atividadeDiscursivaAlunoCons.xhtml";
//				if (Uteis.isAtributoPreenchido(agendaAlunoRSVO.getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getConteudo())) {
//					executarMontagemDadosEstudoOnline(agendaAlunoRSVO.getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO());
//					retorno = "atividadeDiscursivaAlunoEstudoOnlineCons.xhtml";
//				}
//				context().getExternalContext().getSessionMap().put("ativiadeDiscursiva", agendaAlunoRSVO.getCalendarioAtividadeMatriculaVO().getCodOrigem());
//				context().getExternalContext().getSessionMap().put("navegarAtividadeDiscursivaPorCalendario", true);
//			} else if (agendaAlunoRSVO.isEventoHorarioAula()) {
//				setAgendaAlunoRSVO(agendaAlunoRSVO);
//				if (Uteis.isAtributoPreenchido(getAgendaAlunoRSVO().getGoogleMeetVO())) {
//					setModalCalendario("PF('panelGoogleMeet').show();");
//				}
//				inicializarMensagemVazia();
//			}
//		} catch (Exception e) {
//			setModalCalendario("PF('panelMensagemGoogleMeet').show()");
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao(retorno);
		return "";
	}

	public void fecharModalCalendario() {
//		try {
//			setAgendaAlunoRSVO(new AgendaAlunoRSVO());
//			setModalCalendario("");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void realizarNavegacaoSalaAulaBlackboardPorTcc() {
		try {
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			if(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));	
			}else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));	
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(getMatricula().getSalaAulaBlackboardTcc(), sabAluno, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public CalendarioHorarioAulaVO<DataEventosRSVO> getCalendarioDataEventoRsVO() {
//		if (calendarioDataEventoRsVO == null) {
//			calendarioDataEventoRsVO = new CalendarioHorarioAulaVO<DataEventosRSVO>();
//		}
		return calendarioDataEventoRsVO;
	}
	
	public void realizarNavegacaoSalaAulaBlackboardPorAgenda() {
		try {
			AgendaAlunoRSVO agendaAlunoRSVO = (AgendaAlunoRSVO) context().getExternalContext().getRequestMap().get("agendaAlunoRSVOItem");
			if (getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().size() > 0) {
				SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
				sabAluno.setPessoaEmailInstitucionalVO(getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().get(0));
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(agendaAlunoRSVO.getSalaAulaBlackboardVO(), sabAluno, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNavegacaoBlackboardPorMatriculaPeriodoTurmaDisciplina() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem");
			if (getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().size() > 0) {
				SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
				sabAluno.setPessoaEmailInstitucionalVO(getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().get(0));
				sabAluno.setMatricula(mptd.getMatricula());
				sabAluno.setMatriculaPeriodoTurmaDisciplina(mptd.getCodigo());
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(mptd.getSalaAulaBlackboardVO(), sabAluno, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarNavegacaoBlackboardGrupoPorMatriculaPeriodoTurmaDisciplina() {
		try {
			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem");
			if (getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().size() > 0) {
				SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
				sabAluno.setPessoaEmailInstitucionalVO(getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().get(0));
				sabAluno.setMatricula(mptd.getMatricula());
				sabAluno.setMatriculaPeriodoTurmaDisciplina(mptd.getCodigo());
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(mptd.getSalaAulaBlackboardGrupoVO(), sabAluno, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarNavegacaoBlackboardPorEstudoOnline() {
		try {
			if (getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().size() > 0) {
				SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
				sabAluno.setPessoaEmailInstitucionalVO(getCalendarioDataEventoRsVO().getListaPessoaEmailInstitucionalVO().get(0));
				sabAluno.setMatricula(getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
				sabAluno.setMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(getMatriculaPeriodoTurmaDisciplinaVO().getSalaAulaBlackboardVO(), sabAluno, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}



	public void realizarNavegacaoClassroomPorMatriculaPeriodoTurmaDisciplina() {
//		try {
//			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem");
//			if (getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO().size() > 0) {
//				getFacadeFactory().getClassroomGoogleFacade().realizarVerificacaoSeAlunoEstaVinculadoAoClassroomGoogle(mptd.getClassroomGoogleVO(), getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO().get(0), getUsuarioLogadoClone());
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public void realizarNavegacaoClassroomPorEstudoOnline() {
//		try {
//			if (getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO().size() > 0) {
//				getFacadeFactory().getClassroomGoogleFacade().realizarVerificacaoSeAlunoEstaVinculadoAoClassroomGoogle(getMatriculaPeriodoTurmaDisciplinaVO().getClassroomGoogleVO(), getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO().get(0), getUsuarioLogadoClone());
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	private Integer qtdeDownloadsMaterialNaoRealizados;

	
	public void verificarQuantidadeDownloadsNaoRealizadosDownloadVisaoAluno() {
		setQtdeDownloadsMaterialNaoRealizados(0);
	}
	
	private void realizarVerificacaoUsuarioFacilitador() {
		try {
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				if(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().containsKey((getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha()))){
					getUsuarioLogado().setUsuarioFacilitador(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get((getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha())).isUsuarioFacilitador());
				}else {
					getUsuarioLogado().setUsuarioFacilitador(getFacadeFactory().getEstagioFacade().realizarVerificacaoUsuarioFacilitador(getUsuarioLogado().getPessoa().getCodigo()));
				}
				if(getUsuarioLogado().isUsuarioFacilitador()) {
					context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuarioLogado());	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.realizarVerificacaoUsuarioFacilitador: "+e.getMessage());
		}
	}

	

	public Integer getQtdeDownloadsMaterialNaoRealizados() {
		if (qtdeDownloadsMaterialNaoRealizados == null) {
			qtdeDownloadsMaterialNaoRealizados = 0;
		}
		return qtdeDownloadsMaterialNaoRealizados;
	}

	public void setQtdeDownloadsMaterialNaoRealizados(Integer qtdeDownloadsMaterialNaoRealizados) {
		this.qtdeDownloadsMaterialNaoRealizados = qtdeDownloadsMaterialNaoRealizados;
	}

	public Boolean getApresentarModalAlertarAlunoPendenciaDocumentacaoMatricula() {
		if (apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula == null) {
			apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula = Boolean.FALSE;
		}
		return apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula;
	}

	public void setApresentarModalAlertarAlunoPendenciaDocumentacaoMatricula(Boolean apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula) {
		this.apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula = apresentarModalAlertarAlunoPendenciaDocumentacaoMatricula;
	}

	public void verificarAlertarAlunoPendenciaDocumentacaoMatriculaLogar() {
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO()) && !getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno()) {
				return;
			}
			if (Uteis.isAtributoPreenchido(getUsuarioLogado()) && getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("EntregaDocumentoAlertarAlunoPendenciaDocumentacaoMatriculaLogar", getUsuarioLogado()) && getMatricula().getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor()) && getFacadeFactory().getDocumetacaoMatriculaFacade().consultarSeExistemDocumentosPendentesAluno(getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado())) {
				setApresentarModalAlertarAlunoPendenciaDocumentacaoMatricula(Boolean.TRUE);
				mensagemAlertaAlunoPendenciaDocumentacaoMatricula();
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.verificarAlertarAlunoPendenciaDocumentacaoMatriculaLogar: "+e.getMessage());
		}

	}

	public String getMensagemAlertaAlunoPendenciaDocumentacaoMatricula() {
		if (mensagemAlertaAlunoPendenciaDocumentacaoMatricula == null) {
			mensagemAlertaAlunoPendenciaDocumentacaoMatricula = "";
		}
		return mensagemAlertaAlunoPendenciaDocumentacaoMatricula;
	}

	public void setMensagemAlertaAlunoPendenciaDocumentacaoMatricula(String mensagemAlertaAlunoPendenciaDocumentacaoMatricula) {
		this.mensagemAlertaAlunoPendenciaDocumentacaoMatricula = mensagemAlertaAlunoPendenciaDocumentacaoMatricula;
	}

	public void mensagemAlertaAlunoPendenciaDocumentacaoMatricula() throws Exception {
//		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory()
//				.getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(
//						TemplateMensagemAutomaticaEnum.MENSAGEM_ALERTAR_ALUNO_PENDENCIA_DOCUMENTACAO_MATRICULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getMatricula().getUnidadeEnsino().getCodigo(),
//						getUsuarioLogado(), null);
//
//		List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentosPendentesPorMatricula(getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
//
//		String mensagemEditada = obterMensagemAlertaAlunoPendenciaDocumentacaoMatricula(mensagemTemplate.getMensagem(), documentacaoMatriculaVOs);
//		setMensagemAlertaAlunoPendenciaDocumentacaoMatricula(mensagemEditada);

	}

	public String obterMensagemAlertaAlunoPendenciaDocumentacaoMatricula(final String mensagemTemplate, List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(),
				getMatricula().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(),
				getMatricula().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(),
				getMatricula().getMatricula());

		StringBuilder listaDocs = new StringBuilder("<ul>");
		for (DocumetacaoMatriculaVO documentacaoMatriculaVO : documentacaoMatriculaVOs) {
			listaDocs.append("<li>");
			listaDocs.append(" <strong>").append(documentacaoMatriculaVO.getTipoDeDocumentoVO().getNome());
			if (Uteis.isAtributoPreenchido(documentacaoMatriculaVO.getJustificativaNegacao())) {
				listaDocs.append(" - Motivo do Indeferimento: ").append(documentacaoMatriculaVO.getJustificativaNegacao()).append("</strong>");
			}
			listaDocs.append("</li>");
		}
		listaDocs.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DOCUMENTOS.name(), listaDocs.toString());

		return mensagemTexto;

	}

	public Integer getQuantidadeAtividadeComplementar() {
//		if (quantidadeAtividadeComplementar == null) {
//			try {
//				quantidadeAtividadeComplementar = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarQuantidadeAtividadeComplementarPorUsuarioAluno(getMatricula().getMatricula());
//			} catch (Exception e) {
//				setMensagemDetalhada(e.getMessage());
//			}
//			if (quantidadeAtividadeComplementar >= 100) {
//				quantidadeAtividadeComplementar = 99;
//			}
//		}
//		return quantidadeAtividadeComplementar;
		return 0;
	}

	public void setQuantidadeAtividadeComplementar(Integer quantidadeAtividadeComplementar) {
		this.quantidadeAtividadeComplementar = quantidadeAtividadeComplementar;
	}

	public void fecharModalAlertarAlunoContratoNaoAssinado() {
		getMatricula().setAlunoNaoAssinouContratoMatricula(Boolean.FALSE);
	}

	public Integer getTotalInteracaoNaoLida() {
		if (totalInteracaoNaoLida == null) {
			totalInteracaoNaoLida = 0;
		}
		return totalInteracaoNaoLida;
	}

	public void setTotalInteracaoNaoLida(Integer totalInteracaoNaoLida) {
		this.totalInteracaoNaoLida = totalInteracaoNaoLida;
	}

	public Boolean getApresentarCampoProfessor() {
		if (apresentarCampoProfessor == null) {
			apresentarCampoProfessor = false;
		}
		return apresentarCampoProfessor;
	}

	public void setApresentarCampoProfessor(Boolean apresentarCampoProfessor) {
		this.apresentarCampoProfessor = apresentarCampoProfessor;
	}

	public String getAbrirModalInclusaoArquivoVerso() {
		if (abrirModalInclusaoArquivoVerso == null) {
			abrirModalInclusaoArquivoVerso = "";
		}
		return abrirModalInclusaoArquivoVerso;
	}

	public void setAbrirModalInclusaoArquivoVerso(String abrirModalInclusaoArquivoVerso) {
		this.abrirModalInclusaoArquivoVerso = abrirModalInclusaoArquivoVerso;
	}

	public void limparDocumentacaoMatriculaVO() {
		try {
			int index = 0;
			Iterator<DocumetacaoMatriculaVO> i = getDocumetacaoMatriculaVOS().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
				if (documetacaoMatriculaVO.getCodigo().equals(getDocumetacaoMatriculaVO().getCodigo())) {
					getDocumetacaoMatriculaVOS().set(index, getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatriculaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					return;
				}
				index++;
			}
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDocumentacaoMatriculaVerso() {
		try {
			if (!Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getCodigo()) && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome())) {
				String arquivoExterno = "";
				arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivo() + File.separator + (getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome());
				File arquivo = new File(arquivoExterno);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			}
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// usado na visao do aluno
	public void editarFiliacao() {
		FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
		filiacaoEdicaoVO = new FiliacaoVO();
		filiacaoEdicaoVO = obj.getClone();

	}

	public FiliacaoVO getFiliacaoEdicaoVO() {
		if (filiacaoEdicaoVO == null) {
			filiacaoEdicaoVO = new FiliacaoVO();
		}
		return filiacaoEdicaoVO;
	}
	
	public void setFiliacaoEdicaoVO(FiliacaoVO filiacaoEdicaoVO) {
		this.filiacaoEdicaoVO = filiacaoEdicaoVO;
	}

	// TODO nao está redirecionando
	public String realizarNavegacaoPagina(String url) {
//		// return Uteis.getCaminhoRedirecionamentoNavegacao(url);
//		ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaAPagarItens");
//		String url2 = "/BoletoBancarioSV?codigoContaReceber=" + obj.getCodigo() + "&codigoUsuario=" + getUsuarioLogado().getCodigo() + "&titulo=matricula&telaOrigem=visaoAluno/minhasContasPagarAluno.xhtml";
//
//		return Uteis.getCaminhoRedirecionamentoNavegacao("/BoletoBancarioSV?codigoContaReceber=" + obj.getCodigo() + "&codigoUsuario=" + getUsuarioLogado().getCodigo() + "&titulo=matricula&telaOrigem=visaoAluno/minhasContasPagarAluno");
		return "";
	}

	private void atualizarQuantidadesMenuEstudoOnline() throws Exception {
		setQtdeAtualizacaoForum(getFacadeFactory().getForumFacade().consultarQtdeAtualizacaoForumPorUsuarioAluno(getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(),  getMatriculaPeriodoTurmaDisciplinaVO().getAno().trim().isEmpty() ? "" : getMatriculaPeriodoTurmaDisciplinaVO().getSemestre().trim().isEmpty() ? getMatriculaPeriodoTurmaDisciplinaVO().getAno() : getMatriculaPeriodoTurmaDisciplinaVO().getAno()+"/"+getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getUsuarioLogado().getCodigo()));
		setQtdeAtualizacaoDuvidaProfessor(getFacadeFactory().getDuvidaProfessorFacade().consultarQtdeAtualizacaoDuvidaPorUsuarioAluno(getMatricula().getMatricula(), getMatriculaPeriodoTurmaDisciplinaVO()));
//		setQtdeAvaliacoesOnlineDisponiveis(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineNaoConcluidosMatriculaPeriodoTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));

		consultarAlertaInteracoesAtividadeDiscursivaEstudoOnline();
	}

	private String verificarAtualizacaoCadastral(UsuarioVO usuario, Boolean editarPessoaPorResponsavel) throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO().getHabilitarRecursoAtualizacaoCadastral().booleanValue()) {
			if (!getLoginControle().getPermissaoAcessoMenuVO().getAluno()) {
				setBloquearMenuVisaoAluno(Boolean.FALSE);
				return "";
			}
			if (usuario.getPessoa().getDataUltimaAtualizacaoCadatral() == null) {
				setBloquearMenuVisaoAluno(Boolean.TRUE);
				if (editarPessoaPorResponsavel) {
					setBloquearMenuVisaoAluno(Boolean.FALSE);
					return editarDadosPessoaPorResponsavel();
				}
				return editarDadosPessoa();
			} else if (getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO().getSolicitarAtualizacaoACada().intValue() > 0) {
				long diasEntreDatas = Uteis.nrDiasEntreDatas(new Date(), usuario.getPessoa().getDataUltimaAtualizacaoCadatral());
				if (diasEntreDatas > getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO().getSolicitarAtualizacaoACada().intValue()) {
					setBloquearMenuVisaoAluno(Boolean.TRUE);
					if (editarPessoaPorResponsavel) {
						setBloquearMenuVisaoAluno(Boolean.FALSE);
						return editarDadosPessoaPorResponsavel();
					}
					return editarDadosPessoa();
				}
			}
		}
		return "";
	}

	public DashboardVO getDashboardMinhasDisciplinas() {
		if (dashboardMinhasDisciplinas == null) {
			dashboardMinhasDisciplinas = new DashboardVO(TipoDashboardEnum.DISCIPLINAS_ALUNO, false, 2, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardMinhasDisciplinas;
	}

	public void setDashboardMinhasDisciplinas(DashboardVO dashboardMinhasDisciplinas) {
		this.dashboardMinhasDisciplinas = dashboardMinhasDisciplinas;
	}

	public DashboardVO getDashboardCalendario() {
		if (dashboardCalendario == null) {
			dashboardCalendario = new DashboardVO(TipoDashboardEnum.HORARIO_ALUNO, !getLoginControle().getPermissaoAcessoMenuVO().getMeusHorarios(), 3, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardCalendario;
	}

	public void setDashboardCalendario(DashboardVO dashboardCalendario) {
		this.dashboardCalendario = dashboardCalendario;
	}

	public DashboardVO getDashboardEvolucaoAcademica() {
		if (dashboardEvolucaoAcademica == null) {
			dashboardEvolucaoAcademica = new DashboardVO(TipoDashboardEnum.EVOLUCAO_ACADEMICA_ALUNO, getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoSituacaoAcademicaAluno().isEmpty(), 4, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardEvolucaoAcademica;
	}

	public void setDashboardEvolucaoAcademica(DashboardVO dashboardEvolucaoAcademica) {
		this.dashboardEvolucaoAcademica = dashboardEvolucaoAcademica;
	}

	public DashboardVO getDashboardEstagio() {
		if (dashboardEstagio == null) {
			dashboardEstagio = new DashboardVO(TipoDashboardEnum.EVOLUCAO_ACADEMICA_ESTAGIO, false, 5, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardEstagio;
	}

	public void setDashboardEstagio(DashboardVO dashboardEstagio) {
		this.dashboardEstagio = dashboardEstagio;
	}
	
	

	public DashboardVO getDashboardMapaEstagio() {
		if (dashboardMapaEstagio == null) {
			dashboardMapaEstagio = new DashboardVO(TipoDashboardEnum.MAPA_ESTAGIO, !getUsuarioLogado().isUsuarioFacilitador(), 10, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardMapaEstagio;
	}

	public void setDashboardMapaEstagio(DashboardVO dashboardMapaEstagio) {
		this.dashboardMapaEstagio = dashboardMapaEstagio;
	}
	
	

	public DashboardVO getDashboardTcc() {
		if (dashboardTcc == null) {
			dashboardTcc = new DashboardVO(TipoDashboardEnum.TCC, !Uteis.isAtributoPreenchido(getMatricula().getSalaAulaBlackboardTcc().getCodigo()), 11, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardTcc;
	}

	public void setDashboardTcc(DashboardVO dashboardTcc) {
		this.dashboardTcc = dashboardTcc;
	}

	public DashboardVO getDashboardProjetoIntegrador() {
		if (dashboardProjetoIntegrador == null) {
			dashboardProjetoIntegrador = new DashboardVO(TipoDashboardEnum.PROJETO_INTEGRADOR, !getCalendarioAgrupamentoProjetoIntegrador().isPeriodoLiberado(), 12, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardProjetoIntegrador;
	}




	public void setDashboardProjetoIntegrador(DashboardVO dashboardProjetoIntegrador) {
		this.dashboardProjetoIntegrador = dashboardProjetoIntegrador;
	}




	public DashboardVO getDashboardAtividadeComplementar() {
		if (dashboardAtividadeComplementar == null) {
			dashboardAtividadeComplementar = new DashboardVO(TipoDashboardEnum.EVOLUCAO_ACADEMICA_ATIVIDADE_COMPLEMENTAR, getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoAtividadeComplementar().isEmpty(), 6, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardAtividadeComplementar;
	}

	public void setDashboardAtividadeComplementar(DashboardVO dashboardAtividadeComplementar) {
		this.dashboardAtividadeComplementar = dashboardAtividadeComplementar;
	}

	public DashboardVO getDashboardIntegralizacaoCurricular() {
		if(dashboardIntegralizacaoCurricular == null){
			dashboardIntegralizacaoCurricular = new DashboardVO(TipoDashboardEnum.INTEGRALIZACAO_CURRICULAR, getMatricula().getMatriculaComHistoricoAlunoVO().getGraficoIntegralizacaoCurricular().isEmpty(),1, TipoVisaoEnum.ALUNO,getUsuarioLogadoClone());
		}
		return dashboardIntegralizacaoCurricular;
	}

	public void setDashboardIntegralizacaoCurricular(DashboardVO dashboardIntegralizacaoCurricular) {
		this.dashboardIntegralizacaoCurricular = dashboardIntegralizacaoCurricular;
	}

	//	public DashboardVO getDashboardVagaEmprego() {
//		if (dashboardVagaEmprego == null) {
//			dashboardVagaEmprego = new DashboardVO(TipoDashboardEnum.VAGAS_EMPREGO,
//					!getLoginControle().getPermissaoAcessoMenuVO().getBuscaVagas(), 7, TipoVisaoEnum.ALUNO,
//					getUsuarioLogadoClone());
//		}
//		return dashboardVagaEmprego;
//	}
//
//	public void setDashboardVagaEmprego(DashboardVO dashboardVagaEmprego) {
//		this.dashboardVagaEmprego = dashboardVagaEmprego;
//	}

	
	

	public void registrarPosicaoDashboard(DragDropEvent<DashboardVO> event) {
	    // 1. Recupera o objeto que foi ARRASTADO (Vem do atributo 'dragValue' do XHTML)
	    DashboardVO dragItem = event.getData();

	    // 2. Recupera o objeto ALVO onde foi solto (Vem do 'f:attribute' que vamos criar no XHTML)
	    DashboardVO dropItem = (DashboardVO) event.getComponent().getAttributes().get("dashboardAlvo");

	    // 3. Lógica de troca (Swap)
	    if (dragItem != null && dropItem != null && !dragItem.equals(dropItem)) {
	        Integer ordemDrag = dragItem.getOrdem();
	        
	        dragItem.setOrdem(dropItem.getOrdem());
	        dropItem.setOrdem(ordemDrag);
	        
	        persistirDashboard(dragItem);
	        persistirDashboard(dropItem);
	        
	        // Opcional: Recarregar a lista se necessário para refletir a ordem visualmente
	        // inicializarDashboards(); 
	    }
	}

	public void persistirDashboard(DashboardVO dashboardVO) {
		try {
			getFacadeFactory().getDashboardInterfaceFacade().persistir(dashboardVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	
	public void consultarConfiguracaoDashboard() {
		try {
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				List<DashboardVO> dashboards =  null;
				if(getAplicacaoControle().getDashboardsAlunoCache().containsKey(getUsuarioLogado().getCodigo())) {
					dashboards = getAplicacaoControle().getDashboardsAlunoCache().get(getUsuarioLogado().getCodigo());
				}else {
					dashboards = getFacadeFactory().getDashboardInterfaceFacade().consultarDashboardPorUsuarioAmbiente(getUsuarioLogado(), TipoVisaoEnum.ALUNO);
				}
			if (dashboards != null) {
				dashboards.forEach(d -> {
					switch (d.getTipoDashboard()) {
					case BANNER_MATRICULA:
						setDashboardMatricular(d);
						break;
					case DISCIPLINAS_ALUNO:
						setDashboardMinhasDisciplinas(d);
						break;
					case HORARIO_ALUNO:
						setDashboardCalendario(d);
						break;
					case EVOLUCAO_ACADEMICA_ALUNO:
						setDashboardEvolucaoAcademica(d);
						break;
					case EVOLUCAO_ACADEMICA_ATIVIDADE_COMPLEMENTAR:
						setDashboardAtividadeComplementar(d);
						break;
					case EVOLUCAO_ACADEMICA_ESTAGIO:
						setDashboardEstagio(d);
						break;
					
					case TWITTER:
						setDashboardTwitter(d);
						break;
					case MONITOR_CONHECIMENTO_EAD:
						setDashboardMonitorConhecimentoEAD(d);
						break;
					case MARKETING:
						setDashboardBannerMarketing(d);
						break;
					case EVOLUCAO_ESTUDO_ONLINE:
						setDashboardEvolucaoEstudoOnline(d);
						break;
					case MAPA_ESTAGIO:
						setDashboardMapaEstagio(d);
						break;
					case TCC:
						setDashboardTcc(d);
						break;					
					case PROJETO_INTEGRADOR:
						setDashboardProjetoIntegrador(d);
						break;					
					case LINK_UTEIS:
						getLoginControle().setDashboardLinksUteis(d);
						break;
					case INTEGRALIZACAO_CURRICULAR:
						setDashboardIntegralizacaoCurricular(d);
						break;
					}
				});
			}
			}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.consultarConfiguracaoDashboard: "+e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private String filtroAnoSemestreTelaInicial;

	public String getFiltroAnoSemestreTelaInicial() {
		return filtroAnoSemestreTelaInicial;
	}

	public void setFiltroAnoSemestreTelaInicial(String filtroAnoSemestreTelaInicial) {
		this.filtroAnoSemestreTelaInicial = filtroAnoSemestreTelaInicial;
	}

	public void montarListaAnoSemestre() {
		try {
			setListaSelectItemAnoSemestre(null);
			getListaSelectItemAnoSemestre().add(new SelectItem("", ""));
			setFilterAnoSemestre("");
			setFiltroAnoSemestreTelaInicial("");
			if (getIsApresentarCampoAno()) {
				String anoSemestre = getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(getMatricula().getMatricula(), getListaSelectItemAnoSemestre());
				setFilterAnoSemestre(anoSemestre);
				setFiltroAnoSemestreTelaInicial(anoSemestre);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDadosMatriculaPorSelecaoAnoSemestre() {
		consultarMatriculaPeriodoTurmaDisciplinaOnline();
		realizarInicializacaoCalendarioAluno();
		executarInicializacaoCalendarioAgrupamentoProjetoIntegrador();
	}

	public String realizarNavegacaoTelaInicial() {
//		consultarMatriculaPeriodoTurmaDisciplinaOnline();
		return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoAluno.xhtml");
	}

	public String navegarRecursoSelecionandoDisciplina(String tela) {
		context().getExternalContext().getSessionMap().put("disciplinaSelecionada", (MatriculaPeriodoTurmaDisciplinaVO) getRequestMap().get("matriculaPeriodoTurmaDisciplinaItem"));
		if (Uteis.isAtributoPreenchido(tela) && tela.equals("atividadeDiscursivaAlunoCons.xhtml")) {
			removerControleMemoriaFlashTela("AtividadeDiscursivaControle");
		} else if (Uteis.isAtributoPreenchido(tela) && tela.equals("forumAlunoCons.xhtml")) {
			removerControleMemoriaFlashTela("ForumControle");
		}else	if(Uteis.isAtributoPreenchido(tela) && tela.equals("downloadArquivo.xhtml")) {
			removerControleMemoriaFlashTela("ArquivoControle");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(tela);
	}

	public DashboardVO getDashboardMonitorConhecimentoEAD() {
		if (dashboardMonitorConhecimentoEAD == null) {
			dashboardMonitorConhecimentoEAD = new DashboardVO(TipoDashboardEnum.MONITOR_CONHECIMENTO_EAD, false, 10, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardMonitorConhecimentoEAD;
	}

	public void setDashboardMonitorConhecimentoEAD(DashboardVO dashboardMonitorConhecimentoEAD) {
		this.dashboardMonitorConhecimentoEAD = dashboardMonitorConhecimentoEAD;
	}

	public DashboardVO getDashboardEvolucaoEstudoOnline() {
		if (dashboardEvolucaoEstudoOnline == null) {
			dashboardEvolucaoEstudoOnline = new DashboardVO(TipoDashboardEnum.EVOLUCAO_ESTUDO_ONLINE, false, 9, TipoVisaoEnum.ALUNO, getUsuarioLogadoClone());
		}
		return dashboardEvolucaoEstudoOnline;
	}

	public void setDashboardEvolucaoEstudoOnline(DashboardVO dashboardEvolucaoEstudoOnline) {
		this.dashboardEvolucaoEstudoOnline = dashboardEvolucaoEstudoOnline;
	}

	public Boolean getAvaliacaoOnlineEmRealizacao() {
		if (avaliacaoOnlineEmRealizacao == null) {
			avaliacaoOnlineEmRealizacao = false;
		}
		return avaliacaoOnlineEmRealizacao;
	}

	public void setAvaliacaoOnlineEmRealizacao(Boolean avaliacaoOnlineEmRealizacao) {
		this.avaliacaoOnlineEmRealizacao = avaliacaoOnlineEmRealizacao;
	}

	public Boolean getRealizarNavegacaoParaAvaliacaoOnline() {
		if (realizarNavegacaoParaAvaliacaoOnline == null) {
			realizarNavegacaoParaAvaliacaoOnline = false;
		}
		return realizarNavegacaoParaAvaliacaoOnline;
	}

	public void setRealizarNavegacaoParaAvaliacaoOnline(Boolean realizarNavegacaoParaAvaliacaoOnline) {
		this.realizarNavegacaoParaAvaliacaoOnline = realizarNavegacaoParaAvaliacaoOnline;
	}

	public CalendarioAgrupamentoTccVO getCalendarioAgrupamentoTcc() {
		if (calendarioAgrupamentoTcc == null) {
			calendarioAgrupamentoTcc = new CalendarioAgrupamentoTccVO();
		}
		return calendarioAgrupamentoTcc;
	}

	public void setCalendarioAgrupamentoTcc(CalendarioAgrupamentoTccVO calendarioAgrupamentoTcc) {
		this.calendarioAgrupamentoTcc = calendarioAgrupamentoTcc;
	}

	public CalendarioAgrupamentoTccVO getCalendarioAgrupamentoProjetoIntegrador() {
		if (calendarioAgrupamentoProjetoIntegrador == null) {
			calendarioAgrupamentoProjetoIntegrador = new CalendarioAgrupamentoTccVO();
		}
		return calendarioAgrupamentoProjetoIntegrador;
	}
	
	public void setCalendarioAgrupamentoProjetoIntegrador(CalendarioAgrupamentoTccVO calendarioAgrupamentoProjetoIntegrador) {
		this.calendarioAgrupamentoProjetoIntegrador = calendarioAgrupamentoProjetoIntegrador;
	}

	public CalendarioAtividadeMatriculaVO getCalendarioAvaliacaoOnlineEmRealizacao() {
		if (calendarioAvaliacaoOnlineEmRealizacao == null) {
			calendarioAvaliacaoOnlineEmRealizacao = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAvaliacaoOnlineEmRealizacao;
	}

	public void setCalendarioAvaliacaoOnlineEmRealizacao(CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnlineEmRealizacao) {
		this.calendarioAvaliacaoOnlineEmRealizacao = calendarioAvaliacaoOnlineEmRealizacao;
	}

	public String validarExistenciaAvaliacaoOnlineEmRealizacao() {
		try {
			setCalendarioAvaliacaoOnlineEmRealizacao(null);
			setAvaliacaoOnlineEmRealizacao(Boolean.FALSE);
//			setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendarioAtividadeMatriculaVisaoAluno(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado()));
			Optional<CalendarioAtividadeMatriculaVO> avaliacaoOnlineEmRealizacao = getCalendarioAtividadeMatriculaVOs().stream().filter(a -> a.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)).findAny();
			avaliacaoOnlineEmRealizacao.ifPresent(a -> {
				setCalendarioAvaliacaoOnlineEmRealizacao(a);
				setAvaliacaoOnlineEmRealizacao(Boolean.TRUE);
				setRealizarNavegacaoParaAvaliacaoOnline(Boolean.FALSE);
			});
			if (getAvaliacaoOnlineEmRealizacao()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaForm.xhtml");
			} else if (getRealizarNavegacaoParaAvaliacaoOnline()) {
				setRealizarNavegacaoParaAvaliacaoOnline(Boolean.FALSE);
				return "avaliacaoOnlineMatriculaCons.xhtml";
			} else {
				return Uteis.getCaminhoRedirecionamentoNavegacao("estudoOnlineForm.xhtml");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setModalPanelErroAcessoConteudoEstudo("PF('panelErroAcessoConteudoEstudo').show()");
			return "";
		}
	}

	private boolean alterarSenhaContaGsuite = false;
	private boolean existeConfiguracaoSeiGsuite = false;

	public void isPermiteConfiguracaoSeiGsuite() {
		try {
			setExisteConfiguracaoSeiGsuite(getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarSeExisteConfiguracaoSeiGsuitePadrao(getUsuarioLogadoClone()));
		} catch (Exception e) {
			setExisteConfiguracaoSeiGsuite(false);
		}
	}

	public void realizarInicializacaoPessoaGsuiteAlterarSenha() {
		try {
			getPessoaVO().setListaPessoaGsuite(getCalendarioDataEventoRsVO().getListaPessoaGsuiteVO());
			getPessoaVO().setListaPessoaGsuiteVO_ApresentarHtml(null);
		} catch (Exception e) {
			setExisteConfiguracaoSeiGsuite(false);
		}
	}

	public void realizarAlteracaoSenhaContaGsuite() {
//		try {
//			getFacadeFactory().getAdminSdkIntegracaoFacade().executarAlteracaoSenhaContaGsuite(getUsuarioLogado().getPessoa(), getSenhaAnterior(), getSenha(), getUsuarioLogado());
//			setMensagemID(MSG_TELA.msg_dados_gravados.name());
//		} catch (Exception e) {
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
//		}
	}

	public boolean isAlterarSenhaContaGsuite() {
		return alterarSenhaContaGsuite;
	}

	public void setAlterarSenhaContaGsuite(boolean alterarSenhaContaGsuite) {
		this.alterarSenhaContaGsuite = alterarSenhaContaGsuite;
	}

	public boolean isExisteConfiguracaoSeiGsuite() {
		return existeConfiguracaoSeiGsuite;
	}

	public void setExisteConfiguracaoSeiGsuite(boolean existeConfiguracaoSeiGsuite) {
		this.existeConfiguracaoSeiGsuite = existeConfiguracaoSeiGsuite;
	}

	public Integer getFrozenColumnsMinhasNotasAluno() {
		if (frozenColumnsMinhasNotasAluno == null) {
			frozenColumnsMinhasNotasAluno = 2;
		}
		return frozenColumnsMinhasNotasAluno;
	}

	public void setFrozenColumnsMinhasNotasAluno(Integer frozenColumnsMinhasNotasAluno) {
		this.frozenColumnsMinhasNotasAluno = frozenColumnsMinhasNotasAluno;
	}

	public Integer getTotalCargaHorariaPeriodoLetivoExigida() {
		if (totalCargaHorariaPeriodoLetivoExigida == null) {
			totalCargaHorariaPeriodoLetivoExigida = 0;
		}
		return totalCargaHorariaPeriodoLetivoExigida;
	}

	public void setTotalCargaHorariaPeriodoLetivoExigida(Integer totalCargaHorariaPeriodoLetivoExigida) {
		this.totalCargaHorariaPeriodoLetivoExigida = totalCargaHorariaPeriodoLetivoExigida;
	}

	public Integer getTotalCargaHorariaPeriodoLetivoCumprida() {
		if (totalCargaHorariaPeriodoLetivoCumprida == null) {
			totalCargaHorariaPeriodoLetivoCumprida = 0;
		}
		return totalCargaHorariaPeriodoLetivoCumprida;
	}

	public void setTotalCargaHorariaPeriodoLetivoCumprida(Integer totalCargaHorariaPeriodoLetivoCumprida) {
		this.totalCargaHorariaPeriodoLetivoCumprida = totalCargaHorariaPeriodoLetivoCumprida;
	}

	public void realizarMontagemTotalizadoresCargasHorarias() {
		try {
			HashMap<String, Integer> mapTotalCargasHorarias = new HashMap<String, Integer>(0);
			getFacadeFactory().getHistoricoFacade().realizarMontagemTotalizadoresCargasHorarias(getFilterAnoSemestre(), mapTotalCargasHorarias, getListaPrincipalHistoricos());
			setTotalCargaHorariaPeriodoLetivoExigida(mapTotalCargasHorarias.get("totalCargaHorariaPeriodoLetivoExigida"));
			setTotalCargaHorariaPeriodoLetivoCumprida(mapTotalCargasHorarias.get("totalCargaHorariaPeriodoLetivoCumprida"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void verificarDocumentoContratoAssinadoPendente() {
		try {
			setDocumentoAssinado(null);
			setDocumentoAssinadoAtaColacaoGrau(null);
			if(Uteis.isAtributoPreenchido(getUsuarioLogado()) 
					&& (getMatricula().getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor())
					|| getMatricula().getSituacao().equals(SituacaoVinculoMatricula.PREMATRICULA.getValor())
					|| getMatricula().getSituacao().equals(SituacaoVinculoMatricula.FINALIZADA.getValor())
					|| getMatricula().getSituacao().equals(SituacaoVinculoMatricula.FORMADO.getValor()))) {
				List<DocumentoAssinadoVO> listaDocumento = getFacadeFactory().getDocumentoAssinadoFacade().verificarGeracaoDocumentoAssinado(getUsuarioLogado().getPessoa().getCodigo(), getMatricula(), getMatriculaPeriodoVO(), getMatricula().getUnidadeEnsino(), getConfiguracaoGeralPadraoSistema(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				List<DocumentoAssinadoVO> listaContrato = listaDocumento.stream().filter(con -> con.getTipoOrigemDocumentoAssinadoEnum().isContrato()).collect(Collectors.toList());
				List<DocumentoAssinadoVO> listaAtaColacao = listaDocumento.stream().filter(con -> con.getTipoOrigemDocumentoAssinadoEnum().isAtaColacaoGrau()).collect(Collectors.toList());
				List<DocumentoAssinadoVO> listaTermoEstagioNaoObrigatorio = listaDocumento.stream().filter(con -> con.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioNaoObrigatorio()).collect(Collectors.toList());
				List<DocumentoAssinadoVO> listaTermoEstagioObrigatorio = listaDocumento.stream().filter(con -> con.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()).collect(Collectors.toList());
				if (Uteis.isAtributoPreenchido(listaDocumento)) {
					if (Uteis.isAtributoPreenchido(listaContrato)) {
						setDocumentoAssinado(listaContrato.get(0));
					}
					if (Uteis.isAtributoPreenchido(listaAtaColacao)) {
						setDocumentoAssinadoAtaColacaoGrau(listaAtaColacao.get(0));
					}
					if (Uteis.isAtributoPreenchido(listaTermoEstagioNaoObrigatorio)) {
						setDocumentoAssinadoTermoEstagioNaoObrigatorio(listaTermoEstagioNaoObrigatorio.get(0));
					}
					if (Uteis.isAtributoPreenchido(listaTermoEstagioObrigatorio)) {
						setDocumentoAssinadoTermoEstagioObrigatorio(listaTermoEstagioObrigatorio.get(0));
					}
					setApresentarModalPendenciaAtaColacaoGrauAssinado(Boolean.FALSE);
					setApresentarModalPendenciaContratoAssinado(Boolean.FALSE);
					setApresentarModalPendenciaTermoEstagioNaoObrigatorio(Boolean.FALSE);
					setCaminhoPreviewContrato("");
					setCaminhoPreviewDocumentoAta("");
					setCaminhoPreviewTermoEstagioNaoObrigatorio("");
					if(Uteis.isAtributoPreenchido(listaDocumento)) {
						if (Uteis.isAtributoPreenchido(getDocumentoAssinado())) {
							getDocumentoAssinado().getListaDocumentoAssinadoPessoa().stream().forEach(
									documentoAssinadoPessoa -> {
										if (documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()) && documentoAssinadoPessoa.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
											setDocumentoAssinadoPessoa(documentoAssinadoPessoa);
											if(getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorSei()) {
												setApresentarModalPendenciaContratoAssinado(Boolean.TRUE);
												setCaminhoPreviewContrato(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinado().getArquivo().getNome()+"?embedded=true");
											}
										}
									});
						} 
						if (Uteis.isAtributoPreenchido(getDocumentoAssinadoAtaColacaoGrau())) {
							getDocumentoAssinadoAtaColacaoGrau().getListaDocumentoAssinadoPessoa().stream().forEach(
									documentoAssinadoPessoaAta -> {
										if (documentoAssinadoPessoaAta.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoaAta.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()) && documentoAssinadoPessoaAta.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
											setDocumentoAssinadoPessoaAtaColacaoGrau(documentoAssinadoPessoaAta);
											if(getDocumentoAssinadoAtaColacaoGrau().getProvedorDeAssinaturaEnum().isProvedorSei()) {	
												setApresentarModalPendenciaAtaColacaoGrauAssinado(Boolean.TRUE);
												setCaminhoPreviewDocumentoAta(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + getDocumentoAssinadoAtaColacaoGrau().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinadoAtaColacaoGrau().getArquivo().getNome()+"?embedded=true");
											}
										}
									});
						}
						if (Uteis.isAtributoPreenchido(getDocumentoAssinadoTermoEstagioNaoObrigatorio())) {
							getDocumentoAssinadoTermoEstagioNaoObrigatorio().getListaDocumentoAssinadoPessoa().stream().forEach(
									documentoAssinadoPessoaTermoEstagioNaoObrigatorio -> {
										if (documentoAssinadoPessoaTermoEstagioNaoObrigatorio.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoaTermoEstagioNaoObrigatorio.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo()) && documentoAssinadoPessoaTermoEstagioNaoObrigatorio.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
											setDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio(documentoAssinadoPessoaTermoEstagioNaoObrigatorio);
											if (getDocumentoAssinadoTermoEstagioNaoObrigatorio().getProvedorDeAssinaturaEnum().isProvedorSei()) {
												setApresentarModalPendenciaTermoEstagioNaoObrigatorio(Boolean.TRUE);
												setCaminhoPreviewTermoEstagioNaoObrigatorio(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + getDocumentoAssinadoTermoEstagioNaoObrigatorio().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinadoTermoEstagioNaoObrigatorio().getArquivo().getNome() + "?embedded=true");
											}
										}
									});
						}
						if (Uteis.isAtributoPreenchido(getDocumentoAssinadoTermoEstagioObrigatorio())) {
							List<DocumentoAssinadoPessoaVO> lista =
									getDocumentoAssinadoTermoEstagioObrigatorio().getListaDocumentoAssinadoPessoa();
							for (DocumentoAssinadoPessoaVO documento : lista) {
								if (documento.getTipoPessoa().equals(TipoPessoa.ALUNO)
										&& documento.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
										&& documento.getSituacaoDocumentoAssinadoPessoaEnum()
										.equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)) {
									if (getDocumentoAssinadoTermoEstagioObrigatorio()
											.getProvedorDeAssinaturaEnum().isProvedorTechCert()
											&& Uteis.isAtributoPreenchido(documento.getUrlAssinatura())
											&& !getUsuarioLogado().getPermiteSimularNavegacaoAluno()
											) {
										setDocumentoAssinadoPessoaTermoEstagioObrigatorio(documento);
										getDocumentoAssinadoPessoaTermoEstagioObrigatorio()
												.setUrlAssinatura(documento.getUrlAssinatura());
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setApresentarModalResetarSenha(Boolean.FALSE);
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.verificarDocumentoContratoAssinadoPendente: "+e.getMessage());
		}
	}
	
	public Boolean getApresentarModalPendenciaContratoAssinado() {
		if (apresentarModalPendenciaContratoAssinado == null) {
			apresentarModalPendenciaContratoAssinado = Boolean.FALSE;
		}
		return apresentarModalPendenciaContratoAssinado;
	}
	
	public void setApresentarModalPendenciaContratoAssinado(Boolean apresentarModalPendenciaContratoAssinado) {
		this.apresentarModalPendenciaContratoAssinado = apresentarModalPendenciaContratoAssinado;
	}

	public DocumentoAssinadoVO getDocumentoAssinado() {
		if (documentoAssinado == null) {
			documentoAssinado = new DocumentoAssinadoVO();
		}
		return documentoAssinado;
	}

	public void setDocumentoAssinado(DocumentoAssinadoVO documentoAssinado) {
		this.documentoAssinado = documentoAssinado;
	}

	public String getCaminhoPreviewContrato() {
		if (caminhoPreviewContrato == null) {
			caminhoPreviewContrato = "";
		}
		return caminhoPreviewContrato;
	}

	public void setCaminhoPreviewContrato(String caminhoPreviewContrato) {
		this.caminhoPreviewContrato = caminhoPreviewContrato;
	}

	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoa() {
		if (documentoAssinadoPessoa == null) {
			documentoAssinadoPessoa = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoa;
	}

	public void setDocumentoAssinadoPessoa(DocumentoAssinadoPessoaVO documentoAssinadoPessoa) {
		this.documentoAssinadoPessoa = documentoAssinadoPessoa;
	}
	
	public Boolean getApresentarModalDocumentoAssinado() {
		if (apresentarModalDocumentoAssinado == null) {
			apresentarModalDocumentoAssinado = false;
		}
		return apresentarModalDocumentoAssinado;
	}

	public void setApresentarModalDocumentoAssinado(Boolean apresentarModalDocumentoAssinado) {
		this.apresentarModalDocumentoAssinado = apresentarModalDocumentoAssinado;
	}

	public void registrarIndeferimentoContratoPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getDocumentoAssinadoPessoa().setDataRejeicao(new Date());
			getDocumentoAssinadoPessoa().setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			if (Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoa().getMotivoRejeicao())) {
				getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicao(getDocumentoAssinadoPessoa());
			}
			setApresentarModalPendenciaContratoAssinado(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarIndeferimentoContratoPorAluno_visaoAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			} catch (Exception e1) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());	
			}
		}
	}
	
	public void registrarIndeferimentoAtaColacaoGrauPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoAlunoParaRejeitado(getDocumentoAssinadoAtaColacaoGrau(), getDocumentoAssinadoPessoaAtaColacaoGrau().getMotivoRejeicao(), getUsuarioLogado());
			setApresentarModalPendenciaAtaColacaoGrauAssinado(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarIndeferimentoContratoPorAluno_visaoAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			} catch (Exception e1) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());	
			}
		}
	}
	
	public void registrarAssinaturaContratoPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getDocumentoAssinado().getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO)).forEach(documentoAssinadoPessoaAluno-> {documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE);documentoAssinadoPessoaAluno.setLongitude(getLongitude());documentoAssinadoPessoaAluno.setLatitude(getLatitude());});
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinado().getArquivo().getNome(), getDocumentoAssinado().getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(getDocumentoAssinado(), fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setApresentarModalPendenciaContratoAssinado(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(new RegistroExecucaoJobVO("registrarAssinaturaContratoPorAluno_visaoAluno", matricula.getMatricula().concat("-").concat(e.getMessage()) ,new Date()));
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			} catch (Exception e1) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());
			}
		}
	}
	
	


	public String abrirModalContratoCertisign() {
		if(Uteis.isAtributoPreenchido(getDocumentoAssinado()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoa()) 
				&& getDocumentoAssinadoPessoa().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
			    && getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorCertisign()
			    && getDocumentoAssinadoPessoa().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
//			    && getApresentarModalContratoCertsign()
			    && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoa().getUrlAssinatura())){
				return "SignSingleDocument('"+getDocumentoAssinadoPessoa().getUrlAssinatura()+"');";
		}
		return "";
	}

	public String abrirModalContratoTechCert() {
		if(Uteis.isAtributoPreenchido(getDocumentoAssinado()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoa())
				&& getDocumentoAssinadoPessoa().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinado().getProvedorDeAssinaturaEnum().isProvedorTechCert()
				&& getDocumentoAssinadoPessoa().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoa().getUrlAssinatura())
				&& !getUsuarioLogado().getPermiteSimularNavegacaoAluno()){
			return "SignSingleDocument('"+getDocumentoAssinadoPessoa().getUrlAssinatura()+"');";
		}
		return "";
	}

	public void  validarLiberacaoModalContratoCertsign() {
		Boolean valido = true ;
		try {
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			executarValidacaoSimulacaoVisaoAluno();
			if (getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(getMatricula().getMatricula())) {			
				if(getMatricula().getCurso().getPermitirAssinarContratoPendenciaDocumentacao()){					
					valido =  !getFacadeFactory().getDocumetacaoMatriculaFacade().consultarSeExistemDocumentosPendentesAluno(getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
				}else {
					valido = false ;
				}				
			}	
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			valido = false;
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.validarLiberacaoModalContratoCertsign: "+e.getMessage());
		}
       setApresentarModalContratoCertsign(valido);
	}

	
	public void registrarAssinaturaSEI() {		
		setDocumentoAssinado(null);
		setDocumentoAssinadoPessoa(null);
	}

	public Boolean getApresentarModalContratoCertsign() {
		if(apresentarModalContratoCertsign == null) {
			apresentarModalContratoCertsign = Boolean.FALSE;
		}
		return apresentarModalContratoCertsign;
	}

	public void setApresentarModalContratoCertsign(Boolean apresentarModalContratoCertsign) {
		this.apresentarModalContratoCertsign = apresentarModalContratoCertsign;
	}
	
	public void realizarCriacaoAcessoSistemasProvasMestreGR() {
		realizarCriacaoAcessoSistemasProvasMestreGR(Boolean.TRUE);
	}
	public void realizarCriacaoAcessoSistemasProvasMestreGR(Boolean validarAcesso) {

		try {	
			setOncompleteModal("");
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			setIsSistemaProvasHabilitado(getConfiguracaoGeralSistemaVO().getHabilitarIntegracaoSistemaProvas());
			if(getIsSistemaProvasHabilitado()) {	
				String emailInstitucional = "";
				if (getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
					if(!getUsuarioLogado().getPessoa().getListaPessoaEmailInstitucionalVO().isEmpty()) {
						getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().addAll(getUsuarioLogado().getPessoa().getListaPessoaEmailInstitucionalVO());
					}else {
						getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().add(getFacadeFactory().getPessoaEmailInstitucionalFacade().
							consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogadoClone()));
					}
				}
				if (!getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
					emailInstitucional = getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0).getEmail();
				}else {
					throw new Exception("Não foi possível identificar o e-mail institucional para realizar a autenticação no sistema de provas.");
				}
				setHeaderBarIntegracaoSistemasProvaMestreGR(getConfiguracaoGeralSistemaVO().getHeaderBarIntegracaoSistemasProvaMestreGR());
				setTokenSistemasProvasMestreGR(getConfiguracaoGeralSistemaVO().getTokenIntegracaoSistemasProvaMestreGR());
				setLoginSistemasProvasMestreGR(emailInstitucional);
				setActionSistemasProvasMestreGR(getConfiguracaoGeralSistemaVO().getActionIntegracaoSistemasProvaMestreGR());
				String chave = getLoginSistemasProvasMestreGR() + "_"+ UteisData.getDataAplicandoFormatacao(new Date(), "yyyyMMdd") + "_MestreGR_KEY";
				setKeySistemasProvasMestreGR(Uteis.encriptarMD5(chave));
				setAptoAcessarSistemaProvasHabilitado(Boolean.TRUE);
			}
			if(validarAcesso) {				
				validarDadosAcessoSistemaProvasMestreGR();	
				context().getExternalContext().getSessionMap().put("actionMGR", getActionSistemasProvasMestreGR());
				context().getExternalContext().getSessionMap().put("loginMGR", getLoginSistemasProvasMestreGR());
				context().getExternalContext().getSessionMap().put("keyMGR", getKeySistemasProvasMestreGR());
				context().getExternalContext().getSessionMap().put("tokenMGR", getTokenSistemasProvasMestreGR());
				context().getExternalContext().getSessionMap().put("headerBarMGR", getHeaderBarIntegracaoSistemasProvaMestreGR());
				
//				if(resp.isSuccess()) {
				setOncompleteModal("window.open('../MestreGRSV', '_blank')");
//				}else {
//					setMensagemDetalhada("msg_erro", "Não foi possível acessar o sistema de provas, tente mais tarde.");
//				}
			}
			}
		
		} catch (UnsupportedEncodingException e) {		
			setAptoAcessarSistemaProvasHabilitado(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.realizarCriacaoAcessoSistemasProvasMestreGR: "+e.getMessage());
		} catch (Exception e) {	
			setAptoAcessarSistemaProvasHabilitado(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "ERRO NO METODO VisaoAlunoControle.realizarCriacaoAcessoSistemasProvasMestreGR: "+e.getMessage());
		}
		
		
	}
	
	public void registrarFalhaAutenticacaoMestreGR() {
		try {
			RegistroExecucaoJobVO registro = new RegistroExecucaoJobVO("MestreGR", "Falha redirecionamento: aluno: "+getUsuarioLogado().getNome()+", e-mail:"+getLoginSistemasProvasMestreGR()+", chave: "+getKeySistemasProvasMestreGR()+".");
			registro.setDataInicio(new Date());
			registro.setDataTermino(new Date());			
//			getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registro);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	public String getActionSistemasProvasMestreGR() {
		if(actionSistemasProvasMestreGR == null ) {
			actionSistemasProvasMestreGR ="";
		}
		return actionSistemasProvasMestreGR;
	}

	public void setActionSistemasProvasMestreGR(String actionSistemasProvasMestreGR) {
		this.actionSistemasProvasMestreGR = actionSistemasProvasMestreGR;
	}

	public String getLoginSistemasProvasMestreGR() {
		if(loginSistemasProvasMestreGR == null) {
			loginSistemasProvasMestreGR="";
		}
		return loginSistemasProvasMestreGR;
	}

	public void setLoginSistemasProvasMestreGR(String loginSistemasProvasMestreGR) {
		this.loginSistemasProvasMestreGR = loginSistemasProvasMestreGR;
	}

	public String getKeySistemasProvasMestreGR() {
		if(keySistemasProvasMestreGR == null ) {
			keySistemasProvasMestreGR = "";
		}
		return keySistemasProvasMestreGR;
	}

	public void setKeySistemasProvasMestreGR(String keySistemasProvasMestreGR) {
		this.keySistemasProvasMestreGR = keySistemasProvasMestreGR;
	}

	public String getTokenSistemasProvasMestreGR() {
		if(tokenSistemasProvasMestreGR == null) {
			tokenSistemasProvasMestreGR ="";
		}
		return tokenSistemasProvasMestreGR;
	}

	public void setTokenSistemasProvasMestreGR(String tokenSistemasProvasMestreGR) {
		this.tokenSistemasProvasMestreGR = tokenSistemasProvasMestreGR;
	}

	public String getHeaderBarIntegracaoSistemasProvaMestreGR() {
		if(headerBarIntegracaoSistemasProvaMestreGR == null ) {
			headerBarIntegracaoSistemasProvaMestreGR ="";
		}
		return headerBarIntegracaoSistemasProvaMestreGR;
	}

	public void setHeaderBarIntegracaoSistemasProvaMestreGR(String headerBarIntegracaoSistemasProvaMestreGR) {
		this.headerBarIntegracaoSistemasProvaMestreGR = headerBarIntegracaoSistemasProvaMestreGR;
	}
	
	

	public Boolean getIsSistemaProvasHabilitado() {
		if(isSistemaProvasHabilitado == null ) {
			isSistemaProvasHabilitado = Boolean.FALSE;
		}
		return isSistemaProvasHabilitado;
	}

	public void setIsSistemaProvasHabilitado(Boolean isSistemaProvasHabilitado) {
		this.isSistemaProvasHabilitado = isSistemaProvasHabilitado;
	}
	
	
	private  void  validarDadosAcessoSistemaProvasMestreGR() throws Exception {
		if(!Uteis.isAtributoPreenchido( getLoginSistemasProvasMestreGR())) {
			throw new Exception("Falha ao acessar a plataforma  Sistemas de Provas . usuario não possui email institucional .");
		}
		if (!Uteis.isAtributoPreenchido(getTokenSistemasProvasMestreGR())
				|| !Uteis.isAtributoPreenchido(getActionSistemasProvasMestreGR())
				|| !Uteis.isAtributoPreenchido(getHeaderBarIntegracaoSistemasProvaMestreGR())
				|| !Uteis.isAtributoPreenchido(getKeySistemasProvasMestreGR())){
		    	throw new Exception("Falha ao acessar a plataforma  Sistemas de Provas . verifique o dados  de (Integração Sei X Sistema de Provas Mestre.GR).");
		} 		
		
	}




	public Boolean getAptoAcessarSistemaProvasHabilitado() {
		if(aptoAcessarSistemaProvasHabilitado == null ) {
			aptoAcessarSistemaProvasHabilitado = Boolean.FALSE;
		}
		return aptoAcessarSistemaProvasHabilitado;
	}

	public void setAptoAcessarSistemaProvasHabilitado(Boolean aptoAcessarSistemaProvasHabilitado) {
		this.aptoAcessarSistemaProvasHabilitado = aptoAcessarSistemaProvasHabilitado;
	}
	
	public Boolean getAlunoNaoAssinouAtaColacaoGrau() {
		if (alunoNaoAssinouAtaColacaoGrau == null) {
			alunoNaoAssinouAtaColacaoGrau = false;
		}
		return alunoNaoAssinouAtaColacaoGrau;
	}
	
	public void setAlunoNaoAssinouAtaColacaoGrau(Boolean alunoNaoAssinouAtaColacaoGrau) {
		this.alunoNaoAssinouAtaColacaoGrau = alunoNaoAssinouAtaColacaoGrau;
	}
	
	public Boolean getApresentarModalPendenciaAtaColacaoGrauAssinado() {
		if (apresentarModalPendenciaAtaColacaoGrauAssinado == null) {
			apresentarModalPendenciaAtaColacaoGrauAssinado = false;
		}
		return apresentarModalPendenciaAtaColacaoGrauAssinado;
	}
	
	public void setApresentarModalPendenciaAtaColacaoGrauAssinado(Boolean apresentarModalPendenciaAtaColacaoGrauAssinado) {
		this.apresentarModalPendenciaAtaColacaoGrauAssinado = apresentarModalPendenciaAtaColacaoGrauAssinado;
	}
	
	public String getCaminhoPreviewDocumentoAta() {
		if (caminhoPreviewDocumentoAta == null) {
			caminhoPreviewDocumentoAta = "";
		}
		return caminhoPreviewDocumentoAta;
	}
	
	public void setCaminhoPreviewDocumentoAta(String caminhoPreviewDocumentoAta) {
		this.caminhoPreviewDocumentoAta = caminhoPreviewDocumentoAta;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoAtaColacaoGrau() {
		if (documentoAssinadoAtaColacaoGrau == null) {
			documentoAssinadoAtaColacaoGrau = new DocumentoAssinadoVO();
		}
		return documentoAssinadoAtaColacaoGrau;
	}
	
	public void setDocumentoAssinadoAtaColacaoGrau(DocumentoAssinadoVO documentoAssinadoAtaColacaoGrau) {
		this.documentoAssinadoAtaColacaoGrau = documentoAssinadoAtaColacaoGrau;
	}
	
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaAtaColacaoGrau() {
		if (documentoAssinadoPessoaAtaColacaoGrau == null) {
			documentoAssinadoPessoaAtaColacaoGrau = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaAtaColacaoGrau;
	}
	
	public void setDocumentoAssinadoPessoaAtaColacaoGrau(DocumentoAssinadoPessoaVO documentoAssinadoPessoaAtaColacaoGrau) {
		this.documentoAssinadoPessoaAtaColacaoGrau = documentoAssinadoPessoaAtaColacaoGrau;
	}
	
	public void registrarAssinaturaAtaColacaoGrauPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getDocumentoAssinadoAtaColacaoGrau().getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())).forEach(documentoAssinadoPessoaAluno-> {documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE);documentoAssinadoPessoaAluno.setLongitude(getLongitude());documentoAssinadoPessoaAluno.setLatitude(getLatitude());});
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getDocumentoAssinadoAtaColacaoGrau().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinadoAtaColacaoGrau().getArquivo().getNome(), getDocumentoAssinadoAtaColacaoGrau().getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(getDocumentoAssinadoAtaColacaoGrau(), fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			registrarAlunoColouGrau();
			setApresentarModalPendenciaAtaColacaoGrauAssinado(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String abrirModalAtaColacaoGrauCertisign() {
		if(Uteis.isAtributoPreenchido(getDocumentoAssinadoAtaColacaoGrau()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaAtaColacaoGrau()) 
				&& getDocumentoAssinadoPessoaAtaColacaoGrau().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinadoAtaColacaoGrau().getProvedorDeAssinaturaEnum().isProvedorCertisign()
				&& getDocumentoAssinadoPessoaAtaColacaoGrau().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaAtaColacaoGrau().getUrlAssinatura())){
			return "SignSingleDocument('"+getDocumentoAssinadoPessoaAtaColacaoGrau().getUrlAssinatura()+"');";
		}
		return "";
	}

	public String abrirModalAtaColacaoGrauTechCert() {
		if(Uteis.isAtributoPreenchido(getDocumentoAssinadoAtaColacaoGrau()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaAtaColacaoGrau())
				&& getDocumentoAssinadoPessoaAtaColacaoGrau().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinadoAtaColacaoGrau().getProvedorDeAssinaturaEnum().isProvedorTechCert()
				&& getDocumentoAssinadoPessoaAtaColacaoGrau().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& !getUsuarioLogado().getPermiteSimularNavegacaoAluno()
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaAtaColacaoGrau().getUrlAssinatura())){
			return "SignSingleDocument('"+getDocumentoAssinadoPessoaAtaColacaoGrau().getUrlAssinatura()+"');";
		}
		return "";
	}
	
	public void registrarAlunoColouGrau() {
		try {
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarAlteracaoColouGrau(getDocumentoAssinadoAtaColacaoGrau(), getUsuarioLogado().getPessoa().getCodigo(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String montarDadosMatricula() {
		List<MatriculaVO> listaMatricula = null;
		Iterator<MatriculaVO> i = null;
		try {
			listaMatricula = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaNaoCancelada(getMatricula().getAluno().getCodigo(), false,!getUsuarioLogado().getIsApresentarVisaoPais(), true, true,getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			List<SelectItem> lista = new ArrayList<SelectItem>(0);
			boolean primeiro = Boolean.FALSE;
			i = listaMatricula.iterator();
			while (i.hasNext()) {
				MatriculaVO matriculas = (MatriculaVO) i.next();
				if (!primeiro) {
					setMatricula(matriculas);
					getUsuarioLogado().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
					getUsuarioLogado().setUnidadeEnsinoLogado((UnidadeEnsinoVO) getMatricula().getUnidadeEnsino().clone());
					getLoginControle().inicializarLogoApartirUsuario(getUsuarioLogadoClone());
					primeiro = Boolean.TRUE;
				}
				TurmaVO turmaVO = new TurmaVO();
				turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(matriculas.getMatricula(), getUsuarioLogado());
				lista.add(new SelectItem(matriculas.getMatricula(), matriculas.getMatricula() + " - " + matriculas.getCurso().getNome() + " - "+ turmaVO.getIdentificadorTurma() + " - " + matriculas.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo_Apresentar()));
			}
			setListaSelectItemMatriculaCursoTurnoTopoAluno(lista);
			selecionarCursoTopo();
		} catch (Exception e) {
			setListaSelectItemMatriculaCursoTurnoTopoAluno(new ArrayList<SelectItem>(0));
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaMatricula);
			i = null;
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoAluno.xhtml");
	}
	
	private List<SelectItem> listaSelectItemAluno;
	
	public List<SelectItem> getListaSelectItemAluno() {
		if (listaSelectItemAluno == null) {
			listaSelectItemAluno = new ArrayList<SelectItem>();
		}
		return listaSelectItemAluno;
	}
	
	public void setListaSelectItemAluno(List<SelectItem> listaSelectItemAluno) {
		this.listaSelectItemAluno = listaSelectItemAluno;
	}

	
	public void verificarGeracaoDocumentoContratoMatricula(){	
		try{
			if(Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				DocumentoAssinadoPessoaVO documentoAssinadoPessoa = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(getMatricula().getMatricula() , getMatriculaPeriodoVO().getCodigo());
		 	   if(((!Uteis.isAtributoPreenchido(documentoAssinadoPessoa)) && getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaDeCalouro(getMatricula().getMatricula(), getMatriculaPeriodoVO().getCodigo(), false, getUsuarioLogado())) 
		 			            && getFacadeFactory().getTextoPadraoFacade().verificarTextoPadraoAssinaDigitalmentePorMatricula(getMatricula().getMatricula(), false , 0 , getUsuarioLogado())						
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(getMatricula().getUnidadeEnsino().getCodigo(), getUsuarioLogado()).getConfiguracaoGedContratoVO().getAssinarDocumento()
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(getMatricula().getUnidadeEnsino().getCodigo(), getUsuarioLogado()).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()
								&& ((!getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(getMatricula().getMatricula()))  || 
						 				  (getMatricula().getCurso().getPermitirAssinarContratoPendenciaDocumentacao() && !getFacadeFactory().getDocumetacaoMatriculaFacade().consultarSeExistemDocumentosPendentesAluno(getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado())))) {		  				
		 		   getMatriculaPeriodoVO().setContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));     
//		 		   getFacadeFactory().getImpressaoContratoFacade().imprimirContratoVisaoAluno(getMatricula(),getMatriculaPeriodoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatricula().getUnidadeEnsino().getCodigo()), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		 	   }
		 	 }
		} catch (Exception e) {			
			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.SCRIPT, "ERRO NO METODO VisaoAlunoControle.verificarGeracaoDocumentoContratoMatricula: "+e.getMessage());
			e.printStackTrace();
		}
 		 	
	}
	
	public Boolean getPermitirUploadAnexo() {
		if (permitirUploadAnexo == null) {
			try {
				permitirUploadAnexo = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("EntregaDocumentoPermiteAnexarArquivo", getUsuarioLogado());
			} catch (Exception e) {
				permitirUploadAnexo = false;
			}
		}
		return permitirUploadAnexo;
	}
	
	public void setPermitirUploadAnexo(Boolean permitirUploadAnexo) {
		this.permitirUploadAnexo = permitirUploadAnexo;
	}
	
	public void removerDocumentacaoMatriculaFrente() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			validarBloqueioEntregaDocumentoForaPrazoConformePeridoChamadaProcessoSeletivoParaMatriculasVinculadasAInscricao(obj);
			obj.setExcluirArquivo(true);
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
			getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(obj,getConfiguracaoGeralPadraoSistema(), !getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() , getUsuarioLogado());
			obj.setEntregue(false);
			obj.setDataEntrega(null);
			obj.setUsuario(null);			
			obj.setArquivoVO(new ArquivoVO());
			obj.getArquivoVO().setDescricao("");			
			obj.setArquivoVOVerso(new ArquivoVO());
			obj.getArquivoVOVerso().setDescricao("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");			
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerDocumentacaoMatriculaVerso() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			if (!obj.getDocumentoEntregue()) {
				obj.setArquivoVOVerso(new ArquivoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarDocumentacaoMatricula() throws CloneNotSupportedException {
		setTam(100);
		setDocumetacaoMatriculaVO((DocumetacaoMatriculaVO) getDocumetacaoMatriculaVO().clone());
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
		if (getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome().equals("")) {
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao(getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getNome() + "_VERSO");
		}
	}
	
	public void selecionarDocumentacaoMatriculaVerso() throws CloneNotSupportedException {
		setTam(100);
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		setDocumetacaoMatriculaVO((DocumetacaoMatriculaVO) obj.clone());
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
		if (obj.getArquivoVOVerso().getNome().equals("")) {
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao(obj.getTipoDeDocumentoVO().getNome() + "_VERSO");
		}
	}
	
	public void adicionarFiliacao() {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				filiacaoEdicaoVO.setAluno(getPessoaVO().getCodigo());
			}
			validarFiliacaoAluno();
			if (getFiliacaoEdicaoVO().getCep().equals("")) {
				getFiliacaoEdicaoVO().setCep(getPessoaVO().getCEP());
			}
			if (getFiliacaoEdicaoVO().getEndereco().equals("")) {
				getFiliacaoEdicaoVO().setEndereco(getPessoaVO().getEndereco());
			}
			if (getFiliacaoEdicaoVO().getSetor().equals("")) {
				getFiliacaoEdicaoVO().setSetor(getPessoaVO().getSetor());
			}
			if (getFiliacaoEdicaoVO().getPais().getComplemento().equals("")) {
				getFiliacaoEdicaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
			}
			if (getFiliacaoEdicaoVO().getPais().getNumero().equals("")) {
				getFiliacaoEdicaoVO().getPais().setNumero(getPessoaVO().getNumero());
			}			
			if (getFiliacaoEdicaoVO().getCidade().getCodigo().intValue() == 0) {
				getFiliacaoEdicaoVO().setCidade(getPessoaVO().getCidade());
			}
			if (getFiliacaoEdicaoVO().getResponsavelFinanceiro()) {
				if (!Uteis.isAtributoPreenchido(getFiliacaoEdicaoVO().getCPF())) {
					throw new ConsistirException("O campo CPF do responsável financeiro deve ser informado!");
				}
				if (!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getFiliacaoEdicaoVO().getCPF())).replaceAll(" ", ""))) {
					throw new ConsistirException("O campo CPF do responsável financeiro é inválido (" + getFiliacaoEdicaoVO().getCPF()+ ")!");
				}
			}			
			getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoEdicaoVO());
			this.setFiliacaoVO(new FiliacaoVO());
			setFiliacaoEdicaoVO(new FiliacaoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarFiliacaoAluno() throws ConsistirException {
		Iterator<FiliacaoVO> i = null;
		i = getPessoaVO().getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO filiacaoVO = (FiliacaoVO) i.next();
			if (filiacaoVO.getPais().getCPF().equals(getPessoaVO().getCPF())) {
				filiacaoVO.getPais().setCPF(null);
				throw new ConsistirException("O campo CPF do responsável (" + filiacaoVO.getPais().getNome() + ") deve ser diferente do aluno.");
			}
		}
	}
	
	public void removerFiliacao()  {
		try {
			FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
			getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public MatriculaIntegralizacaoCurricularVO getMatriculaIntegralizacaoCurricularVO() {
		if(matriculaIntegralizacaoCurricularVO == null) {
			matriculaIntegralizacaoCurricularVO = new MatriculaIntegralizacaoCurricularVO();
		}
		return matriculaIntegralizacaoCurricularVO;
	}
	
	public void setMatriculaIntegralizacaoCurricularVO(MatriculaIntegralizacaoCurricularVO matriculaIntegralizacaoCurricularVO) {
		this.matriculaIntegralizacaoCurricularVO = matriculaIntegralizacaoCurricularVO;
	}
	
	public void carregarPercentuaisIntegralizacaoCurricularMatricula() {
		if(getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().containsKey(getMatricula().getMatricula()) && getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().get(getMatricula().getMatricula()) != null) {
			setMatriculaIntegralizacaoCurricularVO(getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().get(getMatricula().getMatricula()));
		}else {
			setMatriculaIntegralizacaoCurricularVO(getFacadeFactory().getMatriculaFacade().consultarPercentuaisIntegralizacaoCurricularMatricula(getMatricula().getMatricula()));
		}
	}
	
	public Boolean getPeriodoValidoApresentarModalPendenciaAtaColacaoGrauAssinado() {
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoAtaColacaoGrau().getProgramacaoFormaturaVO())) {
			return getFacadeFactory().getProgramacaoFormaturaFacade().validarDataLimitePodeAssinarAta(getDocumentoAssinadoAtaColacaoGrau().getProgramacaoFormaturaVO());
		}
		return true;
	}
	
	public List<SelectItem> listaSelectItemTranstornosNeuroDivergentes;
	
	public List<SelectItem> getListaSelectItemTranstornosNeurodivergentes() throws Exception {
		if (listaSelectItemTranstornosNeuroDivergentes == null) {
			listaSelectItemTranstornosNeuroDivergentes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTranstornosNeurodivergentes.class);
		}
		return listaSelectItemTranstornosNeuroDivergentes;
	}
	
	public Boolean getApresentarModalPendenciaTermoEstagioNaoObrigatorio() {
		if (apresentarModalPendenciaTermoEstagioNaoObrigatorio == null) {
			apresentarModalPendenciaTermoEstagioNaoObrigatorio = false;
		}
		return apresentarModalPendenciaTermoEstagioNaoObrigatorio;
	}
	
	public void setApresentarModalPendenciaTermoEstagioNaoObrigatorio(Boolean apresentarModalPendenciaTermoEstagioNaoObrigatorio) {
		this.apresentarModalPendenciaTermoEstagioNaoObrigatorio = apresentarModalPendenciaTermoEstagioNaoObrigatorio;
	}

	public Boolean getApresentarModalPendenciaTermoEstagioObrigatorio() {
		if (apresentarModalPendenciaTermoEstagioObrigatorio == null) {
			apresentarModalPendenciaTermoEstagioObrigatorio = false;
		}
		return apresentarModalPendenciaTermoEstagioObrigatorio;
	}

	public void setApresentarModalPendenciaTermoEstagioObrigatorio(Boolean apresentarModalPendenciaTermoEstagioObrigatorio) {
		this.apresentarModalPendenciaTermoEstagioObrigatorio = apresentarModalPendenciaTermoEstagioObrigatorio;
	}
	
	public String getCaminhoPreviewTermoEstagioNaoObrigatorio() {
		if (caminhoPreviewTermoEstagioNaoObrigatorio == null) {
			caminhoPreviewTermoEstagioNaoObrigatorio = "";
		}
		return caminhoPreviewTermoEstagioNaoObrigatorio;
	}
	
	public void setCaminhoPreviewTermoEstagioNaoObrigatorio(String caminhoPreviewTermoEstagioNaoObrigatorio) {
		this.caminhoPreviewTermoEstagioNaoObrigatorio = caminhoPreviewTermoEstagioNaoObrigatorio;
	}

	public String getCaminhoPreviewTermoEstagioObrigatorio() {
		if (caminhoPreviewTermoEstagioNaoObrigatorio == null) {
			caminhoPreviewTermoEstagioNaoObrigatorio = "";
		}
		return caminhoPreviewTermoEstagioNaoObrigatorio;
	}

	public void setCaminhoPreviewTermoEstagioObrigatorio(String caminhoPreviewTermoEstagioNaoObrigatorio) {
		this.caminhoPreviewTermoEstagioNaoObrigatorio = caminhoPreviewTermoEstagioNaoObrigatorio;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoTermoEstagioNaoObrigatorio() {
		if (documentoAssinadoTermoEstagioNaoObrigatorio == null) {
			documentoAssinadoTermoEstagioNaoObrigatorio = new DocumentoAssinadoVO();
		}
		return documentoAssinadoTermoEstagioNaoObrigatorio;
	}
	
	public void setDocumentoAssinadoTermoEstagioNaoObrigatorio(DocumentoAssinadoVO documentoAssinadoTermoEstagioNaoObrigatorio) {
		this.documentoAssinadoTermoEstagioNaoObrigatorio = documentoAssinadoTermoEstagioNaoObrigatorio;
	}
	
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio() {
		if (documentoAssinadoPessoaTermoEstagioNaoObrigatorio == null) {
			documentoAssinadoPessoaTermoEstagioNaoObrigatorio = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaTermoEstagioNaoObrigatorio;
	}
	
	public void setDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio(DocumentoAssinadoPessoaVO documentoAssinadoPessoaTermoEstagioNaoObrigatorio) {
		this.documentoAssinadoPessoaTermoEstagioNaoObrigatorio = documentoAssinadoPessoaTermoEstagioNaoObrigatorio;
	}

	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaTermoEstagioObrigatorio() {
		if (documentoAssinadoPessoaTermoEstagioObrigatorio == null) {
			documentoAssinadoPessoaTermoEstagioObrigatorio = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaTermoEstagioObrigatorio;
	}

	public void setDocumentoAssinadoPessoaTermoEstagioObrigatorio(DocumentoAssinadoPessoaVO documentoAssinadoPessoaTermoEstagioObrigatorio) {
		this.documentoAssinadoPessoaTermoEstagioObrigatorio = documentoAssinadoPessoaTermoEstagioObrigatorio;
	}
	
	public String abrirModalTermoEstagioNaoContratoCertisign() {
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoTermoEstagioNaoObrigatorio()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio())
				&& getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinadoTermoEstagioNaoObrigatorio().getProvedorDeAssinaturaEnum().isProvedorCertisign()
				&& getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getUrlAssinatura())){
				return "SignSingleDocument('"+getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getUrlAssinatura()+"');";
		}
		return "";
	}

	public String abrirModalTermoEstagioNaoContratoTechCert() {
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoTermoEstagioNaoObrigatorio()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio())
				&& getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinadoTermoEstagioNaoObrigatorio().getProvedorDeAssinaturaEnum().isProvedorTechCert()
				&& !getUsuarioLogado().getPermiteSimularNavegacaoAluno()
				&& getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getUrlAssinatura())){
			return "SignSingleDocument('"+getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getUrlAssinatura()+"');";
		}
		return "";
	}

	public String abrirModalTermoEstagioContratoTechCert() {
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoTermoEstagioObrigatorio()) && Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioObrigatorio())
				&& getDocumentoAssinadoPessoaTermoEstagioObrigatorio().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
				&& getDocumentoAssinadoTermoEstagioObrigatorio().getProvedorDeAssinaturaEnum().isProvedorTechCert()
				&& getDocumentoAssinadoPessoaTermoEstagioObrigatorio().getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())
				&& Uteis.isAtributoPreenchido(getDocumentoAssinadoPessoaTermoEstagioObrigatorio().getUrlAssinatura())){
			return "SignSingleDocument('"+getDocumentoAssinadoPessoaTermoEstagioObrigatorio().getUrlAssinatura()+"');";
		}
		return "";
	}
	
	public void registrarIndeferimentoTermoEstagioNaoObrigatorioPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoAlunoParaRejeitado(getDocumentoAssinadoAtaColacaoGrau(), getDocumentoAssinadoPessoaTermoEstagioNaoObrigatorio().getMotivoRejeicao(), getUsuarioLogado());
			setApresentarModalPendenciaTermoEstagioNaoObrigatorio(Boolean.FALSE);
			setMensagem("msg_dados_gravados");
		} catch (Exception e) {
			try {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			} catch (Exception e1) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e1.getMessage());
			}
		}
	}

	/**
	 * Método responsavel por criar painel com total de integralização
	 * mostrando o valor pendente e o integralizado. Painel mostra tambem o tempo
	 * maximo para integralizar com o risco de jubilamento variando entre as cores no texto(verde, amarelo e vermelho).
	 *
	 * @Autor Douglas
	 */
	public void registrarAssinaturaTermoEstagioNaoObrigatorioPorAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			getDocumentoAssinadoTermoEstagioNaoObrigatorio().getListaDocumentoAssinadoPessoa().stream().filter(documentoAssinadoPessoa -> documentoAssinadoPessoa.getTipoPessoa().equals(TipoPessoa.ALUNO) && documentoAssinadoPessoa.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())).forEach(documentoAssinadoPessoaAluno -> {documentoAssinadoPessoaAluno.setAssinarDocumento(Boolean.TRUE); documentoAssinadoPessoaAluno.setLongitude(getLongitude()); documentoAssinadoPessoaAluno.setLatitude(getLatitude());});
			String arquivoTemp = getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getLocalUploadArquivoFixo() + File.separator + getDocumentoAssinadoTermoEstagioNaoObrigatorio().getArquivo().getPastaBaseArquivo() + File.separator + getDocumentoAssinadoTermoEstagioNaoObrigatorio().getArquivo().getNome(), getDocumentoAssinadoTermoEstagioNaoObrigatorio().getArquivo().getNome());
			File fileAssinar = new File(arquivoTemp);
			getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarContrato(getDocumentoAssinadoTermoEstagioNaoObrigatorio(), fileAssinar, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			//registrarAlunoColouGrau();
			setApresentarModalPendenciaTermoEstagioNaoObrigatorio(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void gerarPrazoJubilamentoMatricula(GradeCurricularVO gradeCurricularVO) throws Exception {
		if (Uteis.isAtributoPreenchido(getMatricula().getAnoIngresso()) && Uteis.isAtributoPreenchido(getMatricula().getSemestreIngresso())) {
			try {
			int anoInicial = Integer.parseInt(getMatricula().getAnoIngresso());
			int semestreInicial = Integer.parseInt(getMatricula().getSemestreIngresso());
			
			int periodoLetivoTotal = gradeCurricularVO.getPeriodoLetivosVOs().stream()
					.mapToInt(PeriodoLetivoVO::getPeriodoLetivo)
					.max()
					.orElseThrow(() -> new IllegalStateException("Lista de períodos vazia"));
			setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(getMatricula().getMatricula(), false, getUsuarioLogado()));
			setPeriodoLetivoVO(gradeCurricularVO.getUltimoPeriodoLetivoGrade());
			matricula.getGradeCurricularAtual().setPrazojubilamento(getFacadeFactory().getGradeCurricularFacade().consultarPrazoJubilamento(getMatricula().getMatricula()));
			String anoSemestreJubilamento = getMatricula().getGradeCurricularAtual().getPrazojubilamento();
			if(anoSemestreJubilamento == null || anoSemestreJubilamento.trim().isEmpty()) {
				setCorDoTextoJubilamento(TEXTO_VERDE);
				return;
			}
			String anoSemestreSomaPeriodosInicioIngresso = calcularSomaSemestres(anoInicial,semestreInicial, periodoLetivoTotal);
			Optional<MatriculaPeriodoVO> UltimoMatriculaPeriodo = getMatriculaPeriodoVOs().stream().max(Comparator.comparing(m -> m.getData()));
			if (matricula.getGradeCurricularAtual().getPrazojubilamento().isEmpty()) {
				apresentarDashboardIntegralizacao = false;
			}
			int periodoLetivoFinal = getPeriodoLetivoVO().getPeriodoLetivo();
			int periodoLetivoAtual = getMatriculaPeriodoVOs().stream()
					.max(Comparator.comparing(MatriculaPeriodoVO::getData))
					.map(m -> m.getPeriodoLetivo().getPeriodoLetivo())
					.orElse(0);
			long totalMatriculas = getMatriculaPeriodoVOs().size();
			String cor = definirCorJubilamento(
					UltimoMatriculaPeriodo.get().getAno()+ "/" + UltimoMatriculaPeriodo.get().getSemestre(),
					anoSemestreSomaPeriodosInicioIngresso,
					anoSemestreJubilamento,
					periodoLetivoFinal,
					max(periodoLetivoAtual,totalMatriculas)
					);
			setCorDoTextoJubilamento(cor);
			apresentarDashboardIntegralizacao = true;
			}catch (Exception e) {
				apresentarDashboardIntegralizacao = true;
				setCorDoTextoJubilamento(TEXTO_VERDE);
			}
		}
	}

	public static String definirCorJubilamento(String periodoAtual,
											   String periodoLimiteRegular,
											   String periodoLimiteJubilamento,
											   int periodoLetivoFinal,
											   long periodoLetivoAtual) {

		if (periodoLetivoAtual <= periodoLetivoFinal) {
			return TEXTO_VERDE;
		}

		if (compararPeriodos(periodoAtual, periodoLimiteJubilamento) >= 0) {
			return TEXTO_VERMELHO;
		}

		if (compararPeriodos(periodoAtual, periodoLimiteRegular) > 0) {
			return TEXTO_AMARELO;
		}

		return TEXTO_VERMELHO;
	}

	private static int compararPeriodos(String periodo1, String periodo2) {
		String[] parts1 = periodo1.split("/");
		String[] parts2 = periodo2.split("/");

		int total1 = Integer.parseInt(parts1[0]) * 2 + Integer.parseInt(parts1[1]);
		int total2 = Integer.parseInt(parts2[0]) * 2 + Integer.parseInt(parts2[1]);

		return Integer.compare(total1, total2);
	}

	public String getCorDoTextoJubilamento() {
		if(corDoTextoJubilamento == null){
			corDoTextoJubilamento = "texto-green";
		}
		return corDoTextoJubilamento;
	}

	public void setCorDoTextoJubilamento(String corDoTextoJubilamento) {
		this.corDoTextoJubilamento = corDoTextoJubilamento;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if(periodoLetivoVO == null){
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if(matriculaPeriodoVOs == null){
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public boolean isApresentarDashboardIntegralizacao() {
		return apresentarDashboardIntegralizacao;
	}

	public void setApresentarDashboardIntegralizacao(boolean apresentarDashboardIntegralizacao) {
		this.apresentarDashboardIntegralizacao = apresentarDashboardIntegralizacao;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoTermoEstagioObrigatorio() {
		if (documentoAssinadoTermoEstagioObrigatorio == null) {
			documentoAssinadoTermoEstagioObrigatorio = new DocumentoAssinadoVO();
		}
		return documentoAssinadoTermoEstagioObrigatorio;
	}

	public void setDocumentoAssinadoTermoEstagioObrigatorio(DocumentoAssinadoVO documentoAssinadoTermoEstagioObrigatorio) {
		this.documentoAssinadoTermoEstagioObrigatorio = documentoAssinadoTermoEstagioObrigatorio;
	}
	
	private void removerMemoriaAplicacao() {
		try {
	getAplicacaoControle().getMatriculasAlunoCache().remove(getUsuarioLogado().getCodigo());
	getAplicacaoControle().getMatriculaAlunoCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getMatriculaPeriodoAlunoCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getCalendarioProjetoIntegradorCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getAnoSemestreMatriculaCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getDashboardEstagioFacilitadorCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getDashboardEstagioAlunoCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getMatriculaEstagioDeferidoCache().remove(getMatricula().getMatricula());
	getAplicacaoControle().getPreferenciaSistemaUsuarioCache().remove(getUsuarioLogado().getCodigo());
	getAplicacaoControle().getLinksUteisUsuarioCache().remove(getUsuarioLogado().getCodigo());
	getAplicacaoControle().getDashboardsAlunoCache().remove(getUsuarioLogado().getCodigo());
	for(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO: getPessoaVO().getListaPessoaEmailInstitucionalVO().stream().filter(e -> e.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)).collect(Collectors.toList())) {
		getAplicacaoControle().getUsuarioAlunoPorEmailCache().remove(pessoaEmailInstitucionalVO.getEmail());				
	}
	getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().remove((getUsuarioLogado().getUsername()+getUsuarioLogado().getSenha()));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
