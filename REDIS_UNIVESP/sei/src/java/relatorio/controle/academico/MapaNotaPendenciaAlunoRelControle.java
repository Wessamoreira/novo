package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.MapaNotaPendenciaAlunoRel;

/**
 * @author Wellington Rodrigues - 16 de jul de 2015
 *
 */
@Controller("MapaNotaPendenciaAlunoRelControle")
@Scope("viewScope")
public class MapaNotaPendenciaAlunoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<SelectItem> tipoConsultaComboCurso;
	private List<TurmaVO> turmaVOs;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<SelectItem> tipoConsultaComboTurma;
	private List<DisciplinaVO> disciplinaVOs;
	private DisciplinaVO disciplinaVO;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemDisciplina;
	private String periodicidade;
	private String ano;
	private String semestre;
	private List<SelectItem> tipoConsultaComboSituacaoAluno;
	private String situacaoAluno;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private Boolean marcarTodasNotaSegundaChamada;
	private List<SelectItem> listaSelectItemSituacaoNotaRecuperacao;
	private String situacaoNotaRecuperacao;
	private List<SelectItem> listaSelectItemTipoLayout;
	private String tipoLayout;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private String filtrarNota;
	private List<SelectItem> listaSelectItemFiltrarNota;
	private List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs;
	private Boolean apresentarOutrasNotas;
	private List<ConfiguracaoAcademicaNotaVO>configuracaoAcademicaNotaNaoRecuperacaoVOs;
	private Boolean marcarTodasNotaNaoRecuperacao;

	public MapaNotaPendenciaAlunoRelControle() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemConfiguracaoAcademico();
	}

	public void imprimirPDF() {
		try {
			getFacadeFactory().getMapaNotaPendenciaAlunoRelFacade().validarDados(getConfiguracaoAcademicaNotaVOs(), getAno(), getIsApresentarCampoAno(), getSemestre(), getIsApresentarCampoSemestre(), getSituacaoAluno());
			List<MapaNotaPendenciaAlunoRelVO> mapaNotaPendenciaAlunoRelVOs = getFacadeFactory().getMapaNotaPendenciaAlunoRelFacade().executarCriacaoObjeto(getUnidadeEnsinoVO().getCodigo(), getPeriodicidade(), getAno(), getSemestre(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO(), getDisciplinaVO().getCodigo(), getSituacaoAluno(), getConfiguracaoAcademicoVO().getCodigo(), getSituacaoNotaRecuperacao(), getTipoLayout(), getFiltrarNota(), getConfiguracaoAcademicaNotaVOs(), getFiltroRelatorioAcademicoVO(), getConfiguracaoAcademicaNotaNaoRecuperacaoVOs(), getUsuarioLogado());
			if (!mapaNotaPendenciaAlunoRelVOs.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("prt_MapaNotaPendenciaAluno_tituloForm"));
				getSuperParametroRelVO().setNomeDesignIreport(MapaNotaPendenciaAlunoRel.getDesignIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(MapaNotaPendenciaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(MapaNotaPendenciaAlunoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(mapaNotaPendenciaAlunoRelVOs);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				executarCriacaoParametrosFiltroRelatorio();
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				montarListaSelectItemUnidadeEnsino();
				montarListaSelectItemConfiguracaoAcademico();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void executarCriacaoParametrosFiltroRelatorio() throws Exception {
		getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
		getSuperParametroRelVO().adicionarParametro("periodicidade", PeriodicidadeEnum.getEnumPorValor(getPeriodicidade()).getDescricao());
		getSuperParametroRelVO().setAno(getAno());
		getSuperParametroRelVO().setSemestre(getSemestre());
		getSuperParametroRelVO().adicionarParametro("situacaoAluno", getSituacaoAluno().equals("recuperacao") ? "Em Recuperação" : "2ª Chamada");
		getSuperParametroRelVO().adicionarParametro("configuracaoAcademico", getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado()).getNome());
		getSuperParametroRelVO().adicionarParametro("filtrarNota", getFiltrarNota().equals("combinada") ? "Combinadas" : "Individuais");
		getSuperParametroRelVO().adicionarParametro("situacaoNotaRecuperacao", getSituacaoAluno().equals("recuperacao") ? SituacaoRecuperacaoNotaEnum.valueOf(getSituacaoNotaRecuperacao()).getValorApresentar() : "");
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoTransferenciaExterna", getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoTransferenciaInterna", getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoAbandonado", getFiltroRelatorioAcademicoVO().getAbandonado());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademicoVO().getFormado());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoMatriculaAReceber", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		getSuperParametroRelVO().adicionarParametro("filtroAcademicoMatriculaRecebida", getFiltroRelatorioAcademicoVO().getConfirmado());
		List<MapaNotaPendenciaAlunoTurmaRelVO> mapaNotaPendenciaAlunoTurmaRelVOs = getFacadeFactory().getMapaNotaPendenciaAlunoRelFacade().executarCriacaoMapaNotaPendenciaAlunoTurma(getUnidadeEnsinoVO().getCodigo(), getPeriodicidade(), getAno(), getSemestre(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO(), getDisciplinaVO().getCodigo(), getSituacaoAluno(), getConfiguracaoAcademicoVO().getCodigo(), getSituacaoNotaRecuperacao(), getFiltrarNota(), getConfiguracaoAcademicaNotaVOs(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado());
		getSuperParametroRelVO().adicionarParametro("mapaNotaPendenciaAlunoTurmaRelVOs", mapaNotaPendenciaAlunoTurmaRelVOs);
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				getUnidadeEnsinoVO().setCodigo(unidadeEnsinoVOs.get(0).getCodigo());
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCurso() {
		try {
			if (getCampoConsultaCurso().equals("nome")) {
				setUnidadeEnsinoCursoVOs(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoPeriodicidade(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens"));
			setPeriodicidade(getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade());
			setValorConsultaCurso(null);
			setUnidadeEnsinoCursoVOs(null);
			montarListaSelectItemConfiguracaoAcademico();
			limparTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCursoVO(null);
			limparTurma();
			setValorConsultaCurso(null);
			setUnidadeEnsinoCursoVOs(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			getUnidadeEnsinoCursoVO().setCurso(getTurmaVO().getCurso());
			setPeriodicidade(getTurmaVO().getAnual() ? PeriodicidadeEnum.ANUAL.getValor() : getTurmaVO().getSemestral() ? PeriodicidadeEnum.SEMESTRAL.getValor() : PeriodicidadeEnum.INTEGRAL.getValor());
			montarListaSelectItemDisciplinaTurma();
			montarListaSelectItemConfiguracaoAcademico();
			setValorConsultaTurma(null);
			setTurmaVOs(null);
			limparDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			setValorConsultaTurma(null);
			setTurmaVOs(null);
			setListaSelectItemDisciplina(null);
			limparDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

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
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(valorInt, getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		setDisciplinaVO((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens"));
		montarListaSelectItemConfiguracaoAcademico();
		setValorConsultaDisciplina(null);
		setDisciplinaVOs(null);
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
			setValorConsultaDisciplina(null);
			setDisciplinaVOs(null);
			montarListaSelectItemConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinaTurma() throws Exception {
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(disciplinaVOs, "codigo", "nome"));
	}

	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = consultarConfiguracaoAcademicoPorDisciplina();
			if (Uteis.isAtributoPreenchido(configuracaoAcademicoVOs)) {
				getConfiguracaoAcademicoVO().setCodigo(configuracaoAcademicoVOs.get(0).getCodigo());
			}
			setListaSelectItemConfiguracaoAcademico(UtilSelectItem.getListaSelectItem(configuracaoAcademicoVOs, "codigo", "nome", false));
			executarMontagemConfiguracaoAcademicoNotaVOs();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoPorDisciplina() throws Exception {
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorDisciplinaPorTurma(getDisciplinaVO().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configuracaoAcademicoVOs)) {
			return consultarConfiguracaoAcademicoPorTurma();
		}
		return configuracaoAcademicoVOs;
	}

	private List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoPorTurma() throws Exception {
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configuracaoAcademicoVOs)) {
			return consultarConfiguracaoAcademicoPorCurso();
		}
		return configuracaoAcademicoVOs;
	}

	private List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoPorCurso() throws Exception {
		List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCurso(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), false, getUsuarioLogado());
		if (!Uteis.isAtributoPreenchido(configuracaoAcademicoVOs)) {
			return consultarConfiguracaoAcademicoPorUnidadeEnsino();
		}
		return configuracaoAcademicoVOs;
	}

	private List<ConfiguracaoAcademicoVO> consultarConfiguracaoAcademicoPorUnidadeEnsino() throws Exception {
		return getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemNotaSegundaChamada() {
		try {
			executarMontagemConfiguracaoAcademicoNotaVOs();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void executarMontagemConfiguracaoAcademicoNotaVOs() throws Exception {
		if(Uteis.isAtributoPreenchido(getConfiguracaoAcademicoVO().getCodigo())) {
			setMarcarTodasNotaSegundaChamada(false);
			if (getIsApresentarDadosSegundaChamada()) {
				setConfiguracaoAcademicaNotaVOs(getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademico(getConfiguracaoAcademicoVO().getCodigo()));
			} else {
				setConfiguracaoAcademicaNotaVOs(getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademicoNotaRecuperacao(getConfiguracaoAcademicoVO().getCodigo(), true));
				if (!Uteis.isAtributoPreenchido(getConfiguracaoAcademicaNotaVOs())) {
					throw new Exception(UteisJSF.internacionalizar("msg_MapaNotaPendenciaAlunoRel_configuracaoAcademicoNaoPossuiNotaRecuperacao"));
				}
			}
		}
	}

	public void marcarTodasNotaSegundaChamadaAction() {
		try {
			for (ConfiguracaoAcademicaNotaVO canVO : getConfiguracaoAcademicaNotaVOs()) {
				if (getMarcarTodasNotaSegundaChamada()) {
					canVO.setUtilizarNotaSegundaChamada(true);
				} else {
					canVO.setUtilizarNotaSegundaChamada(false);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getColumn() {
		if (getConfiguracaoAcademicaNotaVOs().size() > 4) {
			return 4;
		}
		return getConfiguracaoAcademicaNotaVOs().size();
	}

	public Integer getElement() {
		return getConfiguracaoAcademicaNotaVOs().size();
	}

	public boolean getIsApresentarCampoAno() {
		return getPeriodicidade().equals("AN") || getPeriodicidade().equals("SE");
	}

	public boolean getIsApresentarCampoSemestre() {
		return getPeriodicidade().equals("SE");
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

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "AN";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
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

	public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
			turmaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public List<DisciplinaVO> getDisciplinaVOs() {
		if (disciplinaVOs == null) {
			disciplinaVOs = new ArrayList<DisciplinaVO>(0);
		}
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
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

	public List<SelectItem> getTipoConsultaComboSituacaoAluno() {
		if (tipoConsultaComboSituacaoAluno == null) {
			tipoConsultaComboSituacaoAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboSituacaoAluno.add(new SelectItem("recuperacao", "Em Recuperação"));
			tipoConsultaComboSituacaoAluno.add(new SelectItem("segundaChamada", "2ª Chamada"));
		}
		return tipoConsultaComboSituacaoAluno;
	}

	public String getSituacaoAluno() {
		if (situacaoAluno == null) {
			situacaoAluno = "";
		}
		return situacaoAluno;
	}

	public void setSituacaoAluno(String situacaoAluno) {
		this.situacaoAluno = situacaoAluno;
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

	public boolean getIsApresentarComboboxDisciplina() {
		return Uteis.isAtributoPreenchido(getTurmaVO());
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public Boolean getMarcarTodasNotaSegundaChamada() {
		if (marcarTodasNotaSegundaChamada == null) {
			marcarTodasNotaSegundaChamada = false;
		}
		return marcarTodasNotaSegundaChamada;
	}

	public void setMarcarTodasNotaSegundaChamada(Boolean marcarTodasNotaSegundaChamada) {
		this.marcarTodasNotaSegundaChamada = marcarTodasNotaSegundaChamada;
	}

	public List<SelectItem> getListaSelectItemSituacaoNotaRecuperacao() {
		if (listaSelectItemSituacaoNotaRecuperacao == null) {
			listaSelectItemSituacaoNotaRecuperacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoNotaRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.TODAS, SituacaoRecuperacaoNotaEnum.TODAS.getValorApresentar()));
			listaSelectItemSituacaoNotaRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA, SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA.getValorApresentar()));
			listaSelectItemSituacaoNotaRecuperacao.add(new SelectItem(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA, SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA.getValorApresentar()));
		}
		return listaSelectItemSituacaoNotaRecuperacao;
	}

	public String getSituacaoNotaRecuperacao() {
		if (situacaoNotaRecuperacao == null) {
			situacaoNotaRecuperacao = "";
		}
		return situacaoNotaRecuperacao;
	}

	public void setSituacaoNotaRecuperacao(String situacaoNotaRecuperacao) {
		this.situacaoNotaRecuperacao = situacaoNotaRecuperacao;
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("analiticoPaisagem", "Analítico Paisagem"));
			listaSelectItemTipoLayout.add(new SelectItem("analiticoRetrato", "Analítico Retrato"));
			listaSelectItemTipoLayout.add(new SelectItem("sintetico", "Sintético"));
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

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	public boolean getIsApresentarDadosSegundaChamada() {
		return getSituacaoAluno().equals("segundaChamada");
	}

	public String getFiltrarNota() {
		if (filtrarNota == null) {
			filtrarNota = "";
		}
		return filtrarNota;
	}

	public void setFiltrarNota(String filtrarNota) {
		this.filtrarNota = filtrarNota;
	}

	public List<SelectItem> getListaSelectItemFiltrarNota() {
		if (listaSelectItemFiltrarNota == null) {
			listaSelectItemFiltrarNota = new ArrayList<SelectItem>(0);
			listaSelectItemFiltrarNota.add(new SelectItem("combinada", "Combinadas"));
			listaSelectItemFiltrarNota.add(new SelectItem("individual", "Individuais"));
		}
		return listaSelectItemFiltrarNota;
	}

	public void setListaSelectItemFiltrarNota(List<SelectItem> listaSelectItemFiltrarNota) {
		this.listaSelectItemFiltrarNota = listaSelectItemFiltrarNota;
	}

	public List<ConfiguracaoAcademicaNotaVO> getConfiguracaoAcademicaNotaVOs() {
		if (configuracaoAcademicaNotaVOs == null) {
			configuracaoAcademicaNotaVOs = new ArrayList<ConfiguracaoAcademicaNotaVO>(0);
		}
		return configuracaoAcademicaNotaVOs;
	}

	public void setConfiguracaoAcademicaNotaVOs(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs) {
		this.configuracaoAcademicaNotaVOs = configuracaoAcademicaNotaVOs;
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

	public void limparDadosPeriodicidade() {
		if (!getIsApresentarCampoAno()) {
			setAno("");
		}
		if (!getIsApresentarCampoSemestre()) {
			setSemestre("");
		}
	}

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
	}

	public Boolean getApresentarOutrasNotas() {
		if(apresentarOutrasNotas == null) {
			apresentarOutrasNotas = Boolean.FALSE;
		}
		return apresentarOutrasNotas;
	}

	public void setApresentarOutrasNotas(Boolean apresentarOutrasNotas) {
		this.apresentarOutrasNotas = apresentarOutrasNotas;
	}

	public List<ConfiguracaoAcademicaNotaVO> getConfiguracaoAcademicaNotaNaoRecuperacaoVOs() {
		if(configuracaoAcademicaNotaNaoRecuperacaoVOs == null) {
			configuracaoAcademicaNotaNaoRecuperacaoVOs = new ArrayList<ConfiguracaoAcademicaNotaVO>(0);
		}
		return configuracaoAcademicaNotaNaoRecuperacaoVOs;
	}

	public void setConfiguracaoAcademicaNotaNaoRecuperacaoVOs(
			List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs) {
		this.configuracaoAcademicaNotaNaoRecuperacaoVOs = configuracaoAcademicaNotaNaoRecuperacaoVOs;
	}
	
	public void montarListaSelectItemNotaNaoRecuperacao() {
		try {
			executarMontagemConfiguracaoAcademicoNotaNaoReucuperacaoVOs();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void executarMontagemConfiguracaoAcademicoNotaNaoReucuperacaoVOs() throws Exception {
		setMarcarTodasNotaNaoRecuperacao(false);
		if (getApresentarOutrasNotas()) {
			setConfiguracaoAcademicaNotaNaoRecuperacaoVOs(getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademicoNotaRecuperacao(getConfiguracaoAcademicoVO().getCodigo(), false));
		} 
	}

	public Boolean getMarcarTodasNotaNaoRecuperacao() {
		if(marcarTodasNotaNaoRecuperacao == null) {
			marcarTodasNotaNaoRecuperacao = Boolean.FALSE;
		}
		return marcarTodasNotaNaoRecuperacao;
	}

	public void setMarcarTodasNotaNaoRecuperacao(Boolean marcarTodasNotaNaoRecuperacao) {
		this.marcarTodasNotaNaoRecuperacao = marcarTodasNotaNaoRecuperacao;
	}
	
	public void marcarTodasNotaNaoRecuperacaoAction() {
		try {
			for (ConfiguracaoAcademicaNotaVO canVO : getConfiguracaoAcademicaNotaNaoRecuperacaoVOs()) {
				if (getMarcarTodasNotaNaoRecuperacao()) {
					canVO.setUtilizarNotaSegundaChamada(true);
				} else {
					canVO.setUtilizarNotaSegundaChamada(false);
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getColumnNaoRecuperacao() {
		if (getConfiguracaoAcademicaNotaNaoRecuperacaoVOs().size() > 4) {
			return 4;
		}
		return getConfiguracaoAcademicaNotaVOs().size();
	}

	public Integer getElementNaoRecuperacao() {
		return getConfiguracaoAcademicaNotaNaoRecuperacaoVOs().size();
	}
	
	
}
