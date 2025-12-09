package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.CronogramaDeAulasRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.CronogramaDeAulasRel;

@Controller("CronogramaDeAulasRelControle")
@Scope("viewScope")
@Lazy
public class CronogramaDeAulasRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsino;
	private CursoVO curso;
	private DisciplinaVO disciplina;
	private TurmaVO turma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private Date dataInicio;
	private Date dataFim;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String tipoLayout;
	private String ordenacao;
	private List<SelectItem> listaSelectItemTipoLayout;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private String ano;
	private String semestre;
	private FuncionarioVO funcionarioVO;
    private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List<FuncionarioVO> listaConsultaProfessor;
    private List<SelectItem> tipoConsultaComboProfessor;
    private TurnoVO turno;
    private List<SelectItem> listaSelectItemTurno;
	private SalaLocalAulaVO salaLocalAula;
    private String campoConsultaSalaLocalAula;
    private String valorConsultaSalaLocalAula;
    private List<SalaLocalAulaVO> listaConsultaSalaLocalAula;
    private List<SelectItem> tipoConsultaComboSalaLocalAula;
	private LocalAulaVO localAula;
    private String campoConsultaLocalAula;
    private String valorConsultaLocalAula;
    private List<LocalAulaVO> listaConsultaLocalAula;
    private List<SelectItem> tipoConsultaComboLocalAula;
    private String tipoTurma;
    private List<SelectItem> listaSelectItemTipoTurma;
    private List<SelectItem> listaSelectItemPeriodicidade;
    private PeriodicidadeEnum periodicidade;
    private List<CronogramaDeAulasRelVO> listaCronogramaDeAulasProgramacaoTutoriaOnline;

	public CronogramaDeAulasRelControle() throws Exception {
		setModuloLayoutEtiquetaEnum(ModuloLayoutEtiquetaEnum.CRONOGRAMA_AULA);
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparListasConsultas() {
		removerObjetoMemoria(getDisciplina());
		removerObjetoMemoria(getCurso());
		removerObjetoMemoria(getTurma());
		if(Uteis.isAtributoPreenchido(getLocalAula()) && !getLocalAula().getUnidadeEnsino().getCodigo().equals(getUnidadeEnsino().getCodigo())){
			limparLocalAula();
		}
		if(Uteis.isAtributoPreenchido(getSalaLocalAula()) && !getSalaLocalAula().getLocalAula().getUnidadeEnsino().getCodigo().equals(getUnidadeEnsino().getCodigo())){
			limparSalaLocalAula();
		}
		getListaConsultaCurso().clear();
		getListaConsultaDisciplina().clear();
	}

	private void incializarDados() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTurno();
		verificarLayoutPadrao();
	}

	public void definirRelatorio() {
		try {
			imprimirPDF(false);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void imprimirPDFVisaoAluno()  {
		VisaoAlunoControle visaoAlunoControle = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CronogramaDeAulasRelControle", "Inicializando Geração de Relatório Cronograma de Aulas", "Emitindo Relatório");
			visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get(VisaoAlunoControle.class.getSimpleName());
			if (visaoAlunoControle != null) {
				MatriculaPeriodoVO matriculaPeriodo = null;
				matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatriculaSemExcecao(visaoAlunoControle.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				setTurma(matriculaPeriodo.getTurma());
				setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(visaoAlunoControle.getMatricula().getCurso().getPeriodicidade()));
				setAno(matriculaPeriodo.getAno());
				setSemestre(matriculaPeriodo.getSemestre());
				setDataInicio(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(getTurma().getCodigo(), matriculaPeriodo.getAno(), matriculaPeriodo.getSemestre()));
				setDataFim(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(getTurma().getCodigo(), matriculaPeriodo.getAno(), matriculaPeriodo.getSemestre()));
				setUnidadeEnsino(visaoAlunoControle.getMatricula().getUnidadeEnsino());
				setCurso(visaoAlunoControle.getMatricula().getCurso());
				if (visaoAlunoControle.getMatricula().getCurso().getNivelEducacional().equals(TipoNivelEducacional.POS_GRADUACAO.getValor())) {
					setTipoLayout("periodoAula");
				} else {
					setTipoLayout("dataAula");
				}
				imprimirPDF(true);
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "CronogramaDeAulasRelControle", "Finalizando Geração de Relatório Cronograma de Aulas", "Emitindo Relatório");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void imprimirPDF(boolean visaoAluno) {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				persistirLayoutPadrao();			
			}		
			if(getApresentarLayoutEtiqueta()){
				setFazerDownload(false);
				setCaminhoRelatorio(getFacadeFactory().getCronogramaDeAulasRelFacade().realizarGeracaoImpressaoEtiquetaPorCronogramaAula(getLayoutEtiquetaVO(), getUnidadeEnsino().getCodigo(), getTurma().getCodigo(), getCurso().getCodigo(), getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFuncionarioVO(), getTurno(), getSalaLocalAula(), getLocalAula(), getOrdenacao(), getUsuarioLogado(), getNumeroCopias(), getLinha(), getColuna(), getRemoverEspacoTAGVazia(), getConfiguracaoGeralPadraoSistema(), getPeriodicidade()));
				setFazerDownload(true);
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			}else{
				if(getTipoLayout().equals("dataAula") || getTipoLayout().equals("dataAulaHorario") || getTipoLayout().equals("horarioPorSala") || getTipoLayout().equals("frequenciaTurmaDisciplinaDataHoraProfessor")) {
					setListaCronogramaDeAulasProgramacaoTutoriaOnline(getFacadeFactory().getCronogramaDeAulasRelFacade().criarObjetoProgramacaoTutoriaOnline(getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), getTurma().getCodigo(),
						getTipoTurma(), getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFuncionarioVO(), getTurno(), getSalaLocalAula(),
						getLocalAula(), getTipoLayout(), visaoAluno, getUsuarioLogado(), getOrdenacao(), getPeriodicidade()));
				}
				listaObjetos = getFacadeFactory().getCronogramaDeAulasRelFacade().criarObjeto(getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), getTurma().getCodigo(),
						getTipoTurma(), getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFuncionarioVO(), getTurno(), getSalaLocalAula(),
						getLocalAula(), getTipoLayout(), visaoAluno, getUsuarioLogado(), getOrdenacao(), getPeriodicidade());			
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(CronogramaDeAulasRel.getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(CronogramaDeAulasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Cronograma de Aulas");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(CronogramaDeAulasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
				getSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
				getSuperParametroRelVO().setPeriodo(periodo_Apresentar());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (getUnidadeEnsino().getCodigo().intValue() > 0) {
					for (SelectItem item : getListaSelectItemUnidadeEnsino()) {
						if (item.getValue().equals(getUnidadeEnsino().getCodigo())) {
							getSuperParametroRelVO().setUnidadeEnsino(item.getLabel());
							break;
						}
					}
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("");
				}
				getSuperParametroRelVO().setCurso(getCurso().getNome());
				getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				if (getTurma().getCodigo() == 0) {
					if (getTipoTurma().equals("NORMAL")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO NORMAL");
					} else if (getTipoTurma().equals("AGRUPADA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO AGRUPADA");
					} else if (getTipoTurma().equals("TEORICA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA TEÓRICA");
					} else if (getTipoTurma().equals("PRATICA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA PRÁTICA");
					} else if (getTipoTurma().equals("GERAL")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA GERAL");
					} else {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "TODAS");
					}
				}
				getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
				getSuperParametroRelVO().setProfessor(getFuncionarioVO().getPessoa().getNome());
				for (SelectItem item : getListaSelectItemTurno()) {
					if (Integer.parseInt(item.getValue().toString()) == getTurno().getCodigo().intValue()) {
						getTurno().setNome(item.getLabel());
						break;
					}
				}
				getSuperParametroRelVO().setTurno(getTurno().getNome());
				getSuperParametroRelVO().adicionarParametro("sala", getSalaLocalAula().getSala());
				getSuperParametroRelVO().adicionarParametro("local", getLocalAula().getLocal());
				if (!getUnidadeEnsino().getCodigo().equals(0)) {
					setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
				}
				getSuperParametroRelVO().adicionarParametro("listaCronogramaDeAulasProgramacaoTutoriaOnline", getListaCronogramaDeAulasProgramacaoTutoriaOnline());
				realizarImpressaoRelatorio();				
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void imprimirExcel() {
		List<CronogramaDeAulasRelVO> listaObjetos = new ArrayList<CronogramaDeAulasRelVO>(0);
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()){
				persistirLayoutPadrao();
			}
			
			if(getTipoLayout().equals("dataAula") || getTipoLayout().equals("dataAulaHorario") || getTipoLayout().equals("horarioPorSala") || getTipoLayout().equals("frequenciaTurmaDisciplinaDataHoraProfessor")) {
				setListaCronogramaDeAulasProgramacaoTutoriaOnline(getFacadeFactory().getCronogramaDeAulasRelFacade().criarObjetoProgramacaoTutoriaOnline(getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), getTurma().getCodigo(),
					getTipoTurma(), getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFuncionarioVO(), getTurno(), getSalaLocalAula(),
					getLocalAula(), getTipoLayout(), false, getUsuarioLogado(), getOrdenacao(), getPeriodicidade()));
			}
			listaObjetos = getFacadeFactory().getCronogramaDeAulasRelFacade().criarObjeto(getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), getTurma().getCodigo(), getTipoTurma(), getDisciplina().getCodigo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getFuncionarioVO(), getTurno(), getSalaLocalAula(), getLocalAula(), getTipoLayout(), false, getUsuarioLogado(), getOrdenacao(), getPeriodicidade());
			if (!listaObjetos.isEmpty()) {
				if (getTipoLayout().equals("dataAulaHorario")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioRel.jrxml");
				} else if (getTipoLayout().equals("horarioPorSala")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioPorSalaRel.jrxml");
				} else if (getTipoLayout().equals("horarioPorCurso")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasHorarioPorCursoRel.jrxml");
				} else if (getTipoLayout().equals("frequenciaProfessor")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaProfessorExcelRel.jrxml");
				} else if (getTipoLayout().equals("frequenciaTurmaDisciplinaProfessor")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaTurmaDisciplinaProfessorRel.jrxml");
				} else if (getTipoLayout().equals("frequenciaTurmaDisciplinaDataHoraProfessor")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasFrequenciaTurmaDisciplinaDataHoraProfessorRel.jrxml");
				} else if (getTipoLayout().equals("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel")) {
					getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CronogramaDeAulasComProfessorCoordenadorEstatisticaMatriculaExcelRel.jrxml");
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(CronogramaDeAulasRel.getDesignIReportRelatorioExcel());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(CronogramaDeAulasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Cronograma de Aulas");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(CronogramaDeAulasRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
				getSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
				getSuperParametroRelVO().setPeriodo(periodo_Apresentar());
				if (getUnidadeEnsino().getCodigo().intValue() > 0) {
					for (SelectItem item : getListaSelectItemUnidadeEnsino()) {
						if (item.getValue().equals(getUnidadeEnsino().getCodigo())) {
							getSuperParametroRelVO().setUnidadeEnsino(item.getLabel());
							break;
						}
					}
				} else {
					getSuperParametroRelVO().setUnidadeEnsino("");
				}
				getSuperParametroRelVO().setCurso(getCurso().getNome());
				getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				if (getTurma().getCodigo() == 0) {
					if (getTipoTurma().equals("NORMAL")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO NORMAL");
					} else if (getTipoTurma().equals("AGRUPADA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO AGRUPADA");
					} else if (getTipoTurma().equals("TEORICA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA TEÓRICA");
					} else if (getTipoTurma().equals("PRATICA")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA PRÁTICA");
					} else if (getTipoTurma().equals("GERAL")) {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "APENAS DO TIPO SUBTURMA GERAL");
					} else {
						getSuperParametroRelVO().adicionarParametro("tipoTurma", "TODAS");
					}
				}
				getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
				getSuperParametroRelVO().setProfessor(getFuncionarioVO().getPessoa().getNome());
				for (SelectItem item : getListaSelectItemTurno()) {
					if (Integer.parseInt(item.getValue().toString()) == getTurno().getCodigo().intValue()) {
						getTurno().setNome(item.getLabel());
						break;
					}
				}
				getSuperParametroRelVO().setTurno(getTurno().getNome());
				getSuperParametroRelVO().adicionarParametro("sala", getSalaLocalAula().getSala());
				getSuperParametroRelVO().adicionarParametro("local", getLocalAula().getLocal());
				getSuperParametroRelVO().adicionarParametro("listaCronogramaDeAulasProgramacaoTutoriaOnline", getListaCronogramaDeAulasProgramacaoTutoriaOnline());
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}			
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	private List<SelectItem> listaSelectItemSemestre;

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<UnidadeEnsinoVO> resultadoConsulta = new ArrayList<UnidadeEnsinoVO>();
		try {
			if (isPermiteVisualizarTodasAsUnidades() || getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else {
				resultadoConsulta.add(getUnidadeEnsinoLogadoClone());
				//getUnidadeEnsinoLogado()
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}	

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			setDisciplina(obj);
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplina(null);
		} catch (Exception e) {
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		if(!obj.getTurmaAgrupada()){
			setCurso(obj.getCurso());
		}
		setCurso(obj.getCurso());
		setUnidadeEnsino(obj.getUnidadeEnsino());
		setTurma(obj);
		setPeriodicidade(obj.getAnual()?PeriodicidadeEnum.ANUAL:obj.getSemestral()?PeriodicidadeEnum.SEMESTRAL:PeriodicidadeEnum.INTEGRAL);
		removerObjetoMemoria(getDisciplina());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public List<SelectItem> getTipoOrdenacaoCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino, Data, Turma e Disciplina"));
		itens.add(new SelectItem("data", "Data, Unidade de Ensino, Turma e Disciplina"));
		itens.add(new SelectItem("turma", "Turma, Data e Disciplina"));
		itens.add(new SelectItem("disciplina", "Disciplina, Data, Unidade de Ensino e Turma"));
		return itens;
	}

	public void consultarCurso() {
		try {
			// if (getUnidadeEnsino().getCodigo() == 0) {
			// throw new Exception("Informe a Unidade de Ensino.");
			// }
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCurso(obj);
			setDisciplina(null);
			setTurma(null);
			setListaConsultaDisciplina(null);
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(obj.getPeriodicidade()));
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCurso(null);
			limparTurma();
		} catch (Exception e) {
		}
	}

	public void limparTurma() throws Exception {
		try {
			removerObjetoMemoria(getTurma());
			limparDisciplina();
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
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

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
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

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public Date getDataInicio() {
		if (dataInicio == null) {
//			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public String periodo_Apresentar() {
		return Uteis.getData(getDataInicio()) + " à " + Uteis.getData(getDataFim());
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			
//			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
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

	public String getOrdenacao() {
		if (ordenacao == null) {
			ordenacao = "";
		}
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	private void persistirLayoutPadrao() {		
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getTipoLayout(), "cronogramaAula", "tipoLayout", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getOrdenacao(), "cronogramaAula", "ordenacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getPeriodicidade().name(), "cronogramaAula", "periodicidade", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(Uteis.getData(getDataInicio(), "dd/MM/yyyy"), "cronogramaAula", "dataInicio", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(Uteis.getData(getDataFim(), "dd/MM/yyyy"), "cronogramaAula", "dataFim", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getAno(), "cronogramaAula", "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getSemestre(), "cronogramaAula", "semestre", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@PostConstruct
	private void verificarLayoutPadrao() {
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			LayoutPadraoVO layoutPadraoVO;
			try {
				layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "tipoLayout", false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
					setTipoLayout(layoutPadraoVO.getValor());
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "ordenacao", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setOrdenacao(layoutPadraoVO.getValor());
					}
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "periodicidade", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setPeriodicidade(PeriodicidadeEnum.valueOf(layoutPadraoVO.getValor()));
					}
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "dataInicio", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setDataInicio(Uteis.getData(layoutPadraoVO.getValor(), "dd/MM/yyyy"));
					}
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "dataFim", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setDataFim(Uteis.getData(layoutPadraoVO.getValor(), "dd/MM/yyyy"));
					}
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "ano", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setAno(layoutPadraoVO.getValor());
					}
					layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("cronogramaAula", "semestre", false, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(layoutPadraoVO.getValor())) {
						setSemestre(layoutPadraoVO.getValor());
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				layoutPadraoVO = null;
			}
		}
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("periodoAula", "Layout 1 - Período Aula"));
			listaSelectItemTipoLayout.add(new SelectItem("dataAula", "Layout 2 - Data Aula"));
			listaSelectItemTipoLayout.add(new SelectItem("dataAulaHorario", "Layout 3 - Data Aula c/ Horário"));
			listaSelectItemTipoLayout.add(new SelectItem("horarioPorSala", "Layout 4 - Horário por Sala"));
			listaSelectItemTipoLayout.add(new SelectItem("horarioPorCurso", "Layout 5 - Horário por Curso"));
			listaSelectItemTipoLayout.add(new SelectItem("frequenciaProfessor", "Layout 6 - Listagem Frequência Professor"));
			listaSelectItemTipoLayout.add(new SelectItem("frequenciaTurmaDisciplinaProfessor", "Layout 7 - Lista Presença Professor por Turma e Disciplina"));
			listaSelectItemTipoLayout.add(new SelectItem("frequenciaTurmaDisciplinaDataHoraProfessor", "Layout 8 - Lista Presença Professor por Data Aula"));
			listaSelectItemTipoLayout.add(new SelectItem("LayoutImpressaoEtiqueta", "Layout 9 - Impressão de Etiqueta"));
			listaSelectItemTipoLayout.add(new SelectItem("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel", "Layout 10 - Horário Aula c/ Professor, Coordenador e Estatística Matrícula"));
		}
		return listaSelectItemTipoLayout;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }
    
	public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
        }
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}
	
    public void limparConsultaProfessor() {
        setListaConsultaProfessor(null);
        setMensagemID("msg_entre_prmconsulta");
    }
    
    public List<SelectItem> getTipoConsultaComboProfessor() {
        if (tipoConsultaComboProfessor == null) {
            tipoConsultaComboProfessor = new ArrayList<SelectItem>();
            tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
        }
        return tipoConsultaComboProfessor;
    }
    
    public void consultarProfessor() {
        try {
            List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), "PR", null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProfessor(new ArrayList<FuncionarioVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarProfessor() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professor");
        getFacadeFactory().getFuncionarioFacade().carregarDados(obj, getUsuarioLogado());
        setFuncionarioVO(obj);
        setValorConsultaProfessor("");
        setCampoConsultaProfessor("");
        getListaConsultaProfessor().clear();
    }
    
    public void limparProfessor() throws Exception {
        try {
        	setFuncionarioVO(null);
//            setPessoaVO(null);
        } catch (Exception e) {
        }
    }
    
    
    public void acrescentarAnos() {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(getDataInicio());
    	calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+2);
    	setDataFim(calendar.getTime());
    }

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		if (turno == null) {
			turno = new TurnoVO();
		}
		this.turno = turno;
	}
	
	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}
	
	public void montarListaSelectItemTurno() throws Exception {
		List<TurnoVO> turnoVOs = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursoUnidadeEnsino(getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(turnoVOs, "codigo", "nome", true));
	}

	public SalaLocalAulaVO getSalaLocalAula() {
		if (salaLocalAula == null) {
			salaLocalAula = new SalaLocalAulaVO();
		}
		return salaLocalAula;
	}

	public void setSalaLocalAula(SalaLocalAulaVO salaLocalAula) {
		this.salaLocalAula = salaLocalAula;
	}

	public String getCampoConsultaSalaLocalAula() {
		if (campoConsultaSalaLocalAula == null) {
			campoConsultaSalaLocalAula = "";
		}
		return campoConsultaSalaLocalAula;
	}

	public void setCampoConsultaSalaLocalAula(String campoConsultaSalaLocalAula) {
		this.campoConsultaSalaLocalAula = campoConsultaSalaLocalAula;
	}

	public String getValorConsultaSalaLocalAula() {
		if (valorConsultaSalaLocalAula == null) {
			valorConsultaSalaLocalAula = "";
		}
		return valorConsultaSalaLocalAula;
	}

	public void setValorConsultaSalaLocalAula(String valorConsultaSalaLocalAula) {
		this.valorConsultaSalaLocalAula = valorConsultaSalaLocalAula;
	}

	public List<SalaLocalAulaVO> getListaConsultaSalaLocalAula() {
		if (listaConsultaSalaLocalAula == null) {
			listaConsultaSalaLocalAula = new ArrayList<SalaLocalAulaVO>(0);
		}
		return listaConsultaSalaLocalAula;
	}

	public void setListaConsultaSalaLocalAula(List<SalaLocalAulaVO> listaConsultaSalaLocalAula) {
		this.listaConsultaSalaLocalAula = listaConsultaSalaLocalAula;
	}

	public LocalAulaVO getLocalAula() {
		if (localAula == null) {
			localAula = new LocalAulaVO();
		}
		return localAula;
	}

	public void setLocalAula(LocalAulaVO localAula) {
		this.localAula = localAula;
	}

	public String getCampoConsultaLocalAula() {
		if (campoConsultaLocalAula == null) {
			campoConsultaLocalAula = "";
		}
		return campoConsultaLocalAula;
	}

	public void setCampoConsultaLocalAula(String campoConsultaLocalAula) {
		this.campoConsultaLocalAula = campoConsultaLocalAula;
	}

	public String getValorConsultaLocalAula() {
		if (valorConsultaLocalAula == null) {
			valorConsultaLocalAula = "";
		}
		return valorConsultaLocalAula;
	}

	public void setValorConsultaLocalAula(String valorConsultaLocalAula) {
		this.valorConsultaLocalAula = valorConsultaLocalAula;
	}

	public List<LocalAulaVO> getListaConsultaLocalAula() {
		if (listaConsultaLocalAula == null) {
			listaConsultaLocalAula = new ArrayList<LocalAulaVO>(0);
		}
		return listaConsultaLocalAula;
	}

	public void setListaConsultaLocalAula(List<LocalAulaVO> listaConsultaLocalAula) {
		this.listaConsultaLocalAula = listaConsultaLocalAula;
	}
	
    public List<SelectItem> getTipoConsultaComboSalaLocalAula() {
        if (tipoConsultaComboSalaLocalAula == null) {
        	tipoConsultaComboSalaLocalAula = new ArrayList<SelectItem>();
        	tipoConsultaComboSalaLocalAula.add(new SelectItem("sala", "Sala"));
        }
        return tipoConsultaComboSalaLocalAula;
    }
    
    public List<SelectItem> getTipoConsultaComboLocalAula() {
        if (tipoConsultaComboLocalAula == null) {
        	tipoConsultaComboLocalAula = new ArrayList<SelectItem>();
        	tipoConsultaComboLocalAula.add(new SelectItem("local", "Local"));
        }
        return tipoConsultaComboLocalAula;
    }
    
    public void limparConsultaSalaLocalAula() {
        setListaConsultaSalaLocalAula(null);
        setMensagemID("msg_entre_prmconsulta");
    }
    
    public void consultarSalaLocalAula() {
        try {
            List<SalaLocalAulaVO> objs = new ArrayList<SalaLocalAulaVO>(0);
            if (getCampoConsultaSalaLocalAula().equals("sala")) {
                objs = getFacadeFactory().getSalaLocalAulaFacade().consultarPorSala(getValorConsultaSalaLocalAula(), getLocalAula(), getUnidadeEnsino().getCodigo(), getUnidadeEnsinoVOs(), StatusAtivoInativoEnum.ATIVO);
            }
            setListaConsultaSalaLocalAula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaSalaLocalAula(new ArrayList<SalaLocalAulaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarSalaLocalAula() throws Exception {
        SalaLocalAulaVO obj = (SalaLocalAulaVO) context().getExternalContext().getRequestMap().get("sala");
        setSalaLocalAula(obj);
        setValorConsultaSalaLocalAula("");
        setCampoConsultaSalaLocalAula("");
        getListaConsultaSalaLocalAula().clear();
    }
    
    public void limparSalaLocalAula() {        
        	setSalaLocalAula(null);        
    }
    
    public void limparConsultaLocalAula() {
        setListaConsultaLocalAula(null);
        setMensagemID("msg_entre_prmconsulta");
    }
    
    public void consultarLocalAula() {
        try {
            List<LocalAulaVO> objs = new ArrayList<LocalAulaVO>(0);
            if (getCampoConsultaLocalAula().equals("local")) {
                objs = getFacadeFactory().getLocalAulaFacade().consultarPorLocal(getValorConsultaLocalAula(), getUnidadeEnsino().getCodigo(), StatusAtivoInativoEnum.ATIVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaLocalAula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaLocalAula(new ArrayList<LocalAulaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarLocalAula() throws Exception {
        LocalAulaVO obj = (LocalAulaVO) context().getExternalContext().getRequestMap().get("local");
        setLocalAula(obj);
        setValorConsultaLocalAula("");
        setCampoConsultaLocalAula("");
        getListaConsultaLocalAula().clear();
    }
    
    public void limparLocalAula(){        
        	setLocalAula(null);        
    }
    
    public boolean getApresentarLayoutEtiqueta(){
    	return getTipoLayout().equals("LayoutImpressaoEtiqueta");
    }
    
    public String getTipoTurma() {
		if (tipoTurma == null) {
			tipoTurma = "";
		}
		return tipoTurma;
	}

	public void setTipoTurma(String tipoTurma) {
		this.tipoTurma = tipoTurma;
	}

	public List<SelectItem> getListaSelectItemTipoTurma() {
		if (listaSelectItemTipoTurma == null) {
			listaSelectItemTipoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemTipoTurma.add(new SelectItem("", ""));
			listaSelectItemTipoTurma.add(new SelectItem("NORMAL", "Normal"));
			listaSelectItemTipoTurma.add(new SelectItem("PRATICA", "Subturma Prática"));
			listaSelectItemTipoTurma.add(new SelectItem("TEORICA", "Subturma Teórica"));
			listaSelectItemTipoTurma.add(new SelectItem("GERAL", "Subturma Geral"));
			listaSelectItemTipoTurma.add(new SelectItem("AGRUPADA", "Agrupada"));
		}
		return listaSelectItemTipoTurma;
	}

	public void setListaSelectItemTipoTurma(
			List<SelectItem> listaSelectItemTipoTurma) {
		this.listaSelectItemTipoTurma = listaSelectItemTipoTurma;
	}
	
	/**
	 * @return the listaSelectItemPeriodicidade
	 */
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL, PeriodicidadeEnum.SEMESTRAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL, PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.INTEGRAL, PeriodicidadeEnum.INTEGRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}

	/**
	 * @param listaSelectItemPeriodicidade the listaSelectItemPeriodicidade to set
	 */
	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	/**
	 * @return the periodicidade
	 */
	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	public boolean getApresentarAno(){
		return !getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL);
	}
	
	public boolean getApresentarSemestre(){
		return getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
	}
	

    /**
     * periodoAula
     * dataAulaHorario
     * dataAula
     */
	
	public boolean getApresentarOrdenacao(){
		return (getTipoLayout().equals("periodoAula") || getTipoLayout().equals("dataAulaHorario") || getTipoLayout().equals("periodoAula") || getTipoLayout().equals("dataAulaComProfessorCoordenadorEstatisticaMatriculaRel"));
	}

	public List<CronogramaDeAulasRelVO> getListaCronogramaDeAulasProgramacaoTutoriaOnline() {
		if (listaCronogramaDeAulasProgramacaoTutoriaOnline == null) {
			listaCronogramaDeAulasProgramacaoTutoriaOnline = new ArrayList<CronogramaDeAulasRelVO>(0);
		}
		return listaCronogramaDeAulasProgramacaoTutoriaOnline;
	}

	public void setListaCronogramaDeAulasProgramacaoTutoriaOnline(
			List<CronogramaDeAulasRelVO> listaCronogramaDeAulasProgramacaoTutoriaOnline) {
		this.listaCronogramaDeAulasProgramacaoTutoriaOnline = listaCronogramaDeAulasProgramacaoTutoriaOnline;
	}
	
	public boolean isPermiteVisualizarTodasAsUnidades() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITIR_EMISSAO_TODAS_UNIDADES_CRONOGRAMA_DE_AULAS_REL, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	

}
