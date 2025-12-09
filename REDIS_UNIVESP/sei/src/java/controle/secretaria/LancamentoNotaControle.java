package controle.secretaria;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.secretaria.enumeradores.FormaReplicarNotaOutraDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.FechamentoPeriodoLetivoException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.GradeCurricularAlunoRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;


@Controller("LancamentoNotaControle")
@Scope("viewScope")
@Lazy
public class LancamentoNotaControle extends SuperControleRelatorio implements Serializable {

	private HistoricoVO historicoVO;
	private HistoricoVO historicoApresentarVO;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaApresentar;
	private CursoVO cursoApresentar;
	private String disciplina_Erro;
	private String matricula_Erro;
	private String turma_Erro;
	private String mensagemCalculoNotas_Erro;
	private String responsavel_Erro;
	private String tipoInformarNota;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private List listaSelectItemTipoInformarNota;
	private List listaSelectItemDisciplinas;
	private List listaSelectItemDisciplinasCurso;
	private List listaSelectItemTurma;
	private List listaSelectItemTurmaDisciplina;
	private List listaSelectItemCurso;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemPeriodosLetivos;
	private String semestre;
	private String ano;
	private String campoConsultaDisciplinaMatricula;
	private String valorConsultaDisciplinaMatricula;
	private List listaConsultaDisciplinaMatricula;
	private String campoConsultaDisciplinaTurma;
	private String valorConsultaDisciplinaTurma;
	private List listaConsultaDisciplinaTurma;
	private Boolean apresentarIntegral;
	private List<HistoricoVO> alunosTurma;
	private String tipoNota;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaAluno;
	private String abaSelecionada;
	private Boolean existeRegistroAula;
	private List<SelectItem> listaSelectItemNota1Conceito;
	private List<SelectItem> listaSelectItemNota2Conceito;
	private List<SelectItem> listaSelectItemNota3Conceito;
	private List<SelectItem> listaSelectItemNota4Conceito;
	private List<SelectItem> listaSelectItemNota5Conceito;
	private List<SelectItem> listaSelectItemNota6Conceito;
	private List<SelectItem> listaSelectItemNota7Conceito;
	private List<SelectItem> listaSelectItemNota8Conceito;
	private List<SelectItem> listaSelectItemNota9Conceito;
	private List<SelectItem> listaSelectItemNota10Conceito;
	private List<SelectItem> listaSelectItemNota11Conceito;
	private List<SelectItem> listaSelectItemNota12Conceito;
	private List<SelectItem> listaSelectItemNota13Conceito;
	private List<SelectItem> listaSelectItemNota14Conceito;
	private List<SelectItem> listaSelectItemNota15Conceito;
	private List<SelectItem> listaSelectItemNota16Conceito;
	private List<SelectItem> listaSelectItemNota17Conceito;
	private List<SelectItem> listaSelectItemNota18Conceito;
	private List<SelectItem> listaSelectItemNota19Conceito;
	private List<SelectItem> listaSelectItemNota20Conceito;
	private List<SelectItem> listaSelectItemNota21Conceito;
	private List<SelectItem> listaSelectItemNota22Conceito;
	private List<SelectItem> listaSelectItemNota23Conceito;
	private List<SelectItem> listaSelectItemNota24Conceito;
	private List<SelectItem> listaSelectItemNota25Conceito;
	private List<SelectItem> listaSelectItemNota26Conceito;
	private List<SelectItem> listaSelectItemNota27Conceito;
	private List<SelectItem> listaSelectItemNota28Conceito;
	private List<SelectItem> listaSelectItemNota29Conceito;
	private List<SelectItem> listaSelectItemNota30Conceito;
	private List<SelectItem> listaSelectItemNota31Conceito;
	private List<SelectItem> listaSelectItemNota32Conceito;
	private List<SelectItem> listaSelectItemNota33Conceito;
	private List<SelectItem> listaSelectItemNota34Conceito;
	private List<SelectItem> listaSelectItemNota35Conceito;
	private List<SelectItem> listaSelectItemNota36Conceito;
	private List<SelectItem> listaSelectItemNota37Conceito;
	private List<SelectItem> listaSelectItemNota38Conceito;
	private List<SelectItem> listaSelectItemNota39Conceito;
	private List<SelectItem> listaSelectItemNota40Conceito;
	HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs;
	private Boolean possuiDiversidadeConfiguracaoAcademico;
	private Boolean trazerTodasDisciplinas;
	private List listaSelectItemTipoBimestreNota;
	private String tipoBimestreNota;
	private TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistoricoMatricula;
	private TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistoricoTurma;
	private HistoricoVO historicoAlterarDataRegistro;
	private Boolean trazerAlunosTransferenciaMatriz;	
	private List<SelectItem> listaSelectItemMatrizCurricular;
	private Boolean permiteLancarNotaDisciplinaComposta;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO;
	private FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina;	
	private List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina;
	private TipoNotaConceitoEnum tipoNotaReplicar;
	private TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial;
	private List<HistoricoNotaParcialVO> historicoNotaParcialVOs;
	private HistoricoVO historicoTemporarioVO;
	private List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs;
	private String tipoNotaUsar;
	private String tituloNotaApresentar;
	
	public LancamentoNotaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		incializarDados();
		ocultarNotasTelaInformar();
		setMensagemID("msg_entre_prmconsulta");
		setTipoNota("");
		novo();
	}
	
	@PostConstruct
	public void realizarCarregamentoLancamentoNotaVindoTelaFichaAluno() {
		
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Historico</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTipoNota("");
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		setDisciplina_Erro("");
		setMatricula_Erro("");
		setTurma_Erro("");
		setDisciplinaApresentar(new DisciplinaVO());
		// setAno(Uteis.getAnoDataAtual4Digitos());
		setMensagemCalculoNotas_Erro("");
		setHistoricoVO(new HistoricoVO());
		setHistoricoApresentarVO(new HistoricoVO());
		setCursoApresentar(new CursoVO());
		setTurmaVO(new TurmaVO());
		setListaSelectItemDisciplinas(new ArrayList(0));
		setListaSelectItemDisciplinasCurso(new ArrayList(0));
		setListaSelectItemPeriodosLetivos(new ArrayList(0));
		setListaSelectItemTipoInformarNota(new ArrayList(0));
		setListaSelectItemTurma(new ArrayList(0));
		setListaSelectItemTurmaDisciplina(new ArrayList(0));
		setListaSelectItemCurso(new ArrayList(0));
		setListaSelectItemUnidadeEnsino(new ArrayList(0));
		setAlunosTurma(new ArrayList(0));
		setConfiguracaoAcademicoVO(new ConfiguracaoAcademicoVO());
		inicializarListasSelectItemTodosComboBox();
		ocultarNotasTela();
		ocultarNotasTelaInformar();

		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
	}

	public void inicializarOpcaoNotaConceito() {
		setListaSelectItemNota1Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota1ConceitoVOs()));
		setListaSelectItemNota2Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota2ConceitoVOs()));
		setListaSelectItemNota3Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota3ConceitoVOs()));
		setListaSelectItemNota4Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota4ConceitoVOs()));
		setListaSelectItemNota5Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota5ConceitoVOs()));
		setListaSelectItemNota6Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota6ConceitoVOs()));
		setListaSelectItemNota7Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota7ConceitoVOs()));
		setListaSelectItemNota8Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota8ConceitoVOs()));
		setListaSelectItemNota9Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota9ConceitoVOs()));
		setListaSelectItemNota10Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
		setListaSelectItemNota11Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota11ConceitoVOs()));
		setListaSelectItemNota12Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota12ConceitoVOs()));
		setListaSelectItemNota13Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota13ConceitoVOs()));
		setListaSelectItemNota14Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota14ConceitoVOs()));
		setListaSelectItemNota15Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota15ConceitoVOs()));
		setListaSelectItemNota16Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota16ConceitoVOs()));
		setListaSelectItemNota17Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota17ConceitoVOs()));
		setListaSelectItemNota18Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota18ConceitoVOs()));
		setListaSelectItemNota19Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota19ConceitoVOs()));
		setListaSelectItemNota20Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota20ConceitoVOs()));
		setListaSelectItemNota21Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota21ConceitoVOs()));
		setListaSelectItemNota22Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota22ConceitoVOs()));
		setListaSelectItemNota23Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota23ConceitoVOs()));
		setListaSelectItemNota24Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota24ConceitoVOs()));
		setListaSelectItemNota25Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota25ConceitoVOs()));
		setListaSelectItemNota26Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota26ConceitoVOs()));
		setListaSelectItemNota27Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota27ConceitoVOs()));
		setListaSelectItemNota28Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota28ConceitoVOs()));
		setListaSelectItemNota29Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota29ConceitoVOs()));
		setListaSelectItemNota30Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota30ConceitoVOs()));
		setListaSelectItemNota31Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota31ConceitoVOs()));
		setListaSelectItemNota32Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota32ConceitoVOs()));
		setListaSelectItemNota33Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota33ConceitoVOs()));
		setListaSelectItemNota34Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota34ConceitoVOs()));
		setListaSelectItemNota35Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota35ConceitoVOs()));
		setListaSelectItemNota36Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota36ConceitoVOs()));
		setListaSelectItemNota37Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota37ConceitoVOs()));
		setListaSelectItemNota38Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota38ConceitoVOs()));
		setListaSelectItemNota39Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota39ConceitoVOs()));
		setListaSelectItemNota40Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota40ConceitoVOs()));
	}

	private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
			itens.add(new SelectItem(obj.getCodigo(), obj.getConceitoNota()));
		}
		return itens;

	}
	
	
	public void imprimirMatrizCurricularAluno() {
		try {
			GradeCurricularAlunoRelControle gradeCurricularAlunoRelControle = (GradeCurricularAlunoRelControle) context().getExternalContext().getSessionMap().get(GradeCurricularAlunoRelControle.class.getSimpleName());
			if (gradeCurricularAlunoRelControle == null) {
				gradeCurricularAlunoRelControle = new GradeCurricularAlunoRelControle();
				context().getExternalContext().getSessionMap().put(GradeCurricularAlunoRelControle.class.getSimpleName(), gradeCurricularAlunoRelControle);
			}
			gradeCurricularAlunoRelControle.setMatriculaVO(getHistoricoVO().getMatricula());
			gradeCurricularAlunoRelControle.setLayout("layout2");
			gradeCurricularAlunoRelControle.setVisaoAluno(false);
			gradeCurricularAlunoRelControle.imprimirPDF();
			setFazerDownload(gradeCurricularAlunoRelControle.getFazerDownload());
			setCaminhoRelatorio(gradeCurricularAlunoRelControle.getCaminhoRelatorio());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Historico</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			setOncompleteModal("");
			validarDados();
			Boolean erro = false;
			if (getAbaSelecionada().equals("buscaPorMatricula")) {
				getFacadeFactory().getHistoricoFacade().incluirListaHistorico(getAlunosTurma(), "LancamentoNota", getUsuarioLogado(), "Visão Administrativa", true, getTipoAlteracaoSituacaoHistoricoMatricula());
			} else {
				getFacadeFactory().getHistoricoFacade().incluirListaHistorico(getAlunosTurma(), "LancamentoNota", getUsuarioLogado(), "Visão Administrativa", true, getTipoAlteracaoSituacaoHistoricoTurma());
			}
			if (getConfiguracaoAcademicoVO().getEnviarMensagemNotaAbaixoMedia()) {
				erro = enviarComunicadoInternoEmailNotaAbaixoMedia();
			}
			if (erro) {
				setMensagemID("msg_textoPadraoBancoCurriculum_inexistente", Uteis.ALERTA);
			} else {
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
			setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivo(getAlunosTurma(), getAno(), getSemestre(), getUsuarioLogado()));
			if(!getMatriculaPeriodoVOs().isEmpty()){
				setOncompleteModal("RichFaces.$('panelReprovarPeriodoLetivo').show();");
			}
		} catch (ConsistirException ex) {			
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {						
			setMensagemDetalhada("msg_erro", e.getMessage());			
		}
	}

	private Boolean enviarComunicadoInternoEmailNotaAbaixoMedia() throws Exception {
		List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
		Boolean erro = false;
		for (HistoricoVO histVO : getAlunosTurma()) {
			if (histVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor())) {
				listaComunicadoInternoDestinatarioVO.clear();
				ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
				ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
				comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
				histVO.getMatricula().getAluno().setEmail(getFacadeFactory().getPessoaFacade().consultarEmailCodigo(histVO.getMatricula().getAluno().getCodigo()));
				if (!histVO.getMatricula().getAluno().getEmail().equals("")) {
					comunicadoInternoDestinatarioVO.setEmail(histVO.getMatricula().getAluno().getEmail());
					comunicadoInternoDestinatarioVO.setNome(histVO.getMatricula().getAluno().getNome());
					comunicadoInternoDestinatarioVO.getDestinatario().setNome(histVO.getMatricula().getAluno().getNome());
					comunicadoInternoDestinatarioVO.getDestinatario().setEmail(histVO.getMatricula().getAluno().getEmail());
					comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
				} else {
					comunicacaoInternaVO.setEnviarEmail(Boolean.FALSE);
				}
				comunicadoInternoDestinatarioVO.setDestinatario(histVO.getMatricula().getAluno());
				listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
				comunicacaoInternaVO.setAssunto("Nota abaixo da média");
				comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
				comunicacaoInternaVO.setData(new Date());
				comunicacaoInternaVO.setTipoRemetente("PR");
				PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(getConfiguracaoGeralPadraoSistema().getResponsavelPadraoComunicadoInterno().getCodigo());
				comunicacaoInternaVO.setResponsavel(responsavel);
				comunicacaoInternaVO.setTipoDestinatario("AL");
				comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
				String corpoMensagem = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipoUnica("notaAbaixoMedia", false, "AT", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()).getTexto();
				if (!corpoMensagem.equals("")) {
					corpoMensagem = substituirTags(corpoMensagem, histVO.getMatricula().getAluno());
					comunicacaoInternaVO.setMensagem(corpoMensagem);
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
				} else {
					erro = Boolean.TRUE;
				}
			}
		}
		return erro;
	}

	private String substituirTags(String corpoMensagem, PessoaVO aluno) throws Exception {
		if (corpoMensagem.contains("#Nome_Aluno")) {
			corpoMensagem = corpoMensagem.replaceAll("#Nome_Aluno", aluno.getNome());
		}
		if (corpoMensagem.contains("#Titulo_BancoCurriculum")) {
			corpoMensagem = corpoMensagem.replaceAll("#Titulo_BancoCurriculum", getConfiguracaoGeralPadraoSistema().getTituloTelaBancoCurriculum());
		}
		if (corpoMensagem.contains("<!DOCTYPE")) {
			corpoMensagem = corpoMensagem.replace(corpoMensagem.substring(corpoMensagem.indexOf("<!DOCTYPE"), corpoMensagem.indexOf("<html>")), "");
		}
		return corpoMensagem;
	}

	public void validarDados() throws Exception {
		if (getAbaSelecionada().equals("buscaPorTurma")) {
			if (getTurmaVO().getIdentificadorTurma().equals("")) {
				throw new Exception("O campo TURMA (Busca por Turma) deve ser informado!");
			}
			if (getHistoricoApresentarVO().getDisciplina().getNomeDisciplinaGrade().equals("")) {
				throw new Exception("O campo DISCIPLINA (Busca por Turma) deve ser informado!");
			}
		}
		if (getAbaSelecionada().equals("buscaPorMatricula")) {
			if (getHistoricoVO().getMatricula().getMatricula().equals("")) {
				throw new Exception("O campo MATRÍCULA ALUNO (Busca por Matrícula) deve ser informado!");
			}
			if (getHistoricoVO().getDisciplina().getNomeDisciplinaGrade().equals("")) {
				throw new Exception("O campo DISCIPLINA (Busca por Matrícula) deve ser informado!");
			}
//			if (getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo() == 0) {
//				throw new Exception("O campo TURMA (Busca por Matrícula) deve ser informado!");
//			}
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			limparLista();
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getHistoricoVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getHistoricoVO().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getHistoricoVO().setMatricula(objAluno);
			getHistoricoVO().setDisciplina(null);
			getHistoricoApresentarVO().setMatricula(null);
			getHistoricoApresentarVO().setMatriculaPeriodoTurmaDisciplina(null);
			ano = Uteis.getAnoDataAtual4Digitos();
			setMensagemDetalhada("");
			setListaSelectItemTurma(new ArrayList(0));
			montarListaSelectItemMatrizCurricular();			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.getHistoricoVO().setMatricula(new MatriculaVO());
		}
	}

	public void selecionarDisciplinaMatricula() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaMatriculaItens");
		limparLista();
		getHistoricoVO().setDisciplina(obj);
		setTurmaVO(new TurmaVO());		
		setAno(obj.getAno());
		setSemestre(obj.getSemestre());
		obj = null;
		setValorConsultaDisciplinaMatricula("");
		setCampoConsultaDisciplinaMatricula("");
		getListaConsultaDisciplinaMatricula().clear();
	}

	public void selecionarDisciplinaTurma() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaTurmaItens");
		limparLista();
		getHistoricoApresentarVO().setDisciplina(obj);
