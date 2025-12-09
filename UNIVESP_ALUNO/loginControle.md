package controle.arquitetura;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



//import org.richfaces.component.UIPanelMenu;
import org.primefaces.component.panelmenu.PanelMenu;

//import org.richfaces.component.UIToolbar;
import org.primefaces.component.toolbar.Toolbar;

//import org.richfaces.event.DropEvent;
import org.primefaces.event.DragDropEvent;

//import org.richfaces.event.FileUploadEvent;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.stereotype.Controller;

import com.google.common.base.Strings;

import controle.academico.VisaoAlunoControle;
import controle.administrativo.ComunicacaoInternaControle;
import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
//import controle.administrativo.NovidadeSeiControle;
//import controle.protocolo.RequerimentoControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.FavoritoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.DataComemorativaVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
//import negocio.comuns.compras.RequisicaoVO;
//import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("LoginControle")
@Scope("session")
@Lazy
public class LoginControle extends QuestionarioRespostaControle implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8976279296789370114L;
	private String mensagemTelaLogin;
	private String username;
	private String senha;
	private String nome;
	private String novaSenha;
	private String novaSenha2;
	private String nomeUnidadeEnsino;
	private String opcao;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private String dataLogin;
	private String dataUltimoAcesso;
	private String diaSemanaLogin;
	private String diaSemanaLoginSemHora;
	protected PermissaoAcessoMenuVO permissaoAcessoMenuVO;
	private FuncionarioVO funcionarioVO;
	private Boolean mostrarBotaoMudarSenha;
	private Boolean apresentarRichNotificaoEmailAcimaPermitido;
	private Boolean apresentarRequisicoes;
	private PanelMenu menuPanelMenu;
	private Toolbar menuToolBar;
	private Boolean possuiItemMenu;
	private Boolean possuiSubGrupo;
	private Boolean acesso;
	private Boolean acessoItemMenu;
	private Integer codUnidadeEnsino;
	private Boolean visualizarComboUnidadeEnsino;
	private Boolean mostrarVisaoAlunoNoModal;
	private Boolean mostrarVisaoPaisNoModal;
	private Boolean mostrarVisaoProfessorNoModal;
	private Boolean mostrarVisaoFuncionarioNoModal;
	private Boolean mostrarVisaoFuncionarioNoModalMultiCampos;
	private Boolean mostrarVisaoAdministradorNoModal;
	private Boolean mostrarVisaoCoordenadorNoModal;
	private Boolean apresentarListaUnidadeEnsinoCoordenador;
	private Boolean mostrarVisaoVisitante;
	private Boolean ocultarBotoes;
	protected List<SelectItem> listaSelectItemUnidadeEnsinoCoordenador;
	private Integer codUnidadeEnsinoCoordenador;
	private Boolean mostrarModalEscolhaVisao;
	private Boolean apresentarModalPerfilProfessor;
	private String cpf;
	private String redefinirSenhaPor;
	protected ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private Boolean possuiArquivoInstitucional;
	private List<SelectItem> listaSelectItemPerfilProfessorGraduacaoPos;
	private List listaDataComemorativa;
	private Integer qtdComunicacaoInternaPendente;
	private int posicao;
	private Integer qtdRequerimentoPendente;
	private String abrirPopup;
	private String tituloTelaBancoCurriculum;
	private String tituloTelaBuscaCandidato;
	private String urlImagemApresentarDataComemorativa;
//	private ParceiroVO parceiroVO;
	private Date dataApresentacaoDataComemorativa;
	private Boolean existeNovidadeSei;
	private Integer qtdeAtualizacaoOuvidoria;
	private String urlLogoUnidadeEnsino;
	private String urlLogoIndexUnidadeEnsino;
	private String urlLogoUnidadeEnsinoRelatorio;
	private String smtpPadrao;
	private String ipServidor;
	private Authenticator auth;
	private String loginServidorSmtp;
	private String senhaServidorSmtp;
	private String portaSmtpPadrao;
	private String emailRemetente;
	private List<File> listaFileCorpoMensagem;
	private List<String> listaAnexosExcluir;
	private String codigoEmails;
	private String urlLogoPaginaInicialApresentar;
	private Boolean disponibilizarResultadoAvaliacaoInstitucional;
	private Boolean isMinhaBibliotecaHabilitado;
	private Boolean acessandoBiblioteca;

	private Boolean abrirModalQuestionario;
//	private RequisicaoVO requisicaoVO;
//	private List<RequisicaoVO> requisicoesVOs;

	private static final String DESIGN_RELATORIO_QUESTIONARIO = "RequisicaoAnaliticoQuestionarioRel";

	private Boolean isLexMagisterHabilitado;
	private Boolean isBibliotecaBvPearsonHabilitado;
	private String emailInstitucional;
	private Boolean logadoComEmailInstitucional;
	private Boolean apresentarRedefinirSenha;
	private DashboardVO dashboardLinksUteis;
	private List<LinksUteisVO> listaLinksUteisVOs;
	
	private Boolean existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao;
	private PessoaVO pessoaVO;

	public LoginControle() {
		inicializarObjetoParceiroVO();
		setApresentarRedefinirSenha(false);
	}

	public void preViewRender() {
		if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue") != null
				|| (context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino") != null
						&& (Integer) context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino") > 0)) {
			inicializarDadosUnidadeEnsinoPadrao();
		}
	}

	@PostConstruct
	public void init() {
		preViewRender();
		if (context().getExternalContext().getSessionMap().get("usuarioExterno") != null) {
			validarLoginExternoServlet();
		}
		String token = (String) ((HttpServletRequest) context().getCurrentInstance().getExternalContext().getRequest())
				.getAttribute("token");
		if (token != null) {

		}
		if (!Uteis.isAtributoPreenchido(getMensagemDetalhada())) {
			setMensagemID("msg_entre_prmlogin");
		}
	}

	public void inicializarDadosUnidadeEnsinoPadrao() {
		try {
			UnidadeEnsinoVO ue = new UnidadeEnsinoVO();
			if (((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue") != null) {
				ue.setCodigo(Integer.parseInt(
						((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("ue")));
				context().getExternalContext().getSessionMap().put("codigoUnidadeEnsino", ue.getCodigo());
			} else {
				ue.setCodigo((Integer) context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino"));
			}
			if (Uteis.isAtributoPreenchido(ue.getCodigo())) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(ue, NivelMontarDados.BASICO, new UsuarioVO());
				if (ue.getExisteLogo()) {
					setUrlLogoUnidadeEnsino(
							ue.getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogo());
				}
				if (ue.getExisteLogoIndex()) {
					setUrlLogoIndexUnidadeEnsino(
							ue.getCaminhoBaseLogoIndex().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoIndex());
				}
				if (ue.getExisteLogoRelatorio()) {
					setUrlLogoUnidadeEnsinoRelatorio(ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/"
							+ ue.getNomeArquivoLogoRelatorio());
				}
				if (ue.getExisteLogoPaginaInicial()) {
					setUrlLogoPaginaInicialApresentar(ue.getCaminhoBaseLogoPaginaInicial().replaceAll("\\\\", "/") + "/"
							+ ue.getNomeArquivoLogoPaginaInicial());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setUrlLogoUnidadeEnsino("");
			setUrlLogoIndexUnidadeEnsino("");
		}
	}

	private void validarLoginExternoServlet() {
		try {
			setUsername((String) context().getExternalContext().getSessionMap().get("usuarioExterno"));
			setSenha((String) context().getExternalContext().getSessionMap().get("senhaExterna"));
			String ambiente = ((String) context().getExternalContext().getSessionMap().get("ambienteExterna"));

			String pagina = getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao();
			String opcao = login3();
			if (getMostrarModalEscolhaVisao() && opcao.isEmpty()) {
				if (Uteis.isAtributoPreenchido(ambiente) && ambiente.equals("aluno")) {
					opcao = logarDiretamenteComoAluno();
				} else if (Uteis.isAtributoPreenchido(ambiente) && ambiente.equals("professor")) {
					if (!getUsuario().getTipoUsuario().equals("DM")) {
						opcao = logarDiretamenteComoProfessor(false);
					}
				} else if (Uteis.isAtributoPreenchido(ambiente) && ambiente.equals("coordenador")) {
					opcao = logarDiretamenteComoCoordenador();
				} else if (Uteis.isAtributoPreenchido(ambiente) && ambiente.equals("funcionario")) {
					opcao = logarDiretamenteComoFuncionario();
				} else if (Uteis.isAtributoPreenchido(ambiente) && ambiente.equals("diretorMultiCampus")) {
					opcao = logarDiretamenteComoDiretorMultiCampus();
				}
			}
			if (opcao.contains("telaInicialVisaoAluno")) {
				pagina += "/visaoAluno/telaInicialVisaoAluno.xhtml";
			} else if (opcao.contains("telaInicialVisaoProfessor")) {
				pagina += "/visaoProfessor/telaInicialVisaoProfessor.xhtml";
			} else if (opcao.contains("telaInicialVisaoCoordenador")) {
				pagina += "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
			} else if (opcao.contains("homeAdministrador")) {
				pagina += "/visaoAdministrativo/administrativo/homeAdministrador.xhtml";
			} else if (opcao.contains("telaInicialVisaoParceiro")) {
				pagina += "/visaoParceiro/telaInicialVisaoParceiro.xhtml";
			} else if (opcao.contains("avaliacaoInstitucionalQuestionario")) {
				pagina += "/visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml";
			} else {
				pagina += "/index.xhtml";
			}
			((HttpServletResponse) context().getExternalContext().getResponse()).sendRedirect(pagina);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getIsAmbienteProducao() {
		return Uteis.isAmbienteProducao();
	}

	public String loginSimulacaoSuporte() {
		return this.loginSistema(true, false);
	}

	public String login3() {
		if (Uteis.isAtributoPreenchido(getEmailInstitucional()) && !getLogadoComEmailInstitucional()) {
			return this.loginAD();
		}
		return loginSistema(false, false);
	}

	public String loginSistemaBiblioteca() {
		setAcessandoBiblioteca(Boolean.TRUE);
		return loginSistema(false, false);
	}

	public String loginSistema(Boolean simulacaoLogin, Boolean encerarSimulacao) {
		visualizarComboUnidadeEnsino = false;
		mostrarVisaoAlunoNoModal = false;
		mostrarVisaoPaisNoModal = false;
		mostrarVisaoProfessorNoModal = false;
		mostrarVisaoFuncionarioNoModal = false;
		mostrarVisaoFuncionarioNoModalMultiCampos = false;
		mostrarVisaoAdministradorNoModal = false;
		mostrarVisaoCoordenadorNoModal = false;
		apresentarListaUnidadeEnsinoCoordenador = false;
		mostrarModalEscolhaVisao = false;
		apresentarModalPerfilProfessor = false;
		setDisponibilizarResultadoAvaliacaoInstitucional(null);

		String tipoPessoa = null;
		String mensagemErroSenha = "";
		try {
			if (getFacadeFactory() == null) {
				HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
				session.invalidate();
				removerObjetoMemoria(this);
				return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
			}

			//
			// NUNCA COMENTAR ESSE METODO E COMITAR, IRï¿½ GERAR PROBLEMAS PARA
			// CONTROLAR O FINANCEIRO DOS CLIENTES.
			//
			if (!this.verificarSessaoClienteServidor()) {
				throw new Exception(
						"O Software SEI não está disponível, caso deseje realizar login entre em contato com o suporte técnico!");
			}
			//
			//
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			
			if (Uteis.isAtributoPreenchido(senha) && senha.startsWith("jmeter") &&
				getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().containsKey((getUsername()+getSenha().replace("jmeter", ""))) &&
				Uteis.isAtributoPreenchido(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get((getUsername()+getSenha().replace("jmeter", ""))))) {
					setUsuario(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get((getUsername()+getSenha().replace("jmeter", ""))));
			} else if (simulacaoLogin || encerarSimulacao) {
				setUsuario(getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioSimulacaoSuporte(getUsername(), getSenha(), true, Uteis.NIVELMONTARDADOS_DADOSLOGIN));
			} else if (getLogadoComEmailInstitucional() && Uteis.isAtributoPreenchido(getEmailInstitucional())) {
//				if(getAplicacaoControle().getUsuarioAlunoPorEmailCache().containsKey(getEmailInstitucional()) && Uteis.isAtributoPreenchido(getAplicacaoControle().getUsuarioAlunoPorEmailCache().get(getEmailInstitucional()))){
//					setUsuario(getAplicacaoControle().getUsuarioAlunoPorEmailCache().get(getEmailInstitucional()));
//				}else {	
//					setUsuario(getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioEmailInstitucional(getEmailInstitucional(), Uteis.NIVELMONTARDADOS_DADOSLOGIN));
//				}
				if (!Uteis.isAtributoPreenchido(getUsuario())) {
					throw new ConsistirException("Usuário e/ou senha incorretos!");
				}
				context().getExternalContext().getSessionMap().put("logadoComEmailInstitucional", true);
				Uteis.setCookie("SAML", getEmailInstitucional(), Integer.MAX_VALUE);
			} else {
				if (!Uteis.isAtributoPreenchido(getSenha()) && getSenha().length() == 64) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Login_usuario_senha_invalido"));
				}
				int qtdTentativas = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado())
						.getQtdTentativasFalhaLogin();
				int tempoTentativas = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado())
						.getTempoBloqTentativasFalhaLogin();
				boolean erroLoginUsuario = true;
				Date dataFutura = null;
				try {
					if(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().containsKey((getUsername()+Uteis.encriptar(getSenha())))
							&& Uteis.isAtributoPreenchido(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get((getUsername()+Uteis.encriptar(getSenha()))))){
						setUsuario(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().get((getUsername()+Uteis.encriptar(getSenha()))));
					}else {
						setUsuario(getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsername(), getSenha(), true, Uteis.NIVELMONTARDADOS_DADOSLOGIN));
					}
					if (Uteis.isAtributoPreenchido(qtdTentativas)) {
						if (Uteis.isAtributoPreenchido(getUsuario().getDataFalhaLogin())) {
							dataFutura = UteisData.getDataFuturaAvancandoMinuto(getUsuario().getDataFalhaLogin(),
									tempoTentativas);
						}
						erroLoginUsuario = false;
						if (getUsuario().getQtdFalhaLogin() >= qtdTentativas && Uteis.isAtributoPreenchido(dataFutura)
								&& (dataFutura.after(new Date()))) {
							throw new ConsistirException(
									"Usuário Bloqueado por excesso de tentativas! Realize nova tentativa após "
											+ tempoTentativas + " minuto(s)!");
						}
					}
				} catch (Exception e) {
					try {
						if (qtdTentativas > 0 && tempoTentativas > 0 && e.getMessage() != null
								&& e.getMessage().contains("Usuário e/ou senha incorretos!")) {
							UsuarioVO usuarioVO2 = getFacadeFactory().getControleAcessoFacade()
									.verificarLoginUsuarioParaControleFalhaLogin(getUsername());
							if (erroLoginUsuario && Uteis.isAtributoPreenchido(usuarioVO2)
									&& !usuarioVO2.getUsuarioBloqPorFalhaLogin()) {
								getFacadeFactory().getUsuarioFacade().registrarFalhaTentativaLogin(getUsername(),
										qtdTentativas);
							}
							usuarioVO2 = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioParaControleFalhaLogin(getUsername());
							if (Uteis.isAtributoPreenchido(usuarioVO2) && usuarioVO2.getUsuarioBloqPorFalhaLogin()) {
								throw new ConsistirException(
										"Usuário Bloqueado por excesso de tentativas! Realize nova tentativa após "
												+ tempoTentativas + " minuto(s)!");
							}
						}
					} catch (Exception e2) {
						throw e2;
					}
					throw new ConsistirException(e.getMessage());
				}
			}
			if (context().getExternalContext().getSessionMap().containsKey("usuarioLogado")
					&& !((UsuarioVO) context().getExternalContext().getSessionMap().get("usuarioLogado")).getCodigo().equals(0)) {
				setEncontradoUsuarioLogadoEmDuasAbasNavegador(true);
				throw new Exception(UteisJSF.internacionalizar("msg_Login_usuario_ja_logado"));
			}
			setEncontradoUsuarioLogadoEmDuasAbasNavegador(false);
			context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuario());
			HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
			Uteis.setCookie("SESSAOID", session.getId(), Integer.MAX_VALUE);
			registrarAtividadeUsuario(getUsuario(), "LoginControle", "Iniciando Login", "Consulta");
			inicializarDadosUsuario(getUsuario());
			registrarPrimeiroAcesso(getUsuario());
			registrarUltimoAcesso(getUsuario());
			incializarDadosPessoa(getUsuario());
			inicializarDadosFotoUsuario();
			//setListaDataComemorativa(obterListaDataComemorativaDataAtual());
			Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("VisaoAlunoControle"));
			removerControleMemoriaFlashTela("OuvidoriaControle");
			Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("VisaoProfessorControle"));
			Uteis.removerObjetoMemoria(
					context().getExternalContext().getSessionMap().remove("ComunicacaoInternaControle"));
			if (getUsuario().getTipoOuvidoria() || getNomeTelaAtual().endsWith("homeOuvidoriaForm.xhtml") || getUsuario().getTipoOuvidoria() || getNomeTelaAtual().endsWith("homeOuvidoriaCons.xhtml")) {
				return logarDiretamenteComoOuvidoria();
			}
			tipoPessoa = verificarSeTipoUsuarioIsUnico(getUsuario());
			if (!tipoPessoa.equals("aluno") && !tipoPessoa.equals("professor") && !tipoPessoa.equals("coordenador")
					&& !tipoPessoa.equals("parceiro") && !tipoPessoa.equals("visitante")) {
				verificaRequerimentoComunicadoPendente(getUsuario());
			}
			if (tipoPessoa.equals("aluno") && (getUsuario().getPessoa().getAluno())) {
				// getUsuario().setVisaoLogar("aluno");
				return logarDiretamenteComoAluno(simulacaoLogin);
			} else if (tipoPessoa.equals("pais")) {
				// getUsuario().setVisaoLogar("pais");
				return logarDiretamenteComoPais();
			} else if (tipoPessoa.equals("professor") && getUsuario().getPessoa().getAtivo()) {
				// getUsuario().setVisaoLogar("professor");
				return logarDiretamenteComoProfessor(simulacaoLogin);
			} else if (tipoPessoa.equals("diretorMultiCampus") && (getUsuario().getPessoa().getCodigo() == 0
					|| (getUsuario().getPessoa().getCodigo() > 0 && getUsuario().getPessoa().getAtivo()))) {
				// getUsuario().setVisaoLogar("diretorMultiCampus");
				return logarDiretamenteComoDiretorMultiCampus();
			} else if (tipoPessoa.equals("coordenador") && getUsuario().getPessoa().getAtivo()) {
				// getUsuario().setVisaoLogar("coordenador");
				return logarDiretamenteComoCoordenador(simulacaoLogin);
			} else if (tipoPessoa.equals("parceiro")) {
				return logarDiretamenteComoParceiro();
			} else if (tipoPessoa.equals("visitante")) {
				return logarDiretamenteComoVisitante();
			} else if (tipoPessoa.equals("funcionario")) {
				verificarLoginComMaisDeUmaOpcao();
				if(getListaSelectItemUnidadeEnsino().isEmpty() || getListaSelectItemUnidadeEnsino().size() == 1) {					
					return logarDiretamenteComoFuncionario();
				}else {
					verificarQuaisVisoesMostrarNoModal(getUsuario());
					setMostrarModalEscolhaVisao(true);
					setMensagemDetalhada("");
					if (getLogadoComEmailInstitucional()) {
						return "index.xhtml?faces-redirect=true&mostrarModalEscolhaVisao=true";
					}
					return "";
				}
			}else {				
				verificarQuaisVisoesMostrarNoModal(getUsuario());
				setMostrarModalEscolhaVisao(true);
				verificarLoginComMaisDeUmaOpcao();
				setMensagemDetalhada("");
				if (getLogadoComEmailInstitucional()) {
					return "index.xhtml?faces-redirect=true&mostrarModalEscolhaVisao=true";
				}
				return "";
			}
			

		} catch (InvalidResultSetAccessException e2) {
			e2.printStackTrace();
			setMensagemDetalhada("msg_erro", e2.getMessage());
			if (getLogadoComEmailInstitucional()) {
				this.logoutAD();
				setLogadoComEmailInstitucional(false);				
			}			
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			try {
				mensagemErroSenha = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarMensagemErroSenha();
				if (mensagemErroSenha == null || mensagemErroSenha.equals("")) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				} else {
					setMensagemDetalhada("msg_erro", mensagemErroSenha);
				}
				if (!getEncontradoUsuarioLogadoEmDuasAbasNavegador()) {
					context().getExternalContext().getSessionMap().remove("usuarioLogado");
				}
			} catch (Exception ex) {
				setMensagemDetalhada("msg_erro", ex.getMessage());
			}

			setMostrarModalEscolhaVisao(false);
			setApresentarModalPerfilProfessor(false);
			if (getLogadoComEmailInstitucional()) {
				this.logoutAD();
				setLogadoComEmailInstitucional(false);				
			}
			return "index.xhtml?faces-redirect=true";
		} finally {
			tipoPessoa = null;
		}
	}

	public String loginAdministrador() {
		try {
			UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginAdministrador(getUsername(), getSenha(), true);
			context().getExternalContext().getSessionMap().put("administradorLogado", usuarioVO);
			if (context().getExternalContext().getSessionMap().get("usuarioLogado") != null) {
				context().getExternalContext().getSessionMap().remove("usuarioLogado");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("visaoAdministrador/administrador");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void carregarConfiguracaoCandidatoVO() throws Exception {
		PerfilAcessoVO perfilCandidato = getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(
				getConfiguracaoGeralPadraoSistema().getPerfilPadraoCandidato().getCodigo(), getUsuarioLogado());
		setPermissaoAcessoMenuVO(getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(perfilCandidato));
	}

	public void registrarUltimoAcesso(UsuarioVO usuario) throws Exception {
		getFacadeFactory().getUsuarioFacade().registrarUltimoAcesso(usuario);
	}

	public void registrarPrimeiroAcesso(UsuarioVO usuario) throws Exception {
		if (usuario.getDataPrimeiroAcesso() == null) {
			usuario.setDataPrimeiroAcesso(new Date());
			getFacadeFactory().getUsuarioFacade().registrarPrimeiroAcesso(usuario);
		}
	}

	public void verificaRequerimentoComunicadoPendente(UsuarioVO usuario) throws Exception {
		setQtdComunicacaoInternaPendente(getFacadeFactory().getComunicacaoInternaFacade()
				.consultaRapidaComunicacaoInternaNaoLidasVisaoFuncionario(usuario.getPessoa().getCodigo()));
		setQtdRequerimentoPendente(getFacadeFactory().getRequerimentoFacade()
				.consultaRapidaRequerimentoPendenteUsuario(usuario.getPessoa().getCodigo()));
	}

	public void consultarComunicacaoInternaPendente() throws Exception {
		if (!getQtdComunicacaoInternaPendente().equals(0)) {
			ComunicacaoInternaControle comunicacaoInternaControle = null;
			comunicacaoInternaControle = (ComunicacaoInternaControle) context().getExternalContext().getSessionMap()
					.get(ComunicacaoInternaControle.class.getSimpleName());
			if (comunicacaoInternaControle == null) {
				comunicacaoInternaControle = new ComunicacaoInternaControle();
				context().getExternalContext().getSessionMap().put(ComunicacaoInternaControle.class.getSimpleName(),
						comunicacaoInternaControle);
			}
			comunicacaoInternaControle.consultarComunicacaoInternaNaoLidasMenu();
			setAbrirPopup("abrirPopup('comunicacaoInternaCons.xhtml', 'comunicacaoInternaCons', 780, 585);");
		} else {
			setAbrirPopup("");
		}
	}

	public void consultarRequerimentoPendente() throws Exception {
//		if (!getQtdRequerimentoPendente().equals(0)) {
//			RequerimentoControle requerimentoControle = null;
//			requerimentoControle = (RequerimentoControle) context().getExternalContext().getSessionMap()
//					.get(RequerimentoControle.class.getSimpleName());
//			if (requerimentoControle == null) {
//				requerimentoControle = new RequerimentoControle();
//				context().getExternalContext().getSessionMap().put(RequerimentoControle.class.getSimpleName(),
//						requerimentoControle);
//			}
//			requerimentoControle.consultarRequerimentosPendentesMenu();
//			setAbrirPopup("abrirPopup('requerimentoCons.xhtml', 'requerimentoCons', 780, 585);");
//		} else {
//			setAbrirPopup("");
//		}
	}

	public void inicializarDadosUsuario(UsuarioVO usuarioVO) {
		setNome(usuarioVO.getNome());
		setUsuario(usuarioVO);
		setDataLogin(Uteis.getDataAtual());
		setDataUltimoAcesso(Uteis.getData(new Date()));
		realizarCapturaIpCliente(usuarioVO);
		// getUsuarioLogado().setDataUltimoAcesso(null);
		setDiaSemanaLogin(Uteis.getDiaSemana_Apresentar() + " - " + Uteis.getHoraAtual() + "h");
		setDiaSemanaLoginSemHora(Uteis.getDiaSemana_Apresentar());
		setPermissaoAcessoMenuVO(null);
	}

	public String verificarSeTipoUsuarioIsUnico(UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO.getPessoa().getFuncionario() && usuarioVO.getPessoa().getAtivo()) {
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade()
					.consultaRapidaPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), false, null));
			if (!getFuncionarioVO().getExerceCargoAdministrativo() && (!usuarioVO.getPerfilAdministrador() || usuarioVO.getTipoUsuario().equals("PR")  || usuarioVO.getTipoUsuario().equals("CO"))) {
				if (!usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()
						&& usuarioVO.getPessoa().getProfessor()
						&& (usuarioVO.getTipoUsuario().equals(TipoUsuario.PROFESSOR.getValor())
								|| usuarioVO.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor()))
						&& !usuarioVO.getPessoa().getCoordenador()) {
					return "professor";
				}
				if (!usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()
						&& !usuarioVO.getPessoa().getProfessor()
						&& usuarioVO.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor())
						&& usuarioVO.getPessoa().getCoordenador()) {
					return "coordenador";
				}
			}
			if (usuarioVO.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor())
					&& (getFuncionarioVO().getExerceCargoAdministrativo() || usuarioVO.getPerfilAdministrador())
					&& ((usuarioVO.getPessoa().getCodigo() > 0
							&& !usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()
							&& !usuarioVO.getPessoa().getCoordenador() && !usuarioVO.getPessoa().getProfessor()
							&& usuarioVO.getPessoa().getAtivo()))) {
				return "funcionario";
			}
		}
		if (usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()
				&& !usuarioVO.getPessoa().getProfessor() && !usuarioVO.getPessoa().getFuncionario()
				&& !usuarioVO.getPessoa().getCoordenador()) {
			return "aluno";
		}
		if (usuarioVO.getPessoa().getPossuiAcessoVisaoPais() && !usuarioVO.getPessoa().getAluno()
				&& !usuarioVO.getPessoa().getProfessor() && !usuarioVO.getPessoa().getFuncionario()
				&& !usuarioVO.getPessoa().getCoordenador()) {
			return "pais";
		}
		// if
		// (usuarioVO.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())
		// && usuarioVO.getPessoa().getCodigo() == 0) {
		if (usuarioVO.getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())
				&& (usuarioVO.getPessoa().getCodigo() == 0 || (usuarioVO.getPessoa().getCodigo() > 0
						&& !usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()
						&& !usuarioVO.getPessoa().getCoordenador() && !usuarioVO.getPessoa().getProfessor()
						&& usuarioVO.getPessoa().getAtivo()))) {
			return "diretorMultiCampus";
		}
