package controle.administrativo;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.component.datatable.DataTable;
import jakarta.annotation.PostConstruct;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;

//import org.primefaces.event.PageEvent;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
//import controle.academico.VisaoCoordenadorControle;
//import controle.academico.VisaoProfessorControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FiliacaoVO;
//import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaProfessorDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoComunicadoInternoComunicacaoInternaEnum;
import negocio.comuns.administrativo.enumeradores.TipoDestinatarioComunicadoInternaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.administrativo.ComunicacaoInterna;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;


import org.primefaces.component.datatable.DataTable; // Nova importação
import jakarta.faces.context.FacesContext;
import jakarta.faces.component.UIViewRoot;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas comunicacaoInternaForm.jsp comunicacaoInternaCons.jsp) com as
 * funcionalidades da classe <code>ComunicacaoInterna</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ComunicacaoInterna
 * @see ComunicacaoInternaVO
 */
@Controller("ComunicacaoInternaControle")
@Scope("session")
@Lazy
public class ComunicacaoInternaControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ComunicacaoInternaVO comunicacaoInternaVO;
//	private ComunicacaoInternaVO comunicacaoInternaMarketing;
//	private ComunicacaoInternaVO comunicacaoInternaLeituraObrigatoria;
	private ComunicacaoInternaVO comunicacaoInternaResposta;
	protected List<SelectItem> listaSelectItemResponsavel;
	protected List<SelectItem> listaSelectItemFuncionario;
	protected List<SelectItem> listaSelectItemCargo;
	protected List<SelectItem> listaSelectItemDepartamento;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected List<SelectItem> listaSelectItemCoordenador;
	protected List<SelectItem> listaSelectItemAluno;
	protected List<SelectItem> listaSelectItemProfessor;
	private List<SelectItem> listaSelectItemAreaConhecimento;
	private List<SelectItem> listaSelectItemTurma;
	protected List<SelectItem> listaSelectItemComunicadoInternoOrigem;
	private Boolean DestinatarioFU;
	private Boolean DestinatarioAL;
	private Boolean DestinatarioPR;
	private Boolean DestinatarioCA;
	private Boolean DestinatarioDE;
	private Boolean DestinatarioAR;
	private Boolean DestinatarioTU;
	private Boolean DestinatarioTD;
	private Boolean DestinatarioTO;
	private Boolean DestinatarioCO;
	private Boolean DestinatarioTA;
	private Boolean DestinatarioTP;
	private Boolean DestinatarioTC;
	private Boolean DestinatarioAA;
	private Boolean DestinatarioTF;
	private Boolean DestinatarioTR;
	private Boolean DestinatarioRL;
	private Boolean DestinatarioTT;
	private Boolean DestinatarioALAS;
	private Boolean TipoMural;
	protected String campoCaixa;
	private String valorConsultaFuncionario;
	private String valorConsultaProfessor;
	private String valorConsultaAluno;
	private String valorConsultaTurma;
	private String valorConsultaPessoa;
	private String campoConsultaFuncionario;
	private List listaConsultaFuncionario;
	private String campoConsultaAluno;
	private List<PessoaVO> listaConsultaAluno;
	private String campoConsultaProfessor;
	private List listaConsultaProfessor;
	private String campoConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaPessoa;
	private List listaConsultaPessoa;
	private String nomeArquivoAnexo;
	private String campoConsultaCoordenador;
	private String valorConsultaCoordenador;
	private List listaConsultaCoordenador;
	// rodrigo
	protected String abaAtiva;
	protected String tipoEntradaSaida;
	protected Boolean verListaDestinatarios;
	protected Boolean novoComunicado;
	protected Boolean editarComunicado;
	protected Boolean lerComunicado;
	protected Boolean responderComunicado;
	protected Boolean habilitarMsgErro;
	protected Boolean existeConsulta;
	protected Boolean lidas;
	protected Boolean naoLidas;
	protected Boolean naoRespondidas;
	protected Boolean respondidas;
	protected Boolean comunicadoSaida;
	protected Boolean removerTodas;
	protected String tipoSaidaConsulta;
	protected String mesConsulta;
	protected Integer anoConsulta;
	protected Integer limiteRecado;
	protected String matricula;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	protected List<SelectItem> listaSelectItemDestinatario;
	protected List listaConsultaSaida;
	// private Boolean consultaPorCodigo;
	// private Boolean consultaPorData;
	// atributos para richModal Professor visão Professor
	private String campoVisaoProfessor;
	private String valorVisaoProfessor;
	private List listaVisaoProfessor;
	private String campoConsultaProfessorVisaoAluno;
	private String valorConsultaProfessorVisaoAluno;
	private List<PessoaVO> listaConsultaProfessorVisaoAluno;
	private List listaPessoaDestinatario;
	protected Boolean abrirRichModalMarketing;
	private Boolean abrirRichModalLeituraObrigatoria;
	private Boolean abrirRichModalMensagem;
	private String mensagemLocalizacao;
	private String apresentarRichMensagem;
	private Integer qtdMensagemCaixaEntrada;
	private TurmaVO turmaVO;
	private Boolean apresentarBotaoResponder;
	private Boolean mostrarBotoes;
	private String caminhoImagemTipoMarketing;
	private Boolean informarEmailManualmente;
	private Boolean mostrarImagem;	
	private Boolean visualizarMensagemAlunoPorResponsavelLegal;
	private String tipoAluno;
	private List<SelectItem> listaSelectItemDisciplinaProfessorTurma;
	protected String telaOrigemCadastro;
	protected Boolean dentroPrazo;
	public Boolean fecharModalTurma;
	private String ano;
	private String semestre;
	private Boolean apresentarModalAnoSemestre;
	private String campoConsultaTurmaDisciplina;
	private String valorConsultaTurmaDisciplina;
	private List listaConsultaTurmaDisciplina;
	private String tipoNivelEducacional;
	private Boolean apresentarBotaoEnviarResposta;
	private String filtrarPorAssunto;
	private String filtrarPorNomeResponsavel;
	private String filtrarPorData;
	private Boolean apresentarEnviarResponsavelFinanceiro;
	private Boolean apresentarEnviarPais;
	private Boolean imprimirEmail;
	private Boolean permitirResponderComunicadoInternoSomenteLeitura;
	private String labelArquivosAnexos;
	private Boolean habilitarFiltroCursoTurma;
	private List listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private Boolean consultarSemConsiderarData;
	private Boolean apresentarBotaoNovoRecado;
	private Boolean permitirEnviarComunicadoParaMeusAmigos;

	private Boolean apresentarBotaoAcessarRequerimento;
	private Boolean apresentarSemestreTurma;
	private Boolean apresentarAnoTurma;
	private Boolean permitirEnviarComunicadoParaTurmaPeriodoAnterior;	
	private Boolean trazerApenasAlunosNaoRenovaram;
	private String anoRenovacao;
	private String semestreRenovacao;
	private PeriodicidadeEnum periodicidade;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private List<ComunicacaoInternaVO> listaConsultaTopo;
	private String nivelEducacional;
	private Double percentualIntegralizacaoCurricularInicial;
	private Double percentualIntegralizacaoCurricularFinal;
	private String identificadorSalaAulaBlackboard;
	private BimestreEnum bimestre;
	private TipoDeficiencia tipoDeficiencia;
	private List<SelectItem> listaSelectItemBimestre;
	private String campoConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private DisciplinaVO disciplinaVO;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private String campoConsultaProgramacaoFormatura;
	private String valorConsultaProgramacaoFormatura;
	private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura;
	private Boolean mostrarSegundoCampoProgramacaoFormatura;
	private List<SelectItem> tipoConsultaComboProgramacaoFormatura;
	private Date valorConsultaDataInicioProgramacaoFormatura;
    private Date valorConsultaDataFinalProgramacaoFormatura;
    private List<SelectItem> comboFiltroAlunosPresentesColacaoGrau;
    private String filtroAlunosPresentesColacaoGrau;
    private List<SelectItem> listaSelectItemDiaSemanaAula;
    private DiaSemana diaSemana;
    private ProgressBarVO progressBarVO;
    private List<SelectItem> listSelectLimitePagina;
    private Boolean consultarTodasMensagens;


	public ComunicacaoInternaControle() throws Exception {
		super();
		// setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
		// setMensagemID("msg_entre_prmconsulta");
//		if (Uteis.isAtributoPreenchido(getUsuarioLogadoClone()) && !getUsuarioLogado().getVisaoLogar().equals("professor") && !getUsuarioLogado().getVisaoLogar().equals("aluno") && !getUsuarioLogado().getVisaoLogar().equals("pais") && !getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
//			setMesConsulta("");
//			consultarTodasEntrada();
//			setMesConsulta(null);
//		}
		if(!verificarUsuarioPossuiPermissaoNovo("ComunicacaoInterna")) {
			setApresentarBotaoNovoRecado(Boolean.FALSE);
		}
	}
	
	@PostConstruct
	public void realizarCarregamentoComunicacaoInternaVindoTelaFichaAluno() {
		if (context().getExternalContext().getSessionMap().get("comunicacaoInternaVO") != null) {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getSessionMap().get("comunicacaoInternaVO");
			setComunicacaoInternaVO(obj);
			try {
				novaMensagemPlanoEnsinoVisaoCoordenador();
			} catch (Exception e) {
				e.printStackTrace();
			}
			context().getExternalContext().getSessionMap().remove("comunicacaoInternaVO");
		}
		if (context().getExternalContext().getSessionMap().get("coumnicacao") != null) {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getSessionMap().get("coumnicacao");
			setComunicacaoInternaVO(obj);
			try {
				editarComunicacaoInternaVindoOutraTela(getComunicacaoInternaVO());
				confirmarLeitura();
			} catch (Exception e) {
				e.printStackTrace();
			}
			context().getExternalContext().getSessionMap().remove("coumnicacao");
		} else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				verificarQtdeMensagemCaixaEntrada();
//				consultarTodasEntradaLimiteProfessor();
				consultarTodasEntradaMarketingLeituraObrigatoriaAluno();
		} else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			verificarQtdeMensagemCaixaEntrada();
