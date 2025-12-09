/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.processosel;

import java.awt.event.ActionEvent;
/**
 *
 * @author Administrador
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.LoginControle;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;

import negocio.comuns.processosel.InscricaoVO;

import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.RespostaPerguntaVO;

import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;

import relatorio.controle.arquitetura.SuperControleRelatorio;


@Controller("VisaoCandidatoControle")
@Scope("session")
@Lazy
public class VisaoCandidatoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	protected CursoVO cursoVO;
	protected PessoaVO pessoaVO;
	protected UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	protected UnidadeEnsinoCursoVO cursoOpcao2;
	protected UnidadeEnsinoCursoVO cursoOpcao3;
	protected ProcSeletivoVO processoSeletivoVO;
	private FiliacaoVO filiacaoVO;
	private InscricaoVO inscricaoVO;
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private GradeCurricularVO gradeCurricularVO;
	private PeriodoLetivoVO periodoLetivoVO;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private ProcessoMatriculaVO processoMatriculaVO;
//	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	protected Boolean menuInscricao;
	protected Boolean menuResultado;
	protected Boolean menuCurso;
	protected Boolean menuCalendario;
	protected Boolean menuMatricula;
	protected Boolean apresentarUnidadeEnsino;
	protected Boolean apresentarCurso;
	protected Boolean apresentarProcessoSeletivo;
	protected Boolean apresentarInscricao;	
	protected Boolean ocultarMedia;
	protected Boolean ocultarClassificacao;
	protected Boolean ocultarChamadaCandidato;
	protected boolean apresentarDadosDaInscricao;
	private Boolean apresentarPanelCadastroCandidado;
	private Boolean apresentarPanelInscricaoCandidado;
	private Boolean apresentarPanelCursoCandidado;
	private Boolean apresentarPanelCalendarioCandidado;
	private Boolean apresentarPanelMatriculaCandidado;
	private Boolean apresentarMensagemCpfRichModal;
	private Boolean apresentarLinguaEstrangeira;
	private Boolean apresentarFormaAcessoProcSeletivo;
	private Boolean cpfValido;
	private String MensagemCpfRichModal;
	private Boolean opcao1;
	private Boolean opcao2;
	private Boolean opcao3;
	private Boolean preRequisito;
	protected Integer opcao;
	private Integer tmpCodigoProcessoSeletivo = 0;
	protected List listaOpcao;
	private List listaSelectItemDatasProva;
	protected List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemCurso;
	protected List listaSelectItemProcessoSeletivo;
	protected List listaSelectItemCidade;
	protected List listaSelectItemNaturalidade;
	protected List listaSelectItemNacionalidade;
	protected List listaSelectItemAreaConhecimento;
	protected List listaSelectItemOpcaolinguaEstrangeira;
	protected List listaSelectItemCursoOpcao;
	protected List listaDisciplinasAnteriores;
	protected List listaProcessoMatricula;
	private String abaSelecionada;
	private String valorConsultaInscricao;
	private String valorConsultaCPF;
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	protected List listaConsultaCidade;
	private String campoConsultaNaturalidade;
	private String valorConsultaNaturalidade;
	protected List listaConsultaNaturalidade;
	private String nomeCursoAprovado;
	private String dataProvaSelecionada;
	private Boolean apresentarDadosProcessoSeletivo;
	private String etapa;
	private Boolean apresentarEtapa1NovaVisao;
	private Boolean apresentarEtapa2NovaVisao;
	private Boolean apresentarEtapa3NovaVisao;
	private Boolean apresentarEtapa4NovaVisao;
	private Boolean apresentarBotaoEtapa2;
	private Boolean apresentarBotaoEtapa3;
	private Boolean apresentarBotaoEtapa4;
	private Boolean apresentarBotaoConcluir3;
	private Boolean apresentarBotaoConcluir4;
	private Boolean apresentarBotaoInscricao;
	private Boolean apresentarBotaoVoltarEtapa1;
	private Boolean apresentarBotaoVoltarEtapa2;
	private Boolean apresentarBotaoVoltarEtapa3;
	private Boolean apresentarBotaoAlterarCandidato;
	private Boolean apresentarBotaoBoleto;
//	private ComprovanteInscricaoRelControle comprovanteInscricaoRelControle;

	private String campoConsultaFiliacaoCidade;
	private String valorConsultaFiliacaoCidade;
	private List listaConsultaFiliacaoCidade;
	private Boolean concluiuEnsinoMedio;	
	private ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO;
    private ArquivoVO arquivoVO;
    private Boolean arquivoAnexado;
	protected List listaTipoSelecao;
    private String labelArquivosAnexos;
    private Boolean mostrarImagem;
	private String erroUpload;
	private String msgErroUpload;
	private TipoCartaoOperadoraCartaoEnum tipoCartao;
	private Integer itemProcessoSeletivoMaiorDataProva;

	public VisaoCandidatoControle() throws Exception {
		// //obterUsuarioLogado();
		inicializarDadosMenu();
		inicializarListaOpcao();
		inicializarListasSelectItemTodosComboBox();
		inicializarVariaveis();
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setMensagemDetalhada("");
		setApresentarDadosProcessoSeletivo(false);
		realizarInvalidacaoSessao();
		LoginControle loginControle = (LoginControle) getControlador("LoginControle");
		if (loginControle != null) {
			loginControle.abrirHomeCandidato();
			loginControle.init();
		}
		setAbaSelecionada("aba1");
	}
	
	@PostConstruct
	public void init() {
	}

	public String novo() {
		removerObjetoMemoria(this);
		setPessoaVO(new PessoaVO());
		inicializarListasSelectItemTodosComboBox();
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		setValidarCpf(this.validarCadastroPorCpf());
		setListaSelectItemDatasProva(new ArrayList(0));
		setDataProvaSelecionada("");
		setArquivoVO(new ArquivoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	public void inicializarVariaveis() {
		setApresentarPanelInscricaoCandidado(false);
	}

	public String inicializarDadosHome() {
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCurso(false);
		setMenuCalendario(false);
		setApresentarMensagemCpfRichModal(false);
		setApresentarLinguaEstrangeira(false);
		setApresentarFormaAcessoProcSeletivo(false);
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeCandidato.xhtml");
	}

	public void inicializarDadosMenu() {
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCurso(false);
		setMenuCalendario(false);
		setApresentarMensagemCpfRichModal(false);
		setApresentarLinguaEstrangeira(false);
		setApresentarFormaAcessoProcSeletivo(false);
	}

	public void inicializarMenuInscricao() {
		setMensagemID("msg_entre_dados");
		setMensagemDetalhada("");
		setApresentarDadosProcessoSeletivo(false);
		setMenuInscricao(true);
		setMenuResultado(false);
		setDataProvaSelecionada("");
		setMenuCurso(false);
		setMenuCalendario(false);
		setMenuMatricula(false);
		setApresentarPanelCadastroCandidado(false);
		setApresentarPanelInscricaoCandidado(false);
		setApresentarCurso(false);
		setApresentarUnidadeEnsino(false);
		setApresentarLinguaEstrangeira(false);
		setApresentarFormaAcessoProcSeletivo(false);
		setApresentarProcessoSeletivo(false);
		setApresentarInscricao(false);
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setCursoOpcao2(new UnidadeEnsinoCursoVO());
		setCursoOpcao3(new UnidadeEnsinoCursoVO());
		setCursoVO(new CursoVO());
		setProcessoSeletivoVO(new ProcSeletivoVO());
		setOpcao(0);
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setListaSelectItemProcessoSeletivo(new ArrayList(0));
		setMensagemDetalhada("");
		setApresentarMensagemCpfRichModal(false);
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		inicializarListasSelectItemTodosComboBox();
		setOpcao1(false);
		setOpcao2(false);
		setOpcao3(false);
		setListaSelectItemCursoOpcao(new ArrayList(0));
		inicializarListaOpcao();
		setApresentarBotaoConcluir3(false);
		setApresentarBotaoConcluir4(false);
		setApresentarBotaoEtapa2(false);
		setApresentarBotaoEtapa3(false);
		setApresentarBotaoEtapa4(false);
		setPermitirRecebimentoCartaoCreditoOnline(false);
		setPagamentoRealizadoComSucesso(false);
//		negociacaoRecebimentoVO = null;
		setApresentarBotaoBoleto(false);	
		setListaTipoSelecao(new ArrayList(0));

	}

	public String inicializarInscricaoCandidato() {
		setMensagemID("msg_entre_dados");
		setMensagemDetalhada("");
		setApresentarDadosProcessoSeletivo(false);
		setMenuInscricao(true);
		setMenuResultado(false);
		setDataProvaSelecionada("");
		setMenuCurso(false);
		setMenuCalendario(false);
		setMenuMatricula(false);
		setApresentarPanelCadastroCandidado(false);
		setApresentarPanelInscricaoCandidado(false);
		setApresentarCurso(false);
		setApresentarUnidadeEnsino(false);
		setApresentarLinguaEstrangeira(false);
		setApresentarFormaAcessoProcSeletivo(false);
		setApresentarProcessoSeletivo(false);
		setApresentarInscricao(false);
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setCursoOpcao2(new UnidadeEnsinoCursoVO());
		setCursoOpcao3(new UnidadeEnsinoCursoVO());
		setCursoVO(new CursoVO());
		setProcessoSeletivoVO(new ProcSeletivoVO());
		setOpcao(0);
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setListaSelectItemProcessoSeletivo(new ArrayList(0));
		setMensagemDetalhada("");
		setApresentarMensagemCpfRichModal(false);
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		inicializarListasSelectItemTodosComboBox();
		setOpcao1(false);
		setOpcao2(false);
		setOpcao3(false);
		setListaSelectItemCursoOpcao(new ArrayList(0));
		setApresentarEtapa1NovaVisao(Boolean.TRUE);
		setApresentarEtapa2NovaVisao(Boolean.FALSE);
		setApresentarEtapa3NovaVisao(Boolean.FALSE);
		setApresentarEtapa4NovaVisao(Boolean.FALSE);

		setApresentarBotaoVoltarEtapa1(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa2(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa3(Boolean.FALSE);
		setApresentarBotaoEtapa2(Boolean.FALSE);
		setApresentarBotaoEtapa3(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 1");
		inicializarListaOpcao();
		setApresentarBotaoConcluir3(false);
		setApresentarBotaoConcluir4(false);
		setApresentarBotaoEtapa2(false);
		setApresentarBotaoEtapa3(false);
		setApresentarBotaoEtapa4(false);
		setPermitirRecebimentoCartaoCreditoOnline(false);
		setPagamentoRealizadoComSucesso(false);
//		negociacaoRecebimentoVO = null;
		setApresentarBotaoBoleto(false);
		setArquivoVO(new ArquivoVO());
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
	return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCandidato.xhtml");

	}

	public List getListaSelectItemTipoInstFormacaoAcademica() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoInstFormacaoAcademicas = (Hashtable) Dominios.getTipoInstFormacaoAcademica();
		Enumeration keys = tipoInstFormacaoAcademicas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoInstFormacaoAcademicas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public String inicializarMenuComprovanteInscricao() {
		setMensagemID("msg_entre_dados");
		setMensagemDetalhada("");
		setApresentarDadosProcessoSeletivo(false);
		setMenuInscricao(true);
		setMenuResultado(false);
		setDataProvaSelecionada("");
		setMenuCurso(false);
		setMenuCalendario(false);
		setMenuMatricula(false);
		setApresentarPanelCadastroCandidado(false);
		setApresentarPanelInscricaoCandidado(false);
		setApresentarCurso(false);
		setApresentarUnidadeEnsino(false);
		setApresentarLinguaEstrangeira(false);
		setApresentarFormaAcessoProcSeletivo(false);
		setApresentarProcessoSeletivo(false);
		setApresentarInscricao(false);
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setCursoOpcao2(new UnidadeEnsinoCursoVO());
		setCursoOpcao3(new UnidadeEnsinoCursoVO());
		setCursoVO(new CursoVO());
		setProcessoSeletivoVO(new ProcSeletivoVO());
		setOpcao(0);
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setListaSelectItemProcessoSeletivo(new ArrayList(0));
		setMensagemDetalhada("");
		setApresentarMensagemCpfRichModal(false);
		setFiliacaoVO(new FiliacaoVO());
		setFormacaoAcademicaVO(new FormacaoAcademicaVO());
		inicializarListasSelectItemTodosComboBox();
		setOpcao1(false);
		setOpcao2(false);
		setOpcao3(false);
		setListaSelectItemCursoOpcao(new ArrayList(0));
		setApresentarEtapa1NovaVisao(Boolean.TRUE);
		setApresentarEtapa2NovaVisao(Boolean.FALSE);
		setApresentarEtapa3NovaVisao(Boolean.FALSE);
		setApresentarEtapa4NovaVisao(Boolean.FALSE);

		setApresentarBotaoVoltarEtapa1(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa2(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa3(Boolean.FALSE);
		setApresentarBotaoEtapa2(Boolean.FALSE);
		setApresentarBotaoEtapa3(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 1");
		inicializarListaOpcao();
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("comprovanteInscricao.xhtml");
	}

	public void inicializarMenuCurso() {
		setMensagemID("msg_entre_dados");
		setMenuCurso(true);
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCalendario(false);
		setMenuMatricula(false);
		setApresentarCurso(true);
		setApresentarPanelCursoCandidado(false);
		setPreRequisito(false);
		montarListaSelectItemCurso();
		setCursoVO(new CursoVO());
		setGradeCurricularVO(new GradeCurricularVO());
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setPeriodoLetivoVO(new PeriodoLetivoVO());
		setListaDisciplinasAnteriores(new ArrayList(0));
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
	}

	public String inicializarMenuCursoCandidato() {
		setMensagemID("msg_entre_dados");
		setMenuCurso(true);
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCalendario(false);
		setMenuMatricula(false);
		setApresentarCurso(true);
		setApresentarPanelCursoCandidado(false);
		setPreRequisito(false);
		montarListaSelectItemCurso();
		setCursoVO(new CursoVO());
		setGradeCurricularVO(new GradeCurricularVO());
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setPeriodoLetivoVO(new PeriodoLetivoVO());
		setListaDisciplinasAnteriores(new ArrayList(0));
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("cursoCandidato.xhtml");
	}

	/**
	 * Metodo Responsavel por inicializar Todos atributos Necessario para que o
	 * Menu Calendario Funcione
	 * 
	 * @throws java.lang.Exception
	 *             caso haja erro na consultar de ProcessoMatricula.
	 */
	public void inicializarMenuCalendario() throws Exception {
		try {
			setMensagemID("msg_entre_dados");
			setMenuInscricao(false);
			setMenuResultado(false);
			setMenuCurso(false);
			setMenuCalendario(true);
			setMenuMatricula(false);
			setApresentarPanelCalendarioCandidado(false);// nao foi utilizada

			setProcessoMatriculaVO(new ProcessoMatriculaVO());
			setListaProcessoMatricula(new ArrayList(0));
			setListaProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarSomentePelaDataFimProcessoMatricula(new Date(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				context().getExternalContext().getSessionMap().remove("usuarioLogado");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarMenuCalendarioCandidato() throws Exception {
		try {
			setMensagemID("msg_entre_dados");
			setMenuInscricao(false);
			setMenuResultado(false);
			setMenuCurso(false);
			setMenuCalendario(true);
			setMenuMatricula(false);
			setApresentarPanelCalendarioCandidado(false);// nao foi utilizada

			setProcessoMatriculaVO(new ProcessoMatriculaVO());
			setListaProcessoMatricula(new ArrayList(0));
			setListaProcessoMatricula(getFacadeFactory().getProcessoMatriculaFacade().consultarSomentePelaDataFimProcessoMatricula(new Date(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				context().getExternalContext().getSessionMap().remove("usuarioLogado");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioCandidato.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("calendarioCandidato.xhtml");
		}
	}

	/**
	 * Metodo Responsavel por inicializar Todos atributos Necessario para que o
	 * Menu Matricua Funcione
	 */
	public void inicializarMenuBoleto() throws Exception {
		setMensagemID("msg_entre_dados");
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCurso(false);
		setMenuCalendario(false);
		setMenuMatricula(true);
		setApresentarPanelInscricaoCandidado(false);
		setApresentarDadosDaInscricao(false);
		setOpcao1(false);
		setOpcao2(false);
		setOpcao3(false);
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setValorConsultaInscricao("");
		setValorConsultaCPF("");
	}

	public String inicializarMenuBoletoCandidato() throws Exception {
		setMensagemID("msg_entre_dados");
		setMenuInscricao(false);
		setMenuResultado(false);
		setMenuCurso(false);
		setMenuCalendario(false);
		setMenuMatricula(true);
		setApresentarPanelInscricaoCandidado(false);
		setApresentarDadosDaInscricao(false);
		setOpcao1(false);
		setOpcao2(false);
		setOpcao3(false);
		setPessoaVO(new PessoaVO());
		setInscricaoVO(new InscricaoVO());
		setValorConsultaInscricao("");
		setValorConsultaCPF("");		
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("boletoCandidato.xhtml");
	}

	// public void consultarResultadoProcessoSeletivoParaFazerMatricula() {
	// try {
	// ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO =
	// resultadoProcessoSeletivoFacade.consultarPorCPFCandidato_ResultadoUnico(getPessoaVO().getCPF(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// setInscricaoVO(resultadoProcessoSeletivoVO.getInscricao());
	// setPessoaVO(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getCandidato().getCodigo(),
	// Uteis.NIVELMONTARDADOS_TODOS));
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	//
	// }
	/**
	 * Metodo Responsavel por Montar os dados Do
	 * ProcessoMatriculaCalendarioCursoVO.
	 */
	public void montarCalendarioCurso() {
		ProcessoMatriculaVO obj = (ProcessoMatriculaVO) context().getExternalContext().getRequestMap().get("processoMatriculaItens");
		setProcessoMatriculaVO(obj);
	}

	public void inicializarListaOpcao() {
		List objs = new ArrayList(0);
		setOpcao(0);
		setListaOpcao(new ArrayList(0));
		objs.add(new SelectItem(new Integer(1), "Escolha por Unidade de Ensino:"));
		objs.add(new SelectItem(new Integer(2), "Escolha por Curso:"));
		setListaOpcao(objs);
	}

	public void validarListaOpcao() {
		if (getOpcao() == 1) {
			setApresentarUnidadeEnsino(true);
			setApresentarLinguaEstrangeira(false);
			setApresentarFormaAcessoProcSeletivo(false);
			setApresentarCurso(false);
			setApresentarProcessoSeletivo(false);
			setApresentarInscricao(false);
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			montarListaSelectItemUnidadeEnsino();
			setListaSelectItemCurso(new ArrayList(0));
			setProcessoSeletivoVO(new ProcSeletivoVO());
		} else {
			setApresentarCurso(true);
			setApresentarUnidadeEnsino(false);
			setApresentarLinguaEstrangeira(false);
			setApresentarFormaAcessoProcSeletivo(false);
			setApresentarInscricao(false);
			setApresentarProcessoSeletivo(false);
			setCursoVO(new CursoVO());
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			setProcessoSeletivoVO(new ProcSeletivoVO());
			montarListaSelectItemCurso();
			setListaSelectItemUnidadeEnsino(new ArrayList(0));
		}
	}

	public void inicializarMenuResultado() {
		setMensagemID("msg_entre_dados");
		setMenuCalendario(false);
		setMenuCurso(false);
		setMenuMatricula(false);
		setMenuResultado(true);
		setMenuInscricao(false);
//		setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
	}

	public String inicializarMenuResultadoCandidato() {
		setMensagemID("msg_entre_dados");
		setMenuCalendario(false);
		setMenuCurso(false);
		setMenuMatricula(false);
		setMenuResultado(true);
		setMenuInscricao(false);
//		setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())) {
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoCandidato.xhtml");
	}

	public void montarDadosCursoGradeAtiva() throws Exception {
		if (getCursoVO().getCodigo() != 0) {
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(getCursoVO().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS,  getUsuarioLogado() ));			
			setApresentarPanelCursoCandidado(true);
			setPreRequisito(false);
			setGradeDisciplinaVO(new GradeDisciplinaVO());
			setPeriodoLetivoVO(new PeriodoLetivoVO());
		}else{
			setApresentarPanelCursoCandidado(false);
		}
	}

	public void montarDadosDisciplinaPeriodo() throws Exception {
		PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
		setPeriodoLetivoVO(obj);
	}

	public void configurarPreRequisito() throws Exception {
		PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
		setPeriodoLetivoVO(obj);
		setGradeDisciplinaVO(new GradeDisciplinaVO());
		setPreRequisito(false);
	}

	public void montarListaDisciplinasAnteriores() {
		setPreRequisito(true);
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaAnteriorItens");
		setGradeDisciplinaVO(obj);
		setListaDisciplinasAnteriores(obj.getDisciplinaRequisitoVOs());
	}

	public String getExistePreRequisito() {
		if (getGradeDisciplinaVO().getCodigo() != 0 && getListaDisciplinasAnteriores().size() == 0) {
			return "Não existe PreRequisito para essa Disciplina " + getGradeDisciplinaVO().getDisciplina().getNome() + ".";
		}
		return "";
	}

	public void inicializarApresentaInscricao() {
		try {
			setProcessoSeletivoVO(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			
		} catch (Exception e) {
			setApresentarInscricao(false);
			setApresentarBotaoEtapa2(Boolean.FALSE);
		}
	}

	public void inicializarBotaoInscricao() {
		try {
			if (getProcessoSeletivoVO().getCodigo() != 0) {
				setApresentarFormaAcessoProcSeletivo(true);
				mostrarLinguaEstrangeira();
				inicializarApresentaInscricao();
			} else {
				setApresentarFormaAcessoProcSeletivo(false);
				setApresentarInscricao(false);
			}
		} catch (Exception e) {
		}
	}

	public boolean getApresentarDataProva() {
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("PS");
	}
	
	public boolean getApresentarUploadEnem() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("EN");
	}
	public boolean getApresentarUploadPortador() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("PD");
	}
	
	public boolean getApresentarUploadTransferencia() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("TR");
	}
	
	public boolean getArquivoAnexadoCorreto() {		
		return Uteis.isAtributoPreenchido(getArquivoAnexado());
	}
	
	public boolean getApresentarFormaIngresso() {
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo());
	}

	public String getApresentarEtapa2() throws ConsistirException {
		// if (pessoaVO.getCPF().length() != 14) {
		// return
		// "Richfaces.showModalPanel('panelModalInscricao');Richfaces.hideModalPanel('panelConsultaCPF')";
		// } else {
		if (getCpfValido()) {
			return "RichFaces.$('panelCadastroCandidato').show(); RichFaces.$('panelModalInscricao').hide(); RichFaces.$('panelConsultaCPF').hide()";
		} else {
			return "RichFaces.$('panelCadastroCandidato').show(); RichFaces.$('panelModalInscricao').hide(); RichFaces.$('panelConsultaCPF').hide()";
		}
		// }
	}

	public String getApresentarEtapa3() {
		if (getApresentarPanelInscricaoCandidado()) {
			setApresentarPanelInscricaoCandidado(Boolean.FALSE);
			setApresentarLinguaEstrangeira(true);
			setAbaSelecionada("aba1");
			return "RichFaces.$('panelInscricaoCandidato').show(); RichFaces.$('panelCadastroCandidato').hide()";
		} else {
			return "RichFaces.$('panelInscricaoCandidato').hide(); RichFaces.$('panelCadastroCandidato').show()";
		}
	}

	public String getConcluirEtapa3() {
		if (getInscricaoVO().getQuestionarioVO().getCodigo() == 0) {
			return "RichFaces.$('panelInscricaoConcluida').show(); RichFaces.$('panelInscricaoCandidato').hide()";
		} else {
			return "RichFaces.$('panelInscricaoCandidatoQuest').show(); RichFaces.$('panelInscricaoCandidato').hide()";
		}
	}

	public String getConcluirEtapa4() {
		if (getMensagemDetalhada().equalsIgnoreCase("")) {
			return "RichFaces.$('panelInscricaoConcluida').show(); RichFaces.$('panelInscricaoCandidatoQuest').hide()";
		} else {
			return "RichFaces.$('panelInscricaoConcluida').hide(); RichFaces.$('panelInscricaoCandidatoQuest').show()";
		}
	}

	public void gravarCandidato() {
		try {
			getFacadeFactory().getPessoaFacade().setIdEntidade("Candidato");
			getPessoaVO().setCandidato(Boolean.TRUE);
			getPessoaVO().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "CANDIDATO");
			getInscricaoVO().setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("1")) {
				setOpcao1(true);
				setOpcao2(false);
				setOpcao3(false);
			}
			if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("2")) {
				setOpcao1(true);
				setOpcao2(true);
				setOpcao3(false);
				if (getCursoOpcao2().getCodigo() > 0) {
					getInscricaoVO().setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
			}
			if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("3")) {
				setOpcao1(true);
				setOpcao2(true);
				setOpcao3(true);
				if (getCursoOpcao2().getCodigo() > 0) {
					getInscricaoVO().setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				if (getCursoOpcao3().getCodigo() > 0) {
					getInscricaoVO().setCursoOpcao3(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
			}
			if (getPessoaVO().getCodigo() == 0) {
				if (getConcluiuEnsinoMedio()) {
					getFormacaoAcademicaVO().setEscolaridade(NivelFormacaoAcademica.MEDIO.getValor());
					getFormacaoAcademicaVO().setSituacao("CO");
					getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
				}
				getFacadeFactory().getPessoaFacade().incluir(getPessoaVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false, true, true, false, false);
			} else {
				if (getConcluiuEnsinoMedio()) {
					if (getFormacaoAcademicaVO().isNovoObj()) {
						getFormacaoAcademicaVO().setEscolaridade(NivelFormacaoAcademica.MEDIO.getValor());
						getFormacaoAcademicaVO().setSituacao("CO");
					}
					getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
				} else if (!getFormacaoAcademicaVO().isNovoObj()) {
					getPessoaVO().excluirObjFormacaoAcademicaVOs(getFormacaoAcademicaVO().getCurso());
				}
				getFacadeFactory().getInscricaoFacade().validarDadosUnicidadeCandidatoCurso(getInscricaoVO().getCursoOpcao1().getCurso().getCodigo(), getInscricaoVO().getCursoOpcao2().getCurso().getCodigo(), getInscricaoVO().getCursoOpcao3().getCurso().getCodigo(), getPessoaVO().getCodigo(),getInscricaoVO().getProcSeletivo().getPermitirAlunosMatriculadosInscreverMesmoCurso());
				getFacadeFactory().getPessoaFacade().alterar(getPessoaVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), false, false, true, false, false);
			}
			/* Atributos Para controlar tela da visao do candidato */
			setApresentarPanelInscricaoCandidado(true);
			setApresentarPanelCadastroCandidado(true);
			getInscricaoVO().setCandidato(getPessoaVO());
			getInscricaoVO().setData(new Date());
			getInscricaoVO().setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getInscricaoVO().getQuestionarioVO().setCodigo(getInscricaoVO().getProcSeletivo().getQuestionario().getCodigo());
			tmpCodigoProcessoSeletivo = getInscricaoVO().getProcSeletivo().getCodigo();			
			getInscricaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getOpcao().equals(1)) {
				getInscricaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (getOpcao().equals(2)) {
				getInscricaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getUnidadeEnsino().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
//			montarListaSelectItemCursoOpcao();
			montarListaSelectItemOpcaoLinguaEstrangeira();

			inicializarDadosEtapa3();
			setMensagemID("msg_verificarDados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarResponderQuestionario() {
		try {
			getInscricaoVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getInscricaoVO().getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (getInscricaoVO().getCodigo() > 0) {
				getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorInscricao(getInscricaoVO().getCodigo(), getInscricaoVO().getQuestionarioVO());
			}
			inicializarDadosEtapa4();
			setMensagemID("msg_respostasQuestoes");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void voltar() {
		setApresentarPanelInscricaoCandidado(false);
		setApresentarPanelCadastroCandidado(true);
	}

	public void emitirBoletoPagamentoInscricaoEvent(ActionEvent evt) {
		emitirBoletoPagamentoInscricao();
	}

	public void emitirComprovanteInscricao() {
		try {
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void emitirBoletoPagamentoInscricao() {
		try {
			if (getInscricaoVO().getSituacao().equals("CO")) {
				setMensagemID("msg_requerimento_requerimentoJaQuitadaFinanceiramente");
				// throw new
				// ConsistirException("msg_requerimento_requerimentoJaQuitadaFinanceiramente");
			} else {
				if (getInscricaoVO().getCodigo() == 0) {
					gravarInscricaoCandidato();
				}
				// imprimirBoleto();
				setMensagemID("msg_inscricao_emitirBoletoPagamento");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}

	}

	public void validarDadosListaResposta() throws Exception {
		RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("resposta");
		getInscricaoVO().getQuestionarioVO().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
	}

	public void gravarInscricaoCandidato() throws Exception {
		try {
					
			arquivoAnexado = false;		
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
		
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public void imprimirBoleto() throws Exception {
	// BoletoBancarioRelControle controle = new
	// BoletoBancarioRelControle(getInscricaoVO().getContaReceber());
	// controle.imprimirHTML();
	// }
	public void consultarProcessoSeletivo() {
		try {
			List<ProcSeletivoVO> objs = new ArrayList<ProcSeletivoVO>(0);
			setListaSelectItemProcessoSeletivo(new ArrayList<SelectItem>(0));
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				// setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(),
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS));
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsinoAptoInscricao(getUnidadeEnsinoVO().getCodigo(), true, false, 0,0,"",Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

				if (!getListaSelectItemCursoOpcao().isEmpty()) {
					montarListaSelectItemCursoPorUnidadeEnsino();
				}
				getListaSelectItemProcessoSeletivo().add(new SelectItem(0, ""));
				for (ProcSeletivoVO obj : objs) {
					getListaSelectItemProcessoSeletivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				}
				if (getListaSelectItemProcessoSeletivo().size() == 1) {
					throw new Exception(UteisJSF.internacionalizar("msg_processoSeletivo_nenhumProcessoVinculadoUnidadeEnsino"));
				} else {
					setMensagemDetalhada("");
					setApresentarProcessoSeletivo(true);
				}
			} else {
				getProcessoSeletivoVO().setCodigo(0);
				inicializarBotaoInscricao();
				setMensagemDetalhada("");
				setApresentarProcessoSeletivo(false);
				getListaSelectItemDatasProva().clear();
				getInscricaoVO().getProcSeletivo().setCodigo(0);
			}
//			setPermitirRecebimentoCartaoCreditoOnline(getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo()).getCodigo(), getNegociacaoRecebimentoVO().getValorTotal(), "permitirecebimentocartaoonline"));
			setMensagemDetalhada("");
			setMensagemID("");
			setMensagem("");
			inicializarApresentaInscricao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void visaoCadastroCandidato() throws Exception {
		try {
			// if (getValidarCpf()) {
			if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
				setCpfValido(false);
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
			}
			// }
			setCpfValido(true);
			// String campoConsulta = pessoaVO.getCPF();
			// if (campoConsulta.length() != 14) {
			// setApresentarMensagemCpfRichModal(false);
			// throw new Exception("Digite o CPF.");
			// }
			setMensagemCpfRichModal("");
			if (getOpcao() == 1) {
				setApresentarUnidadeEnsino(true);
				setApresentarLinguaEstrangeira(false);
				setApresentarFormaAcessoProcSeletivo(false);
				setApresentarCurso(false);
				setApresentarProcessoSeletivo(true);
				setApresentarInscricao(true);
			} else {
				setApresentarCurso(true);
				setApresentarUnidadeEnsino(false);
				setApresentarLinguaEstrangeira(false);
				setApresentarFormaAcessoProcSeletivo(false);
				setApresentarInscricao(true);
				setApresentarProcessoSeletivo(true);
			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemCpfRichModal(e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosProcessoSeletivo() {
		setApresentarDadosProcessoSeletivo(Boolean.TRUE);
		setApresentarEtapa1NovaVisao(Boolean.TRUE);
		setApresentarEtapa2NovaVisao(Boolean.FALSE);
		setApresentarEtapa3NovaVisao(Boolean.FALSE);
		setApresentarEtapa4NovaVisao(Boolean.FALSE);

		setApresentarBotaoVoltarEtapa1(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa2(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa3(Boolean.FALSE);
		setApresentarBotaoEtapa2(Boolean.TRUE);
		setApresentarBotaoEtapa3(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 1");

	}

	public void inicializarDadosEtapa2() {
		boolean erro = false;
		try {
			getFacadeFactory().getInscricaoFacade().validarArquivoInscricao(getInscricaoVO(), getArquivoVO());
			setApresentarEtapa1NovaVisao(Boolean.FALSE);
			setApresentarEtapa2NovaVisao(Boolean.TRUE);
			setApresentarEtapa3NovaVisao(Boolean.FALSE);
			setApresentarEtapa4NovaVisao(Boolean.FALSE);
			setApresentarBotaoEtapa2(Boolean.FALSE);
			setApresentarBotaoEtapa3(Boolean.TRUE);
			setApresentarBotaoEtapa4(Boolean.FALSE);
			setApresentarBotaoConcluir3(Boolean.FALSE);
			setApresentarBotaoConcluir4(Boolean.FALSE);
			setApresentarBotaoVoltarEtapa2(Boolean.FALSE);
			setApresentarBotaoVoltarEtapa1(Boolean.TRUE);
			setApresentarBotaoVoltarEtapa3(Boolean.FALSE);
			setApresentarBotaoAlterarCandidato(Boolean.FALSE);
			setApresentarDadosProcessoSeletivo(Boolean.FALSE);
			setAbaSelecionada("aba1");
			setConfiguracaoCandidatoProcessoSeletivoVO(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getUnidadeEnsinoVO().getCodigo()).getConfiguracaoCandidatoProcessoSeletivoVO());
			setMensagem("");
            setMensagemID("");
		} catch (ConsistirException ex) {
			erro = true;
			inicializarDadosProcessoSeletivo();
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setEtapa(erro ? getEtapa() : "Inscrição - Etapa 2");
	}

	public void inicializarDadosEtapa3() {
		setApresentarEtapa1NovaVisao(Boolean.FALSE);
		setApresentarEtapa2NovaVisao(Boolean.FALSE);
		setApresentarEtapa3NovaVisao(Boolean.TRUE);
		setApresentarEtapa4NovaVisao(Boolean.FALSE);

		setApresentarBotaoEtapa3(Boolean.FALSE);
		setApresentarBotaoEtapa4(Boolean.TRUE);
		setApresentarBotaoConcluir3(Boolean.TRUE);
		setApresentarBotaoConcluir4(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa1(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa2(Boolean.TRUE);
		setApresentarBotaoVoltarEtapa3(Boolean.FALSE);
		setApresentarBotaoBoleto(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 3");
	}

	public void inicializarDadosEtapa4() {
		setApresentarEtapa1NovaVisao(Boolean.FALSE);
		setApresentarEtapa2NovaVisao(Boolean.FALSE);
		setApresentarEtapa3NovaVisao(Boolean.FALSE);
		setApresentarEtapa4NovaVisao(Boolean.TRUE);

		setApresentarBotaoEtapa3(Boolean.FALSE);
		setApresentarBotaoEtapa4(Boolean.FALSE);
		setApresentarBotaoConcluir3(Boolean.FALSE);
		setApresentarBotaoConcluir4(Boolean.TRUE);
		setApresentarBotaoVoltarEtapa1(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa2(Boolean.FALSE);
		setApresentarBotaoVoltarEtapa3(Boolean.TRUE);
		setApresentarBotaoBoleto(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 4");
	}

	public Boolean getApresentarOpcoes() {
		if (getPessoaVO().getCPF().equalsIgnoreCase("")) {
			return false;
		} else {
			return true;
		}
	}

	public Boolean getApresentarBotaoAlterar() {
		if ((getPessoaVO().getCodigo() == 0) || (getInscricaoVO().getCodigo() == 0)) {
			return false;
		} else {
			return true;
		}
	}

	

	public void iniciarInscricao() {
		if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
			setCpfValido(false);
			try {
				setApresentarDadosProcessoSeletivo(false);
				setApresentarBotaoInscricao(Boolean.FALSE);
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
			} catch (ConsistirException e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			montarListaSelectItemUnidadeEnsino();
			setApresentarDadosProcessoSeletivo(true);
			setApresentarBotaoInscricao(Boolean.FALSE);
			setArquivoVO(new ArquivoVO());
		}
	}

	public void alterarInscricao() {
		montarListaSelectItemUnidadeEnsino();
		// setar unidade ensino
		setUnidadeEnsinoVO(getInscricaoVO().getUnidadeEnsino());
		consultarProcessoSeletivo();
		// setar ProcessoSeletivo
		setProcessoSeletivoVO(getInscricaoVO().getProcSeletivo());
		montarListaSelectItemCursoPorUnidadeEnsino();
		setUnidadeEnsinoCursoVO(getInscricaoVO().getCursoOpcao1());
		// setar cursos da inscricao
		setApresentarInscricao(true);
		setApresentarDadosProcessoSeletivo(true);
		setApresentarBotaoEtapa2(Boolean.TRUE);
		setApresentarBotaoAlterarCandidato(Boolean.FALSE);
		setApresentarBotaoBoleto(Boolean.FALSE);
		setEtapa("Inscrição - Etapa 1");
	}

	public void emitirNovoBoleto() {
		montarListaSelectItemUnidadeEnsino();
		setApresentarDadosProcessoSeletivo(false);

	}

	public void consultarCandidatoCPF() {
		try {
			setMensagemDetalhada("");
			limparMensagem();
			setMensagem("");
			setMensagemID("");
			// if (getValidarCpf()) {
			if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
				setCpfValido(false);
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
			}
			// }
			consultarCandidatoPorCPF(getPessoaVO().getCPF());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setPessoaVO(new PessoaVO());
		}
	}

	public void consultarCandidatoPorCPF(String campoConsulta) throws Exception {
		setPermitirRecebimentoCartaoCreditoOnline(false);
		setApresentarBotaoBoleto(Boolean.FALSE);
		setPagamentoRealizadoComSucesso(false);
		PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(campoConsulta, false, false, false, getUsuarioLogado());
		if (!pessoa.getCodigo().equals(0)) {
			// PessoaVO obj =
			// getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(pessoa.getCodigo(),
			// false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			// PessoaVO obj =
			// getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(pessoa.getCodigo(),
			// false, true, false, getUsuarioLogado());

			if (!getFacadeFactory().getInscricaoFacade().consultarSeExisteInscricaoParaPessoa(pessoa.getCodigo())) {
				// setPessoaVO(new PessoaVO());
				// getPessoaVO().setCPF(campoConsulta);
				setPessoaVO(pessoa);
				setInscricaoVO(new InscricaoVO());
				setApresentarBotaoInscricao(Boolean.TRUE);
				setApresentarBotaoAlterarCandidato(Boolean.FALSE);
			} else {
				setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoa(pessoa.getCodigo(), 0,false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				pessoa.setNovoObj(Boolean.FALSE);
				setCursoOpcao2(getInscricaoVO().getCursoOpcao2());
				setCursoOpcao3(getInscricaoVO().getCursoOpcao3());
				setPessoaVO(pessoa);
				setFiliacaoVO(new FiliacaoVO());
				setArquivoVO(getInscricaoVO().getArquivoVO());

				
			}
			setFormacaoAcademicaVO(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorPessoaEEscolaridade(getPessoaVO().getCodigo(), NivelFormacaoAcademica.MEDIO, false, getUsuarioLogado()));
			setConcluiuEnsinoMedio(getFormacaoAcademicaVO().getCodigo() > 0);
		} else {
			setInscricaoVO(null);
			setPessoaVO(new PessoaVO());
			getPessoaVO().setCPF(campoConsulta);
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setConcluiuEnsinoMedio(false);
			setApresentarBotaoInscricao(Boolean.TRUE);
			setApresentarBotaoAlterarCandidato(Boolean.FALSE);

		}
	}

	public void consultarCandidatoCPFParaPreenchimentoComprovanteInscricao() {
		try {
			setMensagemDetalhada("");
			// if (getValidarCpf()) {
			if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
				setCpfValido(false);
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
			}
			// }
			consultarCandidatoPorCPFParaPreenchimentoComprovanteInscricao(getPessoaVO().getCPF());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setPessoaVO(new PessoaVO());
		}
	}

	public void consultarCandidatoCPFPreenchimentoComprovanteInscricao() {
		try {
			setMensagemDetalhada("");
			// if (getValidarCpf()) {
			if (!Uteis.verificaCPF(getPessoaVO().getCPF())) {
				setCpfValido(false);
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_cpfInvalido"));
			}
			// }
			consultarCandidatoPorCPFParaPreenchimentoComprovanteInscricao(getPessoaVO().getCPF());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setPessoaVO(new PessoaVO());
		}
	}

	public void consultarCandidatoPorCPFParaPreenchimentoComprovanteInscricao(String campoConsulta) throws Exception {
		PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorCPFUnico(campoConsulta, false, false, false, getUsuarioLogado());
		if (!pessoa.getCodigo().equals(0)) {
			if (!getFacadeFactory().getInscricaoFacade().consultarSeExisteInscricaoParaPessoa(pessoa.getCodigo())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_candidatoNaoEncontrado"));
			} else {
				setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarUltimaInscricaoPessoa(pessoa.getCodigo(), 0,false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				
				}
		} else {
			setInscricaoVO(null);
			throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_candidatoNaoEncontrado"));
		}
	}

	public void consultarInscricaoPorChavePrimaria() throws Exception {
		try {
			if (getValorConsultaInscricao().equals("") && getValorConsultaCPF().equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_campoInscricaoCPFNaoInformado"));
			}
			setOpcao1(false);
			setOpcao2(false);
			setOpcao3(false);
			setApresentarDadosDaInscricao(false);

			int valorInscricao = 0;
			if (getValorConsultaInscricao() != null && !getValorConsultaInscricao().equals("")) {
				valorInscricao = Integer.parseInt(getValorConsultaInscricao());
			}
			setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultaRapidaInscricaoUnicaPorCodigoInscricaoCpf(valorInscricao, getValorConsultaCPF(), 0, false, getUsuarioLogado()));

			if (getInscricaoVO().getCodigo() != null && getInscricaoVO().getCodigo() != 0) {
				setMensagemID("msg_dados_consultados");
				if (getInscricaoVO().getCursoOpcao1().getCodigo() != 0) {
					setOpcao1(true);
				}
				if (getInscricaoVO().getCursoOpcao2().getCodigo() != 0) {
					setOpcao2(true);
				}
				if (getInscricaoVO().getCursoOpcao3().getCodigo() != 0) {
					setOpcao3(true);
				}
				
			}
		} catch (Exception e) {
//			this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultarResultadoPorChavePrimaria() throws Exception {
		try {
			if (getValorConsultaInscricao().equals("") && getValorConsultaCPF().equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_processoSeletivo_campoInscricaoCPFNaoInformado"));
			}
			setOpcao1(false);
			setOpcao2(false);
			setOpcao3(false);
			setApresentarDadosDaInscricao(false);

			if (!getValorConsultaInscricao().equals("") && !getValorConsultaCPF().equals("")) {
				int valorInscricao = Integer.parseInt(getValorConsultaInscricao());
				setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarPorInscricaoCPF(valorInscricao, getValorConsultaCPF(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (!getValorConsultaInscricao().equals("")) {
				int valorInscricao = Integer.parseInt(getValorConsultaInscricao());
				setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarPorInscricaoUnico(valorInscricao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (!getValorConsultaCPF().equals("")) {
				setInscricaoVO(getFacadeFactory().getInscricaoFacade().consultarPorCPFPessoaUnico(getValorConsultaCPF(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			
			if (getInscricaoVO().getCodigo() != null && getInscricaoVO().getCodigo() != 0) {
                if (getInscricaoVO().getNaoCompareceu()) {
                    setMensagemID("msg_processoSeletivo_resultadoNaoCompareceu");
//                    this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
                    return "resultadoCandidato";
                }     
				setMensagemID("");
				setMensagem("Prezado(a) " + getInscricaoVO().getCandidato().getNome() + ", o resultado do processo seletivo ainda não está disponível. ");
                
				if (getInscricaoVO().getCursoOpcao1().getCodigo() != 0) {
					setOpcao1(true);
				}
				if (getInscricaoVO().getCursoOpcao2().getCodigo() != 0) {
					setOpcao2(true);
				}
				if (getInscricaoVO().getCursoOpcao3().getCodigo() != 0) {
					setOpcao3(true);
				}
				setApresentarDadosDaInscricao(true);
				ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralPadraoSistema();
				setOcultarClassificacao(config.getOcultarClassificacaoProcSeletivo());
				setOcultarMedia(config.getOcultarMediaProcSeletivo());
				setOcultarChamadaCandidato(config.getOcultarChamadaCandidatoProcSeletivo());				
//				inicializarResultadoProcSeletivoInscricao();
                //throw new Exception("Prezado(a) " + getInscricaoVO().getCandidato().getNome() + ", o resultado do processo seletivo ainda n?o est? dispon?vel. ");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoCandidato.xhtml");
		} catch (Exception e) {			
//			this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("resultadoCandidato.xhtml");
		}
	}

	

	public Boolean getApresentarResultado() {
//		if (getResultadoProcessoSeletivoVO() != null && getResultadoProcessoSeletivoVO().getCodigo() != 0) {
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	
	public String getApresentarMensagemErroRichModal() {
		if (getApresentarMensagemCpfRichModal()) {
			return "RichFaces.$('panelConsultaCPF').hide()";
		} else {
			return "";
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por adicionar um novo objeto da classe
	 * <code>Filiacao</code> para o objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void adicionarFiliacao() throws Exception {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getFiliacaoVO().setAluno(getPessoaVO().getCodigo());
			}
			if (getFiliacaoVO().getCep().equals("")) {
				getFiliacaoVO().setCep(getPessoaVO().getCEP());
			}
			getPessoaVO().adicionarObjFiliacaoVOs(getFiliacaoVO());
			setFiliacaoVO(new FiliacaoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por disponibilizar dados de um objeto da classe
	 * <code>Filiacao</code> para ediï¿½ï¿½o pelo usuï¿½rio.
	 */
	public void editarFiliacao() {
		try {
			FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
			setFiliacaoVO(obj.getClone());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe
	 * <code>Filiacao</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerFiliacao() throws Exception {
		FiliacaoVO obj = (FiliacaoVO) context().getExternalContext().getRequestMap().get("filiacaoItens");
		getPessoaVO().excluirObjFiliacaoVOs(obj.getNome());
		setMensagemID("msg_dados_excluidos");

	}

	/*
	 * Mï¿½todo responsï¿½vel por adicionar um novo objeto da classe
	 * <code>FormacaoAcademica</code> para o objeto <code>pessoaVO</code> da
	 * classe <code>Pessoa</code>
	 */
	public void adicionarFormacaoAcademica() throws Exception {
		try {
			if (!getPessoaVO().getCodigo().equals(0)) {
				getFormacaoAcademicaVO().setPessoa(getPessoaVO().getCodigo());
			}
			getPessoaVO().adicionarObjFormacaoAcademicaVOs(getFormacaoAcademicaVO());
			setFormacaoAcademicaVO(new FormacaoAcademicaVO());
			setMensagemID("msg_dados_adicionados");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por disponibilizar dados de um objeto da classe
	 * <code>FormacaoAcademica</code> para ediï¿½ï¿½o pelo usuï¿½rio.
	 */
	public void editarFormacaoAcademica() throws Exception {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
		setFormacaoAcademicaVO(obj);

	}

	/*
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe
	 * <code>FormacaoAcademica</code> do objeto <code>pessoaVO</code> da classe
	 * <code>Pessoa</code>
	 */
	public void removerFormacaoAcademica() throws Exception {
		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) context().getExternalContext().getRequestMap().get("formacaoAcademica");
		getPessoaVO().excluirObjFormacaoAcademicaVOs(obj.getCurso());
		setMensagemID("msg_dados_excluidos");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<UnidadeEnsinoVO> resultadoConsulta = new ArrayList<UnidadeEnsinoVO>(0);
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				if (!obj.getPermitirVisualizacaoLogin()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
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
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoOndeCursoDiferenteDePosGraduacao(Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorCurso(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCurso(codigoPrm, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CursoVO obj = (CursoVO) i.next();
				if (!obj.getNivelEducacionalPosGraduacao()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemCurso(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemCurso() {
		try {
			montarListaSelectItemCurso("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Metodo responsavel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo e uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarCursoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCursoFacade().consultarCursoApresentarProcessoSeletivoPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCursoPorUnidadeEnsinoCodigo() throws Exception {
		getInscricaoVO().setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcessoSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		getInscricaoVO().getQuestionarioVO().setCodigo(getInscricaoVO().getProcSeletivo().getQuestionario().getCodigo());
		tmpCodigoProcessoSeletivo = getInscricaoVO().getProcSeletivo().getCodigo();
		if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("1")) {
			setOpcao1(true);
			setOpcao2(false);
			setOpcao3(false);
		}
		if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("2")) {
			setOpcao1(true);
			setOpcao2(true);
			setOpcao3(false);
		}
		if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("3")) {
			setOpcao1(true);
			setOpcao2(true);
			setOpcao3(true);
		}
		getInscricaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		montarListaSelectItemCursoOpcao();
		montarListaSelectTipoSelecao();
		montarListaSelectItemDataProva();
		
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemCursoPorUnidadeEnsino() {
		try {
			montarListaSelectItemCursoPorUnidadeEnsinoCodigo();
		} catch (Exception e) {
			if (e instanceof ConsistirException) {
				inicializarApresentaInscricao();
				getListaSelectItemCursoOpcao().clear();
				getListaSelectItemDatasProva().clear();
			}
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	

	public void montarListaSelectItemUnidadeEnsinoPorCurso(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = new ArrayList(0);
		Iterator i = null;
		try {
			if (getCursoVO().getCodigo() != 0) {
				getProcessoSeletivoVO().setCodigo(0);
				inicializarBotaoInscricao();
			}
			resultadoConsulta = consultarUnidadeEnsinoPorCodigoCurso(getCursoVO().getCodigo());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				objs.add(new SelectItem(unidadeEnsinoCurso.getCodigo(), obj.getNome() + " - " + unidadeEnsinoCurso.getTurno().getNome()));
				obj = new UnidadeEnsinoVO();
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemUnidadeEnsino(objs);
			setProcessoSeletivoVO(new ProcSeletivoVO());
			setApresentarInscricao(false);
			setApresentarProcessoSeletivo(false);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsinoPorCurso() {
		try {
			montarListaSelectItemUnidadeEnsinoPorCurso("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorCodigoCurso(Integer codigoPrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCurso(codigoPrm, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, false, getUsuarioLogado());
		return lista;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>diaSemana</code>
	 */
	public List getListaSelectItemDiaSemanaDisponibilidadeHorario() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable diaSemanaDisponibilidadeHorarios = (Hashtable) Dominios.getDiaSemanaDisponibilidadeHorario();
		Enumeration keys = diaSemanaDisponibilidadeHorarios.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) diaSemanaDisponibilidadeHorarios.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipo</code>
	 */
	public List getListaSelectItemTipoFiliacao() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoFiliacaos = (Hashtable) Dominios.getTipoFiliacao();
		Enumeration keys = tipoFiliacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoFiliacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoFormacaoAcademica() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFormacaoAcademicas = (Hashtable) Dominios.getSituacaoFormacaoAcademica();
		Enumeration keys = situacaoFormacaoAcademicas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFormacaoAcademicas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>escolaridade</code>
	 */
	public List getListaSelectItemEscolaridadeFormacaoAcademica() throws Exception {
		List objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(NivelFormacaoAcademica.class, false);
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoPessoa</code>
	 */
	public List getListaSelectItemTipoPessoaPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPessoaBasicoPessoas = (Hashtable) Dominios.getTipoPessoaBasicoPessoa();
		Enumeration keys = tipoPessoaBasicoPessoas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPessoaBasicoPessoas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>estadoEmissaoRG</code>
	 */
	public List getListaSelectItemEstadoEmissaoRGPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estados = (Hashtable) Dominios.getEstado();
		Enumeration keys = estados.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estados.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>estadoCivil</code>
	 */
	public List getListaSelectItemEstadoCivilPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable estadoCivils = (Hashtable) Dominios.getEstadoCivil();
		Enumeration keys = estadoCivils.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) estadoCivils.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>sexo</code>
	 */
	public List getListaSelectItemSexoPessoa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable sexos = (Hashtable) Dominios.getSexo();
		Enumeration keys = sexos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) sexos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Nacionalidade</code>.
	 */
	public void montarListaSelectItemNacionalidade(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPaizPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PaizVO obj = (PaizVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNacionalidade()));
			}
			setListaSelectItemNacionalidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Nacionalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Paiz</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemNacionalidade() {
		try {
			montarListaSelectItemNacionalidade("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPaizPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPaizFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Naturalidade</code>.
	 */
	public void montarListaSelectItemNaturalidade(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCidadePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CidadeVO obj = (CidadeVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemNaturalidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Naturalidade</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Cidade</code>. Esta rotina nï¿½o recebe parï¿½metros para
	 * filtragem de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos
	 * dados da tela para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemNaturalidade() {
		try {
			montarListaSelectItemNaturalidade("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemCidade(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCidadePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CidadeVO obj = (CidadeVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemCidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem
	 * de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela
	 * para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemCidade() {
		try {
			montarListaSelectItemCidade("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarCidadePorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCidadeFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	
	

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	
	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>CursoOpcao1</code>.
	 */
	

	public void montarListaSelectItemOpcaoLinguaEstrangeira() {
		
	}

	

	public void montarListaSelectItemCursoOpcao(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		getUnidadeEnsinoCursoVO().setCodigo(0);
		try {
			if ((getInscricaoVO().getProcSeletivo() == null) || (tmpCodigoProcessoSeletivo.intValue() == 0)) {
				List objs = new ArrayList(0);
				setListaSelectItemCursoOpcao(objs);
				return;
			}
			if ((getInscricaoVO().getUnidadeEnsino() == null) || (getInscricaoVO().getUnidadeEnsino().getCodigo() == 0)) {
				List objs = new ArrayList(0);
				setListaSelectItemCursoOpcao(objs);
				return;
			}
			resultadoConsulta = consultarPorProcessoSeletivoCurso();
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			
			setListaSelectItemCursoOpcao(objs);
			inicializarApresentaInscricao();
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	
	public void montarListaSelectTipoSelecao() {
		try {
			getListaTipoSelecao().clear();
			String tipoSelecao = "";
			getListaTipoSelecao().add(new SelectItem("", ""));
			if (getInscricaoVO().getProcSeletivo().getTipoProcessoSeletivo()) {
				getListaTipoSelecao().add(new SelectItem("PS","PROCESSO SELETIVO"));
				tipoSelecao = "PS";
			}
			if (getInscricaoVO().getProcSeletivo().getTipoEnem()) {
				getListaTipoSelecao().add(new SelectItem("EN", "ENEM/ANÁLISE DOCUMENTO"));
				tipoSelecao = "EN";
			}
			if (getInscricaoVO().getProcSeletivo().getTipoPortadorDiploma()) {
				getListaTipoSelecao().add(new SelectItem("PD","PORTADOR DE DIPLOMA"));
				tipoSelecao = "PD";
			}	
			if (getInscricaoVO().getProcSeletivo().getTipoTransferencia()) {
				getListaTipoSelecao().add(new SelectItem("TR","TRANSFERÊNCIA"));
				tipoSelecao = "TR";
			}
			if (getListaTipoSelecao().size() == 2) {
				getListaTipoSelecao().remove(0);				
				getInscricaoVO().setFormaIngresso(tipoSelecao);
			}
			inicializarApresentaInscricao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDataProva() {
		try {
			
			inicializarApresentaInscricao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemCursoOpcao() {
		try {
			montarListaSelectItemCursoOpcao("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPorProcessoSeletivoCurso() throws Exception {
//		return getFacadeFactory().getProcSeletivoCursoFacade().consultarPorCodigoProcSeletivoUnidadeEnsinoOpcaoInscicao(getInscricaoVO().getProcSeletivo().getCodigo(), this.getInscricaoVO().getUnidadeEnsino().getCodigo(), new Date() , Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS , getUsuarioLogado());
	return null;
	}

	/**
	 * Mï¿½todo responsï¿½vel por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemNacionalidade();
//		montarListaSelectItemAreaConhecimento();
		montarListaSelectItemOpcaoLinguaEstrangeira();
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>formaAcessoProcSeletivo</code>
	 */
	public List getListaSelectItemFormaAcessoProcSeletivoInscricao() throws Exception {
		List objs = new ArrayList(0);
		Hashtable inscricaoFormaAcessos = (Hashtable) Dominios.getInscricaoFormaAcesso();
		Enumeration keys = inscricaoFormaAcessos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) inscricaoFormaAcessos.get(value);
			objs.add(new SelectItem(value, label));
		}
		keys = null;
		return objs;
	}

	/*
	 * Mï¿½todo responsï¿½vel por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoInscricao() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoProcessoSeletivos = (Hashtable) Dominios.getSituacaoProcessoSeletivo();
		Enumeration keys = situacaoProcessoSeletivos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoProcessoSeletivos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List getListaSelectItemOpcaoLinguaEstrangeiraInscricao() throws Exception {
		List objs = new ArrayList(0);
		Hashtable inscricaoOpcaoLinguaEstrangeiras = (Hashtable) Dominios.getInscricaoOpcaoLinguaEstrangeira();
		Enumeration keys = inscricaoOpcaoLinguaEstrangeiras.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) inscricaoOpcaoLinguaEstrangeiras.get(value);
			objs.add(new SelectItem(value, label));
		}
		keys = null;
		return objs;
	}

	public void mostrarLinguaEstrangeira() {
		if (getProcessoSeletivoVO().getCodigo().equals(0)) {
			setApresentarLinguaEstrangeira(false);
		} else {
			setApresentarLinguaEstrangeira(true);
		}
	}

	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
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
			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarNaturalidade() {
		try {
			List objs = new ArrayList(0);
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
			setListaConsultaNaturalidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getPessoaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public void selecionarNaturalidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("naturalidadeItens");
		getPessoaVO().setNaturalidade(obj);
		getListaConsultaNaturalidade().clear();
		this.setValorConsultaNaturalidade("");
		this.setCampoConsultaNaturalidade("");
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoNecessidadesEspeciais() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("Nenhum", "Nenhum"));
		itens.add(new SelectItem("Gravida", "Grávida"));
		itens.add(new SelectItem("Canhoto", "Canhoto"));
		itens.add(new SelectItem("Port. de nec. especiais", "Port. de nec. especiais"));
		return itens;
	}

	public List getTipoConsultaNaturalidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getPessoaVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarFiliacaoPessoa() {
		try {
			setFiliacaoVO(getFacadeFactory().getFiliacaoFacade().carregarApenasUmPorCPF(getFiliacaoVO(), getPessoaVO(), true, getUsuarioLogado()));
			if (getFiliacaoVO().getPais().getCodigo() == 0 || getFiliacaoVO().getPais().getCEP().isEmpty()) {
				getFiliacaoVO().getPais().setCEP(getPessoaVO().getCEP());
				getFiliacaoVO().getPais().setEndereco(getPessoaVO().getEndereco());
				getFiliacaoVO().getPais().setSetor(getPessoaVO().getSetor());
				getFiliacaoVO().getPais().setNumero(getPessoaVO().getNumero());
				getFiliacaoVO().getPais().setComplemento(getPessoaVO().getComplemento());
				getFiliacaoVO().getPais().setCidade(getPessoaVO().getCidade());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarEnderecoPessoaFiliacao() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getFiliacaoVO().getPais(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFiliacaoCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFiliacaoCidade().equals("codigo")) {
				if (getValorConsultaFiliacaoCidade().equals("")) {
					setValorConsultaFiliacaoCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFiliacaoCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("nome")) {
				if (getValorConsultaFiliacaoCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaFiliacaoCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaFiliacaoCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaFiliacaoCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaFiliacaoCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarFiliacaoCidade() {
		try {
			CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("filiacaoCidadeItens");
			getFiliacaoVO().getPais().setCidade(obj);
			listaConsultaFiliacaoCidade.clear();
			this.setValorConsultaFiliacaoCidade("");
			this.setCampoConsultaFiliacaoCidade("");
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void verificarExistenciaResponsavelFinanceiroExistente() throws Exception {
		try {
			if (getFiliacaoVO().getResponsavelFinanceiro()) {
				PessoaVO.validarDadosFiliacaoResponsavelFinanceiro(getPessoaVO(), getFiliacaoVO());
			}
			setMensagemDetalhada("");
			setMensagemID("msg_entre_dados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public String getApresentarCss() {
		if (getFiliacaoVO() != null && getFiliacaoVO().getCodigo() != 0) {
			return "camposSomenteLeitura";
		} else {
			return "camposObrigatorios";
		}

	}

	public String getCampoConsultaFiliacaoCidade() {
		if (campoConsultaFiliacaoCidade == null) {
			campoConsultaFiliacaoCidade = "";
		}
		return campoConsultaFiliacaoCidade;
	}

	public void setCampoConsultaFiliacaoCidade(String campoConsultaFiliacaoCidade) {
		this.campoConsultaFiliacaoCidade = campoConsultaFiliacaoCidade;
	}

	public String getValorConsultaFiliacaoCidade() {
		if (valorConsultaFiliacaoCidade == null) {
			valorConsultaFiliacaoCidade = "";
		}
		return valorConsultaFiliacaoCidade;
	}

	public void setValorConsultaFiliacaoCidade(String valorConsultaFiliacaoCidade) {
		this.valorConsultaFiliacaoCidade = valorConsultaFiliacaoCidade;
	}

	public List getListaConsultaFiliacaoCidade() {
		if (listaConsultaFiliacaoCidade == null) {
			listaConsultaFiliacaoCidade = new ArrayList();
		}
		return listaConsultaFiliacaoCidade;
	}

	public void setListaConsultaFiliacaoCidade(List listaConsultaFiliacaoCidade) {
		this.listaConsultaFiliacaoCidade = listaConsultaFiliacaoCidade;
	}

	// public void mostrarFormaAcessoProcSeletivo() {
	// if (getInscricaoVO().getOpcaoLinguaEstrangeira().getCodigo().equals(null)
	// || getInscricaoVO().getOpcaoLinguaEstrangeira().getCodigo().equals(0)) {
	// setApresentarFormaAcessoProcSeletivo(false);
	// } else {
	// setApresentarFormaAcessoProcSeletivo(true);
	// }
	// }
	
	

	public Boolean getMenuInscricao() {
		if (menuInscricao == null) {
			menuInscricao = false;
		}
		return menuInscricao;
	}

	public void setMenuInscricao(Boolean menuInscricao) {
		this.menuInscricao = menuInscricao;
	}

	public Boolean getMenuResultado() {
		if (menuResultado == null) {
			menuResultado = false;
		}
		return menuResultado;
	}

	public void setMenuResultado(Boolean menuResultado) {
		this.menuResultado = menuResultado;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaOpcao() {
		if (listaOpcao == null) {
			listaOpcao = new ArrayList(0);
		}
		return listaOpcao;
	}

	public void setListaOpcao(List listaOpcao) {
		this.listaOpcao = listaOpcao;
	}

	public Integer getOpcao() {
		if (opcao == null) {
			opcao = 0;
		}
		return opcao;
	}

	public void setOpcao(Integer opcao) {
		this.opcao = opcao;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public Boolean getApresentarCurso() {
		if (apresentarCurso == null) {
			apresentarCurso = false;
		}
		return apresentarCurso;
	}

	public void setApresentarCurso(Boolean apresentarCurso) {
		this.apresentarCurso = apresentarCurso;
	}

	public Boolean getApresentarUnidadeEnsino() {
		if (apresentarUnidadeEnsino == null) {
			apresentarUnidadeEnsino = false;
		}
		return apresentarUnidadeEnsino;
	}

	public void setApresentarUnidadeEnsino(Boolean apresentarUnidadeEnsino) {
		this.apresentarUnidadeEnsino = apresentarUnidadeEnsino;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public List<SelectItem> getListaSelectItemProcessoSeletivo() {
		if (listaSelectItemProcessoSeletivo == null) {
			listaSelectItemProcessoSeletivo = new ArrayList(0);
		}
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List<SelectItem> listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}

	public ProcSeletivoVO getProcessoSeletivoVO() {
		if (processoSeletivoVO == null) {
			processoSeletivoVO = new ProcSeletivoVO();
		}
		return processoSeletivoVO;
	}

	public void setProcessoSeletivoVO(ProcSeletivoVO processoSeletivoVO) {
		this.processoSeletivoVO = processoSeletivoVO;
	}

	public Boolean getApresentarInscricao() {
		if (apresentarInscricao == null) {
			apresentarInscricao = false;
		}
		return apresentarInscricao;
	}

	public void setApresentarInscricao(Boolean apresentarInscricao) {
		this.apresentarInscricao = apresentarInscricao;
	}

	public Boolean getApresentarProcessoSeletivo() {
		if (apresentarProcessoSeletivo == null) {
			apresentarProcessoSeletivo = false;
		}
		return apresentarProcessoSeletivo;
	}

	public void setApresentarProcessoSeletivo(Boolean apresentarProcessoSeletivo) {
		this.apresentarProcessoSeletivo = apresentarProcessoSeletivo;
	}

	public Boolean getApresentarPanelInscricaoCandidado() {
		if (apresentarPanelInscricaoCandidado == null) {
			apresentarPanelInscricaoCandidado = false;
		}
		return apresentarPanelInscricaoCandidado;
	}

	public void setApresentarPanelInscricaoCandidado(Boolean apresentarPanelInscricaoCandidado) {
		this.apresentarPanelInscricaoCandidado = apresentarPanelInscricaoCandidado;
	}

	public Boolean getApresentarPanelCadastroCandidado() {
		if (apresentarPanelCadastroCandidado == null) {
			apresentarPanelCadastroCandidado = false;
		}
		return apresentarPanelCadastroCandidado;
	}

	public void setApresentarPanelCadastroCandidado(Boolean apresentarPanelCadastroCandidado) {
		this.apresentarPanelCadastroCandidado = apresentarPanelCadastroCandidado;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
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

	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
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

	public List getListaSelectItemCidade() {
		if (listaSelectItemCidade == null) {
			listaSelectItemCidade = new ArrayList(0);
		}
		return listaSelectItemCidade;
	}

	public void setListaSelectItemCidade(List listaSelectItemCidade) {
		this.listaSelectItemCidade = listaSelectItemCidade;
	}

	public List getListaSelectItemNacionalidade() {
		if (listaSelectItemNacionalidade == null) {
			listaSelectItemNacionalidade = new ArrayList(0);
		}
		return listaSelectItemNacionalidade;
	}

	public void setListaSelectItemNacionalidade(List listaSelectItemNacionalidade) {
		this.listaSelectItemNacionalidade = listaSelectItemNacionalidade;
	}

	public List getListaSelectItemNaturalidade() {
		if (listaSelectItemNaturalidade == null) {
			listaSelectItemNaturalidade = new ArrayList(0);
		}
		return listaSelectItemNaturalidade;
	}

	public void setListaSelectItemNaturalidade(List listaSelectItemNaturalidade) {
		this.listaSelectItemNaturalidade = listaSelectItemNaturalidade;
	}

	public InscricaoVO getInscricaoVO() {
		if (inscricaoVO == null) {
			inscricaoVO = new InscricaoVO();
		}
		return inscricaoVO;
	}

	public void setInscricaoVO(InscricaoVO inscricaoVO) {
		this.inscricaoVO = inscricaoVO;
	}

	public List getListaSelectItemCursoOpcao() {
		if (listaSelectItemCursoOpcao == null) {
			listaSelectItemCursoOpcao = new ArrayList(0);
		}
		return listaSelectItemCursoOpcao;
	}

	public void setListaSelectItemCursoOpcao(List listaSelectItemCursoOpcao) {
		this.listaSelectItemCursoOpcao = listaSelectItemCursoOpcao;
	}

	public List getListaSelectItemOpcaolinguaEstrangeira() {
		if (listaSelectItemOpcaolinguaEstrangeira == null) {
			listaSelectItemOpcaolinguaEstrangeira = new ArrayList(0);
		}
		return listaSelectItemOpcaolinguaEstrangeira;
	}

	public void setListaSelectItemOpcaolinguaEstrangeira(List listaSelectItemOpcaolinguaEstrangeira) {
		this.listaSelectItemOpcaolinguaEstrangeira = listaSelectItemOpcaolinguaEstrangeira;
	}

	public Boolean getOpcao1() {
		if (opcao1 == null) {
			opcao1 = false;
		}
		return opcao1;
	}

	public void setOpcao1(Boolean opcao1) {
		this.opcao1 = opcao1;
	}

	public Boolean getOpcao2() {
		if (opcao2 == null) {
			opcao2 = false;
		}
		return opcao2;
	}

	public void setOpcao2(Boolean opcao2) {
		this.opcao2 = opcao2;
	}

	public Boolean getOpcao3() {
		if (opcao3 == null) {
			opcao3 = false;
		}
		return opcao3;
	}

	public void setOpcao3(Boolean opcao3) {
		this.opcao3 = opcao3;
	}

	public Boolean getApresentarMensagemCpfRichModal() {
		if (apresentarMensagemCpfRichModal == null) {
			apresentarMensagemCpfRichModal = false;
		}
		return apresentarMensagemCpfRichModal;
	}

	public void setApresentarMensagemCpfRichModal(Boolean apresentarMensagemCpfRichModal) {
		this.apresentarMensagemCpfRichModal = apresentarMensagemCpfRichModal;
	}

	public String getMensagemCpfRichModal() {
		if (MensagemCpfRichModal == null) {
			MensagemCpfRichModal = "";
		}
		return MensagemCpfRichModal;
	}

	public void setMensagemCpfRichModal(String MensagemCpfRichModal) {
		this.MensagemCpfRichModal = MensagemCpfRichModal;
	}

	public Boolean getMenuCurso() {
		if (menuCurso == null) {
			menuCurso = false;
		}
		return menuCurso;
	}

	public void setMenuCurso(Boolean menuCurso) {
		this.menuCurso = menuCurso;
	}

	public Boolean getApresentarPanelCursoCandidado() {
		if (apresentarPanelCursoCandidado == null) {
			apresentarPanelCursoCandidado = false;
		}
		return apresentarPanelCursoCandidado;
	}

	public void setApresentarPanelCursoCandidado(Boolean apresentarPanelCursoCandidado) {
		this.apresentarPanelCursoCandidado = apresentarPanelCursoCandidado;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public List getListaDisciplinasAnteriores() {
		if (listaDisciplinasAnteriores == null) {
			listaDisciplinasAnteriores = new ArrayList(0);
		}
		return listaDisciplinasAnteriores;
	}

	public void setListaDisciplinasAnteriores(List listaDisciplinasAnteriores) {
		this.listaDisciplinasAnteriores = listaDisciplinasAnteriores;
	}

	public Boolean getPreRequisito() {
		if (preRequisito == null) {
			preRequisito = false;
		}
		return preRequisito;
	}

	public void setPreRequisito(Boolean preRequisito) {
		this.preRequisito = preRequisito;
	}

	public Boolean getApresentarPanelCalendarioCandidado() {
		if (apresentarPanelCalendarioCandidado == null) {
			apresentarPanelCalendarioCandidado = false;
		}
		return apresentarPanelCalendarioCandidado;
	}

	public void setApresentarPanelCalendarioCandidado(Boolean apresentarPanelCalendarioCandidado) {
		this.apresentarPanelCalendarioCandidado = apresentarPanelCalendarioCandidado;
	}

	public Boolean getMenuCalendario() {
		if (menuCalendario == null) {
			menuCalendario = false;
		}
		return menuCalendario;
	}

	public void setMenuCalendario(Boolean menuCalendario) {
		this.menuCalendario = menuCalendario;
	}

	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if (processoMatriculaVO == null) {
			processoMatriculaVO = new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}

	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}

	public List getListaProcessoMatricula() {
		if (listaProcessoMatricula == null) {
			listaProcessoMatricula = new ArrayList(0);
		}
		return listaProcessoMatricula;
	}

	public void setListaProcessoMatricula(List listaProcessoMatricula) {
		this.listaProcessoMatricula = listaProcessoMatricula;
	}

	public Boolean getApresentarPanelMatriculaCandidado() {
		if (apresentarPanelMatriculaCandidado == null) {
			apresentarPanelMatriculaCandidado = false;
		}
		return apresentarPanelMatriculaCandidado;
	}

	public void setApresentarPanelMatriculaCandidado(Boolean apresentarPanelMatriculaCandidado) {
		this.apresentarPanelMatriculaCandidado = apresentarPanelMatriculaCandidado;
	}

	public Boolean getMenuMatricula() {
		if (menuMatricula == null) {
			menuMatricula = false;
		}
		return menuMatricula;
	}

	public void setMenuMatricula(Boolean menuMatricula) {
		this.menuMatricula = menuMatricula;
	}

	/**
	 * @return the apresentarLinguaEstrangeira
	 */
	public Boolean getApresentarLinguaEstrangeira() {
		if (apresentarLinguaEstrangeira == null) {
			apresentarLinguaEstrangeira = false;
		}
		return apresentarLinguaEstrangeira;
	}

	/**
	 * @param apresentarLinguaEstrangeira
	 *            the apresentarLinguaEstrangeira to set
	 */
	public void setApresentarLinguaEstrangeira(Boolean apresentarLinguaEstrangeira) {
		this.apresentarLinguaEstrangeira = apresentarLinguaEstrangeira;
	}

	/**
	 * @return the apresentarFormaAcessoProcSeletivo
	 */
	public Boolean getApresentarFormaAcessoProcSeletivo() {
		if (apresentarFormaAcessoProcSeletivo == null) {
			apresentarFormaAcessoProcSeletivo = false;
		}
		return apresentarFormaAcessoProcSeletivo;
	}

	/**
	 * @param apresentarFormaAcessoProcSeletivo
	 *            the apresentarFormaAcessoProcSeletivo to set
	 */
	public void setApresentarFormaAcessoProcSeletivo(Boolean apresentarFormaAcessoProcSeletivo) {
		this.apresentarFormaAcessoProcSeletivo = apresentarFormaAcessoProcSeletivo;
	}

	/**
	 * @return the abaSelecionada
	 */
	public String getAbaSelecionada() {
		if (abaSelecionada == null) {
			abaSelecionada = "";
		}
		return abaSelecionada;
	}

	/**
	 * @param abaSelecionada
	 *            the abaSelecionada to set
	 */
	public void setAbaSelecionada(String abaSelecionada) {
		this.abaSelecionada = abaSelecionada;
	}

	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	/**
	 * @return the campoConsultaNaturalidade
	 */
	public String getCampoConsultaNaturalidade() {
		if (campoConsultaNaturalidade == null) {
			campoConsultaNaturalidade = "";
		}
		return campoConsultaNaturalidade;
	}

	/**
	 * @param campoConsultaNaturalidade
	 *            the campoConsultaNaturalidade to set
	 */
	public void setCampoConsultaNaturalidade(String campoConsultaNaturalidade) {
		this.campoConsultaNaturalidade = campoConsultaNaturalidade;
	}

	/**
	 * @return the valorConsultaNaturalidade
	 */
	public String getValorConsultaNaturalidade() {
		if (valorConsultaNaturalidade == null) {
			valorConsultaNaturalidade = "";
		}
		return valorConsultaNaturalidade;
	}

	/**
	 * @param valorConsultaNaturalidade
	 *            the valorConsultaNaturalidade to set
	 */
	public void setValorConsultaNaturalidade(String valorConsultaNaturalidade) {
		this.valorConsultaNaturalidade = valorConsultaNaturalidade;
	}

	/**
	 * @return the listaConsultaCidade
	 */
	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList(0);
		}
		return listaConsultaCidade;
	}

	/**
	 * @param listaConsultaCidade
	 *            the listaConsultaCidade to set
	 */
	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	/**
	 * @return the listaConsultaNaturalidade
	 */
	public List getListaConsultaNaturalidade() {
		if (listaConsultaNaturalidade == null) {
			listaConsultaNaturalidade = new ArrayList(0);
		}
		return listaConsultaNaturalidade;
	}

	/**
	 * @param listaConsultaNaturalidade
	 *            the listaConsultaNaturalidade to set
	 */
	public void setListaConsultaNaturalidade(List listaConsultaNaturalidade) {
		this.listaConsultaNaturalidade = listaConsultaNaturalidade;
	}

	/**
	 * @return the apresentarDescricaoNecessidadesEspeciais
	 */
	public boolean getApresentarDescricaoNecessidadesEspeciais() {
		if (pessoaVO.getPortadorNecessidadeEspecial()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the resultadoProcessoSeletivoVO
	 */
	

	/**
	 * @return the cpfValido
	 */
	public Boolean getCpfValido() {
		if (cpfValido == null) {
			cpfValido = false;
		}
		return cpfValido;
	}

	/**
	 * @param cpfValido
	 *            the cpfValido to set
	 */
	public void setCpfValido(Boolean cpfValido) {
		this.cpfValido = cpfValido;
	}

	/**
	 * @return the valorConsultaInscricao
	 */
	public String getValorConsultaInscricao() {
		if (valorConsultaInscricao == null) {
			valorConsultaInscricao = "";
		}
		return valorConsultaInscricao;
	}

	/**
	 * @param valorConsultaInscricao
	 *            the valorConsultaInscricao to set
	 */
	public void setValorConsultaInscricao(String valorConsultaInscricao) {
		this.valorConsultaInscricao = valorConsultaInscricao;
	}

	/**
	 * @return the valorConsultaCPF
	 */
	public String getValorConsultaCPF() {
		if (valorConsultaCPF == null) {
			valorConsultaCPF = "";
		}
		return valorConsultaCPF;
	}

	/**
	 * @param valorConsultaCPF
	 *            the valorConsultaCPF to set
	 */
	public void setValorConsultaCPF(String valorConsultaCPF) {
		this.valorConsultaCPF = valorConsultaCPF;
	}

	/**
	 * @return the apresentarDadosDaInscriï¿½ï¿½o
	 */
	public boolean isApresentarDadosDaInscricao() {
		return apresentarDadosDaInscricao;
	}

	/**
	 * @param apresentarDadosDaInscriï
	 *            ¿½ï¿½o the apresentarDadosDaInscriï¿½ï¿½o to set
	 */
	public void setApresentarDadosDaInscricao(boolean apresentarDadosDaInscricao) {
		this.apresentarDadosDaInscricao = apresentarDadosDaInscricao;
	}

	public String getNomeCursoAprovado() {
		if (nomeCursoAprovado == null) {
			nomeCursoAprovado = "";
		}
		return nomeCursoAprovado;
	}

	public void setNomeCursoAprovado(String nomeCursoAprovado) {
		this.nomeCursoAprovado = nomeCursoAprovado;
	}

	/**
	 * @return the apresentarDadosProcessoSeletivo
	 */
	public Boolean getApresentarDadosProcessoSeletivo() {
		if (apresentarDadosProcessoSeletivo == null) {
			apresentarDadosProcessoSeletivo = false;
		}
		return apresentarDadosProcessoSeletivo;
	}

	/**
	 * @param apresentarDadosProcessoSeletivo
	 *            the apresentarDadosProcessoSeletivo to set
	 */
	public void setApresentarDadosProcessoSeletivo(Boolean apresentarDadosProcessoSeletivo) {
		this.apresentarDadosProcessoSeletivo = apresentarDadosProcessoSeletivo;
	}

	/**
	 * @return the dataProvaSelecionada
	 */
	public String getDataProvaSelecionada() {
		if (dataProvaSelecionada == null) {
			dataProvaSelecionada = "";
		}
		return dataProvaSelecionada;
	}

	/**
	 * @param dataProvaSelecionada
	 *            the dataProvaSelecionada to set
	 */
	public void setDataProvaSelecionada(String dataProvaSelecionada) {
		this.dataProvaSelecionada = dataProvaSelecionada;
	}

	/**
	 * @return the listaSelectItemDatasProva
	 */
	public List getListaSelectItemDatasProva() {
		if (listaSelectItemDatasProva == null) {
			listaSelectItemDatasProva = new ArrayList(0);
		}
		return listaSelectItemDatasProva;
	}

	/**
	 * @param listaSelectItemDatasProva
	 *            the listaSelectItemDatasProva to set
	 */
	public void setListaSelectItemDatasProva(List listaSelectItemDatasProva) {
		this.listaSelectItemDatasProva = listaSelectItemDatasProva;
	}

	/**
	 * @return the cursoOpcao2
	 */
	public UnidadeEnsinoCursoVO getCursoOpcao2() {
		if (cursoOpcao2 == null) {
			cursoOpcao2 = new UnidadeEnsinoCursoVO();
		}
		return cursoOpcao2;
	}

	/**
	 * @param cursoOpcao2
	 *            the cursoOpcao2 to set
	 */
	public void setCursoOpcao2(UnidadeEnsinoCursoVO cursoOpcao2) {
		this.cursoOpcao2 = cursoOpcao2;
	}

	/**
	 * @return the cursoOpcao3
	 */
	public UnidadeEnsinoCursoVO getCursoOpcao3() {
		if (cursoOpcao3 == null) {
			cursoOpcao3 = new UnidadeEnsinoCursoVO();
		}
		return cursoOpcao3;
	}

	/**
	 * @param cursoOpcao3
	 *            the cursoOpcao3 to set
	 */
	public void setCursoOpcao3(UnidadeEnsinoCursoVO cursoOpcao3) {
		this.cursoOpcao3 = cursoOpcao3;
	}

	public boolean getIsPossuiOpcaoLinguaEstrangeira() {
		if (getListaSelectItemOpcaolinguaEstrangeira().size() > 1) {
			return true;
		}
		return false;
	}

	

	public String getEtapa() {
		if (etapa == null) {
			etapa = "";
		}
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public Boolean getApresentarEtapa1NovaVisao() {
		if (apresentarEtapa1NovaVisao == null) {
			apresentarEtapa1NovaVisao = Boolean.FALSE;
		}
		return apresentarEtapa1NovaVisao;
	}

	public void setApresentarEtapa1NovaVisao(Boolean apresentarEtapa1NovaVisao) {
		this.apresentarEtapa1NovaVisao = apresentarEtapa1NovaVisao;
	}

	public Boolean getApresentarEtapa2NovaVisao() {
		if (apresentarEtapa2NovaVisao == null) {
			apresentarEtapa2NovaVisao = Boolean.FALSE;
		}
		return apresentarEtapa2NovaVisao;
	}

	public void setApresentarEtapa2NovaVisao(Boolean apresentarEtapa2NovaVisao) {
		this.apresentarEtapa2NovaVisao = apresentarEtapa2NovaVisao;
	}

	public Boolean getApresentarEtapa3NovaVisao() {
		if (apresentarEtapa3NovaVisao == null) {
			apresentarEtapa3NovaVisao = Boolean.FALSE;
		}
		return apresentarEtapa3NovaVisao;
	}

	public void setApresentarEtapa3NovaVisao(Boolean apresentarEtapa3NovaVisao) {
		this.apresentarEtapa3NovaVisao = apresentarEtapa3NovaVisao;
	}

	public Boolean getApresentarEtapa4NovaVisao() {
		if (apresentarEtapa4NovaVisao == null) {
			apresentarEtapa4NovaVisao = Boolean.FALSE;
		}
		return apresentarEtapa4NovaVisao;
	}

	public void setApresentarEtapa4NovaVisao(Boolean apresentarEtapa4NovaVisao) {
		this.apresentarEtapa4NovaVisao = apresentarEtapa4NovaVisao;
	}

	public Boolean getApresentarBotaoEtapa3() {
		if (apresentarBotaoEtapa3 == null) {
			apresentarBotaoEtapa3 = Boolean.FALSE;
		}
		return apresentarBotaoEtapa3;
	}

	public void setApresentarBotaoEtapa3(Boolean apresentarBotaoEtapa3) {
		this.apresentarBotaoEtapa3 = apresentarBotaoEtapa3;
	}

	public Boolean getApresentarBotaoEtapa2() {
		if (apresentarBotaoEtapa2 == null) {
			apresentarBotaoEtapa2 = Boolean.FALSE;
		}
		return apresentarBotaoEtapa2;
	}

	public void setApresentarBotaoEtapa2(Boolean apresentarBotaoEtapa2) {
		this.apresentarBotaoEtapa2 = apresentarBotaoEtapa2;
	}

	public Boolean getApresentarBotaoInscricao() {
		if (apresentarBotaoInscricao == null) {
			apresentarBotaoInscricao = Boolean.FALSE;
		}
		return apresentarBotaoInscricao;
	}

	public void setApresentarBotaoInscricao(Boolean apresentarBotaoInscricao) {
		this.apresentarBotaoInscricao = apresentarBotaoInscricao;
	}

	public boolean getIsApresentarProcessoSeletivoEtapa1() {
		return getUnidadeEnsinoVO().getCodigo() != 0;
	}

	public boolean getIsApresentarCursoOpcao1() {
		return getProcessoSeletivoVO().getCodigo() != 0;
	}

	public Boolean getApresentarBotaoVoltarEtapa2() {
		if (apresentarBotaoVoltarEtapa2 == null) {
			apresentarBotaoVoltarEtapa2 = Boolean.FALSE;
		}
		return apresentarBotaoVoltarEtapa2;
	}

	public void setApresentarBotaoVoltarEtapa2(Boolean apresentarBotaoVoltarEtapa2) {
		this.apresentarBotaoVoltarEtapa2 = apresentarBotaoVoltarEtapa2;
	}

	public Boolean getApresentarBotaoVoltarEtapa3() {
		if (apresentarBotaoVoltarEtapa3 == null) {
			apresentarBotaoVoltarEtapa3 = Boolean.FALSE;
		}
		return apresentarBotaoVoltarEtapa3;
	}

	public void setApresentarBotaoVoltarEtapa3(Boolean apresentarBotaoVoltarEtapa3) {
		this.apresentarBotaoVoltarEtapa3 = apresentarBotaoVoltarEtapa3;
	}

	public Boolean getApresentarBotaoVoltarEtapa1() {
		if (apresentarBotaoVoltarEtapa1 == null) {
			apresentarBotaoVoltarEtapa1 = Boolean.FALSE;
		}
		return apresentarBotaoVoltarEtapa1;
	}

	public void setApresentarBotaoVoltarEtapa1(Boolean apresentarBotaoVoltarEtapa1) {
		this.apresentarBotaoVoltarEtapa1 = apresentarBotaoVoltarEtapa1;
	}

	public Boolean getApresentarBotaoEtapa4() {
		if (apresentarBotaoEtapa4 == null) {
			apresentarBotaoEtapa4 = Boolean.FALSE;
		}
		return apresentarBotaoEtapa4;
	}

	public void setApresentarBotaoEtapa4(Boolean apresentarBotaoEtapa4) {
		this.apresentarBotaoEtapa4 = apresentarBotaoEtapa4;
	}

	public Boolean getApresentarBotaoAlterarCandidato() {
		if (apresentarBotaoAlterarCandidato == null) {
			apresentarBotaoAlterarCandidato = Boolean.FALSE;
		}
		return apresentarBotaoAlterarCandidato;
	}

	public void setApresentarBotaoAlterarCandidato(Boolean apresentarBotaoAlterarCandidato) {
		this.apresentarBotaoAlterarCandidato = apresentarBotaoAlterarCandidato;
	}

	public Boolean getApresentarBotaoBoleto() {
		if (apresentarBotaoBoleto == null) {
			apresentarBotaoBoleto = Boolean.FALSE;
		}
		return apresentarBotaoBoleto;
	}

	public void setApresentarBotaoBoleto(Boolean apresentarBotaoBoleto) {
		this.apresentarBotaoBoleto = apresentarBotaoBoleto;
	}

	public Boolean getApresentarBotaoConcluir3() {
		if (apresentarBotaoConcluir3 == null) {
			apresentarBotaoConcluir3 = Boolean.FALSE;
		}
		return apresentarBotaoConcluir3;
	}

	public void setApresentarBotaoConcluir3(Boolean apresentarBotaoConcluir3) {
		this.apresentarBotaoConcluir3 = apresentarBotaoConcluir3;
	}

	public Boolean getApresentarBotaoConcluir4() {
		if (apresentarBotaoConcluir4 == null) {
			apresentarBotaoConcluir4 = Boolean.FALSE;
		}
		return apresentarBotaoConcluir4;
	}

	public void setApresentarBotaoConcluir4(Boolean apresentarBotaoConcluir4) {
		this.apresentarBotaoConcluir4 = apresentarBotaoConcluir4;
	}

	

	

	public Boolean getConcluiuEnsinoMedio() {
		if (concluiuEnsinoMedio == null) {
			concluiuEnsinoMedio = true;
		}
		return concluiuEnsinoMedio;
	}

	public void setConcluiuEnsinoMedio(Boolean concluiuEnsinoMedio) {
		this.concluiuEnsinoMedio = concluiuEnsinoMedio;
	}

	public List<SelectItem> getListaSelectItemCorRaca() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class, "valor", "descricao", false);
		//return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(CorRaca.class);
	}
	
	/**
	 * Responsável por realizar a invalidação da sessão.
	 * @author Wellington
	 * 18/02/2015
	 */
	private void realizarInvalidacaoSessao() {
		if(context().getExternalContext().getSessionMap().get("usuarioLogado") != null){		
			if(getLoginControle() != null){
				getLoginControle().abrirHomeCandidato();
			}
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
		}		
	}
	
	
	
	
	private Integer quantidadeCartao;
//	private List<ContaReceberVO> contaReceberVOs;
	private Boolean desativarBotaoCartaoCredito;
	
	

	public Integer getQuantidadeCartao() {
		if(quantidadeCartao == null) {
			quantidadeCartao = 0;
		}
		return quantidadeCartao;
	}

	public void setQuantidadeCartao(Integer quantidadeCartao) {
		this.quantidadeCartao = quantidadeCartao;
	}
	
	

	

	public Boolean getDesativarBotaoCartaoCredito() {
		if(desativarBotaoCartaoCredito == null) {
			desativarBotaoCartaoCredito = false;
		}
		return desativarBotaoCartaoCredito;
	}

	public void setDesativarBotaoCartaoCredito(Boolean desativarBotaoCartaoCredito) {
		this.desativarBotaoCartaoCredito = desativarBotaoCartaoCredito;
	}
	
	
	
	
	
	
	
	private Boolean permitirRecebimentoCartaoCreditoOnline;
//	private ComprovanteRecebimentoRelControle comprovanteRecebimentoRelControle;

	public Boolean getPermitirRecebimentoCartaoCreditoOnline() {
		if(permitirRecebimentoCartaoCreditoOnline == null) {
			permitirRecebimentoCartaoCreditoOnline = false;
		}
		return permitirRecebimentoCartaoCreditoOnline;
	}

	public void setPermitirRecebimentoCartaoCreditoOnline(Boolean permitirRecebimentoCartaoCreditoOnline) {
		this.permitirRecebimentoCartaoCreditoOnline = permitirRecebimentoCartaoCreditoOnline;
	}

	
	
	private Boolean matriculaConfirmada;
	private Boolean pagamentoRealizadoComSucesso;

	public Boolean getMatriculaConfirmada() {
		if(matriculaConfirmada == null) {
			matriculaConfirmada = false;
		}
		return matriculaConfirmada;
	}

	public void setMatriculaConfirmada(Boolean matriculaConfirmada) {
		this.matriculaConfirmada = matriculaConfirmada;
	}

	public Boolean getPagamentoRealizadoComSucesso() {
		if(pagamentoRealizadoComSucesso == null) {
			pagamentoRealizadoComSucesso = false;
		}
		return pagamentoRealizadoComSucesso;
	}

	public void setPagamentoRealizadoComSucesso(Boolean pagamentoRealizadoComSucesso) {
		this.pagamentoRealizadoComSucesso = pagamentoRealizadoComSucesso;
	}
	

	

	private String modalConfirmacaoPagamento;

	public String getModalConfirmacaoPagamento() {
		if(modalConfirmacaoPagamento == null) {
			modalConfirmacaoPagamento = "";
		}
		return modalConfirmacaoPagamento;
	}

	public void setModalConfirmacaoPagamento(String modalConfirmacaoPagamento) {
		this.modalConfirmacaoPagamento = modalConfirmacaoPagamento;
	}
	
	
	private String modalPagamentoOnline;

	public String getModalPagamentoOnline() {
		if(modalPagamentoOnline == null) {
			modalPagamentoOnline = "";
		}
		return modalPagamentoOnline;
	}

	public void setModalPagamentoOnline(String modalPagamentoOnline) {
		this.modalPagamentoOnline = modalPagamentoOnline;
	}
	
	

	public Boolean getOcultarMedia() {
		if (ocultarMedia == null) {
			ocultarMedia = Boolean.FALSE;
		}
		return ocultarMedia;
	}

	public void setOcultarMedia(Boolean ocultarMedia) {
		this.ocultarMedia = ocultarMedia;
	}

	public Boolean getOcultarClassificacao() {
		if (ocultarClassificacao == null) {
			ocultarClassificacao = Boolean.FALSE;
		}
		return ocultarClassificacao;
	}

	public void setOcultarClassificacao(Boolean ocultarClassificacao) {
		this.ocultarClassificacao = ocultarClassificacao;
	}

	public Boolean getOcultarChamadaCandidato() {
		if (ocultarChamadaCandidato == null) {
			ocultarChamadaCandidato = Boolean.FALSE;
		}
		return ocultarChamadaCandidato;
	}
	
	public void setOcultarChamadaCandidato(Boolean ocultarChamadaCandidato) {
		this.ocultarChamadaCandidato = ocultarChamadaCandidato;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 * 23/02/2016 15:12
	 */
	
	/**
	 * @author Victor Hugo de Paula Costa 18/03/2016 08:29
	 */
	
	
	
	
	
	


	public ConfiguracaoCandidatoProcessoSeletivoVO getConfiguracaoCandidatoProcessoSeletivoVO() {
		if(configuracaoCandidatoProcessoSeletivoVO == null) {
			configuracaoCandidatoProcessoSeletivoVO = new ConfiguracaoCandidatoProcessoSeletivoVO();
		}
		return configuracaoCandidatoProcessoSeletivoVO;
	}

	public void setConfiguracaoCandidatoProcessoSeletivoVO(
			ConfiguracaoCandidatoProcessoSeletivoVO configuracaoCandidatoProcessoSeletivoVO) {
		this.configuracaoCandidatoProcessoSeletivoVO = configuracaoCandidatoProcessoSeletivoVO;
	}
	
	
	synchronized public void realizarImpressaoBoleto() {
		try {
			context().getExternalContext().dispatch("/BoletoBancarioSV?codigoContaReceber=" + getInscricaoVO().getContaReceber() + "&titulo=inscricao");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
	

	public String getLabelArquivosAnexos() {
		if (labelArquivosAnexos == null) {
			labelArquivosAnexos = "";
		}
		return labelArquivosAnexos;
	}

	public void setLabelArquivosAnexos(String labelArquivosAnexos) {
		this.labelArquivosAnexos = labelArquivosAnexos;
	}

	
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getFile() != null && uploadEvent.getFile().getSize() > 15360000) {
				setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
				setMsgErroUpload("Prezado usuário, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
			} else {
				setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
				getArquivoVO().setCpfAlunoDocumentacao(pessoaVO.getCPF());
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP, getUsuarioLogado());
				getArquivoVO().setDescricao(uploadEvent.getFile().getFileName().substring(0, uploadEvent.getFile().getFileName().lastIndexOf(".")));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
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
	
	public void realizarDownloadArquivo() {
		try {			
			if(getInscricaoVO().getArquivoVO() == null) {
				return;
			}
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("file", getInscricaoVO().getArquivoVO());
			request.getSession().setAttribute("nomeArquivo", getInscricaoVO().getArquivoVO());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getLabelArquivosAnexados() {
		labelArquivosAnexos = "Arquivos Anexados ("+ String.valueOf(getInscricaoVO().getListaArquivosAnexo().size() + ")");
		return labelArquivosAnexos;
	}
	
	public void realizarDownloadAnexoLista() {
		context().getExternalContext().getSessionMap().put("arquivoVO", (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoAnexo"));
	}
	
	public void removerArquivoAnexo() {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoAnexo");
		int index = 0;
		for (ArquivoVO arqExcluido : getInscricaoVO().getListaArquivosAnexo()) {
			if (arquivoVO.getDescricao().equals(arqExcluido.getDescricao())) {
				getInscricaoVO().getListaArquivosAnexo().remove(index);
				return;
			}
			index++;
		}
	}
	

	public Boolean getArquivoAnexado() {
		if (arquivoAnexado == null) {
			arquivoAnexado = Boolean.FALSE;
		}
		return arquivoAnexado;
	}

	public void setArquivoAnexado(Boolean arquivoAnexado) {
		this.arquivoAnexado = arquivoAnexado;
	}
	
	public List getListaTipoSelecao() {
		if (listaTipoSelecao == null) {
			listaTipoSelecao = new ArrayList(0);
		}
		return listaTipoSelecao;
	}

	public void setListaTipoSelecao(List listaTipoSelecao) {
		this.listaTipoSelecao = listaTipoSelecao;
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
	
	

	public void setTipoCartao(TipoCartaoOperadoraCartaoEnum tipoCartao) {
		this.tipoCartao = tipoCartao;
	}
	
	

	public void setarNomeBatismo() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNomeBatismo())) {
			getPessoaVO().setNomeBatismo(getPessoaVO().getNome());
		}
	}

	public void setarNomeSocial() {
		if(!Uteis.isAtributoPreenchido(getPessoaVO().getNome())) {
			getPessoaVO().setNome(getPessoaVO().getNomeBatismo());
		}
	}

	public Integer getItemProcessoSeletivoMaiorDataProva() {
		if (itemProcessoSeletivoMaiorDataProva == null) {
			itemProcessoSeletivoMaiorDataProva = 0;
		}
		return itemProcessoSeletivoMaiorDataProva;
	}

	public void setItemProcessoSeletivoMaiorDataProva(Integer itemProcessoSeletivoMaiorDataProva) {
		this.itemProcessoSeletivoMaiorDataProva = itemProcessoSeletivoMaiorDataProva;
	}
	
	
	
}
