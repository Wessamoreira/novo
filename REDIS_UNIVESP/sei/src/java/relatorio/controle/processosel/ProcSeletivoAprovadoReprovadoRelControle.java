package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.ProcSeletivoAprovadoReprovadoRelVO;
import relatorio.negocio.jdbc.processosel.ProcSeletivoAprovadosReprovadosRel;

@SuppressWarnings("unchecked")
@Controller("ProcSeletivoAprovadoReprovadoRelControle")
@Scope("request")
@Lazy
public class ProcSeletivoAprovadoReprovadoRelControle extends SuperControleRelatorio {

    private String valorConsultaProcSeletivoAprovadoReprovado;
    private String campoConsultaProcSeletivoAprovadoReprovado;
    private String situacao;
    private static List listaSelectItemAprovadoReprovado;
    private List<ProcSeletivoAprovadoReprovadoRelVO> procSeletivoAprovadosReprovados;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemCurso;
    private Boolean exibirCursos;
    private Boolean exibirBotaoImprimir;
    private Date dataProvaInicio;
    private Date dataProvaFim;
    private String filtro;

    public ProcSeletivoAprovadoReprovadoRelControle() {
        incializarDados();
    }

    public void incializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirObjetoPDF() {
        String nomeRelatorio = "";
        String titulo = "";
        String design = "";
        try {
            setProcSeletivoAprovadosReprovados(getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().executarGeracaoListaRelatorio(
                    getSituacao(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO(), getDataProvaInicio(), getDataProvaFim(), getUsuarioLogado()));
            nomeRelatorio = ProcSeletivoAprovadosReprovadosRel.getIdEntidade();
            titulo = "Processo Seletivo - " + getItemSituacao();
            design = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getDesignIReportRelatorio();

            if (!getProcSeletivoAprovadosReprovados().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(getProcSeletivoAprovadosReprovados());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(
                        getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware("");
                getSuperParametroRelVO().setFiltros(getFiltro());

                realizarImpressaoRelatorio();

                removerObjetoMemoria(this);
                incializarDados();

                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            nomeRelatorio = null;
            titulo = null;
            design = null;
        }
    }

    public void imprimirExcel() {
        String nomeRelatorio = "";
        String titulo = "";
        String design = "";
        try {
            setProcSeletivoAprovadosReprovados(getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().executarGeracaoListaRelatorio(
                    getSituacao(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO(), getDataProvaInicio(), getDataProvaFim(), getUsuarioLogado()));
            nomeRelatorio = ProcSeletivoAprovadosReprovadosRel.getIdEntidade();
            titulo = "Processo Seletivo - " + getItemSituacao();
            design = getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getDesignIReportRelatorio();
            if (!getProcSeletivoAprovadosReprovados().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(getProcSeletivoAprovadosReprovados());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(
                        getFacadeFactory().getProcSeletivoAprovadosReprovadosFacade().getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware("");
                getSuperParametroRelVO().setFiltros(getFiltro());

                realizarImpressaoRelatorio();

                removerObjetoMemoria(this);
                incializarDados();

                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            nomeRelatorio = null;
            titulo = null;
            design = null;
        }
    }

    private void limparDadosConsulta() {
        setSituacao("");
        setUnidadeEnsinoVO(null);
        setUnidadeEnsinoCursoVO(null);
        setDataProvaFim(null);
        setDataProvaInicio(null);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemAprovadoReprovado() {
        if (listaSelectItemAprovadoReprovado == null) {
            listaSelectItemAprovadoReprovado = new ArrayList(0);
            listaSelectItemAprovadoReprovado.add(new SelectItem("", ""));
            listaSelectItemAprovadoReprovado.add(new SelectItem("ap", "Aprovado"));
            listaSelectItemAprovadoReprovado.add(new SelectItem("rp", "Reprovado"));
            listaSelectItemAprovadoReprovado.add(new SelectItem("td", "Todos"));
        }
        return listaSelectItemAprovadoReprovado;
    }

    public String getItemSituacao() {
        if (getSituacao().equals("ap")) {
            return "Aprovados";
        } else if (getSituacao().equals("rp")) {
            return "Reprovados";
        } else if (getSituacao().equals("td")) {
            return "Aprovados/Reprovados (Todos)";
        }
        return "";
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            if (resultadoConsulta == null) {
                resultadoConsulta = new ArrayList<UnidadeEnsinoCursoVO>(0);
            }
            setExibirCursos(!resultadoConsulta.isEmpty());
            setListaSelectItemCurso(new ArrayList(0));
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino,
                false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public String realizarMontarSelectItemCurso() {
        try {
            montarListaSelectItemCurso();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public String getCampoConsultaProcSeletivoAprovadoReprovado() {
        return campoConsultaProcSeletivoAprovadoReprovado;
    }

    public void setCampoConsultaProcSeletivoAprovadoReprovado(String campoConsultaProcSeletivoAprovadoReprovado) {
        this.campoConsultaProcSeletivoAprovadoReprovado = campoConsultaProcSeletivoAprovadoReprovado;
    }

    public List<ProcSeletivoAprovadoReprovadoRelVO> getProcSeletivoAprovadosReprovados() {
        return procSeletivoAprovadosReprovados;
    }

    public void setProcSeletivoAprovadosReprovados(List<ProcSeletivoAprovadoReprovadoRelVO> procSeletivoAprovadosReprovados) {
        this.procSeletivoAprovadosReprovados = procSeletivoAprovadosReprovados;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getValorConsultaProcSeletivoAprovadoReprovado() {
        return valorConsultaProcSeletivoAprovadoReprovado;
    }

    public void setValorConsultaProcSeletivoAprovadoReprovado(String valorConsultaProcSeletivoAprovadoReprovado) {
        this.valorConsultaProcSeletivoAprovadoReprovado = valorConsultaProcSeletivoAprovadoReprovado;
    }

    public List getListaSelectItemCurso() {
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
    }

    public Boolean getExibirBotaoImprimir() {
        if (!getSituacao().isEmpty() && !getUnidadeEnsinoVO().getCodigo().equals(0)) {
            exibirBotaoImprimir = true;
        } else {
            exibirBotaoImprimir = false;
        }
        return exibirBotaoImprimir;
    }

    public void setExibirBotaoImprimir(Boolean exibirBotaoImprimir) {
        this.exibirBotaoImprimir = exibirBotaoImprimir;
    }

    public Boolean getExibirCursos() {
        if (exibirCursos == null) {
            exibirCursos = false;
        }
        return exibirCursos;
    }

    public void setExibirCursos(Boolean exibirCursos) {
        this.exibirCursos = exibirCursos;
    }

    public Date getDataProvaFim() {
        return dataProvaFim;
    }

    public void setDataProvaFim(Date dataProvaFim) {
        this.dataProvaFim = dataProvaFim;
    }

    public Date getDataProvaInicio() {
        return dataProvaInicio;
    }

    public void setDataProvaInicio(Date dataProvaInicio) {
        this.dataProvaInicio = dataProvaInicio;
    }

    public String getFiltro() {
        filtro = "";
        if (getDataProvaInicio() != null && getDataProvaFim() != null) {
            filtro += Uteis.getData(getDataProvaInicio()) + " até " + Uteis.getData(getDataProvaFim());
        } else if (getDataProvaInicio() != null) {
            filtro += "Data Prova Inicial: " + Uteis.getData(getDataProvaInicio());
        } else if (getDataProvaFim() != null) {
            filtro += "Data Prova Final: " + Uteis.getData(getDataProvaFim());
        }
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }
}
