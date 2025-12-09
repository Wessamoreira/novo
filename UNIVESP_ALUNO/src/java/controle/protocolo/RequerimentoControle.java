package controle.protocolo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import controle.arquitetura.AssuntoDebugEnum;
import org.primefaces.event.FileUploadEvent;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.AlunoControle;
import controle.academico.VisaoAlunoControle;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.TipoTrabalhoConclusaoCurso;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;

import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.protocolo.EstatisticaRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.SituacaoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoSituacaoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoVisaoAcesso;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.protocolo.Requerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.CertificadoCursoExtensaoRelControle;
import relatorio.controle.academico.HistoricoAlunoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CertificadoCursoExtensaoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.RequerimentoRel;

@Controller("RequerimentoControle")
@Scope("viewScope")
@Lazy
public class RequerimentoControle extends HistoricoAlunoRelControle implements Serializable {
	
	private static final long serialVersionUID = 2798236329599692630L;

	private RequerimentoVO requerimentoVO;
	private RequerimentoVO requerimentoConsVO;
	private EstatisticaRequerimentoVO estatisticaRequerimentoVO;
	private String tipoRequerimento_Erro;
	private String matricula_valorApresentar;
	private String matricula_Erro;
	private String departamentoResponsavel_Erro;
	private List<SelectItem> listaSelectItemMatricula;
	private List<SelectItem> listaSelectItemTipoRequerimento;
	private List<SelectItem> listaSelectItemTipoRequerimentoConsulta;
	private List<SelectItem> listaSelectItemCentroReceita;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemUnidadeEnsinoTransferencia;
	private List<SelectItem> listaSelectItemContaCorrente;
	protected List listaConsultaCentroReceita;
	protected String valorConsultaCentroReceita;
	protected String campoConsultaCentroReceita;
	protected List listaConsultaAluno;
	protected String valorConsultaAluno;
	protected String campoConsultaAluno;
	protected List listaConsultaRequisitante;
	protected String valorConsultaRequisitante;
	protected String campoConsultaRequisitante;
	protected TipoRequerimentoVO tipoRequerimentoVO;
	private RequerimentoHistoricoVO requerimentoHistoricoVO;
	private RequerimentoDisciplinasAproveitadasVO requerimentoDisciplinasAproveitadasVO;
	private List<RequerimentoDisciplinasAproveitadasVO> listaRequerimentoDisciplinasAproveitadasVO;
	private List<SelectItem> listaSelectItemDisciplinaAproveitamento;
	private Boolean imprimir;
	private Boolean finalizarEtapaRequerimento;
	private List<SelectItem> listaSelectItemDescontoRequerimento;
	private Boolean novoRegistro;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List listaConsultaCidade;
	private String abrirModalUpload;
	private String popUpAbrir;
	private String abrirModalCapturarImagemWebCam;
	private String gravadoComSucesso;
	private String msgRequerimentoImagemGravadoComSucesso;
	private String msgRequerimentoPossuiValor;
	private String situacao;
	private String situacaoFinanceira;
	private List listaConsultaAbertos;
	private List listaConsultaFinalizados;		
	private Boolean dentroPrazo;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	protected TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemTurma;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List listaConsultaCurso;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List listaConsultaTurma;
	
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private String ordernarPor;
	private List listaConsultaDisciplina;
	private Date dataInicio;
	private Date dataFim;
	private List listaRequerimentoReposicao;
	private List listaDocumentosPendentes;
	private List<SelectItem> listaSelectItemDepartamento;
	private String campoConsultaFuncionarioResponsavel;
	private String valorConsultaFuncionarioResponsavel;
	private List listaConsultaFuncionarioResponsavel;
	private Boolean gerarListaRequerimentoTodoPeriodo;
	private List<SelectItem> listaSelectItemSituacaoRequerimento;
	private Boolean permitirConsultarTodasUnidades;
	private Boolean permitirConsultarRequerimentoOutroConsultorResponsavel;
	private Boolean permitirConsultarRequerimentoOutroDepartametoResponsavel;
	private Boolean permitirConsultarRequerimentoTodasUnidades;
	private Boolean permiteVisualizarRequerinentoOutroDepartamentoTramite;

	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;

	private Boolean abrirModalQuestionario;
	private Boolean abrirModalQuestionarioRequerimentoHistorico;
	private Boolean abrirOpcaoInformarFuncionarioProximoTramite;
	private FuncionarioVO funcionarioProximoTramite;
	private String tipoLayout;
	private String sigla;

	private Boolean apresentarBotaoAproveitamento;
	private Boolean apresentarBotaoMatricula;
	private Boolean apresentarBotaoTransferenciaExt;
	private MaterialRequerimentoVO materialRequerimento;
	private String motivoRetornoDepartamento;
	private List<SelectItem> tipoRequerimentoDepartamentoRetornarVOs;
	private Integer tipoRequerimentoDepartamentoAnterior;
	private String acaoModalDepartamentoAnterior;
	private Boolean ordemCrescente;

	private Boolean imprimirVisaoAluno;
	private Boolean apresentarCampoDisciplina;
	private Boolean existeTipoRequerimentoCadastradoProfessorCoordenador;
	private List<SelectItem> listaSelectItemTipoPessoa;
	private List<SelectItem> listaSelectItemDisciplina;
	private String autocompleteValorCurso;
	private String autocompleteValorTurma;
	private String autocompleteValorRequisitante;
	private String autocompleteValorResponsavel;
	private String autocompleteValorDisciplina;

	private String professorMinistrouAula;
	private String usuarioLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private String tipoFuncionalidadeValidar;
	private Double valorAcrescimoDescontoTemp;
	private String tipoDescontoTemp;
	private boolean permitirInformarDesconto = false;
	private boolean permitirInformarAcrescimo = false;
	private List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs;
	public Boolean disciplinaVazia;
	public Boolean permitirApresentarBotaoEnviarDepartamentoAnterior;
	private Boolean funcionarioTramiteCoordenadorEspecifico;
	private List<SelectItem> listaSelectItemCoordenadorEspecifico;
	public Boolean permiteDeferir;
	public Boolean permiteConsultar;
	public Boolean permiteIndeferir;
	public Boolean permiteImprimirComprovante;
	public Boolean permiteImprimirRequerimento;
	public Boolean permiteExcluir;
	public Boolean permiteNovo;
	public Boolean permiteGravar;
	public Boolean permiteIncluirDisciplina;
	public Boolean permiteIniciarRequerimento;
	public ImpressaoContratoVO impressaoContratoFiltro;
	
	private String valorTurma;
	private String valorRequisitante;
	private String valorResponsavel;	
	private List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamento;
	private List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons;
	private Integer situacaoRequerimentoDepartamento;
	private Boolean permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa;
	private Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios;
	private Boolean permitirAlterarObservacaoIncluidaPeloRequerente;
	private Boolean checkDisponibilizarTodosParaRequerente;
	private Boolean permitirRequerenteAnexarArquivo;
	private Boolean permitirRequerenteVisualizarTramite;
	private InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistorico;
	private InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoFilho;
	private List<CertificadoCursoExtensaoRelVO> listaCertificadoErro;
    private List<File> listaArquivos = new ArrayList<File>(0);
    private Boolean impressaoContratoExistente;
    private String caminhoPreviewCertificado;
    private Boolean certificadoDigital;
    private Boolean certificadoImpresso;
    List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs;
    TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO; 
    private Boolean gerarNovoArquivoAssinado;
    private Boolean podeInserirNota;
    public List<SelectItem> listaSelectItemTipoTrabalhoConclusaoCurso;
    private String msgNovoRequerimento;
    private RequerimentoVO requerimentoAntigo;

    private Boolean permiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade;

    private Boolean origemExterna;
    private String msgRequerimentoExistente;
    private Boolean requerimentoValidado;
	
	private TipoCartaoOperadoraCartaoEnum tipoCartao;
	private Boolean realizarTramiteRequerimentoOutroDepartamento;
	private Boolean consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades;
	private Boolean consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades;
	
	private Boolean abrirModalRequerimentoTCC;
	private RequerimentoVO requerimentoVOMapaReposicao;
	private List<SelectItem> listaSelectItemTipoRequerimentoMapaReposicao;
	Map<Integer, List<TurmaVO>> mapTurmas;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;
	private Boolean bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna;
	private RequerimentoDisciplinaVO requerimentoDisciplinaVO;
	private DisciplinaVO disciplinaVO;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private Boolean apresentarBloquearAproveitamento;
	private List<SelectItem> listaSelectItemGrupoFacilitador;
	private Boolean marcarTodos;
	private String autocompleteValorRegistroAcademico;
	private String valorMatricula;
	private List<SelectItem> listaSelectItemCidTipoRequerimento;
	private CidTipoRequerimentoVO cidTipoRequerimentoVO;
	private Boolean marcarTodosCids;
	private List<PessoaVO> listaConsultaAlunoCons;
	private List<SelectItem> tipoConsultaComboAlunoCons;
	private int qtdRegistrosPorPagina;
	private Boolean habilitarTipoAvaliacao;
	private static final ConcurrentHashMap<Integer, Object> bloqueioMatricula = new ConcurrentHashMap<>();



	
	public RequerimentoControle() throws Exception {
		// obterUsuarioLogado();		
	}
	
	@PostConstruct
	public void init() {
		try {
			if(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador") != null) {
				setIdControlador(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador"));			
			}
			setPermiteAbrirRequerimentoForaDoPrazo(false);
			setSituacao((String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("situacao"));
			setControleConsulta(new ControleConsulta());
			definirEscopoRequerimentoComBaseNaPermissaoAcesso();
			realizarVerificacaoPermissaoFiltroRequerimento();
			realizarVerificacaoPermissaoConsultarRequerimentosProprioUsuario();
			inicializarListasSelectItemTodosComboBox();
			montarDadosConfiguracaoFinanceiro();
			getRequerimentoVO().setExigePagamento(Boolean.FALSE);
			setImprimir(false);		
			verificarExisteUnidadeEnsinoLogado();
			verificaPermissaoBotaoAproveitamentoTransfExter();
			getRequerimentoConsVO().setResponsavel(getUsuarioLogado());
			setAutocompleteValorResponsavel(getUsuarioLogado().getPessoa().getNome() + " (" + getUsuarioLogado().getPessoa().getCodigo() + ")");
			
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("RequerimentoControle", "qtdRegistrosPorPagina", false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO) && Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
					setQtdRegistrosPorPagina(Integer.valueOf(layoutPadraoVO.getValor()));
				} else {
					setQtdRegistrosPorPagina(Integer.valueOf(100));
				}
				getControleConsultaOtimizado().setPaginaAtual(1);
				getControleConsultaOtimizado().setLimitePorPagina(getQtdRegistrosPorPagina());
				if(getNomeTelaAtual().endsWith("requerimentoCons.xhtml")) {
					//consultarOtimizado();
				}else {
					getControleConsultaOtimizado().setLimitePorPagina(100);
				}
			}	
			
			setSituacaoFinanceira("");		
			setAbaAtiva("richTab");
			setMensagemID("msg_entre_prmconsulta");
			LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(Requerimento.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
				setVersaoNova(Boolean.valueOf(layoutPadraoVO.getValor()));
				setVersaoAntiga(!Boolean.valueOf(layoutPadraoVO.getValor()));
			} else {
				setVersaoAntiga(false);
				setVersaoNova(true);
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		executarVerificacaoUsuarioPossuiPermissaoAlterarAcrescimoDesconto();
		realizarCarregamentoRequerimentoVindoTelaFichaAluno();
		realizarEdicaoRequerimentoVindoTelaFichaAluno();
		carregarDadosRequerimentoOrigemComunicadoInterno();
		consultarUnidadeEnsino();
		consultarVisaoAluno();
		consultarVisaoProfessorCoordenador();
			
	}
	
	public void realizarVerificacaoPermissaoFiltroRequerimento() {
		setPermitirConsultarRequerimentoOutroConsultorResponsavel(verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutrosConsultoresResponsaveis"));
		setPermitirConsultarRequerimentoOutroDepartametoResponsavel(verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutroDepartamentoResponsavel"));
		if (!getPermitirConsultarRequerimentoOutroConsultorResponsavel() && !getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades()) {
			try {
				// this.getRequerimentoVO().setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(),
				// false, getUsuarioLogado()));
				getRequerimentoVO().setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorCodigoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getRequerimentoConsVO().setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorCodigoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public void executarVisualizacaoFichaAluno() throws Exception {
		RequerimentoVO requ = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
		requ = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(requ.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		PessoaVO obj = (PessoaVO) requ.getPessoa();
		getFacadeFactory().getPessoaFacade().carregarDados(obj, getUsuarioLogado());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		context().getExternalContext().getSessionMap().put("pessoaItem", obj);
	}

	public void executarVisualizacaoFichaAlunoForm() throws Exception {
		PessoaVO obj = (PessoaVO) getRequerimentoVO().getPessoa();
		getFacadeFactory().getPessoaFacade().carregarDados(obj, getUsuarioLogado());
		setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(obj.getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
		context().getExternalContext().getSessionMap().put("pessoaItem", obj);
	}

	public void executarCapturarFotoWebCam() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			getRequerimentoVO().getArquivoVO().setCpfRequerimento(getRequerimentoVO().getPessoa().getCPF());
			String arquivoFoto = getFacadeFactory().getArquivoHelper().getArquivoUploadFoto(getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REQUERIMENTOS_TMP, getUsuarioLogado());
			String arquivoExterno = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + PastaBaseArquivoEnum.IMAGEM_TMP.getValue() + File.separator + getRequerimentoVO().getPessoa().getCPF() + File.separator + getRequerimentoVO().getPessoa().getArquivoImagem().getNome();
			session.setAttribute("arquivoFoto", arquivoFoto);
			setExibirBotao(Boolean.TRUE);
			setExibirUpload(false);
			setCaminhoFotoUsuario(arquivoExterno);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Requerimento</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			setRequerimentoValidado(false);
			setAbaAtiva("richTab");
			setFazerDownload(false);
			setPermiteAbrirRequerimentoForaDoPrazo(false);
			setAbrirOpcaoInformarFuncionarioProximoTramite(false);
			getListaConsultaCurso().clear();
			getListaSelectItemTipoRequerimento().clear();
			setFinalizarEtapaRequerimento(false);
			setMsgRequerimentoImagemGravadoComSucesso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Novo requerimento", "Novo");
			
			if(getRequerimentoAntigo().getCodigo() == 0) {
				removerObjetoMemoria(this);
			}
			setMatricula_valorApresentar("");
			setMatricula_Erro("");
			setTipoRequerimento_Erro("");
			setDepartamentoResponsavel_Erro("");
			setRequerimentoVO(new RequerimentoVO());
			getRequerimentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getRequerimentoVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
			definirEscopoRequerimentoComBaseNaPermissaoAcesso();
			inicializarListasSelectItemTodosComboBox();

//			getRequerimentoVO().setCentroReceita(getConfiguracaoFinanceiroVO().getCentroReceitaRequerimentoPadrao());
//			getRequerimentoVO().getContaCorrenteVO().setCodigo(getConfiguracaoFinanceiroVO().getContaCorrentePadraoRequerimento());
			getRequerimentoVO().setExigePagamento(Boolean.FALSE);
			verificaPermissaoBotaoAproveitamentoTransfExter();
			setTipoRequerimentoDepartamentoRetornarVOs(null);
			getRequerimentoVO().setSomenteAluno(false);
			getRequerimentoVO().setResponsavel(getUsuarioLogadoClone());
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				getRequerimentoVO().setTipoPessoa(TipoPessoa.REQUERENTE);
				getRequerimentoVO().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
				getRequerimentoVO().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
				getRequerimentoVO().getPessoa().setEmail(getUsuarioLogado().getPessoa().getEmail());
				getRequerimentoVO().getPessoa().setProfessor(getUsuarioLogado().getIsApresentarVisaoProfessor());
				getRequerimentoVO().getPessoa().setCoordenador(getUsuarioLogado().getIsApresentarVisaoCoordenador());
				getRequerimentoVO().getPessoa().setCPF(getUsuarioLogado().getPessoa().getCPF());
			}
			executarVerificacaoUsuarioPossuiPermissaoAlterarAcrescimoDesconto();
			setValorAcrescimoDescontoTemp(0.0);			
			setPermiteGravar(null);
			setNovoRegistro(true);
			verificarPermissaoRequerenteAnexarArquivo();
			verificarPermissaoRequerenteVisualizarTramite();
			verificarPermissaoRequerenteInteragirTramite();
			setOncompleteModal("");
			setModalPagamentoOnline("");
			
			if(getRequerimentoAntigo().getCodigo() != 0) {
				getRequerimentoVO().getUnidadeEnsino().setCodigo(getRequerimentoAntigo().getUnidadeEnsino().getCodigo());
				getRequerimentoVO().getUnidadeEnsino().setNome(getRequerimentoAntigo().getUnidadeEnsino().getNome());
				if(Uteis.isAtributoPreenchido(getRequerimentoAntigo().getTipoRequerimento().getTipoRequerimentoAbrirDeferimento().getCodigo())) {
					getRequerimentoVO().getTipoRequerimento().setCodigo(getRequerimentoAntigo().getTipoRequerimento().getTipoRequerimentoAbrirDeferimento().getCodigo());
				}else {
					getRequerimentoVO().getTipoRequerimento().setCodigo(getRequerimentoAntigo().getTipoRequerimento().getCodigo());
				}
				
				getRequerimentoVO().getMatricula().setMatricula(getRequerimentoAntigo().getMatricula().getMatricula());
				consultarMatriculaPorChavePrimaria();
				getRequerimentoVO().setRequerimentoAntigo(getRequerimentoAntigo());
			}		
			setApresentarBloquearAproveitamento(false);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorForm.xhtml");
		} else {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		}
	}

	public void verificaPermissaoBotaoAproveitamentoTransfExter() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_ApresentarBotaoAproveitamento", getUsuarioLogado());
			if (getRequerimentoVO().getCodigo().intValue() > 0 && getRequerimentoVO().getSituacao().equals("EX") && (getRequerimentoVO().getTipoRequerimento().getTipo().equals("TE") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("TI") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("PO") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("AD"))) {
				setApresentarBotaoAproveitamento(Boolean.TRUE);
			} else {
				setApresentarBotaoAproveitamento(Boolean.FALSE);
			}
		} catch (Exception e) {
			setApresentarBotaoAproveitamento(Boolean.FALSE);
		}
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_ApresentarBotaoTransfExterna", getUsuarioLogado());
			if (getRequerimentoVO().getCodigo().intValue() > 0 && getRequerimentoVO().getSituacao().equals("EX") && (getRequerimentoVO().getTipoRequerimento().getTipo().equals("TE") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("AD"))) {
				setApresentarBotaoTransferenciaExt(Boolean.TRUE);
			} else {
				setApresentarBotaoTransferenciaExt(Boolean.FALSE);
			}
		} catch (Exception e) {
			setApresentarBotaoTransferenciaExt(Boolean.FALSE);
		}
	}

	public void verificaPermissaoBotaoMatricula() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_ApresentarBotaoMatricula", getUsuarioLogado());
			if (getRequerimentoVO().getCodigo().intValue() > 0) {
				setApresentarBotaoMatricula(Boolean.TRUE);
			}
		} catch (Exception e) {
			setApresentarBotaoMatricula(Boolean.FALSE);
		}
	}

	public void novoVisaoAluno() throws Exception {
		try {
			setRequerimentoValidado(false);
			setFazerDownload(false);
			setPermiteAbrirRequerimentoForaDoPrazo(false);
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Novo requerimento", "Nova visão aluno");
			setRequerimentoVO(new RequerimentoVO());
			setRequerimentoDisciplinasAproveitadasVO(new RequerimentoDisciplinasAproveitadasVO());
			setNovoRegistro(true);
			// montarListaSelectItemUnidadeEnsino();
			// montarListaSelectItemContaCorrente();
			// montarListaMatriculaAluno();
			getRequerimentoVO().setEdicao(Boolean.FALSE);
//			getRequerimentoVO().setCentroReceita(getConfiguracaoFinanceiroVO().getCentroReceitaRequerimentoPadrao());
//			getRequerimentoVO().getContaCorrenteVO().setCodigo(getConfiguracaoFinanceiroVO().getContaCorrentePadraoRequerimento());
			getRequerimentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getRequerimentoVO().setPessoa(getUsuarioLogado().getPessoa());
			getRequerimentoVO().setExigePagamento(Boolean.FALSE);
			
			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAluno != null) {
				MatriculaVO matriculaVO = new MatriculaVO();
				matriculaVO = (MatriculaVO)visaoAluno.getMatricula().clone();
				visaoAluno.inicializarNovoRequerimento();
				if (getUsuarioLogado().getIsApresentarVisaoPais()) {
					getRequerimentoVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaVO.getMatricula()));
					getRequerimentoVO().setPessoa(getRequerimentoVO().getMatricula().getAluno());
				} else {
					getRequerimentoVO().setMatricula(matriculaVO);
					getRequerimentoVO().getMatricula().getAluno().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
					getRequerimentoVO().getMatricula().getAluno().setNome(getUsuarioLogado().getPessoa().getNome());
				}
				getRequerimentoVO().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
			}
			montarListaSelectItemTipoRequerimentoVisaoAluno();
			montarListaSelectItemDisciplina(getRequerimentoVO().getCurso().getConfiguracaoAcademico());
			montarListaSelectItemUnidadeEnsinoTransferenciaInterna();
			montarListaSelectItemMotivoCancelamentoTrancamento();
			// verificarQuantidadeDiasMaximoAcesso();
			// limparMatricula();
			verificarPermissaoRequerenteAnexarArquivo();
			verificarPermissaoRequerenteVisualizarTramite();
			verificarPermissaoRequerenteInteragirTramite();
			setOncompleteModal("");
			setModalPagamentoOnline("");
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novoVisaoAlunoReposicao(TurmaVO turma, HistoricoVO hist) throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Novo requerimento Reposição", "Nova visão aluno");
		setRequerimentoVO(new RequerimentoVO());
		setNovoRegistro(true);
		montarListaSelectItemTipoRequerimentoReposicao();
		getRequerimentoVO().setEdicao(Boolean.FALSE);
//		getRequerimentoVO().setCentroReceita(getConfiguracaoFinanceiroVO().getCentroReceitaRequerimentoPadrao());
//		getRequerimentoVO().getContaCorrenteVO().setCodigo(getConfiguracaoFinanceiroVO().getContaCorrentePadraoRequerimento());
		getRequerimentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		getRequerimentoVO().setPessoa(getUsuarioLogado().getPessoa());
		getRequerimentoVO().setExigePagamento(Boolean.FALSE);
		VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		if (visaoAluno != null) {
			visaoAluno.inicializarNovoRequerimento();
			getRequerimentoVO().setMatricula(visaoAluno.getMatricula());
		}
		getRequerimentoVO().setObservacao("Solicitação de Reposição de Aula: Disciplina: " + hist.getDisciplina().getNome() + ", Turma: " + turma.getIdentificadorTurma() + ", Data: " + turma.getDataPrimeiraAulaProgramada());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAluno.xhtml");
	}

	public void montarListaMatriculaAluno() {
		try {
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			List lista = consultarMatriculaAluno();
			Iterator i = lista.iterator();
			objs.add(new SelectItem("", ""));
			while (i.hasNext()) {
				MatriculaVO matricula = (MatriculaVO) i.next();
				if (matricula.getSituacao().equals("AT")) {
					getRequerimentoVO().setPessoa(matricula.getAluno());
					objs.add(new SelectItem(matricula.getMatricula(), matricula.getCurso().getNome() + " - " + matricula.getTurno().getNome()));
				}
			}
			setListaSelectItemMatricula(objs);
		} catch (Exception e) {
			setListaSelectItemMatricula(new ArrayList<>(0));
		}
	}

	public List consultarMatriculaAluno() throws Exception {
		List listaResultado = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return listaResultado;
	}

	public void montarDadosConfiguracaoFinanceiro() {
//		try {
//			// ConfiguracaoFinanceiroVO obj =
//			// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS,
//			// getUsuarioLogado(),
//			// null);
//			ConfiguracaoFinanceiroVO obj = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getCodigo());
//			if (obj == null) {
//				setConfiguracaoFinanceiroVO(new ConfiguracaoFinanceiroVO());
//			} else {
//				setConfiguracaoFinanceiroVO(obj);
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}

	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Requerimento</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			setRequerimentoValidado(true); 
			setPermiteAbrirRequerimentoForaDoPrazo(false);
			setAbrirOpcaoInformarFuncionarioProximoTramite(false);
			getListaConsultaCurso().clear();
			setFinalizarEtapaRequerimento(false);
			setMsgRequerimentoImagemGravadoComSucesso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Editar requerimento", "Editando");
			setNovoRegistro(false);
			setMatricula_valorApresentar("");
			setMatricula_Erro("");
			setTipoRequerimento_Erro("");
			setDepartamentoResponsavel_Erro("");
			setApresentarBloquearAproveitamento(false);
			if(!getOrigemExterna()) {
				setAbaAtiva("richTab");
				setTipoRequerimentoVO(new TipoRequerimentoVO());
				RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
				obj.setNovoObj(Boolean.FALSE);
				setRequerimentoVO(obj);
			}
			getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			getRequerimentoVO().getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getRequerimentoVO().getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(), false, getUsuarioLogado()));
			inicializarListasSelectItemTodosComboBox();
			this.setMatricula_valorApresentar(requerimentoVO.getMatricula().getAluno().getNome() + " (" + requerimentoVO.getMatricula().getCurso().getNome() + " - " + requerimentoVO.getMatricula().getTurno().getNome() + ")");
			if (getRequerimentoVO().getValor().equals(0.0)) {
				getRequerimentoVO().setExigePagamento(Boolean.FALSE);
			} else {
				getRequerimentoVO().setExigePagamento(Boolean.TRUE);
			}			
			getRequerimentoVO().setSomenteAluno(getRequerimentoVO().getTipoPessoa().equals(TipoPessoa.ALUNO));			
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				setTipoRequerimentoDepartamentoRetornarVOs(getFacadeFactory().getRequerimentoFacade().consultarDepartamentoAnterioresPermiteRetornar(getRequerimentoVO()));
				for (RequerimentoDisciplinasAproveitadasVO rda : getRequerimentoVO().getListaRequerimentoDisciplinasAproveitadasVOs()) {
					rda.setQtdIndeferimentos(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(rda.getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getMatricula()));
				}
			}
			getTipoRequerimentoVO().setCodigo(getRequerimentoVO().getTipoRequerimento().getCodigo());
			getRequerimentoVO().setEdicao(Boolean.TRUE);
			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAluno != null) {
				// montarListaMatriculaAluno();
				visaoAluno.inicializarNovoRequerimento();
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getMaterialRequerimentoVOs())) {
					getFacadeFactory().getMaterialRequerimentoFacade().permitirExcluirArquivoMaterialRequerimento(getRequerimentoVO().getMaterialRequerimentoVOs(), getUsuarioLogado());
				}
			}
			if (!getRequerimentoVO().getArquivoVO().getCodigo().equals(0)) {
				getRequerimentoVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
			}
			if (getRequerimentoVO().getTipoRequerimento().getTramitaEntreDepartamentos()) {
				getRequerimentoVO().getPodeSerEncaminhadoProximoDepartamento();
			}
			if(getRequerimentoVO().getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
				montarListaSelectItemDisciplinaAproveitamento();
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Editar requerimento", "Editando");
			verificaPermissaoBotaoAproveitamentoTransfExter();
			this.verificarPermissaoEnviarDepartamentoAnterior();
			validarApresentacaoBotaoImprimir(getRequerimentoVO());
			inicializarTipoPessoaPorTipoRequerimento();
			consultarContaReceberAlunoRequerimento();
			executarVerificacaoProfessorMinistrouAula();
			if (getRequerimentoVO().getDisciplina().getCodigo().intValue() == 0) {
				disciplinaVazia = Boolean.FALSE;
				montarListaSelectItemDisciplina(getRequerimentoVO().getCurso().getConfiguracaoAcademico());
			} else {
				disciplinaVazia = Boolean.TRUE;
			}
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			inicializarListaSelectItemSituacaoRequerimentoDepartamento();
			verificarPermissaoAlterarObservacaoIncluidaPeloRequerente();
			verificarPermissaoRequerenteAnexarArquivo();
			verificarPermissaoRequerenteVisualizarTramite();
			verificarPermissaoRequerenteInteragirTramite();
			verificarPermissaoRequerenteInteragirTramite();	
			verificarExibirCampoNota();
			
			abrirDetalheHistoricoRequerimento();
			
			if (!getListaSelectItemUnidadeEnsino().isEmpty()) {
    			Iterator<SelectItem> itens = getListaSelectItemUnidadeEnsino().iterator();
    			boolean encontrou = false;
        		while (itens.hasNext()) {
        		    SelectItem item = itens.next();
        		    if (item.getValue().equals(getRequerimentoVO().getUnidadeEnsino().getCodigo())) {
        		    	encontrou = true;
        		    	break;
        		    }
        		}
        		
        		if(!encontrou) {
        			getListaSelectItemUnidadeEnsino().add(new SelectItem(getRequerimentoVO().getUnidadeEnsino().getCodigo(), getRequerimentoVO().getUnidadeEnsino().getNome()));
        		}
        		
			}
			
			if(getRequerimentoVO().getTipoRequerimento().getTipo().equals("TC") && getIsPermiteDeferir() && !getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios()) {
				realizarSomatoriaNotaMonografia();
			}
		
			if(visaoAluno != null && getRequerimentoVO().getPossuiInteracaoAtendenteNaoLida() && getIsRequerimentoPessoaLogada()){
				getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().alterarVisualizacaoInteracaoHistrico(getRequerimentoVO(), getUsuarioLogado());
			} else if(getRequerimentoVO().getPossuiInteracaoRequerenteNaoLida() && getIsResponsavelPessoaLogada()) {
				getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().alterarVisualizacaoInteracaoHistrico(getRequerimentoVO(), getUsuarioLogado());
			}
			
			if(!Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoTrabalhoConclusaoCurso()) && getRequerimentoVO().getTipoRequerimento().getTipo().equals("TC")) {
				getRequerimentoVO().setTipoTrabalhoConclusaoCurso("AR");
			}
			realizarNavegacaoAba();
			montarListaSelectItemGrupoFacilitador();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorForm.xhtml");	
		}
		
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorForm.xhtml");	
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm.xhtml");

	}

	private void realizarSomatoriaNotaMonografia() {
		Double somaNotaMonografia = new Double(0);
		for(TipoRequerimentoDepartamentoVO departamentoVO : getRequerimentoVO().getTipoRequerimento().getTipoRequerimentoDepartamentoVOs()) {
			RequerimentoHistoricoVO requerimentoHistoricoVO = getRequerimentoVO().getRequerimentoHistoricoVOs().stream().filter(rh -> rh.getDepartamento().getCodigo().equals(departamentoVO.getDepartamento().getCodigo()) && rh.getOrdemExecucaoTramite().equals(departamentoVO.getOrdemExecucao())).max(Comparator.comparing(RequerimentoHistoricoVO::getDataConclusaoDepartamento, Comparator.nullsFirst(Comparator.naturalOrder()))).orElse(null);
			if(Uteis.isAtributoPreenchido(requerimentoHistoricoVO.getNotaTCC())) {
				somaNotaMonografia += requerimentoHistoricoVO.getNotaTCC();
			}
		}
		getRequerimentoVO().setNotaMonografia(somaNotaMonografia);
	}

	private void verificarExibirCampoNota() throws Exception {
		TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVOAtual = requerimentoVO.getTipoRequerimento().consultarTipoRequerimentoDepartamentoVOs(getRequerimentoVO().getOrdemExecucaoTramiteDepartamento());
		if(tipoRequerimentoDepartamentoVOAtual != null) {
			setPodeInserirNota(tipoRequerimentoDepartamentoVOAtual.getPodeInserirNota());
		}
	}

	public void alterarObservacaoTramiteDepartamento() {
		try {
			RequerimentoHistoricoVO obj = (RequerimentoHistoricoVO) getRequestMap().get("requerimentoHistoricoItem");
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getRequerimentoHistoricoFacade().alterar(obj, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarCampoObservacaoTramiteDepartamento() {
		if (requerimentoVO.getIsIndeferido() || requerimentoVO.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor()) || requerimentoVO.getRequerimentoHistoricoVOs().isEmpty()) {
			return false;
		}
		RequerimentoHistoricoVO obj = (RequerimentoHistoricoVO) getRequestMap().get("requerimentoHistoricoItem");
		return requerimentoVO.getRequerimentoHistoricoVOs().get(requerimentoVO.getRequerimentoHistoricoVOs().size() - 1).getCodigo().equals(obj.getCodigo()) && requerimentoVO.getFuncionarioVO().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo());
	}

	public void inicializarDadosImpressaoComprovante() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
			getFacadeFactory().getRequerimentoFacade().carregarDados(obj, getUsuarioLogado());
			setRequerimentoVO(obj);
			verificarLayoutPadrao();
			montarListaSelectItemImpressora();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void iniciarTransferenciaExt() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			request.getSession().setAttribute("requerimento", Boolean.TRUE);
			setPopUpAbrir("abrirPopup('transferenciaEntradaForm.xhtml?requerimento=" + getRequerimentoVO().getCodigo() + "','TransferenciaEntrada', 950, 595);");
		} catch (Exception e) {
			setPopUpAbrir("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void iniciarAproveitamento() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			setPopUpAbrir("abrirPopupMaximizada('aproveitamentoDisciplinaForm.xhtml?requerimento=" + getRequerimentoVO().getCodigo() + "','Aproveitamento', 950, 595);");
		} catch (Exception e) {
			setPopUpAbrir("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCentroReceita() {
//		try {
//			List objs = new ArrayList<>(0);
//			if (getCampoConsultaCentroReceita().equals("descricao")) {
//				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaCentroReceita().equals("identificadorCentroReceita")) {
//				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorIdentificadorCentroReceita(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaCentroReceita().equals("nomeDepartamento")) {
//				objs = getFacadeFactory().getCentroReceitaFacade().consultarPorNomeDepartamento(getValorConsultaCentroReceita(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//			}
//			setListaConsultaCentroReceita(objs);
//			setMensagemID("msg_dados_consultados");
//		} catch (Exception e) {
//			setListaConsultaCentroReceita(new ArrayList<>(0));
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public List<SelectItem> tipoConsultaComboCentroReceita;
	public List<SelectItem> getTipoConsultaComboCentroReceita() {
		if(tipoConsultaComboCentroReceita == null) {
		tipoConsultaComboCentroReceita = new ArrayList<SelectItem>(0);
		tipoConsultaComboCentroReceita.add(new SelectItem("descricao", "Descrição"));
		tipoConsultaComboCentroReceita.add(new SelectItem("identificadorCentroReceita", "Identificador Centro Receita"));
		tipoConsultaComboCentroReceita.add(new SelectItem("nomeDepartamento", "Departamento"));
		}
		return tipoConsultaComboCentroReceita;
	}

	public void selecionarCentroReceita() {
//		CentroReceitaVO obj = (CentroReceitaVO) context().getExternalContext().getRequestMap().get("centroReceitaItem");
//		this.getRequerimentoVO().setCentroReceita(obj);
	}

	public void verificarApresentacaoQuestionarioAoGravar() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoAluno();
			setGravadoComSucesso("");
			setAbrirModalQuestionario(false);
			setOncompleteModal("");
			limparMensagem();
			try {
				RequerimentoVO.validarDados(getRequerimentoVO());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				return;
			}
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
			if(!getRequerimentoValidado() && verificarDuplicidadeRequerimento()) {
				return;
			}
			if (getRequerimentoVO().getNovoObj() && getRequerimentoVO().getTipoRequerimento().getQuestionario().getCodigo() > 0) {
				realizarResponderQuestionario();
				setAbrirModalQuestionario(true);
				return;
			}			
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
				
				if(getRequerimentoVO().getNovoObj()) {
					
					if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
						realizarPreviewCertificado(getRequerimentoVO());
						setOncompleteModal(getPreviewCertificado());
					}else {
						gravarVisaoAluno();
					}
				}else {
					gravarVisaoAluno();
				}
			} else {
				if(getRequerimentoVO().getNovoObj()) {
					if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
						realizarPreviewCertificado(getRequerimentoVO());
						setOncompleteModal(getPreviewCertificado());
					}else {
						gravar();
					}
				}else {
					gravar();
				}
				//gravar();
			}
			getRequerimentoVO().setRequerimentoAntigo(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private boolean verificarDuplicidadeRequerimento() throws Exception {
		List<RequerimentoVO> listaRequerimento = getFacadeFactory().getRequerimentoFacade().consultarPorTipoRequerimentoAberto(getRequerimentoVO(), getUsuarioLogado());
		if(Uteis.isAtributoPreenchido(listaRequerimento)){
			String situacao = UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaRequerimento, "situacao");
			String ids = UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaRequerimento, "codigo");
			setMsgRequerimentoExistente(UteisJSF.internacionalizar("msg_Requerimento_avisoRequerimentoExistente").replace("{0}", ids).replace("{1}", String.valueOf(situacao)));
			setOncompleteModal("RichFaces.$('panelRequerimentoRequerimentoExistente').show()");
			return true;
		}
		return false;
	}


	public void gravarRequerimentoComQuestionario() {
		/*if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			gravarVisaoAluno();
		} else {
			gravar();
		}*/
		try {
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
		
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {				
				
				if(getRequerimentoVO().getNovoObj()) {
					
					if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
						realizarPreviewCertificado(getRequerimentoVO());
						setOncompleteModal(getPreviewCertificado());
						setAbrirModalQuestionario(false);
					}else {
						gravarVisaoAluno();
					}
				}else {
					gravarVisaoAluno();
				}
			} else {
				if(getRequerimentoVO().getNovoObj()) {				
					if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) || Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
						realizarPreviewCertificado(getRequerimentoVO());
						setOncompleteModal(getPreviewCertificado());
						setAbrirModalQuestionario(false);
					}else {
						gravar();
					}
				}else {
					gravar();
				}
				//gravar();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Requerimento</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			boolean novo = getRequerimentoVO().isNovoObj();
//			if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna() && requerimentoVO.getTipoRequerimento().getValidarVagasPorNumeroComputadoresUnidadeEnsino()) {
//				getFacadeFactory().getRequerimentoFacade().persistirSicronizadoRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), Boolean.FALSE, getRequerimentoVO().getExigePagamento(), getConfiguracaoGeralPadraoSistema(), getUnidadeEnsinoLogado(), getUsuarioLogado());
//			}else {
//				getFacadeFactory().getRequerimentoFacade().persistirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), Boolean.FALSE, getRequerimentoVO().getExigePagamento(), getConfiguracaoGeralPadraoSistema(),  getUnidadeEnsinoLogado(), getUsuarioLogado());
//			}
			
			if (novo && (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor())) {
				if (getRequerimentoVO().getTipoRequerimento().getMensagemAlerta().trim().isEmpty()) {
					setMsgRequerimentoImagemGravadoComSucesso("Requerimento gravado com sucesso.");
					if (!getRequerimentoVO().getValor().equals(0.0)) {
						setMsgRequerimentoPossuiValor("O requerimento só será inicializado após o pagamento.");
						consultarContaReceberAlunoRequerimento();
//						if (getRequerimentoVO().getPermitirRecebimentoCartaoCreditoOnline()) {
//							incicializarDadosPagamentoOnline();
//							setModalPagamentoOnline("RichFaces.$('panelPagamento').show()");
//						}
					} else {
						setMsgRequerimentoPossuiValor("");
					}
				} else {
					setMsgRequerimentoImagemGravadoComSucesso(getRequerimentoVO().getTipoRequerimento().getMensagemAlerta());
				}
			}
			for (OperacaoFuncionalidadeVO operacaoFuncionalidadeVO : getOperacaoFuncionalidadeVOs()) {
				operacaoFuncionalidadeVO.setCodigoOrigem(getRequerimentoVO().getCodigo().toString());
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
			}
			this.setMatricula_Erro("");
			getRequerimentoVO().setEdicao(Boolean.TRUE);
			setAbrirModalQuestionario(false);
			setMensagemID("msg_dados_gravados");
			// } else {
			// setMensagemDetalhada("msg_erro",
			// "Este requerimento já foi quitado financeiramente, portanto não pode ser alterado.");
			// }
			verificaPermissaoBotaoAproveitamentoTransfExter();
			if(getRequerimentoVO().getTipoRequerimento().getTipo().equals("TC")) {
				verificarExibirCampoNota();
			}
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			inicializarListaSelectItemSituacaoRequerimentoDepartamento();
			setPermiteGravar(null);
			getRequerimentoVO().setPodeSerIniciadoExecucaoDepartamentoAtual(null);
			if(!getRequerimentoVO().getNovoObj()) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			}
			getRequerimentoVO().setRequerimentoAntigo(null);
			setRequerimentoAntigo(null);
			
			if(getRequerimentoVO().getIsApresentarModalDeCobranca() && novo) {
					Long qtdDias = getFacadeFactory().getRequerimentoFacade().qtdDiasExedidosDoPrazoComBaseUltimaAula(requerimentoVO.getMatriculaPeriodoVO().getCodigo());
					setMsgNovoRequerimento(UteisJSF.internacionalizar("msg_Requerimento_avisoTaxaCobrancaRequerimento").replace("{0}", String.valueOf(qtdDias)));
					setOncompleteModal("RichFaces.$('panelRequerimentoTCC').show()");
			}
			
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		}
	}

	public String gravarDisciplina() {
		try {
			getFacadeFactory().getRequerimentoFacade().alterarDisciplina(getRequerimentoVO().getCodigo(), getRequerimentoVO().getDisciplina().getCodigo(), getUsuarioLogado());
			getRequerimentoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getRequerimentoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));			
			disciplinaVazia = Boolean.FALSE;			
			setMensagemID("msg_dados_gravados");						
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}	
	
	public void gravarVisaoAluno() throws Exception {
	    Object lock = bloqueioMatricula.computeIfAbsent( getUsuarioLogado().getCodigo(), k -> new Object());
	    synchronized (lock) {
	        MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarBloqueioMatriculaPorMatricula(getRequerimentoVO().getMatricula());
	        if (Uteis.isAtributoPreenchido(mat)) {
				getFacadeFactory().getMatriculaFacade().excluirBolqueioMatricula(mat, getUsuarioLogado());
				throw new ConsistirException("Não é Possível Abrir Requerimento Enquanto é Realizada uma Renovação de Matrícula. ");

	        }
		try {
			executarValidacaoSimulacaoVisaoAluno();
			boolean isNovo = getRequerimentoVO().getNovoObj();
			if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna()) {
				getFacadeFactory().getMatriculaFacade().incluirBloqueioMatricula(getRequerimentoVO().getMatricula(), getUsuarioLogado());
			}
//			if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna() && requerimentoVO.getTipoRequerimento().getValidarVagasPorNumeroComputadoresUnidadeEnsino()) {
//				getFacadeFactory().getRequerimentoFacade().persistirSicronizadoRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), Boolean.TRUE, getRequerimentoVO().getExigePagamento(), getConfiguracaoGeralPadraoSistema(), getUnidadeEnsinoLogado(), getUsuarioLogado());
//			}else {
//				getFacadeFactory().getRequerimentoFacade().persistirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), Boolean.TRUE, getRequerimentoVO().getExigePagamento(), getConfiguracaoGeralPadraoSistema(),  getUnidadeEnsinoLogado(), getUsuarioLogado());
//			}
				if (getRequerimentoVO().getTipoRequerimento().getMensagemAlerta().trim().isEmpty()) {
					setMsgRequerimentoImagemGravadoComSucesso("Requerimento gravado com sucesso.");
				} else {
					setMsgRequerimentoImagemGravadoComSucesso(getRequerimentoVO().getTipoRequerimento().getMensagemAlerta());
				}
				
			if (!getRequerimentoVO().getValorTotalFinal().equals(0.0) && Uteis.isAtributoPreenchido(getRequerimentoVO().getContaReceber()) && getRequerimentoVO().getSituacaoFinanceira().equals("PE")) {
				setMsgRequerimentoPossuiValor("O requerimento só será inicializado após o pagamento.");
				consultarContaReceberAlunoRequerimento();
//				if (getRequerimentoVO().getPermitirRecebimentoCartaoCreditoOnline()) {
//					incicializarDadosPagamentoOnline();
//					setModalPagamentoOnline("RichFaces.$('panelPagamento').show()");
//				}
			} else {
				setMsgRequerimentoPossuiValor("");
			}	
			if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna()) {
				getFacadeFactory().getMatriculaFacade().excluirBolqueioMatricula(requerimentoVO.getMatricula(), getUsuarioLogado());
			}

			if(getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso() || (getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital() && !getRequerimentoVO().getMotivoNaoAceiteCertificado().equals(""))) {
				setGravadoComSucesso("RichFaces.$('panelGravadoComSucesso').show()");
			}
			this.setMatricula_Erro("");
			getRequerimentoVO().setEdicao(true);
			setAbrirModalQuestionario(false);
			getRequerimentoVO().setPodeSerIniciadoExecucaoDepartamentoAtual(null);
			if(!getRequerimentoVO().getNovoObj()) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			}
			if(((getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.TRANCAMENTO.getValor()) && getRequerimentoVO().getIsDeferido())
					|| (getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.CANCELAMENTO.getValor()) && getRequerimentoVO().getIsDeferido())
					|| (getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.TRANSF_INTERNA.getValor()) && getRequerimentoVO().getIsDeferido()))
					&& getVisaoAlunoControle() != null){
				List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaNaoCancelada(getUsuarioLogado().getPessoa().getCodigo(),false, false, true, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				if(!matriculaVOs.stream().anyMatch(m -> m.getMatricula().equals(getVisaoAlunoControle().getMatricula().getMatricula()))) {
					getVisaoAlunoControle().setMatricula(matriculaVOs.get(0));
				}
				getUsuarioLogado().setMatriculaVOs(matriculaVOs);
				getUsuarioLogado().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(getConfiguracaoGeralSistemaVO(), getVisaoAlunoControle().getMatricula(),getVisaoAlunoControle().getMatricula().getAlunoNaoAssinouContratoMatricula(), getUsuarioLogado()));				
				getVisaoAlunoControle().inicializarListasSelectItemTodosComboBox();
				
			}

			if(getRequerimentoVO().getIsApresentarModalDeCobranca() && isNovo) {
					Long qtdDias = getFacadeFactory().getRequerimentoFacade().qtdDiasExedidosDoPrazoComBaseUltimaAula(requerimentoVO.getMatriculaPeriodoVO().getCodigo());
					setMsgNovoRequerimento(UteisJSF.internacionalizar("msg_Requerimento_avisoTaxaCobrancaRequerimento").replace("{0}", String.valueOf(qtdDias)));
					setOncompleteModal("RichFaces.$('panelRequerimentoTCC').show()");
					setAbrirModalRequerimentoTCC(true);
			}
			setMensagemID("msg_dados_gravados");
		}catch (Exception e) {
			if (requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna()) {
                getFacadeFactory().getMatriculaFacade().excluirBolqueioMatricula(requerimentoVO.getMatricula(), getUsuarioLogado());
            }
			setGravadoComSucesso("");
			setMsgRequerimentoImagemGravadoComSucesso("");
			setMsgRequerimentoPossuiValor("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			bloqueioMatricula.remove( getUsuarioLogado().getCodigo());
	    }
    }
}

	public void visualizarQuestionarioTramite() {
		setFinalizarEtapaRequerimento(false);
		setGravadoComSucesso("");
		visualizarQuestionarioDepartamento((RequerimentoHistoricoVO) getRequestMap().get("requerimentoHistoricoItem"));
	}

	private void visualizarQuestionarioDepartamento(RequerimentoHistoricoVO requerimentoHistoricoVO) {
		try {
			setAbrirModalQuestionarioRequerimentoHistorico(false);
			setRequerimentoHistoricoVO(new RequerimentoHistoricoVO());
			setRequerimentoHistoricoVO(requerimentoHistoricoVO);
			if (getRequerimentoHistoricoVO().getQuestionario().getCodigo() > 0) {
				if (getRequerimentoHistoricoVO().getQuestionario().getPerguntaQuestionarioVOs().isEmpty()) {
					getRequerimentoHistoricoVO().setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getRequerimentoHistoricoVO().getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimentoHistorico(getRequerimentoHistoricoVO().getRequerimento(), getRequerimentoHistoricoVO().getDepartamento().getCodigo(), getRequerimentoHistoricoVO().getOrdemExecucaoTramite(), getRequerimentoHistoricoVO().getQuestionario());
				}
			}
			setAbrirModalQuestionarioRequerimentoHistorico(true);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void visualizarQuestionarioTramiteAtual() {

		try {
			setAbrirModalQuestionarioRequerimentoHistorico(false);
			setFinalizarEtapaRequerimento(false);
			setGravadoComSucesso("");
			visualizarQuestionarioDepartamento(getRequerimentoVO().consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarQuestionarioTramite() {
		try {
			setGravadoComSucesso("");
			getRequerimentoHistoricoVO().setGravarRespostaQuestionario(true);
			getFacadeFactory().getRequerimentoHistoricoFacade().gravarRespostaQuestionario(getRequerimentoHistoricoVO(), getUsuarioLogado());
			getRequerimentoHistoricoVO().setGravarRespostaQuestionario(false);
			setAbrirModalQuestionarioRequerimentoHistorico(false);
			setGravadoComSucesso("RichFaces.$('panelGravadoComSucesso').show();");
			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarResponsavelVisaoGerado() {
		try {
			getRequerimentoVO().setResponsavel(getUsuarioLogadoClone());
			if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
				getRequerimentoVO().setVisaoGerado(TipoVisaoAcesso.VISAO_ALUNO.getValor());
			} else if (getUsuarioLogado().getVisaoLogar().equals("pais")) {
				getRequerimentoVO().setVisaoGerado(TipoVisaoAcesso.VISAO_RESPONSAL_LEGAL.getValor());
			} else if (getUsuarioLogado().getPerfilAdministrador() && getUsuarioLogado().getVisaoLogar().equals("")) {
				getRequerimentoVO().setVisaoGerado(TipoVisaoAcesso.VISAO_DIRETOR_MULTI_CAMPUS.getValor());
			} else {
				getRequerimentoVO().setVisaoGerado(TipoVisaoAcesso.VISAO_FUNCIONARIO.getValor());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarNovoArquivo() {
		try {
			getFacadeFactory().getRequerimentoFacade().gravarNovoArquivo(getRequerimentoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			if (getRequerimentoVO().getTipoRequerimento().getMensagemAlerta().trim().isEmpty()) {
				setMsgRequerimentoImagemGravadoComSucesso("Arquivo gravado com sucesso.");
			} else {
				setMsgRequerimentoImagemGravadoComSucesso(getRequerimentoVO().getTipoRequerimento().getMensagemAlerta());
			}
			setGravadoComSucesso("RichFaces.$('panelGravadoComSucesso').show()");
		} catch (Exception e) {
			setGravadoComSucesso("");
			setMsgRequerimentoImagemGravadoComSucesso("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCaminhoServidorDownloadMaterialReq() {
		try {
			return "location.href='../../DownloadSV'";
			// MaterialRequerimentoVO obj = (MaterialRequerimentoVO)
			// context().getExternalContext().getRequestMap().get("materialRequerimentoVO");
			// return
			// getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(),
			// PastaBaseArquivoEnum.REQUERIMENTOS,
			// getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void realizarDownloadMaterialRequerimento() {
		MaterialRequerimentoVO obj = (MaterialRequerimentoVO) context().getExternalContext().getRequestMap().get("materialRequerimentoVOItem");
		context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());

	}

	public void upLoadArquivoMaterialReq(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialRequerimento().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REQUERIMENTOS_TMP, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void removerMaterialRequerimento() {
		try {
			MaterialRequerimentoVO obj = (MaterialRequerimentoVO) context().getExternalContext().getRequestMap().get("materialRequerimentoVOItem");
			realizarExcluirMaterialRequerimento(obj);
			if (getMaterialRequerimento().getCodigo().equals(obj.getCodigo())) {
				setMaterialRequerimento(new MaterialRequerimentoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarExcluirMaterialRequerimento(MaterialRequerimentoVO obj) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			if (getRequerimentoVO().getMaterialRequerimentoVOs().removeIf(mrvo -> mrvo.getCodigo().equals(obj.getCodigo()))) {
				getFacadeFactory().getMaterialRequerimentoFacade().excluir(obj, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
		} else if (obj != null){
			if (getRequerimentoVO().getMaterialRequerimentoVOs().removeIf(mrvo -> mrvo.getDescricao().equals(obj.getDescricao()) && mrvo.getArquivoVO().getNome().equals(obj.getArquivoVO().getNome()))) {
				String caminhoCompletoArquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + obj.getArquivoVO().getPastaBaseArquivo();
				getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(obj.getArquivoVO(), caminhoCompletoArquivo);
			}
		}
	}

	public String adicionarMaterialRequerimento() throws Exception {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			RequerimentoHistoricoVO requerimentoHistoricoAtual = getRequerimentoHistoricoAtual();
			validarDadosInclusaoAnexo();
			getMaterialRequerimento().setDataDisponibilizacaoArquivo(new Date());
			getMaterialRequerimento().setUsuarioDisponibilizouArquivo(getUsuarioLogadoClone());
			if(!requerimentoHistoricoAtual.getCodigo().equals(0)) {				
				getMaterialRequerimento().setRequerimentoHistorico(requerimentoHistoricoAtual);
			}
			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");			
			if(visaoAluno != null || getIsRequerimentoPessoaLogada()) {
				getMaterialRequerimento().setDisponibilizarParaRequerente(true);
			}
			getRequerimentoVO().getMaterialRequerimentoVOs().add(getMaterialRequerimento());
			if (Uteis.isAtributoPreenchido(getRequerimentoVO())) {
				getFacadeFactory().getMaterialRequerimentoFacade().incluirMaterialRequerimentos(getRequerimentoVO().getCodigo(), getRequerimentoVO().getMaterialRequerimentoVOs(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				getFacadeFactory().getRequerimentoFacade().alterarDataUltimaAlteracao(getRequerimentoVO().getCodigo());
			}
			setMaterialRequerimento(new MaterialRequerimentoVO());
			setMensagemID("Arquivo Adicionado com Sucesso.");
			if(!getRequerimentoVO().getNovoObj()) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			}
			if(visaoAluno != null && Uteis.isAtributoPreenchido(getRequerimentoVO().getMaterialRequerimentoVOs())) {
				getFacadeFactory().getMaterialRequerimentoFacade().permitirExcluirArquivoMaterialRequerimento(getRequerimentoVO().getMaterialRequerimentoVOs(), getUsuarioLogado());
			}
			return redirecionarAbaAnexo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void adicionarRequerimentoDisciplinasAproveitadas() {
		String unidadeEnsinoTemp = null;
		CidadeVO cidade = null;
		try {
			unidadeEnsinoTemp =getRequerimentoDisciplinasAproveitadasVO().getInstituicao(); 
			cidade = (CidadeVO) Uteis.clonar(getRequerimentoDisciplinasAproveitadasVO().getCidade()); 
			DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getRequerimentoDisciplinasAproveitadasVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getRequerimentoDisciplinasAproveitadasVO().setDisciplina(disciplina);
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().adicionarRequerimentoDisciplinasAproveitadas(getRequerimentoVO(), getRequerimentoDisciplinasAproveitadasVO(), getUsuarioLogadoClone());
			setRequerimentoDisciplinasAproveitadasVO(new RequerimentoDisciplinasAproveitadasVO());
			getRequerimentoDisciplinasAproveitadasVO().setInstituicao(unidadeEnsinoTemp);
			getRequerimentoDisciplinasAproveitadasVO().setCidade(cidade);
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}finally {
			unidadeEnsinoTemp = null;
			cidade = null;
		}
	}
	
	public void editarRequerimentoDisciplinasAproveitadas() {
		try {
			RequerimentoDisciplinasAproveitadasVO rda = (RequerimentoDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("requerimentoDisciplinasAproveitadasItens");
			setRequerimentoDisciplinasAproveitadasVO(rda);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void removerRequerimentoDisciplinasAproveitadas() {
		try {
			RequerimentoDisciplinasAproveitadasVO rda = (RequerimentoDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("requerimentoDisciplinasAproveitadasItens");
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().removerRequerimentoDisciplinasAproveitadas(getRequerimentoVO(), rda, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarDeferimentoRequerimentoDisciplinasAproveitadas() {
		try {
			RequerimentoDisciplinasAproveitadasVO rda = (RequerimentoDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("requerimentoDisciplinasAproveitadasItens");
//			getFacadeFactory().getRequerimentoFacade().persistirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), Boolean.FALSE, getRequerimentoVO().getExigePagamento(), getConfiguracaoGeralPadraoSistema(),  getUnidadeEnsinoLogado(), getUsuarioLogado());
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().realizarConfirmacaoDeferido(rda, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarIndeferimentoRequerimentoDisciplinasAproveitadas() {
		try {
			RequerimentoDisciplinasAproveitadasVO rda = (RequerimentoDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("requerimentoDisciplinasAproveitadasItens");
			getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().realizarConfirmacaoIndeferido(rda, getUsuarioLogadoClone());
			rda.setQtdIndeferimentos(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(rda.getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getMatricula()));
			if(rda.getQtdIndeferimentos() >= getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento()) {
				setApresentarBloquearAproveitamento(Boolean.TRUE);
			}
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void upLoadArquivoRequerimentoDisciplinasAproveitadas(FileUploadEvent uploadEvent) {
		try {
			getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino().setCpfRequerimento(getRequerimentoVO().getPessoa().getCPF());
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent, getRequerimentoDisciplinasAproveitadasVO().getArquivoPlanoEnsino(), getRequerimentoVO().getArquivoVO().getCpfRequerimento() + getRequerimentoVO().getTipoRequerimento().getNome(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REQUERIMENTOS_TMP, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public String getUrlArquivoRequerimentoDisciplinasAproveitadasDownloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	public void validarNotaRequerimentoDisciplinasAproveitadas() {
		try {
			if(!Uteis.isAtributoPreenchido(getRequerimentoVO().getMatricula().getCurso().getConfiguracaoAcademico())) {
				getRequerimentoVO().getMatricula().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(getRequerimentoVO().getMatricula().getCurso().getCodigo(), getUsuarioLogado()));
			}
			if (!getRequerimentoDisciplinasAproveitadasVO().getNota().equals(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(getRequerimentoDisciplinasAproveitadasVO().getNota(), getRequerimentoVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()))) {
				getRequerimentoDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A Nota Deve Conter o Número de Casas Decimais Presente na Configuração Acadêmica.Quantidade de Casas Decimais:" + getRequerimentoVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
			} if(getRequerimentoDisciplinasAproveitadasVO().getNota() > 10) {
				getRequerimentoDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A nota não pode ser maior que 10.");
			} else {
				setMensagemID("");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void selecinarDisciplinaRequerimentoDisciplinasAproveitadas() {
		try {			
			getRequerimentoDisciplinasAproveitadasVO().setCargaHoraria(getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaDisciplinaPorDisciplinaETurma(getRequerimentoDisciplinasAproveitadasVO().getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getMatricula(), getUsuarioLogadoClone()));
			if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento()) && getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(getRequerimentoDisciplinasAproveitadasVO().getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getMatricula()) >=
					getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento()) {
				getRequerimentoDisciplinasAproveitadasVO().setApresentarMotivoRevisaoAnaliseAproveitamento(true);
			}else {
				getRequerimentoDisciplinasAproveitadasVO().setApresentarMotivoRevisaoAnaliseAproveitamento(false);
			}
			if(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarSeExisteBloqueioParaDisciplinaAproveitadaPorMatricula(getRequerimentoDisciplinasAproveitadasVO().getDisciplina().getCodigo(), getRequerimentoVO().getMatricula().getMatricula())) {
				String msgBloqueioNovaSolicitacaoAproveitamento = getFacadeFactory().getTipoRequerimentoFacade().consultarMsgBloqueioNovaSolicitacaoAproveitamento(getRequerimentoVO().getTipoRequerimento().getCodigo(), getUsuarioLogadoClone());
				if(msgBloqueioNovaSolicitacaoAproveitamento.isEmpty()) {
					msgBloqueioNovaSolicitacaoAproveitamento = "Não é possível realizar o aproveitamento para essa disciplina, pois a mesma foi bloqueada pelo administrativo por situação de indeferimentos anteriores.";
				}
				Uteis.checkState(true,msgBloqueioNovaSolicitacaoAproveitamento);	
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarRequerimentoDisciplinasAproveitadasIndeferidas() {
		try {
			RequerimentoDisciplinasAproveitadasVO rda = (RequerimentoDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("requerimentoDisciplinasAproveitadasItens");
			setListaRequerimentoDisciplinasAproveitadasVO(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarRequerimentoDisciplinasAproveitadasIndeferidas(getRequerimentoVO().getMatricula().getMatricula(), rda.getDisciplina().getCodigo(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void validarDadosInclusaoAnexo() throws Exception {
		if (getMaterialRequerimento().getDescricao() == null || getMaterialRequerimento().getDescricao().equals("")) {
			throw new Exception("O campo DESCRIÇÃO deve ser informado.");
		}
		if (getMaterialRequerimento().getArquivoVO().getNome() == null || getMaterialRequerimento().getArquivoVO().getNome().equals("")) {
			throw new Exception("O campo ARQUIVO deve ser informado.");
		}
	}

	public String gravarDuranteEmissaoBoleto() {
		try {
			if (requerimentoVO.getSituacaoFinanceira().equals("PE")) {
				if (requerimentoVO.getValor().equals(0.0)) {
					requerimentoVO.setSituacaoFinanceira("IS");
					requerimentoVO.setSituacao("EX");
				} else {
					requerimentoVO.setResponsavelEmissaoBoleto(getUsuarioLogadoClone());
				}
				if (requerimentoVO.isNovoObj().booleanValue()) {
					getFacadeFactory().getRequerimentoFacade().incluir(requerimentoVO, getUsuarioLogado(),  getConfiguracaoGeralPadraoSistema());
				} else {
					getFacadeFactory().getRequerimentoFacade().alterar(requerimentoVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				}
				this.setMatricula_Erro("");
				getRequerimentoVO().setEdicao(Boolean.TRUE);
				return "ok";
			} else {
				return "Este requerimento já foi quitado financeiramente, portanto não pode ser alterado";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorCpf() {
		try {
			String campoConsulta = getRequerimentoVO().getPessoa().getCPF();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			TipoPessoa tipoPessoa = TipoPessoa.getEnum(pessoa.getTipoPessoa());
			if (tipoPessoa == null) {
				pessoa.setCandidato(Boolean.TRUE);
			}
			getRequerimentoVO().setPessoa(pessoa);
			getRequerimentoVO().getMatricula().setMatricula("");
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
			if (pessoa.getCodigo().equals(0)) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getRequerimentoVO().getPessoa().setNome("");
			getRequerimentoVO().getPessoa().setCodigo(0);
		}
	}

	public String registrarRecebimentoDoc() {
		try {
			requerimentoVO.setResponsavelRecebimentoDocRequerido(getUsuarioLogadoClone());
			requerimentoVO.setDataRecebimentoDocRequerido(new Date());
			requerimentoVO.setResponsavel(getUsuarioLogadoClone());
			getFacadeFactory().getRequerimentoFacade().registrarRecebimentoDoc(requerimentoVO, getUsuarioLogado(),  getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_doc_recebido");
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		}
	}

	public void emitirBoleto() {
		try {
			if (getRequerimentoVO().getSituacaoFinanceira().equals("PG")) {
				setMensagemID("msg_requerimento_requerimentoJaQuitadaFinanceiramente");
				setImprimir(false);
			} else if (!getRequerimentoVO().getValor().equals(0.0)) {
				if (getRequerimentoVO().getCodigo().intValue() == 0) {
					String t = gravarDuranteEmissaoBoleto();
					if (t.equals("ok")) {
						setImprimir(true);
					} else {
						setImprimir(false);
						setMensagemDetalhada("msg_erro", t);
						return;
					}
				} else if (!getRequerimentoVO().getContaReceber().equals(0)) {
					setMensagemID("msg_inscricao_emitirBoletoPagamento");
					setImprimir(true);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setImprimir(false);
		}
	}

	public String getBoleto() {
		if (getImprimir()) {
			return "abrirPopup('BoletoBancarioSV?codigoContaReceber=" + requerimentoVO.getContaReceber() + "&titulo=requerimento', 'boletoRequerimento', 780, 585)";
		}
		return "";
	}

	public String receberBoleto() {
//		try {
//			if (getRequerimentoVO().getSituacaoFinanceira().equals("PE")) {
//				getFacadeFactory().getRequerimentoFacade().receberBoleto(requerimentoVO, getUsuarioLogado(),  getConfiguracaoGeralPadraoSistema());
//				requerimentoVO.setSituacao("EX");
//				ContaReceberVO contaReceberVO;
//				contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(requerimentoVO.getContaReceber(), false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado());
//				NegociacaoRecebimentoVO negociacaoRecebimentoVO;
//				// ConfiguracaoFinanceiroVO conf =
//				// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS,
//				// getUsuarioLogado(), null);
//				ConfiguracaoFinanceiroVO conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getCodigo());
//				negociacaoRecebimentoVO = contaReceberVO.gerarNegociacaoRecebimentoVOPreenchido(getUsuarioLogado(), contaReceberVO.getPessoa(), contaReceberVO.getResponsavelFinanceiro(), contaReceberVO.getFuncionario(), contaReceberVO.getConvenio().getParceiro(), conf);
//				return navegarPara(NegociacaoRecebimentoControle.class.getSimpleName(), "setNegociacaoRecebimentoVOPreenchido", "negociar", negociacaoRecebimentoVO);
//			} else {
//				setMensagemID("msg_requerimento_requerimentoJaQuitadaFinanceiramente");
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
		return "";
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * RequerimentoCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Consultar requerimento", "Consultando");
			super.consultar();
			List objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorCodigo(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), true, getSituacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				// Date valorData =
				// Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorData(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), getSituacao(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeTipoRequerimento")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorNomeTipoRequerimento(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), getSituacao(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			// if (getControleConsulta().getCampoConsulta().equals("situacao"))
			// {
			// objs =
			// getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorSituacaoRequerimento(getControleConsulta().getValorConsulta(),
			// this.getUnidadeEnsinoLogado().getCodigo(), true,
			// getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			// }
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), getSituacao(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			if (getControleConsulta().getCampoConsulta().equals("cpfPessoa")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorCpfPessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), getSituacao(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), getSituacao(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Consultar requerimento", "Consultando");
			persistirLayoutPadraoQuantidade(String.valueOf(getQtdRegistrosPorPagina()));
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
		}
	}

	public void consultarRequerimentosPendentesMenu() {
		try {
			List objs = new ArrayList<>(0);
			objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPendenteUsuarioMenu(getUsuarioLogado().getPessoa().getCodigo(), getConfiguracaoGeralPadraoSistema());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public String consultarVisaoAluno() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()){
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Consultar requerimento", "Consultar Visão Aluno");
			super.consultar();			
			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAluno != null) {
				getListaConsulta().clear();
				visaoAluno.inicializarMenuRequerimento();
				getRequerimentoVO().getMatricula().setMatricula(visaoAluno.getMatricula().getMatricula());
				List objs = new ArrayList<>(0);
				setNovoRegistro(false);
				getRequerimentoVO().setEdicao(false);
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorCodigoAluno(new Integer(valorInt), getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				realizarConsultaRequerimentoVisaoPai(objs);
				setListaConsulta(objs);
				montarListaConsultaAbertos();
				montarListaConsultaFinalizados();
//				verificarQuantidadeDiasMaximoAcesso();
				validarApresentacaoBotaoImprimirVisaoAluno(getListaConsultaFinalizados());
//				if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPermitiAlunoPreMatriculaSolicitarRequerimento()) {
//					MatriculaPeriodoVO matPer = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(visaoAluno.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado());
//					if (matPer != null && !matPer.getCodigo().equals(0) && !matPer.getSituacao().equals("")) {
//						if (matPer.isPreMatricula()) {
//							setDentroPrazo(Boolean.FALSE);
//							setMensagemID("");
//							setMensagemDetalhada("A situação de sua matrícula está definida como pré-matricula, portanto não é permitido solicitar um requerimento!");
//						}else{
//							setMensagemID("msg_dados_consultados");
//						}
//					}else{
//						setMensagemID("msg_dados_consultados");
//					}
//					getRequerimentoVO().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
//				}else{
				if(objs.isEmpty()) {
					if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
						Object disciplina = context().getExternalContext().getSessionMap().get("disciplinaReposicao");			
						if(disciplina == null) {
							novoVisaoAluno();
							}
						}
					
				}else{
					setMensagemID("msg_dados_consultados");
				}
//				}
				inicializarRequerimentoApartirMinhaNotas();
				}			
			}
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAluno.xhtml");
	}
	

	public void realizarConsultaRequerimentoVisaoPai(List<RequerimentoVO> objs) throws Exception {
		if (getUsuarioLogado().getIsApresentarVisaoPais()) {
			List<RequerimentoVO> listaRequerimentos = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoPorCodigoPai(getUsuarioLogado().getPessoa().getCodigo(), getRequerimentoVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			primeiroFor: for (RequerimentoVO obj : listaRequerimentos) {
				Boolean existeCodigo = false;
				for (RequerimentoVO objExistente : objs) {
					if (objExistente.getCodigo().equals(obj.getCodigo())) {
						continue primeiroFor;
					}
					existeCodigo = true;
				}
				if (existeCodigo) {
					objs.add(obj);
				}
			}
		}
	}

	public List montarListaConsultaAbertos() {
		getListaConsultaAbertos().clear();
		Iterator i = getListaConsulta().iterator();
		while (i.hasNext()) {
			RequerimentoVO obj = (RequerimentoVO) i.next();
			if ((!obj.getSituacao().equals("FD") && !obj.getSituacao().equals("FI")) || (obj.getSituacaoFinanceira().equals("PE"))) {
				getListaConsultaAbertos().add(obj);
			}
		}
		return getListaConsultaAbertos();
	}

	public List montarListaConsultaFinalizados() {
		getListaConsultaFinalizados().clear();
		Iterator i = getListaConsulta().iterator();
		while (i.hasNext()) {
			RequerimentoVO obj = (RequerimentoVO) i.next();
			if ((obj.getSituacao().equals("FD") || obj.getSituacao().equals("FI")) && !obj.getSituacaoFinanceira().equals("PE")) {
				getListaConsultaFinalizados().add(obj);
			}
		}
		return getListaConsultaFinalizados();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>RequerimentoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			setAbaAtiva("richTab");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Excluir Requerimento", "Excluindo");
			if (getIsPermiteExcluir()) {
				getFacadeFactory().getRequerimentoFacade().excluir(requerimentoVO,  getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), true);
				if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
					novoVisaoAluno();
				}else {
					novo();
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Excluir requerimento", "Excluindo");
				setMensagemID("msg_dados_excluidos");
			} else {
				setMensagemDetalhada("msg_erro", "Este requerimento não pode ser excluido");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAluno");
		}else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorForm");
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorForm");
		}else {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoForm");
		}
	}

	public void consultarAluno(Integer unidadeEnsino) {
		try {
//			if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
//			}
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), unidadeEnsino, false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), unidadeEnsino, false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), unidadeEnsino, false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAluno() {
		consultarAluno(getRequerimentoVO().getUnidadeEnsino().getCodigo());
	}
	
	public void consultarAlunoMapaReposicao() {
		consultarAluno(getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo());
	}

	public void consultarRequisitante() {
		try {
			List objs = new ArrayList<>(0);
			if (getValorConsultaRequisitante().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaRequisitante().equals("nome")) {
				// objs =
				// getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaRequisitante(),
				// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorNome(getValorConsultaRequisitante(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			if (getCampoConsultaRequisitante().equals("CPF")) {
				// objs =
				// getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaRequisitante(),
				// "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorCPF(getValorConsultaRequisitante(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisitante().equals("RG")) {
				// objs =
				// getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaRequisitante(),
				// "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorRG(getValorConsultaRequisitante(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaRequisitante(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsultaRequisitante(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void limparMatricula() {
		getRequerimentoVO().setMatricula(null);
		getRequerimentoVO().setPessoa(null);
		getRequerimentoVO().setCpfRequerente("");
		getRequerimentoVO().setNomeRequerente("");
		getListaSelectItemDisciplina().clear();
		getListaTurmaIncluir().clear();
		getRequerimentoVO().setDisciplinaPorEquivalencia(false);
		getRequerimentoVO().setDisciplina(null);
		getRequerimentoVO().setTurmaReposicao(null);
		selecionarTipoRequerimento(true);
	}

	/**
	 * Método usado para validar se existe Unidade de Ensino logada
	 * 
	 * @throws Exception
	 */
	public void verificarExisteUnidadeEnsinoLogado() throws Exception {
		if (getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo() != 0) {
			getRequerimentoVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getRequerimentoConsVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public String getMascaraConsultaRequisitante() {
		if (getCampoConsultaRequisitante().equals("CPF")) {
			return "return mascara(this.form,'formRequerente:valorConsultaRequerente','999.999.999-99',event);";
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaComboRequisitante() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public void selecionarAluno(RequerimentoVO requerimentoVO, boolean montarListaTipoRequerimentoDisciplina) {
		try {
			setMensagemDetalhada("");
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(obj.getCurso().getConfiguracaoAcademico())) {
				obj.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			}
			
			if(obj.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			
			requerimentoVO.setMatricula(obj);
			MatriculaPeriodoVO matriculaPeriodoVO = null;
			String anoAtual = obj.getCurso().getPeriodicidade().equals("SE") || obj.getCurso().getPeriodicidade().equals("AN") ? Uteis.getAnoDataAtual4Digitos() : "";
			String semestreAtual = obj.getCurso().getPeriodicidade().equals("SE")? Uteis.getSemestreAtual() : "";
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAnoSemExcecao(obj.getMatricula(), semestreAtual, anoAtual, false, getUsuarioLogado());
			if (matriculaPeriodoVO.getCodigo().equals(0)) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
			}
			requerimentoVO.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
			requerimentoVO.getTurma().setCodigo(matriculaPeriodoVO.getTurma().getCodigo());
			requerimentoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);
			getFacadeFactory().getPessoaFacade().carregarDados(obj.getAluno(), getUsuarioLogado());
			requerimentoVO.setPessoa(obj.getAluno());
			getListaSelectItemDisciplina().clear();
			getListaTurmaIncluir().clear();
			requerimentoVO.setDisciplinaPorEquivalencia(false);
			requerimentoVO.setDisciplina(null);
			requerimentoVO.setTurmaReposicao(null);
			selecionarTipoRequerimento(false);
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(requerimentoVO, getUsuarioLogado());
			if (montarListaTipoRequerimentoDisciplina) {
				montarListaSelectItemTipoRequerimento();
				montarListaSelectItemDisciplina(requerimentoVO.getCurso().getConfiguracaoAcademico());
			}
			montarListaSelectItemGrupoFacilitador();
			montarListaSelectItemCidTipoRequerimento();
			requerimentoVO.setNomeRequerente(new String(requerimentoVO.getMatricula().getAluno().getNome()));
			requerimentoVO.setCpfRequerente(new String(requerimentoVO.getMatricula().getAluno().getCPF()));
		} catch (Exception e) {
			requerimentoVO.setMatricula(null);
			requerimentoVO.setPessoa(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() {
		selecionarAluno(getRequerimentoVO(), true);
	}
	
	public void selecionarAlunoMapaReposicao() {
		selecionarAluno(getRequerimentoVOMapaReposicao(), false);
	}

	public void selecionarRequisitante() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("requisitanteItens");
			obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			TipoPessoa tipoPessoa = TipoPessoa.getEnum(obj.getTipoPessoa());
			if (tipoPessoa == null) {
				obj.setRequisitante(Boolean.TRUE);
			}
			this.getRequerimentoVO().setPessoa(obj);
			this.getRequerimentoConsVO().setPessoa(obj);
			getRequerimentoVO().getMatricula().setMatricula("");
			if (getNomeTelaAtual().contains("requerimentoForm")) {
				getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
			} else {
				consultarDadosGrafico();
			}
			setAutocompleteValorRequisitante(obj.getNome());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemDescontoRequerimento() {
		setListaSelectItemDescontoRequerimento(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, "valor", "simbolo", false));
	}

	// public void irPaginaInicial() throws Exception {
	// // this.consultar();
	// this.consultarTotalRegistro();
	// }

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

	public void consultarCursoCons(Integer unidadeEnsino) {
		try {
			List<CursoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void consultarCursoCons() {
		try {
			List<CursoVO> objs = new ArrayList<>(0);
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, unidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), unidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void consultarCursoConsMapaReposicao() {
		consultarCursoCons(getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo());
	}

	public void consultarCurso() {
		try {
			getListaConsultaCurso().clear();
			Integer unidadeEnsino = getRequerimentoVO().getUnidadeEnsino().getCodigo();
			if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
				throw new Exception("O campo UNIDADE ENSINO (Requerimento) deve ser informado.");				
			}
			
			if (getCampoConsultaCurso().equals("codigo") && (getValorConsultaCurso().trim() != null || !getValorConsultaCurso().trim().isEmpty())) {
				Uteis.validarSomenteNumeroString(getValorConsultaCurso().trim());
			}
			if (TiposRequerimento.TRANSF_INTERNA.getValor().equals(getRequerimentoVO().getTipoRequerimento().getTipo())) {
				unidadeEnsino = getRequerimentoVO().getUnidadeEnsinoTransferenciaInternaVO().getCodigo();
				if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
					throw new Exception("O campo UNIDADE DE ENSINO TRANSFERÊNCIA INTERNA (Requerimento) deve ser informado.");
				}
				if (getCampoConsultaCurso().equals("codigo")) {
					if (getValorConsultaCurso().equals("")) {
						setValorConsultaCurso("0");
					}
				}
				 List<UnidadeEnsinoCursoVO> lista = montarListaCursoVindaTipoRequerimentoCursoTransferenciaInternaCursoVO(unidadeEnsino , getCampoConsultaCurso(),getValorConsultaCurso() );				
				 if(Uteis.isAtributoPreenchido(lista)) {					 
					setListaConsultaCurso(lista);
					setMensagemID("msg_dados_consultados");
	                return ;
			
				 }			    
			}else {
				if (getCampoConsultaCurso().equals("codigo")) {
					if (getValorConsultaCurso().equals("")) {
						setValorConsultaCurso("0");
					}
					setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(Integer.parseInt(getValorConsultaCurso()), unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				}
				if (getCampoConsultaCurso().equals("nome")) {
					setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), unidadeEnsino, false, "", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoCursoVO> montarListaCursoVindaTipoRequerimentoCursoTransferenciaInternaCursoVO(Integer unidadeEnsino,  String campoConsultaCurso, String valorConsultaCurso) throws Exception {
		List<UnidadeEnsinoCursoVO> listaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		Optional<TipoRequerimentoCursoVO> optntipoRequerimentoCursoVO = getRequerimentoVO().getTipoRequerimento().getTipoRequerimentoCursoVOs().stream().filter( obj-> obj.getCursoVO().getCodigo().equals(getRequerimentoVO().getMatricula().getCurso().getCodigo())).findFirst();
		if(optntipoRequerimentoCursoVO.isPresent()) {
			TipoRequerimentoCursoVO tipoRequerimentoCursoVO =  optntipoRequerimentoCursoVO.get();
			if(tipoRequerimentoCursoVO.getListaTipoRequerimentoTransferenciaCursoVOs().isEmpty()) {
			  return listaCurso ;
			 } 
			List<CursoVO>  lista = tipoRequerimentoCursoVO.getListaTipoRequerimentoTransferenciaCursoVOs().stream().map(TipoRequerimentoCursoTransferenciaInternaCursoVO::getCursoVO).collect(Collectors.toList()) ;
			if(campoConsultaCurso.equals("codigo")) {				
			    lista.removeIf(c -> !c.getCodigo().equals(Integer.parseInt(valorConsultaCurso)));
			}else if(!valorConsultaCurso.equals("")){		
				 lista.removeIf(c -> !Uteis.removerAcentos(c.getNome()).toLowerCase().contains(Uteis.removerAcentos(valorConsultaCurso.replaceAll("%", "")).toLowerCase()));
			}
			if(lista != null && !lista.isEmpty() ){				
				listaCurso =  (getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursosUnidadeEnsino(lista, unidadeEnsino, "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
		}
		return listaCurso ;
		

		
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String consultarRequerimentosMapa() {
		try {
			setListaRequerimentoReposicao(new ArrayList<>());
			if (getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo().intValue() == 0) {
				throw new Exception("Unidade de Ensino deve ser informada.");
			}
			if (getRequerimentoVOMapaReposicao().getTipoRequerimento().getCodigo().intValue() == 0) {
				throw new Exception("Tipo Requerimento deve ser informado.");
			}
			setListaRequerimentoReposicao(getFacadeFactory().getRequerimentoFacade().consultarRequerimentosMapaReposicao(getRequerimentoVOMapaReposicao(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getRequerimentoVOMapaReposicao().getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo(), getOrdernarPor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaReposicao.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaReposicao.xhtml");
		}

	}

	public void montarConsultaCurso() {
		setListaConsultaCurso(new ArrayList<>(0));
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItem");
			if (TiposRequerimento.TRANSF_INTERNA.getValor().equals(getRequerimentoVO().getTipoRequerimento().getTipo())) {
				getRequerimentoVO().setCursoTransferenciaInternaVO(unidadeEnsinoCurso.getCurso());
				getRequerimentoVO().setTurnoTransferenciaInternaVO(unidadeEnsinoCurso.getTurno());
			} else {
				getRequerimentoVO().setCurso(unidadeEnsinoCurso.getCurso());
				getRequerimentoVO().setTurno(unidadeEnsinoCurso.getTurno());
			}
			setListaConsultaCurso(new ArrayList<>());
			setValorConsultaCurso("");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCursoCons() throws Exception {
		try {
			setCursoVO((CursoVO) context().getExternalContext().getRequestMap().get("cursoItem"));
			setAutocompleteValorCurso(getCursoVO().getNome() + " (" + getCursoVO().getCodigo() + ")");
			setListaConsultaCurso(new ArrayList<>());
			setValorConsultaCurso("");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso(null);
			getRequerimentoVO().setCurso(new CursoVO());
			getRequerimentoVO().setTurno(new TurnoVO());
			getRequerimentoVO().setCursoTransferenciaInternaVO(new CursoVO());
			getRequerimentoVO().setTurnoTransferenciaInternaVO(new TurnoVO());
			setAutocompleteValorCurso("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCursoCons() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso(null);
			setCursoVO(null);
			setTurnoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma(Integer unidadeEnsino) {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurma() {
		consultarTurma(getRequerimentoVO().getUnidadeEnsino().getCodigo());
	}
	
	public void consultarTurmaMapaReposicao() {
		consultarTurma(getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo());
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getCursoVO().getCodigo(), getRequerimentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			if (getTurmaVO().getCodigo() == 0) {
				setTurmaVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			setTurmaVO(obj);
			setCursoVO(obj.getCurso());
			setAutocompleteValorTurma(obj.getIdentificadorTurma());
			consultarDadosGrafico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelPorCodigo() {
		consultarResponsavelPorCodigo(getValorAutoComplete(getAutocompleteValorResponsavel()));
		valorResponsavel = getAutocompleteValorResponsavel();
	}

	public void consultarResponsavelPorCodigo(int codigo) {
		try {
			UsuarioVO obj = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getRequerimentoConsVO().setResponsavel(obj);
			consultarDadosGrafico();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurmaPorCodigo() {
		consultarTurmaPorCodigo(getValorAutoComplete(getAutocompleteValorTurma()));
		valorTurma = getAutocompleteValorTurma();
	}

	public void selecionarCursoPorCodigo() {
		consultarCursoPorCodigo(getValorAutoComplete(getAutocompleteValorCurso()));
	}

	public void consultarCursoPorCodigo(int codigo) {
		try {
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			setCursoVO(curso);
			if (Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getCurso().getCodigo().equals(curso.getCodigo())) {
				limparDadosTurma();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarRequisitantePorCodigo(int codigo) {
		try {
			PessoaVO obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			// TipoPessoa tipoPessoa = TipoPessoa.getEnum(obj.getTipoPessoa());
			// if (tipoPessoa == null) {
			// obj.setCandidato(Boolean.TRUE);
			// }
			this.getRequerimentoVO().setPessoa(obj);
			this.getRequerimentoConsVO().setPessoa(obj);
			getRequerimentoVO().getMatricula().setMatricula("");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarRequisitantePorCodigo() {
		consultarRequisitantePorCodigo(getValorAutoComplete(getAutocompleteValorRequisitante()));
		valorRequisitante = getAutocompleteValorRequisitante();
	}

	public void consultarTurmaPorCodigo(int codigo) {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			getListaConsultaTurma().clear();
			setValorConsultaTurma(null);
			setTurmaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
		setAutocompleteValorTurma("");
	}

	public void limparDadosCursoTurmaMatricula() {
		try {
			limparCurso();
			limparTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplina(Integer unidadeEnsino) {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(valorInt, unidadeEnsino, getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), unidadeEnsino, getCursoVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void consultarDisciplina() {
		consultarDisciplina(getRequerimentoVO().getUnidadeEnsino().getCodigo());
	}

	public void consultarDisciplinaMapaReposicao() {
		consultarDisciplina(getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo());
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getRequerimentoVO().setDisciplina(obj);
			getRequerimentoVOMapaReposicao().setDisciplina(obj);
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() {
		try {
			getRequerimentoVO().setDisciplina(null);
			getRequerimentoVOMapaReposicao().setDisciplina(null);
		} catch (Exception e) {
		}
	}

	public List getTipoOrdenacaoCombo() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("disciplina", "Disciplina"));
		itens.add(new SelectItem("data", "Data"));
		return itens;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacaoFinanceira</code>
	 */
	
	public List<SelectItem> listaSelectItemSituacaoFinanceiraRequerimento;
	public List<SelectItem> getListaSelectItemSituacaoFinanceiraRequerimento() throws Exception {
		if(listaSelectItemSituacaoFinanceiraRequerimento == null) {
			listaSelectItemSituacaoFinanceiraRequerimento = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem("", ""));
			listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem("PI", "Pago/Isento"));
			Hashtable<String, String> situacaoFinanceiraProtocolos = (Hashtable<String, String>) Dominios.getSituacaoFinanceiraProtocolo();
			Enumeration<String> keys = situacaoFinanceiraProtocolos.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) situacaoFinanceiraProtocolos.get(value);
				listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem(value, label));
			}
			listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem("SI", "Solicitação Isenção"));
			listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem("SID", "Solicitação Isenção Deferida"));
			listaSelectItemSituacaoFinanceiraRequerimento.add(new SelectItem("SII", "Solicitação Isenção Indeferida"));
		}
		return listaSelectItemSituacaoFinanceiraRequerimento;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	@Override
	public List getListaSelectItemSituacaoRequerimento() throws Exception {
		if (listaSelectItemSituacaoRequerimento == null) {
			listaSelectItemSituacaoRequerimento = new ArrayList<>(0);
		}
		return listaSelectItemSituacaoRequerimento;
	}

	public void setListaSelectItemSituacaoRequerimento(List listaSelectItemSituacaoRequerimento) {
		this.listaSelectItemSituacaoRequerimento = listaSelectItemSituacaoRequerimento;
	}

	public void montarListaSelectItemSituacaoRequerimento(String prm) throws Exception {
		try {
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem("", ""));
			Hashtable situacaoProtocolos = (Hashtable) Dominios.getSituacaoProtocolo();
			Enumeration keys = situacaoProtocolos.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) situacaoProtocolos.get(value);
				objs.add(new SelectItem(value, label));
			}
			objs.add(new SelectItem("PD", "Pendente"));
			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemSituacaoRequerimento(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>SituacaoRequerimento</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemSituacaoRequerimento() {
		try {
			montarListaSelectItemSituacaoRequerimento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			setMensagemDetalhada("");
//			if (getRequerimentoVO().getUnidadeEnsino().getCodigo() == 0) {
//				throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
//			}
			String campoConsulta = requerimentoVO.getMatricula().getMatricula();
			// MatriculaVO matricula =
			// getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaAtivaOuTrancada(campoConsulta,
			// this.getUnidadeEnsinoLogado().getCodigo(), false,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			//  getUsuarioLogado());
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(matricula) && Uteis.isAtributoPreenchido(matricula.getUnidadeEnsino())) {
				getRequerimentoVO().setUnidadeEnsino(matricula.getUnidadeEnsino());
			}			
			if (Uteis.isAtributoPreenchido(matricula.getCurso().getConfiguracaoAcademico())) {
				matricula.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(matricula.getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			}
			if(matricula.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			getFacadeFactory().getPessoaFacade().carregarDados(matricula.getAluno(), getUsuarioLogado());
			requerimentoVO.setMatricula(matricula);
			requerimentoVO.getMatricula().setAluno(matricula.getAluno());
			requerimentoVO.setPessoa(matricula.getAluno());
			requerimentoVO.getUnidadeEnsino().setCodigo(matricula.getUnidadeEnsino().getCodigo());

			MatriculaPeriodoVO matriculaPeriodoVO = null;
			String anoAtual = matricula.getCurso().getPeriodicidade().equals("SE") || matricula.getCurso().getPeriodicidade().equals("AN") ? Uteis.getAnoDataAtual4Digitos() : "";
			String semestreAtual = matricula.getCurso().getPeriodicidade().equals("SE")? Uteis.getSemestreAtual() : "";
			
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAnoSemExcecao(matricula.getMatricula(), semestreAtual, anoAtual, false, getUsuarioLogado());
			if (matriculaPeriodoVO.getCodigo().equals(0)) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
			}
			this.getRequerimentoVO().setMatriculaPeriodoVO(matriculaPeriodoVO);
			this.getRequerimentoVO().getTurma().setCodigo(matriculaPeriodoVO.getTurma().getCodigo());
			getListaSelectItemDisciplina().clear();
			getListaTurmaIncluir().clear();
			getRequerimentoVO().setDisciplinaPorEquivalencia(false);
			getRequerimentoVO().setDisciplina(null);
			getRequerimentoVO().setTurmaReposicao(null);
			selecionarTipoRequerimento(true);
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoRegrasCriacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
			this.setMatricula_valorApresentar(matricula.getAluno().getNome() + " (" + matricula.getCurso().getNome() + " - " + matricula.getTurno().getNome() + ")");
			if (getRequerimentoVO().getMatricula().getMatricula().equals("")) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
			montarListaSelectItemTipoRequerimento();
			montarListaSelectItemDisciplina(getRequerimentoVO().getCurso().getConfiguracaoAcademico());
			montarListaSelectItemGrupoFacilitador();
			requerimentoVO.setNomeRequerente(new String(matricula.getAluno().getNome()));
			requerimentoVO.setCpfRequerente(new String(matricula.getAluno().getCPF()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			requerimentoVO.setMatricula(null);
			requerimentoVO.setPessoa(null);
			requerimentoVO.setNumeroVia(null);			
			this.setMatricula_valorApresentar("");
		}
	}	

	

	public void atualizarDataFinalizacao() {
		try {
			String campoConsulta = requerimentoVO.getSituacao();
			if (campoConsulta.equals("FI")) {
				requerimentoVO.setDataFinalizacao(new Date());
			} else {
				requerimentoVO.setDataFinalizacao(null);
			}
		} catch (Exception e) {
			requerimentoVO.setDataFinalizacao(null);
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>TipoRequerimento</code> por meio de sua respectiva chave primária.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarTipoRequerimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = requerimentoVO.getTipoRequerimento().getCodigo();
			TipoRequerimentoVO tipoRequerimentoPrm = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			requerimentoVO.getTipoRequerimento().setNome(tipoRequerimentoPrm.getNome());
			this.setTipoRequerimento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			requerimentoVO.getTipoRequerimento().setNome("");
			requerimentoVO.getTipoRequerimento().setCodigo(0);
			this.setTipoRequerimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void montarListaSelectItemTipoRequerimento() {
		try {
			montarListaSelectItemTipoRequerimento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemTipoRequerimento(String prm) throws Exception {
		getListaSelectItemTipoRequerimento().clear();
		if (getRequerimentoVO().isNovoObj()) {
			getListaSelectItemTipoRequerimento().add(new SelectItem(0, ""));
			for (TipoRequerimentoVO tipoRequerimentoVO : consultarTipoRequerimentoComboBox()) {
				getListaSelectItemTipoRequerimento().add(new SelectItem(tipoRequerimentoVO.getCodigo(), tipoRequerimentoVO.getCodigo() + " - " + tipoRequerimentoVO.getNome()));
			}
		} else {
			getListaSelectItemTipoRequerimento().add(new SelectItem(getRequerimentoVO().getTipoRequerimento().getCodigo(), getRequerimentoVO().getTipoRequerimento().getCodigo() + " - " + getRequerimentoVO().getTipoRequerimento().getNome()));
		}
	}

	public void montarListaSelectItemTipoRequerimentoVisaoAluno() throws Exception {
		List<TipoRequerimentoVO> resultadoConsulta = null;
		try {
			getListaSelectItemTipoRequerimento().clear();
			resultadoConsulta = consultarTipoRequerimentoPorNomeVisaoAluno();
			if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				resultadoConsulta.addAll(getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoPais(Boolean.TRUE, getUnidadeEnsinoLogado().getCodigo(), getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			}
			getListaSelectItemTipoRequerimento().add(new SelectItem(0, ""));
			if (Uteis.isAtributoPreenchido(resultadoConsulta)) {
				getListaSelectItemTipoRequerimento().addAll(resultadoConsulta.stream().distinct().sorted(Comparator.comparing(TipoRequerimentoVO::getNome)).map(trvo -> new SelectItem(trvo.getCodigo(), trvo.getNome())).collect(Collectors.toList()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemTipoRequerimentoMapaReposicao() {
		List<TipoRequerimentoVO> resultadoConsulta = null;
		try {
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			if (!Uteis.isAtributoPreenchido(getRequerimentoVOMapaReposicao().getUnidadeEnsino())) {
				setListaSelectItemTipoRequerimentoMapaReposicao(objs);
			} else {
				resultadoConsulta = getFacadeFactory().getTipoRequerimentoFacade().consultarPorTipoRequerimento(TiposRequerimento.REPOSICAO.getValor(), "AT", getRequerimentoVOMapaReposicao().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  false, getUsuarioLogado());
				for (TipoRequerimentoVO obj : resultadoConsulta) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			setListaSelectItemTipoRequerimentoMapaReposicao(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemTipoRequerimentoReposicao() throws Exception {
		List resultadoConsulta = null;
		try {
			List objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			TipoRequerimentoVO tr = new TipoRequerimentoVO();
			tr = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(getConfiguracaoGeralPadraoSistema().getTipoRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  getUsuarioLogado());
			objs.add(new SelectItem(tr.getCodigo(), tr.getNome()));
			setListaSelectItemTipoRequerimento(objs);
			getRequerimentoVO().setTipoRequerimento(tr);
			atualizarValor(true);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}
	
	public void atualizarValor(Boolean forcarDefinicaoData) {
		try {
			getFacadeFactory().getRequerimentoFacade().atualizarValorRequerimento(getRequerimentoVO(), forcarDefinicaoData, getUsuarioLogado());
			inicializarTipoPessoaPorTipoRequerimento();
			montarListaSelectItemDisciplina(getRequerimentoVO().getCurso().getConfiguracaoAcademico());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//			requerimentoVO.setValor(0.0);
//			requerimentoVO.setTaxa(null);
		}
	}

	public List<TipoRequerimentoVO> consultarTipoRequerimentoPorNomeVisaoAluno() throws Exception {
		List<TipoRequerimentoVO> lista = new ArrayList<>(0);
		VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		Integer curso = 0;
		if (visaoAluno != null) {
			curso = visaoAluno.getMatricula().getCurso().getCodigo();
		}
		lista = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoAluno(Boolean.TRUE, "AT", getUnidadeEnsinoLogado().getCodigo(), curso, visaoAluno != null ? visaoAluno.getMatricula().getMatricula() : "",  Uteis.NIVELMONTARDADOS_COMBOBOX,  false, getUsuarioLogado());
		return lista;
	}

	public List consultarTipoRequerimentoPorNomeVisaoAluno2() throws Exception {
		List lista = new ArrayList<>(0);
		VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		Integer curso = 0;
		if (visaoAluno != null) {
			curso = visaoAluno.getMatricula().getCurso().getCodigo();
		}
		lista = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoAluno(Boolean.TRUE, "AT", getRequerimentoVO().getUnidadeEnsino().getCodigo(), curso, visaoAluno != null ? visaoAluno.getMatricula().getMatricula() : "",  Uteis.NIVELMONTARDADOS_COMBOBOX,  false, getUsuarioLogado());
		
		for (Iterator iterator = lista.iterator(); iterator.hasNext();) {
			TipoRequerimentoVO tipoRequerimentoVO = (TipoRequerimentoVO) iterator.next();
			            			
			if(Uteis.isAtributoPreenchido(tipoRequerimentoVO.getNivelEducacional())) {
				if(!tipoRequerimentoVO.getNivelEducacional().equals(visaoAluno.getMatricula().getCurso().getNivelEducacional())) {
					iterator.remove();
				}
			}
		}
		
		return lista;
	}

	public List<TipoRequerimentoVO> consultarTipoRequerimentoComboBox() throws Exception {
		List<TipoRequerimentoVO> lista = new ArrayList<>(0);
		realizarVerificacaoPermissaoConsultarRequerimentosProprioUsuario();	
		lista = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoComboBox(false, "AT", getRequerimentoVO().getUnidadeEnsino().getCodigo(), getRequerimentoVO().getMatricula().getCurso().getCodigo(), getPermiteAbrirRequerimentoForaDoPrazo(), getUsuarioLogado(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios());
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			setExisteTipoRequerimentoCadastradoProfessorCoordenador(!lista.isEmpty());
		}
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	// public void montarListaSelectItemUnidadeEnsino(String prm) throws
	// Exception {
	// if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0 &&
	// getRequerimentoVO().getNovoObj()) {
	// setListaSelectItemUnidadeEnsino(new ArrayList<>(0));
	// UnidadeEnsinoVO unidadeEnsino =
	// getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	// getListaSelectItemUnidadeEnsino().add(new
	// SelectItem(unidadeEnsino.getCodigo(), unidadeEnsino.getNome()));
	// getRequerimentoVO().getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
	// unidadeEnsino = null;
	// return;
	// }
	// List resultadoConsulta = consultarUnidadeEnsinoComboBox();
	// Iterator i = resultadoConsulta.iterator();
	// List objs = new ArrayList<>(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
	// objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
	// }
	// setListaSelectItemUnidadeEnsino(objs);
	// resultadoConsulta.clear();
	// resultadoConsulta = null;
	// }

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		getListaSelectItemUnidadeEnsino().clear();
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			if (resultadoConsulta.size() > 1) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				if (resultadoConsulta.size() == 1) {
					if(!Uteis.isAtributoPreenchido(getRequerimentoVO())) {
						getRequerimentoVO().setUnidadeEnsino(obj);
					}
				}
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
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

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoComboBox() throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemContaCorrente(String prm) throws Exception {

//		List resultadoConsulta = consultarContaCorrenteComboBox();
//		Iterator i = resultadoConsulta.iterator();
//		List objs = new ArrayList<>(0);
//		objs.add(new SelectItem(0, ""));
//		while (i.hasNext()) {
//			ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
//			objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString() + "-" + obj.getDigito().toString()));
//		}
//		setListaSelectItemContaCorrente(objs);
//		resultadoConsulta.clear();
//		resultadoConsulta = null;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemContaCorrente() {
		try {
			montarListaSelectItemContaCorrente("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */


	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("cpfPessoa")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	public Boolean getApresentarComboSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarComboSituacaoFinanceira() {
		if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
			return true;
		}
		return false;
	}

	public boolean isApresentarCampoMatricula() {
		if (TiposRequerimento.TRANSF_ENTRADA.getValor().equals(getRequerimentoVO().getTipoRequerimento().getTipo())) {
			return false;
		}
		return true;
	}

	public String getCadastrarNovoAluno() throws Exception {
		try {
			navegarPara(AlunoControle.class.getSimpleName(), "novo", "");
			executarMetodoControle(AlunoControle.class.getSimpleName(), "permitirIniciarInscricao");
			return "abrirPopup('alunoForm.xhtml', 'alunoForm', 780, 585);";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomePessoa", "Nome Requisitante"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nomeTipoRequerimento", "Tipo Requerimento"));
		// itens.add(new SelectItem("valor", "Valor"));
		// itens.add(new SelectItem("dataPrevistaFinalizacao",
		// "Data Prevista Finalização"));
		// itens.add(new SelectItem("dataFinalizacao", "Data Finalização"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
		itens.add(new SelectItem("cpfPessoa", "CPF Requisitante"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoConsultaComboVisaoAluno() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomeTipoRequerimento", "Tipo Requerimento"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		return itens;
	}

	public void limparDadosMatricula() {
		getRequerimentoVO().setMatricula(null);
		if (getRequerimentoVO().getSomenteAluno()) {
			getRequerimentoVO().setPessoa(null);
		}
		getListaSelectItemDisciplina().clear();
		getListaTurmaIncluir().clear();
		getRequerimentoVO().setDisciplinaPorEquivalencia(false);
		getRequerimentoVO().setDisciplina(null);
		getRequerimentoVO().setTurmaReposicao(null);
		montarListaSelectItemTipoRequerimento();
		selecionarTipoRequerimento(true);
		getRequerimentoVO().setNumeroVia(null);		
		montarListaSelectItemCidTipoRequerimento();
	}

	public void limparDadosPesssoa() {
		getRequerimentoVO().setPessoa(new PessoaVO());
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 * 
	 * @throws Exception
	 */
	public String inicializarConsultar() throws Exception {		 
		try {
			init();
			definirEscopoRequerimentoComBaseNaPermissaoAcesso();
			realizarVerificacaoPermissaoFiltroRequerimento();
			inicializarListasSelectItemTodosComboBox();
			getListaConsultaCurso().clear();
			consultarDadosGrafico();
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
			if(!getControleConsultaOtimizado().getListaConsulta().isEmpty() && getControleConsultaOtimizado().getListaConsulta().get(0) instanceof RequerimentoVO) {
				int x = 0;
				for(RequerimentoVO requerimentoVO:(List<RequerimentoVO>)getControleConsultaOtimizado().getListaConsulta()) {
					if(requerimentoVO.getCodigo().equals(getRequerimentoVO().getCodigo())) {
						requerimentoVO.setSituacao(getRequerimentoVO().getSituacao());
						requerimentoVO.setSituacaoFinanceira(getRequerimentoVO().getSituacaoFinanceira());
						break;
					}
					x++;
				}
			}
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void iniciarRequerimento() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getRequerimentoFacade().iniciarRequerimento(getRequerimentoVO(), getUsuarioLogado());
			this.enviarEmailAlunoSobreTramiteRequerimento();
			setMensagemID("msg_Requerimento_iniciadoComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void iniciarRequerimentoDepartamentoAtual() throws Exception {
		try {			
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			getFacadeFactory().getRequerimentoFacade().iniciarExecucaoRequerimentoNoDepartamento(getRequerimentoVO(), false, getUsuarioLogado());
			setAbrirModalQuestionarioRequerimentoHistorico(!getRequerimentoHistoricoVO().getQuestionarioJaRespondido() && getRequerimentoHistoricoVO().getQuestionario().getCodigo() > 0);
			this.enviarEmailAlunoSobreTramiteRequerimento();
			setMensagemID("msg_Requerimento_iniciadoComSucessoNoDpto");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void deferirLoteEspecifico() {
		ConsistirException consistirException =  new ConsistirException();
		if(!getPermiteDeferir()){
			consistirException.adicionarListaMensagemErro("O usuário não possui permissão para DEFERIR o REQUERIMENTO");
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}else {
		for(RequerimentoVO requerimentoVO: (List<RequerimentoVO>)getControleConsultaOtimizado().getListaConsulta()) {
			if(requerimentoVO.getSelecionado()) {
				try {
					deferirEspecifico(requerimentoVO, true);
				}catch (Exception e) {
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
			}
		}		
		consultarOtimizado();
		if(consistirException.existeErroListaMensagemErro()) {
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}
		}
	}
	public void deferirEspecifico(RequerimentoVO requerimentoVO, boolean retornarExcecao) throws Exception {
		String situacaoTmp = getRequerimentoVO().getSituacao();
		try {
			if(!retornarExcecao && !getPermiteDeferir()){
				throw new Exception("O usuário não possui permissão para DEFERIR o REQUERIMENTO");
			}
			setRequerimentoVO(requerimentoVO);
			getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			getRequerimentoVO().getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getRequerimentoVO().getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(), false, getUsuarioLogado()));
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
//			getFacadeFactory().getRequerimentoFacade().deferirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(),getUsuarioLogado());
			this.enviarEmailAlunoSobreTramiteRequerimento();
			if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() && getRequerimentoVO().getTipoRequerimento().getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital()) {
				getFacadeFactory().getRequerimentoFacade().gravarAtualizacaoMatricula(getRequerimentoVO(), getUsuarioLogado());
			}
			if(!retornarExcecao) {
				consultarOtimizado();
				setMensagemID("msg_requerimento_deferido", Uteis.SUCESSO);
			}
		}catch (Exception e) {
			getRequerimentoVO().setSituacao(situacaoTmp);
			if(retornarExcecao) {
				throw e;
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	public void deferirRequerimento() throws Exception {
		String situacaoTmp = getRequerimentoVO().getSituacao();
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			executarValidacaoSimulacaoVisaoCoordenador();
			if(!getPermiteDeferir()){
				throw new Exception("O usuário não possui permissão para DEFERIR o REQUERIMENTO");
			}
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			setAbrirModalQuestionarioRequerimentoHistorico(!getRequerimentoHistoricoVO().getQuestionarioJaRespondido() && getRequerimentoHistoricoVO().getQuestionario().getCodigo() > 0);
			if (!getAbrirModalQuestionarioRequerimentoHistorico()) {
				getFacadeFactory().getRequerimentoFacade().deferirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(),getUsuarioLogado());
				this.enviarEmailAlunoSobreTramiteRequerimento();
				if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() && getRequerimentoVO().getTipoRequerimento().getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital()) {
					getFacadeFactory().getRequerimentoFacade().gravarAtualizacaoMatricula(getRequerimentoVO(), getUsuarioLogado());
				}
				setMensagemID("msg_requerimento_deferido", Uteis.SUCESSO);
			}
			realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
			
			if(getRequerimentoVO().getTipoRequerimento().getAbrirOutroRequerimentoAoDeferirEsteTipoRequerimento()) {
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTipoRequerimentoAbrirDeferimento().getCodigo())) {
					msgNovoRequerimento = UteisJSF.internacionalizar("msg_Requerimento_novoRequerimento").replace("{0}", getRequerimentoVO().getTipoRequerimento().getTipoRequerimentoAbrirDeferimento().getNome());
				}
				else {
					msgNovoRequerimento = UteisJSF.internacionalizar("msg_Requerimento_novoRequerimento").replace("{0}", getRequerimentoVO().getTipoRequerimento().getNome());
				}
				setRequerimentoAntigo(getRequerimentoVO());
				setOncompleteModal("RichFaces.$('panelNovoRequerimento').show()");
			}
		} catch (Exception e) {
			getRequerimentoVO().setSituacao(situacaoTmp);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void indeferirLoteEspecifico() {
		ConsistirException consistirException =  new ConsistirException();
		if(!getPermiteIndeferir()){
			consistirException.adicionarListaMensagemErro("O usuário não possui permissão para INDEFERIR o REQUERIMENTO");
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);			
		}else {
		for(RequerimentoVO requerimentoVO: (List<RequerimentoVO>)getControleConsultaOtimizado().getListaConsulta()) {
			if(requerimentoVO.getSelecionado()) {
				try {
					indeferirEspecifico(requerimentoVO, true);
				}catch (Exception e) {
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
			}
		}		
		consultarOtimizado();
		if(consistirException.existeErroListaMensagemErro()) {
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}
		}
	}
	public void indeferirEspecifico(RequerimentoVO requerimentoVO, boolean retornarExcecao) throws Exception {
		String situacaoTmp = getRequerimentoVO().getSituacao();
		try {
			if(!retornarExcecao && !getPermiteIndeferir()){
				throw new Exception("O usuário não possui permissão para INDEFERIR o REQUERIMENTO");
			}
			setRequerimentoVO(requerimentoVO);
			getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
			getRequerimentoVO().getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getRequerimentoVO().getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(), false, getUsuarioLogado()));
			getFacadeFactory().getRequerimentoFacade().indeferirRequerimento(getRequerimentoVO(), true, false, getUsuarioLogado());			
			this.enviarEmailAlunoSobreTramiteRequerimento();
			if(!retornarExcecao) {
				consultarOtimizado();
				setMensagemID("msg_requerimento_indeferido", Uteis.SUCESSO);
			}
		}catch (Exception e) {
			getRequerimentoVO().setSituacao(situacaoTmp);
			if(retornarExcecao) {
				throw e;
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}

	public void indeferirRequerimento() throws Exception {
		String situacaotmp = getRequerimentoVO().getSituacao();
		RequerimentoHistoricoVO reqHistVO = requerimentoVO.consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
		reqHistVO.setNotaTCC(requerimentoHistoricoVO.getNotaTCC());
		getRequerimentoVO().adicionarRequerimentoHistoricoVOs(reqHistVO);
		try {
			if(!getPermiteIndeferir()){
				throw new Exception("O usuário não possui permissão para INDEFERIR o REQUERIMENTO");
			}
			getFacadeFactory().getRequerimentoFacade().indeferirRequerimento(getRequerimentoVO(), true, false, getUsuarioLogado());
			
			this.enviarEmailAlunoSobreTramiteRequerimento();
			setMensagemID("msg_requerimento_indeferido");
			verificarExisteSolicitacaoRequerimento();
			realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
		} catch (Exception e) {
			getRequerimentoVO().setSituacao(situacaotmp);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void visualizarObservacao() throws Exception {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("reqItens");
			setRequerimentoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarDeferirMapa() throws Exception {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("reqItens");
			setRequerimentoVO(obj);
			setListaDocumentosPendentes(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(getRequerimentoVO().getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void deferirRequerimentoMapa() {
		String situacao = getRequerimentoVO().getSituacao();
		try {
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			setAbrirModalQuestionarioRequerimentoHistorico(!getRequerimentoHistoricoVO().getQuestionarioJaRespondido() && getRequerimentoHistoricoVO().getQuestionario().getCodigo() > 0);
			if (!getAbrirModalQuestionarioRequerimentoHistorico()) {
				getRequerimentoVO().setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
				getRequerimentoVO().setDataFinalizacao(new Date());
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(getRequerimentoVO().getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", getRequerimentoConsVO().getMotivoDeferimento(),getRequerimentoVO(), getUsuarioLogado());
				setRequerimentoVO(new RequerimentoVO());
				consultarRequerimentosMapa();
				setMensagemDetalhada("Requerimento Deferido.");
			}
		} catch (Exception e) {
			getRequerimentoVO().setSituacao(situacao);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarLimpezaDadosAluno() {
		getRequerimentoVO().setMatricula(null);
		getRequerimentoVOMapaReposicao().setMatricula(null);
	}

	public void indeferirRequerimentoMapa() throws Exception {
		try {
			getFacadeFactory().getRequerimentoFacade().executarIndeferirRequerimento(getRequerimentoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setRequerimentoVO(new RequerimentoVO());
			consultarRequerimentosMapa();
			setMensagemDetalhada("Requerimento Indeferido.");
		} catch (Exception e) {
			getRequerimentoVO().setSituacao(situacao);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarExisteSolicitacaoRequerimento() throws Exception {
		if (getRequerimentoVO().getTipoRequerimento().getTipo().equals("CA") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("TR") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("RM") || getRequerimentoVO().getTipoRequerimento().getTipo().equals("TI")) {
			getFacadeFactory().getRequerimentoFacade().verificarExisteSolicitacaoRequerimento(getRequerimentoVO(), getUsuarioLogado());
		}
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("estado", "Estado"));
		return itens;
	}

	public void consultarCidade() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				if (getValorConsultaCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
		getRequerimentoVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	

	public void selecionarCidadeAproveitamentoDisciplina() {
		try {
			CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem");
			getRequerimentoDisciplinasAproveitadasVO().setCidade(obj);
			getListaConsultaCidade().clear();
			this.setValorConsultaCidade("");
			this.setCampoConsultaCidade("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	public void limparCidadeAproveitamentoDisciplina() {
		try {
			getRequerimentoDisciplinasAproveitadasVO().setCidade(new CidadeVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getRequerimentoVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getRequerimentoVO().getArquivoVO().setCpfRequerimento(getRequerimentoVO().getPessoa().getCPF());
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent, getRequerimentoVO().getArquivoVO(), getRequerimentoVO().getArquivoVO().getCpfRequerimento() + getRequerimentoVO().getTipoRequerimento().getNome(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REQUERIMENTOS_TMP, getUsuarioLogado());

			// setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getRequerimentoVO().getArquivoVO(),
			// PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
			// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
			// "foto_usuario.png"));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void adicionarArquivoRequerimento() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Iniciando Adicionar Arquivo Requerimento", "Uploading");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getRequerimentoVO().getArquivoVO().getNome()), "Arquivo nao Encontrado por favor verificar as extensões ou local do arquivo");
			getRequerimentoVO().getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getRequerimentoVO().getArquivoVO().setDataUpload(new Date());
			getRequerimentoVO().getArquivoVO().setManterDisponibilizacao(true);
			getRequerimentoVO().getArquivoVO().setDataDisponibilizacao(getRequerimentoVO().getArquivoVO().getDataUpload());
			getRequerimentoVO().getArquivoVO().setDataIndisponibilizacao(null);
			getRequerimentoVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getRequerimentoVO().getArquivoVO().setOrigem(OrigemArquivo.REQUERIMENTO.getValor());
			getRequerimentoVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
			getRequerimentoVO().setExcluirArquivo(false);
			if (!getRequerimentoVO().isNovoObj()) {
				if (!getRequerimentoVO().getArquivoVO().getNome().equals("")) {
					if (!getRequerimentoVO().getArquivoVO().getCodigo().equals(0)) {
						getFacadeFactory().getArquivoFacade().alterar(getRequerimentoVO().getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					} else {
						getFacadeFactory().getArquivoFacade().incluir(getRequerimentoVO().getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
						getFacadeFactory().getRequerimentoFacade().alterarCodigoArquivo(getRequerimentoVO(), getRequerimentoVO().getArquivoVO().getCodigo(), getUsuarioLogado());
					}
				}
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Adicionar Arquivo Requerimento", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCaminhoServidorDownload() {
		try {
			return "location.href='../DownloadSV'";
			// return
			// getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getRequerimentoVO().getArquivoVO(),
			// PastaBaseArquivoEnum.REQUERIMENTOS,
			// getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void realizarDownloadArquivoRequerimento() {
		context().getExternalContext().getSessionMap().put("nomeArquivo", getRequerimentoVO().getArquivoVO().getNome());
		if (getRequerimentoVO().getArquivoVO().getCpfRequerimento().trim().isEmpty()) {
			context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue());
		} else {
			if (!Uteis.isAtributoPreenchido(getRequerimentoVO().getCodigo())) {
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS_TMP.getValue() + File.separator + getRequerimentoVO().getArquivoVO().getCpfRequerimento());
			} else { 
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.REQUERIMENTOS.getValue() + File.separator + getRequerimentoVO().getArquivoVO().getCpfRequerimento());
			}
		}
	}

	public void removerArquivoRequerimento() throws Exception {
		try {
			if (getRequerimentoVO().getArquivoVO().getNome().trim().isEmpty() && getRequerimentoVO().getArquivoVO().getPastaBaseArquivoEnum() != null && getRequerimentoVO().getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.REQUERIMENTOS_TMP)) {
				getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(getRequerimentoVO().getArquivoVO(), getRequerimentoVO().getArquivoVO().getPastaBaseArquivo());
			}
			getRequerimentoVO().setExcluirArquivo(true);
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Iniciando Remover Arquivo Requerimento ", "Downloading - Removendo");
			getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(getRequerimentoVO().getArquivoVO(), false, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			getFacadeFactory().getRequerimentoFacade().alterarCodigoArquivo(getRequerimentoVO(), null, getUsuarioLogado());
			getRequerimentoVO().setArquivoVO(new ArquivoVO());
			getRequerimentoVO().getArquivoVO().setDescricao("");
			setExibirUpload(true);
			setExibirBotao(false);
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Remover Arquivo Requerimento ", "Downloading - Removendo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarPessoaSelecionada() {
		try {
			if (getRequerimentoVO().getPessoa().getCPF().equals("")) {
				throw new ConsistirException("É necessário informar a matrícula do aluno ou o cpf do requisitante para efetuar upload de arquivo.");
			}
			getRequerimentoVO().setArquivoVO(new ArquivoVO());
			setExibirUpload(true);
			setExibirBotao(false);
			setAbrirModalUpload("RichFaces.$('panelArquivoRequerimento').show()");
			setAbrirModalCapturarImagemWebCam("RichFaces.$('panelCapturarImagemWebCam').hide()");
		} catch (Exception e) {
			setAbrirModalUpload("");
			setAbrirModalCapturarImagemWebCam("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
	}

	public String getShowFotoCrop() {
		try {
			if (getRequerimentoVO().getArquivoVO().getNome() == null || getRequerimentoVO().getArquivoVO().getNome().trim().isEmpty()) {
				return "./resources/imagens/usuarioPadrao.jpg";
			}
			return getFacadeFactory().getArquivoHelper().renderizarImagemRequerimento(getRequerimentoVO().getArquivoVO(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getTagImageComFotoPadrao();
		}
	}

	public void recortarFoto() {
		try {
			getFacadeFactory().getArquivoHelper().recortarImagemRequerimento(getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY());
//			 setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getRequerimentoVO().getArquivoVO(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			setRemoverFoto((Boolean) false);
			// cancelar();
			// setOncompleteModal("Richfaces.hideModalPanel('panelArquivoRequerimento');");
		} catch (Exception ex) {
			// setOncompleteModal("Richfaces.showModalPanel('panelArquivoRequerimento');");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void upLoadImagem(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getRequerimentoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TMP, getUsuarioLogado());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getRequerimentoVO().getArquivoVO(), PastaBaseArquivoEnum.IMAGEM_TMP.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", true));
			setExibirBotao(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void renderizarUpload() {
		setExibirUpload(false);
	}

	public void cancelar() {
		try {
			removerArquivoRequerimento();
			setExibirUpload(true);
			setExibirBotao(false);
		} catch (Exception e) {
		}
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if (tipoRequerimentoVO == null) {
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}

	public String getCss() {
		if (getRequerimentoVO().getSomenteAluno() || getRequerimentoVO().getExisteMatricula()) {
			return "camposObrigatorios";
		}
		return "campos";
	}

	public String getCssPessoa() {
		if (getRequerimentoVO().getExisteMatricula()) {
			return "camposSomenteLeitura";
		}
		return "camposObrigatorios";
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		// montarListaSelectItemContaCorrente();
		montarListaSelectItemDescontoRequerimento();
		montarListaSelectItemTipoRequerimento();
		montarListaSelectItemSituacaoRequerimento();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemMotivoCancelamentoTrancamento();
		montarListaSelectItemCidTipoRequerimento();
	}

	public String getCampoConsultaCentroReceita() {
		if (campoConsultaCentroReceita == null) {
			campoConsultaCentroReceita = "";
		}
		return campoConsultaCentroReceita;
	}

	public void setCampoConsultaCentroReceita(String campoConsultaCentroReceita) {
		this.campoConsultaCentroReceita = campoConsultaCentroReceita;
	}

	public List getListaConsultaCentroReceita() {
		if (listaConsultaCentroReceita == null) {
			listaConsultaCentroReceita = new ArrayList<>(0);
		}
		return listaConsultaCentroReceita;
	}

	public void setListaConsultaCentroReceita(List listaConsultaCentroReceita) {
		this.listaConsultaCentroReceita = listaConsultaCentroReceita;
	}

	public String getValorConsultaCentroReceita() {
		if (valorConsultaCentroReceita == null) {
			valorConsultaCentroReceita = "";
		}
		return valorConsultaCentroReceita;
	}

	public void setValorConsultaCentroReceita(String valorConsultaCentroReceita) {
		this.valorConsultaCentroReceita = valorConsultaCentroReceita;
	}

//	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
//		if (configuracaoFinanceiroVO == null) {
//			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
//		}
//		return configuracaoFinanceiroVO;
//	}
//
//	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
//		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
//	}

	public List getListaSelectItemCentroReceita() {
		if (listaSelectItemCentroReceita == null) {
			listaSelectItemCentroReceita = new ArrayList<>(0);
		}
		return (listaSelectItemCentroReceita);
	}

	public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	public String getMatricula_Erro() {
		if (matricula_Erro == null) {
			matricula_Erro = "";
		}
		return matricula_Erro;
	}

	public void setMatricula_Erro(String matricula_Erro) {
		this.matricula_Erro = matricula_Erro;
	}

	public String getTipoRequerimento_Erro() {
		if (tipoRequerimento_Erro == null) {
			tipoRequerimento_Erro = "";
		}
		return tipoRequerimento_Erro;
	}

	public void setTipoRequerimento_Erro(String tipoRequerimento_Erro) {
		this.tipoRequerimento_Erro = tipoRequerimento_Erro;
	}

	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}

	public List<SelectItem> getListaSelectItemTipoRequerimento() {
		if (listaSelectItemTipoRequerimento == null) {
			listaSelectItemTipoRequerimento = new ArrayList<>(0);
		}
		return listaSelectItemTipoRequerimento;
	}

	public void setListaSelectItemTipoRequerimento(List<SelectItem> listaSelectItemTipoRequerimento) {
		this.listaSelectItemTipoRequerimento = listaSelectItemTipoRequerimento;
	}

	public String getMatricula_valorApresentar() {
		if (matricula_valorApresentar == null) {
			matricula_valorApresentar = "";
		}
		return matricula_valorApresentar;
	}

	public void setMatricula_valorApresentar(String matricula_valorApresentar) {
		this.matricula_valorApresentar = matricula_valorApresentar;
	}

	public String getDepartamentoResponsavel_Erro() {
		if (departamentoResponsavel_Erro == null) {
			departamentoResponsavel_Erro = "";
		}
		return departamentoResponsavel_Erro;
	}

	public void setDepartamentoResponsavel_Erro(String departamentoResponsavel_Erro) {
		this.departamentoResponsavel_Erro = departamentoResponsavel_Erro;
	}

	public List getListaSelectItemMatricula() {
		if (listaSelectItemMatricula == null) {
			listaSelectItemMatricula = new ArrayList<>(0);
		}
		return listaSelectItemMatricula;
	}

	public void setListaSelectItemMatricula(List listaSelectItemMatricula) {
		this.listaSelectItemMatricula = listaSelectItemMatricula;
	}

	public Boolean getImprimir() {
		if (imprimir == null) {
			imprimir = Boolean.FALSE;
		}
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getCampoConsultaRequisitante() {
		if (campoConsultaRequisitante == null) {
			campoConsultaRequisitante = "";
		}
		return campoConsultaRequisitante;
	}

	public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
		this.campoConsultaRequisitante = campoConsultaRequisitante;
	}

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List getListaConsultaRequisitante() {
		if (listaConsultaRequisitante == null) {
			listaConsultaRequisitante = new ArrayList<>(0);
		}
		return listaConsultaRequisitante;
	}

	public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
		this.listaConsultaRequisitante = listaConsultaRequisitante;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getValorConsultaRequisitante() {
		if (valorConsultaRequisitante == null) {
			valorConsultaRequisitante = "";
		}
		return valorConsultaRequisitante;
	}

	public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
		this.valorConsultaRequisitante = valorConsultaRequisitante;
	}

	/**
	 * @return the estatisticaRequerimentoVO
	 */
	public EstatisticaRequerimentoVO getEstatisticaRequerimentoVO() {
		if (estatisticaRequerimentoVO == null) {
			estatisticaRequerimentoVO = new EstatisticaRequerimentoVO();
		}
		return estatisticaRequerimentoVO;
	}

	/**
	 * @param estatisticaRequerimentoVO
	 *            the estatisticaRequerimentoVO to set
	 */
	public void setEstatisticaRequerimentoVO(EstatisticaRequerimentoVO estatisticaRequerimentoVO) {
		this.estatisticaRequerimentoVO = estatisticaRequerimentoVO;
	}

	public boolean getIsExisteUnidadeEnsinoLogada() throws Exception {
		if (getUnidadeEnsinoLogado().getCodigo() != null && getUnidadeEnsinoLogado().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public boolean getIsPermiteExcluir() throws Exception {
		if( getPermiteExcluir() 
			&& (!getRequerimentoVO().getSituacaoFinanceira().equals("PE") && !getRequerimentoVO().getSituacaoFinanceira().equals("AP")) 
			&& (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador())) {
			return false;
		}
		if (getPermiteExcluir() && (!getRequerimentoVO().getNovoObj()) 
				&& (((requerimentoVO.getValorTotalFinal().equals(0.0) && requerimentoVO.getSituacao().equals("EX")) 
				|| (requerimentoVO.getValorTotalFinal().equals(0.0) && requerimentoVO.getSituacao().equals("PE")) 
				|| (!requerimentoVO.getValorTotalFinal().equals(0.0) && (requerimentoVO.getSituacaoFinanceira().equals("AP") || requerimentoVO.getSituacaoFinanceira().equals("PE")  || requerimentoVO.getSituacaoFinanceira().equals("CA")) && (!getRequerimentoVO().getSituacao().equals("FI") && !getRequerimentoVO().getSituacao().equals("FD")))))
				&& (!getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao() || (getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao()
				&& (getRequerimentoVO().getDataInicioAula() == null 
				|| (Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(new Date()).compareTo(Uteis.getDataPassada(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(getRequerimentoVO().getDataInicioAula()), getRequerimentoVO().getTipoRequerimento().getQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao())) < 0 
				|| Uteis.getData(new Date()).equals(Uteis.getData(Uteis.getDataPassada(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(getRequerimentoVO().getDataInicioAula()), getRequerimentoVO().getTipoRequerimento().getQuantidadeDiasAntesAulaPermitirExclusaoRequerimentoReposicao())))))))) {
			return true;
		}
		return false;
	}

	public boolean getIsPermiteDeferir() {
		
		return getRequerimentoVO().getPodeSerDeferidoDepartamentoAtual() && !getIsRequerimentoPessoaLogada() && getPermiteDeferir();
	}

	public boolean getIsPermiteInDeferir() {
		return getRequerimentoVO().getPodeSerIndeferidoDepartamentoAtual() && !getIsRequerimentoPessoaLogada()  && getPermiteIndeferir();
	}

	public boolean getIsPendente() {
		if (!getRequerimentoVO().getNovoObj() && (getRequerimentoVO().getSituacaoFinanceira().equals("PG") || getRequerimentoVO().getSituacaoFinanceira().equals("IS")) && getRequerimentoVO().getSituacao().equals("PE") && (!getRequerimentoVO().getTipoRequerimento().getTipo().equals("CA") && !getRequerimentoVO().getTipoRequerimento().getTipo().equals("TR") && !getRequerimentoVO().getTipoRequerimento().getTipo().equals("RM") && !getRequerimentoVO().getTipoRequerimento().getTipo().equals("TI"))) {
			return true;
		}
		return false;
	}

	public boolean getIsApresentarCampoData() {
		return getRequerimentoVO().getSituacao().equals("data");
	}

	public List getListaSelectItemDescontoRequerimento() {
		if (listaSelectItemDescontoRequerimento == null) {
			listaSelectItemDescontoRequerimento = new ArrayList<>(0);
		}
		return listaSelectItemDescontoRequerimento;
	}

	public void setListaSelectItemDescontoRequerimento(List listaSelectItemDescontoRequerimento) {
		this.listaSelectItemDescontoRequerimento = listaSelectItemDescontoRequerimento;
	}

	public Boolean getNovoRegistro() {
		if (novoRegistro == null) {
			novoRegistro = false;
		}
		return novoRegistro;
	}

	public void setNovoRegistro(Boolean novoRegistro) {
		this.novoRegistro = novoRegistro;
	}

	public boolean getIsPossuiValor() {
		try {
			alterarTipoDesconto();
			if (getRequerimentoVO().getValorTotalFinal().compareTo(0.0) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
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

	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>(0);
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public String getAbrirModalUpload() {
		if (abrirModalUpload == null) {
			abrirModalUpload = "";
		}
		return abrirModalUpload;
	}

	public void setAbrirModalUpload(String abrirModalUpload) {
		this.abrirModalUpload = abrirModalUpload;
	}

	public boolean getIsPermitirInformarEnderecoEntrega() {
		return getRequerimentoVO().getTipoRequerimento().getPermitirInformarEnderecoEntrega();
	}

	public boolean getIsPermitirUploadArquivo() {
		return getRequerimentoVO().getTipoRequerimento().getPermitirUploadArquivo();
	}

	public boolean getIsNaoExisteArquivo() {
		return getIsPermitirUploadArquivo() && getRequerimentoVO().getArquivoVO().getNome().equals("");
	}

	public boolean getIsArquivoNaoSalvo() {
		return getIsPermitirUploadArquivo() && !getRequerimentoVO().getTipoRequerimento().getIsTipoAproveitamentoDisciplina() && getRequerimentoVO().getArquivoVO().getCodigo().equals(0) && !getRequerimentoVO().getNovoObj();
	}

	public String getGravadoComSucesso() {
		if (gravadoComSucesso == null) {
			gravadoComSucesso = "";
		}
		return gravadoComSucesso;
	}

	public void setGravadoComSucesso(String gravadoComSucesso) {
		this.gravadoComSucesso = gravadoComSucesso;
	}

	public String getMsgRequerimentoImagemGravadoComSucesso() {
		if (msgRequerimentoImagemGravadoComSucesso == null) {
			msgRequerimentoImagemGravadoComSucesso = "";
		}
		return msgRequerimentoImagemGravadoComSucesso;
	}

	public void setMsgRequerimentoImagemGravadoComSucesso(String msgRequerimentoImagemGravadoComSucesso) {
		this.msgRequerimentoImagemGravadoComSucesso = msgRequerimentoImagemGravadoComSucesso;
	}

	public String getMsgRequerimentoPossuiValor() {
		if (msgRequerimentoPossuiValor == null) {
			msgRequerimentoPossuiValor = "";
		}
		return msgRequerimentoPossuiValor;
	}

	public void setMsgRequerimentoPossuiValor(String msgRequerimentoPossuiValor) {
		this.msgRequerimentoPossuiValor = msgRequerimentoPossuiValor;
	}

	/**
	 * @return the situacao
	 */
	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	/**
	 * @param situacao
	 *            the situacao to set
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the situacaoFinanceira
	 */
	public String getSituacaoFinanceira() {
		if (situacaoFinanceira == null) {
			situacaoFinanceira = "";
		}
		return situacaoFinanceira;
	}

	/**
	 * @param situacaoFinanceira
	 *            the situacaoFinanceira to set
	 */
	public void setSituacaoFinanceira(String situacaoFinanceira) {
		this.situacaoFinanceira = situacaoFinanceira;
	}

	public List getListaConsultaAbertos() {
		if (listaConsultaAbertos == null) {
			listaConsultaAbertos = new ArrayList<>(0);
		}
		return listaConsultaAbertos;
	}

	public void setListaConsultaAbertos(List listaConsultaAbertos) {
		this.listaConsultaAbertos = listaConsultaAbertos;
	}

	public List getListaConsultaFinalizados() {
		if (listaConsultaFinalizados == null) {
			listaConsultaFinalizados = new ArrayList<>(0);
		}
		return listaConsultaFinalizados;
	}

	public void setListaConsultaFinalizados(List listaConsultaFinalizados) {
		this.listaConsultaFinalizados = listaConsultaFinalizados;
	}

	public void realizarImpressaoPDFEdicao() {

		try {
			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				setTipoLayout("LAYOUT_3");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Inicializando Geração de Relatório Requerimento", "Emitindo Relatório");
			getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, getUsuarioLogado());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().adicionarParametro("isPermitirInformarEnderecoEntrega", getIsPermitirInformarEnderecoEntrega());
			getSuperParametroRelVO().adicionarParametro("professorMinistrouAula", getProfessorMinistrouAula());
			setCaminhoRelatorio(getFacadeFactory().getRequerimentoFacade().realizarImpressaoComprovanteRequerimento(getRequerimentoVO(), getSuperParametroRelVO(), getUsuarioLogado(), getTipoLayout()));
			setFazerDownload(true);
			persistirLayoutPadrao(getTipoLayout());
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Finalizando Geração de Relatório Requerimento", "Emitindo Relatório");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void realizarImpressaoRequerimentoAnalitico() {
		List<RequerimentoVO> requerimentoVOs = new ArrayList<RequerimentoVO>(0);
		try {
			getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, getUsuarioLogado());
			getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(requerimentoVO.getUnidadeEnsino(), NivelMontarDados.BASICO, getUsuarioLogado());
//			if (Uteis.isAtributoPreenchido(requerimentoVO.getQuestionarioVO()) && !getRequerimentoVO().getNovoObj()) {
//				if (getRequerimentoVO().getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
//					getRequerimentoVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(requerimentoVO.getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//					getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimento(getRequerimentoVO().getCodigo().intValue(), getRequerimentoVO().getQuestionarioVO());
//				}
//			}
			for (RequerimentoHistoricoVO rhVO : requerimentoVO.getRequerimentoHistoricoVOs()) {
				if (Uteis.isAtributoPreenchido(rhVO.getQuestionario())) {
					if (rhVO.getQuestionario().getPerguntaQuestionarioVOs().isEmpty()) {
						rhVO.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(rhVO.getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimentoHistorico(rhVO.getRequerimento(), rhVO.getDepartamento().getCodigo(), rhVO.getOrdemExecucaoTramite(), rhVO.getQuestionario());
					}
				}
			}
			requerimentoVOs.add(requerimentoVO);
			getSuperParametroRelVO().setNomeDesignIreport(RequerimentoRel.getDesignIReportRelatorioAnalitico("RequerimentoAnalitico3Rel"));
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(RequerimentoRel.caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Requerimento Analítico");
			getSuperParametroRelVO().setListaObjetos(requerimentoVOs);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(RequerimentoRel.caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getNome());
			getSuperParametroRelVO().adicionarParametro("professorMinistrouAula", getProfessorMinistrouAula());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(requerimentoVOs);
		}
	}

	public void realizarImpressaoRequerimentoAnaliticoAreaConsulta() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
			obj.setNovoObj(Boolean.FALSE);
			setRequerimentoVO(obj);
			realizarImpressaoRequerimentoAnalitico();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public boolean getIsApresentarRealizarImpressaoRequerimentoAnalitico() {
		return !getRequerimentoVO().isNovoObj() && !getIsRequerimentoPessoaLogada() && getPermiteImprimirRequerimento();
	}

	public void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "RequerimentoRel", "designRequerimento", getUsuarioLogado());
	}

	public void verificarLayoutPadraoImpressao() {
		try {
			verificarLayoutPadrao();
			montarListaSelectItemImpressora();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("RequerimentoRel", "designRequerimento", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayout(layoutPadraoVO.getValor());
		}
		removerObjetoMemoria(layoutPadraoVO);
	}

	public void realizarImpressaoPDF() {
		List<RequerimentoVO> listaRequerimentoRelVO = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Inicializando Geração de Relatório Requerimento", "Emitindo Relatório");

			listaRequerimentoRelVO = new ArrayList<RequerimentoVO>(0);
			getFacadeFactory().getRequerimentoFacade().carregarDados(requerimentoVO, getUsuarioLogado());
			listaRequerimentoRelVO.add(requerimentoVO);
			getSuperParametroRelVO().setNomeDesignIreport(RequerimentoRel.getDesignIReportRelatorioAnalitico(getTipoLayout().equals("LAYOUT_1") ? "RequerimentoAnalitico2Rel" : getTipoLayout().equals("LAYOUT_3")  ? "RequerimentoAnalitico4Rel" : "RequerimentoAnaliticoRel"));
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(RequerimentoRel.caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Requerimento");
			getSuperParametroRelVO().setListaObjetos(listaRequerimentoRelVO);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(RequerimentoRel.caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getNome());
			getSuperParametroRelVO().setQuantidade(2);
			getSuperParametroRelVO().adicionarParametro("exigePagamento", getRequerimentoVO().getExigePagamento());
			getSuperParametroRelVO().adicionarParametro("isPermitirInformarEnderecoEntrega", getIsPermitirInformarEnderecoEntrega());
			getSuperParametroRelVO().adicionarParametro("enderecoCompletoUnidade", getFacadeFactory().getRequerimentoFacade().montaEnderecoRelatorioRequerimento(requerimentoVO.getCodigo(), false, getUsuarioLogado()));
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoRelControle", "Finalizando Geração de Relatório Requerimento", "Emitindo Relatório");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaRequerimentoRelVO);
		}
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the turmaVO
	 */
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	/**
	 * @param turmaVO
	 *            the turmaVO to set
	 */
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	/**
	 * @return the cursoVO
	 */
	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	/**
	 * @param cursoVO
	 *            the cursoVO to set
	 */
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the ordernarPor
	 */
	public String getOrdernarPor() {
		if (ordernarPor == null) {
			ordernarPor = "dataRequerimento";
		}
		return ordernarPor;
	}

	/**
	 * @param ordernarPor
	 *            the ordernarPor to set
	 */
	public void setOrdernarPor(String ordernarPor) {
		this.ordernarPor = ordernarPor;
	}

	/**
	 * @return the listaRequerimentoReposicao
	 */
	public List getListaRequerimentoReposicao() {
		if (listaRequerimentoReposicao == null) {
			listaRequerimentoReposicao = new ArrayList();
		}
		return listaRequerimentoReposicao;
	}

	/**
	 * @param listaRequerimentoReposicao
	 *            the listaRequerimentoReposicao to set
	 */
	public void setListaRequerimentoReposicao(List listaRequerimentoReposicao) {
		this.listaRequerimentoReposicao = listaRequerimentoReposicao;
	}

	/**
	 * @return the listaDocumentosPendentes
	 */
	public List getListaDocumentosPendentes() {
		if (listaDocumentosPendentes == null) {
			listaDocumentosPendentes = new ArrayList();
		}
		return listaDocumentosPendentes;
	}

	/**
	 * @param listaDocumentosPendentes
	 *            the listaDocumentosPendentes to set
	 */
	public void setListaDocumentosPendentes(List listaDocumentosPendentes) {
		this.listaDocumentosPendentes = listaDocumentosPendentes;
	}

	public boolean getApresentarBotaoConfirmarRecebimentoDoc() {
		if (!getRequerimentoVO().getResponsavelRecebimentoDocRequerido().getCodigo().equals(0)) {
			return false;
		}
		return true;
	}

	public String getLabelDepartamento() {
		if (getRequerimentoVO().getTipoRequerimento().getTramitaEntreDepartamentos()) {
			return "Departamento Atual (Trâmite)";
		}
		return "Departamento Responsável";
	}

	public boolean getApresentarEmissorBoleto() {
		if (!getRequerimentoVO().getResponsavelEmissaoBoleto().getCodigo().equals(0)) {
			return true;
		}
		return false;
	}

	public void prepararHistoricoTramiteDepartamentos() {
		try {
			// getRequerimentoVO().setRequerimentoHistoricoVOs(
			// getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(),
			// false, getUsuarioLogado()));
		} catch (Exception e) {
		}
	}

	public boolean getPodeSerIniciadoExecucaoDepartamentoAtual() {
		try {
			return getRequerimentoVO().getPodeSerIniciadoExecucaoDepartamentoAtual();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}

	public String getNomeProximoDepartamentoTramite() {
		String nomeDpto = this.getRequerimentoVO().getNomeProximoDepartamentoTramite();
		if (!nomeDpto.equals("")) {
			return "(" + nomeDpto + ")";
		}
		return "";
	}
	
	

	public RequerimentoDisciplinasAproveitadasVO getRequerimentoDisciplinasAproveitadasVO() {
		if (requerimentoDisciplinasAproveitadasVO == null) {
			requerimentoDisciplinasAproveitadasVO = new RequerimentoDisciplinasAproveitadasVO() ;
		}
		return requerimentoDisciplinasAproveitadasVO;
	}


	public void setRequerimentoDisciplinasAproveitadasVO(RequerimentoDisciplinasAproveitadasVO requerimentoDisciplinasAproveitadasVO) {
		this.requerimentoDisciplinasAproveitadasVO = requerimentoDisciplinasAproveitadasVO;
	}


	public List<SelectItem> getListaSelectItemDisciplinaAproveitamento() {
		if (listaSelectItemDisciplinaAproveitamento == null) {
			listaSelectItemDisciplinaAproveitamento = new ArrayList<>();
		}
		return listaSelectItemDisciplinaAproveitamento;
	}


	public void setListaSelectItemDisciplinaAproveitamento(List<SelectItem> listaSelectItemDisciplinaAproveitamento) {
		this.listaSelectItemDisciplinaAproveitamento = listaSelectItemDisciplinaAproveitamento;
	}
	
	public void montarListaSelectItemDisciplinaAproveitamento() {
		getListaSelectItemDisciplinaAproveitamento().clear();;
		List<DisciplinaVO> disciplinaVOs = null;
		try {
			disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasAptasAproveitamentoDisciplina(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getMatricula().getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone(), getRequerimentoVO().getMatricula().getCurso().getConfiguracaoAcademico(), getRequerimentoVO().getTipoRequerimento());
			if(Uteis.isAtributoPreenchido(getRequerimentoVO()) && !getRequerimentoVO().getListaRequerimentoDisciplinasAproveitadasVOs().isEmpty()) {
				for (RequerimentoDisciplinasAproveitadasVO rda : getRequerimentoVO().getListaRequerimentoDisciplinasAproveitadasVOs()) {
					if(disciplinaVOs.stream().noneMatch(pp->  pp.getCodigo().equals(rda.getDisciplina().getCodigo()))) {
						disciplinaVOs.add(rda.getDisciplina());	
					}
				}
			}
			getListaSelectItemDisciplinaAproveitamento().add(new SelectItem(0, " "));
			for (DisciplinaVO disciplinaVO : disciplinaVOs) {
				getListaSelectItemDisciplinaAproveitamento().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getNome()  + " - " + disciplinaVO.getCodigo()));				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		} finally {
			Uteis.liberarListaMemoria(disciplinaVOs);
		}
	}


	public List<RequerimentoDisciplinasAproveitadasVO> getListaRequerimentoDisciplinasAproveitadasVO() {
		if (listaRequerimentoDisciplinasAproveitadasVO == null) {
			listaRequerimentoDisciplinasAproveitadasVO = new ArrayList<>();
		}
		return listaRequerimentoDisciplinasAproveitadasVO;
	}


	public void setListaRequerimentoDisciplinasAproveitadasVO(List<RequerimentoDisciplinasAproveitadasVO> listaRequerimentoDisciplinasAproveitadasVO) {
		this.listaRequerimentoDisciplinasAproveitadasVO = listaRequerimentoDisciplinasAproveitadasVO;
	}


	/**
	 * @return the requerimentoHistoricoVO
	 */
	public RequerimentoHistoricoVO getRequerimentoHistoricoVO() {
		if (requerimentoHistoricoVO == null) {
			requerimentoHistoricoVO = new RequerimentoHistoricoVO();
		}
		return requerimentoHistoricoVO;
	}

	/**
	 * @param requerimentoHistoricoVO
	 *            the requerimentoHistoricoVO to set
	 */
	public void setRequerimentoHistoricoVO(RequerimentoHistoricoVO requerimentoHistoricoVO) {
		this.requerimentoHistoricoVO = requerimentoHistoricoVO;
	}

	public void prepararEnviarRequerimentoProximoDepartamento() {
		try {
			setMotivoRetornoDepartamento("");
			setRequerimentoHistoricoVO(new RequerimentoHistoricoVO());
			setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));
			setAbrirModalQuestionarioRequerimentoHistorico(getRequerimentoHistoricoVO().getQuestionario().getCodigo() > 0);
			setAbrirOpcaoInformarFuncionarioProximoTramite(getFacadeFactory().getRequerimentoFacade().realizarVerificarProximoTramiteExigeInformarFuncionarioResponsavel(getRequerimentoVO(), getFuncionarioProximoTramite(), getUsuarioLogado()));
			setFuncionarioTramiteCoordenadorEspecifico(getFacadeFactory().getRequerimentoFacade().realizarVerificarProximoTramiteExigeInformarCoordenadorCursoResponsavel(getRequerimentoVO(), getFuncionarioProximoTramite(), getUsuarioLogado()));
			if(getFuncionarioTramiteCoordenadorEspecifico()){
				consultarCoordenadorEspecificoProximoTramite();
			}
			setFinalizarEtapaRequerimento(true);
			if (!getAbrirModalQuestionarioRequerimentoHistorico()) {
				if (getRequerimentoHistoricoVO().getObservacaoDepartamento().equals("")) {
					getRequerimentoHistoricoVO().setObservacaoDepartamento("Enviado para próxima etapa execução requerimento");
				}
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String enviarRequerimentoDepartamentoAnterior() {
		try {
			setAbrirOpcaoInformarFuncionarioProximoTramite(false);
			setAcaoModalDepartamentoAnterior("");
			getFacadeFactory().getRequerimentoFacade().enviarRequerimentoDepartamentoAnterior(getRequerimentoVO(), getTipoRequerimentoDepartamentoAnterior(), getMotivoRetornoDepartamento(), getUsuarioLogado());
			getRequerimentoVO().setPodeSerIniciadoExecucaoDepartamentoAtual(null);
			getRequerimentoVO().setExisteQuestionarioDepartamentoAtual(null);
			setTipoRequerimentoDepartamentoRetornarVOs(getFacadeFactory().getRequerimentoFacade().consultarDepartamentoAnterioresPermiteRetornar(getRequerimentoVO()));
			this.enviarEmailAlunoSobreTramiteRequerimento();
			this.enviarEmailAlunoSobreTramiteRequerimentoAtendente();
			realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
			if (!getPermitirConsultarRequerimentoOutroConsultorResponsavel() && !getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades()) {
				if (!getListaConsulta().isEmpty() && getListaConsulta().get(0) instanceof RequerimentoVO) {
					int x = 0;
					for (RequerimentoVO req : (List<RequerimentoVO>) getListaConsulta()) {
						if (req.getCodigo().intValue() == getRequerimentoVO().getCodigo().intValue()) {
							getListaConsulta().remove(x);
							break;
						}
						x++;
					}
				}
				montarListaSelectItemDepartamento();
				limparMensagem();
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorCons.xhtml");
				} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorCons.xhtml");
				} else {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons.xhtml");
				}
			}
			inicializarListaSelectItemSituacaoRequerimentoDepartamento();
			if(getRequerimentoVO().getTipoRequerimento().getTipo().equals("TC")) {
				setPodeInserirNota(getRequerimentoVO().getDepartamentoPodeInserirNota());
			}
			setAcaoModalDepartamentoAnterior("RichFaces.$('panelDepartamentoAnterior').hide()");
			setMensagemID("msg_Requerimento_enviadoComSucesso");

			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String enviarRequerimentoProximoDepartamento() {
		try {
			getFacadeFactory().getRequerimentoFacade().enviarRequerimentoProximoDepartamento(getRequerimentoVO(), getRequerimentoHistoricoVO(), getAbrirOpcaoInformarFuncionarioProximoTramite(),  getFuncionarioTramiteCoordenadorEspecifico(), getFuncionarioProximoTramite(), getUsuarioLogado());
			getRequerimentoVO().setPodeSerIniciadoExecucaoDepartamentoAtual(null);
			setFinalizarEtapaRequerimento(false);
			getRequerimentoVO().setExisteQuestionarioDepartamentoAtual(null);
			setTipoRequerimentoDepartamentoRetornarVOs(getFacadeFactory().getRequerimentoFacade().consultarDepartamentoAnterioresPermiteRetornar(getRequerimentoVO()));
			this.enviarEmailAlunoSobreTramiteRequerimento();
			this.enviarEmailAlunoSobreTramiteRequerimentoAtendente();
			setAbrirOpcaoInformarFuncionarioProximoTramite(false);
			realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
			if (!getPermitirConsultarRequerimentoOutroConsultorResponsavel() && !getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades()) {
				if (getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				if (!getListaConsulta().isEmpty() && getListaConsulta().get(0) instanceof RequerimentoVO) {
					int x = 0;
					for (RequerimentoVO req : (List<RequerimentoVO>) getListaConsulta()) {
						if (req.getCodigo().intValue() == getRequerimentoVO().getCodigo().intValue()) {
							getListaConsulta().remove(x);
							break;
						}
						x++;
					}
				}
				}else {
					if (!getControleConsultaOtimizado().getListaConsulta().isEmpty() && getControleConsultaOtimizado().getListaConsulta().get(0) instanceof RequerimentoVO) {
						int x = 0;
						for (RequerimentoVO req : (List<RequerimentoVO>) getControleConsultaOtimizado().getListaConsulta()) {
							if (req.getCodigo().intValue() == getRequerimentoVO().getCodigo().intValue()) {
								getControleConsultaOtimizado().getListaConsulta().remove(x);
								break;
							}
							x++;							
						}
					}
				}
				montarListaSelectItemDepartamento();
				
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorCons.xhtml");
				} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorCons.xhtml");
				} else {
					return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons.xhtml");
				}
			}
			
			if(!getRequerimentoVO().getPodeSerEncaminhadoProximoDepartamento()) {
				realizarSomatoriaNotaMonografia();
			}
			
			if(getRequerimentoVO().getTipoRequerimento().getTipo().equals("TC")) {
				setPodeInserirNota(getRequerimentoVO().getDepartamentoPodeInserirNota());
			}
			inicializarListaSelectItemSituacaoRequerimentoDepartamento();
			setMensagemID("msg_Requerimento_enviadoComSucesso");

			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String getAbrirModalCapturarImagemWebCam() {
		if (abrirModalCapturarImagemWebCam == null) {
			abrirModalCapturarImagemWebCam = "";
		}
		return abrirModalCapturarImagemWebCam;
	}

	public void setAbrirModalCapturarImagemWebCam(String abrirModalCapturarImagemWebCam) {
		this.abrirModalCapturarImagemWebCam = abrirModalCapturarImagemWebCam;
	}

	public void montarListaSelectItemDepartamento(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getDepartamentoFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), getPermitirConsultarRequerimentoTodasUnidades(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermiteVisualizarRequerinentoOutroDepartamentoTramite(), getPermiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade(), getRealizarTramiteRequerimentoOutroDepartamento(), getConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades(), getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList<>(0);
			if (resultadoConsulta.size() > 0) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
//				if (resultadoConsulta.size() == 1) {
//					getRequerimentoVO().getFuncionarioVO().setDepartamento(obj);
//					getRequerimentoConsVO().getFuncionarioVO().setDepartamento(obj);
//				}
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPreencherComboDepartamento() {
		if (getRequerimentoConsVO().getUnidadeEnsino().getCodigo() > 0) {
			montarListaSelectItemDepartamento();
			return true;
		}
		return false;
	}

	public List<UsuarioVO> autocompleteResponsavel(Object suggest) {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			return getFacadeFactory().getUsuarioFacade().consultaRapidaPorNomeUsuarioAutoComplete((String) suggest, 20, unidadeEnsinoVOs, getRequerimentoConsVO().getFuncionarioVO().getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<UsuarioVO>();
		}
	}

	public List<CursoVO> autocompleteCurso(Object suggest) {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, unidadeEnsinoVOs, 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<CursoVO>();
		}
	}

	public List<TurmaVO> autocompleteTurma(Object suggest) {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino((String) suggest, getCursoVO().getCodigo(), unidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<TurmaVO>();
		}
	}

	public List<PessoaVO> autocompleteRequisitante(Object suggest) {
		try {
			return getFacadeFactory().getPessoaFacade().consultaRapidaPorNomePessoaAutoComplete((String) suggest, "", 20, getRequerimentoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<PessoaVO>();
		}
	}

	public void limparDadosCurso() {
		setCursoVO(new CursoVO());
		setAutocompleteValorCurso("");
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("MATRICULA", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CARGO", "Cargo"));
		if (getRequerimentoVO().getUnidadeEnsino().getCodigo() == 0 && Uteis.isAtributoPreenchido(getNomeTelaAtual()) && !getNomeTelaAtual().contains("requerimentoCons")) {
			itens.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		}
		return itens;
	}

	public void consultarFuncionarioResponsavel() {
		try {
			List objs = new ArrayList<>(0);
			if (getValorConsultaFuncionarioResponsavel().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			if (getCampoConsultaFuncionarioResponsavel().equals("NOME")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorNomeFuncionario(getValorConsultaFuncionarioResponsavel(), getRequerimentoConsVO().getFuncionarioVO().getDepartamento().getCodigo(), "", unidadeEnsinoVOs, null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioResponsavel().equals("MATRICULA")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorMatriculaFuncionario(getValorConsultaFuncionarioResponsavel(), getRequerimentoConsVO().getFuncionarioVO().getDepartamento().getCodigo(), unidadeEnsinoVOs, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioResponsavel().equals("CPF")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorCPFFuncionario(getValorConsultaFuncionarioResponsavel(), getRequerimentoConsVO().getFuncionarioVO().getDepartamento().getCodigo(), "", unidadeEnsinoVOs, null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioResponsavel().equals("CARGO")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorCargoFuncionario(getValorConsultaFuncionarioResponsavel(), getRequerimentoConsVO().getFuncionarioVO().getDepartamento().getCodigo(), unidadeEnsinoVOs, null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
//			if (getCampoConsultaFuncionarioResponsavel().equals("UNIDADEENSINO")) {
//				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaFuncionarioPorUnidadeEnsino(getValorConsultaFuncionarioResponsavel(), null, getRequerimentoConsVO().getUnidadeEnsino().getCodigo(), null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//			}
			setListaConsultaFuncionarioResponsavel(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionarioResponsavel(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioResponsavel() throws Exception {
		UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("funcionarioResponsavelItens");
		getRequerimentoConsVO().setResponsavel(obj);
		setAutocompleteValorResponsavel(obj.getNome() + " (" + obj.getPessoa().getCodigo() + ")");
	}

	public void scrollerListener() throws Exception {
//		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
//		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
//		consultarOtimizado();
	}

	public String consultarOtimizado() {
		getListaConsultaResumoTransferenciaInternaSelecionados().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();		
		setControleConsulta(new ControleConsulta());
		setMarcarTodos(false);
		try {			
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Consultar requerimento", "Consultando");
			boolean possuiUnidadeSelecionada = false;
			for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()){
				if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
					possuiUnidadeSelecionada =  true;
					break;
				}
			}
			if(!possuiUnidadeSelecionada){				
				setMarcarTodasUnidadeEnsino(true);
				marcarTodasUnidadesEnsinoAction();
			}
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
//			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRequerimentoFacade().consultarOtimizado(getRequerimentoConsVO(), getUnidadeEnsinoVOs(), getListaSelectItemDepartamento(), getCursoVO(), getTurmaVO(), getSituacao(), getSituacaoFinanceira(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getGerarListaRequerimentoTodoPeriodo().booleanValue(), getSigla(), getSituacaoRequerimentoDepartamento(), getOrdernarPor(), getOrdemCrescente(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getPermitirConsultarTodasUnidades(), getPermitirConsultarRequerimentoOutroConsultorResponsavel(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios()));
//			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getRequerimentoFacade().consultarTotalRegistros(getRequerimentoConsVO(), getUnidadeEnsinoVOs(), getListaSelectItemDepartamento(), getCursoVO(), getTurmaVO(), getSituacao(), getSituacaoFinanceira(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getGerarListaRequerimentoTodoPeriodo().booleanValue(), getSigla(), getSituacaoRequerimentoDepartamento(), getOrdernarPor(), getOrdemCrescente(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getPermitirConsultarTodasUnidades(), getPermitirConsultarRequerimentoOutroConsultorResponsavel(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios()));
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultarOtimizadoRequerimento"+ getUsuarioLogado().getUsername());
			if(getNomeTelaAtual().contains("requerimentoOperacaoLote")) {
//				getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "requerimentoOperacaoLote"+ getUsuarioLogado().getUsername());
				if(!Uteis.isAtributoPreenchido(getRequerimentoConsVO().getTipoRequerimento())) {
					throw new Exception("O campo TIPO DE REQUERIMENTO deve ser informado.");
				}
				setConsultarVagaOfertadaUnidadeEnsinoCurso(false);
				getRequerimentoConsVO().setTipoRequerimento(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(getRequerimentoConsVO().getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				setSituacao(getNomeTelaAtual());
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRequerimentoFacade().consultarRequerimentoOperacaoLote(getRequerimentoConsVO(), getUnidadeEnsinoVOs(),  getCursoVO(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getGerarListaRequerimentoTodoPeriodo().booleanValue(), getOrdernarPor(), getOrdemCrescente(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), 0, 0, getPermitirConsultarTodasUnidades(), getPermitirConsultarRequerimentoOutroConsultorResponsavel(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios()));			
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getControleConsultaOtimizado().getListaConsulta().size());
//				getResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino().clear();
//				getResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso().clear();
//				getFacadeFactory().getRequerimentoFacade().realizarGeracaoResumoOperacaoLote(((List<RequerimentoVO>) getControleConsultaOtimizado().getListaConsulta()), getRequerimentoConsVO().getTipoRequerimento());
				marcarDesmarcarTodos();
//				getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "requerimentoOperacaoLote1"+ getUsuarioLogado().getUsername());
			}else {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRequerimentoFacade().consultarOtimizado(getRequerimentoConsVO(), getUnidadeEnsinoVOs(), new ArrayList(), getCursoVO(), getTurmaVO(), getSituacao(), getSituacaoFinanceira(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getGerarListaRequerimentoTodoPeriodo().booleanValue(), getSigla(), getSituacaoRequerimentoDepartamento(), getOrdernarPor(), getOrdemCrescente(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getQtdRegistrosPorPagina(), getControleConsultaOtimizado().getOffset(), getPermitirConsultarTodasUnidades(), getPermitirConsultarRequerimentoOutroConsultorResponsavel(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios(), getControleConsultaOtimizado()));							
			}
//			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultarOtimizadoRequerimento1"+ getUsuarioLogado().getUsername());
			
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Consultar requerimento", "Consultando");
			persistirLayoutPadraoQuantidade(String.valueOf(getQtdRegistrosPorPagina()));
			getControleConsultaOtimizado().setLimitePorPagina(getQtdRegistrosPorPagina());
			consultarDadosGrafico();
			
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), e, getUsuarioLogado(), "", "", true);
			if(!getNomeTelaAtual().contains("requerimentoOperacaoLote")) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
			}else {
				return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoOperacaoLote");
			}
		}
	}

	// public String consultarTotalRegistro() throws Exception {
	// registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle",
	// "Inicializando Consultar requerimento", "Consultando");
	// super.consultar();
	// List objs = new ArrayList<>(0);
	// getListaConsulta().clear();
	// try {
	// if (getListaSelectItemDepartamento().isEmpty()) {
	// return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
	// }
	// objs =
	// getFacadeFactory().getRequerimentoFacade().consultarTotalRegistro(getRequerimentoConsVO(),
	// getUnidadeEnsinoLogado().getCodigo(), getCursoVO(), getTurmaVO(),
	// getSituacao(), getControleConsulta().getDataIni(),
	// getControleConsulta().getDataFim(),
	// getGerarListaRequerimentoTodoPeriodo().booleanValue(), getSigla(),
	// getOrdernarPor(), getOrdemCrescente(), false, getUsuarioLogado(),
	// getConfiguracaoGeralPadraoSistema());
	// setListaConsulta(objs);
	// registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle",
	// "Finalizando Consultar requerimento", "Consultando");
	// setMensagemID("msg_dados_consultados");
	// consultarDadosGrafico();
	// return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
	// } catch (Exception e) {
	// setListaConsulta(new ArrayList<>(0));
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCons");
	// } finally {
	// // limparDadosConsultaTotalRegistro();
	// }
	// }

	public void consultarDadosGrafico() throws Exception {
		try {
			if(getNomeTelaAtual().endsWith("requerimentoCons.xhtml")) {
			boolean possuiUnidadeSelecionada = false;
			for(UnidadeEnsinoVO unidadeEnsinoVO: getUnidadeEnsinoVOs()){
				if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
					possuiUnidadeSelecionada =  true;
					break;
				}
			}
			if(!possuiUnidadeSelecionada){				
				setMarcarTodasUnidadeEnsino(true);
				marcarTodasUnidadesEnsinoAction();
			}
			String dadosGrafico = getFacadeFactory().getRequerimentoFacade().consultarDadosGraficoRequerimento(getRequerimentoConsVO(), getUnidadeEnsinoVOs(), getListaSelectItemDepartamento(), getCursoVO(), getTurmaVO(), getSituacao(), getSituacaoFinanceira(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getGerarListaRequerimentoTodoPeriodo(), getSigla(), getSituacaoRequerimentoDepartamento(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getPermitirConsultarTodasUnidades(), getPermitirConsultarRequerimentoOutroConsultorResponsavel(), getPermitirConsultarRequerimentoOutroDepartametoResponsavel(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios());
			context().getExternalContext().getSessionMap().put("grafico", dadosGrafico);
			}
		} catch (Exception e) {
		}
	}

	private void definirEscopoRequerimentoComBaseNaPermissaoAcesso() {
		setPermitirConsultarTodasUnidades(verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarTodasUnidades"));
		if (!getPermitirConsultarTodasUnidades() && !getConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades() && ! getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades()) {
			if ((getUnidadeEnsinoLogado() == null) || (getUnidadeEnsinoLogado().getCodigo().equals(new Integer(0)))) {
				setPermitirConsultarTodasUnidades(Boolean.TRUE);
			} else {
				this.getRequerimentoVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
				this.getRequerimentoConsVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
			}
		}
	}

	public Boolean verificarUsuarioPossuiPermissaoConsulta(String identificadorAcaoPermissao) {
		Boolean liberar = Boolean.FALSE;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	public void limparDadosConsultaTotalRegistro() throws Exception {
		if (permitirConsultarTodasUnidades) {
			getRequerimentoConsVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		}
		setCursoVO(new CursoVO());
		setTurmaVO(new TurmaVO());
		getRequerimentoConsVO().setPessoa(new PessoaVO());
		getRequerimentoConsVO().setDepartamentoResponsavel(new DepartamentoVO());
		if (getPermitirConsultarRequerimentoOutroConsultorResponsavel() && !getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades()) {
			getRequerimentoConsVO().setResponsavel(new UsuarioVO());
		}
		getRequerimentoConsVO().setTipoRequerimento(new TipoRequerimentoVO());
		consultarDadosGrafico();
	}

	public void limparConsultaRequisitante() {
		getListaConsultaRequisitante().clear();
		setValorConsultaRequisitante("");
	}

	public void limparDadosRequisitante() throws Exception {
		getRequerimentoConsVO().setPessoa(new PessoaVO());
		getRequerimentoVO().setPessoa(new PessoaVO());
		setAutocompleteValorRequisitante("");
		consultarDadosGrafico();
	}

	public void limparConsultaFuncionario() {
		getListaConsultaFuncionarioResponsavel().clear();
		setValorConsultaFuncionarioResponsavel("");
	}

	public void limparDadosFuncionarioResponsavel() throws Exception {
		getRequerimentoConsVO().setResponsavel(new UsuarioVO());
		consultarDadosGrafico();
		setAutocompleteValorResponsavel("");
	}

	public String getCampoConsultaFuncionarioResponsavel() {
		if (campoConsultaFuncionarioResponsavel == null) {
			campoConsultaFuncionarioResponsavel = "";
		}
		return campoConsultaFuncionarioResponsavel;
	}

	public void setCampoConsultaFuncionarioResponsavel(String campoConsultaFuncionarioResponsavel) {
		this.campoConsultaFuncionarioResponsavel = campoConsultaFuncionarioResponsavel;
	}

	public String getValorConsultaFuncionarioResponsavel() {
		if (valorConsultaFuncionarioResponsavel == null) {
			valorConsultaFuncionarioResponsavel = "";
		}
		return valorConsultaFuncionarioResponsavel;
	}

	public void setValorConsultaFuncionarioResponsavel(String valorConsultaFuncionarioResponsavel) {
		this.valorConsultaFuncionarioResponsavel = valorConsultaFuncionarioResponsavel;
	}

	public List getListaConsultaFuncionarioResponsavel() {
		if (listaConsultaFuncionarioResponsavel == null) {
			listaConsultaFuncionarioResponsavel = new ArrayList<>(0);
		}
		return listaConsultaFuncionarioResponsavel;
	}

	public void setListaConsultaFuncionarioResponsavel(List listaConsultaFuncionarioResponsavel) {
		this.listaConsultaFuncionarioResponsavel = listaConsultaFuncionarioResponsavel;
	}

	public List getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList<SelectItem>(0);
			montarListaSelectItemDepartamento();
		}
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public Boolean getGerarListaRequerimentoTodoPeriodo() {
		if (gerarListaRequerimentoTodoPeriodo == null) {
			gerarListaRequerimentoTodoPeriodo = Boolean.TRUE;
		}
		return gerarListaRequerimentoTodoPeriodo;
	}

	public void setGerarListaRequerimentoTodoPeriodo(Boolean gerarListaRequerimentoTodoPeriodo) {
		this.gerarListaRequerimentoTodoPeriodo = gerarListaRequerimentoTodoPeriodo;
	}

	public Boolean getPermitirConsultarTodasUnidades() {
		if (permitirConsultarTodasUnidades == null) {
			permitirConsultarTodasUnidades = Boolean.FALSE;
		}
		return permitirConsultarTodasUnidades;
	}

	public void setPermitirConsultarTodasUnidades(Boolean permitirConsultarTodasUnidades) {
		this.permitirConsultarTodasUnidades = permitirConsultarTodasUnidades;
	}

	public Boolean getPermitirConsultarRequerimentoOutroConsultorResponsavel() {
		if (permitirConsultarRequerimentoOutroConsultorResponsavel == null) {
			permitirConsultarRequerimentoOutroConsultorResponsavel = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutrosConsultoresResponsaveis");
		}
		return permitirConsultarRequerimentoOutroConsultorResponsavel;
	}

	public void setPermitirConsultarRequerimentoOutroConsultorResponsavel(Boolean permitirConsultarRequerimentoOutroConsultorResponsavel) {
		this.permitirConsultarRequerimentoOutroConsultorResponsavel = permitirConsultarRequerimentoOutroConsultorResponsavel;
	}

	public Boolean getPermitirConsultarRequerimentoOutroDepartametoResponsavel() {
		if (permitirConsultarRequerimentoOutroDepartametoResponsavel == null) {

			permitirConsultarRequerimentoOutroDepartametoResponsavel = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutroDepartamentoResponsavel");
		}
		return permitirConsultarRequerimentoOutroDepartametoResponsavel;
	}

	public void setPermitirConsultarRequerimentoOutroDepartametoResponsavel(Boolean permitirConsultarRequerimentoOutroDepartametoResponsavel) {
		this.permitirConsultarRequerimentoOutroDepartametoResponsavel = permitirConsultarRequerimentoOutroDepartametoResponsavel;
	}

	public Boolean getPermitirConsultarRequerimentoTodasUnidades() {
		if (permitirConsultarRequerimentoTodasUnidades == null) {
			permitirConsultarRequerimentoTodasUnidades = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarTodasUnidades");
		}
		return permitirConsultarRequerimentoTodasUnidades;
	}

	public void setPermitirConsultarRequerimentoTodasUnidades(Boolean permitirConsultarRequerimentoTodasUnidades) {
		this.permitirConsultarRequerimentoTodasUnidades = permitirConsultarRequerimentoTodasUnidades;
	}

	/**
	 * Rotina responsavel por enviar e-mail de Notificação Mensagem de Interações Tramite Requerimento
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void enviarEmailTramiteInteracaoTramiteRequerimento() throws Exception {
		try {
			if (getUsuarioLogado().getPessoa() == null || getUsuarioLogado().getPessoa().getCodigo().equals(0)) {
				throw new Exception("Este usuário não pode enviar Comunicação Interna, pois não possui nenhuma pessoa vinculada a ele.");
			} else {
//				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTramiteInteracaoTramiteRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(),getInteracaoRequerimentoHistorico(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Rotina responsavel por enviar e-mail para o Aluno informando sobre o
	 * tramite entre departamentos de sua requisição,
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void enviarEmailAlunoSobreTramiteRequerimento() throws Exception {
//		try {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTramiteRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(), getUsuarioLogado());			
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}
	
	/**
	 * Rotina responsavel por enviar e-mail para o Atendente informando sobre o
	 * tramite entre departamentos de sua requisição,
	 * 
	 * @param obj
	 * @throws Exception
	 */
	public void enviarEmailAlunoSobreTramiteRequerimentoAtendente() throws Exception {
		try {
			if (getUsuarioLogado().getPessoa() == null || getUsuarioLogado().getPessoa().getCodigo().equals(0)) {
				return;
			} else {
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getFuncionarioVO())) {
//					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemTramiteRequerimentoAtendente(getRequerimentoVO(), getRequerimentoHistoricoVO(), getUsuarioLogado());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public RequerimentoVO getRequerimentoConsVO() {
		if (requerimentoConsVO == null) {
			requerimentoConsVO = new RequerimentoVO();
		}
		return requerimentoConsVO;
	}

	public void setRequerimentoConsVO(RequerimentoVO requerimentoConsVO) {
		this.requerimentoConsVO = requerimentoConsVO;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
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

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList<>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			// getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String selecionarFuncionario() throws Exception {
		try {
			if (getAbrirOpcaoInformarFuncionarioProximoTramite()) {
				selecionarFuncionarioResponsavelProximoTramite();
			} else {
				FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
				if (Uteis.isAtributoPreenchido(getRequerimentoVO()) && !obj.getCodigo().equals(getRequerimentoVO().getFuncionarioVO().getCodigo())) {
					getFacadeFactory().getRequerimentoFacade().realizarAlteracaoFuncionarioResponsavelRequerimento(getRequerimentoVO(), obj, getUsuarioLogado());
					setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
					realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
				} else {
					getRequerimentoVO().setFuncionarioVO(obj);
				}
				if (Uteis.isAtributoPreenchido(getRequerimentoVO()) && (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor())) {
					consultarVisaoProfessorCoordenador();
					return Uteis.getCaminhoRedirecionamentoNavegacao(getUsuarioLogado().getIsApresentarVisaoProfessor() ? "requerimentoProfessorCons.xhtml" : "requerimentoCoordenadorCons.xhtml");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}
	
	public String realizarResponderQuestionarioNavegandoAba() {
		realizarResponderQuestionario();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return navegarAba("richQuestionarioAluno", "requerimentoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return navegarAba("richQuestionarioAluno", "requerimentoCoordenadorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return navegarAba("richQuestionarioAluno", "requerimentoForm.xhtml"); 
		}
		return "";
	}
	
	public String realizarNavegacaoAba() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return navegarAba("richTab", "requerimentoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return navegarAba("richTab", "requerimentoCoordenadorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return navegarAba("richTab", "requerimentoForm.xhtml"); 
		}
		return "";
	}

	public void realizarResponderQuestionario() {
		try {
			if (getRequerimentoVO().getTipoRequerimento().getQuestionario().getCodigo() != 0 && getRequerimentoVO().getNovoObj()) {
				if (getRequerimentoVO().getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
					getRequerimentoVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
				setAbrirModalQuestionario(true);
			} else if (getRequerimentoVO().getQuestionarioVO().getCodigo() != 0 && !getRequerimentoVO().getNovoObj()) {
				if (getRequerimentoVO().getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
					getRequerimentoVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getRequerimentoVO().getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				}
				getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorRequerimento(getRequerimentoVO().getCodigo().intValue(), getRequerimentoVO().getQuestionarioVO());
				setAbrirModalQuestionario(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosListaResposta() throws Exception {
		RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
		if (obj.getSelecionado()) {
			obj.setSelecionado(false);
		} else {
			obj.setSelecionado(true);
			getRequerimentoVO().getQuestionarioVO().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
		}
	}

	public Boolean getPermiteAlterarQuestionarioRequerimentoHistorico() {		
		return getRequerimentoVO().getDepartamentoResponsavel().getCodigo().equals(getRequerimentoHistoricoVO().getDepartamento().getCodigo()) && getRequerimentoVO().getOrdemExecucaoTramiteDepartamento().equals(getRequerimentoHistoricoVO().getOrdemExecucaoTramite()) && !getRequerimentoVO().getSituacao().equals("FD") && !getRequerimentoVO().getSituacao().equals("FI");				
	}

	public void validarDadosListaRespostaRequerimentoHistorico() throws Exception {
		RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
		if (obj.getSelecionado()) {
			obj.setSelecionado(false);
		} else {
			obj.setSelecionado(true);
			getRequerimentoHistoricoVO().getQuestionario().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
		}
	}

	public Boolean getIsPermiteVisualizarQuestionario() {
		return !getRequerimentoVO().isNovoObj() && getRequerimentoVO().getQuestionarioVO().getCodigo() > 0;
	}

	public String getAcaoModalQuestionario() {
		if (getAbrirModalQuestionario()) {
			return "RichFaces.$('panelQuestionario').show()";
		}

		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			return "RichFaces.$('panelQuestionario').hide(); " + getGravadoComSucesso();
		}
		return "RichFaces.$('panelQuestionario').hide()";
	}

	public String getAcaoModalQuestionarioRequerimentoHistorico() {
		if (getAbrirModalQuestionarioRequerimentoHistorico()) {
			return "RichFaces.$('panelQuestionarioRequerimentoHistorico').show()";
		}
		if (getFinalizarEtapaRequerimento()) {
			return "RichFaces.$('panelObsTramite').show(); RichFaces.$('panelQuestionarioRequerimentoHistorico').hide();";
		}
		if (getMsgRequerimentoImagemGravadoComSucesso().trim().isEmpty()) {
			return "RichFaces.$('panelObsTramite').hide(); RichFaces.$('panelQuestionarioRequerimentoHistorico').hide();";
		}
		return "RichFaces.$('panelObsTramite').hide(); RichFaces.$('panelQuestionarioRequerimentoHistorico').hide(); RichFaces.$('panelGravadoComSucesso').show();";
	}

	public Boolean getAbrirModalQuestionario() {
		if (abrirModalQuestionario == null) {
			abrirModalQuestionario = false;
		}
		return abrirModalQuestionario;
	}

	public void setAbrirModalQuestionario(Boolean abrirModalQuestionario) {
		this.abrirModalQuestionario = abrirModalQuestionario;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "LAYOUT_1";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List<SelectItem> listaSelectItemTipoLayout;
	public List<SelectItem> getListaSelectItemTipoLayout() {
		if(listaSelectItemTipoLayout == null) {
		listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
		listaSelectItemTipoLayout.add(new SelectItem("LAYOUT_1", "Layout 1 - 1ª Via"));
		listaSelectItemTipoLayout.add(new SelectItem("LAYOUT_2", "Layout 2 - 2ª Via"));
		listaSelectItemTipoLayout.add(new SelectItem("LAYOUT_3", "Layout 3 - Online"));
	}
		return listaSelectItemTipoLayout;
	}

	public String getPopUpAbrir() {
		if (popUpAbrir == null) {
			popUpAbrir = "";
		}
		return popUpAbrir;
	}

	public void setPopUpAbrir(String popUpAbrir) {
		this.popUpAbrir = popUpAbrir;
	}

	public Boolean getApresentarBotaoAproveitamento() {
		if (apresentarBotaoAproveitamento == null) {
			apresentarBotaoAproveitamento = Boolean.FALSE;
		}
		return apresentarBotaoAproveitamento;
	}

	public void setApresentarBotaoAproveitamento(Boolean apresentarBotaoAproveitamento) {
		this.apresentarBotaoAproveitamento = apresentarBotaoAproveitamento;
	}

	public Boolean getApresentarBotaoTransferenciaExt() {
		if (apresentarBotaoTransferenciaExt == null) {
			apresentarBotaoTransferenciaExt = Boolean.FALSE;
		}
		return apresentarBotaoTransferenciaExt;
	}

	public void setApresentarBotaoTransferenciaExt(Boolean apresentarBotaoTransferenciaExt) {
		this.apresentarBotaoTransferenciaExt = apresentarBotaoTransferenciaExt;
	}

	public Boolean getApresentarBotaoMatricula() {
		if (apresentarBotaoMatricula == null) {
			apresentarBotaoMatricula = Boolean.FALSE;
		}
		return apresentarBotaoMatricula;
	}

	public void setApresentarBotaoMatricula(Boolean apresentarBotaoMatricula) {
		this.apresentarBotaoMatricula = apresentarBotaoMatricula;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public MaterialRequerimentoVO getMaterialRequerimento() {
		if (materialRequerimento == null) {
			materialRequerimento = new MaterialRequerimentoVO();
		}
		return materialRequerimento;
	}

	public void setMaterialRequerimento(MaterialRequerimentoVO materialRequerimento) {
		this.materialRequerimento = materialRequerimento;
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

	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Boolean getAbrirModalQuestionarioRequerimentoHistorico() {
		if (abrirModalQuestionarioRequerimentoHistorico == null) {
			abrirModalQuestionarioRequerimentoHistorico = false;
		}
		return abrirModalQuestionarioRequerimentoHistorico;
	}

	public void setAbrirModalQuestionarioRequerimentoHistorico(Boolean abrirModalQuestionarioRequerimentoHistorico) {
		this.abrirModalQuestionarioRequerimentoHistorico = abrirModalQuestionarioRequerimentoHistorico;
	}

	public String getMotivoRetornoDepartamento() {
		if (motivoRetornoDepartamento == null) {
			motivoRetornoDepartamento = "";
		}
		return motivoRetornoDepartamento;
	}

	public void setMotivoRetornoDepartamento(String motivoRetornoDepartamento) {
		this.motivoRetornoDepartamento = motivoRetornoDepartamento;
	}

	public List<SelectItem> getTipoRequerimentoDepartamentoRetornarVOs() {
		if (tipoRequerimentoDepartamentoRetornarVOs == null) {
			tipoRequerimentoDepartamentoRetornarVOs = new ArrayList<SelectItem>();
		}
		return tipoRequerimentoDepartamentoRetornarVOs;
	}

	public void setTipoRequerimentoDepartamentoRetornarVOs(List<SelectItem> tipoRequerimentoDepartamentoRetornarVOs) {
		this.tipoRequerimentoDepartamentoRetornarVOs = tipoRequerimentoDepartamentoRetornarVOs;
	}

	public Boolean getIsPermiteRetornarDepartamentoAnterior() {
		return !getTipoRequerimentoDepartamentoRetornarVOs().isEmpty() && !getRequerimentoVO().getSituacao().equals("FD") && !getRequerimentoVO().getSituacao().equals("FI") && !getIsRequerimentoPessoaLogada()
				&& getPermitirApresentarBotaoEnviarDepartamentoAnterior();
	}

	public Integer getTipoRequerimentoDepartamentoAnterior() {
		if (tipoRequerimentoDepartamentoAnterior == null) {
			tipoRequerimentoDepartamentoAnterior = 0;
		}
		return tipoRequerimentoDepartamentoAnterior;
	}

	public void setTipoRequerimentoDepartamentoAnterior(Integer tipoRequerimentoDepartamentoAnterior) {
		this.tipoRequerimentoDepartamentoAnterior = tipoRequerimentoDepartamentoAnterior;
	}

	public String getAcaoModalDepartamentoAnterior() {
		if (acaoModalDepartamentoAnterior == null) {
			acaoModalDepartamentoAnterior = "";
		}
		return acaoModalDepartamentoAnterior;
	}

	public void setAcaoModalDepartamentoAnterior(String acaoModalDepartamentoAnterior) {
		this.acaoModalDepartamentoAnterior = acaoModalDepartamentoAnterior;
	}

	public Boolean getFinalizarEtapaRequerimento() {
		if (finalizarEtapaRequerimento == null) {
			finalizarEtapaRequerimento = false;
		}
		return finalizarEtapaRequerimento;
	}

	public void setFinalizarEtapaRequerimento(Boolean finalizarEtapaRequerimento) {
		this.finalizarEtapaRequerimento = finalizarEtapaRequerimento;
	}

	public Boolean getOrdemCrescente() {
		if (ordemCrescente == null) {
			ordemCrescente = true;
		}
		return ordemCrescente;
	}

	public void setOrdemCrescente(Boolean ordemCrescente) {
		this.ordemCrescente = ordemCrescente;
	}

	public List<SelectItem> getListaSelectItemTipoOrdenacao() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("nomeRequerente", "Nome Requerente"));
		objs.add(new SelectItem("dataRequerimento", "Data Requerimento"));
		objs.add(new SelectItem("dataPrevistaFinalizacao", "Data Prevista Finalização"));
		objs.add(new SelectItem("dataTramiteDepartamento", "Data Trâmite Departamento"));
		objs.add(new SelectItem("tipoRequerimento", "Tipo Requerimento"));
		return objs;
	}

	public void validarApresentacaoBotaoImprimirVisaoAluno(List<RequerimentoVO> listaRequerimento) throws Exception {
		try {
			for (RequerimentoVO obj : listaRequerimento) {
				obj.setAptoImpressaoVisaoAluno(getFacadeFactory().getRequerimentoFacade().validarApresentarBotaoImprimirVisaoAluno(obj));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarApresentacaoBotaoImprimir(RequerimentoVO requerimento) throws Exception {
		try {
			requerimento.setAptoImpressaoVisaoAluno(getFacadeFactory().getRequerimentoFacade().validarApresentarBotaoImprimirVisaoAluno(requerimento));
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void imprirmirDeclaracaoVisaoAdministrativo() {
		imprirmirDeclaracaoVisaoAlunoDeferido();
	}

	public void imprirmirDeclaracaoVisaoAlunoDeferido() {
		try {
			carregarDadosRequerimentoParaImpressao();
			ImpressaoContratoVO contratoGravar = new ImpressaoContratoVO();
			DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
			if(Uteis.isAtributoPreenchido(getRequerimentoVO())) {
				if (getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()) {
					contratoGravar.setGerarNovoArquivoAssinado(false);	
				} else {
					documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getCodigo(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO.name(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					contratoGravar.setGerarNovoArquivoAssinado(!Uteis.isAtributoPreenchido(documentoAssinadoVO));
				}
			}else {
				contratoGravar.setGerarNovoArquivoAssinado(false);
			}
			contratoGravar.setMatriculaVO(getRequerimentoVO().getMatricula());
			contratoGravar.setImpressaoContratoExistente(true);
			contratoGravar.setDocumentoAssinado(documentoAssinadoVO);
			imprimirDeclaracaoVisaoAluno(contratoGravar, getRequerimentoVO(), true, getConfiguracaoGeralPadraoSistema());	
		} catch (Exception e) {
			setImprimirVisaoAluno(Boolean.FALSE);
			setFazerDownload(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void imprirmirDeclaracaoVisaoAlunoOpcao() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
			setRequerimentoVO(obj);
			imprirmirDeclaracaoVisaoAlunoDeferido();
		} catch (Exception e) {
			setImprimirVisaoAluno(Boolean.FALSE);
			setFazerDownload(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void carregarDadosRequerimentoParaImpressao() {
		try {
			if (Uteis.isAtributoPreenchido(getRequerimentoVO())) {
				getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), NivelMontarDados.TODOS, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getRequerimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}
			getFacadeFactory().getMatriculaFacade().carregarDados(getRequerimentoVO().getMatricula(), getUsuarioLogado());
			List<MatriculaPeriodoVO> lista = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getMatricula().getUnidadeEnsino().getCodigo(), false, new DataModelo(), getUsuarioLogado());
			getRequerimentoVO().getMatricula().getMatriculaPeriodoVOs().addAll(lista);
		} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarImpressaoContratoExistente(Boolean gerarNovoContrato ) {
		try {
			if(gerarNovoContrato){
				imprirmirDeclaracaoVisaoAlunoDeferido();	
			}else if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && !getImpressaoContratoFiltro().isImpressaoPdf()){
				setImprimirVisaoAluno(true);
				setFazerDownload(false);
				getImpressaoContratoFiltro().setImpressaoContratoExistente(false);
			}else if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && getImpressaoContratoFiltro().isImpressaoPdf()){
				setFazerDownload(true);	
				setImprimirVisaoAluno(false);
				getImpressaoContratoFiltro().setImpressaoContratoExistente(false);
			}	
		} catch (Exception e) {
			setImprimirVisaoAluno(Boolean.FALSE);
			setFazerDownload(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getContrato() {
		if (getImprimirVisaoAluno()) {
			return "abrirPopup('../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		}else if(getFazerDownload()){
			return getDownload();
		}else if(getImpressaoContratoFiltro().getImpressaoContratoExistente()){
			return "RichFaces.$('panelImpressaoContratoExistente').show()";
		}
		return "";
	}

	public void imprimirDeclaracaoVisaoAluno(ImpressaoContratoVO contratoGravar, RequerimentoVO requerimento, Boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config) throws Exception {		

			limparMensagem();
			setFazerDownload(false);
			setImprimirVisaoAluno(Boolean.FALSE);
			this.setCaminhoRelatorio("");	
			setImpressaoContratoFiltro(new ImpressaoContratoVO());
			TextoPadraoDeclaracaoVO texto = new TextoPadraoDeclaracaoVO(); 

			if(getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()) {
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) && (getRequerimentoVO().getFormatoCertificadoSelecionado().equals("") || getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital())) {
					texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());									
				}
				else if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso()) && (getRequerimentoVO().getFormatoCertificadoSelecionado().equals("") ||getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso())) {
					texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());									
				}
			}else {
				try {
					texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(requerimento.getTipoRequerimento().getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} catch (Exception e) {
					throw new Exception(e.getMessage() + " (TEXTO PADRÃO não vinculado ao TIPO REQUERIMENTO).");
				}
			}
						
			if(texto.getTipo().equals("CE") && requerimento.getTipoRequerimento().getIsCertificadoModular()){
				try {
					realizarImpressaoCertificadoModular(requerimento, contratoGravar);
				} catch (Exception e) {
					throw e;
				}
			} else {
				setCaminhoRelatorio(getFacadeFactory().getRequerimentoFacade().realizarImpressaoDeclaracaoRequerimento(requerimento, contratoGravar, contratoGravar, getConfiguracaoGeralPadraoSistema(), texto, persistirDocumentoAssinado, getUsuarioLogado()));
			}
			if(getImpressaoContratoFiltro().getImpressaoContratoExistente() && !contratoGravar.getImpressaoContratoExistente()){
				setImprimirVisaoAluno(false);
				setFazerDownload(false);
			}else if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && !contratoGravar.isImpressaoPdf() ){
				setImprimirVisaoAluno(false);
				setFazerDownload(true);
			}else if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && contratoGravar.isImpressaoPdf()){
				setFazerDownload(true);	
				setImprimirVisaoAluno(false);
			}
	}

	public Boolean getImprimirVisaoAluno() {
		if (imprimirVisaoAluno == null) {
			imprimirVisaoAluno = Boolean.FALSE;
		}
		return imprimirVisaoAluno;
	}

	public void setImprimirVisaoAluno(Boolean imprimirVisaoAluno) {
		this.imprimirVisaoAluno = imprimirVisaoAluno;
	}

	public void marcarTodasUnidadesEnsinoAction() {
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs())) {
			getUnidadeEnsinoVOs().stream().forEach(u -> u.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino()));
		}
		verificarTodasUnidadeEnsinoSelecionados();
	}
	
	public void consultarUnidadeEnsino() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
					setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(getUsuarioLogado()));
				} else {
					setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, getUsuarioLogado()));
				}
				for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
					obj.setFiltrarUnidadeEnsino(true);
				}
				verificarTodasUnidadeEnsinoSelecionados();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadeEnsinoSelecionados() {
		getRequerimentoConsVO().getUnidadeEnsino().setNome(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs())) {
			getRequerimentoConsVO().getUnidadeEnsino().setNome(getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(Collectors.joining(";  ")));
			if (Uteis.isAtributoPreenchido(getRequerimentoConsVO().getMatricula())) {
				limparDadosRegistroAcademicoCons();
			}
			if (Uteis.isAtributoPreenchido(getCursoVO())) {
				limparDadosCurso();
			}
			if (Uteis.isAtributoPreenchido(getTurmaVO())) {
				limparDadosTurma();
			}
		}
	}

	public void atualizarListaComboBox() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void alterarDataPrevistaFinalizacaoRequerimento() {
		try {
			getFacadeFactory().getRequerimentoFacade().alterarDataPrevistaFinalizacaoRequerimento(getRequerimentoVO(), getUsuario());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPermiteVisualizarRequerinentoOutroDepartamentoTramite() {
		if (permiteVisualizarRequerinentoOutroDepartamentoTramite == null) {
			permiteVisualizarRequerinentoOutroDepartamentoTramite = verificarUsuarioPossuiPermissaoConsulta("Visualizar_Requerinento_Outro_Departamento_Tramite");
		}
		return permiteVisualizarRequerinentoOutroDepartamentoTramite;
	}

	public void setPermiteVisualizarRequerinentoOutroDepartamentoTramite(Boolean permiteVisualizarRequerinentoOutroDepartamentoTramite) {
		this.permiteVisualizarRequerinentoOutroDepartamentoTramite = permiteVisualizarRequerinentoOutroDepartamentoTramite;
	}

	public void executarVerificarApresentarCampoDisciplina() {
		try {
			TipoRequerimentoVO obj = getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(getRequerimentoConsVO().getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,  getUsuarioLogado());
			setApresentarCampoDisciplina(obj.getIsPermiteInformarDisciplina());
			consultarDadosGrafico();
		} catch (Exception e) {
		}
	}

	public Boolean getApresentarCampoDisciplina() {
		if (apresentarCampoDisciplina == null) {
			apresentarCampoDisciplina = false;
		}
		return apresentarCampoDisciplina;
	}

	public void setApresentarCampoDisciplina(Boolean apresentarCampoDisciplina) {
		this.apresentarCampoDisciplina = apresentarCampoDisciplina;
	}

	public void limparConsultaDisciplina() {
		getListaConsultaDisciplina().clear();
		setValorConsultaDisciplina("");
	}

	public void limparDadosDisciplina() throws Exception {
		getRequerimentoConsVO().setDisciplina(null);
		setAutocompleteValorDisciplina("");
		consultarDadosGrafico();
	}

	public List<DisciplinaVO> autocompleteDisciplina(Object suggest) {
		try {
			return getFacadeFactory().getDisciplinaFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getRequerimentoVO().getUnidadeEnsino().getCodigo(), 20, false, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<DisciplinaVO>(0);
		}
	}

	public void selecionarDisciplinaRequerimentoCons() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getRequerimentoConsVO().setDisciplina(obj);
			setAutocompleteValorDisciplina(obj.getAbreviatura()+ " - "+obj.getNome()+" ("+obj.getCodigo()+")");
			consultarDadosGrafico();
		} catch (Exception e) {
		}
	}

	public Boolean getIsApresentarBotaoImprimirVisaoAluno() {
		return !getRequerimentoVO().getNovoObj() && getRequerimentoVO().getEdicao() && getRequerimentoVO().getTipoRequerimento().getRequerimentoVisaoAlunoApresImprimirDeclaracao() && getRequerimentoVO().getSituacao().equals("FD");
	}

	// SO deve apresentar o botao imprimir declaracao na visao administrativa:
	// quando o usuario iniciar a execução é estiver pago o requerimento, ou
	// caso o requerimento for isento e o usuario iniciar a execucao
	public Boolean getIsApresentarBotaoImprimirDeclaracaoVisaoAdministrativa() {
		if (getRequerimentoVO().getExigePagamento().equals(Boolean.TRUE) && getRequerimentoVO().getTipoRequerimento().getIsUtilizaTextoPadrao() && !getRequerimentoVO().getSituacao().equals("PE")) {
			if (getRequerimentoVO().getIsRequerimentoPago()) {
				return true;
			} else {
				return false;
			}
		} else if (getRequerimentoVO().getSituacaoFinanceira().equals("IS") && getRequerimentoVO().getTipoRequerimento().getIsUtilizaTextoPadrao() && !getRequerimentoVO().getSituacao().equals("PE")) {
			return true;
		}
		return false;
	}

	/**
	 * @author Victor Hugo de Paula Costa
	 */
//	private ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;


	public void realizarRecebimentoCartaoCredito() {
//		List<FormaPagamentoNegociacaoRecebimentoVO> listaCartoesAntesAlteracao = getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().stream().collect(Collectors.toList());
//		try {
//			executarValidacaoSimulacaoVisaoAluno();
//			getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
//			getFacadeFactory().getNegociacaoRecebimentoFacade().realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(getNegociacaoRecebimentoVO(), getNegociacaoRecebimentoVO().getMatricula(),  getUsuarioLogado());
//			imprimirComprovanteRecebimento();
//			setModalPagamentoOnline("RichFaces.$('panelPagamento').hide()");
//			setModalConfirmacaoPagamento("RichFaces.$('panelConfirmacaoPagamento').show()");
//			setRequerimentoVO(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(getRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//			getRequerimentoVO().setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
//			setMensagemID("msg_RenovarMatriculaControle_pagamentoRealizadoComSucesso", Uteis.SUCESSO);
//		} catch (Exception e) {
//			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
//			getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().addAll(listaCartoesAntesAlteracao);
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void imprimirComprovanteRecebimento() {
//		try {
//			comprovanteRecebimentoRelControle = null;
//			comprovanteRecebimentoRelControle = (ComprovanteRecebimentoRelControle) context().getExternalContext().getSessionMap().get(ComprovanteRecebimentoRelControle.class.getSimpleName());
//			if (comprovanteRecebimentoRelControle == null) {
//				comprovanteRecebimentoRelControle = new ComprovanteRecebimentoRelControle();
//				context().getExternalContext().getSessionMap().put(ComprovanteRecebimentoRelControle.class.getSimpleName(), comprovanteRecebimentoRelControle);
//			}
//			if (!getNegociacaoRecebimentoVO().getCodigo().equals(0)) {
//				getComprovanteRecebimentoRelControle().setNegociacaoRecebimentoVO((NegociacaoRecebimentoVO) getNegociacaoRecebimentoVO().clone());
//				getComprovanteRecebimentoRelControle().imprimirPDFRecebimentoCartaoCredito();
//			} else {
//				setMensagemID("msg_relatorio_sem_dados");
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}
//
//	public ComprovanteRecebimentoRelControle getComprovanteRecebimentoRelControle() {
//		return comprovanteRecebimentoRelControle;
//	}
//
//	public void setComprovanteRecebimentoRelControle(ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle) {
////		this.comprovanteRecebimentoRelControle = comprovanteRecebimentoRelControle;
//	}

	public void validarNumeroCartaoCredito() {
//		try {
//			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//			formaPagamentoNegociacaoRecebimentoVO.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorCodigoConfiguracaoFinanceiroCartao(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//			if (!getFacadeFactory().getGerenciamentoDeTransacaoCartaoDeCreditoFacade().validarNumeroCartaoCredito(formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao(), formaPagamentoNegociacaoRecebimentoVO.getOperadoraCartaoVO().getNome())) {
//				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValidarCampoNumeroCartaoCredito(false);
//				throw new Exception(UteisJSF.internacionalizar("msg_NumeroCartaoCreditoInvalido"));
//			} else {
//				formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValidarCampoNumeroCartaoCredito(true);
//				setMensagemID("msg_NumeroCartaoCreditoValido", Uteis.SUCESSO);
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public Boolean getDesativarBotaoCartaoCredito() {
		if (desativarBotaoCartaoCredito == null) {
			desativarBotaoCartaoCredito = false;
		}
		return desativarBotaoCartaoCredito;
	}

	public void setDesativarBotaoCartaoCredito(Boolean desativarBotaoCartaoCredito) {
		this.desativarBotaoCartaoCredito = desativarBotaoCartaoCredito;
	}

	public void consultarContaReceberAlunoRequerimento() {
//		try {
//			if(Uteis.isAtributoPreenchido(getRequerimentoVO().getContaReceber())){
//			setNegociacaoRecebimentoVO(new NegociacaoRecebimentoVO());
//			getConfiguracaoFinanceiroCartaoVOs().clear();
//			setQuantidadeCartao(0);
//			getNegociacaoRecebimentoVO().setMatricula(getRequerimentoVO().getMatricula().getMatricula());
//			getNegociacaoRecebimentoVO().setTipoPessoa(TipoPessoa.ALUNO.getValor());
//			ContaReceberVO contaReceberVO = new ContaReceberVO();
//			contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(getRequerimentoVO().getContaReceber(), false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado());
//			contaReceberVO.setEmissaoBloqueada(getFacadeFactory().getContaReceberFacade().verificaBloqueioEmissaoBoleto(contaReceberVO, getUsuarioLogado()));
//			boolean possuiPermissaoEmitirBoletoVencido = getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ImprimirBoletoVencidoVisaoAluno", getUsuarioLogado());
//			getFacadeFactory().getContaReceberFacade().validarTipoImpressaoPorContaReceber(contaReceberVO, possuiPermissaoEmitirBoletoVencido, getUsuarioLogado());
//			ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
//			contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceberVO);
//			contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberVO.getCalcularValorFinal( getUsuarioLogado()));
//			getNegociacaoRecebimentoVO().adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
//			getNegociacaoRecebimentoVO().setValorTotal(getNegociacaoRecebimentoVO().getValorTotal() + contaReceberVO.getValorReceberCalculado());
//			setDesativarBotaoCartaoCredito(false);
//			if (!getRequerimentoVO().getTipoRequerimento().getBloquearRecebimentoCartaoCreditoOnline()) {
//				getRequerimentoVO().setPermitirRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getCodigo()).getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "usarrequerimento"));
//				if (getRequerimentoVO().getPermitirRecebimentoCartaoCreditoOnline()) {
//					montarUltimaMatriculaPeriodoAluno();
//					consultarConfiguracaoCartaoPermiteRecebimentoOnline();
//				}
//			}
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	private String modalPagamentoOnline;

	public String getModalPagamentoOnline() {
		if (modalPagamentoOnline == null) {
			modalPagamentoOnline = "";
		}
		return modalPagamentoOnline;
	}

	public void setModalPagamentoOnline(String modalPagamentoOnline) {
		this.modalPagamentoOnline = modalPagamentoOnline;
	}

//	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;
//	private NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	private Integer quantidadeCartao;
	private Boolean desativarBotaoCartaoCredito;

//	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
//		if (formaPagamentoNegociacaoRecebimentoVOs == null) {
//			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
//		}
//		return formaPagamentoNegociacaoRecebimentoVOs;
//	}
//
//	public void setFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
//		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
//	}
//
//	public NegociacaoRecebimentoVO getNegociacaoRecebimentoVO() {
//		if (negociacaoRecebimentoVO == null) {
//			negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
//		}
//		return negociacaoRecebimentoVO;
//	}
//
//	public void setNegociacaoRecebimentoVO(NegociacaoRecebimentoVO negociacaoRecebimentoVO) {
//		this.negociacaoRecebimentoVO = negociacaoRecebimentoVO;
//	}

	public void adicionarNovoCartaoCredito() {
//		try {
//			limparMensagem();
//			ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = (ConfiguracaoFinanceiroCartaoVO) context().getExternalContext().getRequestMap().get("itemConfiguracaoFinanceiroCartao");
//			setQuantidadeCartao(getQuantidadeCartao() + 1);
//			int parcelas = 1;
//			if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
//				Date menorDataVencimento = null;
//				for (ContaReceberNegociacaoRecebimentoVO c : getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs()) {
//					if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) &&
//							(menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
//						menorDataVencimento = c.getContaReceber().getDataVencimento();
//					}
//				}
//				parcelas = getConfiguracaoRecebimentoCartaoOnlineVO().getQtdeParcelasPermitida(getNegociacaoRecebimentoVO().getResiduo(), menorDataVencimento, getUsuarioLogado(), getNegociacaoRecebimentoVO().getListaTipoOrigemContaReceber());
//			}
//			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().adicionarNovoCartaoCredito(getNegociacaoRecebimentoVO(), configuracaoFinanceiroCartaoVO, parcelas, getQuantidadeCartao(), getUsuarioLogado());
//			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
//			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
//				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
//			}
//			setMensagemID("msg_PagamentoOnline_cartaoAdicionadoComSucesso", Uteis.SUCESSO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void removerCartaoCredito() {
//		try {
//			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().removerCartaoCredito(formaPagamentoNegociacaoRecebimentoVO, getNegociacaoRecebimentoVO(), getUsuarioLogado());
//			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void calcularTotalPago() {
//		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//		try {
//			getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().calcularTotalPago(getNegociacaoRecebimentoVO(), formaPagamentoNegociacaoRecebimentoVO, getUsuarioLogado());
//			setQuantidadeCartao(getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().size());
//			limparMensagem();
//		} catch (Exception e) {
//			formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(0.0);
//			getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
//			for (FormaPagamentoNegociacaoRecebimentoVO obj : getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs()) {
//				getNegociacaoRecebimentoVO().setValorTotalRecebimento(getNegociacaoRecebimentoVO().getValorTotalRecebimento() + obj.getValorRecebimento());
//			}
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public Integer getQuantidadeCartao() {
		if (quantidadeCartao == null) {
			quantidadeCartao = 0;
		}
		return quantidadeCartao;
	}

	public void setQuantidadeCartao(Integer quantidadeCartao) {
		this.quantidadeCartao = quantidadeCartao;
	}

	public void realizarRecebimentoBoletoBancario() {
//		try {
//			List<BoletoBancarioRelVO> boletoBancarioRelVOs = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, getRequerimentoVO().getContaReceber(), getRequerimentoVO().getMatricula().getMatricula(), "", "", "", 0, 0, null, null, 0, "aluno", 0, getUsuarioLogado(), "boletoAluno", 0,  getUsuarioLogado().getPessoa().getCodigo(), false, null, null);
//			getFacadeFactory().getBoletoBancarioRelFacade().realizarImpressaoPDF(boletoBancarioRelVOs, getSuperParametroRelVO(), getVersaoSistema(), "boleto", getUsuarioLogado());
//			realizarImpressaoRelatorio(getSuperParametroRelVO());
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void consultarConfiguracaoFinanceiroCartao() {
//		try {
//			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//			if (!formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo().equals(0)) {
//				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorChavePrimaria(formaPagamentoNegociacaoRecebimentoVO.getConfiguracaoFinanceiroCartaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//			} else {
//				formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(new ConfiguracaoFinanceiroCartaoVO());
//			}
//			esconderDicaCVCartaoCredito();
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

//	public void apresentarDicaCVCartaoCredito() {
//		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(true);
//	}
//
//	public void esconderDicaCVCartaoCredito() {
//		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = (FormaPagamentoNegociacaoRecebimentoVO) context().getExternalContext().getRequestMap().get("formaPagamentoItens");
//		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setApresentarDicaCVCartaoCredito(false);
//	}

	private String modalConfirmacaoPagamento;

	public String getModalConfirmacaoPagamento() {
		if (modalConfirmacaoPagamento == null) {
			modalConfirmacaoPagamento = "";
		}
		return modalConfirmacaoPagamento;
	}

	public void setModalConfirmacaoPagamento(String modalConfirmacaoPagamento) {
		this.modalConfirmacaoPagamento = modalConfirmacaoPagamento;
	}

	public Boolean getIsApresentarResiduoPagamentoOnline() {
		return false; //getNegociacaoRecebimentoVO().getResiduo() != 0.0;
	}

	public void selecionarFuncionarioResponsavelProximoTramite() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		setFuncionarioProximoTramite(obj);
	}

	/**
	 * @return the abrirOpcaoInformarFuncionarioProximoTramite
	 */
	public Boolean getAbrirOpcaoInformarFuncionarioProximoTramite() {
		if (abrirOpcaoInformarFuncionarioProximoTramite == null) {
			abrirOpcaoInformarFuncionarioProximoTramite = false;
		}
		return abrirOpcaoInformarFuncionarioProximoTramite;
	}

	public String getAutocompleteValorCurso() {
		if (autocompleteValorCurso == null) {
			autocompleteValorCurso = "";
		}
		return autocompleteValorCurso;
	}

	public void setAutocompleteValorCurso(String autocompleteValorCurso) {
		this.autocompleteValorCurso = autocompleteValorCurso;
	}

	public String getAutocompleteValorTurma() {
		if (autocompleteValorTurma == null) {
			autocompleteValorTurma = "";
		}
		return autocompleteValorTurma;
	}

	public void setAutocompleteValorTurma(String autocompleteValorTurma) {
		this.autocompleteValorTurma = autocompleteValorTurma;
	}

	public String getAutocompleteValorRequisitante() {
		if (autocompleteValorRequisitante == null) {
			autocompleteValorRequisitante = "";
		}
		return autocompleteValorRequisitante;
	}

	public void setAutocompleteValorRequisitante(String autocompleteValorRequisitante) {
		this.autocompleteValorRequisitante = autocompleteValorRequisitante;
	}

	public String getAutocompleteValorResponsavel() {
		if (autocompleteValorResponsavel == null) {
			autocompleteValorResponsavel = "";
		}
		return autocompleteValorResponsavel;
	}

	public void setAutocompleteValorResponsavel(String autocompleteValorResponsavel) {
		this.autocompleteValorResponsavel = autocompleteValorResponsavel;
	}

	private int getValorAutoComplete(String valor) {
		if (valor != null) {
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("^.*\\((-?\\d+)\\)[ \\t]*$");
			java.util.regex.Matcher m = p.matcher(valor);
			try {
				if (m.matches()) {
					// save the entity id in the managed bean and strip the
					// entity id from the suggested string
					valor = valor.substring(0, valor.lastIndexOf('('));
					return Integer.parseInt(m.group(1));
				}
			} catch (java.lang.NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * @param abrirOpcaoInformarFuncionarioProximoTramite
	 *            the abrirOpcaoInformarFuncionarioProximoTramite to set
	 */
	public void setAbrirOpcaoInformarFuncionarioProximoTramite(Boolean abrirOpcaoInformarFuncionarioProximoTramite) {
		this.abrirOpcaoInformarFuncionarioProximoTramite = abrirOpcaoInformarFuncionarioProximoTramite;
	}

	public void selecionarFuncionarioProximoTramite() {
		setFuncionarioProximoTramite((FuncionarioVO) getRequestMap().get("funcionario"));
	}

	public void limparFuncionarioProximoTramite() {
		setFuncionarioProximoTramite(null);
	}

	/**
	 * @return the funcionarioProximoTramite
	 */
	public FuncionarioVO getFuncionarioProximoTramite() {
		if (funcionarioProximoTramite == null) {
			funcionarioProximoTramite = new FuncionarioVO();
		}
		return funcionarioProximoTramite;
	}

	/**
	 * @param funcionarioProximoTramite
	 *            the funcionarioProximoTramite to set
	 */
	public void setFuncionarioProximoTramite(FuncionarioVO funcionarioProximoTramite) {
		this.funcionarioProximoTramite = funcionarioProximoTramite;
	}

	public String consultarVisaoProfessorCoordenador() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Consultar Requerimento", "Consultar Visão Professor/Coordenador");
				List<RequerimentoVO> requerimentoVOs = getFacadeFactory().getRequerimentoFacade().consultaRapidaRequerimentoVisaoProfessorCoordenador(0, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				getListaConsulta().clear();
				getListaConsultaAbertos().clear();
				getListaConsultaFinalizados().clear();
				for (RequerimentoVO requerimentoVO : requerimentoVOs) {
					if (requerimentoVO.getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
						if ((!requerimentoVO.getSituacao().equals("FD") && !requerimentoVO.getSituacao().equals("FI")) || requerimentoVO.getSituacaoFinanceira().equals("PE")) {
							getListaConsultaAbertos().add(requerimentoVO);
						}
						if ((requerimentoVO.getSituacao().equals("FD") || requerimentoVO.getSituacao().equals("FI")) && !requerimentoVO.getSituacaoFinanceira().equals("PE")) {
							getListaConsultaFinalizados().add(requerimentoVO);
						}
					} else {
						getListaConsulta().add(requerimentoVO);
					}
				}
				realizarAtualizacaoQtdeRequerimentoProfessorCoordenador();
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoProfessorCons.xhtml");
		} else {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoCoordenadorCons.xhtml");
		}
	}

	/**
	 * @return the existeTipoRequerimentoCadastradoProfessorCoordenador
	 */
	public Boolean getExisteTipoRequerimentoCadastradoProfessorCoordenador() {
		if (existeTipoRequerimentoCadastradoProfessorCoordenador == null) {
			existeTipoRequerimentoCadastradoProfessorCoordenador = false;
		}
		return existeTipoRequerimentoCadastradoProfessorCoordenador;
	}

	/**
	 * @param existeTipoRequerimentoCadastradoProfessorCoordenador
	 *            the existeTipoRequerimentoCadastradoProfessorCoordenador to
	 *            set
	 */
	public void setExisteTipoRequerimentoCadastradoProfessorCoordenador(Boolean existeTipoRequerimentoCadastradoProfessorCoordenador) {
		this.existeTipoRequerimentoCadastradoProfessorCoordenador = existeTipoRequerimentoCadastradoProfessorCoordenador;
	}

	public Boolean getIsRequerimentoPessoaLogada() {
		return getRequerimentoVO().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo());
	}
	
	public Boolean getIsResponsavelPessoaLogada() {
		return getRequerimentoVO().getFuncionarioVO().getPessoa().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo());
	}

	/**
	 * @return the listaSelectItemTipoPessoa
	 */
	public List<SelectItem> getListaSelectItemTipoPessoa() {
		if (listaSelectItemTipoPessoa == null) {
			listaSelectItemTipoPessoa = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoPessoa;
	}

	/**
	 * @param listaSelectItemTipoPessoa
	 *            the listaSelectItemTipoPessoa to set
	 */
	public void setListaSelectItemTipoPessoa(List<SelectItem> listaSelectItemTipoPessoa) {
		this.listaSelectItemTipoPessoa = listaSelectItemTipoPessoa;
	}

	public void inicializarTipoPessoaPorTipoRequerimento() {
		getListaSelectItemTipoPessoa().clear();
		if (!getRequerimentoVO().isNovoObj()) {
			getListaSelectItemTipoPessoa().add(new SelectItem(getRequerimentoVO().getTipoPessoa(), getRequerimentoVO().getTipoPessoa().getDescricao()));
		} else if (Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCodigo())) {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				getRequerimentoVO().setTipoPessoa(TipoPessoa.REQUERENTE);
				getListaSelectItemTipoPessoa().add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao()));
			} else {
				getListaSelectItemTipoPessoa().add(new SelectItem(TipoPessoa.ALUNO, TipoPessoa.ALUNO.getDescricao()));
				if (getRequerimentoVO().getTipoRequerimento().getRequerimentoVisaoProfessor() || getRequerimentoVO().getTipoRequerimento().getRequerimentoVisaoCoordenador() || getRequerimentoVO().getTipoRequerimento().getRequerimentoVisaoFuncionario() || getRequerimentoVO().getTipoRequerimento().getRequerimentoMembroComunidade() || TiposRequerimento.TRANSF_ENTRADA.getValor().equals(getRequerimentoVO().getTipoRequerimento().getTipo())) {
					getListaSelectItemTipoPessoa().add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao()));
				} else {
					getRequerimentoVO().setTipoPessoa(TipoPessoa.ALUNO);
				}
			}
		} else {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				getRequerimentoVO().setTipoPessoa(TipoPessoa.REQUERENTE);
				getListaSelectItemTipoPessoa().add(new SelectItem(TipoPessoa.REQUERENTE, TipoPessoa.REQUERENTE.getDescricao()));
			} else {
				getListaSelectItemTipoPessoa().add(new SelectItem(TipoPessoa.ALUNO, TipoPessoa.ALUNO.getDescricao()));
				getRequerimentoVO().setTipoPessoa(TipoPessoa.ALUNO);
			}
		}
	}

	public boolean getIsApresentarTipoRequerente() {
		return getRequerimentoVO().isNovoObj() && getListaSelectItemTipoPessoa().size() > 1;
	}

	public void realizarAtualizacaoQtdeRequerimentoProfessorCoordenador() {
//		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
//			getVisaoCoordenadorControle().consultarQtdeAtualizacaoRequerimentoCoordenador();
//		} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
//			getVisaoProfessorControle().consultarQtdeAtualizacaoRequerimentoProfessor();
//		}
	}

	/**
	 * @return the listaSelectItemDisciplina
	 */
	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	/**
	 * @param listaSelectItemDisciplina
	 *            the listaSelectItemDisciplina to set
	 */
	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public void montarListaSelectItemDisciplina(ConfiguracaoAcademicoVO configuracaoAcademico) {
		getListaSelectItemDisciplina().clear();
		List<DisciplinaVO> disciplinaVOs = null;
		try {
			disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorMatriculaAptoVincularRequerimento(getRequerimentoVO().getMatricula().getMatricula(), "", "", getRequerimentoVO().getTipoRequerimento(), getRequerimentoVO().getMatricula().getCurso().getConfiguracaoAcademico());
			getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			for (DisciplinaVO disciplinaVO : disciplinaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getAbreviatura() + " - " + disciplinaVO.getNome()));				
			}
			if (Uteis.isAtributoPreenchido(getListaSelectItemDisciplina())) {
				getRequerimentoVO().getDisciplina().setCodigo((Integer) getListaSelectItemDisciplina().get(0).getValue());
				executarVerificacaoProfessorMinistrouAula();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		} finally {
			Uteis.liberarListaMemoria(disciplinaVOs);
		}
	}

	public String realizarNavegacaoAtividadeDiscursiva() {
		try {
			if (!getRequerimentoVO().getTipoPessoaAluno()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Requerimento_atividadeDiscursivaSemAluno"));
			}
			context().getExternalContext().getSessionMap().put("requerimentoAtividadeDiscursiva", getRequerimentoVO());
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				return "";
			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				return "atividadeDiscursivaProfessorForm";
			}
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return "";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";

	}

	public Boolean getIsTipoAluno() {
		return getRequerimentoVO().getMatricula().getAluno().getCodigo().intValue() > 0;
	}

	/**
	 * @author Victor Hugo de Paula Costa 23/02/2016 15:12
	 */
//	private List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs;
//
//	public List<ConfiguracaoFinanceiroCartaoVO> getConfiguracaoFinanceiroCartaoVOs() {
//		if (configuracaoFinanceiroCartaoVOs == null) {
//			configuracaoFinanceiroCartaoVOs = new ArrayList<ConfiguracaoFinanceiroCartaoVO>(0);
//		}
//		return configuracaoFinanceiroCartaoVOs;
//	}
//
//	public void setConfiguracaoFinanceiroCartaoVOs(List<ConfiguracaoFinanceiroCartaoVO> configuracaoFinanceiroCartaoVOs) {
//		this.configuracaoFinanceiroCartaoVOs = configuracaoFinanceiroCartaoVOs;
//	}
//
//	public int getRetornarTamanhoListaConfiguracaoFinanceiroCartaoVOs() {
//		if (!getConfiguracaoFinanceiroCartaoVOs().isEmpty())
//			return getConfiguracaoFinanceiroCartaoVOs().size();
//		return 0;
//	}
//
//	public void incicializarDadosPagamentoOnline() {
//		limparMensagem();
//		getNegociacaoRecebimentoVO().getFormaPagamentoNegociacaoRecebimentoVOs().clear();
//		getNegociacaoRecebimentoVO().setValorTotalRecebimento(0.0);
//	}
//
//	/**
//	 * @author Victor Hugo de Paula Costa 18/03/2016 08:29
//	 */
//	private ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO;
//
//	public ConfiguracaoRecebimentoCartaoOnlineVO getConfiguracaoRecebimentoCartaoOnlineVO() {
//		if (configuracaoRecebimentoCartaoOnlineVO == null) {
//			configuracaoRecebimentoCartaoOnlineVO = new ConfiguracaoRecebimentoCartaoOnlineVO();
//		}
//		return configuracaoRecebimentoCartaoOnlineVO;
//	}
//
//	public void setConfiguracaoRecebimentoCartaoOnlineVO(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO) {
//		this.configuracaoRecebimentoCartaoOnlineVO = configuracaoRecebimentoCartaoOnlineVO;
//	}

	public void consultarConfiguracaoCartaoPermiteRecebimentoOnline() {
		try {
//			setConfiguracaoRecebimentoCartaoOnlineVO(new ConfiguracaoRecebimentoCartaoOnlineVO());
			TipoNivelEducacional tipoNivelEducacional = TipoNivelEducacional.getEnum(getRequerimentoVO().getMatricula().getCurso().getNivelEducacional());
//			setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(getRequerimentoVO().getMatriculaPeriodoVO().getTurma().getCodigo(), getRequerimentoVO().getMatricula().getCurso().getCodigo(), tipoNivelEducacional != null ? tipoNivelEducacional.getValor() : "", getRequerimentoVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			verificarContasRecbimentoOnline();
		} catch (Exception e) {
			getRequerimentoVO().setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarContasRecbimentoOnline() {
//		try {
//			if (!getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
//				ConsistirException consistirException = new ConsistirException();
//				getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().verificarContasRecebimentoOnline(getNegociacaoRecebimentoVO().getContaReceberNegociacaoRecebimentoVOs(),  consistirException, true, false, false, false, false, false, getUsuarioLogado());
//				if (!consistirException.getListaMensagemErro().isEmpty()) {
//					getRequerimentoVO().setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
//					return;
//				}
//				consultarCartoesParaRecebimentoOnline();
//			} else {
//				getRequerimentoVO().setPermitirRecebimentoCartaoCreditoOnline(Boolean.FALSE);
//			}
//		} catch (ConsistirException e) {
//			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}

	public void consultarCartoesParaRecebimentoOnline() {
//		try {
//			getConfiguracaoFinanceiroCartaoVOs().clear();
//			getConfiguracaoFinanceiroCartaoVOs().addAll(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRequerimentoVO().getUnidadeEnsino().getCodigo()).getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "", getTipoCartao().name(), getUsuarioLogado()));
//			getNegociacaoRecebimentoVO().setConfiguracaoRecebimentoCartaoOnlineVO(getConfiguracaoRecebimentoCartaoOnlineVO());
//			ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = new ConfiguracaoFinanceiroCartaoVO();
//			configuracaoFinanceiroCartaoVO.setBoletoBancario(true);
//			getConfiguracaoFinanceiroCartaoVOs().add(configuracaoFinanceiroCartaoVO);
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
	}
	
	public boolean getIsExibirScrollCartoes() {
		return false; // getConfiguracaoFinanceiroCartaoVOs().size() > 7;
	}

	public String getProfessorMinistrouAula() {
		if (professorMinistrouAula == null) {
			professorMinistrouAula = "";
		}
		return professorMinistrouAula;
	}

	public void setProfessorMinistrouAula(String professorMinistrouAula) {
		this.professorMinistrouAula = professorMinistrouAula;
	}

	public void executarVerificacaoProfessorMinistrouAula() {
		try {
			setProfessorMinistrouAula("");
			if (getRequerimentoVO().getTipoRequerimento().getIsPermiteInformarDisciplina() && Uteis.isAtributoPreenchido(getRequerimentoVO().getDisciplina())) {
				setProfessorMinistrouAula(getFacadeFactory().getRequerimentoFacade().executarVerificacaoProfessorMinistrouAula(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getDisciplina().getCodigo(), false, getUsuarioLogado()));
			}
			if(getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao() && getRequerimentoVO().isNovoObj()) {
				consultarTurmaParaReposicaoDisciplina();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemUnidadeEnsinoTransferenciaInterna() {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCursoAtivo(getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarUnidadeEnsino() ? 0 : getRequerimentoVO().getMatricula().getUnidadeEnsino().getCodigo(),
					getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarCurso() ? 0 : getRequerimentoVO().getMatricula().getCurso().getCodigo(),
					getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarCurso() ? 0 : getRequerimentoVO().getMatricula().getTurno().getCodigo(),
					getRequerimentoVO().getTipoRequerimento().getValidarVagasPorNumeroComputadoresUnidadeEnsino(),
					getRequerimentoVO().getTipoRequerimento().getValidarVagasPorNumeroComputadoresConsiderandoCurso(),
					getRequerimentoVO().getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? getRequerimentoVO().getTipoRequerimento().getAno() : getRequerimentoVO().getMatriculaPeriodoVO().getAno(),
					getRequerimentoVO().getTipoRequerimento().getRegistrarTransferenciaProximoSemestre() ? getRequerimentoVO().getTipoRequerimento().getSemestre() : getRequerimentoVO().getMatriculaPeriodoVO().getSemestre(),
							getRequerimentoVO().getMatricula().getDiaSemanaAula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado() );
			setListaSelectItemUnidadeEnsinoTransferencia(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUsuarioPossuiPermissaoFuncionalidade() {
		try {
			if (getTipoFuncionalidadeValidar().equals("PermitirInformarDesconto") || getTipoFuncionalidadeValidar().equals("PermitirInformarAcrescimo")) {
				String mensagem = "";
				getFacadeFactory().getRequerimentoFacade().executarVerificacaoUsuarioPossuiPermissaoInformarDescontoAcrescimo(getRequerimentoVO(), getTipoFuncionalidadeValidar().equals("PermitirInformarDesconto"), getTipoDescontoTemp(), getValorAcrescimoDescontoTemp(), getUsuarioLiberarOperacaoFuncionalidade(), getSenhaLiberarOperacaoFuncionalidade(), getOperacaoFuncionalidadeVOs());
				if (!getOperacaoFuncionalidadeVOs().isEmpty()) {
					for (OperacaoFuncionalidadeVO obj : getOperacaoFuncionalidadeVOs()) {
						mensagem += obj.getObservacao();
					}
					setMensagemID(mensagem, Uteis.SUCESSO, true);
				}
				limparMensagem();
			} else if (getTipoFuncionalidadeValidar().equals("PermitirVoltarRequerimentoExecucao")) {
				realizarAlteracaoRequerimentoParaEmExecucao();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			setUsuarioLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setValorAcrescimoDescontoTemp(0.0);
			setTipoDescontoTemp("");
		}
	}
	
	private void executarVerificacaoUsuarioPossuiPermissaoAlterarAcrescimoDesconto() {
		setPermitirInformarAcrescimo(executarVerificacaoUsuarioPossuiPermissaoFuncionalidade("PermitirInformarAcrescimo"));
		setPermitirInformarDesconto(executarVerificacaoUsuarioPossuiPermissaoFuncionalidade("PermitirInformarDesconto"));
	}

	private boolean executarVerificacaoUsuarioPossuiPermissaoFuncionalidade(String funcionalidade) {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(funcionalidade, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getUsuarioLiberarOperacaoFuncionalidade() {
		if (usuarioLiberarOperacaoFuncionalidade == null) {
			usuarioLiberarOperacaoFuncionalidade = "";
		}
		return usuarioLiberarOperacaoFuncionalidade;
	}

	public void setUsuarioLiberarOperacaoFuncionalidade(String usuarioLiberarOperacaoFuncionalidade) {
		this.usuarioLiberarOperacaoFuncionalidade = usuarioLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}

	/**
	 * @return the tipoFuncionalidadeValidar
	 */
	public String getTipoFuncionalidadeValidar() {
		if (tipoFuncionalidadeValidar == null) {
			tipoFuncionalidadeValidar = "";
		}
		return tipoFuncionalidadeValidar;
	}

	/**
	 * @param tipoFuncionalidadeValidar
	 *            the tipoFuncionalidadeValidar to set
	 */
	public void setTipoFuncionalidadeValidar(String tipoFuncionalidadeValidar) {
		this.tipoFuncionalidadeValidar = tipoFuncionalidadeValidar;
	}

	public Double getValorAcrescimoDescontoTemp() {
		if (valorAcrescimoDescontoTemp == null) {
			valorAcrescimoDescontoTemp = 0.0;
		}
		return valorAcrescimoDescontoTemp;
	}

	public void setValorAcrescimoDescontoTemp(Double valorAcrescimoDescontoTemp) {
		this.valorAcrescimoDescontoTemp = valorAcrescimoDescontoTemp;
	}

	public String getTipoDescontoTemp() {
		if (tipoDescontoTemp == null) {
			tipoDescontoTemp = "";
		}
		return tipoDescontoTemp;
	}

	public void setTipoDescontoTemp(String tipoDescontoTemp) {
		this.tipoDescontoTemp = tipoDescontoTemp;
	}

	public boolean isPermitirInformarDesconto() {
		return permitirInformarDesconto;
	}

	public void setPermitirInformarDesconto(boolean permitirInformarDesconto) {
		this.permitirInformarDesconto = permitirInformarDesconto;
	}

	public boolean isPermitirInformarAcrescimo() {
		return permitirInformarAcrescimo;
	}

	public void setPermitirInformarAcrescimo(boolean permitirInformarAcrescimo) {
		this.permitirInformarAcrescimo = permitirInformarAcrescimo;
	}

	private void alterarTipoDesconto() {
		if (getRequerimentoVO().getTipoDesconto().equals("VA") && getRequerimentoVO().getPercDesconto() > 0) {
			getRequerimentoVO().setValorDesconto(getRequerimentoVO().getPercDesconto());
			getRequerimentoVO().setPercDesconto(0.0);
		} else if (getRequerimentoVO().getTipoDesconto().equals("PO") && getRequerimentoVO().getValorDesconto() > 0) {
			getRequerimentoVO().setPercDesconto(getRequerimentoVO().getValorDesconto());
			getRequerimentoVO().setValorDesconto(0.0);
		}
	}

	public List<OperacaoFuncionalidadeVO> getOperacaoFuncionalidadeVOs() {
		if (operacaoFuncionalidadeVOs == null) {
			operacaoFuncionalidadeVOs = new ArrayList<OperacaoFuncionalidadeVO>(0);
		}
		return operacaoFuncionalidadeVOs;
	}

	public void setOperacaoFuncionalidadeVOs(List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs) {
		this.operacaoFuncionalidadeVOs = operacaoFuncionalidadeVOs;
	}
	

	public void realizarAlteracaoRequerimentoParaEmExecucao() throws Exception{
		UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsuarioLiberarOperacaoFuncionalidade(), getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
		getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirVoltarRequerimentoExecucao", usuarioVerif);
		if (getRequerimentoVO().getIsDeferido() && getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao() && Uteis.isAtributoPreenchido(getRequerimentoVO().getDisciplina()) && Uteis.isAtributoPreenchido(getRequerimentoVO().getTurmaReposicao())) {
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(
							getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getTurmaReposicao(),
							getRequerimentoVO().getDisciplina().getCodigo(),
							getRequerimentoVO().getMatriculaPeriodoVO().getAno(),
							getRequerimentoVO().getMatriculaPeriodoVO().getSemestre(),
							getRequerimentoVO().getMatricula().getGradeCurricularAtual().getCodigo(), false, false,
							getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
				throw new Exception("Não é possível estornar este requerimento pois a disciplina já está incluida para o aluno, neste caso é necessário excluir o histórico desta disciplina para prosseguir com esta operação.");
			}
		}
		getFacadeFactory().getRequerimentoFacade().validarDadosBloqueioRequerimentoAbertoSimultaneo(getRequerimentoVO(), getUsuarioLogadoClone());
		if (getRequerimentoVO().getRequerimentoHistoricoVOs().isEmpty()) {
			getFacadeFactory().getRequerimentoFacade().realizarInicializacaoDepartamentoEResponsavelRequerimento(getRequerimentoVO().getCodigo(), false, "EX", getUsuarioLogado());
			getRequerimentoVO().setRequerimentoHistoricoVOs(getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoRequerimento(getRequerimentoVO().getCodigo(), false, getUsuarioLogado()));
			if (!getRequerimentoVO().getRequerimentoHistoricoVOs().isEmpty()) {
				RequerimentoHistoricoVO requerimentoHistoricoVO = getRequerimentoVO().getRequerimentoHistoricoVOs().get(0);
			}
		}
		getFacadeFactory().getRequerimentoFacade().alterarSituacaoRequerimento(getRequerimentoVO().getCodigo(), "EX", usuarioVerif);
//		getFacadeFactory().getRequerimentoFacade().reativarContaReceberRequerimento(getRequerimentoVO(), usuarioVerif);
		getRequerimentoVO().setSituacao("EX");
		getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUERIMENTO, getRequerimentoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.REQUERIMENTO_VOLTAR_SITUACAO_EXECUCAO, usuarioVerif, ""));
		getRequerimentoVO().setPodeSerIniciadoExecucaoDepartamentoAtual(null);
		setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
	}
	
	public void realizarInclusaoDisciplinaRequerimento() {
		RequerimentoVO obj = new RequerimentoVO();
		obj = getRequerimentoVO();
//		setRequerimentoVO(null);
		context().getExternalContext().getSessionMap().put("requerimentoInclusaoExclusaoDisciplina", obj);
	}
	
	public Boolean getApresentarBotaoInclusaoDisciplina() {
		return ((getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString()) 
				|| getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.REPOSICAO.getValor().toString())) 
				&& getRequerimentoVO().getSituacao().equals(SituacaoRequerimento.EM_EXECUCAO.getValor())
				&& getPermiteIncluirDisciplina());
	}
	
	public void selecionarDisciplinaPorCodigo() {
		consultarDisciplinaPorCodigo(getValorAutoComplete(getAutocompleteValorDisciplina()));
	}

	public void consultarDisciplinaPorCodigo(int codigo) {
		try {
			DisciplinaVO obj = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getRequerimentoConsVO().setDisciplina(obj);
			consultarDadosGrafico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getAutocompleteValorDisciplina() {
		if (autocompleteValorDisciplina == null) {
			autocompleteValorDisciplina = "";
		}
		return autocompleteValorDisciplina;
	}

	public void setAutocompleteValorDisciplina(String autocompleteValorDisciplina) {
		this.autocompleteValorDisciplina = autocompleteValorDisciplina;
	}
	
	
	
	public void montarUltimaMatriculaPeriodoAluno() {
		if(Uteis.isAtributoPreenchido(getRequerimentoVO().getMatricula().getMatricula()) && !Uteis.isAtributoPreenchido(getRequerimentoVO().getMatriculaPeriodoVO())) {
			try {
				getRequerimentoVO().setMatriculaPeriodoVO(new MatriculaPeriodoVO());
				getRequerimentoVO().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado()));				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Boolean getDisciplinaVazia() {
		if (disciplinaVazia == null) {
			disciplinaVazia = Boolean.FALSE;
		}
		return disciplinaVazia;
	}

	public void setDisciplinaVazia(boolean disciplinaVazia) {
		this.disciplinaVazia = disciplinaVazia;
	}

	public Boolean permiteAbrirRequerimentoForaDoPrazo;
	public UsuarioVO usuarioLiberarRequerimentoForaDoPrazo;
	
	public void autenticarUsuario() {
		try {
            UsuarioVO usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getUsuarioLiberarRequerimentoForaDoPrazo().getUsername(), this.getUsuarioLiberarRequerimentoForaDoPrazo().getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
            verificarPermissaoAbrirRequerimentoForaDoPrazo(usuarioVerif);
            if (!getPermiteAbrirRequerimentoForaDoPrazo()) {
            	throw new Exception("Usuário informado não possui permissão para realizar essa funcionalidade!");
            }
            montarListaSelectItemTipoRequerimento("");
           setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void verificarPermissaoAbrirRequerimentoForaDoPrazo(UsuarioVO usuario){
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAbrirRequerimentoForaPrazo", usuario);
			setPermiteAbrirRequerimentoForaDoPrazo(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteAbrirRequerimentoForaDoPrazo(Boolean.FALSE);
		}
	}

	public UsuarioVO getUsuarioLiberarRequerimentoForaDoPrazo() {
		if(usuarioLiberarRequerimentoForaDoPrazo == null){
			usuarioLiberarRequerimentoForaDoPrazo = new UsuarioVO();
		}
		return usuarioLiberarRequerimentoForaDoPrazo;
	}

	public void setUsuarioLiberarRequerimentoForaDoPrazo(UsuarioVO usuarioLiberarRequerimentoForaDoPrazo) {
		this.usuarioLiberarRequerimentoForaDoPrazo = usuarioLiberarRequerimentoForaDoPrazo;
	}

	public Boolean getPermiteAbrirRequerimentoForaDoPrazo() {
		if (permiteAbrirRequerimentoForaDoPrazo == null) {
			permiteAbrirRequerimentoForaDoPrazo = Boolean.FALSE;
		}
		return permiteAbrirRequerimentoForaDoPrazo;
	}

	public void setPermiteAbrirRequerimentoForaDoPrazo(Boolean permiteAbrirRequerimentoForaDoPrazo) {
		this.permiteAbrirRequerimentoForaDoPrazo = permiteAbrirRequerimentoForaDoPrazo;
	}
	
	public Boolean getPermitirApresentarBotaoEnviarDepartamentoAnterior() {
		if (permitirApresentarBotaoEnviarDepartamentoAnterior == null) {
			permitirApresentarBotaoEnviarDepartamentoAnterior = Boolean.FALSE;
		}
		return permitirApresentarBotaoEnviarDepartamentoAnterior;
	}

	
	public void setPermitirApresentarBotaoEnviarDepartamentoAnterior(
			Boolean permitirApresentarBotaoEnviarDepartamentoAnterior) {
		this.permitirApresentarBotaoEnviarDepartamentoAnterior = permitirApresentarBotaoEnviarDepartamentoAnterior;
	}
	
	public void verificarPermissaoEnviarDepartamentoAnterior() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_PermitirEnviarDepartamentoAnterior", getUsuarioLogado());
			if (getRequerimentoVO().getCodigo().intValue() > 0) {
				setPermitirApresentarBotaoEnviarDepartamentoAnterior(Boolean.TRUE);
			}
		} catch (Exception e) {
			setPermitirApresentarBotaoEnviarDepartamentoAnterior(Boolean.FALSE);
		}
	}
	

	public Boolean getFuncionarioTramiteCoordenadorEspecifico() {
		if(funcionarioTramiteCoordenadorEspecifico == null){
			funcionarioTramiteCoordenadorEspecifico = false;
		}
		return funcionarioTramiteCoordenadorEspecifico;
	}

	public void setFuncionarioTramiteCoordenadorEspecifico(Boolean funcionarioTramiteCoordenadorEspecifico) {
		this.funcionarioTramiteCoordenadorEspecifico = funcionarioTramiteCoordenadorEspecifico;
	}
	

	public List<SelectItem> getListaSelectItemCoordenadorEspecifico() {
		if(listaSelectItemCoordenadorEspecifico == null){
			listaSelectItemCoordenadorEspecifico = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCoordenadorEspecifico;
	}

	public void setListaSelectItemCoordenadorEspecifico(List<SelectItem> listaSelectItemCoordenadorEspecifico) {
		this.listaSelectItemCoordenadorEspecifico = listaSelectItemCoordenadorEspecifico;
	}
	
	
	

	public ImpressaoContratoVO getImpressaoContratoFiltro() {
		if(impressaoContratoFiltro == null){
			impressaoContratoFiltro = new ImpressaoContratoVO();
		}
		return impressaoContratoFiltro;
	}

	public void setImpressaoContratoFiltro(ImpressaoContratoVO impressaoContratoFiltro) {
		this.impressaoContratoFiltro = impressaoContratoFiltro;
	}

	public void consultarCoordenadorEspecificoProximoTramite() throws Exception{
		List<PessoaVO> coordenadores = getFacadeFactory().getPessoaFacade().consultarCoordenadorCursoUnidadeEnsino(0, 0, true, getUsuarioLogado());
		setListaSelectItemCoordenadorEspecifico(UtilSelectItem.getListaSelectItem(coordenadores, "codigo", "nome"));
	}
	

	public Boolean getPermiteDeferir() {
		if(permiteDeferir == null){
			try{
				getFacadeFactory().getControleAcessoFacade().excluir("Requerimento_PermitirDeferir", getUsuarioLogado());
				permiteDeferir = true;
			}catch(Exception e){
				permiteDeferir = false;
			}
		}
		return permiteDeferir;
	}

	public void setPermiteDeferir(Boolean permiteDeferir) {
		this.permiteDeferir = permiteDeferir;
	}

	public Boolean getPermiteIndeferir() {
		if(permiteIndeferir == null){
			try{
				getFacadeFactory().getControleAcessoFacade().excluir("Requerimento_PermitirIndeferir", getUsuarioLogado());
				permiteIndeferir = true;
			}catch(Exception e){
				permiteIndeferir = false;
			}
		}
		return permiteIndeferir;
	}

	public void setPermiteIndeferir(Boolean permiteIndeferir) {
		this.permiteIndeferir = permiteIndeferir;
	}

	public Boolean getPermiteImprimirComprovante() {
		if(permiteImprimirComprovante == null){
			try{
				getFacadeFactory().getControleAcessoFacade().excluir("Requerimento_PermitirImprimirComprovante", getUsuarioLogado());
				permiteImprimirComprovante = true;
			}catch(Exception e){
				permiteImprimirComprovante = false;
			}
		}
		return permiteImprimirComprovante;
	}

	public void setPermiteImprimirComprovante(Boolean permiteImprimirComprovante) {
		this.permiteImprimirComprovante = permiteImprimirComprovante;
	}

	public Boolean getPermiteImprimirRequerimento() {
		if(permiteImprimirRequerimento == null){
			try{
				getFacadeFactory().getControleAcessoFacade().excluir("Requerimento_PermitirImprimirRequerimento", getUsuarioLogado());
				permiteImprimirRequerimento = true;
			}catch(Exception e){
				permiteImprimirRequerimento = false;
			}
		}
		return permiteImprimirRequerimento;
	}

	public void setPermiteImprimirRequerimento(Boolean permiteImprimirRequerimento) {
		this.permiteImprimirRequerimento = permiteImprimirRequerimento;
	}

	public Boolean getPermiteExcluir() {
		if(permiteExcluir == null){	
				try{
					getFacadeFactory().getControleAcessoFacade().excluir("Requerimento", getUsuarioLogado());
					permiteExcluir = true;
				}catch(Exception e){
					permiteExcluir = false;
				}			
		}
		return permiteExcluir;
	}

	public void setPermiteExcluir(Boolean permiteExcluir) {
		this.permiteExcluir = permiteExcluir;
	}

	public Boolean getPermiteNovo() {
		if(permiteNovo == null){
			try{
				if(getRequerimentoVO().isNovoObj()){
					getFacadeFactory().getControleAcessoFacade().incluir("Requerimento", getUsuarioLogado());
				}
				permiteNovo = true;
			}catch(Exception e){
				permiteNovo = false;
			}
		}
		return permiteNovo;
	}

	public void setPermiteNovo(Boolean permiteNovo) {		
		this.permiteNovo = permiteNovo;
	}

	public Boolean getPermiteGravar() {
		if(permiteGravar == null){
			try{
				if(getRequerimentoVO().isNovoObj()){
					getFacadeFactory().getControleAcessoFacade().incluir("Requerimento", getUsuarioLogado());
				}else{
					getFacadeFactory().getControleAcessoFacade().alterar("Requerimento", getUsuarioLogado());
				}
				permiteGravar = true;
			}catch(Exception e){
				permiteGravar = false;
			}
		}
		return permiteGravar;
	}

	public void setPermiteGravar(Boolean permiteGravar) {
		this.permiteGravar = permiteGravar;
	}

	public Boolean getPermiteIncluirDisciplina() {
		if(permiteIncluirDisciplina == null){
			try{
				getFacadeFactory().getControleAcessoFacade().incluir("InclusaoExclusaoDisciplina", getUsuarioLogado());
				permiteIncluirDisciplina = true;
			}catch(Exception e){
				permiteIncluirDisciplina = false;
			}
		}
		return permiteIncluirDisciplina;
	}

	public void setPermiteIncluirDisciplina(Boolean permiteIncluirDisciplina) {
		this.permiteIncluirDisciplina = permiteIncluirDisciplina;
	}

	public Boolean getPermiteIniciarRequerimento() {
		if(permiteIniciarRequerimento == null){
			try{
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_ApresentarIniciarRequerimento", getUsuarioLogado());
				permiteIniciarRequerimento = true;
			}catch(Exception e){
				permiteIniciarRequerimento = false;
			}
		}
		return permiteIniciarRequerimento;
	}

	public void setPermiteIniciarRequerimento(Boolean permiteIniciarRequerimento) {
		this.permiteIniciarRequerimento = permiteIniciarRequerimento;
	}

	public Boolean getPermiteConsultar() {
		if(permiteConsultar == null){
			try{
				getFacadeFactory().getControleAcessoFacade().consultar("Requerimento", true, getUsuarioLogado());
				permiteConsultar = true;
			}catch(Exception e){
				permiteConsultar = false;
			}
		}
		return permiteConsultar;
	}

	public void setPermiteConsultar(Boolean permiteConsultar) {
		this.permiteConsultar = permiteConsultar;
	}
	

	public boolean getIsPermiteImprimirComprovante(){
		return !getRequerimentoVO().isNovoObj() && getPermiteImprimirComprovante();
	}
	
	public void realizarImpressaoCertificadoModular(RequerimentoVO requerimento, ImpressaoContratoVO contratoGravar) throws Exception{
		CertificadoCursoExtensaoRelControle certificadoCursoExtensaoRelControle = new CertificadoCursoExtensaoRelControle();
		certificadoCursoExtensaoRelControle.setMatriculaVO(requerimento.getMatricula());				
		certificadoCursoExtensaoRelControle.setTipoLayout("TextoPadrao");
		certificadoCursoExtensaoRelControle.setTextoPadraoDeclaracao(requerimento.getTipoRequerimento().getTextoPadrao().getCodigo());
		certificadoCursoExtensaoRelControle.setFiltro("aluno");
		certificadoCursoExtensaoRelControle.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricularDisciplina( requerimento.getDisciplina().getCodigo(), requerimento.getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		certificadoCursoExtensaoRelControle.getGradeDisciplinaVO().setDisciplina(requerimento.getDisciplina());
		certificadoCursoExtensaoRelControle.setRequerimentoVO(getRequerimentoVO());
		certificadoCursoExtensaoRelControle.setGerarNovoArquivoAssinado(contratoGravar.getGerarNovoArquivoAssinado());
		certificadoCursoExtensaoRelControle.setTrazerTodasSituacoesAprovadas(Boolean.TRUE);
		List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
		certificadoCursoExtensaoRelControle.setTipoDisciplinaPeriodoSelecionado("DisciplinaDoPeriodoSelecionado");
		certificadoCursoExtensaoRelControle.montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
		setVisualizarCertificado(false);
		setCaminhoRelatorio("");
		setFazerDownload(false);
		if(certificadoCursoExtensaoRelControle.getImpressaoContratoExistente()){
			getImpressaoContratoFiltro().setImpressaoContratoExistente(true);
			setCaminhoRelatorio(certificadoCursoExtensaoRelControle.getCaminhoRelatorio());
			setFazerDownload(false);
			setImprimirVisaoAluno(false);
		}else if(certificadoCursoExtensaoRelControle.getVisualizarCertificado()){
			setVisualizarCertificado(certificadoCursoExtensaoRelControle.getVisualizarCertificado());
		}else if(certificadoCursoExtensaoRelControle.getFazerDownload()){			
		   setFazerDownload(certificadoCursoExtensaoRelControle.getFazerDownload());
		   setCaminhoRelatorio(certificadoCursoExtensaoRelControle.getCaminhoRelatorio());   
		}
	}
	
	
    public void realizarCarregamentoRequerimentoVindoTelaFichaAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
    	MatriculaVO obj = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAlunoParaRequerimento");
    	if (obj != null && !obj.getMatricula().equals("")) {
    		try {
    			getRequerimentoVO().getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
        		getRequerimentoVO().getMatricula().setMatricula(obj.getMatricula());
        		consultarMatriculaPorChavePrimaria();
        		
        		
        		if (getListaSelectItemTipoRequerimento().size() > 1) {
        			Iterator<SelectItem> itens = getListaSelectItemTipoRequerimento().iterator();
            		while (itens.hasNext()) {
            		    SelectItem item = itens.next();
            		    if (!item.getLabel().equals("")) {
            		    	getRequerimentoVO().getTipoRequerimento().setCodigo(Integer.parseInt(item.getValue().toString()));
            		    	break;
            		    }
            		}
    			}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAlunoParaRequerimento");
			}
    		
    	}
		}
	}
	
	
    public void realizarEdicaoRequerimentoVindoTelaFichaAluno() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
    	RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getSessionMap().get("requerimentoEditarRequerimentoFichaAluno");
    	if (obj != null && !obj.getCodigo().equals(0)) {
    		try {
    			
    			setAbrirOpcaoInformarFuncionarioProximoTramite(false);
    			getListaConsultaCurso().clear();
    			setFinalizarEtapaRequerimento(false);
    			setMsgRequerimentoImagemGravadoComSucesso("");
    			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Inicializando Editar requerimento", "Editando");
    			setNovoRegistro(false);
    			setMatricula_valorApresentar("");
    			setMatricula_Erro("");
    			setTipoRequerimento_Erro("");
    			setDepartamentoResponsavel_Erro("");
    			setTipoRequerimentoVO(new TipoRequerimentoVO());
    			obj.setNovoObj(Boolean.FALSE);
    			setRequerimentoVO(obj);
    			getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
    			
    			getRequerimentoVO().getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(), false, getUsuarioLogado()));
    			inicializarListasSelectItemTodosComboBox();
    			this.setMatricula_valorApresentar(requerimentoVO.getMatricula().getAluno().getNome() + " (" + requerimentoVO.getMatricula().getCurso().getNome() + " - " + requerimentoVO.getMatricula().getTurno().getNome() + ")");
    			if (getRequerimentoVO().getValor().equals(0.0)) {
    				getRequerimentoVO().setExigePagamento(Boolean.FALSE);
    			} else {
    				getRequerimentoVO().setExigePagamento(Boolean.TRUE);
    			}
    			getRequerimentoVO().setSomenteAluno(getRequerimentoVO().getTipoPessoa().equals(TipoPessoa.ALUNO));			
    			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
    				setTipoRequerimentoDepartamentoRetornarVOs(getFacadeFactory().getRequerimentoFacade().consultarDepartamentoAnterioresPermiteRetornar(getRequerimentoVO()));
    			}
    			getTipoRequerimentoVO().setCodigo(getRequerimentoVO().getTipoRequerimento().getCodigo());
    			getRequerimentoVO().setEdicao(Boolean.TRUE);
    			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
    			if (visaoAluno != null) {
    				visaoAluno.inicializarNovoRequerimento();
    			}
    			if (!getRequerimentoVO().getArquivoVO().getCodigo().equals(0)) {
    				getRequerimentoVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REQUERIMENTOS_TMP);
    			}
    			if (getRequerimentoVO().getTipoRequerimento().getTramitaEntreDepartamentos()) {
    				getRequerimentoVO().getPodeSerEncaminhadoProximoDepartamento();
    			}
    			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Editar requerimento", "Editando");
    			verificaPermissaoBotaoAproveitamentoTransfExter();
    			this.verificarPermissaoEnviarDepartamentoAnterior();
    			validarApresentacaoBotaoImprimir(getRequerimentoVO());
    			inicializarTipoPessoaPorTipoRequerimento();
    			consultarContaReceberAlunoRequerimento();
    			executarVerificacaoProfessorMinistrouAula();
    			if (getRequerimentoVO().getDisciplina().getCodigo().intValue() == 0) {
    				disciplinaVazia = Boolean.FALSE;
    				montarListaSelectItemDisciplina(getRequerimentoVO().getCurso().getConfiguracaoAcademico());
    			} else {
    				disciplinaVazia = Boolean.TRUE;
    			}
    			setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			} finally {
				context().getExternalContext().getSessionMap().remove("requerimentoEditarRequerimentoFichaAluno");
			}
    		
    	}
		}
	}
	

	public List getListaSelectItemTipoRequerimentoConsulta() {
		if(listaSelectItemTipoRequerimentoConsulta == null){
			listaSelectItemTipoRequerimentoConsulta = new ArrayList<SelectItem>(0);
			montarListaSelectItemTipoRequerimentoConsulta();
		}
		return listaSelectItemTipoRequerimentoConsulta;
	}

	public void setListaSelectItemTipoRequerimentoConsulta(List listaSelectItemTipoRequerimentoConsulta) {
		this.listaSelectItemTipoRequerimentoConsulta = listaSelectItemTipoRequerimentoConsulta;
	}
	

	public void montarListaSelectItemTipoRequerimentoConsulta(){
		List resultadoConsulta = null;
		Iterator i = null;
		try {			
			resultadoConsulta = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoComboBox(false, "AT", getUnidadeEnsinoLogado().getCodigo(), 0, true, getUsuarioLogado(), getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios());				
			i = resultadoConsulta.iterator();
			getListaSelectItemTipoRequerimentoConsulta().add(new SelectItem(0, ""));			
			while (i.hasNext()) {
				TipoRequerimentoVO obj = (TipoRequerimentoVO) i.next();
				getListaSelectItemTipoRequerimentoConsulta().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}										
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private ImpressoraVO impressoraVO;
	private List<SelectItem> listaSelectItemImpressora;

	public ImpressoraVO getImpressoraVO() {
		if (impressoraVO == null) {
			impressoraVO = new ImpressoraVO();
		}
		return impressoraVO;
	}

	public void setImpressoraVO(ImpressoraVO impressoraVO) {
		this.impressoraVO = impressoraVO;
	}

	public void montarListaSelectItemImpressora() {
		try {
			if (Uteis.isAtributoPreenchido(getRequerimentoVO().getUnidadeEnsino()) && getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				List<ImpressoraVO> impressoraVOs = getFacadeFactory().getImpressoraFacade().consultar("requerimento", getRequerimentoVO().getUnidadeEnsino().getCodigo().toString(), getUnidadeEnsinoLogado(), false, getUsuarioLogado());
				setListaSelectItemImpressora(UtilSelectItem.getListaSelectItem(impressoraVOs, "codigo", "nomeImpressora", false));
				consultarImpressoraPadraoUsuarioRequerimento();
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void registrarImpressoraPadraoUsuarioRequerimento() {
		try {
			if (Uteis.isAtributoPreenchido(getRequerimentoVO().getUnidadeEnsino())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getImpressoraVO().getCodigo().toString(), "Requerimento", "ImpressoraU" + getUsuarioLogado().getCodigo() + "UE" + getRequerimentoVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarImpressoraPadraoUsuarioRequerimento() {
		try {
			if (Uteis.isAtributoPreenchido(getRequerimentoVO().getUnidadeEnsino())) {
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("Requerimento", "ImpressoraU" + getUsuarioLogado().getCodigo() + "UE" + getRequerimentoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
				if (layoutPadraoVO != null && !layoutPadraoVO.getValor().trim().isEmpty() && Uteis.getIsValorNumerico(layoutPadraoVO.getValor())) {
					getImpressoraVO().setCodigo(Integer.valueOf(layoutPadraoVO.getValor()));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemImpressora() {
		if (listaSelectItemImpressora == null) {
			listaSelectItemImpressora = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemImpressora;
	}

	public void setListaSelectItemImpressora(List<SelectItem> listaSelectItemImpressora) {
		this.listaSelectItemImpressora = listaSelectItemImpressora;
	}

	public Boolean getIsHabilitarImpressaoPoll() {
		return !getListaSelectItemImpressora().isEmpty() && !getRequerimentoVO().getNovoObj() && getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getIsPermiteImprimirComprovante();
	}
	
	public void imprimirComprovanteRequerimentoBemateck(){
		try {
			getFacadeFactory().getRequerimentoFacade().imprimirComprovanteRequerimentoBemateck(getRequerimentoVO(), getImpressoraVO(), getUsuarioLogado(), getProfessorMinistrouAula());
			registrarImpressoraPadraoUsuarioRequerimento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void downloadArquivo() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Iniciando Download Arquivo", "Downloading");			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("arquivoVO", getRequerimentoVO().getArquivoVO());
//			context().getExternalContext().dispatch("/DownloadSV");
//			FacesContext.getCurrentInstance().responseComplete();
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Download Arquivo", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public String getAcaoModalQuestionarioRequerimentoHistorico2() {
		if (getAbrirModalQuestionarioRequerimentoHistorico()) {
			return "RichFaces.$('panelQuestionarioRequerimentoHistorico').show()";
		}
		if (getFinalizarEtapaRequerimento()) {
			return "RichFaces.$('panelObsTramite').show(); RichFaces.$('panelQuestionarioRequerimentoHistorico').hide();";
		}
		if (getMsgRequerimentoImagemGravadoComSucesso().trim().isEmpty()) {
			return "Richfaces.$('panelObsTramite').hide(); Richfaces.$('panelQuestionarioRequerimentoHistorico').hide();";
		}
		return "RichFaces.$('panelObsTramite').hide(); RichFaces.$('panelQuestionarioRequerimentoHistorico').hide(); RichFaces.$('panelGravadoComSucesso').show();";
	}
	
	public void blurListenterZeraConsultaCurso() {
		if(!getAutocompleteValorCurso().contains(getCursoVO().getNome()) 
				|| !getAutocompleteValorCurso().contains(getCursoVO().getCodigo().toString())
				|| !getAutocompleteValorCurso().contains(")")
				|| !getAutocompleteValorCurso().contains("(")) {
			setCursoVO(new CursoVO());
		}
	}
	
	public void blurListenterZeraConsultaTurma() {
		if(!getAutocompleteValorTurma().equals(valorTurma)) {
			setTurmaVO(new TurmaVO());
		}
	}
	
	public void blurListenerZeraConsultaRequisitante() {
		if(!getAutocompleteValorRequisitante().equals(valorRequisitante)) {
			getRequerimentoVO().setPessoa(new PessoaVO());
			getRequerimentoConsVO().setPessoa(new PessoaVO());
		}
	}
	
	public void blurListenerZeraConsultaResponsavel() {
		if(!getAutocompleteValorResponsavel().equals(valorResponsavel)) {
			getRequerimentoConsVO().setResponsavel(new UsuarioVO());
		}
	}	
	
	public void realizarAutorizacaoPagamentoRequerimento() {		
		if(getPermitirAutorizarPagamento()) {
			realizarAutorizacaoPagamentoRequerimentoComUsuario(getUsuarioLogadoClone());
		}else {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(usuarioLiberarOperacaoFuncionalidade, senhaLiberarOperacaoFuncionalidade, true, Uteis.NIVELMONTARDADOS_TODOS);
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RequerimentoPermitirAutorizarPagamento", usuarioVO);
				realizarAutorizacaoPagamentoRequerimentoComUsuario(usuarioVO);
			} catch (Exception e) {				
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}finally {
				setSenhaLiberarOperacaoFuncionalidade("");	
			}
		}
		
	}
	
	public void realizarAutorizacaoPagamentoRequerimentoComUsuario(UsuarioVO usuarioVO) {
		try {
			getFacadeFactory().getRequerimentoFacade().realizarAutorizacaoPagamentoRequerimento(getRequerimentoVO(),  usuarioVO);
			setOncompleteModal("RichFaces.$('panelAutorizarPagamento').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private Boolean permitirAutorizarPagamento;
	
	public Boolean getPermitirAutorizarPagamento() {
		if (permitirAutorizarPagamento == null) {
			permitirAutorizarPagamento = executarVerificacaoUsuarioPossuiPermissaoFuncionalidade("RequerimentoPermitirAutorizarPagamento");			
		}
		return permitirAutorizarPagamento;
	}

	public void setPermitirAutorizarPagamento(Boolean permitirAutorizarPagamento) {
		this.permitirAutorizarPagamento = permitirAutorizarPagamento;
	}

	public void realizarDefinicaoVencimentoContaReceberRequerimento() {
		try {
			setOncompleteModal("");
			limparUsuarioSenhaOperacaoFuncionalidade();
			getFacadeFactory().getRequerimentoFacade().realizarDefinicaoVencimentoContaReceberRequerimento(getRequerimentoVO(), true);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void cancelarAutorizacaoPagamentoRequerimento() {
		try {
			getRequerimentoVO().setDataVencimentoContaReceber(null);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemSituacaoRequerimentoDepartamento() {
		if (listaSelectItemSituacaoRequerimentoDepartamento == null) {
			listaSelectItemSituacaoRequerimentoDepartamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemSituacaoRequerimentoDepartamento;
	}

	public void setListaSelectItemSituacaoRequerimentoDepartamento(List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamento) {
		this.listaSelectItemSituacaoRequerimentoDepartamento = listaSelectItemSituacaoRequerimentoDepartamento;
	}
	
	public void inicializarListaSelectItemSituacaoRequerimentoDepartamento() {
		try {
			getListaSelectItemSituacaoRequerimentoDepartamento().clear();
			List<TipoRequerimentoSituacaoDepartamentoVO> tipoRequerimentoSituacaoDepartamentoVOs = getFacadeFactory().getTipoRequerimentoSituacaoDepartamentoFacade().consultarPorTipoRequerimentoDepartamento(getRequerimentoVO().getTipoRequerimento().getCodigo(),getRequerimentoVO().getDepartamentoResponsavel().getCodigo(), getRequerimentoVO().getOrdemExecucaoTramiteDepartamento());
			getListaSelectItemSituacaoRequerimentoDepartamento().add(new SelectItem(0, ""));
			for(TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO: tipoRequerimentoSituacaoDepartamentoVOs) {
				getListaSelectItemSituacaoRequerimentoDepartamento().add(new SelectItem(tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getSituacao()));
			}
		}catch (Exception e) {

		}
	}
	
	public void alterarSituacaoDepartamento() {
		try {
			getFacadeFactory().getRequerimentoHistoricoFacade().alterarSituacaoDepartamento(getRequerimentoHistoricoVO(), getRequerimentoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getApresentarSituacaoTramite() {
		return !getRequerimentoVO().isNovoObj() && getListaSelectItemSituacaoRequerimentoDepartamento().size() > 1 && Uteis.isAtributoPreenchido(getRequerimentoHistoricoVO().getCodigo());
	}

	public List<SelectItem> getListaSelectItemSituacaoRequerimentoDepartamentoCons() {
		if (listaSelectItemSituacaoRequerimentoDepartamentoCons == null) {
			listaSelectItemSituacaoRequerimentoDepartamentoCons = new ArrayList<SelectItem>();
			try {
				List<SituacaoRequerimentoDepartamentoVO> situacaoRequerimentoDepartamentoVOs =  getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorSituacao("", getUsuarioLogado());
				listaSelectItemSituacaoRequerimentoDepartamentoCons = UtilSelectItem.getListaSelectItem(situacaoRequerimentoDepartamentoVOs, "codigo", "situacao");
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
		}
		return listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}

	public void setListaSelectItemSituacaoRequerimentoDepartamentoCons(List<SelectItem> listaSelectItemSituacaoRequerimentoDepartamentoCons) {
		this.listaSelectItemSituacaoRequerimentoDepartamentoCons = listaSelectItemSituacaoRequerimentoDepartamentoCons;
	}

	public Integer getSituacaoRequerimentoDepartamento() {
		if (situacaoRequerimentoDepartamento == null) {
			situacaoRequerimentoDepartamento = 0;
		}
		return situacaoRequerimentoDepartamento;
	}

	public void setSituacaoRequerimentoDepartamento(Integer situacaoRequerimentoDepartamento) {
		this.situacaoRequerimentoDepartamento = situacaoRequerimentoDepartamento;
	}

	public void cancelarSolicitacaoIsencaoTaxa() {
		getRequerimentoVO().setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA);
	}
	
	public void realizarSolicitacaoIsencaoTaxa() {
		try {
			setOncompleteModal("");
			getRequerimentoVO().setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA);			
			if(!getRequerimentoVO().isNovoObj()) {
				getFacadeFactory().getRequerimentoFacade().incluirSolicitacaoIsencaoTaxa(getRequerimentoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}else {
				getFacadeFactory().getRequerimentoFacade().validarDadosSolicitacaoIsencaoTaxa(getRequerimentoVO());
				setMensagemID("msg_acao_realizadaComSucesso", Uteis.SUCESSO);
			}
			setOncompleteModal("RichFaces.$('panelSolicitacaoIsencao').hide()");
		}catch (Exception e) {
			getRequerimentoVO().setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.NAO_REQUERIDA);	
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void upLoadComprovanteSolicitacaoIsencaoTaxa(FileUploadEvent uploadEvent) {
		try {
			getRequerimentoVO().getComprovanteSolicitacaoIsencao().setCpfRequerimento(getRequerimentoVO().getPessoa().getCPF());
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoMatriculaRequerimento(uploadEvent, getRequerimentoVO().getComprovanteSolicitacaoIsencao(), getRequerimentoVO().getComprovanteSolicitacaoIsencao().getCpfRequerimento() + getRequerimentoVO().getTipoRequerimento().getNome(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.REQUERIMENTOS_TMP, getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void downloadComprovanteSolicitacaoIsencaoTaxa() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Iniciando Download Arquivo", "Downloading");			
			context().getExternalContext().getSessionMap().put("arquivoVO", getRequerimentoVO().getComprovanteSolicitacaoIsencao());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequerimentoControle", "Finalizando Download Arquivo", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void realizarDeferimentoSolicitacaoIsencaoTaxa() {
		if(getPermitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa()) {
			gravarDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO, getUsuarioLogado());			
		}else {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(usuarioLiberarOperacaoFuncionalidade, senhaLiberarOperacaoFuncionalidade, true, Uteis.NIVELMONTARDADOS_TODOS);
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa", usuarioVO);
				gravarDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO, usuarioVO);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}finally {
				setSenhaLiberarOperacaoFuncionalidade("");	
			}
		}
		
	}
	
	public void realizarIndeferimentoSolicitacaoIsencaoTaxa() {
		if (getPermitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa()) {
			gravarDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO, getUsuarioLogado());
		} else {
			try {
				UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(usuarioLiberarOperacaoFuncionalidade, senhaLiberarOperacaoFuncionalidade, true, Uteis.NIVELMONTARDADOS_TODOS);
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa", usuarioVO);
				gravarDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO, usuarioVO);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}finally {
				setSenhaLiberarOperacaoFuncionalidade("");	
			}
		}

	}
	
	private void gravarDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum situacaoSolicitacaoIsencaoTaxaRequerimentoEnum, UsuarioVO usuarioVO) {
		try{
			setOncompleteModal("");
			getRequerimentoVO().setSituacaoIsencaoTaxa(situacaoSolicitacaoIsencaoTaxaRequerimentoEnum);
			getFacadeFactory().getRequerimentoFacade().realizarRegistroDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(getRequerimentoVO(),  usuarioVO);
			setOncompleteModal("RichFaces.$('panelDefIndefSolicitacaoIsencao').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			requerimentoVO.setSituacaoIsencaoTaxa(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPermitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa() {
		if (permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa == null) {			
			try{
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa", getUsuarioLogado());
				permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa = true;
			}catch(Exception e){
				permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa = false;
			}
		}
		return permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa;
	}

	public void setPermitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa(Boolean permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa) {
		this.permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa = permitirUsuarioDeferirIndeferirSolicitacaoIsencaoTaxa;
	}
	
	public void limparUsuarioSenhaOperacaoFuncionalidade() {
		setUsuarioLiberarOperacaoFuncionalidade("");
		setSenhaLiberarOperacaoFuncionalidade("");
	}	

	public void consultarTurmaParaReposicaoDisciplina() {
		try {
			if (Uteis.isAtributoPreenchido(this.getRequerimentoVO().getDisciplina())) {
				getRequerimentoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(
						getRequerimentoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,
						getUsuarioLogado()));
			}
			getRequerimentoVO().setDisciplinaPorEquivalencia(false);
			getListaMapaEquivalenciaDisciplinaIncluir().clear();
			getListaSelectItemMapaEquivalenciaDisciplinaIncluir().clear();
			getListaTurmaIncluir().clear();
			if (getRequerimentoVO().getTipoRequerimento().getIsTipoReposicao()
					&& Uteis.isAtributoPreenchido(this.getRequerimentoVO().getDisciplina())) {
//				getFacadeFactory().getRequerimentoFacade().realizarValidacaoCobrancaRequerimento(getRequerimentoVO());
				getRequerimentoVO().setCargaHorariaDisciplina(getFacadeFactory().getGradeDisciplinaFacade()
						.consultarCargaHorariaDisciplinaPorDisciplinaETurma(
								getRequerimentoVO().getDisciplina().getCodigo(),
								getRequerimentoVO().getMatricula().getMatricula(), getUsuarioLogado()));
				montarListaMapaEquivalenciaDisciplinaIncluir();
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private List<MapaEquivalenciaDisciplinaVO> listaMapaEquivalenciaDisciplinaIncluir;	
	private List<SelectItem> listaSelectItemMapaEquivalenciaDisciplinaIncluir;	
	private List<TurmaVO> listaTurmaIncluir;
	
	public List<SelectItem> getListaSelectItemMapaEquivalenciaDisciplinaIncluir() {
		if (listaSelectItemMapaEquivalenciaDisciplinaIncluir == null) {
			listaSelectItemMapaEquivalenciaDisciplinaIncluir = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	public void setListaSelectItemMapaEquivalenciaDisciplinaIncluir(List<SelectItem> listaSelectItemMapaEquivalenciaDisciplinaIncluir) {
		this.listaSelectItemMapaEquivalenciaDisciplinaIncluir = listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	public List<MapaEquivalenciaDisciplinaVO> getListaMapaEquivalenciaDisciplinaIncluir() {
		if (listaMapaEquivalenciaDisciplinaIncluir == null) {
			listaMapaEquivalenciaDisciplinaIncluir = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		}
		return listaMapaEquivalenciaDisciplinaIncluir;
	}

	public void setListaMapaEquivalenciaDisciplinaIncluir(List<MapaEquivalenciaDisciplinaVO> listaMapaEquivalenciaDisciplinaIncluir) {
		this.listaMapaEquivalenciaDisciplinaIncluir = listaMapaEquivalenciaDisciplinaIncluir;
	}

	public List<TurmaVO> getListaTurmaIncluir() {
		if (listaTurmaIncluir == null) {
			listaTurmaIncluir = new ArrayList<TurmaVO>(0);
		}
		return listaTurmaIncluir;
	}

	public void setListaTurmaIncluir(List<TurmaVO> listaTurmaIncluir) {
		this.listaTurmaIncluir = listaTurmaIncluir;
	}

	public void montarListaMapaEquivalenciaDisciplinaIncluir() {
		try {
			
			getListaSelectItemMapaEquivalenciaDisciplinaIncluir().clear();
			if (!this.getRequerimentoVO().getDisciplinaPorEquivalencia()) {			
				getListaMapaEquivalenciaDisciplinaIncluir().clear();
				getFacadeFactory().getRequerimentoFacade().montarListaSelectItemTurmaAdicionar(getRequerimentoVO(), getListaTurmaIncluir(), getUsuarioLogado(), getMapTurmas());
				return;
			}
			setListaMapaEquivalenciaDisciplinaIncluir(consultarMapaEquivalenciaDisciplina());
			if (getListaMapaEquivalenciaDisciplinaIncluir().isEmpty()) {
				getRequerimentoVO().setDisciplinaPorEquivalencia(false);
				throw new Exception("Não Existem Equivalências Disponíveis para Esta Disciplina.");
			}
			inicializarDadosMapaDisciplinaEquivalenteAdicionar(getListaMapaEquivalenciaDisciplinaIncluir().get(0));
			for(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO: getListaMapaEquivalenciaDisciplinaIncluir()) {
				getListaSelectItemMapaEquivalenciaDisciplinaIncluir().add(new SelectItem(mapaEquivalenciaDisciplinaVO.getCodigo(), mapaEquivalenciaDisciplinaVO.getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getDisciplinaApresentar()));				
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<MapaEquivalenciaDisciplinaVO> consultarMapaEquivalenciaDisciplina() throws Exception {
		return getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(getRequerimentoVO().getMatricula().getGradeCurricularAtual().getCodigo(), getRequerimentoVO().getDisciplina().getCodigo(), NivelMontarDados.TODOS, true);				
	}


	public void selecionarMapaDisciplinaEquivalenteAdicionar() {
		for(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO: getListaMapaEquivalenciaDisciplinaIncluir()) {
			if(getRequerimentoVO().getMapaEquivalenciaDisciplinaVO().getCodigo().equals(mapaEquivalenciaDisciplinaVO.getCodigo())) {				
				inicializarDadosMapaDisciplinaEquivalenteAdicionar(mapaEquivalenciaDisciplinaVO);
				break;
			}						
		}
	}
	
	public void inicializarDadosMapaDisciplinaEquivalenteAdicionar(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina) {
		try {
			getRequerimentoVO().setMapaEquivalenciaDisciplinaVO((MapaEquivalenciaDisciplinaVO)mapaEquivalenciaDisciplina.clone());
			getRequerimentoVO().setMapaEquivalenciaDisciplinaCursadaVO((MapaEquivalenciaDisciplinaCursadaVO)mapaEquivalenciaDisciplina.getMapaEquivalenciaDisciplinaCursadaVOs().get(0).clone());		
			getFacadeFactory().getRequerimentoFacade().montarListaSelectItemTurmaAdicionar(getRequerimentoVO(), getListaTurmaIncluir(), getUsuarioLogado(), getMapTurmas());
		} catch (CloneNotSupportedException e) {
			
		}
	}




	
	public void selecionarTurmaReposicao() {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getFacadeFactory().getRequerimentoFacade().definirTurmaBaseRequerimentosTurmaAgrupada(getRequerimentoVO(), getListaTurmaIncluir(), getUsuarioLogado(), getMapTurmas(), turma);
			getRequerimentoVO().getMatricula().getMatriculaComHistoricoAlunoVO().setNivelMontarDados(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS);
			getFacadeFactory().getRequerimentoFacade().realizarValidacaoChoqueHorarioEVagaTurmaRequerimentoReposicao(getRequerimentoVO(),  getUsuarioLogado());			
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		}catch (Exception e) {
			getRequerimentoVO().setTurmaReposicao(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private Boolean origemMinhasNotas;
	
	
	public void inicializarRequerimentoApartirMinhaNotas() {
		if(getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			Object disciplina = context().getExternalContext().getSessionMap().get("disciplinaReposicao");			
			if(disciplina != null) {
				setOrigemMinhasNotas(true);
				context().getExternalContext().getSessionMap().remove("disciplinaReposicao");
				try {
					novoVisaoAluno();
					List<TipoRequerimentoVO> lista = getFacadeFactory().getTipoRequerimentoFacade().consultarPorPermissaoVisaoAlunoMinhasNotas(Boolean.TRUE, "AT", getUnidadeEnsinoLogado().getCodigo(), getRequerimentoVO().getMatricula().getCurso().getCodigo(), getRequerimentoVO().getMatricula().getMatricula(), (Integer)disciplina, getRequerimentoVO().getMatriculaPeriodoVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  false, getUsuarioLogado());					
					if(!lista.isEmpty()) {
						setNovoRegistro(true);
						getRequerimentoVO().setTipoRequerimento(lista.get(0));
						atualizarValor(true);
						getRequerimentoVO().getDisciplina().setCodigo((Integer) disciplina);
						consultarTurmaParaReposicaoDisciplina();
					}
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		}
	}

	public Boolean getOrigemMinhasNotas() {
		if (origemMinhasNotas == null) {
			origemMinhasNotas = false;
		}
		return origemMinhasNotas;
	}

	public void setOrigemMinhasNotas(Boolean origemMinhasNotas) {
		this.origemMinhasNotas = origemMinhasNotas;
	}
	
	public void selecionarTipoRequerimento(Boolean forcarDefinicaoData) {
		getRequerimentoVO().setValorDesconto(0.0);
		getRequerimentoVO().setPercDesconto(0.0);
		getRequerimentoVO().setValorAdicional(0.0);
		getRequerimentoVO().setMensagemChoqueHorario("");
		getRequerimentoVO().setTurmaReposicao(null);
		getRequerimentoVO().setDisciplina(null);
		getRequerimentoVO().setDisciplinaPorEquivalencia(false);
		getRequerimentoVO().setMapaEquivalenciaDisciplinaCursadaVO(null);
		getRequerimentoVO().setMapaEquivalenciaDisciplinaVO(null);
		if (permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
			try {
				getRequerimentoVO().setTipoPessoa(TipoPessoa.REQUERENTE);
				getRequerimentoVO().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		atualizarValor(forcarDefinicaoData);
		montarDadosTipoRequerimento();
		setCertificadoDigital(Boolean.FALSE);
		setCertificadoImpresso(Boolean.FALSE);
		
		if (Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao())){
			setCertificadoDigital(Boolean.TRUE);
		}
		if (Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso())) {
			setCertificadoImpresso(Boolean.TRUE);
		}	
		if (getRequerimentoVO().getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
			montarListaSelectItemDisciplinaAproveitamento();
		}
		setBloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna(!getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarUnidadeEnsino());
		getRequerimentoVO().setUnidadeEnsinoTransferenciaInternaVO(new UnidadeEnsinoVO());
		if (getBloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna() && getRequerimentoVO().getExisteMatricula()) {			
			try {
				getRequerimentoVO().setUnidadeEnsinoTransferenciaInternaVO((UnidadeEnsinoVO)getRequerimentoVO().getMatricula().getUnidadeEnsino().clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		} else {
			getRequerimentoVO().setUnidadeEnsinoTransferenciaInternaVO(new UnidadeEnsinoVO());
		}
		if (getRequerimentoVO().getExisteMatricula() && getRequerimentoVO().getTipoRequerimento().getIsTipoTransferenciaInterna() &&  !getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarCurso()) {
			try {
				getRequerimentoVO().setCursoTransferenciaInternaVO((CursoVO)getRequerimentoVO().getMatricula().getCurso().clone());
				getRequerimentoVO().setTurnoTransferenciaInternaVO((TurnoVO)getRequerimentoVO().getMatricula().getTurno().clone());
			} catch (CloneNotSupportedException e) {				
				e.printStackTrace();
			}
			if (!getBloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna()) {
				montarListaSelectItemUnidadeEnsinoTransferenciaInterna();
			}
		} else {
			getRequerimentoVO().setCursoTransferenciaInternaVO(new CursoVO());
			getRequerimentoVO().setTurnoTransferenciaInternaVO(new TurnoVO());
			if((getRequerimentoVO().getExisteMatricula() && getRequerimentoVO().getTipoRequerimento().getIsTipoTransferenciaInterna()) && getRequerimentoVO().getTipoRequerimento().getPermitirAlunoAlterarUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsinoTransferenciaInterna();
			}
		}
		if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
			montarListaSelectItemGrupoFacilitador();
		}
		montarListaSelectItemCidTipoRequerimento();
	}
	
	public void  montarDadosTipoRequerimento() {
		try {
			getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}

	}

	public Boolean getPermitirUsuarioConsultarIncluirApenasRequerimentosProprios() {
		if (permitirUsuarioConsultarIncluirApenasRequerimentosProprios == null) {
			permitirUsuarioConsultarIncluirApenasRequerimentosProprios = false;			
		}
		return permitirUsuarioConsultarIncluirApenasRequerimentosProprios;
	}

	public void setPermitirUsuarioConsultarIncluirApenasRequerimentosProprios(Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) {
		this.permitirUsuarioConsultarIncluirApenasRequerimentosProprios = permitirUsuarioConsultarIncluirApenasRequerimentosProprios;
	}
	
	public void realizarVerificacaoPermissaoConsultarRequerimentosProprioUsuario() {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RequerimentoPermitirConsultarIncluirRequerimentoApenasProprioUsuario", getUsuarioLogado());
			setPermitirUsuarioConsultarIncluirApenasRequerimentosProprios(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirUsuarioConsultarIncluirApenasRequerimentosProprios(Boolean.FALSE);
		}
	}
	
	public Boolean getPermitirAlterarObservacaoIncluidaPeloRequerente() {
		if(permitirAlterarObservacaoIncluidaPeloRequerente == null) {
			permitirAlterarObservacaoIncluidaPeloRequerente = Boolean.FALSE;
		}
		return permitirAlterarObservacaoIncluidaPeloRequerente;
	}

	public void setPermitirAlterarObservacaoIncluidaPeloRequerente(
			Boolean permitirAlterarObservacaoIncluidaPeloRequerente) {
		this.permitirAlterarObservacaoIncluidaPeloRequerente = permitirAlterarObservacaoIncluidaPeloRequerente;
	}

	public void verificarPermissaoAlterarObservacaoIncluidaPeloRequerente() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirAlterarObservacaoIncluidaPeloRequerente", getUsuarioLogado())) {
				 setPermitirAlterarObservacaoIncluidaPeloRequerente(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirAlterarObservacaoIncluidaPeloRequerente(Boolean.FALSE);
		}
	}

	public Boolean getCheckDisponibilizarTodosParaRequerente() {
		return checkDisponibilizarTodosParaRequerente;
	}

	public void setCheckDisponibilizarTodosParaRequerente(Boolean checkDisponibilizarTodosParaRequerente) {
		this.checkDisponibilizarTodosParaRequerente = checkDisponibilizarTodosParaRequerente;
	}

	public void checarDisponibilizarTodosParaRequerente(){
		try {
			if (getCheckDisponibilizarTodosParaRequerente()) {
				for (MaterialRequerimentoVO materialRequerimentoVO : getRequerimentoVO().getMaterialRequerimentoVOs()) {
					materialRequerimentoVO.setDisponibilizarParaRequerente(true);
					getFacadeFactory().getMaterialRequerimentoFacade().alterarDisponibilizarParaRequerenteMaterialRequerimento(materialRequerimentoVO, getUsuarioLogado());
				}
			} else {
				for (MaterialRequerimentoVO materialRequerimentoVO : getRequerimentoVO().getMaterialRequerimentoVOs()) {
					materialRequerimentoVO.setDisponibilizarParaRequerente(false);
					getFacadeFactory().getMaterialRequerimentoFacade().alterarDisponibilizarParaRequerenteMaterialRequerimento(materialRequerimentoVO, getUsuarioLogado());
				}
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void editarMaterialRequerimento() {
		try {
			MaterialRequerimentoVO obj = (MaterialRequerimentoVO) context().getExternalContext().getRequestMap().get("materialRequerimentoVOItem");
			setMaterialRequerimento(obj);
			for (MaterialRequerimentoVO materialRequerimentoVO : getRequerimentoVO().getMaterialRequerimentoVOs()) {
				if ((obj.getArquivoVO().equals(materialRequerimentoVO.getArquivoVO()) && obj.getDescricao().equals(materialRequerimentoVO.getDescricao()) || materialRequerimentoVO.getCodigo().equals(obj.getCodigo()))) {
					getRequerimentoVO().getMaterialRequerimentoVOs().remove(obj);
					break;
				}
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
    public void cancelarEdicaoMaterialRequerimento() {
    	setMaterialRequerimento(new MaterialRequerimentoVO());
    }
    
	public void alterarDescricaoMaterialRequerimento() throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(getMaterialRequerimento())) {
				getFacadeFactory().getMaterialRequerimentoFacade().alterar(getMaterialRequerimento(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());;
			}
			getRequerimentoVO().getMaterialRequerimentoVOs().add(getMaterialRequerimento());
			setMaterialRequerimento(new MaterialRequerimentoVO());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void alterarDisponibilizarParaRequerenteMaterialRequerimento() throws Exception {
		try {
			MaterialRequerimentoVO obj = (MaterialRequerimentoVO) context().getExternalContext().getRequestMap().get("materialRequerimentoVOItem");
			setMaterialRequerimento((MaterialRequerimentoVO) obj.clone());
			if (Uteis.isAtributoPreenchido(getMaterialRequerimento())) {
				getFacadeFactory().getMaterialRequerimentoFacade().alterarDisponibilizarParaRequerenteMaterialRequerimento(getMaterialRequerimento(), getUsuarioLogado());
			}
			setMaterialRequerimento(new MaterialRequerimentoVO());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
			
	public Boolean getPermitirRequerenteAnexarArquivo() {
		if(permitirRequerenteAnexarArquivo == null) {
			permitirRequerenteAnexarArquivo = Boolean.FALSE;
		}
		return permitirRequerenteAnexarArquivo;
	}

	public void setPermitirRequerenteAnexarArquivo(Boolean permitirRequerenteAnexarArquivo) {
		this.permitirRequerenteAnexarArquivo = permitirRequerenteAnexarArquivo;
	}

	public void verificarPermissaoRequerenteAnexarArquivo() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirRequerenteAnexarArquivo", getUsuarioLogado())) {
				 setPermitirRequerenteAnexarArquivo(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirRequerenteAnexarArquivo(Boolean.FALSE);
		}
	}

	public Boolean getPermitirRequerenteVisualizarTramite() {
		if(permitirRequerenteVisualizarTramite == null) {
			permitirRequerenteVisualizarTramite = Boolean.FALSE;
		}
		return permitirRequerenteVisualizarTramite;
	}

	public void setPermitirRequerenteVisualizarTramite(Boolean permitirRequerenteVisualizarTramite) {
		this.permitirRequerenteVisualizarTramite = permitirRequerenteVisualizarTramite;
	}

	public void verificarPermissaoRequerenteVisualizarTramite() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirRequerenteVisualizarTramite", getUsuarioLogado())) {
				 setPermitirRequerenteVisualizarTramite(Boolean.TRUE);
			 }
		} catch (Exception e) {
			setPermitirRequerenteVisualizarTramite(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoRequerenteInteragirTramite() {
		try {
			 if(getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("RequerimentoPermitirRequerenteInteragirTramite", getUsuarioLogado())) {
				 getRequerimentoVO().setPermitirRequerenteInteragirTramite(Boolean.TRUE);
			 }
		} catch (Exception e) {
			getRequerimentoVO().setPermitirRequerenteInteragirTramite(Boolean.FALSE);
		}
	}

	public InteracaoRequerimentoHistoricoVO getInteracaoRequerimentoHistorico() {
		if(interacaoRequerimentoHistorico == null) {
			interacaoRequerimentoHistorico = new InteracaoRequerimentoHistoricoVO();
		}
		return interacaoRequerimentoHistorico;
	}

	public void setInteracaoRequerimentoHistorico(InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistorico) {
		this.interacaoRequerimentoHistorico = interacaoRequerimentoHistorico;
	}
	
	 public String adicionarInteracaoRequerimentoHistorico() {	    	
	        try {
	        	executarValidacaoSimulacaoVisaoAluno();
	        	getInteracaoRequerimentoHistorico().setRequerimentoHistorico(getRequerimentoHistoricoAtual());
	        	getInteracaoRequerimentoHistorico().setUsuarioInteracao(getUsuarioLogadoClone());
	        	getInteracaoRequerimentoHistorico().setDataInteracao(new Date());
	            getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().persistir(getInteracaoRequerimentoHistorico(), false, getUsuarioLogado());
	            this.enviarEmailTramiteInteracaoTramiteRequerimento();
	            setInteracaoRequerimentoHistorico(new InteracaoRequerimentoHistoricoVO());
				if(!getRequerimentoVO().getNovoObj()) {
					getFacadeFactory().getRequerimentoFacade().alterarDataUltimaAlteracao(getRequerimentoVO().getCodigo());
					getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
					abrirDetalheHistoricoRequerimento();
				}
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	            if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
	            	return navegarAba("richTramite", "requerimentoCoordenadorForm.xhtml");
	            } else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
	            	return navegarAba("richTramite", "requerimentoProfessorForm.xhtml");
	            } else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
	            	return navegarAba("richTramite", "requerimentoForm.xhtml");
	            }
	            return "";
	        } catch (ConsistirException e) {
	            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	            return "";
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	            return "";
	        }
	    }
	 
		public InteracaoRequerimentoHistoricoVO getInteracaoRequerimentoHistoricoFilho() {
			if(interacaoRequerimentoHistoricoFilho == null) {
				interacaoRequerimentoHistoricoFilho = new InteracaoRequerimentoHistoricoVO();
			}
			return interacaoRequerimentoHistoricoFilho;
		}

		public void setInteracaoRequerimentoHistoricoFilho(
				InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoFilho) {
			this.interacaoRequerimentoHistoricoFilho = interacaoRequerimentoHistoricoFilho;
		}
		
	    public void responderComentarioInteracaoRequerimentoHistorico() {
	        try {
	        	InteracaoRequerimentoHistoricoVO obj = (InteracaoRequerimentoHistoricoVO) context().getExternalContext().getRequestMap().get("interacaoRequerimentoHistoricoItens");
	            setInteracaoRequerimentoHistoricoFilho(new InteracaoRequerimentoHistoricoVO());
	            getInteracaoRequerimentoHistoricoFilho().setRequerimentoHistorico(getRequerimentoHistoricoAtual());
	            getInteracaoRequerimentoHistoricoFilho().setInteracaoRequerimentoHistoricoPai(obj);
	            getInteracaoRequerimentoHistoricoFilho().setUsuarioInteracao(getUsuarioLogadoClone());
	            getInteracaoRequerimentoHistoricoFilho().setDataInteracao(new Date());
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	        }
	    }
		
	    public void persistirRespostaInteracaoRequerimentoHistorico() {
	        try {
	        	executarValidacaoSimulacaoVisaoAluno();
	        	getFacadeFactory().getInteracaoRequerimentoHistoricoFacade().persistir(getInteracaoRequerimentoHistoricoFilho(), false, getUsuarioLogado());
	            setInteracaoRequerimentoHistoricoFilho(new InteracaoRequerimentoHistoricoVO());
				if(!getRequerimentoVO().getNovoObj()) {
					getFacadeFactory().getRequerimentoFacade().alterarDataUltimaAlteracao(getRequerimentoVO().getCodigo());
					getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
					abrirDetalheHistoricoRequerimento();
				}
	            setMensagemID("msg_foruminteracao_adicionado", Uteis.SUCESSO);
	        } catch (ConsistirException e) {
	            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	        }
	    }

		public RequerimentoHistoricoVO getRequerimentoHistoricoAtual() {
			try {
				return getRequerimentoVO().consultarUltimoRequerimentoHistoricoDepartamentoAtualVOs();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				return new RequerimentoHistoricoVO();
			}
		}
		
		public String getCaminhoBaseFoto() {
			try {

				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo();
			} catch (Exception e) {
				return "/resources/imagens/visao/foto_usuario.png";
			}
		}	   
		
		public void abrirDetalheHistoricoRequerimento() throws Exception {
			
			RequerimentoHistoricoVO requerimentoHistoricoAtualVO  = getRequerimentoHistoricoAtual();
			for (RequerimentoHistoricoVO requerimentoHistoricoVO : getRequerimentoVO().getRequerimentoHistoricoVOs()) {
				if(requerimentoHistoricoVO.getCodigo().equals(requerimentoHistoricoAtualVO.getCodigo())){
					requerimentoHistoricoVO.setAbrirDetalheHistoricoRequerimento(true);
				}
				else {
					requerimentoHistoricoVO.setAbrirDetalheHistoricoRequerimento(false);
				}
				
			}
		}
		
		public void realizarPreviewCertificado(RequerimentoVO requerimento) throws Exception{
			if(getRequerimentoVO().getTipoRequerimento().getTipo().equals("EC")) {
				List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);				
				montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
			}else {
				imprirmirDeclaracaoVisaoAdministrativo();	

			}
		}
		
		public void inicializarDadosFiltroAcademicoDeAcordoConfiguracaoTipoRequerimento() {
			super.getFiltroRelatorioAcademicoVO().setReprovado(getRequerimentoVO().getTipoRequerimento().getReprovadoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setAprovado(getRequerimentoVO().getTipoRequerimento().getAprovadoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setCursando(getRequerimentoVO().getTipoRequerimento().getCursandoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setTrancado(getRequerimentoVO().getTipoRequerimento().getTrancadoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setAbandonado(getRequerimentoVO().getTipoRequerimento().getAbandonoCursoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setCancelado(getRequerimentoVO().getTipoRequerimento().getCanceladoSituacaoHistorico());
			super.getFiltroRelatorioAcademicoVO().setTransferidoHistorico(getRequerimentoVO().getTipoRequerimento().getTransferidoSituacaoHistorico());
		}
		
		public void imprimirHistoricoVisaoAluno() {
			try {
				DocumentoAssinadoVO documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getCodigo(), OrigemArquivo.REQUERIMENTO.getValor(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(documentoAssinadoVO)) {
					imprimirDocumentoAssinadoJaGerada(documentoAssinadoVO);
				} else {
					super.selecionarAluno(getRequerimentoVO().getMatricula());
					super.setTipoLayout(getRequerimentoVO().getTipoRequerimento().getLayoutHistoricoApresentar());
					super.setCampoFiltroPor("aluno");
					super.setAssinarDigitalmente(getRequerimentoVO().getTipoRequerimento().getAssinarDigitalmenteHistorico());
					super.setOrdem(OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor());
					super.getRequerimentoVO().setCodigo(getRequerimentoVO().getCodigo());
					super.gerarListaMatriculas();
					inicializarDadosFiltroAcademicoDeAcordoConfiguracaoTipoRequerimento();
					super.imprimirPDF();
				}
				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void imprimirDocumentoAssinadoJaGerada(DocumentoAssinadoVO obj) {
			try {
				limparMensagem();
				setFazerDownload(false);
				this.setCaminhoRelatorio("");
				getFacadeFactory().getArquivoFacade().carregarArquivoDigitalmenteAssinado(obj.getArquivo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setCaminhoRelatorio(obj.getArquivo().getNome());
				setFazerDownload(true);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			//return "";
		}
				
		public List<CertificadoCursoExtensaoRelVO> getListaCertificadoErro() {
			if (listaCertificadoErro == null) {
				listaCertificadoErro = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
			}
			return listaCertificadoErro;
		}

		public void setListaCertificadoErro(List<CertificadoCursoExtensaoRelVO> listaCertificadoErro) {
			this.listaCertificadoErro = listaCertificadoErro;
		}
		
		public List<File> getListaArquivos() {
			if(listaArquivos == null) {
				listaArquivos = new ArrayList<File>(0);
			}
			return listaArquivos;
		}

		public void setListaArquivos(List<File> listaArquivos) {
			this.listaArquivos = listaArquivos;
		}		
		
		public Boolean getImpressaoContratoExistente() {
			if(impressaoContratoExistente == null){
				impressaoContratoExistente = false;
			}
			return impressaoContratoExistente;
		}

		public void setImpressaoContratoExistente(Boolean impressaoContratoExistente) {
			this.impressaoContratoExistente = impressaoContratoExistente;
		}

//		public TipoCartaoOperadoraCartaoEnum getTipoCartao() {
//			if (tipoCartao == null) {
//				if (Uteis.isAtributoPreenchido(getConfiguracaoRecebimentoCartaoOnlineVO()) && PermitirCartaoEnum.DEBITO
//						.equals(getConfiguracaoRecebimentoCartaoOnlineVO().getPermitirCartao())) {
//					tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_DEBITO;
//				} else {
//					tipoCartao = TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO;
//				}
//			}
//			return tipoCartao;
//		}

	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	

	public void executarValidacaoParaQualLayoutImprimirRelatorio (TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, String textos) throws IOException{		
			if(textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isPdf() && getListaArquivos().size() == 1){
				setCaminhoRelatorio(getListaArquivos().get(0).getName());
				setVisualizarCertificado(false);

				if(getImpressaoContratoExistente()){
					setFazerDownload(false);
				}else{
					setFazerDownload(true);	
				}
			}else {
				setVisualizarCertificado(true);
				setFazerDownload(false);
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textos);	
			}
		}
	
	public Boolean getConsultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades() {
		if (consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades == null) {
			consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades");
		}
		return consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades;
	}
	
	public Boolean getConsultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades() {
		if (consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades == null) {
			consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades");
		}
		return consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades;
	}	
		
		public String getPreviewCertificado() {
				try {
					/*HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
					String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath();*/
					if (Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
						setCaminhoPreviewCertificado(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()+"/relatorio/"+getCaminhoRelatorio() + "?embedded=true");					
						return "RichFaces.$('panelPreviewCertificado').show();";
					}
				} catch (Exception ex) {
					Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
				}

			return "";
		}

		public String getCaminhoPreviewCertificado() {
			return caminhoPreviewCertificado;
		}

		public void setCaminhoPreviewCertificado(String caminhoPreviewCertificado) {
			this.caminhoPreviewCertificado = caminhoPreviewCertificado;
		}
		
		public List<SelectItem> getListaSelectItemTipoTrabalhoConclusaoCurso() {
			return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTrabalhoConclusaoCurso.class);
		}

		public void setListaSelectItemTipoTrabalhoConclusaoCurso(List<SelectItem> listaSelectItemTipoTrabalhoConclusaoCurso) {
			this.listaSelectItemTipoTrabalhoConclusaoCurso = listaSelectItemTipoTrabalhoConclusaoCurso;
		}

		public void naoAceiteCertificado() {
			try {
				setOncompleteModal("");
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getMotivoNaoAceiteCertificado())) {
					if(getCertificadoDigital() && getCertificadoImpresso()) {				
						setOncompleteModal("RichFaces.$('panelSelecionarFormatoCertificado').show()");
					}
					else if(getCertificadoDigital()) {						
						getRequerimentoVO().setFormatoCertificadoSelecionado("DIGITAL");
						gravarRequerimentoComCertificado();
					}
					else if(getCertificadoImpresso()) {
						getRequerimentoVO().setFormatoCertificadoSelecionado("IMPRESSO");
						if(getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso()) {
							setOncompleteModal("RichFaces.$('panelAvisoCobrancaTaxaCertificadoImpresso').show()");	
						}
						else {							
							gravarRequerimentoComCertificado();
						}
					}
					
					//getRequerimentoVO().setFormatoCertificadoSelecionado("IMPRESSO");
					//gravarVisaoAluno();
					//setOncompleteModal("RichFaces.$('panelMotivoNaoAceite').hide()");
				}
				else {				
					throw new Exception("O motivo do não aceite deve ser informado!");				
				}
			} catch (Exception e) {
				 setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
				
		public Boolean getCertificadoDigital() {
			if(certificadoDigital == null) {
				certificadoDigital = Boolean.FALSE;
			}
			return certificadoDigital;
		}

		public void setCertificadoDigital(Boolean certificadoDigital) {
			this.certificadoDigital = certificadoDigital;
		}

		public Boolean getCertificadoImpresso() {
			if(certificadoImpresso == null) {
				certificadoImpresso = Boolean.FALSE;
			}
			return certificadoImpresso;
		}

		public void setCertificadoImpresso(Boolean certificadoImpresso) {
			this.certificadoImpresso = certificadoImpresso;
		}

		public void aceiteCertificado() {
			try {
				if(getCertificadoDigital() && getCertificadoImpresso()) {				
					setOncompleteModal("RichFaces.$('panelSelecionarFormatoCertificado').show()");
				} else if(getCertificadoDigital()) {
					setOncompleteModal("");
					getRequerimentoVO().setFormatoCertificadoSelecionado("DIGITAL");
					gravarRequerimentoComCertificado();
					if(getRequerimentoVO().getIsDeferido()) {
						if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() && getRequerimentoVO().getTipoRequerimento().getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital()) {
							gravarAtualizacaoMatricula();
						}
						if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado()) {
							List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
							montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
						}
						else if(getRequerimentoVO().getTipoRequerimento().getIsDeclaracao() || getRequerimentoVO().getTipoRequerimento().getIsCertificadoParticipacaoTCC() || getRequerimentoVO().getTipoRequerimento().getIsCertificadoModular()) {
							imprirmirDeclaracaoVisaoAdministrativo();
						}
						setOncompleteModal(getDownloadCertificado());
					}
					if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
						consultarVisaoAluno();
					}
				} else if(getCertificadoImpresso()) {
					getRequerimentoVO().setFormatoCertificadoSelecionado("IMPRESSO");
					if(getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso()) {
						setOncompleteModal("RichFaces.$('panelAvisoCobrancaTaxaCertificadoImpresso').show()");	
					}
					else {						
						gravarRequerimentoComCertificado();
					}
					if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
						consultarVisaoAluno();
					}
				}
			} catch (Exception e) {
				setImprimirVisaoAluno(Boolean.FALSE);
				setFazerDownload(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void gerarCertificado(){
			try {		
				setOncompleteModal("");
				if(!getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital() && !getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()){
					if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado()) {
						throw new Exception("Selecione o formato do certificado!");	
					}
					else if(getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()) {
						throw new Exception("Selecione o formato da declaracao!");	
					}
								
				}
				else {
					if((getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso() && (getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() || getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()) && getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()) || (!getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso() && (getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() || getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()))) {
						getFacadeFactory().getRequerimentoFacade().realizarDefinicaoVencimentoContaReceberRequerimento(getRequerimentoVO(), true);
						getRequerimentoVO().setValor(getRequerimentoVO().getTipoRequerimento().getValor());
//						getFacadeFactory().getRequerimentoFacade().realizarValidacaoCobrancaRequerimento(getRequerimentoVO());
						getRequerimentoVO().setExigePagamento(Boolean.TRUE);
					} else if (getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso() && (getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() || getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()) && getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital() && !getRequerimentoVO().getExigePagamento()) {
						getRequerimentoVO().setValor(0.0);
					}
					gravarRequerimentoComCertificado();
					
					if(getRequerimentoVO().getMotivoNaoAceiteCertificado().equals("")) {
						
						if(getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital()) {
							if(getRequerimentoVO().getIsDeferido()) {
								if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado() && getRequerimentoVO().getTipoRequerimento().getRegistrarFormaturaAoRealizarImpressaoCerticadoDigital()) {
									getFacadeFactory().getRequerimentoFacade().gravarAtualizacaoMatricula(getRequerimentoVO(), getUsuarioLogado());
								}
								if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado()) {
									List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
									montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
									getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(getRequerimentoVO().getCodigo());
								}
								else if(getRequerimentoVO().getTipoRequerimento().getIsDeclaracao() || getRequerimentoVO().getTipoRequerimento().getIsCertificadoModular()) {
									imprirmirDeclaracaoVisaoAlunoDeferido();
								}
								setOncompleteModal(getDownloadCertificado());
							}
						}
					}
				}
			} catch (Exception e) {
				setImprimirVisaoAluno(Boolean.FALSE);
				setFazerDownload(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}

		}

		public List<CertificadoCursoExtensaoRelVO> getCertificadoCursoExtensaoRelVOs() {
			if(certificadoCursoExtensaoRelVOs == null) {
			certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
			}
			return certificadoCursoExtensaoRelVOs;
		}

		public void setCertificadoCursoExtensaoRelVOs(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs) {
			this.certificadoCursoExtensaoRelVOs = certificadoCursoExtensaoRelVOs;
		}

		public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
			if(textoPadraoDeclaracaoVO == null) {
				textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
			}
			return textoPadraoDeclaracaoVO;
		}

		public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
			this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
		}
		
	    public void gravarAtualizacaoMatricula() {
	    	String situacaoAnt = null;
			try {
					MatriculaVO matricula = getRequerimentoVO().getMatricula();
					situacaoAnt = matricula.getSituacao();
					matricula.setSituacao("FO");
					matricula.setMsgErro("");
					matricula.setDataAtualizacaoMatriculaFormada(new Date());
					matricula.setResponsavelAtualizacaoMatriculaFormada(getUsuarioLogadoClone());
					getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaFormadaAtualizacao(matricula, getUsuarioLogadoClone());
					//setMensagemID("msg_dados_gravados");

			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}	
	
		public void gerarCertificadoDigital() throws Exception {
			setOncompleteModal("");
			
			if(getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital()) {
				if(getRequerimentoVO().getIsDeferido()) {
					
					setGerarNovoArquivoAssinado(false);
					List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
					montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
					if(!Uteis.isAtributoPreenchido(getRequerimentoVO().getDataUltimaImpressao())) {
						getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(getRequerimentoVO().getCodigo());
					}
					setOncompleteModal(getDownloadCertificado());
				}
			}

		}
		
		public void gerarCertificadoDigitalVisaoAdministrativa() throws Exception {
			setOncompleteModal("");		
				if(getRequerimentoVO().getIsDeferido()) {
					setGerarNovoArquivoAssinado(false);
					List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
					montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
					if(!Uteis.isAtributoPreenchido(getRequerimentoVO().getDataUltimaImpressao())) {
						getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(getRequerimentoVO().getCodigo());
					}
					setOncompleteModal(getDownloadCertificado());
				}
		}
		
		public String getDownloadCertificado() {
			try {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String caminho = request.getRequestURL().toString().replace(request.getRequestURI().toString(), "") + request.getContextPath() + "/";
				return "location.href='" + caminho + "DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'";
			} catch (Exception ex) {
				Logger.getLogger(SuperControleRelatorio.class.getName()).log(Level.SEVERE, null, ex);
			}

			return "";
		}

		public void montarRelatorioPorLayoutTextoPadrao(List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs) throws Exception{
			DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
			if(Uteis.isAtributoPreenchido(getRequerimentoVO())) {
				if (getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()) {
					setGerarNovoArquivoAssinado(false);	
				} else {
					documentoAssinadoVO = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorAlunoTipoOrigemCodigoOrigem(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoVO().getCodigo(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO.name(), TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					setGerarNovoArquivoAssinado(!Uteis.isAtributoPreenchido(documentoAssinadoVO));
				}
			}else {
				setGerarNovoArquivoAssinado(false);
			}
	        List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>(0);
	        CertificadoCursoExtensaoRelVO certificadoCursoExtensaoRelVO= new CertificadoCursoExtensaoRelVO();

			listaMatricula.add(getRequerimentoVO().getMatricula());
        	certificadoCursoExtensaoRelVO.setUnidadeEnsinoVO(listaMatricula.get(0).getUnidadeEnsino());
        	certificadoCursoExtensaoRelVO.setPeriodoLetivoVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getRequerimentoVO().getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        	certificadoCursoExtensaoRelVOs = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().executarConsultaParametrizadaLayout2(listaMatricula, certificadoCursoExtensaoRelVO, "TextoPadrao", true, null, null, false);
					
			
			if (!certificadoCursoExtensaoRelVOs.isEmpty()) {
				if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTextoPadrao()) && (getRequerimentoVO().getFormatoCertificadoSelecionado().equals("") || getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital())) {
					setTextoPadraoDeclaracaoVO(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));									
				}
				else if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso()) && (getRequerimentoVO().getFormatoCertificadoSelecionado().equals("") ||getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso())) {
					setTextoPadraoDeclaracaoVO(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getRequerimentoVO().getTipoRequerimento().getCertificadoImpresso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));									
				}
				
				HashMap<String, Object> hashMap = getFacadeFactory().getCertificadoCursoExtensaoRelFacade().realizarMontagemRelatorioPorTextoPadrao(certificadoCursoExtensaoRelVOs, getListaArquivos(), textoPadraoDeclaracaoVO, null, new GradeDisciplinaVO(),  getRequerimentoVO(),  getConfiguracaoGeralPadraoSistema(), getGerarNovoArquivoAssinado(), Uteis.isAtributoPreenchido(getRequerimentoVO().getCodigo()) ? true : false, getUsuarioLogado());
				setImpressaoContratoExistente(((Boolean)hashMap.get("impressaoContratoExistente")));
				executarValidacaoParaQualLayoutImprimirRelatorio(textoPadraoDeclaracaoVO, ((String)hashMap.get("texto")));
				getListaArquivos().clear();
				
			} else {
				setVisualizarCertificado(false);
			}
		}
		
		public Boolean getGerarNovoArquivoAssinado() {
			if (gerarNovoArquivoAssinado == null) { 
				gerarNovoArquivoAssinado = false;
			}
			return gerarNovoArquivoAssinado;
		}

		public void setGerarNovoArquivoAssinado(Boolean gerarNovoArquivoAssinado) {
			this.gerarNovoArquivoAssinado = gerarNovoArquivoAssinado;
		}
		
		public void gerarCertificadoDigitalRequerimentoItem() throws Exception {
			setOncompleteModal("");

			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItem");
			setRequerimentoVO(obj);
			getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), NivelMontarDados.TODOS, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getRequerimentoVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());

			
			if(getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital()) {
				if(getRequerimentoVO().getIsDeferido()) {

					setGerarNovoArquivoAssinado(false);
					List<CertificadoCursoExtensaoRelVO> certificadoCursoExtensaoRelVOs = new ArrayList<CertificadoCursoExtensaoRelVO>(0);
					montarRelatorioPorLayoutTextoPadrao(certificadoCursoExtensaoRelVOs);
					if(!Uteis.isAtributoPreenchido(getRequerimentoVO().getDataUltimaImpressao())) {
						getFacadeFactory().getRequerimentoFacade().alterarDataUltimaImpressao(getRequerimentoVO().getCodigo());
					}
					
					setOncompleteModal(getDownloadCertificado());
				}
			}

		}
		
		public void avisoCobrancaTaxaCertificadoImpresso() {
			try {
			setOncompleteModal("");
			if(!getRequerimentoVO().getIsFormatoCertificadoSelecionadoDigital() && !getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso()){
				if(getRequerimentoVO().getTipoRequerimento().getIsEmissaoCertificado()) {
					throw new Exception("Selecione o formato do certificado!");						
				}
				else if(getRequerimentoVO().getTipoRequerimento().getIsDeclaracao()) {
					throw new Exception("Selecione o formato da declaracao!");	
				}		
			}else if (getRequerimentoVO().getIsFormatoCertificadoSelecionadoImpresso() && getRequerimentoVO().getTipoRequerimento().getCobrarTaxaSomenteCertificadoImpresso()) {
				setOncompleteModal("RichFaces.$('panelAvisoCobrancaTaxaCertificadoImpresso').show()");
			}else {
				gerarCertificado();
			}
			} catch (Exception e) {
				setImprimirVisaoAluno(Boolean.FALSE);
				setFazerDownload(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			
		}
		
		public void gravarRequerimentoComCertificado() throws Exception {			
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {	
				gravarVisaoAluno();	
			}
			else {
				gravar();
			}			
		}
		
		public Boolean getApresentarOrientacaoDepartamento() {
			return Uteis.isAtributoPreenchido(getRequerimentoVO().getResponsavelTramite());
		}
		
		public Boolean getPodeInserirNota() {
			if (podeInserirNota == null) {
				podeInserirNota = false;
			}
			return podeInserirNota;
		}

		public void setPodeInserirNota(Boolean podeInserirNota) {
			this.podeInserirNota = podeInserirNota;
		}

		public String getMsgNovoRequerimento() {
			if (msgNovoRequerimento == null) {
				msgNovoRequerimento = "";
			}
			return msgNovoRequerimento;
		}

		public void setMsgNovoRequerimento(String msgNovoRequerimento) {
			this.msgNovoRequerimento = msgNovoRequerimento;
		}

		public RequerimentoVO getRequerimentoAntigo() {
			if (requerimentoAntigo == null) {
				requerimentoAntigo = new RequerimentoVO();
			}
			return requerimentoAntigo;
		}

		public void setRequerimentoAntigo(RequerimentoVO requerimentoAntigo) {
			this.requerimentoAntigo = requerimentoAntigo;
		}

		
		public Boolean getPermiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade() {
			if (permiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade == null) {
				permiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade = verificarUsuarioPossuiPermissaoConsulta("Requerimento_consultarRequerimentoOutrosConsultoresResponsaveis");
			}
			return permiteVisualizarRequerimentoOutrosResponsaveisMesmoDepartamentoMesmaUnidade;
		}

		public Boolean getOrigemExterna() {
			if(origemExterna == null) {
				origemExterna = Boolean.FALSE;
			}
			return origemExterna;
		}


		public void setOrigemExterna(Boolean origemExterna) {
			this.origemExterna = origemExterna;
		}
		
		
		
		public Boolean getAbrirModalRequerimentoTCC() {
			if (abrirModalRequerimentoTCC == null) {
				abrirModalRequerimentoTCC = false;
			}
			return abrirModalRequerimentoTCC;
		}


		public void setAbrirModalRequerimentoTCC(Boolean abrirModalRequerimentoTCC) {
			this.abrirModalRequerimentoTCC = abrirModalRequerimentoTCC;
		}
		
		public Boolean getRealizarTramiteRequerimentoOutroDepartamento() {
			if (realizarTramiteRequerimentoOutroDepartamento == null) {
				realizarTramiteRequerimentoOutroDepartamento = verificarUsuarioPossuiPermissaoConsulta("Requerimento_permiteRealizarTramiteRequerimentoOutroDepartamento");
			}
			return realizarTramiteRequerimentoOutroDepartamento;
		}

		public String getMsgRequerimentoExistente() {
			if(msgRequerimentoExistente == null) {
				msgRequerimentoExistente = "";
			}
			return msgRequerimentoExistente;
		}


		public void setMsgRequerimentoExistente(String msgRequerimentoExistente) {
			this.msgRequerimentoExistente = msgRequerimentoExistente;
		}
		
		


		public Boolean getRequerimentoValidado() {
			if(requerimentoValidado == null) {
				requerimentoValidado = false;
			}
			return requerimentoValidado;
		}


		public void setRequerimentoValidado(Boolean requerimentoValidado) {
			this.requerimentoValidado = requerimentoValidado;
		}

		private void carregarDadosRequerimentoOrigemComunicadoInterno() {
				try {
					Integer idRequerimento = (Integer) context().getExternalContext().getSessionMap().get("idRequerimento");
					if(idRequerimento != null && Uteis.isAtributoPreenchido(idRequerimento)) {
						getRequerimentoVO().setCodigo(idRequerimento);
						getRequerimentoVO().setNovoObj(Boolean.FALSE);
						setOrigemExterna(Boolean.TRUE);
						setAbaAtiva("richTramite");
						editar();
					}
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}finally {
				context().getExternalContext().getSessionMap().remove("idRequerimento");
				setOrigemExterna(Boolean.FALSE);
			}
		}
		
		public String realizarNavegacaoMinhasNotasAluno() {
			return "minhasNotasAlunos.xhtml";

		}

	public RequerimentoVO getRequerimentoVOMapaReposicao() {
		if (requerimentoVOMapaReposicao == null) {
			requerimentoVOMapaReposicao = new RequerimentoVO();
		}
		return requerimentoVOMapaReposicao;
	}

	public void setRequerimentoVOMapaReposicao(RequerimentoVO requerimentoVOMapaReposicao) {
		this.requerimentoVOMapaReposicao = requerimentoVOMapaReposicao;
	}


	public List<SelectItem> getListaSelectItemTipoRequerimentoMapaReposicao() {
		if (listaSelectItemTipoRequerimentoMapaReposicao == null) {
			listaSelectItemTipoRequerimentoMapaReposicao = new ArrayList<>();
		}
		return listaSelectItemTipoRequerimentoMapaReposicao;
	}


	public void setListaSelectItemTipoRequerimentoMapaReposicao(List<SelectItem> listaSelectItemTipoRequerimentoMapaReposicao) {
		this.listaSelectItemTipoRequerimentoMapaReposicao = listaSelectItemTipoRequerimentoMapaReposicao;
	}
	
	public void atualizarAlteracaoUnidadeEnsino() {
		montarListaSelectItemTipoRequerimentoMapaReposicao();
		limparDisciplina();
		limparCursoCons();
		limparTurma();
		realizarLimpezaDadosAluno();
		getListaConsultaAluno().clear();
		getListaConsultaDisciplina().clear();
	}

    
	public Map<Integer, List<TurmaVO>> getMapTurmas() {
		if (mapTurmas == null) {
			mapTurmas = new HashMap<Integer, List<TurmaVO>>();
		}
		return mapTurmas;
	}


	public void setMapTurmas(Map<Integer, List<TurmaVO>> mapTurmas) {
		this.mapTurmas = mapTurmas;
	}
	
    public void prepararNavegacaoImpressaoDeclaracao() {
    	removerControleMemoriaFlashTela("ImpressaoDeclaracaoControle");
    }
    
    private Boolean versaoAntiga;
    private Boolean versaoNova;
    
    public Boolean getVersaoAntiga() {
		if (versaoAntiga == null) {
			versaoAntiga = false;
		}
    	return versaoAntiga;
	}
    
    public void setVersaoAntiga(Boolean versaoAntiga) {
		this.versaoAntiga = versaoAntiga;
	}
    
    public Boolean getVersaoNova() {
		if (versaoNova == null) {
			versaoNova = false;
		}
    	return versaoNova;
	}
    
    public void setVersaoNova(Boolean versaoNova) {
		this.versaoNova = versaoNova;
	}
    
    public void mudarLayoutConsulta() {
    	setVersaoAntiga(true);
    	setVersaoNova(false);
    	try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Requerimento.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova",getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void mudarLayoutConsulta2() {
    	setVersaoAntiga(false);
    	setVersaoNova(true);
    	try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), Requerimento.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void montarListaSelectItemMotivoCancelamentoTrancamento() {
		try {
			montarListaSelectItemMotivoCancelamentoTrancamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemMotivoCancelamentoTrancamento(String prm) throws Exception {
		try {
			List<MotivoCancelamentoTrancamentoVO> resultadoConsulta = consultarMotivoCancelamentoTrancamentoPorNomeAtivo(prm);
			setListaSelectItemMotivoCancelamentoTrancamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			if(Uteis.isAtributoPreenchido(getRequerimentoVO()) && Uteis.isAtributoPreenchido(getRequerimentoVO().getMotivoCancelamentoTrancamento())
					&& !getListaSelectItemMotivoCancelamentoTrancamento().stream().anyMatch(t -> ((Integer)t.getValue()).equals(getRequerimentoVO().getMotivoCancelamentoTrancamento().getCodigo()))) {
				getListaSelectItemMotivoCancelamentoTrancamento().add(new SelectItem(getRequerimentoVO().getMotivoCancelamentoTrancamento().getCodigo(), getRequerimentoVO().getMotivoCancelamentoTrancamento().getNome()));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<MotivoCancelamentoTrancamentoVO> consultarMotivoCancelamentoTrancamentoPorNomeAtivo(String nomePrm) throws Exception {
		return getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	/**
	 * @return the listaSelectItemMotivoCancelamentoTrancamento
	 */
	public List<SelectItem> getListaSelectItemMotivoCancelamentoTrancamento() {
		if (listaSelectItemMotivoCancelamentoTrancamento == null) {
			listaSelectItemMotivoCancelamentoTrancamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoCancelamentoTrancamento;
	}

	/**
	 * @param listaSelectItemMotivoCancelamentoTrancamento
	 *            the listaSelectItemMotivoCancelamentoTrancamento to set
	 */
	public void setListaSelectItemMotivoCancelamentoTrancamento(List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento) {
		this.listaSelectItemMotivoCancelamentoTrancamento = listaSelectItemMotivoCancelamentoTrancamento;
	}

	public Boolean getBloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna() {
		if(bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna == null ) {
			bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna = Boolean.FALSE;
		}
		return bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna;
	}

	public void setBloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna(
			Boolean bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna) {
		this.bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna = bloquearAlunoAlterarUnidadeEnsinoTransferenciaInterna;
	}
	
	private String caminhoPreviewAnexo;
	
	
	public String getCaminhoPreviewAnexo() {
		if(caminhoPreviewAnexo == null) {
			caminhoPreviewAnexo = "";
		}
		return caminhoPreviewAnexo;
	}

	public void setCaminhoPreviewAnexo(String caminhoPreviewAnexo) {
		this.caminhoPreviewAnexo = caminhoPreviewAnexo;
	}

	public void realizarVisualizarArquivoRequerimento(ArquivoVO arquivoVO) {
		try {
			setCaminhoPreviewAnexo("");
			if(arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3) && !arquivoVO.getPastaBaseArquivoEnum().toString().endsWith("TMP")) {
				
			}else {
				String caminhoBase = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado()).getUrlExternoDownloadArquivo();
				if(!getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado()).getUrlExternoDownloadArquivo().endsWith("/")) {
					caminhoBase += "/";
				}
				setCaminhoPreviewAnexo(caminhoBase+arquivoVO.getPastaBaseArquivo()+"/"+arquivoVO.getNome()+"?embedded=true");
			}
		}catch (Exception e) {
			
		}
	}


	public Boolean getIsAnexoPDF() {
		return Uteis.isAtributoPreenchido(getCaminhoPreviewAnexo()) && (getCaminhoPreviewAnexo().endsWith(".pdf?embedded=true") || getCaminhoPreviewAnexo().endsWith(".PDF?embedded=true")) ;	
	}
	
	public Boolean getIsAnexoImagem() {
		return Uteis.isAtributoPreenchido(getCaminhoPreviewAnexo()) && (
				getCaminhoPreviewAnexo().endsWith(".jpeg?embedded=true") || getCaminhoPreviewAnexo().endsWith(".JPEG?embedded=true") || getCaminhoPreviewAnexo().endsWith(".jpg?embedded=true")
		        || getCaminhoPreviewAnexo().endsWith(".JPG?embedded=true") || getCaminhoPreviewAnexo().endsWith(".png?embedded=true") || getCaminhoPreviewAnexo().endsWith(".PNG?embedded=true")
		        || getCaminhoPreviewAnexo().endsWith(".gif?embedded=true") || getCaminhoPreviewAnexo().endsWith(".GIF?embedded=true") || getCaminhoPreviewAnexo().endsWith(".bmp?embedded=true")
		        || getCaminhoPreviewAnexo().endsWith(".BMP?embedded=true") || getCaminhoPreviewAnexo().endsWith(".ico?embedded=true") || getCaminhoPreviewAnexo().endsWith(".ICO?embedded=true"));	
	}
	
	public void adicionarRequerimentoDisciplina() {
		try {
			getFacadeFactory().getRequerimentoFacade().adicionarRequerimentoDisciplina(requerimentoVO, getRequerimentoDisciplinaVO(), getUsuarioLogado());
			setRequerimentoDisciplinaVO(new RequerimentoDisciplinaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerRequerimentoDisciplina(RequerimentoDisciplinaVO requerimentoDisciplinaVO) {
		try {
			getFacadeFactory().getRequerimentoFacade().removerRequerimentoDisciplina(requerimentoVO, requerimentoDisciplinaVO, getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarDeferimentoRequerimentoDisciplina(RequerimentoDisciplinaVO requerimentoDisciplinaVO) {
		try {
		requerimentoDisciplinaVO.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO);
		requerimentoDisciplinaVO.setDataDeferimentoIndeferimento(new Date());
		requerimentoDisciplinaVO.setUsuarioDeferimentoIndeferimento(getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarIndeferimentoRequerimentoDisciplina(RequerimentoDisciplinaVO requerimentoDisciplinaVO) {
		try {
			requerimentoDisciplinaVO.setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO);
			requerimentoDisciplinaVO.setDataDeferimentoIndeferimento(new Date());
			requerimentoDisciplinaVO.setUsuarioDeferimentoIndeferimento(getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public RequerimentoDisciplinaVO getRequerimentoDisciplinaVO() {
		if(requerimentoDisciplinaVO == null) {
			requerimentoDisciplinaVO = new RequerimentoDisciplinaVO();
		}
		return requerimentoDisciplinaVO;
	}

	public void setRequerimentoDisciplinaVO(RequerimentoDisciplinaVO requerimentoDisciplinaVO) {
		this.requerimentoDisciplinaVO = requerimentoDisciplinaVO;
	}
	
	public String getCampoConsultaDisciplina() {
        if (campoConsultaDisciplina == null) {
            campoConsultaDisciplina = "";
        }
        return campoConsultaDisciplina;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
    }
    
    
	
	public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }
		
	public void montarListaSelectItemTipoNota() {
		try {
			setHabilitarTipoAvaliacao(Boolean.TRUE);
			limparMensagem();
			if(getRequerimentoVO().getTipoRequerimento().getIsTipoSegundaChamada() 
					&& Uteis.isAtributoPreenchido(getRequerimentoDisciplinaVO().getDisciplina())) {
				getRequerimentoDisciplinaVO().setListaSelectItemNota(getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarConfiguracaoAcademicaNotaPorMatriculaDisciplina(getRequerimentoVO().getMatricula().getMatricula(), getRequerimentoDisciplinaVO().getDisciplina().getCodigo()));
				if(getRequerimentoDisciplinaVO().getListaSelectItemNota().size() <= 1) {
					getRequerimentoDisciplinaVO().setVariavelNota("");
					throw new Exception("Não foi encontrado um TIPO DE AVALIAÇÃO apto para ser informado.");
				}
				definirTipoNotaRequerimento();
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
//  No tipo requerimento de 2ª chamada pode ser informado um tipo nota. Caso selecioando, o requerimento obrigatoriamente usará ele
	public void definirTipoNotaRequerimento() {
		Uteis.checkState(getRequerimentoDisciplinaVO().getListaSelectItemNota().stream().noneMatch(i -> i.getValue().equals(getRequerimentoVO().getTipoRequerimento().getTipoNota())), "Não foi encontrada nenhum NOTA na configuração acadêmica que corresponda à selecionada no TIPO REQUERIMENTO.");
		if(Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento().getTipoNota())) {
			getRequerimentoDisciplinaVO().getListaSelectItemNota().stream()
			.filter(item -> item.getValue().equals(getRequerimentoVO().getTipoRequerimento().getTipoNota()))
			.findFirst()
			.ifPresent(i -> {
				getRequerimentoDisciplinaVO().setVariavelNota(i.getValue().toString());
				setHabilitarTipoAvaliacao(Boolean.FALSE);
			});
		}
	}
	
	public String navegarAbaTramite() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return navegarAba("richTramite", "requerimentoForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return navegarAba("richTramite", "requerimentoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return navegarAba("richTramite", "requerimentoCoordenadorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
			return navegarAba("richTramite", "requerimentoAluno.xhtml");
		}
		return "";
	}
	
	public String navegarAbaDadosBasicos() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return navegarAba("richTab", "requerimentoForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return navegarAba("richTab", "requerimentoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return navegarAba("richTab", "requerimentoCoordenadorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
			return navegarAba("richTab", "requerimentoAluno.xhtml");
		}
		return "";
	}
	
	public String redirecionarAbaAnexo() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAnexoAdministrativoForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAnexoProfessorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAnexoCoordenadorForm.xhtml");
		} else if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("requerimentoAnexoAlunoForm.xhtml");
		}
		return "";
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

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}

	public void consultarDisciplinaRequerimentoCons() {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplina(), 0, 0, 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public Boolean getApresentarBloquearAproveitamento() {
		if (apresentarBloquearAproveitamento == null) {
			apresentarBloquearAproveitamento = false;
		}
		return apresentarBloquearAproveitamento;
	}

	public void setApresentarBloquearAproveitamento(Boolean apresentarBloquearAproveitamento) {
		this.apresentarBloquearAproveitamento = apresentarBloquearAproveitamento;
	}
	
	public List<SelectItem> getListaSelectItemGrupoFacilitador() {
		if (listaSelectItemGrupoFacilitador == null) {
			listaSelectItemGrupoFacilitador = new ArrayList<>(0);
		}
		return listaSelectItemGrupoFacilitador;
	}
	
	public void setListaSelectItemGrupoFacilitador(List<SelectItem> listaSelectItemGrupoFacilitador) {
		this.listaSelectItemGrupoFacilitador = listaSelectItemGrupoFacilitador;
	}
	
	public void montarListaSelectItemGrupoFacilitador() {
		try {
			setListaSelectItemGrupoFacilitador(new ArrayList<>(0));
			if (Uteis.isAtributoPreenchido(getRequerimentoVO().getTipoRequerimento()) && getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.CERTIFICADO_PARTICIPACAO_TCC.getValor())) {
				if (!(getRequerimentoVO().getMatricula() != null && Uteis.isAtributoPreenchido(getRequerimentoVO().getMatricula().getMatricula()))) {
					throw new Exception("O FACILITADOR deve ser informado para a montagem do GRUPO.");
				}
				List<SalaAulaBlackboardVO> grupos = getFacadeFactory().getSalaAulaBlackboardFacade().consultarGruposPorFacilitador(getRequerimentoVO().getMatricula().getAluno().getCodigo());
				if (Uteis.isAtributoPreenchido(grupos)) {
					grupos.stream().map(g -> new SelectItem(g.getCodigo(), g.getNomeGrupo())).forEach(getListaSelectItemGrupoFacilitador()::add);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> completeUnidadeEnsinoTransferencia(FacesContext facesContext, UIComponent component, final String prefix) {
		if(Uteis.isAtributoPreenchido(prefix.trim())) {
			return getListaSelectItemUnidadeEnsinoTransferencia().stream().filter(u -> Uteis.removerAcentuacao(u.getLabel()).toLowerCase().contains(Uteis.removerAcentuacao(prefix).toLowerCase())).collect(Collectors.toList());
		}
		return getListaSelectItemUnidadeEnsinoTransferencia();
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoTransferencia() {
		if(listaSelectItemUnidadeEnsinoTransferencia == null) {
			listaSelectItemUnidadeEnsinoTransferencia =  new ArrayList<SelectItem>(0);
			montarListaSelectItemUnidadeEnsinoTransferenciaInterna();
		}
		return listaSelectItemUnidadeEnsinoTransferencia;
	}

	public void setListaSelectItemUnidadeEnsinoTransferencia(List<SelectItem> listaSelectItemUnidadeEnsinoTransferencia) {
		this.listaSelectItemUnidadeEnsinoTransferencia = listaSelectItemUnidadeEnsinoTransferencia;
	}
	
	
	
	public Boolean getMarcarTodos() {
		if(marcarTodos == null) {
			marcarTodos =  false;
		}
		
		return marcarTodos;
	}

	public void setMarcarTodos(Boolean marcarTodos) {
		this.marcarTodos = marcarTodos;
	}

	public void marcarDesmarcarTodos() {
		((List<RequerimentoVO>)getControleConsultaOtimizado().getListaConsulta()).forEach(r -> r.setSelecionado(getMarcarTodos()));
	}

//	public List<RequerimentoResumoOperacaoLoteVO> getResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino() {
//		if(resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino == null) {
//			resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino =  new ArrayList<RequerimentoResumoOperacaoLoteVO>(0);
//		}
//		return resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino;
//	}
//
//	public void setResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino(
//			List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino) {
//		this.resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino = resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino;
//	}
//
//	public List<RequerimentoResumoOperacaoLoteVO> getResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso() {
//		if(resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso == null) {
//			resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso =  new ArrayList<RequerimentoResumoOperacaoLoteVO>(0);
//		}
//		return resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso;
//	}
//
//	public void setResumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso(
//			List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso) {
//		this.resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso = resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso;
//	}	
	
//	public void selecionarResumoTransferenciaInterna(RequerimentoResumoOperacaoLoteVO resumo, boolean selecionar, boolean unidadeEnsinoCurso) {
//	    listaConsultaResumoTransferenciaInternaSelecionados = new ArrayList<>();
//
//		
//		((List<RequerimentoVO>)getControleConsultaOtimizado().getListaConsulta()).forEach(r -> { 
//			if(r.getUnidadeEnsinoTransferenciaInternaVO().getCodigo().equals(resumo.getUnidadeEnsinoVO().getCodigo())
//					&& r.getTipoRequerimento().getCodigo().equals(resumo.getTipoRequerimentoVO().getCodigo())
//					&& ((Uteis.isAtributoPreenchido(resumo.getCursoVO()) && resumo.getCursoVO().getCodigo().equals(r.getCursoTransferenciaInternaVO().getCodigo())) || !Uteis.isAtributoPreenchido(resumo.getCursoVO()))){
//				r.setSelecionado(selecionar);
//				if(unidadeEnsinoCurso) {
//					setConsultarVagaOfertadaUnidadeEnsinoCurso(true);
//				}
//				listaConsultaResumoTransferenciaInternaSelecionados.add(r);
//				
//				setQtdeRequerimentoDeferido(resumo.getQtdeRequerimentoDeferido());
//			}			
//		});
//	}
	
	public List<PessoaVO> autocompleteRegistroAcademicoPessoa(Object suggest) {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			return getFacadeFactory().getPessoaFacade().consultaRapidaPorRegistroAcademicoAutoComplete((String) suggest, unidadeEnsinoVOs, 10, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()) ;
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<PessoaVO>();
		}
	}

	public String getAutocompleteValorRegistroAcademico() {
		if (autocompleteValorRegistroAcademico == null) {
			autocompleteValorRegistroAcademico = "";
		}
		return autocompleteValorRegistroAcademico;
	}

	public void setAutocompleteValorRegistroAcademico(String autocompleteValorRegistroAcademico) {
		this.autocompleteValorRegistroAcademico = autocompleteValorRegistroAcademico;
	}
	
	public void selecionarRegistroAcademico() {
		consultarMatriculaPorRegistroAcademico(getAutocompleteValorRegistroAcademico());
	}
	
	public void consultarMatriculaPorRegistroAcademico(String registroAcademico) {
		try {
			PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorRegistroAcademico(registroAcademico, Boolean.FALSE, getUsuarioLogado());
			getRequerimentoConsVO().getMatricula().setAluno(obj);
			setAutocompleteValorRegistroAcademico(obj.getRegistroAcademico() + " (" + obj.getNome() + ")");
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAlunoCons() {
		try {
			List<PessoaVO> objs = new ArrayList<>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaPorRegistroAcademico(getValorConsultaAluno(), unidadeEnsinoVOs, Constantes.EMPTY, true, false, 0, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorNome(getValorConsultaAluno(), unidadeEnsinoVOs, Constantes.EMPTY, false, 0, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorUnidadeEnsinoPorCurso(getValorConsultaAluno(), unidadeEnsinoVOs, false, getUsuarioLogado());
			}
			setListaConsultaAlunoCons(objs);
			if (objs.isEmpty()) {
				setMensagemID("msg_erro_dadosnaoencontrados");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setListaConsultaAlunoCons(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAlunoCons() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoConsItens");
			obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getRequerimentoConsVO().getMatricula().setAluno(obj);
			setAutocompleteValorRegistroAcademico(obj.getRegistroAcademico() + " (" + obj.getNome() + ")");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparConsultaMatricula() {
		getListaConsultaAluno().clear();
		setValorConsultaAluno("");
	}
	
	public void limparDadosRegistroAcademicoCons() {
		getRequerimentoConsVO().setMatricula(new MatriculaVO());
		setAutocompleteValorRegistroAcademico("");
	}
	
	public void blurListenerZeraConsultaMatricula() {
		if(!getAutocompleteValorRegistroAcademico().equals(valorMatricula)) {
			getRequerimentoVO().setMatricula(new MatriculaVO());
			getRequerimentoConsVO().setMatricula(new MatriculaVO());
			setAutocompleteValorRegistroAcademico("");
		}
	}
	
	public void consultarTurmaCons() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<>(0);
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = Uteis.isAtributoPreenchido(getUnidadeEnsinoVOs()) ? getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toList()) : new ArrayList<>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), unidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurmaCons() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			setTurmaVO(obj);
			setAutocompleteValorTurma(obj.getIdentificadorTurma() + " " + "(" + obj.getCodigo() + ")");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemCidTipoRequerimento() {
		if (listaSelectItemCidTipoRequerimento == null) {
			listaSelectItemCidTipoRequerimento = new ArrayList<>(0);
		}
		return listaSelectItemCidTipoRequerimento;
	}

	public void setListaSelectItemCidTipoRequerimento(List<SelectItem> listaSelectItemCidTipoRequerimento) {
		this.listaSelectItemCidTipoRequerimento = listaSelectItemCidTipoRequerimento;
	}
	
	public void montarListaSelectItemCidTipoRequerimento() {
		try {
			montarListaSelectItemCidTipoRequerimento("");
		} catch (Exception e) {
		}
	}
	
	public void montarListaSelectItemCidTipoRequerimento(String prm)  throws Exception {
		try {
			List<CidTipoRequerimentoVO> resultadoConsulta = consultarCidTipoRequerimentoComboBox();
			getRequerimentoVO().getTipoRequerimento().setCidTipoRequerimentoVOs(resultadoConsulta);
//			setListaSelectItemCidTipoRequerimento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricaoCid"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public List<CidTipoRequerimentoVO> consultarCidTipoRequerimentoComboBox() throws Exception {
		List<CidTipoRequerimentoVO> lista = new ArrayList<>(0);
		realizarVerificacaoPermissaoConsultarRequerimentosProprioUsuario();	
		lista = getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().consultarCidPorTipoRequerimento(getRequerimentoVO().getTipoRequerimento(), getUsuarioLogado());
		return lista;
	}
	
	public Boolean getApresentarListaCids() throws Exception {
		List<CidTipoRequerimentoVO> lista = getFacadeFactory().getCidTipoRequerimentoInterfaceFacade().consultarCidPorTipoRequerimento(getRequerimentoVO().getTipoRequerimento(), getUsuarioLogado());
		return getRequerimentoVO().getTipoRequerimento().getTipo().equals("SEGUNDA_CHAMADA") &&  Uteis.isAtributoPreenchido(lista) && Uteis.isAtributoPreenchido(getRequerimentoVO().getMatricula().getMatricula());
	}
	
	public void marcarTodosCidsAction() {
		for (CidTipoRequerimentoVO cids : getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs()) {
			cids.setSelecionado(getMarcarTodosCids());
		}
		verificarTodosCidsSelecionados();
	}
	
	
	public void marcarTodosCids() {
		for (CidTipoRequerimentoVO cids : getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs()) {
			cids.setSelecionado(getMarcarTodosCids());
		}
		verificarTodosCidsSelecionados();
	}
	public void verificarTodosCidsSelecionados() {
		StringBuilder cid = new StringBuilder();
		if(getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs().size() > 1) {
			for (CidTipoRequerimentoVO obj : getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs()) {
				if (obj.getSelecionado()) {
					cid.append(obj.getNomeCid_Apresentar().trim()).append("; ");
				}
			}
			getRequerimentoVO().setCid(cid.toString());
		} else {
			if (!getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs().isEmpty()) {
				if ( getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs().get(0).getSelecionado()) {
					getRequerimentoVO().setCid(getRequerimentoVO().getTipoRequerimento().getCidTipoRequerimentoVOs().get(0).getDescricaoCid());
				}
			}
		}
		
	}
	
	public Boolean getMarcarTodosCids() {
		if (marcarTodosCids == null) {
			marcarTodosCids = Boolean.FALSE;
		}
		return marcarTodosCids;
	}

	public void setMarcarTodosCids(Boolean marcarTodosCids) {
		this.marcarTodosCids = marcarTodosCids;
	}
	
	public List<PessoaVO> getListaConsultaAlunoCons() {
		if (listaConsultaAlunoCons == null) {
			listaConsultaAlunoCons = new ArrayList<>(0);
		}
		return listaConsultaAlunoCons;
	}
	
	public void setListaConsultaAlunoCons(List<PessoaVO> listaConsultaAlunoCons) {
		this.listaConsultaAlunoCons = listaConsultaAlunoCons;
	}
	
	public List<SelectItem> getTipoConsultaComboAlunoCons() {
		if (tipoConsultaComboAlunoCons == null){
			tipoConsultaComboAlunoCons = new ArrayList<SelectItem>(0);
			tipoConsultaComboAlunoCons.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAlunoCons.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAlunoCons.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAlunoCons;
	}
	
	public void setTipoConsultaComboAlunoCons(List<SelectItem> tipoConsultaComboAlunoCons) {
		this.tipoConsultaComboAlunoCons = tipoConsultaComboAlunoCons;
	}
	
	private String realizarDownloadArquivoRequerimento(ArquivoVO obj) {
		try {
			context().getExternalContext().getSessionMap().put("arquivoVO", obj);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public String realizarDownloadArquivoAnexoRequerimento() {
		return realizarDownloadArquivoRequerimento(getRequerimentoVO().getArquivoVO());
	}
	
	public List<SelectItem> getQtdRequerimento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(10, "10"));
		itens.add(new SelectItem(25, "25"));
		itens.add(new SelectItem(50, "50"));
	    itens.add(new SelectItem(75, "75"));
	    itens.add(new SelectItem(100, "100"));

		return itens;
	}
    public int getQtdRegistrosPorPagina() {
    	if(qtdRegistrosPorPagina == 0) {
    		qtdRegistrosPorPagina = 10;
    	}
        return qtdRegistrosPorPagina;
    }

    public void setQtdRegistrosPorPagina(int qtdRegistrosPorPagina) {
        this.qtdRegistrosPorPagina = qtdRegistrosPorPagina;
    }

	public void persistirLayoutPadraoQuantidade(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "RequerimentoControle", "qtdRegistrosPorPagina", getUsuarioLogado());
	}

		public Boolean getHabilitarTipoAvaliacao() {
			if(habilitarTipoAvaliacao == null) {
				habilitarTipoAvaliacao = Boolean.TRUE;
			}
			return habilitarTipoAvaliacao;
		}

		public void setHabilitarTipoAvaliacao(Boolean habilitarTipoAvaliacao) {
			this.habilitarTipoAvaliacao = habilitarTipoAvaliacao;
		}
		
		private Boolean consultarVagaOfertadaUnidadeEnsinoCurso;

		public Boolean getConsultarVagaOfertadaUnidadeEnsinoCurso() {
			if(consultarVagaOfertadaUnidadeEnsinoCurso == null) {
				consultarVagaOfertadaUnidadeEnsinoCurso = false;
			}
			return consultarVagaOfertadaUnidadeEnsinoCurso;		
		}

		public void setConsultarVagaOfertadaUnidadeEnsinoCurso(Boolean consultarVagaOfertadaUnidadeEnsinoCurso) {
			this.consultarVagaOfertadaUnidadeEnsinoCurso = consultarVagaOfertadaUnidadeEnsinoCurso;
		}
		private Integer qtdeRequerimentoDeferido;

		public Integer getQtdeRequerimentoDeferido() {
			if(qtdeRequerimentoDeferido == null) {
				qtdeRequerimentoDeferido = 0;
			}
			return qtdeRequerimentoDeferido;
		}

		public void setQtdeRequerimentoDeferido(Integer qtdeRequerimentoDeferido) {
			this.qtdeRequerimentoDeferido = qtdeRequerimentoDeferido;
		}
		public Boolean getMostrarAfastamento() {
		    return getRequerimentoVO().getTipoRequerimento().getTipo().equals("OU")
		           && getRequerimentoVO().getTipoRequerimento().getCampoAfastamento();
		}

		
		public Boolean verificarVagaOfertadaDisponivelUnidadeEnsino(RequerimentoVO requerimentoVO) throws Exception {
			Integer vagaOfertadaUnidadeEnsinoTransferenciaInterna = requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getNumeroVagaOfertada();
			Boolean vagaOfertadaDisponivel;
			if(consultarVagaOfertadaUnidadeEnsinoCurso) {
				setQtdeRequerimentoDeferido(getFacadeFactory().getRequerimentoFacade().consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo() , requerimentoVO.getCursoTransferenciaInternaVO().getCodigo(), requerimentoVO.getTipoRequerimento().getCodigo()));
			} else {
				setQtdeRequerimentoDeferido(getFacadeFactory().getRequerimentoFacade().consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(requerimentoVO.getUnidadeEnsinoTransferenciaInternaVO().getCodigo() , null, requerimentoVO.getTipoRequerimento().getCodigo()));
			}
			if(getQtdeRequerimentoDeferido() < vagaOfertadaUnidadeEnsinoTransferenciaInterna) {
				return vagaOfertadaDisponivel = true;

			}				
			return vagaOfertadaDisponivel = false;
		}
		
		private List<RequerimentoVO> listaConsultaResumoTransferenciaInternaSelecionados;

		public List<RequerimentoVO> getListaConsultaResumoTransferenciaInternaSelecionados() {
			if(listaConsultaResumoTransferenciaInternaSelecionados == null) {
				return listaConsultaResumoTransferenciaInternaSelecionados = new ArrayList<RequerimentoVO>();
			}
			return listaConsultaResumoTransferenciaInternaSelecionados;
		}

		public void setListaConsultaResumoTransferenciaInternaSelecionados(List<RequerimentoVO> listaConsultaResumoTransferenciaInternaSelecionados) {
			this.listaConsultaResumoTransferenciaInternaSelecionados = listaConsultaResumoTransferenciaInternaSelecionados;
		}
		

		public void deferirLoteTransferenciaInternaEspecifico() {
			ConsistirException consistirException =  new ConsistirException();
			if(!getPermiteDeferir()){
				consistirException.adicionarListaMensagemErro("O usuário não possui permissão para DEFERIR o REQUERIMENTO");
				setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
			}else {
			    List<RequerimentoVO> listaOrdenadaRequerimentoVO = ((List<RequerimentoVO>) getListaConsultaResumoTransferenciaInternaSelecionados())
			                .stream()
			                .sorted(Comparator.comparing(RequerimentoVO::getData))
			                .collect(Collectors.toList());
				for(RequerimentoVO requerimentoVO : listaOrdenadaRequerimentoVO) {
					if(requerimentoVO.getSelecionado()) {
						try {
							deferirTransferenciaInternaEspecifico(requerimentoVO, true);
						}catch (Exception e) {
							consistirException.adicionarListaMensagemErro(e.getMessage());
						}
					}
				}	
				consultarOtimizado();
				setMensagemID("msg_requerimento_em_lote_realizada", Uteis.SUCESSO);			
				if(consistirException.existeErroListaMensagemErro()) {
					setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
				}
			}
		}
		
		public void deferirTransferenciaInternaEspecifico(RequerimentoVO requerimentoVO, boolean retornarExcecao) throws Exception {
			String situacaoTmp = getRequerimentoVO().getSituacao();
			try {
				if(!retornarExcecao && !getPermiteDeferir()){
					throw new Exception("O usuário não possui permissão para DEFERIR o REQUERIMENTO");
				}
				setRequerimentoVO(requerimentoVO);
				getFacadeFactory().getRequerimentoFacade().carregarDados(getRequerimentoVO(), getUsuarioLogado());
				getRequerimentoVO().getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getRequerimentoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getRequerimentoVO().getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(getRequerimentoVO().getTipoRequerimento().getCodigo(), false, getUsuarioLogado()));
				setRequerimentoHistoricoVO(getFacadeFactory().getRequerimentoFacade().realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(getRequerimentoVO(), getUsuarioLogado()));		
				if(requerimentoVO.getTipoRequerimento().getIsTipoTransferenciaInterna()) {
					if(verificarVagaOfertadaDisponivelUnidadeEnsino(requerimentoVO)) {
						getFacadeFactory().getRequerimentoFacade().deferirRequerimento(getRequerimentoVO(), getRequerimentoHistoricoVO(),getUsuarioLogado());
						this.enviarEmailAlunoSobreTramiteRequerimento();
					}else{
						indeferirEspecifico(requerimentoVO, false);			
		            	setMensagemDetalhada("msg_erro", "Requerimento: "+ requerimentoVO.getCodigo()+ " indeferido, o polo não possui mais vagas disponíveis.", Uteis.ERRO);
		            }
				}
			}catch (Exception e) {
				getRequerimentoVO().setSituacao(situacaoTmp);
				if(retornarExcecao) {
					throw e;
				}
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			
		}
	public void indeferirLoteTransferenciaInternaEspecifico() {
		ConsistirException consistirException =  new ConsistirException();
		if(!getPermiteIndeferir()){
			consistirException.adicionarListaMensagemErro("O usuário não possui permissão para INDEFERIR o REQUERIMENTO");
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}else {
			for(RequerimentoVO requerimentoVO: (List<RequerimentoVO>) getListaConsultaResumoTransferenciaInternaSelecionados()) {
				if(requerimentoVO.getSelecionado()) {
					try {
						indeferirEspecifico(requerimentoVO, true);
					}catch (Exception e) {
						consistirException.adicionarListaMensagemErro(e.getMessage());
					}
				}
			}
			consultarOtimizado();
			if(consistirException.existeErroListaMensagemErro()) {
				setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
			}
		}
	}
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			getRequerimentoConsVO().getUnidadeEnsino().setNome(unidade.toString());
			setUnidadeEnsinosApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getRequerimentoConsVO().getUnidadeEnsino().setNome(getUnidadeEnsinoVOs().get(0).getNome());
					setUnidadeEnsinosApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				} else {
					getRequerimentoConsVO().getUnidadeEnsino().setNome("");
					setUnidadeEnsinosApresentar("");
				}
			}
		}
		atualizarListaComboBox();
//		if (Uteis.isAtributoPreenchido(getRequerimentoConsVO().getMatricula())) {
//			limparDadosMatriculaCons();
//		}
		if (Uteis.isAtributoPreenchido(getTurmaVO())) {
			limparDadosTurma();
		}
		if (Uteis.isAtributoPreenchido(getCursoVO())) {
			limparDadosCurso();
		}
	}
	public Integer calcularComputadoresDisponiveis(Integer quantidadeComputadoresAluno, Integer quantidadeMatriculados){
			Integer disponiveis = quantidadeComputadoresAluno - quantidadeMatriculados;
			if(disponiveis < 0){
				disponiveis = 0;
			}
			return disponiveis;
	}
	public Integer calcularRequerimentosDisponiveis(Integer numeroVagaOfertada, Integer qtdeRequerimentoDeferido){
			Integer requerimentosDisponiveis = numeroVagaOfertada - qtdeRequerimentoDeferido;
			if(requerimentosDisponiveis < 0){
				requerimentosDisponiveis = 0;
			}
			return requerimentosDisponiveis;
	}


	}
