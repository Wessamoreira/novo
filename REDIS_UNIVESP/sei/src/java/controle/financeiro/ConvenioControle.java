package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas convenioForm.jsp
 * convenioCons.jsp) com as funcionalidades da classe <code>Convenio</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Convenio
 * @see ConvenioVO
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
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConvenioCursoVO;
import negocio.comuns.financeiro.ConvenioTurnoVO;
import negocio.comuns.financeiro.ConvenioUnidadeEnsinoVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.TipoCoberturaConvenio;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ConvenioControle")
@Scope("viewScope")
@Lazy
public class ConvenioControle extends SuperControle implements Serializable {
    private List listaSelectItemCentroReceita;
    private List listaConsultaCentroDespesa;
    private String valorConsultaCentroDespesa;
    private String campoConsultaCentroDespesa;
    private ConvenioVO convenioVO;
    private String parceiro_Erro;
    private String requisitante_Erro;
    private String responsavelAutorizacao_Erro;
    private String responsavelFinalizacao_Erro;
    private List listaConsultaCurso;
    protected List listaSelectItemFormaPagamento;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private String valorConsultaRequisitante;
    private String campoConsultaRequisitante;
    private List listaConsultaRequisitante;
    private ConvenioUnidadeEnsinoVO convenioUnidadeEnsinoVO;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemParceiro;
    private ConvenioTurnoVO convenioTurnoVO;
    protected List listaSelectItemTurno;
    private ConvenioCursoVO convenioCursoVO;
    private String curso_Erro;
    private Boolean valorAtivo;
    private List listaSelectItemDescontoProgressivoParceiro;
    private List listaSelectItemDescontoProgressivoAluno;
    private List<SelectItem> listaSelectItemTipoFinanciamentoEstudantil;

