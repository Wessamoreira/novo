package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("EstoqueControle")
@Scope("viewScope")
@Lazy
public class EstoqueControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -789963721723206581L;
	private EstoqueVO estoqueVO;
    private String unidadeEnsino_Erro;
    private String produto_Erro;
    protected String tipoEstoque;
    protected List listaSelectItemUnidadeEnsino;
    protected UnidadeEnsinoVO unidadeEnsinoVO;
    protected Boolean existeUnidadeEnsino;

    public EstoqueControle() throws Exception {
    	inicializarListasSelectItemTodosComboBox();
    	inicializarUnidadeEnsino();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
        setTipoEstoque("TO");
    }

	public void inicializarUnidadeEnsino() {
		try {
			getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
			if (getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
				setExisteUnidadeEnsino(Boolean.FALSE);
			} else {
				setExisteUnidadeEnsino(Boolean.TRUE);
			}
		} catch (Exception e) {
			setExisteUnidadeEnsino(Boolean.FALSE);
		}
	}
	
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

    
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getEstoqueVO().setUnidadeEnsino(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
    
    
    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Novo Estoque", "Novo");
        removerObjetoMemoria(this);
        setEstoqueVO(new EstoqueVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueForm");
    }

    
    public void editar() throws Exception{
        registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Editar Estoque", "Editando");
        EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueItem");
        if (obj.getEstoqueVOs().isEmpty()) {
            try {
                obj.setEstoqueVOs(getFacadeFactory().getEstoqueFacade().consultarPorCodigoProdutoComQuantidade(obj.getProduto().getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage());
            }
        }
        obj.setNovoObj(Boolean.FALSE);
        setEstoqueVO(obj);
        registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Finalizando Editar Estoque", "Editando");
        setMensagemID("msg_dados_editar");
    }

  
    public String gravar() {
        try {
            if (estoqueVO.isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Incluir Estoque", "Incluindo");
                getFacadeFactory().getEstoqueFacade().incluir(estoqueVO, getUsuarioLogado(), true);
                registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Incluir Estoque", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Alterar Estoque", "Alterando");
                getFacadeFactory().getEstoqueFacade().alterar(estoqueVO, getUsuarioLogado(), true);
                registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Finalizando Alterar Estoque", "Alterando");
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueForm");
        }
    }

    
    @Override
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Consultar Estoque", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getEstoqueFacade().consultarPorCodigo(new Integer(valorInt), this.getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("quantidade")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getEstoqueFacade().consultarPorQuantidade(new Double(valorDouble), this.getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeProduto")) {
                objs = getFacadeFactory().getEstoqueFacade().consultarPorNomeProdutoAgrupado(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoVO().getCodigo(), getTipoEstoque(),
                        true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("categoriaProduto")) {
                objs = getFacadeFactory().getEstoqueFacade().consultarPorNomeCategoriaProdutoAgrupado(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoVO().getCodigo(),
                        getTipoEstoque(), true, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Finalizando Consultar Estoque", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueCons");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueCons");
        }
    }

   /**
     * Operação responsável por processar a exclusão um objeto da classe <code>EstoqueVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    /*public void imprimiExcel() {
    	try {
    		EstoqueRelControle estoqueRelProfessor = (EstoqueRelControle) getControlador("EstoqueRelControle");
    		estoqueRelProfessor.setTipoConsulta(getControleConsulta().getCampoConsulta());
    		estoqueRelProfessor.setTipoEstoque(getTipoEstoque());
    		estoqueRelProfessor.setValorConsulta(getControleConsulta().getValorConsulta());
    		estoqueRelProfessor.setUnidadeEnsinoVO(getUnidadeEnsinoVO());
    		estoqueRelProfessor.setEstoqueVO(getEstoqueVO());
    		estoqueRelProfessor.imprimirExcel();
    	} catch (Exception e) {    		
    	}        
    }*/
    
	public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Inicializando Excluir Estoque", "Excluindo");
            getFacadeFactory().getEstoqueFacade().excluir(estoqueVO, getUsuarioLogado(), true);
            setEstoqueVO(new EstoqueVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "EstoqueControle", "Finalizando Excluir Estoque", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueForm");
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
     * Método responsável por processar a consulta na entidade <code>Produto</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarProdutoPorChavePrimaria() {
        try {
            Integer campoConsulta = estoqueVO.getProduto().getCodigo();
            ProdutoServicoVO produto = getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            estoqueVO.getProduto().setNome(produto.getNome());
            this.setProduto_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            estoqueVO.getProduto().setNome("");
            estoqueVO.getProduto().setCodigo(0);
            this.setProduto_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUnidadeEnsinoPorChavePrimaria() {
        try {
            Integer campoConsulta = estoqueVO.getUnidadeEnsino().getCodigo();
            UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            estoqueVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
            this.setUnidadeEnsino_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            estoqueVO.getUnidadeEnsino().setNome("");
            estoqueVO.getUnidadeEnsino().setCodigo(0);
            this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomeProduto", "Produto"));
        itens.add(new SelectItem("categoriaProduto", "Categoria Produto"));
        return itens;
    }

    public List getTipoConsultaEstoqueCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("IEM", "Abaixo Estoque Mínimo"));
        itens.add(new SelectItem("SEM", "Acima Estoque Mínimo"));
        itens.add(new SelectItem("TO", "Todos"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("estoqueCons");
    }

    public String getProduto_Erro() {
        return produto_Erro;
    }

    public void setProduto_Erro(String produto_Erro) {
        this.produto_Erro = produto_Erro;
    }

    public String getUnidadeEnsino_Erro() {
        return unidadeEnsino_Erro;
    }

    public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
        this.unidadeEnsino_Erro = unidadeEnsino_Erro;
    }

    public EstoqueVO getEstoqueVO() {
    	if(estoqueVO == null ){
    		estoqueVO = new EstoqueVO();
    	}
        return estoqueVO;
    }

    public void setEstoqueVO(EstoqueVO estoqueVO) {
        this.estoqueVO = estoqueVO;
    }

    /**
     * @return the tipoEstoque
     */
    public String getTipoEstoque() {
        return tipoEstoque;
    }

    /**
     * @param tipoEstoque
     *            the tipoEstoque to set
     */
    public void setTipoEstoque(String tipoEstoque) {
        this.tipoEstoque = tipoEstoque;
    }
    
	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

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

	public Boolean getExisteUnidadeEnsino() {
		if (existeUnidadeEnsino == null) {
			existeUnidadeEnsino = false;
		}
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
	}

}
