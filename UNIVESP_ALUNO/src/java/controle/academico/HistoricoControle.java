package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.academico.MatriculaPeriodo;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
@Controller("HistoricoControle")
@Scope("request")
@Lazy
public class HistoricoControle extends SuperControle implements Serializable {

    private HistoricoVO historicoVO;
    private String disciplina_Erro;
    private String matricula_Erro;
    private String mensagemCalculoNotas_Erro;
    private String frequencia_Erro;
    private String responsavel_Erro;
    private String semestre;
    private String ano;
    private Boolean nota1Readonly;
    private Boolean nota2Readonly;
    private Boolean nota3Readonly;
    private Boolean nota4Readonly;
    private Boolean nota5Readonly;
    private Boolean nota6Readonly;
    private Boolean nota7Readonly;
    private Boolean nota8Readonly;
    private Boolean nota9Readonly;
    private Boolean nota10Readonly;
    private Boolean nota11Readonly;
    private Boolean nota12Readonly;
    private Boolean nota13Readonly;
    private List<SelectItem> listaSelectItemNota1Conceito;
    private List<SelectItem> listaSelectItemNota2Conceito;
    private List<SelectItem> listaSelectItemNota3Conceito;
    private List<SelectItem> listaSelectItemNota4Conceito;
    private List<SelectItem> listaSelectItemNota5Conceito;
    private List<SelectItem> listaSelectItemNota6Conceito;
    private List<SelectItem> listaSelectItemNota7Conceito;
    private List<SelectItem> listaSelectItemNota8Conceito;
    private List<SelectItem> listaSelectItemNota9Conceito;
    private List<SelectItem> listaSelectItemNota10Conceito;
    private List<SelectItem> listaSelectItemNota11Conceito;
    private List<SelectItem> listaSelectItemNota12Conceito;
    private List<SelectItem> listaSelectItemNota13Conceito;
    private List listaSelectItemDisciplinas;
    private List listaSelectItemPeriodosLetivos;
    private List listaHistoricosVOs;
    private List listaSelectItemTipoInformarNota;
    private MatriculaVO matricula;
    private ConfiguracaoAcademicoVO configuracaoAcademico;
    private Integer disciplina;
    private Integer periodoLetivo;
    private String tipoNota;

