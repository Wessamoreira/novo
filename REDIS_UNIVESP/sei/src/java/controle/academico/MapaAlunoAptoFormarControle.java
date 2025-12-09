package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MapaAlunoAptoFormarVO;
import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("MapaAlunoAptoFormarControle")
@Lazy
@Scope("viewScope")
public class MapaAlunoAptoFormarControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -761814048905689780L;
	private String tipoFiltro;
	private MatriculaVO matricula;
	private TurmaVO turmaVO;
	private CursoVO cursoVO;
    protected String valorConsultaAluno;
    protected String campoConsultaAluno;
    protected List listaConsultaAluno;
	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<TurmaVO> listaConsultaTurma;
	private String popUp;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<MapaAlunoAptoFormarVO> mapaAlunoAptoFormarVOs;
	private Boolean permiteModificarDados;
	private boolean navegarParaMinhasNotasControle = false;
	private String situacaoCurricular;

	public MapaAlunoAptoFormarControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		verificaPermissaoModificarDados();
		setMensagemID("msg_entre_prmconsulta");
	}
	
    public void verificaPermissaoModificarDados() {
        Boolean liberar = false;
        try {
        	ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MapaLocalAulaTurma_PermiteModificarDados", getUsuarioLogado());
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPermiteModificarDados(liberar);
    }
	
	public void persistir() {
		try {
			MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurma");
			mapaLocalAulaTurmaVO.getTurmaDisciplina().setTurma(mapaLocalAulaTurmaVO.getTurma().getCodigo().intValue());
			getFacadeFactory().getTurmaDisciplinaFacade().alterarLocalSala(mapaLocalAulaTurmaVO.getTurmaDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void notificar() {
		try {
			MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurma");
                        getFacadeFactory().getTurmaFacade().carregarDados(mapaLocalAulaTurmaVO.getTurma(), getUsuarioLogado());
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoLocalAulaTurma(mapaLocalAulaTurmaVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getGrupoDestinatarioMapaLocalAula().getCodigo(), getUsuarioLogado());
			setMensagemID("msg_msg_emailsEnviados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("registroAcademico")) {
            	objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try{
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        setMatricula(obj);
        limparCurso();
        limparTurma();
        }catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparMatriculaAluno() {
        setMatricula(new MatriculaVO());
        getMapaAlunoAptoFormarVOs().clear();
    }
	
	public void cancelarVindoVisaoAdministrativo() {
        try {
        	MapaAlunoAptoFormarVO obj = (MapaAlunoAptoFormarVO) context().getExternalContext().getRequestMap().get("mapaAlunoAptoFormarItens");
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();            
            request.getSession().setAttribute("codigo", obj.getMatriculaPeriodo().getMatriculaVO().getMatricula());
            removerControleMemoriaFlashTela("LiberacaoFinanceiroCancelamentoTrancamentoControle");
            setPopUp("abrirPopup('../financeiro/liberacaoFinanceiroCancelamentoTrancamentoForm.xhtml','liberacaoFinanceiroCancelamento', 950, 595);");
        } catch (Exception e) {
            setPopUp("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}
	
	public void formarVindoVisaoAdministrativo() {
        try {        	
        	MapaAlunoAptoFormarVO obj = (MapaAlunoAptoFormarVO) context().getExternalContext().getRequestMap().get("mapaAlunoAptoFormarItens");
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            request.getSession().setAttribute("matricula", obj.getMatriculaPeriodo().getMatriculaVO().getMatricula());
            removerControleMemoriaTela("ExpedicaoDiplomaControle");
            setPopUp("abrirPopupMaximizado2('expedicaoDiplomaForm.xhtml','expedicaoDiploma', 950, 595);");
        } catch (Exception e) {
            setPopUp("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
	}
	
	public void consultarDadosMapa() {
		try {
			getMapaAlunoAptoFormarVOs().clear();
			if (getTipoFiltro().equals("AL")) {
				if (getMatricula().getMatricula().equals("")) {
					throw new Exception ("Selecione o Aluno para realizar a consulta!");
				}
			}
			if (getTipoFiltro().equals("CU")) {
				if (getCursoVO().getCodigo().intValue() == 0) {
					throw new Exception ("Selecione o Curso para realizar a consulta!");
				}
			}
			if (getTipoFiltro().equals("TU")) {
				if (getTurmaVO().getCodigo().intValue() == 0) {
					throw new Exception ("Selecione a Turma para realizar a consulta!");
				}
			}
			setMapaAlunoAptoFormarVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMapaAlunoAptoFormar(getMatricula().getMatricula(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(),getSituacaoCurricular()));
			
			
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void geraRelatorioExcel() {
		try {			
			File arquivo = getFacadeFactory().getMapaAlunoAptoFormarFacade().realizarGeracaoExcel(getMapaAlunoAptoFormarVOs(), getLogoPadraoRelatorio(), getUsuarioLogado());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void limparLocalAula(){
		MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurma");
		mapaLocalAulaTurmaVO.getTurmaDisciplina().setSalaLocalAula(null);
	}

	public void consultarTurma() {
		try {

			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			inicializarDadosTurma();
			limparAluno();
			limparCurso();
			limparMensagem();
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void inicializarDadosTurma() throws Exception {
//		List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//		getListaSelectItemDisciplina().clear();
//		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
//		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
//			getListaSelectItemDisciplina().add(new SelectItem(turmaDisciplinaVO.getDisciplina().getCodigo(), turmaDisciplinaVO.getDisciplina().getNome()));
//		}
	}

	public void consultarTurmaPorIdentificador() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			inicializarDadosTurma();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemTipoFiltro() {
		List lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem("AL", "Aluno"));
		lista.add(new SelectItem("TU", "Turma"));
		lista.add(new SelectItem("CU", "Curso"));
		return lista;
	}

	public void limparTurma() {
		setTurmaVO(null);
		getMapaAlunoAptoFormarVOs().clear();
	}
	
	public void limparAluno() {
		setMatricula(null);
		getMapaAlunoAptoFormarVOs().clear();
	}

	public void limparCurso() {
		setCursoVO(null);
		getMapaAlunoAptoFormarVOs().clear();
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void alterarUnidadeEnsino() {
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		limparCurso();
		limparTurma();
		getMapaAlunoAptoFormarVOs().clear();
		
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoLogado().getCodigo(), false,Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO unidadeEnsinoCurso = (CursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setCursoVO(unidadeEnsinoCurso);
			limparTurma();
			limparMatriculaAluno();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public Boolean getPermiteModificarDados() {
		if (permiteModificarDados == null) {
			permiteModificarDados = Boolean.FALSE;
		}
		return permiteModificarDados;
	}
	public void setPermiteModificarDados(Boolean permiteModificarDados) {
		this.permiteModificarDados = permiteModificarDados;
	}

	public List<MapaAlunoAptoFormarVO> getMapaAlunoAptoFormarVOs() {
		if (mapaAlunoAptoFormarVOs == null) {
			mapaAlunoAptoFormarVOs = new ArrayList<MapaAlunoAptoFormarVO>();
		}
		return mapaAlunoAptoFormarVOs;
	}

	public void setMapaAlunoAptoFormarVOs(List<MapaAlunoAptoFormarVO> mapaAlunoAptoFormarVOs) {
		this.mapaAlunoAptoFormarVOs = mapaAlunoAptoFormarVOs;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public String getTipoFiltro() {
		if (tipoFiltro == null) {
			tipoFiltro = "AL";
		}
		return tipoFiltro;
	}

	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}


    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        return itens;
    }

	public String getPopUp() {
		if (popUp == null) {
			popUp = "";
		}
		return popUp;
	}

	public void setPopUp(String popUp) {
		this.popUp = popUp;
	}
	
	public boolean isNavegarParaMinhasNotasControle() {
		return navegarParaMinhasNotasControle;
	}

	public void setNavegarParaMinhasNotasControle(boolean navegarParaMinhasNotasControle) {
		this.navegarParaMinhasNotasControle = navegarParaMinhasNotasControle;
	}
	
	public void navegacaoParaMinhasNotasControle() {
		try {
			setNavegarParaMinhasNotasControle(true);
			MapaAlunoAptoFormarVO obj = (MapaAlunoAptoFormarVO) context().getExternalContext().getRequestMap().get("mapaAlunoAptoFormarItens");
			context().getExternalContext().getSessionMap().put("matricula",obj.getMatriculaPeriodo().getMatriculaVO().getMatricula());
			context().getExternalContext().getSessionMap().put("unidadeEnsino",obj.getMatriculaPeriodo().getMatriculaVO().getUnidadeEnsino().getCodigo());
			context().getExternalContext().getSessionMap().put("realizarMontagemPeriodoLetivo",true);
			removerControleMemoriaFlash("MinhasNotasControle");
			removerControleMemoriaTela("MinhasNotasControle");
		} catch (Exception e) {
			setNavegarParaMinhasNotasControle(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		 
	}
	
	public String getRealizarNavegacaoParaMinhasNotasControle() {
		if(isNavegarParaMinhasNotasControle()) {				
			return "popup('../academico/minhasNotasAdministrativo.xhtml', 'minhasNotasAdministrativo' , 1024, 800)";	
		}
		return "";
	}
	
	public boolean isPermiteVisualizarTelaMinhasNotasAdministrativo() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MINHAS_NOTAS_ADMINSITRATIVO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	public String getSituacaoCurricular() {
		if (situacaoCurricular == null) {
			situacaoCurricular = "IN";
		}
		return situacaoCurricular;
	}

	public void setSituacaoCurricular(String situacaoCurricular) {
		this.situacaoCurricular = situacaoCurricular;
	}

	public List<SelectItem> getListaSelectItemSituacaoCurricular() {
		List lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem("IN", "Integralizado"));
		lista.add(new SelectItem("NI", "Não Integralizado"));
		lista.add(new SelectItem("AB", "Ambos"));
		return lista;
	}

}
