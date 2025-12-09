package controle.administrativo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoFuncionarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro
 */

@Controller("ConfiguracaoAtendimentoControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoAtendimentoControle extends SuperControle {
	
	private ConfiguracaoAtendimentoVO configuracaoAtendimentoVO;
	private ConfiguracaoAtendimentoUnidadeEnsinoVO configuracaoAtendimentoUnidadeEnsinoVO;
	private ConfiguracaoAtendimentoFuncionarioVO configuracaoAtendimentoFuncionarioVO;
	private Boolean configuracaoOuvidoria;
	private Boolean configuracaoFaleConosco;
	private Boolean configuracaoAtendimentoAluno;
	private List<FuncionarioVO> listaConsultaFuncionario;
	protected List<SelectItem> listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo;
	protected List<SelectItem> listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	

	public ConfiguracaoAtendimentoControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TitulacaoCurso</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		try {
			setConfiguracaoAtendimentoVO(new ConfiguracaoAtendimentoVO());
			setControleConsulta(new ControleConsulta());
			getConfiguracaoAtendimentoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			realizarApresentacaoConfiguracoesOuvidoria();
			inicializarListasSelectItemTodosComboBox();
			setListaConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TitulacaoCurso</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ConfiguracaoAtendimentoVO obj = (ConfiguracaoAtendimentoVO) context().getExternalContext().getRequestMap().get("configuracaoAtendimentoItens");
			setConfiguracaoAtendimentoVO(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			inicializarListasSelectItemTodosComboBox();
			realizarApresentacaoConfiguracoesOuvidoria();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TitulacaoCurso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String persistir() {
		try {
			getFacadeFactory().getConfiguracaoAtendimentoFacade().persistir(getConfiguracaoAtendimentoVO(), getUsuarioLogado());
			setListaConsultaFuncionario(null);
			setValorConsultaFuncionario(null);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoForm");
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TitulacaoCursoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			setListaConsulta(getFacadeFactory().getConfiguracaoAtendimentoFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoCons");
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
	public String excluir() {
		try {
			getFacadeFactory().getConfiguracaoAtendimentoFacade().excluir(getConfiguracaoAtendimentoVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoForm");
	}
	
//	public List<UnidadeEnsinoVO> autocompleteUnidadeEnsino(Object suggest) {
//        try {
//            return getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, 20, false, getUsuarioLogado());
//        } catch (Exception ex) {
//            setMensagemDetalhada("msg_erro", ex.getMessage());
//            return new ArrayList<UnidadeEnsinoVO>();
//        }
//    }
	
	public void adicionarUnidadeEnsino() throws Exception {
        try {
            if (getConfiguracaoAtendimentoUnidadeEnsinoVO().getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
            	getConfiguracaoAtendimentoUnidadeEnsinoVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getConfiguracaoAtendimentoUnidadeEnsinoVO().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
            	getFacadeFactory().getConfiguracaoAtendimentoFacade().adicionarConfiguracaoAtendimentoUnidadeEnsinoVOs(getConfiguracaoAtendimentoVO(), getConfiguracaoAtendimentoUnidadeEnsinoVO());
            	setConfiguracaoAtendimentoUnidadeEnsinoVO(new ConfiguracaoAtendimentoUnidadeEnsinoVO());
            	setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
            } else {
                setMensagemDetalhada("msg_erro", "Unidade Ensino não encontrado");
            }        
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
	public void removerUnidadeEnsino() throws Exception {
        ConfiguracaoAtendimentoUnidadeEnsinoVO obj = (ConfiguracaoAtendimentoUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("configuracaoAtendimentoUnidadeEnsinoVO");
        getFacadeFactory().getConfiguracaoAtendimentoFacade().excluirConfiguracaoAtendimentoUnidadeEnsinoVOs(getConfiguracaoAtendimentoVO(), obj, getUsuarioLogado());
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }
	
	public void autocompleteFuncionario() throws Exception {		
				
		try {
			setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeAutoComplete(getValorConsultaFuncionario(), getConfiguracaoAtendimentoVO().getListaCodigoUnidadeEnsino(), "", null, null, 20, false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		
    }
	
	public void popularCodigoFuncionario(Integer codigo) {
		try {
			getConfiguracaoAtendimentoFuncionarioVO().getFuncionarioVO().setCodigo(codigo);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		
	}
	
	public void adicionarFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			getConfiguracaoAtendimentoFuncionarioVO().setFuncionarioVO(obj);
			setValorConsultaFuncionario(null);
        	if(getConfiguracaoAtendimentoVO().getListaConfiguracaoAtendimentoUnidadeEnsinoVOs().isEmpty()){
        		throw new  Exception("Deve ser informado pelo menos uma unidade de ensino para realizar a busca dos funcionários na mesma.");
            }
        	if (getConfiguracaoAtendimentoFuncionarioVO().getFuncionarioVO().getCodigo().intValue() != 0) {
            	getFacadeFactory().getConfiguracaoAtendimentoFacade().adicionarConfiguracaoAtendimentoFuncionarioVOs(getConfiguracaoAtendimentoVO(), getConfiguracaoAtendimentoFuncionarioVO());
            	setConfiguracaoAtendimentoFuncionarioVO(new ConfiguracaoAtendimentoFuncionarioVO());
            	setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
            	
            } else {
                setMensagemDetalhada("msg_erro", "Funcionário não Encontrado.");
            }        
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
	
	public void removerFuncionario()  {
		 try {
			 ConfiguracaoAtendimentoFuncionarioVO obj = (ConfiguracaoAtendimentoFuncionarioVO) context().getExternalContext().getRequestMap().get("configuracaoAtendimentoFuncionarioVO");
			 getFacadeFactory().getConfiguracaoAtendimentoFacade().excluirConfiguracaoAtendimentoFuncionarioVOs(getConfiguracaoAtendimentoVO(), obj);
			 setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		 } catch (Exception e) {
	         setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
	     }
    }
	
	
	public void realizarApresentacaoConfiguracoesOuvidoria(){
		setConfiguracaoAtendimentoAluno(false); 
		setConfiguracaoFaleConosco(false); 
		setConfiguracaoOuvidoria(true); 
		
	}
	public void realizarApresentacaoConfiguracoesFaleConosco(){
		setConfiguracaoAtendimentoAluno(false); 
		setConfiguracaoFaleConosco(true); 
		setConfiguracaoOuvidoria(false);
		
	}
	public void realizarApresentacaoConfiguracoesAtendimentoAluno(){
		setConfiguracaoAtendimentoAluno(true); 
		setConfiguracaoFaleConosco(false); 
		setConfiguracaoOuvidoria(false);
		
	}
	/**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
    	montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo();
        montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada();
       
    }
    
    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>VisaoPadraoAluno</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
    	try {
    		montarListaSelectItemUnidadeEnsino("");
    	} catch (Exception e) {
    		//System.out.println("MENSAGEM => " + e.getMessage());;
    	}
    }
    
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
    	List resultadoConsulta = null;
    	Iterator i = null;
    	try {
    		resultadoConsulta = consultarTodosUnidadeEnsino(prm);    		
    		if(resultadoConsulta.size() == 1) {
    			getConfiguracaoAtendimentoUnidadeEnsinoVO().setUnidadeEnsinoVO((UnidadeEnsinoVO) resultadoConsulta.get(0));
    		}    		
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
    		Uteis.liberarListaMemoria(resultadoConsulta);
    		i = null;
    	}
    }
    
    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTodosUnidadeEnsino(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>VisaoPadraoAluno</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo() {
        try {
            montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTodosGrupoDestinatario(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                GrupoDestinatariosVO obj = (GrupoDestinatariosVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNomeGrupo().toString()));
            }
            setListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>VisaoPadraoAluno</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada() {
    	try {
    		montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada("");
    	} catch (Exception e) {
    		//System.out.println("MENSAGEM => " + e.getMessage());;
    	}
    }
    
    public void montarListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada(String prm) throws Exception {
    	List resultadoConsulta = null;
    	Iterator i = null;
    	try {
    		resultadoConsulta = consultarTodosGrupoDestinatario(prm);
    		i = resultadoConsulta.iterator();
    		List<SelectItem> objs = new ArrayList<SelectItem>(0);
    		objs.add(new SelectItem(0, ""));
    		while (i.hasNext()) {
    			GrupoDestinatariosVO obj = (GrupoDestinatariosVO) i.next();
    			objs.add(new SelectItem(obj.getCodigo(), obj.getNomeGrupo().toString()));
    		}
    		setListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada(objs);
    	} catch (Exception e) {
    		throw e;
    	} finally {
    		Uteis.liberarListaMemoria(resultadoConsulta);
    		i = null;
    	}
    }
    
    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTodosGrupoDestinatario(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorNomeGrupo(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }
    
	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAtendimentoCons");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		configuracaoAtendimentoVO = null;
	}

	public ConfiguracaoAtendimentoVO getConfiguracaoAtendimentoVO() {
		if(configuracaoAtendimentoVO == null){
			configuracaoAtendimentoVO = new ConfiguracaoAtendimentoVO();
		}
		return configuracaoAtendimentoVO;
	}

	public void setConfiguracaoAtendimentoVO(ConfiguracaoAtendimentoVO configuracaoAtendimentoVO) {
		this.configuracaoAtendimentoVO = configuracaoAtendimentoVO;
	}

	public List getListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo() {
		if(listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo == null){
			listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo = new ArrayList();
		}
		return listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo;
	}

	public void setListaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo(List listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo) {
		this.listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo = listaSelectItemGrupoDestinatarioQuandoOuvidoriaNaoAtendidaNoPrazo;
	}

	public List getListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada() {
		if(listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada == null){
			listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada = new ArrayList();
		}
		return listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada;
	}

	public void setListaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada(List listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada) {
		this.listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada = listaSelectItemGrupoDestinatarioQuandoOuvidoriaForMalAvaliada;
	}

	public ConfiguracaoAtendimentoUnidadeEnsinoVO getConfiguracaoAtendimentoUnidadeEnsinoVO() {
		if(configuracaoAtendimentoUnidadeEnsinoVO == null){
			configuracaoAtendimentoUnidadeEnsinoVO = new ConfiguracaoAtendimentoUnidadeEnsinoVO();
		}
		return configuracaoAtendimentoUnidadeEnsinoVO;
	}

	public void setConfiguracaoAtendimentoUnidadeEnsinoVO(ConfiguracaoAtendimentoUnidadeEnsinoVO configuracaoAtendimentoUnidadeEnsinoVO) {
		this.configuracaoAtendimentoUnidadeEnsinoVO = configuracaoAtendimentoUnidadeEnsinoVO;
	}

	public ConfiguracaoAtendimentoFuncionarioVO getConfiguracaoAtendimentoFuncionarioVO() {
		if(configuracaoAtendimentoFuncionarioVO == null){
			configuracaoAtendimentoFuncionarioVO = new ConfiguracaoAtendimentoFuncionarioVO();
		}
		return configuracaoAtendimentoFuncionarioVO;
	}

	public void setConfiguracaoAtendimentoFuncionarioVO(ConfiguracaoAtendimentoFuncionarioVO configuracaoAtendimentoFuncionarioVO) {
		this.configuracaoAtendimentoFuncionarioVO = configuracaoAtendimentoFuncionarioVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null){
			listaSelectItemUnidadeEnsino = new ArrayList();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Boolean getConfiguracaoOuvidoria() {
		if (configuracaoOuvidoria == null) {
			configuracaoOuvidoria = false;
		}
		return configuracaoOuvidoria;
	}

	public void setConfiguracaoOuvidoria(Boolean configuracaoOuvidoria) {
		this.configuracaoOuvidoria = configuracaoOuvidoria;
	}

	public Boolean getConfiguracaoFaleConosco() {
		if (configuracaoFaleConosco == null) {
			configuracaoFaleConosco = false;
		}
		return configuracaoFaleConosco;
	}

	public void setConfiguracaoFaleConosco(Boolean configuracaoFaleConosco) {
		this.configuracaoFaleConosco = configuracaoFaleConosco;
	}

	public Boolean getConfiguracaoAtendimentoAluno() {
		if (configuracaoAtendimentoAluno == null) {
			configuracaoAtendimentoAluno = false;
		}
		return configuracaoAtendimentoAluno;
	}

	public void setConfiguracaoAtendimentoAluno(Boolean configuracaoAtendimentoAluno) {
		this.configuracaoAtendimentoAluno = configuracaoAtendimentoAluno;
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

	public List getTipoConsultaFuncionarioCargo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome Funcionário"));		
		return itens;
	}
	
	public void limparValorConsulta() {
		getControleConsulta().setValorConsulta("");
	}
}
