package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import static com.google.common.collect.Lists.newArrayList;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * 
 * @author Otimize-Not
 */
@Controller("AlunoNaoCursouDisciplinaRelControle")
@Scope("viewScope")
@Lazy
public class AlunoNaoCursouDisciplinaRelControle extends SuperControleRelatorio {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO;
    private List<DisciplinaVO> listaConsultaDisciplina;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String unidadeEnsinoApresentar;
    private String nomeCursoApresentar;
    private Boolean marcarTodosTodasSituacoes;

    public AlunoNaoCursouDisciplinaRelControle() {
        try {
            inicializarListaOrdenacao();
            getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoAnual(true);
            consultarUnidadeEnsino();
            consultarCursos();
            setMensagemID("msg_dados_consultados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }
    
    public String imprimirPDF() {
    	return imprimirRelatorio(TipoRelatorioEnum.PDF);
    }
    
    public String imprimirExcel() {
    	return imprimirRelatorio(TipoRelatorioEnum.EXCEL);
    }

    public String imprimirRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
    	setSuperParametroRelVO(new SuperParametroRelVO());
        List<AlunoNaoCursouDisciplinaRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunoNaoCursouDisciplinaRelControle", "Inicializando Geração de Relatório Aluno Não Cursou Disciplina", "Emitindo Relatório");
            listaObjetos = getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().consultarRelatorio(getAlunoNaoCursouDisciplinaFiltroRelVO(), getFiltroRelatorioAcademicoVO());
            if (!listaObjetos.isEmpty()) {
           		getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().designIReportRelatorio(tipoRelatorioEnum));
                getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().caminhoIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Aluno Não Cursou Disciplina");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().caminhoIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setDisciplina(getAlunoNaoCursouDisciplinaFiltroRelVO().getDisciplinaVO().getNome());
                adicionarParametroUnidadesEnsinoSelecionadas();
                adicionarParametroCursosSelecionados();
                adicionarParametroTurmaSelecionada();
                getSuperParametroRelVO().setAno(periodoRelatorio());
				if (!getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVO().getCodigo().equals(0)) {
				    getAlunoNaoCursouDisciplinaFiltroRelVO().setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				    getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVO());
				}
                realizarImpressaoRelatorio();
                inicializarListaOrdenacao();
                setMensagemID("msg_relatorio_ok");
            } else {
                throw new ConsistirException(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunoNaoCursouDisciplinaRelControle", "Inicializando Geração de Relatório Aluno Não Cursou Disciplina", "Emitindo Relatório");
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
        }
        return "editar";
    }
    
    private String periodoRelatorio() {
		if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual()) {
			return getAlunoNaoCursouDisciplinaFiltroRelVO().getAno();
		} else if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral()) {
			return getAlunoNaoCursouDisciplinaFiltroRelVO().getAnoBaseInicio() + " até " + getAlunoNaoCursouDisciplinaFiltroRelVO().getAnoBaseFim();
		} else if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral()) {
			return getAlunoNaoCursouDisciplinaFiltroRelVO().getAno() + "/" + getAlunoNaoCursouDisciplinaFiltroRelVO().getSemestre();
		}
		return "";
	}

    private void adicionarParametroTurmaSelecionada() throws Exception  {
    	if (Uteis.isAtributoPreenchido(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO())) {
            getSuperParametroRelVO().setTurma(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getIdentificadorTurma());
        } else {
            getSuperParametroRelVO().setTurma("TODAS");
        }
	}

	private void adicionarParametroCursosSelecionados() throws Exception {
    	if (getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().allMatch(CursoVO::getFiltrarCursoVO)) {
        	getSuperParametroRelVO().setCurso("TODOS");
        } else if (getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
        	getSuperParametroRelVO().setCurso(getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs()
        			.stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getNome).collect(joining(", ")));
        }
	}

	private void adicionarParametroUnidadesEnsinoSelecionadas() throws Exception {
    	if (getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().allMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
        	getSuperParametroRelVO().setUnidadeEnsino("TODAS");
        } else if (getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
        	getSuperParametroRelVO().setUnidadeEnsino(getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs()
        			.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(joining(", ")));
        }
	}

	public void subirListaOrdenacao() {
        String ordem = (String) context().getExternalContext().getRequestMap().get("ordem");
        for (int i = 1; i < getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size(); i++) {
            if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i).equals(ordem)) {
                if (i == 1) {
                    return;
                }
                String ordemTrocar = getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i - 1);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i - 1, ordem);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i, ordemTrocar);
                return;
            }
        }
    }

    public void descerListaOrdenacao() {
        String ordem = (String) context().getExternalContext().getRequestMap().get("ordem");
        for (int i = 1; i < getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size(); i++) {
            if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i).equals(ordem)) {
                if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size() == i) {
                    return;
                }
                String ordemTrocar = getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i + 1);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i + 1, ordem);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i, ordemTrocar);
                return;
            }
        }
    }

	public void consultarDisciplina() {
		try {
			if (Uteis.isAtributoPreenchido(getValorConsultaDisciplina())) {
				if (getCampoConsultaDisciplina().equals("codigo") && NumberUtils.isNumber(getValorConsultaDisciplina())) {
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());
					setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else if (getCampoConsultaDisciplina().equals("nome")) {
					setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				} else if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
					setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento_Curso(getValorConsultaDisciplina(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							getUsuarioLogado()));
				}
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("msg_entre_prmconsulta");
			}
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	 public List<SelectItem> getTipoConsultaComboDisciplina() {
	        List<SelectItem> itens = new ArrayList<>(0);
	        itens.add(new SelectItem("nome", "Nome"));
	        itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
	        itens.add(new SelectItem("codigo", "Código"));
	        return itens;
	    }

    public void inicializarListaOrdenacao() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Matrícula");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Aluno");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Curso");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Unidade Ensino");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Ano Ingresso");

    }

	public void selecionarDisciplina() {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			getAlunoNaoCursouDisciplinaFiltroRelVO().setDisciplinaVO(disciplina);
			setListaConsultaDisciplina(new ArrayList<>());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(new ArrayList<>());
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(),
						getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().filter(CursoVO::getFiltrarCursoVO).collect(toList()),
						getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(toList()),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			} else if (getCampoConsultaTurma().equals("nomeTurno")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), 
						getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().filter(CursoVO::getFiltrarCursoVO).collect(toList()),
						getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(toList()),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            if (Uteis.isAtributoPreenchido(obj)) {
            	getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(obj);
            	setListaConsultaTurma(new ArrayList<>());
            }
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosCurso() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVO(null);
        getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(null);
        getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().forEach(c -> c.setFiltrarCursoVO(false));
        verificarTodosCursosSelecionados();
    }

    public void limparDadosDisciplina() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setDisciplinaVO(null);
    }
    
    public void limparDadosTurma() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(new TurmaVO());
        setListaConsultaTurma(new ArrayList<>());
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        return itens;
    }

    public AlunoNaoCursouDisciplinaFiltroRelVO getAlunoNaoCursouDisciplinaFiltroRelVO() {
        if (alunoNaoCursouDisciplinaFiltroRelVO == null) {
            alunoNaoCursouDisciplinaFiltroRelVO = new AlunoNaoCursouDisciplinaFiltroRelVO();
        }
        return alunoNaoCursouDisciplinaFiltroRelVO;
    }

    public void setAlunoNaoCursouDisciplinaFiltroRelVO(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) {
        this.alunoNaoCursouDisciplinaFiltroRelVO = alunoNaoCursouDisciplinaFiltroRelVO;
    }

    public String getCampoConsultaDisciplina() {
        if (campoConsultaDisciplina == null) {
            campoConsultaDisciplina = "";
        }
        return campoConsultaDisciplina;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
    }

    public List<DisciplinaVO> getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public String getValorConsultaDisciplina() {
        if (valorConsultaDisciplina == null) {
            valorConsultaDisciplina = "";
        }
        return valorConsultaDisciplina;
    }

    public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
        this.valorConsultaDisciplina = valorConsultaDisciplina;
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
    
    public List<TurmaVO> getListaConsultaTurma() {
    	if (listaConsultaTurma == null) {
    		listaConsultaTurma = new ArrayList<>();
    	}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO>  listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getNomeCursoApresentar() {
		if (nomeCursoApresentar == null) {
			nomeCursoApresentar = "";
		}
		return nomeCursoApresentar;
	}

	public void setNomeCursoApresentar(String nomeCursoApresentar) {
		this.nomeCursoApresentar = nomeCursoApresentar;
	}

	public Boolean getMarcarTodosTodasSituacoes() {
		if (marcarTodosTodasSituacoes == null) {
			marcarTodosTodasSituacoes = false;
		}
		return marcarTodosTodasSituacoes;
	}

	public void setMarcarTodosTodasSituacoes(Boolean marcarTodosTodasSituacoes) {
		this.marcarTodosTodasSituacoes = marcarTodosTodasSituacoes;
	}
	
	private void consultarUnidadeEnsinoFiltroRelatorio() {
		try {
			getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().clear();
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioNomeEntidadePermissao("", "", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
				getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().forEach(u -> u.setFiltrarUnidadeEnsino(true));
			} else {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		} catch (Exception e) {
			getAlunoNaoCursouDisciplinaFiltroRelVO().setUnidadeEnsinoVOs(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		try {
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().noneMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().clear();
			} else {
				consultarCursos(true);
			}
			verificarTodosCursosSelecionados(true);
			setUnidadeEnsinoApresentar(getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(joining("; ")));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCursos() {
		try {
			consultarCursos(false);
		} catch (Exception e) {
		}
	}
	public void consultarCursos(boolean excecao) throws Exception {
		try {
			getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().clear();
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino) &&
					(getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual() || getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral() || getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral())) {
				String periodicidade = getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual() ? "AN" : getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral() ? "SE" : getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral() ? "IN" : "";
				getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", periodicidade, null,  getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			if (excecao) {
				throw e;
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public void consultarUnidadeEnsino() {
		consultarUnidadeEnsinoFiltroRelatorio();
		verificarTodasUnidadesSelecionadas();
		consultarCursos();
	}
	
	public void verificarTodosCursosSelecionados() {
		try {
			verificarTodosCursosSelecionados(false);
		} catch (Exception e) {
		}
	}
	
	public void verificarTodosCursosSelecionados(boolean excecao) {
		try {
			setNomeCursoApresentar(getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getNome).collect(joining("; ")));
		} catch (Exception e) {
			if (excecao) {
				throw e;
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public void marcarTodosCursosAction() {
		getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().forEach(this::setFiltrarCursoVO);
		verificarTodosCursosSelecionados();
	}
	
	private void setFiltrarCursoVO(CursoVO cursoVO) {
		if (Uteis.isAtributoPreenchido(cursoVO)) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().forEach(this::setFiltrarUnidadeEnsino);
		verificarTodasUnidadesSelecionadas();
	}
	
	private void setFiltrarUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO) {
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			unidadeEnsinoVO.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
	}
	
	public boolean getIsApresentarFiltroPeriodoAnoSemestre() {
		return getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO);
	}

	public void validarFiltroPeriodicidadeCursoAnual() {
		try {
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoIntegral(false);
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoSemestral(false);
			} else if (!getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral() && !getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoAnual(true);
			}
			consultarCursos(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarFiltroPeriodicidadeCursoSemestral() {
		try {
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoAnual(false);
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoIntegral(false);
			} else if (!getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual() && !getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoSemestral(true);
			}
			consultarCursos(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarFiltroPeriodicidadeCursoIntegral() {
		try {
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoIntegral()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoSemestral(false);
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoAnual(false);
			} else if (!getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoSemestral() && !getAlunoNaoCursouDisciplinaFiltroRelVO().getFiltrarCursoAnual()) {
				getAlunoNaoCursouDisciplinaFiltroRelVO().setFiltrarCursoIntegral(true);
			}
			consultarCursos(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemSemestre() {
		return newArrayList(
				new SelectItem("", " "), 
				new SelectItem("1", "1º"), 
				new SelectItem("2", "2º")
		);
	}

	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (getMarcarTodosTodasSituacoes()) {
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoes();
		} else {
			getFiltroRelatorioAcademicoVO().realizarDesmarcarTodasSituacoes();
		}
	}

	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodasSituacoes() {
		return getFiltroRelatorioAcademicoVO().getAtivo() 
				&& getFiltroRelatorioAcademicoVO().getPreMatricula() 
				&& getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada()
				&& getFiltroRelatorioAcademicoVO().getTrancado() 
				&& getFiltroRelatorioAcademicoVO().getCancelado() 
				&& getFiltroRelatorioAcademicoVO().getConcluido()
				&& getFiltroRelatorioAcademicoVO().getTransferenciaInterna()
				&& getFiltroRelatorioAcademicoVO().getTransferenciaExterna()
				&& getFiltroRelatorioAcademicoVO().getFormado()
				&& getFiltroRelatorioAcademicoVO().getAbandonado() ?
			UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos"):
		UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
	
	public boolean getCursosSelecionados() {
		try {
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
				if (getAlunoNaoCursouDisciplinaFiltroRelVO().getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
						.noneMatch(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getUnidadeEnsino()::equals)
						|| getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVOs().stream().filter(CursoVO::getFiltrarCursoVO)
						.noneMatch(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCurso()::equals)) {
					limparDadosTurma();
				}
				return true;
			} else {
				limparDadosTurma();
				return false;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}
	}
	
	public void limparValorConsultaDisciplina() {
		try {
			setValorConsultaDisciplina("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
