/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.MapaSituacaoAlunoInadimplenteRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.financeiro.MapaSituacaoAlunoInadimplenteRel;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("MapaSituacaoAlunoInadimplenteRelControle")
@Scope("request")
@Lazy
public class MapaSituacaoAlunoInadimplenteRelControle extends SuperControleRelatorio {

    private MapaSituacaoAlunoInadimplenteRelVO mapaSituacaoAlunoInadimplenteRelVO;
    private MatriculaVO matriculaVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    protected TurmaVO turmaVO;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemCurso;
    private List listaSelectItemTurma;
    private String ano;
    private String semestre;
    private String campoFiltro;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private List listaConsultaCurso;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List listaConsultaTurma;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaConsultaAluno;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MapaSituacaoAlunoInadimplenteRelControle() throws Exception {
        //obterUsuarioLogado();
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void montarListaSelectItemUnidadeEnsino() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void realizarImpressaoPDF() {
        List listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "MapaSituacaoAlunoInadimplenteRelControle", "Inicializando Geração de Relatório Mapa Situção Aluno", "Emitindo Relatório");
            getFacadeFactory().getMapaSituacaoAlunoInadimplenteRelFacade().validarDados(getMatriculaVO(), getTurmaVO(), getUnidadeEnsinoCursoVO().getCurso(), getUnidadeEnsinoVO().getCodigo(), getAno(), getSemestre());
            listaObjetos = getFacadeFactory().getMapaSituacaoAlunoInadimplenteRelFacade().executarGeracaoRelatorio(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getMatriculaVO(), getAno(), getSemestre());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(MapaSituacaoAlunoInadimplenteRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(MapaSituacaoAlunoInadimplenteRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Mapa Situação do Aluno");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(MapaSituacaoAlunoInadimplenteRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "MapaSituacaoAlunoInadimplenteRelControle", "Finalizando Geração de Relatório Mapa Situção Aluno", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            getListaSelectItemTurma().clear();
            getListaSelectItemCurso().clear();
            setCampoFiltro("");
            realizarLimpezaDadosAluno();
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
                removerObjetoMemoria(unidadeEnsinoCurso);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorUnidadeEnsino(codigoUnidadeEnsino, getUsuarioLogado());
        return lista;
    }

    public void inicializarDadosAposEscolhaFiltro() {
        try {
            setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            realizarLimpezaDadosAluno();
            if (getCampoFiltro().equals("turma")) {
                montarListaSelectItemTurma();
            } else if (getCampoFiltro().equals("aluno")) {
                setListaSelectItemTurma(new ArrayList(0));
                getTurmaVO().setCodigo(0);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void montarListaSelectItemTurma() throws Exception {
        List<TurmaVO> resultadoConsulta = null;
        try {
            resultadoConsulta = consultarTurmasPorCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
            setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {

            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    public void inicializarDadosAposEscolhaCurso() {
        if (getUnidadeEnsinoCursoVO().getCodigo() == 0) {
            try {
                realizarLimpezaDadosAluno();
                setCampoFiltro("");
            } catch (Exception ex) {
                setMensagemDetalhada("msg_erro", ex.getMessage());
            }
        }
    }

    private List<TurmaVO> consultarTurmasPorCurso(String nomeCurso) throws Exception {
        List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsinoVO().getCodigo(),
                getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMatriculaVO(objAluno);
            objAluno = null;
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getListaSelectItemFiltro() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("aluno", "Aluno"));
        lista.add(new SelectItem("turma", "Turma"));
        return lista;
    }

    public void realizarLimpezaDadosAluno() throws Exception {
        getListaConsultaAluno().clear();
        setValorConsultaAluno(null);
        getMatriculaVO().setMatricula("");
        getMatriculaVO().getAluno().setNome("");
        setAno(null);
        setSemestre(null);
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaCursoTurma(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
            this.setMatriculaVO(obj);
        } catch (Exception e) {
        }
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
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
            getUnidadeEnsinoCursoVO().setCurso(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            limparTurma();
            realizarLimpezaDadosAluno();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparCurso() {
        try {
            getListaConsultaCurso().clear();
            setValorConsultaCurso(null);
            getUnidadeEnsinoCursoVO().setCurso(null);
            setAno(null);
            setSemestre(null);
//            limparTurma();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurmaPorChavePrimaria() {
        try {
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
                    getUsuarioLogado()));
            if (getTurmaVO().getCodigo() == 0) {
                setTurmaVO(null);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setTurmaVO(null);
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
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
            setTurmaVO(obj);
            realizarLimpezaDadosAluno();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparTurma() {
        try {
            getListaConsultaTurma().clear();
            setValorConsultaTurma(null);
            setTurmaVO(null);
            setAno(null);
            setSemestre(null);
//            realizarLimpezaDadosAluno();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosCursoTurmaMatricula() {
        try {
            limparCurso();
            limparTurma();
            realizarLimpezaDadosAluno();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * @return the mapaSituacaoAlunoInadimplenteRelVO
     */
    public MapaSituacaoAlunoInadimplenteRelVO getMapaSituacaoAlunoInadimplenteRelVO() {
        if (mapaSituacaoAlunoInadimplenteRelVO == null) {
            mapaSituacaoAlunoInadimplenteRelVO = new MapaSituacaoAlunoInadimplenteRelVO();
        }
        return mapaSituacaoAlunoInadimplenteRelVO;
    }

    /**
     * @param mapaSituacaoAlunoInadimplenteRelVO the mapaSituacaoAlunoInadimplenteRelVO to set
     */
    public void setMapaSituacaoAlunoInadimplenteRelVO(MapaSituacaoAlunoInadimplenteRelVO mapaSituacaoAlunoInadimplenteRelVO) {
        this.mapaSituacaoAlunoInadimplenteRelVO = mapaSituacaoAlunoInadimplenteRelVO;
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
     * @return the unidadeEnsinoCursoVO
     */
    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    /**
     * @param unidadeEnsinoCursoVO the unidadeEnsinoCursoVO to set
     */
    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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
     * @return the listaSelectItemTurma
     */
    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    /**
     * @param listaSelectItemTurma the listaSelectItemTurma to set
     */
    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the semestre
     */
    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    /**
     * @param semestre the semestre to set
     */
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the campoFiltro
     */
    public String getCampoFiltro() {
        if (campoFiltro == null) {
            campoFiltro = "";
        }
        return campoFiltro;
    }

    /**
     * @param campoFiltro the campoFiltro to set
     */
    public void setCampoFiltro(String campoFiltro) {
        this.campoFiltro = campoFiltro;
    }

    public Boolean getIsApresentarCampoAluno() {
        if (getCampoFiltro().equals("aluno")) {
            return true;
        }
        return false;
    }

    public Boolean getIsApresentarCampoTurma() {
        if (getCampoFiltro().equals("turma")) {
            return true;
        }
        return false;
    }

    public Boolean getIsApresentarCampoCurso() {
        if (getUnidadeEnsinoVO().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    public Boolean getIsApresentarCampoFiltro() {
        if (getUnidadeEnsinoCursoVO().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    public Boolean getIsUnidadeEnsinoSelecionada() {
        if (getUnidadeEnsinoVO().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    /**
     * @return the ano
     */
    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    /**
     * @param ano the ano to set
     */
    public void setAno(String ano) {
        this.ano = ano;
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

    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
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

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
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

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the campoConsultaAluno
     */
    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    /**
     * @param campoConsultaAluno the campoConsultaAluno to set
     */
    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    /**
     * @return the valorConsultaAluno
     */
    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    /**
     * @param valorConsultaAluno the valorConsultaAluno to set
     */
    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    /**
     * @return the listaConsultaAluno
     */
    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    /**
     * @param listaConsultaAluno the listaConsultaAluno to set
     */
    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public boolean getIsNivelEducacionalGraduacao() {
        if ((!getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0) && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("PO")
                && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("EX")) || (!getTurmaVO().getCodigo().equals(0)
                && !getTurmaVO().getCurso().getNivelEducacional().equals("PO") && !getTurmaVO().getCurso().getNivelEducacional().equals("EX"))) {
            return true;
        }
        return false;
    }
}
