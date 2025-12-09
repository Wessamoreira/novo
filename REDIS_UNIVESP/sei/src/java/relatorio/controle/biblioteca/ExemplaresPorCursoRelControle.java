package relatorio.controle.biblioteca;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;

@Controller("ExemplaresPorCursoRelControle")
@Scope("viewScope")
@Lazy
public class ExemplaresPorCursoRelControle extends SuperControleRelatorio {

    public ExemplaresPorCursoRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    private ExemplarVO exemplarVO;
	private BibliotecaVO biblioteca;
	private List listaSelectItemBiblioteca;  
    private List listaSelectItemTipoCatalogo;    
	private TipoCatalogoVO tipoCatalogo;  	
    private CursoVO curso;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List listaSelectItemPeriodoLetivo;
    private PeriodoLetivoVO periodo;

    private DisciplinaVO disciplina;
    private List listaSelectItemDisciplina;
    private List<SelectItem> listaSelectItemGradeCurricular;
    private GradeCurricularVO gradeCurricularVO;
    private ExemplaresRelVO exemplaresRelVO;
    private Date  dataInicioCompraExemplar;
    private Date  dataFimCompraExemplar;
    private Date  dataInicioAquisicaoExemplar;
    private Date  dataFimAquisicaoExemplar;
    private String tipoCatalogoPeriodico;
    
