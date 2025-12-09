package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas registroSaidaAcervoForm.jsp
 * registroSaidaAcervoCons.jsp) com as funcionalidades da classe <code>RegistroSaidaAcervo</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see RegistroSaidaAcervo
 * @see RegistroSaidaAcervoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemRegistroSaidaAcervoVO;
import negocio.comuns.biblioteca.RegistroSaidaAcervoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoExemplar;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Controller("RegistroSaidaAcervoControle")
@Scope("viewScope")
@Lazy
public class RegistroSaidaAcervoControle extends SuperControle implements Serializable {

    private RegistroSaidaAcervoVO registroSaidaAcervoVO;
    private List listaSelectItemBiblioteca;
    private ItemRegistroSaidaAcervoVO itemRegistroSaidaAcervoVO;
    private String campoConsultarExemplar;
    private String valorConsultarExemplar;
    private List listaConsultarExemplar;
    private List listaSelectItemTipoSaidaItemRegistroSaidaAcervo;
    private ExemplarVO exemplarConsulta;
    private Date dataConsultaRegistroSaida;

    public RegistroSaidaAcervoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>RegistroSaidaAcervo</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        try {
            setRegistroSaidaAcervoVO(new RegistroSaidaAcervoVO());
            inicializarListasSelectItemTodosComboBox();
            setItemRegistroSaidaAcervoVO(new ItemRegistroSaidaAcervoVO());
            getFacadeFactory().getRegistroSaidaAcervoFacade().inicializarDadosRegistroSaidaAcervoNovo(getRegistroSaidaAcervoVO(), getUsuarioLogado());
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>RegistroSaidaAcervo</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            RegistroSaidaAcervoVO obj = (RegistroSaidaAcervoVO) context().getExternalContext().getRequestMap().get("registroSaidaAcervoItens");
            obj.setNovoObj(Boolean.FALSE);
            setRegistroSaidaAcervoVO(obj);
            inicializarListasSelectItemTodosComboBox();
            setItemRegistroSaidaAcervoVO(new ItemRegistroSaidaAcervoVO());
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>RegistroSaidaAcervo</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (registroSaidaAcervoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getRegistroSaidaAcervoFacade().incluir(registroSaidaAcervoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getRegistroSaidaAcervoFacade().alterar(registroSaidaAcervoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP RegistroSaidaAcervoCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List<RegistroSaidaAcervoVO> objs = new ArrayList<RegistroSaidaAcervoVO>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getRegistroSaidaAcervoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
                objs = getFacadeFactory().getRegistroSaidaAcervoFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
//                Date valorData = UteisData.getData(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getRegistroSaidaAcervoFacade().consultarPorData(Uteis.getDateTime(getDataConsultaRegistroSaida(), 0, 0, 0), Uteis.getDateTime(getDataConsultaRegistroSaida(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeBiblioteca")) {
                objs = getFacadeFactory().getRegistroSaidaAcervoFacade().consultarPorNomeBiblioteca(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tombo")) {
            	objs = getFacadeFactory().getRegistroSaidaAcervoFacade().consultarPorTombo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>RegistroSaidaAcervoVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getRegistroSaidaAcervoFacade().excluir(registroSaidaAcervoVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ItemRegistroSaidaAcervo</code> para o objeto
     * <code>registroSaidaAcervoVO</code> da classe <code>RegistroSaidaAcervo</code>
     */
    public String adicionarItemRegistroSaidaAcervo() throws Exception {
        try {
            if (!getRegistroSaidaAcervoVO().getCodigo().equals(0)) {
                itemRegistroSaidaAcervoVO.setRegistroSaidaAcervo(getRegistroSaidaAcervoVO().getCodigo());
            }
            if (getItemRegistroSaidaAcervoVO().getExemplar().getCodigo().intValue() != 0) {
				getFacadeFactory().getExemplarFacade().carregarDados(getItemRegistroSaidaAcervoVO().getExemplar(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());				
            }
            getRegistroSaidaAcervoVO().adicionarObjItemRegistroSaidaAcervoVOs(getItemRegistroSaidaAcervoVO());
            this.setItemRegistroSaidaAcervoVO(new ItemRegistroSaidaAcervoVO());
            setMensagemID("msg_dados_adicionados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ItemRegistroSaidaAcervo</code> para
     * edição pelo usuário.
     */
    public String editarItemRegistroSaidaAcervo() throws Exception {
        ItemRegistroSaidaAcervoVO obj = (ItemRegistroSaidaAcervoVO) context().getExternalContext().getRequestMap().get("itemRegistroSaidaAcervoItens");
        setItemRegistroSaidaAcervoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItemRegistroSaidaAcervo</code> do objeto
     * <code>registroSaidaAcervoVO</code> da classe <code>RegistroSaidaAcervo</code>
     */
    public String removerItemRegistroSaidaAcervo() throws Exception {
        ItemRegistroSaidaAcervoVO obj = (ItemRegistroSaidaAcervoVO) context().getExternalContext().getRequestMap().get("itemRegistroSaidaAcervoItens");
        getRegistroSaidaAcervoVO().excluirObjItemRegistroSaidaAcervoVOs(obj.getExemplar().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoForm.xhtml");
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Exemplar</code> por meio dos parametros informados
     * no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    
    public void consultarExemplarPeloCodigoBarras() {
		try {
			if (getItemRegistroSaidaAcervoVO().getExemplar().getCodigoBarra() != null && !getItemRegistroSaidaAcervoVO().getExemplar().getCodigoBarra().trim().isEmpty()) {
				Integer codBiblioteca = getRegistroSaidaAcervoVO().getBiblioteca().getCodigo();				
				getItemRegistroSaidaAcervoVO().setExemplar(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnicoBiblioteca(getItemRegistroSaidaAcervoVO().getExemplar().getCodigoBarra(), codBiblioteca, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				adicionarItemRegistroSaidaAcervo();
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {			
			
		}
	}

    /**
     * Método responsável por processar a consulta na entidade <code>Exemplar</code> por meio dos parametros informados
     * no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarExemplar() {        
        	try {
        		getControleConsultaOtimizado().setLimitePorPagina(5);
    			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getExemplarFacade().consultar("", null, getExemplarConsulta().getCatalogo().getTitulo(), getRegistroSaidaAcervoVO().getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, null, getUsuarioLogado()));
    			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultarTotalRegistro("", null, getExemplarConsulta().getCatalogo().getTitulo(), getRegistroSaidaAcervoVO().getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo()));
    			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
    		}
            
    }
    
	public void scrollListenerExemplar(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarExemplar();
	}

    public void selecionarExemplar() throws Exception {
        ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");
        if (getMensagemDetalhada().equals("")) {
            this.getItemRegistroSaidaAcervoVO().setExemplar(obj);
        }
        adicionarItemRegistroSaidaAcervo();
        setControleConsultaOtimizado(null);
    }
//

    public void limparCampoExemplar() {
        this.getItemRegistroSaidaAcervoVO().setExemplar(new ExemplarVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboExemplar() {
        List itens = new ArrayList(0);
//        itens.add(new SelectItem("codigo", "Código"));
//		itens.add(new SelectItem("nomeBiblioteca", "Biblioteca"));
//		itens.add(new SelectItem("obra", "Obra"));
//		itens.add(new SelectItem("tipoExemplar", "Tipo Exemplar"));
        itens.add(new SelectItem("codigoBarra", "Código de Barra"));
//        itens.add(new SelectItem("situacaoAtual", "Situação Atual"));
        return itens;
    }

    public void montarListaSelectItemTipoSaidaItemRegistroSaidaAcervo() throws Exception {
        setListaSelectItemTipoSaidaItemRegistroSaidaAcervo(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoSaidaAcervo.class, false));
    }

    /**
     * Métodos que montam a comboBox de Bibliotecas.
     * */
    public void montarListaSelectItemBiblioteca() throws Exception {
        try {
            List<BibliotecaVO> resultadoConsulta = consultarBibliotecaPorNome("");
            setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<BibliotecaVO> consultarBibliotecaPorNome(String nomePrm) throws Exception {
        List<BibliotecaVO> lista = getFacadeFactory().getBibliotecaFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
     *
     * @throws Exception
     */
    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemBiblioteca();
        montarListaSelectItemTipoSaidaItemRegistroSaidaAcervo();
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data")) {
            return "return mascara(this.form, 'form:valorConsulta', '99/99/9999', event);";
        }
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("tombo", "Tombo"));
        itens.add(new SelectItem("nomeBiblioteca", "Biblioteca"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomeUsuario", "Funcionário"));
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroSaidaAcervoCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        registroSaidaAcervoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemBiblioteca);
        itemRegistroSaidaAcervoVO = null;
    }

    public String getCampoConsultarExemplar() {
        return campoConsultarExemplar;
    }

    public void setCampoConsultarExemplar(String campoConsultarExemplar) {
        this.campoConsultarExemplar = campoConsultarExemplar;
    }

    public String getValorConsultarExemplar() {
        return valorConsultarExemplar;
    }

    public void setValorConsultarExemplar(String valorConsultarExemplar) {
        this.valorConsultarExemplar = valorConsultarExemplar;
    }

    public List getListaConsultarExemplar() {
        if (listaConsultarExemplar == null) {
            listaConsultarExemplar = new ArrayList(0);
        }
        return listaConsultarExemplar;
    }

    public void setListaConsultarExemplar(List listaConsultarExemplar) {
        this.listaConsultarExemplar = listaConsultarExemplar;
    }

    public ItemRegistroSaidaAcervoVO getItemRegistroSaidaAcervoVO() {
        if (itemRegistroSaidaAcervoVO == null) {
            itemRegistroSaidaAcervoVO = new ItemRegistroSaidaAcervoVO();
        }
        return itemRegistroSaidaAcervoVO;
    }

    public void setItemRegistroSaidaAcervoVO(ItemRegistroSaidaAcervoVO itemRegistroSaidaAcervoVO) {
        this.itemRegistroSaidaAcervoVO = itemRegistroSaidaAcervoVO;
    }

    public List getListaSelectItemBiblioteca() {
        if (listaSelectItemBiblioteca == null) {
            listaSelectItemBiblioteca = new ArrayList(0);
        }
        return (listaSelectItemBiblioteca);
    }

    public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
        this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
    }

    public RegistroSaidaAcervoVO getRegistroSaidaAcervoVO() {
        if (registroSaidaAcervoVO == null) {
            registroSaidaAcervoVO = new RegistroSaidaAcervoVO();
        }
        return registroSaidaAcervoVO;
    }

    public void setRegistroSaidaAcervoVO(RegistroSaidaAcervoVO registroSaidaAcervoVO) {
        this.registroSaidaAcervoVO = registroSaidaAcervoVO;
    }

    public void setListaSelectItemTipoSaidaItemRegistroSaidaAcervo(List listaSelectItemTipoSaidaItemRegistroSaidaAcervo) {
        this.listaSelectItemTipoSaidaItemRegistroSaidaAcervo = listaSelectItemTipoSaidaItemRegistroSaidaAcervo;
    }

    public List getListaSelectItemTipoSaidaItemRegistroSaidaAcervo() {
        if (listaSelectItemTipoSaidaItemRegistroSaidaAcervo == null) {
            listaSelectItemTipoSaidaItemRegistroSaidaAcervo = new ArrayList(0);
        }
        return listaSelectItemTipoSaidaItemRegistroSaidaAcervo;
    }
    

	public ExemplarVO getExemplarConsulta() {
		if(exemplarConsulta == null){
			exemplarConsulta = new ExemplarVO();
		}
		return exemplarConsulta;
	}

	public void setExemplarConsulta(ExemplarVO exemplarConsulta) {
		this.exemplarConsulta = exemplarConsulta;
	}
	

	public List<SelectItem> getListaSelectItemTipoExemplar() {
		return TipoExemplar.getListaSelectItemTipoExemplar();
	}
	
	public boolean getTesteBosta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}else {
			return false;
		}
	}

	public Date getDataConsultaRegistroSaida() {
		if (dataConsultaRegistroSaida == null) {
			dataConsultaRegistroSaida = new Date();
		}
		return dataConsultaRegistroSaida;
	}

	public void setDataConsultaRegistroSaida(Date dataConsultaRegistroSaida) {
		this.dataConsultaRegistroSaida = dataConsultaRegistroSaida;
	}
	
}
