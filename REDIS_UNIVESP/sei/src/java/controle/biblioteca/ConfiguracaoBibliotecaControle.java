package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas configuracaoBibliotecaForm.jsp
 * configuracaoBibliotecaCons.jsp) com as funcionalidades da classe <code>ConfiguracaoBiblioteca</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ConfiguracaoBiblioteca
 * @see ConfiguracaoBibliotecaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.BibliotecaExternaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.RegraAplicacaoBloqueioBibliotecaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ConfiguracaoBibliotecaControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoBibliotecaControle extends SuperControle implements Serializable {

    private ConfiguracaoBibliotecaVO configuracaoBibliotecaVO;
    private BibliotecaExternaVO bibliotecaExternaVO;
    private List<SelectItem> listaSelectItemGrupoDestinatariosNotificacao;
    private List<SelectItem> listaSelectItemRegraAplicacaoBloqueioBiblioteca;
    private List<FuncionarioVO> listaFuncionarios;
    private String campoConsultaFuncionario;
    private String campoConsultaAluno;
    private String valorConsultaFuncionario;
    private String valorConsultaAluno;
    private Date dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca;
    private List<UsuarioVO> listaUsuariosIntegracaoMinhaBiblioteca;
	private ProgressBarVO progressBarVO;
	private List<CatalogoVO> catalogoVOs; 
    private List<ArquivoMarc21VO> arquivoMarc21VOs;
    private List<String> listaMensagemExclusaoUsuariosMinhaBiblioteca;
    private String abaNavegar;
    
    public ConfiguracaoBibliotecaControle() throws Exception {
        //obterUsuarioLogado();
        novo();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparCamposParaClone() {
        getConfiguracaoBibliotecaVO().setCodigo(0);
        getConfiguracaoBibliotecaVO().setNovoObj(true);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ConfiguracaoBiblioteca</code> para edição
     * pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setConfiguracaoBibliotecaVO(new ConfiguracaoBibliotecaVO());
        montarListaSelectItemGrupoDestinatarios();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ConfiguracaoBiblioteca</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
    	 try{
    		listaUsuariosIntegracaoMinhaBiblioteca = null;
	        ConfiguracaoBibliotecaVO obj = (ConfiguracaoBibliotecaVO) context().getExternalContext().getRequestMap().get("configuracaoBibliotecaItens");
	        obj.setFuncionarioPadraoEnvioMensagem(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(obj.getFuncionarioPadraoEnvioMensagem().getCodigo(), false, getUsuarioLogado()));
	        montarListaSelectItemGrupoDestinatarios();
	        setConfiguracaoBibliotecaVO(obj);
	        setMensagemID("msg_dados_editar");
    	 } catch(Exception e){
         	setMensagemDetalhada("msg_erro",e.getMessage());
         }
    	 return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>ConfiguracaoBiblioteca</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() throws Exception {
        try {
            getFacadeFactory().getConfiguracaoBibliotecaFacade().validarDados(getConfiguracaoBibliotecaVO());
            if (getConfiguracaoBibliotecaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getConfiguracaoBibliotecaFacade().incluir(getConfiguracaoBibliotecaVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getConfiguracaoBibliotecaFacade().alterar(getConfiguracaoBibliotecaVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            setMensagemDetalhada("");
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ConfiguracaoBibliotecaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List<ConfiguracaoBibliotecaVO> objs = new ArrayList<ConfiguracaoBibliotecaVO>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaCons.xhtml");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ConfiguracaoBibliotecaVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() throws Exception {
        try {
            getFacadeFactory().getConfiguracaoBibliotecaFacade().excluir(getConfiguracaoBibliotecaVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaForm.xhtml");
        }
    }

	public void consultarUsuarioIntegracaoMinhaBiblioteca() {
		try {
			setListaMensagemExclusaoUsuariosMinhaBiblioteca(new ArrayList<>());
			setListaMensagemErro(new ArrayList<>());
			setListaUsuariosIntegracaoMinhaBiblioteca(getFacadeFactory().getUsuarioFacade().consultarUsuarioPossuiIntegracaoMinhaBiblioteca(
					getDataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca(), getValorConsultaAluno(), getCampoConsultaAluno()));
			if (!Uteis.isAtributoPreenchido(getListaUsuariosIntegracaoMinhaBiblioteca())) {
				throw new Exception("Nenhum usuário encontrado.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void removerAlunoIntegradoMinhaBiblioteca() {
		try {
			setListaMensagemExclusaoUsuariosMinhaBiblioteca(new ArrayList<>());
			setListaMensagemErro(new ArrayList<>());
			UsuarioVO usuario = (UsuarioVO) context().getExternalContext().getRequestMap().get("UsuarioItens");
			Map<String, Integer> removerUsuarioMinhaBiblioteca = getFacadeFactory().getUsuarioFacade().removerUsuarioMinhaBiblioteca(Arrays.asList(usuario), new ConsistirException());
			if (Uteis.isAtributoPreenchido(removerUsuarioMinhaBiblioteca)) {
				removerUsuarioMinhaBiblioteca.entrySet().stream().map(this::realizarMontagemMensagemExclusaoUsuariosMinhaBiblioteca).forEach(getListaMensagemExclusaoUsuariosMinhaBiblioteca()::add);
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void removerAlunosIntegradoMinhaBiblioteca() {
		try {
			setListaMensagemExclusaoUsuariosMinhaBiblioteca(new ArrayList<>());
			Map<String, Integer> removerUsuarioMinhaBiblioteca = getFacadeFactory().getUsuarioFacade().removerUsuarioMinhaBiblioteca(getListaUsuariosIntegracaoMinhaBiblioteca(), new ConsistirException());
			if (Uteis.isAtributoPreenchido(removerUsuarioMinhaBiblioteca)) {
				removerUsuarioMinhaBiblioteca.entrySet().stream().map(this::realizarMontagemMensagemExclusaoUsuariosMinhaBiblioteca).forEach(getListaMensagemExclusaoUsuariosMinhaBiblioteca()::add);
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada(MSG_TELA.msg_erro.name(), e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoBibliotecaCons.xhtml");
    }

    public void adicionarUrlBibliotecaExterna() throws Exception {
        try {
            if (!getConfiguracaoBibliotecaVO().getCodigo().equals(0)) {
                getBibliotecaExternaVO().setConfiguracaoBiblioteca(getConfiguracaoBibliotecaVO().getCodigo());
            }
            getConfiguracaoBibliotecaVO().adicionarObjLinkBibliotecaVOs(getBibliotecaExternaVO());
            setBibliotecaExternaVO(new BibliotecaExternaVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

        /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>CursoTurno</code> para edição pelo
     * usuário.
     */
    public void editarUrlBibliotecaExterna() throws Exception {
        BibliotecaExternaVO obj = (BibliotecaExternaVO) context().getExternalContext().getRequestMap().get("bibliotecaExternaItens");
        setBibliotecaExternaVO(obj);
        setMensagemID("msg_dados_editar");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>CursoTurno</code> do objeto <code>cursoVO</code> da
     * classe <code>Curso</code>
     */
    public void removerUrlBibliotecaExterna() throws Exception {
        BibliotecaExternaVO obj = (BibliotecaExternaVO) context().getExternalContext().getRequestMap().get("bibliotecaExternaItens");
        getConfiguracaoBibliotecaVO().excluirObjLinkBibliotecaVOs(obj.getUrl());
        setMensagemID("msg_dados_excluidos");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        configuracaoBibliotecaVO = null;
    }
    /**
     * Metodo Responsavel por montar a combox Grupo Destinatario Notificação.
     */
    public void montarListaSelectItemGrupoDestinatarios() {
        try {
            List<GrupoDestinatariosVO> lista = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            setListaSelectItemGrupoDestinatariosNotificacao(UtilSelectItem.getListaSelectItem(lista, "codigo", "nomeGrupo"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarFuncionarioRichModal(){
    	try{
    		List<FuncionarioVO> objs  = new ArrayList<FuncionarioVO>(0);
    		objs = getFacadeFactory().getFuncionarioFacade().consultar(getValorConsultaFuncionario(), getCampoConsultaFuncionario(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    		setListaFuncionarios(objs);
    		setMensagemID("msg_dados_consultados");
       	}
    	catch(Exception e){
    		setListaFuncionarios(new ArrayList<FuncionarioVO>(0));
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    	
    }
    public List<SelectItem> getTipoConsultarComboFuncionario() {
        List<SelectItem> itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matricula"));
        itens.add(new SelectItem("cargo", "Cargo"));
        return itens;
    }
    public List<SelectItem> getTipoConsultarComboAluno() {
        List<SelectItem> itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("email", "Email"));       
        return itens;
    }
    
    public void selecionarFuncionarioPadrao(){
    	 FuncionarioVO funcionarioPadrao = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
    	 configuracaoBibliotecaVO.setFuncionarioPadraoEnvioMensagem(funcionarioPadrao);
    	 funcionarioPadrao = null;
    	 
    }
    
	public void limparCampoConsultaAluno() {
		removerObjetoMemoria(null);
		setCampoConsultaAluno(null);
		setDataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca(null);
		setListaUsuariosIntegracaoMinhaBiblioteca(null);
		setListaMensagemExclusaoUsuariosMinhaBiblioteca(new ArrayList<>());
		setListaMensagemErro(new ArrayList<>());
	}
    
    public void limparFuncionarioPadrao(){
    	configuracaoBibliotecaVO.setFuncionarioPadraoEnvioMensagem(null);
    }

    public ConfiguracaoBibliotecaVO getConfiguracaoBibliotecaVO() {
        if (configuracaoBibliotecaVO == null) {
            configuracaoBibliotecaVO = new ConfiguracaoBibliotecaVO();
        }
        return configuracaoBibliotecaVO;
    }

    public void setConfiguracaoBibliotecaVO(ConfiguracaoBibliotecaVO configuracaoBibliotecaVO) {
        this.configuracaoBibliotecaVO = configuracaoBibliotecaVO;
    }

    public BibliotecaExternaVO getBibliotecaExternaVO() {
        if (bibliotecaExternaVO == null) {
            bibliotecaExternaVO = new BibliotecaExternaVO();
        }
        return bibliotecaExternaVO;
    }

    public void setBibliotecaExternaVO(BibliotecaExternaVO bibliotecaExternaVO) {
        this.bibliotecaExternaVO = bibliotecaExternaVO;
    }

    public List<SelectItem> getListaSelectItemGrupoDestinatariosNotificacao() {
        return listaSelectItemGrupoDestinatariosNotificacao;
    }

    public void setListaSelectItemGrupoDestinatariosNotificacao(List<SelectItem> listaSelectItemGrupoDestinatariosNotificacao) {
        this.listaSelectItemGrupoDestinatariosNotificacao = listaSelectItemGrupoDestinatariosNotificacao;
    }

	

	public List<FuncionarioVO> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<FuncionarioVO> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valoConsultaFuncionario) {
		this.valorConsultaFuncionario = valoConsultaFuncionario;
	}

	
	public List<SelectItem> getListaSelectItemRegraAplicacaoBloqueioBiblioteca() {
		if(listaSelectItemRegraAplicacaoBloqueioBiblioteca == null){
			listaSelectItemRegraAplicacaoBloqueioBiblioteca = UtilSelectItem.getListaSelectItemEnum(RegraAplicacaoBloqueioBibliotecaEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemRegraAplicacaoBloqueioBiblioteca;
	}

	public void setListaSelectItemRegraAplicacaoBloqueioBiblioteca(
			List<SelectItem> listaSelectItemRegraAplicacaoBloqueioBiblioteca) {
		this.listaSelectItemRegraAplicacaoBloqueioBiblioteca = listaSelectItemRegraAplicacaoBloqueioBiblioteca;
	}

	public Date getDataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca() {
		if(dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca == null ) {
			dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca = new Date();
		}
		return dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca;
	}

	public void setDataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca(
			Date dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca) {
		this.dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca = dataEvasaoExclusaoAlunoIntegradoMinhaBiblioteca;
	}

	public List<UsuarioVO> getListaUsuariosIntegracaoMinhaBiblioteca() {
		if(listaUsuariosIntegracaoMinhaBiblioteca == null) {
			listaUsuariosIntegracaoMinhaBiblioteca = new ArrayList<UsuarioVO>();
		}
		return listaUsuariosIntegracaoMinhaBiblioteca;
	}

	public void setListaUsuariosIntegracaoMinhaBiblioteca(List<UsuarioVO> listaUsuariosIntegracaoMinhaBiblioteca) {
		this.listaUsuariosIntegracaoMinhaBiblioteca = listaUsuariosIntegracaoMinhaBiblioteca;
	}

	public String getValorConsultaAluno() {
		if(valorConsultaAluno ==null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if(campoConsultaAluno ==null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}
    
	public void verificarConexaoBibliotecaEbsco() {
		try {
			getFacadeFactory().getConfiguracaoBibliotecaFacade().verificarConexaoBibliotecaEbsco(
					getConfiguracaoBibliotecaVO().getHostEbsco(), getConfiguracaoBibliotecaVO().getUsuarioEbsco(),
					getConfiguracaoBibliotecaVO().getSenhaEbsco());
			setMensagemID("msg_dados_conexao_validados");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}
    
	
	public void realizarEnvioMarcCatalogosIntegracaoEbsco() {		 
		try {	
			 getFacadeFactory().getConfiguracaoBibliotecaFacade().verificarConexaoBibliotecaEbsco(getConfiguracaoBibliotecaVO().getHostEbsco(),
					 getConfiguracaoBibliotecaVO().getUsuarioEbsco(),getConfiguracaoBibliotecaVO().getSenhaEbsco());	
			 
			 setProgressBarVO(new ProgressBarVO());
			 getProgressBarVO().resetar();
			 getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			 getProgressBarVO().setUsuarioVO(getUsuarioLogado());	    
		     List<ArquivoMarc21VO> arquivoMarc21VOs = getFacadeFactory().getArquivoMarc21Facade().montarArquivoMarc21DadosCatalogo(null , getProgressBarVO().getUsuarioVO());	
		     setArquivoMarc21VOs(arquivoMarc21VOs);
		     if(getArquivoMarc21VOs().get(0).getArquivoMarc21CatalogoVOs().isEmpty()) {		    	 
		    	 throw new Exception("Não Existe dados a serem Enviados ."); 
		     }
		   
		     Uteis.checkState(!Uteis.isAtributoPreenchido(getArquivoMarc21VOs()), "Não foi encontrado nenhum catalogo selecionada para realizar a operação de envio ");		    
		     getProgressBarVO().iniciar(0l,getArquivoMarc21VOs().size(), "Iniciando Processamento da(s) operações.", true, this, "realizarEnviarCatalogosIntegracaoEbsco" );			 
		  } catch (Exception e) {				
			  setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
    
	
	public void realizarEnviarCatalogosIntegracaoEbsco()  {
		try {			
			getFacadeFactory().getConfiguracaoBibliotecaFacade().realizarEnvioCatalogoIntegracaoEbsco(getArquivoMarc21VOs() ,getProgressBarVO() ,getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), true,"");	
			setMensagemID(MSG_TELA.msg_dados_Enviados.name());
			getProgressBarVO().setForcarEncerramento(true);
			
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);		
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}

	public ProgressBarVO getProgressBarVO() {
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public List<CatalogoVO> getCatalogoVOs() {
		return catalogoVOs;
	}

	public void setCatalogoVOs(List<CatalogoVO> catalogoVOs) {
		this.catalogoVOs = catalogoVOs;
	}
	
	public List<ArquivoMarc21VO> getArquivoMarc21VOs() {
		if(arquivoMarc21VOs == null ) {
			arquivoMarc21VOs = new ArrayList<ArquivoMarc21VO>(0);
		}
		return arquivoMarc21VOs;
	}

	public void setArquivoMarc21VOs(List<ArquivoMarc21VO> arquivoMarc21VOs) {
		this.arquivoMarc21VOs = arquivoMarc21VOs;
	}
	
	public List<String> getListaMensagemExclusaoUsuariosMinhaBiblioteca() {
		if (listaMensagemExclusaoUsuariosMinhaBiblioteca == null) {
			listaMensagemExclusaoUsuariosMinhaBiblioteca = new ArrayList<>();
		}
		return listaMensagemExclusaoUsuariosMinhaBiblioteca;
	}

	public void setListaMensagemExclusaoUsuariosMinhaBiblioteca(List<String> listaMensagemExclusaoUsuariosMinhaBiblioteca) {
		this.listaMensagemExclusaoUsuariosMinhaBiblioteca = listaMensagemExclusaoUsuariosMinhaBiblioteca;
	}
	
	private String realizarMontagemMensagemExclusaoUsuariosMinhaBiblioteca(Entry<String, Integer> entry) {
		return entry.getKey() + ": " + entry.getValue();
	}
	
	public String navegarAba(String aba) {
		setAbaNavegar(aba);
	    return "";
	}
	
	public String getAbaNavegar() {
		if (abaNavegar == null) {
			abaNavegar = "tabDadosGeral";
		}
		return abaNavegar;
	}

	public void setAbaNavegar(String abaNavegar) {
		this.abaNavegar = abaNavegar;
	}
}
