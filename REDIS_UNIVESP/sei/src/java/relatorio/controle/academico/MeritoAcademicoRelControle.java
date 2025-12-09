package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.MeritoAcademicoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.MeritoAcademicoRel;

@Controller("MeritoAcademicoRelControle")
@Scope("viewScope")
@Lazy
public class MeritoAcademicoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	private TurmaVO turmaVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String campoFiltroPor;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemNotas;
	private List<SelectItem> listaSelectItemGradeCurriculares;
	private Integer periodoLetivo;
	private String tipoLayout;
	private DisciplinaVO disciplinaVO;
	private String tituloNota;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private GradeCurricularVO gradeCurricularVO;
	private Boolean apresentarDisciplinaComposta;
	private String tipoAluno;
	private String campoRankingPor;
	private Double primeiraNota;
	private Double segundaNota;
	private Boolean considerarNotasZeradas;

	public MeritoAcademicoRelControle() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "MeritoAcademicoRelControle", "Inicializando Geração de Relatório Ranking de Notas", "Emitindo Relatório");
			getFacadeFactory().getMeritoAcademicoRelFacade().validarDados(getTurmaVO(), getUnidadeEnsinoCursoVO(), getUnidadeEnsinoVO(), getIsFiltrarPorAno(),
					getIsFiltrarPorSemestre(), getAno(), getSemestre(), getCampoFiltroPor(), getPrimeiraNota(), getSegundaNota());

			List<MeritoAcademicoRelVO> meritoAcademicoRelVOs = getFacadeFactory().getMeritoAcademicoRelFacade().criarObjeto(getTurmaVO(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(),
					getUnidadeEnsinoVO().getCodigo(), getAno(), getSemestre(), getGradeCurricularVO().getCodigo(),
					getUnidadeEnsinoCursoVO().getTurno().getCodigo(), getPeriodoLetivo(), getDisciplinaVO().getCodigo(), 
					getFiltroRelatorioAcademicoVO(), getApresentarDisciplinaComposta(), getTipoLayout(), getTituloNota(),
					getTipoAluno(), getIsFiltrarPorturma(), getPrimeiraNota(), getSegundaNota(),getCampoRankingPor() ,getConsiderarNotasZeradas());
			        
			if (meritoAcademicoRelVOs.isEmpty()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
			} else {
				if (getTipoLayout().equals("media")) {
					getSuperParametroRelVO().setNomeDesignIreport(MeritoAcademicoRel.getDesignIReportRelatorio());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(MeritoAcademicoRel.getDesignIReportRelatorioPorNota());
				}
				String unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome();
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(MeritoAcademicoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Ranking de Notas");
				getSuperParametroRelVO().setListaObjetos(meritoAcademicoRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(MeritoAcademicoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeEmpresa(unidadeEnsino);
				getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsino);
				getSuperParametroRelVO().setCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
				getSuperParametroRelVO().adicionarParametro("rankingPor", campoRankingPor);
				if (Uteis.isAtributoPreenchido(getGradeCurricularVO())) {
					getSuperParametroRelVO().setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				} else {
					getSuperParametroRelVO().setGradeCurricular("");
				}
				getSuperParametroRelVO().setNota(getTituloNota());
				getSuperParametroRelVO().setAno(getAno());
				getSuperParametroRelVO().setSemestre(getSemestre());				
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
				removerObjetoMemoria(this);
				montarListaSelectItemUnidadeEnsino();
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "MeritoAcademicoRelControle", "Finalizando Geração de Relatório Mérito Acadêmico", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinaPorPeriodoLetivo() {
		try {
			List<DisciplinaVO> disciplinaVOs = new ArrayList<DisciplinaVO>(0);
			if (Uteis.isAtributoPreenchido(getPeriodoLetivo())) {
				disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorPeriodoLetivoFazParteComposicao(getPeriodoLetivo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else {
				disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorGradeCurricularFazParteComposicao(getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(disciplinaVOs, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosCursoTurma() throws Exception {
		setTurmaVO(new TurmaVO());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setAno("");
		setSemestre("");
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaSelectItemTurno().clear();
		getListaSelectItemPeriodoLetivo().clear();
		getListaSelectItemNotas().clear();
		getListaSelectItemGradeCurriculares().clear();
	}

	public void limparIdentificador() {
		setTurmaVO(new TurmaVO());
		getListaConsultaTurma().clear();
		setListaSelectItemGradeCurriculares(new ArrayList<SelectItem>(0));
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaSelectItemNotas(new ArrayList<SelectItem>(0));
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
	}

	public void limparDadosCurso() {
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setListaConsultaCurso(new ArrayList<CursoVO>());
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setListaSelectItemGradeCurriculares(new ArrayList<SelectItem>(0));
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaSelectItemNotas(new ArrayList<SelectItem>(0));
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
	}

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboRankingPor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("disciplina", "Disciplina"));
		if(getCampoFiltroPor().equals("curso")) {
			itens.add(new SelectItem("curso", "Curso"));
		}
		
		itens.add(new SelectItem("turma", "Turma"));
		
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoComboLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("media", "Ranking Por Média"));
		itens.add(new SelectItem("nota", "Ranking Por Nota"));
		return itens;
	}

	public Boolean getIsLayoutPorNota() {
		return getTipoLayout().equals("nota");
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
			objs = getFacadeFactory().getCursoFacade().consultarPorCodigoCoordenador(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUsuarioLogado().getPessoa().getCodigo());
		}
		if (getCampoConsultaCurso().equals("nome")) {
			objs = getFacadeFactory().getCursoFacade().consultarPorNomeCoordenador(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), getUsuarioLogado().getPessoa().getCodigo());
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
			objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		if (getCampoConsultaCurso().equals("nome")) {
			objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		return objs;
	}

	public void selecionarCurso() throws Exception {
		try {
			getUnidadeEnsinoCursoVO().setCurso((CursoVO) context().getExternalContext().getRequestMap().get("cursoItens"));
			if (!getUnidadeEnsinoCursoVO().getCurso().getNivelEducacionalPosGraduacao() || !getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("IN")) {
				setMensagemDetalhada("");
				montarListaSelectItemGradeCurricular();
				montarListaSelectItemTurno();
				montarListaSelectItemNotasPorCurso();
			} else {
				throw new Exception("Para Mérito Acadêmico de Pós-Graduação, por favor usar a consulta por turma.");
			}
			setValorConsultaCurso(null);
			setCampoConsultaCurso(null);
			setListaConsultaCurso(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoVO().getCodigo().intValue(), getValorConsultaTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
		getUnidadeEnsinoCursoVO().setCurso(getTurmaVO().getCurso());
		setAno("");
		setSemestre("");
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemNotasPorCursoTurma();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparDadosCursoTurma();
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			// setMensagemID("");
		} catch (Exception e) {
			// //System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void montarListaSelectItemNotasPorCurso() {
		try {
			setTituloNota("");
			setListaSelectItemNotas(new ArrayList<SelectItem>(0));
			montarListaSelectItemNotasPorCurso(getUnidadeEnsinoCursoVO().getCurso());
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemNotasPorCurso(CursoVO cursoVO) throws Exception {
		List<CursoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getCursoFacade().consultarNotasPorCurso(cursoVO.getCodigo());
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem("mediaFinal", "Média Final"));
			int x = 0;
			for (int i = 0; i < resultadoConsulta.size(); i++) {
				x += 1;
				objs.add(new SelectItem("nota" + x, resultadoConsulta.toArray()[i].toString()));
			}
			setListaSelectItemNotas(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemNotasPorCursoTurma() {
		try {
			setTituloNota("");
			setListaSelectItemNotas(new ArrayList<SelectItem>(0));
			montarListaSelectItemNotasPorCursoTurma(getTurmaVO());
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean getIsTurmaAgrupada() {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaAgrupadaPorCodigoTurma(getTurmaVO().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemNotasPorCursoTurma(TurmaVO turmaVO) throws Exception {
		List<SelectItem> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getTurmaFacade().consultarNotasPorCursoTurma(turmaVO.getCodigo());
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem("mediaFinal", "Média Final"));
			int x = 0;
			for (int i = 0; i < resultadoConsulta.size(); i++) {
				x += 1;
				objs.add(new SelectItem("nota" + x, resultadoConsulta.toArray()[i].toString()));
			}
			setListaSelectItemNotas(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			} else {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone())) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public void montarListaSelectItemPeriodoLetivo() {
		try {
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (getCampoFiltroPor().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO()) && Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo().getCodigo())) {
				PeriodoLetivoVO periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getTurmaVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				objs.add(new SelectItem(periodoLetivoVO.getCodigo(), periodoLetivoVO.getDescricao()));
				setPeriodoLetivo(periodoLetivoVO.getCodigo());
				montarListaSelectItemDisciplinaPorPeriodoLetivo();
			} else {
				List<PeriodoLetivoVO> resultadoConsulta = getFacadeFactory().getPeriodoLetivoFacade().consultarPorCursoGradeCurricular(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getGradeCurricularVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				Iterator<PeriodoLetivoVO> i = resultadoConsulta.iterator();
				if (resultadoConsulta.size() > 0) {
					if (resultadoConsulta.size() != 1) {
						objs.add(new SelectItem(0, "Todos"));
					}
					while (i.hasNext()) {
						PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
						if (getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("SU") || getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("GT")) {
							objs.add(new SelectItem(obj.getCodigo(), obj.getPeriodoLetivo().toString() + "º Semestre"));
						} else {
							objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
						}
					}
				}
				montarListaSelectItemDisciplinaPorPeriodoLetivo();
			}
			setListaSelectItemPeriodoLetivo(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurno() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		List<UnidadeEnsinoCursoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo().intValue(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		Iterator<UnidadeEnsinoCursoVO> i = resultadoConsulta.iterator();
		if (resultadoConsulta.size() > 0) {
			objs.add(new SelectItem(0, "Todos"));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
				objs.add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
			}
		}
		setListaSelectItemTurno(objs);
	}

	/**
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	/**
	 * @param valorConsultaCurso
	 *            the valorConsultaCurso to set
	 */
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the listaConsultaCurso
	 */
	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "curso";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> objs) {
		this.listaSelectItemUnidadeEnsino = objs;
	}

	public String getAno() {
		if (ano == null) {
			return "";
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

	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public Boolean getIsFiltrarPorturma() {
		return getCampoFiltroPor().equals("turma");
	}

	public Boolean getIsFiltrarPorCurso() {
		return getCampoFiltroPor().equals("curso");
	}

	public Boolean getIsFiltrarPorAno() {
		if (getTurmaVO().getTurmaAgrupada()) {
			for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
				if (turmaAgrupada.getTurma().getCurso().getPeriodicidade().equals("AN") || turmaAgrupada.getTurma().getCurso().getPeriodicidade().equals("SE")) {
					return true;
				}
			}
			return false;
		}
		return getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("AN") || getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE");
	}

	public Boolean getIsFiltrarPorSemestre() {
		if (getTurmaVO().getTurmaAgrupada()) {
			for (TurmaAgrupadaVO turmaAgrupada : getTurmaVO().getTurmaAgrupadaVOs()) {
				if (turmaAgrupada.getTurma().getCurso().getPeriodicidade().equals("SE")) {
					return true;
				}
			}
			return false;
		}
		return getUnidadeEnsinoCursoVO().getCurso().getPeriodicidade().equals("SE");
	}

	public Boolean getIsFiltrarPorTurno() {
		if (getListaSelectItemTurno().size() > 0) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
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

	public String getTituloNota() {
		if (tituloNota == null) {
			tituloNota = "";
		}
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	public List<SelectItem> getListaSelectItemNotas() {
		if (listaSelectItemNotas == null) {
			listaSelectItemNotas = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNotas;
	}

	public void setListaSelectItemNotas(List<SelectItem> listaSelectItemNotas) {
		this.listaSelectItemNotas = listaSelectItemNotas;
	}

	public void montarListaSelectItemGradeCurricular() throws Exception {
		List<GradeCurricularVO> resultadoConsulta = null;
		if (getCampoFiltroPor().equals("turma") && Uteis.isAtributoPreenchido(getTurmaVO()) && Uteis.isAtributoPreenchido(getTurmaVO().getGradeCurricularVO())) {
			resultadoConsulta = new ArrayList<GradeCurricularVO>(0);
			resultadoConsulta.add(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getTurmaVO().getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setGradeCurricularVO(resultadoConsulta.get(0));
			montarListaSelectItemPeriodoLetivo();
		} else {
			resultadoConsulta = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (GradeCurricularVO obj : resultadoConsulta) {
				if (obj.getSituacao().equals("AT")) {
					getGradeCurricularVO().setCodigo(obj.getCodigo());
					montarListaSelectItemPeriodoLetivo();
					break;
				}
			}
		}
		setListaSelectItemGradeCurriculares(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
	}

	public List<SelectItem> getListaSelectItemGradeCurriculares() {
		if (listaSelectItemGradeCurriculares == null) {
			listaSelectItemGradeCurriculares = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurriculares;
	}

	public void setListaSelectItemGradeCurriculares(List<SelectItem> listaSelectItemGradeCurriculares) {
		this.listaSelectItemGradeCurriculares = listaSelectItemGradeCurriculares;
	}

	public boolean getIsApresentarCampoGradeCurricular() {
		return Uteis.isAtributoPreenchido(getListaSelectItemGradeCurriculares());
	}

	public boolean getIsApresentarCampoNota() {
		return Uteis.isAtributoPreenchido(getListaSelectItemNotas());
	}

	public boolean getIsApresentarCampoPeriodoLetivo() {
		return Uteis.isAtributoPreenchido(getListaSelectItemPeriodoLetivo());
	}

	public boolean getIsApresentarCampoDisciplina() {
		return Uteis.isAtributoPreenchido(getListaSelectItemDisciplina());
	}

	public boolean getIsApresentarCampoTurno() {
		return Uteis.isAtributoPreenchido(getListaSelectItemTurno());
	}

	/**
	 * @return the filtroRelatorioAcademicoVO
	 */
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	/**
	 * @param filtroRelatorioAcademicoVO
	 *            the filtroRelatorioAcademicoVO to set
	 */
	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

	/**
	 * @return the gradeCurricularVO
	 */
	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	/**
	 * @param gradeCurricularVO
	 *            the gradeCurricularVO to set
	 */
	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	/**
	 * @return the apresentarDisciplinaComposta
	 */
	public Boolean getApresentarDisciplinaComposta() {
		if (apresentarDisciplinaComposta == null) {
			apresentarDisciplinaComposta = false;
		}
		return apresentarDisciplinaComposta;
	}

	/**
	 * @param apresentarDisciplinaComposta
	 *            the apresentarDisciplinaComposta to set
	 */
	public void setApresentarDisciplinaComposta(Boolean apresentarDisciplinaComposta) {
		this.apresentarDisciplinaComposta = apresentarDisciplinaComposta;
	}

	public List<SelectItem> getListaSelectItemTipoAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		return itens;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "normal";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public Boolean getIsCursoPreenchido() {
		if (getCampoFiltroPor().equals("curso") && !getUnidadeEnsinoCursoVO().getCurso().getCodigo().equals(0) && getListaSelectItemPeriodoLetivo().size() > 0) {
			return true;
	    }
	    return false;
	}

	public Double getPrimeiraNota() {
		if (primeiraNota == null) {
			primeiraNota = 0.0;
		}
		return primeiraNota;
	}

	public void setPrimeiraNota(Double primeiraNota) {
		this.primeiraNota = primeiraNota;
	}

	public Double getSegundaNota() {
		if (segundaNota == null) {
			segundaNota = 0.0;
		}
		return segundaNota;
	}

	public void setSegundaNota(Double segundaNota) {
		this.segundaNota = segundaNota;
	}

	public String getCampoRankingPor() {
		if(campoRankingPor == null) {
			campoRankingPor = "Disciplina";
		}
		return campoRankingPor;
	}

	public void setCampoRankingPor(String campoRankingPor) {
		this.campoRankingPor = campoRankingPor;
	}

	public Boolean getConsiderarNotasZeradas() {
		if(considerarNotasZeradas == null) {
			considerarNotasZeradas = false;
		}
		return considerarNotasZeradas;
	}

	public void setConsiderarNotasZeradas(Boolean considerarNotasZeradas) {
		this.considerarNotasZeradas = considerarNotasZeradas;
	}	
}