//			try {
//				consultarTodasEntradaLimiteCoordenador();
//			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			consultarTodasEntradaMarketingLeituraObrigatoriaAluno();
		}else {
			try {
				if(getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno()) {
					consultarTodasEntradaMarketingLeituraObrigatoriaAluno();
					verificarQtdeMensagemCaixaEntrada();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (context().getExternalContext().getSessionMap().get("comunicacaoInternaFichaAluno") != null) {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getSessionMap().get("comunicacaoInternaFichaAluno");
			if (obj != null && !obj.getCodigo().equals(0)) {
				try {
					realizarEdicaoComunicacaoInternaFichaAluno(obj);
					setMensagemID("msg_dados_editar");
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				} finally {
					context().getExternalContext().getSessionMap().remove("comunicacaoInternaFichaAluno");
				}
			} else if (obj != null && obj.getCodigo().equals(0)) {
				getComunicacaoInternaVO().setNovoObj(Boolean.TRUE);
				setComunicacaoInternaVO(obj);
				setNovoComunicado(Boolean.TRUE);
				setVerListaDestinatarios(Boolean.FALSE);
				setLerComunicado(Boolean.FALSE);
				setResponderComunicado(Boolean.FALSE);
				setMensagemID("msg_dados_editar");
				context().getExternalContext().getSessionMap().remove("comunicacaoInternaFichaAluno");
			}
		}
	}
	
	@PostConstruct
	public void carregarLayoutPadrao() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && !getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno()) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
				return;
			}
			if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {				
				LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(ComunicacaoInterna.class.getSimpleName() + "_" + getUsuarioLogado().getCodigo(), "LimitePagina", false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO) && Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
					getControleConsultaOtimizado().setLimitePorPagina(Integer.valueOf(layoutPadraoVO.getValor()));
				} else {
					getControleConsultaOtimizado().setLimitePorPagina(10);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarCarregamentoComunicacaoInternaFichaAluno(PessoaVO pessoaVO) {
		if (pessoaVO != null && !pessoaVO.getCodigo().equals(0)) {
			try {
				this.getComunicacaoInternaVO().setAluno(pessoaVO);
				this.getComunicacaoInternaVO().setAlunoNome(pessoaVO.getNome());
				this.getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.AL.name());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
				executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
				retirarDestinatarioEscolhidoDaLista(this.getListaConsultaAluno(), pessoaVO.getCodigo());
				setMensagemID("msg_dados_adicionados");
				context().getExternalContext().getSessionMap().put("comunicacaoInternaFichaAluno", getComunicacaoInternaVO());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} 
		}
	}
	
	public void realizarEdicaoComunicacaoInternaFichaAluno(ComunicacaoInternaVO obj) {
		if (obj != null && !obj.getCodigo().equals(0)) {
			try {
				editarComunicacaoInternaVindoOutraTela(obj);
				setMensagemID("msg_dados_adicionados");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} 
		}
	}

	
	
	
	public void verificarQtdeMensagemCaixaEntrada(){
		try {
			if (Uteis.isAtributoPreenchido(getUsuarioLogado())  && getLoginControle().getPermissaoAcessoMenuVO().getComunicacaoInterna()) {				
				DataModelo controleConsulta =  new DataModelo();
				controleConsulta.setPaginaAtual(1);
				controleConsulta.setPage(1);
				if (!getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
					getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimite(controleConsulta, getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), 10, 0, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), "", "", "", true);
				} else if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
					getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoAluno(controleConsulta,  getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), 10, 0, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), "", "", "", true);
				}
				setListaConsultaTopo((List<ComunicacaoInternaVO>) controleConsulta.getListaConsulta());
				setQtdMensagemCaixaEntrada(controleConsulta.getTotalRegistrosEncontrados());
				getLoginControle().setMensagemID("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer consultarLimiteRecados() {
		try {			
			if (getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()).getQtdeLimiteMsg().intValue() == 0) {
				return 10;
			}
			return getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()).getQtdeLimiteMsg();
		} catch (Exception e) {
			return 10;
		}
	}

	public void inicializarDestinatarios() {
		setDestinatarioFU(false);
		setDestinatarioAL(false);
		setDestinatarioPR(false);
		setDestinatarioCA(false);
		setDestinatarioDE(false);
		setDestinatarioAR(false);
		setDestinatarioTU(false);
		setDestinatarioTD(false);
		setDestinatarioTO(false);
		setDestinatarioTA(false);
		setDestinatarioTP(false);
		setDestinatarioTC(false);
		setDestinatarioAA(false);
		setDestinatarioTF(false);
		setDestinatarioTR(false);
		setDestinatarioALAS(false);
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ComunicacaoInterna</code> para edição pelo usuário da aplicação.
	 */
	public String novo()  { // removerObjetoMemoria(this);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Nova Comunicação Interna", "Novo");
			setLimiteRecado(consultarLimiteRecados());
			getListaSelectItemDisciplinaProfessorTurma().clear();
			getControleConsultaOtimizado().getListaConsulta().clear();
			inicializarDadosControleConsultaOtimizado();
			consultarTodasEntradaLimiteAluno();
			consultarTodasEntradaLimiteProfessor();
			consultarTodasEntradaLimiteCoordenador();
			setComunicacaoInternaVO(null);
			setAbaAtiva("mensagem");
			getComunicacaoInternaVO().setTipoComunicadoInterno("LE");
			getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getConfiguracaoGeralPadraoSistema().getTextoComunicacaoInterna()));
			inicializarListasSelectItemTodosComboBox();
			setVerListaDestinatarios(Boolean.FALSE);
			inicializarResponsavel();
			setNovoComunicado(Boolean.TRUE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			inicializarDestinatarios();
			setCaminhoFotoUsuario("");
			// definirApresentacao();
			setInformarEmailManualmente(false);
			setApresentarBotaoResponder(Boolean.FALSE);
			if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_permiteEnviarResponsavelFinanceiro")) {
				setApresentarEnviarResponsavelFinanceiro(Boolean.TRUE);
			}
			if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_permiteEnviarPais")) {
				setApresentarEnviarPais(Boolean.TRUE);
			}
			if(Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone())){
				getComunicacaoInternaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getComunicacaoInternaVO().getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
				consultarUnidadeEnsino();
			}
			getComunicacaoInternaVO().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
			getComunicacaoInternaVO().setEnviarEmailInstitucional(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
		
		return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaForm");
	}

	public void novoVisaoAlunoProfessorCoordenador() throws Exception {
		removerObjetoMemoria(this);
		setAbaAtiva("mensagem");
		setMensagemLocalizacao("Novo Recado");
		inicializarListasSelectItemTodosComboBox();
		setVerListaDestinatarios(Boolean.FALSE);
		inicializarResponsavel();
		setNovoComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
		setResponderComunicado(Boolean.FALSE);
		setHabilitarMsgErro(Boolean.FALSE);
		inicializarDestinatarios();
		VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		if (visaoAlunoControle != null) {
			visaoAlunoControle.inicializarMenuRecado();
			visaoAlunoControle.inicializarMenuNovoRecado();
			setMatricula(visaoAlunoControle.getMatricula().getMatricula());
			getComunicacaoInternaVO().setDigitarMensagem(true);
		}
//		VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
//		if (visaoProfessorControle != null) {
//			visaoProfessorControle.inicializarMenuRecado();
//			visaoProfessorControle.inicializarMenuNovoRecado();
//			getComunicacaoInternaVO().setDigitarMensagem(true);
//		}
//		VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
//		if (visaoCoordenadorControle != null) {
//			visaoCoordenadorControle.inicializarMenuRecado();
//			visaoCoordenadorControle.inicializarMenuNovoRecado();
//			getComunicacaoInternaVO().setDigitarMensagem(true);
//		}
		//getComunicacaoInternaVO().setTipoDestinatario("TU");
		if (getLoginControle().getUsuarioLogado().getVisaoLogar().equals("professor")) {
			definirTipoDestinatarioProfessor();
		}
		if (getLoginControle().getUsuarioLogado().getVisaoLogar().equals("aluno")) {
			definirTipoDestinatarioAluno();
		}
		if (getLoginControle().getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			definirTipoDestinatarioCoordenador();
		}
		if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_permiteEnviarResponsavelFinanceiro")) {
			setApresentarEnviarResponsavelFinanceiro(Boolean.TRUE);
		}
		if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_permiteEnviarPais")) {
			setApresentarEnviarPais(Boolean.TRUE);
		}
		if(Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone())){
			getComunicacaoInternaVO().setUnidadeEnsino(getUnidadeEnsinoLogadoClone());
			consultarUnidadeEnsino();
		}
		getComunicacaoInternaVO().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
		setMensagemID("msg_entre_dados");

	}
	
	public void novaMensagemPlanoEnsinoVisaoCoordenador() throws Exception {
		setAbaAtiva("mensagem");
		setMensagemLocalizacao("Novo Recado");
		inicializarListasSelectItemTodosComboBox();
		setVerListaDestinatarios(Boolean.FALSE);
		inicializarResponsavel();
		setNovoComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
		setResponderComunicado(Boolean.FALSE);
		inicializarDestinatarios();
//		VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
//		if (visaoCoordenadorControle != null) {
//			visaoCoordenadorControle.inicializarMenuRecado();
//			visaoCoordenadorControle.inicializarMenuNovoRecado();
//			getComunicacaoInternaVO().setDigitarMensagem(true);
//		}
		if (getLoginControle().getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			definirTipoDestinatarioCoordenador();
		}
		getComunicacaoInternaVO().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
		setMensagemID("msg_entre_dados");
	}

	public String novoCRM() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Nova Comunicação Interna", "Novo");
		setLimiteRecado(consultarLimiteRecados());
		getListaSelectItemDisciplinaProfessorTurma().clear();
		// consultarTodasEntradaLimiteAluno();
		// consultarTodasEntradaLimiteProfessor();
		// consultarTodasEntradaLimiteCoordenador();
		setComunicacaoInternaVO(null);
		setAbaAtiva("mensagem");
		getComunicacaoInternaVO().setTipoComunicadoInterno("LE");
		getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getConfiguracaoGeralPadraoSistema().getTextoComunicacaoInterna()));
		inicializarListasSelectItemTodosComboBox();
		setVerListaDestinatarios(Boolean.FALSE);
		inicializarResponsavel();
		setNovoComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
		setResponderComunicado(Boolean.FALSE);
		inicializarDestinatarios();
		setCaminhoFotoUsuario("");
		// definirApresentacao();
		setInformarEmailManualmente(false);
		setApresentarBotaoResponder(Boolean.FALSE);
		getComunicacaoInternaVO().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public void salvarMensagemSMS() {
		String msg = this.getComunicacaoInternaVO().getMensagemSMS();
		msg = msg;
	}
	
	

	

	public void inicializarResponsavel() {
		try {
			if (getUsuarioLogado().getPessoa() == null || getUsuarioLogado().getPessoa().getCodigo().equals(0)) {
				throw new Exception("Este usuáro não pode enviar Comunicação Interna, pois não possui nenhuma pessoa vinculada a ele. ");
			}
			getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa().getClone());

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public void paint(OutputStream out, Object data) throws Exception {
	// ArquivoHelper arquivoHelper = new ArquivoHelper();
	// try {
	// arquivoHelper.renderizarImagemNaTela(out,
	// getComunicacaoInternaVO().getResponsavel().getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// } finally {
	// arquivoHelper = null;
	// }
	// }
	//
	// public void paintConsultaAluno(OutputStream out, Object data) throws
	// Exception {
	// PessoaVO pessoaVO = new PessoaVO();
	// ArquivoHelper arquivoHelper = new ArquivoHelper();
	// try {
	// if (!getListaConsultaAluno().isEmpty()) {
	// pessoaVO = (PessoaVO) getListaConsultaAluno().get((Integer) data);
	// arquivoHelper.renderizarImagemNaTela(out, pessoaVO.getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// }
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// } finally {
	// arquivoHelper = null;
	// pessoaVO = null;
	// }
	// }
	//
	// public void paintConsultaProfessor(OutputStream out, Object data) throws
	// Exception {
	// PessoaVO pessoaVO = (PessoaVO) getListaConsultaAluno().get((Integer)
	// data);
	// ArquivoHelper arquivoHelper = new ArquivoHelper();
	// try {
	// arquivoHelper.renderizarImagemNaTela(out, pessoaVO.getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// } finally {
	// arquivoHelper = null;
	// pessoaVO = null;
	// }
	// }
	//
	// public void paintConsulta(OutputStream out, Object data) throws Exception
	// {
	// ComunicacaoInternaVO comunicacaoInternaVO = (ComunicacaoInternaVO)
	// getListaConsulta().get((Integer) data);
	// ArquivoHelper arquivoHelper = new ArquivoHelper();
	// try {
	// arquivoHelper.renderizarImagemNaTela(out,
	// comunicacaoInternaVO.getResponsavel().getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// } finally {
	// arquivoHelper = null;
	// comunicacaoInternaVO = null;
	// }
	// }
	//
	// public void paintProfessorAluno(OutputStream out, Object data) throws
	// Exception {
	// ArquivoHelper arquivoHelper = new ArquivoHelper();
	// try {
	// if (getDestinatarioPR()) {
	// arquivoHelper.renderizarImagemNaTela(out,
	// getComunicacaoInternaVO().getProfessor().getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// } else if (getDestinatarioAL()) {
	// arquivoHelper.renderizarImagemNaTela(out,
	// getComunicacaoInternaVO().getAluno().getArquivoImagem(),
	// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
	// "foto_usuario.jpg");
	// }
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// } finally {
	// arquivoHelper = null;
	// }
	// }
	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ComunicacaoInterna</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Editar Comunicação Interna", "Editando");
		ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicacaoInternaItem");
		obj.setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getFacadeFactory().getComunicacaoInternaFacade().carregarDados(obj, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setComunicacaoInternaVO(obj);
		if (getTipoEntradaSaida().equals("saida") || getTipoEntradaSaida().equals("saidaVisao")) {
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.TRUE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.TRUE);
		} else {
			obj.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(obj.getMensagem(), getUsuarioLogado().getPessoa()));
			confirmarLeitura();
			setNovoComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.FALSE);
			if (getComunicacaoInternaVO().getTipoComunicadoInterno().equals("RE")) {
				setResponderComunicado(Boolean.TRUE);
				setLerComunicado(Boolean.FALSE);
				setApresentarBotaoResponder(Boolean.TRUE);
			}else{				
//				if(getVisaoAlunoControle() != null || getVisaoCoordenadorControle() != null || getVisaoProfessorControle() != null){
//					setApresentarBotaoResponder(getPermitirResponderComunicadoInternoSomenteLeitura() && 
//							(getComunicacaoInternaVO().getTipoRemetente().equals("AL") || getComunicacaoInternaVO().getTipoRemetente().equals("RF")
//									|| getComunicacaoInternaVO().getTipoRemetente().equals("PR") || getComunicacaoInternaVO().getTipoRemetente().equals("CO")));
//				}else{
//					setApresentarBotaoResponder(false);
//				}
			}
			if (getComunicacaoInternaVO().getTipoComunicadoInterno().equals("LE")) {
				setResponderComunicado(Boolean.FALSE);
				setLerComunicado(Boolean.TRUE);				
			}
		}
		if (obj.getTipoComunicadoInterno().equals("") || obj.getTipoComunicadoInterno().equals("MU")) {
			setTipoMural(false);
		} else {
			setTipoMural(true);
		}
	
		verificarTipoOrigemComunicacaoInterna();
		
		if (obj.getTipoDestinatario().equals("FU")) {
			setDestinatarioFU(true);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioCO(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("AL")) {
			setDestinatarioFU(false);
			setDestinatarioAL(true);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("PR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioCO(false);
			setDestinatarioPR(true);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("CA")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(true);
			setDestinatarioDE(false);
			setDestinatarioCO(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("DE")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(true);
			setDestinatarioAR(false);
			setDestinatarioCO(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("AR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(true);
			setDestinatarioCO(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("TU")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioCO(false);
			setDestinatarioTU(true);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("TO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioCO(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(true);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("CO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(true);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("TD")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(true);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("TF")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTF(true);
			setDestinatarioTR(false);
		} else if (obj.getTipoDestinatario().equals("TR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTF(false);
			setDestinatarioTR(true);
		}
		inicializarListasSelectItemTodosComboBox();
		setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Editar Comunicação Interna", "Editando");
		setMensagemID("msg_dados_editar");
		if(getNomeTelaAtual().contains("homeAdministrador.xhtml")) {
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaForm");
	}

	private void verificarTipoOrigemComunicacaoInterna() {
		if(getComunicacaoInternaVO().getTipoOrigemComunicacaoInternaEnum().equals((TipoOrigemComunicacaoInternaEnum.ITEM_REQUERIMENTO_INTERACAO))) {
			setApresentarBotaoAcessarRequerimento(true);
		}else {
			setApresentarBotaoAcessarRequerimento(false);
		}
	}

	public void editarComunicacaoInternaVindoOutraTela(ComunicacaoInternaVO obj) throws Exception {
		obj.setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getFacadeFactory().getComunicacaoInternaFacade().carregarDados(obj, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setComunicacaoInternaVO(obj);
		if (getTipoEntradaSaida().equals("saida")) {
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.TRUE);
		} else {
			setNovoComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.FALSE);
			if (getComunicacaoInternaVO().getTipoComunicadoInterno().equals("RE")) {
				setResponderComunicado(Boolean.TRUE);
				setLerComunicado(Boolean.FALSE);
			}
			if (getComunicacaoInternaVO().getTipoComunicadoInterno().equals("LE")) {
				setResponderComunicado(Boolean.FALSE);
				setLerComunicado(Boolean.TRUE);
			}
		}
		
		verificarTipoOrigemComunicacaoInterna();
		
		if (obj.getTipoDestinatario().equals("FU")) {
			setDestinatarioFU(true);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioCO(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
		}
		setMensagemID("msg_dados_editar");
	}

	public void editarVisao() throws Exception {
		editar();
		VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		if (visaoAlunoControle != null) {			
			visaoAlunoControle.inicializarMenuNovoRecado();
		}
//		
		setMensagemID("msg_dados_visualizar");

	}
	
	public String redirecionarTelaRequerimento() throws Exception {
		context().getExternalContext().getSessionMap().put("idRequerimento", getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna());
		String caminhoRedirecionamento = "";
		if(getComunicacaoInternaVO().getTipoDestinatario().equals("AL")) {
			caminhoRedirecionamento = "requerimentoAluno.xhtml";
		}else if(getComunicacaoInternaVO().getTipoDestinatario().equals("PR")) {
			caminhoRedirecionamento = "requerimentoProfessorForm.xhtml";
		}else if(getComunicacaoInternaVO().getTipoDestinatario().equals("FU")) {
			caminhoRedirecionamento = "/visaoAdministrativo/academico/requerimentoForm.xhtml";
		}else if(getComunicacaoInternaVO().getTipoDestinatario().equals("CO")) {
			caminhoRedirecionamento = "requerimentoCoordenadorForm.xhtml";
		}
		
		if(!Uteis.isAtributoPreenchido(caminhoRedirecionamento)) {
			 throw new Exception("Perfil de acesso não encontrado!");
		}
		removerControleMemoriaFlash("ComunicacaoInternaControle");
		removerControleMemoriaTela("ComunicacaoInternaControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao(caminhoRedirecionamento);
	}

	public void verListaDestinatario() {
		try {
			geraComunicadoInternoDestinatario();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getPaintArquivoMarketing1()  {
		try {
			return getFacadeFactory().getArquivoHelper().renderizarAnexo(getComunicacaoInternaVO().getArquivoAnexo(), PastaBaseArquivoEnum.ARQUIVO.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "imagemModelo.jpg", false);
		} catch (Exception e) {
			return"";
		}
	}
	
	public void paintArquivoMarketing(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		try {
			arquivoHelper.renderizarImagemNaTela(out, getComunicacaoInternaVO().getArquivoAnexo(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "imagemModelo.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
		}
	}

	public void paintArquivo(OutputStream out, Object data) throws Exception {
		ArquivoHelper arquivoHelper = new ArquivoHelper();
		try {
			if (getComunicacaoInternaVO().getArquivoAnexo().getNome().contains(".jpg") || getComunicacaoInternaVO().getArquivoAnexo().getNome().contains(".png") || getComunicacaoInternaVO().getArquivoAnexo().getNome().contains(".gif")) {
				arquivoHelper.renderizarImagemNaTela(out, getComunicacaoInternaVO().getArquivoAnexo(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "imagemModelo.jpg");
				setMostrarImagem(Boolean.TRUE);
			} else {
				setMostrarImagem(Boolean.FALSE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
		}
	}

	public void realizarUpload(FileUploadEvent uploadEvent) {

		try {
			// if (uploadEvent.getFile().getFileName().contains(".jpg") ||
			// uploadEvent.getFile().getFileName().contains(".png") ||
			// uploadEvent.getFile().getFileName().contains(".gif")) {
			getFacadeFactory().getArquivoHelper().upLoadLista(uploadEvent, getComunicacaoInternaVO().getListaArquivosAnexo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, true, true, getUsuarioLogado());
			// setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getComunicacaoInternaVO().getArquivoAnexo(),
			// PastaBaseArquivoEnum.IMAGEM_TMP.getValue(),
			// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
			// "imagemModelo.jpg"));
			// setMostrarImagem(Boolean.TRUE);
			// } else {
			// getFacadeFactory().getArquivoHelper().upLoad(uploadEvent,
			// getComunicacaoInternaVO().getArquivoAnexo(),
			// getConfiguracaoGeralPadraoSistema(),
			// PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			// setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getComunicacaoInternaVO().getArquivoAnexo(),
			// PastaBaseArquivoEnum.ARQUIVO_TMP.getValue(),
			// getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(),
			// "imagemModelo.jpg"));
			// setMostrarImagem(Boolean.FALSE);
			// }
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// uploadEvent = null;
		}
	}

	public String getDownloadAnexo() {
		try {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ARQUIVO.getValue() + "/" + getComunicacaoInternaVO().getArquivoAnexo().getNome();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String getDownloadAnexoLista() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoAnexo");
			if (arquivoVO.getOrigem().trim().equals("NFE")) {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.NFE.getValue() + "/" + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + "/" + arquivoVO.getNome();
			} else {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ARQUIVO.getValue() + "/" + arquivoVO.getNome();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void realizarDownloadAnexoLista() {
		context().getExternalContext().getSessionMap().put("arquivoVO", (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoAnexo"));
	}

	public Boolean getApresentarAnexarArquivo() {
		return !getComunicacaoInternaVO().getTipoLeituraObrigatoria() && getComunicacaoInternaVO().getNovoObj();
	}

	public Boolean getApresentarDigitarMensagem() {
		return !getComunicacaoInternaVO().getTipoMarketing();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ComunicacaoInterna</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			// if (getComunicacaoInternaVO().getEnviarEmail()) {
			// getComunicacaoInternaVO().setMensagem(getMensagemFormatada(getComunicacaoInternaVO().getMensagem()));
			// }
			if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
				getComunicacaoInternaVO().setTipoRemetente("PR");
			} else if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
				getComunicacaoInternaVO().setTipoRemetente("AL");
			} else if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				getComunicacaoInternaVO().setTipoRemetente("CO");
			} else {
				getComunicacaoInternaVO().setTipoRemetente("FU");
			}
			if (comunicacaoInternaVO.isNovoObj().booleanValue()) {
				if (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
					// geraComunicadoInternoDestinatario();
					throw new Exception("Favor adicione ao menos 1 destinatário!");
				}
				registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "ComunicacaoInternaControle", "Inicializando Incluir Comunicação Interna", "Incluindo");
				// caso seja CI de resposta
				if ((comunicacaoInternaVO.getComunicadoInternoOrigem() != null) && !comunicacaoInternaVO.getComunicadoInternoOrigem().getCodigo().equals(0)) {
					// marcar CI origem como lida e respondida
					atualizarLeituraResposta('2');
				}
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, getProgressBarVO().getUsuarioVO(), getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO(), true);
				registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "ComunicacaoInternaControle", "Finalizando Incluir Comunicação Interna", "Incluindo");
			} else {
				registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "ComunicacaoInternaControle", "Inicializando Alterar Comunicação Interna", "Alterar");
				getFacadeFactory().getComunicacaoInternaFacade().alterar(comunicacaoInternaVO, getProgressBarVO().getUsuarioVO(), getConfiguracaoGeralPadraoSistema());
				registrarAtividadeUsuario(getProgressBarVO().getUsuarioVO(), "ComunicacaoInternaControle", "Finalizando Alterar Comunicação Interna", "Alterar");
			}
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.TRUE);
			setAbaAtiva("destinatario");
			getProgressBarVO().getSuperControle().setMensagemID("msg_msg_enviados", Uteis.SUCESSO, true);
			return "editar";
		} catch (Exception e) {
			getProgressBarVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "editar";
		} finally {
			getProgressBarVO().setForcarEncerramento(true);
		}
	}

	public String getMensagemFormatada(String mensagem) throws Exception {
		String temp = getConfiguracaoGeralPadraoSistema().getMensagemPadrao();
		if (temp.equals("")) {
			return mensagem;
		}
		String caminho = getCaminhoPastaWeb();
		temp = temp.replaceAll("http://localhost:8080/SEI/", caminho);
		temp = temp.replace("<TEXTO PADRAO>", mensagem);
		return temp;
	}

	public void gravarRecadoProfessor() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getComunicacaoInternaVO().setTipoRemetente("PR");
			if (comunicacaoInternaVO.isNovoObj().booleanValue()) {
				if (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
					// geraComunicadoInternoDestinatario();
					throw new Exception("Favor adicione ao menos 1 destinatário!");
				}
				if ((comunicacaoInternaVO.getComunicadoInternoOrigem() != null) && !comunicacaoInternaVO.getComunicadoInternoOrigem().getCodigo().equals(0)) {
					// marcar CI origem como lida e respondida
					atualizarLeituraResposta('2');
				}
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
				// caso seja CI de resposta
			} else {
				getFacadeFactory().getComunicacaoInternaFacade().alterar(comunicacaoInternaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.TRUE);
			consultarTodasSaidaVisao();
			setApresentarRichMensagem("PF('panelMsgEnviada').show()");
			setMensagemID("msg_msg_enviados", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setApresentarRichMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarRecadoAluno() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			comunicacaoInternaVO.setTipoComunicadoInterno("RE");
			if (getUsuarioLogado().getIsApresentarVisaoPais()) {
				getComunicacaoInternaVO().setTipoRemetente("RL");
			} else {
				getComunicacaoInternaVO().setTipoRemetente("AL");
			}
			if (comunicacaoInternaVO.isNovoObj().booleanValue()) {
				if (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
					// geraComunicadoInternoDestinatario();
					throw new Exception("Favor adicione ao menos 1 destinatário!");
				}
				// caso seja CI de resposta
				if ((comunicacaoInternaVO.getComunicadoInternoOrigem() != null) && !comunicacaoInternaVO.getComunicadoInternoOrigem().getCodigo().equals(0)) {
					// marcar CI origem como lida e respondida
					atualizarLeituraResposta('2');
				}
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
			} else {
				getFacadeFactory().getComunicacaoInternaFacade().alterar(comunicacaoInternaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			setNovoComunicado(Boolean.TRUE);
			setLerComunicado(Boolean.TRUE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.FALSE);
			consultarTodasEntradaLimiteAluno();
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarRecadoCoordenador() {
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			getComunicacaoInternaVO().setTipoRemetente("CO");
			if (comunicacaoInternaVO.isNovoObj().booleanValue()) {
				if (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
					// geraComunicadoInternoDestinatario();
					throw new Exception("Favor adicione ao menos 1 destinatário!");
				}
				if ((comunicacaoInternaVO.getComunicadoInternoOrigem() != null) && !comunicacaoInternaVO.getComunicadoInternoOrigem().getCodigo().equals(0)) {
					// marcar CI origem como lida e respondida
					atualizarLeituraResposta('2');
				}
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
				// caso seja CI de resposta
			} else {
				getFacadeFactory().getComunicacaoInternaFacade().alterar(comunicacaoInternaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.TRUE);
			consultarTodasEntradaLimiteCoordenador();
			setApresentarRichMensagem("PF('panelMsgEnviada').show()");
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setApresentarRichMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void confirmarLeitura() throws Exception {
		try {			
//			if(getUsuarioLogado().getIsApresentarVisaoProfessor() && !getComunicacaoInternaVO().getMensagemLidaVisaoAlunoCoordenador()) {
//				getVisaoProfessorControle().inicializarQtdeCaixaEntradaUsuario();
//			}
			if(!getComunicacaoInternaVO().getMensagemLidaVisaoAlunoCoordenador()) {
				getFacadeFactory().getComunicacaoInternaFacade().registrarLeituraComunicadoInterno(getComunicacaoInternaVO(), getUsuarioLogado().getPessoa(), getUsuarioLogado());
				verificarQtdeMensagemCaixaEntrada();
			}
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				removerControleMemoriaFlashTela("PainelGestorAdministrativoControle");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String editarComunicadoTopoVisao() {
		limparMensagem();
		editarComunicadoVisao();
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			verificarQtdeMensagemCaixaEntrada();
			return Uteis.getCaminhoRedirecionamentoNavegacao("recadoAluno.xhtml");
		}
		
		if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			verificarQtdeMensagemCaixaEntrada();
			return Uteis.getCaminhoRedirecionamentoNavegacao("recadosCoordenador.xhtml");		
		}
		return "";
	} 
	public void editarComunicadoVisao()  {
		try {
			setComunicacaoInternaVO(new ComunicacaoInternaVO());		
			getListaConsulta().clear();
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) context().getExternalContext().getRequestMap().get("comunicacaoInternaItem");
			getFacadeFactory().getComunicacaoInternaFacade().carregarDados(obj, getUsuarioLogado());
			obj.setRemover(true);	
			getListaConsulta().add(obj);
			obj.setNovoObj(Boolean.FALSE);
			obj.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(obj.getMensagem(), getUsuarioLogado().getPessoa()));
			setComunicacaoInternaVO(obj);
			setNovoComunicado(Boolean.FALSE);
//			setApresentarBotaoResponder(obj.getTipoComunicadoInterno().equals("RE") || obj.getTipoComunicadoInterno().equals("LE"));
			setApresentarBotaoResponder(obj.getTipoComunicadoInterno().equals("RE")
					|| (getPermitirResponderComunicadoInternoSomenteLeitura()
							&& (obj.getTipoRemetente().equals("AL") || obj.getTipoRemetente().equals("RF")
									|| obj.getTipoRemetente().equals("PR") || obj.getTipoRemetente().equals("CO"))));
			setLerComunicado(true);
			setResponderComunicado(false);
			setMensagemLocalizacao("Ler Recado");
		
			setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			
			verificarTipoOrigemComunicacaoInterna();
			
			if (!getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				confirmarLeitura();
				obj.setImagemEnvelope("../../resources/imagens/envelopeAberto1.png");
			}
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAlunoControle != null) {
				getComunicacaoInternaVO().getTipoRemetente();
				visaoAlunoControle.inicializarMenuLerRecado();
				if (getComunicacaoInternaVO().getMensagem().contains("#NOMEALUNO")) {
					getComunicacaoInternaVO().setMensagem(Uteis.trocarHashTag("#NOMEALUNO", visaoAlunoControle.getMatricula().getAluno().getNome(), getComunicacaoInternaVO().getMensagem()));
				}
			}
//			VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
//			if (visaoProfessorControle != null) {
//				visaoProfessorControle.inicializarMenuLerRecado();
//			}
//			VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
//			if (visaoCoordenadorControle != null) {
//				visaoCoordenadorControle.inicializarMenuLerRecado();
//			}
			// setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
			obj.setMensagem(Uteis.retiraTagsScript(obj.getMensagem()));
			setMensagemDetalhada("msg_dados_visualizar", "");
			if (!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getMensagem()) && getComunicacaoInternaVO().getMensagem().contains("../../resources")) {
				getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagem().replace("../../resources", "../resources"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosResponderComunicado() {
		setResponderComunicado(Boolean.TRUE);
		setLerComunicado(Boolean.FALSE);
		setNovoComunicado(false);
		getComunicacaoInternaVO().setListaArquivosAnexo(new ArrayList<ArquivoVO>(0));
		getComunicacaoInternaVO().setNovoObj(true);
//		setWidth(700);
//		setHeigth(600);
		setApresentarBotaoResponder(Boolean.FALSE);
		setMensagemLocalizacao("Responder Recado");
		limparMensagem();
		try {
			setComunicacaoInternaResposta(getFacadeFactory().getComunicacaoInternaFacade().inicializarDadosRespotaComunicado(getComunicacaoInternaVO(), getUsuarioLogado()));
			getComunicacaoInternaResposta().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				setApresentarBotaoEnviarResposta(Boolean.TRUE);
				setComunicacaoInternaVO(getComunicacaoInternaResposta());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void responderLeituraObrigatoriaOuMarketing(){
		try {
			if(!getUsuarioLogado().getPermiteSimularNavegacaoAluno()){
				getFacadeFactory().getComunicacaoInternaFacade().responder(getComunicacaoInternaResposta(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				consultarTodasEntradaMarketingLeituraObrigatoriaAluno();
			}else{
				setAbrirRichModalMensagem(false);
				setLerComunicado(false);
				setResponderComunicado(false);
				setApresentarBotaoResponder(false);
				setAbrirRichModalMarketing(false);
				setComunicacaoInternaVO(new ComunicacaoInternaVO());
			}			
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void responder() throws Exception {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getComunicacaoInternaResposta().setMensagem(getComunicacaoInternaVO().getMensagem());
			}
			getComunicacaoInternaResposta().setListaArquivosAnexo(getComunicacaoInternaVO().getListaArquivosAnexo());
			getFacadeFactory().getComunicacaoInternaFacade().responder(getComunicacaoInternaResposta(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setComunicacaoInternaVO(new ComunicacaoInternaVO());
			setNovoComunicado(Boolean.FALSE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setVerListaDestinatarios(Boolean.FALSE);
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				consultarTodasSaida();
			}else{
				consultarTodasSaidaVisao();
			}
			setComunicacaoInternaResposta(new ComunicacaoInternaVO());			
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void geraComunicadoInternoDestinatario() throws Exception {
		getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(getFacadeFactory().getComunicacaoInternaFacade().consultaComunicadoInternoDestinatario(getComunicacaoInternaVO(), false, getUsuarioLogado()));
		setNovoComunicado(Boolean.FALSE);
		setLerComunicado(Boolean.FALSE);
		setResponderComunicado(Boolean.FALSE);
		setVerListaDestinatarios(Boolean.TRUE);
		setAbaAtiva("destinatario");
	}

	private void atualizarLeituraResposta(char lr) throws Exception {
		if (comunicacaoInternaVO.getComunicadoInternoOrigem().getComunicadoInternoDestinatarioVOs() == null) {
			return;
		}
		Iterator i = comunicacaoInternaVO.getComunicadoInternoOrigem().getComunicadoInternoDestinatarioVOs().iterator();
		ComunicadoInternoDestinatarioVO CIDVO = null;
		Integer codigoPessoa = getUsuarioLogado().getPessoa().getCodigo();
		while (i.hasNext()) {
			ComunicadoInternoDestinatarioVO CID = (ComunicadoInternoDestinatarioVO) i.next();
			if (CID.getDestinatario().getCodigo().equals(codigoPessoa)) {
				CIDVO = CID;
				break;
			}
		}
		if (CIDVO != null) {
			if (lr == 'L') {
				CIDVO.setCiJaLida(true);
			} else if (lr == 'R') {
				CIDVO.setCiJaRespondida(true);
			} else if (lr == '2') {
				CIDVO.setCiJaLida(true);
				CIDVO.setCiJaRespondida(true);
			}
		}
		//getFacadeFactory().getComunicacaoInternaFacade().alterar(comunicacaoInternaVO.getComunicadoInternoOrigem(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
//		MuralControle obj = (MuralControle) context().getExternalContext().getSessionMap().get("MuralControle");
//		obj.montarListaSelectItemMensagem();
	}

	public boolean getTipoComunicadoMural() {
		return getComunicacaoInternaVO().getTipoComunicadoInterno().equals("MU") || getComunicacaoInternaVO().getTipoComunicadoInterno().equals("");
	}

	public void gerarXMLMural() throws Exception {
		// criaXMLMural();
		if (!getComunicacaoInternaVO().getTipoComunicadoInterno().equals("MU")) {
			return;
		}
		FileWriter fw = new FileWriter(getCaminhoBase() + "/mural.xml");
		BufferedWriter bf = new BufferedWriter(fw);
		List resultadoConsulta = getFacadeFactory().getComunicacaoInternaFacade().consultarParaMuralComunicacaoInterna(mensagem, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		Iterator i = resultadoConsulta.iterator();
		bf.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		bf.newLine();
		bf.write("<mural>");
		bf.newLine();
		while (i.hasNext()) {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) i.next();
			String msg = obj.getMensagem();
			if (msg.length() > 200) {
				msg = msg.substring(0, 199);
			}
			bf.write("    <mensagem><![CData[" + msg + "]]></mensagem>\n");
			// bf.newLine();
		}
		bf.write("</mural>");
		bf.close();
	}

	public String getMesConsulta_Apresentar() {
		if (getMesConsulta().equals("01")) {
			return "Janeiro";
		} else if (getMesConsulta().equals("02")) {
			return "Fevereiro";
		} else if (getMesConsulta().equals("03")) {
			return "Março";
		} else if (getMesConsulta().equals("04")) {
			return "Abril";
		} else if (getMesConsulta().equals("05")) {
			return "Maio";
		} else if (getMesConsulta().equals("06")) {
			return "Junho";
		} else if (getMesConsulta().equals("07")) {
			return "Julho";
		} else if (getMesConsulta().equals("08")) {
			return "Agosto";
		} else if (getMesConsulta().equals("09")) {
			return "Setembro";
		} else if (getMesConsulta().equals("10")) {
			return "Outubro";
		} else if (getMesConsulta().equals("11")) {
			return "Novembro";
		} else {
			return "Dezembro";
		}
	}

	public List<SelectItem>getListaSelectItemMesesDoAno() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("01", "Janeiro"));
		objs.add(new SelectItem("02", "Fevereiro"));
		objs.add(new SelectItem("03", "Março"));
		objs.add(new SelectItem("04", "Abril"));
		objs.add(new SelectItem("05", "Maio"));
		objs.add(new SelectItem("06", "Junho"));
		objs.add(new SelectItem("07", "Julho"));
		objs.add(new SelectItem("08", "Agosto"));
		objs.add(new SelectItem("09", "Setembro"));
		objs.add(new SelectItem("10", "Outubro"));
		objs.add(new SelectItem("11", "Novembro"));
		objs.add(new SelectItem("12", "Dezembro"));
		return objs;
	}

	public Integer getAnoAtual() {
		return Uteis.getAnoData(new Date());
	}

	// Consultas
	public void consultarTodasEntrada() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setMostrarBotoes(false);
		setTipoEntradaSaida("entrada");
		setLidas(null);
		setRespondidas(null);
		consultar();
	}

//	public void consultarTodasEntradaTotal() {
//		setMostrarBotoes(false);
//		setTipoEntradaSaida("entrada");
//		setLidas(null);
//		setRespondidas(null);
//		consultarTodas();
//		setNovoComunicado(false);
//	}
//
//	public String consultarTodasEntradaLimiteAlunoVisaoPai() throws Exception {
//		setApresentarBotaoAcessarRequerimento(Boolean.FALSE);
//		setVisualizarMensagemAlunoPorResponsavelLegal(true);
//		setResponderComunicado(false);
//		setLerComunicado(false);
//		setApresentarBotaoResponder(false);
//		setAbrirRichModalLeituraObrigatoria(false);
//		setAbrirRichModalMarketing(false);
//		setNovoComunicado(false);
//		setAbrirRichModalMensagem(false);
//		setHabilitarMsgErro(Boolean.FALSE);
//		return consultarTodasMensagemCaixaEntradaParaAlunoOuPai();
//	}

	public String consultarTodasEntradaLimiteAluno() throws Exception {		
		return consultarTodasMensagemCaixaEntradaParaAlunoOuPai();
	}

	public String consultarTodasMensagemCaixaEntradaParaAlunoOuPai() throws Exception {
		try {
			setApresentarBotaoAcessarRequerimento(Boolean.FALSE);
			setVisualizarMensagemAlunoPorResponsavelLegal(false);
			setResponderComunicado(false);
			setLerComunicado(false);
			setApresentarBotaoResponder(false);
			setAbrirRichModalLeituraObrigatoria(false);
			setAbrirRichModalMarketing(false);
			setAbrirRichModalMensagem(false);
			setNovoComunicado(false);
			setHabilitarMsgErro(Boolean.FALSE);
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Todas Entrada Limite Aluno", "Consultando");
			setTipoEntradaSaida("limite");
			setComunicadoSaida(Boolean.FALSE);
			setMensagemLocalizacao("Caixa de Entrada");
			inicializarDadosFiltros();
			if (getConsultarTodasMensagens()) {
				inicializarDadosControleConsultaOtimizado();
				if (!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina())) {
					getControleConsultaOtimizado().setLimitePorPagina(10);
				}
			}
			consultarTodasEntradasAluno();
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAlunoControle != null) {
				visaoAlunoControle.inicializarMenuRecado();
				setMatricula(visaoAlunoControle.getMatricula().getMatricula());				
				return Uteis.getCaminhoRedirecionamentoNavegacao("recadoAluno.xhtml");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Todas Entrada Limite Aluno", "Consultando");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String consultarTodasEntradasAluno() throws Exception {
		setRemoverTodas(Boolean.FALSE);
		getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoAluno(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData(), false);		
//		if(getLoginControle().getPermissaoAcessoMenuVO().getComunicacaoInterna()) {
//		if (!getVisualizarMensagemAlunoPorResponsavelLegal()) {
//			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoAluno(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData(), false));
//			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeEntradaVisaoAluno(getUsuarioLogado().getPessoa().getCodigo(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData()));
//			if (getUsuarioLogado().getIsApresentarVisaoPais()) {
//				setQtdMensagemCaixaEntradaResponsavel(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasVisaoPais(getUsuarioLogado().getPessoa().getCodigo()));
//			} else {
//				setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasVisaoAluno(getUsuarioLogado().getPessoa().getCodigo()));
//			}
//			
//		} else {
//			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoAluno(getUsuarioLogado().getIsApresentarVisaoPais() ? getUsuarioLogado().getPessoa().getCodigo() : getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), getLimiteRecado(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData(), false));
//			setQtdMensagemCaixaEntradaAluno(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasVisaoAluno(getUsuarioLogado().getIsApresentarVisaoPais() ? getUsuarioLogado().getPessoa().getCodigo() : getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo()));
//			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeEntradaVisaoAluno(getUsuarioLogado().getPessoa().getCodigo(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData()));
//		}
//		}
		if (getControleConsultaOtimizado().getListaConsulta().size() >= 0) {
			setExisteConsulta(Boolean.TRUE);
		} else {
			setExisteConsulta(Boolean.FALSE);
		}
		return "";
	}

	/**
	 * Método responsavel por realizar uma consulta quando tipoMarketing =
	 * 'TRUE' or tipoLeitruaObrigatoria = 'TRUE', caso tipoMarketing = 'TRUE'
	 * abre o RichModal de Marketing senao abre o RichModal
	 * tipoLeituraObrigatoria
	 */
	
	public void marcarComoLidasLeituraObrigatoriaOuMarketing() {
		try {
			if(!getUsuarioLogado().getPermiteSimularNavegacaoAluno()){
				getFacadeFactory().getComunicacaoInternaFacade().registrarLeituraComunicadoInterno(getComunicacaoInternaVO(), getUsuarioLogado().getPessoa(), getUsuarioLogado());
				consultarTodasEntradaMarketingLeituraObrigatoriaAluno();
			}else{
				setAbrirRichModalMensagem(false);
				setLerComunicado(false);
				setResponderComunicado(false);
				setApresentarBotaoResponder(false);
				setAbrirRichModalMarketing(false);
				setComunicacaoInternaVO(new ComunicacaoInternaVO());
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getAcaoModalMensagemMaketingOuLeituraObrigatoria(){
		if(getAbrirRichModalMarketing()){
			return "PF('panelMarketing').show(); PF('panelMensagem').hide();";
		}
		if(getAbrirRichModalMensagem()){
			return "PF('panelMensagem').show(); PF('panelMarketing').hide();";
		}
		return "PF('panelMarketing').hide(); PF('panelMensagem').hide();";
	}
	

	public void consultarTodasEntradaMarketingLeituraObrigatoriaAluno() {
		Integer indexMarketing = 1;
		Integer indexLeituraObrigatoria = 1;
		setAbrirRichModalMensagem(false);
		setLerComunicado(false);
		setResponderComunicado(false);
		setApresentarBotaoResponder(false);
		setAbrirRichModalMarketing(false);
		setComunicacaoInternaVO(new ComunicacaoInternaVO());
		try {
			List<ComunicacaoInternaVO> objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteMarketingLeituraObrigatoria(getUsuarioLogado().getPessoa().getCodigo(), 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!objs.isEmpty()) {

				for (ComunicacaoInternaVO comunicacaoInterna : objs) {
					if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && comunicacaoInterna.getMensagem().contains("../../resources/")) {
						String replace = "";
						replace = comunicacaoInterna.getMensagem().replaceAll("../../resources/", "../resources/");
						comunicacaoInterna.setMensagem(replace);
					}
					comunicacaoInterna.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(comunicacaoInterna.getMensagem(), getUsuarioLogado().getPessoa()));
					if (comunicacaoInterna.getTipoMarketing()) {
						if (indexMarketing == 1) {
							setComunicacaoInternaVO(comunicacaoInterna);
							if (getComunicacaoInternaVO().getListaArquivosAnexo().isEmpty() && getComunicacaoInternaVO().getArquivoAnexo().getCodigo()>0) {
								getComunicacaoInternaVO().getListaArquivosAnexo().add(getComunicacaoInternaVO().getArquivoAnexo());
							}else{
								//getComunicacaoInternaVO().setListaArquivosAnexo(getFacadeFactory().getArquivoFacade().consultar)
							}
							if (!getComunicacaoInternaVO().getListaArquivosAnexo().isEmpty()) {
								ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
								for (ArquivoVO arquivoVO : getComunicacaoInternaVO().getListaArquivosAnexo()) {
									getComunicacaoInternaVO().setArquivoAnexo(arquivoVO);
									setAbrirRichModalMarketing(true);
									setLerComunicado(true);
									setApresentarBotaoResponder(comunicacaoInterna.getTipoComunicadoInterno().equals("RE"));
									File fileList = getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(getComunicacaoInternaVO().getArquivoAnexo(), config);
									BufferedImage bufferedImage = ImageIO.read(fileList);
									setWidth(bufferedImage.getWidth()+80);
									setHeigth(bufferedImage.getHeight()+100);
								}
							}														
							return;
						}
						indexMarketing++;

					} else if (comunicacaoInterna.getTipoLeituraObrigatoria() || comunicacaoInterna.getTipoComunicadoInterno().equals("MU")) {
						if (indexLeituraObrigatoria == 1) {
							setComunicacaoInternaVO(comunicacaoInterna);							
							if (comunicacaoInterna.getDigitarMensagem()) {
								setAbrirRichModalMensagem(true);
								setLerComunicado(true);
								if (comunicacaoInterna.getTipoComunicadoInterno().equals("MU")) {
									setApresentarBotaoResponder(false);
								} else {
									setApresentarBotaoResponder(comunicacaoInterna.getTipoComunicadoInterno().equals("RE"));
								}
								return;
							}
							
						}
						indexLeituraObrigatoria++;
					}
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void fecharModalTipoMarketing() {
		setAbrirRichModalMarketing(Boolean.FALSE);
	}

	public String alterarDataExibicaoComunicadoInterno() {
		try {getComunicacaoInternaVO().setDataCancelamento(Uteis.getDataJDBC(new Date()));
			getFacadeFactory().getComunicacaoInternaFacade().alterarDataExibicaoComunicadoInterno(getComunicacaoInternaVO().getCodigo(), Uteis.getDataPassada(new Date(), 1), getUsuarioLogado().getCodigo(), getUsuarioLogado());
			setMensagemID("msg_entre_cancelamentoApresentacaoComunicado");
			return "editar";
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return "editar";
		}
	}

	public Boolean getApresentarBotaoCancelarApresentacaoComunicado() {
		return getComunicacaoInternaVO().getTipoLeituraObrigatoria() || getComunicacaoInternaVO().getTipoMarketing();
	}

	public String getRealizarDefinicaoStyleClassPeriodoExibicao() {
		if (getComunicacaoInternaVO().getTipoMarketing() || getComunicacaoInternaVO().getTipoLeituraObrigatoria()) {
			return "form-control camposObrigatorios";
		}
		return "form-control campos";
	}

	/**
	 * Método responsavel por alterar o campo tipoMarketing quando for
	 * apresentado o Marketing
	 * 
	 * @autor Carlos
	 */
	public void executarMarketingQuandoPessoaLogar() {
		try {
			getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarComunicadoInternoDestinatarioMarketingLida(getComunicacaoInternaVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo());
			setAbrirRichModalMarketing(false);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsável por alterar o campo tipoLeituraObrigatória e a
	 * Subordinada alterando ciJaLida = 'TRUE'
	 * 
	 * @Autor Carlos
	 */
	public void executarLeituraObrigatoriaQuandoPessoaLogar() {
		try {
			if (getComunicacaoInternaVO().getTipoLeituraObrigatoria()) {
				getFacadeFactory().getComunicacaoInternaFacade().alterarTipoLeituraObrigtoriaAPosLeitura(getComunicacaoInternaVO(), getUsuarioLogado());
			}
			if (getComunicacaoInternaVO().getTipoMarketing()) {
				executarMarketingQuandoPessoaLogar();
			}
			setAbrirRichModalMensagem(false);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarfechamentoRichModalLeituraObrigatoria() {
		setAbrirRichModalLeituraObrigatoria(false);
	}

	public String consultarTodasEntradaLimiteProfessor(){
		try {
			setTipoEntradaSaida("limite");
			setVisualizarMensagemAlunoPorResponsavelLegal(false);
			setResponderComunicado(false);
			setLerComunicado(false);
			setApresentarBotaoResponder(false);
			setAbrirRichModalLeituraObrigatoria(false);
			setAbrirRichModalMarketing(false);
			setAbrirRichModalMensagem(false);
			setNovoComunicado(false);
			setComunicadoSaida(false);
			setMensagemLocalizacao("Caixa de entrada");
			inicializarDadosFiltros();
			if (getConsultarTodasMensagens()) {
				inicializarDadosControleConsultaOtimizado();
				if (!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina())) {
					getControleConsultaOtimizado().setLimitePorPagina(10);
				}
			}
			consultarTodasEntradasProfessor();
			getComunicacaoInternaVO().setTipoDestinatario("TU");
//			VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
//			if (visaoProfessorControle != null) {
//				visaoProfessorControle.inicializarMenuRecado();
//				return Uteis.getCaminhoRedirecionamentoNavegacao("recadosProfessor.xhtml");
//			}
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		return "";
	}

	public String consultarTodasEntradasProfessor() throws Exception {
		setRemoverTodas(Boolean.FALSE);
		getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoProfessor(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//		setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasVisaoProfessor(getUsuarioLogado().getPessoa().getCodigo()));
		setExisteConsulta(!getControleConsultaOtimizado().getListaConsulta().isEmpty());
//		getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeEntradaVisaoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData()));
		return "";
	}

	public String consultarTodasEntradaLimiteCoordenador() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Todas Entrada Limite Coordenador", "Consultando");
		setMostrarBotoes(false);
		setTipoEntradaSaida("limite");
		setComunicadoSaida(Boolean.FALSE);
		setVisualizarMensagemAlunoPorResponsavelLegal(false);
		setResponderComunicado(false);
		setLerComunicado(false);
		setApresentarBotaoResponder(false);
		setAbrirRichModalLeituraObrigatoria(false);
		setAbrirRichModalMarketing(false);
		setAbrirRichModalMensagem(false);
		setNovoComunicado(false);
		setMensagemLocalizacao("Caixa de entrada");
		inicializarDadosFiltros();
		if (getConsultarTodasMensagens()) {
			inicializarDadosControleConsultaOtimizado();
			if (!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina())) {
				getControleConsultaOtimizado().setLimitePorPagina(10);
			}
		}
		consultarTodasEntradasCoordenador();
//		VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Todas Entrada Limite Coordenador", "Consultando");
//		if (visaoCoordenadorControle != null) {
//			visaoCoordenadorControle.inicializarMenuRecado();
//			return Uteis.getCaminhoRedirecionamentoNavegacao("recadosCoordenador.xhtml");
//		}
		return "";
	}

	public String consultarTodasEntradasCoordenador() throws Exception {
		setRemoverTodas(Boolean.FALSE);
		getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteVisaoCoordenador(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado().getTipoUsuario(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
		setExisteConsulta(!getControleConsultaOtimizado().getListaConsulta().isEmpty());		
		return "consultar";
	}

	public void consultarLidas() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setMostrarBotoes(false);
		setTipoEntradaSaida("entrada");
		setLidas(Boolean.TRUE);
		setRespondidas(null);
		consultar();
	}

	public void consultarNaoLidas() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setMostrarBotoes(false);
		setTipoEntradaSaida("entrada");
		setLidas(Boolean.FALSE);
		setRespondidas(null);
		consultar();
	}

	public void consultarNaoRespondida() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setMostrarBotoes(false);
		setTipoEntradaSaida("entrada");
		setLidas(null);
		setRespondidas(Boolean.FALSE);
		consultar();
	}

	public void consultarRespondida() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setMostrarBotoes(false);
		setTipoEntradaSaida("entrada");
		setLidas(null);
		setRespondidas(Boolean.TRUE);
		consultar();
	}

	public void consultarTodasSaida() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setTipoEntradaSaida("saida");
		setTipoSaidaConsulta("");
		consultar();
	}

	public void consultarTodasSaidaVisao() throws Exception {
		inicializarDadosFiltros();
		inicializarDadosControleConsultaOtimizado();
		setResponderComunicado(false);
		setLerComunicado(false);
		setApresentarBotaoResponder(false);
		setApresentarBotaoAcessarRequerimento(Boolean.FALSE);
		setAbrirRichModalLeituraObrigatoria(false);
		setAbrirRichModalMarketing(false);
		setAbrirRichModalMensagem(false);
		setTipoEntradaSaida("saidaVisao");
		setNovoComunicado(false);

		setHabilitarMsgErro(Boolean.FALSE);
		setTipoSaidaConsulta("");
		setMensagemLocalizacao("Caixa de Saída");
		setExisteConsulta(Boolean.FALSE);
		consultar();
		VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		if (visaoAlunoControle != null) {
			visaoAlunoControle.inicializarMenuRecado();
		}
//		VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
//		if (visaoProfessorControle != null) {
//			visaoProfessorControle.inicializarMenuRecado();
//		}
//		VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
//		if (visaoCoordenadorControle != null) {
//			visaoCoordenadorControle.inicializarMenuRecado();
//		}
	}

	public void consultarExigeResposta() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setTipoEntradaSaida("saida");
		setTipoSaidaConsulta("RE");
		consultar();
	}

	public void consultarSomenteLeitura() {
		inicializarDadosControleConsultaOtimizado();
		inicializarDadosFiltros();
		setTipoEntradaSaida("saida");
		setTipoSaidaConsulta("LE");
		consultar();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ComunicacaoInternaCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
//	public String consultarTodas() {
//		try {
//			super.consultar();
//			setRemoverTodas(Boolean.FALSE);
//			Date dataIni = null;
//			Date dataFim = null;
//			List objs = new ArrayList(0);
//			if (!getUsuarioLogado().getTipoUsuario().equals("VI")) {
//				String tipo = "";
//				if (!getUsuarioLogado().getTipoUsuario().equals("AL") && !getUsuarioLogado().getTipoUsuario().equals("PR") && !getUsuarioLogado().getTipoUsuario().equals("CO")) {
//					tipo = "FU";
//				} else {
//					tipo = getUsuarioLogado().getTipoUsuario();
//				}
//
//				if (getTipoEntradaSaida().equals("entrada")) {
//					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
//					if (getUsuarioLogado().getVisaoLogar().equals("")) {
//						objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorSituacaoEntradaVisaoFuncionarioOuAdministrador(getUsuarioLogado().getPessoa().getCodigo(), tipo, getLidas(), getRespondidas(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null, null, "", "", "");
//					} else {
//						objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorSituacaoEntrada(getUsuarioLogado().getPessoa().getCodigo(), tipo, false, getRespondidas(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//					}
//					setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
//					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
//				}
//				if (getTipoEntradaSaida().equals("limite")) {
//					objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimite(getUsuarioLogado().getPessoa().getCodigo(), tipo, getLimiteRecado(), null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "", "", "", null);
//					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
//					setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
//					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
//				}
//				setListaConsulta(objs);
//				int tamList = objs.size();
//				if (tamList >= 0) {
//					setExisteConsulta(Boolean.TRUE);
//				} else {
//					setExisteConsulta(Boolean.FALSE);
//				}
//			}
//			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
//			return "consultar";
//		} catch (Exception e) {
//			setListaConsulta(new ArrayList(0));
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//			return "consultar";
//		}
//	}

	@Override
	public String consultar() {
		try {
			super.consultar();
			setRemoverTodas(Boolean.FALSE);
			Date dataIni = null;
			Date dataFim = null;
			if(Uteis.isAtributoPreenchido(getMesConsulta())) {
				String data = "01/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
				dataIni = Uteis.getDate(data);
				data = "31/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
				dataFim = Uteis.getDate(data);
			}
//			List objs = new ArrayList(0);
//			int quantidadeTotalComunicadosSaida = 0;
			if(!Uteis.isAtributoPreenchido(getUsuarioLogado().getPessoa())) {
				getUsuarioLogado().getPessoa().setCodigo(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuarioLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()).getPessoa().getCodigo());
			}
			if (!getUsuarioLogado().getTipoUsuario().equals("VI")) {
				if(getControleConsultaOtimizado().getOffset() < 0) {
					getControleConsultaOtimizado().setPage(1);
					getControleConsultaOtimizado().setPaginaAtual(1);
					getControleConsultaOtimizado().getOffset();
				}
				if (getTipoEntradaSaida().equals("saida")) {
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
					if (getUsuarioLogado().getVisaoLogar().equals("")) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavelFuncionarioOuAdministrador(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavelFuncionarioOuAdministrador(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), dataIni, dataFim, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavel(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavel(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), dataIni, dataFim, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					}
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
				}
				if (getTipoEntradaSaida().equals("saidaVisao")) {
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
					if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavelVisaoProfessor(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavelVisaoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavelVisaoAluno(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida =  getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavelVisaoAluno(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavelVisaoCoordenador(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavelVisaoCoordenador(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else if (getVisualizarMensagemAlunoPorResponsavelLegal()) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavelVisaoCoordenador(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavelVisaoCoordenador(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorCodigoResponsavel(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadePorCodigoResponsavel(getUsuarioLogado().getPessoa().getCodigo(), getTipoSaidaConsulta(), null, null, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					}
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
//					getControleConsultaOtimizado().setListaConsulta(objs);
//					getControleConsultaOtimizado().setTotalRegistrosEncontrados(quantidadeTotalComunicadosSaida);
					setExisteConsulta(!getControleConsultaOtimizado().getListaConsulta().isEmpty());
					setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
					return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaCons");
				}
				
				String tipo = "";
				if (!getUsuarioLogado().getTipoUsuario().equals("AL") && !getUsuarioLogado().getTipoUsuario().equals("PR") && !getUsuarioLogado().getTipoUsuario().equals("CO")) {
					tipo = "FU";
				} else {
					tipo = getUsuarioLogado().getTipoUsuario();
				}

				if (getTipoEntradaSaida().equals("entrada")) {
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
					if (getUsuarioLogado().getVisaoLogar().equals("")) {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorSituacaoEntradaVisaoFuncionarioOuAdministrador(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), tipo, getLidas(), getRespondidas(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeSituacaoEntradaVisaoFuncionarioOuAdministrador(getUsuarioLogado().getPessoa().getCodigo(), tipo, getLidas(), getRespondidas(), dataIni, dataFim, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					} else {
						getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorSituacaoEntradaLimitOffset(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo(), tipo, getLidas(), getRespondidas(), dataIni, dataFim, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
//						quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeSituacaoEntradaLimitOffset(getUsuarioLogado().getPessoa().getCodigo(), tipo, getLidas(), getRespondidas(), dataIni, dataFim, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					}
//					setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getControleConsultaOtimizado(),getUsuarioLogado().getPessoa().getCodigo()));
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
				}
				if (getTipoEntradaSaida().equals("limite")) {
					getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimite(getControleConsultaOtimizado(), getUsuarioLogado().getPessoa().getCodigo(), tipo, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData(), null);
//					quantidadeTotalComunicadosSaida = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaQuantidadeEntradaLimite(getUsuarioLogado().getPessoa().getCodigo(), tipo, getFiltrarPorAssunto(), getFiltrarPorNomeResponsavel(), getFiltrarPorData());
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar Comunicação Interna", "Consultando");
//					setQtdMensagemCaixaEntrada(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidas(getUsuarioLogado().getPessoa().getCodigo()));
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar Comunicação Interna", "Consultando");
				}
				
				setExisteConsulta(!getControleConsultaOtimizado().getListaConsulta().isEmpty());
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaCons");
		} catch (Exception e) {
//			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ComunicacaoInternaVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Excluir Comunicação Interna", "Excluindo");
			getFacadeFactory().getComunicacaoInternaFacade().excluir(getComunicacaoInternaVO(), getUsuarioLogado());
			setComunicacaoInternaVO(new ComunicacaoInternaVO());
			setVerListaDestinatarios(Boolean.FALSE);
			setNovoComunicado(Boolean.TRUE);
			setLerComunicado(Boolean.FALSE);
			setResponderComunicado(Boolean.FALSE);
			setAbaAtiva("mensagem");
			setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			getComunicacaoInternaVO().setEnviarEmail(getLoginControle().getPermissaoAcessoMenuVO().getPermitirComunicacaoInternaEnviarCopiaPorEmail());
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Excluir Comunicação Interna", "Excluindo");
			setMensagemID("msg_msg_excluidos");
			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_msg_excluidos"));
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}

	}

	public void excluirVisaoAlunoProfessorCoordenador() {
		try {
			getFacadeFactory().getComunicacaoInternaFacade().excluir(getComunicacaoInternaVO(), getUsuarioLogado());
			novoVisaoAlunoProfessorCoordenador();
			setMensagemID("msg_msg_excluidos");
			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_msg_excluidos"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public void removerItens() {
		try {
			setHabilitarMsgErro(Boolean.TRUE);
			executarValidacaoSimulacaoVisaoCoordenador();
			executarValidacaoSimulacaoVisaoAluno();
			boolean marcouComoLida =  false;
			List<ComunicacaoInternaVO> list = (List<ComunicacaoInternaVO>) getControleConsultaOtimizado().getListaConsulta();
			if (list.stream().allMatch(l -> !l.getRemover())) {
				throw new Exception("Deve ao menos uma mensagem ser selecionada.");
			}
			Iterator i = getControleConsultaOtimizado().getListaConsulta().iterator();			
			while (i.hasNext()) {
				ComunicacaoInternaVO obj = (ComunicacaoInternaVO) i.next();
				if (obj.getRemover()) {
					if(obj.getMensagemLidaVisaoAlunoCoordenador()) {
						marcouComoLida =  true;
					}
					ComunicadoInternoDestinatarioVO email = (ComunicadoInternoDestinatarioVO) getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarPorComunicadoInterno(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					email.setRemoverCaixaEntrada(true);
					getFacadeFactory().getComunicadoInternoDestinatarioFacade().alterarRemoverCaixaEntrada(email.getDestinatario().getCodigo(), obj.getCodigo(), true, getUsuarioLogado());					
				}		
			}				
			if(marcouComoLida) {
				verificarQtdeMensagemCaixaEntrada();
			}
			inicializarDadosControleConsultaOtimizado();
			if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				consultarTodasEntradaLimiteAluno();
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				consultarTodasEntradaLimiteProfessor();
			}else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				consultarTodasEntradaLimiteCoordenador();
			} else {
				consultar();
			}
			setMensagemID("msg_msg_excluidos", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public void marcarComoLidas() {
		try {
			setHabilitarMsgErro(Boolean.TRUE);
			executarValidacaoSimulacaoVisaoAluno();
			Iterator i = getControleConsultaOtimizado().getListaConsulta().iterator();
			boolean marcouComoLida =  false;
			while (i.hasNext()) {
				ComunicacaoInternaVO obj = (ComunicacaoInternaVO) i.next();
				if (obj.getRemover() && !obj.getMensagemLidaVisaoAlunoCoordenador()) {
					getFacadeFactory().getComunicacaoInternaFacade().registrarLeituraComunicadoInterno(obj, getUsuarioLogado().getPessoa(), getUsuarioLogado());
					marcouComoLida =  true;
				}			
			}
			if(marcouComoLida) {
				verificarQtdeMensagemCaixaEntrada();
			}
			if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				consultarTodasEntradaLimiteAluno();
			} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				consultarTodasEntradaLimiteProfessor();
			}else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				consultarTodasEntradaLimiteCoordenador();
			} else {
				consultar();
			}
			setMensagemID("Mensagen(s) marcada(s) como lida(s).", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarComoLidasTelaVisao() {
		marcarComoLidas();
	}
	

	public void removerItensSaida() {
		try {
			executarValidacaoSimulacaoVisao();
			List<ComunicacaoInternaVO> lista = new ArrayList<>(0);
			List<ComunicacaoInternaVO> comunicado = (List<ComunicacaoInternaVO>) getControleConsultaOtimizado().getListaConsulta();
			lista = comunicado.stream().filter(mn -> mn.getRemover()).collect(Collectors.toList());
			if (lista.isEmpty()) {
				throw new Exception("Selecione pelo menos uma MENSAGEM para excluir.");
			}
			Iterator i = getControleConsultaOtimizado().getListaConsulta().iterator();			
			while (i.hasNext()) {
				ComunicacaoInternaVO obj = (ComunicacaoInternaVO) i.next();
				if (obj.getRemover()) {
					getFacadeFactory().getComunicacaoInternaFacade().alterarRemoverCaixaSaida(obj.getCodigo(), Boolean.TRUE, getUsuarioLogado());										
				}				
			}
			inicializarDadosControleConsultaOtimizado();
			if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				consultarTodasSaidaVisao();
			} else {
				consultar();
			}
			setMensagemID("msg_msg_excluidos");
			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_msg_excluidos"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	public void marcarTodas() {
		//UIDataScroller scroller = getComponente();
		Integer pagina = 1;
//		if (scroller != null) {
//			pagina = scroller.getPage();
//		}
		int index = ((pagina * getControleConsultaOtimizado().getLimitePorPagina()) - getControleConsultaOtimizado().getLimitePorPagina());
		int contador = 0;

		Iterator i = getControleConsultaOtimizado().getListaConsulta().iterator();
		while (i.hasNext()) {
			ComunicacaoInternaVO obj = (ComunicacaoInternaVO) i.next();
			if (contador >= index && contador <= (pagina * getControleConsultaOtimizado().getLimitePorPagina()) - 1) {
				obj.setRemover(getRemoverTodas());
			}
			contador++;
		}
	}
	
	public void adicionarDestinatarioResponsavelLegalComunicacaoInternaAluno() {
		try {
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado());
			if (pessoaVO != null && !pessoaVO.getCodigo().equals(0)) {
				this.getComunicacaoInternaVO().setCoordenador(pessoaVO);
				this.getComunicacaoInternaVO().setCoordenadorNome(pessoaVO.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			} else {
				throw new Exception("Você não possui Responsável Legal.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDestinatarioCoordenadorComunicacaoInternaProfessor() {
		try {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
			pessoaVOs.addAll(getFacadeFactory().getPessoaFacade().consultarCoordenadorCursoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), 0, true, getUsuarioLogado()));
			for (PessoaVO pessoaVO : pessoaVOs) {
				this.getComunicacaoInternaVO().setCoordenador(pessoaVO);
				this.getComunicacaoInternaVO().setCoordenadorNome(pessoaVO.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDestinatarioCoordenadorComunicacaoInternaCoordenador() {
		try {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
			pessoaVOs.addAll(getFacadeFactory().getPessoaFacade().consultarCoordenadorCursoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), true, getUsuarioLogado()));
			for (PessoaVO pessoaVO : pessoaVOs) {
				this.getComunicacaoInternaVO().setCoordenador(pessoaVO);
				this.getComunicacaoInternaVO().setCoordenadorNome(pessoaVO.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarDestinatarioCoordenadorComunicacaoInternaAluno() {
		try {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
			pessoaVOs.addAll(getFacadeFactory().getPessoaFacade().consultarCoordenadorCursoAluno(getMatricula(), getUsuarioLogado()));
			if (pessoaVOs.isEmpty()) {
				throw new ConsistirException("Não foi encontrado coordenador cadastrado para o teu CURSO/TURMA.");
			}
			for (PessoaVO pessoaVO : pessoaVOs) {
				this.getComunicacaoInternaVO().setCoordenador(pessoaVO);
				this.getComunicacaoInternaVO().setCoordenadorNome(pessoaVO.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ComunicadoInternoDestinatario</code> para o objeto
	 * <code>comunicacaoInternaVO</code> da classe
	 * <code>ComunicacaoInterna</code>
	 */
	public void adicionarComunicadoInternoDestinatario() throws Exception {
		try {
			// if (!getComunicacaoInternaVO().getCodigo().equals(new
			// Integer(0))) {
			// comunicadoInternoDestinatarioVO.setComunicadoInterno(getComunicacaoInternaVO().getCodigo());
			// getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			// }

			// if
			// (getComunicadoInternoDestinatarioVO().getDestinatario().getCodigo().intValue()
			// != 0) {
			// Integer campoConsulta =
			// getComunicadoInternoDestinatarioVO().getDestinatario().getCodigo();
			// PessoaVO pessoa =
			// getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta,
			// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			// getComunicadoInternoDestinatarioVO().setDestinatario(pessoa);
			// }
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			getComunicadoInternoDestinatarioVO().setComunicadoInterno(getComunicacaoInternaVO().getCodigo());
			if(getInformarEmailManualmente()){
				getComunicacaoInternaVO().getPessoa().setNome(getComunicadoInternoDestinatarioVO().getNome());	
			}
			getComunicadoInternoDestinatarioVO().setDestinatario(getComunicacaoInternaVO().getPessoa());			
			if (getComunicadoInternoDestinatarioVO().getDestinatario().getEmail().equals("")) {
				getComunicadoInternoDestinatarioVO().getDestinatario().setEmail(getComunicadoInternoDestinatarioVO().getEmail());
			}
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
			limparDadosDestinatario();
			setMensagemID("msg_dados_adicionados");
			// return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}

	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ComunicadoInternoDestinatario</code> para edição pelo usuário.
	 */
	public String editarComunicadoInternoDestinatario() throws Exception {
		ComunicadoInternoDestinatarioVO obj = (ComunicadoInternoDestinatarioVO) context().getExternalContext().getRequestMap().get("comunicadoInternoDestinatarioItem");
		setComunicadoInternoDestinatarioVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>ComunicadoInternoDestinatario</code> do objeto
	 * <code>comunicacaoInternaVO</code> da classe
	 * <code>ComunicacaoInterna</code>
	 */
	public void removerComunicadoInternoDestinatario() throws Exception {
		ComunicadoInternoDestinatarioVO obj = (ComunicadoInternoDestinatarioVO) context().getExternalContext().getRequestMap().get("comunicadoInternoDestinatarioItem");
		getComunicacaoInternaVO().excluirObjComunicadoInternoDestinatarioVOs(obj.getDestinatario().getCodigo());
		setMensagemID("msg_dados_excluidos", Uteis.SUCESSO, true);
//		setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_msg_excluidos"));
		// return "editar";
	}
	
	public void changeEnvioSMS(ValueChangeEvent event){
		getComunicacaoInternaVO().setEnviarSMS((Boolean)event.getNewValue());
	}
	

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoDestinatario</code>
	 */
//	@SuppressWarnings("UseOfObsoleteCollectionType")
//	public List getListaSelectItemTipoDestinatarioComunicacaoInterna() throws Exception {
//		List objs = new ArrayList(0);
//		objs.add(new SelectItem("", ""));
//		Hashtable tipoDestinatarioComunicadoInternos = (Hashtable) Dominios.getTipoDestinatarioComunicadoInterno();
//		Enumeration keys = tipoDestinatarioComunicadoInternos.keys();
//		while (keys.hasMoreElements()) {
//			String value = (String) keys.nextElement();
//			String label = (String) tipoDestinatarioComunicadoInternos.get(value);
//			objs.add(new SelectItem(value, label));
//		}
//
//		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//		Collections.sort((List) objs, ordenador);
//		return objs;
//	}
	
	public List<SelectItem> listaSelectItemTipoDestinatarioComunicacaoInterna ;
	public List<SelectItem> getListaSelectItemTipoDestinatarioComunicacaoInterna() throws Exception {
		if (listaSelectItemTipoDestinatarioComunicacaoInterna == null) {
			listaSelectItemTipoDestinatarioComunicacaoInterna = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem("", ""));

			for (TipoDestinatarioComunicadoInternaEnum tipoDestinatario : TipoDestinatarioComunicadoInternaEnum.values()) {

				if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.AA)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosAlunosAtivos")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.AL)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaAluno")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.CA)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaCargo")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.CO)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaCoordenador")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.DE)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaDepartamento")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.FU)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaFuncionario")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.PR)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaProfessor")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TA)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosAlunos")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TC)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodaComunidade")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TD)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTurma")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TP)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosProfessores")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TU)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTurma")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TR)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosCoordenadores")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TF)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosFuncionarios")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.ALAS)) {
					listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.IP)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_importarPlanilhaDestinatario")) {
						listaSelectItemTipoDestinatarioComunicacaoInterna.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				}
			}
		}

		return listaSelectItemTipoDestinatarioComunicacaoInterna;
	}
	
	public List<SelectItem> listaSelectItemTipoDestinatarioComunicacaoInternaAluno;

	public List<SelectItem> getListaSelectItemTipoDestinatarioComunicacaoInternaAluno() throws Exception {
		if (listaSelectItemTipoDestinatarioComunicacaoInternaAluno == null) {
			listaSelectItemTipoDestinatarioComunicacaoInternaAluno = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem("", ""));
			for (TipoDestinatarioComunicadoInternaEnum tipoDestinatario : TipoDestinatarioComunicadoInternaEnum.values()) {
				if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.AL)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaMeusAmigos")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem(tipoDestinatario.name(), "Meus Amigos"));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.CO)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaCoordenador")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.PR)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaProfessor")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.DE)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaDepartamento")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.FU)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaFuncionario")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaAluno.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				}
			}
		}
		if((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) && listaSelectItemTipoDestinatarioComunicacaoInternaAluno.size() == 1) {
			setApresentarBotaoNovoRecado(false);
		}
		return listaSelectItemTipoDestinatarioComunicacaoInternaAluno;
	}

	
	public List<SelectItem> listaSelectItemTipoDestinatarioComunicacaoInternaProfessor;

	public List<SelectItem> getListaSelectItemTipoDestinatarioComunicacaoInternaProfessor() throws Exception {
		if (listaSelectItemTipoDestinatarioComunicacaoInternaProfessor == null) {
			listaSelectItemTipoDestinatarioComunicacaoInternaProfessor = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem("", ""));
			for (TipoDestinatarioComunicadoInternaEnum tipoDestinatario : TipoDestinatarioComunicadoInternaEnum.values()) {
				if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.AL)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaAluno")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.CO)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaCoordenador")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.PR)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaProfessor")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TU)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTurma")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.DE)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaDepartamento")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				}
			}
		}
		if((getUsuarioLogado().getIsApresentarVisaoProfessor()) && Uteis.isAtributoPreenchido(listaSelectItemTipoDestinatarioComunicacaoInternaProfessor) && listaSelectItemTipoDestinatarioComunicacaoInternaProfessor.size() == 1) {
			setApresentarBotaoNovoRecado(false);
		}
		return listaSelectItemTipoDestinatarioComunicacaoInternaProfessor;
	}

	
	public List<SelectItem> listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador;

	public List<SelectItem> getListaSelectItemTipoDestinatarioComunicacaoInternaCoordenador() throws Exception {
		if (listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador == null) {
			listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador = new ArrayList<SelectItem>(0);
			listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem("", ""));
			for (TipoDestinatarioComunicadoInternaEnum tipoDestinatario : TipoDestinatarioComunicadoInternaEnum.values()) {
				if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.AL)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaAluno")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.CO)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaCoordenador")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.PR)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaProfessor")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.TU)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTurma")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				} else if (tipoDestinatario.equals(TipoDestinatarioComunicadoInternaEnum.DE)) {
					if (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaDepartamento")) {
						listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.add(new SelectItem(tipoDestinatario.name(), tipoDestinatario.getValorApresentar()));
					}
				}
			}
		}
		if((getUsuarioLogado().getIsApresentarVisaoCoordenador()) && listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador.size() == 1) {
			setApresentarBotaoNovoRecado(false);
		}
		return listaSelectItemTipoDestinatarioComunicacaoInternaCoordenador;
	}

	
	public List<SelectItem> listaSelectItemPrioridadeComunicadoInterno;
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemPrioridadeComunicadoInterno() throws Exception {
		listaSelectItemPrioridadeComunicadoInterno = new ArrayList<SelectItem>(0);
		listaSelectItemPrioridadeComunicadoInterno.add(new SelectItem("", ""));
		Hashtable<String,String> prioridadeComunicadoInternos = (Hashtable<String, String>) Dominios.getPrioridadeComunicadoInterno();
		Enumeration<String> keys = prioridadeComunicadoInternos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) prioridadeComunicadoInternos.get(value);
			listaSelectItemPrioridadeComunicadoInterno.add(new SelectItem(value, label));
		}

		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) listaSelectItemPrioridadeComunicadoInterno, ordenador);
		return listaSelectItemPrioridadeComunicadoInterno;
	}


	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoComunicadoInterno</code>
	 */
//	@SuppressWarnings("UseOfObsoleteCollectionType")
//	public List getListaSelectItemTipoComunicadoInternoComunicacaoInterna() throws Exception {
//		List objs = new ArrayList(0);
//		Hashtable tipoComunicadoInternos = (Hashtable) Dominios.getTipoComunicadoInterno();
//		Enumeration keys = tipoComunicadoInternos.keys();
//		while (keys.hasMoreElements()) {
//			String value = (String) keys.nextElement();
//			String label = (String) tipoComunicadoInternos.get(value);
//			objs.add(new SelectItem(value, label));
//		}
//
//		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
//		Collections.sort((List) objs, ordenador);
//		return objs;
//	}
	
	public List<SelectItem> getListaSelectItemTipoComunicadoInternoComunicacaoInterna() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		for(TipoComunicadoInternoComunicacaoInternaEnum tipoComunicado : TipoComunicadoInternoComunicacaoInternaEnum.values()) {
			if(tipoComunicado.equals(TipoComunicadoInternoComunicacaoInternaEnum.LE)) {
				if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_tipoSomenteLeitura")) {
					objs.add(new SelectItem(tipoComunicado.name(), tipoComunicado.getValorApresentar()));
				}
			} else if(tipoComunicado.equals(TipoComunicadoInternoComunicacaoInternaEnum.MU)) {
				if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_tipoMural")) {
					objs.add(new SelectItem(tipoComunicado.name(), tipoComunicado.getValorApresentar()));
				}
			} else if(tipoComunicado.equals(TipoComunicadoInternoComunicacaoInternaEnum.RE)) {
				if(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_tipoExigeResposta")) {
					objs.add(new SelectItem(tipoComunicado.name(), tipoComunicado.getValorApresentar()));
				}
			}
		}
		return objs;
	}

	@SuppressWarnings("UseOfObsoleteCollectionType")
	public List getListaSelectItemTipoComunicadoInternoComunicacaoInternaAluno() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoComunicadoInternos = (Hashtable) Dominios.getTipoComunicadoInternoAluno();
		Enumeration keys = tipoComunicadoInternos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoComunicadoInternos.get(value);
			objs.add(new SelectItem(value, label));
		}

		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>codigo</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarComunicacaoInternaPorCodigo(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getComunicacaoInternaFacade().consultarPorCodigo(codigoPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	

	

	public void montarListaSelectItemCoordenadores(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCoordenadoresPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PessoaVO obj = (PessoaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemCoordenador(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemCoordenadores() {
		try {
			montarListaSelectItemCoordenadores("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemCoordenadores: " + e.getMessage());
		}

	}

	public List consultarCoordenadoresPorNome(String prm) throws Exception {
		List listaResultado = getFacadeFactory().getPessoaFacade().consultarPorCoordenadoresNome(prm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return listaResultado;
	}

	public void montarListaSelectItemTurmaAluno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		setListaSelectItemTurma(null);
		try {
			if (getListaSelectItemTurma().equals(new ArrayList(0))) {
				resultadoConsulta = consultarTurmaAluno();
				Integer curso = null;
				MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (mat.getCurso().getNivelEducacionalPosGraduacao()) {
					curso = mat.getCurso().getCodigo();
				}
				i = resultadoConsulta.iterator();
				List objs = new ArrayList(0);
				while (i.hasNext()) {
					TurmaVO obj = (TurmaVO) i.next();
					if (curso != null) {
						if (obj.getCurso().getCodigo().equals(curso)) {
							objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
						}
					} else {
						objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
					}
				}
				setListaSelectItemTurma(objs);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemTurmaAluno() {
		try {
			montarListaSelectItemTurmaAluno("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemTurmaAluno: " + e.getMessage());
		}

	}

//	public void montarListaSelectItemTurmaProfessor(String prm) throws Exception {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		setListaSelectItemTurma(null);
//		try {
//			if (getListaSelectItemTurma().isEmpty()) {
//				resultadoConsulta = consultarTurmaProfessor();
//				i = resultadoConsulta.iterator();
//				getListaSelectItemTurma().clear();
//				while (i.hasNext()) {
//					TurmaVO obj = (TurmaVO) i.next();
//					getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
//					if (getComunicacaoInternaVO().getTurma().getCodigo() == 0) {
//						getComunicacaoInternaVO().getTurma().setCodigo(obj.getCodigo());
//						getTurmaVO().setCodigo(obj.getCodigo());
//					}
//					removerObjetoMemoria(obj);
//				}
//
//				montarListaDisciplinaTurmaVisaoProfessor();
//				resultadoConsulta.clear();
//				resultadoConsulta = null;
//				i = null;
//			}
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurma();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinaProfessorTurma(objs);

		} catch (Exception e) {
			setListaSelectItemDisciplinaProfessorTurma(new ArrayList(0));
		}
	}

	public void montarListaDisciplinaTurmaVisaoAdministrador() {
//		try {
//			List objs = new ArrayList(0);
//			getListaSelectItemDisciplinaProfessorTurma().clear();
//			List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getComunicacaoInternaVO().getTurma().getCodigo(), false, true, 0);
//			objs.add(new SelectItem(0, ""));
//			for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs ) {
//				objs.add(new SelectItem(obj.getCodigoDisciplina() , obj.getNomeDisciplina()));
//			}
//			setListaSelectItemDisciplinaProfessorTurma(objs);
//		} catch (Exception e) {
//			setListaSelectItemDisciplinaProfessorTurma(new ArrayList(0));
//		}
	}

	public List consultarDisciplinaProfessorTurma() throws Exception {
		List listaConsultas = new ArrayList(0);
		if (getTurmaVO().getCurso().getNivelEducacional().equals("PO")) {
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(getUsuarioLogado().getPessoa().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return listaConsultas;
		} else {
			String ano = Uteis.getAnoDataAtual4Digitos();
			String semestreatual = Uteis.getSemestreAtual();
			if(getPermitirEnviarComunicadoParaTurmaPeriodoAnterior() ) {
				if( Uteis.isAtributoPreenchido(getSemestre())) {
					semestreatual = getSemestre();					
				}
				if(Uteis.isAtributoPreenchido( getAno())) {
					ano = getAno();
				}
			}
				
			listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(getUsuarioLogado().getPessoa().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), ano,semestreatual, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return listaConsultas;
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		setListaSelectItemTurma(null);
		try {
			if (getListaSelectItemTurma().isEmpty()) {
				resultadoConsulta = consultarTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemTurma().clear();
				while (i.hasNext()) {
					TurmaVO obj = (TurmaVO) i.next();
					getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

//	public void montarListaSelectItemTurmaProfessor() {
//		try {
//			montarListaSelectItemTurmaProfessor("");
//		} catch (Exception e) {
//			// System.out.println("Erro montarListaSelectItemTurmaProfessor: " + e.getMessage());
//		}
//	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemTurmaCoordenador: " + e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Professor</code>.
	 */
	public void montarListaSelectItemProfessor(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarProfessorPorProfessor();
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, "Todos"));
			while (i.hasNext()) {
				PessoaVO obj = (PessoaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemProfessor(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Professor</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Pessoa</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemProfessor() {
		try {
			montarListaSelectItemProfessor("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemProfessor: " + e.getMessage());
		}

	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Professor</code>.
	 */
	public void montarListaSelectItemProfessorAluno(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getPessoaFacade().consultarProfessorDisciplina(matricula, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, "Todos"));
			while (i.hasNext()) {
				TurmaProfessorDisciplinaVO obj = (TurmaProfessorDisciplinaVO) i.next();				
				objs.add(new SelectItem(obj.getProfessorVO().getCodigo(), obj.getProfessorVO().getNome() + " - "+obj.getDisciplinaVO().getNome()));
			}
			setListaSelectItemProfessor(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemProfessorProfessor(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarProfessorPorProfessor();
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, "Todos"));
			while (i.hasNext()) {
				PessoaVO obj = (PessoaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemProfessor(objs);
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
	 * <code>Professor</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Pessoa</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemProfessorProfessor() {
		try {
			montarListaSelectItemProfessorProfessor("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemProfessorProfessor: " + e.getMessage());
		}

	}

	public void montarListaSelectItemProfessorAluno() {
		try {
			montarListaSelectItemProfessorAluno("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemProfessorAluno: " + e.getMessage());
		}

	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemUnidadeEnsino(objs);
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
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemUnidadeEnsino: " + e.getMessage());
		}

	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List consultarTurmaAluno() throws Exception {
		// List lista =
		// getFacadeFactory().getTurmaFacade().consultarTurmaPorPessoa(getUsuarioLogado().getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		List lista = getFacadeFactory().getTurmaFacade().consultarTurmaDoAluno(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

//	public List consultarTurmaProfessor() throws Exception {
//		String semestreatual =  Uteis.getSemestreAtual();
//        String ano  =  Uteis.getData(new Date(), "yyyy");
//        
//		if(getPermitirEnviarComunicadoParaTurmaPeriodoAnterior() ) {
//			if( Uteis.isAtributoPreenchido(getSemestre()) && Uteis.isAtributoPreenchido( getAno())) {
//				semestreatual = getSemestre();
//				ano = getAno();
//			}		
//		}
//		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreEturmaEadNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(),semestreatual,ano, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
//	}

	public List consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(), false, false, false, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Departamento</code>.
	 */
	public void montarListaSelectItemDepartamento(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarDepartamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}

			setListaSelectItemDepartamento(objs);
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
	 * <code>Departamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Departamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemDepartamento: " + e.getMessage());
		}

	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDepartamentoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNomeFaleConosco(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cargo</code>.
	 */
	public void montarListaSelectItemCargo(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCargoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CargoVO obj = (CargoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}

			setListaSelectItemCargo(objs);
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
	 * <code>Cargo</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCargo() {
		try {
			montarListaSelectItemCargo("");
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemCargo: " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarCargoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCargoFacade().consultaRapidaPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Funcionario</code>.
	 */
	public void montarListaSelectItemFuncionario(Integer prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarFuncionarioPorCodigo(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FuncionarioVO obj = (FuncionarioVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo().toString()));
			}
			setListaSelectItemFuncionario(objs);
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
	 * <code>Funcionario</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Funcionario</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFuncionario() {
		try {
			montarListaSelectItemFuncionario(0);
		} catch (Exception e) {
			// System.out.println("Erro montarListaSelectItemFuncionario: " + e.getMessage());
		}

	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>codigo</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarFuncionarioPorCodigo(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getFuncionarioFacade().consultarPorCodigo(codigoPrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarProfessorPorAluno() throws Exception {

		List lista = getFacadeFactory().getPessoaFacade().consultarProfessoresDoAluno(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

		return lista;
	}

	public List consultarProfessorPorProfessor() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarPorCodigo(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), "PR", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public String consultarMeusProfessores() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Consultar MeusProfessores", "Consultando");
		setListaConsultaProfessor(new ArrayList(0));
		VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		if (visaoAlunoControle != null) {
			visaoAlunoControle.inicializarMenuMeusProfessores();
			setMatricula(visaoAlunoControle.getMatricula().getMatricula());
			setListaConsultaProfessor(getFacadeFactory().getPessoaFacade().consultarProfessorDisciplina(getMatricula(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Consultar MeusProfessores", "Consultando");
		return Uteis.getCaminhoRedirecionamentoNavegacao("meusProfessoresAluno.xhtml");
	}

	public String enviarMensagemProfessor() throws Exception {
		try {
			novoVisaoAlunoProfessorCoordenador();
			TurmaProfessorDisciplinaVO turmaProfessorDisciplinaVO = (TurmaProfessorDisciplinaVO) context().getExternalContext().getRequestMap().get("professorItens");
			getComunicacaoInternaVO().setTipoDestinatario("PR");
			getComunicacaoInternaVO().setProfessor(turmaProfessorDisciplinaVO.getProfessorVO());
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
			getComunicadoInternoDestinatarioVO().setDestinatario(turmaProfessorDisciplinaVO.getProfessorVO());
			montarListaSelectItemProfessorAluno();
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setDestinatarioPR(Boolean.TRUE);
			setDestinatarioAL(Boolean.FALSE);
			setDestinatarioAR(Boolean.FALSE);
			setDestinatarioCA(Boolean.FALSE);
			setDestinatarioCO(Boolean.FALSE);
			setDestinatarioDE(Boolean.FALSE);
			setDestinatarioFU(Boolean.FALSE);
			setDestinatarioTU(Boolean.FALSE);
			setDestinatarioTD(Boolean.FALSE);
			setDestinatarioTO(Boolean.FALSE);
			return Uteis.getCaminhoRedirecionamentoNavegacao("recadoAluno.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String enviarMensagemAluno() throws Exception {
		try {
			novoVisaoAlunoProfessorCoordenador();
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			getComunicacaoInternaVO().setTipoDestinatario("AL");
			getComunicacaoInternaVO().setAluno(obj);
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
			getComunicadoInternoDestinatarioVO().setDestinatario(obj);
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setDestinatarioPR(Boolean.FALSE);
			setDestinatarioAL(Boolean.TRUE);
			setDestinatarioAR(Boolean.FALSE);
			setDestinatarioCA(Boolean.FALSE);
			setDestinatarioCO(Boolean.FALSE);
			setDestinatarioDE(Boolean.FALSE);
			setDestinatarioFU(Boolean.FALSE);
			setDestinatarioTU(Boolean.FALSE);
			setDestinatarioTD(Boolean.FALSE);
			setDestinatarioTO(Boolean.FALSE);
			return Uteis.getCaminhoRedirecionamentoNavegacao("recadoAluno.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemCargo();
		montarListaSelectItemDepartamento();
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemCoordenadores();
		setListaSelectItemTurma(null);
		setListaSelectItemProfessor(null);
		setListaSelectItemAluno(null);
		
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nomePessoa", "Responsável"));
		itens.add(new SelectItem("tipoComunicadoInterno", "Tipo Comunicado Interno"));
		itens.add(new SelectItem("tipoDestinatario", "Tipo Destinatário"));
		return itens;
	}

	public List getTipoConsultaCaixa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("entrada", "Caixa de Entrada"));
		itens.add(new SelectItem("saida", "Caixa de Saída"));
		itens.add(new SelectItem("todos", "Todos"));
		return itens;
	}

	public void definirTipoDestinatario() {
		setTipoNivelEducacional("");
		if (comunicacaoInternaVO.getTipoDestinatario() == null || comunicacaoInternaVO.getTipoDestinatario().equals("")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("FU")) {
			setDestinatarioFU(true);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("AL")) {
			setDestinatarioFU(false);
			setDestinatarioAL(true);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("PR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(true);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("CA")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(true);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioCO(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("DE")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(true);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("AR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(true);
			setDestinatarioCO(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TU")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(true);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(true);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TA")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(true);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TP")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(true);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TC")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(true);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("CO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(true);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("AA")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(true);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TD")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(true);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(true);
			setDestinatarioTF(false);
			setDestinatarioALAS(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TF")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(true);
			setDestinatarioALAS(false);
		}else if(comunicacaoInternaVO.getTipoDestinatario().equals("ALAS")){
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			setDestinatarioTA(false);
			setDestinatarioTP(false);
			setDestinatarioTC(false);
			setDestinatarioAA(false);
			setDestinatarioTR(false);
			setDestinatarioTF(false);
			setDestinatarioALAS(true);
		}
		getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(new ArrayList(0));

	}

	public void definirTipoDestinatarioAluno() {
		if (getComunicacaoInternaVO().getTipoDestinatario() == null || getComunicacaoInternaVO().getTipoDestinatario().equals("")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioRL(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
		} else {
			setDestinatarioFU(getComunicacaoInternaVO().getTipoDestinatario().equals("FU"));
			setDestinatarioAL(getComunicacaoInternaVO().getTipoDestinatario().equals("AL"));
			setDestinatarioPR(getComunicacaoInternaVO().getTipoDestinatario().equals("PR"));
			setDestinatarioDE(getComunicacaoInternaVO().getTipoDestinatario().equals("DE"));
			setDestinatarioRL(getComunicacaoInternaVO().getTipoDestinatario().equals("RL"));
			setDestinatarioTU(getComunicacaoInternaVO().getTipoDestinatario().equals("TU"));
			setDestinatarioCO(getComunicacaoInternaVO().getTipoDestinatario().equals("CO"));
		}
	}

	public void definirTipoDestinatarioProfessor() {
		if (comunicacaoInternaVO.getTipoDestinatario() == null || comunicacaoInternaVO.getTipoDestinatario().equals("")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTT(false);
			setDestinatarioCO(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("AL")) {
			setDestinatarioFU(false);
			setDestinatarioAL(true);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("PR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(true);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTT(false);
			setDestinatarioCO(false);
			// montarListaSelectItemProfessor();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TU")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(true);
			setDestinatarioTD(false);
			setDestinatarioTT(false);
			setDestinatarioCO(false);
		   setPermitirEnviarComunicadoParaTurmaPeriodoAnterior(verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTurmaPeriodoAnterior"));
		} 
		else if (comunicacaoInternaVO.getTipoDestinatario().equals("CO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTT(false);
			setDestinatarioCO(true);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("DE")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(true);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTT(false);
			setDestinatarioCO(false);
			montarListaSelectItemDepartamento();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TD")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(true);
			setDestinatarioTT(false);
			setDestinatarioCO(false);
			montarListaSelectItemDepartamento();
		}
	}

	public void definirTipoDestinatarioCoordenador() {
		if (comunicacaoInternaVO.getTipoDestinatario() == null || comunicacaoInternaVO.getTipoDestinatario().equals("")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("AL")) {
			setDestinatarioFU(false);
			setDestinatarioAL(true);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("PR")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(true);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			// montarListaSelectItemProfessor();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TU")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(true);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			montarListaSelectItemTurmaCoordenador();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(true);
			setDestinatarioCO(false);
			montarListaSelectItemTurmaCoordenador();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("CO")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(true);
			montarListaSelectItemCoordenadores();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("DE")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(true);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(false);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			montarListaSelectItemDepartamento();
		} else if (comunicacaoInternaVO.getTipoDestinatario().equals("TD")) {
			setDestinatarioFU(false);
			setDestinatarioAL(false);
			setDestinatarioPR(false);
			setDestinatarioCA(false);
			setDestinatarioDE(false);
			setDestinatarioAR(false);
			setDestinatarioTU(false);
			setDestinatarioTD(true);
			setDestinatarioTO(false);
			setDestinatarioCO(false);
			montarListaSelectItemDepartamento();
		}
	}

//	public void definirTipoComunicado() {
//		if (comunicacaoInternaVO.getTipoComunicadoInterno().equals("") || comunicacaoInternaVO.getTipoComunicadoInterno().equals("MU")) {
//			setTipoMural(true);
//		} else {
//			setTipoMural(true);
//		}
//
//	}

	/**
	 * Método responsavel por adicionar os Todos alunos Matriculados na lista de
	 * Destinatarios apos a consulta do mesmo.
	 * 
	 * @author Carlos
	 */
	public void adicionarTodosAlunosMatriculados() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaAlunoMatriculado(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), "AL", "", getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Método responsável por adicionar todos alunos cuja situação da matricula esteja AT na lista de destinatários.
	 */
	public void adicionarTodosAlunosMatriculadosAtivos() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaAlunoMatriculadoSituacaoMatricula(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), "AT", getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsavel por adicionar os Todos alunos Matriculados na lista de
	 * Destinatarios apos a consulta do mesmo.
	 * 
	 * @author Carlos
	 */
	public void adicionarTodosProfessores() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaTodosProfessoresNivelEducacional(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), getTipoNivelEducacional(), "PR", getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsavel por adicionar os Todos alunos Matriculados na lista de
	 * Destinatarios apos a consulta do mesmo.
	 * 
	 * @author Carlos
	 */
	public void adicionarTodaComunidade() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaTodaComunidade(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
		} catch (Exception e) {
			// System.out.println("Erro adicionarTodaComunidade: " + e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (!getValorConsultaFuncionario().equals("")) { 
				if (getCampoConsultaFuncionario().equals("nome")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				if (getCampoConsultaFuncionario().equals("CPF")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
			if (getValorConsultaFuncionario().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCoordenador() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (!getValorConsultaCoordenador().equals("")) {
				if (getCampoConsultaCoordenador().equals("nome")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorNomeApresentarModal(getValorConsultaCoordenador(), false, getUsuarioLogado());
				}
				if (getCampoConsultaCoordenador().equals("CPF")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorCPFApresentarModal(getValorConsultaCoordenador(), false, getUsuarioLogado());
				}
			}
			if (getValorConsultaCoordenador().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			setListaConsultaCoordenador(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public List getTipoConsultaComboCoordenador() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	public void consultarAluno() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			
			if (getCampoConsultaAluno().equals("nome")) {
				if (!getValorConsultaAluno().equals("")) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoMatriculadoPorNome(getValorConsultaAluno(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), "AL", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					throw new Exception("Pelo menos um valor deve ser Informado.");
				}
			}
			if (getCampoConsultaAluno().equals("CPF")) {
				if (!getValorConsultaAluno().equals("")) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoMatriculadoPorCPF(getValorConsultaAluno(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), "AL", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					throw new Exception("Pelo menos um valor deve ser Informado.");
				}
			}
			
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				if (!getValorConsultaAluno().equals("")) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaPorRegistroAcademico(getValorConsultaAluno(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), TipoPessoa.ALUNO.getValor(), true ,false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					
				} else {
					throw new Exception("Pelo menos um valor deve ser Informado.");
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));		
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	public void consultarPessoa() {
		try {
			super.consultar();
			if (getValorConsultaPessoa().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");		
				
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaPessoa().equals("nome")) {				
					objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); // getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(),
		
			}
			if (getCampoConsultaPessoa().equals("CPF")) {				
					objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); // getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(),
				
			}
			if (getCampoConsultaPessoa().equals("registroAcademico")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaPorRegistroAcademico(getValorConsultaPessoa(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), TipoPessoa.ALUNO.getValor(),false,  false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			
			}
			
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPessoa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoVisao() {
		try {
			getFacadeFactory().getComunicacaoInternaFacade().validarUsuarioConsultarMeusAmigos(getUsuarioLogadoClone());
			super.consultar();
			List<PessoaVO> objs = new ArrayList(0);
			if (getCampoConsultaAluno().equals("")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorNomeVisaoAluno(getValorConsultaAluno(), getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorNomeVisaoAluno(getValorConsultaAluno(), getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("turma")) {
				if (!getValorConsultaAluno().equals("")) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorTurmaVisaoAluno(getValorConsultaAluno(), getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

				} else {
					throw new Exception("Informe parametro para consulta.");
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoVisaoCoordenador() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaPorNome(getValorConsultaAluno(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaPorCpf(getValorConsultaAluno(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("curso")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaPorCurso(getValorConsultaAluno(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoVisaoProfessor() {
		try {
			if (getValorConsultaAluno() == null || getValorConsultaAluno().equals("")) {
				throw new ConsistirException("Informe parâmetro para consulta.");
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDoProfessorTutorPorNome(getUsuarioLogado().getPessoa().getCodigo(), getValorConsultaAluno(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("turma")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDoProfessorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getValorConsultaAluno(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoTurmaVisaoProfessor() {
		try {
			
			getListaConsultaAluno().clear();
			List objs = new ArrayList(0);
			String ano = Uteis.getAnoDataAtual4Digitos();
			String semestre = Uteis.getSemestreAtual();
			getComunicacaoInternaVO().getTurma().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getComunicacaoInternaVO().setTurma(getFacadeFactory().getTurmaFacade().carregarDadosTurmaAgrupada(getComunicacaoInternaVO().getTurma(), getUsuarioLogado()));
			if (getComunicacaoInternaVO().getTurma().getIntegral()) {
				ano = "";
				semestre = "";
			} else if (getComunicacaoInternaVO().getTurma().getAnual()) {
				semestre = "";
			}
			if(getPermitirEnviarComunicadoParaTurmaPeriodoAnterior()) {
				if(Uteis.isAtributoPreenchido(getAno()) && Uteis.isAtributoPreenchido(getSemestre())) {
					ano = getAno();
					semestre = getSemestre();
				}
			}
			if (getTipoAluno().equals("normal")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaNormal(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), 0, semestre, ano, false, getUsuarioLogado());
			} else if (getTipoAluno().equals("reposicao")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaReposicaoInclusao(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), 0, semestre, ano, false, getUsuarioLogado());
			} else {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaTodos(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), 0,  semestre, ano, false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

//	public void consultarTodosAlunoTurmaVisaoProfessor() {
//		try {
//			getListaConsultaAluno().clear();
//			List objs = new ArrayList(0);
//			List listaTurma = new ArrayList(0);
//			Iterator j = consultarTurmaProfessor().iterator();
//			while (j.hasNext()) {
//				TurmaVO obj = (TurmaVO) j.next();
//				listaTurma.add(obj.getCodigo());
//			}
//			objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaTodos(listaTurma, 0, Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
//			setListaConsultaAluno(objs);
//			Iterator i = objs.iterator();
//			while (i.hasNext()) {
//				PessoaVO obj = (PessoaVO) i.next();
//				this.getComunicacaoInternaVO().setAluno(obj);
//				this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
//				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
//				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
//				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
//				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
//			}
//			setMensagemID("msg_dados_adicionados");
//		} catch (Exception e) {
//			setListaConsultaAluno(new ArrayList(0));
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//	}

	public void consultarAlunoTurmaVisaoCoordenador() {
		try {
			getListaConsultaAluno().clear();
			List objs = new ArrayList(0);
			// objs =
			// getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorCodigoTurmaSituacaoMatriculaPeriodo(getComunicacaoInternaVO().getTurma().getCodigo(),
			// "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			// getUsuarioLogado());
			if (getTipoAluno().equals("normal")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaNormal(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			} else if (getTipoAluno().equals("reposicao")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaReposicaoInclusao(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			} else {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaTodos(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherDadosTurma() {
		try {
			getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessorVisaoProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoVisaoProfessor().equals("nome")) {
				if (!getValorVisaoProfessor().equals("")) {
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorVisaoProfessor(), "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

				} else {
					throw new Exception("Informe parametro para consulta.");
				}
			}
			if (getCampoVisaoProfessor().equals("codigo")) {
				if (!getValorVisaoProfessor().equals("")) {
					if (getValorVisaoProfessor().equals("")) {
						setValorVisaoProfessor("0");
					}
					int valorInt = Integer.parseInt(getValorVisaoProfessor());
					objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCodigo(new Integer(valorInt), "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

				} else {
					throw new Exception("Informe parametro para consulta.");
				}
			}
			setListaVisaoProfessor(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaVisaoProfessor().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarProfessorVisaoAluno() {
		try {
			super.consultar();
			if (getCampoConsultaProfessorVisaoAluno().equals("nome")) {
				if (!getValorConsultaProfessorVisaoAluno().equals("")) {
					setListaConsultaProfessorVisaoAluno(getFacadeFactory().getPessoaFacade().consultarProfessoresDoAlunoVisaoAluno(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(
							getUsuarioLogado()).getPessoa().getCodigo(), getValorConsultaProfessorVisaoAluno(), getUnidadeEnsinoLogado().getCodigo(), getMatricula(), false, getUsuarioLogado()));
				} else {
					throw new Exception("Informe um parâmetro para a consulta.");
				}
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboPessoa;

	public List getTipoConsultaComboPessoa() {
		tipoConsultaComboPessoa = new ArrayList(0);
		tipoConsultaComboPessoa.add(new SelectItem("nome", "Nome"));
		tipoConsultaComboPessoa.add(new SelectItem("CPF", "CPF"));		
		tipoConsultaComboPessoa.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return tipoConsultaComboPessoa;
	}

	private List<SelectItem> tipoConsultaComboAlunoVisao;

	public List getTipoConsultaComboAlunoVisao() {
		if (tipoConsultaComboAlunoVisao == null) {
			tipoConsultaComboAlunoVisao = new ArrayList(0);
			tipoConsultaComboAlunoVisao.add(new SelectItem("turma", "Turma"));
			tipoConsultaComboAlunoVisao.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboAlunoVisao;
	}

	private List<SelectItem> tipoConsultaComboAlunoVisaoCoordenador;

	public List getTipoConsultaComboAlunoVisaoCoordenador() {
		if (tipoConsultaComboAlunoVisaoCoordenador == null) {
			tipoConsultaComboAlunoVisaoCoordenador = new ArrayList(0);
			tipoConsultaComboAlunoVisaoCoordenador.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboAlunoVisaoCoordenador.add(new SelectItem("cpf", "CPF"));
			tipoConsultaComboAlunoVisaoCoordenador.add(new SelectItem("curso", "Curso"));
		}
		return tipoConsultaComboAlunoVisaoCoordenador;
	}

	private List<SelectItem> tipoConsultaComboProfessorVisao;

	public List getTipoConsultaComboProfessorVisao() {
		if (tipoConsultaComboProfessorVisao == null) {
			tipoConsultaComboProfessorVisao = new ArrayList(0);
			tipoConsultaComboProfessorVisao.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboProfessorVisao;
	}

	private List<SelectItem> tipoConsultaComboProfessorVisaoAluno;

	public List getTipoConsultaComboProfessorVisaoAluno() {
		if (tipoConsultaComboProfessorVisaoAluno == null) {
			tipoConsultaComboProfessorVisaoAluno = new ArrayList(0);
			tipoConsultaComboProfessorVisaoAluno.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboProfessorVisaoAluno;
	}

	public void consultarProfessor() {
		try {
			List objs = new ArrayList(0);
			if (!getValorConsultaProfessor().equals("")) {
				if (getCampoConsultaProfessor().equals("nome")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeNivelEducacional(getValorConsultaProfessor(), getTipoNivelEducacional(), "PR", getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaProfessor().equals("CPF")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPFNivelEducacional(getValorConsultaProfessor(), getTipoNivelEducacional(), "PR", getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaProfessor().equals("matricula")) {
					objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaNivelEducacional(getValorConsultaProfessor(), getTipoNivelEducacional(), "PR", getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
			}
			if (getValorConsultaProfessor().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaProfessor().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarProfessorPorMatricula() {
		try {
			FuncionarioVO obj = new FuncionarioVO();
			if (!getValorConsultaProfessor().equals("")) {
				obj = getFacadeFactory().getFuncionarioFacade().consultarFuncionarioPorMatricula(getValorConsultaProfessor(), "PR", null, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
			}
			setValorConsultaProfessor(obj.getMatricula());
			this.getComunicacaoInternaVO().setProfessor(obj.getPessoa());
			getComunicacaoInternaVO().getProfessor().setNome(obj.getPessoa().getNome());
		} catch (Exception e) {
			getComunicacaoInternaVO().getProfessor().setNome("");
		}
	}

	public void consultarFuncionarioPorMatricula() {
		try {
			FuncionarioVO obj = new FuncionarioVO();
			if (!getValorConsultaFuncionario().equals("")) {
				obj = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
			}
			setValorConsultaFuncionario(obj.getMatricula());
			this.getComunicacaoInternaVO().setFuncionario(obj);
			getComunicacaoInternaVO().getFuncionario().getPessoa().setNome(obj.getPessoa().getNome());
		} catch (Exception e) {
			getComunicacaoInternaVO().getFuncionario().getPessoa().setNome("");
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO obj = new MatriculaVO();
			if (!getValorConsultaAluno().equals("")) {
				obj = getFacadeFactory().getMatriculaFacade().consultarAlunoPorMatricula(getValorConsultaAluno(), "AL", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
			}
			setValorConsultaAluno(obj.getMatricula());
			this.getComunicacaoInternaVO().setAluno(obj.getUsuario().getPessoa());
			getComunicacaoInternaVO().getAluno().setNome(obj.getAluno().getNome());
		} catch (Exception e) {
			getComunicacaoInternaVO().getAluno().setNome("");
		}
	}

	private List tipoConsultaComboProfessor;

	public List getTipoConsultaComboProfessor() {
		if (tipoConsultaComboProfessor == null) {
			tipoConsultaComboProfessor = new ArrayList(0);
			tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboProfessor;
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (!getValorConsultaTurma().equals("")) {
				if (getCampoConsultaTurma().equals("nome")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("curso")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("turno")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
			}
			if (getValorConsultaTurma().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			Uteis.liberarListaMemoria(getListaConsultaTurma());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurmaDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaTurmaDisciplina().equals("")) {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			if (getCampoConsultaTurmaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurmaDisciplina(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurmaDisciplina().equals("curso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurmaDisciplina(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurmaDisciplina().equals("turno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurmaDisciplina(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaTurmaDisciplina(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			Uteis.liberarListaMemoria(getListaConsultaTurmaDisciplina());
		}
	}
	
	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		this.getComunicacaoInternaVO().setFuncionario(obj);
		this.getComunicacaoInternaVO().setFuncionarioNome(obj.getPessoa().getNome());
		getFacadeFactory().getPessoaFacade().carregarDados(obj.getPessoa(), getUsuarioLogado());
		this.getComunicacaoInternaVO().getFuncionario().setMatricula(obj.getMatricula());
		valorConsultaFuncionario = obj.getMatricula();
		this.listaConsultaFuncionario.clear();
		obj = null;
		this.campoConsultaFuncionario = null;
	}

	public void selecionarCoordenador() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("coordenadorItens");
		this.getComunicacaoInternaVO().setFuncionario(obj);
		this.getComunicacaoInternaVO().setFuncionarioNome(obj.getPessoa().getNome());
		this.getComunicacaoInternaVO().getFuncionario().setMatricula(obj.getMatricula());
		valorConsultaCoordenador = obj.getMatricula();
		this.listaConsultaCoordenador.clear();
		obj = null;
		this.campoConsultaCoordenador = null;
	}

	public void adicionarFuncionario() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			this.getComunicacaoInternaVO().setFuncionario(obj);
			this.getComunicacaoInternaVO().setFuncionarioNome(obj.getPessoa().getNome());
			getFacadeFactory().getPessoaFacade().carregarDados(obj.getPessoa(), getUsuarioLogado());			
			getComunicadoInternoDestinatarioVO().setDestinatario(obj.getPessoa());
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setListaConsultaFuncionario(comunicacaoInternaVO.retirarDestinatarioFuncionarioEscolhidoDaLista(this.listaConsultaFuncionario, obj.getPessoa().getCodigo()));
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarCoordenador() throws Exception {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("coordenadorItens");
			this.getComunicacaoInternaVO().setFuncionario(obj);
			this.getComunicacaoInternaVO().setFuncionarioNome(obj.getPessoa().getNome());
			getComunicadoInternoDestinatarioVO().setDestinatario(obj.getPessoa());
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setListaConsultaCoordenador(comunicacaoInternaVO.retirarDestinatarioFuncionarioEscolhidoDaLista(this.listaConsultaCoordenador, obj.getPessoa().getCodigo()));
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
		this.getComunicacaoInternaVO().setAluno(obj);
		this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
		this.listaConsultaAluno.clear();
		obj = null;
		this.valorConsultaAluno = null;
		this.campoConsultaAluno = null;
	}

	public void adicionarAluno() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			this.getComunicacaoInternaVO().setAluno(obj);
			this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
			getComunicadoInternoDestinatarioVO().setDestinatario(obj);
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
			retirarDestinatarioEscolhidoDaLista(getListaConsultaAluno(), obj.getCodigo());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTodosAlunosTurma() throws Exception {
		try {
			List<PessoaVO> objs = new ArrayList(0);
			objs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoVisaoAluno(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (PessoaVO pessoaVO : objs) {
				this.getComunicacaoInternaVO().setAluno(pessoaVO);
				this.getComunicacaoInternaVO().setAlunoNome(pessoaVO.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTodosAlunosProfessor() throws Exception {
		try {
			List<PessoaVO> objs = new ArrayList(0);
			objs = getFacadeFactory().getPessoaFacade().consultarAlunosDoProfessor(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogado()).getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			for (PessoaVO obj : objs) {
				this.getComunicacaoInternaVO().setAluno(obj);
				this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTodosAlunosTurmaCoordenador() throws Exception {
		try {
			List<PessoaVO> objs = new ArrayList(0);
			objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurma(getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			for (PessoaVO obj : objs) {
				this.getComunicacaoInternaVO().setAluno(obj);
				this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarAlunosTurma() throws Exception {
		try {
			List<PessoaVO> objs = new ArrayList(0);
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			if (getTipoAluno().equals("normal")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaNormal(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
			} else if (getTipoAluno().equals("reposicao")) {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaReposicaoInclusao(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
			} else {
				objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaTodos(getComunicacaoInternaVO().getTurma().getCodigo(), getComunicacaoInternaVO().getDisciplina().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
			}
			// objs =
			// getFacadeFactory().getPessoaFacade().consultarAlunosDaTurma(getComunicacaoInternaVO().getTurma().getCodigo(),
			// getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(),
			// Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
			for (PessoaVO obj : objs) {
				this.getComunicacaoInternaVO().setAluno(obj);
				this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			
			if (!Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs())) {
				throw new Exception("Nenhum Destinatário Foi Localizado!");
			}
			
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarAlunosTurmaSelecionada() throws Exception {
		try {
			//getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			validarDadosTurmaSelecionada();
			for (TurmaVO turmaVO : getListaConsultaTurma()) {
				if (turmaVO.getTurmaSelecionada()) {
					List<PessoaVO> objs = new ArrayList(0);
					if (turmaVO.getCurso().getPeriodicidade().equals("SE") || turmaVO.getPeriodicidade().equals("SE")) {
						setSemestre(getSemestre());
						setAno(getAno());
					} else if (turmaVO.getCurso().getPeriodicidade().equals("AN") || turmaVO.getPeriodicidade().equals("AN")) {
						setSemestre("");
						setAno(getAno());
					} else {
						setSemestre("");
						setAno("");
					}
					if (turmaVO.getTipoAlunoFiltro().equals("normal")) {
						objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaNormal(turmaVO.getCodigo(), !getUsuarioLogado().getVisaoLogar().equals("professor") ? getComunicacaoInternaVO().getDisciplina().getCodigo() : 0, getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
					} else if (turmaVO.getTipoAlunoFiltro().equals("reposicao")) {
						objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaReposicaoInclusao(turmaVO.getCodigo(), !getUsuarioLogado().getVisaoLogar().equals("professor") ? getComunicacaoInternaVO().getDisciplina().getCodigo() : 0, getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
					} else {
						objs = getFacadeFactory().getPessoaFacade().consultarAlunosDaTurmaTodos(turmaVO.getCodigo(), !getUsuarioLogado().getVisaoLogar().equals("professor") ? getComunicacaoInternaVO().getDisciplina().getCodigo() : 0, getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
					}
					for (PessoaVO obj : objs) {
						this.getComunicacaoInternaVO().setAluno(obj);
						this.getComunicacaoInternaVO().setAlunoNome(obj.getNome());
						getComunicadoInternoDestinatarioVO().setDestinatario(obj);
						getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
						getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
						this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
					}
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			}
			if (!Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs())) {
				throw new Exception("Nenhum Destinatário Foi Localizado!");
			}
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getIsFecharModalTurma() {
		if (getFecharModalTurma()) {
			return "PF('panelTurma').hide()";
		}
		return "";
	}

	public void validarDadosTurmaSelecionada() throws Exception {
		boolean turmaSelecionada = false;
		for (TurmaVO turmaVO : getListaConsultaTurma()) {
			if (turmaVO.getTurmaSelecionada()) {
				turmaSelecionada = true;
			}
		}
		if (turmaSelecionada) {
			setFecharModalTurma(Boolean.TRUE);
		} else {
			setFecharModalTurma(Boolean.FALSE);
			throw new Exception("Ao menos uma turma deve ser selecionada.");
		}
	}

	public void limparDadosTurma() {
		getListaConsultaTurma().clear();
		setValorConsultaTurma("");
		getListaConsultaTurmaDisciplina().clear();
		setValorConsultaTurmaDisciplina("");
		getComunicacaoInternaVO().setTurma(null);
		getComunicacaoInternaVO().setDisciplina(new DisciplinaVO());
	}

	public void adicionarProfessor() throws Exception {
		try {
			if (getComunicacaoInternaVO().getProfessor().getCodigo().intValue() != 0) {
				PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getComunicacaoInternaVO().getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				setListaConsultaProfessor(comunicacaoInternaVO.retirarDestinatarioFuncionarioEscolhidoDaLista(this.listaConsultaProfessor, obj.getCodigo()));
			} else {
				List listaProfessor = consultarProfessorPorAluno();
				Iterator i = listaProfessor.iterator();
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getComunicadoInternoDestinatarioVO().setDestinatario(obj);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarProfessorVisaoAluno() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>(0);
			boolean professorFoiSelecionado = false;
			if (professorFoiSelecionado = Uteis.isAtributoPreenchido(obj)) {
				listaProfessor.add(obj);
			} else {
				listaProfessor.addAll(getFacadeFactory().getPessoaFacade().consultarProfessoresDoAlunoVisaoAluno(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(
					getUsuarioLogado()).getPessoa().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), getMatricula(), false, getUsuarioLogado()));
				if (listaProfessor.isEmpty()) {
					throw new ConsistirException("Não foram encontrados professores cadastros.");
				}
			}
			for (PessoaVO professor : listaProfessor) {
				if (professorFoiSelecionado) {
					this.getComunicacaoInternaVO().setProfessor(professor);
					this.getComunicacaoInternaVO().setProfessorNome(professor.getNome());
				}
				getComunicadoInternoDestinatarioVO().setDestinatario(professor);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				retirarDestinatarioEscolhidoDaLista(getListaConsultaProfessorVisaoAluno(), professor.getCodigo());
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarProfessorProfessor() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			this.getComunicacaoInternaVO().setProfessor(obj);
			this.getComunicacaoInternaVO().setProfessorNome(obj.getNome());
			getComunicadoInternoDestinatarioVO().setDestinatario(obj);
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			retirarDestinatarioEscolhidoDaLista(getListaVisaoProfessor(), obj.getCodigo());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getIsPermiteAnexarArquivo() {
		return verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_permiteAnexarArquivo");
	}
	// public void adicionarProfessorProfessor() throws Exception {
	// try {
	// if (getComunicacaoInternaVO().getProfessor().getCodigo().intValue() != 0)
	// {
	// PessoaVO obj =
	// getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getComunicacaoInternaVO().getProfessor().getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// getComunicadoInternoDestinatarioVO().setDestinatario(obj);
	// getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
	// getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
	// this.setComunicadoInternoDestinatarioVO(new
	// ComunicadoInternoDestinatarioVO());
	// } else {
	// List listaProfessor = consultarProfessorPorProfessor();
	// Iterator i = listaProfessor.iterator();
	// while (i.hasNext()) {
	// PessoaVO obj = (PessoaVO) i.next();
	// getComunicadoInternoDestinatarioVO().setDestinatario(obj);
	// getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
	// getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
	// this.setComunicadoInternoDestinatarioVO(new
	// ComunicadoInternoDestinatarioVO());
	// }
	// }
	// setMensagemID("msg_dados_adicionados");
	// } catch (Exception e) {
	// this.setComunicadoInternoDestinatarioVO(new
	// ComunicadoInternoDestinatarioVO());
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
	public void adicionarTurma() throws Exception {
		try {
			List<PessoaVO> listaColegas = new ArrayList(0);
			if (getComunicacaoInternaVO().getTurma().getCodigo().intValue() != 0) {
				// List listaColegas =
				// getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurma(getUsuarioLogado().getPessoa().getCodigo(),
				// getComunicacaoInternaVO().getTurma().getCodigo(), false,
				// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				if (getTipoAluno().equals("normal")) {
					listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaNormal(getUsuarioLogado().getPessoa().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
				} else if (getTipoAluno().equals("reposicao")) {
					listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaReposicaoInclusao(getUsuarioLogado().getPessoa().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
				} else {
					listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaTodos(getUsuarioLogado().getPessoa().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
				}
				Iterator i = listaColegas.iterator();
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getComunicadoInternoDestinatarioVO().setDestinatario(obj);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			} else {
				Integer codigoAluno = getUsuarioLogado().getPessoa().getCodigo();
				Iterator j = getListaSelectItemTurma().iterator();
				while (j.hasNext()) {
					SelectItem item = (SelectItem) j.next();
					if (!item.getValue().equals(0)) {
						// List listaColegas =
						// getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurma(codigoAluno,
						// (Integer) item.getValue(), false,
						// Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						// getUsuarioLogado());
						if (getTipoAluno().equals("normal")) {
							listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaNormal(codigoAluno, (Integer) item.getValue(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
						} else if (getTipoAluno().equals("reposicao")) {
							listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaReposicaoInclusao(codigoAluno, (Integer) item.getValue(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
						} else {
							listaColegas = getFacadeFactory().getPessoaFacade().consultarColegasDoAlunoPorCodigoTurmaTodos(codigoAluno, (Integer) item.getValue(), getUnidadeEnsinoLogado().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual4Digitos(), false, getUsuarioLogado());
						}
						Iterator i = listaColegas.iterator();
						while (i.hasNext()) {
							PessoaVO obj = (PessoaVO) i.next();
							getComunicadoInternoDestinatarioVO().setDestinatario(obj);
							getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
							getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
							this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
						}
					}
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarTurmaProfessorCoordenador() throws Exception {
		try {
			if (getComunicacaoInternaVO().getTurma().getCodigo().intValue() != 0) {
				Iterator i = getListaConsultaAluno().iterator();
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getComunicadoInternoDestinatarioVO().setDestinatario(obj);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			} else {
				Iterator j = getListaSelectItemTurma().iterator();
				while (j.hasNext()) {
					SelectItem item = (SelectItem) j.next();
					if (!item.getValue().equals(0)) {
						List listaColegas = getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorCodigoTurma((Integer) item.getValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), true);
						Iterator i = listaColegas.iterator();
						while (i.hasNext()) {
							PessoaVO obj = (PessoaVO) i.next();
							getComunicadoInternoDestinatarioVO().setDestinatario(obj);
							getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
							getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
							this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
						}
					}
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarAlunoSelecionadoCheckBoxGrupoDestinatario() {
		try {
			Iterator i = getListaConsultaAluno().iterator();
			while (i.hasNext()) {
				PessoaVO obj = (PessoaVO) i.next();
				if (obj.getEnviarComunicadoPessoa()) {
					getComunicadoInternoDestinatarioVO().setDestinatario(obj);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			}
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void adicionarDepartamentoProfessorCoordenador() throws Exception {
		try {
			List<PessoaVO> listaResponsavelDepartamento = new ArrayList<PessoaVO>(0);
			List<DepartamentoVO> listaDepartamento = new ArrayList<DepartamentoVO>(0);
			String mensagemResponsaveisNaoEncontrados = "";
			if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getDepartamento())) {
				listaDepartamento.add(getComunicacaoInternaVO().getDepartamento());
				mensagemResponsaveisNaoEncontrados = "O Departamento selecionado não possui nenhum responsável cadastrado!";
			} else {
				for (SelectItem item : getListaSelectItemDepartamento()) {
					DepartamentoVO dep = new DepartamentoVO();
					dep.setCodigo((Integer) item.getValue());
					listaDepartamento.add(dep);
				}
				mensagemResponsaveisNaoEncontrados = "Os Departamentos listados não possuem nenhum responsável cadastrado!";
			}
			listaResponsavelDepartamento = getFacadeFactory().getDepartamentoFacade().consultarDepartamentoObrigatoriamenteComResponsavelPorCoodigoParaEnvioComunicadoInterno(listaDepartamento, getUsuarioLogado())
					.stream().map(DepartamentoVO::getResponsavel).collect(Collectors.toList());
			if (listaResponsavelDepartamento.isEmpty()) {
				throw new ConsistirException(mensagemResponsaveisNaoEncontrados);
			}
			for (PessoaVO responsavel : listaResponsavelDepartamento) {
				getComunicadoInternoDestinatarioVO().setDestinatario(responsavel);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			} 
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDepartamento() throws Exception {
		try {
			List<PessoaVO> listaColegas = new ArrayList<PessoaVO>(0);
			if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getDepartamento())) {
				listaColegas = getFacadeFactory().getPessoaFacade().consultaRapidaFuncionariosPorCodigoDepartamento(getComunicacaoInternaVO().getDepartamento().getCodigo(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), true, false, getUsuarioLogado());
			} else {
				for (SelectItem item : getListaSelectItemDepartamento()) {
					if (!item.getValue().equals(0)) {
						listaColegas.addAll(getFacadeFactory().getPessoaFacade().consultaRapidaFuncionariosPorCodigoDepartamento((Integer) item.getValue(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), true, false, getUsuarioLogado()));
					}
				}
			}
			if (listaColegas.isEmpty()) {
				throw new Exception("O Departamento selecionado não possui nenhum funcionário cadastrado!");
			}
			for (PessoaVO pessoa : listaColegas) {
				getComunicadoInternoDestinatarioVO().setDestinatario(pessoa);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarCargo() throws Exception {
		try {
			if (getComunicacaoInternaVO().getCargo().getCodigo().intValue() != 0) {
				List listaColegas = getFacadeFactory().getPessoaFacade().consultaRapidaFuncionariosPorCodigoCargo(getComunicacaoInternaVO().getCargo().getCodigo(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), true, false, getUsuarioLogado());
				Iterator i = listaColegas.iterator();
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getComunicadoInternoDestinatarioVO().setDestinatario(obj);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
				}
			} else {
				Iterator j = getListaSelectItemCargo().iterator();
				while (j.hasNext()) {
					SelectItem item = (SelectItem) j.next();
					if (!item.getValue().equals(0)) {
						List listaColegas = getFacadeFactory().getPessoaFacade().consultaRapidaFuncionariosPorCodigoCargo((Integer) item.getValue(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), true, false, getUsuarioLogado());
						Iterator i = listaColegas.iterator();
						while (i.hasNext()) {
							PessoaVO obj = (PessoaVO) i.next();
							getComunicadoInternoDestinatarioVO().setDestinatario(obj);
							getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
							getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
							this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
						}
					}
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarCoordenadorProfessor() throws Exception {
		try {
			if (getComunicacaoInternaVO().getPessoa().getCodigo().intValue() != 0) {
				PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getComunicacaoInternaVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getComunicadoInternoDestinatarioVO().setDestinatario(obj);
				getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
				this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			} else {
				Iterator j = getListaSelectItemCoordenador().iterator();
				while (j.hasNext()) {
					SelectItem item = (SelectItem) j.next();
					if (!item.getValue().equals(0)) {
						PessoaVO obj = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria((Integer) item.getValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						getComunicadoInternoDestinatarioVO().setDestinatario(obj);
						getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
						getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
						this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
					}
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPessoa() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
		this.getComunicacaoInternaVO().setPessoa(obj);
		this.getComunicacaoInternaVO().setPessoaNome(obj.getNome());
		this.listaConsultaPessoa.clear();
		obj = null;
		this.valorConsultaPessoa = null;
		this.campoConsultaPessoa = null;
	}

	public void selecionarProfessor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		valorConsultaProfessor = obj.getMatricula();
		this.getComunicacaoInternaVO().setProfessor(obj.getPessoa());
		this.getComunicacaoInternaVO().setProfessorNome(obj.getPessoa().getNome());
		adicionarProfessor();
		this.listaConsultaProfessor.clear();
		obj = null;
		this.campoConsultaProfessor = null;
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		this.getComunicacaoInternaVO().setTurma(obj);
		this.getComunicacaoInternaVO().setTurmaNome(obj.getIdentificadorTurma());
		if(!Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getCurso()) && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getTurma().getCurso())) {
			getComunicacaoInternaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getComunicacaoInternaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
		// adicionarTurmaProfessorCoordenador();
		montarListaDisciplinaTurmaVisaoAdministrador();
		this.listaConsultaTurma.clear();
		obj = null;
		this.valorConsultaTurma = null;
		this.campoConsultaTurma = null;
	}

	public void selecionarTurmaDisciplina() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItem");
		this.getComunicacaoInternaVO().setTurma(obj);
		this.getComunicacaoInternaVO().setTurmaNome(obj.getIdentificadorTurma());
		// adicionarTurmaProfessorCoordenador();
		montarListaDisciplinaTurmaVisaoAdministrador();
		this.listaConsultaTurma.clear();
		obj = null;
		this.valorConsultaTurma = null;
		this.campoConsultaTurma = null;
	}
	
	private List tipoConsultaComboTurma;

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);
			tipoConsultaComboTurma.add(new SelectItem("nome", "Identificador Turma"));
			if (!getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				tipoConsultaComboTurma.add(new SelectItem("curso", "Curso"));
				tipoConsultaComboTurma.add(new SelectItem("turno", "Turno"));
			}
		}
		return tipoConsultaComboTurma;
	}

	public String inicializarConsultar() {
		try {
			removerObjetoMemoria(this);
			setExisteConsulta(Boolean.FALSE);
			Uteis.liberarListaMemoria(getControleConsultaOtimizado().getListaConsulta());
			consultarTodasEntradasPorIntervaloData();
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("comunicacaoInternaCons");
	}

	public void validarTipoMarketing() {
		getFacadeFactory().getComunicacaoInternaFacade().validarTipoMarketing(comunicacaoInternaVO);
	}

	public void validarTipoLeituraObrigatoria() {
		getFacadeFactory().getComunicacaoInternaFacade().validarTipoLeituraObrigatoria(comunicacaoInternaVO);
	}

	public Boolean getIsApresentarTipoMarketingTipoLeituraObrigatoria() {		
		return true;
	}

	public void consultarComunicacaoInternaNaoLidasMenu() {
		try {
			setTipoEntradaSaida("entrada");
			setLidas(Boolean.FALSE);
			setRespondidas(null);
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaNaoLidasMenu(getUsuarioLogado().getPessoa().getCodigo());
			setListaConsulta(objs);
			int tamList = objs.size();
			if (tamList > 0) {
				setExisteConsulta(Boolean.TRUE);
			} else {
				setExisteConsulta(Boolean.FALSE);
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarNavegacaoParaTelaOrigemCadastro() {
		if (getComunicacaoInternaVO().getIsOrigemComunicaoInternaOuvidoria()) {
			StringBuilder sb = new StringBuilder("abrirPopup('");
			sb.append("ouvidoriaForm.xhtml?");// tela de destino
			sb.append("codigoOrigem=").append(getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna()); // codigo
																													// de
																													// origem
			sb.append("&tipoOrigem=VISAO_ADMINISTRATIVA");// tipo da origem
			sb.append("', 'Ouvidoria', 950, 595);");
			setTelaOrigemCadastro(sb.toString());
			sb = null;
		}

	}

	public void realizarNavegacaoParaTelaOrigemCadastroVisaoAluno() {
		if (getComunicacaoInternaVO().getIsOrigemComunicaoInternaOuvidoria()) {
			StringBuilder sb = new StringBuilder("window.location =('ConverteGetParaPostServletComunicacaoInterna?");
			sb.append("pagina=ouvidoriaAlunoForm.xhtml");// tela de destino
			sb.append("&paramCodigoOrigem=codigoOrigem&cdgOrigem=").append(getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna()); // codigo
																																				// de
																																				// origem
			sb.append("&paramTipoOrigem=tipoOrigem&tpOrigem=VISAO_ALUNO");// tipo
																			// da
																			// origem
			sb.append("');");
			setTelaOrigemCadastro(sb.toString());
			sb = null;
		}

	}

	public void realizarNavegacaoParaTelaOrigemCadastroVisaoProfessor() {
		if (getComunicacaoInternaVO().getIsOrigemComunicaoInternaOuvidoria()) {
			StringBuilder sb = new StringBuilder("window.location =('ConverteGetParaPostServletComunicacaoInterna?");
			sb.append("pagina=ouvidoriaProfessorForm.xhtml");// tela de destino
			sb.append("&paramCodigoOrigem=codigoOrigem&cdgOrigem=").append(getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna()); // codigo
																																				// de
																																				// origem
			sb.append("&paramTipoOrigem=tipoOrigem&tpOrigem=VISAO_PROFESSOR");// tipo
																				// da
																				// origem
			sb.append("');");
			setTelaOrigemCadastro(sb.toString());
			sb = null;
		}

	}

	public void realizarNavegacaoParaTelaOrigemCadastroVisaoCoordenador() {
		if (getComunicacaoInternaVO().getIsOrigemComunicaoInternaOuvidoria()) {
			StringBuilder sb = new StringBuilder("window.location =('ConverteGetParaPostServletComunicacaoInterna?");
			sb.append("pagina=ouvidoriaCoordenadorForm.xhtml");// tela de destino
			sb.append("&paramCodigoOrigem=codigoOrigem&cdgOrigem=").append(getComunicacaoInternaVO().getCodigoTipoOrigemComunicacaoInterna()); // codigo
																																				// de
																																				// origem
			sb.append("&paramTipoOrigem=tipoOrigem&tpOrigem=VISAO_COORDENADOR");// tipo
																				// da
																				// origem
			sb.append("');");
			setTelaOrigemCadastro(sb.toString());
			sb = null;
		}

	}
	
	  public void scrollerListener() throws Exception {
	        
	        if (getConsultarSemConsiderarData()) {
	        	setMesConsulta("");
	        }
	        consultar();
	   }
	  
//	 public Filter<?> getFiltroAssunto(){
//		 return new Filter<ComunicacaoInternaVO>(){
//			 public boolean accept(ComunicacaoInternaVO item){
//				 String valor = getFiltro();
//				 System.out.println("Filtro : "+valor);
//				 if(valor == null || valor.length() == 0 || item.getAssunto().contains(valor)){
//					 return true;
//				 }
//				 return false;
//			 }
//		 };
//	 }
	 
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

	// get set
	public List getListaSelectItemDestinatario() {
		if (listaSelectItemDestinatario == null) {
			listaSelectItemDestinatario = new ArrayList(0);
		}
		return (listaSelectItemDestinatario);
	}

	public void setListaSelectItemDestinatario(List listaSelectItemDestinatario) {
		this.listaSelectItemDestinatario = listaSelectItemDestinatario;
	}

	public ComunicadoInternoDestinatarioVO getComunicadoInternoDestinatarioVO() {
		if (comunicadoInternoDestinatarioVO == null) {
			comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		}
		return comunicadoInternoDestinatarioVO;
	}

	public void setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO) {
		this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
	}

	public List getListaSelectItemComunicadoInternoOrigem() {
		if (listaSelectItemComunicadoInternoOrigem == null) {
			listaSelectItemComunicadoInternoOrigem = new ArrayList(0);
		}
		return (listaSelectItemComunicadoInternoOrigem);
	}

	public void setListaSelectItemComunicadoInternoOrigem(List listaSelectItemComunicadoInternoOrigem) {
		this.listaSelectItemComunicadoInternoOrigem = listaSelectItemComunicadoInternoOrigem;
	}

	public List getListaSelectItemProfessor() {
		if (listaSelectItemProfessor == null) {
			listaSelectItemProfessor = new ArrayList(0);
		}
		return listaSelectItemProfessor;
	}

	public void setListaSelectItemProfessor(List listaSelectItemProfessor) {
		this.listaSelectItemProfessor = listaSelectItemProfessor;
	}

	public List getListaSelectItemAluno() {
		if (listaSelectItemAluno == null) {
			listaSelectItemAluno = new ArrayList(0);
		}
		return listaSelectItemAluno;
	}

	public void setListaSelectItemAluno(List listaSelectItemAluno) {
		this.listaSelectItemAluno = listaSelectItemAluno;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList(0);
		}
		return (listaSelectItemDepartamento);
	}

	public void setListaSelectItemDepartamento(List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}

	public List getListaSelectItemCargo() {
		if (listaSelectItemCargo == null) {
			listaSelectItemCargo = new ArrayList(0);
		}
		return (listaSelectItemCargo);
	}

	public void setListaSelectItemCargo(List listaSelectItemCargo) {
		this.listaSelectItemCargo = listaSelectItemCargo;
	}

	public List getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList(0);
		}
		return (listaSelectItemFuncionario);
	}

	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List getListaSelectItemResponsavel() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return (listaSelectItemTurma);
	}

	public void setListaSelectItemResponsavel(List listaSelectItemResponsavel) {
		this.listaSelectItemResponsavel = listaSelectItemResponsavel;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public Boolean getDestinatarioFU() {
		if (DestinatarioFU == null) {
			DestinatarioFU = false;
		}
		return DestinatarioFU;
	}

	public void setDestinatarioFU(Boolean DestinatarioFU) {
		this.DestinatarioFU = DestinatarioFU;
	}

	public Boolean getDestinatarioAL() {
		if (DestinatarioAL == null) {
			DestinatarioAL = false;
		}
		return DestinatarioAL;
	}

	public void setDestinatarioAL(Boolean DestinatarioAL) {
		this.DestinatarioAL = DestinatarioAL;
	}

	public Boolean getDestinatarioPR() {
		if (DestinatarioPR == null) {
			DestinatarioPR = false;
		}
		return DestinatarioPR;
	}

	public void setDestinatarioPR(Boolean DestinatarioPR) {
		this.DestinatarioPR = DestinatarioPR;
	}

	public Boolean getDestinatarioCA() {
		if (DestinatarioCA == null) {
			DestinatarioCA = false;
		}
		return DestinatarioCA;
	}

	public void setDestinatarioCA(Boolean DestinatarioCA) {
		this.DestinatarioCA = DestinatarioCA;
	}

	public Boolean getDestinatarioDE() {
		if (DestinatarioDE == null) {
			DestinatarioDE = false;
		}
		return DestinatarioDE;
	}

	public void setDestinatarioDE(Boolean DestinatarioDE) {
		this.DestinatarioDE = DestinatarioDE;
	}

	public Boolean getTipoMural() {
		if (TipoMural == null) {
			TipoMural = Boolean.TRUE;
		}
		return TipoMural;
	}

	public void setTipoMural(Boolean TipoMural) {
		this.TipoMural = TipoMural;
	}

	public Integer getTamanhoLista() {
		return getComunicacaoInternaVO().getNovoObj() ? new Integer(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().size()) : getComunicacaoInternaVO().getListaConsultaDestinatario().getTotalRegistrosEncontrados();
	}

	public String getCampoCaixa() {
		if (campoCaixa == null) {
			campoCaixa = "";
		}
		return campoCaixa;
	}

	public void setCampoCaixa(String campoCaixa) {
		this.campoCaixa = campoCaixa;
	}

	public Boolean getDestinatarioAR() {
		if (DestinatarioAR == null) {
			DestinatarioAR = false;
		}
		return DestinatarioAR;
	}

	public void setDestinatarioAR(Boolean DestinatarioAR) {
		this.DestinatarioAR = DestinatarioAR;
	}

	public Boolean getDestinatarioTU() {
		if (DestinatarioTU == null) {
			DestinatarioTU = false;
		}
		return DestinatarioTU;
	}

	public void setDestinatarioTU(Boolean DestinatarioTU) {
		this.DestinatarioTU = DestinatarioTU;
	}

	public Boolean getDestinatarioTO() {
		if (DestinatarioTO == null) {
			DestinatarioTO = false;
		}
		return DestinatarioTO;
	}

	public void setDestinatarioTO(Boolean DestinatarioTO) {
		this.DestinatarioTO = DestinatarioTO;
	}

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List getListaSelectItemAreaConhecimento() {
		if (listaSelectItemAreaConhecimento == null) {
			listaSelectItemAreaConhecimento = new ArrayList(0);
		}
		return listaSelectItemAreaConhecimento;
	}

	public void setListaSelectItemAreaConhecimento(List listaSelectItemAreaConhecimento) {
		this.listaSelectItemAreaConhecimento = listaSelectItemAreaConhecimento;
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

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List<PessoaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<PessoaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
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

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public Boolean getVerListaDestinatarios() {
		if (verListaDestinatarios == null) {
			verListaDestinatarios = false;
		}
		return verListaDestinatarios;
	}

	public void setVerListaDestinatarios(Boolean verListaDestinatarios) {
		this.verListaDestinatarios = verListaDestinatarios;
	}

	public Boolean getNovoComunicado() {
		if (novoComunicado == null) {
			novoComunicado = false;
		}
		return novoComunicado;
	}

	public void setNovoComunicado(Boolean novoComunicado) {
		this.novoComunicado = novoComunicado;
	}

	public Boolean getEditarComunicado() {
		if (editarComunicado == null) {
			editarComunicado = false;
		}
		return editarComunicado;
	}

	public void setEditarComunicado(Boolean editarComunicado) {
		this.editarComunicado = editarComunicado;
	}

	public Boolean getLerComunicado() {
		if (lerComunicado == null) {
			lerComunicado = false;
		}
		return lerComunicado;
	}

	public void setLerComunicado(Boolean lerComunicado) {
		this.lerComunicado = lerComunicado;
	}

	public Boolean getResponderComunicado() {
		if (responderComunicado == null) {
			responderComunicado = false;
		}
		return responderComunicado;
	}

	public void setResponderComunicado(Boolean responderComunicado) {
		this.responderComunicado = responderComunicado;
	}

	// public Boolean getExisteConsulta() {
	// if
	// (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty())
	// {
	// return Boolean.FALSE;
	// } else {
	// return Boolean.TRUE;
	// }
	// }
	public Boolean getExisteConsulta() {
		if (existeConsulta == null) {
			existeConsulta = false;
		}
		return existeConsulta;
	}

	public void setExisteConsulta(Boolean existeConsulta) {
		this.existeConsulta = existeConsulta;
	}

	public Boolean getLidas() {
		// if (lidas == null) {
		// lidas = false;
		// }
		return lidas;
	}

	public void setLidas(Boolean lidas) {
		this.lidas = lidas;
	}

	public Boolean getNaoLidas() {
		if (naoLidas == null) {
			naoLidas = false;
		}
		return naoLidas;
	}

	public Boolean getComunicadoSaida() {
		if (comunicadoSaida == null) {
			comunicadoSaida = Boolean.FALSE;
		}
		return comunicadoSaida;
	}

	public void setComunicadoSaida(Boolean comunicadoSaida) {
		this.comunicadoSaida = comunicadoSaida;
	}

	public void setNaoLidas(Boolean naoLidas) {
		this.naoLidas = naoLidas;
	}

	public Boolean getNaoRespondidas() {
		// if (naoRespondidas == null) {
		// naoRespondidas = false;
		// }
		return naoRespondidas;
	}

	public void setNaoRespondidas(Boolean naoRespondidas) {
		this.naoRespondidas = naoRespondidas;
	}

	public Boolean getRespondidas() {
		if (respondidas == null) {
			respondidas = false;
		}
		return respondidas;
	}

	public void setRespondidas(Boolean respondidas) {
		this.respondidas = respondidas;
	}

	public String getTipoSaidaConsulta() {
		if (tipoSaidaConsulta == null) {
			tipoSaidaConsulta = "";
		}
		return tipoSaidaConsulta;
	}

	public void setTipoSaidaConsulta(String tipoSaidaConsulta) {
		this.tipoSaidaConsulta = tipoSaidaConsulta;
	}

	public String getMesConsulta() {
		if (mesConsulta == null) {
			String mesAtual = String.valueOf(Uteis.getMesData(new Date()));
			if (mesAtual.length() == 1) {
				mesAtual = "0" + mesAtual;
			}
			mesConsulta = mesAtual;
			mesAtual = null;
		}
		return mesConsulta;
	}

	public void setMesConsulta(String mesConsulta) {
		this.mesConsulta = mesConsulta;
	}

	public Integer getAnoConsulta() {
		if (anoConsulta == null) {
			anoConsulta = Uteis.getAnoData(new Date());
		}
		return anoConsulta;
	}

	public void setAnoConsulta(Integer anoConsulta) {
		this.anoConsulta = anoConsulta;
	}

	public String getTipoEntradaSaida() {
		if (tipoEntradaSaida == null) {
			tipoEntradaSaida = "";
		}
		return tipoEntradaSaida;
	}

	public void setTipoEntradaSaida(String tipoEntradaSaida) {
		this.tipoEntradaSaida = tipoEntradaSaida;
	}

	public String getAbaAtiva() {
		if (abaAtiva == null) {
			abaAtiva = "";
		}
		return abaAtiva;
	}

	public void setAbaAtiva(String abaAtiva) {
		this.abaAtiva = abaAtiva;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public List getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList(0);
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public Integer getLimiteRecado() {
		if (limiteRecado == null) {
			limiteRecado = 20;
		}
		return limiteRecado;
	}

	public void setLimiteRecado(Integer limiteRecado) {
		this.limiteRecado = limiteRecado;
	}

	public List getListaConsultaSaida() {
		if (listaConsultaSaida == null) {
			listaConsultaSaida = new ArrayList(0);
		}
		return listaConsultaSaida;
	}

	public void setListaConsultaSaida(List listaConsultaSaida) {
		this.listaConsultaSaida = listaConsultaSaida;
	}

	public Boolean getRemoverTodas() {
		if (removerTodas == null) {
			removerTodas = Boolean.FALSE;
		}
		return removerTodas;
	}

	public void setRemoverTodas(Boolean removerTodas) {
		this.removerTodas = removerTodas;
	}

	public List getListaSelectItemCoordenador() {
		if (listaSelectItemCoordenador == null) {
			listaSelectItemCoordenador = new ArrayList(0);
		}
		return listaSelectItemCoordenador;
	}

	public void setListaSelectItemCoordenador(List listaSelectItemCoordenador) {
		this.listaSelectItemCoordenador = listaSelectItemCoordenador;
	}

	public Boolean getDestinatarioCO() {
		if (DestinatarioCO == null) {
			DestinatarioCO = false;
		}
		return DestinatarioCO;
	}

	public void setDestinatarioCO(Boolean DestinatarioCO) {
		this.DestinatarioCO = DestinatarioCO;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCampoVisaoProfessor() {
		if (campoVisaoProfessor == null) {
			campoVisaoProfessor = "";
		}
		return campoVisaoProfessor;
	}

	public void setCampoVisaoProfessor(String campoVisaoProfessor) {
		this.campoVisaoProfessor = campoVisaoProfessor;
	}

	public String getValorVisaoProfessor() {
		if (valorVisaoProfessor == null) {
			valorVisaoProfessor = "";
		}
		return valorVisaoProfessor;
	}

	public void setValorVisaoProfessor(String valorVisaoProfessor) {
		this.valorVisaoProfessor = valorVisaoProfessor;
	}

	public List getListaVisaoProfessor() {
		if (listaVisaoProfessor == null) {
			listaVisaoProfessor = new ArrayList(0);
		}
		return listaVisaoProfessor;
	}

	public void setListaVisaoProfessor(List listaVisaoProfessor) {
		this.listaVisaoProfessor = listaVisaoProfessor;
	}

	public String getCampoConsultaProfessorVisaoAluno() {
		if (campoConsultaProfessorVisaoAluno == null) {
			campoConsultaProfessorVisaoAluno = "";
		}
		return campoConsultaProfessorVisaoAluno;
	}

	public void setCampoConsultaProfessorVisaoAluno(String campoConsultaProfessorVisaoAluno) {
		this.campoConsultaProfessorVisaoAluno = campoConsultaProfessorVisaoAluno;
	}

	public String getValorConsultaProfessorVisaoAluno() {
		if (valorConsultaProfessorVisaoAluno == null) {
			valorConsultaProfessorVisaoAluno = "";
		}
		return valorConsultaProfessorVisaoAluno;
	}

	public void setValorConsultaProfessorVisaoAluno(String valorConsultaProfessorVisaoAluno) {
		this.valorConsultaProfessorVisaoAluno = valorConsultaProfessorVisaoAluno;
	}

	public List<PessoaVO> getListaConsultaProfessorVisaoAluno() {
		if (listaConsultaProfessorVisaoAluno == null) {
			listaConsultaProfessorVisaoAluno = new ArrayList(0);
		}
		return listaConsultaProfessorVisaoAluno;
	}

	public void setListaConsultaProfessorVisaoAluno(List<PessoaVO> listaConsultaProfessorVisaoAluno) {
		this.listaConsultaProfessorVisaoAluno = listaConsultaProfessorVisaoAluno;
	}

	public Boolean getDestinatarioTA() {
		if (DestinatarioTA == null) {
			DestinatarioTA = Boolean.FALSE;
		}
		return DestinatarioTA;
	}

	public void setDestinatarioTA(Boolean destinatarioTA) {
		DestinatarioTA = destinatarioTA;
	}

	public Boolean getDestinatarioTP() {
		if (DestinatarioTP == null) {
			DestinatarioTP = Boolean.FALSE;
		}
		return DestinatarioTP;
	}

	public void setDestinatarioTP(Boolean destinatarioTP) {
		DestinatarioTP = destinatarioTP;
	}

	public Boolean getDestinatarioTC() {
		if (DestinatarioTC == null) {
			DestinatarioTC = Boolean.FALSE;
		}
		return DestinatarioTC;
	}

	public void setDestinatarioTC(Boolean destinatarioTC) {
		DestinatarioTC = destinatarioTC;
	}

	public List getListaPessoaDestinatario() {
		if (listaPessoaDestinatario == null) {
			listaPessoaDestinatario = new ArrayList(0);
		}
		return listaPessoaDestinatario;
	}

	public void setListaPessoaDestinatario(List listaPessoaDestinatario) {
		this.listaPessoaDestinatario = listaPessoaDestinatario;
	}

	public Boolean getAbrirRichModalMarketing() {
		if (abrirRichModalMarketing == null) {
			abrirRichModalMarketing = Boolean.FALSE;
		}
		return abrirRichModalMarketing;
	}

	public void setAbrirRichModalMarketing(Boolean abrirRichModalMarketing) {
		this.abrirRichModalMarketing = abrirRichModalMarketing;
	}

	/**
	 * @return the abrirRichModalLeituraObrigatoria
	 */
	public Boolean getAbrirRichModalLeituraObrigatoria() {
		if (abrirRichModalLeituraObrigatoria == null) {
			abrirRichModalLeituraObrigatoria = Boolean.FALSE;
		}
		return abrirRichModalLeituraObrigatoria;
	}

	/**
	 * @param abrirRichModalLeituraObrigatoria
	 *            the abrirRichModalLeituraObrigatoria to set
	 */
	public void setAbrirRichModalLeituraObrigatoria(Boolean abrirRichModalLeituraObrigatoria) {
		this.abrirRichModalLeituraObrigatoria = abrirRichModalLeituraObrigatoria;
	}

	public Boolean getIsDigitarMensagem() {
		if (getComunicacaoInternaVO().getDigitarMensagem()) {
			return true;
		}
		return false;
	}

	/**
	 * @return the abrirRichModalMensagem
	 */
	public Boolean getAbrirRichModalMensagem() {
		if (abrirRichModalMensagem == null) {
			abrirRichModalMensagem = Boolean.FALSE;
		}
		return abrirRichModalMensagem;
	}

	/**
	 * @param abrirRichModalMensagem
	 *            the abrirRichModalMensagem to set
	 */
	public void setAbrirRichModalMensagem(Boolean abrirRichModalMensagem) {
		this.abrirRichModalMensagem = abrirRichModalMensagem;
	}

	public String getNomeArquivoAnexo() {
		if (nomeArquivoAnexo == null) {
			nomeArquivoAnexo = "";
		}
		return nomeArquivoAnexo;
	}

	public void setNomeArquivoAnexo(String nomeArquivoAnexo) {
		this.nomeArquivoAnexo = nomeArquivoAnexo;
	}

	/**
	 * @return the mensagemLocalizacao
	 */
	public String getMensagemLocalizacao() {
		if (mensagemLocalizacao == null) {
			mensagemLocalizacao = "";
		}
		return mensagemLocalizacao;
	}

	/**
	 * @param mensagemLocalizacao
	 *            the mensagemLocalizacao to set
	 */
	public void setMensagemLocalizacao(String mensagemLocalizacao) {
		this.mensagemLocalizacao = mensagemLocalizacao;
	}

	/**
	 * @return the qtdMensagemCaixaEntrada
	 */
	public Boolean getApresentarQtdMensagemCaixaEntrada() {
		if (getQtdMensagemCaixaEntrada() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return the qtdMensagemCaixaEntrada
	 */
	public Boolean getApresentarQtdMensagemCaixaEntradaAluno() {
		if (getQtdMensagemCaixaEntrada() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return the qtdMensagemCaixaEntrada
	 */
	public Boolean getApresentarQtdMensagemCaixaEntradaResponsavel() {
		if (getQtdMensagemCaixaEntrada() != 0) {
			return true;
		}
		return false;
	}

	public Integer getQtdMensagemCaixaEntrada() {
		if (qtdMensagemCaixaEntrada == null) {
			qtdMensagemCaixaEntrada = 0;
		}
		return qtdMensagemCaixaEntrada;
	}

	public String getQtdMensagemCaixaEntradaStr() {
		if (getQtdMensagemCaixaEntrada() > 99) {
			return "99";
		} else if (getQtdMensagemCaixaEntrada() < 10) {
			return "0" + getQtdMensagemCaixaEntrada().toString();
		}
		return getQtdMensagemCaixaEntrada().toString();		
	}

	/**
	 * @param qtdMensagemCaixaEntrada
	 *            the qtdMensagemCaixaEntrada to set
	 */
	public void setQtdMensagemCaixaEntrada(Integer qtdMensagemCaixaEntrada) {
		this.qtdMensagemCaixaEntrada = qtdMensagemCaixaEntrada;
	}

	/**
	 * @return the apresentarRichMensagem
	 */
	public String getApresentarRichMensagem() {
		if (apresentarRichMensagem == null) {
			apresentarRichMensagem = "";
		}
		return apresentarRichMensagem;
	}

	/**
	 * @param apresentarRichMensagem
	 *            the apresentarRichMensagem to set
	 */
	public void setApresentarRichMensagem(String apresentarRichMensagem) {
		this.apresentarRichMensagem = apresentarRichMensagem;
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

	/**
	 * @return the apresentarBotaoResponder
	 */
	public Boolean getApresentarBotaoResponder() {
		if (apresentarBotaoResponder == null) {
			apresentarBotaoResponder = Boolean.FALSE;
		}
		return apresentarBotaoResponder;
	}

	/**
	 * @param apresentarBotaoResponder
	 *            the apresentarBotaoResponder to set
	 */
	public void setApresentarBotaoResponder(Boolean apresentarBotaoResponder) {
		this.apresentarBotaoResponder = apresentarBotaoResponder;
	}

//	public ComunicacaoInternaVO getComunicacaoInternaMarketing() {
//		if (comunicacaoInternaMarketing == null) {
//			comunicacaoInternaMarketing = new ComunicacaoInternaVO();
//		}
//		return comunicacaoInternaMarketing;
//	}
//
//	public void setComunicacaoInternaMarketing(ComunicacaoInternaVO comunicacaoInternaMarketing) {
//		this.comunicacaoInternaMarketing = comunicacaoInternaMarketing;
//	}
//
//	public ComunicacaoInternaVO getComunicacaoInternaLeituraObrigatoria() {
//		if (comunicacaoInternaLeituraObrigatoria == null) {
//			comunicacaoInternaLeituraObrigatoria = new ComunicacaoInternaVO();
//		}
//		return comunicacaoInternaLeituraObrigatoria;
//	}
//
//	public void setComunicacaoInternaLeituraObrigatoria(ComunicacaoInternaVO comunicacaoInternaLeituraObrigatoria) {
//		this.comunicacaoInternaLeituraObrigatoria = comunicacaoInternaLeituraObrigatoria;
//	}

	public Boolean getApresentarBotaoExcluir() {
		if (getTipoEntradaSaida().equals("saida") && getUsuarioLogado().getPessoa().getCodigo().equals(getComunicacaoInternaVO().getResponsavel().getCodigo())) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarBotaoAdicionarTodosAlunosTurma() {
		if (getComunicacaoInternaVO().getTurma().getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public String getLabelNaoLidas() throws Exception {
		Date dataIni = null;
		Date dataFim =  null;
		if(Uteis.isAtributoPreenchido(getMesConsulta())) {
		String data = "01/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
		dataIni = Uteis.getDate(data);
		data = "31/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
		dataFim = Uteis.getDate(data);
		}
		if (getUsuarioLogado().getVisaoLogar().equals("")) {
			return "Não Lidas (" + String.valueOf(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaLidasNaoLidasFuncionarioOuAdministrador(getUsuarioLogado().getPessoa().getCodigo(), false, dataIni, dataFim)) + ")";
		} else {
			return "Não Lidas (" + String.valueOf(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaLidasNaoLidas(getUsuarioLogado().getPessoa().getCodigo(), false, dataIni, dataFim)) + ")";
		}
	}

	public String getLabelLidas() throws Exception {
		Date dataIni = null;
		Date dataFim =  null;
		if(Uteis.isAtributoPreenchido(getMesConsulta())) {
			String data = "01/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
			dataIni = Uteis.getDate(data);
			data = "31/" + getMesConsulta() + "/" + String.valueOf(getAnoConsulta());
			dataFim = Uteis.getDate(data);
		}
		if (getUsuarioLogado().getVisaoLogar().equals("")) {
			return "Lidas (" + String.valueOf(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaLidasNaoLidasFuncionarioOuAdministrador(getUsuarioLogado().getPessoa().getCodigo(), true, dataIni, dataFim)) + ")";
		} else {
			return "Lidas (" + String.valueOf(getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaComunicacaoInternaLidasNaoLidas(getUsuarioLogado().getPessoa().getCodigo(), true, dataIni, dataFim)) + ")";
		}
	}

	public List getListaSelectItemTipoAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		itens.add(new SelectItem("todos", "Todos"));
		return itens;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "todos";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public Boolean getMostrarBotoes() {
		if (mostrarBotoes == null) {
			mostrarBotoes = Boolean.FALSE;
		}
		return mostrarBotoes;
	}

	public void setMostrarBotoes(Boolean mostrarBotoes) {
		this.mostrarBotoes = mostrarBotoes;
	}

	public DataTable getComponente() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    UIComponent dataTable = context.getViewRoot().findComponent("form:tabela");
	    return (DataTable) dataTable;
	}

	public String getCaminhoImagemTipoMarketing() {
		if (caminhoImagemTipoMarketing == null) {
			caminhoImagemTipoMarketing = "";
		}
		return caminhoImagemTipoMarketing;
	}

	public void setCaminhoImagemTipoMarketing(String caminhoImagemTipoMarketing) {
		this.caminhoImagemTipoMarketing = caminhoImagemTipoMarketing;
	}

	public String getCampoConsultaCoordenador() {
		if (campoConsultaCoordenador == null) {
			campoConsultaCoordenador = "";
		}
		return campoConsultaCoordenador;
	}

	public void setCampoConsultaCoordenador(String campoConsultaCoordenador) {
		this.campoConsultaCoordenador = campoConsultaCoordenador;
	}

	public String getValorConsultaCoordenador() {
		if (valorConsultaCoordenador == null) {
			valorConsultaCoordenador = "";
		}
		return valorConsultaCoordenador;
	}

	public void setValorConsultaCoordenador(String valorConsultaCoordenador) {
		this.valorConsultaCoordenador = valorConsultaCoordenador;
	}

	public List getListaConsultaCoordenador() {
		if (listaConsultaCoordenador == null) {
			listaConsultaCoordenador = new ArrayList(0);
		}
		return listaConsultaCoordenador;
	}

	public void setListaConsultaCoordenador(List listaConsultaCoordenador) {
		this.listaConsultaCoordenador = listaConsultaCoordenador;
	}

	public ComunicacaoInternaVO getComunicacaoInternaResposta() {
		if (comunicacaoInternaResposta == null) {
			comunicacaoInternaResposta = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaResposta;
	}

	public void setComunicacaoInternaResposta(ComunicacaoInternaVO comunicacaoInternaResposta) {
		this.comunicacaoInternaResposta = comunicacaoInternaResposta;
	}

	public Boolean getInformarEmailManualmente() {
		if (informarEmailManualmente == null) {
			informarEmailManualmente = false;
		}
		return informarEmailManualmente;
	}

	public void setInformarEmailManualmente(Boolean informarEmailManualmente) {
		this.informarEmailManualmente = informarEmailManualmente;
	}

	public void limparDadosDestinatario() {
		this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
		this.getComunicacaoInternaVO().setPessoa(new PessoaVO());
	}

	public Boolean getVisualizarMensagemAlunoPorResponsavelLegal() {
		if (visualizarMensagemAlunoPorResponsavelLegal == null) {
			visualizarMensagemAlunoPorResponsavelLegal = false;
		}
		return visualizarMensagemAlunoPorResponsavelLegal;
	}

	public void setVisualizarMensagemAlunoPorResponsavelLegal(Boolean visualizarMensagemAlunoPorResponsavelLegal) {
		this.visualizarMensagemAlunoPorResponsavelLegal = visualizarMensagemAlunoPorResponsavelLegal;
	}

	public List getListaSelectItemDisciplinaProfessorTurma() {
		if (listaSelectItemDisciplinaProfessorTurma == null) {
			listaSelectItemDisciplinaProfessorTurma = new ArrayList(0);
		}
		return listaSelectItemDisciplinaProfessorTurma;
	}

	public void setListaSelectItemDisciplinaProfessorTurma(List listaSelectItemDisciplinaProfessorTurma) {
		this.listaSelectItemDisciplinaProfessorTurma = listaSelectItemDisciplinaProfessorTurma;
	}

	public String getTelaOrigemCadastro() {
		if (telaOrigemCadastro == null) {
			telaOrigemCadastro = "";
		}
		return telaOrigemCadastro;
	}

	public void setTelaOrigemCadastro(String telaOrigemCadastro) {
		this.telaOrigemCadastro = telaOrigemCadastro;
	}

	public Boolean getMostrarImagem() {
		if (mostrarImagem == null) {
			mostrarImagem = Boolean.FALSE;
		}
		return mostrarImagem;
	}

	public void setMostrarImagem(Boolean mostrarImagem) {
		this.mostrarImagem = mostrarImagem;
	}

	/**
	 * @return the dentroPrazo
	 */
	public Boolean getDentroPrazo() {
		if (dentroPrazo == null) {
			dentroPrazo = Boolean.TRUE;
		}
		return dentroPrazo;
	}

	/**
	 * @param dentroPrazo
	 *            the dentroPrazo to set
	 */
	public void setDentroPrazo(Boolean dentroPrazo) {
		this.dentroPrazo = dentroPrazo;
	}

	public void preencherTodosListaTurma() {
		getFacadeFactory().getComunicacaoInternaFacade().preencherTodosListaTurma(getListaConsultaTurma());
		//realizarVerificacaoNivelEducacionalCursoTurmaSelecionada();
		realizarVerificacaoPeriodicidadeCursoTurmaSelecionada();
	}

	public void desmarcarTodosListaTurma() {
		getFacadeFactory().getComunicacaoInternaFacade().desmarcarTodosListaTurma(getListaConsultaTurma());
	}

	public void realizarVerificacaoNivelEducacionalCursoTurmaSelecionada() {
		setAno("");
		setSemestre("");
		setApresentarModalAnoSemestre(getFacadeFactory().getComunicacaoInternaFacade().realizarVerificacaoNivelEducacionalCursoTurmaSelecionada(getListaConsultaTurma(), getUsuarioLogado()));
	}
	
	private boolean isCursoSemestral() {
		for (TurmaVO turmaVO : getListaConsultaTurma()) {
			if(Uteis.isAtributoPreenchido(turmaVO.getCurso())) {
				if(turmaVO.getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())){
					setApresentarSemestreTurma(Boolean.TRUE);
					setApresentarAnoTurma(Boolean.TRUE);
					return true;
				}
			}else {
				if(turmaVO.getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
					setApresentarSemestreTurma(Boolean.TRUE);
					setApresentarAnoTurma(Boolean.TRUE);
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isCursoAnual() {
		for (TurmaVO turmaVO : getListaConsultaTurma()) {
			if(Uteis.isAtributoPreenchido(turmaVO.getCurso())) {
				if(turmaVO.getCurso().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())){
					setApresentarAnoTurma(Boolean.TRUE);
					return true;
				}
			}else {
				if(turmaVO.getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
					setApresentarAnoTurma(Boolean.TRUE);
					return true;
				}
			}
		}
		return false;
	}
	
	public void realizarVerificacaoPeriodicidadeCursoTurmaSelecionada() {
		if(!isCursoSemestral()) {
			isCursoAnual();
		}
		if(getApresentarSemestreTurma() || getApresentarAnoTurma()) {
			setApresentarModalAnoSemestre(Boolean.TRUE);
		}else {
			setApresentarModalAnoSemestre(Boolean.FALSE);
		}
		
//		for (TurmaVO turmaVO : getListaConsultaTurma()) {
//			if(turmaVO.getCurso().getPeriodicidade().equals("SE")){
//				setApresentarSemestreTurma(Boolean.TRUE);
//			}
//			if(turmaVO.getCurso().getPeriodicidade().equals("AN")){
//				setApresentarAnoTurma(Boolean.TRUE);
//			}
//		}
	}

	public void realizarFechamentoModalAnoSemestre() {
		try {
			validarDadosAnoSemestre();
			setApresentarModalAnoSemestre(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosAnoSemestre() throws Exception {
		if (getApresentarAnoTurma() && getAno().equals("")) {
			throw new Exception("O campo ANO deve ser informado.");
		}
		if (getApresentarSemestreTurma() && getSemestre().equals("")) {
			throw new Exception("O campo SEMESTRE deve ser informado.");
		}
	}

	public Boolean getFecharModalTurma() {
		if (fecharModalTurma == null) {
			fecharModalTurma = Boolean.FALSE;
		}
		return fecharModalTurma;
	}

	public void setFecharModalTurma(Boolean fecharModalTurma) {
		this.fecharModalTurma = fecharModalTurma;
	}

	public List getListaSelectSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public String getAbrirModalAnoSemestre() {
		if (getApresentarModalAnoSemestre()) {
			return "PF('panelAnoSemestre').show();";
		}
		return "";
	}

	public String getFecharModalAnoSemestre() {
		if (!getApresentarModalAnoSemestre()) {
			return "PF('panelAnoSemestre').hide();";
		}
		return "";
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

	public Boolean getApresentarModalAnoSemestre() {
		if (apresentarModalAnoSemestre == null) {
			apresentarModalAnoSemestre = Boolean.FALSE;
		}
		return apresentarModalAnoSemestre;
	}

	public void setApresentarModalAnoSemestre(Boolean apresentarModalAnoSemestre) {
		this.apresentarModalAnoSemestre = apresentarModalAnoSemestre;
	}

	public Boolean getDestinatarioAA() {
		if(DestinatarioAA == null) {
			DestinatarioAA = Boolean.FALSE;
		}
		return DestinatarioAA;
	}

	public void setDestinatarioAA(Boolean destinatarioAA) {
		DestinatarioAA = destinatarioAA;
	}

	public Boolean getDestinatarioTD() {
		if (DestinatarioTD == null) {
			DestinatarioTD = Boolean.FALSE;
		}
		return DestinatarioTD;
	}

	public void setDestinatarioTD(Boolean destinatarioTD) {
		DestinatarioTD = destinatarioTD;
	}

	public String getCampoConsultaTurmaDisciplina() {
		if (campoConsultaTurmaDisciplina == null) {
			campoConsultaTurmaDisciplina = "";
		}
		return campoConsultaTurmaDisciplina;
	}

	public void setCampoConsultaTurmaDisciplina(String campoConsultaTurmaDisciplina) {
		this.campoConsultaTurmaDisciplina = campoConsultaTurmaDisciplina;
	}

	public String getValorConsultaTurmaDisciplina() {
		if (valorConsultaTurmaDisciplina == null) {
			valorConsultaTurmaDisciplina = "";
		}
		return valorConsultaTurmaDisciplina;
	}

	public void setValorConsultaTurmaDisciplina(String valorConsultaTurmaDisciplina) {
		this.valorConsultaTurmaDisciplina = valorConsultaTurmaDisciplina;
	}

	public List getListaConsultaTurmaDisciplina() {
		if (listaConsultaTurmaDisciplina == null) {
			listaConsultaTurmaDisciplina = new ArrayList(0);
		}
		return listaConsultaTurmaDisciplina;
	}

	public void setListaConsultaTurmaDisciplina(List listaConsultaTurmaDisciplina) {
		this.listaConsultaTurmaDisciplina = listaConsultaTurmaDisciplina;
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

	public Boolean verificarUsuarioPossuiPermissaoNovo(String identificadorAcaoPermissao) {
		Boolean liberar = Boolean.FALSE;
		try {
			//getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao, getUsuarioLogado());
			getFacadeFactory().getControleAcessoFacade().incluir(identificadorAcaoPermissao, true, getUsuarioLogado());
			liberar = Boolean.TRUE;
		} catch (Exception e) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	public Boolean getDestinatarioTF() {
		if (DestinatarioTF == null) {
			DestinatarioTF = false;
		}
		return DestinatarioTF;
	}

	public void setDestinatarioTF(Boolean destinatarioTF) {
		DestinatarioTF = destinatarioTF;
	}

	public Boolean getDestinatarioTR() {
		if (DestinatarioTR == null) {
			DestinatarioTR = false;
		}
		return DestinatarioTR;
	}

	public void setDestinatarioTR(Boolean destinatarioTR) {
		DestinatarioTR = destinatarioTR;
	}
	
	public void adicionarTodosCoordenadores() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaTodosCoordenadores(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), "TR", getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			// System.out.println("Erro adicionarTodosCoordenadores: " + e.getMessage());
		}
	}
	
	public void adicionarTodosFuncionarios() {
		try {
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getFacadeFactory().getPessoaFacade().consultaRapidaTodosFuncionarios(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), "TF", getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} catch (Exception e) {
			// System.out.println("Erro adicionarTodosFuncionarios: " + e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoNivelEducacional() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class);
	}

	public String getTipoNivelEducacional() {
		if (tipoNivelEducacional == null) {
			tipoNivelEducacional = "";
		}
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(String tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public Boolean getApresentarBotaoEnviarResposta() {
		if (apresentarBotaoEnviarResposta == null) {
			apresentarBotaoEnviarResposta = Boolean.FALSE;
		}
		return apresentarBotaoEnviarResposta;
	}

	public void setApresentarBotaoEnviarResposta(Boolean apresentarBotaoEnviarResposta) {
		this.apresentarBotaoEnviarResposta = apresentarBotaoEnviarResposta;
	}

	public Boolean getDestinatarioRL() {
		if (DestinatarioRL == null) {
			DestinatarioRL = Boolean.FALSE;
		}
		return DestinatarioRL;
	}

	public void setDestinatarioRL(Boolean destinatarioRL) {
		DestinatarioRL = destinatarioRL;
	}

	public Boolean getDestinatarioTT() {
		if (DestinatarioTT == null) {
			DestinatarioTT = Boolean.FALSE;
		}
		return DestinatarioTT;
	}

	public void setDestinatarioTT(Boolean destinatarioTT) {
		DestinatarioTT = destinatarioTT;
	}
	
	public Boolean getApresentarCheckboxCopiaFollowUp() {
		return getComunicacaoInternaVO().getTipoDestinatario().equals("AL") || getComunicacaoInternaVO().getTipoDestinatario().equals("TA") ||getComunicacaoInternaVO().getTipoDestinatario().equals("AA");
	}

	public Boolean getApresentarVisaoAluno(){
		return getLoginControle().getPermissaoAcessoMenuVO().getComunicacaoInterna() 
				&& (verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaProfessor") || verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaTodosProfessores"));
	}

	public String getFiltrarPorAssunto() {
		if (filtrarPorAssunto == null) {
			filtrarPorAssunto = "";
		}
		return filtrarPorAssunto;
	}

	public void setFiltrarPorAssunto(String filtrarPorAssunto) {
		this.filtrarPorAssunto = filtrarPorAssunto;
	}

	public String getFiltrarPorNomeResponsavel() {
		if (filtrarPorNomeResponsavel == null) {
			filtrarPorNomeResponsavel = "";
		}
		return filtrarPorNomeResponsavel;
	}

	public void setFiltrarPorNomeResponsavel(String filtrarPorNomeResponsavel) {
		this.filtrarPorNomeResponsavel = filtrarPorNomeResponsavel;
	}

	public String getFiltrarPorData() {
		if (filtrarPorData == null) {
			filtrarPorData = "";
		}
		return filtrarPorData;
	}

	public void setFiltrarPorData(String filtrarPorData) {
		this.filtrarPorData = filtrarPorData;
	}
	

	public Boolean getApresentarEnviarResponsavelFinanceiro() {
		if (apresentarEnviarResponsavelFinanceiro == null) {
			apresentarEnviarResponsavelFinanceiro = Boolean.FALSE;
		}
		return apresentarEnviarResponsavelFinanceiro;
	}

	public void setApresentarEnviarResponsavelFinanceiro(Boolean apresentarEnviarResponsavelFinanceiro) {
		this.apresentarEnviarResponsavelFinanceiro = apresentarEnviarResponsavelFinanceiro;
	}

	public Boolean getApresentarEnviarPais() {
		if (apresentarEnviarPais == null) {
			apresentarEnviarPais = Boolean.FALSE;
		}
		return apresentarEnviarPais;
	}

	public void setApresentarEnviarPais(Boolean apresentarEnviarPais) {
		this.apresentarEnviarPais = apresentarEnviarPais;
	}
	
	private void executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro() throws Exception {
		if (getComunicacaoInternaVO().getEnviarCopiaPais() || getComunicacaoInternaVO().getEnviarCopiaResponsavelFinanceiro() || getComunicacaoInternaVO().getEnviarSomenteResponsavelFinanceiro()) {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>(0);
			for (ComunicadoInternoDestinatarioVO cidVO : getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs()) {
				if (getComunicacaoInternaVO().getEnviarCopiaPais()) {
					List<FiliacaoVO> pais = getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(cidVO.getDestinatario().getCodigo(), false, getUsuario());
					for (FiliacaoVO pai : pais) {
						pessoaVOs.add(pai.getPais());
					}
				}
				if (getComunicacaoInternaVO().getEnviarCopiaResponsavelFinanceiro() || getComunicacaoInternaVO().getEnviarSomenteResponsavelFinanceiro()) {
					PessoaVO responsavelFinanceiro = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(cidVO.getDestinatario().getCodigo(), getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(responsavelFinanceiro) && !pessoaVOs.contains(responsavelFinanceiro)) {
						pessoaVOs.add(responsavelFinanceiro);
					}
				}
			}
			if (getComunicacaoInternaVO().getEnviarSomenteResponsavelFinanceiro()) {
				getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			}
			for (PessoaVO pessoa : pessoaVOs) {
				ComunicadoInternoDestinatarioVO comunicadoInternoDestinatario = new ComunicadoInternoDestinatarioVO();
				comunicadoInternoDestinatario.setDestinatario(pessoa);
				comunicadoInternoDestinatario.setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
				getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatario);
			}
		}
	}
	
	public Boolean getHabilitarMsgErro() {
		if(habilitarMsgErro == null){
			habilitarMsgErro = false;
		}
		return habilitarMsgErro;
	}

	public void setHabilitarMsgErro(Boolean habilitarMsgErro) {
		this.habilitarMsgErro = habilitarMsgErro;
	}
	
	public boolean getIsApresentarCheckboxPais() {
		return getApresentarEnviarPais() && (getDestinatarioAL() || getDestinatarioTU() || getDestinatarioTD() || getDestinatarioTA() || getDestinatarioTC() || getDestinatarioAA());
	}

	public boolean getIsApresentarCheckboxResposavelFinanceiro() {
		return getApresentarEnviarResponsavelFinanceiro() && (getDestinatarioAL() || getDestinatarioTU() || getDestinatarioTD() || getDestinatarioTA() || getDestinatarioTC() || getDestinatarioAA());
	}
	
	public void consultarUnidadeEnsino() {
		try {
			if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getUnidadeEnsino())) {
				getComunicacaoInternaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));				
			}
			getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(getComunicacaoInternaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getImprimirEmail() {
		if(imprimirEmail == null){
			imprimirEmail = false;
		}
		return imprimirEmail;
	}

	public void setImprimirEmail(Boolean imprimirEmail) {		
		this.imprimirEmail = imprimirEmail;
	}

	public String getUrlImprimirEmail() {
		if (getImprimirEmail()) {
			return "abrirPopup('../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		}
		return "";
	}
	
	public void imprimirEmailHtml() {
		try {
			limparMensagem();
			this.setCaminhoRelatorio("");			
//			this.setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarConversaoHtmlParaPdfComunicadoInterno(getComunicacaoInternaVO(), false, getUsuarioLogado()));						
			setImprimirEmail(true);	
			setMensagemID("msg_impressaoContrato_contratoDeclaracao");
		} catch (Exception e) {
			setImprimirEmail(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirEmail() {
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");			
//			this.setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarConversaoHtmlParaPdfComunicadoInterno(getComunicacaoInternaVO(), true, getUsuarioLogado()));
			setFazerDownload(true);		
			setMensagemID("msg_impressaoContrato_contratoDeclaracao");
		} catch (Exception e) {			
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void encaminharComunicadoInterno(){
		try {
			
			String texto = getComunicacaoInternaVO().getMensagem();
	    	int parametro = texto.indexOf("<div");
	    	String textoAntes = texto.substring(0, parametro);
			String textoDepois = texto.substring(parametro, texto.length());
			texto = textoAntes + getComunicacaoInternaVO().getEncaminharMensagemComLayout("") + textoDepois;
			
			getComunicacaoInternaVO().setCodigo(0);
			getComunicacaoInternaVO().setNovoObj(true);
			getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
			getComunicacaoInternaVO().setTipoComunicadoInterno("LE");
			getComunicacaoInternaVO().setTipoDestinatario("");
			getComunicacaoInternaVO().setAssunto("");
			getComunicacaoInternaVO().setFuncionario(new FuncionarioVO());
			getComunicacaoInternaVO().setFuncionarioNome("");
			getComunicacaoInternaVO().setProfessorNome("");
			getComunicacaoInternaVO().setProfessor(new PessoaVO());
			getComunicacaoInternaVO().setAluno(new PessoaVO());
			getComunicacaoInternaVO().setAlunoNome("");
			getComunicacaoInternaVO().setCargo(new CargoVO());
			getComunicacaoInternaVO().setDepartamento(new DepartamentoVO());
			getComunicacaoInternaVO().setTurma(new TurmaVO());
			getComunicacaoInternaVO().setDisciplina(new DisciplinaVO());
//			getComunicacaoInternaVO().setAreaConhecimento(new AreaConhecimentoVO());	
			getComunicacaoInternaVO().setData(new Date());
			getComunicacaoInternaVO().setDataExibicaoInicial(new Date());
			getComunicacaoInternaVO().setDataExibicaoFinal(new Date());
			getComunicacaoInternaVO().setPrioridade("NO");
			getComunicacaoInternaVO().setImgCima("cima_sei");
			getComunicacaoInternaVO().setImgBaixo("baixo_sei");			
			novoVisaoAlunoProfessorCoordenador();
			getComunicacaoInternaVO().setMensagem(texto);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {			
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPermitirResponderComunicadoInternoSomenteLeitura() {
		if(permitirResponderComunicadoInternoSomenteLeitura == null){
			if(!getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				try{
					getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("ComunicacaoInterna_permitirResponderComunicadoInternoSomenteLeitura", getUsuarioLogado());
					permitirResponderComunicadoInternoSomenteLeitura = true;
				}catch(Exception e){
					permitirResponderComunicadoInternoSomenteLeitura = false;
				}
			}else{
				permitirResponderComunicadoInternoSomenteLeitura =  false;
			}
		}
		return permitirResponderComunicadoInternoSomenteLeitura;
	}

	public void setPermitirResponderComunicadoInternoSomenteLeitura(Boolean permitirResponderComunicadoInternoSomenteLeitura) {
		this.permitirResponderComunicadoInternoSomenteLeitura = permitirResponderComunicadoInternoSomenteLeitura;
	}
	
	public void scrollListenerVisaoAluno() {
		
		try {
			consultarTodasEntradasAluno();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scrollListenerVisaoProfessor() {
		
		try {
			consultarTodasEntradasProfessor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scrollListenerVisaoCoordenador() {
		
		try {
			consultarTodasEntradasCoordenador();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void scrollListenerItemsSaida () {
		
		try {
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void inicializarDadosControleConsultaOtimizado() {
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
//		if (!getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
//			getControleConsultaOtimizado().setLimitePorPagina(10);
//		}
	}
	
	private void inicializarDadosFiltros() {
		setFiltrarPorAssunto("");
		setFiltrarPorNomeResponsavel("");
		setFiltrarPorData("");
	}
	
	public void filtroConsultaVisaoAdministrativo() {
		inicializarDadosControleConsultaOtimizado();
		consultar();
	}
	
	public void filtroConsultaVisaoAluno() {
		inicializarDadosControleConsultaOtimizado();
		try {
			consultarTodasEntradasAluno();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void filtroConsultaVisaoProfessor() {
		inicializarDadosControleConsultaOtimizado();
		try {
			consultarTodasEntradasProfessor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void filtroConsultaVisaoCoordenador() {
		inicializarDadosControleConsultaOtimizado();
		try {
			consultarTodasEntradasCoordenador();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void filtroConsultaSaidaVisaoAlunoProfessorCoordenador() {
		inicializarDadosControleConsultaOtimizado();
		consultar();
	}

	public void removerArquivoAnexo() {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoAnexo");
		int index = 0;
		for (ArquivoVO arqExcluido : getComunicacaoInternaVO().getListaArquivosAnexo()) {
			if (arquivoVO.getDescricao().equals(arqExcluido.getDescricao())) {
				getComunicacaoInternaVO().getListaArquivosAnexo().remove(index);
				return;
			}
			index++;
		}
	}
	
	public String getLabelArquivosAnexados() {
		labelArquivosAnexos = "Arquivos Anexados ("+ String.valueOf(getComunicacaoInternaVO().getListaArquivosAnexo().size() + ")");
		return labelArquivosAnexos;
	}
	
	public Boolean getApresentarBotaoExcluirSaidas() {
		if (getTipoEntradaSaida().equals("saida")) {
			setExisteConsulta(Boolean.FALSE);
			return true;
		}
		return false;
	}

	public Boolean getDestinatarioALAS() {
		if (DestinatarioALAS == null) {
			DestinatarioALAS = false;
		}
		return DestinatarioALAS;
	}

	public void setDestinatarioALAS(Boolean destinatarioALAS) {
		DestinatarioALAS = destinatarioALAS;
	}
	
	public void adicionarTodosAlunosCursoTurmaAnoSemestre() {
		try {
			if (getHabilitarFiltroCursoTurma() && !Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getCurso().getCodigo()) && !Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getTurma().getCodigo())) {
				throw new Exception("O Campo (Habilitar Filtro Curso/Turma) Está Marcado.Deve Ser Selecionado Pelo Menos Um Curso para filtrar os Alunos.");
			}
			if (!getTrazerApenasAlunosNaoRenovaram()) {
				if(getProgramacaoFormaturaVO().getCodigo().equals(0)) {
					if (!Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getAno()) && getIsApresentarAno()) {
						throw new Exception("O Campo (ANO) Deve Ser Informado.");
					}
				}
//				getFacadeFactory().getPessoaFacade().consultaRapidaAlunoPorCursoTurmaAnoSemestreSitacaoAcademica(getComunicacaoInternaVO().getUnidadeEnsino().getCodigo().intValue(), getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getNivelEducacional(), getIdentificadorSalaAulaBlackboard(), getPercentualIntegralizacaoCurricularInicial(), getPercentualIntegralizacaoCurricularFinal(), getProgramacaoFormaturaVO(), getBimestre(), getTipoDeficiencia(), getDiaSemana(), getDisciplinaVO());
			} else {
				if (!Uteis.isAtributoPreenchido(getAnoRenovacao())) {
					throw new Exception("O Campo (ANO da Renovação) Deve Ser Informado.");
				}
				if (!Uteis.isAtributoPreenchido(getAnoRenovacao()) && getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)) {
					throw new Exception("O Campo (SEMESTRE da Renovação) Deve Ser Informado.");
				}
				getFacadeFactory().getPessoaFacade().consultaAlunosNaoRenovaramMatriculaUltimoSemestre(getPeriodicidade(), getAnoRenovacao(), getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL) ? getSemestreRenovacao() : "0", getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), getComunicacaoInternaVO().getCurso().getCodigo(), getComunicacaoInternaVO().getTurma().getCodigo(), false, getListaPessoaDestinatario(), getComunicacaoInternaVO(), getComunicadoInternoDestinatarioVO(), getNivelEducacional(), getIdentificadorSalaAulaBlackboard(), getPercentualIntegralizacaoCurricularInicial(), getPercentualIntegralizacaoCurricularFinal(), getProgramacaoFormaturaVO(), getBimestre(), getTipoDeficiencia(), getDiaSemana(), getDisciplinaVO());
			}
			executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
			if(getComunicacaoInternaVO().getTotalizadorDestinatario() < 1) {
				setMensagemID("Foi adicionado nenhum registro na lista de destinatário");
			}else if(getComunicacaoInternaVO().getTotalizadorDestinatario() == 1){
				setMensagemID("Foi adicionado " + getComunicacaoInternaVO().getTotalizadorDestinatario() + " registro na lista de destinatário");
			}else {
				setMensagemID("Foram adicionados " + getComunicacaoInternaVO().getTotalizadorDestinatario() + " registros na lista de destinatário");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public Boolean getHabilitarFiltroCursoTurma() {
		if (habilitarFiltroCursoTurma == null) {
			habilitarFiltroCursoTurma = false;
		}
		return habilitarFiltroCursoTurma;
	}

	public void setHabilitarFiltroCursoTurma(Boolean habilitarFiltroCursoTurma) {
		this.habilitarFiltroCursoTurma = habilitarFiltroCursoTurma;
	}
	
	public boolean getIsApresentarAno() {

			if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getCurso()) && (getComunicacaoInternaVO().getCurso().getPeriodicidade().equals("SE") || getComunicacaoInternaVO().getCurso().getPeriodicidade().equals("AN"))) {
				return true;
			} else if(Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getTurma()) && (getComunicacaoInternaVO().getTurma().getPeriodicidade().equals("SE") || getComunicacaoInternaVO().getTurma().getPeriodicidade().equals("AN"))) {
				return true;
			}
			else if (!getHabilitarFiltroCursoTurma()) {
				return true;
			} else {
				getComunicacaoInternaVO().setAno("");
				getComunicacaoInternaVO().setSemestre("");
			}
		
			return false;
	}
	
	/*public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademico() {
		if (filtroRelatorioAcademico == null) {
			filtroRelatorioAcademico = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademico;
	}

	public void setFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademico) {
		this.filtroRelatorioAcademico = filtroRelatorioAcademico;
	}*/
	
	public void selecionarCurso() {
		try {
	        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("listaCurso");
	        getComunicacaoInternaVO().setCurso(obj);
	        listaConsultaCurso.clear();
	        setValorConsultaCurso("");
			setCampoConsultaCurso("");
	        getComunicacaoInternaVO().setTurma(null);
		} catch (Exception e) {
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCurso() {
		try {
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getComunicacaoInternaVO().getUnidadeEnsino().getCodigo() ,false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  getUsuarioLogado()));
			}
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					throw new Exception("O Código do (CURSO) deve ser Informado.");
				}
				setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(Integer.parseInt(getValorConsultaCurso()),getComunicacaoInternaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso("");
			getComunicacaoInternaVO().setTurma(null);
			getComunicacaoInternaVO().setCurso(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List getListaConsultaCurso() {
		if(listaConsultaCurso == null){
			listaConsultaCurso = new ArrayList();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	} 
	
	public String getCampoConsultaCurso() {
		if(campoConsultaCurso == null){
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null){
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	
	public List getListaSelectTipoConsultaCurso() {
		List listaTipoConsultaComboCurso = new ArrayList(0);
		listaTipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		listaTipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		return listaTipoConsultaComboCurso;

	}
	
	public String getIsFecharModalFiltroAlunoAnoSemestre() {
		if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getAno()) && getIsApresentarAno() && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs() )) {
			getComunicacaoInternaVO().setAno("");
	        limparCurso();
	        limparDadosTurma();
	        
			getComunicacaoInternaVO().setSemestre("");
			return "";
		}else if (getComunicacaoInternaVO().getCurso().getPeriodicidade().equals("IN") && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs() )){
			setHabilitarFiltroCursoTurma(false);
	        limparCurso();
	        limparDadosTurma();
	        
			return "";
		}
		return "PF('panelAlunoAnoSemestre').hide()";
	}
	
	public String getIsRenderizarModalFiltroAlunoAnoSemestre() {
		if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getAno()) && getIsApresentarAno() && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs() ) ) {
			return "form:tabDestinatario, form:tabPanelBasico, formAlunoAnoSemestre, form";
		}else if (getTrazerApenasAlunosNaoRenovaram() || (getComunicacaoInternaVO().getCurso().getPeriodicidade().equals("IN") && Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs() ))) {
			return "form:tabDestinatario, form:tabPanelBasico, formAlunoAnoSemestre, form";
		}
		return "mensagemConsultaAluno";
	}
	
	public void limparDadosFiltroAlunoAnoSemestre() throws Exception {
		limparCurso();
		limparDadosTurma();
		setHabilitarFiltroCursoTurma(null);
		
		setMensagem("");
		setMensagemDetalhada("");
		setAnoRenovacao("");
		setSemestreRenovacao("");
		setTrazerApenasAlunosNaoRenovaram(false);
		limparProgramacaoFormatura();
		limparDisciplina();
		setPercentualIntegralizacaoCurricularInicial(null);
		setPercentualIntegralizacaoCurricularFinal(null);
		setIdentificadorSalaAulaBlackboard("");
//		setTipoDeficiencia(TipoDeficiencia.NAO_DECLARADO);
		setDiaSemana(DiaSemana.NENHUM);
		setBimestre(null);
	}
	
	public boolean getIsApresentarSemestre() {
		if (Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getCurso()) && getComunicacaoInternaVO().getCurso().getPeriodicidade().equals("SE")) {
			getComunicacaoInternaVO().setSemestre(Uteis.getSemestreAtual());
			return true;		
		} else if(Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getTurma()) && getComunicacaoInternaVO().getTurma().getPeriodicidade().equals("SE")) {
			getComunicacaoInternaVO().setSemestre(Uteis.getSemestreAtual());
			return true;	
		}
		else if (!getHabilitarFiltroCursoTurma()) {
			return true;
		} else {
			getComunicacaoInternaVO().setSemestre("");
		}
		return false;
	}
	
	public Boolean getConsultarSemConsiderarData() {
		if (consultarSemConsiderarData == null) {
			consultarSemConsiderarData = Boolean.TRUE;
		}
		return consultarSemConsiderarData;
	}
	
	public void setConsultarSemConsiderarData(Boolean consultarSemConsiderarData) {
		this.consultarSemConsiderarData = consultarSemConsiderarData;
	}
	
	public void consultarTodasEntradasPorIntervaloData() {
		setConsultarSemConsiderarData(Boolean.FALSE);
		consultarTodasEntrada();
	}
	
	public String getCssPanelGridEnviarResposta() {
		return getLerComunicado() ? "inputTextOcultoRegistroAulaNota" : "colunaCentralizada";
	}
	
	public String getCssBotaoMarcarComunicadoInternoLido() {
		if (!getLerComunicado()) {
			return getResponderComunicado() ? "inputTextOcultoRegistroAulaNota" : "button1";
		}
		if (!getApresentarBotaoResponder()) {
			return getResponderComunicado() ? "inputTextOcultoRegistroAulaNota" : "button1";
		}
		return "inputTextOcultoRegistroAulaNota";
	}

	public Boolean getApresentarBotaoNovoRecado() {
		if (apresentarBotaoNovoRecado == null) {
			apresentarBotaoNovoRecado = Boolean.TRUE;
		}
		return apresentarBotaoNovoRecado;
	}

	public void setApresentarBotaoNovoRecado(Boolean apresentarBotaoNovoRecado) {
		this.apresentarBotaoNovoRecado = apresentarBotaoNovoRecado;
	}
	
	public Boolean getApresentarBotaoAcessarRequerimento() {
		if(apresentarBotaoAcessarRequerimento == null) {
			apresentarBotaoAcessarRequerimento =  Boolean.FALSE;
		}
		return apresentarBotaoAcessarRequerimento;
	}

	public void setApresentarBotaoAcessarRequerimento(Boolean apresentarBotaoAcessarRequerimento) {
		this.apresentarBotaoAcessarRequerimento = apresentarBotaoAcessarRequerimento;
	}

	public boolean getIsApresentarTipoDestinatarioComunicacaoInternaAluno() {
		try {
			return getListaSelectItemTipoDestinatarioComunicacaoInternaAluno().size() > 1;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void retirarDestinatarioEscolhidoDaLista(List<PessoaVO> listaDestinatario, Integer destinatario) throws Exception{
    	listaDestinatario.removeIf(p -> p.getCodigo().equals(destinatario));
    }
	
		
	public Object getDestinatarioVOs(){
		if(getComunicacaoInternaVO().getNovoObj()) {
			return getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs();
}
		return getComunicacaoInternaVO().getListaConsultaDestinatario();
	}
		
	public void paginarConsultaDestinatarios(){
		try {
			
			getComunicacaoInternaVO().getListaConsultaDestinatario().setListaConsulta(getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarPorCodigoComunicacaoInterna(getComunicacaoInternaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getComunicacaoInternaVO().getListaConsultaDestinatario().getLimitePorPagina(), getComunicacaoInternaVO().getListaConsultaDestinatario().getOffset()));
		}catch (Exception e) {
			
		}
		
	}
	
	public Boolean getPermitirEnviarComunicadoParaMeusAmigos() {
		if (permitirEnviarComunicadoParaMeusAmigos == null) {
			return verificarUsuarioPossuiPermissaoConsulta("ComunicacaoInterna_enviarParaMeusAmigos");
		}
		return permitirEnviarComunicadoParaMeusAmigos;
	}

	public void setPermitirEnviarComunicadoParaMeusAmigos(Boolean permitirEnviarComunicadoParaMeusAmigos) {
		this.permitirEnviarComunicadoParaMeusAmigos = permitirEnviarComunicadoParaMeusAmigos;
	}
	
	public boolean getApresentarCampoSemestre() {
		
		if (getComunicacaoInternaVO().getTurma().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor()) && getDestinatarioTD() && getComunicacaoInternaVO().getTurma().getCodigo() != 0) {
			return true;
		}else {
		    return false;	
		}
	}
	
	public boolean getApresentarCampoAno() {
		
		if ((getComunicacaoInternaVO().getTurma().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor()) || getComunicacaoInternaVO().getTurma().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) && getDestinatarioTD() && getComunicacaoInternaVO().getTurma().getCodigo() != 0) {
			return true;
		}else {
		    return false;	
		}
	}
	
	public void realizarCarregamentoComunicacaoInternaInteracaoWorkflow(PessoaVO pessoaVO) {
		if (pessoaVO != null && !pessoaVO.getCodigo().equals(0)) {
			try {
				if (pessoaVO.getAluno()) {
					this.getComunicacaoInternaVO().setAluno(pessoaVO);
					this.getComunicacaoInternaVO().setAlunoNome(pessoaVO.getNome());
					this.getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.AL.name());
					getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
					getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
					executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
					retirarDestinatarioEscolhidoDaLista(this.getListaConsultaAluno(), pessoaVO.getCodigo());
					setMensagemID("msg_dados_adicionados");
					context().getExternalContext().getSessionMap().put("comunicacaoInternaFichaAluno", getComunicacaoInternaVO());
				}else {
					this.getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.TC.name());
					getComunicadoInternoDestinatarioVO().setDestinatario(pessoaVO);
					getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
					getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
					this.setComunicadoInternoDestinatarioVO(new ComunicadoInternoDestinatarioVO());
					getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
					retirarDestinatarioEscolhidoDaLista(this.getListaConsultaAluno(), pessoaVO.getCodigo());
					setMensagemID("msg_dados_adicionados");
					context().getExternalContext().getSessionMap().put("comunicacaoInternaFichaAluno", getComunicacaoInternaVO());
			}
			 }catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} 
		}
	}

	public Boolean getApresentarSemestreTurma() {
		if(apresentarSemestreTurma == null) {
			apresentarSemestreTurma = Boolean.FALSE;
		}
		return apresentarSemestreTurma;
	}

	public void setApresentarSemestreTurma(Boolean apresentarSemestreTurma) {
		this.apresentarSemestreTurma = apresentarSemestreTurma;
	}

	public Boolean getApresentarAnoTurma() {
		if(apresentarAnoTurma == null) {
			apresentarAnoTurma = Boolean.FALSE;
		}
		return apresentarAnoTurma;
	}

	public void setApresentarAnoTurma(Boolean apresentarAnoTurma) {
		this.apresentarAnoTurma = apresentarAnoTurma;
	}
	
	
	public List getListaSelectItemSemestre() {
		List<SelectItem> listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre.add(new SelectItem("", ""));
		listaSelectItemSemestre.add(new SelectItem("1", "1º"));
		listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		return listaSelectItemSemestre;
	}
	
	
	
	public Boolean getPermitirEnviarComunicadoParaTurmaPeriodoAnterior() {
		if (permitirEnviarComunicadoParaTurmaPeriodoAnterior == null) {
			return permitirEnviarComunicadoParaTurmaPeriodoAnterior = Boolean.FALSE;
		}
		return permitirEnviarComunicadoParaTurmaPeriodoAnterior;
	}

	public void setPermitirEnviarComunicadoParaTurmaPeriodoAnterior(Boolean permitirEnviarComunicadoParaTurmaPeriodoAnterior) {
		this.permitirEnviarComunicadoParaTurmaPeriodoAnterior = permitirEnviarComunicadoParaTurmaPeriodoAnterior;
	}
	
	public void realizarUploadMarketing(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getComunicacaoInternaVO().getArquivoAnexo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			getComunicacaoInternaVO().getArquivoAnexo().setCaminhoImagemAnexo(getFacadeFactory().getArquivoHelper().renderizarAnexo(getComunicacaoInternaVO().getArquivoAnexo(), getComunicacaoInternaVO().getArquivoAnexo().getPastaBaseArquivo(), getConfiguracaoGeralPadraoSistema(), "", "", false));
			getComunicacaoInternaVO().getArquivoAnexo().setExtensao(getFacadeFactory().getArquivoHelper().getExtensaoArquivo(getComunicacaoInternaVO().getArquivoAnexo().getNome()));
			getFacadeFactory().getArquivoFacade().validarDadosExtensaoArquivoTipoMarketing(getComunicacaoInternaVO().getArquivoAnexo(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			removerArquivoAnexoMarketing();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}
	
	public void removerArquivoAnexoMarketing() {
		getComunicacaoInternaVO().setArquivoAnexo(null);
		setMensagemID("msg_dados_excluidos");
	}
	
	public Boolean getTrazerApenasAlunosNaoRenovaram() {
		if (trazerApenasAlunosNaoRenovaram == null) {
			trazerApenasAlunosNaoRenovaram = false;
		}
		return trazerApenasAlunosNaoRenovaram;
	}

	public void setTrazerApenasAlunosNaoRenovaram(Boolean trazerApenasAlunosNaoRenovaram) {
		this.trazerApenasAlunosNaoRenovaram = trazerApenasAlunosNaoRenovaram;
	}

	public String getAnoRenovacao() {
		return anoRenovacao;
	}

	public void setAnoRenovacao(String anoRenovacao) {
		if (anoRenovacao == null) {
			anoRenovacao = "";
		}
		this.anoRenovacao = anoRenovacao;
	}

	public String getSemestreRenovacao() {
		if (semestreRenovacao == null) {
			semestreRenovacao = "";
		}
		return semestreRenovacao;
	}

	public void setSemestreRenovacao(String semestreRenovacao) {
		this.semestreRenovacao = semestreRenovacao;
	}
	
	public PeriodicidadeEnum getPeriodicidade() {
		if(periodicidade == null){
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if(listaSelectItemPeriodicidade == null){
			listaSelectItemPeriodicidade =  new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}
	
	public void consultarTodasEntradaMarketingLeituraObrigatoriaAlunoProfessorCoordenador() {
		Integer indexMarketing = 1;
		Integer indexLeituraObrigatoria = 1;
		setAbrirRichModalMensagem(false);
		setLerComunicado(false);
		setResponderComunicado(false);
		setApresentarBotaoResponder(false);
		setAbrirRichModalMarketing(false);
		setComunicacaoInternaVO(new ComunicacaoInternaVO());
		try {
			List<ComunicacaoInternaVO> objs = getFacadeFactory().getComunicacaoInternaFacade().consultaRapidaPorEntradaLimiteMarketingLeituraObrigatoria(getUsuarioLogado().getPessoa().getCodigo(), 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!objs.isEmpty()) {
				for (ComunicacaoInternaVO comunicacaoInterna : objs) {
					if ((getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() || getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) 
							&& comunicacaoInterna.getMensagem().contains("../../resources/")) {
						String replace = "";
						replace = comunicacaoInterna.getMensagem().replaceAll("../../resources/", "../resources/");
						comunicacaoInterna.setMensagem(replace);
					}
					comunicacaoInterna.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(comunicacaoInterna.getMensagem(), getUsuarioLogado().getPessoa()));
					if (comunicacaoInterna.getTipoMarketing()) {
						if (indexMarketing == 1) {
							setComunicacaoInternaVO(comunicacaoInterna);
							if (getComunicacaoInternaVO().getListaArquivosAnexo().isEmpty() && getComunicacaoInternaVO().getArquivoAnexo().getCodigo() > 0) {
								getComunicacaoInternaVO().getListaArquivosAnexo().add(getComunicacaoInternaVO().getArquivoAnexo());
							}
							if (!getComunicacaoInternaVO().getListaArquivosAnexo().isEmpty()) {
								ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
								for (ArquivoVO arquivoVO : getComunicacaoInternaVO().getListaArquivosAnexo()) {
									getComunicacaoInternaVO().setArquivoAnexo(arquivoVO);
									setAbrirRichModalMarketing(true);
									setLerComunicado(true);
									setApresentarBotaoResponder(comunicacaoInterna.getTipoComunicadoInterno().equals("RE"));
									File fileList = getFacadeFactory().getArquivoHelper().buscarArquivoDiretorioFixo(getComunicacaoInternaVO().getArquivoAnexo(), config);
									BufferedImage bufferedImage = ImageIO.read(fileList);
									setWidth(bufferedImage.getWidth()+80);
									setHeigth(bufferedImage.getHeight()+100);
								}
							}														
							return;
						}
						indexMarketing++;
					} else if (comunicacaoInterna.getTipoLeituraObrigatoria() || comunicacaoInterna.getTipoComunicadoInterno().equals("MU")) {
						if (indexLeituraObrigatoria == 1) {
							setComunicacaoInternaVO(comunicacaoInterna);							
							if (comunicacaoInterna.getDigitarMensagem()) {
								setAbrirRichModalMensagem(true);
								setLerComunicado(true);
								if (comunicacaoInterna.getTipoComunicadoInterno().equals("MU")) {
									setApresentarBotaoResponder(false);
								} else {
									setApresentarBotaoResponder(comunicacaoInterna.getTipoComunicadoInterno().equals("RE"));
								}
								return;
							}
						}
						indexLeituraObrigatoria++;
					}
				}
			}
			verificarQtdeMensagemCaixaEntrada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurmaCoordenadorLogado() {
		try {
			List<TurmaVO> objs = new ArrayList(0);
			if (!getValorConsultaTurma().equals("")) {
				objs = getFacadeFactory().getTurmaFacade().consultaPorCoordenadorParametizada(getUsuarioLogado().getPessoa().getCodigo(), getValorConsultaTurma(), getCampoConsultaTurma(), false, false, false, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			} else {
				throw new Exception("Pelo menos um valor deve ser Informado.");
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			Uteis.liberarListaMemoria(getListaConsultaTurma());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarTurmaProfessorLogado() throws Exception {
		String semestreatual =  Uteis.getSemestreAtual();
        String ano  =  Uteis.getData(new Date(), "yyyy");
        
		if(getPermitirEnviarComunicadoParaTurmaPeriodoAnterior() ) {
			if( Uteis.isAtributoPreenchido(getSemestre()) && Uteis.isAtributoPreenchido( getAno())) {
				semestreatual = getSemestre();
				ano = getAno();
			}		
		}
		List<TurmaVO> objs = new ArrayList(0);
		if (!getValorConsultaTurma().equals("")) {
			objs = getFacadeFactory().getTurmaFacade().consultaPorCoordenadorParametizada(getUsuarioLogado().getPessoa().getCodigo(), getValorConsultaTurma(), getCampoConsultaTurma(), false, false, false, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			objs = getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreEturmaEadNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getValorConsultaTurma(), semestreatual,ano, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
		} else {
			throw new Exception("Pelo menos um valor deve ser Informado.");
		}
		setListaConsultaTurma(objs);
		setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
	}
	

	public List<ComunicacaoInternaVO> getListaConsultaTopo() {
		if(listaConsultaTopo == null) {
			listaConsultaTopo =  new ArrayList<ComunicacaoInternaVO>(0);
		}
		return listaConsultaTopo;
	}

	public void setListaConsultaTopo(List<ComunicacaoInternaVO> listaConsultaTopo) {
		this.listaConsultaTopo = listaConsultaTopo;
	}
	
	
	public String getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional = Constantes.EMPTY;
		}
		return nivelEducacional;
	}
	
	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
	public Double getPercentualIntegralizacaoCurricularFinal() {
		return percentualIntegralizacaoCurricularFinal;
	}
	
	public void setPercentualIntegralizacaoCurricularFinal(Double percentualIntegralizacaoCurricularFinal) {
		this.percentualIntegralizacaoCurricularFinal = percentualIntegralizacaoCurricularFinal;
	}
	
	public Double getPercentualIntegralizacaoCurricularInicial() {
		return percentualIntegralizacaoCurricularInicial;
	}
	
	public void setPercentualIntegralizacaoCurricularInicial(Double percentualIntegralizacaoCurricularInicial) {
		this.percentualIntegralizacaoCurricularInicial = percentualIntegralizacaoCurricularInicial;
	}
	
	public String getIdentificadorSalaAulaBlackboard() {
		if(identificadorSalaAulaBlackboard == null) {
			identificadorSalaAulaBlackboard = Constantes.EMPTY;
		}
		return identificadorSalaAulaBlackboard;
	}
	
	public void setIdentificadorSalaAulaBlackboard(String identificadorSalaAulaBlackboard) {
		this.identificadorSalaAulaBlackboard = identificadorSalaAulaBlackboard;
	}
	
	public BimestreEnum getBimestre() {
		return bimestre;
	}
	
	public void setBimestre(BimestreEnum bimestre) {
		this.bimestre = bimestre;
	}
	
	public TipoDeficiencia getTipoDeficiencia() {
		return tipoDeficiencia;
	}
	
	public void setTipoDeficiencia(TipoDeficiencia tipoDeficiencia) {
		this.tipoDeficiencia = tipoDeficiencia;
	}
	
	public List<SelectItem> getListaSelectItemBimestre() {
		if(listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(null,""));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_01,BimestreEnum.BIMESTRE_01.getValorApresentar()));
			listaSelectItemBimestre.add(new SelectItem(BimestreEnum.BIMESTRE_02,BimestreEnum.BIMESTRE_02.getValorApresentar()));
		}
		return listaSelectItemBimestre;
	}
	
	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}
	
	public List<SelectItem> getListaSelectItemTipoDeficiencia() {
		return TipoDeficiencia.getListaSelectItemTipoDeficiencia();
	}
	
	public String getCampoConsultaDisciplina() {
		if(campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "abreviatura";
		}
		return campoConsultaDisciplina;
	}
	
	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}
	
	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboDisciplina.add(new SelectItem("abreviatura", "Abreviatura"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	public void setTipoConsultaComboDisciplina(List<SelectItem> tipoConsultaComboDisciplina) {
		this.tipoConsultaComboDisciplina = tipoConsultaComboDisciplina;
	}
	
	public String getValorConsultaDisciplina() {
		if(valorConsultaDisciplina == null) {
			valorConsultaDisciplina = Constantes.EMPTY;
		}
		return valorConsultaDisciplina;
	}
	
	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
	
	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}
	
	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	
	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplinaVO(obj);
	        getListaConsultaDisciplina().clear();
		    setValorConsultaDisciplina("");
			setCampoConsultaDisciplina("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparDisciplina() throws Exception {
		try {
			removerObjetoMemoria(getDisciplinaVO());
			Uteis.liberarListaMemoria(getListaConsultaDisciplina());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<>(0);
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				try {
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} catch (Exception e) {
					throw new ConsistirException("Valor do campo inválido.");
				}	
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if(getCampoConsultaDisciplina().equals("abreviatura")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if(programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	public String getCampoConsultaProgramacaoFormatura() {
		if(campoConsultaProgramacaoFormatura == null) {
			campoConsultaProgramacaoFormatura = Constantes.EMPTY;
		}
		return campoConsultaProgramacaoFormatura;
	}
	
	public void setCampoConsultaProgramacaoFormatura(String campoConsultaProgramacaoFormatura) {
		this.campoConsultaProgramacaoFormatura = campoConsultaProgramacaoFormatura;
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
	
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormatura() {
		if(listaConsultaProgramacaoFormatura == null) {
			listaConsultaProgramacaoFormatura = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormatura;
	}
	
	public void setListaConsultaProgramacaoFormatura(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura) {
		this.listaConsultaProgramacaoFormatura = listaConsultaProgramacaoFormatura;
	}
	
	public Boolean getMostrarSegundoCampoProgramacaoFormatura() {
		if(mostrarSegundoCampoProgramacaoFormatura == null) {
			mostrarSegundoCampoProgramacaoFormatura = Boolean.FALSE;
		}
		return mostrarSegundoCampoProgramacaoFormatura;
	}
	
	public void setMostrarSegundoCampoProgramacaoFormatura(Boolean mostrarSegundoCampoProgramacaoFormatura) {
		this.mostrarSegundoCampoProgramacaoFormatura = mostrarSegundoCampoProgramacaoFormatura;
	}
	
	public List<SelectItem> getTipoConsultaComboProgramacaoFormatura() {
		   if(tipoConsultaComboProgramacaoFormatura == null) {
		   tipoConsultaComboProgramacaoFormatura = new ArrayList<SelectItem>(0);
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("colacaoGrau", "Colação Grau"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("codigo", "Código"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeCurso", "Curso"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeTurno", "Turno"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("identificadorTurmaTurma", "Turma"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("matriculaMatricula", "Matricula"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("periodoCadastro", "Período Cadastro"));
		   tipoConsultaComboProgramacaoFormatura.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
		   }
	       return tipoConsultaComboProgramacaoFormatura;
	   }
	
	public Date getValorConsultaDataInicioProgramacaoFormatura() {
		return valorConsultaDataInicioProgramacaoFormatura;
	}
	
	public void setValorConsultaDataInicioProgramacaoFormatura(Date valorConsultaDataInicioProgramacaoFormatura) {
		this.valorConsultaDataInicioProgramacaoFormatura = valorConsultaDataInicioProgramacaoFormatura;
	}
	
	public Date getValorConsultaDataFinalProgramacaoFormatura() {
		return valorConsultaDataFinalProgramacaoFormatura;
	}
	
	public void setValorConsultaDataFinalProgramacaoFormatura(Date valorConsultaDataFinalProgramacaoFormatura) {
		this.valorConsultaDataFinalProgramacaoFormatura = valorConsultaDataFinalProgramacaoFormatura;
	}
	
	public void mostrarSegundoCampoProgramacaoFormatura() {
		if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau") || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
			setMostrarSegundoCampoProgramacaoFormatura(true);
		} else {
			setMostrarSegundoCampoProgramacaoFormatura(false);
		}
		setValorConsultaProgramacaoFormatura("");
		getListaConsultaProgramacaoFormatura().clear();
	}
	
	public String getFiltroAlunosPresentesColacaoGrau() {
		if(filtroAlunosPresentesColacaoGrau == null) {
			filtroAlunosPresentesColacaoGrau = "NI";
		}
	
		return filtroAlunosPresentesColacaoGrau;
	}

	public void setFiltroAlunosPresentesColacaoGrau(String filtroAlunosPresentesColacaoGrau) {
		this.filtroAlunosPresentesColacaoGrau = filtroAlunosPresentesColacaoGrau;
	}
	

	public boolean getApresentarResultadoConsultaProgramacaoFormatura() {
		if (this.getListaConsultaProgramacaoFormatura() == null || this.getListaConsultaProgramacaoFormatura().size() == 0) {
			return false;
		}
		return true;
	}
	
	public List<SelectItem> getComboFiltroAlunosPresentesColacaoGrau() {
		   if(comboFiltroAlunosPresentesColacaoGrau == null) {
		   comboFiltroAlunosPresentesColacaoGrau = new ArrayList<SelectItem>(0);
		   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("SI", "Presentes"));
		   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("NO", "Ausentes"));
		   comboFiltroAlunosPresentesColacaoGrau.add(new SelectItem("NI", "Ambos"));
		   }
	       return comboFiltroAlunosPresentesColacaoGrau;
	   }
	
	public void setComboFiltroAlunosPresentesColacaoGrau(List<SelectItem> comboFiltroAlunosPresentesColacaoGrau) {
		this.comboFiltroAlunosPresentesColacaoGrau = comboFiltroAlunosPresentesColacaoGrau;
	}
	
	public void limparProgramacaoFormatura() {
		removerObjetoMemoria(getProgramacaoFormaturaVO());
		Uteis.liberarListaMemoria(getListaConsultaProgramacaoFormatura());
		getStyleClassAno();
	}
	
	public String consultarProgramacaoFormatura() {
		try {
			super.consultar();
			List objs = new ArrayList(0);

			if (getCampoConsultaProgramacaoFormatura().equals("codigo")) {
				if (getValorConsultaProgramacaoFormatura().equals("")) {
					setValorConsultaProgramacaoFormatura("0");
				}
				if (getValorConsultaProgramacaoFormatura().trim() != null || !getValorConsultaProgramacaoFormatura().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaProgramacaoFormatura().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaProgramacaoFormatura());

				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			}
			if (getCampoConsultaProgramacaoFormatura().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaProgramacaoFormatura().equals("colacaoGrau")) {
				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorColacaoGrau(getValorConsultaProgramacaoFormatura(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaProgramacaoFormatura().equals("nomeCurso")) {

				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			}
			if (getCampoConsultaProgramacaoFormatura().equals("nomeTurno")) {

				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			}
			if (getCampoConsultaProgramacaoFormatura().equals("identificadorTurmaTurma")) {

				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

			}
			if (getCampoConsultaProgramacaoFormatura().equals("matriculaMatricula")) {

				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaProgramacaoFormatura().equals("nomeUsuario")) {
				objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau") || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
				objs = validarDataConsultaProgramacaoFormatura(objs);
			}
			setListaConsultaProgramacaoFormatura(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";

		} catch (Exception e) {
			setListaConsultaProgramacaoFormatura(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
	
	public List validarDataConsultaProgramacaoFormatura(List objs) throws Exception {
		if (getValorConsultaDataFinalProgramacaoFormatura() != null && getValorConsultaDataInicioProgramacaoFormatura() != null) {
			if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento")) {
				if (getFiltroAlunosPresentesColacaoGrau().equals("NI")) {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
			if (getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")) {
				if (getFiltroAlunosPresentesColacaoGrau().equals("NI")) {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
			if (getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
				if (getFiltroAlunosPresentesColacaoGrau().equals("NI")) {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0), Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
			}
		} else {
			throw new ConsistirException("Por favor digite uma data válida.");
		}

		return objs;
	}
	
	   public void selecionarProgramacaoFormatura() throws Exception {
			ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
	        setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setValorConsultaProgramacaoFormatura("");
			setCampoConsultaProgramacaoFormatura("");
			getListaConsultaProgramacaoFormatura().clear();
			getStyleClassAno();
	   }
	   
	   public String getStyleClassAno() {
		   if(Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO())){
			  return "form-control campos"; 
		   }
		return "form-control camposObrigatorios";
		   
	   }
	   
	   public List<SelectItem> getListaSelectItemDiaSemanaAula() {
			if(listaSelectItemDiaSemanaAula == null) {
				listaSelectItemDiaSemanaAula = UtilSelectItem.getListaSelectItemEnum(DiaSemana.values(), Obrigatorio.SIM);
			}
			return listaSelectItemDiaSemanaAula;
		}
	   
	   public DiaSemana getDiaSemana() {
		 return diaSemana;
	}
	   
	   public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}
	   
	   public ProgressBarVO getProgressBarVO() {
		   if(progressBarVO == null) {
			   progressBarVO = new ProgressBarVO();
		   }
		return progressBarVO;
	}
	   
	   public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	   
	   
	   public void realizarInicializacaoProgressBar(Boolean verificarTamanhoMensagem) {
			try {
				setOncompleteModal("");
				getProgressBarVO().resetar();
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				Integer totalEmailsEnviar = new Long(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().stream().count()).intValue();				
				if(totalEmailsEnviar.equals(0)) {
					throw new Exception("Favor adicione ao menos 1 destinatário!");
				}
				if((Uteis.getQuantidadeMemoriaStringEmMega(comunicacaoInternaVO.getMensagem())) > getConfiguracaoGeralSistemaVO().getTamanhoMaximoCorpoMensagem() && verificarTamanhoMensagem
						&& Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO().getTamanhoMaximoCorpoMensagem())) {
					setOncompleteModal("PF('panelConfirmacaoEnvio').show()");
					return;
				}
				getProgressBarVO().iniciar(0l, totalEmailsEnviar, "Realizando Envio de Email", true, this, "gravar");	
			} catch (ConsistirException ex) {			
				setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	   
	   public void scrollerListenerDestinatario() {
			try {
			
				consultarDestinatario();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		

		public void consultarDestinatario() throws Exception {
			try {
				List<ComunicadoInternoDestinatarioVO> destinatarios = getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarPorCodigoComunicacaoInterna(getComunicacaoInternaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset());
				getComunicacaoInternaVO().getListaConsultaDestinatario().getListaConsulta().clear();
	            getComunicacaoInternaVO().getListaConsultaDestinatario().setLimitePorPagina(10);
				getComunicacaoInternaVO().getListaConsultaDestinatario().setListaConsulta(destinatarios);
			} catch (Exception ex) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
			} 
		}
		
		public List<SelectItem> getListSelectLimitePagina() {
			if (listSelectLimitePagina == null) {
				listSelectLimitePagina = new ArrayList<>(0);
				listSelectLimitePagina.add(new SelectItem(10, "10"));
				listSelectLimitePagina.add(new SelectItem(20, "20"));
				listSelectLimitePagina.add(new SelectItem(30, "30"));
				listSelectLimitePagina.add(new SelectItem(40, "40"));
				listSelectLimitePagina.add(new SelectItem(50, "50"));
			}
			return listSelectLimitePagina;
		}
		
		public void setListSelectLimitePagina(List<SelectItem> listSelectLimitePagina) {
			this.listSelectLimitePagina = listSelectLimitePagina;
		}
		
		public Boolean getConsultarTodasMensagens() {
			if (consultarTodasMensagens == null) {
				consultarTodasMensagens = Boolean.TRUE;
			}
			return consultarTodasMensagens;
		}
		
		public void setConsultarTodasMensagens(Boolean consultarTodasMensagens) {
			this.consultarTodasMensagens = consultarTodasMensagens;
		}
		
		public void persistirLimitePaginaPadrao() {
			try {
				if (Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getLimitePorPagina())) {
					getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getControleConsultaOtimizado().getLimitePorPagina() + Constantes.EMPTY, ComunicacaoInterna.class.getSimpleName() + "_" + getUsuarioLogado().getCodigo(), "LimitePagina", getUsuarioLogado());
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void realizarUploadPlanilhaDestinatario (FileUploadEvent uploadEvent) {
			try {
				getComunicacaoInternaVO().getErrosProcessamentoPlanilha().clear();
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getComunicacaoInternaVO().getPlanilhaDestinatarioAnexo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
				getFacadeFactory().getComunicacaoInternaFacade().preencherDestinatariosPlanilhaExcel(uploadEvent, getComunicacaoInternaVO().getPlanilhaDestinatarioAnexo(), getConfiguracaoGeralSistemaVO(), getComunicacaoInternaVO(), getUsuario());
				executarCriacaoComucadoInternoDestinatarioPaisEResponsavelFinanceiro();
				if(Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getErrosProcessamentoPlanilha())) {
					setOncompleteModal("PF('panelErrosProcessamentoPlanilha').show()");
				}
			} catch (Exception e) {
				setOncompleteModal("");
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		
		public void downloadLayoutPlanilhaDestinatarios() throws Exception {
			try {
				File arquivo = new File(UteisJSF.getCaminhoWeb() + File.separator+ "resources" + File.separator + "layoutPadraoExcel" + File.separator + "layoutPadraoPlanilhaDestinatario" + File.separator +  "ArquivoExcelLayoutPadraoDestinatario.xlsx");
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("nomeArquivo", arquivo.getName());
				request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
				request.getSession().setAttribute("deletarArquivo", false);
				context().getExternalContext().dispatch("/DownloadSV");
				FacesContext.getCurrentInstance().responseComplete();
				
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	
}
