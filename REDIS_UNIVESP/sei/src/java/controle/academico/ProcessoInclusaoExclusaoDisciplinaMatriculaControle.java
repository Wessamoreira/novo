package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaComHistoricoAlunoVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoDescontoInclusaoDisciplinaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.financeiro.PlanoFinanceiroReposicaoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.InclusaoDisciplinasForaDoPrazo;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.academico.Turma;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ProcessoInclusaoExclusaoDisciplinaMatriculaControle")
@Scope("viewScope")
@Lazy
public class ProcessoInclusaoExclusaoDisciplinaMatriculaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVO;
	private Integer peridoLetivo;
	private Integer gradeDisciplina;
	private TurmaVO turma;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasSemVagas;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasComVagasUltrapassadas;
	private String userName;
	private String senha;
	private String matricula;
	private HorarioAlunoTurnoVO horarioAlunoTurnoVO;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadas;
	private Boolean imprimir;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina;
	private String semestre;
	private String ano;
	private List<SelectItem> listaPlanoDescontoInclusaoDisciplina;
	private List<SelectItem> listaPlanoFinanceiroReposicao;
	private Boolean disciplinaIncluida;
	private Boolean apresentarDisciplinasEquivalentesIncluir;
	private List<SelectItem> listaSelectItemDisciplinaEquivalenteAdicionar;
	private Integer disciplinaEquivalenteAdicionar;
	private List<SelectItem> listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
	private TurmaVO turmaIncluirMatriculaPeriodo;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	private Boolean enviarComunicadoPorEmail;
	private Boolean parcelaReposicaoNaoGravada;
	private List<SelectItem> listaSelectItemInclusaoDisciplinasForaDoPrazo;
	private String justificativa;
	private String observacaoJustificativa;
	private Boolean imprimirContrato;
	private List<SelectItem> listaSelectItemTextoPadraoContrato;
	private TextoPadraoVO textoPadraoContratoInclusao;
	private InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO;
	private Boolean apresentarPopUpReposicao;
	private String msgPopUpReposicao;
	private Boolean editandoInclusaoHistoricoForaPrazo;
	private Date dataInclusao;
	private UsuarioVO responsavel;
	private PeriodoLetivoVO periodoLetivoGrupoOptativaVO;
	private Integer codigoPeriodoLetivoGrupoOptativa;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	private List<SelectItem> listaSelectItemPeriodoLetivoComGrupoOptativas;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private Boolean panelAproveitamentoDisciplinaAberto;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar;
	private PeriodoLetivoVO periodoLetivoPrevistoMatricula;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar;
	private Integer codigoMapaEquivalenciaDisciplinaVOIncluir;
	private Integer codigoMapaEquivalenciaDisciplinaCursar;
	private List<MapaEquivalenciaDisciplinaVO> listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	private List<TurmaVO> listaSelectItemTurmaAdicionar;
	private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
	private String onCompleteIncluirDisciplinaTurma;
	private HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs;
	private Integer codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas;
	private PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoVOIncluirGrupoOptativas;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private DisciplinaVO disciplinaForaGradeVO;
	private String mensagemMediaFrequencia;
	private String mensagemNotaLancada;
	private String mensagemRegistroAulaLancado;
	private HistoricoVO historicoVO;
	private HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademivoVOs;
	private Boolean permitirUsuarioExcluirDisciplina;
	private Boolean permitirUsuarioExcluirDisciplinaForaPrazo;
	private List<HistoricoVO> listaHistoricoDisciplinaForaGradeVOs;
	private Boolean fecharModalConfirmacaoExclusaoDisciplina;
	private Boolean realizandoExclusaoDisciplinaForaPrazo;
	private List<SelectItem> listaSelectItemMatrizCurricular;
	private Integer matrizCurricular;
	private List<HistoricoVO> listaHistoricosDuplicadosAluno;
	private Boolean realizandoExclusaoHistoricoDuplicadoAluno;
	private UsuarioVO usuarioLibercaoChoqueHorario;
	private GradeDisciplinaCompostaVO gradeDisciplinaCompostaSelecionadaVO;
	private Boolean permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento;
	private RequerimentoVO requerimentoVO;
	private GradeDisciplinaVO gradeDisciplinaRequerimentoVO;
	private Boolean choqueHorarioLiberado;
	private UsuarioVO usuarioLibercaoPreRequisito;
	private Boolean preRequisitoLiberado;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	private String usernameLiberarMatriculaAcimaNrVagas;
	private String senhaLiberarMatriculaAcimaNrVagas;	
	private Boolean permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena;
	private Boolean filtrarTurmaAulaNaoOcorreu;
	private Predicate<String> permissaoFuncionalidadeUsuarioLogado;
	
	public ProcessoInclusaoExclusaoDisciplinaMatriculaControle() {
		super();
		setMatricula("");
		getControleConsulta().setCampoConsulta("matricula");
		novo();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setMatriculaVO(new MatriculaVO());
		setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		getMatriculaPeriodoVO().setInclusaoForaPrazo(Boolean.TRUE);
		setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
		// setHorarioAlunoVO(new HorarioAlunoVO());
		setListaDisciplinasComVagasUltrapassadas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setListaDisciplinasSemVagas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setListaMatriculaPeriodoTurmaDisciplina(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setListaDisciplinaAdicionadas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setAno("");
		setSemestre("");
		setSenha("");
		setUserName("");
		setGradeDisciplina(0);
		setPeridoLetivo(0);
		setTurma(new TurmaVO());
		setImprimir(Boolean.FALSE);
		setEditandoInclusaoHistoricoForaPrazo(false);
		getMatriculaPeriodoVO().setInclusaoForaPrazo(false);
		getMatriculaPeriodoVO().setReposicao(false);
		setApresentarDisciplinasEquivalentesIncluir(false);
		setDisciplinaEquivalenteAdicionar((Integer) 0);
		setResponsavel(getUsuarioLogadoClone());
		montarListaSelectItens();
		setTipoRequerimento("");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaForm.xhtml");
	}

	@PostConstruct
	public String inicializarDadosInclusaoExclusaoDisciplinaTelaRequerimento() {
		RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getSessionMap().get("requerimentoInclusaoExclusaoDisciplina");
		if (obj != null && !obj.getCodigo().equals(0)) {
			novo();
			setRequerimentoVO(obj);
			consultarRequerimentoPorChavePrimaria();
			if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString())) {
				getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
				getMatriculaPeriodoVO().setReposicao(false);
			} else {
				getMatriculaPeriodoVO().setReposicao(true);
				getMatriculaPeriodoVO().setInclusaoForaPrazo(false);
			}
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaForm.xhtml");
	}
	
	@PostConstruct
	public void realizarCarregamentoInclusaoDisciplinaVindoTelaFichaAluno() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				setMatriculaVO(matriculaVO);
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}
			
		}
	}
	
	public void verificarPermissaoUsuarioExcluirDisciplina() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoExclusaoDisciplina_permitirExcluirDisciplina", getUsuarioLogado());
			setPermitirUsuarioExcluirDisciplina(Boolean.TRUE);
		} catch (Exception e) {
			setPermitirUsuarioExcluirDisciplina(Boolean.FALSE);
		}
	}

	public void verificarPermissaoUsuarioExcluirDisciplinaForaPrazo() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoExclusaoDisciplina_permitirExcluirDisciplinaForaPrazo", getUsuarioLogado());
			setPermitirUsuarioExcluirDisciplinaForaPrazo(true);
		} catch (Exception e) {
			setPermitirUsuarioExcluirDisciplinaForaPrazo(false);
		}
	}

	public void autorizar() {
		try {
			setDisciplinaIncluida(false);
			if (!getListaDisciplinasComVagasUltrapassadas().isEmpty()) {
				verificaPermisaoMatriculaNrMaximo();
			}
			removerDisciplinasSemVagas();
			getFacadeFactory().getMatriculaPeriodoFacade().alterar(getMatriculaPeriodoVO(), getMatriculaVO(), null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			setDisciplinaIncluida(true);
		} catch (Exception e) {
			setDisciplinaIncluida(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerDisciplinasSemVagas() throws Exception {
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = getListaDisciplinasSemVagas().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			getMatriculaPeriodoVO().excluirObjMatriculaPeriodoVOs(obj.getDisciplina().getCodigo());
			// getHorarioAlunoVO().removerDisponibilidadeHorarioAlunoVOs(obj.getDisciplina().getCodigo());
		}
		setListaDisciplinasSemVagas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
	}

	public void verificaPermisaoMatriculaNrMaximo() throws Exception {
		UsuarioVO usuario = Usuario.verificarLoginUsuario(getUserName(), getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
		Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "MatriculaNrMaximoVagas");
		getMatriculaPeriodoVO().setResponsavelMatriculaForaPrazo(usuario);
	}

	public void naoAutorizar() {
		setListaDisciplinasComVagasUltrapassadas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setListaDisciplinasSemVagas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setMensagemDetalhada("", "");
		setDisciplinaIncluida(false);
	}

	public void persistir() {
		try {
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarPorMatriculaPeriodo();
			setDisciplinaIncluida(false);
			getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().persistir(false, getInclusaoHistoricoForaPrazoVO(), getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs(), getMatriculaPeriodoVO(), getMatriculaVO(), getJustificativa(), getObservacaoJustificativa(), textoPadraoContratoInclusao, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()), getRequerimentoVO(), getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO(), getMapMatriculaPeriodoVOs(), false, getUsuarioLogado());
			// getInclusaoHistoricoForaPrazoVO().setListaInclusaoDisciplinasHistoricoForaPrazoVO(getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().consultarPorInclusaoHistoricoForaPrazo(getInclusaoHistoricoForaPrazoVO().getCodigo(),
			// false, getUsuarioLogado()));
			Integer gradeCurricular = getMatrizCurricular();
			preencherDadosInclusaoHistoricoForaPrazoEdicao();
			setImprimir(Boolean.TRUE);
			setParcelaReposicaoNaoGravada(false);
			setDisciplinaIncluida(true);
			if(Uteis.isAtributoPreenchido(gradeCurricular)) {
				setMatrizCurricular(gradeCurricular);
			}
			realizarMontagemPainelMatrizCurricular();
			setEditandoInclusaoHistoricoForaPrazo(true);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setDisciplinaIncluida(false);
			setInclusaoHistoricoForaPrazoVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoInclusaoExclusaoDisciplinaMatriculaControle", "Iniciando Consultar InclusaoHistoricoForaPrazo", "Consultando");
			super.consultar();
			List<InclusaoHistoricoForaPrazoVO> objs = new ArrayList<InclusaoHistoricoForaPrazoVO>(0);
			getControleConsultaOtimizado().setLimitePorPagina(10);

			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorMatriculaTotalRegistros(getControleConsulta().getValorConsulta(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				objs = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorRegistroAcademico(getControleConsulta().getValorConsulta(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorRegistroAcademicoTotalRegistros(getControleConsulta().getValorConsulta(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				objs = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorAluno(getControleConsulta().getValorConsulta(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorAlunoTotalRegistros(getControleConsulta().getValorConsulta(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("responsavel")) {
				objs = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorResponsavel(getControleConsulta().getValorConsulta(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorResponsavelTotalRegistros(getControleConsulta().getValorConsulta(), false, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInclusao")) {
				objs = getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorDataInclusao(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), false, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorDataInclusaoTotalRegistros(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), false, getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoInclusaoExclusaoDisciplinaMatriculaControle", "Finalizando Consultar InclusaoHistoricoForaPrazo", "Consultando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<InclusaoHistoricoForaPrazoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaCons.xhtml");
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("responsavel", "Responsável"));
		itens.add(new SelectItem("dataInclusao", "Data de Inclusão"));
		return itens;
	}

	public boolean isCampoText() {
		return getControleConsulta().getCampoConsulta().equals("matricula") ||getControleConsulta().getCampoConsulta().equals("registroAcademico") ||  getControleConsulta().getCampoConsulta().equals("aluno") || getControleConsulta().getCampoConsulta().equals("responsavel");
	}

	public String editar() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoInclusaoExclusaoDisciplinaMatriculaControle", "Inicializando Editar InclusaoHistoricoForaPrazo", "Editando");
			InclusaoHistoricoForaPrazoVO obj = (InclusaoHistoricoForaPrazoVO) context().getExternalContext().getRequestMap().get("inclusaoHistoricoForaPrazoItens");
			obj.setListaInclusaoDisciplinasHistoricoForaPrazoVO(getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().consultarPorInclusaoHistoricoForaPrazo(obj.getCodigo(), false, getUsuarioLogado()));
			obj.setNovoObj(Boolean.FALSE);
			setEditandoInclusaoHistoricoForaPrazo(true);
			setInclusaoHistoricoForaPrazoVO(obj);
			preencherDadosInclusaoHistoricoForaPrazoEdicao();
			registrarAtividadeUsuario(getUsuarioLogado(), "ProcessoInclusaoExclusaoDisciplinaMatriculaControle", "Finalizando Editar InclusaoHistoricoForaPrazo", "Editando");
			verificarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena();
			validarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena();
			verificarPermissaoUsuarioExcluirDisciplina();
			verificarPermissaoUsuarioExcluirDisciplinaForaPrazo();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void preencherDadosInclusaoHistoricoForaPrazoEdicao() throws Exception {
		getMapMatriculaPeriodoVOs().clear();
		if (!getInclusaoHistoricoForaPrazoVO().getCodigo().equals(0)) {
			montarListaSelectItens();
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getInclusaoHistoricoForaPrazoVO().getMatriculaPeriodoVO().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			setMatriculaPeriodoVO(getInclusaoHistoricoForaPrazoVO().getMatriculaPeriodoVO());
			getMatriculaPeriodoVO().setReposicao(getInclusaoHistoricoForaPrazoVO().getReposicao());
			if (!getInclusaoHistoricoForaPrazoVO().getReposicao()) {
				getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
			}
			setTextoPadraoContratoInclusao(getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato());
			setJustificativa(getInclusaoHistoricoForaPrazoVO().getJustificativa());
			setObservacaoJustificativa(getInclusaoHistoricoForaPrazoVO().getObservacao());
			getMatriculaPeriodoVO().setPlanoFinanceiroReposicaoVO(getInclusaoHistoricoForaPrazoVO().getPlanoFinanceiroReposicaoVO());
			getMatriculaPeriodoVO().setNumParcelasInclusaoForaPrazo(getInclusaoHistoricoForaPrazoVO().getNrParcelas());
			getMatriculaPeriodoVO().setValorTotalParcelaInclusaoForaPrazo(getInclusaoHistoricoForaPrazoVO().getValorTotalParcela());
			getMatriculaPeriodoVO().setDescontoReposicao(getInclusaoHistoricoForaPrazoVO().getDesconto());
			getMatriculaPeriodoVO().setDiaVencimentoInclusaoForaPrazo(getInclusaoHistoricoForaPrazoVO().getDataVencimento());
			setResponsavel(getInclusaoHistoricoForaPrazoVO().getResponsavel());
			setDataInclusao(getInclusaoHistoricoForaPrazoVO().getDataInclusao());
			montarListaSelectItemPlanoFinanceiroReposicao();
			montarListaSelectItemMatrizCurricular();
			if (!getInclusaoHistoricoForaPrazoVO().getRequerimentoVO().getCodigo().equals(0)) {
				setRequerimentoVO(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaTipoRequerimentoInclusaoEReposicaoDisciplinaDadosMinimos(getInclusaoHistoricoForaPrazoVO().getRequerimentoVO().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
		}
	}

	// public void imprimirContratoReposicao() {
	// try {
	// ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
	// impressaoContratoVO.setMatriculaVO(getMatriculaVO());
	// impressaoContratoVO.setMatriculaPeriodoVO(getMatriculaPeriodoVO());
	// getFacadeFactory().getPlanoFinanceiroReposicaoFacade().carregarDados(getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO(),
	// NivelMontarDados.TODOS, getUsuarioLogado());
	// setImprimirContrato(getFacadeFactory().getImpressaoContratoFacade().imprimirContratoInclusaoReposicao(impressaoContratoVO,
	// getConfiguracaoFinanceiroPadraoSistema(),
	// getTextoPadraoContratoInclusao(), getUsuarioLogado()));
	// setMensagemID("msg_impressaoContrato_contratoImpresso");
	// } catch (Exception ex) {
	// setMensagemDetalhada("msg_erro", ex.getMessage());
	// }
	// }

	public void imprimirContrato() {
		try {
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setMatriculaVO(getMatriculaVO());
			impressaoContratoVO.setMatriculaPeriodoVO(getMatriculaPeriodoVO());
			if (impressaoContratoVO.getMatriculaPeriodoVO().getReposicao()) {
				getFacadeFactory().getPlanoFinanceiroReposicaoFacade().carregarDados(getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			}
			String caminhoRelatorio = "";
			caminhoRelatorio = getFacadeFactory().getImpressaoContratoFacade().imprimirContratoInclusaoReposicao(impressaoContratoVO, getConfiguracaoFinanceiroPadraoSistema(), getTextoPadraoContratoInclusao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			if(Uteis.isAtributoPreenchido(caminhoRelatorio)){
				setImprimirContrato(true);
			}
			setMensagemID("msg_impressaoContrato_contratoImpresso");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545)";
		}
		return "";
	}

	public String getExigirPermissao() {
		if (getListaDisciplinasComVagasUltrapassadas().isEmpty() && getListaDisciplinasSemVagas().isEmpty()) {
			if (getDisciplinaIncluida()) {
				return "RichFaces.$('panelEnviarEmail').show()";
			} else {
				return "";
			}
		}
		return "RichFaces.$('panelDisciplina').show()";
	}

	public Boolean getDisciplinasSemVaga() {
		return !getListaDisciplinasSemVagas().isEmpty();
	}

	public Boolean getDisciplinasComVagaUltrapassadas() {
		return !getListaDisciplinasComVagasUltrapassadas().isEmpty();
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			setMapMatriculaPeriodoVOs(new HashMap<String, MatriculaPeriodoVO>(0));
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				// MatriculaVO obj =
				// getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,  getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				// objs =
				// getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				// objs =
				// getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
				// this.getUnidadeEnsinoLogado().getCodigo(), false, "AT");
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			setMapMatriculaPeriodoVOs(new HashMap<String, MatriculaPeriodoVO>(0));
			carregarDadosMatriculaSelecionada(getMatriculaVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
			setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		}
	}

	public void carregarDadosMatriculaSelecionada(MatriculaVO obj) throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(obj, getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado ou a situação da matrícula não possibilita a alteração da mesma.");
			}

			getFacadeFactory().getTrabalhoConclusaoCursoFacade().validarTrabalhoConclusaoCursoIniciado(obj.getMatricula());
			this.setMatriculaVO(obj);
			setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			if (getMatriculaPeriodoVO() == null || getMatriculaPeriodoVO().getCodigo().equals(0)) {
				throw new Exception("Não foi encontrado nehuma Matricula Periodo para esta Matricula.");
			}
			verificarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena();
			validarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena();
			getMatriculaPeriodoVO().setInclusaoForaPrazo(Boolean.TRUE);
			verificarPermissaoUsuarioExcluirDisciplina();
			montarListaSelectItemMatrizCurricular();
			verificarPermissaoUsuarioExcluirDisciplinaForaPrazo();
			getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO();
			if (getIsApresentarPlanoFinanceiroInclusao() || getIsApresentarPlanoFinanceiroReposicao()) {
				montarListaSelectItemPlanoFinanceiroReposicao();
			}
			if (getRequerimentoVO().getCodigo().equals(0)) {
				verificarObrigatoriedadeInclusaoExclusaoSomenteViaRequerimento();
				validarDadosRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void consultarDisciplinaForaGrade() {
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
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarMatriculaComHistoricoAluno(Boolean forcarNovoCarregamentoDados) throws Exception {
		if ((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getIsInicializado()) && (!forcarNovoCarregamentoDados)) {
			return;
		}

		// CARREGANDO DADOS DA CFG DO CURSO PARA SER UTILIZADO POSTERIORMENTE
		if (Uteis.isAtributoPreenchido(getMatriculaVO().getCurso().getConfiguracaoAcademico())) {
			ConfiguracaoAcademicoVO cfgCurso = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado());
			getMatriculaVO().getCurso().setConfiguracaoAcademico(cfgCurso);
		}

		if (getMatriculaVO().getMatricula().equals("")) {
			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getGradeCurricular())) {
				throw new Exception("Não é possível iniciar uma Inclusão de Disciplinas sem que uma matriz curricular seja informada");
			}
			setMatrizCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
		}
		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(getMatriculaVO(), getMatrizCurricular(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
		getMatriculaVO().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		getMatriculaPeriodoVO().setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			carregarDadosMatriculaSelecionada(obj);
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
			setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		}
	}

	public void selecionarMatriculaPeriodoTurmaDisciplinaComposta() {
		setGradeDisciplinaVO((GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens"));
		getGradeDisciplinaVO().setSelecionado(true);
		try {
			MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO());
			getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(getFacadeFactory().getMatriculaPeriodoFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getGradeDisciplinaVO().setSelecionado(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void naoContinuarReposicao() {
		// setTurma(new TurmaVO());
		getGradeDisciplinaVO().setMatriculaPeriodoTurmaDisciplinaVO(null);
		getGradeDisciplinaVO().setSelecionado(Boolean.FALSE);
	}

	public void naoContinuarExclusaoDisciplina() {
		setMensagemMediaFrequencia("");
		setMensagemNotaLancada("");
		setMensagemRegistroAulaLancado("");
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
		// setListaHistorico(new ArrayList(0));
		setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		setListaDisciplinaAdicionadas(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0));
		setTurma(new TurmaVO());
		setGradeDisciplina(0);
		setAno("");
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		setRequerimentoVO(null);
	}

	public void limparDadosObservacao() throws Exception {
		setObservacaoJustificativa("");
		setJustificativa("");
	}

	@SuppressWarnings("unchecked")
	public void realizarVerificarSeHaIncompatibilidadeHorarioDisciplina() throws Exception {
		List<MatriculaVO> listaAlunosTurma = getFacadeFactory().getInclusaoExclusaoDisciplina().consultarAlunosPorMatricula(getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		List<DisciplinaVO> listaDisciplinasCursadasPeloAluno = getFacadeFactory().getInclusaoExclusaoDisciplina().executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(listaAlunosTurma, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
		TurmaVO turmaAtual = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTurma().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		getFacadeFactory().getInclusaoExclusaoDisciplina().executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(getMatriculaPeriodoTumaDisciplinaVO(), getListaDisciplinaAdicionadas(), listaDisciplinasCursadasPeloAluno, turmaAtual, getSemestre(), getAno(), getUsuarioLogado());

	}

	public void validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina() throws Exception {
		if (getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getValidarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina().booleanValue()) {
			Date data = getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaDisciplina(getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getCodigo(), getMatriculaPeriodoTumaDisciplinaVO().getAno(), getMatriculaPeriodoTumaDisciplinaVO().getSemestre());
			if (data != null) {
				if (data.after(new Date())) {
					List<MapaLocalAulaTurmaVO> listaTurmaDisciplina = getFacadeFactory().getTurmaDisciplinaFacade().consultarMapaLocalAulaTurma(getMatriculaPeriodoTumaDisciplinaVO().getTurma().getUnidadeEnsino().getCodigo(), new Date(), Uteis.obterDataFutura(new Date(), 3650), 0, getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getCodigo());
					if (listaTurmaDisciplina.isEmpty()) {
						throw new Exception("Não existe uma local de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que haja essa definição, posteriormente será permitido realizar a reposição.");
					} else if (listaTurmaDisciplina.size() > 1) {
						throw new Exception("Existe dois locais de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que exista apenas um local de aula/sala cadastrado.");
					} else {
						MapaLocalAulaTurmaVO t = (MapaLocalAulaTurmaVO) listaTurmaDisciplina.get(0);
						if (t.getTurmaDisciplina().getLocalAula().getCodigo().intValue() == 0 || t.getTurmaDisciplina().getSalaLocalAula().getCodigo().intValue() == 0) {
							throw new Exception("Não existe uma local de aula/sala definido para a turma e disciplina. Para realizar a reposição é necessário que haja essa definição, posteriormente será permitido realizar a reposição.");
						} else {
							Integer qtd = t.getQtdeAluno().intValue() + t.getQtdeAlunoExtRep().intValue();
							SalaLocalAulaVO sala = (SalaLocalAulaVO) getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(t.getTurmaDisciplina().getSalaLocalAula().getCodigo());
							if (qtd >= sala.getCapacidade().intValue()) {
								throw new Exception("A sala de aula encontra-se lotada. Capacidade = " + sala.getCapacidade().intValue() + "; Alunos alocados a sala = " + qtd + ";");
							}
						}
					}
				}
			}
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplina() {
		try {
			if (getGradeDisciplina() == null || getGradeDisciplina() == 0) {
				throw new Exception("Preencha os dados para adicionar a disciplina.");
			}
			
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTumaDisciplinaVO().setTurma(getTurma());
			getMatriculaPeriodoTumaDisciplinaVO().setInclusaoForaPrazo(true);
			if (getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
				getMatriculaPeriodoTumaDisciplinaVO().setMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo());
			}
			if (getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue() != 0) {
				Integer turma = getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue();
				TurmaVO obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				Turma.montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getMatriculaPeriodoTumaDisciplinaVO().setTurma(obj);
			}
			if (getGradeDisciplina().intValue() != 0) {
				GradeDisciplinaVO obj = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplina(), getUsuarioLogado());
				getMatriculaPeriodoTumaDisciplinaVO().setDisciplina(obj.getDisciplina());
				getMatriculaPeriodoTumaDisciplinaVO().setGradeDisciplinaVO(obj);
			}

			if (getFacadeFactory().getHistoricoFacade().consultarSeDisciplinaEstaSendoCursadaOuAprovada(getMatriculaVO().getMatricula(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getCodigo(), getUsuarioLogado())) {
				throw new Exception("Essa disciplina já existe no histórico do aluno, seja cursando ou aprovada.");
			}
			validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina();
			// getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(getMatriculaVO().getMatricula(),
			// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS));
			if (getMatriculaVO().getCurso().getIntegral()) {
				getMatriculaPeriodoTumaDisciplinaVO().setAno(getMatriculaPeriodoVO().getAno());
				getMatriculaPeriodoTumaDisciplinaVO().setSemestre(getMatriculaPeriodoVO().getSemestre());
			} else {
				getMatriculaPeriodoTumaDisciplinaVO().setAno(getAno());
				getMatriculaPeriodoTumaDisciplinaVO().setSemestre(getSemestre());
			}
			getMatriculaPeriodoTumaDisciplinaVO().setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(getMatriculaVO().getCurso(), 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaVO(), getMatriculaPeriodoTumaDisciplinaVO().getGradeCurricularGrupoOptativaDisciplinaVO(), 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaCompostaVO(), getMatriculaPeriodoTumaDisciplinaVO().getTurma(), getMatriculaPeriodoTumaDisciplinaVO().getAno(), getMatriculaPeriodoTumaDisciplinaVO().getSemestre(), getUsuarioLogado()));
			// realizarVerificarSeHaIncompatibilidadeHorarioDisciplina();
			getMatriculaPeriodoVO().adicionarObjMatriculaPeriodoVOsInclusaoForaPrazo(getMatriculaPeriodoTumaDisciplinaVO());
			executarVerificacaoDisciplinaAdicionadaLista(getListaDisciplinaAdicionadas(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina());
			getListaDisciplinaAdicionadas().add(getMatriculaPeriodoTumaDisciplinaVO());
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setGradeDisciplina(0);
			setTurma(new TurmaVO());
			// setPeridoLetivo(0);
			getListaSelectItemDisciplina().clear();
			getListaSelectItemTurma().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararLiberarMatriculaAcimaNrMaximoVagas() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(getMatriculaPeriodoVO(), obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getAno(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), true, getInclusaoHistoricoForaPrazoVO().getReposicao());
			setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(obj.getMatriculaPeriodoTurmaDisciplinaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoUsuarioPodeLiberarMatriculaAcimaNrVagas() {
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarMatriculaAcimaNrVagas(), this.getSenhaLiberarMatriculaAcimaNrVagas(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberarDentroLimiteMaximoTurma = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoNrMaximoVagas", usuarioVerif);
			usuarioTemPermissaoLiberarDentroLimiteMaximoTurma = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoAcimaNrMaximoVagas", usuarioVerif);
			usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma = true;
		} catch (Exception e) {
		}
		try {
			if (!usuarioValido) {
				throw new Exception("Usuário/Senha Inválidos");
			}
			if (!usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma) {
				// se o usuario tem permissao para liberar acima do limite
				// maximo, entao
				// nao entramos neste if, pois nao faz sentido validarmos se
				// est? dentro do limite
				// maximo da turma. Basta permitir a matricula normalmente
				Integer vagasDisponiveisDentroLimite = 0;
				if(!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica()) && !Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica())){
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurma().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculados();
				}
				if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica())){
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaPratica().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculadosTurmaPratica();
				}				
				if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica()) && vagasDisponiveisDentroLimite >= 0){
					vagasDisponiveisDentroLimite = this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getTurmaTeorica().getNrMaximoMatricula() - this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getNrAlunosMatriculadosTurmaTeorica();
				}
				if ((vagasDisponiveisDentroLimite > 0)) {
					if (!usuarioTemPermissaoLiberarDentroLimiteMaximoTurma) {
						throw new Exception("Você não tem permissão para liberar a inclusão de disciplina deste aluno acima do número de vagas disponíveis na turma.");
					}
				} else {
					throw new Exception("Você não tem permissão para liberar a inclusão de disciplina deste aluno acima do número máximo de vagas disponíveis na turma.");
				}
			}
			if(!getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaReferenteAUmGrupoOptativa()){
			Iterator i = getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs().iterator();
			while (i.hasNext()) {
				PeriodoLetivoVO periodoLetivo = (PeriodoLetivoVO)i.next();
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivo.getGradeDisciplinaVOs()) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina = gradeDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO();
					if ((matriculaPeriodoTurmaDisciplina.getDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplina().getCodigo()))
							|| (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaComposta() && matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeDisciplinaVO().getCodigo()))
							|| (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaFazParteComposicao() && matriculaPeriodoTurmaDisciplina.getGradeDisciplinaVO().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeDisciplinaCompostaVO().getGradeDisciplina().getCodigo()))) {
						if (usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma) {
							matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
						} else {
							if ((matriculaPeriodoTurmaDisciplina.getApresentarLiberacaoVaga()) && (usuarioTemPermissaoLiberarDentroLimiteMaximoTurma)) {
								matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
							}
						}					
					}
				}
			}
			}else{
				Iterator<GradeCurricularGrupoOptativaVO> i = getMatriculaPeriodoVO().getGradeCurricular().getGradeCurricularGrupoOptativaVOs().iterator();
				while (i.hasNext()) {
					GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = (GradeCurricularGrupoOptativaVO)i.next();
					for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()) {
						MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina = gradeCurricularGrupoOptativaDisciplinaVO.getMatriculaPeriodoTurmaDisciplinaVO();
						if ((matriculaPeriodoTurmaDisciplina.getDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplina().getCodigo()))
								|| (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaComposta() && matriculaPeriodoTurmaDisciplina.getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo()))
								|| (getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaFazParteComposicao() && matriculaPeriodoTurmaDisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().equals(this.getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getGradeDisciplinaCompostaVO().getGradeCurricularGrupoOptativaDisciplina().getCodigo()))) {
							if (usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma) {
								matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
							} else {
								if ((matriculaPeriodoTurmaDisciplina.getApresentarLiberacaoVaga()) && (usuarioTemPermissaoLiberarDentroLimiteMaximoTurma)) {
									matriculaPeriodoTurmaDisciplina.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
								}
							}					
						}
					}
				}
			}
			
			if(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas().getDisciplinaFazParteComposicao()){
				List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(), getMatriculaPeriodoVO());
				for(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO: listaMatriculaPeriodoTurmaDisciplinaFazParteComposicao){
					if (usuarioTemPermissaoLiberarAcimaLimiteMaximoTurma || ((matriculaPeriodoTurmaDisciplinaVO.getApresentarLiberacaoVaga()) && (usuarioTemPermissaoLiberarDentroLimiteMaximoTurma))) {
						matriculaPeriodoTurmaDisciplinaVO.setLiberadaSemDisponibilidadeVagas(Boolean.TRUE);
					}
				}
			}
			
			this.setUsernameLiberarMatriculaAcimaNrVagas("");
			this.setSenhaLiberarMatriculaAcimaNrVagas("");
			setMensagemID("msg_ConfirmacaoLiberacaoMatriculaAcimaNrVadas");
		} catch (Exception e) {
			this.setUsernameLiberarMatriculaAcimaNrVagas("");
			this.setSenhaLiberarMatriculaAcimaNrVagas("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarVerificacaoDisciplinaAdicionadaLista(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasAdicionadas, DisciplinaVO disciplinaVO) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : listaDisciplinasAdicionadas) {
			if (matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
				throw new Exception("Essa disciplina já foi adicionada na lista.");
			}
		}
	}

	public void montarListaSelectItens() {
		try {
			montarListaSelectItemPlanoDescontoInclusaoDisciplina();
			montarListaSelectItemInclusaoDisciplinasForaDoPrazo();
			montarListaSelectItemTextoPadraoContrato();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void prepararDisciplinaForaGradeInclusao() {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaForaGradeItens");
		setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());

		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(obj);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(new GradeDisciplinaVO());
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaReferenteAUmGrupoOptativa(Boolean.FALSE);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaForaGrade(Boolean.TRUE);
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().setCargaHoraria(obj.getCargaHorariaForaGrade());
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().setDisciplina(obj);
		setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
		setCodigoMapaEquivalenciaDisciplinaCursar(0);
		setMensagemID("msg_informe_dados"); // tem que ficar antes, pois o
		// montar da turma pode disparar
		// um excption importante que
		// deve prevalecar
		montarListaSelectItemTurmaAdicionar();
		if (getHorarioAlunoTurnoVOs().isEmpty()) {
			prepararHorarioAulaAluno();
		} else {
			getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
		}

	}

	public void limparDadosConsultaDisciplinaforaGrade() {
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void montarListaSelectItemInclusaoDisciplinasForaDoPrazo() {
		setListaSelectItemInclusaoDisciplinasForaDoPrazo(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(InclusaoDisciplinasForaDoPrazo.class, false));
	}

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
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

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
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

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public MatriculaVO getMatriculaVO() {
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTumaDisciplinaVO() {
		return matriculaPeriodoTumaDisciplinaVO;
	}

	public void setMatriculaPeriodoTumaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTumaDisciplinaVO) {
		this.matriculaPeriodoTumaDisciplinaVO = matriculaPeriodoTumaDisciplinaVO;
	}

	public Integer getGradeDisciplina() {
		return gradeDisciplina;
	}

	public void setGradeDisciplina(Integer gradeDisciplina) {
		this.gradeDisciplina = gradeDisciplina;
	}

	public Integer getPeridoLetivo() {
		return peridoLetivo;
	}

	public void setPeridoLetivo(Integer peridoLetivo) {
		this.peridoLetivo = peridoLetivo;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasComVagasUltrapassadas() {
		if (listaDisciplinasComVagasUltrapassadas == null) {
			listaDisciplinasComVagasUltrapassadas = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasComVagasUltrapassadas;
	}

	public void setListaDisciplinasComVagasUltrapassadas(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasComVagasUltrapassadas) {
		this.listaDisciplinasComVagasUltrapassadas = listaDisciplinasComVagasUltrapassadas;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasSemVagas() {
		if (listaDisciplinasSemVagas == null) {
			listaDisciplinasSemVagas = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasSemVagas;
	}

	public void setListaDisciplinasSemVagas(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasSemVagas) {
		this.listaDisciplinasSemVagas = listaDisciplinasSemVagas;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public HorarioAlunoTurnoVO getHorarioAlunoTurnoVO() {
		return horarioAlunoTurnoVO;
	}

	public void setHorarioAlunoTurnoVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO) {
		this.horarioAlunoTurnoVO = horarioAlunoTurnoVO;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinaAdicionadas() {
		if (listaDisciplinaAdicionadas == null) {
			listaDisciplinaAdicionadas = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinaAdicionadas;
	}

	public void setListaDisciplinaAdicionadas(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaAdicionadas) {
		this.listaDisciplinaAdicionadas = listaDisciplinaAdicionadas;
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

	public Boolean getImprimir() {
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
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
	public List<MatriculaVO> getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
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
	 * @return the listaMatriculaPeriodoTurmaDisciplina
	 */
	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplina() {
		return listaMatriculaPeriodoTurmaDisciplina;
	}

	/**
	 * @param listaMatriculaPeriodoTurmaDisciplina
	 *            the listaMatriculaPeriodoTurmaDisciplina to set
	 */
	public void setListaMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina) {
		this.listaMatriculaPeriodoTurmaDisciplina = listaMatriculaPeriodoTurmaDisciplina;
	}

	public Boolean getMostrarAnoSemestreNoFormulario() {
		return (getMatriculaVO().getCurso().getPeriodicidade().equals("SE") || getMatriculaVO().getCurso().getPeriodicidade().equals("AN"));
	}

	public void realizarDesativacaoInclusaoForaPrazo() {
		getMatriculaPeriodoVO().setInclusaoForaPrazo(false);
		getMatriculaPeriodoVO().setReposicao(true);
		montarListaSelectItemPlanoFinanceiroReposicao();
	}

	public void realizarDesativacaoReposicao() {
		getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
		getMatriculaPeriodoVO().setReposicao(false);
		montarListaSelectItemPlanoFinanceiroReposicao();
	}

	public boolean getIsApresentarOpcoesReposicaoInclusao() {
		return getMatriculaVO().getCurso().getNivelEducacional().equals("PO");
	}

	public boolean getIsApresentarJustificativa() {
		if (getMatriculaPeriodoVO().getReposicao() || getMatriculaPeriodoVO().getInclusaoForaPrazo()) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaPlanoDescontoInclusaoDisciplina() {
		if (listaPlanoDescontoInclusaoDisciplina == null) {
			listaPlanoDescontoInclusaoDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaPlanoDescontoInclusaoDisciplina;
	}

	public void setListaPlanoDescontoInclusaoDisciplina(List<SelectItem> listaPlanoDescontoInclusaoDisciplina) {
		this.listaPlanoDescontoInclusaoDisciplina = listaPlanoDescontoInclusaoDisciplina;
	}

	public void montarListaSelectItemPlanoDescontoInclusaoDisciplina() {
		List<PlanoDescontoInclusaoDisciplinaVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarPlanoDescontoInclusaoDisciplinaPorDescricao();
			setListaPlanoDescontoInclusaoDisciplina(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void montarListaSelectItemPlanoFinanceiroReposicao() {
		List<PlanoFinanceiroReposicaoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarPlanoFinanceiroReposicaoPorSituacao();
			setListaPlanoFinanceiroReposicao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<PlanoDescontoInclusaoDisciplinaVO> consultarPlanoDescontoInclusaoDisciplinaPorDescricao() throws Exception {
		return getFacadeFactory().getPlanoDescontoInclusaoDisciplinaFacade().consultarPorSituacao("AT", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public List<PlanoFinanceiroReposicaoVO> consultarPlanoFinanceiroReposicaoPorSituacao() throws Exception {
		return getFacadeFactory().getPlanoFinanceiroReposicaoFacade().consultaRapidaPorSituacao("AT", false, getUsuarioLogado());
	}

	public boolean getIsApresentarCampoPlanoDesconto() throws Exception {
		return (getMatriculaPeriodoVO().getReposicao() && !getMatriculaPeriodoVO().getInclusaoForaPrazo()) && (!getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroReposicao() && !getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroInclusao());
	}

	public boolean getIsApresentarCampoTextoPadraoContrato() throws Exception {
		return (getMatriculaPeriodoVO().getReposicao() || getMatriculaPeriodoVO().getInclusaoForaPrazo());
	}

	public boolean getIsApresentarPlanoFinanceiroReposicao() throws Exception {
		return (getMatriculaPeriodoVO().getReposicao() && getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroReposicao());
	}

	public boolean getIsApresentarPlanoFinanceiroInclusao() throws Exception {
		return (getMatriculaPeriodoVO().getInclusaoForaPrazo()) && getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroInclusao();
	}

	public boolean getIsApresentarCampoReposicaoForaPrazo() throws Exception {
		return (getMatriculaPeriodoVO().getReposicao() && getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroReposicao());
	}

	public boolean getIsApresentarCampoInclusaoForaPrazo() throws Exception {
		return (getMatriculaPeriodoVO().getInclusaoForaPrazo() && getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroInclusao());
	}

	public boolean getIsApresentarCampoReposicaoForaPrazoSemPermissao() throws Exception {
		return (getMatriculaPeriodoVO().getReposicao() && !getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroReposicao());
	}

	public boolean getIsApresentarCampoInclusaoForaPrazoSemPermissao() throws Exception {
		return (getMatriculaPeriodoVO().getInclusaoForaPrazo() && !getConfiguracaoFinanceiroPadraoSistema().getUtilizaPlanoFinanceiroInclusao());
	}

	public boolean getIsApresentarTurmaDisciplina() {
		return ((getMatriculaPeriodoVO().getInclusaoForaPrazo() || getMatriculaPeriodoVO().getReposicao()) && !getEditandoInclusaoHistoricoForaPrazo());
	}

	public boolean getIsApresentarTurmaDisciplinaEditando() {
		return (getMatriculaPeriodoVO().getInclusaoForaPrazo() || getMatriculaPeriodoVO().getReposicao() || getEditandoInclusaoHistoricoForaPrazo());
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

	public ComunicadoInternoDestinatarioVO getComunicadoInternoDestinatarioVO() {
		if (comunicadoInternoDestinatarioVO == null) {
			comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		}
		return comunicadoInternoDestinatarioVO;
	}

	public void setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO) {
		this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
	}

	public Boolean getDisciplinaIncluida() {
		if (disciplinaIncluida == null) {
			disciplinaIncluida = false;
		}
		return disciplinaIncluida;
	}

	public void setDisciplinaIncluida(Boolean disciplinaIncluida) {
		this.disciplinaIncluida = disciplinaIncluida;
	}

	public Boolean getEnviarComunicadoPorEmail() {
		if (enviarComunicadoPorEmail == null) {
			enviarComunicadoPorEmail = true;
		}
		return enviarComunicadoPorEmail;
	}

	public void setEnviarComunicadoPorEmail(Boolean enviarComunicadoPorEmail) {
		this.enviarComunicadoPorEmail = enviarComunicadoPorEmail;
	}

	public Boolean getApresentarDisciplinasEquivalentesIncluir() {
		if (apresentarDisciplinasEquivalentesIncluir == null) {
			apresentarDisciplinasEquivalentesIncluir = false;
		}
		return apresentarDisciplinasEquivalentesIncluir;
	}

	public void setApresentarDisciplinasEquivalentesIncluir(Boolean apresentarDisciplinasEquivalentesIncluir) {
		this.apresentarDisciplinasEquivalentesIncluir = apresentarDisciplinasEquivalentesIncluir;
	}

	public List<SelectItem> getListaSelectItemDisciplinaEquivalenteAdicionar() {
		if (listaSelectItemDisciplinaEquivalenteAdicionar == null) {
			listaSelectItemDisciplinaEquivalenteAdicionar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaEquivalenteAdicionar;
	}

	public void setListaSelectItemDisciplinaEquivalenteAdicionar(List<SelectItem> listaSelectItemDisciplinaEquivalenteAdicionar) {
		this.listaSelectItemDisciplinaEquivalenteAdicionar = listaSelectItemDisciplinaEquivalenteAdicionar;
	}

	public Integer getDisciplinaEquivalenteAdicionar() {
		if (disciplinaEquivalenteAdicionar == null) {
			disciplinaEquivalenteAdicionar = 0;
		}
		return disciplinaEquivalenteAdicionar;
	}

	public void setDisciplinaEquivalenteAdicionar(Integer disciplinaEquivalenteAdicionar) {
		this.disciplinaEquivalenteAdicionar = disciplinaEquivalenteAdicionar;
	}

	public List<SelectItem> getListaSelectItemTurmaDisciplinaEquivalenteAdicionar() {
		if (listaSelectItemTurmaDisciplinaEquivalenteAdicionar == null) {
			listaSelectItemTurmaDisciplinaEquivalenteAdicionar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
	}

	public void setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(List<SelectItem> listaSelectItemTurmaDisciplinaEquivalenteAdicionar) {
		this.listaSelectItemTurmaDisciplinaEquivalenteAdicionar = listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
	}

	public TurmaVO getTurmaIncluirMatriculaPeriodo() {
		if (turmaIncluirMatriculaPeriodo == null) {
			turmaIncluirMatriculaPeriodo = new TurmaVO();
		}
		return turmaIncluirMatriculaPeriodo;
	}

	public void setTurmaIncluirMatriculaPeriodo(TurmaVO turmaIncluirMatriculaPeriodo) {
		this.turmaIncluirMatriculaPeriodo = turmaIncluirMatriculaPeriodo;
	}

	public boolean getIsNaoApresentarTurmaBotaoIncluir() {
		return (!getApresentarDisciplinasEquivalentesIncluir());
	}

	public String getAutorizadaInclusaoDisciplina() {
		if (getDisciplinaIncluida()) {
			return "RichFaces.$('panelDisciplina').hide(); RichFaces.$('panelEnviarEmail').show()";
		}
		return "RichFaces.$('panelDisciplina').hide()";
	}

	public void enviarEmailAlunos() {
		try {
			getMatriculaPeriodoVO().getMatriculaVO().setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getMatriculaPeriodoVO().getMatriculaVO().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoInclusaoDisciplina(getMatriculaPeriodoVO().getMatriculaVO(), getInclusaoHistoricoForaPrazoVO().getListaInclusaoDisciplinasHistoricoForaPrazoVO(), getUsuarioLogado());
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemDisciplinaEquivalenteAdicionar() {
		List<MapaEquivalenciaDisciplinaCursadaVO> resultadoConsulta = null;
		Iterator<MapaEquivalenciaDisciplinaCursadaVO> i = null;
		try {
			if ((!this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) || (this.getCodigoMapaEquivalenciaDisciplinaVOIncluir().equals(0))) {
				setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList<SelectItem>(0));
				return;
			}
			MapaEquivalenciaDisciplinaVO mapa = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(this.getCodigoMapaEquivalenciaDisciplinaVOIncluir(), NivelMontarDados.TODOS);
			validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(this.getMatriculaVO(), getMapMatriculaPeriodoVOs().values(), mapa);
			this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setMapaEquivalenciaDisciplinaVOIncluir(mapa);
			resultadoConsulta = mapa.getMapaEquivalenciaDisciplinaCursadaVOs();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			i = resultadoConsulta.iterator();
			objs.add(new SelectItem("", ""));
			while (i.hasNext()) {
				MapaEquivalenciaDisciplinaCursadaVO obj = (MapaEquivalenciaDisciplinaCursadaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplinaVO().getNome() + " - CH: " + obj.getCargaHoraria()));
			}
			setListaSelectItemDisciplinaEquivalenteAdicionar(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList<>());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<GradeDisciplinaVO> consultarDisciplinasEquivalentesPorGradeDisciplina() throws Exception {
		return getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinasEquivalentesPorGradeDisciplina(getGradeDisciplina(), false, getUsuarioLogado());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemTurmaDisciplinaEquivalenteAdicionar() {
		List<TurmaVO> resultadoConsulta = null;
		Iterator<TurmaVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsino(disciplinaEquivalenteAdicionar, 0, false, getUsuarioLogado());
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma() + " " + obj.getUnidadeEnsino().getAbreviatura() + " - " + obj.getTurno().getNome()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaEquivalente() {
		try {
			if (getDisciplinaEquivalenteAdicionar() == null || getDisciplinaEquivalenteAdicionar() == 0) {
				throw new Exception("Preencha os dados para adicionar a disciplina equivalente.");
			}
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTumaDisciplinaVO().setTurma(getTurmaIncluirMatriculaPeriodo());
			getMatriculaPeriodoTumaDisciplinaVO().setInclusaoForaPrazo(true);
			if (getMatriculaPeriodoVO().getCodigo().intValue() != 0) {
				getMatriculaPeriodoTumaDisciplinaVO().setMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo());
			}
			if (getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue() != 0) {
				Integer turmaAdicionar = getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCodigo().intValue();
				TurmaVO obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaAdicionar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				Turma.montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getMatriculaPeriodoTumaDisciplinaVO().setTurma(obj);
			}
			if (getDisciplinaEquivalenteAdicionar() != 0) {
				DisciplinaVO obj = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaEquivalenteAdicionar(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getMatriculaPeriodoTumaDisciplinaVO().setDisciplina(obj);
				getMatriculaPeriodoTumaDisciplinaVO().setDisciplinaEquivale(true);
				GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplina(), getUsuarioLogado());
				getMatriculaPeriodoTumaDisciplinaVO().setDisciplinaEquivalente(gradeDisciplinaVO.getDisciplina());
				getMatriculaPeriodoTumaDisciplinaVO().setGradeDisciplinaVO(gradeDisciplinaVO);				
			}

			if (getFacadeFactory().getHistoricoFacade().consultarSeDisciplinaEstaSendoCursadaOuAprovada(getMatriculaVO().getMatricula(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getCodigo(), getUsuarioLogado()) || getFacadeFactory().getHistoricoFacade().consultarSeDisciplinaEstaSendoCursadaOuAprovada(getMatriculaVO().getMatricula(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina().getCodigo(), getUsuarioLogado())) {
				throw new Exception("Essa disciplina já existe no histórico do aluno, seja cursando ou aprovada.");
			}
			if (getMatriculaVO().getCurso().getIntegral()) {
				getMatriculaPeriodoTumaDisciplinaVO().setAno(getMatriculaPeriodoVO().getAno());
				getMatriculaPeriodoTumaDisciplinaVO().setSemestre(getMatriculaPeriodoVO().getSemestre());
			} else {
				getMatriculaPeriodoTumaDisciplinaVO().setAno(getAno());
				getMatriculaPeriodoTumaDisciplinaVO().setSemestre(getSemestre());
			}

			CursoVO cursoEquivalente = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoTumaDisciplinaVO().getTurma().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			getMatriculaPeriodoTumaDisciplinaVO().setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(cursoEquivalente, 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaVO(), getMatriculaPeriodoTumaDisciplinaVO().getGradeCurricularGrupoOptativaDisciplinaVO(), 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaCompostaVO(), getMatriculaPeriodoTumaDisciplinaVO().getTurma(), getMatriculaPeriodoTumaDisciplinaVO().getAno(), getMatriculaPeriodoTumaDisciplinaVO().getSemestre(), getUsuarioLogado()));

			validarQtdAlunoLocalSalaAulaInclusaoReposicaoDisciplina();
			getMatriculaPeriodoVO().adicionarObjMatriculaPeriodoVOsInclusaoForaPrazo(getMatriculaPeriodoTumaDisciplinaVO());
			executarVerificacaoDisciplinaAdicionadaLista(getListaDisciplinaAdicionadas(), getMatriculaPeriodoTumaDisciplinaVO().getDisciplina());
			getListaDisciplinaAdicionadas().add(getMatriculaPeriodoTumaDisciplinaVO());
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setDisciplinaEquivalenteAdicionar(0);
			setTurmaIncluirMatriculaPeriodo(new TurmaVO());
			getListaSelectItemDisciplinaEquivalenteAdicionar().clear();
			getListaSelectItemTurmaDisciplinaEquivalenteAdicionar().clear();
			setGradeDisciplina(0);
			setTurma(new TurmaVO());
			getListaSelectItemDisciplina().clear();
			getListaSelectItemTurma().clear();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaPeriodoTumaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemPeriodoLetivoComGrupoOptativas() {
		try {
			setListaSelectItemPeriodoLetivoComGrupoOptativas(null);
			getListaSelectItemPeriodoLetivoComGrupoOptativas().add(new SelectItem(0, ""));
			for (PeriodoLetivoVO periodoLetivoVO : getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs()) {
				getListaSelectItemPeriodoLetivoComGrupoOptativas().add(new SelectItem(periodoLetivoVO.getCodigo(), periodoLetivoVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararDisciplinasGrupoOptativaPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivoComGrupoOptativas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarDisciplinaGrupoOptativaMatriculaPeriodo() {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO gradeOptativaAdicionar = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens");
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			setGradeCurricularGrupoOptativaDisciplinaVO(gradeOptativaAdicionar);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(gradeOptativaAdicionar.getDisciplina());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(new GradeDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(gradeOptativaAdicionar);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaReferenteAUmGrupoOptativa(Boolean.TRUE);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setModalidadeDisciplina(gradeOptativaAdicionar.getModalidadeDisciplina());
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setMensagemID("msg_informe_dados"); // tem que ficar antes, pois o
			// montar da turma pode disparar
			// um excption importante que
			// deve prevalecar
			montarListaSelectItemTurmaAdicionar();
			if (getHorarioAlunoTurnoVOs().isEmpty()) {
				prepararHorarioAulaAluno();
			} else {
				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();

	}

	public void prepararDisciplinasGrupoOptativa() {
		try {
			if (getCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas().intValue() != 0) {
				PeriodoLetivoComHistoricoAlunoVO periodoComHistoricoVO = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOPorCodigo(getCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas());
				Integer codigoGradeCurricularGrupoOptativas = periodoComHistoricoVO.getPeriodoLetivoVO().getGradeCurricularGrupoOptativa().getCodigo();
				GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(codigoGradeCurricularGrupoOptativas, NivelMontarDados.TODOS, getUsuarioLogado());
				periodoComHistoricoVO.getPeriodoLetivoVO().setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
				this.setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(periodoComHistoricoVO);
			}
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			this.setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTextoPadraoContrato() throws Exception {
		List<TextoPadraoVO> textoPadraoVOMatricula = consultarTextoPadraoPorTipo(TipoTextoPadrao.INCLUSAO_REPOSICAO.getValor());
		setListaSelectItemTextoPadraoContrato(UtilSelectItem.getListaSelectItem(textoPadraoVOMatricula, "codigo", "descricao"));
	}

	public void realizarMarcacaoInclusaoDisciplina() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			obj.setSelecionado(Boolean.TRUE);
			setGradeDisciplinaVO(obj);
			setPanelAproveitamentoDisciplinaAberto(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void editarInclusaoGradeDisciplina() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		try {
			setGradeDisciplinaVO(obj);			
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar((MatriculaPeriodoTurmaDisciplinaVO)obj.getMatriculaPeriodoTurmaDisciplinaVO().clone());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina((DisciplinaVO)obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().clone());
		} catch (CloneNotSupportedException e) {			
		}
		setPanelAproveitamentoDisciplinaAberto(Boolean.TRUE);
	}

	public void realizarDesmarcacaoInclusao() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			if(Uteis.isAtributoPreenchido(obj.getHistoricoAtualAluno().getCodigo())){
				removerMatriculaPeriodoTurmaDisciplinaPorHistorico(obj.getHistoricoAtualAluno());
				removerHistoricosDisciplinasAlunoCursandoGradeCurricular(obj.getHistoricoAtualAluno());
			} else {
				obj.getHistoricoAtualAluno().setMatriculaPeriodoTurmaDisciplina(obj.getMatriculaPeriodoTurmaDisciplinaVO());
				removerMatriculaPeriodoTurmaDisciplinaPorMatriculaPeriodoTurmaDisciplina(obj.getMatriculaPeriodoTurmaDisciplinaVO());
			}
			removerMatriculaPeriodoTurmaDisciplinaPorHistorico(obj.getHistoricoAtualAluno());
			removerHistoricosDisciplinasAlunoCursandoGradeCurricular(obj.getHistoricoAtualAluno());
			if (obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplinaEquivale()) {
				MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(obj.getMatriculaPeriodoTurmaDisciplinaVO());
				MapaEquivalenciaDisciplinaVO mapaVisualizar = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getHistoricoAtualAluno().getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
				getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().removerGradeDisciplinaEquivalente(getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs(), obj);
				getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().removerMatriculaPeriodoTurmaDisciplinaEquivalenteCursada(matriculaPeriodoVO, obj.getDisciplina().getCodigo());
				getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().alterarSituacaoDisciplinaCursandoPorEquivalenciaAposRemocaoDisciplinaEquivale(getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs(), obj, mapaVisualizar);
			}
			obj.setSelecionado(false);
			if(Uteis.isAtributoPreenchido(obj.getHistoricoAtualAluno().getCodigo())){
				obj.setHistoricoAtualAluno(getFacadeFactory().getHistoricoFacade().consultarPorChavePrimaria(obj.getHistoricoAtualAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			}else{
				obj.setHistoricoAtualAluno(new HistoricoVO());
			}
			if(Uteis.isAtributoPreenchido(obj.getHistoricoAtualAluno().getMatriculaPeriodoTurmaDisciplina().getCodigo())){
				obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getHistoricoAtualAluno().getMatriculaPeriodoTurmaDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}else{
				obj.setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
			}
			obj.getGradeDisciplinaCompostaVOs().clear();
			setGradeDisciplinaVO(obj);
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(null);
			
			if(!obj.getHistoricosDuplicadosAluno().isEmpty()){
				Ordenacao.ordenarLista(obj.getHistoricosDuplicadosAluno(), "anoSemestreOrdenacao");
				obj.setHistoricoAtualAluno(obj.getHistoricosDuplicadosAluno().get(obj.getHistoricosDuplicadosAluno().size()-1));
				obj.setMatriculaPeriodoTurmaDisciplinaVO(obj.getHistoricoAtualAluno().getMatriculaPeriodoTurmaDisciplina());
			}else{
				for(HistoricoVO historicoVO: getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTodosHistoricosAlunoGradeCurricular()){
					if(historicoVO.getGradeDisciplinaVO().getCodigo().equals(obj.getCodigo())){
						obj.setHistoricoAtualAluno(historicoVO);
						obj.setMatriculaPeriodoTurmaDisciplinaVO(historicoVO.getMatriculaPeriodoTurmaDisciplina());
						break;
					}
				}
			}			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarMapaEquivalenciaParaVisualizacao() {
		try {
			setGradeDisciplinaVO((GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens"));			
			MapaEquivalenciaDisciplinaVO mapaVisualizar = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(getGradeDisciplinaVO().getHistoricoAtualAluno().getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
			prepararMapaEquivalenciaParaVisualizacao(mapaVisualizar, getGradeDisciplinaVO().getHistoricoAtualAluno().getNumeroAgrupamentoEquivalenciaDisciplina());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararMapaEquivalenciaParaVisualizacao(MapaEquivalenciaDisciplinaVO mapaVisualizar, Integer numeroAgrupamentoEquivalenciaDisciplina) throws Exception {
		getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().atualizarSituacaoMapaEquivalenciaDisciplinaAluno(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina, getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaEquivalenteCursadaVOs());
		setMapaEquivalenciaDisciplinaVisualizar(mapaVisualizar);
	}

	public String getAbrirModalAproveitamentoDisciplina() {
		return "RichFaces.$('panelIncluirDisciplinas').show()";
	}

	public void selecionarMapaDisciplinaEquivalenteAdicionar() {
		MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplina = (MapaEquivalenciaDisciplinaVO) context().getExternalContext().getRequestMap().get("mapaEquivalenciaDisciplinaItens");
		this.setCodigoMapaEquivalenciaDisciplinaVOIncluir(mapaEquivalenciaDisciplina.getCodigo());
		montarListaSelectItemDisciplinaEquivalenteAdicionar();
		if ((!getListaSelectItemDisciplinaEquivalenteAdicionar().isEmpty()) && (getListaSelectItemDisciplinaEquivalenteAdicionar().size() >= 2)) {
			// se só existe uma disciplina na lista a ser cursada, já vamos
			// setar esta disciplina
			// automaticamemte, evitando um clique a mais pelo usuários
			// (testamos 2, pois existem uma opcao em branco,
			// que sempre é listada.
			SelectItem opcaoUnica = (SelectItem) getListaSelectItemDisciplinaEquivalenteAdicionar().get(1); // opcao
			// 0,
			// é
			// a
			// linha
			// em
			// branco
			// do
			// combobox
			Integer codigoDisciplinaCursar = (Integer) opcaoUnica.getValue();
			setCodigoMapaEquivalenciaDisciplinaCursar(codigoDisciplinaCursar);
		}
		montarListaSelectItemTurmaAdicionar();
	}

	public void atualizarPeriodoLetivoPrevistoMatricula() {
		try {
			getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().realizarMontagemListaDisciplinasAproveitadas(getInclusaoHistoricoForaPrazoVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
			// List<HistoricoVO> historicosPrevitosAproveitamento =
			// getFacadeFactory().getAproveitamentoDisciplinaFacade().gerarHistoricosPrevistosDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(),
			// getUsuarioLogado());
			// getMatriculaVO().setHistoricosAproveitamentoDisciplinaPrevisto(historicosPrevitosAproveitamento);
			getFacadeFactory().getHistoricoFacade().atualizarDadosMatriculaComHistoricoAlunoVO(getMatriculaVO(), getMatriculaVO().getGradeCurricularAtual().getCodigo(), false, getMatriculaVO().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
			List<PeriodoLetivoVO> listaPeriodoValidosRenovacao = getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), getMatriculaVO().getMatriculaComHistoricoAlunoVO(), getUsuarioLogado());
			if (listaPeriodoValidosRenovacao.isEmpty()) {
				// se vazia adota-se o primeiro periodo como indicado para
				// matricula do aluno
				this.setPeriodoLetivoPrevistoMatricula(getMatriculaVO().getGradeCurricularAtual().getPrimeiroPeriodoLetivoGrade());
			} else {
				// adota-se o ultimo periodo da lista como sendo o indicado /
				// previsto para renovacao do aluno.
				this.setPeriodoLetivoPrevistoMatricula(listaPeriodoValidosRenovacao.get(listaPeriodoValidosRenovacao.size() - 1));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<TextoPadraoVO> consultarTextoPadraoPorTipo(String nomePrm) throws Exception {
		List<TextoPadraoVO> lista = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox(nomePrm, new UnidadeEnsinoVO(), "", false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemMapaEquivalenciaDisciplina() {
		List<MapaEquivalenciaDisciplinaVO> resultadoConsulta = null;
		Iterator<MapaEquivalenciaDisciplinaVO> i = null;
		try {
			if (!this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
				setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
				setCodigoMapaEquivalenciaDisciplinaCursar(0);
				setListaSelectItemMapaEquivalenciaDisciplinaIncluir(new ArrayList<MapaEquivalenciaDisciplinaVO>(0));
				montarListaSelectItemTurmaAdicionar();
				return;
			}
			// this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(null);
			resultadoConsulta = consultarMapaEquivalenciaDisciplina();
			List<MapaEquivalenciaDisciplinaVO> objs = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
			i = resultadoConsulta.iterator();
			while (i.hasNext()) {
				MapaEquivalenciaDisciplinaVO obj = (MapaEquivalenciaDisciplinaVO) i.next();
				objs.add(obj);
			}
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(objs);
			montarListaSelectItemDisciplinaEquivalenteAdicionar();
			montarListaSelectItemTurmaAdicionar();
			if (resultadoConsulta.isEmpty()) {
				throw new Exception("Não Existem Equivalências Disponíveis para Esta Disciplina (Mapas de Equivalências)");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<MapaEquivalenciaDisciplinaVO> consultarMapaEquivalenciaDisciplina() throws Exception {
		return getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(this.getMatriculaPeriodoVO().getGradeCurricular().getCodigo(), this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), NivelMontarDados.TODOS, false);
	}

	@SuppressWarnings("deprecation")
	public void montarListaSelectItemTurmaAdicionar() {
		limparDadosTurmaLiberacaoPreRequisitoChoqueHorario();
		if (this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo().equals(0)) {
			setListaSelectItemTurmaAdicionar(new ArrayList<TurmaVO>(0));
			return;
		}
		try {
			// Por default vamos buscar pela disciplina indica pelo usuário para inclusão
			Integer disciplinaConsultarTurma = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo();
			Integer cargaHorariaConsultarTurma = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getCargaHorariaDisciplina();
			if (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
				if (this.getCodigoMapaEquivalenciaDisciplinaCursar().equals(0)) {
					setListaSelectItemTurmaAdicionar(new ArrayList<TurmaVO>(0));
					return;
				}
				// Caso seja, por equivalencia, entao termos que buscar turmas para a disciplina equivalente selecionada.
				MapaEquivalenciaDisciplinaCursadaVO mapa = this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getMapaEquivalenciaDisciplinaVOIncluir().consultarObjMapaEquivalenciaDisciplinaCursadaVOPorCodigo(this.getCodigoMapaEquivalenciaDisciplinaCursar());
				this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setMapaEquivalenciaDisciplinaCursada(mapa);
				disciplinaConsultarTurma = mapa.getDisciplinaVO().getCodigo();
				cargaHorariaConsultarTurma = mapa.getCargaHoraria();
				if (disciplinaConsultarTurma == 0) {
					setListaSelectItemTurmaAdicionar(new ArrayList<TurmaVO>(0));
					return;
				}
			}
			boolean validarTurmaComAulaProgramadaCursoIntegral = (getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada() && 
					!getPermissaoFuncionalidadeUsuarioLogado().test(PerfilAcessoPermissaoAcademicoEnum.PERMITIR_INCLUIR_DISCIPLINA_SEM_AULA_PROGRAMADA_EM_CURSOS_INTEGRAIS.getValor())) || getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada();
			
			setListaSelectItemTurmaAdicionar(getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(disciplinaConsultarTurma, cargaHorariaConsultarTurma, 
					0, "AB", false, 0, getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), 
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre(), true, 
					getFiltrarTurmaAulaNaoOcorreu(), getInclusaoHistoricoForaPrazoVO().getReposicao(), true, false, getUsuarioLogado(), 
					validarTurmaComAulaProgramadaCursoIntegral));
			
			if (!Uteis.isAtributoPreenchido(getListaSelectItemTurmaAdicionar()) && ((!getApresentarAno() && !getApresentarSemestre())
					|| (getApresentarAno() && !getApresentarSemestre() &&  !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().trim().isEmpty())
					|| (getApresentarSemestre() &&  !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().trim().isEmpty() && !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().trim().isEmpty())
					)) {
				throw new Exception("Não existe nenhuma turma (Aberta) disponível para esta inclusão desta disciplina.");
			}
			limparMensagem();
		} catch (Exception e) {
			setListaSelectItemTurmaAdicionar(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurmaIncluirDisciplina() {
		try {
			TurmaVO turmaSelecionada = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaIncluirDisciplinaItens");
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaTurmaDisciplinaAnoSemestre(getMatriculaVO().getMatricula(), turmaSelecionada, getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre(), getMatrizCurricular(), false, false, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
				String periodo = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno() + (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre() == "" ? "" : "/" + getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre());
				throw new Exception(UteisJSF.internacionalizar("msg_InclusaoExclusaoDiscipla_alunoJaCursouDisciplinaTurmaAnoSemestre").replace("{0}", getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getNome()).replace("{1}", periodo).replace("{2}", turmaSelecionada.getIdentificadorTurma()));
			}
			getFacadeFactory().getMatriculaPeriodoFacade().verificarDisciplinaCursandoMapaEquivalencia(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina(), getMapMatriculaPeriodoVOs().values().stream().map(MatriculaPeriodoVO::getMatriculaPeriodoTumaDisciplinaVOs).flatMap(Collection::stream).collect(Collectors.toList()));
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoChoqueHorarioDisciplina("");
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoPreRequisitoDisciplina("");
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(turmaSelecionada);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurmaPratica(null);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurmaTeorica(null);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setSugestaoTurmaPraticaTeoricaRealizada(false);					
			TurmaDisciplinaVO turDis = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia() ? getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getMapaEquivalenciaDisciplinaCursada().getDisciplinaVO().getCodigo() : getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), false, getUsuarioLogado());
			if (turDis.getCodigo().intValue() > 0) {
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setModalidadeDisciplina(turDis.getModalidadeDisciplina());				
			}
			MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = null;
			if (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
				mapaEquivalenciaDisciplinaCursadaVO = getFacadeFactory().getMapaEquivalenciaDisciplinaCursadaFacade().consultarPorChavePrimaria(getCodigoMapaEquivalenciaDisciplinaCursar());
			}
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarSugestaoTurmaPraticaTeorica(matriculaPeriodoVO, getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia() ? mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO() : getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina(), getMatriculaVO().getCurso().getConfiguracaoAcademico(), getHorarioAlunoTurnoVOs(), getInclusaoHistoricoForaPrazoVO().getReposicao(), getUsuarioLogado());
			
			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaPratica().getCodigo()) && !Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaTeorica().getCodigo())) {
				if (this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
					getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, getHorarioAlunoTurnoVOs(), matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaPratica().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaTeorica().getCodigo(), mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
				} else {
					if (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals(getMatriculaPeriodoVO().getAno()) 
							&& getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().equals(getMatriculaPeriodoVO().getSemestre())) {
						getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, getHorarioAlunoTurnoVOs(), matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), turmaSelecionada.getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaPratica().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaTeorica().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
					} else {
						List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaAnoSemestreAdicionarVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestre(getMatriculaVO().getMatricula(), 0, 0, getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre(), false, false, false, false, false, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
						getFacadeFactory().getHorarioAlunoFacade().realizarVerificaoChoqueHorarioPorMatriculaPeriodoTurmaDisciplinaVOs(matriculaPeriodoVO, getHorarioAlunoTurnoVOs(), listaMatriculaPeriodoTurmaDisciplinaAnoSemestreAdicionarVOs, turmaSelecionada.getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaPratica().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurmaTeorica().getCodigo(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo(), getUsuarioLogado(), true, getMatriculaVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
					}
				}
			}
			limparMensagem();
		} catch (Exception e) {
			if (e instanceof ConsistirException && ((ConsistirException) e).getReferenteChoqueHorario()) {
				if(e.getMessage() == null){
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoChoqueHorarioDisciplina(((ConsistirException) e).getToStringMensagemErro());
				}else{
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoChoqueHorarioDisciplina(((ConsistirException) e).getMessage());
				}
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaChoqueHorario(false);
			} else if (e instanceof ConsistirException && ((ConsistirException) e).getReferentePreRequisito()) {
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaPreRequisito(false);
			} else{
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		// realizarMontagemQuadroHorarioTurmaDisciplina();
	}

	public void prepararAdicionarDisciplinaMatriculaPeriodo() {
		try {
			setGradeDisciplinaVO((GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens"));
			getGradeDisciplinaVO().setSelecionado(true);
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(getGradeDisciplinaVO().getDisciplina());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(getGradeDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDataRegistroHistorico(getMatriculaPeriodoVO().getData());
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar();
			if (getHorarioAlunoTurnoVOs().isEmpty()) {
				if (getMatriculaVO().getCurso().getIntegral()) {
					prepararHorarioAulaAluno();
				}
			} else {
				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
			}
			// realizarMontagemQuadroHorarioTurmaDisciplina();
			setChoqueHorarioLiberado(false);
			limparMensagem();
			setMensagemID("");
		} catch (Exception e) {
			getGradeDisciplinaVO().setSelecionado(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getApresentarBotaoAtualizarHorarioAluno() {
		if (getMatriculaVO().getCurso().getSemestral()) {
			return !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals("") && !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().equals("");
		}
		if (getMatriculaVO().getCurso().getAnual()) {
			return !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals("");
		}
		return false;
	}

	public void atualizarHorarioAlunoDeAcordoComPeriodo() {
		try {
			MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				prepararHorarioAulaAluno();
			}
			if(getHorarioAlunoTurnoVOs().isEmpty()) {
				setMensagemID("Sem horário para o aluno ( " + getMatriculaVO().getAluno().getNome() + " )", Uteis.ALERTA, true);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void prepararHorarioAulaAluno() {
		try {
			prepararHorarioAulaAluno(getMatriculaPeriodoVO());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void prepararHorarioAulaAluno(MatriculaPeriodoVO obj) throws Exception {
		setHorarioAlunoTurnoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarHorarioAlunoPorMatriculaPeriodoDisciplina(obj.getMatriculaPeriodoTumaDisciplinaVOs(), getUsuarioLogado()));
		limparMensagem();
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusaoLiberandoChoqueHorario() {
		try {
			UsuarioVO usuarioVerif = getUsuarioLibercaoChoqueHorario();
			usuarioVerif = ControleAcesso.verificarLoginUsuario(usuarioVerif.getUsername(), usuarioVerif.getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoExclusaoDisciplina_permitirIncluirDisciplinaChoqueHorario", usuarioVerif);
			} catch (Exception e) {
				throw new Exception("Usuário informado não possui permissão para liberar a inclusão de disciplina com choque de horário, verifique as permissções de acesso do usuário!");
			}
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setUsuarioLiberacaoChoqueHorario(usuarioVerif);
			setChoqueHorarioLiberado(true);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaChoqueHorario(true);
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaChoqueHorario(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaPreRequisito());
		} catch (Exception e) {
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaChoqueHorario(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusaoLiberandoPreRequisito() {
		try {
			UsuarioVO usuarioVerif = getUsuarioLibercaoPreRequisito();
			usuarioVerif = ControleAcesso.verificarLoginUsuario(usuarioVerif.getUsername(), usuarioVerif.getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("InclusaoExclusaoDisciplina_permitirIncluirDisciplinaPreRequisito", usuarioVerif);
			} catch (Exception e) {
				throw new Exception("Usuário informado não possui permissão para liberar a inclusão de disciplina com pré-requisito, verifique as permissões de acesso do usuário!");
			}
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setUsuarioLiberacaoPreRequisito(usuarioVerif);
			setPreRequisitoLiberado(true);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaPreRequisito(true);
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaChoqueHorario(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaPreRequisito());
		} catch (Exception e) {
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setLiberarInclusaoDisciplinaPreRequisito(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao() {
		try {
			adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaChoqueHorario(), getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getLiberarInclusaoDisciplinaPreRequisito());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarMatriculaPeriodoTurmaDisciplinaPorInclusao(Boolean liberarChoqueHorario, Boolean liberarPreRequisito) {
		try {
			if (!getRequerimentoVO().getCodigo().equals(0)) {
				if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo().equals(getRequerimentoVO().getDisciplina().getCodigo())) {
					throw new Exception("Deve ser incluída a Disciplina vinculada ao Requerimento ("+getRequerimentoVO().getDisciplina().getCodigo()+" - "+getRequerimentoVO().getDisciplina().getNome()+").");
				}
			}
			if (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo().equals(0)) {
				throw new Exception("Selecionar uma Turma para incluir a disciplina.");
			}
			MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
			matriculaPeriodoVO.setMensagemAvisoUsuario(new ConsistirException());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().realizarDefinicaoConfiguracaoAcademicaVincularHistoricoAluno(getMatriculaVO().getCurso(), 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaVO(), getMatriculaPeriodoTumaDisciplinaVO().getGradeCurricularGrupoOptativaDisciplinaVO(), 
					getMatriculaPeriodoTumaDisciplinaVO().getGradeDisciplinaCompostaVO(), getMatriculaPeriodoTumaDisciplinaVO().getTurma(), getMatriculaPeriodoTumaDisciplinaVO().getAno(), getMatriculaPeriodoTumaDisciplinaVO().getSemestre(), getUsuarioLogado()));

			getFacadeFactory().getMatriculaPeriodoFacade().verificarDisciplinaCursandoMapaEquivalencia(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina(), getMapMatriculaPeriodoVOs().values().stream().map(MatriculaPeriodoVO::getMatriculaPeriodoTumaDisciplinaVOs).flatMap(Collection::stream).collect(Collectors.toList()));
//			MapaEquivalenciaDisciplinaVO mapaEquivalencia = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(this.getCodigoMapaEquivalenciaDisciplinaVOIncluir(), NivelMontarDados.TODOS);
//			validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(this.getMatriculaVO(), getMapMatriculaPeriodoVOs().values(), mapaEquivalencia);
			getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoVO, getMatriculaVO(), getGradeDisciplinaVO(), getGradeDisciplinaCompostaSelecionadaVO(), getGradeCurricularGrupoOptativaDisciplinaVO(), liberarPreRequisito, getMatriculaPeriodoTurmaDisciplinaVOAdicionar(), getHorarioAlunoTurnoVOs(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getConfiguracaoFinanceiroPadraoSistema(), getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(), liberarChoqueHorario, getUsuarioLogado(), getMatriculaPeriodoVO().getGradeCurricular(),getMatriculaPeriodoVO().getReposicao(), false);
//			if (getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
//				for (MapaEquivalenciaDisciplinaMatrizCurricularVO medmcVO : getMapaEquivalenciaDisciplinaVisualizar().getMapaEquivalenciaDisciplinaMatrizCurricularVOs()) {
//					medmcVO.getHistorico().setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
//				}
//			}
			prepararHorarioAulaAluno();
			if (TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(getGradeDisciplinaVO().getTipoControleComposicao())) {
				getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(getFacadeFactory().getMatriculaPeriodoFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO));
				setOnCompleteIncluirDisciplinaTurma("RichFaces.$('panelIncluirDisciplinas').hide(); RichFaces.$('panelDisciplinasComposta').show();");
			} else {
				setOnCompleteIncluirDisciplinaTurma("RichFaces.$('panelIncluirDisciplinas').hide();");
			}
			if (this.getMatriculaPeriodoVO().getReposicao()) {
				Integer codDisciplina = 0;
				Integer codGradeDisciplina = 0;
				Integer codTurma = 0;
				if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getNovoObj() && !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
					codDisciplina = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getDisciplina().getCodigo();
					codTurma = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo();
					codGradeDisciplina = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getCodigo();
				} else if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getNovoObj() && getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaPorEquivalencia()) {
					@SuppressWarnings("deprecation")
					MapaEquivalenciaDisciplinaCursadaVO mapa = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getMapaEquivalenciaDisciplinaVOIncluir().consultarObjMapaEquivalenciaDisciplinaCursadaVOPorCodigo(this.getCodigoMapaEquivalenciaDisciplinaCursar());
					codDisciplina = mapa.getDisciplinaVO().getCodigo();
					codTurma = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo();
					// codGradeDisciplina =
					// getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getCodigo();
				} else {
					codDisciplina = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo();
					codTurma = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo();
					codGradeDisciplina = getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO().getCodigo();
				}
				String msg = getFacadeFactory().getTurmaFacade().verificaQtdAlunoAtivoPreReposicao(codTurma, codGradeDisciplina, codDisciplina);
				setApresentarPopUpReposicao(Boolean.TRUE);
				setMsgPopUpReposicao(msg);
			} else {
				setApresentarPopUpReposicao(Boolean.FALSE);
				setMsgPopUpReposicao("");
			}
			realizarAtualizacaoGradeDisciplinaRequerimentoAdicionada(getGradeDisciplinaVO());
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			setMensagemID("msg_dados_incluidos");
		} catch (Exception e) {
//			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(null);
			if (e instanceof ConsistirException) {
				ConsistirException c = (ConsistirException) e;
				if (c.getReferenteChoqueHorario()) {
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setPermitirUsuarioTentarInclusaoComChoqueHorario(true);
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoChoqueHorarioDisciplina(c.getToStringMensagemErro());
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoPreRequisitoDisciplina(null);			
				}else if (c.getReferentePreRequisito()) {
					getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoPreRequisitoDisciplina(e.getMessage());
				}
				setConsistirExceptionMensagemDetalhada("msg_erro", c, Uteis.ERRO);
				((ConsistirException) e).getListaMensagemErro().clear();
			}else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			setApresentarPopUpReposicao(Boolean.FALSE);
			setOnCompleteIncluirDisciplinaTurma("");
			setChoqueHorarioLiberado(false);
	  //  	setPreRequisitoLiberado(false);
			getGradeDisciplinaVO().setSelecionado(false);
		}
	}

	public void prepararDisciplinaMapaEquivalenciaParaInclusao() {
		MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
		getListaSelectItemMapaEquivalenciaDisciplinaIncluir().clear();
		MatriculaPeriodoTurmaDisciplinaVO novoObj = new MatriculaPeriodoTurmaDisciplinaVO();
		novoObj.setBloquarAlteracaoMapaEquivalenciaDisciplinaCursada(true);
		novoObj.setMapaEquivalenciaDisciplinaVOIncluir(getMapaEquivalenciaDisciplinaVisualizar());
		novoObj.setMapaEquivalenciaDisciplinaCursada(mapaEquivalenciaDisciplinaCursadaVO);
		novoObj.setDisciplinaPorEquivalencia(true);
		novoObj.setDisciplina(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO());
		novoObj.setTurma(new TurmaVO());
		// setando carga horaria somente para apresentacao ao usuario
		novoObj.getGradeDisciplinaVO().setCargaHoraria(mapaEquivalenciaDisciplinaCursadaVO.getCargaHoraria());
		// definindo mapa de equivalencia
		setCodigoMapaEquivalenciaDisciplinaVOIncluir(getMapaEquivalenciaDisciplinaVisualizar().getCodigo());

		// inicializando lista de Mapas somanete com a opção válida do mapa que
		// está sendo referenciado.
		getListaSelectItemMapaEquivalenciaDisciplinaIncluir().add(getMapaEquivalenciaDisciplinaVisualizar());

		// definindo disciplina será cursada
		montarListaSelectItemDisciplinaEquivalenteAdicionar();
		setCodigoMapaEquivalenciaDisciplinaCursar(mapaEquivalenciaDisciplinaCursadaVO.getCodigo());
		// getMatriculaPeriodoVO().getPeriodoLetivo().getNumeroMaximoCreditoAlunoPodeCursar();

		setMensagemID("msg_informe_dados");

		// montar depois de setar a mensagem, pois ao montar a turma, pode-se
		// gerar mensagens ao usuário
		setMatriculaPeriodoTurmaDisciplinaVOAdicionar(novoObj);
		montarListaSelectItemTurmaAdicionar();
	}

	public Boolean getApresentarAno() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals("SE") || getMatriculaVO().getCurso().getPeriodicidade().equals("AN");
	}

	public Boolean getApresentarSemestre() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public Boolean getParcelaReposicaoNaoGravada() {
		if (parcelaReposicaoNaoGravada == null) {
			parcelaReposicaoNaoGravada = true;
		}
		return parcelaReposicaoNaoGravada;
	}

	public void setParcelaReposicaoNaoGravada(Boolean parcelaReposicaoNaoGravada) {
		this.parcelaReposicaoNaoGravada = parcelaReposicaoNaoGravada;
	}

	public List<SelectItem> getListaSelectItemInclusaoDisciplinasForaDoPrazo() {
		return listaSelectItemInclusaoDisciplinasForaDoPrazo;
	}

	public void setListaSelectItemInclusaoDisciplinasForaDoPrazo(List<SelectItem> listaSelectItemInclusaoDisciplinasForaDoPrazo) {
		this.listaSelectItemInclusaoDisciplinasForaDoPrazo = listaSelectItemInclusaoDisciplinasForaDoPrazo;
	}

	public String getObservacaoJustificativa() {
		if (observacaoJustificativa == null) {
			observacaoJustificativa = "";
		}
		return observacaoJustificativa;
	}

	public void setObservacaoJustificativa(String observacaoJustificativa) {
		this.observacaoJustificativa = observacaoJustificativa;
	}

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public List<SelectItem> getListaPlanoFinanceiroReposicao() {
		if (listaPlanoFinanceiroReposicao == null) {
			listaPlanoFinanceiroReposicao = new ArrayList<SelectItem>(0);
		}
		return listaPlanoFinanceiroReposicao;
	}

	public void setListaPlanoFinanceiroReposicao(List<SelectItem> listaPlanoFinanceiroReposicao) {
		this.listaPlanoFinanceiroReposicao = listaPlanoFinanceiroReposicao;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public Boolean getApresentarComboboxReposicaoInclusao() {
		if (getMatriculaVO().getAluno().getCodigo() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoContrato() {
		if (listaSelectItemTextoPadraoContrato == null) {
			listaSelectItemTextoPadraoContrato = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoPadraoContrato;
	}

	public void setListaSelectItemTextoPadraoContrato(List<SelectItem> listaSelectItemTextoPadraoContrato) {
		this.listaSelectItemTextoPadraoContrato = listaSelectItemTextoPadraoContrato;
	}

	public TextoPadraoVO getTextoPadraoContratoInclusao() {
		if (textoPadraoContratoInclusao == null) {
			textoPadraoContratoInclusao = new TextoPadraoVO();
		}
		return textoPadraoContratoInclusao;
	}

	public void setTextoPadraoContratoInclusao(TextoPadraoVO textoPadraoContratoInclusao) {
		this.textoPadraoContratoInclusao = textoPadraoContratoInclusao;
	}

	public Boolean getApresentarCampoDesconto() throws Exception {
		if (getLoginControle().getPermissaoAcessoMenuVO().getDescontoInclusaoReposicaoForaPrazo()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public InclusaoHistoricoForaPrazoVO getInclusaoHistoricoForaPrazoVO() {
		if (inclusaoHistoricoForaPrazoVO == null) {
			inclusaoHistoricoForaPrazoVO = new InclusaoHistoricoForaPrazoVO();
		}
		return inclusaoHistoricoForaPrazoVO;
	}

	public void setInclusaoHistoricoForaPrazoVO(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO) {
		this.inclusaoHistoricoForaPrazoVO = inclusaoHistoricoForaPrazoVO;
	}

	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
		getControleConsulta().setCampoConsulta("matricula");
		getControleConsulta().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsulta().setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoExclusaoDisciplinaMatriculaCons.xhtml");
	}

	public Boolean getEditandoInclusaoHistoricoForaPrazo() {
		if (editandoInclusaoHistoricoForaPrazo == null) {
			editandoInclusaoHistoricoForaPrazo = false;
		}
		return editandoInclusaoHistoricoForaPrazo;
	}

	public void setEditandoInclusaoHistoricoForaPrazo(Boolean editandoInclusaoHistoricoForaPrazo) {
		this.editandoInclusaoHistoricoForaPrazo = editandoInclusaoHistoricoForaPrazo;
	}

	public boolean getIsParcelaReposicaoNaoGravadaNaoEditandoInclusaoHistoricoForaPrazo() {
		// if (getParcelaReposicaoNaoGravada() &&
		// getEditandoInclusaoHistoricoForaPrazo()) {
		if (getParcelaReposicaoNaoGravada()) {
			return true;
		}
		return false;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataInclusao() {
		if (dataInclusao == null) {
			dataInclusao = new Date();
		}
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getDataInclusao_Apresentar() {
		return (Uteis.getData(getDataInclusao(), "dd/MM/yyyy"));
	}

	public Boolean getApresentarPopUpReposicao() {
		if (apresentarPopUpReposicao == null) {
			apresentarPopUpReposicao = Boolean.FALSE;
		}
		return apresentarPopUpReposicao;
	}

	public void setApresentarPopUpReposicao(Boolean apresentarPopUpReposicao) {
		this.apresentarPopUpReposicao = apresentarPopUpReposicao;
	}

	public String getMsgPopUpReposicao() {
		if (msgPopUpReposicao == null) {
			msgPopUpReposicao = "";
		}
		return msgPopUpReposicao;
	}

	public void setMsgPopUpReposicao(String msgPopUpReposicao) {
		this.msgPopUpReposicao = msgPopUpReposicao;
	}

	public PeriodoLetivoVO getPeriodoLetivoGrupoOptativaVO() {
		if (periodoLetivoGrupoOptativaVO == null) {
			periodoLetivoGrupoOptativaVO = new PeriodoLetivoVO();
		}
		return periodoLetivoGrupoOptativaVO;
	}

	public void setPeriodoLetivoGrupoOptativaVO(PeriodoLetivoVO periodoLetivoGrupoOptativaVO) {
		this.periodoLetivoGrupoOptativaVO = periodoLetivoGrupoOptativaVO;
	}

	public Integer getCodigoPeriodoLetivoGrupoOptativa() {
		if (codigoPeriodoLetivoGrupoOptativa == null) {
			codigoPeriodoLetivoGrupoOptativa = 0;
		}
		return codigoPeriodoLetivoGrupoOptativa;
	}

	public void setCodigoPeriodoLetivoGrupoOptativa(Integer codigoPeriodoLetivoGrupoOptativa) {
		this.codigoPeriodoLetivoGrupoOptativa = codigoPeriodoLetivoGrupoOptativa;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivoComGrupoOptativas() {
		if (listaSelectItemPeriodoLetivoComGrupoOptativas == null) {
			listaSelectItemPeriodoLetivoComGrupoOptativas = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	public void setListaSelectItemPeriodoLetivoComGrupoOptativas(List<SelectItem> listaSelectItemPeriodoLetivoComGrupoOptativas) {
		this.listaSelectItemPeriodoLetivoComGrupoOptativas = listaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public Boolean getPanelAproveitamentoDisciplinaAberto() {
		if (panelAproveitamentoDisciplinaAberto == null) {
			panelAproveitamentoDisciplinaAberto = Boolean.FALSE;
		}
		return panelAproveitamentoDisciplinaAberto;
	}

	public void setPanelAproveitamentoDisciplinaAberto(Boolean panelAproveitamentoDisciplinaAberto) {
		this.panelAproveitamentoDisciplinaAberto = panelAproveitamentoDisciplinaAberto;
	}

	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVisualizar() {
		if (mapaEquivalenciaDisciplinaVisualizar == null) {
			mapaEquivalenciaDisciplinaVisualizar = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVisualizar;
	}

	public void setMapaEquivalenciaDisciplinaVisualizar(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar) {
		this.mapaEquivalenciaDisciplinaVisualizar = mapaEquivalenciaDisciplinaVisualizar;
	}

	public PeriodoLetivoVO getPeriodoLetivoPrevistoMatricula() {
		if (periodoLetivoPrevistoMatricula == null) {
			periodoLetivoPrevistoMatricula = new PeriodoLetivoVO();
		}
		return periodoLetivoPrevistoMatricula;
	}

	public void setPeriodoLetivoPrevistoMatricula(PeriodoLetivoVO periodoLetivoPrevistoMatricula) {
		this.periodoLetivoPrevistoMatricula = periodoLetivoPrevistoMatricula;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVOAdicionar() {
		if (matriculaPeriodoTurmaDisciplinaVOAdicionar == null) {
			matriculaPeriodoTurmaDisciplinaVOAdicionar = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVOAdicionar;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOAdicionar(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar) {
		this.matriculaPeriodoTurmaDisciplinaVOAdicionar = matriculaPeriodoTurmaDisciplinaVOAdicionar;
	}

	public Integer getCodigoMapaEquivalenciaDisciplinaVOIncluir() {
		if (codigoMapaEquivalenciaDisciplinaVOIncluir == null) {
			codigoMapaEquivalenciaDisciplinaVOIncluir = 0;
		}
		return codigoMapaEquivalenciaDisciplinaVOIncluir;
	}

	public void setCodigoMapaEquivalenciaDisciplinaVOIncluir(Integer codigoMapaEquivalenciaDisciplinaVOIncluir) {
		this.codigoMapaEquivalenciaDisciplinaVOIncluir = codigoMapaEquivalenciaDisciplinaVOIncluir;
	}

	public Integer getCodigoMapaEquivalenciaDisciplinaCursar() {
		if (codigoMapaEquivalenciaDisciplinaCursar == null) {
			codigoMapaEquivalenciaDisciplinaCursar = 0;
		}
		return codigoMapaEquivalenciaDisciplinaCursar;
	}

	public void setCodigoMapaEquivalenciaDisciplinaCursar(Integer codigoMapaEquivalenciaDisciplinaCursar) {
		this.codigoMapaEquivalenciaDisciplinaCursar = codigoMapaEquivalenciaDisciplinaCursar;
	}

	public List<MapaEquivalenciaDisciplinaVO> getListaSelectItemMapaEquivalenciaDisciplinaIncluir() {
		if (listaSelectItemMapaEquivalenciaDisciplinaIncluir == null) {
			listaSelectItemMapaEquivalenciaDisciplinaIncluir = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		}
		return listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	public void setListaSelectItemMapaEquivalenciaDisciplinaIncluir(List<MapaEquivalenciaDisciplinaVO> listaSelectItemMapaEquivalenciaDisciplinaIncluir) {
		this.listaSelectItemMapaEquivalenciaDisciplinaIncluir = listaSelectItemMapaEquivalenciaDisciplinaIncluir;
	}

	public List<TurmaVO> getListaSelectItemTurmaAdicionar() {
		if (listaSelectItemTurmaAdicionar == null) {
			listaSelectItemTurmaAdicionar = new ArrayList<TurmaVO>(0);
		}
		return listaSelectItemTurmaAdicionar;
	}

	public void setListaSelectItemTurmaAdicionar(List<TurmaVO> listaSelectItemTurmaAdicionar) {
		this.listaSelectItemTurmaAdicionar = listaSelectItemTurmaAdicionar;
	}

	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
		if (horarioAlunoTurnoVOs == null) {
			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
		}
		return horarioAlunoTurnoVOs;
	}

	public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
		this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
	}

	public String getOnCompleteIncluirDisciplinaTurma() {
		if (onCompleteIncluirDisciplinaTurma == null) {
			onCompleteIncluirDisciplinaTurma = "RichFaces.$('panelIncluirDisciplinas').hide();";
		}
		return onCompleteIncluirDisciplinaTurma;
	}

	public void setOnCompleteIncluirDisciplinaTurma(String onCompleteIncluirDisciplinaTurma) {
		this.onCompleteIncluirDisciplinaTurma = onCompleteIncluirDisciplinaTurma;
	}

	public HashMap<String, MatriculaPeriodoVO> getMapMatriculaPeriodoVOs() {
		if (mapMatriculaPeriodoVOs == null) {
			mapMatriculaPeriodoVOs = new HashMap<String, MatriculaPeriodoVO>(0);
		}
		return mapMatriculaPeriodoVOs;
	}

	public void setMapMatriculaPeriodoVOs(HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs) {
		this.mapMatriculaPeriodoVOs = mapMatriculaPeriodoVOs;
	}

	public Integer getCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas() {
		if (codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas == null) {
			codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas = 0;
		}
		return codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas;
	}

	/**
	 * @param codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas
	 *            the codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas to set
	 */
	public void setCodigoPeriodoLetivoIncluirDisciplinasGrupoOptativas(Integer codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas) {
		this.codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas = codigoPeriodoLetivoIncluirDisciplinasGrupoOptativas;
	}

	public PeriodoLetivoComHistoricoAlunoVO getPeriodoLetivoComHistoricoVOIncluirGrupoOptativas() {
		if (periodoLetivoComHistoricoVOIncluirGrupoOptativas == null) {
			periodoLetivoComHistoricoVOIncluirGrupoOptativas = new PeriodoLetivoComHistoricoAlunoVO();
		}
		return periodoLetivoComHistoricoVOIncluirGrupoOptativas;
	}

	public void setPeriodoLetivoComHistoricoVOIncluirGrupoOptativas(PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoVOIncluirGrupoOptativas) {
		this.periodoLetivoComHistoricoVOIncluirGrupoOptativas = periodoLetivoComHistoricoVOIncluirGrupoOptativas;
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

	public DisciplinaVO getDisciplinaForaGradeVO() {
		if (disciplinaForaGradeVO == null) {
			disciplinaForaGradeVO = new DisciplinaVO();
		}
		return disciplinaForaGradeVO;
	}

	public void setDisciplinaForaGradeVO(DisciplinaVO disciplinaForaGradeVO) {
		this.disciplinaForaGradeVO = disciplinaForaGradeVO;
	}

	public ConfiguracaoAcademicoVO carregarDadosConfiguracaoAcademico(Integer configuracaoAcademico) throws Exception {
		if (getMapConfiguracaoAcademivoVOs().containsKey(configuracaoAcademico)) {
			return getMapConfiguracaoAcademivoVOs().get(configuracaoAcademico);
		}
		if (!configuracaoAcademico.equals(0)) {
			ConfiguracaoAcademicoVO obj = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracaoAcademico, getUsuarioLogado());
			getMapConfiguracaoAcademivoVOs().put(configuracaoAcademico, obj);
			return obj;
		}
		return getMatriculaVO().getCurso().getConfiguracaoAcademico();
	}

	public void verificarDadosExclusaoDisciplinaAluno() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		try {
			obj.getHistoricoAtualAluno().setConfiguracaoAcademico(carregarDadosConfiguracaoAcademico(obj.getHistoricoAtualAluno().getConfiguracaoAcademico().getCodigo()));
			setMensagemMediaFrequencia(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemMediaFrequencia(obj.getHistoricoAtualAluno(), getUsuarioLogado()));
			setMensagemNotaLancada(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemNotaLancada(obj.getHistoricoAtualAluno(), obj.getHistoricoAtualAluno().getConfiguracaoAcademico(), getUsuarioLogado()));
			setMensagemRegistroAulaLancado(getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorDisciplinaMatricula(getMatriculaVO().getMatricula(), obj.getDisciplina().getCodigo(), getUsuarioLogado()));
			setHistoricoVO(obj.getHistoricoAtualAluno());
			setGradeDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirDisciplinaAluno() {
		String situacaoHistoricoTemporario = getHistoricoVO().getSituacao();
		try {
			//getFacadeFactory().getEstagioFacade().validarDadosDisciplinaIncluidaEmEstagio(getHistoricoVO().getMatricula().getMatricula(), getHistoricoVO().getDisciplina().getCodigo(), getHistoricoVO().getAnoHistorico(), getHistoricoVO().getSemestreHistorico(), getUsuarioLogado());
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().excluirDisciplinaAlunoPorDisciplina(getHistoricoVO(), getRealizandoExclusaoDisciplinaForaPrazo(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			// realizarMontagemPainelMatrizCurricular();
			for(HistoricoVO historicoVO: historicoVOs){
				executarRemocaoHistoricoDisciplinaExclusao(historicoVO, true);
			}
			setHistoricoVO(new HistoricoVO());
			setRealizandoExclusaoHistoricoDuplicadoAluno(false);
			setFecharModalConfirmacaoExclusaoDisciplina(Boolean.TRUE);
//			getGradeDisciplinaVO().setHistoricoAtualAluno(new HistoricoVO());
//			getGradeDisciplinaVO().setMatriculaPeriodoTurmaDisciplinaVO(new MatriculaPeriodoTurmaDisciplinaVO());
//			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			setMapaEquivalenciaDisciplinaVisualizar(new MapaEquivalenciaDisciplinaVO());
			prepararHorarioAulaAluno(getMatriculaPeriodoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setFecharModalConfirmacaoExclusaoDisciplina(Boolean.FALSE);
			getHistoricoVO().setSituacao(situacaoHistoricoTemporario);
		}
	}

	public String getOncompleteMensagemConfirmacaoExclusaoDisciplina() {
		if (getFecharModalConfirmacaoExclusaoDisciplina()) {
			return "RichFaces.$('panelAvisoExclusaoDisciplina').hide(), RichFaces.$('panelHistoricosDuplicadosAluno').hide(), RichFaces.$('panelMapaEquivalencia').hide(), RichFaces.$('panelHistoricoDisciplinaForaGrade').hide()";
		}
		return "";
	}

	public void inicializarHistoricoExcluidoPorDisciplina() throws Exception {
		if (!getHistoricoVO().getHistoricoDisciplinaForaGrade()) {
			Integer codigoDisciplinaExcluida = getGradeDisciplinaVO().getDisciplina().getCodigo();
			removerDisciplinaMatriculaComHistoricoCursando(codigoDisciplinaExcluida);
			HistoricoVO historicoVO = new HistoricoVO();
			historicoVO.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
			getGradeDisciplinaVO().setHistoricoAtualAluno(historicoVO);
		} else {
			removerDisciplinaMatriculaComHistoricoCursando(getHistoricoVO().getDisciplina().getCodigo());
			removerDisciplinaListaHistoricoDisciplinaForaGrade(getHistoricoVO());
		}
	}

	public void removerDisciplinaListaHistoricoDisciplinaForaGrade(HistoricoVO obj) {
		int index = 0;
		for (HistoricoVO objExcluir : getListaHistoricoDisciplinaForaGradeVOs()) {
			if (objExcluir.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getListaHistoricoDisciplinaForaGradeVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void inicializarListaHistoricoDisciplinaForaGrade() {
		setListaHistoricoDisciplinaForaGradeVOs(getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasForaGradeCurricular());
	}

	public void selecionarDisciplinaForaGradeParaExclusao() {
		try {
			HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoForaGradeItens");
			obj.setConfiguracaoAcademico(carregarDadosConfiguracaoAcademico(obj.getConfiguracaoAcademico().getCodigo()));
			setMensagemMediaFrequencia(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemMediaFrequencia(obj, getUsuarioLogado()));
			setMensagemNotaLancada(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemNotaLancada(obj, obj.getConfiguracaoAcademico(), getUsuarioLogado()));
			setMensagemRegistroAulaLancado(getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorDisciplinaMatricula(getMatriculaVO().getMatricula(), obj.getDisciplina().getCodigo(), getUsuarioLogado()));
			obj.setHistoricoDisciplinaForaGrade(Boolean.TRUE);
			setHistoricoVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerDisciplinaMatriculaComHistoricoCursando(Integer codigoDisciplinaExcluida) {
		MatriculaComHistoricoAlunoVO matriculaComHistorico = matriculaVO.getMatriculaComHistoricoAlunoVO();
		int index = 0;
		for (HistoricoVO obj : matriculaComHistorico.getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular()) {
			if (obj.getDisciplina().getCodigo().equals(codigoDisciplinaExcluida)) {
				matriculaComHistorico.getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular().remove(index);
				break;
			}
			index++;
		}
	}

	public String getMensagemMediaFrequencia() {
		if (mensagemMediaFrequencia == null) {
			mensagemMediaFrequencia = "";
		}
		return mensagemMediaFrequencia;
	}

	public void setMensagemMediaFrequencia(String mensagemMediaFrequencia) {
		this.mensagemMediaFrequencia = mensagemMediaFrequencia;
	}

	public String getMensagemNotaLancada() {
		if (mensagemNotaLancada == null) {
			mensagemNotaLancada = "";
		}
		return mensagemNotaLancada;
	}

	public void setMensagemNotaLancada(String mensagemNotaLancada) {
		this.mensagemNotaLancada = mensagemNotaLancada;
	}

	public String getMensagemRegistroAulaLancado() {
		if (mensagemRegistroAulaLancado == null) {
			mensagemRegistroAulaLancado = "";
		}
		return mensagemRegistroAulaLancado;
	}

	public void setMensagemRegistroAulaLancado(String mensagemRegistroAulaLancado) {
		this.mensagemRegistroAulaLancado = mensagemRegistroAulaLancado;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public HashMap<Integer, ConfiguracaoAcademicoVO> getMapConfiguracaoAcademivoVOs() {
		if (mapConfiguracaoAcademivoVOs == null) {
			mapConfiguracaoAcademivoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapConfiguracaoAcademivoVOs;
	}

	public void setMapConfiguracaoAcademivoVOs(HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademivoVOs) {
		this.mapConfiguracaoAcademivoVOs = mapConfiguracaoAcademivoVOs;
	}

	public Boolean getPermitirUsuarioExcluirDisciplina() {
		if (permitirUsuarioExcluirDisciplina == null) {
			permitirUsuarioExcluirDisciplina = Boolean.FALSE;
		}
		return permitirUsuarioExcluirDisciplina;
	}

	public void setPermitirUsuarioExcluirDisciplina(Boolean permitirUsuarioExcluirDisciplina) {
		this.permitirUsuarioExcluirDisciplina = permitirUsuarioExcluirDisciplina;
	}

	public List<HistoricoVO> getListaHistoricoDisciplinaForaGradeVOs() {
		if (listaHistoricoDisciplinaForaGradeVOs == null) {
			listaHistoricoDisciplinaForaGradeVOs = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricoDisciplinaForaGradeVOs;
	}

	public void setListaHistoricoDisciplinaForaGradeVOs(List<HistoricoVO> listaHistoricoDisciplinaForaGradeVOs) {
		this.listaHistoricoDisciplinaForaGradeVOs = listaHistoricoDisciplinaForaGradeVOs;
	}

	public Boolean getFecharModalConfirmacaoExclusaoDisciplina() {
		if (fecharModalConfirmacaoExclusaoDisciplina == null) {
			fecharModalConfirmacaoExclusaoDisciplina = Boolean.TRUE;
		}
		return fecharModalConfirmacaoExclusaoDisciplina;
	}

	public void setFecharModalConfirmacaoExclusaoDisciplina(Boolean fecharModalConfirmacaoExclusaoDisciplina) {
		this.fecharModalConfirmacaoExclusaoDisciplina = fecharModalConfirmacaoExclusaoDisciplina;
	}

	public Boolean getPermitirUsuarioExcluirDisciplinaForaPrazo() {
		if (permitirUsuarioExcluirDisciplinaForaPrazo == null) {
			permitirUsuarioExcluirDisciplinaForaPrazo = false;
		}
		return permitirUsuarioExcluirDisciplinaForaPrazo;
	}

	public void setPermitirUsuarioExcluirDisciplinaForaPrazo(Boolean permitirUsuarioExcluirDisciplinaForaPrazo) {
		this.permitirUsuarioExcluirDisciplinaForaPrazo = permitirUsuarioExcluirDisciplinaForaPrazo;
	}

	public Boolean getRealizandoExclusaoDisciplinaForaPrazo() {
		if (realizandoExclusaoDisciplinaForaPrazo == null) {
			realizandoExclusaoDisciplinaForaPrazo = false;
		}
		return realizandoExclusaoDisciplinaForaPrazo;
	}

	public void setRealizandoExclusaoDisciplinaForaPrazo(Boolean realizandoExclusaoDisciplinaForaPrazo) {
		this.realizandoExclusaoDisciplinaForaPrazo = realizandoExclusaoDisciplinaForaPrazo;
	}

	/**
	 * Método responsável por executar a montagem da lista de select item da matriz curricular do curso, setando como padrão a matriz curricular atual
	 * da matrícula
	 * 
	 * @throws Exception
	 */
	public void montarListaSelectItemMatrizCurricular() throws Exception {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorMatriculaGradeCurricularVOsVinculadaHistoricoInclusaoExclusaoDisciplina(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
			getListaSelectItemMatrizCurricular().clear();
			boolean existeGradeAtual = false;
			for (GradeCurricularVO grade : gradeCurricularVOs) {
				getListaSelectItemMatrizCurricular().add(new SelectItem(grade.getCodigo(), grade.getNome()));
				if (grade.getCodigo().equals(getMatriculaVO().getGradeCurricularAtual().getCodigo())) {
					existeGradeAtual = true;
				}
			}
			if (!existeGradeAtual && Uteis.isAtributoPreenchido(getMatriculaVO().getGradeCurricularAtual())) {
				getMatriculaVO().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getListaSelectItemMatrizCurricular().add(new SelectItem(getMatriculaVO().getGradeCurricularAtual().getCodigo(), getMatriculaVO().getGradeCurricularAtual().getNome()));
			}
			setMatrizCurricular(getMatriculaVO().getGradeCurricularAtual().getCodigo());
			realizarMontagemPainelMatrizCurricular();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por executar a montagem do painel da matriz curricular do aluno
	 */
	public void realizarMontagemPainelMatrizCurricular() throws Exception {
		try {
			inicializarMatriculaComHistoricoAluno(true);
			getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(getInclusaoHistoricoForaPrazoVO(), getMatriculaVO(), getMatriculaPeriodoVO().getGradeCurricular(), getUsuarioLogado());
		} catch (Exception e) {
			throw e;
		}
	}

	public List<SelectItem> getListaSelectItemMatrizCurricular() {
		if (listaSelectItemMatrizCurricular == null) {
			listaSelectItemMatrizCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMatrizCurricular;
	}

	public void setListaSelectItemMatrizCurricular(List<SelectItem> listaSelectItemMatrizCurricular) {
		this.listaSelectItemMatrizCurricular = listaSelectItemMatrizCurricular;
	}

	public Integer getMatrizCurricular() {
		if (matrizCurricular == null) {
			matrizCurricular = 0;
		}
		return matrizCurricular;
	}

	public void setMatrizCurricular(Integer matrizCurricular) {
		this.matrizCurricular = matrizCurricular;
	}

	public List<HistoricoVO> getListaHistoricosDuplicadosAluno() {
		if (listaHistoricosDuplicadosAluno == null) {
			listaHistoricosDuplicadosAluno = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricosDuplicadosAluno;
	}

	public void setListaHistoricosDuplicadosAluno(List<HistoricoVO> listaHistoricosDuplicadosAluno) {
		this.listaHistoricosDuplicadosAluno = listaHistoricosDuplicadosAluno;
	}

	/**
	 * Método responsável por carregar os dados do histórico do aluno a ser excluído
	 */
	public void selecionarHistoricoDuplicadoParaExclusao() {
		try {
			HistoricoVO obj = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicosDuplicadosAlunoItens");
			obj.setConfiguracaoAcademico(carregarDadosConfiguracaoAcademico(obj.getConfiguracaoAcademico().getCodigo()));
			setMensagemMediaFrequencia(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemMediaFrequencia(obj, getUsuarioLogado()));
			setMensagemNotaLancada(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemNotaLancada(obj, obj.getConfiguracaoAcademico(), getUsuarioLogado()));
			setMensagemRegistroAulaLancado(getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorDisciplinaMatricula(getMatriculaVO().getMatricula(), obj.getDisciplina().getCodigo(), getUsuarioLogado()));
			setHistoricoVO(obj);
			setRealizandoExclusaoHistoricoDuplicadoAluno(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Método responsável por executar a montagem dos históricos duplicados do aluno
	 */
	public void verificarDadosExclusaoHistoricoDuplicadoAluno() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		try {
			setListaHistoricosDuplicadosAluno(obj.getHistoricosDuplicadosAluno());
			setGradeDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getRealizandoExclusaoHistoricoDuplicadoAluno() {
		if (realizandoExclusaoHistoricoDuplicadoAluno == null) {
			realizandoExclusaoHistoricoDuplicadoAluno = false;
		}
		return realizandoExclusaoHistoricoDuplicadoAluno;
	}

	public void setRealizandoExclusaoHistoricoDuplicadoAluno(Boolean realizandoExclusaoHistoricoDuplicadoAluno) {
		this.realizandoExclusaoHistoricoDuplicadoAluno = realizandoExclusaoHistoricoDuplicadoAluno;
	}

	public boolean getIsApresentarBotaoRemoverDisciplinaForaGrade() {
		return !getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasForaGradeCurricular().isEmpty();
	}

	public void prepararDisciplinaMapaEquivalenciaParaExclusao() {
		MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
		setHistoricoVO(mapaEquivalenciaDisciplinaCursadaVO.getHistorico());
	}

	// Transient
	private List<PeriodoLetivoComHistoricoAlunoVO> periodoLetivoComHistoricoAlunoVOs;

	public List<PeriodoLetivoComHistoricoAlunoVO> getPeriodoLetivoComHistoricoAlunoVOs() {
		if (periodoLetivoComHistoricoAlunoVOs == null) {
			periodoLetivoComHistoricoAlunoVOs = new ArrayList<PeriodoLetivoComHistoricoAlunoVO>();
		}
		return periodoLetivoComHistoricoAlunoVOs;
	}

	public void setPeriodoLetivoComHistoricoAlunoVOs(List<PeriodoLetivoComHistoricoAlunoVO> periodoLetivoComHistoricoAlunoVOs) {
		this.periodoLetivoComHistoricoAlunoVOs = periodoLetivoComHistoricoAlunoVOs;
	}

	/**
	 * @author Victor Hugo de Paula Costa 19/08/2015 09:09 5.0.4.0
	 */
	public void verificarDadosExclusaoDisciplinaAlunoGrupoOptativa() {
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens"));
			getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().setConfiguracaoAcademico(carregarDadosConfiguracaoAcademico(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().getConfiguracaoAcademico().getCodigo()));
			setMensagemMediaFrequencia(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemMediaFrequencia(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno(), getUsuarioLogado()));
			setMensagemNotaLancada(getFacadeFactory().getHistoricoFacade().realizarCriacaoMensagemNotaLancada(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno(), getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().getConfiguracaoAcademico(), getUsuarioLogado()));
			setMensagemRegistroAulaLancado(getFacadeFactory().getRegistroAulaFacade().consultarRegistroAulaPorDisciplinaMatricula(getMatriculaVO().getMatricula(), getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().getCodigo(), getUsuarioLogado()));
			setHistoricoVO(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerHistoricosDisciplinasAlunoCursandoGradeCurricular(HistoricoVO historicoVO){
		if (historicoVO.getCursando()) {
			for (Iterator<HistoricoVO> iterator = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAlunoCursandoGradeCurricular().iterator(); iterator.hasNext();) {
				HistoricoVO historicoRemover = (HistoricoVO) iterator.next();
				if (historicoRemover.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo())
						&& historicoRemover.getAnoHistorico().equals(historicoVO.getAnoHistorico())
						&& historicoRemover.getSemestreHistorico().equals(historicoVO.getSemestreHistorico())
						&& historicoRemover.getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())) {
					iterator.remove();
					break;
				}
			}
		}
	}
		
	public void removerHistoricosDisciplinasAlunoAprovadoGradeCurricular(HistoricoVO historicoVO){
		if (historicoVO.getAprovado()) {
			for (Iterator<HistoricoVO> iterator = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasAprovadasAlunoGradeCurricular().iterator(); iterator.hasNext();) {
				HistoricoVO historicoRemover = (HistoricoVO) iterator.next();
				if (historicoRemover.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo())
						&& historicoRemover.getAnoHistorico().equals(historicoVO.getAnoHistorico())
						&& historicoRemover.getSemestreHistorico().equals(historicoVO.getSemestreHistorico())
						&& historicoRemover.getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())) {
					iterator.remove();
					break;
				}
			}
		}
	}	
	
	public void removerHistoricosDisciplinasAlunoForaGradeCurricular(HistoricoVO historicoVO){
		if (historicoVO.getHistoricoDisciplinaForaGrade()) {
			for (Iterator<HistoricoVO> iterator = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getHistoricosDisciplinasForaGradeCurricular().iterator(); iterator.hasNext();) {
				HistoricoVO historicoRemover = (HistoricoVO) iterator.next();
				if (historicoRemover.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo())
						&& historicoRemover.getAnoHistorico().equals(historicoVO.getAnoHistorico())
						&& historicoRemover.getSemestreHistorico().equals(historicoVO.getSemestreHistorico())
						&& historicoRemover.getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())) {
					iterator.remove();
					break;
				}
			}
		}
	}
	
	public void removerHistoricoGrupoOptativaGradeCurricularSelecionado(HistoricoVO historicoVO){
		if (Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
			for(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO: getMatriculaPeriodoVO().getGradeCurricular().getGradeCurricularGrupoOptativaVOs()){				
				for(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO: gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs()){
					if(gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo())
							&& gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getAnoHistorico().equals(historicoVO.getAnoHistorico())
							&& gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getSemestreHistorico().equals(historicoVO.getSemestreHistorico())
							&& gradeCurricularGrupoOptativaDisciplinaVO.getHistoricoAtualAluno().getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())){
						gradeCurricularGrupoOptativaDisciplinaVO.setHistoricoAtualAluno(null);
						gradeCurricularGrupoOptativaDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(null);
						return;
					}
				}								
			}
		}
	}
	
	public void removerHistoricoGradeDisciplinaGradeCurricularSelecionado(HistoricoVO historicoVO){
		if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())) {
			for (PeriodoLetivoVO periodoLetivoVO : getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs()) {				
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
					if (gradeDisciplinaVO.getHistoricoAtualAluno().getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo()) &&
							gradeDisciplinaVO.getHistoricoAtualAluno().getAnoHistorico().equals(historicoVO.getAnoHistorico()) &&
							gradeDisciplinaVO.getHistoricoAtualAluno().getSemestreHistorico().equals(historicoVO.getSemestreHistorico()) && 
							gradeDisciplinaVO.getHistoricoAtualAluno().getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo()) ) {
						gradeDisciplinaVO.setHistoricoAtualAluno(null);
						for (Iterator<HistoricoVO> iterator = gradeDisciplinaVO.getHistoricosDuplicadosAluno().iterator(); iterator.hasNext();) {
							HistoricoVO his = iterator.next();
							if (his.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo()) &&
									his.getAnoHistorico().equals(historicoVO.getAnoHistorico()) &&
									his.getSemestreHistorico().equals(historicoVO.getSemestreHistorico()) && 
									his.getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo()) ) {
								iterator.remove();
								break;
							}
						}
						
						gradeDisciplinaVO.setHistoricoAtualAluno(realizarDefinicaoHistoricoAtual(gradeDisciplinaVO.getHistoricosDuplicadosAluno()));
						gradeDisciplinaVO.setMatriculaPeriodoTurmaDisciplinaVO(gradeDisciplinaVO.getHistoricoAtualAluno().getMatriculaPeriodoTurmaDisciplina());
						if(gradeDisciplinaVO.getHistoricosDuplicadosAluno().size()==1){
							gradeDisciplinaVO.getHistoricosDuplicadosAluno().clear();
						}
						return;
					}else if(gradeDisciplinaVO.getCodigo().equals(historicoVO.getGradeDisciplinaVO().getCodigo()) && !gradeDisciplinaVO.getHistoricosDuplicadosAluno().isEmpty()){
						for (Iterator<HistoricoVO> iterator = gradeDisciplinaVO.getHistoricosDuplicadosAluno().iterator(); iterator.hasNext();) {
							HistoricoVO his = iterator.next();
							if (his.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo()) &&
									his.getAnoHistorico().equals(historicoVO.getAnoHistorico()) &&
									his.getSemestreHistorico().equals(historicoVO.getSemestreHistorico()) && 
									his.getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo()) ) {
								iterator.remove();
								break;
							}
						}
						if(gradeDisciplinaVO.getHistoricosDuplicadosAluno().size()==1){
							gradeDisciplinaVO.getHistoricosDuplicadosAluno().clear();
						}
					}
				}				
			}
		}
	}
	
	public void removerHistoricoGradeDisciplinaComHistoricoAlunoVO(HistoricoVO historicoVO) {
		if (Uteis.isAtributoPreenchido(historicoVO.getGradeDisciplinaVO())) {
			for (Iterator<PeriodoLetivoComHistoricoAlunoVO> iterator = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs().iterator(); iterator.hasNext();) {
				PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = iterator.next();
				for (GradeDisciplinaComHistoricoAlunoVO gradeDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeDisciplinaComHistoricoAlunoVOs()) {
					if (gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo()) && 
							gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAnoHistorico().equals(historicoVO.getAnoHistorico()) && 
							gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(historicoVO.getSemestreHistorico()) && 
							gradeDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())) {
						gradeDisciplinaComHistoricoAlunoVO.setHistoricoAtualAluno(null);
						gradeDisciplinaComHistoricoAlunoVO.setHistoricoAtualAluno(realizarDefinicaoHistoricoAtual(gradeDisciplinaComHistoricoAlunoVO.getHistoricosAluno()));					
						return;
					}
				}
			}
		}
	}
	
	public void removerHistoricoGradeCurricularGrupoOptativaDisciplinaVO(HistoricoVO historicoVO) {
		if (Uteis.isAtributoPreenchido(historicoVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
			for (Iterator<PeriodoLetivoComHistoricoAlunoVO> iterator = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getPeriodoLetivoComHistoricoAlunoVOs().iterator(); iterator.hasNext();) {
				PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO = iterator.next();
				for (GradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO : periodoLetivoComHistoricoAlunoVO.getGradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO()) {
					if (gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo()) && 
							gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getAnoHistorico().equals(historicoVO.getAnoHistorico()) && 
							gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getSemestreHistorico().equals(historicoVO.getSemestreHistorico()) && 
							gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.getHistoricoAtualAluno().getMatriculaPeriodo().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())) {										
						gradeCurricularGrupoOptativaDisciplinaComHistoricoAlunoVO.setHistoricoAtualAluno(null);
						return;
					}
				}
			}
		}
	}
	
	public void removerHistoricoCursandoPorEquivalencia(HistoricoVO historicoVO) throws Exception{
		if (Uteis.isAtributoPreenchido(historicoVO.getMapaEquivalenciaDisciplina()) && historicoVO.getHistoricoEquivalente() && getMapaEquivalenciaDisciplinaVisualizar().getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1) {
			for (Iterator<MapaEquivalenciaDisciplinaMatrizCurricularVO> iterator2 = getMapaEquivalenciaDisciplinaVisualizar().getMapaEquivalenciaDisciplinaMatrizCurricularVOs().iterator(); iterator2.hasNext();) {
				MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularRemoverVO = (MapaEquivalenciaDisciplinaMatrizCurricularVO) iterator2.next();				
				for (Iterator<HistoricoVO> iterator3 = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTodosHistoricosAlunoGradeCurricular().iterator(); iterator3.hasNext();) {
					HistoricoVO historicoPorEquivalenciaRemover = (HistoricoVO) iterator3.next();
					if (mapaEquivalenciaDisciplinaMatrizCurricularRemoverVO.getDisciplinaVO().getCodigo().equals(historicoPorEquivalenciaRemover.getDisciplina().getCodigo())
							&& historicoPorEquivalenciaRemover.getMapaEquivalenciaDisciplina().getCodigo().equals(historicoVO.getMapaEquivalenciaDisciplina().getCodigo())
							&& historicoPorEquivalenciaRemover.getMapaEquivalenciaDisciplinaMatrizCurricular().getCodigo().equals(mapaEquivalenciaDisciplinaMatrizCurricularRemoverVO.getCodigo())
							&& historicoPorEquivalenciaRemover.getNumeroAgrupamentoEquivalenciaDisciplina().equals(historicoVO.getNumeroAgrupamentoEquivalenciaDisciplina())) {
						executarRemocaoHistoricoDisciplinaExclusao(historicoVO, false);
						break;
					}
				}
			}
		}
		if (Uteis.isAtributoPreenchido(historicoVO.getMapaEquivalenciaDisciplina()) && historicoVO.getHistoricoPorEquivalencia() && getMapaEquivalenciaDisciplinaVisualizar().getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1) {
			for (Iterator<MapaEquivalenciaDisciplinaCursadaVO> iterator2 = getMapaEquivalenciaDisciplinaVisualizar().getMapaEquivalenciaDisciplinaCursadaVOs().iterator(); iterator2.hasNext();) {
				MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaRemoverVO = (MapaEquivalenciaDisciplinaCursadaVO) iterator2.next();
				for (Iterator<HistoricoVO> iterator3 = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTodosHistoricosAlunoGradeCurricular().iterator(); iterator3.hasNext();) {
					HistoricoVO historicoPorEquivalenciaRemover = (HistoricoVO) iterator3.next();
					if (mapaEquivalenciaDisciplinaCursadaRemoverVO.getDisciplinaVO().getCodigo().equals(historicoPorEquivalenciaRemover.getDisciplina().getCodigo())
							&& historicoPorEquivalenciaRemover.getMapaEquivalenciaDisciplina().getCodigo().equals(historicoVO.getMapaEquivalenciaDisciplina().getCodigo())
							&& historicoPorEquivalenciaRemover.getMapaEquivalenciaDisciplinaCursada().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaRemoverVO.getCodigo())
							&& historicoPorEquivalenciaRemover.getNumeroAgrupamentoEquivalenciaDisciplina().equals(historicoVO.getNumeroAgrupamentoEquivalenciaDisciplina())) {
						executarRemocaoHistoricoDisciplinaExclusao(historicoVO, false);
						break;
					}
				}
			}
		}
	}
	
	public HistoricoVO realizarDefinicaoHistoricoAtual(List<HistoricoVO> historicoVOs){		
		if(!historicoVOs.isEmpty()){
			Ordenacao.ordenarLista(historicoVOs, "anoSemestreOrdenacao");
			return historicoVOs.get(historicoVOs.size()-1);
		}
		HistoricoVO historicoVO = new HistoricoVO();
		historicoVO.setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
		return historicoVO;
	}
	
	public void removerMatriculaPeriodoTurmaDisciplinaPorHistorico(HistoricoVO historicoVO){
		try {
			MatriculaPeriodoVO matriculaPeriodoVO;
			matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(historicoVO.getMatriculaPeriodoTurmaDisciplina());
			for(Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator2= matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator(); iterator2.hasNext();){				
				MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = iterator2.next();
				if(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo().equals(historicoVO.getDisciplina().getCodigo())
						&& matriculaPeriodoTurmaDisciplinaVO.getAno().equals(historicoVO.getAnoHistorico())
						&& matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals(historicoVO.getSemestreHistorico())
						&& matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getCodigo().equals(historicoVO.getMatriculaPeriodo().getCodigo())
						){
					iterator2.remove();
					break;
				}
			}
		} catch (Exception e) {
			
		}
	}
	
	public void executarRemocaoHistoricoDisciplinaExclusao(HistoricoVO historicoVO, boolean validarEquivalencia) throws Exception {
		removerHistoricosDisciplinasAlunoCursandoGradeCurricular(historicoVO);		
		removerHistoricosDisciplinasAlunoAprovadoGradeCurricular(historicoVO);
		removerHistoricosDisciplinasAlunoForaGradeCurricular(historicoVO);
		removerHistoricoGrupoOptativaGradeCurricularSelecionado(historicoVO);
		removerHistoricoGradeDisciplinaGradeCurricularSelecionado(historicoVO);
		removerHistoricoGradeDisciplinaComHistoricoAlunoVO(historicoVO);	
		removerHistoricoGradeCurricularGrupoOptativaDisciplinaVO(historicoVO);
		if(validarEquivalencia) {
			removerHistoricoCursandoPorEquivalencia(historicoVO);		
		}
		removerMatriculaPeriodoTurmaDisciplinaPorHistorico(historicoVO);		
		
	}

	public void selecionarMapaEquivalenciaDisciplinaGrupoOptativaParaVisualizacao() {
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens"));
			prepararMapaEquivalenciaParaVisualizacao(getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS), getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().getNumeroAgrupamentoEquivalenciaDisciplina());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	public void selecionarMatriculaPeriodoTurmaDisciplinaCompostaGrupoOptativa() {
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO((GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens"));
			getGradeCurricularGrupoOptativaDisciplinaVO().setSelecionado(true);
			MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(getGradeCurricularGrupoOptativaDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO());
			getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(getFacadeFactory().getMatriculaPeriodoFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getGradeCurricularGrupoOptativaDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getGradeCurricularGrupoOptativaDisciplinaVO().setSelecionado(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public UsuarioVO getUsuarioLibercaoChoqueHorario() {
		if (usuarioLibercaoChoqueHorario == null) {
			usuarioLibercaoChoqueHorario = new UsuarioVO();
		}
		return usuarioLibercaoChoqueHorario;
	}

	public void setUsuarioLibercaoChoqueHorario(UsuarioVO usuarioLibercaoChoqueHorario) {
		this.usuarioLibercaoChoqueHorario = usuarioLibercaoChoqueHorario;
	}

	public String getOncompleteLiberacaoChoqueHorario() {
		if (getChoqueHorarioLiberado()) {
			return "RichFaces.$('panelAutenticarUsuarioLiberacaoChoqueHorario').hide(); RichFaces.$('panelIncluirDisciplinas').hide();";
		}
		return "";
	}

	public void excluirGradeDisciplinaComposta() {
		try {
			MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO());
			
			getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(matriculaPeriodoVO, getMatriculaVO(), getGradeDisciplinaCompostaSelecionadaVO().getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado());
			setHistoricoVO(getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().verificarAlunoCursandoDisciplina(getGradeDisciplinaCompostaSelecionadaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo()));
			executarRemocaoHistoricoDisciplinaExclusao(getHistoricoVO(), false);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluir(getGradeDisciplinaCompostaSelecionadaVO().getMatriculaPeriodoTurmaDisciplinaVO(), false, getUsuarioLogado());
			getGradeDisciplinaCompostaSelecionadaVO().setMatriculaPeriodoTurmaDisciplinaVO(null);
			getGradeDisciplinaCompostaSelecionadaVO().setHistoricoAtualAluno(null);
			getGradeDisciplinaVO().setGradeDisciplinaCompostaVOs(getFacadeFactory().getMatriculaPeriodoFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO(), matriculaPeriodoVO));
			getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().realizarDefinicaoNumeroVagasDisciplinaCompostaPorEscolha(getGradeDisciplinaVO());
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void executarInicializacaoDadosAdicionarGradeDisciplinaComposta() {
		setGradeDisciplinaCompostaSelecionadaVO((GradeDisciplinaCompostaVO) getRequestMap().get("disciplinaCompostaItens"));
		try {
			setOnCompleteIncluirDisciplinaTurma("RichFaces.$('panelIncluirDisciplinas').show()");
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarAdicionarDisciplina(getFacadeFactory().getMatriculaPeriodoFacade().realizarObtencaoMatriculaPeriodoTurmaDisciplinaFazParteComposicao(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO(), getMatriculaPeriodoVO()));
			getGradeDisciplinaCompostaSelecionadaVO().setSelecionado(true);
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaCompostaVO(getGradeDisciplinaCompostaSelecionadaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(getGradeDisciplinaCompostaSelecionadaVO().getDisciplina());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaFazParteComposicao(true);
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(getGradeDisciplinaCompostaSelecionadaVO().getGradeCurricularGrupoOptativaDisciplina());
			if(Uteis.isAtributoPreenchido(getGradeDisciplinaCompostaSelecionadaVO().getGradeDisciplina())){
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setAno(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getAno());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setSemestre(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDataRegistroHistorico(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDataRegistroHistorico());
			}else{
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setAno(getGradeCurricularGrupoOptativaDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getAno());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setSemestre(getGradeCurricularGrupoOptativaDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getSemestre());
				getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDataRegistroHistorico(getGradeCurricularGrupoOptativaDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getDataRegistroHistorico());
			}
			getGradeDisciplinaCompostaSelecionadaVO().setMatriculaPeriodoTurmaDisciplinaVO(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());			
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar();
			if (getHorarioAlunoTurnoVOs().isEmpty()) {
					prepararHorarioAulaAluno(executarDefinicaoQualMatriculaPeriodoUtilizar(getMatriculaPeriodoTurmaDisciplinaVOAdicionar()));					
			} else {
				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
			}
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			setOnCompleteIncluirDisciplinaTurma("");
			getGradeDisciplinaCompostaSelecionadaVO().setSelecionado(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private boolean fecharPainelDisciplinaComposta = false;

	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar() {
		try {
			setFecharPainelDisciplinaComposta(true);
			if(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().isNovoObj() 
					&& getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getAno().trim().isEmpty() 
					&& !getMatriculaVO().getCurso().getPeriodicidade().equals("IN")
					&& !getGradeDisciplinaVO().getHistoricoAtualAluno().isNovoObj()){
				getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().setAno(getGradeDisciplinaVO().getHistoricoAtualAluno().getAnoHistorico());
				getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(getGradeDisciplinaVO().getHistoricoAtualAluno().getSemestreHistorico());
			}
			executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getAno(), 
					getGradeDisciplinaVO().getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), getGradeDisciplinaVO().getGradeDisciplinaCompostaVOs());
			setMensagemID("msg_dados_selecionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setFecharPainelDisciplinaComposta(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarPorMatriculaPeriodo() throws Exception {
		for (PeriodoLetivoVO periodoLetivoVO : getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gdVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
				if (gdVO.getSelecionado() || ((!gdVO.getMatriculaPeriodoTurmaDisciplinaVO().isNovoObj() || !gdVO.getHistoricoAtualAluno().isNovoObj()) && gdVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplinaComposta())){
					if(gdVO.getMatriculaPeriodoTurmaDisciplinaVO().isNovoObj() && gdVO.getMatriculaPeriodoTurmaDisciplinaVO().getAno().trim().isEmpty() 
							&& !getMatriculaVO().getCurso().getPeriodicidade().equals("IN")
							&& !gdVO.getHistoricoAtualAluno().isNovoObj()){
						gdVO.getMatriculaPeriodoTurmaDisciplinaVO().setAno(gdVO.getHistoricoAtualAluno().getAnoHistorico());
						gdVO.getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(gdVO.getHistoricoAtualAluno().getSemestreHistorico());
					}
					//MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(gdVO.getMatriculaPeriodoTurmaDisciplinaVO());
					executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(gdVO.getMatriculaPeriodoTurmaDisciplinaVO().getAno(),  gdVO.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), gdVO.getGradeDisciplinaCompostaVOs());
				}
			}
		}
	}

	private void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudar(String ano, String semestre, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVOs)) {
			return;
		}
		Integer numeroMaximoDisciplinaComposicaoEstudar = 0;
		GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCompostaVOs.get(0).getGradeDisciplina().getCodigo(), getUsuarioLogado());
		for (GradeDisciplinaCompostaVO compostaVO : gradeDisciplinaCompostaVOs) {
			numeroMaximoDisciplinaComposicaoEstudar += (Uteis.isAtributoPreenchido(compostaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma())
					&& compostaVO.getMatriculaPeriodoTurmaDisciplinaVO().getAno().equals(ano)
					&& compostaVO.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre().equals(semestre)) 
					|| (Uteis.isAtributoPreenchido(compostaVO.getHistoricoAtualAluno()) 
							&& compostaVO.getHistoricoAtualAluno().getAnoHistorico().equals(ano)
							&& compostaVO.getHistoricoAtualAluno().getSemestreHistorico().equals(semestre)) ? 1 : 0;
		}
		if ((numeroMaximoDisciplinaComposicaoEstudar < gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar() || numeroMaximoDisciplinaComposicaoEstudar > gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar()) 
				&& TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(gradeDisciplinaVO.getTipoControleComposicao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudar").replace("{0}", 
					gradeDisciplinaVO.getDisciplina().getNome()).replace("{1}", numeroMaximoDisciplinaComposicaoEstudar.toString())
					.replace("{2}", gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar().toString())
					.replace("{3}", gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar().toString()));
		}	
	}

	public boolean isFecharPainelDisciplinaComposta() {
		return fecharPainelDisciplinaComposta;
	}

	public void setFecharPainelDisciplinaComposta(boolean fecharPainelDisciplinaComposta) {
		this.fecharPainelDisciplinaComposta = fecharPainelDisciplinaComposta;
	}

	public String getOncompletePainelDisciplinaComposta() {
		if (isFecharPainelDisciplinaComposta()) {
			return "Richfaces.$('panelDisciplinasComposta').hide()";
		}
		return "";
	}

	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaSelecionadaVO() {
		if (gradeDisciplinaCompostaSelecionadaVO == null) {
			gradeDisciplinaCompostaSelecionadaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaSelecionadaVO;
	}

	public void setGradeDisciplinaCompostaSelecionadaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaSelecionadaVO) {
		this.gradeDisciplinaCompostaSelecionadaVO = gradeDisciplinaCompostaSelecionadaVO;
	}

	private MatriculaPeriodoVO executarDefinicaoQualMatriculaPeriodoUtilizar(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		return getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().verificarQualMatriculaPeriodoCarregarDadosParaChoqueHorario(getMatriculaVO(), matriculaPeriodoTurmaDisciplinaVO, getConfiguracaoFinanceiroPadraoSistema(), getMapMatriculaPeriodoVOs(), getUsuarioLogado());
	}
	
	public void selecionarMatriculaPeriodoTurmaDiscipinaFazParteComposicaoRemover() {
		try {
			setGradeDisciplinaCompostaSelecionadaVO((GradeDisciplinaCompostaVO) getRequestMap().get("disciplinaCompostaItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void executarVerificacaoNumeroMaximoDisciplinaComposicaoEstudarAdicionarDisciplina(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if (!Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVOs)) {
			return;
		}
		Integer numeroMaximoDisciplinaComposicaoEstudar = 0;
		GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(gradeDisciplinaCompostaVOs.get(0).getGradeDisciplina().getCodigo(), getUsuarioLogado());
		for (GradeDisciplinaCompostaVO compostaVO : gradeDisciplinaCompostaVOs) {
			numeroMaximoDisciplinaComposicaoEstudar += Uteis.isAtributoPreenchido(compostaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma()) ? 1 : 0;
		}
		if ((numeroMaximoDisciplinaComposicaoEstudar < gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar() || numeroMaximoDisciplinaComposicaoEstudar > gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar()) 
				&& TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(gradeDisciplinaVO.getTipoControleComposicao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudar").replace("{0}", 
					gradeDisciplinaVO.getDisciplina().getNome()).replace("{1}", numeroMaximoDisciplinaComposicaoEstudar.toString())
					.replace("{2}", gradeDisciplinaVO.getNumeroMinimoDisciplinaComposicaoEstudar().toString())
					.replace("{3}", gradeDisciplinaVO.getNumeroMaximoDisciplinaComposicaoEstudar().toString()));
		}	
	}
	
	public void alterarDataRegistroHistorico() {
		try {
			getFacadeFactory().getHistoricoFacade().alterarDataRegistroHistorico(getHistoricoVO().getCodigo(), getHistoricoVO().getDataRegistro(),getHistoricoVO().getDataInicioAula(), getHistoricoVO().getDataFimAula(), getUsuarioLogado());
			setMensagemID("msg_data_registro_alterada", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getPermiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento() {
		if (permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento == null) {
			permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento = false;
		}
		return permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento;
	}

	public void setPermiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento(Boolean permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento) {
		this.permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento = permiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento;
	}
	
	public void verificarObrigatoriedadeInclusaoExclusaoSomenteViaRequerimento() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Requerimento_PermitirRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento", getUsuarioLogado());
			setPermiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento(Boolean.TRUE);
			
		} catch (Exception e) {
			setPermiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento(Boolean.FALSE);
		}
	}


	public void validarDadosRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento() throws Exception {
		if (getPermiteRealizarInclusaoExclusaoDisciplinaSomenteViaRequerimento()) {
			if (getRequerimentoVO().getCodigo().equals(0)) {
				throw new Exception("O Usuário não possui permissão para Incluir/Excluir Disciplina sem o Requerimento estar informado, do Tipo (INCLUSÃO/EXCLUSÃO ou REPOSIÇAO de Disciplina). Favor informar o código do Requerimento para prosseguir a operação.");
			} 
		}
	}
	
	public void validarDadosRequerimentoTipoRequerimentoInclusaoExclusaoDisciplina() throws Exception {
		if (!getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString()) && !getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.REPOSICAO.getValor().toString())) {
			throw new Exception("O TIPO DE REQUERIMENTO deve ser INCLUSÃO/EXCLUSÃO ou REPOSIÇÃO de Disciplina.");
		}
	}

	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}
	
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			RequerimentoVO obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaTipoRequerimentoInclusaoEReposicaoDisciplinaDadosMinimos(getRequerimentoVO().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			if (obj != null && !obj.getCodigo().equals(0)) {
				setRequerimentoVO(obj);
				validarDadosSituacaoRequerimento(getRequerimentoVO());
				validarDadosRequerimentoTipoRequerimentoInclusaoExclusaoDisciplina();
				setMatriculaVO(getRequerimentoVO().getMatricula());
				carregarDadosMatriculaSelecionada(getMatriculaVO());
				obterGradeDisciplinaRequerimentoAluno(getRequerimentoVO());
				inicializacaoDadosInclusaoDisciplinaRequerimento();
				if (obj.getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString())) {
					getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
					getMatriculaPeriodoVO().setReposicao(false);
				} else {
					getMatriculaPeriodoVO().setReposicao(true);
					getMatriculaPeriodoVO().setInclusaoForaPrazo(false);
				}
			} else {
				setRequerimentoVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRequerimentoVO(null);
			
		}
	}
	
	public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			setRequerimentoVO(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaTipoRequerimentoInclusaoEReposicaoDisciplinaDadosMinimos(obj.getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			validarDadosSituacaoRequerimento(getRequerimentoVO());
			validarDadosRequerimentoTipoRequerimentoInclusaoExclusaoDisciplina();
			setMatriculaVO(getRequerimentoVO().getMatricula());
			carregarDadosMatriculaSelecionada(getMatriculaVO());
			obterGradeDisciplinaRequerimentoAluno(getRequerimentoVO());
			inicializacaoDadosInclusaoDisciplinaRequerimento();
			if (getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString())) {
				getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
				getMatriculaPeriodoVO().setReposicao(false);
			} else {
				getMatriculaPeriodoVO().setReposicao(true);
				getMatriculaPeriodoVO().setInclusaoForaPrazo(false);
			}
			getListaConsultaRequerimento().clear();
			setCampoConsultaRequerimento("");
			setValorConsultaRequerimento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setRequerimentoVO(null);
		}
	}
	
	public void validarDadosSituacaoRequerimento(RequerimentoVO obj) throws Exception {
		if (!obj.getSituacao().equals(SituacaoRequerimento.EM_EXECUCAO.getValor())) {
			throw new Exception("A situação do Requerimento deve estar em Execução.");
		}
	}
	
	public void obterGradeDisciplinaRequerimentoAluno(RequerimentoVO requerimento) throws Exception {
		setGradeDisciplinaRequerimentoVO(new GradeDisciplinaVO());
		for (PeriodoLetivoVO periodoLetivo : getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs()) {
			for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
				if (requerimento.getDisciplina().getCodigo().equals(gradeDisciplina.getDisciplina().getCodigo())) {
					setGradeDisciplinaRequerimentoVO((GradeDisciplinaVO) gradeDisciplina.clone());
					return;
				}
			}
		}
	}
	
	public void inicializacaoDadosInclusaoDisciplinaRequerimento () {
			prepararAdicionarDisciplinaRequerimentoMatriculaPeriodo(getGradeDisciplinaRequerimentoVO());
	}
	
	
	public String getAbrirModalInclusaoExclusaoDisciplinaRequerimento() {
		if (!getGradeDisciplinaRequerimentoVO().getCodigo().equals(0)) {
			return "RichFaces.$('panelIncluirDisciplinas').show()";
		}
		return "";
	}
	
	public void prepararAdicionarDisciplinaRequerimentoMatriculaPeriodo(GradeDisciplinaVO gradeDisciplinaRequerimento) {
		try {
			setGradeDisciplinaVO(gradeDisciplinaRequerimento);
			getGradeDisciplinaVO().setSelecionado(true);
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(getGradeDisciplinaVO().getDisciplina());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeDisciplinaVO(getGradeDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDataRegistroHistorico(getMatriculaPeriodoVO().getData());
			setCodigoMapaEquivalenciaDisciplinaVOIncluir(0);
			setCodigoMapaEquivalenciaDisciplinaCursar(0);
			setListaSelectItemMapaEquivalenciaDisciplinaIncluir(null);
			setListaSelectItemDisciplinaEquivalenteAdicionar(null);
			montarListaSelectItemTurmaAdicionar();
			if (getHorarioAlunoTurnoVOs().isEmpty()) {
				if (getMatriculaVO().getCurso().getIntegral()) {
					prepararHorarioAulaAluno();
				}
			} else {
				getFacadeFactory().getHorarioAlunoFacade().realizarLimpezaRegistroChoqueHorario(getHorarioAlunoTurnoVOs());
			}
			setChoqueHorarioLiberado(false);
			setMensagemID("msg_informe_dados");
		} catch (Exception e) {
			getGradeDisciplinaVO().setSelecionado(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public GradeDisciplinaVO getGradeDisciplinaRequerimentoVO() {
		if (gradeDisciplinaRequerimentoVO == null) {
			gradeDisciplinaRequerimentoVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaRequerimentoVO;
	}

	public void setGradeDisciplinaRequerimentoVO(GradeDisciplinaVO gradeDisciplinaRequerimentoVO) {
		this.gradeDisciplinaRequerimentoVO = gradeDisciplinaRequerimentoVO;
	}
	
	public Boolean getApresentarBotaoIncluirDisciplina() {
		return getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDescricaoChoqueHorarioDisciplina().equals("") || getChoqueHorarioLiberado();
	}

	public Boolean getChoqueHorarioLiberado() {
		if (choqueHorarioLiberado == null) {
			choqueHorarioLiberado = false;
		}
		return choqueHorarioLiberado;
	}

	public void setChoqueHorarioLiberado(Boolean choqueHorarioLiberado) {
		this.choqueHorarioLiberado = choqueHorarioLiberado;
	}
	
	public void realizarAtualizacaoGradeDisciplinaRequerimentoAdicionada(GradeDisciplinaVO gradeDisciplinaIncluida) {
		if (!getRequerimentoVO().getCodigo().equals(0)) {
			if (getRequerimentoVO().getDisciplina().getCodigo().equals(gradeDisciplinaIncluida.getDisciplina().getCodigo())) {
				int index = 0;
				for (PeriodoLetivoVO periodoLetivo : getMatriculaPeriodoVO().getGradeCurricular().getPeriodoLetivosVOs()) {
					index = 0;
					for (GradeDisciplinaVO gradeDisciplina : periodoLetivo.getGradeDisciplinaVOs()) {
						if (getRequerimentoVO().getDisciplina().getCodigo().equals(gradeDisciplina.getDisciplina().getCodigo())) {
							periodoLetivo.getGradeDisciplinaVOs().set(index, gradeDisciplinaIncluida);
							return;
						}
						index++;
					}
				}
			}
		}
	}
	
	public void consultarTurmaFiltroAnoSelecionado() {
		if (!getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada()) {
			limparDadosTurmaLiberacaoPreRequisitoChoqueHorario();
			return;
		}
		if (getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
			if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals("")) {
				montarListaSelectItemTurmaAdicionar();
			}
		}
		if (getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
			if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals("") && !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().equals("")) {
				montarListaSelectItemTurmaAdicionar();
			}
		}
	}
	
	public void consultarTurmaFiltroSemestreSelecionado() {
		if (!getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada()) {
			limparDadosTurmaLiberacaoPreRequisitoChoqueHorario();
			return;
		}
		if (getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
			if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().equals("") && !getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().equals("")) {
				montarListaSelectItemTurmaAdicionar();
			}
		}
	}
	
	public Boolean getReadOnlyCheckBoxInclusaoRequerimento() {
		return (!getRequerimentoVO().getCodigo().equals(0) && getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.REPOSICAO.getValor().toString()));
	}
	
	public Boolean getReadOnlyCheckBoxReposicaoRequerimento() {
		return (!getRequerimentoVO().getCodigo().equals(0) && getRequerimentoVO().getTipoRequerimento().getTipo().equals(TiposRequerimento.INCLUSAO_DISCIPLINA.toString()));
	}
	
	public UsuarioVO getUsuarioLibercaoPreRequisito() {
		if (usuarioLibercaoPreRequisito == null) {
			usuarioLibercaoPreRequisito = new UsuarioVO();
		}
		return usuarioLibercaoPreRequisito;
	}
	
	public void setUsuarioLibercaoPreRequisito(UsuarioVO usuarioLibercaoPreRequisito) {
		this.usuarioLibercaoPreRequisito = usuarioLibercaoPreRequisito;
	}
	
	public String getOncompleteLiberacaoPreRequisito() {
		if (getPreRequisitoLiberado() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDescricaoChoqueHorarioDisciplina())) {
			return "RichFaces.$('panelAutenticarUsuarioLiberacaoPreRequisito').hide(); RichFaces.$('panelIncluirDisciplinas').hide();";
		}else {
			return "RichFaces.$('panelAutenticarUsuarioLiberacaoPreRequisito').hide();";
		}
	}
	
	public Boolean getPreRequisitoLiberado() {
		if (preRequisitoLiberado == null) {
			preRequisitoLiberado = Boolean.FALSE;
		}
		return preRequisitoLiberado;
	}

	public void setPreRequisitoLiberado(Boolean preRequisitoLiberado) {
		this.preRequisitoLiberado = preRequisitoLiberado;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas() {
		if (matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas == null) {
			matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	}

	public void setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas) {
		this.matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas = matriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas;
	}

	/**
	 * @return the usernameLiberarMatriculaAcimaNrVagas
	 */
	public String getUsernameLiberarMatriculaAcimaNrVagas() {
		if (usernameLiberarMatriculaAcimaNrVagas == null) {
			usernameLiberarMatriculaAcimaNrVagas = "";
		}
		return usernameLiberarMatriculaAcimaNrVagas;
	}

	/**
	 * @param usernameLiberarMatriculaAcimaNrVagas
	 *            the usernameLiberarMatriculaAcimaNrVagas to set
	 */
	public void setUsernameLiberarMatriculaAcimaNrVagas(String usernameLiberarMatriculaAcimaNrVagas) {
		this.usernameLiberarMatriculaAcimaNrVagas = usernameLiberarMatriculaAcimaNrVagas;
	}

	public String getSenhaLiberarMatriculaAcimaNrVagas() {
		if (senhaLiberarMatriculaAcimaNrVagas == null) {
			senhaLiberarMatriculaAcimaNrVagas = "";
		}
		return senhaLiberarMatriculaAcimaNrVagas;
	}

	public void setSenhaLiberarMatriculaAcimaNrVagas(String senhaLiberarMatriculaAcimaNrVagas) {
		this.senhaLiberarMatriculaAcimaNrVagas = senhaLiberarMatriculaAcimaNrVagas;
	}

	public void verificarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena", getUsuarioLogado());
			setPermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena(Boolean.FALSE);
		}
	}
	
	public void validarPermissaoUsuarioIncluirExcluirApenasParaAlunosCursoCoordena() throws Exception {
		if (getPermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena()) {
			if (!getFacadeFactory().getCursoCoordenadorFacade().consultarPorFuncionarioUnidadeEnsinoTurma(getUsuarioLogado(), getMatriculaPeriodoVO())) {
				throw new Exception("Usuário não possui permissão para alterar alunos de cursos que o mesmo não seja coordenador.");
			}
		}
	}

	public Boolean getPermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena() {
		if(permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena == null){
			permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena = Boolean.FALSE;
		}
		return permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena;
	}

	public void setPermiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena(Boolean permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena) {
		this.permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena = permiteUsuarioIncluirExcluirApenasParaAlunosCursoCoordena;
	}

	public void prepararLiberarMatriculaAcimaNrMaximoVagasGrupoOptativa() {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens");
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarNrAlunosMatriculadosTurmaDisciplina(getMatriculaPeriodoVO(), obj.getMatriculaPeriodoTurmaDisciplinaVO(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getAno(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(), true, getInclusaoHistoricoForaPrazoVO().getReposicao());
			setMatriculaPeriodoTurmaDisciplinaLiberarAcimaNrVagas(obj.getMatriculaPeriodoTurmaDisciplinaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public String getMensagemTurma(){
		if (!Uteis.isAtributoPreenchido(getListaSelectItemTurmaAdicionar())) {
			if(getApresentarAno() && getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getAno().trim().isEmpty()){
				return "Informe o campo ANO.";
			}
			if(getApresentarSemestre() && getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getSemestre().trim().isEmpty()){
				return "Informe o campo SEMESTRE.";
			}
			return "Não existe turmas disponíveis para esta disciplina";
		}
		return "";
	}
	
	public void editarInclusaoGradeDisciplinaOptativa() {
		GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaGrupoOptativaItens");
		try {
			setGradeCurricularGrupoOptativaDisciplinaVO(obj);			
			setMatriculaPeriodoTurmaDisciplinaVOAdicionar((MatriculaPeriodoTurmaDisciplinaVO)obj.getMatriculaPeriodoTurmaDisciplinaVO().clone());
			getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina((DisciplinaVO)obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().clone());
		} catch (CloneNotSupportedException e) {			
		}
		setPanelAproveitamentoDisciplinaAberto(Boolean.TRUE);
	}
	
	
	public void cancelarInclusaoMatriculaPeriodoTurmaDisciplina() {
		if(Uteis.isAtributoPreenchido(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getGradeDisciplinaVO())){
			if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaIncluida()) {
				getGradeDisciplinaVO().setSelecionado(false);
			}
		}else {
			if (!getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaIncluida()) {
				getGradeCurricularGrupoOptativaDisciplinaVO().setSelecionado(false);
			}
		}
	}

	public Boolean getFiltrarTurmaAulaNaoOcorreu() {
		if (filtrarTurmaAulaNaoOcorreu == null) {
			filtrarTurmaAulaNaoOcorreu = false;
		}
		return filtrarTurmaAulaNaoOcorreu;
	}

	public void setFiltrarTurmaAulaNaoOcorreu(Boolean filtrarTurmaAulaNaoOcorreu) {
		this.filtrarTurmaAulaNaoOcorreu = filtrarTurmaAulaNaoOcorreu;
	}

	public Predicate<String> getPermissaoFuncionalidadeUsuarioLogado() {
		if (permissaoFuncionalidadeUsuarioLogado == null) {
			permissaoFuncionalidadeUsuarioLogado = s -> verificarPermissaoUsuarioLogadoPorFuncionalidade(s);
		}
		return permissaoFuncionalidadeUsuarioLogado;
	}

	public void setPermissaoFuncionalidadeUsuarioLogado(Predicate<String> permissaoFuncionalidadeUsuarioLogado) {
		this.permissaoFuncionalidadeUsuarioLogado = permissaoFuncionalidadeUsuarioLogado;
	}

	private boolean verificarPermissaoUsuarioLogadoPorFuncionalidade(String funcionalidade) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(funcionalidade, getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}
	
	private void removerMatriculaPeriodoTurmaDisciplinaPorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVO = executarDefinicaoQualMatriculaPeriodoUtilizar(matriculaPeriodoTurmaDisciplinaVO);
		if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
			Predicate<MatriculaPeriodoTurmaDisciplinaVO> isRemover = m -> 
					m.getDisciplina().getCodigo().equals(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo())
					&& m.getAno().equals(matriculaPeriodoTurmaDisciplinaVO.getAno())
					&& m.getSemestre().equals(matriculaPeriodoTurmaDisciplinaVO.getSemestre())
					&& m.getMatriculaPeriodoObjetoVO().getCodigo().equals(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getCodigo());
			matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().removeIf(isRemover);
		}
	}
	
	private void validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(MatriculaVO matriculaVO, Collection<MatriculaPeriodoVO> matriculaPeriodoVOs, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception {
		if (Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplinaVO)) {
			for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
				getFacadeFactory().getMatriculaPeriodoFacade().validarAlunoPodeCursarDisciplinaPorMapaEquivalencia(matriculaVO, matriculaPeriodoVO, mapaEquivalenciaDisciplinaVO, true);
			}
		}
	}
	
	private void limparDadosTurmaLiberacaoPreRequisitoChoqueHorario() {
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setTurma(new TurmaVO());
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoChoqueHorarioDisciplina("");
		getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDescricaoPreRequisitoDisciplina("");
	}
}
