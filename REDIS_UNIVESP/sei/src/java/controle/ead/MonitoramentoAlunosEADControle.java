package controle.ead;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConteudoRegistroAcessoVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.ItemParametrosMonitoramentoAvaliacaoOnlineVO;
import negocio.comuns.ead.MonitorConhecimentoVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.ead.MonitoramentoAlunosEAD;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Victor Hugo 10/12/2014
 */
@Controller("MonitoramentoAlunosEADControle")
@Scope("viewScope")
public class MonitoramentoAlunosEADControle extends SuperControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SelectItem> listaSelectItemNivelProgramacaoTutoria;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private List<SelectItem> listaSelectItemTurma;
	private Boolean buscarTurmasAnteriores;
	private TurmaVO turmaVO;
	private TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria;
	private DisciplinaVO disciplinaVO;
	private CursoVO cursoVO;
	private List<SelectItem> listaSelectItemCursos;
	private Boolean estudando;
	private Boolean concluiram;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs;
	private Boolean mostrarGabarito;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	private Integer totalQuestoes;
	private List<AtividadeDiscursivaRespostaAlunoVO> atividadeDiscursivaRespostaAlunoVO;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboDisciplina;
	private List<SelectItem> listaSelectItemDisciplinas;
	private List<TurmaVO> listaConsultaTurmaVOs;
	private List<CursoVO> listaConsultaCursoVOs;
	private String ano;
	private String semestre;
	private String tipoNivelConhecimento;
	private List<SelectItem> listaSelectItemTemasAssuntosDisciplina;
	private TemaAssuntoVO temaAssuntoVO;
	private MonitorConhecimentoVO monitorConhecimentoVO;
	private CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO;
	private List<SelectItem> listaSelectItemNivel;
	private ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO;
	private Boolean selecionarAtividade;
	private Integer nrAtividadeSelecionadas;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private boolean navegarGestaoEventoMonitoramentoEad = false;
	private ConteudoVO conteudoVO;
    private List<SelectItem> listaSelectItemPeriodicidade;
    private PeriodicidadeEnum periodicidade;
	private Date dataInicio;
	private Date dataFim;
	private MatriculaVO matriculaVO;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaAluno;
	private Boolean situacaoAvaliacaoOnlineAprovado;
	private Boolean situacaoAvaliacaoOnlineReprovado;
	private Boolean situacaoAvaliacaoOnlineAguardandoExecucao;
	private Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado;
	private Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada;
	private Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor;
	private Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno;
	private Boolean situacaoAtividadeDiscursivaAvaliada;
	private Boolean situacaoDuvidaTutorAguardandoRespostaProfessor;
	private Boolean situacaoDuvidaTutorAguardandoRespostaAluno;
	private Double percentualInicio;
	private Double percentualFim;
	private String campoConsultaAvaliacaoOnlineRea;
	private String valorConsultaAvaliacaoOnlineRea;
	private List<AvaliacaoOnlineVO> listaConsultaAvaliacaoOnlineRea;
	private List<SelectItem> tipoConsultaComboAvaliacaoOnlineRea;
	private AvaliacaoOnlineVO avaliacaoOnlineVO;
	private String campoConsultaAtividadeDiscursiva;
	private String valorConsultaAtividadeDiscursiva;
	private List<AtividadeDiscursivaVO> listaConsultaAtividadeDiscursiva;
	private List<SelectItem> tipoConsultaComboAtividadediscursiva;
	private AtividadeDiscursivaVO atividadeDiscursivaVO;
	private String campoConsultaTutor;
	private String valorConsultaTutor;
	private List<FuncionarioVO> listaConsultaTutor;
	private List<SelectItem> tipoConsultaComboTutor;
	private FuncionarioVO tutor;


	@PostConstruct
	public void init() throws Exception {
		LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo(Matricula.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", false, getUsuarioLogado());
		if (Uteis.isAtributoPreenchido(layoutPadraoVO)) {
			setVersaoNova(Boolean.valueOf(layoutPadraoVO.getValor()));
			setVersaoAntiga(!Boolean.valueOf(layoutPadraoVO.getValor()));
		} else {
			setVersaoAntiga(false);
			setVersaoNova(true);
		}
		setControleConsultaOtimizado(new DataModelo());		
		montarComboBoxFiltroConsulta();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemDisciplinasProfessor();
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaTurmasVisaoCoodernador();
		}
		if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarGestaoEventoMonitoramentoEad"))) {
			carregarDadosGestaoEventoConteudoTurma();
		}else if (Uteis.isAtributoPreenchido((Boolean) context().getExternalContext().getSessionMap().get("navegarProfessorAtividadeDiscursivaMonitoramentoEad"))) {
			carregarDadosAtividadesDiscursivasVisaoProfessor();
		}
		
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}
	
	public void carregarDadosGestaoEventoConteudoTurma() {
		try {			
			montarListaSelectItemTurmasProfessor();
			setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum.TURMA);
			getTurmaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("turmaGestao"));
			setAno((String) context().getExternalContext().getSessionMap().get("anoGestao"));
			setSemestre((String) context().getExternalContext().getSessionMap().get("semestreGestao"));
			getDisciplinaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("disciplinaGestao"));
			setConteudoVO((ConteudoVO) context().getExternalContext().getSessionMap().get("conteudoGestao"));
			setEstudando(true);
			setNavegarGestaoEventoMonitoramentoEad(true);
			montarListaDisciplinaTurmaVisaoProfessor();
			context().getExternalContext().getSessionMap().remove("navegarGestaoEventoMonitoramentoEad");
			context().getExternalContext().getSessionMap().remove("conteudoGestao");
			context().getExternalContext().getSessionMap().remove("turmaGestao");
			context().getExternalContext().getSessionMap().remove("anoGestao");
			context().getExternalContext().getSessionMap().remove("semestreGestao");
			context().getExternalContext().getSessionMap().remove("disciplinaGestao");
			setControleConsultaOtimizado(new DataModelo());
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String navegarGestaoEventoTurma() {
		context().getExternalContext().getSessionMap().put("navegarGestaoEventoMonitoramentoEad", true);
		context().getExternalContext().getSessionMap().put("turmaGestao", getTurmaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("anoGestao", getAno());
		context().getExternalContext().getSessionMap().put("semestreGestao", getSemestre());
		context().getExternalContext().getSessionMap().put("disciplinaGestao", getDisciplinaVO().getCodigo());
		context().getExternalContext().getSessionMap().put("conteudoGestao", getConteudoVO());
		return Uteis.getCaminhoRedirecionamentoNavegacao("gestaoEventoConteudoCons.xhtml");
	}

	@SuppressWarnings("unchecked")
	public void selecionarTodasAtividades() {
		int cont = 0;
		for (CalendarioAtividadeMatriculaVO object : (List<CalendarioAtividadeMatriculaVO>) getControleConsultaOtimizado().getListaConsulta()) {
			if ((object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS)) || !(object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO) || object.getTipoCalendarioAtividade().equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS) && object.getSituacaoAtividade().equals(SituacaoAtividadeEnum.NAO_CONCLUIDA))) {
				object.setSelecionarAtividade(getSelecionarAtividade());
				cont++;
			}
		}
		if (getSelecionarAtividade() == false) {
			setNrAtividadeSelecionadas(0);
		} else {
			setNrAtividadeSelecionadas(cont);
		}
	}

	public void selecionarAtividade() {
		int cont = 0;
		CalendarioAtividadeMatriculaVO obj = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosItens");
		if (obj.getSelecionarAtividade()) {
			cont++;
		} else {
			cont--;
		}
		setNrAtividadeSelecionadas(getNrAtividadeSelecionadas() + cont);
	}

	public void inicializarComunicacao() {
		getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout());
		limparMensagem();
	}
	
	public void realizarEnvioComunicadoInterno() {
		try {
			getFacadeFactory().getMonitoramentoAlunosEADFacade().realizarEnvioComunicadoInternoComEnvioEmail(getComunicacaoInternaVO(), (List<CalendarioAtividadeMatriculaVO>) getControleConsultaOtimizado().getListaConsulta(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_msg_enviados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarGeracaoMonitoramentoDeConhecimentoAssuntos() {
		try {
			GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO = (GraficoAproveitamentoAvaliacaoVO) context().getExternalContext().getRequestMap().get("grafico");
			setMonitorConhecimentoVO(new MonitorConhecimentoVO());
			getCalendarioAtividadeMatriculaVO().setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMonitorConhecimentoVO(getFacadeFactory().getMonitorConhecimentoFacade().realizarGeracaoGraficosMonitorConhecimento(graficoAproveitamentoAvaliacaoVO, getMonitorConhecimentoVO(), 250, 350, 350, getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO(), getCalendarioAtividadeMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCurso().getCodigo(), getUsuarioLogado()));
		} catch (CloneNotSupportedException e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void paginarConsulta(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultar();
	}

	public String consultar() {
		try {			
			getControleConsultaOtimizado().setLimitePorPagina(5);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarAlunosDoTutorPorTurmaCursoDisciplinaUnidadeEnsinoMonitoramentoEAD(getTipoNivelProgramacaoTutoria(), getTurmaVO(), getUnidadeEnsinoVO().getCodigo(), getDisciplinaVO(), getCursoVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getEstudando(), getConcluiram(), getIsApresentarAno(), getAno(), getSemestre(), getTemaAssuntoVO().getCodigo(), getItemParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo(), getPeriodicidade(), getDataInicio(), getDataFim(),  getFiltroRelatorioAcademicoVO(), getMatriculaVO(), getSituacaoAvaliacaoOnlineAprovado(), getSituacaoAvaliacaoOnlineReprovado(), getSituacaoAvaliacaoOnlineAguardandoExecucao(), getSituacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado(), getSituacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada(), getAvaliacaoOnlineVO().getCodigo(), getSituacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor(), getSituacaoAtividadeDiscursivaAguardandoRespostaAluno(), getSituacaoAtividadeDiscursivaAvaliada(), getAtividadeDiscursivaVO().getCodigo(), getSituacaoDuvidaTutorAguardandoRespostaProfessor(), getSituacaoDuvidaTutorAguardandoRespostaAluno(), getTutor().getPessoa().getCodigo(), getPercentualInicio(), getPercentualFim(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarTotalResgistro(getTipoNivelProgramacaoTutoria(), getTurmaVO(), getUnidadeEnsinoVO().getCodigo(), getDisciplinaVO().getCodigo(), getCursoVO().getCodigo(), getUsuarioLogado().getPessoa().getCodigo(), getEstudando(), getConcluiram(), getIsApresentarAno(), getAno(), getSemestre(), getTemaAssuntoVO().getCodigo(), getItemParametrosMonitoramentoAvaliacaoOnlineVO().getCodigo(), getPeriodicidade(), getDataInicio(), getDataFim(), getFiltroRelatorioAcademicoVO(), getMatriculaVO(), getSituacaoAvaliacaoOnlineAprovado(), getSituacaoAvaliacaoOnlineReprovado(), getSituacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado(),  getSituacaoAvaliacaoOnlineAguardandoExecucao(), getSituacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada(), getAvaliacaoOnlineVO().getCodigo(), getSituacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor(), getSituacaoAtividadeDiscursivaAguardandoRespostaAluno(), getSituacaoAtividadeDiscursivaAvaliada(), getAtividadeDiscursivaVO().getCodigo(), getSituacaoDuvidaTutorAguardandoRespostaProfessor(), getSituacaoDuvidaTutorAguardandoRespostaAluno(), getTutor().getPessoa().getCodigo(), getPercentualInicio(), getPercentualFim(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public void montarComboBoxFiltroConsulta() {
		getListaSelectItemNivelProgramacaoTutoria().clear();
		getListaSelectItemNivelProgramacaoTutoria().add(new SelectItem(TipoNivelProgramacaoTutoriaEnum.DISCIPLINA.getName(), TipoNivelProgramacaoTutoriaEnum.DISCIPLINA.getValorApresentar()));
		getListaSelectItemNivelProgramacaoTutoria().add(new SelectItem(TipoNivelProgramacaoTutoriaEnum.TURMA.getName(), TipoNivelProgramacaoTutoriaEnum.TURMA.getValorApresentar()));
		getListaSelectItemNivelProgramacaoTutoria().add(new SelectItem(TipoNivelProgramacaoTutoriaEnum.CURSO.getName(), TipoNivelProgramacaoTutoriaEnum.CURSO.getValorApresentar()));
		getListaSelectItemNivelProgramacaoTutoria().add(new SelectItem(TipoNivelProgramacaoTutoriaEnum.UNIDADE_ENSINO.getName(), TipoNivelProgramacaoTutoriaEnum.UNIDADE_ENSINO.getValorApresentar()));
	}

	public void consultarTurma() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				setListaConsultaTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
				setListaConsultaTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				setListaConsultaTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsulta().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, getUsuarioLogado());
			setTurmaVO(obj);
			montarListaDisciplinas();
			montarListaSelectItemNivel();
			getControleConsulta().getListaConsulta().clear();
			getPeriodicidade().setValor(getTurmaVO().getPeriodicidade());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaDisciplinas() {
		try {
			List<DisciplinaVO> resultado = consultarDisciplina(getCursoVO(), getTurmaVO(), null, null);
			setListaSelectItemDisciplinas(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList<SelectItem>(0));
		}
	}

	public List<DisciplinaVO> consultarDisciplina(CursoVO cursoVO, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, String nivelEducacional) throws Exception {
		if (getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.CURSO)) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorCurso(cursoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else if (getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.TURMA)) {
			return getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(turmaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return new ArrayList<DisciplinaVO>();
	}

	public void consultarDadosCurso() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				setListaConsultaCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nrRegistroInterno")) {
				setListaConsultaCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				setListaConsultaCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
				setListaConsultaCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				setListaConsultaCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsultaCursoVOs(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			setCursoVO(obj);			
			montarListaDisciplinas();
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getCursoVO().getPeriodicidade()));
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarTurma() throws Exception {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getTurmaVO().getPeriodicidade()));
			montarListaDisciplinas();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			removerObjetoMemoria(getTurmaVO());
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
		}
	}

	public void montarListaSelectItemTemasAssuntosDisciplina() {
		try {
			setListaSelectItemTemasAssuntosDisciplina(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTemaAssuntoFacade().consultarTemaAssuntoPorCodigoDisciplina(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemNivel() {
		try {
			List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> itemParametrosMonitoramentoAvaliacaoOnlineVOs = getFacadeFactory().getItemParametrosMonitoramentoAvaliacaoOnlineFacade().consultarItemParametrosMonitoramentoAvalaicaoOnlinePorTurmaAnoSemestre(getTurmaVO().getCodigo(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemNivel().add(new SelectItem(0, ""));
			for (ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO : itemParametrosMonitoramentoAvaliacaoOnlineVOs) {
				getListaSelectItemNivel().add(new SelectItem(itemParametrosMonitoramentoAvaliacaoOnlineVO.getCodigo(), itemParametrosMonitoramentoAvaliacaoOnlineVO.getDescricaoParametro()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Getters and Setters
	public List<SelectItem> getListaSelectItemNivelProgramacaoTutoria() {
		if (listaSelectItemNivelProgramacaoTutoria == null) {
			listaSelectItemNivelProgramacaoTutoria = new ArrayList<SelectItem>();
		}
		return listaSelectItemNivelProgramacaoTutoria;
	}

	public void setListaSelectItemNivelProgramacaoTutoria(List<SelectItem> listaSelectItemNivelProgramacaoTutoria) {
		this.listaSelectItemNivelProgramacaoTutoria = listaSelectItemNivelProgramacaoTutoria;
	}

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}
	
	public void montarListaSelectItemDisciplinasProfessor() {
		try {
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void montarListaTurmasVisaoCoodernador() {
		try {
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(), false, false, false, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()), "codigo", "identificadorTurma", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = false;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
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

	public TipoNivelProgramacaoTutoriaEnum getTipoNivelProgramacaoTutoria() {
		if (tipoNivelProgramacaoTutoria == null) {
			tipoNivelProgramacaoTutoria = TipoNivelProgramacaoTutoriaEnum.DISCIPLINA;
		}
		return tipoNivelProgramacaoTutoria;
	}

	public void setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria) {
		this.tipoNivelProgramacaoTutoria = tipoNivelProgramacaoTutoria;
	}

	public boolean getIsApresentarCampoTurma() {
		return getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.TURMA);
	}

	public boolean getIsApresentarCampoCurso() {
		return getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.CURSO);
	}

	public boolean getIsApresentarCampoUnidadeEnsino() {
		return getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.UNIDADE_ENSINO);
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public List<SelectItem> getListaSelectItemCursos() {
		if (listaSelectItemCursos == null) {
			listaSelectItemCursos = new ArrayList<SelectItem>();
		}
		return listaSelectItemCursos;
	}

	public void setListaSelectItemCursos(List<SelectItem> listaSelectItemCursos) {
		this.listaSelectItemCursos = listaSelectItemCursos;
	}

	public Boolean getEstudando() {
		if (estudando == null) {
			estudando = true;
		}
		return estudando;
	}

	public void setEstudando(Boolean estudando) {
		this.estudando = estudando;
	}

	public Boolean getConcluiram() {
		if (concluiram == null) {
			concluiram = false;
		}
		return concluiram;
	}

	public void setConcluiram(Boolean concluiram) {
		this.concluiram = concluiram;
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
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<CalendarioAtividadeMatriculaVO> getCalendarioAtividadeMatriculaVOs() {
		if (calendarioAtividadeMatriculaVOs == null) {
			calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		}
		return calendarioAtividadeMatriculaVOs;
	}

	public void setCalendarioAtividadeMatriculaVOs(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs) {
		this.calendarioAtividadeMatriculaVOs = calendarioAtividadeMatriculaVOs;
	}

	public Boolean getMostrarGabarito() {
		if (mostrarGabarito == null) {
			mostrarGabarito = false;
		}
		return mostrarGabarito;
	}

	public void setMostrarGabarito(Boolean mostrarGabarito) {
		this.mostrarGabarito = mostrarGabarito;
	}

	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if (avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public Integer getTotalQuestoes() {
		if (totalQuestoes == null) {
			totalQuestoes = 0;
		}
		return totalQuestoes;
	}

	public void setTotalQuestoes(Integer totalQuestoes) {
		this.totalQuestoes = totalQuestoes;
	}

	public List<AtividadeDiscursivaRespostaAlunoVO> getAtividadeDiscursivaRespostaAlunoVO() {
		if (atividadeDiscursivaRespostaAlunoVO == null) {
			atividadeDiscursivaRespostaAlunoVO = new ArrayList<AtividadeDiscursivaRespostaAlunoVO>();
		}
		return atividadeDiscursivaRespostaAlunoVO;
	}

	public void setAtividadeDiscursivaRespostaAlunoVO(List<AtividadeDiscursivaRespostaAlunoVO> atividadeDiscursivaRespostaAlunoVO) {
		this.atividadeDiscursivaRespostaAlunoVO = atividadeDiscursivaRespostaAlunoVO;
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
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>();
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemDisciplinas() {
		if (listaSelectItemDisciplinas == null) {
			listaSelectItemDisciplinas = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List<SelectItem> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nrRegistroInterno", "Número (Registro Interno)"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
		itens.add(new SelectItem("nivelEducacional", "Nível Educacional"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public Boolean getIsApresentarCampoDisciplina() {
		return getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.TURMA) || getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.CURSO);
	}

	public List<TurmaVO> getListaConsultaTurmaVOs() {
		if (listaConsultaTurmaVOs == null) {
			listaConsultaTurmaVOs = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurmaVOs;
	}

	public void setListaConsultaTurmaVOs(List<TurmaVO> listaConsultaTurmaVOs) {
		this.listaConsultaTurmaVOs = listaConsultaTurmaVOs;
	}

	public List<CursoVO> getListaConsultaCursoVOs() {
		if (listaConsultaCursoVOs == null) {
			listaConsultaCursoVOs = new ArrayList<CursoVO>();
		}
		return listaConsultaCursoVOs;
	}

	public void setListaConsultaCursoVOs(List<CursoVO> listaConsultaCursoVOs) {
		this.listaConsultaCursoVOs = listaConsultaCursoVOs;
	}

	public boolean getIsApresentarAno() {
		if (!getCursoVO().getCodigo().equals(0)) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return true;
			} else if (getCursoVO().getPeriodicidade().equals("AN")) {
				return true;
			}
		} else if (!getTurmaVO().getCodigo().equals(0)) {
			if (getTurmaVO().getSemestral()) {
				return true;
			} else if (getTurmaVO().getAnual()) {
				return true;
			}
		} else if(!getDisciplinaVO().getCodigo().equals(0) && getCursoVO().getCodigo().equals(0) && getTurmaVO().getCodigo().equals(0)) {
			if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL) || getPeriodicidade().equals(PeriodicidadeEnum.ANUAL)) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (!getCursoVO().getCodigo().equals(0)) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return true;
			}
		} else if (!getTurmaVO().getCodigo().equals(0)) {
			if (getTurmaVO().getSemestral()) {
				return true;
			}
		}else if(!getDisciplinaVO().getCodigo().equals(0) && getCursoVO().getCodigo().equals(0) && getTurmaVO().getCodigo().equals(0)) {
			if(getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL)) {
				return true;
			}
		}
		return false;
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

	public List<SelectItem> getCampoBoxSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public String getTipoNivelConhecimento() {
		if (tipoNivelConhecimento == null) {
			if (getTemaAssuntoVO().getCodigo().equals(0)) {
				tipoNivelConhecimento = "Por Disciplina";
			} else {
				tipoNivelConhecimento = "Por Assunto";
			}
		}
		return tipoNivelConhecimento;
	}

	public void setTipoNivelConhecimento(String tipoNivelConhecimento) {
		this.tipoNivelConhecimento = tipoNivelConhecimento;
	}

	public List<SelectItem> getListaSelectItemTemasAssuntosDisciplina() {
		if (listaSelectItemTemasAssuntosDisciplina == null) {
			listaSelectItemTemasAssuntosDisciplina = new ArrayList<SelectItem>();
		}
		return listaSelectItemTemasAssuntosDisciplina;
	}

	public void setListaSelectItemTemasAssuntosDisciplina(List<SelectItem> listaSelectItemTemasAssuntosDisciplina) {
		this.listaSelectItemTemasAssuntosDisciplina = listaSelectItemTemasAssuntosDisciplina;
	}

	public TemaAssuntoVO getTemaAssuntoVO() {
		if (temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}

	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}

	public MonitorConhecimentoVO getMonitorConhecimentoVO() {
		if (monitorConhecimentoVO == null) {
			monitorConhecimentoVO = new MonitorConhecimentoVO();
		}
		return monitorConhecimentoVO;
	}

	public void setMonitorConhecimentoVO(MonitorConhecimentoVO monitorConhecimentoVO) {
		this.monitorConhecimentoVO = monitorConhecimentoVO;
	}

	public CalendarioAtividadeMatriculaVO getCalendarioAtividadeMatriculaVO() {
		if (calendarioAtividadeMatriculaVO == null) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAtividadeMatriculaVO;
	}

	public void setCalendarioAtividadeMatriculaVO(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		this.calendarioAtividadeMatriculaVO = calendarioAtividadeMatriculaVO;
	}

	public List<SelectItem> getListaSelectItemNivel() {
		if (listaSelectItemNivel == null) {
			listaSelectItemNivel = new ArrayList<SelectItem>();
		}
		return listaSelectItemNivel;
	}

	public void setListaSelectItemNivel(List<SelectItem> listaSelectItemNivel) {
		this.listaSelectItemNivel = listaSelectItemNivel;
	}

	public ItemParametrosMonitoramentoAvaliacaoOnlineVO getItemParametrosMonitoramentoAvaliacaoOnlineVO() {
		if (itemParametrosMonitoramentoAvaliacaoOnlineVO == null) {
			itemParametrosMonitoramentoAvaliacaoOnlineVO = new ItemParametrosMonitoramentoAvaliacaoOnlineVO();
		}
		return itemParametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public void setItemParametrosMonitoramentoAvaliacaoOnlineVO(ItemParametrosMonitoramentoAvaliacaoOnlineVO itemParametrosMonitoramentoAvaliacaoOnlineVO) {
		this.itemParametrosMonitoramentoAvaliacaoOnlineVO = itemParametrosMonitoramentoAvaliacaoOnlineVO;
	}

	public Boolean getSelecionarAtividade() {
		if (selecionarAtividade == null) {
			selecionarAtividade = false;
		}
		return selecionarAtividade;
	}

	public void setSelecionarAtividade(Boolean selecionarAtividade) {
		this.selecionarAtividade = selecionarAtividade;
	}

	public Integer getNrAtividadeSelecionadas() {
		if (nrAtividadeSelecionadas == null) {
			nrAtividadeSelecionadas = 0;
		}
		return nrAtividadeSelecionadas;
	}

	public void setNrAtividadeSelecionadas(Integer nrAtividadeSelecionadas) {
		this.nrAtividadeSelecionadas = nrAtividadeSelecionadas;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}
	
	public boolean isNavegarGestaoEventoMonitoramentoEad() {
		return navegarGestaoEventoMonitoramentoEad;
	}

	public void setNavegarGestaoEventoMonitoramentoEad(boolean navegarGestaoEventoMonitoramentoEad) {
		this.navegarGestaoEventoMonitoramentoEad = navegarGestaoEventoMonitoramentoEad;
	}
	
	

	public ConteudoVO getConteudoVO() {
		if(conteudoVO == null){
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}
	
	public void montarGraficoLinhaEvolucaoAluno() {
		try {
			StringBuilder series = new StringBuilder();
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosItens");
			List<ConteudoRegistroAcessoVO> conteudoRegistroAcessoVOs = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarDataAcessoPontosTotalAcumuladoConteudo(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
			series.append("[{ name: 'Evolução Dos Estudos', pointInterval: 30* 24 * 3600 * 1000, data: [");

			Boolean virgula = false;
			for (ConteudoRegistroAcessoVO conteudoRegistroAcessoVO : conteudoRegistroAcessoVOs) {
				if (virgula) {
					series.append(", ");
				}
				Calendar cal = new GregorianCalendar();
				cal.setTime(conteudoRegistroAcessoVO.getDataAcesso());

				series.append("[Date.UTC(").append(cal.get(Calendar.YEAR) + ", ").append(cal.get(Calendar.MONTH) + ", ").append(cal.get(Calendar.DAY_OF_MONTH) + "), ").append(conteudoRegistroAcessoVO.getTotalAcumulado());
				virgula = true;
				series.append("]");
			}
			series.append(" ]}, ");

			series.append(" { name: 'Pontos Por Dia', pointInterval: 30* 24 * 3600 * 1000, data: [");
			virgula = false;
			for (ConteudoRegistroAcessoVO conteudoRegistroAcessoVO : conteudoRegistroAcessoVOs) {
				if (virgula) {
					series.append(", ");
				}
				Calendar cal = new GregorianCalendar();
				cal.setTime(conteudoRegistroAcessoVO.getDataAcesso());

				series.append("[Date.UTC(").append(cal.get(Calendar.YEAR) + ", ").append(cal.get(Calendar.MONTH) + ", ").append(cal.get(Calendar.DAY_OF_MONTH) + "), ").append(conteudoRegistroAcessoVO.getPonto());
				virgula = true;
				series.append("]");
			}
			series.append("]}]");
			context().getExternalContext().getSessionMap().put("series", series.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSeriesGraficoLinhaEvolucaoAluno() {
		return (String) context().getExternalContext().getSessionMap().get("series");
	}
	
	public void montarObjetosPorValorFiltroCampoSelecionado() {
		limparDadosCurso();
		limparDadosTurma();
		limparDadosUnidadeEnsino();
		setAno("");
		setSemestre("");
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			if (getIsApresentarCampoTurma()) {
				montarListaSelectItemTurmasProfessor();
			} else if (getIsApresentarCampoCurso()) {
				montarListaSelectItemCursosProfessor();
			} else if (getIsApresentarCampoUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsinoProfessor();
			} else {
				montarListaSelectItemDisciplinasProfessor();
			}
		} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			if (getIsApresentarCampoUnidadeEnsino()) {
				montarListaSelectItemUnidadesEnsinoVisaoAdministrativa();
			}
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {

		}
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public void limparDadosTurma() {
		removerObjetoMemoria(getTurmaVO());
		getListaSelectItemDisciplinas().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public void limparDadosCurso() {
		removerObjetoMemoria(getCursoVO());
		getListaSelectItemDisciplinas().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public void limparDadosUnidadeEnsino() {
		removerObjetoMemoria(getUnidadeEnsinoVO());
		getListaSelectItemDisciplinas().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();
	}

	public void montarListaSelectItemTurmasProfessor() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}
			getListaSelectItemDisciplinasTurma().clear();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			mapAuxiliarSelectItem = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<TurmaVO>();
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getTurmaVO().getPeriodicidade()));
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", true));
			montarListaSelectItemNivel();
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public void montarListaDisciplinaTurmaVisaoCoodernador() {
		try {
			if (!getTurmaVO().getCodigo().equals(0)) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getTurmaVO().getPeriodicidade()));
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome", true));
				montarListaSelectItemNivel();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() {
		try {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, getTurmaVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogado());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<DisciplinaVO>();
	}

	public void montarListaSelectItemCursosProfessor() {
		try {
			setListaSelectItemCursos(UtilSelectItem.getListaSelectItem(getFacadeFactory().getCursoFacade().consultaCursoDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()), "codigo", "nome", true));
			getListaSelectItemDisciplinasTurma().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemDisciplinasCursoProfessor() {
		try {
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getCursoVO().getCodigo(), null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome", true));
			if(Uteis.isAtributoPreenchido(getCursoVO())) {
				setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
				setPeriodicidade(PeriodicidadeEnum.getEnumPorValor(getCursoVO().getPeriodicidade()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemUnidadeEnsinoProfessor() {
		try {
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome", true));
			getListaSelectItemDisciplinasTurma().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemDisciplinasUnidadeEnsinoProfessor() {
		try {
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void montarListaSelectItemUnidadesEnsinoVisaoAdministrativa() {
		try {
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String consultarDuvidasTutor() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("duvidaProfessorCons.xhtml");
	}

	public void consultarCalendarioAtividadesMatriculaPeriodoTurmaDisciplina() {
		try {
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosItens");
			setCalendarioAtividadeMatriculaVO(new CalendarioAtividadeMatriculaVO());
			setCalendarioAtividadeMatriculaVO(calendarioAtividadeMatriculaVO);
			setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendarioAtividadeMatriculaVisaoAluno(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void visualizarGabarito() {
		try {
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("avaliacaoOnlineItem");
			setAvaliacaoOnlineMatriculaVO(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO());
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVisualizacaoGabarito(getAvaliacaoOnlineMatriculaVO(), getUsuarioLogado()));
			setTotalQuestoes(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size());
			setMostrarGabarito(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAtividadesDiscursivasAluno() {
		try {
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("alunosItens");
			setAtividadeDiscursivaRespostaAlunoVO(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado(), "", ""));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void carregarDadosAtividadesDiscursivasVisaoProfessor() {
		try {
			setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum.DISCIPLINA);
			AtividadeDiscursivaRespostaAlunoVO adra = ((AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getSessionMap().get("ativiadesDiscursivas"));
			getDisciplinaVO().setCodigo(adra.getAtividadeDiscursivaVO().getDisciplinaVO().getCodigo());
			setEstudando(true);
			context().getExternalContext().getSessionMap().remove("navegarProfessorAtividadeDiscursivaMonitoramentoEad");
			context().getExternalContext().getSessionMap().remove("ativiadesDiscursivas");
			setControleConsultaOtimizado(new DataModelo());
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			consultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public String navegarAtividadeDiscursivaProfessorAluno() {
		try {
			AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO = (AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getRequestMap().get("ativiadesDiscursivasItens");
			if(!Uteis.isAtributoPreenchido(atividadeDiscursivaRespostaAlunoVO.getCodigo())){
				throw new Exception("Não foi encontrado nenhuma atividade discursiva para esse aluno.");
			}
			context().getExternalContext().getSessionMap().put("ativiadesDiscursivas", atividadeDiscursivaRespostaAlunoVO);
			context().getExternalContext().getSessionMap().put("navegarProfessorAtividadeDiscursivaMonitoramentoEad", true);
			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoProfessor/atividadeDiscursivaProfessorAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return ""; 
		
	}
	
	public String visualizarAtividadeDiscursivaAluno() {
		AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO = (AtividadeDiscursivaRespostaAlunoVO) context().getExternalContext().getRequestMap().get("ativiadesDiscursivasItens");
		context().getExternalContext().getSessionMap().put("ativiadesDiscursivas", atividadeDiscursivaRespostaAlunoVO);
		context().getExternalContext().getSessionMap().put("booleanoConsultarAtividadeDiscursivaRespostaAluno", true);
		return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoProfessor/atividadeDiscursivaProfessorAlunoForm.xhtml");
	}

	public void consultarDisciplina() {
		try {
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getIsApresentarCampoUnidadeEnsino()) {
				consultarDisciplinaPorUnidadeEnsino();
			} else {
				List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
				if (getCampoConsultaDisciplina().equals("codigo")) {
					int valorInt = Integer.parseInt(getValorConsultaDisciplina());

					DisciplinaVO disciplina = new DisciplinaVO();
					disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
						objs.add(disciplina);
					}
				}
				if (getCampoConsultaDisciplina().equals("nome")) {
					objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				setListaConsultaDisciplina(objs);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDisciplinaPorUnidadeEnsino() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(valorInt, getUnidadeEnsinoVO().getCodigo(), 0, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoTurmaAgrupada(getValorConsultaDisciplina(), getUnidadeEnsinoVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimentoUnidadeEnsino(getValorConsultaDisciplina(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (objs.isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setListaConsultaDisciplina(objs);
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			setDisciplinaVO(disciplina);
			montarListaSelectItemTemasAssuntosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarDisciplinaAdministrativo() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			setDisciplinaVO(disciplina);
			montarListaSelectItemTemasAssuntosDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparDisciplina() {
		setDisciplinaVO(null);
		setCalendarioAtividadeMatriculaVOs(null);
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparMensagem();
	}
	
	public boolean getApresentarGrafico() {
		if (getControleConsultaOtimizado().getListaConsulta().size() > 0) {
			return true;
		} else
			return false;
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
	
	public Date getDataInicio() {
		if (dataInicio == null) {
//			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
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
	
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(
					getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO,
					getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(objAluno.getMatricula(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
	}
	
	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}
	
	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}
	
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);

				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
							this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);

			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
		selecionarAluno(obj);
	}

	public void selecionarAluno(MatriculaVO obj) {
		try {
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(),
					obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(obj);

			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false,
							Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula()
						+ " não encontrado. Verifique se o número de matrícula está correto.");
			}
			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Boolean getSituacaoAvaliacaoOnlineAprovado() {
		if(situacaoAvaliacaoOnlineAprovado == null) {
			situacaoAvaliacaoOnlineAprovado = Boolean.FALSE;
		}
		return situacaoAvaliacaoOnlineAprovado;
	}

	public void setSituacaoAvaliacaoOnlineAprovado(Boolean situacaoAvaliacaoOnlineAprovado) {
		this.situacaoAvaliacaoOnlineAprovado = situacaoAvaliacaoOnlineAprovado;
	}

	public Boolean getSituacaoAvaliacaoOnlineReprovado() {
		if(situacaoAvaliacaoOnlineReprovado == null) {
			situacaoAvaliacaoOnlineReprovado = Boolean.FALSE;
		}
		return situacaoAvaliacaoOnlineReprovado;
	}

	public void setSituacaoAvaliacaoOnlineReprovado(Boolean situacaoAvaliacaoOnlineReprovado) {
		this.situacaoAvaliacaoOnlineReprovado = situacaoAvaliacaoOnlineReprovado;
	}

	public Boolean getSituacaoAvaliacaoOnlineAguardandoExecucao() {
		if(situacaoAvaliacaoOnlineAguardandoExecucao == null) {
			situacaoAvaliacaoOnlineAguardandoExecucao = Boolean.FALSE;
		}
		return situacaoAvaliacaoOnlineAguardandoExecucao;
	}

	public void setSituacaoAvaliacaoOnlineAguardandoExecucao(Boolean situacaoAvaliacaoOnlineAguardandoExecucao) {
		this.situacaoAvaliacaoOnlineAguardandoExecucao = situacaoAvaliacaoOnlineAguardandoExecucao;
	}

	public Boolean getSituacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado() {
		if(situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado == null) {
			situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado = Boolean.FALSE;
		}
		return situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado;
	}

	public void setSituacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado(
			Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado) {
		this.situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado = situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado;
	}

	public Boolean getSituacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada() {
		if(situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada == null) {
			situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada = Boolean.FALSE;
		}
		return situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada;
	}

	public void setSituacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada(
			Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada) {
		this.situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada = situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada;
	}

	public Boolean getSituacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor() {
		if(situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor == null) {
			situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor = Boolean.FALSE;
		}
		return situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor;
	}

	public void setSituacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor(
			Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor) {
		this.situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor = situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor;
	}

	public Boolean getSituacaoAtividadeDiscursivaAguardandoRespostaAluno() {
		if(situacaoAtividadeDiscursivaAguardandoRespostaAluno == null) {
			situacaoAtividadeDiscursivaAguardandoRespostaAluno = Boolean.FALSE;
		}
		return situacaoAtividadeDiscursivaAguardandoRespostaAluno;
	}

	public void setSituacaoAtividadeDiscursivaAguardandoRespostaAluno(
			Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno) {
		this.situacaoAtividadeDiscursivaAguardandoRespostaAluno = situacaoAtividadeDiscursivaAguardandoRespostaAluno;
	}

	public Boolean getSituacaoAtividadeDiscursivaAvaliada() {
		if(situacaoAtividadeDiscursivaAvaliada == null) {
			situacaoAtividadeDiscursivaAvaliada = Boolean.FALSE;
		}
		return situacaoAtividadeDiscursivaAvaliada;
	}

	public void setSituacaoAtividadeDiscursivaAvaliada(Boolean situacaoAtividadeDiscursivaAvaliada) {
		this.situacaoAtividadeDiscursivaAvaliada = situacaoAtividadeDiscursivaAvaliada;
	}

	public Boolean getSituacaoDuvidaTutorAguardandoRespostaProfessor() {
		if(situacaoDuvidaTutorAguardandoRespostaProfessor == null) {
			situacaoDuvidaTutorAguardandoRespostaProfessor = Boolean.FALSE;
		}
		return situacaoDuvidaTutorAguardandoRespostaProfessor;
	}

	public void setSituacaoDuvidaTutorAguardandoRespostaProfessor(Boolean situacaoDuvidaTutorAguardandoRespostaProfessor) {
		this.situacaoDuvidaTutorAguardandoRespostaProfessor = situacaoDuvidaTutorAguardandoRespostaProfessor;
	}

	public Boolean getSituacaoDuvidaTutorAguardandoRespostaAluno() {
		if(situacaoDuvidaTutorAguardandoRespostaAluno == null) {
			situacaoDuvidaTutorAguardandoRespostaAluno = Boolean.FALSE;
		}
		return situacaoDuvidaTutorAguardandoRespostaAluno;
	}

	public void setSituacaoDuvidaTutorAguardandoRespostaAluno(Boolean situacaoDuvidaTutorAguardandoRespostaAluno) {
		this.situacaoDuvidaTutorAguardandoRespostaAluno = situacaoDuvidaTutorAguardandoRespostaAluno;
	}

	public Double getPercentualInicio() {
		return percentualInicio;
	}

	public void setPercentualInicio(Double percentualInicio) {
		this.percentualInicio = percentualInicio;
	}

	public Double getPercentualFim() {
		return percentualFim;
	}

	public void setPercentualFim(Double percentualFim) {
		this.percentualFim = percentualFim;
	}

	public String getCampoConsultaAvaliacaoOnlineRea() {
		if(campoConsultaAvaliacaoOnlineRea == null) {
			campoConsultaAvaliacaoOnlineRea = "";
		}
		return campoConsultaAvaliacaoOnlineRea;
	}

	public void setCampoConsultaAvaliacaoOnlineRea(String campoConsultaAvaliacaoOnlineRea) {
		this.campoConsultaAvaliacaoOnlineRea = campoConsultaAvaliacaoOnlineRea;
	}

	public String getValorConsultaAvaliacaoOnlineRea() {
		if(valorConsultaAvaliacaoOnlineRea == null) {
			valorConsultaAvaliacaoOnlineRea = "";
		}
		return valorConsultaAvaliacaoOnlineRea;
	}

	public void setValorConsultaAvaliacaoOnlineRea(String valorConsultaAvaliacaoOnlineRea) {
		this.valorConsultaAvaliacaoOnlineRea = valorConsultaAvaliacaoOnlineRea;
	}

	public List<AvaliacaoOnlineVO> getListaConsultaAvaliacaoOnlineRea() {
		if(listaConsultaAvaliacaoOnlineRea == null) {
			listaConsultaAvaliacaoOnlineRea = new ArrayList<AvaliacaoOnlineVO>(0);
		}
		return listaConsultaAvaliacaoOnlineRea;
	}

	public void setListaConsultaAvaliacaoOnlineRea(List<AvaliacaoOnlineVO> listaConsultaAvaliacaoOnlineRea) {
		this.listaConsultaAvaliacaoOnlineRea = listaConsultaAvaliacaoOnlineRea;
	}	
	
	public void consultarAvaliacaoOnlineRea() {
		try {
			if (getValorConsultaAvaliacaoOnlineRea().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}

			List<AvaliacaoOnlineVO> objs = new ArrayList<AvaliacaoOnlineVO>(0);
			if (getCampoConsultaAvaliacaoOnlineRea().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaAvaliacaoOnlineRea());

				AvaliacaoOnlineVO avaliacaoOnline = new AvaliacaoOnlineVO();
				avaliacaoOnline = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorCodigoAvaliacaoCodigoDisciplina(new Integer(valorInt), getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!avaliacaoOnline.equals(new AvaliacaoOnlineVO()) || avaliacaoOnline != null) {
					objs.add(avaliacaoOnline);
				}
			}
			if (getCampoConsultaAvaliacaoOnlineRea().equals("nome")) {
				objs = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlinePorNomeAvaliacaoDisciplina(getValorConsultaAvaliacaoOnlineRea(), getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaAvaliacaoOnlineRea(objs);
		
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAvaliacaoOnlineRea(new ArrayList<AvaliacaoOnlineVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getTipoConsultaComboAvaliacaoOnlineRea() {
		if (tipoConsultaComboAvaliacaoOnlineRea == null) {
			tipoConsultaComboAvaliacaoOnlineRea = new ArrayList<SelectItem>(0);
			tipoConsultaComboAvaliacaoOnlineRea.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboAvaliacaoOnlineRea.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboAvaliacaoOnlineRea;
	}
	
	public void limparAvaliacaoOnlineRea() {
		setAvaliacaoOnlineVO(null);
		setCalendarioAtividadeMatriculaVOs(null);
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparMensagem();
	}

	public AvaliacaoOnlineVO getAvaliacaoOnlineVO() {
		if(avaliacaoOnlineVO == null) {
			avaliacaoOnlineVO = new AvaliacaoOnlineVO();
		}
		return avaliacaoOnlineVO;
	}

	public void setAvaliacaoOnlineVO(AvaliacaoOnlineVO avaliacaoOnlineVO) {
		this.avaliacaoOnlineVO = avaliacaoOnlineVO;
	}
	
	public void selecionarAvaliacaoOnlineRea() throws Exception {
		try {
			AvaliacaoOnlineVO avaliacaoOnlineRea = (AvaliacaoOnlineVO) context().getExternalContext().getRequestMap().get("avaliacaoOnlineReaItem");
			setAvaliacaoOnlineVO(avaliacaoOnlineRea);
			if(!Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				setDisciplinaVO(avaliacaoOnlineRea.getDisciplinaVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCampoConsultaAtividadeDiscursiva() {
		if(campoConsultaAtividadeDiscursiva == null) {
			campoConsultaAtividadeDiscursiva = "";
		}
		return campoConsultaAtividadeDiscursiva;
	}

	public void setCampoConsultaAtividadeDiscursiva(String campoConsultaAtividadeDiscursiva) {
		this.campoConsultaAtividadeDiscursiva = campoConsultaAtividadeDiscursiva;
	}

	public String getValorConsultaAtividadeDiscursiva() {
		if(valorConsultaAtividadeDiscursiva == null) {
			valorConsultaAtividadeDiscursiva = "";
		}
		return valorConsultaAtividadeDiscursiva;
	}

	public void setValorConsultaAtividadeDiscursiva(String valorConsultaAtividadeDiscursiva) {
		this.valorConsultaAtividadeDiscursiva = valorConsultaAtividadeDiscursiva;
	}

	public List<AtividadeDiscursivaVO> getListaConsultaAtividadeDiscursiva() {
		if(listaConsultaAtividadeDiscursiva == null) {
			listaConsultaAtividadeDiscursiva = new ArrayList<AtividadeDiscursivaVO>(0);
		}
		return listaConsultaAtividadeDiscursiva;
	}

	public void setListaConsultaAtividadeDiscursiva(List<AtividadeDiscursivaVO> listaConsultaAtividadeDiscursiva) {
		this.listaConsultaAtividadeDiscursiva = listaConsultaAtividadeDiscursiva;
	}

	public AtividadeDiscursivaVO getAtividadeDiscursivaVO() {
		if(atividadeDiscursivaVO == null) {
			atividadeDiscursivaVO = new AtividadeDiscursivaVO();
		}
		return atividadeDiscursivaVO;
	}

	public void setAtividadeDiscursivaVO(AtividadeDiscursivaVO atividadeDiscursivaVO) {
		this.atividadeDiscursivaVO = atividadeDiscursivaVO;
	}

	public void consultarAtividadeDiscursiva() {
		try {
			if (getValorConsultaAtividadeDiscursiva().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}

			List<AtividadeDiscursivaVO> objs = new ArrayList<AtividadeDiscursivaVO>(0);
			
			if (getCampoConsultaAtividadeDiscursiva().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaAtividadeDiscursiva());

				AtividadeDiscursivaVO atividadeDiscursiva = new AtividadeDiscursivaVO();
				atividadeDiscursiva = getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorCodigoAtividadeDisciplina(new Integer(valorInt), getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!atividadeDiscursiva.equals(new AtividadeDiscursivaVO()) || atividadeDiscursiva != null) {
					objs.add(atividadeDiscursiva);
				}
			}

			if (getCampoConsultaAtividadeDiscursiva().equals("enunciado")) {
				objs = getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarAtividadeDiscursivaPorEnunciadoDisciplina(getValorConsultaAtividadeDiscursiva(), getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaAtividadeDiscursiva(objs);
		
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAtividadeDiscursiva(new ArrayList<AtividadeDiscursivaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getTipoConsultaComboAtividadeDiscursiva() {
		if (tipoConsultaComboAtividadediscursiva == null) {
			tipoConsultaComboAtividadediscursiva = new ArrayList<SelectItem>(0);
			tipoConsultaComboAtividadediscursiva.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboAtividadediscursiva.add(new SelectItem("enunciado", "Enunciado"));
		}
		return tipoConsultaComboAtividadediscursiva;
	}
	
	public void limparAtividadeDiscursiva() {
		setAtividadeDiscursivaVO(null);
		setCalendarioAtividadeMatriculaVOs(null);
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparMensagem();
	}
	
	public void selecionarAtividadeDiscursiva() throws Exception {
		try {
			AtividadeDiscursivaVO atividadeDiscursiva = (AtividadeDiscursivaVO) context().getExternalContext().getRequestMap().get("atividadeDiscursivaItem");
			setAtividadeDiscursivaVO(atividadeDiscursiva);
			if(!Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				setDisciplinaVO(atividadeDiscursiva.getDisciplinaVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCampoConsultaTutor() {
		if(campoConsultaTutor == null) {
			campoConsultaTutor = "";
		}
		return campoConsultaTutor;
	}

	public void setCampoConsultaTutor(String campoConsultaTutor) {
		this.campoConsultaTutor = campoConsultaTutor;
	}

	public String getValorConsultaTutor() {
		if(valorConsultaTutor == null) {
			valorConsultaTutor = "";
		}
		return valorConsultaTutor;
	}

	public void setValorConsultaTutor(String valorConsultaTutor) {
		this.valorConsultaTutor = valorConsultaTutor;
	}

	public List<FuncionarioVO> getListaConsultaTutor() {
		if(listaConsultaTutor == null) {
			listaConsultaTutor =  new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaTutor;
	}

	public void setListaConsultaTutor(List<FuncionarioVO> listaConsultaTutor) {
		this.listaConsultaTutor = listaConsultaTutor;
	}

	public FuncionarioVO getTutor() {
		if(tutor == null) {
			tutor = new FuncionarioVO();
		}
		return tutor;
	}

	public void setTutor(FuncionarioVO tutor) {
		this.tutor = tutor;
	}
		
	public List<SelectItem> getTipoConsultaComboTutor() {
		if (tipoConsultaComboTutor == null) {
			tipoConsultaComboTutor = new ArrayList<SelectItem>(0);
			tipoConsultaComboTutor.add(new SelectItem("codigo", "Código"));
			tipoConsultaComboTutor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboTutor.add(new SelectItem("matricula", "Matricula"));
		}
		return tipoConsultaComboTutor;
	}
	
	public void limparTutor() {
		setTutor(null);
		setCalendarioAtividadeMatriculaVOs(null);
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparMensagem();
	}
	
	public void selecionarTutor() throws Exception {
		try {
			FuncionarioVO tutor = (FuncionarioVO) context().getExternalContext().getRequestMap().get("tutorItem");
			setTutor(tutor);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarTutor() {
		try {
			List objs = new ArrayList(0);
			
			if (getCampoConsultaTutor().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaTutor());

				FuncionarioVO funcionarioVO = new FuncionarioVO();
				funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(new Integer(valorInt), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!funcionarioVO.equals(new FuncionarioVO()) || funcionarioVO != null) {
					objs.add(funcionarioVO);
				}
			}
			
			if (getCampoConsultaTutor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaTutor(), "PR", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTutor().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaTutor(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTutor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTutor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private Boolean versaoNova;
	private Boolean versaoAntiga;
	
	public Boolean getVersaoNova() {
		if (versaoNova == null) {
			versaoNova = false;
		}
		return versaoNova;
	}
	
	public void setVersaoNova(Boolean versaoNova) {
		this.versaoNova = versaoNova;
	}
	
	public Boolean getVersaoAntiga() {
		if (versaoAntiga == null) {
			versaoAntiga = false;
		}
		return versaoAntiga;
	}
	
	public void setVersaoAntiga(Boolean versaoAntiga) {
		this.versaoAntiga = versaoAntiga;
	}
	
	public void mudarLayoutConsulta() {
		setVersaoAntiga(true);
		setVersaoNova(false);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), MonitoramentoAlunosEAD.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mudarLayoutConsulta2() {
		setVersaoAntiga(false);
		setVersaoNova(true);
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getVersaoNova().toString(), MonitoramentoAlunosEAD.class.getName()+"_"+getUsuarioLogado().getCodigo(), "VersaoNova", getUsuarioLogado());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}