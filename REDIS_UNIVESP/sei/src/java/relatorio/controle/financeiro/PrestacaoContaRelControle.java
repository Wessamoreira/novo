package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.Uteis;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.PrestacaoContaRelVO;

@Controller("PrestacaoContaRelControle")
@Scope("viewScope")
@Lazy
public class PrestacaoContaRelControle extends SuperControleRelatorio {

    private List<SelectItem> listaSelectItemTurma;
    private List listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<SelectItem> listaSelectItemOrdenacao;
    private Date dataInicio;
    private Date dataFim;
    private TurmaVO turma;
    private Integer ordenacao;
    private String tipoLayout;
    private String descricaoFiltros;
    private boolean trazerContasConvenio;

    public PrestacaoContaRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirRelatorioExcel() {
        List<PrestacaoContaRelVO> listaRecebimento = null;
        try {
            getFacadeFactory().getPrestacaoContaRelFacade().validarDados(getTurma().getCodigo());
            listaRecebimento = getFacadeFactory().getPrestacaoContaRelFacade().criarObjeto(getDataInicio(), getDataFim(),
                    getTurma(), getTrazerContasConvenio(), getOrdenacao(), getDescricaoFiltros());
            if (!listaRecebimento.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPrestacaoContaRelFacade().getDesignIReportRelatorio(getTipoLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPrestacaoContaRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Recebimento por Turma");
                getSuperParametroRelVO().setListaObjetos(listaRecebimento);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPrestacaoContaRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));

                if (getTurma().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }

                if (ordenacao.equals(1)) {
                    getSuperParametroRelVO().setOrdenadoPor("Data");
                } else {
                    getSuperParametroRelVO().setOrdenadoPor("Aluno");
                }
                persistirLayoutPadrao(getTipoLayout());
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaRecebimento);
        }
    }

    public void imprimirPDF() {
        List<PrestacaoContaRelVO> listaRecebimento = null;
        try {
            getFacadeFactory().getPrestacaoContaRelFacade().validarDados(getTurma().getCodigo());
            listaRecebimento = getFacadeFactory().getPrestacaoContaRelFacade().criarObjeto(getDataInicio(), getDataFim(),
                    getTurma(), getTrazerContasConvenio(), getOrdenacao(), getDescricaoFiltros());
            if (!listaRecebimento.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getPrestacaoContaRelFacade().getDesignIReportRelatorio(getTipoLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPrestacaoContaRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Prestação de Contas");
                getSuperParametroRelVO().setListaObjetos(listaRecebimento);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getPrestacaoContaRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaRecebimento.size());
                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));

                if (getTurma().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }

                if (ordenacao.equals(1)) {
                    getSuperParametroRelVO().setOrdenadoPor("Data");
                } else {
                    getSuperParametroRelVO().setOrdenadoPor("Aluno");
                }
                persistirLayoutPadrao(getTipoLayout());
                realizarImpressaoRelatorio();               
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {

            Uteis.liberarListaMemoria(listaRecebimento);
        }
    }

    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }
    
    private void persistirLayoutPadrao(String valor) throws Exception {
        getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "recebimento", "designPrestacaoConta", getUsuarioLogado());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, false, false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, false, false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
//            return "consultar";
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
//            return "consultar";
            return "";
        }

    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
        getTurma().setCodigo(obj.getCodigo());
        getTurma().setIdentificadorTurma(obj.getIdentificadorTurma());
        verificarLayoutPadrao();
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList<TurmaVO>(0));

    }

    /**
     * Método que ao selecionar uma pessoa para geração do histórico,
     * verifica se já existe uma preferência de layout para determinado relatório.
     * @throws Exception
     */
    private void verificarLayoutPadrao() throws Exception {
        LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
        layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("recebimento", "designPrestacaoConta", false, getUsuarioLogado());
        if (!layoutPadraoVO.getValor().equals("")) {
            setTipoLayout(layoutPadraoVO.getValor());
        }
    }

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void montarListaSelectItemOrdenacao() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("0", "Aluno"));
        itens.add(new SelectItem("1", "Data"));
        setListaSelectItemOrdenacao(itens);
    }

    public List<SelectItem> getListaTipoLayout() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("PrestacaoContaRel", "Layout 1 - Sem Observação"));
        itens.add(new SelectItem("PrestacaoContaRelComObservacao", "Layout 2 - Com Observação"));
        return itens;
    }

    public void limparDadosTurma() {
        getTurma().setCodigo(0);
        getTurma().setIdentificadorTurma("");
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
    }

    /**
     * @return the listaSelectItemTurma
     */
    public List getListaSelectItemTurma() {
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * @return the listaConsultaTurma
     */
    public List getListaConsultaTurma() {
        return listaConsultaTurma;
    }

    /**
     * @param listaConsultaTurma the listaConsultaTurma to set
     */
    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        return valorConsultaTurma;
    }

    /**
     * @param valorConsultaTurma the valorConsultaTurma to set
     */
    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    /**
     * @return the campoConsultaTurma
     */
    public String getCampoConsultaTurma() {
        return campoConsultaTurma;
    }

    /**
     * @param campoConsultaTurma the campoConsultaTurma to set
     */
    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getNewDateComMesesAMenos(1);
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public Integer getOrdenacao() {
        if (ordenacao == null) {
            ordenacao = 0;
        }
        return ordenacao;
    }

    public void setOrdenacao(Integer ordenacao) {
        this.ordenacao = ordenacao;
    }

    public String getDescricaoFiltros() {
        if (descricaoFiltros == null) {
            descricaoFiltros = new String();
        }
        return descricaoFiltros;
    }

    public void setDescricaoFiltros(String descricaoFiltros) {
        this.descricaoFiltros = descricaoFiltros;
    }

    public List<SelectItem> getListaSelectItemOrdenacao() {
        if (listaSelectItemOrdenacao == null) {
            listaSelectItemOrdenacao = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemOrdenacao;
    }

    public void setListaSelectItemOrdenacao(List<SelectItem> listaSelectItemOrdenacao) {
        this.listaSelectItemOrdenacao = listaSelectItemOrdenacao;
    }

    public boolean getIsApresentarTipoLayout() {
        if (getTurma().getCodigo().equals(0)) {
            return false;
        }
        return true;
    }

    public String getTipoLayout() {
        if (tipoLayout == null) {
            tipoLayout = "PrestacaoContaRel";
        }
        return tipoLayout;
    }

    public void setTipoLayout(String tipoLayout) {
        this.tipoLayout = tipoLayout;
    }

    public boolean getTrazerContasConvenio() {
        return trazerContasConvenio;
    }

    public void setTrazerContasConvenio(boolean trazerContasConvenio) {
        this.trazerContasConvenio = trazerContasConvenio;
    }
}
