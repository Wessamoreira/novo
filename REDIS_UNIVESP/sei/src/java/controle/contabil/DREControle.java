package controle.contabil;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas dREForm.jsp dRECons.jsp) com as
 * funcionalidades da classe <code>DRE</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see DRE
 * @see DREVO
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
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.contabil.DREVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem; @Controller("DREControle")
@Scope("request")
@Lazy
public class DREControle extends SuperControle implements Serializable {

    private DREVO dREVO;
    protected List<SelectItem> listaSelectItemUnidadeEnsino;
    private String campoConsultarPlanoConta;
    private String valorConsultarPlanoConta;
    private List<PlanoContaVO> listaConsultarPlanoConta;
    private List<DREVO> listaDRE;
    private boolean selecionarUnidadeEnsino;

    public DREControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        novo();
        setMensagemID("msg_entre_prmconsulta");
        validarMostrarComboUnidadeEnsino();
    }

    public void validarMostrarComboUnidadeEnsino() throws Exception {
        if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
            setSelecionarUnidadeEnsino(false);
        } else {
            setSelecionarUnidadeEnsino(true);
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>DRE</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() throws Exception {         removerObjetoMemoria(this);
        setDREVO(new DREVO());
        setListaDRE(new ArrayList<DREVO>(0));
        inicializarListasSelectItemTodosComboBox();
        validarMostrarComboUnidadeEnsino();
        montarDREUnidadeEnsino();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>DRE</code> para alteração. O objeto
     * desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        DREVO obj = (DREVO) context().getExternalContext().getRequestMap().get("dRE");
        obj.setNovoObj(Boolean.FALSE);
        setDREVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>DRE</code>. Caso o objeto
     * seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
     * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o
     * usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                getFacadeFactory().getDREFacade().incluirDRE(getListaDRE(), getUnidadeEnsinoLogado().getCodigo());
            } else {
                getFacadeFactory().getDREFacade().incluirDRE(getListaDRE(), getDREVO().getUnidadeEnsino().getCodigo());
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

     /**
     * Operação responsável por processar a exclusão um objeto da classe <code>DREVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getDREFacade().excluir(dREVO);
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void montarDREUnidadeEnsino() throws Exception {
        List<DREVO> lista = new ArrayList<DREVO>(0);
        if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
            lista = getFacadeFactory().getDREFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,getUsuarioLogado());
        } else {
            lista = getFacadeFactory().getDREFacade().consultarPorUnidadeEnsino(getDREVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,getUsuarioLogado());
        }
        setListaDRE(lista);
    }

    public void moverParaBaixo() {
        getFacadeFactory().getDREFacade().moverParaBaixo((DREVO) context().getExternalContext().getRequestMap().get("dre"), this.getListaDRE());
    }

    public void moverParaCima() {
        getFacadeFactory().getDREFacade().moverParaCima((DREVO) context().getExternalContext().getRequestMap().get("dre"), this.getListaDRE());
    }

    /**
     * Método responsável por processar a consulta na entidade <code>PlanoConta</code> por meio dos parametros
     * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
     * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     * @throws Exception
     */
    public void consultarPlanoConta() throws Exception {
        Integer unidadeEnsino = 0;
        if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
            unidadeEnsino = getDREVO().getUnidadeEnsino().getCodigo();
        } else {
            unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
        }
        try {
            List<PlanoContaVO> objs = new ArrayList<PlanoContaVO>(0);
            if (getCampoConsultarPlanoConta().equals("codigo")) {
                if (getValorConsultarPlanoConta().equals("")) {
                    setValorConsultarPlanoConta("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarPlanoConta());
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorCodigo(new Integer(valorInt), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPlanoConta().equals("planoContaPrincipal")) {
                if (getValorConsultarPlanoConta().equals("")) {
                    setValorConsultarPlanoConta("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarPlanoConta());
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorPlanoContaPrincipal(new Integer(valorInt), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPlanoConta().equals("identificadorPlanoConta")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorIdentificadorPlanoConta(getValorConsultarPlanoConta(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPlanoConta().equals("descricao")) {
                objs = getFacadeFactory().getPlanoContaFacade().consultarPorDescricao(getValorConsultarPlanoConta(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            List<PlanoContaVO> novaLista = new ArrayList<PlanoContaVO>(0);
            for (PlanoContaVO obj : objs) {
                Integer quantidadePontos = Uteis.contarQuantidadeDePontos(obj.getIdentificadorPlanoConta(), ".");
                if (quantidadePontos != 1) {
                    continue;
                } else {
                    if (obj.getIdentificadorPlanoConta().startsWith("1.")
                            || obj.getIdentificadorPlanoConta().startsWith("01.")
                            || obj.getIdentificadorPlanoConta().startsWith("001.")
                            || obj.getIdentificadorPlanoConta().startsWith("2.")
                            || obj.getIdentificadorPlanoConta().startsWith("02.")
                            || obj.getIdentificadorPlanoConta().startsWith("002.")) {
                        continue;
                    }
                }
                novaLista.add(obj);
            }
            setListaConsultarPlanoConta(novaLista);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarPlanoConta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void adicionarPlanoConta() {
        try {
            PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoConta");
            DREVO dre = new DREVO();
            int index = 0;
            dre.setPlanoConta(obj);
            dre.setOrdem(getListaDRE().size());
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                dre.getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
                dre.getUnidadeEnsino().setNome(getUnidadeEnsinoLogado().getNome());
            } else {
                dre.setUnidadeEnsino(getDREVO().getUnidadeEnsino());
            }
            Iterator i = getListaDRE().iterator();
            while (i.hasNext()) {
                DREVO objExistente = (DREVO) i.next();
                if (objExistente.getPlanoConta().getCodigo().equals(dre.getPlanoConta().getCodigo())) {
                    getListaDRE().set(index, dre);
                    getListaConsultarPlanoConta().remove(obj);
                    return;
                }
                index++;
            }
            getListaDRE().add(dre);
            getListaConsultarPlanoConta().remove(obj);
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void removerDRE() {
        try {
            DREVO obj = (DREVO) context().getExternalContext().getRequestMap().get("dre");
            getListaDRE().remove(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void limparListaPlanoConta() {
        setListaConsultarPlanoConta(new ArrayList(0));
    }

    public void selecionarPlanoConta() throws Exception {
        PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoConta");
        if (getMensagemDetalhada().equals("")) {
            this.getDREVO().setPlanoConta(obj);
        }   
        setListaConsultarPlanoConta(new ArrayList(0));
        this.setValorConsultarPlanoConta(null);
        this.setCampoConsultarPlanoConta(null);
    }

    public void limparCampoPlanoConta() {
        this.getDREVO().setPlanoConta(new PlanoContaVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboPlanoConta() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorPlanoConta", "Identificador do Plano de Contas"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("planoContaPrincipal", "Plano de Contas Principal"));
        itens.add(new SelectItem("descricao", "Descrição"));
        return itens;
    }

    public List getTipoSinal() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("DE", "Débito"));
        objs.add(new SelectItem("CR", "Crédito"));
        return objs;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>.
     * Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
        setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
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
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("ordem", "Ordem"));
        itens.add(new SelectItem("descricaoPlanoConta", "Plano de Contas"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        dREVO = null;
        setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
    }

    public String getCampoConsultarPlanoConta() {
        if (campoConsultarPlanoConta == null) {
            campoConsultarPlanoConta = "";
        }
        return campoConsultarPlanoConta;
    }

    public void setCampoConsultarPlanoConta(String campoConsultarPlanoConta) {
        this.campoConsultarPlanoConta = campoConsultarPlanoConta;
    }

    public String getValorConsultarPlanoConta() {
        if (valorConsultarPlanoConta == null) {
            valorConsultarPlanoConta = "";
        }
        return valorConsultarPlanoConta;
    }

    public void setValorConsultarPlanoConta(String valorConsultarPlanoConta) {
        this.valorConsultarPlanoConta = valorConsultarPlanoConta;
    }

    public List<PlanoContaVO> getListaConsultarPlanoConta() {
        if (listaConsultarPlanoConta == null) {
            listaConsultarPlanoConta = new ArrayList<PlanoContaVO>(0);
        }
        return listaConsultarPlanoConta;
    }

    public void setListaConsultarPlanoConta(List<PlanoContaVO> listaConsultarPlanoConta) {
        this.listaConsultarPlanoConta = listaConsultarPlanoConta;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public DREVO getDREVO() {
        if (dREVO == null) {
            dREVO = new DREVO();
        }
        return dREVO;
    }

    public void setDREVO(DREVO dREVO) {
        this.dREVO = dREVO;
    }

    public List<DREVO> getListaDRE() {
        if (listaDRE == null) {
            listaDRE = new ArrayList<DREVO>(0);
        }
        return listaDRE;
    }

    public void setListaDRE(List<DREVO> listaDRE) {
        this.listaDRE = listaDRE;
    }

    public boolean isSelecionarUnidadeEnsino() {
        return selecionarUnidadeEnsino;
    }

    public void setSelecionarUnidadeEnsino(boolean selecionarUnidadeEnsino) {
        this.selecionarUnidadeEnsino = selecionarUnidadeEnsino;
    }
}
