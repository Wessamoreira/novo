
package controle.arquitetura;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UICommand;
import javax.faces.component.UIPanel;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

import io.sentry.Sentry;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.Base64;

import appletImpressaoMatricial.LinhaImpressao;
import controle.academico.VisaoAlunoControle;
import controle.academico.VisaoCoordenadorControle;
import controle.academico.VisaoProfessorControle;
import controle.processosel.VisaoCandidatoControle;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConfiguracaoLayoutAtaResultadosFinaisVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoDestinatarioComunicadoInternaEnum;
import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.MapaControleAtividadesUsuarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TratamentoErroVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.financeiro.enumerador.FormaPadraoDataBaseCartaoRecorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.interfaces.arquitetura.SuperVOSelecionadoInterface;
import org.springframework.jdbc.BadSqlGrammarException;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.arquitetura.InfoWSVO;
import webservice.servicos.AutenticadorWS;

public abstract class SuperControle implements Serializable {

	private static final long serialVersionUID = -3770765615126758181L;
	protected static final String URL_AUTENTICACAO_1 = "https://cloud.otimize-ti.com.br/Otimize/webservice/sei/autenticacao";
	protected static final String URL_AUTENTICACAO_2 = "https://c2.otimize-ti.com.br/Otimize/webservice/sei/autenticacao";

	public enum MSG_TELA {
		msg_entre_prmconsulta,
		msg_entre_dados,
		msg_dados_editar,
		msg_dados_selecionados,
		msg_dados_excluidos,
		msg_dados_gravados,
		msg_dados_adicionados,
		msg_dados_consultados,
		msg_dados_atualizados,
		msg_dados_operacao,
		msg_erro ,
		msg_relatorio_ok,
		msg_dados_Enviados;	
	}	
	protected List listaConsulta;
	protected List listaConsultaRequerimento;
	protected String campoConsultaRequerimento;
	protected String valorConsultaRequerimento;
	protected String tipoRequerimento;
	protected ControleConsulta controleConsulta;
	protected String mensagem = null;
	private String mensagemDetalhada = null;
	private String mensagemID = null;
	protected UICommand apresentarPrimeiro;
	protected UICommand apresentarUltimo;
	protected UICommand apresentarAnterior;
	protected UICommand apresentarPosterior;
	private UIPanel apresentarLinha;
	private String paginaAtualDeTodas = "0/0";
	private Integer page = 1;
	private UsuarioVO usuario = null;
	private String versaoSistema = "0.0";
	private Boolean validarCpf;
	private Integer tamanhoModal;
	private ArquivoHelper arquivoHelper;
	private String login;
	private String loginAnterior;
	private String senha;
	private String senhaAnterior;
	private String diretorioPastaWeb;
	private Integer tam;
	private Integer width;
	private Integer heigth;
	protected String tagAppletImpressaoMatricial;
	private FraseInspiracaoVO fraseInspiracaoVO;
	private Boolean sucesso;
	private String iconeMensagem;
	protected List listaMensagemErro;
	// Atributos para upload de fotos
	private String caminhoFotoUsuario;
	private String caminhoFotoUsuario2;
	private Float x;
	private Float y;
	private Float largura;  
	private Float altura;
	private Float xcropVerso;
	private Float ycropVerso;
	private Float larguraVerso;
	private Float alturaVerso;
	private Boolean removerFoto = false;
	Boolean exibirBotao = false;
	private Boolean exibirUpload = true;
	private boolean usuarioLogadoOtimize = false;
	private String oncompleteModal = "RichFaces.$('panelImagem').hide();";
	private static FacadeFactory facadeFactory;
	// ==========================Paginador
	// Otimizado=====================================
	private DataModelo controleConsultaOtimizado;
	// ==========================Paginador
	// Otimizado=====================================
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
	private List<TipoRequerimentoVO> tipoReqVOs;
	private Boolean filtrarUnidadeEnsino;
	private Boolean filtrarTipoReq;
	private Boolean marcarTodasUnidadeEnsino;
	private Boolean marcarTodosTipoReq;
	private List<UnidadeEnsinoVO> listaConsultarUnidadeEnsino;
	private Boolean marcarTodosCursos;
	private Boolean marcarTodosTurnos;
	private List<CursoVO> cursoVOs;
	private List<TurnoVO> turnoVOs;
	private Boolean marcarTodosCentroResultado;
	private Boolean acessoRestrito; 
	private String cursosApresentar;
	private String unidadeEnsinosApresentar;
	private String formaPagamentosApresentar;
	private String turnosApresentar;
	private String configuracaoEditorBasica;
	private String configuracaoEditorSomenteLeitura;
	private String configuracaoEditorAvancada;
	private String configuracaoEditorUpload;
	private Boolean apresentarQRCODELoginAPP;
	private String idControlador;
	private Boolean marcarTodosOperadoraCartaos;
	private List<OperadoraCartaoVO> operadoraCartaoVOs;
	private String operadoraCartaosApresentar;

	private List<FormaPagamentoVO> formaPagamentoVOs;
	private Boolean filtrarFormaPagamento;
	private Boolean marcarTodasFormaPagamento;
	private Boolean marcarTodosTipoOrigemContaPagar = false;
	
	private String usernameLiberacaoBloqueioPorFechamentoMes;
	private String senhaLiberacaoBloqueioPorFechamentoMes;
	private DataModelo controleConsultaTurma;
	private String tipoLayoutHistorico;
	private String tipoLayoutAtaResultadosFinais;
	private String caminhoImagemPadraoTopo;
    private String caminhoImagemPadraoRodape;
    private ComunicacaoInternaVO comunicacaoInternaVO;
    private String mensagemEnvioEmail;
    private Boolean abrirModalEnvioMensagem;
    private Integer codigoAvaliacaoNaoRespondido;    
    private String controleAba;
    private String loginPearson;
    private String tokenPearson;
    private String urlPearson;

    private String longitude;
    private String latitude;
    
    private ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum;
    private ControleConsultaCidade controleConsultaCidade;
    
    
    public void isVerificarUsuarioLogadoOtimize() {
    	setUsuarioLogadoOtimize(getUsuarioLogado().getUsername().equalsIgnoreCase("otimize-ti"));
	}
    
	public boolean isUsuarioLogadoOtimize() {
		return usuarioLogadoOtimize;
	}

	public void setUsuarioLogadoOtimize(boolean usuarioLogadoOtimize) {
		this.usuarioLogadoOtimize = usuarioLogadoOtimize;
	}

	public String getUsernameLiberacaoBloqueioPorFechamentoMes() {
		if (usernameLiberacaoBloqueioPorFechamentoMes == null) {
			usernameLiberacaoBloqueioPorFechamentoMes = "";
		}
		return usernameLiberacaoBloqueioPorFechamentoMes;
	}

	public void setUsernameLiberacaoBloqueioPorFechamentoMes(String usernameLiberacaoBloqueioPorFechamentoMes) {
		this.usernameLiberacaoBloqueioPorFechamentoMes = usernameLiberacaoBloqueioPorFechamentoMes;
	}

	public String getSenhaLiberacaoBloqueioPorFechamentoMes() {
		if (senhaLiberacaoBloqueioPorFechamentoMes == null) {
			senhaLiberacaoBloqueioPorFechamentoMes = "";
		}
		return senhaLiberacaoBloqueioPorFechamentoMes;
	}

	public void setSenhaLiberacaoBloqueioPorFechamentoMes(String senhaLiberacaoBloqueioPorFechamentoMes) {
		this.senhaLiberacaoBloqueioPorFechamentoMes = senhaLiberacaoBloqueioPorFechamentoMes;
	}
	
	
	//
	@Autowired
	public void setFacadeFactory(FacadeFactory facadeFactory) {
		SuperControle.facadeFactory = facadeFactory;
	}

