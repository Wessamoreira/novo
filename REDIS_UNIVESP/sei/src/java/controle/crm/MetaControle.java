package controle.crm;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.crm.MetaItemVO;
import negocio.comuns.crm.MetaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * metaForm.jsp metaCons.jsp) com as funcionalidades da classe <code>Meta</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Meta
 * @see MetaVO
 */
@Controller("MetaControle")
@Scope("viewScope")
@Lazy
public class MetaControle extends SuperControle {

    private MetaVO metaVO;
    protected List listaSelectItemCargo;
    private MetaItemVO metaItemVO;
    private Boolean padraoUnico;

    public MetaControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Meta</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        setPadraoUnico(true);
        setMetaVO(new MetaVO());
        setMetaItemVO(new MetaItemVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("metaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Meta</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        MetaVO obj = (MetaVO) context().getExternalContext().getRequestMap().get("metaItens");
        setMetaVO(getFacadeFactory().getMetaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(new Boolean(false));
        setMetaVO(obj);
        Iterator i = getMetaVO().getListaMetaItem().iterator();
        while (i.hasNext()) {
            MetaItemVO metaPadrao = (MetaItemVO) i.next();
            if (metaPadrao.getPadrao()) {
                setPadraoUnico(false);
            }
        }
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("metaForm.xhtml");
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>MetaVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(MetaVO obj) {
        if (obj.getCargo() == null) {
            obj.setCargo(new CargoVO());
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Meta</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getMetaFacade().persistir(metaVO, metaVO.getListaMetaItem(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("metaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MetaCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getMetaFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("metaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>MetaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getMetaFacade().validarDadosExclusao(getMetaVO());
            getFacadeFactory().getMetaFacade().excluir(getMetaVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("metaForm.xhtml");
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Cargo</code>.
     */
    public void montarListaSelectItemCargo(String prm) throws Exception {
        List resultadoConsulta = consultarCargoPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            CargoVO obj = (CargoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        setListaSelectItemCargo(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cargo</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Cargo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCargo() {
        try {
            montarListaSelectItemCargo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarCargoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCargoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCargo();
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("metaCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        metaVO = null;
        Uteis.liberarListaMemoria(listaSelectItemCargo);
    }

    public List getListaSelectItemCargo() {
        if (listaSelectItemCargo == null) {
            listaSelectItemCargo = new ArrayList(0);
        }
        return (listaSelectItemCargo);
    }

    public void setListaSelectItemCargo(List listaSelectItemCargo) {
        this.listaSelectItemCargo = listaSelectItemCargo;
    }

    public MetaVO getMetaVO() {
        if (metaVO == null) {
            metaVO = new MetaVO();
        }
        return metaVO;
    }

    public void setMetaVO(MetaVO metaVO) {
        this.metaVO = metaVO;
    }

    public MetaItemVO getMetaItemVO() {
        if (metaItemVO == null) {
            metaItemVO = new MetaItemVO();
        }
        return metaItemVO;
    }

    public void setMetaItemVO(MetaItemVO metaItemVO) {
        this.metaItemVO = metaItemVO;
    }

    /* Método responsável por adicionar um novo objeto da classe <code>RegistroEntradaProspects</code>
     * para o objeto <code>registroEntradaVO</code> da classe <code>RegistroEntrada</code>
     */
    public void adicionarMetaItem() throws Exception {
        try {
            if (!getMetaVO().getCodigo().equals(0)) {
                metaItemVO.setMeta(getMetaVO());
            }
            getFacadeFactory().getMetaItemFacade().adicionarObjMetaItemVOs(getMetaVO(), getMetaItemVO());
            this.setMetaItemVO(new MetaItemVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
            Iterator i = getMetaVO().getListaMetaItem().iterator();
            while (i.hasNext()) {
                MetaItemVO metaPadrao = (MetaItemVO) i.next();
                if (metaPadrao.getPadrao()) {
                    setPadraoUnico(false);
                    return;
                }
            }
            setPadraoUnico(true);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void alterarMetaItem() throws Exception {
        MetaItemVO obj = (MetaItemVO) context().getExternalContext().getRequestMap().get("metaItemItens");
        setMetaItemVO(obj);
        setMensagemID("msg_dados_editar", Uteis.SUCESSO);
        if (obj.getPadrao()) {
            setPadraoUnico(true);
        } else {
            Iterator i = getMetaVO().getListaMetaItem().iterator();
            while (i.hasNext()) {
                MetaItemVO metaPadrao = (MetaItemVO) i.next();
                if (metaPadrao.getPadrao()) {
                    setPadraoUnico(false);
                    return;
                }
            }
        }
        setPadraoUnico(true);
    }

    public void removerMetaItem() throws Exception {
        MetaItemVO obj = (MetaItemVO) context().getExternalContext().getRequestMap().get("metaItemItens");
        setPadraoUnico(getMetaItemVO().getPadrao());
        getFacadeFactory().getMetaItemFacade().excluirObjMetaItemVOs(getMetaVO(), obj);
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
    }

    public Boolean getPadraoUnico() {
        if (padraoUnico == null) {
            padraoUnico = Boolean.TRUE;
        }
        return padraoUnico;
    }

    public void setPadraoUnico(Boolean padraoUnico) {
        this.padraoUnico = padraoUnico;
    }
}
