package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoAlunoControle;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.HorarioDaTurmaPrincipalRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("HorarioDaTurmaRelControle")
@Scope("viewScope")
public class HorarioDaTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	protected List<SelectItem> listaSelectItemUnidadeEnsino;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	protected List<TurmaVO> listaConsultaTurma;
	protected String valorConsultaTurma;
	protected String campoConsultaTurma;
	protected TurmaVO turmaVO;
	protected boolean apresentarProfessor = false;
	protected boolean apresentarSala = false;
	protected List<SelectItem> listaSelectItemTurma;
	private String tipoLayout;
	private String ano;
	private String semestre;
	private Date dataBaseHorarioAula;
	private Date dataInicio;
	private Date dataTermino;
	private String mesAnoApresentar;

	public HorarioDaTurmaRelControle() {
		super();
		
	}
	
	@PostConstruct
	public void  init() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			montarListaSelectItemUnidadeEnsino();
			realizarCarregamentoVindoTelaProgramacaoAulaTurma();
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaSelectItemTurmaVisaoCoordenador();
		}
		
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
		getFacadeFactory().getTurmaFacade().carregarDados(obj, getUsuarioLogado());
		setTurmaVO(obj);
		getUnidadeEnsinoVO().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
		carregarDadosLayoutPadraoPorIdentificadorTurma();
		obj = null;
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
	}

	public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null) {
		tipoConsultaComboTurma = new ArrayList<SelectItem>();
		tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
		tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public boolean isApresentarProfessor() {
		return apresentarProfessor;
	}

	public void setApresentarProfessor(boolean apresentarProfessor) {
		this.apresentarProfessor = apresentarProfessor;
	}

	public void imprimirPDFVisaoAluno() throws Exception {
		VisaoAlunoControle visaoAlunoControle = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "HorarioDaTurmaRelControle", "Inicializando Geração de Relatório Horario da Turma", "Emitindo Relatório");
			visaoAlunoControle = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get(VisaoAlunoControle.class.getSimpleName());
			if (visaoAlunoControle != null) {
				MatriculaPeriodoVO matriculaPeriodo = null;
				matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatriculaSemExcecao(visaoAlunoControle.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				setTurmaVO(matriculaPeriodo.getTurma());
				// setDataInicio(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(getTurmaVO().getCodigo(),
				// matriculaPeriodo.getAno(), matriculaPeriodo.getSemestre()));
				// setDataFim(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(getTurmaVO().getCodigo(),
				// matriculaPeriodo.getAno(), matriculaPeriodo.getSemestre()));
				setUnidadeEnsinoVO(visaoAlunoControle.getMatricula().getUnidadeEnsino());
				// setCurso(visaoAlunoControle.getMatricula().getCurso());
				// setNivelEducacional(visaoAlunoControle.getMatricula().getCurso().getNivelEducacional());
				setTipoLayout("HorarioDaTurmaRel");
				imprimirPDF();
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "CronogramaDeAulasRelControle", "Finalizando Geração de Relatório Cronograma de Aulas", "Emitindo Relatório");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}

	}

	public void imprimirPDF() {
		List<HorarioDaTurmaPrincipalRelVO> listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "HorarioDaTurmaRelControle", "Inicializando Geração de Relatório Horário da Turma", "Emitindo Relatório");
			if (getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo()) {
				setAno("");
				setSemestre("");
			}
			if (getTurmaVO().getAnual()) {
				setSemestre("");
			}
			getFacadeFactory().getHorarioDaTurmaRelFacade().validarDados(getTurmaVO(), getAno(), getSemestre(), getUnidadeEnsinoVO(), getUsuarioLogado(), getTipoLayout(), getDataBaseHorarioAula());
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				setTipoLayout("HorarioDaTurmaRel");
			}
			listaObjetos = getFacadeFactory().getHorarioDaTurmaRelFacade().criarObjeto(getTurmaVO(), isApresentarProfessor(), isApresentarSala(), getAno(), getSemestre(), getUsuarioLogado(), getUnidadeEnsinoLogado().getCodigo(), getTipoLayout(), getDataInicio(), getDataTermino());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getHorarioDaTurmaRelFacade().designIReportRelatorio(getTipoLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getHorarioDaTurmaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Horário da Turma");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getHorarioDaTurmaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
					getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
				}
				getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
				realizarImpressaoRelatorio();
				persistirLayoutPadraoPorIdentificadorTurma();
				removerObjetoMemoria(this);
				montarListaSelectItemUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "HorarioDaTurmaRelControle", "Finalizando Geração de Relatório Horário da Turma", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
			listaObjetos = null;
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				montarListaSelectItemTurmaVisaoCoordenador();
			}
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (getUnidadeEnsinoVO().getCodigo().equals(0) && !getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoVO().getCodigo().intValue(), getTurmaVO().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getUnidadeEnsinoVO().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
			carregarDadosLayoutPadraoPorIdentificadorTurma();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", super.getUnidadeEnsinoLogado().getCodigo().equals(0)));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurmaVisaoCoordenador() {
		try {
			List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
			List<TurmaVO> listaResultado = consultarTurmaPorCoordenador();
			getListaSelectItemTurma().clear();
			String value = "";
			for (TurmaVO turma : listaResultado) {
				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				selectItems.add(new SelectItem(turma.getCodigo(), value));
			}
			Collections.sort(selectItems, new SelectItemOrdemValor());
			setListaSelectItemTurma(selectItems);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		}
	}

	private List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void preencherDadosTurma() throws Exception {
		if (!getTurmaVO().getCodigo().equals(0)) {
			getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public void limparIdentificador() {
		setTurmaVO(null);
	}

	public void limparConsulta() {
		setListaConsultaTurma(null);
		setMensagemID("msg_entre_prmconsulta");
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
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

	public List<SelectItem> listaSelectItemTipoLayout;
	public List<SelectItem> getListaSelectItemTipoLayout() {
		if(listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
			listaSelectItemTipoLayout.add(new SelectItem("HorarioDaTurmaRel", "Layout 1"));
			listaSelectItemTipoLayout.add(new SelectItem("HorarioDaTurma2Rel", "Layout 2"));
			listaSelectItemTipoLayout.add(new SelectItem("HorarioDaTurmaSemanalRel", "Layout Semanal"));
		}
		return listaSelectItemTipoLayout;
	}
	
	public List<SelectItem> listaSelectItemSemestre;
	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null) {
		listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre.add(new SelectItem("", ""));
		listaSelectItemSemestre.add(new SelectItem("1", "1º"));
		listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public String getAno() {
		if (ano == null) {			
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
			
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
						setAno(getAno());
						return true;
					} else {
						setAno("");
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
						setAno(getAno());
					} else {
						setAno("");
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(getSemestre());
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(getSemestre());
					} else {
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean getIsApresentarAno() {		
		return Uteis.isAtributoPreenchido(getTurmaVO().getCodigo()) && (getTurmaVO().getAnual() || getTurmaVO().getSemestral());
	}

	public boolean getIsApresentarSemestre() {
		return Uteis.isAtributoPreenchido(getTurmaVO().getCodigo()) && (getTurmaVO().getSemestral());
	}

	/**
	 * @return the apresentarSala
	 */
	public boolean isApresentarSala() {
		return apresentarSala;
	}

	/**
	 * @param apresentarSala
	 *            the apresentarSala to set
	 */
	public void setApresentarSala(boolean apresentarSala) {
		this.apresentarSala = apresentarSala;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public void executarMontagemPeriodoSemanaSelecionada() {
		try {
			if (getCalendarioMensal()) {
				setDataInicio(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(Uteis.getDataPrimeiroDiaMes(getDataBaseHorarioAula()))), 1));
				setDataTermino(Uteis.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getUltimoDiaSemana(Uteis.getDataUltimoDiaMes(getDataBaseHorarioAula()))), 1));
			} else {
				setDataInicio(UteisData.obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(UteisData.getPrimeiroDiaSemana(getDataBaseHorarioAula())), 1));
				setDataTermino(UteisData.obterDataFutura(UteisData.getUltimoDiaSemana(getDataBaseHorarioAula()), 1));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataBaseHorarioAula() {
		if (dataBaseHorarioAula == null) {
			dataBaseHorarioAula = new Date();
			executarMontagemPeriodoSemanaSelecionada();
		}
		return dataBaseHorarioAula;
	}

	public void setDataBaseHorarioAula(Date dataBaseHorarioAula) {
		this.dataBaseHorarioAula = dataBaseHorarioAula;
	}

	public boolean getCalendarioMensal() {
		return getTurmaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor());
	}

	public boolean getApresentarCalendario() {
		return getTipoLayout().equals("HorarioDaTurmaSemanalRel");
	}

	public String getMesAnoApresentar() {
		if (mesAnoApresentar == null) {
			mesAnoApresentar = "";
		}
		return mesAnoApresentar;
	}

	public void setMesAnoApresentar(String mesAnoApresentar) {
		this.mesAnoApresentar = mesAnoApresentar;
	}
	
	void realizarCarregamentoVindoTelaProgramacaoAulaTurma() {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getSessionMap().get("turmaHorarioTurmaTelaProgramacao");
			if (Uteis.isAtributoPreenchido(turma)) {
				getFacadeFactory().getTurmaFacade().carregarDados(turma, getUsuarioLogado());
				setTurmaVO(turma);
				getUnidadeEnsinoVO().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
				carregarDadosLayoutPadraoPorIdentificadorTurma();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("turmaHorarioTurmaTelaProgramacao");
		}
	}

	private void persistirLayoutPadraoPorIdentificadorTurma() throws Exception {
		String entidade = "HorarioTurmaRel_" + getTurmaVO().getUnidadeEnsino().getCodigo() + "_" + getTurmaVO().getIdentificadorTurma();
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoLayout(), entidade, "tipoLayoutPorIdTurma", getUsuarioLogado());
	}
	
	private void carregarDadosLayoutPadraoPorIdentificadorTurma() throws Exception {
		String entidade = "HorarioTurmaRel_" + getTurmaVO().getUnidadeEnsino().getCodigo() + "_" + getTurmaVO().getIdentificadorTurma();
		Map<String, String> resultados = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "tipoLayoutPorIdTurma" }, entidade);
		resultados.forEach((key, value) -> {
			if (key.equals("tipoLayoutPorIdTurma") && value != null && !value.equals("")) {
				setTipoLayout(value);
			}
		});
	}
}
