package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.MapaFinanceiroAlunosRelVO;

@Controller("MapaFinanceiroAlunosRelControle")
@Scope("viewScope")
@Lazy
public class MapaFinanceiroAlunosRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private TurmaVO turmaVO;
    private String ano;
    private String semestre;
    private String tipoRelatorio;
    private MapaFinanceiroAlunosRelVO mapaFinanceiroAlunosRelVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemCurso;
    private List<SelectItem> listaSelectItemTurma;
    private List<SelectItem> listaSelectItemTipoRelatorio;
    private List<SelectItem> listaSelectItemSemestre;
    private Boolean trazerApenasAlunosAtivos;

    public MapaFinanceiroAlunosRelControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
    }

    public void novo() {
        try {
            montarListaSelectItemUnidadeEnsino();
        } catch (Exception e) {
            setMensagemID(e.getMessage());
        }
    }

    public void imprimirPDFSintetico() {
        List<MapaFinanceiroAlunosRelVO> listaObjetos = null;
        String retornoTipoRelatorio = null;
        try {
            getFacadeFactory().getMapaFinanceiroAlunosRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTipoRelatorio(), getAno(), getSemestre(), getUnidadeEnsinoCursoVO().getCurso(), getUnidadeEnsinoCursoVO());
            listaObjetos = getFacadeFactory().getMapaFinanceiroAlunosRelFacade().criarLista(getTrazerApenasAlunosAtivos(), getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getMapaFinanceiroAlunosRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getMapaFinanceiroAlunosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Mapa Financeiro de Alunos");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getMapaFinanceiroAlunosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                getFacadeFactory().getMapaFinanceiroAlunosRelFacade().calcularSomatoriosParcelas(listaObjetos, getSuperParametroRelVO());
                if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }
                if (getTurmaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }
                retornoTipoRelatorio = "";
                if (getTipoRelatorio().equals("s")) {
                    retornoTipoRelatorio = "Relatório Sintético";
                } else {
                    retornoTipoRelatorio = "Relatório Analítico";
                }
                getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
                getSuperParametroRelVO().setAno(getAno());
                if (!getSemestre().equals("")) {
                	getSuperParametroRelVO().setSemestre(getSemestre() + "º");
                } else {
                	getSuperParametroRelVO().setSemestre("");
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
            retornoTipoRelatorio = null;
        }
    }

    private void montarListaSelectItemUnidadeEnsino() throws Exception {
        List<SelectItem> listaTemp = new ArrayList<SelectItem>(0);
        List<UnidadeEnsinoVO> lista = null;
        try {
            lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            listaTemp.add(new SelectItem(0, ""));
            for (UnidadeEnsinoVO unidadeEnsinoVO : lista) {
                listaTemp.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
                removerObjetoMemoria(unidadeEnsinoVO);
            }
            setListaSelectItemUnidadeEnsino(listaTemp);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(lista);
            //Uteis.liberarListaMemoria(listaTemp);
            //listaTemp = null;
        }
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<SelectItem> listaTemp = new ArrayList<SelectItem>(0);
        try {
            List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(),
                    false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            listaTemp.add(new SelectItem(0, ""));
            for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : lista) {
                listaTemp.add(new SelectItem(unidadeEnsinoCursoVO.getCodigo(), unidadeEnsinoCursoVO.getNomeCursoTurno()));
            }
            setListaSelectItemCurso(listaTemp);
            montarListaSelectItemTipoRelatorio();
            montarListaSelectItemSemestre();
            getListaSelectItemTurma().clear();
            setTurmaVO(null);
            setUnidadeEnsinoCursoVO(null);
            setAno(null);
            setSemestre(null);
            setTipoRelatorio(null);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            listaTemp = null;
        }
    }

    public void montarListaSelectItemTurma() throws Exception {
        List<SelectItem> listaTemp = new ArrayList<SelectItem>(0);
        try {
            if (!getUnidadeEnsinoCursoVO().getCodigo().equals(0)) {
                setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorTurnoCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            listaTemp.add(new SelectItem(0, ""));
            for (TurmaVO turmaVO : lista) {
                listaTemp.add(new SelectItem(turmaVO.getCodigo(), turmaVO.getIdentificadorTurma()));
            }
            setListaSelectItemTurma(listaTemp);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            listaTemp = null;
        }
    }

    public void montarListaSelectItemTipoRelatorio() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("s", "Relatório Sintético"));
        setListaSelectItemTipoRelatorio(lista);
        lista = null;
    }

    public void montarListaSelectItemSemestre() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        setListaSelectItemSemestre(lista);
        lista = null;
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
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

    public MapaFinanceiroAlunosRelVO getMapaFinanceiroAlunosRelVO() {
        if (mapaFinanceiroAlunosRelVO == null) {
            mapaFinanceiroAlunosRelVO = new MapaFinanceiroAlunosRelVO();
        }
        return mapaFinanceiroAlunosRelVO;
    }

    public void setMapaFinanceiroAlunosRelVO(MapaFinanceiroAlunosRelVO mapaFinanceiroAlunosRelVO) {
        this.mapaFinanceiroAlunosRelVO = mapaFinanceiroAlunosRelVO;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public List<SelectItem> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public List<SelectItem> getListaSelectItemTipoRelatorio() {
        if (listaSelectItemTipoRelatorio == null) {
            listaSelectItemTipoRelatorio = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemTipoRelatorio;
    }

    public void setListaSelectItemTipoRelatorio(List<SelectItem> listaSelectItemTipoRelatorio) {
        this.listaSelectItemTipoRelatorio = listaSelectItemTipoRelatorio;
    }

    public List<SelectItem> getListaSelectItemSemestre() {
        if (listaSelectItemSemestre == null) {
            listaSelectItemSemestre = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemSemestre;
    }

    public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
        this.listaSelectItemSemestre = listaSelectItemSemestre;
    }

    public boolean getIsNivelCursoGraduacao() {
        if (!getUnidadeEnsinoCursoVO().getCodigo().equals(0) && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao() && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("EX")) {
            return true;
        }
        return false;
    }

	public Boolean getTrazerApenasAlunosAtivos() {
		if (trazerApenasAlunosAtivos == null) {
			trazerApenasAlunosAtivos = Boolean.TRUE;
		}
		return trazerApenasAlunosAtivos;
	}

	public void setTrazerApenasAlunosAtivos(Boolean trazerApenasAlunosAtivos) {
		this.trazerApenasAlunosAtivos = trazerApenasAlunosAtivos;
	}
}