//		if (usuarioVO.getParceiro().getCodigo() != 0) {
//			return "parceiro";
//		}
		if (!usuarioVO.getPessoa().getAtivo() && usuarioVO.getPessoa().getCodigo() > 0
				&& !usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Login_usuario_inativo"));
		}
		if (!usuarioVO.getPessoa().getAtivo() && usuarioVO.getPessoa().getCodigo() > 0
				&& usuarioVO.getPessoa().getAluno() && !usuarioVO.getPessoa().getPossuiAcessoVisaoPais()) {
			return "aluno";
		}
		if (!usuarioVO.getPessoa().getAtivo() && usuarioVO.getPessoa().getCodigo() > 0
				&& !usuarioVO.getPessoa().getAluno() && usuarioVO.getPessoa().getPossuiAcessoVisaoPais()) {
			return "pais";
		}
		if (usuarioVO.getPessoa().getMembroComunidade() && !usuarioVO.getPessoa().getAluno()
				&& !usuarioVO.getPessoa().getPossuiAcessoVisaoPais() && !usuarioVO.getPessoa().getProfessor()
				&& !usuarioVO.getPessoa().getFuncionario() && !usuarioVO.getPessoa().getCoordenador()) {
			return "visitante";
		}
		
		return "tipoNaoIdentificado";

	}

	public String logarDiretamenteComoAluno(Boolean simulacaoLogin) {
		try {
			if (getAcessandoBiblioteca()) {
				setOpcao("");
				context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",getConfiguracaoGeralSistemaVO());
				getUsuarioLogado().setTipoUsuario(TipoPessoa.ALUNO.getValor());
				setAcessandoBiblioteca(Boolean.FALSE);
			} else {
				setOpcao("visaoAluno/telaInicialVisaoAluno");
				getUsuario().setVisaoLogar("aluno");
				if (getUsuarioLogado() == null) {
					context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuario());
				}
				List<MatriculaVO> matriculaVOs = null;
				if(getAplicacaoControle().getMatriculasAlunoCache().containsKey(getUsuarioLogado().getCodigo()) && !getAplicacaoControle().getMatriculasAlunoCache().get(getUsuarioLogado().getCodigo()).isEmpty()){
					matriculaVOs = getAplicacaoControle().getMatriculasAlunoCache().get(getUsuarioLogado().getCodigo());
				}else {
					matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaNaoCancelada(getUsuarioLogado().getPessoa().getCodigo(),false, false, true, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				}
				if (matriculaVOs.isEmpty()) {
					setMensagemDetalhada("msg_erro",UteisJSF.internacionalizar("msg_Login_matricula_com_irregulariedade"));
					setMostrarModalEscolhaVisao(false);
					setApresentarModalPerfilProfessor(false);
					setMensagemDetalhada("msg_erro","Foi encontrada uma irregularidade em sua matrícula. Entre em contato com o departamento pedagógico.");
					return "";
				} else {
					Iterator<MatriculaVO> i = matriculaVOs.iterator();
					while (i.hasNext()) {
						MatriculaVO matriculaVO = (MatriculaVO) i.next();
						if (!matriculaVO.getUnidadeEnsino().getCodigo().equals(0)) {
							matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
							if (!matriculaVO.getUnidadeEnsino().getConfiguracoes().getCodigo().equals(0)) {
								getUsuarioLogado().setUnidadeEnsinoLogado((UnidadeEnsinoVO) matriculaVO.getUnidadeEnsino().clone());
								inicializarLogoApartirUsuario(getUsuarioLogadoClone());
							}
						}
						break;
					}
				}
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().validarConfiguracaoGeralSistema(getConfiguracaoGeralSistemaVO());
//				setTituloTelaBancoCurriculum(getConfiguracaoGeralSistemaVO().getTituloTelaBancoCurriculum());
//				setTituloTelaBuscaCandidato(getConfiguracaoGeralSistemaVO().getTituloTelaBuscaCandidato());
				if (getConfiguracaoGeralSistemaVO().getApresentarMensagemAlertaAlunoNaoAssinouContrato() && !matriculaVOs.isEmpty()) {
					matriculaVOs.get(0).setMensagemAlertaAlunoNaoAssinouContratoMatricula(getConfiguracaoGeralSistemaVO().getMensagemAlertaAlunoNaoAssinouContratoMatricula());
				}
				getUsuario().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(getConfiguracaoGeralSistemaVO(), matriculaVOs.get(0),matriculaVOs.get(0).getAlunoNaoAssinouContratoMatricula(), getUsuarioLogado()));
				getUsuario().setMatriculaVOs(matriculaVOs);
				setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(matriculaVOs.get(0), getConfiguracaoGeralSistemaVO()));
				verificarValidarCpfSetarIpMaquinaLogada();
				setMostrarModalEscolhaVisao(false);
				setUsername("");
				setSenha("");
				if (!simulacaoLogin && !getOpcao().contains("avaliacaoInstitucionalQuestionario.xhtml")) {
					String retorno = verificaAtualizacaoCadastral(getUsuario(), Boolean.FALSE);
					if (!retorno.equals("")) {
						return retorno;
					}
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Aluno", "Consulta");
				return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String verificaAtualizacaoCadastral(UsuarioVO usuario, Boolean editarPessoaPorResponsavel) throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO()
				.getHabilitarRecursoAtualizacaoCadastral().booleanValue() && getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno()) {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) getControlador("VisaoAlunoControle");
			if (!getPermissaoAcessoMenuVO().getAluno()) {
				visaoAlunoControle.setBloquearMenuVisaoAluno(Boolean.FALSE);
				return "";
			}
			if (usuario.getPessoa().getDataUltimaAtualizacaoCadatral() == null) {
				visaoAlunoControle.setBloquearMenuVisaoAluno(Boolean.TRUE);
				if (editarPessoaPorResponsavel) {
					visaoAlunoControle.setBloquearMenuVisaoAluno(Boolean.FALSE);
					return "visaoAluno/" + visaoAlunoControle.editarDadosPessoaPorResponsavel();
				}
				return "visaoAluno/" + visaoAlunoControle.editarDadosPessoa();
			} else if (getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO()
					.getSolicitarAtualizacaoACada().intValue() > 0) {
				long diasEntreDatas = Uteis.nrDiasEntreDatas(new Date(),
						usuario.getPessoa().getDataUltimaAtualizacaoCadatral());
				if (diasEntreDatas > getConfiguracaoGeralPadraoSistema().getConfiguracaoAtualizacaoCadastralVO()
						.getSolicitarAtualizacaoACada().intValue()) {
					visaoAlunoControle.setBloquearMenuVisaoAluno(Boolean.TRUE);
					if (editarPessoaPorResponsavel) {
						visaoAlunoControle.setBloquearMenuVisaoAluno(Boolean.FALSE);
						return "visaoAluno/" + visaoAlunoControle.editarDadosPessoaPorResponsavel();
					}
					return "visaoAluno/" + visaoAlunoControle.editarDadosPessoa();
				}
			}
		}
		return "";
	}

	public String logarDiretamenteComoPais() throws Exception {
		Boolean existeFiliacao = getFacadeFactory().getFiliacaoFacade()
				.consultarSeExisteFiliacaoPorPais(getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado());
		if (!existeFiliacao) {
			setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_Login_usuario_sem_matricula_ativa"));
			setMostrarModalEscolhaVisao(false);
			setApresentarModalPerfilProfessor(false);
			return "";
		}
		if (getAcessandoBiblioteca()) {
			setOpcao("");
//			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), null));
			context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",
					getConfiguracaoGeralSistemaVO());
//			setConfiguracaoGeralSistemaVO(obterConfiguracaoGeralSistemaVONivelAplicacao(getConfiguracaoGeralSistemaVO()));
			getUsuarioLogado().setTipoUsuario(TipoPessoa.RESPONSAVEL_LEGAL.getValor());
			setAcessandoBiblioteca(Boolean.FALSE);
		} else {
			setOpcao("/visaoAluno/telaInicialVisaoAluno");
			getUsuario().setVisaoLogar("pais");
			getUsuario().setPerfilAcesso(verificarPerfilPais());
			montarPermissoesMenu(getUsuario().getPerfilAcesso());
		}
		verificarValidarCpfSetarIpMaquinaLogada();
		setMostrarModalEscolhaVisao(false);
		setUsername("");
		setSenha("");
		registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Pais", "Consulta");

		List<UsuarioVO> listaAlunos = (getFacadeFactory().getUsuarioFacade().consultaRapidaPorResponsavelLegal(
				getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado()));
		for (UsuarioVO aluno : listaAlunos) {
			String retorno = verificaAtualizacaoCadastral(aluno, Boolean.TRUE);
			if (!retorno.equals("")) {
				return retorno;
			}
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
	}

	public PerfilAcessoVO verificarPerfilAluno(List<MatriculaVO> matriculaVOs) throws Exception {
		carregarDadosTitulosApresentar();
		return getFacadeFactory().getPerfilAcessoFacade().executarVerificacaoPerfilAcessoSelecionarVisaoAluno(
				getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVOs.get(0).getUnidadeEnsino().getCodigo(),
						getUsuarioLogado()),
				matriculaVOs.get(0), matriculaVOs.get(0).getAlunoNaoAssinouContratoMatricula(), getUsuarioLogado());
	}

	public PerfilAcessoVO verificarPerfilPais() throws Exception {
		carregarDadosTitulosApresentar();
		return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getPerfilPadraoPais();
	}

	public PerfilAcessoVO verificarPerfilOuvidoria() throws Exception {
		return getConfiguracaoGeralPadraoSistema().getPerfilPadraoOuvidoria();
	}

	public String logarDiretamenteComoProfessor(Boolean simulacaoLogin) {

		try {
			if (getAcessandoBiblioteca()) {
				setOpcao("");
//				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), null));
				context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",
						getConfiguracaoGeralSistemaVO());
//				setConfiguracaoGeralSistemaVO(obterConfiguracaoGeralSistemaVONivelAplicacao(getConfiguracaoGeralSistemaVO()));
				verificarValidarCpfSetarIpMaquinaLogada();
				setMostrarModalEscolhaVisao(false);
				setUsername(null);
				setSenha(null);
				getUsuarioLogado().setTipoUsuario(TipoPessoa.PROFESSOR.getValor());
				setAcessandoBiblioteca(Boolean.FALSE);
				registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Professor",
						"Consulta");
				Uteis.removerObjetoMemoria(
						context().getExternalContext().getSessionMap().remove("codigoUnidadeEnsino"));

			} else {
				setOpcao("/visaoProfessor/telaInicialVisaoProfessor");
				getUsuario().setVisaoLogar("professor");
				verificarPerfilProfessor();
				executarVerificacaoExistenciaArquivoInstitucinalProfessor();
//				setRequisicoesVOs(getFacadeFactory().getRequisicaoFacade()
//						.consultarRequisicaoRespostaQuestionarioFechamento(getUsuario(), Uteis.NIVELMONTARDADOS_TODOS));
//				if (Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//					setApresentarRequisicoes(Boolean.TRUE);
//				}
				if (getUsuario().getPerfilAcesso() == null || getUsuario().getPerfilAcesso().getCodigo() == 0) {
//				setMostrarModalEscolhaVisao(false);
//				inicializarLogoApartirUsuario(getUsuario());
					throw new Exception(UteisJSF.internacionalizar("msg_Login_sem_perfil"));
				} else {
					montarPermissoesMenu(getUsuario().getPerfilAcesso());
					// montarPermissoesMenuMaisDeUmPerfil(getUsuario());
//				validarDadosCssProfessor();
					setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
					verificarValidarCpfSetarIpMaquinaLogada();
					setMostrarModalEscolhaVisao(false);
					setUsername(null);
					setSenha(null);
//				if (!simulacaoLogin) {
//					String retorno = verificaAtualizacaoCadastral(getUsuario(), Boolean.FALSE);
//					if (!retorno.equals("")) {
//						return retorno;
//					}
//				}
					// System.out.println("Final login: " + new Date());
					registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Professor",
							"Consulta");
					Uteis.removerObjetoMemoria(
							context().getExternalContext().getSessionMap().remove("codigoUnidadeEnsino"));
					inicializarLogoApartirUsuario(getUsuario());
					getUsuario().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
				}
