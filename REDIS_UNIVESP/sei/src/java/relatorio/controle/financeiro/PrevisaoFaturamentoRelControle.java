package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.PrevisaoFaturamentoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;
import relatorio.negocio.jdbc.financeiro.PrevisaoFaturamentoRel;

@Controller("PrevisaoFaturamentoRelControle")
@Scope("viewScope")
@Lazy
public class PrevisaoFaturamentoRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8940668123823942335L;
	protected Date dataInicio;
	protected Date dataFim;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemCurso;
	private List listaSelectItemTurma;
	private List listaSelectItemTurno;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private TurnoVO turnoVO;
	private Boolean gerarRelatorio;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String ordenador;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private Boolean filtrarPorDataCompetencia;
	private Boolean considerarUnidadeEnsinoFinanceira;
	private FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO;

	public PrevisaoFaturamentoRelControle() throws Exception {
		inicializarDadosControle();
		setMensagemID("msg_entre_prmrelatorio");
	}

	private void inicializarDadosControle() {
		inicializarListasSelectItemTodosComboBox();
		carregarDadosLayoutRelatorio();
	}

	public void carregarDadosLayoutRelatorio() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "PrevisaoFaturamentoRel", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroRelatorioContaReceberVO(getFiltroRelatorioContaReceberVO(), "PrevisaoFaturamentoRel", getUsuarioLogado());
		Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "filtrarPorDataCompetencia", "considerarUnidadeEnsinoFinanceira","ordenador"  }, "PrevisaoFaturamentoRel");
			camposPadroes.entrySet().stream().forEach(p -> {
				if (p.getKey().equals("filtrarPorDataCompetencia") && Uteis.isAtributoPreenchido(p.getValue())) {
					setFiltrarPorDataCompetencia(Boolean.parseBoolean(p.getValue()));
				} else if (p.getKey().equals("considerarUnidadeEnsinoFinanceira") && Uteis.isAtributoPreenchido(p.getValue())) {
					setConsiderarUnidadeEnsinoFinanceira(Boolean.parseBoolean(p.getValue()));
				} else if (p.getKey().equals("ordenador") && Uteis.isAtributoPreenchido(p.getValue())) {
					setOrdenador(p.getValue());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Boolean getGerarRelatorio() {
		if (gerarRelatorio == null) {
			gerarRelatorio = false;
		}
		return gerarRelatorio;
	}

	public void setGerarRelatorio(Boolean gerarRelatorio) {
		this.gerarRelatorio = gerarRelatorio;
	}

	public void imprimirPDF() {
		List<PrevisaoFaturamentoRelVO> listaObjetos = null;
		try {
			getFacadeFactory().getPrevisaoFaturamentoRelFacade().validarDados(getUnidadeEnsinoVOs(), getDataInicio(), getDataFim());
			listaObjetos = getFacadeFactory().getPrevisaoFaturamentoRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getFiltrarPorDataCompetencia(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getDataInicio(), getDataFim(), getOrdenador(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioContaReceberVO(), getConsiderarUnidadeEnsinoFinanceira(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(PrevisaoFaturamentoRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Previsão de Faturamento");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
				} else {
					getSuperParametroRelVO().setCurso("Todos os Cursos");
				}
				if (getTurnoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setTurno((getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getTurnoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				} else {
					getSuperParametroRelVO().setTurno("Todos os Turnos");
				}
				if (getTurmaVO().getCodigo() != 0) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("Todas as Turmas");
				}
				if (getOrdenador().equals("nome")) {
					getSuperParametroRelVO().setOrdenadoPor("Nome");
				} else if (getOrdenador().equals("dataVencimento")) {
					getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
				} else if (getOrdenador().equals("tipoOrigem")) {
					getSuperParametroRelVO().setOrdenadoPor("Tipo de Origem");
				} else if (getOrdenador().equals("parcela")) {
					getSuperParametroRelVO().setOrdenadoPor("Parcela");
				}
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				persistirLayoutPadrao();
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

	public void imprimirExcel() {
		List<PrevisaoFaturamentoRelVO> listaObjetos = null;
		try {
			getFacadeFactory().getPrevisaoFaturamentoRelFacade().validarDados(getUnidadeEnsinoVOs(), getDataInicio(), getDataFim());
			listaObjetos = getFacadeFactory().getPrevisaoFaturamentoRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getFiltrarPorDataCompetencia(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getDataInicio(), getDataFim(), getOrdenador(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioContaReceberVO(), getConsiderarUnidadeEnsinoFinanceira(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(PrevisaoFaturamentoRel.getDesignIReportRelatorioExcel());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Previsão de Faturamento");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(PrevisaoFaturamentoRel.getCaminhoBaseDesignIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
				} else {
					getSuperParametroRelVO().setCurso("Todos os Cursos");
				}
				if (getTurnoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setTurno((getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(getTurnoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
				} else {
					getSuperParametroRelVO().setTurno("Todos os Turnos");
				}
				if (getTurmaVO().getCodigo() != 0) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("Todas as Turmas");
				}
				if (getOrdenador().equals("nome")) {
					getSuperParametroRelVO().setOrdenadoPor("Nome");
				} else if (getOrdenador().equals("dataVencimento")) {
					getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
				} else if (getOrdenador().equals("tipoOrigem")) {
					getSuperParametroRelVO().setOrdenadoPor("Tipo de Origem");
				} else if (getOrdenador().equals("parcela")) {
					getSuperParametroRelVO().setOrdenadoPor("Parcela");
				}
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				persistirLayoutPadrao();
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
	
	private void persistirLayoutPadrao() throws Exception{
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "PrevisaoFaturamentoRel", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroRelatorioContaReceberVO(getFiltroRelatorioContaReceberVO(), "PrevisaoFaturamentoRel", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltrarPorDataCompetencia().toString(), "PrevisaoFaturamentoRel", "filtrarPorDataCompetencia", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getConsiderarUnidadeEnsinoFinanceira().toString(), "PrevisaoFaturamentoRel", "considerarUnidadeEnsinoFinanceira", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getOrdenador(), "PrevisaoFaturamentoRel", "ordenador", getUsuarioLogado());
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoConfirmado", getFiltroRelatorioAcademicoVO().getConfirmado());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna", getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaSaida", getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademicoVO().getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonoCurso", getFiltroRelatorioAcademicoVO().getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoJubilado", getFiltroRelatorioAcademicoVO().getJubilado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem("", ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemTurma(new ArrayList(0));
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem("", ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}

	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorUnidadeEnsino(codigoUnidadeEnsino, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemTurma() throws Exception {
		try {
			setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			List<TurmaVO> resultadoConsulta = consultarTurmasPorCurso();
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<TurmaVO> consultarTurmasPorCurso() throws Exception {
		List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsinoCursoVO().getUnidadeEnsino(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurnoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			getFacadeFactory().getPrevisaoFaturamentoRelFacade().validarDados(getUnidadeEnsinoVOs(), getDataInicio(), getDataFim());
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			setUnidadeEnsinoCursoVO(obj);
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			montarListaTurnoCurso();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void montarListaTurnoCurso() throws Exception {
		try {
			List<TurnoVO> resultadoConsulta = consultarTurnoPorCurso();
			setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<TurnoVO> consultarTurnoPorCurso() throws Exception {
		List<TurnoVO> lista = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getListaSelectItemOrdenador() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataVencimento", "Data Vencimento"));
		itens.add(new SelectItem("tipoOrigem", "Tipo Origem"));
		itens.add(new SelectItem("parcela", "Parcela"));
		return itens;
	}

	public void limparCurso() {
		getUnidadeEnsinoCursoVO().setCurso(null);
		setListaSelectItemTurma(null);
		setListaSelectItemTurno(null);
		setTurmaVO(null);
		setTurnoVO(null);
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getNewDateComUmMesAMais();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList();
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the listaSelectItemCurso
	 */
	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	/**
	 * @param listaSelectItemCurso
	 *            the listaSelectItemCurso to set
	 */
	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the unidadeEnsinoVO
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	/**
	 * @param unidadeEnsinoVO
	 *            the unidadeEnsinoVO to set
	 */
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	/**
	 * @return the unidadeEnsinoCursoVO
	 */
	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	/**
	 * @param unidadeEnsinoCursoVO
	 *            the unidadeEnsinoCursoVO to set
	 */
	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	/**
	 * @return the turmaVO
	 */
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	/**
	 * @param turmaVO
	 *            the turmaVO to set
	 */
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			campoConsultaCurso = "";
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
	 * @return the listaConsultaCurso
	 */
	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * @return the cursoVO
	 */
	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	/**
	 * @param cursoVO
	 *            the cursoVO to set
	 */
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	/**
	 * @return the listaSelectItemTurno
	 */
	public List getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList(0);
		}
		return listaSelectItemTurno;
	}

	/**
	 * @param listaSelectItemTurno
	 *            the listaSelectItemTurno to set
	 */
	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the turnoVO
	 */
	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	/**
	 * @param turnoVO
	 *            the turnoVO to set
	 */
	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public Boolean getApresentarTurno() {
		if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarTurma() {
		if (getTurnoVO().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return the ordenador
	 */
	public String getOrdenador() {
		if (ordenador == null) {
			ordenador = "";
		}
		return ordenador;
	}

	/**
	 * @param ordenador
	 *            the ordenador to set
	 */
	public void setOrdenador(String ordenador) {
		this.ordenador = ordenador;
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

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("PrevisaoFaturamentoRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public Boolean getFiltrarPorDataCompetencia() {
		if (filtrarPorDataCompetencia == null) {
			filtrarPorDataCompetencia = false;
		}
		return filtrarPorDataCompetencia;
	}

	public void setFiltrarPorDataCompetencia(Boolean filtrarPorDataCompetencia) {
		this.filtrarPorDataCompetencia = filtrarPorDataCompetencia;
	}

	public FiltroRelatorioContaReceberVO getFiltroRelatorioContaReceberVO() {
		if (filtroRelatorioContaReceberVO == null) {
			filtroRelatorioContaReceberVO = new FiltroRelatorioContaReceberVO();
		}
		return filtroRelatorioContaReceberVO;
	}

	public void setFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO) {
		this.filtroRelatorioContaReceberVO = filtroRelatorioContaReceberVO;
	}

	public Boolean getConsiderarUnidadeEnsinoFinanceira() {
		if (considerarUnidadeEnsinoFinanceira == null) {
			considerarUnidadeEnsinoFinanceira = false;
		}
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(Boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}
	
	

}
