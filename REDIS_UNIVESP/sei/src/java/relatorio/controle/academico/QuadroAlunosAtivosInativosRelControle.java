package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.QuadroAlunosAtivosInativosRel;

@SuppressWarnings("unchecked")
@Controller("QuadroAlunosAtivosInativosRelControle")
@Scope("viewScope")
@Lazy
public class QuadroAlunosAtivosInativosRelControle extends SuperControleRelatorio {

    private String ano;
    private String semestre;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private TurmaVO turma;
    private List listaSelectItemUnidadeEnsino;
    
    private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;

    public QuadroAlunosAtivosInativosRelControle() throws Exception {
        //obterUsuarioLogado();
        incializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }
    
    public void limparListasConsultas() {
		setTurma(null);
		setUnidadeEnsinoCurso(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
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
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    private void incializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "QuadroAlunosAtivosInativosRelControle", "Inicializando Geração de Relatório Quadro Alunos Ativos e Inativos", "Emitindo Relatório");
            QuadroAlunosAtivosInativosRel.validarDados(getUnidadeEnsinoVO(), getAno(), getSemestre());
            getFacadeFactory().getQuadroAlunosAtivosInativosRelFacade().setDescricaoFiltros("");
            setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            listaObjetos = getFacadeFactory().getQuadroAlunosAtivosInativosRelFacade().criarObjeto(getAno(), getSemestre(), getUnidadeEnsinoVO(), getUnidadeEnsinoCurso(), getTurma());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(QuadroAlunosAtivosInativosRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(QuadroAlunosAtivosInativosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("QUADRO DE ALUNOS ATIVOS/INATIVOS");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(QuadroAlunosAtivosInativosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
                	setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                	getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
                }
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "QuadroAlunosAtivosInativosRelControle", "Finalizando Geração de Relatório Quadro Alunos Ativos e Inativos", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void consultarCurso() {
		try {
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCurso(obj);
			limparTurma();
			getListaConsultaTurma().clear();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso(null);
			setTurma(null);
		} catch (Exception e) {
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}
	
	public void consultarTurma() {
		try {
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(),
						getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
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
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurma(), getTurma().getIdentificadorTurma(),
					getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurma().getCodigo() == 0) {
				setTurma(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
			if (getUnidadeEnsinoCurso().getCurso().getCodigo() == 0) {
				setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurma().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getTurma().getTurno().getCodigo(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
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

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO =  new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO the unidadeEnsinoVO to set
     */
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
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
	
	public boolean getApresentarCampoAno() {
		return !Uteis.isAtributoPreenchido(getUnidadeEnsinoCurso().getCurso()) || (PeriodicidadeEnum.ANUAL.getValor().equals(getUnidadeEnsinoCurso().getCurso().getPeriodicidade()) || PeriodicidadeEnum.SEMESTRAL.getValor().equals(getUnidadeEnsinoCurso().getCurso().getPeriodicidade()));
	}
	
	public boolean getApresentarCampoSemestre() {
		return !Uteis.isAtributoPreenchido(getUnidadeEnsinoCurso().getCurso()) || PeriodicidadeEnum.SEMESTRAL.getValor().equals(getUnidadeEnsinoCurso().getCurso().getPeriodicidade());
	}
	
}