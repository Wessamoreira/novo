package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MapaNotasDisciplinaAlunosRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.MapaNotasDisciplinaAlunosRel;

@Controller("MapaNotasDisciplinaAlunosRelControle")
@Scope("viewScope")
@Lazy
public class MapaNotasDisciplinaAlunosRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDisciplina;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public MapaNotasDisciplinaAlunosRelControle() throws Exception {
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparListasConsultas() {
		setTurma(null);
		setDisciplina(null);
		setUnidadeEnsinoCurso(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaSelectItemDisciplina().clear();
	}

	private void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), MapaNotasDisciplinaAlunosRelControle.class.getSimpleName(), getUsuarioLogado());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	public void imprimirPDF() {
		List<MapaNotasDisciplinaAlunosRelVO> mapaNotasDisciplinaAlunosRelVOs = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaRelControle", "Inicializando Geração de Relatório Mapa de Notas Alunos Por Turma", "Emitindo Relatório");
			MapaNotasDisciplinaAlunosRel.validarDados(getUnidadeEnsino(), getUnidadeEnsinoCurso(), getTurma(), getDisciplina(), getAno(), getSemestre());
			mapaNotasDisciplinaAlunosRelVOs = getFacadeFactory().getMapaNotasDisciplinaAlunosRelFacade().criarObjeto(getFiltroRelatorioAcademicoVO(), getUnidadeEnsino(), getUnidadeEnsinoCurso().getCurso(), getTurma(), getDisciplina(), getAno(), getSemestre(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
			if (!mapaNotasDisciplinaAlunosRelVOs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(MapaNotasDisciplinaAlunosRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosNaoRenovaramRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				getSuperParametroRelVO().setCurso(getUnidadeEnsinoCurso().getCurso().getNome());
				getSuperParametroRelVO().setTurma(getTurma().getIdentificadorTurma());
				if (!getDisciplina().getCodigo().equals(0) && getDisciplina().getNome().equals("")) {
					setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}
				getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());
				if(getUnidadeEnsinoCurso().getCurso().getPeriodicidade().equals("IN")) {
					getSuperParametroRelVO().adicionarParametro("dataInicioModulo", mapaNotasDisciplinaAlunosRelVOs.get(0).getDataInicioModulo());
					getSuperParametroRelVO().adicionarParametro("mostrarDataInicial", true);
				}else {
					getSuperParametroRelVO().adicionarParametro("mostrarDataInicial", false);
				}
				
				
				getSuperParametroRelVO().setTituloRelatorio("Mapa de Notas Alunos Disciplina");
				getSuperParametroRelVO().setListaObjetos(mapaNotasDisciplinaAlunosRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosNaoRenovaramRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(mapaNotasDisciplinaAlunosRelVOs.size());
				if (!getUnidadeEnsino().getCodigo().equals(0)) {
					setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
				}
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				realizarImpressaoRelatorio();
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), MapaNotasDisciplinaAlunosRelControle.class.getSimpleName(), getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaRelControle", "Finalizando Geração de Relatório Mapa de Notas Alunos Por Turma", "Emitindo Relatório");
				setMensagemID("msg_relatorio_ok");
				removerObjetoMemoria(this);
				incializarDados();
			} else {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(mapaNotasDisciplinaAlunosRelVOs);			
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

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemDisciplina() throws Exception {
		try {
			setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getListaSelectItemDisciplina().clear();
			List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurma().getCodigo(), true, true, 0);
			getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina() + " - CH: " + obj.getChDisciplina()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		}
	}

	@SuppressWarnings("rawtypes")
	public void consultarTurma() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			super.consultar();
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurma(), getTurma().getIdentificadorTurma(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurma().getCodigo() == 0) {
				setTurma(null);
			}
			montarListaSelectItemDisciplina();
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
				setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurma().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), getTurma().getTurno().getCodigo(), getUsuarioLogado()));
			}
			montarListaSelectItemDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setDisciplina(null);
			setTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarCurso() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens"));
			limparTurma();
			getListaConsultaTurma().clear();
			getListaSelectItemDisciplina().clear();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso(null);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
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
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
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
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
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

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
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
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
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

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public boolean getIsApresentarCampoAno() {
		if (getUnidadeEnsinoCurso().getCurso().getPeriodicidade().equals("AN") || getUnidadeEnsinoCurso().getCurso().getPeriodicidade().equals("SE") || getTurma().getCurso().getPeriodicidade().equals("SE")) {
			setAno(Uteis.getAnoDataAtual4Digitos());
			setSemestre("");
			return true;
		}
		setAno("");
		return false;
	}

	public boolean getIsApresentarCampoSemestre() {
		if (getUnidadeEnsinoCurso().getCurso().getPeriodicidade().equals("SE")) {
			setSemestre(Uteis.getSemestreAtual());
			return true;
		}
		setSemestre("");
		return false;
	}

}
