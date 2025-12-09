package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas condicaoPagamentoForm.jsp
 * condicaoPagamentoCons.jsp) com as funcionalidades da classe <code>CondicaoPagamento</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CondicaoPagamento
 * @see CondicaoPagamentoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Lazy
@Scope("viewScope")
@Controller("CondicaoPagamentoControle")
public class CondicaoPagamentoControle extends SuperControle implements Serializable {

    private CondicaoPagamentoVO condicaoPagamentoVO;
    private ParcelaCondicaoPagamentoVO parcelaCondicaoPagamentoVO;

    public CondicaoPagamentoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>CondicaoPagamento</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() throws Exception{
        registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Novo Codição Pagamento", "Novo");
        removerObjetoMemoria(this);
        setCondicaoPagamentoVO(new CondicaoPagamentoVO());
        setParcelaCondicaoPagamentoVO(new ParcelaCondicaoPagamentoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CondicaoPagamento</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Inicializando Editar Codição Pagamento", "Editando");
        CondicaoPagamentoVO obj = (CondicaoPagamentoVO) context().getExternalContext().getRequestMap().get("condicaoPagamentoItem");
        obj.setNovoObj(Boolean.FALSE);
        setCondicaoPagamentoVO(getFacadeFactory().getCondicaoPagamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        setParcelaCondicaoPagamentoVO(new ParcelaCondicaoPagamentoVO());
        registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Finalizando Editar Codição Pagamento", "Editando");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CondicaoPagamento</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getCondicaoPagamentoVO().isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Inicializando Incluir Codição Pagamento", "Incluindo");
                getFacadeFactory().getCondicaoPagamentoFacade().incluir(getCondicaoPagamentoVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Finalizando Incluir Codição Pagamento", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Inicializando Alterar Codição Pagamento", "Alterando");
                getFacadeFactory().getCondicaoPagamentoFacade().alterar(getCondicaoPagamentoVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Finalizando Alterar Codição Pagamento", "Alterando");
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CondicaoPagamentoCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Inicializando Consultar Codição Pagamento", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCondicaoPagamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getCondicaoPagamentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Finalizando Consultar Codição Pagamento", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CondicaoPagamentoVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Inicializando Excluir Codição Pagamento", "Excluindo");
            getFacadeFactory().getCondicaoPagamentoFacade().excluir(getCondicaoPagamentoVO(), getUsuarioLogado());
            setCondicaoPagamentoVO(new CondicaoPagamentoVO());
            setParcelaCondicaoPagamentoVO(new ParcelaCondicaoPagamentoVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "CondicaoPagamentoControle", "Finalizando Excluir Codição Pagamento", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ParcelaCondicaoPagamento</code> para o objeto
     * <code>condicaoPagamentoVO</code> da classe <code>CondicaoPagamento</code>
     */
    public String adicionarParcelaCondicaoPagamento() throws Exception {
        try {
        	CondicaoPagamentoVO.validarDadosAdicaoParcelaCondicaoPagamento(getCondicaoPagamentoVO());
            if (!getCondicaoPagamentoVO().getCodigo().equals(0)) {
                getParcelaCondicaoPagamentoVO().setCondicaoPagamento(condicaoPagamentoVO.getCodigo());
            }
            if (!getCondicaoPagamentoVO().getNrParcela().equals(0)) {
                condicaoPagamentoVO.montarListaParcelaCondicaoPagamento();
            }
            setMensagemID("msg_dados_adicionados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ParcelaCondicaoPagamento</code> para
     * edição pelo usuário.
     */
    public String editarParcelaCondicaoPagamento() throws Exception {
        ParcelaCondicaoPagamentoVO obj = (ParcelaCondicaoPagamentoVO) context().getExternalContext().getRequestMap().get("parcelaCondicaoPagamentoItem");
        setParcelaCondicaoPagamentoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ParcelaCondicaoPagamento</code> do objeto
     * <code>condicaoPagamentoVO</code> da classe <code>CondicaoPagamento</code>
     */
    public String removerParcelaCondicaoPagamento() throws Exception {
        ParcelaCondicaoPagamentoVO obj = (ParcelaCondicaoPagamentoVO) context().getExternalContext().getRequestMap().get("parcelaCondicaoPagamentoItem");
        getCondicaoPagamentoVO().excluirObjParcelaCondicaoPagamentoVOs(obj.getNumeroParcela());
        setMensagemID("msg_dados_excluidos");
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoForm.xhtml");
    }

    public void irPaginaInicial() throws Exception {
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoDespesaFinanceira</code>
     */
    public List getListaSelectItemTipoDespesaFinanceiraCondicaoPagamento() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoDespesaFinanceiras = (Hashtable) Dominios.getTipoDespesaFinanceira();
        Enumeration keys = tipoDespesaFinanceiras.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoDespesaFinanceiras.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>presenteTabelaPreco</code>
     */
    public List getListaSelectItemPresenteTabelaPrecoCondicaoPagamento() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable simNaos = (Hashtable) Dominios.getSimNao();
        Enumeration keys = simNaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) simNaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoPagamentoCons.xhtml");
    }

    public ParcelaCondicaoPagamentoVO getParcelaCondicaoPagamentoVO() {
        if (parcelaCondicaoPagamentoVO == null) {
            parcelaCondicaoPagamentoVO = new ParcelaCondicaoPagamentoVO();
        }
        return parcelaCondicaoPagamentoVO;
    }

    public void setParcelaCondicaoPagamentoVO(ParcelaCondicaoPagamentoVO parcelaCondicaoPagamentoVO) {
        this.parcelaCondicaoPagamentoVO = parcelaCondicaoPagamentoVO;
    }

    public CondicaoPagamentoVO getCondicaoPagamentoVO() {
        if (condicaoPagamentoVO == null) {
            condicaoPagamentoVO = new CondicaoPagamentoVO();
        }
        return condicaoPagamentoVO;
    }

    public void setCondicaoPagamentoVO(CondicaoPagamentoVO condicaoPagamentoVO) {
        this.condicaoPagamentoVO = condicaoPagamentoVO;
    }
    
    public String getIntervaloParcelaCondicaoPagamentoObrigatorio() {
    	return getCondicaoPagamentoVO().getNrParcela() > 1 ? "form-control camposObrigatorios" : "form-control campos";
    }
}
