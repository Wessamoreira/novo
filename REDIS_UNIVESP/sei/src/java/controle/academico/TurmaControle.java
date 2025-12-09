package controle.academico;

/**
 * TESTE SVN
 */

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turmaForm.jsp turmaCons.jsp) com
 * as funcionalidades da classe <code>Turma</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turma
 * @see TurmaVO
 */
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.ControleConsultaTurma;
import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AlteracaoPlanoFinanceiroAlunoTurmaVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.LogTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaInclusaoSugeridaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaUnidadeEnsinoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.SituacaoAlteracaoPlanoFinanceiroEnum;
import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoEstatisticaTurmaDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ChancelaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.Turma;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turmaForm.jsp turmaCons.jsp) com as funcionalidades da classe <code>Turma</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turma
 * @see TurmaVO
 */

@Controller("TurmaControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("unchecked")
public class TurmaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<SelectItem> listaSelectItemPlanoFinanceiroCurso;
	private List<String> listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso;
	private TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private TurmaAgrupadaVO turmaAgrupadaVO;
	private TurmaAberturaVO turmaAberturaVO;
	private String curso_Erro;
	private String periodicidadeTurma;
	private Boolean campoValorPorAluno;
	private List<SelectItem> listaSelectItemGrupoDestinatarios;
	private List<SelectItem> listaSelectItemChancela;
	private List<SelectItem> listaSelectItemContaCorrente;
	private Boolean apresentarAviso;
	private String mensagemComplementarApresentarAviso;
	private Integer quantidadeAlunosTurma;
	private List<MatriculaPeriodoVO> listaAlunosTurma;
	private String mensagemQuantidadeAlunos;
	private Boolean atualizarDisciplinaAlunos;
	private String mensagemAlteracaoContaCorrente;
	private Boolean existeContaReceberVinculadaOutraContaCorrente;
	private Boolean apresentarDataBaseGeracaoParcela;
	private List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase;
	private List<MatriculaVO> listaMatriculaSemControleGeracaoParcelaDataBase;
	private TurmaVO turmaPrincipal;
	private TurmaVO turmaInclusaoSugeridaVO;
	private List<TurmaDisciplinaVO> turmaDisciplinaSugeridaVOs;
	private TurmaDisciplinaVO turmaDisciplinaVO;
	private List<SelectItem> listaSelectItemTipoSubturma;
	private List<TurmaVO> turmaAgrupadaVOs;
	private List<TurmaVO> subturmaVOs;
	private Boolean permiteAlterarDadosEAD;
	private List<TurmaDisciplinaEstatisticaAlunoVO> turmaDisciplinaEstatisticaAlunoVOs;
	private TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO;
	private DisciplinaVO disciplinaVO;

	private Boolean permiteAlterarPlanoFinanceiro;
	private List<Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>> situacaoPlanoAlterado;

	public Boolean telaPrincipalAlteracaoPlanoFinanceiro;
	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> alteracoesPlanoFinanceiro;

	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComParcelaGerada;
	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComValorInferiorAoNovoPlano;
	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoNaoEncontrada;

	// Situacao de sucesso, existe somente uma condicao pagamento, = Lista de Alunos
	// que serao alterados
	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComUmaCondicaoPagamento;

	// Situacao de sucesso, porem existe mais de uma condicao pagamento
	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoDuplicidade;

	private Boolean habilitarListagemNovaCondicaoPagamento;
	private Boolean habilitarBotaGravarAlteracoesPlanoFinanceiro;

	private List<SelectItem> listaSelectItemNovaCondicaoPagamento;
	private ProgressBarVO progressBar;

	private List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoEmConformidade;

	/**
	 * Variavel criada unicamente para verificar se a turma foi salva antes de realizar a alteracao da condicao de pagamento dos alunos matriculados na turma
	 */
	private Boolean controle;

	public String situacaoExibidaNoDetalhe;

	public String situacaoAtualNoDetalhe;

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> logsAlteracoesPlanoFinanceiro;

	private List<SelectItem> listaSelectItemIndiceReajusteVOs;
	private String userNameLiberarAlteracaoPlanoFinanceiro;
	private String senhaLiberarAlteracaoPlanoFinanceiro;
	private List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs;
	private String mensagemLiberacaoAlteracaoPlanoFinanceiro;
	private Boolean apresentarAbaFinanceiro;
	private Boolean permitirAgruparTurmasUnidadeEnsinoDiferente;

	private List<SelectItem> listaSelectItemTextoPadraoContratoMatricula;
	private List<SelectItem> listaSelectItemTextoPadraoContratoExtensao;
	private List<SelectItem> listaSelectItemTextoPadraoContratoFiador;
	private TurmaContratoVO turmaContratoVO;
	private TurmaVO turmaNovaGradeCurricular;
	private List<SelectItem> listaSelectItemNovaGradeCurricular;
	private List<SelectItem> listaSelectItemPeriodoLetivoTemp;
	private String manterModalAberto;
	private List<MatriculaVO> listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula;
	private Date dataBaseGeracaoParcelasTemporaria;
	private List<SelectItem> listaSelectItemTurnoApresentarCenso;
	private List<MatriculaPeriodoVO> listaAlunosComTransferenciaMatrizCurricular;
	private List<MatriculaPeriodoVO> listaMatriculaPeriodo;
	private List<SelectItem> listaRegraDefinicaoVaga;
	private List<SelectItem> listaSelectItemUnidadeEnsinoClone;
	private Boolean apresentarIconeAlteracaoUnidadeEnsinoTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsinoAlteracaoTurma;
	private UnidadeEnsinoVO unidadeEnsinoAlteracaoTurma;
	protected List<MatriculaVO> listaAlunosUnidadeEnsinoAlterada;
	private List<String> listaErroAtualizarAluno;
	
	private ControleConsultaTurma controleConsultaTurma;

	public TurmaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Turma</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		setCampoValorPorAluno(Boolean.TRUE);
		setApresentarAviso(Boolean.FALSE);
		setTurmaVO(null);
		setTurmaAgrupadaVO(null);
		setValorConsultaTurma("");
		setCampoConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setListaSelectItemCurso(new ArrayList<SelectItem>(0));
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
		setAtualizarDisciplinaAlunos(Boolean.FALSE);
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemPlanoFinanceiroCurso(false);
		montarListaSelectItemTurnoApresentarCenso();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("turmaForm.xhtml");
	}

	public void verificarCampoValorFixo() {
		getTurmaVO().setPorcentagemChancela(Double.valueOf(0));
		setCampoValorPorAluno(Boolean.FALSE);
	}

	public void verificarCampoPorcentagem() {
		getTurmaVO().setValorFixoChancela(Double.valueOf(0));
		setCampoValorPorAluno(Boolean.TRUE);
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe <code>Turma</code> para alteracao. O objeto desta classe e disponibilizado na session da pagina (request) para que o JSP correspondente possa disponibiliza-lo para edicao.
	 */
	public String editar() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
		getFacadeFactory().getTurmaAberturaFacade().carregarDados(obj, getUsuarioLogado());
		getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade().carregarDados(obj, getUsuarioLogado());
		verificarVinculoMatricula(obj);
		obj.setNovoObj(Boolean.FALSE);
		setTurmaVO(obj);
		if (getTurmaVO().getSubturma()) {
			setTurmaPrincipal(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getTurmaPrincipal(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
		}
		turmaVO.setGradeCurricularAtiva(turmaVO.getGradeCurricularVO().getCodigo());
		montarDadosCompletosPeriodoLetivoTurma();
		setTurmaNovaGradeCurricular(new TurmaVO());
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setListaSelectItemCurso(new ArrayList<SelectItem>(0));
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
		montarListaSelectItemTurno();
		setTurmaAgrupadaVO(new TurmaAgrupadaVO());
		setValorConsultaTurma("");
		setCampoConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		montarListaSelectItemGradeCurricular(obj.getCurso().getCodigo());
		montarListaSelectItemPeriodoLetivo();
		montarListaSelectItemTipoSubturma();
		inicializarListasSelectItemTodosComboBox();
		montarListaSelectItemPlanoFinanceiroCurso(true);
		montarListaSelectItemCategoriaCondicoesPagamentoPlanoFinanceiroCurso();
		getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
		montarListaSelectItemModalidadeDisciplina();
		executarDefinicaoPeriodicidadeTurma();
		montarListaSelectItemTurnoApresentarCenso();
		// Carrega lista de logs
		exibirLogAlteracoesNoPlanoFinanceiroDosAlunosMatriculadosNaTurma();
		getTurmaVO().setListaTurmaAtualizacaoDisciplinaLog(getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().consultaRapidaPorTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		carregarListaSituacaoAberturaTurmaBase();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("turmaForm.xhtml");
	}

	public void verificarExistemOutrasTurmasQueReferenciamEstaTurmaImpedindoEdicao(TurmaVO obj) throws Exception {
		mensagemComplementarApresentarAviso = "";
		String mensagemComplementarApresentarAvisoAgrupada = "";
		String mensagemComplementarApresentarAvisoSubTurma = "";
		String mensagemComplementarApresentarAvisoAulaProgramada = "";
		getSubturmaVOs().clear();
		getTurmaAgrupadaVOs().clear();
		List<TurmaVO> listaSubTurma = getFacadeFactory().getTurmaFacade()
				.consultarExisteSubTurmaCadastradaTurmaPrincipal(obj.getCodigo(), false, getUsuarioLogado());
		if ((listaSubTurma != null) && (!listaSubTurma.isEmpty())) {
			setSubturmaVOs(listaSubTurma);
			setApresentarAviso(Boolean.TRUE);
			mensagemComplementarApresentarAvisoSubTurma = "<li>Existem subturmas vinculadas a esta turma acesse a aba Sub-Turmas.</li>";
		}
		List<TurmaVO> listaTurmaAgrupada = getFacadeFactory().getTurmaFacade()
				.consultarExisteTurmaAgrupadaEnvolvendoTurmaPrincipal(obj.getCodigo(), false, getUsuarioLogado());
		if ((listaTurmaAgrupada != null) && (!listaTurmaAgrupada.isEmpty())) {
			setTurmaAgrupadaVOs(listaTurmaAgrupada);
			setApresentarAviso(Boolean.TRUE);
			mensagemComplementarApresentarAvisoAgrupada = "<li>Existem turmas agrupadas vinculadas a esta turma acesse a aba Turmas Agrupadas.</li>";
		}
		mensagemComplementarApresentarAviso = mensagemComplementarApresentarAvisoAgrupada;
		if (!mensagemComplementarApresentarAvisoSubTurma.equals("")) {
			mensagemComplementarApresentarAviso = mensagemComplementarApresentarAviso + "<br/>"
					+ mensagemComplementarApresentarAvisoSubTurma;
		}
		List<HorarioTurmaVO> horarioTurmaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurma(
				obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		if (Uteis.isAtributoPreenchido(horarioTurmaVOs)) {
			setApresentarAviso(Boolean.TRUE);
			mensagemComplementarApresentarAvisoAulaProgramada = "<li>Existem aulas programadas para esta turma, portanto algumas informações não poderão ser alteradas.</li>";
			mensagemComplementarApresentarAviso = mensagemComplementarApresentarAviso + "<br/>"
					+ mensagemComplementarApresentarAvisoAulaProgramada;
		}

	}

	public void verificarVinculoMatricula(TurmaVO obj) throws Exception {
		try {
			if (!getFacadeFactory().getTurmaFacade().consultarExisteMatriculaVinculadaTurma(obj, false, getUsuarioLogado())) {
				setApresentarAviso(Boolean.FALSE);
				verificarExistemOutrasTurmasQueReferenciamEstaTurmaImpedindoEdicao(obj);
			} else {
				setApresentarAviso(Boolean.TRUE);
				verificarExistemOutrasTurmasQueReferenciamEstaTurmaImpedindoEdicao(obj);
				setListaMatriculaComControleGeracaoParcelaDataBase(getFacadeFactory().getMatriculaFacade().consultaRapidaPorAlteracaoDataBaseTurma(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, true, false, getUsuarioLogado()));
				setListaMatriculaSemControleGeracaoParcelaDataBase(getFacadeFactory().getMatriculaFacade().consultaRapidaPorAlteracaoDataBaseTurma(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, false, false, getUsuarioLogado()));
				setListaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorAlteracaoDataBaseTurma(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, false, true, getUsuarioLogado()));
			}
			getFacadeFactory().getTurmaDisciplinaFacade().realizarVerificacaoExistenciaAlunoMatriculaOUAulaProgramadaTurmaDisciplina(obj);
		} catch (Exception e) {
			setApresentarAviso(Boolean.TRUE);
		}
	}

	public void definirPeriodicidadeTurma() throws Exception {
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setAnual(getTurmaPrincipal().getAnual());
			getTurmaVO().setSemestral(getTurmaPrincipal().getSemestral());
		} else if (getTurmaVO().getTurmaAgrupada()) {
			for (TurmaAgrupadaVO taVO : getTurmaVO().getTurmaAgrupadaVOs()) {
				getTurmaVO().setSemestral(taVO.getTurma().getCurso().getSemestral());
				getTurmaVO().setAnual(taVO.getTurma().getCurso().getAnual());
				break;
			}
		} else {
			getTurmaVO().setSemestral(getTurmaVO().getCurso().getSemestral());
			getTurmaVO().setAnual(getTurmaVO().getCurso().getAnual());
		}
		
		if(getTurmaVO().getSubturma() || getTurmaVO().getTurmaAgrupada()) {
			getTurmaVO().setChancelaVO(null);
			getTurmaVO().setTipoChancela("");
			getTurmaVO().setPorcentagemChancela(0.0);
			getTurmaVO().setValorFixoChancela(0.0);
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Turma</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			definirPeriodicidadeTurma();
			if (turmaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTurmaFacade().executarInicializacaoDisciplinasTurma(turmaVO, getUsuarioLogado());
				turmaVO.setGradeCurricularAtiva(turmaVO.getGradeCurricularVO().getCodigo());
				getFacadeFactory().getTurmaFacade().incluir(turmaVO, getUsuarioLogado());
				getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().executarLogAtualizacaoDisciplinaTurma(
						getTurmaVO(), getTurmaVO().getTurmaDisciplinaVOs(), getUsuarioLogado());
				gravarLogTurma("inclusao");
			} else {
				// Isto está comentado, pois esta atualização não pode ser
				// realizada automaticamente,
				// pois agora existem turmas que utilizam somente algumas
				// disciplinas da Grade.
				// Adicionalmente, existem turmas agrupadas que possuem
				// disciplinas comuns. Que também,
				// dependem das disciplinas relacionada em cada uma das turmas
				// agrupadas.
				// Boolean trocou = verificarAlteracaoGradeCurricular();
				// if (trocou) {
				// turmaVO.setTurmaDisciplinaVOs(new ArrayList(0));
				// turmaVO.incluirListaTurmaDisciplina(consultarListaGradeDisciplina());
				// }
				if (!getApresentarAviso()) {
					verificarVinculoMatricula(getTurmaVO());
					if (getApresentarAviso()) {
						setApresentarAviso(false);
						throw new Exception(
								"Foram realizadas operações por outro usuário usando esta turma, portanto é necessário consultar e editar esta turma para realizar as alterações necessárias.");
					}
				}
				verificarNecessidadeAtualizarDisciplinaAlteracaoGradeCurricular();
				turmaVO.setGradeCurricularAtiva(turmaVO.getGradeCurricularVO().getCodigo());
				getFacadeFactory().getTurmaFacade().alterar(turmaVO, getUsuarioLogado());				
//				getFacadeFactory().getTurmaFacade().executarAtualizacaoDisciplinaAlunosTurma(getAtualizarDisciplinaAlunos(), getListaAlunosTurma(), getTurmaVO(),getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado(), getUsuarioLogado());
				gravarLogTurma("alteracao");
			}
			getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(null, null, false, true);
			setMensagemID("msg_dados_gravados");
			setControle(true);
		} catch (Exception e) {
			setControle(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			setAtualizarDisciplinaAlunos(Boolean.FALSE);
		}
	}
	
	public void persistirClone() {
		try {
			getFacadeFactory().getTurmaFacade().persistirTurmaClone(getTurmaVO(), getUsuarioLogado());
			getTurmaVO().setTurmaUnidadeEnsino(new TurmaUnidadeEnsinoVO());
			setOncompleteModal("RichFaces.$('panelUnidadeEnsino').hide();");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setAtualizarDisciplinaAlunos(Boolean.FALSE);
		}
	}
	
	public void alterarDataAberturaTurmaBaseAgrupada() {
		try {
			getFacadeFactory().getTurmaAberturaFacade().alterarDataAberturaTurmaBaseAgrupada(getTurmaVO().getTurmaAgrupadaVOs(), getTurmaAberturaVO(), getUsuarioLogado());
			carregarListaSituacaoAberturaTurmaBase();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerTurmaBaseAbertura() throws Exception {
		TurmaAberturaVO turmaAberturaVO = (TurmaAberturaVO) context().getExternalContext().getRequestMap().get("turmaAberturaItens");
		getFacadeFactory().getTurmaAberturaFacade().excluir(turmaAberturaVO);
		carregarListaSituacaoAberturaTurmaBase();
		setMensagemID("msg_dados_excluidos");
	}
	
	public void carregarListaSituacaoAberturaTurmaBase() throws Exception {
		if(getTurmaVO().getTurmaAgrupada()) {
			getTurmaVO().getListaSituacaoAberturaTurmaBase().clear();
			getFacadeFactory().getTurmaAberturaFacade().carregarDadosTurmaAberturaTurmaBase(getTurmaVO(), getUsuarioLogado());
		}
	}
	
	public void clonar() {
		try {
			setApresentarAviso(false);
			setOncompleteModal("");
			if(!getTurmaVO().getSubturma() && !getTurmaVO().getTurmaAgrupada()) {
				inicializarDadosListaSelectItemUnidadeEnsino();
				setOncompleteModal("RichFaces.$('panelUnidadeEnsino').show();");
				getFacadeFactory().getTurmaFacade().inicializarDadosUnidadeEnsinoSelecionada(getTurmaVO(),getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(0, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), getUsuarioLogado());
			}else {
				setTurmaVO(getFacadeFactory().getTurmaFacade().clonar(getTurmaVO(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
	}
	
	public void verificarVinculaTurmaAgrupadaTurmaBase() {
		try {
			setOncompleteModal("");
			if(getTurmaVO().getTurmaUnidadeEnsino().getVincularTurmasCloneNaTurmaAgrupada()) {
				getFacadeFactory().getTurmaFacade().consultarTurmaBaseVinculadaTurmaBase(getTurmaVO(), getUsuarioLogado());
				if(Uteis.isAtributoPreenchido(getTurmaVO().getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getCodigo())) {
					setOncompleteModal("RichFaces.$('panelConfirmaçãoTurmaAgrupada').show();");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void cancelarTurmaAgrupada(){
		setOncompleteModal("");
		getTurmaVO().getTurmaUnidadeEnsino().setTurmaAgrupadaVO(new TurmaAgrupadaVO());
	}
	

	
	  public void inicializarDadosListaSelectItemUnidadeEnsino() {
	        try {
	            List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            setListaSelectItemUnidadeEnsinoClone(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        }
	    }
	
	public void selecionarUnidadeEnsinoTurma() {
		TurmaUnidadeEnsinoVO obj = (TurmaUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
	}
	
	public void cancelarClone(){
		setOncompleteModal("");
		getTurmaVO().setTurmaUnidadeEnsino(new TurmaUnidadeEnsinoVO());
		getTurmaVO().getListaTurmaUnidadeEnsino().clear();
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (TurmaUnidadeEnsinoVO turmaUnidadeEnsinoVO : getTurmaVO().getListaTurmaUnidadeEnsino()) {
			if (getMarcarTodasUnidadeEnsino()) {
				turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
	}
	
	public void verificarTodasUnidadesSelecionadas() {

	}

	public void persistirAlteracaoDataBaseGeracaoParcela() {
		try {
			getFacadeFactory().getTurmaFacade().alterarDataBaseGeracaoTurmaParcela(getTurmaVO(), getDataBaseGeracaoParcelasTemporaria(), getListaMatriculaComControleGeracaoParcelaDataBase(), getUsuarioLogado());
			getTurmaVO().setDataBaseGeracaoParcelas(getDataBaseGeracaoParcelasTemporaria());
			gravarLogTurma("alteracao");
			setMensagemID("msg_dados_gravados");
			setControle(true);
		} catch (Exception e) {
			setControle(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			setAtualizarDisciplinaAlunos(Boolean.FALSE);
		}
	}

	public void exibirLogAlteracoesNoPlanoFinanceiroDosAlunosMatriculadosNaTurma() {
		setLogsAlteracoesPlanoFinanceiro(getFacadeFactory().getAlteracaoPlanoFinanceiroAlunoTurmaFacade()
				.consultarAlunosQueSofreramAlteracaoNaCondicaoPagamento(getTurmaVO(),
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
	}

	public List<GradeDisciplinaVO> consultarListaGradeDisciplina() {
		try {
			List<GradeDisciplinaVO> listaRetorno = getFacadeFactory().getGradeDisciplinaFacade()
					.consultarGradeDisciplinas(getTurmaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
			return listaRetorno;
		} catch (Exception e) {
			return new ArrayList<GradeDisciplinaVO>(0);
		}
	}

	public void gravarLogTurma(String acao) {
		try {
			LogTurmaVO logTurmaVO = new LogTurmaVO();
			logTurmaVO.setTurma(getTurmaVO());
			logTurmaVO.setCurso(getTurmaVO().getCurso());
			logTurmaVO.setGradeCurricular(getTurmaVO().getGradeCurricularVO());
			logTurmaVO.setPeriodoLetivo(getTurmaVO().getPeridoLetivo());
			logTurmaVO.setContaCorrente(getTurmaVO().getContaCorrente());
			logTurmaVO.setUsuarioResponsavel(getUsuarioLogadoClone());
			if (Uteis.isAtributoPreenchido(getTurmaVO().getDataBaseGeracaoParcelas())) {
				logTurmaVO.setDataBaseGeracaoParcelas(getTurmaVO().getDataBaseGeracaoParcelas());
			}
			logTurmaVO.setAcao(acao);
			getFacadeFactory().getLogTurmaFacade().incluir(logTurmaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean verificarAlteracaoGradeCurricular() {
		if (turmaVO.getGradeCurricularVO().getCodigo().equals(turmaVO.getGradeCurricularAtiva())) {
			return false;
		}
		return true;
	}

	public void montarListaSelectItemConfiguracaoAcademico() {
		try {
			List<ConfiguracaoAcademicoVO> resultadoConsulta = getFacadeFactory().getConfiguracaoAcademicoFacade()
					.consultarPorUnidadeEnsinoNivelCombobox(getTurmaVO().getUnidadeEnsino().getCodigo(), false,
							getUsuarioLogado());
			setListaSelectItemConfiguracaoAcademico(
					UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			
			super.consultar();
			if(Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				getControleConsultaTurma().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			}
			getFacadeFactory().getTurmaFacade().consultarTurma(getControleConsultaTurma(), getControleConsultaTurma().getUnidadeEnsinoVO().getCodigo(), null, null, null, null, null, null, null, getUsuarioLogado());
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("turmaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("turmaCons.xhtml");
		}

	}

	public void consultarTurma() {
		try {
			Integer codigoUnidadeEnsino = 0;
			if (getTurmaVO().getSubturma()) {
				codigoUnidadeEnsino = getTurmaVO().getUnidadeEnsino().getCodigo();
			}
			if (getTurmaVO().getTurmaAgrupada() && !getTurmaVO().getSubturma()) {
				if(!getPermitirAgruparTurmasUnidadeEnsinoDiferente() && !Uteis.isAtributoPreenchido(getTurmaVO().getUnidadeEnsino().getCodigo())) {
					throw new Exception("Selecione uma Unidade de Ensino!");
				}
				if(!getPermitirAgruparTurmasUnidadeEnsinoDiferente() && Uteis.isAtributoPreenchido(getTurmaVO().getUnidadeEnsino().getCodigo())) {
					codigoUnidadeEnsino = getTurmaVO().getUnidadeEnsino().getCodigo();
				}
			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(),
						getTurmaVO().getCodigo(), codigoUnidadeEnsino, false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(),
						getTurmaVO().getCodigo(), codigoUnidadeEnsino, false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(),
						getTurmaVO().getCodigo(), codigoUnidadeEnsino, false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(),
						getTurmaVO().getCodigo(), codigoUnidadeEnsino, false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaInclusaoSugerida() {
		try {

			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoECurso(
						getValorConsultaTurma(), getTurmaVO().getCurso().getCodigo(),
						getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultaRapidaPorNomeUnidadeEnsinoCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(
								getValorConsultaTurma(), getTurmaVO().getCurso().getCodigo(),
								getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultaRapidaNomeCursoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(
								getValorConsultaTurma(), getTurmaVO().getCurso().getCodigo(),
								getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade()
						.consultaRapidaPorTurnoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(
								getValorConsultaTurma(), getTurmaVO().getCurso().getCodigo(),
								getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurmaInclusaoSugerida() {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaInclusaoSugeridaItens");
			getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade()
					.validarDadosSelecaoTurma(getTurmaVO(), turma);
			setTurmaInclusaoSugeridaVO(turma);
			consultarTurmaDisciplinaPorTurma();
			setValorConsultaTurma("");
			getListaConsultaTurma().clear();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurmaInclusaoSugerida() {
		setTurmaInclusaoSugeridaVO(null);
	}

	public void consultarTurmaDisciplinaPorTurma() {
		setTurmaDisciplinaSugeridaVOs(getFacadeFactory().getTurmaDisciplinaFacade().consultaRapidaPorTurma(
				getTurmaInclusaoSugeridaVO().getCodigo(), getTurmaVO().getCodigo(), getUsuarioLogado()));
	}

	public Boolean getApresentarMensagemDisciplinaSugeridaNaoEncontrada() {
		return getTurmaDisciplinaSugeridaVOs().isEmpty();
	}

	public void adicionarTurmaDisciplinaInclusaoSugerida() {
		getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade().adicionarTurmaDisciplinaInclusaoSugerida(
				getTurmaVO().getTurmaDisciplinaInclusaoSugeridaVOs(), getTurmaDisciplinaSugeridaVOs());
	}

	public void removerTurmaDisciplinaInclusaoSugerida() {
		try {
			TurmaDisciplinaInclusaoSugeridaVO obj = (TurmaDisciplinaInclusaoSugeridaVO) context().getExternalContext()
					.getRequestMap().get("turmaDisciplinaInclusaoSugeridaItens");
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getTurmaDisciplinaVO().getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getTurmaDisciplinaVO().getDisciplina().getNome()));
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getTurmaDisciplinaVO().getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getTurmaDisciplinaVO().getDisciplina().getNome()));
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getTurmaDisciplinaVO().getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getTurmaDisciplinaVO().getDisciplina().getNome()));
			getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade()
					.removerTurmaDisciplinaInclusaoSugerida(getTurmaVO().getTurmaDisciplinaInclusaoSugeridaVOs(), obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerDisciplinaTurma() {
		try {
			TurmaDisciplinaVO obj = (TurmaDisciplinaVO) context().getExternalContext().getRequestMap()
					.get("turmaDisciplinaItens");
			if (obj.getGradeDisciplinaVO().getDisciplinaComposta()) {
				executarGeracaoTurmaDisciplinaComposta();
				for (TurmaDisciplinaCompostaVO tdcVO : obj.getTurmaDisciplinaCompostaVOs()) {
					if (!tdcVO.isEditavel()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
								obj.getDisciplina().getNome()));
					}
				}
			} else {
				if (getFacadeFactory().getTurmaFacade()
						.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
								obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
					throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
							obj.getDisciplina().getNome()));
			}
			if (obj.getGradeDisciplinaVO().getDisciplinaComposta()) {
				executarGeracaoTurmaDisciplinaComposta();
				for (TurmaDisciplinaCompostaVO tdcVO : obj.getTurmaDisciplinaCompostaVOs()) {
					if (!tdcVO.isEditavel()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
								obj.getDisciplina().getNome()));
					}
				}
			} else {
				if (getFacadeFactory().getTurmaFacade()
						.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
								obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
					throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
							obj.getDisciplina().getNome()));
			}
			if (obj.getGradeDisciplinaVO().getDisciplinaComposta()) {
				executarGeracaoTurmaDisciplinaComposta();
				for (TurmaDisciplinaCompostaVO tdcVO : obj.getTurmaDisciplinaCompostaVOs()) {
					if (!tdcVO.isEditavel()) {
						throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
								obj.getDisciplina().getNome()));
					}
				}
			} else {
				if (getFacadeFactory().getTurmaFacade()
						.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
								obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
					throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
							obj.getDisciplina().getNome()));
			}
			this.getTurmaVO().getTurmaDisciplinaVOs().remove(obj);
			getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerDisciplinaComumTurmaAgrupada() {
		try {
			TurmaDisciplinaVO obj = (TurmaDisciplinaVO) context().getExternalContext().getRequestMap()
					.get("turmaDisciplinaItens");
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getDisciplina().getNome()));
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getDisciplina().getNome()));
			if (getFacadeFactory().getTurmaFacade()
					.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(getTurmaVO().getCodigo(),
							obj.getDisciplina().getCodigo(), false, getUsuarioLogado()))
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_removerDisciplina").replace("{0}",
						obj.getDisciplina().getNome()));
			this.getTurmaVO().getTurmaDisciplinaVOs().remove(obj);
			getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarDisciplinasTurmaComBaseGradeCurricular() {
		try {
			List<GradeDisciplinaVO> listaGradeDisciplina = consultarListaGradeDisciplina();
			if (getTurmaVO().getSubturma() && getTurmaVO().isNovoObj()) {
				for (TurmaDisciplinaVO turmaDisciplina : getTurmaVO().getTurmaDisciplinaVOs()) {
					turmaDisciplina.setCodigo(0);
					turmaDisciplina.setNovoObj(true);
				}
			}
			getFacadeFactory().getTurmaFacade().executarGeracaoTurmaDisciplinaVOs(getTurmaVO(), listaGradeDisciplina, getUsuarioLogado());
			turmaVO.atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			obterSalaLocalAulaCasoExistesseAnteriormente();
			montarListaSelectItemModalidadeDisciplina();
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().executarLogAtualizacaoDisciplinaTurma(getTurmaVO(), getTurmaVO().getTurmaDisciplinaVOs(), getUsuarioLogado());
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAtualizacaoDisciplinaTurmaRealizado(getTurmaVO(), getUsuarioLogado());
			gravarLogTurma("atualizacaoListaDisciplinas");
			setMensagemID("msg_Turma_CarregarDisciplinasDaGrade");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void obterSalaLocalAulaCasoExistesseAnteriormente() throws Exception {
		Iterator<TurmaDisciplinaVO> i = getTurmaVO().getTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			TurmaDisciplinaVO turmaDisc = (TurmaDisciplinaVO) i.next();
			getFacadeFactory().getTurmaFacade().obterLocalSalaTurmaDisciplinaLog(turmaDisc, getTurmaVO().getCodigo());
		}
	}

	public void montarListaDisciplinaComunsTurmasAgrupadas() {
		try {
			getFacadeFactory().getTurmaAgrupadaFacade()
					.executarBuscaPorDisciplinasComunsEntreTurmasAgrupadas(this.getTurmaVO(), getUsuarioLogado());
			getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			montarListaSelectItemModalidadeDisciplina();
			setMensagemID("msg_Turma_DisciplinasCarregadasComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			getTurmaAgrupadaVO().setTurma((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			getTurmaAgrupadaVO().setTurmaOrigem(getTurmaVO().getCodigo());
			getTurmaVO().adicionarObjTurmaAgrupadaVOs(getTurmaAgrupadaVO());
			retirarTurmaListaConsulta(getTurmaAgrupadaVO().getTurma().getCodigo());
			setTurmaAgrupadaVO(new TurmaAgrupadaVO());
			executarDefinicaoPeriodicidadeTurma();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void retirarTurmaListaConsulta(Integer turma) {
		int index = 0;
		for (TurmaVO obj : (List<TurmaVO>) listaConsultaTurma) {
			if (obj.getCodigo().intValue() == turma.intValue()) {
				listaConsultaTurma.remove(index);
				return;
			}
			index++;
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt,
						turmaVO.getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(
						getValorConsultaCurso(), turmaVO.getUnidadeEnsino().getCodigo(), false, "", false,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext()
					.getRequestMap().get("unidadeensinocursoItens");
			getTurmaVO().setCurso(unidadeEnsinoCurso.getCurso());
			getTurmaVO().setTurno(unidadeEnsinoCurso.getTurno());
			montarListaSelectItemGradeCurricular(getTurmaVO().getCurso().getCodigo());
			GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade()
					.consultarPorSituacaoGradeCurso(getTurmaVO().getCurso().getCodigo(), "AT", false,
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (gradeCurricularVO.getCodigo() == 0) {
				throw new ConverterException("Não existe uma Grade Curricular Ativa para este curso.");
			}
			getTurmaVO().setTurmaDisciplinaVOs(new ArrayList<TurmaDisciplinaVO>(0));
			getTurmaVO().setGradeCurricularVO(gradeCurricularVO);
			getTurmaVO().setCurso(
					getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getTurmaVO().getCurso().getCodigo(),
							Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
			getTurmaVO().getCurso().setConfiguracaoAcademico(
					getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimariaDadosMinimos(
							getTurmaVO().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
			executarDefinicaoPeriodicidadeTurma();
			montarListaSelectItemPeriodoLetivo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemGradeCurricular(Integer prm) {
		List<GradeCurricularVO> resultadoConsulta = null;
		Iterator<GradeCurricularVO> i = null;
		try {
			resultadoConsulta = consultarGradeCurricularCurso(prm);
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				GradeCurricularVO obj = (GradeCurricularVO) i.next();
				if (!obj.getSituacao().equals("CO")) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			if (Uteis.isAtributoPreenchido(getTurmaVO()) 
					&& Uteis.isAtributoPreenchido(getTurmaVO().getGradeCurricularVO())
					&& resultadoConsulta.stream().noneMatch(gc -> gc.getCodigo().equals(getTurmaVO().getGradeCurricularVO().getCodigo()))) {
				objs.add(new SelectItem(getTurmaVO().getGradeCurricularVO().getCodigo(), getTurmaVO().getGradeCurricularVO().getNome()));
			}
			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemGradeCurricular(objs);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<GradeCurricularVO> consultarGradeCurricularCurso(Integer prm) throws Exception {
		if (getApresentarAviso()) {
			return getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(prm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		return getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtivaPorCodigoCurso(prm, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public void selecionouUnidadeEnsino() {
		getTurmaVO().setCurso(new CursoVO());
		getTurmaVO().setTurno(new TurnoVO());
		getTurmaVO().setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
		// getTurmaVO().setTurmaPreMatricula(Boolean.FALSE);
		getTurmaVO().setGradeCurricularVO(new GradeCurricularVO());
		getTurmaVO().setPeridoLetivo(new PeriodoLetivoVO());
		getTurmaVO().setTurmaDisciplinaVOs(new ArrayList<TurmaDisciplinaVO>(0));
		setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
		setListaSelectItemCurso(new ArrayList<SelectItem>(0));
		montarListaSelectItemPlanoFinanceiroCurso(false);
		montarListaSelectItemContaCorrente();
		montarListaSelectItemConfiguracaoAcademico();
		montarDadosChancela();
	}

	private void montarDadosChancela() {
		try {
			if(Uteis.isAtributoPreenchido(getTurmaVO().getUnidadeEnsino().getCodigo())) {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(0, getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				unidadeEnsinoVO = unidadeEnsinoVOs.iterator().next();
				getTurmaVO().setChancelaVO(unidadeEnsinoVO.getChancelaVO());
				getTurmaVO().setTipoChancela(unidadeEnsinoVO.getTipoChancela());
				getTurmaVO().setPorcentagemChancela(unidadeEnsinoVO.getPorcentagemChancela());
				getTurmaVO().setValorFixoChancela(unidadeEnsinoVO.getValorFixoChancela());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
		
	}

	public boolean getExisteUnidadeEnsino() {
		if (getTurmaVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>TurmaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTurmaFacade().excluir(turmaVO, getUsuarioLogado());
			gravarLogTurma("exclusao");
			setTurmaVO(new TurmaVO());
			setTurmaPrincipal(new TurmaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("turmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("turmaForm.xhtml");
		}

	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemSituacaoTurma() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoTurmas = (Hashtable) Dominios.getSituacaoTurma();
		Enumeration keys = situacaoTurmas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoTurmas.get(value);
			objs.add(new SelectItem(value, label));
		}
		Collections.sort(objs, new SelectItemOrdemValor());
		return objs;
	}

	public List<SelectItem> getListaSelectItemSituacaoTurmaAbertura() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("AC", "A CONFIRMAR"));
		objs.add(new SelectItem("AD", "ADIADA"));
		objs.add(new SelectItem("CO", "CONFIRMADA"));
		objs.add(new SelectItem("IN", "INAUGURADA"));
		objs.add(new SelectItem("CA", "CANCELADA"));
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemAcaoTurmaBase() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("", ""));
		objs.add(new SelectItem("EI", "Excluir os Registros de Abertura das Turma Base e Incluir Somente Este Registro"));
		objs.add(new SelectItem("IR", "Incluir Este Registro"));
		return objs;
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	@SuppressWarnings("unchecked")
	public List<TurnoVO> consultarTurnoPorCodigo(Integer codigoCurso) throws Exception {
		return getFacadeFactory().getTurnoFacade().consultarPorCodigo(codigoCurso, false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurno() {
		List<TurnoVO> resultadoConsulta = null;
		Iterator<TurnoVO> i = null;
		try {
			resultadoConsulta = consultarTurnoPorCodigo(0);
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurnoVO obj = (TurnoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}

	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemCurso() {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoCursoVO> i = null;
		try {
			resultadoConsulta = consultarCursoPorUnidadeEnsino(this.getTurmaVO().getUnidadeEnsino().getCodigo());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) i.next();
				objs.add(new SelectItem(obj.getCurso().getCodigo(), obj.getCurso().getNome()));
			}
			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemCurso(objs);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}

	}

	public List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer prm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarUnidadeEnsinoCursos(prm,
				Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade()
					.consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false,
							Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty() || getApresentarAviso()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}

			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				if (!super.getUnidadeEnsinoLogado().getCodigo().equals(0)
						&& obj.getCodigo().equals(super.getUnidadeEnsinoLogado().getCodigo())) {
					getTurmaVO().setUnidadeEnsino(obj);
				}
			}

			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<PlanoFinanceiroCursoVO> consultarPlanoFinanceiroCurso(Boolean editandoTurma) throws Exception {
		if (editandoTurma) {
			return getFacadeFactory().getPlanoFinanceiroCursoFacade()
					.consultarPlanoFinanceiroTurmaEspecificaEPlanoFinanceiroAtivoUnidade(getTurmaVO().getCodigo(),
							getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado());
		}
		return getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorUnidadeEnsino(
				getTurmaVO().getUnidadeEnsino().getCodigo(), SituacaoPlanoFinanceiroCursoEnum.ATIVO, false,
				Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public List<String> consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso() throws Exception {
		return getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade()
				.consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso(
						getTurmaVO().getPlanoFinanceiroCurso().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemPlanoFinanceiroCurso(Boolean editandoTurma) {
		try {
			montarListaSelectItemPlanoFinanceiroCurso("", editandoTurma);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void montarListaSelectItemPlanoFinanceiroCurso(String prm, Boolean editandoTurma) throws Exception {
		List<PlanoFinanceiroCursoVO> resultadoConsulta = consultarPlanoFinanceiroCurso(editandoTurma);
		setListaSelectItemPlanoFinanceiroCurso(
				UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
	}

	public void montarListaSelectItemCategoriaCondicoesPagamentoPlanoFinanceiroCurso() throws Exception {
		setListaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso(
				consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso());
		if (Uteis.isAtributoPreenchido(getTurmaVO()) && Uteis.isAtributoPreenchido(getTurmaVO().getCategoriaCondicaoPagamento())
				&& !getListaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso().contains(getTurmaVO().getCategoriaCondicaoPagamento())) {
			getListaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso().add(getTurmaVO().getCategoriaCondicaoPagamento());
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm,
				super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemGrupoDestinatarios() {
		try {
			List<GrupoDestinatariosVO> lista = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorCodigo(0,
					false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaSelectItemGrupoDestinatarios(UtilSelectItem.getListaSelectItem(lista, "codigo", "nomeGrupo"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemChancela() {
		List<ChancelaVO> lista = new ArrayList<ChancelaVO>(0);
		try {
			lista = getFacadeFactory().getChancelaFacade().consultarPorCodigo(0, false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaSelectItemChancela(UtilSelectItem.getListaSelectItem(lista, "codigo", "instituicaoChanceladora"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			lista = null;
		}
	}

	public void montarListaSelectItemContaCorrente() {
		List<ContaCorrenteVO> lista = new ArrayList<ContaCorrenteVO>(0);
		try {
			lista = getFacadeFactory().getContaCorrenteFacade().consultarPorCodigoSomenteContasCorrente(0,
					getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
			setListaSelectItemContaCorrente(UtilSelectItem.getListaSelectItem(lista, "codigo", "numeroDigito"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			lista = null;
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemContaCorrente();		
		montarListaSelectItemConfiguracaoAcademico();
		montarListaSelectItemTurno();
		montarListaSelectItemGrupoDestinatarios();
		montarListaSelectItemChancela();
		montarComboBoxConfiguracaoNotaFiscal();
		montarListaSelectItemModalidadeDisciplina();
		montarComboBoxConfiguracaoEAD();
		montarComboBoxAvaliacaoOnline();
		montarListaSelectItemIndiceReajustePreco();
	}

	public String removerRegistroTurma() {
		TurmaAgrupadaVO obj = (TurmaAgrupadaVO) context().getExternalContext().getRequestMap()
				.get("turmaAgrupadaItens");
		try {
			getFacadeFactory().getTurmaFacade().validarDadosRemocaoTurmaAgrupada(getTurmaVO(), obj, getUsuarioLogado());
			getTurmaVO().getTurmaAgrupadaVOs().remove(obj);
			limparMensagem();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "excluir";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		//itens.add(new SelectItem("situacaoTurma", "Situação (Aberta/Fechada)"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSituacaoTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AB", "Aberta"));
		itens.add(new SelectItem("FE", "Fechada"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {		
		setApresentarAviso(Boolean.FALSE);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("turmaCons.xhtml");
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>PeriodoLetivo</code>.
	 */
	public void montarListaSelectItemPeriodoLetivo(String prm) throws Exception {
		if (!getTurmaVO().getGradeCurricularAtiva().equals(getTurmaVO().getGradeCurricularVO().getCodigo())) {
			getTurmaVO().setDisciplinasAtualizadasAlteracaoMatrizCurricular(false);
		}
		List<PeriodoLetivoVO> resultadoConsulta = consultarPeriodoLetivoPorGradecurricular(
				getTurmaVO().getGradeCurricularVO().getCodigo());
		setListaSelectItemPeriodoLetivo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
	}

	public void montarDadosCompletosPeriodoLetivoTurma() throws Exception {
		try {
			if (this.getTurmaVO().getTurmaAgrupada() || getTurmaVO().getSubturma()) {
				return;
			}
			if (Uteis.isAtributoPreenchido(getTurmaVO().getPeridoLetivo())) {
				PeriodoLetivoVO periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(this.getTurmaVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				this.getTurmaVO().setPeridoLetivo(periodoLetivoVO);
				if ((this.getTurmaVO().getPeridoLetivo().getControleOptativaGrupo()) && (!this.getTurmaVO().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo().equals(0))) {
					GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(this.getTurmaVO().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
					this.getTurmaVO().getPeridoLetivo().setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
				}
				validarTurmaDisciplinaQueNaoEstaoNoPeriodoLetivoDaTurma();
				/**
				 * Atualiza a lista de disciplinas da turma quando for um novo cadastro.
				 */
				if (getTurmaVO().isNovoObj() || getTurmaVO().getTurmaDisciplinaVOs().isEmpty()) {
					atualizarDisciplinasTurmaComBaseGradeCurricular();
				}
				this.getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_peridoLetivoSelecionado"));
			}
			montarListaSelectItemModalidadeDisciplina();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void validarTurmaDisciplinaQueNaoEstaoNoPeriodoLetivoDaTurma() {
		for (Iterator<TurmaDisciplinaVO> iterator = getTurmaVO().getTurmaDisciplinaVOs().iterator(); iterator.hasNext();) {
			TurmaDisciplinaVO turmaDisciplinaVO = iterator.next();
			if (turmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa()
					&& !turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO()
							.getGradeCurricularGrupoOptativa().getCodigo().equals(this.getTurmaVO()
									.getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo())) {
				iterator.remove();
			} else if (!turmaDisciplinaVO.getDisciplinaReferenteAUmGrupoOptativa()
					&& !turmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo()
							.equals(this.getTurmaVO().getPeridoLetivo().getCodigo())) {
				iterator.remove();
			}
		}
	}

	private List<PeriodoLetivoVO> consultarPeriodoLetivoPorGradecurricular(Integer codigo) throws Exception {
		return getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void consultarQuantidadeAlunoTurma() {
		try {
			getListaErroAtualizarAluno().clear();
			setListaAlunosTurma(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaPeriodoUnicaPorTurmaSituacao(getTurmaVO(), getTurmaVO().getTurmaDisciplinaVOs(), getListaErroAtualizarAluno(), false, getUsuarioLogado()));
			setQuantidadeAlunosTurma(getListaAlunosTurma().size());
			gravarLogTurma("atualizacaoDisciplinasAlunos");
			setMensagemQuantidadeAlunos("As novas disciplinas serão incluídas para " + getQuantidadeAlunosTurma()
					+ " alunos (Ativos). O choque de horário das disciplinas dessa turma, terão que ser tratadas separadamente.");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosNecessidadeMudancaCarteira() {
		setExisteContaReceberVinculadaOutraContaCorrente(
				getFacadeFactory().getContaReceberFacade().consultarExistenciaContaGeradaVinculadaOutraContaCorrente(
						getTurmaVO().getCodigo(), getTurmaVO().getContaCorrente().getCodigo(), getUsuarioLogado()));
	}

	public String getAbrirModalContaCorrenteNecessitaMudancaCarteira() {
		if (getExisteContaReceberVinculadaOutraContaCorrente()) {
			return "RichFaces.$('panelMensagemContaCorrente').show();";
		}
		return "";
	}

	public void inicializarDadosSetandoFalsoParaMensagemContaCorrente() {
		setExisteContaReceberVinculadaOutraContaCorrente(Boolean.FALSE);
	}

	public void inicializarDadosAtualizacaoDisciplinaAlunos() {
		try {
			setAtualizarDisciplinaAlunos(Boolean.TRUE);
			getFacadeFactory().getTurmaFacade().executarAtualizacaoDisciplinaAlunosTurma(getAtualizarDisciplinaAlunos(), getListaAlunosTurma(), getTurmaVO(),getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarBotaoAtualizarDisciplinaAlunos() {
		return !getTurmaVO().getNovoObj() && getTurmaVO().getCurso().getNivelEducacional().equals("PO");
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}

	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemPeriodoLetivo);
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public List<SelectItem> getListaSelectItemTurno() {
		return (listaSelectItemTurno);
	}

	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemGradeCurricular);
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public List<SelectItem> getListaSelectItemNovaGradeCurricular() {
		if (listaSelectItemNovaGradeCurricular == null) {
			listaSelectItemNovaGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNovaGradeCurricular;
	}

	public void setListaSelectItemNovaGradeCurricular(List<SelectItem> listaSelectItemNovaGradeCurricular) {
		this.listaSelectItemNovaGradeCurricular = listaSelectItemNovaGradeCurricular;
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

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public TurmaAgrupadaVO getTurmaAgrupadaVO() {
		if (turmaAgrupadaVO == null) {
			turmaAgrupadaVO = new TurmaAgrupadaVO();
		}
		return turmaAgrupadaVO;
	}

	public void setTurmaAgrupadaVO(TurmaAgrupadaVO turmaAgrupadaVO) {
		this.turmaAgrupadaVO = turmaAgrupadaVO;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		turmaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemPeriodoLetivo);
		Uteis.liberarListaMemoria(listaSelectItemGradeCurricular);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		Uteis.liberarListaMemoria(listaSelectItemTurno);
		Uteis.liberarListaMemoria(listaConsultaCurso);
		campoConsultaCurso = null;
		valorConsultaCurso = null;
		curso_Erro = null;
	}

	/**
	 * @return the listaSelectItemPlanoFinanceiroCurso
	 */
	public List<SelectItem> getListaSelectItemPlanoFinanceiroCurso() {
		return listaSelectItemPlanoFinanceiroCurso;
	}

	/**
	 * @param listaSelectItemPlanoFinanceiroCurso
	 *            the listaSelectItemPlanoFinanceiroCurso to set
	 */
	public void setListaSelectItemPlanoFinanceiroCurso(List<SelectItem> listaSelectItemPlanoFinanceiroCurso) {
		this.listaSelectItemPlanoFinanceiroCurso = listaSelectItemPlanoFinanceiroCurso;
	}

	public List<SelectItem> getListaSelectItemTipoChancela() throws Exception {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("PC", "Paga Chancela"));
		itens.add(new SelectItem("RC", "Receber Chancela"));
		return itens;
	}

	public boolean getIsUsaChancela() throws Exception {
		return getConfiguracaoFinanceiroPadraoSistema().getUsaChancela();
	}

	public void limparDadosPorcentagemEValor() throws Exception {
		getTurmaVO().setValorFixoChancela(Double.valueOf(0));
		getTurmaVO().setPorcentagemChancela(Double.valueOf(0));
		getTurmaVO().setValorPorAluno(Boolean.FALSE);
	}

	public boolean getIsApresentarDadosChancela() {
		return !getTurmaVO().getTipoChancela().equals("");
	}

	public Boolean getCampoValorPorAluno() {
		return campoValorPorAluno;
	}

	public void setCampoValorPorAluno(Boolean campoValorPorAluno) {
		this.campoValorPorAluno = campoValorPorAluno;
	}

	public List<SelectItem> getListaSelectItemGrupoDestinatarios() {
		if (listaSelectItemGrupoDestinatarios == null) {
			listaSelectItemGrupoDestinatarios = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGrupoDestinatarios;
	}

	public void setListaSelectItemGrupoDestinatarios(List<SelectItem> listaSelectItemGrupoDestinatarios) {
		this.listaSelectItemGrupoDestinatarios = listaSelectItemGrupoDestinatarios;
	}

	public List<SelectItem> getListaSelectItemChancela() {
		if (listaSelectItemChancela == null) {
			listaSelectItemChancela = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemChancela;
	}

	public void setListaSelectItemChancela(List<SelectItem> listaSelectItemChancela) {
		this.listaSelectItemChancela = listaSelectItemChancela;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContaCorrente;
	}

	public Boolean getApresentarAviso() {
		if (apresentarAviso == null) {
			apresentarAviso = Boolean.FALSE;
		}
		return apresentarAviso;
	}

	public void setApresentarAviso(Boolean apresentarAviso) {
		this.apresentarAviso = apresentarAviso;
	}

	public Integer getQuantidadeAlunosTurma() {
		if (quantidadeAlunosTurma == null) {
			quantidadeAlunosTurma = 0;
		}
		return quantidadeAlunosTurma;
	}

	public void setQuantidadeAlunosTurma(Integer quantidadeAlunosTurma) {
		this.quantidadeAlunosTurma = quantidadeAlunosTurma;
	}

	public List<MatriculaPeriodoVO> getListaAlunosTurma() {
		if (listaAlunosTurma == null) {
			listaAlunosTurma = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return listaAlunosTurma;
	}

	public void setListaAlunosTurma(List<MatriculaPeriodoVO> listaAlunosTurma) {
		this.listaAlunosTurma = listaAlunosTurma;
	}

	public String getMensagemQuantidadeAlunos() {
		if (mensagemQuantidadeAlunos == null) {
			mensagemQuantidadeAlunos = "";
		}
		return mensagemQuantidadeAlunos;
	}

	public void setMensagemQuantidadeAlunos(String mensagemQuantidadeAlunos) {
		this.mensagemQuantidadeAlunos = mensagemQuantidadeAlunos;
	}

	public Boolean getAtualizarDisciplinaAlunos() {
		if (atualizarDisciplinaAlunos == null) {
			atualizarDisciplinaAlunos = Boolean.FALSE;
		}
		return atualizarDisciplinaAlunos;
	}

	public void setAtualizarDisciplinaAlunos(Boolean atualizarDisciplinaAlunos) {
		this.atualizarDisciplinaAlunos = atualizarDisciplinaAlunos;
	}

	public void adicionarTurmaAbertura() throws Exception {
		try {
			if (getTurmaAberturaVO().getTurma().getCodigo().equals(0)) {
				getTurmaAberturaVO().setTurma(getTurmaVO());
			}
			if (getTurmaAberturaVO().getUsuario().getCodigo().equals(0)) {
				getTurmaAberturaVO().setUsuario(getUsuarioLogadoClone());
			}
			getFacadeFactory().getTurmaFacade().adicionarObjTurmaAberturaVOs(getTurmaVO(), getTurmaAberturaVO());
			this.setTurmaAberturaVO(new TurmaAberturaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerTurmaAbertura() throws Exception {
		TurmaAberturaVO turmaAberturaVO = (TurmaAberturaVO) context().getExternalContext().getRequestMap().get("turmaAberturaItens");
		getTurmaVO().getTurmaAberturaVOs().remove(turmaAberturaVO);
		setMensagemID("msg_dados_excluidos");
	}

	public TurmaAberturaVO getTurmaAberturaVO() {
		if (turmaAberturaVO == null) {
			turmaAberturaVO = new TurmaAberturaVO();
		}
		return turmaAberturaVO;
	}

	public void setTurmaAberturaVO(TurmaAberturaVO turmaAberturaVO) {
		this.turmaAberturaVO = turmaAberturaVO;
	}

	public String getMensagemAlteracaoContaCorrente() {
		if (mensagemAlteracaoContaCorrente == null) {
			mensagemAlteracaoContaCorrente = "";
		}
		return mensagemAlteracaoContaCorrente;
	}

	public void setMensagemAlteracaoContaCorrente(String mensagemAlteracaoContaCorrente) {
		this.mensagemAlteracaoContaCorrente = mensagemAlteracaoContaCorrente;
	}

	public Boolean getExisteContaReceberVinculadaOutraContaCorrente() {
		if (existeContaReceberVinculadaOutraContaCorrente == null) {
			existeContaReceberVinculadaOutraContaCorrente = Boolean.FALSE;
		}
		return existeContaReceberVinculadaOutraContaCorrente;
	}

	public void setExisteContaReceberVinculadaOutraContaCorrente(
			Boolean existeContaReceberVinculadaOutraContaCorrente) {
		this.existeContaReceberVinculadaOutraContaCorrente = existeContaReceberVinculadaOutraContaCorrente;
	}

	/**
	 * @return the apresentarDataBaseGeracaoParcela
	 */
	public Boolean getApresentarDataBaseGeracaoParcela() {
		if (apresentarDataBaseGeracaoParcela == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Turma_definirDataBaseGeracaoParcela", getUsuarioLogado());
				apresentarDataBaseGeracaoParcela = Boolean.TRUE;
			} catch (Exception e) {
				apresentarDataBaseGeracaoParcela = Boolean.FALSE;
			}
		}
		return apresentarDataBaseGeracaoParcela;
	}

	/**
	 * @param apresentarDataBaseGeracaoParcela
	 *            the apresentarDataBaseGeracaoParcela to set
	 */
	public void setApresentarDataBaseGeracaoParcela(Boolean apresentarDataBaseGeracaoParcela) {
		this.apresentarDataBaseGeracaoParcela = apresentarDataBaseGeracaoParcela;
	}

	public boolean isApresentarAlteracaoDataBaseGeracaoParcela() {
		return Uteis.isAtributoPreenchido(getListaMatriculaComControleGeracaoParcelaDataBase());
	}

	public List<MatriculaVO> getListaMatriculaComControleGeracaoParcelaDataBase() {
		listaMatriculaComControleGeracaoParcelaDataBase = Optional
				.ofNullable(listaMatriculaComControleGeracaoParcelaDataBase).orElse(new ArrayList<>());
		return listaMatriculaComControleGeracaoParcelaDataBase;
	}

	public void setListaMatriculaComControleGeracaoParcelaDataBase(
			List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase) {
		this.listaMatriculaComControleGeracaoParcelaDataBase = listaMatriculaComControleGeracaoParcelaDataBase;
	}

	public List<MatriculaVO> getListaMatriculaSemControleGeracaoParcelaDataBase() {
		listaMatriculaSemControleGeracaoParcelaDataBase = Optional
				.ofNullable(listaMatriculaSemControleGeracaoParcelaDataBase).orElse(new ArrayList<>());
		return listaMatriculaSemControleGeracaoParcelaDataBase;
	}

	public void setListaMatriculaSemControleGeracaoParcelaDataBase(
			List<MatriculaVO> listaMatriculaSemControleGeracaoParcelaDataBase) {
		this.listaMatriculaSemControleGeracaoParcelaDataBase = listaMatriculaSemControleGeracaoParcelaDataBase;
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

	public void montarListaDisciplinaComBaseTurmaPrincipal() throws Exception {
		getTurmaPrincipal().setCodigo(getTurmaVO().getTurmaPrincipal());
		getFacadeFactory().getTurmaFacade().carregarDados(getTurmaPrincipal(), NivelMontarDados.TODOS,
				getUsuarioLogado());
		if (getTurmaPrincipal().getTurmaAgrupada()) {
			executarVerificacaoTurmaDisciplinaAdicionar();
		} else {
			getFacadeFactory().getTurmaFacade().executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(
					getTurmaVO(), getTurmaPrincipal().getTurmaDisciplinaVOs(), false, getUsuarioLogado());
		}
		montarListaSelectItemModalidadeDisciplina();
		getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
	}

	public void preencherDadosUnidadeEnsinoTurmaPeriodicidadeDisciplinaTurma() throws Exception {
		getFacadeFactory().getTurmaFacade().carregarDados(getTurmaPrincipal(), NivelMontarDados.TODOS,
				getUsuarioLogado());
		if (getTurmaPrincipal().getSubturma()) {
			throw new Exception(
					"Não é possível cadastrar uma subturma de outra subturma, escolha outra turma principal.");
		}
		getTurmaVO().setTurmaPrincipal(getTurmaPrincipal().getCodigo());
		getTurmaVO().setTurmaAgrupada(getTurmaPrincipal().getTurmaAgrupada());
		getTurmaVO().setTurmaAgrupadaVOs(getTurmaPrincipal().getTurmaAgrupadaVOs());
		getTurmaVO().setAbreviaturaCurso(getTurmaPrincipal().getAbreviaturaCurso());
		getTurmaVO().setUnidadeEnsino(getTurmaPrincipal().getUnidadeEnsino());
		getTurmaVO().setTurno(getTurmaPrincipal().getTurno());
		getTurmaVO().setAbreviaturaCurso(getTurmaPrincipal().getAbreviaturaCurso());
		getTurmaVO().setAnual(getTurmaPrincipal().getAnual());
		getTurmaVO().setSemestral(getTurmaPrincipal().getSemestral());
		getTurmaVO().setGradeCurricularVO(getTurmaPrincipal().getGradeCurricularVO());
		getTurmaVO().setPeridoLetivo(getTurmaPrincipal().getPeridoLetivo());
		getTurmaVO().setCurso(getTurmaPrincipal().getCurso());
		montarListaSelectItemConfiguracaoAcademico();
		montarListaSelectItemPeriodoLetivo();
		getTurmaVO().setTurmaDisciplinaVOs(getTurmaPrincipal().getTurmaDisciplinaVOs());
		montarListaSelectItemTipoSubturma();
		montarListaSelectItemModalidadeDisciplina();
		for (TurmaDisciplinaVO turmaDisciplina : getTurmaVO().getTurmaDisciplinaVOs()) {
			turmaDisciplina.setCodigo(0);
			turmaDisciplina.setNovoObj(true);
		}
		if (getTurmaPrincipal().getTurmaAgrupada()) {
			getTurmaVO().setTurmaAgrupadaVOs(getTurmaPrincipal().getTurmaAgrupadaVOs());
			for (TurmaAgrupadaVO turmaAgrupadaVO : getTurmaVO().getTurmaAgrupadaVOs()) {
				turmaAgrupadaVO.setCodigo(0);
				turmaAgrupadaVO.setNovoObj(true);
			}
		}
		executarDefinicaoPeriodicidadeTurma();
		getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();

	}

	public boolean getMatrizCurricularUtilizaGrupoDisciplinaOptativas() {
		if (this.getTurmaVO().getPeridoLetivo().getControleOptativaGrupo().booleanValue()) {
			if (!this.getTurmaVO().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo().equals(0)) {
				return true;
			}
		}
		return false;
	}

	public Boolean getIsApresentarGradeDisciplinaParteDiversificada() {
		return getTurmaVO().getCurso().getUtilizaDisciplinaParteDiversificada();
	}

	public void fecharPainelDisciplinaGrupoOptativaTurma() throws Exception {
		setMensagemID("msg_lembreteGravarDadosConfirmarAlteracao");
	}

	/**
	 * 
	 */
	public void adicionarDisciplinaGrupoOptativaTurma() throws Exception {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			getFacadeFactory().getTurmaFacade().executarGeracaoTurmaDisciplinaGradeCurricularGrupoOptativaDisciplina(getTurmaVO(), obj, getUsuarioLogado());
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().executarLogAtualizacaoDisciplinaTurma(getTurmaVO(), getTurmaVO().getTurmaDisciplinaVOs(), getUsuarioLogado());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			if (config.getResponsavelPadraoComunicadoInterno() != null && !config.getResponsavelPadraoComunicadoInterno().getCodigo().equals(0)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAtualizacaoDisciplinaTurmaRealizado(getTurmaVO(), getUsuarioLogado());
			}
			getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			gravarLogTurma("atualizacaoListaDisciplinas");
			setMensagemID("msg_Turma_disciplinaOptativaAdicionadaComSucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararAdicionarDisciplinaGrupoOptativa() throws Exception {
		setMensagemID("msg_Turma_selecioneDisciplinaOptativaDesejada");
	}

	/**
	 * @return the mensagemComplementarApresentarAviso
	 */
	public String getMensagemComplementarApresentarAviso() {
		if (mensagemComplementarApresentarAviso == null) {
			mensagemComplementarApresentarAviso = "";
		}
		return mensagemComplementarApresentarAviso;
	}

	/**
	 * @param mensagemComplementarApresentarAviso
	 *            the mensagemComplementarApresentarAviso to set
	 */
	public void setMensagemComplementarApresentarAviso(String mensagemComplementarApresentarAviso) {
		this.mensagemComplementarApresentarAviso = mensagemComplementarApresentarAviso;
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaPrincipal().getIdentificadorTurma().equals("")) {
				setTurmaPrincipal(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(
						getTurmaPrincipal().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
				preencherDadosUnidadeEnsinoTurmaPeriodicidadeDisciplinaTurma();
			} else {
				throw new Exception("Informe a Turma.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			limparDadosTurmaPrincipal();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurmaPrincipal() {
		try {
			setTurmaPrincipal((TurmaVO) context().getExternalContext().getRequestMap().get("turmaPrincipalItens"));
			setTurmaPrincipal(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(
					getTurmaPrincipal().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
			preencherDadosUnidadeEnsinoTurmaPeriodicidadeDisciplinaTurma();
		} catch (Exception e) {
			limparDadosTurmaPrincipal();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurmaPrincipal() {
		setTurmaPrincipal(null);
		getTurmaVO().getTurmaAgrupadaVOs().clear();
		getTurmaVO().getTurmaDisciplinaVOs().clear();
		getTurmaVO().setTurno(null);
		getTurmaVO().setGradeCurricularVO(null);
		getTurmaVO().setUnidadeEnsino(null);
		getTurmaVO().setPeridoLetivo(null);
		getTurmaVO().setTurmaAgrupada(false);
		getListaConsultaTurma().clear();
	}

	public void limparCentroResultado() {
		try {
			getTurmaVO().setCentroResultadoVO(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap()
					.get("centroResultadoItens");
			getTurmaVO().setCentroResultadoVO(obj);
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA,
					getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, false, null, null, null,
					getControleConsultaOtimizado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public TurmaVO getTurmaPrincipal() {
		if (turmaPrincipal == null) {
			turmaPrincipal = new TurmaVO();
		}
		return turmaPrincipal;
	}

	public void setTurmaPrincipal(TurmaVO turmaPrincipal) {
		this.turmaPrincipal = turmaPrincipal;
	}

	public List<TurmaDisciplinaVO> getTurmaDisciplinaSugeridaVOs() {
		if (turmaDisciplinaSugeridaVOs == null) {
			turmaDisciplinaSugeridaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		}
		return turmaDisciplinaSugeridaVOs;
	}

	public void setTurmaDisciplinaSugeridaVOs(List<TurmaDisciplinaVO> turmaDisciplinaSugeridaVOs) {
		this.turmaDisciplinaSugeridaVOs = turmaDisciplinaSugeridaVOs;
	}

	public TurmaVO getTurmaInclusaoSugeridaVO() {
		if (turmaInclusaoSugeridaVO == null) {
			turmaInclusaoSugeridaVO = new TurmaVO();
		}
		return turmaInclusaoSugeridaVO;
	}

	public void setTurmaInclusaoSugeridaVO(TurmaVO turmaInclusaoSugeridaVO) {
		this.turmaInclusaoSugeridaVO = turmaInclusaoSugeridaVO;
	}

	public Boolean getApresentarAtualizarDiscAluno() {
		if (this.getTurmaVO().getCurso().getNivelEducacional().equals("PO")
				|| this.getTurmaVO().getCurso().getNivelEducacional().equals("PR")
				|| this.getTurmaVO().getCurso().getNivelEducacional().equals("EX")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author Victor Hugo 22/10/2014
	 */
	private List<SelectItem> listaSelectItemConfiguracaoEAD;
	private List<SelectItem> listaSelectItemAvaliacaoOnline;
	private List<SelectItem> listaSelectItemConfiguracaoNotaFiscal;

	public List<SelectItem> getListaSelectItemConfiguracaoEAD() {
		if (listaSelectItemConfiguracaoEAD == null) {
			listaSelectItemConfiguracaoEAD = new ArrayList<SelectItem>();
		}
		return listaSelectItemConfiguracaoEAD;
	}

	public void setListaSelectItemConfiguracaoEAD(List<SelectItem> listaSelectItemConfiguracaoEAD) {
		this.listaSelectItemConfiguracaoEAD = listaSelectItemConfiguracaoEAD;
	}

	public boolean getIsVerificarExistenciaDisciplinaOnlineOuAmbas() {
		int cont = 0;
		for (TurmaDisciplinaVO object : getTurmaVO().getTurmaDisciplinaVOs()) {
			if (object.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.AMBAS)
					|| object.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				cont++;
				break;
			}
		}
		if (cont > 0 || getTurmaVO().getCurso().getConfiguracaoAcademico()
				.getUtilizarApoioEADParaDisciplinasModalidadePresencial()) {
			montarComboBoxConfiguracaoEAD();
			montarComboBoxAvaliacaoOnline();
			return true;
		} else if (getTurmaVO().getTurmaDisciplinaVOs().isEmpty()) {
			return false;
		} else {
			return false;
		}
	}

	public void montarComboBoxConfiguracaoEAD() {
		try {
			List<ConfiguracaoEADVO> resultado = getFacadeFactory().getConfiguracaoEADFacade()
					.consultarConfiguracoesEADAtivasTurma(getTurmaVO().getConfiguracaoEADVO().getCodigo());
			setListaSelectItemConfiguracaoEAD(
					UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
		} catch (Exception e) {
			setListaSelectItemConfiguracaoEAD(new ArrayList<SelectItem>(0));
		}
	}

	public void montarComboBoxAvaliacaoOnline() {
		try {
			List<AvaliacaoOnlineVO> resultado = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade()
					.consultarAvaliacoesOnlinesPorCodigoDisciplinaETipoUsoGeral(null, getUsuarioLogado());
			setListaSelectItemAvaliacaoOnline(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemConfiguracaoEAD(new ArrayList<SelectItem>(0));
		}
	}

	public List<SelectItem> getListaSelectItemAvaliacaoOnline() {
		if (listaSelectItemAvaliacaoOnline == null) {
			listaSelectItemAvaliacaoOnline = new ArrayList<SelectItem>();
		}
		return listaSelectItemAvaliacaoOnline;
	}

	public void setListaSelectItemAvaliacaoOnline(List<SelectItem> listaSelectItemAvaliacaoOnline) {
		this.listaSelectItemAvaliacaoOnline = listaSelectItemAvaliacaoOnline;
	}

	/**
	 * @author Victor Hugo 10/11/2014
	 */

	public List<SelectItem> getListaSelectItemDefinicoesTutoriaOnline() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(DefinicoesTutoriaOnlineEnum.class, "name",
				"valorApresentar", false);
	}

	public boolean getIsApresentarPeriodicidade() {
		return !getTurmaVO().getTurmaAgrupada() || getApresentarAviso() || getTurmaVO().getSubturma();
	}

	public List<SelectItem> getListaSelectItemConfiguracaoNotaFiscal() {
		if (listaSelectItemConfiguracaoNotaFiscal == null) {
			listaSelectItemConfiguracaoNotaFiscal = new ArrayList<SelectItem>();
		}
		return listaSelectItemConfiguracaoNotaFiscal;
	}

	public void setListaSelectItemConfiguracaoNotaFiscal(List<SelectItem> listaSelectItemConfiguracaoNotaFiscal) {
		this.listaSelectItemConfiguracaoNotaFiscal = listaSelectItemConfiguracaoNotaFiscal;
	}

	public void montarComboBoxConfiguracaoNotaFiscal() {
		try {
			List<ConfiguracaoNotaFiscalVO> resultado = getFacadeFactory().getConfiguracaoNotaFiscalFacade()
					.consultarConfiguracaoNotaFiscal(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemConfiguracaoNotaFiscal(
					UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemConfiguracaoEAD(new ArrayList<SelectItem>(0));
		}
	}

	/**
	 * @author Victor Hugo 12/02/2015
	 */
	public void montarListaSelectItemModalidadeDisciplina() {
		try {
			getFacadeFactory().getTurmaDisciplinaFacade().montarDadosListaSelectItemModalidade(getTurmaVO().getTurmaDisciplinaVOs());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void executarVerificacaoTurmaDisciplinaAdicionar() throws Exception {
		TurmaAgrupadaVO agrupada = getTurmaPrincipal().getTurmaAgrupadaVOs().get(0);
		List<TurmaDisciplinaVO> disciplinasVerificar = getFacadeFactory().getTurmaDisciplinaFacade()
				.consultarPorCodigoTurma(agrupada.getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS,
						getUsuarioLogado());
		for (Iterator<TurmaDisciplinaVO> iterator = disciplinasVerificar.iterator(); iterator.hasNext();) {
			TurmaDisciplinaVO disciplinaVerificar = (TurmaDisciplinaVO) iterator.next();
			disciplinaVerificar.setCodigo(0);
			disciplinaVerificar.setNovoObj(true);
			disciplinaVerificar
					.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(
							disciplinaVerificar.getGradeDisciplinaVO().getCodigo(), getUsuarioLogado()));
			boolean disciplinaComum = false;
			for (TurmaAgrupadaVO turmaAgrupadaVO : getTurmaPrincipal().getTurmaAgrupadaVOs()) {
				List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade()
						.consultarPorCodigoTurma(turmaAgrupadaVO.getTurma().getCodigo(), false,
								Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				disciplinaComum = false;
				for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
					turmaDisciplinaVO.setGradeDisciplinaVO(
							getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(
									turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo(), getUsuarioLogado()));
					if (turmaDisciplinaVO.getDisciplina().getCodigo()
							.equals(disciplinaVerificar.getDisciplina().getCodigo())
							&& turmaDisciplinaVO.getGradeDisciplinaVO().getCargaHoraria()
									.equals(disciplinaVerificar.getGradeDisciplinaVO().getCargaHoraria())) {
						disciplinaComum = true;
						break;
					}
				}
			}
			if (!disciplinaComum
					&& !getFacadeFactory().getTurmaFacade().executarVerificacaoDisciplinaCursandoEProgramacaoAula(
							disciplinaVerificar, getTurmaVO(), getUsuarioLogado())) {
				iterator.remove();
			}
		}
		getFacadeFactory().getTurmaFacade().executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(getTurmaVO(),
				disciplinasVerificar, false, getUsuarioLogado());
	}

	public String getPeriodicidadeTurma() {
		if (periodicidadeTurma == null) {
			periodicidadeTurma = "";
		}
		return periodicidadeTurma;
	}

	public void setPeriodicidadeTurma(String periodicidadeTurma) {
		this.periodicidadeTurma = periodicidadeTurma;
	}

	public void executarGeracaoTurmaDisciplinaComposta() {
		try {
			setTurmaDisciplinaVO((TurmaDisciplinaVO) getRequestMap().get("turmaDisciplinaItens"));
			List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
			if (Uteis.isAtributoPreenchido(getTurmaDisciplinaVO().getGradeCurricularGrupoOptativaDisciplinaVO())) {
				gradeDisciplinaCompostaVOs = getFacadeFactory().getGradeDisciplinaCompostaFacade()
						.consultarPorGrupoOptativaDisciplina(
								getTurmaDisciplinaVO().getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(),
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else {
				gradeDisciplinaCompostaVOs = getFacadeFactory().getGradeDisciplinaCompostaFacade()
						.consultarPorGradeDisciplina(getTurmaDisciplinaVO().getGradeDisciplinaVO().getCodigo(),
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			boolean encontrado = false;
			for (GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO : gradeDisciplinaCompostaVOs) {
				for (TurmaDisciplinaCompostaVO obj : getTurmaDisciplinaVO().getTurmaDisciplinaCompostaVOs()) {
					if (obj.getGradeDisciplinaCompostaVO().getCodigo().equals(gradeDisciplinaCompostaVO.getCodigo())) {
						if (Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeDisciplina())) {
							obj.getGradeDisciplinaCompostaVO().setGradeDisciplina(
									getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimaria(
											gradeDisciplinaCompostaVO.getGradeDisciplina().getCodigo(),
											getUsuarioLogado()));
						} else {
							obj.getGradeDisciplinaCompostaVO()
									.setGradeCurricularGrupoOptativaDisciplina(
											getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade()
													.consultarPorChavePrimaria(gradeDisciplinaCompostaVO
															.getGradeCurricularGrupoOptativaDisciplina().getCodigo(),
															getUsuarioLogado()));
						}
						obj.setEditavel(!getFacadeFactory().getTurmaFacade()
								.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(
										getTurmaVO().getCodigo(),
										obj.getGradeDisciplinaCompostaVO().getDisciplina().getCodigo(), false,
										getUsuarioLogado()));
						if (!obj.isSelecionado()) {
							obj.setEditavel(true);
						}
						encontrado = true;
						break;
					}
				}
				if (!encontrado) {
					if (Uteis.isAtributoPreenchido(gradeDisciplinaCompostaVO.getGradeDisciplina())) {
						gradeDisciplinaCompostaVO.setGradeDisciplina(getFacadeFactory().getGradeDisciplinaFacade()
								.consultarPorChavePrimaria(gradeDisciplinaCompostaVO.getGradeDisciplina().getCodigo(),
										getUsuarioLogado()));
					} else {
						gradeDisciplinaCompostaVO.setGradeCurricularGrupoOptativaDisciplina(
								getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade()
										.consultarPorChavePrimaria(gradeDisciplinaCompostaVO
												.getGradeCurricularGrupoOptativaDisciplina().getCodigo(),
												getUsuarioLogado()));
					}
					TurmaDisciplinaCompostaVO obj = new TurmaDisciplinaCompostaVO();
					obj.setGradeDisciplinaCompostaVO(gradeDisciplinaCompostaVO);
					obj.setTurmaDisciplinaVO(getTurmaDisciplinaVO());
					obj.setEditavel(!getFacadeFactory().getTurmaFacade()
							.consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(
									getTurmaVO().getCodigo(),
									obj.getGradeDisciplinaCompostaVO().getDisciplina().getCodigo(), false,
									getUsuarioLogado()));
					if (!obj.isSelecionado()) {
						obj.setEditavel(true);
					}
					if (!getTurmaVO().getSubturma() && gradeDisciplinaCompostaVO.getTipoControleComposicao()
							.equals(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS)) {
						obj.setSelecionado(true);
					}
					getTurmaDisciplinaVO().getTurmaDisciplinaCompostaVOs().add(obj);
				}
				encontrado = false;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public TurmaDisciplinaVO getTurmaDisciplinaVO() {
		if (turmaDisciplinaVO == null) {
			turmaDisciplinaVO = new TurmaDisciplinaVO();
		}
		return turmaDisciplinaVO;
	}

	public void setTurmaDisciplinaVO(TurmaDisciplinaVO turmaDisciplinaVO) {
		this.turmaDisciplinaVO = turmaDisciplinaVO;
	}

	private void executarDefinicaoPeriodicidadeTurma() throws Exception {
		if (getTurmaVO().getSubturma()) {
			setPeriodicidadeTurma(getTurmaVO().getAnual() ? "AN" : getTurmaVO().getSemestral() ? "SE" : "IN");
		} else if (getTurmaVO().getTurmaAgrupada()) {
			for (TurmaAgrupadaVO taVO : getTurmaVO().getTurmaAgrupadaVOs()) {
				setPeriodicidadeTurma(taVO.getTurma().getCurso().getPeriodicidade());
				break;
			}
		} else {
			setPeriodicidadeTurma(getTurmaVO().getCurso().getPeriodicidade());
		}
	}

	public void montarListaSelectItemTipoSubturma() {
		getListaSelectItemTipoSubturma().clear();
		getListaSelectItemTipoSubturma()
				.add(new SelectItem(TipoSubTurmaEnum.TEORICA.getName(), TipoSubTurmaEnum.TEORICA.getValorApresentar()));
		getListaSelectItemTipoSubturma()
				.add(new SelectItem(TipoSubTurmaEnum.PRATICA.getName(), TipoSubTurmaEnum.PRATICA.getValorApresentar()));
		if (!getTurmaPrincipal().getTurmaAgrupada()) {
			getListaSelectItemTipoSubturma()
					.add(new SelectItem(TipoSubTurmaEnum.GERAL.getName(), TipoSubTurmaEnum.GERAL.getValorApresentar()));
		}
	}

	/**
	 * @return the listaSelectItemTipoSubturma
	 */
	public List<SelectItem> getListaSelectItemTipoSubturma() {
		if (listaSelectItemTipoSubturma == null) {
			listaSelectItemTipoSubturma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoSubturma;
	}

	/**
	 * @param listaSelectItemTipoSubturma
	 *            the listaSelectItemTipoSubturma to set
	 */
	public void setListaSelectItemTipoSubturma(List<SelectItem> listaSelectItemTipoSubturma) {
		this.listaSelectItemTipoSubturma = listaSelectItemTipoSubturma;
	}

	/**
	 * @return the turmaAgrupadaVOs
	 */
	public List<TurmaVO> getTurmaAgrupadaVOs() {
		if (turmaAgrupadaVOs == null) {
			turmaAgrupadaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaAgrupadaVOs;
	}

	/**
	 * @param turmaAgrupadaVOs
	 *            the turmaAgrupadaVOs to set
	 */
	public void setTurmaAgrupadaVOs(List<TurmaVO> turmaAgrupadaVOs) {
		this.turmaAgrupadaVOs = turmaAgrupadaVOs;
	}

	/**
	 * @return the subturmaVOs
	 */
	public List<TurmaVO> getSubturmaVOs() {
		if (subturmaVOs == null) {
			subturmaVOs = new ArrayList<TurmaVO>(0);
		}
		return subturmaVOs;
	}

	/**
	 * @param subturmaVOs
	 *            the subturmaVOs to set
	 */
	public void setSubturmaVOs(List<TurmaVO> subturmaVOs) {
		this.subturmaVOs = subturmaVOs;
	}

	public void verificarNecessidadeAtualizarDisciplinaAlteracaoGradeCurricular() {
		if (!getTurmaVO().isNovoObj()
				&& !getTurmaVO().getGradeCurricularAtiva().equals(getTurmaVO().getGradeCurricularVO().getCodigo())
				&& getTurmaVO().getSubturma() == false && getTurmaVO().getTurmaAgrupada() == false) {
			if (!getTurmaVO().getDisciplinasAtualizadasAlteracaoMatrizCurricular()) {
				atualizarDisciplinasTurmaComBaseGradeCurricular();
			}
		}
	}

	public Boolean getPermiteAlterarDadosEAD() {
		if (permiteAlterarDadosEAD == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Turma_permiteAlterarDadosEAD", getUsuarioLogado());
				permiteAlterarDadosEAD = true;
			} catch (Exception e) {
				permiteAlterarDadosEAD = false;
			}

		}
		return permiteAlterarDadosEAD;
	}

	public void setPermiteAlterarDadosEAD(Boolean permiteAlterarDadosEAD) {
		this.permiteAlterarDadosEAD = permiteAlterarDadosEAD;
	}

	public void consultarAlunoComModalidadeDiferenteTurmaDisciplina() {
		try {
			getTurmaDisciplinaEstatisticaAlunoVO().setTipoEstatisticaTurmaDisciplinaEnum(TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE);
			setTurmaDisciplinaVO((TurmaDisciplinaVO) getRequestMap().get("turmaDisciplinaItens"));
			setDisciplinaVO(getTurmaDisciplinaVO().getDisciplina());
			setTurmaDisciplinaEstatisticaAlunoVOs(getFacadeFactory().getTurmaDisciplinaFacade()
					.consultarPorAlunoComModalidadeDiferenteTurma(getTurmaVO(), getTurmaDisciplinaVO(), getUsuario()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarListaAlunoComModalidadeDiferenteTurmaDisciplina() {
		try {
			setTurmaDisciplinaEstatisticaAlunoVO(
					(TurmaDisciplinaEstatisticaAlunoVO) getRequestMap().get("turmaDisciplinaEstatisticaItens"));
			if (getTurmaDisciplinaEstatisticaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVOs().isEmpty()) {
				getTurmaDisciplinaEstatisticaAlunoVO().setMatriculaPeriodoTurmaDisciplinaVOs(
						getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade()
								.consultarPorMatriculaPeriodoTurmaDisciplinaComModalidadeDiferenteTurma(getTurmaVO(),
										getTurmaDisciplinaEstatisticaAlunoVO(), getUsuarioLogado()));
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarAlunoComModalidadeDiferenteTurmaDisciplina() {
		try {
			if (getTurmaDisciplinaEstatisticaAlunoVO().getTipoEstatisticaTurmaDisciplinaEnum()
					.equals(TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE)) {
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade()
						.alterarModalidadeMatriculaPeriodoTurmaDisciplina(getTurmaVO(),
								getTurmaDisciplinaEstatisticaAlunoVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getHistoricoFacade()
						.alterarConfiguracaoAcademicaHistoricoConformeTurmaDisciplinaEstatisticaAlunoVO(getTurmaVO(),
								getTurmaDisciplinaEstatisticaAlunoVO(), getUsuarioLogado());
			}
			getTurmaDisciplinaEstatisticaAlunoVOs().remove(getTurmaDisciplinaEstatisticaAlunoVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<TurmaDisciplinaEstatisticaAlunoVO> getTurmaDisciplinaEstatisticaAlunoVOs() {
		if (turmaDisciplinaEstatisticaAlunoVOs == null) {
			turmaDisciplinaEstatisticaAlunoVOs = new ArrayList<TurmaDisciplinaEstatisticaAlunoVO>(0);
		}
		return turmaDisciplinaEstatisticaAlunoVOs;
	}

	public void setTurmaDisciplinaEstatisticaAlunoVOs(
			List<TurmaDisciplinaEstatisticaAlunoVO> turmaDisciplinaEstatisticaAlunoVOs) {
		this.turmaDisciplinaEstatisticaAlunoVOs = turmaDisciplinaEstatisticaAlunoVOs;
	}

	public TurmaDisciplinaEstatisticaAlunoVO getTurmaDisciplinaEstatisticaAlunoVO() {
		if (turmaDisciplinaEstatisticaAlunoVO == null) {
			turmaDisciplinaEstatisticaAlunoVO = new TurmaDisciplinaEstatisticaAlunoVO(
					TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE);
		}
		return turmaDisciplinaEstatisticaAlunoVO;
	}

	public void setTurmaDisciplinaEstatisticaAlunoVO(TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO) {
		this.turmaDisciplinaEstatisticaAlunoVO = turmaDisciplinaEstatisticaAlunoVO;
	}

	public List<String> getListaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso() {
		if (listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso == null) {
			listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso = new ArrayList<>();
		}
		return listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso;
	}

	public void setListaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso(
			List<String> listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso) {
		this.listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso = listaSelectItemCategoriasDasCondicoesDePagamentoDoPlanoFinanceiroCurso;
	}

	public void consultarAlunoComConfiguracaoAcademicaDiferenteTurmaDisciplina() {
		try {
			getTurmaDisciplinaEstatisticaAlunoVO().setTipoEstatisticaTurmaDisciplinaEnum(TipoEstatisticaTurmaDisciplinaEnum.CONFIGURACAO_ACADEMICA);
			setTurmaDisciplinaVO((TurmaDisciplinaVO) getRequestMap().get("turmaDisciplinaItens"));
			setDisciplinaVO(getTurmaDisciplinaVO().getDisciplina());
			setTurmaDisciplinaEstatisticaAlunoVOs(getFacadeFactory().getTurmaDisciplinaFacade()
					.consultarPorAlunoComConfiguracaoAcademicaDiferenteTurma(getTurmaVO(), getTurmaDisciplinaVO(), null,
							getTurmaDisciplinaVO().getConfiguracaoAcademicoVO(), getTurmaDisciplinaVO().getDisciplina(),
							getUsuario()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Valida regras de negocio e se o plano financeiro foi alterado
	 * 
	 * @return true se atende as RGN <br>
	 *         false nao atende
	 */
	public Boolean getValidarAlteracaoPlanoFinanceiroDosAlunosDaTurma() {

		// RGN
		// if (!getPermiteAlterarPlanoFinanceiro() && (getTurmaVO().isNovoObj() ||
		// !getTurmaVO().getIntegral())) {
		// return false;
		// }

		if (!getPermiteAlterarPlanoFinanceiro() || getTurmaVO().getSubturma() || getTurmaVO().getTurmaAgrupada()) {
			return false;
		}

		return true;
	}

	public Boolean getPermiteAlterarPlanoFinanceiro() {
		if (permiteAlterarPlanoFinanceiro == null) {
			permiteAlterarPlanoFinanceiro = false;
		}
		return permiteAlterarPlanoFinanceiro;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosDaTurmaComParcelasGeradas(Integer turma,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
		setListaDeAlunosComParcelaGerada(getFacadeFactory().getAlteracaoPlanoFinanceiroAlunoTurmaFacade()
				.consultarAlunosDaTurmaComParcelasGeradas(turma, nivelMontarDados, usuario));
		return getListaDeAlunosComParcelaGerada();
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoNaoEncontrado(Integer turma,
			Integer codigoNovoPlanoFinanceiroCurso, String categoria, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		setListaDeAlunosComCondicaoPagamentoNaoEncontrada(getFacadeFactory()
				.getAlteracaoPlanoFinanceiroAlunoTurmaFacade().consultarAlunosComCondicaoPagamentoNaoEncontrado(turma,
						codigoNovoPlanoFinanceiroCurso, categoria, nivelMontarDados, usuario));
		return getListaDeAlunosComCondicaoPagamentoNaoEncontrada();
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> recuperarAlunosQueSeraoAlterados(Integer turma,
			Integer codigoNovoPlanoFinanceiroCurso, String categoria, int nivelMontarDados) throws Exception {
		// Realiza a consulta das matriculas do alunos que terao sua condicao de
		// pagamento alterada
		List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosQueSeraoAlterados = getFacadeFactory()
				.getAlteracaoPlanoFinanceiroAlunoTurmaFacade().consultarAlunosQueSeraoAlterados(turma,
						codigoNovoPlanoFinanceiroCurso, categoria, nivelMontarDados, getUsuarioLogado());

		// Faz a classificacao dos alunos que tem mais de uma opcao para alterar sua
		// condicao de pagamento
		classificarListaDeAlunosDaTurmaQueSeraoAlterados(listaDeAlunosQueSeraoAlterados);
		return getListaDeAlunosComUmaCondicaoPagamento();
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComValorInferiorAoNovoPlano(Integer turma,
			Integer codigoNovoPlanoFinanceiroCurso, String categoria, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		setListaDeAlunosComValorInferiorAoNovoPlano(getFacadeFactory().getAlteracaoPlanoFinanceiroAlunoTurmaFacade()
				.consultarAlunosComValorInferiorAoNovoPlano(turma, codigoNovoPlanoFinanceiroCurso, categoria,
						nivelMontarDados, usuario));
		return getListaDeAlunosComValorInferiorAoNovoPlano();
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoEmConformidade(Integer turma,
			Integer codigoNovoPlanoFinanceiroCurso, String categoria, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		setListaDeAlunosComCondicaoPagamentoEmConformidade(getFacadeFactory()
				.getAlteracaoPlanoFinanceiroAlunoTurmaFacade().consultarAlunosComCondicaoPagamentoEmConformidade(turma,
						codigoNovoPlanoFinanceiroCurso, categoria, nivelMontarDados, usuario));
		return getListaDeAlunosComCondicaoPagamentoEmConformidade();
	}

	/**
	 * Classifica a lista de todos os alunos que serao alterados Alguns alunos possuem mais de uma condicao de pagamento compativel para alteracao
	 * 
	 * Os alunos que contem somente 1 condicao de pagamento compativel ja estao aptos para alteracao e sao incluidos na lista listaDeAlunosComUmaCondicaoPagamento Os alunos que contem mais de uma condicao de pagamento compativel precisam que o usuario informe a condicao de pagamento mais adequada e sao incluidos na lista listaDeAlunosComMaisDeUmaCondicaoPagamento
	 * 
	 * @param turma
	 * @param codigoNovoPlanoFinanceiroCurso
	 * @param categoria
	 * @throws Exception
	 */
	public void classificarListaDeAlunosDaTurmaQueSeraoAlterados(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosQueSeraoAlterados) throws Exception {

		// Inicializa listas de apoio
		List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComUmaCondicaoPagamento = new ArrayList<>();
		List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComMaisDeUmaCondicaoPagamento = new ArrayList<>();
		List<CondicaoPagamentoPlanoFinanceiroCursoVO> listaDeCondicaoPagamento = new ArrayList<>();

		// Verifica se a consulta trouxe dados
		if (listaDeAlunosQueSeraoAlterados != null && !listaDeAlunosQueSeraoAlterados.isEmpty()) {

			// Percorre todas os alunos que terao a condicao de pagamento alterado
			for (AlteracaoPlanoFinanceiroAlunoTurmaVO aluno : listaDeAlunosQueSeraoAlterados) {

				// Busca as possiveis condicoes de pgto
				String[] condicoesPgto = aluno.getOpcoes().split("&;&");

				// Converte a lista para objetos (CondicaoPagamentoPlanoFinanceiroCursoVO)
				JAXBContext jaxbContext = JAXBContext.newInstance(CondicaoPagamentoPlanoFinanceiroCursoVO.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				StringReader reader = new StringReader(condicoesPgto[0]);
				CondicaoPagamentoPlanoFinanceiroCursoVO condicao = (CondicaoPagamentoPlanoFinanceiroCursoVO) unmarshaller
						.unmarshal(reader);

				condicao.setDescricao(montarStringParaExibirInformacoesCompletasDaCondicaoDePagamento(condicao));

				listaDeCondicaoPagamento.add(condicao);
				aluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setCodigo(condicao.getCodigo());
				aluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().setDescricao(condicao.getDescricao());

				if (condicoesPgto.length <= 1) {
					listaDeAlunosComUmaCondicaoPagamento.add(aluno);
					aluno.setListaNovaCondicaoPgto(
							UtilSelectItem.getListaSelectItem(listaDeCondicaoPagamento, "codigo", "descricao", false));
					continue;
				} else {

					// Busca demais condicoes de pagamento aptas para o aluno
					int cont = 1;
					while (condicoesPgto.length > cont) {

						reader = new StringReader(condicoesPgto[cont]);
						condicao = (CondicaoPagamentoPlanoFinanceiroCursoVO) unmarshaller.unmarshal(reader);

						condicao.setDescricao(
								montarStringParaExibirInformacoesCompletasDaCondicaoDePagamento(condicao));

						listaDeCondicaoPagamento.add(condicao);
						cont++;
					}
					aluno.setListaNovaCondicaoPgto(
							UtilSelectItem.getListaSelectItem(listaDeCondicaoPagamento, "codigo", "descricao"));
					listaDeAlunosComMaisDeUmaCondicaoPagamento.add(aluno);
				}

			}
		}
		setListaDeAlunosComUmaCondicaoPagamento(listaDeAlunosComUmaCondicaoPagamento);
		setListaDeAlunosComCondicaoPagamentoDuplicidade(listaDeAlunosComMaisDeUmaCondicaoPagamento);
	}

	private String montarStringParaExibirInformacoesCompletasDaCondicaoDePagamento(
			CondicaoPagamentoPlanoFinanceiroCursoVO condicao) {

		StringBuilder descricaoDetalhadaCondicaoPgto = new StringBuilder(condicao.getDescricao()).append(" - ")
				.append(condicao.getCategoria()).append(" - ").append(condicao.getNrParcelasPeriodo()).append(" x ")
				.append(Uteis.formatarDoubleParaMoeda(condicao.getValorParcela()));

		return descricaoDetalhadaCondicaoPgto.toString();
	}

	/**
	 * Chama a rotina para alteracao as condicoes de pagamento dos alunos matriculados na turma
	 * 
	 * @return
	 */
	public void alterarPlanoFinanceiroDosAlunosDaTurma() {

		if (!getPermiteAlterarPlanoFinanceiro() || !getValidarAlteracaoPlanoFinanceiroDosAlunosDaTurma()) {
			return;
		}

		setTelaPrincipalAlteracaoPlanoFinanceiro(true);
		limparListasComAsSituacoesDoPlanoFinanceiro();

		try {

			Integer codigoPlanoFinanceiroCurso = getTurmaVO().getPlanoFinanceiroCurso().getCodigo();
			String categoria = getTurmaVO().getCategoriaCondicaoPagamento().trim() == "" ? ""
					: getTurmaVO().getCategoriaCondicaoPagamento();

			consultarAlunosDaTurmaComParcelasGeradas(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			consultarAlunosComCondicaoPagamentoNaoEncontrado(getTurmaVO().getCodigo(), codigoPlanoFinanceiroCurso,
					categoria, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			consultarAlunosComValorInferiorAoNovoPlano(getTurmaVO().getCodigo(), codigoPlanoFinanceiroCurso, categoria,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			consultarAlunosComCondicaoPagamentoEmConformidade(getTurmaVO().getCodigo(), codigoPlanoFinanceiroCurso,
					categoria, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			// Consulta as duas opcoes: Com mais de uma condicao e com apenas uma condicao
			recuperarAlunosQueSeraoAlterados(getTurmaVO().getCodigo(), codigoPlanoFinanceiroCurso, categoria,
					Uteis.NIVELMONTARDADOS_DADOSBASICOS);

		} catch (Exception e) {
			e.printStackTrace();
		}

		atualizarListaComAsSituacoesDaAlteracaoDoPlanoFinanceiro();

	}

	public void limparListasComAsSituacoesDoPlanoFinanceiro() {
		setListaDeAlunosComParcelaGerada(new ArrayList<>());
		setListaDeAlunosComCondicaoPagamentoNaoEncontrada(new ArrayList<>());
		setListaDeAlunosComCondicaoPagamentoDuplicidade(new ArrayList<>());
		setListaDeAlunosComUmaCondicaoPagamento(new ArrayList<>());
		setListaDeAlunosComValorInferiorAoNovoPlano(new ArrayList<>());
		setListaDeAlunosComCondicaoPagamentoEmConformidade(new ArrayList<>());
	}

	private void atualizarListaComAsSituacoesDaAlteracaoDoPlanoFinanceiro() {

		HashMap<SituacaoAlteracaoPlanoFinanceiroEnum, Integer> itens = new HashMap<>();
		List<Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>> list = new ArrayList<Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>>();

		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.COM_PARCELA_GERADA, getListaDeAlunosComParcelaGerada().size());
		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.CONDICAO_PAGAMENTO_NAO_ENCONTRADO,
				getListaDeAlunosComCondicaoPagamentoNaoEncontrada().size());
		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.CONDICAO_PAGAMENTO_EM_DUPLICIDADE,
				getListaDeAlunosComCondicaoPagamentoDuplicidade().size());
		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.COM_UMA_CONDICAO_PAGAMENTO,
				getListaDeAlunosComUmaCondicaoPagamento().size());
		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.VALOR_INFERIOR_NOVO_PLANO,
				getListaDeAlunosComValorInferiorAoNovoPlano().size());
		itens.put(SituacaoAlteracaoPlanoFinanceiroEnum.CONDICAO_PAGAMENTO_EM_CONFORMIDADE,
				getListaDeAlunosComCondicaoPagamentoEmConformidade().size());

		list.addAll(itens.entrySet());
		setSituacaoPlanoAlterado(list);
	}

	public List<Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>> getSituacaoPlanoAlterado() {
		if (situacaoPlanoAlterado == null)
			situacaoPlanoAlterado = new ArrayList<>();
		return situacaoPlanoAlterado;
	}

	public void setSituacaoPlanoAlterado(
			List<Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>> situacaoPlanoAlterado) {
		this.situacaoPlanoAlterado = situacaoPlanoAlterado;
	}

	public Boolean getTelaPrincipalAlteracaoPlanoFinanceiro() {
		if (telaPrincipalAlteracaoPlanoFinanceiro == null)
			telaPrincipalAlteracaoPlanoFinanceiro = false;
		return telaPrincipalAlteracaoPlanoFinanceiro;
	}

	public void setTelaPrincipalAlteracaoPlanoFinanceiro(Boolean telaPrincipalAlteracaoPlanoFinanceiro) {
		this.telaPrincipalAlteracaoPlanoFinanceiro = telaPrincipalAlteracaoPlanoFinanceiro;
	}

	public void exibirDetalheDaAlteracaoDoPlanoFinanceiroDosAlunosDaTurma() {

		Object objSelecionado = (Object) context().getExternalContext().getRequestMap().get("situacoes");

		String situacao = ((Map.Entry<SituacaoAlteracaoPlanoFinanceiroEnum, Integer>) objSelecionado).getKey()
				.toString();

		SituacaoAlteracaoPlanoFinanceiroEnum situacoes = SituacaoAlteracaoPlanoFinanceiroEnum.getEnum(situacao);

		switch (situacoes) {
		case COM_PARCELA_GERADA:
			setAlteracoesPlanoFinanceiro(getListaDeAlunosComParcelaGerada());
			setHabilitarListagemNovaCondicaoPagamento(false);
			setHabilitarBotaGravarAlteracoesPlanoFinanceiro(false);
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		case CONDICAO_PAGAMENTO_NAO_ENCONTRADO:
			setAlteracoesPlanoFinanceiro(getListaDeAlunosComCondicaoPagamentoNaoEncontrada());
			setHabilitarListagemNovaCondicaoPagamento(false);
			setHabilitarBotaGravarAlteracoesPlanoFinanceiro(false);
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		case CONDICAO_PAGAMENTO_EM_DUPLICIDADE:
			setAlteracoesPlanoFinanceiro(getListaDeAlunosComCondicaoPagamentoDuplicidade());
			setHabilitarListagemNovaCondicaoPagamento(true);
			setHabilitarBotaGravarAlteracoesPlanoFinanceiro(true);
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		case COM_UMA_CONDICAO_PAGAMENTO:
			listarAluunosComUmaCondicaoDePagamentoQueSeraAlterado();
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		case VALOR_INFERIOR_NOVO_PLANO:
			setAlteracoesPlanoFinanceiro(getListaDeAlunosComValorInferiorAoNovoPlano());
			setHabilitarListagemNovaCondicaoPagamento(false);
			setHabilitarBotaGravarAlteracoesPlanoFinanceiro(false);
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		case CONDICAO_PAGAMENTO_EM_CONFORMIDADE:
			setAlteracoesPlanoFinanceiro(getListaDeAlunosComCondicaoPagamentoEmConformidade());
			setHabilitarListagemNovaCondicaoPagamento(false);
			setHabilitarBotaGravarAlteracoesPlanoFinanceiro(false);
			setSituacaoAtualNoDetalhe(situacoes.getValor());
			break;
		default:
			break;
		}

		setSituacaoExibidaNoDetalhe(situacoes.getDescricao());
		setTelaPrincipalAlteracaoPlanoFinanceiro(false);

	}

	public void listarAluunosComUmaCondicaoDePagamentoQueSeraAlterado() {
		setAlteracoesPlanoFinanceiro(getListaDeAlunosComUmaCondicaoPagamento());
		setHabilitarListagemNovaCondicaoPagamento(true);
		setHabilitarBotaGravarAlteracoesPlanoFinanceiro(false);
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getAlteracoesPlanoFinanceiro() {
		if (alteracoesPlanoFinanceiro == null)
			alteracoesPlanoFinanceiro = new ArrayList<>();
		return alteracoesPlanoFinanceiro;
	}

	public void setAlteracoesPlanoFinanceiro(List<AlteracaoPlanoFinanceiroAlunoTurmaVO> alteracoesPlanoFinanceiro) {
		this.alteracoesPlanoFinanceiro = alteracoesPlanoFinanceiro;
	}

	public void retornarTelaPrincipalAlteracaoPlanoFinanceiro() {

		atualizarListaComAsSituacoesDaAlteracaoDoPlanoFinanceiro();

		setTelaPrincipalAlteracaoPlanoFinanceiro(true);
	}

	public void setPermiteAlterarPlanoFinanceiro(Boolean permiteAlterarPlanoFinanceiro) {
		this.permiteAlterarPlanoFinanceiro = permiteAlterarPlanoFinanceiro;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComParcelaGerada() {
		if (listaDeAlunosComParcelaGerada == null)
			listaDeAlunosComParcelaGerada = new ArrayList<>();
		return listaDeAlunosComParcelaGerada;
	}

	public void setListaDeAlunosComParcelaGerada(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComParcelaGerada) {
		this.listaDeAlunosComParcelaGerada = listaDeAlunosComParcelaGerada;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComValorInferiorAoNovoPlano() {
		if (listaDeAlunosComValorInferiorAoNovoPlano == null)
			listaDeAlunosComValorInferiorAoNovoPlano = new ArrayList<>();
		return listaDeAlunosComValorInferiorAoNovoPlano;
	}

	public void setListaDeAlunosComValorInferiorAoNovoPlano(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComValorInferiorAoNovoPlano) {
		this.listaDeAlunosComValorInferiorAoNovoPlano = listaDeAlunosComValorInferiorAoNovoPlano;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComCondicaoPagamentoNaoEncontrada() {
		if (listaDeAlunosComCondicaoPagamentoNaoEncontrada == null)
			listaDeAlunosComCondicaoPagamentoNaoEncontrada = new ArrayList<>();
		return listaDeAlunosComCondicaoPagamentoNaoEncontrada;
	}

	public void setListaDeAlunosComCondicaoPagamentoNaoEncontrada(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoNaoEncontrada) {
		this.listaDeAlunosComCondicaoPagamentoNaoEncontrada = listaDeAlunosComCondicaoPagamentoNaoEncontrada;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComCondicaoPagamentoDuplicidade() {
		if (listaDeAlunosComCondicaoPagamentoDuplicidade == null)
			listaDeAlunosComCondicaoPagamentoDuplicidade = new ArrayList<>();
		return listaDeAlunosComCondicaoPagamentoDuplicidade;
	}

	public void setListaDeAlunosComCondicaoPagamentoDuplicidade(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoDuplicidade) {
		this.listaDeAlunosComCondicaoPagamentoDuplicidade = listaDeAlunosComCondicaoPagamentoDuplicidade;
	}

	public Boolean getHabilitarListagemNovaCondicaoPagamento() {
		if (habilitarListagemNovaCondicaoPagamento == null)
			habilitarListagemNovaCondicaoPagamento = false;
		return habilitarListagemNovaCondicaoPagamento;
	}

	public void setHabilitarListagemNovaCondicaoPagamento(Boolean habilitarListagemNovaCondicaoPagamento) {
		this.habilitarListagemNovaCondicaoPagamento = habilitarListagemNovaCondicaoPagamento;
	}

	public Boolean getHabilitarBotaGravarAlteracoesPlanoFinanceiro() {
		if (habilitarBotaGravarAlteracoesPlanoFinanceiro == null)
			habilitarBotaGravarAlteracoesPlanoFinanceiro = false;
		return habilitarBotaGravarAlteracoesPlanoFinanceiro;
	}

	public void setHabilitarBotaGravarAlteracoesPlanoFinanceiro(Boolean habilitarBotaGravarAlteracoesPlanoFinanceiro) {
		this.habilitarBotaGravarAlteracoesPlanoFinanceiro = habilitarBotaGravarAlteracoesPlanoFinanceiro;
	}

	public List<SelectItem> getListaSelectItemNovaCondicaoPagamento() {
		if (listaSelectItemNovaCondicaoPagamento == null)
			listaSelectItemNovaCondicaoPagamento = new ArrayList<>();
		return listaSelectItemNovaCondicaoPagamento;
	}

	public void setListaSelectItemNovaCondicaoPagamento(List<SelectItem> listaSelectItemNovaCondicaoPagamento) {
		this.listaSelectItemNovaCondicaoPagamento = listaSelectItemNovaCondicaoPagamento;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComUmaCondicaoPagamento() {
		if (listaDeAlunosComUmaCondicaoPagamento == null)
			listaDeAlunosComUmaCondicaoPagamento = new ArrayList<>();
		return listaDeAlunosComUmaCondicaoPagamento;
	}

	public void setListaDeAlunosComUmaCondicaoPagamento(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComUmaCondicaoPagamento) {
		this.listaDeAlunosComUmaCondicaoPagamento = listaDeAlunosComUmaCondicaoPagamento;
	}

	public void gravarAlteracaoAluno() {

		for (Iterator<AlteracaoPlanoFinanceiroAlunoTurmaVO> iter = getListaDeAlunosComCondicaoPagamentoDuplicidade()
				.listIterator(); iter.hasNext();) {
			AlteracaoPlanoFinanceiroAlunoTurmaVO aluno = iter.next();

			if (aluno.getCondicaoPagamentoPlanoFinanceiroCursoVONovo().getCodigo() > 0) {
				getListaDeAlunosComUmaCondicaoPagamento().add(aluno);
				iter.remove();
			}
		}

		retornarTelaPrincipalAlteracaoPlanoFinanceiro();
	}

	/**
	 * Inicializa ProgressBar e chama o metodo para alterar o plano financeiro e a condicao de pagamento do aluno
	 * 
	 */
	public void inicializarProgressBar() {
		try {
			getProgressBar().setConfiguracaoFinanceiroVO(
					getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo()));
			getProgressBar().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBar().iniciar(0l, getListaDeAlunosComUmaCondicaoPagamento().size(), "Iniciando Processamento",
					true, this, "alterarPlanoFinanceiroAlunoConformeDadosDaTurma");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Metodo responsavel por alterar o plano financeiro e a condicao de pagamento do aluno
	 */
	public void alterarPlanoFinanceiroAlunoConformeDadosDaTurma() {
		try {
			getFacadeFactory().getAlteracaoPlanoFinanceiroAlunoTurmaFacade()
					.alterarPlanoFinanceiroAlunoConformeDadosDaTurma(getListaDeAlunosComUmaCondicaoPagamento(),
							getProgressBar().getConfiguracaoFinanceiroVO(), getProgressBar().getUsuarioVO(),
							getTurmaVO(), getProgressBar());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Grava a turma e altera o plano financeiro e a condicao de pagamento do aluno
	 */
	public void gravarTurmaEAlterarCondicaoPagamentoDaMatricula() {

		try {
			if (getListaDeAlunosComUmaCondicaoPagamento().isEmpty()) {
				setMensagemDetalhada("msg_erro",
						UteisJSF.internacionalizar("prt_Turma_naoExistemDadosParaSeremAlterados"));
				return;
			}
			persistir();
			if (getControle()) {
				inicializarProgressBar();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProgressBarVO getProgressBar() {
		if (progressBar == null)
			progressBar = new ProgressBarVO();
		return progressBar;
	}

	public void setProgressBar(ProgressBarVO progressBar) {
		this.progressBar = progressBar;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getListaDeAlunosComCondicaoPagamentoEmConformidade() {
		if (listaDeAlunosComCondicaoPagamentoEmConformidade == null)
			listaDeAlunosComCondicaoPagamentoEmConformidade = new ArrayList<>();
		return listaDeAlunosComCondicaoPagamentoEmConformidade;
	}

	public void setListaDeAlunosComCondicaoPagamentoEmConformidade(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosComCondicaoPagamentoEmConformidade) {
		this.listaDeAlunosComCondicaoPagamentoEmConformidade = listaDeAlunosComCondicaoPagamentoEmConformidade;
	}

	public Boolean getControle() {
		if (controle == null)
			controle = false;
		return controle;
	}

	public void setControle(Boolean controle) {
		this.controle = controle;
	}

	public String getSituacaoExibidaNoDetalhe() {
		if (situacaoExibidaNoDetalhe == null)
			situacaoExibidaNoDetalhe = "";
		return situacaoExibidaNoDetalhe;
	}

	public void setSituacaoExibidaNoDetalhe(String situacaoExibidaNoDetalhe) {
		this.situacaoExibidaNoDetalhe = situacaoExibidaNoDetalhe;
	}

	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> getLogsAlteracoesPlanoFinanceiro() {
		if (logsAlteracoesPlanoFinanceiro == null)
			logsAlteracoesPlanoFinanceiro = new ArrayList<>();
		return logsAlteracoesPlanoFinanceiro;
	}

	public void setLogsAlteracoesPlanoFinanceiro(
			List<AlteracaoPlanoFinanceiroAlunoTurmaVO> logsAlteracoesPlanoFinanceiro) {
		this.logsAlteracoesPlanoFinanceiro = logsAlteracoesPlanoFinanceiro;
	}

	public String getSituacaoAtualNoDetalhe() {
		if (situacaoAtualNoDetalhe == null)
			situacaoAtualNoDetalhe = "";
		return situacaoAtualNoDetalhe;
	}

	public void setSituacaoAtualNoDetalhe(String situacaoAtualNoDetalhe) {
		this.situacaoAtualNoDetalhe = situacaoAtualNoDetalhe;
	}

	public void atualizarLista() {
		exibirLogAlteracoesNoPlanoFinanceiroDosAlunosMatriculadosNaTurma();
	}

	public List<SelectItem> getListaSelectItemIndiceReajusteVOs() {
		if (listaSelectItemIndiceReajusteVOs == null) {
			listaSelectItemIndiceReajusteVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemIndiceReajusteVOs;
	}

	public void setListaSelectItemIndiceReajusteVOs(List<SelectItem> listaSelectItemIndiceReajusteVOs) {
		this.listaSelectItemIndiceReajusteVOs = listaSelectItemIndiceReajusteVOs;
	}

	public void montarListaSelectItemIndiceReajustePreco() {
		List<IndiceReajusteVO> listaIndiceReajusteVOs = getFacadeFactory().getIndiceReajusteFacade()
				.consultarPorDescricao("", getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		for (IndiceReajusteVO indiceReajusteVO : listaIndiceReajusteVOs) {
			itens.add(new SelectItem(indiceReajusteVO.getCodigo(), indiceReajusteVO.getDescricao()));
		}
		setListaSelectItemIndiceReajusteVOs(itens);
	}

	public String getUserNameLiberarAlteracaoPlanoFinanceiro() {
		if (userNameLiberarAlteracaoPlanoFinanceiro == null) {
			userNameLiberarAlteracaoPlanoFinanceiro = "";
		}
		return userNameLiberarAlteracaoPlanoFinanceiro;
	}

	public void setUserNameLiberarAlteracaoPlanoFinanceiro(String userNameLiberarAlteracaoPlanoFinanceiro) {
		this.userNameLiberarAlteracaoPlanoFinanceiro = userNameLiberarAlteracaoPlanoFinanceiro;
	}

	public String getSenhaLiberarAlteracaoPlanoFinanceiro() {
		if (senhaLiberarAlteracaoPlanoFinanceiro == null) {
			senhaLiberarAlteracaoPlanoFinanceiro = "";
		}
		return senhaLiberarAlteracaoPlanoFinanceiro;
	}

	public void setSenhaLiberarAlteracaoPlanoFinanceiro(String senhaLiberarAlteracaoPlanoFinanceiro) {
		this.senhaLiberarAlteracaoPlanoFinanceiro = senhaLiberarAlteracaoPlanoFinanceiro;
	}

	public void executarVerificacaoUsuarioPodeAlterarPlanoFinanceiro() {
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarAlteracaoPlanoFinanceiro(),
					this.getSenhaLiberarAlteracaoPlanoFinanceiro(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
					"Turma_alterarCondicaoPagamentoAluno", usuarioVerif);
			usuarioTemPermissaoLiberar = true;
		} catch (Exception e) {
		}
		try {
			if (!usuarioValido) {
				throw new Exception("Usuário/Senha Inválidos");
			}
			if (!usuarioTemPermissaoLiberar) {
				throw new Exception("Você não tem permissão para Alterar Plano Financeiro da Turma.");
			} else {
				setPermiteAlterarPlanoFinanceiro(Boolean.TRUE);
			}
			getOperacaoFuncionalidadePersistirVOs()
					.add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(
							OrigemOperacaoFuncionalidadeEnum.TURMA, getTurmaVO().getCodigo().toString(),
							OperacaoFuncionalidadeEnum.LIBERACAO_ALTERACAO_PLANO_FINANCEIRO_TURMA, usuarioVerif, ""));
			this.setUserNameLiberarAlteracaoPlanoFinanceiro("");
			;
			this.setSenhaLiberarAlteracaoPlanoFinanceiro("");
			setMensagemID("msg_ConfirmacaoLiberacaoAlteracaoPlanoFinanceiroTurma");
		} catch (Exception e) {
			this.setUserNameLiberarAlteracaoPlanoFinanceiro("");
			;
			this.setSenhaLiberarAlteracaoPlanoFinanceiro("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<OperacaoFuncionalidadeVO> getOperacaoFuncionalidadePersistirVOs() {
		if (operacaoFuncionalidadePersistirVOs == null) {
			operacaoFuncionalidadePersistirVOs = new ArrayList<OperacaoFuncionalidadeVO>(0);
		}
		return operacaoFuncionalidadePersistirVOs;
	}

	public void setOperacaoFuncionalidadePersistirVOs(
			List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs) {
		this.operacaoFuncionalidadePersistirVOs = operacaoFuncionalidadePersistirVOs;
	}

	public void inicializarDadosUsuarioSenhaLiberacaoAlteracaoPlanoFinanceiro() {
		setUserNameLiberarAlteracaoPlanoFinanceiro("");
		setSenhaLiberarAlteracaoPlanoFinanceiro("");
	}

	public String getMensagemLiberacaoAlteracaoPlanoFinanceiro() {
		if (mensagemLiberacaoAlteracaoPlanoFinanceiro == null) {
			mensagemLiberacaoAlteracaoPlanoFinanceiro = "";
		}
		return mensagemLiberacaoAlteracaoPlanoFinanceiro;
	}

	public void setMensagemLiberacaoAlteracaoPlanoFinanceiro(String mensagemLiberacaoAlteracaoPlanoFinanceiro) {
		this.mensagemLiberacaoAlteracaoPlanoFinanceiro = mensagemLiberacaoAlteracaoPlanoFinanceiro;
	}

	public Boolean getApresentarAbaFinanceiro() {
		if (apresentarAbaFinanceiro == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(
						"Turma_permiteAcessoAbaFinanceiro", getUsuarioLogado());
				apresentarAbaFinanceiro = Boolean.TRUE;
			} catch (Exception e) {
				apresentarAbaFinanceiro = Boolean.FALSE;
			}
		}
		return apresentarAbaFinanceiro;
	}

	public void realizarNavegacaoVagaTurma() {
		removerControleMemoriaFlashTela("VagaTurmaControle");
		context().getExternalContext().getSessionMap().put("vagaTurma.turma", getTurmaVO().getCodigo());
		if (!getTurmaVO().getIntegral()) {
			context().getExternalContext().getSessionMap().put("vagaTurma.ano", Uteis.getAnoDataAtual4Digitos());
		}
		if (getTurmaVO().getSemestral()) {
			context().getExternalContext().getSessionMap().put("vagaTurma.semestre", Uteis.getSemestreAtual());
		}
	}

	public void consultarAlunoComConfiguracaoAcademicaDiferenteTurmaDisciplinaComposta() {
		try {
			TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO = (TurmaDisciplinaCompostaVO) getRequestMap()
					.get("turmaDisciplinaCompostaItem");
			setDisciplinaVO(turmaDisciplinaCompostaVO.getGradeDisciplinaCompostaVO().getDisciplina());
			setTurmaDisciplinaEstatisticaAlunoVOs(getFacadeFactory().getTurmaDisciplinaFacade()
					.consultarPorAlunoComConfiguracaoAcademicaDiferenteTurma(getTurmaVO(), null,
							turmaDisciplinaCompostaVO, turmaDisciplinaCompostaVO.getConfiguracaoAcademicoVO(),
							turmaDisciplinaCompostaVO.getGradeDisciplinaCompostaVO().getDisciplina(), getUsuario()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private Boolean permiteAlterarConfiguracaoAcademico;

	public Boolean getPermiteAlterarConfiguracaoAcademico() {
		if (permiteAlterarConfiguracaoAcademico == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Turma_permiteAlterarConfiguracaoAcademica", getUsuarioLogado());
				permiteAlterarConfiguracaoAcademico = true;
			} catch (Exception e) {
				permiteAlterarConfiguracaoAcademico = false;
			}

		}
		return permiteAlterarConfiguracaoAcademico;
	}

	public void setPermiteAlterarConfiguracaoAcademico(Boolean permiteAlterarConfiguracaoAcademico) {
		this.permiteAlterarConfiguracaoAcademico = permiteAlterarConfiguracaoAcademico;
	}
	
	public void persistirAlteracaoGradeCurricularCursoIntegral() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getTurmaFacade().realizarAtualizacaoGradeCurricularCursoIntegral(getTurmaVO(), getTurmaNovaGradeCurricular(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado(), getListaMatriculaPeriodo());
			getTurmaVO().getTurmaDisciplinaVOs().clear();
			getFacadeFactory().getTurmaDisciplinaFacade().consultaTurmaDisciplinaCompletaPorTurma(getTurmaVO(), getUsuarioLogado());
			getTurmaVO().setGradeCurricularAtiva(getTurmaVO().getGradeCurricularVO().getCodigo());
			getTurmaVO().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getTurmaVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if(getTurmaVO().getPeridoLetivo().getControleOptativaGrupo() && !getTurmaVO().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo().equals(0)) {
				getTurmaVO().getPeridoLetivo().setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(this.getTurmaVO().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			}
			validarTurmaDisciplinaQueNaoEstaoNoPeriodoLetivoDaTurma();
			getTurmaVO().atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			obterSalaLocalAulaCasoExistesseAnteriormente();
			montarListaSelectItemModalidadeDisciplina();
			montarListaSelectItemPeriodoLetivo();
			setTurmaNovaGradeCurricular(new TurmaVO());
			setMensagemID("msg_dados_gravados");
			setOncompleteModal("RichFaces.$('panelAlterarMatrizCurricularCursoIntegral').hide();");
		} catch (ConsistirException ex) {
			setOncompleteModal("");
			setManterModalAberto("");
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setOncompleteModal("");
			setManterModalAberto("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void carregarDadosParaAlteracaoGradeCurricularCursoIntegral() {
		try {
			setTurmaNovaGradeCurricular(new TurmaVO());
			montarListaSelectItemNovaGradeCurricular();
			limparCamposAlterarMatrizCurricular();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparCamposAlterarMatrizCurricular() {
		try {
			getTurmaVO().getTurmaDisciplinaVOs().stream()
			.forEach(p-> {
				p.setAlterarMatrizCurricular(false);
				p.setQtdAlunosReposicao(0);
				p.setOperacaoMatrizCurricular(null);
				p.setOperacaoMatrizCurricularTemp(null);
				p.setOperacaoMatrizCurricularReposicao(null);
				p.getListaSelectItemOperacaoMatrizCurricular().clear();
				p.getListaSelectItemOperacaoMatrizCurricularReposicao().clear();
			});
			getTurmaNovaGradeCurricular().setTurmaDisciplinaVOs(new ArrayList<>());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void validarDadosParaAlteracaoMatrizCurricularCursoIntegral() {
		try {
			TurmaDisciplinaVO obj = (TurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
			if(obj.getOperacaoMatrizCurricular().equals(-2) && getFacadeFactory().getRegistroAulaFacade().existeRegistroAula(getTurmaVO(), obj.getDisciplina())){
				throw new Exception("Não é possível excluir essa disciplina "+obj.getDisciplina().getNome()+" da grade, pois a mesma tem aula registrada para essa turma.");
			}
						
			if(!obj.getOperacaoMatrizCurricular().equals(-2) && !obj.getOperacaoMatrizCurricular().equals(-1)){
				if(getTurmaVO().getTurmaDisciplinaVOs()
				.stream().anyMatch(p-> !p.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo()) && p.getOperacaoMatrizCurricular().equals(obj.getOperacaoMatrizCurricular()))){
					throw new Exception("Não é possível escolher a correspondência de código  "+obj.getOperacaoMatrizCurricular()+" para a disciplina "+obj.getDisciplina().getNome()+ ", pois ela já esta sendo utilizada para outra discipina.");
				}
				getTurmaNovaGradeCurricular().getTurmaDisciplinaVOs()
				.stream().filter(p-> !p.isAlterarMatrizCurricular() && p.getDisciplina().getCodigo().equals(obj.getOperacaoMatrizCurricular()))
				.forEach(p->{
					p.setOperacaoMatrizCurricular(obj.getOperacaoMatrizCurricular());});
			}
			if(!obj.getOperacaoMatrizCurricularTemp().equals(-2) && !obj.getOperacaoMatrizCurricularTemp().equals(-1)){
				getTurmaNovaGradeCurricular().getTurmaDisciplinaVOs()
				.stream().filter(p-> !p.isAlterarMatrizCurricular() && p.getDisciplina().getCodigo().equals(obj.getOperacaoMatrizCurricularTemp()))
				.forEach(p->{
					p.setOperacaoMatrizCurricular(null);
					});
			}
			obj.setOperacaoMatrizCurricularTemp(obj.getOperacaoMatrizCurricular());
			if(obj.getOperacaoMatrizCurricular().equals(-2) || obj.getOperacaoMatrizCurricular().equals(-1)){
				obj.setOperacaoMatrizCurricularReposicao(-2);
			}
			limparMensagem();
		} catch (Exception e) {
			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public void montarListaSelectItemPeriodoLetivoTemp() {
		try {
			getTurmaNovaGradeCurricular().setPeridoLetivo(new PeriodoLetivoVO());
			List<PeriodoLetivoVO> resultadoConsulta = consultarPeriodoLetivoPorGradecurricular(getTurmaNovaGradeCurricular().getGradeCurricularVO().getCodigo());
			setListaSelectItemPeriodoLetivoTemp(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
			limparCamposAlterarMatrizCurricular(); 
			limparMensagem();
		} catch (Exception e) {
			limparCamposAlterarMatrizCurricular(); 
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarDadosCompletosPeriodoLetivoNovaMatrizCurricular() {
		try {
			if (Uteis.isAtributoPreenchido(getTurmaNovaGradeCurricular().getPeridoLetivo())) {
				limparCamposAlterarMatrizCurricular(); 
				getTurmaNovaGradeCurricular().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getTurmaNovaGradeCurricular().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				if ((getTurmaNovaGradeCurricular().getPeridoLetivo().getControleOptativaGrupo()) && (!getTurmaNovaGradeCurricular().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo().equals(0))) {
					getTurmaNovaGradeCurricular().getPeridoLetivo().setGradeCurricularGrupoOptativa(getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(getTurmaNovaGradeCurricular().getPeridoLetivo().getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
				}
				List<GradeDisciplinaVO> listaGradeDisciplina = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getTurmaNovaGradeCurricular().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
				if(Uteis.isAtributoPreenchido(listaGradeDisciplina)){
					getTurmaNovaGradeCurricular().setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getTurmaNovaGradeCurricular().getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getFacadeFactory().getTurmaFacade().executarGeracaoTurmaDisciplinaVOs(getTurmaNovaGradeCurricular(), listaGradeDisciplina, getUsuarioLogado());
					if(Uteis.isAtributoPreenchido(getTurmaNovaGradeCurricular().getPeridoLetivo().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs())){
						for (GradeCurricularGrupoOptativaDisciplinaVO gcgod : getTurmaNovaGradeCurricular().getPeridoLetivo().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
							getFacadeFactory().getTurmaFacade().executarGeracaoTurmaDisciplinaGradeCurricularGrupoOptativaDisciplina(getTurmaNovaGradeCurricular(), gcgod, getUsuarioLogado());
						}						
					}
					turmaDisciplinaAtual
					:for (TurmaDisciplinaVO tdAtual : getTurmaVO().getTurmaDisciplinaVOs()) {
						for (TurmaDisciplinaVO tdNovaGrade : getTurmaNovaGradeCurricular().getTurmaDisciplinaVOs()) {
							if(tdAtual.getDisciplina().getCodigo().equals(tdNovaGrade.getDisciplina().getCodigo())){
								tdAtual.setAlterarMatrizCurricular(true);
								tdNovaGrade.setAlterarMatrizCurricular(true);
								tdNovaGrade.setQtdAlunosReposicao(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaQtdMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(getTurmaVO().getGradeCurricularVO().getCodigo(), getTurmaVO().getCodigo(), tdNovaGrade.getDisciplina().getCodigo(), false, getUsuarioLogado()));
								continue turmaDisciplinaAtual;
							}
						}
					}
					montarListaSelectItemOperacaoMatrizCurricular();
				}
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_Turma_peridoLetivoSelecionado"));
			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			limparCamposAlterarMatrizCurricular();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void montarListaSelectItemOperacaoMatrizCurricular() throws Exception {
		getTurmaVO().setQtdMatriculados(getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeAlunoMatriculadoTurmaPorSituacao(getTurmaVO().getCodigo(), getTurmaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
		getTurmaVO().getTurmaDisciplinaVOs()
		.stream().filter(p1-> !p1.isAlterarMatrizCurricular())
		.forEach(p1->{
			try {
				if(getTurmaVO().isExisteAlunosMatriculados()){
					p1.getListaSelectItemOperacaoMatrizCurricular().clear();
					p1.getListaSelectItemOperacaoMatrizCurricular().add(new SelectItem(-2, "Excluir"));
					p1.getListaSelectItemOperacaoMatrizCurricular().add(new SelectItem(-1, "Fora da Grade"));
					getTurmaNovaGradeCurricular().getTurmaDisciplinaVOs()
					.stream().filter(p-> !p.isAlterarMatrizCurricular() && !p.getDisciplinaReferenteAUmGrupoOptativa())
					.forEach(p->{p1.getListaSelectItemOperacaoMatrizCurricular().add(new SelectItem(p.getDisciplina().getCodigo(), p.getDisciplina().getCodigo().toString()+" - "+p.getDisciplina().getNome()));});
					
					getTurmaNovaGradeCurricular().getTurmaDisciplinaVOs()
					.stream().filter(p-> !p.isAlterarMatrizCurricular() && p.getDisciplinaReferenteAUmGrupoOptativa())
					.forEach(p->{p1.getListaSelectItemOperacaoMatrizCurricular().add(new SelectItem(p.getDisciplina().getCodigo(), p.getDisciplina().getCodigo().toString()+" - Grupo Optativa - "+p.getDisciplina().getNome()));});
				}
				p1.setQtdAlunosReposicao(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaQtdMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(getTurmaVO().getGradeCurricularVO().getCodigo(), getTurmaVO().getCodigo(), p1.getDisciplina().getCodigo(), false, getUsuarioLogado()));
				p1.getListaSelectItemOperacaoMatrizCurricularReposicao().clear();
				if(p1.getQtdAlunosReposicao() > 0){
					p1.getListaSelectItemOperacaoMatrizCurricularReposicao().add(new SelectItem(-2, "Excluir"));
					p1.getListaSelectItemOperacaoMatrizCurricularReposicao().add(new SelectItem(-1, "Manter/Equivalência"));
				}	
			} catch (Exception e) {
				new StreamSeiException(e);
			}
		});
	}
	
	public void montarListaSelectItemNovaGradeCurricular() {
		try {
			List<GradeCurricularVO> resultadoConsulta = consultarGradeCurricularCurso(getTurmaVO().getCurso().getCodigo());
			getListaSelectItemNovaGradeCurricular().clear();
			getListaSelectItemNovaGradeCurricular().add(new SelectItem(0, ""));
			resultadoConsulta.stream()
			.filter(p-> !p.getSituacao().equals("CO") && !p.getCodigo().equals(getTurmaVO().getGradeCurricularVO().getCodigo()))
			.forEach(p->{
				getListaSelectItemNovaGradeCurricular().add(new SelectItem(p.getCodigo(), p.getSituacao_Apresentar() +" -- "+p.getNome()));
			});
			Collections.sort(getListaSelectItemNovaGradeCurricular(), new SelectItemOrdemValor());
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private Boolean permiteAlterarMatrizCurricularCursoIntegral;

	public Boolean getPermiteAlterarMatrizCurricularCursoIntegral() {
		if (permiteAlterarMatrizCurricularCursoIntegral == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.TURMA_PERMITE_ALTERAR_MATRIZ_CURRICULAR_CURSO_INTEGRAL, getUsuarioLogado());
				permiteAlterarMatrizCurricularCursoIntegral = true;
			} catch (Exception e) {
				permiteAlterarMatrizCurricularCursoIntegral = false;
			}
		}
		return permiteAlterarMatrizCurricularCursoIntegral;
	}

	public void setPermiteAlterarMatrizCurricularCursoIntegral(Boolean permiteAlterarMatrizCurricularCursoIntegral) {
		this.permiteAlterarMatrizCurricularCursoIntegral = permiteAlterarMatrizCurricularCursoIntegral;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivoTemp() {
		if (listaSelectItemPeriodoLetivoTemp == null) {
			listaSelectItemPeriodoLetivoTemp = new ArrayList<>();
		}
		return listaSelectItemPeriodoLetivoTemp;
	}

	public void setListaSelectItemPeriodoLetivoTemp(List<SelectItem> listaSelectItemPeriodoLetivoTemp) {
		this.listaSelectItemPeriodoLetivoTemp = listaSelectItemPeriodoLetivoTemp;
	}

	public Boolean getValidarAlteracaoMatrizCurricularCursoIntegral() {
		if (!getPermiteAlterarMatrizCurricularCursoIntegral() || !getTurmaVO().getIntegralSemValidarLiberarRegistroAulaEntrePeriodo() || getTurmaVO().getSubturma() || getTurmaVO().getTurmaAgrupada()) {
			return false;
		}
		return true;
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

	public List<SelectItem> comboBoxTipoMatricula;

	public List<SelectItem> getComboBoxTipoMatricula() {
		if (comboBoxTipoMatricula == null) {
			comboBoxTipoMatricula = new ArrayList<SelectItem>(0);
			comboBoxTipoMatricula.add(new SelectItem(TipoContratoMatriculaEnum.NORMAL, TipoContratoMatriculaEnum.NORMAL.getDescricao()));
			comboBoxTipoMatricula.add(new SelectItem(TipoContratoMatriculaEnum.EXTENSAO, TipoContratoMatriculaEnum.EXTENSAO.getDescricao()));
			comboBoxTipoMatricula.add(new SelectItem(TipoContratoMatriculaEnum.FIADOR, TipoContratoMatriculaEnum.FIADOR.getDescricao()));
		}
		return comboBoxTipoMatricula;
	}

	public void adicionarTurmaContratoVO() {
		try {
			getFacadeFactory().getTurmaContratoFacade().adicionarTurmaContratoVOs(getTurmaVO(), getTurmaContratoVO(),
					getUsuarioLogado());
			setTurmaContratoVO(new TurmaContratoVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerTurmaContratoVO() {
		try {
			getFacadeFactory().getTurmaContratoFacade().removerTurmaContratoVOs(getTurmaVO(),
					(TurmaContratoVO) getRequestMap().get("turmaContratoItens"), getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public TurmaContratoVO getTurmaContratoVO() {
		if (turmaContratoVO == null) {
			turmaContratoVO = new TurmaContratoVO();
		}
		return turmaContratoVO;
	}

	public void setTurmaContratoVO(TurmaContratoVO turmaContratoVO) {
		this.turmaContratoVO = turmaContratoVO;
	}

	public TurmaVO getTurmaNovaGradeCurricular() {
		if (turmaNovaGradeCurricular == null) {
			turmaNovaGradeCurricular = new TurmaVO();
		}
		return turmaNovaGradeCurricular;
	}

	public void setTurmaNovaGradeCurricular(TurmaVO turmaNovaGradeCurricular) {
		this.turmaNovaGradeCurricular = turmaNovaGradeCurricular;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoContratoMatricula() {
		if (listaSelectItemTextoPadraoContratoMatricula == null) {
			listaSelectItemTextoPadraoContratoMatricula = consultarTextoPadrao("MA");
		}
		return listaSelectItemTextoPadraoContratoMatricula;
	}

	public void setListaSelectItemTextoPadraoContratoMatricula(
			List<SelectItem> listaSelectItemTextoPadraoContratoMatricula) {
		this.listaSelectItemTextoPadraoContratoMatricula = listaSelectItemTextoPadraoContratoMatricula;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoContratoExtensao() {
		if (listaSelectItemTextoPadraoContratoMatricula == null) {
			listaSelectItemTextoPadraoContratoMatricula = consultarTextoPadrao("EX");
		}
		return listaSelectItemTextoPadraoContratoExtensao;
	}

	public void setListaSelectItemTextoPadraoContratoExtensao(List<SelectItem> listaSelectItemTextoPadraoContratoExtensao) {
		this.listaSelectItemTextoPadraoContratoExtensao = listaSelectItemTextoPadraoContratoExtensao;
	}

	public void montarListaSelectItemTextoPadrao() {
		getTurmaContratoVO().setTextoPadraoVO(null);
		if (getTurmaContratoVO().getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)) {
			setListaSelectItemTextoPadraoContratoMatricula(consultarTextoPadrao("MA"));
		} else if (getTurmaContratoVO().getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)) {
			setListaSelectItemTextoPadraoContratoFiador(consultarTextoPadrao("FI"));
		} else {
			setListaSelectItemTextoPadraoContratoExtensao(consultarTextoPadrao("EX"));
		}
	}

	public List<SelectItem> consultarTextoPadrao(String tipoContratoMatriculaEnum) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		try {
			List<TextoPadraoVO> textoPadraoVOs = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox(tipoContratoMatriculaEnum, getTurmaVO().getUnidadeEnsino(), "AT", false, getUsuarioLogado());
			itens = UtilSelectItem.getListaSelectItem(textoPadraoVOs, "codigo", "descricao");
		} catch (Exception e) {

		}
		return itens;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoContratoFiador() {
		if(listaSelectItemTextoPadraoContratoFiador == null){
			listaSelectItemTextoPadraoContratoFiador = consultarTextoPadrao("FI");
		}
		return listaSelectItemTextoPadraoContratoFiador;
	}

	public void setListaSelectItemTextoPadraoContratoFiador(List<SelectItem> listaSelectItemTextoPadraoContratoFiador) {
		this.listaSelectItemTextoPadraoContratoFiador = listaSelectItemTextoPadraoContratoFiador;
	}
	
	public List<String> getListaDigitoTurma(){		
		List<String> listaDigitoTurma = IntStream.rangeClosed('A', 'Z')
	            .mapToObj(i -> Character.toString ((char) i)).collect(Collectors.toList());
		
		listaDigitoTurma.add(0, "");

		return listaDigitoTurma;
		
		
	}
	
	
	
	public String getManterModalAberto() {
		if (manterModalAberto == null) {
			manterModalAberto = "";
		}
		return manterModalAberto;
	}

	public void setManterModalAberto(String manterModalAberto) {
		this.manterModalAberto = manterModalAberto;
	}
	
	public void selecionarTurmaDisciplinaEstatisticaAlunoVO() {
		TurmaDisciplinaEstatisticaAlunoVO obj = (TurmaDisciplinaEstatisticaAlunoVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaEstatisticaItens");
		setTurmaDisciplinaEstatisticaAlunoVO(obj);
	}
	
	public List<MatriculaVO> getListaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula() {
		if (listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula == null) {
			listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula;
	}

	public void setListaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula(List<MatriculaVO> listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula) {
		this.listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula = listaMatriculaControleGeracaoParcelaDataBaseDefinidoMatricula;
	}
	
	public Date getDataBaseGeracaoParcelasTemporaria() {
		return dataBaseGeracaoParcelasTemporaria;
	}

	public void setDataBaseGeracaoParcelasTemporaria(Date dataBaseGeracaoParcelasTemporaria) {
		this.dataBaseGeracaoParcelasTemporaria = dataBaseGeracaoParcelasTemporaria;
	}
	
	public List<SelectItem> getListaSelectItemTurnoApresentarCenso() {
		if (listaSelectItemTurnoApresentarCenso == null) {
			listaSelectItemTurnoApresentarCenso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurnoApresentarCenso;
	}

	public void setListaSelectItemTurnoApresentarCenso(List<SelectItem> listaSelectItemTurnoApresentarCenso) {
		this.listaSelectItemTurnoApresentarCenso = listaSelectItemTurnoApresentarCenso;
	}

	public void montarListaSelectItemTurnoApresentarCenso() {
		try {
			List<NomeTurnoCensoEnum> listaTurnos = Arrays.asList(NomeTurnoCensoEnum.values());
			getListaSelectItemTurnoApresentarCenso().add(new SelectItem(0, ""));
			getListaSelectItemTurnoApresentarCenso().addAll(listaTurnos.stream().map(t -> new SelectItem(t.getValorCenso(), t.getValorApresentar())).collect(Collectors.toList()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<MatriculaPeriodoVO> getListaAlunosComTransferenciaMatrizCurricular() {
		if (listaAlunosComTransferenciaMatrizCurricular == null) {
			listaAlunosComTransferenciaMatrizCurricular = new ArrayList<MatriculaPeriodoVO>();
		}
		return listaAlunosComTransferenciaMatrizCurricular;
	}

	public void setListaAlunosComTransferenciaMatrizCurricular(List<MatriculaPeriodoVO> listaAlunosComTransferenciaMatrizCurricular) {
		this.listaAlunosComTransferenciaMatrizCurricular = listaAlunosComTransferenciaMatrizCurricular;
	}
	
	
	
	public List<MatriculaPeriodoVO> getListaMatriculaPeriodo() {
		if (listaMatriculaPeriodo == null) {
			listaMatriculaPeriodo = new ArrayList<MatriculaPeriodoVO>();
		}
		return listaMatriculaPeriodo;
	}

	public void setListaMatriculaPeriodo(List<MatriculaPeriodoVO> listaMatriculaPeriodo) {
		this.listaMatriculaPeriodo = listaMatriculaPeriodo;
	}

	public void confirmarTransferenciaMatrizCurricular() {
		setOncompleteModal("");
		getListaAlunosComTransferenciaMatrizCurricular().clear();
		getListaMatriculaPeriodo().clear();
		try {
			if (getTurmaVO().isExisteAlunosMatriculados()) {
				setListaMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaPeriodoUnicaPorTurma(null, getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado()));
				Iterator<MatriculaPeriodoVO> i = getListaMatriculaPeriodo().iterator();
				while (i.hasNext()) {
					MatriculaPeriodoVO objExistente = i.next();
					if (!objExistente.getGradeCurricular().getCodigo().equals(objExistente.getMatriculaVO().getGradeCurricularAtual().getCodigo())) {
						getListaAlunosComTransferenciaMatrizCurricular().add(objExistente);
						i.remove();
					}
				}
			}
			if(Uteis.isAtributoPreenchido(getListaAlunosComTransferenciaMatrizCurricular())) {
				setOncompleteModal("RichFaces.$('panelConfirmarTransferenciaMatrizCurricular').show();");
			}else {
				persistirAlteracaoGradeCurricularCursoIntegral();
				setOncompleteModal("RichFaces.$('panelAlterarMatrizCurricularCursoIntegral').hide();");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setOncompleteModal("");
		}
	}
	
	public void alterarDefinicaoTutoriaOnline() {
		try {
			TurmaDisciplinaVO obj = (TurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaItens");
			if (obj != null) {
				obj.getIsDesativarSelectOneMenuDefinicaoTutoriaSeModalidadePresencial();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getPermitirAgruparTurmasUnidadeEnsinoDiferente() {
		if (permitirAgruparTurmasUnidadeEnsinoDiferente == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(
						PerfilAcessoPermissaoAcademicoEnum.TURMA_PERMITIR_AGRUPAR_TURMAS_UNIDADE_ENSINO_DIFERENTE, getUsuarioLogado());
				permitirAgruparTurmasUnidadeEnsinoDiferente = Boolean.TRUE;
			} catch (Exception e) {
				permitirAgruparTurmasUnidadeEnsinoDiferente = Boolean.FALSE;
			}
		}
		return permitirAgruparTurmasUnidadeEnsinoDiferente;
	}
	
	public void limparDadosConsultaTurma() {
		setListaConsultaTurma(new ArrayList<>());
	}

	public List<SelectItem> getListaRegraDefinicaoVaga() {
		if(listaRegraDefinicaoVaga == null) {
			listaRegraDefinicaoVaga = new ArrayList<SelectItem>();
			listaRegraDefinicaoVaga.add(new SelectItem("VC", "Usar Controle Vaga Turma Clonada"));
			listaRegraDefinicaoVaga.add(new SelectItem("VM", "Definir Manualmente as Vagas"));
		}
		return listaRegraDefinicaoVaga;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoClone() {
		if(listaSelectItemUnidadeEnsinoClone == null) {
			listaSelectItemUnidadeEnsinoClone = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsinoClone;
	}

	public void setListaSelectItemUnidadeEnsinoClone(List<SelectItem> listaSelectItemUnidadeEnsinoClone) {
		this.listaSelectItemUnidadeEnsinoClone = listaSelectItemUnidadeEnsinoClone;
	}
	
	
	public void persistirAlteracaoUnidadeEnsinoTurma() {
		try {
			getFacadeFactory().getTurmaFacade().alterarDataBaseGeracaoTurmaParcela(getTurmaVO(), getDataBaseGeracaoParcelasTemporaria(), getListaMatriculaComControleGeracaoParcelaDataBase(), getUsuarioLogado());
			getTurmaVO().setDataBaseGeracaoParcelas(getDataBaseGeracaoParcelasTemporaria());
			gravarLogTurma("alteracao");
			setMensagemID("msg_dados_gravados");
			setControle(true);
		} catch (Exception e) {
			setControle(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			setAtualizarDisciplinaAlunos(Boolean.FALSE);
		}
	}

	public Boolean getApresentarIconeAlteracaoUnidadeEnsinoTurma() {
		if (apresentarIconeAlteracaoUnidadeEnsinoTurma == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Turma_permiteAlterarUnidadeEnsinoturmaIntegral", getUsuarioLogado());
				apresentarIconeAlteracaoUnidadeEnsinoTurma = Boolean.TRUE;
			} catch (Exception e) {
				apresentarIconeAlteracaoUnidadeEnsinoTurma = Boolean.FALSE;
			}
		}
		return apresentarIconeAlteracaoUnidadeEnsinoTurma;
	}

	public void setApresentarIconeAlteracaoUnidadeEnsinoTurma(Boolean apresentarIconeAlteracaoUnidadeEnsinoTurma) {
		this.apresentarIconeAlteracaoUnidadeEnsinoTurma = apresentarIconeAlteracaoUnidadeEnsinoTurma;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsinoAlteracaoTurma() {
		if (listaSelectItemUnidadeEnsinoAlteracaoTurma == null) {
			listaSelectItemUnidadeEnsinoAlteracaoTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsinoAlteracaoTurma;
	}

	public List<MatriculaVO> getListaAlunosUnidadeEnsinoAlterada() {
		if (listaAlunosUnidadeEnsinoAlterada == null) {
			listaAlunosUnidadeEnsinoAlterada = new ArrayList<MatriculaVO>();
		}
		return listaAlunosUnidadeEnsinoAlterada;
	}

	public void setListaAlunosUnidadeEnsinoAlterada(List<MatriculaVO> listaAlunosUnidadeEnsinoAlterada) {
		this.listaAlunosUnidadeEnsinoAlterada = listaAlunosUnidadeEnsinoAlterada;
	}

	public void setListaSelectItemUnidadeEnsinoAlteracaoTurma(List<SelectItem> listaSelectItemUnidadeEnsinoAlteracaoTurma) {
		this.listaSelectItemUnidadeEnsinoAlteracaoTurma = listaSelectItemUnidadeEnsinoAlteracaoTurma;
	}
 
	public UnidadeEnsinoVO getUnidadeEnsinoAlteracaoTurma() {
		if (unidadeEnsinoAlteracaoTurma == null) {
			unidadeEnsinoAlteracaoTurma = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoAlteracaoTurma;
	}

	public void setUnidadeEnsinoAlteracaoTurma(UnidadeEnsinoVO unidadeEnsinoAlteracaoTurma) {
		this.unidadeEnsinoAlteracaoTurma = unidadeEnsinoAlteracaoTurma;
	}

	public void montarListaSelectItemUnidadeEnsinoAlteracaoTurma() throws Exception {
		setUnidadeEnsinoAlteracaoTurma(null);
		setListaAlunosUnidadeEnsinoAlterada(new ArrayList<MatriculaVO>(0));
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false,Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty() || getApresentarAviso()) {
				resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			}
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}

			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}

			Collections.sort(objs, new SelectItemOrdemValor());
			setListaSelectItemUnidadeEnsinoAlteracaoTurma(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			setMensagemID("msg_dados_editar");
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
 
	public void selecionarUnidadeEnsinoAlteracaoTurma() throws Exception {
		getListaAlunosUnidadeEnsinoAlterada().clear();
		setListaAlunosUnidadeEnsinoAlterada(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaIntegralUnidadeEnsino(getTurmaVO(),false, getUsuarioLogado()));
	}

	public void executarAlteracaoUnidadeEnsinoTurma() throws Exception {
		try {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsinoAlteracaoTurma.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().removerConstraintValidacaoMatriculaPeriodo();
			getFacadeFactory().getTurmaFacade().persistirAlteracaoUnidadeEnsinoTurma(getTurmaVO(),unidadeEnsinoVO,getUsuarioLogado(), getPermitirAgruparTurmasUnidadeEnsinoDiferente());
			getTurmaVO().setUnidadeEnsino(unidadeEnsinoVO); 
			getListaAlunosUnidadeEnsinoAlterada().clear();
			setListaAlunosUnidadeEnsinoAlterada(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaIntegralUnidadeEnsino(getTurmaVO(),false, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally {
			getFacadeFactory().getMatriculaPeriodoFacade().incluirConstraintValidacaoMatriculaPeriodo();
		}
	}
	
	public List<String> getListaErroAtualizarAluno() {
		if (listaErroAtualizarAluno == null) {
			listaErroAtualizarAluno = new ArrayList<>(0);
		}
		return listaErroAtualizarAluno;
	}
	
	public void setListaErroAtualizarAluno(List<String> listaErroAtualizarAluno) {
		this.listaErroAtualizarAluno = listaErroAtualizarAluno;
	}

	public ControleConsultaTurma getControleConsultaTurma() {
		if(controleConsultaTurma == null) {
			controleConsultaTurma =  new ControleConsultaTurma();
			controleConsultaTurma.setLimitePorPagina(10);
			controleConsultaTurma.setPage(0);
			controleConsultaTurma.setPaginaAtual(1);
		}
		return controleConsultaTurma;
	}

	public void setControleConsultaTurma(ControleConsultaTurma controleConsultaTurma) {
		this.controleConsultaTurma = controleConsultaTurma;
	}
	
	

}