	public static FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}

	public SuperControle() {
//		autenticarPermissaoAcessoTela(getNomeTelaAtual());
		setTamanhoModal(450);
		inicializarVersaoSistema();
	}

	@PostConstruct
	public void realizarAutenticacaoAcessoTelaUsuario(){
		setAcessoRestrito(!autenticarPermissaoAcessoTela(getNomeTelaAtual()));
	}
	
	public boolean autenticarPermissaoAcessoTela(String nomeTela) {
		if(Uteis.isAtributoPreenchido(getUsuarioLogado())){
			if(nomeTela.contains("/")){
				nomeTela = nomeTela.substring(nomeTela.lastIndexOf("/")+1,nomeTela.length());
			}
			if(nomeTela.contains("?")){
				nomeTela = nomeTela.substring(0, nomeTela.indexOf("?"));
			}
			List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> permissoes = PerfilAcessoModuloEnum.getPermissaoPorTelaAmbiente(nomeTela, getUsuarioLogado().getTipoVisaoAcesso());
			if(permissoes.isEmpty()){
				return true;
			}
			for(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoEnum: permissoes){
				Object possuiAcesso = UtilReflexao.invocarMetodoGet(getLoginControle().getPermissaoAcessoMenuVO(), ((PerfilAcessoPermissaoEnumInterface)permissaoEnum).getValor());
				if(possuiAcesso instanceof Boolean && (Boolean) possuiAcesso){
					return true;
				}				
			}			
		}
		return false;
	}

	public UsuarioVO getUsuarioLogado() {
	    if(context() == null){
		return new UsuarioVO();
	    }
		UsuarioVO user = (UsuarioVO) context().getExternalContext().getSessionMap().get("usuarioLogado");
		if(user == null){
			user = new UsuarioVO();
		}
		return user;
	}
	
	
	
	public UsuarioVO getUsuarioLogadoClone() {
		UsuarioVO user  = new UsuarioVO();
		if (context() == null) {
			return new UsuarioVO();
		}		
		try {
		 user = (UsuarioVO) context().getExternalContext().getSessionMap().get("usuarioLogado");
		 if (Uteis.isAtributoPreenchido(user)) {
			 user.clone();
		}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return user ;		
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(Integer codigoUnidadeEnsino,TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			try {
				ConfiguracaoGEDVO configGEDVO = null;
				if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino) && Uteis.isAtributoPreenchido(tipoOrigemDocumentoAssinadoEnum)){
					configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(codigoUnidadeEnsino, getUsuarioLogado());
				}
				else {
					return lista;
				}
				ConfiguracaoGedOrigemVO configuracaoGedOrigemVO = configGEDVO.getConfiguracaoGedOrigemVO(tipoOrigemDocumentoAssinadoEnum);
				
				if(Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) && configuracaoGedOrigemVO.getAssinarDocumento()){
					setProvedorDeAssinaturaEnum(configuracaoGedOrigemVO.getProvedorAssinaturaPadraoEnum());
					lista.add(new SelectItem(ProvedorDeAssinaturaEnum.SEI,internacionalizarEnum(ProvedorDeAssinaturaEnum.SEI)));
					if(configGEDVO.getHabilitarIntegracaoCertisign()) {
						lista.add(new SelectItem(ProvedorDeAssinaturaEnum.CERTISIGN, internacionalizarEnum(ProvedorDeAssinaturaEnum.CERTISIGN)));
					}
					if(configGEDVO.getHabilitarIntegracaoTechCert()) {
						lista.add(new SelectItem(ProvedorDeAssinaturaEnum.TECHCERT, internacionalizarEnum(ProvedorDeAssinaturaEnum.TECHCERT)));
					}
					if(configGEDVO.getHabilitarIntegracaoImprensaOficial()) {
						lista.add(new SelectItem(ProvedorDeAssinaturaEnum.IMPRENSAOFICIAL, internacionalizarEnum(ProvedorDeAssinaturaEnum.IMPRENSAOFICIAL)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return lista;
		
	}
	

	public void imprimirModoMatricial(List<LinhaImpressao> linhaImpressaos) throws IOException, Exception {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.setAttribute("lista", linhaImpressaos);
		context().getExternalContext().dispatch("/ImpressaoMatricialSV");
		setTagAppletImpressaoMatricial("<applet archive=\"AppletImpressaoMatricial.jar\" code=\"Main.class\" width=\"1\" height=\"1\"> <param name=url value=\"" + getURLAplicacao() + "/ImpressaoMatricialSV\"> </applet>");
	}
	
	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaEnum() {
		return provedorDeAssinaturaEnum;
	}

	public void setProvedorDeAssinaturaEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaEnum) {
		this.provedorDeAssinaturaEnum = provedorDeAssinaturaEnum;
	}

	public String getTagAppletImpressaoMatricial() {
		if (tagAppletImpressaoMatricial == null) {
			tagAppletImpressaoMatricial = "";
		}
		return tagAppletImpressaoMatricial;
	}

	public void setTagAppletImpressaoMatricial(String tagAppletImpressaoMatricial) {
		this.tagAppletImpressaoMatricial = tagAppletImpressaoMatricial;
	}

	public SuperControle getControlador(String nomeControlador) {
		return (SuperControle) UtilNavegacao.getControlador(nomeControlador);
	}

	public List obterListaDataComemorativaDataAtual() throws Exception {		
		return getAplicacaoControle().obterListaDataComemorativaDataAtual(getUsuarioLogado());
	}

	public AplicacaoControle getControladorAplicacaoControle(String nomeControlador) {
		return (AplicacaoControle) UtilNavegacao.getControlador(nomeControlador);
	}

	public AplicacaoControle getAplicacaoControle() {
		return (AplicacaoControle) SpringUtil.getApplicationContext().getBean(AplicacaoControle.class);
	}

	public String navegarPara(String controlador, String metodoASerExecutado, String fromOutcome, Object... parametroDoMetodo) throws Exception {
		return UtilNavegacao.navegarPara(controlador, metodoASerExecutado, fromOutcome, parametroDoMetodo);
	}

	public void executarMetodoControle(String controlador, String metodoASerExecutado, Object... parametroDoMetodo) {
		UtilNavegacao.executarMetodoControle(controlador, metodoASerExecutado, parametroDoMetodo);
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public void limparMensagem() {
		setSucesso(sucesso);
		getListaMensagemErro().clear();
		if(context() != null && context().getMessages() != null) {
			Iterator<FacesMessage> it = context().getMessages();
			while (it.hasNext()) {
				it.next();
				it.remove();
			}
		}
		setMensagemID("msg_entre_dados", Uteis.ALERTA);

	}
	
	public void inicializarMensagemVazia() {
		this.getListaMensagemErro().clear();
		setMensagemID("", "");
		setMensagem("");
		setIconeMensagem("");
		if(context() != null && context().getMessages() != null) {
			Iterator<FacesMessage> it = context().getMessages();
			while (it.hasNext()) {
				it.next();
				it.remove();
			}
		}
		setSucesso(false);
	}

	protected void limparRecursosMemoria() {
		try {
			if (this.getListaConsulta() != null) {
				this.getListaConsulta().clear();
			}
			this.mensagem = null;
			this.mensagemDetalhada = null;
			this.mensagemID = null;
			this.apresentarPrimeiro = null;
			this.apresentarUltimo = null;
			this.apresentarAnterior = null;
			this.apresentarPosterior = null;
			this.apresentarLinha = null;
			this.paginaAtualDeTodas = "0/0";
			this.usuario = null;
			// System.out.println("BACKING....: " +
			// this.getClass().getSimpleName() +
			// " RECURSOS LIBERADOS DA MEMÓRIA.");
		} catch (Exception e) {
		}
	}

	public void liberarBackingBeanMemoria() {
		try {
			this.limparRecursosMemoria();
			String nomeBackingBean = this.getClass().getSimpleName();
			removerManagedBean(nomeBackingBean);
			removerControleMemoriaFlashTela(nomeBackingBean);
			System.gc();
			// System.out.println("BACKING....: " +
			// this.getClass().getSimpleName() + " REMOVIDO DA MEMÓRIA.");
		} catch (Exception e) {
			// System.out.println("Nao conseguimos remover o Backing da Memória
			// ("
			// + e.getMessage() + ") " + this.getClass().getSimpleName());
		}
	}

	public void liberarBackingBeanMemoria(String backingBean) {
		try {
			this.limparRecursosMemoria();
			String nomeBackingBean = backingBean;
			removerManagedBean(nomeBackingBean);
			System.gc();
			// System.out.println("BACKING....: " +
			// this.getClass().getSimpleName() + " REMOVIDO DA MEMÓRIA.");
		} catch (Exception e) {
			// System.out.println("Nao conseguimos remover o Backing da Memória
			// ("
			// + e.getMessage() + ") " + this.getClass().getSimpleName());
		}
	}

	protected void removerManagedBean(String nomeManagedBean) {
		context().getExternalContext().getSessionMap().remove(nomeManagedBean);
	}

	protected FacesContext context() {
		return (FacesContext.getCurrentInstance());
	}

	protected HttpServletRequest request() {
		return (HttpServletRequest) this.context().getExternalContext().getRequest();
	}

	protected HttpServletResponse response() {
		return (HttpServletResponse) this.context().getExternalContext().getResponse();
	}

	// public UnidadeEnsinoVO getUnidadeEnsinoLogado() {
	// LoginControle loginControle = (LoginControle)
	// context().getExternalContext().getSessionMap().get("LoginControle");
	// UnidadeEnsinoVO unidadeEnsinoVO =
	// loginControle.getUsuario().getUnidadeEnsinoLogado();
	// return unidadeEnsinoVO;
	// }
	public OperadoraCartaoVO operadoraCartaoVO;
	public UnidadeEnsinoVO unidadeEnsinoVO;

	public UnidadeEnsinoVO getUnidadeEnsinoLogado() {
		// if (unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo().intValue()
		// == 0) {
		// LoginControle loginControle = (LoginControle)
		// context().getExternalContext().getSessionMap().get("LoginControle");
		// UnidadeEnsinoVO unidadeEnsinoVO2 =
		// loginControle.getUsuario().getUnidadeEnsinoLogado();
		UnidadeEnsinoVO unidadeEnsinoVO2 = getUsuarioLogado().getUnidadeEnsinoLogado();
		if (unidadeEnsinoVO2 != null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
			unidadeEnsinoVO = unidadeEnsinoVO2;
		}
		// }
		return unidadeEnsinoVO;
	}
	
	public UnidadeEnsinoVO getUnidadeEnsinoLogadoClone() {		
		try {
			unidadeEnsinoVO = (UnidadeEnsinoVO)getUsuarioLogado().getUnidadeEnsinoLogado().clone();
		} catch (CloneNotSupportedException e) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public String alterarLingua_En() {
		Locale local = new Locale("en", "US");
		context().getApplication().setDefaultLocale(local);
		context().getViewRoot().setLocale(local);
		return "alterarLingua";
	}

	public String alterarLingua_Pt() {
		Locale local = new Locale("pt", "BR");
		context().getApplication().setDefaultLocale(local);
		context().getViewRoot().setLocale(local);
		return "alterarLingua";
	}

	protected String consultar() throws Exception {
		if (getControleConsulta() == null) {
			setControleConsulta(new ControleConsulta());
		}
		getControleConsulta().definirProximaPaginaApresentar(getControleConsulta().getPaginaAtual());
		return "consultar";
	}

	public void definirVisibilidadeLinksNavegacao(int paginaAtual, int nrTotalPaginas) {
		setPaginaAtualDeTodas(paginaAtual + "/" + nrTotalPaginas);
		if (paginaAtual == 1 && nrTotalPaginas > 1) {
			apresentarPrimeiro.setRendered(false);
			apresentarAnterior.setRendered(false);
			apresentarPosterior.setRendered(true);
			apresentarUltimo.setRendered(true);
		} else if (paginaAtual == 1 && nrTotalPaginas == 1) {
			apresentarPrimeiro.setRendered(false);
			apresentarAnterior.setRendered(false);
			apresentarPosterior.setRendered(false);
			apresentarUltimo.setRendered(false);
		} else if (paginaAtual > 1 && paginaAtual < nrTotalPaginas) {
			apresentarPrimeiro.setRendered(true);
			apresentarAnterior.setRendered(true);
			apresentarPosterior.setRendered(true);
			apresentarUltimo.setRendered(true);
		} else if (paginaAtual == nrTotalPaginas && paginaAtual != 0) {
			apresentarPrimeiro.setRendered(true);
			apresentarAnterior.setRendered(true);
			apresentarPosterior.setRendered(false);
			apresentarUltimo.setRendered(false);
		} else if (nrTotalPaginas == 0) {
			setPaginaAtualDeTodas("0/0");
			apresentarPrimeiro.setRendered(false);
			apresentarAnterior.setRendered(false);
			apresentarPosterior.setRendered(false);
			apresentarUltimo.setRendered(false);
		}
	}

	public String getCaminhoBase() throws Exception {
		String caminhoBaseAplicacao = Class.forName(SuperControle.class.getName()).getResource(File.separator).getPath();
		caminhoBaseAplicacao = URLDecoder.decode(caminhoBaseAplicacao, "UTF-8");
		caminhoBaseAplicacao = caminhoBaseAplicacao.replaceAll("%20", " ");
		int x = caminhoBaseAplicacao.lastIndexOf("/");
		x = caminhoBaseAplicacao.lastIndexOf("/", x - 1);
		x = caminhoBaseAplicacao.lastIndexOf("/", x - 1);
		x = caminhoBaseAplicacao.lastIndexOf("/", x - 1);
		x = caminhoBaseAplicacao.lastIndexOf("/", x - 1);
		caminhoBaseAplicacao = caminhoBaseAplicacao.substring(0, x);
		return caminhoBaseAplicacao;
	}

	public String obterCaminhoWebFotos() throws Exception {
		return getCaminhoPastaWeb() +File.separator + "resources"+File.separator + "imagem";
	}

	public String obterCaminhoWebImagem() throws Exception {
		return getCaminhoPastaWeb() +File.separator + "resources"+ File.separator + "imagens";
	}

	public String getCaminhoPastaArquivosCenso() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "arquivos";
	}

	public String getCaminhoPastaArquivo() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "arquivo";
	}

	public String getCaminhoPastaArquivosCobranca() {
		return getCaminhoPastaWeb() + File.separator + "arquivos";
	}

	public String getCaminhoPastaWeb() {
		if (diretorioPastaWeb == null || diretorioPastaWeb.equals("")) {
			if(context() == null) {
				diretorioPastaWeb = getCaminhoPastaWebForaContext();
			}else {
				ServletContext servletContext = (ServletContext) this.context().getExternalContext().getContext();
				diretorioPastaWeb = servletContext.getRealPath("");
			}
		}
		return diretorioPastaWeb;
	}
	
	public String getCaminhoPastaWebForaContext() {
		return this.getClass().getResource("/").toString().replace("file:", "").replace("WEB-INF/classes/", "");
	}
	
	public String getCaminhoPastaAplicacaoForaContext() {
		return this.getClass().getResource("/").toString().replace("file:", "").replace("WEB-INF/classes/", "");
	}

	public String getURLAplicacao() throws Exception {
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		StringBuffer urlAplicacao = request.getRequestURL();
		request.getRequestURI();
		String url = urlAplicacao.toString();
		url = url.substring(0, urlAplicacao.lastIndexOf("/"));
		url = url.replace("localhost", getLocalhostIP());
		return url.substring(0, url.indexOf("faces") + 5);
	}
	
	public String getURLAplicacaoAcessoRestrito() throws Exception {		
		return getURLAplicacao()+"/acessoRestrito.xhtml";
	}

	public String getLocalhostIP() {
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			// localHost.getHostName();
			return localHost.getHostAddress();
		} catch (UnknownHostException ex) {
			return "localhost";
		}
	}

	public String getMensagem() {
		if ((getMensagemID() != null) && (!getMensagemID().equals(""))) {
			mensagem = getMensagemInternalizacao(getMensagemID());
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String tratarMensagemErroDetalhada(String mensagemDetalhada) {
		String novaMensagem = mensagemDetalhada;
		// TODO Alberto 15/12/2010 Corrigido para aparecer a mensagem de erro do código somente no console
		//String erroCodigo = "Motivo do Erro: >>>" + mensagemDetalhada;		
		
			novaMensagem = MensagensRetornoErroEnum.getMensagensRetornoErroEnum(mensagemDetalhada);
			TratamentoErroVO tratamentoErroVO = getFacadeFactory().getTratamentoErroFacade()
					.inicializarDadosTratamentoErro(novaMensagem, getUsuarioLogado());
			
		if(context() != null && Uteis.isAtributoPreenchido(novaMensagem)){
			FacesMessage msg = null;
			if (tratamentoErroVO != null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, tratamentoErroVO.getMensagemApresentar());
			} else {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, novaMensagem);
			}
			boolean existeMsg =  false;
			Iterator<FacesMessage> it = context().getMessages();
			while (it.hasNext()) {
				FacesMessage facesMessage = it.next();
				if(facesMessage.getDetail().equals(msg.getDetail())) {
					existeMsg = true;
					break;
				}
			}
			if(!existeMsg) {
				context().addMessage(null, msg);
			}
			context().getExternalContext().getFlash().setKeepMessages(true); 
		}
		if (tratamentoErroVO != null) {
			return tratamentoErroVO.getMensagemApresentar();
		}
		return novaMensagem;
	}

	public String tratarMensagemErroDetalhada(String mensagemDetalhada, String tipoIcone) {
		String novaMensagem = mensagemDetalhada;
		TratamentoErroVO tratamentoErroVO = null;
		if (Uteis.isAtributoPreenchido(novaMensagem)
				&& novaMensagem.contains("viola restrição de chave estrangeira") && novaMensagem.contains("ainda é referenciada pela tabela \"")) {
			String tabela = novaMensagem.substring(novaMensagem.indexOf("ainda é referenciada pela tabela \""), novaMensagem.length());
			if (tabela.contains("\"")) {
				tabela = tabela.substring(tabela.indexOf("\"") + 1);
				tabela = tabela.substring(0, tabela.indexOf("\""));
				novaMensagem = "Este registro ainda é referenciada pela tabela \"" + tabela + "\".";
			}
		} else if (novaMensagem != null && (novaMensagem.contains("\"unq_contareceber_codorigem_tipoorigem_parcela_pessoa_matricu\"") || novaMensagem.contains("\"unq_contareceber_codorigem_tipoorigem_parcela_parceiro_matricu\""))) {
			novaMensagem = MensagensRetornoErroEnum.getMensagensRetornoErroEnum(mensagemDetalhada);
			Map<String, String> valoresChave = UteisTexto.extrairValoresChave(mensagemDetalhada);
			for (Map.Entry<String, String> entry : valoresChave.entrySet()) {
				novaMensagem = novaMensagem.concat("-").concat(entry.getKey()).concat(Constantes.DOISPONTO).concat(entry.getValue());
			}
			tratamentoErroVO = getFacadeFactory().getTratamentoErroFacade().inicializarDadosTratamentoErro(novaMensagem, getUsuarioLogado());
		} else {
			novaMensagem = MensagensRetornoErroEnum.getMensagensRetornoErroEnum(mensagemDetalhada);
			tratamentoErroVO = getFacadeFactory().getTratamentoErroFacade().inicializarDadosTratamentoErro(novaMensagem, getUsuarioLogado());
		}
		if(context() != null && Uteis.isAtributoPreenchido(novaMensagem)){
			FacesMessage msg = null;
			if (tratamentoErroVO != null) {
				msg = new FacesMessage(tipoIcone.equals(Uteis.SUCESSO) ? FacesMessage.SEVERITY_INFO : tipoIcone.equals(Uteis.ALERTA) ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_ERROR, null, tratamentoErroVO.getMensagemApresentar());
			} else {
				msg = new FacesMessage(tipoIcone.equals(Uteis.SUCESSO) ? FacesMessage.SEVERITY_INFO : tipoIcone.equals(Uteis.ALERTA) ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_ERROR, null, novaMensagem);
			}
			boolean existeMsg =  false;
			Iterator<FacesMessage> it = context().getMessages();
			while (it.hasNext()) {
				FacesMessage facesMessage = it.next();
				if(facesMessage.getDetail().equals(msg.getDetail())) {
					existeMsg = true;
					break;
				}
			}
			if(!existeMsg) {
				context().addMessage(null, msg);
			}

			context().getExternalContext().getFlash().setKeepMessages(true);
		}
		if (tratamentoErroVO != null) {
			return tratamentoErroVO.getMensagemApresentar();
		}
		return novaMensagem;
	}


	public void setConsistirExceptionMensagemDetalhada(String mensagemID, ConsistirException consistir, String tipoIcone) {
		this.mensagemID = mensagemID;
		setMensagemID(mensagemID, tipoIcone);
		if ((consistir.getListaMensagemErro() == null || consistir.getListaMensagemErro().isEmpty()) && consistir.getMessage() != null && !consistir.getMessage().trim().isEmpty()) {
			consistir.setListaMensagemErro(new ArrayList<String>(0));
			consistir.adicionarListaMensagemErro(tratarMensagemErroDetalhada(consistir.getMessage()));
		}
		for(String msg : (List<String>)consistir.getListaMensagemErro()){
			tratarMensagemErroDetalhada(msg);
		}
		this.setListaMensagemErro(consistir.getListaMensagemErro());

	}
	
	protected void setMensagemResponseJson(HttpResponse<JsonNode> response) throws Exception {
		InfoWSVO rep = new Gson().fromJson(response.getBody().toString(), InfoWSVO.class);
		if (response.getStatus() != (HttpStatus.OK.value())) {				
			tratarMensagemErroWebService(rep, String.valueOf(response.getStatus()), response.getBody().toString());
		}else {
			setMensagem(rep.getMensagem());	
		}
	}
	
	protected void setMensagemResponse(HttpResponse<String> response) throws Exception {
		InfoWSVO rep = new Gson().fromJson(response.getBody(), InfoWSVO.class);
		if (response.getStatus() != (HttpStatus.OK.value())) {				
			tratarMensagemErroWebService(rep, String.valueOf(response.getStatus()), response.getBody());
		}else {
			setMensagem(rep.getMensagem());	
		}
	}
	
	protected void tratarMensagemErroWebService(InfoWSVO resp, String status,  String mensagemRetorno) throws Exception {
		String msg = null;
		if(resp != null && resp.getCodigo() != 0) {
			msg = resp.getMensagem() + " - " + resp.getMensagemCampos();
		}else if(resp != null && resp.getStatus() != 0) {
			msg = resp.getMessage();
		}else {
			msg = mensagemRetorno;
		}
		if (status.contains("400")) {
			throw new Exception("A requisição é inválida, em geral conteúdo malformado. " + msg);
		}
		if (status.contains("401")) {
			throw new Exception("O usuário e senha ou token de acesso são inválidos. " + msg);
		}
		if (status.contains("403")) {
			throw new Exception("O acesso à API está bloqueado ou o usuário está bloqueado. " + msg);
		}
		if (status.contains("404")) {
			throw new Exception("O endereço acessado não existe. " + msg);
		}
		if (status.contains("422")) {
			throw new Exception("A Requisição é válida, mas os dados passados não são válidos. " + msg);
		}
		if (status.contains("429")) {
			throw new Exception("O usuário atingiu o limite de requisições. " + msg);
		}
		if (status.contains("500")) {
			throw new Exception("Houve um erro interno do servidor ao processar a requisição. " + msg);
		}
		throw new Exception("Erro Não tratato Web Service : " + msg);
	}

	public List<SelectItem> getTipoConsultaComboRequerimento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Nr. Requerimento"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
		itens.add(new SelectItem("nomePessoa", "Nome Requisitante"));
		itens.add(new SelectItem("cpfPessoa", "CPF Requisitante"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		return itens;
	}	
	

	public List getListaSelectItemSituacaoFinanceiraRequerimento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFinanceiraProtocolos = (Hashtable) Dominios.getSituacaoFinanceiraProtocolo();
		Enumeration keys = situacaoFinanceiraProtocolos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceiraProtocolos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoRequerimento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoProtocolos = (Hashtable) Dominios.getSituacaoProtocolo();
		Enumeration keys = situacaoProtocolos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoProtocolos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public Boolean getSitucaoRequerimento() {
		if (getCampoConsultaRequerimento().equals("situacao")) {
			return true;
		}
		return false;
	}

	public Boolean getSitucaoFinanceiraRequerimento() {
		if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
			return true;
		}
		return false;
	}

	public String getMascaraRequerimento() {
		if (getCampoConsultaRequerimento().equals("data")) {
			return "return mascara(this.form,'formRequerimento:valorConsultaRequerimento','99/99/9999',event);";
		}
		if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
			return "return mascara(this.form,'formRequerimento:valorConsultaRequerimento','999.999.999-99',event);";
		}
		return "";
	}

	public void consultarRequerimento() {
		try {

			if (getValorConsultaRequerimento().equals("")) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_informeUmParametro"));
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaRequerimento().equals("codigo")) {
				int valorInt = Uteis.getValorInteiro(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomeTipoRequerimento")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeTipoRequerimento(getValorConsultaRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacao")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacao(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoFinanceira(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomePessoa")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomePessoa(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeCPFPessoa(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("matriculaMatricula")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatricula(getValorConsultaRequerimento(), getTipoRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}

			setListaConsultaRequerimento(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaRequerimento(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCampoConsultaRequerimento() {
		if (campoConsultaRequerimento == null) {
			campoConsultaRequerimento = "nomePessoa";
		}
		return campoConsultaRequerimento;
	}

	public void setCampoConsultaRequerimento(String campoConsultaRequerimento) {
		this.campoConsultaRequerimento = campoConsultaRequerimento;
	}

	public List getListaConsultaRequerimento() {
		if(listaConsultaRequerimento == null){
			listaConsultaRequerimento = new ArrayList<>();
		}
		return listaConsultaRequerimento;
	}

	public void setListaConsultaRequerimento(List listaConsultaRequerimento) {
		this.listaConsultaRequerimento = listaConsultaRequerimento;
	}

	public String getTipoRequerimento() {
		if (tipoRequerimento == null) {
			tipoRequerimento = TiposRequerimento.CARTEIRINHA_ESTUDANTIL.getValor();
		}
		return tipoRequerimento;
	}

	public void setTipoRequerimento(String tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public String getValorConsultaRequerimento() {
		if (valorConsultaRequerimento == null) {
			valorConsultaRequerimento = "";
		}
		return valorConsultaRequerimento;
	}

	public void setValorConsultaRequerimento(String valorConsultaRequerimento) {
		this.valorConsultaRequerimento = valorConsultaRequerimento;
	}

	public void setMensagemDetalhada(String mensagemID, String mensagemDetalhada) {
		setSucesso(false);
		setMensagemID(mensagemID);
		this.mensagemDetalhada = tratarMensagemErroDetalhada(mensagemDetalhada);
	}

	public void setMensagemDetalhada(String mensagemID, String mensagemDetalhada, Throwable e, UsuarioVO usuarioVO, String informacaoAdicional1, String informacaoAdicional2, Boolean enviarSentry) {
		setSucesso(false);
		setMensagemID(mensagemID);
		this.mensagemDetalhada = tratarMensagemErroDetalhada(mensagemDetalhada, Uteis.ERRO);
		registrarExceptionSentry(e, usuarioVO, informacaoAdicional1, informacaoAdicional2, enviarSentry);
	}

	public void setMensagemDetalhada(String mensagemID, String mensagemDetalhada1, Boolean sucesso) {
		setSucesso(sucesso);
		if (sucesso) {
			setMensagemID("");
		} else {
			setMensagemID(mensagemID);
		}
		this.mensagemDetalhada = tratarMensagemErroDetalhada(mensagemDetalhada1);
	}

	public void setMensagemDetalhada(String mensagemID, String mensagemDetalhada, String tipoIcone) {
		this.mensagemID = mensagemID;
		setMensagemID(mensagemID, tipoIcone);
		this.mensagemDetalhada = tratarMensagemErroDetalhada(mensagemDetalhada);
		if (tipoIcone.equals(Uteis.ERRO)) {
			this.getListaMensagemErro().clear();
			this.getListaMensagemErro().add(this.mensagemDetalhada);
		}	
		
	}

	public void setMensagemDetalhada(String mensagemID, String mensagemDetalhada, String tipoIcone, Throwable e, UsuarioVO usuarioVO, String informacaoAdicional1, String informacaoAdicional2, Boolean enviarSentry) {
		this.mensagemID = mensagemID;
		setMensagemID(mensagemID, tipoIcone);
		this.mensagemDetalhada = tratarMensagemErroDetalhada(mensagemDetalhada, tipoIcone);
		if (tipoIcone.equals(Uteis.ERRO)) {
			this.getListaMensagemErro().clear();
			this.getListaMensagemErro().add(this.mensagemDetalhada);
		}
		if (tipoIcone.equals(Uteis.ERRO ) || mensagemID.equals("msg_erro")) {
			registrarExceptionSentry(e, usuarioVO, informacaoAdicional1, informacaoAdicional2, enviarSentry);
		}
	}

	public void registrarExceptionSentry(Throwable e, UsuarioVO usuarioVO, String informacaoAdicional1, String informacaoAdicional2, Boolean enviarSentry) {
		List<String> listaInformacao = new ArrayList<String>();
		try {
			if (Objects.isNull(getAplicacaoControle())) {
				return;
			}
			if (Uteis.isAtributoPreenchido(informacaoAdicional1)) {
				listaInformacao.add(informacaoAdicional1);
			}
			if (Uteis.isAtributoPreenchido(informacaoAdicional2)) {
				listaInformacao.add(informacaoAdicional2);
			}
			getFacadeFactory().getServicoIntegracaoSentryInterfaceFacade().registrarExceptionSentry(e, listaInformacao, enviarSentry, Uteis.isAtributoPreenchido(getUsuarioLogado()) && Uteis.isAtributoPreenchido(getUsuarioLogado().getUnidadeEnsinoLogado()) ? getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade() : getConfiguracaoGeralPadraoSistema(), usuarioVO);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			listaInformacao.clear();
			listaInformacao = null;
		}
	}

	public String getMensagemDetalhada() {
		return mensagemDetalhada;
	}

	public void setMensagemDetalhada(String mensagemDetalhada) {
		this.mensagemDetalhada = mensagemDetalhada;
	}

	public String getMensagemID() {
		return mensagemID;
	}

	public void setMensagemID(String mensagemID) {
		this.mensagemID = mensagemID;
		this.mensagemDetalhada = "";
		setIconeMensagem("");

		if(mensagemID.equals("msg_dados_erro") || mensagemID.equals("msg_erro")){
			setMensagemID(mensagemID, Uteis.ERRO);			
		}else if(mensagemID.equals("msg_dados_consultados") || mensagemID.equals("msg_entre_dados") ||  mensagemID.equals("msg_entre_prmconsulta") ||  mensagemID.equals("msg_dados_editar") || mensagemID.contentEquals("msg_entre_prmrelatorio")){
			setMensagemID(mensagemID, Uteis.ALERTA);
			
		} else if(mensagemID.equals("msg_dados_gravados") || mensagemID.equals("msg_dados_excluidos")){
			setMensagemID(mensagemID, Uteis.SUCESSO);			
		}
		getListaMensagemErro().clear();
		
	}

	public boolean getApresentarBotaoVerTodos() {
		if (getListaMensagemErro() != null && getListaMensagemErro().size() > 3) {
			return true;
		} else {
			return false;
		}
	}

	public String getTitleBotaoVerTodos() {
		return UteisJSF.internacionalizar("msg_TotalErros") + getListaMensagemErro().size() + UteisJSF.internacionalizar("msg_FechaParenteses");
	}

	public void setMensagemID(String mensagemID, String tipoIcone, boolean isMostraMsgFlutuante) {
		setSucesso(true);
		if (tipoIcone.equals(Uteis.ERRO)) {
			/* setIconeMensagem("/resources/imagens/erro.gif"); */			
			setIconeMensagem("fas fa-times");	
		}
		if (tipoIcone.equals(Uteis.ALERTA)) {
			/* setIconeMensagem("/resources/imagens/alerta.gif"); */
			setIconeMensagem("fas fa-exclamation-triangle");
			getListaMensagemErro().clear();
		}
		if (tipoIcone.equals(Uteis.SUCESSO)) {
			/* setIconeMensagem("/resources/imagens/sucesso.gif"); */
			setIconeMensagem("fas fa-check");
			getListaMensagemErro().clear();
		}
		if(isMostraMsgFlutuante) {
			if(context() != null && Uteis.isAtributoPreenchido(mensagemID)){
				FacesMessage msg = new FacesMessage(tipoIcone.equals(Uteis.SUCESSO) ? FacesMessage.SEVERITY_INFO : tipoIcone.equals(Uteis.ALERTA) ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_ERROR, null, UteisJSF.internacionalizar(mensagemID));
				context().addMessage(null, msg);
				context().getExternalContext().getFlash().setKeepMessages(true); 
			}
		}
		this.mensagemID = mensagemID;
		this.mensagemDetalhada = "";
	}
	
	public void setMensagemID(String mensagemID, String tipoIcone) {
		if(mensagemID.equals("msg_dados_gravados") || mensagemID.equals("msg_dados_excluir") || mensagemID.equals("msg_mensagem_enviada") || mensagemID.equals("msg_AlunoAvaliadoComSucesso")) {
			setMensagemID(mensagemID, tipoIcone, true);
		}else{
			setMensagemID(mensagemID, tipoIcone, false);
		}
	}

	public Boolean getSucesso() {
		if (sucesso == null) {
			sucesso = false;
		}
		return sucesso;
	}
	public void setSucesso(Boolean sucesso) {
		this.sucesso = sucesso;
	}

	public String getIconeMensagem() {
		if (iconeMensagem == null) {
			iconeMensagem = "";
		}
		return iconeMensagem;
	}

	public void setIconeMensagem(String iconeMensagem) {
		this.iconeMensagem = iconeMensagem;
	}

	public List getListaMensagemErro() {
		if (listaMensagemErro == null) {
			listaMensagemErro = new ArrayList(0);
		}
		return listaMensagemErro;
	}

	public void setListaMensagemErro(List listaMensagemErro) {
		this.listaMensagemErro = listaMensagemErro;
	}

	public List getListaConsulta() {
		if (listaConsulta == null) {
			listaConsulta = new ArrayList(0);
		}
		return listaConsulta;
	}

	public void setListaConsulta(List listaConsulta) {
		this.listaConsulta = listaConsulta;
	}

	public ControleConsulta getControleConsulta() {
		if (controleConsulta == null) {
			controleConsulta = new ControleConsulta();
		}
		return controleConsulta;
	}

	public void setControleConsulta(ControleConsulta controleConsulta) {
		this.controleConsulta = controleConsulta;
	}

	public UICommand getApresentarPrimeiro() {
		if(apresentarPrimeiro == null){
			apresentarPrimeiro = new UICommand();
		}
		return apresentarPrimeiro;
	}

	public void setApresentarPrimeiro(UICommand apresentarPrimeiro) {
		this.apresentarPrimeiro = apresentarPrimeiro;
	}

	public UICommand getApresentarUltimo() {
		if(apresentarUltimo == null){
			apresentarUltimo = new UICommand();
		}
		return apresentarUltimo;
	}

	public void setApresentarUltimo(UICommand apresentarUltimo) {
		this.apresentarUltimo = apresentarUltimo;
	}

	public UICommand getApresentarAnterior() {
		if(apresentarAnterior == null){
			apresentarAnterior = new UICommand();
		}
		return apresentarAnterior;
	}

	public void setApresentarAnterior(UICommand apresentarAnterior) {
		this.apresentarAnterior = apresentarAnterior;
	}

	public UICommand getApresentarPosterior() {
		if(apresentarPosterior == null){
			apresentarPosterior = new UICommand();
		}
		return apresentarPosterior;
	}

	public void setApresentarPosterior(UICommand apresentarPosterior) {
		this.apresentarPosterior = apresentarPosterior;
	}

	public String getPaginaAtualDeTodas() {
		return paginaAtualDeTodas;
	}

	public void setPaginaAtualDeTodas(String paginaAtualDeTodas) {
		this.paginaAtualDeTodas = paginaAtualDeTodas;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public UIPanel getApresentarLinha() {
		if(apresentarLinha == null){
			apresentarLinha = new UIPanel();
		}
		return apresentarLinha;
	}

	public void setApresentarLinha(UIPanel apresentarLinha) {
		this.apresentarLinha = apresentarLinha;
	}

	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO aUsuario) {
		usuario = aUsuario;
	}

	public String obterCaminhoWebAplicacao() throws Exception {
		return getCaminhoPastaWeb() + File.separator + "relatorio";
	}

	public boolean getApresentarResultadoConsulta() {
		if (this.getListaConsulta() == null || this.getListaConsulta().size() == 0) {
			return false;
		}
		return true;
	}

	public String getVersaoSistema() {
		return versaoSistema;
	}

	public void setVersaoSistema(String versaoSistema) {
		this.versaoSistema = versaoSistema;
	}

	protected void inicializarVersaoSistema() {
		try {
//			if (context() != null) {
//				ServletContext servletContext = (ServletContext) this.context().getExternalContext().getContext();
//				String versao = servletContext.getInitParameter("versaoSoftware");
//				setVersaoSistema(versao);
//			}
			setVersaoSistema("SEI - " + Uteis.VERSAO_SISTEMA);
		} catch (Exception e) {
		}
	}

	protected void removerTodosManagedBean() {
		Set<String> identificadores = context().getExternalContext().getSessionMap().keySet();
		Iterator i = identificadores.iterator();
		while (i.hasNext()) {
			String identificadorMB = (String) i.next();
			if (identificadorMB.endsWith("Controle")) {
				SuperControle controle = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
				context().getExternalContext().getSessionMap().remove(identificadorMB);
			}
		}
	}

	public Integer getTamanhoModal() {
		return tamanhoModal;
	}

	public void setTamanhoModal(Integer tamanhoModal) {
		this.tamanhoModal = tamanhoModal;
	}

	public String getMinimizar() {
		setTamanhoModal(30);
		return "RichFaces.$('panelModalMatricula').hide(); RichFaces.$('panelModalMatricula').show()";
	}

	public String getMaximizar() {
		setTamanhoModal(450);
		return "RichFaces.$('panelModalMatricula').hide(); RichFaces.$('panelModalMatricula').show()";
	}

	public Boolean getValidarCpf() {
		if (validarCpf == null) {
			validarCpf = false;
		}
		return validarCpf;
	}

	public void setValidarCpf(Boolean validarCpf) {
		this.validarCpf = validarCpf;
	}

	public Boolean validarCadastroPorCpf() {
		try {
			ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade();
			if (config.getCodigo().intValue() != 0) {
				return config.getValidarCadastroCpf();
			} else if (config.getCodigo().intValue() == 0 && this.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
				return config.getValidarCadastroCpf();
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}

	}

	public String getZoom() {
		if (getTam().intValue() == 5) {
			return "width:5%";
		}
		if (getTam().intValue() == 10) {
			return "width:10%";
		}
		if (getTam().intValue() == 15) {
			return "width:15%";
		}
		if (getTam().intValue() == 20) {
			return "width:20%";
		}
		if (getTam().intValue() == 25) {
			return "width:25%";
		}
		if (getTam().intValue() == 30) {
			return "width:30%";
		}
		if (getTam().intValue() == 35) {
			return "width:35%";
		}
		if (getTam().intValue() == 40) {
			return "width:40%";
		}
		if (getTam().intValue() == 50) {
			return "width:50%";
		}
		if (getTam().intValue() == 55) {
			return "width:55%";
		}
		if (getTam().intValue() == 60) {
			return "width:60%";
		}
		if (getTam().intValue() == 65) {
			return "width:65%";
		}
		if (getTam().intValue() == 70) {
			return "width:70%";
		}
		if (getTam().intValue() == 75) {
			return "width:75%";
		}
		if (getTam().intValue() == 80) {
			return "width:80%";
		}
		if (getTam().intValue() == 85) {
			return "width:85%";
		}
		if (getTam().intValue() == 90) {
			return "width:90%";
		}
		if (getTam().intValue() == 95) {
			return "width:95%";
		}
		if (getTam().intValue() == 100) {
			return "width:100%";
		}
		if (getTam().intValue() == 105) {
			return "width:105%";
		}
		if (getTam().intValue() == 110) {
			return "width:110%";
		}
		if (getTam().intValue() == 115) {
			return "width:115%";
		}
		if (getTam().intValue() == 120) {
			return "width:120%";
		}
		if (getTam().intValue() == 125) {
			return "width:125%";
		}
		if (getTam().intValue() == 130) {
			return "width:130%";
		}
		if (getTam().intValue() == 135) {
			return "width:135%";
		}
		if (getTam().intValue() == 140) {
			return "width:140%";
		}
		if (getTam().intValue() == 145) {
			return "width:145%";
		}
		if (getTam().intValue() == 150) {
			return "width:150%";
		}
		return "width:50%";

	}

	public ArquivoHelper getArquivoHelper() {
		return arquivoHelper;
	}

	public void setArquivoHelper(ArquivoHelper arquivoHelper) {
		this.arquivoHelper = arquivoHelper;
	}

	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenhaAnterior() {
		if (senhaAnterior == null) {
			senhaAnterior = "";
		}
		return senhaAnterior;
	}

	public void setSenhaAnterior(String senhaAnterior) {
		this.senhaAnterior = senhaAnterior;
	}

	public String getLoginAnterior() {
		if (loginAnterior == null) {
			loginAnterior = "";
		}
		return loginAnterior;
	}

	public void setLoginAnterior(String loginAnterior) {
		this.loginAnterior = loginAnterior;
	}

	public String getLogin() {
		if (login == null) {
			login = "";
		}
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the tam
	 */
	public Integer getTam() {
		if (tam == null) {
			tam = 0;
		}
		return tam;
	}

	/**
	 * @param tam
	 *            the tam to set
	 */
	public void setTam(Integer tam) {
		this.tam = tam;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		if (width == null) {
			width = 550;
		}
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Integer width) {
		if (width > 800) {
			this.width = 800;
			return;
		}
		this.width = width;
	}

	/**
	 * @return the heigth
	 */
	public Integer getHeigth() {
		if (heigth == null) {
			heigth = 330;
		}
		return heigth;
	}

	/**
	 * @param heigth
	 *            the heigth to set
	 */
	public void setHeigth(Integer heigth) {
		if (heigth > 600) {
			this.heigth = 600;
			return;
		}
		this.heigth = heigth;
	}

	/**
	 * @return the fraseInspiracaoVO
	 */
	public FraseInspiracaoVO getFraseInspiracaoVO() {
		if (fraseInspiracaoVO == null) {
			return new FraseInspiracaoVO();
		}
		return fraseInspiracaoVO;
	}

	/**
	 * @param fraseInspiracaoVO
	 *            the fraseInspiracaoVO to set
	 */
	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}

	public Boolean getIsExisteUnidadeEnsinoLogado() throws Exception {
		return getUnidadeEnsinoLogado().getCodigo() > 0 ? true : false;
	}

	// ========================================================================================
	// COISAS QUE ERAM DO SUPERARQUITETURA E DEVERÃO SER DELETADAS CONFORME A
	// SUA NÃO UTILIZACAO
	private static Logger logger;

//	private HashMap<Integer, ConfiguracaoGeralSistemaVO> mapConfiguracao = new HashMap<Integer, ConfiguracaoGeralSistemaVO>(0);

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade() throws Exception {		
		return getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());		
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO()  throws Exception {
		return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade();
	}
	
	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO()  throws Exception {
		return getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino();
	}
	
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralPadraoSistema()  {
		try {
			if (!Uteis.isAtributoPreenchido(getUsuarioLogado())) {
				return getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null);
			} else {
				return getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado());			
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPadraoSistema() throws Exception {
		return getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
	}

	public LoginControle getLoginControle() {
		LoginControle loginControle = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
		return loginControle;
	}

	public VisaoAlunoControle getVisaoAlunoControle() {
		VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
		return visaoAlunoControle;
	}
	
	public VisaoCandidatoControle getVisaoCandidatoControle() {
		VisaoCandidatoControle visaoCandidatoControle = (VisaoCandidatoControle) context().getExternalContext().getSessionMap().get("VisaoCandidatoControle");
		return visaoCandidatoControle;
	}

	protected String internacionalizar(String mensagem) {
		String propriedade = obterArquivoPropriedades(mensagem);
		ResourceBundle bundle = ResourceBundle.getBundle(propriedade, getLocale(), getCurrentLoader(propriedade));
		try {
			return bundle.getString(mensagem);
		} catch (MissingResourceException e) {
			return mensagem;
		}
	}

	public String obterArquivoPropriedades(String mensagem) {
		if (mensagem.startsWith("msg")) {
			return "propriedades.Mensagens";
		} else if (mensagem.startsWith("enum")) {
			return "propriedades.Enum";
		} else if (mensagem.startsWith("prt")) {
			return "propriedades.Aplicacao";
		} else if (mensagem.startsWith("menu")) {
			return "propriedades.Menu";
		} else if (mensagem.startsWith("btn")) {
			return "propriedades.Botoes";
		} else {
			return "propriedades.Mensagens";
		}
	}

	protected static ClassLoader getCurrentLoader(Object fallbackClass) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = fallbackClass.getClass().getClassLoader();
		}
		return loader;
	}

	protected String getMensagemInternalizacao(String mensagemID) {
		
		ResourceBundle bundle = null;
		Locale locale = null;
		String nomeBundle = context().getApplication().getMessageBundle();
		if (nomeBundle != null) {
			locale = context().getViewRoot().getLocale();
			bundle = ResourceBundle.getBundle(nomeBundle, locale, getCurrentLoader(nomeBundle));
			try {
				mensagem = bundle.getString(mensagemID);
				return mensagem;
			} catch (MissingResourceException e) {
				return mensagemID;
			}
		}
		return mensagemID;
	}

	public Locale getLocale() {
		return context().getViewRoot().getLocale();
	}

	/**
	 * @return the configuracaoGeralSistemaVO
	 */
	// public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
	// if (configuracaoGeralSistemaVO == null) {
	// configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
	// }
	// return configuracaoGeralSistemaVO;
	// }
	/**
	 * @param configuracaoGeralSistemaVO
	 *            the configuracaoGeralSistemaVO to set
	 */
	// public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO
	// configuracaoGeralSistemaVO) {
	// this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	// }
	/**
	 * @return the ipMaquinaLogada
	 */

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger(SuperControle.class);
		}
		return logger;
	}

	/**
	 * @param aLogger
	 *            the logger to set
	 */
	public static void setLogger(Logger aLogger) {
		logger = aLogger;
	}

	public String getCaminhoClassesAplicacao() throws Exception {
		String caminhoBaseAplicacaoPrm = getCaminhoPastaWeb() + File.separator + "WEB-INF" + File.separator + "classes";
		return caminhoBaseAplicacaoPrm;
	}

	public HttpSession getSessionAplicacao() {
		return (HttpSession) UteisJSF.context().getExternalContext().getSession(false);
	}

	public void inativarUsuarioControleAtividadesUsuarioVO(UsuarioVO usuario) {
		try {
			AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");
			HttpSession session = (HttpSession) UteisJSF.context().getExternalContext().getSession(false);
			if (aplicacaoControle != null) {
				MapaControleAtividadesUsuarioVO mapaControleUsuarioVO = aplicacaoControle.obterMapaControleAtividadesUsuarioEspecifico(usuario, session.getId());
				mapaControleUsuarioVO.setUsuarioAtivo(Boolean.FALSE);
			}
		} catch (Exception e) {
		}
	}

	public boolean verificarSessaoClienteServidor() throws Exception {
		try {
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
			String idAutenticacao = configuracaoGeralSistemaVO.getIdAutenticacaoServOtimize();
			AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");

			Date dataAutenticacao = aplicacaoControle.getAutenticacaoRealizada();
			if (dataAutenticacao != null) {
//				System.out.println("INVESTLOGIN: dataAutenticacao: " + dataAutenticacao.toGMTString());
				if (new Date().before(dataAutenticacao)) {
//					System.out.println("INVESTLOGIN: entrando, pois agora " + new Date().toGMTString() + " é antes da dataAutenticacao " + dataAutenticacao.toGMTString());
					return true;
				} else {
//					System.out.println("INVESTLOGIN: autenticando, agora " + new Date().toGMTString() + " não é antes da dataAutenticacao " + dataAutenticacao.toGMTString());
					return !realizaBuscaAutenticacao(idAutenticacao, aplicacaoControle);
				}
			} else {
//				System.out.println("INVESTLOGIN: autenticando, pois dataAutenticacao NULL");
	     		return !realizaBuscaAutenticacao(idAutenticacao, aplicacaoControle);		
			}
		} catch (Exception e) {
//			System.out.println("INVESTLOGIN: exception: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	private boolean realizaBuscaAutenticacao(String idCliente, AplicacaoControle aplicacaoControle) throws Exception {
		boolean erroconexao = false;
		boolean autenticado = false;
		try {
			autenticado = getAutenticacao(idCliente, URL_AUTENTICACAO_1);
			erroconexao = false;
		} catch (Exception e) {
			erroconexao = true;
		}
		if (erroconexao) {
			try {
				autenticado = getAutenticacao(idCliente, URL_AUTENTICACAO_2);
				erroconexao = false;
			} catch (Exception e) {
				erroconexao = true;
			}
		}
		if (!Uteis.isDiaDaSemana(new Date()) && erroconexao) {
			this.executarAtivacaoTodosModulosSei();
			return false;
		} else if (erroconexao) {
			return erroconexao;
		}

		if (!autenticado) {
			return true;
		} else {
			if (autenticado) {
				aplicacaoControle.setAutenticacaoRealizada(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(new Date()));
			}
			return !autenticado;
		}
	}

	private boolean getAutenticacao(String idCliente, String url) throws Exception {
		Cliente cliente = new Cliente();
		cliente.setNome(idCliente);
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		WebResource resource = client.resource(url);
		Cliente autorizacao = resource.type("application/xml").post(Cliente.class, cliente);
		if (autorizacao.getAutorizado() == null || !autorizacao.getAutorizado()) {
			throw new Exception("");
		}
		getAplicacaoControle().setCliente(autorizacao);
		return autorizacao.getAutorizado();
	}

	public void verificarSessoesAbertas(AplicacaoControle aplicacao) throws Exception {
		Map map = (Map) UteisJSF.context().getExternalContext().getSessionMap();

	}

	public void registrarAtividadeUsuario(UsuarioVO usuarioVO, String entidade, String descricao, String tipo) throws Exception {
		if (usuarioVO == null) {
			return;
		}
		try {
			AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");
			if (aplicacaoControle.getMapaControleAtividadesUsuariosAtivo()) {
				HttpSession session = (HttpSession) UteisJSF.context().getExternalContext().getSession(false);
				verificarSessoesAbertas(aplicacaoControle);
				String sessionId = session.getId();
				aplicacaoControle.registrarAtividadeUsuarioEmNivelAplicacao(usuarioVO, sessionId, entidade, descricao, tipo);
				sessionId = null;
			}
		} catch (Exception e) {
		     System.out.print("Erro ao registrar atividade usuário.");
		}
	}

	public void removerObjetoMemoria(Object object) {
		Uteis.removerObjetoMemoria(object);
		// if (object != null) {
		// Class classe = object.getClass();
		// for (Field field : classe.getDeclaredFields()) {
		// field.setAccessible(true);
		// if (field != null) {
		// try {
		// if
		// (field.getType().getClass().getSuperclass().getSimpleName().equals("SuperVO")
		// ||
		// field.getType().getClass().getSuperclass().getSimpleName().equals("SuperControle")
		// ||
		// field.getType().getClass().getSuperclass().getSimpleName().equals("SuperControleRelatorio"))
		// {
		// removerObjetoMemoria(field);
		// }
		// if (field.getType().getName().equals("java.lang.Integer")
		// || field.getType().getName().equals("java.lang.String")
		// || field.getType().getName().equals("java.lang.Double")
		// || field.getType().getName().equals("java.lang.Boolean")
		// || field.getType().getName().equals("java.lang.Long")) {
		// field.set(object, null);
		// } else if (field.getType().getName().equals("java.util.List")
		// || field.getType().getName().equals("java.util.ArrayList")) {
		// List<?> lista = (List<?>) field.get(object);
		// // for(Object o: lista){
		// // removerObjetoMemoria(o);
		// // }
		// lista.clear();
		// field.set(object, null);
		// } else {
		// field.set(object, null);
		// }
		// field = null;
		// } catch (Exception e) {
		// }
		// }
		// }
		//
		// classe = null;
		// }
		//
	}

	public String getCaminhoFotoUsuario() {
		if (caminhoFotoUsuario == null) {
			caminhoFotoUsuario = "";
		}
		return caminhoFotoUsuario;
	}

	public void setCaminhoFotoUsuario(String caminhoFotoUsuario) {
		this.caminhoFotoUsuario = caminhoFotoUsuario;
	}

	public String getCaminhoFotoUsuario2() {
		if (caminhoFotoUsuario2 == null) {
			caminhoFotoUsuario2 = "";
		}
		return caminhoFotoUsuario2;
	}

	public void setCaminhoFotoUsuario2(String caminhoFotoUsuario2) {
		this.caminhoFotoUsuario2 = caminhoFotoUsuario2;
	}

	public String getOncompleteModal() {
		if (oncompleteModal == null) {
			oncompleteModal = "";
		}
		return oncompleteModal;
	}

	public void setOncompleteModal(String oncompleteModal) {
		this.oncompleteModal = oncompleteModal;
	}

	public Boolean getExibirBotao() {
		if (exibirBotao == null) {
			exibirBotao = false;
		}
		return exibirBotao;
	}

	public void setExibirBotao(Boolean exibirBotao) {
		this.exibirBotao = exibirBotao;
	}

	public Float getX() {
		if (x == null) {
			x = new Float(0);
		}
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		if (y == null) {
			y = new Float(0);
		}
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public Float getLargura() {
		if (largura == null) {
			largura = new Float(0);
		}
		return largura;
	}

	public void setLargura(Float largura) {
		this.largura = largura;
	}

	public Float getAltura() {
		if (altura == null) {
			altura = new Float(0);
		}
		return altura;
	}

	public void setAltura(Float altura) {
		this.altura = altura;
	}

	public Float getXcropVerso() {
		if (xcropVerso == null) {
			xcropVerso = new Float(0);
		}
		return xcropVerso;
	}

	public void setXcropVerso(Float xcropVerso) {
		this.xcropVerso = xcropVerso;
	}

	public Float getYcropVerso() {
		if (ycropVerso == null) {
			ycropVerso = new Float(0);
		}
		return ycropVerso;
	}

	public void setYcropVerso(Float ycropVerso) {
		this.ycropVerso = ycropVerso;
	}

	public Float getLarguraVerso() {
		if (larguraVerso == null) {
			larguraVerso = new Float(0);
		}
		return larguraVerso;
	}

	public void setLarguraVerso(Float larguraVerso) {
		this.larguraVerso = larguraVerso;
	}

	public Float getAlturaVerso() {
		if (alturaVerso == null) {
			alturaVerso = new Float(0);
		}
		return alturaVerso;
	}

	public void setAlturaVerso(Float alturaVerso) {
		this.alturaVerso = alturaVerso;
	}

	public Boolean getRemoverFoto() {
		if (removerFoto == null) {
			removerFoto = Boolean.FALSE;
		}
		return removerFoto;
	}

	public void setRemoverFoto(Boolean removerFoto) {
		this.removerFoto = removerFoto;
	}

	public Boolean getExibirUpload() {
		if (exibirUpload == null) {
			exibirUpload = Boolean.FALSE;
		}
		return exibirUpload;
	}

	public void setExibirUpload(Boolean exibirUpload) {
		this.exibirUpload = exibirUpload;
	}

	public DataModelo getControleConsultaOtimizado() {
		if (controleConsultaOtimizado == null) {
			controleConsultaOtimizado = new DataModelo();
		}
		return controleConsultaOtimizado;
	}

	public void setControleConsultaOtimizado(DataModelo controleConsultaOtimizado) {
		this.controleConsultaOtimizado = controleConsultaOtimizado;
	}

	public void anularDataModelo() {
		setControleConsultaOtimizado(null);
	}

	public UsuarioVO getRealizarValidacaoParaObterQualSeraUsuarioCorrente(UsuarioVO usuario) {
		if (usuario.getIsApresentarVisaoPais()) {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAlunoControle != null) {
				return visaoAlunoControle.getAlunoPorResponsavelLegal();
			}
		}
		return usuario;
	}

	public boolean getIsApresentarVisaoPais() {
		return getUsuarioLogado().getIsApresentarVisaoPais();
	}

	public ConfiguracaoFinanceiroVO configuracaoFinanceiroUnidadeLogada() throws Exception {
		if (getUnidadeEnsinoLogado() != null && !getUnidadeEnsinoLogado().getCodigo().equals(0) && !getUnidadeEnsinoLogado().getConfiguracoes().getCodigo().equals(0)) {			
			return getAplicacaoControle().getConfiguracaoFinanceiroVO(getUnidadeEnsinoLogado().getCodigo());
		}
		return getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
	}

	// /**
	// * @return the unidadeEnsinoVO
	// */
	// public UnidadeEnsinoVO getUnidadeEnsinoVO() {
	// return unidadeEnsinoVO;
	// }
	//
	// /**
	// * @param unidadeEnsinoVO the unidadeEnsinoVO to set
	// */
	// public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
	// this.unidadeEnsinoVO = unidadeEnsinoVO;
	// }
	public Map<String, Object> getRequestMap() {
		return context().getExternalContext().getRequestMap();
	}

	public ComunicacaoInternaVO inicializarDadosPadrao(ComunicacaoInternaVO comunicacaoEnviar) {
		// Caso o valor seja True, um email sera envida quando a comunicacao for
		// persistida.
		comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
		// Para obter a mensagem do email formatado Usamos um metodo a parte.
		comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
		comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
		return comunicacaoEnviar;

	}

	public List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(PessoaVO pessoa) {
		ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
		destinatario.setCiJaLida(Boolean.FALSE);
		destinatario.setDestinatario(pessoa);
		destinatario.setEmail(pessoa.getEmail());
		destinatario.setNome(pessoa.getNome());
		destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<ComunicadoInternoDestinatarioVO>();
		listDestinatario.add(destinatario);
		return listDestinatario;
	}

	public Integer getColumn() throws Exception {
		if (getUnidadeEnsinoVOs().size() > 4) {
			return 4;
		}
		return getUnidadeEnsinoVOs().size();
	}

	public Integer getElement() throws Exception {
		return getUnidadeEnsinoVOs().size();
	}

	public Integer getColumnTipoReq() throws Exception {
		if (getTipoReqVOs().size() > 4) {
			return 4;
		}
		return getTipoReqVOs().size();
	}
	
	public Integer getElementTipoReq() throws Exception {
		return getTipoReqVOs().size();
	}
	
	public void consultarUnidadeEnsinoFiltroRelatorio(String nomeEntidade) {
		consultarUnidadeEnsinoFiltroRelatorio(nomeEntidade, Boolean.TRUE);
	}
	
	public void consultarUnidadeEnsinoFiltroRelatorio(String nomeEntidade, Boolean validarUnidadeEnsinoLogado) {
		try {
			getUnidadeEnsinoVOs().clear();
			if (getUnidadeEnsinoLogado().getCodigo() > 0 && validarUnidadeEnsinoLogado) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioNomeEntidadePermissao(nomeEntidade, "", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
				for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
					obj.setFiltrarUnidadeEnsino(true);
				}
			} else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOMarcadasParaSeremUtilizadas() {
		return getUnidadeEnsinoVOs().stream().filter(p->p.getFiltrarUnidadeEnsino()).collect(Collectors.toList());
	}

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
			// consultarUnidadeEnsinoFiltroRelatorio("");
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public List<TipoRequerimentoVO> getTipoReqVOs() {
		if (tipoReqVOs == null) {
			tipoReqVOs = new ArrayList<TipoRequerimentoVO>(0);
			// consultarUnidadeEnsinoFiltroRelatorio("");
		}
		return tipoReqVOs;
	}
	
	public void setTipoReqVOs(List<TipoRequerimentoVO> tipoReqVOs) {
		this.tipoReqVOs = tipoReqVOs;
	}

	public Boolean getFiltrarUnidadeEnsino() {
		if (filtrarUnidadeEnsino == null) {
			filtrarUnidadeEnsino = Boolean.FALSE;
		}
		return filtrarUnidadeEnsino;
	}

	public void setFiltrarUnidadeEnsino(Boolean filtrarUnidadeEnsino) {
		this.filtrarUnidadeEnsino = filtrarUnidadeEnsino;
	}

	public Boolean getFiltrarTipoReq() {
		if (filtrarTipoReq == null) {
			filtrarTipoReq = Boolean.FALSE;
		}
		return filtrarTipoReq;
	}
	
	public void setFiltrarTipoReq(Boolean filtrarTipoReq) {
		this.filtrarTipoReq = filtrarTipoReq;
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadeEnsinoSelecionados();	
	}
	
	public Boolean getMarcarTodasUnidadeEnsino() {
		if (marcarTodasUnidadeEnsino == null) {
			marcarTodasUnidadeEnsino = Boolean.FALSE;
		}
		return marcarTodasUnidadeEnsino;
	}

	public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
		this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
	}

	public void marcarTodosTipoReqAction() {
		for (TipoRequerimentoVO unidade : getTipoReqVOs()) {
			if (marcarTodosTipoReq) {
				unidade.setFiltrarTipoReq(Boolean.TRUE);
			} else {
				unidade.setFiltrarTipoReq(Boolean.FALSE);
			}
		}
		//verificarTodosTipoReqSelecionados();	
	}
	
	public Boolean getMarcarTodosTipoReq() {
		if (marcarTodosTipoReq == null) {
			marcarTodosTipoReq = Boolean.FALSE;
		}
		return marcarTodosTipoReq;
	}
	
	public void setMarcarTodosTipoReq(Boolean marcarTodosTipoReq) {
		this.marcarTodosTipoReq = marcarTodosTipoReq;
	}

	public List<UnidadeEnsinoVO> getListaConsultarUnidadeEnsino() {
		if (listaConsultarUnidadeEnsino == null) {
			listaConsultarUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaConsultarUnidadeEnsino;
	}

	public void setListaConsultarUnidadeEnsino(List<UnidadeEnsinoVO> listaConsultarUnidadeEnsino) {
		this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(Integer unidadeEnsino) throws Exception {		
		return getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsino, getUsuarioLogado());
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(Integer unidadeEnsino) throws Exception {		
			return getAplicacaoControle().getConfiguracaoFinanceiroVO(unidadeEnsino);
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino() throws Exception {		
		return getAplicacaoControle().getConfiguracaoFinanceiroVO(getUnidadeEnsinoLogado().getCodigo());
	}
	
	public String getNomeTelaAtual() {
		if(context() != null && context().getViewRoot() != null){
			return context().getViewRoot().getViewId();	
		}
		return "";
	}

	protected void consultarDados() throws Exception {
		if (getControleConsulta() == null) {
			setControleConsulta(new ControleConsulta());
		}
		getControleConsulta().definirProximaPaginaApresentar(getControleConsulta().getPaginaAtual());
	}

	public void removerUsuarioLogadoSessao() {
		
		context().getExternalContext().getSessionMap().remove("usuarioLogado");
	}

	public VisaoProfessorControle getVisaoProfessorControle() {
		VisaoProfessorControle visaoProfessorControle = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
		return visaoProfessorControle;
	}

	public VisaoCoordenadorControle getVisaoCoordenadorControle() {
		VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
		return visaoCoordenadorControle;
	}

	public Boolean getMarcarTodosCursos() {
		if (marcarTodosCursos == null) {
			marcarTodosCursos = false;
		}
		return marcarTodosCursos;
	}

	public void setMarcarTodosCursos(Boolean marcarTodosCursos) {
		this.marcarTodosCursos = marcarTodosCursos;
	}
	
	public void marcarTodosCursoAction() {
		for (CursoVO curso : getCursoVOs()) {
			if (marcarTodosCursos) {
				curso.setFiltrarCursoVO(Boolean.TRUE);
			} else {
				curso.setFiltrarCursoVO(Boolean.FALSE);
			}
		}
		verificarTodosCursosSelecionados();
	}

	public Boolean getMarcarTodosTurnos() {
		if (marcarTodosTurnos == null) {
			marcarTodosTurnos = false;
		}
		return marcarTodosTurnos;
	}

	public void setMarcarTodosTurnos(Boolean marcarTodosTurnos) {
		this.marcarTodosTurnos = marcarTodosTurnos;
	}
	
	public void marcarTodosOsTurnosAction() {
		for (TurnoVO turno : getTurnoVOs()) {
			if (marcarTodosTurnos) {
				turno.setFiltrarTurnoVO(Boolean.TRUE);
			} else {
				turno.setFiltrarTurnoVO(Boolean.FALSE);
			}
		}
		verificarTodosTurnosSelecionados();
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}
	
	public List<CursoVO> getCursoVOsMarcadosParaSeremUtilizados() {
		return getCursoVOs().stream().filter(p->p.getFiltrarCursoVO()).collect(Collectors.toList());
	}

	public List<TurnoVO> getTurnoVOs() {
		if (turnoVOs == null) {
			turnoVOs = new ArrayList<TurnoVO>(0);
		}
		return turnoVOs;
	}

	public void setTurnoVOs(List<TurnoVO> turnoVOs) {
		this.turnoVOs = turnoVOs;
	}
	
	public List<TurnoVO> getTurnoVOsMarcadosParaSeremUtilizados() {
		return getTurnoVOs().stream().filter(p->p.getFiltrarTurnoVO()).collect(Collectors.toList());
	}
	

	public Boolean getMarcarTodosCentroResultado() {
		if (marcarTodosCentroResultado == null) {
			marcarTodosCentroResultado = false;
		}
		return marcarTodosCentroResultado;
	}

	public void setMarcarTodosCentroResultado(Boolean marcarTodosCentroResultado) {
		this.marcarTodosCentroResultado = marcarTodosCentroResultado;
	}
	
	public void consultarCursoFiltroRelatorio(String periodicidade) {
		consultarCursoFiltroRelatorio(periodicidade, null);
	}

	public void consultarCursoFiltroRelatorio(String periodicidade, String nivelEducacional) {
		try {
			setCursosApresentar("");			
			setTurnosApresentar("");
			if (getUnidadeEnsinoVOs().isEmpty()) {
				setCursoVOs(null);
				setTurnoVOs(null);
				return;
			}
			setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", periodicidade, nivelEducacional, getUnidadeEnsinoVOs(), getUsuarioLogado()));
		} catch (Exception e) {
			setCursoVOs(null);
			
		}
	}

	public void consultarTurnoFiltroRelatorio() {
		try {
			if(getTurnoVOs().isEmpty()){
				setTurnoVOs(getFacadeFactory().getTurnoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setTurnoVOs(null);			
		}
	}

	public Integer getColumnCurso() throws Exception {
		if (getCursoVOs().size() > 4) {
			return 4;
		}
		return getCursoVOs().size();
	}

	public Integer getElementCurso() throws Exception {
		return getCursoVOs().size();
	}

	public Integer getColumnTurno() throws Exception {
		if (getTurnoVOs().size() > 4) {
			return 4;
		}
		return getTurnoVOs().size();
	}

	public Integer getElementTurno() throws Exception {
		return getTurnoVOs().size();
	}

	public Boolean getAcessoRestrito() {
		if (acessoRestrito == null) {
			acessoRestrito = false;
		}
		return acessoRestrito;
	}

	public void setAcessoRestrito(Boolean acessoRestrito) {
		this.acessoRestrito = acessoRestrito;
	}
	
/* Início Situação Acadêmica */
	
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private Boolean marcarTodasSituacoesAcademicas;
	private Boolean marcarTodasSituacoesHistorico;
	
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
	
	public Boolean getMarcarTodasSituacoesAcademicas() {
		if (marcarTodasSituacoesAcademicas == null) {
			marcarTodasSituacoesAcademicas = false;
		}
		return marcarTodasSituacoesAcademicas;
	}

	public void setMarcarTodasSituacoesAcademicas(Boolean marcarTodasSituacoesAcademicas) {
		this.marcarTodasSituacoesAcademicas = marcarTodasSituacoesAcademicas;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoAcademica() {
		if (getMarcarTodasSituacoesAcademicas()) {
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoes();
		} else {
			getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFormacaoAcademica() {
		if (getMarcarTodasSituacoesAcademicas()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public void validarfiltroRelatorioAcademicoVOMarcarDesmarcarTodos() {
		if (getFiltroRelatorioAcademicoVO().isTodasSituacoesHistorico()) {
			setMarcarTodasSituacoesHistorico(true);
		}else {
			setMarcarTodasSituacoesHistorico(false);
		}
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosSituacaoHistorico() {
		if (getMarcarTodasSituacoesHistorico()) {
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
		} else {
			getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoesHistorico();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosFormacaoHistorico() {
		if (getMarcarTodasSituacoesHistorico()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	
	public Boolean getMarcarTodasSituacoesHistorico() {
		if (marcarTodasSituacoesHistorico == null) {
			marcarTodasSituacoesHistorico = false;
		}
		return marcarTodasSituacoesHistorico;
	}

	public void setMarcarTodasSituacoesHistorico(Boolean marcarTodasSituacoesHistorico) {
		this.marcarTodasSituacoesHistorico = marcarTodasSituacoesHistorico;
	}
	
	
	
	/* Final Situação Acadêmica */
	
	/* Início Notas Configuração Acadêmica */
	private Integer codigoConfiguracaoAcademicaVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicaVO;
	private List<SelectItem> listaSelectItemConfiguracaoAcademica;
	private Map<Integer, ConfiguracaoAcademicoVO> mapaConfiguracaoAcademica;
	private Boolean marcarTodasNotasConfiguracaoAcademica;
	
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicaVO() {
		if (configuracaoAcademicaVO == null) {
			configuracaoAcademicaVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicaVO;
	}

	public void setConfiguracaoAcademicaVO(ConfiguracaoAcademicoVO configuracaoAcademicaVO) {
		this.configuracaoAcademicaVO = configuracaoAcademicaVO;
	}
	
	public List<SelectItem> getListaSelectItemConfiguracaoAcademica() {
		if (listaSelectItemConfiguracaoAcademica == null) {
			montarListaSelectItemConfiguracaoAcademica();
		}
        return listaSelectItemConfiguracaoAcademica;
    }
	
	public void setListaSelectItemConfiguracaoAcademica(List<SelectItem> listaSelectItemConfiguracaoAcademica) {
		this.listaSelectItemConfiguracaoAcademica = listaSelectItemConfiguracaoAcademica;
	}
	
	public void montarListaSelectItemConfiguracaoAcademica() {
		listaSelectItemConfiguracaoAcademica = new ArrayList<SelectItem>(0);
		listaSelectItemConfiguracaoAcademica.add(new SelectItem(0, ""));
		try {
			List<ConfiguracaoAcademicoVO> lista = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracoesASeremUsadas(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			for (ConfiguracaoAcademicoVO configuracao : lista) {
				listaSelectItemConfiguracaoAcademica.add(new SelectItem(configuracao.getCodigo(), configuracao.getNome()));
				getMapaConfiguracaoAcademica().put(configuracao.getCodigo(), configuracao);
	        }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Map<Integer, ConfiguracaoAcademicoVO> getMapaConfiguracaoAcademica() {
		if (mapaConfiguracaoAcademica == null) {
			mapaConfiguracaoAcademica = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapaConfiguracaoAcademica;
	}

	public void setMapaConfiguracaoAcademica(Map<Integer, ConfiguracaoAcademicoVO> mapaConfiguracaoAcademica) {
		this.mapaConfiguracaoAcademica = mapaConfiguracaoAcademica;
	}
	
	public boolean getIsExibirFiltrarNotas() {
		return getConfiguracaoAcademicaVO().getCodigo() > 0;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodasNotasConfiguracaoAcademica() {
		if (getMarcarTodasNotasConfiguracaoAcademica()) {
			getConfiguracaoAcademicaVO().realizarMarcarTodasNotas();
		} else {
			getConfiguracaoAcademicaVO().realizarDesmarcarTodasNotas();
		}
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasNotasConfiguracaoAcademica() {
		if (getMarcarTodasNotasConfiguracaoAcademica()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}

	public Boolean getMarcarTodasNotasConfiguracaoAcademica() {
		if (marcarTodasNotasConfiguracaoAcademica == null) {
			marcarTodasNotasConfiguracaoAcademica = false;
		}
		return marcarTodasNotasConfiguracaoAcademica;
	}

	public void setMarcarTodasNotasConfiguracaoAcademica(
			Boolean marcarTodasNotasConfiguracaoAcademica) {
		this.marcarTodasNotasConfiguracaoAcademica = marcarTodasNotasConfiguracaoAcademica;
	}
	
	public Integer getCodigoConfiguracaoAcademicaVO() {
		if (codigoConfiguracaoAcademicaVO == null) {
			codigoConfiguracaoAcademicaVO = 0;
		}
		return codigoConfiguracaoAcademicaVO;
	}

	public void setCodigoConfiguracaoAcademicaVO(
			Integer codigoConfiguracaoAcademicaVO) {
		this.codigoConfiguracaoAcademicaVO = codigoConfiguracaoAcademicaVO;
	}
	
	public void atualizarConfiguracaoAcademica() {
		setConfiguracaoAcademicaVO(getMapaConfiguracaoAcademica().get(getCodigoConfiguracaoAcademicaVO()));
	}
	
	/* Final Notas Configuração Acadêmica */

	/**
	 * @return the cursoApresentar
	 */
	public String getCursosApresentar() {
		if (cursosApresentar == null) {
			cursosApresentar = "";
		}
		if (cursosApresentar.length() > 100) {
			return cursosApresentar.substring(0, 99) + "...";
		}
		return cursosApresentar;
	}

	/**
	 * @param cursoApresentar the cursoApresentar to set
	 */
	public void setCursosApresentar(String cursosApresentar) {
		this.cursosApresentar = cursosApresentar;
	}

	/**
	 * @return the turnoApresentar
	 */
	public String getTurnosApresentar() {
		if (turnosApresentar == null) {
			turnosApresentar = "";
		}
		if(turnosApresentar.length() > 90) {
			return turnosApresentar.substring(0, 89) + "...";
		}
		return turnosApresentar;
	}
	
	public void limparUnidadeEnsinos(){
		setUnidadeEnsinosApresentar(null);
		setMarcarTodasUnidadeEnsino(false);
		marcarTodasUnidadesEnsinoAction();
	}
	
	public void limparCursos(){
		setMarcarTodosCursos(false);
		realizarMarcacaoCursos();
	}
	
	
	
	public void limparTurnos(){
		setMarcarTodosTurnos(false);
		realizarMarcacaoTurnos();
	}
	
	public void realizarMarcacaoCursos() {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void realizarMarcacaoTurnos() {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}
	
	public void executarValidacaoSimulacaoVisao() throws Exception{
		if(getUsuarioLogado().getPermiteSimularNavegacaoAluno() && getUsuarioLogado().getIsApresentarVisaoAluno()){
			throw new Exception("Você está em um ambiente de simulação da visão do aluno, e não é possível realizar esta operação.");
		}
		if(getUsuarioLogado().getPermiteSimularNavegacaoAluno() && getUsuarioLogado().getIsApresentarVisaoAluno()){
			throw new Exception("Você está em um ambiente de simulação da visão do pais, e não é possível realizar esta operação.");
		}
		if(getUsuarioLogado().getPermiteSimularNavegacaoAluno() && getUsuarioLogado().getIsApresentarVisaoProfessor()){
			throw new Exception("Você está em um ambiente de simulação da visão do professor, e não é possível realizar esta operação.");
		}
		if(getUsuarioLogado().getPermiteSimularNavegacaoAluno() && getUsuarioLogado().getIsApresentarVisaoCoordenador()){
			throw new Exception("Você está em um ambiente de simulação da visão do coordenador, e não é possível realizar esta operação.");
		}
	}
	public void executarValidacaoSimulacaoVisaoAluno() throws Exception{
		executarValidacaoSimulacaoVisao();
	}
	
	public void executarValidacaoSimulacaoVisaoCoordenador() throws Exception{
		executarValidacaoSimulacaoVisao();		
	}
	
	public void executarValidacaoSimulacaoVisaoProfessor() throws Exception{
		executarValidacaoSimulacaoVisao();
	}
	
	public boolean isExecutarValidacaoSimulacaoVisaoProfessor() {
		boolean valida = false;
		if(getUsuarioLogado().getPermiteSimularNavegacaoAluno()){
			valida = true;
		}
		return valida;
	}

	/**
	 * @param turnoApresentar the turnoApresentar to set
	 */
	public void setTurnosApresentar(String turnosApresentar) {
		this.turnosApresentar = turnosApresentar;
	}
	
	public void verificarTodasUnidadeEnsinoSelecionados() {
		StringBuilder unidades = new StringBuilder();
		for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
			if (obj.getFiltrarUnidadeEnsino()) {				
				unidades.append(obj.getNome()).append("; ");
			}
		}
		setUnidadeEnsinosApresentar(unidades.toString());
	}

	public void verificarTodosTipoReqSelecionadas() {
		StringBuilder tipoRequerimento = new StringBuilder();
		for (TipoRequerimentoVO obj : getTipoReqVOs()) {
			if (obj.getFiltrarTipoReq()) {				
				tipoRequerimento.append(obj.getNome()).append("; ");
			}
		}
		setTipoRequerimento(tipoRequerimento.toString());
	}
	
	public void verificarTodasFormaPagamentoSelecionados() {
		StringBuilder unidades = new StringBuilder();
		for (FormaPagamentoVO obj : getFormaPagamentoVOs()) {
			if (obj.getFiltrarFormaPagamento()) {				
				unidades.append(obj.getNome()).append("; ");
			}
		}
		setFormaPagamentosApresentar(unidades.toString());
	}	
	
	public void verificarTodosCursosSelecionados() {
		setCursosApresentar("");
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursosApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursosApresentar(getCursoVOs().get(0).getNome());
				}
			} else {
				setCursosApresentar(curso.toString());
			}
		}
	}

	public void verificarTodosTurnosSelecionados() {
		setTurnosApresentar("");
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			setTurnosApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnosApresentar(getTurnoVOs().get(0).getNome());
				}
			} else {
				setTurnosApresentar(turno.toString());
			}
		}
	}

	public String getUnidadeEnsinosApresentar() {
		if(unidadeEnsinosApresentar == null){
			unidadeEnsinosApresentar = "";
		}
		return unidadeEnsinosApresentar;
	}

	public void setUnidadeEnsinosApresentar(String unidadeEnsinosApresentar) {
		this.unidadeEnsinosApresentar = unidadeEnsinosApresentar;
	}
	
	public void executarValidacaoParametroConsultaVazio() throws Exception {
		if ((isCampoConsultaSomenteNumero() && getControleConsulta().getValorConsulta().length() < 1) ||
				(!isCampoConsultaSomenteNumero() && getControleConsulta().getValorConsulta().length() < 2)) {
			throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
		}
	}
	
	public void executarValidacaoParametroConsultaOtimizadoVazio() throws Exception {
		if ((isCampoConsultaOtimizadoSomenteNumero() && getControleConsultaOtimizado().getValorConsulta().length() < 0) ||
				(!isCampoConsultaOtimizadoSomenteNumero() && getControleConsultaOtimizado().getValorConsulta().length() < 2)) {
			throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
		}
	}
	
	public boolean isCampoConsultaOtimizadoSomenteNumero(){
		return getControleConsultaOtimizado().getCampoConsulta().equals("codigo") || getControleConsultaOtimizado().getCampoConsulta().equals("CODIGO") ? true: false;
	}
	
	public boolean isCampoConsultaSomenteNumero(){
		return getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("CODIGO") ? true: false;
	}
	
	public String getMontarScriptParaCampoConsultaOtimizadoSomenteNumero(){
		return isCampoConsultaOtimizadoSomenteNumero() ? "return somenteNumero1(event);":"";
	}
	
	public String getMontarScriptParaCampoConsultaSomenteNumero(){
		return isCampoConsultaSomenteNumero() ? "return somenteNumero1(event);":"";
	}
	
	public void limparCampoValorConsultaOtimizado(){
		getControleConsultaOtimizado().setValorConsulta("");
	}
	
	public void limparCampoValorConsulta(){
		getControleConsulta().setValorConsulta("");
	}
	
	public String getConfiguracaoEditorSomenteLeitura() {
		if(configuracaoEditorSomenteLeitura == null) {
			StringBuilder sb = new StringBuilder();
//			sb.append(" toolbar: 'custom', ");
//			sb.append(" toolbar_custom:[], ");
//			sb.append(" customConfig:'', ");
			sb.append(" resize_enabled: false,");
			sb.append(" fullPage: true, ");		
			sb.append(" removePlugins: 'toolbar, elementspath' "); 
			configuracaoEditorSomenteLeitura = sb.toString();
		}
		return configuracaoEditorSomenteLeitura;
	}

	public String getConfiguracaoEditorBasica() {
		if(configuracaoEditorBasica == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(" resize_enabled: false,");
			sb.append(" fullPage: true");
			configuracaoEditorBasica = sb.toString();
		}
		return configuracaoEditorBasica;
	}
	
	public String getConfiguracaoEditorAvancada() {
		if(configuracaoEditorAvancada == null) {
		StringBuilder sb = new StringBuilder();
		sb.append("toolbar: 'custom',");
		sb.append(" fullPage: true,");
		sb.append(" resize_enabled: false,");
		sb.append(" toolbar_custom:");
		sb.append(" [");
		sb.append(" { name: 'document', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-','Templates' ] },");
		sb.append(" { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },");
		sb.append(" { name: 'editing', items : [ 'Find','Replace','-','SelectAll','-','SpellChecker', 'Scayt' ] },");
		sb.append(" { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },");		
		sb.append(" { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },");
		sb.append(" { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv', '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },");
		sb.append(" { name: 'links', items : [ 'Link','Unlink','Anchor' ] },");
		sb.append(" { name: 'insert', items : [ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe' ] },");		
		sb.append(" { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },");
		sb.append(" { name: 'colors', items : [ 'TextColor','BGColor' ] },");
		sb.append(" { name: 'tools', items : [ 'Maximize' ] }");
		sb.append("],");
		sb.append(" forceEnterMode : true,");
		sb.append(" removePlugins : 'elementspath, magicline'");
		configuracaoEditorAvancada = sb.toString();
		}
		return configuracaoEditorAvancada;
	}
	
	public String getConfiguracaoEditorUpload() {
		if(configuracaoEditorUpload == null) {
		StringBuilder sb = new StringBuilder();
		sb.append("toolbar: 'custom',");
		sb.append(" fullPage: true,");
		sb.append(" resize_enabled: false,");
		sb.append(" allowedContent : true,");
		sb.append(" forcePasteAsPlainText : false,");
		sb.append(" basicEntities : true,");
		sb.append(" entities : true,");
		sb.append(" entities_latin : true,");
		sb.append(" entities_greek : true,");
		sb.append(" entities_processNumerical : true,");
		sb.append(" filebrowserBrowseUrl : '',");
		sb.append(" filebrowserUploadUrl : '../../UploadServlet?caminhoBase=#{ConteudoControle.caminhoUploadBaseArquivoREA}',");
		sb.append(" toolbar_custom:");
		sb.append(" [");
		sb.append(" { name: 'LinhaConfiguracaoUpload1', items : [ 'Source','-','Save','NewPage','DocProps','Preview','Print','-',");
		sb.append(" 'Templates', 'Cut','Copy','Paste', 'PasteText','PasteFromWord','-','Undo','Redo','Find','Replace','-','SelectAll','-',");
		sb.append(" 'SpellChecker', 'Scayt','Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField','Bold','Italic',");
		sb.append(" 'Underline','Strike','Subscript','Superscript','-','RemoveFormat','NumberedList','BulletedList','-','Outdent','Indent','-',");
		sb.append(" 'Blockquote','CreateDiv', '-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl','Link','Unlink','Anchor','Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak','Iframe'] },");
		sb.append(" { name: 'LinhaConfiguracaoUpload2', items : [ 'Styles','Format','Font','FontSize','TextColor','BGColor' ] },");
		sb.append(" { name: 'tools', items : [ 'Maximize' ] }");
		sb.append(" ],");
		sb.append(" forceEnterMode : true,");
		sb.append(" removePlugins : 'elementspath, magicline'");
		configuracaoEditorUpload = sb.toString();
		}
		return configuracaoEditorUpload;
	}
	
	public Flash getFlashMemoria() {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash();
	}

	public String getIdControlador() {
		if (idControlador == null) {
			idControlador = "";
		}
		return idControlador;
	}

	public void setIdControlador(String idControlador) {
		this.idControlador = idControlador;
	}
	
	private Map<String, Object> getViewMap() {
		if (FacesContext.getCurrentInstance().getViewRoot() != null) {
			return (Map<String, Object>) FacesContext.getCurrentInstance().getViewRoot().getViewMap();
		}
		return null;

	}

	public void executarAtivacaoTodosModulosSei() {
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloAcademico(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloAdministrativo(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloAvaliacaoInstitucional(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloBancoDeCurriculo(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloBiblioteca(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloCompras(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloCrm(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloEad(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloEstagio(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloFinanceiro(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloMonografia(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloPatrimonio(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloPlanoOrcamentario(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloProcessoSeletivo(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloSeiDecidir(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloNotaFiscal(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloRH(true);
		this.getAplicacaoControle().getCliente().setPermitirAcessoModuloContabil(true);
	}

	public Boolean getMarcarTodosOperadoraCartaos() {
		if (marcarTodosOperadoraCartaos == null) {
			marcarTodosOperadoraCartaos = false;
		}
		return marcarTodosOperadoraCartaos;
	}
	
	public void setMarcarTodosOperadoraCartaos(Boolean marcarTodosOperadoraCartaos) {
		this.marcarTodosOperadoraCartaos = marcarTodosOperadoraCartaos;
	}
	
	public void setOperadoraCartaoVOs(List<OperadoraCartaoVO> operadoraCartaoVOs) {
		this.operadoraCartaoVOs = operadoraCartaoVOs;
	}

	public List<OperadoraCartaoVO> getOperadoraCartaoVOs() {
		if (operadoraCartaoVOs == null) {
			operadoraCartaoVOs = new ArrayList<OperadoraCartaoVO>(0);
		}
		return operadoraCartaoVOs;
	}
	
	public List<OperadoraCartaoVO> getOperadoraCartaoVOMarcadasParaSeremUtilizadas() {
		return getOperadoraCartaoVOs().stream().filter(p->p.getFiltrarOperadoraCartaoVO()).collect(Collectors.toList());
	}

	public void consultarOperadoraCartaoFiltroRelatorio()  {
		try {
			if (getOperadoraCartaoVOs().isEmpty()) {
				setOperadoraCartaosApresentar("");			
				if (getUnidadeEnsinoVOs().isEmpty()) {
					setOperadoraCartaoVOs(null);
					return;
				}			
				setOperadoraCartaoVOs(getFacadeFactory().getOperadoraCartaoFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado()));
			}
		} catch (Exception e) {
			setOperadoraCartaoVOs(null);
		}
	}

	public void realizarMarcacaoOperadoraCartaos() {
		for (OperadoraCartaoVO operadoraCartaoVO : getOperadoraCartaoVOs()) {
			operadoraCartaoVO.setFiltrarOperadoraCartaoVO(getMarcarTodosOperadoraCartaos());
		}
		verificarTodosOperadoraCartaosSelecionados();
	}

	public void verificarTodosOperadoraCartaosSelecionados() {
		StringBuilder operadoraCartao = new StringBuilder();
		setOperadoraCartaosApresentar("");
		if (getOperadoraCartaoVOs().size() > 0) {
			for (OperadoraCartaoVO obj : getOperadoraCartaoVOs()) {
				if (obj.getFiltrarOperadoraCartaoVO()) {
					operadoraCartao.append(obj.getCodigo()).append(" - ");
					operadoraCartao.append(obj.getNome()).append(" - ");
					operadoraCartao.append(obj.getOperadoraCartaoCreditoApresentar()).append("; ");
				}
			}
			setOperadoraCartaosApresentar(operadoraCartao.toString());
		}
	}


	public String getOperadoraCartaosApresentar() {
		if (operadoraCartaosApresentar == null) {
			operadoraCartaosApresentar = "";
		}
		return operadoraCartaosApresentar;
	}

	public void setOperadoraCartaosApresentar(String operadoraCartaosApresentar) {
		this.operadoraCartaosApresentar = operadoraCartaosApresentar;
	}
	
	public void limparOperadoraCartao() {
		try {
			getOperadoraCartaoVOs().clear();
			setOperadoraCartaosApresentar("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getNumeroVersaoSistema() {
		if (Uteis.isAtributoPreenchido(versaoSistema)) {
			return versaoSistema.substring(6);
		}
		return "";
	}
	
	public void marcarTodasOpcoesEntidade(List<SuperVOSelecionadoInterface> listaSuperVO) {
		listaSuperVO.parallelStream().forEach(p->p.setSelecionado(true));
	}
	
	public void desmarcarTodasOpcoesEntidade(List<SuperVOSelecionadoInterface> listaSuperVO) {
		listaSuperVO.parallelStream().forEach(p->p.setSelecionado(false));
	}	    

	public Integer getColumnFormaPagamento() throws Exception {
		if (getFormaPagamentoVOs().size() > 4) {
			return 4;
		}
		return getFormaPagamentoVOs().size();
	}

	public Integer getElementFormaPagamento() throws Exception {
		return getFormaPagamentoVOs().size();
	}

	public void consultarFormaPagamentoFiltroRelatorio(String nomeEntidade) {
		try {
			getFormaPagamentoVOs().clear();
			setFormaPagamentoVOs(getFacadeFactory().getFormaPagamentoFacade().consultarFormaPagamentoFaltandoLista(getFormaPagamentoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			//setListaConsultarFormaPagamento(new ArrayList<FormaPagamentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<FormaPagamentoVO> getFormaPagamentoVOs() {
		if (formaPagamentoVOs == null) {
			formaPagamentoVOs = new ArrayList<FormaPagamentoVO>(0);
			// consultarUnidadeEnsinoFiltroRelatorio("");
		}
		return formaPagamentoVOs;
	}

	public void setFormaPagamentoVOs(List<FormaPagamentoVO> formaPagamentoVOs) {
		this.formaPagamentoVOs = formaPagamentoVOs;
	}

	public Boolean getFiltrarFormaPagamento() {
		if (filtrarFormaPagamento == null) {
			filtrarFormaPagamento = Boolean.FALSE;
		}
		return filtrarFormaPagamento;
	}

	public void setFiltrarFormaPagamento(Boolean filtrarFormaPagamento) {
		this.filtrarFormaPagamento = filtrarFormaPagamento;
	}

	public void marcarTodasFormaPagamentoAction() {
		for (FormaPagamentoVO unidade : getFormaPagamentoVOs()) {
			if (marcarTodasFormaPagamento) {
				unidade.setFiltrarFormaPagamento(Boolean.TRUE);
			} else {
				unidade.setFiltrarFormaPagamento(Boolean.FALSE);
			}
		}
		verificarTodasFormaPagamentoSelecionados();	
	}

	public Boolean getMarcarTodasFormaPagamento() {
		if (marcarTodasFormaPagamento == null) {
			marcarTodasFormaPagamento = Boolean.FALSE;
		}
		return marcarTodasFormaPagamento;
	}

	public void setMarcarTodasFormaPagamento(Boolean marcarTodasFormaPagamento) {
		this.marcarTodasFormaPagamento = marcarTodasFormaPagamento;
	}
	
	public void removerControleMemoriaFlash(String idControle){
		if(context() != null){
			context().getExternalContext().getFlash().remove(idControle);
		}
	}
	
	public void removerControleMemoriaTela(String idControle){
		if(context() != null){
			context().getViewRoot().getViewMap().remove(idControle);
		}
	}
	
	public void removerControleMemoriaFlashTela(String idControle){
		if(Uteis.isAtributoPreenchido(idControle)) {
			removerControleMemoriaFlash(idControle);
			removerControleMemoriaTela(idControle);
		}
	}
	
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;

	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return filtroRelatorioFinanceiroVO;
	}

	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
	}
	
	public void setCaminhoPastaWeb(String diretorioPastaWeb) {
		this.diretorioPastaWeb = diretorioPastaWeb;
	}

	public String getFormaPagamentosApresentar() {
		if (formaPagamentosApresentar == null) {
			formaPagamentosApresentar = "";
		}
		return formaPagamentosApresentar;
	}

	public void setFormaPagamentosApresentar(String formaPagamentosApresentar) {
		this.formaPagamentosApresentar = formaPagamentosApresentar;
	}
	
	
	
	public Boolean getMarcarTodosTipoOrigemContaPagar() {
		return marcarTodosTipoOrigemContaPagar;
	}

	public void setMarcarTodosTipoOrigemContaPagar(Boolean marcarTodosTipoOrigemContaPagar) {
		this.marcarTodosTipoOrigemContaPagar = marcarTodosTipoOrigemContaPagar;
	}	
	
	public void realizarDownloadArquivo(ArquivoVO arquivoVO) throws CloneNotSupportedException {
		AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.UPLOAD, "realizarDownloadArquivo " + arquivoVO.getDescricao());
		if(!arquivoVO.getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}
		AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.UPLOAD, "pastaBaseArquivo " + arquivoVO.getPastaBaseArquivo());
		AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.UPLOAD, "servidorArquivoOnline " + arquivoVO.getServidorArquivoOnline().getValor());
	}
	
	public void realizarDownloadArquivo(ArquivoVO arquivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws CloneNotSupportedException {	
		if(!arquivoVO.getPastaBaseArquivo().startsWith(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			cloneArquivo.setPastaBaseArquivo(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);		
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigemContaPagar() {
		getFiltroRelatorioFinanceiroVO().realizarSelecaoTodasOrigensContaPagar(getMarcarTodosTipoOrigemContaPagar());
	}
	
	private Boolean marcarTodosTipoOrigemContaReceber =  false;
	public Boolean getMarcarTodosTipoOrigemContaReceber() {
		return marcarTodosTipoOrigemContaReceber;
	}

	public void setMarcarTodosTipoOrigemContaReceber(Boolean marcarTodosTipoOrigemContaReceber) {
		this.marcarTodosTipoOrigemContaReceber = marcarTodosTipoOrigemContaReceber;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigemContaReceber() {
		getFiltroRelatorioFinanceiroVO().realizarSelecaoTodasOrigens(getMarcarTodosTipoOrigemContaReceber());
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTipoOrigemContaReceber()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	private String abaAtiva;

	public String getAbaAtiva() {
		return abaAtiva;
}

	public void setAbaAtiva(String abaAtiva) {
		this.abaAtiva = abaAtiva;
	}
	
	private String nomeParcelaMatriculaApresentarAluno;
	private String nomeParcelaMaterialDidaticoApresentarAluno;
	
	public String getNomeParcelaMatriculaApresentarAluno() {
		if(nomeParcelaMatriculaApresentarAluno == null) {
			nomeParcelaMatriculaApresentarAluno = "Matrícula";
		}
		return nomeParcelaMatriculaApresentarAluno;
	}

	public void setNomeParcelaMatriculaApresentarAluno(String nomeParcelaMatriculaApresentarAluno) {
		this.nomeParcelaMatriculaApresentarAluno = nomeParcelaMatriculaApresentarAluno;
	}

	public String getNomeParcelaMaterialDidaticoApresentarAluno() {
		if(nomeParcelaMaterialDidaticoApresentarAluno == null) {
			nomeParcelaMaterialDidaticoApresentarAluno = "Material Didático";
		}
		return nomeParcelaMaterialDidaticoApresentarAluno;
	}

	public void setNomeParcelaMaterialDidaticoApresentarAluno(String nomeParcelaMaterialDidaticoApresentarAluno) {
		this.nomeParcelaMaterialDidaticoApresentarAluno = nomeParcelaMaterialDidaticoApresentarAluno;
	}

	public void preencherNomeParcelaMatriculaApresentarAluno(Integer codigoUnidadeEnsino) {
		ConfiguracaoFinanceiroVO conf = null;
		try {
			conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(codigoUnidadeEnsino);
			if(conf!= null && Uteis.isAtributoPreenchido(conf.getNomeParcelaMatriculaApresentarAluno())) {
				setNomeParcelaMatriculaApresentarAluno(conf.getNomeParcelaMatriculaApresentarAluno());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void preencherNomeParcelaMaterialDidaticoApresentarAluno(Integer codigoUnidadeEnsino) {
		ConfiguracaoFinanceiroVO conf = null;
		try {
			conf = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(codigoUnidadeEnsino);
			if(conf!= null && Uteis.isAtributoPreenchido(conf.getNomeParcelaMaterialDidaticoApresentarAluno())) {
				setNomeParcelaMaterialDidaticoApresentarAluno(conf.getNomeParcelaMaterialDidaticoApresentarAluno());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String navegarAba(String abaAtiva, String navegacao) {
		setAbaAtiva(abaAtiva);
		if(Uteis.isAtributoPreenchido(navegacao)) {
			return Uteis.getCaminhoRedirecionamentoNavegacao(navegacao);
		}
		return "";
	}	
	
	public InfoWSVO validarDadosAutenticacaoTokenWebService(HttpServletRequest request) {
		InfoWSVO infoRSVO = null;
		try {
			String token = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarTokenWebServicePadraoSistema();
			if (token.isEmpty()) {
				infoRSVO = new InfoWSVO(Status.FORBIDDEN, "Não esta habilidado o uso de Web Service na Configurações do Sei." );
			}else if (request.getHeader("Authorization") == null || !token.equals(request.getHeader("Authorization"))) {
				infoRSVO = new InfoWSVO(Status.FORBIDDEN, "Token não confere com informado pelo servidor.");
			}else {
				infoRSVO = new InfoWSVO(Status.OK, "token confirmado.");
			}	
		} catch (Exception e) {
			infoRSVO = new InfoWSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return infoRSVO;
	}

	public InfoWSVO validarDadosAutenticacaoBearerTokenWebService(HttpServletRequest request) {
		InfoWSVO infoRSVO;
		try {
			String expectedToken = getFacadeFactory()
					.getConfiguracaoGeralSistemaFacade()
					.consultarTokenWebServicePadraoSistema();

			if (expectedToken.isEmpty()) {
				return new InfoWSVO(Status.FORBIDDEN,
						"Não está habilitado o uso de Web Service nas configurações do Sei.");
			}

			String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return new InfoWSVO(Status.FORBIDDEN,
						"Cabeçalho Authorization inválido ou ausente.");
			}

			String receivedToken = authHeader.substring("Bearer ".length()).trim();

			if (!expectedToken.equals(receivedToken)) {
				return new InfoWSVO(Status.FORBIDDEN,
						"Token não confere com o informado pelo servidor.");
			}

			infoRSVO = new InfoWSVO(Status.OK, "Token confirmado.");
		} catch (Exception e) {
			infoRSVO = new InfoWSVO(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		return infoRSVO;
	}


	/***
	 * 
	 * Monta COMBOBOX <br> 
	 * 
	 * Enum nao considera opcao de nenhum para fazer o processamento (vide outros montarCombobox)
	 *  
	 * @param enumeradores
	 * @param obrigatorio
	 * @return
	 */
	public List<SelectItem> montarComboboxSemOpcaoDeNenhum(@SuppressWarnings("rawtypes") Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		
		if (obrigatorio == Obrigatorio.NAO) 
			lista.add(new SelectItem("", ""));
		
		for (Enum<?> enumerador : enumeradores) {
			lista.add(new SelectItem(enumerador, internacionalizarEnum(enumerador)));
		}
		return lista;
	}
	
	public String internacionalizarEnum(Enum<?> enumerador) {
		return UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString());
	}

	public DataModelo getControleConsultaTurma() {
		if(controleConsultaTurma == null) {
			controleConsultaTurma = new DataModelo();
			controleConsultaTurma.setLimitePorPagina(10);
			controleConsultaTurma.setPaginaAtual(0);
			controleConsultaTurma.setPage(0);
		}
		return controleConsultaTurma;
	}

	public void setControleConsultaTurma(DataModelo controleConsultaTurma) {
		this.controleConsultaTurma = controleConsultaTurma;
	}
	
	public void consultarTurma(String valorConsulta, Integer curso, Integer unidadeEnsino, int nivelMontarDados) throws Exception {
		getControleConsultaTurma().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(valorConsulta,curso,  unidadeEnsino, false, nivelMontarDados, getUsuarioLogado(), getControleConsultaTurma().getLimitePorPagina(), getControleConsultaTurma().getOffset()));
		getControleConsultaTurma().setTotalRegistrosEncontrados(getFacadeFactory().getTurmaFacade().consultaRapidaTotalRegistroPorUnidadeEnsinoCurso(valorConsulta,curso,  unidadeEnsino));
		setMensagemID("msg_dados_consultados");		
	}
	
	
	
	
	public List<SelectItem> getListaTipoLayoutAtaResultadosFinais(String tipoLayout) throws Exception {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		List<ConfiguracaoLayoutAtaResultadosFinaisVO> configuracaoLayoutAtaResultadosFinaisVOs =  null;
		try {
			if(tipoLayout == null || !tipoLayout.equals("PADRAO")) {
				configuracaoLayoutAtaResultadosFinaisVOs = getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().consultar(getUsuarioLogado());
			}
			if(configuracaoLayoutAtaResultadosFinaisVOs != null && !configuracaoLayoutAtaResultadosFinaisVOs.isEmpty()) {
				itens = UtilSelectItem.getListaSelectItem(configuracaoLayoutAtaResultadosFinaisVOs.stream().filter(t -> !t.getInativarLayout()).collect(Collectors.toList()), "chaveUsar", "titulo", false);
				if(configuracaoLayoutAtaResultadosFinaisVOs.stream().anyMatch(t -> t.getLayoutPadrao())) {
					setTipoLayoutAtaResultadosFinais(configuracaoLayoutAtaResultadosFinaisVOs.stream().filter(t -> t.getLayoutPadrao()).findFirst().get().getChaveUsar());
				}else {
					setTipoLayoutAtaResultadosFinais(configuracaoLayoutAtaResultadosFinaisVOs.stream().findFirst().get().getChaveUsar());
				}
			}else if(configuracaoLayoutAtaResultadosFinaisVOs == null || configuracaoLayoutAtaResultadosFinaisVOs.isEmpty()) {
				itens.add(new SelectItem("Layout1", "Layout 1"));
				itens.add(new SelectItem("Layout2", "Layout 2"));
				itens.add(new SelectItem("Layout3", "Layout 3"));
				itens.add(new SelectItem("Layout4", "Layout 4"));
				if(!tipoLayout.equals("Layout1") && !tipoLayout.equals("Layout2") && !tipoLayout.equals("Layout3") && !tipoLayout.equals("Layout4")) {
					setTipoLayoutAtaResultadosFinais("Layout1");
				}else {
					setTipoLayoutAtaResultadosFinais(tipoLayout);
				}
			
			} 
					
		
		if(Uteis.isAtributoPreenchido(tipoLayout)) {
			boolean existeLayout = false;
			for(SelectItem item : itens) {
				if(item.getValue().equals(tipoLayout)) {
					existeLayout = true;
					break;
				}
			}
			if(!existeLayout) {
				if(itens.isEmpty()) {
					setTipoLayoutAtaResultadosFinais("");
				}else {
					setTipoLayoutAtaResultadosFinais((String)itens.get(0).getValue());
				}
			}else {
				setTipoLayoutAtaResultadosFinais(tipoLayout);
			}
		}
		} catch (Exception e) {
			throw e;
		}
		return itens;
	}

	public String getTipoLayoutHistorico() {
		if (tipoLayoutHistorico == null) {
			tipoLayoutHistorico = "";
		}
		return tipoLayoutHistorico;
	}

	public void setTipoLayoutHistorico(String tipoLayoutHistorico) {
		this.tipoLayoutHistorico = tipoLayoutHistorico;
	}
	
	public String getCaminhoImagemPadraoTopo() {
		if (caminhoImagemPadraoTopo == null) {
			caminhoImagemPadraoTopo = "";
		}
		return caminhoImagemPadraoTopo;
	}

	public void setCaminhoImagemPadraoTopo(String caminhoImagemPadraoTopo) {
		this.caminhoImagemPadraoTopo = caminhoImagemPadraoTopo;
	}

	public String getCaminhoImagemPadraoRodape() {
		if (caminhoImagemPadraoRodape == null) {
			caminhoImagemPadraoRodape = "";
		}
		return caminhoImagemPadraoRodape;
	}

	public void setCaminhoImagemPadraoRodape(String caminhoImagemPadraoRodape) {
		this.caminhoImagemPadraoRodape = caminhoImagemPadraoRodape;
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
	
	public String getMensagemEnvioEmail() {
		if (mensagemEnvioEmail == null) {
			mensagemEnvioEmail = "";
		}
		return mensagemEnvioEmail;
	}

	public void setMensagemEnvioEmail(String mensagemEnvioEmail) {
		this.mensagemEnvioEmail = mensagemEnvioEmail;
	}

	public Boolean getAbrirModalEnvioMensagem() {
		if (abrirModalEnvioMensagem == null) {
			abrirModalEnvioMensagem = false;
		}
		return abrirModalEnvioMensagem;
	}

	public void setAbrirModalEnvioMensagem(Boolean abrirModalEnvioMensagem) {
		this.abrirModalEnvioMensagem = abrirModalEnvioMensagem;
	}

	public String removerControleMemoriaFlashTela(String idControle, String telaRetorno){
		if(Uteis.isAtributoPreenchido(idControle)) {
			removerControleMemoriaFlashTela(idControle);
		}
		
		return Uteis.getCaminhoRedirecionamentoNavegacao(telaRetorno);
		
	}

	public String getLongitude() {
		if (longitude == null) {
			longitude = "";
		}
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		if (latitude == null) {
			latitude = "";
		}
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


	 public String getMensagemComLayout(String mensagemEnvioEmail) {

	        String dominio = "";
	        try {
	          	FacesContext context = FacesContext.getCurrentInstance();
	          	String paginaAtual = context == null ? "" : context.getViewRoot().getViewId();
	          	if (paginaAtual.contains("visaoProfessor") 
	          			|| paginaAtual.contains("visaoAluno") 
	          			|| paginaAtual.contains("visaoCoordenador") 
	          			|| paginaAtual.contains("visaoCandidato") 
	          			|| paginaAtual.contains("visaoParceiro") 
	          			|| paginaAtual.contains("visaoPreInscricao")) {
	          		dominio = "resources/imagens/email/";	
	          	} else {
	          		dominio = "resources/imagens/email/";        		
}
	          } catch (Exception e) {
	             // //System.out.println("Comunicacao Erro:" + e.getMessage());
	          }
	        try {
	        	setCaminhoImagemPadraoTopo(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getUrlAcessoExternoAplicacao()+"/resources/imagens/email/cima_sei.jpg");
	        	setCaminhoImagemPadraoRodape(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getUrlAcessoExternoAplicacao()+"/resources/imagens/email/baixo_sei.jpg");
	        }catch (Exception e) {
	        	setCaminhoImagemPadraoTopo(dominio + "cima_sei.jpg");
				setCaminhoImagemPadraoRodape(dominio + "baixo_sei.jpg");
			}
	        
	                

	        StringBuilder sb = new StringBuilder();
	        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
	        sb.append("<div class=\"Section1\">");
	        sb.append("<div>");
	        sb.append("<table class=\"MsoNormalTable\" style=\"mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
	        sb.append("<tbody>");
	        sb.append("<tr style=\"height: 59.25pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">");
	        sb.append("<td style=\"height: 59.25pt; padding: 0cm;\" colspan=\"1\">");
	        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1025\" src=\"" + getCaminhoImagemPadraoTopo() + "\"  width=\"750px;\" border=\"0\" /></span></p>");
	        sb.append("</td>");
	        sb.append("</tr>");
	        sb.append("<tr style=\"mso-yfti-irow:2\">");
	        sb.append("<td style=\"background: #FFFFFF; padding: 0cm;\" width=\"750px\">");
	        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
	        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
	        sb.append("</span></td>");
	        sb.append("</tr>");
	        sb.append("<tr style=\"height: 43.5pt; mso-yfti-irow: 4; mso-yfti-lastrow: yes;\">");
	        sb.append("<td style=\"height: 43.5pt; padding: 0cm;\" colspan=\"1\">");
	        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\"><img id=\"_x0000_i1028\" src=\"" + getCaminhoImagemPadraoRodape() + "\" width=\"750px;\" border=\"0\"/></span></p>");
	        sb.append("</td>");
	        sb.append("</tr>");
	        sb.append("</tbody>");
	        sb.append("</table>");
	        sb.append("</div>");
	        sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"color: black;\">&nbsp;</span></p>");
	        sb.append("</div>");
	        sb.append("</body></html>");
	        return sb.toString();
	    }
	 
	 
	    public void abrirComunicacaoInterna(ProspectsVO prospectsVO) throws Exception {
	    	setOncompleteModal("");
	    	
	    	setComunicacaoInternaVO(new ComunicacaoInternaVO()); 
	    	
	    	getComunicacaoInternaVO().setMensagem(getMensagemComLayout(""));
			  
			  ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();

			  if (prospectsVO != null && !prospectsVO.getCodigo().equals(0)) {
				try {
					 
					if (!Uteis.isAtributoPreenchido(prospectsVO.getEmailPrincipal())) {
						throw new Exception("Prospect sem email cadastrado");
					}					
					
					if (prospectsVO.getPessoa().getAluno()) {
						this.getComunicacaoInternaVO().setAluno(prospectsVO.getPessoa());
						this.getComunicacaoInternaVO().setAlunoNome(prospectsVO.getPessoa().getNome());
						this.getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.AL.name());
						comunicadoInternoDestinatarioVO.setDestinatario(prospectsVO.getPessoa());
						comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
						getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);
						getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
						setMensagemID("msg_dados_adicionados");
					}else {
						if (!Uteis.isAtributoPreenchido(prospectsVO.getPessoa())) {
							prospectsVO.getPessoa().setNome(prospectsVO.getNome());
							prospectsVO.getPessoa().setEmail(prospectsVO.getEmailPrincipal());
							prospectsVO.getPessoa().setEmail2(prospectsVO.getEmailSecundario());
						}
						
						this.getComunicacaoInternaVO().setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.TC.name());
						comunicadoInternoDestinatarioVO.setDestinatario(prospectsVO.getPessoa());
						comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(getComunicacaoInternaVO().getTipoComunicadoInterno());
						getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().add(comunicadoInternoDestinatarioVO);
						getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
						setMensagemID("msg_dados_adicionados");
				}
					
				 }catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				} 
			}
		}
	    	
	    public void enviarMensagemComunicadoInterno() {
			try {
				ComunicacaoInternaVO.validarDados(getComunicacaoInternaVO());
				getComunicacaoInternaVO().setTipoRemetente("FU");				
				
				if (!Uteis.isAtributoPreenchido(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getCodigo())) {
					enviarEmailProspect(getComunicacaoInternaVO());	
				}else {
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Incluir Comunicação Interna", "Incluindo");
					getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), null);
					registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Finalizando Incluir Comunicação Interna", "Incluindo");
					setOncompleteModal("RichFaces.$('panelEnvioEmailEnvMsg').hide()");
					setMensagemID("msg_mensagem_enviada", Uteis.SUCESSO);
					}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	    
	    public void enviarEmailProspect(ComunicacaoInternaVO obj) throws Exception{
	    	
	    	try {
	    		
	    	if (getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getEmail().contains(";")) {
	    		
	    		String [] emailDestinatario = new String [0];	    		
	    		emailDestinatario = getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getEmail().split(";");
				
	    		for (String objEmail : emailDestinatario) {
					
	    		
	    		EmailVO email = new EmailVO();
				
				email.setAnexarImagensPadrao(true);
				email.setEmailDest(objEmail);
				email.setNomeDest(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getNome());
				email.setEmailRemet(getConfiguracaoGeralPadraoSistema().getEmailRemetente());
				email.setNomeRemet(getConfiguracaoGeralPadraoSistema().getNomeRepositorio());
				email.setAssunto(getComunicacaoInternaVO().getAssunto());
				email.setMensagem(getComunicacaoInternaVO().getMensagem());
				email.setCaminhoAnexos(getComunicacaoInternaVO().getArquivoAnexo().getDescricao());
				email.setDataCadastro(new Date());
				registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Incluir Email", "Incluindo");
				getFacadeFactory().getEmailFacade().incluir(email);
	    		
	    		}
	    		
			}else {
		
			EmailVO email = new EmailVO();
			
			email.setAnexarImagensPadrao(true);
			email.setEmailDest(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getEmail());
			email.setNomeDest(getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().get(0).getDestinatario().getNome());
			email.setEmailRemet(getConfiguracaoGeralPadraoSistema().getEmailRemetente());
			email.setNomeRemet(getConfiguracaoGeralPadraoSistema().getNomeRepositorio());
			email.setAssunto(getComunicacaoInternaVO().getAssunto());
			email.setMensagem(getComunicacaoInternaVO().getMensagem());
			email.setCaminhoAnexos(getComunicacaoInternaVO().getArquivoAnexo().getDescricao());
			email.setDataCadastro(new Date());
			registrarAtividadeUsuario(getUsuarioLogado(), "ComunicacaoInternaControle", "Inicializando Incluir Email", "Incluindo");
			getFacadeFactory().getEmailFacade().incluir(email);
			
			}
	    	setOncompleteModal("RichFaces.$('panelEnvioEmailEnvMsg').hide()");
	    	 
	    	 setMensagemID("msg_mensagem_enviada", Uteis.SUCESSO);
	    	
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }

	    public String getControleAba() {
			if (controleAba == null) {
				controleAba = "";
			}
			return controleAba;
		}

	    public void setControleAba(String controleAba) {
			this.controleAba = controleAba;
		}
    
	    public String realizarNavegacaoPagina(String page) {
	    	return Uteis.getCaminhoRedirecionamentoNavegacao(page);
	    }
	    public Boolean getApresentarColunaModal(){	
			if (getTam() < 625 && getTam() > 0) {
				 return false; 
			 }	    
			  return true; 
		}

	    public Boolean getAppMovel() {				
			return getAcessandoAppMovel();
		}

		
		public Boolean getAcessandoAppMovel() {		
			String userAgent = context().getExternalContext().getRequestHeaderMap().get("User-Agent");
			return Uteis.ClienteMovel(userAgent);
		}
		
		public <T extends SuperVO> void  realizarVisualizacaoSeiLog(T obj, String nomeTabela, String key, String value) {
			try {
				getFacadeFactory().getLogTriggerInterfaceFacade().realizarVisualizacaoSeiLog(obj, nomeTabela, key, value);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}


	    private PixContaCorrenteVO pixVO;
	    
	    public PixContaCorrenteVO getPixVO() {
			if(pixVO == null) {
				pixVO = new PixContaCorrenteVO();
			}
			return pixVO;
		}

		public void setPixVO(PixContaCorrenteVO pixVO) {
			this.pixVO = pixVO;
		}
	    
	    public void realizarVisualizacaoPix(ContaReceberVO contaReceberVO) {
			try {
				setPixVO(getFacadeFactory().getPixContaCorrenteFacade().realizarVisualizacaoPix(contaReceberVO, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogadoClone()));
				setOncompleteModal("RichFaces.$('panelPix').show()");
			} catch (Exception e) {
				setOncompleteModal("");
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	    
	    public void realizarGeracaoPix() {
	    	try {
	    		setPixVO(getFacadeFactory().getPixContaCorrenteFacade().realizarGeracaoPix(getPixVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getPixVO().getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo()), getUsuarioLogadoClone()));
	    	} catch (Exception e) {
	    		setOncompleteModal("");
	    		setMensagemDetalhada("msg_erro", e.getMessage());
	    	}
	    }
	  
	    public static String getSizeChavePix(TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum) {
		   	   return tipoIdentificacaoChavePixEnum.isTelefone() ? "15" :
		   		      tipoIdentificacaoChavePixEnum.isCpfCnpj() ? "18" :   			   
		   			  tipoIdentificacaoChavePixEnum.isChaveAleatoria() ? "37" : "78" ;
		   	    
		      }
	    	    
	    public Gson inicializaGson() {
			return new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		}
	    
	    public Integer getCodigoAvaliacaoNaoRespondido() {
			if (codigoAvaliacaoNaoRespondido == null) {
				codigoAvaliacaoNaoRespondido = 0;
			}
			return codigoAvaliacaoNaoRespondido;
		}

		public void setCodigoAvaliacaoNaoRespondido(Integer codigoAvaliacaoNaoRespondido) {
			this.codigoAvaliacaoNaoRespondido = codigoAvaliacaoNaoRespondido;
		}
	    
		@Context
		private HttpServletRequest request;
		public static final String AUTHENTICATION_HEADER = "Authorization";

		public UsuarioVO autenticarConexao() throws Exception {
//			Enumeration<String> headerNames = request.getHeaderNames();
//			if (headerNames != null) {
//				while (headerNames.hasMoreElements()) {
//					System.out.println("Header: " + request.getHeader(headerNames.nextElement()));
//				}
//			}
			String credenciais = "";
			if (request.getHeader(AUTHENTICATION_HEADER) != null) {
				credenciais = request.getHeader(AUTHENTICATION_HEADER);
			} else if (request.getHeader("usuario") != null) {
				credenciais = request.getHeader("usuario");
			}
			AutenticadorWS autenticadorWS = new AutenticadorWS();
			UsuarioVO usuarioVO = autenticadorWS.autenticar(credenciais);
			if (usuarioVO == null) {
				throw new Exception(UteisJSF.internacionalizar("msg_AutenticacaoAplicativo_naoPossuiAutorizacao"));
			}
			return usuarioVO;
		}
		
		public void autenticarTokenCliente() throws Exception {
			String token64 = "";
			String tokenSha512 = "";
			if (request.getHeader(AUTHENTICATION_HEADER) != null) {		
				try {
					token64 = request.getHeader(AUTHENTICATION_HEADER);			
					token64 = new String(Base64.decode(token64.getBytes()));
				} catch (Exception e) {
					System.out.println("erro ao tentar converter token base 64 ");		
				}
				try {
					tokenSha512  = request.getHeader(AUTHENTICATION_HEADER);
				} catch (Exception e) {
					System.out.println("erro ao tentar converter token base sha 512 ");	
				}
				
			}
			String tokenSei = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarTokenWebServicePadraoSistema();
			String tokenSeiSha = Uteis.encriptarSenhaSHA512(tokenSei);			
			if (tokenSei.equals(token64) || tokenSeiSha.equals(tokenSha512)) {
				return; 
			}
			throw new Exception("Requisição não autorizada (Token Inválido)");			
		}
		
		
		
		public UsuarioVO obterUsuarioWebService() throws Exception {
			UsuarioVO usuarioVO = new UsuarioVO();
			if (request.getHeader(AUTHENTICATION_HEADER) != null && (request.getHeader(AUTHENTICATION_HEADER).contains("Basic") || request.getHeader(AUTHENTICATION_HEADER).contains("SHA-256"))) {
				usuarioVO = autenticarConexao();
			} else {
				autenticarTokenCliente();
				usuarioVO = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			}
			return usuarioVO;
		}
		

		private DashboardVO dashboardMatricular;
		private DashboardVO dashboardNovidadeDestaque;
		private DashboardVO dashboardBannerMarketing;
		private DashboardVO dashboardTwitter;
		private DashboardVO dashboardFavorito;
		private String caminhoArquivoBanner;
		

		public String getCaminhoArquivoBanner() {
			if (caminhoArquivoBanner == null) {
				caminhoArquivoBanner = "";
			}
			return caminhoArquivoBanner;
		}

		public void setCaminhoArquivoBanner(String caminhoArquivoBanner) {
			this.caminhoArquivoBanner = caminhoArquivoBanner;
		}
		

		public DashboardVO getDashboardMatricular() {
			if (dashboardMatricular == null) {
				if(getUsuarioLogado().getTipoVisaoAcesso().equals(TipoVisaoEnum.ADMINISTRATIVA)) {
					dashboardMatricular =  new DashboardVO(TipoDashboardEnum.BANNER_MATRICULA, false, 3, getUsuarioLogado().getTipoVisaoAcesso(), PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogadoClone());
				}else {
					dashboardMatricular = new DashboardVO(TipoDashboardEnum.BANNER_MATRICULA,
						getLoginControle().getCaminhoArquivoBanner().isEmpty(), 3, getUsuarioLogado().getTipoVisaoAcesso() , getUsuarioLogadoClone());
				}
			}
			return dashboardMatricular;
		}

		public void setDashboardMatricular(DashboardVO dashboardMatricular) {
			this.dashboardMatricular = dashboardMatricular;
		}
				

		public DashboardVO getDashboardTwitter() {
			if(dashboardTwitter == null) {
				if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					dashboardTwitter =  new DashboardVO(TipoDashboardEnum.TWITTER, false, 30, getUsuarioLogado().getTipoVisaoAcesso(), PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogadoClone());
				}else {
					dashboardTwitter =  new DashboardVO(TipoDashboardEnum.TWITTER, false, 30, getUsuarioLogado().getTipoVisaoAcesso(), getUsuarioLogadoClone());
				}
			}
			return dashboardTwitter;
		}

		public void setDashboardTwitter(DashboardVO dashboardTwitter) {
			this.dashboardTwitter = dashboardTwitter;
		}
		
		
		
		public DashboardVO getDashboardBannerMarketing() {
			if(dashboardBannerMarketing == null) {
				if(getUsuarioLogado().getTipoVisaoAcesso().equals(TipoVisaoEnum.ADMINISTRATIVA)) {
					dashboardBannerMarketing =  new DashboardVO(TipoDashboardEnum.MARKETING, false, 2, getUsuarioLogado().getTipoVisaoAcesso(), PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogadoClone());
				}else {
					dashboardBannerMarketing =  new DashboardVO(TipoDashboardEnum.MARKETING, false, 2, getUsuarioLogado().getTipoVisaoAcesso(), getUsuarioLogadoClone());
				}
			}
			return dashboardBannerMarketing;
		}

		public void setDashboardBannerMarketing(DashboardVO dashboardBannerMarketing) {
			this.dashboardBannerMarketing = dashboardBannerMarketing;
		}

		private String bannerMarketing;
		
		public String getBannerMarketing() {
			if(bannerMarketing == null) {
				bannerMarketing = "";
			}
			return bannerMarketing;
		}

		public void setBannerMarketing(String bannerMarketing) {
			this.bannerMarketing = bannerMarketing;
		}
		
		public void consultarBannerMarketing() {
			try {
				if(!getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() || (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno())) {
					setBannerMarketing(getFacadeFactory().getComunicacaoInternaFacade().consultarBannerComunicadoInternoMarketingDisponivelUsuario(getUsuarioLogado()));
				}
			} catch (Exception e) {
				setBannerMarketing("");
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}

		public DashboardVO getDashboardFavorito() {
			if(dashboardFavorito == null) {
			if(getUsuarioLogado().getTipoVisaoAcesso().equals(TipoVisaoEnum.ADMINISTRATIVA)) {
				dashboardFavorito =  new DashboardVO(TipoDashboardEnum.FAVORITOS, false, 0, getUsuarioLogado().getTipoVisaoAcesso(), PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogadoClone());
			}else {
				dashboardFavorito =  new DashboardVO(TipoDashboardEnum.FAVORITOS, false, 0, getUsuarioLogado().getTipoVisaoAcesso(), getUsuarioLogadoClone());
			}
			}
			return dashboardFavorito;
		}

		public void setDashboardFavorito(DashboardVO dashboardFavorito) {
			this.dashboardFavorito = dashboardFavorito;
		}
		
	    
	  		public String getLoginPearson() {
	  			if(loginPearson == null) {
	  				loginPearson ="";				
	  			} 
	  			return loginPearson;
	  		}

	  		public void setLoginPearson(String loginPearson) {
	  			this.loginPearson = loginPearson;
	  		}

	  		public String getTokenPearson() {
	  			if(tokenPearson == null ) {
	  				tokenPearson = "";				
	  			}		
	  			return tokenPearson;
	  		}

	  		public void setTokenPearson(String tokenPearson) {
	  			this.tokenPearson = tokenPearson;
	  		}
	  		
	  		public String getUrlPearson() {
	  			if(urlPearson == null ) {
	  				urlPearson   ="";			
	  			}		
	  			return urlPearson;
	  		}

	  		public void setUrlPearson(String urlPearson) {
	  			this.urlPearson = urlPearson;
	  		}
	  		
	  		
	  	public void realizarCriacaoAcessoBibliotecaPierson() {
	  		try {
	  			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getUsuarioLogado().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	  			if (Uteis.isAtributoPreenchido(getUsuarioLogado().getPessoa().getCodigo()) &&  Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO.getCodigo())) {	  			
	  			//	ConfiguracaoBibliotecaVO configuracaoBibilioteca =  getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoBibliotecaBVPearson(); 
	  				ConfiguracaoBibliotecaVO configuracaoBibilioteca =  getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao();	  				
	  				setLoginPearson(pessoaEmailInstitucionalVO.getEmail());
	  				setUrlPearson(configuracaoBibilioteca.getLinkAcessoBVPerson());
	  				String chave = getLoginPearson() + configuracaoBibilioteca.getChaveTokenBVPerson();		
	  				setTokenPearson(Uteis.encriptarMD5(chave));
	  			}else {
	  				String msg = "<p>Falha ao acessar a plataforma  Biblioteca Pearson. Procure a instituição de ensino para maiores informações. </p><p></p>";
	      			//ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	      			String urlStr = getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao() + "/paginaErroCustomizada.xhtml?msg=" + msg;
	      			URL url= new URL(urlStr);
	      		    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
	      		    setUrlPearson(urlStr=uri.toASCIIString());
	  			}
	  		} catch (UnsupportedEncodingException e) {				
	  			e.printStackTrace();
	  		} catch (Exception e) {				
	  			e.printStackTrace();			
	  		}
	  	}
	  	
	    public void resetarMemoriaFlash() {
	    	context().getExternalContext().getFlash().clear();
	    }	  	
	    
	    
	    public boolean validarAssinaturaDigitalHabilitada(int unidadeEnsinoCurso , int contratoMatricula) throws Exception {
			Boolean habilidado = false;
			ConfiguracaoGEDVO configuracaoGEDUnidadeEnsino = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(unidadeEnsinoCurso, false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(contratoMatricula)) {
				TextoPadraoVO contratoMatriculaPeriodo = getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(contratoMatricula, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				habilidado = (Uteis.isAtributoPreenchido(contratoMatriculaPeriodo) && contratoMatriculaPeriodo.getAssinarDigitalmenteTextoPadrao()) && (Uteis.isAtributoPreenchido(configuracaoGEDUnidadeEnsino) && configuracaoGEDUnidadeEnsino.getConfiguracaoGedContratoVO().getAssinarDocumento());
				setProvedorDeAssinaturaEnum(configuracaoGEDUnidadeEnsino.getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum());				
				return habilidado;
			}else {
				return habilidado;
			}
		}
	    
	    public List<SelectItem> getListaTipoLayoutHistorico(String tipoLayout, String nivelEducacional) throws Exception {
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			List<ConfiguracaoLayoutHistoricoVO> configuracaoLayoutHistoricoVOs =  null;
			try {
				if(tipoLayout == null || !tipoLayout.equals("PADRAO")) {
					configuracaoLayoutHistoricoVOs = getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorNivelEducacional(TipoNivelEducacional.getEnum(nivelEducacional), getUsuarioLogado());
				}
				if(configuracaoLayoutHistoricoVOs != null && !configuracaoLayoutHistoricoVOs.isEmpty()) {
					itens = UtilSelectItem.getListaSelectItem(configuracaoLayoutHistoricoVOs.stream().filter(t -> !t.getOcultarLayout()).collect(Collectors.toList()), "chaveUsar", "descricao", false);
					if(configuracaoLayoutHistoricoVOs.stream().anyMatch(t -> t.getLayoutPadrao())) {
						setTipoLayoutHistorico(configuracaoLayoutHistoricoVOs.stream().filter(t -> t.getLayoutPadrao()).findFirst().get().getChaveUsar());
					}else {
						setTipoLayoutHistorico(configuracaoLayoutHistoricoVOs.stream().findFirst().get().getChaveUsar());
					}
				}else if(configuracaoLayoutHistoricoVOs == null || configuracaoLayoutHistoricoVOs.isEmpty()) {
				if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX") || nivelEducacional.equals("MT")) {
					itens.add(new SelectItem("HistoricoAlunoPosRel", "Layout 1 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos2Rel", "Layout 2 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos3Rel", "Layout 3 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos4Rel", "Layout 4 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout3Rel", "Layout 5 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos6Rel", "Layout 6 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout10Rel", "Layout 10 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout11Rel", "Layout 11 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoResidenciaMedicaRel", "Layout 12 - Residência Médica"));
					itens.add(new SelectItem("HistoricoAlunoPos12Rel", "Layout 13 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos15Rel", "Layout 15 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoPos16Rel", "Layout 16 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos17Rel", "Layout 17 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos18Rel", "Layout 18 - Pós Graduação"));
					itens.add(new SelectItem("HistoricoAlunoPos3PercentualIntegralizacaoRel", "Layout 3 - Pós Graduação % Integralização"));
					if (nivelEducacional.equals("MT")) {
						itens.add(new SelectItem("HistoricoAlunoLayout23PortariaMECRel", "Layout 23 - Mestrado - Portaria (1095)"));
					}
					if (!tipoLayout.equals("HistoricoAlunoPosRel")
							&& !tipoLayout.equals("HistoricoAlunoPos2Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos3Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos4Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout3Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos6Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout10Rel")
							&& !tipoLayout.equals("HistoricoAlunoResidenciaMedicaRel")
							&& !tipoLayout.equals("HistoricoAlunoLayout11Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos15Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos16Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos17Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos18Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos3PercentualIntegralizacaoRel")){
						setTipoLayoutHistorico("HistoricoAlunoPosRel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
				} else if (nivelEducacional.equals("PO") || nivelEducacional.equals("EX") || nivelEducacional.equals("MT")) {
					itens.add(new SelectItem("HistoricoAlunoPosRel", "Layout 1 - Pós Graduação"));		
					if (nivelEducacional.equals("MT")) {
						itens.add(new SelectItem("HistoricoAlunoLayout23PortariaMECRel", "Layout 23 - Mestrado - Portaria (1095)"));
					}
					setTipoLayoutHistorico("HistoricoAlunoPosRel");
				} else if (nivelEducacional.equals("ME")) {
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedio", "Layout 1 - Ensino Médio"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout2Rel", "Layout 2 - Ensino Médio"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout3Rel", "Layout 3 - Ensino Médio"));
					if (!tipoLayout.equals("HistoricoAlunoEnsinoMedio") && !tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout2Rel") && !tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout3Rel")) {
						setTipoLayoutHistorico("HistoricoAlunoEnsinoMedio");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
				} else if (nivelEducacional.equals("BA")) {
					itens.add(new SelectItem("HistoricoAlunoEnsinoBasicoLayout1Rel", "Layout 1 - Ensino Fundamental"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoBasicoLayout2Rel", "Layout 2 - Ensino Fundamental"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout3Rel",  "Layout 3 - Ensino Fundamental"));
					if (!tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout1Rel") && !tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
						setTipoLayoutHistorico("HistoricoAlunoEnsinoBasicoLayout1Rel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
					//itens.add(new SelectItem("HistoricoAlunoPos12Rel", "Layout 13 - Pós Graduação"));					
				} else if (nivelEducacional.equals("PR")) {
					itens.add(new SelectItem("HistoricoAlunoNivelTecnicoRel", "Layout 1 - Nível Técnico"));
					itens.add(new SelectItem("HistoricoAlunoNivelTecnico2Rel", "Layout 2 - Nível Técnico"));
					itens.add(new SelectItem("HistoricoAlunoLayout3Rel", "Layout 3 - Nível Tecnico"));
					itens.add(new SelectItem("HistoricoAlunoNivelTecnicoLayout4Rel", "Layout 4 - Nível Tecnico"));
					itens.add(new SelectItem("HistoricoAlunoLayout6Rel", "Layout 6 - Nível Técnico"));
					if (!tipoLayout.equals("HistoricoAlunoNivelTecnicoRel") 
							&& !tipoLayout.equals("HistoricoAlunoNivelTecnico2Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout3Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout6Rel") && !tipoLayout.equals("HistoricoAlunoNivelTecnicoLayout4Rel")) {
						setTipoLayoutHistorico("HistoricoAlunoNivelTecnicoRel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}							
				} else if (nivelEducacional.equals("MT")) {
					itens.add(new SelectItem("HistoricoAlunoPosRel", "Layout 1 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoPos2Rel", "Layout 2 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoPos2Rel", "Layout 2 - Mestrado"));			
					itens.add(new SelectItem("HistoricoAlunoPos3Rel", "Layout 3 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoPos4Rel", "Layout 4 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoRel", "Layout 5 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout2Rel", "Layout 6 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout2PortariaMECRel", "Layout 6 - Mestrado - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout3Rel", "Layout 7 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout4Rel", "Layout 8 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout4PortariaMECRel", "Layout 8 - Mestrado - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout5Rel", "Layout 9 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout5PortariaMECRel", "Layout 9 - Mestrado - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout6Rel", "Layout 10 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout7Rel", "Layout 11 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout7PortariaMECRel", "Layout 11 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout8Rel", "Layout 12 - Certidão de Estudos"));
					itens.add(new SelectItem("HistoricoAlunoLayout9Rel", "Layout 13 - Logo Município"));
					itens.add(new SelectItem("HistoricoAlunoLayout23PortariaMECRel", "Layout 23 - Mestrado - Portaria (1095)"));
					if (!tipoLayout.equals("HistoricoAlunoRel") && !tipoLayout.equals("HistoricoAlunoLayout2Rel") &&
							!tipoLayout.equals("HistoricoAlunoLayout3Rel") && !tipoLayout.equals("HistoricoAlunoLayout4Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout5Rel") && !tipoLayout.equals("HistoricoAlunoLayout6Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout8Rel") && !tipoLayout.equals("HistoricoAlunoLayout9Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout7Rel") && !tipoLayout.equals("HistoricoAlunoPosRel")
							&& !tipoLayout.equals("HistoricoAlunoPos2Rel") && !tipoLayout.equals("HistoricoAlunoPos3Rel")
							&& !tipoLayout.equals("HistoricoAlunoPos4Rel") && !tipoLayout.equals("HistoricoAlunoLayout5PortariaMECRel") && !tipoLayout.equals("HistoricoAlunoLayout23PortariaMECRel")) {
						setTipoLayoutHistorico("HistoricoAlunoPosRel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
					itens.add(new SelectItem("HistoricoAlunoLayout10Rel", "Layout 14 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout11Rel", "Layout 15 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout11PortariaMECRel", "Layout 15 - Mestrado - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoResidenciaMedicaRel", "Layout 16 - Mestrado - Residência Médica"));
					itens.add(new SelectItem("HistoricoAlunoPos6Rel", "Layout 17 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoPos12Rel", "Layout 18 - Mestrado"));
				} else if (nivelEducacional.equals("ME")) {
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedio", "Layout 1 - Ensino Médio"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout2Rel", "Layout 2 - Ensino Médio"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout3Rel", "Layout 3 - Ensino Médio"));
					if (!tipoLayout.equals("HistoricoAlunoEnsinoMedio") && !tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout2Rel") && !tipoLayout.equals("HistoricoAlunoEnsinoMedioLayout3Rel")) {
						setTipoLayoutHistorico("HistoricoAlunoEnsinoMedio");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
					itens.add(new SelectItem("HistoricoAlunoEnsinoMedioLayout3Rel", "Layout 3 - Ensino Médio"));			
				} else if (nivelEducacional.equals("BA")) {
					itens.add(new SelectItem("HistoricoAlunoEnsinoBasicoLayout1Rel", "Layout 1 - Ensino Fundamental"));
					itens.add(new SelectItem("HistoricoAlunoEnsinoBasicoLayout2Rel", "Layout 2 - Ensino Fundamental"));
					if (!tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout1Rel") && !tipoLayout.equals("HistoricoAlunoEnsinoBasicoLayout2Rel")) {
						setTipoLayoutHistorico("HistoricoAlunoEnsinoBasicoLayout1Rel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
				} else if (nivelEducacional.equals("PR")) {
					itens.add(new SelectItem("HistoricoAlunoNivelTecnicoRel", "Layout 1 - Nível Técnico"));
					setTipoLayoutHistorico("HistoricoAlunoNivelTecnicoRel");
				} else if (nivelEducacional.equals("MT")) {
					itens.add(new SelectItem("HistoricoAlunoRel", "Layout 1 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout2Rel", "Layout 2 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout3Rel", "Layout 3 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout4Rel", "Layout 4 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout5Rel", "Layout 5 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout5PortariaMECRel", "Layout 5 - Mestrado - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout6Rel", "Layout 6 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout7Rel", "Layout 7 - Mestrado"));
					itens.add(new SelectItem("HistoricoAlunoLayout8Rel", "Layout 8 - Certidão de Estudos"));
					itens.add(new SelectItem("HistoricoAlunoLayout9Rel", "Layout 9 - Logo Município"));
					itens.add(new SelectItem("HistoricoAlunoLayout23PortariaMECRel", "Layout 23 - Mestrado - Portaria (1095)"));
					if (!tipoLayout.equals("HistoricoAlunoRel") && !tipoLayout.equals("HistoricoAlunoLayout2Rel") && !tipoLayout.equals("HistoricoAlunoLayout3Rel") && !tipoLayout.equals("HistoricoAlunoLayout4Rel") && !tipoLayout.equals("HistoricoAlunoLayout5Rel") && !tipoLayout.equals("HistoricoAlunoLayout6Rel") && !tipoLayout.equals("HistoricoAlunoLayout8Rel") && !tipoLayout.equals("HistoricoAlunoLayout9Rel") && !tipoLayout.equals("HistoricoAlunoLayout7Rel") && !tipoLayout.equals("HistoricoAlunoLayout5PortariaMECRel") && !tipoLayout.equals("HistoricoAlunoLayout23PortariaMECRel")) {
						setTipoLayoutHistorico("HistoricoAlunoRel");
					}else {
						setTipoLayoutHistorico(tipoLayout);
					}
					itens.add(new SelectItem("HistoricoAlunoEnsinoBasicoLayout2Rel", "Layout 2 - Ensino Fundamental"));						
				} else {
					itens.add(new SelectItem("HistoricoAlunoRel", "Layout 1 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayoutPortariaMECRel", "Layout 1 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout2Rel", "Layout 2 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout2PortariaMECRel", "Layout 2 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout3Rel", "Layout 3 - Graduação" ));
					itens.add(new SelectItem("HistoricoAlunoLayout3PortariaMECRel", "Layout 3 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout4Rel", "Layout 4 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout4PortariaMECRel", "Layout 4 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout5Rel", "Layout 5 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout5PortariaMECRel", "Layout 5 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout6Rel", "Layout 6 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout6PortariaMECRel", "Layout 6 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout7Rel", "Layout 7 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout7PortariaMECRel", "Layout 7 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout8Rel", "Layout 8 - Certidão de Estudos"));
					itens.add(new SelectItem("HistoricoAlunoLayout8PortariaMECRel", "Layout 8 - Certidão de Estudos - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout9Rel", "Layout 9 - Logo Município"));
					itens.add(new SelectItem("HistoricoAlunoLayout9PortariaMECRel", "Layout 9 - Logo Município - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout10Rel", "Layout 10 - Histórico de Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout10PortariaMECRel", "Layout 10 - Histórico de Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout11Rel", "Layout 11 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout11PortariaMECRel", "Layout 11 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout12Rel", "Layout 12 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout12PortariaMECRel", "Layout 12 - Graduação - Portaria (1095)"));

					itens.add(new SelectItem("HistoricoAlunoLayout12MedicinaRel", "Layout 12 - Graduação Medicina"));
					itens.add(new SelectItem("HistoricoAlunoLayout12MedicinaPortariaMECRel", "Layout 12 - Graduação Medicina - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout12MedicinaAdaptacaoMatrizCurricularRel", "Layout 12 - Graduação Medicina - Adaptação de Matriz Curricular"));

					itens.add(new SelectItem("HistoricoAlunoLayout13Rel", "Layout 13 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout13PortariaMECRel", "Layout 13 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout14Rel", "Layout 14 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout14PortariaMECRel", "Layout 14 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout15Rel", "Layout 15 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout15PortariaMECRel", "Layout 15 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout16Rel", "Layout 16 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout16PortariaMECRel", "Layout 16 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout17Rel", "Layout 17 - Graduação"));
					itens.add(new SelectItem("HistoricoAlunoLayout17PortariaMECRel", "Layout 17 - Graduação - Portaria (1095)"));

					itens.add(new SelectItem("HistoricoAlunoLayout18PortariaMECRel", "Layout 18 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout19PortariaMECRel", "Layout 19 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout20PortariaMECRel", "Layout 20 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout21PortariaMECRel", "Layout 21 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout22PortariaMECRel", "Layout 22 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout24GraduacaoPortariaMEC", "Layout 24 - Graduação - Portaria (1095)"));
					itens.add(new SelectItem("HistoricoAlunoLayout24Graduacao", "Layout 24 - Graduação"));
					
					if (!tipoLayout.equals("HistoricoAlunoRel") && !tipoLayout.equals("HistoricoAlunoLayout2Rel") && !tipoLayout.equals("HistoricoAlunoLayout3Rel") 
							&& !tipoLayout.equals("HistoricoAlunoLayout4Rel") && !tipoLayout.equals("HistoricoAlunoLayout5Rel") 
							&& !tipoLayout.equals("HistoricoAlunoLayout6Rel") && !tipoLayout.equals("HistoricoAlunoLayout7Rel") 
							&& !tipoLayout.equals("HistoricoAlunoLayout8Rel") && !tipoLayout.equals("HistoricoAlunoLayout9Rel") 
							&& !tipoLayout.equals("HistoricoAlunoLayout10Rel") && !tipoLayout.equals("HistoricoAlunoLayout11Rel") 
							&& !tipoLayout.equals("HistoricoAlunoLayout12Rel")
							&& !tipoLayout.equals("HistoricoAlunoLayout13Rel") && !tipoLayout.equals("HistoricoAlunoLayout14Rel")
									&& !tipoLayout.equals("HistoricoAlunoLayout15Rel") && !tipoLayout.equals("HistoricoAlunoLayout16Rel")
									&& !tipoLayout.equals("HistoricoAlunoLayout17Rel") && !tipoLayout.equals("HistoricoAlunoLayout5PortariaMECRel") 
									&& !tipoLayout.equals("HistoricoAlunoLayout2PortariaMECRel") && !tipoLayout.equals("HistoricoAlunoLayout4PortariaMECRel")
									&& !tipoLayout.equals("HistoricoAlunoLayout7PortariaMECRel") && !tipoLayout.equals("HistoricoAlunoLayout11PortariaMECRel")
									&& !tipoLayout.equals("HistoricoAlunoLayout21PortariaMECRel") && !tipoLayout.equals("HistoricoAlunoLayout22PortariaMECRel")) {
						setTipoLayoutHistorico("HistoricoAlunoRel");
					} else {
						//itens.add(new SelectItem("HistoricoAlunoLayout17Rel", "Layout 17 - Graduação"));			
					}
				
			}
				}
			
			if(Uteis.isAtributoPreenchido(tipoLayout)) {
				boolean existeLayout = false;
				for(SelectItem item : itens) {
					if(item.getValue().equals(tipoLayout)) {
						existeLayout = true;
						break;
					}
				}
				if(!existeLayout) {
					if(itens.isEmpty()) {
						setTipoLayoutHistorico("");
					}else {
						setTipoLayoutHistorico((String)itens.get(0).getValue());
					}
				}else {
					setTipoLayoutHistorico(tipoLayout);
				}
			}
			} catch (Exception e) {
				throw e;
			}
			return itens;
		}
	    
		public String getTipoLayoutAtaResultadosFinais() {
			if (tipoLayoutAtaResultadosFinais == null) {
				tipoLayoutAtaResultadosFinais = "";
			}
			return tipoLayoutAtaResultadosFinais;
		}

		public void setTipoLayoutAtaResultadosFinais(String tipoLayoutAtaResultadosFinais) {
			this.tipoLayoutAtaResultadosFinais = tipoLayoutAtaResultadosFinais;
		}
	    
	    
	    
	    public List realizarConsultaAutocompleteaRegistroAcademico(Object suggest) {					
			try {
			   	return 	 getFacadeFactory().getMatriculaFacade().consultarPorRegistroAcademicoMatriculaAutoComplete( (String) suggest.toString() ,getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					
			} catch (Exception e) {
				return new ArrayList(0);
		
			}
       }
	
		public List realizarConsultaAutocompleteMatricula(Object suggest) {					
			try {
				return   getFacadeFactory().getMatriculaFacade().consultarPorRegistroAcademicoMatriculaAutoComplete( (String) suggest.toString() ,getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo(), false,	Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			
			} catch (Exception e) {
				return new ArrayList(0);
				
			}
		}		
		
	
		public List<SelectItem> getComboBoxConsultaComboFormaPadraoDataBaseCartaoRecorrente() {
			List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO, FormaPadraoDataBaseCartaoRecorrenteEnum.DIA_FIXO.getValorApresentar()));
			itens.add(new SelectItem(FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO, FormaPadraoDataBaseCartaoRecorrenteEnum.VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO.getValorApresentar()));
			return itens;
	    }
		
		public boolean isExisteArtefatoAjuda() {
			try {
				return getAplicacaoControle().getArtefatoAjudaKeys().containsKey(getNomeTelaAtualTratado());
			}catch (Exception e) {
				return false;
			}
		}
		
		public String getNomeTelaAtualTratado() {
			try {
				if(context() != null && context().getViewRoot() != null){
					return context().getViewRoot().getViewId().substring(context().getViewRoot().getViewId().lastIndexOf("/")+1, context().getViewRoot().getViewId().indexOf(".xhtml"));	
				}
				return Constantes.EMPTY;
			}catch (Exception e) {
				return Constantes.EMPTY;
			}
		}
		
		private String caminhoPreview;
		
		public String getCaminhoPreview() {
			if (caminhoPreview == null) {
				caminhoPreview = "";
			}
			return caminhoPreview;
		}
		
		public void setCaminhoPreview(String caminhoPreview) {
			this.caminhoPreview = caminhoPreview;
		}
		
		private final String EXTENSAO_PDF = ".pdf";
		
		
		public Boolean getCaminhoPreviewTipoPdf() {
			return getCaminhoPreview().toLowerCase().contains(EXTENSAO_PDF);
		}
		
		public void realizarVisualizacaoPreview(ArquivoVO arquivoVO) {
			try {			
				setCaminhoPreview(getFacadeFactory().getArquivoFacade().realizarVisualizacaoPreview(arquivoVO));			
				inicializarMensagemVazia();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public Boolean getLocalHost() {
			try {
				return (((String) request().getHeader("host")) != null && ((String) request().getHeader("host")) != "" ? ((String) request().getHeader("host")).contains("localhost") : false) || (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema()) && Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()) && getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao().contains("http:") && !getAppMovel());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
				return false;
			}
		}
		

		public DashboardVO getDashboardNovidadeDestaque() {
			if(dashboardNovidadeDestaque == null) {
				dashboardNovidadeDestaque =  new DashboardVO(TipoDashboardEnum.NOVIDADE_DESTAQUE, false, -1, getUsuarioLogado().getTipoVisaoAcesso(), PerfilAcessoModuloEnum.ADMINISTRATIVO, getUsuarioLogadoClone());
			}
			return dashboardNovidadeDestaque;
		}

		public void setDashboardNovidadeDestaque(DashboardVO dashboardNovidadeDestaque) {
			this.dashboardNovidadeDestaque = dashboardNovidadeDestaque;
		}
		
		public String getUrlDownloadSV() {
			if(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
				return "abrirPopup('"+getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()+"/DownloadSV', 'DownloadSV_"+ new Date().getTime() +"', 950,595)";
			}else if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				return "location.href='../../DownloadSV'";
			}else {
				return "location.href='../DownloadSV'";
			}
		}
		
		public ControleConsultaCidade getControleConsultaCidade() {
			if (controleConsultaCidade == null) {
				controleConsultaCidade = new ControleConsultaCidade();
				controleConsultaCidade.setLimitePorPagina(10);
				controleConsultaCidade.setPaginaAtual(0);
				controleConsultaCidade.setPage(0);
			}
			return controleConsultaCidade;
		}
		
		public void setControleConsultaCidade(ControleConsultaCidade controleConsultaCidade) {
			this.controleConsultaCidade = controleConsultaCidade;
		}
}

