package relatorio.controle.processosel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.AlunosMatriculadosPorProcessoSeletivoRelVO;

@Controller("AlunosMatriculadosPorProcessoSeletivoRelControle")
@Scope("request")
@Lazy
public class AlunosMatriculadosPorProcessoSeletivoRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private ProcSeletivoVO procSeletivoVO;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemCurso;
    private List listaSelectItemProcSeletivo;
    public String tipoRelatorio;

    public AlunosMatriculadosPorProcessoSeletivoRelControle() throws Exception {
        inicializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void inicializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirPDF() {    
        List<AlunosMatriculadosPorProcessoSeletivoRelVO> listaObjetos;
        String retornoTipoRelatorio = "Todos";
        try {
            getFacadeFactory().getAlunosMatriculadosPorProcessoSeletivoRelFacade().validarDados(getUnidadeEnsinoCursoVO(), getTipoRelatorio());
            listaObjetos = getFacadeFactory().getAlunosMatriculadosPorProcessoSeletivoRelFacade().criarObjeto(getTipoRelatorio(), getProcSeletivoVO().getCodigo(),
                getUnidadeEnsinoCursoVO().getCodigo());

            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosPorProcessoSeletivoRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosMatriculadosPorProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Alunos Matriculados por Processo Seletivo");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(
                    getFacadeFactory().getAlunosMatriculadosPorProcessoSeletivoRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setUnidadeEnsino(
                    (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

                if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso(
                        (getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }

                if (getTipoRelatorio().equals("aprovados")) {
                    retornoTipoRelatorio = "Relatório de Vestibulandos Aprovados";
                } else if (getTipoRelatorio().equals("reprovados")) {
                    retornoTipoRelatorio = "Relatório de Vestibulandos Reprovados";
                } else if (getTipoRelatorio().equals("aprovadosMatriculados")) {
                    retornoTipoRelatorio = "Relatório de Vestibulandos Aprovados e Matriculados";
                } else if (getTipoRelatorio().equals("aprovadosNaoMatriculados")) {
                    retornoTipoRelatorio = "Relatório de Vestibulandos Aprovados e Não Matriculados";
                }
                getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
                String descricaoProcessoSeletivo = "Todos";
                if (getProcSeletivoVO().getCodigo() != null && getProcSeletivoVO().getCodigo() > 0) {
                    descricaoProcessoSeletivo = (getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(getProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getDescricao());    
                }
                getSuperParametroRelVO().setProcessoSeletivo(descricaoProcessoSeletivo);

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
        	retornoTipoRelatorio = null;
            listaObjetos = null;
            removerObjetoMemoria(this);
            inicializarDados();
        }

    }

    private void montarObjetoUnidadeEnsinoCurso() throws Exception {
        if (getUnidadeEnsinoCursoVO().getCodigo() != 0) {
            setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(),
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        }
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
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListasRelacionadas() {
        try {
            montarListaSelectItemCurso();
            montarListaSelectItemProcSeletivo();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
            setListaSelectItemCurso(null);
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
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false,
            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemProcSeletivo() throws Exception {
        List<ProcSeletivoVO> resultadoConsulta = getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoCursoVO().getUnidadeEnsino(),
            false,
                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        setListaSelectItemProcSeletivo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
    }

    public List<SelectItem> getListaTipoRelatorio() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("aprovados", "Relatório de Vestibulandos Aprovados"));
        itens.add(new SelectItem("reprovados", "Relatório de Vestibulandos Reprovados"));
        itens.add(new SelectItem("aprovadosMatriculados", "Relatório de Vestibulandos Aprovados e Matriculados"));
        itens.add(new SelectItem("aprovadosNaoMatriculados", "Relatório de Vestibulandos Aprovados e Não Matriculados"));
        return itens;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
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

    public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
        this.procSeletivoVO = procSeletivoVO;
    }

    public ProcSeletivoVO getProcSeletivoVO() {
        if (procSeletivoVO == null) {
            procSeletivoVO = new ProcSeletivoVO();
        }
        return procSeletivoVO;
    }

    public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
        this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
    }

    public List getListaSelectItemProcSeletivo() {
        if (listaSelectItemProcSeletivo == null) {
            listaSelectItemProcSeletivo = new ArrayList(0);
        }
        return listaSelectItemProcSeletivo;
    }

    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "";
        }
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }
}
