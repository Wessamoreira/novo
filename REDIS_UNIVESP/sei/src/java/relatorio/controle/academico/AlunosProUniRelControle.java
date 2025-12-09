package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosProUniRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("AlunosProUniRelControle")
@Scope("viewScope")
@Lazy
public class AlunosProUniRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private String ano;
    private String semestre;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private List listaConsultaCurso;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List listaConsultaTurma;
    private List<SelectItem> listaSelectItemUnidadeEnsino;

    public AlunosProUniRelControle() {
        limparMensagem();
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirRelatorioExcel() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunosProUniRelControle", "Inicializando Geração de Relatório Alunos Pro Uni", "Emitindo Relatório");
            getFacadeFactory().getAlunosProUniRelFacade().validarDados(getUnidadeEnsinoVO(), getCursoVO(), getAno(), getSemestre());
            List<AlunosProUniRelVO> listaAlunosProUniRelVO = getFacadeFactory().getAlunosProUniRelFacade().consultarMatriculaPorFormaIngresso(unidadeEnsinoVO.getCodigo(),
                    getCursoVO().getCodigo(), turmaVO.getCodigo(), ano, semestre);
            if (!listaAlunosProUniRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosProUniRelFacade().designIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosProUniRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Alunos PROUNI");
                getSuperParametroRelVO().setListaObjetos(listaAlunosProUniRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosProUniRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                if (getCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso((getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }
                if (getTurmaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }
                getSuperParametroRelVO().setAno(getAno() + "/" + getSemestre());
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
                registrarAtividadeUsuario(getUsuarioLogado(), "AlunosProUniRelControle", "Finalizando Geração de Relatório Alunos Pro Uni", "Emitindo Relatório");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void imprimirRelatorioPDF() {
        try {
            getFacadeFactory().getAlunosProUniRelFacade().validarDados(getUnidadeEnsinoVO(), getCursoVO(), getAno(), getSemestre());
            List<AlunosProUniRelVO> listaAlunosProUniRelVO = getFacadeFactory().getAlunosProUniRelFacade().consultarMatriculaPorFormaIngresso(unidadeEnsinoVO.getCodigo(),
                    getCursoVO().getCodigo(), turmaVO.getCodigo(), ano, semestre);
            if (!listaAlunosProUniRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosProUniRelFacade().designIReportRelatorioPDF());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosProUniRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Alunos PROUNI");
                getSuperParametroRelVO().setListaObjetos(listaAlunosProUniRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosProUniRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                if (getCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso((getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }
                if (getTurmaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }
                getSuperParametroRelVO().setAno(getAno() + "/" + getSemestre());
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(),
                    false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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

    public List<SelectItem> getListaSelectItemSemestre() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            limparDadosTurma();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosCurso() {
        try {
            getListaConsultaCurso().clear();
            setValorConsultaCurso(null);
            setCursoVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            if (getCursoVO().getCodigo().equals(0)) {
                setCursoVO(obj.getCurso());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosTurma() {
        try {
            getListaConsultaTurma().clear();
            setValorConsultaTurma(null);
            setCampoConsultaTurma(null);
            setTurmaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
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

    public Boolean getApresentarAnoSemestre() {
        return !getCursoVO().getNivelEducacionalPosGraduacao() && getCursoVO().getCodigo() != 0;
    }
}
