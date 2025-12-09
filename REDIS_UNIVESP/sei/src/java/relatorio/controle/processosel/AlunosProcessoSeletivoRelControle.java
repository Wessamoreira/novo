package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.AlunosProcessoSeletivoRelVO;

/**
 * 
 * @author Carlos
 */
@Controller("AlunosProcessoSeletivoRelControle")
@Scope("request")
@Lazy
public class AlunosProcessoSeletivoRelControle extends SuperControleRelatorio {

    private AlunosProcessoSeletivoRelVO AlunosProcessoSeletivoRelVO;
    private List listaSelectItemProcessoSeletivo;
    private ProcSeletivoVO procSeletivoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemUnidadeEnsino;
    private CursoVO cursoVO;
    private List listaSelectItemCurso;
    private Date dataInicio;
    private Date dataFim;

    public AlunosProcessoSeletivoRelControle() throws Exception {
        inicializarDados();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void inicializarDados() {
        realizarMontagemListaSelectItemProcessoSeletivo();
    }

    public void realizarImpressaoPDF() {
        List<AlunosProcessoSeletivoRelVO> listaObjetos;
        try {
            getFacadeFactory().getAlunosProcessoSeletivoRelFacade().validarDados(getUnidadeEnsinoVO(), getProcSeletivoVO(), getAlunosProcessoSeletivoRelVO(),
                getDataInicio(), getDataFim());
            listaObjetos = getFacadeFactory().getAlunosProcessoSeletivoRelFacade().realizarCriacaoObjeto(getProcSeletivoVO().getCodigo(),
                getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getDataInicio(), getDataFim(), getAlunosProcessoSeletivoRelVO().getSituacaoAluno(),
                Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Alunos Processo Seletivo");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  até  " + String.valueOf(Uteis.getData(getDataFim())));

                getSuperParametroRelVO().setUnidadeEnsino(
                    (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

                if (getCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso(
                        (getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }

                String retornoProcessoSeletivo = (getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcSeletivoVO().getCodigo(),
                    Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getDescricao();
                getSuperParametroRelVO().setProcessoSeletivo(retornoProcessoSeletivo);

                String retornoSituacao = "Todos";
                if (getAlunosProcessoSeletivoRelVO().getSituacaoAluno().equals("aprovados")) {
                    retornoSituacao = "Aprovados";
                }
                getSuperParametroRelVO().setSituacao(retornoSituacao);
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDados();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            listaObjetos = null;
        }

    }

    public void realizarMontagemListaSelectItemProcessoSeletivo() {
        try {
            setListaSelectItemProcessoSeletivo(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().realizarMontagemListaSelectItemProcessoSeletivo(
                getUnidadeEnsinoLogado().getCodigo()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarMontagemListaSelectItemUnidadeEnsino() throws Exception {
        setListaSelectItemUnidadeEnsino(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().realizarMontagemListaSelectItemUnidadeEnsino(
            getProcSeletivoVO().getCodigo().intValue()));
    }

    public void realizarMontagemListaSelectItemCurso() throws Exception {
        setListaSelectItemCurso(getFacadeFactory().getAlunosProcessoSeletivoRelFacade().realizarMontagemListaSelectItemCurso(
            getUnidadeEnsinoVO().getCodigo().intValue(), getProcSeletivoVO().getCodigo().intValue()));
    }

    public List<SelectItem> getListaSelectItemSituacaoAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("todos", "Todos"));
        itens.add(new SelectItem("aprovados", "Aprovados"));
        return itens;
    }

    /**
     * @return the AlunosProcessoSeletivoRelVO
     */
    public AlunosProcessoSeletivoRelVO getAlunosProcessoSeletivoRelVO() {
        if (AlunosProcessoSeletivoRelVO == null) {
            AlunosProcessoSeletivoRelVO = new AlunosProcessoSeletivoRelVO();
        }
        return AlunosProcessoSeletivoRelVO;
    }

    /**
     * @param AlunosProcessoSeletivoRelVO the AlunosProcessoSeletivoRelVO to set
     */
    public void setAlunosProcessoSeletivoRelVO(AlunosProcessoSeletivoRelVO AlunosProcessoSeletivoRelVO) {
        this.AlunosProcessoSeletivoRelVO = AlunosProcessoSeletivoRelVO;
    }

    /**
     * @return the listaSelectItemProcessoSeletivo
     */
    public List getListaSelectItemProcessoSeletivo() {
        if (listaSelectItemProcessoSeletivo == null) {
            listaSelectItemProcessoSeletivo = new ArrayList(0);
        }
        return listaSelectItemProcessoSeletivo;
    }

    /**
     * @param listaSelectItemProcessoSeletivo the listaSelectItemProcessoSeletivo to set
     */
    public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
        this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
    }

    /**
     * @return the procSeletivoVO
     */
    public ProcSeletivoVO getProcSeletivoVO() {
        if (procSeletivoVO == null) {
            procSeletivoVO = new ProcSeletivoVO();
        }
        return procSeletivoVO;
    }

    /**
     * @param procSeletivoVO the procSeletivoVO to set
     */
    public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
        this.procSeletivoVO = procSeletivoVO;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    /**
     * @param listaSelectItemUnidadeEnsino the listaSelectItemUnidadeEnsino to set
     */
    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the cursoVO
     */
    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    /**
     * @param cursoVO the cursoVO to set
     */
    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }

    /**
     * @return the listaSelectItemCurso
     */
    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    /**
     * @param listaSelectItemCurso the listaSelectItemCurso to set
     */
    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    /**
     * @return the dataInicio
     */
    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataFim
     */
    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
