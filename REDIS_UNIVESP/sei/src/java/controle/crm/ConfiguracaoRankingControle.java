/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;
import negocio.comuns.crm.enumerador.TagFormulaConfiguracaoRankingEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoConfiguracaoRankingEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboConfiguracaoRankingEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author Paulo Taucci
 */
@Controller("ConfiguracaoRankingControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoRankingControle extends SuperControle implements Serializable {

    private ConfiguracaoRankingVO configuracaoRankingVO;
    private List listaSelectItemUnidadeEnsino;
//    private List listaSelectItemCurso;
//    private List listaSelectItemTurma;
    private PercentualConfiguracaoRankingVO percentualConfiguracaoRankingVO;
    protected List listaConsultaCurso;
    protected String valorConsultaCurso;
    protected String campoConsultaCurso;
    protected List listaConsultaTurma;
    protected String valorConsultaTurma;
    protected String campoConsultaTurma;
    private String campoFormula;
    

    public ConfiguracaoRankingControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ConfiguracaoRanking</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        setConfiguracaoRankingVO(new ConfiguracaoRankingVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ConfiguracaoRanking</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            ConfiguracaoRankingVO obj = (ConfiguracaoRankingVO) context().getExternalContext().getRequestMap().get("configuracaoRankingItens");
            obj = getFacadeFactory().getConfiguracaoRankingFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            obj.setNovoObj(new Boolean(false));
            setConfiguracaoRankingVO(obj);
            inicializarListasSelectItemTodosComboBox();
            getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemID("msg_dados_editar", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingForm.xhtml");
    }

    public void clonar() throws Exception {
        getConfiguracaoRankingVO().setCodigo(0);
        getConfiguracaoRankingVO().setUnidadeEnsino(getConfiguracaoRankingVO().getUnidadeEnsino());
        getConfiguracaoRankingVO().setCurso(getConfiguracaoRankingVO().getCurso());
        getConfiguracaoRankingVO().setTurma(getConfiguracaoRankingVO().getTurma());
        getConfiguracaoRankingVO().setPeriodoInicial(getConfiguracaoRankingVO().getPeriodoInicial());
        getConfiguracaoRankingVO().setPeriodoFinal(getConfiguracaoRankingVO().getPeriodoFinal());
        getConfiguracaoRankingVO().setNovoObj(Boolean.TRUE);
        getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO);
        getConfiguracaoRankingVO().setNome(getConfiguracaoRankingVO().getNome() + " - Clone");
        setMensagemID("msg_dados_clonados");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ConfiguracaoRanking</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getConfiguracaoRankingFacade().persistir(getConfiguracaoRankingVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingForm.xhtml");
        }
    }

    public void ativar() {
        try {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.ATIVO);
            getFacadeFactory().getConfiguracaoRankingFacade().persistir(configuracaoRankingVO, getUsuarioLogado());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO);
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void inativar() {
        try {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.INATIVO);
            getFacadeFactory().getConfiguracaoRankingFacade().persistir(configuracaoRankingVO, getUsuarioLogado());
            setMensagemID("msg_dados_inativados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO);
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getConfiguracaoRankingVO().setSituacao(TipoSituacaoConfiguracaoRankingEnum.ATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ConfiguracaoRankingCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            getControleConsultaOtimizado().setLimitePorPagina(10);
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getConfiguracaoRankingFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getCampoConsulta(), false,
                    Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getPaginaAtual(), getUsuarioLogado()));
            getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getConfiguracaoRankingFacade().consultarTotalRegistrosEncontrados(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getControleConsulta().getCampoConsulta(), false,
                    Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ConfiguracaoRankingVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getConfiguracaoRankingFacade().excluir(configuracaoRankingVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingForm.xhtml");
        }
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
//        limparValorConsulta();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoRankingCons.xhtml");
    }

    public boolean getPossibilidadeGravar() {
        if (getConfiguracaoRankingVO().getSituacao().equals(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO)) {
            return true;
        } else if (getConfiguracaoRankingVO().isNovoObj().booleanValue()) {
            return true;
        }
        return false;

    }

    public boolean getAtivar() {
        if (getConfiguracaoRankingVO().isNovoObj().booleanValue()) {
            return false;
        } else if (getConfiguracaoRankingVO().getSituacao().equals(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO)) {
            return true;
        }

        return false;
    }

    public boolean getInativar() {
        if (getConfiguracaoRankingVO().getSituacao().equals(TipoSituacaoConfiguracaoRankingEnum.ATIVO)) {
            return true;
        }
        return false;
    }

    public boolean getClonar() {
        if (getConfiguracaoRankingVO().isNovoObj().booleanValue()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        configuracaoRankingVO = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
//        Uteis.liberarListaMemoria(listaSelectItemCurso);
//        Uteis.liberarListaMemoria(listaSelectItemTurma);
    }

//    public List getListaSelectItemCurso() {
//        if (listaSelectItemCurso == null) {
//            listaSelectItemCurso = new ArrayList(0);
//        }
//        return (listaSelectItemCurso);
//    }
//
//    public void setListaSelectItemCurso(List listaSelectItemCurso) {
//        this.listaSelectItemCurso = listaSelectItemCurso;
//    }
    public ConfiguracaoRankingVO getConfiguracaoRankingVO() {
        if (configuracaoRankingVO == null) {
            configuracaoRankingVO = new ConfiguracaoRankingVO();
        }
        return configuracaoRankingVO;
    }

    public void setConfiguracaoRankingVO(ConfiguracaoRankingVO configuracaoRankingVO) {
        this.configuracaoRankingVO = configuracaoRankingVO;
    }

//    public List getListaSelectItemTurma() {
//        if (listaSelectItemTurma == null) {
//            listaSelectItemTurma = new ArrayList(0);
//        }
//        return listaSelectItemTurma;
//    }
//
//    public void setListaSelectItemTurma(List listaSelectItemTurma) {
//        this.listaSelectItemTurma = listaSelectItemTurma;
//    }
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        try {
            montarListaSelectItemUnidadeEnsino();
//            montarListaSelectItemCurso();
//            montarListaSelectItemTurma();
        } catch (Exception ex) {
            Logger.getLogger(ConfiguracaoRankingControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(null, true, getUsuarioLogado());
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        setListaSelectItemUnidadeEnsino(objs);
    }
//
//    public void montarListaSelectItemCurso() throws Exception {
//        montarListaSelectItemCurso(getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo());
//    }

//    public void montarListaSelectItemCurso(Integer unidadeEnsino) throws Exception {
//        List resultadoConsulta = getFacadeFactory().getCursoFacade().consultarPorUnidadeEnsino(unidadeEnsino, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//        Iterator i = resultadoConsulta.iterator();
//        List objs = new ArrayList(0);
//        objs.add(new SelectItem(0, ""));
//        while (i.hasNext()) {
//            CursoVO obj = (CursoVO) i.next();
//            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//        }
//        Uteis.liberarListaMemoria(resultadoConsulta);
//        setListaSelectItemCurso(objs);
//
//    }
//
//    public void montarListaSelectItemTurma() throws Exception {
//        montarListaSelectItemTurma(getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo(), getConfiguracaoRankingVO().getCurso().getCodigo());
//    }
//    public void montarListaSelectItemTurma(Integer unidadeEnsino, Integer curso) throws Exception {
//        List resultadoConsulta = getFacadeFactory().getTurmaFacade().consultarPorCurso(curso, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//        Iterator i = resultadoConsulta.iterator();
//        List objs = new ArrayList(0);
//        objs.add(new SelectItem(0, ""));
//        while (i.hasNext()) {
//            TurmaVO obj = (TurmaVO) i.next();
//            objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
//        }
//        Uteis.liberarListaMemoria(resultadoConsulta);
//        setListaSelectItemTurma(objs);
//
//    }
    public boolean getPesquisaPorSituacao() {
        if (getControleConsulta().getCampoConsulta().toUpperCase().equals(TipoConsultaComboConfiguracaoRankingEnum.SITUACAO.toString())) {
            return true;
        }
        return false;
    }

    public boolean getPesquisaPorPeriodo() {
//        if (getControleConsulta().getCampoConsulta().toUpperCase().equals(TipoConsultaComboConfiguracaoRankingEnum.PERIODO.toString())) {
//            return true;
//        }
        return false;
    }

    public List getTipoConsultaComboSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(TipoSituacaoConfiguracaoRankingEnum.ATIVO.toString(), UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.ATIVO.toString())));
        itens.add(new SelectItem(TipoSituacaoConfiguracaoRankingEnum.INATIVO.toString(), UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.INATIVO.toString())));
        itens.add(new SelectItem(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO.toString(), UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO.toString())));
        return itens;
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
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();

    }

    public void adicionarPercentual() {
        try {
            getFacadeFactory().getPercentualConfiguracaoRankingFacade().validarDados(getPercentualConfiguracaoRankingVO());
            getFacadeFactory().getConfiguracaoRankingFacade().adicionarPercentual(getConfiguracaoRankingVO().getPercentualVOs(), getPercentualConfiguracaoRankingVO());
            setPercentualConfiguracaoRankingVO(new PercentualConfiguracaoRankingVO());
            setMensagemID("msg_dados_adicionados");
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerPercentual() {
        try {
            PercentualConfiguracaoRankingVO obj = (PercentualConfiguracaoRankingVO) context().getExternalContext().getRequestMap().get("percentualItens");
            getFacadeFactory().getConfiguracaoRankingFacade().removerPercentual(getConfiguracaoRankingVO().getPercentualVOs(), obj);
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public PercentualConfiguracaoRankingVO getPercentualConfiguracaoRankingVO() {
        if (percentualConfiguracaoRankingVO == null) {
            percentualConfiguracaoRankingVO = new PercentualConfiguracaoRankingVO();
        }
        return percentualConfiguracaoRankingVO;
    }

    public void setPercentualConfiguracaoRankingVO(PercentualConfiguracaoRankingVO percentualConfiguracaoRankingVO) {
        this.percentualConfiguracaoRankingVO = percentualConfiguracaoRankingVO;
    }

    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaCurso().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
        getConfiguracaoRankingVO().setCurso(obj);
        getConfiguracaoRankingVO().setTurma(new TurmaVO());
        setCampoConsultaCurso("");
        setValorConsultaCurso("");
        setListaConsultaCurso(new ArrayList(0));
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nome", UteisJSF.internacionalizar("prt_Curso_nome")));
        itens.add(new SelectItem("codigo", UteisJSF.internacionalizar("prt_Curso_codigo")));
        return itens;
    }

    public void limparDadosCurso() {
        getConfiguracaoRankingVO().setCurso(new CursoVO());
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaTurma().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaTurma().equals("codigo")) {
                if (getValorConsultaTurma().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaTurma());
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(new Integer(valorInt), getConfiguracaoRankingVO().getCurso().getCodigo(), getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getConfiguracaoRankingVO().getCurso().getCodigo(), getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), 0, 0);
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
            getConfiguracaoRankingVO().setTurma(obj);
            getConfiguracaoRankingVO().setCurso(obj.getCurso());
            obj = null;
            valorConsultaTurma = "";
            campoConsultaTurma = "";
            listaConsultaTurma.clear();
        } catch (Exception e) {
            getConfiguracaoRankingVO().setTurma(new TurmaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", UteisJSF.internacionalizar("prt_Turma_identificador")));
        itens.add(new SelectItem("codigo", UteisJSF.internacionalizar("prt_Turma_codigo")));
        return itens;
    }

    public void limparDadosTurma() {
        getConfiguracaoRankingVO().setTurma(new TurmaVO());
    }

    public boolean getPermitirConsultaCurso() {
        return getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo() != null && getConfiguracaoRankingVO().getUnidadeEnsino().getCodigo() != 0;
    }

    public boolean getPermitirConsultaTurma() {
        return getConfiguracaoRankingVO().getCurso().getCodigo() != null && getConfiguracaoRankingVO().getCurso().getCodigo() != 0;
    }

    public void selecionarUnidadeEnsino() throws Exception {
        getConfiguracaoRankingVO().setCurso(new CursoVO());
        getConfiguracaoRankingVO().setTurma(new TurmaVO());
        setCampoConsultaCurso("");
        setValorConsultaCurso("");
        setListaConsultaCurso(new ArrayList(0));
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList(0));
    }
    
    public void adicionarCampoFormula(){
    	if(campoFormula.trim().isEmpty() || campoFormula.equals("_")){
    		campoFormula = "  ";
    	}
    	getConfiguracaoRankingVO().setFormulaCalculoComissao(getConfiguracaoRankingVO().getFormulaCalculoComissao() + "" +campoFormula);
    }
    
    public void removerCampoFormula(){
    	String formula = getConfiguracaoRankingVO().getFormulaCalculoComissao();
    	int size = formula.length();
    	for(TagFormulaConfiguracaoRankingEnum tag:TagFormulaConfiguracaoRankingEnum.values()){
    		if(getConfiguracaoRankingVO().getFormulaCalculoComissao().endsWith(tag.name())){
    			formula = formula.substring(0, formula.lastIndexOf(tag.name()));
    		}
    	}
    	if(getConfiguracaoRankingVO().getFormulaCalculoComissao().endsWith("  ")){
    		formula = formula.substring(0, formula.length()-2);
    	}
    	if(size == formula.length() && formula.length() > 0){
    		formula = formula.substring(0, formula.length()-1);
    	}
    	getConfiguracaoRankingVO().setFormulaCalculoComissao(formula);
    }
    
    public void zerarFormula(){
    	getConfiguracaoRankingVO().setFormulaCalculoComissao("");
    }

	public String getCampoFormula() {
		if(campoFormula == null){
			campoFormula = "";
		}
		return campoFormula;
	}

	public void setCampoFormula(String campoFormula) {
		this.campoFormula = campoFormula;
	}
    
    
}
