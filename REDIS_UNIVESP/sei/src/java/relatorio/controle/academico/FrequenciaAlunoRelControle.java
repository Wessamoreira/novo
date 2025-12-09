package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.FrequenciaAlunoMesDiaRelVO;
import relatorio.negocio.comuns.academico.FrequenciaAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FrequenciaAlunoRel;

@Controller("FrequenciaAlunoRelControle")
@Scope("viewScope")
public class FrequenciaAlunoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private MatriculaVO matriculaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private Date dataInicio;
	private Date dataFim;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<DisciplinaVO> disciplinaVOs;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<MatriculaVO> matriculaVOs;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String tipoLayout;
	private boolean trazerAlunoTransferencia = false;

	private CursoVO curso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private TurmaVO turmaVO;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	private Integer quantidadeMinimaFaltas;
	private Integer percentualMinimaFaltas;
	private Boolean considerarFaltaAulasNaoRegistradas;

	public FrequenciaAlunoRelControle() {
		montarListaSelectItemUnidadeEnsino();
	}

	@PostConstruct
	public void verificarLayoutPadrao() throws Exception {
		setTipoLayout(getFacadeFactory().getLayoutPadraoFacade()
				.consultarPorEntidadeCampo("frequenciaAluno", "designRelatorio", false, getUsuarioLogado()).getValor());
		setConsiderarFaltaAulasNaoRegistradas(
				Boolean.valueOf(getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("frequenciaAluno",
						"considerarFaltaAulasNaoRegistradas", false, getUsuarioLogado()).getValor()));
	}

	public void imprimirPDF() {
		try {
			getFacadeFactory().getFrequenciaAlunoRelFacade().validarDados(getUnidadeEnsinoVO(), getMatriculaVO(),
					getAno(), getSemestre(), getCurso(), getTurmaVO());

			List<FrequenciaAlunoRelVO> frequenciaAlunoRelVOs = getFacadeFactory().getFrequenciaAlunoRelFacade()
					.executarCriacaoObjeto(getMatriculaVO(), getDisciplinaVO(), getAno(), getSemestre(),
							getDataInicio(), getDataFim(), isTrazerAlunoTransferencia(), getUsuarioLogado(), getCurso(),
							getTurmaVO(), getQuantidadeMinimaFaltas(), getPercentualMinimaFaltas(),
							getConsiderarFaltaAulasNaoRegistradas());

			filtrarPorPercentual(frequenciaAlunoRelVOs, percentualMinimaFaltas);
			filtrarPorQuantidadeFalta(frequenciaAlunoRelVOs, quantidadeMinimaFaltas);

			if (!frequenciaAlunoRelVOs.isEmpty()) {
				getSuperParametroRelVO()
						.setTituloRelatorio(UteisJSF.internacionalizar("prt_FrequenciaAlunoRel_tituloForm"));
				if (getTipoLayout().equals("1")) {
					getSuperParametroRelVO()
							.setNomeDesignIreport(FrequenciaAlunoRel.getDesignIReportRelatorioLayout1());
				} else if (getTipoLayout().equals("2")) {
					getSuperParametroRelVO()
							.setNomeDesignIreport(FrequenciaAlunoRel.getDesignIReportRelatorioLayout2());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(FrequenciaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(FrequenciaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(frequenciaAlunoRelVOs);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(
							getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
							getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				executarCriacaoParametrosFiltroRelatorio(frequenciaAlunoRelVOs);
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getTipoLayout());
				// removerObjetoMemoria(this);
				montarListaSelectItemUnidadeEnsino();
				verificarLayoutPadrao();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void filtrarPorPercentual(List<FrequenciaAlunoRelVO> frequenciaAlunoRelVOs,
			Integer percentualMinimaFaltas) {
		for (FrequenciaAlunoRelVO frequenciaAlunoRelVO : frequenciaAlunoRelVOs) {
			List<FrequenciaAlunoMesDiaRelVO> lista = frequenciaAlunoRelVO.getFrequenciaAlunoMesDiaRelVOs().stream()
					.filter(p -> p.getPorcentagemFaltas() >= percentualMinimaFaltas).collect(Collectors.toList());
			frequenciaAlunoRelVO.getFrequenciaAlunoMesDiaRelVOs().clear();
			frequenciaAlunoRelVO.setFrequenciaAlunoMesDiaRelVOs(lista);
		}
		frequenciaAlunoRelVOs.removeIf(p -> !Uteis.isAtributoPreenchido(p.getFrequenciaAlunoMesDiaRelVOs()));
	}

	private void filtrarPorQuantidadeFalta(List<FrequenciaAlunoRelVO> frequenciaAlunoRelVOs,
			Integer quantidadeMinimaFaltas) {
		for (FrequenciaAlunoRelVO frequenciaAlunoRelVO : frequenciaAlunoRelVOs) {
			List<FrequenciaAlunoMesDiaRelVO> lista = frequenciaAlunoRelVO.getFrequenciaAlunoMesDiaRelVOs().stream()
					.filter(p -> p.getFaltas() >= quantidadeMinimaFaltas).collect(Collectors.toList());
			frequenciaAlunoRelVO.getFrequenciaAlunoMesDiaRelVOs().clear();
			frequenciaAlunoRelVO.setFrequenciaAlunoMesDiaRelVOs(lista);
		}
		frequenciaAlunoRelVOs.removeIf(p -> !Uteis.isAtributoPreenchido(p.getFrequenciaAlunoMesDiaRelVOs()));
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "frequenciaAluno", "designRelatorio",
				getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(
				getConsiderarFaltaAulasNaoRegistradas().toString(), "frequenciaAluno",
				"considerarFaltaAulasNaoRegistradas", getUsuarioLogado());
	}

	public void consultarAluno() {
		try {
			if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_campoUnidadeEnsinoDeveSerInformado"));
			}
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				@SuppressWarnings("deprecation")
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
						getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), 0,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(),
						getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					getMatriculaVOs().add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(
						getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), 0, false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(
						getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), 0, false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			setCurso((CursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
			setMatriculaVOs(null);
			setCurso(getMatriculaVO().getCurso());
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarUnidadeEnsino() {
		try {
			limparDadosAluno();
			limparDadosCurso();
			limparDadosCursoTurma();
			limparDadosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() {
		setMatriculaVO(null);
		setDisciplinaVO(null);
		setAno(null);
		setSemestre(null);
	}

	public void limparDisciplinas() {
		setCampoConsultaDisciplina("");
		setValorConsultaDisciplina("");

		setDisciplinaVOs(new ArrayList<>());
	}

	public void limparDadosDisciplina() {
		limparDisciplinas();

		setDisciplinaVO(null);
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					getMatriculaVO().getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setCurso(objAluno.getCurso());

			setMatriculaVO(objAluno);
			setUnidadeEnsinoVO(getMatriculaVO().getUnidadeEnsino());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void selecionarDisciplina() throws Exception {
		setDisciplinaVO((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens"));
		setAno(getDisciplinaVO().getAno());
		setSemestre(getDisciplinaVO().getSemestre());
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		setDisciplinaVOs(null);
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);

			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {

				if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					objs = consultarCursoVisaoCoordenador(objs);
				} else {
					objs = consultarCursoVisaoAdministrativa(objs);
				}

				setListaConsultaCurso(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<CursoVO> consultarCursoVisaoCoordenador(List<CursoVO> objs) throws Exception {
		if (getCampoConsultaCurso().equals("codigo")) {
			if (getValorConsultaCurso().equals("")) {
				setValorConsultaCurso("0");
			}
			int valorInt = Integer.parseInt(getValorConsultaCurso());
			objs = getFacadeFactory().getCursoFacade().consultarPorCodigoCoordenador(valorInt,
					getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(),
					getUsuarioLogado().getPessoa().getCodigo());
		}
		if (getCampoConsultaCurso().equals("nome")) {
			objs = getFacadeFactory().getCursoFacade().consultarPorNomeCoordenador(getValorConsultaCurso(),
					getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(),
					getUsuarioLogado().getPessoa().getCodigo());
		}
		return objs;
	}

	@SuppressWarnings("unchecked")
	private List<CursoVO> consultarCursoVisaoAdministrativa(List<CursoVO> objs) throws Exception {
		if (getCampoConsultaCurso().equals("codigo")) {
			if (getValorConsultaCurso().equals("")) {
				setValorConsultaCurso("0");
			}
			int valorInt = Integer.parseInt(getValorConsultaCurso());
			objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoVO().getCodigo(),
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		if (getCampoConsultaCurso().equals("nome")) {
			objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(),
					getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		return objs;
	}

	public void limparDadosCurso() {
		setCurso(new CursoVO());
		setListaConsultaCurso(new ArrayList<>());

		setTurmaVO(new TurmaVO());
		setListaConsultaTurma(new ArrayList<>());
		setMensagemDetalhada("");
		setMensagemID("");

		limparDadosDisciplina();
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);

			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorTurmaCodigo(valorInt,
						getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorTurmaNome(getValorConsultaDisciplina(),
						getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); // objs
			}
			setDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setDisciplinaVOs(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoTurno(
						getValorConsultaTurma(), getCurso().getCodigo(), 0, getUnidadeEnsinoVO().getCodigo().intValue(),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(
						getValorConsultaTurma(), getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo().intValue(),
						false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosCursoTurma() throws Exception {
		setTurmaVO(new TurmaVO());

		getListaConsultaTurma().clear();
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void selecionarTurma() throws Exception {
		setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
		setCurso(getTurmaVO().getCurso());
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();

		limparDadosDisciplina();
	}

	private void executarCriacaoParametrosFiltroRelatorio(List<FrequenciaAlunoRelVO> frequenciaAlunoRelVOs)
			throws Exception {
		GradeCurricularVO gradeCurricularVO = new GradeCurricularVO();
		if (Uteis.isAtributoPreenchido(getMatriculaVO().getGradeCurricularAtual())) {
			gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(
					getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
		}
		getSuperParametroRelVO().setMatricula(getMatriculaVO().getMatricula());
		getSuperParametroRelVO().setAluno(getMatriculaVO().getAluno().getNome());
		getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
		getSuperParametroRelVO().setCurso(getCurso().getNome());
		getSuperParametroRelVO().setMatrizCurricular(gradeCurricularVO.getNome());
		getSuperParametroRelVO().setDataInicio(Uteis.getDataAplicandoFormatacao(getDataInicio(), "dd/MM/yyyy"));
		getSuperParametroRelVO().setDataFim(Uteis.getDataAplicandoFormatacao(getDataFim(), "dd/MM/yyyy"));
		getSuperParametroRelVO().setAno(getAno());
		getSuperParametroRelVO().setSemestre(getSemestre());
		int faltaGeral = 0;
		int presencaGeral = 0;
		for (FrequenciaAlunoRelVO obj : frequenciaAlunoRelVOs) {
			for (FrequenciaAlunoMesDiaRelVO diaRelVO : obj.getFrequenciaAlunoMesDiaRelVOs()) {
				faltaGeral += diaRelVO.getFaltas();
				presencaGeral += (diaRelVO.getPresencas() + diaRelVO.getAbonos());
			}
		}
		getSuperParametroRelVO().adicionarParametro("faltaGeral", faltaGeral);
		getSuperParametroRelVO().adicionarParametro("presencaGeral", presencaGeral);
		getSuperParametroRelVO().adicionarParametro("disciplina", getDisciplinaVO().getNome());
		getSuperParametroRelVO().adicionarParametro("turma", getTurmaVO().getIdentificadorTurma());
		getSuperParametroRelVO().adicionarParametro("quantidadeMinimaFaltas", getQuantidadeMinimaFaltas());
		getSuperParametroRelVO().adicionarParametro("percentualMinimaFaltas", getPercentualMinimaFaltas());
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("",
					super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public boolean getIsApresentarCampoAno() {
		if (Uteis.isAtributoPreenchido(getMatriculaVO())) {
			return getMatriculaVO().getCurso().getPeriodicidade().equals("AN")
					|| getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
		} else if (Uteis.isAtributoPreenchido(getTurmaVO())) {
			return getTurmaVO().getPeriodicidade().equals("AN") || getTurmaVO().getPeriodicidade().equals("SE");
		} else if (Uteis.isAtributoPreenchido(getCurso())) {
			return getCurso().getPeriodicidade().equals("AN") || getCurso().getPeriodicidade().equals("SE");
		} else {
			return false;
		}
	}

	public boolean getIsApresentarCampoSemestre() {
		if (Uteis.isAtributoPreenchido(getMatriculaVO())) {
			return getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
		} else if (Uteis.isAtributoPreenchido(getTurmaVO())) {
			return getTurmaVO().getPeriodicidade().equals("SE");
		} else if (Uteis.isAtributoPreenchido(getCurso())) {
			return getCurso().getPeriodicidade().equals("SE");
		} else {
			return false;
		}
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<DisciplinaVO> getDisciplinaVOs() {
		if (disciplinaVOs == null) {
			disciplinaVOs = new ArrayList<DisciplinaVO>(0);
		}
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
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

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "1";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List<SelectItem> getListaTipoLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(1, "Layout 1 - Presenças e Faltas"));
		itens.add(new SelectItem(2, "Layout 2 - Somente Faltas"));
		return itens;
	}

	public boolean isTrazerAlunoTransferencia() {
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public Integer getQuantidadeMinimaFaltas() {
		if (quantidadeMinimaFaltas == null) {
			quantidadeMinimaFaltas = 0;
		}
		return quantidadeMinimaFaltas;
	}

	public void setQuantidadeMinimaFaltas(Integer quantidadeMinimaFaltas) {
		this.quantidadeMinimaFaltas = quantidadeMinimaFaltas;
	}

	public Integer getPercentualMinimaFaltas() {
		if (percentualMinimaFaltas == null) {
			percentualMinimaFaltas = 0;
		}
		return percentualMinimaFaltas;
	}

	public void setPercentualMinimaFaltas(Integer percentualMinimaFaltas) {
		this.percentualMinimaFaltas = percentualMinimaFaltas;
	}

	public Boolean getConsiderarFaltaAulasNaoRegistradas() {
		if (considerarFaltaAulasNaoRegistradas == null) {
			considerarFaltaAulasNaoRegistradas = false;
		}
		return considerarFaltaAulasNaoRegistradas;
	}

	public void setConsiderarFaltaAulasNaoRegistradas(Boolean considerarFaltaAulasNaoRegistradas) {
		this.considerarFaltaAulasNaoRegistradas = considerarFaltaAulasNaoRegistradas;
	}
}
