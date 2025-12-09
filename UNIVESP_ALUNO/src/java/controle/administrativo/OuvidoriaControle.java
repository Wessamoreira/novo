package controle.administrativo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces. context.FacesContext;
import jakarta.faces. model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.LoginControle;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipagemOuvidoriaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.AvaliacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoAtendimentoEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemOuvidoriaEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoUsuario;

/**
 * 
 * @author Pedro
 */

@Controller("OuvidoriaControle")
@Scope("viewScope")
@Lazy
public class OuvidoriaControle extends SuperControle {
	private AtendimentoVO atendimentoVO;
	private AtendimentoInteracaoSolicitanteVO atendimentoInteracaoSolicitanteVO;
	private AtendimentoInteracaoDepartamentoVO atendimentoInteracaoDepartamentoVO;
	private ArquivoVO arquivoVO;
	private ConfiguracaoAtendimentoVO configuracaoAtendimentoVO;
	private String erroUpload;
	private String msgErroUpload;
	protected List<SelectItem> listaSelectItemMatriculaAluno;
	protected List<SelectItem> listaSelectItemtipagemOuvidoriaVO;
	protected List<SelectItem> listaSelectItemDepartameto;
	protected List<SelectItem> listaSelectItemFuncionario;
	protected List<SelectItem> listaSelectItemResponsavelAtendimento;
	protected SituacaoAtendimentoEnum situacaoOuvidoriaEnum;
	protected Boolean novoOuvidoria;
	protected Boolean lerOuvidoria;
	protected Boolean apresentarUsuarioParaSolicitante;
	protected Boolean apresentarUsuarioDiretorMultiCampus;
	protected Boolean apresentarTextoExplicativoOuvidoria;
	protected Integer codigoUnidadeEnsino;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String otimo;
	private String bom;
	private String regular;
	private String ruim;
	private Boolean manterRichModalAvaliacaoAberto;
	private Date dataInicial;
	private Date dataTermino;
	private Integer codigoResponsavelAtendimento;
	private String nomeSolicitante;
	private String CPFSolicitante;
	private String emailSolicitante;
	private Integer codigoTipoAtendimento;
	private Integer totalOuvidoriaAtrasada;
	private Integer totalOuvidoriaNova;
	private Integer totalOuvidoriaEmAnaliseOuvidor;
	private Integer totalOuvidoriaFinalizada;
	private Integer totalOuvidoriaAguardandoDepartamento;
	private Integer totalOuvidoriaAguardandoSolicitante;
	private String quadroSituacao;
	private Boolean habilitarEscolharOuvidor;
	private Integer codigoRespostaAtendimentoInteracaoSolicitanteVO;
	private String consultarPessoa;
	private String camposConsulta;
	private List<PessoaVO> listaPessoa;
	

	public OuvidoriaControle() throws Exception {
		getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		getControleConsultaOtimizado().setPaginaAtual(1);
		if (getUsuarioLogado().getIsApresentarVisaoOuvidoria()) {
			consultarOuvidoriaVisaoUsuarioSemVinculo();
		}
	}

