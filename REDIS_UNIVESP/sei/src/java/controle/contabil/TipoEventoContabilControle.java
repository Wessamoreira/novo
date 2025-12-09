package controle.contabil;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas padronizadoForm.jsp
 * padronizadoCons.jsp) com as funcionalidades da classe <code>TipoEventoContabil</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see TipoEventoContabil
 * @see TipoEventoContabilVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.contabil.HistoricoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.TipoEventoContabilVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoEventoContabilControle")
@Scope("request")
@Lazy
public class TipoEventoContabilControle extends SuperControle implements Serializable {

    private TipoEventoContabilVO tipoEventoContabilVO;
    protected List listaSelectItemContaDebito;
    protected List listaSelectItemContaCredito;
    protected List listaSelectItemHistorico;
    private String campoConsultaContaDebito;
    private String valorConsultaContaDebito;
    private List listaConsultaContaDebito;
    private String campoConsultaContaCredito;
    private String valorConsultaContaCredito;
    private List listaConsultaContaCredito;
    private String campoConsultaHistorico;
    private String valorConsultaHistorico;
    private List listaConsultaHistorico;
    private String descricaoContaDebito;
    private String descricaoContaCredito;
    private String descricaoHistorico;

    public TipoEventoContabilControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TipoEventoContabil</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setTipoEventoContabilVO(new TipoEventoContabilVO());
        inicializarListasSelectItemTodosComboBox();
        this.setDescricaoContaCredito("");
        this.setDescricaoContaDebito("");
        this.setDescricaoHistorico("");
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TipoEventoContabil</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        TipoEventoContabilVO obj = (TipoEventoContabilVO) context().getExternalContext().getRequestMap().get("padronizado");
        obj.setNovoObj(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
        try {
            setTipoEventoContabilVO(getFacadeFactory().getTipoEventoContabilFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
        this.setDescricaoContaDebito(obj.getContaDebito().getIdentificadorPlanoConta() + " - " + obj.getContaDebito().getDescricao());
        this.setDescricaoContaCredito(obj.getContaCredito().getIdentificadorPlanoConta() + " - " + obj.getContaCredito().getDescricao());
        this.setDescricaoHistorico(obj.getHistorico().getDescricao());
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TipoEventoContabil</code>
     * . Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário
     * é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (tipoEventoContabilVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getTipoEventoContabilFacade().incluir(tipoEventoContabilVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getTipoEventoContabilFacade().alterar(tipoEventoContabilVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TipoEventoContabilCons.jsp. Define o tipo de
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
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("contaCredito")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorTipoPlanoContaCredito(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("contaDebito")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorTipoPlanoContaDebito(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricaoHistorico")) {
                objs = getFacadeFactory().getTipoEventoContabilFacade().consultarPorDescricaoHistorico(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void consultarContaDebito() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaContaDebito().equals("codigo")) {
                if (getValorConsultaContaDebito().equals("")) {
                    setValorConsultaContaDebito("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaContaDebito());
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoLogado().getCodigo().intValue(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaContaDebito().equals("descricao")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorDescricao(getValorConsultaContaDebito(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaContaDebito().equals("identificadorPlanoConta")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(getValorConsultaContaDebito(), getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaContaDebito(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaContaDebito(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarContaDebito() throws Exception {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("contaDebito");
        if (getMensagemDetalhada().equals("")) {
            this.getTipoEventoContabilVO().setContaDebito(obj);
            this.setDescricaoContaDebito(obj.getIdentificadorPlanoConta() + " - " + obj.getDescricao());

        }
        this.getListaConsultaContaDebito().clear();
        obj = null;
        this.setValorConsultaContaDebito(null);
        this.setCampoConsultaContaDebito(null);
    }

    public List getTipoConsultaComboContaDebito() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Conta"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));

        return itens;
    }

    public void consultarContaCredito() {
        try {            
            setListaConsultaContaCredito(getFacadeFactory().getPlanoContaFacade().consultar(getUnidadeEnsinoLogado().getCodigo(), getCampoConsultaContaCredito(), getValorConsultaContaCredito(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaContaCredito(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarContaCredito() throws Exception {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("contaCredito");
        if (getMensagemDetalhada().equals("")) {
            this.getTipoEventoContabilVO().setContaCredito(obj);
            this.setDescricaoContaCredito(obj.getIdentificadorPlanoConta() + " - " + obj.getDescricao());
        }
        this.getListaConsultaContaCredito().clear();
        obj = null;
        this.setValorConsultaContaCredito(null);
        this.setCampoConsultaContaCredito(null);
    }

    public List getTipoConsultaComboContaCredito() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Conta"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));

        return itens;
    }

    public void consultarHistorico() {
        try {
            List objs = new ArrayList(0);

            if (getCampoConsultaHistorico().equals("codigo")) {
                if (getValorConsultaHistorico().equals("")) {
                    setValorConsultaHistorico("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaHistorico());
                objs = getFacadeFactory().getHistoricoContabilFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
            }
            if (getCampoConsultaHistorico().equals("descricao")) {
                objs = getFacadeFactory().getHistoricoContabilFacade().consultarPorDescricao(getValorConsultaHistorico(), true, getUsuarioLogado());
            }
            setListaConsultaHistorico(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaHistorico(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarHistorico() throws Exception {
        HistoricoContabilVO obj = (HistoricoContabilVO) context().getExternalContext().getRequestMap().get("historico");
        if (getMensagemDetalhada().equals("")) {
            this.getTipoEventoContabilVO().setHistorico(obj);
            this.setDescricaoHistorico(obj.getDescricao());
        }
        this.getListaConsultaHistorico().clear();
        obj = null;
        this.setValorConsultaHistorico(null);
        this.setCampoConsultaHistorico(null);
    }

    public List getTipoConsultaComboHistorico() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TipoEventoContabilVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getTipoEventoContabilFacade().excluir(tipoEventoContabilVO, getUsuarioLogado());
            this.setDescricaoContaCredito("");
            this.setDescricaoContaDebito("");
            this.setDescricaoHistorico("");
            setTipoEventoContabilVO(new TipoEventoContabilVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Historico</code>.
     */
    public void montarListaSelectItemHistorico(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarHistoricoPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                HistoricoContabilVO obj = (HistoricoContabilVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemHistorico(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Historico</code>. Buscando todos os
     * objetos correspondentes a entidade <code>Historico</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemHistorico() {
        try {
            montarListaSelectItemHistorico("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarHistoricoPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getHistoricoContabilFacade().consultarPorDescricao(descricaoPrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaDebito</code>.
     */
    public void montarListaSelectItemContaDebito(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPlanoContaDebitoPorIdentificadorPlanoConta(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoContaVO obj = (PlanoContaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorPlanoConta().toString() + " - " + obj.getDescricao()));
            }
            setListaSelectItemContaDebito(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaDebito</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PlanoConta</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaDebito() {
        try {
            montarListaSelectItemContaDebito("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorPlanoConta</code> Este atributo é uma lista (
     * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPlanoContaDebitoPorIdentificadorPlanoConta(String identificadorPlanoContaPrm) throws Exception {
        List lista = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(identificadorPlanoContaPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaDebito</code>.
     */
    public void montarListaSelectItemContaCredito(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPlanoContaCreditoPorIdentificadorPlanoConta(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoContaVO obj = (PlanoContaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorPlanoConta().toString() + " - " + obj.getDescricao()));
            }
            setListaSelectItemContaCredito(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaDebito</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PlanoConta</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCredito() {
        try {
            montarListaSelectItemContaCredito("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorPlanoConta</code> Este atributo é uma lista (
     * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPlanoContaCreditoPorIdentificadorPlanoConta(String identificadorPlanoContaPrm) throws Exception {
        List lista = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(identificadorPlanoContaPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemContaDebito();
        montarListaSelectItemContaCredito();
        montarListaSelectItemHistorico();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("contaCredito", "Conta Crédito"));
        itens.add(new SelectItem("contaDebito", "Conta Débito"));
        itens.add(new SelectItem("descricaoHistorico", "Histórico"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public List getListaSelectItemHistorico() {
        return (listaSelectItemHistorico);
    }

    public void setListaSelectItemHistorico(List listaSelectItemHistorico) {
        this.listaSelectItemHistorico = listaSelectItemHistorico;
    }

    public List getListaSelectItemContaDebito() {
        return (listaSelectItemContaDebito);
    }

    public void setListaSelectItemContaDebito(List listaSelectItemContaDebito) {
        this.listaSelectItemContaDebito = listaSelectItemContaDebito;
    }

    public TipoEventoContabilVO getTipoEventoContabilVO() {
        return tipoEventoContabilVO;
    }

    public void setTipoEventoContabilVO(TipoEventoContabilVO tipoEventoContabilVO) {
        this.tipoEventoContabilVO = tipoEventoContabilVO;
    }

    public List getListaSelectItemContaCredito() {
        return listaSelectItemContaCredito;
    }

    public void setListaSelectItemContaCredito(List listaSelectItemContaCredito) {
        this.listaSelectItemContaCredito = listaSelectItemContaCredito;
    }

    public String getCampoConsultaContaDebito() {
        return campoConsultaContaDebito;
    }

    public void setCampoConsultaContaDebito(String campoConsultaContaDebito) {
        this.campoConsultaContaDebito = campoConsultaContaDebito;
    }

    public String getValorConsultaContaDebito() {
        return valorConsultaContaDebito;
    }

    public void setValorConsultaContaDebito(String valorConsultaContaDebito) {
        this.valorConsultaContaDebito = valorConsultaContaDebito;
    }

    public List getListaConsultaContaDebito() {
        return listaConsultaContaDebito;
    }

    public void setListaConsultaContaDebito(List listaConsultaContaDebito) {
        this.listaConsultaContaDebito = listaConsultaContaDebito;
    }

    public String getCampoConsultaContaCredito() {
        return campoConsultaContaCredito;
    }

    public void setCampoConsultaContaCredito(String campoConsultaContaCredito) {
        this.campoConsultaContaCredito = campoConsultaContaCredito;
    }

    public String getValorConsultaContaCredito() {
        return valorConsultaContaCredito;
    }

    public void setValorConsultaContaCredito(String valorConsultaContaCredito) {
        this.valorConsultaContaCredito = valorConsultaContaCredito;
    }

    public List getListaConsultaContaCredito() {
        return listaConsultaContaCredito;
    }

    public void setListaConsultaContaCredito(List listaConsultaContaCredito) {
        this.listaConsultaContaCredito = listaConsultaContaCredito;
    }

    public String getCampoConsultaHistorico() {
        return campoConsultaHistorico;
    }

    public void setCampoConsultaHistorico(String campoConsultaHistorico) {
        this.campoConsultaHistorico = campoConsultaHistorico;
    }

    public String getValorConsultaHistorico() {
        return valorConsultaHistorico;
    }

    public void setValorConsultaHistorico(String valorConsultaHistorico) {
        this.valorConsultaHistorico = valorConsultaHistorico;
    }

    public List getListaConsultaHistorico() {
        return listaConsultaHistorico;
    }

    public void setListaConsultaHistorico(List listaConsultaHistorico) {
        this.listaConsultaHistorico = listaConsultaHistorico;
    }

    public String getDescricaoContaDebito() {
        return descricaoContaDebito;
    }

    public void setDescricaoContaDebito(String descricaoContaDebito) {
        this.descricaoContaDebito = descricaoContaDebito;
    }

    public String getDescricaoContaCredito() {
        return descricaoContaCredito;
    }

    public void setDescricaoContaCredito(String descricaoContaCredito) {
        this.descricaoContaCredito = descricaoContaCredito;
    }

    public String getDescricaoHistorico() {
        return descricaoHistorico;
    }

    public void setDescricaoHistorico(String descricaoHistorico) {
        this.descricaoHistorico = descricaoHistorico;
    }
}