//				inicializarDadosFotoUsuario();
				return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			e.printStackTrace();
		}
		return "";
	}

	public String logarDiretamenteComoProfessorMobile() throws Exception {
		setOpcao("/visaProfessor/telaInicialVisaoProfessor");
		getUsuario().setVisaoLogar("professor");
		verificarPerfilProfessor();
		executarVerificacaoExistenciaArquivoInstitucinalProfessor();
		if (getUsuario().getPerfilAcesso() == null || getUsuario().getPerfilAcesso().getCodigo() == 0) {
			setMostrarModalEscolhaVisao(false);
//			return "indexMobileOpcoesProfessor";
			return Uteis.getCaminhoRedirecionamentoNavegacao("visaoProfessor/indexMobileOpcoesProfessor");
		} else {
			montarPermissoesMenu(getUsuario().getPerfilAcesso());
			// montarPermissoesMenuMaisDeUmPerfil(getUsuario());
			validarDadosCssProfessor();
			setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			setUsername(null);
			setSenha(null);
			// System.out.println("Final login: " + new Date());
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Professor", "Consulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
		}
	}

	public void executarVerificacaoExistenciaArquivoInstitucinalProfessor() throws Exception {
		List<ArquivoVO> listaArquivos = (getFacadeFactory().getArquivoFacade()
				.consultarArquivoInstituicionalParaProfessor(getUsuarioLogado(), 1));
		if (listaArquivos.isEmpty()) {
			setPossuiArquivoInstitucional(Boolean.FALSE);
		} else {
			setPossuiArquivoInstitucional(Boolean.TRUE);
		}
	}

	public void executarVerificacaoExistenciaArquivoInstitucinalCoordenador() throws Exception {
		List<ArquivoVO> listaArquivos = (getFacadeFactory().getArquivoFacade()
				.consultarArquivoInstituicionalParaCoordenador(getUsuarioLogado(), 1));
		if (listaArquivos.isEmpty()) {
			setPossuiArquivoInstitucional(Boolean.FALSE);
		} else {
			setPossuiArquivoInstitucional(Boolean.TRUE);
		}
	}

	public void executarVerificacaoExistenciaArquivoInstitucinalAluno(String matricula) throws Exception {
		List<ArquivoVO> listaArquivos = getFacadeFactory().getArquivoFacade()
				.consultarArquivoInstituicionalParaAluno(getUsuarioLogado(), matricula, 1);
		try {
			if (!listaArquivos.isEmpty()) {
				setPossuiArquivoInstitucional(Boolean.TRUE);
				listaArquivos = null;
				return;
			}
			setPossuiArquivoInstitucional(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String adicionarPerfilProfessor() {
		try {
			setOpcao("/visaoProfessor/telaInicialVisaoProfessor");
			getUsuario().setVisaoLogar("professor");
			getUsuario().setPerfilAcesso(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
					.getPerfilAcessoProfessor(getUsuario().getTipoNivelEducacionalLogado()));
			if (getUsuario().getPerfilAcesso() == null || getUsuario().getPerfilAcesso().getCodigo() == 0) {
				throw new Exception(
						"Não foi encontrado configuraçãoo para o Perfil do seu Usuário no nivel educacional "
								+ getUsuario().getTipoNivelEducacionalLogado().getDescricao() + "!");
			}
			montarPermissoesMenu(getUsuario().getPerfilAcesso());
//			getUsuario().setAvaliacaoInstitucionalVOs(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUsuarioLogado(getUsuarioLogado(), null));
			// montarPermissoesMenuMaisDeUmPerfil(getUsuario());
			// validarDadosCssProfessor();
			if (!getUsuarioLogado().getAvaliacaoInstitucionalVOs().isEmpty()) {
				setOpcao("/visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml");
			}
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			setApresentarModalPerfilProfessor(false);
			setUsername(null);
			setSenha(null);
			setIsMinhaBibliotecaHabilitado(null);
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Professor", "Consulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void verificarPerfilProfessor() throws Exception {
		carregarDadosTitulosApresentar();
		verificarPerfilNivelGraduacaoProfessor(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
	}

	public void montarListaSelectItemUnidadeEnsinoCoordenador(List<UnidadeEnsinoVO> objs) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		for (UnidadeEnsinoVO obj : objs) {
			lista.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) lista, ordenador);
		setListaSelectItemUnidadeEnsinoCoordenador(lista);
	}

	public String logarDiretamenteComoCoordenador(Boolean simulacaoLogin) throws Exception {
		// if (getCodUnidadeEnsinoCoordenador() == null) {

		if (getAcessandoBiblioteca()) {
			setOpcao("");
//			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), null));
			context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",
					getConfiguracaoGeralSistemaVO());
//			setConfiguracaoGeralSistemaVO(obterConfiguracaoGeralSistemaVONivelAplicacao(getConfiguracaoGeralSistemaVO()));
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			setUsername(null);
			setSenha(null);
			getUsuarioLogado().setTipoUsuario(TipoPessoa.COORDENADOR_CURSO.getValor());
			setAcessandoBiblioteca(Boolean.FALSE);
		} else {
			setListaSelectItemUnidadeEnsinoCoordenador(null);
			executarVerificacaoExistenciaArquivoInstitucinalCoordenador();
			List<UnidadeEnsinoVO> objs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCoordenador(
					getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			if (objs.size() > 1) {
				montarListaSelectItemUnidadeEnsinoCoordenador(objs);
				setApresentarListaUnidadeEnsinoCoordenador(true);
				setMostrarModalEscolhaVisao(false);
				getUsuarioLogado().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
				setCodUnidadeEnsinoCoordenador(objs.get(0).getCodigo());
				objs = null;
				setOpcao("");
			} else if (objs.size() == 1) {
				UnidadeEnsinoVO unidadeEnsino = (UnidadeEnsinoVO) objs.get(0);
				if (unidadeEnsino.getCodigo() != null && !unidadeEnsino.getCodigo().equals(0)) {
					getUsuarioLogado().setUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade()
							.consultarPorChavePrimaria(unidadeEnsino.getCodigo(), false,
									Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado()));
					setNomeUnidadeEnsino(getUsuarioLogado().getUnidadeEnsinoLogado().getNome());
				} else {
					getUsuarioLogado().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
				}
				setCodUnidadeEnsinoCoordenador(getUsuarioLogado().getUnidadeEnsinoLogado().getCodigo());
				montarListaSelectItemUnidadeEnsinoCoordenador(objs);
				setMostrarModalEscolhaVisao(false);
				unidadeEnsino = null;
				getUsuarioLogado().setVisaoLogar("coordenador");

				setOpcao("visaoCoordenador/telaInicialVisaoCoordenador");
			}
			getUsuarioLogado().setPerfilAcesso(verificarPerfilCoordenador());
			montarPermissoesMenu(getUsuarioLogado().getPerfilAcesso());
			if (!getApresentarListaUnidadeEnsinoCoordenador()) {
				setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
			}
			verificarValidarCpfSetarIpMaquinaLogada();
			setUsername(null);
			setSenha(null);
//		if (!simulacaoLogin) {
//			String retorno = verificaAtualizacaoCadastral(getUsuario(), Boolean.FALSE);
//			if (!retorno.equals("")) {
//				return retorno;
//			}
//		}
			verificarNivelEducacionalCursosCoordenador();
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Coordenador", "Consulta");
			Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("codigoUnidadeEnsino"));
			inicializarLogoApartirUsuario(getUsuarioLogadoClone());
			getUsuarioLogado().setUnidadeEnsinoLogado(new UnidadeEnsinoVO());
			setUsuario(getUsuarioLogadoClone());
		}
		setUsuario(getUsuarioLogado());

//		setRequisicoesVOs(getFacadeFactory().getRequisicaoFacade()
//				.consultarRequisicaoRespostaQuestionarioFechamento(getUsuario(), Uteis.NIVELMONTARDADOS_TODOS));
//		if (Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//			setApresentarRequisicoes(Boolean.TRUE);
//		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
	}

	public void verificarNivelEducacionalCursosCoordenador() throws Exception {
		getUsuarioLogado().setNivelEducacionalProfessorEnums(
				getFacadeFactory().getCursoCoordenadorFacade().consultarNivelEducacionalCursosCoordenador(
						getUsuarioLogado().getPessoa().getCodigo(), getUsuarioLogado()));
	}

	public PerfilAcessoVO verificarPerfilCoordenador() throws Exception {
		carregarDadosTitulosApresentar();
		return getAplicacaoControle()
				.getConfiguracaoGeralSistemaVO(getCodUnidadeEnsinoCoordenador(), getUsuarioLogado())
				.getPerfilPadraoCoordenador();
	}

	public String selecionarUnidadeEnsinoCoordenador() {
		try {
			if (getCodUnidadeEnsinoCoordenador().equals(0)) {
				SelectItem obj = (SelectItem) getListaSelectItemUnidadeEnsinoCoordenador().get(0);
				setCodUnidadeEnsinoCoordenador((Integer) obj.getValue());
			}
			if (getCodUnidadeEnsinoCoordenador() != null && !getCodUnidadeEnsinoCoordenador().equals(0)) {
				getUsuario().setUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade()
						.consultarPorChavePrimaria(getCodUnidadeEnsinoCoordenador(), false,
								Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado()));
				setNomeUnidadeEnsino(getUsuario().getUnidadeEnsinoLogado().getNome());
			}
			getUsuario().setVisaoLogar("coordenador");
			inicializarLogoApartirUsuario(getUsuarioLogadoClone());
			setApresentarListaUnidadeEnsinoCoordenador(false);
			setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
			if (!getUsuarioLogado().getAvaliacaoInstitucionalVOs().isEmpty()) {
				return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
			}
			if (getNomeTelaAtual().contains("visaoCoordenador")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("telaInicialVisaoCoordenador.xhtml");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("visaoCoordenador/telaInicialVisaoCoordenador.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void verificarPerfilNivelGraduacaoProfessor(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO)
			throws Exception {
		List<TipoNivelEducacional> listaNivelEducacional = getFacadeFactory().getCursoFacade()
				.consultarNivelEducacionalProfessor(getUsuario());
		getListaSelectItemPerfilProfessorGraduacaoPos().clear();
		TipoNivelEducacional[] tipoNivelEducacionalProfessor = new TipoNivelEducacional[listaNivelEducacional.size()];
		if (!listaNivelEducacional.isEmpty()) {
			int contador = 0;
			for (TipoNivelEducacional tipoNivelEducacional : listaNivelEducacional) {
				tipoNivelEducacionalProfessor[contador] = tipoNivelEducacional;
				contador++;
			}
		}
		setListaSelectItemPerfilProfessorGraduacaoPos(
				UtilSelectItem.getListaSelectItemEnum(tipoNivelEducacionalProfessor, Obrigatorio.SIM));

		for (TipoNivelEducacional tipoNivelEducacional : listaNivelEducacional) {
			if (!Uteis.isAtributoPreenchido(getUsuario().getPerfilAcesso())) {
				getUsuario().setTipoNivelEducacionalLogado(tipoNivelEducacional);
				getUsuario().setPerfilAcesso(configuracaoGeralSistemaVO.getPerfilAcessoProfessor(tipoNivelEducacional));
			} else {
				break;
			}
		}
		setApresentarModalPerfilProfessor(listaNivelEducacional.size() > 1);
		getUsuario().setNivelEducacionalProfessorEnums(listaNivelEducacional);
	}

	public String logarDiretamenteComoDiretorMultiCampus() throws Exception {

		if (getAcessandoBiblioteca()) {
			setOpcao("");
//			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), null));
			context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",
					getConfiguracaoGeralSistemaVO());
//			setConfiguracaoGeralSistemaVO(obterConfiguracaoGeralSistemaVONivelAplicacao(getConfiguracaoGeralSistemaVO()));
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			getUsuarioLogado().setTipoUsuario(TipoPessoa.FUNCIONARIO.getValor());
			setAcessandoBiblioteca(Boolean.FALSE);
			setUsername("");
			setSenha("");
		} else {

			getUsuario().setVisaoLogar("funcionario");
			setOpcao("visaoAdministrativo/administrativo/homeAdministrador");
//			if (getFacadeFactory().getFuncionarioCargoFacade().verificaUsuarioDepartamento(
//					getConfiguracaoGeralPadraoSistema().getDepartamentoRespServidorNotificacao(),
//					getUsuario().getCodigo())) {
//				setApresentarRichNotificaoEmailAcimaPermitido(
//						getFacadeFactory().getEmailFacade().consultarQtdEmailMaiorQueConfiguracao(
//								getConfiguracaoGeralPadraoSistema().getQtdeMsgLimiteServidorNotificacao(),
//								getUsuario()));
//			}
			getUsuario().setPerfilAcesso(
					getFacadeFactory().getPerfilAcessoFacade().consultarPerfilAcessoDiretorMultiCampus(getUsuario()));
			montarPermissoesMenu(getUsuario().getPerfilAcesso());
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			setUsername("");
			setSenha("");
			inicializarLogoApartirUsuario(getUsuario());
			setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Administrador",
					"Consulta");

//			setRequisicoesVOs(getFacadeFactory().getRequisicaoFacade()
//					.consultarRequisicaoRespostaQuestionarioFechamento(getUsuario(), Uteis.NIVELMONTARDADOS_TODOS));
//			if (Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//				setApresentarRequisicoes(Boolean.TRUE);
//			}

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
	}

	public String logarDiretamenteComoFuncionario() throws Exception {
		try {
			// setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuario().getCodigo(),
			// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getUsuario().setVisaoLogar("funcionario");
			if (getVisualizarComboUnidadeEnsino()) {
				if (getCodUnidadeEnsino().intValue() == 0) {
					if (getUsuario().getPerfilAdministrador()) {
						setOpcao("");
					} else {
						// setOpcao("funcionario");
						throw new ConsistirException("A UNIDADE ENSINO deve ser informada.");
					}
					return getOpcao();
				} else {
					getUsuario().getUnidadeEnsinoLogado().setCodigo(getCodUnidadeEnsino());
					getUsuarioLogado().getUnidadeEnsinoLogado().setCodigo(getCodUnidadeEnsino());
//					setOpcao("funcionario");
					setOpcao("visaoAdministrativo/administrativo/homeAdministrador");
					setMostrarModalEscolhaVisao(false);
					setUsername("");
					setSenha("");
				}
			} else {
				getUsuario().getUnidadeEnsinoLogado().setCodigo(getCodUnidadeEnsino());
				if (getUsuarioLogado() == null) {
					context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuario());
				}
				getUsuarioLogado().getUnidadeEnsinoLogado().setCodigo(getCodUnidadeEnsino());
//				setOpcao("funcionario");
				setOpcao("visaoAdministrativo/administrativo/homeAdministrador");
				setMostrarModalEscolhaVisao(false);
				setUsername("");
				setSenha("");
			}
			getUsuario().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade()
					.consultarPerfilParaFuncionarioAdministrador(getCodUnidadeEnsino(), getUsuario()));
			montarPermissoesMenu(getUsuario().getPerfilAcesso());
			setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
			verificarValidarCpfSetarIpMaquinaLogada();
//			inicializarDadosFotoUsuario();
			inicializarLogoApartirUsuario(getUsuario());

//			setRequisicoesVOs(getFacadeFactory().getRequisicaoFacade()
//					.consultarRequisicaoRespostaQuestionarioFechamento(getUsuario(), Uteis.NIVELMONTARDADOS_TODOS));
//			if (Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//				setApresentarRequisicoes(Boolean.TRUE);
//			}

			return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMostrarModalEscolhaVisao(false);
			return "";
		}
	}

	public String logarDiretamenteComoAdministrador() throws Exception {
		getUsuario().setVisaoLogar("funcionario");
		setOpcao("visaoAdministrativo/administrativo/homeAdministrador");
		getUsuario().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade()
				.consultarPerfilParaFuncionarioAdministrador(getCodUnidadeEnsino(), getUsuario()));
		montarPermissoesMenu(getUsuario().getPerfilAcesso());
		verificarValidarCpfSetarIpMaquinaLogada();
//		inicializarDadosFotoUsuario();
		setMostrarModalEscolhaVisao(false);
		setUsername("");
		setSenha("");
		setOpcao(verificarAvaliacaoInstitucionalAtivaParaUsuario(null));
		inicializarLogoApartirUsuario(getUsuario());

//		setRequisicoesVOs(getFacadeFactory().getRequisicaoFacade()
//				.consultarRequisicaoRespostaQuestionarioFechamento(getUsuario(), Uteis.NIVELMONTARDADOS_TODOS));
//		if (Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//			setApresentarRequisicoes(Boolean.TRUE);
//		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
	}

	public String logarDiretamenteComoParceiro() throws Exception {
		// setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuario().getCodigo(),
		// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setOpcao("visaoParceiro/telaInicialVisaoParceiro");
//		setOpcao("parceiro");
		getUsuario().setVisaoLogar("parceiro");
		carregarDadosTitulosApresentar();
		getUsuario().setPerfilAcesso(
				getFacadeFactory().getPerfilAcessoFacade().consultarPerfilAcessoDiretorMultiCampus(getUsuario()));
		if(!Uteis.isAtributoPreenchido(getUsuario().getPerfilAcesso()) && Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getPerfilPadraoParceiro())) {
			getUsuario().setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(getConfiguracaoGeralPadraoSistema().getPerfilPadraoParceiro().getCodigo(), getUsuario()));
		}
		montarPermissoesMenu(getUsuario().getPerfilAcesso());
		// montarPermissoesMenuMaisDeUmPerfil(getUsuario());
		verificarValidarCpfSetarIpMaquinaLogada();
		setMostrarModalEscolhaVisao(false);
		setUsername("");
		setSenha("");
		// System.out.println("Final login: " + new Date());
		registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Administrador", "Consulta");
		inicializarLogoApartirUsuario(getUsuario());
		return Uteis.getCaminhoRedirecionamentoNavegacao("visaoParceiro/telaInicialVisaoParceiro");
	}

	public String logarDiretamenteComoVisitante() {
		try {
			setOpcao("");
//		setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), null));
			context().getExternalContext().getSessionMap().put("configuracaoGeralSistemaVO",
					getConfiguracaoGeralSistemaVO());
//		setConfiguracaoGeralSistemaVO(obterConfiguracaoGeralSistemaVONivelAplicacao(getConfiguracaoGeralSistemaVO()));
			getUsuarioLogado().setTipoUsuario(TipoPessoa.MEMBRO_COMUNIDADE.getValor());
			setAcessandoBiblioteca(Boolean.FALSE);
			verificarValidarCpfSetarIpMaquinaLogada();
			setMostrarModalEscolhaVisao(false);
			setUsername("");
			setSenha("");
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Aluno", "Consulta");
//		return Uteis.getCaminhoRedirecionamentoNavegacao("visaoAluno/telaInicialVisaoAluno");
			return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public void verificarQuaisVisoesMostrarNoModal(UsuarioVO usuarioVO) throws Exception {
		boolean existeVisao = false;
		if (usuarioVO.getPessoa().getAluno()) {
			setMostrarVisaoAlunoNoModal(true);
			existeVisao = true;
		}
		if (usuarioVO.getPessoa().getPossuiAcessoVisaoPais()) {
			setMostrarVisaoPaisNoModal(true);
			existeVisao = true;
		}
		if (usuarioVO.getPessoa().getProfessor() && usuarioVO.getPessoa().getAtivo()) {
			setMostrarVisaoProfessorNoModal(true);
			existeVisao = true;
		}
		if (usuarioVO.getPessoa().getCoordenador() && usuarioVO.getPessoa().getAtivo()) {
			setMostrarVisaoCoordenadorNoModal(true);
			existeVisao = true;
		}
		if (usuarioVO.getPessoa().getMembroComunidade()) {
			setMostrarVisaoVisitante(true);
			existeVisao = true;
		}
		if ((usuarioVO.getPessoa().getFuncionario() || usuarioVO.getPerfilAdministrador())
				&& usuarioVO.getPessoa().getAtivo()) {
			if (getUsuario().getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())) {
				setMostrarVisaoFuncionarioNoModalMultiCampos(true);
				existeVisao = true;
			} else {
				if (getFuncionarioVO().getCodigo().intValue() == 0) {
					try {
						setFuncionarioVO(getFacadeFactory().getFuncionarioFacade()
								.consultaRapidaPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), false, null));
					} catch (Exception e) {
					}
				}
				if (getFuncionarioVO().getExerceCargoAdministrativo() && usuarioVO.getPerfilAdministrador()) {
					setMostrarVisaoFuncionarioNoModal(true);
					existeVisao = true;
				}
			}
		}
		if (!existeVisao) {
			throw new Exception(
					"Não foi encontrado um tipo de acesso para o seu usuário, favor entrar em contado com a instituição de ensino.");
		}
	}

	public void verificarLoginComMaisDeUmaOpcao() throws Exception {
		if (getUsuario().getPessoa().getAtivo()) {
			if (getUsuario().getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())) {
				setVisualizarComboUnidadeEnsino(false);
				carregarDadosTitulosApresentar();
				registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Finalizando Login Administrador",
						"Consulta");
			} else {
				Integer quantidadeUnidadesUsuarioVinculado = getFacadeFactory().getUsuarioFacade()
						.consultarQuantidadeDeUnidadesUsuarioEstaVinculado(getUsuario());
				if (quantidadeUnidadesUsuarioVinculado == 1) {
					setCodUnidadeEnsino(
							getUsuario().getUsuarioPerfilAcessoVOs().get(0).getUnidadeEnsinoVO().getCodigo());
					carregarUnidadeEnsinoInformada();
				} else if (quantidadeUnidadesUsuarioVinculado > 1) {
					setVisualizarComboUnidadeEnsino(true);
					List<UnidadeEnsinoVO> listaUnidades = getFacadeFactory().getUnidadeEnsinoFacade()
							.consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuario().getCodigo(), false,
									Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaUnidades, "codigo", "nome", false));
					Uteis.liberarListaMemoria(listaUnidades);
					// System.out.println("Final login: " + new Date());
				}
			}
			inicializarLogoApartirUsuario(getUsuarioLogadoClone());
		}
	}

	public void carregarUnidadeEnsinoInformada() throws Exception {
		setVisualizarComboUnidadeEnsino(false);
		getUsuario().setUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
				getCodUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado()));
		setNomeUnidadeEnsino(getUsuario().getUnidadeEnsinoLogado().getNome());
		carregarDadosTitulosApresentar();
	}

	public void carregarDadosTitulosApresentar() throws Exception {
		setTituloTelaBancoCurriculum(
				getAplicacaoControle().getConfiguracaoGeralSistemaVO(getCodUnidadeEnsino(), getUsuarioLogado())
						.getTituloTelaBancoCurriculum());
		setTituloTelaBuscaCandidato(
				getAplicacaoControle().getConfiguracaoGeralSistemaVO(getCodUnidadeEnsino(), getUsuarioLogado())
						.getTituloTelaBuscaCandidato());
	}

	public void selecionarUnidadeEnsino() {
		try {
			// Ao mudar a unidade de ensino na comboBox do modal de escolha da
			// visão, temos que montar
			// os dados da unidade de ensino, para verificações em métodos
			// posteriores.
			// setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getCodUnidadeEnsino(),
			// false));
			getUsuario().setUnidadeEnsinoLogado(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
					getCodUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado()));
			setNomeUnidadeEnsino(getUsuario().getUnidadeEnsinoLogado().getNome());
			inicializarLogoApartirUsuario(getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarRequisicao() {
//		try {
//			setRequisicaoVO((RequisicaoVO) context().getExternalContext().getRequestMap().get("requisicaoItemVOItens"));
//
//			if (Uteis.isAtributoPreenchido(getRequisicaoVO()) && Uteis
//					.isAtributoPreenchido(getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao())) {
//
//				getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao()
//						.setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade()
//								.consultarPorCodigoQuestionario(getRequisicaoVO().getCategoriaProduto()
//										.getQuestionarioEntregaRequisicao().getCodigo(), false,
//										Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
//
//				getRequisicaoVO().setQuestionarioRespostaOrigemFechamentoVO(getFacadeFactory().getRequisicaoFacade()
//						.realizarCriacaoQuestionarioRespostaOrigem(getRequisicaoVO(),
//								getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao(),
//								getUsuarioLogado()));
//				setAbrirModalQuestionario(true);
//			}
//			setMensagemID("msg_dados_selecionados");
//		} catch (Exception e) {
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
//		}
	}

	public void gravarRequisicaoComQuestionario() {
//		try {
//			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().getQuestionarioVO())
//					&& !Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO())) {
//				validarDados(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().getPerguntaRespostaOrigemVOs());
//				getFacadeFactory().getRequisicaoFacade().incluirQuestionarioRespostaAlterandoQuestionarioRespostaOrigemFechamentoRequisicao(getRequisicaoVO(), getUsuarioLogado());
//				getRequisicoesVOs().removeIf(p -> p.getCodigo().equals(getRequisicaoVO().getCodigo()));
//				if (!Uteis.isAtributoPreenchido(getRequisicoesVOs())) {
//					setApresentarRequisicoes(false);
//				}
//			}
//			setAbrirModalQuestionario(false);
//			setMensagemID("msg_requisicao_respostaQuestionarioFechamento", Uteis.SUCESSO);
//		} catch (Exception e) {
//			setAbrirModalQuestionario(true);
//			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
//		}
	}

	public boolean apresentarBotaoImprimirQuestionarioRequisicao() {
//		return Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().getCodigo());
		return false;
	}

	public void imprimirPDFEntregaQuestionario() throws Exception {
//		List<RequisicaoVO> listaObjetos = new ArrayList<>();
//
//		if (Uteis.isAtributoPreenchido(getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo())) {
//			getRequisicaoVO().setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(
//					getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo(),
//					getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
//					getUsuarioLogado()));
//		}
//		listaObjetos.add(getRequisicaoVO());
//		try {
//			if (!listaObjetos.isEmpty()) {
//				getSuperParametroRelVO().setTituloRelatorio("Requisição de Diárias");
//				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
//				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade()
//						.designIReportRelatorio(DESIGN_RELATORIO_QUESTIONARIO));
//				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
//				getSuperParametroRelVO().setSubReport_Dir(
//						getFacadeFactory().getRequisicaoRelFacade().caminhoBaseQuestionarioRelatorio());
//				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
//				getSuperParametroRelVO().setListaObjetos(listaObjetos);
//				getSuperParametroRelVO()
//						.setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
//				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//
//				realizarImpressaoRelatorio();
//				setMensagemID("msg_relatorio_ok");
//			} else {
//				setMensagemID("msg_relatorio_sem_dados");
//			}
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	public String getAcaoModalQuestionario() {
		if (getAbrirModalQuestionario()) {
			return "	PF('panelQuestionarioFechamento').show()";
		}
		return "PF('panelQuestionarioFechamento').hide();";
	}

	public void verificarValidarCpfSetarIpMaquinaLogada() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("validarCPF",
				getConfiguracaoGeralPadraoSistema().getValidarCadastroCpf());
		getUsuario().setIpMaquinaLogada(this.request().getRemoteAddr().toString());
	}
	
	public String verificarAvaliacaoInstitucionalAtivaParaUsuario(MatriculaVO matriculaVO) throws Exception {
		return verificarAvaliacaoInstitucionalAtivaParaUsuario(matriculaVO, null);
	}

	public String verificarAvaliacaoInstitucionalAtivaParaUsuario(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		boolean realizarVerificaoAvaliacoesInstitucionais = true;
		if (Uteis.isAtributoPreenchido(matriculaVO)) {
			if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO)) {
				realizarVerificaoAvaliacoesInstitucionais = configuracaoGeralSistemaVO.getHabilitarRecursosAcademicosVisaoAluno();
			}
		}
		if (realizarVerificaoAvaliacoesInstitucionais
				&& !getUsuario().getVisaoLogar().equals("erroLogin") 
				&& !getUsuario().getVisaoLogar().equals("candidato") 
				&& getUsuario().getPessoa().getCodigo().intValue() != 0) {
			if(matriculaVO != null && getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().containsKey(matriculaVO.getMatricula())) {
				getUsuario().setAvaliacaoInstitucionalVOs(getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().get(matriculaVO.getMatricula()));
			}else {
				getUsuario().setAvaliacaoInstitucionalVOs(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUsuarioLogado(getUsuario(), matriculaVO));
			}
			if (getUsuario().getAvaliacaoInstitucionalVOs().isEmpty()) {

				if (getUsuario().getIsApresentarVisaoAluno()) {
					return "/visaoAluno/telaInicialVisaoAluno.xhtml";
				} else if (getUsuario().getIsApresentarVisaoProfessor()) {
					return "/visaoProfessor/telaInicialVisaoProfessor.xhtml";
				} else if (getUsuario().getIsApresentarVisaoCoordenador()) {
					if (getNomeTelaAtual().contains("visaoCoordenador")) {
						return "telaInicialVisaoCoordenador.xhtml";
					} else {
						return "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
					}
				} else if (getUsuario().getIsApresentarVisaoAdministrativa()) {
					return "/visaoAdministrativo/administrativo/homeAdministrador.xhtml";
				}
			} else {
				Boolean existeQuestionario = Boolean.FALSE;
				int x = 0;
				for (AvaliacaoInstitucionalVO obj : getUsuario().getAvaliacaoInstitucionalVOs()) {
					obj = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(obj, matriculaVO, getUsuarioLogado());
					getUsuario().getAvaliacaoInstitucionalVOs().set(x, obj);
					x++;
//					if (Uteis.isAtributoPreenchido(obj.getQuestionarioVOs())) {
//						existeQuestionario = Boolean.TRUE;
//					}
				}
//				if (existeQuestionario) {
//					getUsuario().getAvaliacaoInstitucionalVOs().removeIf(a -> a.getQuestionarioVOs().isEmpty());
//					if (!getUsuario().getAvaliacaoInstitucionalVOs().stream().allMatch(a -> a.getQuestionarioVOs().isEmpty())) {
//						return "/visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml";
//					}
				}

				if (getUsuario().getIsApresentarVisaoAluno()) {
					return "/visaoAluno/telaInicialVisaoAluno.xhtml";
				} else if (getUsuario().getIsApresentarVisaoProfessor()) {
					return "/visaoProfessor/telaInicialVisaoProfessor.xhtml";
				} else if (getUsuario().getIsApresentarVisaoCoordenador()) {
					if (getNomeTelaAtual().contains("visaoCoordenador")) {
						return "telaInicialVisaoCoordenador.xhtml";
					} else {
						return "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
					}
				} else if (getUsuario().getIsApresentarVisaoAdministrativa()) {
					return "/visaoAdministrativo/administrativo/homeAdministrador.xhtml";
				}

			}

			return "/visaoAdministrativo/avaliacaoInstitucional/avaliacaoInstitucionalQuestionario.xhtml";

		}

	

	public Boolean consultaApresentarMensagemTelaLogin;

	public Boolean getConsultaApresentarMensagemTelaLogin() throws Exception {
		if (consultaApresentarMensagemTelaLogin == null) {
			consultaApresentarMensagemTelaLogin = false;
			try {
				ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
						.consultarPorMensagemTelaLoginConfiguracaoPadraoSistema();
				setMensagemTelaLogin(conf.getMensagemTelaLogin());
				consultaApresentarMensagemTelaLogin = conf.getApresentarMensagemTelaLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return consultaApresentarMensagemTelaLogin;
	}

	public Boolean consultaApresentarLinkHomeCandidato;

	public Boolean getConsultaApresentarLinkHomeCandidato() throws Exception {
		if (consultaApresentarLinkHomeCandidato == null) {
			try {
				consultaApresentarLinkHomeCandidato = false;
				ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
						.consultarPorApresentarHomeCandidato();
				consultaApresentarLinkHomeCandidato = conf.getApresentarHomeCandidato();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return consultaApresentarLinkHomeCandidato;
	}

	public Boolean consultaApresentarLinkEsqueceuMinhaSenha;

	public Boolean getConsultaApresentarLinkEsqueceuMinhaSenha() throws Exception {
		if (consultaApresentarLinkEsqueceuMinhaSenha == null) {
			try {
				consultaApresentarLinkEsqueceuMinhaSenha = false;
				ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
						.consultarPorApresentarEsqueceuMinhaSenha();
				consultaApresentarLinkEsqueceuMinhaSenha = conf.getApresentarEsqueceuMinhaSenha();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return consultaApresentarLinkEsqueceuMinhaSenha;
	}

	public String logout() {
		try {
			if (getUsuario() == null) {
				setUsuario(new UsuarioVO());
			}
			Integer codigoUnidade = getUsuario().getUnidadeEnsinoLogado().getCodigo();
			if ((codigoUnidade == null || codigoUnidade == 0)
					&& context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino") != null) {
				codigoUnidade = (Integer) context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino");
			}
			setEncontradoUsuarioLogadoEmDuasAbasNavegador(false);
			inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
			removerObjetoMemoria(this);
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			context().getExternalContext().getFlash().clear();
			context().getExternalContext().getSessionMap().put("codigoUnidadeEnsino", codigoUnidade);
			setMensagemID("msg_entre_prmlogout");
			if (getLogadoComEmailInstitucional()) {
				String retorno = this.logoutAD();
				removerTodosManagedBean();
				HttpSession Httpsession = (HttpSession) context().getExternalContext().getSession(false);
				Httpsession.invalidate();
				Uteis.setCookie("SAML", "", 0);
				Uteis.setCookie("SESSAOID", "", 0);
				return retorno;
			}
			HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
			session.invalidate();
			if (getNomeTelaAtual().endsWith("homeBibliotecaExterna.xhtml")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("homeBibliotecaExterna.xhtml");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("/index");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			if (getNomeTelaAtual().endsWith("homeBibliotecaExterna.xhtml")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("homeBibliotecaExterna.xhtml");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("/index");
		}
	}
	
	public String logoutAD() {
		try {
			context().getExternalContext().getSessionMap().remove("logadoComEmailInstitucional");
			Uteis.setCookie("SAML", "", 0);
			Uteis.setCookie("SESSAOID", "", 0);
			String emailInstitucionalValidar = getEmailInstitucional();
			if(Uteis.isAtributoPreenchido(getUsuarioLogado().getPessoa().getCodigo())) {
				PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getUsuarioLogado().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				emailInstitucionalValidar = emailInstitucional.getEmail();
			}
			ConfiguracaoLdapVO configuracaoLdapVO = this.obterConfiguracaoLdapVO(emailInstitucionalValidar);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(configuracaoLdapVO.getUrlLogoutAD());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ALERTA);
		}
		return "";
	}

	public String logoutVisao() {
		String retorno = "";
		try {
			Integer codigoUnidade = getUsuario().getUnidadeEnsinoLogado().getCodigo();
			if ((codigoUnidade == null || codigoUnidade == 0)
					&& context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino") != null) {
				codigoUnidade = (Integer) context().getExternalContext().getSessionMap().get("codigoUnidadeEnsino");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Logout Visao", "Logout");
			inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
			removerObjetoMemoria(this);
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			setUsername("");
			setSenha("");
			context().getExternalContext().getSessionMap().put("codigoUnidadeEnsino", codigoUnidade);
			setMensagemID("msg_entre_prmlogout");
			if (getLogadoComEmailInstitucional()) {
				retorno = this.logoutAD();
				removerTodosManagedBean();
				HttpSession Httpsession = (HttpSession) context().getExternalContext().getSession(false);
				Httpsession.invalidate();
				return retorno;
			}
//			return Uteis.getCaminhoRedirecionamentoNavegacao("../erroLogin");
			retorno = executarVerificacaoSimulacaoAcessoVisaoAluno();
			if (getNomeTelaAtual().endsWith("homeOuvidoriaCons.xhtml")) {
				removerTodosManagedBean();
				HttpSession Httpsession = (HttpSession) context().getExternalContext().getSession(false);
				Httpsession.invalidate();
				removerControleMemoriaFlashTela("OuvidoriaControle");
				return Uteis.getCaminhoRedirecionamentoNavegacao(
						"/visaoAdministrativo/administrativo/homeOuvidoriaForm.xhtml");
			}
			if (retorno.contains("index")) {
				removerTodosManagedBean();
				HttpSession Httpsession = (HttpSession) context().getExternalContext().getSession(false);
				Httpsession.invalidate();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			retorno = Uteis.getCaminhoRedirecionamentoNavegacao("/index.xhtml");
		}
		return retorno;
	}

	public String executarVerificacaoSimulacaoAcessoVisaoAluno() throws Exception {
		if (getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
			SimularAcessoAlunoVO simulacao = getUsuarioLogado().getSimularAcessoAluno();
			Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("usuarioLogado"));
			removerControleMemoriaFlashTela("UsuarioControle");
			removerControleMemoriaFlashTela("RenovarMatriculaControle");
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			setUsername(simulacao.getResponsavelSimulacaoAluno().getUsername());
			setSenha(simulacao.getResponsavelSimulacaoAluno().getSenha());
			String retorno = loginSistema(false, true);
			if (getUsuario().getPerfilAdministrador()) {
				setCodUnidadeEnsino(simulacao.getResponsavelSimulacaoAluno().getUnidadeEnsinoLogado().getCodigo());
				getUsuario().setUnidadeEnsinoLogado(simulacao.getResponsavelSimulacaoAluno().getUnidadeEnsinoLogado());
				if (simulacao.getResponsavelSimulacaoAluno().getTipoUsuario().equals("DM")) {
					retorno = logarDiretamenteComoAdministrador();
				} else {
					setVisualizarComboUnidadeEnsino(true);
					retorno = logarDiretamenteComoFuncionario();
				}
			}
			return retorno;
		} else {
			return Uteis.getCaminhoRedirecionamentoNavegacao("/index.xhtml");
		}
	}

	public String logoutVisaoCandidato() {
		try {
			// registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle",
			// "Logout Visão", "Logout");
			// inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
			// setUsuario(new UsuarioVO());
			// getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			removerTodosManagedBean();
			HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
			session.invalidate();
			removerObjetoMemoria(this);
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			setUsername("");
			setSenha("");
			setMensagemID("msg_entre_prmlogout");
//			return Uteis.getCaminhoRedirecionamentoNavegacao("../erroLogin");
			return Uteis.getCaminhoRedirecionamentoNavegacao("/index.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("../login");
			return Uteis.getCaminhoRedirecionamentoNavegacao("/homeCandidato.xhtml");
		}
	}

	public String abriRichModalEsqueceuSenha() {
		try {
			setOcultarBotoes(Boolean.FALSE);
			setMensagemID("msg_informe_usuario_para_redefinir_senha");
			setMensagemDetalhada("");
			return Uteis.getCaminhoRedirecionamentoNavegacao("redefinirSenha.xhtml");
		} catch (Exception e) {
			setMensagemID("msg_erro_login");
//			return Uteis.getCaminhoRedirecionamentoNavegacao("../erroLogin");
			return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
		}
	}

	public String abrirHomeCandidato() {
		try {
			setOncompleteModal("");
			carregarConfiguracaoCandidatoVO();
			if (getVisaoCandidatoControle() != null) {
				getVisaoCandidatoControle().setMenuInscricao(false);
			}
			if (!getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getUrlHomeCandidato().trim().isEmpty()) {
				setOncompleteModal("window.open('"
						+ getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getUrlHomeCandidato()
						+ "', '_balck')");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (getOncompleteModal().trim().isEmpty()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("visaoCandidato/homeCandidato");
		}
		return "";
	}

	public String cancelarRedefinirSenha() {
		setMensagemID("msg_entre_prmlogin");
		setMensagemDetalhada("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("index");
	}

	public String redefinirSenha() {
		try {
			
			if ((getEmailInstitucional() == null || getEmailInstitucional().isEmpty()) && (getUsername() == null || getUsername().isEmpty()) && (getCpf() == null || getCpf().isEmpty())) {
				throw new ConsistirException("Informe o E-MAIL, USUÁRIO ou CPF para redefinir a senha.");
			} else {
				UsuarioVO usua = null; 
				if (getEmailInstitucional() == null || getEmailInstitucional().isEmpty()){
					usua = getFacadeFactory().getUsuarioFacade().consultarPorUsernameCPFUnico(getUsername(),
						Uteis.retirarMascaraCPF(getCpf()), false, Uteis.NIVELMONTARDADOS_TODOS, null);
				}else {
//					try {
//						usua = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioEmailInstitucional(getEmailInstitucional(), Uteis.NIVELMONTARDADOS_DADOSLOGIN);
//					}catch (Exception e) {
//						throw new Exception("Nenhum USUÁRIO foi encontrado para este e-mail.");
//					}
				}
				if (usua == null) {
					throw new Exception("Nenhum USUÁRIO foi encontrado.");
				} else {
					setUsuario(usua);
					validarDadosUsuarioRedefinirSenha(usua);
				}
			}
			setMensagemID("msg_senhaRedefinidaSucesso", Uteis.SUCESSO, true);
			setOcultarBotoes(Boolean.TRUE);
			setApresentarRedefinirSenha(false);
			return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
//			return "redefinirSenha";
		} catch (ConsistirException e) {
			setOcultarBotoes(Boolean.FALSE);
			setMensagemDetalhada("msg_erroSenhaRedefinir", e.getMessage());
			if (!e.getMessage().contains("Já existe um email")) {				
				setMensagemID("msg_erroSenhaRedefinir", Uteis.ERRO, true);
			} else {
				
				setMensagemID("msg_erroSenhaRedefinirJaExiste", Uteis.ERRO, true);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
//			return "redefinirSenha";
		} catch (Exception e) {
			setOcultarBotoes(Boolean.FALSE);
			setMensagemDetalhada(e.getMessage());
			if (!e.getMessage().contains("Já existe um email")) {
				setMensagemID("msg_erroSenhaRedefinir", Uteis.ERRO, true);
			} else {
				
				setMensagemID("msg_erroSenhaRedefinirJaExiste", Uteis.ERRO, true);
			}
//			return "redefinirSenha";
			return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
		}
	}

	public String redefinirSenhaFinal(String token) {
		try {
			UsuarioVO usua = getFacadeFactory().getUsuarioFacade().consultarPorUsernameCPFUnico(getUsername(),
					Uteis.retirarMascaraCPF(getCpf()), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			if (usua == null) {
				throw new Exception("Nenhum USUÁRIO foi encontrado.");
			} else {
				usua.setUsername(getUsuario().getUsername());
				usua.setSenha(getUsuario().getSenha());
				usua.setValidarDados(Boolean.FALSE);
				getFacadeFactory().getUsuarioFacade().alterarSenha( usua, usua.getUsername(), usua.getSenha(),
						usua.getUsername(), usua.getSenha(), false);
				getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, usua, getSenha());
			}
			setMensagemID("msg_senhaRedefinidaSucesso");
			setOcultarBotoes(Boolean.TRUE);
			return "redefinirSenha";
		} catch (ConsistirException e) {
			setOcultarBotoes(Boolean.FALSE);
			setMensagemDetalhada("msg_erroSenhaRedefinir", e.getMessage());
			return "redefinirSenha";
		} catch (Exception e) {
			setOcultarBotoes(Boolean.FALSE);
			setMensagemDetalhada(e.getMessage());
			if (!e.getMessage().contains("Já existe um email")) {
				setMensagemID("msg_erroSenhaRedefinir");
			} else {
				setMensagemID("msg_erroSenhaRedefinirJaExiste");
			}
			return "redefinirSenha";
		}
	}

	public void validarDadosUsuarioRedefinirSenha(UsuarioVO usuario) throws Exception {
		// String mailServer = null;
		// String login = null;
		// String senha = null;
		String novaSenha = null;
		String link = "";
		String msg = null;
		ComunicacaoInternaVO comunicacaoInternaVO = null;
		ComunicadoInternoDestinatarioVO cidVO = null;
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguraoesEnvioEmail();
			comunicacaoInternaVO = new ComunicacaoInternaVO();
			cidVO = new ComunicadoInternoDestinatarioVO();
			novaSenha = Uteis.gerarSenhaRandomica(10, false);
			link = config.getUrlAcessoExternoAplicacao()
					+ "/ConverteGetParaPostServletFollowUp?pagina=redefinicaoSenha.xhtml&key=token&valor=" + novaSenha;
			msg = gerarMensagemRedefinirSenha2(usuario.getPessoa().getNome(), link);

			usuario.setTokenRedefinirSenha(novaSenha);
			if (config == null) {
				throw new Exception("Configuração Geral do Sistema não pode ser carregada!");
			}
			Boolean possuiEmail = getFacadeFactory().getEmailFacade()
					.consultarEmailRedefinirSenhaUsuario(usuario.getPessoa());
			if (possuiEmail) {
				throw new Exception(
						"Já existe um email de redefinição de senha na lista de envio, favor aguardar o recebimento do mesmo!");
			}
			EmailVO emailVO = new EmailVO();
			emailVO.setNomeDest(usuario.getPessoa().getNome());
			String emailDest = usuario.getPessoa().getEmail();
			emailVO.setEmailDest(emailDest.trim());
			emailVO.setAnexarImagensPadrao(true);
			emailVO.setEmailRemet(config.getEmailRemetente());
			if (emailVO.getEmailRemet().equals("")) {
				emailVO.setEmailRemet("envio@sistema.com");
			}
			emailVO.setNomeRemet("SEI - SISTEMA EDUCACIONAL EDUCACIONAL");
			emailVO.setAssunto("Redefinir Senha");
			emailVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(msg));
			if (emailVO.getMensagem().contains("#NOMEALUNO")) {
				emailVO.setMensagem(Uteis.trocarHashTag("#NOMEALUNO", emailVO.getNomeDest(), emailVO.getMensagem()));
			}
			if (usuario.getTipoUsuario().equals(TipoUsuario.ALUNO.getValor())) {
				List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade()
						.consultaRapidaBasicaPorCodigoPessoaNaoCancelada(usuario.getPessoa().getCodigo(), false, false,
								true, true, usuario, config);
				if (!matriculaVOs.isEmpty()) {
					MatriculaVO matriculaVO = matriculaVOs.get(0);
					if (!matriculaVO.getUnidadeEnsino().getCodigo().equals(0)) {
						UnidadeEnsinoVO unidadeEnsino = (getFacadeFactory().getUnidadeEnsinoFacade()
								.consultaRapidaPorCodigo(matriculaVO.getUnidadeEnsino().getCodigo(), false, usuario));
						carregarDadosDaUnidadeEnsinoParaRedefinicaoSenha(comunicacaoInternaVO, emailVO, unidadeEnsino,
								config, usuario);
					}
				}
			} else if (!usuario.getUsuarioPerfilAcessoVOs().isEmpty()) {
				UnidadeEnsinoVO unidadeEnsino = (getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(
						usuario.getUsuarioPerfilAcessoVOs().get(0).getUnidadeEnsinoVO().getCodigo(), false, usuario));
				carregarDadosDaUnidadeEnsinoParaRedefinicaoSenha(comunicacaoInternaVO, emailVO, unidadeEnsino, config,
						usuario);
			}

			// enviarEmail();
			if (Uteis.validarEnvioEmail(config.getIpServidor())) {
				ipServidor = config.getIpServidor();
				smtpPadrao = config.getSmptPadrao();
				loginServidorSmtp = config.getLogin();
				emailRemetente = config.getEmailRemetente();
				senhaServidorSmtp = config.getSenha();
				portaSmtpPadrao = config.getPortaSmtpPadrao();
				listaAnexosExcluir = new ArrayList<String>();
				codigoEmails = "";
				try {
					enviarEmail(emailVO.getEmailDest(), emailVO.getNomeDest(), emailVO.getEmailRemet(),
							emailVO.getNomeRemet(), emailVO.getAssunto(), emailVO.getMensagem(),
							emailVO.getCaminhoAnexos(), emailVO.getAnexarImagensPadrao(),
							config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(),
							emailVO.getCaminhoLogoEmailCima(), emailVO.getCaminhoLogoEmailBaixo());
					getFacadeFactory().getUsuarioFacade().alterarTokenRedefinirSenha(usuario);
				} catch (Exception e) {
					getFacadeFactory().getEmailFacade().incluir(emailVO);
					getFacadeFactory().getUsuarioFacade().alterarTokenRedefinirSenha(usuario);
				}
				getFacadeFactory().getUsuarioFacade().alterarTokenRedefinirSenha(usuario);
			} else {
				throw new Exception(
						"Sistema não configurado para envio de email! Entre em contato com a instituição de ensino!");
			}
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
			throw e;
		} finally {
			novaSenha = null;
			msg = null;
			comunicacaoInternaVO = null;
			cidVO = null;
		}
	}

	public void carregarDadosDaUnidadeEnsinoParaRedefinicaoSenha(ComunicacaoInternaVO comunicacaoInternaVO,
			EmailVO emailVO, UnidadeEnsinoVO unidadeEnsino, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) {
		comunicacaoInternaVO.setUnidadeEnsino(unidadeEnsino);
		getFacadeFactory().getComunicacaoInternaFacade().realizarTrocarLogoEmailPorUnidadeEnsino(comunicacaoInternaVO,
				config, usuario);
		if (Uteis.isAtributoPreenchido(comunicacaoInternaVO.getUnidadeEnsino())
				&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())
				&& Uteis.isAtributoPreenchido(comunicacaoInternaVO.getUnidadeEnsino().getNomeArquivoLogoEmailCima())) {
			emailVO.setCaminhoLogoEmailCima(config.getLocalUploadArquivoFixo() + File.separator
					+ comunicacaoInternaVO.getUnidadeEnsino().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/")
					+ "/" + comunicacaoInternaVO.getUnidadeEnsino().getNomeArquivoLogoEmailCima());
		}
		if (Uteis.isAtributoPreenchido(comunicacaoInternaVO.getUnidadeEnsino())
				&& Uteis.isAtributoPreenchido(config.getLocalUploadArquivoFixo())
				&& Uteis.isAtributoPreenchido(comunicacaoInternaVO.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo())) {
			emailVO.setCaminhoLogoEmailBaixo(config.getLocalUploadArquivoFixo() + File.separator
					+ comunicacaoInternaVO.getUnidadeEnsino().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/")
					+ "/" + comunicacaoInternaVO.getUnidadeEnsino().getNomeArquivoLogoEmailBaixo());
		}
	}

	public void enviarEmail() {
		ConfiguracaoGeralSistemaVO config = null;
		try {
			List<EmailVO> listaEmail = new ArrayList<EmailVO>();
			try {
				config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			} catch (DataAccessException dae) {
				config = consultarConfiguracaoGeralSistemaConexaoManual();
			} catch (Exception e) {
			}
			ipServidor = config.getIpServidor();
			smtpPadrao = config.getSmptPadrao();
			loginServidorSmtp = config.getLogin();
			emailRemetente = config.getEmailRemetente();
			senhaServidorSmtp = config.getSenha();
			portaSmtpPadrao = config.getPortaSmtpPadrao();
			listaAnexosExcluir = new ArrayList<String>();
			codigoEmails = "";
			if (Uteis.validarEnvioEmail(ipServidor)) {
				listaEmail = getFacadeFactory().getEmailFacade().consultarEmailsRedefinirSenha(config, false);
				enviarEmails(listaEmail, config);
			}
		} catch (Exception ex) {
		}
	}

	public void enviarEmails(List<EmailVO> listaEmail, ConfiguracaoGeralSistemaVO config) {

		try {
			if (!listaEmail.isEmpty()) {
				for (EmailVO email : listaEmail) {
					try {
						if (Uteis.validarEnvioEmail(ipServidor)) {
							enviarEmail(email.getEmailDest(), email.getNomeDest(), email.getEmailRemet(),
									email.getNomeRemet(), email.getAssunto(), email.getMensagem(),
									email.getCaminhoAnexos(), email.getAnexarImagensPadrao(),
									config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(),
									email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo());
						}
						codigoEmails += email.getCodigo().toString() + ",";
						if (Uteis.validarEnvioEmail(ipServidor)) {
							getFacadeFactory().getEmailFacade().realizarExclusaoEmail(email);
						}
					} catch (Exception e) {
					} finally {
					}
				}
				config = null;
			}
		} catch (Exception e) {
		} finally {
			smtpPadrao = null;
			loginServidorSmtp = null;
			emailRemetente = null;
			senhaServidorSmtp = null;
			portaSmtpPadrao = null;
			listaFileCorpoMensagem = null;
			listaAnexosExcluir = null;
		}
	}

	public void enviarEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto,
			String corpo, String caminhoAnexos, Boolean anexarImagensPadrao, Boolean servidorEmailUtilizaSSL,
			Boolean servidorEmailUtilizaTSL, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo)
			throws Exception {
		Session session = null;
		MimeMessage message = null;
		MimeMultipart multipart = null;
		BodyPart messageBodyPart = null;
		Properties props = null;

		try {
			props = System.getProperties();
			if (props != null) {
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.host", smtpPadrao);
				if (portaSmtpPadrao != null && !portaSmtpPadrao.equals("")) {
					props.put("mail.smtp.port", portaSmtpPadrao);
				}
				props.put("mail.smtp.auth", "true");
				/*
				 * Para TSL
				 */
				if (servidorEmailUtilizaTSL) {
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.mime.charset", "ISO-8859-1");
					props.put("mail.smtp.user", loginServidorSmtp);
					props.put("mail.smtp.password", senhaServidorSmtp);
					props.remove("mail.smtp.socketFactory.class");
					//props.remove("mail.smtp.port");
					props.put("mail.smtp.socketFactory.port", portaSmtpPadrao);
					props.put("mail.smtp.socketFactory.fallback", "false");
					props.put("mail.smtp.quitwait", "false");
					props.put("mail.smtp.ssl.protocols", "SSLv3 TLSv1 TLSv1.1 TLSv1.2");
					props.put("mail.smtp.ssl.trust",  "*");
				} else if (servidorEmailUtilizaSSL) {
					final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
					props.remove("mail.smtp.starttls.enable");
					props.remove("mail.mime.charset");
					props.remove("mail.smtp.user");
					props.remove("mail.smtp.password");
					props.remove("mail.smtp.socketFactory.class");
					props.remove("mail.smtp.port");
					props.remove("mail.smtp.socketFactory.port");
					props.remove("mail.smtp.socketFactory.fallback");
					props.remove("mail.smtp.quitwait");
					props.remove("mail.smtp.ssl.protocols");
					props.remove("mail.smtp.ssl.trust");

					props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
					props.put("mail.smtp.socketFactory.fallback", "false");
					props.put("mail.smtp.socketFactory.port", portaSmtpPadrao);
				} else {

					/*
					 * Comentado por Wendel Rodrigues no dia 03/09/2014. Se não utilizar TSL ou SSL,
					 * as configurações de usuario, senha e porta não poderão ser removidas. Porque
					 * a mesma é utilizada para efetuar a autenticação no servidor de email.
					 */
				}
				session = Session.getInstance(props, getAuthenticator(loginServidorSmtp.trim(), senhaServidorSmtp));
				session.setDebug(false);

				message = new MimeMessage(session);
				message.setSentDate(new Date());
				message.setFrom(
						new InternetAddress(emailRemetente, MimeUtility.encodeText(nomeRemet, "ISO-8859-1", "Q")));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailDest, nomeDest));
				message.setSubject(MimeUtility.encodeText(assunto, "ISO-8859-1", "Q"));

				corpo = alterarNomeImagemEmail(corpo, caminhoLogoEmailCima, caminhoLogoEmailBaixo);

				multipart = new MimeMultipart("related");
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(corpo, "text/html; charset=ISO-8859-1");

				multipart.addBodyPart(messageBodyPart);
				if (anexarImagensPadrao
						&& (corpo.contains("<img id=\"_x0000_i1025\"") || corpo.contains("<img id=\"_x0000_i1028\""))) {
					criarFileCorpoMensagemEmail(multipart, caminhoLogoEmailCima, caminhoLogoEmailBaixo);
				}
				criarFileAtendimentoCorpoMensagemEmail(multipart, corpo);
				if (!caminhoAnexos.isEmpty() && !caminhoAnexos.trim().equals("\"")) {
					criarAnexoEmail(multipart, caminhoAnexos);
				}

				if (servidorEmailUtilizaTSL) {
					MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
					mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
					mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
					mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
					mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
					mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
					CommandMap.setDefaultCommandMap(mc);
				}

				message.setContent(multipart);
				message.saveChanges();

				long start = System.currentTimeMillis();
				Transport.send(message);
				long elapsed = System.currentTimeMillis() - start;
				if (elapsed >= 60000) {

				}
			}
		} catch (AuthenticationFailedException e) {
			throw e;
		} catch (Exception e) {
			if (e.getMessage().contains("Access to default session denied")) {
				throw new Exception(
						"Ocorreu um erro durante o envio do e-mail. A permissão de acesso à sessão de email foi negada.");
			}
			throw e;
		} finally {
			session = null;
			message = null;
			multipart = null;
			messageBodyPart = null;
			props = null;

		}
	}

	private void criarAnexoEmail(MimeMultipart multipart, String caminhoAnexos) throws Exception {
		for (String caminhoArquivo : caminhoAnexos.split(";")) {
			File file = new File(caminhoArquivo);
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(file);
			messageBodyPart.setDisposition(Part.ATTACHMENT);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setFileName(file.getName());
			multipart.addBodyPart(messageBodyPart);
			if (!listaAnexosExcluir.contains(caminhoArquivo)) {
				listaAnexosExcluir.add(caminhoArquivo);
			}
		}
	}

	private void criarFileAtendimentoCorpoMensagemEmail(MimeMultipart multipart, String corpo) throws Exception {
		// BodyPart messageBodyPart
		listaFileCorpoMensagem = new ArrayList<File>();
		if (corpo.contains("cid:star1")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento"
					+ File.separator + "star1.png"));
		}
		if (corpo.contains("cid:star2")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento"
					+ File.separator + "star2.png"));
		}
		if (corpo.contains("cid:star3")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento"
					+ File.separator + "star3.png"));
		}
		if (corpo.contains("cid:star4")) {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "atendimento"
					+ File.separator + "star4.png"));
		}
		for (File imagem : listaFileCorpoMensagem) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(imagem);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID",
					"<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
			multipart.addBodyPart(messageBodyPart);
		}
	}

	private String alterarNomeImagemEmail(String texto, String caminhoLogoEmailCima, String caminhoLogoEmailBaixo)
			throws Exception {
		File imagemCima = null;
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailCima)) {
			imagemCima = new File(caminhoLogoEmailCima);
		} else {
			imagemCima = new File(
					UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator + "cima_sei.jpg");
		}
		if (texto.contains("<img id=\"_x0000_i1025\"")) {
			String parteCima1, parteCima2;
			parteCima1 = texto.substring(texto.indexOf("<img id=\"_x0000_i1025\""), texto.length());
			parteCima2 = parteCima1.substring(0, parteCima1.indexOf("/>") + 2);
			texto = texto.replaceAll(parteCima2,
					"<img id=\"_x0000_i1025\" src=\"cid:"
							+ imagemCima.getName().substring(0, imagemCima.getName().lastIndexOf("."))
							+ "\" border=\"0\" alt=\""
							+ imagemCima.getName().substring(0, imagemCima.getName().lastIndexOf(".")) + "\" />");
		}
		File imagemBaixo = null;
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailBaixo)) {
			imagemBaixo = new File(caminhoLogoEmailBaixo);
		} else {
			imagemBaixo = new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email" + File.separator
					+ "baixo_sei.jpg");
		}
		if (texto.contains("<img id=\"_x0000_i1028\"")) {
			String parteBaixo1, parteBaixo2;
			parteBaixo1 = texto.substring(texto.indexOf("<img id=\"_x0000_i1028\""), texto.length());
			parteBaixo2 = parteBaixo1.substring(0, parteBaixo1.indexOf("/>") + 2);
			texto = texto.replaceAll(parteBaixo2,
					"<img id=\"_x0000_i1028\" src=\"cid:"
							+ imagemBaixo.getName().substring(0, imagemBaixo.getName().lastIndexOf("."))
							+ "\" border=\"0\" alt=\""
							+ imagemBaixo.getName().substring(0, imagemBaixo.getName().lastIndexOf(".")) + "\" />");
		}

		return texto;
	}

	private void criarFileCorpoMensagemEmail(MimeMultipart multipart, String caminhoLogoEmailCima,
			String caminhoLogoEmailBaixo) throws Exception {
		// BodyPart messageBodyPart
		listaFileCorpoMensagem = new ArrayList<File>();
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailCima)) {
			File imagem = new File(caminhoLogoEmailCima);
			listaFileCorpoMensagem.add(imagem);
		} else {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email"
					+ File.separator + "cima_sei.jpg"));
		}
		if (Uteis.isAtributoPreenchido(caminhoLogoEmailBaixo)) {
			File imagem = new File(caminhoLogoEmailBaixo);
			listaFileCorpoMensagem.add(imagem);
		} else {
			listaFileCorpoMensagem.add(new File(UteisJSF.obterCaminhoWebImagemClass() + File.separator + "email"
					+ File.separator + "baixo_sei.jpg"));
		}

		for (File imagem : listaFileCorpoMensagem) {
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource fds = new FileDataSource(imagem);
			messageBodyPart.setDataHandler(new DataHandler(fds));
			messageBodyPart.setHeader("Content-ID",
					"<" + imagem.getName().substring(0, imagem.getName().lastIndexOf(".")) + ">");
			multipart.addBodyPart(messageBodyPart);
		}
	}

	private Authenticator getAuthenticator(final String userName, final String senha) {
		if (auth == null) {
			auth = new Authenticator() {

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, senha);
				}
			};
		}
		return auth;
	}

	public static ConfiguracaoGeralSistemaVO consultarConfiguracaoGeralSistemaConexaoManual() throws Exception {
		Connection cnn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sqlStr = "SELECT smptpadrao, login, senha, portaSmtpPadrao, servidorEmailUtilizaSSL FROM configuracaogeralsistema limit 1";
			cnn = getConexaoManual();
			st = cnn.prepareStatement(sqlStr.toString());
			rs = st.executeQuery();
			if (rs.next()) {
				ConfiguracaoGeralSistemaVO obj = new ConfiguracaoGeralSistemaVO();
				obj.setSmptPadrao(rs.getString("smptpadrao"));
				obj.setLogin(rs.getString("login"));
				obj.setSenha(rs.getString("senha"));
				obj.setPortaSmtpPadrao(rs.getString("portaSmtpPadrao"));
				obj.setServidorEmailUtilizaSSL(rs.getBoolean("servidorEmailUtilizaSSL"));
				return obj;
			}
			return new ConfiguracaoGeralSistemaVO();
		} catch (Exception e) {
			// System.out.println("Erro
			// ThreadEmail.consultarConfiguracaoGeralSistemaConexaoManual: "
			// + e.getMessage());
			throw e;
		} finally {
			if (!cnn.isClosed()) {
				cnn.close();
			}
			cnn = null;
			st = null;
			rs = null;
		}
	}

	public static Connection getConexaoManual() throws Exception {
		Connection conn = null;
		try {
			String url = "jdbc:postgresql://localhost:5432/sei_univesp";
			String username = "postgres";
			String password = "admin";
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			// System.out.println("Erro ThreadEmail.criarConexao: " +
			// e.getMessage());
			throw e;
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
	}

	public String gerarMensagemRedefinirSenha2(String userName, String linkRedefinirSenha) {
		StringBuilder sb = new StringBuilder();
		sb.append("Prezado(a) ").append(userName).append(", \r <br />");
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("ACESSE O LINK ABAIXO PARA ALTERAR A SENHA DE ACESSO:  \r <br />");
		sb.append("\r <br />");
		sb.append(" <a href=\"" + linkRedefinirSenha + "\"> Clique Aqui para Redefinir a Senha</a>");
//		sb.append("SEGUE SUA NOVA SENHA:  ").append(senha).append(" \r <br />");
		sb.append("\r <br />");
		sb.append("\r <br />");
		return sb.toString();
	}

	public String gerarMensagemRedefinirSenha(String senha, String userName) {
		StringBuilder sb = new StringBuilder();
		sb.append("Prezado(a) ").append(userName).append(", \r <br />");
		sb.append("\r <br />");
		sb.append("\r <br />");
		sb.append("SEGUE SUA NOVA SENHA:  ").append(senha).append(" \r <br />");
		sb.append("\r <br />");
		sb.append("\r <br />");
		return getMensagemFormatada(sb.toString());
	}

	public String getMensagemFormatada(String mensagem) {
		String temp = getConfiguracaoGeralPadraoSistema().getMensagemPadrao();
		if (temp.equals("")) {
			return mensagem;
		}
		String caminho = getCaminhoPastaWeb();
		temp = temp.replaceAll("http://localhost:8080/SEI/", caminho);
		temp = temp.replace("<TEXTO PADRAO>", mensagem);
		return temp;
	}

	public void validarDadosCssProfessor() {
		if (getUsuario().getPessoa().getValorCssTopoLogo().equals("")
				&& getUsuario().getPessoa().getValorCssBackground().equals("")
				&& getUsuario().getPessoa().getValorCssMenu().equals("")) {
			try {
				getUsuario().getPessoa().setValorCssTopoLogo(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoProfessor().getValorCssTopoLogo());
				getUsuario().getPessoa().setValorCssBackground(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoProfessor().getValorCssBackground());
				getUsuario().getPessoa().setValorCssMenu(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoProfessor().getValorCssMenu());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void validarDadosCssAluno() {
		if (getUsuario().getPessoa().getValorCssTopoLogo().equals("")
				&& getUsuario().getPessoa().getValorCssBackground().equals("")
				&& getUsuario().getPessoa().getValorCssMenu().equals("")) {
			try {
				getUsuario().getPessoa().setValorCssTopoLogo(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoAluno().getValorCssTopoLogo());
				getUsuario().getPessoa().setValorCssBackground(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoAluno().getValorCssBackground());
				getUsuario().getPessoa().setValorCssMenu(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoAluno().getValorCssMenu());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void validarDadosCssCoordenador() {
		if (getUsuario().getPessoa().getValorCssTopoLogo().equals("")
				&& getUsuario().getPessoa().getValorCssBackground().equals("")
				&& getUsuario().getPessoa().getValorCssMenu().equals("")) {
			try {
				getUsuario().getPessoa().setValorCssTopoLogo(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoCoordenador().getValorCssTopoLogo());
				getUsuario().getPessoa().setValorCssBackground(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoCoordenador().getValorCssBackground());
				getUsuario().getPessoa().setValorCssMenu(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade()
						.getVisaoPadraoCoordenador().getValorCssMenu());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void montarPermissoesMenu(PerfilAcessoVO perfilAcessoVO) {
		setPermissaoAcessoMenuVO(getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenu(perfilAcessoVO));
		if (!getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			montarPermissoesMenuFavorito();
		}
//		consultarBannerMarketing();
//		inicializarBanner();
		consultarLinksUteisUsuario();
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			consultarConfiguracaoDashboard();
		}
		consultarPreferenciaSistemaUsuario();
	}

	public void montarPermissoesMenuFavorito() {
		try {
			List<FavoritoVO> lista = getFacadeFactory().getFavoritoFacade().consultarPorUsuario(
					getUsuarioLogado().getCodigo(), getUsuarioLogado().getTipoVisaoAcesso(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getUsuarioLogado()
					.setListaFavoritos(getUsuarioLogado().montarMenuFavorito(getPermissaoAcessoMenuVO(), lista));
		} catch (Exception e) {
			// System.out.print("ERRO AKI");
		}
	}

	public void montarPermissoesMenuMaisDeUmPerfil(UsuarioVO usuarioVO) {
		setPermissaoAcessoMenuVO(
				getFacadeFactory().getPerfilAcessoFacade().montarPermissoesMenuComMaisDeUmPerfil(usuarioVO));
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List lista = null;
		try {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(lista, "codigo", "nome"));
		} catch (Exception e) {
			getListaSelectItemUnidadeEnsino().clear();
		} finally {
			Uteis.liberarListaMemoria(lista);
		}
	}

	public String loginParceiro() {
		String mensagemErroSenha = "";
		try {
			setUsername(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(getUsername()));
			UsuarioVO usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsername(), getSenha(), true,
					Uteis.NIVELMONTARDADOS_DADOSLOGIN);
			setUsuario(usuarioVO);
			context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuario());
			registrarAtividadeUsuario(usuarioVO, "LoginControle", "Iniciando Login", "Consulta");
			inicializarDadosUsuario(usuarioVO);
			registrarUltimoAcesso(usuarioVO);
			usuarioVO.setPerfilAcesso(getUsuario().getUsuarioPerfilAcessoVOs().get(0).getPerfilAcesso());
			montarPermissoesMenu(getUsuario().getUsuarioPerfilAcessoVOs().get(0).getPerfilAcesso());

			return "parceiro";
		} catch (Exception e) {
			try {
				mensagemErroSenha = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarMensagemErroSenha();
				if (mensagemErroSenha == null || mensagemErroSenha.equals("")) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				} else {
					setMensagemDetalhada("msg_erro", mensagemErroSenha);
				}
			} catch (Exception ex) {
				setMensagemDetalhada("msg_erro", ex.getMessage());
			}
			return "";
		}
	}

	public String navegarTelaCadastroParceiro() throws Exception {
//		try {
//			if (getParceiroVO().getCNPJ().equals("")) {
//				throw new Exception("O campo CNPJ deve ser informado.");
//			}
//
//			List<UsuarioVO> lista = getFacadeFactory().getUsuarioFacade().consultarPorUsername(
//					Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(getParceiroVO().getCNPJ()), false,
//					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuario());
//
//			if (!lista.isEmpty()) {
//				throw new Exception("Já existe um usuário cadastrado com o CNPJ informado.");
//			}
//			getParceiroVO().setParticipaBancoCurriculum(true);
//			context().getExternalContext().getSessionMap().put(ParceiroVO.class.getSimpleName(), getParceiroVO());
//			context().getExternalContext().getSessionMap().put("usuarioLogado", new UsuarioVO());
//			setMensagemID("msg_entre_dados");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "";
//		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao("cadastroVisaoParceiro.xhtml");
		
		return null;
	}

	public String getMensagemTelaBancoCurriculum() throws Exception {
//		setParceiroVO(new ParceiroVO());
//		setMensagemDetalhada("");
//		return getConfiguracaoGeralPadraoSistema().getMensagemTelaBancoCurriculum();
		return "";
	}

	// public String getTituloTelaBancoCurriculum() throws Exception {
	// setParceiroVO(new ParceiroVO());
	// ConfiguracaoGeralSistemaVO conf =
	// getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistemaTextoBancoCurriculum();
	// setConfiguracaoGeralSistemaVO(conf);
	// setMensagemDetalhada("");
	// return getConfiguracaoGeralSistemaVO().getTituloTelaBancoCurriculum();
	// }
	public void inicializarObjetoParceiroVO() {
//		try {
//			if (context() != null) {
//				ParceiroVO parceiro;
//				parceiro = (ParceiroVO) context().getExternalContext().getSessionMap()
//						.get(ParceiroVO.class.getSimpleName());
//				if (parceiro != null) {
//					setParceiroVO(parceiro);
//					setUsername(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(getParceiroVO().getCNPJ()));
//					setSenha(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(getParceiroVO().getCNPJ()));
//					loginParceiro();
//				}
//			}
//		} catch (RuntimeException e) {
//		}
	}

	// public String realizarSelecaoUnidadeEnsinoParaCoordenador() {
	// try {
	// setAlunoPorResponsavelLegal(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getAlunoPorResponsavelLegal().getCodigo(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	// realizarPreenchimentoDadosParaUsuarioPorResponsavelLegal();
	// existeProcessoMatriculaAberto = null;
	// setMensagemID("msg_dados_selecionados");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// return "coordenador";
	// }
	public String logoutVisaoParceiro() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "LoginControle", "Logout Visão Parceiro", "Logout");
			inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			removerTodosManagedBean();
			HttpSession session = (HttpSession) context().getExternalContext().getSession(false);
			session.invalidate();
			removerObjetoMemoria(this);
			setUsuario(new UsuarioVO());
			getUsuario().setPerfilAcesso(new PerfilAcessoVO());
			setUsername("");
			setSenha("");
			setMensagemID("msg_entre_prmlogout");
			return "bancoCurriculos";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String menuPrincipal() {
		return "menuPrincipal";
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

	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getDataLogin() {
		if (dataLogin == null) {
			dataLogin = "";
		}
		return dataLogin;
	}

	public void setDataLogin(String dataLogin) {
		this.dataLogin = dataLogin;
	}

	public String getDiaSemanaLogin() {
		if (diaSemanaLogin == null) {
			diaSemanaLogin = Uteis.getDiaSemana_Apresentar() + " - " + Uteis.getHoraAtual() + "h";
		}
		return diaSemanaLogin;
	}

	public void setDiaSemanaLogin(String diaSemanaLogin) {
		this.diaSemanaLogin = diaSemanaLogin;
	}

	public String getDiaSemanaLoginSemHora() {
		if (diaSemanaLoginSemHora == null) {
			diaSemanaLoginSemHora = Uteis.getDiaSemana_Apresentar();
		}
		return diaSemanaLoginSemHora;
	}

	public void setDiaSemanaLoginSemHora(String diaSemanaLoginSemHora) {
		this.diaSemanaLoginSemHora = diaSemanaLoginSemHora;
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

	public String getOpcao() {
		if (opcao == null) {
			opcao = "";
		}
		return opcao;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public PermissaoAcessoMenuVO getPermissaoAcessoMenuVO() {
		if (permissaoAcessoMenuVO == null) {
			permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		}
		return permissaoAcessoMenuVO;
	}

	public void setPermissaoAcessoMenuVO(PermissaoAcessoMenuVO permissaoAcessoMenuVO) {
		this.permissaoAcessoMenuVO = permissaoAcessoMenuVO;
	}

	private String navegacaoPadraoParaPaginaHome;

	public String getNavegacaoPadraoParaPaginaHome() {
		try {
			if (navegacaoPadraoParaPaginaHome == null) {
				if (this.getUsuarioLogado().getPessoa().getFuncionario()
						|| this.getUsuarioLogado().getPerfilAdministrador()) {
					if (this.getUsuarioLogado().getPerfilAdministrador()) {
						navegacaoPadraoParaPaginaHome = "homeAdministrador";
					} else {
						navegacaoPadraoParaPaginaHome = "homePadrao";
					}
				} else {
					navegacaoPadraoParaPaginaHome = "";
				}
			}

		} catch (Exception e) {
			navegacaoPadraoParaPaginaHome = "";
		} finally {
			return navegacaoPadraoParaPaginaHome;
		}
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

	public Vector getVectorNomeMenu(Map<String, Object> obj) {
		Vector vItemMenu = new Vector(obj.keySet());
		Collections.sort(vItemMenu);
		return vItemMenu;
	}

	public Boolean getAcesso() {
		if (acesso == null) {
			acesso = Boolean.FALSE;
		}
		return acesso;
	}

	public void setAcesso(Boolean acesso) {
		this.acesso = acesso;
	}

	public String getAbrirModalAlterarSenha() {
		return "PF('panelMudarSenha').show();";
	}

	public void verificarDataComemorativaApresentar2() {
		try {
			if (getListaDataComemorativa().isEmpty()) {
				setUrlImagemApresentarDataComemorativa("/resources/V2/imagens/logo-sei-otimize.png");
			} else {
				// int posicao = ((int)Math.random() *
				// (getListaDataComemorativa().size()));
				DataComemorativaVO dataCom = (DataComemorativaVO) getListaDataComemorativa().get(getPosicao());
				if (dataCom.getArquivoImagemTopo().getCodigo().intValue() == 0) {
					setUrlImagemApresentarDataComemorativa("/resources/V2/imagens/logo-sei-otimize.png");
				} else {
					setUrlImagemApresentarDataComemorativa(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
							dataCom.getArquivoImagemTopo(), PastaBaseArquivoEnum.IMAGEM.getValue(),
							getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.png", false));
				}
				if (getListaDataComemorativa().size() > 0) {
					if (getPosicao() == 0) {
						setPosicao(getListaDataComemorativa().size() - 1);
					} else {
						setPosicao(getPosicao() - 1);
					}
				} else {
					setPosicao(getListaDataComemorativa().size() - 1);
				}
				// dataCom.setDataDiaAntecedencia(Uteis.obterDataFutura(dataCom.getDataDiaAntecedencia(),
				// 1));
				// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
				// Collections.sort((List) getListaDataComemorativa(),
				// ordenador);
			}
		} catch (Exception e) {
			setPosicao(getListaDataComemorativa().size() - 1);
			setUrlImagemApresentarDataComemorativa("/resources/V2/imagens/logo-sei-otimize.png");
		}
	}

	public void mudarSenha() {
		UsuarioVO usuario = null;
		setOncompleteModal("");
		try {
			usuario = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(getUsuarioLogado().getUsername(), getSenha(), true,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			if (usuario.getCodigo() != 0) {
				if (getNovaSenha().equals(getNovaSenha2())) {
					usuario.setSenha(getNovaSenha());
					getFacadeFactory().getUsuarioFacade().alterarSenha(true, usuario);
					getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, usuario, getNovaSenha());
				} else {
					setMensagemDetalhada("msg_erro", "Nova Senha não Confere");
					return;
				}
			} else {
				setMensagemDetalhada("msg_erro", "Senha Atual não Confere");
				return;
			}
			setSenha(null);
			setNovaSenha(null);
			setNovaSenha2(null);
			setMensagemDetalhada("Senha Alterada com Sucesso");
			setOncompleteModal("Rich.$('alterarSenha').hide()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			removerObjetoMemoria(usuario);
		}
	}

	public String getNovaSenha() {
		if (novaSenha == null) {
			novaSenha = "";
		}
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getNovaSenha2() {
		if (novaSenha2 == null) {
			novaSenha2 = "";
		}
		return novaSenha2;
	}

	public void setNovaSenha2(String novaSenha2) {
		this.novaSenha2 = novaSenha2;
	}

	public void setMostrarBotaoMudarSenha(Boolean mostrarBotaoMudarSenha) {
		this.mostrarBotaoMudarSenha = mostrarBotaoMudarSenha;
	}

	public Boolean getIsMostrarBotaoMudarSenha() throws Exception {
		if (getUsuarioLogado() != null && !getUsuarioLogado().getPerfilAdministrador()) {
			return true;
		}
		return false;
	}

	public Integer getCodUnidadeEnsino() {
		if (codUnidadeEnsino == null) {
			codUnidadeEnsino = 0;
		}
		return codUnidadeEnsino;
	}

	public void setCodUnidadeEnsino(Integer codUnidadeEnsino) {
		this.codUnidadeEnsino = codUnidadeEnsino;
	}

	public void setVisualizarComboUnidadeEnsino(Boolean visualizarComboUnidadeEnsino) {
		this.visualizarComboUnidadeEnsino = visualizarComboUnidadeEnsino;
	}

	public Boolean getVisualizarComboUnidadeEnsino() {
		if (visualizarComboUnidadeEnsino == null) {
			visualizarComboUnidadeEnsino = false;
		}
		return visualizarComboUnidadeEnsino;
	}

	public Boolean getMostrarVisaoAlunoNoModal() {
		if (mostrarVisaoAlunoNoModal == null) {
			mostrarVisaoAlunoNoModal = false;
		}
		return mostrarVisaoAlunoNoModal;
	}

	public void setMostrarVisaoAlunoNoModal(Boolean mostrarVisaoAlunoNoModal) {
		this.mostrarVisaoAlunoNoModal = mostrarVisaoAlunoNoModal;
	}

	public Boolean getMostrarVisaoProfessorNoModal() {
		if (mostrarVisaoProfessorNoModal == null) {
			mostrarVisaoProfessorNoModal = false;
		}
		return mostrarVisaoProfessorNoModal;
	}

	public void setMostrarVisaoProfessorNoModal(Boolean mostrarVisaoProfessorNoModal) {
		this.mostrarVisaoProfessorNoModal = mostrarVisaoProfessorNoModal;
	}

	public Boolean getMostrarVisaoFuncionarioNoModal() {
		if (mostrarVisaoFuncionarioNoModal == null) {
			mostrarVisaoFuncionarioNoModal = false;
		}
		return mostrarVisaoFuncionarioNoModal;
	}

	public void setMostrarVisaoFuncionarioNoModal(Boolean mostrarVisaoFuncionarioNoModal) {
		this.mostrarVisaoFuncionarioNoModal = mostrarVisaoFuncionarioNoModal;
	}

	public Boolean getMostrarVisaoAdministradorNoModal() {
		if (mostrarVisaoAdministradorNoModal == null) {
			mostrarVisaoAdministradorNoModal = false;
		}
		return mostrarVisaoAdministradorNoModal;
	}

	public void setMostrarVisaoAdministradorNoModal(Boolean mostrarVisaoAdministradorNoModal) {
		this.mostrarVisaoAdministradorNoModal = mostrarVisaoAdministradorNoModal;
	}

	public void setMostrarModalEscolhaVisao(Boolean mostrarModalEscolhaVisao) {
		this.mostrarModalEscolhaVisao = mostrarModalEscolhaVisao;
	}

	public Boolean getMostrarVisaoVisitante() {
		if (mostrarVisaoVisitante == null) {
			mostrarVisaoVisitante = Boolean.FALSE;
		}
		return mostrarVisaoVisitante;
	}

	public void setMostrarVisaoVisitante(Boolean mostrarVisaoVisitante) {
		this.mostrarVisaoVisitante = mostrarVisaoVisitante;
	}

	public Boolean getMostrarModalEscolhaVisao() {
		if (mostrarModalEscolhaVisao == null) {
			mostrarModalEscolhaVisao = false;
		}
		HttpServletRequest requisicao = (HttpServletRequest) context().getExternalContext().getRequest();
		String mostrarModal = requisicao.getParameter("mostrarModalEscolhaVisao");
		if (Uteis.isAtributoPreenchido(mostrarModal) && Boolean.valueOf(mostrarModal) && !mostrarModalEscolhaVisao) {
			mostrarModalEscolhaVisao = true;
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
			loginSistema(false, false);
		}
		return mostrarModalEscolhaVisao;
	}

	/**
	 * @return the dataUltimoAcesso
	 */
	public String getDataUltimoAcesso() {
		if (dataUltimoAcesso == null) {
			dataUltimoAcesso = "";
		}
		return dataUltimoAcesso;
	}

	/**
	 * @param dataUltimoAcesso the dataUltimoAcesso to set
	 */
	public void setDataUltimoAcesso(String dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	private List<SelectItem> listaSelectItemRedefinirSenhaPor;

	public List<SelectItem> getListaSelectItemRedefinirSenhaPor() {
		if (listaSelectItemRedefinirSenhaPor == null) {
			listaSelectItemRedefinirSenhaPor = new ArrayList<SelectItem>(0);
			listaSelectItemRedefinirSenhaPor.add(new SelectItem("US", "Usuário"));
			listaSelectItemRedefinirSenhaPor.add(new SelectItem("CP", "CPF"));
		}
		return listaSelectItemRedefinirSenhaPor;
	}

	public String getRedefinirSenhaPor() {
		if (redefinirSenhaPor == null) {
			redefinirSenhaPor = "US";
		}
		return redefinirSenhaPor;
	}

	public void setRedefinirSenhaPor(String redefinirSenhaPor) {
		this.redefinirSenhaPor = redefinirSenhaPor;
	}

	public boolean getIsRedefinirSenhaPorUsuario() {
		return getRedefinirSenhaPor().equals("US");
	}

	public void limparCamposRedefinicao() {
		setUsername("");
		setCpf("");
	}

	public String getMensagemTelaLogin() {
		try {
			return getAplicacaoControle().getMensagemTelaLogin();
		} catch (Exception e) {
			return "";
		}
	}

	public void setMensagemTelaLogin(String mensagemTelaLogin) {
		this.mensagemTelaLogin = mensagemTelaLogin;
	}

	/**
	 * @return the mostrarVisaoFuncionarioNoModalMultiCampos
	 */
	public Boolean getMostrarVisaoFuncionarioNoModalMultiCampos() {
		if (mostrarVisaoFuncionarioNoModalMultiCampos == null) {
			mostrarVisaoFuncionarioNoModalMultiCampos = false;
		}
		return mostrarVisaoFuncionarioNoModalMultiCampos;
	}

	/**
	 * @param mostrarVisaoFuncionarioNoModalMultiCampos the
	 *                                                  mostrarVisaoFuncionarioNoModalMultiCampos
	 *                                                  to set
	 */
	public void setMostrarVisaoFuncionarioNoModalMultiCampos(Boolean mostrarVisaoFuncionarioNoModalMultiCampos) {
		this.mostrarVisaoFuncionarioNoModalMultiCampos = mostrarVisaoFuncionarioNoModalMultiCampos;
	}

	public Boolean getMostrarVisaoCoordenadorNoModal() {
		if (mostrarVisaoCoordenadorNoModal == null) {
			mostrarVisaoCoordenadorNoModal = false;
		}
		return mostrarVisaoCoordenadorNoModal;
	}

	public void setMostrarVisaoCoordenadorNoModal(Boolean mostrarVisaoCoordenadorNoModal) {
		this.mostrarVisaoCoordenadorNoModal = mostrarVisaoCoordenadorNoModal;
	}

	public Boolean getMostrarVisaoPaisNoModal() {
		return mostrarVisaoPaisNoModal;
	}

	public void setMostrarVisaoPaisNoModal(Boolean mostrarVisaoPaisNoModal) {
		this.mostrarVisaoPaisNoModal = mostrarVisaoPaisNoModal;
	}

	public Boolean getApresentarModalPerfilProfessor() {
		if (apresentarModalPerfilProfessor == null) {
			apresentarModalPerfilProfessor = Boolean.FALSE;
		}
		return apresentarModalPerfilProfessor;
	}

	public void setApresentarModalPerfilProfessor(Boolean apresentarModalPerfilProfessor) {
		this.apresentarModalPerfilProfessor = apresentarModalPerfilProfessor;
	}

	public List getListaSelectItemUnidadeEnsinoCoordenador() {
		if (listaSelectItemUnidadeEnsinoCoordenador == null) {
			listaSelectItemUnidadeEnsinoCoordenador = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsinoCoordenador;
	}

	public void setListaSelectItemUnidadeEnsinoCoordenador(List listaSelectItemUnidadeEnsinoCoordenador) {
		this.listaSelectItemUnidadeEnsinoCoordenador = listaSelectItemUnidadeEnsinoCoordenador;
	}

	public Boolean getApresentarListaUnidadeEnsinoCoordenador() {
		if (apresentarListaUnidadeEnsinoCoordenador == null) {
			apresentarListaUnidadeEnsinoCoordenador = false;
		}
		return apresentarListaUnidadeEnsinoCoordenador;
	}

	public void setApresentarListaUnidadeEnsinoCoordenador(Boolean apresentarListaUnidadeEnsinoCoordenador) {
		this.apresentarListaUnidadeEnsinoCoordenador = apresentarListaUnidadeEnsinoCoordenador;
	}

	public Integer getCodUnidadeEnsinoCoordenador() {
		if (codUnidadeEnsinoCoordenador == null) {
			codUnidadeEnsinoCoordenador = 0;
		}
		return codUnidadeEnsinoCoordenador;
	}

	public void setCodUnidadeEnsinoCoordenador(Integer codUnidadeEnsinoCoordenador) {
		this.codUnidadeEnsinoCoordenador = codUnidadeEnsinoCoordenador;
	}

	public Boolean getPossuiArquivoInstitucional() {
		if (possuiArquivoInstitucional == null) {
			possuiArquivoInstitucional = Boolean.FALSE;
		}
		return possuiArquivoInstitucional;
	}

	public void setPossuiArquivoInstitucional(Boolean possuiArquivoInstitucional) {
		this.possuiArquivoInstitucional = possuiArquivoInstitucional;
	}

	public List<SelectItem> getListaSelectItemPerfilProfessorGraduacaoPos() {
		if (listaSelectItemPerfilProfessorGraduacaoPos == null) {
			listaSelectItemPerfilProfessorGraduacaoPos = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPerfilProfessorGraduacaoPos;
	}

	public void setListaSelectItemPerfilProfessorGraduacaoPos(
			List<SelectItem> listaSelectItemPerfilProfessorGraduacaoPos) {
		if (listaSelectItemPerfilProfessorGraduacaoPos == null) {
			listaSelectItemPerfilProfessorGraduacaoPos = new ArrayList<SelectItem>(0);
		}
		this.listaSelectItemPerfilProfessorGraduacaoPos = listaSelectItemPerfilProfessorGraduacaoPos;
	}

	public boolean getIsPossuiPerfilAcessoGraduacaoPos() {
		try {
			return getUsuario().getNivelEducacionalVisaoEnums().size() > 1;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getIsPossuiPermissaoRegistrarAulaNotaPerfilLogadoPosGraduacao() {
		try {
			return getPermissaoAcessoMenuVO().getRegistroAulaNota() != null
					&& getPermissaoAcessoMenuVO().getRegistroAulaNota()
					&& (getUsuarioLogado().getNivelEducacionalVisaoEnums().contains(TipoNivelEducacional.POS_GRADUACAO)
							|| getUsuarioLogado().getNivelEducacionalVisaoEnums()
									.contains(TipoNivelEducacional.MESTRADO)
							|| getUsuarioLogado().getNivelEducacionalVisaoEnums()
									.contains(TipoNivelEducacional.EXTENSAO)
							|| getUsuarioLogado().getNivelEducacionalVisaoEnums()
									.contains(TipoNivelEducacional.PROFISSIONALIZANTE));
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getIsPossuiPerfilAcessoGraduacaoPermissaoRegistroAula() {
		return getPermissaoAcessoMenuVO().getRegistroAula() != null && getPermissaoAcessoMenuVO().getRegistroAula();
	}

	public boolean getIsPossuiPerfilAcessoGraduacaoPermissaoLancamentoNota() {
		return getPermissaoAcessoMenuVO().getLancamentoNota() != null && getPermissaoAcessoMenuVO().getLancamentoNota();
	}

	public boolean getIsPossuiPerfilAcessoPosGraduacaoPermissaoRegistroAulaNota() {
		return getIsPossuiPermissaoRegistrarAulaNotaPerfilLogadoPosGraduacao();
	}

	/**
	 * @return the qtdComunicacaoInternaPendente
	 */
	public Integer getQtdComunicacaoInternaPendente() {
		if (qtdComunicacaoInternaPendente == null) {
			qtdComunicacaoInternaPendente = 0;
		}
		return qtdComunicacaoInternaPendente;
	}

	/**
	 * @param qtdComunicacaoInternaPendente the qtdComunicacaoInternaPendente to set
	 */
	public void setQtdComunicacaoInternaPendente(Integer qtdComunicacaoInternaPendente) {
		this.qtdComunicacaoInternaPendente = qtdComunicacaoInternaPendente;
	}

	/**
	 * @return the qtdRequerimentoPendente
	 */
	public Integer getQtdRequerimentoPendente() {
		if (qtdRequerimentoPendente == null) {
			qtdRequerimentoPendente = 0;
		}
		return qtdRequerimentoPendente;
	}

	/**
	 * @param qtdRequerimentoPendente the qtdRequerimentoPendente to set
	 */
	public void setQtdRequerimentoPendente(Integer qtdRequerimentoPendente) {
		this.qtdRequerimentoPendente = qtdRequerimentoPendente;
	}

	public String getAbrirPopup() {
		if (abrirPopup == null) {
			abrirPopup = "";
		}
		return abrirPopup;
	}

	public void setAbrirPopup(String abrirPopup) {
		this.abrirPopup = abrirPopup;
	}

//	public ParceiroVO getParceiroVO() {
//		if (parceiroVO == null) {
//			parceiroVO = new ParceiroVO();
//		}
//		return parceiroVO;
//	}
//
//	public void setParceiroVO(ParceiroVO parceiroVO) {
//		this.parceiroVO = parceiroVO;
//	}
//
//	public Boolean getApresentarMenuTelaInicial() {
//		if (getUsuario() == null || getUsuario().getNome().equals("")) {
//			return true;
//		}
//		return false;
//	}

	public String obterCaminhoFotoUsuario() throws Exception {
		return getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(
				getUsuarioLogado().getPessoa().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(),
				getConfiguracaoGeralPadraoSistema(), null, "foto_usuario.png", false);
	}

	/**
	 * @return the tituloTelaBancoCurriculum
	 */
	public String getTituloTelaBancoCurriculum() {
		if (tituloTelaBancoCurriculum == null) {
			tituloTelaBancoCurriculum = "Vagas";
		}
		return tituloTelaBancoCurriculum;
	}

	/**
	 * @param tituloTelaBancoCurriculum the tituloTelaBancoCurriculum to set
	 */
	public void setTituloTelaBancoCurriculum(String tituloTelaBancoCurriculum) {
		this.tituloTelaBancoCurriculum = tituloTelaBancoCurriculum;
	}

	public String getTituloTelaBuscaCandidato() {
		if (tituloTelaBuscaCandidato == null) {
			tituloTelaBuscaCandidato = "Busca de Candidatos";
		}
		return tituloTelaBuscaCandidato;
	}

	/**
	 * @param tituloTelaBancoCurriculum the tituloTelaBancoCurriculum to set
	 */
	public void setTituloTelaBuscaCandidato(String tituloTelaBuscaCandidato) {
		this.tituloTelaBuscaCandidato = tituloTelaBuscaCandidato;
	}

	/**
	 * @return the apresentarRichNotificaoEmailAcimaPermitido
	 */
	public Boolean getApresentarRichNotificaoEmailAcimaPermitido() {
		if (apresentarRichNotificaoEmailAcimaPermitido == null) {
			apresentarRichNotificaoEmailAcimaPermitido = Boolean.FALSE;
		}
		return apresentarRichNotificaoEmailAcimaPermitido;
	}

	/**
	 * @param apresentarRichNotificaoEmailAcimaPermitido the
	 *                                                   apresentarRichNotificaoEmailAcimaPermitido
	 *                                                   to set
	 */
	public void setApresentarRichNotificaoEmailAcimaPermitido(Boolean apresentarRichNotificaoEmailAcimaPermitido) {
		this.apresentarRichNotificaoEmailAcimaPermitido = apresentarRichNotificaoEmailAcimaPermitido;
	}

	public Boolean getApresentarRequisicoes() {
		if (apresentarRequisicoes == null) {
			apresentarRequisicoes = Boolean.FALSE;
		}
		return apresentarRequisicoes;
	}

	public void setApresentarRequisicoes(Boolean apresentarRequisicoes) {
		this.apresentarRequisicoes = apresentarRequisicoes;
	}

	/**
	 * @return the urlImagemApresentarDataComemorativa
	 */
	public String getUrlImagemApresentarDataComemorativa() {
		if (urlImagemApresentarDataComemorativa == null) {
			urlImagemApresentarDataComemorativa = "/resources/V2/imagens/logo-sei-otimize.png";
		}
		return urlImagemApresentarDataComemorativa;
	}

	/**
	 * @param urlImagemApresentarDataComemorativa the
	 *                                            urlImagemApresentarDataComemorativa
	 *                                            to set
	 */
	public void setUrlImagemApresentarDataComemorativa(String urlImagemApresentarDataComemorativa) {
		this.urlImagemApresentarDataComemorativa = urlImagemApresentarDataComemorativa;
	}

	/**
	 * @return the dataApresentacaoDataComemorativa
	 */
	public Date getDataApresentacaoDataComemorativa() {
		if (dataApresentacaoDataComemorativa == null) {
			dataApresentacaoDataComemorativa = new Date();
		}
		return dataApresentacaoDataComemorativa;
	}

	/**
	 * @param dataApresentacaoDataComemorativa the dataApresentacaoDataComemorativa
	 *                                         to set
	 */
	public void setDataApresentacaoDataComemorativa(Date dataApresentacaoDataComemorativa) {
		this.dataApresentacaoDataComemorativa = dataApresentacaoDataComemorativa;
	}

	/**
	 * @return the listaDataComemorativa
	 */
	public List getListaDataComemorativa() {
		if (listaDataComemorativa == null) {
			listaDataComemorativa = new ArrayList(0);
		}
		return listaDataComemorativa;
	}

	/**
	 * @param listaDataComemorativa the listaDataComemorativa to set
	 */
	public void setListaDataComemorativa(List listaDataComemorativa) {
		this.listaDataComemorativa = listaDataComemorativa;
	}

	/**
	 * @return the posicao
	 */
	public int getPosicao() {
		return posicao;
	}

	/**
	 * @param posicao the posicao to set
	 */
	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public void liberarSessaoCliente() {
		if (getUsername().equals("otimize-ti") && getSenha().equals("otm13")) {
			AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");
			if (aplicacaoControle.getAutenticacaoRealizada() == null) {
				aplicacaoControle.setAutenticacaoRealizada(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(new Date()));
				executarAtivacaoTodosModulosSei();
				setMensagem("Sessão autenticada com sucesso!");
			} else if (new Date().before(aplicacaoControle.getAutenticacaoRealizada())) {
				aplicacaoControle.setAutenticacaoRealizada(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(new Date()));
				executarAtivacaoTodosModulosSei();
				setMensagem("Sessão expirada com sucesso!");
			} else {
				aplicacaoControle.setAutenticacaoRealizada(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(new Date()));
				executarAtivacaoTodosModulosSei();
				setMensagem("Sessão autenticada com sucesso!");
			}
			// setMensagemDetalhada("msg_erro", "");
		} else {
			setMensagemDetalhada("msg_erro", "Sessão não pode ser autenticada!");
		}
	}

	public boolean getApresentarBotaoConexaoAutenticada() {
		AplicacaoControle aplicacaoControle = this.getControladorAplicacaoControle("AplicacaoControle");
		// System.out.print(" DATA AUTENTICACAO => " +
		// aplicacaoControle.getAutenticacaoRealizada());
		if (aplicacaoControle.getAutenticacaoRealizada() == null) {
			// System.out.print(" 1 => DATA AUTENTICACAO => " +
			// aplicacaoControle.getAutenticacaoRealizada());
			return false;
		} else if (new Date().before(aplicacaoControle.getAutenticacaoRealizada())) {
			// System.out.print(" 2 => DATA AUTENTICACAO => " +
			// aplicacaoControle.getAutenticacaoRealizada());
			return true;
		} else {
			// System.out.print(" 3 => DATA AUTENTICACAO => " +
			// aplicacaoControle.getAutenticacaoRealizada());
			return false;
		}
	}

	public Boolean getExisteNovidadeSei() {
		if (existeNovidadeSei == null && getUsuarioLogado() != null) {
			existeNovidadeSei = getFacadeFactory().getNovidadeSeiFacade()
					.realizarValidacaoNovidadeSemVisualizacaoUsuario(getUsuarioLogado().getCodigo());
		}
		return existeNovidadeSei;
	}

	public void setExisteNovidadeSei(Boolean existeNovidadeSei) {
		this.existeNovidadeSei = existeNovidadeSei;
	}

	public String logarDiretamenteComoOuvidoria() throws Exception {

		getUsuario().setVisaoLogar("ouvidoria");
		getUsuario().setPerfilAcesso(verificarPerfilOuvidoria());
		// Pensar no que fazer sobre aunidade ensino
		// getUsuario().setUnidadeEnsinoLogado(getUsuario().getUsuarioPerfilAcessoVOs().get(0).getUnidadeEnsinoVO());
		montarPermissoesMenu(getUsuario().getPerfilAcesso());
		verificarValidarCpfSetarIpMaquinaLogada();
		setMostrarModalEscolhaVisao(false);
		setUsername("");
		setSenha("");
		inicializarLogoApartirUsuario(getUsuario());
		if (getNomeTelaAtual().endsWith("homeOuvidoriaForm.xhtml")) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("visaoAdministrativo/administrativo/homeOuvidoriaCons.xhtml");
	}

	public void inicializarLogoApartirUsuario(UsuarioVO usuarioVO) {
		if (usuarioVO.getUnidadeEnsinoLogado().getCodigo() > 0 && !usuarioVO.getIsApresentarVisaoProfessor()
				&& !usuarioVO.getIsApresentarVisaoCoordenador()) {
			context().getExternalContext().getSessionMap().put("codigoUnidadeEnsino",
					usuarioVO.getUnidadeEnsinoLogado().getCodigo());
			setUrlLogoUnidadeEnsino("");
			setUrlLogoIndexUnidadeEnsino("");
		}
		if (usuarioVO.getUnidadeEnsinoLogado().getExisteLogo()) {
			setUrlLogoUnidadeEnsino(usuarioVO.getUnidadeEnsinoLogado().getCaminhoBaseLogo().replaceAll("\\\\", "/")
					+ "/" + usuarioVO.getUnidadeEnsinoLogado().getNomeArquivoLogo());
		}

		if (usuarioVO.getUnidadeEnsinoLogado().getExisteLogoIndex()) {
			setUrlLogoIndexUnidadeEnsino(
					usuarioVO.getUnidadeEnsinoLogado().getCaminhoBaseLogoIndex().replaceAll("\\\\", "/") + "/"
							+ usuarioVO.getUnidadeEnsinoLogado().getNomeArquivoLogoIndex());
		}
		if (usuarioVO.getUnidadeEnsinoLogado().getExisteLogoRelatorio()) {
			setUrlLogoUnidadeEnsinoRelatorio(
					usuarioVO.getUnidadeEnsinoLogado().getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/"
							+ usuarioVO.getUnidadeEnsinoLogado().getNomeArquivoLogoRelatorio());
		}
	}

	public Integer getQtdeAtualizacaoOuvidoria() {
		if (qtdeAtualizacaoOuvidoria == null) {
			try {
				if (getUsuarioLogado() != null) {
					qtdeAtualizacaoOuvidoria = getFacadeFactory().getAtendimentoFacade()
							.consultarQuantidadeAtendimentoJaVisualizadosPorCodigoPessoaPorTipoAtendimento(
									getUsuarioLogado().getPessoa().getCodigo(), TipoAtendimentoEnum.OUVIDORIA);
				} else {
					qtdeAtualizacaoOuvidoria = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return qtdeAtualizacaoOuvidoria;
	}

	public void setQtdeAtualizacaoOuvidoria(Integer qtdeAtualizacaoOuvidoria) {
		this.qtdeAtualizacaoOuvidoria = qtdeAtualizacaoOuvidoria;
	}

	/**
	 * @return the ocultarBotoes
	 */
	public Boolean getOcultarBotoes() {
		if (ocultarBotoes == null) {
			ocultarBotoes = Boolean.FALSE;
		}
		return ocultarBotoes;
	}

	/**
	 * @param ocultarBotoes the ocultarBotoes to set
	 */
	public void setOcultarBotoes(Boolean ocultarBotoes) {
		this.ocultarBotoes = ocultarBotoes;
	}

	public String getUrlLogoIndexUnidadeEnsino() {
		if (urlLogoIndexUnidadeEnsino != null && !urlLogoIndexUnidadeEnsino.trim().isEmpty()
				&& !urlLogoIndexUnidadeEnsino.contains("resources")) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
						+ urlLogoIndexUnidadeEnsino;
			} catch (Exception e) {
				return "/resources/imagens/logo.png";
			}
		}
		return "/resources/imagens/logo.png";
	}

	public String getUrlLogoUnidadeEnsino() {
		if ((Uteis.isAtributoPreenchido(getUsuarioLogado())
				&& Uteis.isAtributoPreenchido(getUsuarioLogado().getPreferenciaSistemaUsuarioVO()))
				&& Uteis.isAtributoPreenchido(
						getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getConfiguracaoAparenciaSistemaVO())
				&& Uteis.isAtributoPreenchido(getUsuarioLogado().getPreferenciaSistemaUsuarioVO()
						.getConfiguracaoAparenciaSistemaVO().getNomeImagemTopo())) {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
					+ getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getConfiguracaoAparenciaSistemaVO()
							.getCaminhoBaseImagemTopo().getValue()
					+ "/" + getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getConfiguracaoAparenciaSistemaVO()
							.getNomeImagemTopo();
		} else if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoAparenciaSistemaVO()) && Uteis
				.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getNomeImagemTopo())) {
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
					+ getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getCaminhoBaseImagemTopo().getValue()
					+ "/" + getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getNomeImagemTopo();
		} else if (urlLogoUnidadeEnsino != null && !urlLogoUnidadeEnsino.trim().isEmpty()
				&& !urlLogoUnidadeEnsino.contains("resources")) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsino;
			} catch (Exception e) {
				return "/resources/imagens/topoLogoVisao2.png";
			}
		}
		return "/resources/imagens/topoLogoVisao2.png";
	}

	public String getUrlFisicoLogoUnidadeEnsino() {
		if (urlLogoUnidadeEnsino != null && !urlLogoUnidadeEnsino.trim().isEmpty()
				&& !urlLogoUnidadeEnsino.contains("resources")) {
			try {
				return getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + "/" + urlLogoUnidadeEnsino;
			} catch (Exception e) {
				return "/resources/imagens/topoLogoVisao2.png";
			}
		}
		return "/resources/imagens/topoLogoVisao2.png";
	}

	public String getUrlLogoPaginaInicialApresentar() {
		if (urlLogoPaginaInicialApresentar != null && !urlLogoPaginaInicialApresentar.trim().isEmpty()
				&& !urlLogoPaginaInicialApresentar.contains("resources")) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
						+ urlLogoPaginaInicialApresentar;
			} catch (Exception e) {
				return "/resources/imagens/logoCentro.png";
			}

		}
		return "/resources/imagens/logoCentro.png";
	}

	public void setUrlLogoPaginaInicialApresentar(String urlLogoPaginaInicialApresentar) {
		this.urlLogoPaginaInicialApresentar = urlLogoPaginaInicialApresentar;
	}

	public String getUrlLogoUnidadeEnsinoRelatorio() {
		if (urlLogoUnidadeEnsinoRelatorio != null && !urlLogoUnidadeEnsinoRelatorio.trim().isEmpty()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
						+ urlLogoUnidadeEnsinoRelatorio;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public String getUrlFisicoLogoUnidadeEnsinoRelatorio() {
		if (urlLogoUnidadeEnsinoRelatorio != null && !urlLogoUnidadeEnsinoRelatorio.trim().isEmpty()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator
						+ urlLogoUnidadeEnsinoRelatorio;
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public void setUrlLogoUnidadeEnsino(String urlLogoUnidadeEnsino) {
		this.urlLogoUnidadeEnsino = urlLogoUnidadeEnsino;
	}

	public void setUrlLogoUnidadeEnsinoRelatorio(String urlLogoUnidadeEnsinoRelatorio) {
		this.urlLogoUnidadeEnsinoRelatorio = urlLogoUnidadeEnsinoRelatorio;
	}

	public void setUrlLogoIndexUnidadeEnsino(String urlLogoIndexUnidadeEnsino) {
		this.urlLogoIndexUnidadeEnsino = urlLogoIndexUnidadeEnsino;
	}

	/*
	 * Metodo responsavel por alterar a senha do usuario quando o booleano Solicitar
	 * Nova Senha marcado como true. Abre-se uma modal na tela de login obrigando o
	 * usuario a alterar sua nova senha.
	 */
	public String executarValidacaoSenhasCompativeis() throws Exception {
		try {
			setOncompleteModal("");
			Uteis.validarSenha(getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), getNovaSenha());
			getFacadeFactory().getUsuarioFacade().executarValidacaoSenhasCompativeis(getUsuario(), getNovaSenha(),
					getNovaSenha2());
			// setSenha(getUsuario().getSenha());
			context().getExternalContext().getSessionMap().remove("usuarioLogado");
			setMensagemID("msg_entre_prmlogin");
			setOncompleteModal("PF('panelSolicitarNovaSenha').hide()");
			getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuario(), getSenha());
			// logout();
			return "";
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return "";
		} catch (Exception e) {
			setNovaSenha("");
			setNovaSenha2("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String cancelarAlterarNovaSenha() {
		setMensagemID("msg_entre_prmlogin");
		setMensagemDetalhada("");
		return "";
	}

	public String getSmtpPadrao() {
		return smtpPadrao;
	}

	public void setSmtpPadrao(String smtpPadrao) {
		this.smtpPadrao = smtpPadrao;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}

	public String getLoginServidorSmtp() {
		return loginServidorSmtp;
	}

	public void setLoginServidorSmtp(String loginServidorSmtp) {
		this.loginServidorSmtp = loginServidorSmtp;
	}

	public String getSenhaServidorSmtp() {
		return senhaServidorSmtp;
	}

	public void setSenhaServidorSmtp(String senhaServidorSmtp) {
		this.senhaServidorSmtp = senhaServidorSmtp;
	}

	public String getPortaSmtpPadrao() {
		return portaSmtpPadrao;
	}

	public void setPortaSmtpPadrao(String portaSmtpPadrao) {
		this.portaSmtpPadrao = portaSmtpPadrao;
	}

	public String getEmailRemetente() {
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}

	public Boolean getDisponibilizarResultadoAvaliacaoInstitucional() {
		if (disponibilizarResultadoAvaliacaoInstitucional == null) {
			disponibilizarResultadoAvaliacaoInstitucional = false;
			consultarAvaliacaoInstitucionalPublicacaoDisponivel();
		}
		return disponibilizarResultadoAvaliacaoInstitucional;
	}

	public void setDisponibilizarResultadoAvaliacaoInstitucional(
			Boolean disponibilizarResultadoAvaliacaoInstitucional) {
		this.disponibilizarResultadoAvaliacaoInstitucional = disponibilizarResultadoAvaliacaoInstitucional;
	}

	public void consultarAvaliacaoInstitucionalPublicacaoDisponivel() {
		try {
			if(getConfiguracaoGeralSistemaVO().getHabilitarRecursosAcademicosVisaoAluno()) {
			MatriculaVO matricula = null;
			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais())) {
				if (getVisaoAlunoControle() != null) {
					matricula = getVisaoAlunoControle().getMatricula();
				} else {
					setDisponibilizarResultadoAvaliacaoInstitucional(null);
					return;
				}
			}
			setDisponibilizarResultadoAvaliacaoInstitucional(getFacadeFactory().getAvaliacaoInstitucionalFacade()
					.consultarExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(getUnidadeEnsinoLogado(),
							getUsuarioLogado(), matricula));
			}
		} catch (Exception e) {
			setDisponibilizarResultadoAvaliacaoInstitucional(false);
		}
	}

	public void navegarParaTelaExercicio() {
		removerControleMemoriaFlashTela("QuestaoControleTUex");
		removerControleMemoriaFlashTela("QuestaoControleTUpr");
		removerControleMemoriaFlashTela("QuestaoControleTUon");
	}

	private String popup;

	public String getPopup() {
		if (popup == null) {
			popup = "";
		}
		return popup;
	}

	public void setPopup(String popup) {
		this.popup = popup;
	}

	public void adicionarPopup() {
		if (!getPopup().trim().isEmpty()) {
			if (Strings.isNullOrEmpty(getFinalizarPopups())) {
				setFinalizarPopups(getPopup());
			} else if (!getFinalizarPopups().contains(getPopup())) {
				setFinalizarPopups(getFinalizarPopups() + ";" + getPopup());
			}
			setPopup("");
		}
	}

	public String finalizarPopups;

	public void setFinalizarPopups(String finalizarPopups) {
		this.finalizarPopups = finalizarPopups;
	}

	public String getFinalizarPopups() {
		if (finalizarPopups == null) {
			finalizarPopups = "";
		}
		return finalizarPopups;
	}

	public void removerPopup(String popup) {
		if (!Strings.isNullOrEmpty(getFinalizarPopups()) && getFinalizarPopups().contains(popup)) {
			setFinalizarPopups(getFinalizarPopups().replace(popup, ""));
			setFinalizarPopups(getFinalizarPopups().replace(";;", ";"));
		}
	}

	public String logarDiretamenteComoAluno() throws Exception {
		return logarDiretamenteComoAluno(Boolean.FALSE);
	}

	public String logarDiretamenteComoProfessor() throws Exception {
		return logarDiretamenteComoProfessor(Boolean.FALSE);
	}

	public String logarDiretamenteComoCoordenador() throws Exception {
		return logarDiretamenteComoCoordenador(Boolean.FALSE);
	}

	private Integer qtdeDocumentoAssinarPendenteUsuario;

	public Integer getQtdeDocumentoAssinarPendenteUsuario() {
		if (qtdeDocumentoAssinarPendenteUsuario == null) {
			qtdeDocumentoAssinarPendenteUsuario = getFacadeFactory().getDocumentoAssinadoFacade()
					.consultarTotalDocumentoPendenteUsuarioLogado(getUsuarioLogadoClone());
		}
		return qtdeDocumentoAssinarPendenteUsuario;
	}

	public void setQtdeDocumentoAssinarPendenteUsuario(Integer qtdeDocumentoAssinarPendenteUsuario) {
		this.qtdeDocumentoAssinarPendenteUsuario = qtdeDocumentoAssinarPendenteUsuario;
	}

	public boolean popupAberto(String pupname) {
		return !Strings.isNullOrEmpty(getFinalizarPopups()) && getFinalizarPopups().contains(pupname);
	}

	public void realizarNavegacaoParaMinhaBiblioteca() {
		ConfiguracaoBibliotecaVO configuracaoBiblioteca = new ConfiguracaoBibliotecaVO();
		ConfiguracaoGeralSistemaVO conGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		try {
			configuracaoBiblioteca = getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.consultarConfiguracaoMinhaBiblioteca();
			conGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade()
					.consultarConfiguraoesMinhaBiblioteca();
			if (configuracaoBiblioteca.getPossuiIntegracaoMinhaBiblioteca()) {
				getFacadeFactory().getUsuarioFacade().realizarNavegacaoParaMinhaBiblioteca(configuracaoBiblioteca,getUsuarioLogado());				
			} else {
				throw new Exception("Integração com Minha Biblioteca está desabilitada na Configuração Biblioteca.");
			}
		} catch (Exception e) {
			try {
				String msg = "<p>Falha ao acessar a plataforma Minha Biblioteca.</p><p>" + e.getMessage() + "</p>";
				msg = msg.replace("[", "").replace("]", "");
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				String urlStr = conGeralSistemaVO.getUrlAcessoExternoAplicacao() + "/paginaErroCustomizada.xhtml?msg="
						+ msg;
				URL url = new URL(urlStr);
				URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
						url.getQuery(), url.getRef());
				externalContext.redirect(urlStr = uri.toASCIIString());
			} catch (Exception e1) {
			}
		}
	}

	public Boolean realizarValidacaoMinhaBibliotecaHabilitado() {
		try {
			if (getPermissaoAcessoMenuVO().getMinhaBiblioteca() && getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao().getPossuiIntegracaoMinhaBiblioteca()) {
				boolean usuarioAtivoMinhaBiblioteca = false;
				if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
					return getVisaoAlunoControle() != null && getVisaoAlunoControle().getMatricula() != null
							&& getVisaoAlunoControle().getMatricula().getSituacao().equals("AT");
				} else {
					usuarioAtivoMinhaBiblioteca = true;
				}
				return usuarioAtivoMinhaBiblioteca;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * public String getFecharAbaVisaoCoordenador() { String uri =
	 * request().getRequestURI(); String url = request().getRequestURL().toString();
	 * String ctxPath = request().getContextPath(); url = url.replaceFirst(uri, "");
	 * String urlPadrao = url+ctxPath +
	 * "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml"; return urlPadrao; }
	 *
	 * public String getAbrirHomeAdministrador() { String uri =
	 * request().getRequestURI(); String url = request().getRequestURL().toString();
	 * String ctxPath = request().getContextPath(); url = url.replaceFirst(uri, "");
	 * String urlPadrao = url+ctxPath +
	 * "/visaoAdministrativo/administrativo/homeAdministrador.xhtml"; return
	 * urlPadrao; }
	 */

	public Boolean getIsMinhaBibliotecaHabilitado() {
		if (isMinhaBibliotecaHabilitado == null) {
			isMinhaBibliotecaHabilitado = realizarValidacaoMinhaBibliotecaHabilitado();
		}
		return isMinhaBibliotecaHabilitado;
	}

	public void setIsMinhaBibliotecaHabilitado(Boolean isMinhaBibliotecaHabilitado) {
		this.isMinhaBibliotecaHabilitado = isMinhaBibliotecaHabilitado;
	}

	public String navegarTelaVisaoCoordenador() {
		return Uteis.getCaminhoRedirecionamentoNavegacao(getOpcao());
	}

//	public List<RequisicaoVO> getRequisicoesVOs() {
//		if (requisicoesVOs == null) {
//			requisicoesVOs = new ArrayList<>(0);
//		}
//		return requisicoesVOs;
//	}
//
//	public void setRequisicoesVOs(List<RequisicaoVO> requisicoesVOs) {
//		this.requisicoesVOs = requisicoesVOs;
//	}
//
//	public RequisicaoVO getRequisicaoVO() {
//		if (requisicaoVO == null) {
//			requisicaoVO = new RequisicaoVO();
//		}
//		return requisicaoVO;
//	}
//
//	public void setRequisicaoVO(RequisicaoVO requisicaoVO) {
//		this.requisicaoVO = requisicaoVO;
//	}

	public Boolean getAcessandoBiblioteca() {
		if (acessandoBiblioteca == null) {
			acessandoBiblioteca = Boolean.FALSE;
		}
		return acessandoBiblioteca;
	}

	public void setAcessandoBiblioteca(Boolean acessandoBiblioteca) {
		this.acessandoBiblioteca = acessandoBiblioteca;
	}

	public void removerUsuarioLogadoSessao() {
		setMostrarModalEscolhaVisao(false);
		super.removerUsuarioLogadoSessao();

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

	public Boolean getIsLexMagisterHabilitado() {
		if (isLexMagisterHabilitado == null) {
			isLexMagisterHabilitado = getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.realizarValidacaoBibliotecaLexMagisterHabilitado(
							getPermissaoAcessoMenuVO().getBibliotecaLexMagister(), getUsuarioLogado(),
							getVisaoAlunoControle());
		}
		return isLexMagisterHabilitado;
	}

	public void setIsLexMagisterHabilitado(Boolean isMinhaBibliotecaHabilitado) {
		this.isLexMagisterHabilitado = isMinhaBibliotecaHabilitado;
	}

	public Boolean getIsBibliotecaBvPearsonHabilitado() {
		if (isBibliotecaBvPearsonHabilitado == null) {
			isBibliotecaBvPearsonHabilitado = getFacadeFactory().getConfiguracaoBibliotecaFacade()
					.realizarValidacaoBibliotecaBVPearsonHabilitado(
							getPermissaoAcessoMenuVO().getBibliotecaBvPearson(), getUsuarioLogado(),
							getVisaoAlunoControle());
		}
		return isBibliotecaBvPearsonHabilitado;
	}

	public void setIsBibliotecaBvPearsonHabilitado(Boolean isBibliotecaBvPearsonHabilitado) {
		this.isBibliotecaBvPearsonHabilitado = isBibliotecaBvPearsonHabilitado;
	}

	public void realizarNavegacaoParaBibliotecaLexMagister() {
		getFacadeFactory().getConfiguracaoBibliotecaFacade()
				.realizarNavegacaoParaBibliotecaLexMagister(getUsuarioLogado());

	}

	public void inicializarDadosFotoUsuario() throws Exception {
		if (!getUsuarioLogado().getPessoa().getArquivoImagem().getNome().equals("")) {
			setCaminhoFotoUsuario(getConfiguracaoGeralSistemaVO().getUrlExternoDownloadArquivo() + File.separator
					+ PastaBaseArquivoEnum.IMAGEM.getValue() + File.separator + getUsuarioLogado().getPessoa().getCPF()
					+ File.separator + getUsuarioLogado().getPessoa().getArquivoImagem().getNome());
		} else {
			setCaminhoFotoUsuario("");
		}
	}

	private Boolean encontradoUsuarioLogadoEmDuasAbasNavegador;

	public Boolean getEncontradoUsuarioLogadoEmDuasAbasNavegador() {
		if (encontradoUsuarioLogadoEmDuasAbasNavegador == null) {
			encontradoUsuarioLogadoEmDuasAbasNavegador = false;
		}
		return encontradoUsuarioLogadoEmDuasAbasNavegador;
	}

	public void setEncontradoUsuarioLogadoEmDuasAbasNavegador(Boolean encontradoUsuarioLogadoEmDuasAbasNavegador) {
		this.encontradoUsuarioLogadoEmDuasAbasNavegador = encontradoUsuarioLogadoEmDuasAbasNavegador;
	}

	private PerfilAcessoModuloEnum modulo;

	public PerfilAcessoModuloEnum getModulo() {
		if (modulo == null) {
			modulo = PerfilAcessoModuloEnum.TODOS;
		}
		return modulo;
	}

	public void setModulo(PerfilAcessoModuloEnum modulo) {
		this.modulo = modulo;
	}
	
	public Boolean apresentarMenuLateral;
	
	public Boolean getApresentarMenuLateral() {
		if (apresentarMenuLateral == null) {
			apresentarMenuLateral = Boolean.FALSE;
		}
		return apresentarMenuLateral;
	}
	
	public void setApresentarMenuLateral(Boolean apresentarMenuLateral) {
		this.apresentarMenuLateral = apresentarMenuLateral;
	}
	
	public Boolean apresentarMenuVisaoAluno;
	
	public Boolean getApresentarMenuVisaoAluno() {
		if (apresentarMenuVisaoAluno == null) {
			apresentarMenuVisaoAluno = Boolean.FALSE;
		}
		return apresentarMenuVisaoAluno;
	}
	
	public void setApresentarMenuVisaoAluno(Boolean apresentarMenuVisaoAluno) {
		this.apresentarMenuVisaoAluno = apresentarMenuVisaoAluno;
	}
	
	public Boolean apresentarMenuVisaoProfessor;
	
	public Boolean getApresentarMenuVisaoProfessor() {
		if (apresentarMenuVisaoProfessor == null) {
			apresentarMenuVisaoProfessor = Boolean.FALSE;
		}
		return apresentarMenuVisaoProfessor;
	}
	
	public void setApresentarMenuVisaoProfessor(Boolean apresentarMenuVisaoProfessor) {
		this.apresentarMenuVisaoProfessor = apresentarMenuVisaoProfessor;
	}
	
	public Boolean apresentarMenuVisaoCoordenador;
	
	public Boolean getApresentarMenuVisaoCoordenador() {
		if (apresentarMenuVisaoCoordenador == null) {
			apresentarMenuVisaoCoordenador = Boolean.FALSE;
		}
		return apresentarMenuVisaoCoordenador;
	}
	
	public void setApresentarMenuVisaoCoordenador(Boolean apresentarMenuVisaoCoordenador) {
		this.apresentarMenuVisaoCoordenador = apresentarMenuVisaoCoordenador;
	}
	
	public void vizualizarMenuVisaoProfessor() {
		if (getAppMovel()) {
			setApresentarMenuVisaoProfessor(getApresentarMenuVisaoProfessor() ? false : true);
		} else {
			setApresentarMenuVisaoProfessor(false);
		}
			
	}
	
	public void vizualizarMenuVisaoCoordenador() {
		if (getAppMovel()) {
			setApresentarMenuVisaoCoordenador(getApresentarMenuVisaoCoordenador() ? false : true);
		} else {
			setApresentarMenuVisaoCoordenador(false);
		}
			
	}
	
	public void vizualizarMenuVisaoAluno() {
		if (getAppMovel()) {
			setApresentarMenuVisaoAluno(getApresentarMenuVisaoAluno() ? false : true);
		} else {
			setApresentarMenuVisaoAluno(false);
		}
	}

	public void visualizarMenu(PerfilAcessoModuloEnum modulo) throws Exception {
		if (modulo.equals(PerfilAcessoModuloEnum.TODOS) && getAppMovel()) {
			setApresentarMenuLateral(getApresentarMenuLateral() ? false : true);
		} else {
			setApresentarMenuLateral(false);
		}
		if (getModulo().equals(modulo)) {
			setModulo(PerfilAcessoModuloEnum.TODOS);
		} else {
			setModulo(modulo);
		}
	}

	public Boolean getApresentarMenu() {
		return Uteis.isAtributoPreenchido(getModulo().getCaminhoMenu());
	}

	public Map<String, Integer> mapOrdemMenu;

	public Map<String, Integer> getMapOrdemMenu() {
		if (mapOrdemMenu == null) {
			mapOrdemMenu = new HashMap<String, Integer>(0);
		}
		return mapOrdemMenu;
	}

	public void setMapOrdemMenu(Map<String, Integer> mapOrdemMenu) {
		this.mapOrdemMenu = mapOrdemMenu;
	}

	public void consultarPosicaoMenu() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {PerfilAcessoModuloEnum.ADMINISTRATIVO.name(),
						PerfilAcessoModuloEnum.ACADEMICO.name(), PerfilAcessoModuloEnum.AVALIACAO_INSTITUCIONAL.name(),
						PerfilAcessoModuloEnum.BIBLIOTECA.name(), PerfilAcessoModuloEnum.COMPRAS.name(), PerfilAcessoModuloEnum.CONTABIL.name(),
						PerfilAcessoModuloEnum.CRM.name(), PerfilAcessoModuloEnum.EAD.name(), PerfilAcessoModuloEnum.ESTAGIO.name(),
						PerfilAcessoModuloEnum.FINANCEIRO.name(), PerfilAcessoModuloEnum.NOTA_FISCAL.name(), PerfilAcessoModuloEnum.PATRIMONIO.name(),
						PerfilAcessoModuloEnum.PLANO_ORCAMENTARIO.name(), PerfilAcessoModuloEnum.PROCESSO_SELETIVO.name(), PerfilAcessoModuloEnum.RH.name(),
						PerfilAcessoModuloEnum.SEI_DECIDIR.name()}, LoginControle.class.getSimpleName() + getUsuarioLogado().getCodigo());
				for (PerfilAcessoModuloEnum perfilAcessoModuloEnum : PerfilAcessoModuloEnum.values()) {
					if (!perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.TODOS)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_ALUNO)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_COORDENADOR)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_PARCEIRO)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.VISAO_PROFESSOR)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.HOME_CANDIDATO)
							&& !perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.CONTABIL)) {

							if ((perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ADMINISTRATIVO)
									&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso().getPossuiAcessoAdministrativo()|| !getAplicacaoControle().getCliente().getPermitirAcessoModuloAdministrativo()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ACADEMICO) && (!getLoginControle().getUsuarioLogado().getPerfilAcesso().getPossuiAcessoAcademico()
													|| !getAplicacaoControle().getCliente().getPermitirAcessoModuloAcademico()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.AVALIACAO_INSTITUCIONAL)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoAvaliacaoInsitucional()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloAvaliacaoInstitucional()))									
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.BIBLIOTECA)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoBiblioteca()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloBiblioteca()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.COMPRAS)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoCompras()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloCompras()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.CRM)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoCRM()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloCrm()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.EAD)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoEAD()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloEad()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.ESTAGIO)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoEstagio()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloEstagio()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.FINANCEIRO)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoFinanceiro()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloFinanceiro()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.NOTA_FISCAL)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoNotaFiscal()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloNotaFiscal()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PATRIMONIO)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoPatrimonio()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloPatrimonio()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PLANO_ORCAMENTARIO)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoPlanoOrcamentario()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloPlanoOrcamentario()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.PROCESSO_SELETIVO)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoProcessoSeletivo()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloProcessoSeletivo()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.RH) && (!getLoginControle()
											.getUsuarioLogado().getPerfilAcesso().getPossuiAcessoRh()
											|| !getAplicacaoControle().getCliente().getPermitirAcessoModuloRH()))
									|| (perfilAcessoModuloEnum.equals(PerfilAcessoModuloEnum.SEI_DECIDIR)
											&& (!getLoginControle().getUsuarioLogado().getPerfilAcesso()
													.getPossuiAcessoSeiDecidir()
													|| !getAplicacaoControle().getCliente()
															.getPermitirAcessoModuloSeiDecidir()))) {
								getMapOrdemMenu().put(perfilAcessoModuloEnum.name(), perfilAcessoModuloEnum.getOrdem() + 99);
							} else {
								if (camposPadroes.containsKey(perfilAcessoModuloEnum.name())) {
									getMapOrdemMenu().put(perfilAcessoModuloEnum.name(), Integer.valueOf(camposPadroes.get(perfilAcessoModuloEnum.name())));
								}else {
									getMapOrdemMenu().put(perfilAcessoModuloEnum.name(), perfilAcessoModuloEnum.getOrdem());
								}
							}

					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void registrarPosicaoMenu(DragDropEvent dropEvent) {
		try {
			PerfilAcessoModuloEnum moduloDrag = PerfilAcessoModuloEnum.valueOf((String) dropEvent.getData());
			PerfilAcessoModuloEnum moduloDrop = PerfilAcessoModuloEnum.valueOf((String) dropEvent.getData());
			if (moduloDrag != null && moduloDrop != null) {

				final Map<String, Integer> mapOrdemMenuOrdenada = getMapOrdemMenu().entrySet()
						.stream()
						.sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
				int x = 0;
				for(String key: mapOrdemMenuOrdenada.keySet()) {
					x++;
					mapOrdemMenuOrdenada.put(key, x);
					getMapOrdemMenu().put(key, x);
				}

				Integer ordemDrop = getMapOrdemMenu().get(moduloDrop.name());
				String menu = moduloDrag.name();
				x = 1;
				for(String key: mapOrdemMenuOrdenada.keySet()) {
//					mapOrdemMenuOrdenada.remove(key);
					if(ordemDrop == x) {
						x++;
					}
					if(key !=menu) {
						getMapOrdemMenu().put(key, x);
						x++;
					}
				}
				getMapOrdemMenu().put(menu, ordemDrop);
			}
			setModulo(PerfilAcessoModuloEnum.TODOS);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Map<String, DashboardVO> mapDashboards;

	public Map<String, DashboardVO> getMapDashboards() {
		if (mapDashboards == null) {
			mapDashboards = new HashMap<String, DashboardVO>(0);
		}
		return mapDashboards;
	}

	public void setMapDashboards(Map<String, DashboardVO> mapDashboards) {
		this.mapDashboards = mapDashboards;
	}

	public void registrarPosicaoDashboard(DragDropEvent dropEvent) {
		DashboardVO drag = (DashboardVO) dropEvent.getData();
		DashboardVO drop = (DashboardVO) dropEvent.getData();
		if (drag != null && drop != null) {
			Integer ordemDrag = drag.getOrdem();
			drag.setOrdem(drop.getOrdem());
			drop.setOrdem(ordemDrag);
			persistirDashboard(drag);
			persistirDashboard(drop);
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
			List<DashboardVO> dashboards = getFacadeFactory().getDashboardInterfaceFacade()
					.consultarDashboardPorUsuarioAmbiente(getUsuarioLogado(), TipoVisaoEnum.ADMINISTRATIVA,
							PerfilAcessoModuloEnum.ADMINISTRATIVO);
			getMapDashboards().clear();
			for (Iterator<DashboardVO> iterator = dashboards.iterator(); iterator.hasNext();) {
				DashboardVO dashboardVO = (DashboardVO) iterator.next();
				if(dashboardVO.getTipoDashboard().equals(TipoDashboardEnum.FAVORITOS)) {
					setDashboardFavorito(dashboardVO);
				}else if(dashboardVO.getTipoDashboard().equals(TipoDashboardEnum.BANNER_MATRICULA)) {
					setDashboardMatricular(dashboardVO);
				}else if(dashboardVO.getTipoDashboard().equals(TipoDashboardEnum.MARKETING)) {
					setDashboardBannerMarketing(dashboardVO);
				}else if(dashboardVO.getTipoDashboard().equals(TipoDashboardEnum.TWITTER)) {
					setDashboardTwitter(dashboardVO);
				} else if (dashboardVO.getTipoDashboard().equals(TipoDashboardEnum.LINK_UTEIS)) {
					setDashboardLinksUteis(dashboardVO);
				}
				getMapDashboards().put(dashboardVO.getTipoDashboard().name(), dashboardVO);
			}
			if (!getMapDashboards().containsKey(TipoDashboardEnum.FAVORITOS.name())) {
				getMapDashboards().put(TipoDashboardEnum.FAVORITOS.name(), getDashboardFavorito());
			}
			if (!getMapDashboards().containsKey(TipoDashboardEnum.MARKETING.name())
					&& !getBannerMarketing().isEmpty()) {
				getMapDashboards().put(TipoDashboardEnum.MARKETING.name(),getDashboardBannerMarketing());
			} else if (getMapDashboards().containsKey(TipoDashboardEnum.MARKETING.name())
					&& getBannerMarketing().isEmpty()) {
				getMapDashboards().remove(TipoDashboardEnum.MARKETING.name());
			}
			if (!getMapDashboards().containsKey(TipoDashboardEnum.BANNER_MATRICULA.name())
					&& !getCaminhoArquivoBanner().isEmpty()) {
				getMapDashboards().put(TipoDashboardEnum.BANNER_MATRICULA.name(), getDashboardMatricular());
			} else if (getMapDashboards().containsKey(TipoDashboardEnum.BANNER_MATRICULA.name())
					&& getCaminhoArquivoBanner().isEmpty()) {
				getMapDashboards().remove(TipoDashboardEnum.BANNER_MATRICULA.name());
			}
			if (!getMapDashboards().containsKey(TipoDashboardEnum.TWITTER.name()) && getConfiguracaoGeralSistemaVO().getApresentarCodigoTwitts()) {
				getMapDashboards().put(TipoDashboardEnum.TWITTER.name(), getDashboardTwitter());
			}else if (getMapDashboards().containsKey(TipoDashboardEnum.TWITTER.name())
					&& !getConfiguracaoGeralSistemaVO().getApresentarCodigoTwitts()) {
				getMapDashboards().remove(TipoDashboardEnum.TWITTER.name());
			}
			if (!getMapDashboards().containsKey(TipoDashboardEnum.LINK_UTEIS.name()) && !getListaLinksUteisVOs().isEmpty()) {
				getMapDashboards().put(TipoDashboardEnum.LINK_UTEIS.name(), getDashboardLinksUteis());
			} else if (getMapDashboards().containsKey(TipoDashboardEnum.LINK_UTEIS.name()) && getListaLinksUteisVOs().isEmpty()) {
				getMapDashboards().remove(TipoDashboardEnum.LINK_UTEIS.name());
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDashboardUsuarioLogado() {

	}

	public Collection<DashboardVO> getListaDashboard() {
		return getMapDashboards().values();
	}

	public void consultarPreferenciaSistemaUsuario() {
		try {
			
			if(getAplicacaoControle().getPreferenciaSistemaUsuarioCache().containsKey(getUsuario().getCodigo())) {
				getUsuario().setPreferenciaSistemaUsuarioVO(getAplicacaoControle().getPreferenciaSistemaUsuarioCache().get(getUsuario().getCodigo()));
			}else {
				getUsuario().setPreferenciaSistemaUsuarioVO(getFacadeFactory().getPreferenciaSistemaUsuarioFacade().consultarPorUsuario(getUsuario()));
			}
			consultarPosicaoMenu();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}



	public void persistirPreferenciaSistemaUsuario() {
		try {
			getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getUsuarioVO()
					.setCodigo(getUsuarioLogado().getCodigo());
			getFacadeFactory().getPreferenciaSistemaUsuarioFacade()
					.persistir(getUsuarioLogado().getPreferenciaSistemaUsuarioVO());
			if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				for (String key : getMapOrdemMenu().keySet()) {
					getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getMapOrdemMenu().get(key) + "",
							LoginControle.class.getSimpleName() + getUsuarioLogado().getCodigo(), key,
							getUsuarioLogado());
					getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getMapOrdemMenu().get(key) + "",
							LoginControle.class.getSimpleName() + getUsuarioLogado().getCodigo(), key,
							getUsuarioLogado());
				}
			}

			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirPreferenciaSistemaUsuario() {
		try {
			getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getUsuarioVO()
					.setCodigo(getUsuarioLogado().getCodigo());
			getFacadeFactory().getPreferenciaSistemaUsuarioFacade()
					.excluir(getUsuarioLogado().getPreferenciaSistemaUsuarioVO());
			getUsuarioLogado().setPreferenciaSistemaUsuarioVO(new PreferenciaSistemaUsuarioVO());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getScriptCssGerado() {
		if (Uteis.isAtributoPreenchido(getUsuarioLogado())
				&& Uteis.isAtributoPreenchido(getUsuarioLogado().getPreferenciaSistemaUsuarioVO())
				&& Uteis.isAtributoPreenchido(
						getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getConfiguracaoAparenciaSistemaVO())) {
			return getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getConfiguracaoAparenciaSistemaVO()
					.getScriptCssGerado()
					+ (getAppMovel() ? "" : getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getCssFonte());
		}
		return getAplicacaoControle().getConfiguracaoAparenciaSistemaVO().getScriptCssGerado()
				+ (getAppMovel() ? "" : getUsuarioLogado().getPreferenciaSistemaUsuarioVO().getCssFonte());
	}

	public List<ConfiguracaoAparenciaSistemaVO> getConfiguracaoAparenciaSistemaVOs() {
		return getAplicacaoControle().getConfiguracaoAparenciaSistemaVOs();			
	}

	public void inicializarBanner() {
		try {
			StringBuilder html = new StringBuilder();
			String url = "";						
			setPoliticaDivulgacaoMatriculaOnlineVOs(getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarBanners(getUsuarioLogadoClone()));
			for (PoliticaDivulgacaoMatriculaOnlineVO politica : getPoliticaDivulgacaoMatriculaOnlineVOs()) {
				url = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
						+ politica.getCaminhoBaseLogo().replaceAll("\\\\", "/").trim() + "/"
						+ politica.getNomeArquivoLogo().replaceAll("\\\\", "/").trim();
				politica.setUrlImagem(url);
				StringBuilder urlClick = new StringBuilder(UteisJSF.getUrlAplicacaoExterna());
//				html.append("<a href=\"").append();
				if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					urlClick.append("/visaoAdministrativo/academico/matriculaOnlineVisaoAdmForm.xhtml");
				}else if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
					urlClick.append("/visaoAluno/matriculaOnlineVisaoAlunoForm.xhtml");
				}else if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					urlClick.append("/visaoProfessor/matriculaOnlineVisaoProfessorForm.xhtml");
				}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					urlClick.append("/visaoCoordenador/matriculaOnlineVisaoCoordenadorForm.xhtml");
				}else {
					urlClick.append("/visaoAluno/matriculaOnlineForm.xhtml");
				}
				urlClick.append("?banner=").append(politica.getCodigo());
				politica.setUrlClick(urlClick.toString());
				html.append("<div class=\"col-md-12 pn\" style=\"height:280px\">");
				html.append(" 	<img id=\"bannerMat").append(politica.getCodigo()).append("\" src=\"").append(url).append("\" width=\"100%\" height=\"280px\"   title=\"").append(politica.getNome()).append("\"  />");				
				html.append("</div>");
				html.append("</a>");
			}
			setCaminhoArquivoBanner(html.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String loginAD() {
		try {
			ConfiguracaoLdapVO configuracaoLdapVO = this.obterConfiguracaoLdapVO(getEmailInstitucional());
			String saml = Uteis.obterSaml(configuracaoLdapVO);
			String base64 = Uteis.obterRequisicaoBase64Saml(saml);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect(Uteis.obterUrlRedirecionamentoSaml(configuracaoLdapVO, base64, getEmailInstitucional()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ALERTA);
		}
		return "";
	}

	private ConfiguracaoLdapVO obterConfiguracaoLdapVO(String emailInstitucional) throws Exception {
		return getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getConfiguracaoLdapVOs()
				.stream()
				.filter(conf -> emailInstitucional.toLowerCase().endsWith(conf.getDominio().toLowerCase()))
				.findFirst()
				.orElseThrow(() -> new Exception("Configurações de login para o domínio informado não encontrada."));
	}

	public String getEmailInstitucional() {
		if (emailInstitucional == null) {
			emailInstitucional = "";
		}
		return emailInstitucional;
	}

	public void setEmailInstitucional(String emailInstitucional) {
		this.emailInstitucional = emailInstitucional;
	}

	public Boolean getLogadoComEmailInstitucional() {
		if (logadoComEmailInstitucional == null) {
			logadoComEmailInstitucional = false;
		}
		if (context().getExternalContext().getSessionMap().containsKey("logadoComEmailInstitucional")) {
			return (Boolean) context().getExternalContext().getSessionMap().get("logadoComEmailInstitucional");
		}
		return logadoComEmailInstitucional;
	}

	public void setLogadoComEmailInstitucional(Boolean logadoComEmailInstitucional) {
		this.logadoComEmailInstitucional = logadoComEmailInstitucional;
	}
	
	public Boolean getApresentarRedefinirSenha() {
		if(apresentarRedefinirSenha == null) {
			apresentarRedefinirSenha = false;
		} 
		return apresentarRedefinirSenha;
	}
	
	public void setApresentarRedefinirSenha(Boolean apresentarRedefinirSenha) {
		this.apresentarRedefinirSenha = apresentarRedefinirSenha;
	}

	private List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs;
	
	public List<PoliticaDivulgacaoMatriculaOnlineVO> getPoliticaDivulgacaoMatriculaOnlineVOs() {
		if(politicaDivulgacaoMatriculaOnlineVOs == null) {
			politicaDivulgacaoMatriculaOnlineVOs =  new ArrayList<PoliticaDivulgacaoMatriculaOnlineVO>(0);
		}
		return politicaDivulgacaoMatriculaOnlineVOs;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineVOs(
			List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs) {
		this.politicaDivulgacaoMatriculaOnlineVOs = politicaDivulgacaoMatriculaOnlineVOs;
	}
	
	public DashboardVO getDashboardLinksUteis() {
		if (dashboardLinksUteis == null) {
			dashboardLinksUteis = new DashboardVO(TipoDashboardEnum.LINK_UTEIS, false, 20, getUsuarioLogado().getTipoVisaoAcesso(), getUsuarioLogado());
		}
		return dashboardLinksUteis;
	}

	public void setDashboardLinksUteis(DashboardVO dashboardLinksUteis) {
		this.dashboardLinksUteis = dashboardLinksUteis;
	}

	public List<LinksUteisVO> getListaLinksUteisVOs() {
		if (listaLinksUteisVOs == null) {
			listaLinksUteisVOs = new ArrayList<LinksUteisVO>(0);

		}
		return listaLinksUteisVOs;
	}

	public void setListaLinksUteisVOs(List<LinksUteisVO> listaLinksUteisVOs) {
		this.listaLinksUteisVOs = listaLinksUteisVOs;
	}

	public void consultarLinksUteisUsuario() {
		try {
			if(getAplicacaoControle().getLinksUteisUsuarioCache().containsKey(getUsuarioLogado().getCodigo())) {
				setListaLinksUteisVOs(getAplicacaoControle().getLinksUteisUsuarioCache().get(getUsuarioLogado().getCodigo()));
			}else {
				setListaLinksUteisVOs(getFacadeFactory().getLinksUteisFacade().consultarLinksUteisUsuario(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
		}
	}
	public Boolean getExisteTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao() {
		try {
			if(Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO().getTextoPadraoDadosSensiveisLGPD().getCodigo()) && getFacadeFactory().getControleAcessoFacade().verificarPermissaoFuncionalidadeUsuario("AcessoDados", getUsuarioLogado())) {
				
				existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao = Boolean.TRUE;
			}
		} catch (Exception e) {
			existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao = Boolean.FALSE;
		}
		return existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao;
	}

	public void setExisteTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao(Boolean existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao) {
		this.existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao = existeTextoPadraoConfigGeralSistemaEUsuarioPossuiPermissao;
	}
	
	public PessoaVO getPessoaVO() {
		return pessoaVO;
	}
	
	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}
	
	public void incializarDadosPessoa(UsuarioVO pessoa) {
		try {		
			setPessoaVO((PessoaVO)Uteis.clonar(pessoa.getPessoa()));
			if(pessoa.getPessoa().getNivelMontarDados() == null || !pessoa.getPessoa().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
				setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(getPessoaVO().getCodigo(), false, true, false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setPessoaVO(new PessoaVO());
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
	
	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomInVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
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
	
	public void renderizarUpload() {
		setExibirUpload(false);
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
	
	public void confirmarFoto() throws Exception {
		if (Uteis.isAtributoPreenchido(getPessoaVO())) {
			getPessoaVO().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(getConfiguracaoGeralPadraoSistema(), "FUNCIONARIO");
			getFacadeFactory().getPessoaFacade().alterarFoto(getPessoaVO(), getUsuarioLogado(),getConfiguracaoGeralPadraoSistema());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			getLoginControle().setCaminhoFotoUsuario(getCaminhoFotoUsuario());
			removerImagensUploadArquivoTemp();
		}
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
			// throw e;
		}
	}
	
	public void cancelar() {
		try {
			setRemoverFoto((Boolean) false);
			setExibirUpload(true);
			setExibirBotao(false);
			getFacadeFactory().getArquivoHelper().realizarGravacaoFotoSemRecorte(getPessoaVO().getArquivoImagem(),getConfiguracaoGeralPadraoSistema());
			setCaminhoFotoUsuario(getFacadeFactory().getArquivoHelper().renderizarFotoUsuario(getPessoaVO().getArquivoImagem(), PastaBaseArquivoEnum.IMAGEM.getValue(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg", false));
			confirmarFoto();
		} catch (Exception ex) {
			setOncompleteModal("PF('panelImagem').show();");
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}
	
	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getPessoaVO().getArquivoImagem(),getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getPessoaVO().getArquivoImagem(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void realizarCapturaIpCliente(UsuarioVO usuario) {
		try {
		    String ipAddress = this.request().getHeader("X-FORWARDED-FOR");
		    if (ipAddress != null) {
		    	usuario.setIpUltimoAcesso(ipAddress);
//		        ipAddress = ipAddress.replaceFirst(",.*", ""); // captura apenas o primeiro IP
		    } else {
		    	ipAddress = this.request().getRemoteAddr();
		    	if (ipAddress != null) {
		    		usuario.setIpUltimoAcesso(ipAddress);
		    	}		        
		    }
		} catch(Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.LOGIN, e);
		}
	}
	
	public void redirecionarTelaComunicadoForm() {
		removerControleMemoriaFlashTela("ComunicacaoInternaControle");
		executarMetodoControle(ComunicacaoInternaControle.class.getSimpleName(), "consultarTodasEntradasPorIntervaloData");
	}

}
