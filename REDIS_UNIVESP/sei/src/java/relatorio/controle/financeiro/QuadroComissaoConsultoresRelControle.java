package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;


import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.PrevisaoFaturamentoRelVO;
import relatorio.negocio.comuns.financeiro.QuadroComissaoConsultoresRelVO;
import relatorio.negocio.jdbc.financeiro.PrevisaoFaturamentoRel;
import relatorio.negocio.jdbc.financeiro.QuadroComissaoConsultoresRel;

@Controller("QuadroComissaoConsultoresRelControle")
@Scope("viewScope")
@Lazy
public class QuadroComissaoConsultoresRelControle extends SuperControleRelatorio {

    protected Date dataInicio;
    protected Date dataFim;
    private List listaSelectItemUnidadeEnsino;
    private List<CursoVO> listaSelectItemCurso;
    private List listaSelectItemTurma;
    private List listaSelectItemTurno;
    private UnidadeEnsinoVO unidadeEnsinoVO;    
    private CursoVO cursoVO;
    private TurmaVO turmaVO;
    private Boolean gerarRelatorio;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String ordenador;
    private Boolean situacaoPreMatricula;
    private Boolean matRecebida;
    private Boolean matAReceber;

    public QuadroComissaoConsultoresRelControle() throws Exception {
        inicializarDadosControle();
        setMensagemID("msg_entre_prmrelatorio");
    }

    private void inicializarDadosControle() {
        inicializarListasSelectItemTodosComboBox();
    }

    public Boolean getGerarRelatorio() {
        if (gerarRelatorio == null) {
            gerarRelatorio = false;
        }
        return gerarRelatorio;
    }

    public void setGerarRelatorio(Boolean gerarRelatorio) {
        this.gerarRelatorio = gerarRelatorio;
    }

