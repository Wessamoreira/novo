package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas creditoFornecedorForm.jsp
 * creditoFornecedorCons.jsp) com as funcionalidades da classe <code>CreditoFornecedor</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CreditoFornecedor
 * @see CreditoFornecedorVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CreditoFornecedorVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("CreditoFornecedorControle")
@Scope("request")
@Lazy
public class CreditoFornecedorControle extends SuperControle implements Serializable {

    private CreditoFornecedorVO creditoFornecedorVO;
    protected List listaSelectItemResponsavelCadastro;
    private String campoConsultarFornecedor;
    private String valorConsultarFornecedor;
    private List listaConsultarFornecedor;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemResponsavelDevolucao;
    protected List listaSelectItemContaCorrente;

    public CreditoFornecedorControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>CreditoFornecedor</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setCreditoFornecedorVO(new CreditoFornecedorVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CreditoFornecedor</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        CreditoFornecedorVO obj = (CreditoFornecedorVO) context().getExternalContext().getRequestMap().get("creditoFornecedor");
        obj.setNovoObj(Boolean.FALSE);
        setCreditoFornecedorVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CreditoFornecedor</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (creditoFornecedorVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getCreditoFornecedorFacade().incluir(creditoFornecedorVO);
            } else {
                getFacadeFactory().getCreditoFornecedorFacade().alterar(creditoFornecedorVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CreditoFornecedorCons.jsp. Define o tipo de
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
                objs = getFacadeFactory().getCreditoFornecedorFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getCreditoFornecedorFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeFornecedor")) {
                objs = getFacadeFactory().getCreditoFornecedorFacade().consultarPorNomeFornecedor(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CreditoFornecedorVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getCreditoFornecedorFacade().excluir(creditoFornecedorVO);
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ContaCorrente</code>.
     */
    public void montarListaSelectItemContaCorrente(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCorrentePorNumero(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNumero().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemContaCorrente(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ContaCorrente</code>. Buscando todos os
     * objetos correspondentes a entidade <code>ContaCorrente</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemContaCorrente() {
        try {
            montarListaSelectItemContaCorrente("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarContaCorrentePorNumero(String numeroPrm) throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero(numeroPrm, this.getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ResponsavelDevolucao</code>.
     */
    public void montarListaSelectItemResponsavelDevolucao(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUsuarioPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UsuarioVO obj = (UsuarioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemResponsavelDevolucao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ResponsavelDevolucao</code>. Buscando
     * todos os objetos correspondentes a entidade <code>Usuario</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemResponsavelDevolucao() {
        try {
            montarListaSelectItemResponsavelDevolucao("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Fornecedor</code> por meio dos parametros
     * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
     * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarFornecedor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarFornecedor().equals("codigo")) {
                if (getValorConsultarFornecedor().equals("")) {
                    setValorConsultarFornecedor("0");
                }
                int valorInt = Integer.parseInt(getValorConsultarFornecedor());
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultarFornecedor(), "AT", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultarFornecedor(), "AT", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarFornecedor().equals("RG")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultarFornecedor(), "AT", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultarFornecedor(), "AT", true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarFornecedor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFornecedor() throws Exception {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedor");
        if (getMensagemDetalhada().equals("")) {
            this.getCreditoFornecedorVO().setFornecedor(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarFornecedor());
        this.setValorConsultarFornecedor(null);
        this.setCampoConsultarFornecedor(null);
    }

    public void limparCampoFornecedor() {
        this.getCreditoFornecedorVO().setFornecedor(new FornecedorVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboFornecedor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ResponsavelCadastro</code>.
     */
    public void montarListaSelectItemResponsavelCadastro(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUsuarioPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UsuarioVO obj = (UsuarioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemResponsavelCadastro(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ResponsavelCadastro</code>. Buscando todos
     * os objetos correspondentes a entidade <code>Usuario</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemResponsavelCadastro() {
        try {
            montarListaSelectItemResponsavelCadastro("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUsuarioPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUsuarioFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemResponsavelCadastro();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemResponsavelDevolucao();
        montarListaSelectItemContaCorrente();
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
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomeFornecedor", "Fornecedor"));
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

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        creditoFornecedorVO = null;
        Uteis.liberarListaMemoria(listaSelectItemResponsavelCadastro);
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemResponsavelDevolucao);
        Uteis.liberarListaMemoria(listaSelectItemContaCorrente);
    }

    public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return (listaSelectItemContaCorrente);
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public List getListaSelectItemResponsavelDevolucao() {
        if (listaSelectItemResponsavelDevolucao == null) {
            listaSelectItemResponsavelDevolucao = new ArrayList(0);
        }
        return (listaSelectItemResponsavelDevolucao);
    }

    public void setListaSelectItemResponsavelDevolucao(List listaSelectItemResponsavelDevolucao) {
        this.listaSelectItemResponsavelDevolucao = listaSelectItemResponsavelDevolucao;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultarFornecedor() {
        return campoConsultarFornecedor;
    }

    public void setCampoConsultarFornecedor(String campoConsultarFornecedor) {
        this.campoConsultarFornecedor = campoConsultarFornecedor;
    }

    public String getValorConsultarFornecedor() {
        return valorConsultarFornecedor;
    }

    public void setValorConsultarFornecedor(String valorConsultarFornecedor) {
        this.valorConsultarFornecedor = valorConsultarFornecedor;
    }

    public List getListaConsultarFornecedor() {
        return listaConsultarFornecedor;
    }

    public void setListaConsultarFornecedor(List listaConsultarFornecedor) {
        this.listaConsultarFornecedor = listaConsultarFornecedor;
    }

    public List getListaSelectItemResponsavelCadastro() {
        if (listaSelectItemResponsavelCadastro == null) {
            listaSelectItemResponsavelCadastro = new ArrayList(0);
        }
        return (listaSelectItemResponsavelCadastro);
    }

    public void setListaSelectItemResponsavelCadastro(List listaSelectItemResponsavelCadastro) {
        this.listaSelectItemResponsavelCadastro = listaSelectItemResponsavelCadastro;
    }

    public CreditoFornecedorVO getCreditoFornecedorVO() {
        return creditoFornecedorVO;
    }

    public void setCreditoFornecedorVO(CreditoFornecedorVO creditoFornecedorVO) {
        this.creditoFornecedorVO = creditoFornecedorVO;
    }
}
