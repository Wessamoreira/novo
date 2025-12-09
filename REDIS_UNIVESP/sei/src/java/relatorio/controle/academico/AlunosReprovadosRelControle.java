package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosReprovadosRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunosReprovadosRel;


@SuppressWarnings("unchecked")
@Controller("AlunosReprovadosRelControle")
@Scope("viewScope")
@Lazy
public class AlunosReprovadosRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private DisciplinaVO disciplinaVO;
    private String ano;
    private String semestre;
    private List<UnidadeEnsinoVO> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemCurso;
    private List<SelectItem> listaSelectItemTurma;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<DisciplinaVO> listaConsultaDisciplina;
    private List<CursoVO> listaConsultaCurso;
    private List<TurmaVO> listaConsultaTurma;
    private String tipoRelatorio;
    private List<SelectItem> tipoSelectItemSituacaoAlunoReposicao;
    private String motivoReprovacao;
    private String situacaoAlunoReposicao;
    private ComunicacaoInternaVO comunicacaoInternaVO;
    private String tags;
    private Boolean desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao;
    private Boolean desconsiderarAlunosCursandoDisciplinaAposReprovacao;
    private String layout;
    private Boolean mostrarAno;
    private Boolean mostrarSemestre;
    private Boolean filtrarSituacaoAtualMatricula;

    public AlunosReprovadosRelControle() throws Exception {
    	this.inicializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }
    
    @PostConstruct
	private void inicializarDadosPadroes(){
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "RelatorioAlunosReprovadosRel", getUsuarioLogado());
			Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "tipoRelatorio", "motivoReprovacao", "ano", "semestre", "situacaoAlunoReposicao", "layout", "filtrarSituacaoAtualMatricula" }, "RelatorioAlunosReprovadosRel");
			for (String key : camposPadroes.keySet()) {
				if (key.equals("tipoRelatorio")) {
					setTipoRelatorio(camposPadroes.get(key));
				} else if (key.equals("ano")) {
					setAno(camposPadroes.get(key));
				} else if (key.equals("semestre")) {
					setSemestre(camposPadroes.get(key));
				} else if (key.equals("motivoReprovacao")) {
					setMotivoReprovacao(camposPadroes.get(key));
				} else if (key.equals("situacaoAlunoReposicao")) {
					setSituacaoAlunoReposicao(camposPadroes.get(key));
				} else if (key.equals("layout")) {
					setLayout(camposPadroes.get(key));
				} else if (key.equals("filtrarSituacaoAtualMatricula")) {
					setFiltrarSituacaoAtualMatricula(Boolean.valueOf(camposPadroes.get(key)));
				}
				
			}
		} catch (Exception e) {

		}
	}
    
    private void inicializarDados() {
    	
        montarListaSelectItemUnidadeEnsino();
    }
    
    public void realizarEnvioMensagem(){
    	try{
    		setMensagemID("");
    		setMensagem(getFacadeFactory().getAlunosReprovadosRelFacade().realizarEnvioComunicacaoInternaAlunos(getMotivoReprovacao(), getTipoRelatorio(), getSituacaoAlunoReposicao(),
                    getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getComunicacaoInternaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(),getFiltroRelatorioAcademicoVO()));
    	}catch(Exception e){
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    public void imprimirPDF() {
        List<AlunosReprovadosRelVO> alunosReprovadosRelVOs = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunosReprovadosRelControle", "Inicializando Geração de Relatório Alunos Reprovados", "Emitindo Relatório");
            getFacadeFactory().getAlunosReprovadosRelFacade().validarDados(getTipoRelatorio(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO());
            alunosReprovadosRelVOs = getFacadeFactory().getAlunosReprovadosRelFacade().criarObjeto(getMotivoReprovacao(), getTipoRelatorio(), getSituacaoAlunoReposicao(),
                    getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(),getFiltroRelatorioAcademicoVO(), getDesconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao(), getDesconsiderarAlunosCursandoDisciplinaAposReprovacao(), getLayout(), getFiltrarSituacaoAtualMatricula());
            

            if (!alunosReprovadosRelVOs.isEmpty()) {
            	if(getLayout().equals("cursoAlunoDisciplina")){
            		getSuperParametroRelVO().setNomeDesignIreport(AlunosReprovadosRel.getDesignIReportRelatorioCursoAlunoDisciplina());
            	} else if (getLayout().equals("cursoDisciplinaAluno")) {
            		getSuperParametroRelVO().setNomeDesignIreport(AlunosReprovadosRel.getDesignIReportRelatorioCursoDisciplinaAluno());
            	}  else {
            		getSuperParametroRelVO().setNomeDesignIreport(AlunosReprovadosRel.getDesignIReportRelatorio());
            	}
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(AlunosReprovadosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Reprovados");
                getSuperParametroRelVO().setListaObjetos(alunosReprovadosRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosReprovadosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                Boolean comparacao = false;
                getSuperParametroRelVO().setPeriodo(getAno() + "/" + getSemestre());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(),Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }
                String retornoTipoRelatorio = "";
                if (getTipoRelatorio().equals("turma")) {
                    retornoTipoRelatorio = "Relatório por Turma";
                } else if (getTipoRelatorio().equals("curso")) {
                	retornoTipoRelatorio = "Relatório por Curso";
                } else {
                	retornoTipoRelatorio = "Relatório por Disciplina";
                }
                getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);

                if (getTurmaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
                } else {
                    getSuperParametroRelVO().setTurma("TODAS");
                }

                if (getDisciplinaVO().getCodigo() != null && getDisciplinaVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setDisciplina(getDisciplinaVO().getNome());
                    comparacao = true;
                }
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) { setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                getSuperParametroRelVO().setComparacao(comparacao);
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                this.inicializarDados();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            this.persistirDadosPadroes();
            registrarAtividadeUsuario(getUsuarioLogado(), "AlunosReprovadosRelControle", "Finalizando Geração de Relatório Alunos Reprovados", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
//            Uteis.liberarListaMemoria(alunosReprovadosRelVOs);
        }
    }

    public void imprimirRelatorioExcel() {
        List<AlunosReprovadosRelVO> alunosReprovadosRelVOs = null;
        try {
            getFacadeFactory().getAlunosReprovadosRelFacade().validarDados(getTipoRelatorio(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO());
            alunosReprovadosRelVOs = getFacadeFactory().getAlunosReprovadosRelFacade().criarObjeto(getMotivoReprovacao(), getTipoRelatorio(), getSituacaoAlunoReposicao(),
                    getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(),getFiltroRelatorioAcademicoVO(), getDesconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao(), getDesconsiderarAlunosCursandoDisciplinaAposReprovacao(), getLayout(), getFiltrarSituacaoAtualMatricula());

            if (!alunosReprovadosRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(AlunosReprovadosRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(AlunosReprovadosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Reprovados");
                getSuperParametroRelVO().setListaObjetos(alunosReprovadosRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosReprovadosRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware("");
                getSuperParametroRelVO().setFiltros("");

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                this.inicializarDados();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            this.persistirDadosPadroes();

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(alunosReprovadosRelVOs);
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
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            getListaSelectItemTurma().clear();
            getListaSelectItemCurso().clear();
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
                removerObjetoMemoria(unidadeEnsinoCurso);
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemTurma() throws Exception {
        getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getUnidadeEnsinoCursoVO(), getUsuarioLogado());
        List<TurmaVO> resultadoConsulta = consultarTurmasPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo());
        setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
    }

    private List<TurmaVO> consultarTurmasPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) throws Exception {
        List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void consultarDisciplinas() throws Exception {
        try {
            if (getCampoConsultaDisciplina().equals("codigo")) {
                Integer valorConsulta = 0;
                if (!getValorConsultaDisciplina().equals("")) {
                    valorConsulta = Integer.valueOf(getValorConsultaDisciplina());
                }
                setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasPorCodigoCursoCodigoDisciplina(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            } else if (getCampoConsultaDisciplina().equals("nome")) {
                setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasPorCodigoCursoNomeDisciplina(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getValorConsultaDisciplina(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getCampoConsultaTurma().equals("")) {
					setValorConsultaTurma("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoTurmaCursoEUnidadeEnsino(valorInt, getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
    
	public void selecionarTurma() throws Exception {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTurmaVO().getSubturma()) {
				setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			} else if (getTurmaVO().getTurmaAgrupada()) {
				setCursoVO(new CursoVO());
			} else {
				setCursoVO(getTurmaVO().getCurso());
			}getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


    
    public void consultarCurso() {
		try {

			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

			}

			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsinoBasica(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
//			if (getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao()) {
//				setMostrarCamposAnoSemestre(Boolean.FALSE);
//			} else {
//				setMostrarCamposAnoSemestre(Boolean.TRUE);
//			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void limparCurso() throws Exception {
		try {
			setCursoVO(new CursoVO());
			limparTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void limparTurma() throws Exception {
		try {
			setTurmaVO(new TurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}



    public void selecionarDisciplina() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
        try {
            setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            obj = null;
        }
    }

//    public void selecionarCurso() throws Exception {
//        try {
//            getUnidadeEnsinoCursoVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
//            getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getUnidadeEnsinoCursoVO(), NivelMontarDados.BASICO, getUsuarioLogado());
//            if (getTipoRelatorio().equals("turma")) {
//                montarListaSelectItemTurma();
//            }
//            if (getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao()) {
//                setMostrarCamposAnoSemestre(Boolean.FALSE);
//            } else {
//                setMostrarCamposAnoSemestre(Boolean.TRUE);
//            }
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }

    public List getTipoConsultaComboDisciplina() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("nome", "Nome"));
        lista.add(new SelectItem("codigo", "Código"));
        return lista;
    }
    
    

	public List getTipoConsultaComboCurso() {
		List<SelectItem> tipoConsultaComboCurso = new ArrayList<SelectItem>();
		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		return tipoConsultaComboCurso;
	}
	
	public List getTipoConsultaComboTurma() {
		List<SelectItem> tipoConsultaComboTurma = new ArrayList<SelectItem>();
		tipoConsultaComboTurma.add(new SelectItem("nome", "Nome"));
		tipoConsultaComboTurma.add(new SelectItem("codigo", "Código"));
		return tipoConsultaComboTurma;
	}



    public List getComboMotivoReprovacao() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("nota", "Nota"));
        lista.add(new SelectItem("falta", "Falta"));
        return lista;
    }

    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getListaSelectItemTipoRelatorio() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("curso", "Relatório por Curso"));
        lista.add(new SelectItem("turma", "Relatório por Turma"));
        lista.add(new SelectItem("disciplina", "Relatório por Disciplina"));
        return lista;
    }

    public Boolean getIsMostrarFiltrosPorTurma() {
        if (getTipoRelatorio().equals("turma")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean getIsMostrarFiltrosPorDisciplina() {
        if (getTipoRelatorio().equals("disciplina")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
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

    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
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

    public String getCampoConsultaDisciplina() {
        if (campoConsultaDisciplina == null) {
            campoConsultaDisciplina = "";
        }
        return campoConsultaDisciplina;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
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

    public List<DisciplinaVO> getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "";
        }
        return tipoRelatorio;
    }


    public void limparDadosDisciplina() {
        setDisciplinaVO(new DisciplinaVO());
    }

    public String getMotivoReprovacao() {
        if (motivoReprovacao == null) {
            motivoReprovacao = "";
        }
        return motivoReprovacao;
    }

    public void setMotivoReprovacao(String motivoReprovacao) {
        this.motivoReprovacao = motivoReprovacao;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();}
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

	public List<SelectItem> getTipoSelectItemSituacaoAlunoReposicao() {
		if(tipoSelectItemSituacaoAlunoReposicao == null){
			tipoSelectItemSituacaoAlunoReposicao = new ArrayList<SelectItem>(0);
			tipoSelectItemSituacaoAlunoReposicao.add(new SelectItem("", "Todos"));
			tipoSelectItemSituacaoAlunoReposicao.add(new SelectItem("COM_REPOSICAO", "Sim"));
			tipoSelectItemSituacaoAlunoReposicao.add(new SelectItem("SEM_REPOSICAO", "Não"));
		}
		return tipoSelectItemSituacaoAlunoReposicao;
	}

	public void setTipoSelectItemSituacaoAlunoReposicao(List<SelectItem> tipoSelectItemSituacaoAlunoReposicao) {
		this.tipoSelectItemSituacaoAlunoReposicao = tipoSelectItemSituacaoAlunoReposicao;
	}

	public String getSituacaoAlunoReposicao() {
		if(situacaoAlunoReposicao == null){
			situacaoAlunoReposicao = "";
		}
		return situacaoAlunoReposicao;
	}

	public void setSituacaoAlunoReposicao(String situacaoAlunoReposicao) {
		this.situacaoAlunoReposicao = situacaoAlunoReposicao;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if(comunicacaoInternaVO == null){
			comunicacaoInternaVO = new ComunicacaoInternaVO();
			comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(""));
			comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagem().replaceAll("\"./imagens", "\"../imagens"));
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public String getTags() {
		if(tags == null){
			tags = TagsMensagemAutomaticaEnum.NOME_ALUNO+", "+TagsMensagemAutomaticaEnum.MATRICULA+", "+
					  TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO+", "+TagsMensagemAutomaticaEnum.NOME_CURSO+", "+
					  TagsMensagemAutomaticaEnum.TURMA+", "+TagsMensagemAutomaticaEnum.NOME_DISCIPLINA;
		}
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	private void persistirDadosPadroes() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "RelatorioAlunosReprovadosRel", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getTipoRelatorio(), "RelatorioAlunosReprovadosRel", "tipoRelatorio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getAno(), "RelatorioAlunosReprovadosRel", "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSemestre(), "RelatorioAlunosReprovadosRel", "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getMotivoReprovacao(), "RelatorioAlunosReprovadosRel", "motivoReprovacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSituacaoAlunoReposicao(), "RelatorioAlunosReprovadosRel", "situacaoAlunoReposicao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getLayout(), "RelatorioAlunosReprovadosRel", "layout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getFiltrarSituacaoAtualMatricula().toString(), "RelatorioAlunosReprovadosRel", "filtrarSituacaoAtualMatricula", getUsuarioLogado());

		} catch (Exception e) {
		}

	}

	public Boolean getDesconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao() {
		if (desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao == null) {
			desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao = false;
		}
		return desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao;
	}


	public void setDesconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao(Boolean desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao) {
		this.desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao = desconsiderarAlunosQueAprovaramNaDisciplinaAposReprovacao;
	}

	public Boolean getDesconsiderarAlunosCursandoDisciplinaAposReprovacao() {
		if (desconsiderarAlunosCursandoDisciplinaAposReprovacao == null) {
			desconsiderarAlunosCursandoDisciplinaAposReprovacao = false;
		}
		return desconsiderarAlunosCursandoDisciplinaAposReprovacao;
	}

	public void setDesconsiderarAlunosCursandoDisciplinaAposReprovacao(Boolean desconsiderarAlunosCursandoDisciplinaAposReprovacao) {
		this.desconsiderarAlunosCursandoDisciplinaAposReprovacao = desconsiderarAlunosCursandoDisciplinaAposReprovacao;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "cursoTurmaAlunoDisciplina";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public List getTipoConsultaComboLayout() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("cursoTurmaAlunoDisciplina", "Curso/Turma/Aluno/Disciplina"));
		itens.add(new SelectItem("cursoAlunoDisciplina", "Curso/Aluno/Disciplina"));
		itens.add(new SelectItem("cursoDisciplinaAluno", "Curso/Disciplina/Aluno"));
		return itens;
	}

	/**
	 * @return the mostrarAno
	 */
	public Boolean getMostrarAno() {
		if (Uteis.isAtributoPreenchido(getCursoVO())) {
			if (getCursoVO().getSemestral() || getCursoVO().getAnual()) {
					return true;
				} else {
					if (!getCursoVO().getIntegral()) {
						setAno(Uteis.getAnoDataAtual());
					}
					return false;
				}
			}
			setAno("");
			return false;
	}

	public void setMostrarAno(Boolean mostrarAno) {
		this.mostrarAno = mostrarAno;
	}

	public Boolean getMostrarSemestre() {
		if (Uteis.isAtributoPreenchido(getCursoVO())) {
			if (getCursoVO().getSemestral()) {
				return true;
			} else {
				setSemestre("");
				return false;
			}
		}
		setSemestre("");
		return false;
	}

	/**
	 * @param mostrarSemestre the mostrarSemestre to set
	 */
	public void setMostrarSemestre(Boolean mostrarSemestre) {
		this.mostrarSemestre = mostrarSemestre;
	}

	public Boolean getFiltrarSituacaoAtualMatricula() {
		if (filtrarSituacaoAtualMatricula == null) {
			filtrarSituacaoAtualMatricula = false;
		}
		return filtrarSituacaoAtualMatricula;
	}

	public void setFiltrarSituacaoAtualMatricula(Boolean filtrarSituacaoAtualMatricula) {
		this.filtrarSituacaoAtualMatricula = filtrarSituacaoAtualMatricula;
	}
	
}
