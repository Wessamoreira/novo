package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas expedicaoDiplomaForm.jsp expedicaoDiplomaCons.jsp) com as funcionalidades
 * da classe <code>ExpedicaoDiploma</code> . Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ExpedicaoDiploma
 * @see ExpedicaoDiplomaVO
 */
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.FilterFactory;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.DeclaracaoAcercaProcessoJudicialVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;
import negocio.comuns.academico.ObservacaoComplementarVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.diplomaDigital.versao1_05.TMotivoAnulacao;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ControleConsultaExpedicaoDiploma;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.AtributoComparacao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DiplomaAlunoRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.HistoricoAlunoRel;

@Controller("ExpedicaoDiplomaControle")
@Scope("viewScope")
@Lazy
public class ExpedicaoDiplomaControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 5480935427069935197L;

	private ExpedicaoDiplomaVO expedicaoDiplomaVO;
	private String campoConsultarMatricula;
	private String valorConsultarMatricula;
	private List listaConsultarMatricula;
	private Boolean habilitaBotaoImprimirDiploma;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List listaCargoFuncionarioPrincipal;
	private List listaCargoFuncionarioSecundario;
	private List listaCargoFuncionarioTerceiro;
	private List listaCargoFuncionarioQuarto;
	private List listaCargoFuncionarioQuinto;
	private List listaSelectItemFuncionario;
	private List listaCargoReitor;
	private Boolean funcionarioPrincipal;
	private Boolean funcionarioSecundario;
	private Boolean funcionarioTerceiro;
	private Boolean funcionarioQuarto;
	private Boolean funcionarioQuinto;	
	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;
	private FuncionarioVO funcionarioTerceiroVO;
	private FuncionarioVO funcionarioQuartoVO;
	private FuncionarioVO funcionarioQuintoVO;	
	private CargoVO cargoFuncionarioPrincipal;
	private CargoVO cargoFuncionarioSecundario;
	private CargoVO cargoFuncionarioTerceiro;
	private CargoVO cargoFuncionarioQuarto;
	private CargoVO cargoFuncionarioQuinto;
	private String tituloFuncionarioPrincipal;
	private String tituloFuncionarioSecundario;
	private String tituloFuncionarioTerceiro;
	private String tituloFuncionarioQuarto;
	private String tituloFuncionarioQuinto;
	private boolean imprimirDiploma;
	private Boolean existeColacao;
	private ColacaoGrauVO colacaoGrauVO;
	private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
	private Date valorConsultaData;
	private String tipoLayout;
	private String abrirModalVerso;
	private List<MatriculaVO> listaMatriculas;
	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer textoPadraoDeclaracao;
	private List listaSelectItemTipoTextoPadrao;
	private Boolean abrirModalOK;
	private String observacaoDiploma;
	private Boolean imprimirContrato;
	private Boolean apresentarProgramacaoFormatura ;

	private List<ObservacaoComplementarVO> listaObservacaoComplementarVOs;

	private String layoutImpressao;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;
	private boolean permitirExibirRegraEmissao = false;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura;
	private String valorConsultaProgramacaoFormatura;
	private String campoConsultaProgramacaoFormatura;
    private Boolean mostrarSegundoCampoProgramacaoFormatura;
    private Date valorConsultaDataInicioProgramacaoFormatura;
    private Date valorConsultaDataFinalProgramacaoFormatura;
    private String filtroAlunosPresentesColacaoGrau;
    private Boolean trazerAlunosComDiplomaEmitido;   
    private List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOs;
    private Boolean apresentarResultadoConsultaExpedicaoDiploma;
    private Boolean marcarTodasExpedicaoDiplomaUtilizarMatriz;
    private Date dataExpedicaoDiplomaLote;
    private Boolean apresentarListaErros;
    public List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOErros;
    private ProgressBarVO progressBarVO;
    private Boolean possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote;
    private String onCompleteDownloadExpedicaoDiplomaLote;
	private String onCompleteRejeicaoDocumento;
	private String motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	private DocumentoAssinadoVO documentoAssinadoRejeicao;
	private Boolean gerarXmlDiplomaLote;
	private Date dataPublicacaoDiarioOficialLote;
	private Date dataRegistroDiplomaLote;
	private Boolean lancarExcecao;
	private DocumentoAssinadoVO documentoAssinadoRegrarRepresentacaoVisual;
	private Boolean possuiPermissaoRegerarCodigoValidacaoDiploma;
	private String modalErro;
	private List<String> listaMensagensErro;
	private List<SelectItem> listaSelectItemTipoAutorizacaoCurso;
	protected List<CidadeVO> listaConsultaCidade;
	protected String valorConsultaCidade;
	protected String campoConsultaCidade;
	private DeclaracaoAcercaProcessoJudicialVO declaracaoAcercaProcessoJudicialVO;
	private Boolean selecionarDeclaracao;
	private List<SelectItem> listaSelectItemVersaoDiploma;
	private VersaoDiplomaDigitalEnum versaoDiplomaLote;
	private UnidadeEnsinoVO unidadeEnsinoCertificadoraLote;
	private Integer cargaHorariaTotal;
	private Integer cargaHorariaCursada;
	private String motivoRejeicaoDiploma;
	private Boolean apresentarAnularExpedicaoDiploma;
	private List<SelectItem> listaSelectItemMotivoAnulacao;
	private List<SelectItem> tipoConsultarComboMatricula;
	private ControleConsultaExpedicaoDiploma controleConsultaExpedicaoDiploma;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private Boolean versaoLayoutListaNovo;
	private Boolean informarCamposLivroRegistradoraLote;
	
	private static final String XML_NAO_GERADO = "xmlPendenteGeracao";
	private static final String XML_ASSINADO = "xmlAssinado";
	private static final String XML_PENDENTE = "xmlPendente";
	private static final String XML_REJEITADO = "xmlRejeitado";
	private String filtroSituacaoDiplomaDigitalLote;
	private String filtroSituacaoDocumentacaoAcademicaLote;
	private List<SelectItem> selectItemSituacaoXMLLote;
	private TipoOrigemDocumentoAssinadoEnum tipoXmlMecGerar;
	private List<SelectItem> listaSelectItemTipoXmlMec;
	private List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaCorrigirDocumentacaoMatricula;

	public ExpedicaoDiplomaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setControleConsultaExpedicaoDiploma(new ControleConsultaExpedicaoDiploma());
		montarListaSelectItemUnidadeEnsino(Constantes.EMPTY);
		setImprimirContrato(Boolean.FALSE);
		getExpedicaoDiplomaVO().setExisteDiplomaDigitalGerado(Boolean.FALSE);
		verificarPermissaoRegraEmissao();
//		verificarPermissaoRegerarCodigoValidacaoDiploma();
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void iniciarExpedicaoOutraTela() throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request.getSession().getAttribute("matricula") != null) {
			if (request.getSession().getAttribute("matricula").equals(Constantes.EMPTY)) {
				return;
			}
			try {
				this.getExpedicaoDiplomaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnicaParaExpedicaoDiploma((String) request.getSession().getAttribute("matricula"), 0, false, Constantes.EMPTY, getUsuarioLogado()));
				getExpedicaoDiplomaVO().getGradeCurricularVO().setCodigo(getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo());
				montarListaGradeCurricular();
				getMostrarCampoCursoGradeCurricular();
				verificarLayoutPadrao();
				if (this.getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
					this.getExpedicaoDiplomaVO().getMatricula().setDataConclusaoCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorMatricula(this.getExpedicaoDiplomaVO().getMatricula().getMatricula()));
					if (this.getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso() == null) {
						this.getExpedicaoDiplomaVO().getMatricula().setDataInicioCurso(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorMatricula(this.getExpedicaoDiplomaVO().getMatricula().getMatricula()));
					}
				}
				montarListaSelectItemUnidadeEnsino();				
				request.getSession().removeAttribute("matricula");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			} finally {
				setValorConsultarMatricula(null);
				setCampoConsultarMatricula(null);
				getListaConsultarMatricula().clear();
			}
		}
	}
	
	
	 private void verificarPermissaoRegraEmissao() {
	    	try {
	    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_DEFINIR_REGRA_EMISSAO_DIPLOMA, getUsuarioLogado());
	    		setPermitirExibirRegraEmissao(Boolean.TRUE);
			} catch (Exception e) {
				setPermitirExibirRegraEmissao(Boolean.FALSE);
			}
	    }
	public String novo() {
		removerObjetoMemoria(this);
		setListaSelectItemUnidadeEnsino(new ArrayList<>());
		setExpedicaoDiplomaVO(new ExpedicaoDiplomaVO());
		setHabilitaBotaoImprimirDiploma(Boolean.FALSE);
		getListaMensagensErro().clear();
		setDeclaracaoAcercaProcessoJudicialVO(new DeclaracaoAcercaProcessoJudicialVO());
		getExpedicaoDiplomaVO().setResponsavelCadastro(getUsuarioLogado());
		getListaDocumentoAsssinados().clear();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
	}
	
	public String navegarExpedicaoDiplomaLote() {
		removerObjetoMemoria(this);
		setExpedicaoDiplomaVO(new ExpedicaoDiplomaVO());
		setHabilitaBotaoImprimirDiploma(Boolean.FALSE);
		setApresentarProgramacaoFormatura(Boolean.FALSE);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaLote.xhtml");
	}

	@Override
	public SuperControle getControlador(String nomeControlador) {
		return super.getControlador(nomeControlador);
	}
	
	@SuppressWarnings("finally")
	public String editar() throws Exception {
		ExpedicaoDiplomaVO obj = (ExpedicaoDiplomaVO) context().getExternalContext().getRequestMap().get("expedicaoDiplomaItens");
		try {
			setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().carregarDadosCompletoExpedicaoDiploma(obj.getCodigo(), getUsuarioLogado()));
			getExpedicaoDiplomaVO().setDiplomaDigital(obj.getDiplomaDigital());
			setListaSelectItemUnidadeEnsino(new ArrayList<>());
			getListaMensagensErro().clear();
			setHabilitaBotaoImprimirDiploma(Boolean.TRUE);
			montarDadosUltimoDiplomaAluno();
			montarListaGradeCurricular();
			montarListaSelectItemUnidadeEnsino();
			verificarLayoutPadrao();
			montarDadosCampos();
			getExpedicaoDiplomaVO().setErro(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setPossuiErro(Boolean.FALSE);
			getFacadeFactory().getExpedicaoDiplomaFacade().validarViaExpedicaoDiplomaValida(getExpedicaoDiplomaVO());
			verificarSegundaVia(Boolean.FALSE);
			montarListaSelectItemTipoTextoPadrao();
			verificarConfiguracaoDiplomaExistente(Boolean.FALSE);
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getErro())) {
				setMensagemID(getExpedicaoDiplomaVO().getErro(), Uteis.ALERTA, Boolean.TRUE);
			} else {
				setMensagemID("msg_dados_editar");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Constantes.EMPTY;
		} finally {
			obj = null;
			return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
		}
	}
	
	public void gravar() {
		try {
			getExpedicaoDiplomaVO().setSituacaoApresentar(null);
			if (expedicaoDiplomaVO.isNovoObj().booleanValue() && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital())) {
				persistirLayoutPadrao(getExpedicaoDiplomaVO(), getTipoLayout());
			}
			getExpedicaoDiplomaVO().setLayoutDiploma(getTipoLayout());
			getFacadeFactory().getExpedicaoDiplomaFacade().validarDados(getExpedicaoDiplomaVO(),  true ,getUsuarioLogado());
//			alterarUnidadeEnsinoCertificadora(getExpedicaoDiplomaVO() , getPermissaoAlterarUnidadeEnsinoCertificadora());
			if (getConfiguracaoGeralPadraoSistema().getNaoPermitirExpedicaoDiplomaDocumentacaoPendente()) {
				List<DocumetacaoMatriculaVO> listaDocumentacao = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
				for (DocumetacaoMatriculaVO documetacaoMatriculaVO : listaDocumentacao) {
					if (!documetacaoMatriculaVO.getEntregue()) {
						throw new ConsistirException("Não é possível realizar a expedição do diploma pois o aluno possui documentação obrigatória pendente de entrega.");
					}
				}
			}
			if (!getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
				if (!getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarSeExisteColacaoGrauParaMatricula(expedicaoDiplomaVO.getMatricula().getMatricula())) {
					setExisteColacao(Boolean.TRUE);
					getColacaoGrauVO().setData(getExpedicaoDiplomaVO().getMatricula().getDataColacaoGrau());
					return;
				}
			}

			if (this.getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao() && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula().getDataConclusaoCurso())) {
				Date dataConclusao = getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorMatricula(this.getExpedicaoDiplomaVO().getMatricula().getMatricula());
				if (dataConclusao != null) {
					getExpedicaoDiplomaVO().getMatricula().setDataConclusaoCurso(dataConclusao);
				}
				Date dataInicio = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorMatricula(this.getExpedicaoDiplomaVO().getMatricula().getMatricula());
				if (dataInicio != null) {
					this.getExpedicaoDiplomaVO().getMatricula().setDataInicioCurso(dataInicio);
				}
			}
			limparDadosDecisaoJudicial();
			limparDadosPTA();
			limparDadosInformarCamposLivroRegistradora();
			if (expedicaoDiplomaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getExpedicaoDiplomaFacade().incluir(getExpedicaoDiplomaVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getExpedicaoDiplomaFacade().alterar(getExpedicaoDiplomaVO(), getUsuarioLogado(), true);
			}
			getExpedicaoDiplomaVO().setErro(Constantes.EMPTY);
			getFacadeFactory().getExpedicaoDiplomaFacade().consultarFuncionarioResponsavelExpedicao(getExpedicaoDiplomaVO(), getUsuarioLogado());
			realizarMontagemCargoFuncionario();
			setHabilitaBotaoImprimirDiploma(Boolean.TRUE);
			setMensagemID("msg_dados_gravados");
			getExpedicaoDiplomaVO().setPercentualCHIntegralizacaoMatricula(Uteis.getDoubleFormatado(getFacadeFactory().getHistoricoFacade().consultarPercentualCHIntegralizacaoPorMatriculaGradeCurricular(getExpedicaoDiplomaVO().getMatricula().getMatricula(), getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), getUsuarioLogado())));
			getExpedicaoDiplomaVO().setCargaHorariaTotal(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), getUsuarioLogado()));
			getExpedicaoDiplomaVO().setCargaHorariaCursada(getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(getExpedicaoDiplomaVO().getMatricula().getMatricula(), getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), true, getUsuarioLogado()));
			consultarUltimoDocumentoAssinadoDiplomaDigital();
