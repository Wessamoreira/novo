package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AberturaTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AberturaTurmaRel;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("AberturaTurmaRelControle")
@Scope("viewScope")
@Lazy
public class AberturaTurmaRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private List listaSelectItemUnidadeEnsino;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private Date dataInicio;
    private Date dataFim;
    private Date dataInicioTelaMapaAberturaTurma;
    private Date dataFimTelaMapaAberturaTurma;
    private String situacao;
    private List<UnidadeEnsinoVO> listaUnidades;
    private String unidadeEnsinoApresentar;


    public AberturaTurmaRelControle() {
        inicializarListasSelectItemTodosComboBox();
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirPDF() {
        List<AberturaTurmaRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
            getFacadeFactory().getAberturaTurmaFacade().validarDados(getDataInicio(), getDataFim(), false);

            listaObjetos = getFacadeFactory().getAberturaTurmaFacade().realizarCriacaoOjbRel(getTurmaVO().getCodigo(), getUnidadeEnsinoVOs(), getCursoVO().getCodigo(), getSituacao(), dataInicio, dataFim);
            if (listaObjetos.isEmpty()) {
                throw new Exception("Não há dados a serem exibidos no relatório.");
            }
            getSuperParametroRelVO().setNomeDesignIreport(AberturaTurmaRel.getDesignIReportRelatorio());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            if (getMarcarTodasUnidadeEnsino()) {
            	getSuperParametroRelVO().setUnidadeEnsino("Todas as Unidades");
            } else {
            	getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoApresentar());
            }
            if (getTurmaVO().getCodigo() != 0) {
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
            } else {
                getSuperParametroRelVO().setTurma("Todas as Turmas");
            }
            if (getCursoVO().getCodigo() != 0) {
                getSuperParametroRelVO().setCurso(getCursoVO().getNome());
            } else {
                getSuperParametroRelVO().setCurso("Todos os Cursos");
            }
            getSuperParametroRelVO().setPeriodo(Uteis.getData(getDataInicio()) + " a " + Uteis.getData(getDataFim()));
            getSuperParametroRelVO().setSituacao(getFacadeFactory().getAberturaTurmaFacade().validarDadosSituacaoApresentar(getSituacao()));

            getSuperParametroRelVO().setTituloRelatorio("Relatório da Abertura de Turma");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaObjetos.size());
            if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
		setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
            }
            realizarImpressaoRelatorio();
//            removerObjetoMemoria(this);
            consultarUnidadeEnsino();
            inicializarListasSelectItemTodosComboBox();
            setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirExcel() {
        List<AberturaTurmaRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
            getFacadeFactory().getAberturaTurmaFacade().validarDados(getDataInicio(), getDataFim(), false);

            listaObjetos = getFacadeFactory().getAberturaTurmaFacade().realizarCriacaoOjbRel(getTurmaVO().getCodigo(), getUnidadeEnsinoVOs(), getCursoVO().getCodigo(), getSituacao(), getDataInicio(), getDataFim());
            if (listaObjetos.isEmpty()) {
                throw new Exception("Não há dados a serem exibidos no relatório.");
            }
            getSuperParametroRelVO().setNomeDesignIreport(AberturaTurmaRel.getDesignIReportRelatorioExcel());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
            getSuperParametroRelVO().setSubReport_Dir(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            if (getMarcarTodasUnidadeEnsino()) {
            	getSuperParametroRelVO().setUnidadeEnsino("Todas as Unidades");
            } else {
            	getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoApresentar());
            }
            if (getTurmaVO().getCodigo() != 0) {
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
            } else {
                getSuperParametroRelVO().setTurma("Todas as Turmas");
            }
            if (getCursoVO().getCodigo() != 0) {
                getSuperParametroRelVO().setCurso(getCursoVO().getNome());
            } else {
                getSuperParametroRelVO().setCurso("Todos os Cursos");
            }
            getSuperParametroRelVO().setPeriodo(Uteis.getData(getDataInicio()) + " a " + Uteis.getData(getDataFim()));
            getSuperParametroRelVO().setSituacao(getFacadeFactory().getAberturaTurmaFacade().validarDadosSituacaoApresentar(getSituacao()));

            getSuperParametroRelVO().setTituloRelatorio("Relatório da Abertura de Turma");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaObjetos.size());
            realizarImpressaoRelatorio();
