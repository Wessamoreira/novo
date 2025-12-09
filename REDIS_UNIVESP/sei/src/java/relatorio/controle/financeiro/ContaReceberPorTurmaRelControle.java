package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.financeiro.ContaReceberPorTurmaRel;

@Controller("ContaReceberPorTurmaRelControle")
@Scope("viewScope")
@Lazy
public class ContaReceberPorTurmaRelControle extends SuperControleRelatorio {

    private List listaSelectItemTurma;
    private List listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private Date dataInicio;
    private Date dataFim;
    private TurmaVO turma;
    private String situacaoContaReceber;

    public ContaReceberPorTurmaRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirRelatorioExcel() {
        List listaObjetos = null;
        String titulo = null;
        String design = null;
        try {
            getFacadeFactory().getContaReceberPorTurmaRelFacade().setOrdenarPor(getOpcaoOrdenacao().intValue());
            titulo = " Relatório de Contas a Receber Por Turma";
            design = ContaReceberPorTurmaRel.getDesignIReportRelatorioExcel();
            listaObjetos = getFacadeFactory().getContaReceberPorTurmaRelFacade().criarObjeto(false, getTurma(), getDataInicio(), getDataFim(),
                    getSituacaoContaReceber(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaReceberPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaReceberPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
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
            Uteis.liberarListaMemoria(listaObjetos);
            titulo = null;
            design = null;
        }
    }

    public void imprimirPDF() {
         List listaObjetos = null;
        String titulo = null;
        String design = null;
        try {
            getFacadeFactory().getContaReceberPorTurmaRelFacade().setOrdenarPor(getOpcaoOrdenacao().intValue());
            titulo = " Relatório de Contas a Receber Por Turma";
            listaObjetos = new ArrayList(0);
            design = ContaReceberPorTurmaRel.getDesignIReportRelatorio();
            listaObjetos = getFacadeFactory().getContaReceberPorTurmaRelFacade().criarObjeto(false, getTurma(), getDataInicio(), getDataFim(),
                    getSituacaoContaReceber(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            if (!listaObjetos.isEmpty()) {
//				apresentarRelatorioObjetos(ContaReceberPorTurmaRel.getIdEntidade(), titulo, "", "", "PDF", "", design, getUsuarioLogado().getNome(),
//						"", listaObjetos, "");
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getContaReceberPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaReceberPorTurmaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
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
             Uteis.liberarListaMemoria(listaObjetos);
            titulo = null;
            design = null;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
     * JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }

    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getTurma().setCodigo(obj.getCodigo());
        getTurma().setIdentificadorTurma(obj.getIdentificadorTurma());
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList(0));

    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getTipoConsultaComboSituacaoContaReceber() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("receber", "A Receber"));
        itens.add(new SelectItem("recebidos", "Recebidas"));
        return itens;
    }

    public void limparDadosTurma() {
        getTurma().setCodigo(0);
        getTurma().setIdentificadorTurma("");
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getContaReceberPorTurmaRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List objs = new ArrayList(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String opcao = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), opcao));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    public List getListaSelectItemTurma() {
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public List getListaConsultaTurma() {
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public String getCampoConsultaTurma() {
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public String getSituacaoContaReceber() {
        if (situacaoContaReceber == null) {
            situacaoContaReceber = "";
        }
        return situacaoContaReceber;
    }

    public void setSituacaoContaReceber(String situacaoContaReceber) {
        this.situacaoContaReceber = situacaoContaReceber;
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
}