//			return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
		} catch (Exception e) {
			setHabilitaBotaoImprimirDiploma(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
		}
	}
	
	private void limparDadosDecisaoJudicial() {
		if (!getExpedicaoDiplomaVO().getEmitidoPorDecisaoJudicial()) {
			getExpedicaoDiplomaVO().setNomeJuizDecisaoJudicial(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setNumeroProcessoDecisaoJudicial(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setDecisaoJudicial(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setInformacoesAdicionaisDecisaoJudicial(Constantes.EMPTY);
			getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().clear();
		}
	}
	
	private void limparDadosPTA() {
		if (!getExpedicaoDiplomaVO().getEmitidoPorProcessoTransferenciaAssistida()) {
			getExpedicaoDiplomaVO().setNomeIesPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setCnpjPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setCodigoMecPTA(0);
			getExpedicaoDiplomaVO().setCepPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setCidadePTA(null);
			getExpedicaoDiplomaVO().setLogradouroPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setBairroPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setNumeroPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setComplementoPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setTipoDescredenciamentoPTA(null);
			getExpedicaoDiplomaVO().setNumeroPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setDataDescredenciamentoPTA(null);
			getExpedicaoDiplomaVO().setVeiculoPublicacaoDescredenciamentoPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setNumeroDescredenciamentoPTA(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setDataPublicacaoDescredenciamentoPTA(null);
			getExpedicaoDiplomaVO().setSecaoPublicacaoDescredenciamentoPTA(0);
			getExpedicaoDiplomaVO().setPaginaPublicacaoDescredenciamentoPTA(0);
			getExpedicaoDiplomaVO().setNumeroDOUDescredenciamentoPTA(0);
		}
	}

	public void gravarColacao() {
		try {
			getExpedicaoDiplomaVO().getMatricula().setUsuario(getUsuarioLogadoClone());
			getFacadeFactory().getColacaoGrauFacade().inicializarDadosColacaoGrauParaExpedicaoDiploma(getColacaoGrauVO(), getExpedicaoDiplomaVO().getMatricula());
			getFacadeFactory().getColacaoGrauFacade().incluirComProgramacaoFormaturaAluno(getExpedicaoDiplomaVO().getMatricula(), getProgramacaoFormaturaAlunoVO(), getColacaoGrauVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setExisteColacao(Boolean.FALSE);
			gravar();
			setMensagemID("msg_ColacaoGrauAutomatica_gerada");
		} catch (Exception e) {
			setExisteColacao(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@Override
	public String consultar() {
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().consultarExpedicaoDiplomaGenericoOtimizado(getControleConsultaExpedicaoDiploma());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Constantes.EMPTY;
	}

	public String excluir() {
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().excluir(expedicaoDiplomaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaForm.xhtml");
		}
	}

	public String getAbrirModalNovaColacaoGrau() {
		if (getExisteColacao()) {
			getColacaoGrauVO().setData(getExpedicaoDiplomaVO().getMatricula().getDataColacaoGrau());
			return "RichFaces.$('panelColacaoGrau').show()";
		} else {
			return Constantes.EMPTY;
		}
	}

	@SuppressWarnings("rawtypes")
	public void consultarMatricula() {
		List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
		try {
			if (getValorConsultarMatricula().equals(Constantes.EMPTY)) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultarMatricula().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultarMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultarMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultarMatricula().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultarMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultarMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void selecionarMatricula() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		try {
			// getFacadeFactory().getMatriculaFacade().verificarAlunoPosGraduacaoFormadoExpedicaoDiploma(objCompleto);
			this.getExpedicaoDiplomaVO().setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			realizarMontagemNovaExpedicaoDiploma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			// obj = null;
			// objCompleto = null;
			setValorConsultarMatricula(null);
			setCampoConsultarMatricula(null);
			getListaConsultarMatricula().clear();
		}
	}

	public void montarNumeroProcesso() throws Exception {
		getFacadeFactory().getExpedicaoDiplomaFacade().montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(this.getExpedicaoDiplomaVO(),this.getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(),this.getExpedicaoDiplomaVO().getMatricula().getCurso().getCodigo(),   getUsuarioLogadoClone());
		
	}

	public void verificarSegundaVia(Boolean montarNumeroProcesso) {
		try {
			if (montarNumeroProcesso) {
				getExpedicaoDiplomaVO().setPossuiErro(Boolean.FALSE);
				getExpedicaoDiplomaVO().setErro(Constantes.EMPTY);
				montarNumeroProcesso();
			}
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getVia()) && !getExpedicaoDiplomaVO().getVia().equals("1")) {
				ExpedicaoDiplomaVO exp = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaPrimeiraVia(getExpedicaoDiplomaVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (exp != null) {
					if (getExpedicaoDiplomaVO().getNumeroRegistroDiplomaViaAnterior().equals(Constantes.EMPTY)) {
						getExpedicaoDiplomaVO().setNumeroRegistroDiplomaViaAnterior(getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterNumeroRegistroMatriculaPrimeiraVia(getExpedicaoDiplomaVO().getMatricula().getMatricula()));
					}
					if (getExpedicaoDiplomaVO().getNumeroProcessoViaAnterior().equals(Constantes.EMPTY)) {
						getExpedicaoDiplomaVO().setNumeroProcessoViaAnterior(exp.getNumeroProcesso());
					}
					getExpedicaoDiplomaVO().setDataRegistroDiplomaViaAnterior(exp.getDataExpedicao());
					if (!exp.getFuncionarioSecundarioVO().getPessoa().getNome().equals(Constantes.EMPTY)) {
						getExpedicaoDiplomaVO().setReitorRegistroDiplomaViaAnterior(exp.getFuncionarioSecundarioVO());
						
						if (exp.getFuncionarioSecundarioVO().getFuncionarioCargoVOs().isEmpty() && exp.getFuncionarioSecundarioVO().getCodigo().intValue() > 0) {
							exp.getFuncionarioSecundarioVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(exp.getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						}
						montarComboCargoReitor(exp.getFuncionarioSecundarioVO().getFuncionarioCargoVOs());
						if (exp.getCargoFuncionarioSecundarioVO().getCodigo().intValue() > 0) {
							exp.setCargoReitorRegistroDiplomaViaAnterior(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(exp.getCargoFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
						}
						getExpedicaoDiplomaVO().setCargoReitorRegistroDiplomaViaAnterior(exp.getCargoReitorRegistroDiplomaViaAnterior());
					}
					if (!exp.getFuncionarioPrimarioVO().getPessoa().getNome().equals(Constantes.EMPTY)) {
						getExpedicaoDiplomaVO().setSecretariaRegistroDiplomaViaAnterior(exp.getFuncionarioPrimarioVO());
					}
					getExpedicaoDiplomaVO().setSegundaVia(Boolean.TRUE);
				} else {
					getExpedicaoDiplomaVO().verificarSegundaVia();
				}
			} else {
				if (getExpedicaoDiplomaVO().getSegundaVia()) {
					getExpedicaoDiplomaVO().setSegundaVia(Boolean.FALSE);
				}
			}
		} catch (Exception e) {
			getExpedicaoDiplomaVO().verificarSegundaVia();
		}
	}

	public void montarListaGradeCurricular() throws Exception {
		List<GradeCurricularVO> lista = new ArrayList<GradeCurricularVO>(0);
		try {
			lista = getFacadeFactory().getGradeCurricularFacade().consultarGradeAtualGradeAntigaPorMatriculaAluno(getExpedicaoDiplomaVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(lista, "codigo", "nome"));
		} finally {
			lista = null;
		}
	}

	public void montarListaSelectItemTipoTextoPadrao() {
		try {
			if (getTipoLayout().equals("TextoPadrao")) {
				consultarListaSelectItemTipoTextoPadrao(0);
			} else {
				setTextoPadraoDeclaracao(null);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			getListaSelectItemTipoTextoPadrao().clear();
			List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("DI", unidadeEnsino, getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (TextoPadraoDeclaracaoVO objeto : lista) {
				getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getMostrarCampoCursoGradeCurricular() {
		if (getExpedicaoDiplomaVO().getMatricula().getMatricula().equals(Constantes.EMPTY)) {
			return false;
		} else {
			return true;
		}
	}

	public void limparCampoMatricula() {
		removerObjetoMemoria(this);
		setExpedicaoDiplomaVO(new ExpedicaoDiplomaVO());
		setHabilitaBotaoImprimirDiploma(Boolean.FALSE);
		limparMensagem();
		setMensagemID("msg_entre_dados");
	}

	public void limparDadosFuncionarioPrincipal(Boolean expedicaoDiplomaLote) throws Exception {
		if (expedicaoDiplomaLote) {
			setFuncionarioPrincipalVO(new FuncionarioVO());
			setTituloFuncionarioPrincipal(Constantes.EMPTY);
			setTituloFuncionarioPrincipal(Constantes.EMPTY);
		} else {
			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setCargoFuncionarioPrincipalVO(new CargoVO());
			getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(Constantes.EMPTY);
		}
		setListaConsultaFuncionario(new ArrayList<>(0));
		setListaCargoFuncionarioPrincipal(new ArrayList<>(0));
	}

	@SuppressWarnings("rawtypes")
	public void limparDadosReitor() throws Exception {
		getExpedicaoDiplomaVO().setReitorRegistroDiplomaViaAnterior(new FuncionarioVO());
		getExpedicaoDiplomaVO().setCargoReitorRegistroDiplomaViaAnterior(new CargoVO());
		setListaConsultaFuncionario(new ArrayList(0));
	}

	@SuppressWarnings("rawtypes")
	public void limparDadosSecGeral() throws Exception {
		getExpedicaoDiplomaVO().setSecretariaRegistroDiplomaViaAnterior(new FuncionarioVO());
		setListaConsultaFuncionario(new ArrayList(0));
	}

	public void limparDadosFuncionarioSecundario(Boolean expedicaoDiplomaLote) throws Exception {
		if (expedicaoDiplomaLote) {
			setFuncionarioSecundarioVO(new FuncionarioVO());
			setListaConsultaFuncionario(new ArrayList<>(0));
			setTituloFuncionarioSecundario(Constantes.EMPTY);
		} else {
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setCargoFuncionarioSecundarioVO(new CargoVO());
			getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
		}
		setListaCargoFuncionarioSecundario(new ArrayList<>(0));
	}

	public void limparDadosFuncionarioTerceiro(Boolean expedicaoDiplomaLote) throws Exception {
		if (expedicaoDiplomaLote) {
			setFuncionarioTerceiroVO(new FuncionarioVO());
			setListaConsultaFuncionario(new ArrayList<>(0));
			setTituloFuncionarioTerceiro(Constantes.EMPTY);
		} else {
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setCargoFuncionarioTerceiroVO(new CargoVO());
			getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(Constantes.EMPTY);
		}
		setListaCargoFuncionarioTerceiro(new ArrayList<>(0));
	}
	
	public void imprimirPDF() throws Exception {
		try {
			setOncompleteModal(Constantes.EMPTY);
			verificarDadosCargosFuncionariosMontados(Boolean.FALSE);
			getExpedicaoDiplomaVO().setLayoutDiploma(getTipoLayout());
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarImpressaoExpedicaoDiploma(getExpedicaoDiplomaVO(), isAssinarDigitalmente(), getExpedicaoDiplomaVO().getGerarXMLDiploma(), Boolean.FALSE, getSuperParametroRelVO(), getSuperControleRelatorio(), getTipoLayout(), getListaMensagensErro(), getCaminhoPastaWeb(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			inicializarDadosAssinaturaExpedicaoDiploma(getExpedicaoDiplomaVO());
			getExpedicaoDiplomaVO().setDiplomaDigital(new DocumentoAssinadoVO());
			montarDadosUltimoDiplomaAluno();
			setImprimirContrato(getExpedicaoDiplomaVO().getImprimirContrato());
			setAbrirModalVerso(getExpedicaoDiplomaVO().getAbrirModalVerso());
			setAbrirModalOK(getExpedicaoDiplomaVO().getAbrirModalOK());
			setOncompleteModal("RichFaces.$('panelAvisoAssinaturaDigital').hide(); RichFaces.$('panelDocumentoAssinado').hide();");
			setModalErro(Constantes.EMPTY);
			if (getExpedicaoDiplomaVO().getNovaGeracaoRepresentacaoVisualDiplomaDigital()) {
				setMensagemID("Nova representação visual gerada com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
			} else {
				setMensagemID(isAssinarDigitalmente() && getExpedicaoDiplomaVO().getGerarXMLDiploma() ? "Documento digital gerado com sucesso." : "Diploma gerado com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
			}
		} catch (ConsistirException ce) {
			setImprimirContrato(getExpedicaoDiplomaVO().getImprimirContrato());
			setAbrirModalOK(getExpedicaoDiplomaVO().getAbrirModalOK());
			if (getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
				if (!ce.getListaMensagemErro().isEmpty() && getListaMensagensErro().isEmpty()) {
					getListaMensagensErro().addAll(ce.getListaMensagemErro());
					setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
					setModalErro("RichFaces.$('panelAvisoAssinaturaDigital').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
				} else if (!getListaMensagensErro().isEmpty()) { 
					setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
					setModalErro("RichFaces.$('panelAvisoAssinaturaDigital').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
				} else {
					getListaMensagensErro().add(ce.getMessage());
				}
			} else {
				if (!getLancarExcecao()) {
					setMensagemDetalhada("msg_erro", ce.getMessage());
				}
			}
			if (getLancarExcecao()) {
				throw ce;
			}
		} catch (Exception e) {
			setImprimirContrato(getExpedicaoDiplomaVO().getImprimirContrato());
			setAbrirModalOK(getExpedicaoDiplomaVO().getAbrirModalOK());
			setModalErro(Constantes.EMPTY);
			setFazerDownload(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
			if (getLancarExcecao()) {
				throw e;
			}
		}
	}

	private void adicionarParametrosAssinaturaDigital(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(expedicaoDiplomaVO.getMatricula().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
		if (isAssinarDigitalmente()) {
			
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioPrimario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioPrimario", false);
			}
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() &&Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioSecundario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioSecundario", false);
			}
			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario() && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getCodigo())) {
				getSuperParametroRelVO().adicionarParametro("assinaturaDigitalFuncionarioTerciario", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getPastaBaseArquivo() + File.separator + getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getArquivoAssinaturaVO().getNome());
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarAssinaturaDigitalizadoFuncionario());
			} else {
				getSuperParametroRelVO().adicionarParametro("apresentarAssinaturaDigitalFuncionarioTerciario", false);
			}
//			if (Uteis.isAtributoPreenchido(configGEDVO.getCodigo())) {
//				if (configGEDVO.getConfiguracaoGedDiplomaVO().getApresentarSelo()) {
//					getSuperParametroRelVO().adicionarParametro("seloAssinaturaDigital", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + configGEDVO.getSeloAssinaturaEletronicaVO().getPastaBaseArquivoEnum().getValue() + File.separator + configGEDVO.getSeloAssinaturaEletronicaVO().getNome());
//					getSuperParametroRelVO().adicionarParametro("apresentarSeloAssinaturaDigital", true);
//				}
//			}
			getSuperParametroRelVO().adicionarParametro("apresentarSeloAssinaturaDigital", false);
			
		} 
	}

	public void imprimirPDFVersoLayout3Pos() {
		getSuperParametroRelVO().setTituloRelatorio("Verso Diploma do Aluno");
		getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
		List<DiplomaAlunoRelVO> listaObjetos = new ArrayList<DiplomaAlunoRelVO>(0);
		try {

			// executarMetodoControle("HistoricoAlunoRelControle",
			// "receberDadosDiplomaVerso",
			// getExpedicaoDiplomaVO().getMatricula(),
			// getExpedicaoDiplomaVO().getGradeCurricularVO(),
			// getFuncionarioPrincipalVO(), getFuncionarioSecundarioVO(),
			// getCargoFuncionarioPrincipal(), getCargoFuncionarioSecundario());

			listaObjetos = getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(getExpedicaoDiplomaVO().getUtilizarUnidadeMatriz(), getExpedicaoDiplomaVO(), getExpedicaoDiplomaVO().getFuncionarioPrimarioVO(), getExpedicaoDiplomaVO().getFuncionarioSecundarioVO(), getExpedicaoDiplomaVO().getFuncionarioTerceiroVO(), getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO(), getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO(), getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO(), getExpedicaoDiplomaVO().getTituloFuncionarioPrincipal(), getExpedicaoDiplomaVO().getTituloFuncionarioSecundario(), getUsuarioLogado(), getTipoLayout(), getCargoFuncionarioQuarto(), getCargoFuncionarioQuinto(), getFuncionarioQuartoVO(), getFuncionarioQuintoVO(), Boolean.TRUE);
			// getFacadeFactory().getDiplomaAlunoRelFacade().montaNivelEducacional();
			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO());

			String design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio("Verso" + getTipoLayout(), getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional());

			getSuperParametroRelVO().setNomeDesignIreport(design);
			// getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			Integer codigoUnidadeEnsino = 0;
			if (!getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				codigoUnidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			} else {
				codigoUnidadeEnsino = getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo();
			}
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
			realizarImpressaoRelatorio();
			setAbrirModalVerso(Constantes.EMPTY);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimirPDFVersoLayout3Superior() {
		getSuperParametroRelVO().setTituloRelatorio("Diploma do Aluno");
		getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
		// String titulo = Constantes.EMPTY;
		// String nomeEmpresa = Constantes.EMPTY;
		List<DiplomaAlunoRelVO> listaObjetos = new ArrayList<DiplomaAlunoRelVO>(0);
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().validarImprimirDiploma(getExpedicaoDiplomaVO(), getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getCodigo(), getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getCodigo(), getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getCodigo(), getTipoLayout(), getUsuarioLogado(), getExpedicaoDiplomaVO().getGerarXMLDiploma());
			listaObjetos = getFacadeFactory().getDiplomaAlunoRelFacade().criarObjeto(getExpedicaoDiplomaVO().getUtilizarUnidadeMatriz(), getExpedicaoDiplomaVO(), getExpedicaoDiplomaVO().getFuncionarioPrimarioVO(), getExpedicaoDiplomaVO().getFuncionarioSecundarioVO(), getExpedicaoDiplomaVO().getFuncionarioTerceiroVO(), getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO(), getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO(), getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO(), getExpedicaoDiplomaVO().getTituloFuncionarioPrincipal(), getExpedicaoDiplomaVO().getTituloFuncionarioSecundario(), getUsuarioLogado(), getTipoLayout(), getExpedicaoDiplomaVO().getCargoFuncionarioQuartoVO(), getExpedicaoDiplomaVO().getCargoFuncionarioQuintoVO(), getExpedicaoDiplomaVO().getFuncionarioQuartoVO(), getExpedicaoDiplomaVO().getFuncionarioQuintoVO(), Boolean.TRUE);
			// getFacadeFactory().getDiplomaAlunoRelFacade().montaNivelEducacional();

			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO());
			getFacadeFactory().getExpedicaoDiplomaFacade().alterarFuncionarioResponsavel(getExpedicaoDiplomaVO(), getUsuarioLogado());

			String design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(getTipoLayout() + "Verso", getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional());
			if (listaObjetos.get(0).getObservacaoComplementarDiploma0().getCodigo().intValue() > 0) {
				design = getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(getTipoLayout() + "Verso2", getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional());
			}

			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().getParametros().put("simboloBandeiraPretoBranco", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
			getSuperParametroRelVO().getParametros().put("bordaDiploma", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "bordaDiplom.png");
			Integer codigoUnidadeEnsino = 0;
			if (!getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				codigoUnidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			} else {
				codigoUnidadeEnsino = getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo();
			}
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
			// apresentarRelatorioObjetos(DiplomaAlunoRel.getIdEntidade(getTipoLayout()),
			// titulo, nomeEmpresa, Constantes.EMPTY, "PDF", Constantes.EMPTY, design, Constantes.EMPTY, Constantes.EMPTY, listaObjetos,
			// getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			realizarImpressaoRelatorio();
			setAbrirModalVerso(Constantes.EMPTY);
			setMensagemID("msg_relatorio_ok");
			// if (getIsHistoricoPosGraduacao()) {
			// removerObjetoMemoria(this);
			// }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirDiplomaAlunoRel() {
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().validarImprimirDiploma(getExpedicaoDiplomaVO(), getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getCodigo(), getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getCodigo(), getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getCodigo(), getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getCodigo(), getTipoLayout(), getUsuarioLogado(), getExpedicaoDiplomaVO().getGerarXMLDiploma());
			setImprimirDiploma(Boolean.TRUE);
			setMensagemDetalhada(Constantes.EMPTY);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirHistoricoPos() {
		try {
			String layout = Constantes.EMPTY;
			if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
				if (getTipoLayout().equals("DiplomaAlunoPos4Rel")) {
				}
				layout = ("HistoricoAlunoPosRel");
			} else if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional().equals("ME")) {
				layout = ("HistoricoAlunoEnsinoMedio");
			} else if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional().equals("BA")) {
				layout = ("HistoricoAlunoEnsinoMedio");
			} else {
				layout = ("HistoricoAlunoRel");
			}

			HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();

			histAlunoRelVO.setFuncionarioPrincipalVO(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
			histAlunoRelVO.setFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
			histAlunoRelVO.setCargoFuncionarioPrincipal(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO());
			histAlunoRelVO.setCargoFuncionarioSecundario(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO());

			if (getExpedicaoDiplomaVO().getMatricula().getTituloMonografia() != null) {
				histAlunoRelVO.setTituloMonografia(getExpedicaoDiplomaVO().getMatricula().getTituloMonografia().replaceAll("(\n|\r)+", " "));
			} else {
				histAlunoRelVO.setTituloMonografia(getExpedicaoDiplomaVO().getMatricula().getTituloMonografia());
			}

			if (getExpedicaoDiplomaVO().getMatricula().getNotaMonografia() != null) {
				histAlunoRelVO.setNotaMonografia(getExpedicaoDiplomaVO().getMatricula().getNotaMonografia().toString());
			} else {
				histAlunoRelVO.setNotaMonografia(Constantes.EMPTY);
			}

			getFacadeFactory().getMatriculaFacade().alterarObservacaoComplementar(getExpedicaoDiplomaVO().getMatricula(), getExpedicaoDiplomaVO().getGradeCurricularVO().getCodigo(), Constantes.EMPTY, getUsuarioLogadoClone());
			getFacadeFactory().getMatriculaFacade().carregarDados(getExpedicaoDiplomaVO().getMatricula(), NivelMontarDados.TODOS, getUsuarioLogado());
			getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros(Constantes.EMPTY);
			String design = HistoricoAlunoRel.getDesignIReportRelatorio(TipoNivelEducacional.getEnum(getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional()), layout, false);
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
			histAlunoRelVO = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, getExpedicaoDiplomaVO().getMatricula(), getExpedicaoDiplomaVO().getGradeCurricularVO(), getFiltroRelatorioAcademicoVO(), 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, "HistoricoAlunoPosPaisagemRel", false, getExpedicaoDiplomaVO().getDataExpedicao(), false, false, false, getUsuarioLogado(), false, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", null, null);
			histAlunoRelVO.setApresentarFrequencia(Boolean.FALSE);
			for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
				histAlunoDiscRelVO.setApresentarFrequencia(Boolean.FALSE);
			}
			List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
			historicoAlunoRelVOs.add(histAlunoRelVO);

			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(historicoAlunoRelVOs);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
			
			realizarImpressaoRelatorio();

			// executarMetodoControle("HistoricoAlunoRelControle",
			// "receberDadosDiploma", getExpedicaoDiplomaVO().getMatricula(),
			// getExpedicaoDiplomaVO().getGradeCurricularVO(),
			// getFuncionarioPrincipalVO(), getFuncionarioSecundarioVO(),
			// getFuncionarioTerceiroVO(), getCargoFuncionarioPrincipal(),
			// getCargoFuncionarioSecundario(), getCargoFuncionarioTerceiro());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirVersoPDF() {
		try {
			if (getTipoLayout().equals("DiplomaAlunoMedio2Rel")) {
				imprimirPDFVersoEnsinoMedio();
			} else if (!getTipoLayout().equals("DiplomaAlunoPos3Rel") && !getTipoLayout().equals("DiplomaAlunoSuperior3Rel") && !getTipoLayout().equals("DiplomaAlunoSuperior4Rel") && !getTipoLayout().equals("DiplomaAlunoSuperior5Rel") && !getTipoLayout().equals("DiplomaAlunoSuperior6Rel")) {

				HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();

				histAlunoRelVO.setFuncionarioPrincipalVO(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
				histAlunoRelVO.setFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
				histAlunoRelVO.setCargoFuncionarioPrincipal(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO());
				histAlunoRelVO.setCargoFuncionarioSecundario(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO());

				if (getExpedicaoDiplomaVO().getMatricula().getTituloMonografia() != null) {
					histAlunoRelVO.setTituloMonografia(getExpedicaoDiplomaVO().getMatricula().getTituloMonografia().replaceAll("(\n|\r)+", " "));
				} else {
					histAlunoRelVO.setTituloMonografia(getExpedicaoDiplomaVO().getMatricula().getTituloMonografia());
				}
				if (getExpedicaoDiplomaVO().getMatricula().getNotaMonografia() != null) {
					histAlunoRelVO.setNotaMonografia(getExpedicaoDiplomaVO().getMatricula().getNotaMonografia().toString());
				}
				// setHistoricoAlunoRelVO(new HistoricoAlunoRelVO());
				getFacadeFactory().getMatriculaFacade().alterarObservacaoComplementar(getExpedicaoDiplomaVO().getMatricula(), getExpedicaoDiplomaVO().getGradeCurricularVO().getCodigo(), Constantes.EMPTY, getUsuarioLogadoClone());
				getFacadeFactory().getMatriculaFacade().carregarDados(getExpedicaoDiplomaVO().getMatricula(), NivelMontarDados.TODOS, getUsuarioLogado());
				// setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(),
				// getUnidadeEnsinoLogado().getCodigo(),
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS));
				// HistoricoAlunoRel.validarDados(matricula);
				getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros(Constantes.EMPTY);
				String design = Constantes.EMPTY;
				if (getTipoLayout().equals("DiplomaAlunoSuperior5Rel") || getTipoLayout().equals("DiplomaAlunoSuperior6Rel")) {
					design = HistoricoAlunoRel.getDesignIReportRelatorio(TipoNivelEducacional.getEnum(getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional()), "DiplomaAlunoSuperior5RelVerso", false);
				} else {
					design = HistoricoAlunoRel.getDesignIReportRelatorio(TipoNivelEducacional.getEnum(getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional()), "HistoricoAlunoVersoDiplomaRel2", false);
				}
				getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
				histAlunoRelVO = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, getExpedicaoDiplomaVO().getMatricula(), getExpedicaoDiplomaVO().getGradeCurricularVO(), getFiltroRelatorioAcademicoVO(), 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, "HistoricoAlunoPosPaisagemRel", false, getExpedicaoDiplomaVO().getDataExpedicao(), false, false, false, getUsuarioLogado(), false, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", null, null);
				histAlunoRelVO.setApresentarFrequencia(Boolean.FALSE);
				for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : histAlunoRelVO.getListaHistoricoAlunoDisciplinaRelVOs()) {
					histAlunoDiscRelVO.setApresentarFrequencia(Boolean.FALSE);
				}
				List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
				historicoAlunoRelVOs.add(histAlunoRelVO);

				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(historicoAlunoRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
					getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
				realizarImpressaoRelatorio();

			} else if (getTipoLayout().equals("DiplomaAlunoSuperior4Rel")) {
				HistoricoAlunoRelVO historicoTemp = null;
				getSuperParametroRelVO().setTituloRelatorio("HISTÓRICO ESCOLAR");
				getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
				String design = Constantes.EMPTY;
				List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
				registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoAlunoRelControle", "Inicializando Geração de Relatório Histórico Aluno", "Emitindo Relatório");
				setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnicaParaHistoricoAluno(getExpedicaoDiplomaVO().getMatricula().getMatricula(), getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
				if (getListaMatriculas().isEmpty()) {
					throw new Exception(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
				}
				getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
				for (MatriculaVO matricula : getListaMatriculas()) {

					HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
					histAlunoRelVO.setFuncionarioPrincipalVO(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
					histAlunoRelVO.setFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
					histAlunoRelVO.setCargoFuncionarioPrincipal(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO());
					histAlunoRelVO.setCargoFuncionarioSecundario(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO());
					getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, getUsuarioLogado());
					getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros(Constantes.EMPTY);
					matricula.setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado()));
					
					historicoTemp = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, matricula, matricula.getUltimoMatriculaPeriodoVO().getGradeCurricular(), getFiltroRelatorioAcademicoVO(), 1, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, "HistoricoAlunoLayout5Rel", false, getExpedicaoDiplomaVO().getDataExpedicao(), false, false, false, getUsuarioLogado(), false, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", null, null);
					historicoTemp.setApresentarFrequencia(Boolean.TRUE);
					for (HistoricoAlunoDisciplinaRelVO histAlunoDiscRelVO : historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs()) {
						histAlunoDiscRelVO.setApresentarFrequencia(Boolean.TRUE);
					}
					historicoAlunoRelVOs.add(historicoTemp);
				}
				design = "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HistoricoAlunoLayout5Rel.jrxml";
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setSubReport_Dir(HistoricoAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(historicoAlunoRelVOs);
				getSuperParametroRelVO().setQuantidade(historicoAlunoRelVOs.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
					getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
				realizarImpressaoRelatorio();

			} else if (getTipoLayout().equals("DiplomaAlunoSuperior3Rel") || getTipoLayout().equals("DiplomaAlunoSuperior5Rel") || getTipoLayout().equals("DiplomaAlunoSuperior6Rel")) {
				imprimirPDFVersoLayout3Superior();
			} else {
				imprimirPDFVersoLayout3Pos();
			}
//			limparDadosMemoria();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimirPDFVersoEnsinoMedio() {
		List<HistoricoAlunoRelVO> listaObjetos = new ArrayList<HistoricoAlunoRelVO>(0);
		HistoricoAlunoRelVO historicoAlunoRelVO = new HistoricoAlunoRelVO();
		try {
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
			listaObjetos.add(getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(historicoAlunoRelVO, getExpedicaoDiplomaVO().getMatricula(), expedicaoDiplomaVO.getGradeCurricularVO(), getFiltroRelatorioAcademicoVO(), 2, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, "DiplomaAlunoMedio2RelVerso", false, null, false, false, false, getUsuarioLogado(), false, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", null, null));
			getSuperParametroRelVO().setTituloRelatorio("Histórico Escolar");
			getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getDiplomaAlunoRelFacade().getDesignIReportRelatorio(getTipoLayout() + "Verso", getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional()));
			getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDiplomaAlunoRelFacade().getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().getParametros().put("simboloBandeiraPretoBranco", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "simboloBandeiraPretoBranco.jpg");
			getSuperParametroRelVO().getParametros().put("bordaDiploma", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "bordaDiplom.png");
			Integer codigoUnidadeEnsino = 0;
			if (!getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				codigoUnidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			} else {
				codigoUnidadeEnsino = getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo();
			}
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (unidadeEnsino.getNomeExpedicaoDiploma().trim().isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNome());
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino.getNomeExpedicaoDiploma());
			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			adicionarParametrosAssinaturaDigital(getExpedicaoDiplomaVO());	
			realizarImpressaoRelatorio();
			setAbrirModalVerso(Constantes.EMPTY);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			listaObjetos = null;
		}
	}

	public void limparDadosMemoria() {
		removerObjetoMemoria(this);
	}

	private void persistirLayoutPadrao(ExpedicaoDiplomaVO expedicaoDiploma, String valor) throws Exception {
		if (expedicaoDiploma.getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "diploma", "designDiplomaPos", getUsuarioLogado());
		} else {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "diploma", "designDiplomaGraduacao",
					expedicaoDiploma.getFuncionarioPrimarioVO().getCodigo(),
					expedicaoDiploma.getFuncionarioSecundarioVO().getCodigo(),
					expedicaoDiploma.getFuncionarioTerceiroVO().getCodigo(),
					expedicaoDiploma.getFuncionarioQuartoVO().getCodigo(),
					expedicaoDiploma.getFuncionarioQuintoVO().getCodigo(),
					false,
					expedicaoDiploma.getTituloFuncionarioPrincipal(),
					expedicaoDiploma.getTituloFuncionarioSecundario(),
					expedicaoDiploma.getTituloFuncionarioTerceiro(),
					expedicaoDiploma.getTituloFuncionarioQuarto(),
					expedicaoDiploma.getTituloFuncionarioQuinto(),
					Constantes.EMPTY,
					Constantes.EMPTY, getUsuarioLogado(), Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(expedicaoDiploma.getTextoPadrao())) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(expedicaoDiploma.getTextoPadrao().getCodigo().toString(), "diploma", "textoPadrao", getUsuarioLogado());
			}
			
			
//			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "diploma", "designDiplomaGraduacao", getUsuarioLogado());
		}
	}
	
	/**
	 * Método que ao selecionar uma pessoa para geração do histórico, verifica se já existe uma preferência de layout para determinado relatório.
	 * 
	 * @throws Exception
	 */
	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("diploma", "designDiplomaPos", false, getUsuarioLogado());
		} else {
			layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("diploma", "designDiplomaGraduacao", false, getUsuarioLogado());
		}
		if (!layoutPadraoVO.getValor().equals(Constantes.EMPTY)) {
			setTipoLayout(layoutPadraoVO.getValor());
			if (getTipoLayout().equals("TextoPadrao")) {
				montarListaSelectItemTipoTextoPadrao();
				LayoutPadraoVO layoutPadraoVO2 = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("diploma", "textoPadrao", false, getUsuarioLogado());
				if (layoutPadraoVO2 != null && Uteis.isAtributoPreenchido(layoutPadraoVO2.getValor())) {
					setTextoPadraoDeclaracao(Integer.parseInt(layoutPadraoVO2.getValor()));
				}
			}
			if (layoutPadraoVO.getAssinaturaFunc1() != 0) {
				getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().setCodigo(layoutPadraoVO.getAssinaturaFunc1());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getTituloAssinaturaFunc1())) {
					getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(layoutPadraoVO.getTituloAssinaturaFunc1());
				}
				getFacadeFactory().getFuncionarioFacade().carregarDados(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				consultarFuncionarioPrincipal();
			}
			if (layoutPadraoVO.getAssinaturaFunc2() != 0) {
				getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().setCodigo(layoutPadraoVO.getAssinaturaFunc2());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getTituloAssinaturaFunc2())) {
					getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(layoutPadraoVO.getTituloAssinaturaFunc2());
				}
				getFacadeFactory().getFuncionarioFacade().carregarDados(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				consultarFuncionarioSecundario();
			}
			if (layoutPadraoVO.getAssinaturaFunc3() != 0) {
				getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().setCodigo(layoutPadraoVO.getAssinaturaFunc3());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getTituloAssinaturaFunc3())) {
					getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(layoutPadraoVO.getTituloAssinaturaFunc3());
				}
				getFacadeFactory().getFuncionarioFacade().carregarDados(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				consultarFuncionarioTerciario();
			}
			if (layoutPadraoVO.getAssinaturaFunc4() != 0) {
				getExpedicaoDiplomaVO().getFuncionarioQuartoVO().setCodigo(layoutPadraoVO.getAssinaturaFunc4());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getTituloAssinaturaFunc4())) {
					getExpedicaoDiplomaVO().setTituloFuncionarioQuarto(layoutPadraoVO.getTituloAssinaturaFunc4());
				}
				getFacadeFactory().getFuncionarioFacade().carregarDados(getExpedicaoDiplomaVO().getFuncionarioQuartoVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				consultarFuncionarioQuarto();
			}
			if (layoutPadraoVO.getAssinaturaFunc5() != 0) {
				getExpedicaoDiplomaVO().getFuncionarioQuintoVO().setCodigo(layoutPadraoVO.getAssinaturaFunc5());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getTituloAssinaturaFunc5())) {
					getExpedicaoDiplomaVO().setTituloFuncionarioQuinto(layoutPadraoVO.getTituloAssinaturaFunc5());
				}
				getFacadeFactory().getFuncionarioFacade().carregarDados(getExpedicaoDiplomaVO().getFuncionarioQuintoVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				consultarFuncionarioQuinto();
			}
		}
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getTextoPadrao().getCodigo()) && getExpedicaoDiplomaVO().getLayoutDiploma().equals("TextoPadrao")) {
			setTipoLayout("TextoPadrao");
			montarListaSelectItemTipoTextoPadrao();
			setTextoPadraoDeclaracao(getExpedicaoDiplomaVO().getTextoPadrao().getCodigo());
		} else if (!getExpedicaoDiplomaVO().getLayoutDiploma().equals("TextoPadrao")) {
			setTipoLayout(getExpedicaoDiplomaVO().getLayoutDiploma());
		}
	}	

	public void realizarNavegacaoTelaHistoricoAluno() {
		try {
			context().getExternalContext().getSessionMap().put("matriculaExpedicaoDiploma", getExpedicaoDiplomaVO().getMatricula().getMatricula());
			context().getExternalContext().getSessionMap().put("funcionarioPrincipal", getExpedicaoDiplomaVO().getFuncionarioPrimarioVO());
			context().getExternalContext().getSessionMap().put("cargoFuncionarioPrincipal", getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO());
			if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica() && getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
				context().getExternalContext().getSessionMap().put("funcionarioSecundario", getExpedicaoDiplomaVO().getFuncionarioTerceiroVO());
				context().getExternalContext().getSessionMap().put("cargoFuncionarioSecundario", getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO());
				context().getExternalContext().getSessionMap().put("gerarXmlDiploma", Boolean.TRUE);
			} else {
				context().getExternalContext().getSessionMap().put("funcionarioSecundario", getExpedicaoDiplomaVO().getFuncionarioSecundarioVO());
				context().getExternalContext().getSessionMap().put("cargoFuncionarioSecundario", getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO());
				context().getExternalContext().getSessionMap().put("gerarXmlDiploma", Boolean.FALSE);
			}
			removerControleMemoriaFlash("HistoricoAlunoRelControle");
			removerControleMemoriaTela("HistoricoAlunoRelControle");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("incomplete-switch")
	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<>(0);
		TipoNivelEducacional tipoNivelEducacional = TipoNivelEducacional.getEnum(getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional());
		if (tipoNivelEducacional == null) {
			return itens;
		}
		switch (tipoNivelEducacional) {
		case BASICO:
		case MEDIO:
			itens.add(new SelectItem("DiplomaAlunoMedioRel", "Layout 1 - Ensino Médio"));
			itens.add(new SelectItem("DiplomaAlunoMedio2Rel", "Layout 2 - Ensino Médio"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case SEQUENCIAL:
		case SUPERIOR:
			itens.add(new SelectItem("DiplomaAlunoSuperiorRel", "Layout 1 - Graduação"));
			itens.add(new SelectItem("DiplomaAlunoSuperior2Rel", "Layout 2 - Graduação"));
			itens.add(new SelectItem("DiplomaAlunoSuperior3Rel", "Layout 3 - Graduação"));
			itens.add(new SelectItem("DiplomaAlunoSuperior4Rel", "Layout 4 - Graduação"));
			itens.add(new SelectItem("DiplomaAlunoSuperior5Rel", "Layout 5 - Graduação"));
			itens.add(new SelectItem("DiplomaAlunoSuperior6Rel", "Layout 6 - Graduação"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case POS_GRADUACAO:
			itens.add(new SelectItem("DiplomaAlunoPosRel", "Layout 1 - Pós-Graduação"));
			itens.add(new SelectItem("DiplomaAlunoPos2Rel", "Layout 2 - Pós-Graduação"));
			itens.add(new SelectItem("DiplomaAlunoPos3Rel", "Layout 3 - Pós-Graduação"));
			itens.add(new SelectItem("DiplomaAlunoPos4Rel", "Layout 4 - Pós-Graduação"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case MESTRADO:
			itens.add(new SelectItem("DiplomaAlunoPosRel", "Layout 1 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoPos2Rel", "Layout 2 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoPos3Rel", "Layout 3 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoPos4Rel", "Layout 4 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperiorRel", "Layout 5 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperior2Rel", "Layout 6 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperior3Rel", "Layout 7 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperior4Rel", "Layout 8 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperior5Rel", "Layout 9 - Mestrado"));
			itens.add(new SelectItem("DiplomaAlunoSuperior6Rel", "Layout 10 - Mestrado"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case GRADUACAO_TECNOLOGICA:
			itens.add(new SelectItem("DiplomaAlunoGradTecnologicaRel", "Layout 1 - Graduação Tecnológica"));
			itens.add(new SelectItem("DiplomaAlunoSuperior5Rel", "Layout 2 - Graduação Tecnológica"));
			itens.add(new SelectItem("DiplomaAlunoSuperior6Rel", "Layout 3 - Graduação Tecnológica"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case PROFISSIONALIZANTE:
			itens.add(new SelectItem("DiplomaAlunoGradTecnologicaRel", "Layout 1 - Técnico/Profissionalizante"));
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
			break;
		case EXTENSAO:
			itens.add(new SelectItem("TextoPadrao", "Texto Padrão"));
		}
		return itens;
	}

	@SuppressWarnings("rawtypes")
	public void consultarFuncionario() {
		List objs = new ArrayList(0);
		try {
			if (getValorConsultaFuncionario().equals(Constantes.EMPTY)) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			objs = null;
		}
	}

	public void consultarFuncionarioPrincipalPorMatricula(Boolean expedicaoDiplomaLote) throws Exception {
		try {
			setFuncionarioPrincipal(Boolean.TRUE);
			setFuncionarioSecundario(Boolean.FALSE);
			setFuncionarioTerceiro(Boolean.FALSE);
			setFuncionarioQuarto(Boolean.FALSE);
			setFuncionarioQuinto(Boolean.FALSE);
			if (expedicaoDiplomaLote) {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioPrincipalVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				this.setFuncionarioPrincipalVO(objFuncionario);
				setTituloFuncionarioPrincipal(Constantes.EMPTY);
				setCargoFuncionarioPrincipal(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(objFuncionario);
				getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setCargoFuncionarioPrincipalVO(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFuncionarioPrincipalVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(new FuncionarioVO());
			getListaCargoFuncionarioPrincipal().clear();
		}
	}

	public void consultarFuncionarioSecundarioPorMatricula(Boolean expedicaoDiplomaLote) throws Exception {
		try {
			setFuncionarioPrincipal(Boolean.FALSE);
			setFuncionarioSecundario(Boolean.TRUE);
			setFuncionarioTerceiro(Boolean.FALSE);
			setFuncionarioQuarto(Boolean.FALSE);
			setFuncionarioQuinto(Boolean.FALSE);
			if (expedicaoDiplomaLote) {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioSecundarioVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				this.setFuncionarioSecundarioVO(objFuncionario);
				setTituloFuncionarioSecundario(Constantes.EMPTY);
				setCargoFuncionarioSecundario(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(objFuncionario);
				getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setCargoFuncionarioSecundarioVO(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFuncionarioSecundarioVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(new FuncionarioVO());
			getListaCargoFuncionarioSecundario().clear();
		}
	}

	public void consultarFuncionarioTerceiroPorMatricula(Boolean expedicaoDiplomaLote) throws Exception {
		try {
			setFuncionarioPrincipal(Boolean.FALSE);
			setFuncionarioSecundario(Boolean.FALSE);
			setFuncionarioTerceiro(Boolean.TRUE);
			setFuncionarioQuarto(Boolean.FALSE);
			setFuncionarioQuinto(Boolean.FALSE);
			if (expedicaoDiplomaLote) {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioTerceiroVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				this.setFuncionarioTerceiroVO(objFuncionario);
				setTituloFuncionarioTerceiro(Constantes.EMPTY);
				setCargoFuncionarioTerceiro(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(objFuncionario);
				getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setCargoFuncionarioTerceiroVO(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFuncionarioTerceiroVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(new FuncionarioVO());
			getListaCargoFuncionarioTerceiro().clear();
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		try {
			if (isFuncionarioPrincipal()) {
				setFuncionarioPrincipalVO(objCompleto);
				getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(objCompleto);
				montarComboCargoFuncionario(objCompleto.getFuncionarioCargoVOs());
				setTituloFuncionarioPrincipal(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(Constantes.EMPTY);
			}  else if (getFuncionarioSecundario()) {
				setFuncionarioSecundarioVO(objCompleto);
				getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(objCompleto);
				montarComboCargoFuncionario(objCompleto.getFuncionarioCargoVOs());
				setTituloFuncionarioSecundario(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(Constantes.EMPTY);
			} else if (getFuncionarioTerceiro()) {
				setFuncionarioTerceiroVO(objCompleto);
				getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(objCompleto);
				montarComboCargoFuncionario(objCompleto.getFuncionarioCargoVOs());
				setTituloFuncionarioTerceiro(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(Constantes.EMPTY);
			} else if (getFuncionarioQuarto()) {
				setFuncionarioQuartoVO(objCompleto);
				getExpedicaoDiplomaVO().setFuncionarioQuartoVO(objCompleto);
				montarComboCargoFuncionario(objCompleto.getFuncionarioCargoVOs());
				setTituloFuncionarioQuarto(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setTituloFuncionarioQuarto(Constantes.EMPTY);
			} else if (getFuncionarioQuinto()) {
				setFuncionarioQuintoVO(objCompleto);
				getExpedicaoDiplomaVO().setFuncionarioQuintoVO(objCompleto);
				montarComboCargoFuncionario(objCompleto.getFuncionarioCargoVOs());
				setTituloFuncionarioQuinto(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setTituloFuncionarioQuinto(Constantes.EMPTY);
			}
		} finally {
			obj = null;
			objCompleto = null;
			setCampoConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
			setListaConsultaFuncionario(null);
		}
	}

	public void selecionarReitor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		try {
			getExpedicaoDiplomaVO().setReitorRegistroDiplomaViaAnterior(objCompleto);
			montarComboCargoReitor(objCompleto.getFuncionarioCargoVOs());
		} finally {
			obj = null;
			objCompleto = null;
			setCampoConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
			setListaConsultaFuncionario(null);
		}
	}

	public void selecionarSecGeral() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		try {
			getExpedicaoDiplomaVO().setSecretariaRegistroDiplomaViaAnterior(objCompleto);
		} finally {
			obj = null;
			objCompleto = null;
			setCampoConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
			setListaConsultaFuncionario(null);
		}
	}

	public void montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		for (FuncionarioCargoVO obj : cargos) {
			if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
				selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
			}
		}
		if (isFuncionarioPrincipal()) {
			setListaCargoFuncionarioPrincipal(selectItems);
		} else if (getFuncionarioTerceiro()) {
			setListaCargoFuncionarioTerceiro(selectItems);
		} else if (getFuncionarioQuarto()) {
			setListaCargoFuncionarioQuarto(selectItems);
		} else if (getFuncionarioQuinto()) {
			setListaCargoFuncionarioQuinto(selectItems);
		} else {
			setListaCargoFuncionarioSecundario(selectItems);
		}
	}

	public void montarComboCargoReitor(List<FuncionarioCargoVO> cargos) throws Exception {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		for (FuncionarioCargoVO obj : cargos) {
			if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
				selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
			}
		}
		setListaCargoReitor(selectItems);
	}

	public void controlarFuncionarioPrincipal() {
		setFuncionarioPrincipal(Boolean.TRUE);
		setFuncionarioSecundario(Boolean.FALSE);
		setFuncionarioTerceiro(Boolean.FALSE);
		setFuncionarioQuarto(Boolean.FALSE);
		setFuncionarioQuinto(Boolean.FALSE);
	}

	public void controlarFuncionarioSecundario() {
		setFuncionarioPrincipal(Boolean.FALSE);
		setFuncionarioSecundario(Boolean.TRUE);
		setFuncionarioTerceiro(Boolean.FALSE);
		setFuncionarioQuarto(Boolean.FALSE);
		setFuncionarioQuinto(Boolean.FALSE);	
	}

	public void controlarFuncionarioTerceiro() {
		setFuncionarioPrincipal(Boolean.FALSE);
		setFuncionarioSecundario(Boolean.FALSE);
		setFuncionarioTerceiro(Boolean.TRUE);
		setFuncionarioQuarto(Boolean.FALSE);
		setFuncionarioQuinto(Boolean.FALSE);	
	}
	
	public void controlarFuncionarioQuarto() {
		setFuncionarioPrincipal(Boolean.FALSE);
		setFuncionarioSecundario(Boolean.FALSE);
		setFuncionarioTerceiro(Boolean.FALSE);
		setFuncionarioQuarto(Boolean.TRUE);
		setFuncionarioQuinto(Boolean.FALSE);
	}
	
	public void controlarFuncionarioQuinto() {
		setFuncionarioPrincipal(Boolean.FALSE);
		setFuncionarioSecundario(Boolean.FALSE);
		setFuncionarioTerceiro(Boolean.FALSE);
		setFuncionarioQuarto(Boolean.FALSE);
		setFuncionarioQuinto(Boolean.TRUE);
	}	

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));

		return itens;
	}

	public List<SelectItem> getTipoConsultarComboMatricula() {
		if (tipoConsultarComboMatricula == null) {
			tipoConsultarComboMatricula = new ArrayList<>(0);
			tipoConsultarComboMatricula.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultarComboMatricula.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultarComboMatricula.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultarComboMatricula;
	}

	public String getMascaraConsulta() {
		return Constantes.EMPTY;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("dataExpedicao", "Data Expedição"));
		itens.add(new SelectItem("via", "Via"));
		return itens;
	}

	public String inicializarConsultar() {
		try {
			removerObjetoMemoria(this);
			setListaConsulta(new ArrayList<>(0));
			setControleConsultaExpedicaoDiploma(new ControleConsultaExpedicaoDiploma());
			setListaSelectItemUnidadeEnsino(new ArrayList<>());
			montarListaSelectItemUnidadeEnsino(Constantes.EMPTY);
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("expedicaoDiplomaCons.xhtml");
	}

	public String getCampoConsultarMatricula() {
		if (campoConsultarMatricula == null) {
			campoConsultarMatricula = Constantes.EMPTY;
		}
		return campoConsultarMatricula;
	}

	public void setCampoConsultarMatricula(String campoConsultarMatricula) {
		this.campoConsultarMatricula = campoConsultarMatricula;
	}

	public String getValorConsultarMatricula() {
		if (valorConsultarMatricula == null) {
			valorConsultarMatricula = Constantes.EMPTY;
		}
		return valorConsultarMatricula;
	}

	public void setValorConsultarMatricula(String valorConsultarMatricula) {
		this.valorConsultarMatricula = valorConsultarMatricula;
	}

	@SuppressWarnings("rawtypes")
	public List getListaConsultarMatricula() {
		if (listaConsultarMatricula == null) {
			listaConsultarMatricula = new ArrayList<>(0);
		}
		return listaConsultarMatricula;
	}

	@SuppressWarnings("rawtypes")
	public void setListaConsultarMatricula(List listaConsultarMatricula) {
		this.listaConsultarMatricula = listaConsultarMatricula;
	}

	public ExpedicaoDiplomaVO getExpedicaoDiplomaVO() {
		if (expedicaoDiplomaVO == null) {
			expedicaoDiplomaVO = new ExpedicaoDiplomaVO();
		}
		return expedicaoDiplomaVO;
	}

	public void setExpedicaoDiplomaVO(ExpedicaoDiplomaVO expedicaoDiplomaVO) {
		this.expedicaoDiplomaVO = expedicaoDiplomaVO;
	}

	public Boolean getHabilitaBotaoImprimirDiploma() {
		if (habilitaBotaoImprimirDiploma == null) {
			habilitaBotaoImprimirDiploma = false;
		}
		return habilitaBotaoImprimirDiploma;
	}

	public void setHabilitaBotaoImprimirDiploma(Boolean habilitaBotaoImprimirDiploma) {
		this.habilitaBotaoImprimirDiploma = habilitaBotaoImprimirDiploma;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>();
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	@SuppressWarnings("rawtypes")
	public List getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList<>(0);
		}
		return listaSelectItemFuncionario;
	}

	@SuppressWarnings("rawtypes")
	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = Constantes.EMPTY;
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = Constantes.EMPTY;
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	@SuppressWarnings("rawtypes")
	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList(0);
		}
		return listaConsultaFuncionario;
	}

	@SuppressWarnings("rawtypes")
	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	@SuppressWarnings("rawtypes")
	public List getListaCargoFuncionarioPrincipal() {
		if (listaCargoFuncionarioPrincipal == null) {
			listaCargoFuncionarioPrincipal = new ArrayList(0);
		}
		return listaCargoFuncionarioPrincipal;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoFuncionarioPrincipal(List listaCargoFuncionarioPrincipal) {
		this.listaCargoFuncionarioPrincipal = listaCargoFuncionarioPrincipal;
	}

	public Boolean isFuncionarioPrincipal() {
		if (funcionarioPrincipal == null) {
			funcionarioPrincipal = false;
		}
		return funcionarioPrincipal;
	}

	public void setFuncionarioPrincipal(Boolean funcionarioPrincipal) {
		this.funcionarioPrincipal = funcionarioPrincipal;
	}

	public Boolean getFuncionarioTerceiro() {
		if (funcionarioTerceiro == null) {
			funcionarioTerceiro = false;
		}
		return funcionarioTerceiro;
	}

	public void setFuncionarioTerceiro(Boolean funcionarioTerceiro) {
		this.funcionarioTerceiro = funcionarioTerceiro;
	}

	@SuppressWarnings("rawtypes")
	public List getListaCargoFuncionarioSecundario() {
		if (listaCargoFuncionarioSecundario == null) {
			listaCargoFuncionarioSecundario = new ArrayList(0);
		}
		return listaCargoFuncionarioSecundario;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoFuncionarioSecundario(List listaCargoFuncionarioSecundario) {
		this.listaCargoFuncionarioSecundario = listaCargoFuncionarioSecundario;
	}

	@SuppressWarnings("rawtypes")
	public List getListaCargoFuncionarioTerceiro() {
		if (listaCargoFuncionarioTerceiro == null) {
			listaCargoFuncionarioTerceiro = new ArrayList(0);
		}
		return listaCargoFuncionarioTerceiro;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoFuncionarioTerceiro(List listaCargoFuncionarioTerceiro) {
		this.listaCargoFuncionarioTerceiro = listaCargoFuncionarioTerceiro;
	}

	public FuncionarioVO getFuncionarioPrincipalVO() {
		if (funcionarioPrincipalVO == null) {
			funcionarioPrincipalVO = new FuncionarioVO();
		}
		return funcionarioPrincipalVO;
	}

	public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
		this.funcionarioPrincipalVO = funcionarioPrincipalVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public FuncionarioVO getFuncionarioTerceiroVO() {
		if (funcionarioTerceiroVO == null) {
			funcionarioTerceiroVO = new FuncionarioVO();
		}
		return funcionarioTerceiroVO;
	}

	public void setFuncionarioTerceiroVO(FuncionarioVO funcionarioTerceiroVO) {
		this.funcionarioTerceiroVO = funcionarioTerceiroVO;
	}

	public CargoVO getCargoFuncionarioPrincipal() {
		if (cargoFuncionarioPrincipal == null) {
			cargoFuncionarioPrincipal = new CargoVO();
		}
		return cargoFuncionarioPrincipal;
	}

	public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
		this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
	}

	public CargoVO getCargoFuncionarioSecundario() {
		if (cargoFuncionarioSecundario == null) {
			cargoFuncionarioSecundario = new CargoVO();
		}
		return cargoFuncionarioSecundario;
	}

	public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
		this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
	}

	public CargoVO getCargoFuncionarioTerceiro() {
		if (cargoFuncionarioTerceiro == null) {
			cargoFuncionarioTerceiro = new CargoVO();
		}
		return cargoFuncionarioTerceiro;
	}

	public void setCargoFuncionarioTerceiro(CargoVO cargoFuncionarioTerceiro) {
		this.cargoFuncionarioTerceiro = cargoFuncionarioTerceiro;
	}

	public boolean isImprimirDiploma() {
		return imprimirDiploma;
	}

	public void setImprimirDiploma(boolean imprimirDiploma) {
		this.imprimirDiploma = imprimirDiploma;
	}

	public void setExisteColacao(Boolean existeColacao) {
		this.existeColacao = existeColacao;
	}

	public Boolean getExisteColacao() {
		if (existeColacao == null) {
			existeColacao = false;
		}
		return existeColacao;
	}

	public Boolean getIsLayoutGraduacao3() {
		if (getTipoLayout().equals("DiplomaAlunoSuperior3Rel")) {
			return Boolean.TRUE;
		}
		if (getTipoLayout().equals("DiplomaAlunoSuperior5Rel") || getTipoLayout().equals("DiplomaAlunoSuperior6Rel")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getIsLayoutGraduacao5() {
		return getIsLayoutGraduacao3();
	}

	public void setColacaoGrauVO(ColacaoGrauVO colacaoGrauVO) {
		this.colacaoGrauVO = colacaoGrauVO;
	}

	public ColacaoGrauVO getColacaoGrauVO() {
		if (colacaoGrauVO == null) {
			colacaoGrauVO = new ColacaoGrauVO();
		}
		return colacaoGrauVO;
	}

	public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
		this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
	}

	public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
		if (programacaoFormaturaAlunoVO == null) {
			programacaoFormaturaAlunoVO = new ProgramacaoFormaturaAlunoVO();
		}
		return programacaoFormaturaAlunoVO;
	}

	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Boolean getApresentarCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("dataExpedicao")) {
			return true;
		}
		return false;
	}
	
	public Boolean getApresentarCampoCargoFuncionarioPrincipal() {
		return (getHabilitaBotaoImprimirDiploma() || getApresentarResultadoConsultaExpedicaoDiploma()) &&
				((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO())));
	}

	public boolean isApresentarCampoCargoFuncionarioSecundario() {
		return  (getHabilitaBotaoImprimirDiploma() || getApresentarResultadoConsultaExpedicaoDiploma()) && 
				((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO())));
	}

	public boolean isApresentarCampoCargoFuncionarioTerceiro() {
		return  (getHabilitaBotaoImprimirDiploma() || getApresentarResultadoConsultaExpedicaoDiploma())  && 
				((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO())));
	}
	
	public boolean isApresentarCampoCargoFuncionarioQuarto() {
		return (getHabilitaBotaoImprimirDiploma() || getApresentarResultadoConsultaExpedicaoDiploma()) && 
				((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioQuartoVO())));
	}
	
	public boolean isApresentarCampoCargoFuncionarioQuinto() {
		return (getHabilitaBotaoImprimirDiploma() || getApresentarResultadoConsultaExpedicaoDiploma()) && 
				((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioQuintoVO())));
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = Constantes.EMPTY;
		}
		return tipoLayout;
	}

	public boolean getIsHistoricoPosGraduacao() {
		return getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao();
	}

	/**
	 * @return the abrirModalVerso
	 */
	public String getAbrirModalVerso() {
		if (abrirModalVerso == null || isAssinarDigitalmente()) {
			abrirModalVerso = Constantes.EMPTY;
		}
		return abrirModalVerso;
	}

	/**
	 * @param abrirModalVerso
	 *            the abrirModalVerso to set
	 */
	public void setAbrirModalVerso(String abrirModalVerso) {
		this.abrirModalVerso = abrirModalVerso;
	}

	public String getAbrirModalPanelOK() {
		if (getAbrirModalOK()) {
			return "RichFaces.$('panelOk').show()";
		}
		return Constantes.EMPTY;
	}

	public Boolean getAbrirModalOK() {
		if (abrirModalOK == null) {
			abrirModalOK = Boolean.FALSE;
		}
		return abrirModalOK;
	}

	public void setAbrirModalOK(Boolean abrirModalOK) {
		this.abrirModalOK = abrirModalOK;
	}

	public List<MatriculaVO> getListaMatriculas() {
		if (listaMatriculas == null) {
			listaMatriculas = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculas;
	}

	public void setListaMatriculas(List<MatriculaVO> listaMatriculas) {
		this.listaMatriculas = listaMatriculas;
	}

	public Boolean getPermissaoAlterarUnidadeEnsinoCertificadora() {
		return getLoginControle().getPermissaoAcessoMenuVO().getPermiteAlterarUnidadeEnsinoCertificadora();
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemUnidadeEnsino() throws Exception {
		try {
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getPermissaoAlterarUnidadeEnsinoCertificadora()) {
				montarListaSelectItemUnidadeEnsino(Constantes.EMPTY);
			} else {
				montarListaSelectItemUnidadeEnsinoEspecifica();
				if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO()) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora())) {
					if (!getListaSelectItemUnidadeEnsino().isEmpty()) {
						if (!((List<SelectItem>) getListaSelectItemUnidadeEnsino()).stream().anyMatch(l -> l.getValue().equals(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getCodigo()))) {
							getListaSelectItemUnidadeEnsino().add(new SelectItem(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getCodigo(), getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getNome()));
						}
					} else if (getListaSelectItemUnidadeEnsino().isEmpty()) {
						List<SelectItem> objs = new ArrayList<>(0);
						objs.add(new SelectItem(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getCodigo(), getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getNome()));
						getListaSelectItemUnidadeEnsino().add(objs);
					}
				}
			}
			setMensagemID(Constantes.EMPTY);
		} catch (Exception e) {
			 throw e;
		}
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemUnidadeEnsinoEspecifica() throws Exception {
		List resultadoConsulta = new ArrayList<>(0);
		Iterator i = null;
		try {
			UnidadeEnsinoVO unidadeEnsinoPrincipal = new UnidadeEnsinoVO();
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital()) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao())) {
				unidadeEnsinoPrincipal = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(unidadeEnsinoPrincipal)) {
					resultadoConsulta.add(unidadeEnsinoPrincipal);
				}
			} else {
				unidadeEnsinoPrincipal = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(false, false, 0, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(unidadeEnsinoPrincipal)) {
					resultadoConsulta.add(unidadeEnsinoPrincipal);
				}
			}
			if ((Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO()) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital()) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao())) || (!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital()) || !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao()))) {
				if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino()) && ((Uteis.isAtributoPreenchido(unidadeEnsinoPrincipal) && !(unidadeEnsinoPrincipal.getCodigo().equals(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo()))) || !Uteis.isAtributoPreenchido(unidadeEnsinoPrincipal))) {
					resultadoConsulta.add(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino());
				}
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, Constantes.EMPTY));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	@SuppressWarnings("rawtypes")
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	@SuppressWarnings("rawtypes")
	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	@SuppressWarnings("rawtypes")
	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	@SuppressWarnings("rawtypes")
	public List getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	@SuppressWarnings("rawtypes")
	public void setListaSelectItemTipoTextoPadrao(List listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public String getObservacaoDiploma() {
		if (observacaoDiploma == null) {
			observacaoDiploma = Constantes.EMPTY;
		}
		return observacaoDiploma;
	}

	public void setObservacaoDiploma(String observacaoDiploma) {
		this.observacaoDiploma = observacaoDiploma;
	}

	public String getTituloFuncionarioPrincipal() {
		if (tituloFuncionarioPrincipal == null) {
			tituloFuncionarioPrincipal = Constantes.EMPTY;
		}
		return tituloFuncionarioPrincipal;
	}

	public void setTituloFuncionarioPrincipal(String tituloFuncionarioPrincipal) {
		this.tituloFuncionarioPrincipal = tituloFuncionarioPrincipal;
	}

	public String getTituloFuncionarioSecundario() {
		if (tituloFuncionarioSecundario == null) {
			tituloFuncionarioSecundario = Constantes.EMPTY;
		}
		return tituloFuncionarioSecundario;
	}

	public void setTituloFuncionarioSecundario(String tituloFuncionarioSecundario) {
		this.tituloFuncionarioSecundario = tituloFuncionarioSecundario;
	}

	public String getTituloFuncionarioTerceiro() {
		if (tituloFuncionarioTerceiro == null) {
			tituloFuncionarioTerceiro = Constantes.EMPTY;
		}
		return tituloFuncionarioTerceiro;
	}

	public void setTituloFuncionarioTerceiro(String tituloFuncionarioTerceiro) {
		this.tituloFuncionarioTerceiro = tituloFuncionarioTerceiro;
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (!Uteis.isAtributoPreenchido(objAluno)) {
				throw new Exception("Aluno de matrícula " + getExpedicaoDiplomaVO().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getExpedicaoDiplomaVO().setMatricula(objAluno);
			realizarMontagemNovaExpedicaoDiploma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getAbrirModalPanelConclusaoCurso() {
		try {
			if (getExpedicaoDiplomaVO().getMatricula().getDataConclusaoCurso() == null || getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso() == null) {
				return "RichFaces.$('panelConclusaoCurso').show()";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Constantes.EMPTY;
	}
	
	private String oncompleteData;
	
	public String getOncompleteData() {
		if (oncompleteData == null) {
			oncompleteData = Constantes.EMPTY;
		}
		return oncompleteData;
	}
	
	public void setOncompleteData(String oncompleteData) {
		this.oncompleteData = oncompleteData;
	}
	
	public void alterarDataConclusaoCurso() {
		try {
			setOncompleteData(Constantes.EMPTY);
			Uteis.checkState(!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso()), "A DATA INÍCIO CURSO deve ser informado");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula().getDataConclusaoCurso()), "DATA CONCLUSÃO CURSO deve ser informado");
			if (getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso().after(getExpedicaoDiplomaVO().getMatricula().getDataConclusaoCurso())) {
				throw new Exception("A DATA INÍCIO do CURSO ("+ Uteis.getData(getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso(), "dd/MM/yyyy") +") não poder ser maior do que a DATA CONCLUSÃO CURSO do aluno ("+ Uteis.getData(getExpedicaoDiplomaVO().getMatricula().getDataConclusaoCurso(), "dd/MM/yyyy") +").");
			}
			getFacadeFactory().getMatriculaFacade().alterarDataInicioEDataConclusaoCursoPorMatricula(getExpedicaoDiplomaVO().getMatricula(), getUsuarioLogado());
//			if (isExibirListaDocumentoAssinados()) {
//			} else {
//				imprimirPDF();
//			}
			setOncompleteData("RichFaces.$('panelConclusaoCurso').hide()");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setOncompleteData(Constantes.EMPTY);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarObservacaoComplementarDiplomaAlteracao() {
		try {
			List<ObservacaoComplementarDiplomaVO> listaAtualizada = new ArrayList<ObservacaoComplementarDiplomaVO>();
			for (ObservacaoComplementarVO observacosGravar : getListaObservacaoComplementarVOs()) {
				if (observacosGravar.getSelecionado()) {
					ObservacaoComplementarDiplomaVO novoRegistro = new ObservacaoComplementarDiplomaVO();
					novoRegistro.setObservacaoComplementar(observacosGravar);
					listaAtualizada.add(novoRegistro);
				}
			}
			getExpedicaoDiplomaVO().setObservacaoComplementarDiplomaVOs(listaAtualizada);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void prepararObservacaoComplementarDiplomaAlteracao() {
		try {
			setListaObservacaoComplementarVOs(getFacadeFactory().getObservacaoComplementarFacade().consultarPorNome("%%%", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getExpedicaoDiplomaVO().getObservacaoComplementarDiplomaVOs().size() > 0) {
				// rotina que já marca os itens que estão gravados na base de dados, para que usuário perceba
				// quais são as observacoes que vao sair no diploma do aluno.
				List<ObservacaoComplementarDiplomaVO> listaJaSelecionadaObservacoes = getExpedicaoDiplomaVO().getObservacaoComplementarDiplomaVOs();
				for (ObservacaoComplementarDiplomaVO obsSelecionado : listaJaSelecionadaObservacoes) {
					for (ObservacaoComplementarVO objMarcar : getListaObservacaoComplementarVOs()) {
						if (objMarcar.getCodigo().equals(obsSelecionado.getObservacaoComplementar().getCodigo())) {
							objMarcar.setSelecionado(Boolean.TRUE);
							break;
						}
					}
				}
			}
			validarQuantidadeObservacoesSelecionadas();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    
    public Boolean getLayoutComObservacaoComplementar() {
    	if (!getTipoLayout().equals("DiplomaAlunoPos3Rel") && !getTipoLayout().equals("DiplomaAlunoSuperior3Rel")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
        
    }    
    
    public void realizarGeracaoEtiqueta() {
        setFazerDownload(Boolean.FALSE);
        try {
            MatriculaPeriodoVO ultimoMatriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getExpedicaoDiplomaVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getLayoutEtiquetaVO().setCodigoExpedicaoDiploma(getExpedicaoDiplomaVO().getCodigo());
            setCaminhoRelatorio(getFacadeFactory().getEtiquetaAlunoRelFacade().realizarImpressaoEtiquetaMatricula(
                    getLayoutEtiquetaVO(), getNumeroCopias(), getLinha(), getColuna(), 
                    getExpedicaoDiplomaVO().getMatricula().getCurso().getCodigo(), 
                    ultimoMatriculaPeriodoVO.getTurma().getCodigo(), getExpedicaoDiplomaVO().getMatricula().getMatricula(), 
                    ultimoMatriculaPeriodoVO.getAno(), ultimoMatriculaPeriodoVO.getSemestre(), 
                    ultimoMatriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo(), "aluno", 
                    getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional(), 
                    getConfiguracaoGeralPadraoSistema(), null, getUsuarioLogado(), getExpedicaoDiplomaVO().getVia()));
            super.setFazerDownload(Boolean.TRUE);
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }    

	public List<ObservacaoComplementarVO> getListaObservacaoComplementarVOs() {
		if (listaObservacaoComplementarVOs == null) {
			listaObservacaoComplementarVOs = new ArrayList<ObservacaoComplementarVO>();
		}
		return listaObservacaoComplementarVOs;
	}

	public void setListaObservacaoComplementarVOs(List<ObservacaoComplementarVO> listaObservacaoComplementarVOs) {
		this.listaObservacaoComplementarVOs = listaObservacaoComplementarVOs;
	}
	

	public void prepararEmitirEtiquetaDiploma() {
		setListaSelectItemlayoutEtiqueta(null);
		getListaSelectItemlayoutEtiqueta();
	}

	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
					getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
				}
				for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
					getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	/**
	 * @return the layoutImpressao
	 */
	public String getLayoutImpressao() {
		if (layoutImpressao == null) {
			layoutImpressao = Constantes.EMPTY;
		}
		return layoutImpressao;
	}

	/**
	 * @param layoutImpressao
	 *            the layoutImpressao to set
	 */
	public void setLayoutImpressao(String layoutImpressao) {
		this.layoutImpressao = layoutImpressao;
	}

	/**
	 * @return the layoutEtiquetaVO
	 */
	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	/**
	 * @param layoutEtiquetaVO
	 *            the layoutEtiquetaVO to set
	 */
	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	/**
	 * @return the numeroCopias
	 */
	public Integer getNumeroCopias() {
		if (numeroCopias == null) {
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	/**
	 * @param numeroCopias
	 *            the numeroCopias to set
	 */
	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	/**
	 * @return the coluna
	 */
	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	/**
	 * @param coluna
	 *            the coluna to set
	 */
	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	/**
	 * @return the linha
	 */
	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	/**
	 * @param linha
	 *            the linha to set
	 */
	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	/**
	 * @return the listaSelectItemlayoutEtiqueta
	 */
	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
			try {
				List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.MATRICULA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				listaSelectItemlayoutEtiqueta.add(new SelectItem(0, Constantes.EMPTY));
				for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
					listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemlayoutEtiqueta;
	}

	/**
	 * @param listaSelectItemlayoutEtiqueta
	 *            the listaSelectItemlayoutEtiqueta to set
	 */
	public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
		this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
	}

	/**
	 * @return the listaSelectItemColuna
	 */
	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	/**
	 * @param listaSelectItemColuna
	 *            the listaSelectItemColuna to set
	 */
	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	/**
	 * @return the listaSelectItemLinha
	 */
	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	/**
	 * @param listaSelectItemLinha
	 *            the listaSelectItemLinha to set
	 */
	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	@SuppressWarnings("rawtypes")
	public List getListaCargoReitor() {
		if (listaCargoReitor == null) {
			listaCargoReitor = new ArrayList(0);
		}
		return listaCargoReitor;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoReitor(List listaCargoReitor) {
		this.listaCargoReitor = listaCargoReitor;
	}

	public String getTextoRelatorio() {
		if (getImprimirContrato()) {
			try {
				if(getCaminhoRelatorio().contains(ServidorArquivoOnlineEnum.AMAZON_S3.name())) {
					return "abrirPopup('"+getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUrlAcessoExternoAplicacao()+"/DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "', 'DownloadRelatorioSV_"+ new Date().getTime() +"', 950,595)";
				} else {
					return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); ";
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else if (getFazerDownload()) {
			return getDownload();
		}
		return Constantes.EMPTY;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public boolean isPermitirExibirRegraEmissao() {
		return permitirExibirRegraEmissao;
	}

	public void setPermitirExibirRegraEmissao(boolean permitirExibirRegraEmissao) {
		this.permitirExibirRegraEmissao = permitirExibirRegraEmissao;
	}
	
	public String getUrlDonloadSV() {
		if(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
			return "abrirPopup('"+getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()+"/DownloadSV', 'DownloadSV_"+ new Date().getTime() +"', 950,595)";
		}else if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public String getDownload() {
		if (!request().getRequestURI().contains("expedicaoDiplomaLote")) {
			if (getFazerDownload() && !isAssinarDigitalmente() && !getUtilizarCaminhoDownloadArquivoAssinadoDigitalmente()) {
				return super.getDownload();
			}
			if (getFazerDownload() && isAssinarDigitalmente() && getUtilizarCaminhoDownloadArquivoAssinadoDigitalmente()) {
				setFazerDownload(Boolean.FALSE);
				setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(Boolean.FALSE);
				try {
					realizarDownloadArquivo(getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
					try {
						if(getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
							return "abrirPopupMaximizado3('"+getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUrlAcessoExternoAplicacao()+"/DownloadRelatorioSV?relatorio=" + getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().getNome() + "', 'DownloadRelatorioSV_"+ new Date().getTime() +"', 450, 450)";
						} else {
							return "location.href='../../DownloadSV'";
						}
					} catch (Exception e) {
						setMensagemDetalhada("msg_erro", e.getMessage());
					}
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getFazerDownload()) {
				return super.getDownload();
			}
		}
		return Constantes.EMPTY;
	}
	
	
	public String getDownloadArquivoExpedicaoDiplomaLote() {
		if (getFazerDownload() && !isAssinarDigitalmente() && !getUtilizarCaminhoDownloadArquivoAssinadoDigitalmente()) {
			String download = super.getDownload();
			setOnCompleteDownloadExpedicaoDiplomaLote(download);
			return download;
		}
//		if (getFazerDownload() && isAssinarDigitalmente() && getUtilizarCaminhoDownloadArquivoAssinadoDigitalmente()) {
//			setFazerDownload(Boolean.FALSE);
//			setUtilizarCaminhoDownloadArquivoAssinadoDigitalmente(Boolean.FALSE);
//			try {
//				realizarDownloadArquivo(getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
//				return "location.href='../../DownloadSV'";
//			} catch (CloneNotSupportedException e) {
//				e.printStackTrace();
//			}
//		}
		if (getFazerDownload() && Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
			String download = super.getDownload();
			setOnCompleteDownloadExpedicaoDiplomaLote(download);
			return download;
		}
		
		return Constantes.EMPTY;
	}
	
	public boolean isExisteDiplomaDigitalGerado() {
		return Uteis.isAtributoPreenchido(getListaDocumentoAsssinados()) && getListaDocumentoAsssinados().stream().anyMatch(documentoAssinado -> documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL));
	}
	
	public void imprimirPDFImpressaoDiploma() {
		try {
			setModalErro(Constantes.EMPTY);
			getExpedicaoDiplomaVO().setExisteDiplomaDigitalGerado(Boolean.FALSE);
			if (isAssinarDigitalmente()) {
				if (isExpedicaoDiplomaGerarXmlDiploma()) {
					inicializarDadosAssinaturaExpedicaoDiploma(getExpedicaoDiplomaVO());
					getExpedicaoDiplomaVO().setExisteDiplomaDigitalGerado(isExisteDiplomaDigitalGerado());
					setTipoXmlMecGerar(null);
					setListaSelectItemTipoXmlMec(null);
					setModalErro("RichFaces.$('panelAvisoAssinaturaDigital').show();");
				} else {
					imprimirPDF();
				}
			} else {
				imprimirPDF();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void inicializarDadosAssinaturaExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiplomaVO) throws Exception {
		DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
		obj.setMatricula(expedicaoDiplomaVO.getMatricula());
		consultarDocumentosAssinados();
	}
	
	public void excluirDocumentoAssinado() {
		try {
			setOnCompleteRejeicaoDocumento(Constantes.EMPTY);
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO) getRequestMap().get("documentoAssinadoItem");
			if (Uteis.isAtributoPreenchido(obj)) {
				Boolean possuiProvededorAssinatura = obj.getListaDocumentoAssinadoPessoa().stream().anyMatch(n -> n.getProvedorAssinatura().equals("LACUNAS") || n.getProvedorAssinatura().equals("BRY"));
				if (possuiProvededorAssinatura) {
					setDocumentoAssinadoRejeicao(obj);
					setOnCompleteRejeicaoDocumento("RichFaces.$('panelAvisoRejeicaoDocumentoAssinadoProvedorAssinatura').show()");
					return;
				}
				getFacadeFactory().getDocumentoAssinadoFacade().excluir(obj, false, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()));
				getListaDocumentoAsssinados().removeIf(davo -> davo.getCodigo().equals(obj.getCodigo()));
				setMensagemID("msg_dados_excluidos");
				consultarUltimoDocumentoAssinadoDiplomaDigital();
				if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO())) {
					getExpedicaoDiplomaVO().setDiplomaDigital(new DocumentoAssinadoVO());
				}
				montarDadosUltimoDiplomaAluno();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	 /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ProgramacaoFormaturaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String consultarProgramacaoFormatura() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
			//if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {

	            if (getCampoConsultaProgramacaoFormatura().equals("codigo")) {
	                if (getValorConsultaProgramacaoFormatura().equals(Constantes.EMPTY)) {
	                    setValorConsultaProgramacaoFormatura("0");
	                }
	                if (getValorConsultaProgramacaoFormatura().trim() != null || !getValorConsultaProgramacaoFormatura().trim().isEmpty()) {
	                    Uteis.validarSomenteNumeroString(getValorConsultaProgramacaoFormatura().trim());
	                }
	                int valorInt = Integer.parseInt(getValorConsultaProgramacaoFormatura());
	                if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {               	
	                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	                }else {
	                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigoFiltroAlunosPresentesColacaoGrau(new Integer(valorInt), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	                }
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeUnidadeEnsino")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorProgramacaoUnidadeEnsino(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUnidadeEnsinoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeCurso")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCursoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
	                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeTurno")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurnoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
	                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("identificadorTurmaTurma")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
	                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("matriculaMatricula")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
	                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatriculaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
	                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("nomeUsuario")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuarioFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
	                    || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
	                objs = validarDataConsultaProgramacaoFormatura(objs);
	            }
	            setListaConsultaProgramacaoFormatura(objs);	           
	            setMensagemID("msg_dados_consultados");
	            return "consultar";
//			} else {
//				throw new Exception("Por Favor Informe a Unidade de Ensino.");
//			}
        } catch (Exception e) {
        	setListaConsultaProgramacaoFormatura(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }
    
	

	public boolean getApresentarResultadoConsultaProgramacaoFormatura() {
		if (this.getListaConsultaProgramacaoFormatura() == null ||
				this.getListaConsultaProgramacaoFormatura().size() == 0 ) {
			return false;
		}
		return true;
	}

    @SuppressWarnings("rawtypes")
	public List validarDataConsultaProgramacaoFormatura(List objs) throws Exception {
        if (getValorConsultaDataFinalProgramacaoFormatura() != null && getValorConsultaDataInicioProgramacaoFormatura() != null) {
            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento")) {
            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {          		            	
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}else {
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}
            }
            if (getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")) {
            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}else {
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}
            }
            if (getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}else {
            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	}
            }
        } else {
            throw new ConsistirException("Por favor digite uma data válida.");
        }

        return objs;
    }

    public void selecionarProgramacaoFormatura() throws Exception {		
        ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
        obj.setNovoObj(Boolean.FALSE);
        setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
        montarListaExpedicaoDiplomaEmitirPorProgramacaoFormatura(getProgramacaoFormaturaVO());       
		montarListaSelectItemUnidadeEnsinoLote();
		setApresentarProgramacaoFormatura(Boolean.TRUE);
		setValorConsultaProgramacaoFormatura(Constantes.EMPTY);
		setCampoConsultaProgramacaoFormatura(Constantes.EMPTY);
        getListaConsultaProgramacaoFormatura().clear();
        setMotivoRejeicaoDiploma(Constantes.EMPTY);
        setFiltroSituacaoDiplomaDigitalLote(Constantes.EMPTY);
        setFiltroSituacaoDocumentacaoAcademicaLote(Constantes.EMPTY);
        limparFilterFactory();
        setAssinarDigitalmente(Boolean.TRUE);
    }  
    
    public void mostrarSegundoCampoProgramacaoFormatura() {
        if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
                || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
        	setMostrarSegundoCampoProgramacaoFormatura(Boolean.TRUE);
        } else {
        	setMostrarSegundoCampoProgramacaoFormatura(Boolean.FALSE);
        }
        setValorConsultaProgramacaoFormatura(Constantes.EMPTY);
        getListaConsultaProgramacaoFormatura().clear();
    }
    
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormatura() {
		if (listaConsultaProgramacaoFormatura == null) {
			listaConsultaProgramacaoFormatura = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormatura;
	}

	public void setListaConsultaProgramacaoFormatura(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura) {
		this.listaConsultaProgramacaoFormatura = listaConsultaProgramacaoFormatura;
	} 	
    
    public String getValorConsultaProgramacaoFormatura() {
    	if(valorConsultaProgramacaoFormatura == null) {
    		valorConsultaProgramacaoFormatura = Constantes.EMPTY;
    	}
		return valorConsultaProgramacaoFormatura;
	}

	public void setValorConsultaProgramacaoFormatura(String valorConsultaProgramacaoFormatura) {
		this.valorConsultaProgramacaoFormatura = valorConsultaProgramacaoFormatura;
	}

	public String getCampoConsultaProgramacaoFormatura() {
    	if(campoConsultaProgramacaoFormatura == null) {
    		campoConsultaProgramacaoFormatura = "codigo";
    	}
		return campoConsultaProgramacaoFormatura;
	}

	public void setCampoConsultaProgramacaoFormatura(String campoConsultaProgramacaoFormatura) {
		this.campoConsultaProgramacaoFormatura = campoConsultaProgramacaoFormatura;
	}

	/**
     * @return the mostrarSegundoCampoProgramacaoFormatura
     */
    public Boolean getMostrarSegundoCampoProgramacaoFormatura() {
        if (mostrarSegundoCampoProgramacaoFormatura == null) {
        	mostrarSegundoCampoProgramacaoFormatura = false;
        }
        return mostrarSegundoCampoProgramacaoFormatura;
    }

    /**
     * @param mostrarSegundoCampoProgramacaoFormatura
     *            the mostrarSegundoCampoProgramacaoFormatura to set
     */
    public void setMostrarSegundoCampoProgramacaoFormatura(Boolean mostrarSegundoCampoProgramacaoFormatura) {
        this.mostrarSegundoCampoProgramacaoFormatura = mostrarSegundoCampoProgramacaoFormatura;
    }
    
    /**
     * @return the valorConsultaDataInicioProgramacaoFormatura
     */
    public Date getValorConsultaDataInicioProgramacaoFormatura() {
        return valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataInicioProgramacaoFormatura
     *            the valorConsultaDataInicioProgramacaoFormatura to set
     */
    public void setValorConsultaDataInicioProgramacaoFormatura(Date valorConsultaDataInicioProgramacaoFormatura) {
        this.valorConsultaDataInicioProgramacaoFormatura = valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @return the valorConsultaDataFinalProgramacaoFormatura
     */
    public Date getValorConsultaDataFinalProgramacaoFormatura() {
        return valorConsultaDataFinalProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataFinalProgramacaoFormatura
     *            the valorConsultaDataFinalProgramacaoFormatura to set
     */
    public void setValorConsultaDataFinalProgramacaoFormatura(Date valorConsultaDataFinalProgramacaoFormatura) {
        this.valorConsultaDataFinalProgramacaoFormatura = valorConsultaDataFinalProgramacaoFormatura;
    }
    
    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List getTipoConsultaComboProgramacaoFormatura() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
        itens.add(new SelectItem("matriculaMatricula", "Matricula"));
        itens.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
        itens.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
        itens.add(new SelectItem("periodoCadastro", "Período Cadastro"));
        itens.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
        return itens;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List getComboFiltroAlunosPresentesColacaoGrau() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("SI", "Presentes"));
        itens.add(new SelectItem("NO", "Ausentes"));
        itens.add(new SelectItem("NI", "Não Informado"));
        itens.add(new SelectItem("AM", "Ambos"));
        return itens;
    }

	public String getFiltroAlunosPresentesColacaoGrau() {
		if(filtroAlunosPresentesColacaoGrau == null) {
			filtroAlunosPresentesColacaoGrau = "AM";
		}
	
		return filtroAlunosPresentesColacaoGrau;
	}

	public void setFiltroAlunosPresentesColacaoGrau(String filtroAlunosPresentesColacaoGrau) {
		this.filtroAlunosPresentesColacaoGrau = filtroAlunosPresentesColacaoGrau;
	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}

	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}

	public Boolean getTrazerAlunosComDiplomaEmitido() {
		if(trazerAlunosComDiplomaEmitido == null) {
			trazerAlunosComDiplomaEmitido = Boolean.FALSE;
		}
		return trazerAlunosComDiplomaEmitido;
	}

	public void setTrazerAlunosComDiplomaEmitido(Boolean trazerAlunosComDiplomaEmitido) {
		this.trazerAlunosComDiplomaEmitido = trazerAlunosComDiplomaEmitido;
	}



	public List<ExpedicaoDiplomaVO> getListaExpedicaoDiplomaVOs() {
		if(listaExpedicaoDiplomaVOs == null ) {
			listaExpedicaoDiplomaVOs = new ArrayList<ExpedicaoDiplomaVO>(0);
		}
		return listaExpedicaoDiplomaVOs;
	}

	public void setListaExpedicaoDiplomaVOs(List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOs) {
		this.listaExpedicaoDiplomaVOs = listaExpedicaoDiplomaVOs;
	}

	
	public void montarListaExpedicaoDiplomaEmitirPorProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO) {		
		try {
			getListaExpedicaoDiplomaVOErros().clear();
			setApresentarListaErros(Boolean.FALSE);
			getFacadeFactory().getExpedicaoDiplomaFacade().montarListaExpedicaoDiplomaEmitirPorProgramacaoFormatura(programacaoFormaturaVO, getListaExpedicaoDiplomaVOs(), getListaExpedicaoDiplomaVOErros(), getTrazerAlunosComDiplomaEmitido(), getUsuarioLogado());
			setFuncionarioPrincipalVO(new FuncionarioVO());
			setFuncionarioSecundarioVO(new FuncionarioVO());
			setFuncionarioTerceiroVO(new FuncionarioVO());
			setFuncionarioQuartoVO(new FuncionarioVO());
			setFuncionarioQuintoVO(new FuncionarioVO());
			LayoutPadraoVO funcionarioPadrao = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampoValor("ExpedicaoDiplomaLote", "FuncionarioPadrao", "UsuarioLogado=" + getUsuarioLogado().getCodigo(), Boolean.FALSE, getUsuarioLogado());
			LayoutPadraoVO assinarDitalmentePadrao = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("ExpedicaoDiplomaLote", "assinarDigitalmentePadrao", Boolean.FALSE, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(assinarDitalmentePadrao)) {
				if (assinarDitalmentePadrao.getValor().equals("true_false")) {
					setGerarXmlDiplomaLote(Boolean.FALSE);
				} else if (assinarDitalmentePadrao.getValor().equals("true_true")) {
					setGerarXmlDiplomaLote(Boolean.TRUE);
				}
			}
			if (Uteis.isAtributoPreenchido(funcionarioPadrao)) {
				if (Uteis.isAtributoPreenchido(funcionarioPadrao.getAssinaturaFunc1())) {
					setFuncionarioPrincipalVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(funcionarioPadrao.getAssinaturaFunc1(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if (getFuncionarioPrincipalVO().getFuncionarioCargoVOs().isEmpty() && getFuncionarioPrincipalVO().getCodigo().intValue() > 0) {
						getFuncionarioPrincipalVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					}
					List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
					for (FuncionarioCargoVO obj : getFuncionarioPrincipalVO().getFuncionarioCargoVOs()) {
						if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
							selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
						}
					}
					setListaCargoFuncionarioPrincipal(selectItems);
					setTituloFuncionarioPrincipal(funcionarioPadrao.getTituloAssinaturaFunc1());
				}
				if (Uteis.isAtributoPreenchido(funcionarioPadrao.getAssinaturaFunc2())) {
					setFuncionarioSecundarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(funcionarioPadrao.getAssinaturaFunc2(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if (getFuncionarioSecundarioVO().getFuncionarioCargoVOs().isEmpty() && getFuncionarioSecundarioVO().getCodigo().intValue() > 0) {
						getFuncionarioSecundarioVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					}
					List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
					for (FuncionarioCargoVO obj : getFuncionarioSecundarioVO().getFuncionarioCargoVOs()) {
						if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
							selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
						}
					}
					setListaCargoFuncionarioSecundario(selectItems);
					setTituloFuncionarioSecundario(funcionarioPadrao.getTituloAssinaturaFunc2());
				}
				if (Uteis.isAtributoPreenchido(funcionarioPadrao.getAssinaturaFunc3())) {
					setFuncionarioTerceiroVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(funcionarioPadrao.getAssinaturaFunc3(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if (getFuncionarioTerceiroVO().getFuncionarioCargoVOs().isEmpty() && getFuncionarioTerceiroVO().getCodigo().intValue() > 0) {
						getFuncionarioTerceiroVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					}
					List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
					for (FuncionarioCargoVO obj : getFuncionarioTerceiroVO().getFuncionarioCargoVOs()) {
						if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
							selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
						}
					}
					setListaCargoFuncionarioTerceiro(selectItems);
					setTituloFuncionarioTerceiro(funcionarioPadrao.getTituloAssinaturaFunc3());
				}
				if (getGerarXmlDiplomaLote()) {
					if (Uteis.isAtributoPreenchido(funcionarioPadrao.getAssinaturaFunc4())) {
						setFuncionarioQuartoVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(funcionarioPadrao.getAssinaturaFunc4(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						if (getFuncionarioQuartoVO().getFuncionarioCargoVOs().isEmpty() && getFuncionarioQuartoVO().getCodigo().intValue() > 0) {
							getFuncionarioQuartoVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getFuncionarioQuartoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						}
						List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
						for (FuncionarioCargoVO obj : getFuncionarioQuartoVO().getFuncionarioCargoVOs()) {
							if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
								selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
							}
						}
						setListaCargoFuncionarioQuarto(selectItems);
						setTituloFuncionarioQuarto(funcionarioPadrao.getTituloAssinaturaFunc4());
					}
					if (Uteis.isAtributoPreenchido(funcionarioPadrao.getAssinaturaFunc5())) {
						setFuncionarioQuintoVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(funcionarioPadrao.getAssinaturaFunc5(), 0, Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						if (getFuncionarioQuintoVO().getFuncionarioCargoVOs().isEmpty() && getFuncionarioQuartoVO().getCodigo().intValue() > 0) {
							getFuncionarioQuintoVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getFuncionarioQuintoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						}
						List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
						for (FuncionarioCargoVO obj : getFuncionarioQuintoVO().getFuncionarioCargoVOs()) {
							if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
								selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
							}
						}
						setListaCargoFuncionarioQuinto(selectItems);
						setTituloFuncionarioQuinto(funcionarioPadrao.getTituloAssinaturaFunc5());
					}
				}
			}
			getExpedicaoDiplomaVO().getMatricula().getCurso().setNivelEducacional(programacaoFormaturaVO.getNivelEducacional());
			getListaTipoLayout().stream().findFirst().map(SelectItem::getValue).map(Object::toString).ifPresent(this::setTipoLayout);
			montarListaSelectItemTipoTextoPadrao();
			limparDadosDiplomaLote();
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getApresentarResultadoConsultaExpedicaoDiploma() {
		if(apresentarResultadoConsultaExpedicaoDiploma == null ) {
			apresentarResultadoConsultaExpedicaoDiploma = Boolean.FALSE;
		}
		return apresentarResultadoConsultaExpedicaoDiploma;
	}

	public void setApresentarResultadoConsultaExpedicaoDiploma(Boolean apresentarResultadoConsultaExpedicaoDiploma) {
		this.apresentarResultadoConsultaExpedicaoDiploma = apresentarResultadoConsultaExpedicaoDiploma;
	}

	
	
	public List<ExpedicaoDiplomaVO> getListaExpedicaoDiplomaVOErros() {
		if(listaExpedicaoDiplomaVOErros == null ) {
			listaExpedicaoDiplomaVOErros = new ArrayList<ExpedicaoDiplomaVO>(0);
		}
		return listaExpedicaoDiplomaVOErros;
	}

	public void setListaExpedicaoDiplomaVOErros(List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaVOErros) {
		this.listaExpedicaoDiplomaVOErros = listaExpedicaoDiplomaVOErros;
	}
	
	
	public void realizarGeracaoExpedicaoDiplomaLote() {
		try {
			validarDadosGeracaoExpedicaoDiplomaLote();
			verificarDadosCargosFuncionariosMontados(Boolean.TRUE);
			List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList());
			Boolean gerarXMLDiploma = isProgramacaoFormaturaGerarXmlDiploma();
			Boolean informarCamposLivroRegistradoraLote = getInformarCamposLivroRegistradoraLote();
			Date dataPublicacaoDO = getDataPublicacaoDiarioOficialLote();
			Date dataRegistroDiploma = getDataRegistroDiplomaLote();
			Date dataExpedicao = getDataExpedicaoDiplomaLote();
			int x = 1;
			for (ExpedicaoDiplomaVO expedicaoDiploma : expedicaoDiplomaVOs) {
				getProgressBarVO().setStatus("Processando Expedição Diploma " + x + " de " + (getProgressBarVO().getMaxValue()) + " ");
				getProgressBarVO().setProgresso(Long.valueOf(x));
				if (!Uteis.isAtributoPreenchido(expedicaoDiploma)) {
					expedicaoDiploma.setGerarXMLDiplomaLote(gerarXMLDiploma);
				}
				if (getTipoLayout().equals("TextoPadrao")) {
					expedicaoDiploma.getTextoPadrao().setCodigo(getTextoPadraoDeclaracao());
				}
				expedicaoDiploma.setConsistirException(new ConsistirException());
				expedicaoDiploma.setInformarCamposLivroRegistradora(informarCamposLivroRegistradoraLote);
				expedicaoDiploma.setFuncionarioPrimarioVO(getFuncionarioPrincipalVO());
				expedicaoDiploma.setFuncionarioSecundarioVO(getFuncionarioSecundarioVO());
				expedicaoDiploma.setFuncionarioTerceiroVO(getFuncionarioTerceiroVO());
				expedicaoDiploma.setFuncionarioQuartoVO(getFuncionarioQuartoVO());
				expedicaoDiploma.setFuncionarioQuintoVO(getFuncionarioQuintoVO());
				expedicaoDiploma.setCargoFuncionarioPrincipalVO(getCargoFuncionarioPrincipal());
				expedicaoDiploma.setCargoFuncionarioSecundarioVO(getCargoFuncionarioSecundario());
				expedicaoDiploma.setCargoFuncionarioTerceiroVO(getCargoFuncionarioTerceiro());
				expedicaoDiploma.setCargoFuncionarioQuartoVO(getCargoFuncionarioQuarto());
				expedicaoDiploma.setCargoFuncionarioQuintoVO(getCargoFuncionarioQuinto());
				expedicaoDiploma.setTituloFuncionarioPrincipal(getTituloFuncionarioPrincipal());
				expedicaoDiploma.setTituloFuncionarioSecundario(getTituloFuncionarioSecundario());
				expedicaoDiploma.setTituloFuncionarioTerceiro(getTituloFuncionarioTerceiro());
				expedicaoDiploma.setTituloFuncionarioQuarto(getTituloFuncionarioQuarto());
				expedicaoDiploma.setTituloFuncionarioQuinto(getTituloFuncionarioQuinto());
				expedicaoDiploma.setGerarXMLDiploma(gerarXMLDiploma);
				if (gerarXMLDiploma) {
					expedicaoDiploma.setMotivoRejeicaoDiplomaDigital(getMotivoRejeicaoDiploma());
				}
				expedicaoDiploma.setVersaoDiploma(getVersaoDiplomaLote());
				expedicaoDiploma.setUnidadeEnsinoCertificadora(getProgressBarVO().getUnidadeEnsinoVO());
				if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getDataPublicacaoDiarioOficial())) {
					expedicaoDiploma.setDataPublicacaoDiarioOficial(dataPublicacaoDO);
				}
				if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getDataRegistroDiploma())) {
					expedicaoDiploma.setDataRegistroDiploma(dataRegistroDiploma);
				}
				if (!Uteis.isAtributoPreenchido(expedicaoDiploma)) {
					expedicaoDiploma.setDataExpedicao(dataExpedicao);
				}
				if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getNumeroRegistroDiploma()) && !Uteis.isAtributoPreenchido(expedicaoDiploma.getNumeroProcesso())) {
					getFacadeFactory().getExpedicaoDiplomaFacade().montarNumeroProcessoERegistroDiplomaVindoMascaraConfiguracaoAcademico(expedicaoDiploma, expedicaoDiploma.getMatricula().getUnidadeEnsino().getCodigo(), expedicaoDiploma.getMatricula().getCurso().getCodigo(), getProgressBarVO().getUsuarioVO());
				}
				try {
					getFacadeFactory().getExpedicaoDiplomaFacade().realizarImpressaoExpedicaoDiploma(expedicaoDiploma, isAssinarDigitalmente(), expedicaoDiploma.getGerarXMLDiploma(), Boolean.TRUE, getSuperParametroRelVO(), getSuperControleRelatorio(), getTipoLayout(), getListaMensagensErro(), getProgressBarVO().getCaminhoWebRelatorio(), getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO());
				} catch (Exception e) {
					getListaExpedicaoDiplomaVOErros().add(expedicaoDiploma);
				} finally {
					if (Uteis.isAtributoPreenchido(getCaminhoRelatorio())) {
						File file = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + getCaminhoRelatorio());
						if (Uteis.isAtributoPreenchido(file)) {
							file.delete();
						}
					}
				}
				setCaminhoRelatorio(Constantes.EMPTY);
				setFazerDownload(Boolean.FALSE);
				x++;
			}
			setOnCompleteDownloadExpedicaoDiplomaLote(Constantes.EMPTY);
			setCaminhoRelatorio(Constantes.EMPTY);
			setFazerDownload(Boolean.FALSE);
			if (isAssinarDigitalmente()) {
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(isAssinarDigitalmente() && gerarXMLDiploma ? "true_true" : "true_false", "ExpedicaoDiplomaLote", "assinarDigitalmentePadrao", getProgressBarVO().getUsuarioVO());
				getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao("UsuarioLogado=" + getProgressBarVO().getUsuarioVO().getCodigo(), "ExpedicaoDiplomaLote", "FuncionarioPadrao", getFuncionarioPrincipalVO().getCodigo(), getFuncionarioSecundarioVO().getCodigo(), getFuncionarioTerceiroVO().getCodigo(), getFuncionarioQuartoVO().getCodigo(), getFuncionarioQuintoVO().getCodigo(), Boolean.FALSE, getTituloFuncionarioPrincipal(), getTituloFuncionarioSecundario(), getTituloFuncionarioTerceiro(), getTituloFuncionarioQuarto(), getTituloFuncionarioQuinto(), Constantes.EMPTY, Constantes.EMPTY, getProgressBarVO().getUsuarioVO(), Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY, Constantes.EMPTY);
			}
			setMotivoRejeicaoDiploma(Constantes.EMPTY);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setOnCompleteDownloadExpedicaoDiplomaLote(Constantes.EMPTY);
			setFazerDownload(Boolean.FALSE);
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			filtrarAlunosGerarXMl();
			setSuperParametroRelVO(null);
			getSuperParametroRelVO().setNomeEspecificoRelatorio(Constantes.EMPTY);
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	
	private void validarDadosGeracaoExpedicaoDiplomaLote() throws ConsistirException {
		if (!(Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado))) {
			throw new ConsistirException("Não existem dados a serem gerados.");
		}
		if(!Uteis.isAtributoPreenchido(getDataExpedicaoDiplomaLote())) {
			throw new ConsistirException("O campo DATA (Expedição Diploma) deve ser informado.");
		}
		if(!Uteis.isAtributoPreenchido(getFuncionarioPrincipalVO().getCodigo())) {
			throw new ConsistirException("O campo Assinatura Funcionário 1 deve ser informado.");
		}
		if((!getGerarXmlDiplomaLote() && !Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo()))) {
			throw new ConsistirException("O campo Assinatura Funcionário 2 deve ser informado.");
		}		
		if(!Uteis.isAtributoPreenchido(getCargoFuncionarioPrincipal().getCodigo())) {
			throw new ConsistirException("O campo Cargo do  Funcionário 1 deve ser informado.");
		}
		if((!getGerarXmlDiplomaLote() && !Uteis.isAtributoPreenchido(getFuncionarioSecundarioVO().getCodigo()))) {
			throw new ConsistirException("O campo Cargo do  Funcionário 2 deve ser informado.");
		}		
		if(getGerarXmlDiplomaLote() && !Uteis.isAtributoPreenchido(getFuncionarioTerceiroVO().getCodigo())) {
			throw new ConsistirException("O campo Assinatura Funcionário 3 deve ser informado.");
		}		
		if(getGerarXmlDiplomaLote() && !Uteis.isAtributoPreenchido(getCargoFuncionarioTerceiro().getCodigo())) {
			throw new ConsistirException("O campo Cargo do  Funcionário 3 deve ser informado.");
		}		
		if (getTituloFuncionarioPrincipal().length() > 100) {
			throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 1 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
		if (getTituloFuncionarioSecundario().length() > 100) {
			throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 2 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
		if (getTituloFuncionarioTerceiro().length() > 100) {
			throw new ConsistirException("O campo TITULO DO FUNCIONÁRIO 3 (Expedição Diploma) excedeu o limite de 100 caracteres.");
		}
	}
	
	
	public void marcarUtilizarUnidadeMatrizTodosAction() {
		
		for(ExpedicaoDiplomaVO obj  :getListaExpedicaoDiplomaVOs()) {
			obj.setUtilizarUnidadeMatriz(getMarcarTodasExpedicaoDiplomaUtilizarMatriz());
		}
	}

	public Boolean getMarcarTodasExpedicaoDiplomaUtilizarMatriz() {
		if(marcarTodasExpedicaoDiplomaUtilizarMatriz == null ) {
			marcarTodasExpedicaoDiplomaUtilizarMatriz = Boolean.FALSE;
		}
		return marcarTodasExpedicaoDiplomaUtilizarMatriz;
	}

	public void setMarcarTodasExpedicaoDiplomaUtilizarMatriz(Boolean marcarTodasExpedicaoDiplomaUtilizarMatriz) {
		this.marcarTodasExpedicaoDiplomaUtilizarMatriz = marcarTodasExpedicaoDiplomaUtilizarMatriz;
	}

	public Date getDataExpedicaoDiplomaLote() {
		if(dataExpedicaoDiplomaLote == null ) {
			dataExpedicaoDiplomaLote = new Date();
		}
		return dataExpedicaoDiplomaLote;
	}

	public void setDataExpedicaoDiplomaLote(Date dataExpedicaoDiplomaLote) {
		this.dataExpedicaoDiplomaLote = dataExpedicaoDiplomaLote;
	}

	public Boolean getApresentarListaErros() {
		if(apresentarListaErros == null ) {
			apresentarListaErros = Boolean.FALSE;
		}
		return apresentarListaErros;
	}

	public void setApresentarListaErros(Boolean apresentarListaErros) {
		this.apresentarListaErros = apresentarListaErros;
	}
	
	
	@SuppressWarnings("rawtypes")
	public void removerListaProgramacao() {
		
		ExpedicaoDiplomaVO obj = (ExpedicaoDiplomaVO) context().getExternalContext().getRequestMap().get("expedicaoDiplomaItens");
		Iterator it = getListaExpedicaoDiplomaVOs().iterator();
		while(it.hasNext()) {
			ExpedicaoDiplomaVO exp = (ExpedicaoDiplomaVO) it.next();
			if(exp.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
				it.remove();
			}
			
		}
		
		
	}
	
	
	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public void executarInicioProgressBarGeracaoExpedicaoDiplomaLote() {
		try {
			setOncompleteModal("RichFaces.$('panelMotivoRejeicao').hide()");
			getListaExpedicaoDiplomaVOErros().clear();
			setApresentarListaErros(Boolean.FALSE);		   
			if(Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado)) {
				if (isAssinarDigitalmente() && getGerarXmlDiplomaLote() && !Uteis.isAtributoPreenchido(getMotivoRejeicaoDiploma())) {
					setOncompleteModal("RichFaces.$('panelMotivoRejeicao').show()");
					setMensagemID("O motivo da rejeição deve ser informado.", Uteis.ALERTA, Boolean.TRUE);
					return;
				}
				validarDadosGeracaoExpedicaoDiplomaLote();
		    	setPossuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote(getPermissaoAlterarUnidadeEnsinoCertificadora());
				setProgressBarVO(new ProgressBarVO());			
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoCertificadoraLote().clone());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList()).size()), "Carregando Expedição Diploma", true, this,	"realizarGeracaoExpedicaoDiplomaLote");
			}else {
				throw new ConsistirException("Não existem dados a serem gerados.");
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public Boolean getPossuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote() {
		if(possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote == null ) {
			possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote = Boolean.FALSE;
		}
		return possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote;
	}

	public void setPossuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote(
			Boolean possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote) {
		this.possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote = possuiPermissaoAlterarEnsinoCertificadoraExpedicaoDiplomaLote;
	}

	public String getOnCompleteDownloadExpedicaoDiplomaLote() {
		if(onCompleteDownloadExpedicaoDiplomaLote == null ) {
			onCompleteDownloadExpedicaoDiplomaLote =Constantes.EMPTY;
		}
		return onCompleteDownloadExpedicaoDiplomaLote;
	}

	public void setOnCompleteDownloadExpedicaoDiplomaLote(String onCompleteDownloadExpedicaoDiplomaLote) {
		this.onCompleteDownloadExpedicaoDiplomaLote = onCompleteDownloadExpedicaoDiplomaLote;
	}

	public Boolean getApresentarProgramacaoFormatura() {
		if(apresentarProgramacaoFormatura == null ) {
			apresentarProgramacaoFormatura = Boolean.FALSE;
		}
		return apresentarProgramacaoFormatura;
	}

	public void setApresentarProgramacaoFormatura(Boolean apresentarProgramacaoFormatura) {
		this.apresentarProgramacaoFormatura = apresentarProgramacaoFormatura;
	}


	public void realizarDownloadArquivoPDF() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItem");
		try {
			realizarDownloadArquivo(obj.getArquivoVisual());
		} catch (CloneNotSupportedException e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDownloadArquivoXML() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItem");
		try {
			realizarDownloadArquivo(obj.getArquivo());
		} catch (CloneNotSupportedException e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioQuartoPorMatricula(Boolean expedicaoDiplomaLote) throws Exception {
		try {
			setFuncionarioPrincipal(Boolean.FALSE);
			setFuncionarioSecundario(Boolean.FALSE);
			setFuncionarioTerceiro(Boolean.FALSE);
			setFuncionarioQuarto(Boolean.TRUE);
			setFuncionarioQuinto(Boolean.FALSE);
			if (expedicaoDiplomaLote) {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioQuartoVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				this.setFuncionarioQuartoVO(objFuncionario);
				setTituloFuncionarioQuarto(Constantes.EMPTY);
				setCargoFuncionarioQuarto(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioQuartoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				getExpedicaoDiplomaVO().setFuncionarioQuartoVO(objFuncionario);
				getExpedicaoDiplomaVO().setTituloFuncionarioQuarto(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setCargoFuncionarioQuartoVO(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFuncionarioQuartoVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setFuncionarioQuartoVO(new FuncionarioVO());
			getListaCargoFuncionarioQuarto().clear();
		}
	}
	
	public void consultarFuncionarioQuintoPorMatricula(Boolean expedicaoDiplomaLote) throws Exception {
		try {
			setFuncionarioPrincipal(Boolean.FALSE);
			setFuncionarioSecundario(Boolean.FALSE);
			setFuncionarioTerceiro(Boolean.FALSE);
			setFuncionarioQuarto(Boolean.FALSE);
			setFuncionarioQuinto(Boolean.TRUE);
			if (expedicaoDiplomaLote) {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioQuintoVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				this.setFuncionarioQuintoVO(objFuncionario);
				setTituloFuncionarioQuinto(Constantes.EMPTY);
				setCargoFuncionarioQuinto(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioQuintoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (objFuncionario.getMatricula().equals(Constantes.EMPTY)) {
					setMensagemDetalhada("Funcionário não encontrado.");
				}
				getExpedicaoDiplomaVO().setFuncionarioQuintoVO(objFuncionario);
				getExpedicaoDiplomaVO().setTituloFuncionarioQuinto(Constantes.EMPTY);
				getExpedicaoDiplomaVO().setCargoFuncionarioQuintoVO(null);
				montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFuncionarioQuintoVO(new FuncionarioVO());
			getExpedicaoDiplomaVO().setFuncionarioQuintoVO(new FuncionarioVO());
			getListaCargoFuncionarioQuinto().clear();
		}
	}	

	@SuppressWarnings("rawtypes")
	public List getListaCargoFuncionarioQuarto() {
		if (listaCargoFuncionarioQuarto == null) {
			listaCargoFuncionarioQuarto = new ArrayList(0);
		}
		return listaCargoFuncionarioQuarto;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoFuncionarioQuarto(List listaCargoFuncionarioQuarto) {
		this.listaCargoFuncionarioQuarto = listaCargoFuncionarioQuarto;
	}

	@SuppressWarnings("rawtypes")
	public List getListaCargoFuncionarioQuinto() {
		if (listaCargoFuncionarioQuinto == null) {
			listaCargoFuncionarioQuinto = new ArrayList(0);
		}
		return listaCargoFuncionarioQuinto;
	}

	@SuppressWarnings("rawtypes")
	public void setListaCargoFuncionarioQuinto(List listaCargoFuncionarioQuinto) {
		this.listaCargoFuncionarioQuinto = listaCargoFuncionarioQuinto;
	}

	public Boolean getFuncionarioQuarto() {
		if (funcionarioQuarto == null) {
			funcionarioQuarto = false;
		}
		return funcionarioQuarto;
	}

	public void setFuncionarioQuarto(Boolean funcionarioQuarto) {
		this.funcionarioQuarto = funcionarioQuarto;
	}

	public Boolean getFuncionarioQuinto() {
		if (funcionarioQuinto == null) {
			funcionarioQuinto = false;
		}
		return funcionarioQuinto;
	}

	public void setFuncionarioQuinto(Boolean funcionarioQuinto) {
		this.funcionarioQuinto = funcionarioQuinto;
	}

	public FuncionarioVO getFuncionarioQuartoVO() {
		if (funcionarioQuartoVO == null) {
			funcionarioQuartoVO = new FuncionarioVO();
		}
		return funcionarioQuartoVO;
	}

	public void setFuncionarioQuartoVO(FuncionarioVO funcionarioQuartoVO) {
		this.funcionarioQuartoVO = funcionarioQuartoVO;
	}

	public FuncionarioVO getFuncionarioQuintoVO() {
		if (funcionarioQuintoVO == null) {
			funcionarioQuintoVO = new FuncionarioVO();
		}
		return funcionarioQuintoVO;
	}

	public void setFuncionarioQuintoVO(FuncionarioVO funcionarioQuintoVO) {
		this.funcionarioQuintoVO = funcionarioQuintoVO;
	}

	public CargoVO getCargoFuncionarioQuarto() {
		if (cargoFuncionarioQuarto == null) {
			cargoFuncionarioQuarto = new CargoVO();
		}
		return cargoFuncionarioQuarto;
	}

	public void setCargoFuncionarioQuarto(CargoVO cargoFuncionarioQuarto) {
		this.cargoFuncionarioQuarto = cargoFuncionarioQuarto;
	}

	public CargoVO getCargoFuncionarioQuinto() {
		if (cargoFuncionarioQuinto == null) {
			cargoFuncionarioQuinto = new CargoVO();
		}
		return cargoFuncionarioQuinto;
	}

	public void setCargoFuncionarioQuinto(CargoVO cargoFuncionarioQuinto) {
		this.cargoFuncionarioQuinto = cargoFuncionarioQuinto;
	}

	public String getTituloFuncionarioQuarto() {
		if (tituloFuncionarioQuarto == null) {
			tituloFuncionarioQuarto = Constantes.EMPTY;
		}
		return tituloFuncionarioQuarto;
	}

	public void setTituloFuncionarioQuarto(String tituloFuncionarioQuarto) {
		this.tituloFuncionarioQuarto = tituloFuncionarioQuarto;
	}

	public String getTituloFuncionarioQuinto() {
		if (tituloFuncionarioQuinto == null) {
			tituloFuncionarioQuinto = Constantes.EMPTY;
		}
		return tituloFuncionarioQuinto;
	}

	public void setTituloFuncionarioQuinto(String tituloFuncionarioQuinto) {
		this.tituloFuncionarioQuinto = tituloFuncionarioQuinto;
	}
	
	public void limparDadosFuncionarioQuarto() throws Exception {
		setFuncionarioQuartoVO(new FuncionarioVO());
		setListaConsultaFuncionario(new ArrayList<>(0));
		setListaCargoFuncionarioQuarto(new ArrayList<>(0));
		getExpedicaoDiplomaVO().setFuncionarioQuartoVO(new FuncionarioVO());
		getExpedicaoDiplomaVO().setCargoFuncionarioQuartoVO(new CargoVO());
		getExpedicaoDiplomaVO().setTituloFuncionarioQuarto(Constantes.EMPTY);
		setTituloFuncionarioQuarto(Constantes.EMPTY);
	}
	
	public void limparDadosFuncionarioQuinto() throws Exception {
		setFuncionarioQuintoVO(new FuncionarioVO());
		setListaConsultaFuncionario(new ArrayList<>(0));
		setListaCargoFuncionarioQuinto(new ArrayList<>(0));
		getExpedicaoDiplomaVO().setFuncionarioQuintoVO(new FuncionarioVO());
		getExpedicaoDiplomaVO().setCargoFuncionarioQuintoVO(new CargoVO());
		getExpedicaoDiplomaVO().setTituloFuncionarioQuinto(Constantes.EMPTY);
		setTituloFuncionarioQuinto(Constantes.EMPTY);
	}
	
	public void limparDadosGerarXmlDiploma() throws Exception {
		if (getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula())) {
				getExpedicaoDiplomaVO().setConfiguracaoDiplomaDigital(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultarConfiguracaoExistente(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
				montarDadosFuncionarioXmlDiploma(Boolean.TRUE);
			}
		}
		getExpedicaoDiplomaVO().setSituacaoApresentar(null);
	}	
	
	public void limparDadosInformarCamposLivroRegistradora() {
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO()) && !getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica()) {
			getExpedicaoDiplomaVO().setInformarCamposLivroRegistradora(Boolean.FALSE);
			getExpedicaoDiplomaVO().setLivroRegistradora(null);
			getExpedicaoDiplomaVO().setFolhaReciboRegistradora(null);
		}
	}
	
	public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}
	
	public void consultarFuncionarioPrincipal() throws Exception {
		try {
			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(consultarFuncionarioPorMatricula(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getMatricula()));
			montarComboCargoFuncionario(getFuncionarioPrincipalVO().getFuncionarioCargoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioSecundario() throws Exception {
		try {
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(consultarFuncionarioPorMatricula(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getMatricula()));
			montarComboCargoFuncionario(getFuncionarioSecundarioVO().getFuncionarioCargoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioTerciario() throws Exception {
		try {
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(consultarFuncionarioPorMatricula(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getMatricula()));
			montarComboCargoFuncionario(getFuncionarioTerceiroVO().getFuncionarioCargoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioQuarto() throws Exception {
		try {
			getExpedicaoDiplomaVO().setFuncionarioQuartoVO(consultarFuncionarioPorMatricula(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getMatricula()));
			montarComboCargoFuncionario(getFuncionarioQuartoVO().getFuncionarioCargoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioQuinto() throws Exception {
		try {
			getExpedicaoDiplomaVO().setFuncionarioQuintoVO(consultarFuncionarioPorMatricula(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getMatricula()));
			montarComboCargoFuncionario(getFuncionarioQuintoVO().getFuncionarioCargoVOs());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void limparDadosAssinarDigitalmente() throws Exception {
		if (!isAssinarDigitalmente()) {
			getExpedicaoDiplomaVO().setGerarXMLDiploma(Boolean.FALSE);
		}
	}

	public Boolean getFuncionarioSecundario() {
		if (funcionarioSecundario == null) {
			funcionarioSecundario = false;
		}
		return funcionarioSecundario;
	}

	public void setFuncionarioSecundario(Boolean funcionarioSecundario) {
		this.funcionarioSecundario = funcionarioSecundario;
	}
	
	public void consultarDocumentosAssinados() {
		try {
			DocumentoAssinadoVO obj = new DocumentoAssinadoVO();
			obj.setMatricula(getExpedicaoDiplomaVO().getMatricula());
			obj.setExpedicaoDiplomaVO(getExpedicaoDiplomaVO());
			List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
			tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
			tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
			tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL);
			tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL);
			setListaDocumentoAsssinados(getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(obj, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), tipoOrigemDocumentoAssinadoEnums, null));
			Ordenacao.ordenarListaDecrescente(getListaDocumentoAsssinados(), "codigo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void rejeitarDocumentoAssinadoProvedorAssinatura() {
		try {
			DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO = new DocumentoAssinadoPessoaVO();
			documentoAssinadoPessoaVO.setDataRejeicao(new Date());
			if (!Uteis.isAtributoPreenchido(getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura())) {
				throw new Exception("O campo motivo rejeição diplomas assinados digitalmente pendentes deve ser preenchido");
			}
			documentoAssinadoPessoaVO.setMotivoRejeicao(getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura());
			documentoAssinadoPessoaVO.setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.REJEITADO);
			List<DocumentoAssinadoVO> documentoAssinadoVOs = new ArrayList<DocumentoAssinadoVO>();
			documentoAssinadoVOs.add(getDocumentoAssinadoRejeicao());
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarDadosRejeicaoDocumentosAssinados(documentoAssinadoPessoaVO, documentoAssinadoVOs);
			getDocumentoAssinadoRejeicao().setDocumentoAssinadoInvalido(Boolean.TRUE);
			getDocumentoAssinadoRejeicao().setMotivoDocumentoAssinadoInvalido(getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura());
			getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(getDocumentoAssinadoRejeicao().getCodigo(), getDocumentoAssinadoRejeicao().isDocumentoAssinadoInvalido(), getDocumentoAssinadoRejeicao().getMotivoDocumentoAssinadoInvalido(), getUsuarioLogado());
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setDocumentoAssinadoRejeicao(null);
			setOnCompleteRejeicaoDocumento(Constantes.EMPTY);
		}
	}

	public String getOnCompleteRejeicaoDocumento() {
		if (onCompleteRejeicaoDocumento == null) {
			onCompleteRejeicaoDocumento = Constantes.EMPTY;
		}
		return onCompleteRejeicaoDocumento;
	}

	public void setOnCompleteRejeicaoDocumento(String onCompleteRejeicaoDocumento) {
		this.onCompleteRejeicaoDocumento = onCompleteRejeicaoDocumento;
	}

	public String getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura() {
		if (motivoRejeicaoDocumentoAssinadoProvedorAssinatura == null) {
			motivoRejeicaoDocumentoAssinadoProvedorAssinatura = Constantes.EMPTY;
		}
		return motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	}

	public void setMotivoRejeicaoDocumentoAssinadoProvedorAssinatura(
			String motivoRejeicaoDocumentoAssinadoProvedorAssinatura) {
		this.motivoRejeicaoDocumentoAssinadoProvedorAssinatura = motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoRejeicao() {
		if (documentoAssinadoRejeicao == null) {
			documentoAssinadoRejeicao = new DocumentoAssinadoVO();
		}
		return documentoAssinadoRejeicao;
	}

	public void setDocumentoAssinadoRejeicao(DocumentoAssinadoVO documentoAssinadoRejeicao) {
		this.documentoAssinadoRejeicao = documentoAssinadoRejeicao;
	}
	
	public void realizarGeracaoXmlDocumentacao() throws Exception {
		try {
			getListaMensagensErro().clear();
			if (isAssinarDigitalmente()) {
				if (!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora())) {
					throw new Exception("O campo Unidade Ensino Expedição Diploma deve ser informado.");
				} else {
					if (getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getNovoObj()) {
						getExpedicaoDiplomaVO().setUnidadeEnsinoCertificadora(getAplicacaoControle().getUnidadeEnsinoVO(getExpedicaoDiplomaVO().getUnidadeEnsinoCertificadora().getCodigo(), getUsuarioLogado()));
					}
				}
				verificarDadosCargosFuncionariosMontados(Boolean.FALSE);
				getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoDocumentacaoAcademica(getExpedicaoDiplomaVO(), getSuperControleRelatorio(), getUsuarioLogado());
			}
			setMensagemID("Documento digital gerado com sucesso", Uteis.SUCESSO, Boolean.TRUE);
			setModalErro(Constantes.EMPTY);
		} catch (ConsistirException ce) {
			setImprimirContrato(Boolean.FALSE);
			setFazerDownload(Boolean.FALSE);
			setAbrirModalOK(Boolean.FALSE);
			if (!ce.getListaMensagemErro().isEmpty()) {
				getListaMensagensErro().addAll(ce.getListaMensagemErro());
				setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
				setModalErro("RichFaces.$('panelAvisoAssinaturaDigital').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
			} else {
				getListaMensagensErro().add(ce.getMessage());
			}
			if (getLancarExcecao()) {
				throw ce;
			}
		}
		catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setModalErro(Constantes.EMPTY);
		}
	}

	public Boolean getGerarXmlDiplomaLote() {
		if (gerarXmlDiplomaLote == null) {
			gerarXmlDiplomaLote = false;
		}
		return gerarXmlDiplomaLote;
	}

	public void setGerarXmlDiplomaLote(Boolean gerarXmlDiplomaLote) {
		this.gerarXmlDiplomaLote = gerarXmlDiplomaLote;
	}

	public Date getDataPublicacaoDiarioOficialLote() {
		return dataPublicacaoDiarioOficialLote;
	}

	public void setDataPublicacaoDiarioOficialLote(Date dataPublicacaoDiarioOficialLote) {
		this.dataPublicacaoDiarioOficialLote = dataPublicacaoDiarioOficialLote;
	}	
	
	public Date getDataRegistroDiplomaLote() {
		return dataRegistroDiplomaLote;
	}
	
	public void setDataRegistroDiplomaLote(Date dataRegistroDiplomaLote) {
		this.dataRegistroDiplomaLote = dataRegistroDiplomaLote;
	}
	
	public void consultarUltimoDocumentoAssinadoDiplomaDigital() throws Exception {
		DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
		documentoAssinadoVO.setMatricula(getExpedicaoDiplomaVO().getMatricula());
		List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
		tipoOrigemDocumentoAssinadoEnums.add(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
		List<DocumentoAssinadoVO> documentoAssinados = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentosAssinadoPorRelatorio(documentoAssinadoVO, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), tipoOrigemDocumentoAssinadoEnums, null);
		Ordenacao.ordenarListaDecrescente(documentoAssinados, "codigo");
		getExpedicaoDiplomaVO().setDocumentoAssinadoVO(documentoAssinados.stream().findFirst().orElse(new DocumentoAssinadoVO()));
	}

	public Boolean getLancarExcecao() {
		if (lancarExcecao == null) {
			lancarExcecao = false;
		}
		return lancarExcecao;
	}

	public void setLancarExcecao(Boolean lancarExcecao) {
		this.lancarExcecao = lancarExcecao;
	}
	
	public void selecionarDocumentoAssinadoRegerarPDF(DocumentoAssinadoVO documentoAssinadoVO) {
		setDocumentoAssinadoRegrarRepresentacaoVisual(documentoAssinadoVO);
	}

	public DocumentoAssinadoVO getDocumentoAssinadoRegrarRepresentacaoVisual() {
		if (documentoAssinadoRegrarRepresentacaoVisual == null) {
			documentoAssinadoRegrarRepresentacaoVisual = new DocumentoAssinadoVO();
		}
		return documentoAssinadoRegrarRepresentacaoVisual;
	}

	public void setDocumentoAssinadoRegrarRepresentacaoVisual(DocumentoAssinadoVO documentoAssinadoRegrarRepresentacaoVisual) {
		this.documentoAssinadoRegrarRepresentacaoVisual = documentoAssinadoRegrarRepresentacaoVisual;
	}
	
	public void realizarGeracaoRepresentacaoVisualDiplomaDigital() throws Exception {
		try {
			getListaMensagensErro().clear();
			ExpedicaoDiplomaVO expedicaoDiplomaVO = null;
			if (Uteis.isAtributoPreenchido(getDocumentoAssinadoRegrarRepresentacaoVisual().getExpedicaoDiplomaVO())) {
				expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().carregarDadosCompletoExpedicaoDiploma(getDocumentoAssinadoRegrarRepresentacaoVisual().getExpedicaoDiplomaVO().getCodigo(), getUsuarioLogado());
			} else {
				expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(getDocumentoAssinadoRegrarRepresentacaoVisual().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoRepresentacaoVisualDiplomaDigital(expedicaoDiplomaVO);
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getCodigo())) {
				getDocumentoAssinadoRegrarRepresentacaoVisual().setArquivoVisual(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual());
				getFacadeFactory().getDocumentoAssinadoFacade().alterarArquivoVisualDocumentoAssinado(getDocumentoAssinadoRegrarRepresentacaoVisual(),false, getUsuario());
			}
			consultarDocumentosAssinados();
//			setMensagemID("msg_dados_gravados");
			setModalErro(Constantes.EMPTY);
		} catch (ConsistirException ce) {
			if (!ce.getListaMensagemErro().isEmpty() && getListaMensagensErro().isEmpty()) {
				getListaMensagensErro().addAll(ce.getListaMensagemErro());
				setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
				setModalErro("RichFaces.$('panelAvisoRegerarPDF').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
			} else if (!getListaMensagensErro().isEmpty()) { 
				setMensagemDetalhada("msg_erro", "Não Foi Possível Concluir Essa Operação", Uteis.ERRO);
				setModalErro("RichFaces.$('panelAvisoRegerarPDF').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
			} else {
				getListaMensagensErro().add(ce.getMessage());
				setModalErro("RichFaces.$('panelAvisoRegerarPDF').hide(); RichFaces.$('panelListaErroGeracaoDiploma').show();");
			}
			if (getLancarExcecao()) {
				throw ce;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	
			setModalErro(Constantes.EMPTY);
		} finally {
			setDocumentoAssinadoRegrarRepresentacaoVisual(null);
		}
	}

	public void limparDocumentoAssinadoRegrarRepresentacaoVisual() {
		setDocumentoAssinadoRegrarRepresentacaoVisual(null);
	}
		
	public void regerarCodigoValidacaoDiploma() {
//		try {
//			if (getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
//				String hashCodigoValidacao = getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoHashCodigoValidacao(getExpedicaoDiplomaVO(), getExpedicaoDiplomaVO().getLivroRegistroDiplomaVO().getNrLivro().toString().trim(), getExpedicaoDiplomaVO().getLivroRegistroDiplomaVO().getNrFolhaRecibo().toString().trim(), getExpedicaoDiplomaVO().getNumeroRegistroDiploma().trim(), getUsuarioLogado());
//		        if (Uteis.isAtributoPreenchido(hashCodigoValidacao)) {
//		        	getExpedicaoDiplomaVO().setCodigoValidacaoDiplomaDigital(hashCodigoValidacao);
//		        } else {
//		        	getExpedicaoDiplomaVO().setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
//		        }
//			} else if (!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCodigoValidacaoDiplomaDigital())) {
//				getExpedicaoDiplomaVO().setCodigoValidacaoDiplomaDigital(Constantes.EMPTY);
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public Boolean getPossuiPermissaoRegerarCodigoValidacaoDiploma() {
		if (possuiPermissaoRegerarCodigoValidacaoDiploma == null) {
			possuiPermissaoRegerarCodigoValidacaoDiploma = false;
		}
		return possuiPermissaoRegerarCodigoValidacaoDiploma;
	}

	public void setPossuiPermissaoRegerarCodigoValidacaoDiploma(Boolean possuiPermissaoRegerarCodigoValidacaoDiploma) {
		this.possuiPermissaoRegerarCodigoValidacaoDiploma = possuiPermissaoRegerarCodigoValidacaoDiploma;
	}

	 @SuppressWarnings("unused")
	private void verificarPermissaoRegerarCodigoValidacaoDiploma() {
	    	try {
	    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("PermiteRegerarCodigoValidacaoDiploma", getUsuarioLogado());
	    		setPossuiPermissaoRegerarCodigoValidacaoDiploma(Boolean.TRUE);
			} catch (Exception e) {
				setPossuiPermissaoRegerarCodigoValidacaoDiploma(Boolean.FALSE);
			}
	    }

	public String getModalErro() {
		if (modalErro == null) {
			modalErro = Constantes.EMPTY;
		}
		return modalErro;
	}

	public void setModalErro(String modalErro) {
		this.modalErro = modalErro;
	}

	public List<String> getListaMensagensErro() {
		if (listaMensagensErro == null) {
			listaMensagensErro = new ArrayList<String>();
		}
		return listaMensagensErro;
	}

	public void setListaMensagensErro(List<String> listaMensagensErro) {
		this.listaMensagensErro = listaMensagensErro;
	}
	 
	public void verificarConfiguracaoDiplomaExistente(Boolean montarDados) throws Exception {
		if (isExpedicaoDiplomaGraduacao()) {
			getExpedicaoDiplomaVO().setConfiguracaoDiplomaDigital(getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().consultarConfiguracaoExistente(getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			if (montarDados && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital())) {
				montarDadosAPartitConfiguracaoDiplomaDigital();
			}
		}
	}
	
	public void montarDadosAPartitConfiguracaoDiplomaDigital() throws Exception {
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital())) {
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao())) {
				getExpedicaoDiplomaVO().setUnidadeEnsinoCertificadora(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getUnidadeEnsinoPadrao());
			}
			if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())) {
				if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoPadrao())) {
					setTipoLayout(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoPadrao());
					if (getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoPadrao().equals("TextoPadrao")) {
						setTextoPadraoDeclaracao(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTextoPadraoGraduacaoPadrao().getCodigo());
					}
				}
			} else if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
				if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoTecnologicaPadrao())) {
					setTipoLayout(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoTecnologicaPadrao());
					if (getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getLayoutGraduacaoTecnologicaPadrao().equals("TextoPadrao")) {
						setTextoPadraoDeclaracao(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTextoPadraoGraduacaoTecnologicaPadrao().getCodigo());
					}
				}
			}
			montarListaSelectItemTipoTextoPadrao();
			getExpedicaoDiplomaVO().setVersaoDiploma(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getVersao());
			montarDadosFuncionarioXmlDiploma(Boolean.FALSE);
		}
	}
	
	public void montarDadosFuncionarioXmlDiploma(Boolean validarFuncionarioPreenchido) throws Exception {
		if (((validarFuncionarioPreenchido && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO())) || !validarFuncionarioPreenchido) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioPrimario())) {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioPrimario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getExpedicaoDiplomaVO().setFuncionarioPrimarioVO(obj);
			getExpedicaoDiplomaVO().setCargoFuncionarioPrincipalVO(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getCargoFuncionarioPrimario());
			getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTituloFuncionarioPrimario());
			montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getFuncionarioCargoVOs(), "F1");
		}
		if (((validarFuncionarioPreenchido && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO())) || !validarFuncionarioPreenchido) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioSecundario())) {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioSecundario().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getExpedicaoDiplomaVO().setFuncionarioSecundarioVO(obj);
			getExpedicaoDiplomaVO().setCargoFuncionarioSecundarioVO(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getCargoFuncionarioSecundario());
			getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTituloFuncionarioSecundario());
			montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getFuncionarioCargoVOs(), "F2");
		}
		if (((validarFuncionarioPreenchido && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO())) || !validarFuncionarioPreenchido) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioTerceiro())) {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioTerceiro().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getExpedicaoDiplomaVO().setFuncionarioTerceiroVO(obj);
			getExpedicaoDiplomaVO().setCargoFuncionarioTerceiroVO(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getCargoFuncionarioTerceiro());
			getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTituloFuncionarioTerceiro());
			montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getFuncionarioCargoVOs(), "F3");
		}
		if (((validarFuncionarioPreenchido && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioQuartoVO())) || !validarFuncionarioPreenchido) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioQuarto())) {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioQuarto().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getExpedicaoDiplomaVO().setFuncionarioQuartoVO(obj);
			getExpedicaoDiplomaVO().setCargoFuncionarioQuartoVO(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getCargoFuncionarioQuarto());
			getExpedicaoDiplomaVO().setTituloFuncionarioQuarto(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTituloFuncionarioQuarto());
			montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getFuncionarioCargoVOs(), "F4");
		}
		if (((validarFuncionarioPreenchido && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getFuncionarioQuintoVO())) || !validarFuncionarioPreenchido) && Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioQuinto())) {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getFuncionarioQuinto().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getExpedicaoDiplomaVO().setFuncionarioQuintoVO(obj);
			getExpedicaoDiplomaVO().setCargoFuncionarioQuintoVO(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getCargoFuncionarioQuinto());
			getExpedicaoDiplomaVO().setTituloFuncionarioQuinto(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital().getTituloFuncionarioQuinto());
			montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getFuncionarioCargoVOs(), "F5");
		}
	}
	
	public void montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos, String funcionario) throws Exception {
		List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
		for (FuncionarioCargoVO obj : cargos) {
			if (!selectItems.stream().anyMatch(s -> s.getValue().equals(obj.getCargo().getCodigo()))) {
				selectItems.add(new SelectItem(obj.getCargo().getCodigo(), obj.getCargo().getNome()));
			}
		}
		if (funcionario.equals("F1")) {
			setListaCargoFuncionarioPrincipal(selectItems);
		} else if (funcionario.equals("F3")) {
			setListaCargoFuncionarioTerceiro(selectItems);
		} else if (funcionario.equals("F4")) {
			setListaCargoFuncionarioQuarto(selectItems);
		} else if (funcionario.equals("F5")) {
			setListaCargoFuncionarioQuinto(selectItems);
		} else {
			setListaCargoFuncionarioSecundario(selectItems);
		}
	}
	
	public void montarDadosCoordenadorCurso() throws Exception {
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getConfiguracaoDiplomaDigital()) && (!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCursoCoordenadorVO()))) {
			List<CursoCoordenadorVO> coordenadorVOs = getFacadeFactory().getCursoCoordenadorFacade().consultarPorPessoaUnidadeEnsinoNivelEducacionalCurso(0, getExpedicaoDiplomaVO().getMatricula().getUnidadeEnsino().getCodigo(), Constantes.EMPTY, getExpedicaoDiplomaVO().getMatricula().getCurso().getCodigo(), 0, true, true, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(coordenadorVOs) && coordenadorVOs.stream().anyMatch(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino()))) {
				getExpedicaoDiplomaVO().setCursoCoordenadorVO(coordenadorVOs.stream().filter(coordenador -> Uteis.isAtributoPreenchido(coordenador.getUnidadeEnsino())).findFirst().get());
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(getExpedicaoDiplomaVO().getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), getUsuarioLogado(), 1);
				if (!formacaoAcademica.isEmpty()) {
					getExpedicaoDiplomaVO().setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			} else if (Uteis.isAtributoPreenchido(coordenadorVOs)) {
				getExpedicaoDiplomaVO().setCursoCoordenadorVO(coordenadorVOs.get(0));
				List<FormacaoAcademicaVO> formacaoAcademica = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(getExpedicaoDiplomaVO().getCursoCoordenadorVO().getFuncionario().getPessoa().getCodigo(), getUsuarioLogado(), 1);
				if (!formacaoAcademica.isEmpty()) {
					getExpedicaoDiplomaVO().setFormacaoAcademicaVO(formacaoAcademica.get(0));
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List getListaSelectItemTipoAutorizacaoCurso() {
		if (listaSelectItemTipoAutorizacaoCurso == null) {
			listaSelectItemTipoAutorizacaoCurso = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAutorizacaoCurso.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
			for (TipoAutorizacaoEnum enumerador : TipoAutorizacaoEnum.values()) {
				listaSelectItemTipoAutorizacaoCurso.add(new SelectItem(enumerador, enumerador.getValorApresentar()));
			}
		}
		return listaSelectItemTipoAutorizacaoCurso;
	}
	
	public void carregarEnderecoIesPta() {
		try {
			UnidadeEnsinoVO ensinoVO = new UnidadeEnsinoVO();
			ensinoVO.setCEP(getExpedicaoDiplomaVO().getCepPTA());
			getFacadeFactory().getEnderecoFacade().carregarEndereco(ensinoVO, getUsuarioLogado());
			getExpedicaoDiplomaVO().setCepPTA(ensinoVO.getCEP());
			getExpedicaoDiplomaVO().setLogradouroPTA(ensinoVO.getEndereco());
			getExpedicaoDiplomaVO().setBairroPTA(ensinoVO.getSetor());
			getExpedicaoDiplomaVO().setCidadePTA(ensinoVO.getCidade());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCidade() {
		try {
			getFacadeFactory().getCidadeFacade().consultarCidade(getControleConsultaCidade(), false, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarCidadePTA() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItems");
		getExpedicaoDiplomaVO().setCidadePTA(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade(Constantes.EMPTY);
		this.setCampoConsultaCidade(Constantes.EMPTY);
	}
	
	public List<CidadeVO> getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>();
		}
		return listaConsultaCidade;
	}
	
	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}
	
	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = Constantes.EMPTY;
		}
		return valorConsultaCidade;
	}
	
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}
	
	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = Constantes.EMPTY;
		}
		return campoConsultaCidade;
	}
	
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}
	
	public DeclaracaoAcercaProcessoJudicialVO getDeclaracaoAcercaProcessoJudicialVO() {
		if (declaracaoAcercaProcessoJudicialVO == null) {
			declaracaoAcercaProcessoJudicialVO = new DeclaracaoAcercaProcessoJudicialVO();
		}
		return declaracaoAcercaProcessoJudicialVO;
	}
	
	public void setDeclaracaoAcercaProcessoJudicialVO(DeclaracaoAcercaProcessoJudicialVO declaracaoAcercaProcessoJudicialVO) {
		this.declaracaoAcercaProcessoJudicialVO = declaracaoAcercaProcessoJudicialVO;
	}
	
	public void adicionarDeclaracaoAcercaProcessoJudicial() {
		try {
			getDeclaracaoAcercaProcessoJudicialVO().setExpedicaoDiploma(getExpedicaoDiplomaVO());
			if (Uteis.isAtributoPreenchido(getDeclaracaoAcercaProcessoJudicialVO())) {
				UtilReflexao.adicionarObjetoLista(getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs(), getDeclaracaoAcercaProcessoJudicialVO(), new AtributoComparacao().add("codigo", getDeclaracaoAcercaProcessoJudicialVO().getCodigo()));
			} else {
				getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().add(getDeclaracaoAcercaProcessoJudicialVO());
			}
			setDeclaracaoAcercaProcessoJudicialVO(new DeclaracaoAcercaProcessoJudicialVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void excluirDeclaracao() {
		try {
			getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().excluir(getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getSelecionarDeclaracao() {
		if (selecionarDeclaracao == null) {
			selecionarDeclaracao = Boolean.FALSE;
		}
		return selecionarDeclaracao;
	}
	
	public void setSelecionarDeclaracao(Boolean selecionarDeclaracao) {
		this.selecionarDeclaracao = selecionarDeclaracao;
	}
	
	public void selecionarTodasDeclaracao() {
		getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().stream().forEach(d -> d.setSelecionado(getSelecionarDeclaracao()));
	}
	
	public Boolean getApresentarBotaoExcluirDeclaracao() {
		return (!getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().isEmpty()) && getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().stream().anyMatch(d -> d.getSelecionado());
	}
	
	public void verificarDeclaracoesSelecionadas() {
		if (getExpedicaoDiplomaVO().getDeclaracaoAcercaProcessoJudicialVOs().stream().allMatch(d -> d.getSelecionado())) {
			setSelecionarDeclaracao(Boolean.TRUE);
		} else {
			setSelecionarDeclaracao(Boolean.FALSE);
		}
	}
	
	public void editarDeclaracao() {
		DeclaracaoAcercaProcessoJudicialVO obj = (DeclaracaoAcercaProcessoJudicialVO) context().getExternalContext().getRequestMap().get("declaracaoItesn");
		setDeclaracaoAcercaProcessoJudicialVO(obj);
	}
	
	public List<SelectItem> getListaSelectItemVersaoDiploma() {
		if (listaSelectItemVersaoDiploma == null) {
			listaSelectItemVersaoDiploma = new ArrayList<SelectItem>(0);
			for (VersaoDiplomaDigitalEnum obj : VersaoDiplomaDigitalEnum.values()) {
				listaSelectItemVersaoDiploma.add(new SelectItem(obj, obj.getDescricao()));
			}
		}
		return listaSelectItemVersaoDiploma;
	}
	
	public void setListaSelectItemVersaoDiploma(List<SelectItem> listaSelectItemVersaoDiploma) {
		this.listaSelectItemVersaoDiploma = listaSelectItemVersaoDiploma;
	}
	
	public VersaoDiplomaDigitalEnum getVersaoDiplomaLote() {
		if (versaoDiplomaLote == null) {
			versaoDiplomaLote = VersaoDiplomaDigitalEnum.VERSAO_1_05;
		}
		return versaoDiplomaLote;
	}
	
	public void setVersaoDiplomaLote(VersaoDiplomaDigitalEnum versaoDiplomaLote) {
		this.versaoDiplomaLote = versaoDiplomaLote;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoCertificadoraLote() {
		if (unidadeEnsinoCertificadoraLote == null) {
			unidadeEnsinoCertificadoraLote = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoCertificadoraLote;
	}
	
	public void setUnidadeEnsinoCertificadoraLote(UnidadeEnsinoVO unidadeEnsinoCertificadoraLote) {
		this.unidadeEnsinoCertificadoraLote = unidadeEnsinoCertificadoraLote;
	}
	
	@SuppressWarnings("rawtypes")
	public void montarListaSelectItemUnidadeEnsinoLote() throws Exception {
		try {
			setUnidadeEnsinoCertificadoraLote(new UnidadeEnsinoVO());
			if (getPermissaoAlterarUnidadeEnsinoCertificadora()) {
				montarListaSelectItemUnidadeEnsino(Constantes.EMPTY);
			} else {
				List<UnidadeEnsinoVO> resultadoConsulta = new ArrayList<>(0);
				Iterator i = null;
				for (ProgramacaoFormaturaUnidadeEnsinoVO un : getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs()) {
					resultadoConsulta.add(un.getUnidadeEnsinoVO());
				} 
				UnidadeEnsinoVO unidadeEnsinoPrincipal = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(false, false, 0, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(unidadeEnsinoPrincipal)) {
					if (!resultadoConsulta.stream().anyMatch(u -> u.getCodigo().equals(unidadeEnsinoPrincipal.getCodigo()))) {
						resultadoConsulta.add(unidadeEnsinoPrincipal);
					}
				}
				i = resultadoConsulta.iterator();
				List<SelectItem> objs = new ArrayList<>(0);
				while (i.hasNext()) {
					UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				setListaSelectItemUnidadeEnsino(objs);
			}
			setMensagemID(Constantes.EMPTY);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public Integer getCargaHorariaTotal() {
		return cargaHorariaTotal;
	}

	public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	public Integer getCargaHorariaCursada() {
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}
	
	public String getMotivoRejeicaoDiploma() {
		if (motivoRejeicaoDiploma == null) {
			motivoRejeicaoDiploma = Constantes.EMPTY;
		}
		return motivoRejeicaoDiploma;
	}
	
	public void setMotivoRejeicaoDiploma(String motivoRejeicaoDiploma) {
		this.motivoRejeicaoDiploma = motivoRejeicaoDiploma;
	}
	
	public void limparDadosDiplomaLote() {
		if (!isAssinarDigitalmente()) {
			setGerarXmlDiplomaLote(Boolean.FALSE);
			setFuncionarioQuartoVO(new FuncionarioVO());
			setFuncionarioQuintoVO(new FuncionarioVO());
			setCargoFuncionarioQuarto(new CargoVO());
			setCargoFuncionarioQuinto(new CargoVO());
			setTituloFuncionarioQuarto(Constantes.EMPTY);
			setTituloFuncionarioQuinto(Constantes.EMPTY);
		} else if (isAssinarDigitalmente() && !getGerarXmlDiplomaLote()) {
			setFuncionarioQuartoVO(new FuncionarioVO());
			setFuncionarioQuintoVO(new FuncionarioVO());
			setCargoFuncionarioQuarto(new CargoVO());
			setCargoFuncionarioQuinto(new CargoVO());
			setTituloFuncionarioQuarto(Constantes.EMPTY);
			setTituloFuncionarioQuinto(Constantes.EMPTY);
		}
		verificarAlunosSelecionadosListaExpedicaoDiploma();
	}
	
	public void limparDadosConsultaProgramacaoFormatura() {
		setCampoConsultaProgramacaoFormatura("codigo");
		setValorConsultaProgramacaoFormatura(Constantes.EMPTY);
		setValorConsultaDataInicioProgramacaoFormatura(null);
		setTrazerAlunosComDiplomaEmitido(Boolean.FALSE);
		getListaConsultaProgramacaoFormatura().clear();
		mostrarSegundoCampoProgramacaoFormatura();
	}
	
	
	public Boolean getApresentarAnularExpedicaoDiploma() {
		if (apresentarAnularExpedicaoDiploma == null) {
			try {
				if (ControleAcesso.verificarPermissaoFuncionalidadeUsuario(PerfilAcessoPermissaoAcademicoEnum.PERMITE_REGISTRAR_ANULACAO_DIPLOMA.getValor(), getUsuarioLogado())) {
					apresentarAnularExpedicaoDiploma = Boolean.TRUE;
				} else {
					apresentarAnularExpedicaoDiploma = Boolean.FALSE;
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return apresentarAnularExpedicaoDiploma;
	}
	
	public void setApresentarAnularExpedicaoDiploma(Boolean apresentarAnularExpedicaoDiploma) {
		this.apresentarAnularExpedicaoDiploma = apresentarAnularExpedicaoDiploma;
	}
	
	public List<SelectItem> getListaSelectItemMotivoAnulacao() {
		if (listaSelectItemMotivoAnulacao == null) {
			listaSelectItemMotivoAnulacao = new ArrayList<>(0);
			for (TMotivoAnulacao motivo : TMotivoAnulacao.values()) {
				listaSelectItemMotivoAnulacao.add(new SelectItem(motivo, motivo.value()));
			}
		}
		return listaSelectItemMotivoAnulacao;
	}
	
	public void setListaSelectItemMotivoAnulacao(List<SelectItem> listaSelectItemMotivoAnulacao) {
		this.listaSelectItemMotivoAnulacao = listaSelectItemMotivoAnulacao;
	}
	
	public void registrarAnulacaoDiploma() {
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarAnulacaoDiploma(getExpedicaoDiplomaVO(), getUsuarioLogado());
			setMensagemID("Expedição Diploma anulado.", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void estornarAnulacaoDiploma() {
		try {
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarEstornoAnulacaoDiploma(getExpedicaoDiplomaVO(), getUsuarioLogado());
			setMensagemID("Estorno anulação diploma realizado.", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public ControleConsultaExpedicaoDiploma getControleConsultaExpedicaoDiploma() {
		if (controleConsultaExpedicaoDiploma == null) {
			controleConsultaExpedicaoDiploma = new ControleConsultaExpedicaoDiploma();
		}
		return controleConsultaExpedicaoDiploma;
	}
	
	public void setControleConsultaExpedicaoDiploma(ControleConsultaExpedicaoDiploma controleConsultaExpedicaoDiploma) {
		this.controleConsultaExpedicaoDiploma = controleConsultaExpedicaoDiploma;
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaExpedicaoDiploma().getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaExpedicaoDiploma().getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}
	
	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
	
	public void realizarMontagemDiplomaDigital() {
		ExpedicaoDiplomaVO obj = (ExpedicaoDiplomaVO) context().getExternalContext().getRequestMap().get("expedicaoDiplomaItens");
		try {
			consultarLayoutListaDocumentoAssinadoPadrao();
			if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getDiplomaDigital())) {
				setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getDiplomaDigital().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarMontagemDocumentacaoAcademica() {
		ExpedicaoDiplomaVO obj = (ExpedicaoDiplomaVO) context().getExternalContext().getRequestMap().get("expedicaoDiplomaItens");
		try {
			consultarLayoutListaDocumentoAssinadoPadrao();
			if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getDocumentacaoAcademicaDigital())) {
				setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getDocumentacaoAcademicaDigital().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarMontagemHistoricoDigital() {
		ExpedicaoDiplomaVO obj = (ExpedicaoDiplomaVO) context().getExternalContext().getRequestMap().get("expedicaoDiplomaItens");
		try {
			consultarLayoutListaDocumentoAssinadoPadrao();
			if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getHistoricoDigital())) {
				setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(obj.getHistoricoDigital().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarDownloadDocumentoAssinado() {
		setOncompleteModal(Constantes.EMPTY);
		setCaminhoPreview(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoVO())) {
			try {
				if (getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
					realizarDownloadArquivo(getDocumentoAssinadoVO().getArquivo());
					setOncompleteModal(getUrlDownloadSV());
				} else {
					setCaminhoPreview(getFacadeFactory().getDocumentoAssinadoFacade().realizarGeracaoPreviewDocumento(getDocumentoAssinadoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
					setOncompleteModal("RichFaces.$('panelPreviewPdf').show()");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public void realizarDownloadRepresentacaoVisual() {
		setOncompleteModal(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(getDocumentoAssinadoVO())) {
			try {
				if (getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL) || getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
					setCaminhoPreview(getFacadeFactory().getDocumentoAssinadoFacade().realizarGeracaoPreviewRepresentacaoVisual(getDocumentoAssinadoVO(), getConfiguracaoGeralPadraoSistema()));
					setOncompleteModal("RichFaces.$('panelPreviewPdf').show()");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public void consultarLayoutListaDocumentoAssinadoPadrao() {
		try {
			LayoutPadraoVO layout = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("ExpedicaoDiploma", "listaDocumentoAssinado", Boolean.FALSE, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(layout)) {
				setVersaoLayoutListaNovo(Boolean.valueOf(layout.getValor()));
			} else {
				setVersaoLayoutListaNovo(Boolean.TRUE);
			}
		} catch (Exception e) {
			setVersaoLayoutListaNovo(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void fecharPanelPreview() {
		setOncompleteModal(Constantes.EMPTY);
		File file = new File(getCaminhoPreview());
		if (file.exists()) {
			file.delete();
		}
		setCaminhoPreview(Constantes.EMPTY);
	}
	
	private void montarDadosCampos() throws Exception {
		setObservacaoDiploma(getExpedicaoDiplomaVO().getMatricula().getObservacaoDiploma());
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getLayoutDiploma())) {
			setTipoLayout(getExpedicaoDiplomaVO().getLayoutDiploma());
		}
		setAbrirModalOK(Boolean.FALSE);
		if (getExpedicaoDiplomaVO().getGerarXMLDiploma()) {
			setAssinarDigitalmente(Boolean.TRUE);
		}
		
		realizarMontagemCargoFuncionario();
		inicializarDadosAssinaturaExpedicaoDiploma(getExpedicaoDiplomaVO());
	}
	
	public Boolean getVersaoLayoutListaNovo() {
		if (versaoLayoutListaNovo == null) {
			versaoLayoutListaNovo = Boolean.FALSE;
		}
		return versaoLayoutListaNovo;
	}
	
	public void setVersaoLayoutListaNovo(Boolean versaoLayoutListaNovo) {
		this.versaoLayoutListaNovo = versaoLayoutListaNovo;
	}
	
	public void persistirLayoutListaPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoLayoutListaNovo().toString(), "ExpedicaoDiploma", "listaDocumentoAssinado", getUsuarioLogado());
			setMensagemID("Layout padrão alterado com sucesso.", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getInformarCamposLivroRegistradoraLote() {
		if (informarCamposLivroRegistradoraLote == null) {
			informarCamposLivroRegistradoraLote = Boolean.FALSE;
		}
		return informarCamposLivroRegistradoraLote;
	}
	
	public void setInformarCamposLivroRegistradoraLote(Boolean informarCamposLivroRegistradoraLote) {
		this.informarCamposLivroRegistradoraLote = informarCamposLivroRegistradoraLote;
	}
	
	public void executarInicioProgressBarGeracaoDocumentacaoAcademicaLote() {
		try {
			setOncompleteModal("RichFaces.$('panelMotivoRejeicao').hide()");
			getListaExpedicaoDiplomaVOErros().clear();
			setOnCompleteDownloadExpedicaoDiplomaLote(Constantes.EMPTY);
			setFazerDownload(Boolean.FALSE);
			setCaminhoRelatorio(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado)) {
				setProgressBarVO(new ProgressBarVO());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoCertificadoraLote().clone());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList()).size()), "Carregando Expedição Diploma", true, this, "realizarGeracaoXmlDocumentacaoLote");
			} else {
				throw new ConsistirException("Não existem dados a serem gerados.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarGeracaoXmlDocumentacaoLote() {
		try {
			if (!(Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado))) {
				throw new ConsistirException("Não existem dados a serem gerados.");
			}
			if (Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs())) {
				List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList());
				Boolean gerarXMLDiploma = isAssinarDigitalmente() && getGerarXmlDiplomaLote();
				Date dataPublicacaoDO = getDataPublicacaoDiarioOficialLote();
				Date dataRegistroDiploma = getDataRegistroDiplomaLote();
				Date dataExpedicao = getDataExpedicaoDiplomaLote();
				int x = 1;
				if (!Uteis.isAtributoPreenchido(getProgressBarVO().getUnidadeEnsinoVO())) {
					throw new Exception("O campo Unidade Ensino Expedição Diploma deve ser informado.");
				} else {
					if (getProgressBarVO().getUnidadeEnsinoVO().getNovoObj()) {
						getProgressBarVO().setUnidadeEnsinoVO(getAplicacaoControle().getUnidadeEnsinoVO(getProgressBarVO().getUnidadeEnsinoVO().getCodigo(), getProgressBarVO().getUsuarioVO()));
					}
				}
				verificarDadosCargosFuncionariosMontados(Boolean.TRUE);
				for (ExpedicaoDiplomaVO expedicaoDiploma : expedicaoDiplomaVOs) {
					try {
						getProgressBarVO().setStatus("Processando Expedição Diploma " + x + " de " + (getProgressBarVO().getMaxValue()) + " ");
						getProgressBarVO().setProgresso(Long.valueOf(x));
						if (!Uteis.isAtributoPreenchido(expedicaoDiploma)) {
							expedicaoDiploma.setGerarXMLDiplomaLote(isAssinarDigitalmente() && gerarXMLDiploma);
						}
						if (getTipoLayout().equals("TextoPadrao")) {
							expedicaoDiploma.getTextoPadrao().setCodigo(getTextoPadraoDeclaracao());
						}
						expedicaoDiploma.setConsistirException(new ConsistirException());
						expedicaoDiploma.setFuncionarioPrimarioVO(getFuncionarioPrincipalVO());
						expedicaoDiploma.setFuncionarioSecundarioVO(getFuncionarioSecundarioVO());
						expedicaoDiploma.setFuncionarioTerceiroVO(getFuncionarioTerceiroVO());
						expedicaoDiploma.setFuncionarioQuartoVO(getFuncionarioQuartoVO());
						expedicaoDiploma.setFuncionarioQuintoVO(getFuncionarioQuintoVO());
						expedicaoDiploma.setCargoFuncionarioPrincipalVO(getCargoFuncionarioPrincipal());
						expedicaoDiploma.setCargoFuncionarioSecundarioVO(getCargoFuncionarioSecundario());
						expedicaoDiploma.setCargoFuncionarioTerceiroVO(getCargoFuncionarioTerceiro());
						expedicaoDiploma.setCargoFuncionarioQuartoVO(getCargoFuncionarioQuarto());
						expedicaoDiploma.setCargoFuncionarioQuintoVO(getCargoFuncionarioQuinto());
						expedicaoDiploma.setTituloFuncionarioPrincipal(getTituloFuncionarioPrincipal());
						expedicaoDiploma.setTituloFuncionarioSecundario(getTituloFuncionarioSecundario());
						expedicaoDiploma.setTituloFuncionarioTerceiro(getTituloFuncionarioTerceiro());
						expedicaoDiploma.setTituloFuncionarioQuarto(getTituloFuncionarioQuarto());
						expedicaoDiploma.setTituloFuncionarioQuinto(getTituloFuncionarioQuinto());
						expedicaoDiploma.setVersaoDiploma(getVersaoDiplomaLote());
						expedicaoDiploma.setUnidadeEnsinoCertificadora(getProgressBarVO().getUnidadeEnsinoVO());
						expedicaoDiploma.setGerarXMLDiploma(gerarXMLDiploma);
						if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getDataPublicacaoDiarioOficial())) {
							expedicaoDiploma.setDataPublicacaoDiarioOficial(dataPublicacaoDO);
						}
						if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getDataRegistroDiploma())) {
							expedicaoDiploma.setDataRegistroDiploma(dataRegistroDiploma);
						}
						if (!Uteis.isAtributoPreenchido(expedicaoDiploma.getDataExpedicao())) {
							expedicaoDiploma.setDataExpedicao(dataExpedicao);
						}
						getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoDocumentacaoAcademica(expedicaoDiploma, getSuperControleRelatorio(), getProgressBarVO().getUsuarioVO());
					} catch (ConsistirException e) {
						if (Uteis.isAtributoPreenchido(e.getListaMensagemErro())) {
							expedicaoDiploma.getConsistirException().getListaMensagemErro().addAll(e.getListaMensagemErro());
						} else {
							expedicaoDiploma.getConsistirException().getListaMensagemErro().add(e.getMessage());
						}
						getListaExpedicaoDiplomaVOErros().add(expedicaoDiploma);
					} catch (Exception ex) {
						expedicaoDiploma.getConsistirException().getListaMensagemErro().add(ex.getMessage());
						getListaExpedicaoDiplomaVOErros().add(expedicaoDiploma);
					}
					x++;
				}
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			filtrarAlunosGerarXMl();
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	public void executarInicioProgressBarGeracaoHistoricoDigitalLote() {
		try {
			setOncompleteModal("RichFaces.$('panelMotivoRejeicao').hide()");
			getListaExpedicaoDiplomaVOErros().clear();
			setOnCompleteDownloadExpedicaoDiplomaLote(Constantes.EMPTY);
			setFazerDownload(Boolean.FALSE);
			setCaminhoRelatorio(Constantes.EMPTY);
			if (!getListaExpedicaoDiplomaVOs().isEmpty()) {
				setProgressBarVO(new ProgressBarVO());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoCertificadoraLote().clone());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (getListaExpedicaoDiplomaVOs().size()), "Carregando Expedição Diploma", true, this, "realizarGeracaoHistoricoDigitalLote");
			} else {
				throw new ConsistirException("Não existem dados a serem gerados .");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void montarDadosUltimoDiplomaAluno() throws Exception {
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getDiplomaDigital())) {
			if (!Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getDiplomaDigital().getListaDocumentoAssinadoPessoa())) {
				getExpedicaoDiplomaVO().setDiplomaDigital(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getDiplomaDigital().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} else {
			if (Uteis.isAtributoPreenchido(getListaDocumentoAsssinados())) {
				Optional<DocumentoAssinadoVO> max = getListaDocumentoAsssinados().stream().filter(d -> d.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || d.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)).max(Comparator.comparing(DocumentoAssinadoVO::getCodigo));
				if (max.isPresent()) {
					getExpedicaoDiplomaVO().setDiplomaDigital(max.get());
				}
			}
		}
	}
	
	public void limparFilterFactory() {
    	FilterFactory filter = (FilterFactory) getControlador("FilterFactory");
    	if (filter != null && !filter.getMapFilter().isEmpty()) {
    		if (filter.getMapFilter().get("expedicaoDiplomaVO") != null) {
    			filter.getMapFilter().get("expedicaoDiplomaVO").setFiltro(Constantes.EMPTY);
    		}
    		if (filter.getMapFilter().get("expedicaoDiplomaVO2") != null) {
    			filter.getMapFilter().get("expedicaoDiplomaVO2").setFiltro(Constantes.EMPTY);
    		}
    		if (filter.getMapFilter().get("expedicaoDiplomaVO3") != null) {
    			filter.getMapFilter().get("expedicaoDiplomaVO3").setFiltro(Constantes.EMPTY);
    		}
    		if (filter.getMapFilter().get("expedicaoDiplomaVO4") != null) {
    			filter.getMapFilter().get("expedicaoDiplomaVO4").setFiltro(Constantes.EMPTY);
    		}
    		if (filter.getMapFilter().get("expedicaoDiplomaVO5") != null) {
    			filter.getMapFilter().get("expedicaoDiplomaVO5").setFiltro(Constantes.EMPTY);
    		}
    	}
    }
	
	public void validarQuantidadeObservacoesSelecionadas() {
		if (Uteis.isAtributoPreenchido(getListaObservacaoComplementarVOs())) {
			if (getListaObservacaoComplementarVOs().stream().filter(ObservacaoComplementarVO::getSelecionado).count() == 3) {
				getListaObservacaoComplementarVOs().stream().forEach(o -> {
					if (o.getSelecionado()) {
						o.setDisabled(Boolean.FALSE);
					} else {
						o.setDisabled(Boolean.TRUE);
					}
				});
			} else {
				getListaObservacaoComplementarVOs().stream().forEach(o -> o.setDisabled(Boolean.FALSE));
			}
		}
	}
	
	public void realizarMontagemCargoFuncionario() throws Exception {
		setFuncionarioPrincipal(Boolean.TRUE);
		if (getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getFuncionarioCargoVOs().isEmpty() && getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getCodigo().intValue() > 0) {
			getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getFuncionarioCargoVOs());
		setFuncionarioPrincipal(Boolean.FALSE);
		
		setFuncionarioSecundario(Boolean.TRUE);
		if (getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getFuncionarioCargoVOs().isEmpty() && getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getCodigo().intValue() > 0) {
			getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getFuncionarioCargoVOs());
		setFuncionarioSecundario(Boolean.FALSE);
		
		setFuncionarioTerceiro(Boolean.TRUE);
		if (getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getFuncionarioCargoVOs().isEmpty() && getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getCodigo().intValue() > 0) {
			getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getFuncionarioCargoVOs());
		setFuncionarioTerceiro(Boolean.FALSE);
		
		setFuncionarioQuarto(Boolean.TRUE);
		if (getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getFuncionarioCargoVOs().isEmpty() && getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getCodigo().intValue() > 0) {
			getExpedicaoDiplomaVO().getFuncionarioQuartoVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuartoVO().getFuncionarioCargoVOs());
		setFuncionarioQuarto(Boolean.FALSE);
		
		setFuncionarioQuinto(Boolean.TRUE);
		if (getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getFuncionarioCargoVOs().isEmpty() && getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getCodigo().intValue() > 0) {
			getExpedicaoDiplomaVO().getFuncionarioQuintoVO().setFuncionarioCargoVOs(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargos(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		}
		montarComboCargoFuncionario(getExpedicaoDiplomaVO().getFuncionarioQuintoVO().getFuncionarioCargoVOs());
		setFuncionarioQuinto(Boolean.FALSE);
	}

	public void realizarNavegacaoTelaHistoricoAlunoPorProgramacaoFormatura() {
		try {
			context().getExternalContext().getSessionMap().put("programacaoFormaturaLote", getProgramacaoFormaturaVO().getCodigo());
			context().getExternalContext().getSessionMap().put("funcionarioPrincipal", getFuncionarioPrincipalVO());
			context().getExternalContext().getSessionMap().put("cargoFuncionarioPrincipal", getCargoFuncionarioPrincipal());
			if (getProgramacaoFormaturaVO().getNivelEducacionalGraduacaoGraduacaoTecnologica() && getGerarXmlDiplomaLote()) {
				context().getExternalContext().getSessionMap().put("funcionarioSecundario", getFuncionarioTerceiroVO());
				context().getExternalContext().getSessionMap().put("cargoFuncionarioSecundario", getCargoFuncionarioTerceiro());
				context().getExternalContext().getSessionMap().put("gerarXmlDiploma", Boolean.TRUE);
			} else {
				context().getExternalContext().getSessionMap().put("funcionarioSecundario", getFuncionarioSecundarioVO());
				context().getExternalContext().getSessionMap().put("cargoFuncionarioSecundario", getCargoFuncionarioSecundario());
				context().getExternalContext().getSessionMap().put("gerarXmlDiploma", Boolean.FALSE);
			}
			removerControleMemoriaFlash("HistoricoAlunoRelControle");
			removerControleMemoriaTela("HistoricoAlunoRelControle");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarAlunosSelecionadosListaExpedicaoDiploma() {
		if (!isProgramacaoFormaturaGerarXmlDiploma()) {
			setFiltroSituacaoDiplomaDigitalLote(Constantes.EMPTY);
			setFiltroSituacaoDocumentacaoAcademicaLote(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(expedicaoDiploma -> !expedicaoDiploma.getSelecionado())) {
				getListaExpedicaoDiplomaVOs().stream().filter(expedicaoDiploma -> !expedicaoDiploma.getSelecionado()).forEach(expedicaoDiploma -> expedicaoDiploma.setSelecionado(Boolean.TRUE));
			}
		}
	}

	public boolean isProgramacaoFormaturaGerarXmlDiploma() {
		return Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO()) && getProgramacaoFormaturaVO().getNivelEducacionalGraduacaoGraduacaoTecnologica() && isAssinarDigitalmente() && getGerarXmlDiplomaLote();
	}

	public String getFiltroSituacaoDiplomaDigitalLote() {
		if (filtroSituacaoDiplomaDigitalLote == null) {
			filtroSituacaoDiplomaDigitalLote = Constantes.EMPTY;
		}
		return filtroSituacaoDiplomaDigitalLote;
	}

	public void setFiltroSituacaoDiplomaDigitalLote(String filtroSituacaoDiplomaDigitalLote) {
		this.filtroSituacaoDiplomaDigitalLote = filtroSituacaoDiplomaDigitalLote;
	}

	public String getFiltroSituacaoDocumentacaoAcademicaLote() {
		if (filtroSituacaoDocumentacaoAcademicaLote == null) {
			filtroSituacaoDocumentacaoAcademicaLote = Constantes.EMPTY;
		}
		return filtroSituacaoDocumentacaoAcademicaLote;
	}

	public void setFiltroSituacaoDocumentacaoAcademicaLote(String filtroSituacaoDocumentacaoAcademicaLote) {
		this.filtroSituacaoDocumentacaoAcademicaLote = filtroSituacaoDocumentacaoAcademicaLote;
	}

	public List<SelectItem> getSelectItemSituacaoXMLLote() {
		if (selectItemSituacaoXMLLote == null) {
			selectItemSituacaoXMLLote = new ArrayList<>(0);
			selectItemSituacaoXMLLote.add(new SelectItem("", ""));
			selectItemSituacaoXMLLote.add(new SelectItem(XML_NAO_GERADO, "Não Gerado"));
			selectItemSituacaoXMLLote.add(new SelectItem(XML_ASSINADO, "Assinado"));
			selectItemSituacaoXMLLote.add(new SelectItem(XML_PENDENTE, "Pendente"));
			selectItemSituacaoXMLLote.add(new SelectItem(XML_REJEITADO, "Rejeitado"));
		}
		return selectItemSituacaoXMLLote;
	}

	public void setSelectItemSituacaoXMLLote(List<SelectItem> selectItemSituacaoXMLLote) {
		this.selectItemSituacaoXMLLote = selectItemSituacaoXMLLote;
	}

	public void filtrarAlunosGerarXMl() {
		try {
			if (isProgramacaoFormaturaGerarXmlDiploma()) {
				getFacadeFactory().getExpedicaoDiplomaFacade().realizarFiltragemAlunosPermitirGerarXML(getListaExpedicaoDiplomaVOs(), getFiltroSituacaoDiplomaDigitalLote(), getFiltroSituacaoDocumentacaoAcademicaLote());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getOutputTextFuncionarioPrimarioLote() {
		if (isProgramacaoFormaturaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioPrincipalEmissora");
		} else {
			return "Assinatura Funcionário 1ª";

		}
	}

	public String getOutputTextFuncionarioSecundarioLote() {
		if (isProgramacaoFormaturaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioSecundarioEmissora");
		} else {
			return "Assinatura Funcionário 2ª";

		}
	}

	public String getOutputTextFuncionarioTerceiroLote() {
		if (isProgramacaoFormaturaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioTerceiroEmissora");
		} else {
			return "Assinatura Funcionário 3ª";

		}
	}

	public String getStyleClassInputTextFuncionarioSecundarioLote() {
		if (isProgramacaoFormaturaGerarXmlDiploma()) {
			return "form-control campos";
		} else {
			return "form-control camposObrigatorios";
		}
	}

	public String getStyleClassInputTextFuncionarioTerceiroLote() {
		if (isProgramacaoFormaturaGerarXmlDiploma()) {
			return "form-control camposObrigatorios";
		} else {
			return "form-control campos";
		}
	}
	
	public boolean isApresentarListaExpedicaoDiploma() {
		return Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado);
	}
	
	public boolean isExpedicaoDiplomaGraduacao() {
		return Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula()) && getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalGraduacaoGraduacaoTecnologica();
	}
	
	public boolean isPermitirExpedicaoDiplomaGerarXmlDiploma() {
		return isExpedicaoDiplomaGraduacao() && isAssinarDigitalmente();
	}
	
	public boolean isExpedicaoDiplomaGerarXmlDiploma() {
		return isPermitirExpedicaoDiplomaGerarXmlDiploma() && getExpedicaoDiplomaVO().getGerarXMLDiploma();
	}
	
	public String getOutputTextFuncionarioPrimarioExpedicaoDiploma() {
		if (isExpedicaoDiplomaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioPrincipalEmissora");
		} else {
			return "Assinatura Funcionário 1ª";

		}
	}

	public String getOutputTextFuncionarioSecundarioExpedicaoDiploma() {
		if (isExpedicaoDiplomaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioSecundarioEmissora");
		} else {
			return "Assinatura Funcionário 2ª";

		}
	}

	public String getOutputTextFuncionarioTerceiroExpedicaoDiploma() {
		if (isExpedicaoDiplomaGerarXmlDiploma()) {
			return UteisJSF.internacionalizar("prt_DiplomaAlunoRel_funcionarioTerceiroEmissora");
		} else {
			return "Assinatura Funcionário 3ª";

		}
	}

	public String getStyleClassInputTextFuncionarioSecundarioExpedicaoDiploma() {
		if (isExpedicaoDiplomaGerarXmlDiploma()) {
			return "form-control campos";
		} else {
			return "form-control camposObrigatorios";
		}
	}

	public String getStyleClassInputTextFuncionarioTerceiroExpedicaoDiploma() {
		if (isExpedicaoDiplomaGerarXmlDiploma()) {
			return "form-control camposObrigatorios";
		} else {
			return "form-control campos";
		}
	}
	
	public TipoOrigemDocumentoAssinadoEnum getTipoXmlMecGerar() {
		return tipoXmlMecGerar;
	}
	
	public void setTipoXmlMecGerar(TipoOrigemDocumentoAssinadoEnum tipoXmlMecGerar) {
		this.tipoXmlMecGerar = tipoXmlMecGerar;
	}
	
	public List<SelectItem> getListaSelectItemTipoXmlMec() {
		if (listaSelectItemTipoXmlMec == null) {
			listaSelectItemTipoXmlMec = new ArrayList<>(0);
			listaSelectItemTipoXmlMec.add(new SelectItem(null, Constantes.EMPTY));
			listaSelectItemTipoXmlMec.add(new SelectItem(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL, TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL.getDescricao()));
			listaSelectItemTipoXmlMec.add(new SelectItem(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL, TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL.getDescricao()));
			listaSelectItemTipoXmlMec.add(new SelectItem(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL, TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL.getDescricao()));
		}
		return listaSelectItemTipoXmlMec;
	}
	
	public void setListaSelectItemTipoXmlMec(List<SelectItem> listaSelectItemTipoXmlMec) {
		this.listaSelectItemTipoXmlMec = listaSelectItemTipoXmlMec;
	}
	
	public boolean isGerarDiplomaDigital() {
		return Objects.nonNull(getTipoXmlMecGerar()) && getTipoXmlMecGerar().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
	}
	
	public boolean isGerarExpedicaoDiploma() {
		return Objects.nonNull(getTipoXmlMecGerar()) && getTipoXmlMecGerar().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
	}
	
	private void verificarDadosCargosFuncionariosMontados(Boolean expedicaoDiplomaLote) throws Exception {
		if (expedicaoDiplomaLote) {
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioPrincipal()) && !Uteis.isAtributoPreenchido(getCargoFuncionarioPrincipal().getNome())) {
				setCargoFuncionarioPrincipal(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionarioPrincipal().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioSecundario()) && !Uteis.isAtributoPreenchido(getCargoFuncionarioSecundario().getNome())) {
				setCargoFuncionarioSecundario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionarioSecundario().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioTerceiro()) && !Uteis.isAtributoPreenchido(getCargoFuncionarioTerceiro().getNome())) {
				setCargoFuncionarioTerceiro(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionarioTerceiro().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioQuarto()) && !Uteis.isAtributoPreenchido(getCargoFuncionarioQuarto().getNome())) {
				setCargoFuncionarioQuarto(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionarioQuarto().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getCargoFuncionarioQuinto()) && !Uteis.isAtributoPreenchido(getCargoFuncionarioQuinto().getNome())) {
				setCargoFuncionarioQuinto(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoFuncionarioQuinto().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		} else {
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO()) && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getNome())) {
				getExpedicaoDiplomaVO().setCargoFuncionarioPrincipalVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getCargoFuncionarioPrincipalVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO()) && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getNome())) {
				getExpedicaoDiplomaVO().setCargoFuncionarioSecundarioVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getCargoFuncionarioSecundarioVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO()) && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getNome())) {
				getExpedicaoDiplomaVO().setCargoFuncionarioTerceiroVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getCargoFuncionarioTerceiroVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioQuartoVO()) && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioQuartoVO().getNome())) {
				getExpedicaoDiplomaVO().setCargoFuncionarioQuartoVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getCargoFuncionarioQuartoVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioQuintoVO()) && !Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getCargoFuncionarioQuintoVO().getNome())) {
				getExpedicaoDiplomaVO().setCargoFuncionarioQuintoVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getExpedicaoDiplomaVO().getCargoFuncionarioQuintoVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		}
	}
	
	public List<ExpedicaoDiplomaVO> getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula() {
		if (listaExpedicaoDiplomaCorrigirDocumentacaoMatricula == null) {
			listaExpedicaoDiplomaCorrigirDocumentacaoMatricula = new ArrayList<>(0);
		}
		return listaExpedicaoDiplomaCorrigirDocumentacaoMatricula;
	}
	
	public void setListaExpedicaoDiplomaCorrigirDocumentacaoMatricula(List<ExpedicaoDiplomaVO> listaExpedicaoDiplomaCorrigirDocumentacaoMatricula) {
		this.listaExpedicaoDiplomaCorrigirDocumentacaoMatricula = listaExpedicaoDiplomaCorrigirDocumentacaoMatricula;
	}
	
	public void executarInicioProgressBarCorrecaoDocumentacaoMatriculaLote() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula().clear();
			if(Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs()) && getListaExpedicaoDiplomaVOs().stream().anyMatch(ExpedicaoDiplomaVO::getSelecionado)) {
				setOncompleteModal("RichFaces.$('panelAvisoPdfA').hide()");
				setProgressBarVO(new ProgressBarVO());			
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUnidadeEnsinoVO((UnidadeEnsinoVO) getUnidadeEnsinoCertificadoraLote().clone());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, (getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList()).size()), "Carregando Expedição Diploma", true, this,	"realizarCorrecaoDocumentacaoMatricula");
			}else {
				throw new ConsistirException("Não existem dados a serem corrigidos.");
			}	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarCorrecaoDocumentacaoMatricula() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula().clear();
			List<ExpedicaoDiplomaVO> expedicaoDiplomaVOs = getListaExpedicaoDiplomaVOs().stream().filter(ExpedicaoDiplomaVO::getSelecionado).collect(Collectors.toList());
			for (ExpedicaoDiplomaVO expedicaoDiploma : expedicaoDiplomaVOs) {
				if (Objects.nonNull(expedicaoDiploma)) {
					try {
						getFacadeFactory().getExpedicaoDiplomaFacade().realizarCorrecaoDocumentacaoMatriculaPorExpedicaoDiploma(expedicaoDiploma, getProgressBarVO());
					} catch (ConsistirException ce) {
						getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula().add(expedicaoDiploma);
					} catch (Exception e) {
						expedicaoDiploma.getConsistirException().getListaMensagemErro().add(e.getMessage());
						getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula().add(expedicaoDiploma);
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			if (Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaCorrigirDocumentacaoMatricula())) {
				setOncompleteModal("RichFaces.$('panelErroCorrecaoDocumentacaoMatricula').show()");
			}
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	public void realizarMontagemNovaExpedicaoDiploma() throws Exception {
		if (Uteis.isAtributoPreenchido(getExpedicaoDiplomaVO().getMatricula())) {
			getFacadeFactory().getExpedicaoDiplomaFacade().consultarFuncionarioResponsavelExpedicao(getExpedicaoDiplomaVO(), getUsuarioLogado());
			getFacadeFactory().getExpedicaoDiplomaFacade().consultarObservacaoComplementarUltimaExpedicaoMatricula(getExpedicaoDiplomaVO(), getUsuarioLogado());
			getFacadeFactory().getExpedicaoDiplomaFacade().carregarViaAnteriorExpedicaoDiploma(getExpedicaoDiplomaVO());
			getExpedicaoDiplomaVO().getGradeCurricularVO().setCodigo(getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo());
			getExpedicaoDiplomaVO().gerarNumeroProcesso(getFacadeFactory().getRequerimentoFacade().consultaRapidaUltimoRequerimentoPorMatriculaTipoDocumentoDiploma(getExpedicaoDiplomaVO().getMatricula().getMatricula()));
			montarListaGradeCurricular();
			getMostrarCampoCursoGradeCurricular();
			verificarLayoutPadrao();
			if (!getExpedicaoDiplomaVO().getLayoutDiploma().trim().isEmpty()) {
				setTipoLayout(getExpedicaoDiplomaVO().getLayoutDiploma());
			}
			realizarMontagemCargoFuncionario();
			if (getExpedicaoDiplomaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
				if (getExpedicaoDiplomaVO().getMatricula().getDataInicioCurso() == null) {
					getExpedicaoDiplomaVO().getMatricula().setDataInicioCurso(getFacadeFactory().getMatriculaPeriodoFacade().consultarDataInicioCursoMatricula(getExpedicaoDiplomaVO().getMatricula().getMatricula()));
				}
			}
			getListaTipoLayout().stream().findFirst().map(SelectItem::getValue).map(Object::toString).ifPresent(this::setTipoLayout);
			montarListaSelectItemTipoTextoPadrao();
			montarNumeroProcesso();
			setMensagemDetalhada(Constantes.EMPTY);
			setMensagemID("msg_dados_consultados");
			getExpedicaoDiplomaVO().setPercentualCHIntegralizacaoMatricula(Uteis.getDoubleFormatado(getFacadeFactory().getHistoricoFacade().consultarPercentualCHIntegralizacaoPorMatriculaGradeCurricular(getExpedicaoDiplomaVO().getMatricula().getMatricula(), getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), getUsuarioLogado())));
			getExpedicaoDiplomaVO().setCargaHorariaTotal(getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaExigidaGrade(getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), getUsuarioLogado()));
			getExpedicaoDiplomaVO().setCargaHorariaCursada(getFacadeFactory().getDisciplinaFacade().consultarCargaHorariaCumpridaNoHistoricoPorGradeCurricularComDisciplinaEquivalente(getExpedicaoDiplomaVO().getMatricula().getMatricula(), getExpedicaoDiplomaVO().getMatricula().getGradeCurricularAtual().getCodigo(), false, getUsuarioLogado()));
			getListaSelectItemVersaoDiploma();
			verificarConfiguracaoDiplomaExistente(Boolean.TRUE);
			montarListaSelectItemUnidadeEnsino();
			verificarSegundaVia(Boolean.FALSE);
		}
	}
	
	public boolean isProgramacaoFormaturaGraduacao() {
		return Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO()) && getProgramacaoFormaturaVO().getNivelEducacionalGraduacaoGraduacaoTecnologica();
	}
	
	public void realizarMarcacaoTodosInformarCamposLivroRegistradora() {
		if (Uteis.isAtributoPreenchido(getListaExpedicaoDiplomaVOs())) {
			getListaExpedicaoDiplomaVOs().stream().forEach(expedicao -> expedicao.setInformarCamposLivroRegistradora(getInformarCamposLivroRegistradoraLote()));
		}
	}
}
