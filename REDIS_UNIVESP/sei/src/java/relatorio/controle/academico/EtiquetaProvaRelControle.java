package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.biblioteca.enumeradores.TipoRelatorioEtiquetaAcademicoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisEmail;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EtiquetaProvaRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EtiquetaProvaRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Carlos
 */
@Controller("EtiquetaProvaRelControle")
@Scope("viewScope")
@Lazy
public class EtiquetaProvaRelControle extends SuperControleRelatorio {

    private EtiquetaProvaRelVO etiquetaProvaRelVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private TurmaVO turmaVO;
    private DisciplinaVO disciplinaVO;
    private List listaSelectItemUnidadeEnsino;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String ano;
    private String semestre;
    private List listaSelectItemDisciplina;
    private Boolean trazerAlunoPendenFinanceiramente;
    private TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioEtiquetaAcademico;
    private String turno;
    private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
    private Integer numeroCopias;
    private Integer coluna;
    private Integer linha;
    private LayoutEtiquetaVO layoutEtiquetaVO;
    private List<SelectItem> listaSelectItemlayoutEtiqueta;
    private List<SelectItem> listaSelectItemColuna;
    private List<SelectItem> listaSelectItemLinha;
    private List listaSelectItemProfessorTitular;
    private FuncionarioVO professor;
    private Boolean apresentarComboProfessor;

