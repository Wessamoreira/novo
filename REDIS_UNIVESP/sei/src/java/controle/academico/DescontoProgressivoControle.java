package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas descontoProgressivoForm.jsp
 * descontoProgressivoCons.jsp) com as funcionalidades da classe <code>DescontoProgressivo</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see DescontoProgressivo
 * @see DescontoProgressivoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("DescontoProgressivoControle")
@Scope("viewScope")
@Lazy
public class DescontoProgressivoControle extends SuperControle implements Serializable {

	private DescontoProgressivoVO descontoProgressivoVO;
        private Boolean apresentarBotaoAtivar;
        private Boolean apresentarBotaoInativar;
        private Boolean permiteAlteracao;
        private String situacao;

	public DescontoProgressivoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>DescontoProgressivo</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setDescontoProgressivoVO(new DescontoProgressivoVO());
                setApresentarBotaoAtivar(Boolean.TRUE);
                setApresentarBotaoInativar(Boolean.FALSE);
                setPermiteAlteracao(true);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>DescontoProgressivo</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try{
		DescontoProgressivoVO obj = (DescontoProgressivoVO) context().getExternalContext().getRequestMap().get("descontoProgressivoItem");
		obj.setNovoObj(Boolean.FALSE);
		setDescontoProgressivoVO(obj);
                if (obj.getAtivado()) {
                    setApresentarBotaoAtivar(Boolean.FALSE);
                    setApresentarBotaoInativar(Boolean.TRUE);
                } else if (obj.getDataInativacao() != null) {
                    setApresentarBotaoAtivar(Boolean.FALSE);
                    setApresentarBotaoInativar(Boolean.FALSE);
                } else {
                    setApresentarBotaoAtivar(Boolean.TRUE);
                    setApresentarBotaoInativar(Boolean.FALSE);
                }
	
		setPermiteAlteracao(!getFacadeFactory().getDescontoProgressivoFacade().consultarDescontoProgressivoUtilizado(obj.getCodigo()));
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
	} catch (Exception e) {
		setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoCons");
	}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>DescontoProgressivo</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (descontoProgressivoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getDescontoProgressivoFacade().incluir(descontoProgressivoVO,getUsuarioLogado());
			} else {
				if(getPermiteAlteracao()){
					getFacadeFactory().getDescontoProgressivoFacade().alterar(descontoProgressivoVO,getUsuarioLogado());
				}else{
                            throw new Exception("Este registro já foi salvo, portanto não pode mais ser alterado!");
                        }
			}
			 if (getDescontoProgressivoVO().getAtivado()) {
                 setApresentarBotaoAtivar(Boolean.FALSE);
                 setApresentarBotaoInativar(Boolean.TRUE);             
             } else {
                 setApresentarBotaoAtivar(Boolean.TRUE);
                 setApresentarBotaoInativar(Boolean.FALSE);
             }
                        setPermiteAlteracao(!getFacadeFactory().getDescontoProgressivoFacade().consultarDescontoProgressivoUtilizado(getDescontoProgressivoVO().getCodigo()));
			setMensagemID("msg_dados_gravados");
//			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
		}
	}
	
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP DescontoProgressivoCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorCodigoSituacao((new Integer(valorInt)), getSituacao(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeSituacao(getControleConsulta().getValorConsulta(), getSituacao(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoCons");
		}
	}

        public void realizarAtivacaoDescontoProgressivo(){
        try {
            getFacadeFactory().getDescontoProgressivoFacade().realizarAtivacaoDescontoProgressivo(getDescontoProgressivoVO(), Boolean.TRUE, getUsuarioLogado());
            setApresentarBotaoAtivar(Boolean.FALSE);
            setApresentarBotaoInativar(Boolean.TRUE);
            setMensagemID("msg_dados_ativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarInativacaoDescontoProgressivo(){
        try {
            getFacadeFactory().getDescontoProgressivoFacade().realizarInativacaoDescontoProgressivo(getDescontoProgressivoVO(), Boolean.FALSE, getUsuarioLogado());
            setApresentarBotaoAtivar(Boolean.TRUE);
            setApresentarBotaoInativar(Boolean.FALSE);
            setMensagemID("msg_dados_inativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>DescontoProgressivoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			getFacadeFactory().getDescontoProgressivoFacade().excluir(descontoProgressivoVO,getUsuarioLogado());
			setDescontoProgressivoVO(new DescontoProgressivoVO());
			setMensagemID("msg_dados_excluidos");
//			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoForm");
		}
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
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("descontoProgressivoCons");
	}

	public DescontoProgressivoVO getDescontoProgressivoVO() {
            if (descontoProgressivoVO == null) {
                descontoProgressivoVO = new DescontoProgressivoVO();
            }
		return descontoProgressivoVO;
	}

	public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
		this.descontoProgressivoVO = descontoProgressivoVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		descontoProgressivoVO = null;
	}

     /**
     * @return the apresentarBotaoAtivar
     */
    public Boolean getApresentarBotaoAtivar() {
        if (apresentarBotaoAtivar == null) {
            apresentarBotaoAtivar = Boolean.TRUE;
        }
        return apresentarBotaoAtivar;
    }

    /**
     * @param apresentarBotaoAtivar the apresentarBotaoAtivar to set
     */
    public void setApresentarBotaoAtivar(Boolean apresentarBotaoAtivar) {
        this.apresentarBotaoAtivar = apresentarBotaoAtivar;
    }

    /**
     * @return the apresentarBotaoInativar
     */
    public Boolean getApresentarBotaoInativar() {
        if (apresentarBotaoInativar == null) {
            apresentarBotaoInativar = Boolean.FALSE;
        }
        return apresentarBotaoInativar;
    }

    /**
     * @param apresentarBotaoInativar the apresentarBotaoInativar to set
     */
    public void setApresentarBotaoInativar(Boolean apresentarBotaoInativar) {
        this.apresentarBotaoInativar = apresentarBotaoInativar;
    }

    public Boolean getApresentarMensagemEBotaoAtivar() {
        if (getApresentarBotaoAtivar()) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarBotaoGravar() {
        if (getApresentarBotaoAtivar()) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarMensagemEBotaoInativar() {
        if (getApresentarBotaoInativar()) {
            return true;
        }
        return false;
    }

    public void desmarcarDemaisUtilizarDiaFixo() {
        if (getDescontoProgressivoVO().getUtilizarDiaFixo().booleanValue()) {
            getDescontoProgressivoVO().setUtilizarDiaUtil(Boolean.FALSE);
        } 
    }

    public void desmarcarDemaisUtilizarDiaUtil() {
        if (getDescontoProgressivoVO().getUtilizarDiaUtil().booleanValue()) {
            getDescontoProgressivoVO().setUtilizarDiaFixo(Boolean.FALSE);
            
        }
    }

    public void desmarcarDemais() {
        if (getDescontoProgressivoVO().getUtilizarDiaUtil().booleanValue()) {
        	getDescontoProgressivoVO().setUtilizarDiaUtil(Boolean.FALSE);
        }
        if (getDescontoProgressivoVO().getUtilizarDiaFixo().booleanValue()) {
        	getDescontoProgressivoVO().setUtilizarDiaFixo(Boolean.FALSE);            
        }
    }
    

	public Boolean getPermiteAlteracao() {
		if(permiteAlteracao == null){
			permiteAlteracao = true;
		}
		return permiteAlteracao;
	}

	public void setPermiteAlteracao(Boolean permiteAlteracao) {
		this.permiteAlteracao = permiteAlteracao;
	}
    
    public String getSituacao() {
    	if (situacao == null) {
    		situacao = "";
    	}
		return situacao;
	}
    
    public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
    
    public List<SelectItem> getSituacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("true", "Ativo"));
		itens.add(new SelectItem("false", "Inativo"));
		return itens;
	}
}