    public ConvenioControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Convenio</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        try {
            setConvenioVO(new ConvenioVO());
            inicializarListasSelectItemTodosComboBox();
            inicializarFuncionarioRequisitante();
            setConvenioCursoVO(new ConvenioCursoVO());
            setConvenioTurnoVO(new ConvenioTurnoVO());
            setConvenioUnidadeEnsinoVO(new ConvenioUnidadeEnsinoVO());
            setCampoConsultaRequisitante("");
            setValorConsultaRequisitante("");
            setListaConsultaRequisitante(new ArrayList(0));
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Convenio</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        try {
            ConvenioVO obj = (ConvenioVO) context().getExternalContext().getRequestMap().get("convenioItens");
            ConvenioVO convenio = getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(obj.getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            obj.setNovoObj(Boolean.FALSE);
            setConvenioVO(convenio);
            inicializarListasSelectItemTodosComboBox();
            setConvenioCursoVO(new ConvenioCursoVO());
            setConvenioTurnoVO(new ConvenioTurnoVO());
            setConvenioUnidadeEnsinoVO(new ConvenioUnidadeEnsinoVO());
            setCampoConsultaRequisitante("");
            setValorConsultaRequisitante("");
            setListaConsultaRequisitante(new ArrayList(0));
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Convenio</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	getFacadeFactory().getConvenioFacade().persistir(getConvenioVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ConvenioCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getConvenioFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getConvenioFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataAssinatura")) {
                objs = getFacadeFactory().getConvenioFacade().consultarPorDataAssinatura(
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeParceiro")) {
                objs = getFacadeFactory().getConvenioFacade().consultarPorNomeParceiro(getControleConsulta().getValorConsulta(), "", true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("ativo")) {
                objs = getFacadeFactory().getConvenioFacade().consultarPorSituacao(getValorAtivo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioCons.xhtml");
        }
    }

    public void inicializarFuncionarioRequisitante() throws Exception {
        try {
            getConvenioVO().setRequisitante(
                    getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false,
                    getUsuarioLogado()));
        } catch (Exception e) {
            throw new Exception("A configuração do seu funcionário não permite que você cadastre um novo convênio.");
        }
    }

    public void consultarRequisitante() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaRequisitante().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaRequisitante(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
            }
            if (getCampoConsultaRequisitante().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaRequisitante(),
                        this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
            }
            setListaConsultaRequisitante(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarRequisitante() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
        this.convenioVO.setRequisitante(obj);
        obj = null;
        setCampoConsultaRequisitante("");
        setValorConsultaRequisitante("");
        this.setRequisitante_Erro("");
        listaConsultaRequisitante.clear();
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ConvenioVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getConvenioFacade().excluir(convenioVO, getUsuarioLogado());
            setConvenioVO(new ConvenioVO());
            setConvenioCursoVO(new ConvenioCursoVO());
            setConvenioTurnoVO(new ConvenioTurnoVO());
            setConvenioUnidadeEnsinoVO(new ConvenioUnidadeEnsinoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("convenioForm.xhtml");
        }
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ConvenioCurso</code> para o objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void adicionarConvenioCurso() throws Exception {
        try {
            if (!getConvenioVO().getCodigo().equals(0)) {
                convenioCursoVO.setConvenio(getConvenioVO().getCodigo());
            }
            if (getConvenioCursoVO().getCurso().getCodigo().intValue() != 0) {
                Integer campoConsulta = getConvenioCursoVO().getCurso().getCodigo();
                CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
                getConvenioCursoVO().setCurso(curso);
            }
            getConvenioVO().adicionarObjConvenioCursoVOs(getConvenioCursoVO());
            this.setConvenioCursoVO(new ConvenioCursoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ConvenioCurso</code> para edição pelo
     * usuário.
     */
    public void editarConvenioCurso() throws Exception {
        ConvenioCursoVO obj = (ConvenioCursoVO) context().getExternalContext().getRequestMap().get("convenioCursoItens");
        setConvenioCursoVO(obj);
        // return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ConvenioCurso</code> do objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void removerConvenioCurso() throws Exception {
        ConvenioCursoVO obj = (ConvenioCursoVO) context().getExternalContext().getRequestMap().get("convenioCursoItens");
        getConvenioVO().excluirObjConvenioCursoVOs(obj.getCurso().getCodigo());
        setMensagemID("msg_dados_excluidos");
        // return "editar";
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ConvenioTurno</code> para o objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void adicionarConvenioTurno() throws Exception {
        try {
            if (!getConvenioVO().getCodigo().equals(0)) {
                convenioTurnoVO.setConvenio(getConvenioVO().getCodigo());
            }
            if (getConvenioTurnoVO().getTurno().getCodigo().intValue() != 0) {
                Integer campoConsulta = getConvenioTurnoVO().getTurno().getCodigo();
                TurnoVO turno = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
                getConvenioTurnoVO().setTurno(turno);
            }
            getConvenioVO().adicionarObjConvenioTurnoVOs(getConvenioTurnoVO());
            this.setConvenioTurnoVO(new ConvenioTurnoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ConvenioTurno</code> para edição pelo
     * usuário.
     */
    public void editarConvenioTurno() throws Exception {
        ConvenioTurnoVO obj = (ConvenioTurnoVO) context().getExternalContext().getRequestMap().get("convenioTurnoItens");
        setConvenioTurnoVO(obj);
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ConvenioTurno</code> do objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void removerConvenioTurno() throws Exception {
        ConvenioTurnoVO obj = (ConvenioTurnoVO) context().getExternalContext().getRequestMap().get("convenioTurnoItens");
        getConvenioVO().excluirObjConvenioTurnoVOs(obj.getTurno().getCodigo());
        setMensagemID("msg_dados_excluidos");
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>ConvenioUnidadeEnsino</code> para o objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void adicionarConvenioUnidadeEnsino() throws Exception {
        try {
            if (!getConvenioVO().getCodigo().equals(0)) {
                convenioUnidadeEnsinoVO.setConvenio(getConvenioVO().getCodigo());
            }
            if (getConvenioUnidadeEnsinoVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
                Integer campoConsulta = getConvenioUnidadeEnsinoVO().getUnidadeEnsino().getCodigo();
                UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getConvenioUnidadeEnsinoVO().setUnidadeEnsino(unidadeEnsino);
            }
            getConvenioVO().adicionarObjConvenioUnidadeEnsinoVOs(getConvenioUnidadeEnsinoVO());
            this.setConvenioUnidadeEnsinoVO(new ConvenioUnidadeEnsinoVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ConvenioUnidadeEnsino</code> para edição
     * pelo usuário.
     */
    public void editarConvenioUnidadeEnsino() throws Exception {
        ConvenioUnidadeEnsinoVO obj = (ConvenioUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("convenioUnidadeEnsinoItens");
        setConvenioUnidadeEnsinoVO(obj);
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ConvenioUnidadeEnsino</code> do objeto
     * <code>convenioVO</code> da classe <code>Convenio</code>
     */
    public void removerConvenioUnidadeEnsino() throws Exception {
        ConvenioUnidadeEnsinoVO obj = (ConvenioUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("convenioUnidadeEnsinoItens");
        getConvenioVO().excluirObjConvenioUnidadeEnsinoVOs(obj.getUnidadeEnsino().getCodigo());
        setMensagemID("msg_dados_excluidos");
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

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
        getConvenioCursoVO().setCurso(obj);
        montarListaSelectItemTurno();
        listaConsultaCurso.clear();
        this.setValorConsultaCurso("");
        this.setCampoConsultaCurso("");
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCursoPorChavePrimaria() {
        try {
            Integer campoConsulta = convenioCursoVO.getCurso().getCodigo();
            CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
            convenioCursoVO.getCurso().setNome(curso.getNome());
            this.setCurso_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            convenioCursoVO.getCurso().setNome("");
            convenioCursoVO.getCurso().setCodigo(0);
            this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Operação responsável por processar a consulta pelo parâmetro informado pelo usuário.<code>CursoVO</code> Após a
     * consulta ela automaticamente adciona o código e o nome da cidade na tela.
     */
    public List consultarCursoSuggestionbox(Object event) {
        try {
            String valor = event.toString();
            List lista = getFacadeFactory().getCursoFacade().consultarPorNome(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turno</code>.
     */
    public void montarListaSelectItemTurno(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTurnoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TurnoVO obj = (TurnoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemTurno(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurno() {
        try {
            montarListaSelectItemTurno("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>FormaPagamento</code>.
     */
    public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarFormaPagamentoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemFormaPagamento(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>FormaPagamento</code>. Buscando todos os
     * objetos correspondentes a entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemFormaPagamento() {
        try {
            montarListaSelectItemFormaPagamento("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTurnoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
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
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemParceiro(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarParceiroPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ParceiroVO obj = (ParceiroVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemParceiro(objs);
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
    public void montarListaSelectItemParceiro() {
        try {
            montarListaSelectItemParceiro("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarParceiroPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getParceiroFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemCentroReceita();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemTurno();
        montarListaSelectItemParceiro();
        montarListaSelectItemFormaPagamento();
        if (getConvenioVO() != null && getConvenioVO().getPossuiDescontoAntecipacao()) {
            montarListasDescontosProgressivos();
        }
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable convenioSituacaos = (Hashtable) Dominios.getConvenioSituacao();
        Enumeration keys = convenioSituacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) convenioSituacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>formaRecebimentoParceiro</code>
     */
    public List getListaSelectItemFormaRecebimentoParceiroConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoFormaPagamento.class);

        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoBolsaCusteadaParceiroParcela</code>
     */
    public List getListaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoValorConvenios = (Hashtable) Dominios.getTipoValorConvenio();
        Enumeration keys = tipoValorConvenios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoValorConvenios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoBolsaCusteadaParceiroMatricula</code>
     */
    public List getListaSelectItemTipoBolsaCusteadaParceiroMatriculaConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoValorConvenios = (Hashtable) Dominios.getTipoValorConvenio();
        Enumeration keys = tipoValorConvenios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoValorConvenios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoDescontoParcela</code>
     */
    public List getListaSelectItemTipoDescontoParcelaConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoValorConvenios = (Hashtable) Dominios.getTipoValorConvenio();
        Enumeration keys = tipoValorConvenios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoValorConvenios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoDescontoMatricula</code>
     */
    public List getListaSelectItemTipoDescontoMatriculaConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoValorConvenios = (Hashtable) Dominios.getTipoValorConvenio();
        Enumeration keys = tipoValorConvenios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoValorConvenios.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>cobertura</code>
     */
    public List getListaSelectItemCoberturaConvenio() throws Exception {
        List objs = new ArrayList(0);
        objs = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCoberturaConvenio.class);
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            String campoConsulta = convenioVO.getRequisitante().getMatricula();
            List lista = new ArrayList(0);
            lista = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(),
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            Iterator i = lista.iterator();
            FuncionarioVO pessoa = new FuncionarioVO();
            while (i.hasNext()) {
                pessoa = (FuncionarioVO) i.next();
            }

            if (pessoa.getPessoa().getFuncionario().equals(Boolean.TRUE)) {
                convenioVO.setRequisitante(pessoa);
                this.setRequisitante_Erro("");
                setMensagemID("msg_dados_consultados");
            } else {
                setMensagemID("msg_erro_dadosnaoencontrados");
                convenioVO.getRequisitante().getPessoa().setNome("");
                convenioVO.getRequisitante().setCodigo(0);
                this.setRequisitante_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
            }
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            convenioVO.getRequisitante().getPessoa().setNome("");
            convenioVO.getRequisitante().setCodigo(0);
            this.setRequisitante_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Parceiro</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarParceiroPorChavePrimaria() {
        try {
            Integer campoConsulta = convenioVO.getParceiro().getCodigo();
            ParceiroVO parceiro = getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(campoConsulta, false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            convenioVO.getParceiro().setNome(parceiro.getNome());
            this.setParceiro_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            convenioVO.getParceiro().setNome("");
            convenioVO.getParceiro().setCodigo(0);
            this.setParceiro_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Operação responsável por processar a consulta pelo parâmetro informado pelo usuário.<code>ParceiroVO</code> Após
     * a consulta ela automaticamente adciona o código e o nome da cidade na tela.
     */
    public List consultarParceiroSuggestionbox(Object event) {
        try {
            String valor = event.toString();
            List lista = getFacadeFactory().getParceiroFacade().consultarPorNome(valor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                    getUsuarioLogado());
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("dataAssinatura", "Data Assinatura"));
        itens.add(new SelectItem("nomeParceiro", "Parceiro"));
        itens.add(new SelectItem("ativo", "Ativo"));
        return itens;
    }

    /**
     * Rotina responsável por preencher a combo de consultar por ativo (situação).
     */
    public List getAtivoCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(true, "Sim"));
        itens.add(new SelectItem(false, "Não"));
        return itens;
    }

    public boolean isCampoData() {
        if (getControleConsulta().getCampoConsulta().equals("dataAssinatura")) {
            return true;
        }
        return false;
    }

    public boolean isCampoAtivo() {
        if (getControleConsulta().getCampoConsulta().equals("ativo")) {
            return true;
        }
        return false;
    }

    public List getTipoConsultaComboRequisitante() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("matricula", "Matricula"));
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
        return Uteis.getCaminhoRedirecionamentoNavegacao("convenioCons.xhtml");
    }

    public void autorizarConvenio() throws Exception {
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("Convenio_AutorizarConvenio", getUsuarioLogado());
            setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuarioLogado().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,
                    getUsuarioLogado()));
            getConvenioVO().setSituacao("AT");
            getConvenioVO().getResponsavelAutorizacao().setCodigo(getUsuario().getPessoa().getCodigo());
            consultarResponsavelAutorizacaoPorChavePrimaria();
            getConvenioVO().setDataAutorizacao(new Date());
            getConvenioVO().setResponsavelFinalizacao(new PessoaVO());
            getConvenioVO().setDataFinalizacao(null);
            getFacadeFactory().getConvenioFacade().alterarSituacao(getConvenioVO(), getUsuarioLogado());
            setMensagemID("msg_convenio_convenioAutorizado");
        } catch (Exception e) {
            setMensagemID("msg_convenio_erro");
        }
    }

    public void finalizarConvenio() throws Exception {
        try {
            if (!getConvenioVO().getResponsavelAutorizacao().getCodigo().equals(0)) {
                ControleAcesso.verificarPermissaoUsuarioFuncionalidade("Convenio_FinalizarConvenio", getUsuarioLogado());
                setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getUsuarioLogado().getCodigo(),
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                getConvenioVO().setSituacao("FI");
                getConvenioVO().setAtivo(Boolean.FALSE);
                getConvenioVO().getResponsavelFinalizacao().setCodigo(getUsuario().getPessoa().getCodigo());
                getConvenioVO().getResponsavelInativacao().setCodigo(getUsuario().getPessoa().getCodigo());
                consultarResponsavelFinalizacaoPorChavePrimaria();
                getConvenioVO().setDataFinalizacao(new Date());
                getConvenioVO().setDataInativacao(new Date());
                getFacadeFactory().getConvenioFacade().alterarSituacao(getConvenioVO(), getUsuarioLogado());
                setMensagemID("msg_convenio_convenioFinalizado");
            } else {
                setMensagemID("msg_convenio_faltaAutorizar");
            }
        } catch (Exception e) {
            setMensagemID("msg_convenio_erro");
        }
    }

    public void consultarResponsavelAutorizacaoPorChavePrimaria() {
        try {
            Integer campoConsulta = convenioVO.getResponsavelAutorizacao().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            convenioVO.getResponsavelAutorizacao().setNome(pessoa.getNome());
            this.setRequisitante_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            convenioVO.getResponsavelAutorizacao().setNome("");
            convenioVO.getResponsavelAutorizacao().setCodigo(0);
            this.setResponsavelAutorizacao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public void consultarResponsavelFinalizacaoPorChavePrimaria() {
        try {
            Integer campoConsulta = convenioVO.getResponsavelFinalizacao().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            convenioVO.getResponsavelFinalizacao().setNome(pessoa.getNome());
            this.setResponsavelAutorizacao_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            convenioVO.getResponsavelFinalizacao().setNome("");
            convenioVO.getResponsavelFinalizacao().setCodigo(0);
            this.setResponsavelFinalizacao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public List getListaSelectItemParceiro() {
        return listaSelectItemParceiro;
    }

    public void setListaSelectItemParceiro(List listaSelectItemParceiro) {
        this.listaSelectItemParceiro = listaSelectItemParceiro;
    }

    public String getCurso_Erro() {
        return curso_Erro;
    }

    public void setCurso_Erro(String curso_Erro) {
        this.curso_Erro = curso_Erro;
    }

    public ConvenioCursoVO getConvenioCursoVO() {
        if (convenioCursoVO == null) {
            convenioCursoVO = new ConvenioCursoVO();
        }
        return convenioCursoVO;
    }

    public void setConvenioCursoVO(ConvenioCursoVO convenioCursoVO) {
        this.convenioCursoVO = convenioCursoVO;
    }

    public List getListaSelectItemTurno() {
        return (listaSelectItemTurno);
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public ConvenioTurnoVO getConvenioTurnoVO() {
        if (convenioTurnoVO == null) {
            convenioTurnoVO = new ConvenioTurnoVO();
        }
        return convenioTurnoVO;
    }

    public void setConvenioTurnoVO(ConvenioTurnoVO convenioTurnoVO) {
        this.convenioTurnoVO = convenioTurnoVO;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public ConvenioUnidadeEnsinoVO getConvenioUnidadeEnsinoVO() {
        if (convenioUnidadeEnsinoVO == null) {
            convenioUnidadeEnsinoVO = new ConvenioUnidadeEnsinoVO();
        }
        return convenioUnidadeEnsinoVO;
    }

    public void setConvenioUnidadeEnsinoVO(ConvenioUnidadeEnsinoVO convenioUnidadeEnsinoVO) {
        this.convenioUnidadeEnsinoVO = convenioUnidadeEnsinoVO;
    }

    public String getResponsavelFinalizacao_Erro() {
        return responsavelFinalizacao_Erro;
    }

    public void setResponsavelFinalizacao_Erro(String responsavelFinalizacao_Erro) {
        this.responsavelFinalizacao_Erro = responsavelFinalizacao_Erro;
    }

    public String getResponsavelAutorizacao_Erro() {
        return responsavelAutorizacao_Erro;
    }

    public void setResponsavelAutorizacao_Erro(String responsavelAutorizacao_Erro) {
        this.responsavelAutorizacao_Erro = responsavelAutorizacao_Erro;
    }

    public String getRequisitante_Erro() {
        return requisitante_Erro;
    }

    public void setRequisitante_Erro(String requisitante_Erro) {
        this.requisitante_Erro = requisitante_Erro;
    }

    public String getParceiro_Erro() {
        return parceiro_Erro;
    }

    public void setParceiro_Erro(String parceiro_Erro) {
        this.parceiro_Erro = parceiro_Erro;
    }

    public ConvenioVO getConvenioVO() {
        return convenioVO;
    }

    public void setConvenioVO(ConvenioVO convenioVO) {
        this.convenioVO = convenioVO;
    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public List getListaConsultaCurso() {
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public String getCampoConsultaRequisitante() {
        return campoConsultaRequisitante;
    }

    public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
        this.campoConsultaRequisitante = campoConsultaRequisitante;
    }

    public List getListaConsultaRequisitante() {
        return listaConsultaRequisitante;
    }

    public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
        this.listaConsultaRequisitante = listaConsultaRequisitante;
    }

    public String getValorConsultaRequisitante() {
        return valorConsultaRequisitante;
    }

    public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
        this.valorConsultaRequisitante = valorConsultaRequisitante;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        convenioVO = null;
        parceiro_Erro = null;
        requisitante_Erro = null;
        responsavelAutorizacao_Erro = null;
        responsavelFinalizacao_Erro = null;
        convenioUnidadeEnsinoVO = null;
        campoConsultaCurso = null;
        valorConsultaCurso = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaConsultaCurso);
        Uteis.liberarListaMemoria(listaSelectItemParceiro);
        convenioTurnoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemTurno);
        convenioCursoVO = null;
        curso_Erro = null;
    }

    /**
     * @return the listaSelectItemFormaPagamento
     */
    public List getListaSelectItemFormaPagamento() {
        return listaSelectItemFormaPagamento;
    }

    /**
     * @param listaSelectItemFormaPagamento
     *            the listaSelectItemFormaPagamento to set
     */
    public void setListaSelectItemFormaPagamento(List listaSelectItemFormaPagamento) {
        this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
    }

    /**
     * Método responsável por ativar um Convênio inativado, ou inativar um ativado. Faz uso do método de mesmo nome no
     * facade.
     *
     * @author Paulo Taucci
     */
    public void realizarAtivarInativar() {
        try {
            setMensagemID(getFacadeFactory().getConvenioFacade().realizarAtivacaoInativacao(getConvenioVO(), getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getBotaoAtivar() {
    	if (getIsApresentarBotaoAtivar() == null) {
    		return false;
    	}
    	return getIsApresentarBotaoAtivar();
    }
    
    public Boolean getIsApresentarBotaoAtivar() {
        return !getConvenioVO().getAtivo() && getConvenioVO().getCodigo() != null && !getConvenioVO().getCodigo().equals(0)
                && getConvenioVO().getResponsavelAtivacao().getCodigo().equals(0);
    }
    

    public Boolean getIsApresentarBotaoInativar() {
        return getConvenioVO().getAtivo() && getConvenioVO().getCodigo() != null && !getConvenioVO().getCodigo().equals(0);
    }

    /**
     * Método que valida se um convênio pode ser alterado (Gravar ou Excluir). Depois que o convênio é ativado pela
     * primeira vez ele não pode mais ser alterado, pois ele fica disponível para vínculos na matrícula. Assim, um
     * convênio só pode ser alterado até ser ativado pela primeira vez.
     *
     * @author Paulo Taucci
     * @return
     */
    public Boolean getIsPodeAlterar() {
        if (getConvenioVO().getAtivo()) {
            return false;
        } else {
            // Se houver um responsável pela ativação, significa que já foi ativado e depois inativado
            return getConvenioVO().getResponsavelAtivacao().getCodigo() == 0;
        }
    }

    public Boolean getValorAtivo() {
        if (valorAtivo == null) {
            valorAtivo = Boolean.FALSE;
        }
        return valorAtivo;
    }

    public void setValorAtivo(Boolean valorAtivo) {
        this.valorAtivo = valorAtivo;
    }

    public void montarListasDescontosProgressivos() {
        List<DescontoProgressivoVO> descontoProgressivoVOs = new ArrayList<DescontoProgressivoVO>(0);
        try {
            montarListaSelectItemDescontoProgressivoParceiro(descontoProgressivoVOs);
            montarListaSelectItemDescontoProgressivoAluno(descontoProgressivoVOs);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            descontoProgressivoVOs = null;
        }
    }

    public void montarListaSelectItemDescontoProgressivoParceiro(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
        descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
        if(Uteis.isAtributoPreenchido(getConvenioVO().getDescontoProgressivoParceiro().getCodigo()) && descontoProgressivoVOs.stream().noneMatch(p-> p.getCodigo().equals(getConvenioVO().getDescontoProgressivoParceiro().getCodigo()))) {
        	descontoProgressivoVOs.add((DescontoProgressivoVO) Uteis.clonar(getConvenioVO().getDescontoProgressivoParceiro()));
        }
        setListaSelectItemDescontoProgressivoParceiro(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
    }

    public void montarListaSelectItemDescontoProgressivoAluno(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
        descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
        if(Uteis.isAtributoPreenchido(getConvenioVO().getDescontoProgressivoAluno().getCodigo()) && descontoProgressivoVOs.stream().noneMatch(p-> p.getCodigo().equals(getConvenioVO().getDescontoProgressivoAluno().getCodigo()))) {
        	descontoProgressivoVOs.add((DescontoProgressivoVO) Uteis.clonar(getConvenioVO().getDescontoProgressivoAluno()));
        }
        setListaSelectItemDescontoProgressivoAluno(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
    }

    public List getListaSelectItemDescontoProgressivoParceiro() {
        if (listaSelectItemDescontoProgressivoParceiro == null) {
            listaSelectItemDescontoProgressivoParceiro = new ArrayList(0);
        }
        return listaSelectItemDescontoProgressivoParceiro;
    }

    public void setListaSelectItemDescontoProgressivoParceiro(List listaSelectItemDescontoProgressivoParceiro) {
        this.listaSelectItemDescontoProgressivoParceiro = listaSelectItemDescontoProgressivoParceiro;
    }

    public List getListaSelectItemDescontoProgressivoAluno() {
        if (listaSelectItemDescontoProgressivoAluno == null) {
            listaSelectItemDescontoProgressivoAluno = new ArrayList(0);
        }
        return listaSelectItemDescontoProgressivoAluno;
    }

    public void setListaSelectItemDescontoProgressivoAluno(List listaSelectItemDescontoProgressivoAluno) {
        this.listaSelectItemDescontoProgressivoAluno = listaSelectItemDescontoProgressivoAluno;
    }

    public void atualizarDescontoConvenio() {
        this.getConvenioVO().setTipoDescontoMatricula(this.getConvenioVO().getTipoBolsaCusteadaParceiroMatricula());
        this.getConvenioVO().setDescontoMatricula(this.getConvenioVO().getBolsaCusteadaParceiroMatricula());
        this.getConvenioVO().setTipoDescontoParcela(this.getConvenioVO().getTipoBolsaCusteadaParceiroParcela());
        this.getConvenioVO().setDescontoParcela(this.getConvenioVO().getBolsaCusteadaParceiroParcela());
    }

    public void atualizarAplicarSobreValorBaseDeduzidoValorOutrosConvenios() {
        if (this.getConvenioVO().getAplicarSobreValorCheio()) {
            this.getConvenioVO().setAplicarSobreValorBaseDeduzidoValorOutrosConvenios(Boolean.FALSE);
        }
    }

    /**
     * @return the listaConsultaCentroDespesa
     */
    public List getListaConsultaCentroDespesa() {
        if (listaConsultaCentroDespesa == null) {
            listaConsultaCentroDespesa = new ArrayList(0);
        }
        return listaConsultaCentroDespesa;
    }

    /**
     * @param listaConsultaCentroDespesa the listaConsultaCentroDespesa to set
     */
    public void setListaConsultaCentroDespesa(List listaConsultaCentroDespesa) {
        this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
    }

    /**
     * @return the valorConsultaCentroDespesa
     */
    public String getValorConsultaCentroDespesa() {
        if (valorConsultaCentroDespesa == null) {
            valorConsultaCentroDespesa = "";
        }
        return valorConsultaCentroDespesa;
    }

    /**
     * @param valorConsultaCentroDespesa the valorConsultaCentroDespesa to set
     */
    public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
        this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
    }

    /**
     * @return the campoConsultaCentroDespesa
     */
    public String getCampoConsultaCentroDespesa() {
        if (campoConsultaCentroDespesa == null) {
            campoConsultaCentroDespesa = "";
        }
        return campoConsultaCentroDespesa;
    }

    /**
     * @param campoConsultaCentroDespesa the campoConsultaCentroDespesa to set
     */
    public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
        this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
    }

    public void consultarCentroDespesa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCentroDespesa().equals("descricao")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCentroDespesa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCentroDespesa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCentroDespesa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
        return itens;
    }
    
    public void limparDadoscategoriaDespesaRestituicaoConvenio() {
        this.getConvenioVO().setCategoriaDespesaRestituicaoConvenio(null);
        this.getConvenioVO().setCategoriaDespesaRestituicaoConvenio(new CategoriaDespesaVO());
    }

    public void selecionarCentroDespesa() {
        CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
        this.getConvenioVO().setCategoriaDespesaRestituicaoConvenio(obj);
    }
    
    public void montarListaSelectItemCentroReceita() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CentroReceitaVO obj = (CentroReceitaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString() + "-" + obj.getIdentificadorCentroReceita().toString()));
            }
            setListaSelectItemCentroReceita(objs);
        } catch (Exception e) {
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public List getListaSelectItemCentroReceita() {
        if (listaSelectItemCentroReceita == null) {
            listaSelectItemCentroReceita = new ArrayList(0);
        }        
        return listaSelectItemCentroReceita;
    }

    /**
     * @param listaSelectItemCentroReceita the listaSelectItemCentroReceita to set
     */
    public void setListaSelectItemCentroReceita(List listaSelectItemCentroReceita) {
        this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
    }

	public List<SelectItem> getListaSelectItemTipoFinanciamentoEstudantil() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FinanciamentoEstudantil.class,"valor", "descricao", false);
	}

	public void setListaSelectItemTipoFinanciamentoEstudantil(List<SelectItem> listaSelectItemTipoFinanciamentoEstudantil) {
		this.listaSelectItemTipoFinanciamentoEstudantil = listaSelectItemTipoFinanciamentoEstudantil;
	}
}