	@PostConstruct
	public void realizarInicializacaoPeloServlet() {
		Integer codigoOrigem = 0;
		String tipoOrigem = "";
		try {
			if (getUsuarioLogado() == null || !Uteis.isAtributoPreenchido(getUsuarioLogado()) || (Uteis.isAtributoPreenchido(getUsuarioLogado()) && !getUsuarioLogado().getVisaoLogar().equals("ouvidoria"))) {
				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
				if (request.getParameter("codigoOrigem") != null && request.getParameter("tipoOrigem") != null) {
					codigoOrigem = Integer.parseInt((String) request.getParameter("codigoOrigem"));
					tipoOrigem = (String) request.getParameter("tipoOrigem");
				}
				if (codigoOrigem != 0 && !tipoOrigem.isEmpty()) {
					realizarPreenchimnetoDadosOuvidoria(codigoOrigem, tipoOrigem);
				} else {
					novo();
					realizarValidacaoResponsavelAtendimento();
				}
				// Iniciar ouvidoria pela visao do usuario sem vinculo com
				// unidade de ensino ou pela visao professor
				if ((getAtendimentoVO().getUnidadeEnsino() == null || getAtendimentoVO().getUnidadeEnsino().intValue() == 0)) {
					setApresentarUsuarioParaSolicitante(false);
					setLerOuvidoria(false);
					setNovoOuvidoria(true);
					montarListaSelectItemUnidadeEnsino("");
				}else {
					montarListaSelectItemUnidadeEnsino("");
				}
			}else if(getUsuarioLogado() != null && getUsuarioLogado().getIsApresentarVisaoProfessor()){
				consultarOuvidoriaVisaoProfessorValidandoConfiguracaoAtendimento();
			}else if(getUsuarioLogado() != null && getUsuarioLogado().getIsApresentarVisaoCoordenador()){
				consultarOuvidoriaVisaoCoordenadorValidandoConfiguracaoAtendimento();
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void realizarValidacaoResponsavelAtendimento() throws Exception {
		if(Uteis.isAtributoPreenchido(getUsuarioLogado())){
		    if(getUsuarioLogado().getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())){
	            setHabilitarEscolharOuvidor(true);
	        } else {
	            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado());
	            Iterator<SelectItem> i = getListaSelectItemResponsavelAtendimento().iterator();
	             while (i.hasNext()) {
	                SelectItem item = (SelectItem) i.next();
	                if(item.getValue().equals(funcionario.getCodigo())) {
	                    setCodigoResponsavelAtendimento(funcionario.getCodigo());
	                    setHabilitarEscolharOuvidor(false);
	                    getControleConsultaOtimizado().setPaginaAtual(1);
	                    getControleConsultaOtimizado().setPage(1);                  
	                    consultar();
	                }
	            }  
	        }    
		}
	    
	}

	public void realizarPreenchimnetoDadosOuvidoria(Integer codigoOrigem, String tipoOrigem) throws Exception {
		setApresentarUsuarioParaSolicitante(false);
		setApresentarTextoExplicativoOuvidoria(false);
		setLerOuvidoria(false);
		setNovoOuvidoria(false);
		setAtendimentoVO(new AtendimentoVO());
		setConfiguracaoAtendimentoVO(new ConfiguracaoAtendimentoVO());
		setCodigoUnidadeEnsino(0);
		getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
		setAtendimentoVO(getFacadeFactory().getAtendimentoFacade().consultarPorChavePrimaria(codigoOrigem, Uteis.NIVELMONTARDADOS_TODOS, getAtendimentoVO().getIdEntidade(), getUsuarioLogado()));
		
		setCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino());
		inicializarListasSelectItemTodosComboBox();
		setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
		setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
		setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		setArquivoVO(new ArquivoVO());
		if (getIsUsuarioLogadoAndFuncionarioDepartamento() && !getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO().getExisteDataRegistroResposta()) {
			setAtendimentoInteracaoDepartamentoVO(getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO());
		}
		if (tipoOrigem.equals("VISAO_ALUNO") || tipoOrigem.equals("VISAO_PROFESSOR") || tipoOrigem.equals("VISAO_COORDENADOR")) {
			setLerOuvidoria(true);
			setNovoOuvidoria(false);
			if (!getAtendimentoVO().getAtendimentoJaVisualizado()) {
				getAtendimentoVO().setAtendimentoJaVisualizado(true);
				getFacadeFactory().getAtendimentoFacade().realizarAtualizacaoAtendimentoJaVisualizado(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
				LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
				login.setQtdeAtualizacaoOuvidoria(null);
			}
			if (tipoOrigem.equals("VISAO_PROFESSOR") || tipoOrigem.equals("VISAO_COORDENADOR")) {
				montarListaSelectItemUnidadeEnsino("");
			}
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TitulacaoCurso</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			setApresentarUsuarioParaSolicitante(false);
			setApresentarTextoExplicativoOuvidoria(false);
			setLerOuvidoria(false);
			setNovoOuvidoria(true);
			setAtendimentoVO(new AtendimentoVO());
			setConfiguracaoAtendimentoVO(new ConfiguracaoAtendimentoVO());
			setCodigoUnidadeEnsino(0);
			inicializarListasSelectItemTodosComboBox();
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			getAtendimentoVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
			setCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
			getFacadeFactory().getAtendimentoFacade().realizarValidacoesParaBuscarResponsavelAtendimento(getAtendimentoVO(), getAtendimentoVO().getUnidadeEnsino(), true, getAtendimentoVO().getTipoAtendimentoEnum(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			setControleConsulta(new ControleConsulta());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setArquivoVO(new ArquivoVO());
			getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());				getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			if (getUsuarioLogado().getTipoUsuario().equals(TipoUsuario.DIRETOR_MULTI_CAMPUS.getValor())) {
				setApresentarUsuarioDiretorMultiCampus(true);
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TitulacaoCurso</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			AtendimentoVO obj = (AtendimentoVO) context().getExternalContext().getRequestMap().get("ouvidoriaItens");
			obj.setNovoObj(false);
			setAtendimentoVO(getFacadeFactory().getAtendimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, obj.getIdEntidade(), getUsuarioLogado()));
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			inicializarListasSelectItemTodosComboBox();
			if (getIsUsuarioLogadoAndFuncionarioDepartamento() && !getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO().getExisteDataRegistroResposta()) {
	            setAtendimentoInteracaoDepartamentoVO(getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO());
	        }
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	
	
	public void persistirPorVisaoAdminstrariva() {
		try {
			getAtendimentoVO().setAtendimentoJaVisualizado(false);
			getFacadeFactory().getAtendimentoFacade().persistir(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema() , codigoRespostaAtendimentoInteracaoSolicitanteVO);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 

	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	@SuppressWarnings("finally")
	public void persistirPorOutrasVisoes() { 
	    try {
	        executarValidacaoSimulacaoVisaoProfessor();
	        executarValidacaoSimulacaoVisaoAluno();
	        getAtendimentoVO().setAtendimentoJaVisualizado(true);
	        
	        UsuarioVO usuarioVO = new UsuarioVO();
	        if (!Uteis.isAtributoPreenchido(getUsuarioLogado())) {
	            usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getConfiguracaoGeralPadraoSistema().getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
	            usuarioVO.setPerfilAcesso(getConfiguracaoGeralPadraoSistema().getPerfilPadraoOuvidoria());
	        } else {
	            usuarioVO = getUsuarioLogado();
	        }
	        
	        if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
	            getAtendimentoVO().setMatriculaAluno(getVisaoAlunoControle().getMatricula().getMatricula());
	        }
	            
	        getFacadeFactory().getAtendimentoFacade().persistir(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), usuarioVO, getConfiguracaoGeralPadraoSistema() , codigoRespostaAtendimentoInteracaoSolicitanteVO);
	        setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
	        
	    } catch (ConsistirException e) {
	        setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	    } catch (Exception e) {
	        setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	    } finally {
	        // Bloco de redirecionamento manual
	        try {
	            if (!getAtendimentoVO().getUsername().isEmpty() && !getAtendimentoVO().getSenha().isEmpty()) {
	                LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
	                login.setUsername(getAtendimentoVO().getUsername());
	                login.setSenha(getAtendimentoVO().getSenha());
	                
	                if (getNomeTelaAtual().endsWith("homeOuvidoriaForm.xhtml")) {
	                    // login3 já é void e faz o redirect internamente. Apenas chamamos.
	                    login.login3();
	                    return; // Para a execução aqui
	                } else {
	                    // Redirecionamento Manual para a mesma tela
	                    String url = Uteis.getCaminhoRedirecionamentoNavegacao(getNomeTelaAtual());
	                    redirecionarManualmente(url);
	                    return;
	                }
	            }
	            
	            // Redirecionamento padrão (Else do finally)
	            String urlDefault = Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaCons");
	            redirecionarManualmente(urlDefault);
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	// Método auxiliar para evitar repetição de código de redirect
	private void redirecionarManualmente(String url) throws IOException {
	    if (url != null) {
	        // Remove parametros JSF antigos se houver
	        String urlLimpa = url.split("\\?")[0];
	        if (!urlLimpa.startsWith("/")) {
	            urlLimpa = "/" + urlLimpa;
	        }
	        // Adiciona extensão se faltar (ajuste conforme seu padrão, ex: .xhtml)
	        if (!urlLimpa.endsWith(".xhtml")) {
	             urlLimpa += ".xhtml";
	        }

	        ExternalContext ec = context().getExternalContext();
	        ec.redirect(ec.getRequestContextPath() + urlLimpa);
	        context().responseComplete();
	    }
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void realizarAvaliacaoAtendimento() {
		try {
			getAtendimentoVO().setAvaliacaoAtendimentoEnum(AvaliacaoAtendimentoEnum.valueOf(getOtimo() + getBom() + getRegular() + getRuim()));
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getAtendimentoFacade().realizarPersistenciaAvaliacaoAtendimento(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			setManterRichModalAvaliacaoAberto(false);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setManterRichModalAvaliacaoAberto(true);
			getAtendimentoVO().setAvaliacaoAtendimentoEnum(AvaliacaoAtendimentoEnum.NENHUM);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarValidacaoParaPreenchimentoAvaliacao() {
		String avaliacao = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("avaliacao");
		if (avaliacao.equals("OTIMO")) {
			setBom("");
			setRegular("");
			setRuim("");
		} else if (avaliacao.equals("BOM")) {
			setOtimo("");
			setRegular("");
			setRuim("");
		} else if (avaliacao.equals("REGULAR")) {
			setOtimo("");
			setBom("");
			setRuim("");
		} else if (avaliacao.equals("RUIM")) {
			setOtimo("");
			setBom("");
			setRegular("");
		}
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	// /**
	// * Rotina responsável por gravar no BD os dados editados de um novo objeto
	// * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda
	// não
	// * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	// * contrário é acionado o <code>alterar()</code>. Se houver alguma
	// * inconsistência o objeto não é gravado, sendo re-apresentado para o
	// * usuário juntamente com uma mensagem de erro.
	// */
	// public String persistir() {
	// try {
	// getFacadeFactory().getAtendimentoFacade().persistir(getAtendimentoVO(),
	// getAtendimentoVO().getIdEntidade(), getUsuarioLogado(),
	// getConfiguracaoGeralPadraoSistema());
	// setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
	// } catch (ConsistirException e) {
	// setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	// } finally {
	// if(!getAtendimentoVO().getUsername().isEmpty() &&
	// !getAtendimentoVO().getSenha().isEmpty()){
	// LoginControle login = (LoginControle)
	// context().getExternalContext().getSessionMap().get("LoginControle");
	// login.setUsername(getAtendimentoVO().getUsername());
	// login.setSenha(getAtendimentoVO().getSenha());
	// return login.login3();
	// }
	// return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
	// }
	// }

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TitulacaoCursoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			setQuadroSituacao("");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public void consultarAtendimentoAtrazadas() {
		try {
			setQuadroSituacao("ATRASADA");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//		finally {
//			return "consultar";
//		}
	}
	public void consultarAtendimentoNovas() {
		try {
			setQuadroSituacao("NOVA");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//		finally {
//			return "consultar";
//		}
	}
	public void consultarAtendimentoAnaliseOuvidor() {
		try {
			setQuadroSituacao("ANALISE_OUVIDOR");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//		finally {
//			return "consultar";
//		}
	}
	public void  consultarAtendimentoFinalizadas() {
		try {
			setQuadroSituacao("FINALIZADA");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
//		finally {
//			return "consultar";
//		}
	}
	public void consultarAtendimentoAguardandoDepartamento() {
		try {
			setQuadroSituacao("AGUARDANDO_DEPARTAMENTO");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//		finally {
//			return "consultar";
//		}
	}
	public void consultarAtendimentoAguardandoSolicitante() {
		try {
			setQuadroSituacao("AGUARDANDO_SOLICITANTE");
			realizarConsultar();
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//		finally {
//			return "consultar";
//		}
	}
	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TitulacaoCursoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String realizarConsultar() {
		try {
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			super.consultar();
			HashMap<String, Object> ma = getFacadeFactory().getAtendimentoFacade().consultar(getDataInicial(), getDataTermino(), getCodigoResponsavelAtendimento(),getNomeSolicitante(),
					getCPFSolicitante(), getEmailSolicitante(), getCodigoTipoAtendimento(), getQuadroSituacao(), TipoAtendimentoEnum.OUVIDORIA, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			getControleConsultaOtimizado().setListaConsulta((List)ma.get("LISTA"));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getAtendimentoFacade().consultarTotalRegistro(getDataInicial(), getDataTermino(), getCodigoResponsavelAtendimento(),getNomeSolicitante(),
					getCPFSolicitante(), getEmailSolicitante(), getCodigoTipoAtendimento(), getQuadroSituacao(), TipoAtendimentoEnum.OUVIDORIA));
			
			setTotalOuvidoriaNova((Integer)ma.get("NOVA"));
			setTotalOuvidoriaAguardandoDepartamento((Integer)ma.get("AGUARDANDO_DEPARTAMENTO"));
			setTotalOuvidoriaAguardandoSolicitante((Integer)ma.get("AGUARDANDO_SOLICITANTE"));
			setTotalOuvidoriaAtrasada((Integer)ma.get("ATRASADA"));
			setTotalOuvidoriaEmAnaliseOuvidor((Integer)ma.get("ANALISE_OUVIDOR"));
			setTotalOuvidoriaFinalizada((Integer)ma.get("FINALIZADA"));
			
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
		return "";
	}

	public void scrollerListener() throws Exception {
		
		consultar();
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public boolean getApresentarResultadoConsulta() {
		if (getListaConsulta() == null || getListaConsulta().isEmpty()) {
			return false;
		}
		return true;
	}

	public void limparConsultaRichModal() {
		getListaConsulta().clear();
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>TitulacaoCursoVO</code> Após a exclusão ela automaticamente aciona
	 * a rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getAtendimentoFacade().excluir(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
//			finally {
//			return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
//		}
	}

	public void realizarFinalizacaoOuvidoria() {
		try {
			getFacadeFactory().getAtendimentoFacade().adicionarAtendimentoInteracaoSolicitanteVO(getAtendimentoVO(), getAtendimentoInteracaoSolicitanteVO(), true);
			getAtendimentoVO().setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.FINALIZADA);
			getAtendimentoVO().setDataFechamento(new Date());
			getAtendimentoVO().setResponsavelFechamento(getUsuarioLogadoClone());
			getAtendimentoVO().setAtendimentoJaVisualizado(false);
			getAtendimentoInteracaoSolicitanteVO().setDataRegistro(new Date());
			getAtendimentoInteracaoSolicitanteVO().setUsuarioQuestionamento(getUsuarioLogado());
			getFacadeFactory().getAtendimentoFacade().realizarFinalizacaoOuvidoria(getAtendimentoVO(), getAtendimentoInteracaoSolicitanteVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarConsultarPessoaExistePorCPF() {
		try {
			getFacadeFactory().getAtendimentoFacade().realizarConsultarPessoaExistePorCPF(getAtendimentoVO(), getAtendimentoVO().getTipoAtendimentoEnum(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			if (getAtendimentoVO().getNome().isEmpty()) {
				setMensagemID("msg_Ouvidoria_primeiroContato", Uteis.ALERTA);
			} else {
				montarListaSelectItemMatriculaAluno();
				getFacadeFactory().getAtendimentoFacade().realizarValidacoesParaBuscarResponsavelAtendimento(getAtendimentoVO(), getAtendimentoVO().getUnidadeEnsino(), false, getAtendimentoVO().getTipoAtendimentoEnum(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
				if (getAtendimentoVO().getExistePessoa() && getUsuarioLogado() == null) {
					setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAtendimentoVO().getPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
					context().getExternalContext().getSessionMap().put("usuarioLogado", getUsuario());
				}
			}
			setApresentarUsuarioParaSolicitante(!getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoaSeUsuarioExiste(getAtendimentoVO().getPessoaVO().getCodigo(), false, null));
			if (getApresentarUsuarioParaSolicitante()) {
				getAtendimentoVO().setUrlAplicacao(UteisJSF.getAcessoAplicadoURL());
				getAtendimentoVO().setUsername(Uteis.retirarMascaraCPF(getAtendimentoVO().getCPF()));
				if (getUsuarioLogado() != null) {
					getAtendimentoVO().setSenha(Uteis.retirarMascaraCPF(getAtendimentoVO().getCPF()));
				}
			}else {
				setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAtendimentoVO().getPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));				
				getAtendimentoVO().setUsername("");
				getAtendimentoVO().setSenha("");
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void realizarEdicaoAtendimentoInteracaoDepartamento() {
		AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoDepartamentoItens");
		setAtendimentoInteracaoDepartamentoVO(obj);
	}

	public void realizarRegistroInteracaoDepartamento() {
		try {
			AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoDepartamentoItens");
			getAtendimentoVO().setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.EM_PROCESSAMENTO);
			getFacadeFactory().getAtendimentoFacade().realizarRegistroInteracaoDepartamento(getAtendimentoVO(), obj, true, getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema() ,codigoRespostaAtendimentoInteracaoSolicitanteVO );
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAdicaoAtendimentoInteracaoDepartamento() {
		try {
	//if (getIsUsuarioLogadoAndFuncionarioDepartamento() && getAtendimentoInteracaoDepartamentoVO().getDepartamento().getCodigo() == 0) {
		//		throw new Exception("Não é possivel inserir mais uma resposta para mesma pergunta.");
			//}
			if (getIsUsuarioLogadoAndFuncionarioDepartamento() && (!Uteis.retiraTags(getAtendimentoInteracaoDepartamentoVO().getResposta()).replaceAll("Untitled document", "").trim().isEmpty())) {
				getAtendimentoInteracaoDepartamentoVO().setDataRegistroRespostaTemp(new Date());
			}

			if (getAtendimentoInteracaoDepartamentoVO().getFuncionarioVO().getPessoa().getNome().isEmpty()) {
				if (getAtendimentoInteracaoDepartamentoVO().getResposta() != null && getAtendimentoInteracaoDepartamentoVO().getResposta() != "" ) {					
					String resposta = getAtendimentoInteracaoDepartamentoVO().getResposta();
					setAtendimentoInteracaoDepartamentoVO(getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO());
					getAtendimentoInteracaoDepartamentoVO().setResposta(resposta);
					setCodigoRespostaAtendimentoInteracaoSolicitanteVO(getAtendimentoInteracaoDepartamentoVO().getCodigo());
					getAtendimentoInteracaoDepartamentoVO().setDataRegistroResposta(new Date());
					getAtendimentoInteracaoDepartamentoVO().setUsuariorespostaquestionamento(getUsuarioLogado());
				}else {
					getAtendimentoInteracaoDepartamentoVO().setDataRegistro(new Date());
					getAtendimentoInteracaoDepartamentoVO().setUsuarioquestionamento(getUsuarioLogado());
					getAtendimentoInteracaoDepartamentoVO().setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(getAtendimentoInteracaoDepartamentoVO().getFuncionarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}
			}
			if (((getAtendimentoInteracaoDepartamentoVO().getQuestionamento() == null) || (Uteis.retiraTags(getAtendimentoInteracaoDepartamentoVO().getQuestionamento()).replaceAll("Untitled document", "").trim().isEmpty()))) {
				getAtendimentoInteracaoDepartamentoVO().setQuestionamento(getAtendimentoVO().getDescricao());
			}
			if (!getArquivoVO().getNome().isEmpty()) {
				adicionarArquivoLista();
			}
				
			getFacadeFactory().getAtendimentoFacade().adicionarAtendimentoInteracaoDepartamentoVO(getAtendimentoVO(), getAtendimentoInteracaoDepartamentoVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRemocaoAtendimentoInteracaoDepartamento() {
		try {
			AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoDepartamentoItens");
			getFacadeFactory().getAtendimentoFacade().removerAtendimentoInteracaoDepartamentoVO(getAtendimentoVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRemocaoRespostaAtendimentoInteracaoDepartamento() {
		try {
			AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoDepartamentoItens");
			obj.setResposta("");
			obj.setDataRegistroResposta(null);
			obj.setDataRegistroRespostaTemp(null);
			getFacadeFactory().getAtendimentoFacade().adicionarAtendimentoInteracaoDepartamentoVO(getAtendimentoVO(), obj);
			setAtendimentoInteracaoDepartamentoVO(obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRegistroRespostaInteracaoDepartamento() {
		try {
			AtendimentoInteracaoDepartamentoVO obj = (AtendimentoInteracaoDepartamentoVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoDepartamentoItens");
			getAtendimentoVO().setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR);
			obj.setDataRegistroResposta(obj.getDataRegistroRespostaTemp());
			getFacadeFactory().getAtendimentoFacade().realizarRegistroInteracaoDepartamento(getAtendimentoVO(), obj, false, getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),codigoRespostaAtendimentoInteracaoSolicitanteVO);
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarEdicaoAtendimentoInteracaoSolicitante() {
		AtendimentoInteracaoSolicitanteVO obj = (AtendimentoInteracaoSolicitanteVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoSolicitanteItens");
		setAtendimentoInteracaoSolicitanteVO(obj);
	}

	public void realizarAdicaoAtendimentoInteracaoSolicitante() {
		try {
			getFacadeFactory().getAtendimentoFacade().adicionarAtendimentoInteracaoSolicitanteVO(getAtendimentoVO(), getAtendimentoInteracaoSolicitanteVO(), true);
			if (!getArquivoVO().getNome().isEmpty()) {
				adicionarArquivoLista();
			}
			getAtendimentoInteracaoSolicitanteVO().setDataRegistro(new Date());
			getAtendimentoInteracaoSolicitanteVO().setUsuarioQuestionamento(getUsuarioLogado());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRemocaoAtendimentoInteracaoSolicitante() {
		try {
			AtendimentoInteracaoSolicitanteVO obj = (AtendimentoInteracaoSolicitanteVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoSolicitanteItens");
			getFacadeFactory().getAtendimentoFacade().removerAtendimentoInteracaoSolicitanteVO(getAtendimentoVO(), obj);
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRegistroInteracaoSolicitante() {
		try {
			AtendimentoInteracaoSolicitanteVO obj = (AtendimentoInteracaoSolicitanteVO) context().getExternalContext().getRequestMap().get("atendimentoInteracaoSolicitanteItens");
			getAtendimentoVO().setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.AGURADANDO_INFORMACAO_SOLICITANTE);
			getAtendimentoVO().setAtendimentoJaVisualizado(false);
			getFacadeFactory().getAtendimentoFacade().realizarRegistroInteracaoSolicitante(getAtendimentoVO(), obj, true, getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRespostaAtendimentoInteracaoSolicitante() {
		setAtendimentoInteracaoSolicitanteVO(getAtendimentoVO().getObterUltimoAtendimentoInteracaoSolicitanteVO());
	}

	public void realizarRegistroInteracaoSolicitantePorAluno() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getAtendimentoInteracaoSolicitanteVO().setDataRegistroRespostaQuestionamento(new Date());
			getAtendimentoInteracaoSolicitanteVO().setUsuarioRespostaQuestionamento(new UsuarioVO());
			getFacadeFactory().getAtendimentoFacade().adicionarAtendimentoInteracaoSolicitanteVO(getAtendimentoVO(), getAtendimentoInteracaoSolicitanteVO(), false);
			if (!getArquivoVO().getNome().isEmpty()) {
				adicionarArquivoLista();
			}
			getAtendimentoVO().setSituacaoAtendimentoEnum(SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR);
			getAtendimentoVO().setAtendimentoJaVisualizado(true);
			getAtendimentoInteracaoSolicitanteVO().setUsuarioRespostaQuestionamento(getUsuarioLogado());
			getFacadeFactory().getAtendimentoFacade().realizarRegistroInteracaoSolicitante(getAtendimentoVO(), getAtendimentoInteracaoSolicitanteVO(), false, getAtendimentoVO().getIdEntidade(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		setTotalOuvidoriaNova(0);
		setTotalOuvidoriaAguardandoDepartamento(0);
		setTotalOuvidoriaAguardandoSolicitante(0);
		setTotalOuvidoriaAtrasada(0);
		setTotalOuvidoriaEmAnaliseOuvidor(0);
		setTotalOuvidoriaFinalizada(0);
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
//		return "realizarConsultarOuvidoria";
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaCons");
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemTipagemOuvidoria();
		montarListaSelectItemMatriculaAluno();
		montarListaSelectItemDepartamento();
		montarListaSelectItemResponsavelAtendimento();

	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemMatriculaAluno() {
		try {
			montarListaSelectItemMatriculaAluno("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemMatriculaAluno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoaParaComboBox(getAtendimentoVO().getPessoaVO().getCodigo(), 0, false, getUsuarioLogado());
			if (resultadoConsulta.size() == 1) {
				getAtendimentoVO().setMatriculaAluno(((MatriculaVO) resultadoConsulta.get(0)).getMatricula());
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem("", ""));
			while (i.hasNext()) {
				MatriculaVO obj = (MatriculaVO) i.next();
				objs.add(new SelectItem(obj.getMatricula(), obj.getMatricula().toString()));
			}
			setListaSelectItemMatriculaAluno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTipagemOuvidoria() {
		try {
			montarListaSelectItemTipagemOuvidoria("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemTipagemOuvidoria(String prm) throws Exception {
		List<TipagemOuvidoriaVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getTipagemOuvidoriaFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.size() == 1) {
				getAtendimentoVO().setTipagemOuvidoriaVO((TipagemOuvidoriaVO) resultadoConsulta.get(0));
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipagemOuvidoriaVO obj = (TipagemOuvidoriaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemtipagemOuvidoriaVO(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemDepartamento(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {

			resultadoConsulta = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			getAtendimentoInteracaoDepartamentoVO().getFuncionarioVO().setCodigo(0);
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemDepartameto(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFuncionario() {
		try {
			montarListaSelectItemFuncionario("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemFuncionario(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getFuncionarioFacade().consultarPorNomeECodigoDepartamentoEMultiDepartamento("", getAtendimentoInteracaoDepartamentoVO().getDepartamento().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			getAtendimentoInteracaoDepartamentoVO().getFuncionarioVO().setCodigo(0);
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FuncionarioVO obj = (FuncionarioVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getPessoa().getNome().toString()));
			}
			setListaSelectItemFuncionario(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getFile() != null && uploadEvent.getFile().getSize() > 15360000) {
				setErroUpload("PF('panelMsgErroUpload').show()");
				setMsgErroUpload("Prezado usuário, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
			} else {
				setErroUpload("PF('panelMsgErroUpload').hide()");
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.OUVIDORIA_TMP, getUsuarioLogado());
				getArquivoVO().setDescricao(uploadEvent.getFile().getFileName().substring(0, uploadEvent.getFile().getFileName().lastIndexOf(".")));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void adicionarArquivoLista() {
		try {
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getArquivoVO().setDataUpload(new Date());
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.ATENDIMENTO.getValor());
			getFacadeFactory().getAtendimentoFacade().adicionarArquivoVO(getAtendimentoVO(), getArquivoVO());
			setArquivoVO(new ArquivoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerArquivoLista() throws Exception {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			getFacadeFactory().getAtendimentoFacade().removerArquivoVO(getAtendimentoVO(), obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTamanhoMaximoUpload() {
		String tamanhoMaximo = ((HttpServletRequest) context().getExternalContext().getRequest()).getSession().getServletContext().getInitParameter("tamanhoMaximoUpload");
		if (tamanhoMaximo == null) {
			return "Tamanho Máximo Permitido: 15MB.";
		} else {
			return "Tamanho Máximo Permitido: " + tamanhoMaximo;
		}
	}

	public void caminhoServidorDownload() throws CloneNotSupportedException {
		ArquivoVO arquivoVO = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		
		if(!arquivoVO.getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			if (cloneArquivo.getPastaBaseArquivo().endsWith("TMP")) {
				cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
			}
			context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);		
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorConfiguracaoAtendimento(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
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
	
	public void montarListaSelectItemResponsavelAtendimento() {		
		try {
			List<FuncionarioVO> resultadoConsulta = null;
			Iterator i = null;
			try {
				resultadoConsulta = getFacadeFactory().getAtendimentoFacade().realizarBuscaPorTodosReponsavelAtendimento(TipoAtendimentoEnum.OUVIDORIA);
				i = resultadoConsulta.iterator();
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					FuncionarioVO obj = (FuncionarioVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getPessoa().getNome()));
				}
				setListaSelectItemResponsavelAtendimento(objs);
			} catch (Exception e) {
				throw e;
			} finally {			
				Uteis.liberarListaMemoria(resultadoConsulta);
				i = null;
			}	
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
		}
		
	}
	

	@PostConstruct
	public void consultarOuvidoriaVisaoAlunoValidandoConfiguracaoAtendimento() {
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			try {
				setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				consultarOuvidoriaVisaoAluno();
				setApresentarTextoExplicativoOuvidoria(true);
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}		
		}
	}

	public String consultarOuvidoriaVisaoAluno() {
		try {
			VisaoAlunoControle visao = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visao != null) {
				setAtendimentoVO(new AtendimentoVO());
				setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
				setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
				setArquivoVO(new ArquivoVO());
				setLerOuvidoria(false);
				setNovoOuvidoria(false);
				setApresentarUsuarioParaSolicitante(false);
				setApresentarTextoExplicativoOuvidoria(false);
				getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
				getAtendimentoVO().setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.SISTEMA);
				setListaConsulta(getFacadeFactory().getAtendimentoFacade().consultarPorCodigoPessoa(visao.getMatricula().getAluno().getCodigo(), getAtendimentoVO().getTipoAtendimentoEnum(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getAtendimentoVO().getIdEntidade(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaAlunoForm");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TitulacaoCurso</code> para edição pelo usuário da aplicação.
	 */
	public String novoOuvidoriaAluno() {
		try {
			VisaoAlunoControle visao = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visao != null) {
				setAtendimentoVO(new AtendimentoVO());
				getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
				getAtendimentoVO().setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.SISTEMA);
				getAtendimentoVO().setCPF(visao.getMatricula().getAluno().getCPF());
				getAtendimentoVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
				getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
				realizarConsultarPessoaExistePorCPF();
				setControleConsulta(new ControleConsulta());
				setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
				setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
				setArquivoVO(new ArquivoVO());
				setLerOuvidoria(false);
				setNovoOuvidoria(true);
				setApresentarTextoExplicativoOuvidoria(false);
				setApresentarUsuarioParaSolicitante(false);
				inicializarListasSelectItemTodosComboBox();
			}
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
	}

	public void realizarVisualizacaoOuvidoria() {
		try {
			AtendimentoVO obj = (AtendimentoVO) context().getExternalContext().getRequestMap().get("ouvidoriaItens");
			obj.setNovoObj(new Boolean(false));
			setAtendimentoVO(getFacadeFactory().getAtendimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, obj.getIdEntidade(), getUsuarioLogado()));
			getAtendimentoVO().setAtendimentoJaVisualizado(true);
			getFacadeFactory().getAtendimentoFacade().realizarAtualizacaoAtendimentoJaVisualizado(getAtendimentoVO(), getAtendimentoVO().getIdEntidade(), getUsuarioLogado());
			inicializarListasSelectItemTodosComboBox();
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			setLerOuvidoria(true);
			setNovoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			setApresentarTextoExplicativoOuvidoria(false);
			LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
			login.setQtdeAtualizacaoOuvidoria(null);
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	// Codigo utilizado na visao do Usuario Sem Vinculo
	public String consultarOuvidoriaVisaoUsuarioSemVinculoValidandoConfiguracaoAtendimento() {
		try {
			if(getUnidadeEnsinoLogado().getCodigo() != 0){
				setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			consultarOuvidoriaVisaoUsuarioSemVinculo();
			setApresentarTextoExplicativoOuvidoria(true);
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaCons");
	}
	
	public Boolean getApresentarConsultar() {
		if (getUsuarioLogado().getCodigo() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void limparDadosUsuario() {
		getUsuario().setUsername("");
		getUsuario().setSenha("");
	}

	public String consultarOuvidoriaVisaoUsuarioSemVinculo() {
		try {
			setAtendimentoVO(new AtendimentoVO());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			setLerOuvidoria(false);
			setNovoOuvidoria(false);
			setApresentarTextoExplicativoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			setListaConsulta(getFacadeFactory().getAtendimentoFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getAtendimentoVO().getTipoAtendimentoEnum(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getAtendimentoVO().getIdEntidade(), getUsuarioLogado()));
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaCons");
	}

	public void novoOuvidoriaVisaoUsuarioSemVinculo() {
		try {
			setAtendimentoVO(new AtendimentoVO());
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			getAtendimentoVO().setUnidadeEnsino(getCodigoUnidadeEnsino());
			getAtendimentoVO().setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.SISTEMA);
			if (getCodigoUnidadeEnsino() > 0) {
				getFacadeFactory().getAtendimentoFacade().realizarValidacoesParaBuscarResponsavelAtendimento(getAtendimentoVO(), getAtendimentoVO().getUnidadeEnsino(), false, getAtendimentoVO().getTipoAtendimentoEnum(), getAtendimentoVO().getIdEntidade(), null);
				setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
//			setConfiguracaoGeralSistemaOuvidoriaExterna(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(getAtendimentoVO().getUnidadeEnsino(), false, 10, getUsuario()));
//			if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
//				throw new Exception("Não é possivel registrar uma ouvidoria por meio do sistema para a unidade de ensino escolhida.");
//			}
			setControleConsulta(new ControleConsulta());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			if(getUsuarioLogado().getCodigo() > 0){
				getAtendimentoVO().setCPF(getUsuarioLogado().getPessoa().getCPF());		
				realizarConsultarPessoaExistePorCPF();
				setLerOuvidoria(false);
				setNovoOuvidoria(true);
				setApresentarTextoExplicativoOuvidoria(false);
				setApresentarUsuarioParaSolicitante(false);
			}
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
				setMensagemDetalhada("msg_erro", "Não é possivel registrar uma ouvidoria por meio do sistema para a unidade de ensino escolhida.", Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void novoTelaInicialVisaoOuvidoria() {
		try {
			setAtendimentoVO(new AtendimentoVO());
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			getAtendimentoVO().setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.SISTEMA);
			getAtendimentoVO().setCPF(getUsuarioLogado().getPessoa().getCPF());
			getAtendimentoVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
			setCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
			getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			realizarConsultarPessoaExistePorCPF();
			setControleConsulta(new ControleConsulta());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			setLerOuvidoria(false);
			setNovoOuvidoria(true);
			setApresentarTextoExplicativoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			inicializarListasSelectItemTodosComboBox();
			montarListaSelectItemUnidadeEnsino("");
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRegistoManifestacao() {
		try {
			if (getCodigoUnidadeEnsino().intValue() == 0) {
				throw new Exception("O campo Unidade Ensino (Ouvidoria) deve ser informado.");
			}
			setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getCodigoUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setApresentarUsuarioParaSolicitante(false);
			setLerOuvidoria(true);
			setNovoOuvidoria(false);
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
				setMensagemDetalhada("msg_erro", "Não é possivel registrar uma ouvidoria por meio do sistema para a unidade de ensino escolhida.", Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}


	
	// Codigo utilizado na visao do professor
	public String consultarOuvidoriaVisaoProfessorValidandoConfiguracaoAtendimento() {
		try {
			realizarValidacaoUnidadeEnsinoInformadoVisaoProfessor();
			montarListaSelectItemUnidadeEnsino("");
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaProfessorForm");
	}

	public void realizarValidacaoUnidadeEnsinoInformadoVisaoProfessor() {
		try {
			setLerOuvidoria(false);
			setNovoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			setConfiguracaoAtendimentoVO(new ConfiguracaoAtendimentoVO());
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() && getCodigoUnidadeEnsino() == 0) {
				return;
			}
			setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getCodigoUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
				throw new Exception("Não é possivel visualizar a ouvidoria por meio do sistema para a unidade de ensino escolhida.");
			}
			consultarOuvidoriaVisaoProfessor();
			setApresentarTextoExplicativoOuvidoria(true);
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setCodigoUnidadeEnsino(0);
			if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
				setMensagemDetalhada("msg_erro", "Não é possivel visualizar a ouvidoria por meio do sistema para a unidade de ensino escolhida.", Uteis.ERRO);
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public String consultarOuvidoriaVisaoProfessor() {
		try {
			setAtendimentoVO(new AtendimentoVO());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			setLerOuvidoria(false);
			setNovoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			setApresentarTextoExplicativoOuvidoria(true);
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			setListaConsulta(getFacadeFactory().getAtendimentoFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getAtendimentoVO().getTipoAtendimentoEnum(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getAtendimentoVO().getIdEntidade(), getUsuarioLogado()));
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaProfessorForm");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TitulacaoCurso</code> para edição pelo usuário da aplicação.
	 */
	public String novoOuvidoriaProfessor() {
		try {
			setAtendimentoVO(new AtendimentoVO());
			getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
			getAtendimentoVO().setTipoOrigemOuvidoriaEnum(TipoOrigemOuvidoriaEnum.SISTEMA);
			getAtendimentoVO().setCPF(getUsuarioLogado().getPessoa().getCPF());
			getAtendimentoVO().setUnidadeEnsino(getCodigoUnidadeEnsino());
			getAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			realizarConsultarPessoaExistePorCPF();
			setControleConsulta(new ControleConsulta());
			setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
			setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
			setArquivoVO(new ArquivoVO());
			setLerOuvidoria(false);
			setNovoOuvidoria(true);
			setApresentarTextoExplicativoOuvidoria(false);
			setApresentarUsuarioParaSolicitante(false);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaForm");
	}
	
	// Codigo utilizado na visao do Coordenador
		public String consultarOuvidoriaVisaoCoordenadorValidandoConfiguracaoAtendimento() {
			try {
				realizarValidacaoUnidadeEnsinoInformadoVisaoCoordenador();
				montarListaSelectItemUnidadeEnsino("");
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaCoordenadorForm");
		}

		public void realizarValidacaoUnidadeEnsinoInformadoVisaoCoordenador() {
			try {
				setLerOuvidoria(false);
				setNovoOuvidoria(false);
				setApresentarUsuarioParaSolicitante(false);
				setConfiguracaoAtendimentoVO(new ConfiguracaoAtendimentoVO());
				setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				//if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
				//	throw new Exception("Não é possivel visualizar a ouvidoria por meio do sistema para a unidade de ensino escolhida.");
				//}
				consultarOuvidoriaVisaoProfessor();
				setApresentarTextoExplicativoOuvidoria(true);
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			} catch (Exception e) {
				setCodigoUnidadeEnsino(0);
				if (getConfiguracaoAtendimentoVO().getCodigo().intValue() == 0) {
					setMensagemDetalhada("msg_erro", "Não é possivel visualizar a ouvidoria por meio do sistema para a unidade de ensino escolhida.", Uteis.ERRO);
				} else {
					setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				}
			}
		}

		public String consultarOuvidoriaVisaoCoordenador() {
			try {
				setAtendimentoVO(new AtendimentoVO());
				setAtendimentoInteracaoSolicitanteVO(new AtendimentoInteracaoSolicitanteVO());
				setAtendimentoInteracaoDepartamentoVO(new AtendimentoInteracaoDepartamentoVO());
				setArquivoVO(new ArquivoVO());
				setLerOuvidoria(false);
				setNovoOuvidoria(false);
				setApresentarUsuarioParaSolicitante(false);
				setApresentarTextoExplicativoOuvidoria(true);
				getAtendimentoVO().setTipoAtendimentoEnum(TipoAtendimentoEnum.OUVIDORIA);
				setListaConsulta(getFacadeFactory().getAtendimentoFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getAtendimentoVO().getTipoAtendimentoEnum(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getAtendimentoVO().getIdEntidade(), getUsuarioLogado()));
				setMensagemID("msg_dados_editar", Uteis.ALERTA);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("ouvidoriaCoordenadorForm");
		}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo
	 * <code>nome</code> Este atributo é uma lista ( <code>List</code>)
	 * utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		atendimentoVO = null;
	}

	public AtendimentoVO getAtendimentoVO() {
		if (atendimentoVO == null) {
			atendimentoVO = new AtendimentoVO();
		}
		return atendimentoVO;
	}

	public void setAtendimentoVO(AtendimentoVO ouvidoriaVO) {
		this.atendimentoVO = ouvidoriaVO;
	}

	public List<SelectItem> getListaSelectItemMatriculaAluno() {
		if (listaSelectItemMatriculaAluno == null) {
			listaSelectItemMatriculaAluno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMatriculaAluno;
	}

	public void setListaSelectItemMatriculaAluno(List<SelectItem> listaSelectItemMatriculaAluno) {
		this.listaSelectItemMatriculaAluno = listaSelectItemMatriculaAluno;
	}

	public List<SelectItem> getListaSelectItemtipagemOuvidoriaVO() {
		if (listaSelectItemtipagemOuvidoriaVO == null) {
			listaSelectItemtipagemOuvidoriaVO = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemtipagemOuvidoriaVO;
	}

	public void setListaSelectItemtipagemOuvidoriaVO(List<SelectItem> listaSelectItemtipagemOuvidoriaVO) {
		this.listaSelectItemtipagemOuvidoriaVO = listaSelectItemtipagemOuvidoriaVO;
	}

	public Boolean getApresentarFiltroPorSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("SITUACAO")) {
			return true;
		}
		return false;
	}

	public SituacaoAtendimentoEnum getSituacaoOuvidoriaEnum() {
		if (situacaoOuvidoriaEnum == null) {
			situacaoOuvidoriaEnum = SituacaoAtendimentoEnum.EM_ANALISE_OUVIDOR;
		}
		return situacaoOuvidoriaEnum;
	}

	public void setSituacaoOuvidoriaEnum(SituacaoAtendimentoEnum situacaoOuvidoriaEnum) {
		this.situacaoOuvidoriaEnum = situacaoOuvidoriaEnum;
	}

	public AtendimentoInteracaoSolicitanteVO getAtendimentoInteracaoSolicitanteVO() {
		if (atendimentoInteracaoSolicitanteVO == null) {
			atendimentoInteracaoSolicitanteVO = new AtendimentoInteracaoSolicitanteVO();
		}
		return atendimentoInteracaoSolicitanteVO;
	}

	public void setAtendimentoInteracaoSolicitanteVO(AtendimentoInteracaoSolicitanteVO atendimentoInteracaoSolicitanteVO) {
		this.atendimentoInteracaoSolicitanteVO = atendimentoInteracaoSolicitanteVO;
	}

	public AtendimentoInteracaoDepartamentoVO getAtendimentoInteracaoDepartamentoVO() {
		if (atendimentoInteracaoDepartamentoVO == null) {
			atendimentoInteracaoDepartamentoVO = new AtendimentoInteracaoDepartamentoVO();
		}
		return atendimentoInteracaoDepartamentoVO;
	}

	public void setAtendimentoInteracaoDepartamentoVO(AtendimentoInteracaoDepartamentoVO atendimentoInteracaoDepartamentoVO) {
		this.atendimentoInteracaoDepartamentoVO = atendimentoInteracaoDepartamentoVO;
	}

	public Boolean getNovoOuvidoria() {
		if (novoOuvidoria == null) {
			novoOuvidoria = false;
		}
		return novoOuvidoria;
	}

	public void setNovoOuvidoria(Boolean novoOuvidoria) {
		this.novoOuvidoria = novoOuvidoria;
	}

	public Boolean getLerOuvidoria() {
		if (lerOuvidoria == null) {
			lerOuvidoria = false;
		}
		return lerOuvidoria;
	}

	public void setLerOuvidoria(Boolean lerOuvidoria) {
		this.lerOuvidoria = lerOuvidoria;
	}

	public List<SelectItem> getListaSelectItemDepartameto() {
		if (listaSelectItemDepartameto == null) {
			listaSelectItemDepartameto = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDepartameto;
	}

	public void setListaSelectItemDepartameto(List<SelectItem> listaSelectItemDepartameto) {
		this.listaSelectItemDepartameto = listaSelectItemDepartameto;
	}

	public List<SelectItem> getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemFuncionario;
	}

	public void setListaSelectItemFuncionario(List<SelectItem> listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
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

	public ConfiguracaoAtendimentoVO getConfiguracaoAtendimentoVO() {
		if (configuracaoAtendimentoVO == null) {
			configuracaoAtendimentoVO = new ConfiguracaoAtendimentoVO();
		}
		return configuracaoAtendimentoVO;
	}

	public void setConfiguracaoAtendimentoVO(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO) {
		this.configuracaoAtendimentoVO = configuracaoAtendimentoVO;
	}

	public Boolean getIsUsuarioLogadoAndOuvidor() {
		if (getUsuarioLogado().getPessoa().getCodigo().equals(getAtendimentoVO().getResponsavelAtendimento().getPessoa().getCodigo())) {
			return true;
		}
		return false;
	}

	public Boolean getIsUsuarioLogadoAndFuncionarioDepartamento() {
		if (getUsuarioLogado().getPessoa().getCodigo().equals(getAtendimentoVO().getObterUltimoAtendimentoInteracaoDepartamentoVO().getFuncionarioVO().getPessoa().getCodigo())) {
			return true;
		}
		return false;
	}

	public Boolean getNaoPermitirAlteracaoCampo() {
		if (getAtendimentoVO().getNovoObj() || getIsUsuarioLogadoAndOuvidor()) {
			return true;
		}
		return false;

	}

	public String getCss_LeituraOrObrigatorio() {
		if (getAtendimentoVO().getNovoObj() || getIsUsuarioLogadoAndOuvidor()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}

	public String getCss_LeituraOrNormal() {
		if (getAtendimentoVO().getNovoObj() || getIsUsuarioLogadoAndOuvidor()) {
			return "campos";
		}
		return "camposSomenteLeitura";
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() throws Exception {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Boolean getApresentarUsuarioParaSolicitante() {
		if (apresentarUsuarioParaSolicitante == null) {
			apresentarUsuarioParaSolicitante = false;
		}
		return apresentarUsuarioParaSolicitante;
	}

	public void setApresentarUsuarioParaSolicitante(Boolean existeUsuarioParaSolicitante) {
		this.apresentarUsuarioParaSolicitante = existeUsuarioParaSolicitante;
	}

	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	public Boolean getApresentarTextoExplicativoOuvidoria() {
		if (apresentarTextoExplicativoOuvidoria == null) {
			apresentarTextoExplicativoOuvidoria = false;
		}
		return apresentarTextoExplicativoOuvidoria;
	}

	public void setApresentarTextoExplicativoOuvidoria(Boolean apresentarTextoExplicativoOuvidoria) {
		this.apresentarTextoExplicativoOuvidoria = apresentarTextoExplicativoOuvidoria;
	}

	public Boolean getApresentarCampoMatricula() {
		if (getListaSelectItemMatriculaAluno().size() == 1) {
			return false;
		}
		return true;
	}

	public Boolean getExisteUnidadeEnsinoInformada() {
		if (getCodigoUnidadeEnsino().intValue() == 0) {
			return false;
		}
		return true;
	}

	public String getBom() {
		if (bom == null) {
			bom = "";
		}
		return bom;
	}

	public void setBom(String bom) {
		this.bom = bom;
	}

	public String getOtimo() {
		if (otimo == null) {
			otimo = "";
		}
		return otimo;
	}

	public void setOtimo(String otimo) {
		this.otimo = otimo;
	}

	public String getRegular() {
		if (regular == null) {
			regular = "";
		}
		return regular;
	}

	public void setRegular(String regular) {
		this.regular = regular;
	}

	public String getRuim() {
		if (ruim == null) {
			ruim = "";
		}
		return ruim;
	}

	public void setRuim(String ruim) {
		this.ruim = ruim;
	}
	
	

	public Boolean getManterRichModalAvaliacaoAberto() {
		if (manterRichModalAvaliacaoAberto == null) {
			manterRichModalAvaliacaoAberto = false;
		}
		return manterRichModalAvaliacaoAberto;
	}
	public String  getFecharRichModalAvaliacao() {
		if (getManterRichModalAvaliacaoAberto()) {
			return "";
		}
		return "PF('panelAvaliacaoAtendimento').hide()";
	}

	public void setManterRichModalAvaliacaoAberto(Boolean manterRichModalAvaliacaoAberto) {
		this.manterRichModalAvaliacaoAberto = manterRichModalAvaliacaoAberto;
	}

	public Boolean getApresentarCampoMotivoAvaliacao() {
		return !getRegular().equals("") || !getRuim().equals("");
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public Integer getCodigoResponsavelAtendimento() {
		if(codigoResponsavelAtendimento == null){
			codigoResponsavelAtendimento = 0;
		}
		return codigoResponsavelAtendimento;
	}

	public void setCodigoResponsavelAtendimento(Integer codigoResponsavelAtendimento) {
		this.codigoResponsavelAtendimento = codigoResponsavelAtendimento;
	}

	public String getNomeSolicitante() {
		if(nomeSolicitante == null){
			nomeSolicitante = "";
		}
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}

	public String getCPFSolicitante() {
		if(CPFSolicitante == null){
			CPFSolicitante = "";
		}
		return CPFSolicitante;
	}

	public void setCPFSolicitante(String cPFSolicitante) {
		CPFSolicitante = cPFSolicitante;
	}

	public String getEmailSolicitante() {
		if(emailSolicitante == null){
			emailSolicitante = "";
		}
		return emailSolicitante;
	}

	public void setEmailSolicitante(String emailSolicitante) {
		this.emailSolicitante = emailSolicitante;
	}

	

	public Integer getCodigoTipoAtendimento() {
		if(codigoTipoAtendimento == null){
			codigoTipoAtendimento = 0;
		}
		return codigoTipoAtendimento;
	}

	public void setCodigoTipoAtendimento(Integer codigoTipoAtendimento) {
		this.codigoTipoAtendimento = codigoTipoAtendimento;
	}

	public List<SelectItem> getListaSelectItemResponsavelAtendimento() {
		if(listaSelectItemResponsavelAtendimento == null){
			listaSelectItemResponsavelAtendimento = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemResponsavelAtendimento;
	}

	public void setListaSelectItemResponsavelAtendimento(List<SelectItem> listaSelectItemResponsavelAtendimento) {
		this.listaSelectItemResponsavelAtendimento = listaSelectItemResponsavelAtendimento;
	}

	public Integer getTotalOuvidoriaAtrasada() {
		if(totalOuvidoriaAtrasada == null){
			totalOuvidoriaAtrasada = 0;
		}
		return totalOuvidoriaAtrasada;
	}

	public void setTotalOuvidoriaAtrasada(Integer totalOuvidoriaAtrasada) {
		
		this.totalOuvidoriaAtrasada = totalOuvidoriaAtrasada;
	}

	public Integer getTotalOuvidoriaNova() {
		if(totalOuvidoriaNova == null){
			totalOuvidoriaNova = 0;
		}
		return totalOuvidoriaNova;
	}

	public void setTotalOuvidoriaNova(Integer totalOuvidoriaNova) {		
		this.totalOuvidoriaNova = totalOuvidoriaNova;
	}

	public Integer getTotalOuvidoriaEmAnaliseOuvidor() {
		if(totalOuvidoriaEmAnaliseOuvidor == null){
			totalOuvidoriaEmAnaliseOuvidor = 0;
		}
		return totalOuvidoriaEmAnaliseOuvidor;
	}

	public void setTotalOuvidoriaEmAnaliseOuvidor(Integer totalOuvidoriaEmAnaliseOuvidor) {		
		this.totalOuvidoriaEmAnaliseOuvidor = totalOuvidoriaEmAnaliseOuvidor;
	}

	public Integer getTotalOuvidoriaFinalizada() {
		if(totalOuvidoriaFinalizada == null){
			totalOuvidoriaFinalizada = 0;
		}
		return totalOuvidoriaFinalizada;
	}

	public void setTotalOuvidoriaFinalizada(Integer totalOuvidoriaFinalizada) {
		this.totalOuvidoriaFinalizada = totalOuvidoriaFinalizada;
	}

	public Integer getTotalOuvidoriaAguardandoDepartamento() {
		if(totalOuvidoriaAguardandoDepartamento == null){
			totalOuvidoriaAguardandoDepartamento = 0;
		}
		return totalOuvidoriaAguardandoDepartamento;
	}

	public void setTotalOuvidoriaAguardandoDepartamento(Integer totalOuvidoriaAguardandoDepartamento) {
		this.totalOuvidoriaAguardandoDepartamento = totalOuvidoriaAguardandoDepartamento;
	}

	public Integer getTotalOuvidoriaAguardandoSolicitante() {
		if(totalOuvidoriaAguardandoSolicitante == null){
			totalOuvidoriaAguardandoSolicitante = 0;
		}
		return totalOuvidoriaAguardandoSolicitante;
	}

	public void setTotalOuvidoriaAguardandoSolicitante(Integer totalOuvidoriaAguardandoSolicitante) {
		this.totalOuvidoriaAguardandoSolicitante = totalOuvidoriaAguardandoSolicitante;
	}

	public String getQuadroSituacao() {
		if(quadroSituacao == null){
			quadroSituacao = "";
		}
		return quadroSituacao;
	}

	public void setQuadroSituacao(String quadroSituacao) {
		this.quadroSituacao = quadroSituacao;
	}

	public Boolean getHabilitarEscolharOuvidor() {
		if(habilitarEscolharOuvidor == null){
			habilitarEscolharOuvidor = false;
		}
		return habilitarEscolharOuvidor;
	}

	public void setHabilitarEscolharOuvidor(Boolean habilitarEscolharOuvidor) {
		this.habilitarEscolharOuvidor = habilitarEscolharOuvidor;
	}

	public Integer getCodigoRespostaAtendimentoInteracaoSolicitanteVO() {
		if (codigoRespostaAtendimentoInteracaoSolicitanteVO == null) {
			codigoRespostaAtendimentoInteracaoSolicitanteVO = 0;
		}
		return codigoRespostaAtendimentoInteracaoSolicitanteVO;
	}

	public void setCodigoRespostaAtendimentoInteracaoSolicitanteVO(
			Integer codigoRespostaAtendimentoInteracaoSolicitanteVO) {
		this.codigoRespostaAtendimentoInteracaoSolicitanteVO = codigoRespostaAtendimentoInteracaoSolicitanteVO;
	}

	public Boolean getApresentarUsuarioDiretorMultiCampus() {
		if (apresentarUsuarioDiretorMultiCampus == null) {
			apresentarUsuarioDiretorMultiCampus = false;
		}
		return apresentarUsuarioDiretorMultiCampus;
	}

	public void setApresentarUsuarioDiretorMultiCampus(Boolean apresentarUsuarioDiretorMultiCampus) {
		this.apresentarUsuarioDiretorMultiCampus = apresentarUsuarioDiretorMultiCampus;
	}

	public String getConsultarPessoa() {
		if (consultarPessoa == null) {
			consultarPessoa = "CODIGO";
		}
		return consultarPessoa;
	}
	
	public void setConsultarPessoa(String consultarPessoa) {
		this.consultarPessoa = consultarPessoa;
	}
	
	public List<SelectItem> getListaTipoPessoa() {
		List<SelectItem> item = new ArrayList<SelectItem>(0);
		item.add(new SelectItem("CPF", "CPF"));
		item.add(new SelectItem("NOME", "Nome"));
		return item;
	}
	
	public String getCamposConsulta() {
		if (camposConsulta == null) {
			camposConsulta = "";
		}
		return camposConsulta;
	}
	
	public void setCamposConsulta(String camposConsulta) {
		this.camposConsulta = camposConsulta;
	}
	
	public List<PessoaVO> getListaPessoa() {
		if (listaPessoa == null) {
			listaPessoa = new ArrayList<>(0);
		}
		return listaPessoa;
	}
	
	public void setListaPessoa(List<PessoaVO> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}
	
	public void realizarConsultaPessoa() {
		try {
			if (getConsultarPessoa().equals("CPF")) {
				setListaPessoa(getFacadeFactory().getPessoaFacade().consultarPorCPF(getCamposConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getConsultarPessoa().equals("NOME")) {
				setListaPessoa(getFacadeFactory().getPessoaFacade().consultarPorNome(getCamposConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarPessoa() throws Exception {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
		getAtendimentoVO().setCPF(obj.getCPF());
		getAtendimentoVO().setNome(obj.getNome());
		getAtendimentoVO().setTelefone(obj.getTelefoneRes());
		getAtendimentoVO().setEmail(obj.getEmail());
		getAtendimentoVO().setPessoaVO(obj);
		montarListaSelectItemMatriculaAluno();
		setApresentarUsuarioParaSolicitante(!getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoaSeUsuarioExiste(getAtendimentoVO().getPessoaVO().getCodigo(), false, null));
		if (getApresentarUsuarioParaSolicitante()) {
			getAtendimentoVO().setUrlAplicacao(UteisJSF.getAcessoAplicadoURL());
			getAtendimentoVO().setUsername(Uteis.retirarMascaraCPF(getAtendimentoVO().getCPF()));
			if (getUsuarioLogado() != null) {
				getAtendimentoVO().setSenha(Uteis.retirarMascaraCPF(getAtendimentoVO().getCPF()));
			}
		}else {
			setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorPessoa(getAtendimentoVO().getPessoaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			getAtendimentoVO().setUsername(getUsuario().getUsername());
			getAtendimentoVO().setSenha(getUsuario().getSenha());
		}
	}
	
	public void limparPessoa() {
		getAtendimentoVO().setCPF(new AtendimentoVO().getCPF());
		getAtendimentoVO().setEmail(new AtendimentoVO().getEmail());
		getAtendimentoVO().setTelefone(new AtendimentoVO().getTelefone());
		getAtendimentoVO().setNome(new AtendimentoVO().getNome());
		getAtendimentoVO().setPessoaVO(new PessoaVO());
	}
	
	public void limparCampoPessoa() {
		setCamposConsulta("");
		setListaPessoa(new ArrayList<>(0));
	}
	
	public String logoutOuvidoria() {
		try {
			getLoginControle().logout();
			novo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaForm.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("homeOuvidoriaForm.xhtml");
	}

}
