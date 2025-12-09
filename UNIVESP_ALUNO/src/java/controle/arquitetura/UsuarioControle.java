/**
 * CLASSE ALTERADA MANUALMENTE PARA:
 *   - Adicionar o atributo pessoa a entidade Usuário. Necessário para manter um vínculo entre usuário a pessoa real que está
 *     acessando o sistema.
 */
package controle.arquitetura;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas usuarioForm.jsp usuarioCons.jsp) com as funcionalidades da classe <code>Usuario</code>. Implemtação da camada
 * controle (Backing Bean).
 *
 * @see SuperControle
 * @see Usuario
 * @see UsuarioVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;
import jakarta.servlet.http.HttpSession;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SimularAcessoAlunoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import static negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAdministrativoEnum.USUARIO_PERMITIR_ALTERAR_PESSOA_USUARIO;

import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("UsuarioControle")
@Scope("viewScope")
@Lazy
public class UsuarioControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private UsuarioVO usuarioVO;
    protected List<SelectItem> listaSelectItemCodPerfilAcesso;
    private UsuarioPerfilAcessoVO usuarioPerfilAcessoVO;
    protected List<SelectItem> listaSelectItemUnidadeEnsino;
    private String pessoa_Erro;
    private Boolean nomeReadonly;
    private List listaConsultaPessoa;
    private String campoConsultaPessoa;
    private String valorConsultaPessoa;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private Boolean permitirAlterarUsername;
    private String valorConsultaTipoUsuario;
    private boolean visualizarBotoesVisao;
    private List<UsuarioVO> listaUsuario;

    public UsuarioControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }
   
	@PostConstruct
	public void inicializarUsuarioPorFuncionario() {
		try {
			HttpSession session = (HttpSession) context().getExternalContext().getSession(true);
			FuncionarioVO funcionarioVO = (FuncionarioVO) session.getAttribute("funcionarioItem");
			if (funcionarioVO != null && !funcionarioVO.getCodigo().equals(0)) {
				novo();
				List<UsuarioVO> usuarioVOs = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(funcionarioVO.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(usuarioVOs)) {
					setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
					setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(usuarioVOs.get(0).getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					setMensagemID("msg_dados_editar");
				} else {
					getUsuarioVO().setPessoa(funcionarioVO.getPessoa());
					getUsuarioVO().setNome(funcionarioVO.getPessoa().getNome());
					getUsuarioVO().setTipoUsuario(TipoPessoa.FUNCIONARIO.getValor());
					if (funcionarioVO.getPessoa().getFuncionario()) {
						obterUnidadesEnsino(funcionarioVO.getPessoa());
					}
				}
			}
			session.removeAttribute("funcionarioItem");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void inicializarUsuarioAutomatico(FuncionarioVO funcionario, String tipoUsuario) throws Exception {
        novo();
        List<UsuarioVO> users = getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(funcionario.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        if (users == null || users.size() == 0) {
            PessoaVO obj = funcionario.getPessoa();
            getUsuarioVO().setPessoa(obj);
            getUsuarioVO().setNome(obj.getNome());
            getUsuarioVO().setTipoUsuario(tipoUsuario);
            if (obj.getFuncionario()) {
                obterUnidadesEnsino(obj);
            }
        } else {
            throw new ConsistirException("O  USUÁRIO já esta cadastrado.");
        }

    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Usuario</code> para edição pelo usuário da aplicação.
     */
    public String novo() {         //removerObjetoMemoria(this);
        setUsuarioVO(new UsuarioVO());
        setUsuarioPerfilAcessoVO(new UsuarioPerfilAcessoVO());
        inicializarListasSelectItemTodosComboBox();
        setNomeReadonly(Boolean.FALSE);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("usuarioForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Usuario</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP
     * correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItem");
        try {
            setPermitirAlterarUsername(getConfiguracaoGeralPadraoSistema().getNaoPermitirAlterarUsernameUsuario());
        } catch (Exception e) {
        }
        obj = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setUsuarioVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setUsuarioPerfilAcessoVO(new UsuarioPerfilAcessoVO());
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("usuarioForm");
    }

    public void mudaValorCampoSenha() {
        getUsuarioVO().setValidaAlteracaoSenha(true);
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Usuario</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma
     * mensagem de erro.
     */
    public void gravar() {
        try {
            
            if(getUsuarioVO().getAlterarSenha()){
            	Uteis.validarSenha(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null,null), getUsuarioVO().getSenha());
            }      
            
            if (!getUsuarioVO().getTipoUsuario().equals("DM") && !getUsuarioVO().getTipoUsuario().equals("PA")) {
            	if(!getUsuarioVO().getApresentarListaPerfilAcesso()){
            		getUsuarioVO().getUsuarioPerfilAcessoVOs().clear();
            	}
            } else {
//                Iterator i = getUsuarioVO().getUsuarioPerfilAcessoVOs().iterator();
//                int passo = 0;
//                while (i.hasNext()) {
//                    UsuarioPerfilAcessoVO objExistente = (UsuarioPerfilAcessoVO) i.next();
//                    if (objExistente.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
//                        passo = 0;
//                        break;
//                    }
//                    passo = 1;
//                }
//                if (passo == 1) {
//                    throw new ConsistirException("Por Favor Cadastre um Perfil Acesso sem uma Unidade de Ensino.");
//                }           
                
				Uteis.checkState(!getUsuarioVO().getUsuarioPerfilAcessoVOs().stream().anyMatch(uperfilAcesso -> !Uteis.isAtributoPreenchido(uperfilAcesso.getUnidadeEnsinoVO().getCodigo())),
						"Por Favor Cadastre um Perfil Acesso sem uma Unidade de Ensino.");
            }
            String senha = "";
            if (usuarioVO.isNovoObj().booleanValue()) {
            	senha =  usuarioVO.getSenha();
                getFacadeFactory().getUsuarioFacade().incluir(usuarioVO, getUsuarioLogado());
                usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(usuarioVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                ConfiguracaoLdapVO conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(usuarioVO.getPessoa().getCodigo());
                if(Uteis.isAtributoPreenchido(conf)) {
                    PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(usuarioVO.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                    if(!Uteis.isAtributoPreenchido(emailInstitucional)) {
                        getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(conf, usuarioVO, senha, null ,emailInstitucional,getUsuarioLogadoClone());
                    }
                    getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(conf, usuarioVO, senha, null ,emailInstitucional,getUsuarioLogadoClone());
                }else if(usuarioVO.getTipoUsuario().equals("FU")) {
                	FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
                 	getFacadeFactory().getFuncionarioFacade().realizarVerificacaoUsuarioRegistradoLdap(fun, usuarioVO, senha, getUsuarioLogadoClone());
                }
            } else {
            	ConfiguracaoLdapVO conf = null;
        		if (usuarioVO.getAlterarSenha()) {
        			senha =  usuarioVO.getSenha();
        		}
                getFacadeFactory().getUsuarioFacade().alterar(usuarioVO, getUsuarioLogado(), USUARIO_PERMITIR_ALTERAR_PESSOA_USUARIO);
                if(usuarioVO.getAlterarSenha() ) {
                    conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(usuarioVO.getPessoa().getCodigo());
                    if(Uteis.isAtributoPreenchido(conf) && getFacadeFactory().getLdapFacade().consultarSeUsuarioExisteLdap(conf, usuarioVO)) {
                        getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(conf, usuarioVO, senha);
                    }else if(Uteis.isAtributoPreenchido(conf) && !getFacadeFactory().getLdapFacade().consultarSeUsuarioExisteLdap(conf, usuarioVO)) {
                        PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(usuarioVO.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                        if(!Uteis.isAtributoPreenchido(emailInstitucional)){
                            emailInstitucional =  getFacadeFactory().getPessoaEmailInstitucionalFacade().incluirPessoaEmailInstitucional(conf, null , false,  usuarioVO,getUsuarioLogadoClone());
                        }
                        getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(conf, usuarioVO, senha, null ,emailInstitucional,getUsuarioLogadoClone());
                    }else if(usuarioVO.getTipoUsuario().equals("FU")) {
                        FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(usuarioVO.getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
                        getFacadeFactory().getFuncionarioFacade().realizarVerificacaoUsuarioRegistradoLdap(fun, usuarioVO, senha, getUsuarioLogadoClone());
                    }
                }
            }
            setMensagemID("msg_dados_gravados");
        } catch (ConsistirException  e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception  e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
     public void anularDataModelo(){
        setControleConsultaOtimizado(null);
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener() throws Exception {
     
        consultar();
    
    }
    
    public void scrollerListenerEspecificoUsuarioAlunoProfessorCoordenador() throws Exception {
       
        consultarEspecificoSimulacaoUsuario();
    
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP UsuarioCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
     * JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            getControleConsultaOtimizado().getListaConsulta().clear();
            getControleConsultaOtimizado().setLimitePorPagina(10);
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getUsuarioFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultarPorNome(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeGegistroPorNome(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            if (getControleConsulta().getCampoConsulta().equals("username")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultarPorUsername(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
                        true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeRegistroPorUsername(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePerfilAcesso")) {
                if (getControleConsulta().getValorConsulta().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultarPorNomePerfilAcesso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
               getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeRegistroPorNomePerfilAcesso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoUsuario")) {
                if (getValorConsultaTipoUsuario().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getUsuarioFacade().consultarPorTipoUsuario(getValorConsultaTipoUsuario(), getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
               getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeRegistroPorTipoUsuario(getValorConsultaTipoUsuario(), getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }           
			 if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				 objs = getFacadeFactory().getUsuarioFacade().consultarPorRegistroAcademico(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),	 Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado().getLimitePorPagina(),
                        getControleConsultaOtimizado().getOffset() ,  getControleConsultaOtimizado(), getUsuarioLogado());
				
			}
            
            
            getControleConsultaOtimizado().setListaConsulta(objs);
            
            listaUsuario =(List<UsuarioVO>)objs;
            listaUsuario = listaUsuario.stream().filter(u -> u.getPessoa().getAluno() || u.getPessoa().getProfessor() || u.getPessoa().getCoordenador()).collect(Collectors.toList());
            
          
            setMensagemID("msg_dados_consultados");
//            return "consultar";
            return "";
        } catch (Exception e) {
            getControleConsultaOtimizado().getListaConsulta().clear();
                     setMensagemDetalhada("msg_erro", e.getMessage());
//            return "consultar";
            return "";
        }
    }
     
	public String consultarEspecificoSimulacaoUsuario() {
		try {
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (getControleConsulta().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			if (getControleConsulta().getCampoConsultaEspecificoSimulacao().equals("nome")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarPorNomeTipoEspecificoUsuarioAlunoProfessorCoordenador(
						getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsultaEspecificoSimulacao().equals("username")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarPorUsernameTipoEspecificoUsuarioAlunoProfessorCoordenador(
						getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado(), getUsuarioLogado()));
			}
			else if (getControleConsulta().getCampoConsultaEspecificoSimulacao().equals("registroAcademico")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarPorRegistroAcademicoTipoEspecificoUsuarioAlunoProfessorCoordenador(
						getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(),
						getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(),
						true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleConsultaOtimizado(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
    
    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>UsuarioVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getUsuarioFacade().excluir(usuarioVO, getUsuarioLogado());
            setUsuarioVO(new UsuarioVO());
            setUsuarioPerfilAcessoVO(new UsuarioPerfilAcessoVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarPessoa() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (!getUsuarioVO().getPessoaConsulta().equals("")) {
                if (getCampoConsultaPessoa().equals("nome")) {
                    objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorNome(getValorConsultaPessoa(), getUsuarioVO().getPessoaConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }
                if (getCampoConsultaPessoa().equals("CPF")) {
                    objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaPessoa(), getUsuarioVO().getPessoaConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }
                if (getCampoConsultaPessoa().equals("RG")) {
                    objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorRG(getValorConsultaPessoa(), getUsuarioVO().getPessoaConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }                
                if (getCampoConsultaPessoa().equals("registroAcademico")) {
                	objs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaPorRegistroAcademico(getValorConsultaPessoa(),0 , getUsuarioVO().getPessoaConsulta(), false,false,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                }
            }
            setListaConsultaPessoa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaPessoa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPessoa() {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
        getUsuarioVO().setPessoa(obj);
        getUsuarioVO().setNome(obj.getNome());
        if (obj.getFuncionario()) {
            obterUnidadesEnsino(obj);
        }
//        setListaConsultaPessoa(new ArrayList(0));
    }

    public void alterarPessoa() {
        setNomeReadonly(Boolean.FALSE);
        getUsuarioVO().setPessoa(new PessoaVO());
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

    public void adicionarUsuarioPerfilAcesso() throws Exception {
        try {
            if (!getUsuarioVO().getCodigo().equals(0)) {
                usuarioPerfilAcessoVO.setUsuario(getUsuarioVO().getCodigo());
            }
            if (getUsuarioPerfilAcessoVO().getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                Integer campoUnidadeEnsino = getUsuarioPerfilAcessoVO().getUnidadeEnsinoVO().getCodigo();
                UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getUsuarioPerfilAcessoVO().setUnidadeEnsinoVO(unidadeEnsino);
            }
            if (getUsuarioPerfilAcessoVO().getPerfilAcesso().getCodigo().intValue() != 0) {
                Integer campoPerfilAcesso = getUsuarioPerfilAcessoVO().getPerfilAcesso().getCodigo();
                PerfilAcessoVO perfilAcesso = getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(campoPerfilAcesso, getUsuarioLogado());
                getUsuarioPerfilAcessoVO().setPerfilAcesso(perfilAcesso);
            }
            getUsuarioVO().adicionarObjUsuarioPerfilAcessoVOs(getUsuarioPerfilAcessoVO());
            this.setUsuarioPerfilAcessoVO(new UsuarioPerfilAcessoVO());
            setMensagemID("msg_dados_adicionados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>EnderecoCobranca</code> para edição pelo usuário.
     */
    public void editarUsuarioPerfilAcesso() throws Exception {
        UsuarioPerfilAcessoVO obj = (UsuarioPerfilAcessoVO) context().getExternalContext().getRequestMap().get("usuarioPerfilAcessoItem");
        setUsuarioPerfilAcessoVO(obj);

    }

    /*
     * Método responsável por remover um novo objeto da classe <code>EnderecoCobranca</code> do objeto <code>clienteVO</code> da classe <code>Cliente</code>
     */
    public void removerUsuarioPerfilAcesso() throws Exception {
        UsuarioPerfilAcessoVO obj = (UsuarioPerfilAcessoVO) context().getExternalContext().getRequestMap().get("usuarioPerfilAcessoItem");
        getUsuarioVO().excluirObjUsuarioPerfilAcessoVOs(obj);
        setMensagemID("msg_dados_excluidos");

    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List getListaSelectItemTipoUsuario() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoClientes = (Hashtable) Dominios.getTipoUsuario();
        Enumeration keys = situacaoClientes.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoClientes.get(value);
            objs.add(new SelectItem(value, label));
        }

        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>CodPerfilAcesso</code>.
     */
    public void montarListaSelectItemCodPerfilAcesso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPerfilAcessoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PerfilAcessoVO obj = (PerfilAcessoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCodPerfilAcesso(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CodPerfilAcesso</code>. Buscando todos os objetos correspondentes a entidade <code>PerfilAcesso</code>. Esta rotina não
     * recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCodPerfilAcesso() {
        try {
            montarListaSelectItemCodPerfilAcesso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarPerfilAcessoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>CodPerfilAcesso</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
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
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>PerfilAcesso</code>. Esta rotina não
     * recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem
     * apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCodPerfilAcesso();
        montarListaSelectItemUnidadeEnsino();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem> (0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("username", "Username"));
        itens.add(new SelectItem("nomePerfilAcesso", "Perfil de Acesso"));
        itens.add(new SelectItem("tipoUsuario", "Tipo de Usuário"));        
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaEspecificaSimulacao() {
        List<SelectItem> itens = new ArrayList<SelectItem> (0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("username", "Username"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        return itens;
    }
    
    public List<SelectItem> getTipoConsultaPessoa() {
        List<SelectItem> itens = new ArrayList<SelectItem> (0);    	
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));       
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaParceiro() {
        List<SelectItem> itens = new ArrayList<SelectItem> (0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("usuarioCons");
    }

    public List<SelectItem> getListaSelectItemCodPerfilAcesso() {
        if (listaSelectItemCodPerfilAcesso == null) {
            listaSelectItemCodPerfilAcesso = new ArrayList<SelectItem>(0);
        }
        return (listaSelectItemCodPerfilAcesso);
    }

    public void setListaSelectItemCodPerfilAcesso(List listaSelectItemCodPerfilAcesso) {
        this.listaSelectItemCodPerfilAcesso = listaSelectItemCodPerfilAcesso;
    }

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    public void consultarPessoaPorChavePrimaria() {
        try {
            if (usuarioVO.getPessoa().getCPF().length() == 14) {
                String campoConsulta = usuarioVO.getPessoa().getCPF();
                PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, getUsuarioVO().getPessoaConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (pessoa.getCodigo().intValue() != 0) {
                    usuarioVO.setPessoa(pessoa);
                    usuarioVO.setNome(pessoa.getNome());
                    setNomeReadonly(true);
                } else {
                    usuarioVO.getPessoa().setCPF("");
                    usuarioVO.setNome("");
                    usuarioVO.getPessoa().setNome("");
                    usuarioVO.getPessoa().setCodigo(0);
                    setMensagemID("msg_erro_dadosnaoencontrados");
                }
                if (pessoa.getFuncionario()) {
                    obterUnidadesEnsino(pessoa);
                }
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setNomeReadonly(false);
            usuarioVO.getPessoa().setCPF("");
            usuarioVO.setNome("");
            usuarioVO.getPessoa().setNome("");
            usuarioVO.getPessoa().setCodigo(0);
        }
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
            listaConsultaPessoa = new ArrayList();
        }
        return listaConsultaPessoa;
    }

    public void setListaConsultaPessoa(List listaConsultaPessoa) {
        this.listaConsultaPessoa = listaConsultaPessoa;
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

    public String getPessoa_Erro() {
        if (pessoa_Erro == null) {
            pessoa_Erro = "";
        }
        return pessoa_Erro;
    }

    public void setPessoa_Erro(String pessoa_Erro) {
        this.pessoa_Erro = pessoa_Erro;
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

    public UsuarioPerfilAcessoVO getUsuarioPerfilAcessoVO() {
        if (usuarioPerfilAcessoVO == null) {
            usuarioPerfilAcessoVO = new UsuarioPerfilAcessoVO();
        }
        return usuarioPerfilAcessoVO;
    }

    public void setUsuarioPerfilAcessoVO(UsuarioPerfilAcessoVO usuarioPerfilAcessoVO) {
        this.usuarioPerfilAcessoVO = usuarioPerfilAcessoVO;
    }

    public Boolean getNomeReadonly() {
        if (nomeReadonly == null) {
            nomeReadonly = Boolean.FALSE;
        }
        return nomeReadonly;
    }

    public void setNomeReadonly(Boolean nomeReadonly) {
        this.nomeReadonly = nomeReadonly;
    }

    private void obterUnidadesEnsino(PessoaVO pessoaVO) {
        try {
            FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(pessoaVO.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            Iterator i = funcionarioVO.getFuncionarioCargoVOs().iterator();
            while (i.hasNext()) {
                UsuarioPerfilAcessoVO objUsuarioPerfilAcessoVO = new UsuarioPerfilAcessoVO();
                FuncionarioCargoVO objFuncionarioCargo = (FuncionarioCargoVO) i.next();
                if (!verificarJaExistePerfilAcessoUnidadeEnsino(objFuncionarioCargo.getUnidade())) {
                    objUsuarioPerfilAcessoVO.setUnidadeEnsinoVO(objFuncionarioCargo.getUnidade());
                    usuarioVO.getUsuarioPerfilAcessoVOs().add(objUsuarioPerfilAcessoVO);
                }
            }
        } catch (Exception e) {
        }
    }

    private Boolean verificarJaExistePerfilAcessoUnidadeEnsino(UnidadeEnsinoVO unidade) {
        Iterator i = usuarioVO.getUsuarioPerfilAcessoVOs().iterator();
        while (i.hasNext()) {
            UsuarioPerfilAcessoVO perfil = (UsuarioPerfilAcessoVO) i.next();
            if (perfil.getUnidadeEnsinoVO().getCodigo().equals(unidade.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public void atualizarUsernameSenhaProfessores() throws Exception {
        getFacadeFactory().getUsuarioFacade().executarAtualizarUsernameSenha("PR", getUsuarioLogado());
        getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoAlterarSenha(null, getUsuarioLogado(), getUsuarioLogado().getSenha());
    }

    public String getApresentarMascaraCpf() {
        if (getCampoConsultaPessoa().equals("CPF")) {
            return "return mascara(this.form, 'formPessoa:valorConsulta', '999.999.999-99', event);";
        }
        return "";
    }

    public String getApresentarQuantidadeCaracteresCpf() {
        if (getCampoConsultaPessoa().equals("CPF")) {
            return "14";
        }
        return "100";
    }

    public boolean getIsApresentarCamposUserNameSenha() {
        return getUsuarioVO().getAlterarSenha() || getUsuarioVO().getNovoObj();
    }

   
    
    public void realizarLimpezaCamposParaConsultaPessoa() {
        setCampoConsultaPessoa("");
        setValorConsultaPessoa("");
        getListaConsultaPessoa().clear();
    }
    
//    public void verificarVisaoDoUsuario() {
//      try {
//    	UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItem");
//    	getLoginControle().verificarSeTipoUsuarioIsUnico(obj);
//		List consulta = getFacadeFactory().getUsuarioFacade().consultarPorNome(obj.getNome(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
//      } catch (Exception e) {
//		e.printStackTrace();
//		}
//    }
    
     private boolean alunoSimulacaoValido = false;
     private String qualVisaoSimularAcesso ="";
     
     public String getQualVisaoSimularAcesso() {
		return qualVisaoSimularAcesso;
	 }

     public void setQualVisaoSimularAcesso(String qualVisaoSimularAcesso) {
		this.qualVisaoSimularAcesso = qualVisaoSimularAcesso;
     }

	public String getExecutarNavegacaoSimulacaoVisaoAluno() {
		try {
			if (isAlunoSimulacaoValido() && (getQualVisaoSimularAcesso().equals("AL")
					|| getLoginControle().getOpcao().contains("telaInicialVisaoAluno"))) {
				if (!getLoginControle().getOpcao().contains("telaInicialVisaoAluno")) {
					getLoginControle().logarDiretamenteComoAluno(true);
				}
				return "removerPopup('" + getLoginControle().getFinalizarPopups() + "');simularAcessoFichaAluno();window.close();";
			} else if (isAlunoSimulacaoValido() && getQualVisaoSimularAcesso().equals("PR")
					|| getLoginControle().getOpcao().contains("telaInicialVisaoProfessor")) {
				
					getLoginControle().logarDiretamenteComoProfessor(true);
//					return "/visaoAdministrativo/administrativo/homeAdministrador.xhtml";		
				return "removerPopup('" + getLoginControle().getFinalizarPopups() + "');simularAcessoProfessor();window.close();";
			} else if (isAlunoSimulacaoValido() && (getQualVisaoSimularAcesso().equals("CO")
					|| getLoginControle().getOpcao().contains("telaInicialVisaoCoordenador"))) {
				if (!getLoginControle().getOpcao().contains("telaInicialVisaoCoordenador")) {
					getLoginControle().logarDiretamenteComoCoordenador(true);
				}
				return "removerPopup('" + getLoginControle().getFinalizarPopups() + "');simularAcessoCoordenador();window.close();";
			} else if (isAlunoSimulacaoValido() && !getLoginControle().getOpcao().contains("telaInicialVisaoAluno")
					&& !getLoginControle().getOpcao().contains("telaInicialVisaoProfessor")
					&& !getLoginControle().getOpcao().contains("telaInicialVisaoCoordenador")) {
				return "removerPopup('" + getLoginControle().getFinalizarPopups()
						+ "');simularAcessoFichaAlunoAvaliacaoInst();window.close();";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";

	}
     
	public String loginComSimulacaoAcessoVisaoAluno()  {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItem");
			setQualVisaoSimularAcesso("AL");
			return loginComSimulacaoAcesso(obj);
		} catch (Exception e) {
			setAlunoSimulacaoValido(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String loginComSimulacaoAcessoVisaoProfessor() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItem");
			setQualVisaoSimularAcesso("PR");
			return loginComSimulacaoAcesso(obj);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String loginComSimulacaoAcessoVisaoCoordenador() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItem");
			setQualVisaoSimularAcesso("CO");
			return loginComSimulacaoAcesso(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
     
    
    public String loginComSimulacaoAcesso(UsuarioVO obj) throws Exception {
		UsuarioVO usuarioVO = null;
		SimularAcessoAlunoVO simulacao = new SimularAcessoAlunoVO();
		try {
			usuarioVO = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuarioSimulacaoVisaoAluno(obj.getPessoa().getCodigo(), false);
			simulacao.setDataSimulacao(new Date());
			simulacao.setResponsavelSimulacaoAluno(new UsuarioVO());
			simulacao.getResponsavelSimulacaoAluno().setCodigo(getUsuarioLogado().getCodigo());
			simulacao.getResponsavelSimulacaoAluno().setUsername(getUsuarioLogado().getUsername());
			simulacao.getResponsavelSimulacaoAluno().setNome(getUsuarioLogado().getNome());
			simulacao.getResponsavelSimulacaoAluno().setSenha(getUsuarioLogado().getSenha());
			simulacao.getResponsavelSimulacaoAluno().setTipoUsuario(getUsuarioLogado().getTipoUsuario());
			simulacao.getResponsavelSimulacaoAluno().setUnidadeEnsinoLogado(getUnidadeEnsinoLogado());

			LoginControle login = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
			simulacao.setOpcaoLogin(login.getOpcao());
			String retorno = executarLogin(usuarioVO.getUsername(), usuarioVO.getSenha());
			if(!Uteis.isAtributoPreenchido(retorno)) {
				throw new Exception("Não foi possível simular acesso com este usuário, entre em contato com o administrador do sistema.");
			}
			simulacao.setUsuarioSimulado(getUsuarioLogadoClone());
			getFacadeFactory().getSimularAcessoAlunoFacade().incluir(simulacao, false, getUsuarioLogado());
			getUsuarioLogado().setSimularAcessoAluno(simulacao);
			setAlunoSimulacaoValido(true);
			return retorno;
		} catch (Exception e) {
			try {
				setQualVisaoSimularAcesso("FU");
				if (Uteis.isAtributoPreenchido(simulacao.getResponsavelSimulacaoAluno())) {
					executarLogin(simulacao.getResponsavelSimulacaoAluno().getUsername(), simulacao.getResponsavelSimulacaoAluno().getSenha());
				}
			} catch (Exception e2) {
				setAlunoSimulacaoValido(false);
				throw e2;
			}
			setAlunoSimulacaoValido(false);
			throw e;
		}
	}

	public String executarLogin(String userName, String senha) throws Exception {
		LoginControle loginControle = (LoginControle) getControlador("LoginControle");
		inativarUsuarioControleAtividadesUsuarioVO(getUsuarioLogadoClone());
		Uteis.removerObjetoMemoria(context().getExternalContext().getSessionMap().remove("usuarioLogado"));
		loginControle.setUsuario(new UsuarioVO());
		loginControle.getUsuario().setPerfilAcesso(new PerfilAcessoVO());
		loginControle.setUsername(userName);
		loginControle.setSenha(senha);
		String retorno = loginControle.loginSistema(true, false);
		if(retorno.equals("")) {
			if(getQualVisaoSimularAcesso().equals("CO")) {
				retorno = loginControle.logarDiretamenteComoCoordenador(true);
				if(!Uteis.isAtributoPreenchido(retorno) && !loginControle.getListaSelectItemUnidadeEnsinoCoordenador().isEmpty()) {
					retorno = loginControle.selecionarUnidadeEnsinoCoordenador();
				}				
			}else if(getQualVisaoSimularAcesso().equals("PR")) {
				retorno = loginControle.logarDiretamenteComoProfessor(true);
			}else if(getQualVisaoSimularAcesso().equals("AL")) {
				retorno = loginControle.logarDiretamenteComoAluno(true);
			}else if(getQualVisaoSimularAcesso().equals("FU")) {
				retorno = loginControle.logarDiretamenteComoFuncionario();
			}/*else if(getQualVisaoSimularAcesso().equals("DM")) {
				retorno = loginControle.logarDiretamenteComoDiretorMultiCampus();
			}*/
		} 
		if (!loginControle.getMostrarModalEscolhaVisao() && !Uteis.isAtributoPreenchido(retorno)) {
			//setMensagemDetalhada("msg_erro", "Foi encontrada uma irregularidade em sua matrícula. Entre em contato com o departamento pedagógico.", Uteis.ERRO);
			throw new Exception("Foi encontrada uma irregularidade em sua matrícula. Entre em contato com o departamento pedagógico.");
			
		}
		return retorno;
	}
	 

	 
	


	public boolean isVisualizarBotoesVisao() {
		return visualizarBotoesVisao;
	}

	public void setVisualizarBotoesVisao(boolean visualizarBotoesVisao) {
		
		this.visualizarBotoesVisao = visualizarBotoesVisao;
	}

	public boolean isAlunoSimulacaoValido() {
		return alunoSimulacaoValido;
	}

	public void setAlunoSimulacaoValido(boolean alunoSimulacaoValido) {
		this.alunoSimulacaoValido = alunoSimulacaoValido;
	}

    public String getCampoConsultaParceiro() {
        return campoConsultaParceiro;
    }

    public void setCampoConsultaParceiro(String campoConsultaParceiro) {
        this.campoConsultaParceiro = campoConsultaParceiro;
    }

    public List getListaConsultaParceiro() {
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;
    }

    public String getValorConsultaParceiro() {
        return valorConsultaParceiro;
    }

    public void setValorConsultaParceiro(String valorConsultaParceiro) {
        this.valorConsultaParceiro = valorConsultaParceiro;
    }

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

	public String getValorConsultaTipoUsuario() {
		if (valorConsultaTipoUsuario == null) {
			valorConsultaTipoUsuario = "AL";
		}
		return valorConsultaTipoUsuario;
	}

	public void setValorConsultaTipoUsuario(String valorConsultaTipoUsuario) {
		this.valorConsultaTipoUsuario = valorConsultaTipoUsuario;
	}
	
	public Boolean getApresentarComboTipoUsuario() {
		return getControleConsulta().getCampoConsulta().equals("tipoUsuario");
	}

	public List<UsuarioVO> getListaUsuario() {
		if(listaUsuario == null) {
			listaUsuario = new ArrayList<UsuarioVO>();
		}
		return listaUsuario;
	}

	public void setListaUsuario(List<UsuarioVO> listaUsuario) {
		
		this.listaUsuario = listaUsuario;
	}
	public String getFecharAbaVisaoAdministrativo() {
		String uri = request().getRequestURI();
		 String url = request().getRequestURL().toString();
		 String ctxPath = request().getContextPath();
		 url = url.replaceFirst(uri, "");
		 String urlPadrao = url+ctxPath + "/visaoAdministrativo/administrativo/administrativo.xhtml";
		return urlPadrao;
	}
	public String getLinkVisaoCoordenador() {
		String uri = request().getRequestURI();
		 String url = request().getRequestURL().toString();
		 String ctxPath = request().getContextPath();
		 url = url.replaceFirst(uri, "");
		 String urlPadrao = url+ctxPath + "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
		return urlPadrao;
//		seireturn getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
	}
	
	public String getLinkVisaoProfessor() {
		String uri = request().getRequestURI();
		 String url = request().getRequestURL().toString();
		 String ctxPath = request().getContextPath();
		 url = url.replaceFirst(uri, "");
		 String urlPadrao = url+ctxPath + "/visaoProfessor/telaInicialVisaoProfessor.xhtml";
		return urlPadrao;
//		seireturn getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
	}
	
	public String getLinkVisaoAluno() {
		String uri = request().getRequestURI();
		 String url = request().getRequestURL().toString();
		 String ctxPath = request().getContextPath();
		 url = url.replaceFirst(uri, "");
		 String urlPadrao = url+ctxPath + "/visaoAluno/telaInicialVisaoAluno.xhtml";
		return urlPadrao;
//		seireturn getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/visaoCoordenador/telaInicialVisaoCoordenador.xhtml";
	}
	
}
