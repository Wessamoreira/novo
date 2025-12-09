package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas pgtoServicoAcademicoForm.jsp
 * pgtoServicoAcademicoCons.jsp) com as funcionalidades da classe <code>PgtoServicoAcademico</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PgtoServicoAcademico
 * @see PgtoServicoAcademicoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.PgtoServicoAcademicoVO;
import negocio.comuns.compras.SolicitacaoPgtoServicoAcademicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("PgtoServicoAcademicoControle")
@Scope("request")
@Lazy
public class PgtoServicoAcademicoControle extends SuperControle implements Serializable {

    private PgtoServicoAcademicoVO pgtoServicoAcademicoVO;
    private String solicitacaoPgtoServicoAcademico_Erro;
    private String centroCusto_Erro;
    private String unidadeEnsino_Erro;
    private String contaCorrente_Erro;
    protected List listaSelectItemCentroDespesa;

    public PgtoServicoAcademicoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>PgtoServicoAcademico</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setPgtoServicoAcademicoVO(new PgtoServicoAcademicoVO());
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PgtoServicoAcademico</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        PgtoServicoAcademicoVO obj = (PgtoServicoAcademicoVO) context().getExternalContext().getRequestMap().get("pgtoServicoAcademico");
        obj.setNovoObj(Boolean.FALSE);
        setPgtoServicoAcademicoVO(obj);
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>PgtoServicoAcademico</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (pgtoServicoAcademicoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPgtoServicoAcademicoFacade().incluir(pgtoServicoAcademicoVO);
            } else {
                getFacadeFactory().getPgtoServicoAcademicoFacade().alterar(pgtoServicoAcademicoVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PgtoServicoAcademicoCons.jsp. Define o tipo de
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
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricaoSolicitacaoPgtoServicoAcademico")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorDescricaoSolicitacaoPgtoServicoAcademico(getControleConsulta().getValorConsulta(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("identificadorCentroDespesaCentroDespesa")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorIdentificadorCentroDespesaCentroDespesa(getControleConsulta().getValorConsulta(), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoDestinatario")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorTipoDestinatario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("numeroContaCorrente")) {
                objs = getFacadeFactory().getPgtoServicoAcademicoFacade().consultarPorNumeroContaCorrente(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
     * Operação responsável por processar a exclusão um objeto da classe <code>PgtoServicoAcademicoVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPgtoServicoAcademicoFacade().excluir(pgtoServicoAcademicoVO);
            setPgtoServicoAcademicoVO(new PgtoServicoAcademicoVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
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

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoDestinatario</code>
     */
    public List getListaSelectItemTipoDestinatarioPgtoServicoAcademico() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoDestinatarioPgtServicoAcademicos = (Hashtable) Dominios.getTipoDestinatarioPgtServicoAcademico();
        Enumeration keys = tipoDestinatarioPgtServicoAcademicos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoDestinatarioPgtServicoAcademicos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoPgtoServicoAcademico() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoPgtServicoAcademicos = (Hashtable) Dominios.getSituacaoPgtServicoAcademico();
        Enumeration keys = situacaoPgtServicoAcademicos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoPgtServicoAcademicos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>formaPagamento</code>
     */
    public List getListaSelectItemFormaPagamentoPgtoServicoAcademico() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable formaPagamentoPgtoServicoAcademicos = (Hashtable) Dominios.getFormaPagamentoPgtoServicoAcademico();
        Enumeration keys = formaPagamentoPgtoServicoAcademicos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) formaPagamentoPgtoServicoAcademicos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>ContaCorrente</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarContaCorrentePorChavePrimaria() {
        try {
            Integer campoConsulta = pgtoServicoAcademicoVO.getContaCorrente().getCodigo();
            ContaCorrenteVO contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            pgtoServicoAcademicoVO.getContaCorrente().setNumero(contaCorrente.getNumero());
            this.setContaCorrente_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            pgtoServicoAcademicoVO.getContaCorrente().setNumero("");
            pgtoServicoAcademicoVO.getContaCorrente().setCodigo(0);
            this.setContaCorrente_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUnidadeEnsinoPorChavePrimaria() {
        try {
            Integer campoConsulta = pgtoServicoAcademicoVO.getUnidadeEnsino().getCodigo();
            UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            pgtoServicoAcademicoVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
            this.setUnidadeEnsino_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            pgtoServicoAcademicoVO.getUnidadeEnsino().setNome("");
            pgtoServicoAcademicoVO.getUnidadeEnsino().setCodigo(0);
            this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * M?todo respons?vel por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>CentroDespesa</code>.
     */
    public void montarListaSelectItemCentroDespesa(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCentroDespesaPorIdentificadorCentroDespesa(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CategoriaDespesaVO obj = (CategoriaDespesaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorCategoriaDespesa().toString()));
            }
            setListaSelectItemCentroDespesa(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * M?todo respons?vel por atualizar o ComboBox relativo ao atributo <code>CentroDespesa</code>. Buscando todos os
     * objetos correspondentes a entidade <code>CentroDespesa</code>. Esta rotina n?o recebe par?metros para filtragem
     * de dados, isto ? importante para a inicializa??o dos dados da tela para o acionamento por meio requisi??es Ajax.
     */
    public void montarListaSelectItemCentroDespesa() {
        try {
            montarListaSelectItemCentroDespesa("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * M?todo respons?vel por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorCentroDespesa</code> Este atributo ? uma lista (
     * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarCentroDespesaPorIdentificadorCentroDespesa(String identificadorCentroDespesaPrm) throws Exception {
        List lista = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(identificadorCentroDespesaPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>SolicitacaoPgtoServicoAcademico</code> por meio de
     * sua respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca
     * pela chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarSolicitacaoPgtoServicoAcademicoPorChavePrimaria() {
        try {
            Integer campoConsulta = pgtoServicoAcademicoVO.getSolicitacaoPgtoServicoAcademico().getCodigo();
            SolicitacaoPgtoServicoAcademicoVO solicitacaoPgtoServicoAcademico = getFacadeFactory().getSolicitacaoPgtoServicoAcademicoFacade().consultarPorChavePrimaria(campoConsulta, false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            pgtoServicoAcademicoVO.getSolicitacaoPgtoServicoAcademico().setDescricao(solicitacaoPgtoServicoAcademico.getDescricao());
            this.setSolicitacaoPgtoServicoAcademico_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            pgtoServicoAcademicoVO.getSolicitacaoPgtoServicoAcademico().setDescricao("");
            pgtoServicoAcademicoVO.getSolicitacaoPgtoServicoAcademico().setCodigo(0);
            this.setSolicitacaoPgtoServicoAcademico_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("descricaoSolicitacaoPgtoServicoAcademico", "Solicitação Pagamento Serviço Acadêmico"));
        itens.add(new SelectItem("identificadorCentroDespesaCentroDespesa", "Centro Despesa"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("tipoDestinatario", "Tipo Destinatário"));
        itens.add(new SelectItem("numeroContaCorrente", "Conta Corrente"));
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

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCentroDespesa();
    }

    public String getContaCorrente_Erro() {
        return contaCorrente_Erro;
    }

    public void setContaCorrente_Erro(String contaCorrente_Erro) {
        this.contaCorrente_Erro = contaCorrente_Erro;
    }

    public String getUnidadeEnsino_Erro() {
        return unidadeEnsino_Erro;
    }

    public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
        this.unidadeEnsino_Erro = unidadeEnsino_Erro;
    }

    public List getListaSelectItemCentroDespesa() {
        return (listaSelectItemCentroDespesa);
    }

    public void setListaSelectItemCentroDespesa(List listaSelectItemCentroDespesa) {
        this.listaSelectItemCentroDespesa = listaSelectItemCentroDespesa;
    }

    public String getSolicitacaoPgtoServicoAcademico_Erro() {
        return solicitacaoPgtoServicoAcademico_Erro;
    }

    public void setSolicitacaoPgtoServicoAcademico_Erro(String solicitacaoPgtoServicoAcademico_Erro) {
        this.solicitacaoPgtoServicoAcademico_Erro = solicitacaoPgtoServicoAcademico_Erro;
    }

    public PgtoServicoAcademicoVO getPgtoServicoAcademicoVO() {
        return pgtoServicoAcademicoVO;
    }

    public void setPgtoServicoAcademicoVO(PgtoServicoAcademicoVO pgtoServicoAcademicoVO) {
        this.pgtoServicoAcademicoVO = pgtoServicoAcademicoVO;
    }
}
