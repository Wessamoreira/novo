package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas planoFinanceiroAlunoForm.jsp
 * planoFinanceiroAlunoCons.jsp) com as funcionalidades da classe <code>PlanoFinanceiroAluno</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PlanoFinanceiroAluno
 * @see PlanoFinanceiroAlunoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("PlanoFinanceiroAlunoControle")
@Scope("request")
@Lazy
public class PlanoFinanceiroAlunoControle extends SuperControle implements Serializable {

    private PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO;
    private String matricula_Erro;
    private String descontoProgressivo_Erro;
    private String responsavel_Erro;
    private ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO;
    protected List listaSelectItemConvenio;
    protected List listaSelectItemPlanoDesconto;

    public PlanoFinanceiroAlunoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoFinanceiroAluno</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        inicializarListasSelectItemTodosComboBox();
        setPlanoFinanceiroAlunoVO(new PlanoFinanceiroAlunoVO());
        setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoFinanceiroAluno</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        PlanoFinanceiroAlunoVO obj = (PlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("planoFinanceiroAluno");
        obj.setNovoObj(Boolean.FALSE);
        setPlanoFinanceiroAlunoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>PlanoFinanceiroAluno</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (planoFinanceiroAlunoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPlanoFinanceiroAlunoFacade().incluir(planoFinanceiroAlunoVO);
            } else {
                getFacadeFactory().getPlanoFinanceiroAlunoFacade().alterar(planoFinanceiroAlunoVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PlanoFinanceiroAlunoCons.jsp. Define o tipo de
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
                objs = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
                objs = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
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
     * Operação responsável por processar a exclusão um objeto da classe <code>PlanoFinanceiroAlunoVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPlanoFinanceiroAlunoFacade().excluir(planoFinanceiroAlunoVO);
            setPlanoFinanceiroAlunoVO(new PlanoFinanceiroAlunoVO());

            setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ItemPlanoFinanceiroAluno</code> para o objeto
     * <code>planoFinanceiroAlunoVO</code> da classe <code>PlanoFinanceiroAluno</code>
     */
    public String adicionarItemPlanoFinanceiroAluno() throws Exception {
        try {
            if (!getPlanoFinanceiroAlunoVO().getCodigo().equals(0)) {
                itemPlanoFinanceiroAlunoVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO().getCodigo());
            }
            if (getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo().intValue() != 0) {
                Integer campoConsulta = getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo();
                PlanoDescontoVO planoDesconto = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                getItemPlanoFinanceiroAlunoVO().setPlanoDesconto(planoDesconto);
            }
            if (getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo().intValue() != 0) {
                Integer campoConvenio = getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo();
                ConvenioVO convenio = getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(campoConvenio, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getItemPlanoFinanceiroAlunoVO().setConvenio(convenio);
            }            
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs( getPlanoFinanceiroAlunoVO(), getItemPlanoFinanceiroAlunoVO());
            this.setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
            setMensagemID("msg_dados_adicionados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ItemPlanoFinanceiroAluno</code> para
     * edição pelo usuário.
     */
    public String editarItemPlanoFinanceiroAluno() throws Exception {
        ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
        setItemPlanoFinanceiroAlunoVO(obj);
        return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItemPlanoFinanceiroAluno</code> do objeto
     * <code>planoFinanceiroAlunoVO</code> da classe <code>PlanoFinanceiroAluno</code>
     */
    public String removerItemPlanoFinanceiroAluno() throws Exception {
        ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
        getPlanoFinanceiroAlunoVO().excluirObjItemPlanoFinanceiroAlunoVOs(obj.getPlanoDesconto().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return "editar";
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
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PlanoDesconto</code>.
     */
    public void montarListaSelectItemPlanoDesconto(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPlanoDescontoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemPlanoDesconto(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PlanoDesconto</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PlanoDesconto</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPlanoDesconto() {
        try {
            montarListaSelectItemPlanoDesconto("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPlanoDescontoFacade().consultarPorNome(nomePrm, 0, false, getUsuarioLogado(), 0, 0);
        return lista;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoItemPlanoFinanceiro</code>
     */
    public List getListaSelectItemTipoItemPlanoFinanceiroItemPlanoFinanceiroAluno() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoItemPlanoFinanceiroAlunos = (Hashtable) Dominios.getTipoItemPlanoFinanceiroAluno();
        Enumeration keys = tipoItemPlanoFinanceiroAlunos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoItemPlanoFinanceiroAlunos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Convenio</code>.
     */
    public void montarListaSelectItemConvenio(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarConvenioPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ConvenioVO obj = (ConvenioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemConvenio(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Convenio</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Convenio</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto
     * é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemConvenio() {
        try {
            montarListaSelectItemConvenio("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarConvenioPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getConvenioFacade().consultarPorDescricao(descricaoPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemConvenio();
        montarListaSelectItemPlanoDesconto();
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = planoFinanceiroAlunoVO.getResponsavel().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            planoFinanceiroAlunoVO.getResponsavel().setNome(pessoa.getNome());
            this.setResponsavel_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            planoFinanceiroAlunoVO.getResponsavel().setNome("");
            planoFinanceiroAlunoVO.getResponsavel().setCodigo(0);
            this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>DescontoProgressivo</code> por meio de sua
     * respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
     * chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarDescontoProgressivoPorChavePrimaria() {
        try {
            Integer campoConsulta = planoFinanceiroAlunoVO.getDescontoProgressivo().getCodigo();
            DescontoProgressivoVO descontoProgressivo = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
            planoFinanceiroAlunoVO.getDescontoProgressivo().setNome(descontoProgressivo.getNome());
            this.setDescontoProgressivo_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            planoFinanceiroAlunoVO.getDescontoProgressivo().setNome("");
            planoFinanceiroAlunoVO.getDescontoProgressivo().setCodigo(0);
            this.setDescontoProgressivo_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            // String campoConsulta = planoFinanceiroAlunoVO.getMatricula().getMatricula();
            // MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta);
            // planoFinanceiroAlunoVO.getMatricula().setMatricula(matricula.getMatricula());
            // this.setMatricula_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            planoFinanceiroAlunoVO.setMatricula("");
            planoFinanceiroAlunoVO.setMatricula("");
            this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomePessoa", "Responsável Descontos Aluno"));
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
        return "consultar";
    }

    public List getListaSelectItemPlanoDesconto() {
        return (listaSelectItemPlanoDesconto);
    }

    public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
        this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
    }

    public List getListaSelectItemConvenio() {
        return (listaSelectItemConvenio);
    }

    public void setListaSelectItemConvenio(List listaSelectItemConvenio) {
        this.listaSelectItemConvenio = listaSelectItemConvenio;
    }

    public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
        return itemPlanoFinanceiroAlunoVO;
    }

    public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
        this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
    }

    public String getResponsavel_Erro() {
        return responsavel_Erro;
    }

    public void setResponsavel_Erro(String responsavel_Erro) {
        this.responsavel_Erro = responsavel_Erro;
    }

    public String getDescontoProgressivo_Erro() {
        return descontoProgressivo_Erro;
    }

    public void setDescontoProgressivo_Erro(String descontoProgressivo_Erro) {
        this.descontoProgressivo_Erro = descontoProgressivo_Erro;
    }

    public String getMatricula_Erro() {
        return matricula_Erro;
    }

    public void setMatricula_Erro(String matricula_Erro) {
        this.matricula_Erro = matricula_Erro;
    }

    public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
        return planoFinanceiroAlunoVO;
    }

    public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) {
        this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        planoFinanceiroAlunoVO = null;
        matricula_Erro = null;
        descontoProgressivo_Erro = null;
        responsavel_Erro = null;
        itemPlanoFinanceiroAlunoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemConvenio);
        Uteis.liberarListaMemoria(listaSelectItemPlanoDesconto);
    }
}