//		montarListaTurmaDisciplina();
		setListaConsultaDisciplinaTurma(new ArrayList(0));
		obj = null;
		setValorConsultaDisciplinaTurma("");
		setCampoConsultaDisciplinaTurma("");
		getListaConsultaDisciplinaTurma().clear();
	}

	public void limparDadosDisciplinaMatricula() throws Exception {
		getHistoricoVO().setDisciplina(new DisciplinaVO());
		setListaSelectItemTurma(new ArrayList(0));
		limparAnoSemestre();
		limparLista();
		setMensagemID("msg_entre_prmconsulta");
	}
	
	public void limparAnoSemestre() {
		limparMensagem();
		setAno("");
		setSemestre("");
	}

	public void limparDadosDisciplinaTurma() throws Exception {
		getHistoricoApresentarVO().setDisciplina(new DisciplinaVO());
		setListaSelectItemTurmaDisciplina(new ArrayList(0));
		limparLista();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparDadosAluno() throws Exception {
		setHistoricoVO(new HistoricoVO());
		limparLista();
		getListaSelectItemTurma().clear();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void selecionarAluno() throws Exception {
		limparLista();
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		getHistoricoVO().setMatricula(objCompleto);
		getHistoricoVO().setDisciplina(null);
		getHistoricoApresentarVO().setMatricula(null);
		getHistoricoApresentarVO().setMatriculaPeriodoTurmaDisciplina(null);
		setListaSelectItemTurma(new ArrayList(0));
		ano = Uteis.getAnoDataAtual4Digitos();
		obj = null;
		objCompleto = null;
		setValorConsultaAluno("");
		setCampoConsultaAluno("");
		getListaConsultaAluno().clear();
		montarListaSelectItemMatrizCurricular();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * HistoricoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
		}
	}

	public void consultarDisciplinaMatriculaRich() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplinaMatricula().equals("codigo")) {
				if (getValorConsultaDisciplinaMatricula().equals("")) {
					setValorConsultaDisciplinaMatricula("0");
				}
				if (getValorConsultaDisciplinaMatricula().trim() != null || !getValorConsultaDisciplinaMatricula().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplinaMatricula().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplinaMatricula());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_Matricula_DisciplinaEquivalenteEDisciplinaComposta(new Integer(valorInt), getHistoricoVO().getMatricula().getMatricula(), getHistoricoVO().getMatrizCurricular().getCodigo(), getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());				
			}
			if (getCampoConsultaDisciplinaMatricula().equals("nome")) {						
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_Matricula_DisciplinaEquivalenteEDisciplinaComposta(getValorConsultaDisciplinaMatricula(), getHistoricoVO().getMatricula().getMatricula(), getHistoricoVO().getMatrizCurricular().getCodigo(), 0, getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
			}
			if (getCampoConsultaDisciplinaMatricula().equals("abreviatura")) {						
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura_Matricula_DisciplinaEquivalenteEDisciplinaComposta(getValorConsultaDisciplinaMatricula(), getHistoricoVO().getMatricula().getMatricula(), getHistoricoVO().getMatrizCurricular().getCodigo(), 0, getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
			}
			setListaConsultaDisciplinaMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplinaTurmaRich() {
		try {
			List objs = new ArrayList(0);
			if(!getTrazerTodasDisciplinas()){
				objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorTurmaParaLancamentoNota(getTurmaVO().getCodigo(), getCampoConsultaDisciplinaTurma(), getValorConsultaDisciplinaTurma(), getPermiteLancarNotaDisciplinaComposta(), getUsuarioLogado());
			}else{
			if (getCampoConsultaDisciplinaTurma().equals("codigo")) {
				if (getValorConsultaDisciplinaTurma().equals("")) {
					setValorConsultaDisciplinaTurma("0");
				}
				if (getValorConsultaDisciplinaTurma().trim() != null || !getValorConsultaDisciplinaTurma().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplinaTurma().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplinaTurma());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo_CursoDisciplinaComposta(new Integer(valorInt), getHistoricoApresentarVO().getMatricula().getCurso().getCodigo(), getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaTurma().equals("nome")) {
				Integer periodoTurma = getTurmaVO().getPeridoLetivo().getCodigo();
				if (trazerTodasDisciplinas) {
					periodoTurma = 0;
				}
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome_CursoDisciplinaComposta(getValorConsultaDisciplinaTurma(), getHistoricoApresentarVO().getMatricula().getCurso().getCodigo(), periodoTurma, getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaMatricula().equals("abreviatura")) {
				Integer periodoTurma = getTurmaVO().getPeridoLetivo().getCodigo();
				if (trazerTodasDisciplinas) {
					periodoTurma = 0;
				}
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorAbreviatura_CursoDisciplinaComposta(getValorConsultaDisciplinaTurma(), getHistoricoApresentarVO().getMatricula().getCurso().getCodigo(), periodoTurma, getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
			}
			}
			setListaConsultaDisciplinaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparLista() {
		getAlunosTurma().clear();
//		ocultarNotasTela();
//		ocultarNotasTelaInformar();
	}

	public void atualizarSituacaoMatriculaPeriodo(List<HistoricoVO> listas) throws Exception {
		for (HistoricoVO hist : listas) {
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(hist.getMatriculaPeriodo(), null, getUsuarioLogado());
		}
	}

	public void consultarAlunosPorMatricula() {
		try {
			this.setAlunosTurma(new ArrayList(0));
			if (historicoVO.getMatricula().getMatricula().equals("")) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_matriculaAluno"));
			}
			if (historicoVO.getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_disciplina"));
			}
			if (historicoVO.getMatricula().getCurso().getPeriodicidade().equals("AN") || historicoVO.getMatricula().getCurso().getPeriodicidade().equals("SE")) {
				if (getAno().trim().isEmpty()) {
					throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_ano"));
				}
			}
			if (historicoVO.getMatricula().getCurso().getPeriodicidade().equals("SE")) {
				if (getSemestre().trim().isEmpty()) {
					throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_semestre"));
				}
			}
//			if (historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo().intValue() == 0) {
//				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_turma"));
//			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				Date dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), getHistoricoVO().getDisciplina().getCodigo(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getAno(), historicoVO.getMatriculaPeriodoTurmaDisciplina().getSemestre());
				if (dataPrimeiraAula == null || Uteis.getDataJDBC(dataPrimeiraAula).compareTo(Uteis.getDataJDBC(new Date())) >= 1) {
					if (dataPrimeiraAula != null) {
						throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", Uteis.getData(dataPrimeiraAula)));
					}
					throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", "Data não cadastrada"));
				}
			}

			List<HistoricoVO> lista = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaTurmaSituacaoHistoricoAnoSemestre(historicoVO.getMatricula().getMatricula(), getHistoricoVO().getMatrizCurricular().getCodigo(), historicoVO.getDisciplina().getCodigo(), null, SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), getAno(), getSemestre(), getPermiteLancarNotaDisciplinaComposta(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), null, getUsuarioLogado());
			getFacadeFactory().getHistoricoFacade().carregarDadosProfessorTitularTitulacao(lista);
			if (lista.isEmpty()) {
				throw new Exception("A matrícula do aluno está suspensa ou o aluno já esta aprovado por aproveitamento nesta disciplina ou o aluno não cursa a disciplina no ano e semestre informado.");
			} else {
				atualizarSituacaoMatriculaPeriodo(lista);
			}
			this.setAlunosTurma(lista);
			this.getDisciplinaApresentar().setCodigo(historicoVO.getDisciplina().getCodigo());
			this.getCursoApresentar().setCodigo(historicoVO.getMatricula().getCurso().getCodigo());
			historicoVO.getMatriculaPeriodoTurmaDisciplina().setTurma(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurma());
			historicoVO.getMatriculaPeriodoTurmaDisciplina().setTurmaPratica(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurmaPratica());
			historicoVO.getMatriculaPeriodoTurmaDisciplina().setTurmaTeorica(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica());
			montarConfiguracaoAcademicoLancamentoPorMatricula();			
			setExisteRegistroAula(getFacadeFactory().getHistoricoFacade().realizarVerificacaoEAtualizacaoFrequenciaHistoricoAluno(lista.get(0), getUsuarioLogado()));			
			getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(getAlunosTurma(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurma())){
				getFacadeFactory().getTurmaFacade().carregarDados(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurma(), getUsuarioLogado());
			}
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(lista.get(0).getMatriculaPeriodoTurmaDisciplina().getTurma(), historicoVO.getDisciplina(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			montarListaOpcoesBimestreNota(getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTurmaIntegral() {
		try {
			this.getHistoricoApresentarVO().getMatriculaPeriodoTurmaDisciplina().setTurma(turmaVO);
		} catch (Exception e) {
		}
	}

	public Boolean verificarExistenciaRegistroAula(String matricula, Integer turma, Integer disciplina, Integer professor, String semestre, String ano) throws Exception {
		try {
			return getFacadeFactory().getRegistroAulaFacade().consultarExistenciaRegistroAula(matricula, turma, disciplina, professor, semestre, ano);
		} catch (Exception e) {
			throw e;
		}
	}

	public void consultarAlunosPorTurma() {
		try {
			this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
			if (getTurmaVO().getIdentificadorTurma().equals("")) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_turma"));
			}
			if (historicoApresentarVO.getMatricula().getUnidadeEnsino().getCodigo().intValue() == 0) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_unidadeEnsino"));
			}
//			if (historicoApresentarVO.getMatricula().getCurso().getCodigo().intValue() == 0 && !getTurmaVO().getSubturma()) {
//				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_curso"));
//			}
			if (historicoApresentarVO.getDisciplina().getCodigo().intValue() == 0) {
				throw new Exception(getMensagemInternalizacao("msg_LancamentoNota_disciplina"));
			}
			if (this.getHistoricoApresentarVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getIntegral()) {
				ano = "";
				semestre = "";
			}
			if (this.getHistoricoApresentarVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getAnual()) {
				semestre = "";
			}
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				Date dataPrimeiraAula = getFacadeFactory().getHorarioTurmaFacade().consultarPrimeiroDiaAulaTurmaDisciplina(getTurmaVO().getCodigo(), getHistoricoApresentarVO().getDisciplina().getCodigo(), ano, semestre);
				if (dataPrimeiraAula == null || Uteis.getDataJDBC(dataPrimeiraAula).compareTo(Uteis.getDataJDBC(new Date())) >= 1) {
					if (dataPrimeiraAula != null) {
						throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", Uteis.getData(dataPrimeiraAula)));
					}
					throw new Exception(UteisJSF.internacionalizar("msg_Historico_aulaNaoIniciada").replace("{0}", "Data não cadastrada"));
				}
			}
			List<HistoricoVO> lista = null;
			if (getPossuiDiversidadeConfiguracaoAcademico()) {
				if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
					lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(0, 0, historicoApresentarVO.getDisciplina().getCodigo(), historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma(), ano, semestre, "", "", false, false, this.getConfiguracaoAcademicoVO(), Uteis.NIVELMONTARDADOS_TODOS, getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getTrazerAlunosTransferenciaMatriz(), getPermiteLancarNotaDisciplinaComposta());
					if (lista.isEmpty()) {
						throw new Exception("Não foram encontrados registros com os filtros informados. Verifique se os alunos não estão suspensos ou os alunos já estejam aprovados por aproveitamento nesta disciplina ou não possui alunos cursando a disciplina no ano e semestre informado.");
					}
					montarConfiguracaoAcademicoPorTurma();
				}else{
					throw new Exception("Foi encontrando mais de uma configuração acadêmica, deve ser selecionado a configuração academica que deseja realizar o lançamento de nota para depois buscar os alunos.");					
				}
				this.setAlunosTurma(lista);
			} else {
				lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(0, 0, historicoApresentarVO.getDisciplina().getCodigo(), historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma(), ano, semestre, "", "", false, false, null, Uteis.NIVELMONTARDADOS_TODOS, getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getTrazerAlunosTransferenciaMatriz(), getPermiteLancarNotaDisciplinaComposta());
				if (lista.isEmpty()) {
					throw new Exception("Não foram encontrados registros com os filtros informados. Verifique se os alunos não estão suspensos ou os alunos já estejam aprovados por aproveitamento nesta disciplina ou não possui alunos cursando a disciplina no ano e semestre informado.");
				}
				this.setAlunosTurma(lista);
				realizarVerificacaoDiversidadeConfiguracaoAcademico();
			}
			this.getDisciplinaApresentar().setCodigo(historicoApresentarVO.getDisciplina().getCodigo());
			this.getCursoApresentar().setCodigo(historicoApresentarVO.getMatricula().getCurso().getCodigo());
			// montarConfiguracaoAcademico(false);
			setExisteRegistroAula(verificarExistenciaRegistroAula(null, historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), historicoApresentarVO.getDisciplina().getCodigo(), null, semestre, ano));
			if (getExisteRegistroAula()) {
				preencherFrequenciaAlunos(getAlunosTurma());
			}
			getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(getAlunosTurma(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getHistoricoApresentarVO().getDisciplina(), ano, semestre, this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
			montarListaOpcoesBimestreNota(getConfiguracaoAcademicoVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunosPorTurmaPorConfiguracaoAcademico() {
		List<HistoricoVO> lista;
		try {
			if (!this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				lista = getFacadeFactory().getHistoricoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHist(historicoApresentarVO.getMatricula().getUnidadeEnsino().getCodigo(), 0, historicoApresentarVO.getDisciplina().getCodigo(), historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma(), ano, semestre, "", "", false, false, this.getConfiguracaoAcademicoVO(), Uteis.NIVELMONTARDADOS_TODOS, getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getTrazerAlunosTransferenciaMatriz(), getPermiteLancarNotaDisciplinaComposta());
				this.setAlunosTurma(lista);
				this.getDisciplinaApresentar().setCodigo(historicoApresentarVO.getDisciplina().getCodigo());
				this.getCursoApresentar().setCodigo(historicoApresentarVO.getMatricula().getCurso().getCodigo());
				setExisteRegistroAula(verificarExistenciaRegistroAula(null, historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), historicoApresentarVO.getDisciplina().getCodigo(), null, semestre, ano));
				if (getExisteRegistroAula()) {
					preencherFrequenciaAlunos(getAlunosTurma());
				}
				montarConfiguracaoAcademicoPorTurma();
				getFacadeFactory().getHistoricoFacade().realizarVerificacaoBloqueiNotaDisciplinaComposta(getAlunosTurma(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			} else {
				getAlunosTurma().clear();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			this.setAlunosTurma(new ArrayList<HistoricoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/*
	 * Operação responsável para preencher a porcentagem de frequência dos
	 * alunos de acordo com os registros de aula
	 */
	public void preencherFrequenciaAlunos(List<HistoricoVO> listaHistoricos) {
		try {
			for (HistoricoVO obj : listaHistoricos) {
				getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(obj, getConfiguracaoAcademicoVO(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>HistoricoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getHistoricoFacade().excluir(historicoVO, true, getUsuarioLogado());
			setHistoricoVO(new HistoricoVO());
			ocultarNotasTela();
			setMensagemID("msg_dados_excluidos");
			setTipoNota("");
			return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
		}
	}

	public void montarListaOpcoesNotas(ConfiguracaoAcademicoVO ca) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		if (ca == null) {
			setListaSelectItemTipoInformarNota(lista);
			return;
		}
		if (ca.getCodigo().intValue() != 0) {
			for (int i = 1; i <= 40; i++) {
				Boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarNota" + i);
				Boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "apresentarNota" + i);
				String tituloNotaApresentar = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNotaApresentar" + i);
				String tituloNota = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNota" + i);
				if (utilizarNota && apresentarNota) {
					TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = (TurmaDisciplinaNotaTituloVO) UtilReflexao.invocarMetodoGet(ca, "turmaDisciplinaNotaTitulo"+ i);
					if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO)){
						lista.add(new SelectItem(tituloNota, turmaDisciplinaNotaTituloVO.getTituloNotaApresentar()));
					}else{
						lista.add(new SelectItem(tituloNota, tituloNotaApresentar));
					}
				}
			}
		}
		setListaSelectItemTipoInformarNota(lista);
	}
	
	public void montarListaOpcoesBimestreNota(ConfiguracaoAcademicoVO ca) {
		Map<String, String> mapBimestreNotaVOs = new HashMap<String, String>(0);
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
//		lista.add(new SelectItem("", ""));
		if (ca == null) {
			setListaSelectItemTipoBimestreNota(lista);
			return;
		}
		if (ca.getCodigo().intValue() != 0) {
			for (int i = 1; i <= 40; i++) {
				BimestreEnum bimestreNotaEnum = (BimestreEnum) UtilReflexao.invocarMetodoGet(ca, "bimestreNota" + i);
				
				if (bimestreNotaEnum != null) {
					if (!mapBimestreNotaVOs.containsKey(bimestreNotaEnum.name())) {
						mapBimestreNotaVOs.put(bimestreNotaEnum.name(), bimestreNotaEnum.name());
						lista.add(new SelectItem(bimestreNotaEnum.name(), bimestreNotaEnum.getValorApresentar()));
					} 
				}
			}
		}
		if (!mapBimestreNotaVOs.isEmpty()) {
			setListaSelectItemTipoBimestreNota(lista);
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			if (resultadoConsulta.isEmpty()) {
				setListaSelectItemCurso(new ArrayList(0));
				setListaSelectItemDisciplinasCurso(new ArrayList(0));
				setListaSelectItemTurmaDisciplina(new ArrayList(0));
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() {
		try {
			UnidadeEnsinoVO unidadeEndino = (UnidadeEnsinoVO) getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getHistoricoApresentarVO().getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (getHistoricoApresentarVO().getMatricula().getUnidadeEnsino().getUnidadeEnsinoCursoVOs().isEmpty()) {
				setListaSelectItemCurso(new ArrayList(0));
				setListaSelectItemDisciplinasCurso(new ArrayList(0));
				setListaSelectItemTurmaDisciplina(new ArrayList(0));
			}
			List<SelectItem> objs = new ArrayList<SelectItem>();
			objs.add(new SelectItem(0, ""));
			for (UnidadeEnsinoCursoVO obj : unidadeEndino.getUnidadeEnsinoCursoVOs()) {
				objs.add(new SelectItem(obj.getCurso().getCodigo(), obj.getNomeCursoTurno()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemCurso(objs);
		} catch (Exception ex) {
			setListaSelectItemCurso(new ArrayList<SelectItem>());
			setListaSelectItemTurmaDisciplina(new ArrayList(0));
			getHistoricoVO().setDisciplina(new DisciplinaVO());
			setHistoricoApresentarVO(new HistoricoVO());
			ocultarNotasTela();
			setAlunosTurma(new ArrayList(0));
		}
	}

	public List consultarCursoPorUnidadeEnsino(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemDisciplinas() {
		try {
			montarListaDisciplinaMatricula();
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaDisciplinaMatricula() throws Exception {
		if (getHistoricoVO().getMatricula().getMatricula().equals("")) {
			setListaSelectItemDisciplinas(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarDisciplinaMatricula();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinas(objs);
	}

	public void montarListaTurma() throws Exception {
		try {
			if (getHistoricoVO().getDisciplina().getCodigo().intValue() == 0) {
				setListaSelectItemTurma(new ArrayList(0));
				return;
			}
			List resultadoConsulta = consultarTurmaPorDisciplinaMatricula();
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<DisciplinaVO> consultarDisciplinaMatricula() throws Exception {
		List<DisciplinaVO> objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorMatricula(getHistoricoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public List<TurmaVO> consultarTurmaPorDisciplinaMatricula() throws Exception {
		List<TurmaVO> objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplinaMatricula(getHistoricoVO().getDisciplina().getCodigo(), getHistoricoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public void montarListaTurmaDisciplina() throws Exception {
		if (getHistoricoApresentarVO().getDisciplina().getCodigo().intValue() == 0) {
			setListaSelectItemTurmaDisciplina(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarTurmaPorDisciplina();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TurmaVO obj = (TurmaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemTurmaDisciplina(objs);
	}

	public void montarListaDisciplinaCurso() throws Exception {
		if ((getHistoricoApresentarVO().getMatricula().getCurso().getCodigo().intValue() == 0) && (!historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getTurmaAgrupada())) {
			setListaSelectItemDisciplinasCurso(new ArrayList(0));
			setListaSelectItemTurmaDisciplina(new ArrayList(0));
			getHistoricoVO().setDisciplina(new DisciplinaVO());
			getHistoricoApresentarVO().setDisciplina(new DisciplinaVO());
			getHistoricoApresentarVO().setMatriculaPeriodoTurmaDisciplina(new MatriculaPeriodoTurmaDisciplinaVO());
			setAlunosTurma(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarDisciplinaMatriculaCurso();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinasCurso(objs);
	}

	public List<DisciplinaVO> consultarDisciplinaMatriculaCurso() throws Exception {
		List<DisciplinaVO> objs = null;
		objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorCurso(getHistoricoApresentarVO().getMatricula().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public List<TurmaVO> consultarTurmaPorDisciplina() throws Exception {
		List<TurmaVO> objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorDisciplina(getHistoricoApresentarVO().getDisciplina().getCodigo(), false, getUsuarioLogado());
		return objs;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDisciplinasPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getTurmaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
		return lista;
	}

	public void montarConfiguracaoAcademicoPorTurma() throws Exception {
		this.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado()));
		inicializarOpcaoNotaConceito();
	}

	public void realizarVerificacaoDiversidadeConfiguracaoAcademico() throws Exception {
		getMapConfiguracaoAcademicoVOs().clear();
		for (HistoricoVO historicoVO : getAlunosTurma()) {
			if (historicoVO != null && !historicoVO.getConfiguracaoAcademico().getCodigo().equals(0)) {
				if (!getMapConfiguracaoAcademicoVOs().containsKey(historicoVO.getConfiguracaoAcademico().getCodigo())) {
					getMapConfiguracaoAcademicoVOs().put(historicoVO.getConfiguracaoAcademico().getCodigo(), historicoVO.getConfiguracaoAcademico());
				}
			}
		}
		if (getMapConfiguracaoAcademicoVOs().size() > 1) {
			setPossuiDiversidadeConfiguracaoAcademico(Boolean.TRUE);
			montarComboboxConfiguracaoAcademico();
			getAlunosTurma().clear();
		} else {
			setPossuiDiversidadeConfiguracaoAcademico(Boolean.FALSE);
			if (!getAlunosTurma().isEmpty()) {
				ConfiguracaoAcademicoVO configuracao = getAlunosTurma().get(0).getConfiguracaoAcademico();
				if (configuracao.getCodigo().equals(0)) {
					throw new Exception("Nenhuma configuração acadêmico encontrado!");
				}
				this.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracao.getCodigo(), getUsuarioLogado()));
				inicializarOpcaoNotaConceito();
			}
		}
	}

	private List listaSelectItemConfiguracaoAcademico;

	public void montarComboboxConfiguracaoAcademico() {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		for (ConfiguracaoAcademicoVO configuracao : getMapConfiguracaoAcademicoVOs().values()) {
			objs.add(new SelectItem(configuracao.getCodigo(), configuracao.getNome()));
		}
		setListaSelectItemConfiguracaoAcademico(objs);
	}

	public void montarConfiguracaoAcademicoLancamentoPorMatricula() throws Exception {
		if (!getAlunosTurma().isEmpty()) {
			ConfiguracaoAcademicoVO configuracao = getAlunosTurma().get(0).getConfiguracaoAcademico();
			if (configuracao.getCodigo().equals(0)) {
				throw new Exception("Nenhuma configuração acadêmico encontrado!");
			}
			this.configuracaoAcademicoVO = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracao.getCodigo(), getUsuarioLogado());
			inicializarOpcaoNotaConceito();
		}
	}

	public String getApresentarModalAvisoDiversidadeConfiguracaoAcademico() {
		if (getPossuiDiversidadeConfiguracaoAcademico() && this.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
			return "RichFaces.$('panelAvisoDiversidadeConfiguracaoAcademico').show()";
		}
		return "";
	}

	public void realizarDesativacaoAviso() {
		setPossuiDiversidadeConfiguracaoAcademico(Boolean.FALSE);
	}

	public List apresentarTipoInformarNota(ConfiguracaoAcademicoVO ca) {
		List h = new ArrayList(0);
		if (ca.getUtilizarNota1().booleanValue()) {
			if (ca.getFormulaCalculoNota1().equals("")) {
				h.add(ca.getTituloNota1());
			}
		}
		if (ca.getUtilizarNota2().booleanValue()) {
			if (ca.getFormulaCalculoNota2().equals("")) {
				h.add(ca.getTituloNota2());
			}
		}
		if (ca.getUtilizarNota3().booleanValue()) {
			if (ca.getFormulaCalculoNota3().equals("")) {
				h.add(ca.getTituloNota3());
			}
		}
		if (ca.getUtilizarNota4().booleanValue()) {
			if (ca.getFormulaCalculoNota4().equals("")) {
				h.add(ca.getTituloNota4());
			}
		}
		if (ca.getUtilizarNota5().booleanValue()) {
			if (ca.getFormulaCalculoNota5().equals("")) {
				h.add(ca.getTituloNota5());
			}
		}
		if (ca.getUtilizarNota6().booleanValue()) {
			if (ca.getFormulaCalculoNota6().equals("")) {
				h.add(ca.getTituloNota6());
			}
		}
		if (ca.getUtilizarNota7().booleanValue()) {
			if (ca.getFormulaCalculoNota7().equals("")) {
				h.add(ca.getTituloNota7());
			}
		}
		if (ca.getUtilizarNota8().booleanValue()) {
			if (ca.getFormulaCalculoNota8().equals("")) {
				h.add(ca.getTituloNota8());
			}
		}
		if (ca.getUtilizarNota9().booleanValue()) {
			if (ca.getFormulaCalculoNota9().equals("")) {
				h.add(ca.getTituloNota9());
			}
		}
		if (ca.getUtilizarNota10().booleanValue()) {
			if (ca.getFormulaCalculoNota10().equals("")) {
				h.add(ca.getTituloNota10());
			}
		}
		if (ca.getUtilizarNota11().booleanValue()) {
			if (ca.getFormulaCalculoNota11().equals("")) {
				h.add(ca.getTituloNota11());
			}
		}
		if (ca.getUtilizarNota12().booleanValue()) {
			if (ca.getFormulaCalculoNota12().equals("")) {
				h.add(ca.getTituloNota12());
			}
		}
		if (ca.getUtilizarNota13().booleanValue()) {
			if (ca.getFormulaCalculoNota13().equals("")) {
				h.add(ca.getTituloNota13());
			}
		}
		return h;
	}

	public void incializarDados() {
		ocultarNotasTela();
	}

	public void ocultarNotasTela() {
		configuracaoAcademicoVO = null;
	}

	public void ocultarNotasTelaInformar() {
	}

	public void calcularMedia(ConfiguracaoAcademicoVO ca, HistoricoVO histVO, UsuarioVO usuarioVO, Map<Integer, ConfiguracaoAcademicoVO> mapConf) throws Exception {
		
		boolean resultado = false;
		String situacaoAtual = histVO.getSituacao();
		try {
			getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(histVO, ca, usuarioVO);
			if(!histVO.getHistoricoDisciplinaFazParteComposicao() || (histVO.getHistoricoDisciplinaFazParteComposicao() && !ca.getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal())){
				verificaAlunoReprovadoFalta(histVO, usuarioVO);
			}
			getFacadeFactory().getConfiguracaoAcademicoFacade().prepararVariaveisNotaParaSubstituicaoFormulaNota(ca, histVO, usuarioVO);
			
			if(histVO.getHistoricoDisciplinaComposta() && !Uteis.isAtributoPreenchido(histVO.getHistoricoDisciplinaFilhaComposicaoVOs())) {
				histVO.setHistoricoDisciplinaFilhaComposicaoVOs(getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(histVO.getMatricula().getMatricula(), 0, histVO.getMatriculaPeriodo().getCodigo(), histVO.getMatrizCurricular().getCodigo(), histVO.getGradeDisciplinaVO().getCodigo(), histVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), false, getUsuarioLogado()));					
				if(!mapConf.containsKey(histVO.getConfiguracaoAcademico().getCodigo())){
					mapConf.put(histVO.getConfiguracaoAcademico().getCodigo(), histVO.getConfiguracaoAcademico());
				}
				mapConf.put(histVO.getConfiguracaoAcademico().getCodigo(), histVO.getConfiguracaoAcademico());
				for (HistoricoVO obj : histVO.getHistoricoDisciplinaFilhaComposicaoVOs()) {
					if(!obj.getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
						if (!mapConf.containsKey(obj.getConfiguracaoAcademico().getCodigo())) {			
							mapConf.put(obj.getConfiguracaoAcademico().getCodigo(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(obj.getConfiguracaoAcademico().getCodigo(), usuarioVO));
						}
						obj.setConfiguracaoAcademico(mapConf.get(obj.getConfiguracaoAcademico().getCodigo()));
					}
				}	
			}
			resultado = ca.substituirVariaveisFormulaPorValores(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs(), true);			
			if(!resultado){
				if(!histVO.getSituacao().equals("RF")){
					if(histVO.getMediaFinal() != null ){													
						histVO.setSituacao("RE");						
					}else{						
						histVO.setSituacao("CS");
					}
				}
			}
			if(histVO.getHistoricoDisciplinaFazParteComposicao()){	
				if (getAbaSelecionada().equals("buscaPorMatricula")) {
					resultado = getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs(), getTipoAlteracaoSituacaoHistoricoMatricula(), false, resultado, usuarioVO);
				} else {
					resultado = getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaComposta(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs(), getTipoAlteracaoSituacaoHistoricoTurma(), false, resultado, usuarioVO);
				}												
			}else if(histVO.getHistoricoDisciplinaComposta()){
				if (getAbaSelecionada().equals("buscaPorMatricula")) {
					getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(histVO,  getTipoAlteracaoSituacaoHistoricoMatricula(), false, usuarioVO);
				} else {
					getFacadeFactory().getHistoricoFacade().executarAtualizacaoDisciplinaFilhaComposicaoComBaseDisciplinaCompostaComposta(histVO,  getTipoAlteracaoSituacaoHistoricoTurma(), false, usuarioVO);
				}
			}else {
				histVO.setEmRecuperacao(false);
				histVO.getHistoricoNotaVOs().clear();
				getFacadeFactory().getHistoricoFacade().realizarCriacaoHistoricoNotaVO(histVO, histVO.getHistoricoDisciplinaFilhaComposicaoVOs());
			}
			
			getFacadeFactory().getHistoricoFacade().verificarMatriculaDohistoricoSituacaoTransferidoERealizarAproveitamentoDisciplinaParaCursoTransferido(histVO,usuarioVO);
		} catch (FechamentoPeriodoLetivoException e) {
			histVO.setMediaFinal(null);
			getFacadeFactory().getLogFechamentoFacade().realizarRegistroLogFechamento(histVO.getMatricula().getMatricula());
		}
		if (histVO.getMediaFinal() != null) {
			if (getConfiguracaoAcademicoVO().getUtilizarArredondamentoMediaParaMais()) {
				histVO.setMediaFinal(Uteis.arredondarMultiploDeCincoParaCima(histVO.getMediaFinal()));
			} else if (getConfiguracaoAcademicoVO().getNotasDeCincoEmCincoDecimos() || getConfiguracaoAcademicoVO().getNotasDeCincoEmCincoDecimosApenasMedia()) {
				histVO.setMediaFinal(Math.round(2 * histVO.getMediaFinal()) / 2.0);
			}
			/**
			 * A primeira opção é para as disciplinas que não fazem parte da composição, para elas será alterado a situação dos históricos normalmente.
			 * A outra opção é caso a disciplina seja composta e não esteja configurado para utilizar a situação da mãe também deverá ser alterada a situação normalmente
			 * Autor: Carlos Eugênio
			 */
			if (!histVO.getHistoricoDisciplinaFazParteComposicao() || (histVO.getHistoricoDisciplinaFazParteComposicao() && !ca.getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal())) {
				if ((!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) && (!histVO.getSituacao().equals(SituacaoHistorico.ISENTO.getValor())) && !histVO.getSituacao().equals("")) {
					if (resultado) {
						histVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
					} else {
						histVO.setSituacao(SituacaoHistorico.REPROVADO.getValor());
					}
				}
			/**
			 * Caso a disciplina faça parte de composição e a opção na configuração esteja marcada da situação da filha ser controlada pela situação da mãe,
			 * deverá então realizar o cálculo da mãe para definir então a situação da filha
			 * Autor: Carlos Eugênio	
			 */
			} else if (histVO.getHistoricoDisciplinaFazParteComposicao() && ca.getSituacaoDisciplinaQueFazParteComposicaoControladaDisciplinaPrincipal()) {					
				histVO.setSituacao(histVO.getSituacaoHistoricoDisciplinaComposta());
			}
			if (situacaoAtual.equals(SituacaoHistorico.TRANCAMENTO.getValor()) || situacaoAtual.equals(SituacaoHistorico.ABANDONO_CURSO.getValor()) || situacaoAtual.equals(SituacaoHistorico.CANCELADO.getValor())) {
				histVO.setSituacao(situacaoAtual);
			}
			realizarAtualizacaoSituacaoRegraEstabelecidaComboBoxTipoAlteracaoSituacaoHistorico(histVO, situacaoAtual);
		} else if (!situacaoAtual.equals(SituacaoHistorico.TRANCAMENTO.getValor()) && !situacaoAtual.equals(SituacaoHistorico.ABANDONO_CURSO.getValor()) && !situacaoAtual.equals(SituacaoHistorico.CANCELADO.getValor())) {		
			histVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
		}
		getFacadeFactory().getHistoricoFacade().realizarAlteracaoSituacaoHistoricoReprovadoPeriodoLetivoDeAcordoRegraConfiguracaoAcademica(histVO, situacaoAtual, getAno(), getSemestre(), getUsuarioLogado());
}
	
	public void realizarAtualizacaoSituacaoRegraEstabelecidaComboBoxTipoAlteracaoSituacaoHistorico(HistoricoVO histVO, String situacaoAtual) {
		if (getAbaSelecionada().equals("buscaPorMatricula")) {
			if (getTipoAlteracaoSituacaoHistoricoMatricula().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.APROVADO.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoMatricula().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoMatricula().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoMatricula().equals(TipoAlteracaoSituacaoHistoricoEnum.NENHUM)) {
				histVO.setSituacao(situacaoAtual);
			}
		} else {
			if (getTipoAlteracaoSituacaoHistoricoTurma().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.APROVADO.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoTurma().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoTurma().equals(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA)) {
				if (!histVO.getSituacao().equals(SituacaoHistorico.REPROVADO_FALTA.getValor())) {
					histVO.setSituacao(situacaoAtual);
				}
			} else if (getTipoAlteracaoSituacaoHistoricoTurma().equals(TipoAlteracaoSituacaoHistoricoEnum.NENHUM)) {
				histVO.setSituacao(situacaoAtual);
			}
		}
	}

	public void verificarAprovacaoAlunos() {
		try {
			boolean executarParalelismo = true;
			final UsuarioVO usuarioVO = getUsuarioLogado();
			final Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
			if(executarParalelismo){
				final ConsistirException ex = new ConsistirException();
				ProcessarParalelismo.executar(0, getAlunosTurma().size(), ex, new ProcessarParalelismo.Processo() {					
					@Override
					public void run(int i) {
						HistoricoVO historicoVO = getAlunosTurma().get(i);				
						try {							
							getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVO, usuarioVO);
							verificarAprovacaoAluno(historicoVO, usuarioVO, mapConf);								
						} catch (ConsistirException e) {										
							ex.adicionarListaMensagemErro("Erro ao processar matrícula "+historicoVO.getMatricula().getMatricula()+" - "+e.getMessage()+".");															
						} catch (Exception e) {										
							ex.adicionarListaMensagemErro("Erro ao processar matrícula "+historicoVO.getMatricula().getMatricula()+" - "+e.getMessage()+".");															
						}						
					}
				});
				if(!ex.getListaMensagemErro().isEmpty()){					
					throw ex;
				}
			} else {

				for (HistoricoVO historicoVo : getAlunosTurma()) {
					historicoVo.setConfiguracaoAcademico(getConfiguracaoAcademicoVO());
					historicoVo.setMediaFinal(null);
					historicoVo.setMediaFinalConceito(new ConfiguracaoAcademicoNotaConceitoVO());
					historicoVo.setHistoricoCriterioAvaliacaoAluno(false);
					historicoVo.setCriterioAvaliacao(null);
					getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVo, getUsuarioLogado());
					verificarAprovacaoAluno(historicoVo, getUsuarioLogado(), mapConf);
				}
			}
			setMensagemCalculoNotas_Erro("");
		} catch (Exception e) {
			historicoVO.setSituacao("VS");
			setMensagemCalculoNotas_Erro(e.getMessage());
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void verificarAprovacaoAluno() {
		try {
			HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
			historicoVO.setConfiguracaoAcademico(getConfiguracaoAcademicoVO());
			getFacadeFactory().getHistoricoFacade().verificarNotasLancadas(historicoVO, getUsuarioLogado());
			Map<Integer, ConfiguracaoAcademicoVO> mapConf = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
			verificarAprovacaoAluno(historicoVO, getUsuarioLogado(), mapConf);
			setMensagemCalculoNotas_Erro("");
		} catch (Exception e) {
			historicoVO.setSituacao("VS");
			setMensagemCalculoNotas_Erro(e.getMessage());
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	public void verificarAprovacaoAluno(HistoricoVO historicoVO, UsuarioVO usuarioVO, Map<Integer, ConfiguracaoAcademicoVO> mapConf) throws Exception {
		historicoVO.setConfiguracaoAcademico(getConfiguracaoAcademicoVO());
		historicoVO.setFreguencia(0.0);
		try {
			calcularMedia(getConfiguracaoAcademicoVO(), historicoVO, usuarioVO, mapConf);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getFacadeFactory().getHistoricoFacade().executarGeracaoFaltaPrimeiroSegundoTerceiroQuartoBimestreTotalFaltaFrequenciaHistorico(historicoVO, getConfiguracaoAcademicoVO(), getUsuarioLogado());
			verificaAlunoReprovadoFalta(historicoVO, usuarioVO);
			historicoVO.setSituacao("CS");
			historicoVO.setMediaFinal(null);
		}
	}

	public void atualizarSituacao() {
		if (!historicoVO.getTipoHistorico().equalsIgnoreCase("NO")) {
			historicoVO.setSituacao("AP");
		} else {
			historicoVO.setSituacao("");
		}
	}

	public void verificaAlunoReprovadoFalta(HistoricoVO obj, UsuarioVO usuarioVO) {
		try {
			getFacadeFactory().getHistoricoFacade().verificaAlunoReprovadoFalta(obj, this.getConfiguracaoAcademicoVO(), usuarioVO);
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("abreviatura", "Abreviatura"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("lancamentoNota.xhtml");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplinas();
		montarListaSelectItemUnidadeEnsino();
		montarListaOpcoesNotas(null);
	}

	public boolean getTipoNota1() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota1())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota2() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota2())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota3() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota3())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota4() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota4())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota5() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota5())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota6() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota6())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota7() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota7())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota8() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota8())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota9() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota9())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota10() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota10())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota11() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota11())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota12() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota12())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota13() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota13())) {
				return true;
			} else {
				return false;
			}
		}
	}
	public boolean getTipoNota14() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota14())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota15() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota15())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota16() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota16())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota17() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota17())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota18() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota18())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota19() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota19())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota20() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota20())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota21() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota21())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota22() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota22())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota23() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota23())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota24() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota24())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota25() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota25())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota26() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota26())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota27() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota27())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota28() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota28())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota29() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota29())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota30() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota30())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota31() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota31())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota32() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota32())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota33() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota33())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota34() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota34())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota35() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota35())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota36() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota36())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota37() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota37())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota38() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota38())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota39() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota39())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoNota40() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (tipoNota.equals("")) {
			return true;
		} else {
			if (tipoNota.equals(this.getConfiguracaoAcademicoVO().getTituloNota40())) {
				return true;
			} else {
				return false;
			}
		}
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

	public List getListaSelectSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
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

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	public String getMatricula_Erro() {
		return matricula_Erro;
	}

	public void setMatricula_Erro(String matricula_Erro) {
		this.matricula_Erro = matricula_Erro;
	}

	public String getDisciplina_Erro() {
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
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

	public String getResponsavel_Erro() {
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public String getMensagemCalculoNotas_Erro() {
		return mensagemCalculoNotas_Erro;
	}

	public void setMensagemCalculoNotas_Erro(String mensagemCalculoNotas_Erro) {
		this.mensagemCalculoNotas_Erro = mensagemCalculoNotas_Erro;
	}

	public List<HistoricoVO> getAlunosTurma() {
		return alunosTurma;
	}

	public void setAlunosTurma(List<HistoricoVO> alunosTurma) {
		this.alunosTurma = alunosTurma;
	}

	public TurmaVO getTurmaVO() {
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getTurma_Erro() {
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
	}

	public HistoricoVO getHistoricoApresentarVO() {
		return historicoApresentarVO;
	}

	public void setHistoricoApresentarVO(HistoricoVO historicoApresentarVO) {
		this.historicoApresentarVO = historicoApresentarVO;
	}

	public String getTipoInformarNota() {
		return tipoInformarNota;
	}

	public void setTipoInformarNota(String tipoInformarNota) {
		this.tipoInformarNota = tipoInformarNota;
	}

	public List<SelectItem> getListaSelectItemTipoInformarNota() {
		return listaSelectItemTipoInformarNota;
	}

	public void setListaSelectItemTipoInformarNota(List<SelectItem> listaSelectItemTipoInformarNota) {
		this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
	}

	public List getListaSelectItemDisciplinas() {
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	public List getListaSelectItemPeriodosLetivos() {
		return listaSelectItemPeriodosLetivos;
	}

	public void setListaSelectItemPeriodosLetivos(List listaSelectItemPeriodosLetivos) {
		this.listaSelectItemPeriodosLetivos = listaSelectItemPeriodosLetivos;
	}

	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		historicoVO = null;
		historicoApresentarVO = null;
		turmaVO = null;
		disciplina_Erro = null;
		matricula_Erro = null;
		turma_Erro = null;
		mensagemCalculoNotas_Erro = null;
		responsavel_Erro = null;
		tipoInformarNota = null;
		Uteis.liberarListaMemoria(listaSelectItemTipoInformarNota);
		Uteis.liberarListaMemoria(listaSelectItemDisciplinas);
		Uteis.liberarListaMemoria(listaSelectItemTurma);
		Uteis.liberarListaMemoria(listaSelectItemPeriodosLetivos);
		Uteis.liberarListaMemoria(alunosTurma);
	}

	/**
	 * @return the configuracaoAcademicoVO
	 */
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	/**
	 * @param configuracaoAcademicoVO
	 *            the configuracaoAcademicoVO to set
	 */
	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
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
	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List getListaSelectItemUnidadeEnsino() {
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
	 * @return the listaSelectItemDisciplinasCurso
	 */
	public List getListaSelectItemDisciplinasCurso() {
		return listaSelectItemDisciplinasCurso;
	}

	/**
	 * @param listaSelectItemDisciplinasCurso
	 *            the listaSelectItemDisciplinasCurso to set
	 */
	public void setListaSelectItemDisciplinasCurso(List listaSelectItemDisciplinasCurso) {
		this.listaSelectItemDisciplinasCurso = listaSelectItemDisciplinasCurso;
	}

	/**
	 * @return the listaSelectItemTurmaDisciplina
	 */
	public List getListaSelectItemTurmaDisciplina() {
		return listaSelectItemTurmaDisciplina;
	}

	/**
	 * @param listaSelectItemTurmaDisciplina
	 *            the listaSelectItemTurmaDisciplina to set
	 */
	public void setListaSelectItemTurmaDisciplina(List listaSelectItemTurmaDisciplina) {
		this.listaSelectItemTurmaDisciplina = listaSelectItemTurmaDisciplina;
	}

	/**
	 * @return the disciplinaApresentar
	 */
	public DisciplinaVO getDisciplinaApresentar() {
		return disciplinaApresentar;
	}

	/**
	 * @param disciplinaApresentar
	 *            the disciplinaApresentar to set
	 */
	public void setDisciplinaApresentar(DisciplinaVO disciplinaApresentar) {
		this.disciplinaApresentar = disciplinaApresentar;
	}

	/**
	 * @return the cursoApresentar
	 */
	public CursoVO getCursoApresentar() {
		return cursoApresentar;
	}

	/**
	 * @param cursoApresentar
	 *            the cursoApresentar to set
	 */
	public void setCursoApresentar(CursoVO cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	/**
	 * @return the apresentarIntegral
	 */
	public Boolean getApresentarIntegral() {
		return apresentarIntegral;
	}

	/**
	 * @param apresentarIntegral
	 *            the apresentarIntegral to set
	 */
	public void setApresentarIntegral(Boolean apresentarIntegral) {
		this.apresentarIntegral = apresentarIntegral;
	}

	/**
	 * @return the campoConsultaDisciplinaMatricula
	 */
	public String getCampoConsultaDisciplinaMatricula() {
		if(campoConsultaDisciplinaMatricula == null) {
			campoConsultaDisciplinaMatricula = "nome";
		}
		return campoConsultaDisciplinaMatricula;
	}

	/**
	 * @param campoConsultaDisciplinaMatricula
	 *            the campoConsultaDisciplinaMatricula to set
	 */
	public void setCampoConsultaDisciplinaMatricula(String campoConsultaDisciplinaMatricula) {
		this.campoConsultaDisciplinaMatricula = campoConsultaDisciplinaMatricula;
	}

	/**
	 * @return the valorConsultaDisciplinaMatricula
	 */
	public String getValorConsultaDisciplinaMatricula() {
		if(valorConsultaDisciplinaMatricula == null) {
			valorConsultaDisciplinaMatricula = "";
		}
		return valorConsultaDisciplinaMatricula;
	}

	/**
	 * @param valorConsultaDisciplinaMatricula
	 *            the valorConsultaDisciplinaMatricula to set
	 */
	public void setValorConsultaDisciplinaMatricula(String valorConsultaDisciplinaMatricula) {
		this.valorConsultaDisciplinaMatricula = valorConsultaDisciplinaMatricula;
	}

	/**
	 * @return the listaConsultaDisciplinaMatricula
	 */
	public List getListaConsultaDisciplinaMatricula() {
		if (listaConsultaDisciplinaMatricula == null) {
			listaConsultaDisciplinaMatricula = new ArrayList(0);
		}
		return listaConsultaDisciplinaMatricula;
	}

	/**
	 * @param listaConsultaDisciplinaMatricula
	 *            the listaConsultaDisciplinaMatricula to set
	 */
	public void setListaConsultaDisciplinaMatricula(List listaConsultaDisciplinaMatricula) {
		this.listaConsultaDisciplinaMatricula = listaConsultaDisciplinaMatricula;
	}

	/**
	 * @return the campoConsultaDisciplinaTurma
	 */
	public String getCampoConsultaDisciplinaTurma() {
		return campoConsultaDisciplinaTurma;
	}

	/**
	 * @param campoConsultaDisciplinaTurma
	 *            the campoConsultaDisciplinaTurma to set
	 */
	public void setCampoConsultaDisciplinaTurma(String campoConsultaDisciplinaTurma) {
		this.campoConsultaDisciplinaTurma = campoConsultaDisciplinaTurma;
	}

	/**
	 * @return the valorConsultaDisciplinaTurma
	 */
	public String getValorConsultaDisciplinaTurma() {
		return valorConsultaDisciplinaTurma;
	}

	/**
	 * @param valorConsultaDisciplinaTurma
	 *            the valorConsultaDisciplinaTurma to set
	 */
	public void setValorConsultaDisciplinaTurma(String valorConsultaDisciplinaTurma) {
		this.valorConsultaDisciplinaTurma = valorConsultaDisciplinaTurma;
	}

	/**
	 * @return the listaConsultaDisciplinaTurma
	 */
	public List getListaConsultaDisciplinaTurma() {
		return listaConsultaDisciplinaTurma;
	}

	/**
	 * @param listaConsultaDisciplinaTurma
	 *            the listaConsultaDisciplinaTurma to set
	 */
	public void setListaConsultaDisciplinaTurma(List listaConsultaDisciplinaTurma) {
		this.listaConsultaDisciplinaTurma = listaConsultaDisciplinaTurma;
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			turmaVO = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			consultarTurmaPorChavePrimaria();
			setAlunosTurma(new ArrayList(0));
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			getHistoricoVO().setMatricula(null);
			String campoConsulta = turmaVO.getIdentificadorTurma();
			if (campoConsulta.equalsIgnoreCase("")) {
				throw new Exception("Informe o identificador da turma que deseja consultar.");
			} else {
				turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(turmaVO, campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				// if (turmaVO.getCodigo().equals(0)) {
				// turmaVO =
				// getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(turmaVO,
				// campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(),
				// false,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				// }
//				if (turmaVO.getTurmaAgrupada()) {
//					throw new Exception("Não é possível lançar uma nota na secretaria para uma turma agrupada. Informe uma turma não agrupada (específica) para continuar.");
//				}
				
				historicoApresentarVO.getMatricula().setCurso(turmaVO.getCurso());
				historicoApresentarVO.getMatricula().setUnidadeEnsino(turmaVO.getUnidadeEnsino());
				historicoApresentarVO.getMatricula().setTurno(turmaVO.getTurno());
				historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().setTurma(turmaVO);
				ano = Uteis.getAnoDataAtual4Digitos();
				if(getHistoricoApresentarVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getSemestral()){
					setSemestre(Uteis.getSemestreAtual());
				}else{
					setSemestre("");
				}
				getAlunosTurma().clear();
				historicoApresentarVO.setDisciplina(new DisciplinaVO());
				if(getHistoricoApresentarVO().getMatriculaPeriodoTurmaDisciplina().getTurma().getSemestral()){
					setSemestre(Uteis.getSemestreAtual());
				}else{
					setSemestre("");
				}
				getAlunosTurma().clear();
				// montarListaDisciplinaCurso();
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			turmaVO = new TurmaVO();
			historicoApresentarVO.getMatricula().setCurso(turmaVO.getCurso());
			historicoApresentarVO.getMatricula().setUnidadeEnsino(turmaVO.getUnidadeEnsino());
			historicoApresentarVO.getMatricula().setTurno(turmaVO.getTurno());
			historicoApresentarVO.setDisciplina(new DisciplinaVO());
			historicoApresentarVO.getMatriculaPeriodoTurmaDisciplina().setTurma(turmaVO);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificaNota1Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota1() != null) {
			historicoVO.setNota1Lancada(true);
		}
	}

	public void verificaNota2Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota2() != null) {
			historicoVO.setNota2Lancada(true);
		}
	}

	public void verificaNota3Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota3() != null) {
			historicoVO.setNota3Lancada(true);
		}
	}

	public void verificaNota4Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota4() != null) {
			historicoVO.setNota4Lancada(true);
		}
	}

	public void verificaNota5Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota5() != null) {
			historicoVO.setNota5Lancada(true);
		}
	}

	public void verificaNota6Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota6() != null) {
			historicoVO.setNota6Lancada(true);
		}
	}

	public void verificaNota7Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota7() != null) {
			historicoVO.setNota7Lancada(true);
		}
	}

	public void verificaNota8Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota8() != null) {
			historicoVO.setNota8Lancada(true);
		}
	}

	public void verificaNota9Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota9() != null) {
			historicoVO.setNota9Lancada(true);
		}
	}

	public void verificaNota10Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota10() != null) {
			historicoVO.setNota10Lancada(true);
		}
	}

	public void verificaNota11Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota11() != null) {
			historicoVO.setNota11Lancada(true);
		}
	}

	public void verificaNota12Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota12() != null) {
			historicoVO.setNota12Lancada(true);
		}
	}

	public void verificaNota13Lancada() {
		HistoricoVO historicoVO = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		if (historicoVO.getNota13() != null) {
			historicoVO.setNota13Lancada(true);
		}
	}

	public Boolean getIsNota1Media() {
		if (!getConfiguracaoAcademicoVO().getNota1TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(1)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota2Media() {
		if (!getConfiguracaoAcademicoVO().getNota2TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(2)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota3Media() {
		if (!getConfiguracaoAcademicoVO().getNota3TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(3)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota4Media() {
		if (!getConfiguracaoAcademicoVO().getNota4TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(4)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota5Media() {
		if (!getConfiguracaoAcademicoVO().getNota5TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(5)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota6Media() {
		if (!getConfiguracaoAcademicoVO().getNota6TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(6)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota7Media() {
		if (!getConfiguracaoAcademicoVO().getNota7TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(7)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota8Media() {
		if (!getConfiguracaoAcademicoVO().getNota8TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(8)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota9Media() {
		if (!getConfiguracaoAcademicoVO().getNota9TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(9)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota10Media() {
		if (!getConfiguracaoAcademicoVO().getNota10TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(10)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota11Media() {
		if (!getConfiguracaoAcademicoVO().getNota11TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(11)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota12Media() {
		if (!getConfiguracaoAcademicoVO().getNota12TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(12)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota13Media() {
		if (!getConfiguracaoAcademicoVO().getNota13TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(13)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota14Media() {
		if (!getConfiguracaoAcademicoVO().getNota14TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(14)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota15Media() {
		if (!getConfiguracaoAcademicoVO().getNota15TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(15)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota16Media() {
		if (!getConfiguracaoAcademicoVO().getNota16TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(16)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota17Media() {
		if (!getConfiguracaoAcademicoVO().getNota17TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(17)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota18Media() {
		if (!getConfiguracaoAcademicoVO().getNota18TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(18)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota19Media() {
		if (!getConfiguracaoAcademicoVO().getNota19TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(19)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota20Media() {
		if (!getConfiguracaoAcademicoVO().getNota20TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(20)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota21Media() {
		if (!getConfiguracaoAcademicoVO().getNota21TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(21)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota22Media() {
		if (!getConfiguracaoAcademicoVO().getNota22TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(22)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota23Media() {
		if (!getConfiguracaoAcademicoVO().getNota23TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(23)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota24Media() {
		if (!getConfiguracaoAcademicoVO().getNota24TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(24)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota25Media() {
		if (!getConfiguracaoAcademicoVO().getNota25TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(25)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota26Media() {
		if (!getConfiguracaoAcademicoVO().getNota26TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(26)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota27Media() {
		if (!getConfiguracaoAcademicoVO().getNota27TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(27)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota28Media() {
		if (!getConfiguracaoAcademicoVO().getNota28TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(28)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota29Media() {
		if (!getConfiguracaoAcademicoVO().getNota29TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(29)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota30Media() {
		if (!getConfiguracaoAcademicoVO().getNota30TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(30)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota31Media() {
		if (!getConfiguracaoAcademicoVO().getNota31TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(31)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota32Media() {
		if (!getConfiguracaoAcademicoVO().getNota32TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(32)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota33Media() {
		if (!getConfiguracaoAcademicoVO().getNota33TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(33)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota34Media() {
		if (!getConfiguracaoAcademicoVO().getNota34TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(34)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota35Media() {
		if (!getConfiguracaoAcademicoVO().getNota35TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(35)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota36Media() {
		if (!getConfiguracaoAcademicoVO().getNota36TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(36)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota37Media() {
		if (!getConfiguracaoAcademicoVO().getNota37TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(37)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota38Media() {
		if (!getConfiguracaoAcademicoVO().getNota38TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(38)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota39Media() {
		if (!getConfiguracaoAcademicoVO().getNota39TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(39)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean getIsNota40Media() {
		if (!getConfiguracaoAcademicoVO().getNota40TipoLancamento() || getConfiguracaoAcademicoVO().getBloquearNotaComposta(40)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return the abaSelecionada
	 */
	public String getAbaSelecionada() {
		if (abaSelecionada == null) {
			abaSelecionada = "buscaPorMatricula";
		}
		return abaSelecionada;
	}

	/**
	 * @param abaSelecionada
	 *            the abaSelecionada to set
	 */
	public void setAbaSelecionada(String abaSelecionada) {
		this.abaSelecionada = abaSelecionada;
	}

	public void checarFormatarValoresNota1() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota1() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 1);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota2() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota2() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 2);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota3() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota3() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 3);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota4() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota4() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 4);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota5() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota5() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 5);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota6() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota6() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 6);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota7() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota7() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 7);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota8() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota8() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 8);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota9() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota9() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 9);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota10() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota10() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 10);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota11() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota11() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 11);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota12() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota12() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 12);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota13() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota13() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 13);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota14() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota14() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 14);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota15() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota15() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 15);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota16() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota16() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 16);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota17() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota17() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 17);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota18() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota18() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 18);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota19() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota19() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 19);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota20() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota20() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 20);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota21() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota21() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 21);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota22() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota22() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 22);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota23() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota23() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 23);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota24() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota24() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 24);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota25() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota25() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 25);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota26() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota26() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 26);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota27() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota27() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 27);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota28() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota28() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 28);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota29() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota29() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 29);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota30() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota30() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 30);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}
	
	public void checarFormatarValoresNota31() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota31() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 31);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota32() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota32() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 32);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota33() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota33() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 33);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota34() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota34() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 34);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota35() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota35() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 35);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota36() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota36() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 36);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota37() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota37() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 37);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota38() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota38() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 38);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota39() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota39() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 39);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	public void checarFormatarValoresNota40() {
		HistoricoVO historicoAux = (HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens");
		limparMensagem();
		if (historicoAux.getNota40() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 40);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}


	public String getNomeColunas() {
		StringBuilder nomeColunas = new StringBuilder("colunaMatricula,colunaSituacaoMatricula,colunaNomeAluno,colunaSituacao,colunaFrequencia,colunaMediaFinal");
		if (getConfiguracaoAcademicoVO().getApresentarNota1()) {
			nomeColunas.append(",colunaNota1");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota2()) {
			nomeColunas.append(",colunaNota2");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota3()) {
			nomeColunas.append(",colunaNota3");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota4()) {
			nomeColunas.append(",colunaNota4");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota5()) {
			nomeColunas.append(",colunaNota5");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota6()) {
			nomeColunas.append(",colunaNota6");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota7()) {
			nomeColunas.append(",colunaNota7");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota8()) {
			nomeColunas.append(",colunaNota8");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota9()) {
			nomeColunas.append(",colunaNota9");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota10()) {
			nomeColunas.append(",colunaNota10");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota11()) {
			nomeColunas.append(",colunaNota11");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota12()) {
			nomeColunas.append(",colunaNota12");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota13()) {
			nomeColunas.append(",colunaNota13");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota14()) {
			nomeColunas.append(",colunaNota14");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota15()) {
			nomeColunas.append(",colunaNota15");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota16()) {
			nomeColunas.append(",colunaNota16");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota17()) {
			nomeColunas.append(",colunaNota17");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota18()) {
			nomeColunas.append(",colunaNota18");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota19()) {
			nomeColunas.append(",colunaNota19");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota20()) {
			nomeColunas.append(",colunaNota20");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota21()) {
			nomeColunas.append(",colunaNota21");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota22()) {
			nomeColunas.append(",colunaNota22");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota23()) {
			nomeColunas.append(",colunaNota23");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota24()) {
			nomeColunas.append(",colunaNota24");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota25()) {
			nomeColunas.append(",colunaNota25");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota26()) {
			nomeColunas.append(",colunaNota26");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota27()) {
			nomeColunas.append(",colunaNota27");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota28()) {
			nomeColunas.append(",colunaNota28");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota29()) {
			nomeColunas.append(",colunaNota29");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota30()) {
			nomeColunas.append(",colunaNota30");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota31()) {
			nomeColunas.append(",colunaNota31");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota32()) {
			nomeColunas.append(",colunaNota32");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota33()) {
			nomeColunas.append(",colunaNota33");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota34()) {
			nomeColunas.append(",colunaNota34");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota35()) {
			nomeColunas.append(",colunaNota35");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota36()) {
			nomeColunas.append(",colunaNota36");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota37()) {
			nomeColunas.append(",colunaNota37");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota38()) {
			nomeColunas.append(",colunaNota38");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota39()) {
			nomeColunas.append(",colunaNota39");
		}
		if (getConfiguracaoAcademicoVO().getApresentarNota40()) {
			nomeColunas.append(",colunaNota40");
		}
		return nomeColunas.toString();
	}

	public Boolean getExisteRegistroAula() {
		if (existeRegistroAula == null) {
			existeRegistroAula = false;
		}
		return existeRegistroAula;
	}

	public void setExisteRegistroAula(Boolean existeRegistroAula) {
		this.existeRegistroAula = existeRegistroAula;
	}

	public String getCssExisteRegistroAula() {
		if (getExisteRegistroAula()) {
			return "camposSomenteLeitura";
		}
		return "campos";
	}

	public List<SelectItem> getListaSelectItemNota1Conceito() {
		if (listaSelectItemNota1Conceito == null) {
			listaSelectItemNota1Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota1Conceito;
	}

	public void setListaSelectItemNota1Conceito(List<SelectItem> listaSelectItemNota1Conceito) {
		this.listaSelectItemNota1Conceito = listaSelectItemNota1Conceito;
	}

	public List<SelectItem> getListaSelectItemNota2Conceito() {
		if (listaSelectItemNota2Conceito == null) {
			listaSelectItemNota2Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota2Conceito;
	}

	public void setListaSelectItemNota2Conceito(List<SelectItem> listaSelectItemNota2Conceito) {
		this.listaSelectItemNota2Conceito = listaSelectItemNota2Conceito;
	}

	public List<SelectItem> getListaSelectItemNota3Conceito() {
		if (listaSelectItemNota3Conceito == null) {
			listaSelectItemNota3Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota3Conceito;
	}

	public void setListaSelectItemNota3Conceito(List<SelectItem> listaSelectItemNota3Conceito) {
		this.listaSelectItemNota3Conceito = listaSelectItemNota3Conceito;
	}

	public List<SelectItem> getListaSelectItemNota4Conceito() {
		if (listaSelectItemNota4Conceito == null) {
			listaSelectItemNota4Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota4Conceito;
	}

	public void setListaSelectItemNota4Conceito(List<SelectItem> listaSelectItemNota4Conceito) {
		this.listaSelectItemNota4Conceito = listaSelectItemNota4Conceito;
	}

	public List<SelectItem> getListaSelectItemNota5Conceito() {
		if (listaSelectItemNota5Conceito == null) {
			listaSelectItemNota5Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota5Conceito;
	}

	public void setListaSelectItemNota5Conceito(List<SelectItem> listaSelectItemNota5Conceito) {
		this.listaSelectItemNota5Conceito = listaSelectItemNota5Conceito;
	}

	public List<SelectItem> getListaSelectItemNota6Conceito() {
		if (listaSelectItemNota6Conceito == null) {
			listaSelectItemNota6Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota6Conceito;
	}

	public void setListaSelectItemNota6Conceito(List<SelectItem> listaSelectItemNota6Conceito) {
		this.listaSelectItemNota6Conceito = listaSelectItemNota6Conceito;
	}

	public List<SelectItem> getListaSelectItemNota7Conceito() {
		if (listaSelectItemNota7Conceito == null) {
			listaSelectItemNota7Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota7Conceito;
	}

	public void setListaSelectItemNota7Conceito(List<SelectItem> listaSelectItemNota7Conceito) {
		this.listaSelectItemNota7Conceito = listaSelectItemNota7Conceito;
	}

	public List<SelectItem> getListaSelectItemNota8Conceito() {
		if (listaSelectItemNota8Conceito == null) {
			listaSelectItemNota8Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota8Conceito;
	}

	public void setListaSelectItemNota8Conceito(List<SelectItem> listaSelectItemNota8Conceito) {
		this.listaSelectItemNota8Conceito = listaSelectItemNota8Conceito;
	}

	public List<SelectItem> getListaSelectItemNota9Conceito() {
		if (listaSelectItemNota9Conceito == null) {
			listaSelectItemNota9Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota9Conceito;
	}

	public void setListaSelectItemNota9Conceito(List<SelectItem> listaSelectItemNota9Conceito) {
		this.listaSelectItemNota9Conceito = listaSelectItemNota9Conceito;
	}

	public List<SelectItem> getListaSelectItemNota10Conceito() {
		if (listaSelectItemNota10Conceito == null) {
			listaSelectItemNota10Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota10Conceito;
	}

	public void setListaSelectItemNota10Conceito(List<SelectItem> listaSelectItemNota10Conceito) {
		this.listaSelectItemNota10Conceito = listaSelectItemNota10Conceito;
	}

	public List<SelectItem> getListaSelectItemNota11Conceito() {
		if (listaSelectItemNota11Conceito == null) {
			listaSelectItemNota11Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota11Conceito;
	}

	public void setListaSelectItemNota11Conceito(List<SelectItem> listaSelectItemNota11Conceito) {
		this.listaSelectItemNota11Conceito = listaSelectItemNota11Conceito;
	}

	public List<SelectItem> getListaSelectItemNota12Conceito() {
		if (listaSelectItemNota12Conceito == null) {
			listaSelectItemNota12Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota12Conceito;
	}

	public void setListaSelectItemNota12Conceito(List<SelectItem> listaSelectItemNota12Conceito) {
		this.listaSelectItemNota12Conceito = listaSelectItemNota12Conceito;
	}

	public List<SelectItem> getListaSelectItemNota13Conceito() {
		if (listaSelectItemNota13Conceito == null) {
			listaSelectItemNota13Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota13Conceito;
	}

	public void setListaSelectItemNota13Conceito(List<SelectItem> listaSelectItemNota13Conceito) {
		this.listaSelectItemNota13Conceito = listaSelectItemNota13Conceito;
	}

	public List<SelectItem> getListaSelectItemNota14Conceito() {
		if (listaSelectItemNota14Conceito == null) {
			listaSelectItemNota14Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota14Conceito;
	}

	public void setListaSelectItemNota14Conceito(List<SelectItem> listaSelectItemNota14Conceito) {
		this.listaSelectItemNota14Conceito = listaSelectItemNota14Conceito;
	}

	public List<SelectItem> getListaSelectItemNota15Conceito() {
		if (listaSelectItemNota15Conceito == null) {
			listaSelectItemNota15Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota15Conceito;
	}

	public void setListaSelectItemNota15Conceito(List<SelectItem> listaSelectItemNota15Conceito) {
		this.listaSelectItemNota15Conceito = listaSelectItemNota15Conceito;
	}

	public List<SelectItem> getListaSelectItemNota16Conceito() {
		if (listaSelectItemNota16Conceito == null) {
			listaSelectItemNota16Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota16Conceito;
	}

	public void setListaSelectItemNota16Conceito(List<SelectItem> listaSelectItemNota16Conceito) {
		this.listaSelectItemNota16Conceito = listaSelectItemNota16Conceito;
	}

	public List<SelectItem> getListaSelectItemNota17Conceito() {
		if (listaSelectItemNota17Conceito == null) {
			listaSelectItemNota17Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota17Conceito;
	}

	public void setListaSelectItemNota17Conceito(List<SelectItem> listaSelectItemNota17Conceito) {
		this.listaSelectItemNota17Conceito = listaSelectItemNota17Conceito;
	}

	public List<SelectItem> getListaSelectItemNota18Conceito() {
		if (listaSelectItemNota18Conceito == null) {
			listaSelectItemNota18Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota18Conceito;
	}

	public void setListaSelectItemNota18Conceito(List<SelectItem> listaSelectItemNota18Conceito) {
		this.listaSelectItemNota18Conceito = listaSelectItemNota18Conceito;
	}

	public List<SelectItem> getListaSelectItemNota19Conceito() {
		if (listaSelectItemNota19Conceito == null) {
			listaSelectItemNota19Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota19Conceito;
	}

	public void setListaSelectItemNota19Conceito(List<SelectItem> listaSelectItemNota19Conceito) {
		this.listaSelectItemNota19Conceito = listaSelectItemNota19Conceito;
	}

	public List<SelectItem> getListaSelectItemNota20Conceito() {
		if (listaSelectItemNota20Conceito == null) {
			listaSelectItemNota20Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota20Conceito;
	}

	public void setListaSelectItemNota20Conceito(List<SelectItem> listaSelectItemNota20Conceito) {
		this.listaSelectItemNota20Conceito = listaSelectItemNota20Conceito;
	}

	public List<SelectItem> getListaSelectItemNota21Conceito() {
		if (listaSelectItemNota21Conceito == null) {
			listaSelectItemNota21Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota21Conceito;
	}

	public void setListaSelectItemNota21Conceito(List<SelectItem> listaSelectItemNota21Conceito) {
		this.listaSelectItemNota21Conceito = listaSelectItemNota21Conceito;
	}

	public List<SelectItem> getListaSelectItemNota22Conceito() {
		if (listaSelectItemNota22Conceito == null) {
			listaSelectItemNota22Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota22Conceito;
	}

	public void setListaSelectItemNota22Conceito(List<SelectItem> listaSelectItemNota22Conceito) {
		this.listaSelectItemNota22Conceito = listaSelectItemNota22Conceito;
	}

	public List<SelectItem> getListaSelectItemNota23Conceito() {
		if (listaSelectItemNota23Conceito == null) {
			listaSelectItemNota23Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota23Conceito;
	}

	public void setListaSelectItemNota23Conceito(List<SelectItem> listaSelectItemNota23Conceito) {
		this.listaSelectItemNota23Conceito = listaSelectItemNota23Conceito;
	}

	public List<SelectItem> getListaSelectItemNota24Conceito() {
		if (listaSelectItemNota24Conceito == null) {
			listaSelectItemNota24Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota24Conceito;
	}

	public void setListaSelectItemNota24Conceito(List<SelectItem> listaSelectItemNota24Conceito) {
		this.listaSelectItemNota24Conceito = listaSelectItemNota24Conceito;
	}

	public List<SelectItem> getListaSelectItemNota25Conceito() {
		if (listaSelectItemNota25Conceito == null) {
			listaSelectItemNota25Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota25Conceito;
	}

	public void setListaSelectItemNota25Conceito(List<SelectItem> listaSelectItemNota25Conceito) {
		this.listaSelectItemNota25Conceito = listaSelectItemNota25Conceito;
	}

	public List<SelectItem> getListaSelectItemNota26Conceito() {
		if (listaSelectItemNota26Conceito == null) {
			listaSelectItemNota26Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota26Conceito;
	}

	public void setListaSelectItemNota26Conceito(List<SelectItem> listaSelectItemNota26Conceito) {
		this.listaSelectItemNota26Conceito = listaSelectItemNota26Conceito;
	}

	public List<SelectItem> getListaSelectItemNota27Conceito() {
		if (listaSelectItemNota27Conceito == null) {
			listaSelectItemNota27Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota27Conceito;
	}

	public void setListaSelectItemNota27Conceito(List<SelectItem> listaSelectItemNota27Conceito) {
		this.listaSelectItemNota27Conceito = listaSelectItemNota27Conceito;
	}

	public List<SelectItem> getListaSelectItemNota28Conceito() {
		if (listaSelectItemNota28Conceito == null) {
			listaSelectItemNota28Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota28Conceito;
	}

	public void setListaSelectItemNota28Conceito(List<SelectItem> listaSelectItemNota28Conceito) {
		this.listaSelectItemNota28Conceito = listaSelectItemNota28Conceito;
	}

	public Boolean getApresentarAnoMatricula() {
		return ((this.getHistoricoVO().getMatricula().getCurso().getPeriodicidade().equals("AN") || this.getHistoricoVO().getMatricula().getCurso().getPeriodicidade().equals("SE")) && !this.getHistoricoVO().getDisciplina().getCodigo().equals(0));
	}

	public Boolean getApresentarSemestreMatricula() {
		return (this.getHistoricoVO().getMatricula().getCurso().getPeriodicidade().equals("SE") && !this.getHistoricoVO().getDisciplina().getCodigo().equals(0));
	}

	public Boolean getApresentarTurmaMatricula() {
		return !this.getHistoricoVO().getDisciplina().getCodigo().equals(0);
	}

	public List getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList(0);
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}

	public Boolean getApresentarComboboxConfiguracaoAcademico() {
		return getPossuiDiversidadeConfiguracaoAcademico();
	}

	public Boolean getPossuiDiversidadeConfiguracaoAcademico() {
		if (possuiDiversidadeConfiguracaoAcademico == null) {
			possuiDiversidadeConfiguracaoAcademico = Boolean.FALSE;
		}
		return possuiDiversidadeConfiguracaoAcademico;
	}

	public void setPossuiDiversidadeConfiguracaoAcademico(Boolean possuiDiversidadeConfiguracaoAcademico) {
		this.possuiDiversidadeConfiguracaoAcademico = possuiDiversidadeConfiguracaoAcademico;
	}

	public HashMap<Integer, ConfiguracaoAcademicoVO> getMapConfiguracaoAcademicoVOs() {
		if (mapConfiguracaoAcademicoVOs == null) {
			mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapConfiguracaoAcademicoVOs;
	}

	public void setMapConfiguracaoAcademicoVOs(HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs) {
		this.mapConfiguracaoAcademicoVOs = mapConfiguracaoAcademicoVOs;
	}

	public Boolean getTrazerTodasDisciplinas() {
		if (trazerTodasDisciplinas == null) {
			trazerTodasDisciplinas = Boolean.FALSE;
		}
		return trazerTodasDisciplinas;
	}

	public void setTrazerTodasDisciplinas(Boolean trazerTodasDisciplinas) {
		this.trazerTodasDisciplinas = trazerTodasDisciplinas;
	}

	private List<SelectItem> listaSelectItemTipoHistorico;

	public List<SelectItem> getListaSelectItemTipoHistorico() {

		if (listaSelectItemTipoHistorico == null) {
			listaSelectItemTipoHistorico = new ArrayList<SelectItem>();
			for (TipoHistorico tipo : TipoHistorico.values()) {
				if (tipo.getApresentarLancamentoNota()) {
					listaSelectItemTipoHistorico.add(new SelectItem(tipo.getValor(), tipo.getDescricao()));
				}
			}
		}
		return listaSelectItemTipoHistorico;
	}

	public void setListaSelectItemTipoHistorico(List<SelectItem> listaSelectItemTipoHistorico) {
		this.listaSelectItemTipoHistorico = listaSelectItemTipoHistorico;
	}
	
	public StringBuilder realizarCalculoMediaFinal(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, String ano, String semestre, Integer configuracaoAcademica, String situacoesMatriculaPeriodo, String nivelEducacional) {
		StringBuilder resultado = new StringBuilder("");		
		try {
			if (Uteis.isAtributoPreenchido(configuracaoAcademica)) {
				setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(configuracaoAcademica, getUsuarioLogado()));
			}
			List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarTurmaDisciplinaCalcularMedia(unidadeEnsino, curso, turno, turma, disciplina, ano, semestre, configuracaoAcademica, situacoesMatriculaPeriodo, nivelEducacional);
			if(turmaDisciplinaVOs.isEmpty()){
				resultado.append("NÃO FOI LOCALIZADO NENHUMA TURMA/DISCIPLINA COM OS FILTROS INFORMADOS ");
			}			
			for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
				getAlunosTurma().clear();
				setTurmaVO(new TurmaVO());
				getHistoricoApresentarVO().setDisciplina(new DisciplinaVO());
				getAlunosTurma().clear();
				if (!Uteis.isAtributoPreenchido(configuracaoAcademica)) {
					setConfiguracaoAcademicoVO(new ConfiguracaoAcademicoVO());
				}
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaDisciplinaVO.getTurma(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));				
				consultarTurmaPorChavePrimaria();
				if (getTurmaVO().getCodigo() > 0) {
					getHistoricoApresentarVO().setDisciplina(turmaDisciplinaVO.getDisciplina());
					setAno(ano);
					setSemestre(semestre);
					consultarAlunosPorTurma();	
					if(!getAlunosTurma().isEmpty()){
					verificarAprovacaoAlunos();
					gravar();
					if (getMensagemDetalhada().trim().isEmpty()) {
						resultado.append("EXECUTOU TURMA: ").append(getTurmaVO().getIdentificadorTurma()).append(" DISICIPLINA: ").append(getHistoricoApresentarVO().getDisciplina().getCodigo()).append(" - ").append(getHistoricoApresentarVO().getDisciplina().getNome()).append("\n");
					} else {
						resultado.append("ERRO TURMA: ").append(getTurmaVO().getIdentificadorTurma()).append(" DISICIPLINA: ").append(getHistoricoApresentarVO().getDisciplina().getCodigo()).append(" - ").append(getHistoricoApresentarVO().getDisciplina().getNome()).append(" (").append(getMensagemDetalhada()).append(") ").append("\n");
					}					
					}else{
						resultado.append("ERRO TURMA: ").append(getTurmaVO().getIdentificadorTurma()).append(" DISICIPLINA: ").append(getHistoricoApresentarVO().getDisciplina().getCodigo()).append(" - ").append(getHistoricoApresentarVO().getDisciplina().getNome()).append(" NENHUM ALUNO ENCONTRADO. ").append("\n");
					}
				}
			}
		} catch (Exception e) {
			resultado.append("ERRO: ").append(e.getMessage());
		}								
		return resultado;
	}
	
	public Boolean getNota1AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota1();
		}
		return getTipoNota1();
	}

	public Boolean getNota2AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota2();
		}
		return getTipoNota2();
	}

	public Boolean getNota3AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota3();
		}
		return getTipoNota3();
	}

	public Boolean getNota4AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota4();
		}
		return getTipoNota4();
	}

	public Boolean getNota5AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota5();
		}
		return getTipoNota5();
	}

	public Boolean getNota6AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota6();
		}
		return getTipoNota6();
	}

	public Boolean getNota7AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota7();
		}
		return getTipoNota7();
	}

	public Boolean getNota8AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota8();
		}
		return getTipoNota8();
	}

	public Boolean getNota9AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota9();
		}
		return getTipoNota9();
	}

	public Boolean getNota10AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota10();
		}
		return getTipoNota10();
	}

	public Boolean getNota11AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota11();
		}
		return getTipoNota11();
	}

	public Boolean getNota12AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota12();
		}
		return getTipoNota12();
	}

	public Boolean getNota13AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota13();
		}
		return getTipoNota13();
	}

	public Boolean getNota14AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota14();
		}
		return getTipoNota14();
	}

	public Boolean getNota15AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota15();
		}
		return getTipoNota15();
	}

	public Boolean getNota16AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota16();
		}
		return getTipoNota16();
	}

	public Boolean getNota17AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota17();
		}
		return getTipoNota17();
	}

	public Boolean getNota18AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota18();
		}
		return getTipoNota18();
	}

	public Boolean getNota19AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota19();
		}
		return getTipoNota19();
	}

	public Boolean getNota20AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota20();
		}
		return getTipoNota20();
	}

	public Boolean getNota21AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota21();
		}
		return getTipoNota21();
	}

	public Boolean getNota22AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota22();
		}
		return getTipoNota22();
	}

	public Boolean getNota23AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota23();
		}
		return getTipoNota23();
	}

	public Boolean getNota24AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota24();
		}
		return getTipoNota24();
	}

	public Boolean getNota25AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota25();
		}
		return getTipoNota25();
	}

	public Boolean getNota26AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota26();
		}
		return getTipoNota26();
	}

	public Boolean getNota27AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota27();
		}
		return getTipoNota27();
	}
	
	public Boolean getNota28AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota28();
		}
		return getTipoNota28();
	}
	
	public Boolean getNota29AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota29();
		}
		return getTipoNota29();
	}
	
	public Boolean getNota30AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota30();
		}
		return getTipoNota30();
	}
	
	public Boolean getNota31AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota31();
		}
		return getTipoNota31();
	}

	public Boolean getNota32AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota32();
		}
		return getTipoNota32();
	}

	public Boolean getNota33AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota33();
		}
		return getTipoNota33();
	}

	public Boolean getNota34AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota34();
		}
		return getTipoNota34();
	}

	public Boolean getNota35AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota35();
		}
		return getTipoNota35();
	}

	public Boolean getNota36AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota36();
		}
		return getTipoNota36();
	}

	public Boolean getNota37AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota37();
		}
		return getTipoNota37();
	}

	public Boolean getNota38AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota38();
		}
		return getTipoNota38();
	}

	public Boolean getNota39AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota39();
		}
		return getTipoNota39();
	}

	public Boolean getNota40AptoApresentar() {
		if (!getTipoBimestreNota().equals("")) {
			return getTipoBimestreNota40();
		}
		return getTipoNota40();
	}


	public List getListaSelectItemTipoBimestreNota() {
		if (listaSelectItemTipoBimestreNota == null) {
			listaSelectItemTipoBimestreNota = new ArrayList(0);
		} 
		return listaSelectItemTipoBimestreNota;
	}

	public void setListaSelectItemTipoBimestreNota(List listaSelectItemTipoBimestreNota) {
		this.listaSelectItemTipoBimestreNota = listaSelectItemTipoBimestreNota;
	}

	public String getTipoBimestreNota() {
		if (tipoBimestreNota == null) {
			tipoBimestreNota = "";
		}
		return tipoBimestreNota;
	}

	public void setTipoBimestreNota(String tipoBimestreNota) {
		this.tipoBimestreNota = tipoBimestreNota;
	}
	
	public boolean getTipoBimestreNota1() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota1().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota2() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota2().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota3() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota3().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota4() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota4().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota5() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota5().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota6() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota6().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota7() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota7().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota8() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota8().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota9() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota9().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota10() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota10().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota11() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota11().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota12() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota12().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota13() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota13().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota14() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota14().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota15() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota15().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota16() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota16().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota17() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota17().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota18() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota18().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota19() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota19().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota20() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota20().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota21() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota21().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota22() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota22().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota23() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota23().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota24() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota24().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota25() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota25().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota26() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota26().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota27() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota27().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota28() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota28().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota29() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota29().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoBimestreNota30() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota30().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota31() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota31().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota32() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota32().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota33() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota33().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota34() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota34().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota35() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota35().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota36() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota36().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota37() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota37().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota38() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota38().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota39() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota39().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean getTipoBimestreNota40() {
		if (getTipoBimestreNota() == null) {
			setTipoBimestreNota("");
		}
		if (getTipoBimestreNota().equals("")) {
			return true;
		} else {
			if (getTipoBimestreNota().equals(this.getConfiguracaoAcademicoVO().getBimestreNota40().getValor())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public Boolean getApresentarOpcaoTipoBimestreNota() {
		return !getAlunosTurma().isEmpty() && !getListaSelectItemTipoBimestreNota().isEmpty();
	}

	public void limparDadosTipoNota() {
		setTipoNota("");
	}
	
	public void limparDadosTipoBimestreNota() {
		setTipoBimestreNota("");
	}

private List<SelectItem> listaSelectItemTipoAlteracaoSituacaoHistorico;
	
	public List<SelectItem> getListaSelectItemTipoAlteracaoSituacaoHistorico() {
		if (listaSelectItemTipoAlteracaoSituacaoHistorico == null) {
			listaSelectItemTipoAlteracaoSituacaoHistorico = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, "Todos Históricos"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_APROVADOS, "Apenas Aprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS, "Apenas Reprovados"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.APENAS_REPROVADOS_POR_FALTA, "Apenas Reprovados Por Falta"));
			listaSelectItemTipoAlteracaoSituacaoHistorico.add(new SelectItem(TipoAlteracaoSituacaoHistoricoEnum.NENHUM, "Nenhum"));

		}
		return listaSelectItemTipoAlteracaoSituacaoHistorico;
	}

	public TipoAlteracaoSituacaoHistoricoEnum getTipoAlteracaoSituacaoHistoricoMatricula() {
		if (tipoAlteracaoSituacaoHistoricoMatricula == null) {
			tipoAlteracaoSituacaoHistoricoMatricula = TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS;
		}
		return tipoAlteracaoSituacaoHistoricoMatricula;
	}

	public void setTipoAlteracaoSituacaoHistoricoMatricula(TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistoricoMatricula) {
		this.tipoAlteracaoSituacaoHistoricoMatricula = tipoAlteracaoSituacaoHistoricoMatricula;
	}

	public TipoAlteracaoSituacaoHistoricoEnum getTipoAlteracaoSituacaoHistoricoTurma() {
		if (tipoAlteracaoSituacaoHistoricoTurma == null) {
			tipoAlteracaoSituacaoHistoricoTurma = TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS;
		}
		return tipoAlteracaoSituacaoHistoricoTurma;
	}

	public void setTipoAlteracaoSituacaoHistoricoTurma(TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistoricoTurma) {
		this.tipoAlteracaoSituacaoHistoricoTurma = tipoAlteracaoSituacaoHistoricoTurma;
	}
	
	public Boolean getApresentarSituacaoAplicandoRegraConfiguracaoAcademica() {
		if (getAlunosTurma() != null && !getAlunosTurma().isEmpty() && !getAlunosTurma().get(0).getHistoricoDisciplinaFazParteComposicao()) {
			return true;
		}
		if (getAlunosTurma() != null && !getAlunosTurma().isEmpty() && getAlunosTurma().get(0).getHistoricoDisciplinaFazParteComposicao() && !getConfiguracaoAcademicoVO().getOcultarSituacaoHistoricoDisciplinaQueFazParteComposicao()) {
			return true;
		}
		return false;
	}
	
	public boolean getApresentarDataRegistro() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNota_AlterarDataRegistro", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void alterarDataRegistroHistorico() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNota_AlterarDataRegistro", getUsuarioLogado());
			getFacadeFactory().getHistoricoFacade().alterarDataRegistroHistorico(getHistoricoAlterarDataRegistro().getCodigo(), getHistoricoAlterarDataRegistro().getDataRegistro(), getHistoricoAlterarDataRegistro().getDataInicioAula(), getHistoricoAlterarDataRegistro().getDataFimAula(), getUsuarioLogado());
			setMensagemID("msg_data_registro_alterada");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public HistoricoVO getHistoricoAlterarDataRegistro() {
		if (historicoAlterarDataRegistro == null) {
			historicoAlterarDataRegistro = new HistoricoVO();
		}
		return historicoAlterarDataRegistro;
	}

	public void setHistoricoAlterarDataRegistro(
			HistoricoVO historicoAlterarDataRegistro) {
		this.historicoAlterarDataRegistro = historicoAlterarDataRegistro;
	}
	
	public List<SelectItem> getListaSelectItemNota29Conceito() {
		if (listaSelectItemNota29Conceito == null) {
			listaSelectItemNota29Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota29Conceito;
	}

	public void setListaSelectItemNota29Conceito(List<SelectItem> listaSelectItemNota29Conceito) {
		this.listaSelectItemNota29Conceito = listaSelectItemNota29Conceito;
	}

	public List<SelectItem> getListaSelectItemNota30Conceito() {
		if (listaSelectItemNota30Conceito == null) {
			listaSelectItemNota30Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota30Conceito;
	}

	public void setListaSelectItemNota30Conceito(List<SelectItem> listaSelectItemNota30Conceito) {
		this.listaSelectItemNota30Conceito = listaSelectItemNota30Conceito;
	}

	public List<SelectItem> getListaSelectItemNota31Conceito() {
		if (listaSelectItemNota31Conceito == null) {
			listaSelectItemNota31Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota31Conceito;
	}

	public void setListaSelectItemNota31Conceito(List<SelectItem> listaSelectItemNota31Conceito) {
		this.listaSelectItemNota31Conceito = listaSelectItemNota31Conceito;
	}

	public List<SelectItem> getListaSelectItemNota32Conceito() {
		if (listaSelectItemNota32Conceito == null) {
			listaSelectItemNota32Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota32Conceito;
	}

	public void setListaSelectItemNota32Conceito(List<SelectItem> listaSelectItemNota32Conceito) {
		this.listaSelectItemNota32Conceito = listaSelectItemNota32Conceito;
	}

	public List<SelectItem> getListaSelectItemNota33Conceito() {
		if (listaSelectItemNota33Conceito == null) {
			listaSelectItemNota33Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota33Conceito;
	}

	public void setListaSelectItemNota33Conceito(List<SelectItem> listaSelectItemNota33Conceito) {
		this.listaSelectItemNota33Conceito = listaSelectItemNota33Conceito;
	}

	public List<SelectItem> getListaSelectItemNota34Conceito() {
		if (listaSelectItemNota34Conceito == null) {
			listaSelectItemNota34Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota34Conceito;
	}

	public void setListaSelectItemNota34Conceito(List<SelectItem> listaSelectItemNota34Conceito) {
		this.listaSelectItemNota34Conceito = listaSelectItemNota34Conceito;
	}

	public List<SelectItem> getListaSelectItemNota35Conceito() {
		if (listaSelectItemNota35Conceito == null) {
			listaSelectItemNota35Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota35Conceito;
	}

	public void setListaSelectItemNota35Conceito(List<SelectItem> listaSelectItemNota35Conceito) {
		this.listaSelectItemNota35Conceito = listaSelectItemNota35Conceito;
	}

	public List<SelectItem> getListaSelectItemNota36Conceito() {
		if (listaSelectItemNota36Conceito == null) {
			listaSelectItemNota36Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota36Conceito;
	}

	public void setListaSelectItemNota36Conceito(List<SelectItem> listaSelectItemNota36Conceito) {
		this.listaSelectItemNota36Conceito = listaSelectItemNota36Conceito;
	}

	public List<SelectItem> getListaSelectItemNota37Conceito() {
		if (listaSelectItemNota37Conceito == null) {
			listaSelectItemNota37Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota37Conceito;
	}

	public void setListaSelectItemNota37Conceito(List<SelectItem> listaSelectItemNota37Conceito) {
		this.listaSelectItemNota37Conceito = listaSelectItemNota37Conceito;
	}

	public List<SelectItem> getListaSelectItemNota38Conceito() {
		if (listaSelectItemNota38Conceito == null) {
			listaSelectItemNota38Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota38Conceito;
	}

	public void setListaSelectItemNota38Conceito(List<SelectItem> listaSelectItemNota38Conceito) {
		this.listaSelectItemNota38Conceito = listaSelectItemNota38Conceito;
	}

	public List<SelectItem> getListaSelectItemNota39Conceito() {
		if (listaSelectItemNota39Conceito == null) {
			listaSelectItemNota39Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota39Conceito;
	}

	public void setListaSelectItemNota39Conceito(List<SelectItem> listaSelectItemNota39Conceito) {
		this.listaSelectItemNota39Conceito = listaSelectItemNota39Conceito;
	}

	public List<SelectItem> getListaSelectItemNota40Conceito() {
		if (listaSelectItemNota40Conceito == null) {
			listaSelectItemNota40Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota40Conceito;
	}

	public void setListaSelectItemNota40Conceito(List<SelectItem> listaSelectItemNota40Conceito) {
		this.listaSelectItemNota40Conceito = listaSelectItemNota40Conceito;
	}

	public void setListaSelectItemTipoAlteracaoSituacaoHistorico(List<SelectItem> listaSelectItemTipoAlteracaoSituacaoHistorico) {
		this.listaSelectItemTipoAlteracaoSituacaoHistorico = listaSelectItemTipoAlteracaoSituacaoHistorico;
	}
	
	public Boolean getTrazerAlunosTransferenciaMatriz() {
		if(trazerAlunosTransferenciaMatriz == null){
			trazerAlunosTransferenciaMatriz = false;
		}
		return trazerAlunosTransferenciaMatriz;
	}

	public void setTrazerAlunosTransferenciaMatriz(Boolean trazerAlunosTransferenciaMatriz) {
		this.trazerAlunosTransferenciaMatriz = trazerAlunosTransferenciaMatriz;
	}

	public List<SelectItem> getListaSelectItemMatrizCurricular() {
		if(listaSelectItemMatrizCurricular == null){
			listaSelectItemMatrizCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMatrizCurricular;
	}

	public void setListaSelectItemMatrizCurricular(List<SelectItem> listaSelectItemMatrizCurricular) {
		this.listaSelectItemMatrizCurricular = listaSelectItemMatrizCurricular;
	}
	
	public void montarListaSelectItemMatrizCurricular(){
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarPorMatriculaGradeCurricularVOsVinculadaHistoricoInclusaoExclusaoDisciplina(getHistoricoVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
			getListaSelectItemMatrizCurricular().clear();
			boolean existeGradeAtual = false;
			for (GradeCurricularVO grade : gradeCurricularVOs) {
				getListaSelectItemMatrizCurricular().add(new SelectItem(grade.getCodigo(), grade.getNome()));
				if (grade.getCodigo().equals(getHistoricoVO().getMatricula().getGradeCurricularAtual().getCodigo())) {
					existeGradeAtual = true;
				}
			}
			if (!existeGradeAtual && Uteis.isAtributoPreenchido(getHistoricoVO().getMatricula().getGradeCurricularAtual())) {
				getHistoricoVO().getMatricula().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getHistoricoVO().getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getListaSelectItemMatrizCurricular().add(new SelectItem(getHistoricoVO().getMatricula().getGradeCurricularAtual().getCodigo(), getHistoricoVO().getMatricula().getGradeCurricularAtual().getNome()));
			}
			getHistoricoVO().getMatrizCurricular().setCodigo(getHistoricoVO().getMatricula().getGradeCurricularAtual().getCodigo());
		
		} catch (Exception e) {
			
		}	
	}
	

	public boolean getApresentarFrequenciaGeral(){
		return (Uteis.isAtributoPreenchido(getAlunosTurma()) 
				&& (Uteis.isAtributoPreenchido(getTurmaVO()) && (!getTurmaVO().getSubturma() || getTurmaVO().getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)))
				|| (Uteis.isAtributoPreenchido(getHistoricoVO().getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(getHistoricoVO().getDisciplina().getCodigo())
						&& !Uteis.isAtributoPreenchido(getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo())
						&& !Uteis.isAtributoPreenchido(getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo()))
				);
	}
	
	public boolean getApresentarFrequenciaPratica(){
		return (Uteis.isAtributoPreenchido(getAlunosTurma()) 
				&& (Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSubturma() && getTurmaVO().getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))
				|| (Uteis.isAtributoPreenchido(getHistoricoVO().getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(getHistoricoVO().getDisciplina().getCodigo())
						&& Uteis.isAtributoPreenchido(getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo()))
				);
	}
	
	public boolean getApresentarFrequenciaTeorica(){
		return (Uteis.isAtributoPreenchido(getAlunosTurma()) 
				&& (Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSubturma() && getTurmaVO().getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA))
				|| (Uteis.isAtributoPreenchido(getHistoricoVO().getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(getHistoricoVO().getDisciplina().getCodigo())
						&& Uteis.isAtributoPreenchido(getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo()))
				);
	}
	
	public Boolean getPermiteLancarNotaDisciplinaComposta() {
		if(permiteLancarNotaDisciplinaComposta == null){
			try {
				permiteLancarNotaDisciplinaComposta = Historico.verificarPermissaoFuncionalidadeUsuario("PermiteLancarNotaDisciplinaComposta", getUsuarioLogado());
			} catch (Exception e) {
				permiteLancarNotaDisciplinaComposta = false;
			}
		}
		return permiteLancarNotaDisciplinaComposta;
	}

	public void setPermiteLancarNotaDisciplinaComposta(Boolean permiteLancarNotaDisciplinaComposta) {
		this.permiteLancarNotaDisciplinaComposta = permiteLancarNotaDisciplinaComposta;
	}
	
	@PostConstruct
	public void realizarInicializacaoDadosNavegacaoPagina(){
		if (context().getExternalContext().getSessionMap().containsKey("lancamentoNota.turma")) {
			try{
				getTurmaVO().setIdentificadorTurma((String) context().getExternalContext().getSessionMap().get("lancamentoNota.turma"));
				consultarTurmaPorChavePrimaria();
				setAlunosTurma(new ArrayList(0));
				context().getExternalContext().getSessionMap().remove("lancamentoNota.turma");
				setAno((String) context().getExternalContext().getSessionMap().get("lancamentoNota.ano"));
				context().getExternalContext().getSessionMap().remove("lancamentoNota.ano");
				setSemestre((String) context().getExternalContext().getSessionMap().get("lancamentoNota.semestre"));
				context().getExternalContext().getSessionMap().remove("lancamentoNota.semestre");
				setAbaSelecionada("buscaPorTurma");
				if (context().getExternalContext().getSessionMap().containsKey("lancamentoNota.disciplina")) {
					getHistoricoApresentarVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(((Integer)context().getExternalContext().getSessionMap().get("lancamentoNota.disciplina")), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					consultarAlunosPorTurma();
				}
			}catch(Exception e){
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}else if (context().getExternalContext().getSessionMap().get("historicoFichaAluno") != null) {
			HistoricoVO historicoFichaAlunoVO = (HistoricoVO) context().getExternalContext().getSessionMap().get("historicoFichaAluno");
		if (historicoFichaAlunoVO != null && !historicoFichaAlunoVO.getCodigo().equals(0) ) {
			try {
				novo();
				setHistoricoVO((HistoricoVO) historicoFichaAlunoVO.clone());
				DisciplinaVO disciplinaTemporariaVO = historicoFichaAlunoVO.getDisciplina().clone();
				consultarAlunoPorMatricula();
				limparLista();
				getHistoricoVO().setDisciplina(disciplinaTemporariaVO);
				setTurmaVO(new TurmaVO());		
				setAno(historicoFichaAlunoVO.getAnoHistorico());
				setSemestre(historicoFichaAlunoVO.getSemestreHistorico());
				
				setValorConsultaDisciplinaMatricula("");
				setCampoConsultaDisciplinaMatricula("");
				getListaConsultaDisciplinaMatricula().clear();
				
				consultarAlunosPorMatricula();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				context().getExternalContext().getSessionMap().remove("historicoFichaAluno");
			}
			
		}
		}
	}
	
	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if(matriculaPeriodoVOs == null){
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public void realizarReprovacaoPeriodoLetivo(){
		try {
			setOncompleteModal("");
			getFacadeFactory().getHistoricoFacade().realizarAlteracaoHistoricoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(getMatriculaPeriodoVOs(), getAno(), getSemestre(), getUsuarioLogado());
			for(MatriculaPeriodoVO matriculaPeriodoVO: getMatriculaPeriodoVOs()){
				for(HistoricoVO historicoVO:getAlunosTurma()){
					if(historicoVO.getMatriculaPeriodo().getCodigo().equals(matriculaPeriodoVO.getCodigo())){
						if(!historicoVO.getReprovado()){
							historicoVO.setSituacao(SituacaoHistorico.REPROVADO_PERIODO_LETIVO.getValor());
						}
						break;
					}
				}
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTituloVO() {
		if(turmaDisciplinaNotaTituloVO == null){
			turmaDisciplinaNotaTituloVO =  new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTituloVO;
	}

	public void setTurmaDisciplinaNotaTituloVO(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		this.turmaDisciplinaNotaTituloVO = turmaDisciplinaNotaTituloVO;
	}

    public void gravarTituloNota(){
    	try {
    		if(getTurmaDisciplinaNotaTituloVO().getPossuiFormula() && !Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO().getFormula())) {
    			setOncompleteModal("");
    			throw new Exception("O campo Fórmula Cálculo deve ser informado!");
    		}
    		if(Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO())){
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().alterar(getTurmaDisciplinaNotaTituloVO(), getAlunosTurma(), getTurmaVO(), getUsuarioLogado(), getFiltroRelatorioAcademicoVO(), getTrazerAlunosTransferenciaMatriz(), getPermiteLancarNotaDisciplinaComposta());
    		}else{
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().incluir(getTurmaDisciplinaNotaTituloVO(), getAlunosTurma(), getTurmaVO(), getUsuarioLogado(), getFiltroRelatorioAcademicoVO(), getTrazerAlunosTransferenciaMatriz(), getPermiteLancarNotaDisciplinaComposta());
    		}
    		getTurmaDisciplinaNotaTituloVO().setTituloOriginal(getTurmaDisciplinaNotaTituloVO().getTitulo());
    		for(SelectItem item: getListaSelectItemTipoInformarNota()){
    			if(item.getValue().equals(getTurmaDisciplinaNotaTituloVO().getVariavelConfiguracao())){
    				item.setLabel(getTurmaDisciplinaNotaTituloVO().getTituloNotaApresentar());
    				break;
    			}
    		}
    		setOncompleteModal("RichFaces.$('panelTituloNota').hide();");
//    		consultarAlunosPorTurma();
    		setTurmaDisciplinaNotaTituloVO(new TurmaDisciplinaNotaTituloVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    
    public void cancelarTituloNota(){
    	getTurmaDisciplinaNotaTituloVO().setTitulo(getTurmaDisciplinaNotaTituloVO().getTituloOriginal());
    }

	public Boolean getPermiteInformarTituloNota() {
			try{
				return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermiteInformarTituloNota", getUsuarioLogado());
			}catch(Exception e){
				setMensagemDetalhada("msg_erro", e.getMessage());
				return false;
			}
	}
	
	public FormaReplicarNotaOutraDisciplinaEnum getFormaReplicarNotaOutraDisciplina() {
		if(formaReplicarNotaOutraDisciplina == null){
			formaReplicarNotaOutraDisciplina = FormaReplicarNotaOutraDisciplinaEnum.TODAS;
		}
		return formaReplicarNotaOutraDisciplina;
	}

	public void setFormaReplicarNotaOutraDisciplina(FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina) {
		this.formaReplicarNotaOutraDisciplina = formaReplicarNotaOutraDisciplina;
	}

	public List<SelectItem> getListaSelectItemFormaReplicarNotaOutraDisciplina() {
		if(listaSelectItemFormaReplicarNotaOutraDisciplina == null){
			listaSelectItemFormaReplicarNotaOutraDisciplina =  new ArrayList<SelectItem>(0);
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.TODAS, FormaReplicarNotaOutraDisciplinaEnum.TODAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS.getValorApresentar()));
		}
		return listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public void setListaSelectItemFormaReplicarNotaOutraDisciplina(
			List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina) {
		this.listaSelectItemFormaReplicarNotaOutraDisciplina = listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public TipoNotaConceitoEnum getTipoNotaReplicar() {
		return tipoNotaReplicar;
	}

	public void setTipoNotaReplicar(TipoNotaConceitoEnum tipoNotaReplicar) {
		this.tipoNotaReplicar = tipoNotaReplicar;
	}

	public void gravarReplicacaoNotaOutraDisciplina(){
		try{			
			getFacadeFactory().getHistoricoFacade().realizarReplicacaoNota(getAlunosTurma(), getConfiguracaoAcademicoVO(), getTipoNotaReplicar(), getFormaReplicarNotaOutraDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}  

	private List<HistoricoVO> listaHistoricoMinhasNotasAlunoVOs;	
	private MatriculaVO matriculaVO;
	

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<HistoricoVO> getListaHistoricoMinhasNotasAlunoVOs() {
		return listaHistoricoMinhasNotasAlunoVOs;
	}

	public void setListaHistoricoMinhasNotasAlunoVOs(List<HistoricoVO> listaHistoricoMinhasNotasAlunoVOs) {
		this.listaHistoricoMinhasNotasAlunoVOs = listaHistoricoMinhasNotasAlunoVOs;
	}

	public void consultarMinhasNotasAluno() {
		try {
			HistoricoVO historicoVO = (HistoricoVO)getRequestMap().get("historicoItens");
			setMatriculaVO(historicoVO.getMatricula());
			setListaHistoricoMinhasNotasAlunoVOs(getFacadeFactory().getHistoricoFacade().executarMontagemListaHistoricoAluno(historicoVO.getMatricula(), 0, historicoVO.getMatricula().getCurso().getConfiguracaoAcademico(), null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(historicoVO.getMatricula().getUnidadeEnsino().getCodigo()), 
					historicoVO.getAnoSemestreApresentar(), false, false, false ,getUsuarioLogado()));
			boolean permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica = 
					ControleAcesso.verificarPermissaoFuncionalidadeUsuario(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_APRESENTAR_TODAS_NOTAS_PARAMETRIZADAS_CONFIGURACAO_ACADEMICA.getValor(), getUsuarioLogado());
			getFacadeFactory().getHistoricoFacade().realizarCriacaoDescricaoNotasParciaisHistoricoVOs(getListaHistoricoMinhasNotasAlunoVOs(), permitirApresentarTodasNotasParametrizadasConfiguracaoAcademica, getUsuarioLogado());			
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		 
	}
	
	public void consultarFaltasAluno() {
		try {
			setListaDetalhesMinhasFaltasVOs(new ArrayList<RegistroAulaVO>(0));
			HistoricoVO historicoVO = (HistoricoVO)getRequestMap().get("historicoItens");
			setMatriculaVO(historicoVO.getMatricula());			
			if(!Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica()) && !Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaPratica().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
			if(Uteis.isAtributoPreenchido(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica())) {
				getListaDetalhesMinhasFaltasVOs().addAll(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(historicoVO.getMatriculaPeriodoTurmaDisciplina().getTurmaTeorica().getCodigo(), getMatriculaVO().getMatricula(), historicoVO.getDisciplina().getCodigo(), historicoVO.getSemestreHistorico(), historicoVO.getAnoHistorico(), false, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs;
	
	/**
	 * @return the listaDetalhesMinhasFaltasVOs
	 */
	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
		if (listaDetalhesMinhasFaltasVOs == null) {
			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
		}
		return listaDetalhesMinhasFaltasVOs;
	}

	/**
	 * @param listaDetalhesMinhasFaltasVOs
	 *            the listaDetalhesMinhasFaltasVOs to set
	 */
	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
	}

	public TurmaDisciplinaNotaParcialVO getTurmaDisciplinaNotaParcial() {
		if(turmaDisciplinaNotaParcial == null) {
			turmaDisciplinaNotaParcial= new TurmaDisciplinaNotaParcialVO();
		}
		return turmaDisciplinaNotaParcial;
	}

	public void setTurmaDisciplinaNotaParcial(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial) {
		this.turmaDisciplinaNotaParcial = turmaDisciplinaNotaParcial;
	}

	public void adicionarTurmaDisciplinaNotaParcialItem(){
		try {
			
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().adicionarTurmaDisciplinaNotaParcialItem(getTurmaDisciplinaNotaParcial(), getTurmaDisciplinaNotaTituloVO());
			
			setTurmaDisciplinaNotaParcial(new TurmaDisciplinaNotaParcialVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerTurmaDisciplinaNotaParcialItem() {
		
		TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO = (TurmaDisciplinaNotaParcialVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaNotaParcialItens");
		try {
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().removerTurmaDisciplinaNotaParcialItem(turmaDisciplinaNotaParcialVO, getTurmaDisciplinaNotaTituloVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		

	}	
		
	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialVOs() {
		if(historicoNotaParcialVOs == null) {
			historicoNotaParcialVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialVOs;
	}

	public void setHistoricoNotaParcialVOs(List<HistoricoNotaParcialVO> historicoNotaParcialVOs) {
		this.historicoNotaParcialVOs = historicoNotaParcialVOs;
	}

	public void buscarHistoricoParcialNota(String tipoNota) throws Exception {

		setHistoricoTemporarioVO((HistoricoVO) context().getExternalContext().getRequestMap().get("historicoItens"));
		setHistoricoNotaParcialVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistorico(getHistoricoTemporarioVO(), tipoNota, getUsuarioLogado(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
		setarTituloNotaApresentar(tipoNota);
	}
	
	public void alterarHistoricosNotaParcial() {
		
		try {
			getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().alterarHistoricosNotaParcial(getHistoricoNotaParcialVOs(), getHistoricoTemporarioVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
		
	}

	public HistoricoVO getHistoricoTemporarioVO() {
		if (historicoTemporarioVO == null) {
			historicoTemporarioVO = new HistoricoVO();
		}
		return historicoTemporarioVO;
	}

	public void setHistoricoTemporarioVO(HistoricoVO historicoTemporarioVO) {
		this.historicoTemporarioVO = historicoTemporarioVO;
	}
	
	public void buscarHistoricoParcialNotaGeral(String tipoNota) throws Exception {
		getHistoricoNotaParcialGeralVOs().clear();

		for (HistoricoVO obj : getAlunosTurma()) {
			obj.setHistoricoNotaParcialNotaVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistorico(obj, tipoNota, getUsuarioLogado(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
		}
		setTipoNotaUsar(tipoNota);
		setarTituloNotaApresentar(tipoNota);

	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialGeralVOs() {
		if (historicoNotaParcialGeralVOs == null) {
			historicoNotaParcialGeralVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialGeralVOs;
	}

	public void setHistoricoNotaParcialGeralVOs(List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs) {
		this.historicoNotaParcialGeralVOs = historicoNotaParcialGeralVOs;
	}
	
	public void alterarHistoricosNotaParcialGeral(){
		try {			
			for (HistoricoVO obj : getAlunosTurma()) {
				if(Uteis.isAtributoPreenchido(obj.getHistoricoNotaParcialNotaVOs())) {					
					getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().alterarHistoricosNotaParcial(obj.getHistoricoNotaParcialNotaVOs(), obj, getUsuarioLogado());
				}
			}		
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}	
		
	}

	public String getTipoNotaUsar() {
		if (tipoNotaUsar == null) {
			tipoNotaUsar = "";
		}
		return tipoNotaUsar;
	}

	public void setTipoNotaUsar(String tipoNotaUsar) {
		this.tipoNotaUsar = tipoNotaUsar;
	}
	
	public Boolean buscarTurmaDisciplinaNotaParcial(String tipoNota) {
		List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcialVO = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
		try {
			listTurmaDisciplinaNotaParcialVO = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplinaTipoNota(getTurmaVO(), getDisciplinaApresentar(), tipoNota, getAno(), getSemestre(), getConfiguracaoAcademicoVO().getCodigo(),  getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcialVO)) {				
				return true;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	
		}
		return false;
		
	}

	public String getTituloNotaApresentar() {
		if (tituloNotaApresentar == null) {
			tituloNotaApresentar = "";
		}
		return tituloNotaApresentar;
	}

	public void setTituloNotaApresentar(String tituloNotaApresentar) {
		this.tituloNotaApresentar = tituloNotaApresentar;
	}

	public void setarTituloNotaApresentar(String tipoNota) {		
		setTituloNotaApresentar(getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().setarTituloNotaApresentar(tipoNota, getConfiguracaoAcademicoVO().getTurmaDisciplinaNotaTitulo1().getTituloNotaApresentar()));
	}	
		
	public Boolean getPermiteDefinirDetalhamentoNota() {
			try{
				return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("LancamentoNota_PermiteDefinirDetalhamentoNota", getUsuarioLogado());
			}catch(Exception e){
				setMensagemDetalhada("msg_erro", e.getMessage());
				return false;
			}		
	}
	
	public int getTamanhoPopupPanel() {
		if(getPermiteDefinirDetalhamentoNota()) {
			return 600;
		}
		else {
			return 200;
		}
	}
	
	public Boolean getNotaAptoApresentar(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "nota"+nrNota+"AptoApresentar");
		
	}
	
	public Boolean getIsNotaMedia(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "isNota"+nrNota+"Media");
	}

}