    public EtiquetaProvaRelControle() {
        setMensagemID("msg_entre_prmrelatorio");
        inicializarListasSelectItemTodosComboBox();
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public void inicializarDadosLayoutEtiqueta() {
        try {
            getListaSelectItemColuna().clear();
            getListaSelectItemLinha().clear();
            if (getLayoutEtiquetaVO().getCodigo() > 0) {
                setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
                for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
                    getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
                }
                for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
                    getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void iniciarGeracaoEtiqueta() {
        setNumeroCopias(1);
        setLinha(1);
        setColuna(1);
        setCaminhoRelatorio("");
    }
    
    public void realizarGeracaoArquivoEtiqueta() {
        try {
        	setFazerDownload(false);
            List<EtiquetaProvaRelVO> etiquetaProvaRelVOs = getFacadeFactory().getEtiquetaProvaRelFacade().consultarOjbRel(getUnidadeEnsinoVO().getCodigo(), getTurmaVO(), getDisciplinaVO().getCodigo(), getAno(), getSemestre(), getTurno(), getTrazerAlunoPendenFinanceiramente(), getTipoRelatorioEtiquetaAcademico(), getFiltroRelatorioAcademicoVO());
            if (!etiquetaProvaRelVOs.isEmpty()) {
            	if (getProfessor().getCodigo() != null && !getProfessor().getCodigo().equals(0)) {
            		etiquetaProvaRelVOs.get(0).setProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getProfessor().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getPessoa().getNome());
            	}
            	setCaminhoRelatorio(getFacadeFactory().getEtiquetaProvaRelFacade().realizarImpressaoEtiquetaProva(getLayoutEtiquetaVO(), etiquetaProvaRelVOs, getNumeroCopias(), getLinha(), getColuna(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
            	super.setFazerDownload(true);
            	limparMensagem();
            } else {
            	throw new Exception("Não há dados a serem exibidos no relatório.");
            }
        } catch (Exception e) {
        	setCaminhoRelatorio("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
	public String getDownload() {
		if (getFazerDownload()) {
			try {
				if (UteisEmail.getURLAplicacao().endsWith("/SEI/") || UteisEmail.getURLAplicacao().endsWith("/SEI") || UteisEmail.getURLAplicacao().endsWith("/SEI/faces") || UteisEmail.getURLAplicacao().endsWith("/SEI/faces/")) {
					return "location.href='../../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'; RichFaces.$('panelImprimirEtiqueta').hide();";
				}
				return "location.href='../../../DownloadRelatorioSV?relatorio=" + getCaminhoRelatorio() + "'; RichFaces.$('panelImprimirEtiqueta').hide();";
			} catch (Exception ex) {
				
			} finally {
				setFazerDownload(false);
			}
		}
		return "";
	}

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EtiquetaProvaRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
                  getFacadeFactory().getEtiquetaProvaRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo().intValue(), getTurmaVO(), getAno(), getSemestre());
            listaObjetos.add(getFacadeFactory().getEtiquetaProvaRelFacade().realizarCriacaoOjbRel(getUnidadeEnsinoVO().getCodigo(), getTurmaVO(), getDisciplinaVO().getCodigo(), getAno(), getSemestre(), getTurno(), getTrazerAlunoPendenFinanceiramente(), getTipoRelatorioEtiquetaAcademico(), getFiltroRelatorioAcademicoVO()));
                        if (listaObjetos.isEmpty()) {
                throw new Exception("Não há dados a serem exibidos no relatório.");
            }
            getSuperParametroRelVO().setNomeDesignIreport(EtiquetaProvaRel.getDesignIReportRelatorio(tipoRelatorioEtiquetaAcademico));
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(EtiquetaProvaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            if (getUnidadeEnsinoVO().getCodigo() != 0) {
                setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
            } else {
                getSuperParametroRelVO().setUnidadeEnsino("Todas as Unidades");
            }
            if (getTurmaVO().getCodigo() != 0) {
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
            } else {
                getSuperParametroRelVO().setTurma("Todas as Turmas");
            }
            getSuperParametroRelVO().setTituloRelatorio("Relatório - Etiqueta de Prova");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(EtiquetaProvaRel.getCaminhoBaseRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setQuantidade(listaObjetos.size());
            realizarImpressaoRelatorio();
            removerObjetoMemoria(this);
            inicializarListasSelectItemTodosComboBox();
            setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "EtiquetaProvaRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
    public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
    	superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
    	superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
    	superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
    	superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
    	superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
    	superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
    	superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        try {
            setListaSelectItemUnidadeEnsino(getFacadeFactory().getEtiquetaProvaRelFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado(), getUsuarioLogado()));
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public void montarListaSelectItemDisciplina() throws Exception {
        try {
        	getListaSelectItemDisciplina().clear();
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
				getListaSelectItemDisciplina().add(new SelectItem(0, ""));
				for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina() + " - CH: " + obj.getChDisciplina()));
				}
			}
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_entre_prmconsulta");
        }
    }
    
    public void montarListaSelectItemProfessorTitular() {
        try {
        	montarListaSelectItemProfessoresTurmaCoordenador("");
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void montarListaSelectItemProfessoresTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemProfessorTitular().clear();
			if (getTurmaVO().getCodigo() != null && !getTurmaVO().getCodigo().equals(0)) {
				resultadoConsulta = consultarProfessoresTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemProfessorTitular().clear();
				getListaSelectItemProfessorTitular().add(new SelectItem(0, ""));
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getListaSelectItemProfessorTitular().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarProfessoresTurmaCoordenador() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurma(getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
	}

    public void consultarTurma() {
        try {
            setListaConsultaTurma(getFacadeFactory().getEtiquetaProvaRelFacade().consultarTurma(getCampoConsultaTurma(), getUnidadeEnsinoVO(), getValorConsultaTurma(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
        if (getTurmaVO().getSubturma()) {
        	getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
        }
        setUnidadeEnsinoVO(obj.getUnidadeEnsino());
        if(!getIsApresentarSemestre()){
        	setSemestre("");
        }
        if(!getIsApresentarAno()){
        	setAno("");
        }
        montarListaSelectItemDisciplina();
        obj = null;
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public void limparIdentificador() {
        setTurmaVO(null);
        getListaSelectItemDisciplina().clear();
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getTipoConsultaComboSemestre() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("1", "1º"));
        itens.add(new SelectItem("2", "2º"));
        return itens;
    }

    public EtiquetaProvaRelVO getEtiquetaProvaRelVO() {
        if (etiquetaProvaRelVO == null) {
            etiquetaProvaRelVO = new EtiquetaProvaRelVO();
        }
        return etiquetaProvaRelVO;
    }

    public void setEtiquetaProvaRelVO(EtiquetaProvaRelVO etiquetaProvaRelVO) {
        this.etiquetaProvaRelVO = etiquetaProvaRelVO;
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

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public List getListaSelectItemDisciplina() {
        if (listaSelectItemDisciplina == null) {
            listaSelectItemDisciplina = new ArrayList(0);
        }
        return listaSelectItemDisciplina;
    }

    public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
        this.listaSelectItemDisciplina = listaSelectItemDisciplina;
    }

    public boolean getApresentarAnoSemestre() {
        return getTurmaVO().getCodigo() != 0 && getTurmaVO().getPeriodicidade().equals("SE");
    }
    public boolean getApresentarAno() {
    	return getTurmaVO().getCodigo() != 0 && !getTurmaVO().getPeriodicidade().equals("IN");
    }

    public boolean getApresentarComboDisciplina() {
        return !getListaSelectItemDisciplina().isEmpty();
    }
    
    public boolean getApresentarCurso() {
        return getTurmaVO().getCodigo() != 0;
    }

    public Boolean getTrazerAlunoPendenFinanceiramente() {
        if (trazerAlunoPendenFinanceiramente == null) {
            trazerAlunoPendenFinanceiramente = Boolean.TRUE;
        }
        return trazerAlunoPendenFinanceiramente;
    }

    public void setTrazerAlunoPendenFinanceiramente(Boolean trazerAlunoPendenFinanceiramente) {
        this.trazerAlunoPendenFinanceiramente = trazerAlunoPendenFinanceiramente;
    }

      public TipoRelatorioEtiquetaAcademicoEnum getTipoRelatorioEtiquetaAcademico() {
        if(tipoRelatorioEtiquetaAcademico == null){
            tipoRelatorioEtiquetaAcademico = TipoRelatorioEtiquetaAcademicoEnum.ETIQUETA_PROVA;
        }
        return tipoRelatorioEtiquetaAcademico;
    }

    public void setTipoRelatorioEtiquetaAcademico(TipoRelatorioEtiquetaAcademicoEnum tipoRelatorioEtiquetaAcademico) {
        this.tipoRelatorioEtiquetaAcademico = tipoRelatorioEtiquetaAcademico;
    }

    public String getTurno() {
        if(turno == null){
            turno = "";
        }
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
    
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public Integer getNumeroCopias() {
		if (numeroCopias == null) {
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

    public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
        if (listaSelectItemlayoutEtiqueta == null) {
            listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
            try {
                List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.PROVA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
                listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
                for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
                    listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            }
        }
        return listaSelectItemlayoutEtiqueta;
    }

	public void setListaSelectItemlayoutEtiqueta(List<SelectItem> listaSelectItemlayoutEtiqueta) {
		this.listaSelectItemlayoutEtiqueta = listaSelectItemlayoutEtiqueta;
	}

	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	public List getListaSelectItemProfessorTitular() {
		if (listaSelectItemProfessorTitular == null) {
			listaSelectItemProfessorTitular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessorTitular;
	}

	public void setListaSelectItemProfessorTitular(List listaSelectItemProfessorTitular) {
		this.listaSelectItemProfessorTitular = listaSelectItemProfessorTitular;
	}

	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	public void setProfessor(FuncionarioVO professor) {
		this.professor = professor;
	}

	public Boolean getApresentarComboProfessor() {
		return !getListaSelectItemProfessorTitular().isEmpty();
	}

	public Boolean getIsApresentarAno() {
		return ((getTurmaVO().getPeriodicidade().equals("AN") || getTurmaVO().getPeriodicidade().equals("SE")) && !getTurmaVO().getCurso().getCodigo().equals(0));
	}

	public Boolean getIsApresentarSemestre() {
		return getTurmaVO().getPeriodicidade().equals("SE");
	}


}