    public HistoricoControle() throws Exception {
        //obterUsuarioLogado();                
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Historico</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Novo Histórico", "Novo");
        removerObjetoMemoria(this);
        setDisciplina_Erro("");
        setMatricula_Erro("");
        setMensagemCalculoNotas_Erro("");
        setFrequencia_Erro("");
        setResponsavel_Erro("");
        setHistoricoVO(new HistoricoVO());
        setMatricula(new MatriculaVO());
        setDisciplina(0);
        setPeriodoLetivo(0);
        setListaHistoricosVOs(new ArrayList(0));
        setTipoNota("");
        setConfiguracaoAcademico(new ConfiguracaoAcademicoVO());
        inicializarListasSelectItemTodosComboBox();
        inicializarUsuarioResponsavelMatriculaUsuarioLogado();
        ocultarNotasTela();
        habilitarCampos();
        setTipoNota("");
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectPeriodosLetivos();
        montarListaSelectItemDisciplinas();
        montarListaOpcoesNotas();
        
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Historico</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Editar Histórico", "Editando");
        setDisciplina_Erro("");
        setMatricula_Erro("");
        setMensagemCalculoNotas_Erro("");
        setFrequencia_Erro("");
        setResponsavel_Erro("");
        setTipoNota("");
        setDisciplina(0);
        setPeriodoLetivo(0);
        setListaHistoricosVOs(new ArrayList(0));
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
        obj.setNovoObj(Boolean.FALSE);
        setMatricula(obj);
        montarConfiguracaoAcademico();
        inicializarListasSelectItemTodosComboBox();
        registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Editar Histórico", "Editando");
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Historico</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
//    public String gravar() {
//        try {
////            inicializarUsuarioResponsavelMatriculaUsuarioLogado();
////            if (historicoVO.isNovoObj().booleanValue()) {
////                historicoFacade.incluir(historicoVO);
////            } else {
////                inicializarUsuarioResponsavelMatriculaUsuarioLogado();
////                historicoFacade.alterar(historicoVO);
////            }
//            getFacadeFactory().getHistoricoFacade().incluirListaHistorico(getListaHistoricosVOs(), getUsuarioLogado(), "Visão Administrativa");
//            setMensagemID("msg_dados_gravados");
//            return "editar";
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//            return "editar";
//        }
//    }

    public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
        try {
            historicoVO.setResponsavel(getUsuarioLogadoClone());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP HistoricoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeResponsavel")) {
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Consultar Histórico", "Consultando");
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Consultar Histórico", "Consultando");
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

    public void montarConfiguracaoAcademico() {
        try {
            if (getMatricula().getCurso().getConfiguracaoAcademico().getCodigo().intValue() == 0) {
                getMatricula().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatricula().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
            }
            Integer codigoConfiguracaoAcademico = getMatricula().getCurso().getConfiguracaoAcademico().getCodigo();
            setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codigoConfiguracaoAcademico, getUsuarioLogado()));
            apresentarNotasTela(getConfiguracaoAcademico());
            inicializarOpcaoNotaConceito();
            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }
    
    public void inicializarOpcaoNotaConceito(){
        setListaSelectItemNota1Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota1ConceitoVOs()));
        setListaSelectItemNota2Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota2ConceitoVOs()));
        setListaSelectItemNota3Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota3ConceitoVOs()));
        setListaSelectItemNota4Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota4ConceitoVOs()));
        setListaSelectItemNota5Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota5ConceitoVOs()));
        setListaSelectItemNota6Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota6ConceitoVOs()));
        setListaSelectItemNota7Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota7ConceitoVOs()));
        setListaSelectItemNota8Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota8ConceitoVOs()));
        setListaSelectItemNota9Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota9ConceitoVOs()));
        setListaSelectItemNota10Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota10ConceitoVOs()));
        setListaSelectItemNota11Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota11ConceitoVOs()));
        setListaSelectItemNota12Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota12ConceitoVOs()));
        setListaSelectItemNota13Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademico().getConfiguracaoAcademicoNota13ConceitoVOs()));
    }
    
    private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs){
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(new ConfiguracaoAcademicoNotaConceitoVO(), ""));
        for(ConfiguracaoAcademicoNotaConceitoVO obj:configuracaoAcademicoNotaConceitoVOs){
            itens.add(new SelectItem(obj, obj.getConceitoNota()));
        }        
        return itens;
        
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>HistoricoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Inicializando Excluir Histórico", "Excluindo");
            novo();
            ocultarNotasTela();
            registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoControle", "Finalizando Excluir Histórico", "Excluindo");
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

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List getListaSelectItemSituacaoHistorico() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoHistoricos = (Hashtable) Dominios.getSituacaoHistorico();
        Enumeration keys = situacaoHistoricos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoHistoricos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>tipoHistorico</code>
     */
    public List getListaSelectItemTipoHistoricoHistorico() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoHistoricoHistoricos = (Hashtable) Dominios.getTipoHistoricoHistorico();
        Enumeration keys = tipoHistoricoHistoricos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoHistoricoHistoricos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public void montarListaSelectPeriodosLetivos() {
        try {
            montarListaSelectPeriodosLetivos("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectPeriodosLetivos(String prm) throws Exception {

        if (getMatricula().getMatricula().equals("")) {
            setListaSelectItemPeriodosLetivos(new ArrayList(0));
            return;
        }

        List resultadoConsulta = consultarMatriculaPeriodo(getMatricula().getMatricula());
        List objs = new ArrayList(0);
        Iterator i = resultadoConsulta.iterator();
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) i.next();
            getFacadeFactory().getMatriculaPeriodoFacade().montarDadosPeriodoLetivoMatricula(obj, NivelMontarDados.BASICO, getUsuarioLogado());
            adicionarPeriodoLetivoNaListaPeridoLetivo(objs, obj);
        }

        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        setListaSelectItemPeriodosLetivos(objs);
    }

    public void adicionarPeriodoLetivoNaListaPeridoLetivo(List objs, MatriculaPeriodoVO obj) {
        Iterator i = objs.iterator();
        while (i.hasNext()) {
            SelectItem item = (SelectItem) i.next();
            if (item.getValue().equals(obj.getPeridoLetivo().getCodigo())) {
                return;
            }
        }
        objs.add(new SelectItem(obj.getPeridoLetivo().getCodigo(), obj.getPeridoLetivo().getDescricao()));
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>sigla</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarMatriculaPeriodo(String matricula) throws Exception {
        List lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodos(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemDisciplinas() {
        try {
            montarListaSelectItemDisciplinas("");
        } catch (Exception e) {
            setListaSelectItemDisciplinas(new ArrayList(0));
        }
    }

    public void montarListaSelectItemDisciplinas(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDisciplinasPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
                getFacadeFactory().getMatriculaPeriodoFacade().montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
            }
            setListaSelectItemDisciplinas(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarDisciplinasPorNome(String nomePrm) throws Exception {
        List lista = new ArrayList(0);
        if (periodoLetivo.intValue() != 0) {
            validarDadosParaMontarListaHistorico();
            MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoSemestreAno(getMatricula().getMatricula(), periodoLetivo, semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            List matriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinas(matriculaPeriodo.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            return matriculaPeriodoTurmaDisciplina;
        }
        return lista;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave primária.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            MatriculaVO matriculas = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
            if (matriculas.getSituacao().equalsIgnoreCase("AT")) {
                setListaHistoricosVOs(new ArrayList(0));
                setConfiguracaoAcademico(new ConfiguracaoAcademicoVO());
                setListaSelectItemPeriodosLetivos(new ArrayList(0));
                setPeriodoLetivo(0);
                setListaSelectItemDisciplinas(new ArrayList(0));
                setDisciplina(0);
                setMatricula(matriculas);
                montarConfiguracaoAcademico();
                if (!getMatricula().getCurso().getIntegral()) {
                    setAno(Uteis.getAnoDataAtual4Digitos());
                }
                inicializarListasSelectItemTodosComboBox();
                this.setMatricula_Erro("");
                setMensagemID("msg_dados_consultados");
            } else {
                setMensagemID("");
                setListaSelectItemDisciplinas(new ArrayList(0));
                setListaSelectItemPeriodosLetivos(new ArrayList(0));
                setListaSelectItemTipoInformarNota(new ArrayList(0));
                setTipoNota("");
                getMatricula().setMatricula("");
                getMatricula().getAluno().setNome("");
                setMensagemDetalhada(getMensagemInternalizacao("msg_erro_matriculanaoativa"));
            }
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            setListaSelectItemDisciplinas(new ArrayList(0));
            setListaSelectItemPeriodosLetivos(new ArrayList(0));
            setListaSelectItemTipoInformarNota(new ArrayList(0));
            setTipoNota("");
            getMatricula().setMatricula("");
            getMatricula().getAluno().setNome("");
            setMensagemDetalhada(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public void validarDadosParaMontarListaHistorico() throws Exception {
        if (getMatricula().getMatricula().equals("")) {
            throw new Exception("O campo MATRÍCULA (Historico) deve ser informado");
        }
        if (getPeriodoLetivo().intValue() == 0) {
            throw new Exception("O campo PERÍODO LETIVO (Historico) deve ser informado");
        }

        if (getMatricula().getCurso().getSemestral()) {
            if (semestre.equals("")) {
                throw new Exception("O campo SEMESTRE (Historico) deve ser informado");
            }
            if (ano.equals("")) {
                throw new Exception("O campo ANO (Historico) deve ser informado");
            }
        }

        if (getMatricula().getCurso().getAnual()) {
            if (ano.equals("")) {
                throw new Exception("O campo ANO (Historico) deve ser informado");
            }
        }

        if (getMatricula().getCurso().getIntegral()) {
            setAno("");
            setSemestre("");
        }
    }

    public void montarListaHistoricoAluno() {
        try {
            validarDadosParaMontarListaHistorico();
            setListaHistoricosVOs(new ArrayList(0));
            MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoSemestreAno(getMatricula().getMatricula(), periodoLetivo, semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (getDisciplina().intValue() != 0) {
                HistoricoVO histVO = getFacadeFactory().getHistoricoFacade().consultarPorMatricula_matriculaPeriodo_Disciplina(getMatricula().getMatricula(), matriculaPeriodo.getCodigo(), getDisciplina(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                adicionarHistoricoListaHistoricosVOs(histVO, getDisciplina(), matriculaPeriodo);
            } else {
                //Aqui consulta todos os historicos do periodoLetivo escolhido
                adicionarDisciplinaHistoricoVO(matriculaPeriodo);
            }
            this.setMatricula_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            setListaHistoricosVOs(new ArrayList(0));
            this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public void adicionarDisciplinaHistoricoVO(MatriculaPeriodoVO matriculaPeriodo) throws Exception {
        Iterator i = getListaSelectItemDisciplinas().iterator();
        while (i.hasNext()) {
            SelectItem item = (SelectItem) i.next();
            if (!item.getValue().equals(0)) {
                HistoricoVO histVO = getFacadeFactory().getHistoricoFacade().consultarPorMatricula_matriculaPeriodo_Disciplina(getMatricula().getMatricula(), matriculaPeriodo.getCodigo(), (Integer) item.getValue(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                adicionarHistoricoListaHistoricosVOs(histVO, (Integer) item.getValue(), matriculaPeriodo);
            }

        }
    }

    public void adicionarHistoricoListaHistoricosVOs(HistoricoVO histVO, Integer disciplina, MatriculaPeriodoVO matriculaPeriodo) throws Exception {

        if (histVO == null) {
            MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodoDisciplinaSemestreAno(matriculaPeriodo.getCodigo(), disciplina, semestre, ano, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (matriculaPeriodoTurmaVO != null) {
                histVO = new HistoricoVO();
                histVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                histVO.setMatricula(getMatricula());
                histVO.setMatriculaPeriodo(matriculaPeriodo);
                histVO.getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
                histVO.setDataRegistro(new Date());
//                histVO.setCargaHoraria(histVO.getDisciplina().getCargaHoraria().doubleValue());
                histVO.setSituacao("");
                histVO.setMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaVO);
            } else {
                return;
            }
        }
        if (matriculaPeriodo.getSituacaoMatriculaPeriodo().equals("FI")) {
            histVO.setEditavel(Boolean.TRUE);
        }
        montarFrequenciaAluno(histVO);
        getListaHistoricosVOs().add(histVO);
    }

    public void apresentarNotasTela(ConfiguracaoAcademicoVO ca) {
        if (ca.getUtilizarNota1().booleanValue()) {
            if (!ca.getFormulaCalculoNota1().equals("")) {
                setNota1Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota2().booleanValue()) {
            if (!ca.getFormulaCalculoNota2().equals("")) {
                setNota2Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota3().booleanValue()) {
            if (!ca.getFormulaCalculoNota3().equals("")) {
                setNota3Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota4().booleanValue()) {
            if (!ca.getFormulaCalculoNota4().equals("")) {
                setNota4Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota5().booleanValue()) {
            if (!ca.getFormulaCalculoNota5().equals("")) {
                setNota5Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota6().booleanValue()) {
            if (!ca.getFormulaCalculoNota6().equals("")) {
                setNota6Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota7().booleanValue()) {
            if (!ca.getFormulaCalculoNota7().equals("")) {
                setNota7Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota8().booleanValue()) {
            if (!ca.getFormulaCalculoNota8().equals("")) {
                setNota8Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota9().booleanValue()) {
            if (!ca.getFormulaCalculoNota9().equals("")) {
                setNota9Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota10().booleanValue()) {
            if (!ca.getFormulaCalculoNota10().equals("")) {
                setNota10Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota11().booleanValue()) {
            if (!ca.getFormulaCalculoNota11().equals("")) {
                setNota11Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota12().booleanValue()) {
            if (!ca.getFormulaCalculoNota12().equals("")) {
                setNota12Readonly(Boolean.TRUE);
            }
        }
        if (ca.getUtilizarNota13().booleanValue()) {
            if (!ca.getFormulaCalculoNota13().equals("")) {
                setNota13Readonly(Boolean.TRUE);
            }
        }
    }

    public void habilitarCampos() {
        setNota1Readonly(Boolean.FALSE);
        setNota2Readonly(Boolean.FALSE);
        setNota3Readonly(Boolean.FALSE);
        setNota4Readonly(Boolean.FALSE);
        setNota5Readonly(Boolean.FALSE);
        setNota6Readonly(Boolean.FALSE);
        setNota7Readonly(Boolean.FALSE);
        setNota8Readonly(Boolean.FALSE);
        setNota9Readonly(Boolean.FALSE);
        setNota10Readonly(Boolean.FALSE);
        setNota11Readonly(Boolean.FALSE);
        setNota12Readonly(Boolean.FALSE);
        setNota13Readonly(Boolean.FALSE);
    }

    public void ocultarNotasTela() {
    }

    public boolean getTipoNota1() {

        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota1())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota2() {
        if (tipoNota.equals("")) {
            return true;
        } else {
            if (tipoNota.equals(getConfiguracaoAcademico().getTituloNota2())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota3() {
        if (tipoNota.equals("")) {
            return true;
        } else {
            if (tipoNota.equals(getConfiguracaoAcademico().getTituloNota3())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota4() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota4())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota5() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota5())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota6() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota6())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota7() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota7())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota8() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota8())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota9() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota9())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota10() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota10())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota11() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota11())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota12() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota12())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getTipoNota13() {
        if (getTipoNota().equals("")) {
            return true;
        } else {
            if (getTipoNota().equals(getConfiguracaoAcademico().getTituloNota13())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String getTipoNota() {
        if (tipoNota == null) {
            tipoNota = "";
        }
        return tipoNota;
    }

    public void setTipoNota(String tipoNota) {
        this.tipoNota = tipoNota;
    }

    public void calcularMedia(ConfiguracaoAcademicoVO ca, HistoricoVO histVO) throws Exception {
        boolean resultado = getConfiguracaoAcademico().substituirVariaveisFormulaPorValores(histVO);
        montarFrequenciaAluno(histVO);
        verificaAlunoReprovadoFalta(histVO);
        if (!histVO.getSituacao().equals("AA") && (!histVO.getSituacao().equals("RF")) || (!histVO.getSituacao().equals("IS")) || histVO.getSituacao().equals("")) {
            if (resultado) {
                histVO.setSituacao("AP");
            } else {
                histVO.setSituacao("RE");
            }
        }
    }

    public void verificarAprovacaoAluno() {
        try {
            Iterator i = getListaHistoricosVOs().iterator();
            while (i.hasNext()) {
                HistoricoVO histVO = (HistoricoVO) i.next();

                try {
                    calcularMedia(configuracaoAcademico, histVO);

                } catch (Exception e) {
                    histVO.setSituacao("VS");
                    montarFrequenciaAluno(histVO);
                }
            }
            setMensagemCalculoNotas_Erro("");
            apresentarNotasTela(getConfiguracaoAcademico());
            inicializarOpcaoNotaConceito();
        } catch (Exception e) {
            historicoVO.setSituacao("VS");
            setMensagemCalculoNotas_Erro(e.getMessage());
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public void atualizarSituacao(HistoricoVO histVO) {
        montarFrequenciaAluno(histVO);
        if (!histVO.getTipoHistorico().equalsIgnoreCase("NO")) {
            histVO.setSituacao("AP");
        } else {
            histVO.setSituacao("");
        }
    }

    public void montarFrequenciaAluno(HistoricoVO obj) {
        try {
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public void verificaAlunoReprovadoFalta(HistoricoVO obj) {
        try {
            getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(obj, getConfiguracaoAcademico(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
       
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
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

    public List getListaSelectSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public void montarListaOpcoesNotas() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        if (getConfiguracaoAcademico().getCodigo().intValue() != 0) {
            if (getConfiguracaoAcademico().getUtilizarNota1().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota1(), getConfiguracaoAcademico().getTituloNota1()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota2().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota2(), getConfiguracaoAcademico().getTituloNota2()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota3().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota3(), getConfiguracaoAcademico().getTituloNota3()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota4().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota4(), getConfiguracaoAcademico().getTituloNota4()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota5().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota5(), getConfiguracaoAcademico().getTituloNota5()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota6().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota6(), getConfiguracaoAcademico().getTituloNota6()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota7().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota7(), getConfiguracaoAcademico().getTituloNota7()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota8().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota8(), getConfiguracaoAcademico().getTituloNota8()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota9().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota9(), getConfiguracaoAcademico().getTituloNota9()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota10().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota10(), getConfiguracaoAcademico().getTituloNota10()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota11().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota11(), getConfiguracaoAcademico().getTituloNota11()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota12().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota12(), getConfiguracaoAcademico().getTituloNota12()));
            }
            if (getConfiguracaoAcademico().getUtilizarNota13().equals(Boolean.TRUE)) {
                lista.add(new SelectItem(getConfiguracaoAcademico().getTituloNota13(), getConfiguracaoAcademico().getTituloNota13()));
            }
        }
        setListaSelectItemTipoInformarNota(lista);
    }

    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        return configuracaoAcademico;
    }

    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }

    public Integer getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }

    public List getListaHistoricosVOs() {
        return listaHistoricosVOs;
    }

    public void setListaHistoricosVOs(List listaHistoricosVOs) {
        this.listaHistoricosVOs = listaHistoricosVOs;
    }

    public MatriculaVO getMatricula() {
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public String getMatricula_Erro() {
        return matricula_Erro;
    }

    public void setMatricula_Erro(String matricula_Erro) {
        this.matricula_Erro = matricula_Erro;
    }

    public String getDisciplina_Erro() {
        return disciplina_Erro;
    }

    public void setDisciplina_Erro(String disciplina_Erro) {
        this.disciplina_Erro = disciplina_Erro;
    }

    public HistoricoVO getHistoricoVO() {
        return historicoVO;
    }

    public List getListaSelectItemTipoInformarNota() {
        return listaSelectItemTipoInformarNota;
    }

    public void setListaSelectItemTipoInformarNota(List listaSelectItemTipoInformarNota) {
        this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
    }

    public void setHistoricoVO(HistoricoVO historicoVO) {
        this.historicoVO = historicoVO;
    }

    public Boolean getNota13Readonly() {
        return nota13Readonly;
    }

    public void setNota13Readonly(Boolean nota13Readonly) {
        this.nota13Readonly = nota13Readonly;
    }
    public Boolean getNota12Readonly() {
        return nota12Readonly;
    }

    public void setNota12Readonly(Boolean nota12Readonly) {
        this.nota12Readonly = nota12Readonly;
    }
    public Boolean getNota11Readonly() {
        return nota11Readonly;
    }

    public void setNota11Readonly(Boolean nota11Readonly) {
        this.nota11Readonly = nota11Readonly;
    }
    public Boolean getNota10Readonly() {
        return nota10Readonly;
    }

    public void setNota10Readonly(Boolean nota10Readonly) {
        this.nota10Readonly = nota10Readonly;
    }

    public Boolean getNota1Readonly() {
        return nota1Readonly;
    }

    public void setNota1Readonly(Boolean nota1Readonly) {
        this.nota1Readonly = nota1Readonly;
    }

    public Boolean getNota2Readonly() {
        return nota2Readonly;
    }

    public void setNota2Readonly(Boolean nota2Readonly) {
        this.nota2Readonly = nota2Readonly;
    }

    public Boolean getNota3Readonly() {
        return nota3Readonly;
    }

    public Integer getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(Integer periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public void setNota3Readonly(Boolean nota3Readonly) {
        this.nota3Readonly = nota3Readonly;
    }

    public Boolean getNota4Readonly() {
        return nota4Readonly;
    }

    public void setNota4Readonly(Boolean nota4Readonly) {
        this.nota4Readonly = nota4Readonly;
    }

    public Boolean getNota5Readonly() {
        return nota5Readonly;
    }

    public void setNota5Readonly(Boolean nota5Readonly) {
        this.nota5Readonly = nota5Readonly;
    }

    public Boolean getNota6Readonly() {
        return nota6Readonly;
    }

    public void setNota6Readonly(Boolean nota6Readonly) {
        this.nota6Readonly = nota6Readonly;
    }

    public Boolean getNota7Readonly() {
        return nota7Readonly;
    }

    public void setNota7Readonly(Boolean nota7Readonly) {
        this.nota7Readonly = nota7Readonly;
    }

    public Boolean getNota8Readonly() {
        return nota8Readonly;
    }

    public void setNota8Readonly(Boolean nota8Readonly) {
        this.nota8Readonly = nota8Readonly;
    }

    public Boolean getNota9Readonly() {
        return nota9Readonly;
    }

    public void setNota9Readonly(Boolean nota9Readonly) {
        this.nota9Readonly = nota9Readonly;
    }

    public String getResponsavel_Erro() {
        return responsavel_Erro;
    }

    public void setResponsavel_Erro(String responsavel_Erro) {
        this.responsavel_Erro = responsavel_Erro;
    }

    public String getMensagemCalculoNotas_Erro() {
        return mensagemCalculoNotas_Erro;
    }

    public void setMensagemCalculoNotas_Erro(String mensagemCalculoNotas_Erro) {
        this.mensagemCalculoNotas_Erro = mensagemCalculoNotas_Erro;
    }

    public List getListaSelectItemDisciplinas() {
        return listaSelectItemDisciplinas;
    }

    public void setListaSelectItemDisciplinas(List listaSelectItemDisciplinas) {
        this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
    }

    public String getFrequencia_Erro() {
        return frequencia_Erro;
    }

    public void setFrequencia_Erro(String frequencia_Erro) {
        this.frequencia_Erro = frequencia_Erro;
    }

    public List getListaSelectItemPeriodosLetivos() {
        return listaSelectItemPeriodosLetivos;
    }

    public void setListaSelectItemPeriodosLetivos(List listaSelectItemPeriodosLetivos) {
        this.listaSelectItemPeriodosLetivos = listaSelectItemPeriodosLetivos;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        historicoVO = null;
        disciplina_Erro = null;
        matricula_Erro = null;
        mensagemCalculoNotas_Erro = null;
        frequencia_Erro = null;
        responsavel_Erro = null;
        semestre = null;
        ano = null;
        Uteis.liberarListaMemoria(listaSelectItemDisciplinas);
        Uteis.liberarListaMemoria(listaSelectItemPeriodosLetivos);
        nota1Readonly = null;
        nota2Readonly = null;
        nota3Readonly = null;
        nota4Readonly = null;
        nota5Readonly = null;
        nota6Readonly = null;
        nota7Readonly = null;
        nota8Readonly = null;
        nota9Readonly = null;
        nota10Readonly = null;
        nota11Readonly = null;
        nota12Readonly = null;
        nota13Readonly = null;
    }

    
    public List<SelectItem> getListaSelectItemNota1Conceito() {
        if(listaSelectItemNota1Conceito == null){
            listaSelectItemNota1Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota1Conceito;
    }

    
    public void setListaSelectItemNota1Conceito(List<SelectItem> listaSelectItemNota1Conceito) {
        this.listaSelectItemNota1Conceito = listaSelectItemNota1Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota2Conceito() {
        if(listaSelectItemNota2Conceito == null){
            listaSelectItemNota2Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota2Conceito;
    }

    
    public void setListaSelectItemNota2Conceito(List<SelectItem> listaSelectItemNota2Conceito) {
        this.listaSelectItemNota2Conceito = listaSelectItemNota2Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota3Conceito() {
        if(listaSelectItemNota3Conceito == null){
            listaSelectItemNota3Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota3Conceito;
    }

    
    public void setListaSelectItemNota3Conceito(List<SelectItem> listaSelectItemNota3Conceito) {
        this.listaSelectItemNota3Conceito = listaSelectItemNota3Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota4Conceito() {
        if(listaSelectItemNota4Conceito == null){
            listaSelectItemNota4Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota4Conceito;
    }

    
    public void setListaSelectItemNota4Conceito(List<SelectItem> listaSelectItemNota4Conceito) {
        this.listaSelectItemNota4Conceito = listaSelectItemNota4Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota5Conceito() {
        if(listaSelectItemNota5Conceito == null){
            listaSelectItemNota5Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota5Conceito;
    }

    
    public void setListaSelectItemNota5Conceito(List<SelectItem> listaSelectItemNota5Conceito) {
        this.listaSelectItemNota5Conceito = listaSelectItemNota5Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota6Conceito() {
        if(listaSelectItemNota6Conceito == null){
            listaSelectItemNota6Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota6Conceito;
    }

    
    public void setListaSelectItemNota6Conceito(List<SelectItem> listaSelectItemNota6Conceito) {
        this.listaSelectItemNota6Conceito = listaSelectItemNota6Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota7Conceito() {
        if(listaSelectItemNota7Conceito == null){
            listaSelectItemNota7Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota7Conceito;
    }

    
    public void setListaSelectItemNota7Conceito(List<SelectItem> listaSelectItemNota7Conceito) {
        this.listaSelectItemNota7Conceito = listaSelectItemNota7Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota8Conceito() {
        if(listaSelectItemNota8Conceito == null){
            listaSelectItemNota8Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota8Conceito;
    }

    
    public void setListaSelectItemNota8Conceito(List<SelectItem> listaSelectItemNota8Conceito) {
        this.listaSelectItemNota8Conceito = listaSelectItemNota8Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota9Conceito() {
        if(listaSelectItemNota9Conceito == null){
            listaSelectItemNota9Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota9Conceito;
    }

    
    public void setListaSelectItemNota9Conceito(List<SelectItem> listaSelectItemNota9Conceito) {
        this.listaSelectItemNota9Conceito = listaSelectItemNota9Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota10Conceito() {
        if(listaSelectItemNota10Conceito == null){
            listaSelectItemNota10Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota10Conceito;
    }

    
    public void setListaSelectItemNota10Conceito(List<SelectItem> listaSelectItemNota10Conceito) {
        this.listaSelectItemNota10Conceito = listaSelectItemNota10Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota11Conceito() {
        if(listaSelectItemNota11Conceito == null){
            listaSelectItemNota11Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota11Conceito;
    }

    
    public void setListaSelectItemNota11Conceito(List<SelectItem> listaSelectItemNota11Conceito) {
        this.listaSelectItemNota11Conceito = listaSelectItemNota11Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota12Conceito() {
        if(listaSelectItemNota12Conceito == null){
            listaSelectItemNota12Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota12Conceito;
    }

    
    public void setListaSelectItemNota12Conceito(List<SelectItem> listaSelectItemNota12Conceito) {
        this.listaSelectItemNota12Conceito = listaSelectItemNota12Conceito;
    }

    
    public List<SelectItem> getListaSelectItemNota13Conceito() {
        if(listaSelectItemNota13Conceito == null){
            listaSelectItemNota13Conceito = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemNota13Conceito;
    }

    
    public void setListaSelectItemNota13Conceito(List<SelectItem> listaSelectItemNota13Conceito) {
        this.listaSelectItemNota13Conceito = listaSelectItemNota13Conceito;
    }
    
    
}