//          removerObjetoMemoria(this);
            consultarUnidadeEnsino();
            inicializarListasSelectItemTodosComboBox();
            setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirRelatorioExcelTelaMapaAberturaTurma() {
        List<AberturaTurmaRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Iniciando Impressao Relatorio EXCEL", "Emitindo Relatorio");
            getFacadeFactory().getAberturaTurmaFacade().validarDados(getDataInicioTelaMapaAberturaTurma(), getDataFimTelaMapaAberturaTurma(), true);

            listaObjetos = getFacadeFactory().getAberturaTurmaFacade().realizarCriacaoOjbRel(getTurmaVO().getCodigo(), getUnidadeEnsinoVOs(), getCursoVO().getCodigo(), getSituacao(), getDataInicioTelaMapaAberturaTurma(), getDataFimTelaMapaAberturaTurma());
            if (listaObjetos.isEmpty()) {
                throw new Exception("Não há dados a serem exibidos no relatório.");
            }
            getSuperParametroRelVO().setNomeDesignIreport(AberturaTurmaRel.getDesignIReportRelatorioExcel());
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
            getSuperParametroRelVO().setSubReport_Dir(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            if (getMarcarTodasUnidadeEnsino()) {
            	getSuperParametroRelVO().setUnidadeEnsino("Todas as Unidades");
            } else {
            	getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoApresentar());
            }
            if (getTurmaVO().getCodigo() != 0) {
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
            } else {
                getSuperParametroRelVO().setTurma("Todas as Turmas");
            }
            if (getCursoVO().getCodigo() != 0) {
                getSuperParametroRelVO().setCurso(getCursoVO().getNome());
            } else {
                getSuperParametroRelVO().setCurso("Todos os Cursos");
            }
            if (getDataFimTelaMapaAberturaTurma() != null) {
                getSuperParametroRelVO().setPeriodo(Uteis.getData(getDataInicioTelaMapaAberturaTurma()) + " a " + Uteis.getData(getDataFimTelaMapaAberturaTurma()));
            } else {
                getSuperParametroRelVO().setPeriodo(Uteis.getData(getDataInicioTelaMapaAberturaTurma()) + " a - ");
            }
            getSuperParametroRelVO().setSituacao(getFacadeFactory().getAberturaTurmaFacade().validarDadosSituacaoApresentar(getSituacao()));

            getSuperParametroRelVO().setTituloRelatorio("Relatório da Abertura de Turma");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(AberturaTurmaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaObjetos.size());
            realizarImpressaoRelatorio();
//          removerObjetoMemoria(this);
            consultarUnidadeEnsino();
            setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "AberturaTurmaRelControle", "Finalizando Impressao Relatorio EXCEL", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
           // System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        try {
            setListaSelectItemUnidadeEnsino(getFacadeFactory().getAberturaTurmaFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado(), getUsuarioLogado()));
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }
    List<SelectItem> tipoConsultaComboCurso;

    public List<SelectItem> getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }

    public List<SelectItem> getTipoConsultaComboSituacao() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("AC", "A confirmar"));
        itens.add(new SelectItem("AD", "Adiada"));
        itens.add(new SelectItem("CO", "Confirmada"));
        itens.add(new SelectItem("IN", "Inaugurada"));
        itens.add(new SelectItem("CA", "Cancelada"));
        return itens;
    }

    public void consultarTurma() {
        try {
            setListaConsultaTurma(getFacadeFactory().getAberturaTurmaFacade().consultarTurma(getCampoConsultaTurma(), getUnidadeEnsinoVO(), getValorConsultaTurma(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getAberturaTurmaFacade().consultarCurso(getCampoConsultaCurso(), getUnidadeEnsinoVO(), getValorConsultaCurso(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(obj);
            setTurmaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		if (getTurmaVO().getSubturma()) {
			setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		} else if (getTurmaVO().getTurmaAgrupada()) {
			setCursoVO(new CursoVO());
		} else {
			setCursoVO(getTurmaVO().getCurso());
		}
        obj = null;
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public void limparIdentificador() {
        setTurmaVO(null);
    }

    public void limparCurso() throws Exception {
        try {
            setCursoVO(null);
        } catch (Exception e) {
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
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

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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

    public Date getDataFimTelaMapaAberturaTurma() {
        return dataFimTelaMapaAberturaTurma;
    }

    public void setDataFimTelaMapaAberturaTurma(Date dataFimTelaMapaAberturaTurma) {
        this.dataFimTelaMapaAberturaTurma = dataFimTelaMapaAberturaTurma;
    }

    public Date getDataInicioTelaMapaAberturaTurma() {
        return dataInicioTelaMapaAberturaTurma;
    }

    public void setDataInicioTelaMapaAberturaTurma(Date dataInicioTelaMapaAberturaTurma) {
        this.dataInicioTelaMapaAberturaTurma = dataInicioTelaMapaAberturaTurma;
    }
    /**
     * @return the listaUnidades
     */
    public List<UnidadeEnsinoVO> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = new ArrayList();
        }
        return listaUnidades;
    }

    /**
     * @param listaUnidades the listaUnidades to set
     */
    public void setListaUnidades(List listaUnidades) {
        this.listaUnidades = listaUnidades;
    }
    
	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
//		setCursoApresentar("");
//		setTurnoApresentar("");
		consultarCursoFiltroRelatorio("");
		consultarTurnoFiltroRelatorio();
	}
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