    private Boolean considerarSubTiposCatalogo;
    private Boolean considerarPlanoEnsinoCursoVinculadoAoCatalogo;

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    // if (getExemplarVO().getBiblioteca().getUnidadeEnsino().getCodigo() == 0) {
    // throw new Exception("Informe a Unidade de Ensino.");
    // }
    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultarPorCodigoEspecifico(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), true);

            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNomeEpecifico(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), true);
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDados() {
        setCurso(new CursoVO());
        setPeriodo(new PeriodoLetivoVO());
        setDisciplina(new DisciplinaVO());
        setDataInicioCompraExemplar(null);
        setDataFimCompraExemplar(null);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
    }

    public void selecionarCurso() {
        try {
            CursoVO cur = ((CursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
            getCurso().setNome(cur.getNome());
            getCurso().setCodigo(cur.getCodigo());
            getListaConsultaCurso().clear();
            montarListaSelectItemGradeCurricularTurma();
            montarListaSelectItemTipoCatalogo();            
            montarListaSelectItemBiblioteca();            			
//            montarListaSelectItemPeriodoLetivo();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
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
            listaConsultaCurso = new ArrayList<CursoVO>();
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public List getListaSelectItemPeriodoLetivo() throws Exception {
        if (listaSelectItemPeriodoLetivo == null) {
            listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);

        }
        return listaSelectItemPeriodoLetivo;
    }

	public void montarListaSelectItemPeriodoLetivo() throws Exception {
		setPeriodo(new PeriodoLetivoVO());
		setDisciplina(new DisciplinaVO());
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		listaSelectItemPeriodoLetivo.add(new SelectItem(0, ""));
		List<PeriodoLetivoVO> listaPeriodo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorCursoGradeCurricularAtiva(getCurso().getCodigo(), getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		for (PeriodoLetivoVO periodoLetivo : listaPeriodo) {
			listaSelectItemPeriodoLetivo.add(new SelectItem(periodoLetivo.getCodigo(), periodoLetivo.getDescricao()));
		}

	}

	public void montarListaSelectItemBiblioteca() throws Exception {
		getListaSelectItemBiblioteca().clear();
		List<BibliotecaVO> bibliotecaVOs = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if (bibliotecaVOs.size() == 1) {
			setBiblioteca(bibliotecaVOs.get(0));
			getBiblioteca().setCodigo(bibliotecaVOs.get(0).getCodigo());
			getListaSelectItemBiblioteca().addAll(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", false));
			getBiblioteca().setCodigo((Integer) getListaSelectItemBiblioteca().get(0).getValue());
		} else if (bibliotecaVOs.size() > 1) {
			getListaSelectItemBiblioteca().addAll(UtilSelectItem.getListaSelectItem(bibliotecaVOs, "codigo", "nome", true));
			getBiblioteca().setCodigo((Integer) getListaSelectItemBiblioteca().get(0).getValue());
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroNaoExisteBiblioteca"));
		}
	}
	
    public void montarListaSelectItemTipoCatalogo() throws Exception {
        try {
            setListaSelectItemTipoCatalogo(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTipoCatalogoFacade().consultarTipoCatalogoComboBox(false, getUsuarioLogado()), "codigo", "nome",true));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
    public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
        this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
    }

    public PeriodoLetivoVO getPeriodo() {
        if (periodo == null) {
            periodo = new PeriodoLetivoVO();

        }
        return periodo;
    }

    public void setPeriodo(PeriodoLetivoVO periodo) {
        this.periodo = periodo;
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public List getListaSelectItemDisciplina() {
        if (listaSelectItemDisciplina == null) {
            listaSelectItemDisciplina = new ArrayList<DisciplinaVO>();
        }
        return listaSelectItemDisciplina;
    }

    public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
        this.listaSelectItemDisciplina = listaSelectItemDisciplina;
    }
    
    public Boolean getApresentarGradeCurricular() {
    	return (getCurso() != null && getCurso().getCodigo() != 0);
    }

    public Boolean getApresentarPeriodoLetivo() {
        return (getGradeCurricularVO() != null && getGradeCurricularVO().getCodigo() != 0);
    }

    public Boolean getApresentarDisciplina() {
        return (getPeriodo() != null && getPeriodo().getCodigo() != 0);
    }

    public void montarListaSelectItemDisciplina() throws Exception {
        try {
        	setDisciplina(new DisciplinaVO());
            setListaSelectItemDisciplina(new ArrayList<>(0));
            listaSelectItemDisciplina.add(new SelectItem(0, ""));
            List<DisciplinaVO> listaDisciplina = getFacadeFactory().getDisciplinaFacade().consutlarDisciplinaPorCursoEPeriodoLetivo(getPeriodo().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            for (DisciplinaVO disc : listaDisciplina) {
                listaSelectItemDisciplina.add(new SelectItem(disc.getCodigo(), disc.getNome()));
            }
        } catch (Exception e) {
            setMensagemID("msg_entre_prmconsulta");
        }
    }

    public ExemplarVO getExemplarVO() {
        if (exemplarVO == null) {
            exemplarVO = new ExemplarVO();
        }
        return exemplarVO;
    }

    public void setExemplarVO(ExemplarVO exemplarVO) {
        this.exemplarVO = exemplarVO;
    }

    public void imprimirPDF() {
		List<ExemplaresRelVO> listaObjetos = null;
		try {
			getFacadeFactory().getExemplaresPorCursoRelFacade().validarDados(getCurso(), getPeriodo(), getDisciplina(), getDataInicioCompraExemplar(), getDataFimCompraExemplar() ,  getDataInicioAquisicaoExemplar(), getDataFimAquisicaoExemplar());
			listaObjetos = getFacadeFactory().getExemplaresPorCursoRelFacade().criarObjeto(getBiblioteca(), getCurso(), getDisciplina(), getDataInicioCompraExemplar(), getDataFimCompraExemplar(), getTipoCatalogo(), getTipoCatalogoPeriodico() ,  getDataInicioAquisicaoExemplar(), getDataFimAquisicaoExemplar(), getConsiderarSubTiposCatalogo(), getConsiderarPlanoEnsinoCursoVinculadoAoCatalogo());

			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExemplaresPorCursoRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getExemplaresPorCursoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Exemplares Por Curso");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getExemplaresPorCursoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().adicionarParametro("tipoCatalogoPeriodico", getTipoCatalogoPeriodico());
				if (getDisciplina().getCodigo() != 0) {
					setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				if (getPeriodo().getCodigo() != 0) {
					setPeriodo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getPeriodo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}
				getSuperParametroRelVO().setCurso(getCurso().getNome());
				if (getDisciplina().getNome() == null || getDisciplina().getNome().equals("")) {
					getSuperParametroRelVO().setDisciplina("Todas");
				} else {
					getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
				}
				if (getPeriodo().getDescricao() == null || getPeriodo().getDescricao().equals("")) {
					getSuperParametroRelVO().setPeriodoLetivo("Todos");
				} else {
					getSuperParametroRelVO().setPeriodoLetivo(getPeriodo().getDescricao());
				}
				if (Uteis.isAtributoPreenchido(getDataInicioCompraExemplar()) && Uteis.isAtributoPreenchido(getDataFimCompraExemplar())) {
					getSuperParametroRelVO().adicionarParametro("periodo", Uteis.getData(getDataInicioCompraExemplar()) + " à " + Uteis.getData(getDataFimCompraExemplar()));
				}
				if (Uteis.isAtributoPreenchido(getTipoCatalogo().getCodigo())) {
					setTipoCatalogo(getFacadeFactory().getTipoCatalogoFacade().consultarPorChavePrimaria(getTipoCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					
					if(!getConsiderarSubTiposCatalogo()) {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", getTipoCatalogo().getNome()); 
					}else {
						getSuperParametroRelVO().adicionarParametro("tipoCatalogo", getTipoCatalogo().getNome()+" e Subdivisões"); 
					}
				} else {
					getSuperParametroRelVO().adicionarParametro("tipoCatalogo", "Todos");
				}
				if (Uteis.isAtributoPreenchido(getBiblioteca().getCodigo())) {
					setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("biblioteca", getBiblioteca().getNome());
				} else {
					getSuperParametroRelVO().adicionarParametro("biblioteca", "Todas");
				}

				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				limparDados();
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
    
	public void montarListaSelectItemGradeCurricularTurma() {
		try {
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtivaPorCodigoCurso(getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()) , "codigo", "nome", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}
	
	 public Boolean getApresentarGradeFiltroDataCompraExemplar() {
	    	return Uteis.isAtributoPreenchido(getCurso().getCodigo());
	    }
	    
	    public ExemplaresRelVO getExemplaresRelVO() {
	    	if (exemplaresRelVO == null) {
	    		exemplaresRelVO = new ExemplaresRelVO();
			}
			return exemplaresRelVO;
		}

		public void setExemplaresRelVO(ExemplaresRelVO exemplaresRelVO) {
			this.exemplaresRelVO = exemplaresRelVO;
		}
		
		
		public Date getDataInicioCompraExemplar() {
			return dataInicioCompraExemplar;
		}

		public void setDataInicioCompraExemplar(Date dataInicioCompraExemplar) {
			this.dataInicioCompraExemplar = dataInicioCompraExemplar;
		}

		public Date getDataFimCompraExemplar() {
			return dataFimCompraExemplar;
		}

		public void setDataFimCompraExemplar(Date dataFimCompraExemplar) {
			this.dataFimCompraExemplar = dataFimCompraExemplar;
		}

    public List getListaSelectItemTipoCatalogo() {
        if (listaSelectItemTipoCatalogo == null) {
            listaSelectItemTipoCatalogo = new ArrayList(0);
        }
        return listaSelectItemTipoCatalogo;
    }

    public void setListaSelectItemTipoCatalogo(List listaSelectItemTipoCatalogo) {
        this.listaSelectItemTipoCatalogo = listaSelectItemTipoCatalogo;
    }

	public TipoCatalogoVO getTipoCatalogo() {
		if (tipoCatalogo == null) {
			tipoCatalogo = new TipoCatalogoVO();
		}
		return tipoCatalogo;
	}

	public void setTipoCatalogo(TipoCatalogoVO tipoCatalogo) {
		this.tipoCatalogo = tipoCatalogo;
	}

	public BibliotecaVO getBiblioteca() {
		if (biblioteca == null) {
			biblioteca = new BibliotecaVO();
		}
		return biblioteca;
	}

	public void setBiblioteca(BibliotecaVO biblioteca) {
		this.biblioteca = biblioteca;
	}

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public List<SelectItem> getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList(0);
		}
		return listaSelectItemBiblioteca;
	}
	
	public List<SelectItem> getTipoComboCatalogoPeriodico() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TODOS", "Todos"));
		itens.add(new SelectItem("CATALOGO", "Catálogo"));
		itens.add(new SelectItem("PERIODICO", "Periódico"));
		return itens;
	}
	
	public String getTipoCatalogoPeriodico() {
		if (tipoCatalogoPeriodico == null) {
			tipoCatalogoPeriodico = "TODOS";
		}
		return tipoCatalogoPeriodico;
	}

	public void setTipoCatalogoPeriodico(String tipoCatalogoPeriodico) {
		this.tipoCatalogoPeriodico = tipoCatalogoPeriodico;
	}

	public Date getDataInicioAquisicaoExemplar() {
		return dataInicioAquisicaoExemplar;
	}

	public void setDataInicioAquisicaoExemplar(Date dataInicioAquisicaoExemplar) {
		this.dataInicioAquisicaoExemplar = dataInicioAquisicaoExemplar;
	}

	public Date getDataFimAquisicaoExemplar() {
		return dataFimAquisicaoExemplar;
	}

	public void setDataFimAquisicaoExemplar(Date dataFimAquisicaoExemplar) {
		this.dataFimAquisicaoExemplar = dataFimAquisicaoExemplar;
	}
	
	public Boolean getConsiderarSubTiposCatalogo() {
		if(considerarSubTiposCatalogo == null) {
			return false;
		}
		return considerarSubTiposCatalogo;
	}

	public void setConsiderarSubTiposCatalogo(Boolean considerarSubTiposCatalogo) {
		this.considerarSubTiposCatalogo = considerarSubTiposCatalogo;
	}
	
	public Boolean getApresentarFiltroSubTiposCatalogo() {
		if(getTipoCatalogo() != null && getTipoCatalogo().getCodigo() != null && getTipoCatalogo().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public Boolean getConsiderarPlanoEnsinoCursoVinculadoAoCatalogo() {
		if(considerarPlanoEnsinoCursoVinculadoAoCatalogo == null) {
			considerarPlanoEnsinoCursoVinculadoAoCatalogo = false;
		}
		return considerarPlanoEnsinoCursoVinculadoAoCatalogo;
	}

	public void setConsiderarPlanoEnsinoCursoVinculadoAoCatalogo(Boolean considerarPlanoEnsinoCursoVinculadoAoCatalogo) {
		this.considerarPlanoEnsinoCursoVinculadoAoCatalogo = considerarPlanoEnsinoCursoVinculadoAoCatalogo;
	}
	
	
}