    public void imprimirPDF() {
        List<QuadroComissaoConsultoresRelVO> listaObjetos = null;
        try {
            getFacadeFactory().getQuadroComissaoConsultoresRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo());
            listaObjetos = getFacadeFactory().getQuadroComissaoConsultoresRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoPreMatricula(), getMatRecebida(), getMatAReceber(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(QuadroComissaoConsultoresRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(QuadroComissaoConsultoresRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Quadro Comissão de Consultores");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(QuadroComissaoConsultoresRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                if (getCursoVO().getCodigo() > 0) {
                	getSuperParametroRelVO().setCurso(getCursoVO().getNome());
                } else {
                    getSuperParametroRelVO().setCurso("Todos os Cursos");
                }

                if (getTurmaVO().getCodigo() != 0) {
                    setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
                    getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("Todas as Turmas");
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDadosControle();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirExcel() {
        List<QuadroComissaoConsultoresRelVO> listaObjetos = null;
        try {
            getFacadeFactory().getQuadroComissaoConsultoresRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo());
            listaObjetos = getFacadeFactory().getQuadroComissaoConsultoresRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacaoPreMatricula(), getMatRecebida(), getMatAReceber(),  getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(PrevisaoFaturamentoRel.getDesignIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Quadro Comissão de Consultores");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                if (getCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso(getCursoVO().getNome());
                } else {
                    getSuperParametroRelVO().setCurso("Todos os Cursos");
                }

                if (getTurmaVO().getCodigo() != 0) {
                    setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
                    getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("Todas as Turmas");
                }
                if (getOrdenador().equals("nome")) {
                    getSuperParametroRelVO().setOrdenadoPor("Nome");
                } else if (getOrdenador().equals("dataVencimento")) {
                    getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
                } else if (getOrdenador().equals("tipoOrigem")) {
                    getSuperParametroRelVO().setOrdenadoPor("Tipo de Origem");
                } else if (getOrdenador().equals("parcela")) {
                    getSuperParametroRelVO().setOrdenadoPor("Parcela");
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDadosControle();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }

    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem("", ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            setListaSelectItemTurma(new ArrayList(0));
            setListaSelectItemCurso(new ArrayList(0));
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().add(new SelectItem("", ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
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

    public void consultarCurso() {
        try {
            List<CursoVO> objs = new ArrayList<CursoVO>(0);
            List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
            unidadeEnsinoVOs.add(getUnidadeEnsinoVO());
            getFacadeFactory().getPrevisaoFaturamentoRelFacade().validarDados(unidadeEnsinoVOs, getDataInicio(), getDataFim());
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNomeCurso_UnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(new Integer(valorInt), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 0 , false, getUsuarioLogado());

            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosCurso() {
        setCursoVO(new CursoVO());
    }

    public void montarTurma() throws Exception {
        try {
            if (!getTurmaVO().getIdentificadorTurma().equals("")) {
                setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
            } else {
                throw new Exception("Informe a Turma.");
            }
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setTurmaVO(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(obj);
            listaConsultaCurso.clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            montarListaTurnoCurso();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparIdentificador() {
        setTurmaVO(null);
    }

    public void selecionarTurma() throws Exception {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            setCursoVO(obj.getCurso());
            obj = null;
            valorConsultaTurma = "";
            campoConsultaTurma = "";
            listaConsultaTurma.clear();
        } catch (Exception e) {
            setTurmaVO(new TurmaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaTurnoCurso() throws Exception {
        try {
            List<TurnoVO> resultadoConsulta = consultarTurnoPorCurso();
            setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<TurnoVO> consultarTurnoPorCurso() throws Exception {
        List<TurnoVO> lista = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
    
    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        return itens;
    }


    public List getListaSelectItemOrdenador() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
        itens.add(new SelectItem("tipoOrigem", "Tipo Origem"));
        itens.add(new SelectItem("parcela", "Parcela"));
        return itens;
    }

    public void limparCurso() {
        setCursoVO(null);
        setListaSelectItemTurma(null);
        setListaSelectItemTurno(null);
        setTurmaVO(null);

    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getNewDateComUmMesAMais();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @return the listaSelectItemUnidadeEnsino
     */
    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList();
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
     * @return the campoConsultaCurso
     */
    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    /**
     * @param campoConsultaCurso the campoConsultaCurso to set
     */
    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    /**
     * @return the valorConsultaCurso
     */
    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    /**
     * @param valorConsultaCurso the valorConsultaCurso to set
     */
    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    /**
     * @return the listaConsultaCurso
     */
    public List<CursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<CursoVO>(0);
        }
        return listaConsultaCurso;
    }

    /**
     * @param listaConsultaCurso the listaConsultaCurso to set
     */
    public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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
     * @return the listaSelectItemTurno
     */
    public List getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList(0);
        }
        return listaSelectItemTurno;
    }

    /**
     * @param listaSelectItemTurno the listaSelectItemTurno to set
     */
    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public Boolean getApresentarTurno() {
        if (getCursoVO().getCodigo() != 0) {
            return true;
        }
        return false;
    }

    /**
     * @return the ordenador
     */
    public String getOrdenador() {
        if (ordenador == null) {
            ordenador = "";
        }
        return ordenador;
    }

    /**
     * @param ordenador the ordenador to set
     */
    public void setOrdenador(String ordenador) {
        this.ordenador = ordenador;
    }

    /**
     * @return the campoConsultaTurma
     */
    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    /**
     * @param campoConsultaTurma the campoConsultaTurma to set
     */
    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    /**
     * @param valorConsultaTurma the valorConsultaTurma to set
     */
    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    /**
     * @return the listaConsultaTurma
     */
    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    /**
     * @param listaConsultaTurma the listaConsultaTurma to set
     */
    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

	public Boolean getSituacaoPreMatricula() {
		if (situacaoPreMatricula == null) {
			situacaoPreMatricula = Boolean.TRUE;
		}
		return situacaoPreMatricula;
	}

	public void setSituacaoPreMatricula(Boolean situacaoPreMatricula) {
		this.situacaoPreMatricula = situacaoPreMatricula;
	}

	public Boolean getMatRecebida() {
		if (matRecebida == null) {
			matRecebida = Boolean.TRUE;
		}
		return matRecebida;
	}

	public void setMatRecebida(Boolean matRecebida) {
		this.matRecebida = matRecebida;
	}

	public Boolean getMatAReceber() {
		if (matAReceber == null) {
			matAReceber = Boolean.TRUE;
		}
		return matAReceber;
	}

	public void setMatAReceber(Boolean matAReceber) {
		this.matAReceber = matAReceber;
	}
}
